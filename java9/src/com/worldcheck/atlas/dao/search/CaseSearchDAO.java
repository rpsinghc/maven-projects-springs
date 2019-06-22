package com.worldcheck.atlas.dao.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.CaseSearchVO;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CaseSearchDAO extends SqlMapClientTemplate implements ISearchDAO {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.search.CaseSearchDAO");
	private static final String CASE_SEARCH_GET_CASE_CREATOR = "CaseSearch.getCaseCreator";
	private static final String CASE_SEARCH_GET_CASE_LIST = "CaseSearch.getCaseList";
	private static final String CASE_SEARCH_GET_LEGACY_CASE_LIST = "CaseSearch.getLegacyCaseList";
	private static final String CASE_SEARCH_GET_LEGACY_CASE_COUNT = "CaseSearch.getLegacyCaseCount";
	private static final String CASE_SEARCH_GET_CASE_LIST_COUNT = "CaseSearch.getCaseListCount";
	private static final String CASE_SEARCH_EXPORT_LEGACY_CASE_LIST = "CaseSearch.getExportLegacyCaseList";
	private static final String CASE_SEARCH_EXPORT_CASE_LIST = "CaseSearch.getExportCaseList";
	private static final String CASE_SEARCH_RECURRENCE_CASE_LIST = "CaseSearch.getRecCaseList";
	private static final String CASE_SEARCH_GET_REC_CASE_LIST_COUNT = "CaseSearch.getRecCaseCount";
	private static final String CASE_SEARCH_EXPORT_REC_CASE_LIST = "CaseSearch.getExportRecCaseList";

	public List<CaseSearchVO> search(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("CaseSearch.getCaseList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> getCaseCreatorMaster() throws CMSException {
		List userMasterList = null;

		try {
			userMasterList = this.queryForList("CaseSearch.getCaseCreator");
			return userMasterList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CaseSearchVO> searchLegacyData(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("CaseSearch.getLegacyCaseList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCountLegacyData(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("CaseSearch.getLegacyCaseCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("CaseSearch.getCaseListCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CaseSearchVO> searchExportLegacyData(Map<String, Object> excelParamMap) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("CaseSearch.getExportLegacyCaseList", excelParamMap);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CaseSearchVO> searchExport(Map<String, Object> excelParamMap) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("CaseSearch.getExportCaseList", excelParamMap);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CaseSearchVO> searchRecurrecnce(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("CaseSearch.getRecCaseList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCountRec(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("CaseSearch.getRecCaseCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CaseSearchVO> searchExportForRec(Map<String, Object> excelParamMap) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("CaseSearch.getExportRecCaseList", excelParamMap);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}