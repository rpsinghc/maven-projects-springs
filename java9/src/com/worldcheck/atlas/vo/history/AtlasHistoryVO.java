package com.worldcheck.atlas.vo.history;

public class AtlasHistoryVO {
	private int minRecord;
	private int maxRecord;
	private Integer limit;
	private Integer start;
	private long historyId;
	private String tableName;
	private String data;
	private String action;
	private String newInfo;
	private String oldInfo;
	private String updatedBy;
	private String updatedOn;
	private String historyKey;
	private String SortColumnName;
	private String SortType;

	public long getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(long historyId) {
		this.historyId = historyId;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getSortColumnName() {
		return this.SortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.SortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.SortType;
	}

	public void setSortType(String sortType) {
		this.SortType = sortType;
	}

	public String getNewInfo() {
		return this.newInfo;
	}

	public void setNewInfo(String newInfo) {
		this.newInfo = newInfo;
	}

	public String getOldInfo() {
		return this.oldInfo;
	}

	public void setOldInfo(String oldInfo) {
		this.oldInfo = oldInfo;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getHistoryKey() {
		return this.historyKey;
	}

	public void setHistoryKey(String historyKey) {
		this.historyKey = historyKey;
	}

	public int getMinRecord() {
		return this.minRecord;
	}

	public void setMinRecord(int minRecord) {
		this.minRecord = minRecord;
	}

	public int getMaxRecord() {
		return this.maxRecord;
	}

	public void setMaxRecord(int maxRecord) {
		this.maxRecord = maxRecord;
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

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}