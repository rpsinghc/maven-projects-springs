package com.worldcheck.atlas.bl.notification;

import com.savvion.sbm.alerts.svo.RuntimeAlert;
import java.util.List;

public interface INotificationController {
	void createAlerts(String var1, String var2, String var3, String var4, String var5);

	void createProcessAlerts(String var1, String var2, String var3);

	void publishProcessAlerts(String var1);

	void removeAlerts();

	void cleanBLAlertService();

	void unsubscribeAlerts(String var1, String var2);

	void subscribeAlerts(String var1, String var2);

	RuntimeAlert getRuntimeAlert(String var1);

	void forwardRuntimeAlert(String var1, String var2, String var3, String var4);

	void replyRuntimeAlert(String var1, String var2, String var3, String var4);

	void removeRuntimeAlerts(List var1);

	void cleanPortalAlertService();
}