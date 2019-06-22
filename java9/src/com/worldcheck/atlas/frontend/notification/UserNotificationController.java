package com.worldcheck.atlas.frontend.notification;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.notification.NotificationManager;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UserNotificationController extends MultiActionController {
	private static final String THESE_RECIPIENTS_ARE_ON_LEAVE = "These recipients is/are on leave ";
	private static final String USER_LEAVE_MESSAGE = "userLeaveMessage";
	private static final String MESSAGE = "message";
	private static final String GRIDTODISPLAY = "gridtodisplaytwo";
	private static final String PREV_PAGE = "prevPage";
	private static final String NOTIFICATION_PAGE = "notification";
	private static final String NOTIFICATIONREPLY_PAGE = "notification";
	private static final String USERMESSAGE_PAGE = "case_notifications";
	private static final String ALERTNAMES = "alertNames";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.notification.UserNotificationController");
	private NotificationManager notificationManager;

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public ModelAndView notification(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;
		this.logger.debug("redirecting to user notifications page");
		modelAndView = new ModelAndView("notification");
		if (request.getSession().getAttribute("message") != null) {
			modelAndView.addObject("message", request.getSession().getAttribute("message"));
			modelAndView.addObject("gridtodisplaytwo", request.getSession().getAttribute("gridtodisplaytwo"));
			this.logger.debug("message in user notification " + request.getSession().getAttribute("message"));
			this.logger
					.debug("tab to load in user notification " + request.getSession().getAttribute("gridtodisplaytwo"));
			request.getSession().removeAttribute("message");
			request.getSession().removeAttribute("gridtodisplaytwo");
		}

		return modelAndView;
	}

	public ModelAndView caseNotification(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = "";
			modelAndView = new ModelAndView("case_notifications");
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			} else if (request.getSession().getAttribute("crn") != null) {
				crn = (String) request.getSession().getAttribute("crn");
				request.getSession().removeAttribute("crn");
			}

			if (request.getSession().getAttribute("userLeaveMessage") != null) {
				modelAndView.addObject("userLeaveMessage", request.getSession().getAttribute("userLeaveMessage"));
				request.getSession().removeAttribute("userLeaveMessage");
			}

			this.logger.debug("crn is notification " + crn);
			String isFromComments = "";
			String commentData = "";
			if (request.getParameter("isFromComments") != null && request.getParameter("hdnCommentData") != null) {
				isFromComments = request.getParameter("isFromComments");
				commentData = request.getParameter("hdnCommentData");
			}

			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("isFromComments", isFromComments);
			modelAndView.addObject("commentData", commentData);
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView sendUserNotification(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		String message = "Message Sent successfully.";
		this.logger.debug("  sending notification  ");
		String title = notificationVO.getName();
		String recipient = notificationVO.getRecipient();
		String description = notificationVO.getMessage();
		this.logger.debug("message is " + description);
		String crn = "";
		if (request.getParameter("crn") != null) {
			crn = request.getParameter("crn");
		}

		this.logger.debug("recp " + recipient);
		new ArrayList();
		String userLeaveMessage = "";

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			List<String> userList = StringUtils.commaSeparatedStringToList(recipient);
			ResourceLocator.self().getNotificationService().createUserNotification(userBean.getUserName(), title,
					description, (String) null, (String) null, userList, crn);
			userLeaveMessage = ResourceLocator.self().getLeaveService().getUserOnLeaveList(userList);
		} catch (Exception var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}

		ModelAndView mv = new ModelAndView("redirect:caseNotification.do");
		if (!userLeaveMessage.equals("")) {
			message = message + " " + "These recipients is/are on leave " + userLeaveMessage + ".";
		}

		request.getSession().setAttribute("userLeaveMessage", message);
		request.getSession().setAttribute("crn", crn);
		return mv;
	}

	public ModelAndView sendUserNotificationWithComments(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		String message = "Message Sent successfully.";
		this.logger.debug("  sending notification  ");
		String title = notificationVO.getName();
		String recipient = notificationVO.getRecipient();
		String recipientFullName = "";
		String description = notificationVO.getMessage();
		String crn = "";
		if (request.getParameter("crn") != null) {
			crn = request.getParameter("crn");
		}

		if (request.getParameter("recipientFullName") != null) {
			recipientFullName = request.getParameter("recipientFullName");
		}

		this.logger.debug("recp " + recipient);
		new ArrayList();
		String userLeaveMessage = "";

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			List<String> userList = StringUtils.commaSeparatedStringToList(recipient);
			String sentFrom = "";
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			if (request.getSession().getAttribute("loginLevel") != null) {
				sentFrom = (String) request.getSession().getAttribute("performedBy");
				ResourceLocator.self().getNotificationService().createUserNotificationWithComments(
						userDetailsBean.getLoginUserId(), title, description, (String) null, (String) null, userList,
						crn, recipientFullName, sentFrom, true);
			} else {
				ResourceLocator.self().getNotificationService().createUserNotificationWithComments(
						userDetailsBean.getLoginUserId(), title, description, (String) null, (String) null, userList,
						crn, recipientFullName, sentFrom, false);
			}

			userLeaveMessage = ResourceLocator.self().getLeaveService().getUserOnLeaveList(userList);
			request.getSession().setAttribute("crn", crn);
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}

		ModelAndView mv = new ModelAndView("redirect:viewCommentsPage.do");
		if (!userLeaveMessage.equals("")) {
			message = message + " " + "These recipients is/are on leave " + userLeaveMessage + ".";
		}

		request.getSession().setAttribute("userLeaveMessage", message);
		return mv;
	}

	public ModelAndView acknowledgeMessage(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("redirect:notification.do");
		String message = "Acknowledged successfully.";
		String alertNames = "";
		if (request.getParameter("alertNames") != null) {
			alertNames = request.getParameter("alertNames");
		}

		this.logger.debug("alert name " + alertNames);
		String gridtodisplaytwo = "";
		gridtodisplaytwo = request.getParameter("gridtodisplaytwo");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (alertNames != null && !alertNames.equalsIgnoreCase("")) {
				ResourceLocator.self().getNotificationService().acknowledgeNotification(alertNames,
						userBean.getUserName());
			}
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		request.getSession().setAttribute("message", message);
		request.getSession().setAttribute("gridtodisplaytwo", gridtodisplaytwo);
		return mv;
	}

	public ModelAndView replyMessage(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("redirect:notification.do");
		String message = "Notification Message Reply Sent successfully.";

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("parametes " + request.getParameterMap());
			this.logger.debug(" id " + notificationVO.getId());
			this.logger.debug(" message " + notificationVO.getMessage());
			this.logger.debug(" name " + notificationVO.getName());
			this.logger.debug(" rece " + notificationVO.getRecipient());
			this.logger.debug(" PREV_PAGE " + request.getParameter("prevPage"));
			mv.addObject("prevPage", request.getParameter("prevPage"));
			ResourceLocator.self().getNotificationService().replyUserNotification(userBean.getUserName(),
					notificationVO);
			String crn = "";
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			}

			mv.addObject("crn", crn);
			if (request.getParameter("prevPage") != null && !"".equalsIgnoreCase(request.getParameter("prevPage"))) {
				request.getSession().setAttribute("userLeaveMessage", message);
				request.getSession().setAttribute("crn", crn);
				mv = new ModelAndView("redirect:caseNotification.do");
			} else {
				request.getSession().setAttribute("message", message);
			}

			return mv;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView forwardMessage(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("redirect:notification.do");
		String message = "Notification message Forwarded successfully.";
		new ArrayList();

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("parameters " + request.getParameterMap());
			this.logger.debug(" id " + notificationVO.getId());
			this.logger.debug(" message " + notificationVO.getMessage());
			this.logger.debug(" name " + notificationVO.getName());
			this.logger.debug(" rece " + notificationVO.getRecipient());
			mv.addObject("prevPage", request.getParameter("prevPage"));
			ResourceLocator.self().getNotificationService().forwardUserNotification(userBean.getUserName(),
					notificationVO);
			String crn = "";
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			}

			mv.addObject("crn", crn);
			if (request.getParameter("prevPage") != null && !"".equalsIgnoreCase(request.getParameter("prevPage"))) {
				request.getSession().setAttribute("userLeaveMessage", message);
				request.getSession().setAttribute("crn", crn);
				mv = new ModelAndView("redirect:caseNotification.do");
			} else {
				request.getSession().setAttribute("message", message);
			}

			return mv;
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView recAcknowledgeMessage(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("redirect:notification.do");
		String message = "Acknowledged successfully.";
		String alertNames = "";
		String gridtodisplaytwo = "";
		gridtodisplaytwo = request.getParameter("gridtodisplaytwo");
		if (request.getParameter("alertNames") != null) {
			alertNames = request.getParameter("alertNames");
		}

		this.logger.debug("alert name " + alertNames);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (alertNames != null && !alertNames.equalsIgnoreCase("")) {
				this.logger.debug("going to acknowledge rec case");
				this.notificationManager.acknowledgeRecNotification(alertNames, userBean.getUserName());
			}
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		request.getSession().setAttribute("message", message);
		request.getSession().setAttribute("gridtodisplaytwo", gridtodisplaytwo);
		return mv;
	}

	public ModelAndView createRecCase(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("redirect:notification.do");
		String message = "Case Created Successfully.";
		String gridtodisplaytwo = request.getParameter("gridtodisplaytwo");
		this.logger.debug("tab to load in user notification two:::::: " + gridtodisplaytwo);
		String recCaseSchedulerId = "";
		if (request.getParameter("recCaseSchedulerId") != null) {
			recCaseSchedulerId = request.getParameter("recCaseSchedulerId");
		}

		String recClientCaseId = "";
		if (request.getParameter("recClientCaseId") != null) {
			recClientCaseId = request.getParameter("recClientCaseId");
		}

		this.logger.debug("recClientCaseId " + recClientCaseId + " recCaseSchedulerId " + recCaseSchedulerId);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (recClientCaseId != null && !recClientCaseId.equalsIgnoreCase("")) {
				this.logger.debug("Going to create case");
				this.notificationManager.createRecCase(recClientCaseId, recCaseSchedulerId, userBean.getUserName(),
						SBMUtils.getSession(request));
			}
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}

		request.getSession().setAttribute("message", message);
		request.getSession().setAttribute("gridtodisplaytwo", gridtodisplaytwo);
		return mv;
	}
}