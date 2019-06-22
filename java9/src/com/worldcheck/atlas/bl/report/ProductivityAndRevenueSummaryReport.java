package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.ProductivityAndRevenueSummaryVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductivityAndRevenueSummaryReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.report.ProductivityAndRevenueSummaryReport");
	private TabularReportDAO tabularReportDAO = null;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List<ProductivityAndRevenueSummaryVO> reportResult = null;
		this.logger.debug("in ProductivityAndRevenueSummaryReport");
		reportResult = this.tabularReportDAO.fetchProductivityRevenueSummaryReport(request, response);
		this.logger.debug("reportResult size :: " + reportResult.size());
		return reportResult;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}
}