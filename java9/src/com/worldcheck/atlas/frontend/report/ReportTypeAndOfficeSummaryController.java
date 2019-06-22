package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ReportTypeAndOfficeSummaryController extends MultiActionController {
	private static final String MISREPORTTYPEANDOFFICESUMMARY = "misReportTypeAndOfficeSummary";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.ReportTypeAndOfficeSummaryController");
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView reportTypeAndOfficeSummary(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In ReportTypeAndOfficeSummaryController : ReportTypeAndOfficeSummary");

		try {
			ModelAndView modelAndView = new ModelAndView("misReportTypeAndOfficeSummary");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}
}