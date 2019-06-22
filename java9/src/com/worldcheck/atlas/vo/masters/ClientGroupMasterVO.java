package com.worldcheck.atlas.vo.masters;

public class ClientGroupMasterVO {
	private String clientGroupId;
	private String statusval;
	private String clientGroupName;
	private String updateDate;
	private String updateBy;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;

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

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getClientGroupName() {
		return this.clientGroupName;
	}

	public void setClientGroupName(String clientGroupName) {
		this.clientGroupName = clientGroupName;
	}

	public String getClientGroupId() {
		return this.clientGroupId;
	}

	public void setClientGroupId(String clientGroupId) {
		this.clientGroupId = clientGroupId;
	}

	public String getStatusval() {
		return this.statusval;
	}

	public void setStatusval(String statusval) {
		this.statusval = statusval;
	}
}