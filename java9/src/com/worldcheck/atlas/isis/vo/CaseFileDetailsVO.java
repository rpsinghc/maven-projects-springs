package com.worldcheck.atlas.isis.vo;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CaseFileDetailsVO implements Serializable {
	private String fileName;
	private String path;
	private Calendar creationDateTime;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CaseFileDetailsVO.class, true);

	public CaseFileDetailsVO() {
	}

	public CaseFileDetailsVO(String fileName, String path, Calendar creationDateTime) {
		this.fileName = fileName;
		this.path = path;
		this.creationDateTime = creationDateTime;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Calendar getCreationDateTime() {
		return this.creationDateTime;
	}

	public void setCreationDateTime(Calendar creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CaseFileDetailsVO)) {
			return false;
		} else {
			CaseFileDetailsVO other = (CaseFileDetailsVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.fileName == null && other.getFileName() == null
						|| this.fileName != null && this.fileName.equals(other.getFileName()))
						&& (this.path == null && other.getPath() == null
								|| this.path != null && this.path.equals(other.getPath()))
						&& (this.creationDateTime == null && other.getCreationDateTime() == null
								|| this.creationDateTime != null
										&& this.creationDateTime.equals(other.getCreationDateTime()));
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
			if (this.getFileName() != null) {
				_hashCode += this.getFileName().hashCode();
			}

			if (this.getPath() != null) {
				_hashCode += this.getPath().hashCode();
			}

			if (this.getCreationDateTime() != null) {
				_hashCode += this.getCreationDateTime().hashCode();
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

	static {
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseFileDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("fileName");
		elemField.setXmlName(new QName("", "fileName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("path");
		elemField.setXmlName(new QName("", "path"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("creationDateTime");
		elemField.setXmlName(new QName("", "creationDateTime"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}
}