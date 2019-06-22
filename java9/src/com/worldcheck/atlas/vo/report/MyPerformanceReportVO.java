package com.worldcheck.atlas.vo.report;

import java.util.List;

public class MyPerformanceReportVO {
	private String reportType;
	private String month;
	private String numOfCasesCMP;
	private String numOfCasesWIP;
	private List<MyPerformanceReportVO> listMyPerformanceReportVO;
	private String year;
	private String analyst;
	private String crn;
	private String numOfCases;
	private String cmpJLP;
	private String wipJLP;

	public String getCmpJLP() {
		return this.cmpJLP;
	}

	public void setCmpJLP(String cmpJLP) {
		this.cmpJLP = cmpJLP;
	}

	public String getWipJLP() {
		return this.wipJLP;
	}

	public void setWipJLP(String wipJLP) {
		this.wipJLP = wipJLP;
	}

	public String getNumOfCases() {
		return this.numOfCases;
	}

	public void setNumOfCases(String numOfCases) {
		this.numOfCases = numOfCases;
	}

	public String getNumOfCasesCMP() {
		return this.numOfCasesCMP;
	}

	public void setNumOfCasesCMP(String numOfCasesCMP) {
		this.numOfCasesCMP = numOfCasesCMP;
	}

	public String getNumOfCasesWIP() {
		return this.numOfCasesWIP;
	}

	public void setNumOfCasesWIP(String numOfCasesWIP) {
		this.numOfCasesWIP = numOfCasesWIP;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<MyPerformanceReportVO> getListMyPerformanceReportVO() {
		return this.listMyPerformanceReportVO;
	}

	public void setListMyPerformanceReportVO(List<MyPerformanceReportVO> listMyPerformanceReportVO) {
		this.listMyPerformanceReportVO = listMyPerformanceReportVO;
	}
}