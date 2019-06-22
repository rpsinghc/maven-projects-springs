package com.worldcheck.atlas.vo.report;

import java.util.List;

public class CasesReceivedSummaryVO {
	private int officeId;
	private String office;
	private List<NCRSReportTypeVO> reportTypeList;
	private int total;

	public int getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(int officeId) {
		this.officeId = officeId;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setReportTypeList(List<NCRSReportTypeVO> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public List<NCRSReportTypeVO> getReportTypeList() {
		return this.reportTypeList;
	}
}