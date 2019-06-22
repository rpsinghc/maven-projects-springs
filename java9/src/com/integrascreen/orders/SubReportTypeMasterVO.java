package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SubReportTypeMasterVO implements Serializable {
	private String subReportTypeCode;
	private String subReportTypeDescription;
	private int SRStatus;
	private String SRLDefaultREs;
	private String SRLAction;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SubReportTypeMasterVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "SubReportTypeMasterVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("subReportTypeCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubReportTypeCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subReportTypeDescription");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubReportTypeDescription"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("SRStatus");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SRStatus"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("SRLDefaultREs");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SRLDefaultREs"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("SRLAction");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SRLAction"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SubReportTypeMasterVO() {
	}

	public SubReportTypeMasterVO(String subReportTypeCode, String subReportTypeDescription, int SRStatus,
			String SRLDefaultREs, String SRLAction) {
		this.subReportTypeCode = subReportTypeCode;
		this.subReportTypeDescription = subReportTypeDescription;
		this.SRStatus = SRStatus;
		this.SRLDefaultREs = SRLDefaultREs;
		this.SRLAction = SRLAction;
	}

	public String getSubReportTypeCode() {
		return this.subReportTypeCode;
	}

	public void setSubReportTypeCode(String subReportTypeCode) {
		this.subReportTypeCode = subReportTypeCode;
	}

	public String getSubReportTypeDescription() {
		return this.subReportTypeDescription;
	}

	public void setSubReportTypeDescription(String subReportTypeDescription) {
		this.subReportTypeDescription = subReportTypeDescription;
	}

	public int getSRStatus() {
		return this.SRStatus;
	}

	public void setSRStatus(int SRStatus) {
		this.SRStatus = SRStatus;
	}

	public String getSRLDefaultREs() {
		return this.SRLDefaultREs;
	}

	public void setSRLDefaultREs(String SRLDefaultREs) {
		this.SRLDefaultREs = SRLDefaultREs;
	}

	public String getSRLAction() {
		return this.SRLAction;
	}

	public void setSRLAction(String SRLAction) {
		this.SRLAction = SRLAction;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SubReportTypeMasterVO)) {
			return false;
		} else {
			SubReportTypeMasterVO other = (SubReportTypeMasterVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.subReportTypeCode == null && other.getSubReportTypeCode() == null
						|| this.subReportTypeCode != null
								&& this.subReportTypeCode.equals(other.getSubReportTypeCode()))
						&& (this.subReportTypeDescription == null && other.getSubReportTypeDescription() == null
								|| this.subReportTypeDescription != null
										&& this.subReportTypeDescription.equals(other.getSubReportTypeDescription()))
						&& this.SRStatus == other.getSRStatus()
						&& (this.SRLDefaultREs == null && other.getSRLDefaultREs() == null
								|| this.SRLDefaultREs != null && this.SRLDefaultREs.equals(other.getSRLDefaultREs()))
						&& (this.SRLAction == null && other.getSRLAction() == null
								|| this.SRLAction != null && this.SRLAction.equals(other.getSRLAction()));
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
			if (this.getSubReportTypeCode() != null) {
				_hashCode += this.getSubReportTypeCode().hashCode();
			}

			if (this.getSubReportTypeDescription() != null) {
				_hashCode += this.getSubReportTypeDescription().hashCode();
			}

			_hashCode += this.getSRStatus();
			if (this.getSRLDefaultREs() != null) {
				_hashCode += this.getSRLDefaultREs().hashCode();
			}

			if (this.getSRLAction() != null) {
				_hashCode += this.getSRLAction().hashCode();
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