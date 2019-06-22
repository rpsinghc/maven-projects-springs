package com.worldcheck.atlas.vo.report;

import java.util.HashMap;

public class CurrentAnalystLoadingVO {
	private String supervisor;
	private String analyst;
	private long total;
	private HashMap monthDetails;
	private String day;
	private String crn;
	private String primarySubject;
	private String primarySubjectCountry;
	private String itrm1;
	private String itrm2;
	private String finalDueDate;
	private String leaveDate;
	private String leaveType;
	private String task;
	private String caseStatus;
	private String performer;

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getLeaveDate() {
		return this.leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public long getTotal() {
		return this.total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getItrm1() {
		return this.itrm1;
	}

	public void setItrm1(String itrm1) {
		this.itrm1 = itrm1;
	}

	public String getItrm2() {
		return this.itrm2;
	}

	public void setItrm2(String itrm2) {
		this.itrm2 = itrm2;
	}

	public String getFinalDueDate() {
		return this.finalDueDate;
	}

	public void setFinalDueDate(String finalDueDate) {
		this.finalDueDate = finalDueDate;
	}

	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getPrimarySubjectCountry() {
		return this.primarySubjectCountry;
	}

	public void setPrimarySubjectCountry(String primarySubjectCountry) {
		this.primarySubjectCountry = primarySubjectCountry;
	}

	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public long getTotalCases() {
		return this.total;
	}

	public void setTotalCases(long totalCases) {
		this.total = totalCases;
	}

	public HashMap getMonthDetails() {
		return this.monthDetails;
	}

	public void setMonthDetails(HashMap monthDetails) {
		this.monthDetails = monthDetails;
	}

	public String getLeaveType() {
		return this.leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}
}