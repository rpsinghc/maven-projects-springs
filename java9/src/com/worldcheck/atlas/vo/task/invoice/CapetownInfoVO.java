package com.worldcheck.atlas.vo.task.invoice;

public class CapetownInfoVO {
	private String caseFee;
	private String disbursment;
	private String ctDate;
	private String invoiceCurrencyCode;
	private String invoiceDate;
	private int capetownID;
	private String invoiceNO;
	private String invoiceAmount;
	private String discount;
	private String crn;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCtDate() {
		return this.ctDate;
	}

	public void setCtDate(String ctDate) {
		this.ctDate = ctDate;
	}

	public String getInvoiceCurrencyCode() {
		return this.invoiceCurrencyCode;
	}

	public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
		this.invoiceCurrencyCode = invoiceCurrencyCode;
	}

	public String getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getCapetownID() {
		return this.capetownID;
	}

	public void setCapetownID(int capetownID) {
		this.capetownID = capetownID;
	}

	public String getInvoiceNO() {
		return this.invoiceNO;
	}

	public void setInvoiceNO(String invoiceNO) {
		this.invoiceNO = invoiceNO;
	}

	public String getInvoiceAmount() {
		return this.invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public String getDisbursment() {
		return this.disbursment;
	}

	public void setDisbursment(String disbursment) {
		this.disbursment = disbursment;
	}

	public String getDiscount() {
		return this.discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}
}