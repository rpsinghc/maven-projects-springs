package com.worldcheck.atlas.vo.report;

public class OverDueRportSUBVO {
	private String countryName;
	private String interim1DueDate;
	private String interim2DueDate;
	private String finalDate;
	private String overDueBy;
	private String caseManager;
	private String office;

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getInterim1DueDate() {
		return this.interim1DueDate;
	}

	public void setInterim1DueDate(String interim1DueDate) {
		this.interim1DueDate = interim1DueDate;
	}

	public String getInterim2DueDate() {
		return this.interim2DueDate;
	}

	public void setInterim2DueDate(String interim2DueDate) {
		this.interim2DueDate = interim2DueDate;
	}

	public String getFinalDate() {
		return this.finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public String getOverDueBy() {
		return this.overDueBy;
	}

	public void setOverDueBy(String overDueBy) {
		this.overDueBy = overDueBy;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
}