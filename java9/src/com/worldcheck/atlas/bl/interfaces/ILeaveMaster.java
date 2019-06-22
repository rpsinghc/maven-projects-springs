package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.leave.LeaveDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.leave.LeaveVO;
import java.util.List;

public interface ILeaveMaster {
	void setLeaveDAO(LeaveDAO var1);

	void addLeave(LeaveVO var1) throws CMSException;

	List<LeaveVO> getLeaves(String var1, String var2, String var3, String var4, int var5, int var6, String var7,
			String var8) throws CMSException;

	int getLeavesCount(String var1, String var2, String var3, String var4) throws CMSException;

	void deleteLeave(String var1) throws CMSException;

	void updateLeave(String[] var1, String var2) throws CMSException;

	String getUserOnLeaveList(List<String> var1) throws CMSException;

	int checkExistLeave(LeaveVO var1) throws CMSException;

	int checkExistLeaveBulk(LeaveVO var1) throws CMSException;
}