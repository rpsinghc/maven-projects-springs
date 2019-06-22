package com.worldcheck.atlas.bl.flowcontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.HashMap;
import java.util.Iterator;

public class FlowNotificationManager {
	private String PH_INTERIM_NAME = "PH_INTERIM_NAME";
	private String PH_DUE_DATE = "PH_DUE_DATE";
	private String PH_OFFICE_NAME = "PH_OFFICE_NAME";
	private String PH_OLD_MAIN_ANALYST = "PH_OLD_MAIN_ANALYST";
	private String PH_NEW_MAIN_ANALYST = "PH_NEW_MAIN_ANALYST";
	private String PH_REVIEWER = "PH_REVIEWER";
	private String PH_ANALYST = "PH_ANALYST";
	private String INTERIM_ADDED;
	private String INTERIM_ADDED_NO_CYCLE;
	private String RESEARCH_DUE_DATE_UPDATED;
	private String TEAM_DUE_DATE_UPDATED;
	private String PRIMARY_TEAM_DELETED;
	private String SUPPORTING_TEAM_DELETED;
	private String VENDOR_TEAM_DELETED;
	private String BI_DUE_DATE_UPDATED;
	private String VENDOR_DUE_DATE_UPDATED;
	private String MAIN_ANALYST_REPLACED;
	private String REVIEWER_DELETED;
	private String ANALYST_DELETED;
	private String ANALYST_ADDED;
	private String RE_UPDATED;
	private HashMap<String, String> notificationsMap;
	private String crn;
	private ILogProducer logger;

	public FlowNotificationManager() {
		this.INTERIM_ADDED = "A new Interim process cycle has been added. You need to complete " + this.PH_INTERIM_NAME
				+ " cycle now.";
		this.INTERIM_ADDED_NO_CYCLE = "A new Interim process cycle has been added.";
		this.RESEARCH_DUE_DATE_UPDATED = "Research " + this.PH_INTERIM_NAME + " Due Date has been changed to "
				+ this.PH_DUE_DATE + ".";
		this.TEAM_DUE_DATE_UPDATED = "Research Due Date has been updated. Please check.";
		this.PRIMARY_TEAM_DELETED = "Primary Team, " + this.PH_OFFICE_NAME + ", has been deleted from the case.";
		this.SUPPORTING_TEAM_DELETED = "Supporting Team, " + this.PH_OFFICE_NAME + ", has been deleted from the case.";
		this.VENDOR_TEAM_DELETED = "Vendor Team, " + this.PH_OFFICE_NAME + ", has been deleted from the case.";
		this.BI_DUE_DATE_UPDATED = "BI Research Due Date has been updated. Please check.";
		this.VENDOR_DUE_DATE_UPDATED = "Vendor Research Due Date has been updated. Please check.";
		this.MAIN_ANALYST_REPLACED = "The old main analyst, " + this.PH_OLD_MAIN_ANALYST + ", is replaced by "
				+ this.PH_NEW_MAIN_ANALYST + ".";
		this.REVIEWER_DELETED = "Reviewer, " + this.PH_REVIEWER + ", is removed from this case.";
		this.ANALYST_DELETED = "Analyst, " + this.PH_ANALYST + ", has been deleted.";
		this.ANALYST_ADDED = "Analyst, " + this.PH_ANALYST + ", has been added.";
		this.RE_UPDATED = "Subject Research Element has been updated. Please check it out.";
		this.notificationsMap = new HashMap();
		this.logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.flowcontroller.FlowNotificationManager");
	}

	public FlowNotificationManager(String crn) {
		this.INTERIM_ADDED = "A new Interim process cycle has been added. You need to complete " + this.PH_INTERIM_NAME
				+ " cycle now.";
		this.INTERIM_ADDED_NO_CYCLE = "A new Interim process cycle has been added.";
		this.RESEARCH_DUE_DATE_UPDATED = "Research " + this.PH_INTERIM_NAME + " Due Date has been changed to "
				+ this.PH_DUE_DATE + ".";
		this.TEAM_DUE_DATE_UPDATED = "Research Due Date has been updated. Please check.";
		this.PRIMARY_TEAM_DELETED = "Primary Team, " + this.PH_OFFICE_NAME + ", has been deleted from the case.";
		this.SUPPORTING_TEAM_DELETED = "Supporting Team, " + this.PH_OFFICE_NAME + ", has been deleted from the case.";
		this.VENDOR_TEAM_DELETED = "Vendor Team, " + this.PH_OFFICE_NAME + ", has been deleted from the case.";
		this.BI_DUE_DATE_UPDATED = "BI Research Due Date has been updated. Please check.";
		this.VENDOR_DUE_DATE_UPDATED = "Vendor Research Due Date has been updated. Please check.";
		this.MAIN_ANALYST_REPLACED = "The old main analyst, " + this.PH_OLD_MAIN_ANALYST + ", is replaced by "
				+ this.PH_NEW_MAIN_ANALYST + ".";
		this.REVIEWER_DELETED = "Reviewer, " + this.PH_REVIEWER + ", is removed from this case.";
		this.ANALYST_DELETED = "Analyst, " + this.PH_ANALYST + ", has been deleted.";
		this.ANALYST_ADDED = "Analyst, " + this.PH_ANALYST + ", has been added.";
		this.RE_UPDATED = "Subject Research Element has been updated. Please check it out.";
		this.notificationsMap = new HashMap();
		this.logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.flowcontroller.FlowNotificationManager");
		this.crn = crn;
	}

	public void prepareInterimCycleAddedNotification(TeamAnalystMapping primaryTeam, CycleInfo cycleInfo,
			String caseManager, String updatedBy, String newCurrentCycle) throws CMSException {
		HashMap<String, String> placeholders = new HashMap();
		if (newCurrentCycle != null) {
			placeholders.put(this.PH_INTERIM_NAME, newCurrentCycle);
		}

		Iterator i$;
		String teamName;
		if (primaryTeam.isTeamAssignmentDone()) {
			i$ = primaryTeam.getAnalystTaskStatus().keySet().iterator();

			while (i$.hasNext()) {
				teamName = (String) i$.next();
				if (newCurrentCycle != null) {
					this.addToNotificationsMap(teamName, this.INTERIM_ADDED, placeholders);
				} else {
					this.addToNotificationsMap(teamName, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
				}
			}

			i$ = primaryTeam.getReviewers().keySet().iterator();

			while (i$.hasNext()) {
				teamName = (String) i$.next();
				if (newCurrentCycle != null) {
					this.addToNotificationsMap(teamName, this.INTERIM_ADDED, placeholders);
				} else {
					this.addToNotificationsMap(teamName, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
				}
			}
		}

		i$ = cycleInfo.getTeamInfo().keySet().iterator();

		while (true) {
			TeamAnalystMapping teamAnalystMapping;
			String reviewer;
			Iterator i$;
			do {
				do {
					do {
						do {
							if (!i$.hasNext()) {
								if (!updatedBy.equals(caseManager)) {
									this.addToNotificationsMap(caseManager, this.INTERIM_ADDED_NO_CYCLE,
											(HashMap) null);
								}

								return;
							}

							teamName = (String) i$.next();
						} while (teamName.contains("Primary"));

						teamAnalystMapping = (TeamAnalystMapping) cycleInfo.getTeamInfo().get(teamName);
						this.addToNotificationsMap(teamAnalystMapping.getResearchHead(), this.INTERIM_ADDED_NO_CYCLE,
								(HashMap) null);
					} while (!teamAnalystMapping.isTeamAssignmentDone());

					if (!teamName.contains("BI") && !teamName.contains("Vendor")) {
						String teamManager = ResourceLocator.self().getTeamAssignmentService()
								.getTeamManager(teamName.split("#")[1]);
						if (teamManager != null) {
							this.addToNotificationsMap(teamManager, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
						}
					}

					i$ = teamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

					while (i$.hasNext()) {
						reviewer = (String) i$.next();
						this.addToNotificationsMap(reviewer, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
					}
				} while (teamName.contains("BI"));
			} while (teamName.contains("Vendor"));

			i$ = teamAnalystMapping.getReviewers().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
			}
		}
	}

	public void prepareDueDateAddedNotification(TeamAnalystMapping team, String newCurrentCycle) {
		HashMap<String, String> placeholders = new HashMap();
		if (newCurrentCycle != null) {
			placeholders.put(this.PH_INTERIM_NAME, newCurrentCycle);
		}

		if (team.isTeamAssignmentDone()) {
			Iterator i$ = team.getAnalystTaskStatus().keySet().iterator();

			String reviewer;
			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				if (newCurrentCycle != null) {
					this.addToNotificationsMap(reviewer, this.INTERIM_ADDED, placeholders);
				} else {
					this.addToNotificationsMap(reviewer, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
				}
			}

			if (!team.getTeamName().contains("BI") && !team.getTeamName().contains("Vendor")) {
				i$ = team.getReviewers().keySet().iterator();

				while (i$.hasNext()) {
					reviewer = (String) i$.next();
					if (newCurrentCycle != null) {
						this.addToNotificationsMap(reviewer, this.INTERIM_ADDED, placeholders);
					} else {
						this.addToNotificationsMap(reviewer, this.INTERIM_ADDED_NO_CYCLE, (HashMap) null);
					}
				}
			}
		}

	}

	public void prepareResearchDueDateUpdatedNotification(TeamAnalystMapping primaryTeam, String interimCycle,
			String dueDate) throws CMSException {
		HashMap<String, String> placeholders = new HashMap();
		placeholders.put(this.PH_INTERIM_NAME, interimCycle);
		placeholders.put(this.PH_DUE_DATE, dueDate);
		if (primaryTeam.isTeamAssignmentDone()) {
			Iterator i$ = primaryTeam.getAnalystTaskStatus().keySet().iterator();

			String reviewer;
			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.RESEARCH_DUE_DATE_UPDATED, placeholders);
			}

			i$ = primaryTeam.getReviewers().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.RESEARCH_DUE_DATE_UPDATED, placeholders);
			}

			String teamManager = ResourceLocator.self().getTeamAssignmentService()
					.getTeamManager(primaryTeam.getTeamName().split("#")[1]);
			if (teamManager != null) {
				this.addToNotificationsMap(teamManager, this.RESEARCH_DUE_DATE_UPDATED, placeholders);
			}
		}

	}

	public void prepareTeamDueDateUpdatedNotification(TeamAnalystMapping team, String interimCycle)
			throws CMSException {
		this.addToNotificationsMap(team.getResearchHead(), this.TEAM_DUE_DATE_UPDATED, (HashMap) null);
		if (team.isTeamAssignmentDone()) {
			Iterator i$ = team.getAnalystTaskStatus().keySet().iterator();

			String reviewer;
			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.TEAM_DUE_DATE_UPDATED, (HashMap) null);
			}

			if (!team.getTeamName().contains("BI") && !team.getTeamName().contains("Vendor")) {
				i$ = team.getReviewers().keySet().iterator();

				while (i$.hasNext()) {
					reviewer = (String) i$.next();
					this.addToNotificationsMap(reviewer, this.TEAM_DUE_DATE_UPDATED, (HashMap) null);
				}
			}

			String teamManager = ResourceLocator.self().getTeamAssignmentService()
					.getTeamManager(team.getTeamName().split("#")[1]);
			if (teamManager != null) {
				this.addToNotificationsMap(teamManager, this.TEAM_DUE_DATE_UPDATED, (HashMap) null);
			}
		}

	}

	public void prepareBIDueDateUpdatedNotification(TeamAnalystMapping biTeam, TeamAnalystMapping primaryTeam) {
		Iterator i$ = biTeam.getAnalystTaskStatus().keySet().iterator();

		String reviewer;
		while (i$.hasNext()) {
			reviewer = (String) i$.next();
			this.addToNotificationsMap(reviewer, this.TEAM_DUE_DATE_UPDATED, (HashMap) null);
		}

		if (primaryTeam.isTeamAssignmentDone()) {
			i$ = primaryTeam.getAnalystTaskStatus().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.BI_DUE_DATE_UPDATED, (HashMap) null);
			}

			i$ = primaryTeam.getReviewers().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.BI_DUE_DATE_UPDATED, (HashMap) null);
			}
		}

	}

	public void prepareVendorDueDateUpdatedNotification(TeamAnalystMapping vendorTeam, TeamAnalystMapping primaryTeam) {
		Iterator i$ = vendorTeam.getAnalystTaskStatus().keySet().iterator();

		String reviewer;
		while (i$.hasNext()) {
			reviewer = (String) i$.next();
			this.addToNotificationsMap(reviewer, this.TEAM_DUE_DATE_UPDATED, (HashMap) null);
		}

		if (primaryTeam.isTeamAssignmentDone()) {
			i$ = primaryTeam.getAnalystTaskStatus().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.VENDOR_DUE_DATE_UPDATED, (HashMap) null);
			}

			i$ = primaryTeam.getReviewers().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.VENDOR_DUE_DATE_UPDATED, (HashMap) null);
			}
		}

	}

	private void prepareTeamDeletedNotification(CycleInfo cycleInfo, String officeName, String teamTypeNotification)
			throws CMSException {
		if (teamTypeNotification.equals(this.VENDOR_TEAM_DELETED)) {
			UserMasterVO deletedVendorManager = ResourceLocator.self().getUserService().getUserInfo(officeName);
			officeName = deletedVendorManager.getUsername();
		}

		HashMap<String, String> placeholders = new HashMap();
		placeholders.put(this.PH_OFFICE_NAME, officeName);
		Iterator i$ = cycleInfo.getTeamInfo().keySet().iterator();

		while (true) {
			TeamAnalystMapping teamAnalystMapping;
			String reviewer;
			Iterator i$;
			do {
				do {
					String teamName;
					do {
						if (!i$.hasNext()) {
							return;
						}

						teamName = (String) i$.next();
						teamAnalystMapping = (TeamAnalystMapping) cycleInfo.getTeamInfo().get(teamName);
						this.addToNotificationsMap(teamAnalystMapping.getResearchHead(), teamTypeNotification,
								placeholders);
					} while (!teamAnalystMapping.isTeamAssignmentDone());

					if (!teamName.contains("BI") && !teamName.contains("Vendor")) {
						String teamManager = ResourceLocator.self().getTeamAssignmentService()
								.getTeamManager(teamName.split("#")[1]);
						if (teamManager != null) {
							this.addToNotificationsMap(teamManager, teamTypeNotification, placeholders);
						}
					}

					i$ = teamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

					while (i$.hasNext()) {
						reviewer = (String) i$.next();
						this.addToNotificationsMap(reviewer, teamTypeNotification, placeholders);
					}
				} while (teamAnalystMapping.getTeamName().contains("BI"));
			} while (teamAnalystMapping.getTeamName().contains("Vendor"));

			i$ = teamAnalystMapping.getReviewers().keySet().iterator();

			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, teamTypeNotification, placeholders);
			}
		}
	}

	public void preparePrimaryTeamDeletedNotification(CycleInfo cycleInfo, String officeName) throws CMSException {
		this.prepareTeamDeletedNotification(cycleInfo, officeName, this.PRIMARY_TEAM_DELETED);
	}

	public void prepareSupportingTeamDeletedNotification(CycleInfo cycleInfo, String officeName) throws CMSException {
		this.prepareTeamDeletedNotification(cycleInfo, officeName, this.SUPPORTING_TEAM_DELETED);
	}

	public void prepareVendorTeamDeletedNotification(CycleInfo cycleInfo, String officeName) throws CMSException {
		this.prepareTeamDeletedNotification(cycleInfo, officeName, this.VENDOR_TEAM_DELETED);
	}

	private void prepareNotificationForTeam(TeamAnalystMapping team, String notificationType,
			HashMap<String, String> placeholders) throws CMSException {
		if (team.isTeamAssignmentDone()) {
			Iterator i$ = team.getAnalystTaskStatus().keySet().iterator();

			String reviewer;
			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, notificationType, placeholders);
			}

			if (!team.getTeamName().contains("BI") && !team.getTeamName().contains("Vendor")) {
				i$ = team.getReviewers().keySet().iterator();

				while (i$.hasNext()) {
					reviewer = (String) i$.next();
					this.addToNotificationsMap(reviewer, notificationType, placeholders);
				}
			}

			String teamManager = ResourceLocator.self().getTeamAssignmentService()
					.getTeamManager(team.getTeamName().split("#")[1]);
			if (teamManager != null) {
				this.addToNotificationsMap(teamManager, notificationType, placeholders);
			}
		}

	}

	public void prepareAnalystDeletedNotification(TeamAnalystMapping team, String deletedAnalyst) throws CMSException {
		UserMasterVO deletedAnalystInfo = ResourceLocator.self().getUserService().getUserInfo(deletedAnalyst);
		HashMap<String, String> placeholders = new HashMap();
		placeholders.put(this.PH_ANALYST, deletedAnalystInfo.getUsername());
		this.prepareNotificationForTeam(team, this.ANALYST_DELETED, placeholders);
	}

	public void prepareAnalystAddedNotification(TeamAnalystMapping team, String addedAnalyst) throws CMSException {
		UserMasterVO addedAnalystInfo = ResourceLocator.self().getUserService().getUserInfo(addedAnalyst);
		HashMap<String, String> placeholders = new HashMap();
		placeholders.put(this.PH_ANALYST, addedAnalystInfo.getUsername());
		this.prepareNotificationForTeam(team, this.ANALYST_ADDED, placeholders);
	}

	public void prepareMainAnalystReplacedNotification(TeamAnalystMapping team, String oldMainAnalyst,
			String newMainAnalyst) throws CMSException {
		UserMasterVO oldMainAnalystInfo = ResourceLocator.self().getUserService().getUserInfo(oldMainAnalyst);
		UserMasterVO newMainAnalystInfo = ResourceLocator.self().getUserService().getUserInfo(newMainAnalyst);
		HashMap<String, String> placeholders = new HashMap();
		placeholders.put(this.PH_OLD_MAIN_ANALYST, oldMainAnalystInfo.getUsername());
		placeholders.put(this.PH_NEW_MAIN_ANALYST, newMainAnalystInfo.getUsername());
		this.prepareNotificationForTeam(team, this.MAIN_ANALYST_REPLACED, placeholders);
	}

	public void prepareReviewerRemovedNotification(TeamAnalystMapping team, String deletedReviewer)
			throws CMSException {
		UserMasterVO deletedReviewerInfo = ResourceLocator.self().getUserService().getUserInfo(deletedReviewer);
		HashMap<String, String> placeholders = new HashMap();
		placeholders.put(this.PH_REVIEWER, deletedReviewerInfo.getUsername());
		this.prepareNotificationForTeam(team, this.REVIEWER_DELETED, placeholders);
	}

	public void prepareREUpdatedNotification(TeamAnalystMapping team, String caseManager, String updatedBy) {
		if (team.isTeamAssignmentDone()) {
			Iterator i$ = team.getAnalystTaskStatus().keySet().iterator();

			String reviewer;
			while (i$.hasNext()) {
				reviewer = (String) i$.next();
				this.addToNotificationsMap(reviewer, this.RE_UPDATED, (HashMap) null);
			}

			if (!team.getTeamName().contains("BI") && !team.getTeamName().contains("Vendor")) {
				i$ = team.getReviewers().keySet().iterator();

				while (i$.hasNext()) {
					reviewer = (String) i$.next();
					this.addToNotificationsMap(reviewer, this.RE_UPDATED, (HashMap) null);
				}
			}
		}

		if (!updatedBy.equals(caseManager)) {
			this.addToNotificationsMap(caseManager, this.RE_UPDATED, (HashMap) null);
		}

	}

	private void addToNotificationsMap(String user, String notificationString, HashMap<String, String> placeholders) {
		String notification = notificationString;
		String placeholder;
		if (placeholders != null) {
			for (Iterator i$ = placeholders.keySet().iterator(); i$.hasNext(); notification = notification
					.replaceAll(placeholder, (String) placeholders.get(placeholder))) {
				placeholder = (String) i$.next();
			}
		}

		String userNotificationKey = user + "#" + notification;
		this.logger.debug("adding notification to map :: " + user + " :: " + notification);
		this.notificationsMap.put(userNotificationKey, notification);
	}

	public void sendNotifications() throws CMSException {
		Iterator i$ = this.notificationsMap.keySet().iterator();

		while (i$.hasNext()) {
			String userNotificationKey = (String) i$.next();
			String sendToUser = userNotificationKey.split("#")[0];
			String message = (String) this.notificationsMap.get(userNotificationKey);
			this.logger.debug("sending notification :: " + sendToUser + " :: " + message);
			ResourceLocator.self().getNotificationService().createSystemNotification(message, message, sendToUser,
					this.crn);
		}

	}
}