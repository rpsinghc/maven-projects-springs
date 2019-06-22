package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.ReportTypeAndOfficeSummaryVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReportTypeAndOfficeSummaryManager implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.report.ReportTypeAndOfficeSummaryManager");
	private TabularReportDAO tabularReportDAO = null;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List reportResult = null;
		this.logger.debug("in ReportTypeAndOfficeSummaryManager: fetchReport");
		reportResult = this.tabularReportDAO.fetchReportTypeAndOffcieSummaryReport(request, response);
		this.logger.debug("reportResult size :: " + reportResult.size());
		return reportResult;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}

	public int fetchOfficeCount(ReportTypeAndOfficeSummaryVO reportTypeAndOfficeSummaryVO) throws CMSException {
		this.logger.debug("In fetchOfficeCount");
		boolean var2 = false;

		int count;
		try {
			new ReportTypeAndOfficeSummaryVO();
			count = this.tabularReportDAO
					.fetchReportTypeAndOffcieSummaryReport_OfficeCount(reportTypeAndOfficeSummaryVO);
		} catch (CMSException var4) {
			throw var4;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("fetchOfficeCount :" + count);
		return count;
	}

	public int fetchReportCount(ReportTypeAndOfficeSummaryVO reportTypeAndOfficeSummaryVO) throws CMSException {
		this.logger.debug("In fetchReportCount");
		boolean var2 = false;

		int count;
		try {
			count = this.tabularReportDAO
					.fetchReportTypeAndOffcieSummaryReport_ReportCount(reportTypeAndOfficeSummaryVO);
		} catch (CMSException var4) {
			throw var4;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("fetchReportCount :" + count);
		return count;
	}
}