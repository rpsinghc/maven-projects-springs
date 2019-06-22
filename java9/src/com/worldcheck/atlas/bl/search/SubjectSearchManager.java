package com.worldcheck.atlas.bl.search;

import com.worldcheck.atlas.bl.interfaces.ISearch;
import com.worldcheck.atlas.bl.interfaces.ISubjectSearchManager;
import com.worldcheck.atlas.dao.search.SubjectSearchDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.search.SubjectSearchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubjectSearchManager implements ISubjectSearchManager, ISearch {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.search.SubjectSearchManager");
	SubjectSearchDAO subjectSearchDAO = null;

	public void setSubjectSearchDAO(SubjectSearchDAO subjectSearchDAO) {
		this.subjectSearchDAO = subjectSearchDAO;
	}

	public List search(SearchCriteria searchCriteria) throws CMSException {
		new ArrayList();

		try {
			this.setDates(searchCriteria);
			List caseSearchList;
			if (searchCriteria.getLegacyData().equalsIgnoreCase("true")) {
				caseSearchList = this.subjectSearchDAO.searchLegacyData(searchCriteria);
			} else {
				caseSearchList = this.subjectSearchDAO.search(searchCriteria);
			}

			this.logger.debug("Number of records fetched for subject search " + caseSearchList.size());
			return caseSearchList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<SubjectDetails> getSubjectTypeList() throws CMSException {
		new ArrayList();

		try {
			List<SubjectDetails> subjectTypeMasterList = this.subjectSearchDAO.getSubjectTypeList();
			SubjectDetails subjectDetailsVO = new SubjectDetails();
			subjectDetailsVO.setEntityName("All");
			subjectDetailsVO.setEntityTypeId(0);
			subjectTypeMasterList.add(subjectDetailsVO);
			this.logger.debug("fetched subject types " + subjectTypeMasterList.size());
			return subjectTypeMasterList;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int resultCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			this.setDates(searchCriteria);
			int count;
			if (searchCriteria.getLegacyData().equalsIgnoreCase("true")) {
				count = this.subjectSearchDAO.resultCountLegacyData(searchCriteria);
			} else {
				count = this.subjectSearchDAO.resultCount(searchCriteria);
			}

			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	private void setDates(SearchCriteria searchCriteria) {
		if (searchCriteria.getStartCaseRecvDate() != null
				&& searchCriteria.getStartCaseRecvDate().trim().length() > 0) {
			searchCriteria.setStartCaseRecvDate(searchCriteria.getStartCaseRecvDate().replace("T", " "));
		}

		if (searchCriteria.getEndCaseRecvDate() != null && searchCriteria.getEndCaseRecvDate().trim().length() > 0) {
			searchCriteria.setEndCaseRecvDate(searchCriteria.getEndCaseRecvDate().replace("T", " "));
		}

		if (searchCriteria.getStartClientDueDate() != null
				&& searchCriteria.getStartClientDueDate().trim().length() > 0) {
			searchCriteria.setStartClientDueDate(searchCriteria.getStartClientDueDate().replace("T", " "));
		}

		if (searchCriteria.getEndClientDueDate() != null && searchCriteria.getEndClientDueDate().trim().length() > 0) {
			searchCriteria.setEndClientDueDate(searchCriteria.getEndClientDueDate().replace("T", " "));
		}

	}

	private void setDatesMap(Map<String, Object> excelParamMap) {
		if (excelParamMap.get("startCaseRecvDate") != null
				&& ((String) excelParamMap.get("startCaseRecvDate")).trim().length() > 0) {
			excelParamMap.put("startCaseRecvDate", ((String) excelParamMap.get("startCaseRecvDate")).replace("T", " "));
		}

		if (excelParamMap.get("endCaseRecvDate") != null
				&& ((String) excelParamMap.get("endCaseRecvDate")).trim().length() > 0) {
			excelParamMap.put("endCaseRecvDate", ((String) excelParamMap.get("endCaseRecvDate")).replace("T", " "));
		}

		if (excelParamMap.get("startClientDueDate") != null
				&& ((String) excelParamMap.get("startClientDueDate")).trim().length() > 0) {
			excelParamMap.put("startClientDueDate",
					((String) excelParamMap.get("startClientDueDate")).replace("T", " "));
		}

		if (excelParamMap.get("endClientDueDate") != null
				&& ((String) excelParamMap.get("endClientDueDate")).trim().length() > 0) {
			excelParamMap.put("endClientDueDate", ((String) excelParamMap.get("endClientDueDate")).replace("T", " "));
		}

	}

	public List<SubjectSearchVO> searchForExport(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();

		try {
			this.setDatesMap(excelParamMap);
			List subjectSearchList;
			if (((String) excelParamMap.get("legacyData")).equalsIgnoreCase("true")) {
				subjectSearchList = this.subjectSearchDAO.searchExportLegacyData(excelParamMap);
			} else {
				subjectSearchList = this.subjectSearchDAO.searchExport(excelParamMap);
			}

			return subjectSearchList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<SubjectDetails> getAssociateCasesForSub(SearchCriteria searchCriteria) throws CMSException {
		new ArrayList();

		try {
			List subjectSearchList;
			if (searchCriteria.getLegacyData().equalsIgnoreCase("1")) {
				subjectSearchList = this.subjectSearchDAO.getAssociateLegacyCasesForSub(searchCriteria);
			} else {
				subjectSearchList = this.subjectSearchDAO.getAssociateCasesForSub(searchCriteria);
			}

			return subjectSearchList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getAsscciateCaseCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count;
			if (searchCriteria.getLegacyData().equalsIgnoreCase("1")) {
				count = this.subjectSearchDAO.getAsscciateLegacyCaseCount(searchCriteria);
			} else {
				count = this.subjectSearchDAO.getAssociateCaseCount(searchCriteria);
			}

			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}