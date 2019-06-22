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

public class USSubjectRiskVO implements Serializable {
	private String ISISSubjectID;
	private String atlasSubjectID;
	private USRiskVO[] risk;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(USSubjectRiskVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "USSubjectRiskVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("ISISSubjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ISISSubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("atlasSubjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "AtlasSubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("risk");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Risk"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "USRiskVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "USRiskVO"));
		typeDesc.addFieldDesc(elemField);
	}

	public USSubjectRiskVO() {
	}

	public USSubjectRiskVO(String ISISSubjectID, String atlasSubjectID, USRiskVO[] risk) {
		this.ISISSubjectID = ISISSubjectID;
		this.atlasSubjectID = atlasSubjectID;
		this.risk = risk;
	}

	public String getISISSubjectID() {
		return this.ISISSubjectID;
	}

	public void setISISSubjectID(String ISISSubjectID) {
		this.ISISSubjectID = ISISSubjectID;
	}

	public String getAtlasSubjectID() {
		return this.atlasSubjectID;
	}

	public void setAtlasSubjectID(String atlasSubjectID) {
		this.atlasSubjectID = atlasSubjectID;
	}

	public USRiskVO[] getRisk() {
		return this.risk;
	}

	public void setRisk(USRiskVO[] risk) {
		this.risk = risk;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof USSubjectRiskVO)) {
			return false;
		} else {
			USSubjectRiskVO other = (USSubjectRiskVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.ISISSubjectID == null && other.getISISSubjectID() == null
						|| this.ISISSubjectID != null && this.ISISSubjectID.equals(other.getISISSubjectID()))
						&& (this.atlasSubjectID == null && other.getAtlasSubjectID() == null
								|| this.atlasSubjectID != null && this.atlasSubjectID.equals(other.getAtlasSubjectID()))
						&& (this.risk == null && other.getRisk() == null
								|| this.risk != null && Arrays.equals(this.risk, other.getRisk()));
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
			if (this.getISISSubjectID() != null) {
				_hashCode += this.getISISSubjectID().hashCode();
			}

			if (this.getAtlasSubjectID() != null) {
				_hashCode += this.getAtlasSubjectID().hashCode();
			}

			if (this.getRisk() != null) {
				for (int i = 0; i < Array.getLength(this.getRisk()); ++i) {
					Object obj = Array.get(this.getRisk(), i);
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