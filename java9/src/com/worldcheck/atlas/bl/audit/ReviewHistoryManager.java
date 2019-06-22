package com.worldcheck.atlas.bl.audit;

import com.worldcheck.atlas.bl.interfaces.IReviewHistory;
import com.worldcheck.atlas.dao.audit.ReviewHistoryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReviewHistoryManager implements IReviewHistory {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.audit.ReviewHistoryManager");
	private ReviewHistoryDAO reviewHistoryDAO;

	public void setReviewHistoryDAO(ReviewHistoryDAO reviewHistoryDAO) {
		this.reviewHistoryDAO = reviewHistoryDAO;
	}

	public List<ReviewHistory> getReviewHistory(String CRN, int start, int limit, String columnName, String sortType)
			throws CMSException {
		this.logger.debug("In ReviewHistoryManager::getReviewHistory");
		new ArrayList();
		HashMap paramList = new HashMap();

		List reviewHistoryList;
		try {
			paramList.put("crn", CRN);
			paramList.put("start", new Integer(start + 1));
			paramList.put("limit", new Integer(start + limit));
			paramList.put("sort", columnName);
			paramList.put("dir", sortType);
			reviewHistoryList = this.reviewHistoryDAO.selectReviewHistory(paramList);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("In ReviewHistoryManager::getReviewHistory");
		return reviewHistoryList;
	}

	public void insertReviewHistory(List<ReviewHistory> reviewHistoryItemsList) throws CMSException {
		this.logger.debug("In ReviewHistoryManager::insertReviewHistory");

		try {
			this.reviewHistoryDAO.insertReviewHistory(reviewHistoryItemsList);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting ReviewHistoryManager::insertReviewHistory");
	}

	public int getReviewHistoryCountForCRN(String CRN) throws CMSException {
		this.logger.debug("IN ReviewHistoryManager::getReviewHistoryCountForCRN");
		boolean var2 = false;

		int count;
		try {
			count = Integer.valueOf(this.reviewHistoryDAO.getReviewHistoryCountForCRN(CRN));
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting ReviewHistoryManager::getReviewHistoryCountForCRN");
		return count;
	}
}