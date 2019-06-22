package com.worldcheck.atlas.bl.search;

import java.util.List;

public class SearchCriteria {
	private String crnNo;
	private List<String> crnNumbers;
	private String startCaseRecvDate;
	private String endCaseRecvDate;
	private String startClientDueDate;
	private String endClientDueDate;
	private String includeIntrimDueDate;
	private String subjectName;
	private String subjectCountry;
	private String subjectOtherdetails;
	private String clientRefNo;
	private String caseCreator;
	private String caseManager;
	private String caseStatus;
	private String isisBulkOrder;
	private String sortType;
	private String legacyData;
	private String exactMatch;
	private String searchType;
	private String biAnalyst;
	private String analyst;
	private String entityTypeId;
	private String vendorManager;
	private String office;
	private String reviewer;
	private String showOnlyTeamLevelResults;
	private String messageText;
	private String acknowledge;
	private String messageType;
	private String userName;
	private String caseType;
	private String countryId;
	private String keyWord;
	private List<Integer> country;
	private String clientCode;
	private String reportCode;
	private String recurrPattern;
	private int minRecord;
	private String sysUserName;
	private String loginUser;
	private String sender;
	private String recipient;
	private int maxRecord;
	private String sortColumnName;

	public int getMinRecord() {
		return this.minRecord;
	}

	public void setMinRecord(int minRecord) {
		this.minRecord = minRecord;
	}

	public int getMaxRecord() {
		return this.maxRecord;
	}

	public void setMaxRecord(int maxRecord) {
		this.maxRecord = maxRecord;
	}

	public String getStartClientDueDate() {
		return this.startClientDueDate;
	}

	public void setStartClientDueDate(String startClientDueDate) {
		this.startClientDueDate = startClientDueDate;
	}

	public String getEndClientDueDate() {
		return this.endClientDueDate;
	}

	public void setEndClientDueDate(String endClientDueDate) {
		this.endClientDueDate = endClientDueDate;
	}

	public String getIncludeIntrimDueDate() {
		return this.includeIntrimDueDate;
	}

	public void setIncludeIntrimDueDate(String includeIntrimDueDate) {
		this.includeIntrimDueDate = includeIntrimDueDate;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectCountry() {
		return this.subjectCountry;
	}

	public void setSubjectCountry(String subjectCountry) {
		this.subjectCountry = subjectCountry;
	}

	public String getSubjectOtherdetails() {
		return this.subjectOtherdetails;
	}

	public void setSubjectOtherdetails(String subjectOtherdetails) {
		this.subjectOtherdetails = subjectOtherdetails;
	}

	public String getClientRefNo() {
		return this.clientRefNo;
	}

	public void setClientRefNo(String clientRefNo) {
		this.clientRefNo = clientRefNo;
	}

	public String getCaseCreator() {
		return this.caseCreator;
	}

	public void setCaseCreator(String caseCreator) {
		this.caseCreator = caseCreator;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getIsisBulkOrder() {
		return this.isisBulkOrder;
	}

	public void setIsisBulkOrder(String isiBulkOrder) {
		this.isisBulkOrder = isiBulkOrder;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getLegacyData() {
		return this.legacyData;
	}

	public void setLegacyData(String legacyData) {
		this.legacyData = legacyData;
	}

	public String getSearchType() {
		return this.searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getBiAnalyst() {
		return this.biAnalyst;
	}

	public void setBiAnalyst(String biAnalyst) {
		this.biAnalyst = biAnalyst;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getEntityTypeId() {
		return this.entityTypeId;
	}

	public void setEntityTypeId(String entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public String getVendorManager() {
		return this.vendorManager;
	}

	public void setVendorManager(String vendorManager) {
		this.vendorManager = vendorManager;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String isShowOnlyTeamLevelResults() {
		return this.showOnlyTeamLevelResults;
	}

	public void setShowOnlyTeamLevelResults(String showOnlyTeamLevelResults) {
		this.showOnlyTeamLevelResults = showOnlyTeamLevelResults;
	}

	public String getShowOnlyTeamLevelResults() {
		return this.showOnlyTeamLevelResults;
	}

	public String getMessageText() {
		return this.messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getAcknowledge() {
		return this.acknowledge;
	}

	public void setAcknowledge(String acknowledge) {
		this.acknowledge = acknowledge;
	}

	public String getMessageType() {
		return this.messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCaseType() {
		return this.caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getCountryId() {
		return this.countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List getCountry() {
		return this.country;
	}

	public void setCountry(List country) {
		this.country = country;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getCrnNo() {
		return this.crnNo;
	}

	public void setCrnNo(String crnNo) {
		this.crnNo = crnNo;
	}

	public List<String> getCrnNumbers() {
		return this.crnNumbers;
	}

	public void setCrnNumbers(List<String> crnNumbers) {
		this.crnNumbers = crnNumbers;
	}

	public String getStartCaseRecvDate() {
		return this.startCaseRecvDate;
	}

	public void setStartCaseRecvDate(String startCaseRecvDate) {
		this.startCaseRecvDate = startCaseRecvDate;
	}

	public String getEndCaseRecvDate() {
		return this.endCaseRecvDate;
	}

	public void setEndCaseRecvDate(String endCaseRecvDate) {
		this.endCaseRecvDate = endCaseRecvDate;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getRecurrPattern() {
		return this.recurrPattern;
	}

	public void setRecurrPattern(String recurrPattern) {
		this.recurrPattern = recurrPattern;
	}

	public String getSysUserName() {
		return this.sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getLoginUser() {
		return this.loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setExactMatch(String exactMatch) {
		this.exactMatch = exactMatch;
	}

	public String getExactMatch() {
		return this.exactMatch;
	}
}