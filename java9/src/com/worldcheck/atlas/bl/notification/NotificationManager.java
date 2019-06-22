package com.worldcheck.atlas.bl.notification;

import com.savvion.sbm.alerts.svo.RuntimeAlert;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.notification.NotificationDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CommentDetails;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NotificationManager {
	private static final String ATLAS = "ATLAS";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.notification.NotificationManager");
	private ImplNotificationDeliveryManager notificationDeliveryManager;
	private NotificationDAO notificationDAO;
	private PropertyReaderUtil propertyReader;
	private static final String CASE = "CASE";
	private static final String SUBTEAM = "SUBTEAM";
	private static final String TEAM = "TEAM";

	public void setNotificationDeliveryManager(ImplNotificationDeliveryManager notificationDeliveryManager) {
		this.notificationDeliveryManager = notificationDeliveryManager;
	}

	public void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public List<UserMasterVO> getAssignedUsers(String crn, String userId) throws CMSException {
		new HashMap();
		List<UserMasterVO> userVoList = new ArrayList();
		new ArrayList();
		new ArrayList();
		new ArrayList();
		ArrayList userMasterVOListFinal = new ArrayList();

		try {
			HashMap<String, List<NotificationVO>> userMasterMapList = this.notificationDAO.getAssignedUsers(crn);
			List<NotificationVO> userCaseList = (List) userMasterMapList.get("CASE");
			this.logger.debug(" list of users from userSubTeamList " + userCaseList.size());
			HashMap<String, NotificationVO> userMap = new HashMap();
			Iterator userMasterVO = userCaseList.iterator();

			NotificationVO notificationVO;
			while (userMasterVO.hasNext()) {
				notificationVO = (NotificationVO) userMasterVO.next();
				userMap.put(notificationVO.getUserName(), notificationVO);
			}

			List<NotificationVO> userSubTeamList = (List) userMasterMapList.get("SUBTEAM");
			this.logger.debug(" list of users from userSubTeamList " + userSubTeamList.size());
			userMasterVO = userSubTeamList.iterator();

			while (userMasterVO.hasNext()) {
				notificationVO = (NotificationVO) userMasterVO.next();
				userMap.put(notificationVO.getUserName(), notificationVO);
			}

			List<NotificationVO> userTeamList = (List) userMasterMapList.get("TEAM");
			this.logger.debug(" list of users from usercase " + userTeamList.size());
			userMasterVO = userTeamList.iterator();

			while (userMasterVO.hasNext()) {
				notificationVO = (NotificationVO) userMasterVO.next();
				new UserMasterVO();
				userMap.put(notificationVO.getUserName(), notificationVO);
			}

			Set userSet = userMap.keySet();
			userMasterVO = null;
			String userName = "";
			Iterator iterator = userSet.iterator();

			while (iterator.hasNext()) {
				userName = (String) iterator.next();
				this.logger.debug("user name is " + userName);
				if (userName != null && !userName.equalsIgnoreCase("") && !userName.equalsIgnoreCase("null")) {
					UserMasterVO userMasterVO = new UserMasterVO();
					NotificationVO notificationVO = (NotificationVO) userMap.get(userName);
					userMasterVO.setUserFullName(notificationVO.getUserFullName());
					userMasterVO.setUsername(userName);
					userVoList.add(userMasterVO);
				}
			}

			iterator = userVoList.iterator();

			while (iterator.hasNext()) {
				UserMasterVO userMasterVoFinal = (UserMasterVO) iterator.next();
				if (!userMasterVoFinal.getUsername().equalsIgnoreCase(userId)) {
					userMasterVOListFinal.add(userMasterVoFinal);
				}
			}

			return userMasterVOListFinal;
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	public void createUserNotificationWithComments(String userName, String message, String description,
			String workstepName, String cond, List<String> sendToUserList, String crn, String recipient,
			String sentFrom, boolean isBackup) throws CMSException {
		boolean var11 = false;

		int commentID;
		try {
			CommentDetails commentDetails = new CommentDetails();
			commentDetails.setCRN(crn);
			commentDetails.setComment(description);
			commentDetails.setSentTo(recipient);
			commentDetails.setUpdatedBy(userName);
			commentDetails.setSentFrom(sentFrom);
			commentDetails.setPerformedByBakup(isBackup);
			commentID = ResourceLocator.self().getCommentService().insertComments(commentDetails);
		} catch (Exception var21) {
			throw new CMSException(this.logger, var21);
		}

		try {
			String alertName = this.getUniqueAlertName();
			this.logger.debug(" alert name generated " + alertName);
			String tempMessage = message.replaceAll("\\{", "&#123;");
			message = tempMessage.replaceAll("\\}", "&#125;");
			String tempDescription = description.replaceAll("\\{", "&#123;");
			description = tempDescription.replaceAll("\\}", "&#125;");
			this.notificationDeliveryManager.createAlerts(userName, alertName, message, (String) null, description);
			this.notificationDeliveryManager.createProcessAlerts(alertName, "temp", "temp");
			this.notificationDeliveryManager.publishProcessAlerts(alertName);
			long alertId = 0L;
			alertId = this.getMaxAlertID(userName, alertName);
			this.logger.debug("alert Id " + alertId);

			for (int i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.notificationDeliveryManager.createRuntimeAlert(alertId, (String) sendToUserList.get(i));
				}
			}

			this.logger.debug("createUserNotificationWithComments Sent From UserId ==" + userName);
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setCrn(crn);
			notificationVO.setName(alertName);
			notificationVO.setRecipient(userName);
			notificationVO.setParentAlertId(-1);
			notificationVO.setFalg(1);
			this.notificationDAO.insertCRNNotificationData(notificationVO);

			for (int i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.logger.debug(
							"createUserNotificationWithComments Sent to UserId ==" + (String) sendToUserList.get(i));
					notificationVO = new NotificationVO();
					notificationVO.setCrn(crn);
					notificationVO.setName(alertName);
					notificationVO.setRecipient((String) sendToUserList.get(i));
					notificationVO.setParentAlertId(-1);
					notificationVO.setFalg(0);
					this.notificationDAO.insertCRNNotificationData(notificationVO);
				}
			}

		} catch (Exception var20) {
			ArrayList commentIdList = new ArrayList();

			try {
				commentIdList.add(String.valueOf(commentID));
				ResourceLocator.self().getCommentService().deleteCommentsForCRN(commentIdList, userName, crn);
			} catch (Exception var19) {
				throw new CMSException(this.logger, var20);
			}

			throw new CMSException(this.logger, var20);
		}
	}

	public void createUserNotification(String userName, String message, String description, String workstepName,
			String cond, List<String> sendToUserList, String crn) throws CMSException {
		try {
			String alertName = this.getUniqueAlertName();
			this.logger.debug(" alert name generated " + alertName);
			String tempMessage = message.replaceAll("\\{", "&#123;");
			message = tempMessage.replaceAll("\\}", "&#125;");
			String tempDescription = description.replaceAll("\\{", "&#123;");
			description = tempDescription.replaceAll("\\}", "&#125;");
			this.notificationDeliveryManager.createAlerts(userName, alertName, message, (String) null, description);
			this.notificationDeliveryManager.createProcessAlerts(alertName, "temp", "temp");
			this.notificationDeliveryManager.publishProcessAlerts(alertName);
			long alertId = 0L;
			alertId = this.getMaxAlertID(userName, alertName);
			this.logger.debug("alert Id " + alertId);

			for (int i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.notificationDeliveryManager.createRuntimeAlert(alertId, (String) sendToUserList.get(i));
				}
			}

			this.logger.debug("createUserNotification Sent From UserId ==" + userName);
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setCrn(crn);
			notificationVO.setName(alertName);
			notificationVO.setRecipient(userName);
			notificationVO.setParentAlertId(-1);
			notificationVO.setFalg(1);
			this.notificationDAO.insertCRNNotificationData(notificationVO);

			for (int i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.logger.debug("createUserNotification Sent to UserId ==" + (String) sendToUserList.get(i));
					notificationVO = new NotificationVO();
					notificationVO.setCrn(crn);
					notificationVO.setName(alertName);
					notificationVO.setRecipient((String) sendToUserList.get(i));
					notificationVO.setParentAlertId(-1);
					notificationVO.setFalg(0);
					this.notificationDAO.insertCRNNotificationData(notificationVO);
				}
			}

		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
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

	public List<NotificationVO> getUserNotifications(String userName, int start, int limit, String sortColumnName,
			String sortType, NotificationVO notificationVO) throws CMSException {
		new ArrayList();
		new ArrayList();

		try {
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			notificationVO.setMinRecord(start + 1);
			notificationVO.setMaxRecord(start + limit);
			notificationVO.setSortColumnName(sortColumnName);
			notificationVO.setSortType(sortType);
			List userNotificationList;
			List userNotificationDetailsList;
			if (notificationVO.getFilter().equalsIgnoreCase("true")) {
				userNotificationList = this.notificationDAO.getUserNotificationsFilterBase(notificationVO);
				this.logger.debug("number of user message alerts based on filter " + userNotificationList.size());
				userNotificationDetailsList = this.notificationDAO.getUserNotificationDetails(notificationVO);
				this.logger.debug("details list size " + userNotificationDetailsList.size());
			} else {
				userNotificationList = this.notificationDAO.getUserNotifications(notificationVO);
				this.logger.debug("number of user message alerts default" + userNotificationList.size());
				userNotificationDetailsList = this.notificationDAO.getUserNotificationDetails(notificationVO);
				this.logger.debug("details list size " + userNotificationDetailsList.size());
			}

			Iterator var10 = userNotificationList.iterator();

			while (var10.hasNext()) {
				NotificationVO userNotificationVO = (NotificationVO) var10.next();
				Iterator var12 = userNotificationDetailsList.iterator();

				while (var12.hasNext()) {
					NotificationVO userNotificationDetailVO = (NotificationVO) var12.next();
					if (userNotificationDetailVO.getName().equalsIgnoreCase(userNotificationVO.getName())) {
						String recipient = userNotificationVO.getRecipientFullName();
						String ackDate = userNotificationVO.getAckDate();
						String sender = userNotificationVO.getSenderFullName();
						this.logger.debug("before " + ackDate);
						if (recipient == null) {
							if (userNotificationDetailVO.getRecipientFullName() != null
									&& !userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
								recipient = userNotificationDetailVO.getRecipientFullName();
							}
						} else if (!userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
							recipient = recipient + "," + userNotificationDetailVO.getRecipientFullName();
						}

						this.logger.debug(
								"userNotificationDetailVO.getAckDate() " + userNotificationDetailVO.getAckDate());
						if (ackDate == null) {
							if (userNotificationDetailVO.getRecipientFullName() != null
									&& !userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
								ackDate = userNotificationDetailVO.getAckDate();
							}
						} else if (!userNotificationDetailVO.getRecipientFullName().equalsIgnoreCase(sender)) {
							ackDate = ackDate + "," + userNotificationDetailVO.getAckDate();
						}

						this.logger.debug("ack date " + ackDate);
						this.logger.debug("recipient " + recipient);
						userNotificationVO.setRecipientFullName(recipient);
						userNotificationVO.setAckDate(ackDate);
					}
				}
			}

			return userNotificationList;
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public int getUserNotificationsCount(String userName, NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Notification Manager : getUserNotificationsCount");
		boolean var3 = false;

		int count;
		try {
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			if (notificationVO.getFilter().equalsIgnoreCase("true")) {
				count = this.notificationDAO.getUserNotificationsCountFilterBase(notificationVO);
			} else {
				count = this.notificationDAO.getUserNotificationsCount(notificationVO);
			}
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Notification Manager : getUserNotificationsCount : count :" + count);
		return count;
	}

	public int checkForNotificationCount(String userName) throws CMSException {
		boolean var2 = false;

		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			notificationVO.setId(0);
			int count = this.notificationDAO.checkForNotificationCount(notificationVO);
			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateFlag(String loginId) throws CMSException {
		int count = 0;

		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(loginId);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			notificationVO.setId(0);
			notificationVO.setFalg(1);
			List<String> list = new ArrayList();
			List<NotificationVO> alertNameList = this.notificationDAO.getUserAlerts(notificationVO);
			Iterator var7 = alertNameList.iterator();

			while (var7.hasNext()) {
				NotificationVO notificationDetailVO = (NotificationVO) var7.next();
				list.add(notificationDetailVO.getName());
				this.logger.debug("user data alert name===" + notificationDetailVO.getName());
			}

			if (list.size() > 0) {
				notificationVO.setAlertName(list);
				count = this.notificationDAO.updateFlag(notificationVO);
			}

			return count;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int updateFlagForSystem(String loginId) throws CMSException {
		this.logger.debug("in updateFlagForSystem 2");
		int count = 0;

		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(loginId);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			notificationVO.setId(0);
			notificationVO.setFalg(1);
			List<NotificationVO> alertNameList = this.notificationDAO.getSystemAlerts(notificationVO);
			List<String> list = new ArrayList();
			Iterator var7 = alertNameList.iterator();

			while (var7.hasNext()) {
				NotificationVO notificationDetailVO = (NotificationVO) var7.next();
				list.add(notificationDetailVO.getName());
				this.logger.debug("System alert name===" + notificationDetailVO.getName());
			}

			if (list.size() > 0) {
				notificationVO.setAlertName(list);
				count = this.notificationDAO.updateFlagForSystem(notificationVO);
			}

			return count;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<NotificationVO> getNotificationHistory(String userName, String crn, int start, int limit,
			String sortOrder) throws CMSException {
		NotificationVO notificationVO = new NotificationVO();
		new ArrayList();
		new ArrayList();

		try {
			notificationVO.setRecipient(userName);
			notificationVO.setCrn(crn);
			notificationVO.setMinRecord(start + 1);
			notificationVO.setMaxRecord(start + limit);
			notificationVO.setSortOrder(sortOrder);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			List<NotificationVO> notificationHistoryList = this.notificationDAO.getNotificationHistory(notificationVO);
			this.logger.debug("number of alerts " + notificationHistoryList.size());
			List<NotificationVO> userNotificationDetailsList = this.notificationDAO
					.getNotificationHistoryDetails(notificationVO);
			this.logger.debug("details list size " + userNotificationDetailsList.size());
			Iterator var10 = notificationHistoryList.iterator();

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

			return notificationHistoryList;
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public void acknowledgeNotification(String alertNames, String userName) throws Exception {
		new ArrayList();

		try {
			List<String> alertNamesList = StringUtils.commaSeparatedStringToList(alertNames);
			Iterator iterator = alertNamesList.iterator();

			while (iterator.hasNext()) {
				String alertName = (String) iterator.next();
				NotificationVO notificationVO = new NotificationVO();
				notificationVO.setName(alertName);
				notificationVO.setRecipient(userName);
				this.notificationDAO.acknowledgeNotification(notificationVO);
			}

		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void replyNotification(String userName, NotificationVO notificationVO) throws Exception {
		try {
			String crn = notificationVO.getCrn();
			int parentAlertId = notificationVO.getId();
			List<String> sendToUserList = StringUtils.commaSeparatedStringToList(notificationVO.getRecipient());
			String alertName = this.getUniqueAlertName();
			this.logger.debug(" alert name generated " + alertName);
			this.notificationDeliveryManager.createAlerts(userName, alertName, notificationVO.getName(), (String) null,
					notificationVO.getMessage());
			this.notificationDeliveryManager.createProcessAlerts(alertName, "temp", "temp");
			this.notificationDeliveryManager.publishProcessAlerts(alertName);
			long alertId = 0L;
			alertId = this.getMaxAlertID(userName, alertName);
			this.logger.debug("alert Id " + alertId);

			int i;
			for (i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.notificationDeliveryManager.createRuntimeAlert(alertId, (String) sendToUserList.get(i));
				}
			}

			this.logger.debug("replyNotification Sent From UserId ==" + userName);
			notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			notificationVO.setCrn(crn);
			notificationVO.setName(alertName);
			notificationVO.setParentAlertId(parentAlertId);
			notificationVO.setFalg(1);
			this.notificationDAO.insertCRNNotificationData(notificationVO);

			for (i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.logger.debug("replyNotification Sent to UserId ==" + (String) sendToUserList.get(i));
					notificationVO = new NotificationVO();
					notificationVO.setCrn(crn);
					notificationVO.setName(alertName);
					notificationVO.setRecipient((String) sendToUserList.get(i));
					notificationVO.setParentAlertId(parentAlertId);
					notificationVO.setFalg(0);
					this.notificationDAO.insertCRNNotificationData(notificationVO);
				}
			}

			this.acknowledgeParentMessage((long) parentAlertId, userName);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	private void acknowledgeParentMessage(long alertId, String userName) throws CMSException {
		try {
			RuntimeAlert runtimeAlert = this.notificationDeliveryManager.getRuntimeAlert(String.valueOf(alertId));
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setName(runtimeAlert.getName());
			notificationVO.setRecipient(userName);
			this.notificationDAO.acknowledgeNotification(notificationVO);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void forwardNotification(String userName, NotificationVO notificationVO) throws Exception {
		try {
			String crn = notificationVO.getCrn();
			int parentAlertId = notificationVO.getId();
			List<String> sendToUserList = StringUtils.commaSeparatedStringToList(notificationVO.getRecipient());
			String alertName = this.getUniqueAlertName();
			this.logger.debug(" alert name generated " + alertName);
			this.notificationDeliveryManager.createAlerts(userName, alertName, notificationVO.getName(), (String) null,
					notificationVO.getMessage());
			this.notificationDeliveryManager.createProcessAlerts(alertName, "temp", "temp");
			this.notificationDeliveryManager.publishProcessAlerts(alertName);
			long alertId = 0L;
			alertId = this.getMaxAlertID(userName, alertName);
			this.logger.debug("alert Id " + alertId);

			int i;
			for (i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.notificationDeliveryManager.createRuntimeAlert(alertId, (String) sendToUserList.get(i));
				}
			}

			this.logger.debug("forwardNotification Sent From UserId ==" + userName);
			notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			notificationVO.setCrn(crn);
			notificationVO.setName(alertName);
			notificationVO.setParentAlertId(parentAlertId);
			notificationVO.setFalg(1);
			this.notificationDAO.insertCRNNotificationData(notificationVO);

			for (i = 0; i < sendToUserList.size(); ++i) {
				if (!((String) sendToUserList.get(i)).equalsIgnoreCase(userName)) {
					this.logger.debug("forwardNotification Sent to UserId ==" + (String) sendToUserList.get(i));
					notificationVO = new NotificationVO();
					notificationVO.setCrn(crn);
					notificationVO.setName(alertName);
					notificationVO.setRecipient((String) sendToUserList.get(i));
					notificationVO.setParentAlertId(parentAlertId);
					notificationVO.setFalg(0);
					this.notificationDAO.insertCRNNotificationData(notificationVO);
				}
			}

		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public void createSystemNotification(String message, String description, List<String> sendToUserList, String crn)
			throws CMSException {
		try {
			this.logger.debug("number of users before distinct " + sendToUserList.size());
			String tempMessage = message.replaceAll("\\{", "&#123;");
			message = tempMessage.replaceAll("\\}", "&#125;");
			String tempDescription = description.replaceAll("\\{", "&#123;");
			description = tempDescription.replaceAll("\\}", "&#125;");
			HashMap<String, String> userMap = new HashMap();
			Iterator iterator = sendToUserList.iterator();

			while (iterator.hasNext()) {
				String userName = (String) iterator.next();
				userMap.put(userName, userName);
			}

			Set<String> userSet = userMap.keySet();
			List<String> finalUserList = new ArrayList();
			Iterator iterator = userSet.iterator();

			while (iterator.hasNext()) {
				String userName = (String) iterator.next();
				finalUserList.add(userName);
			}

			this.logger.debug("number of users after distinct " + finalUserList.size());
			this.logger.debug("going to push message for notification");
			NotificationProducerImpl notificationProducerImpl = new NotificationProducerImpl();
			notificationProducerImpl.createSystemNotification(message, description, finalUserList, crn);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public void createSystemNotification(String message, String description, String sendToUser, String crn)
			throws CMSException {
		try {
			String tempMessage = message.replaceAll("\\{", "&#123;");
			message = tempMessage.replaceAll("\\}", "&#125;");
			String tempDescription = description.replaceAll("\\{", "&#123;");
			description = tempDescription.replaceAll("\\}", "&#125;");
			List<String> sendToUserList = new ArrayList();
			sendToUserList.add(sendToUser);
			NotificationProducerImpl notificationProducerImpl = new NotificationProducerImpl();
			notificationProducerImpl.createSystemNotification(message, description, sendToUserList, crn);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List<NotificationVO> getSystemNotification(String userName, int start, int limit, String sortColumnName,
			String sortType, NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			notificationVO.setRecipient(userName);
			notificationVO.setMinRecord(start + 1);
			notificationVO.setMaxRecord(start + limit);
			notificationVO.setSortColumnName(sortColumnName);
			notificationVO.setSortType(sortType);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			List systemNotificationList;
			if (notificationVO.getFilter().equalsIgnoreCase("true")) {
				systemNotificationList = this.notificationDAO.getSystemNotificationsFilterBase(notificationVO);
			} else {
				systemNotificationList = this.notificationDAO.getSystemNotifications(notificationVO);
			}

			return systemNotificationList;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int getSystemNotificationCount(String userName, NotificationVO notificationVO) throws CMSException {
		this.logger.debug("Notification Manager : getSystemNotificationCount");
		boolean var3 = false;

		int count;
		try {
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			if (notificationVO.getFilter().equalsIgnoreCase("true")) {
				count = this.notificationDAO.getSystemNotificationsCountFilterBase(notificationVO);
			} else {
				count = this.notificationDAO.getSystemNotificationsCount(notificationVO);
			}
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Notification Manager : getSystemNotificationCount : count :" + count);
		return count;
	}

	public List<NotificationVO> getMessageTrail(String alertId, String userName) throws CMSException {
		new ArrayList();

		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			List<NotificationVO> messageTrailList = this.notificationDAO.getMessageTrail(alertId, userName,
					notificationVO);
			return messageTrailList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getNotificationHistoryCount(String userName, String crn) throws CMSException {
		NotificationVO notificationVO = new NotificationVO();
		boolean var4 = false;

		try {
			notificationVO.setRecipient(userName);
			notificationVO.setCrn(crn);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			int count = this.notificationDAO.getNotificationHistoryCount(notificationVO);
			return count;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<UserMasterVO> getSentToList(NotificationVO notificationVO, String userName) throws CMSException {
		new ArrayList();

		try {
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			List<UserMasterVO> userVoList = this.notificationDAO.getSentToList(notificationVO);
			return userVoList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<NotificationVO> getRecCaseSystemNotifications(String userName, int start, int limit,
			String sortColumnName, String sortType, NotificationVO notificationVO) throws CMSException {
		new ArrayList();

		try {
			notificationVO.setRecipient(userName);
			notificationVO.setMinRecord(start + 1);
			notificationVO.setMaxRecord(start + limit);
			notificationVO.setSortColumnName(sortColumnName);
			notificationVO.setSortType(sortType);
			List systemNotificationList;
			if (notificationVO.getFilter().equalsIgnoreCase("true")) {
				systemNotificationList = this.notificationDAO.getRecCaseSystemNotificationsFilter(notificationVO);
				this.logger.debug(
						"count of recurrence notification after applying filter " + systemNotificationList.size());
			} else {
				systemNotificationList = this.notificationDAO.getRecCaseSystemNotifications(notificationVO);
				this.logger.debug(
						"count of recurrence notification without applying filter " + systemNotificationList.size());
			}

			return systemNotificationList;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int getRecCaseSystemNotificationCount(String userName, NotificationVO notificationVO) throws CMSException {
		boolean var3 = false;

		try {
			notificationVO.setRecipient(userName);
			int count;
			if (notificationVO.getFilter().equalsIgnoreCase("true")) {
				count = this.notificationDAO.getRecCaseSystemNotificationCountFilter(notificationVO);
			} else {
				count = this.notificationDAO.getRecCaseSystemNotificationCount(notificationVO);
			}

			return count;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void createRecCase(String recClientCaseId, String recCaseSchedulerId, String userName, Session session)
			throws CMSException {
		try {
			ResourceLocator.self().getAtlasRecurrCaseSchedulerService().createRecurrCase(recCaseSchedulerId,
					recClientCaseId, userName, session);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void acknowledgeRecNotification(String alertNames, String userName) throws CMSException {
		try {
			List<String> alerts = StringUtils.commaSeparatedStringToList(alertNames.substring(1, alertNames.length()));
			Iterator iterator = alerts.iterator();

			while (iterator.hasNext()) {
				String alert = (String) iterator.next();
				String[] tempIds = alert.split("#");
				String recCaseSchedulerId = tempIds[0];
				String recClientCaseId = tempIds[1];
				ResourceLocator.self().getAtlasRecurrCaseSchedulerService().updateAcknowledgeDate(recCaseSchedulerId,
						userName);
			}

		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int getUserNotificationsCountWithoutFilter(String userName) throws CMSException {
		this.logger.debug("Notification Manager : getUserNotificationsCount");
		boolean var2 = false;

		int count;
		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			count = this.notificationDAO.getUserNotificationsCountWithOutFilter(notificationVO);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Notification Manager : getUserNotificationsCount : count :" + count);
		return count;
	}

	public int getSystemNotificationCountWithoutFilter(String userName) throws CMSException {
		boolean var2 = false;

		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			notificationVO.setSysUserName(this.propertyReader.getSystemNotificationUser());
			int count = this.notificationDAO.getSystemNotificationsCountWithoutFilter(notificationVO);
			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getRecCaseSystemNotificationCountWithoutFilter(String userName) throws CMSException {
		boolean var2 = false;

		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setRecipient(userName);
			int count = this.notificationDAO.getRecCaseSystemNotificationCountWithoutFilter(notificationVO);
			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}