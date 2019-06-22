package com.worldcheck.atlas.vo.task.invoice;

public class AccountsLinkcasesVO {
	private long accountsLinkCaseID;
	private String linkedCRN;
	private String currency_code;
	private String caseFee_USD;
	private String updateBy;
	private String crn;
	private String registerNo;
	private String caseFee;
	private Long case_status_id;
	private String caseStatus;
	private String withCharges;

	public String getCurrency_code() {
		return this.currency_code;
	}

	public void setCurrency_code(String currencyCode) {
		this.currency_code = currencyCode;
	}

	public String getCaseFee_USD() {
		return this.caseFee_USD;
	}

	public void setCaseFee_USD(String caseFeeUSD) {
		this.caseFee_USD = caseFeeUSD;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public Long getCase_status_id() {
		return this.case_status_id;
	}

	public void setCase_status_id(Long caseStatusId) {
		this.case_status_id = caseStatusId;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getWithCharges() {
		return this.withCharges;
	}

	public void setWithCharges(String withCharges) {
		this.withCharges = withCharges;
	}

	public String getRegisterNo() {
		return this.registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public long getAccountsLinkCaseID() {
		return this.accountsLinkCaseID;
	}

	public void setAccountsLinkCaseID(long accountsLinkCaseID) {
		this.accountsLinkCaseID = accountsLinkCaseID;
	}

	public String getLinkedCRN() {
		return this.linkedCRN;
	}

	public void setLinkedCRN(String linkedCRN) {
		this.linkedCRN = linkedCRN;
	}
}