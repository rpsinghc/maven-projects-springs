package com.worldcheck.atlas.vo.leave;

import java.util.Date;

public class LeaveVO {
	private String userName;
	private String username;
	private String userFullName;
	private String userId;
	private int userLeaveId;
	private String backup1;
	private String backup2;
	private int leaveTypeId;
	private String leaveType;
	private String fromDate;
	private String toDate;
	private Date updatedOn;
	private String updatedBy;
	private String[] modifiedRecords;

	public String[] getModifiedRecords() {
		return this.modifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.modifiedRecords = modifiedRecords;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getUserLeaveId() {
		return this.userLeaveId;
	}

	public void setUserLeaveId(int userLeaveId) {
		this.userLeaveId = userLeaveId;
	}

	public String getBackup1() {
		return this.backup1;
	}

	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}

	public String getBackup2() {
		return this.backup2;
	}

	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}

	public int getLeaveTypeId() {
		return this.leaveTypeId;
	}

	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getLeaveType() {
		return this.leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return this.toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUserFullName() {
		return this.userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}