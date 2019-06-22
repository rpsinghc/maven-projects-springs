package com.worldcheck.atlas.vo.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CaseRawExcelVO {
	private String CRN;
	private String clientCode;
	private String clientName;
	private String clientRefNo;
	private boolean isExpress;
	private String caseDetails;
	private String reportType;
	private String casePriority;
	private Date caseReceivedDate;
	private String clientFeedback;
	private String caseManager;
	private String currency;
	private String caseStatus;
	private float caseFee;
	private float caseFeeInUSD;
	private float disbrusement;
	private String invoiceTo;
	private int invoiceNumber;
	private Date invoiceSentDate;
	private Date IDD1;
	private Date IDD2;
	private Date FDD;
	private Date ISD1;
	private Date ISD2;
	private Date FSD;
	private String primarySubject;
	private String primarySubCountry;
	private String subjects;
	private String subjectCountries;
	private String ISISorSavvion;
	private String ISISUserID;
	private List<TeamDetailsVO> teamDetails = new ArrayList();

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String crn) {
		this.CRN = crn;
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

	public String getClientRefNo() {
		return this.clientRefNo;
	}

	public void setClientRefNo(String clientRefNo) {
		this.clientRefNo = clientRefNo;
	}

	public boolean isExpress() {
		return this.isExpress;
	}

	public void setExpress(boolean isExpress) {
		this.isExpress = isExpress;
	}

	public String getCaseDetails() {
		return this.caseDetails;
	}

	public void setCaseDetails(String caseDetails) {
		this.caseDetails = caseDetails;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getCasePriority() {
		return this.casePriority;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public Date getCaseReceivedDate() {
		return this.caseReceivedDate;
	}

	public void setCaseReceivedDate(Date caseReceivedDate) {
		this.caseReceivedDate = caseReceivedDate;
	}

	public String getClientFeedback() {
		return this.clientFeedback;
	}

	public void setClientFeedback(String clientFeedback) {
		this.clientFeedback = clientFeedback;
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

	public float getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(float caseFee) {
		this.caseFee = caseFee;
	}

	public float getCaseFeeInUSD() {
		return this.caseFeeInUSD;
	}

	public void setCaseFeeInUSD(float caseFeeInUSD) {
		this.caseFeeInUSD = caseFeeInUSD;
	}

	public float getDisbrusement() {
		return this.disbrusement;
	}

	public void setDisbrusement(float disbrusement) {
		this.disbrusement = disbrusement;
	}

	public String getInvoiceTo() {
		return this.invoiceTo;
	}

	public void setInvoiceTo(String invoiceTo) {
		this.invoiceTo = invoiceTo;
	}

	public int getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceSentDate() {
		return this.invoiceSentDate;
	}

	public void setInvoiceSentDate(Date invoiceSentDate) {
		this.invoiceSentDate = invoiceSentDate;
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

	public Date getISD1() {
		return this.ISD1;
	}

	public void setISD1(Date isd1) {
		this.ISD1 = isd1;
	}

	public Date getISD2() {
		return this.ISD2;
	}

	public void setISD2(Date isd2) {
		this.ISD2 = isd2;
	}

	public Date getFSD() {
		return this.FSD;
	}

	public void setFSD(Date fsd) {
		this.FSD = fsd;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getPrimarySubCountry() {
		return this.primarySubCountry;
	}

	public void setPrimarySubCountry(String primarySubCountry) {
		this.primarySubCountry = primarySubCountry;
	}

	public String getSubjects() {
		return this.subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getSubjectCountries() {
		return this.subjectCountries;
	}

	public void setSubjectCountries(String subjectCountries) {
		this.subjectCountries = subjectCountries;
	}

	public String getISISorSavvion() {
		return this.ISISorSavvion;
	}

	public void setISISorSavvion(String sorSavvion) {
		this.ISISorSavvion = sorSavvion;
	}

	public String getISISUserID() {
		return this.ISISUserID;
	}

	public void setISISUserID(String userID) {
		this.ISISUserID = userID;
	}

	public List<TeamDetailsVO> getTeamDetails() {
		return this.teamDetails;
	}

	public void setTeamDetails(List<TeamDetailsVO> teamDetails) {
		this.teamDetails = teamDetails;
	}
}