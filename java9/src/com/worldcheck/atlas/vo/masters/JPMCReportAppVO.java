package com.worldcheck.atlas.vo.masters;

public class JPMCReportAppVO {
	private String case_reference_no;
	public String client_code;
	private String client_name;
	private String case_status;
	private String case_fee;
	private String request_received_date;
	private String final_due_date;
	private String client_final_sentdate;
	private String subject_type;
	private String subject_name;
	private String subject_country;
	private String other_details;
	private String research_element_original;

	private void setCase_reference_no(String caseReferenceNo) {
		this.case_reference_no = caseReferenceNo;
	}

	public void setClient_code(String clientCode) {
		this.client_code = clientCode;
	}

	private void setClient_name(String clientName) {
		this.client_name = clientName;
	}

	private void setCase_status(String caseStatus) {
		this.case_status = caseStatus;
	}

	private void setCase_fee(String caseFee) {
		this.case_fee = caseFee;
	}

	private void setRequest_received_date(String requestReceivedDate) {
		this.request_received_date = requestReceivedDate;
	}

	private void setFinal_due_date(String finalDueDate) {
		this.final_due_date = finalDueDate;
	}

	private void setClient_final_sentdate(String clientFinalSentdate) {
		this.client_final_sentdate = clientFinalSentdate;
	}

	private void setSubject_type(String subjectType) {
		this.subject_type = subjectType;
	}

	private void setSubject_name(String subjectName) {
		this.subject_name = subjectName;
	}

	private void setSubject_country(String subjectCountry) {
		this.subject_country = subjectCountry;
	}

	private void setOther_details(String otherDetails) {
		this.other_details = otherDetails;
	}

	private void setResearch_element_original(String researchElementOriginal) {
		this.research_element_original = researchElementOriginal;
	}

	public String getCase_reference_no() {
		return this.case_reference_no;
	}

	public String getClient_code() {
		return this.client_code;
	}

	public String getClient_name() {
		return this.client_name;
	}

	public String getCase_status() {
		return this.case_status;
	}

	public String getCase_fee() {
		return this.case_fee;
	}

	public String getRequest_received_date() {
		return this.request_received_date;
	}

	public String getFinal_due_date() {
		return this.final_due_date;
	}

	public String getClient_final_sentdate() {
		return this.client_final_sentdate;
	}

	public String getSubject_type() {
		return this.subject_type;
	}

	public String getSubject_name() {
		return this.subject_name;
	}

	public String getSubject_country() {
		return this.subject_country;
	}

	public String getOther_details() {
		return this.other_details;
	}

	public String getResearch_element_original() {
		return this.research_element_original;
	}
}