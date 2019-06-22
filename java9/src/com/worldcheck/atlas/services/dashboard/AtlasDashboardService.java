package com.worldcheck.atlas.services.dashboard;

import com.savvion.acl.impl.SBMACLUser;
import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.savvion.sbm.bpmportal.service.DashboardService;
import com.worldcheck.atlas.bl.dashboard.DashboardManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtlasDashboardService {
	private static final String USER_UPDATED_DASHBOARD = "userUpdatedDashboard";
	private static final String OLD_DASHBOARD_DELETE = "oldDashboardDelete";
	private static final String YES_STR = "yes";
	protected DashboardService dashboardService;
	private DashboardManager dashboardManager;
	private final String SBM_ACL_USER_OBJ = "sbmAclUserObj";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.dashboard.AtlasDashboardService");

	public void setDashboardManager(DashboardManager dashboardManager) {
		this.dashboardManager = dashboardManager;
	}

	public void setDashboardService(DashboardService dashboardservice) {
		this.dashboardService = dashboardservice;
	}

	public DashboardService getDashboardService() {
		return this.dashboardService;
	}

	public void updateUserDashboard(String userId, Map updatedRoleMap, String userIdVal) throws CMSException {
		this.logger.debug("In AtlasDashboardService::updateUserDashboard");
		new ArrayList();
		SBMACLUser sbmacluserObj = new SBMACLUser(Integer.parseInt(userIdVal), userId);
		List<Dashboard> dashList = this.getDashboardService().getUserDefaultDashboard(sbmacluserObj, true);
		this.logger.debug("updateing dashboard for- " + sbmacluserObj.getName());
		if (dashList.size() > 0) {
			this.logger.debug("dashList.get(0).getId() " + ((Dashboard) dashList.get(0)).getId());
		}

		if (dashList.size() > 0 && ((Dashboard) dashList.get(0)).getId() != -1L) {
			Dashboard dash = this.getDashboardService().getDashboardById(((Dashboard) dashList.get(0)).getId());
			new HashMap();
			Map decisionMap = this.dashboardManager.updateUserDashboard(userId, updatedRoleMap, dash, sbmacluserObj);
			Dashboard updatedDash = (Dashboard) decisionMap.get("userUpdatedDashboard");
			if (updatedDash != null) {
				updatedDash = this.getDashboardService().createDashboard(updatedDash);
				this.getDashboardService();
				DashboardService.setUserDefaultDashboard(sbmacluserObj, updatedDash.getId());
				if (dash != null && dash.getId() != -1L) {
					this.getDashboardService().deleteDashboard(dash);
				}
			} else if (updatedDash == null && decisionMap.get("oldDashboardDelete").equals("yes") && dash != null
					&& dash.getId() != -1L) {
				this.getDashboardService().deleteDashboard(dash);
			}
		}

	}
}