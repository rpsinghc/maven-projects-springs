package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.report.RevenueSummaryManager;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.RevenueSummaryVO;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class RevenueSummaryMultiActionController extends MultiActionController {
	private static PropertyReaderUtil propertyReader;
	private AtlasReportService atlasReportService;
	private RevenueSummaryManager revenueSummaryManager;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.RevenueSummaryMultiActionController");
	private static final String JSP_NAME = "misRevenueSummary";
	private static final String REPORT_TYPES = "reportTypes";
	private static final String YEARS = "years";
	private static final String CURRENT_YEAR = "currentYear";
	private String REDIRECT_URL = "redirect:revenueSummary.do";
	private static final String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private TabularReportDAO tabularReportDAO;
	private static final String COMBINED_CASE_REVENUE = "combined";
	private static final String REPORT_TYPE_CASE_REVENUE = "reportType";
	private static final String SUB_REPORT_TYPE_CASE_REVENUE = "subReportType";

	public void setRevenueSummaryManager(RevenueSummaryManager revenueSummatyManager) {
		this.revenueSummaryManager = revenueSummatyManager;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
		this.logger.debug("RevenueSummaryMiltiActionController..setAtlasReportService");
	}

	public ModelAndView revenueSummary(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO RevenueSummaryVO) throws Exception {
		this.logger.debug("in  RevenueSummaryMultiActionController. revenueSummary");
		ModelAndView modelandview = null;

		try {
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(propertyReader.getFinanceOverviewReportTypes());
			List<String> years = StringUtils.commaSeparatedStringToList(propertyReader.getYearList());
			this.logger.debug("fetched reportTypelist>>Size is " + reportTypelist.size());
			this.logger.debug("fetched yearsList>>Size is " + years.size());
			modelandview = new ModelAndView("misRevenueSummary");
			modelandview.addObject("reportTypes", reportTypelist);
			modelandview.addObject("years", years);
			modelandview.addObject("currentYear", currentYear);
			if (request.getSession().getAttribute("noRecordsAvailable") != null) {
				modelandview.addObject("noRecordsAvailable", "true");
				request.getSession().removeAttribute("noRecordsAvailable");
			}

			this.logger.debug("exiting RevenueSummaryMultiActionController. revenueSummary");
		} catch (Exception var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		}

		return modelandview;
	}

	public ModelAndView revenueSummaryTopTenClients(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  RevenueSummaryMultiActionController. revenueSummaryTopTenClients");
		ModelAndView modelandview = null;

		try {
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(propertyReader.getFinanceOverviewReportTypes());
			List<String> years = StringUtils.commaSeparatedStringToList(propertyReader.getYearList());
			this.logger.debug("fetched reportTypelist>>Size is " + reportTypelist.size());
			this.logger.debug("fetched yearsList>>Size is " + years.size());
			modelandview = new ModelAndView("misRevenueSummary");
			modelandview.addObject("reportTypes", reportTypelist);
			modelandview.addObject("years", years);
			modelandview.addObject("currentYear", currentYear);
			this.logger.debug("exiting RevenueSummaryMultiActionController. revenueSummaryTopTenClients");
		} catch (Exception var8) {
			AtlasUtils.getExceptionView(this.logger, var8);
		}

		return modelandview;
	}

	public ModelAndView exportToExcelRevenueChartData(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) {
		ModelAndView modelandview = null;
		this.logger.debug("Flag paramaeter from request::" + request.getParameter("flag"));

		try {
			this.logger.debug("Flag paramaeter from request::" + request.getParameter("flag"));
			request.setAttribute("reportName", "invoiceCTExcelReport");
			request.getSession().setAttribute("excelGeneration", new Date());
			int x = this.revenueSummaryManager.getTotalCount(request, response, revenueSummaryVO);
			this.logger.debug("RevenueSummaryMultiActionController::result count::" + x);
			if (x > 0) {
				String fileName = this.revenueSummaryManager.exportChartAndDataToExcel(request, response,
						revenueSummaryVO);
				modelandview = new ModelAndView("misExcelDownloadPopup");
				modelandview.addObject("fileName", fileName);
				this.logger.debug("exiting  exportToExcelRevenueChartData ");
			} else {
				modelandview = new ModelAndView(this.REDIRECT_URL);
				request.getSession().setAttribute("noRecordsAvailable", "true");
				modelandview.addObject("noRecordsAvailable", "true");
			}
		} catch (Exception var10) {
			this.logger.debug("Exception::" + var10.getMessage());
		} finally {
			if (request.getSession().getAttribute("excelGeneration") != null) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return modelandview;
	}
}