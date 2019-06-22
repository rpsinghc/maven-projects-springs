package com.worldcheck.atlas.bl.integration;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.visualkey.VisualKey;
import com.worldcheck.juno.exception.JunoException;
import com.worldcheck.juno.util.AtlasJunoTriggers;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AtlasJunoIntegration {
	private static ILogProducer logger = LogProducerImpl.getLogger(AtlasJunoIntegration.class.getName());
	private static final String CURRENCY_MASTER = "CURRENCY_MASTER";

	public static List<CountryMasterVO> getCountryList() throws CMSException {
		List<CountryMasterVO> countryList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("COUNTRY_MASTER");
		return countryList;
	}

	public static String getLocalCurrencyValue(Date date, String localCurrencyCode) throws CMSException {
		return ResourceLocator.self().getCurrencyConversionService().getLocalCurrencyValue(date, localCurrencyCode);
	}

	public static List<String> getUserREList(String userId, String teamId) throws CMSException {
		List<String> res = new ArrayList();
		List<SubTeamReMapVO> reMap = ResourceLocator.self().getTeamAssignmentService().getUserAssignedRe(userId,
				teamId);
		Iterator var5 = reMap.iterator();

		while (var5.hasNext()) {
			SubTeamReMapVO subTeamReMapVO = (SubTeamReMapVO) var5.next();
			logger.debug("subTeamReMapVO.getReId()  ::::::::::::::::   " + subTeamReMapVO.getReId());
			res.add(subTeamReMapVO.getReId());
		}

		return res;
	}

	public static String getTeamDetails(String crn, long taskPid, Session session, String ptName) throws CMSException {
		String teamDetails = "";
		if (ptName != null && (ptName.equalsIgnoreCase("ResearchTask") || ptName.equalsIgnoreCase("ResearchProcess"))) {
			teamDetails = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid, "TeamTypeList",
					session);
		} else if (ptName != null && ptName.equalsIgnoreCase("BIVendorResearch")) {
			teamDetails = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid,
					"BIVendorTaskTypeList", session);
		} else if (ptName != null && ptName.equalsIgnoreCase("CaseCreation")) {
			int primaryTeamId = ResourceLocator.self().getTeamAssignmentService().getPrimaryTeamId(crn);
			logger.debug("primary team id:" + primaryTeamId);
			if (primaryTeamId != 0) {
				teamDetails = "Primary#" + primaryTeamId;
			}
		} else if (ptName != null && ptName.equalsIgnoreCase("Review")) {
			teamDetails = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid, "TeamTypeList",
					session);
		} else {
			logger.debug("Error: no condition matched while getting team type list");
		}

		return teamDetails;
	}

	public static String getTaskProcessCycle(long taskPid, Session session, String ptName) throws CMSException {
		String processCycle = "";

		try {
			if (ptName != null && ptName.equalsIgnoreCase("ResearchTask")) {
				processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid,
						"TeamCycleName", session);
			} else if (ptName != null && ptName.equalsIgnoreCase("CaseCreation")) {
				processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid, "ProcessCycle",
						session);
			} else if (ptName != null && ptName.equalsIgnoreCase("BIVendorResearch")) {
				processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid,
						"BITeamCycleName", session);
			} else if (ptName != null && ptName.equalsIgnoreCase("ResearchProcess")) {
				processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid,
						"TeamCycleName", session);
			} else if (ptName != null && ptName.equalsIgnoreCase("Review")) {
				processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(taskPid,
						"TeamCycleName", session);
			}

			logger.debug("getTaskProcessCycle   ::   " + processCycle);
			return processCycle;
		} catch (CMSException var6) {
			throw new CMSException(logger, var6);
		}
	}

	public static String getCurrentProcessCycleOfCase(String crn, Session session) throws CMSException {
		long piid = ResourceLocator.self().getSubjectService().getPIIDForCRN(crn);
		String caseStatus = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "CaseStatus",
				session);
		if ("Completed".equalsIgnoreCase(caseStatus)) {
			return "Final";
		} else {
			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					session);
			logger.debug("getCurrentProcessCycleOfCase() " + crn + "=" + processCycle);
			return processCycle;
		}
	}

	public static List<String> getTeamREList(Integer teamId, String crn) throws CMSException {
		List<TeamDetails> teamDetaiList = ResourceLocator.self().getTeamAssignmentService()
				.getTeamRes(teamId.toString());
		List<SubjectDetails> teamSubIds = ((TeamDetails) teamDetaiList.get(0)).getSubList();
		List<String> reList = new ArrayList();
		Set<String> reSet = new HashSet();
		Iterator var7 = teamSubIds.iterator();

		while (var7.hasNext()) {
			SubjectDetails subjectDetails = (SubjectDetails) var7.next();
			Iterator var9 = subjectDetails.getReList().iterator();

			while (var9.hasNext()) {
				ResearchElementMasterVO reMasterVO = (ResearchElementMasterVO) var9.next();
				reSet.add("" + reMasterVO.getrEMasterId());
			}
		}

		reList.addAll(reSet);
		logger.debug("getTeamREList()::" + reList);
		return reList;
	}

	public static String getReportType(String crn, Session session) throws CMSException {
		long piid = ResourceLocator.self().getSubjectService().getPIIDForCRN(crn);
		Object obj = ResourceLocator.self().getSBMService().getDataslotValue(piid, "ReportType", session);
		logger.debug("AtlasJunoIntegration.getReportType()" + obj);
		return (String) obj;
	}

	public static List<String> getProcessCycleListForCRN(String crn, Session session, String caseStatus)
			throws CMSException {
		List<String> processCyclesStr = null;
		if ("Completed".equalsIgnoreCase(caseStatus)) {
			processCyclesStr = new ArrayList();
			CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
			if (caseDetails.getcInterim1() != null) {
				((List) processCyclesStr).add("Interim1");
			}

			if (caseDetails.getcInterim2() != null) {
				((List) processCyclesStr).add("Interim2");
			}

			((List) processCyclesStr).add("Final");
		} else {
			long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
			logger.debug("AtlasJunoInterface.getProcessCycleListForCRN() pid is  " + pid);
			processCyclesStr = ResourceLocator.self().getSBMService().getAllProcessCycles(pid, session);
			logger.debug("pc received from atlas for CRN:" + processCyclesStr);
		}

		return (List) processCyclesStr;
	}

	public static String getFirstProcessCycle(String crn, Session session) throws CMSException {
		List<String> processCycles = getProcessCycleListForCRN(crn, session, (String) null);
		return processCycles != null ? (String) processCycles.get(0) : null;
	}

	public static Set<String> getREListForCRN(String crn) throws CMSException {
		Set<String> reSet = new HashSet();
		CaseDetails caseDetails = ResourceLocator.self().getSubjectService().getSubjectListForCase(crn);
		List<SubjectDetails> caseSubjectDetails = caseDetails.getSubjectList();
		Iterator var5 = caseSubjectDetails.iterator();

		while (var5.hasNext()) {
			SubjectDetails subjectDetails = (SubjectDetails) var5.next();
			String reIds = subjectDetails.getReIds();
			String[] reArray = reIds.split(",");
			String[] var11 = reArray;
			int var10 = reArray.length;

			for (int var9 = 0; var9 < var10; ++var9) {
				String reVal = var11[var9];
				reSet.add(reVal);
			}
		}

		return reSet;
	}

	public static List<SubjectDetails> getSubjectListForCRN(String crn) throws CMSException {
		CaseDetails caseDetails = null;

		try {
			caseDetails = ResourceLocator.self().getSubjectService().getSubjectListForCase(crn);
		} catch (Exception var3) {
			;
		}

		return caseDetails.getSubjectList();
	}

	public static String getCaseStatus(String crn, Session session) throws CMSException {
		long piid = ResourceLocator.self().getSubjectService().getPIIDForCRN(crn);
		return (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "CaseStatus", session);
	}

	public static String getUserProfile(String userId) throws CMSException {
		UserMasterVO userMasterVO = ResourceLocator.self().getUserService().getUserInfo(userId);
		return userMasterVO.getProbation();
	}

	public static List<RisksMasterVO> getAllRisks() throws CMSException {
		List<RisksMasterVO> riskMasterList = ResourceLocator.self().getCacheService().getCacheItemsList("RISK_MASTER");
		return riskMasterList;
	}

	public static List<String> getProcessCycleListForTeam(String workItemId, String teamName, Session session)
			throws CMSException {
		List<String> processCyclesStr = ResourceLocator.self().getSBMService().getAllProcessCycles(workItemId, teamName,
				session);
		logger.debug("pc received from atlas for team:" + processCyclesStr);
		return processCyclesStr;
	}

	public static VisualKey getVisualKeys(String crn)
			throws CMSException, InvocationTargetException, IllegalAccessException {
		VisualKey vk = ResourceLocator.self().getVisualKeyService().getVisualkeyForCRN(crn);
		return vk;
	}

	public static Set<String> getREListForSubject(String crn, Long subjectId) {
		HashSet reSet = new HashSet();

		try {
			logger.debug("Insideget REListForSubject ");
			CaseDetails caseDetails = ResourceLocator.self().getSubjectService().getSubjectListForCase(crn);
			List<SubjectDetails> caseSubjectDetails = caseDetails.getSubjectList();
			Iterator var6 = caseSubjectDetails.iterator();

			while (true) {
				SubjectDetails subjectDetails;
				do {
					if (!var6.hasNext()) {
						return reSet;
					}

					subjectDetails = (SubjectDetails) var6.next();
					logger.debug("subjectDetails.getSubjectId()" + subjectDetails.getSubjectId()
							+ "subjectId.intValue()" + subjectId.intValue());
				} while (subjectDetails.getSubjectId() != subjectId.intValue());

				String reIds = subjectDetails.getReIds();
				String[] reArray = reIds.split(",");
				String[] var12 = reArray;
				int var11 = reArray.length;

				for (int var10 = 0; var10 < var11; ++var10) {
					String reVal = var12[var10];
					reSet.add(reVal);
				}
			}
		} catch (Exception var13) {
			var13.printStackTrace();
			return reSet;
		}
	}

	public static List<TeamDetails> getTeamNameForCRN(String crn) throws CMSException {
		List<TeamDetails> teamList = ResourceLocator.self().getTeamAssignmentService().getTeamNamesForCRN(crn);
		logger.debug("getTeamNameForCRN List size  " + teamList);
		return teamList;
	}

	public static List<CurrencyMasterVO> getCurrencyCodeList() throws CMSException {
		List<CurrencyMasterVO> currencyList = null;
		currencyList = ResourceLocator.self().getCacheService().getCacheItemsList("CURRENCY_MASTER");
		return currencyList;
	}

	public static SubjectDetails getPrimarySubject(String crn) throws CMSException {
		SubjectDetails subjectDetails = ResourceLocator.self().getSubjectService().getPrimarySubjectDetailsForCase(crn);
		return subjectDetails;
	}

	public static int createDocAndMarkAsFinal(String userName, String[] tempPath, String pid, String processCycle)
			throws CMSException {
		String[] folderNames;
		if ("Final".equals(processCycle)) {
			folderNames = new String[]{"Final Report"};
		} else if ("Interim2".equals(processCycle)) {
			folderNames = new String[]{"Interim2 Report"};
		} else {
			folderNames = new String[]{"Interim1 Report"};
		}

		Session session = ResourceLocator.self().getSBMService().getSession(userName);
		List<String> docIdList = ResourceLocator.self().getDocService().CreateDocForJUNO(userName, tempPath, pid,
				folderNames, session);
		int version = ResourceLocator.self().getTaskService().getDocumentVersion((String) docIdList.get(0));
		logger.debug("generated document uploaded:" + tempPath + " for process cycle:" + processCycle + " version:"
				+ version);
		if ("Final".equals(processCycle)) {
			logger.debug("marking the document as final:" + tempPath);
			Map<String, String> docMap = ResourceLocator.self().getDocService().getAllDocuments(pid, session);
			ResourceLocator.self().getTaskService().resetStatusForJuno(docMap.keySet(), (String) docIdList.get(0));
		} else {
			ResourceLocator.self().getTaskService().markJuno((String) docIdList.get(0));
		}

		return version;
	}

	public static String getUserEmailId(String userId) throws CMSException {
		return ResourceLocator.self().getSBMService().getEmailId(userId);
	}

	public static boolean isPIDActive(long pid, String workStepName, Session session) throws CMSException {
		boolean pidActive;
		if (!"Consolidation Task".equals(workStepName) && !"Review Task".equals(workStepName)
				&& !"Client Submission Task".equals(workStepName)) {
			pidActive = ResourceLocator.self().getSBMService().isTaskCompleted(pid, session);
		} else {
			pidActive = ResourceLocator.self().getSBMService().isWSCompleted(pid, workStepName, session);
		}

		return !pidActive;
	}

	public static boolean isOfficeAssignmentComplete(String crn, Session session) throws CMSException {
		logger.debug("isOfficeAssignmentComplete() crn:" + crn + ", session:" + session);
		long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
		logger.debug("isOfficeAssignmentComplete() crn:" + crn + ", pid:" + pid + ", session:" + session);
		String[] completedWSNames = ResourceLocator.self().getSBMService().getCompletedWSNames(pid, session);
		logger.debug("isOfficeAssignmentComplete() crn:" + crn + ", return = " + completedWSNames.length);
		String[] var8 = completedWSNames;
		int var7 = completedWSNames.length;

		for (int var6 = 0; var6 < var7; ++var6) {
			String completedWSName = var8[var6];
			if ("Office Assignment Task".equals(completedWSName)) {
				return true;
			}
		}

		boolean isAutoOA = (Boolean) ResourceLocator.self().getSBMService().getDataslotValue(pid, "isAutoOA", session);
		if (isAutoOA) {
			logger.debug("isOfficeAssignmentComplete() crn:" + crn + ", auto-office assignment complete");
			return true;
		} else {
			return false;
		}
	}

	public static void receiveSubjectRisks(String crn, List<SubjectDetails> subjectList) throws CMSException {
		ResourceLocator.self().getSubjectService().mergeAndInsertSubjectRisks(subjectList);
	}

	public static void sendSubjectAddTrigger(String crn, SubjectDetails addedSubjectDetails) {
		try {
			AtlasJunoTriggers.sendSubjectAddTrigger(crn, addedSubjectDetails);
		} catch (JunoException var3) {
			logger.debug(var3.getMessage());
			logger.debug(var3.getStackTrace().toString());
		} catch (Exception var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		}

	}

	public static void primarySubjectChangedTrigger(String crn, SubjectDetails primarySubjectDetails) {
		try {
			AtlasJunoTriggers.primarySubjectChangedTrigger(crn, primarySubjectDetails);
		} catch (JunoException var3) {
			logger.debug(var3.getMessage());
			logger.debug(var3.getStackTrace().toString());
		} catch (Exception var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		}

	}

	public static void sendREAddedToSubjectTrigger(String crn, SubjectDetails subject, List<String> subjectREList) {
		try {
			AtlasJunoTriggers.sendREAddedToSubjectTrigger(crn, subject, subjectREList);
		} catch (JunoException var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		} catch (Exception var5) {
			logger.debug(var5.getMessage());
			logger.debug(var5.getStackTrace().toString());
		}

	}

	public static void sendREDeleteFromSubjectTrigger(String crn, SubjectDetails subject, List<String> subjectREList) {
		try {
			AtlasJunoTriggers.sendREDeleteFromSubjectTrigger(crn, subject, subjectREList);
		} catch (JunoException var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		} catch (Exception var5) {
			logger.debug(var5.getMessage());
			logger.debug(var5.getStackTrace().toString());
		}

	}

	public static void sendSubjectDeleteTrigger(String crn, SubjectDetails deletedSubjectDetails) {
		try {
			AtlasJunoTriggers.sendSubjectDeleteTrigger(crn, deletedSubjectDetails);
		} catch (JunoException var3) {
			logger.debug(var3.getMessage());
			logger.debug(var3.getStackTrace().toString());
		} catch (Exception var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		}

	}

	public static void sendProcessCycleAddedToTeamTrigger(String crn, String teamName, String cycleAdded) {
		try {
			AtlasJunoTriggers.sendProcessCycleAddedToTeamTrigger(crn, teamName, cycleAdded);
		} catch (JunoException var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		} catch (Exception var5) {
			logger.debug(var5.getMessage());
			logger.debug(var5.getStackTrace().toString());
		}

	}

	public static void sendCasePullbackTrigger(String crn, List<Long> pIdList, boolean isPullback) {
		try {
			AtlasJunoTriggers.sendCasePullbackTrigger(crn, pIdList, isPullback);
		} catch (JunoException var4) {
			logger.debug(var4.getMessage());
			logger.debug(var4.getStackTrace().toString());
		} catch (Exception var5) {
			logger.debug(var5.getMessage());
			logger.debug(var5.getStackTrace().toString());
		}

	}

	public int markJuno(String docId) throws CMSException {
		return ResourceLocator.self().getTaskService().markJuno(docId);
	}
}