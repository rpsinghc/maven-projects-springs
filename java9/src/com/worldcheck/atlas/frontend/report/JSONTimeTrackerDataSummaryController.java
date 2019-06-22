package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.TimeTrackerVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONTimeTrackerDataSummaryController extends JSONController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONTimeTrackerDataSummaryController");
	private AtlasReportService atlasReportService = null;
	private String TIME_TRACKER_DATA_LIST = "timeTrackerDSList";
	private String TOTAL = "total";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTimeTrackerDataSummaryController");
		ModelAndView modelAndView = super.handleRequest(request, response);
		if (null != modelAndView) {
			return modelAndView;
		} else {
			try {
				request.setAttribute("reportName", "timetrackerdatasummary");
				List<TimeTrackerVO> timeTrackerDSList = this.atlasReportService.getReport(request, response);
				this.logger.debug("timeTrackerDSList size :: " + timeTrackerDSList.size());
				int totalCount = this.atlasReportService.getTotalCount(request, response);
				this.logger.debug("totalCount :: " + totalCount);
				modelAndView = new ModelAndView("jsonView");
				modelAndView.addObject(this.TIME_TRACKER_DATA_LIST, timeTrackerDSList);
				modelAndView.addObject(this.TOTAL, totalCount);
				return modelAndView;
			} catch (CMSException var6) {
				return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
			} catch (Exception var7) {
				return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
			}
		}
	}
}