package com.worldcheck.atlas.vo.task;

import java.sql.Date;

public class RecurrentCaseVO {
	private long recurrenceId;
	private String clientName;
	private String CRN;
	private String reportType;
	private String subReportType;
	private String subjectId;
	private String caseManager;
	private int isExpress;
	private String casePriority;
	private String recurrenceRange;
	private String recurrencePattern;
	private int cFinal;
	private int cInterim1;
	private int cInterim2;
	private double caseFee;
	private String currency;
	private String caseCreator;
	private String updatedBy;
	private Date updatedOn;
	private Date nextRun;
	private String branchOffice;
	private long lastRecurrenceNum;
	private Date endDate;
	private String clientReference;
	private Date lastRun;
	private int noOfRecurrences;

	public Date getLastRun() {
		return this.lastRun;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	public long getRecurrenceId() {
		return this.recurrenceId;
	}

	public void setRecurrenceId(long recurrenceId) {
		this.recurrenceId = recurrenceId;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public int getIsExpress() {
		return this.isExpress;
	}

	public void setIsExpress(int isExpress) {
		this.isExpress = isExpress;
	}

	public String getCasePriority() {
		return this.casePriority;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public String getRecurrenceRange() {
		return this.recurrenceRange;
	}

	public void setRecurrenceRange(String recurrenceRange) {
		this.recurrenceRange = recurrenceRange;
	}

	public String getRecurrencePattern() {
		return this.recurrencePattern;
	}

	public void setRecurrencePattern(String recurrencePattern) {
		this.recurrencePattern = recurrencePattern;
	}

	public int getcFinal() {
		return this.cFinal;
	}

	public void setcFinal(int cFinal) {
		this.cFinal = cFinal;
	}

	public int getcInterim1() {
		return this.cInterim1;
	}

	public void setcInterim1(int cInterim1) {
		this.cInterim1 = cInterim1;
	}

	public int getcInterim2() {
		return this.cInterim2;
	}

	public void setcInterim2(int cInterim2) {
		this.cInterim2 = cInterim2;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCaseCreator() {
		return this.caseCreator;
	}

	public void setCaseCreator(String caseCreator) {
		this.caseCreator = caseCreator;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getNextRun() {
		return this.nextRun;
	}

	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}

	public String getBranchOffice() {
		return this.branchOffice;
	}

	public void setBranchOffice(String branchOffice) {
		this.branchOffice = branchOffice;
	}

	public long getLastRecurrenceNum() {
		return this.lastRecurrenceNum;
	}

	public void setLastRecurrenceNum(long lastRecurrenceNum) {
		this.lastRecurrenceNum = lastRecurrenceNum;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getClientReference() {
		return this.clientReference;
	}

	public void setClientReference(String clientReference) {
		this.clientReference = clientReference;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public double getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(double caseFee) {
		this.caseFee = caseFee;
	}

	public int getNoOfRecurrences() {
		return this.noOfRecurrences;
	}

	public void setNoOfRecurrences(int noOfRecurrences) {
		this.noOfRecurrences = noOfRecurrences;
	}
}