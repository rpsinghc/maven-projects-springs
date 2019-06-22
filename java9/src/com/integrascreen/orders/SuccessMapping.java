package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SuccessMapping implements Serializable {
	private String successCode;
	private String successMessage;
	private CodeDetailsVO mapping;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SuccessMapping.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "SuccessMapping"));
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
		elemField.setFieldName("mapping");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Mapping"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SuccessMapping() {
	}

	public SuccessMapping(String successCode, String successMessage, CodeDetailsVO mapping) {
		this.successCode = successCode;
		this.successMessage = successMessage;
		this.mapping = mapping;
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

	public CodeDetailsVO getMapping() {
		return this.mapping;
	}

	public void setMapping(CodeDetailsVO mapping) {
		this.mapping = mapping;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SuccessMapping)) {
			return false;
		} else {
			SuccessMapping other = (SuccessMapping) obj;
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
						&& (this.mapping == null && other.getMapping() == null
								|| this.mapping != null && this.mapping.equals(other.getMapping()));
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

			if (this.getMapping() != null) {
				_hashCode += this.getMapping().hashCode();
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