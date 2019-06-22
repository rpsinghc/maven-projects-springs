package com.worldcheck.atlas.dao.dashboard;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.dashboard.DashBoardReportVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class DashboardDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.dashboard.DashboardDAO");
	private static final String GET_TOP_CLIENT_DATA_SQL = "Dashboard.getTopClientMetrics";
	private static final String GET_PROFIT_REPORT_DATA_SQL = "Dashboard.getProfitReport";
	private static final String GET_EXPENDITURE_REPORT_DATA_SQL = "Dashboard.getExpenditureReport";
	private static final String GET_REVENUE_REPORT_DATA_SQL = "Dashboard.getRevenueReport";
	private static final String GET_OFFICE_REVENUE_DATA_SQL = "Dashboard.getOfficeRevenueReport";
	private static final String GET_OFFICE_JLP_DATA_SQL = "Dashboard.getOfficeJLPReport";
	private static final String GET_TEAM_LEAVE_DATA_SQL = "Dashboard.getTeamLeaveReport";
	private static final String GET_PUBLIC_HOLIDAY_DATA_SQL = "Dashboard.getPublicHolidayReport";
	private static final String GET_CASE_OVERDUE_DATA_SQL = "Dashboard.getCaseOverDue";
	private static final String GET_CASE_OVERDUE_R2R9_DATA_SQL = "Dashboard.getCaseOverDueR2R9";
	private static final String GET_TEAM_JLP_DATA_SQL = "Dashboard.getTeamJLPReport";
	private static final String GET_OFFICE_CASES_COUNT_SQL = "Dashboard.getOfficeCasesWipCmpReport";
	private static final String GET_TOTAL_CASES_CM_SQL = "Dashboard.getTotalCasesCMReport";
	private static final String GET_TOTAL_CASES_CM_COUNT_SQL = "Dashboard.getTotalCasesCountCM";
	private static final String GET_REPORT_TYPE_SQL = "Dashboard.getAllReportTypes";
	private static final String CASESDUE_FOR_R2R9_SQL = "Dashboard.getCasesDueForR2R9";
	private static final String CASESDUE_FOR_OTHERS_SQL = "Dashboard.getCasesDueForOthers";
	private static final String CASESDUE_DETAIL_FOR_R2R9_SQL = "Dashboard.getCasesDueTodayTmrwDetailsForR2R9";
	private static final String CASESDUE_DETAIL_FOR_OTHER_SQL = "Dashboard.getCasesDueTodayTmrwDetailsForOthers";

	public List<DashBoardReportVO> getTopClientData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getTopClientData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getTopClientMetrics", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("in DashboardDAO :: getTopClientData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getProfitReportData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getProfitReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getProfitReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getProfitReportData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getExpenditureReportData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getExpenditureReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getExpenditureReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getExpenditureReportData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getRevenueReportData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getRevenueReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getRevenueReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getRevenueReportData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getOfficeRevenueReportData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getOfficeRevenueReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getOfficeRevenueReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getOfficeRevenueReportData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getOfficeJLPReportData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getOfficeJLPReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getOfficeJLPReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getOfficeJLPReportData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getTeamLeaveData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getTeamLeaveData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getTeamLeaveReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getTeamLeaveData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getPublicHolidayData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getPublicHolidayData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getPublicHolidayReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getPublicHolidayData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getCaseOverdueData() throws CMSException {
		this.logger.debug("in DashboardDAO :: getCaseOverdueData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getCaseOverDue");
			this.logger.debug("getCaseOverdueData result size " + resultSetObj.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting DashboardDAO :: getCaseOverdueData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getCaseOverdueDataForR2R9(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getCaseOverdueDataForR2R9");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getCaseOverDueR2R9", paramList);
			this.logger.debug("getCaseOverdueDataForR2R9 result size " + resultSetObj.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getCaseOverdueDataForR2R9");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getTeamJLPData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getTeamJLPData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getTeamJLPReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getTeamJLPData");
		return resultSetObj;
	}

	public List<LinkedHashMap> getOfficeCasesData(Map<String, Object> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getOfficeCasesData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getOfficeCasesWipCmpReport", paramList);
			this.logger.debug("office cases Data list size " + resultSetObj.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getOfficeCasesData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getTotalCasesCmData(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getTotalCasesCmData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getTotalCasesCMReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getTotalCasesCmData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getTotalCaseCountCmPerReport(Map<String, String> paramList) throws CMSException {
		this.logger.debug("in DashboardDAO :: getTotalCaseCountCmPerReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Dashboard.getTotalCasesCountCM", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO :: getTotalCaseCountCmPerReport");
		return resultSetObj;
	}

	public List<ReportTypeMasterVO> getAllReportCrnInitial() throws CMSException {
		this.logger.debug("In DashboardDAO::getAllReportCrnInitial");
		new ArrayList();

		List reportTypes;
		try {
			reportTypes = this.queryForList("Dashboard.getAllReportTypes");
			this.logger.debug("DashboardDAO::reportTypes ----------" + reportTypes.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting DashboardDAO::getAllReportCrnInitial");
		return reportTypes;
	}

	public List<DashBoardReportVO> getCasesDueForR2R9() throws CMSException {
		this.logger.debug("In DashboardDAO::getCasesDueForR2R9");
		new ArrayList();

		List casesCountList;
		try {
			casesCountList = this.queryForList("Dashboard.getCasesDueForR2R9");
			this.logger.debug("DashboardDAO::casesCountList ----------" + casesCountList.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting DashboardDAO::getCasesDueForR2R9");
		return casesCountList;
	}

	public List<DashBoardReportVO> getCasesDueForOtherUsers() throws CMSException {
		this.logger.debug("In DashboardDAO::getCasesDueForOtherUsers");
		new ArrayList();

		List casesCountList;
		try {
			casesCountList = this.queryForList("Dashboard.getCasesDueForOthers");
			this.logger.debug("DashboardDAO::casesCountList ----------" + casesCountList.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting DashboardDAO::getCasesDueForOtherUsers");
		return casesCountList;
	}

	public List<DashBoardReportVO> getCasesDueTodayTmrwDetails(Map<String, String> dateParamMap) throws CMSException {
		this.logger.debug("In DashboardDAO::getCasesDueTodayTmrwDetails");
		new ArrayList();

		List casesCountDetailsList;
		try {
			casesCountDetailsList = this.queryForList("Dashboard.getCasesDueTodayTmrwDetailsForR2R9", dateParamMap);
			this.logger.debug("DashboardDAO::casesCountDetailsList ----------" + casesCountDetailsList.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO::getCasesDueTodayTmrwDetails");
		return casesCountDetailsList;
	}

	public List<DashBoardReportVO> getCasesDueTodayTmrwDetailsForOthers(Map<String, String> dateParamMap)
			throws CMSException {
		this.logger.debug("In DashboardDAO::getCasesDueTodayTmrwDetailsForOthers");
		new ArrayList();

		List casesCountDetailsList;
		try {
			casesCountDetailsList = this.queryForList("Dashboard.getCasesDueTodayTmrwDetailsForOthers", dateParamMap);
			this.logger.debug(
					"DashboardDAO::getCasesDueTodayTmrwDetailsForOthers ----------" + casesCountDetailsList.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting DashboardDAO::getCasesDueTodayTmrwDetailsForOthers");
		return casesCountDetailsList;
	}
}