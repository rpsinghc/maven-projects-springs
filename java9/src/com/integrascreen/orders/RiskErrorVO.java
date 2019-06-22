package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class RiskErrorVO implements Serializable {
	private String errorCode;
	private String errorMessage;
	private CodeDetailsVO risks;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskErrorVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskErrorVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("errorCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("errorMessage");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorMessage"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("risks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Risks"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public RiskErrorVO() {
	}

	public RiskErrorVO(String errorCode, String errorMessage, CodeDetailsVO risks) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.risks = risks;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public CodeDetailsVO getRisks() {
		return this.risks;
	}

	public void setRisks(CodeDetailsVO risks) {
		this.risks = risks;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskErrorVO)) {
			return false;
		} else {
			RiskErrorVO other = (RiskErrorVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.errorCode == null && other.getErrorCode() == null
						|| this.errorCode != null && this.errorCode.equals(other.getErrorCode()))
						&& (this.errorMessage == null && other.getErrorMessage() == null
								|| this.errorMessage != null && this.errorMessage.equals(other.getErrorMessage()))
						&& (this.risks == null && other.getRisks() == null
								|| this.risks != null && this.risks.equals(other.getRisks()));
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
			if (this.getErrorCode() != null) {
				_hashCode += this.getErrorCode().hashCode();
			}

			if (this.getErrorMessage() != null) {
				_hashCode += this.getErrorMessage().hashCode();
			}

			if (this.getRisks() != null) {
				_hashCode += this.getRisks().hashCode();
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