package com.worldcheck.atlas.scheduler.vo;

public class OverdueCasesSchedulerVo {
	private String crn;
	private String office;
	private String overdueMsg;
	private String caseManagerFullName;
	private String caseManagerLoginID;
	private String teamType;
	private String eligibleUsersForNotification;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getOverdueMsg() {
		return this.overdueMsg;
	}

	public void setOverdueMsg(String overdueMsg) {
		this.overdueMsg = overdueMsg;
	}

	public String getCaseManagerFullName() {
		return this.caseManagerFullName;
	}

	public void setCaseManagerFullName(String caseManagerFullName) {
		this.caseManagerFullName = caseManagerFullName;
	}

	public String getCaseManagerLoginID() {
		return this.caseManagerLoginID;
	}

	public void setCaseManagerLoginID(String caseManagerLoginID) {
		this.caseManagerLoginID = caseManagerLoginID;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getEligibleUsersForNotification() {
		return this.eligibleUsersForNotification;
	}

	public void setEligibleUsersForNotification(String eligibleUsersForNotification) {
		this.eligibleUsersForNotification = eligibleUsersForNotification;
	}
}