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

public class CodeDetailsVO implements Serializable {
	private String[] codes;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CodeDetailsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("codes");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Codes"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "string"));
		typeDesc.addFieldDesc(elemField);
	}

	public CodeDetailsVO() {
	}

	public CodeDetailsVO(String[] codes) {
		this.codes = codes;
	}

	public String[] getCodes() {
		return this.codes;
	}

	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CodeDetailsVO)) {
			return false;
		} else {
			CodeDetailsVO other = (CodeDetailsVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.codes == null && other.getCodes() == null
						|| this.codes != null && Arrays.equals(this.codes, other.getCodes());
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
			if (this.getCodes() != null) {
				for (int i = 0; i < Array.getLength(this.getCodes()); ++i) {
					Object obj = Array.get(this.getCodes(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
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