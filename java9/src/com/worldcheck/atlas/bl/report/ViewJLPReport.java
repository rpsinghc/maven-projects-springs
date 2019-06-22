package com.worldcheck.atlas.bl.report;

import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.savvion.sbm.bpmportal.domain.DashboardWidget;
import com.savvion.sbm.bpmportal.domain.Widget;
import com.savvion.sbm.bpmportal.service.DashboardService;
import com.tdiinc.userManager.JDBCRealm;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewJLPReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.ViewJLPReport");
	private DashboardService dashboardService = null;
	private String DASHBOARD_NAME = "Office Utilization";
	private String WIDGET_NAME_1 = "Current_Month_JLP_Data";
	private String WIDGET_NAME_2 = "Next_Month_JLP_Data";
	private String ADMIN = "admin";

	public DashboardService getDashboardService() {
		return this.dashboardService;
	}

	public void setDashboardService(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	public Dashboard getDashBoard() throws CMSException {
		this.logger.debug("in getDashBoard :: " + this.getDashboardService().getDashboardByName(this.DASHBOARD_NAME));
		Dashboard dash = this.getDashboardService().getDashboardByName(this.DASHBOARD_NAME);
		if (null == dash) {
			this.logger.debug("Office Utilization is not present so creating");
			dash = this.createDashboard();
		} else {
			this.logger.debug("Office Utilization exists so not creating");
		}

		return dash;
	}

	private Dashboard createDashboard() throws CMSException {
		this.logger.debug("in createDashboard");
		Dashboard dashboard = null;

		try {
			List<String> userList = new ArrayList();
			String adminUser = (new JDBCRealm()).getUserNames("DMS_USER", this.ADMIN)[0];
			userList.add(adminUser);
			dashboard = new Dashboard();
			dashboard.setName(this.DASHBOARD_NAME);
			dashboard.setLayoutType(0);
			List dashboardWidgetList = new ArrayList();
			List<Widget> widgetList = this.getDashboardService().getAllWidgets();
			this.logger.debug("Size of widgets list::::" + widgetList.size());
			Iterator itr = widgetList.iterator();
			boolean var7 = false;

			while (true) {
				Widget widget;
				do {
					if (!itr.hasNext()) {
						this.logger.debug("Size of DashBoardWidgets list is::" + dashboardWidgetList.size());
						dashboard.setDashboardWidgets(dashboardWidgetList);
						dashboard.setUsers(userList);
						dashboard.setPublished(true);
						this.logger.debug("Creating dashboard >> " + dashboard);
						dashboard = this.getDashboardService().createDashboard(dashboard);
						this.logger.debug("dashboard created");
						return dashboard;
					}

					widget = (Widget) itr.next();
					this.logger.debug("widget :: " + widget.getId() + " :: " + widget.getName());
				} while (!widget.getName().equalsIgnoreCase(this.WIDGET_NAME_1)
						&& !widget.getName().equalsIgnoreCase(this.WIDGET_NAME_2));

				DashboardWidget dw = new DashboardWidget();
				dw.setWidget(widget);
				dw.setDashboardWidgetColumn(0);
				dw.setDashboardWidgetName(widget.getName());
				dw.setDashboardWidgetDesc(widget.getDescription());
				dw.setDashboardWidgetTitle(widget.getTitle());
				if (widget.getName().equalsIgnoreCase(this.WIDGET_NAME_1)) {
					dw.setDashboardWidgetRow(0);
				}

				if (widget.getName().equalsIgnoreCase(this.WIDGET_NAME_2)) {
					dw.setDashboardWidgetRow(1);
				}

				dw.setWidgetId(widget.getId());
				dashboardWidgetList.add(dw);
			}
		} catch (UnsupportedOperationException var10) {
			throw new CMSException(this.logger, var10);
		} catch (ClassCastException var11) {
			throw new CMSException(this.logger, var11);
		} catch (NullPointerException var12) {
			throw new CMSException(this.logger, var12);
		} catch (IllegalArgumentException var13) {
			throw new CMSException(this.logger, var13);
		}
	}
}