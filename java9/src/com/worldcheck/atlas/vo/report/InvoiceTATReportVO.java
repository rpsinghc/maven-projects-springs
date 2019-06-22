package com.worldcheck.atlas.vo.report;

public class InvoiceTATReportVO {
	private String totalCaseCount;
	private String within48Hours;
	private String over48Hours;

	public String getTotalCaseCount() {
		return this.totalCaseCount;
	}

	public void setTotalCaseCount(String totalCaseCount) {
		this.totalCaseCount = totalCaseCount;
	}

	public String getWithin48Hours() {
		return this.within48Hours;
	}

	public void setWithin48Hours(String within48Hours) {
		this.within48Hours = within48Hours;
	}

	public String getOver48Hours() {
		return this.over48Hours;
	}

	public void setOver48Hours(String over48Hours) {
		this.over48Hours = over48Hours;
	}
}