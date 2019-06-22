package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.ProductivityPointsAndCasesVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONUtilizationByProductivityController extends JSONController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONUtilizationByProductivityController");
	private AtlasReportService atlasReportService = null;
	private String UTILIZATION_BY_PRODUCTIVITY_LIST = "utilizationByProductivityList";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONUtilizationByProductivityController");
		ModelAndView modelAndView = super.handleRequest(request, response);
		if (null != modelAndView) {
			return modelAndView;
		} else {
			try {
				request.setAttribute("reportName", "utilizationproductivitypointscases");
				List<ProductivityPointsAndCasesVO> utilizationByProductivityList = this.atlasReportService
						.getReport(request, response);
				this.logger.debug("utilizationByProductivityList size :: " + utilizationByProductivityList.size());
				modelAndView = new ModelAndView("jsonView");
				modelAndView.addObject(this.UTILIZATION_BY_PRODUCTIVITY_LIST, utilizationByProductivityList);
				return modelAndView;
			} catch (CMSException var5) {
				return AtlasUtils.getExceptionView(this.logger, var5);
			} catch (Exception var6) {
				return AtlasUtils.getExceptionView(this.logger, var6);
			}
		}
	}
}