package com.worldcheck.atlas.bl.audit;

import com.worldcheck.atlas.bl.interfaces.IRiskHistoryEvents;
import com.worldcheck.atlas.dao.audit.RiskHistoryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiskHistoryEventManager implements IRiskHistoryEvents {
	private String BREAKLINE = "<br />";
	private String BOLDSTART = "<b>";
	private String BOLDEND = "</b>";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.audit.RiskHistoryEventManager");
	private RiskHistoryDAO riskHistoryDAO;

	public void setRiskHistoryDAO(RiskHistoryDAO riskHistoryDAO) {
		this.riskHistoryDAO = riskHistoryDAO;
	}

	public List<RiskHistory> getRiskHistory(String CRN, String riskCode, long subID, int start, int limit,
			String columnName, String sortType, List<String> roleList) throws CMSException {
		this.logger.debug("IN RiskHistoryEventManager::getRiskHistory");
		new ArrayList();
		new ArrayList();
		new ArrayList();
		HashMap paramList = new HashMap();

		List riskHistoryList;
		try {
			paramList.put("CRN", CRN);
			paramList.put("riskCode", riskCode);
			paramList.put("subID", subID);
			paramList.put("start", new Integer(start + 1));
			paramList.put("limit", new Integer(start + limit));
			paramList.put("sort", columnName);
			paramList.put("dir", sortType);
			riskHistoryList = this.riskHistoryDAO.getRiskHistoryDetails(paramList);
			this.logger.debug("riskProfileHistoryList length::" + riskHistoryList.size());
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}

		this.logger.debug("IN RiskHistoryEventManager::getRiskHistory");
		return riskHistoryList;
	}

	public Object getRiskHistoryCountForRiskCode(String CRN, String riskCode, long subID) throws CMSException {
		this.logger.debug("IN RiskHistoryEventManager::getRiskHistoryCountForRiskCode");
		int count = false;
		HashMap paramList = new HashMap();

		int count;
		try {
			paramList.put("crn", CRN);
			paramList.put("riskCode", riskCode);
			paramList.put("subID", subID);
			count = this.riskHistoryDAO.getRiskHistoryCountForRiskCode(paramList);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("Exiting RiskHistoryEventManager::getRiskHistoryCountForRiskCode");
		return count;
	}
}