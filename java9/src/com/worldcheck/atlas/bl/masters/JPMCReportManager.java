package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.dao.report.JPMCReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import com.worldcheck.atlas.vo.masters.JPMCReportAppVO;
import java.util.List;
import java.util.Map;

public class JPMCReportManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.JPMCReportManager");
	JPMCReportDAO jpmcReportDAO = null;

	public void setJpmcReportDAO(JPMCReportDAO jpmcReportDAO) {
		this.jpmcReportDAO = jpmcReportDAO;
	}

	public List<JPMCReportAppVO> getJPMCDataForExport(Map<String, Object> excelParamMap) throws CMSException {
		this.logger.debug("clientCode " + excelParamMap.get("clientCode"));
		JPMCReportAppVO jpmcReportAppVO = new JPMCReportAppVO();
		jpmcReportAppVO.setClient_code(excelParamMap.get("clientCode").toString());
		return this.jpmcReportDAO.getJPMCDataForExport(jpmcReportAppVO);
	}

	public List<MaterializedViewVO> getMaterializedViewRefreshTime(String mvName) throws CMSException {
		return this.jpmcReportDAO.getMaterializedViewRefreshTime(mvName);
	}
}