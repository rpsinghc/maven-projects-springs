package com.worldcheck.atlas.vo.task.review;

import java.util.List;

public class ReviewScoreSheetSubCatgVO {
	private int review_ss_subcateg_id;
	private String review_ss_category_id;
	private String ss_sub_category_id;
	private String sub_category;
	private String cnt_of_errors;
	private String points;
	private String score;
	private String comments;
	private String applicability;
	private String select;
	private String updated_by;
	private String updated_on;
	private List<ReviewScoreSheetSubCatgVO> gradingPoint;

	public int getReview_ss_subcateg_id() {
		return this.review_ss_subcateg_id;
	}

	public void setReview_ss_subcateg_id(int reviewSsSubcategId) {
		this.review_ss_subcateg_id = reviewSsSubcategId;
	}

	public String getReview_ss_category_id() {
		return this.review_ss_category_id;
	}

	public void setReview_ss_category_id(String reviewSsCategoryId) {
		this.review_ss_category_id = reviewSsCategoryId;
	}

	public String getSs_sub_category_id() {
		return this.ss_sub_category_id;
	}

	public void setSs_sub_category_id(String ssSubCategoryId) {
		this.ss_sub_category_id = ssSubCategoryId;
	}

	public String getSub_category() {
		return this.sub_category;
	}

	public void setSub_category(String subCategory) {
		this.sub_category = subCategory;
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

	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getApplicability() {
		return this.applicability;
	}

	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}

	public String getSelect() {
		return this.select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getUpdated_by() {
		return this.updated_by;
	}

	public void setUpdated_by(String updatedBy) {
		this.updated_by = updatedBy;
	}

	public String getUpdated_on() {
		return this.updated_on;
	}

	public void setUpdated_on(String updatedOn) {
		this.updated_on = updatedOn;
	}

	public List<ReviewScoreSheetSubCatgVO> getGradingPoint() {
		return this.gradingPoint;
	}

	public void setGradingPoint(List<ReviewScoreSheetSubCatgVO> gradingPoint) {
		this.gradingPoint = gradingPoint;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}