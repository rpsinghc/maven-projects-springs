package com.worldcheck.atlas.vo.task.invoice;

public class CRNInfoVO {
	private String crn;
	private String primarySubjectName;
	private String client_code;
	private String currency_code;
	private String client_ref;
	private String country;
	private String caseCreationDate;
	private String caseStatus;
	private String case_info;
	private Long case_status_id;
	private String bulk_order_id;
	private Long pid;
	private String isis_user;
	private String req_recd_dt;
	private String searchedCRN;
	private String linkedCRN;
	private String caseFee;
	private String withCharges;
	private String caseFinalDueDate;

	public String getCaseFinalDueDate() {
		return this.caseFinalDueDate;
	}

	public void setCaseFinalDueDate(String caseFinalDueDate) {
		this.caseFinalDueDate = caseFinalDueDate;
	}

	public String getLinkedCRN() {
		return this.linkedCRN;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public String getWithCharges() {
		return this.withCharges;
	}

	public void setWithCharges(String withCharges) {
		this.withCharges = withCharges;
	}

	public void setLinkedCRN(String linkedCRN) {
		this.linkedCRN = linkedCRN;
	}

	public String getCurrency_code() {
		return this.currency_code;
	}

	public void setCurrency_code(String currencyCode) {
		this.currency_code = currencyCode;
	}

	public String getReq_recd_dt() {
		return this.req_recd_dt;
	}

	public void setReq_recd_dt(String reqRecdDt) {
		this.req_recd_dt = reqRecdDt;
	}

	public String getSearchedCRN() {
		return this.searchedCRN;
	}

	public void setSearchedCRN(String searchedCRN) {
		this.searchedCRN = searchedCRN;
	}

	public String getIsis_user() {
		return this.isis_user;
	}

	public void setIsis_user(String isisUser) {
		this.isis_user = isisUser;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getPrimarySubjectName() {
		return this.primarySubjectName;
	}

	public void setPrimarySubjectName(String primarySubjectName) {
		this.primarySubjectName = primarySubjectName;
	}

	public String getClient_code() {
		return this.client_code;
	}

	public void setClient_code(String clientCode) {
		this.client_code = clientCode;
	}

	public String getClient_ref() {
		return this.client_ref;
	}

	public void setClient_ref(String clientRef) {
		this.client_ref = clientRef;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCaseCreationDate() {
		return this.caseCreationDate;
	}

	public void setCaseCreationDate(String caseCreationDate) {
		this.caseCreationDate = caseCreationDate;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCase_info() {
		return this.case_info;
	}

	public void setCase_info(String caseInfo) {
		this.case_info = caseInfo;
	}

	public Long getCase_status_id() {
		return this.case_status_id;
	}

	public void setCase_status_id(Long caseStatusId) {
		this.case_status_id = caseStatusId;
	}

	public String getBulk_order_id() {
		return this.bulk_order_id;
	}

	public void setBulk_order_id(String bulkOrderId) {
		this.bulk_order_id = bulkOrderId;
	}

	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
}