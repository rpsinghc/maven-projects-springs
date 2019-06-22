package com.worldcheck.atlas.vo.report;

import java.util.List;

public class InvoiceMISVO {
	private String crn;
	private List<String> clientCode;
	private List<String> caseStatus;
	private String clientReferenceNo;
	private String subjectName;
	private String subjectCountry;
	private String entityType;
	private String reportLevel;
	private String subjectFee;
	private String caseFee;
	private String caseCreationDateFrom;
	private String caseCreationDateTo;
	private String finalaReportSentdateFrom;
	private String finalaReportSentdateTo;
	private String caseCreationDate;
	private String finalaReportSentdate;
	private String registerNumber;
	private String registerDate;

	public String getCaseCreationDate() {
		return this.caseCreationDate;
	}

	public void setCaseCreationDate(String caseCreationDate) {
		this.caseCreationDate = caseCreationDate;
	}

	public String getFinalaReportSentdate() {
		return this.finalaReportSentdate;
	}

	public void setFinalaReportSentdate(String finalaReportSentdate) {
		this.finalaReportSentdate = finalaReportSentdate;
	}

	public List<String> getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(List<String> clientCode) {
		this.clientCode = clientCode;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getClientReferenceNo() {
		return this.clientReferenceNo;
	}

	public List<String> getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(List<String> caseStatus) {
		this.caseStatus = caseStatus;
	}

	public void setClientReferenceNo(String clientReferenceNo) {
		this.clientReferenceNo = clientReferenceNo;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectCountry() {
		return this.subjectCountry;
	}

	public void setSubjectCountry(String subjectCountry) {
		this.subjectCountry = subjectCountry;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getReportLevel() {
		return this.reportLevel;
	}

	public void setReportLevel(String reportLevel) {
		this.reportLevel = reportLevel;
	}

	public String getSubjectFee() {
		return this.subjectFee;
	}

	public void setSubjectFee(String subjectFee) {
		this.subjectFee = subjectFee;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public String getCaseCreationDateFrom() {
		return this.caseCreationDateFrom;
	}

	public void setCaseCreationDateFrom(String caseCreationDateFrom) {
		this.caseCreationDateFrom = caseCreationDateFrom;
	}

	public String getCaseCreationDateTo() {
		return this.caseCreationDateTo;
	}

	public void setCaseCreationDateTo(String caseCreationDateTo) {
		this.caseCreationDateTo = caseCreationDateTo;
	}

	public String getFinalaReportSentdateFrom() {
		return this.finalaReportSentdateFrom;
	}

	public void setFinalaReportSentdateFrom(String finalaReportSentdateFrom) {
		this.finalaReportSentdateFrom = finalaReportSentdateFrom;
	}

	public String getFinalaReportSentdateTo() {
		return this.finalaReportSentdateTo;
	}

	public void setFinalaReportSentdateTo(String finalaReportSentdateTo) {
		this.finalaReportSentdateTo = finalaReportSentdateTo;
	}

	public String getRegisterNumber() {
		return this.registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public String getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
}