package com.worldcheck.atlas.vo.masters;

import java.util.ArrayList;
import java.util.List;

public class ScoreSheetSubCategoryVO {
	private int scoresheetMasterId;
	private int categoryMasterId;
	private int scoreSheetSubCategoryId;
	private String scoreSheetSubCategoryName;
	private List<GradingPoint> gradingPoint = new ArrayList();

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

	public int getScoreSheetSubCategoryId() {
		return this.scoreSheetSubCategoryId;
	}

	public void setScoreSheetSubCategoryId(int scoreSheetSubCategoryId) {
		this.scoreSheetSubCategoryId = scoreSheetSubCategoryId;
	}

	public String getScoreSheetSubCategoryName() {
		return this.scoreSheetSubCategoryName;
	}

	public void setScoreSheetSubCategoryName(String scoreSheetSubCategoryName) {
		this.scoreSheetSubCategoryName = scoreSheetSubCategoryName;
	}

	public List<GradingPoint> getGradingPoint() {
		return this.gradingPoint;
	}

	public void setGradingPoint(List<GradingPoint> gradingPoint) {
		this.gradingPoint = gradingPoint;
	}
}