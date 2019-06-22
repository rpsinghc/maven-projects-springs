package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.report.FinanceRawDataVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FinanceRawDataController extends MultiActionController {
	private AtlasReportService atlasReportService;
	private static PropertyReaderUtil propertyReader;
	private static final String JSP_NAME = "misFinanceRawData";
	private static final String FINANCE_SHEET_NAME = "Finance Raw Data Sheet";
	private static final String FINANCE_REPORT_NAME = "FinanceRawData";
	private static final String TRUE = "true";
	private static final String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private static final String IS_ZIP = "isZip";
	private static final String CASE_REC_START_DATE = "caseRecStartDate";
	private static final String CASE_REC_END_DATE = "caseRecEndDate";
	private static final String CLIENT_FINAL_SENT_START_DATE = "clientFinalSentStartDate";
	private static final String CLIENT_FINAL_SENT_END_DATE = "clientFinalSentEndDate";
	private static final String CRN_HEADER = "CRN";
	private static final String CLIENT_CODE = "Client Code";
	private static final String CLIENT_NAME = "Client Name";
	private static final String CLIENT_REF_NO = "Client's Reference Number";
	private static final String BRANCH_OFFICE = "Branch Office";
	private static final String CLIENT_ADDRESS = "Client Address";
	private static final String REPORT_TYPE = "Report Type";
	private static final String PRIMARY_TEAM_OFFICE = "Primary Team Office";
	private static final String CASE_REC_START_DATE_HDR = "Case Received Date";
	private static final String CLIENT_FINAL_SENT_DATE = "Client Final Sent Date";
	private static final String CASE_MANAGER = "Case Manager";
	private static final String CASE_STATUS = "Case Status";
	private static final String CURRENCY = "Currency";
	private static final String CASE_FEE = "Case Fee";
	private static final String DISBURSEMENT = "Disbursement";
	private static final String INVOICE_VALUE = "Invoice Value";
	private static final String INVOICE_NUMBER = "Invoice Number";
	private static final String INVOICE_DATE = "Invoice Date";
	private static final String COUNTRIES = "List of Subject Names";
	private static final String SUBJECTS = "List of Subject Countries";
	private static final String BULK_ORDER_ID = "Bulk OrderID";
	private static final String DISCOUNT = "Discount";
	private static final String CANCELLED_CHARGES = "Cancelled With Charges";
	private static final String PARENT_CRN = "Parent CRN";
	private static final String SUBREPORT_TYPE_PARENT_CRN = "Sub Report Type_Parent CRN";
	private static final String INVOICE_VALUE_USD = "Invoice value(USD)";
	private static final String CREDIT = "Credit";
	private static final String CREDIT_USD = "Credit(USD)";
	private static final String MULTIPLE_YEAR_BONUS = "Multiple Year Bonus";
	private static final String MULTIPLE_YEAR_BONUS_USD = "Multiple Year Bonus(USD)";
	private static final String TOTAL_PREPAYMENT_CREDIT = "Total Prepayment Credit";
	private static final String TOTAL_SPENT_USD = "Total Spent";
	private static final String TOTAL_BALANCE = "Total Balance";
	private static final String TOTAL_PREPAYMENT_CREDIT_USD = "Total Prepayment Credit(USD)";
	private static final String TOTAL_SPENT = "Total Spent(USD)";
	private static final String TOTAL_BALANCE_USD = "Total Balance(USD)";
	private static final String SALES_REPRESENTATIVE = "Sales Representative";
	private static final String SALES_REPRESENTATIVE_REGION = "Sales Representative Region";
	private static final String SFDC_OTN_NUMBERS = "SFDC OTN NUMBERS";
	private static final String WORk_WEEK = "WORK WEEK";
	private static final String REGION_TERRITORY = "Regional Territory";
	private static final String SALES_MONTH = "Sales Month";
	private static final String CASE_FEE_USD_PLAN_FX = "Case Fee in USD(PLAN FX)";
	private String REDIRECT_URL = "redirect:viewFinanceRawData.do";
	private int COLUMN_COUNT = 21;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.FinanceRawDataController");

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public ModelAndView viewFinanceRawData(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in FinanceRawDataController::viewFinanceRawData");
		modelAndView = new ModelAndView("misFinanceRawData");
		if (null != request.getSession().getAttribute("noRecordsAvailable")) {
			modelAndView.addObject("noRecordsAvailable", "true");
			modelAndView.addObject("caseRecStartDate", request.getSession().getAttribute("caseRecStartDate"));
			modelAndView.addObject("caseRecEndDate", request.getSession().getAttribute("caseRecEndDate"));
			modelAndView.addObject("clientFinalSentStartDate",
					request.getSession().getAttribute("clientFinalSentStartDate"));
			modelAndView.addObject("clientFinalSentEndDate",
					request.getSession().getAttribute("clientFinalSentEndDate"));
			request.getSession().removeAttribute("noRecordsAvailable");
			request.getSession().removeAttribute("caseRecStartDate");
			request.getSession().removeAttribute("caseRecEndDate");
			request.getSession().removeAttribute("clientFinalSentStartDate");
			request.getSession().removeAttribute("clientFinalSentEndDate");
		}

		this.logger.debug("Exiting FinanceRawDataController::viewFinanceRawData");
		return modelAndView;
	}

	public ModelAndView exportToExcelFinanceRawData(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  FinanceRawDataController.exportToExcel ");
		ModelAndView modelandview = null;

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "financeRawDataReport");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			if (rowsInResultset > 0) {
				List financeRawDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("fetched financeRawDataList>>Size is " + financeRawDataList.size());
				String fileName = this.writeToExcel(financeRawDataList, response);
				modelandview = new ModelAndView("misExcelDownloadPopup");
				modelandview.addObject("fileName", fileName);
			} else {
				modelandview = new ModelAndView(this.REDIRECT_URL);
				request.getSession().setAttribute("noRecordsAvailable", "true");
				this.setupFinRawDataExcel(modelandview, request);
			}

			this.logger.debug("exiting  FinanceRawDataController ");
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

	private String writeToExcel(List<FinanceRawDataVO> financeRawDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = this.getFRDHeaderList();
			List<List<String>> dataList = this.getFRDDataMap(financeRawDataList);
			return propertyReader.getGenerateExcelOrCsv().equalsIgnoreCase("excel")
					? ExcelDownloader.writeToExcel1(lstHeader, dataList, "Finance Raw Data Sheet", (short) 0, (short) 1,
							response, "FinanceRawData")
					: CSVDownloader.exportCSV(lstHeader, dataList, "FinanceRawData", response);
		} catch (ClassCastException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void setupFinRawDataExcel(ModelAndView modelAndView, HttpServletRequest request) throws CMSException {
		this.logger.debug("Inside FinanceRawDataController::setupFinRawDataExcel");
		request.getSession().setAttribute("caseRecStartDate", request.getParameter("caseRecStartDate"));
		request.getSession().setAttribute("caseRecEndDate", request.getParameter("caseRecEndDate"));
		request.getSession().setAttribute("clientFinalSentStartDate", request.getParameter("clientFinalSentStartDate"));
		request.getSession().setAttribute("clientFinalSentEndDate", request.getParameter("clientFinalSentEndDate"));
		this.logger.debug("Out FinanceRawDataController::setupFinRawDataExcel");
	}

	public ModelAndView writeToMultipleExcels(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In FinanceRawDataController::writeToMultipleExcels");
		ModelAndView modelandview = null;
		List<List<FinanceRawDataVO>> partedList = new ArrayList();
		String zipFileName = "";

		label118 : {
			ModelAndView var8;
			try {
				modelandview = new ModelAndView("excelDownloadPopupZip");
				request.setAttribute("reportName", "financeRawDataReport");
				request.getSession().setAttribute("excelGeneration", new Date());
				List financeRawDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("fetched financeRawDataList>>Size is " + financeRawDataList.size());
				int rowsAccomodated = false;
				int rowsAccomodated = 100000 / this.COLUMN_COUNT - 1;
				List<String> fileNamesList = new ArrayList();
				int N = financeRawDataList.size();
				int L = rowsAccomodated;

				int i;
				for (i = 0; i < N; i += L) {
					partedList.add(partedList.size(), financeRawDataList.subList(i, Math.min(N, i + L)));
				}

				this.logger.debug("partedList .size " + partedList.size());

				for (i = 0; i < partedList.size(); ++i) {
					String fileName = this.writeToMulipleExcel((List) partedList.get(i), response, "Part" + (i + 1),
							request);
					fileNamesList.add(fileName);
				}

				zipFileName = ExcelDownloader.generateZipFile(fileNamesList, "FinanceRawData", response);
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

		this.logger.debug("Out FinanceRawDataController::writeToMultipleExcels");
		return modelandview;
	}

	private String writeToMulipleExcel(List<FinanceRawDataVO> fRDSubList, HttpServletResponse response, String partName,
			HttpServletRequest request) throws CMSException {
		try {
			this.logger.debug("In FinanceRawDataController :: writeToMulipleExcel");
			List<String> lstHeader = this.getFRDHeaderList();
			List<List<String>> dataList = this.getFRDDataMap(fRDSubList);
			String fileName;
			if (propertyReader.getGenerateExcelOrCsv().equalsIgnoreCase("excel")) {
				fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, "Finance Raw Data Sheet" + partName,
						(short) 0, (short) 1, response, "FinanceRawData" + partName);
			} else {
				fileName = CSVDownloader.exportCSV(lstHeader, dataList, "FinanceRawData" + partName, response);
			}

			return fileName;
		} catch (CMSException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private List<String> getFRDHeaderList() {
		List<String> lstHeader = new ArrayList();
		lstHeader.add("CRN");
		lstHeader.add("Parent CRN");
		lstHeader.add("Sub Report Type_Parent CRN");
		lstHeader.add("Client Code");
		lstHeader.add("Client Name");
		lstHeader.add("Client's Reference Number");
		lstHeader.add("Branch Office");
		lstHeader.add("Client Address");
		lstHeader.add("Report Type");
		lstHeader.add("Primary Team Office");
		lstHeader.add("Case Received Date");
		lstHeader.add("Sales Month");
		lstHeader.add("Client Final Sent Date");
		lstHeader.add("Case Manager");
		lstHeader.add("Case Status");
		lstHeader.add("Currency");
		lstHeader.add("Case Fee");
		lstHeader.add("Case Fee in USD(PLAN FX)");
		lstHeader.add("Disbursement");
		lstHeader.add("Invoice Value");
		lstHeader.add("Discount");
		lstHeader.add("Cancelled With Charges");
		lstHeader.add("Invoice Number");
		lstHeader.add("Invoice Date");
		lstHeader.add("List of Subject Names");
		lstHeader.add("List of Subject Countries");
		lstHeader.add("Bulk OrderID");
		lstHeader.add("Invoice value(USD)");
		lstHeader.add("Credit");
		lstHeader.add("Credit(USD)");
		lstHeader.add("Multiple Year Bonus");
		lstHeader.add("Multiple Year Bonus(USD)");
		lstHeader.add("Total Prepayment Credit");
		lstHeader.add("Total Prepayment Credit(USD)");
		lstHeader.add("Total Spent(USD)");
		lstHeader.add("Total Spent");
		lstHeader.add("Total Balance");
		lstHeader.add("Total Balance(USD)");
		lstHeader.add("Sales Representative");
		lstHeader.add("Sales Representative Region");
		lstHeader.add("SFDC OTN NUMBERS");
		lstHeader.add("WORK WEEK");
		lstHeader.add("Regional Territory");
		return lstHeader;
	}

	private List<List<String>> getFRDDataMap(List<FinanceRawDataVO> financeRawDataList) {
		this.logger.debug("In FinanceRawDataController :: getFRDDataMap");
		List<List<String>> dataList = new ArrayList();
		String subjectsStr = "";
		String subjectCountryStr = "";
		Iterator iterator = financeRawDataList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			FinanceRawDataVO financeRawDataVO = (FinanceRawDataVO) iterator.next();
			datamap.add(String.valueOf(financeRawDataVO.getCRN()));
			datamap.add(String.valueOf(financeRawDataVO.getParentCRN() != null ? financeRawDataVO.getParentCRN() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getSubReportType_Parent_crn() != null
					? financeRawDataVO.getSubReportType_Parent_crn()
					: ""));
			datamap.add(String.valueOf(financeRawDataVO.getClientCode()));
			datamap.add(String.valueOf(financeRawDataVO.getClientName()));
			datamap.add(String.valueOf(financeRawDataVO.getClientRefNo()));
			datamap.add(String.valueOf(financeRawDataVO.getBranchOffice()));
			datamap.add(String
					.valueOf(financeRawDataVO.getClientAddress() != null ? financeRawDataVO.getClientAddress() : ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getReportType() != null ? financeRawDataVO.getReportType() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getPTOffice() != null ? financeRawDataVO.getPTOffice() : ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getCaseReceivedDate() != null ? financeRawDataVO.getCaseReceivedDate() : ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getSalesmonth() != null ? financeRawDataVO.getSalesmonth() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getClientFinalSentDate() != null
					? financeRawDataVO.getClientFinalSentDate()
					: ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getCaseManager() != null ? financeRawDataVO.getCaseManager() : ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getCaseStatus() != null ? financeRawDataVO.getCaseStatus() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getCurrency() != null ? financeRawDataVO.getCurrency() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getCaseFee() != null ? financeRawDataVO.getCaseFee() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getCase_fee_usd_plan_fx() != null
					? financeRawDataVO.getCase_fee_usd_plan_fx()
					: ""));
			datamap.add(String
					.valueOf(financeRawDataVO.getDisbrusement() != null ? financeRawDataVO.getDisbrusement() : ""));
			datamap.add(String
					.valueOf(financeRawDataVO.getInvoiceValue() != null ? financeRawDataVO.getInvoiceValue() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getDiscount() != null ? financeRawDataVO.getDiscount() : ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getCancelledCharges() != null ? financeRawDataVO.getCancelledCharges() : ""));
			datamap.add(String
					.valueOf(financeRawDataVO.getInvoiceNumber() != null ? financeRawDataVO.getInvoiceNumber() : ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getInvoiceDate() != null ? financeRawDataVO.getInvoiceDate() : ""));
			subjectsStr = financeRawDataVO.getSubjects();
			subjectCountryStr = financeRawDataVO.getSubjectCountries();
			if (subjectsStr != null) {
				if (subjectsStr != null && !subjectsStr.isEmpty() && subjectsStr.length() > 0
						&& subjectsStr.length() > 5000) {
					subjectsStr = subjectsStr.substring(0, 5000);
					subjectsStr = subjectsStr + "...";
				}

				datamap.add(String.valueOf(subjectsStr));
			} else {
				datamap.add("");
			}

			if (subjectCountryStr != null) {
				if (subjectCountryStr != null && !subjectCountryStr.isEmpty() && subjectCountryStr.length() > 0
						&& subjectCountryStr.length() > 5000) {
					subjectCountryStr = subjectCountryStr.substring(0, 5000);
					subjectCountryStr = subjectCountryStr + "...";
				}

				datamap.add(String.valueOf(subjectCountryStr));
			} else {
				datamap.add("");
			}

			datamap.add(
					String.valueOf(financeRawDataVO.getBulkOrderID() != null ? financeRawDataVO.getBulkOrderID() : ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getInvoice_amount_usd() != null ? financeRawDataVO.getInvoice_amount_usd() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getCredit() != null ? financeRawDataVO.getCredit() : ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getCredit_usd() != null ? financeRawDataVO.getCredit_usd() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getMultiple_year_bonus() != null
					? financeRawDataVO.getMultiple_year_bonus()
					: ""));
			datamap.add(String.valueOf(financeRawDataVO.getMultiple_year_bonus_usd() != null
					? financeRawDataVO.getMultiple_year_bonus_usd()
					: ""));
			datamap.add(String.valueOf(financeRawDataVO.getTotal_prepayment_credit() != null
					? financeRawDataVO.getTotal_prepayment_credit()
					: ""));
			datamap.add(String.valueOf(financeRawDataVO.getTotal_prepayment_credit_usd() != null
					? financeRawDataVO.getTotal_prepayment_credit_usd()
					: ""));
			datamap.add(
					String.valueOf(financeRawDataVO.getTotal_spent() != null ? financeRawDataVO.getTotal_spent() : ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getTotal_spent_usd() != null ? financeRawDataVO.getTotal_spent_usd() : ""));
			datamap.add(String
					.valueOf(financeRawDataVO.getTotal_balance() != null ? financeRawDataVO.getTotal_balance() : ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getTotal_balance_usd() != null ? financeRawDataVO.getTotal_balance_usd() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getSalesRepresentative() != null
					? financeRawDataVO.getSalesRepresentative()
					: ""));
			datamap.add(String.valueOf(financeRawDataVO.getSalesRepresentativeRegion() != null
					? financeRawDataVO.getSalesRepresentativeRegion()
					: ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getSfdc_Otn_Numbers() != null ? financeRawDataVO.getSfdc_Otn_Numbers() : ""));
			datamap.add(String.valueOf(financeRawDataVO.getWork_week() != null ? financeRawDataVO.getWork_week() : ""));
			datamap.add(String.valueOf(
					financeRawDataVO.getRegional_territory() != null ? financeRawDataVO.getRegional_territory() : ""));
			dataList.add(datamap);
		}

		return dataList;
	}
}