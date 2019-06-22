package com.worldcheck.atlas.sbm;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.AnalystTaskStatus;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class SBMNotificationManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.SBMNotificationManager");
	private ResourceLocator locator = ResourceLocator.self();

	public void checkAndSendNotification(String crn, String notificationType, Session session, String processCycle)
			throws CMSException {
		long pid = 0L;
		pid = this.locator.getOfficeAssignmentService().getPIDForCRN(crn);
		this.logger.debug("pid is " + pid);
		Object object = this.locator.getSBMService().getDataslotValue(pid, "customDSMap", session);
		CycleTeamMapping cycleTeamMap = null;
		CycleInfo cycleInfo = null;
		String mainAnalyst = "";
		if (null != object) {
			cycleTeamMap = (CycleTeamMapping) ((HashMap) object).get("CycleTeamMapping");
			if (null != cycleTeamMap) {
				cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation().get(processCycle);
				if (null != cycleInfo) {
					Map<String, TeamAnalystMapping> mapOfTeams = cycleInfo.getTeamInfo();
					Iterator iterator = mapOfTeams.keySet().iterator();

					while (iterator.hasNext()) {
						String teamName = (String) iterator.next();
						if (teamName.contains("Primary")) {
							mainAnalyst = ((TeamAnalystMapping) mapOfTeams.get(teamName)).getMainAnalyst();
							this.logger.debug("Main analyst of primary team is " + mainAnalyst);
							break;
						}
					}
				}
			}
		}

		boolean isConditionMatched = this.confirmNotification(cycleInfo, notificationType, processCycle);
		if (isConditionMatched && notificationType.equalsIgnoreCase("STTaskCompleted")) {
			this.logger.debug("Main analyst is " + mainAnalyst);
			Vector<String> reviewers = (Vector) this.locator.getSBMService().getDataslotValue(pid, "Reviewers",
					session);
			this.sendNotificationToST(reviewers, mainAnalyst, crn, processCycle);
		} else if (isConditionMatched && notificationType.equalsIgnoreCase("PTTaskCompleted")) {
			this.logger.debug("Main analyst is " + mainAnalyst);
			this.sendNotificationToPrimary(mainAnalyst, crn);
		} else if (isConditionMatched && notificationType.equalsIgnoreCase("financeTaskGenearted")) {
			this.sendNotificationToFinanceQueue(crn);
		}

	}

	private boolean confirmNotification(CycleInfo cycleInfo, String notificationType, String processCycle) {
		boolean isConfirmed = true;
		Map mapOfTeams;
		if (notificationType.equalsIgnoreCase("STTaskCompleted")) {
			mapOfTeams = cycleInfo.getTeamInfo();
			isConfirmed = this.checkSTTeamStatus(isConfirmed, mapOfTeams);
		} else if (notificationType.equalsIgnoreCase("PTTaskCompleted")) {
			mapOfTeams = cycleInfo.getTeamInfo();
			isConfirmed = this.checkSTTeamStatus(isConfirmed, mapOfTeams);
			if (!isConfirmed) {
				isConfirmed = this.checkPTTeamStatus(isConfirmed, mapOfTeams);
			}
		} else if (notificationType.equalsIgnoreCase("financeTaskGenearted")) {
			;
		}

		return isConfirmed;
	}

	private boolean checkSTTeamStatus(boolean isConfirmed, Map<String, TeamAnalystMapping> mapOfTeams) {
		Iterator iterator = mapOfTeams.keySet().iterator();

		while (true) {
			while (true) {
				String teamName;
				do {
					if (!iterator.hasNext()) {
						return isConfirmed;
					}

					teamName = (String) iterator.next();
					this.logger.debug("team name is " + teamName);
				} while (teamName.contains("Primary"));

				TeamAnalystMapping temAnalystMapping = (TeamAnalystMapping) mapOfTeams.get(teamName);
				Map<String, AnalystTaskStatus> mapOfAnalysts = temAnalystMapping.getAnalystTaskStatus();
				Iterator iterator1 = mapOfAnalysts.keySet().iterator();

				while (iterator1.hasNext()) {
					String analystName = (String) iterator1.next();
					this.logger.debug("Analyst name is " + analystName);
					AnalystTaskStatus analystStatus = (AnalystTaskStatus) mapOfAnalysts.get(analystName);
					this.logger.debug("Analyst status is " + analystStatus.getStatus());
					if (!"Done".equalsIgnoreCase(analystStatus.getStatus())) {
						isConfirmed = false;
						break;
					}
				}
			}
		}
	}

	private boolean checkPTTeamStatus(boolean isConfirmed, Map<String, TeamAnalystMapping> mapOfTeams) {
		Iterator iterator = mapOfTeams.keySet().iterator();

		while (true) {
			while (true) {
				String teamName;
				do {
					if (!iterator.hasNext()) {
						return isConfirmed;
					}

					teamName = (String) iterator.next();
					this.logger.debug("team name is " + teamName);
				} while (!teamName.contains("Primary"));

				TeamAnalystMapping temAnalystMapping = (TeamAnalystMapping) mapOfTeams.get(teamName);
				Map<String, AnalystTaskStatus> mapOfAnalysts = temAnalystMapping.getAnalystTaskStatus();
				Iterator iterator1 = mapOfAnalysts.keySet().iterator();

				while (iterator1.hasNext()) {
					String analystName = (String) iterator1.next();
					this.logger.debug("Analyst name is " + analystName);
					AnalystTaskStatus analystStatus = (AnalystTaskStatus) mapOfAnalysts.get(analystName);
					this.logger.debug("Analyst status is " + analystStatus.getStatus());
					if (!"Done".equalsIgnoreCase(analystStatus.getStatus())) {
						isConfirmed = false;
						break;
					}
				}
			}
		}
	}

	private void sendNotificationToST(Vector<String> reviewers, String mainAnalyst, String crn, String processCycle)
			throws CMSException {
		List<String> sendToUserList = new ArrayList();
		Iterator iterator = reviewers.iterator();

		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			sendToUserList.add(string);
		}

		sendToUserList.add(mainAnalyst);
		this.locator.getNotificationService().createSystemNotification(
				SavvionConstants.notificationMessageForST + processCycle + SavvionConstants.processCycleNot,
				SavvionConstants.notificationMessageForST + processCycle + SavvionConstants.processCycleNot,
				sendToUserList, crn);
	}

	private void sendNotificationToPrimary(String mainAnalyst, String crn) throws CMSException {
		this.locator.getNotificationService().createSystemNotification(
				"Awaiting Consolidation Task. Other teams have not completed their tasks. Please wait until all of the other teams have completed their tasks before starting your Consolidation task.",
				"Awaiting Consolidation Task. Other teams have not completed their tasks. Please wait until all of the other teams have completed their tasks before starting your Consolidation task.",
				mainAnalyst, crn);
	}

	private void sendNotificationToFinanceQueue(String crn) throws CMSException {
		List<UserMasterVO> listOfUsers = this.locator.getUserService().getUsersForRole("R3");
		List<String> userList = new ArrayList();
		Iterator iterator = listOfUsers.iterator();

		while (iterator.hasNext()) {
			UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
			userList.add(userMasterVO.getUserID());
		}

		this.locator.getNotificationService().createSystemNotification("Please invoice this case.",
				"Please invoice this case.", userList, crn);
	}
}