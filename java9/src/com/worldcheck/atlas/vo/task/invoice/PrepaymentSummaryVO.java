package com.worldcheck.atlas.vo.task.invoice;

public class PrepaymentSummaryVO {
	private String crn;
	private String currency_Code;
	private String caseCreationDate;
	private String case_Fee;
	private String credit;
	private String multiple_Year_Bonus;
	private String total_Prepayment_Credit;
	private String total_Spent;
	private String total_Balance;
	private String total_Prepayment_Credit_USD;
	private String total_Spent_USD;
	private String total_Balance_USD;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCurrency_Code() {
		return this.currency_Code;
	}

	public void setCurrency_Code(String currencyCode) {
		this.currency_Code = currencyCode;
	}

	public String getCaseCreationDate() {
		return this.caseCreationDate;
	}

	public void setCaseCreationDate(String caseCreationDate) {
		this.caseCreationDate = caseCreationDate;
	}

	public String getCase_Fee() {
		return this.case_Fee;
	}

	public void setCase_Fee(String caseFee) {
		this.case_Fee = caseFee;
	}

	public String getCredit() {
		return this.credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getMultiple_Year_Bonus() {
		return this.multiple_Year_Bonus;
	}

	public void setMultiple_Year_Bonus(String multipleYearBonus) {
		this.multiple_Year_Bonus = multipleYearBonus;
	}

	public String getTotal_Prepayment_Credit() {
		return this.total_Prepayment_Credit;
	}

	public void setTotal_Prepayment_Credit(String totalPrepaymentCredit) {
		this.total_Prepayment_Credit = totalPrepaymentCredit;
	}

	public String getTotal_Spent() {
		return this.total_Spent;
	}

	public void setTotal_Spent(String totalSpent) {
		this.total_Spent = totalSpent;
	}

	public String getTotal_Balance() {
		return this.total_Balance;
	}

	public void setTotal_Balance(String totalBalance) {
		this.total_Balance = totalBalance;
	}

	public String getTotal_Prepayment_Credit_USD() {
		return this.total_Prepayment_Credit_USD;
	}

	public void setTotal_Prepayment_Credit_USD(String totalPrepaymentCreditUSD) {
		this.total_Prepayment_Credit_USD = totalPrepaymentCreditUSD;
	}

	public String getTotal_Spent_USD() {
		return this.total_Spent_USD;
	}

	public void setTotal_Spent_USD(String totalSpentUSD) {
		this.total_Spent_USD = totalSpentUSD;
	}

	public String getTotal_Balance_USD() {
		return this.total_Balance_USD;
	}

	public void setTotal_Balance_USD(String totalBalanceUSD) {
		this.total_Balance_USD = totalBalanceUSD;
	}
}