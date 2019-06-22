package com.worldcheck.atlas.vo;

public class UpdateCTDataExcelVO {
	private String crn;
	private String ctDate;
	private String invCurrency;
	private float invNumber;
	private float invAmount;
	private String invDate;
	private String status;
	private String updatedBy;

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

	public String getInvCurrency() {
		return this.invCurrency;
	}

	public void setInvCurrency(String invCurrency) {
		this.invCurrency = invCurrency;
	}

	public float getInvNumber() {
		return this.invNumber;
	}

	public void setInvNumber(float invNumber) {
		this.invNumber = invNumber;
	}

	public float getInvAmount() {
		return this.invAmount;
	}

	public void setInvAmount(float invAmount) {
		this.invAmount = invAmount;
	}

	public String getInvDate() {
		return this.invDate;
	}

	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}