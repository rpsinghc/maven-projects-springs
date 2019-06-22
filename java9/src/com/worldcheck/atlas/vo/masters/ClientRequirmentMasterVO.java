package com.worldcheck.atlas.vo.masters;

import java.io.File;
import java.util.List;

public class ClientRequirmentMasterVO {
	private int clientReqId;
	private List<String> clientList;
	private List<String> selectedClientList;
	private File file;
	private String clientCode;
	private String clientName;
	private String reportType;
	private String instruction;
	private String comment;
	private String uploadedFile;
	private String fileSize;
	private String uploadedDateAndTime;
	private String uploladedBy;
	private String updateStartDate;
	private String updateEndDate;
	private String isGeneral;
	private Integer start;
	private Integer limit;
	private String clientReqComment;
	private String sortColumnName;
	private String sortType;

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getIsGeneral() {
		return this.isGeneral;
	}

	public void setIsGeneral(String isGeneral) {
		this.isGeneral = isGeneral;
	}

	public int getClientReqId() {
		return this.clientReqId;
	}

	public void setClientReqId(int clientReqId) {
		this.clientReqId = clientReqId;
	}

	public List<String> getClientList() {
		return this.clientList;
	}

	public void setClientList(List<String> clientList) {
		this.clientList = clientList;
	}

	public List<String> getSelectedClientList() {
		return this.selectedClientList;
	}

	public void setSelectedClientList(List<String> selectedClientList) {
		this.selectedClientList = selectedClientList;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
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

	public String getInstruction() {
		return this.instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUploadedFile() {
		return this.uploadedFile;
	}

	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getUploadedDateAndTime() {
		return this.uploadedDateAndTime;
	}

	public void setUploadedDateAndTime(String uploadedDateAndTime) {
		this.uploadedDateAndTime = uploadedDateAndTime;
	}

	public String getUploladedBy() {
		return this.uploladedBy;
	}

	public void setUploladedBy(String uploladedBy) {
		this.uploladedBy = uploladedBy;
	}

	public String getUpdateStartDate() {
		return this.updateStartDate;
	}

	public void setUpdateStartDate(String updateStartDate) {
		this.updateStartDate = updateStartDate;
	}

	public String getUpdateEndDate() {
		return this.updateEndDate;
	}

	public void setUpdateEndDate(String updateEndDate) {
		this.updateEndDate = updateEndDate;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getClientReqComment() {
		return this.clientReqComment;
	}

	public void setClientReqComment(String clientReqComment) {
		this.clientReqComment = clientReqComment;
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
}