package com.worldcheck.atlas.bl.notification;

import com.savvion.sbm.alerts.AlertServiceFactory;
import com.savvion.sbm.alerts.intf.AlertService;
import com.savvion.sbm.alerts.svo.Alert;
import com.savvion.sbm.alerts.svo.AlertDelivery;
import com.savvion.sbm.alerts.svo.ProcessAlert;
import com.savvion.sbm.alerts.svo.RuntimeAlert;
import com.savvion.sbm.alerts.util.AlertConstants.CacheType;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class ImplNotificationDeliveryManager implements INotificationController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.notification.ImplNotificationDeliveryManager");
	private AlertService bas = null;
	private AlertService pas = null;
	private String templateName;

	public ImplNotificationDeliveryManager(String appName, String ds) {
		this.templateName = appName;
		Properties properties = new Properties();
		properties.put("CACHE", CacheType.SYNCHRONIZED);
		properties.put("MAX_TEMPLATE_ALERTS", 50);
		properties.put("ALERT_DATASOURCE", ds);
		this.bas = AlertServiceFactory.getInstance("BizLogic", properties);
		Properties properties1 = new Properties();
		this.pas = AlertServiceFactory.getInstance("Portal", properties1);
	}

	public void createAlerts(String userName, String alertName, String message, String condition, String description) {
		HashMap variableMap = new HashMap();
		variableMap.put("creator", "STRING");
		variableMap.put("manager", "STRING");
		variableMap.put("days", "LONG");
		AlertDelivery alertdelivery = new AlertDelivery(userName, "USER", "SBM");
		ArrayList alertDeliveryList = new ArrayList();
		alertDeliveryList.add(alertdelivery);
		Alert alert = new Alert(alertName, this.templateName, "CRITICAL", message, (String) null, description,
				alertDeliveryList, variableMap);
		ArrayList arraylist4 = new ArrayList();
		arraylist4.add(alert);
		this.bas.createAlerts(this.templateName, arraylist4);
	}

	public void createProcessAlerts(String alertName, String workstepName, String condition) {
		ProcessAlert processalert = new ProcessAlert(alertName, this.templateName, workstepName, condition, -1L);
		ArrayList arraylist = new ArrayList();
		arraylist.add(processalert);
		this.bas.createProcessAlerts(this.templateName, arraylist);
	}

	public void publishProcessAlerts(String alertName) {
		HashMap hashmap = new HashMap();
		hashmap.put("startdate", new Date(2006, 7, 2));
		hashmap.put("enddate", new Date(2006, 7, 27));
		hashmap.put("days", new Long(25L));
		ArrayList arraylist = new ArrayList();
		arraylist.add(alertName);
		HashMap hashmap1 = new HashMap();
		Set set = this.bas.getVariables(this.templateName, arraylist);
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			hashmap1.put(s, hashmap.get(s));
		}

		hashmap1.put("PROCESS_TEMPLATE_NAME", this.templateName);
		hashmap1.put("PROCESS_INSTANCE_ID", new Long(124L));
		hashmap1.put("WORKSTEP_ID", new Long(1L));
		this.bas.publishProcessAlerts(this.templateName, arraylist, hashmap1);
	}

	public void removeAlerts() {
		this.bas.removeAlerts(this.templateName);
	}

	public void cleanBLAlertService() {
		AlertServiceFactory.removeInstance("BizLogic");
		this.bas = null;
	}

	public void unsubscribeAlerts(String AppName, String userName) {
		ArrayList arraylist = new ArrayList();
		arraylist.add(AppName);
		this.pas.unsubscribeAlerts(userName, arraylist);
	}

	public void subscribeAlerts(String AppName, String userName) {
		ArrayList arraylist = new ArrayList();
		arraylist.add(AppName);
		this.pas.subscribeAlerts(userName, arraylist);
	}

	public List<Alert> getAlert(String title) {
		List<Alert> alertList = this.bas.getAlerts("AtlasNotificationProcess");
		System.out.println("erros " + alertList.size());
		return alertList;
	}

	public RuntimeAlert getRuntimeAlert(String id) {
		long alertId = Long.parseLong(id);
		RuntimeAlert runtimealert = this.pas.getRuntimeAlert(alertId);
		return runtimealert;
	}

	public void forwardRuntimeAlerts(List list) {
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			RuntimeAlert runtimealert = (RuntimeAlert) iterator.next();
			runtimealert.setName("FW: " + runtimealert.getName());
			AlertDelivery alertdelivery = new AlertDelivery("hkcagent", "USER", "SBM");
			AlertDelivery alertdelivery1 = new AlertDelivery("rxcmnagent", "USER", "SBM");
			ArrayList arraylist = new ArrayList();
			arraylist.add(alertdelivery);
			arraylist.add(alertdelivery1);
			this.pas.forwardAlert(runtimealert, arraylist);
		}

	}

	public void forwardRuntimeAlert(String alertId, String userName, String message, String name) {
		RuntimeAlert runtimealert = this.pas.getRuntimeAlert(Long.parseLong(alertId));
		runtimealert.setName(name);
		runtimealert.setMessage(alertId + "_" + message);
		AlertDelivery alertdelivery = new AlertDelivery(userName, "USER", "SBM");
		ArrayList arraylist = new ArrayList();
		arraylist.add(alertdelivery);
		this.pas.forwardAlert(runtimealert, arraylist);
	}

	public void sendRuntimeAlert(String alertId, String userName, String message) {
		RuntimeAlert runtimealert = this.pas.getRuntimeAlert(Long.parseLong(alertId));
		runtimealert.setName(runtimealert.getName());
		runtimealert.setMessage(alertId + "_" + message);
		AlertDelivery alertdelivery = new AlertDelivery(userName, "USER", "SBM");
		ArrayList arraylist = new ArrayList();
		arraylist.add(alertdelivery);
		this.pas.forwardAlert(runtimealert, arraylist);
	}

	public void replyRuntimeAlert(String alertId, String userName, String message, String name) {
		RuntimeAlert runtimealert = this.pas.getRuntimeAlert(Long.parseLong(alertId));
		runtimealert.setName(name);
		runtimealert.setMessage(alertId + "_" + message);
		AlertDelivery alertdelivery = new AlertDelivery(userName, "USER", "SBM");
		ArrayList arraylist = new ArrayList();
		arraylist.add(alertdelivery);
		this.pas.forwardAlert(runtimealert, arraylist);
	}

	public void removeRuntimeAlerts(List list) {
		ArrayList arraylist = new ArrayList();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			RuntimeAlert runtimealert = (RuntimeAlert) iterator.next();
			arraylist.add(new Long(runtimealert.getRalertId()));
		}

		this.pas.removeRuntimeAlerts(arraylist);
	}

	public void cleanPortalAlertService() {
		AlertServiceFactory.removeInstance("Portal");
		this.pas = null;
	}

	public void createRuntimeAlert(long alertId, String userName) {
		RuntimeAlert runtimealert = this.pas.getRuntimeAlert(alertId);
		AlertDelivery alertdelivery = new AlertDelivery(userName, "USER", "SBM");
		ArrayList arraylist = new ArrayList();
		arraylist.add(alertdelivery);
		this.pas.forwardAlert(runtimealert, arraylist);
	}
}