package com.worldcheck.atlas.dao.casedetails;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.CaseStatus;
import com.worldcheck.atlas.vo.EmailTemplateVO;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import com.worldcheck.atlas.vo.task.MyOnHoldCaseVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CaseDetailsDAO extends SqlMapClientTemplate {
	private static final String CASE_DETAILS_GET_PIDS_FOR_BULK_CRN = "CaseDetails.getPIDsForBulkCRN";
	private static final String CASE_DETAILS_UPDATE_RESEARCH_DUE_DATES = "CaseDetails.updateResearchDueDates";
	private static final String CASE_DETAILS_GET_CASE_DETAILS = "CaseDetails.getCaseDetails";
	private static final String CASE_DETAILS_UPDATE_CASE_DETAILS = "CaseDetails.updateCaseDetails";
	private static final String CASE_DETAILS_UPDATE_CASE_FEE = "CaseDetails.updateCaseFee";
	private static final String GET_CASE_STATUS_MASTER = "CaseDetails.getCaseStatusMaster";
	private static final String GET_ALL_COUNTRY_MASTER = "CaseDetails.getAllCountryMaster";
	private static final String CASE_DETAILS_UPDATE_REC_CASE_DETAILS = "CaseDetails.updateRecCaseDetails";
	private static final String CASE_DETAILS_UPDATE_CASE_STATUS = "CaseDetails.updateCaseStatus";
	private static final String GET_ON_HOLD_CASES = "CaseDetails.getOnHoldCases";
	private static final String GET_COUNT_OF_ON_HOLD_CASES = "CaseDetails.getCountOfOnHoldCases";
	private static final String CASE_DETAILS_GET_LEGACY_CASE_DETAILS = "CaseDetails.getLegacyCaseDetails";
	private static final String CASE_DETAILS_GET_CRN_FOR_PID = "CaseDetails.getCrn";
	private static final String REC_CASE_DETAILS = "CaseDetails.getRecurranceCaseDetails";
	private static final String UPDATE_REC_CASE_DETAILS = "CaseDetails.updateRecurranceCaseDetails";
	private static final String CASE_DETAILS_UPDATE_SAVVION_CASE_DETAILS = "CaseDetails.updateSavvionCaseDetails";
	private static final String GET_CASE_STATUS = "CaseDetails.getCaseStatus";
	private static final String CASE_DETAILS_GET_CASE_DETAILS_PID = "CaseDetails.getCaseDetailsForPid";
	private static final String CASE_DETAILS_GET_RESEARCHHEAD_DETAILS = "CaseDetails.getResearchHeadDetails";
	private static final String CASE_DETAILS_GET_TEAMMANAGER_DETAILS = "CaseDetails.getTeamManagerDetails";
	private static final String CASE_DETAILS_UPDATE_CLIENT_FEEDBACK = "CaseDetails.updateClientFeedback";
	private static final String CASE_DETAILS_UPDATE_CASE_INFO = "CaseDetails.updateCaseInfo";
	private static final String GET_MAIL_BODY = "CaseDetails.getMailBody";
	private static final String INSERT_MAIL_LOGS = "CaseDetails.insertMailLogs";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.casedetails.CaseDetailsDAO");

	public CaseDetails getCaseDetails(String crn) throws CMSException {
		CaseDetails caseDetails = null;

		try {
			caseDetails = (CaseDetails) this.queryForObject("CaseDetails.getCaseDetails", crn);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public CaseDetails getCaseStatus(String crn) throws CMSException {
		try {
			return (CaseDetails) this.queryForObject("CaseDetails.getCaseStatus", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int saveCaseInformation(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("CaseDetails.updateCaseDetails", caseDetails);
			this.logger.debug("count for the first query " + count);
			count = this.update("CaseDetails.updateCaseInfo", caseDetails);
			if (caseDetails.getCaseStatusId() == 4) {
				this.update("CaseDetails.updateCaseFee", caseDetails);
			}

			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CaseStatus> getCaseStatusMaster() throws CMSException {
		List caseStatus = null;

		try {
			caseStatus = this.queryForList("CaseDetails.getCaseStatusMaster");
			return caseStatus;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int saveRecCaseInformation(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("CaseDetails.updateRecCaseDetails", caseDetails);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateCaseStatus(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("CaseDetails.updateCaseStatus", caseDetails);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<MyOnHoldCaseVO> getOnHoldCases(Map parameterMap) throws CMSException {
		List onHoldCases = null;

		try {
			onHoldCases = this.queryForList("CaseDetails.getOnHoldCases", parameterMap);
			return onHoldCases;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getCountOfOnHoldCases(Map parameterMap) throws CMSException {
		Integer count = 0;

		try {
			count = (Integer) this.queryForObject("CaseDetails.getCountOfOnHoldCases", parameterMap);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		return count;
	}

	public CaseDetails getLegacyCaseDetails(String crn) throws CMSException {
		CaseDetails caseDetails = null;

		try {
			caseDetails = (CaseDetails) this.queryForObject("CaseDetails.getLegacyCaseDetails", crn);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getCrnForPid(long pid) throws CMSException {
		String crn = "";

		try {
			crn = (String) this.queryForObject("CaseDetails.getCrn", pid);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("The crn for pid " + pid + " is " + crn);
		return crn;
	}

	public CaseDetails getRecurranceCaseDetails(String crn) throws CMSException {
		new CaseDetails();
		this.logger.debug("IN getRecurranceCaseDetails");

		try {
			CaseDetails caseDetails = (CaseDetails) this.queryForObject("CaseDetails.getRecurranceCaseDetails", crn);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateRecurranceCaseDetails(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("IN updateRecurranceCaseDetails");

		try {
			this.update("CaseDetails.updateRecurranceCaseDetails", caseDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateCaseResearchDueDates(CaseDetails caseDetails) throws CMSException {
		Integer count = 0;

		try {
			count = this.update("CaseDetails.updateResearchDueDates", caseDetails);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		return count;
	}

	public int saveSavvionCaseInformation(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("CaseDetails.updateSavvionCaseDetails", caseDetails);
			this.logger.debug("count for the first query " + count);
			count = this.update("CaseDetails.updateCaseInfo", caseDetails);
			if (caseDetails.getCaseStatusId() == 4) {
				this.update("CaseDetails.updateCaseFee", caseDetails);
			}

			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public CaseDetails getCaseInfoForPID(String pid) throws CMSException {
		CaseDetails caseDetails = null;

		try {
			caseDetails = (CaseDetails) this.queryForObject("CaseDetails.getCaseDetailsForPid", pid);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<String> getResearchHeadsForCase(String crn) throws CMSException {
		List researchHeadList = null;

		try {
			researchHeadList = this.queryForList("CaseDetails.getResearchHeadDetails", crn);
			return researchHeadList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<String> getTeamManagerForCase(String crn) throws CMSException {
		List researchHeadList = null;

		try {
			researchHeadList = this.queryForList("CaseDetails.getTeamManagerDetails", crn);
			return researchHeadList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateClientFeedback(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("CaseDetails.updateClientFeedback", caseDetails);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public HashMap<String, String> getPIDsForBulkCRN(List<String> cRNList) throws CMSException {
		new HashMap();

		try {
			HashMap<String, String> teamDetails = (HashMap) this.queryForMap("CaseDetails.getPIDsForBulkCRN", cRNList,
					"key", "value");
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CountryDatabaseMasterVO> getAllCountryMasterList() throws CMSException {
		List allCountryMasterList = null;

		try {
			allCountryMasterList = this.queryForList("CaseDetails.getAllCountryMaster");
			return allCountryMasterList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public EmailTemplateVO getMailBody(Map<String, String> dataMap) throws CMSException {
		try {
			return (EmailTemplateVO) this.queryForObject("CaseDetails.getMailBody", dataMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void insertMailLogs(EmailTemplateVO emailTemplateVO) {
		this.insert("CaseDetails.insertMailLogs", emailTemplateVO);
	}
}