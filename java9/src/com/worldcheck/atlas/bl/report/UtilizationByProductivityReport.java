package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.vo.ExcelDownloadMultiTabVO;
import com.worldcheck.atlas.vo.report.ProductivityPointsAndCasesVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilizationByProductivityReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.report.UtilizationByProductivityReport");
	private TabularReportDAO tabularReportDAO = null;
	private String OFFICE = "office";
	private String START_DATE = "startDate";
	private String END_DATE = "endDate";
	private String START_MONTH = "startMonth";
	private String END_MONTH = "endMonth";
	private String START_YEAR = "startYear";
	private String END_YEAR = "endYear";
	private String PRODUCTIVITY_SUMMARY_REPORT = "ProductivitySummaryReport";
	private String SUPERVISOR = "Supervisor";
	private String ANALYST = "Analyst";
	private String COMPLETED_POINTS = "Completed Points";
	private String COMPLETED_CASES = "Completed Cases";
	private String WIP_POINTS = "In Progress Points";
	private String WIP_CASES = "In Progress Cases";
	private String TOTAL_POINTS = "Total Points";
	private String TOTAL_CASES = "Total Cases";

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List<ProductivityPointsAndCasesVO> reportResult = null;
		this.logger.debug("in ProductivityAndRevenueSummaryReport");

		try {
			this.logger.debug("in request params:: " + request.getParameter(this.OFFICE) + " :: "
					+ request.getParameter(this.START_DATE) + " :: " + request.getParameter(this.END_DATE));
			HashMap<String, Object> paramMap = new HashMap();
			String startDate = request.getParameter(this.START_DATE);
			String endDate = request.getParameter(this.END_DATE);
			String[] startDetails = startDate.split(" ");
			String[] endDetails = endDate.split(" ");
			this.logger
					.debug("start details : " + ReportConstants.monthMap.get(startDetails[0]) + " :: " + startDetails[1]
							+ "end details : " + ReportConstants.monthMap.get(endDetails[0]) + " :: " + endDetails[1]);
			paramMap.put(this.OFFICE, request.getParameter(this.OFFICE));
			paramMap.put(this.START_MONTH, ReportConstants.monthMap.get(startDetails[0]));
			paramMap.put(this.START_YEAR, startDetails[1]);
			paramMap.put(this.END_MONTH, ReportConstants.monthMap.get(endDetails[0]));
			paramMap.put(this.END_YEAR, endDetails[1]);
			reportResult = this.tabularReportDAO.fetchUtilizationByProductivityReport(paramMap);
			this.logger.debug("reportResult size :: " + reportResult.size());
			return reportResult;
		} catch (PatternSyntaxException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("in fetchTotalCount");

		try {
			this.logger.debug("in request params:: " + request.getParameter(this.OFFICE) + " :: "
					+ request.getParameter(this.START_DATE) + " :: " + request.getParameter(this.END_DATE));
			HashMap<String, Object> paramMap = new HashMap();
			String startDate = request.getParameter(this.START_DATE);
			String endDate = request.getParameter(this.END_DATE);
			String[] startDetails = startDate.split(" ");
			String[] endDetails = endDate.split(" ");
			this.logger
					.debug("start details : " + ReportConstants.monthMap.get(startDetails[0]) + " :: " + startDetails[1]
							+ "end details : " + ReportConstants.monthMap.get(endDetails[0]) + " :: " + endDetails[1]);
			paramMap.put(this.OFFICE, request.getParameter(this.OFFICE));
			paramMap.put(this.START_MONTH, ReportConstants.monthMap.get(startDetails[0]));
			paramMap.put(this.START_YEAR, startDetails[1]);
			paramMap.put(this.END_MONTH, ReportConstants.monthMap.get(endDetails[0]));
			paramMap.put(this.END_YEAR, endDetails[1]);
			int totalCount = this.tabularReportDAO.utilizationByProductivityCount(paramMap);
			this.logger.debug("totalCount :: " + totalCount);
			return totalCount;
		} catch (PatternSyntaxException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public String writeToExcel(List<ProductivityPointsAndCasesVO> utilizationByProductivityList,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("in  writeToExcel ");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		List<String> lstHeader = this.getHeaderList();
		List<List<String>> dataList = new ArrayList();
		List<ExcelDownloadMultiTabVO> lstExcelDownloadMultiTabVO = new ArrayList();
		ProductivityPointsAndCasesVO productivityPointsAndCasesVO = null;
		int previousMonth = 0;
		int previousYear = 0;
		Calendar cal = Calendar.getInstance();
		cal.set(5, 1);
		ExcelDownloadMultiTabVO excelDownloadMultiTabVO = null;

		try {
			Iterator iterator = utilizationByProductivityList.iterator();

			while (iterator.hasNext()) {
				productivityPointsAndCasesVO = (ProductivityPointsAndCasesVO) iterator.next();
				List<String> datamap = new ArrayList();
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getSupervisor()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getAnalyst()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getCmpPoints()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getCmpCases()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getWipPoints()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getWipCases()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getTotalPoints()));
				datamap.add(String.valueOf(productivityPointsAndCasesVO.getTotalCases()));
				if (previousYear != 0 && previousMonth != productivityPointsAndCasesVO.getMonth()) {
					cal.set(2, previousMonth - 1);
					excelDownloadMultiTabVO = AtlasUtils.getExcelVO(lstHeader, dataList,
							sdf.format(cal.getTime()) + "-" + previousYear);
					lstExcelDownloadMultiTabVO.add(excelDownloadMultiTabVO);
					dataList = new ArrayList();
					dataList.add(datamap);
					previousMonth = productivityPointsAndCasesVO.getMonth();
					previousYear = productivityPointsAndCasesVO.getYear();
				} else {
					previousMonth = productivityPointsAndCasesVO.getMonth();
					previousYear = productivityPointsAndCasesVO.getYear();
					dataList.add(datamap);
				}

				if (!iterator.hasNext() && dataList.size() != 0) {
					cal.set(2, previousMonth - 1);
					excelDownloadMultiTabVO = AtlasUtils.getExcelVO(lstHeader, dataList,
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
				this.PRODUCTIVITY_SUMMARY_REPORT);
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add(this.SUPERVISOR);
			lstHeader.add(this.ANALYST);
			lstHeader.add(this.COMPLETED_POINTS);
			lstHeader.add(this.COMPLETED_CASES);
			lstHeader.add(this.WIP_POINTS);
			lstHeader.add(this.WIP_CASES);
			lstHeader.add(this.TOTAL_POINTS);
			lstHeader.add(this.TOTAL_CASES);
			return lstHeader;
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

	public String writeToMultipleExcel(List<List<ProductivityPointsAndCasesVO>> partedList,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("in  writeToMultipleExcel ");
		new ArrayList();
		String zipFileName = "";

		try {
			List<String> fileNamesList = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM");
			new ArrayList();
			ProductivityPointsAndCasesVO productivityPointsAndCasesVO = null;
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
					productivityPointsAndCasesVO = (ProductivityPointsAndCasesVO) iterator.next();
					List<String> datamap = new ArrayList();
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getSupervisor()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getAnalyst()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getCmpPoints()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getCmpCases()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getWipPoints()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getWipCases()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getTotalPoints()));
					datamap.add(String.valueOf(productivityPointsAndCasesVO.getTotalCases()));
					if (previousYear != 0 && previousMonth != productivityPointsAndCasesVO.getMonth()) {
						cal.set(2, previousMonth - 1);
						excelDownloadMultiTabVO = AtlasUtils.getExcelVO(lstHeader, dataList,
								sdf.format(cal.getTime()) + "-" + previousYear);
						lstExcelDownloadMultiTabVO.add(excelDownloadMultiTabVO);
						dataList = new ArrayList();
						dataList.add(datamap);
						previousMonth = productivityPointsAndCasesVO.getMonth();
						previousYear = productivityPointsAndCasesVO.getYear();
					} else {
						previousMonth = productivityPointsAndCasesVO.getMonth();
						previousYear = productivityPointsAndCasesVO.getYear();
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
						response, this.PRODUCTIVITY_SUMMARY_REPORT + partName);
				fileNamesList.add(fileName);
			}

			zipFileName = ExcelDownloader.generateZipFile(fileNamesList, this.PRODUCTIVITY_SUMMARY_REPORT, response);
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