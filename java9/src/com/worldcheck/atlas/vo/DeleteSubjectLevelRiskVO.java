package com.worldcheck.atlas.vo;

public class DeleteSubjectLevelRiskVO {
	private long profileId;
	private long subjectId;
	private String riskCode;
	private String isRiskMandatory;
	private long countryMasterId;
	private String crn;
	private String action;
	private String task;

	public String getIsRiskMandatory() {
		return this.isRiskMandatory;
	}

	public void setIsRiskMandatory(String isRiskMandatory) {
		this.isRiskMandatory = isRiskMandatory;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getProfileId() {
		return this.profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public long getCountryMasterId() {
		return this.countryMasterId;
	}

	public void setCountryMasterId(long countryMasterId) {
		this.countryMasterId = countryMasterId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}
}