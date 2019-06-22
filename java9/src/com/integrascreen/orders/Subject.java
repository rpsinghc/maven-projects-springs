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

public class Subject implements Serializable {
	private int subjectID;
	private String subjectName;
	private String ISISSubjectID;
	private int subjectType;
	private String country;
	private String subjectIndustryCode;
	private RiskAggregation riskAggregation;
	private RiskCategory[] riskCategories;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(Subject.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "Subject"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("subjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "subjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectName");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("ISISSubjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ISISSubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("country");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Country"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectIndustryCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectIndustryCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskAggregation");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskCategories");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskCategories"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskCategory"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "RiskCategory"));
		typeDesc.addFieldDesc(elemField);
	}

	public Subject() {
	}

	public Subject(int subjectID, String subjectName, String ISISSubjectID, int subjectType, String country,
			String subjectIndustryCode, RiskAggregation riskAggregation, RiskCategory[] riskCategories) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.ISISSubjectID = ISISSubjectID;
		this.subjectType = subjectType;
		this.country = country;
		this.subjectIndustryCode = subjectIndustryCode;
		this.riskAggregation = riskAggregation;
		this.riskCategories = riskCategories;
	}

	public int getSubjectID() {
		return this.subjectID;
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getISISSubjectID() {
		return this.ISISSubjectID;
	}

	public void setISISSubjectID(String ISISSubjectID) {
		this.ISISSubjectID = ISISSubjectID;
	}

	public int getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSubjectIndustryCode() {
		return this.subjectIndustryCode;
	}

	public void setSubjectIndustryCode(String subjectIndustryCode) {
		this.subjectIndustryCode = subjectIndustryCode;
	}

	public RiskAggregation getRiskAggregation() {
		return this.riskAggregation;
	}

	public void setRiskAggregation(RiskAggregation riskAggregation) {
		this.riskAggregation = riskAggregation;
	}

	public RiskCategory[] getRiskCategories() {
		return this.riskCategories;
	}

	public void setRiskCategories(RiskCategory[] riskCategories) {
		this.riskCategories = riskCategories;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof Subject)) {
			return false;
		} else {
			Subject other = (Subject) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.subjectID == other.getSubjectID()
						&& (this.subjectName == null && other.getSubjectName() == null
								|| this.subjectName != null && this.subjectName.equals(other.getSubjectName()))
						&& (this.ISISSubjectID == null && other.getISISSubjectID() == null
								|| this.ISISSubjectID != null && this.ISISSubjectID.equals(other.getISISSubjectID()))
						&& this.subjectType == other.getSubjectType()
						&& (this.country == null && other.getCountry() == null
								|| this.country != null && this.country.equals(other.getCountry()))
						&& (this.subjectIndustryCode == null && other.getSubjectIndustryCode() == null
								|| this.subjectIndustryCode != null
										&& this.subjectIndustryCode.equals(other.getSubjectIndustryCode()))
						&& (this.riskAggregation == null && other.getRiskAggregation() == null
								|| this.riskAggregation != null
										&& this.riskAggregation.equals(other.getRiskAggregation()))
						&& (this.riskCategories == null && other.getRiskCategories() == null
								|| this.riskCategories != null
										&& Arrays.equals(this.riskCategories, other.getRiskCategories()));
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
			int _hashCode = _hashCode + this.getSubjectID();
			if (this.getSubjectName() != null) {
				_hashCode += this.getSubjectName().hashCode();
			}

			if (this.getISISSubjectID() != null) {
				_hashCode += this.getISISSubjectID().hashCode();
			}

			_hashCode += this.getSubjectType();
			if (this.getCountry() != null) {
				_hashCode += this.getCountry().hashCode();
			}

			if (this.getSubjectIndustryCode() != null) {
				_hashCode += this.getSubjectIndustryCode().hashCode();
			}

			if (this.getRiskAggregation() != null) {
				_hashCode += this.getRiskAggregation().hashCode();
			}

			if (this.getRiskCategories() != null) {
				for (int i = 0; i < Array.getLength(this.getRiskCategories()); ++i) {
					Object obj = Array.get(this.getRiskCategories(), i);
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