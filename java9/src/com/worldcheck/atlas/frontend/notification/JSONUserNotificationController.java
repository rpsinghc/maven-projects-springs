package com.worldcheck.atlas.frontend.notification;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.notification.NotificationManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONUserNotificationController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.notification.JSONUserNotificationController");
	private static final String USER_MASTER = "userMaster";
	private static final String USERNOTIFICATIONLIST = "userNotificationList";
	private static final String NOTIFICATION = "notification";
	private static final String NOTIFICATIONHISTORYLIST = "notificationHistory";
	private static final String SYSTEMNOTIFICATIONLIST = "systemNotificationList";
	private static final String MESSAGETRAIL = "messageTrail";
	private static final String ALERTID = "alertId";
	private static final String SENTTOLIST = "sentToList";
	private static final String USERS = "users";
	private static final String LEAVEMESSAGE = "leaveMessage";
	private NotificationManager notificationManager;
	private static final String RECNOTIFICATIONCOUNT = "recnotificationcount";
	private static final String SYSTEMNOTIFICATIONCOUNT = "systemnotificationcount";
	private static final String USERNOTIFICATIONCOUNT = "usernotificationcount";

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public ModelAndView getNotificatiosnAsssignedUsers(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();
		String crn = "";

		List userMasterVOListFinal;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			}

			userMasterVOListFinal = this.notificationManager.getAssignedUsers(crn, userBean.getUserName());
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		viewForJson.addObject("userMaster", userMasterVOListFinal);
		return viewForJson;
	}

	public ModelAndView getNotificatiosnAllUsers(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();
		List<UserMasterVO> userMasterVOListFinal = new ArrayList();
		String var7 = "";

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (request.getParameter("crn") != null) {
				var7 = request.getParameter("crn");
			}

			List<UserMasterVO> userMasterVOList = ResourceLocator.self().getUserService().getActiveUserList();
			Iterator iterator = userMasterVOList.iterator();

			while (iterator.hasNext()) {
				UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
				if (!userMasterVO.getUsername().equalsIgnoreCase(userBean.getUserName())) {
					userMasterVOListFinal.add(userMasterVO);
				}
			}
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}

		viewForJson.addObject("userMaster", userMasterVOListFinal);
		return viewForJson;
	}

	public ModelAndView getUserNotifications(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		this.logger.debug("Entirng JSOnUserNotificationController: getUserNotifications");
		ModelAndView mv = new ModelAndView("jsonView");
		new ArrayList();
		boolean var6 = false;

		List userNotificationList;
		int count;
		try {
			int start = 0;
			int limit = 10;
			String defaultSortingOrder = "DESC";
			String defaultSortingColumn = "recvTime";
			if (request.getParameterMap().size() != 0 && request.getParameter("start") != null
					&& request.getParameter("limit") != null && request.getParameter("sort") != null
					&& request.getParameter("dir") != null) {
				this.logger.debug("Else.. not null");
				start = Integer.parseInt(request.getParameter("start"));
				limit = Integer.parseInt(request.getParameter("limit"));
				defaultSortingOrder = request.getParameter("dir");
				defaultSortingColumn = request.getParameter("sort");
			} else {
				this.logger.debug("Getting null..");
			}

			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			userNotificationList = ResourceLocator.self().getNotificationService().getUserNotification(
					userBean.getUserName(), start, limit, defaultSortingColumn, defaultSortingOrder, notificationVO);
			count = ResourceLocator.self().getNotificationService().getUserNotificationCount(userBean.getUserName(),
					notificationVO);
			this.logger.debug(" Number of message " + userNotificationList.size());
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}

		mv.addObject("userNotificationList", userNotificationList);
		mv.addObject("total", count);
		return mv;
	}

	public ModelAndView getSystemNotifications(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		this.logger.debug("Inside getSystemNotifications Method");
		ModelAndView mv = new ModelAndView("jsonView");
		this.logger.debug("testing json call" + notificationVO.getCrn() + notificationVO.getMessage()
				+ notificationVO.getSentFromTime() + notificationVO.getSentToTime() + notificationVO.getPrimarySub()
				+ notificationVO.getFilter() + notificationVO.getCaseStatus());
		new ArrayList();
		boolean var6 = false;

		List systemNotificationList;
		int count;
		try {
			int start = 0;
			int limit = 10;
			String defaultSortingOrder = "DESC";
			String defaultSortingColumn = "recvTime";
			if (request.getParameterMap().size() != 0 && request.getParameter("start") != null
					&& request.getParameter("limit") != null && request.getParameter("sort") != null
					&& request.getParameter("dir") != null) {
				this.logger.debug("Else.. not null");
				start = Integer.parseInt(request.getParameter("start"));
				limit = Integer.parseInt(request.getParameter("limit"));
				defaultSortingOrder = request.getParameter("dir");
				defaultSortingColumn = request.getParameter("sort");
			} else {
				this.logger.debug("Getting null..");
			}

			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			systemNotificationList = ResourceLocator.self().getNotificationService().getSystemNotification(
					userBean.getUserName(), start, limit, defaultSortingColumn, defaultSortingOrder, notificationVO);
			count = ResourceLocator.self().getNotificationService().getSystemNotificationCount(userBean.getUserName(),
					notificationVO);
			this.logger.debug(" Number of system message " + systemNotificationList.size());
			this.logger.debug("getSystemNotifications:::::::" + count);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}

		mv.addObject("systemNotificationList", systemNotificationList);
		mv.addObject("total", count);
		return mv;
	}

	public ModelAndView getRecCaseSystemNotifications(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		this.logger.debug("In get recurrence notfication controller");
		ModelAndView mv = new ModelAndView("jsonView");
		this.logger.debug("Search filter values: " + notificationVO.getNotificationFromDate()
				+ notificationVO.getNotificationToDate() + notificationVO.getClientCode()
				+ notificationVO.getReportType() + notificationVO.getFilter());
		new ArrayList();
		boolean var6 = false;

		List systemNotificationList;
		int count;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			systemNotificationList = ResourceLocator.self().getNotificationService().getRecCaseSystemNotifications(
					userBean.getUserName(), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
					request.getParameter("dir"), notificationVO);
			count = ResourceLocator.self().getNotificationService()
					.getRecCaseSystemNotificationCount(userBean.getUserName(), notificationVO);
			this.logger.debug(" Number of rec system message " + systemNotificationList.size());
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		mv.addObject("systemNotificationList", systemNotificationList);
		mv.addObject("total", count);
		return mv;
	}

	public ModelAndView updateFlag(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			int userCount = false;
			int systemCount = 0;
			String loginUserId = request.getParameter("loginUserId");
			this.logger.debug("updateFlag  " + loginUserId);
			int userCount = ResourceLocator.self().getNotificationService().updateFlag(loginUserId);
			if (userCount <= 0 && systemCount <= 0) {
				modelAndView.addObject("Status", "Falure");
			} else {
				modelAndView.addObject("Status", "Success");
			}
		} catch (Exception var7) {
			AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return modelAndView;
	}

	public ModelAndView getCRNUserNotifications(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("jsonView");
		new ArrayList();
		int totalCount = false;
		String crn = "";

		List userNotificationList;
		int totalCount;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			}

			String sortOrder = request.getParameter("sortOrder");
			this.logger.debug("sort order " + sortOrder);
			totalCount = ResourceLocator.self().getNotificationService()
					.getNotificationHistoryCount(userBean.getUserName(), crn);
			userNotificationList = ResourceLocator.self().getNotificationService().getNotificationHistory(
					userBean.getUserName(), crn, Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), sortOrder);
			this.logger.debug(" Number of message " + userNotificationList.size());
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		mv.addObject("notificationHistory", userNotificationList);
		mv.addObject("total", totalCount);
		return mv;
	}

	public ModelAndView getNotificationSentToList(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("jsonView");
		new ArrayList();
		boolean var6 = false;

		List sentToList;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			sentToList = this.notificationManager.getSentToList(notificationVO, userBean.getUserName());
			this.logger.debug(" Number of user " + sentToList.size());
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		mv.addObject("sentToList", sentToList);
		return mv;
	}

	public ModelAndView getMessageTrail(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("jsonView");
		new ArrayList();
		String crn = "";
		String alertId = "";

		List userNotificationList;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			}

			if (request.getParameter("alertId") != null) {
				this.logger.debug("alert id " + request.getParameter("alertId"));
				alertId = request.getParameter("alertId");
			}

			this.logger.debug("alert id " + request.getParameter("alertId"));
			userNotificationList = ResourceLocator.self().getNotificationService().getMessageTrail(alertId,
					userBean.getUserName());
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		mv.addObject("messageTrail", userNotificationList);
		return mv;
	}

	public ModelAndView getNotificationsCount(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("Inside the getNotificationsCount method");
		ModelAndView mv = new ModelAndView("jsonView");
		int usercount = false;
		int syscount = false;
		int reccount = false;
		new ArrayList();
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		int usercount = ResourceLocator.self().getNotificationService()
				.getUserNotificationCountWithoutFilter(userBean.getUserName());
		int syscount = ResourceLocator.self().getNotificationService()
				.getSystemNotificationCountWithoutFilter(userBean.getUserName());
		int reccount = ResourceLocator.self().getNotificationService()
				.getRecCaseSystemNotificationCountWithoutFilter(userBean.getUserName());
		mv.addObject("systemnotificationcount", syscount);
		mv.addObject("usernotificationcount", usercount);
		mv.addObject("recnotificationcount", reccount);
		return mv;
	}
}