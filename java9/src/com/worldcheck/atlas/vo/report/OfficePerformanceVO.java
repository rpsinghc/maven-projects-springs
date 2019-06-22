package com.worldcheck.atlas.vo.report;

import java.util.List;

public class OfficePerformanceVO {
	private String year;
	private String office;
	private String month;
	private float productivityPoints;
	private float productivitypointsByAnalyst;
	private int completedCases;
	private float completedCasesByAnalyst;
	private float revenue;
	private float revenueByAnalyst;
	private List<OfficePerformanceVO> officePerformanceVOList;

	public List<OfficePerformanceVO> getOfficePerformanceVOList() {
		return this.officePerformanceVOList;
	}

	public void setOfficePerformanceVOList(List<OfficePerformanceVO> officePerformanceVOList) {
		this.officePerformanceVOList = officePerformanceVOList;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public float getProductivityPoints() {
		return this.productivityPoints;
	}

	public void setProductivityPoints(float productivityPoints) {
		this.productivityPoints = productivityPoints;
	}

	public float getProductivitypointsByAnalyst() {
		return this.productivitypointsByAnalyst;
	}

	public void setProductivitypointsByAnalyst(float productivitypointsByAnalyst) {
		this.productivitypointsByAnalyst = productivitypointsByAnalyst;
	}

	public int getCompletedCases() {
		return this.completedCases;
	}

	public void setCompletedCases(int completedCases) {
		this.completedCases = completedCases;
	}

	public float getCompletedCasesByAnalyst() {
		return this.completedCasesByAnalyst;
	}

	public void setCompletedCasesByAnalyst(float completedCasesByAnalyst) {
		this.completedCasesByAnalyst = completedCasesByAnalyst;
	}

	public float getRevenue() {
		return this.revenue;
	}

	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

	public float getRevenueByAnalyst() {
		return this.revenueByAnalyst;
	}

	public void setRevenueByAnalyst(float revenueByAnalyst) {
		this.revenueByAnalyst = revenueByAnalyst;
	}
}