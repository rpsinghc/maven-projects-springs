package com.worldcheck.atlas.vo.masters;

public class IndustryMasterVO {
	private int industryId;
	private String industry;
	private String status;
	private String industryCode;
	private String prePendIndustryCode;
	private String updatedBy;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private String totalCRN;
	private String subjectCount;

	public int getIndustryId() {
		return this.industryId;
	}

	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIndustryCode() {
		return this.industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getPrePendIndustryCode() {
		return this.prePendIndustryCode;
	}

	public void setPrePendIndustryCode(String prePendIndustryCode) {
		this.prePendIndustryCode = prePendIndustryCode;
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

	public String getTotalCRN() {
		return this.totalCRN;
	}

	public void setTotalCRN(String totalCRN) {
		this.totalCRN = totalCRN;
	}

	public String getSubjectCount() {
		return this.subjectCount;
	}

	public void setSubjectCount(String subjectCount) {
		this.subjectCount = subjectCount;
	}
}