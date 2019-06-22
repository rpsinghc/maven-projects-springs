package com.worldcheck.atlas.frontend.dashboard;

import com.savvion.acl.impl.SBMACLUser;
import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.savvion.sbm.bpmportal.domain.DashboardWidget;
import com.savvion.sbm.bpmportal.domain.Widget;
import com.savvion.sbm.bpmportal.service.DashboardService;
import com.tdiinc.userManager.JDBCRealm;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.dashboard.DashBoardVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CreateDashboardController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.dashboard.CreateDashboardController");
	private static final String JSP_NAME = "createDashboard";
	private final String redirectStringForViewStats = "redirect:viewStats.do";
	private final String DASHBOARD_NAME = "dashboardName";
	private final String DASHBOARD_DESC = "dashboardDescription";
	private final String WIDGET_LIST = "widgetList";
	private final String SBM_ACL_USER_OBJ = "sbmAclUserObj";
	private final String IS_DASH_EXISTS = "isDashboardExists";
	private final String TRUE = "true";
	private final String REQ_DASH_NAME = "dashName";
	private final String REQ_DASH_DESC = "txtDashDescription";
	private final String REQ_DASH_SELCTED_METRICS = "selectedMetricsName";
	private final String DASH_ID_JSP = "hdnDashboardId";
	private final String DASH_NAME = "dashName";
	protected DashboardService dashboardService;
	private static PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public DashboardService getDashboardService() {
		return this.dashboardService;
	}

	public void setDashboardService(DashboardService dashboardservice) {
		this.dashboardService = dashboardservice;
	}

	public ModelAndView setupCreateDashboard(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In CreateDashboardController::setupCreateDashboard");
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("createDashboard");

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

			List<Dashboard> dashList = this.getuserDefaultDashBoard(request, response);
			if (dashList != null && !dashList.isEmpty() && dashList.size() > 0
					&& ((Dashboard) dashList.get(0)).getId() != -1L) {
				this.logger.debug("dashboard exists for user");
				Dashboard dash = this.getDashboardService().getDashboardById(((Dashboard) dashList.get(0)).getId());
				List<DashboardWidget> addedWidgetsList = dash.getDashboardWidgets();
				String widgetCSVstr = "";

				DashboardWidget dashboardWidget;
				for (Iterator iterator = addedWidgetsList.iterator(); iterator
						.hasNext(); widgetCSVstr = widgetCSVstr + "," + dashboardWidget.getDashboardWidgetName()) {
					dashboardWidget = (DashboardWidget) iterator.next();
				}

				modelAndView.addObject("dashboardName", ((Dashboard) dashList.get(0)).getName());
				modelAndView.addObject("dashboardId", ((Dashboard) dashList.get(0)).getId());
				modelAndView.addObject("dashboardDescription", ((Dashboard) dashList.get(0)).getDescription());
				modelAndView.addObject("widgetList", StringUtils.commaSeparatedStringToList(widgetCSVstr));
			}

			modelAndView.addObject("dashboardVOList", dashboardVOList);
		} catch (CMSException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}

		this.logger.debug("EXITING CreateDashboardController::setupCreateDashboard");
		return modelAndView;
	}

	public ModelAndView createDashboard(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In CreateDashboardController::createDashboard");
		ModelAndView modelAndView = null;
		String view = "";
		view = "redirect:viewStats.do";
		modelAndView = new ModelAndView(view);

		try {
			HttpSession httpsession = request.getSession(false);
			SBMACLUser sbmacluser = (SBMACLUser) httpsession.getAttribute("sbmAclUserObj");
			String dashName = "";
			String dashDescription = "";
			String selMetricsName = "";
			Long dashboardId = new Long(-2L);
			if (request.getParameter("dashName") != null) {
				dashName = request.getParameter("dashName");
			}

			if (request.getParameter("txtDashDescription") != null) {
				dashDescription = request.getParameter("txtDashDescription");
			}

			if (request.getParameter("selectedMetricsName") != null) {
				selMetricsName = request.getParameter("selectedMetricsName");
			}

			if (request.getParameter("hdnDashboardId") != null && !request.getParameter("hdnDashboardId").isEmpty()) {
				dashboardId = Long.parseLong(request.getParameter("hdnDashboardId"));
			}

			Dashboard dash;
			if (!request.getParameter("isDashboardExists").equalsIgnoreCase("true")) {
				this.logger.debug("dashboard not created.. so creating");
				dash = this.createDashBoardUsingParams(dashName, dashDescription, selMetricsName);
				List<String> userList = new ArrayList();
				userList.add(sbmacluser.getName());
				dash.setUsers(userList);
				this.getDashboardService();
				DashboardService.setUserDefaultDashboard(sbmacluser, dash.getId());
			} else {
				this.logger.debug("dashboard already created need editing");
				dash = this.getDashboardService().getDashboardById(dashboardId);
				Dashboard dashNew = this.createDashBoardUsingParams(dashName, dashDescription, selMetricsName);
				List<String> userList = new ArrayList();
				userList.add(sbmacluser.getName());
				if (dashNew != null) {
					dashNew.setUsers(userList);
				}

				if (dash != null && dash.getId() != -1L) {
					this.getDashboardService().deleteDashboard(dash);
				}

				this.getDashboardService();
				DashboardService.setUserDefaultDashboard(sbmacluser, dashNew.getId());
			}
		} catch (Exception var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		}

		this.logger.debug("Exiting CreateDashboardController::createDashboard");
		return modelAndView;
	}

	private Dashboard createDashBoardUsingParams(String dashboardName, String txtDashDescription,
			String selectedDashMetrics) throws CMSException {
		this.logger.debug("In CreateDashboardController::createDashBoardUsingParams");
		Dashboard dashboard = null;

		try {
			List<String> userList = new ArrayList();
			String adminUser = (new JDBCRealm()).getUserNames("DMS_USER", "admin")[0];
			userList.add(adminUser);
			dashboard = new Dashboard();
			dashboard.setName(dashboardName);
			dashboard.setLayoutType(0);
			dashboard.setDescription(txtDashDescription);
			List dashboardWidgetList = new ArrayList();
			List<Widget> widgetList = this.getDashboardService().getAllWidgets();
			Iterator itr = widgetList.iterator();
			byte i = -1;

			while (itr.hasNext()) {
				Widget widget = (Widget) itr.next();
				this.logger.debug("widget :: " + widget.getId() + " :: " + widget.getName());
				if (selectedDashMetrics.contains(widget.getName())) {
					int i = i + 1;
					i = 0;
					this.logger.debug("inside ad widget" + widget.getName());
					DashboardWidget dw = new DashboardWidget();
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
			dashboard.setDashboardWidgets(dashboardWidgetList);
			dashboard.setUsers(userList);
			dashboard.setPublished(true);
			this.logger.debug("Creating dashboard with Name>> " + dashboard.getName());
			dashboard = this.getDashboardService().createDashboard(dashboard);
			this.logger.debug("dashboard created");
		} catch (UnsupportedOperationException var13) {
			throw new CMSException(this.logger, var13);
		} catch (ClassCastException var14) {
			throw new CMSException(this.logger, var14);
		} catch (NullPointerException var15) {
			throw new CMSException(this.logger, var15);
		} catch (IllegalArgumentException var16) {
			throw new CMSException(this.logger, var16);
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}

		this.logger.debug("Exiting CreateDashboardController::createDashBoardUsingParams");
		return dashboard;
	}

	public List<Dashboard> getuserDefaultDashBoard(HttpServletRequest request, HttpServletResponse response)
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

	private Dashboard addWidgetInDashboard(Dashboard dash, String selectedDashMetrics, String txtDashDescription) {
		List<String> userList = new ArrayList();
		String adminUser = (new JDBCRealm()).getUserNames("DMS_USER", "admin")[0];
		userList.add(adminUser);
		List dashboardWidgetList = new ArrayList();
		List<Widget> widgetList = this.getDashboardService().getAllWidgets();
		dash.setDescription(txtDashDescription);
		dash.setLayoutType(0);
		Iterator itr = widgetList.iterator();
		int var9 = 0;

		while (itr.hasNext()) {
			Widget widget = (Widget) itr.next();
			if (selectedDashMetrics.contains(widget.getName())) {
				DashboardWidget dw = new DashboardWidget();
				dw.setWidget(widget);
				dw.setDashboardWidgetColumn(var9++);
				dw.setDashboardWidgetName(widget.getName());
				dw.setDashboardWidgetDesc(widget.getDescription());
				dw.setDashboardWidgetTitle(widget.getTitle());
				dw.setDashboardWidgetRow(0);
				dw.setWidgetId(widget.getId());
				dashboardWidgetList.add(dw);
			}
		}

		this.logger.debug("Size of DashBoardWidgets list is::" + dashboardWidgetList.size());
		dash.setDashboardWidgets(dashboardWidgetList);
		dash.setUsers(userList);
		dash.setPublished(true);
		return dash;
	}
}