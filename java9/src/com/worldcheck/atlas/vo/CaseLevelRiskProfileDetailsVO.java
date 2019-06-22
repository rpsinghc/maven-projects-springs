package com.worldcheck.atlas.vo;

public class CaseLevelRiskProfileDetailsVO {
	private String reportType;
	private String subReportType;
	private String clientCode;
	private String crn;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
}