package com.worldcheck.atlas.vo;

public class SubjectLevelAggregation {
	private int totalAggregationId;
	private int subjectAggrId;
	private String crn;
	private int eachSubLvlAggr;
	private Long subjectId;
	private int hasCountryBrkDown;
	private String updatedBy;

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getTotalAggregationId() {
		return this.totalAggregationId;
	}

	public void setTotalAggregationId(int totalAggregationId) {
		this.totalAggregationId = totalAggregationId;
	}

	public int getSubjectAggrId() {
		return this.subjectAggrId;
	}

	public void setSubjectAggrId(int subjectAggrId) {
		this.subjectAggrId = subjectAggrId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getEachSubLvlAggr() {
		return this.eachSubLvlAggr;
	}

	public void setEachSubLvlAggr(int eachSubLvlAggr) {
		this.eachSubLvlAggr = eachSubLvlAggr;
	}

	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public int getHasCountryBrkDown() {
		return this.hasCountryBrkDown;
	}

	public void setHasCountryBrkDown(int hasCountryBrkDown) {
		this.hasCountryBrkDown = hasCountryBrkDown;
	}
}