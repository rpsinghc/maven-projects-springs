package com.worldcheck.atlas.vo.masters;

import java.io.File;
import java.util.List;

public class ReportTemplateMasterVO {
	private int rptDownId;
	private String clientCode;
	private String clientName;
	private String reportType;
	private File file;
	private String fileName;
	private String fileSize;
	private String uploadDateAndTime;
	private String uploladedBy;
	private String comment;
	private String updateStartDate;
	private String updateEndDate;
	private List<String> client;
	private List<String> selectedClient;
	private String isGeneral;
	private Integer start;
	private Integer limit;
	private String rptComment;
	private String sortColumnName;
	private String sortType;

	public String getRptComment() {
		return this.rptComment;
	}

	public void setRptComment(String rptComment) {
		this.rptComment = rptComment;
	}

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

	public int getRptDownId() {
		return this.rptDownId;
	}

	public void setRptDownId(int rptDownId) {
		this.rptDownId = rptDownId;
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

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
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

	public String getUploadDateAndTime() {
		return this.uploadDateAndTime;
	}

	public void setUploadDateAndTime(String uploadDateAndTime) {
		this.uploadDateAndTime = uploadDateAndTime;
	}

	public String getUploladedBy() {
		return this.uploladedBy;
	}

	public void setUploladedBy(String uploladedBy) {
		this.uploladedBy = uploladedBy;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public List<String> getClient() {
		return this.client;
	}

	public void setClient(List<String> client) {
		this.client = client;
	}

	public List<String> getSelectedClient() {
		return this.selectedClient;
	}

	public void setSelectedClient(List<String> selectedClient) {
		this.selectedClient = selectedClient;
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