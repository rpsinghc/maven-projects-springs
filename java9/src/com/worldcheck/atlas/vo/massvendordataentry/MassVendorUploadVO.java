package com.worldcheck.atlas.vo.massvendordataentry;

import java.util.Date;

public class MassVendorUploadVO {
	private Date commissioningDate;
	private String CRN;
	private String country;
	private String vendorName;
	private String nameSearched;
	private String currency;
	private double amount;
	private String invoiceNumber;
	private String vendorManagerMessage;
	private String pid;
	private String[] records;

	public Date getCommissioningDate() {
		return this.commissioningDate;
	}

	public void setCommissioningDate(Date commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
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

	public String getNameSearched() {
		return this.nameSearched;
	}

	public void setNameSearched(String nameSearched) {
		this.nameSearched = nameSearched;
	}

	public String getVurrency() {
		return this.currency;
	}

	public void setVurrency(String vurrency) {
		this.currency = vurrency;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getVendorManagerMessage() {
		return this.vendorManagerMessage;
	}

	public void setVendorManagerMessage(String vendorManagerMessage) {
		this.vendorManagerMessage = vendorManagerMessage;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String[] getRecords() {
		return this.records;
	}

	public void setRecords(String[] records) {
		this.records = records;
	}
}