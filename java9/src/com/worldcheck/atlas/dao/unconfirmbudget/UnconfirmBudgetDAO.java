package com.worldcheck.atlas.dao.unconfirmbudget;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class UnconfirmBudgetDAO extends SqlMapClientTemplate {
	private String UPDATE_FINAL_DUE_DATE_SQL = "UnconfirmBudget.updateFinalDueDate";
	private String UPDATE_FINAL_DUE_DATE_SQL_FOR_BULK = "UnconfirmBudget.updateFinalDueDateForBulk";
	private String SAVE_UNCONFIRM_BUDGET_DETAILS_SQL = "UnconfirmBudget.saveUnconfirmedBudgetDetails";
	private String FETCH_UNCONFIRM_DETAILS_SQL = "UnconfirmBudget.unconfirmedBudgetDetails";
	private String GET_UNCONFIRMED_BUDGET_DETAILS = "UnconfirmBudget.getUnconfirmedBudgetDetails";
	private String GET_UNCONFIRMED_BUDGET_DETAILS_COUNT = "UnconfirmBudget.getUnconfirmedBudgetDetailsCount";
	private String FETCH_ISIS_DETAILS_SQL = "UnconfirmBudget.getDetailForISIS";
	private String GET_CASE_OFFICE = "UnconfirmBudget.getCaseOffice";
	private String UPDATE_SUPPORTING_INTERNAL_DUE_DATE_SQL = "UnconfirmBudget.updateSupportingInternal1DueDate";
	private String GET_SUBJECT_LEVEL_BUDGET = "UnconfirmBudget.getSubjectLevelBudget";
	private String caseSubjectDetails = "Subject.crnDetail";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.unconfirmbudget.UnconfirmBudgetDAO");

	public UnconfimredBudgetVO getBudgetDetails(String crn) throws CMSException {
		try {
			return (UnconfimredBudgetVO) this.queryForObject(this.FETCH_UNCONFIRM_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public UnconfimredBudgetVO getDetailForISIS(String crn) throws CMSException {
		try {
			return (UnconfimredBudgetVO) this.queryForObject(this.FETCH_ISIS_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int saveISISDetails(UnconfimredBudgetVO unconfimredBudgetVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update(this.SAVE_UNCONFIRM_BUDGET_DETAILS_SQL, unconfimredBudgetVO);
			this.logger.debug(" updated records in account table :: " + count);
			count = this.update(this.UPDATE_FINAL_DUE_DATE_SQL, unconfimredBudgetVO);
			this.logger.debug(" updated record in clientcase :: " + count);
			if (unconfimredBudgetVO.getSupportingTeam1DueDate() != null
					&& !unconfimredBudgetVO.getSupportingTeam1DueDate().isEmpty()) {
				count = this.update(this.UPDATE_SUPPORTING_INTERNAL_DUE_DATE_SQL, unconfimredBudgetVO);
				this.logger.debug(" updated record in teamdetails " + count);
			}

			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UnconfimredBudgetVO> getBudgetRecords(UnconfimredBudgetVO vo) throws CMSException {
		try {
			List<UnconfimredBudgetVO> unConfirmedBudgetList = this.queryForList(this.GET_UNCONFIRMED_BUDGET_DETAILS,
					vo);
			Iterator iterator = unConfirmedBudgetList.iterator();

			while (iterator.hasNext()) {
				UnconfimredBudgetVO uvo = (UnconfimredBudgetVO) iterator.next();
				new CaseDetails();
				new ArrayList();
				List subjGroup = new ArrayList();
				this.logger.debug("crn is " + uvo.getCRN());
				Map caseMap = new HashMap();
				caseMap.put("crn", uvo.getCRN());
				caseMap.put("sort", vo.getSortColumnName());
				caseMap.put("start", vo.getStart());
				caseMap.put("limit", vo.getLimit());
				caseMap.put("dir", vo.getSortType());
				this.logger.debug("caseMap is::::::::" + caseMap);
				if (uvo.getIsSubreportRequire() != null && uvo.getIsSubreportRequire().trim().equalsIgnoreCase("1")) {
					this.logger.debug("getting subject Details for the CRN " + uvo.getCRN());
					List<SubjectDetails> subjectDtls = this.queryForList("Subject.subjectDetailsForCRN", caseMap);
					Iterator var10 = subjectDtls.iterator();

					while (var10.hasNext()) {
						SubjectDetails subDtls = (SubjectDetails) var10.next();
						ArrayList<String> subjList = new ArrayList();
						subjList.add(subDtls.getIsUpdated());
						subjList.add("" + subDtls.getSubjectId());
						subjList.add(subDtls.getSubjectName());
						subjList.add("" + subDtls.getCountryId());
						subjList.add(subDtls.getSubReportType());
						subjList.add(subDtls.getCountryName());
						subjList.add("" + subDtls.getEntityTypeId());
						subjList.add("" + subDtls.getSlBudget());
						subjList.add(subDtls.getCurrencycode());
						subjList.add(subDtls.getIsSubjLevelSubRptReq());
						subjList.add(subDtls.getIsisSubjectId());
						subjList.add(subDtls.getCrn());
						subjList.add(subDtls.getSubReportTypeId());
						subjGroup.add(subjList);
					}
				}

				this.logger.debug("subjectDtls::::::::::" + subjGroup);
				uvo.setSubjectBudgetList(subjGroup);
			}

			return unConfirmedBudgetList;
		} catch (DataAccessException var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public int getBudgetRecordsCount(String loginId) throws CMSException {
		try {
			return (Integer) this.queryForObject(this.GET_UNCONFIRMED_BUDGET_DETAILS_COUNT, loginId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int saveUnconfirmDetails(UnconfimredBudgetVO unconfimredBudgetVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update(this.SAVE_UNCONFIRM_BUDGET_DETAILS_SQL, unconfimredBudgetVO);
			this.logger.debug(" updated records in account table " + count);
			count = this.update(this.UPDATE_FINAL_DUE_DATE_SQL_FOR_BULK, unconfimredBudgetVO);
			this.logger.debug(" updated record in clientcase " + count);
			if (unconfimredBudgetVO.getSupportingTeam1DueDate() != null
					&& !unconfimredBudgetVO.getSupportingTeam1DueDate().isEmpty()) {
				count = this.update(this.UPDATE_SUPPORTING_INTERNAL_DUE_DATE_SQL, unconfimredBudgetVO);
				this.logger.debug(" updated record in teamdetails " + count);
			}

			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getCrnOffice(String crn) throws CMSException {
		try {
			String officeId = (String) this.queryForObject(this.GET_CASE_OFFICE, crn);
			return officeId;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}