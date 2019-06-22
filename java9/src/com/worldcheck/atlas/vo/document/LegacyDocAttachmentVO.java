package com.worldcheck.atlas.vo.document;

public class LegacyDocAttachmentVO {
	private String attachmentId;
	private String crn;
	private String subjectId;
	private String isSensitive;
	private String reference;
	private String fileName;
	private String folderName;
	private String version;
	private String whoCreated;
	private String whenCreated;
	private String whoModified;
	private String whenModified;
	private String finalReportFlag;
	private String clientCode;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private Double fileSize;

	public Double getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getAttachmentId() {
		return this.attachmentId;
	}

	public Integer getLimit() {
		return this.limit;
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

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getIsSensitive() {
		return this.isSensitive;
	}

	public void setIsSensitive(String isSensitive) {
		this.isSensitive = isSensitive;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWhoCreated() {
		return this.whoCreated;
	}

	public void setWhoCreated(String whoCreated) {
		this.whoCreated = whoCreated;
	}

	public String getWhenCreated() {
		return this.whenCreated;
	}

	public void setWhenCreated(String whenCreated) {
		this.whenCreated = whenCreated;
	}

	public String getWhoModified() {
		return this.whoModified;
	}

	public void setWhoModified(String whoModified) {
		this.whoModified = whoModified;
	}

	public String getWhenModified() {
		return this.whenModified;
	}

	public void setWhenModified(String whenModified) {
		this.whenModified = whenModified;
	}

	public String getFinalReportFlag() {
		return this.finalReportFlag;
	}

	public void setFinalReportFlag(String finalReportFlag) {
		this.finalReportFlag = finalReportFlag;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
}