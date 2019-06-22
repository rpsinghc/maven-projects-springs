package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.OfficeSummaryVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONOfficeSummaryController extends JSONController {
	private String OFFICE_SUMMARY_LIST = "officeSummaryList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONOfficeSummaryController");
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONOfficeSummaryController");
		ModelAndView modelAndView = super.handleRequest(request, response);
		if (null != modelAndView) {
			return modelAndView;
		} else {
			try {
				request.setAttribute("reportName", "OfficeSummaryReport");
				List<OfficeSummaryVO> officeSummaryList = this.atlasReportService.getReport(request, response);
				this.logger.debug("OfficeSummarydata list size :: " + officeSummaryList.size());
				modelAndView = new ModelAndView("jsonView");
				modelAndView.addObject(this.OFFICE_SUMMARY_LIST, officeSummaryList);
			} catch (CMSException var5) {
				AtlasUtils.getExceptionView(this.logger, var5);
			} catch (Exception var6) {
				AtlasUtils.getExceptionView(this.logger, var6);
			}

			return modelAndView;
		}
	}
}