package com.worldcheck.atlas.dao.comment;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CommentDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CommentsDAO extends SqlMapClientTemplate {
	private String loggerClass = "com.worldcheck.atlas.dao.comment.CommentsDAO";
	private ILogProducer logger;
	private String INSERT_COMMENT_SQL_ID;
	private String FETCH_COMMENT_SQL_ID;
	private String FETCH_ALL_COMMENT_SQL_ID;
	private String FETCH_COMMENT_COUNT_SQL_ID;
	private String DELETE_COMMENT_SQL_ID;
	private String GET_COMMENTS_FROM_ID_SQL;
	private String getCaseStatus_SQL;
	private String INSERT_COMMENTBYBAKUP_SQL_ID;

	public CommentsDAO() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.INSERT_COMMENT_SQL_ID = "Comments.insertComments";
		this.FETCH_COMMENT_SQL_ID = "Comments.getComments";
		this.FETCH_ALL_COMMENT_SQL_ID = "Comments.getAllCommentsForCRN";
		this.FETCH_COMMENT_COUNT_SQL_ID = "Comments.getCommentsCount";
		this.DELETE_COMMENT_SQL_ID = "Comments.deleteComments";
		this.GET_COMMENTS_FROM_ID_SQL = "Comments.getCommentsValueForId";
		this.getCaseStatus_SQL = "Comments.getCaseStatus";
		this.INSERT_COMMENTBYBAKUP_SQL_ID = "Comments.insertCommentsByBakup";
	}

	public int insertCommentsList(CommentDetails commentDetails) throws CMSException {
		this.logger.debug("In CommentsDAO.insertCommentsList");

		int commentID;
		try {
			if (commentDetails.isPerformedByBakup()) {
				commentID = (Integer) this.insert(this.INSERT_COMMENTBYBAKUP_SQL_ID, commentDetails);
			} else {
				commentID = (Integer) this.insert(this.INSERT_COMMENT_SQL_ID, commentDetails);
			}
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsDAO.insertCommentsList");
		return commentID;
	}

	public List<CommentDetails> getCommentsForCRN(HashMap paramList) throws CMSException {
		this.logger.debug("In CommentsDAO.getCommentsForCRN");
		new ArrayList();

		List commentsList;
		try {
			commentsList = this.queryForList(this.FETCH_COMMENT_SQL_ID, paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsDAO.getCommentsForCRN");
		return commentsList;
	}

	public List<CommentDetails> getAllCommentsListForCRN(String CRN) throws CMSException {
		this.logger.debug("In CommentsDAO::getAllCommentsListForCRN");
		new ArrayList();

		List commentsList;
		try {
			commentsList = this.queryForList(this.FETCH_ALL_COMMENT_SQL_ID, CRN);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsDAO::getAllCommentsListForCRN");
		return commentsList;
	}

	public int getCommentsCountForCRN(String CRN) throws CMSException {
		this.logger.debug("In CommentsDAO.getCommentsCountForCRN");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject(this.FETCH_COMMENT_COUNT_SQL_ID, CRN);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsDAO.getCommentsCountForCRN");
		return count;
	}

	public void deleteCommentsForCRN(List<String> commentIdList) throws CMSException {
		this.logger.debug("In CommentsDAO.deleteCommentsForCRN");

		try {
			this.delete(this.DELETE_COMMENT_SQL_ID, commentIdList);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CommentsDAO.deleteCommentsForCRN");
	}

	public String getCommentsTextForId(List<String> commentIdList) throws CMSException {
		this.logger.debug("In CommentsDAO::getCommentsTextForId");
		String commentsText = "";

		try {
			commentsText = (String) this.queryForObject(this.GET_COMMENTS_FROM_ID_SQL, commentIdList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsDAO::getCommentsTextForId");
		return commentsText;
	}

	public Integer getCaseStatus(String CRN) throws CMSException {
		this.logger.debug("In CommentsDAO::getCaseStatus");
		Integer caseStatusID = 0;

		try {
			caseStatusID = (Integer) this.queryForObject(this.getCaseStatus_SQL, CRN);
			this.logger.debug("caseStatusID " + caseStatusID);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CommentsDAO::getCaseStatus");
		return caseStatusID;
	}
}