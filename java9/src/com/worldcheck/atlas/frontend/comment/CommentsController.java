package com.worldcheck.atlas.frontend.comment;

import com.worldcheck.atlas.bl.interfaces.ICaseComments;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CommentsController extends MultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.comment.CommentsController";
	private ILogProducer logger;
	private static final String JSP_NAME = "caseComments_list";
	private static final String COMMENT_DATA_FIELD = "hdnCommentData";
	private static final String USER_LEAVE_MESSAGE = "userLeaveMessage";
	private static final String USER_LOGIN_ID = "userLoginId";
	private static final String CRN_STR = "crn";
	private static final String IS_CASE_CANCELLED_COMPLETED = "isCaseCompletedOrCancelled";
	private ICaseComments commentsManager;

	public CommentsController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
	}

	public void setCommentsManager(ICaseComments commentsManager) {
		this.commentsManager = commentsManager;
	}

	public ModelAndView viewCommentsPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("caseComments_list");
			String crn = "";
			if (null != request.getParameter("crn")) {
				crn = request.getParameter("crn");
			} else if (null != request.getSession().getAttribute("crn")) {
				crn = (String) request.getSession().getAttribute("crn");
				request.getSession().removeAttribute("crn");
			}

			if (request.getSession().getAttribute("userLeaveMessage") != null) {
				modelAndView.addObject("userLeaveMessage", request.getSession().getAttribute("userLeaveMessage"));
				request.getSession().removeAttribute("userLeaveMessage");
			}

			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			Integer caseStatus = 0;
			String isCaseCompletedOrCancelled = "";
			caseStatus = this.commentsManager.getCaseStatus(crn);
			if (caseStatus != 4 && caseStatus != 5 && caseStatus != 1) {
				isCaseCompletedOrCancelled = "false";
			} else {
				isCaseCompletedOrCancelled = "true";
			}

			modelAndView.addObject("userLoginId", userId);
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("isCaseCompletedOrCancelled", isCaseCompletedOrCancelled);
			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView insertComments(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("caseComments_list");

		try {
			String CRN = request.getParameter("crn");
			String commentMessage = request.getParameter("hdnCommentData");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String updatedBy = "";
			String sentFrom = "";
			updatedBy = userDetailsBean.getLoginUserId();
			if (request.getSession().getAttribute("loginLevel") != null) {
				sentFrom = (String) request.getSession().getAttribute("performedBy");
				this.commentsManager.insertComments(CRN, commentMessage, updatedBy, sentFrom, true);
			} else {
				this.commentsManager.insertComments(CRN, commentMessage, updatedBy, sentFrom, false);
			}

			modelAndView.addObject("userLoginId", updatedBy);
			modelAndView.addObject("crn", request.getParameter("crn"));
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}
}