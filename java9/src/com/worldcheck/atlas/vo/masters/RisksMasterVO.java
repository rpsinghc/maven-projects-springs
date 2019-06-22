package com.worldcheck.atlas.vo.masters;

import java.util.ArrayList;
import java.util.List;

public class RisksMasterVO {
	private String status;
	private String risks;
	private String risksStatus;
	private String riskcode;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private String risksTest;
	private String riskGroup;
	private String visibleToClient;
	private String subjectType;
	private List<String> clientCodeList;
	private List<String> subjectCountryList;
	private List<String> researchElementList;
	private List<String> reportTypeList;
	private List<String> subReportTypeList;
	private List<String> riskCategoryList;
	private List<String> associatedSubReportTypeList;
	private String fromDate;
	private String toDate;
	private String selectedClients;
	private String selectedReportTypes;
	private String selectedSubReportTypes;
	private String selectedSubjectCountries;
	private String selectedResearchElements;
	private String selectedRiskCategories;
	private String associatedSubReports;
	private String action;
	private String newRiskLabel;
	private String oldRiskStatus;
	private String newRiskStatus;
	private String oldDisplayOnProfile;
	private String newDisplayOnProfile;
	private String oldRemark;
	private String newRemark;
	private int riskId;
	private String riskCode;
	private String prePendRiskCode;
	private String updatedBy;
	private String totalCRN;
	private String subjectCount;
	private String riskCategory;
	private String riskLabel;
	private String riskType;
	private String riskTypeName;
	private String riskActive;
	private int riskIsActive;
	private String countryBreakdown;
	private int hasCountryBreakdown;
	private String remarks;
	private int displayOnProfileForm;
	private String displayOnProfile;
	private String updatedOn;
	private String hiddenText;
	private int historyId;
	private int isRiskMandatory;
	private int isRiskApplied;
	private int countryId;
	private String countryName;
	private List<RiskAttributeVO> riskAttributeVOList;
	private String oldRiskIsActive;
	private long visibleToClientForm;
	private String jsonData;
	private int oldCountryBreakdown;
	private String oldRemarks;
	private String oldDisplayOnProfileForm;
	private String oldRiskLabel;
	private String creationDate;
	private String riskCategoryLabel;
	private String categoryName;
	private ArrayList<RisksMapVO> riskMapVO;

	public String getRisksTest() {
		return this.risksTest;
	}

	public void setRisksTest(String risksTest) {
		this.risksTest = risksTest;
	}

	public String getRiskGroup() {
		return this.riskGroup;
	}

	public void setRiskGroup(String riskGroup) {
		this.riskGroup = riskGroup;
	}

	public String getVisibleToClient() {
		return this.visibleToClient;
	}

	public void setVisibleToClient(String visibleToClient) {
		this.visibleToClient = visibleToClient;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public List<String> getClientCodeList() {
		return this.clientCodeList;
	}

	public void setClientCodeList(List<String> clientCodeList) {
		this.clientCodeList = clientCodeList;
	}

	public List<String> getSubjectCountryList() {
		return this.subjectCountryList;
	}

	public void setSubjectCountryList(List<String> subjectCountryList) {
		this.subjectCountryList = subjectCountryList;
	}

	public List<String> getResearchElementList() {
		return this.researchElementList;
	}

	public void setResearchElementList(List<String> researchElementList) {
		this.researchElementList = researchElementList;
	}

	public List<String> getReportTypeList() {
		return this.reportTypeList;
	}

	public void setReportTypeList(List<String> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public List<String> getSubReportTypeList() {
		return this.subReportTypeList;
	}

	public void setSubReportTypeList(List<String> subReportTypeList) {
		this.subReportTypeList = subReportTypeList;
	}

	public List<String> getRiskCategoryList() {
		return this.riskCategoryList;
	}

	public void setRiskCategoryList(List<String> riskCategoryList) {
		this.riskCategoryList = riskCategoryList;
	}

	public List<String> getAssociatedSubReportTypeList() {
		return this.associatedSubReportTypeList;
	}

	public void setAssociatedSubReportTypeList(List<String> associatedSubReportTypeList) {
		this.associatedSubReportTypeList = associatedSubReportTypeList;
	}

	public String getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return this.toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getSelectedClients() {
		return this.selectedClients;
	}

	public void setSelectedClients(String selectedClients) {
		this.selectedClients = selectedClients;
	}

	public String getSelectedReportTypes() {
		return this.selectedReportTypes;
	}

	public void setSelectedReportTypes(String selectedReportTypes) {
		this.selectedReportTypes = selectedReportTypes;
	}

	public String getSelectedSubReportTypes() {
		return this.selectedSubReportTypes;
	}

	public void setSelectedSubReportTypes(String selectedSubReportTypes) {
		this.selectedSubReportTypes = selectedSubReportTypes;
	}

	public String getSelectedSubjectCountries() {
		return this.selectedSubjectCountries;
	}

	public void setSelectedSubjectCountries(String selectedSubjectCountries) {
		this.selectedSubjectCountries = selectedSubjectCountries;
	}

	public String getSelectedResearchElements() {
		return this.selectedResearchElements;
	}

	public void setSelectedResearchElements(String selectedResearchElements) {
		this.selectedResearchElements = selectedResearchElements;
	}

	public String getSelectedRiskCategories() {
		return this.selectedRiskCategories;
	}

	public void setSelectedRiskCategories(String selectedRiskCategories) {
		this.selectedRiskCategories = selectedRiskCategories;
	}

	public String getAssociatedSubReports() {
		return this.associatedSubReports;
	}

	public void setAssociatedSubReports(String associatedSubReports) {
		this.associatedSubReports = associatedSubReports;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNewRiskLabel() {
		return this.newRiskLabel;
	}

	public void setNewRiskLabel(String newRiskLabel) {
		this.newRiskLabel = newRiskLabel;
	}

	public String getOldRiskStatus() {
		return this.oldRiskStatus;
	}

	public void setOldRiskStatus(String oldRiskStatus) {
		this.oldRiskStatus = oldRiskStatus;
	}

	public String getNewRiskStatus() {
		return this.newRiskStatus;
	}

	public void setNewRiskStatus(String newRiskStatus) {
		this.newRiskStatus = newRiskStatus;
	}

	public String getOldDisplayOnProfile() {
		return this.oldDisplayOnProfile;
	}

	public void setOldDisplayOnProfile(String oldDisplayOnProfile) {
		this.oldDisplayOnProfile = oldDisplayOnProfile;
	}

	public String getNewDisplayOnProfile() {
		return this.newDisplayOnProfile;
	}

	public void setNewDisplayOnProfile(String newDisplayOnProfile) {
		this.newDisplayOnProfile = newDisplayOnProfile;
	}

	public String getOldRemark() {
		return this.oldRemark;
	}

	public void setOldRemark(String oldRemark) {
		this.oldRemark = oldRemark;
	}

	public String getNewRemark() {
		return this.newRemark;
	}

	public void setNewRemark(String newRemark) {
		this.newRemark = newRemark;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRisks() {
		return this.risks;
	}

	public void setRisks(String risks) {
		this.risks = risks;
	}

	public String getRisksStatus() {
		return this.risksStatus;
	}

	public void setRisksStatus(String risksStatus) {
		this.risksStatus = risksStatus;
	}

	public String getRiskcode() {
		return this.riskcode;
	}

	public void setRiskcode(String riskcode) {
		this.riskcode = riskcode;
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

	public long getVisibleToClientForm() {
		return this.visibleToClientForm;
	}

	public String getJsonData() {
		return this.jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public void setVisibleToClientForm(long visibleToClientForm) {
		this.visibleToClientForm = visibleToClientForm;
	}

	public int getIsRiskMandatory() {
		return this.isRiskMandatory;
	}

	public void setIsRiskMandatory(int isRiskMandatory) {
		this.isRiskMandatory = isRiskMandatory;
	}

	public int getIsRiskApplied() {
		return this.isRiskApplied;
	}

	public void setIsRiskApplied(int isRiskApplied) {
		this.isRiskApplied = isRiskApplied;
	}

	public int getCountryId() {
		return this.countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<RiskAttributeVO> getRiskAttributeVOList() {
		return this.riskAttributeVOList;
	}

	public void setRiskAttributeVOList(List<RiskAttributeVO> riskAttributeVOList) {
		this.riskAttributeVOList = riskAttributeVOList;
	}

	public int getRiskId() {
		return this.riskId;
	}

	public void setRiskId(int riskId) {
		this.riskId = riskId;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getPrePendRiskCode() {
		return this.prePendRiskCode;
	}

	public void setPrePendRiskCode(String prePendRiskCode) {
		this.prePendRiskCode = prePendRiskCode;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getTotalCRN() {
		return this.totalCRN;
	}

	public void setTotalCRN(String totalCRN) {
		this.totalCRN = totalCRN;
	}

	public String getSubjectCount() {
		return this.subjectCount;
	}

	public void setSubjectCount(String subjectCount) {
		this.subjectCount = subjectCount;
	}

	public String getRiskCategory() {
		return this.riskCategory;
	}

	public void setRiskCategory(String riskCategory) {
		this.riskCategory = riskCategory;
	}

	public String getRiskLabel() {
		return this.riskLabel;
	}

	public void setRiskLabel(String riskLabel) {
		this.riskLabel = riskLabel;
	}

	public String getRiskType() {
		return this.riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getRiskTypeName() {
		return this.riskTypeName;
	}

	public void setRiskTypeName(String riskTypeName) {
		this.riskTypeName = riskTypeName;
	}

	public String getRiskActive() {
		return this.riskActive;
	}

	public void setRiskActive(String riskActive) {
		this.riskActive = riskActive;
	}

	public int getRiskIsActive() {
		return this.riskIsActive;
	}

	public void setRiskIsActive(int riskIsActive) {
		this.riskIsActive = riskIsActive;
	}

	public String getCountryBreakdown() {
		return this.countryBreakdown;
	}

	public void setCountryBreakdown(String countryBreakdown) {
		this.countryBreakdown = countryBreakdown;
	}

	public int getHasCountryBreakdown() {
		return this.hasCountryBreakdown;
	}

	public void setHasCountryBreakdown(int hasCountryBreakdown) {
		this.hasCountryBreakdown = hasCountryBreakdown;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getDisplayOnProfileForm() {
		return this.displayOnProfileForm;
	}

	public void setDisplayOnProfileForm(int displayOnProfileForm) {
		this.displayOnProfileForm = displayOnProfileForm;
	}

	public String getDisplayOnProfile() {
		return this.displayOnProfile;
	}

	public void setDisplayOnProfile(String displayOnProfile) {
		this.displayOnProfile = displayOnProfile;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getHiddenText() {
		return this.hiddenText;
	}

	public void setHiddenText(String hiddenText) {
		this.hiddenText = hiddenText;
	}

	public int getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public int getOldCountryBreakdown() {
		return this.oldCountryBreakdown;
	}

	public void setOldCountryBreakdown(int oldCountryBreakdown) {
		this.oldCountryBreakdown = oldCountryBreakdown;
	}

	public String getOldRemarks() {
		return this.oldRemarks;
	}

	public void setOldRemarks(String oldRemarks) {
		this.oldRemarks = oldRemarks;
	}

	public String getOldRiskIsActive() {
		return this.oldRiskIsActive;
	}

	public void setOldRiskIsActive(String oldRiskIsActive) {
		this.oldRiskIsActive = oldRiskIsActive;
	}

	public String getOldDisplayOnProfileForm() {
		return this.oldDisplayOnProfileForm;
	}

	public void setOldDisplayOnProfileForm(String oldDisplayOnProfileForm) {
		this.oldDisplayOnProfileForm = oldDisplayOnProfileForm;
	}

	public String getOldRiskLabel() {
		return this.oldRiskLabel;
	}

	public void setOldRiskLabel(String oldRiskLabel) {
		this.oldRiskLabel = oldRiskLabel;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getRiskCategoryLabel() {
		return this.riskCategoryLabel;
	}

	public void setRiskCategoryLabel(String riskCategoryLabel) {
		this.riskCategoryLabel = riskCategoryLabel;
	}

	public ArrayList<RisksMapVO> getRiskMapVO() {
		return this.riskMapVO;
	}

	public void setRiskMapVO(ArrayList<RisksMapVO> riskMapVO) {
		this.riskMapVO = riskMapVO;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}