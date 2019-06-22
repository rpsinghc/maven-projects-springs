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

public class ResponseDetailsVO implements Serializable {
	private RiskSuccessVO successObject;
	private RiskErrorVO[] errorObject;
	private ErrorDetailObjects errorDetailObjects;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ResponseDetailsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ResponseDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("successObject");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SuccessObject"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskSuccessVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("errorObject");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorObject"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskErrorVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "RiskErrorVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("errorDetailObjects");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorDetailObjects"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorDetailObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public ResponseDetailsVO() {
	}

	public ResponseDetailsVO(RiskSuccessVO successObject, RiskErrorVO[] errorObject,
			ErrorDetailObjects errorDetailObjects) {
		this.successObject = successObject;
		this.errorObject = errorObject;
		this.errorDetailObjects = errorDetailObjects;
	}

	public RiskSuccessVO getSuccessObject() {
		return this.successObject;
	}

	public void setSuccessObject(RiskSuccessVO successObject) {
		this.successObject = successObject;
	}

	public RiskErrorVO[] getErrorObject() {
		return this.errorObject;
	}

	public void setErrorObject(RiskErrorVO[] errorObject) {
		this.errorObject = errorObject;
	}

	public ErrorDetailObjects getErrorDetailObjects() {
		return this.errorDetailObjects;
	}

	public void setErrorDetailObjects(ErrorDetailObjects errorDetailObjects) {
		this.errorDetailObjects = errorDetailObjects;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ResponseDetailsVO)) {
			return false;
		} else {
			ResponseDetailsVO other = (ResponseDetailsVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.successObject == null && other.getSuccessObject() == null
						|| this.successObject != null && this.successObject.equals(other.getSuccessObject()))
						&& (this.errorObject == null && other.getErrorObject() == null
								|| this.errorObject != null && Arrays.equals(this.errorObject, other.getErrorObject()))
						&& (this.errorDetailObjects == null && other.getErrorDetailObjects() == null
								|| this.errorDetailObjects != null
										&& this.errorDetailObjects.equals(other.getErrorDetailObjects()));
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
			if (this.getSuccessObject() != null) {
				_hashCode += this.getSuccessObject().hashCode();
			}

			if (this.getErrorObject() != null) {
				for (int i = 0; i < Array.getLength(this.getErrorObject()); ++i) {
					Object obj = Array.get(this.getErrorObject(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getErrorDetailObjects() != null) {
				_hashCode += this.getErrorDetailObjects().hashCode();
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