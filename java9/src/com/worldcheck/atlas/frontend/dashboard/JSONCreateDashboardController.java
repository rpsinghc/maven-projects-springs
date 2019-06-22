package com.worldcheck.atlas.frontend.dashboard;

import com.savvion.acl.impl.SBMACLUser;
import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.savvion.sbm.bpmportal.domain.DashboardWidget;
import com.savvion.sbm.bpmportal.service.DashboardService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.dashboard.DashBoardVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public class JSONCreateDashboardController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.dashboard.JSONViewDashboardController");
	private static PropertyReaderUtil propertyReader;
	private final String SBM_ACL_USER_OBJ = "sbmAclUserObj";
	private final String SEL_WIDGET_LIST = "selWidgetList";
	private final String AVL_WIDGET_LIST = "avlWidgetList";
	private final String REQ_DASH_NAME = "dashName";
	private final String DASH_EXISTS = "dashBoardExists";
	protected DashboardService dashboardService;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public DashboardService getDashboardService() {
		return this.dashboardService;
	}

	public void setDashboardService(DashboardService dashboardservice) {
		this.dashboardService = dashboardservice;
	}

	public ModelAndView getAllDashboardMetrics(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		modelandview = new ModelAndView("jsonView");
		this.logger.debug("IN JSONViewDashboardController::getAllDashboardMetrics");

		try {
			List<String> dashboardMetricslist = StringUtils
					.commaSeparatedStringToList(propertyReader.getDashboardMetrics());
			List<DashBoardVO> dashboardVOList = new ArrayList();
			Iterator iterator = dashboardMetricslist.iterator();

			while (iterator.hasNext()) {
				DashBoardVO dashboardVO = new DashBoardVO();
				String metricsName = (String) iterator.next();
				dashboardVO.setDashboardMetrics(metricsName);
				dashboardVOList.add(dashboardVO);
			}

			modelandview.addObject("dashboardVOList", dashboardVOList);
		} catch (CMSException var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			AtlasUtils.getExceptionView(this.logger, var10);
		}

		this.logger.debug("Exiting JSONViewDashboardController::getAllDashboardMetrics");
		return modelandview;
	}

	public ModelAndView getUserDashboardMetrics(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		modelandview = new ModelAndView("jsonView");
		this.logger.debug("IN JSONViewDashboardController::getAllDashboardMetrics");

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List<Dashboard> dashList = this.getuserDefaultDashBoard(request, response);
			List<String> dashboardAllMetricslist = StringUtils
					.commaSeparatedStringToList(propertyReader.getDashboardMetrics());
			List<String> dashboardRolewiseMetricslist = this.checkPermission(userDetailsBean, dashboardAllMetricslist);
			List<DashBoardVO> dashboardAvalialableMetricsList = new ArrayList();
			List<DashBoardVO> dashboardSelectedMetricsList = new ArrayList();
			Map<String, String> metricsWidgetNameMap = this.getMetricsWidgetNameMap();
			if (dashList.size() > 0 && ((Dashboard) dashList.get(0)).getId() != -1L) {
				Dashboard dash = this.getDashboardService().getDashboardById(((Dashboard) dashList.get(0)).getId());
				List<DashboardWidget> addedWidgetsList = dash.getDashboardWidgets();
				Iterator iterator = dashboardRolewiseMetricslist.iterator();

				while (iterator.hasNext()) {
					String str = (String) iterator.next();
					boolean isExists = false;
					Iterator iterator2 = addedWidgetsList.iterator();

					while (iterator2.hasNext()) {
						DashboardWidget dashboardWidget = (DashboardWidget) iterator2.next();
						if (str.equalsIgnoreCase(dashboardWidget.getDashboardWidgetName())) {
							isExists = true;
						}
					}

					DashBoardVO dashBoardVO;
					if (isExists) {
						dashBoardVO = new DashBoardVO();
						dashBoardVO.setDashboardMetrics(str);
						dashBoardVO.setDashboardMetricsFullName((String) metricsWidgetNameMap.get(str));
						dashboardSelectedMetricsList.add(dashBoardVO);
					} else {
						dashBoardVO = new DashBoardVO();
						dashBoardVO.setDashboardMetrics(str);
						dashBoardVO.setDashboardMetricsFullName((String) metricsWidgetNameMap.get(str));
						dashboardAvalialableMetricsList.add(dashBoardVO);
					}
				}
			} else {
				Iterator iterator = dashboardRolewiseMetricslist.iterator();

				while (iterator.hasNext()) {
					DashBoardVO dashboardVO = new DashBoardVO();
					String metricsName = (String) iterator.next();
					dashboardVO.setDashboardMetrics(metricsName);
					dashboardVO.setDashboardMetricsFullName((String) metricsWidgetNameMap.get(metricsName));
					dashboardAvalialableMetricsList.add(dashboardVO);
				}
			}

			modelandview.addObject("selWidgetList", dashboardSelectedMetricsList);
			modelandview.addObject("avlWidgetList", dashboardAvalialableMetricsList);
			this.logger.debug("dashboardAvalialableMetricsList size" + dashboardAvalialableMetricsList.size());
		} catch (CMSException var18) {
			AtlasUtils.getExceptionView(this.logger, var18);
		} catch (Exception var19) {
			AtlasUtils.getExceptionView(this.logger, var19);
		}

		this.logger.debug("Exiting JSONViewDashboardController::getAllDashboardMetrics");
		return modelandview;
	}

	private List<String> checkPermission(UserDetailsBean userDetailsBean, List<String> dashboardAllMetricslist) {
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
			if (userDetailsBean != null) {
				Map permissionMap = userDetailsBean.getPermissionMap();
				Iterator iterator = dashboardAllMetricslist.iterator();

				while (iterator.hasNext()) {
					String metricsName = (String) iterator.next();
					String permissionKey = (String) metricsPermissionMap.get(metricsName);
					String permissionType = (String) permissionMap.get(permissionKey);
					if ("A".equalsIgnoreCase(permissionType)) {
						dashboardPermissibleMetricsLst.add(metricsName);
					}
				}
			}
		} catch (Exception var10) {
			new CMSException(this.logger, var10);
		}

		return dashboardPermissibleMetricsLst;
	}

	private List<Dashboard> getuserDefaultDashBoard(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("In CreateDashboardController::getuserDefaultDashBoard");
		new ArrayList();

		List dashList;
		try {
			HttpSession httpsession = request.getSession(false);
			SBMACLUser sbmacluser = (SBMACLUser) httpsession.getAttribute("sbmAclUserObj");
			dashList = this.getDashboardService().getUserDefaultDashboard(sbmacluser, true);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("In CreateDashboardController::getuserDefaultDashBoard");
		return dashList;
	}

	private Map<String, String> getMetricsWidgetNameMap() throws CMSException {
		this.logger.debug("In CreateDashboardController::getMetricsWidgetNameMap");
		HashMap metricsWidgetNameMap = new HashMap();

		try {
			metricsWidgetNameMap.put("CasesDue", "Cases Due");
			metricsWidgetNameMap.put("CasesOverdue", "Cases Overdue");
			metricsWidgetNameMap.put("ExpenditureCostPerReport", "Expenditure Cost/Report");
			metricsWidgetNameMap.put("MyTeamJLP", "My Team's JLP");
			metricsWidgetNameMap.put("MyTeamLeave", "My Team's Leave");
			metricsWidgetNameMap.put("OfficeCasesWIPAndCompleted", "Office Cases (WIP and Completed)");
			metricsWidgetNameMap.put("OfficeJLP", "Office JLP");
			metricsWidgetNameMap.put("OfficeRevenue", "Office Revenue");
			metricsWidgetNameMap.put("ProfitPerReport", "Profit/Report");
			metricsWidgetNameMap.put("PublicHoliday", "Public Holiday");
			metricsWidgetNameMap.put("RevenuePerReport", "Revenue/Report");
			metricsWidgetNameMap.put("TopClients", "Top Clients");
			metricsWidgetNameMap.put("TotalCasesCreatedCM", "Total Cases Created (CM)");
			metricsWidgetNameMap.put("TotalISISCasesCreatedCM", "Total EDDO Cases Created (CM)");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("In CreateDashboardController::getMetricsWidgetNameMap");
		return metricsWidgetNameMap;
	}

	public ModelAndView checkDuplicateDashboardName(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelandview = null;
		modelandview = new ModelAndView("jsonView");
		String requestedDashboardName = "";

		try {
			if (request.getParameter("dashName") != null) {
				requestedDashboardName = request.getParameter("dashName");
			}

			Dashboard dash = this.getDashboardService().getDashboardByName(requestedDashboardName);
			if (dash != null) {
				modelandview.addObject("dashBoardExists", "true");
			} else {
				modelandview.addObject("dashBoardExists", "false");
			}
		} catch (Exception var6) {
			AtlasUtils.getExceptionView(this.logger, var6);
		}

		return modelandview;
	}
}