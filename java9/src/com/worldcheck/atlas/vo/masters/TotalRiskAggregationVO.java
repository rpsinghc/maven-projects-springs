package com.worldcheck.atlas.vo.masters;

public class TotalRiskAggregationVO {
	private long totalRiskAggrId;
	private String crn;
	private int totalCaseLevelAggrValue;
	private int totalSubLevelAggrValue;
	private int totalCRNLevelAggrValue;
	private String updatedBy;

	public long getTotalRiskAggrId() {
		return this.totalRiskAggrId;
	}

	public void setTotalRiskAggrId(long totalRiskAggrId) {
		this.totalRiskAggrId = totalRiskAggrId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getTotalCaseLevelAggrValue() {
		return this.totalCaseLevelAggrValue;
	}

	public void setTotalCaseLevelAggrValue(int totalCaseLevelAggrValue) {
		this.totalCaseLevelAggrValue = totalCaseLevelAggrValue;
	}

	public int getTotalSubLevelAggrValue() {
		return this.totalSubLevelAggrValue;
	}

	public void setTotalSubLevelAggrValue(int totalSubLevelAggrValue) {
		this.totalSubLevelAggrValue = totalSubLevelAggrValue;
	}

	public int getTotalCRNLevelAggrValue() {
		return this.totalCRNLevelAggrValue;
	}

	public void setTotalCRNLevelAggrValue(int totalCRNLevelAggrValue) {
		this.totalCRNLevelAggrValue = totalCRNLevelAggrValue;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}