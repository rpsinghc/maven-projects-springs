package com.worldcheck.atlas.vo.masters;

public class RiskAttributeVO {
	private long attributeId;
	private String attributeName;
	private String attributeType;
	private String attributeValue;
	private String riskAttributeValue;

	public String getRiskAttributeValue() {
		return this.riskAttributeValue;
	}

	public void setRiskAttributeValue(String riskAttributeValue) {
		if (riskAttributeValue == null) {
			this.riskAttributeValue = "";
		} else {
			this.riskAttributeValue = riskAttributeValue;
		}

	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return this.attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public long getAttributeId() {
		return this.attributeId;
	}
}