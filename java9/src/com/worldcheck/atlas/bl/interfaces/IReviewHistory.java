package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.audit.ReviewHistoryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import java.util.List;

public interface IReviewHistory {
	void setReviewHistoryDAO(ReviewHistoryDAO var1);

	List<ReviewHistory> getReviewHistory(String var1, int var2, int var3, String var4, String var5) throws CMSException;

	void insertReviewHistory(List<ReviewHistory> var1) throws CMSException;

	int getReviewHistoryCountForCRN(String var1) throws CMSException;
}