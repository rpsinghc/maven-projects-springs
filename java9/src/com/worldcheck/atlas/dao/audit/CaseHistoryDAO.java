package com.worldcheck.atlas.dao.audit;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CaseHistoryDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.audit.CaseHistoryDAO");
	private static final String INSERT_CASE_HISTORY_SQL = "AuditHistory.insertCaseHistoryEvent";
	private static final String INSERT_CASE_HISTORY_DETAIL_SQL = "AuditHistory.insertCaseHistoryDetail";
	private static final String FETCH_CASE_COUNT_SQL_ID = "AuditHistory.getCaseHistoryCount";
	private static final String FETCH_CASE_HISTORY_SQL_ID = "AuditHistory.getCaseHistoryData";
	private static final String FETCH_CCS_TIMESTAMP = "AuditHistory.getCCSTimestamp";

	public int insertCaseHistoryEvent(List<CaseHistory> caseHistoryList) throws CMSException {
		this.logger.debug("In CaseHistoryDAO::insertCaseHistoryEvent");
		int caseHistoryID = 0;

		try {
			Iterator iterator = caseHistoryList.iterator();

			while (iterator.hasNext()) {
				CaseHistory caseHistoryObj = (CaseHistory) iterator.next();
				caseHistoryID = (Integer) this.insert("AuditHistory.insertCaseHistoryEvent", caseHistoryObj);
				this.logger.debug("caseHistoryID :--- " + caseHistoryID);
				caseHistoryObj.setCaseHistoryID(caseHistoryID);
				this.insertCaseHistoryDetails2(caseHistoryObj);
			}
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting CaseHistoryDAO::insertCaseHistoryEvent");
		return caseHistoryID;
	}

	public void insertCaseHistoryDetails2(CaseHistory caseHistory) throws CMSException {
		this.logger.debug("In CaseHistoryDAO::insertCaseHistoryDetails2");

		try {
			this.insert("AuditHistory.insertCaseHistoryDetail", caseHistory);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CaseHistoryDAO::insertCaseHistoryDetails2");
	}

	public List<CaseHistory> selectCaseHistoryDetails(HashMap paramList) throws CMSException {
		this.logger.debug("In CaseHistoryDAO::selectCaseHistoryDetails");
		new ArrayList();

		List result;
		try {
			result = this.queryForList("AuditHistory.getCaseHistoryData", paramList);
			this.logger.debug("Case history list size:- " + result.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryDAO::selectCaseHistoryDetails");
		return result;
	}

	public int getCaseHistoryCountForCrn(HashMap paramList) throws CMSException {
		this.logger.debug("In CaseHistoryDAO::getCaseHistoryCountForCrn");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("AuditHistory.getCaseHistoryCount", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryDAO::getCaseHistoryCountForCrn");
		return count;
	}

	public Map<BigDecimal, String> getSubjectMap(List<String> crnList) throws CMSException {
		this.logger.debug("In CaseHistoryDAO::getSubjectMap");
		new HashMap();

		Map subjectMap;
		try {
			subjectMap = this.queryForMap("AuditHistory.getSubjectIdNameMap", crnList, "key", "value");
			this.logger.debug("subjectMap " + subjectMap);
			Iterator var3 = subjectMap.keySet().iterator();
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryDAO::getSubjectMap");
		return subjectMap;
	}

	public long getTimeStampOfCCS(String pid) throws CMSException {
		this.logger.debug("In CaseHistoryDAO::getTimeStampOfCCS");
		Timestamp time = null;
		Timestamp date = null;

		try {
			time = (Timestamp) this.queryForObject("AuditHistory.getCCSTimestamp", pid);
			date = time;
			this.logger.debug("Timestamp:::" + time);
			this.logger.debug("date with timestamp:::" + time);
			this.logger.debug("date with time in millisecond:::" + time.getTime());
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting CaseHistoryDAO::getTimeStampOfCCS");
		return date.getTime();
	}
}