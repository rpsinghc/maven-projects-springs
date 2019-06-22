package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.report.ReviewerRawDataVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ReviewerRawDataController extends MultiActionController {
	private AtlasReportService atlasReportService;
	private static PropertyReaderUtil propertyReader;
	private static final String JSP_NAME = "misReviewerRawData";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.ReviewerRawDataController");
	private static final String TRUE = "true";
	private static final String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private static final String CASE_REC_START_DATE = "caseRecStartDate";
	private static final String CASE_REC_END_DATE = "caseRecEndDate";
	private String REDIRECT_URL = "redirect:reviewerReportRawData.do";
	private int COLUMN_COUNT = 21;
	private static final String OFFICE = "Office";
	private static final String CRN = "Crn";
	private static final String CASE_STATUS = "Case Status";
	private static final String CASE_RECEIVED_DATE = "Case Received Date";
	private static final String PROCESS_CYCLE = "Process Cycle";
	private static final String TASK_NAME = "Task Name";
	private static final String REVIEWER = "Reviewer";
	private static final String ANALYST = "Analyst";
	private static final String REJECTION = "Rejection";
	private static final String REVEIWER_COMMENT = "Reviewer Comment";
	private static final String REVIEWER_SHEET_NAME = "Reviewer Raw Data Sheet";
	private static final String REVIEWER_REPORT_NAME = "ReviewerRawData";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public ModelAndView reviewerReportRawData(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in ReviewerRawDataController::viewReviewerRawData");
		modelAndView = new ModelAndView("misReviewerRawData");
		if (null != request.getSession().getAttribute("noRecordsAvailable")) {
			modelAndView.addObject("noRecordsAvailable", "true");
			modelAndView.addObject("caseRecStartDate", request.getSession().getAttribute("caseRecStartDate"));
			modelAndView.addObject("caseRecEndDate", request.getSession().getAttribute("caseRecEndDate"));
			request.getSession().removeAttribute("noRecordsAvailable");
			request.getSession().removeAttribute("caseRecStartDate");
			request.getSession().removeAttribute("caseRecEndDate");
			this.logger.debug("Exiting ReviewerRawDataController::reviewerRawDataController");
			return modelAndView;
		} else {
			return modelAndView;
		}
	}

	public ModelAndView exportToExcelReviewerRawData(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  exportToExcelReviewerRawData ");
		ModelAndView modelandview = null;

		ModelAndView var5;
		try {
			this.logger.debug("date1=" + request.getParameter("caseRecStartDate") + "date2"
					+ request.getParameter("caseRecEndDate") + "selected country "
					+ request.getParameter("officeList"));
			request.setAttribute("reportName", "reviewerRawDataReport");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			if (rowsInResultset > 0) {
				List reviewerRawDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("fetched reviewerRawDataList>>Size is " + reviewerRawDataList.size());
				String fileName = this.writeToExcel(reviewerRawDataList, response);
				modelandview = new ModelAndView("misExcelDownloadPopup");
				modelandview.addObject("fileName", fileName);
			} else {
				modelandview = new ModelAndView(this.REDIRECT_URL);
				request.getSession().setAttribute("noRecordsAvailable", "true");
				this.setupRevRawDataExcel(modelandview, request);
			}

			this.logger.debug("exiting  exportToExcelReviewerRawData ");
			return modelandview;
		} catch (CMSException var12) {
			var5 = AtlasUtils.getExceptionView(this.logger, var12);
		} catch (Exception var13) {
			var5 = AtlasUtils.getExceptionView(this.logger, var13);
			return var5;
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var5;
	}

	public void setupRevRawDataExcel(ModelAndView modelAndView, HttpServletRequest request) throws CMSException {
		this.logger.debug("Inside ReviewerRawDataController::setupRevRawDataExcel");
		request.getSession().setAttribute("caseRecStartDate", request.getParameter("caseRecStartDate"));
		request.getSession().setAttribute("caseRecEndDate", request.getParameter("caseRecEndDate"));
		this.logger.debug("Out ReviewerRawDataController::setupRevRawDataExcel");
	}

	private String writeToExcel(List<ReviewerRawDataVO> reviewerRawDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = this.getFRDHeaderList();
			List<List<String>> dataList = this.getRRDDataMap(reviewerRawDataList);
			return propertyReader.getGenerateExcelOrCsv().equalsIgnoreCase("excel")
					? ExcelDownloader.writeToExcel1(lstHeader, dataList, "Reviewer Raw Data Sheet", (short) 0,
							(short) 1, response, "ReviewerRawData")
					: CSVDownloader.exportCSV(lstHeader, dataList, "ReviewerRawData", response);
		} catch (ClassCastException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private List<String> getFRDHeaderList() {
		List<String> lstHeader = new ArrayList();
		lstHeader.add("Office");
		lstHeader.add("Crn");
		lstHeader.add("Case Status");
		lstHeader.add("Case Received Date");
		lstHeader.add("Process Cycle");
		lstHeader.add("Task Name");
		lstHeader.add("Reviewer");
		lstHeader.add("Analyst");
		lstHeader.add("Rejection");
		lstHeader.add("Reviewer Comment");
		return lstHeader;
	}

	private List<List<String>> getRRDDataMap(List<ReviewerRawDataVO> reviewerRawDataList) {
		this.logger.debug("In ReviewerRawDataController :: getRRDDataMap");
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = reviewerRawDataList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			ReviewerRawDataVO reviewerRawDataVO = (ReviewerRawDataVO) iterator.next();
			datamap.add(String.valueOf(reviewerRawDataVO.getOffice()));
			datamap.add(String.valueOf(reviewerRawDataVO.getCRN()));
			datamap.add(String.valueOf(reviewerRawDataVO.getStatus()));
			datamap.add(String.valueOf(reviewerRawDataVO.getReqDate()));
			datamap.add(String.valueOf(reviewerRawDataVO.getCycle()));
			datamap.add(String.valueOf(reviewerRawDataVO.getTaskName()));
			datamap.add(String.valueOf(reviewerRawDataVO.getReviewer()));
			datamap.add(String.valueOf(reviewerRawDataVO.getAnalyst()));
			datamap.add(String.valueOf(reviewerRawDataVO.getRejection()));
			datamap.add(String.valueOf(reviewerRawDataVO.getComment()));
			dataList.add(datamap);
		}

		return dataList;
	}

	public ModelAndView writeToMultipleExcels(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In ReviewerRawDataController::writeToMultipleExcels");
		ModelAndView modelandview = null;
		List<List<ReviewerRawDataVO>> partedList = new ArrayList();
		String zipFileName = "";

		label118 : {
			ModelAndView var8;
			try {
				modelandview = new ModelAndView("excelDownloadPopupZip");
				request.setAttribute("reportName", "reviewerRawDataReport");
				request.getSession().setAttribute("excelGeneration", new Date());
				List reviewerRawDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("fetched reviewerRawDataList>>Size is " + reviewerRawDataList.size());
				int rowsAccomodated = false;
				int rowsAccomodated = 100000 / this.COLUMN_COUNT - 1;
				List<String> fileNamesList = new ArrayList();
				int N = reviewerRawDataList.size();
				int L = rowsAccomodated;

				int i;
				for (i = 0; i < N; i += L) {
					partedList.add(partedList.size(), reviewerRawDataList.subList(i, Math.min(N, i + L)));
				}

				this.logger.debug("partedList .size " + partedList.size());

				for (i = 0; i < partedList.size(); ++i) {
					String fileName = this.writeToMulipleExcel((List) partedList.get(i), response, "Part" + (i + 1),
							request);
					fileNamesList.add(fileName);
				}

				zipFileName = ExcelDownloader.generateZipFile(fileNamesList, "ReviewerRawData", response);
				modelandview.addObject("fileName", zipFileName);
				break label118;
			} catch (CMSException var17) {
				var8 = AtlasUtils.getExceptionView(this.logger, var17);
			} catch (Exception var18) {
				var8 = AtlasUtils.getExceptionView(this.logger, var18);
				return var8;
			} finally {
				if (null != request.getSession().getAttribute("excelGeneration")) {
					request.getSession().removeAttribute("excelGeneration");
				}

			}

			return var8;
		}

		this.logger.debug("Out ReviewerRawDataController::writeToMultipleExcels");
		return modelandview;
	}

	private String writeToMulipleExcel(List<ReviewerRawDataVO> fRDSubList, HttpServletResponse response,
			String partName, HttpServletRequest request) throws CMSException {
		try {
			this.logger.debug("In ReviewerRawDataController :: writeToMulipleExcel");
			List<String> lstHeader = this.getFRDHeaderList();
			List<List<String>> dataList = this.getRRDDataMap(fRDSubList);
			String fileName;
			if (propertyReader.getGenerateExcelOrCsv().equalsIgnoreCase("excel")) {
				fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, "Reviewer Raw Data Sheet" + partName,
						(short) 0, (short) 1, response, "ReviewerRawData" + partName);
			} else {
				fileName = CSVDownloader.exportCSV(lstHeader, dataList, "ReviewerRawData" + partName, response);
			}

			return fileName;
		} catch (CMSException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}
}