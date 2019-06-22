package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.report.CTExcelVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CTExcelController extends MultiActionController {
	private AtlasReportService atlasReportService;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.report.CTExcelController");
	private static final String JSP_NAME = "misCTExcelReport";
	private static final String TRUE = "true";
	private static final String NEW_CASE = "NEW CASE";
	private static final String CANCELLED_CASE = "Cancelled";
	private static final String CT_START_DATE = "startCTDate";
	private static final String CT_END_DATE = "endCTDate";
	private static final String REGISTER_START_DATE = "startRegisterDate";
	private static final String REGISTER_END_DATE = "endRegisterDate";
	private static final String CHECK_CAPETOWN = "hchekTownCmb";
	private static final String CRN = "CRNumber";
	private static final String CT_DATE_FILLED = "hctDate";
	private static final String REGISTER_NUMBER = "registerNumber";
	private static final String CASE_STATUS = "hcaseStatus";
	private static final String CASE_STATUS_LIST = "caseStatusList";
	private static final String CTEXCEL_SHEET_NAME = "Invoice CT Excel Sheet";
	private static final String CTEXCEL_REPORT_NAME = "InvoiceCTExcel";
	private static final String NO_RECORDS_AVAILABLE = "noRecordsAvailable";
	private static final String CRN_HEADER = "CRN";
	private static final String CLIENT_CODE = "Client Code";
	private static final String CLIENT_NAME = "Client Name";
	private static final String CLIENT_REF_NO = "Client Reference Number";
	private static final String CASE_CREATION_DATE = "Case Creation Date";
	private static final String FINAL_RPT_SENT_DATE = "Final Report Sent Date";
	private static final String CASE_MGR = "Case Manager";
	private static final String CASE_STATUS_HEADER = "Case Status";
	private static final String CASE_FEE_USD = "Case Fee (in USD)";
	private static final String SUBJECT_NAMES = "Subject Names";
	private static final String COUNTRIES = "Countries";
	private static final String BILLING_ADDRESS = "Billing Address";
	private static final String SPCL_BILLING_INSTRUCTION = "Special Billing Instruction";
	private static final String CURRENCY = "Currency";
	private static final String CASE_FEE = "Case Fee";
	private static final String REG_NO = "Register Number";
	private static final String BILLING_METHOD = "Billing Method";
	private static final String REGISTER_DATE = "Register Date";
	private static final String HANDLE_BY = "Handle By";
	private static final String CT_DATE = "CT Date";
	private static final String INVOICE_NUMBER = "Invoice Number";
	private static final String INV_CURRENCY = "Invoice Currency";
	private static final String INV_AMOUNT = "Invoice Amount";
	private static final String INV_DATE = "Invoice Date";
	private static final String CANCELLED_WITH_CHARGES = "Cancelled With Charges";
	private static final String SFDC_OTN_NUMBERS = "SFDC OTN NUMBERS";
	private static final String WORk_WEEK = "WORK WEEK";
	private static final String REGION_TERRITORY = "Regional Territory";
	private static final String SALES_MONTH = "Sales Month";
	private static final String CASE_FEE_USD_PLAN_FX = "Case Fee in USD(PLAN FX)";
	private String REDIRECT_URL = "redirect:viewCTExcelReport.do";
	private static final String IS_CSV = "isCsv";
	private int COLUMN_COUNT = 19;
	private static final String INVOICE_AMOUNT_USD = "Invoice Value(USD)";
	private static final String CREDIT = "Credit";
	private static final String CREDIT_USD = "Credit(USD)";
	private static final String MULTIPLE_YEAR_BONUS = "Multiple Year Bonus";
	private static final String TOTAL_PREPAYMENT_CREDIT = "Total Prepayment Credit";
	private static final String TOTAL_SPENT = "Total Spent";
	private static final String TOTAL_BALANCE = "Total Balance";
	private static final String TOTAL_PREPAYMENT_CREDIT_USD = "Total Prepayment Credit(USD)";
	private static final String TOTAL_SPENT_USD = "Total Spent(USD)";
	private static final String TOTAL_BALANCE_USD = "Total Balance(USD)";
	private static final String SUB_REPORT_TYPE_PARENT_CRN = "Sub Report Type_Parent CRN";
	private static final String PARENT_CRN = "Parent CRN";
	private static final String MULTIPLE_YEAR_BONUS_USD = "Multiple Year Bonus(USD)";
	private static final String SALES_REPRESENTATIVE = "Sales Representative";
	private static final String SALES_REPRESENTATIVE_REGION = "Sales Representative Region";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView viewCTExcelReport(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in CTExcelController::viewCTExcelReport");
		ModelAndView modelandview = null;

		try {
			List<CTExcelVO> caseStatusList = this.getCaseStatusList();
			modelandview = new ModelAndView("misCTExcelReport");
			modelandview.addObject("caseStatusList", caseStatusList);
			if (null != request.getSession().getAttribute("noRecordsAvailable")) {
				modelandview.addObject("noRecordsAvailable", "true");
				request.getSession().removeAttribute("noRecordsAvailable");
				this.setParamsInModelAndView(request, modelandview);
			}

			this.logger.debug("Exiting CTExcelController::viewCTExcelReport");
			return modelandview;
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView generateExcelOfIncoiceData(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  CTExcelController::generateExcelOfIncoiceData ");
		ModelAndView modelandview = null;

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "invoiceCTExcelReport");
			request.getSession().setAttribute("excelGeneration", new Date());
			int rowsInResultset = this.atlasReportService.getTotalCount(request, response);
			this.logger.debug("rowsInResultset" + rowsInResultset);
			if (rowsInResultset > 0) {
				ArrayList<CTExcelVO> invoiceCTExcelDataList = (ArrayList) this.atlasReportService.getReport(request,
						response);
				this.logger.debug("fetched invoiceCTExcelDataList>>Size is " + invoiceCTExcelDataList.size());
				String fileName = this.writeToExcel(invoiceCTExcelDataList, response);
				modelandview = new ModelAndView("misExcelDownloadPopup");
				modelandview.addObject("fileName", fileName);
			} else {
				modelandview = new ModelAndView(this.REDIRECT_URL);
				request.getSession().setAttribute("noRecordsAvailable", "true");
				this.setupInvoiceCTExcel(modelandview, request);
			}

			this.logger.debug("Exiting  CTExcelController:: generateExcelOfIncoiceData");
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

	private String writeToExcel(List<CTExcelVO> invoiceCTExcelDataList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("In  CTExcelController:: writeToExcel");

		try {
			List<String> lstHeader = this.getCTExcelHeaderList();
			List<List<String>> dataList = this.getCTExcelDataMap(invoiceCTExcelDataList);
			this.logger.debug("Exiting  CTExcelController:: writeToExcel");
			return CSVDownloader.exportCSV(lstHeader, dataList, "InvoiceCTExcel", response);
		} catch (ClassCastException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<CTExcelVO> getCaseStatusList() throws CMSException {
		this.logger.debug("Inside CTExcelController::getCaseStatusList");
		List<CTExcelVO> completeCaseStatusList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("CASE_STATUS_MASTER");
		List<CTExcelVO> caseStatusList = new ArrayList();
		Iterator iterator = completeCaseStatusList.iterator();

		while (iterator.hasNext()) {
			CTExcelVO ctExcelVO = (CTExcelVO) iterator.next();
			if (!ctExcelVO.getCaseStatus().equalsIgnoreCase("NEW CASE")
					&& !ctExcelVO.getCaseStatus().equalsIgnoreCase("Cancelled")) {
				caseStatusList.add(ctExcelVO);
			}
		}

		this.logger.debug("Exit CTExcelController::getCaseStatusList");
		return caseStatusList;
	}

	public void setupInvoiceCTExcel(ModelAndView modelAndView, HttpServletRequest request) throws CMSException {
		this.logger.debug("Inside CTExcelController::setupInvoiceCTExcel");
		request.getSession().setAttribute("startCTDate", request.getParameter("startCTDate"));
		request.getSession().setAttribute("endCTDate", request.getParameter("endCTDate"));
		request.getSession().setAttribute("startRegisterDate", request.getParameter("startRegisterDate"));
		request.getSession().setAttribute("endRegisterDate", request.getParameter("endRegisterDate"));
		request.getSession().setAttribute("hchekTownCmb", request.getParameter("hchekTownCmb"));
		request.getSession().setAttribute("CRNumber", request.getParameter("CRNumber"));
		request.getSession().setAttribute("hctDate", request.getParameter("hctDate"));
		request.getSession().setAttribute("registerNumber", request.getParameter("registerNumber"));
		request.getSession().setAttribute("hcaseStatus", request.getParameter("hcaseStatus"));
		List<CTExcelVO> completeCaseStatusList = this.getCaseStatusList();
		modelAndView.addObject("caseStatusList", completeCaseStatusList);
		this.logger.debug("Exit CTExcelController::setupInvoiceCTExcel");
	}

	public ModelAndView writeToMultipleCTExcels(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside CTExcelController::writeToMultipleExcels");
		ModelAndView modelandview = null;
		List<List<CTExcelVO>> partedList = new ArrayList();
		String zipFileName = "";

		label118 : {
			ModelAndView var8;
			try {
				request.setAttribute("reportName", "invoiceCTExcelReport");
				request.getSession().setAttribute("excelGeneration", new Date());
				List invoiceCTExcelDataList = this.atlasReportService.getReport(request, response);
				this.logger.debug("fetched invoiceCTExcelDataList>>Size is " + invoiceCTExcelDataList.size());
				int rowsAccomodated = false;
				int rowsAccomodated = 100000 / this.COLUMN_COUNT - 1;
				List<String> fileNamesList = new ArrayList();
				int N = invoiceCTExcelDataList.size();
				int L = rowsAccomodated;

				int i;
				for (i = 0; i < N; i += L) {
					partedList.add(partedList.size(), invoiceCTExcelDataList.subList(i, Math.min(N, i + L)));
				}

				this.logger.debug("partedList .size " + partedList.size());

				for (i = 0; i < partedList.size(); ++i) {
					String fileName = this.writeToMulipleExcel((List) partedList.get(i), response, "Part" + (i + 1),
							request);
					fileNamesList.add(fileName);
				}

				modelandview = new ModelAndView("excelDownloadPopupZip");
				zipFileName = ExcelDownloader.generateZipFile(fileNamesList, "InvoiceCTExcel", response);
				modelandview.addObject("fileName", zipFileName);
				break label118;
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

		this.logger.debug("Exiting CTExcelController::writeToMultipleExcels");
		return modelandview;
	}

	private String writeToMulipleExcel(List<CTExcelVO> ctExcelSubList, HttpServletResponse response, String partName,
			HttpServletRequest request) throws CMSException {
		this.logger.debug("In CTExcelController::writeToMulipleExcel");

		try {
			List<String> lstHeader = this.getCTExcelHeaderList();
			List<List<String>> dataList = this.getCTExcelDataMap(ctExcelSubList);
			String fileName = CSVDownloader.exportCSV(lstHeader, dataList, "InvoiceCTExcel" + partName, response);
			this.logger.debug("Exiting CTExcelController::writeToMulipleExcel");
			return fileName;
		} catch (CMSException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private List<String> getCTExcelHeaderList() {
		List<String> lstHeader = new ArrayList();
		lstHeader.add("CRN");
		lstHeader.add("Parent CRN");
		lstHeader.add("Sub Report Type_Parent CRN");
		lstHeader.add("Client Code");
		lstHeader.add("Client Name");
		lstHeader.add("Client Reference Number");
		lstHeader.add("Case Creation Date");
		lstHeader.add("Sales Month");
		lstHeader.add("Final Report Sent Date");
		lstHeader.add("Case Manager");
		lstHeader.add("Case Status");
		lstHeader.add("Case Fee (in USD)");
		lstHeader.add("Subject Names");
		lstHeader.add("Countries");
		lstHeader.add("Billing Address");
		lstHeader.add("Special Billing Instruction");
		lstHeader.add("Currency");
		lstHeader.add("Case Fee");
		lstHeader.add("Case Fee in USD(PLAN FX)");
		lstHeader.add("Register Number");
		lstHeader.add("Billing Method");
		lstHeader.add("Register Date");
		lstHeader.add("Handle By");
		lstHeader.add("Invoice Value(USD)");
		lstHeader.add("Credit");
		lstHeader.add("Credit(USD)");
		lstHeader.add("Multiple Year Bonus");
		lstHeader.add("Total Prepayment Credit");
		lstHeader.add("Total Spent");
		lstHeader.add("Total Balance");
		lstHeader.add("Total Prepayment Credit(USD)");
		lstHeader.add("Total Spent(USD)");
		lstHeader.add("Total Balance(USD)");
		lstHeader.add("Multiple Year Bonus(USD)");
		lstHeader.add("Sales Representative");
		lstHeader.add("Sales Representative Region");
		lstHeader.add("SFDC OTN NUMBERS");
		lstHeader.add("WORK WEEK");
		lstHeader.add("Regional Territory");
		lstHeader.add("Cancelled With Charges");
		lstHeader.add("CT Date");
		lstHeader.add("Invoice Number");
		lstHeader.add("Invoice Currency");
		lstHeader.add("Invoice Amount");
		lstHeader.add("Invoice Date");
		return lstHeader;
	}

	private List<List<String>> getCTExcelDataMap(List<CTExcelVO> invoiceCTExcelDataList) {
		this.logger.debug("In CTExcelController::getCTExcelDataMap");
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = invoiceCTExcelDataList.iterator();

		while (iterator.hasNext()) {
			List<String> subDataList = new ArrayList();
			CTExcelVO ctExcelVO = (CTExcelVO) iterator.next();
			subDataList.add(String.valueOf(ctExcelVO.getCRN()));
			subDataList.add(String.valueOf(ctExcelVO.getParentCRN()));
			subDataList.add(String.valueOf(ctExcelVO.getSubReportTypeParentCRN()));
			subDataList.add(String.valueOf(ctExcelVO.getClientCode()));
			subDataList.add(String.valueOf(ctExcelVO.getClientName()));
			subDataList.add(String.valueOf(ctExcelVO.getClientRefNo() != null ? ctExcelVO.getClientRefNo() : ""));
			subDataList.add(String.valueOf(ctExcelVO.getCaseCreationDate()));
			subDataList.add(String.valueOf(ctExcelVO.getSalesmonth() != null ? ctExcelVO.getSalesmonth() : ""));
			subDataList.add(String.valueOf(ctExcelVO.getFinalRptSentDate()));
			subDataList.add(String.valueOf(ctExcelVO.getCaseManager()));
			subDataList.add(String.valueOf(ctExcelVO.getCaseStatus()));
			subDataList.add(String.valueOf(ctExcelVO.getCaseFeeInUSD()));
			String subjectNames = ctExcelVO.getSubjectNames().replaceAll(",", "\n");
			String countryNames = ctExcelVO.getSubjectCountries().replaceAll(",", "\n");
			if (subjectNames != null && !subjectNames.isEmpty() && subjectNames.length() > 0
					&& subjectNames.length() > 5000) {
				subjectNames = subjectNames.substring(0, 5000);
				subjectNames = subjectNames + "...";
			}

			if (countryNames != null && !countryNames.isEmpty() && countryNames.length() > 0
					&& countryNames.length() > 5000) {
				countryNames = countryNames.substring(0, 5000);
				countryNames = countryNames + "...";
			}

			subDataList.add(String.valueOf(subjectNames));
			subDataList.add(String.valueOf(countryNames));
			subDataList.add(String.valueOf(ctExcelVO.getBillingAddress()));
			String clobValue = ctExcelVO.getSpecialBillingInst();
			if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty()
					&& clobValue.length() > 5000) {
				clobValue = clobValue.substring(0, 5000);
				clobValue = clobValue + "...";
			}

			subDataList.add(String.valueOf(clobValue));
			subDataList.add(String.valueOf(ctExcelVO.getCurrency()));
			subDataList.add(String.valueOf(ctExcelVO.getCaseFee()));
			subDataList.add(String
					.valueOf(ctExcelVO.getCase_fee_usd_plan_fx() != null ? ctExcelVO.getCase_fee_usd_plan_fx() : ""));
			subDataList.add(String.valueOf(ctExcelVO.getRegisterNo()));
			subDataList.add(String.valueOf(ctExcelVO.getBillingMethod()));
			subDataList.add(String.valueOf(ctExcelVO.getRegisterDate()));
			subDataList.add(String.valueOf(ctExcelVO.getHandleBy() != null ? ctExcelVO.getHandleBy() : ""));
			subDataList.add(String.valueOf(ctExcelVO.getInvoiceAmountUSD()));
			subDataList.add(String.valueOf(ctExcelVO.getCredit()));
			subDataList.add(String.valueOf(ctExcelVO.getCreditUSD()));
			subDataList.add(String.valueOf(ctExcelVO.getMultipleYearBonus()));
			subDataList.add(String.valueOf(ctExcelVO.getTotalPrepaymentCredit()));
			subDataList.add(String.valueOf(ctExcelVO.getTotalSpent()));
			subDataList.add(String.valueOf(ctExcelVO.getTotalBalance()));
			subDataList.add(String.valueOf(ctExcelVO.getTotalPrepaymentCreditUSD()));
			subDataList.add(String.valueOf(ctExcelVO.getTotalSpentUSD()));
			subDataList.add(String.valueOf(ctExcelVO.getTotalBalanceUSD()));
			subDataList.add(String.valueOf(ctExcelVO.getMultipleYearBonusUSD()));
			subDataList.add(String.valueOf(ctExcelVO.getSalesRepresentative()));
			subDataList.add(String.valueOf(ctExcelVO.getSalesRepresentativeRegion()));
			subDataList.add(
					String.valueOf(ctExcelVO.getSfdc_Otn_Numbers() != null ? ctExcelVO.getSfdc_Otn_Numbers() : ""));
			subDataList.add(String.valueOf(ctExcelVO.getWork_week() != null ? ctExcelVO.getWork_week() : ""));
			subDataList.add(
					String.valueOf(ctExcelVO.getRegional_territory() != null ? ctExcelVO.getRegional_territory() : ""));
			subDataList.add(String.valueOf(ctExcelVO.getWithCharges()));
			dataList.add(subDataList);
		}

		return dataList;
	}

	private void setParamsInModelAndView(HttpServletRequest request, ModelAndView modelandview) {
		modelandview.addObject("startCTDate", request.getSession().getAttribute("startCTDate"));
		modelandview.addObject("endCTDate", request.getSession().getAttribute("endCTDate"));
		modelandview.addObject("startRegisterDate", request.getSession().getAttribute("startRegisterDate"));
		modelandview.addObject("endRegisterDate", request.getSession().getAttribute("endRegisterDate"));
		modelandview.addObject("hchekTownCmb", request.getSession().getAttribute("hchekTownCmb"));
		modelandview.addObject("CRNumber", request.getSession().getAttribute("CRNumber"));
		modelandview.addObject("hctDate", request.getSession().getAttribute("hctDate"));
		modelandview.addObject("registerNumber", request.getSession().getAttribute("registerNumber"));
		modelandview.addObject("hcaseStatus", request.getSession().getAttribute("hcaseStatus"));
		request.getSession().removeAttribute("startCTDate");
		request.getSession().removeAttribute("endCTDate");
		request.getSession().removeAttribute("startRegisterDate");
		request.getSession().removeAttribute("endRegisterDate");
		request.getSession().removeAttribute("hchekTownCmb");
		request.getSession().removeAttribute("CRNumber");
		request.getSession().removeAttribute("hctDate");
		request.getSession().removeAttribute("registerNumber");
		request.getSession().removeAttribute("hcaseStatus");
	}
}