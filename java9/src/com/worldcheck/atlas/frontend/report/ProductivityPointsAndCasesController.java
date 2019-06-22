package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.report.UtilizationByProductivityReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.ProductivityPointsAndCasesVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ProductivityPointsAndCasesController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.ProductivityPointsAndCasesController");
	private PropertyReaderUtil propertyReader;
	private String REPORT_TYPE_LIST = "reportTypes";
	private String JSP = "misUBPPCView";
	private String REPORT_TO_REMOVE = "Utilization by Revenue";
	private String KEY_TO_REMOVE = "cms.sub7Menu9";
	private String NA_KEY = "NA";
	private AtlasReportService atlasReportService = null;
	private String OFFICE_FROM_REQUEST = "officeFromReq";
	private String END_DATE_FROM_REQUEST = "endDateFromReq";
	private String START_DATE_FROM_REQUEST = "startDateFromReq";
	private String TRUE = "true";
	private String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private String OFFICE = "office";
	private String END_DATE = "endDate";
	private String START_DATE = "startDate";
	private String REDIRECT_URL = "redirect:viewUBPPC.do";
	private int COLUMN_COUNT = 8;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView viewUBPPC(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in ProductivityPointsAndCasesController");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader.getOfficeCapacityReportTypes());
			List<String> reportTypeList = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getOfficeCapacityReportTypes());
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			HashMap permissionMap = null;
			permissionMap = (HashMap) userDetailsBean.getPermissionMap();
			String val = (String) permissionMap.get(this.KEY_TO_REMOVE);
			if (null != val && val.trim().length() > 0 && val.equalsIgnoreCase(this.NA_KEY)) {
				reportTypeList.remove(this.REPORT_TO_REMOVE);
			}

			this.logger.debug("updated reportTypeList size :: " + reportTypeList.size() + " :: val :: " + val);
			modelAndView = new ModelAndView(this.JSP);
			modelAndView.addObject(this.REPORT_TYPE_LIST, reportTypeList);
			if (null != request.getSession().getAttribute(this.NO_RECORDS_AVAILABLE)) {
				modelAndView.addObject(this.NO_RECORDS_AVAILABLE, this.TRUE);
				modelAndView.addObject(this.START_DATE_FROM_REQUEST,
						request.getSession().getAttribute(this.START_DATE_FROM_REQUEST));
				modelAndView.addObject(this.END_DATE_FROM_REQUEST,
						request.getSession().getAttribute(this.END_DATE_FROM_REQUEST));
				modelAndView.addObject(this.OFFICE_FROM_REQUEST,
						request.getSession().getAttribute(this.OFFICE_FROM_REQUEST));
				request.getSession().removeAttribute(this.NO_RECORDS_AVAILABLE);
				request.getSession().removeAttribute(this.START_DATE_FROM_REQUEST);
				request.getSession().removeAttribute(this.END_DATE_FROM_REQUEST);
				request.getSession().removeAttribute(this.OFFICE_FROM_REQUEST);
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
				modelAndView.addObject(this.OFFICE_FROM_REQUEST,
						request.getSession().getAttribute(this.OFFICE_FROM_REQUEST));
				request.getSession().removeAttribute("isZip");
				request.getSession().removeAttribute(this.START_DATE_FROM_REQUEST);
				request.getSession().removeAttribute(this.END_DATE_FROM_REQUEST);
				request.getSession().removeAttribute(this.OFFICE_FROM_REQUEST);
				if (null != request.getSession().getAttribute(this.NO_RECORDS_AVAILABLE)) {
					request.getSession().removeAttribute(this.NO_RECORDS_AVAILABLE);
				}
			}

			this.logger.debug("exiting ProductivityPointsAndCasesController");
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView generateExcelForUBPPC(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  generateExcelForUBPPC");
		ModelAndView modelandview = null;

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "utilizationproductivitypointscases");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			this.logger.debug("rowsInResultset in ubppc is :: " + rowsInResultset);
			if (rowsInResultset > 0) {
				int cellsInResultset = rowsInResultset * this.COLUMN_COUNT;
				if (cellsInResultset > 100000) {
					this.logger.debug("export as ZIP");
					request.getSession().setAttribute("isZip", this.TRUE);
					modelandview = new ModelAndView(this.REDIRECT_URL);
					request.getSession().setAttribute(this.START_DATE_FROM_REQUEST,
							request.getParameter(this.START_DATE));
					request.getSession().setAttribute(this.END_DATE_FROM_REQUEST, request.getParameter(this.END_DATE));
					request.getSession().setAttribute(this.OFFICE_FROM_REQUEST, request.getParameter(this.OFFICE));
				} else {
					List<ProductivityPointsAndCasesVO> utilizationByProductivityList = this.atlasReportService
							.getReport(request, response);
					this.logger
							.debug("utilizationByProductivityList size is :: " + utilizationByProductivityList.size());
					UtilizationByProductivityReport utilizationReport = (UtilizationByProductivityReport) this.atlasReportService
							.getReportObject(request);
					String fileName = utilizationReport.writeToExcel(utilizationByProductivityList, response);
					modelandview = new ModelAndView("misExcelDownloadPopup");
					modelandview.addObject("fileName", fileName);
				}
			} else {
				modelandview = new ModelAndView(this.REDIRECT_URL);
				request.getSession().setAttribute(this.NO_RECORDS_AVAILABLE, this.TRUE);
				request.getSession().setAttribute(this.START_DATE_FROM_REQUEST, request.getParameter(this.START_DATE));
				request.getSession().setAttribute(this.END_DATE_FROM_REQUEST, request.getParameter(this.END_DATE));
				request.getSession().setAttribute(this.OFFICE_FROM_REQUEST, request.getParameter(this.OFFICE));
			}

			this.logger.debug("exiting  ProductivityPointsAndCasesController generateExcelForUBPPC ");
			return modelandview;
		} catch (CMSException var13) {
			var5 = AtlasUtils.getExceptionView(this.logger, var13);
			return var5;
		} catch (Exception var14) {
			var5 = AtlasUtils.getExceptionView(this.logger, var14);
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var5;
	}

	public ModelAndView generateExcelZipForUBPPC(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		ArrayList partedList = new ArrayList();

		ModelAndView var7;
		try {
			request.setAttribute("reportName", "utilizationproductivitypointscases");
			request.getSession().setAttribute("excelGeneration", new Date());
			List<ProductivityPointsAndCasesVO> utilizationByProductivityList = this.atlasReportService
					.getReport(request, response);
			this.logger.debug("fetched utilizationByProductivityList Size is " + utilizationByProductivityList.size());
			int rowsAccomodated = 100000 / this.COLUMN_COUNT;
			int N = utilizationByProductivityList.size();
			int L = rowsAccomodated - 1;

			for (int i = 0; i < N; i += L) {
				partedList.add(partedList.size(), utilizationByProductivityList.subList(i, Math.min(N, i + L)));
			}

			this.logger.debug("partedList .size " + partedList.size());
			UtilizationByProductivityReport utilizationReport = (UtilizationByProductivityReport) this.atlasReportService
					.getReportObject(request);
			modelandview = new ModelAndView("excelDownloadPopupZip");
			String zipFileName = utilizationReport.writeToMultipleExcel(partedList, response);
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
}