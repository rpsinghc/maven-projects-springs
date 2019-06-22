package com.worldcheck.atlas.frontend.dashboard;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.dashboard.DashboardManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.dashboard.DashBoardReportVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class JSONViewDashboardController implements Controller {
	private DashboardManager dashboardManager;
	private static final String CHART_WIDTH_VALUE = "1150";
	private static final String CHART_HEIGHT_VALUE = "310";
	private static final String YEAR = "myYear";
	private static final String MONTH = "month";
	private static final String GRAPH_NAME = "graphName";
	private static final String PROFIT_GRAPH_NAME = "ProfitGraph";
	private static final String EXPENDITURE_GRAPH_NAME = "ExpenditureGraph";
	private static final String PROFIT_GRAPH_MAP = "profitReportMap";
	private static final String EXPENDITURE_GRAPH_MAP = "expenditureReportMap";
	private static final String REVENUE_GRAPH_MAP = "revenueReportMap";
	private static final String TOP_CLIENT_GRAPH = "TopClientGraph";
	private static final String TOP_CLIENT_RESULT = "topClientResult";
	private static final String REVENUE_GRAPH = "RevenueGraph";
	private static final String OFFICE_REVENUE_GRAPH = "OfficeRevenueGraph";
	private static final String OFFICE_CASES_GRAPH = "OfficeCasesGraph";
	private static final String TEAM_JLP_GRAPH = "TeamJLPGraph";
	private static final String OFFICE_JLP_GRAPH = "OfcJLPGraph";
	private static final String TEAM_LEAVE_TABLE = "TeamLeaveTable";
	private static final String PUBLIC_HOLIDAY_TABLE = "PublicHolidayTable";
	private static final String CASES_OVERDUE_TABLE = "CaseOverdueTable";
	private static final String TOTAL_CASES_CM_TABLE = "TotalCasesCmTable";
	private static final String FROM_ISIS = "fromISIS";
	private static final String CLIENT_NAME = "clientName";
	private static final String TTL_CASES_CM_TABLE_DETAIL = "TotalCasesCmTableDetail";
	private static final String SEL_DATE = "selectedDate";
	private static final String TTL_ISIS_CASES_CM_TABLE = "TotalISISCasesCmTable";
	private static final String TTL_ISIS_CASES_CM_TABLE_DETAIL = "TotalISISCasesDetail";
	private static final String CASES_DUE_DATA = "CasesDueData";
	private static final String CASES_DUE_DETAIL_DATA = "casesDueDetails";
	private static final String REPORT_TYPE_LABEL = "Report Type";
	private static final String PROFIT = "Profit";
	private static final String EXPENDITURE = "Expenditure";
	private static final String CHART_WIDTH = "chartWidth";
	private static final String CHART_HEIGHT = "chartHeight";
	private static final String FUSION_XML = "fusionXML";
	private static final String CASES_DUE_TODAY_TMRW = "CasesDueTodayTable";
	private static final String CASES_DUE_TODAY_TMRW_DETAIL = "CasesDueTodayTableDetail";
	private static final String DATE_PARAM = "dateParam";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.dashboard.JSONViewDashboardController");

	public void setDashboardManager(DashboardManager dashboardManager) {
		this.dashboardManager = dashboardManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		modelandview = new ModelAndView("jsonView");
		this.logger.debug("IN JSONViewDashboardController:handleRequest");

		try {
			String graphName = request.getParameter("graphName");
			int currentYear = Calendar.getInstance().get(1);
			int currentMonth = Calendar.getInstance().get(2) + 1;
			Integer selYear;
			if (request.getParameter("myYear") != null && !request.getParameter("myYear").isEmpty()) {
				selYear = new Integer(request.getParameter("myYear").toString());
			} else {
				selYear = currentYear;
			}

			Integer selmonth;
			if (request.getParameter("month") != null && !request.getParameter("month").isEmpty()) {
				selmonth = new Integer(request.getParameter("month").toString());
			} else {
				selmonth = currentMonth;
			}

			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List<String> roleList = userDetailsBean.getRoleList();
			this.logger.debug("graphName>>" + graphName);
			List casesDueList;
			if (graphName.equalsIgnoreCase("TopClientGraph")) {
				casesDueList = this.getTopClientTable(selYear, selmonth);
				modelandview.addObject("topClientResult", casesDueList);
			}

			HashMap officeCasesReportMap;
			if (graphName.equalsIgnoreCase("ProfitGraph")) {
				officeCasesReportMap = this.getProfitGraph(selYear, selmonth);
				modelandview.addObject("profitReportMap", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("ExpenditureGraph")) {
				officeCasesReportMap = this.getExpenditureGraph(selYear, selmonth);
				modelandview.addObject("expenditureReportMap", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("RevenueGraph")) {
				officeCasesReportMap = this.getRevenueGraph(selYear, selmonth);
				modelandview.addObject("revenueReportMap", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("OfficeRevenueGraph")) {
				officeCasesReportMap = this.getOfficeRevenueGraph(selYear, selmonth);
				modelandview.addObject("OfficeRevenueGraph", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("OfcJLPGraph")) {
				officeCasesReportMap = this.getOfficeJLPGraph(selYear, selmonth);
				modelandview.addObject("OfcJLPGraph", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("TeamJLPGraph")) {
				officeCasesReportMap = this.getTeamJLPGraph(selYear, selmonth, userBean.getUserName());
				modelandview.addObject("TeamJLPGraph", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("OfficeCasesGraph")) {
				officeCasesReportMap = this.getOfficeCasesGraph(selYear, selmonth);
				modelandview.addObject("OfficeCasesGraph", officeCasesReportMap);
			}

			if (graphName.equalsIgnoreCase("TeamLeaveTable")) {
				casesDueList = this.getTeamLeaveTable(selYear, selmonth, userBean.getUserName());
				modelandview.addObject("TeamLeaveTable", casesDueList);
			}

			if (graphName.equalsIgnoreCase("PublicHolidayTable")) {
				casesDueList = this.getPublicHolidayTable(selYear, selmonth);
				modelandview.addObject("PublicHolidayTable", casesDueList);
			}

			if (graphName.equalsIgnoreCase("CaseOverdueTable")) {
				casesDueList = this.getCaseOverdue(userBean.getUserName(), roleList);
				modelandview.addObject("CaseOverdueTable", casesDueList);
			}

			List casesDueDetailsList;
			String dateParam;
			if (graphName.equalsIgnoreCase("TotalCasesCmTable")) {
				dateParam = "";
				if (request.getParameter("fromISIS") != null && !request.getParameter("fromISIS").isEmpty()) {
					dateParam = request.getParameter("fromISIS");
				}

				casesDueDetailsList = this.getTotalCasesCM(userBean.getUserName(), dateParam);
				modelandview.addObject("TotalCasesCmTable", casesDueDetailsList);
			}

			String fromISIS;
			List tCaseCMReportList;
			String selectedDate;
			if (graphName.equalsIgnoreCase("TotalCasesCmTableDetail")) {
				dateParam = request.getParameter("clientName");
				selectedDate = request.getParameter("selectedDate");
				fromISIS = "";
				if (request.getParameter("fromISIS") != null && !request.getParameter("fromISIS").isEmpty()) {
					fromISIS = request.getParameter("fromISIS");
				}

				this.logger.debug(
						"clientName>>" + dateParam + " selectedDate>>" + selectedDate + " fromISIS>>" + fromISIS);
				tCaseCMReportList = this.getTotalCaseCountCmPerReport(dateParam, selectedDate, userBean.getUserName(),
						fromISIS);
				modelandview.addObject("TotalCasesCmTableDetail", tCaseCMReportList);
			}

			if (graphName.equalsIgnoreCase("TotalISISCasesCmTable")) {
				dateParam = "";
				if (request.getParameter("fromISIS") != null && !request.getParameter("fromISIS").isEmpty()) {
					dateParam = request.getParameter("fromISIS");
				}

				casesDueDetailsList = this.getTotalCasesCM(userBean.getUserName(), dateParam);
				modelandview.addObject("TotalISISCasesCmTable", casesDueDetailsList);
			}

			if (graphName.equalsIgnoreCase("TotalISISCasesDetail")) {
				dateParam = request.getParameter("clientName");
				selectedDate = request.getParameter("selectedDate");
				fromISIS = "";
				if (request.getParameter("fromISIS") != null && !request.getParameter("fromISIS").isEmpty()) {
					fromISIS = request.getParameter("fromISIS");
				}

				tCaseCMReportList = this.getTotalCaseCountCmPerReport(dateParam, selectedDate, userBean.getUserName(),
						fromISIS);
				modelandview.addObject("TotalISISCasesDetail", tCaseCMReportList);
			}

			if (graphName.equalsIgnoreCase("CasesDueTodayTable")) {
				casesDueList = this.getCaseDueForTodayTmrw(userBean.getUserName(), roleList);
				modelandview.addObject("CasesDueData", casesDueList);
			}

			if (graphName.equalsIgnoreCase("CasesDueTodayTableDetail")) {
				dateParam = "";
				if (request.getParameter("dateParam") != null && !request.getParameter("dateParam").isEmpty()) {
					dateParam = request.getParameter("dateParam");
				}

				casesDueDetailsList = this.getCaseDueForTodayTmrwDetails(userBean.getUserName(), roleList, dateParam);
				modelandview.addObject("casesDueDetails", casesDueDetailsList);
			}

			this.logger.debug("Exiting JSONViewDashboardController::handleRequest");
		} catch (CMSException var16) {
			AtlasUtils.getExceptionView(this.logger, var16);
		} catch (Exception var17) {
			AtlasUtils.getExceptionView(this.logger, var17);
		}

		return modelandview;
	}

	private HashMap<String, String> getProfitGraph(Integer selYear, Integer selmonth) throws CMSException {
		this.logger.debug("IN JSONViewDashboardController::getProfitGraph");
		new HashMap();

		try {
			new ArrayList();
			List<DashBoardReportVO> profitResult = this.dashboardManager.getProfitData(String.valueOf(selYear),
					String.valueOf(selmonth));
			LinkedHashMap<String, Float> profitResultMap = new LinkedHashMap();
			Iterator iterator = profitResult.iterator();

			while (iterator.hasNext()) {
				DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
				String key = dashBoardReportVO.getReportCrnInitial();
				Float value = dashBoardReportVO.getProfit();
				profitResultMap.put(key, value);
			}

			HashMap<String, String> profitReportMap = this.setGraphParameters("1150", "310", "Report Type", "Profit",
					profitResultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getProfitGraph");
			return profitReportMap;
		} catch (CMSException var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	private HashMap<String, String> getExpenditureGraph(Integer selYear, Integer selmonth) throws CMSException {
		new HashMap();
		this.logger.debug("IN JSONViewDashboardController::getExpenditureGraph");

		try {
			new ArrayList();
			List<DashBoardReportVO> expenditureResult = this.dashboardManager
					.getExpenditureData(String.valueOf(selYear), String.valueOf(selmonth));
			LinkedHashMap<String, Float> expenditureResultMap = new LinkedHashMap();
			Iterator iterator = expenditureResult.iterator();

			while (iterator.hasNext()) {
				DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
				String key = dashBoardReportVO.getReportCrnInitial();
				Float value = dashBoardReportVO.getExpenditure();
				expenditureResultMap.put(key, value);
			}

			HashMap<String, String> mp = this.setGraphParameters("1150", "310", "Report Type", "Expenditure",
					expenditureResultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getExpenditureGraph");
			return mp;
		} catch (CMSException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private List<DashBoardReportVO> getTopClientTable(Integer selYear, Integer selmonth) throws CMSException {
		this.logger.debug("In JSONViewDashboardController::getTopClientTable");
		new ArrayList();

		try {
			List<DashBoardReportVO> topClientList = this.dashboardManager.getTopClientData(String.valueOf(selYear),
					String.valueOf(selmonth));
			this.logger.debug("Exiting JSONViewDashboardController::getTopClientTable");
			return topClientList;
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private List<DashBoardReportVO> getTeamLeaveTable(Integer selYear, Integer selmonth, String userId)
			throws CMSException {
		this.logger.debug("In JSONViewDashboardController::getTeamLeaveTable");
		new ArrayList();

		try {
			List<DashBoardReportVO> teamLeaveList = this.dashboardManager.getTeamLeaveData(String.valueOf(selYear),
					String.valueOf(selmonth), userId);
			this.logger.debug("Exiting JSONViewDashboardController::getTeamLeaveTable");
			return teamLeaveList;
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private List<DashBoardReportVO> getPublicHolidayTable(Integer selYear, Integer selmonth) throws CMSException {
		this.logger.debug("In JSONViewDashboardController::getPublicHolidayTable");
		new ArrayList();

		try {
			List<DashBoardReportVO> publicHoliday = this.dashboardManager.getPublicHolidayData(String.valueOf(selYear),
					String.valueOf(selmonth));
			this.logger.debug("Exiting JSONViewDashboardController::getPublicHolidayTable");
			return publicHoliday;
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private List<DashBoardReportVO> getTotalCasesCM(String userId, String fromISIS) throws CMSException {
		this.logger.debug("In JSONViewDashboardController::getTotalCasesCM");
		new ArrayList();

		try {
			List<DashBoardReportVO> dateValueMapList = this.dashboardManager.getTotalCasesCmData(userId, fromISIS);
			this.logger.debug("Exiting JSONViewDashboardController::getTotalCasesCM" + dateValueMapList.size());
			return dateValueMapList;
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private List<DashBoardReportVO> getTotalCaseCountCmPerReport(String clientName, String requestRecvdDate,
			String caseMgrId, String fromISIS) throws CMSException {
		this.logger.debug("In JSONViewDashboardController::getTotalCaseCountCmPerReport");
		new ArrayList();

		try {
			List<DashBoardReportVO> dataResult = this.dashboardManager.getTotalCaseCountCmPerReport(clientName,
					requestRecvdDate, caseMgrId, fromISIS);

			DashBoardReportVO var7;
			for (Iterator iterator = dataResult.iterator(); iterator
					.hasNext(); var7 = (DashBoardReportVO) iterator.next()) {
				;
			}

			this.logger.debug("Exiting JSONViewDashboardController::getTotalCaseCountCmPerReport");
			return dataResult;
		} catch (CMSException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private List<DashBoardReportVO> getCaseOverdue(String userId, List<String> roleList) throws CMSException {
		new ArrayList();

		try {
			boolean isUserRoleR2OrR9 = false;
			Iterator iterator = roleList.iterator();

			while (iterator.hasNext()) {
				String role = (String) iterator.next();
				if (role.equals("R2") || role.equals("R9")) {
					isUserRoleR2OrR9 = true;
					break;
				}
			}

			this.logger.debug("isUserRoleR2OrR9 " + isUserRoleR2OrR9);
			List<DashBoardReportVO> caseOverdueResult = this.dashboardManager.getCaseOverdueData(userId,
					isUserRoleR2OrR9);
			return caseOverdueResult;
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private HashMap<String, String> getRevenueGraph(Integer selYear, Integer selmonth) throws CMSException {
		new HashMap();
		this.logger.debug("IN JSONViewDashboardController::getRevenueGraph");

		try {
			new ArrayList();
			List<DashBoardReportVO> revenueResult = this.dashboardManager.getRevenueData(String.valueOf(selYear),
					String.valueOf(selmonth));
			LinkedHashMap<String, Float> revenueResultMap = new LinkedHashMap();
			Iterator iterator = revenueResult.iterator();

			while (iterator.hasNext()) {
				DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
				String key = dashBoardReportVO.getReportCrnInitial();
				Float value = dashBoardReportVO.getRevenue();
				revenueResultMap.put(key, value);
			}

			HashMap<String, String> mp = this.setGraphParameters("1150", "310", "Report Type", "Case Fee",
					revenueResultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getExpenditureGraph");
			return mp;
		} catch (CMSException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private HashMap<String, String> getOfficeRevenueGraph(Integer selYear, Integer selmonth) throws CMSException {
		new HashMap();
		this.logger.debug("IN JSONViewDashboardController::getOfficeRevenueGraph");

		try {
			new ArrayList();
			List<DashBoardReportVO> ofcRevResult = this.dashboardManager.getOfficeRevenueData(String.valueOf(selYear),
					String.valueOf(selmonth));
			LinkedHashMap<String, Float[]> ofcRevResultMap = new LinkedHashMap();
			Iterator iterator = ofcRevResult.iterator();

			while (iterator.hasNext()) {
				DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
				String key = dashBoardReportVO.getOffice();
				Float[] valuesArr = new Float[2];
				Float value = dashBoardReportVO.getRevenue();
				Float value2 = dashBoardReportVO.getWipRevenue();
				valuesArr[0] = value;
				valuesArr[1] = value2;
				ofcRevResultMap.put(key, valuesArr);
			}

			HashMap<String, String> mp = this.set2SeriesGraphParameters("1150", "310", "Office", "Revenue",
					ofcRevResultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getOfficeRevenueGraph");
			return mp;
		} catch (CMSException var12) {
			throw new CMSException(this.logger, var12);
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private HashMap<String, String> getOfficeJLPGraph(Integer selYear, Integer selmonth) throws CMSException {
		new HashMap();
		this.logger.debug("IN JSONViewDashboardController::getOfficeJLPGraph");

		try {
			new ArrayList();
			List<DashBoardReportVO> ofcRevResult = this.dashboardManager.getOfficeJLPData(String.valueOf(selYear),
					String.valueOf(selmonth));
			LinkedHashMap<String, Float[]> ofcJlpResultMap = new LinkedHashMap();
			Iterator iterator = ofcRevResult.iterator();

			while (iterator.hasNext()) {
				DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
				String key = dashBoardReportVO.getOffice();
				Float[] valuesArr = new Float[2];
				Float value = dashBoardReportVO.getCompletedJLP();
				Float value2 = dashBoardReportVO.getInProgressJLP();
				valuesArr[0] = value;
				valuesArr[1] = value2;
				ofcJlpResultMap.put(key, valuesArr);
			}

			HashMap<String, String> mp = this.set2SeriesGraphParameters("1150", "310", "Office", "JLP",
					ofcJlpResultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getOfficeJLPGraph");
			return mp;
		} catch (CMSException var12) {
			throw new CMSException(this.logger, var12);
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private HashMap<String, String> getTeamJLPGraph(Integer selYear, Integer selmonth, String userId)
			throws CMSException {
		new HashMap();
		this.logger.debug("IN JSONViewDashboardController::getTeamJLPGraph");

		try {
			new ArrayList();
			List<DashBoardReportVO> teamJLPResult = this.dashboardManager.getTeamJLPData(String.valueOf(selYear),
					String.valueOf(selmonth), userId);
			LinkedHashMap<String, Float[]> teamJlpResultMap = new LinkedHashMap();
			Iterator iterator = teamJLPResult.iterator();

			while (iterator.hasNext()) {
				DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
				String key = dashBoardReportVO.getUserName();
				Float[] valuesArr = new Float[2];
				Float value = dashBoardReportVO.getCompletedJLP();
				Float value2 = dashBoardReportVO.getInProgressJLP();
				valuesArr[0] = value;
				valuesArr[1] = value2;
				teamJlpResultMap.put(key, valuesArr);
			}

			HashMap<String, String> mp = this.set2SeriesGraphParameters("1150", "310", "Analyst", "Average JLP",
					teamJlpResultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getTeamJLPGraph");
			return mp;
		} catch (CMSException var13) {
			throw new CMSException(this.logger, var13);
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		}
	}

	private HashMap<String, String> getOfficeCasesGraph(Integer selYear, Integer selmonth) throws CMSException {
		new HashMap();
		this.logger.debug("IN JSONViewDashboardController::getOfficeCasesGraph");

		try {
			new ArrayList();
			List<LinkedHashMap> resultMap = this.dashboardManager.getOfficeCasesData(String.valueOf(selYear),
					String.valueOf(selmonth));
			HashMap<String, String> mp = this.setMultiSeriesGraphParameters("1150", "310", "Office", "Number Of Cases",
					resultMap);
			this.logger.debug("Exiting JSONViewDashboardController::getOfficeCasesGraph");
			return mp;
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private List<DashBoardReportVO> getCaseDueForTodayTmrw(String userId, List<String> roleList) throws CMSException {
		new ArrayList();

		try {
			boolean isUserRoleR2OrR9 = false;
			Iterator iterator = roleList.iterator();

			while (iterator.hasNext()) {
				String role = (String) iterator.next();
				if (role.equals("R2") || role.equals("R9")) {
					isUserRoleR2OrR9 = true;
					break;
				}
			}

			this.logger.debug("isUserRoleR2OrR9 " + isUserRoleR2OrR9);
			List<DashBoardReportVO> caseDueList = this.dashboardManager.getCasesDueCount(isUserRoleR2OrR9);
			return caseDueList;
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private List<DashBoardReportVO> getCaseDueForTodayTmrwDetails(String userId, List<String> roleList,
			String dateParam) throws CMSException {
		new ArrayList();

		try {
			boolean isUserRoleR2OrR9 = false;
			Iterator iterator = roleList.iterator();

			while (iterator.hasNext()) {
				String role = (String) iterator.next();
				if (role.equals("R2") || role.equals("R9")) {
					isUserRoleR2OrR9 = true;
					break;
				}
			}

			this.logger.debug("isUserRoleR2OrR9 " + isUserRoleR2OrR9);
			List<DashBoardReportVO> caseDueList = this.dashboardManager
					.getCasesDueTodayTmrwDetailsForR2R9(isUserRoleR2OrR9, dateParam);
			return caseDueList;
		} catch (CMSException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private HashMap<String, String> setGraphParameters(String chartWidth, String chartHeight, String xAxisName,
			String yAxisName, LinkedHashMap<String, Float> resultMap) throws CMSException {
		HashMap mp = new HashMap();

		try {
			String fusionXMLString = "";
			String categories = "";
			String dataset = "";
			String[] colors = new String[]{"d4e2f4"};
			int iterateColors = 0;

			String key;
			Float value;
			for (Iterator mapIterator = resultMap.keySet().iterator(); mapIterator.hasNext(); dataset = dataset
					+ " <set value='" + value + "' color='" + colors[iterateColors] + "' toolText='" + key
					+ "%26lt;BR%26gt;" + yAxisName + " : " + value + "' link='' " + "/>") {
				key = (String) mapIterator.next();
				value = (Float) resultMap.get(key);
				categories = categories + " <category label='" + key + "'/>";
			}

			fusionXMLString = "<chart showValues='0' xAxisName='" + xAxisName + "' yAxisName='" + yAxisName
					+ "' bgColor='FFFFFF' legendPosition='BOTTOM' labelDisplay='Stagger'>" + " <categories>    "
					+ categories + " </categories>    " + " <dataset>" + dataset + " </dataset> " + " <styles>  "
					+ " <definition> "
					+ " <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />"
					+ " <style type='font' name='CaptionFont' size='10' color='666666' />"
					+ " <style type='font' name='SubCaptionFont' bold='0'/>"
					+ " <style name='myHTMLFont' type='font' isHTML='1' />" + " </definition>      "
					+ " <application>  " + " <apply toObject='Canvas' styles='CanvasAnim' />  "
					+ " <apply toObject='caption' styles='CaptionFont'/>  "
					+ " <apply toObject='SubCaption' styles='SubCaptionFont'/>"
					+ " <apply toObject='DATALABELS' styles='CaptionFont'/>"
					+ " <apply toObject='YAXISVALUES' styles='CaptionFont'/>"
					+ " <apply toObject='TOOLTIP' styles='myHTMLFont' />" + " </application> " + " </styles></chart>";
			mp.put("chartWidth", chartWidth);
			mp.put("chartHeight", chartHeight);
			mp.put("fusionXML", fusionXMLString);
			return mp;
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	private HashMap<String, String> set2SeriesGraphParameters(String chartWidth, String chartHeight, String xAxisName,
			String yAxisName, LinkedHashMap<String, Float[]> resultMap) throws CMSException {
		HashMap mp = new HashMap();

		try {
			String fusionXMLString = "";
			String categories = "";
			String dataset = "";
			String datasetSeries2 = "";
			String value1 = "";
			String value2 = "";

			String modifiedKey;
			for (Iterator mapIterator = resultMap.keySet().iterator(); mapIterator
					.hasNext(); datasetSeries2 = datasetSeries2 + " <set value='" + value2 + "' toolText='"
							+ modifiedKey + "%26lt;BR%26gt;In Progress " + yAxisName + " : " + value2 + "' link='' "
							+ "/>") {
				String key = (String) mapIterator.next();
				Float[] valueArray = (Float[]) resultMap.get(key);
				value1 = String.valueOf(valueArray[0]);
				value2 = String.valueOf(valueArray[1]);
				modifiedKey = key.replace("&", "&amp;").replace(">", "%26gt;").replace("<", "%26lt;").replace("%",
						"&#37;");
				categories = categories + " <category label='" + modifiedKey + "'/>";
				dataset = dataset + " <set value='" + value1 + "'  toolText='" + modifiedKey
						+ "%26lt;BR%26gt;Completed " + yAxisName + " : " + value1 + "' link='' " + "/>";
			}

			fusionXMLString = "<chart showValues='0' xAxisName='" + xAxisName + "' yAxisName='" + yAxisName
					+ "' bgColor='FFFFFF' legendPosition='BOTTOM' labelDisplay='Stagger'>" + " <categories>    "
					+ categories + " </categories>    " + " <dataset seriesName='Completed'>" + dataset + " </dataset> "
					+ " <dataset  seriesName='In Progress'>" + datasetSeries2 + " </dataset> " + " <styles>  "
					+ " <definition> "
					+ " <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />"
					+ " <style type='font' name='CaptionFont' size='10' color='666666' />"
					+ " <style type='font' name='SubCaptionFont' bold='0'/>"
					+ " <style name='myHTMLFont' type='font' isHTML='1' />" + " </definition>      "
					+ " <application>  " + " <apply toObject='Canvas' styles='CanvasAnim' />  "
					+ " <apply toObject='caption' styles='CaptionFont'/>  "
					+ " <apply toObject='SubCaption' styles='SubCaptionFont'/>"
					+ " <apply toObject='DATALABELS' styles='CaptionFont'/>"
					+ " <apply toObject='YAXISVALUES' styles='CaptionFont'/>"
					+ " <apply toObject='TOOLTIP' styles='myHTMLFont' />" + " </application> " + " </styles></chart>";
			mp.put("chartWidth", chartWidth);
			mp.put("chartHeight", chartHeight);
			mp.put("fusionXML", fusionXMLString);
			return mp;
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	private HashMap<String, String> setMultiSeriesGraphParameters(String chartWidth, String chartHeight,
			String xAxisName, String yAxisName, List<LinkedHashMap> resultMapList) throws CMSException {
		HashMap mp = new HashMap();

		try {
			LinkedHashMap<String, String> dataSetSeriesMap = new LinkedHashMap();
			String fusionXMLString = "";
			String categories = "";
			String[] colorArray = StringUtils.hexCodeGeneratorForColors();
			Iterator reportTypeItr = resultMapList.iterator();

			String key;
			String dtString;
			while (reportTypeItr.hasNext()) {
				LinkedHashMap<String, String> resultMap = (LinkedHashMap) reportTypeItr.next();
				Iterator mapIterator = resultMap.keySet().iterator();

				while (mapIterator.hasNext()) {
					key = (String) mapIterator.next();
					if (key.equalsIgnoreCase("Office")) {
						dtString = (String) resultMap.get(key);
						dtString = dtString.replace("&", "&amp;").replace(">", "%26gt;").replace("<", "%26lt;")
								.replace("%", "&#37;");
						categories = categories + " <category label='" + dtString + "'/>";
					} else {
						dtString = "";
						String reportTypeWithoutCustomPrefix = key.substring(6, key.length());
						this.logger.debug("reportTypeName " + key);
						this.logger.debug("reportTypeWithoutCustomPrefix " + reportTypeWithoutCustomPrefix);
						String caseCount = String.valueOf(resultMap.get(key));
						if (dataSetSeriesMap.containsKey(key)) {
							dtString = (String) dataSetSeriesMap.get(key);
							dtString = dtString + " <set value='" + caseCount + "' toolText='"
									+ reportTypeWithoutCustomPrefix + "%26lt;BR%26gt;" + yAxisName + " : " + caseCount
									+ "' link='' " + "/>";
						} else {
							dtString = " <set value='" + caseCount + "' toolText='" + reportTypeWithoutCustomPrefix
									+ "%26lt;BR%26gt;" + yAxisName + " : " + caseCount + "' link='' " + "/>";
						}

						dataSetSeriesMap.put(key, dtString);
					}
				}
			}

			reportTypeItr = dataSetSeriesMap.keySet().iterator();
			String compDataSetString = "";
			int count = 0;

			for (key = ""; reportTypeItr.hasNext(); ++count) {
				dtString = (String) reportTypeItr.next();
				String reportTypeWithoutCustomPrefix = dtString.substring(6, dtString.length());
				key = colorArray[count];
				key = key.substring(1, key.length());
				compDataSetString = compDataSetString + " <dataset color='" + key + "' seriesName='"
						+ reportTypeWithoutCustomPrefix + "'" + "   > " + (String) dataSetSeriesMap.get(dtString)
						+ " </dataset> ";
			}

			this.logger.debug("compDataSetString " + compDataSetString);
			fusionXMLString = "<chart showValues='0' xAxisName='" + xAxisName + "' yAxisName='" + yAxisName
					+ "' bgColor='FFFFFF' legendPosition='BOTTOM' labelDisplay='Stagger'>" + " <categories>    "
					+ categories + " </categories>    " + compDataSetString + " <styles>  " + " <definition> "
					+ " <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />"
					+ " <style type='font' name='CaptionFont' size='10' color='666666' />"
					+ " <style type='font' name='SubCaptionFont' bold='0'/>"
					+ " <style name='myHTMLFont' type='font' isHTML='1' />" + " </definition>      "
					+ " <application>  " + " <apply toObject='Canvas' styles='CanvasAnim' />  "
					+ " <apply toObject='caption' styles='CaptionFont'/>  "
					+ " <apply toObject='SubCaption' styles='SubCaptionFont'/>"
					+ " <apply toObject='DATALABELS' styles='CaptionFont'/>"
					+ " <apply toObject='YAXISVALUES' styles='CaptionFont'/>"
					+ " <apply toObject='TOOLTIP' styles='myHTMLFont' />" + " </application> " + " </styles></chart>";
			mp.put("chartWidth", chartWidth);
			mp.put("chartHeight", chartHeight);
			mp.put("fusionXML", fusionXMLString);
			return mp;
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	private String getPreviousDates(int dayToSubtract) {
		Calendar cal = Calendar.getInstance();
		cal.add(5, -dayToSubtract);
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String resultDate = sdf.format(cal.getTime());
		return resultDate;
	}

	private String getCompletePreviousDate(int dayToSubtract) {
		Calendar cal = Calendar.getInstance();
		cal.add(5, -dayToSubtract);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String resultDate = sdf.format(cal.getTime());
		return resultDate;
	}
}