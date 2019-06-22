package com.worldcheck.atlas.isis.dao;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.dao.AtlasWebServiceDAO.1;
import com.worldcheck.atlas.isis.util.WebServicePropertyReaderUtil;
import com.worldcheck.atlas.isis.vo.CaseFileDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class AtlasWebServiceDAO extends SqlMapClientTemplate {
	private static String CHECK_DATABASE_CONNECTIVITY = "AtlasWebService.checkDatabaseConnectivity";
	private static String CHECK_ORDER_EXISTENCE = "AtlasWebService.checkExistenceforOrder";
	private static String GET_SUBJECT_LIST_AEDDO_CASE = "AtlasWebService.getSubjectListforAEDDOCase";
	private static String GET_PID_LIST_SAVVION = "AtlasWebService.getPIDListfromSavvionforCRN";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.dao.AtlasWebServiceDAO");
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public String getOfficeNameFromOfficeId(int officeId) throws CMSException {
		this.logger.debug("Inside getOfficeNameFromOfficeId method of AtlasWebServiceDAO class.");
		String officeName = "";

		try {
			officeName = (String) this.queryForObject("AtlasWebService.getOfficeName", new Integer(officeId));
			this.logger.debug("office name is ::::::" + officeName);
			return officeName;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getReportNameFromReportCode(String reportCode) throws CMSException {
		this.logger.debug("Inside getReportNameFromReportCode method of AtlasWebServiceDAO class.");
		String reportName = "";

		try {
			reportName = (String) this.queryForObject("AtlasWebService.getReportName", reportCode);
			this.logger.debug("report name is ::::::" + reportName);
			return reportName;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getClientNameFromClientCode(String clientCode) throws CMSException {
		this.logger.debug("Inside getClientNameFromClientCode method of AtlasWebServiceDAO class.");
		String clientName = null;

		try {
			clientName = (String) this.queryForObject("AtlasWebService.getClientName", clientCode);
			this.logger.debug("client name is ::::::" + clientName);
			return clientName;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public BranchOfficeMasterVO getofficeIdForRH(String loginId) throws CMSException {
		this.logger.debug("Inside getofficeIdForRH method of AtlasWebServiceDAO class.");
		BranchOfficeMasterVO branchOfficeMasterVO = null;

		try {
			branchOfficeMasterVO = (BranchOfficeMasterVO) this.queryForObject("AtlasWebService.getofficeDetailsForRH",
					loginId);
			this.logger.debug("branch offic eis:::::" + branchOfficeMasterVO.getBranchOffice());
			return branchOfficeMasterVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public BranchOfficeMasterVO getofficeIdForCaseManager(String loginId) throws CMSException {
		this.logger.debug("Inside getofficeIdForCaseManager method of AtlasWebServiceDAO class.");
		BranchOfficeMasterVO branchOfficeMasterVO = null;

		try {
			branchOfficeMasterVO = (BranchOfficeMasterVO) this.queryForObject("AtlasWebService.getofficeDetailsForCM",
					loginId);
			this.logger.debug("branch offic eis:::::" + branchOfficeMasterVO.getBranchOffice());
			return branchOfficeMasterVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int validateCM(String caseManager) throws CMSException {
		this.logger.debug("Inside validateCM method of AtlasWebServiceDAO class.");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("AtlasWebService.getCMValidation", caseManager);
			this.logger.debug("caseManager count value is :::::" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int validateRH(String rh) throws CMSException {
		this.logger.debug("Inside validateRH method of AtlasWebServiceDAO class.");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("AtlasWebService.getRHValidation", rh);
			this.logger.debug(" validateRH count value is :::::" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean validateCRN(String guId) throws CMSException {
		this.logger.debug("Inside validateCRN method of AtlasWebServiceDAO class.");
		String crn = null;
		boolean flag = true;

		try {
			Object obj = this.queryForObject("AtlasWebService.crnValidation", guId);
			if (obj != null) {
				flag = false;
			}

			return flag;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int deleteFromClientCase(String crn) throws CMSException {
		this.logger.debug("Inside deleteFromClientCase method of AtlasWebServiceDAO class.");
		this.logger.debug("crn value is :::::" + crn);
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("AtlasWebService.deleteCRN", crn);
			this.logger.debug("count value is :::::" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void insertToCMSFTPFailFiles(LinkedList filesList, String crn) throws CMSException {
		this.logger.debug("Inside insertToCMSFTPFailFiles method of AtlasWebServiceDAO class.");
		boolean var3 = false;

		try {
			Iterator iterator = filesList.iterator();

			while (iterator.hasNext()) {
				CaseFileDetailsVO caseFileDetailsVO = (CaseFileDetailsVO) iterator.next();
				Map map = new HashMap();
				map.put("crn", crn);
				map.put("fileName", caseFileDetailsVO.getFileName());
				map.put("path", caseFileDetailsVO.getPath());
				map.put("updatedBy", this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
				this.logger.debug("map for failed files is::::" + map);
				this.insert("AtlasWebService.insertToCMSFTPFailFiles", map);
			}

		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public String getDocId(long pid, String fileName, long version) throws CMSException {
		this.logger.debug("Inside getDocId method of AtlasWebServiceDAO class.");
		String docId = "";

		try {
			Map map = new HashMap();
			map.put("pid", pid);
			map.put("fileName", fileName);
			map.put("version", version);
			docId = (String) this.queryForObject("AtlasWebService.getDocId", map);
			return docId;
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public String getCaseManagerIdForCase(String crn) throws CMSException {
		this.logger.debug("Inside getCaseManagerIdForCase method of AtlasWebServiceDAO class.");
		String caseManagerId = "";

		try {
			caseManagerId = (String) this.queryForObject("AtlasWebService.getcaseManagerId", crn);
			return caseManagerId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getSubReportsForReport(String reportTypeCode) throws CMSException {
		this.logger.debug("Inside getSubReportsForReport method of AtlasWebServiceDAO class.");
		new ArrayList();

		try {
			List<String> subReportList = this.queryForList("AtlasWebService.getSubReportType", reportTypeCode);
			return subReportList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getPrimarySubjectISISId(String crn) throws CMSException {
		this.logger.debug("Inside getPrimasrSubjectISISId method of AtlasWebServiceDAO class.");
		String isisSubejctId = "";

		try {
			Map primarySubMap = new HashMap();
			primarySubMap.put("crn", crn);
			primarySubMap.put("primarySubFlag", 1);
			isisSubejctId = (String) this.queryForObject("AtlasWebService.getISISPrimarySubjectId", primarySubMap);
			return isisSubejctId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCRNInQueue(String crn) throws CMSException {
		this.logger.debug("Inside getCRNInQueue method of AtlasWebServiceDAO class.");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("AtlasWebService.getCRNInQueue", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("count is:::::" + count);
		return count;
	}

	public void deleteDataForRollBack(String crn) throws CMSException {
		this.logger.debug("Inside deleteDataForRollBack method of AtlasWebServiceDAO class.");

		try {
			this.delete("AtlasWebService.deleteSubTeamREMap", crn);
			this.delete("AtlasWebService.deleteTeamDetails", crn);
			this.delete("AtlasWebService.deleteAccounts", crn);
			this.delete("AtlasWebService.deleteNotifications", crn);
			List<Long> caseHistoryIDList = this.queryForList("AtlasWebService.getCaseHistory", crn);
			List<Long> subjectIDList = this.queryForList("AtlasWebService.getSubjectIdList", crn);
			this.logger.debug("caseHistoryIDList::" + caseHistoryIDList);
			this.logger.debug("subjectIDList::" + subjectIDList);
			if (caseHistoryIDList != null && caseHistoryIDList.size() > 0) {
				this.delete("AtlasWebService.deleteCaseHistoryDetails", caseHistoryIDList);
				this.delete("AtlasWebService.deleteCaseHistory", crn);
			}

			if (subjectIDList != null && subjectIDList.size() > 0) {
				this.delete("AtlasWebService.deleteRiskProfileCntryBreakDown", subjectIDList);
			}

			this.delete("AtlasWebService.deleteRiskEachSubjectAggr", crn);
			this.delete("AtlasWebService.deleteRiskAggregation", crn);
			this.delete("AtlasWebService.deleteRiskTotalAggregation", crn);
			this.delete("AtlasWebService.deleteRiskProfile", crn);
			this.delete("AtlasWebService.deleteSubject", crn);
			this.delete("AtlasWebService.deleteCase", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateCRN(int year) throws CMSException {
		this.logger.debug("Inside deleteDataForRollBack method of AtlasWebServiceDAO class.");

		try {
			this.update("AtlasWebService.updateSEQDetails", year);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List getBIRes(int entityType) throws CMSException {
		this.logger.debug("Inside getBIRes Method of AtlasWebServiceDAO class");
		new ArrayList();

		try {
			List biReList = this.queryForList("AtlasWebService.getBIRes", entityType);
			return biReList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean checkCRNInDB(String crn) throws CMSException {
		this.logger.debug("Inside checkCRNInDB Method of AtlasWebServiceDAO class");
		boolean crnFlag = true;

		try {
			int count = (Integer) this.queryForObject("AtlasWebService.getCRNCount", crn);
			if (count == 0) {
				crnFlag = false;
			}
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("crn Flag is:::::::::::::::" + crnFlag);
		return crnFlag;
	}

	public Map getReBiTeamMap() throws CMSException {
		this.logger.debug("Inside getReBiTeamMap Method of AtlasWebServiceDAO class");
		new ArrayList();
		HashMap reMap = new HashMap();

		try {
			List reList = this.queryForList("AtlasWebService.reBITeamMap");
			Iterator iterator = reList.iterator();

			while (iterator.hasNext()) {
				ResearchElementMasterVO object = (ResearchElementMasterVO) iterator.next();
				reMap.put(object.getResearchElementcode(), object.getBiTeam());
			}

			return reMap;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getSubjectValidationForTeam(String crn, int subjectID) throws CMSException {
		this.logger.debug("Inside getSubjectValidationForTeam Method of AtlasWebServiceDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("AtlasWebService.subjectValidation", map);
			this.logger.debug("getSubjectValidationForTeam teamList size::::::" + teamList.size());
			return teamList;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getSubjectValidationForBITeam(String crn, int subjectID) throws CMSException {
		this.logger.debug("Inside getSubjectValidationForBITeam Method of AtlasWebServiceDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("AtlasWebService.subjectValidationForBI", map);
			return teamList;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getUpdateSubjectREValidationForTeam(String crn, int subjectID, String reRemovedListString)
			throws CMSException {
		this.logger.debug("Inside getSubjectValidationForTeam Method of AtlasWebServiceDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			map.put("re_Ids", reRemovedListString);
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("AtlasWebService.updateSubjectREValidation", map);
			return teamList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List getUpdateSubjectBIREValidationForTeam(String crn, int subjectID, String reRemovedListString)
			throws CMSException {
		this.logger.debug("Inside getSubjectValidationForTeam Method of AtlasWebServiceDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			map.put("re_Ids", reRemovedListString);
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("AtlasWebService.updateSubjectBIREValidation", map);
			return teamList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public String getReIdsForSubject(int subjectId) throws CMSException {
		this.logger.debug("Inside getReIdsForSubject Method of AtlasWebServiceDAO class..." + subjectId);
		String reIds = "";

		try {
			reIds = (String) this.queryForObject("AtlasWebService.getResForISISSubject", subjectId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("reIds is:::" + reIds);
		return reIds;
	}

	public boolean checkISISSubjectExist(String isisSubjectId, String crn) throws CMSException {
		this.logger.debug("Inside checkISISSubjectExist Method of AtlasWebServiceDAO class");
		int subjectCount = false;
		boolean resultFlag = false;

		try {
			Map subjectMap = new HashMap();
			subjectMap.put("crn", crn);
			subjectMap.put("isisSubjectId", isisSubjectId);
			int subjectCount = (Integer) this.queryForObject("AtlasWebService.checkForISISSubjectId", subjectMap);
			if (subjectCount > 0) {
				resultFlag = true;
			} else {
				resultFlag = false;
			}
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("resultFlag is:::" + resultFlag);
		return resultFlag;
	}

	public int getAtlasSubIdFromISISSubId(String isisSubjectId, String crn) throws CMSException {
		this.logger.debug("Inside getAtlasSubIdFromISISSubId Method of AtlasWebServiceDAO class");
		boolean var3 = false;

		int atlasSubId;
		try {
			Map subjectMap = new HashMap();
			subjectMap.put("crn", crn);
			subjectMap.put("isisSubjectId", isisSubjectId);
			atlasSubId = (Integer) this.queryForObject("AtlasWebService.getAtlasSubIdFromIsisSubId", subjectMap);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("atlasSubId is:::" + atlasSubId);
		return atlasSubId;
	}

	public boolean getDeleteSubjectPrimaryTeamValidation(String crn, int subjectID) throws CMSException {
		this.logger.debug("Inside getDeleteSubjectPrimaryTeamValidation Method of AtlasWebServiceDAO class");
		boolean resultFlag = false;
		new ArrayList();

		try {
			Map dataMap = new HashMap();
			dataMap.put("crn", crn);
			dataMap.put("subjectID", subjectID);
			List teamList = this.queryForList("AtlasWebService.deleteSubjectPrimaryTeamCheck", dataMap);
			if (teamList != null && teamList.size() > 0) {
				resultFlag = true;
			}
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("resultFlag is:::" + resultFlag);
		return resultFlag;
	}

	public boolean getUpdateSubjectPrimaryTeamValidation(String crn, int subjectID, String re_Ids) throws CMSException {
		this.logger.debug("Inside getUpdateSubjectPrimaryTeamValidation Method of AtlasWebServiceDAO class");
		boolean resultFlag = false;
		new ArrayList();

		try {
			Map dataMap = new HashMap();
			dataMap.put("crn", crn);
			dataMap.put("subjectID", subjectID);
			dataMap.put("re_Ids", re_Ids);
			this.logger.debug("dataMap is:::::::::::" + dataMap);
			List teamList = this.queryForList("AtlasWebService.updateSubjectPrimaryTeamCheck", dataMap);
			if (teamList != null && teamList.size() > 0) {
				resultFlag = true;
			}
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("resultFlag is:::" + resultFlag);
		return resultFlag;
	}

	public List getPrimarySubjectDetailsForISISCase(String crn) throws CMSException {
		new ArrayList();

		try {
			List subjectList = this.queryForList("AtlasWebService.primarySubjectDetails", crn);
			return subjectList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int insertAccountDetails(AccountsVO accountsVO) throws CMSException {
		boolean var2 = false;

		try {
			int added = (Integer) this.insert("AtlasWebService.insertAccountDetails", accountsVO);
			return added;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateAccountDetails(AccountsVO accountsVO) throws CMSException {
		boolean var2 = false;

		try {
			int flag = Integer.valueOf(this.update("AtlasWebService.updateAccountDetails", accountsVO));
			return flag;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getCaseManager(String crn) throws CMSException {
		this.logger.debug("Inside getCaseManager Method of AtlasWebServiceDAO class");
		this.logger.debug("crn is:::" + crn);
		String caseManager = "";

		try {
			caseManager = (String) this.queryForObject("AtlasWebService.getCaseManager", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("caseManager is:::" + caseManager);
		return caseManager;
	}

	public List getAnalystForUploadDocNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getAnalystForUploadDocNotification Method of AtlasWebServiceDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List analystList;
		try {
			analystList = this.queryForList("AtlasWebService.getAnalystForUploadDocNotification", crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("analystList size is:::" + analystList.size());
		return analystList;
	}

	public List getReviewersForUploadDocNotification(String crn) throws CMSException {
		this.logger.debug("Inside getReviewersForUploadDocNotification Method of AtlasWebServiceDAO class");
		new ArrayList();

		List reviewersList;
		try {
			reviewersList = this.queryForList("AtlasWebService.getReviewersForUploadDocNotification", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("reviewer1List size is:::" + reviewersList.size());
		return reviewersList;
	}

	public List getManagerForUploadDocNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getManagerForUploadDocNotification Method of AtlasWebServiceDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List managerList;
		try {
			managerList = this.queryForList("AtlasWebService.getManagerForUploadDocNotification", crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("managerList size is:::" + managerList.size());
		return managerList;
	}

	public boolean getRHOfficeValidationForOA(String researchHead) throws CMSException {
		this.logger.debug("Inside getRHOfficeValidationForOA Method of AtlasWebServiceDAO class");
		int count = false;
		boolean rhFlag = true;

		try {
			int count = (Integer) this.queryForObject("AtlasWebService.getRHOfficeValidationForOA", researchHead);
			if (count == 0) {
				rhFlag = false;
			}
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("rhFlag value is:::::::::::::" + rhFlag);
		return rhFlag;
	}

	public String getCaseInformation(String crn) throws CMSException {
		this.logger.debug("Inside getCaseInformation methos of AtlasWebServiceDAO class::" + crn);
		String caseinfo = "";

		try {
			caseinfo = (String) this.queryForObject("AtlasWebService.getCaseInfo", crn);
			return caseinfo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			System.out.println(var5.getMessage());
			throw new CMSException(this.logger, var5);
		}
	}

	public int validateLegacyDocRequest(String crn, String fileName, String version) throws CMSException {
		this.logger.debug("Inside validateLegacyDocRequest methos of AtlasWebServiceDAO class::" + crn
				+ ":::fielName:::" + fileName + "::::::version::" + version);
		int count = false;
		Map dataMap = new HashMap();
		dataMap.put("crn", crn);
		dataMap.put("fileName", fileName);
		dataMap.put("version", new Float(version));
		dataMap.put("folderName", "FINAL-REPORT");

		int count;
		try {
			count = (Integer) this.queryForObject("AtlasWebService.validateLegacyDocRequest", dataMap);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("count value is:::::::::::::" + count);
		return count;
	}

	public long getPIIDForCRN(String crn) throws CMSException {
		this.logger.debug("Inside getPIIDForCRN methos of AtlasWebServiceDAO class");
		long piid = 0L;

		try {
			Object object = this.queryForObject("AtlasWebService.getPIIDForCRN", crn);
			if (object == null) {
				piid = 0L;
			} else {
				piid = (Long) object;
			}
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("piid value is::::::" + piid);
		return piid;
	}

	public long checkDatabaseConnection() throws CMSException {
		this.logger.debug("Inside checkDatabaseConnection of AtlasWebService class");
		long value = 0L;

		try {
			Object obj = this.queryForObject(CHECK_DATABASE_CONNECTIVITY);
			if (obj == null) {
				value = 0L;
			} else {
				value = (Long) obj;
			}

			return value;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String checkExistenceforOrder(String orderGUID) throws CMSException {
		this.logger.debug("Inside checkExistenceforOrder of AtlasWebService class");
		String crn = "";

		try {
			crn = (String) this.queryForObject(CHECK_ORDER_EXISTENCE, orderGUID);
			return crn;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getSubjectListforAEDDOCase(String crn) {
		this.logger.debug("Inside getSubjectListforAEDDOCase..");
		List resultSubjectVOList = this.queryForList(GET_SUBJECT_LIST_AEDDO_CASE, crn);
		this.logger.debug("Exiting getSubjectListforAEDDOCase..");
		return resultSubjectVOList;
	}

	public List<Long> getPIDListfromSavvionforCRN(String crn) {
		this.logger.debug("inside getPIDListfromSavvionforCRN::" + crn);
		List<Long> pidList = this.queryForList(GET_PID_LIST_SAVVION, crn);
		this.logger.debug("exit getPIDListfromSavvionforCRN");
		return pidList;
	}

	public List<String> getSubReportTypeIDForReport(String reportTypeCode) throws CMSException {
		this.logger.debug("Inside getSubReportTypeIDForReport method of AtlasWebServiceDAO class.");
		new ArrayList();

		try {
			this.logger.debug("Inside reportTypeCode" + reportTypeCode);
			List<String> subReportIDList = this.queryForList("AtlasWebService.getSubReportTypeID", reportTypeCode);
			this.logger.debug("Inside method of subReportIDList" + subReportIDList);
			return subReportIDList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void insertToCMSFtpEntries(List filesList, String crn) throws CMSException {
      this.logger.debug("Inside insertToCMSFtpEntries method of AtlasWebServiceDAO class.");
      boolean var3 = false;

      try {
         int var6 = (Integer)this.execute(new 1(this, filesList, crn));
      } catch (Exception var5) {
         this.getStackTraceAsString(var5);
      }

   }

	public int updateCMSFtpEntries(String fileName, String crn, int status) throws CMSException {
		int flag = 0;

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("fileName", fileName);
			map.put("status", status);
			flag = Integer.valueOf(this.update("AtlasWebService.updateCMSFtpEntries", map));
		} catch (Exception var6) {
			this.getStackTraceAsString(var6);
		}

		return flag;
	}

	public String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}
}