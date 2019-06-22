package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.report.ReportTypeAndOfficeSummaryManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.ReportTypeAndOfficeSummaryVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONReportTypeAndOfficeSummaryController extends JSONMultiActionController {
	private static final String BLANK = "";
	private static final String CASETYPE = "caseType";
	private static final String STARTDATE = "startDate";
	private static final String ENDDATE = "endDate";
	private static final String REPORTTYPEREVENUELIST = "reportTypeRevenueList";
	private static final String OFFICEWISEREVENUELIST = "officeWiseRevenueList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONReportTypeAndOfficeSummaryController");
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView viewReportTypeAndOfficeSummary(HttpServletRequest request, HttpServletResponse response,
			ReportTypeAndOfficeSummaryVO reportTypeAndOfficeSummaryVO) throws Exception {
		this.logger.debug("In JSONReportTypeAndOfficeSummaryController : viewReportTypeAndOfficeSummary");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		ArrayList<ReportTypeAndOfficeSummaryVO> reportTypeRevenueVO = new ArrayList();
		ArrayList officeWiseRevenueVO = new ArrayList();

		try {
			request.setAttribute("reportName", "reportTypeAndOfficeSummary");
			ReportTypeAndOfficeSummaryManager rtosManager = (ReportTypeAndOfficeSummaryManager) this.atlasReportService
					.getReportObject(request);
			if (reportTypeAndOfficeSummaryVO.getCaseType() != null
					&& !reportTypeAndOfficeSummaryVO.getCaseType().equalsIgnoreCase("")
					&& (reportTypeAndOfficeSummaryVO.getStartDate() != null
							&& !reportTypeAndOfficeSummaryVO.getStartDate().equalsIgnoreCase("")
							|| reportTypeAndOfficeSummaryVO.getEndDate() != null
									&& !reportTypeAndOfficeSummaryVO.getEndDate().equalsIgnoreCase(""))) {
				request.setAttribute("caseType", reportTypeAndOfficeSummaryVO.getCaseType());
				request.setAttribute("startDate", reportTypeAndOfficeSummaryVO.getStartDate());
				request.setAttribute("endDate", reportTypeAndOfficeSummaryVO.getEndDate());
				List<List<ReportTypeAndOfficeSummaryVO>> resultList = rtosManager.fetchReport(request, response);
				reportTypeRevenueVO = (ArrayList) resultList.get(0);
				officeWiseRevenueVO = (ArrayList) resultList.get(1);
				modelAndView.addObject("OfficeTotal", rtosManager.fetchOfficeCount(reportTypeAndOfficeSummaryVO));
				modelAndView.addObject("ReportTotal", rtosManager.fetchReportCount(reportTypeAndOfficeSummaryVO));
			}

			modelAndView.addObject("reportTypeRevenueList", reportTypeRevenueVO);
			modelAndView.addObject("officeWiseRevenueList", officeWiseRevenueVO);
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}