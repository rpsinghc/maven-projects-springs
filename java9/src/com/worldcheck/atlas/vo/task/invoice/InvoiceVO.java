package com.worldcheck.atlas.vo.task.invoice;

public class InvoiceVO {
	private String clientCode;
	private String invoiceTo;
	private String invoiceAddress;
	private String ctDate;
	private String invoiceCurrencyCode;
	private String invoiceDate;
	private String invoiceInstruction;
	private int capetownID;
	private String invoiceNO;
	private String crn;
	private String updateBy;
	private String pid;
	private String invoiceAmount;
	private String isisUser;
	private String discount;
	private String caseStatusId;

	public String getDiscount() {
		return this.discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getInvoiceInstruction() {
		return this.invoiceInstruction;
	}

	public void setInvoiceInstruction(String invoiceInstruction) {
		this.invoiceInstruction = invoiceInstruction;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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

	public String getIsisUser() {
		return this.isisUser;
	}

	public void setIsisUser(String isisUser) {
		this.isisUser = isisUser;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getInvoiceTo() {
		return this.invoiceTo;
	}

	public void setInvoiceTo(String invoiceTo) {
		this.invoiceTo = invoiceTo;
	}

	public String getInvoiceAddress() {
		return this.invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getInvoiceCurrencyCode() {
		return this.invoiceCurrencyCode;
	}

	public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
		this.invoiceCurrencyCode = invoiceCurrencyCode;
	}

	public String getCtDate() {
		return this.ctDate;
	}

	public void setCtDate(String ctDate) {
		this.ctDate = ctDate;
	}

	public String getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getCaseStatusId() {
		return this.caseStatusId;
	}

	public void setCaseStatusId(String caseStatusId) {
		this.caseStatusId = caseStatusId;
	}
}