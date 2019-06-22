package com.worldcheck.atlas.services.audit;

import com.worldcheck.atlas.bl.audit.ReviewHistoryManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import java.util.ArrayList;
import java.util.List;

public class ReviewHistoryService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.audit.ReviewHistoryService");
	private ReviewHistoryManager reviewHistoryManager;

	public void setReviewHistoryManager(ReviewHistoryManager reviewHistoryManager) {
		this.reviewHistoryManager = reviewHistoryManager;
	}

	public void setReviewHistory(List<ReviewHistory> reviewHistoryItemsList) throws CMSException {
		this.logger.debug("In ReviewHistoryService::setReviewHistory");

		try {
			this.reviewHistoryManager.insertReviewHistory(reviewHistoryItemsList);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting ReviewHistoryService::setReviewHistory");
	}

	public List<ReviewHistory> getReviewHistory(String CRN, int start, int limit, String columnName, String sortType)
			throws CMSException {
		this.logger.debug("In ReviewHistoryService::getReviewHistory");
		new ArrayList();

		List reviewHistoryList;
		try {
			reviewHistoryList = this.reviewHistoryManager.getReviewHistory(CRN, start, limit, columnName, sortType);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("Exiting ReviewHistoryService::getReviewHistory");
		return reviewHistoryList;
	}
}