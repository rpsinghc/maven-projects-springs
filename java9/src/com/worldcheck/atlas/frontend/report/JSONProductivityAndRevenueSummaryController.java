package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.ProductivityAndRevenueSummaryVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONProductivityAndRevenueSummaryController extends JSONController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONProductivityAndRevenueSummaryController");
	private AtlasReportService atlasReportService = null;
	private String SUMMARY_LIST = "productivityAndRevenueSummaryList";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONProductivityAndRevenueSummaryController");
		ModelAndView modelAndView = super.handleRequest(request, response);
		if (null != modelAndView) {
			return modelAndView;
		} else {
			try {
				request.setAttribute("reportName", "productivityrevenuesummary");
				List<ProductivityAndRevenueSummaryVO> productivityAndRevenueSummaryList = this.atlasReportService
						.getReport(request, response);
				this.logger
						.debug("productivityAndRevenueSummaryList size :: " + productivityAndRevenueSummaryList.size());
				modelAndView = new ModelAndView("jsonView");
				modelAndView.addObject(this.SUMMARY_LIST, productivityAndRevenueSummaryList);
				return modelAndView;
			} catch (CMSException var5) {
				return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
			} catch (Exception var6) {
				return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
			}
		}
	}
}