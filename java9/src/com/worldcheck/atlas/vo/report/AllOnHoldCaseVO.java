package com.worldcheck.atlas.vo.report;

public class AllOnHoldCaseVO {
	private String crn;
	private String clientName;
	private String caseManager;
	private String subjectName;
	private String analystName;
	private String officeName;
	private String caseReceivedDate;
	private String caseDueDate;
	private String totalDays;
	private int start;
	private int limit;
	private String sortColumnName;
	private String sortType;

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

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getAnalystName() {
		return this.analystName;
	}

	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getCaseReceivedDate() {
		return this.caseReceivedDate;
	}

	public void setCaseReceivedDate(String caseReceivedDate) {
		this.caseReceivedDate = caseReceivedDate;
	}

	public String getCaseDueDate() {
		return this.caseDueDate;
	}

	public void setCaseDueDate(String caseDueDate) {
		this.caseDueDate = caseDueDate;
	}

	public String getTotalDays() {
		return this.totalDays;
	}

	public void setTotalDays(String totalDays) {
		this.totalDays = totalDays;
	}
}