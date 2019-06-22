package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.UtilizationByRevenueVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilizationByRevenueReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.report.UtilizationByRevenueReport");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List<UtilizationByRevenueVO> reportResult = null;
		this.logger.debug("in UtilizationByRevenueReport");

		try {
			reportResult = this.tabularReportDAO.fetchUtilizationByRevenueReport(request, response);
			return reportResult;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("in fetchTotalCount of UtilizationByRevenueReport");

		try {
			int totalCount = this.tabularReportDAO.utilizationByRevenueCount(request, response);
			return totalCount;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}