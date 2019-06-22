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

public class RiskMapping implements Serializable {
	private int mappingID;
	private ErrorObjects[] mappingError;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskMapping.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskMapping"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("mappingID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "MappingID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("mappingError");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "MappingError"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		typeDesc.addFieldDesc(elemField);
	}

	public RiskMapping() {
	}

	public RiskMapping(int mappingID, ErrorObjects[] mappingError) {
		this.mappingID = mappingID;
		this.mappingError = mappingError;
	}

	public int getMappingID() {
		return this.mappingID;
	}

	public void setMappingID(int mappingID) {
		this.mappingID = mappingID;
	}

	public ErrorObjects[] getMappingError() {
		return this.mappingError;
	}

	public void setMappingError(ErrorObjects[] mappingError) {
		this.mappingError = mappingError;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskMapping)) {
			return false;
		} else {
			RiskMapping other = (RiskMapping) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.mappingID == other.getMappingID()
						&& (this.mappingError == null && other.getMappingError() == null || this.mappingError != null
								&& Arrays.equals(this.mappingError, other.getMappingError()));
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
			int _hashCode = _hashCode + this.getMappingID();
			if (this.getMappingError() != null) {
				for (int i = 0; i < Array.getLength(this.getMappingError()); ++i) {
					Object obj = Array.get(this.getMappingError(), i);
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