package com.worldcheck.atlas.vo.report;

public class ProductivityAndRevenueSummaryVO {
	private String office;
	private float avgWipPoints;
	private float avgWipPointsForMonth;
	private float avgCmpPointsForMonth;
	private float avgWipRevenue;
	private float avgWipRevenueForMonth;
	private float avgCmpRevenueForMonth;
	private float totalPoints;
	private float totalRevenue;

	public float getTotalPoints() {
		return this.totalPoints;
	}

	public void setTotalPoints(float totalPoints) {
		this.totalPoints = totalPoints;
	}

	public float getTotalRevenue() {
		return this.totalRevenue;
	}

	public void setTotalRevenue(float totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public float getAvgWipPoints() {
		return this.avgWipPoints;
	}

	public void setAvgWipPoints(float avgWipPoints) {
		this.avgWipPoints = avgWipPoints;
	}

	public float getAvgWipPointsForMonth() {
		return this.avgWipPointsForMonth;
	}

	public void setAvgWipPointsForMonth(float avgWipPointsForMonth) {
		this.avgWipPointsForMonth = avgWipPointsForMonth;
	}

	public float getAvgCmpPointsForMonth() {
		return this.avgCmpPointsForMonth;
	}

	public void setAvgCmpPointsForMonth(float avgCmpPointsForMonth) {
		this.avgCmpPointsForMonth = avgCmpPointsForMonth;
	}

	public float getAvgWipRevenue() {
		return this.avgWipRevenue;
	}

	public void setAvgWipRevenue(float avgWipRevenue) {
		this.avgWipRevenue = avgWipRevenue;
	}

	public float getAvgWipRevenueForMonth() {
		return this.avgWipRevenueForMonth;
	}

	public void setAvgWipRevenueForMonth(float avgWipRevenueForMonth) {
		this.avgWipRevenueForMonth = avgWipRevenueForMonth;
	}

	public float getAvgCmpRevenueForMonth() {
		return this.avgCmpRevenueForMonth;
	}

	public void setAvgCmpRevenueForMonth(float avgCmpRevenueForMonth) {
		this.avgCmpRevenueForMonth = avgCmpRevenueForMonth;
	}
}