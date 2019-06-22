package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SuccessRisk implements Serializable {
	private String successCode;
	private String successMessage;
	private CodeDetailsVO riskCode;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SuccessRisk.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "SuccessRisk"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("successCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SuccessCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("successMessage");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SuccessMessage"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskCode"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SuccessRisk() {
	}

	public SuccessRisk(String successCode, String successMessage, CodeDetailsVO riskCode) {
		this.successCode = successCode;
		this.successMessage = successMessage;
		this.riskCode = riskCode;
	}

	public String getSuccessCode() {
		return this.successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public String getSuccessMessage() {
		return this.successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public CodeDetailsVO getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(CodeDetailsVO riskCode) {
		this.riskCode = riskCode;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SuccessRisk)) {
			return false;
		} else {
			SuccessRisk other = (SuccessRisk) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.successCode == null && other.getSuccessCode() == null
						|| this.successCode != null && this.successCode.equals(other.getSuccessCode()))
						&& (this.successMessage == null && other.getSuccessMessage() == null
								|| this.successMessage != null && this.successMessage.equals(other.getSuccessMessage()))
						&& (this.riskCode == null && other.getRiskCode() == null
								|| this.riskCode != null && this.riskCode.equals(other.getRiskCode()));
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
			if (this.getSuccessCode() != null) {
				_hashCode += this.getSuccessCode().hashCode();
			}

			if (this.getSuccessMessage() != null) {
				_hashCode += this.getSuccessMessage().hashCode();
			}

			if (this.getRiskCode() != null) {
				_hashCode += this.getRiskCode().hashCode();
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