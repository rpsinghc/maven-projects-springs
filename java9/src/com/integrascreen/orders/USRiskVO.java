package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class USRiskVO implements Serializable {
	private String riskID;
	private String riskName;
	private String possibleRisk;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(USRiskVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "USRiskVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("riskID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskName");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("possibleRisk");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "PossibleRisk"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public USRiskVO() {
	}

	public USRiskVO(String riskID, String riskName, String possibleRisk) {
		this.riskID = riskID;
		this.riskName = riskName;
		this.possibleRisk = possibleRisk;
	}

	public String getRiskID() {
		return this.riskID;
	}

	public void setRiskID(String riskID) {
		this.riskID = riskID;
	}

	public String getRiskName() {
		return this.riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getPossibleRisk() {
		return this.possibleRisk;
	}

	public void setPossibleRisk(String possibleRisk) {
		this.possibleRisk = possibleRisk;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof USRiskVO)) {
			return false;
		} else {
			USRiskVO other = (USRiskVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.riskID == null && other.getRiskID() == null
						|| this.riskID != null && this.riskID.equals(other.getRiskID()))
						&& (this.riskName == null && other.getRiskName() == null
								|| this.riskName != null && this.riskName.equals(other.getRiskName()))
						&& (this.possibleRisk == null && other.getPossibleRisk() == null
								|| this.possibleRisk != null && this.possibleRisk.equals(other.getPossibleRisk()));
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
			if (this.getRiskID() != null) {
				_hashCode += this.getRiskID().hashCode();
			}

			if (this.getRiskName() != null) {
				_hashCode += this.getRiskName().hashCode();
			}

			if (this.getPossibleRisk() != null) {
				_hashCode += this.getPossibleRisk().hashCode();
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