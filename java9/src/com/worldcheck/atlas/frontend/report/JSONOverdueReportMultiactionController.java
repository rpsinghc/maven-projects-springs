package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.OverdueReportVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONOverdueReportMultiactionController extends JSONMultiActionController {
	private String JSONVIEW = "jsonView";
	private String levelType = "level";
	private String searchResult = "searchResult";
	private AtlasReportService atlasReportService;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONOverdueReportMultiactionController");

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView searchOverdue(HttpServletRequest request, HttpServletResponse response,
			OverdueReportVO overdueReportVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.JSONVIEW);

		try {
			this.logger.debug(request.getParameter("office"));
			this.logger.debug("IN controler of Overdue Report====>" + request.getParameter(this.levelType));
			this.logger.debug("=========value comming from Vo" + overdueReportVO.getOffice());
			request.setAttribute("reportName", "overdueReport");
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