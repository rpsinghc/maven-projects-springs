package com.worldcheck.atlas.vo.report;

public class ProductivityPointsAndCasesVO {
	private String supervisor;
	private String analyst;
	private int month;
	private float cmpPoints;
	private int cmpCases;
	private float wipPoints;
	private int wipCases;
	private float totalPoints;
	private int totalCases;
	private int year;

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public float getTotalPoints() {
		return this.totalPoints;
	}

	public void setTotalPoints(float totalPoints) {
		this.totalPoints = totalPoints;
	}

	public int getTotalCases() {
		return this.totalCases;
	}

	public void setTotalCases(int totalCases) {
		this.totalCases = totalCases;
	}

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

	public float getCmpPoints() {
		return this.cmpPoints;
	}

	public void setCmpPoints(float cmpPoints) {
		this.cmpPoints = cmpPoints;
	}

	public int getCmpCases() {
		return this.cmpCases;
	}

	public void setCmpCases(int cmpCases) {
		this.cmpCases = cmpCases;
	}

	public float getWipPoints() {
		return this.wipPoints;
	}

	public void setWipPoints(float wipPoints) {
		this.wipPoints = wipPoints;
	}

	public int getWipCases() {
		return this.wipCases;
	}

	public void setWipCases(int wipCases) {
		this.wipCases = wipCases;
	}
}