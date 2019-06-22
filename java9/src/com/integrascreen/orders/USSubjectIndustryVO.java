package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class USSubjectIndustryVO implements Serializable {
	private String ISISSubjectID;
	private String atlasSubjectID;
	private String industryID;
	private String industryName;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(USSubjectIndustryVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "USSubjectIndustryVO"));
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
		elemField.setFieldName("industryID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "IndustryID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("industryName");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "IndustryName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public USSubjectIndustryVO() {
	}

	public USSubjectIndustryVO(String ISISSubjectID, String atlasSubjectID, String industryID, String industryName) {
		this.ISISSubjectID = ISISSubjectID;
		this.atlasSubjectID = atlasSubjectID;
		this.industryID = industryID;
		this.industryName = industryName;
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

	public String getIndustryID() {
		return this.industryID;
	}

	public void setIndustryID(String industryID) {
		this.industryID = industryID;
	}

	public String getIndustryName() {
		return this.industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof USSubjectIndustryVO)) {
			return false;
		} else {
			USSubjectIndustryVO other = (USSubjectIndustryVO) obj;
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
						&& (this.industryID == null && other.getIndustryID() == null
								|| this.industryID != null && this.industryID.equals(other.getIndustryID()))
						&& (this.industryName == null && other.getIndustryName() == null
								|| this.industryName != null && this.industryName.equals(other.getIndustryName()));
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

			if (this.getIndustryID() != null) {
				_hashCode += this.getIndustryID().hashCode();
			}

			if (this.getIndustryName() != null) {
				_hashCode += this.getIndustryName().hashCode();
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