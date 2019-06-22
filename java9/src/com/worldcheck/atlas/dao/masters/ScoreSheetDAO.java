package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.GradingPoint;
import com.worldcheck.atlas.vo.masters.ScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import com.worldcheck.atlas.vo.masters.ScoreSheetSubCategoryVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ScoreSheetDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ScoreSheetDAO");
	private ScoreSheetCategoryVO categoryVO;
	private ScoreSheetSubCategoryVO subCategoryVO;
	private GradingPoint gradingPoint;
	private Map cateMap;
	private Map subCateMap;
	private String DELETE_SCORESHEET_MASTER = "ScoreSheet_Master.deleteScoreSheet";
	private String COUNT_SCORESHEET = "ScoreSheet_Master.getScoreSheetCount";
	private String SCORESHEET_GRIDLIST = "ScoreSheet_Master.getScoreSheetGridList";
	private String SCORESHEETID_LIST = "scoreSheetIdList";
	private String ADD_SCORESHEET_MASTER = "ScoreSheet_Master.insertScoreSheetMaster";
	private String ADD_REVIEW_FIELDS = "ScoreSheet_Master.insertSelectedReviewField";
	private String ADD_SUBCATEGORY = "ScoreSheet_Master.insertSubCategory";
	private String SUCCESS = "success";

	public List<ScoreSheetMasterVO> getScoreSheetGrid(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		try {
			new ArrayList();
			List<ScoreSheetMasterVO> scoreSheetGridList = this.queryForList(this.SCORESHEET_GRIDLIST,
					scoreSheetMasterVO);
			return scoreSheetGridList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getScoreSheetCount(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		this.logger.debug("Inside Score Sheet Dao to return Total no of Score Sheet.");

		try {
			return (Integer) this.queryForObject(this.COUNT_SCORESHEET, scoreSheetMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getCountExistScoreSheetName(String scoreSheetName) {
		int count = Integer.parseInt(
				this.queryForObject("ScoreSheet_Master.searchExistScoreSheetName", scoreSheetName).toString());
		return count;
	}

	public ScoreSheetMasterVO getScoreSheetInfo(String scoresheetMasterId) throws CMSException {
		ScoreSheetMasterVO scoreSheetObj = null;

		try {
			new ScoreSheetMasterVO();
			scoreSheetObj = (ScoreSheetMasterVO) this.queryForObject("ScoreSheet_Master.getScoreSheetInfo",
					scoresheetMasterId);
			return scoreSheetObj;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String updateScoreSheet(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		this.logger.debug("<<<<updateScoreSheet>>>>>ScoreSheetMasterId:" + scoreSheetMasterVO.getScoresheetMasterId());
		String sheetId = scoreSheetMasterVO.getScoresheetMasterId() + "";
		this.deleteScoreSheet(sheetId);
		this.logger.debug("<<<<All Deleted...>>>>");
		this.addScoreSheet(scoreSheetMasterVO);
		return this.SUCCESS;
	}

	public String addScoreSheet(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		try {
			this.logger.debug("<<<<<<<Iside ScoreSheetDAO>>>>>>>");
			Integer scoreSheetId = (Integer) this.insert(this.ADD_SCORESHEET_MASTER, scoreSheetMasterVO);
			this.logger.debug("<<<<<<added record in ScoreSheet Master ID : " + scoreSheetId);
			scoreSheetMasterVO.setScoresheetMasterId(scoreSheetId);
			Integer reviewFieldMasterId = (Integer) this.insert(this.ADD_REVIEW_FIELDS, scoreSheetMasterVO);
			this.logger.debug("<<<<<<ReviewFields of ScoreSheet MasterId : " + reviewFieldMasterId);
			Iterator iterator = scoreSheetMasterVO.getCategoryList().iterator();

			while (iterator.hasNext()) {
				this.categoryVO = new ScoreSheetCategoryVO();
				this.categoryVO = (ScoreSheetCategoryVO) iterator.next();
				this.categoryVO.setScoresheetMasterId(scoreSheetId);
				this.categoryVO.setUpdatedBy(scoreSheetMasterVO.getUpdatedBy());
				this.logger.debug("ScoreSheetMasterId:>>>>>>>>>>" + this.categoryVO.getScoresheetMasterId());
				this.logger.debug("CategoryName:>>>>>>>>>>" + this.categoryVO.getCategoryName());
				Integer categoryMasterId = (Integer) this.insert("ScoreSheet_Master.insertCategory", this.categoryVO);
				this.logger
						.debug("\n***>>>>>>>Category Inserted for Sucessfully....with CategoryId :" + categoryMasterId);
				Iterator iterator2 = this.categoryVO.getScoreSheetSubCategory().iterator();

				while (iterator2.hasNext()) {
					this.subCategoryVO = new ScoreSheetSubCategoryVO();
					this.subCategoryVO = (ScoreSheetSubCategoryVO) iterator2.next();
					this.subCategoryVO.setScoresheetMasterId(scoreSheetId);
					Iterator gradPoint = this.subCategoryVO.getGradingPoint().iterator();

					while (gradPoint.hasNext()) {
						this.gradingPoint = new GradingPoint();
						this.gradingPoint = (GradingPoint) gradPoint.next();
						this.subCateMap = new HashMap();
						this.subCateMap.clear();
						this.logger.debug("\n****<<<<ScoreSheet Data Enter One by One >>>>>>*****\n");
						this.logger.debug("scoresheetMasterId:" + scoreSheetId);
						this.logger.debug("Category Id:" + categoryMasterId);
						this.logger
								.debug("scoreSheetSubCategoryName" + this.subCategoryVO.getScoreSheetSubCategoryName());
						this.logger.debug("countOfError" + this.gradingPoint.getCountOfError());
						this.logger.debug("point" + this.gradingPoint.getPoint());
						this.logger.debug("updatedBy" + this.categoryVO.getUpdatedBy());
						this.logger.debug("\n***<<<< End of the Data of SubCategory >>>>>>****\n");
						this.subCateMap.put("scoresheetMasterId", scoreSheetId);
						this.subCateMap.put("scoreSheetCategoryId", categoryMasterId);
						this.subCateMap.put("scoreSheetSubCategoryName",
								this.subCategoryVO.getScoreSheetSubCategoryName());
						this.subCateMap.put("countOfError", this.gradingPoint.getCountOfError());
						this.subCateMap.put("point", this.gradingPoint.getPoint());
						this.subCateMap.put("updatedBy", this.categoryVO.getUpdatedBy());
						Integer subcategoryMasterId = (Integer) this.insert(this.ADD_SUBCATEGORY, this.subCateMap);
						this.logger.debug("SubCategory Master Id >>>>>>>> : " + subcategoryMasterId);
					}
				}
			}

			return this.SUCCESS;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public String deleteScoreSheet(String scoreSheetId) throws CMSException {
		List scoreSheetIdList = null;

		try {
			scoreSheetIdList = StringUtils.commaSeparatedStringToList(scoreSheetId);
			this.logger.debug("Inside the ScoreSheet Dao:" + scoreSheetIdList);
			this.delete(this.DELETE_SCORESHEET_MASTER, scoreSheetIdList);
			this.logger.debug("Sucessful Updated");
			return this.SUCCESS;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}