package com.worldcheck.atlas.vo;

public class CaseStatus {
	private String caseStatusId;
	private String description;
	private String updatedBy;
	private String updatedOn;

	public String getCaseStatusId() {
		return this.caseStatusId;
	}

	public void setCaseStatusId(String caseStatusId) {
		this.caseStatusId = caseStatusId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
}