package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SubReportTypesVO implements Serializable {
	private String subjReportTypeCode;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SubReportTypesVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "SubReportTypesVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("subjReportTypeCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjReportTypeCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SubReportTypesVO() {
	}

	public SubReportTypesVO(String subjReportTypeCode) {
		this.subjReportTypeCode = subjReportTypeCode;
	}

	public String getSubjReportTypeCode() {
		return this.subjReportTypeCode;
	}

	public void setSubjReportTypeCode(String subjReportTypeCode) {
		this.subjReportTypeCode = subjReportTypeCode;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SubReportTypesVO)) {
			return false;
		} else {
			SubReportTypesVO other = (SubReportTypesVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.subjReportTypeCode == null && other.getSubjReportTypeCode() == null
						|| this.subjReportTypeCode != null
								&& this.subjReportTypeCode.equals(other.getSubjReportTypeCode());
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
			if (this.getSubjReportTypeCode() != null) {
				_hashCode += this.getSubjReportTypeCode().hashCode();
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