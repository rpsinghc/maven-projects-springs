package com.worldcheck.atlas.vo.report;

public class TeamJLPSummaryVO {
	private String userId;
	private String userName;
	private float completedJLP;
	private float wipJLP;
	private float totalJLP;
	private String templateName;
	private String templateCreator;
	private int templateId;
	private String sharedWith;
	private String updatedBy;
	private Integer start;
	private Integer limit;
	private String sortColumnName;
	private String sortType;
	private String isDefaultUser;

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateCreator() {
		return this.templateCreator;
	}

	public void setTemplateCreator(String templateCreator) {
		this.templateCreator = templateCreator;
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getSharedWith() {
		return this.sharedWith;
	}

	public void setSharedWith(String sharedWith) {
		this.sharedWith = sharedWith;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public float getCompletedJLP() {
		return this.completedJLP;
	}

	public void setCompletedJLP(float completedJLP) {
		this.completedJLP = completedJLP;
	}

	public float getWipJLP() {
		return this.wipJLP;
	}

	public void setWipJLP(float wipJLP) {
		this.wipJLP = wipJLP;
	}

	public float getTotalJLP() {
		return this.totalJLP;
	}

	public void setTotalJLP(float totalJLP) {
		this.totalJLP = totalJLP;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
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

	public String getIsDefaultUser() {
		return this.isDefaultUser;
	}

	public void setIsDefaultUser(String isDefaultUser) {
		this.isDefaultUser = isDefaultUser;
	}
}