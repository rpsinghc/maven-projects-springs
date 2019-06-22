package com.worldcheck.atlas.bl.comment;

import com.worldcheck.atlas.bl.interfaces.ICaseComments;
import com.worldcheck.atlas.dao.comment.CommentsDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CommentDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsManager implements ICaseComments {
	private String loggerClass = "com.worldcheck.atlas.bl.comment.CommentsManager";
	private ILogProducer logger;
	private static final String CRN = "CRN";
	private CommentsDAO commentsDAO;

	public CommentsManager() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
	}

	public void setCommentsDAO(CommentsDAO commentsDAO) {
		this.commentsDAO = commentsDAO;
	}

	public void insertComments(String crn, String commentsData, String updatedBy, String sentFrom,
			boolean isPerformedByBakup) throws CMSException {
		this.logger.debug("Inside CommentsManager.insertComments ");

		try {
			CommentDetails commentDetails = new CommentDetails();
			commentDetails.setCRN(crn);
			commentDetails.setComment(commentsData);
			commentDetails.setSentFrom(sentFrom);
			commentDetails.setSentTo("");
			commentDetails.setUpdatedBy(updatedBy);
			commentDetails.setPerformedByBakup(isPerformedByBakup);
			this.commentsDAO.insertCommentsList(commentDetails);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting CommentsManager.insertComments ");
	}

	public int insertComments(CommentDetails commentDetailsParam) throws CMSException {
		this.logger.debug("Inside CommentsManager.insertComments via notification");

		int commentID;
		try {
			if (commentDetailsParam.getSentTo().charAt(commentDetailsParam.getSentTo().length() - 1) == ',') {
				commentDetailsParam.setSentTo(
						commentDetailsParam.getSentTo().substring(0, commentDetailsParam.getSentTo().length() - 1));
			}

			this.logger.debug("sentTo String:- " + commentDetailsParam.getSentTo());
			commentID = this.commentsDAO.insertCommentsList(commentDetailsParam);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CommentsManager.insertComments via notification");
		return commentID;
	}

	public List<CommentDetails> getComments(String crn, int start, String columnName, String sortType, int limit)
			throws CMSException {
		this.logger.debug("IN CommentsManager.getComments");
		new ArrayList();
		HashMap paramList = new HashMap();

		List commentsList;
		try {
			paramList.put("CRN", crn);
			paramList.put("start", new Integer(start + 1));
			paramList.put("limit", new Integer(start + limit));
			paramList.put("sort", columnName);
			paramList.put("dir", sortType);
			commentsList = this.commentsDAO.getCommentsForCRN(paramList);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting CommentsManager.getComments");
		return commentsList;
	}

	public List<CommentDetails> getAllCommentsListForCRN(String crn) throws CMSException {
		this.logger.debug("IN CommentsManager::getAllCommentsListForCRN");
		new ArrayList();

		List commentsList;
		try {
			commentsList = this.commentsDAO.getAllCommentsListForCRN(crn);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CommentsManager::getAllCommentsListForCRN");
		return commentsList;
	}

	public int getCommentsCountForCRN(String CRN) throws CMSException {
		this.logger.debug("IN CommentsManager.getCommentsCountForCRN");
		boolean var2 = false;

		int count;
		try {
			count = Integer.valueOf(this.commentsDAO.getCommentsCountForCRN(CRN));
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CommentsManager.getCommentsCountForCRN");
		return count;
	}

	public void deleteCommentsForCRN(List<String> commentIdList, String loginUserId, String CRN) throws CMSException {
		this.logger.debug("IN CommentsManager.deleteCommentsForCRN");

		try {
			String commentsText = "";
			commentsText = this.commentsDAO.getCommentsTextForId(commentIdList);
			this.commentsDAO.deleteCommentsForCRN(commentIdList);
			this.setCaseHistoryForComment(commentsText, loginUserId, CRN);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsManager.deleteCommentsForCRN");
	}

	private void setCaseHistoryForComment(String commentsText, String loginUserId, String CRN) throws CMSException {
		try {
			this.logger.debug("IN CommentsManager::setCaseHistoryForComment");
			CaseHistory caseHistory = new CaseHistory();
			long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(CRN);
			caseHistory.setCRN(CRN);
			caseHistory.setPerformer(loginUserId);
			caseHistory.setPid(String.valueOf(pid));
			caseHistory.setProcessCycle("");
			caseHistory.setTaskName("");
			caseHistory.setTaskStatus("");
			ResourceLocator.self().getCaseHistoryService().setCaseHistoryForComments(commentsText, caseHistory);
			this.logger.debug("Exiting CommentsManager::setCaseHistoryForComment");
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public Integer getCaseStatus(String CRN) throws CMSException {
		this.logger.debug("IN CommentsManager::getCaseStatus");
		Integer caseStatus = 0;

		try {
			caseStatus = this.commentsDAO.getCaseStatus(CRN);
			this.logger.debug("Exiting CommentsManager::getCaseStatus");
			return caseStatus;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}