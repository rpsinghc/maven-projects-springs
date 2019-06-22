package com.worldcheck.atlas.vo.dashboard;

import java.util.List;

public class DashBoardVO {
	List dashBoard = null;
	long dashBoardID = 0L;
	String dashboardMetrics;
	String dashboardMetricsFullName;

	public List getDashBoard() {
		return this.dashBoard;
	}

	public void setDashBoard(List dashBoard) {
		this.dashBoard = dashBoard;
	}

	public long getDashBoardID() {
		return this.dashBoardID;
	}

	public void setDashBoardID(long dashBoardID) {
		this.dashBoardID = dashBoardID;
	}

	public String getDashboardMetrics() {
		return this.dashboardMetrics;
	}

	public void setDashboardMetrics(String dashboardMetrics) {
		this.dashboardMetrics = dashboardMetrics;
	}

	public String getDashboardMetricsFullName() {
		return this.dashboardMetricsFullName;
	}

	public void setDashboardMetricsFullName(String dashboardMetricsFullName) {
		this.dashboardMetricsFullName = dashboardMetricsFullName;
	}
}