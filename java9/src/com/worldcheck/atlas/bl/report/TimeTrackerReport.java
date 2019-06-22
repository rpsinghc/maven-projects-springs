package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.TimeTrackerRawDataVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeTrackerReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.TimeTrackerReport");
	private TabularReportDAO tabularReportDAO = null;
	private String OFFICE = "office";
	private String MONTH = "month";
	private String YEAR = "year";
	private String START = "start";
	private String LIMIT = "limit";
	private String START_DATE = "startDate";
	private String END_DATE = "endDate";
	private String SHEET_NAME = "TimeTrackerRawData";
	private String OFFICE_NAME = "Office Name";
	private String USER = "Username";
	private String CRN = "CRN";
	private String REPORT_TYPE = "Report Type";
	private String START_TIME = "Start Time";
	private String END_TIME = "End Time";
	private String TASK_NAME = "Task Name";
	private String TASK_STATUS = "Task Status";
	private String TOTAL_TIME = "Total Time (mins)";
	private String DEFAULT_SORT = "upper(OFFICE),upper(REPORT_TYPE),upper(TASKNAME)";

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List result = null;

		try {
			this.logger.debug("in fetchTimeTrackerReport :: " + request.getAttribute("reportName"));
			HashMap<String, Object> paramMap = null;
			if (((String) request.getAttribute("reportName")).equalsIgnoreCase("timetrackerdatasummary")) {
				this.logger.debug(
						"in request params for data summary :: " + request.getParameter(this.OFFICE) + " :: month :: "
								+ request.getParameter(this.MONTH) + " :: year :: " + request.getParameter(this.YEAR)
								+ " sort column :: " + request.getParameter("sort") + " :: start limit :: "
								+ request.getParameter(this.START) + ":: limit :: " + request.getParameter(this.LIMIT));
				int start = Integer.parseInt(request.getParameter(this.START));
				int limit = Integer.parseInt(request.getParameter(this.LIMIT));
				paramMap = new HashMap();
				paramMap.put(this.OFFICE, request.getParameter(this.OFFICE));
				paramMap.put(this.MONTH, request.getParameter(this.MONTH));
				paramMap.put(this.YEAR, request.getParameter(this.YEAR));
				paramMap.put(this.START, start + 1);
				paramMap.put(this.LIMIT, start + limit);
				if (null != request.getParameter("sort") && request.getParameter("sort").trim().length() > 0) {
					paramMap.put("sort", "upper(" + request.getParameter("sort") + ")");
				} else {
					paramMap.put("sort", this.DEFAULT_SORT);
				}

				paramMap.put("dir", request.getParameter("dir"));
				result = this.tabularReportDAO.fetchTimeTrackerDSReport(paramMap);
			} else {
				this.logger.debug("in request params for raw data:: " + request.getParameter(this.END_DATE) + " :: "
						+ request.getParameter(this.START_DATE));
				paramMap = new HashMap();
				paramMap.put(this.END_DATE, request.getParameter(this.END_DATE));
				paramMap.put(this.START_DATE, request.getParameter(this.START_DATE));
				result = this.tabularReportDAO.fetchTimeTrackerRDReport(paramMap);
			}

			return result;
		} catch (NumberFormatException var7) {
			throw new CMSException(this.logger, var7);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		boolean var3 = false;

		try {
			HashMap<String, Object> paramMap = new HashMap();
			int totalCount;
			if (((String) request.getAttribute("reportName")).equalsIgnoreCase("timetrackerdatasummary")) {
				this.logger.debug("in request params for data summary :: office :: " + request.getParameter(this.OFFICE)
						+ " :: month :: " + request.getParameter(this.MONTH) + " ::year :: "
						+ request.getParameter(this.YEAR));
				paramMap.put(this.OFFICE, request.getParameter(this.OFFICE));
				paramMap.put(this.MONTH, request.getParameter(this.MONTH));
				paramMap.put(this.YEAR, request.getParameter(this.YEAR));
				totalCount = this.tabularReportDAO.fetchTimeTrackerDSCount(paramMap);
			} else {
				this.logger.debug("in request params for raw data:: " + request.getParameter(this.END_DATE) + " :: "
						+ request.getParameter(this.START_DATE));
				paramMap = new HashMap();
				paramMap.put(this.END_DATE, request.getParameter(this.END_DATE));
				paramMap.put(this.START_DATE, request.getParameter(this.START_DATE));
				totalCount = this.tabularReportDAO.fetchTimeTrackerRDCount(paramMap);
			}

			return totalCount;
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public String writeToExcel(List<TimeTrackerRawDataVO> ttRawDataList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToExcel ");
		List<String> lstHeader = this.getHeaderList();
		ArrayList dataList = new ArrayList();

		try {
			this.populateDataList(ttRawDataList, dataList);
		} catch (UnsupportedOperationException var6) {
			throw new CMSException(this.logger, var6);
		} catch (ClassCastException var7) {
			throw new CMSException(this.logger, var7);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (IllegalArgumentException var9) {
			throw new CMSException(this.logger, var9);
		}

		return CSVDownloader.exportCSV(lstHeader, dataList, this.SHEET_NAME, response);
	}

	private void populateDataList(List<TimeTrackerRawDataVO> ttRawDataList, List<List<String>> dataList) {
		TimeTrackerRawDataVO timeTrackerRawDataVO = null;
		Iterator iterator = ttRawDataList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			timeTrackerRawDataVO = (TimeTrackerRawDataVO) iterator.next();
			datamap.add(String.valueOf(timeTrackerRawDataVO.getOffice()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getUserName()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getCRN()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getReportType()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getStartTime()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getEndTime()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getTaskName()));
			datamap.add(String.valueOf(timeTrackerRawDataVO.getTaskStatus()));
			datamap.add(timeTrackerRawDataVO.getTotalTime().toString());
			dataList.add(datamap);
		}

	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add(this.OFFICE_NAME);
			lstHeader.add(this.USER);
			lstHeader.add(this.CRN);
			lstHeader.add(this.REPORT_TYPE);
			lstHeader.add(this.START_TIME);
			lstHeader.add(this.END_TIME);
			lstHeader.add(this.TASK_NAME);
			lstHeader.add(this.TASK_STATUS);
			lstHeader.add(this.TOTAL_TIME);
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

	public String writeToMultipleExcel(List<List<TimeTrackerRawDataVO>> partedList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToMultipleExcel ");
		new ArrayList();
		String zipFileName = "";

		try {
			List<String> fileNamesList = new ArrayList();
			List<String> lstHeader = this.getHeaderList();

			for (int i = 0; i < partedList.size(); ++i) {
				String partName = "Part" + (i + 1);
				List<List<String>> dataList = new ArrayList();
				this.populateDataList((List) partedList.get(i), dataList);
				String fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, this.SHEET_NAME + partName,
						(short) 0, (short) 1, response, this.SHEET_NAME + partName);
				fileNamesList.add(fileName);
			}

			zipFileName = ExcelDownloader.generateZipFile(fileNamesList, this.SHEET_NAME, response);
			return zipFileName;
		} catch (UnsupportedOperationException var10) {
			throw new CMSException(this.logger, var10);
		} catch (ClassCastException var11) {
			throw new CMSException(this.logger, var11);
		} catch (NullPointerException var12) {
			throw new CMSException(this.logger, var12);
		} catch (IllegalArgumentException var13) {
			throw new CMSException(this.logger, var13);
		}
	}
}