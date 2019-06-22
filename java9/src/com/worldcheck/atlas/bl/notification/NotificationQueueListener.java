package com.worldcheck.atlas.bl.notification;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.worldcheck.atlas.dao.notification.NotificationDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class NotificationQueueListener implements MessageListener, MessageDrivenBean {
	private static final String NOTIFICATION_INSERT_CRN_MAP_DATA = "Notification.insertCRNMapData";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.notification.NotificationQueueListener");
	private NotificationDAO notificationDAO = null;
	private static final String ATLAS = "ATLAS";
	private static SqlMapClient sqlMap = null;
	private static Reader reader;

	public void onMessage(Message message) {
		try {
			if (null != message) {
				reader = Resources.getResourceAsReader("SqlMapConfig1.xml");
				sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
				this.notificationDAO = new NotificationDAO();
				ObjectMessage objMessage = (ObjectMessage) message;
				this.logger.debug("inside OnMessage method");
				NotificationVO notificationVO = (NotificationVO) objMessage.getObject();
				this.logger.debug("After getting HashMap Object from message");
				this.processMessage(notificationVO);
				this.logger.debug("Servlet executed successfully...");
			}
		} catch (Exception var4) {
			this.logger.error("Error occured in servlet calling .. Exception is " + var4);
		}

	}

	public void ejbRemove() throws EJBException {
	}

	public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException {
	}

	private void processMessage(NotificationVO notificationVO) {
		try {
			new ArrayList();
			List<String> sendToUserList = notificationVO.getSendToUserList();
			String alertName = this.getUniqueAlertName();
			String userName = notificationVO.getSender();
			this.logger.debug(" alert name generated " + alertName);
			ImplNotificationDeliveryManager notificationDeliveryManager = new ImplNotificationDeliveryManager("Review",
					notificationVO.getDsName());
			notificationDeliveryManager.createAlerts(userName, alertName, notificationVO.getMessage(), (String) null,
					notificationVO.getDescription());
			notificationDeliveryManager.createProcessAlerts(alertName, "temp", "temp");
			notificationDeliveryManager.publishProcessAlerts(alertName);
			long alertId = 0L;
			alertId = this.getMaxAlertID(userName, alertName);
			this.logger.debug("alert Id " + alertId);
			this.logger.debug("send to user list size " + sendToUserList.size());

			for (int i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					notificationDeliveryManager.createRuntimeAlert(alertId, (String) sendToUserList.get(i));
				}
			}

			NotificationVO notificationVOTest = new NotificationVO();
			notificationVOTest.setCrn(notificationVO.getCrn());
			notificationVOTest.setName(alertName);
			notificationVOTest.setRecipient(userName);
			notificationVOTest.setParentAlertId(-1);
			sqlMap.insert("Notification.insertCRNMapData", notificationVOTest);

			for (int i = 0; i < sendToUserList.size(); ++i) {
				notificationVOTest = new NotificationVO();
				notificationVOTest.setCrn(notificationVO.getCrn());
				notificationVOTest.setName(alertName);
				notificationVOTest.setRecipient((String) sendToUserList.get(i));
				notificationVOTest.setParentAlertId(-1);
				sqlMap.insert("Notification.insertCRNMapData", notificationVOTest);
			}
		} catch (CMSException var10) {
			var10.printStackTrace();
		} catch (SQLException var11) {
			var11.printStackTrace();
		}

	}

	private String getUniqueAlertName() throws CMSException {
		String uniqueAlertName = "";

		try {
			int number = this.notificationDAO.getUniqueNumber();
			uniqueAlertName = "ATLAS" + number;
			return uniqueAlertName;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	private long getMaxAlertID(String userName, String alertName) throws CMSException {
		long rAlertId = 0L;

		try {
			rAlertId = this.notificationDAO.getMaxAlertID(userName, alertName);
			return rAlertId;
		} catch (Exception var6) {
			this.logger.error("Error in fetching max alert id " + var6.getMessage());
			throw new CMSException(this.logger, var6);
		}
	}
}