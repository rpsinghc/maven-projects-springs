package com.worldcheck.atlas.vo.masters;

import com.worldcheck.atlas.vo.audit.RiskHistory;
import java.util.List;

public class RiskProfileVO {
	private String riskCode;
	private String attributeId;
	private String newattrValue;
	private String industryCode;
	private long isApplied;
	private long countryId;
	private String CRN;
	private String oldAttrValue;
	private long riskCategoryId;
	private long subjectId;
	private String updatedBy;
	private String attributeName;
	private List<RiskHistory> riskHistoryDataList;
	private long riskProfileId;
	private long riskType;
	private long hasCountryBreakDown;
	private long visibleToClient;
	private Integer isRiskMandatory;
	private String riskLabel;

	public String getRiskLabel() {
		return this.riskLabel;
	}

	public void setRiskLabel(String riskLabel) {
		this.riskLabel = riskLabel;
	}

	public Integer getIsRiskMandatory() {
		return this.isRiskMandatory;
	}

	public void setIsRiskMandatory(Integer isRiskMandatory) {
		this.isRiskMandatory = isRiskMandatory;
	}

	public long getVisibleToClient() {
		return this.visibleToClient;
	}

	public void setVisibleToClient(long visibleToClient) {
		this.visibleToClient = visibleToClient;
	}

	public long getHasCountryBreakDown() {
		return this.hasCountryBreakDown;
	}

	public void setHasCountryBreakDown(long hasCountryBreakDown) {
		this.hasCountryBreakDown = hasCountryBreakDown;
	}

	public long getRiskType() {
		return this.riskType;
	}

	public void setRiskType(long riskType) {
		this.riskType = riskType;
	}

	public long getRiskProfileId() {
		return this.riskProfileId;
	}

	public void setRiskProfileId(long riskProfileId) {
		this.riskProfileId = riskProfileId;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public List<RiskHistory> getRiskHistoryDataList() {
		return this.riskHistoryDataList;
	}

	public void setRiskHistoryDataList(List<RiskHistory> riskHistoryDataList) {
		this.riskHistoryDataList = riskHistoryDataList;
	}

	public List<RiskHistory> getRiskHistoryData() {
		return this.riskHistoryDataList;
	}

	public void setRiskHistoryData(List<RiskHistory> riskHistoryData) {
		this.riskHistoryDataList = riskHistoryData;
	}

	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public long getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(long riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public String getOldAttrValue() {
		return this.oldAttrValue;
	}

	public void setOldAttrValue(String oldAttrValue) {
		this.oldAttrValue = oldAttrValue;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public long getCountryId() {
		return this.countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	public String getNewattrValue() {
		return this.newattrValue;
	}

	public void setNewattrValue(String newattrValue) {
		this.newattrValue = newattrValue;
	}

	public String getIndustryCode() {
		return this.industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public long getIsApplied() {
		return this.isApplied;
	}

	public void setIsApplied(long isApplied) {
		this.isApplied = isApplied;
	}
}