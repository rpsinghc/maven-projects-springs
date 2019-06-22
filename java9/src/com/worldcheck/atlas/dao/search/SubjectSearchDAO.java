package com.worldcheck.atlas.dao.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.search.SubjectSearchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class SubjectSearchDAO extends SqlMapClientTemplate implements ISearchDAO {
	private static final String SUBJECT_SEARCH_GET_ASSOCIATE_CASES_FOR_SUB = "SubjectSearch.getAssociateCasesForSub";
	private static final String SUBJECT_SEARCH_GET_SUBJECT_TYPE = "SubjectSearch.getSubjectType";
	private static final String SUBJECT_SEARCH_GET_SUBJECT_LIST = "SubjectSearch.getSubjectList";
	private static final String SUBJECT_SEARCH_GET_SUBJECT_LIST_COUNT = "SubjectSearch.getSubjectListCount";
	private static final String SUBJECT_SEARCH_EXPORT_SUBJECT_LIST = "SubjectSearch.getExportSubjectList";
	private static final String SUBJECT_SEARCH_GET_LEGACY_SUBJECT_LIST = "SubjectSearch.getLegacySubjectList";
	private static final String SUBJECT_SEARCH_GET_LEGACY_SUBJECT_LIST_COUNT = "SubjectSearch.getLegacySubjectListCount";
	private static final String SUBJECT_SEARCH_EXPORT_LEGACY_SUBJECT_LIST = "SubjectSearch.getExportLegacySubjectList";
	private static final String SUBJECT_SEARCH_GET_ASSOCIATE_CASES_COUNT_FOR_SUB = "SubjectSearch.getAssociateCasesCountForSub";
	private static final String SUBJECT_SEARCH_GET_LEGACY_ASSOCIATE_CASES_FOR_SUB = "SubjectSearch.getLegacyPastRecords";
	private static final String SUBJECT_SEARCH_GET_LEGACY_ASSOCIATE_CASES_COUNT_FOR_SUB = "SubjectSearch.getLegacyPastRecordsCount";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.search.SubjectSearchDAO");

	public List<SubjectSearchVO> search(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("SubjectSearch.getSubjectList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<SubjectDetails> getSubjectTypeList() throws CMSException {
		List subTypeMasterList = null;

		try {
			subTypeMasterList = this.queryForList("SubjectSearch.getSubjectType");
			return subTypeMasterList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<SubjectSearchVO> searchLegacyData(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("SubjectSearch.getLegacySubjectList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCountLegacyData(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("SubjectSearch.getLegacySubjectListCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("SubjectSearch.getSubjectListCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<SubjectSearchVO> searchExportLegacyData(Map<String, Object> excelParamMap) throws CMSException {
		List subjectSearchVOs = null;

		try {
			subjectSearchVOs = this.queryForList("SubjectSearch.getExportLegacySubjectList", excelParamMap);
			return subjectSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<SubjectSearchVO> searchExport(Map<String, Object> excelParamMap) throws CMSException {
		List subjectSearchVOs = null;

		try {
			subjectSearchVOs = this.queryForList("SubjectSearch.getExportSubjectList", excelParamMap);
			return subjectSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getAsscciateLegacyCaseCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		int asscciateCaseCount;
		try {
			asscciateCaseCount = (Integer) this.queryForObject("SubjectSearch.getLegacyPastRecordsCount",
					searchCriteria);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("asscciateCaseCount is:::" + asscciateCaseCount);
		return asscciateCaseCount;
	}

	public List<SubjectDetails> getAssociateLegacyCasesForSub(SearchCriteria searchCriteria) throws CMSException {
		new ArrayList();

		try {
			List<SubjectDetails> caseList = this.queryForList("SubjectSearch.getLegacyPastRecords", searchCriteria);
			return caseList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubjectDetails> getAssociateCasesForSub(SearchCriteria searchCriteria) throws CMSException {
		new ArrayList();

		try {
			List<SubjectDetails> caseList = this.queryForList("SubjectSearch.getAssociateCasesForSub", searchCriteria);
			return caseList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getAssociateCaseCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		int asscciateCaseCount;
		try {
			asscciateCaseCount = (Integer) this.queryForObject("SubjectSearch.getAssociateCasesCountForSub",
					searchCriteria);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("asscciateCaseCount is:::" + asscciateCaseCount);
		return asscciateCaseCount;
	}
}