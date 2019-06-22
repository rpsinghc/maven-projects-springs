package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.OfficeSummaryVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OfficeSummaryReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.OfficeSummaryReport");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		new ArrayList();
		this.logger.debug("in OfficeSummaryReport");
		List<OfficeSummaryVO> reportResult = this.tabularReportDAO.fetchOfficeSummaryReport(request, response);
		this.logger.debug("Exiting OfficeSummaryReport");
		return reportResult;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}
}