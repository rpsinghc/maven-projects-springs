package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ResponseVO implements Serializable {
	private String errorCode;
	private String message;
	private ResponseSubjectVO responseSubjectVO;
	private ResponseDetailsVO responseDetailsVo;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ResponseVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ResponseVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("errorCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("message");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Message"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("responseSubjectVO");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ResponseSubjectVO"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ResponseSubjectVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("responseDetailsVo");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ResponseDetailsVo"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ResponseDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public ResponseVO() {
	}

	public ResponseVO(String errorCode, String message, ResponseSubjectVO responseSubjectVO,
			ResponseDetailsVO responseDetailsVo) {
		this.errorCode = errorCode;
		this.message = message;
		this.responseSubjectVO = responseSubjectVO;
		this.responseDetailsVo = responseDetailsVo;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseSubjectVO getResponseSubjectVO() {
		return this.responseSubjectVO;
	}

	public void setResponseSubjectVO(ResponseSubjectVO responseSubjectVO) {
		this.responseSubjectVO = responseSubjectVO;
	}

	public ResponseDetailsVO getResponseDetailsVo() {
		return this.responseDetailsVo;
	}

	public void setResponseDetailsVo(ResponseDetailsVO responseDetailsVo) {
		this.responseDetailsVo = responseDetailsVo;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ResponseVO)) {
			return false;
		} else {
			ResponseVO other = (ResponseVO) obj;
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
						&& (this.message == null && other.getMessage() == null
								|| this.message != null && this.message.equals(other.getMessage()))
						&& (this.responseSubjectVO == null && other.getResponseSubjectVO() == null
								|| this.responseSubjectVO != null
										&& this.responseSubjectVO.equals(other.getResponseSubjectVO()))
						&& (this.responseDetailsVo == null && other.getResponseDetailsVo() == null
								|| this.responseDetailsVo != null
										&& this.responseDetailsVo.equals(other.getResponseDetailsVo()));
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

			if (this.getMessage() != null) {
				_hashCode += this.getMessage().hashCode();
			}

			if (this.getResponseSubjectVO() != null) {
				_hashCode += this.getResponseSubjectVO().hashCode();
			}

			if (this.getResponseDetailsVo() != null) {
				_hashCode += this.getResponseDetailsVo().hashCode();
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