package com.worldcheck.atlas.vo.report;

public class TimeTrackerVO {
	private String office;
	private String reportType;
	private String task;
	private float averageTime;

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public float getAverageTime() {
		return this.averageTime;
	}

	public void setAverageTime(float averageTime) {
		this.averageTime = averageTime;
	}
}