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

public class CaseLevelRisks implements Serializable {
	private RiskAggregation riskAggregation;
	private RiskCategory[] riskCatagories;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CaseLevelRisks.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "CaseLevelRisks"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("riskAggregation");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskCatagories");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskCatagories"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskCategory"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "RiskCategory"));
		typeDesc.addFieldDesc(elemField);
	}

	public CaseLevelRisks() {
	}

	public CaseLevelRisks(RiskAggregation riskAggregation, RiskCategory[] riskCatagories) {
		this.riskAggregation = riskAggregation;
		this.riskCatagories = riskCatagories;
	}

	public RiskAggregation getRiskAggregation() {
		return this.riskAggregation;
	}

	public void setRiskAggregation(RiskAggregation riskAggregation) {
		this.riskAggregation = riskAggregation;
	}

	public RiskCategory[] getRiskCatagories() {
		return this.riskCatagories;
	}

	public void setRiskCatagories(RiskCategory[] riskCatagories) {
		this.riskCatagories = riskCatagories;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CaseLevelRisks)) {
			return false;
		} else {
			CaseLevelRisks other = (CaseLevelRisks) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.riskAggregation == null && other.getRiskAggregation() == null
						|| this.riskAggregation != null && this.riskAggregation.equals(other.getRiskAggregation()))
						&& (this.riskCatagories == null && other.getRiskCatagories() == null
								|| this.riskCatagories != null
										&& Arrays.equals(this.riskCatagories, other.getRiskCatagories()));
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
			if (this.getRiskAggregation() != null) {
				_hashCode += this.getRiskAggregation().hashCode();
			}

			if (this.getRiskCatagories() != null) {
				for (int i = 0; i < Array.getLength(this.getRiskCatagories()); ++i) {
					Object obj = Array.get(this.getRiskCatagories(), i);
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