package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.ISupportApp;
import com.worldcheck.atlas.dao.masters.SupportAppDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.SupportAppVO;
import java.util.List;
import java.util.Map;

public class SupportAppManager implements ISupportApp {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.SupportAppManager");
	SupportAppDAO supportAppDAO = new SupportAppDAO();

	public void setSupportAppDAO(SupportAppDAO supportAppDAO) {
		this.supportAppDAO = supportAppDAO;
	}

	public SupportAppVO searchCRNDetails(String crn) throws CMSException {
		this.logger.debug("In setSupportAppDAO");
		return this.supportAppDAO.searchCRNDetails(crn);
	}

	public void updateCRNDates(SupportAppVO suppVO) throws CMSException {
		this.supportAppDAO.updateCRNDates(suppVO);
	}

	public String getUserPWD(String userId) throws CMSException {
		return this.supportAppDAO.getUserPWD(userId);
	}

	public List<SupportAppVO> getREDataForExport(Map<String, Object> excelParamMap) throws CMSException {
		this.logger.debug("StartSentDate " + excelParamMap.get("sentDateStart"));
		this.logger.debug("EndSentDate " + excelParamMap.get("sentDateEnd"));
		SupportAppVO supportAppVO = new SupportAppVO();
		supportAppVO.setStartSentDate(excelParamMap.get("sentDateStart").toString());
		supportAppVO.setEndSentDate(excelParamMap.get("sentDateEnd").toString());
		return this.supportAppDAO.getREDataForExport(supportAppVO);
	}

	public List<SupportAppVO> getHistoryDataForExport(Map<String, Object> excelParamMap) throws CMSException {
		this.logger.debug("startCaseReceivedDate " + excelParamMap.get("startCaseReceivedDate"));
		this.logger.debug("endCaseReceivedDate " + excelParamMap.get("endCaseReceivedDate"));
		this.logger.debug("clientCode " + excelParamMap.get("clientCode"));
		SupportAppVO supportAppVO = new SupportAppVO();
		supportAppVO.setStartCaseReceivedDate(excelParamMap.get("startCaseReceivedDate").toString());
		supportAppVO.setEndCaseReceivedDate(excelParamMap.get("endCaseReceivedDate").toString());
		supportAppVO.setClient_code(excelParamMap.get("clientCode").toString());
		return this.supportAppDAO.getHistoryDataForExport(supportAppVO);
	}
}