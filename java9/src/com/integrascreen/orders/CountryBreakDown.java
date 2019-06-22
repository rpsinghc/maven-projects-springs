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

public class CountryBreakDown implements Serializable {
	private RiskAggregation riskAggregation;
	private Country[] countries;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CountryBreakDown.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "CountryBreakDown"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("riskAggregation");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("countries");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Countries"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "Country"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "Country"));
		typeDesc.addFieldDesc(elemField);
	}

	public CountryBreakDown() {
	}

	public CountryBreakDown(RiskAggregation riskAggregation, Country[] countries) {
		this.riskAggregation = riskAggregation;
		this.countries = countries;
	}

	public RiskAggregation getRiskAggregation() {
		return this.riskAggregation;
	}

	public void setRiskAggregation(RiskAggregation riskAggregation) {
		this.riskAggregation = riskAggregation;
	}

	public Country[] getCountries() {
		return this.countries;
	}

	public void setCountries(Country[] countries) {
		this.countries = countries;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CountryBreakDown)) {
			return false;
		} else {
			CountryBreakDown other = (CountryBreakDown) obj;
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
						&& (this.countries == null && other.getCountries() == null
								|| this.countries != null && Arrays.equals(this.countries, other.getCountries()));
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

			if (this.getCountries() != null) {
				for (int i = 0; i < Array.getLength(this.getCountries()); ++i) {
					Object obj = Array.get(this.getCountries(), i);
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