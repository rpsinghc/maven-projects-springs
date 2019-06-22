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

public class Risk implements Serializable {
	private String code;
	private String label;
	private int riskGroup;
	private int riskIsActive;
	private int visibleToClient;
	private int displayOnProfile;
	private int riskType;
	private int riskHasCountryBreakDown;
	private Attribute[] attributes;
	private CountryBreakDown COuntryBreak;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(Risk.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "Risk"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("code");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Code"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("label");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Label"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskGroup");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskGroup"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskIsActive");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskIsActive"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("visibleToClient");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "VisibleToClient"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("displayOnProfile");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "DisplayOnProfile"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskHasCountryBreakDown");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskHasCountryBreakDown"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
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
		elemField = new ElementDesc();
		elemField.setFieldName("COuntryBreak");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "COuntryBreak"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CountryBreakDown"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public Risk() {
	}

	public Risk(String code, String label, int riskGroup, int riskIsActive, int visibleToClient, int displayOnProfile,
			int riskType, int riskHasCountryBreakDown, Attribute[] attributes, CountryBreakDown COuntryBreak) {
		this.code = code;
		this.label = label;
		this.riskGroup = riskGroup;
		this.riskIsActive = riskIsActive;
		this.visibleToClient = visibleToClient;
		this.displayOnProfile = displayOnProfile;
		this.riskType = riskType;
		this.riskHasCountryBreakDown = riskHasCountryBreakDown;
		this.attributes = attributes;
		this.COuntryBreak = COuntryBreak;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getRiskGroup() {
		return this.riskGroup;
	}

	public void setRiskGroup(int riskGroup) {
		this.riskGroup = riskGroup;
	}

	public int getRiskIsActive() {
		return this.riskIsActive;
	}

	public void setRiskIsActive(int riskIsActive) {
		this.riskIsActive = riskIsActive;
	}

	public int getVisibleToClient() {
		return this.visibleToClient;
	}

	public void setVisibleToClient(int visibleToClient) {
		this.visibleToClient = visibleToClient;
	}

	public int getDisplayOnProfile() {
		return this.displayOnProfile;
	}

	public void setDisplayOnProfile(int displayOnProfile) {
		this.displayOnProfile = displayOnProfile;
	}

	public int getRiskType() {
		return this.riskType;
	}

	public void setRiskType(int riskType) {
		this.riskType = riskType;
	}

	public int getRiskHasCountryBreakDown() {
		return this.riskHasCountryBreakDown;
	}

	public void setRiskHasCountryBreakDown(int riskHasCountryBreakDown) {
		this.riskHasCountryBreakDown = riskHasCountryBreakDown;
	}

	public Attribute[] getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public CountryBreakDown getCOuntryBreak() {
		return this.COuntryBreak;
	}

	public void setCOuntryBreak(CountryBreakDown COuntryBreak) {
		this.COuntryBreak = COuntryBreak;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof Risk)) {
			return false;
		} else {
			Risk other = (Risk) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.code == null && other.getCode() == null
						|| this.code != null && this.code.equals(other.getCode()))
						&& (this.label == null && other.getLabel() == null
								|| this.label != null && this.label.equals(other.getLabel()))
						&& this.riskGroup == other.getRiskGroup() && this.riskIsActive == other.getRiskIsActive()
						&& this.visibleToClient == other.getVisibleToClient()
						&& this.displayOnProfile == other.getDisplayOnProfile() && this.riskType == other.getRiskType()
						&& this.riskHasCountryBreakDown == other.getRiskHasCountryBreakDown()
						&& (this.attributes == null && other.getAttributes() == null
								|| this.attributes != null && Arrays.equals(this.attributes, other.getAttributes()))
						&& (this.COuntryBreak == null && other.getCOuntryBreak() == null
								|| this.COuntryBreak != null && this.COuntryBreak.equals(other.getCOuntryBreak()));
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
			if (this.getCode() != null) {
				_hashCode += this.getCode().hashCode();
			}

			if (this.getLabel() != null) {
				_hashCode += this.getLabel().hashCode();
			}

			_hashCode += this.getRiskGroup();
			_hashCode += this.getRiskIsActive();
			_hashCode += this.getVisibleToClient();
			_hashCode += this.getDisplayOnProfile();
			_hashCode += this.getRiskType();
			_hashCode += this.getRiskHasCountryBreakDown();
			if (this.getAttributes() != null) {
				for (int i = 0; i < Array.getLength(this.getAttributes()); ++i) {
					Object obj = Array.get(this.getAttributes(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getCOuntryBreak() != null) {
				_hashCode += this.getCOuntryBreak().hashCode();
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