package com.worldcheck.atlas.vo.masters;

public class RiskAggregationVO {
	private String crn;
	private long catId;
	private long subId;
	private long riskType;
	private long aggrValue;
	private long riskAggrId;
	private String updatedBy;

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public long getRiskAggrId() {
		return this.riskAggrId;
	}

	public void setRiskAggrId(long riskAggrId) {
		this.riskAggrId = riskAggrId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public long getCatId() {
		return this.catId;
	}

	public void setCatId(long catId) {
		this.catId = catId;
	}

	public long getSubId() {
		return this.subId;
	}

	public void setSubId(long subId) {
		this.subId = subId;
	}

	public long getRiskType() {
		return this.riskType;
	}

	public void setRiskType(long riskType) {
		this.riskType = riskType;
	}

	public long getAggrValue() {
		return this.aggrValue;
	}

	public void setAggrValue(long aggrValue) {
		this.aggrValue = aggrValue;
	}
}