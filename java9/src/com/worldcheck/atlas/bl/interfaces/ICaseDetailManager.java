package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.casedetails.CaseDetailsDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.CaseStatus;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.task.MyOnHoldCaseVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICaseDetailManager {
	void setCaseDetailsDAO(CaseDetailsDAO var1);

	CaseDetails getCaseDetails(String var1) throws CMSException;

	CaseDetails getCaseStatus(String var1) throws CMSException;

	int saveCaseInformation(CaseDetails var1, Session var2, HashMap<String, Object> var3) throws CMSException;

	List<CaseStatus> getCaseStatusMaster() throws CMSException;

	boolean updateCaseStatus(String var1, String var2, String var3) throws CMSException;

	List<MyOnHoldCaseVO> getOnHoldCase(Map<String, Object> var1) throws CMSException;

	int getCountOfOnHoldCases(Map<String, Object> var1) throws CMSException;

	CaseDetails getLegacyCaseDetails(String var1) throws CMSException;

	String getCrnForPid(long var1) throws CMSException;

	CaseDetails getRecurranceCaseDetails(String var1) throws CMSException;

	void updateRecurranceCaseDetails(CaseDetails var1) throws CMSException;

	String getCurrentCaseCycle(Session var1, String var2) throws CMSException;

	String getCurrentCaseStatus(Session var1, String var2) throws CMSException;

	int updateCaseResearchDueDates(String var1, String var2, String var3, String var4, String var5) throws CMSException;

	void saveSavvionCaseInformation(CaseDetails var1, Session var2, HashMap<String, Object> var3) throws CMSException;

	CaseDetails getCaseInfoForPID(String var1) throws CMSException;

	int updateClientFeedback(CaseDetails var1) throws CMSException;

	HashMap<String, String> getPIDsForBulkCRN(List<String> var1) throws CMSException;

	int updateCaseStatusInfo(CaseDetails var1, Session var2, HashMap<String, Object> var3) throws CMSException;

	void isisCancelCaseNotification(CaseDetails var1, CaseDetails var2, String var3) throws CMSException;

	List<CountryDatabaseMasterVO> getAllCountryMasterList() throws CMSException;

	void getMailBody(String var1, String var2, String var3, String var4) throws CMSException;

	void saveAfterCompletionSavvionCaseInformation(CaseDetails var1, Session var2, HashMap<String, Object> var3,
			List<RiskProfileVO> var4, List<RiskProfileVO> var5, List<RiskProfileVO> var6, List<RiskAggregationVO> var7,
			String var8, List<TotalRiskAggregationVO> var9, List<SubjectLevelAggregation> var10) throws CMSException;

	void saveSavvionCaseInformation(CaseDetails var1, Session var2, HashMap<String, Object> var3,
			List<RiskProfileVO> var4, List<RiskProfileVO> var5, List<RiskProfileVO> var6, List<RiskAggregationVO> var7,
			String var8, List<TotalRiskAggregationVO> var9) throws CMSException;

	long fetchProfileId(List<RiskProfileVO> var1) throws CMSException;

	int fetchTotalAggrId(String var1) throws CMSException;

	void saveSubjectAggregation(List<SubjectLevelAggregation> var1) throws CMSException;
}