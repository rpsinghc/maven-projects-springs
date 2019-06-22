package com.worldcheck.atlas.vo.report;

public class NCRSReportTypeVO {
	private String reportCrnInitial;
	private int caseCount;
	private float casePercent;

	public void setReportCrnInitial(String reportCrnInitial) {
		this.reportCrnInitial = reportCrnInitial;
	}

	public String getReportCrnInitial() {
		return this.reportCrnInitial;
	}

	public void setCaseCount(int caseCount) {
		this.caseCount = caseCount;
	}

	public int getCaseCount() {
		return this.caseCount;
	}

	public void setCasePercent(float casePercent) {
		this.casePercent = casePercent;
	}

	public float getCasePercent() {
		return this.casePercent;
	}
}