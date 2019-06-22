package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.ExcelDownloadMultiTabVO;
import com.worldcheck.atlas.vo.report.UtilizationByRevenueVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UtilizationByRevenueController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.UtilizationByRevenueController");
	private AtlasReportService atlasReportService;
	private PropertyReaderUtil propertyReader;
	private static final String REPORT_TYPES = "reportTypes";
	private String JSP_NAME = "misUBRView";
	private static final String REDIRECT_VIEW_UBR = "redirect:viewUBR.do";
	private static final String EXCEL_FILE_NAME = "UtilizationByRevenueReport";
	private String SUPERVISOR = "Supervisor";
	private String ANALYST = "Analyst";
	private String CMP_REVENUE = "Completed Revenue";
	private String WIP_REVENUE = "In Progress Revenue";
	private String TOTAL_REVENUE = "Total Revenue";
	private static final String MONTH_FORMAT = "MMM";
	private static final String OFFICE_FROM_REQUEST = "officeFromReq";
	private static final String END_DATE_FROM_REQUEST = "endDateFromReq";
	private static final String START_DATE_FROM_REQUEST = "startDateFromReq";
	private static final String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private static final String OFFICE = "office";
	private static final String END_DATE = "endDate";
	private static final String START_DATE = "startDate";
	private static final String NA_KEY = "NA";
	private static final String KEY_TO_REMOVE1 = "cms.sub7Menu7";
	private static final String KEY_TO_REMOVE2 = "cms.sub7Menu8";
	private static final String REPORT_TO_REMOVE1 = "Current Analyst Loading";
	private static final String REPORT_TO_REMOVE2 = "Utilization by Productivity Points & Cases";
	private int COLUMN_COUNT = 5;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView viewUBR(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in UtilizationByRevenueController.viewUBR");

		try {
			List<String> reportTypeList = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getOfficeCapacityReportTypes());
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			HashMap permissionMap = (HashMap) userDetailsBean.getPermissionMap();
			String val1 = (String) permissionMap.get("cms.sub7Menu7");
			if (null != val1 && val1.trim().length() > 0 && val1.equalsIgnoreCase("NA")) {
				reportTypeList.remove("Current Analyst Loading");
			}

			String val2 = (String) permissionMap.get("cms.sub7Menu8");
			if (null != val2 && val2.trim().length() > 0 && val2.equalsIgnoreCase("NA")) {
				reportTypeList.remove("Utilization by Productivity Points & Cases");
			}

			this.logger.debug("updated reportTypeList size :: " + reportTypeList.size());
			modelAndView = new ModelAndView(this.JSP_NAME);
			modelAndView.addObject("reportTypes", reportTypeList);
			if (request.getSession().getAttribute("noRecordsAvailable") != null) {
				modelAndView.addObject("noRecordsAvailable", true);
				request.getSession().removeAttribute("noRecordsAvailable");
				if (null != request.getSession().getAttribute("isZip")) {
					request.getSession().removeAttribute("isZip");
				}
			}

			if (request.getSession().getAttribute("startDateFromReq") != null) {
				modelAndView.addObject("startDateFromReq", request.getSession().getAttribute("startDateFromReq"));
				request.getSession().removeAttribute("startDateFromReq");
			}

			if (request.getSession().getAttribute("endDateFromReq") != null) {
				modelAndView.addObject("endDateFromReq", request.getSession().getAttribute("endDateFromReq"));
				request.getSession().removeAttribute("endDateFromReq");
			}

			if (request.getSession().getAttribute("officeFromReq") != null) {
				modelAndView.addObject("officeFromReq", request.getSession().getAttribute("officeFromReq"));
				request.getSession().removeAttribute("officeFromReq");
			}

			if (null != request.getSession().getAttribute("isZip")) {
				modelAndView.addObject("isZip", true);
				request.getSession().removeAttribute("isZip");
				if (null != request.getSession().getAttribute("noRecordsAvailable")) {
					request.getSession().removeAttribute("noRecordsAvailable");
				}
			}

			this.logger.debug("exiting UtilizationByRevenueController.viewUBR");
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView generateUBRExcel(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  UtilizationByRevenueController.generateUBRExcel");
		ModelAndView modelAndView = null;

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "utilizationbyrevenue");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			this.logger.debug("rowsInResultset in ubr is :: " + rowsInResultset);
			if (rowsInResultset > 0) {
				int cellsInResultset = rowsInResultset * this.COLUMN_COUNT;
				if (cellsInResultset > 100000) {
					this.logger.debug("export as ZIP");
					request.getSession().setAttribute("isZip", true);
					modelAndView = new ModelAndView("redirect:viewUBR.do");
					request.getSession().setAttribute("startDateFromReq", request.getParameter("startDate"));
					request.getSession().setAttribute("endDateFromReq", request.getParameter("endDate"));
					request.getSession().setAttribute("officeFromReq", request.getParameter("office"));
				} else {
					List utilizationByRevenueList = this.atlasReportService.getReport(request, response);
					String fileName = this.writeToExcel(utilizationByRevenueList, response);
					modelAndView = new ModelAndView("misExcelDownloadPopup");
					modelAndView.addObject("fileName", fileName);
				}
			} else {
				request.getSession().setAttribute("noRecordsAvailable", true);
				modelAndView = new ModelAndView("redirect:viewUBR.do");
				request.getSession().setAttribute("startDateFromReq", request.getParameter("startDate"));
				request.getSession().setAttribute("endDateFromReq", request.getParameter("endDate"));
				request.getSession().setAttribute("officeFromReq", request.getParameter("office"));
			}

			this.logger.debug("exiting  UtilizationByRevenueController.generateUBRExcel");
			return modelAndView;
		} catch (CMSException var12) {
			var5 = AtlasUtils.getExceptionView(this.logger, var12);
			return var5;
		} catch (Exception var13) {
			var5 = AtlasUtils.getExceptionView(this.logger, var13);
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var5;
	}

	private String writeToExcel(List<UtilizationByRevenueVO> utilizationByRevenueList, HttpServletResponse response)
			throws CMSException {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		List<String> headerList = this.getHeaderList();
		List<List<String>> dataList = new ArrayList();
		List<ExcelDownloadMultiTabVO> lstExcelDownloadMultiTabVO = new ArrayList();
		UtilizationByRevenueVO utilizationByRevenueVO = null;
		int previousMonth = 0;
		int previousYear = 0;
		Calendar cal = Calendar.getInstance();
		cal.set(5, 1);
		ExcelDownloadMultiTabVO excelDownloadMultiTabVO = null;

		try {
			Iterator iterator = utilizationByRevenueList.iterator();

			while (iterator.hasNext()) {
				utilizationByRevenueVO = (UtilizationByRevenueVO) iterator.next();
				List<String> datamap = new ArrayList();
				datamap.add(utilizationByRevenueVO.getSupervisor());
				datamap.add(utilizationByRevenueVO.getAnalyst());
				datamap.add(utilizationByRevenueVO.getCmpRevenue().trim());
				datamap.add(utilizationByRevenueVO.getWipRevenue().trim());
				datamap.add(utilizationByRevenueVO.getTtlRevenue().trim());
				if (previousYear != 0 && previousMonth != utilizationByRevenueVO.getMonth()) {
					cal.set(2, previousMonth - 1);
					excelDownloadMultiTabVO = AtlasUtils.getExcelVO(headerList, dataList,
							sdf.format(cal.getTime()) + "-" + previousYear);
					lstExcelDownloadMultiTabVO.add(excelDownloadMultiTabVO);
					dataList = new ArrayList();
					dataList.add(datamap);
					previousMonth = utilizationByRevenueVO.getMonth();
					previousYear = utilizationByRevenueVO.getYear();
				} else {
					previousMonth = utilizationByRevenueVO.getMonth();
					previousYear = utilizationByRevenueVO.getYear();
					dataList.add(datamap);
				}

				if (!iterator.hasNext() && dataList.size() != 0) {
					cal.set(2, previousMonth - 1);
					excelDownloadMultiTabVO = AtlasUtils.getExcelVO(headerList, dataList,
							sdf.format(cal.getTime()) + "-" + previousYear);
					lstExcelDownloadMultiTabVO.add(excelDownloadMultiTabVO);
				}
			}
		} catch (UnsupportedOperationException var14) {
			throw new CMSException(this.logger, var14);
		} catch (ClassCastException var15) {
			throw new CMSException(this.logger, var15);
		} catch (NullPointerException var16) {
			throw new CMSException(this.logger, var16);
		} catch (IllegalArgumentException var17) {
			throw new CMSException(this.logger, var17);
		}

		return ExcelDownloader.writeToMultiTabExcel(lstExcelDownloadMultiTabVO, (short) 0, (short) 1, response,
				"UtilizationByRevenueReport");
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList headerList = new ArrayList();

		try {
			headerList.add(this.SUPERVISOR);
			headerList.add(this.ANALYST);
			headerList.add(this.CMP_REVENUE);
			headerList.add(this.WIP_REVENUE);
			headerList.add(this.TOTAL_REVENUE);
			return headerList;
		} catch (UnsupportedOperationException var3) {
			throw new CMSException(this.logger, var3);
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public ModelAndView generateExcelZipForUBR(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		ArrayList partedList = new ArrayList();

		ModelAndView var7;
		try {
			request.setAttribute("reportName", "utilizationbyrevenue");
			request.getSession().setAttribute("excelGeneration", new Date());
			List<UtilizationByRevenueVO> utilizationByRevenueList = this.atlasReportService.getReport(request,
					response);
			int rowsAccomodated = 100000 / this.COLUMN_COUNT;
			int N = utilizationByRevenueList.size();
			int L = rowsAccomodated - 1;

			for (int i = 0; i < N; i += L) {
				partedList.add(partedList.size(), utilizationByRevenueList.subList(i, Math.min(N, i + L)));
			}

			this.logger.debug("partedList .size " + partedList.size());
			modelandview = new ModelAndView("excelDownloadPopupZip");
			String zipFileName = this.writeToMultipleExcel(partedList, response);
			modelandview.addObject("fileName", zipFileName);
			return modelandview;
		} catch (CMSException var14) {
			var7 = AtlasUtils.getExceptionView(this.logger, var14);
		} catch (Exception var15) {
			var7 = AtlasUtils.getExceptionView(this.logger, var15);
			return var7;
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var7;
	}

	public String writeToMultipleExcel(List<List<UtilizationByRevenueVO>> partedList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToMultipleExcel ");
		new ArrayList();
		String zipFileName = "";

		try {
			List<String> fileNamesList = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM");
			new ArrayList();
			UtilizationByRevenueVO utilizationByRevenueVO = null;
			Calendar cal = Calendar.getInstance();
			cal.set(5, 1);
			ExcelDownloadMultiTabVO excelDownloadMultiTabVO = null;
			List<String> lstHeader = this.getHeaderList();

			for (int i = 0; i < partedList.size(); ++i) {
				List<ExcelDownloadMultiTabVO> lstExcelDownloadMultiTabVO = new ArrayList();
				String partName = "Part" + (i + 1);
				int previousMonth = 0;
				int previousYear = 0;
				List<List<String>> dataList = new ArrayList();
				Iterator iterator = ((List) partedList.get(i)).iterator();

				while (iterator.hasNext()) {
					utilizationByRevenueVO = (UtilizationByRevenueVO) iterator.next();
					List<String> datamap = new ArrayList();
					datamap.add(utilizationByRevenueVO.getSupervisor());
					datamap.add(utilizationByRevenueVO.getAnalyst());
					datamap.add(utilizationByRevenueVO.getCmpRevenue().trim());
					datamap.add(utilizationByRevenueVO.getWipRevenue().trim());
					datamap.add(utilizationByRevenueVO.getTtlRevenue().trim());
					if (previousYear != 0 && previousMonth != utilizationByRevenueVO.getMonth()) {
						cal.set(2, previousMonth - 1);
						excelDownloadMultiTabVO = AtlasUtils.getExcelVO(lstHeader, dataList,
								sdf.format(cal.getTime()) + "-" + previousYear);
						lstExcelDownloadMultiTabVO.add(excelDownloadMultiTabVO);
						dataList = new ArrayList();
						dataList.add(datamap);
						previousMonth = utilizationByRevenueVO.getMonth();
						previousYear = utilizationByRevenueVO.getYear();
					} else {
						previousMonth = utilizationByRevenueVO.getMonth();
						previousYear = utilizationByRevenueVO.getYear();
						dataList.add(datamap);
					}

					if (!iterator.hasNext() && dataList.size() != 0) {
						cal.set(2, previousMonth - 1);
						excelDownloadMultiTabVO = AtlasUtils.getExcelVO(lstHeader, dataList,
								sdf.format(cal.getTime()) + "-" + previousYear);
						lstExcelDownloadMultiTabVO.add(excelDownloadMultiTabVO);
					}
				}

				String fileName = ExcelDownloader.writeToMultiTabExcel(lstExcelDownloadMultiTabVO, (short) 0, (short) 1,
						response, "UtilizationByRevenueReport" + partName);
				fileNamesList.add(fileName);
			}

			zipFileName = ExcelDownloader.generateZipFile(fileNamesList, "UtilizationByRevenueReport", response);
			return zipFileName;
		} catch (UnsupportedOperationException var18) {
			throw new CMSException(this.logger, var18);
		} catch (ClassCastException var19) {
			throw new CMSException(this.logger, var19);
		} catch (NullPointerException var20) {
			throw new CMSException(this.logger, var20);
		} catch (IllegalArgumentException var21) {
			throw new CMSException(this.logger, var21);
		}
	}
}