package com.worldcheck.atlas.isis;

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

public class ArrayOf_xsd_anyType implements Serializable {
	private Object[] item;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ArrayOf_xsd_anyType.class, true);

	public ArrayOf_xsd_anyType() {
	}

	public ArrayOf_xsd_anyType(Object[] item) {
		this.item = item;
	}

	public Object[] getItem() {
		return this.item;
	}

	public void setItem(Object[] item) {
		this.item = item;
	}

	public Object getItem(int i) {
		return this.item[i];
	}

	public void setItem(int i, Object _value) {
		this.item[i] = _value;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ArrayOf_xsd_anyType)) {
			return false;
		} else {
			ArrayOf_xsd_anyType other = (ArrayOf_xsd_anyType) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.item == null && other.getItem() == null
						|| this.item != null && Arrays.equals(this.item, other.getItem());
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
			if (this.getItem() != null) {
				for (int i = 0; i < Array.getLength(this.getItem()); ++i) {
					Object obj = Array.get(this.getItem(), i);
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

	static {
		typeDesc.setXmlType(new QName("http://isis.atlas.worldcheck.com", "ArrayOf_xsd_anyType"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("item");
		elemField.setXmlName(new QName("", "item"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "anyType"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
		typeDesc.addFieldDesc(elemField);
	}
}