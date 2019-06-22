package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ILeaveMaster;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.leave.LeaveVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONLeaveController extends JSONMultiActionController {
	private static final String TOTAL = "total";
	private static final String BLANK2 = "blank";
	private static final String FULL_DAY_LEAVE = "Full Day Leave";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.JSONLeaveController");
	private ILeaveMaster leaveService;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
	private String USER_LIST = "getUserList";
	private String ACTION = "action";
	private String USER_MASTER = "userMaster";
	private String USER_NAME = "userName";
	private String GET_USER_MSG = "getUserMsg";
	private String USER_MSG = "userMsg";
	private String LEAVE_LIST = "leaveList";
	private String GET_LEAVE_LIST = "getLeaveList";
	private String LEAVE_TYPE = "leaveType";
	private String FROM_DATE = "fromDate";
	private String TO_DATE = "toDate";

	public void setLeaveService(ILeaveMaster leaveService) {
		this.leaveService = leaveService;
	}

	public ModelAndView leave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new ArrayList();
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			String action = request.getParameter(this.ACTION);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List roleList = userDetailsBean.getRoleList();
			this.logger.debug(" JSONLeaveController.leave() " + roleList);
			this.logger.debug(" user name  " + userBean.getUserName());
			String userName = null;
			String startDate = null;
			String endDate = null;
			String reportsTo = null;
			String sortColumn = null;
			String sortType = null;
			startDate = request.getParameter("startDate");
			userName = request.getParameter(this.USER_NAME);
			endDate = request.getParameter("endDate");
			sortColumn = request.getParameter("sort");
			sortType = request.getParameter("dir");
			if (action.equalsIgnoreCase(this.GET_LEAVE_LIST)) {
				this.logger.debug("request for leave list ");
				new ArrayList();
				int totalCount = false;
				this.logger.debug("roleList.contains(Constants.ROLE_R1) " + roleList.contains("R1"));
				if (!roleList.contains("R1")) {
					reportsTo = userBean.getUserName();
				}

				List<LeaveVO> leaveList = this.leaveService.getLeaves(userName, startDate, endDate, reportsTo,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), sortColumn, sortType);
				int totalCount = this.leaveService.getLeavesCount(userName, startDate, endDate, reportsTo);
				viewForJson.addObject(this.LEAVE_LIST, leaveList);
				viewForJson.addObject("total", totalCount);
				return viewForJson;
			} else {
				List userMasterVOList;
				if (action.equalsIgnoreCase(this.USER_LIST)) {
					this.logger.debug(" getting user list");
					if (roleList.contains("R1")) {
						userMasterVOList = ResourceLocator.self().getUserService().getActiveUserList();
						this.logger.debug(" user master vo list " + userMasterVOList);
					} else {
						userMasterVOList = ResourceLocator.self().getUserService()
								.getSubOrdinateList(userBean.getUserName());
						UserMasterVO vo = ResourceLocator.self().getUserService().getUserInfo(userBean.getUserName());
						UserMasterVO userMasterVO = new UserMasterVO();
						userMasterVO.setUserFullName(vo.getUsername());
						userMasterVO.setUsername(vo.getUserID());
						userMasterVOList.add(userMasterVO);
					}

					viewForJson.addObject(this.USER_MASTER, userMasterVOList);
					return viewForJson;
				} else if (!action.equalsIgnoreCase(this.GET_USER_MSG)) {
					return viewForJson;
				} else {
					List<LeaveVO> userTableList = new ArrayList();
					LeaveVO leaveVO = new LeaveVO();
					String blank = request.getParameter("blank");
					this.logger.debug(" get list of user table blank " + blank);
					Iterator iterator;
					UserMasterVO userVO;
					if (null != blank && !blank.equals("")) {
						userMasterVOList = ResourceLocator.self().getUserService().getBackUpList(userName);
						iterator = userMasterVOList.iterator();

						while (iterator.hasNext()) {
							userVO = (UserMasterVO) iterator.next();
							leaveVO.setBackup1(userVO.getBackup1());
							leaveVO.setBackup2(userVO.getBackup2());
							leaveVO.setUserFullName(userVO.getUserFullName());
						}

						leaveVO.setUserId(request.getParameter(this.USER_NAME));
						leaveVO.setUserName(userName);
						leaveVO.setUsername(userName);
						if (null != request.getParameter(this.LEAVE_TYPE)
								&& !request.getParameter(this.LEAVE_TYPE).equals("")) {
							leaveVO.setLeaveType(request.getParameter(this.LEAVE_TYPE));
						} else {
							leaveVO.setLeaveType("Full Day Leave");
						}

						leaveVO.setFromDate(request.getParameter(this.FROM_DATE));
						leaveVO.setToDate(request.getParameter(this.TO_DATE));
					} else {
						userMasterVOList = ResourceLocator.self().getUserService()
								.getBackUpList(userBean.getUserName());
						iterator = userMasterVOList.iterator();

						while (iterator.hasNext()) {
							userVO = (UserMasterVO) iterator.next();
							leaveVO.setBackup1(userVO.getBackup1());
							leaveVO.setBackup2(userVO.getBackup2());
							leaveVO.setUserFullName(userVO.getUserFullName());
						}

						leaveVO.setUserId(userBean.getUserName());
						leaveVO.setUserName(userBean.getUserName());
						leaveVO.setUsername(userBean.getUserName());
						leaveVO.setFromDate(this.sdf.format(new Date()));
						leaveVO.setToDate(this.sdf.format(new Date()));
						leaveVO.setLeaveType("Full Day Leave");
					}

					userTableList.add(leaveVO);
					viewForJson.addObject(this.USER_MSG, userTableList);
					return viewForJson;
				}
			}
		} catch (Exception var20) {
			return AtlasUtils.getJsonExceptionView(this.logger, var20, response);
		}
	}

	public ModelAndView checkExistLeave(HttpServletRequest request, HttpServletResponse response, LeaveVO leaveVO)
			throws Exception {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			int count = this.leaveService.checkExistLeave(leaveVO);
			viewForJson.addObject("leaveCount", count);
			return viewForJson;
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView checkExistLeaveBulk(HttpServletRequest request, HttpServletResponse response, LeaveVO leaveVO)
			throws Exception {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		this.logger.debug(" inside to check leave exist for bulk ");

		try {
			this.logger.debug(" leave vo users " + leaveVO.getUserId());
			this.logger.debug(" leave vo fromdate " + leaveVO.getFromDate());
			this.logger.debug(" leave vo todate " + leaveVO.getToDate());
			int count = this.leaveService.checkExistLeaveBulk(leaveVO);
			viewForJson.addObject("leaveCount", count);
			return viewForJson;
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}
}