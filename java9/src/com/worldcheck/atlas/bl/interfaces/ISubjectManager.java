package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ISubjectManager {
	CaseDetails getSubjectListForCase(String var1, String var2, int var3, int var4, String var5) throws CMSException;

	CaseDetails getSubjectListForCase(String var1) throws CMSException;

	List getCaseSubjectsIndustryList(String var1, String var2, int var3, int var4, String var5) throws CMSException;

	SubjectDetails deleteSubjectsForCase(String var1, String var2, String var3, String var4, String var5, Session var6,
			String var7) throws CMSException;

	SubjectDetails getSubjectDetailForSubId(int var1) throws CMSException;

	Map getSubIndMapForCase(String var1) throws CMSException;

	List getCountryMaster() throws CMSException;

	List getREElements(String var1, String var2, String var3, String var4, String var5, String var6, String var7)
			throws CMSException;

	String reNameIDMap(String var1, Map var2) throws CMSException;

	List getAssociateCase(String var1, String var2, int var3, int var4, String var5) throws CMSException;

	String doSubjectValidation(String var1, String var2, String var3) throws CMSException;

	String doSubjectBIValidation(String var1, String var2, String var3) throws CMSException;

	int deleteAssCRNForSub(String var1, String var2) throws CMSException;

	Map getsubjectIndMap() throws CMSException;

	int updateSubjectIndustry(String[] var1, String var2) throws CMSException;

	int updateSubjectRisk(String[] var1, String var2) throws CMSException;

	List convertCommaStringToList(String var1) throws CMSException;

	SubjectDetails updateSubject(SubjectDetails var1, Session var2, String var3) throws CMSException;

	SubjectDetails getDefaultSubjectDetail(String var1) throws CMSException;

	List getREElementsForNewSubject(String var1, String var2, String var3, String var4) throws CMSException;

	List getMatchingAssociateCasesForSub(String var1, String var2, String var3, String var4, int var5, int var6,
			String var7) throws CMSException;

	List getMatchingAssociateCasesForAddSub(String var1, String var2, String var3) throws CMSException;

	SubjectDetails addNewSubject(SubjectDetails var1, Session var2, String var3) throws CMSException;

	int addAssociateCaseForSubject(String var1, String var2, String var3, String var4) throws CMSException;

	List getNotificationUsersForSubject(String var1, String var2) throws CMSException;

	boolean doReValidation(String var1, int var2) throws CMSException;

	List getCountryListForSubject() throws CMSException;

	int getSubjectCount(String var1) throws CMSException;

	CaseDetails getSubjectRiskForCase(String var1, String var2, int var3, int var4, String var5) throws CMSException;

	int getSubjectIndustryCount(String var1) throws CMSException;

	int getMatchingAssociateCasesCount(String var1, String var2, String var3) throws CMSException;

	int getAsscciateCaseCount(String var1) throws CMSException;

	int getAddEditSubjectValidation(String var1, String var2, String var3, String var4, String var5)
			throws CMSException;

	long getPIIDForCRN(String var1) throws CMSException;

	boolean checkForBI(String var1, String var2) throws CMSException;

	boolean isOfficeAssignmentDone(String var1) throws CMSException;

	boolean isBITeamExist(String var1) throws CMSException;

	Map getBiREStatus(String var1, String var2) throws CMSException;

	List getupdateSubjectTeamDeleteInfo(String var1, String var2, String var3) throws CMSException;

	String getUpdateSubjectBITeamInfo(String var1, String var2, String var3) throws CMSException;

	String listToCommaSeparatedString(List<String> var1) throws CMSException;

	int addISISSubjectToCase(SubjectDetailsVO var1, boolean var2, String var3, SubjectDetails var4) throws CMSException;

	int updateISISSubjectToCase(SubjectDetailsVO var1, String var2, List var3, SubjectDetails var4) throws CMSException;

	int deleteISISSubjectToCase(String var1, String var2, boolean var3, List var4) throws CMSException;

	List getLegacySubjectListData(String var1, String var2, int var3, int var4, String var5) throws CMSException;

	int getLegacyCaseSubjectCount(String var1) throws CMSException;

	int getLegacyPastRecordsCount(String var1, String var2) throws CMSException;

	List getLegacyPastRecords(String var1, String var2, String var3, int var4, int var5, String var6)
			throws CMSException;

	List getCaseLevelRiskCategory(String var1) throws CMSException;

	List getSubjectLevelRiskCategory(String var1) throws CMSException;

	List getSubjectLevelRiskCategory(String var1, String var2, String var3, String var4, String var5, String var6)
			throws CMSException;

	Date getUpdateOnForSubject(int var1) throws CMSException;

	boolean checkIfOfficeAssignmnetDone(String var1, Session var2) throws CMSException;

	ClientCaseStatusIndustryVO[] getSubjectsIndustryForISIS(String var1) throws CMSException;

	ClientCaseStatusRiskVO[] getSubjectsRisksForISIS(String var1) throws CMSException;

	void mergeAndInsertSubjectRisks(List<SubjectDetails> var1) throws CMSException;

	String getCaseStatus(String var1) throws CMSException;

	int addColorForSubject(String var1, String var2) throws CMSException;

	SubjectDetails getSubjectColorDetails(String var1, int var2) throws CMSException;

	int getSubjectIdCount(String var1) throws CMSException;

	int getDisAssociatedSubjectCount(int var1) throws CMSException;

	int getAssociatedSubjectCount(String var1, int var2, String var3) throws CMSException;

	int saveColorDetails(String var1, String var2, String var3, String var4) throws CMSException;

	int getCRNCount(String var1) throws CMSException;

	String isWatchListedSubject(String var1) throws CMSException;

	List<String> getWatchListedSubject() throws CMSException;

	String caseManagerId(String var1) throws CMSException;

	boolean checkForAutoSubjectAddition(SubjectDetails var1) throws CMSException;

	List<SubjectDetails> getListOfSubjectForReplication(SubjectDetails var1) throws CMSException;

	SubjectDetails getReportTypeForCase(SubjectDetails var1) throws CMSException;

	int updateSubjectBudget(String var1, String var2) throws CMSException;

	long updateCaseFee(String var1, String var2, String var3) throws CMSException;

	long updateEDDOCaseFee(String var1, String var2, boolean var3) throws CMSException;

	List<SubjectDetails> getSubjectDetailsForCRN(String var1) throws CMSException;

	SubjectDetails getSlSubreportFlag(String var1) throws CMSException;
}