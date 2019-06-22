package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.report.TimeTrackerReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.TimeTrackerRawDataVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TimeTrackerRawDataController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.TimeTrackerRawDataController");
	private PropertyReaderUtil propertyReader;
	private String REPORT_TYPE_LIST = "reportTypes";
	private String JSP = "misTTRawData";
	private String END_DATE_FROM_REQUEST = "endDateFromReq";
	private String START_DATE_FROM_REQUEST = "startDateFromReq";
	private String TRUE = "true";
	private String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private String END_DATE = "endDate";
	private String START_DATE = "startDate";
	private String REDIRECT_URL = "redirect:viewTTRDReport.do";
	private int COLUMN_COUNT = 9;
	private AtlasReportService atlasReportService = null;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView generateExcelForTTRD(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("in  TimeTrackerRawDataController generateExcelForTTRD");
		ModelAndView modelandview = null;

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "timetrackerrawdata");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			this.logger.debug("rowsInResultset in ttrd is :: " + rowsInResultset);
			if (rowsInResultset > 0) {
				new ModelAndView(this.JSP);
				TimeTrackerReport timeTrackerReport = (TimeTrackerReport) this.atlasReportService
						.getReportObject(request);
				List<TimeTrackerRawDataVO> ttRawDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("ttRawDataList size is :: " + ttRawDataList.size());
				String fileName = timeTrackerReport.writeToExcel(ttRawDataList, response);
				modelandview = new ModelAndView("misExcelDownloadPopup");
				modelandview.addObject("fileName", fileName);
			} else {
				modelandview = new ModelAndView(this.REDIRECT_URL);
				request.getSession().setAttribute(this.NO_RECORDS_AVAILABLE, this.TRUE);
				request.getSession().setAttribute(this.START_DATE_FROM_REQUEST, request.getParameter(this.START_DATE));
				request.getSession().setAttribute(this.END_DATE_FROM_REQUEST, request.getParameter(this.END_DATE));
			}

			this.logger.debug("exiting  TimeTrackerRawDataController generateExcelForTTRD ");
			return modelandview;
		} catch (CMSException var13) {
			var5 = AtlasUtils.getExceptionView(this.logger, var13);
		} catch (Exception var14) {
			var5 = AtlasUtils.getExceptionView(this.logger, var14);
			return var5;
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var5;
	}

	public ModelAndView generateExcelZipForTTRD(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		ArrayList partedList = new ArrayList();

		ModelAndView var7;
		try {
			request.setAttribute("reportName", "timetrackerrawdata");
			request.getSession().setAttribute("excelGeneration", new Date());
			List<TimeTrackerRawDataVO> ttRawDataList = this.atlasReportService.getReport(request, response);
			this.logger.debug("fetched ttRawDataList Size is " + ttRawDataList.size());
			int rowsAccomodated = 100000 / this.COLUMN_COUNT;
			int N = ttRawDataList.size();
			int L = rowsAccomodated - 1;

			for (int i = 0; i < N; i += L) {
				partedList.add(partedList.size(), ttRawDataList.subList(i, Math.min(N, i + L)));
			}

			this.logger.debug("partedList .size " + partedList.size());
			TimeTrackerReport timeTrackerReport = (TimeTrackerReport) this.atlasReportService.getReportObject(request);
			modelandview = new ModelAndView("excelDownloadPopupZip");
			String zipFileName = timeTrackerReport.writeToMultipleExcel(partedList, response);
			modelandview.addObject("fileName", zipFileName);
			return modelandview;
		} catch (CMSException var15) {
			var7 = AtlasUtils.getExceptionView(this.logger, var15);
			return var7;
		} catch (Exception var16) {
			var7 = AtlasUtils.getExceptionView(this.logger, var16);
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var7;
	}

	public ModelAndView viewTTRDReport(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in TimeTrackerRawDataController viewTTRDReport");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader.getTimeTrackerReportTypes());
			List<String> reportTypeList = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getTimeTrackerReportTypes());
			modelAndView = new ModelAndView(this.JSP);
			modelAndView.addObject(this.REPORT_TYPE_LIST, reportTypeList);
			if (null != request.getSession().getAttribute(this.NO_RECORDS_AVAILABLE)) {
				modelAndView.addObject(this.NO_RECORDS_AVAILABLE, this.TRUE);
				modelAndView.addObject(this.START_DATE_FROM_REQUEST,
						request.getSession().getAttribute(this.START_DATE_FROM_REQUEST));
				modelAndView.addObject(this.END_DATE_FROM_REQUEST,
						request.getSession().getAttribute(this.END_DATE_FROM_REQUEST));
				request.getSession().removeAttribute(this.NO_RECORDS_AVAILABLE);
				request.getSession().removeAttribute(this.START_DATE_FROM_REQUEST);
				request.getSession().removeAttribute(this.END_DATE_FROM_REQUEST);
				if (null != request.getSession().getAttribute("isZip")) {
					request.getSession().removeAttribute("isZip");
				}
			}

			if (null != request.getSession().getAttribute("isZip")) {
				modelAndView.addObject("isZip", this.TRUE);
				modelAndView.addObject(this.START_DATE_FROM_REQUEST,
						request.getSession().getAttribute(this.START_DATE_FROM_REQUEST));
				modelAndView.addObject(this.END_DATE_FROM_REQUEST,
						request.getSession().getAttribute(this.END_DATE_FROM_REQUEST));
				request.getSession().removeAttribute("isZip");
				request.getSession().removeAttribute(this.START_DATE_FROM_REQUEST);
				request.getSession().removeAttribute(this.END_DATE_FROM_REQUEST);
				if (null != request.getSession().getAttribute(this.NO_RECORDS_AVAILABLE)) {
					request.getSession().removeAttribute(this.NO_RECORDS_AVAILABLE);
				}
			}

			this.logger.debug("exiting TimeTrackerRawDataController viewTTRDReport");
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}
}