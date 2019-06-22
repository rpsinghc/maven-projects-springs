package com.worldcheck.atlas.vo.audit;

import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import java.util.List;

public class RiskHistory {
	private long riskHistoryId;
	private String CRN;
	private String riskCode;
	private String oldInfo;
	private String newInfo;
	private String updatedBy;
	private String updatedOn;
	private String task;
	private long riskCategoryId;
	private String action;
	private long attributeId;
	private long countryMasterId;
	private String country;
	private long subjectId;
	private String old_value;
	private String new_value;
	private List<RiskProfileVO> riskHistoryDataList;

	public String getOld_value() {
		return this.old_value;
	}

	public void setOld_value(String oldValue) {
		this.old_value = oldValue;
	}

	public String getNew_value() {
		return this.new_value;
	}

	public void setNew_value(String newValue) {
		this.new_value = newValue;
	}

	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public List<RiskProfileVO> getRiskHistoryDataList() {
		return this.riskHistoryDataList;
	}

	public void setRiskHistoryDataList(List<RiskProfileVO> riskHistoryDataList) {
		this.riskHistoryDataList = riskHistoryDataList;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getCountryMasterId() {
		return this.countryMasterId;
	}

	public void setCountryMasterId(long countryMasterId) {
		this.countryMasterId = countryMasterId;
	}

	public long getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(long riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public long getRiskHistoryId() {
		return this.riskHistoryId;
	}

	public void setRiskHistoryId(long riskHistoryId) {
		this.riskHistoryId = riskHistoryId;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getOldInfo() {
		return this.oldInfo;
	}

	public void setOldInfo(String oldInfo) {
		this.oldInfo = oldInfo;
	}

	public String getNewInfo() {
		return this.newInfo;
	}

	public void setNewInfo(String newInfo) {
		this.newInfo = newInfo;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
}