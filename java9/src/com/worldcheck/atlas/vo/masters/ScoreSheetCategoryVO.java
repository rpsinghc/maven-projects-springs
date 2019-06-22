package com.worldcheck.atlas.vo.masters;

import java.util.ArrayList;
import java.util.List;

public class ScoreSheetCategoryVO {
	private int scoresheetMasterId;
	private int categoryMasterId;
	private String categoryName;
	private String updatedBy;
	private List<ScoreSheetSubCategoryVO> scoreSheetSubCategory = new ArrayList();

	public int getScoresheetMasterId() {
		return this.scoresheetMasterId;
	}

	public void setScoresheetMasterId(int scoresheetMasterId) {
		this.scoresheetMasterId = scoresheetMasterId;
	}

	public int getCategoryMasterId() {
		return this.categoryMasterId;
	}

	public void setCategoryMasterId(int categoryMasterId) {
		this.categoryMasterId = categoryMasterId;
	}

	public List<ScoreSheetSubCategoryVO> getScoreSheetSubCategory() {
		return this.scoreSheetSubCategory;
	}

	public void setScoreSheetSubCategory(List<ScoreSheetSubCategoryVO> scoreSheetSubCategory) {
		this.scoreSheetSubCategory = scoreSheetSubCategory;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}