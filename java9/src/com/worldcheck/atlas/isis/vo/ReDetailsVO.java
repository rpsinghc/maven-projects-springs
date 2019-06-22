package com.worldcheck.atlas.isis.vo;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ReDetailsVO implements Serializable {
	private int reId;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ReDetailsVO.class, true);

	public ReDetailsVO() {
	}

	public ReDetailsVO(int reId) {
		this.reId = reId;
	}

	public int getReId() {
		return this.reId;
	}

	public void setReId(int reId) {
		this.reId = reId;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ReDetailsVO)) {
			return false;
		} else {
			ReDetailsVO other = (ReDetailsVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.reId == other.getReId();
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
			int _hashCode = _hashCode + this.getReId();
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
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "ReDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("reId");
		elemField.setXmlName(new QName("", "reId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}
}