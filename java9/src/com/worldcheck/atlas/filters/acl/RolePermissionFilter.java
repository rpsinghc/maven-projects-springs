package com.worldcheck.atlas.filters.acl;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.tdiinc.common.SessionManagement;
import com.worldcheck.atlas.cache.service.CacheService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RolePermissionFilter implements Filter {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.filters.acl.RolePermissionFilter");
	private static SecureRandom random = new SecureRandom();
	private static final String WCPAGE = "wcPage";
	private static final String LOGOUT = "logout";
	private static final String ERROR_CODE = "errorCode";
	private static final String ONE = "1";
	private String REDIRECT_PAGE = "/sbm/bpmportal/login.jsp";
	private String errorMessageCode = "CMS.message011";
	private String excludePatterns = "";

	public void destroy() {
	}

	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
			throws IOException, ServletException {
		String path = ((HttpServletRequest) servletrequest).getServletPath();
		if (this.excludeFromFilter(path)) {
			filterchain.doFilter(servletrequest, servletresponse);
		} else {
			UserDetailsBean userDetailsBean = null;
			this.logger.debug("REQUEST PARAMETERS IN ROLE PERMISSION FILTER::: " + servletrequest.getParameterMap());
			HttpServletResponse response = (HttpServletResponse) servletresponse;
			HttpSession session = SessionManagement.getSession((HttpServletRequest) servletrequest);
			Map hsMAP = null;
			Long userId = null;

			try {
				if (session != null) {
					UserBean userBean = (UserBean) session.getAttribute("userBean");
					if (userBean != null) {
						String username = userBean.getUserName();
						this.logger.debug("User name is " + username);
						userId = (Long) session.getAttribute("user_Id");
						if (userId == null) {
							userId = ResourceLocator.self().getSBMService().getUserID(username);
							session.setAttribute("user_Id", userId);
						}

						userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
						if (!userId.equals(new Long(1L)) && userDetailsBean == null) {
							userDetailsBean = new UserDetailsBean();
							userDetailsBean.setLoggedAsBackup(false);
							userDetailsBean.setLoginUserId(userBean.getUserName());
							userDetailsBean.setMainUserId(userBean.getUserName());
							userDetailsBean.setToken((new BigInteger(130, random)).toString(32).toString());
							hsMAP = ResourceLocator.self().getACLService().getACLPermissions("" + userId);
							userDetailsBean.setPermissionMap((Map) hsMAP.get("permissionMap"));
							userDetailsBean.setRoleList((List) hsMAP.get("roleList"));
							session.setAttribute("userDetailsBean", userDetailsBean);
							List<TimeTrackerVO> trackerInfo = ResourceLocator.self().getTimeTrackerService()
									.isTimeTrackerOnForUser(userBean.getUserName());
							String workItem = null;

							try {
								UserMasterVO userMasterVO = ResourceLocator.self().getUserService()
										.getUserPassExpiryDetails(userDetailsBean.getLoginUserId());
								if ((userMasterVO.getIsNewUser() != 1
										|| !userMasterVO.getIsPasswordExpire().equalsIgnoreCase("0"))
										&& trackerInfo.size() > 0) {
									workItem = ResourceLocator.self().getSBMService().getworkItemName(
											Long.parseLong(((TimeTrackerVO) trackerInfo.get(0)).getTaskPid()),
											((TimeTrackerVO) trackerInfo.get(0)).getTaskName());
									((TimeTrackerVO) trackerInfo.get(0)).setWorkItemName(workItem);
									session.setAttribute("trackerBean", trackerInfo.get(0));
									session.setAttribute("TrackerOn", "TrackerOn");
									ResourceLocator.self().getCacheService().updateTTTokenCache(userBean.getUserName(),
											String.valueOf(((TimeTrackerVO) trackerInfo.get(0)).getTrackerId()));
								}
							} catch (CMSException var15) {
								this.logger.debug(
										"there is some error while showing the time tracker on at login time...workItem :: "
												+ workItem);
								if (workItem == null) {
									this.logger.debug("workItem is null so updating the end time in DB.");
									ResourceLocator.self().getTimeTrackerService()
											.stopTimeTracker((TimeTrackerVO) trackerInfo.get(0));
								}
							}

							this.manageLoginMapInCache(userDetailsBean);
						}

						if (session.getAttribute("trackerBean") != null && session.getAttribute("TrackerOn") != null) {
							String trackerId = ResourceLocator.self().getCacheService()
									.getTimeTrackerTokenCache(username);
							if (trackerId == null) {
								session.removeAttribute("trackerBean");
								session.removeAttribute("TrackerOn");
							}
						}

						if (!userId.equals(new Long(1L)) && userDetailsBean != null
								&& this.checkUserUpdateInCache(userDetailsBean)) {
							session.setAttribute("bizManage", (Object) null);
							ResourceLocator.self().getCacheService()
									.removeACLTokenCache(userDetailsBean.getLoginUserId());
							if (!response.isCommitted()) {
								response.sendRedirect("/sbm/bpmportal/updateRole.jsp");
							}
						}

						if (!userId.equals(new Long(1L)) && this.checkUserStatus(session, username)) {
							StringBuffer requestURL = ((HttpServletRequest) servletrequest).getRequestURL();
							session.setAttribute("bizManage", (Object) null);
							if (requestURL.toString().indexOf(".json") < 0 && requestURL.toString().indexOf(".ajax") < 0
									&& !response.isCommitted()) {
								response.sendRedirect(this.REDIRECT_PAGE);
							}
						}

						if (userDetailsBean.getPermissionMap() == null
								|| userDetailsBean.getPermissionMap().isEmpty()) {
							servletrequest.setAttribute("errorCode", this.errorMessageCode);
						}
					} else if (!response.isCommitted()) {
						response.sendRedirect(this.REDIRECT_PAGE);
					}
				}
			} catch (Exception var16) {
				new CMSException(this.logger, var16);
			}

			filterchain.doFilter(servletrequest, servletresponse);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.excludePatterns = arg0.getInitParameter("excludePatterns");
	}

	private void manageLoginMapInCache(UserDetailsBean userDetailsBean) throws CMSException {
		CacheService cacheService = ResourceLocator.self().getCacheService();
		HashMap<String, String> userTokenMap = new HashMap();
		userTokenMap.put(userDetailsBean.getLoginUserId(), userDetailsBean.getToken());
		cacheService.updateACLTokenCache(userTokenMap);
		this.logger.debug("after insertation userTokenMap" + userTokenMap);
	}

	private boolean checkUserStatus(HttpSession session, String loginId) throws CMSException {
		UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
		boolean userUpdateFlag = false;
		UserMasterVO masterVO = null;

		try {
			if (userDetailsBean != null) {
				masterVO = ResourceLocator.self().getUserService().getUserInfo(loginId);
				if (masterVO != null) {
					if ("1".equals(masterVO.getStatus())) {
						userUpdateFlag = this.checkUserStatusInCache(userDetailsBean);
					} else {
						userUpdateFlag = true;
					}
				}
			} else {
				this.logger.debug("Invalid user logged in(Not created properly) " + loginId);
				this.checkUserStatusInCache(userDetailsBean);
			}

			this.logger.debug("userUpdateFlag is " + userUpdateFlag);
			return userUpdateFlag;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private boolean checkUserStatusInCache(UserDetailsBean userDetailsBean) throws CMSException {
		boolean userUpdateFlag = false;
		Map<String, String> statusMap = ResourceLocator.self().getCacheService().getACLTokenCache();
		if (statusMap != null) {
			this.logger
					.debug("RolePermissionFilter.checkUserStatusInCache():::: Current Logged in User statusMap size is "
							+ statusMap.size());
			String token = (String) statusMap.get(userDetailsBean.getLoginUserId());
			if (token == null || !token.equals(userDetailsBean.getToken())) {
				userUpdateFlag = true;
			}
		} else if (statusMap != null && statusMap.isEmpty()) {
			userUpdateFlag = true;
		}

		this.logger.debug("userUpdate flag " + userUpdateFlag);
		return userUpdateFlag;
	}

	private boolean checkUserUpdateInCache(UserDetailsBean userDetailsBean) throws CMSException {
		this.logger.debug("Check User Update Incache ");
		boolean userUpdateFlag = false;
		HashMap<String, String> statusMap = ResourceLocator.self().getCacheService().getACLTokenCache();
		if (statusMap != null) {
			this.logger
					.debug("RolePermissionFilter.checkUserUpdateInCache():::: Current Logged in User statusMap size is "
							+ statusMap.size());
			String token = (String) statusMap.get(userDetailsBean.getLoginUserId());
			if (token != null && token.equals("RoleUpdated")) {
				userUpdateFlag = true;
			}
		}

		return userUpdateFlag;
	}

	private boolean excludeFromFilter(String path) {
		return path.contains(this.excludePatterns);
	}
}