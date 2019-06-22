package com.worldcheck.atlas.vo.report;

public class UtilizationByRevenueVO {
	private String supervisor;
	private String analyst;
	private int month;
	private int year;
	private String cmpRevenue;
	private String wipRevenue;
	private String ttlRevenue;

	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public int getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCmpRevenue() {
		return this.cmpRevenue;
	}

	public void setCmpRevenue(String cmpRevenue) {
		this.cmpRevenue = cmpRevenue;
	}

	public String getWipRevenue() {
		return this.wipRevenue;
	}

	public void setWipRevenue(String wipRevenue) {
		this.wipRevenue = wipRevenue;
	}

	public String getTtlRevenue() {
		return this.ttlRevenue;
	}

	public void setTtlRevenue(String ttlRevenue) {
		this.ttlRevenue = ttlRevenue;
	}
}