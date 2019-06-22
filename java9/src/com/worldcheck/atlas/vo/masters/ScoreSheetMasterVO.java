package com.worldcheck.atlas.vo.masters;

import java.util.List;

public class ScoreSheetMasterVO {
	private String getScoreSheetMasterId;
	private int scoresheetMasterId;
	private int reviewFieldMasterId;
	private String office;
	private String gradingSystem;
	private String categoryString;
	private String scoreSheetName;
	private String selectedReviewField;
	private List<ScoreSheetCategoryVO> categoryList;
	private String updatedBy;
	private Integer limit;
	private Integer start;
	private String scoreSheetIdList;
	private String sortColumnName;
	private String sortType;
	private String boxLabel;
	private String name;
	private String inputValue;
	private String checked;
	private String scoresheetfieldId;
	private String fieldName;
	private int month;
	private int year;
	private String analyst;
	private String reviewer;
	private long score;
	private long maxPoints;
	private String rating;
	private long noOfReports;

	public List<ScoreSheetCategoryVO> getCategoryList() {
		return this.categoryList;
	}

	public void setCategoryList(List<ScoreSheetCategoryVO> categoryList) {
		this.categoryList = categoryList;
	}

	public String getBoxLabel() {
		return this.boxLabel;
	}

	public void setBoxLabel(String boxLabel) {
		this.boxLabel = boxLabel;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInputValue() {
		return this.inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getChecked() {
		return this.checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getScoresheetfieldId() {
		return this.scoresheetfieldId;
	}

	public void setScoresheetfieldId(String scoresheetfieldId) {
		this.scoresheetfieldId = scoresheetfieldId;
	}

	public int getScoresheetMasterId() {
		return this.scoresheetMasterId;
	}

	public void setScoresheetMasterId(int scoresheetMasterId) {
		this.scoresheetMasterId = scoresheetMasterId;
	}

	public int getReviewFieldMasterId() {
		return this.reviewFieldMasterId;
	}

	public void setReviewFieldMasterId(int reviewFieldMasterId) {
		this.reviewFieldMasterId = reviewFieldMasterId;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getScoreSheetName() {
		return this.scoreSheetName;
	}

	public void setScoreSheetName(String scoreSheetName) {
		this.scoreSheetName = scoreSheetName;
	}

	public String getGradingSystem() {
		return this.gradingSystem;
	}

	public void setGradingSystem(String gradingSystem) {
		this.gradingSystem = gradingSystem;
	}

	public String getSelectedReviewField() {
		return this.selectedReviewField;
	}

	public void setSelectedReviewField(String selectedReviewField) {
		this.selectedReviewField = selectedReviewField;
	}

	public String getCategoryString() {
		return this.categoryString;
	}

	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getScoreSheetIdList() {
		return this.scoreSheetIdList;
	}

	public void setScoreSheetIdList(String scoreSheetIdList) {
		this.scoreSheetIdList = scoreSheetIdList;
	}

	public int getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public long getScore() {
		return this.score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public long getMaxPoints() {
		return this.maxPoints;
	}

	public void setMaxPoints(long maxPoints) {
		this.maxPoints = maxPoints;
	}

	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public long getNoOfReports() {
		return this.noOfReports;
	}

	public void setNoOfReports(long noOfReports) {
		this.noOfReports = noOfReports;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getGetScoreSheetMasterId() {
		return this.getScoreSheetMasterId;
	}

	public void setGetScoreSheetMasterId(String getScoreSheetMasterId) {
		this.getScoreSheetMasterId = getScoreSheetMasterId;
	}
}