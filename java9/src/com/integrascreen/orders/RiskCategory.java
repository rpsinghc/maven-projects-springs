package com.integrascreen.orders;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class RiskCategory implements Serializable {
	private int categoryID;
	private String categoryLabel;
	private RiskAggregation riskAggregation;
	private Risk[] risks;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskCategory.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskCategory"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("categoryID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CategoryID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("categoryLabel");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CategoryLabel"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskAggregation");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("risks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Risks"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "Risk"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "Risk"));
		typeDesc.addFieldDesc(elemField);
	}

	public RiskCategory() {
	}

	public RiskCategory(int categoryID, String categoryLabel, RiskAggregation riskAggregation, Risk[] risks) {
		this.categoryID = categoryID;
		this.categoryLabel = categoryLabel;
		this.riskAggregation = riskAggregation;
		this.risks = risks;
	}

	public int getCategoryID() {
		return this.categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryLabel() {
		return this.categoryLabel;
	}

	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	public RiskAggregation getRiskAggregation() {
		return this.riskAggregation;
	}

	public void setRiskAggregation(RiskAggregation riskAggregation) {
		this.riskAggregation = riskAggregation;
	}

	public Risk[] getRisks() {
		return this.risks;
	}

	public void setRisks(Risk[] risks) {
		this.risks = risks;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskCategory)) {
			return false;
		} else {
			RiskCategory other = (RiskCategory) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.categoryID == other.getCategoryID()
						&& (this.categoryLabel == null && other.getCategoryLabel() == null
								|| this.categoryLabel != null && this.categoryLabel.equals(other.getCategoryLabel()))
						&& (this.riskAggregation == null && other.getRiskAggregation() == null
								|| this.riskAggregation != null
										&& this.riskAggregation.equals(other.getRiskAggregation()))
						&& (this.risks == null && other.getRisks() == null
								|| this.risks != null && Arrays.equals(this.risks, other.getRisks()));
				this.__equalsCalc = null;
				return _equals;
			}
		}
	}

	public synchronized int hashCode() {
		if (this.__hashCodeCalc) {
			return 0;
		} else {
			this.__hashCodeCalc = true;
			int _hashCode = 1;
			int _hashCode = _hashCode + this.getCategoryID();
			if (this.getCategoryLabel() != null) {
				_hashCode += this.getCategoryLabel().hashCode();
			}

			if (this.getRiskAggregation() != null) {
				_hashCode += this.getRiskAggregation().hashCode();
			}

			if (this.getRisks() != null) {
				for (int i = 0; i < Array.getLength(this.getRisks()); ++i) {
					Object obj = Array.get(this.getRisks(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			this.__hashCodeCalc = false;
			return _hashCode;
		}
	}

	public static TypeDesc getTypeDesc() {
		return typeDesc;
	}

	public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanDeserializer(_javaType, _xmlType, typeDesc);
	}
}