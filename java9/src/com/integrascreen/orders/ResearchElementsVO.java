package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ResearchElementsVO implements Serializable {
	private int REID;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ResearchElementsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ResearchElementsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("REID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "REID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public ResearchElementsVO() {
	}

	public ResearchElementsVO(int REID) {
		this.REID = REID;
	}

	public int getREID() {
		return this.REID;
	}

	public void setREID(int REID) {
		this.REID = REID;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ResearchElementsVO)) {
			return false;
		} else {
			ResearchElementsVO other = (ResearchElementsVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.REID == other.getREID();
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
			int _hashCode = _hashCode + this.getREID();
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