package com.worldcheck.atlas.vo.task.review;

import java.util.List;

public class ReviewScoreSheetCategoryVO {
	private int review_ss_categ_id;
	private String review_sheet_id;
	private String ss_category_id;
	private String category;
	private String comments;
	private String updated_by;
	private String updated_on;
	private List<ReviewScoreSheetSubCatgVO> scoreSheetSubCategory;

	public int getReview_ss_categ_id() {
		return this.review_ss_categ_id;
	}

	public void setReview_ss_categ_id(int reviewSsCategId) {
		this.review_ss_categ_id = reviewSsCategId;
	}

	public String getReview_sheet_id() {
		return this.review_sheet_id;
	}

	public void setReview_sheet_id(String reviewSheetId) {
		this.review_sheet_id = reviewSheetId;
	}

	public String getSs_category_id() {
		return this.ss_category_id;
	}

	public void setSs_category_id(String ssCategoryId) {
		this.ss_category_id = ssCategoryId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public List<ReviewScoreSheetSubCatgVO> getScoreSheetSubCategory() {
		return this.scoreSheetSubCategory;
	}

	public void setScoreSheetSubCategory(List<ReviewScoreSheetSubCatgVO> scoreSheetSubCategory) {
		this.scoreSheetSubCategory = scoreSheetSubCategory;
	}
}