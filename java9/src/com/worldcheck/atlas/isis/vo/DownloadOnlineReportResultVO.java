package com.worldcheck.atlas.isis.vo;

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

public class DownloadOnlineReportResultVO implements Serializable {
	private String errorCode;
	private String errorMessage;
	private Boolean success;
	private byte[] fileContent;
	private String crn;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(DownloadOnlineReportResultVO.class, true);

	public DownloadOnlineReportResultVO() {
	}

	public DownloadOnlineReportResultVO(String errorCode, String errorMessage, Boolean success, byte[] fileContent,
			String crn) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.success = success;
		this.fileContent = fileContent;
		this.crn = crn;
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

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public byte[] getFileContent() {
		return this.fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof DownloadOnlineReportResultVO)) {
			return false;
		} else {
			DownloadOnlineReportResultVO other = (DownloadOnlineReportResultVO) obj;
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
						&& (this.success == null && other.getSuccess() == null
								|| this.success != null && this.success.equals(other.getSuccess()))
						&& (this.fileContent == null && other.getFileContent() == null
								|| this.fileContent != null && Arrays.equals(this.fileContent, other.getFileContent()))
						&& (this.crn == null && other.getCrn() == null
								|| this.crn != null && this.crn.equals(other.getCrn()));
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

			if (this.getSuccess() != null) {
				_hashCode += this.getSuccess().hashCode();
			}

			if (this.getFileContent() != null) {
				for (int i = 0; i < Array.getLength(this.getFileContent()); ++i) {
					Object obj = Array.get(this.getFileContent(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getCrn() != null) {
				_hashCode += this.getCrn().hashCode();
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
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "DownloadOnlineReportResultVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("errorCode");
		elemField.setXmlName(new QName("", "errorCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("errorMessage");
		elemField.setXmlName(new QName("", "errorMessage"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("success");
		elemField.setXmlName(new QName("", "success"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("fileContent");
		elemField.setXmlName(new QName("", "fileContent"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("crn");
		elemField.setXmlName(new QName("", "crn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}
}