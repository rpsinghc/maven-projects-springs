package com.worldcheck.atlas.vo.task;

import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import java.util.List;

public class SubjectListForUserVO {
	private String industryCode;
	private String subId;
	private String countryName;
	private String position;
	private String entityName;
	private String address;
	private String details;
	private String rName;
	private Integer limit;
	private Integer start;
	private String subReportType;
	private List<RiskCategoryMasterVO> categoryMasterDataList;

	public String getIndustryCode() {
		return this.industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getSubId() {
		return this.subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getrName() {
		return this.rName;
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

	public void setrName(String rName) {
		this.rName = rName;
	}

	public void setCategoryMasterDataList(List<RiskCategoryMasterVO> categoryMasterDataList) {
		this.categoryMasterDataList = categoryMasterDataList;
	}

	public List<RiskCategoryMasterVO> getCategoryMasterDataList() {
		return this.categoryMasterDataList;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public String getSubReportType() {
		return this.subReportType;
	}
}