package com.worldcheck.atlas.bl.search;

import com.worldcheck.atlas.bl.interfaces.ICaseSearchManager;
import com.worldcheck.atlas.bl.interfaces.ISearch;
import com.worldcheck.atlas.dao.search.CaseSearchDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.CaseSearchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaseSearchManager implements ICaseSearchManager, ISearch {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.search.CaseSearchManager");
	PropertyReaderUtil propertyReader = null;
	CaseSearchDAO caseSearchDAO = null;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setCaseSearchDAO(CaseSearchDAO caseSearchDAO) {
		this.caseSearchDAO = caseSearchDAO;
	}

	public List<UserMasterVO> getCaseCreatorMaster() throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> userMasterList = this.caseSearchDAO.getCaseCreatorMaster();
			UserMasterVO userMasterVO = new UserMasterVO();
			userMasterVO.setUsername(this.propertyReader.getIsisUserId());
			userMasterVO.setUserFullName(this.propertyReader.getIsisUserId());
			userMasterList.add(userMasterVO);
			return userMasterList;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CaseSearchVO> search(SearchCriteria searchCriteria) throws CMSException {
		new ArrayList();

		try {
			List caseSearchList;
			if (null != searchCriteria.getSearchType() && searchCriteria.getSearchType().equals("recurrence")) {
				caseSearchList = this.caseSearchDAO.searchRecurrecnce(searchCriteria);
			} else {
				this.setDates(searchCriteria);
				if (searchCriteria.getLegacyData().equalsIgnoreCase("true")) {
					caseSearchList = this.caseSearchDAO.searchLegacyData(searchCriteria);
				} else {
					caseSearchList = this.caseSearchDAO.search(searchCriteria);
				}
			}

			this.logger.debug("Number of records fetched for case search " + caseSearchList.size());
			return caseSearchList;
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

	public int resultCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count;
			if (null != searchCriteria.getSearchType() && searchCriteria.getSearchType().equals("recurrence")) {
				count = this.caseSearchDAO.resultCountRec(searchCriteria);
			} else {
				this.setDates(searchCriteria);
				if (searchCriteria.getLegacyData().equalsIgnoreCase("true")) {
					count = this.caseSearchDAO.resultCountLegacyData(searchCriteria);
				} else {
					count = this.caseSearchDAO.resultCount(searchCriteria);
				}
			}

			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CaseSearchVO> searchForExport(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();

		try {
			List caseSearchList;
			if (null != excelParamMap.get("searchType") && excelParamMap.get("searchType").equals("recurrence")) {
				caseSearchList = this.caseSearchDAO.searchExportForRec(excelParamMap);
			} else {
				this.setDatesMap(excelParamMap);
				if (((String) excelParamMap.get("legacyData")).equalsIgnoreCase("true")) {
					caseSearchList = this.caseSearchDAO.searchExportLegacyData(excelParamMap);
				} else {
					caseSearchList = this.caseSearchDAO.searchExport(excelParamMap);
				}
			}

			return caseSearchList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}