package com.worldcheck.atlas.vo.task.review;

public class ScoreSheetVO {
	private String scoreSheetID;
	private String scoreSheetName;
	private String officeId;
	private String userName;
	private String userId;
	private String fieldId;
	private String fieldName;
	private String performer;
	private String main_Analyist;
	private String userFullName;
	private String crn;

	public String getUserFullName() {
		return this.userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getMain_Analyist() {
		return this.main_Analyist;
	}

	public void setMain_Analyist(String mainAnalyist) {
		this.main_Analyist = mainAnalyist;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public String getFieldId() {
		return this.fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getScoreSheetID() {
		return this.scoreSheetID;
	}

	public void setScoreSheetID(String scoreSheetID) {
		this.scoreSheetID = scoreSheetID;
	}

	public String getScoreSheetName() {
		return this.scoreSheetName;
	}

	public void setScoreSheetName(String scoreSheetName) {
		this.scoreSheetName = scoreSheetName;
	}
}