package com.worldcheck.atlas.vo;

public class CaseCreationRiskAssociationVO {
	private Long profileId;
	private Long countryBrkDownId;
	private Integer visibleToClient;
	private String countryCode;
	private int subjectType;
	private int isRiskMandatory;
	private int isRiskApplied;
	private int hasCntryBreakdown;
	private int riskType;
	private String riskCode;
	private String crn;
	private String task;
	private Long riskCategoryId;
	private Long attributeId;
	private String attributeValue;
	private String attributeName;
	private Long subjectId;
	private String industryCode;
	private Long countryMasterId;

	public Integer getVisibleToClient() {
		return this.visibleToClient;
	}

	public void setVisibleToClient(Integer visibleToClient) {
		this.visibleToClient = visibleToClient;
	}

	public Long getCountryBrkDownId() {
		return this.countryBrkDownId;
	}

	public void setCountryBrkDownId(Long countryBrkDownId) {
		this.countryBrkDownId = countryBrkDownId;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public int getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Long getProfileId() {
		return this.profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public int getRiskType() {
		return this.riskType;
	}

	public void setRiskType(int riskType) {
		this.riskType = riskType;
	}

	public int getIsRiskMandatory() {
		return this.isRiskMandatory;
	}

	public void setIsRiskMandatory(int isRiskMandatory) {
		this.isRiskMandatory = isRiskMandatory;
	}

	public int getIsRiskApplied() {
		return this.isRiskApplied;
	}

	public void setIsRiskApplied(int isRiskApplied) {
		this.isRiskApplied = isRiskApplied;
	}

	public int getHasCntryBreakdown() {
		return this.hasCntryBreakdown;
	}

	public void setHasCntryBreakdown(int hasCntryBreakdown) {
		this.hasCntryBreakdown = hasCntryBreakdown;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public Long getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(Long riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public Long getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getIndustryCode() {
		return this.industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public Long getCountryMasterId() {
		return this.countryMasterId;
	}

	public void setCountryMasterId(Long countryMasterId) {
		this.countryMasterId = countryMasterId;
	}
}