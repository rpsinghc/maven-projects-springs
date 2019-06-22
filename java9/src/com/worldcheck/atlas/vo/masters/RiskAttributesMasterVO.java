package com.worldcheck.atlas.vo.masters;

public class RiskAttributesMasterVO {
	private int category_Id;
	private int attribute_Id;
	private String attribute_name;
	private String value;
	private String notes;

	public int getCategory_Id() {
		return this.category_Id;
	}

	public void setCategory_Id(int categoryId) {
		this.category_Id = categoryId;
	}

	public int getAttribute_Id() {
		return this.attribute_Id;
	}

	public void setAttribute_Id(int attributeId) {
		this.attribute_Id = attributeId;
	}

	public String getAttribute_name() {
		return this.attribute_name;
	}

	public void setAttribute_name(String attributeName) {
		this.attribute_name = attributeName;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}