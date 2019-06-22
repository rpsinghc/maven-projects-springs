package com.worldcheck.atlas.vo.report;

import java.util.List;

public class OverdueReportVO {
	private String crn;
	private String primarySubName;
	private String countryName;
	private String interim1DueDate;
	private String interim2DueDate;
	private String finalDate;
	private String overDueBy;
	private String caseManager;
	private String office;
	private String overdueStage;
	private int start;
	private int limit;
	private String sortColumnName;
	private String sortType;
	private List<OverdueReportVO> overdueReportVOList;

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getOverdueStage() {
		return this.overdueStage;
	}

	public void setOverdueStage(String overdueStage) {
		this.overdueStage = overdueStage;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public List<OverdueReportVO> getOverdueReportVOList() {
		return this.overdueReportVOList;
	}

	public void setOverdueReportVOList(List<OverdueReportVO> overdueReportVOList) {
		this.overdueReportVOList = overdueReportVOList;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getPrimarySubName() {
		return this.primarySubName;
	}

	public void setPrimarySubName(String primarySubName) {
		this.primarySubName = primarySubName;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getInterim1DueDate() {
		return this.interim1DueDate;
	}

	public void setInterim1DueDate(String interim1DueDate) {
		this.interim1DueDate = interim1DueDate;
	}

	public String getInterim2DueDate() {
		return this.interim2DueDate;
	}

	public void setInterim2DueDate(String interim2DueDate) {
		this.interim2DueDate = interim2DueDate;
	}

	public String getFinalDate() {
		return this.finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public String getOverDueBy() {
		return this.overDueBy;
	}

	public void setOverDueBy(String overDueBy) {
		this.overDueBy = overDueBy;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}
}