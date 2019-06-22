package com.worldcheck.atlas.vo.noncrnexpenditure;

public class NonCRNExpenditureVO {
	private int nonCrnExpenditureId;
	private String country;
	private String vendorName;
	private String commissioningDate;
	private String paymentCycle;
	private String noOfCycles;
	private String amount;
	private String currency;
	private String comments;
	private String userName;

	public int getNonCrnExpenditureId() {
		return this.nonCrnExpenditureId;
	}

	public void setNonCrnExpenditureId(int nonCrnExpenditureId) {
		this.nonCrnExpenditureId = nonCrnExpenditureId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getVendorName() {
		return this.vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getCommissioningDate() {
		return this.commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public String getPaymentCycle() {
		return this.paymentCycle;
	}

	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public String getNoOfCycles() {
		return this.noOfCycles;
	}

	public void setNoOfCycles(String noOfCycles) {
		this.noOfCycles = noOfCycles;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String toString() {
		return "amount : " + this.getAmount() + " comment: " + this.getComments() + " comm date: "
				+ this.getCommissioningDate() + " country : " + this.getCountry() + " currency : " + this.getCurrency()
				+ " no of cycles : " + this.getNoOfCycles() + " payment cycle:" + this.getPaymentCycle()
				+ " vendor name : " + this.getVendorName();
	}
}