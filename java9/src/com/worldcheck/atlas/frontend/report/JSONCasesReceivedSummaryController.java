package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.CasesReceivedSummaryVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCasesReceivedSummaryController extends JSONMultiActionController {
	private static final String NCRS_REPORT_TYPE_LIST = "ncrsReportTypeList";
	private static final String CASES_RECEIVED_SUMMARY_LIST = "casesReceivedSummaryList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONCasesReceivedSummaryController");
	private AtlasReportService atlasReportService;
	private TabularReportDAO tabularReportDAO;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public ModelAndView viewNCRS(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONCasesReceivedSummaryController::viewNCRS");
		ModelAndView modelAndView = null;

		try {
			request.setAttribute("reportName", "casesreceivedsummary");
			List<CasesReceivedSummaryVO> casesReceivedSummaryList = this.atlasReportService.getReport(request,
					response);
			this.logger.debug("casesReceivedSummaryList size :: " + casesReceivedSummaryList.size());
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("casesReceivedSummaryList", casesReceivedSummaryList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getNCRSHeader(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONCasesReceivedSummaryController::getNCRSHeader");
		ModelAndView modelAndView = null;

		try {
			List<String> ncrsReportTypeList = this.tabularReportDAO.fetchCasesReceivedSummaryReportHeader(request,
					response);
			this.logger.debug("ncrsReportTypeList size :: " + ncrsReportTypeList.size());
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("ncrsReportTypeList", ncrsReportTypeList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}
}