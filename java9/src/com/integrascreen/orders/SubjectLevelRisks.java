package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SubjectLevelRisks implements Serializable {
	private Subjects subjects;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SubjectLevelRisks.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "SubjectLevelRisks"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("subjects");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "subjects"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "Subjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SubjectLevelRisks() {
	}

	public SubjectLevelRisks(Subjects subjects) {
		this.subjects = subjects;
	}

	public Subjects getSubjects() {
		return this.subjects;
	}

	public void setSubjects(Subjects subjects) {
		this.subjects = subjects;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SubjectLevelRisks)) {
			return false;
		} else {
			SubjectLevelRisks other = (SubjectLevelRisks) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.subjects == null && other.getSubjects() == null
						|| this.subjects != null && this.subjects.equals(other.getSubjects());
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
			if (this.getSubjects() != null) {
				_hashCode += this.getSubjects().hashCode();
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