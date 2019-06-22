package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.ChartReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvoiceTATReportManager implements IAtlasReport {
	private static final String MONTH = "month";
	private static final String YEAR = "year";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.InvoiceTATReportManager");
	private ChartReportDAO chartReportDAO = null;

	public void setChartReportDAO(ChartReportDAO chartReportDAO) {
		this.chartReportDAO = chartReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("In fetchReport");
		HashMap<String, String> hmap = new HashMap();
		hmap.put("month", request.getParameter("month"));
		hmap.put("year", request.getParameter("year"));
		return this.chartReportDAO.fetchInvoiceTATReport(hmap);
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}
}