package com.worldcheck.atlas.scheduler.vo;

public class EmailPrepaymentCaseVo {
	private String crn;
	private String caseDueDate;
	private String emailCM;
	private String emailBDM;
	private String emailWCFinance;
	private String totalBalanceUSD;
	private String receivedType;

	public String getReceivedType() {
		return this.receivedType;
	}

	public void setReceivedType(String receivedType) {
		this.receivedType = receivedType;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCaseDueDate() {
		return this.caseDueDate;
	}

	public void setCaseDueDate(String caseDueDate) {
		this.caseDueDate = caseDueDate;
	}

	public String getEmailCM() {
		return this.emailCM;
	}

	public void setEmailCM(String emailCM) {
		this.emailCM = emailCM;
	}

	public String getEmailBDM() {
		return this.emailBDM;
	}

	public void setEmailBDM(String emailBDM) {
		this.emailBDM = emailBDM;
	}

	public String getEmailWCFinance() {
		return this.emailWCFinance;
	}

	public void setEmailWCFinance(String emailWCFinance) {
		this.emailWCFinance = emailWCFinance;
	}

	public String getTotalBalanceUSD() {
		return this.totalBalanceUSD;
	}

	public void setTotalBalanceUSD(String totalBalanceUSD) {
		this.totalBalanceUSD = totalBalanceUSD;
	}
}