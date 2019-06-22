package com.worldcheck.atlas.vo.task.review;

public class ReviewScoreSheetTemplateVO {
	private String category;
	private String sheet_id;
	private String sub_category;
	private String category_id;
	private String cnt_of_errors;
	private String points;
	private String scoresheet_subcateg_id;
	private int scores;
	private String comments;
	private String applicability;
	private String selected;
	private int number;
	private int maxSubCategoryPoints;

	public int getMaxSubCategoryPoints() {
		return this.maxSubCategoryPoints;
	}

	public void setMaxSubCategoryPoints(int maxSubCategoryPoints) {
		this.maxSubCategoryPoints = maxSubCategoryPoints;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getApplicability() {
		return this.applicability;
	}

	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}

	public String getSelected() {
		return this.selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public int getScores() {
		return this.scores;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSheet_id() {
		return this.sheet_id;
	}

	public void setSheet_id(String sheetId) {
		this.sheet_id = sheetId;
	}

	public String getSub_category() {
		return this.sub_category;
	}

	public void setSub_category(String subCategory) {
		this.sub_category = subCategory;
	}

	public String getCategory_id() {
		return this.category_id;
	}

	public void setCategory_id(String categoryId) {
		this.category_id = categoryId;
	}

	public String getCnt_of_errors() {
		return this.cnt_of_errors;
	}

	public void setCnt_of_errors(String cntOfErrors) {
		this.cnt_of_errors = cntOfErrors;
	}

	public String getPoints() {
		return this.points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getScoresheet_subcateg_id() {
		return this.scoresheet_subcateg_id;
	}

	public void setScoresheet_subcateg_id(String scoresheetSubcategId) {
		this.scoresheet_subcateg_id = scoresheetSubcategId;
	}
}