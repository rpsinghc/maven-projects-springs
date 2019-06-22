package com.worldcheck.atlas.scheduler.dao;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.vo.EmailPrepaymentCaseVo;
import com.worldcheck.atlas.scheduler.vo.OverdueCasesSchedulerVo;
import com.worldcheck.atlas.scheduler.vo.SchedulerVo;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.document.DocMapVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class SchedulerDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.scheduler.dao.SchedulerDAO");
	private static final String GET_FINANCE_USERS = "AtlasScheduler.getAvailableCasesForCreation";
	private static final String UPDATE_SCHEDULER_DATES = "AtlasScheduler.updateSchedulerLastNextRunDate";
	private static final String INSERT_REC_CASE_NOTIFICATION = "AtlasScheduler.insertRecCaseNotification";
	private static final String UPDATE_LAST_REC_NUMBER = "AtlasScheduler.updateLastRecurNumber";
	private static final String UPDATE_NEW_CRN_IN_SCHEDULER = "AtlasScheduler.updateNewCrnInScheduler";
	private static final String ADD_NEW_SUBJECT = "AtlasScheduler.insertSubject";
	private static final String GET_LAST_RECURR_NUMBER = "AtlasScheduler.getLastRecurNumber";
	private static final String GET_CASE_DETAILS = "AtlasScheduler.getCaseDetails";
	private static final String TURN_AROUND_TIME = "AtlasScheduler.getTurnAround";
	private static final String CHECK_FOR_HOLIDAY = "AtlasScheduler.isHoliday";
	private static final String UPDATE_PID_FOR_CRN = "AtlasScheduler.updatePidForCrn";
	private static final String GET_SUBJECT_FOR_CASE = "AtlasScheduler.getSubjectForCrn";
	private static final String UPDATE_ACKNOWLEDGE_DATE = "AtlasScheduler.updateAcknowledgeDate";
	private static final String INSERT_ASC_SUBJECT_SQL = "AtlasScheduler.insertAscSubject";
	private static final String GET_ATTACHMENT_SQL = "AtlasScheduler.getAttachmentsForCase";
	private static final String DELETE_CASE_SQL = "AtlasScheduler.deleteCrn";
	private static final String GET_OVERDUE_CASES_LIST = "AtlasScheduler.getUsersForOverdueNotification";
	private static final String UPDATE_DOC_VERSION = "AtlasScheduler.updateDocVersion";
	private static final String EMAIL_PREPAYMENT_CASE = "AtlasScheduler.getEmailPrepaymentCaseResult";
	private static final String GET_FEEDBACK_ATTACH = "AtlasScheduler.getTempFeedbackAttach";
	private static final String REMOVE_TEMP_FEEDBACK_FILES = "AtlasScheduler.removeTempFeedbackAttach";

	public List<SchedulerVo> getAvailableRecCasesForScheduler() throws CMSException {
		this.logger.debug("In SchedulerDAO::getAvailableRecCasesForScheduler");
		new ArrayList();

		List schedulerVoList;
		try {
			schedulerVoList = this.queryForList("AtlasScheduler.getAvailableCasesForCreation");
			this.logger.debug("schedulerVoList size :- " + schedulerVoList.size());
			this.logger.debug("schedulerVoList.size :- " + schedulerVoList.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting SchedulerDAO::getAvailableRecCasesForScheduler");
		return schedulerVoList;
	}

	public void updateSchedulerLastNextRunDate(List<String> crnList) throws CMSException {
		this.logger.debug("In SchedulerDAO::updateSchedulerLastNextRunDate");
		boolean var2 = false;

		try {
			int updateCount = this.update("AtlasScheduler.updateSchedulerLastNextRunDate", crnList);
			this.logger.debug("updateCount " + updateCount);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting SchedulerDAO::updateSchedulerLastNextRunDate");
	}

	public void insertRecCaseNotificationAndDates(SchedulerVo schedulerVo) throws CMSException {
		this.logger.debug("In SchedulerDAO::insertRecCaseNotification");

		try {
			this.insert("AtlasScheduler.insertRecCaseNotification", schedulerVo);
			this.logger.debug("INSERTED");
			this.logger.debug("Exiting SchedulerDAO::insertRecCaseNotification");
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateLastRecurrenceNumber(HashMap<String, String> paramMap) throws CMSException {
		this.logger.debug("In SchedulerDAO::updateLastRecurrneceNumber");
		boolean var2 = false;

		try {
			int updateCount = this.update("AtlasScheduler.updateLastRecurNumber", paramMap);
			this.logger.debug("updateCount " + updateCount);
			this.logger.debug("Exiting SchedulerDAO::updateLastRecurrneceNumber");
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateNewCrnInScheduler(HashMap<String, String> paramMap) throws CMSException {
		this.logger.debug("In SchedulerDAO::updateNewCrnInScheduler");
		boolean var2 = false;

		try {
			int updateCount = this.update("AtlasScheduler.updateNewCrnInScheduler", paramMap);
			this.logger.debug("updateCount " + updateCount);
			this.logger.debug("Exiting SchedulerDAO::updateNewCrnInScheduler");
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubjectDetails> getSubjectForCrn(HashMap<String, String> paramMap) throws CMSException {
		this.logger.debug("In SchedulerDAO::getSubjectForCrn");
		new ArrayList();

		List subjectList;
		try {
			subjectList = this.queryForList("AtlasScheduler.getSubjectForCrn", paramMap);
			this.logger.debug("subjectList size:- " + subjectList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting SchedulerDAO::getSubjectForCrn");
		return subjectList;
	}

	public int addSubjectToCase(SubjectDetails subjectDetails) throws CMSException {
		this.logger.debug("In SchedulerDAO::addSubjectToCase");

		int subjectID;
		try {
			subjectID = (Integer) this.insert("AtlasScheduler.insertSubject", subjectDetails);
			this.logger.debug("new subjectID " + subjectID);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting SchedulerDAO::addSubjectToCase");
		return subjectID;
	}

	public SchedulerVo getLastRecurNumberAndDates(String recCaseSchedulerId) throws CMSException {
		this.logger.debug("In SchedulerDAO::getLastRecurNumberAndDates");
		new SchedulerVo();

		SchedulerVo schedulerVo;
		try {
			schedulerVo = (SchedulerVo) this.queryForObject("AtlasScheduler.getLastRecurNumber", recCaseSchedulerId);
			this.logger.debug("lastRecurrNumber " + schedulerVo.getLastRecurrenceNumber());
			this.logger.debug("getClientFinalDate " + schedulerVo.getClientFinalDate());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting SchedulerDAO::getLastRecurNumberAndDates");
		return schedulerVo;
	}

	public CaseDetails getcaseDetails(String recClientCaseId) throws CMSException {
		this.logger.debug(" in SchedulerDAO::getcaseDetails");
		new CaseDetails();

		CaseDetails caseDetails;
		try {
			caseDetails = (CaseDetails) this.queryForObject("AtlasScheduler.getCaseDetails", recClientCaseId);
			this.logger.debug("caseDetails.getOfficeId() " + caseDetails.getOfficeId());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("exit SchedulerDAO::getcaseDetails");
		return caseDetails;
	}

	public int getTurnAround(String reportType) throws CMSException {
		int i = 0;

		try {
			Object obj = this.queryForObject("AtlasScheduler.getTurnAround", reportType);
			if (obj != null) {
				i = (Integer) obj;
			}

			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public boolean checkForHoliday(String holidayDate, String officeName) throws CMSException {
		boolean isHoliday = false;
		Map<String, String> map = new HashMap();
		map.put("holidayDate", holidayDate);
		map.put("officeName", officeName);

		try {
			Object obj = this.queryForObject("AtlasScheduler.isHoliday", map);
			if (obj != null && (Integer) obj > 0) {
				isHoliday = true;
			}

			return isHoliday;
		} catch (ClassCastException var6) {
			throw new CMSException(this.logger, var6);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public void updatePidForCase(Map<String, String> map) throws CMSException {
		this.logger.debug("In SchedulerDAO::updatePidForCase");

		try {
			int updateCount = this.update("AtlasScheduler.updatePidForCrn", map);
			this.logger.debug("updateCount " + updateCount);
			this.logger.debug("Exiting SchedulerDAO::updatePidForCase");
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateAcknowledgeDate(Map<String, String> map) throws CMSException {
		this.logger.debug("In SchedulerDAO::updateAcknowledgeDate");
		boolean var2 = false;

		try {
			int updateCount = this.update("AtlasScheduler.updateAcknowledgeDate", map);
			this.logger.debug("updateCount " + updateCount);
			this.logger.debug("Exiting SchedulerDAO::updateAcknowledgeDate");
			return updateCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void insertAscSubject(Map<String, String> map) throws CMSException {
		this.logger.debug("In SchedulerDAO::insertAscSubject");

		try {
			this.insert("AtlasScheduler.insertAscSubject", map);
			this.logger.debug("Exiting SchedulerDAO::insertAscSubject");
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<DocMapVO> getAttachmentsForCase(Map<String, String> map) throws CMSException {
		this.logger.debug("In SchedulerDAO::getAttachmentsForCase");
		new ArrayList();

		try {
			List<DocMapVO> DocMapVOList = this.queryForList("AtlasScheduler.getAttachmentsForCase", map);
			this.logger.debug("Exiting SchedulerDAO::getAttachmentsForCase");
			return DocMapVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void deleteCaseFromClientCase(Map<String, String> map) throws CMSException {
		this.logger.debug("In SchedulerDAO::deleteCaseFromClientCase");

		try {
			this.delete("AtlasScheduler.deleteCrn", map);
			this.logger.debug("Exiting SchedulerDAO::deleteCaseFromClientCase");
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<OverdueCasesSchedulerVo> getOverdueCases() throws CMSException {
		this.logger.debug("In SchedulerDAO::getOverdueCases");
		new ArrayList();

		List overdueSchedulerVoList;
		try {
			overdueSchedulerVoList = this.queryForList("AtlasScheduler.getUsersForOverdueNotification");
			this.logger.debug("overdueSchedulerVo size:- " + overdueSchedulerVoList.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting SchedulerDAO::getOverdueCases");
		return overdueSchedulerVoList;
	}

	public int updateDocVersion(DocMapVO docMapVO) throws CMSException {
		this.logger.debug("In SchedulerDAO::updateDocVersion");
		boolean var2 = false;

		int updateCount;
		try {
			updateCount = this.update("AtlasScheduler.updateDocVersion", docMapVO);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting SchedulerDAO::updateDocVersion");
		return updateCount;
	}

	public List<EmailPrepaymentCaseVo> sendEmailPrepayementCase() throws CMSException {
		this.logger.debug("In SchedulerDAO::sendEmailPrepayementCase");
		new ArrayList();

		List resultVo;
		try {
			resultVo = this.queryForList("AtlasScheduler.getEmailPrepaymentCaseResult");
			this.logger.debug("Size of resultVO for Prepayment Cases is" + resultVo.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting SchedulerDAO::sendEmailPrepayementCase");
		return resultVo;
	}

	public List<String> getTempFeedbackAttach() throws CMSException {
		this.logger.debug("In SchedulerDAO::getTempFeedbackAttach");
		new ArrayList();

		List listOfFiles;
		try {
			listOfFiles = this.queryForList("AtlasScheduler.getTempFeedbackAttach");
			this.logger.debug("Size of resultVO for removing attachments is" + listOfFiles.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting SchedulerDAO::getTempFeedbackAttach");
		return listOfFiles;
	}

	public void removeTempFeedbackAttach(List<String> listOfFiles) throws CMSException {
		this.logger.debug("In SchedulerDAO::removeTempFeedbackAttach==========");

		try {
			SchedulerVo schedulerVo = new SchedulerVo();
			schedulerVo.setListOfEnteries(listOfFiles);
			this.delete("AtlasScheduler.removeTempFeedbackAttach", schedulerVo);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting SchedulerDAO::removeTempFeedbackAttach==========");
	}
}