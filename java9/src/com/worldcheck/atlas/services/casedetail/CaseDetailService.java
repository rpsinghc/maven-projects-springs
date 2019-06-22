package com.worldcheck.atlas.services.casedetail;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ICaseDetailManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.task.MyOnHoldCaseVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseDetailService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.casedetail.CaseDetailService");
	ICaseDetailManager caseDetailManager = null;

	public void setCaseDetailManager(ICaseDetailManager caseDetailManager) {
		this.caseDetailManager = caseDetailManager;
	}

	public boolean updateCaseStatus(String crn, String userId, String status) throws CMSException {
		return this.caseDetailManager.updateCaseStatus(crn, userId, status);
	}

	public List<MyOnHoldCaseVO> getOnHoldCase(Map<String, Object> parameterMap) throws CMSException {
		return this.caseDetailManager.getOnHoldCase(parameterMap);
	}

	public int getCountOfOnHoldCases(Map parameterMap) throws CMSException {
		return this.caseDetailManager.getCountOfOnHoldCases(parameterMap);
	}

	public CaseDetails getCaseInfo(String crn) throws CMSException {
		return this.caseDetailManager.getCaseDetails(crn);
	}

	public CaseDetails getCaseInfoForPID(String pid) throws CMSException {
		return this.caseDetailManager.getCaseInfoForPID(pid);
	}

	public CaseDetails getCaseStatus(String crn) throws CMSException {
		return this.caseDetailManager.getCaseStatus(crn);
	}

	public String getCrnForPid(long pid) throws CMSException {
		return this.caseDetailManager.getCrnForPid(pid);
	}

	public void saveCaseInformation(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap)
			throws CMSException {
		this.caseDetailManager.saveCaseInformation(caseDetails, session, dsMap);
	}

	public void saveSavvionCaseInformation(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap)
			throws CMSException {
		this.caseDetailManager.saveSavvionCaseInformation(caseDetails, session, dsMap);
	}

	public int updateCaseResearchDueDates(String crn, String rInterim1, String rInterim2, String finalDueDate,
			String userId) throws CMSException {
		return this.caseDetailManager.updateCaseResearchDueDates(crn, rInterim1, rInterim2, finalDueDate, userId);
	}

	public HashMap<String, String> getPIDsForBulkCRN(List<String> CRNList) throws CMSException {
		return this.caseDetailManager.getPIDsForBulkCRN(CRNList);
	}

	public void isisCancelCaseNotification(CaseDetails oldCaseDetails, CaseDetails newCaseDetails, String userId)
			throws CMSException {
		this.caseDetailManager.isisCancelCaseNotification(oldCaseDetails, newCaseDetails, userId);
	}

	public void getMailBody(String crn, String isISIS, String isAtlas, String isBulk) throws CMSException {
		this.caseDetailManager.getMailBody(crn, isISIS, isAtlas, isBulk);
	}

	public void saveSavvionCaseInformation(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap,
			List<RiskProfileVO> riskProfileList, List<RiskProfileVO> riskProfileListWithHBD,
			List<RiskProfileVO> subIndusList, List<RiskAggregationVO> riskAggregationList, String taskName,
			List<TotalRiskAggregationVO> totalRiskAggregationList) throws CMSException {
		this.caseDetailManager.saveSavvionCaseInformation(caseDetails, session, dsMap, riskProfileList,
				riskProfileListWithHBD, subIndusList, riskAggregationList, taskName, totalRiskAggregationList);
	}

	public long fetchProfileId(List<RiskProfileVO> riskForProfileIDList) throws CMSException {
		return this.caseDetailManager.fetchProfileId(riskForProfileIDList);
	}

	public int fetchTotalAggrId(String crn) throws CMSException {
		return this.caseDetailManager.fetchTotalAggrId(crn);
	}

	public void saveSubjectAggregation(List<SubjectLevelAggregation> subLevelAggregation) throws CMSException {
		this.caseDetailManager.saveSubjectAggregation(subLevelAggregation);
	}

	public void saveAfterCompletionSavvionCaseInformation(CaseDetails caseDetails, Session session,
			HashMap<String, Object> dsMap, List<RiskProfileVO> riskProfileList,
			List<RiskProfileVO> riskProfileListWithHBD, List<RiskProfileVO> subIndusList,
			List<RiskAggregationVO> riskAggregationList, String taskName,
			List<TotalRiskAggregationVO> totalRiskAggregationVo, List<SubjectLevelAggregation> subjectAggregationList)
			throws CMSException {
		this.caseDetailManager.saveAfterCompletionSavvionCaseInformation(caseDetails, session, dsMap, riskProfileList,
				riskProfileListWithHBD, subIndusList, riskAggregationList, taskName, totalRiskAggregationVo,
				subjectAggregationList);
	}
}