package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;

public class InvoiceCTExcelReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.InvoiceCTExcelReport");
	private String START_CT_DATE = "startCTDate";
	private String END_CT_DATE = "endCTDate";
	private String START_REGISTER_DATE = "startRegisterDate";
	private String END_REGISTER_DATE = "endRegisterDate";
	private String CHECK_TOWN_CMB = "hchekTownCmb";
	private String CRNUMBER = "CRNumber";
	private String CT_DATE_FILLED = "hctDate";
	private String REGISTER_NUMBER = "registerNumber";
	private String CASE_STATUS = "hcaseStatus";
	private String WITH_CHARGES = "withCharges";
	static SimpleDateFormat sdfCTExcel = new SimpleDateFormat("MM/dd/yyyy");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");
	private static final SimpleDateFormat sdfCTExcel1 = new SimpleDateFormat("dd MMM yyyy");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		new ArrayList();
		this.logger.debug("in InvoiceCTExcelReport::fetchReport");

		List reportResult;
		try {
			HashMap<String, String> paramList = this.setParamaters(request);
			reportResult = this.tabularReportDAO.fetchInvoiceCTExcelReport(paramList);
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("exiting InvoiceCTExcelReport::fetchReport");
		return reportResult;
	}

	private HashMap<String, String> setParamaters(HttpServletRequest request) throws CMSException {
		String ctStartDate = request.getParameter(this.START_CT_DATE);
		String ctEndDate = request.getParameter(this.END_CT_DATE);
		String registerStartDate = request.getParameter(this.START_REGISTER_DATE);
		String registerEndDate = request.getParameter(this.END_REGISTER_DATE);
		String chekTownCmb = request.getParameter(this.CHECK_TOWN_CMB);
		String CRN = request.getParameter(this.CRNUMBER);
		String ctDateFilled = request.getParameter(this.CT_DATE_FILLED);
		String registerNumber = request.getParameter(this.REGISTER_NUMBER);
		String withCharges = request.getParameter(this.WITH_CHARGES);
		String[] caseStatus_array = request.getParameterValues(this.CASE_STATUS);
		String selected = "";
		List<String> list = Arrays.asList(caseStatus_array);

		int index;
		for (index = 0; index < caseStatus_array.length; ++index) {
			if (list.indexOf(caseStatus_array[index]) != -1 && !((String) list.get(index)).equals("Cancelled")) {
				selected = "'" + caseStatus_array[index] + "'," + selected;
			}
		}

		index = selected.length();
		String caseStatus = "";
		if (index != 0) {
			caseStatus = selected.substring(0, index - 1);
			this.logger.debug("SELECTED CASES in COUNT::" + caseStatus);
		}

		new ArrayList();
		HashMap paramList = new HashMap();

		try {
			if (request.getParameterValues(this.CASE_STATUS) == null) {
				throw new NullPointerException();
			} else {
				if (ctStartDate != null && !ctStartDate.isEmpty()) {
					paramList.put(this.START_CT_DATE, sdf1.format(sdfCTExcel1.parse(ctStartDate)));
				} else {
					paramList.put(this.START_CT_DATE, "");
				}

				if (ctEndDate != null && !ctEndDate.isEmpty()) {
					paramList.put(this.END_CT_DATE, sdf1.format(sdfCTExcel1.parse(ctEndDate)));
				} else {
					paramList.put(this.END_CT_DATE, "");
				}

				if (registerStartDate != null && !registerStartDate.isEmpty()) {
					paramList.put(this.START_REGISTER_DATE, sdf1.format(sdfCTExcel1.parse(registerStartDate)));
				} else {
					paramList.put(this.START_REGISTER_DATE, "");
				}

				if (registerEndDate != null && !registerEndDate.isEmpty()) {
					paramList.put(this.END_REGISTER_DATE, sdf1.format(sdfCTExcel1.parse(registerEndDate)));
				} else {
					paramList.put(this.END_REGISTER_DATE, "");
				}

				if (chekTownCmb != null && !chekTownCmb.isEmpty()) {
					if (chekTownCmb.equalsIgnoreCase("Both")) {
						paramList.put(this.CHECK_TOWN_CMB, "");
					} else {
						paramList.put(this.CHECK_TOWN_CMB, chekTownCmb);
					}
				} else {
					paramList.put(this.CHECK_TOWN_CMB, chekTownCmb);
				}

				paramList.put(this.CRNUMBER, CRN);
				if (ctDateFilled != null && !ctDateFilled.isEmpty()) {
					if (ctDateFilled.equalsIgnoreCase("Both")) {
						paramList.put("ctDateFilled", "");
					} else {
						paramList.put("ctDateFilled", ctDateFilled);
					}
				} else {
					paramList.put("ctDateFilled", ctDateFilled);
				}

				paramList.put(this.REGISTER_NUMBER, registerNumber);
				paramList.put(this.CASE_STATUS, caseStatus);
				paramList.put(this.WITH_CHARGES, withCharges);
				return paramList;
			}
		} catch (ParseException var19) {
			throw new CMSException(this.logger, var19);
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("in InvoiceCTExcelReport:: fetchTotalCount");
		HashMap<String, String> paramList = this.setParamaters(request);
		int result = this.fetchCTEcxcelRowCount(paramList);
		this.logger.debug("Exiting InvoiceCTExcelReport::fetchTotalCount");
		return result;
	}

	private int fetchCTEcxcelRowCount(HashMap<String, String> paramList) throws CMSException {
		this.logger.debug("in InvoiceCTExcelReport:: fetchCTEcxcelRowCount+++++++++++:");

		int result;
		try {
			result = this.tabularReportDAO.fetchInvoiceCTExcelCount(paramList);
			this.logger.debug("fetchCTEcxcelRowCount is " + result);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting InvoiceCTExcelReport::fetchCTEcxcelRowCount");
		return result;
	}
}