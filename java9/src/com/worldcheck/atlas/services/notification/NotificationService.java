package com.worldcheck.atlas.services.notification;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.notification.NotificationManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.notification.NotificationService");
	NotificationManager notificationManager = null;

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	protected NotificationService() {
		this.notificationManager = null;
	}

	public void createUserNotification(String userName, String message, String description, String workstepName,
			String cond, List sendToUserList, String crn) throws CMSException {
		this.notificationManager.createUserNotification(userName, message, description, workstepName, cond,
				sendToUserList, crn);
	}

	public void createUserNotificationWithComments(String userName, String message, String description,
			String workstepName, String cond, List sendToUserList, String crn, String recipient, String sentFrom,
			boolean isBackup) throws CMSException {
		this.notificationManager.createUserNotificationWithComments(userName, message, description, workstepName, cond,
				sendToUserList, crn, recipient, sentFrom, isBackup);
	}

	public void createSystemNotification(String message, String description, List<String> sendToUserList, String crn)
			throws CMSException {
		this.notificationManager.createSystemNotification(message, description, sendToUserList, crn);
	}

	public void createSystemNotification(String message, String description, String sendToUser, String crn)
			throws CMSException {
		this.notificationManager.createSystemNotification(message, description, sendToUser, crn);
	}

	public List<NotificationVO> getUserNotification(String userName, int start, int limit, String sortColumnName,
			String sortType, NotificationVO notificationVO) throws CMSException {
		new ArrayList();
		List<NotificationVO> userNotificationList = this.notificationManager.getUserNotifications(userName, start,
				limit, sortColumnName, sortType, notificationVO);
		return userNotificationList;
	}

	public int getUserNotificationCount(String userName, NotificationVO notificationVO) throws CMSException {
		int count = false;
		this.logger.debug("Notification services getUserNotificationCount");
		int count = this.notificationManager.getUserNotificationsCount(userName, notificationVO);
		this.logger.debug("user Message Count in Notification Services" + count);
		return count;
	}

	public int checkForNotificationCount(String userName) throws CMSException {
		int count = false;
		int count = this.notificationManager.checkForNotificationCount(userName);
		return count;
	}

	public int updateFlag(String loginId) throws CMSException {
		int count = false;
		int count = this.notificationManager.updateFlag(loginId);
		return count;
	}

	public int updateFlagForSystem(String loginId) throws CMSException {
		this.logger.debug("in updateFlagForSystem 1");
		int count = false;
		int count = this.notificationManager.updateFlagForSystem(loginId);
		return count;
	}

	public List<NotificationVO> getNotificationHistory(String userName, String crn, int start, int limit,
			String sortOrder) throws CMSException {
		new ArrayList();
		List<NotificationVO> notificationHistoryList = this.notificationManager.getNotificationHistory(userName, crn,
				start, limit, sortOrder);
		return notificationHistoryList;
	}

	public int getNotificationHistoryCount(String userName, String crn) throws CMSException {
		int count = false;
		int count = this.notificationManager.getNotificationHistoryCount(userName, crn);
		return count;
	}

	public void acknowledgeNotification(String alertNames, String userName) throws Exception {
		this.notificationManager.acknowledgeNotification(alertNames, userName);
	}

	public void replyUserNotification(String userName, NotificationVO notificationVO) throws Exception {
		this.notificationManager.replyNotification(userName, notificationVO);
	}

	public void forwardUserNotification(String userName, NotificationVO notificationVO) throws Exception {
		this.notificationManager.replyNotification(userName, notificationVO);
	}

	public List<NotificationVO> getSystemNotification(String userName, int start, int limit, String sortColumnName,
			String sortType, NotificationVO notificationVO) throws CMSException {
		new ArrayList();
		List<NotificationVO> systemNotificationList = this.notificationManager.getSystemNotification(userName, start,
				limit, sortColumnName, sortType, notificationVO);
		return systemNotificationList;
	}

	public int getSystemNotificationCount(String userName, NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Notification services getSystemNotificationCount");
		int count = false;
		int count = this.notificationManager.getSystemNotificationCount(userName, notificationVO);
		return count;
	}

	public List<NotificationVO> getMessageTrail(String alertId, String userName) throws CMSException {
		new ArrayList();
		List<NotificationVO> systemNotificationList = this.notificationManager.getMessageTrail(alertId, userName);
		return systemNotificationList;
	}

	public List<UserMasterVO> getAssignedUsers(String crn, String userId) throws CMSException {
		List<UserMasterVO> userVoList = this.notificationManager.getAssignedUsers(crn, userId);
		return userVoList;
	}

	public List<NotificationVO> getRecCaseSystemNotifications(String userName, int start, int limit,
			String sortColumnName, String sortType, NotificationVO notificationVO) throws CMSException {
		new ArrayList();
		List<NotificationVO> systemNotificationList = this.notificationManager.getRecCaseSystemNotifications(userName,
				start, limit, sortColumnName, sortType, notificationVO);
		return systemNotificationList;
	}

	public int getRecCaseSystemNotificationCount(String userName, NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Notification services getRecCaseSystemNotificationCount");
		int count = false;
		int count = this.notificationManager.getRecCaseSystemNotificationCount(userName, notificationVO);
		return count;
	}

	public void createRecCase(String recClientCaseId, String recCaseSchedulerId, String userName, Session session)
			throws CMSException {
		this.notificationManager.createRecCase(recClientCaseId, recCaseSchedulerId, userName, session);
	}

	public int getUserNotificationCountWithoutFilter(String userName) throws CMSException {
		int count = false;
		this.logger.debug("Notification services getUserNotificationCount");
		int count = this.notificationManager.getUserNotificationsCountWithoutFilter(userName);
		this.logger.debug("user Message Count in Notification Services" + count);
		return count;
	}

	public int getSystemNotificationCountWithoutFilter(String userName) throws CMSException {
		this.logger.debug("Notification services getSystemNotificationCount");
		int count = false;
		int count = this.notificationManager.getSystemNotificationCountWithoutFilter(userName);
		return count;
	}

	public int getRecCaseSystemNotificationCountWithoutFilter(String userName) throws CMSException {
		this.logger.debug("Notification services getRecCaseSystemNotificationCount");
		int count = false;
		int count = this.notificationManager.getRecCaseSystemNotificationCountWithoutFilter(userName);
		return count;
	}
}