package com.worldcheck.atlas.vo.report;

public class SSReviewerEffortVO {
	private String reviewer;
	private int numberOfReports;
	private String office;

	public String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public int getNumberOfReports() {
		return this.numberOfReports;
	}

	public void setNumberOfReports(int numberOfReports) {
		this.numberOfReports = numberOfReports;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
}