package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.VendorRawDataVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VendorRawDataReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.VendorRawDataReport");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List<VendorRawDataVO> reportResult = null;
		this.logger.debug("in VendorRawDataReport");

		try {
			reportResult = this.tabularReportDAO.fetchVendorRawDataReport(request, response);
			this.logger.debug("reportResult size :: " + reportResult.size());
			return reportResult;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("in VendorRawDataReport:: fetchTotalCount");
		int result = this.tabularReportDAO.fetchVendorRawDataReportCount(request, response);
		this.logger.debug("Exiting VendorRawDataReport::fetchTotalCount");
		return result;
	}
}