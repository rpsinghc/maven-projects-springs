package com.worldcheck.atlas.frontend.dashboard;

import com.savvion.sbm.bpmportal.service.DashboardService;

public interface IDashBoardController {
	DashboardService getDashboardService();

	void setDashboardService(DashboardService var1);
}