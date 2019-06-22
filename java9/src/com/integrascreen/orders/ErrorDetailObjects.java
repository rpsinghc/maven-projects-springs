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

public class ErrorDetailObjects implements Serializable {
	private String CRN;
	private String ORDERID;
	private ErrorObjects category;
	private ErrorObjects risk;
	private ErrorObjects order;
	private ErrorObjects attribute;
	private ErrorObjects[] risks;
	private RiskMapping[] riskMappings;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ErrorDetailObjects.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ErrorDetailObjects"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("ORDERID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ORDERID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("category");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Category"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("risk");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Risk"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("order");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Order"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("attribute");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Attribute"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("risks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Risks"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskMappings");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskMappings"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskMapping"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "RiskMapping"));
		typeDesc.addFieldDesc(elemField);
	}

	public ErrorDetailObjects() {
	}

	public ErrorDetailObjects(String CRN, String ORDERID, ErrorObjects category, ErrorObjects risk, ErrorObjects order,
			ErrorObjects attribute, ErrorObjects[] risks, RiskMapping[] riskMappings) {
		this.CRN = CRN;
		this.ORDERID = ORDERID;
		this.category = category;
		this.risk = risk;
		this.order = order;
		this.attribute = attribute;
		this.risks = risks;
		this.riskMappings = riskMappings;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public String getORDERID() {
		return this.ORDERID;
	}

	public void setORDERID(String ORDERID) {
		this.ORDERID = ORDERID;
	}

	public ErrorObjects getCategory() {
		return this.category;
	}

	public void setCategory(ErrorObjects category) {
		this.category = category;
	}

	public ErrorObjects getRisk() {
		return this.risk;
	}

	public void setRisk(ErrorObjects risk) {
		this.risk = risk;
	}

	public ErrorObjects getOrder() {
		return this.order;
	}

	public void setOrder(ErrorObjects order) {
		this.order = order;
	}

	public ErrorObjects getAttribute() {
		return this.attribute;
	}

	public void setAttribute(ErrorObjects attribute) {
		this.attribute = attribute;
	}

	public ErrorObjects[] getRisks() {
		return this.risks;
	}

	public void setRisks(ErrorObjects[] risks) {
		this.risks = risks;
	}

	public RiskMapping[] getRiskMappings() {
		return this.riskMappings;
	}

	public void setRiskMappings(RiskMapping[] riskMappings) {
		this.riskMappings = riskMappings;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ErrorDetailObjects)) {
			return false;
		} else {
			ErrorDetailObjects other = (ErrorDetailObjects) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.CRN == null && other.getCRN() == null
						|| this.CRN != null && this.CRN.equals(other.getCRN()))
						&& (this.ORDERID == null && other.getORDERID() == null
								|| this.ORDERID != null && this.ORDERID.equals(other.getORDERID()))
						&& (this.category == null && other.getCategory() == null
								|| this.category != null && this.category.equals(other.getCategory()))
						&& (this.risk == null && other.getRisk() == null
								|| this.risk != null && this.risk.equals(other.getRisk()))
						&& (this.order == null && other.getOrder() == null
								|| this.order != null && this.order.equals(other.getOrder()))
						&& (this.attribute == null && other.getAttribute() == null
								|| this.attribute != null && this.attribute.equals(other.getAttribute()))
						&& (this.risks == null && other.getRisks() == null
								|| this.risks != null && Arrays.equals(this.risks, other.getRisks()))
						&& (this.riskMappings == null && other.getRiskMappings() == null || this.riskMappings != null
								&& Arrays.equals(this.riskMappings, other.getRiskMappings()));
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
			if (this.getCRN() != null) {
				_hashCode += this.getCRN().hashCode();
			}

			if (this.getORDERID() != null) {
				_hashCode += this.getORDERID().hashCode();
			}

			if (this.getCategory() != null) {
				_hashCode += this.getCategory().hashCode();
			}

			if (this.getRisk() != null) {
				_hashCode += this.getRisk().hashCode();
			}

			if (this.getOrder() != null) {
				_hashCode += this.getOrder().hashCode();
			}

			if (this.getAttribute() != null) {
				_hashCode += this.getAttribute().hashCode();
			}

			int i;
			Object obj;
			if (this.getRisks() != null) {
				for (i = 0; i < Array.getLength(this.getRisks()); ++i) {
					obj = Array.get(this.getRisks(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getRiskMappings() != null) {
				for (i = 0; i < Array.getLength(this.getRiskMappings()); ++i) {
					obj = Array.get(this.getRiskMappings(), i);
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