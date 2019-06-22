package com.worldcheck.atlas.isis.vo;

import com.worldcheck.atlas.isis.ArrayOf_xsd_anyType;
import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CaseResultVO implements Serializable {
	private String crn;
	private String errorCode;
	private String errorMessage;
	private String status;
	private boolean success;
	private ArrayOf_xsd_anyType resultSubjectDetails;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CaseResultVO.class, true);

	public CaseResultVO() {
	}

	public CaseResultVO(String crn, String errorCode, String errorMessage, String status, boolean success,
			ArrayOf_xsd_anyType resultSubjectDetails) {
		this.crn = crn;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
		this.success = success;
		this.resultSubjectDetails = resultSubjectDetails;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ArrayOf_xsd_anyType getResultSubjectDetails() {
		return this.resultSubjectDetails;
	}

	public void setResultSubjectDetails(ArrayOf_xsd_anyType resultSubjectDetails) {
		this.resultSubjectDetails = resultSubjectDetails;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CaseResultVO)) {
			return false;
		} else {
			CaseResultVO other = (CaseResultVO) obj;
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
						&& (this.errorCode == null && other.getErrorCode() == null
								|| this.errorCode != null && this.errorCode.equals(other.getErrorCode()))
						&& (this.errorMessage == null && other.getErrorMessage() == null
								|| this.errorMessage != null && this.errorMessage.equals(other.getErrorMessage()))
						&& (this.status == null && other.getStatus() == null
								|| this.status != null && this.status.equals(other.getStatus()))
						&& this.success == other.isSuccess()
						&& (this.resultSubjectDetails == null && other.getResultSubjectDetails() == null
								|| this.resultSubjectDetails != null
										&& this.resultSubjectDetails.equals(other.getResultSubjectDetails()));
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

			if (this.getErrorCode() != null) {
				_hashCode += this.getErrorCode().hashCode();
			}

			if (this.getErrorMessage() != null) {
				_hashCode += this.getErrorMessage().hashCode();
			}

			if (this.getStatus() != null) {
				_hashCode += this.getStatus().hashCode();
			}

			_hashCode += (this.isSuccess() ? Boolean.TRUE : Boolean.FALSE).hashCode();
			if (this.getResultSubjectDetails() != null) {
				_hashCode += this.getResultSubjectDetails().hashCode();
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
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseResultVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("crn");
		elemField.setXmlName(new QName("", "crn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
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
		elemField.setFieldName("status");
		elemField.setXmlName(new QName("", "status"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("success");
		elemField.setXmlName(new QName("", "success"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("resultSubjectDetails");
		elemField.setXmlName(new QName("", "resultSubjectDetails"));
		elemField.setXmlType(new QName("http://isis.atlas.worldcheck.com", "ArrayOf_xsd_anyType"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}
}