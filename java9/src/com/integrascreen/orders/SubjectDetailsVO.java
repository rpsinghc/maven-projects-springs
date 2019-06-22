package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SubjectDetailsVO implements Serializable {
	private String ISISSubjectID;
	private String subjectName;
	private String country;
	private int entityType;
	private String subReportCode;
	private double SLBudget;
	private String SLCurrency;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SubjectDetailsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "SubjectDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("ISISSubjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ISISSubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
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
		elemField.setFieldName("country");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Country"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("entityType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "EntityType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subReportCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubReportCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("SLBudget");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SLBudget"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("SLCurrency");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SLCurrency"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SubjectDetailsVO() {
	}

	public SubjectDetailsVO(String ISISSubjectID, String subjectName, String country, int entityType,
			String subReportCode, double SLBudget, String SLCurrency) {
		this.ISISSubjectID = ISISSubjectID;
		this.subjectName = subjectName;
		this.country = country;
		this.entityType = entityType;
		this.subReportCode = subReportCode;
		this.SLBudget = SLBudget;
		this.SLCurrency = SLCurrency;
	}

	public String getISISSubjectID() {
		return this.ISISSubjectID;
	}

	public void setISISSubjectID(String ISISSubjectID) {
		this.ISISSubjectID = ISISSubjectID;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getEntityType() {
		return this.entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public String getSubReportCode() {
		return this.subReportCode;
	}

	public void setSubReportCode(String subReportCode) {
		this.subReportCode = subReportCode;
	}

	public double getSLBudget() {
		return this.SLBudget;
	}

	public void setSLBudget(double SLBudget) {
		this.SLBudget = SLBudget;
	}

	public String getSLCurrency() {
		return this.SLCurrency;
	}

	public void setSLCurrency(String SLCurrency) {
		this.SLCurrency = SLCurrency;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SubjectDetailsVO)) {
			return false;
		} else {
			SubjectDetailsVO other = (SubjectDetailsVO) obj;
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
						&& (this.subjectName == null && other.getSubjectName() == null
								|| this.subjectName != null && this.subjectName.equals(other.getSubjectName()))
						&& (this.country == null && other.getCountry() == null
								|| this.country != null && this.country.equals(other.getCountry()))
						&& this.entityType == other.getEntityType()
						&& (this.subReportCode == null && other.getSubReportCode() == null
								|| this.subReportCode != null && this.subReportCode.equals(other.getSubReportCode()))
						&& this.SLBudget == other.getSLBudget()
						&& (this.SLCurrency == null && other.getSLCurrency() == null
								|| this.SLCurrency != null && this.SLCurrency.equals(other.getSLCurrency()));
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

			if (this.getSubjectName() != null) {
				_hashCode += this.getSubjectName().hashCode();
			}

			if (this.getCountry() != null) {
				_hashCode += this.getCountry().hashCode();
			}

			_hashCode += this.getEntityType();
			if (this.getSubReportCode() != null) {
				_hashCode += this.getSubReportCode().hashCode();
			}

			_hashCode += (new Double(this.getSLBudget())).hashCode();
			if (this.getSLCurrency() != null) {
				_hashCode += this.getSLCurrency().hashCode();
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