package com.worldcheck.atlas.vo.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;

public class CaseSearchVO extends SearchCriteria {
	private String reportType;
	private String reqRecdDate;
	private String finalDueDate;
	private String clientDueDate;
	private String cInterim1;
	private String cInterim2;
	private String finalRDueDate;
	private String rInterim1;
	private String rInterim2;
	private String primarySub;
	private String priCountry;
	private String otherSubject;
	private String primaryTeamOffice;
	private String isLegacyData;
	private String isExactMatch;
	private String recurrNumber;

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReqRecdDate() {
		return this.reqRecdDate;
	}

	public void setReqRecdDate(String reqRecdDate) {
		this.reqRecdDate = reqRecdDate;
	}

	public String getFinalDueDate() {
		return this.finalDueDate;
	}

	public void setFinalDueDate(String finalDueDate) {
		this.finalDueDate = finalDueDate;
	}

	public String getClientDueDate() {
		return this.clientDueDate;
	}

	public void setClientDueDate(String clientDueDate) {
		this.clientDueDate = clientDueDate;
	}

	public String getcInterim1() {
		return this.cInterim1;
	}

	public void setcInterim1(String cInterim1) {
		this.cInterim1 = cInterim1;
	}

	public String getcInterim2() {
		return this.cInterim2;
	}

	public void setcInterim2(String cInterim2) {
		this.cInterim2 = cInterim2;
	}

	public String getFinalRDueDate() {
		return this.finalRDueDate;
	}

	public void setFinalRDueDate(String finalRDueDate) {
		this.finalRDueDate = finalRDueDate;
	}

	public String getrInterim1() {
		return this.rInterim1;
	}

	public void setrInterim1(String rInterim1) {
		this.rInterim1 = rInterim1;
	}

	public String getrInterim2() {
		return this.rInterim2;
	}

	public void setrInterim2(String rInterim2) {
		this.rInterim2 = rInterim2;
	}

	public String getPrimarySub() {
		return this.primarySub;
	}

	public void setPrimarySub(String primarySub) {
		this.primarySub = primarySub;
	}

	public String getPriCountry() {
		return this.priCountry;
	}

	public void setPriCountry(String priCountry) {
		this.priCountry = priCountry;
	}

	public String getOtherSubject() {
		return this.otherSubject;
	}

	public void setOtherSubject(String otherSubject) {
		this.otherSubject = otherSubject;
	}

	public String getPrimaryTeamOffice() {
		return this.primaryTeamOffice;
	}

	public void setPrimaryTeamOffice(String primaryTeamOffice) {
		this.primaryTeamOffice = primaryTeamOffice;
	}

	public String getIsLegacyData() {
		return this.isLegacyData;
	}

	public void setIsLegacyData(String isLegacyData) {
		this.isLegacyData = isLegacyData;
	}

	public String getRecurrNumber() {
		return this.recurrNumber;
	}

	public void setRecurrNumber(String recurrNumber) {
		this.recurrNumber = recurrNumber;
	}

	public void setIsExactMatch(String isExactMatch) {
		this.isExactMatch = isExactMatch;
	}

	public String getIsExactMatch() {
		return this.isExactMatch;
	}
}