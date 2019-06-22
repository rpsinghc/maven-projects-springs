package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.util.List;

public interface ITimeTracker {
	int startTimeTracker(TimeTrackerVO var1) throws CMSException;

	void stopTimeTracker(TimeTrackerVO var1) throws CMSException;

	int stopTimeTrackerForUser(String var1) throws CMSException;

	TimeTrackerVO checkActiveTaskForUser(String var1, String var2, Session var3, String var4) throws CMSException;

	List<TimeTrackerVO> isTimeTrackerOnForUser(String var1) throws CMSException;

	void stopAllTrackerForCase(String var1, String var2) throws CMSException;

	int checkTTOnForTask(String var1, String var2, String var3) throws CMSException;

	boolean checkTimeTrackerForUserTask(TimeTrackerVO var1) throws CMSException;
}