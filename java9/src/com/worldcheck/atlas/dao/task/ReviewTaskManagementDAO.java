package com.worldcheck.atlas.dao.task;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetSubCatgVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetTemplateVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetVO;
import com.worldcheck.atlas.vo.task.review.ScoreSheetVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ReviewTaskManagementDAO extends SqlMapClientTemplate {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.task.ReviewTaskManagementDAO");
	private static final String SCORESHEET = "ReviewTask.getScoreSheet";
	private static final String REVIEW_SCORESHEET_TEMPLATE = "ReviewTask.getReviewScoreSheetTemplate";
	private static final String REVIEW_SCORE_SHEET_FIELD_MAP = "ReviewTask.getReviewScoreSheetFieldMap";
	private static final String ANALYST = "ReviewTask.getAnalyst";
	private static final String TEAM_DETAILS = "ReviewTask.getTeamDetails";
	private static final String EDITIORS = "ReviewTask.getEditiors";
	private static final String REVIEW_SCORESHEET_DETAILS = "ReviewTask.insertReviewScoreSheetDetails";
	private static final String REVIEW_SCORESHEET_ITEMS = "ReviewTask.insertReviewScoreSheetItems";
	private static final String ANALYST_REVIEWED_SCORESHEET = "ReviewTask.checkAnalystReviewStatus";
	private static final String INSERT_CATEGORY_DETAILS = "ReviewTask.insertReviewCategory";
	private static final String INSERT_SUB_CATEGORY_DETAILS = "ReviewTask.insertReviewSubCategory";
	private static final String REVIEWED_SCORESHEET_DETAILS = "ReviewTask.getReviewedScoreSheet";
	private static final String REVIEWED_SCORESHEET_DETAILS_COUNT = "ReviewTask.getReviewedScoreSheetCount";
	private static final String REVIEWED_SCORESHEET_CATEGORY_DETAILS = "ReviewTask.getReviewedSSCategoryDetails";
	private static final String REVIEWED_SCORESHEET_ITEM_DETAILS = "ReviewTask.getReviewedSSItemDetails";
	private static final String UPDATE_CATEGORY_DETAILS = "ReviewTask.updateReviewSSCatgory";
	private static final String UPDATE_SUB_CATEGORY_DETAILS = "ReviewTask.updateReviewSSSubCatgory";
	private static final String UPDATE_REVIEW_SCORE_SHEET_DETAILS = "ReviewTask.updateReviewSSDetails";
	private static final String REMOVE_REVIEW_SCORE_SHEET = "ReviewTask.deleteReviewedScoreSheet";

	public List<ScoreSheetVO> getReviewScoreSheet(String teamId) throws CMSException {
		new ArrayList();

		try {
			List<ScoreSheetVO> scoreSheetList = this.queryForList("ReviewTask.getScoreSheet", teamId);
			this.logger.debug(" list size in dao " + scoreSheetList.size());
			return scoreSheetList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReviewScoreSheetTemplateVO> getReviewScoreSheetTemplate(String scoreSheetId) throws CMSException {
		new ArrayList();

		try {
			List<ReviewScoreSheetTemplateVO> reviewScoreSheetList = this
					.queryForList("ReviewTask.getReviewScoreSheetTemplate", scoreSheetId);
			this.logger.debug(" list size in dao " + reviewScoreSheetList.size());
			return reviewScoreSheetList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getReviewedScoreSheetCount(HashMap<String, Object> paramMap) throws CMSException {
		Integer count = 0;

		try {
			count = (Integer) this.queryForObject("ReviewTask.getReviewedScoreSheetCount", paramMap);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return count;
	}

	public List<ReviewScoreSheetVO> getReviewedScoreSheet(HashMap<String, Object> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<ReviewScoreSheetVO> reviewScoreSheetList = this.queryForList("ReviewTask.getReviewedScoreSheet",
					paramMap);
			this.logger.debug(" list size in dao " + reviewScoreSheetList.size());
			return reviewScoreSheetList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ScoreSheetVO> getReviewScoreSheetFieldMap(String scoreSheetId) throws CMSException {
		new ArrayList();

		try {
			List<ScoreSheetVO> reviewScoreSheetList = this.queryForList("ReviewTask.getReviewScoreSheetFieldMap",
					scoreSheetId);
			this.logger.debug(" getReviewScoreSheetFieldMap list size in dao " + reviewScoreSheetList.size());
			return reviewScoreSheetList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ScoreSheetVO> getAnalyst(Map<String, String> parameterMap) throws CMSException {
		new ArrayList();

		try {
			List<ScoreSheetVO> reviewScoreSheetList = this.queryForList("ReviewTask.getAnalyst", parameterMap);
			this.logger.debug("getAnalyst list size in dao " + reviewScoreSheetList.size());
			return reviewScoreSheetList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getTeamDetails(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetailList = this.queryForList("ReviewTask.getTeamDetails", crn);
			this.logger.debug(" getTeamDetails list size in dao " + teamDetailList.size());
			return teamDetailList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ScoreSheetVO> getEditiors(String scoreSheetId) throws CMSException {
		new ArrayList();

		try {
			List<ScoreSheetVO> editorList = this.queryForList("ReviewTask.getEditiors", scoreSheetId);
			this.logger.debug(" getEditiors list size in dao " + editorList.size());
			return editorList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Integer insertReviewScoreSheetDetails(ReviewScoreSheetVO reviewScoreSheetVO) throws CMSException {
		try {
			Integer reviewId = (Integer) this.insert("ReviewTask.insertReviewScoreSheetDetails", reviewScoreSheetVO);
			return reviewId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void insertReviewScoreSheetItems(ReviewScoreSheetVO reviewScoreSheetItemsVO) throws CMSException {
		try {
			this.insert("ReviewTask.insertReviewScoreSheetItems", reviewScoreSheetItemsVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public boolean isAnalystReviewedScoresheet(Map<String, String> parameterMap) throws CMSException {
		int count;
		try {
			count = (Integer) this.queryForObject("ReviewTask.checkAnalystReviewStatus", parameterMap);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return count > 0;
	}

	public Integer insertCategoryDetails(ReviewScoreSheetCategoryVO categoryVO) throws CMSException {
		try {
			Integer reviewId = (Integer) this.insert("ReviewTask.insertReviewCategory", categoryVO);
			return reviewId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Integer insertSubCategoryDetails(ReviewScoreSheetSubCatgVO scoreSheetSubCatgVO) throws CMSException {
		try {
			Integer reviewId = (Integer) this.insert("ReviewTask.insertReviewSubCategory", scoreSheetSubCatgVO);
			return reviewId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReviewScoreSheetTemplateVO> getReviewedSSCategoryDetails(String sheetId) throws CMSException {
		new ArrayList();

		try {
			List<ReviewScoreSheetTemplateVO> scoreSheetCategoryList = this
					.queryForList("ReviewTask.getReviewedSSCategoryDetails", sheetId);
			this.logger.debug(" list size in dao " + scoreSheetCategoryList.size());
			return scoreSheetCategoryList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReviewScoreSheetVO> getReviewedSSItemsDetails(String sheetId) throws CMSException {
		new ArrayList();

		try {
			List<ReviewScoreSheetVO> scoreSheetItemList = this.queryForList("ReviewTask.getReviewedSSItemDetails",
					sheetId);
			this.logger.debug(" list size in dao " + scoreSheetItemList.size());
			return scoreSheetItemList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateCategoryDetails(ReviewScoreSheetCategoryVO categoryVO) throws CMSException {
		try {
			this.update("ReviewTask.updateReviewSSCatgory", categoryVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateSubCategoryDetails(ReviewScoreSheetSubCatgVO scoreSheetSubCatgVO) throws CMSException {
		try {
			this.update("ReviewTask.updateReviewSSSubCatgory", scoreSheetSubCatgVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateReviewSSDetails(ReviewScoreSheetVO scoreSheetVO) throws CMSException {
		try {
			this.update("ReviewTask.updateReviewSSDetails", scoreSheetVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void removedReviewedSSDetails(String sheetIds) throws CMSException {
		try {
			this.update("ReviewTask.deleteReviewedScoreSheet", sheetIds);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}