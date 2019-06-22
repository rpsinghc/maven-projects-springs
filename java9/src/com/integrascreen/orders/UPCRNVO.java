package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class UPCRNVO implements Serializable {
	private String CRN;
	private String clientRefNumber;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(UPCRNVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "UPCRNVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clientRefNumber");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ClientRefNumber"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public UPCRNVO() {
	}

	public UPCRNVO(String CRN, String clientRefNumber) {
		this.CRN = CRN;
		this.clientRefNumber = clientRefNumber;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public String getClientRefNumber() {
		return this.clientRefNumber;
	}

	public void setClientRefNumber(String clientRefNumber) {
		this.clientRefNumber = clientRefNumber;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof UPCRNVO)) {
			return false;
		} else {
			UPCRNVO other = (UPCRNVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.CRN == null && other.getCRN() == null
						|| this.CRN != null && this.CRN.equals(other.getCRN()))
						&& (this.clientRefNumber == null && other.getClientRefNumber() == null
								|| this.clientRefNumber != null
										&& this.clientRefNumber.equals(other.getClientRefNumber()));
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
			if (this.getCRN() != null) {
				_hashCode += this.getCRN().hashCode();
			}

			if (this.getClientRefNumber() != null) {
				_hashCode += this.getClientRefNumber().hashCode();
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