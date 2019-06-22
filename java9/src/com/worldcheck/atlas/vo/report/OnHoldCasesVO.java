package com.worldcheck.atlas.vo.report;

import java.util.Date;

public class OnHoldCasesVO {
	private String CRN;
	private String clientName;
	private String caseManager;
	private String subject;
	private String analyst;
	private String office;
	private Date caseReceivedDate;
	private Date caseDueDate;
	private int dueDays;

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String crn) {
		this.CRN = crn;
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

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public Date getCaseReceivedDate() {
		return this.caseReceivedDate;
	}

	public void setCaseReceivedDate(Date caseReceivedDate) {
		this.caseReceivedDate = caseReceivedDate;
	}

	public Date getCaseDueDate() {
		return this.caseDueDate;
	}

	public void setCaseDueDate(Date caseDueDate) {
		this.caseDueDate = caseDueDate;
	}

	public int getDueDays() {
		return this.dueDays;
	}

	public void setDueDays(int dueDays) {
		this.dueDays = dueDays;
	}
}