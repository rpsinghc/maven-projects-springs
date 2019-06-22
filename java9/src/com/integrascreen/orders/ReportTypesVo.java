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

public class ReportTypesVo implements Serializable {
	private String reportTypeCode;
	private SubReportTypesVO[] subjReportType;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ReportTypesVo.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ReportTypesVo"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("reportTypeCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ReportTypeCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjReportType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjReportType"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SubReportTypesVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "SubReportTypesVO"));
		typeDesc.addFieldDesc(elemField);
	}

	public ReportTypesVo() {
	}

	public ReportTypesVo(String reportTypeCode, SubReportTypesVO[] subjReportType) {
		this.reportTypeCode = reportTypeCode;
		this.subjReportType = subjReportType;
	}

	public String getReportTypeCode() {
		return this.reportTypeCode;
	}

	public void setReportTypeCode(String reportTypeCode) {
		this.reportTypeCode = reportTypeCode;
	}

	public SubReportTypesVO[] getSubjReportType() {
		return this.subjReportType;
	}

	public void setSubjReportType(SubReportTypesVO[] subjReportType) {
		this.subjReportType = subjReportType;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ReportTypesVo)) {
			return false;
		} else {
			ReportTypesVo other = (ReportTypesVo) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.reportTypeCode == null && other.getReportTypeCode() == null
						|| this.reportTypeCode != null && this.reportTypeCode.equals(other.getReportTypeCode()))
						&& (this.subjReportType == null && other.getSubjReportType() == null
								|| this.subjReportType != null
										&& Arrays.equals(this.subjReportType, other.getSubjReportType()));
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
			if (this.getReportTypeCode() != null) {
				_hashCode += this.getReportTypeCode().hashCode();
			}

			if (this.getSubjReportType() != null) {
				for (int i = 0; i < Array.getLength(this.getSubjReportType()); ++i) {
					Object obj = Array.get(this.getSubjReportType(), i);
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