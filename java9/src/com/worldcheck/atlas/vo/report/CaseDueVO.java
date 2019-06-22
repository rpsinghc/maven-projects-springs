package com.worldcheck.atlas.vo.report;

public class CaseDueVO {
	private String CRN;
	private String subject;
	private String country;
	private String caseManager;
	private String officeName;
	private String primaryAnalyst;
	private boolean expressCase;
	private String casePriority;
	private String processCycle;

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String crn) {
		this.CRN = crn;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getPrimaryAnalyst() {
		return this.primaryAnalyst;
	}

	public void setPrimaryAnalyst(String primaryAnalyst) {
		this.primaryAnalyst = primaryAnalyst;
	}

	public boolean isExpressCase() {
		return this.expressCase;
	}

	public void setExpressCase(boolean expressCase) {
		this.expressCase = expressCase;
	}

	public String getCasePriority() {
		return this.casePriority;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}
}