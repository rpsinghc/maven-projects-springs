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

public class Country implements Serializable {
	private String country;
	private Attribute[] attributes;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(Country.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "Country"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("country");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Country"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("attributes");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Attributes"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "Attribute"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "Attribute"));
		typeDesc.addFieldDesc(elemField);
	}

	public Country() {
	}

	public Country(String country, Attribute[] attributes) {
		this.country = country;
		this.attributes = attributes;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Attribute[] getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof Country)) {
			return false;
		} else {
			Country other = (Country) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.country == null && other.getCountry() == null
						|| this.country != null && this.country.equals(other.getCountry()))
						&& (this.attributes == null && other.getAttributes() == null
								|| this.attributes != null && Arrays.equals(this.attributes, other.getAttributes()));
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
			if (this.getCountry() != null) {
				_hashCode += this.getCountry().hashCode();
			}

			if (this.getAttributes() != null) {
				for (int i = 0; i < Array.getLength(this.getAttributes()); ++i) {
					Object obj = Array.get(this.getAttributes(), i);
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