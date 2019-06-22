package com.worldcheck.atlas.vo.masters;

public class CategoryDetailsVO {
	private long riskCategoryId;
	private long attributeId;
	private String attributeName;
	private String attributeDefaultValue;
	private String attributeValue;

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public long getRiskCategoryId() {
		return this.riskCategoryId;
	}

	public void setRiskCategoryId(long riskCategoryId) {
		this.riskCategoryId = riskCategoryId;
	}

	public long getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeDefaultValue() {
		return this.attributeDefaultValue;
	}

	public void setAttributeDefaultValue(String attributeDefaultValue) {
		this.attributeDefaultValue = attributeDefaultValue;
	}
}