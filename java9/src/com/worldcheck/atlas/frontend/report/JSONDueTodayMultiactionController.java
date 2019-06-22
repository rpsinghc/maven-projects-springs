package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.CaseDueTodayVO;
import com.worldcheck.atlas.vo.report.OverdueReportVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONDueTodayMultiactionController extends JSONMultiActionController {
	private String JSONVIEW = "jsonView";
	private String searchResult = "searchResult";
	private AtlasReportService atlasReportService;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONDueTodayMultiactionController");

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView searchDueToday(HttpServletRequest request, HttpServletResponse response,
			CaseDueTodayVO caseDueTodayVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.JSONVIEW);

		try {
			this.logger.debug("inside DueToday Manager");
			this.logger.debug("==========Start Val" + caseDueTodayVO.getStart());
			this.logger.debug(request.getParameter("start"));
			this.logger.debug("==========Limit Val" + caseDueTodayVO.getLimit());
			this.logger.debug(request.getParameter("limit"));
			request.setAttribute("reportName", "caseDueTodayReport");
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