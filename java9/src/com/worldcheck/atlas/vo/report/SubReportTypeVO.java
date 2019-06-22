package com.worldcheck.atlas.vo.report;

public class SubReportTypeVO {
	private String subReportId;
	private String subReportName;
	private String subCrnInitial;
	private String reportTypeId;
	private String associatedResearchElements;
	private String subReportTypeCode;
	private String SRStatus;
	private String SRLAction;

	public String getSRStatus() {
		return this.SRStatus;
	}

	public void setSRStatus(String sRStatus) {
		this.SRStatus = sRStatus;
	}

	public String getSRLAction() {
		return this.SRLAction;
	}

	public void setSRLAction(String sRLAction) {
		this.SRLAction = sRLAction;
	}

	public String getSubReportTypeCode() {
		return this.subReportTypeCode;
	}

	public void setSubReportTypeCode(String subReportTypeCode) {
		this.subReportTypeCode = subReportTypeCode;
	}

	public String getAssociatedResearchElements() {
		return this.associatedResearchElements;
	}

	public void setAssociatedResearchElements(String associatedResearchElements) {
		this.associatedResearchElements = associatedResearchElements;
	}

	public String getSubReportId() {
		return this.subReportId;
	}

	public void setSubReportId(String subReportId) {
		this.subReportId = subReportId;
	}

	public String getSubReportName() {
		return this.subReportName;
	}

	public void setSubReportName(String subReportName) {
		this.subReportName = subReportName;
	}

	public void setSubCrnInitial(String subCrnInitial) {
		this.subCrnInitial = subCrnInitial;
	}

	public String getSubCrnInitial() {
		return this.subCrnInitial;
	}

	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getReportTypeId() {
		return this.reportTypeId;
	}
}