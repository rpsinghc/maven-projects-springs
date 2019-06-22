package com.worldcheck.atlas.vo.report;

public class CaseDueTodayVO {
	private String crn;
	private String expressCase;
	private String priority;
	private String office;
	private String primaryAnalystName;
	private String primarySubName;
	private String processCycle;
	private String caseManager;
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

	public String getExpressCase() {
		return this.expressCase;
	}

	public void setExpressCase(String expressCase) {
		this.expressCase = expressCase;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getPrimaryAnalystName() {
		return this.primaryAnalystName;
	}

	public void setPrimaryAnalystName(String primaryAnalystName) {
		this.primaryAnalystName = primaryAnalystName;
	}

	public String getPrimarySubName() {
		return this.primarySubName;
	}

	public void setPrimarySubName(String primarySubName) {
		this.primarySubName = primarySubName;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}
}