package com.worldcheck.atlas.vo.report;

import java.util.Date;

public class OverDueCasesVO {
	private String CRN;
	private String primarySubject;
	private String country;
	private String caseManager;
	private String officeName;
	private int overDueBy;
	private Date CIDD1;
	private Date CIDD2;
	private Date CFDD;

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String crn) {
		this.CRN = crn;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public int getOverDueBy() {
		return this.overDueBy;
	}

	public void setOverDueBy(int overDueBy) {
		this.overDueBy = overDueBy;
	}

	public Date getCIDD1() {
		return this.CIDD1;
	}

	public void setCIDD1(Date cidd1) {
		this.CIDD1 = cidd1;
	}

	public Date getCIDD2() {
		return this.CIDD2;
	}

	public void setCIDD2(Date cidd2) {
		this.CIDD2 = cidd2;
	}

	public Date getCFDD() {
		return this.CFDD;
	}

	public void setCFDD(Date cfdd) {
		this.CFDD = cfdd;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
}