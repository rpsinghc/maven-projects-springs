package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.report.RevenueSummaryManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.RevenueSummaryVO;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONRevenueSummaryController extends JSONMultiActionController {
	private static final String CHART_WIDTH_VALUE = "1150";
	private static final String CHART_HEIGHT_VALUE = "400";
	private static final String CHART_WIDTH = "chartWidth";
	private static final String CHART_HEIGHT = "chartHeight";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report. JSONRevenueSummaryController");
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView getDataRevenueSummaryFO(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In getDataRevenueSummaryFO method::");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new HashMap();

		try {
			request.setAttribute("reportName", "FinanceRevenueSummary");
			RevenueSummaryManager revenueSummaryManager = (RevenueSummaryManager) this.atlasReportService
					.getReportObject(request);
			Map<String, Object> controllerDataMap = revenueSummaryManager.getDataRevenueSummaryFO(revenueSummaryVO);
			int labelOne = 0;
			int labelTwo = 0;
			if (revenueSummaryVO.getLabelFirstGraphField().equalsIgnoreCase("true")) {
				labelOne = 1;
			}

			if (revenueSummaryVO.getLabelSecondGraphField().equalsIgnoreCase("true")) {
				labelTwo = 1;
			}

			modelAndView.addObject("xAxisFirstGraph", revenueSummaryVO.getXaxisFirstGraphField());
			modelAndView.addObject("yAxisFirstGraph", revenueSummaryVO.getYaxisFirstGraphField());
			modelAndView.addObject("headerFirstGraph", revenueSummaryVO.getHeaderFirstGraphField());
			modelAndView.addObject("xAxisSecondGraph", revenueSummaryVO.getXaxisSecondGraphField());
			modelAndView.addObject("yAxisSecondGraph", revenueSummaryVO.getYaxisSecondGraphField());
			modelAndView.addObject("headerSecondGraph", revenueSummaryVO.getHeaderSecondGraphField());
			modelAndView.addObject("labelOne", Integer.valueOf(labelOne));
			modelAndView.addObject("labelTwo", Integer.valueOf(labelTwo));
			modelAndView.addObject("chartWidth", "1150");
			modelAndView.addObject("chartHeight", "400");
			modelAndView.addObject("caseTable", controllerDataMap.get("caseTable"));
			modelAndView.addObject("revenueTable", controllerDataMap.get("revenueTable"));
			modelAndView.addObject("casesDataXMLString", controllerDataMap.get("casesDataXMLString"));
			modelAndView.addObject("revenueDataXMLString", controllerDataMap.get("revenueDataXMLString"));
			modelAndView.addObject("isDataPresent", controllerDataMap.get("isDataPresent"));
			modelAndView.addObject("casesPieXMLString", controllerDataMap.get("casesPieXMLString"));
			modelAndView.addObject("revenuePieXMLString", controllerDataMap.get("revenuePieXMLString"));
			modelAndView.addObject("statisticsTable", controllerDataMap.get("statisticsTable"));
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}