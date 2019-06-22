package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.VendorRawDataVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class VendorRawDataController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.VendorRawDataController");
	private static final String REDIRECT_VIEW_VEORD = "redirect:viewVEORD.do";
	private static final String JSP_NAME = "misVEORDView";
	private static final String REPORT_TYPES = "reportTypes";
	private static final String SHEET_NAME = "VendorExpOverviewRawData";
	private static final String EXCEL_FILE_NAME = "VendorExpOverviewRawData";
	private static final String CRN = "CRN";
	private static final String PRIMARY_SUB_NAME = "Primary Subject Name";
	private static final String PRIMARY_SUB_COUNTRY = "Primary Subject Country";
	private static final String OTHER_SUBJECTS = "Other Subject Names (Countries)";
	private static final String VENDOR_TYPE = "Vendor Type";
	private static final String VENDOR_NAME = "Vendor Name";
	private static final String VENDOR_STATUS = "Vendor Status";
	private static final String JOB_REQ_SENT_DATE = "Job Request Sent Date";
	private static final String COMMISSIONING_DATE = "Commissioning Date";
	private static final String VENDOR_DUE_DATE = "Vendor Due Date";
	private static final String VENDOR_SUBMISSION_DATE = "Vendor Submission Date";
	private static final String VENDOR_CURRENCY = "Vendor Currency";
	private static final String VENDOR_AMOUNT = "Vendor Amount";
	private static final String CASE_CURRENCY = "Case Currency";
	private static final String CASE_FEE = "Case Fee";
	private static final String VENDOR_MANAGER_MSG = "Vendor Manager Message to Vendor";
	private static final String COMMENTS = "Comments";
	private static final String CREATED_BY = "Created By";
	private static final String CREATED_ON = "Created On";
	private static final String BI_VENDOR_MANAGER = "BI Analyst/Vendor Mgr";
	private static final String CASE_MANAGER = "Case Manager";
	private static final String PRIMARY_TEAM_OFFICE = "Primary Team Office";
	private static final String BI_VENDOR_FINAL_DUE_DATE = "BI/Vendor Final Due Date";
	private static final String PRIMARY_FINAL_DUE_DATE = "Final PT Research Due Date";
	private static final String CASE_STATUS = "Case Status";
	private static final String TASK_STATUS = "Task Status";
	private static final String IMMEDIATE_ATTENTION = "Immediate Attention";
	private static final String NAME_SEARCHED = "Name Searched";
	private static final String COUNTRY = "Country";
	private static final String NDA = "NDA";
	private static final String CONTRACT = "Contract";
	private static final String DEACTIVE_STATUS = "Deactive";
	private static final String ACTIVE_STATUS = "Active";
	private static final String IMMEDIATE_ATTENTION_REQUIRED = "YES";
	private static final String NO_IMMEDIATE_ATTENTION = "";
	private static final String ZERO = "0";
	private static final String ONE = "1";
	private static final String CASE_TYPE_FROM_REQUEST = "caseTypeFromReq";
	private static final String END_DATE_FROM_REQUEST = "endDateFromReq";
	private static final String START_DATE_FROM_REQUEST = "startDateFromReq";
	private static final String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private static final String IS_ZIP = "isZip";
	private static final String CASE_TYPE = "caseType";
	private static final String END_DATE = "endDate";
	private static final String START_DATE = "startDate";
	private int COLUMN_COUNT = 31;
	private AtlasReportService atlasReportService;
	private PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView viewVEORD(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in VendorRawDataController");

		try {
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getVendorExpenditureReportTypes());
			this.logger.debug("reportTypelist size :: " + reportTypelist.size());
			modelAndView = new ModelAndView("misVEORDView");
			modelAndView.addObject("reportTypes", reportTypelist);
			this.logger.debug("exiting VendorRawDataController");
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView generateVEORDExcel(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in VendorRawDataController.generateVEORDExcel");
		ModelAndView modelAndView = null;

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "vendorrawdata");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			if (rowsInResultset > 0) {
				List vendorRawDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("vendorRawDataList size :: " + vendorRawDataList.size());
				String fileName = this.writeToExcel(vendorRawDataList, response);
				modelAndView = new ModelAndView("misExcelDownloadPopup");
				modelAndView.addObject("fileName", fileName);
			} else {
				modelAndView = new ModelAndView("redirect:viewVEORD.do");
				request.getSession().setAttribute("noRecordsAvailable", "true");
				this.setupVendorRDExcel(modelAndView, request);
			}

			this.logger.debug("exiting VendorRawDataController::generateVEORDExcel");
			return modelAndView;
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

	public void setupVendorRDExcel(ModelAndView modelAndView, HttpServletRequest request) throws CMSException {
		this.logger.debug("Inside VendorRawDataController::setupVendorRDExcel");
		request.getSession().setAttribute("startDateFromReq", request.getParameter("startDate"));
		request.getSession().setAttribute("endDateFromReq", request.getParameter("endDate"));
		request.getSession().setAttribute("caseTypeFromReq", request.getParameter("caseType"));
		this.logger.debug("Exit VendorRawDataController::setupVendorRDExcel");
	}

	private String writeToExcel(List<VendorRawDataVO> vendorRawDataList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  VendorRawDataController ::writeToExcel ");
		List<String> lstHeader = this.getHeaderList();
		List<List<String>> dataList = new ArrayList();
		VendorRawDataVO vendorRawDataVO = null;

		try {
			Iterator iterator = vendorRawDataList.iterator();

			while (iterator.hasNext()) {
				List<String> subDataList = new ArrayList();
				vendorRawDataVO = (VendorRawDataVO) iterator.next();
				this.populateDataMap(vendorRawDataVO, subDataList);
				dataList.add(subDataList);
			}
		} catch (UnsupportedOperationException var8) {
			throw new CMSException(this.logger, var8);
		} catch (ClassCastException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IllegalArgumentException var11) {
			throw new CMSException(this.logger, var11);
		}

		this.logger.debug("Exit  VendorRawDataController ::writeToExcel ");
		return CSVDownloader.exportCSV(lstHeader, dataList, "VendorExpOverviewRawData", response);
	}

	private void setParamters(HttpServletRequest request, ModelAndView modelAndView) throws CMSException {
		if (request.getSession().getAttribute("startDateFromReq") != null) {
			modelAndView.addObject("startDateFromReq", request.getSession().getAttribute("startDateFromReq"));
			request.getSession().removeAttribute("startDateFromReq");
		}

		if (request.getSession().getAttribute("endDateFromReq") != null) {
			modelAndView.addObject("endDateFromReq", request.getSession().getAttribute("endDateFromReq"));
			request.getSession().removeAttribute("endDateFromReq");
		}

		if (request.getSession().getAttribute("caseTypeFromReq") != null) {
			modelAndView.addObject("caseTypeFromReq", request.getSession().getAttribute("caseTypeFromReq"));
			request.getSession().removeAttribute("caseTypeFromReq");
		}

	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("CRN");
			lstHeader.add("Primary Subject Name");
			lstHeader.add("Primary Subject Country");
			lstHeader.add("Other Subject Names (Countries)");
			lstHeader.add("Vendor Type");
			lstHeader.add("Vendor Name");
			lstHeader.add("Vendor Status");
			lstHeader.add("Job Request Sent Date");
			lstHeader.add("Commissioning Date");
			lstHeader.add("Vendor Due Date");
			lstHeader.add("Vendor Submission Date");
			lstHeader.add("Vendor Currency");
			lstHeader.add("Vendor Amount");
			lstHeader.add("Case Currency");
			lstHeader.add("Case Fee");
			lstHeader.add("Vendor Manager Message to Vendor");
			lstHeader.add("Comments");
			lstHeader.add("Created By");
			lstHeader.add("Created On");
			lstHeader.add("BI Analyst/Vendor Mgr");
			lstHeader.add("Case Manager");
			lstHeader.add("Primary Team Office");
			lstHeader.add("BI/Vendor Final Due Date");
			lstHeader.add("Final PT Research Due Date");
			lstHeader.add("Case Status");
			lstHeader.add("Task Status");
			lstHeader.add("Immediate Attention");
			lstHeader.add("Name Searched");
			lstHeader.add("Country");
			lstHeader.add("NDA");
			lstHeader.add("Contract");
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

	private void populateDataMap(VendorRawDataVO vendorRawDataVO, List<String> dataList) {
		dataList.add(String.valueOf(vendorRawDataVO.getCrn()));
		dataList.add(String.valueOf(vendorRawDataVO.getPrimarySubName()));
		dataList.add(String.valueOf(vendorRawDataVO.getPrimarySubCountry()));
		String clobValue = vendorRawDataVO.getOtherSubjects();
		if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty() && clobValue.length() > 5000) {
			clobValue = clobValue.substring(0, 5000);
			clobValue = clobValue + "...";
		}

		dataList.add(String.valueOf(clobValue));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorType()));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorName()));
		if (vendorRawDataVO.getVendorStatus() == null) {
			dataList.add(String.valueOf(vendorRawDataVO.getVendorStatus()));
		} else if (vendorRawDataVO.getVendorStatus().equals("1")) {
			dataList.add("Active");
		} else if (vendorRawDataVO.getVendorStatus().equals("0")) {
			dataList.add("Deactive");
		}

		dataList.add(String.valueOf(vendorRawDataVO.getJobRequestSentDate()));
		dataList.add(String.valueOf(vendorRawDataVO.getCommissioningDate()));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorDueDate()));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorSubmissionDate()));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorCurrency()));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorAmount()));
		dataList.add(String.valueOf(vendorRawDataVO.getCaseCurrency()));
		dataList.add(String.valueOf(vendorRawDataVO.getCaseFee()));
		dataList.add(String.valueOf(vendorRawDataVO.getVendorManagerMsg()));
		dataList.add(String.valueOf(vendorRawDataVO.getComments()));
		dataList.add(String.valueOf(vendorRawDataVO.getCreatedBy()));
		dataList.add(String.valueOf(vendorRawDataVO.getCreatedOn()));
		dataList.add(String.valueOf(vendorRawDataVO.getBiVendorManager()));
		dataList.add(String.valueOf(vendorRawDataVO.getCaseManager()));
		dataList.add(String.valueOf(vendorRawDataVO.getPrimaryTeamOffice()));
		dataList.add(String.valueOf(vendorRawDataVO.getBiVendorFinalDueDate()));
		dataList.add(String.valueOf(vendorRawDataVO.getPrimaryFinalDueDate()));
		dataList.add(String.valueOf(vendorRawDataVO.getCaseStatus()));
		dataList.add(String.valueOf(vendorRawDataVO.getTaskStatus()));
		if (vendorRawDataVO.getImmediateAttention() == null) {
			dataList.add(String.valueOf(vendorRawDataVO.getImmediateAttention()));
		} else if (vendorRawDataVO.getImmediateAttention().equals("1")) {
			dataList.add("YES");
		} else if (vendorRawDataVO.getImmediateAttention().equals("0")) {
			dataList.add("");
		}

		dataList.add(String.valueOf(vendorRawDataVO.getNameSearched()));
		dataList.add(String.valueOf(vendorRawDataVO.getCountry()));
		dataList.add(String.valueOf(vendorRawDataVO.getNdaExists()));
		dataList.add(String.valueOf(vendorRawDataVO.getContractExists()));
	}

	public ModelAndView writeToMultipleVRDExcels(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		List<List<VendorRawDataVO>> partedList = new ArrayList();
		String zipFileName = "";

		ModelAndView var8;
		try {
			request.setAttribute("reportName", "vendorrawdata");
			request.getSession().setAttribute("excelGeneration", new Date());
			List vendorRawDataList = this.atlasReportService.getReport(request, response);
			this.logger.debug("fetched vendorRawDataList>>Size is " + vendorRawDataList.size());
			int rowsAccomodated = false;
			int rowsAccomodated = 100000 / this.COLUMN_COUNT - 1;
			List<String> fileNamesList = new ArrayList();
			int N = vendorRawDataList.size();
			int L = rowsAccomodated;

			int i;
			for (i = 0; i < N; i += L) {
				partedList.add(partedList.size(), vendorRawDataList.subList(i, Math.min(N, i + L)));
			}

			this.logger.debug("partedList .size " + partedList.size());

			for (i = 0; i < partedList.size(); ++i) {
				String fileName = this.writeToMulipleExcel((List) partedList.get(i), response, "Part" + (i + 1),
						request);
				fileNamesList.add(fileName);
			}

			modelandview = new ModelAndView("excelDownloadPopupZip");
			zipFileName = ExcelDownloader.generateZipFile(fileNamesList, "VendorExpOverviewRawData", response);
			modelandview.addObject("fileName", zipFileName);
			return modelandview;
		} catch (CMSException var17) {
			var8 = AtlasUtils.getExceptionView(this.logger, var17);
			return var8;
		} catch (Exception var18) {
			var8 = AtlasUtils.getExceptionView(this.logger, var18);
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var8;
	}

	private String writeToMulipleExcel(List<VendorRawDataVO> vRDSubList, HttpServletResponse response, String partName,
			HttpServletRequest request) throws CMSException {
		try {
			this.logger.debug("In VendorRawDataController :: writeToCSV");
			List<String> lstHeader = this.getHeaderList();
			List<List<String>> dataList = new ArrayList();
			VendorRawDataVO vendorRawDataVO = null;
			Iterator iterator = vRDSubList.iterator();

			while (iterator.hasNext()) {
				List<String> subDataList = new ArrayList();
				vendorRawDataVO = (VendorRawDataVO) iterator.next();
				this.populateDataMap(vendorRawDataVO, subDataList);
				dataList.add(subDataList);
			}

			String filename = CSVDownloader.exportCSV(lstHeader, dataList, "VendorExpOverviewRawData" + partName,
					response);
			return filename;
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