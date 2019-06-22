package com.worldcheck.atlas.vo.report;

import java.util.Date;

public class TeamDetailsVO {
	private String CRN;
	private String teamId;
	private String teamType;
	private String teammOffice;
	private String analyst;
	private String analystSupervisor;
	private float JLP;
	private Date assignDate;
	private Date IDD1;
	private Date IDD2;
	private Date FDD;
	private Date ICD1;
	private Date ICD2;
	private Date FCD;
	private String reviewer1;
	private String reviewer2;
	private String reviewer3;
	private boolean isPTRERejected;
	private int teamTypeID;

	public int getTeamTypeID() {
		return this.teamTypeID;
	}

	public void setTeamTypeID(int teamTypeID) {
		this.teamTypeID = teamTypeID;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String crn) {
		this.CRN = crn;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamId() {
		return this.teamId;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getTeammOffice() {
		return this.teammOffice;
	}

	public void setTeammOffice(String teammOffice) {
		this.teammOffice = teammOffice;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getAnalystSupervisor() {
		return this.analystSupervisor;
	}

	public void setAnalystSupervisor(String analystSupervisor) {
		this.analystSupervisor = analystSupervisor;
	}

	public float getJLP() {
		return this.JLP;
	}

	public void setJLP(float jlp) {
		this.JLP = jlp;
	}

	public Date getAssignDate() {
		return this.assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}

	public Date getIDD1() {
		return this.IDD1;
	}

	public void setIDD1(Date idd1) {
		this.IDD1 = idd1;
	}

	public Date getIDD2() {
		return this.IDD2;
	}

	public void setIDD2(Date idd2) {
		this.IDD2 = idd2;
	}

	public Date getFDD() {
		return this.FDD;
	}

	public void setFDD(Date fdd) {
		this.FDD = fdd;
	}

	public Date getICD1() {
		return this.ICD1;
	}

	public void setICD1(Date icd1) {
		this.ICD1 = icd1;
	}

	public Date getICD2() {
		return this.ICD2;
	}

	public void setICD2(Date icd2) {
		this.ICD2 = icd2;
	}

	public Date getFCD() {
		return this.FCD;
	}

	public void setFCD(Date fcd) {
		this.FCD = fcd;
	}

	public String getReviewer1() {
		return this.reviewer1;
	}

	public void setReviewer1(String reviewer1) {
		this.reviewer1 = reviewer1;
	}

	public String getReviewer2() {
		return this.reviewer2;
	}

	public void setReviewer2(String reviewer2) {
		this.reviewer2 = reviewer2;
	}

	public String getReviewer3() {
		return this.reviewer3;
	}

	public void setReviewer3(String reviewer3) {
		this.reviewer3 = reviewer3;
	}

	public boolean isPTRERejected() {
		return this.isPTRERejected;
	}

	public void setPTRERejected(boolean isPTRERejected) {
		this.isPTRERejected = isPTRERejected;
	}
}