package com.worldcheck.atlas.vo.masters;

import java.util.List;

public class ClientFeedBackVO {
	private String attachedFiles;
	private List<String> attachedFilesList;
	private String logInUserId;
	private String feedBackCategoryID;
	private String feedBackCategoryName;
	private String crn;
	private String caseReceivedDate;
	private String primarySubject;
	private String clientCode;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private long newFbSeqId;
	private String updatedBy;
	private String dateClosed;
	private List<String> linkUnlinkCrnList;
	private String sessionID;
	private String feedback_type_id;
	private String feedback_type_value;
	private String isAttach;
	private String fileName;
	private String fileSize;
	private String filePath;
	private long fileId;
	private String fileURL;
	private String actionTakenField;
	private String fbDescriptionField;
	private String clientContactField;
	private String fbStatusField;
	private String casOwnerField;
	private String fbCategoryField;
	private String clientNameField;
	private String linkedCrnList;
	private long crnMapID;
	private String crnName;
	private String crnLinkedBy;
	private String dateLoggedStart;
	private String dateLoggedEnd;
	private String searchClientCode;
	private String searchCaseOwner;
	private String searchCaseStatus;
	private String searchClientName;
	private String searchCRN;
	private String searchfbDescription;
	private String searchDateLogged;
	private List<Integer> statusList;
	private List<String> clientCodeList;
	private List<String> caseOwnerList;
	private long searchFBID;
	private String isLegacyData;

	public List<String> getAttachedFilesList() {
		return this.attachedFilesList;
	}

	public void setAttachedFilesList(List<String> attachedFilesList) {
		this.attachedFilesList = attachedFilesList;
	}

	public String getAttachedFiles() {
		return this.attachedFiles;
	}

	public void setAttachedFiles(String attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	public String getFeedback_type_id() {
		return this.feedback_type_id;
	}

	public void setFeedback_type_id(String feedbackTypeId) {
		this.feedback_type_id = feedbackTypeId;
	}

	public String getFeedback_type_value() {
		return this.feedback_type_value;
	}

	public void setFeedback_type_value(String feedbackTypeValue) {
		this.feedback_type_value = feedbackTypeValue;
	}

	public String getSessionID() {
		return this.sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getIsLegacyData() {
		return this.isLegacyData;
	}

	public void setIsLegacyData(String isLegacyData) {
		this.isLegacyData = isLegacyData;
	}

	public String getIsAttach() {
		return this.isAttach;
	}

	public void setIsAttach(String isAttach) {
		this.isAttach = isAttach;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public String getFileURL() {
		return this.fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public long getFileId() {
		return this.fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<String> getLinkUnlinkCrnList() {
		return this.linkUnlinkCrnList;
	}

	public void setLinkUnlinkCrnList(List<String> linkUnlinkCrnList) {
		this.linkUnlinkCrnList = linkUnlinkCrnList;
	}

	public long getSearchFBID() {
		return this.searchFBID;
	}

	public void setSearchFBID(long searchFBID) {
		this.searchFBID = searchFBID;
	}

	public List<String> getClientCodeList() {
		return this.clientCodeList;
	}

	public void setClientCodeList(List<String> clientCodeList) {
		this.clientCodeList = clientCodeList;
	}

	public List<String> getCaseOwnerList() {
		return this.caseOwnerList;
	}

	public void setCaseOwnerList(List<String> caseOwnerList) {
		this.caseOwnerList = caseOwnerList;
	}

	public List<Integer> getStatusList() {
		return this.statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

	public String getSearchDateLogged() {
		return this.searchDateLogged;
	}

	public void setSearchDateLogged(String searchDateLogged) {
		this.searchDateLogged = searchDateLogged;
	}

	public String getSearchClientName() {
		return this.searchClientName;
	}

	public void setSearchClientName(String searchClientName) {
		this.searchClientName = searchClientName;
	}

	public String getSearchCRN() {
		return this.searchCRN;
	}

	public void setSearchCRN(String searchCRN) {
		this.searchCRN = searchCRN;
	}

	public String getSearchfbDescription() {
		return this.searchfbDescription;
	}

	public void setSearchfbDescription(String searchfbDescription) {
		this.searchfbDescription = searchfbDescription;
	}

	public String getDateLoggedStart() {
		return this.dateLoggedStart;
	}

	public void setDateLoggedStart(String dateLoggedStart) {
		this.dateLoggedStart = dateLoggedStart;
	}

	public String getDateLoggedEnd() {
		return this.dateLoggedEnd;
	}

	public void setDateLoggedEnd(String dateLoggedEnd) {
		this.dateLoggedEnd = dateLoggedEnd;
	}

	public String getSearchClientCode() {
		return this.searchClientCode;
	}

	public void setSearchClientCode(String searchClientCode) {
		this.searchClientCode = searchClientCode;
	}

	public String getSearchCaseOwner() {
		return this.searchCaseOwner;
	}

	public void setSearchCaseOwner(String searchCaseOwner) {
		this.searchCaseOwner = searchCaseOwner;
	}

	public String getSearchCaseStatus() {
		return this.searchCaseStatus;
	}

	public void setSearchCaseStatus(String searchCaseStatus) {
		this.searchCaseStatus = searchCaseStatus;
	}

	public long getCrnMapID() {
		return this.crnMapID;
	}

	public void setCrnMapID(long crnMapID) {
		this.crnMapID = crnMapID;
	}

	public String getCrnName() {
		return this.crnName;
	}

	public void setCrnName(String crnName) {
		this.crnName = crnName;
	}

	public String getCrnLinkedBy() {
		return this.crnLinkedBy;
	}

	public void setCrnLinkedBy(String crnLinkedBy) {
		this.crnLinkedBy = crnLinkedBy;
	}

	public String getDateClosed() {
		return this.dateClosed;
	}

	public void setDateClosed(String dateClosed) {
		this.dateClosed = dateClosed;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public long getNewFbSeqId() {
		return this.newFbSeqId;
	}

	public void setNewFbSeqId(long fbId) {
		this.newFbSeqId = fbId;
	}

	public String getLinkedCrnList() {
		return this.linkedCrnList;
	}

	public void setLinkedCrnList(String linkedCrnList) {
		this.linkedCrnList = linkedCrnList;
	}

	public String getLinkCrnList() {
		return this.linkedCrnList;
	}

	public void setLinkCrnList(String linkCrnList) {
		this.linkedCrnList = linkCrnList;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public String getActionTakenField() {
		return this.actionTakenField;
	}

	public void setActionTakenField(String actionTakenField) {
		this.actionTakenField = actionTakenField;
	}

	public String getFbDescriptionField() {
		return this.fbDescriptionField;
	}

	public void setFbDescriptionField(String fbDescriptionField) {
		this.fbDescriptionField = fbDescriptionField;
	}

	public String getClientContactField() {
		return this.clientContactField;
	}

	public void setClientContactField(String clientContactField) {
		this.clientContactField = clientContactField;
	}

	public String getFbStatusField() {
		return this.fbStatusField;
	}

	public void setFbStatusField(String fbStatusField) {
		this.fbStatusField = fbStatusField;
	}

	public String getCasOwnerField() {
		return this.casOwnerField;
	}

	public void setCasOwnerField(String casOwnerField) {
		this.casOwnerField = casOwnerField;
	}

	public String getFbCategoryField() {
		return this.fbCategoryField;
	}

	public void setFbCategoryField(String fbCategoryField) {
		this.fbCategoryField = fbCategoryField;
	}

	public String getClientNameField() {
		return this.clientNameField;
	}

	public void setClientNameField(String clientNameField) {
		this.clientNameField = clientNameField;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCaseReceivedDate() {
		return this.caseReceivedDate;
	}

	public void setCaseReceivedDate(String caseReceivedDate) {
		this.caseReceivedDate = caseReceivedDate;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getFeedBackCategoryID() {
		return this.feedBackCategoryID;
	}

	public void setFeedBackCategoryID(String feedBackCategoryID) {
		this.feedBackCategoryID = feedBackCategoryID;
	}

	public String getFeedBackCategoryName() {
		return this.feedBackCategoryName;
	}

	public void setFeedBackCategoryName(String feedBackCategoryName) {
		this.feedBackCategoryName = feedBackCategoryName;
	}

	public String getLogInUserId() {
		return this.logInUserId;
	}

	public void setLogInUserId(String logInUserId) {
		this.logInUserId = logInUserId;
	}
}