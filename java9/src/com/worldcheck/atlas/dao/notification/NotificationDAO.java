package com.worldcheck.atlas.dao.notification;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class NotificationDAO extends SqlMapClientTemplate {
	private static final String NOTIFICATION_GET_TRAIL_SENT_TO_LIST = "Notification.getTrailSentToList";
	private static final String PARENT_ALERT_IDS = "parentAlertIds";
	private static final String ALERT_ID = "alertId";
	private static final String CASE = "CASE";
	private static final String SUBTEAM = "SUBTEAM";
	private static final String TEAM = "TEAM";
	private static final String NOTIFICATION_GETUNIQUENUMBER = "Notification.getUniqueNumber";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONS = "Notification.getUserNotifications";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONSCOUNT = "Notification.getUserNotificationsCount";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONS_FILTER = "Notification.getUserNotificationsFilterBase";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONSCOUNT_FILTER = "Notification.getUserNotificationsCountFilterBase";
	private static final String CHECK_NOTIFICATION_COUNT = "Notification.checkNotificationCount";
	private static final String CHECK_SYSTEM_NOTIFICATION_COUNT = "Notification.checkSystemNotificationCount";
	private static final String UPDATE_NOTIFICATION_FLAG = "Notification.updateNotificationFlag";
	private static final String UPDATE_NOTIFICATION_FLAG_FOR_SYSTEM = "Notification.updateNotificationFlagForSystem";
	private static final String GET_ALL_SYSTEM_ALERT = "Notification.getSystemAlerts";
	private static final String GET_ALL_USER_ALERT = "Notification.getUserAlerts";
	private static final String NOTIFICATION_INSERTCRNMAPDATA = "Notification.insertCRNMapData";
	private static final String NOTIFICATION_GETNOTIFICATIONHISTORY = "Notification.getNotificationHistory";
	private static final String NOTIFICATION_UPDATEACKNOWLEDGEDATE = "Notification.updateAcknowledgeDate";
	private static final String NOTIFICATION_SUBTEAMASSIGNEDUSER = "Notification.getAssignedUsersSubTeam";
	private static final String NOTIFICATION_TEAMASSIGNEDUSER = "Notification.getAssignedUsersTeam";
	private static final String NOTIFICATION_CASEASSIGNEDUSER = "Notification.getAssignedUsersCase";
	private static final String NOTIFICATION_INSERTCRNMAPREPLYDATA = "Notification.insertCRNMapReplyData";
	private static final String NOTIFICATION_GETSYSTEMNOTIFICATIONS = "Notification.getSystemNotifications";
	private static final String NOTIFICATION_GETSYSTEMNOTIFICATIONSCOUNT = "Notification.getSystemNotificationsCount";
	private static final String NOTIFICATION_CALLMESSAGETRAIL = "Notification.callMessageTrail";
	private static final String NOTIFICATION_GETMESSAGETRAIL = "Notification.getMessageTrail";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONSDETAILS = "Notification.getUserNotificationDetails";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONHISTORYDETAILS = "Notification.getNotificationHistoryDetails";
	private static final String NOTIFICATION_GETNOTIFICATIONHISTORYCOUNT = "Notification.getNotificationHistoryCount";
	private static final String NOTIFICATION_GETNOTIFICATIONSENTTOLIST = "Notification.getNotificationSentToList";
	private static final String NOTIFICATION_GETRECNOTIFICATIONS = "Notification.getRecNotifications";
	private static final String NOTIFICATION_GETRECNOTIFICATIONSCOUNT = "Notification.getRecNotificationsCount";
	private static final String NOTIFICATION_MAX_ALERT_ID = "Notification.getMaxAlertID";
	private static final String NOTIFICATION_GETSYSTEMNOTIFICATIONS_FILTER = "Notification.getSystemNotificationsFilterBase";
	private static final String NOTIFICATION_GETSYSTEMNOTIFICATIONSCOUNT_FILTER = "Notification.getSystemNotificationsCountFilterBase";
	private static final String NOTIFICATION_GETUSERNOTIFICATIONSCOUNT_FILTER_WITHOUT_FILTER = "Notification.getUserNotificationsCountWithOutFilter";
	private static final String NOTIFICATION_GETRECNOTIFICATIONSCOUNT_WITHOUT_FILTER = "Notification.getRecCaseSystemNotificationCountWithoutFilter";
	private static final String NOTIFICATION_GETSYSTEMNOTIFICATIONSCOUNT_WITHOUT_FILTER = "Notification.getSystemNotificationsCountWithoutFilter";
	private static final String NOTIFICATION_GETRECURRENCENOTIFICATION_FILTER = "Notification.getRecurrenceNotificationsFilter";
	private static final String NOTIFICATION_GETRECURRENCENOTIFICATIONCOUNT_FILTER = "Notification.getRecurrenceNotificationsCountFilter";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.notification.NotificationDAO");

	public HashMap<String, List<NotificationVO>> getAssignedUsers(String crn) throws CMSException {
		new ArrayList();
		new ArrayList();
		new ArrayList();
		HashMap dataMap = new HashMap();

		try {
			List<NotificationVO> userCaseList = this.queryForList("Notification.getAssignedUsersCase", crn);
			List<NotificationVO> userSubTeamList = this.queryForList("Notification.getAssignedUsersSubTeam", crn);
			List<NotificationVO> userTeamList = this.queryForList("Notification.getAssignedUsersTeam", crn);
			dataMap.put("CASE", userCaseList);
			dataMap.put("SUBTEAM", userSubTeamList);
			dataMap.put("TEAM", userTeamList);
			return dataMap;
		} catch (DataAccessException var8) {
			CMSException cmsException = new CMSException(this.logger, var8);
			throw cmsException;
		}
	}

	public int getUniqueNumber() throws CMSException {
		boolean var1 = false;

		try {
			int id = (Integer) this.queryForObject("Notification.getUniqueNumber");
			return id;
		} catch (DataAccessException var4) {
			CMSException cmsException = new CMSException(this.logger, var4);
			throw cmsException;
		}
	}

	public List<NotificationVO> getUserNotifications(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			List<NotificationVO> userNotificationList = this.queryForList("Notification.getUserNotifications",
					notificationVO);
			return userNotificationList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getUserNotificationDetails(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			List<NotificationVO> userNotificationDetailsList = this
					.queryForList("Notification.getUserNotificationDetails", notificationVO);
			return userNotificationDetailsList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getUserNotificationsCount(NotificationVO notificationVO) throws CMSException {
		int count = false;
		new ArrayList();

		try {
			List<NotificationVO> dataList = this.queryForList("Notification.getUserNotificationsCount", notificationVO);
			int count = dataList.size();
			return count;
		} catch (DataAccessException var6) {
			CMSException cmsException = new CMSException(this.logger, var6);
			throw cmsException;
		}
	}

	public List<NotificationVO> getUserNotificationsFilterBase(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Entirng NotificationDAO:getUserNotificationsFilterBase");
		new ArrayList();

		try {
			List<NotificationVO> userNotificationList = this.queryForList("Notification.getUserNotificationsFilterBase",
					notificationVO);
			return userNotificationList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getUserNotificationDetailsFilterBase(NotificationVO notificationVO)
			throws CMSException {
		this.logger.debug("Entirng getUserNotificationDetailsFilterBase");
		new ArrayList();

		try {
			List<NotificationVO> userNotificationDetailsList = this
					.queryForList("Notification.getUserNotificationDetails", notificationVO);
			return userNotificationDetailsList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getUserNotificationsCountFilterBase(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Entirng getUserNotificationsCountFilterBase");
		int count = false;
		new ArrayList();

		try {
			List<NotificationVO> dataList = this.queryForList("Notification.getUserNotificationsCountFilterBase",
					notificationVO);
			int count = dataList.size();
			return count;
		} catch (DataAccessException var6) {
			CMSException cmsException = new CMSException(this.logger, var6);
			throw cmsException;
		}
	}

	public int checkForNotificationCount(NotificationVO notificationVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("Notification.checkNotificationCount", notificationVO);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int checkForSystemNotificationCount(NotificationVO notificationVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("Notification.checkSystemNotificationCount", notificationVO);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int updateFlag(NotificationVO notificationVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = Integer.valueOf(this.update("Notification.updateNotificationFlag", notificationVO));
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int updateFlagForSystem(NotificationVO notificationVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = Integer.valueOf(this.update("Notification.updateNotificationFlagForSystem", notificationVO));
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getSystemAlerts(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("in updateFlagForSystem 3");
		new ArrayList();

		try {
			List<NotificationVO> alertNames = this.queryForList("Notification.getSystemAlerts", notificationVO);
			return alertNames;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getUserAlerts(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			List<NotificationVO> alertNames = this.queryForList("Notification.getUserAlerts", notificationVO);
			return alertNames;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public boolean insertCRNNotificationData(NotificationVO notificationVO) throws CMSException {
		boolean flag = false;

		try {
			this.insert("Notification.insertCRNMapData", notificationVO);
			flag = true;
			return flag;
		} catch (Exception var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getNotificationHistory(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			List<NotificationVO> notificationHistoryList = this.queryForList("Notification.getNotificationHistory",
					notificationVO);
			return notificationHistoryList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getNotificationHistoryDetails(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			List<NotificationVO> userNotificationDetailsList = this
					.queryForList("Notification.getNotificationHistoryDetails", notificationVO);
			return userNotificationDetailsList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int acknowledgeNotification(NotificationVO notificationVO) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("recp name os " + notificationVO.getRecipient());
			int count = this.update("Notification.updateAcknowledgeDate", notificationVO);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public void insertCRNNotificationReplyData(NotificationVO notificationVO) throws CMSException {
		try {
			this.insert("Notification.insertCRNMapReplyData", notificationVO);
		} catch (Exception var4) {
			CMSException cmsException = new CMSException(this.logger, var4);
			throw cmsException;
		}
	}

	public List<NotificationVO> getSystemNotifications(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			this.logger.debug("max limit " + notificationVO.getMaxRecord());
			this.logger.debug("max limit " + notificationVO.getMinRecord());
			List<NotificationVO> systemNotificationList = this.queryForList("Notification.getSystemNotifications",
					notificationVO);
			this.logger.debug("system notifications " + systemNotificationList.size());
			return systemNotificationList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getMessageTrail(String alertId, String userName, NotificationVO notificationVO2)
			throws CMSException {
		HashMap<String, Object> oMapValue = new HashMap();
		oMapValue.put("alertId", Integer.parseInt(alertId));
		oMapValue.put("parentAlertIds", "");
		this.queryForList("Notification.callMessageTrail", oMapValue);
		String parentAlertIds = (String) oMapValue.get("parentAlertIds");
		this.logger.debug("parent alert ids" + parentAlertIds);
		new ArrayList();
		parentAlertIds = parentAlertIds.replace("'", " ");
		NotificationVO notificationVO = new NotificationVO();
		notificationVO.setMessage(parentAlertIds);
		this.logger.debug("after replace" + parentAlertIds);
		notificationVO.setUserName(userName);
		List<NotificationVO> messageTrailList = this.queryForList("Notification.getMessageTrail", notificationVO);
		this.logger.debug("list is " + messageTrailList.size());
		notificationVO2.setMessage(parentAlertIds);
		new ArrayList();
		this.logger.debug("number of alerts " + messageTrailList.size());
		List<NotificationVO> userNotificationDetailsList = this.queryForList("Notification.getTrailSentToList",
				notificationVO2);
		this.logger.debug("details list size " + userNotificationDetailsList.size());
		Iterator var10 = messageTrailList.iterator();

		while (var10.hasNext()) {
			NotificationVO userNotificationVO = (NotificationVO) var10.next();
			Iterator var12 = userNotificationDetailsList.iterator();

			while (var12.hasNext()) {
				NotificationVO userNotificationDetailVO = (NotificationVO) var12.next();
				if (userNotificationDetailVO.getName().equalsIgnoreCase(userNotificationVO.getName())) {
					String recipient = userNotificationVO.getRecipientFullName();
					String ackDate = userNotificationVO.getAckDate();
					String sender = userNotificationVO.getSenderFullName();
					if (recipient == null) {
						if (userNotificationDetailVO.getRecipientFullName() != null
								&& !userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
							recipient = userNotificationDetailVO.getRecipientFullName();
						}
					} else if (!userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
						recipient = recipient + "," + userNotificationDetailVO.getRecipientFullName();
					}

					if (ackDate == null) {
						if (userNotificationDetailVO.getAckDate() == null) {
							if (userNotificationDetailVO.getRecipientFullName() != null
									&& !userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
								ackDate = ackDate + ",";
							}
						} else if (!userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
							ackDate = userNotificationDetailVO.getAckDate();
						}
					} else if (userNotificationDetailVO.getAckDate() == null) {
						if (userNotificationDetailVO.getRecipientFullName() != null
								&& !userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
							ackDate = ackDate + ",";
						}
					} else if (!userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
						ackDate = ackDate + "," + userNotificationDetailVO.getAckDate();
					}

					userNotificationVO.setRecipientFullName(recipient);
					userNotificationVO.setAckDate(ackDate);
				}
			}
		}

		return messageTrailList;
	}

	public int getSystemNotificationsCount(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("NotificationDAO : getSystemNotificationsCount");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("Notification.getSystemNotificationsCount", notificationVO);
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}

		this.logger.debug("NotificationDAO : getSystemNotificationsCount" + count);
		return count;
	}

	public int getNotificationHistoryCount(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		List userNotificationHistoryList;
		try {
			userNotificationHistoryList = this.queryForList("Notification.getNotificationHistoryCount", notificationVO);
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}

		return userNotificationHistoryList.size();
	}

	public List<UserMasterVO> getSentToList(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> userList = this.queryForList("Notification.getNotificationSentToList", notificationVO);
			return userList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<NotificationVO> getRecCaseSystemNotifications(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			this.logger.debug("max limit " + notificationVO.getMaxRecord());
			this.logger.debug("max limit " + notificationVO.getMinRecord());
			List<NotificationVO> systemNotificationList = this.queryForList("Notification.getRecNotifications",
					notificationVO);
			this.logger.debug("system notifications " + systemNotificationList.size());
			return systemNotificationList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getRecCaseSystemNotificationCount(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("NotificationDAO : getRecCaseSystemNotificationCount");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("Notification.getRecNotificationsCount", notificationVO);
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}

		this.logger.debug("NotificationDAO : getRecCaseSystemNotificationCount" + count);
		return count;
	}

	public long getMaxAlertID(String userName, String alertName) throws Exception {
		long rAlertId = 0L;
		NotificationVO notificationVO = new NotificationVO();
		notificationVO.setRecipient(userName);
		notificationVO.setName(alertName);

		try {
			rAlertId = (Long) this.queryForObject("Notification.getMaxAlertID", notificationVO);
			return rAlertId;
		} catch (Exception var7) {
			this.logger.error("Error in fetching max alert id " + var7.getMessage());
			throw var7;
		}
	}

	public List<NotificationVO> getSystemNotificationsFilterBase(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Entirng NotificationDAO:getSystemNotificationsFilterBase");
		new ArrayList();

		try {
			List<NotificationVO> systemNotificationList = this
					.queryForList("Notification.getSystemNotificationsFilterBase", notificationVO);
			return systemNotificationList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getSystemNotificationsCountFilterBase(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Entirng getSystemNotificationsCountFilterBase");
		int count = false;
		new ArrayList();

		try {
			List<NotificationVO> dataList = this.queryForList("Notification.getSystemNotificationsCountFilterBase",
					notificationVO);
			int count = dataList.size();
			return count;
		} catch (DataAccessException var6) {
			CMSException cmsException = new CMSException(this.logger, var6);
			throw cmsException;
		}
	}

	public List<NotificationVO> getRecCaseSystemNotificationsFilter(NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			this.logger.debug("max limit " + notificationVO.getMaxRecord());
			this.logger.debug("max limit " + notificationVO.getMinRecord());
			List<NotificationVO> systemNotificationList = this
					.queryForList("Notification.getRecurrenceNotificationsFilter", notificationVO);
			this.logger.debug("system notifications " + systemNotificationList.size());
			return systemNotificationList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getRecCaseSystemNotificationCountFilter(NotificationVO notificationVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("Notification.getRecurrenceNotificationsCountFilter",
					notificationVO);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getUserNotificationsCountWithOutFilter(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("NotificationDAO : getUserNotificationsCount");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("Notification.getUserNotificationsCountWithOutFilter",
					notificationVO);
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}

		this.logger.debug("NotificationDAO : getUserNotificationsCount" + count);
		return count;
	}

	public int getSystemNotificationsCountWithoutFilter(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("NotificationDAO : getSystemNotificationsCount");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("Notification.getSystemNotificationsCountWithoutFilter",
					notificationVO);
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}

		this.logger.debug("NotificationDAO : getSystemNotificationsCount" + count);
		return count;
	}

	public int getRecCaseSystemNotificationCountWithoutFilter(NotificationVO notificationVO) throws CMSException {
		this.logger.debug("NotificationDAO : getRecCaseSystemNotificationCount");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("Notification.getRecCaseSystemNotificationCountWithoutFilter",
					notificationVO);
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}

		this.logger.debug("NotificationDAO : getRecCaseSystemNotificationCount" + count);
		return count;
	}
}