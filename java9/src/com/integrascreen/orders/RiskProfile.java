package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class RiskProfile implements Serializable {
	private int CRNHasRiskData;
	private CRNRiskData CRNRiskData;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskProfile.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskProfile"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRNHasRiskData");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRNHasRiskData"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("CRNRiskData");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRNRiskData"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CRNRiskData"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public RiskProfile() {
	}

	public RiskProfile(int CRNHasRiskData, CRNRiskData CRNRiskData) {
		this.CRNHasRiskData = CRNHasRiskData;
		this.CRNRiskData = CRNRiskData;
	}

	public int getCRNHasRiskData() {
		return this.CRNHasRiskData;
	}

	public void setCRNHasRiskData(int CRNHasRiskData) {
		this.CRNHasRiskData = CRNHasRiskData;
	}

	public CRNRiskData getCRNRiskData() {
		return this.CRNRiskData;
	}

	public void setCRNRiskData(CRNRiskData CRNRiskData) {
		this.CRNRiskData = CRNRiskData;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskProfile)) {
			return false;
		} else {
			RiskProfile other = (RiskProfile) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.CRNHasRiskData == other.getCRNHasRiskData()
						&& (this.CRNRiskData == null && other.getCRNRiskData() == null
								|| this.CRNRiskData != null && this.CRNRiskData.equals(other.getCRNRiskData()));
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
			int _hashCode = _hashCode + this.getCRNHasRiskData();
			if (this.getCRNRiskData() != null) {
				_hashCode += this.getCRNRiskData().hashCode();
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