package com.worldcheck.atlas.vo;

import java.util.List;

public class SubjectLevelRiskProfileDetailsVO {
	private long reportType;
	private long subReportType;
	private String clientCode;
	private String crn;
	private String subjectCountry;
	private List<Integer> reIds;
	private int subjectId;
	private String reIdString;
	private int subjectType;

	public String getReIdString() {
		return this.reIdString;
	}

	public void setReIdString(String reIdString) {
		this.reIdString = reIdString;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public List<Integer> getReIds() {
		return this.reIds;
	}

	public void setReIds(List<Integer> reIds) {
		this.reIds = reIds;
	}

	public String getSubjectCountry() {
		return this.subjectCountry;
	}

	public void setSubjectCountry(String subjectCountry) {
		this.subjectCountry = subjectCountry;
	}

	public int getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

	public long getReportType() {
		return this.reportType;
	}

	public void setReportType(long reportType) {
		this.reportType = reportType;
	}

	public long getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(long subReportType) {
		this.subReportType = subReportType;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}
}