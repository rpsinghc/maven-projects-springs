package com.worldcheck.atlas.vo.masters;

public class PublicHolidayMasterVO {
	private String officeId;
	private int holidayMasterId;
	private String holidayId;
	private String office;
	private String holiday;
	private String holidayDate;
	private String holidayYear;
	private String updatedBy;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public int getHolidayMasterId() {
		return this.holidayMasterId;
	}

	public void setHolidayMasterId(int holidayMasterId) {
		this.holidayMasterId = holidayMasterId;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getHoliday() {
		return this.holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getHolidayDate() {
		return this.holidayDate;
	}

	public void setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getHolidayYear() {
		return this.holidayYear;
	}

	public void setHolidayYear(String holidayYear) {
		this.holidayYear = holidayYear;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getHolidayId() {
		return this.holidayId;
	}

	public void setHolidayId(String holidayId) {
		this.holidayId = holidayId;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
}