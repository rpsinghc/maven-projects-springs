package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.ChartReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductivityPointAnalystByOfficeReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.report.ProductivityPointAnalystByOfficeReport");
	private ChartReportDAO chartReportDAO;

	public void setChartReportDAO(ChartReportDAO chartReportDAO) {
		this.chartReportDAO = chartReportDAO;
	}

	public List<Object> fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("ProductivityPointAnalystByOfficeReport In fetchReport");
		List list = this.chartReportDAO.productivityPointAnalystByOfficePerReport(request.getParameter("year"));
		this.logger.debug("size:" + list.size());
		return list;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}
}