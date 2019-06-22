package com.worldcheck.atlas.services.task;

import com.worldcheck.atlas.bl.interfaces.IReviewTaskManagementService;
import com.worldcheck.atlas.dao.task.ReviewTaskManagementDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetSubCatgVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetTemplateVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetVO;
import com.worldcheck.atlas.vo.task.review.ScoreSheetVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewTaskManagementService implements IReviewTaskManagementService {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.task.invoice.ReviewTaskManagementService");
	private ReviewTaskManagementDAO reviewTaskManagementDAO;

	public void setReviewTaskManagementDAO(ReviewTaskManagementDAO reviewTaskManagementDAO) {
		this.reviewTaskManagementDAO = reviewTaskManagementDAO;
	}

	public List<ScoreSheetVO> getReviewScoreSheet(String teamId) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewScoreSheet(teamId);
	}

	public List<ReviewScoreSheetTemplateVO> getReviewScoreSheetTemplate(String scoreSheetId) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewScoreSheetTemplate(scoreSheetId);
	}

	public List<ScoreSheetVO> getReviewScoreSheetFieldMap(String scoreSheetId) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewScoreSheetFieldMap(scoreSheetId);
	}

	public List<ScoreSheetVO> getAnalyst(Map<String, String> parameterMap) throws CMSException {
		return this.reviewTaskManagementDAO.getAnalyst(parameterMap);
	}

	public List<TeamDetails> getTeamDetails(String crn) throws CMSException {
		return this.reviewTaskManagementDAO.getTeamDetails(crn);
	}

	public List<ScoreSheetVO> getEditiors(String scoreSheetId) throws CMSException {
		return this.reviewTaskManagementDAO.getEditiors(scoreSheetId);
	}

	public Integer insertReviewScoreSheetDetails(ReviewScoreSheetVO reviewScoreSheetVO) throws CMSException {
		return this.reviewTaskManagementDAO.insertReviewScoreSheetDetails(reviewScoreSheetVO);
	}

	public void insertReviewScoreSheetItems(ReviewScoreSheetVO reviewScoreSheetItemsVO) throws CMSException {
		this.reviewTaskManagementDAO.insertReviewScoreSheetItems(reviewScoreSheetItemsVO);
	}

	public boolean isAnalystReviewedScoresheet(Map<String, String> parameterMap) throws CMSException {
		return this.reviewTaskManagementDAO.isAnalystReviewedScoresheet(parameterMap);
	}

	public Integer insertCategoryDetails(ReviewScoreSheetCategoryVO categoryVO) throws CMSException {
		return this.reviewTaskManagementDAO.insertCategoryDetails(categoryVO);
	}

	public Integer insertSubCategoryDetails(ReviewScoreSheetSubCatgVO scoreSheetSubCatgVO) throws CMSException {
		return this.reviewTaskManagementDAO.insertSubCategoryDetails(scoreSheetSubCatgVO);
	}

	public List<ReviewScoreSheetVO> getReviewedScoreSheet(HashMap<String, Object> paramMap) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewedScoreSheet(paramMap);
	}

	public int getReviewedScoreSheetCount(HashMap<String, Object> paramMap) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewedScoreSheetCount(paramMap);
	}

	public List<ReviewScoreSheetTemplateVO> getReviewedSSCategoryDetails(String sheetId) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewedSSCategoryDetails(sheetId);
	}

	public List<ReviewScoreSheetVO> getReviewedSSItemsDetails(String sheetId) throws CMSException {
		return this.reviewTaskManagementDAO.getReviewedSSItemsDetails(sheetId);
	}

	public void updateCategoryDetails(ReviewScoreSheetCategoryVO categoryVO) throws CMSException {
		this.reviewTaskManagementDAO.updateCategoryDetails(categoryVO);
	}

	public void updateSubCategoryDetails(ReviewScoreSheetSubCatgVO scoreSheetSubCatgVO) throws CMSException {
		this.reviewTaskManagementDAO.updateSubCategoryDetails(scoreSheetSubCatgVO);
	}

	public void updateReviewSSDetails(ReviewScoreSheetVO scoreSheetVO) throws CMSException {
		this.reviewTaskManagementDAO.updateReviewSSDetails(scoreSheetVO);
	}

	public void removedReviewedSSDetails(String sheetIds) throws CMSException {
		this.reviewTaskManagementDAO.removedReviewedSSDetails(sheetIds);
	}
}