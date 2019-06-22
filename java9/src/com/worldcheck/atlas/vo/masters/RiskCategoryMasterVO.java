package com.worldcheck.atlas.vo.masters;

import java.util.List;

public class RiskCategoryMasterVO {
	private String riskCategoryId;
	private String category;
	private String notes;
	private String panelId;
	private List<RisksMasterVO> riskMasterDataList;

	public String getPanelId() {
		return this.panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public String getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(String riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setRiskMasterDataList(List<RisksMasterVO> riskMasterDataList) {
		this.riskMasterDataList = riskMasterDataList;
	}

	public List<RisksMasterVO> getRiskMasterDataList() {
		return this.riskMasterDataList;
	}
}