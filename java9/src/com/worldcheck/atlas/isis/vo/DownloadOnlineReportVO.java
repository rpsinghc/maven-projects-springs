package com.worldcheck.atlas.isis.vo;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class DownloadOnlineReportVO implements Serializable {
	private String crn;
	private String fileName;
	private String version;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(DownloadOnlineReportVO.class, true);

	public DownloadOnlineReportVO() {
	}

	public DownloadOnlineReportVO(String crn, String fileName, String version) {
		this.crn = crn;
		this.fileName = fileName;
		this.version = version;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof DownloadOnlineReportVO)) {
			return false;
		} else {
			DownloadOnlineReportVO other = (DownloadOnlineReportVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.crn == null && other.getCrn() == null
						|| this.crn != null && this.crn.equals(other.getCrn()))
						&& (this.fileName == null && other.getFileName() == null
								|| this.fileName != null && this.fileName.equals(other.getFileName()))
						&& (this.version == null && other.getVersion() == null
								|| this.version != null && this.version.equals(other.getVersion()));
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
			if (this.getCrn() != null) {
				_hashCode += this.getCrn().hashCode();
			}

			if (this.getFileName() != null) {
				_hashCode += this.getFileName().hashCode();
			}

			if (this.getVersion() != null) {
				_hashCode += this.getVersion().hashCode();
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
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "DownloadOnlineReportVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("crn");
		elemField.setXmlName(new QName("", "crn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("fileName");
		elemField.setXmlName(new QName("", "fileName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("version");
		elemField.setXmlName(new QName("", "version"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}
}