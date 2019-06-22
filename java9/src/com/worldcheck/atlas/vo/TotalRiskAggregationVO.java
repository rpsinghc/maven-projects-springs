package com.worldcheck.atlas.vo;

public class TotalRiskAggregationVO {
	private int totalAggregationId;
	private String crn;
	private int overallCaseLvlAggr;
	private int eachSubLvlAggr;
	private int overallSubLvlAggr;
	private int overallCrnLvlAggr;
	private Long subjectId;
	private int riskCategoryId;
	private int riskType;
	private int aggregateValue;
	private int hasCountryBrkDown;

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

	public int getHasCountryBrkDown() {
		return this.hasCountryBrkDown;
	}

	public void setHasCountryBrkDown(int hasCountryBrkDown) {
		this.hasCountryBrkDown = hasCountryBrkDown;
	}

	public int getTotalAggregationId() {
		return this.totalAggregationId;
	}

	public void setTotalAggregationId(int totalAggregationId) {
		this.totalAggregationId = totalAggregationId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getOverallCaseLvlAggr() {
		return this.overallCaseLvlAggr;
	}

	public void setOverallCaseLvlAggr(int overallCaseLvlAggr) {
		this.overallCaseLvlAggr = overallCaseLvlAggr;
	}

	public int getEachSubLvlAggr() {
		return this.eachSubLvlAggr;
	}

	public void setEachSubLvlAggr(int eachSubLvlAggr) {
		this.eachSubLvlAggr = eachSubLvlAggr;
	}

	public int getOverallSubLvlAggr() {
		return this.overallSubLvlAggr;
	}

	public void setOverallSubLvlAggr(int overallSubLvlAggr) {
		this.overallSubLvlAggr = overallSubLvlAggr;
	}

	public int getOverallCrnLvlAggr() {
		return this.overallCrnLvlAggr;
	}

	public void setOverallCrnLvlAggr(int overallCrnLvlAggr) {
		this.overallCrnLvlAggr = overallCrnLvlAggr;
	}
}