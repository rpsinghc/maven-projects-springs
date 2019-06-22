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

public class Subjects implements Serializable {
	private RiskAggregation riskAggregation;
	private Subject[] subject;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(Subjects.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "Subjects"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("riskAggregation");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subject");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Subject"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "Subject"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "Subject"));
		typeDesc.addFieldDesc(elemField);
	}

	public Subjects() {
	}

	public Subjects(RiskAggregation riskAggregation, Subject[] subject) {
		this.riskAggregation = riskAggregation;
		this.subject = subject;
	}

	public RiskAggregation getRiskAggregation() {
		return this.riskAggregation;
	}

	public void setRiskAggregation(RiskAggregation riskAggregation) {
		this.riskAggregation = riskAggregation;
	}

	public Subject[] getSubject() {
		return this.subject;
	}

	public void setSubject(Subject[] subject) {
		this.subject = subject;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof Subjects)) {
			return false;
		} else {
			Subjects other = (Subjects) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.riskAggregation == null && other.getRiskAggregation() == null
						|| this.riskAggregation != null && this.riskAggregation.equals(other.getRiskAggregation()))
						&& (this.subject == null && other.getSubject() == null
								|| this.subject != null && Arrays.equals(this.subject, other.getSubject()));
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
			if (this.getRiskAggregation() != null) {
				_hashCode += this.getRiskAggregation().hashCode();
			}

			if (this.getSubject() != null) {
				for (int i = 0; i < Array.getLength(this.getSubject()); ++i) {
					Object obj = Array.get(this.getSubject(), i);
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