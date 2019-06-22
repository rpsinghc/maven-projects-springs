package com.worldcheck.atlas.vo;

public class RiskAggregationVO {
	private int hasCountryBrkDown;
	private int riskAggregationId;
	private String crn;
	private Long subjectId;
	private int riskCategoryId;
	private int riskType;
	private int aggregateValue;
	private int ragColor;
	private int updatedBy;

	public int getHasCountryBrkDown() {
		return this.hasCountryBrkDown;
	}

	public void setHasCountryBrkDown(int hasCountryBrkDown) {
		this.hasCountryBrkDown = hasCountryBrkDown;
	}

	public int getRiskAggregationId() {
		return this.riskAggregationId;
	}

	public void setRiskAggregationId(int riskAggregationId) {
		this.riskAggregationId = riskAggregationId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public int getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(int riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public int getRiskType() {
		return this.riskType;
	}

	public void setRiskType(int riskType) {
		this.riskType = riskType;
	}

	public int getAggregateValue() {
		return this.aggregateValue;
	}

	public void setAggregateValue(int aggregateValue) {
		this.aggregateValue = aggregateValue;
	}

	public int getRagColor() {
		return this.ragColor;
	}

	public void setRagColor(int ragColor) {
		this.ragColor = ragColor;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
}