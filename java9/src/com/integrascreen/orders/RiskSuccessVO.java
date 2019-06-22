package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class RiskSuccessVO implements Serializable {
	private SuccessRisk risks;
	private SuccessMapping riskMapping;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskSuccessVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskSuccessVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("risks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Risks"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SuccessRisk"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskMapping");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskMapping"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SuccessMapping"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public RiskSuccessVO() {
	}

	public RiskSuccessVO(SuccessRisk risks, SuccessMapping riskMapping) {
		this.risks = risks;
		this.riskMapping = riskMapping;
	}

	public SuccessRisk getRisks() {
		return this.risks;
	}

	public void setRisks(SuccessRisk risks) {
		this.risks = risks;
	}

	public SuccessMapping getRiskMapping() {
		return this.riskMapping;
	}

	public void setRiskMapping(SuccessMapping riskMapping) {
		this.riskMapping = riskMapping;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskSuccessVO)) {
			return false;
		} else {
			RiskSuccessVO other = (RiskSuccessVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.risks == null && other.getRisks() == null
						|| this.risks != null && this.risks.equals(other.getRisks()))
						&& (this.riskMapping == null && other.getRiskMapping() == null
								|| this.riskMapping != null && this.riskMapping.equals(other.getRiskMapping()));
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
			if (this.getRisks() != null) {
				_hashCode += this.getRisks().hashCode();
			}

			if (this.getRiskMapping() != null) {
				_hashCode += this.getRiskMapping().hashCode();
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