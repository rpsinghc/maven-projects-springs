package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.AllOnHoldCaseVO;
import com.worldcheck.atlas.vo.report.OverdueReportVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONAllOnHoldCasesMultiactionController extends JSONMultiActionController {
	private String JSONVIEW = "jsonView";
	private String searchResult = "searchResult";
	private AtlasReportService atlasReportService;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONDueTodayMultiactionController");

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView searchAllOnHold(HttpServletRequest request, HttpServletResponse response,
			AllOnHoldCaseVO allOnHoldCaseVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.JSONVIEW);

		try {
			request.setAttribute("reportName", "allOnHoldReport");
			List<OverdueReportVO> reportData = this.atlasReportService.getReport(request, response);
			int count = this.atlasReportService.getTotalCount(request, response);
			modelAndView.addObject(this.searchResult, reportData);
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}
}