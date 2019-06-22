package com.worldcheck.atlas.vo;

import java.util.List;

public class EmailTemplateVO {
	String crn;
	String caseManager;
	String interim1CDD;
	String interim2CDD;
	String finalCDD;
	String clientReferenceNumber;
	String emailBody;
	String emailTO;
	String emailCC;
	String emailBCC;
	String defaultMailingList;
	List<SubjectDetails> subjectDetailsList;
	String finalEmailBody;
	String isEmailSent;
	String clientCode;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getInterim1CDD() {
		return this.interim1CDD;
	}

	public void setInterim1CDD(String interim1cdd) {
		this.interim1CDD = interim1cdd;
	}

	public String getInterim2CDD() {
		return this.interim2CDD;
	}

	public void setInterim2CDD(String interim2cdd) {
		this.interim2CDD = interim2cdd;
	}

	public String getFinalCDD() {
		return this.finalCDD;
	}

	public void setFinalCDD(String finalCDD) {
		this.finalCDD = finalCDD;
	}

	public String getClientReferenceNumber() {
		return this.clientReferenceNumber;
	}

	public void setClientReferenceNumber(String clientReferenceNumber) {
		this.clientReferenceNumber = clientReferenceNumber;
	}

	public String getEmailBody() {
		return this.emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailTO() {
		return this.emailTO;
	}

	public void setEmailTO(String emailTO) {
		this.emailTO = emailTO;
	}

	public String getEmailCC() {
		return this.emailCC;
	}

	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}

	public String getEmailBCC() {
		return this.emailBCC;
	}

	public void setEmailBCC(String emailBCC) {
		this.emailBCC = emailBCC;
	}

	public String getDefaultMailingList() {
		return this.defaultMailingList;
	}

	public void setDefaultMailingList(String defaultMailingList) {
		this.defaultMailingList = defaultMailingList;
	}

	public List<SubjectDetails> getSubjectDetailsList() {
		return this.subjectDetailsList;
	}

	public void setSubjectDetailsList(List<SubjectDetails> subjectDetailsList) {
		this.subjectDetailsList = subjectDetailsList;
	}

	public String getFinalEmailBody() {
		return this.finalEmailBody;
	}

	public void setFinalEmailBody(String finalEmailBody) {
		this.finalEmailBody = finalEmailBody;
	}

	public String getIsEmailSent() {
		return this.isEmailSent;
	}

	public void setIsEmailSent(String isEmailSent) {
		this.isEmailSent = isEmailSent;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
}