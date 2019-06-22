package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import com.worldcheck.atlas.vo.report.AllOnHoldCaseVO;
import com.worldcheck.atlas.vo.report.CTExcelVO;
import com.worldcheck.atlas.vo.report.CaseDueTodayVO;
import com.worldcheck.atlas.vo.report.CaseRawDataFinanceVO;
import com.worldcheck.atlas.vo.report.CasesReceivedSummaryVO;
import com.worldcheck.atlas.vo.report.CurrentAnalystLoadingVO;
import com.worldcheck.atlas.vo.report.FinanceRawDataVO;
import com.worldcheck.atlas.vo.report.MyPerformanceReportVO;
import com.worldcheck.atlas.vo.report.OfficeSummaryVO;
import com.worldcheck.atlas.vo.report.OverdueReportVO;
import com.worldcheck.atlas.vo.report.ProductivityAndRevenueSummaryVO;
import com.worldcheck.atlas.vo.report.ProductivityPointsAndCasesVO;
import com.worldcheck.atlas.vo.report.ReportTypeAndOfficeSummaryVO;
import com.worldcheck.atlas.vo.report.RevenueSummaryVO;
import com.worldcheck.atlas.vo.report.ReviewerRawDataVO;
import com.worldcheck.atlas.vo.report.TeamJLPSummaryVO;
import com.worldcheck.atlas.vo.report.TimeTrackerVO;
import com.worldcheck.atlas.vo.report.UtilizationByRevenueVO;
import com.worldcheck.atlas.vo.report.VendorDataSummaryVO;
import com.worldcheck.atlas.vo.report.VendorRawDataVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TabularReportDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.report.TabularReportDAO");
	private static final String teamLevelJLPReport = "TeamJLP.teamLevelJLPReport";
	private static final String caseLevelJLPReport = "TeamJLP.caseLevelJLPReport";
	private String WIP_ONHOLD_COMPLETED_CCS_CANCELLED_CASES = "3, 1, 5, 6, 4";
	private String WIP_ONHOLD_COMPLETED_CCS_CASES = "3, 1, 5, 6";
	private String WIP_CASES = "3, 1";
	private String CASE_TYPE_3 = "3";
	private String CASE_TYPE_2 = "2";
	private String CASE_TYPE_1 = "1";
	private String CASE_TYPE_LIST = "caseTypeList";
	private String CASE_TYPE = "caseType";
	private String OFFICE = "office";
	private String MONTH = "month";
	private String YEAR = "year";
	private String QUARTER = "quarter";
	private String START_DATE = "startDate";
	private String END_DATE = "endDate";
	private String START_MONTH = "startMonth";
	private String END_MONTH = "endMonth";
	private String START_YEAR = "startYear";
	private String END_YEAR = "endYear";
	private String QUARTER1 = "Quarter 1";
	private String QUARTER2 = "Quarter 2";
	private String QUARTER3 = "Quarter 3";
	private String QUARTER4 = "Quarter 4";
	private String TIME_TRACKER_DATA_SUMMARY_SQL_ID = "Reports.getTimeTrackerDS";
	private String TIME_TRACKER_RAW_DATA_SQL_ID = "Reports.getTimeTrackerRD";
	private String PRODUCTIVITY_REVENUE_SUMMARY_SQL_ID = "OfficeUtilization.getProductivityRevenueSummaryDetails";
	private String UTILIZATION_BY_PRODUCTIVITY_POINTS_CASES_SQL_ID = "OfficeCapacity.getUtilizationByProductivityPtsCD";
	private String GET_CASES_RECEIVED_SUMMARY_SQL = "OfficeUtilization.getCasesReceivedSummary";
	private String GET_CASES_RECEIVED_SUMMARY_HEADER_SQL = "OfficeUtilization.getCasesReceivedSummaryHeader";
	private String GET_UTILIZATION_BY_REVENUE_SQL = "OfficeCapacity.getUtilizationByRevenue";
	private String GET_VENDOR_EXP_DATA_SUMMARY_SQL = "Reports.getVendorExpenditureDS";
	private String GET_FINANCE_RAW_DATA_SQL = "Reports.getFinanceRawData";
	private String GET_INVOICE_CT_EXCEL_DATA_SQL = "Reports.getInvoiceCTExcelData";
	private String GET_OFFICE_SUMMARY_DATA_SQL = "OfficeUtilization.getOfficeSummaryData";
	private String GET_VENDOR_EXP_RAW_DATA_SQL = "Reports.getVendorExpenditureRD";
	private String GET_VENDOR_EXP_RAW_DATA_COUNT_SQL = "Reports.getVendorExpenditureRDCount";
	private String TIME_TRACKER_DATA_SUMMARY_COUNT_SQL_ID = "Reports.getTimeTrackerDSCount";
	private String GET_VENDOR_EXPENDITURE_DS_COUNT_SQL = "Reports.getVendorExpenditureDSCount";
	private String allOnHoldCases = "OverDueCases.allOnHoldCasesDetails";
	private String allOnHoldCasesCount = "AllOnHoldCases.allOnHoldCasesDetailsCount";
	private String myPerformanceDetail = "MyPerformance.myPerformanceDetails";
	private String getCRN = "MyPerformance.noOfCRNS";
	private String overDueTL_Count = "OverDueCases.overDueTeamLevel_Team_Count";
	private String overDueOffice_Count = "OverDueCases.overDueTeamLevel_Count";
	private String overDueCL_Count = "OverDueCases.overDueCaseLevel_Count";
	private String dueToday_Count = "OverDueCases.dueTodayDetailsCount";
	private String dueToday_Detail = "OverDueCases.dueTodayDetails";
	private String overDueTL_Detail = "OverDueCases.overDueTeamLevel_Team";
	private String overDueOffice_Detail = "OverDueCases.overDueTeamLevel";
	private String overDueCL_Detail = "OverDueCases.overDueCaseLevel";
	private String CURRENT_ANALYST_DOWNLOAD_XLS = "OfficeCapacity.currentAnalystLoadinXls";
	private String CURRENT_ANALYST_DOWNLOAD_PH = "OfficeCapacity.currentAnalystLoadin_PH";
	private String TIME_TRACKER_RAW_DATA_COUNT_SQL_ID = "Reports.getTimeTrackerRDCount";
	private String UTILIZATION_BY_PRODUCTIVITY_COUNT_SQL_ID = "OfficeCapacity.getUtilizationByProductivityCount";
	private String GET_UTILIZATION_BY_REVENUE_COUNT_SQL = "OfficeCapacity.getUtilizationByRevenueCount";
	private String GET_FRD_COUNT_SQL = "Reports.getFinanceRawDataCount";
	private String GET_CT_EXCEL_COUNT_SQL = "Reports.getInvoiceCTExcelDataCount";
	private String GET_REVIWER_RAW_DATA_SQL = "Reports.getReviewerRawData";
	private String GET_RRD_COUNT_SQL = "Reports.getReviewerRawDataCount";
	private String GET_CASES_BY_MONTH = "Reports.getCasesByMonth";
	private String GET_CASES_BY_STATUS = "Reports.getCasesByCaseStatus";
	private String GET_CASES_BY_REPORT_TYPE = "Reports.getCasesByReportType";
	private String GET_CASES_BY_CLIENTS = "Reports.getCasesByClient";
	private String GET_REVENUES_BY_CLIENTS = "Reports.getRevenueByClient";
	private String GET_CASES_BY_PRIMARY_SUB_COUNTRY = "Reports.getCasesByPrimarySubjectCountry";
	private String GET_REVENUES_BY_PRIMARY_SUB_COUNTRY = "Reports.getRevenueByPrimarySubjectCountry";
	private String GET_CASES_BY_DELIVERY = "Reports.getOntimeDeliveryPercentage";
	private String GET_CASES_BY_TAT_HISTOGRAM = "Reports.getTatHistogram";
	private String GET_STATISTICS_DATA_TAT_HISTOGRAM = "Reports.getStatisticsDataTatHistogram";
	private String GET_CASES_RAW_DATA_BY_MONTH = "Reports.getCRDColumnsData";
	private String GET_CASES_RAW_DATA_BY_CASE_STATUS = "Reports.getCRDColumnsDataCaseStatus";
	private String GET_CASES_RAW_DATA_BY_REPORT_TYPE = "Reports.getCRDColumnsDataReportType";
	private String GET_CASES_RAW_DATA_BY_CLIENTS_TYPE = "Reports.getCRDColumnsDataClientsType";
	private String GET_CASES_RAW_DATA_BY_COUNTRY_TYPE = "Reports.getCRDColumnsDataCountryType";
	private String GET_CASES_RAW_DATA_BY_DELIVERY_TYPE = "Reports.getCRDColumnsDataDeliveryType";
	private String GET_CASES_RAW_DATA_BY_TAT_HISTOGRAM = "Reports.getCRDColumnsDataTAT";
	private String GET_CRD_COUNT_BY_MONTH = "Reports.getCRDColumnsDataMonthCount";
	private String GET_CRD_COUNT_BY_STATUS = "Reports.getCRDColumnsDataStatusCount";
	private String GET_CRD_COUNT_BY_REPORT_TYPE = "Reports.getCRDColumnsDataReportTypeCount";
	private String GET_CRD_COUNT_BY_CLIENT = "Reports.getCRDColumnsDataClientCount";
	private String GET_CRD_COUNT_BY_COUNTRY_TYPE = "Reports.getCRDColumnsDataCountryTypeCount";
	private String GET_CRD_COUNT_BY_DELIVERY = "Reports.getCRDColumnsDeliveryCount";
	private String GET_CRD_COUNT_BY_TAT = "Reports.getCRDColumnsDataTATCount";
	private String GET_CASES_BY_CLIENT_CODE = "Reports.getCasesByClient";
	private String GET_REVENUE_BY_CLIENT_CODE = "Reports.getRevenueByClient";

	public List<TimeTrackerVO> fetchTimeTrackerDSReport(HashMap<String, Object> paramMap) throws CMSException {
		List result = null;

		try {
			this.logger.debug("in fetchTimeTrackerDSReport");
			result = this.queryForList(this.TIME_TRACKER_DATA_SUMMARY_SQL_ID, paramMap);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TimeTrackerVO> fetchTimeTrackerRDReport(HashMap<String, Object> paramMap) throws CMSException {
		List result = null;

		try {
			this.logger.debug("in fetchTimeTrackerRDReport");
			result = this.queryForList(this.TIME_TRACKER_RAW_DATA_SQL_ID, paramMap);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int fetchTimeTrackerRDCount(HashMap<String, Object> paramMap) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("in fetchTimeTrackerRDCount :: ");
			int totalCount = (Integer) this.queryForObject(this.TIME_TRACKER_RAW_DATA_COUNT_SQL_ID, paramMap);
			this.logger.debug(" totalCount :: " + totalCount);
			return totalCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int fetchTimeTrackerDSCount(HashMap<String, Object> paramMap) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("in fetchTimeTrackerDSCount :: ");
			int totalCount = (Integer) this.queryForObject(this.TIME_TRACKER_DATA_SUMMARY_COUNT_SQL_ID, paramMap);
			this.logger.debug(" totalCount :: " + totalCount);
			return totalCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ProductivityAndRevenueSummaryVO> fetchProductivityRevenueSummaryReport(HttpServletRequest request,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("in fetchProductivityRevenueSummaryReport");

		try {
			return this.queryForList(this.PRODUCTIVITY_REVENUE_SUMMARY_SQL_ID);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CasesReceivedSummaryVO> fetchCasesReceivedSummaryReport(HttpServletRequest request,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("in fetchCasesReceivedSummaryReport");
		List result = null;

		try {
			result = this.queryForList(this.GET_CASES_RECEIVED_SUMMARY_SQL);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> fetchCasesReceivedSummaryReportHeader(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in fetchCasesReceivedSummaryReportHeader");
		List result = null;

		try {
			result = this.queryForList(this.GET_CASES_RECEIVED_SUMMARY_HEADER_SQL);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UtilizationByRevenueVO> fetchUtilizationByRevenueReport(HttpServletRequest request,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("in fetchUtilizationByRevenueReport");
		List<UtilizationByRevenueVO> result = null;
		HashMap paramMap = new HashMap();

		try {
			this.logger.debug(
					"fetchUtilizationByRevenueReport :: office : " + request.getParameter("office") + " startDate: "
							+ request.getParameter("startDate") + " endDate: " + request.getParameter("endDate"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String[] startDateDetails = startDate.split(" ");
			String[] endDateDetails = endDate.split(" ");
			this.logger.debug("fetchUtilizationByRevenueReport :: startMonth : "
					+ ReportConstants.monthMap.get(startDateDetails[0]) + " startYear: " + startDateDetails[1]
					+ ": endMonth : " + ReportConstants.monthMap.get(endDateDetails[0]) + " endYear: "
					+ endDateDetails[1]);
			paramMap.put(this.OFFICE, request.getParameter("office"));
			paramMap.put(this.START_MONTH, ReportConstants.monthMap.get(startDateDetails[0]));
			paramMap.put(this.START_YEAR, startDateDetails[1]);
			paramMap.put(this.END_MONTH, ReportConstants.monthMap.get(endDateDetails[0]));
			paramMap.put(this.END_YEAR, endDateDetails[1]);
			result = this.queryForList(this.GET_UTILIZATION_BY_REVENUE_SQL, paramMap);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (PatternSyntaxException var10) {
			throw new CMSException(this.logger, var10);
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public int utilizationByRevenueCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		this.logger.debug("in utilizationByRevenueCount");
		HashMap paramMap = new HashMap();

		try {
			this.logger.debug("utilizationByRevenueCount :: office : " + request.getParameter("office") + " startDate: "
					+ request.getParameter("startDate") + " endDate: " + request.getParameter("endDate"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String[] startDateDetails = startDate.split(" ");
			String[] endDateDetails = endDate.split(" ");
			this.logger.debug(
					"utilizationByRevenueCount :: startMonth : " + ReportConstants.monthMap.get(startDateDetails[0])
							+ " startYear: " + startDateDetails[1] + ": endMonth : "
							+ ReportConstants.monthMap.get(endDateDetails[0]) + " endYear: " + endDateDetails[1]);
			paramMap.put(this.OFFICE, request.getParameter("office"));
			paramMap.put(this.START_MONTH, ReportConstants.monthMap.get(startDateDetails[0]));
			paramMap.put(this.START_YEAR, startDateDetails[1]);
			paramMap.put(this.END_MONTH, ReportConstants.monthMap.get(endDateDetails[0]));
			paramMap.put(this.END_YEAR, endDateDetails[1]);
			int totalCount = (Integer) this.queryForObject(this.GET_UTILIZATION_BY_REVENUE_COUNT_SQL, paramMap);
			this.logger.debug(" totalCount :: " + totalCount);
			return totalCount;
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (PatternSyntaxException var10) {
			throw new CMSException(this.logger, var10);
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public List<ProductivityPointsAndCasesVO> fetchUtilizationByProductivityReport(HashMap<String, Object> paramMap)
			throws CMSException {
		this.logger.debug("in fetchUtilizationByProductivityReport");
		List result = null;

		try {
			result = this.queryForList(this.UTILIZATION_BY_PRODUCTIVITY_POINTS_CASES_SQL_ID, paramMap);
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int utilizationByProductivityCount(HashMap<String, Object> paramMap) throws CMSException {
		this.logger.debug("in utilizationByProductivityCount");

		try {
			int totalCount = (Integer) this.queryForObject(this.UTILIZATION_BY_PRODUCTIVITY_COUNT_SQL_ID, paramMap);
			return totalCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CurrentAnalystLoadingVO> fetchCurrentAnalystLoadingReport(HashMap<String, Object> paramMap)
			throws CMSException {
		this.logger.debug("in fetchCurrentAnalystLoadingReport");
		List result = null;

		try {
			result = this.queryForList(this.CURRENT_ANALYST_DOWNLOAD_XLS, paramMap);
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<VendorDataSummaryVO> fetchVendorDataSummaryReport(HttpServletRequest request,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("in fetchVendorDataSummaryReport");
		List<VendorDataSummaryVO> result = null;
		HashMap paramMap = new HashMap();

		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			limit += start;
			++start;
			this.logger.debug("fetchVendorDataSummaryReport :: month : " + request.getParameter(this.MONTH) + " year: "
					+ request.getParameter(this.YEAR));
			paramMap.put(this.MONTH, request.getParameter(this.MONTH));
			paramMap.put(this.YEAR, request.getParameter(this.YEAR));
			paramMap.put("start", start);
			paramMap.put("limit", limit);
			paramMap.put("sort", request.getParameter("sort"));
			paramMap.put("dir", request.getParameter("dir"));
			this.logger.debug("TabularReportDAO.fetchVendorDataSummaryReport::start: " + start);
			this.logger.debug("TabularReportDAO.fetchVendorDataSummaryReport::limit: " + limit);
			result = this.queryForList(this.GET_VENDOR_EXP_DATA_SUMMARY_SQL, paramMap);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int fetchVendorDataSummaryCount(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in fetchVendorDataSummaryCount");
		HashMap paramMap = new HashMap();

		try {
			paramMap.put(this.MONTH, request.getParameter(this.MONTH));
			paramMap.put(this.YEAR, request.getParameter(this.YEAR));
			return (Integer) this.queryForObject(this.GET_VENDOR_EXPENDITURE_DS_COUNT_SQL, paramMap);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<VendorRawDataVO> fetchVendorRawDataReport(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in fetchVendorRawDataReport");
		List<VendorRawDataVO> result = null;
		HashMap paramMap = new HashMap();

		try {
			this.logger.debug("fetchVendorRawDataReport :: caseType : " + request.getParameter(this.CASE_TYPE)
					+ " startDate: " + request.getParameter(this.START_DATE) + " endDate: "
					+ request.getParameter(this.END_DATE));
			String caseTypeList = request.getParameter(this.CASE_TYPE);
			String startDate = request.getParameter(this.START_DATE);
			String endDate = request.getParameter(this.END_DATE);
			if (caseTypeList.equals(this.CASE_TYPE_1)) {
				caseTypeList = this.WIP_CASES;
			} else if (caseTypeList.equals(this.CASE_TYPE_2)) {
				caseTypeList = this.WIP_ONHOLD_COMPLETED_CCS_CASES;
			} else if (caseTypeList.equals(this.CASE_TYPE_3)) {
				caseTypeList = this.WIP_ONHOLD_COMPLETED_CCS_CANCELLED_CASES;
			}

			paramMap.put(this.CASE_TYPE_LIST, caseTypeList);
			paramMap.put(this.START_DATE, startDate);
			paramMap.put(this.END_DATE, endDate);
			this.logger.debug("fetchVendorRawDataReport :: caseTypeList : " + caseTypeList);
			result = this.queryForList(this.GET_VENDOR_EXP_RAW_DATA_SQL, paramMap);
			this.logger.debug(" list size :: " + result.size());
			return result;
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int fetchVendorRawDataReportCount(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in TabularReportDAO :: fetchVendorRawDataReportCount");
		HashMap paramMap = new HashMap();

		try {
			this.logger.debug("fetchVendorRawDataReport :: caseType : " + request.getParameter(this.CASE_TYPE)
					+ " startDate: " + request.getParameter(this.START_DATE) + " endDate: "
					+ request.getParameter(this.END_DATE));
			String caseTypeList = request.getParameter(this.CASE_TYPE);
			String startDate = request.getParameter(this.START_DATE);
			String endDate = request.getParameter(this.END_DATE);
			if (caseTypeList.equals(this.CASE_TYPE_1)) {
				caseTypeList = this.WIP_CASES;
			} else if (caseTypeList.equals(this.CASE_TYPE_2)) {
				caseTypeList = this.WIP_ONHOLD_COMPLETED_CCS_CASES;
			} else if (caseTypeList.equals(this.CASE_TYPE_3)) {
				caseTypeList = this.WIP_ONHOLD_COMPLETED_CCS_CANCELLED_CASES;
			}

			paramMap.put(this.CASE_TYPE_LIST, caseTypeList);
			paramMap.put(this.START_DATE, startDate);
			paramMap.put(this.END_DATE, endDate);
			this.logger.debug("fetchVendorRawDataReport :: caseTypeList : " + caseTypeList);
			int count = (Integer) this.queryForObject(this.GET_VENDOR_EXP_RAW_DATA_COUNT_SQL, paramMap);
			this.logger.debug(" count:: " + count);
			this.logger.debug("Exiting TabularReportDAO :: fetchVendorRawDataReportCount");
			return count;
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List<CTExcelVO> fetchInvoiceCTExcelReport(HashMap<String, String> paramList) throws CMSException {
		this.logger.debug(" in TabularReportDAO .fetch InvoiceCTExcelReport");
		new ArrayList();

		try {
			List<CTExcelVO> result = this.queryForList(this.GET_INVOICE_CT_EXCEL_DATA_SQL, paramList);
			this.logger.debug("Exiting TabularReportDAO .fetch InvoiceCTExcelReport");
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int fetchInvoiceCTExcelCount(HashMap<String, String> paramList) throws CMSException {
		this.logger.debug("in TabularReportDAO:: fetchInvoiceCTExcelCount");

		int result;
		try {
			result = (Integer) this.queryForObject(this.GET_CT_EXCEL_COUNT_SQL, paramList);
			this.logger.debug("Row count of ct excel MIS is " + result);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TabularReportDAO::fetchInvoiceCTExcelCount");
		return result;
	}

	public List<FinanceRawDataVO> fetchFinanceRawDataReport(HttpServletRequest request, HttpServletResponse response,
			HashMap<String, String> paramList) throws CMSException {
		this.logger.debug("in TabularReportDAO.fetchFinanceRawDataReport");
		new ArrayList();

		List result;
		try {
			result = this.queryForList(this.GET_FINANCE_RAW_DATA_SQL, paramList);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting TabularReportDAO.fetchFinanceRawDataReport");
		return result;
	}

	public int fetchFinanceRawDataRowCount(HashMap<String, String> paramList) throws CMSException {
		this.logger.debug("in TabularReportDAO:: fetchFinanceRawDataRowCount");

		int result;
		try {
			result = (Integer) this.queryForObject(this.GET_FRD_COUNT_SQL, paramList);
			this.logger.debug("Row count is " + result);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TabularReportDAO::fetchFinanceRawDataRowCount");
		return result;
	}

	public List<ReviewerRawDataVO> fetchReviewerRawDataReport(HttpServletRequest request, HttpServletResponse response,
			HashMap<String, Object> paramList) throws CMSException {
		this.logger.debug("in TabularReportDAO.fetchReviewerRawDataReport");
		new ArrayList();

		List result;
		try {
			result = this.queryForList(this.GET_REVIWER_RAW_DATA_SQL, paramList);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting TabularReportDAO.fetchReviewerRawDataReport");
		return result;
	}

	public int fetchReviewerRawDataRowCount(HashMap<String, Object> paramList) throws CMSException {
		this.logger.debug("in TabularReportDAO:: fetchReviewerRawDataRowCount");

		int result;
		try {
			result = (Integer) this.queryForObject(this.GET_RRD_COUNT_SQL, paramList);
			this.logger.debug("Row count is " + result);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TabularReportDAO::fetchReviewerRawDataRowCount");
		return result;
	}

	public List<OfficeSummaryVO> fetchOfficeSummaryReport(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug(" IN TabularReportDAO .fetch OfficeSummaryReport");
		HashMap<String, String> paramList = new HashMap();
		new ArrayList();

		List result;
		try {
			String year = request.getParameter(this.YEAR);
			String quarter = request.getParameter(this.QUARTER);
			String monthFirst = "";
			String monthSecond = "";
			String monthThird = "";
			Calendar cal = Calendar.getInstance();
			if (year == null || year.isEmpty()) {
				int currentYear = cal.get(1);
				year = Integer.toString(currentYear);
			}

			new Integer(year);
			if (quarter != null && !quarter.isEmpty()) {
				if (quarter != null && !quarter.isEmpty()) {
					if (quarter.equalsIgnoreCase(this.QUARTER1)) {
						monthFirst = "1";
						monthSecond = "2";
						monthThird = "3";
					} else if (quarter.equalsIgnoreCase(this.QUARTER2)) {
						monthFirst = "4";
						monthSecond = "5";
						monthThird = "6";
					} else if (quarter.equalsIgnoreCase(this.QUARTER3)) {
						monthFirst = "7";
						monthSecond = "8";
						monthThird = "9";
					} else if (quarter.equalsIgnoreCase(this.QUARTER4)) {
						monthFirst = "10";
						monthSecond = "11";
						monthThird = "12";
					}
				}
			} else {
				int currentMonth = cal.get(2) + 1;
				String currentQuarterMonths = getQuarterForMonth(currentMonth);
				String[] stringArray = currentQuarterMonths.split(",");
				monthFirst = stringArray[0];
				monthSecond = stringArray[1];
				monthThird = stringArray[2];
			}

			paramList.put("year", year);
			paramList.put("monthFirst", monthFirst);
			paramList.put("monthSecond", monthSecond);
			paramList.put("monthThird", monthThird);
			result = this.queryForList(this.GET_OFFICE_SUMMARY_DATA_SQL, paramList);
		} catch (DataAccessException var15) {
			throw new CMSException(this.logger, var15);
		}

		this.logger.debug(" exiting TabularReportDAO .fetch OfficeSummaryReport");
		return result;
	}

	private static String getQuarterForMonth(int currentMonth) throws CMSException {
		String currentQuarterMonths = "";
		String strCurrentMonth = Integer.toString(currentMonth);
		Map<String, String> quarterMap = new HashMap();
		quarterMap.put("FIRST_QUARTER", "1,2,3");
		quarterMap.put("SECOND_QUARTER", "4,5,6");
		quarterMap.put("THIRD_QUARTER", "7,8,9");
		quarterMap.put("FOURTH_QUARTER", "10,11,12");
		Iterator mapIterator = quarterMap.keySet().iterator();

		while (mapIterator.hasNext()) {
			String key = (String) mapIterator.next();
			String months = (String) quarterMap.get(key);
			List<String> monthsList = StringUtils.commaSeparatedStringToList(months);
			if (monthsList.contains(strCurrentMonth)) {
				currentQuarterMonths = months;
			}
		}

		return currentQuarterMonths;
	}

	public List<List<ReportTypeAndOfficeSummaryVO>> fetchReportTypeAndOffcieSummaryReport(HttpServletRequest request,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("IN fetchReportTypeAndOffcieSummaryReport");
		ReportTypeAndOfficeSummaryVO rtosvo = new ReportTypeAndOfficeSummaryVO();
		List<List<ReportTypeAndOfficeSummaryVO>> finalResultList = new ArrayList();
		List<ReportTypeAndOfficeSummaryVO> reportTypeSummaryResultList = new ArrayList();
		Object officeSummaryResultList = new ArrayList();

		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			rtosvo.setStart(start + 1);
			rtosvo.setLimit(start + limit);
			rtosvo.setCaseType(request.getAttribute("caseType").toString());
			rtosvo.setStartDate(request.getAttribute("startDate").toString());
			rtosvo.setEndDate(request.getAttribute("endDate").toString());
			if (rtosvo.getCaseType().equalsIgnoreCase("1")) {
				reportTypeSummaryResultList = this.queryForList("FinanceOverview.reportTypeSummaryForCaseReceivedDdate",
						rtosvo);
				officeSummaryResultList = this.queryForList("FinanceOverview.OfficeSummaryForCaseReceivedDdate",
						rtosvo);
			} else if (rtosvo.getCaseType().equalsIgnoreCase("2")) {
				reportTypeSummaryResultList = this.queryForList("FinanceOverview.reportTypeSummaryForInvoiceDate",
						rtosvo);
				officeSummaryResultList = this.queryForList("FinanceOverview.OfficeSummaryForInvoiceDate", rtosvo);
			}

			finalResultList.add(reportTypeSummaryResultList);
			finalResultList.add(officeSummaryResultList);
			return finalResultList;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public int fetchReportTypeAndOffcieSummaryReport_OfficeCount(ReportTypeAndOfficeSummaryVO rtosvo)
			throws CMSException {
		this.logger.debug("In fetchReportTypeAndOffcieSummaryReport_OfficeCount");
		int count = 0;

		try {
			if (rtosvo.getCaseType().equalsIgnoreCase("1")) {
				count = (Integer) this.queryForObject("FinanceOverview.OfficeSummaryForCaseReceivedDdateCount", rtosvo);
			} else if (rtosvo.getCaseType().equalsIgnoreCase("2")) {
				count = (Integer) this.queryForObject("FinanceOverview.OfficeSummaryForInvoiceDateCount", rtosvo);
			}

			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int fetchReportTypeAndOffcieSummaryReport_ReportCount(ReportTypeAndOfficeSummaryVO rtosvo)
			throws CMSException {
		int count = 0;

		try {
			if (rtosvo.getCaseType().equalsIgnoreCase("1")) {
				count = (Integer) this.queryForObject("FinanceOverview.reportTypeSummaryForCaseReceivedDdateCount",
						rtosvo);
			} else if (rtosvo.getCaseType().equalsIgnoreCase("2")) {
				count = (Integer) this.queryForObject("FinanceOverview.reportTypeSummaryForInvoiceDateCount", rtosvo);
			}

			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<OverdueReportVO> fetchOverdue(String levelType, OverdueReportVO overdueReportVO) throws CMSException {
		this.logger.debug("=================>TabularReport==== fetchOverdue" + overdueReportVO.getOffice());
		new ArrayList();

		List overDueRportVOList;
		try {
			if (levelType.equalsIgnoreCase("2")) {
				String filterVal = overdueReportVO.getOffice();
				this.logger.debug("office Val in Overdue REport===>" + filterVal);
				if (filterVal.equalsIgnoreCase("T3")) {
					overdueReportVO.setOffice("3");
					overDueRportVOList = this.queryForList(this.overDueTL_Detail, overdueReportVO);
				} else if (filterVal.equalsIgnoreCase("T4")) {
					overdueReportVO.setOffice("4");
					overDueRportVOList = this.queryForList(this.overDueTL_Detail, overdueReportVO);
				} else {
					overDueRportVOList = this.queryForList(this.overDueOffice_Detail, overdueReportVO);
				}
			} else {
				overDueRportVOList = this.queryForList(this.overDueCL_Detail, overdueReportVO);
			}
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("in Overdue Dao===>");
		return overDueRportVOList;
	}

	public List<CaseDueTodayVO> caseDueToday(CaseDueTodayVO caseDueTodayVO) throws CMSException {
		new ArrayList();

		try {
			this.logger.debug("INside CAseDueToday Dao");
			List<CaseDueTodayVO> caseDueTodayVOList = this.queryForList(this.dueToday_Detail, caseDueTodayVO);
			return caseDueTodayVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int caseDueTodayCount() throws CMSException {
		try {
			this.logger.debug("INside CAseCount Dao");
			int caseCount = (Integer) this.queryForObject(this.dueToday_Count);
			return caseCount;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<AllOnHoldCaseVO> allOnHoldCase(AllOnHoldCaseVO allOnHoldCaseVO) throws CMSException {
		new ArrayList();

		try {
			this.logger.debug("INside allOnHoldCaseVOList Dao");
			List<AllOnHoldCaseVO> allOnHoldCaseVOList = this.queryForList(this.allOnHoldCases, allOnHoldCaseVO);
			return allOnHoldCaseVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int allOnHoldCaseCount() throws CMSException {
		try {
			this.logger.debug("INside CAseCount Dao");
			int caseCount = (Integer) this.queryForObject(this.allOnHoldCasesCount);
			return caseCount;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int fetchOverdueCount(String levelType, OverdueReportVO overdueReportVO) throws CMSException {
		this.logger.debug("=================>TabularReport==== fetchOverdue---count" + overdueReportVO.getOffice());

		int overDueRportVOListCount;
		try {
			if (levelType.equalsIgnoreCase("2")) {
				String filterVal = overdueReportVO.getOffice();
				this.logger.debug("office Val in Overdue REport+Count===>" + filterVal);
				if (filterVal.equalsIgnoreCase("T3")) {
					overdueReportVO.setOffice("3");
					overDueRportVOListCount = (Integer) this.queryForObject(this.overDueTL_Count, overdueReportVO);
				} else if (filterVal.equalsIgnoreCase("T4")) {
					overdueReportVO.setOffice("4");
					overDueRportVOListCount = (Integer) this.queryForObject(this.overDueTL_Count, overdueReportVO);
				} else {
					overDueRportVOListCount = (Integer) this.queryForObject(this.overDueOffice_Count, overdueReportVO);
				}
			} else {
				overDueRportVOListCount = (Integer) this.queryForObject(this.overDueCL_Count, overdueReportVO);
			}
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("in Overdue Dao===>" + overDueRportVOListCount);
		return overDueRportVOListCount;
	}

	public List getScoreSheetNameList(String officeId) throws CMSException {
		try {
			return this.queryForList("Reports.getAllScoreSheetName", officeId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ScoreSheetMasterVO> analystScoringPerformanceScoreSheetMIS(String office, String scoreSheetName,
			String month, String year, String sortColumnName, String sortType, int start, int limit)
			throws CMSException {
		this.logger.debug("Inside the tabularReportDAO ::analystScoringPerformanceScoreSheetMIS Method::");

		try {
			this.logger.debug("Office id ::" + office + "ScoreSheetId :: " + scoreSheetName + "Month::" + month
					+ "year ::" + year + " sortColumnName:" + sortColumnName + " sortType:" + sortType + " start:"
					+ start + " limit:" + limit);
			Map scoreSheetMISReportMap = new HashMap();
			scoreSheetMISReportMap.put("office", office);
			scoreSheetMISReportMap.put("scoreSheetName", scoreSheetName);
			scoreSheetMISReportMap.put("month", month);
			scoreSheetMISReportMap.put("year", year);
			scoreSheetMISReportMap.put("sortColumnName", sortColumnName);
			scoreSheetMISReportMap.put("sortType", sortType);
			scoreSheetMISReportMap.put("start", start + 1);
			scoreSheetMISReportMap.put("limit", limit + start);
			List<ScoreSheetMasterVO> list = this.queryForList("Reports.scoringPerformanceScoreSheetMIS",
					scoreSheetMISReportMap);
			this.logger.debug("list size::" + list.size());
			return list;
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public int getAnalystScoringMISGridCount(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		this.logger.debug("count>>>>>>>>>>>>>>>>>>>>>>inside dao:" + scoreSheetMasterVO.getStart());

		try {
			return (Integer) this.queryForObject("Reports.totalScoringPerformanceScoreSheetMIS", scoreSheetMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ScoreSheetMasterVO> reviewEffortScoreSheetMIS(ScoreSheetMasterVO scoreSheetMasterVO, int start,
			int limit, String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("Inside the tabularReportDAO ::reviewEffortScoreSheetMIS Method::");

		try {
			scoreSheetMasterVO.setSortColumnName(sortColumnName);
			scoreSheetMasterVO.setSortType(sortType);
			scoreSheetMasterVO.setStart(start + 1);
			scoreSheetMasterVO.setLimit(start + limit);
			List<ScoreSheetMasterVO> list = this.queryForList("Reports.reviewEffortScoreSheetMIS", scoreSheetMasterVO);
			this.logger.debug("list size::" + list.size());
			return list;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int getReviewEffortScoringMISGridCount(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		this.logger.debug("count>>>>>>>>>>>>>>>>>>>>>>inside dao:" + scoreSheetMasterVO.getStart());

		try {
			scoreSheetMasterVO.setStart(new Integer(scoreSheetMasterVO.getStart() + 1));
			scoreSheetMasterVO.setLimit(new Integer(scoreSheetMasterVO.getStart() + scoreSheetMasterVO.getLimit()));
			return (Integer) this.queryForObject("Reports.totalReviewEffortScoreSheetMIS", scoreSheetMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<HashMap> getReviewFieldScoreSheetMISReport(String scoreSheetName, String month, String year)
			throws CMSException {
		try {
			this.logger.debug(
					"Inside the TabularDaoReport :: getReviewFieldScoreSheetMISReport :: Sheetid::" + scoreSheetName);
			HashMap<String, String> map = new HashMap();
			map.put("scoreSheetName", scoreSheetName);
			map.put("month", month);
			map.put("year", year);
			List<HashMap> list = this.queryForList("ReviewTask.getReviewedSelectedItem", map);
			this.logger.debug("getReviewFieldScoreSheetMISReport list size::" + list.size());
			return list;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<ReviewScoreSheetVO> getCat_SubCat_ScoreSheetMISReport(String scoreSheetName, String month, String year)
			throws CMSException {
		try {
			this.logger.debug(
					"Inside the TabularDaoReport :: getCat_SubCat_ScoreSheetMISReport :: Sheetid::" + scoreSheetName);
			HashMap<String, String> map = new HashMap();
			map.put("scoreSheetName", scoreSheetName);
			map.put("month", month);
			map.put("year", year);
			List<ReviewScoreSheetVO> fetchExcelData = this.queryForList("ReviewTask.getReviewScoreSheetInfo", map);
			this.logger.debug("CategorySubCategoryList size::" + fetchExcelData.size());
			return fetchExcelData;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List fetchMyPerformance(MyPerformanceReportVO myPerformanceReportVO) throws CMSException {
		List myPerformanceVOList = null;

		try {
			this.logger.debug("INside MyPerformance Dao" + myPerformanceReportVO.getYear());
			this.logger.debug("INside MyPerformance Dao Analist::::::::" + myPerformanceReportVO.getAnalyst());
			myPerformanceVOList = this.queryForList(this.myPerformanceDetail, myPerformanceReportVO);
			Iterator itr = myPerformanceVOList.iterator();

			while (itr.hasNext()) {
				MyPerformanceReportVO inVo = (MyPerformanceReportVO) itr.next();

				MyPerformanceReportVO var6;
				for (Iterator itrIn = inVo.getListMyPerformanceReportVO().iterator(); itrIn
						.hasNext(); var6 = (MyPerformanceReportVO) itrIn.next()) {
					;
				}
			}

			this.logger.debug("Main Object" + myPerformanceVOList);
			this.logger.debug(String.valueOf(myPerformanceVOList.size()));
			return myPerformanceVOList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<MyPerformanceReportVO> getCRNS(HashMap hm) throws CMSException {
		this.logger.debug(hm.toString());

		try {
			List<MyPerformanceReportVO> crnVAL = this.queryForList(this.getCRN, hm);
			return crnVAL;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamJLPSummaryVO> fetchTeamLevelJLPReport(HashMap<String, String> hmap) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchTeamJLPReport");
		new ArrayList();

		try {
			List<TeamJLPSummaryVO> resultList = this.queryForList("TeamJLP.teamLevelJLPReport", hmap);
			return resultList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamJLPSummaryVO> fetchCaseLevelJLPReport(HashMap<String, String> hmap) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchCaseJLPReport");
		new ArrayList();

		try {
			List<TeamJLPSummaryVO> resultList = this.queryForList("TeamJLP.caseLevelJLPReport", hmap);
			return resultList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String fetchPublicHolidays(HashMap<String, Object> hmap) {
		String phd = (String) this.queryForObject(this.CURRENT_ANALYST_DOWNLOAD_PH, hmap);
		return phd;
	}

	public List<RevenueSummaryVO> fetchRevenueChartReport(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchRevenueChartReport");
		new ArrayList();
		String str = "";
		if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByMonth")) {
			str = this.GET_CASES_BY_MONTH;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")) {
			str = this.GET_CASES_BY_STATUS;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
			str = this.GET_CASES_BY_REPORT_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataOnTimeDeliveryPercentage")) {
			str = this.GET_CASES_BY_DELIVERY;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataTatHistogram")) {
			str = this.GET_CASES_BY_TAT_HISTOGRAM;
		}

		try {
			List<RevenueSummaryVO> resultList = this.queryForList(str, revenueSummaryVO);
			return resultList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public Map<Integer, List<RevenueSummaryVO>> fetchCasesAndRevenueChartReport(HttpServletRequest request,
			HttpServletResponse response, RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchRevenueChartReport");
		Map<Integer, List<RevenueSummaryVO>> map = new HashMap();
		new ArrayList();
		new ArrayList();
		String strCases = "";
		String strRevenue = "";
		if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByClients")) {
			strCases = this.GET_CASES_BY_CLIENTS;
			strRevenue = this.GET_REVENUES_BY_CLIENTS;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByPrimarySubjectCountry")) {
			strCases = this.GET_CASES_BY_PRIMARY_SUB_COUNTRY;
			strRevenue = this.GET_REVENUES_BY_PRIMARY_SUB_COUNTRY;
		}

		List resultListForCases;
		List resultListForRevenue;
		try {
			resultListForCases = this.queryForList(strCases, revenueSummaryVO);
			resultListForRevenue = this.queryForList(strRevenue, revenueSummaryVO);
		} catch (DataAccessException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}

		map.put(1, resultListForCases);
		map.put(2, resultListForRevenue);
		return map;
	}

	public List<CaseRawDataFinanceVO> fetchCrdDataReport(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchRevenueChartReport");
		new ArrayList();
		String str = "";
		if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByMonth")) {
			str = this.GET_CASES_RAW_DATA_BY_MONTH;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")) {
			str = this.GET_CASES_RAW_DATA_BY_CASE_STATUS;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
			str = this.GET_CASES_RAW_DATA_BY_REPORT_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByClients")) {
			str = this.GET_CASES_RAW_DATA_BY_CLIENTS_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByPrimarySubjectCountry")) {
			str = this.GET_CASES_RAW_DATA_BY_COUNTRY_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataOnTimeDeliveryPercentage")) {
			str = this.GET_CASES_RAW_DATA_BY_DELIVERY_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataTatHistogram")) {
			str = this.GET_CASES_RAW_DATA_BY_TAT_HISTOGRAM;
		}

		try {
			List<CaseRawDataFinanceVO> resultList = this.queryForList(str, revenueSummaryVO);
			return resultList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int getTotalCount() throws CMSException {
		this.logger.debug("in TabularReportDAO:: getTotalCount");

		int result;
		try {
			result = (Integer) this.queryForObject("Reports.getTotalRevenueCount");
			this.logger.debug("Row count is " + result);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting TabularReportDAO::getTotalCount");
		return result;
	}

	public List<RevenueSummaryVO> fetchSubReportChart(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchRevenueChartReport");
		new ArrayList();

		try {
			List<RevenueSummaryVO> resultList = this.queryForList("Reports.completedCasesByReportTypeSummary");
			return resultList;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<RevenueSummaryVO> fetchStatusChartReport(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchRevenueChartReport");
		new ArrayList();

		try {
			List<RevenueSummaryVO> resultList = this.queryForList(this.GET_CASES_BY_STATUS, revenueSummaryVO);
			return resultList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public int fetchReportSize(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In TabularReportDAO : fetchReportSize");
		String str = "";
		int result = false;
		if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByMonth")) {
			str = this.GET_CRD_COUNT_BY_MONTH;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")) {
			str = this.GET_CRD_COUNT_BY_STATUS;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
			str = this.GET_CRD_COUNT_BY_REPORT_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByClients")) {
			str = this.GET_CRD_COUNT_BY_CLIENT;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByPrimarySubjectCountry")) {
			str = this.GET_CRD_COUNT_BY_COUNTRY_TYPE;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataOnTimeDeliveryPercentage")) {
			str = this.GET_CRD_COUNT_BY_DELIVERY;
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataTatHistogram")) {
			str = this.GET_CRD_COUNT_BY_TAT;
		}

		try {
			int result = (Integer) this.queryForObject(str, revenueSummaryVO);
			this.logger.debug("TabularReportDAO::result count::" + result);
			return result;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public Map<String, List<RevenueSummaryVO>> getCasesByPrimarySubjectCountry(RevenueSummaryVO revenueSummaryVO)
			throws CMSException {
		this.logger.debug("In tabular ReportDAo: getCasesByPrimarySubjectCountry");
		new ArrayList();
		new ArrayList();
		HashMap dataMap = new HashMap();

		try {
			List<RevenueSummaryVO> casesList = this.queryForList(this.GET_CASES_BY_PRIMARY_SUB_COUNTRY,
					revenueSummaryVO);
			List<RevenueSummaryVO> revenueList = this.queryForList(this.GET_REVENUES_BY_PRIMARY_SUB_COUNTRY,
					revenueSummaryVO);
			dataMap.put("cases", casesList);
			dataMap.put("revenue", revenueList);
			return dataMap;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public Map<String, List<RevenueSummaryVO>> getTATHistogramData(RevenueSummaryVO revenueSummaryVO)
			throws CMSException {
		this.logger.debug("In Tabular Report DAO: TAT Histogram");
		new ArrayList();
		new ArrayList();
		HashMap dataMap = new HashMap();

		try {
			List<RevenueSummaryVO> casesList = this.queryForList(this.GET_CASES_BY_TAT_HISTOGRAM, revenueSummaryVO);
			List<RevenueSummaryVO> statisticDataList = this.queryForList(this.GET_STATISTICS_DATA_TAT_HISTOGRAM,
					revenueSummaryVO);
			dataMap.put("cases", casesList);
			dataMap.put("invoiceTATReport", statisticDataList);
			return dataMap;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<RevenueSummaryVO> getOntimeDeliveryPercentage(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In Tabular Report DAO: On time Delivery percentage");
		new ArrayList();

		try {
			List<RevenueSummaryVO> dataList = this.queryForList(this.GET_CASES_BY_DELIVERY, revenueSummaryVO);
			return dataList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<RevenueSummaryVO> getCasesByMonth(RevenueSummaryVO revenueSummaryVO) {
		List<RevenueSummaryVO> revenueSummaryVOList = this.queryForList(this.GET_CASES_BY_MONTH, revenueSummaryVO);
		Iterator it = revenueSummaryVOList.iterator();
		this.logger.debug("Size from backend" + revenueSummaryVOList.size());

		while (it.hasNext()) {
			RevenueSummaryVO rev = (RevenueSummaryVO) it.next();
			this.logger.debug("No of cases in getCasesByMonth::: " + rev.getNumberOfCases());
		}

		return revenueSummaryVOList;
	}

	public List<RevenueSummaryVO> getCasesByCaseStatus(RevenueSummaryVO revenueSummaryVO) {
		List<RevenueSummaryVO> revenueSummaryVOList = this.queryForList(this.GET_CASES_BY_STATUS, revenueSummaryVO);
		Iterator it = revenueSummaryVOList.iterator();
		this.logger.debug("Size from backend" + revenueSummaryVOList.size());

		while (it.hasNext()) {
			RevenueSummaryVO rev = (RevenueSummaryVO) it.next();
			this.logger.debug("No of cases getCasesByCaseStatus:::: " + rev.getNumberOfCases());
		}

		return revenueSummaryVOList;
	}

	public List<RevenueSummaryVO> getCasesByReportType(RevenueSummaryVO revenueSummaryVO) {
		List<RevenueSummaryVO> revenueSummaryVOList = this.queryForList(this.GET_CASES_BY_REPORT_TYPE,
				revenueSummaryVO);
		Iterator it = revenueSummaryVOList.iterator();
		this.logger.debug("Size from backend getCasesByReportType:::" + revenueSummaryVOList.size());

		while (it.hasNext()) {
			RevenueSummaryVO rev = (RevenueSummaryVO) it.next();
			this.logger.debug("No of cases getCasesByReportType:::: " + rev.getNumberOfCases());
		}

		return revenueSummaryVOList;
	}

	public Map<String, Object> getCasesByClientCode(RevenueSummaryVO revenueSummaryVO) {
		Map<String, Object> clientDataMap = new HashMap();
		List<RevenueSummaryVO> clientCasesList = this.queryForList(this.GET_CASES_BY_CLIENT_CODE, revenueSummaryVO);
		List<RevenueSummaryVO> clientRevenueList = this.queryForList(this.GET_REVENUE_BY_CLIENT_CODE, revenueSummaryVO);
		this.logger.debug("Size from backend getCasesByClientCode for cases:::" + clientCasesList.size());
		this.logger.debug("Size from backend getCasesByClientCode for revenue:::" + clientRevenueList.size());
		clientDataMap.put("clientCaseList", clientCasesList);
		clientDataMap.put("clientRevenueList", clientRevenueList);
		return clientDataMap;
	}
}