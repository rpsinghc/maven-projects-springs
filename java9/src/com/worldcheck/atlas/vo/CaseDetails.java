package com.worldcheck.atlas.vo;

import com.savvion.jdbc.oraclebase.ddz;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.io.IOException;
import java.io.Reader;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaseDetails {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.vo.CaseDetails");
	private String action;
	private String crn;
	private String pid;
	private String taskName;
	private String taskStatus;
	private int caseHistoryId;
	private String clientCode;
	private String clientName;
	private String clientRef;
	private String reportTypeId;
	private String reportType;
	private String subReportTypeId;
	private String subReportType;
	private String officeId;
	private String officeName;
	private String caseMgrId;
	private String sfdcOtnNumbers;
	private String regionalTerritory;
	private String caseManagerName;
	private int isRecurCase;
	private int expressCase;
	private Timestamp reqRecdDate;
	private String reqRecdDate1;
	private Date finalDueDate;
	private Date cInterim1;
	private Date cInterim2;
	private Date finalRDueDate;
	private Date rInterim1;
	private Date rInterim2;
	private String clientCodes;
	private Date clientSentDate1;
	private Date clientSentDate2;
	private Date clientFinalDate;
	private int isAutoOfcAssign;
	private String caseInfo;
	private Object caseInfoBlock;
	private String clientFeedBack;
	private int caseStatusId;
	private String caseStatus;
	private int clientReportStatus;
	private String caseCreatorId;
	private String caseCreatorFullName;
	private String updatedBy;
	private List<SubjectDetails> subjectList;
	private SubjectDetails subjectREList;
	private List<String> officeList;
	private java.util.Date dueDate1;
	private java.util.Date dueDate2;
	private List<TeamDetails> teamList;
	private String caseFee;
	private String caseCurrency;
	private String invoiceNumber;
	private List<VendorDetailVO> vendorList;
	private String casePriority;
	private boolean isExpressCase;
	private List<CommentDetails> commentList;
	private boolean isCapeTown;
	private java.util.Date capeTownDate;
	private boolean reFlag = false;
	private boolean teamFlag = false;
	private boolean analystFlag = false;
	private boolean subjectFlag = false;
	private boolean reviewerFlag = false;
	private boolean interimCycleFlag = false;
	private boolean dueDateFlag = false;
	private boolean primarySubjectFlag = false;
	private boolean teamReFlag = false;
	private boolean caseManagerFlag = false;
	private boolean researchDueDateFlag = false;
	private boolean mainAnalystFlag = false;
	private boolean dueDateUpdatedFlag = false;
	private String[] ModifiedRecords;
	private String primarySubjectName;
	private String primarySubjectID;
	private String primarySubjectCountryName;
	private String otherSubjectNameCountryMap;
	private boolean checkBizSolo;
	private String primaryTeamName;
	private boolean pullbackFlag = false;
	private List<String> addedSubTeamReMappings = new ArrayList();
	private List<String> deletedSubTeamReMappings = new ArrayList();
	private List<String> addedTeamReList = new ArrayList();
	private List<String> deletedTeamReList = new ArrayList();
	private List<String> addedTeams = new ArrayList();
	private List<String> deletedTeams = new ArrayList();
	private List<String> addedAnalysts = new ArrayList();
	private List<String> deletedAnalysts = new ArrayList();
	private List<String> addedReviewers = new ArrayList();
	private HashMap<String, String> mainAnalystMap = new HashMap();
	private List<String> updatedDueDatesList = new ArrayList();
	private String researchDueDates;
	private String isisUser;
	private String caseHistoryPerformer;
	private boolean isTaskCompleted = true;
	private String isSubjLevelSubRPTReq;
	private int clSubreportID;
	private String salesRepresentative;
	private List<String> replicatedSubjectList;
	private List<String> replicationOffice;
	private List<BranchOfficeMasterVO> officeBasedSubjectList;
	private List<String> deletedReviewers = new ArrayList();
	private List<String> addedInterimDueDates = new ArrayList();
	private HashMap<String, TeamDetails> addedTeamsMap = new HashMap();
	private HashMap<String, TeamDetails> deletedTeamsMap = new HashMap();
	private HashMap<String, TeamDetails> addedInterimsForTeamMap = new HashMap();
	private HashMap<String, TeamDetails> deletedInterimsForTeamMap = new HashMap();
	private List<SubTeamReMapVO> subTeamReMapForPullback;
	private List<Long> removedPids = new ArrayList();
	private boolean isOASaved = false;
	private boolean isBIREPresent = false;
	private String isREPresent = "false";
	private String workitemName;
	private float budget;
	private boolean isBudgetDueDateConfirmed;
	private String currency_Code;
	private String taxCode;
	private String isLegacyData;
	private int isISIS;
	private int isBulkOrder;
	private String bulkOrderId;
	private String orderGUID;
	private String recurrNumber;
	private String recurrencePattern;
	private String recurrenceRange;
	private String finalDueDays;
	private String cInterim1Days;
	private String cInterim2Days;
	private String recurrenceEndDate;
	private int noOfRecurrences;

	public Object getCaseInfoBlock() {
		return this.caseInfoBlock;
	}

	public void setCaseInfoBlock(Object caseInfoBlock) {
		this.caseInfoBlock = caseInfoBlock;
	}

	public boolean isTeamReFlag() {
		return this.teamReFlag;
	}

	public void setTeamReFlag(boolean teamReFlag) {
		this.teamReFlag = teamReFlag;
	}

	public List<String> getAddedTeamReList() {
		return this.addedTeamReList;
	}

	public void setAddedTeamReList(List<String> addedTeamReList) {
		this.addedTeamReList = addedTeamReList;
	}

	public List<String> getDeletedTeamReList() {
		return this.deletedTeamReList;
	}

	public void setDeletedTeamReList(List<String> deletedTeamReList) {
		this.deletedTeamReList = deletedTeamReList;
	}

	public boolean isPullbackFlag() {
		return this.pullbackFlag;
	}

	public void setPullbackFlag(boolean pullbackFlag) {
		this.pullbackFlag = pullbackFlag;
	}

	public List<String> getAddedSubTeamReMappings() {
		return this.addedSubTeamReMappings;
	}

	public void setAddedSubTeamReMappings(List<String> addedSubTeamReMappings) {
		this.addedSubTeamReMappings = addedSubTeamReMappings;
	}

	public List<String> getDeletedSubTeamReMappings() {
		return this.deletedSubTeamReMappings;
	}

	public void setDeletedSubTeamReMappings(List<String> deletedSubTeamReMappings) {
		this.deletedSubTeamReMappings = deletedSubTeamReMappings;
	}

	public List<String> getAddedTeams() {
		return this.addedTeams;
	}

	public void setAddedTeams(List<String> addedTeams) {
		this.addedTeams = addedTeams;
	}

	public List<String> getDeletedTeams() {
		return this.deletedTeams;
	}

	public void setDeletedTeams(List<String> deletedTeams) {
		this.deletedTeams = deletedTeams;
	}

	public List<String> getAddedAnalysts() {
		return this.addedAnalysts;
	}

	public void setAddedAnalysts(List<String> addedAnalysts) {
		this.addedAnalysts = addedAnalysts;
	}

	public List<String> getDeletedAnalysts() {
		return this.deletedAnalysts;
	}

	public void setDeletedAnalysts(List<String> deletedAnalysts) {
		this.deletedAnalysts = deletedAnalysts;
	}

	public Date getFinalRDueDate() {
		return this.finalRDueDate;
	}

	public void setFinalRDueDate(Date finalRDueDate) {
		this.finalRDueDate = finalRDueDate;
	}

	public Date getrInterim1() {
		return this.rInterim1;
	}

	public void setrInterim1(Date rInterim1) {
		this.rInterim1 = rInterim1;
	}

	public Date getrInterim2() {
		return this.rInterim2;
	}

	public void setrInterim2(Date rInterim2) {
		this.rInterim2 = rInterim2;
	}

	public String getClientCodes() {
		return this.clientCodes;
	}

	public void setClientCodes(String clientCodes) {
		this.clientCodes = clientCodes;
	}

	public boolean isCheckBizSolo() {
		return this.checkBizSolo;
	}

	public void setCheckBizSolo(boolean checkBizSolo) {
		this.checkBizSolo = checkBizSolo;
	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getPrimarySubjectName() {
		return this.primarySubjectName;
	}

	public void setPrimarySubjectName(String primarySubjectName) {
		this.primarySubjectName = primarySubjectName;
	}

	public String getPrimarySubjectID() {
		return this.primarySubjectID;
	}

	public void setPrimarySubjectID(String primarySubjectID) {
		this.primarySubjectID = primarySubjectID;
	}

	public String getPrimarySubjectCountryName() {
		return this.primarySubjectCountryName;
	}

	public void setPrimarySubjectCountryName(String primarySubjectCountryName) {
		this.primarySubjectCountryName = primarySubjectCountryName;
	}

	public String getOtherSubjectNameCountryMap() {
		return this.otherSubjectNameCountryMap;
	}

	public void setOtherSubjectNameCountryMap(String otherSubjectNameCountryMap) {
		this.otherSubjectNameCountryMap = otherSubjectNameCountryMap;
	}

	public String[] getModifiedRecords() {
		return this.ModifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.ModifiedRecords = modifiedRecords;
	}

	public boolean isReviewerFlag() {
		return this.reviewerFlag;
	}

	public void setReviewerFlag(boolean reviewerFlag) {
		this.reviewerFlag = reviewerFlag;
	}

	public boolean isReFlag() {
		return this.reFlag;
	}

	public void setReFlag(boolean reFlag) {
		this.reFlag = reFlag;
	}

	public boolean isTeamFlag() {
		return this.teamFlag;
	}

	public void setTeamFlag(boolean teamFlag) {
		this.teamFlag = teamFlag;
	}

	public boolean isAnalystFlag() {
		return this.analystFlag;
	}

	public void setAnalystFlag(boolean analystFlag) {
		this.analystFlag = analystFlag;
	}

	public boolean isSubjectFlag() {
		return this.subjectFlag;
	}

	public void setSubjectFlag(boolean subjectFlag) {
		this.subjectFlag = subjectFlag;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public List<SubjectDetails> getSubjectList() {
		return this.subjectList;
	}

	public void setSubjectList(List<SubjectDetails> subjectList) {
		this.subjectList = subjectList;
	}

	public java.util.Date getDueDate1() {
		return this.dueDate1;
	}

	public void setDueDate1(java.util.Date dueDate1) {
		this.dueDate1 = dueDate1;
	}

	public java.util.Date getDueDate2() {
		return this.dueDate2;
	}

	public void setDueDate2(java.util.Date dueDate2) {
		this.dueDate2 = dueDate2;
	}

	public Date getFinalDueDate() {
		return this.finalDueDate;
	}

	public void setFinalDueDate(Date finalDueDate) {
		this.finalDueDate = finalDueDate;
	}

	public List<TeamDetails> getTeamList() {
		return this.teamList;
	}

	public void setTeamList(List<TeamDetails> teamList) {
		this.teamList = teamList;
	}

	public String getCaseFee() {
		return this.caseFee;
	}

	public void setCaseFee(String caseFee) {
		this.caseFee = caseFee;
	}

	public String getCaseCurrency() {
		return this.caseCurrency;
	}

	public void setCaseCurrency(String caseCurrency) {
		this.caseCurrency = caseCurrency;
	}

	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public List<VendorDetailVO> getVendorList() {
		return this.vendorList;
	}

	public void setVendorList(List<VendorDetailVO> vendorList) {
		this.vendorList = vendorList;
	}

	public String getCasePriority() {
		return this.casePriority;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public boolean isExpressCase() {
		return this.isExpressCase;
	}

	public void setExpressCase(boolean isExpressCase) {
		this.isExpressCase = isExpressCase;
	}

	public int getExpressCase() {
		return this.expressCase;
	}

	public void setExpressCase(int expressCase) {
		if (expressCase == 1) {
			this.isExpressCase = true;
		}

		this.expressCase = expressCase;
	}

	public List<CommentDetails> getCommentList() {
		return this.commentList;
	}

	public void setCommentList(List<CommentDetails> commentList) {
		this.commentList = commentList;
	}

	public boolean isCapeTown() {
		return this.isCapeTown;
	}

	public void setCapeTown(boolean isCapeTown) {
		this.isCapeTown = isCapeTown;
	}

	public java.util.Date getCapeTownDate() {
		return this.capeTownDate;
	}

	public void setCapeTownDate(java.util.Date capeTownDate) {
		this.capeTownDate = capeTownDate;
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

	public String getClientRef() {
		return this.clientRef;
	}

	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}

	public String getReportTypeId() {
		return this.reportTypeId;
	}

	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSubReportTypeId() {
		return this.subReportTypeId;
	}

	public void setSubReportTypeId(String subReportTypeId) {
		this.subReportTypeId = subReportTypeId;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public String getCaseMgrId() {
		return this.caseMgrId;
	}

	public void setCaseMgrId(String caseMgrId) {
		this.caseMgrId = caseMgrId;
	}

	public String getCaseManagerName() {
		return this.caseManagerName;
	}

	public void setCaseManagerName(String caseManagerName) {
		this.caseManagerName = caseManagerName;
	}

	public int getIsRecurCase() {
		return this.isRecurCase;
	}

	public void setIsRecurCase(int isRecurCase) {
		this.isRecurCase = isRecurCase;
	}

	public int getIsAutoOfcAssign() {
		return this.isAutoOfcAssign;
	}

	public void setIsAutoOfcAssign(int isAutoOfcAssign) {
		this.isAutoOfcAssign = isAutoOfcAssign;
	}

	public String getCaseInfo() {
		if (this.getCaseInfoBlock() != null) {
			try {
				Reader reader = ((ddz) this.getCaseInfoBlock()).getCharacterStream();
				char[] arr = new char[8192];
				StringBuffer buf = new StringBuffer();

				int numChars;
				while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
					buf.append(arr, 0, numChars);
				}

				this.caseInfo = buf.toString();
			} catch (SQLException var5) {
				this.logger.error(
						"Need urgent attention.There is some SQLException while trying to get the case information block "
								+ var5);
			} catch (IOException var6) {
				this.logger.error(
						"Need urgent attention.There is some IOException while trying to get the case information block "
								+ var6);
			} catch (Exception var7) {
				this.logger.error(
						"Need urgent attention.There is some Exception while trying to get the case information block "
								+ var7);
			}
		}

		return this.caseInfo;
	}

	public void setCaseInfo(String caseInfo) {
		this.caseInfo = caseInfo;
	}

	public String getClientFeedBack() {
		return this.clientFeedBack;
	}

	public void setClientFeedBack(String clientFeedBack) {
		this.clientFeedBack = clientFeedBack;
	}

	public int getCaseStatusId() {
		return this.caseStatusId;
	}

	public void setCaseStatusId(int caseStatusId) {
		this.caseStatusId = caseStatusId;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCaseCreatorId() {
		return this.caseCreatorId;
	}

	public void setCaseCreatorId(String caseCreatorId) {
		this.caseCreatorId = caseCreatorId;
	}

	public String getCaseCreatorFullName() {
		return this.caseCreatorFullName;
	}

	public void setCaseCreatorFullName(String caseCreatorFullName) {
		this.caseCreatorFullName = caseCreatorFullName;
	}

	public int getCaseHistoryId() {
		return this.caseHistoryId;
	}

	public void setCaseHistoryId(int caseHistoryId) {
		this.caseHistoryId = caseHistoryId;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getReqRecdDate1() {
		return this.reqRecdDate1;
	}

	public void setReqRecdDate1(String reqRecdDate1) {
		this.reqRecdDate1 = reqRecdDate1;
	}

	public Date getcInterim1() {
		return this.cInterim1;
	}

	public void setcInterim1(Date cInterim1) {
		this.cInterim1 = cInterim1;
	}

	public Date getcInterim2() {
		return this.cInterim2;
	}

	public void setcInterim2(Date cInterim2) {
		this.cInterim2 = cInterim2;
	}

	public Date getClientSentDate1() {
		return this.clientSentDate1;
	}

	public void setClientSentDate1(Date clientSentDate1) {
		this.clientSentDate1 = clientSentDate1;
	}

	public Date getClientSentDate2() {
		return this.clientSentDate2;
	}

	public void setClientSentDate2(Date clientSentDate2) {
		this.clientSentDate2 = clientSentDate2;
	}

	public Date getClientFinalDate() {
		return this.clientFinalDate;
	}

	public void setClientFinalDate(Date clientFinalDate) {
		this.clientFinalDate = clientFinalDate;
	}

	public int getClientReportStatus() {
		return this.clientReportStatus;
	}

	public void setClientReportStatus(int clientReportStatus) {
		this.clientReportStatus = clientReportStatus;
	}

	public void setInterimCycleFlag(boolean interimCycleFlag) {
		this.interimCycleFlag = interimCycleFlag;
	}

	public boolean isInterimCycleFlag() {
		return this.interimCycleFlag;
	}

	public HashMap<String, TeamDetails> getAddedTeamsMap() {
		return this.addedTeamsMap;
	}

	public void setAddedTeamsMap(HashMap<String, TeamDetails> addedTeamsMap) {
		this.addedTeamsMap = addedTeamsMap;
	}

	public HashMap<String, TeamDetails> getDeletedTeamsMap() {
		return this.deletedTeamsMap;
	}

	public void setDeletedTeamsMap(HashMap<String, TeamDetails> deletedTeamsMap) {
		this.deletedTeamsMap = deletedTeamsMap;
	}

	public HashMap<String, TeamDetails> getAddedInterimsForTeamMap() {
		return this.addedInterimsForTeamMap;
	}

	public void setAddedInterimsForTeamMap(HashMap<String, TeamDetails> addedInterimsForTeamMap) {
		this.addedInterimsForTeamMap = addedInterimsForTeamMap;
	}

	public void setDueDateFlag(boolean dueDateFlag) {
		this.dueDateFlag = dueDateFlag;
	}

	public boolean isDueDateFlag() {
		return this.dueDateFlag;
	}

	public Timestamp getReqRecdDate() {
		return this.reqRecdDate;
	}

	public void setReqRecdDate(Timestamp reqRecdDate) {
		this.reqRecdDate = reqRecdDate;
	}

	public boolean getIsOASaved() {
		return this.isOASaved;
	}

	public void setIsOASaved(boolean isOASaved) {
		this.isOASaved = isOASaved;
	}

	public boolean getIsBIREPresent() {
		return this.isBIREPresent;
	}

	public void setIsBIREPresent(boolean isBIREPresent) {
		this.isBIREPresent = isBIREPresent;
	}

	public void setWorkitemName(String workitemName) {
		this.workitemName = workitemName;
	}

	public String getWorkitemName() {
		return this.workitemName;
	}

	public void setAddedInterimDueDates(List<String> addedInterimDueDates) {
		this.addedInterimDueDates = addedInterimDueDates;
	}

	public List<String> getAddedInterimDueDates() {
		return this.addedInterimDueDates;
	}

	public void setSubTeamReMapForPullback(List<SubTeamReMapVO> subTeamReMapForPullback) {
		this.subTeamReMapForPullback = subTeamReMapForPullback;
	}

	public List<SubTeamReMapVO> getSubTeamReMapForPullback() {
		return this.subTeamReMapForPullback;
	}

	public void setAddedReviewers(List<String> addedReviewers) {
		this.addedReviewers = addedReviewers;
	}

	public List<String> getAddedReviewers() {
		return this.addedReviewers;
	}

	public void setDeletedReviewers(List<String> deletedReviewers) {
		this.deletedReviewers = deletedReviewers;
	}

	public List<String> getDeletedReviewers() {
		return this.deletedReviewers;
	}

	public float getBudget() {
		return this.budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public boolean isBudgetDueDateConfirmed() {
		return this.isBudgetDueDateConfirmed;
	}

	public void setBudgetDueDateConfirmed(boolean isBudgetDueDateConfirmed) {
		this.isBudgetDueDateConfirmed = isBudgetDueDateConfirmed;
	}

	public String getCurrency_Code() {
		return this.currency_Code;
	}

	public void setCurrency_Code(String currencyCode) {
		this.currency_Code = currencyCode;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getIsLegacyData() {
		return this.isLegacyData;
	}

	public void setIsLegacyData(String isLegacyData) {
		this.isLegacyData = isLegacyData;
	}

	public void setPrimarySubjectFlag(boolean primarySubjectFlag) {
		this.primarySubjectFlag = primarySubjectFlag;
	}

	public boolean isPrimarySubjectFlag() {
		return this.primarySubjectFlag;
	}

	public int getIsISIS() {
		return this.isISIS;
	}

	public void setIsISIS(int isISIS) {
		this.isISIS = isISIS;
	}

	public int getIsBulkOrder() {
		return this.isBulkOrder;
	}

	public void setIsBulkOrder(int isBulkOrder) {
		this.isBulkOrder = isBulkOrder;
	}

	public String getBulkOrderId() {
		return this.bulkOrderId;
	}

	public void setBulkOrderId(String bulkOrderId) {
		this.bulkOrderId = bulkOrderId;
	}

	public String getOrderGUID() {
		return this.orderGUID;
	}

	public void setOrderGUID(String orderGUID) {
		this.orderGUID = orderGUID;
	}

	public void setPrimaryTeamName(String primaryTeamName) {
		this.primaryTeamName = primaryTeamName;
	}

	public String getPrimaryTeamName() {
		return this.primaryTeamName;
	}

	public String getRecurrNumber() {
		return this.recurrNumber;
	}

	public void setRecurrNumber(String recurrNumber) {
		this.recurrNumber = recurrNumber;
	}

	public String getRecurrencePattern() {
		return this.recurrencePattern;
	}

	public void setRecurrencePattern(String recurrencePattern) {
		this.recurrencePattern = recurrencePattern;
	}

	public String getRecurrenceRange() {
		return this.recurrenceRange;
	}

	public void setRecurrenceRange(String recurrenceRange) {
		this.recurrenceRange = recurrenceRange;
	}

	public String getFinalDueDays() {
		return this.finalDueDays;
	}

	public void setFinalDueDays(String finalDueDays) {
		this.finalDueDays = finalDueDays;
	}

	public String getcInterim1Days() {
		return this.cInterim1Days;
	}

	public void setcInterim1Days(String cInterim1Days) {
		this.cInterim1Days = cInterim1Days;
	}

	public String getcInterim2Days() {
		return this.cInterim2Days;
	}

	public void setcInterim2Days(String cInterim2Days) {
		this.cInterim2Days = cInterim2Days;
	}

	public String getRecurrenceEndDate() {
		return this.recurrenceEndDate;
	}

	public void setRecurrenceEndDate(String recurrenceEndDate) {
		this.recurrenceEndDate = recurrenceEndDate;
	}

	public void setRemovedPids(List<Long> removedPids) {
		this.removedPids = removedPids;
	}

	public List<Long> getRemovedPids() {
		return this.removedPids;
	}

	public void setDeletedInterimsForTeamMap(HashMap<String, TeamDetails> deletedInterimsForTeamMap) {
		this.deletedInterimsForTeamMap = deletedInterimsForTeamMap;
	}

	public HashMap<String, TeamDetails> getDeletedInterimsForTeamMap() {
		return this.deletedInterimsForTeamMap;
	}

	public void setCaseManagerFlag(boolean caseManagerFlag) {
		this.caseManagerFlag = caseManagerFlag;
	}

	public boolean isCaseManagerFlag() {
		return this.caseManagerFlag;
	}

	public void setResearchDueDateFlag(boolean researchDueDateFlag) {
		this.researchDueDateFlag = researchDueDateFlag;
	}

	public boolean isResearchDueDateFlag() {
		return this.researchDueDateFlag;
	}

	public void setResearchDueDates(String researchDueDates) {
		this.researchDueDates = researchDueDates;
	}

	public String getResearchDueDates() {
		return this.researchDueDates;
	}

	public void setMainAnalystMap(HashMap<String, String> mainAnalystMap) {
		this.mainAnalystMap = mainAnalystMap;
	}

	public HashMap<String, String> getMainAnalystMap() {
		return this.mainAnalystMap;
	}

	public void setMainAnalystFlag(boolean mainAnalystFlag) {
		this.mainAnalystFlag = mainAnalystFlag;
	}

	public boolean isMainAnalystFlag() {
		return this.mainAnalystFlag;
	}

	public String getIsisUser() {
		return this.isisUser;
	}

	public void setIsisUser(String isisUser) {
		this.isisUser = isisUser;
	}

	public void setDueDateUpdatedFlag(boolean dueDateUpdatedFlag) {
		this.dueDateUpdatedFlag = dueDateUpdatedFlag;
	}

	public boolean isDueDateUpdatedFlag() {
		return this.dueDateUpdatedFlag;
	}

	public void setUpdatedDueDatesList(List<String> updatedDueDatesList) {
		this.updatedDueDatesList = updatedDueDatesList;
	}

	public List<String> getUpdatedDueDatesList() {
		return this.updatedDueDatesList;
	}

	public String getCaseHistoryPerformer() {
		return this.caseHistoryPerformer;
	}

	public void setCaseHistoryPerformer(String caseHistoryPerformer) {
		this.caseHistoryPerformer = caseHistoryPerformer;
	}

	public void setTaskCompleted(boolean isTaskCompleted) {
		this.isTaskCompleted = isTaskCompleted;
	}

	public boolean isTaskCompleted() {
		return this.isTaskCompleted;
	}

	public int getNoOfRecurrences() {
		return this.noOfRecurrences;
	}

	public void setNoOfRecurrences(int noOfRecurrences) {
		this.noOfRecurrences = noOfRecurrences;
	}

	public void setSfdcOtnNumbers(String sfdcOtnNumbers) {
		this.sfdcOtnNumbers = sfdcOtnNumbers;
	}

	public String getSfdcOtnNumbers() {
		return this.sfdcOtnNumbers;
	}

	public void setRegionalTerritory(String regionalTerritory) {
		this.regionalTerritory = regionalTerritory;
	}

	public String getRegionalTerritory() {
		return this.regionalTerritory;
	}

	public void setSalesRepresentative(String salesRepresentative) {
		this.salesRepresentative = salesRepresentative;
	}

	public String getSalesRepresentative() {
		return this.salesRepresentative;
	}

	public void setOfficeList(List<String> officeList) {
		this.officeList = officeList;
	}

	public List<String> getOfficeList() {
		return this.officeList;
	}

	public void setSubjectREList(SubjectDetails subjectREList) {
		this.subjectREList = subjectREList;
	}

	public SubjectDetails getSubjectREList() {
		return this.subjectREList;
	}

	public void setIsREPresent(String isREPresent) {
		this.isREPresent = isREPresent;
	}

	public String getIsREPresent() {
		return this.isREPresent;
	}

	public void setReplicationOffice(List<String> replicationOffice) {
		this.replicationOffice = replicationOffice;
	}

	public List<String> getReplicationOffice() {
		return this.replicationOffice;
	}

	public void setReplicatedSubjectList(List<String> replicatedSubjectList) {
		this.replicatedSubjectList = replicatedSubjectList;
	}

	public List<String> getReplicatedSubjectList() {
		return this.replicatedSubjectList;
	}

	public void setOfficeBasedSubjectList(List<BranchOfficeMasterVO> officeBasedSubjectList) {
		this.officeBasedSubjectList = officeBasedSubjectList;
	}

	public List<BranchOfficeMasterVO> getOfficeBasedSubjectList() {
		return this.officeBasedSubjectList;
	}

	public String getIsSubjLevelSubRPTReq() {
		return this.isSubjLevelSubRPTReq;
	}

	public void setIsSubjLevelSubRPTReq(String isSubjLevelSubRPTReq) {
		this.isSubjLevelSubRPTReq = isSubjLevelSubRPTReq;
	}

	public int getclSubreportID() {
		return this.clSubreportID;
	}

	public void setclSubreportID(int clSubreportID) {
		this.clSubreportID = clSubreportID;
	}
}