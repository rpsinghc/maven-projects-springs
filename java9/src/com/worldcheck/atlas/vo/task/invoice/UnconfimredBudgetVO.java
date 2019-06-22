package com.worldcheck.atlas.vo.task.invoice;

import com.worldcheck.atlas.vo.SubjectDetails;
import java.util.List;

public class UnconfimredBudgetVO {
	private String CRN;
	private long pid;
	private String caseStatus;
	private String receivedDate;
	private String IDD1;
	private String IDD2;
	private String finalDate;
	private String caseFee;
	private String currency;
	private String updatedBy;
	private String[] modifiedRecords;
	private String RIDD1;
	private String RIDD2;
	private String finalRDate;
	private String teamId;
	private String STIDD1;
	private String STIDD2;
	private String supportingTeam1DueDate;
	private String caseMgrId;
	private String caseManagerName;
	private String bulkOrderId;
	private String isisOrderId;
	private int start;
	private int limit;
	private String sortColumnName;
	private String sortType;
	private String caseHistoryPerformer;
	private String isConfirmed;
	private SubjectDetails subjectDetails;
	private String isSubreportRequire;
	private String newInfo;
	private String oldInfo;
	private String isCBDFail;
	private String subjectGrid;
	private String store;
	private String gridData;
	private String taskName;
	private String[] subjectGridData;
	private List<SubjectDetails> subjectList;
	private List<String> subjectBudgetList;
	private String subjectID;
	private String isisSubjectID;
	private String subjectName;
	private String countryName;
	private String entityTYPE;
	private String SUBREPORT;
	private String subjectFee;
	private String subjectCurrency;

	public List<String> getSubjectBudgetList() {
		return this.subjectBudgetList;
	}

	public void setSubjectBudgetList(List<String> subjectBudgetList) {
		this.subjectBudgetList = subjectBudgetList;
	}

	public List<SubjectDetails> getSubjectList() {
		return this.subjectList;
	}

	public void setSubjectList(List<SubjectDetails> subjectList) {
		this.subjectList = subjectList;
	}

	public String getSubjectID() {
		return this.subjectID;
	}

	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}

	public String getIsisSubjectID() {
		return this.isisSubjectID;
	}

	public void setIsisSubjectID(String isisSubjectID) {
		this.isisSubjectID = isisSubjectID;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getEntityTYPE() {
		return this.entityTYPE;
	}

	public void setEntityTYPE(String entityTYPE) {
		this.entityTYPE = entityTYPE;
	}

	public String getSUBREPORT() {
		return this.SUBREPORT;
	}

	public void setSUBREPORT(String sUBREPORT) {
		this.SUBREPORT = sUBREPORT;
	}

	public String getSubjectFee() {
		return this.subjectFee;
	}

	public void setSubjectFee(String subjectFee) {
		this.subjectFee = subjectFee;
	}

	public String getSubjectCurrency() {
		return this.subjectCurrency;
	}

	public void setSubjectCurrency(String subjectCurrency) {
		this.subjectCurrency = subjectCurrency;
	}

	public String[] getSubjectGridData() {
		return this.subjectGridData;
	}

	public void setSubjectGridData(String[] subjectGridData) {
		this.subjectGridData = subjectGridData;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getGridData() {
		return this.gridData;
	}

	public void setGridData(String gridData) {
		this.gridData = gridData;
	}

	public String getStore() {
		return this.store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getIsSubreportRequire() {
		return this.isSubreportRequire;
	}

	public void setIsSubreportRequire(String isSubreportRequire) {
		this.isSubreportRequire = isSubreportRequire;
	}

	public String getIsCBDFail() {
		return this.isCBDFail;
	}

	public void setIsCBDFail(String isCBDFail) {
		this.isCBDFail = isCBDFail;
	}

	public String getSubjectGrid() {
		return this.subjectGrid;
	}

	public void setSubjectGrid(String subjectGrid) {
		this.subjectGrid = subjectGrid;
	}

	public String getNewInfo() {
		return this.newInfo;
	}

	public void setNewInfo(String newInfo) {
		this.newInfo = newInfo;
	}

	public String getOldInfo() {
		return this.oldInfo;
	}

	public void setOldInfo(String oldInfo) {
		this.oldInfo = oldInfo;
	}

	public String[] getModifiedRecords() {
		return this.modifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.modifiedRecords = modifiedRecords;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public String getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getFinalDate() {
		return this.finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getIDD1() {
		return this.IDD1;
	}

	public void setIDD1(String iDD1) {
		this.IDD1 = iDD1;
	}

	public String getIDD2() {
		return this.IDD2;
	}

	public void setIDD2(String iDD2) {
		this.IDD2 = iDD2;
	}

	public String getFinalRDate() {
		return this.finalRDate;
	}

	public void setFinalRDate(String finalRDate) {
		this.finalRDate = finalRDate;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
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

	public String getBulkOrderId() {
		return this.bulkOrderId;
	}

	public void setBulkOrderId(String bulkOrderId) {
		this.bulkOrderId = bulkOrderId;
	}

	public String getIsisOrderId() {
		return this.isisOrderId;
	}

	public void setIsisOrderId(String isisOrderId) {
		this.isisOrderId = isisOrderId;
	}

	public long getPid() {
		return this.pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCaseHistoryPerformer() {
		return this.caseHistoryPerformer;
	}

	public void setCaseHistoryPerformer(String caseHistoryPerformer) {
		this.caseHistoryPerformer = caseHistoryPerformer;
	}

	public String getIsConfirmed() {
		return this.isConfirmed;
	}

	public void setIsConfirmed(String isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public void setSupportingTeam1DueDate(String supportingTeam1DueDate) {
		this.supportingTeam1DueDate = supportingTeam1DueDate;
	}

	public String getSupportingTeam1DueDate() {
		return this.supportingTeam1DueDate;
	}

	public void setCaseMgrId(String caseMgrId) {
		this.caseMgrId = caseMgrId;
	}

	public String getCaseMgrId() {
		return this.caseMgrId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamId() {
		return this.teamId;
	}

	public String getRIDD1() {
		return this.RIDD1;
	}

	public void setRIDD1(String rIDD1) {
		this.RIDD1 = rIDD1;
	}

	public String getRIDD2() {
		return this.RIDD2;
	}

	public void setRIDD2(String rIDD2) {
		this.RIDD2 = rIDD2;
	}

	public String getSTIDD1() {
		return this.STIDD1;
	}

	public void setSTIDD1(String sTIDD1) {
		this.STIDD1 = sTIDD1;
	}

	public String getSTIDD2() {
		return this.STIDD2;
	}

	public void setSTIDD2(String sTIDD2) {
		this.STIDD2 = sTIDD2;
	}

	public void setCaseManagerName(String caseManagerName) {
		this.caseManagerName = caseManagerName;
	}

	public String getCaseManagerName() {
		return this.caseManagerName;
	}
}