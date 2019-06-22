package com.worldcheck.atlas.services.timetracker;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ITimeTracker;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.util.List;

public class TimeTrackerService {
	ITimeTracker timeTrackerManager = null;

	public void setTimeTrackerManager(ITimeTracker timeTrackerManager) {
		this.timeTrackerManager = timeTrackerManager;
	}

	public int startTimeTracker(TimeTrackerVO timeTrackerVO) throws CMSException {
		return this.timeTrackerManager.startTimeTracker(timeTrackerVO);
	}

	public void stopTimeTracker(TimeTrackerVO timeTrackerVO) throws CMSException {
		this.timeTrackerManager.stopTimeTracker(timeTrackerVO);
	}

	public TimeTrackerVO checkActiveTaskForUser(String userId, String crn, Session session, String caseHistoryPerformer)
			throws CMSException {
		return this.timeTrackerManager.checkActiveTaskForUser(userId, crn, session, caseHistoryPerformer);
	}

	public List<TimeTrackerVO> isTimeTrackerOnForUser(String userId) throws CMSException {
		return this.timeTrackerManager.isTimeTrackerOnForUser(userId);
	}

	public int stopTimeTrackerForUser(String userId) throws CMSException {
		return this.timeTrackerManager.stopTimeTrackerForUser(userId);
	}

	public void stopAllTrackerForCase(String crn, String userId) throws CMSException {
		this.timeTrackerManager.stopAllTrackerForCase(crn, userId);
	}

	public int checkTTOnForTask(String crn, String taskName, String userName) throws CMSException {
		return this.timeTrackerManager.checkTTOnForTask(crn, taskName, userName);
	}

	public boolean checkTimeTrackerForUserTask(TimeTrackerVO timeTrackerVO) throws CMSException {
		return this.timeTrackerManager.checkTimeTrackerForUserTask(timeTrackerVO);
	}
}