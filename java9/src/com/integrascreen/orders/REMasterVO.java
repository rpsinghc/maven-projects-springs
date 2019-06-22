package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class REMasterVO implements Serializable {
	private String code;
	private String description;
	private String subjectType;
	private String status;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(REMasterVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "REMasterVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("code");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Code"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Description"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("status");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Status"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public REMasterVO() {
	}

	public REMasterVO(String code, String description, String subjectType, String status) {
		this.code = code;
		this.description = description;
		this.subjectType = subjectType;
		this.status = status;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof REMasterVO)) {
			return false;
		} else {
			REMasterVO other = (REMasterVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.code == null && other.getCode() == null
						|| this.code != null && this.code.equals(other.getCode()))
						&& (this.description == null && other.getDescription() == null
								|| this.description != null && this.description.equals(other.getDescription()))
						&& (this.subjectType == null && other.getSubjectType() == null
								|| this.subjectType != null && this.subjectType.equals(other.getSubjectType()))
						&& (this.status == null && other.getStatus() == null
								|| this.status != null && this.status.equals(other.getStatus()));
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
			if (this.getCode() != null) {
				_hashCode += this.getCode().hashCode();
			}

			if (this.getDescription() != null) {
				_hashCode += this.getDescription().hashCode();
			}

			if (this.getSubjectType() != null) {
				_hashCode += this.getSubjectType().hashCode();
			}

			if (this.getStatus() != null) {
				_hashCode += this.getStatus().hashCode();
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