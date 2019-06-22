package com.worldcheck.atlas.vo.task.review;

import java.util.List;

public class ReviewScoreSheetVO {
	private String sheet_id;
	private String date_of_review;
	private String complexity;
	private String review_time;
	private String final_interim;
	private String primary_team;
	private String supporting_team_1;
	private String supporting_team_2;
	private String supporting_team_3;
	private String supporting_team_4;
	private String supporting_team_5;
	private String supporting_team_6;
	private String report_due_date;
	private String reviewer_1;
	private String reviewer_2;
	private String reviewer_3;
	private String general_comment;
	private String sr_analyst;
	private String editor;
	private String date_report_filed;
	private String complete_os_rcvd_date;
	private String reviewers;
	private String scoresheet_name;
	private String office_id;
	private String reviewer;
	private String analyst;
	private String score;
	private String quality_percent;
	private String updated_by;
	private String crn;
	private int reviewSSID;
	private String[] category;
	private String[] categoryId;
	private String[] sub_category;
	private String[] sub_categoryId;
	private String[] scores;
	private String[] cnt_of_errors;
	private String[] points;
	private String[] comments;
	private String[] applicablity;
	private String[] select;
	private List<ReviewScoreSheetCategoryVO> reviewCategory;
	private String officeName;

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String[] getSelect() {
		return this.select;
	}

	public void setSelect(String[] select) {
		this.select = select;
	}

	public String[] getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(String[] categoryId) {
		this.categoryId = categoryId;
	}

	public String[] getSub_categoryId() {
		return this.sub_categoryId;
	}

	public void setSub_categoryId(String[] subCategoryId) {
		this.sub_categoryId = subCategoryId;
	}

	public String[] getCategory() {
		return this.category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}

	public String[] getSub_category() {
		return this.sub_category;
	}

	public void setSub_category(String[] subCategory) {
		this.sub_category = subCategory;
	}

	public String[] getScores() {
		return this.scores;
	}

	public void setScores(String[] scores) {
		this.scores = scores;
	}

	public String[] getCnt_of_errors() {
		return this.cnt_of_errors;
	}

	public void setCnt_of_errors(String[] cntOfErrors) {
		this.cnt_of_errors = cntOfErrors;
	}

	public String[] getPoints() {
		return this.points;
	}

	public void setPoints(String[] points) {
		this.points = points;
	}

	public String[] getComments() {
		return this.comments;
	}

	public void setComments(String[] comments) {
		this.comments = comments;
	}

	public String[] getApplicablity() {
		return this.applicablity;
	}

	public void setApplicablity(String[] applicablity) {
		this.applicablity = applicablity;
	}

	public String getReviewers() {
		return this.reviewers;
	}

	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}

	public int getReviewSSID() {
		return this.reviewSSID;
	}

	public void setReviewSSID(int reviewSSID) {
		this.reviewSSID = reviewSSID;
	}

	public String getScoresheet_name() {
		return this.scoresheet_name;
	}

	public void setScoresheet_name(String scoresheetName) {
		this.scoresheet_name = scoresheetName;
	}

	public String getOffice_id() {
		return this.office_id;
	}

	public void setOffice_id(String officeId) {
		this.office_id = officeId;
	}

	public String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getQuality_percent() {
		return this.quality_percent;
	}

	public void setQuality_percent(String qualityPercent) {
		this.quality_percent = qualityPercent;
	}

	public String getUpdated_by() {
		return this.updated_by;
	}

	public void setUpdated_by(String updatedBy) {
		this.updated_by = updatedBy;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getSheet_id() {
		return this.sheet_id;
	}

	public void setSheet_id(String sheetId) {
		this.sheet_id = sheetId;
	}

	public String getDate_of_review() {
		return this.date_of_review;
	}

	public void setDate_of_review(String dateOfReview) {
		this.date_of_review = dateOfReview;
	}

	public String getComplexity() {
		return this.complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	public String getReview_time() {
		return this.review_time;
	}

	public void setReview_time(String reviewTime) {
		this.review_time = reviewTime;
	}

	public String getFinal_interim() {
		return this.final_interim;
	}

	public void setFinal_interim(String finalInterim) {
		this.final_interim = finalInterim;
	}

	public String getPrimary_team() {
		return this.primary_team;
	}

	public void setPrimary_team(String primaryTeam) {
		this.primary_team = primaryTeam;
	}

	public String getSupporting_team_1() {
		return this.supporting_team_1;
	}

	public void setSupporting_team_1(String supportingTeam_1) {
		this.supporting_team_1 = supportingTeam_1;
	}

	public String getSupporting_team_2() {
		return this.supporting_team_2;
	}

	public void setSupporting_team_2(String supportingTeam_2) {
		this.supporting_team_2 = supportingTeam_2;
	}

	public String getSupporting_team_3() {
		return this.supporting_team_3;
	}

	public void setSupporting_team_3(String supportingTeam_3) {
		this.supporting_team_3 = supportingTeam_3;
	}

	public String getSupporting_team_4() {
		return this.supporting_team_4;
	}

	public void setSupporting_team_4(String supportingTeam_4) {
		this.supporting_team_4 = supportingTeam_4;
	}

	public String getSupporting_team_5() {
		return this.supporting_team_5;
	}

	public void setSupporting_team_5(String supportingTeam_5) {
		this.supporting_team_5 = supportingTeam_5;
	}

	public String getSupporting_team_6() {
		return this.supporting_team_6;
	}

	public void setSupporting_team_6(String supportingTeam_6) {
		this.supporting_team_6 = supportingTeam_6;
	}

	public String getReport_due_date() {
		return this.report_due_date;
	}

	public void setReport_due_date(String reportDueDate) {
		this.report_due_date = reportDueDate;
	}

	public String getReviewer_1() {
		return this.reviewer_1;
	}

	public void setReviewer_1(String reviewer_1) {
		this.reviewer_1 = reviewer_1;
	}

	public String getReviewer_2() {
		return this.reviewer_2;
	}

	public void setReviewer_2(String reviewer_2) {
		this.reviewer_2 = reviewer_2;
	}

	public String getReviewer_3() {
		return this.reviewer_3;
	}

	public void setReviewer_3(String reviewer_3) {
		this.reviewer_3 = reviewer_3;
	}

	public String getGeneral_comment() {
		return this.general_comment;
	}

	public void setGeneral_comment(String generalComment) {
		this.general_comment = generalComment;
	}

	public String getSr_analyst() {
		return this.sr_analyst;
	}

	public void setSr_analyst(String srAnalyst) {
		this.sr_analyst = srAnalyst;
	}

	public String getEditor() {
		return this.editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getDate_report_filed() {
		return this.date_report_filed;
	}

	public void setDate_report_filed(String dateReportFiled) {
		this.date_report_filed = dateReportFiled;
	}

	public String getComplete_os_rcvd_date() {
		return this.complete_os_rcvd_date;
	}

	public void setComplete_os_rcvd_date(String completeOsRcvdDate) {
		this.complete_os_rcvd_date = completeOsRcvdDate;
	}

	public List<ReviewScoreSheetCategoryVO> getReviewCategory() {
		return this.reviewCategory;
	}

	public void setReviewCategory(List<ReviewScoreSheetCategoryVO> reviewCategory) {
		this.reviewCategory = reviewCategory;
	}
}