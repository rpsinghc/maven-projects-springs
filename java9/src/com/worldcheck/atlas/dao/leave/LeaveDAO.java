package com.worldcheck.atlas.dao.leave;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.leave.LeaveVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class LeaveDAO extends SqlMapClientTemplate {
	private static final String USER_NAME = "userName";
	private static final String GET_LEAVESCOUNT_QUERYSTRING = "AdminConsole.getLeavesCount";
	private static final String GET_USERONLEAVE_QUERYSTRING = "AdminConsole.getUserOnLeave";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.leave.LeaveDAO");
	private String GET_LEAVES_QUERYSTRING = "AdminConsole.getLeaves";
	private String INSERT_LEAVES_QUERYSTRING = "AdminConsole.insertLeave";
	private String UPDATE_LEAVES_QUERYSTRING = "AdminConsole.updateLeave";
	private String DELETE_LEAVES_QUERYSTRING = "AdminConsole.deleteLeave";
	private String REPORTS_TO = "reportsTo";

	public List<LeaveVO> getLeaves(String userId, String startDate, String endDate, String reportsTo, int start,
			int limit, String sortColumn, String sortType) throws CMSException {
		new ArrayList();
		HashMap<String, Object> parameterMap = new HashMap();
		parameterMap.put("startDate", startDate);
		parameterMap.put("endDate", endDate);
		parameterMap.put("userId", userId);
		parameterMap.put(this.REPORTS_TO, reportsTo);
		parameterMap.put("start", new Integer(start + 1));
		parameterMap.put("limit", new Integer(start + limit));
		parameterMap.put("sort", sortColumn);
		parameterMap.put("dir", sortType);

		try {
			List<LeaveVO> leaveList = this.queryForList(this.GET_LEAVES_QUERYSTRING, parameterMap);
			return leaveList;
		} catch (DataAccessException var13) {
			CMSException cmsException = new CMSException(this.logger, var13);
			throw cmsException;
		}
	}

	public int deleteLeave(String leaveIds) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.delete(this.DELETE_LEAVES_QUERYSTRING, leaveIds);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int insertLeave(LeaveVO leaveVO) throws CMSException {
		boolean var2 = false;

		try {
			int leaveId = (Integer) this.insert(this.INSERT_LEAVES_QUERYSTRING, leaveVO);
			return leaveId;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int updateLeave(LeaveVO leaveVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update(this.UPDATE_LEAVES_QUERYSTRING, leaveVO);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getLeavesCount(String userName, String startDate, String endDate, String reportsTo) throws CMSException {
		int leaveCount = false;
		HashMap parameterMap = new HashMap();
		parameterMap.put("startDate", startDate);
		parameterMap.put("endDate", endDate);
		parameterMap.put("userId", userName);
		parameterMap.put(this.REPORTS_TO, reportsTo);

		try {
			int leaveCount = (Integer) this.queryForObject("AdminConsole.getLeavesCount", parameterMap);
			return leaveCount;
		} catch (DataAccessException var9) {
			CMSException cmsException = new CMSException(this.logger, var9);
			throw cmsException;
		}
	}

	public List<String> getUserOnLeave(List<String> userName) throws CMSException {
		new ArrayList();
		HashMap map = new HashMap();
		map.put("userName", userName);

		try {
			List<String> users = this.queryForList("AdminConsole.getUserOnLeave", map);
			this.logger.debug(" users  " + users.size());
			return users;
		} catch (DataAccessException var6) {
			CMSException cmsException = new CMSException(this.logger, var6);
			throw cmsException;
		}
	}
}