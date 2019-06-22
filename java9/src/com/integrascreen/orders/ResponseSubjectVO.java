package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ResponseSubjectVO implements Serializable {
	private String CRN;
	private String ISISSubjectID;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ResponseSubjectVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ResponseSubjectVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("ISISSubjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ISISSubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public ResponseSubjectVO() {
	}

	public ResponseSubjectVO(String CRN, String ISISSubjectID) {
		this.CRN = CRN;
		this.ISISSubjectID = ISISSubjectID;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public String getISISSubjectID() {
		return this.ISISSubjectID;
	}

	public void setISISSubjectID(String ISISSubjectID) {
		this.ISISSubjectID = ISISSubjectID;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ResponseSubjectVO)) {
			return false;
		} else {
			ResponseSubjectVO other = (ResponseSubjectVO) obj;
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
						&& (this.ISISSubjectID == null && other.getISISSubjectID() == null
								|| this.ISISSubjectID != null && this.ISISSubjectID.equals(other.getISISSubjectID()));
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

			if (this.getISISSubjectID() != null) {
				_hashCode += this.getISISSubjectID().hashCode();
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