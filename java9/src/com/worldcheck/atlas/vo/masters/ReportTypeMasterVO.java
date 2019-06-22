package com.worldcheck.atlas.vo.masters;

import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.List;

public class ReportTypeMasterVO implements Comparable<ReportTypeMasterVO> {
	private String reportType;
	private String initialsUseCRN;
	private String associatedResearchElements;
	private String needSubReportType;
	private String turnaroundTime;
	private String subReportType;
	private String initialsUseEndCRN;
	private String researchElement;
	private String reportTypeCode;
	private String reportTypeStatus;
	private String userName;
	private long reportTypeId;
	private String[] modifiedRecords;
	private long subReportTypeId;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private String clientReqMaster;
	private String rptClientMap;
	private String reMap;
	private String reportDownloadMaster;
	private String totalRecurrCRN;
	private String totalCRN;
	private List<REMasterVO> reList;
	private String totalRecurrCRNSLSubReport;
	private String totalCRNSLSubReport;
	private String isRPTStatusChange;
	private String deactivateSubreportList;
	private String activateSubreportList;
	private String researchElementID;
	private String subreportStatus;
	private String subReportTypeStatus;
	private String isSubRPTStatusChange;
	private String subReportTypeCode;
	private long[] subReportID;
	private String[] subReportCode;
	private SubReportTypeVO[] subReportTypeVO;
	private String HasSubReport;
	private String researchElementNames;
	private String hdnAssociatedResearchElements;
	private String hdnResearchElementNames;
	private String hdnInitialsUseCRN;
	private String hdnTurnaroundTime;
	private String hdnNeedSubReportType;
	private String hdnreportTypeStatus;
	private String hdnResearchElement;
	private String hdnResearchElementID;
	private String needSubreportTypeFlag;
	private String subCRNInitial;

	public String getTotalRecurrCRNSLSubReport() {
		return this.totalRecurrCRNSLSubReport;
	}

	public void setTotalRecurrCRNSLSubReport(String totalRecurrCRNSLSubReport) {
		this.totalRecurrCRNSLSubReport = totalRecurrCRNSLSubReport;
	}

	public String getTotalCRNSLSubReport() {
		return this.totalCRNSLSubReport;
	}

	public void setTotalCRNSLSubReport(String totalCRNLSubReport) {
		this.totalCRNSLSubReport = totalCRNLSubReport;
	}

	public String getNeedSubreportTypeFlag() {
		return this.needSubreportTypeFlag;
	}

	public void setNeedSubreportTypeFlag(String needSubreportTypeFlag) {
		this.needSubreportTypeFlag = needSubreportTypeFlag;
	}

	public String getHdnResearchElement() {
		return this.hdnResearchElement;
	}

	public void setHdnResearchElement(String hdnResearchElement) {
		this.hdnResearchElement = hdnResearchElement;
	}

	public String getHdnResearchElementID() {
		return this.hdnResearchElementID;
	}

	public void setHdnResearchElementID(String hdnResearchElementID) {
		this.hdnResearchElementID = hdnResearchElementID;
	}

	public String getHdnreportTypeStatus() {
		return this.hdnreportTypeStatus;
	}

	public void setHdnreportTypeStatus(String hdnreportTypeStatus) {
		this.hdnreportTypeStatus = hdnreportTypeStatus;
	}

	public String getHdnAssociatedResearchElements() {
		return this.hdnAssociatedResearchElements;
	}

	public void setHdnAssociatedResearchElements(String hdnAssociatedResearchElements) {
		this.hdnAssociatedResearchElements = hdnAssociatedResearchElements;
	}

	public String getHdnResearchElementNames() {
		return this.hdnResearchElementNames;
	}

	public void setHdnResearchElementNames(String hdnResearchElementNames) {
		this.hdnResearchElementNames = hdnResearchElementNames;
	}

	public String getHdnInitialsUseCRN() {
		return this.hdnInitialsUseCRN;
	}

	public void setHdnInitialsUseCRN(String hdnInitialsUseCRN) {
		this.hdnInitialsUseCRN = hdnInitialsUseCRN;
	}

	public String getHdnTurnaroundTime() {
		return this.hdnTurnaroundTime;
	}

	public void setHdnTurnaroundTime(String hdnTurnaroundTime) {
		this.hdnTurnaroundTime = hdnTurnaroundTime;
	}

	public String getHdnNeedSubReportType() {
		return this.hdnNeedSubReportType;
	}

	public void setHdnNeedSubReportType(String hdnNeedSubReportType) {
		this.hdnNeedSubReportType = hdnNeedSubReportType;
	}

	public String getResearchElementNames() {
		return this.researchElementNames;
	}

	public void setResearchElementNames(String researchElementNames) {
		this.researchElementNames = researchElementNames;
	}

	public String getHasSubReport() {
		return this.HasSubReport;
	}

	public void setHasSubReport(String hasSubReport) {
		this.HasSubReport = hasSubReport;
	}

	public SubReportTypeVO[] getSubReportTypeVO() {
		return this.subReportTypeVO;
	}

	public void setSubReportTypeVO(SubReportTypeVO[] subReportTypeVO) {
		this.subReportTypeVO = subReportTypeVO;
	}

	public long[] getSubReportID() {
		return this.subReportID;
	}

	public void setSubReportID(long[] subReportID) {
		this.subReportID = subReportID;
	}

	public String[] getSubReportCode() {
		return this.subReportCode;
	}

	public void setSubReportCode(String[] subReportCode) {
		this.subReportCode = subReportCode;
	}

	public String getSubReportTypeCode() {
		return this.subReportTypeCode;
	}

	public void setSubReportTypeCode(String subReportTypeCode) {
		this.subReportTypeCode = subReportTypeCode;
	}

	public String getIsSubRPTStatusChange() {
		return this.isSubRPTStatusChange;
	}

	public void setIsSubRPTStatusChange(String isSubRPTStatusChange) {
		this.isSubRPTStatusChange = isSubRPTStatusChange;
	}

	public String getActivateSubreportList() {
		return this.activateSubreportList;
	}

	public void setActivateSubreportList(String activateSubreportList) {
		this.activateSubreportList = activateSubreportList;
	}

	public String getSubReportTypeStatus() {
		return this.subReportTypeStatus;
	}

	public void setSubReportTypeStatus(String subReportTypeStatus) {
		this.subReportTypeStatus = subReportTypeStatus;
	}

	public String getSubreportStatus() {
		return this.subreportStatus;
	}

	public void setSubreportStatus(String subreportStatus) {
		this.subreportStatus = subreportStatus;
	}

	public String getResearchElementID() {
		return this.researchElementID;
	}

	public void setResearchElementID(String researchElementID) {
		this.researchElementID = researchElementID;
	}

	public String getDeactivateSubreportList() {
		return this.deactivateSubreportList;
	}

	public void setDeactivateSubreportList(String deactivateSubreportList) {
		this.deactivateSubreportList = deactivateSubreportList;
	}

	public String getIsRPTStatusChange() {
		return this.isRPTStatusChange;
	}

	public void setIsRPTStatusChange(String isRPTStatusChange) {
		this.isRPTStatusChange = isRPTStatusChange;
	}

	public long getSubReportTypeId() {
		return this.subReportTypeId;
	}

	public void setSubReportTypeId(long subReportTypeId) {
		this.subReportTypeId = subReportTypeId;
	}

	public String[] getModifiedRecords() {
		return this.modifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.modifiedRecords = modifiedRecords;
	}

	public long getReportTypeId() {
		return this.reportTypeId;
	}

	public void setReportTypeId(long reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getInitialsUseCRN() {
		return this.initialsUseCRN;
	}

	public void setInitialsUseCRN(String initialsUseCRN) {
		this.initialsUseCRN = initialsUseCRN;
	}

	public String getAssociatedResearchElements() {
		return this.associatedResearchElements;
	}

	public void setAssociatedResearchElements(String associatedResearchElements) {
		this.associatedResearchElements = associatedResearchElements;
	}

	public String getNeedSubReportType() {
		return this.needSubReportType;
	}

	public void setNeedSubReportType(String needSubReportType) {
		this.needSubReportType = needSubReportType;
	}

	public String getTurnaroundTime() {
		return this.turnaroundTime;
	}

	public void setTurnaroundTime(String turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public String getInitialsUseEndCRN() {
		return this.initialsUseEndCRN;
	}

	public void setInitialsUseEndCRN(String initialsUseEndCRN) {
		this.initialsUseEndCRN = initialsUseEndCRN;
	}

	public String getResearchElement() {
		return this.researchElement;
	}

	public void setResearchElement(String researchElement) {
		this.researchElement = researchElement;
	}

	public String getReportTypeCode() {
		return this.reportTypeCode;
	}

	public void setReportTypeCode(String reportTypeCode) {
		this.reportTypeCode = reportTypeCode;
	}

	public String getReportTypeStatus() {
		return this.reportTypeStatus;
	}

	public void setReportTypeStatus(String reportTypeStatus) {
		this.reportTypeStatus = reportTypeStatus;
	}

	public int compareTo(ReportTypeMasterVO vo) {
		return this.reportType.compareTo(vo.getReportType());
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

	public String getClientReqMaster() {
		return this.clientReqMaster;
	}

	public void setClientReqMaster(String clientReqMaster) {
		this.clientReqMaster = clientReqMaster;
	}

	public String getRptClientMap() {
		return this.rptClientMap;
	}

	public void setRptClientMap(String rptClientMap) {
		this.rptClientMap = rptClientMap;
	}

	public String getReMap() {
		return this.reMap;
	}

	public void setReMap(String reMap) {
		this.reMap = reMap;
	}

	public String getReportDownloadMaster() {
		return this.reportDownloadMaster;
	}

	public void setReportDownloadMaster(String reportDownloadMaster) {
		this.reportDownloadMaster = reportDownloadMaster;
	}

	public String getTotalRecurrCRN() {
		return this.totalRecurrCRN;
	}

	public void setTotalRecurrCRN(String totalRecurrCRN) {
		this.totalRecurrCRN = totalRecurrCRN;
	}

	public String getTotalCRN() {
		return this.totalCRN;
	}

	public void setTotalCRN(String totalCRN) {
		this.totalCRN = totalCRN;
	}

	public void setReList(List<REMasterVO> reList) {
		this.reList = reList;
	}

	public List<REMasterVO> getReList() {
		return this.reList;
	}

	public String getSubCRNInitial() {
		return this.subCRNInitial;
	}

	public void setSubCRNInitial(String subCRNInitial) {
		this.subCRNInitial = subCRNInitial;
	}
}