package com.worldcheck.atlas.vo;

public class RiskProfileHistoryVO {
	private long historyId;
	private String crn;
	private long riskCategoryId;
	private String riskCode;
	private String action;
	private String task;
	private long attributeId;
	private long subjectId;
	private String countryCode;
	private long countryMasterId;
	private String attributeName;
	private String oldValue;
	private String newValue;
	private String updatedBy;

	public long getCountryMasterId() {
		return this.countryMasterId;
	}

	public void setCountryMasterId(long countryMasterId) {
		this.countryMasterId = countryMasterId;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public long getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(long historyId) {
		this.historyId = historyId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public long getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(long riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public long getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public String getOldValue() {
		return this.oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return this.newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}
}