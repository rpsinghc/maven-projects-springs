package com.worldcheck.atlas.dao.audit;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ReviewHistoryDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.audit.ReviewHistoryDAO");
	private String INSERT_REVIEW_HISTORY_SQL = "AuditHistory.insertReviewHistoryEvent";
	private String SELECT_REVIEW_HISTORY_SQL = "AuditHistory.getReviewHistoryData";
	private static final String FETCH_REVIEW_COUNT_SQL_ID = "AuditHistory.getReviewHistoryCount";

	public List<ReviewHistory> selectReviewHistory(HashMap paramList) throws CMSException {
		this.logger.debug("In ReviewHistoryDAO::selectReviewHistory");
		new ArrayList();

		List result;
		try {
			result = this.queryForList(this.SELECT_REVIEW_HISTORY_SQL, paramList);
			this.logger.debug("Review history list size:- " + result.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("In ReviewHistoryDAO::selectReviewHistory");
		return result;
	}

	public void insertReviewHistory(List<ReviewHistory> reviewHistoryItemsList) throws CMSException {
		this.logger.debug("In ReviewHistoryDAO::insertReviewHistory");

		try {
			Iterator iterator = reviewHistoryItemsList.iterator();

			while (iterator.hasNext()) {
				ReviewHistory ReviewHistoryObj = (ReviewHistory) iterator.next();
				this.logger.debug("ReviewHistoryObj.getCRN() " + ReviewHistoryObj.getCRN());
				this.logger.debug("ReviewHistoryObj.getTaskName() " + ReviewHistoryObj.getTaskName());
				this.logger.debug("ReviewHistoryObj.getReviewStatus() " + ReviewHistoryObj.getReviewStatus());
				this.logger.debug("ReviewHistoryObj.getCommentFrom() " + ReviewHistoryObj.getCommentFrom());
				this.logger.debug("ReviewHistoryObj.getUpdatedBy() " + ReviewHistoryObj.getUpdatedBy());
				this.insert(this.INSERT_REVIEW_HISTORY_SQL, ReviewHistoryObj);
			}
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ReviewHistoryDAO::insertReviewHistory");
	}

	public int getReviewHistoryCountForCRN(String CRN) throws CMSException {
		this.logger.debug("In ReviewHistoryDAO::getReviewHistoryCountForCRN");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("AuditHistory.getReviewHistoryCount", CRN);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ReviewHistoryDAO::getReviewHistoryCountForCRN");
		return count;
	}
}