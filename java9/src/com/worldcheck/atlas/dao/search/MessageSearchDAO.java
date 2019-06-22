package com.worldcheck.atlas.dao.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.search.MessageSearchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class MessageSearchDAO extends SqlMapClientTemplate implements ISearchDAO {
	private static final String MESSAGE_SEARCH_GET_MESSAGE_LIST = "MessageSearch.getUserMessageList";
	private static final String MESSAGE_SEARCH_GET_MESSAGE_COUNT = "MessageSearch.getUserMessageCount";
	private static final String MESSAGE_SEARCH_GETUSERMESSAGESDETAILS = "MessageSearch.getUserMessageDetails";
	private static final String MESSAGE_SEARCH_EXPORT_DATA = "MessageSearch.getExportUserMessage";
	private static final String MESSAGE_SEARCH_GET_REC_MESSAGE_LIST = "MessageSearch.getRecMessageList";
	private static final String MESSAGE_SEARCH_GET_REC_MESSAGE_COUNT = "MessageSearch.getRecMessageCount";
	private static final String MESSAGE_SEARCH_REC_EXPORT_DATA = "MessageSearch.getExportRecMessage";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.search.MessageSearchDAO");

	public List<MessageSearchVO> search(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("MessageSearch.getUserMessageList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("MessageSearch.getUserMessageCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<MessageSearchVO> getUserMessageDetails(SearchCriteria searchCriteria) throws CMSException {
		new ArrayList();

		try {
			List<MessageSearchVO> userNotificationDetailsList = this.queryForList("MessageSearch.getUserMessageDetails",
					searchCriteria);
			return userNotificationDetailsList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<MessageSearchVO> searchExport(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();

		try {
			List<MessageSearchVO> userNotificationDetailsList = this.queryForList("MessageSearch.getExportUserMessage",
					excelParamMap);
			return userNotificationDetailsList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<MessageSearchVO> recSearch(SearchCriteria searchCriteria) throws CMSException {
		List caseSearchVOs = null;

		try {
			caseSearchVOs = this.queryForList("MessageSearch.getRecMessageList", searchCriteria);
			return caseSearchVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int recResultCount(SearchCriteria searchCriteria) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("MessageSearch.getRecMessageCount", searchCriteria);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<MessageSearchVO> searchExportRec(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();

		try {
			List<MessageSearchVO> userNotificationDetailsList = this.queryForList("MessageSearch.getExportRecMessage",
					excelParamMap);
			return userNotificationDetailsList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}