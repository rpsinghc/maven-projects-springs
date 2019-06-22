package com.worldcheck.atlas.vo.report;

public class CaseRawDataFinanceVO {
	private String CRN;
	private String clientCode;
	private String clientName;
	private String reportType;
	private String caseManager;
	private String currency;
	private String caseStatus;
	private String caseFee;
	private String caseFee_usd;
	private String salesmonth;
	private String parentCRN;
	private String subReportType_Parent_crn;
	private String case_fee_usd_plan_fx;
	private String caseReceivedDate;
	private String clientFinalSentDate;
	private String cancelledCharges;
	private String primarySubject;
	private String primarySubjectCountry;
	private String otherSubjects;
	private String otherSubjectsCountries;
	private String mainClientGroup;
	private String final_dd_Client;
	private String acheived_tat;
	private String target_tat;
	private String delivery;
	private String case_received_date;
	private String primarySubjectType;
	private String clientReferenceNumber;
	private String salesRepresentativeRegion;

	public String getCRN() {
		return this.CRN;
	}

	public String getClientReferenceNumber() {
		return this.clientReferenceNumber;
	}

	public void setClientReferenceNumber(String clientReferenceNumber) {
		this.clientReferenceNumber = clientReferenceNumber;
	}

	public String getSalesRepresentativeRegion() {
		return this.salesRepresentativeRegion;
	}

	public void setSalesRepresentativeRegion(String salesRepresentativeRegion) {
		this.salesRepresentativeRegion = salesRepresentativeRegion;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public String getCaseFee_usd() {
		return this.caseFee_usd;
	}

	public void setCaseFee_usd(String caseFeeUsd) {
		this.caseFee_usd = caseFeeUsd;
	}

	public String getSalesmonth() {
		return this.salesmonth;
	}

	public void setSalesmonth(String salesmonth) {
		this.salesmonth = salesmonth;
	}

	public String getParentCRN() {
		return this.parentCRN;
	}

	public void setParentCRN(String parentCRN) {
		this.parentCRN = parentCRN;
	}

	public String getSubReportType_Parent_crn() {
		return this.subReportType_Parent_crn;
	}

	public void setSubReportType_Parent_crn(String subReportTypeParentCrn) {
		this.subReportType_Parent_crn = subReportTypeParentCrn;
	}

	public String getCase_fee_usd_plan_fx() {
		return this.case_fee_usd_plan_fx;
	}

	public void setCase_fee_usd_plan_fx(String caseFeeUsdPlanFx) {
		this.case_fee_usd_plan_fx = caseFeeUsdPlanFx;
	}

	public String getCaseReceivedDate() {
		return this.caseReceivedDate;
	}

	public void setCaseReceivedDate(String caseReceivedDate) {
		this.caseReceivedDate = caseReceivedDate;
	}

	public String getClientFinalSentDate() {
		return this.clientFinalSentDate;
	}

	public void setClientFinalSentDate(String clientFinalSentDate) {
		this.clientFinalSentDate = clientFinalSentDate;
	}

	public String getCancelledCharges() {
		return this.cancelledCharges;
	}

	public void setCancelledCharges(String cancelledCharges) {
		this.cancelledCharges = cancelledCharges;
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

	public String getOtherSubjects() {
		return this.otherSubjects;
	}

	public void setOtherSubjects(String otherSubjects) {
		this.otherSubjects = otherSubjects;
	}

	public String getOtherSubjectsCountries() {
		return this.otherSubjectsCountries;
	}

	public void setOtherSubjectsCountries(String otherSubjectsCountries) {
		this.otherSubjectsCountries = otherSubjectsCountries;
	}

	public String getMainClientGroup() {
		return this.mainClientGroup;
	}

	public void setMainClientGroup(String mainClientGroup) {
		this.mainClientGroup = mainClientGroup;
	}

	public String getFinal_dd_Client() {
		return this.final_dd_Client;
	}

	public void setFinal_dd_Client(String finalDdClient) {
		this.final_dd_Client = finalDdClient;
	}

	public String getAcheived_tat() {
		return this.acheived_tat;
	}

	public void setAcheived_tat(String acheivedTat) {
		this.acheived_tat = acheivedTat;
	}

	public String getTarget_tat() {
		return this.target_tat;
	}

	public void setTarget_tat(String targetTat) {
		this.target_tat = targetTat;
	}

	public String getDelivery() {
		return this.delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public void setCase_received_date(String case_received_date) {
		this.case_received_date = case_received_date;
	}

	public String getCase_received_date() {
		return this.case_received_date;
	}

	public void setPrimarySubjectType(String primarySubjectType) {
		this.primarySubjectType = primarySubjectType;
	}

	public String getPrimarySubjectType() {
		return this.primarySubjectType;
	}
}