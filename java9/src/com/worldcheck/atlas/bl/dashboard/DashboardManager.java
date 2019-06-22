package com.worldcheck.atlas.bl.dashboard;

import com.savvion.acl.impl.SBMACLUser;
import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.savvion.sbm.bpmportal.domain.DashboardWidget;
import com.savvion.sbm.bpmportal.domain.Widget;
import com.worldcheck.atlas.dao.dashboard.DashboardDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.dashboard.DashBoardReportVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.dashboard.DashboardManager");
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String USER_ID = "userId";
	private static final String FROM_ISIS = "fromISIS";
	private static final String CLIENT_NAME = "clientName";
	private static final String REQUEST_RECVD_DATE = "requestRecvdDate";
	private static final String CASE_MGR_ID = "caseMgrId";
	private static final String OFFICE_ID = "officeId";
	private static final String QUERY_STR = "queryStringList";
	private static final String DATE_PARAM_STR = "dateParam";
	private static final String NO_CYCLE = "No cycle";
	private static final String USER_UPDATED_DASHBOARD = "userUpdatedDashboard";
	private static final String OLD_DASHBOARD_DELETE = "oldDashboardDelete";
	private static final String YES_STR = "yes";
	private static final String NO_STR = "no";
	private DashboardDAO dashboardDAO;

	public void setDashboardDAO(DashboardDAO dashboardDAO) {
		this.dashboardDAO = dashboardDAO;
	}

	public List<DashBoardReportVO> getTopClientData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getTopClientData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List topClientDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			topClientDataList = this.dashboardDAO.getTopClientData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getTopClientData");
		return topClientDataList;
	}

	public List<DashBoardReportVO> getExpenditureData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getExpenditureData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List expenditureDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			expenditureDataList = this.dashboardDAO.getExpenditureReportData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getExpenditureData");
		return expenditureDataList;
	}

	public List<DashBoardReportVO> getProfitData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getProfitData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List profitDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			profitDataList = this.dashboardDAO.getProfitReportData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getProfitData");
		return profitDataList;
	}

	public List<DashBoardReportVO> getRevenueData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getRevenueData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List revenueDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			revenueDataList = this.dashboardDAO.getRevenueReportData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getRevenueData");
		return revenueDataList;
	}

	public List<DashBoardReportVO> getOfficeRevenueData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getOfficeRevenueData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List ofcRevenueDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			ofcRevenueDataList = this.dashboardDAO.getOfficeRevenueReportData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getOfficeRevenueData");
		return ofcRevenueDataList;
	}

	public List<DashBoardReportVO> getOfficeJLPData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getOfficeJLPData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List ofcRevenueDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			ofcRevenueDataList = this.dashboardDAO.getOfficeJLPReportData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getOfficeJLPData");
		return ofcRevenueDataList;
	}

	public List<DashBoardReportVO> getTeamLeaveData(String year, String month, String userId) throws CMSException {
		this.logger.debug("IN DashboardManager::getTeamLeaveData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List teamLeaveList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			paramList.put("userId", userId);
			teamLeaveList = this.dashboardDAO.getTeamLeaveData(paramList);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting DashboardManager::getTeamLeaveData");
		return teamLeaveList;
	}

	public List<DashBoardReportVO> getPublicHolidayData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getPublicHolidayData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List pHolidayList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			pHolidayList = this.dashboardDAO.getPublicHolidayData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getPublicHolidayData");
		return pHolidayList;
	}

	public List<DashBoardReportVO> getCaseOverdueData(String userID, boolean isUserRoleR2OrR9) throws CMSException {
		this.logger.debug("IN DashboardManager::getCaseOverdueData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List cOverdueList;
		try {
			paramList.put("userId", userID);
			if (isUserRoleR2OrR9) {
				cOverdueList = this.dashboardDAO.getCaseOverdueDataForR2R9(paramList);
			} else {
				cOverdueList = this.dashboardDAO.getCaseOverdueData();
			}
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getCaseOverdueData");
		return cOverdueList;
	}

	public List<DashBoardReportVO> getTeamJLPData(String year, String month, String userId) throws CMSException {
		this.logger.debug("IN DashboardManager::getTeamJLPData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List teamJLPDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			paramList.put("userId", userId);
			teamJLPDataList = this.dashboardDAO.getTeamJLPData(paramList);
			this.logger.debug("teamJLPDataList size " + teamJLPDataList.size());
			int analystCount = false;
			if (teamJLPDataList != null && teamJLPDataList.size() > 0) {
				int analystCount = teamJLPDataList.size();
				Iterator iterator = teamJLPDataList.iterator();

				while (iterator.hasNext()) {
					DashBoardReportVO dashBoardReportVO = (DashBoardReportVO) iterator.next();
					dashBoardReportVO.setCompletedJLP(dashBoardReportVO.getCompletedJLP() / (float) analystCount);
					dashBoardReportVO.setInProgressJLP(dashBoardReportVO.getInProgressJLP() / (float) analystCount);
				}
			}
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting DashboardManager::getTeamJLPData");
		return teamJLPDataList;
	}

	public List<LinkedHashMap> getOfficeCasesData(String year, String month) throws CMSException {
		this.logger.debug("IN DashboardManager::getOfficeCasesData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List officeCasesDataList;
		try {
			paramList.put("year", year);
			paramList.put("month", month);
			List<ReportTypeMasterVO> reportTypeVOList = this.dashboardDAO.getAllReportCrnInitial();
			List<String> queryStringList = new ArrayList();
			Iterator iterator = reportTypeVOList.iterator();

			while (true) {
				if (!iterator.hasNext()) {
					paramList.put("queryStringList", queryStringList);
					officeCasesDataList = this.dashboardDAO.getOfficeCasesData(paramList);
					break;
				}

				ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) iterator.next();
				String queryStr = "";
				queryStr = "sum(case when (REPORT_TYPE = '" + reportTypeMasterVO.getReportType() + "') then "
						+ "(CASE_COUNT_WIP+CASE_COUNT_CMP) else 0 end) as " + reportTypeMasterVO.getInitialsUseCRN()
						+ "";
				queryStringList.add(queryStr);
			}
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		this.logger.debug("Exiting DashboardManager::getOfficeCasesData");
		return officeCasesDataList;
	}

	public List<DashBoardReportVO> getTotalCasesCmData(String userId, String fromISIS) throws CMSException {
		this.logger.debug("IN DashboardManager::getTotalCasesCmData");
		new ArrayList();
		HashMap paramList = new HashMap();

		List tCaseCMDataList;
		try {
			paramList.put("userId", userId);
			paramList.put("fromISIS", fromISIS);
			tCaseCMDataList = this.dashboardDAO.getTotalCasesCmData(paramList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting DashboardManager::getTotalCasesCmData");
		return tCaseCMDataList;
	}

	public List<DashBoardReportVO> getTotalCaseCountCmPerReport(String clientName, String requestRecvdDate,
			String caseMgrId, String fromISIS) throws CMSException {
		this.logger.debug("IN DashboardManager::getTotalCaseCountCmPerReport");
		new ArrayList();
		HashMap paramList = new HashMap();

		List tCaseCMPerRptList;
		try {
			paramList.put("clientName", clientName);
			paramList.put("requestRecvdDate", requestRecvdDate);
			paramList.put("caseMgrId", caseMgrId);
			paramList.put("fromISIS", fromISIS);
			tCaseCMPerRptList = this.dashboardDAO.getTotalCaseCountCmPerReport(paramList);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("Exiting DashboardManager::getTotalCaseCountCmPerReport");
		return tCaseCMPerRptList;
	}

	public List<DashBoardReportVO> getCasesDueCount(boolean isUserR2R9) throws CMSException {
		this.logger.debug("IN DashboardManager::getCasesDueCount");
		new ArrayList();

		List casesDueResult;
		try {
			if (isUserR2R9) {
				casesDueResult = this.dashboardDAO.getCasesDueForR2R9();
			} else {
				casesDueResult = this.dashboardDAO.getCasesDueForOtherUsers();
			}
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting DashboardManager::getCasesDueCount");
		return casesDueResult;
	}

	public List<DashBoardReportVO> getCasesDueTodayTmrwDetailsForR2R9(boolean isUserR2R9, String dateParam)
			throws CMSException {
		this.logger.debug("IN DashboardManager::getCasesDueTodayTmrwDetails");
		new ArrayList();
		HashMap paramList = new HashMap();

		List casesDueResult;
		try {
			paramList.put("dateParam", dateParam);
			Iterator iterator;
			DashBoardReportVO dashBoardReportVO;
			if (isUserR2R9) {
				casesDueResult = this.dashboardDAO.getCasesDueTodayTmrwDetails(paramList);
				iterator = casesDueResult.iterator();

				while (iterator.hasNext()) {
					dashBoardReportVO = (DashBoardReportVO) iterator.next();
					dashBoardReportVO.setIsUserR2R9("true");
					if (!dashBoardReportVO.getOverdueStage().equals("No cycle")) {
						this.getTeamStatus(dashBoardReportVO,
								dashBoardReportVO.getTeamType() + "#" + dashBoardReportVO.getTeamID(),
								dashBoardReportVO.getOverdueStage(), Long.parseLong(dashBoardReportVO.getPid()));
					} else {
						dashBoardReportVO.setTeamTaskStatus("");
						dashBoardReportVO.setOffice("");
						dashBoardReportVO.setTeamType("");
					}
				}
			} else {
				casesDueResult = this.dashboardDAO.getCasesDueTodayTmrwDetailsForOthers(paramList);
				iterator = casesDueResult.iterator();

				while (iterator.hasNext()) {
					dashBoardReportVO = (DashBoardReportVO) iterator.next();
					dashBoardReportVO.setIsUserR2R9("false");
					this.logger.debug("goiin to fetch team status for crn--> " + dashBoardReportVO.getCrn());
					this.getTeamStatus(dashBoardReportVO,
							dashBoardReportVO.getTeamType() + "#" + dashBoardReportVO.getTeamID(),
							dashBoardReportVO.getOverdueStage(), Long.parseLong(dashBoardReportVO.getPid()));
				}
			}
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting DashboardManager::getCasesDueTodayTmrwDetails");
		return casesDueResult;
	}

	private DashBoardReportVO getTeamStatus(DashBoardReportVO dashReportVO, String teamName, String cycle,
			long parentPID) throws CMSException {
		this.logger.debug("Inside DashboardManager::getTeamStatus");
		boolean isResearchDone = false;
		boolean isConsolidationActive = false;
		boolean isConsolidationDone = false;
		this.logger.debug("teamName-------- " + teamName);
		Session session = ResourceLocator.self().getSBMService().getSession();
		HashMap customDSMap = (HashMap) ResourceLocator.self().getSBMService().getDataslotValue(parentPID,
				"customDSMap", session);

		try {
			if (customDSMap != null) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				Map<String, CycleInfo> allCycleInformation = cycleTeamMapping.getCycleInformation();
				CycleInfo cycleInfo = (CycleInfo) allCycleInformation.get(cycle);
				Map<String, TeamAnalystMapping> teamInfoForCycle = null;
				if (cycleInfo != null) {
					teamInfoForCycle = cycleInfo.getTeamInfo();
				}

				if (teamInfoForCycle != null && teamInfoForCycle.containsKey(teamName)) {
					TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) teamInfoForCycle.get(teamName);
					this.logger.debug(
							"teamAnalystMapping.getResearchTaskStatus:- " + teamAnalystMapping.getResearchTaskStatus());
					if (null != teamAnalystMapping.getResearchTaskStatus()
							&& teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
						isResearchDone = true;
					} else if (null == teamAnalystMapping.getResearchTaskStatus()) {
						dashReportVO.setTeamTaskStatus("");
						dashReportVO.setTeamType("");
					} else {
						dashReportVO.setTeamTaskStatus("Research Task");
					}

					String[] activatedWSNames = new String[0];
					if (teamName.contains("Primary")) {
						activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(session,
								parentPID);
					} else {
						activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(session,
								teamAnalystMapping.getResearchProcessPID());
					}

					for (int i = 0; i < activatedWSNames.length; ++i) {
						this.logger.debug("activatedWSNames[i]-->" + activatedWSNames[i]);
						if (activatedWSNames[i].equalsIgnoreCase("Consolidation Task")) {
							dashReportVO.setTeamTaskStatus("Consolidation Task");
							isConsolidationActive = true;
						} else {
							LinkedHashMap reviewersForReviewProcess;
							String currentReviewer;
							Set keySet;
							int index;
							Iterator iterator;
							String key;
							if (activatedWSNames[i].equalsIgnoreCase("FinalReview")) {
								dashReportVO.setTeamTaskStatus("Review Task");
								reviewersForReviewProcess = teamAnalystMapping.getReviewers();
								currentReviewer = (String) ResourceLocator.self().getSBMService().getDataslotValue(
										teamAnalystMapping.getReviewProcessPID(), "Reviewer", session);
								if (reviewersForReviewProcess != null) {
									keySet = reviewersForReviewProcess.keySet();
									index = 1;

									for (iterator = keySet.iterator(); iterator.hasNext(); ++index) {
										key = (String) iterator.next();
										if (key.equalsIgnoreCase(currentReviewer)) {
											dashReportVO.setTeamTaskStatus("Review " + index + " Task");
											break;
										}
									}
								}
							} else if (activatedWSNames[i].equalsIgnoreCase("Review")) {
								dashReportVO.setTeamTaskStatus("Supporting Reviewer Task");
								reviewersForReviewProcess = teamAnalystMapping.getReviewers();
								currentReviewer = (String) ResourceLocator.self().getSBMService().getDataslotValue(
										teamAnalystMapping.getReviewProcessPID(), "Reviewer", session);
								if (reviewersForReviewProcess != null) {
									keySet = reviewersForReviewProcess.keySet();
									index = 1;

									for (iterator = keySet.iterator(); iterator.hasNext(); ++index) {
										key = (String) iterator.next();
										if (key.equalsIgnoreCase(currentReviewer)) {
											dashReportVO.setTeamTaskStatus("Review " + index + " Task");
											break;
										}
									}
								}
							}
						}
					}

					String[] completedWSNames = new String[0];
					if (teamName.contains("Primary") && isResearchDone && !isConsolidationActive
							&& teamName.contains("Primary")) {
						completedWSNames = ResourceLocator.self().getSBMService().getCompletedWSNames(parentPID,
								session);

						for (int k = 0; k < completedWSNames.length; ++k) {
							this.logger.debug("completedWSNames[k] " + completedWSNames[k]);
							if (completedWSNames[k].equalsIgnoreCase("Consolidation Task")) {
								isConsolidationDone = true;
								break;
							}
						}

						if (!isConsolidationDone) {
							dashReportVO.setTeamTaskStatus("Awaiting Consolidation");
						}
					}
				}
			} else {
				dashReportVO.setTeamTaskStatus("");
				dashReportVO.setOffice("");
				dashReportVO.setTeamType("");
			}
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}

		this.logger.debug("dashReportVO.getTeamTaskStatus:- " + dashReportVO.getTeamTaskStatus());
		this.logger.debug("Inside DashboardManager::getTeamStatus");
		return dashReportVO;
	}

	public Map updateUserDashboard(String userId, Map updatedRoleMap, Dashboard userDashboard, SBMACLUser sbmacluser)
			throws CMSException {
		this.logger.debug("in CreateDashboardController::updateUserDashboard ");
		Map decisionMap = new HashMap();
		Dashboard userUpdatedDashboard = new Dashboard();
		new ArrayList();
		List<String> dashboardPermissibleMetricsLst = this.getAvailableMetricsList(updatedRoleMap);
		List dashboardWidgetList = new ArrayList();
		List<DashboardWidget> addedWidgetsList = userDashboard.getDashboardWidgets();
		this.logger.debug("getId-- () " + userDashboard.getId());
		this.logger.debug("addedWidgetsList.size() " + addedWidgetsList.size());
		int i = -1;
		Iterator iterator2 = addedWidgetsList.iterator();

		while (iterator2.hasNext()) {
			DashboardWidget dashboardWidget = (DashboardWidget) iterator2.next();
			if (dashboardPermissibleMetricsLst.contains(dashboardWidget.getDashboardWidgetName())) {
				++i;
				DashboardWidget dw = new DashboardWidget();
				Widget widget = dashboardWidget.getWidget();
				dw.setWidget(widget);
				dw.setDashboardWidgetColumn(i % 2);
				dw.setDashboardWidgetName(widget.getName());
				dw.setDashboardWidgetDesc(widget.getDescription());
				dw.setDashboardWidgetTitle(widget.getTitle());
				dw.setDashboardWidgetRow(0);
				dw.setWidgetId(widget.getId());
				dashboardWidgetList.add(dw);
			}
		}

		this.logger.debug("Size of DashBoardWidgets list is::" + dashboardWidgetList.size());
		if (dashboardWidgetList.size() == addedWidgetsList.size()) {
			this.logger.debug("no need to creaete again");
		}

		if (dashboardWidgetList.size() > 0 && dashboardWidgetList.size() != addedWidgetsList.size()) {
			userUpdatedDashboard.setName(userDashboard.getName());
			userUpdatedDashboard.setLayoutType(0);
			userUpdatedDashboard.setDescription(userDashboard.getDescription());
			userUpdatedDashboard.setDashboardWidgets(dashboardWidgetList);
			List<String> userList = new ArrayList();
			userList.add(sbmacluser.getName());
			userUpdatedDashboard.setUsers(userList);
			userUpdatedDashboard.setPublished(true);
			this.logger.debug("Creating dashboard with Name>> " + userUpdatedDashboard.getName());
			this.logger.debug("dashboard created");
			decisionMap.put("userUpdatedDashboard", userUpdatedDashboard);
			decisionMap.put("oldDashboardDelete", "yes");
		} else if (dashboardWidgetList.size() == 0) {
			userUpdatedDashboard = null;
			decisionMap.put("userUpdatedDashboard", userUpdatedDashboard);
			decisionMap.put("oldDashboardDelete", "yes");
		} else {
			userUpdatedDashboard = null;
			decisionMap.put("userUpdatedDashboard", userUpdatedDashboard);
			decisionMap.put("oldDashboardDelete", "no");
		}

		this.logger.debug("Exiting  CreateDashboardController::updateUserDashboard ");
		return decisionMap;
	}

	private List<String> getAvailableMetricsList(Map updatedRoleMap) throws CMSException {
		this.logger.debug("in CreateDashboardController::getAvailableMetricsList ");
		ArrayList dashboardPermissibleMetricsLst = new ArrayList();

		try {
			Map<String, String> metricsPermissionMap = new HashMap();
			metricsPermissionMap.put("CasesDue", "cms.CasesDueMetrics");
			metricsPermissionMap.put("CasesOverdue", "cms.CasesOverdueMetrics");
			metricsPermissionMap.put("ExpenditureCostPerReport", "cms.ExpenditureCostPerMetrics");
			metricsPermissionMap.put("MyTeamJLP", "cms.MyTeamJLPMetrics");
			metricsPermissionMap.put("MyTeamLeave", "cms.MyTeamLeaveMetrics");
			metricsPermissionMap.put("OfficeCasesWIPAndCompleted", "cms.OfficeCasesWIPAndCompletedMetrics");
			metricsPermissionMap.put("OfficeJLP", "cms.OfficeJLPMetrics");
			metricsPermissionMap.put("OfficeRevenue", "cms.OfficeRevenueMetrics");
			metricsPermissionMap.put("ProfitPerReport", "cms.ProfitPerReportMetrics");
			metricsPermissionMap.put("PublicHoliday", "cms.PublicHolidayMetrics");
			metricsPermissionMap.put("RevenuePerReport", "cms.RevenuePerReportMetrics");
			metricsPermissionMap.put("TopClients", "cms.TopClientsMetrics");
			metricsPermissionMap.put("TotalCasesCreatedCM", "cms.TotalCasesCreatedCMMetrics");
			metricsPermissionMap.put("TotalISISCasesCreatedCM", "cms.TotalISISCasesCreatedCMMetrics");
			Iterator<String> mapIterator = metricsPermissionMap.keySet().iterator();
			Map updatedPermissionMap = (Map) updatedRoleMap.get("permissionMap");

			while (true) {
				if (!mapIterator.hasNext()) {
					this.logger.debug("dashboardPermissibleMetricsLst size" + dashboardPermissibleMetricsLst.size());
					break;
				}

				String permissionKey = (String) mapIterator.next();
				String permissionValue = (String) metricsPermissionMap.get(permissionKey);
				String permissionType = (String) updatedPermissionMap.get(permissionValue);
				if ("A".equalsIgnoreCase(permissionType)) {
					this.logger.debug("permissionKey is avaibale " + permissionKey);
					dashboardPermissibleMetricsLst.add(permissionKey);
				}
			}
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting CreateDashboardController::getAvailableMetricsList ");
		return dashboardPermissibleMetricsLst;
	}
}