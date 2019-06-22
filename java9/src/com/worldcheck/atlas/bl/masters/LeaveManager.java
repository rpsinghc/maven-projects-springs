package com.worldcheck.atlas.bl.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ILeaveMaster;
import com.worldcheck.atlas.dao.leave.LeaveDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.leave.LeaveVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONValue;

public class LeaveManager implements ILeaveMaster {
	private static final char COMMA_CHAR = ',';
	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String USER_LEAVE_ID = "userLeaveId";
	private static final String LEAVE_TYPE = "leaveType";
	private static final String FULL_DAY_LEAVE = "Full Day Leave";
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.LeaveManager");
	private LeaveDAO leaveDAO = null;

	public void setLeaveDAO(LeaveDAO leaveDAO) {
		this.leaveDAO = leaveDAO;
	}

	public void addLeave(LeaveVO leaveVO) throws CMSException {
		this.logger.debug("Going to insert leave is database");
		this.leaveDAO.insertLeave(leaveVO);
		this.logger.info("Leave added successfully");
	}

	public List<LeaveVO> getLeaves(String userId, String startDate, String endDate, String reportsTo, int start,
			int limit, String sortColumn, String sortType) throws CMSException {
		new ArrayList();
		this.logger.debug("Going to fetch list of leaves for userId " + userId + " start date " + startDate
				+ " end date " + endDate + " reports to " + reportsTo + " SortColumn: " + sortColumn + " SortType :"
				+ sortType);
		List<LeaveVO> leaveList = this.leaveDAO.getLeaves(userId, startDate, endDate, reportsTo, start, limit,
				sortColumn, sortType);
		this.logger.info("Leaves extracted successfully number of leaves found " + leaveList.size());
		return leaveList;
	}

	public int getLeavesCount(String userName, String startDate, String endDate, String reportsTo) throws CMSException {
		int leaveCount = false;
		this.logger.debug("Going to fetch list of leaves for userId " + userName + " start date " + startDate
				+ " end date " + endDate + " reports to " + reportsTo);
		int leaveCount = this.leaveDAO.getLeavesCount(userName, startDate, endDate, reportsTo);
		this.logger.info("leave count extracted successfully number of leaves found " + leaveCount);
		return leaveCount;
	}

	public void deleteLeave(String leaveIds) throws CMSException {
		this.logger.debug("Going to delete list of leaves for leave Ids " + leaveIds);

		try {
			leaveIds = leaveIds.substring(0, leaveIds.lastIndexOf(44));
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.leaveDAO.deleteLeave(leaveIds);
		this.logger.info("leave deleted  " + leaveIds);
	}

	public void updateLeave(String[] modifiedRecords, String userName) throws CMSException {
		ArrayList listLeaveVOs = new ArrayList();

		try {
			LeaveVO leaveVO;
			for (int i = 0; i < modifiedRecords.length; ++i) {
				leaveVO = new LeaveVO();
				String jsonString = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(jsonString);
				this.logger.debug("json object " + jsonObject);
				this.logger.debug("from date " + jsonObject.get("fromDate"));
				leaveVO.setFromDate((String) jsonObject.get("fromDate"));
				leaveVO.setToDate((String) jsonObject.get("toDate"));
				leaveVO.setLeaveType((String) jsonObject.get("leaveType"));
				leaveVO.setUserLeaveId(Integer.parseInt((String) jsonObject.get("userLeaveId")));
				leaveVO.setUpdatedBy(userName);
				listLeaveVOs.add(leaveVO);
			}

			this.logger.debug("Going to update list of leaves size is " + listLeaveVOs.size());
			Iterator iterator = listLeaveVOs.iterator();

			while (iterator.hasNext()) {
				leaveVO = (LeaveVO) iterator.next();
				this.logger.info("Going to update leave with leave id " + leaveVO.getUserLeaveId());
				this.leaveDAO.updateLeave(leaveVO);
				this.logger.info("leave updated  " + leaveVO.getUserLeaveId());
			}

		} catch (NumberFormatException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public String getUserOnLeaveList(List<String> userName) throws CMSException {
		List<String> userList = new ArrayList();
		if (userName.size() < 1000 && userName.size() > 0) {
			userList = this.leaveDAO.getUserOnLeave(userName);
		} else {
			int k = 0;

			while (k < userName.size()) {
				int start = k;
				int end = k + 1000;
				this.logger.debug("start is before " + k + " end is " + end);
				if (end >= userName.size()) {
					end = userName.size() + 1;
				}

				this.logger.debug("k is " + k + " end is " + end);
				this.logger.debug("start is after " + k + " end is " + end);
				List<String> tempUserList = this.leaveDAO.getUserOnLeave(userName.subList(k, end - 1));
				k += 999;
				this.logger.debug("start is after k updated " + start + " end is " + end);
				((List) userList).addAll(tempUserList);
			}
		}

		StringBuffer resultString = new StringBuffer();
		Iterator iterator = ((List) userList).iterator();

		while (iterator.hasNext()) {
			String userId = (String) iterator.next();
			if (resultString.length() == 0) {
				resultString.append(userId);
			} else {
				resultString.append("," + userId);
			}
		}

		this.logger.info("user names on leave " + resultString);
		return resultString.toString();
	}

	public List<UserMasterVO> getUserList(UserBean userBean, List roleList) throws CMSException {
		try {
			this.logger.debug(" getting user list");
			List userMasterVOList;
			if (roleList.contains("R1")) {
				userMasterVOList = ResourceLocator.self().getUserService().getActiveUserList();
				this.logger.debug(" user master vo list " + userMasterVOList);
			} else {
				userMasterVOList = ResourceLocator.self().getUserService().getSubOrdinateList(userBean.getUserName());
				UserMasterVO vo = ResourceLocator.self().getUserService().getUserInfo(userBean.getUserName());
				UserMasterVO userMasterVO = new UserMasterVO();
				userMasterVO.setUserFullName(vo.getUsername());
				userMasterVO.setUsername(vo.getUserID());
				userMasterVOList.add(userMasterVO);
			}

			return userMasterVOList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public LeaveVO getBackupDetails(UserBean userBean, String userName, String blank, String leaveType, String fromDate,
			String toDate) throws CMSException {
		LeaveVO leaveVO = new LeaveVO();

		try {
			List userMasterVOList;
			Iterator iterator;
			UserMasterVO userVO;
			if (null != blank && !blank.equals("")) {
				userMasterVOList = ResourceLocator.self().getUserService().getBackUpList(userName);
				iterator = userMasterVOList.iterator();

				while (iterator.hasNext()) {
					userVO = (UserMasterVO) iterator.next();
					leaveVO.setBackup1(userVO.getBackup1());
					leaveVO.setBackup2(userVO.getBackup2());
					leaveVO.setUserFullName(userVO.getUserFullName());
				}

				leaveVO.setUserId(userName);
				leaveVO.setUserName(userName);
				leaveVO.setUsername(userName);
				if (null != leaveType && !leaveType.equals("")) {
					leaveVO.setLeaveType(leaveType);
				} else {
					leaveVO.setLeaveType("Full Day Leave");
				}

				leaveVO.setFromDate(fromDate);
				leaveVO.setToDate(toDate);
			} else {
				userMasterVOList = ResourceLocator.self().getUserService().getBackUpList(userBean.getUserName());
				iterator = userMasterVOList.iterator();

				while (iterator.hasNext()) {
					userVO = (UserMasterVO) iterator.next();
					leaveVO.setBackup1(userVO.getBackup1());
					leaveVO.setBackup2(userVO.getBackup2());
					leaveVO.setUserFullName(userVO.getUserFullName());
				}

				leaveVO.setUserId(userBean.getUserName());
				leaveVO.setUserName(userBean.getUserName());
				leaveVO.setUsername(userBean.getUserName());
				leaveVO.setFromDate(this.sdf.format(new Date()));
				leaveVO.setToDate(this.sdf.format(new Date()));
				leaveVO.setLeaveType("Full Day Leave");
			}

			return leaveVO;
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public int checkExistLeave(LeaveVO leaveVO) throws CMSException {
		return this.leaveDAO.getLeavesCount(leaveVO.getUserId(), leaveVO.getFromDate(), leaveVO.getToDate(),
				(String) null);
	}

	public int checkExistLeaveBulk(LeaveVO leaveVO) throws CMSException {
		byte canUpdate = 0;

		try {
			List<String> userIdArray = StringUtils.commaSeparatedStringToList(leaveVO.getUserId());
			List<String> fromDateArray = StringUtils.commaSeparatedStringToList(leaveVO.getFromDate());
			List<String> toDateArray = StringUtils.commaSeparatedStringToList(leaveVO.getToDate());

			for (int i = 0; i < toDateArray.size(); ++i) {
				if (this.leaveDAO.getLeavesCount((String) userIdArray.get(i), (String) fromDateArray.get(i),
						(String) toDateArray.get(i), (String) null) > 0) {
					canUpdate = 1;
					break;
				}
			}
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.info(" leaves exist user can update flag is " + canUpdate);
		return canUpdate;
	}
}