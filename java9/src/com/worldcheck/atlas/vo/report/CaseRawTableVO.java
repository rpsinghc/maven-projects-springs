package com.worldcheck.atlas.vo.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CaseRawTableVO {
	private String userId;
	private String templateId;
	private int caseRawDataTemplateId;
	private String templateName;
	private String templateComments;
	private String created_by;
	private Date updated_on;
	private String updated_by;
	private String shareWith;
	private String defaultTemplate;
	private String CRN;
	private String reportType;
	private String clientCode;
	private String clientName;
	private String casePriority;
	private String caseDetails;
	private String selectedInfoId;
	private String selectedInfo;
	private String selectedOption;
	private String searchType;
	private String fieldMapId;
	private String startDate;
	private String endDate;
	private String dateType;
	private String optionStatus;
	private String caseRawDataMasterFieldId;
	private String caseRawDataMasterColumn;
	private List<String> fieldName;
	private String selectedReportType;
	private String selectedSubReportType;
	private String selectedClientCode;
	private String selectedCaseManager;
	private String selectedCaseStatus;
	private String selectedPriTeamOffice;
	private String selectedSuppTeam1;
	private String selectedSuppTeam2;
	private String selectedSuppTeam3;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private String defaultStatus;
	private String fieldFullName;
	private String creatorFullName;
	private String buttonName;
	private List<HashMap> excelDataList;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCaseRawDataTemplateId() {
		return this.caseRawDataTemplateId;
	}

	public void setCaseRawDataTemplateId(int caseRawDataTemplateId) {
		this.caseRawDataTemplateId = caseRawDataTemplateId;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateComments() {
		return this.templateComments;
	}

	public void setTemplateComments(String templateComments) {
		this.templateComments = templateComments;
	}

	public String getShareWith() {
		return this.shareWith;
	}

	public void setShareWith(String shareWith) {
		this.shareWith = shareWith;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String crn) {
		this.CRN = crn;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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

	public String getCasePriority() {
		return this.casePriority;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public String getCaseDetails() {
		return this.caseDetails;
	}

	public void setCaseDetails(String caseDetails) {
		this.caseDetails = caseDetails;
	}

	public String getSelectedInfo() {
		return this.selectedInfo;
	}

	public void setSelectedInfo(String selectedInfo) {
		this.selectedInfo = selectedInfo;
	}

	public String getCreated_by() {
		return this.created_by;
	}

	public void setCreated_by(String createdBy) {
		this.created_by = createdBy;
	}

	public Date getUpdated_on() {
		return this.updated_on;
	}

	public void setUpdated_on(Date updatedOn) {
		this.updated_on = updatedOn;
	}

	public String getUpdated_by() {
		return this.updated_by;
	}

	public void setUpdated_by(String updatedBy) {
		this.updated_by = updatedBy;
	}

	public String getSearchType() {
		return this.searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getCaseRawDataMasterFieldId() {
		return this.caseRawDataMasterFieldId;
	}

	public void setCaseRawDataMasterFieldId(String caseRawDataMasterFieldId) {
		this.caseRawDataMasterFieldId = caseRawDataMasterFieldId;
	}

	public String getCaseRawDataMasterColumn() {
		return this.caseRawDataMasterColumn;
	}

	public void setCaseRawDataMasterColumn(String caseRawDataMasterColumn) {
		this.caseRawDataMasterColumn = caseRawDataMasterColumn;
	}

	public String getDefaultTemplate() {
		return this.defaultTemplate;
	}

	public void setDefaultTemplate(String defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}

	public String getSelectedOption() {
		return this.selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}

	public String getFieldMapId() {
		return this.fieldMapId;
	}

	public void setFieldMapId(String fieldMapId) {
		this.fieldMapId = fieldMapId;
	}

	public String getSelectedInfoId() {
		return this.selectedInfoId;
	}

	public void setSelectedInfoId(String selectedInfoId) {
		this.selectedInfoId = selectedInfoId;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDateType() {
		return this.dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getOptionStatus() {
		return this.optionStatus;
	}

	public void setOptionStatus(String optionStatus) {
		this.optionStatus = optionStatus;
	}

	public List<String> getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(List<String> fieldName) {
		this.fieldName = fieldName;
	}

	public String getSelectedReportType() {
		return this.selectedReportType;
	}

	public void setSelectedReportType(String selectedReportType) {
		this.selectedReportType = selectedReportType;
	}

	public String getSelectedClientCode() {
		return this.selectedClientCode;
	}

	public void setSelectedClientCode(String selectedClientCode) {
		this.selectedClientCode = selectedClientCode;
	}

	public String getSelectedCaseManager() {
		return this.selectedCaseManager;
	}

	public void setSelectedCaseManager(String selectedCaseManager) {
		this.selectedCaseManager = selectedCaseManager;
	}

	public String getSelectedCaseStatus() {
		return this.selectedCaseStatus;
	}

	public void setSelectedCaseStatus(String selectedCaseStatus) {
		this.selectedCaseStatus = selectedCaseStatus;
	}

	public String getSelectedPriTeamOffice() {
		return this.selectedPriTeamOffice;
	}

	public void setSelectedPriTeamOffice(String selectedPriTeamOffice) {
		this.selectedPriTeamOffice = selectedPriTeamOffice;
	}

	public String getSelectedSuppTeam1() {
		return this.selectedSuppTeam1;
	}

	public void setSelectedSuppTeam1(String selectedSuppTeam1) {
		this.selectedSuppTeam1 = selectedSuppTeam1;
	}

	public String getSelectedSuppTeam2() {
		return this.selectedSuppTeam2;
	}

	public void setSelectedSuppTeam2(String selectedSuppTeam2) {
		this.selectedSuppTeam2 = selectedSuppTeam2;
	}

	public String getSelectedSuppTeam3() {
		return this.selectedSuppTeam3;
	}

	public void setSelectedSuppTeam3(String selectedSuppTeam3) {
		this.selectedSuppTeam3 = selectedSuppTeam3;
	}

	public List<HashMap> getExcelDataList() {
		return this.excelDataList;
	}

	public void setExcelDataList(List<HashMap> excelDataList) {
		this.excelDataList = excelDataList;
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

	public String getDefaultStatus() {
		return this.defaultStatus;
	}

	public void setDefaultStatus(String defaultStatus) {
		this.defaultStatus = defaultStatus;
	}

	public String getFieldFullName() {
		return this.fieldFullName;
	}

	public void setFieldFullName(String fieldFullName) {
		this.fieldFullName = fieldFullName;
	}

	public String getCreatorFullName() {
		return this.creatorFullName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonName() {
		return this.buttonName;
	}

	public void setSelectedSubReportType(String selectedSubReportType) {
		this.selectedSubReportType = selectedSubReportType;
	}

	public String getSelectedSubReportType() {
		return this.selectedSubReportType;
	}
}