package com.worldcheck.atlas.services.subject;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ISubjectManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubjectService {
	private ISubjectManager subjectManager;
	private String loggerClass = "com.worldcheck.atlas.services.subject.ISubjectService";
	private ILogProducer logger;

	public SubjectService() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
	}

	public void setSubjectManager(ISubjectManager subjectManager) {
		this.subjectManager = subjectManager;
	}

	public int saveSubjectIndustry(String[] modifiedRecords, String currentUser) throws CMSException {
		this.logger.debug("inside saveSubjectIndustry method of SubjectService class");
		int updateSubjectIndustryTotalCount = this.subjectManager.updateSubjectIndustry(modifiedRecords, currentUser);
		return updateSubjectIndustryTotalCount;
	}

	public int saveSubjectRisk(String[] modifiedRecords, String currentUser) throws CMSException {
		this.logger.debug("inside saveSubjectRisk method of SubjectService class");
		int totalSubjectsRiskUpdate = this.subjectManager.updateSubjectRisk(modifiedRecords, currentUser);
		return totalSubjectsRiskUpdate;
	}

	public SubjectDetails editSubjectForCase(String subjectID) throws CMSException {
		this.logger.debug("inside editSubjectForCase method of SubjectService class");
		SubjectDetails subjectDetailVO = this.subjectManager.getSubjectDetailForSubId(Integer.parseInt(subjectID));
		return subjectDetailVO;
	}

	public SubjectDetails addSubjectForCase(String crn) throws CMSException {
		this.logger.debug("inside addSubjectForCase method of SubjectService class");
		SubjectDetails primarysubDetailVO = this.subjectManager.getDefaultSubjectDetail(crn);
		return primarysubDetailVO;
	}

	public SubjectDetails getPrimarySubjectDetailsForCase(String crn) throws CMSException {
		this.logger.debug("inside addSubjectForCase method of SubjectService class");
		SubjectDetails primarysubDetailVO = this.subjectManager.getDefaultSubjectDetail(crn);
		return primarysubDetailVO;
	}

	public SubjectDetails saveSubjectForCase(SubjectDetails subjectDetails, Session session,
			String caseHistoryPerformer) throws CMSException {
		this.logger.debug("inside saveSubjectForCase method of SubjectService class");
		SubjectDetails updateSubjectDetials = this.subjectManager.updateSubject(subjectDetails, session,
				caseHistoryPerformer);
		return updateSubjectDetials;
	}

	public SubjectDetails saveNewSubjectForCase(SubjectDetails subjectDetails, Session session,
			String caseHistoryPerformer) throws CMSException {
		this.logger.debug("inside saveNewSubjectForCase method of SubjectService class");
		SubjectDetails addSubjectDetails = this.subjectManager.addNewSubject(subjectDetails, session,
				caseHistoryPerformer);
		return addSubjectDetails;
	}

	public CaseDetails getSubjectListForCase(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("inside getSubjectListForCase method of SubjectService class");
		CaseDetails caseDetails = this.subjectManager.getSubjectListForCase(crn, sortColumnName, start, limit,
				sortType);
		return caseDetails;
	}

	public SubjectDetails getSubjectColorDetails(String crn, int subjectId) throws CMSException {
		this.logger.debug("inside getSubjectListForCase method of SubjectService class");
		SubjectDetails subjectColor = this.subjectManager.getSubjectColorDetails(crn, subjectId);
		return subjectColor;
	}

	public CaseDetails getSubjectListForCase(String crn) throws CMSException {
		this.logger.debug("inside getSubjectListForCase method of SubjectService class");
		CaseDetails caseDetails = this.subjectManager.getSubjectListForCase(crn);
		return caseDetails;
	}

	public List getAssociateCasesForSub(String subjectID, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("inside getAssociateCasesForSub method of SubjectService class");
		List<String> assCaseList = this.subjectManager.getAssociateCase(subjectID, sortColumnName, start, limit,
				sortType);
		return assCaseList;
	}

	public int deleteAssCRN(String crnString, String subjectID) throws CMSException {
		this.logger.debug("inside deleteAssCRN method of SubjectService class");
		int deleteAssociateCRNCount = this.subjectManager.deleteAssCRNForSub(crnString, subjectID);
		return deleteAssociateCRNCount;
	}

	public String doSubjectDeletionValidation(String crn, String subNameString, String subIDString)
			throws CMSException {
		this.logger.debug("inside doSubjectDeletionValidation method of SubjectService class");
		String alertMessage = this.subjectManager.doSubjectValidation(crn, subNameString, subIDString);
		return alertMessage;
	}

	public SubjectDetails confirmDeleteSubject(String subjectIDString, String subNameString, String crn,
			String currentUser, String isBIDelete, Session session, String caseHistoryPerformer) throws CMSException {
		this.logger.debug("inside confirmDeleteSubject method of SubjectService class");
		SubjectDetails deleteSubjectDetails = this.subjectManager.deleteSubjectsForCase(subjectIDString, subNameString,
				crn, currentUser, isBIDelete, session, caseHistoryPerformer);
		return deleteSubjectDetails;
	}

	public List getResearchElements(String entity, String subjectEntity, String crn, String subjectID,
			String isSubjLevelSubRptReq, String formAction, String subReportTypeId) throws CMSException {
		this.logger.debug("inside getResearchElements method of SubjectService class");
		List reList = this.subjectManager.getREElements(entity, subjectEntity, crn, subjectID, isSubjLevelSubRptReq,
				formAction, subReportTypeId);
		return reList;
	}

	public List getResearchElementsForNewSubject(String subjectEntity, String crn, String subReportTypeId,
			String isSubjLevelSubRptReq) throws CMSException {
		this.logger.debug("inside getResearchElementsForNewSubject method of SubjectService class");
		List reList = this.subjectManager.getREElementsForNewSubject(subjectEntity, crn, subReportTypeId,
				isSubjLevelSubRptReq);
		return reList;
	}

	public List getCaseSubjectsIndustryList(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("inside getCaseSubjectsIndustryList method of SubjectService class");
		List caseSubjectIndustryList = this.subjectManager.getCaseSubjectsIndustryList(crn, sortColumnName, start,
				limit, sortType);
		return caseSubjectIndustryList;
	}

	public List getMatchingAssociateCasesForSub(String subjectName, String subjectID, String crn, String sortColumnName,
			int start, int limit, String sortType) throws CMSException {
		this.logger.debug("inside getMatchingAssociateCasesForSub method of SubjectService class");
		List<CaseDetails> matchingSubjectList = this.subjectManager.getMatchingAssociateCasesForSub(subjectName,
				subjectID, crn, sortColumnName, start, limit, sortType);
		return matchingSubjectList;
	}

	public List getMatchingAssociateCasesForAddSub(String subjectName, String subjectID, String crn)
			throws CMSException {
		this.logger.debug("inside getMatchingAssociateCasesForSub method of SubjectService class");
		List<CaseDetails> matchingSubjectList = this.subjectManager.getMatchingAssociateCasesForAddSub(subjectName,
				subjectID, crn);
		return matchingSubjectList;
	}

	public int addAssociateCaseForSubject(String subjectID, String crn, String currentUser, String isLegacy)
			throws CMSException {
		this.logger.debug("inside addAssociateCaseForSubject method of SubjectService class");
		int totalrecordInserted = this.subjectManager.addAssociateCaseForSubject(subjectID, crn, currentUser, isLegacy);
		return totalrecordInserted;
	}

	public int addColorForSubject(String subID, String crn) throws CMSException {
		this.logger.debug("inside addColorForSubject method of SubjectService class");
		int flag = this.subjectManager.addColorForSubject(subID, crn);
		return flag;
	}

	public List getCountryListForSubject() throws CMSException {
		this.logger.debug("inside getCountryListForSubject method of SubjectService class");
		new ArrayList();
		List countryList = this.subjectManager.getCountryListForSubject();
		return countryList;
	}

	public int getSubjectCount(String crn) throws CMSException {
		this.logger.debug("inside getSubjectCount method of SubjectService class");
		int count = this.subjectManager.getSubjectCount(crn);
		return count;
	}

	public int getSubjectIdCount(String subjectId) throws CMSException {
		this.logger.debug("inside getSubjectIdCount method of SubjectService class");
		int count = this.subjectManager.getSubjectIdCount(subjectId);
		return count;
	}

	public int getCRNCount(String crn) throws CMSException {
		this.logger.debug("inside getCRNCount method of SubjectService class");
		int count = this.subjectManager.getCRNCount(crn);
		return count;
	}

	public int getDisAssociatedSubjectCount(int subjectId) throws CMSException {
		this.logger.debug("inside getDisAssociatedSubjectCount method of SubjectService class");
		int count = this.subjectManager.getDisAssociatedSubjectCount(subjectId);
		return count;
	}

	public int getAssociatedSubjectCount(String subjectName, int subjectID, String crn) throws CMSException {
		this.logger.debug("inside  method of SubjectService class");
		int count = this.subjectManager.getAssociatedSubjectCount(subjectName, subjectID, crn);
		return count;
	}

	public CaseDetails getSubjectRiskForCase(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("inside getSubjectRiskForCase method of SubjectService class");
		CaseDetails caseDetails = this.subjectManager.getSubjectRiskForCase(crn, sortColumnName, start, limit,
				sortType);
		return caseDetails;
	}

	public int getSubjectIndustryCount(String crn) throws CMSException {
		this.logger.debug("inside getSubjectIndustryCount method of SubjectService class");
		int subjectIndustryCount = this.subjectManager.getSubjectIndustryCount(crn);
		return subjectIndustryCount;
	}

	public int getMatchingAssociateCasesCount(String subjectName, String subjectID, String crn) throws CMSException {
		this.logger.debug("inside getMatchingAssociateCasesCount method of SubjectService class");
		int matchingAssociateCasesCount = this.subjectManager.getMatchingAssociateCasesCount(subjectName, subjectID,
				crn);
		return matchingAssociateCasesCount;
	}

	public int getAsscciateCaseCount(String subjectID) throws CMSException {
		this.logger.debug("inside getAsscciateCaseCount method of SubjectService class");
		int asscciateCaseCount = this.subjectManager.getAsscciateCaseCount(subjectID);
		return asscciateCaseCount;
	}

	public int getAddEditSubjectValidation(String crn, String subjectName, String countryName, String subjectID,
			String entityType) throws CMSException {
		this.logger.debug("inside getAddEditSubjectValidation method of SubjectService class");
		int validationCount = this.subjectManager.getAddEditSubjectValidation(crn, subjectName, countryName, subjectID,
				entityType);
		return validationCount;
	}

	public boolean doReValidation(String reIDString, int subjectId) throws CMSException {
		this.logger.debug("inside doReValidation method of SubjectService class");
		boolean reUpdateFlag = this.subjectManager.doReValidation(reIDString, subjectId);
		return reUpdateFlag;
	}

	public SubjectDetails getPrimarySubjectDetail(String crn) throws CMSException {
		this.logger.debug("inside getPrimarySubjectDetail method of SubjectService class");
		SubjectDetails subjectDetails = this.subjectManager.getDefaultSubjectDetail(crn);
		return subjectDetails;
	}

	public long getPIIDForCRN(String crn) throws CMSException {
		this.logger.debug("inside getPIIDForCRN method of SubjectService class");
		long piid = this.subjectManager.getPIIDForCRN(crn);
		return piid;
	}

	public boolean checkForBI(String crn, String reString) throws CMSException {
		this.logger.debug("inside checkForBI method of SubjectService class");
		boolean result = this.subjectManager.checkForBI(crn, reString);
		return result;
	}

	public List getupdateSubjectTeamDeleteInfo(String crn, String subjectID, String newReString) throws CMSException {
		List alertMess = this.subjectManager.getupdateSubjectTeamDeleteInfo(crn, subjectID, newReString);
		return alertMess;
	}

	public String getUpdateSubjectBITeamInfo(String crn, String subjectID, String newReString) throws CMSException {
		String updateBIString = this.subjectManager.getUpdateSubjectBITeamInfo(crn, subjectID, newReString);
		return updateBIString;
	}

	public String doSubjectBIValidation(String crn, String subName, String subID) throws CMSException {
		String teamType = this.subjectManager.doSubjectBIValidation(crn, subName, subID);
		return teamType;
	}

	public int addISISSubjectToCase(SubjectDetailsVO subjectDetailsVO, boolean biFlag, String biManager,
			SubjectDetails subDetails) throws CMSException {
		int subjectId = this.subjectManager.addISISSubjectToCase(subjectDetailsVO, biFlag, biManager, subDetails);
		return subjectId;
	}

	public int updateISISSubjectToCase(SubjectDetailsVO subjectDetailsVO, String biStatus, List teamList,
			SubjectDetails subDetails) throws CMSException {
		int updateSubjectCount = this.subjectManager.updateISISSubjectToCase(subjectDetailsVO, biStatus, teamList,
				subDetails);
		return updateSubjectCount;
	}

	public int deleteISISSubjectToCase(String isisSubjectId, String crn, boolean biTeamDeleteFlag, List teamList)
			throws CMSException {
		int deleteResultCount = this.subjectManager.deleteISISSubjectToCase(isisSubjectId, crn, biTeamDeleteFlag,
				teamList);
		return deleteResultCount;
	}

	public List getLegacyCaseSubjectDetails(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		return this.subjectManager.getLegacySubjectListData(crn, sortColumnName, start, limit, sortType);
	}

	public int getLegacyCaseSubjectCount(String crn) throws CMSException {
		return this.subjectManager.getLegacyCaseSubjectCount(crn);
	}

	public int getLegacyPastRecordsCount(String crn, String subjectId) throws CMSException {
		return this.subjectManager.getLegacyPastRecordsCount(crn, subjectId);
	}

	public List getLegacyPastRecords(String crn, String subjectId, String sortColumnName, int start, int limit,
			String sortType) throws CMSException {
		return this.subjectManager.getLegacyPastRecords(crn, subjectId, sortColumnName, start, limit, sortType);
	}

	public List getSubjectLevelRiskCategory(String crn) throws CMSException {
		return this.subjectManager.getSubjectLevelRiskCategory(crn);
	}

	public List getSubjectLevelRiskCategory(String crn, String performer, String teamTypeId, String taskName,
			String teamName, String teamId) throws CMSException {
		return this.subjectManager.getSubjectLevelRiskCategory(crn, performer, teamTypeId, taskName, teamName, teamId);
	}

	public List getCaseLevelRiskCategory(String crn) throws CMSException {
		return this.subjectManager.getCaseLevelRiskCategory(crn);
	}

	public Date getUpdateOnForSubject(int subjectId) throws CMSException {
		return this.subjectManager.getUpdateOnForSubject(subjectId);
	}

	public boolean checkIfOfficeAssignmnetDone(String crn, Session session) throws CMSException {
		this.logger.debug("Inside service class...");
		return this.subjectManager.checkIfOfficeAssignmnetDone(crn, session);
	}

	public ClientCaseStatusIndustryVO[] getSubjectsIndustryForISIS(String crn) throws CMSException {
		return this.subjectManager.getSubjectsIndustryForISIS(crn);
	}

	public ClientCaseStatusRiskVO[] getSubjectsRisksForISIS(String crn) throws CMSException {
		return this.subjectManager.getSubjectsRisksForISIS(crn);
	}

	public void mergeAndInsertSubjectRisks(List<SubjectDetails> subjectDetailsList) throws CMSException {
		this.subjectManager.mergeAndInsertSubjectRisks(subjectDetailsList);
	}

	public String getCaseStatus(String crn) throws CMSException {
		return this.subjectManager.getCaseStatus(crn);
	}

	public int saveColorDetails(String taskName, String crn, String color, String taskStatus) throws CMSException {
		return this.subjectManager.saveColorDetails(taskName, crn, color, taskStatus);
	}

	public String isWatchListedSubject(String subjectName) throws CMSException {
		return this.subjectManager.isWatchListedSubject(subjectName);
	}

	public List<String> getWatchListedSubject() throws CMSException {
		return this.subjectManager.getWatchListedSubject();
	}

	public String caseManagerId(String crn) throws CMSException {
		return this.subjectManager.caseManagerId(crn);
	}

	public boolean checkForAutoSubjectAddition(SubjectDetails subDetails) throws CMSException {
		return this.subjectManager.checkForAutoSubjectAddition(subDetails);
	}

	public List<SubjectDetails> getListOfSubjectForReplication(SubjectDetails subDetails) throws CMSException {
		return this.subjectManager.getListOfSubjectForReplication(subDetails);
	}

	public SubjectDetails getReportTypeForCase(SubjectDetails subDetails) throws CMSException {
		return this.subjectManager.getReportTypeForCase(subDetails);
	}

	public int updateSubjectBudget(String gridData, String crn) throws CMSException {
		return this.subjectManager.updateSubjectBudget(gridData, crn);
	}

	public long updateCaseFee(String crn, String caseFee) throws CMSException {
		String isDeleteFlag = "false";
		return this.subjectManager.updateCaseFee(crn, caseFee, isDeleteFlag);
	}

	public long updateEDDOCaseFee(String crn, String casefee, boolean isBudgetDueDateConfirmed) throws CMSException {
		return this.subjectManager.updateEDDOCaseFee(crn, casefee, isBudgetDueDateConfirmed);
	}

	public List<SubjectDetails> getSubjectDetailsForCRN(String crn) throws CMSException {
		return this.subjectManager.getSubjectDetailsForCRN(crn);
	}

	public SubjectDetails getSlSubreportFlag(String crn) throws CMSException {
		this.logger.debug("Inside getSlSubreportFlag");
		SubjectDetails subjectDetails = this.subjectManager.getSlSubreportFlag(crn);
		this.logger.debug("Service Subject Level Subreport Flag:::::" + subjectDetails.getIsSubjLevelSubRptReq());
		return subjectDetails;
	}
}