package com.worldcheck.atlas.services.comments;

import com.worldcheck.atlas.bl.interfaces.ICaseComments;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CommentDetails;
import java.util.ArrayList;
import java.util.List;

public class CommentService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.comments.CommentService");
	private ICaseComments commentsManager;

	public void setCommentsManager(ICaseComments commentsManager) {
		this.commentsManager = commentsManager;
	}

	public void insertComments(String crn, String commentsData, String updatedBy, String sentFrom,
			boolean isPerformedByBakup) throws CMSException {
		this.logger.debug("In CommentService.insertComments");
		this.commentsManager.insertComments(crn, commentsData, updatedBy, sentFrom, isPerformedByBakup);
		this.logger.debug("Exiting CommentService.insertComments");
	}

	public List<CommentDetails> getCommentsList(String CRN, int start, String columnName, String sortType, int limit)
			throws CMSException {
		new ArrayList();
		this.logger.debug("In CommentService.getCommentsList");
		List<CommentDetails> commentsList = this.commentsManager.getComments(CRN, start, columnName, sortType, limit);
		this.logger.debug("Exiting CommentService.getCommentsList");
		return commentsList;
	}

	public int insertComments(CommentDetails commentDetailsParam) throws CMSException {
		this.logger.debug("In CommentService.insertComments via notification");
		int commentID = this.commentsManager.insertComments(commentDetailsParam);
		this.logger.debug("Exiting CommentService.insertComments via notification");
		return commentID;
	}

	public void deleteCommentsForCRN(List<String> commentIdList, String loginUserId, String CRN) throws CMSException {
		this.logger.debug("IN CommentService.deleteCommentsForCRN");

		try {
			this.commentsManager.deleteCommentsForCRN(commentIdList, loginUserId, CRN);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsManager.deleteCommentsForCRN");
	}

	public List<CommentDetails> getAllCommentsListForCRN(String CRN) throws CMSException {
		this.logger.debug("In CommentService::getAllCommentsListForCRN");
		new ArrayList();

		List commentsList;
		try {
			commentsList = this.commentsManager.getAllCommentsListForCRN(CRN);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CommentService::getAllCommentsListForCRN");
		return commentsList;
	}
}