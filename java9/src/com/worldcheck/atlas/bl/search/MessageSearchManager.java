package com.worldcheck.atlas.bl.search;

import com.worldcheck.atlas.bl.interfaces.IMessageSearchManager;
import com.worldcheck.atlas.bl.interfaces.ISearch;
import com.worldcheck.atlas.dao.search.MessageSearchDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.MessageSearchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageSearchManager implements IMessageSearchManager, ISearch {
	private static final String _BLANK = " ";
	private static final String T = "T";
	private static final String SYS_USER_NAME = "sysUserName";
	private static final String END_CLIENT_DUE_DATE = "endClientDueDate";
	private static final String START_CLIENT_DUE_DATE = "startClientDueDate";
	private static final String END_CASE_RECV_DATE = "endCaseRecvDate";
	private static final String START_CASE_RECV_DATE = "startCaseRecvDate";
	private static final String MY_TEAM = "My Team";
	private static final String _2 = "2";
	private static final String _1 = "1";
	MessageSearchDAO messageSearchDAO = null;
	PropertyReaderUtil propertyReader = null;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.search.MessageSearchManager");

	public void setMessageSearchDAO(MessageSearchDAO messageSearchDAO) {
		this.messageSearchDAO = messageSearchDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public List<MessageSearchVO> search(SearchCriteria searchCriteria) throws CMSException {
		searchCriteria.setSysUserName(this.propertyReader.getSystemNotificationUser());
		new ArrayList();
		new ArrayList();

		try {
			this.setDates(searchCriteria);
			List<MessageSearchVO> userMessageList = this.messageSearchDAO.search(searchCriteria);
			return userMessageList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int resultCount(SearchCriteria searchCriteria) throws CMSException {
		searchCriteria.setSysUserName(this.propertyReader.getSystemNotificationUser());
		this.setDates(searchCriteria);
		return this.messageSearchDAO.resultCount(searchCriteria);
	}

	public List<UserMasterVO> getUsersList(String sentParam, String userName) throws CMSException {
		Object userList = new ArrayList();

		try {
			if (null != sentParam && sentParam.equals("1")) {
				userList = ResourceLocator.self().getUserService().getActiveUserList();
			} else if (null != sentParam && sentParam.equals("2")) {
				userList = ResourceLocator.self().getUserService().getSubOrdinateList(userName);
				UserMasterVO userMaster = new UserMasterVO();
				if (((List) userList).size() > 0) {
					userMaster.setUsername("My Team");
					userMaster.setUserFullName("My Team");
					((List) userList).add(userMaster);
				}

				userMaster = new UserMasterVO();
				userMaster.setUsername(userName);
				userMaster.setUserFullName(ResourceLocator.self().getUserService().getUserInfo(userName).getUsername());
				((List) userList).add(userMaster);
			}

			return (List) userList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
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

	public List<MessageSearchVO> searchForExport(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();

		try {
			excelParamMap.put("sysUserName", this.propertyReader.getSystemNotificationUser());
			this.setDatesMap(excelParamMap);
			List<MessageSearchVO> caseSearchList = this.messageSearchDAO.searchExport(excelParamMap);
			return caseSearchList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<MessageSearchVO> recSearch(SearchCriteria searchCriteria) throws CMSException {
		this.setDates(searchCriteria);
		return this.messageSearchDAO.recSearch(searchCriteria);
	}

	public int recResultCount(SearchCriteria searchCriteria) throws CMSException {
		this.setDates(searchCriteria);
		return this.messageSearchDAO.recResultCount(searchCriteria);
	}

	public List<MessageSearchVO> searchForExportRec(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();

		try {
			this.setDatesMap(excelParamMap);
			List<MessageSearchVO> messageSearchList = this.messageSearchDAO.searchExportRec(excelParamMap);
			return messageSearchList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}