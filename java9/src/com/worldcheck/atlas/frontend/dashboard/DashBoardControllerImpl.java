package com.worldcheck.atlas.frontend.dashboard;

import com.savvion.acl.impl.SBMACLUser;
import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.savvion.sbm.bpmportal.service.DashboardService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.dashboard.DashBoardVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class DashBoardControllerImpl extends AbstractController implements IDashBoardController {
	private final String VIEW_DASHBOARD = "ViewDashboard";
	private final String JSP_NAME = "stats_dashboard";
	private final String MY_DASHBOARD = "mydashboards";
	private final String DASHBOARD = "dashboard";
	private final String DISABLE_RESTORE_BUTTON = "disableRestoreButton";
	private final String CUSTOM_DASHBOARD_ID = "customdashboardid";
	private final String IS_REQUEST_FROM_BCP = "isRequestFromBcp";
	private final String SBM_ACL_USER_OBJ = "sbmAclUserObj";
	private final String DASH_EXISTS = "dashExists";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.DashBoardControllerImpl");
	protected DashboardService dashboardService = null;
	protected DashBoardVO userDashBoardVO;

	public DashboardService getDashboardService() {
		return this.dashboardService;
	}

	public void setDashboardService(DashboardService dashboardservice) {
		this.dashboardService = dashboardservice;
	}

	private List<Dashboard> getuserDefaultDashBoard(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("IN DashBoardControllerImpl::getuserDefaultDashBoard");
		new ArrayList();

		List dashList;
		try {
			HttpSession httpsession = request.getSession(false);
			SBMACLUser sbmacluser = (SBMACLUser) httpsession.getAttribute("sbmAclUserObj");
			String userName = null;
			dashList = this.getDashboardService().getUserDefaultDashboard(sbmacluser, true);

			Dashboard var8;
			for (Iterator iterator = dashList.iterator(); iterator.hasNext(); var8 = (Dashboard) iterator.next()) {
				;
			}
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting DashBoardControllerImpl::getuserDefaultDashBoard");
		return dashList;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("In DashBoardControllerImpl::handleRequestInternal");
		ModelAndView modelandview = null;

		try {
			HttpSession httpsession = httpServletRequest.getSession(false);
			SBMACLUser sbmacluser = (SBMACLUser) httpsession.getAttribute("sbmAclUserObj");
			List<Dashboard> dashList = this.getuserDefaultDashBoard(httpServletRequest, httpServletResponse);
			Dashboard dashb = (Dashboard) dashList.get(0);
			Dashboard dash = this.getDashboardService().getDashboardById(dashb.getId());
			if (dash != null && dash.getId() != -1L) {
				this.logger.debug("Dashboard exists " + dash.getId() + " " + dash.getName() + " "
						+ dash.getDashboardWidgets().size());
				List<String> userList = new ArrayList();
				userList.add(sbmacluser.getName());
				dash.setUsers(userList);
				HashMap<String, String> hashmap = new HashMap();
				hashmap.put(String.valueOf(dash.getId()), dash.getName());
				httpsession.setAttribute("ViewDashboard", dash);
				modelandview = new ModelAndView("stats_dashboard");
				modelandview.addObject("mydashboards", hashmap);
				modelandview.addObject("dashboard", dash);
				modelandview.addObject("disableRestoreButton", false);
				modelandview.addObject("customdashboardid", dash.getId());
				modelandview.addObject("isRequestFromBcp", true);
				modelandview.addObject("dashExists", true);
			} else if (dash.getId() == -1L) {
				modelandview = new ModelAndView("stats_dashboard");
			} else {
				this.logger.debug("dashboard neither exists nor created.");
			}

			this.logger.debug("exiting  DashBoardControllerImpl::handleRequestInternal ");
			return modelandview;
		} catch (UnsupportedOperationException var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		} catch (ClassCastException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		} catch (NullPointerException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (IllegalArgumentException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		} catch (CMSException var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		} catch (Exception var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}
	}
}