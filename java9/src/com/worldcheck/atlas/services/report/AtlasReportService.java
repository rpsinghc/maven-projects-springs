package com.worldcheck.atlas.services.report;

import com.worldcheck.atlas.bl.report.AtlasReportFactory;
import com.worldcheck.atlas.bl.report.IAtlasReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtlasReportService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.report.AtlasReportService");
	private AtlasReportFactory atlasReportFactory = null;

	public void setAtlasReportFactory(AtlasReportFactory atlasReportFactory) {
		this.atlasReportFactory = atlasReportFactory;
	}

	public List getReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("report name in service class :: " + request.getAttribute("reportName"));
		List<Object> reportResult = null;
		IAtlasReport reportObj = this.atlasReportFactory.getReportObject((String) request.getAttribute("reportName"));
		reportResult = reportObj.fetchReport(request, response);
		return reportResult;
	}

	public int getTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("report name in service class :: " + request.getAttribute("reportName"));
		int totalCount = false;
		IAtlasReport reportObj = this.atlasReportFactory.getReportObject((String) request.getAttribute("reportName"));
		int totalCount = reportObj.fetchTotalCount(request, response);
		return totalCount;
	}

	public IAtlasReport getReportObject(HttpServletRequest request) {
		this.logger.debug("report name in getReportObject of service class :: " + request.getAttribute("reportName"));
		return this.atlasReportFactory.getReportObject((String) request.getAttribute("reportName"));
	}
}