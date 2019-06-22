package com.worldcheck.atlas.vo.timetracker;

import java.util.Date;

public class TimeTrackerVO {
	private int trackerId;
	private String crn;
	private String processCycle;
	private String userName;
	private String taskName;
	private Date startTime;
	private Date endTime;
	private float totalTime;
	private String reportType;
	private String updatedBy;
	private String taskPid;
	private String taskStatus;
	private String workItemName;

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public int getTrackerId() {
		return this.trackerId;
	}

	public void setTrackerId(int trackerId) {
		this.trackerId = trackerId;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getTotalTime() {
		return this.totalTime;
	}

	public void setTotalTime(float totalTime) {
		this.totalTime = totalTime;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getTaskPid() {
		return this.taskPid;
	}

	public void setTaskPid(String taskPid) {
		this.taskPid = taskPid;
	}

	public String getWorkItemName() {
		return this.workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
}