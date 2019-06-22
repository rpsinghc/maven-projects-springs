package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.task.ReviewTaskManagementDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetSubCatgVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetTemplateVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetVO;
import com.worldcheck.atlas.vo.task.review.ScoreSheetVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IReviewTaskManagementService {
	void setReviewTaskManagementDAO(ReviewTaskManagementDAO var1);

	List<ScoreSheetVO> getReviewScoreSheet(String var1) throws CMSException;

	List<ReviewScoreSheetTemplateVO> getReviewScoreSheetTemplate(String var1) throws CMSException;

	List<ScoreSheetVO> getReviewScoreSheetFieldMap(String var1) throws CMSException;

	List<ScoreSheetVO> getAnalyst(Map<String, String> var1) throws CMSException;

	List<TeamDetails> getTeamDetails(String var1) throws CMSException;

	List<ScoreSheetVO> getEditiors(String var1) throws CMSException;

	Integer insertReviewScoreSheetDetails(ReviewScoreSheetVO var1) throws CMSException;

	void insertReviewScoreSheetItems(ReviewScoreSheetVO var1) throws CMSException;

	boolean isAnalystReviewedScoresheet(Map<String, String> var1) throws CMSException;

	Integer insertCategoryDetails(ReviewScoreSheetCategoryVO var1) throws CMSException;

	Integer insertSubCategoryDetails(ReviewScoreSheetSubCatgVO var1) throws CMSException;

	List<ReviewScoreSheetVO> getReviewedScoreSheet(HashMap<String, Object> var1) throws CMSException;

	int getReviewedScoreSheetCount(HashMap<String, Object> var1) throws CMSException;

	List<ReviewScoreSheetTemplateVO> getReviewedSSCategoryDetails(String var1) throws CMSException;

	List<ReviewScoreSheetVO> getReviewedSSItemsDetails(String var1) throws CMSException;

	void updateCategoryDetails(ReviewScoreSheetCategoryVO var1) throws CMSException;

	void updateSubCategoryDetails(ReviewScoreSheetSubCatgVO var1) throws CMSException;

	void updateReviewSSDetails(ReviewScoreSheetVO var1) throws CMSException;

	void removedReviewedSSDetails(String var1) throws CMSException;
}