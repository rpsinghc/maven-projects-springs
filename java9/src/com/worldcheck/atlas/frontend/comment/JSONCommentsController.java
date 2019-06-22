package com.worldcheck.atlas.frontend.comment;

import com.worldcheck.atlas.bl.interfaces.ICaseComments;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CommentDetails;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCommentsController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.comment.JSONCommentsController");
	private static final String COMMENT_LIST = "commentsList";
	private static final String COMMENT_ID = "commentId";
	private static final String USER_LOGIN_ID = "userLoginId";
	private static final String CRN_STR = "crn";
	private ICaseComments commentsManager;

	public void setCommentsManager(ICaseComments commentsManager) {
		this.commentsManager = commentsManager;
	}

	public ModelAndView getCommentsList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN JSONCommentsController.getCommentsList");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String CRN = request.getParameter("crn");
			new ArrayList();
			List<CommentDetails> commentsList = this.commentsManager.getComments(CRN,
					Integer.parseInt(request.getParameter("start")), request.getParameter("sort"),
					request.getParameter("dir"), Integer.parseInt(request.getParameter("limit")));
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			this.logger.debug("commentsList size :- " + commentsList.size());
			modelAndView.addObject("total", this.commentsManager.getCommentsCountForCRN(CRN));
			modelAndView.addObject("commentsList", commentsList);
			modelAndView.addObject("userLoginId", userId);
		} catch (CMSException var8) {
			AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		}

		this.logger.debug("Exiting JSONCommentsController.getCommentsList");
		return modelAndView;
	}

	public ModelAndView deleteCommentsForCRN(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN JSONCommentsController.deleteCommentsForCRN");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			String commentId = "";
			String CRN = "";
			if (request.getParameter("commentId") != null) {
				commentId = request.getParameter("commentId");
			}

			if (request.getParameter("crn") != null) {
				CRN = request.getParameter("crn");
			}

			this.logger.debug("commentId list is :- " + commentId);
			List<String> commentIdList = StringUtils.commaSeparatedStringToList(commentId);
			this.commentsManager.deleteCommentsForCRN(commentIdList, userId, CRN);
			modelAndView.addObject("userLoginId", userId);
		} catch (CMSException var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			AtlasUtils.getExceptionView(this.logger, var10);
		}

		this.logger.debug("Exiting JSONCommentsController.deleteCommentsForCRN");
		return modelAndView;
	}
}