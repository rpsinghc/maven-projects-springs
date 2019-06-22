package com.worldcheck.atlas.cache.vo;

public class ResultSetVO {
	private String target;
	private String[] name;
	private String[] id;
	private String desc;
	private String sqlId;
	private String sqlName;

	public String[] getName() {
		return this.name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public String[] getId() {
		return this.id;
	}

	public void setId(String[] id) {
		this.id = id;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSqlId() {
		return this.sqlId;
	}

	public void setSqlId(String id) {
		this.sqlId = id;
	}

	public String getSqlName() {
		return this.sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
}