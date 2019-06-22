package com.worldcheck.atlas.dao.timetracker;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TimeTrackerDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.timetracker.TimeTrackerDAO");
	private String STOP_ALL_TT_FOR_CASE_SQL = "TimeTracker.stopAllTTForCase";
	private String STOP_TT_FOR_USER_SQL = "TimeTracker.stopTTForUser";
	private String GET_TT_ON_INFO_SQL = "TimeTracker.getOnTrackerInfo";
	private String STOP_TT_SQL = "TimeTracker.stopTT";
	private String START_TT_SQL = "TimeTracker.startTT";
	private String CHECK_TT_FOR_TASK_SQL = "TimeTracker.checkTTOnForTask";
	private String GET_TT_INFO_CASE_SQL = "TimeTracker.getTrackerInfoForCase";
	private String CHECK_TT_FOR_USER_TASK_SQL = "TimeTracker.checkTTForUserTask";

	public int insert(TimeTrackerVO timeTrackerVO) throws CMSException {
		try {
			return (Integer) this.insert(this.START_TT_SQL, timeTrackerVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int update(TimeTrackerVO timeTrackerVO) throws CMSException {
		try {
			int updateCount = Integer.valueOf(this.update(this.STOP_TT_SQL, timeTrackerVO));
			this.logger.debug("updated :: " + updateCount);
			return updateCount;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TimeTrackerVO> getTrackerInfoForUser(String userId) throws CMSException {
		try {
			return this.queryForList(this.GET_TT_ON_INFO_SQL, userId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int stopTimeTrackerForUser(String userId) throws CMSException {
		try {
			return Integer.valueOf(this.update(this.STOP_TT_FOR_USER_SQL, userId));
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int checkTTOnForTask(HashMap param) throws CMSException {
		try {
			return Integer.parseInt(this.queryForObject(this.CHECK_TT_FOR_TASK_SQL, param).toString());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int stopAllTrackerForCase(HashMap param) throws CMSException {
		try {
			return Integer.valueOf(this.update(this.STOP_ALL_TT_FOR_CASE_SQL, param));
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TimeTrackerVO> getTrackerInfoForCase(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_TT_INFO_CASE_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int checkTimeTrackerForUserTask(TimeTrackerVO timeTrackerVO) throws CMSException {
		try {
			return Integer.parseInt(this.queryForObject(this.CHECK_TT_FOR_USER_TASK_SQL, timeTrackerVO).toString());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}