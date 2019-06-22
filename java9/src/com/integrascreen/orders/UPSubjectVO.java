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

public class UPSubjectVO implements Serializable {
	private String CRN;
	private String subjectID;
	private int atlasSubjectID;
	private String subjectName;
	private String subjectType;
	private String subjectPosition;
	private String country;
	private String otherDetails;
	private boolean isPrimary;
	private String slSubreportCode;
	private String slCurrency;
	private double slBudget;
	private String updateType;
	private UPSubjectREVO[] subjectRE;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(UPSubjectVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "UPSubjectVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("atlasSubjectID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "AtlasSubjectID"));
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
		elemField.setFieldName("subjectType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectPosition");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectPosition"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
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
		elemField.setFieldName("otherDetails");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "OtherDetails"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isPrimary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "IsPrimary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slSubreportCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SlSubreportCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slCurrency");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SlCurrency"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slBudget");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SlBudget"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("updateType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "UpdateType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectRE");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectRE"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "UPSubjectREVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "UPSubjectREVO"));
		typeDesc.addFieldDesc(elemField);
	}

	public UPSubjectVO() {
	}

	public UPSubjectVO(String CRN, String subjectID, int atlasSubjectID, String subjectName, String subjectType,
			String subjectPosition, String country, String otherDetails, boolean isPrimary, String slSubreportCode,
			String slCurrency, double slBudget, String updateType, UPSubjectREVO[] subjectRE) {
		this.CRN = CRN;
		this.subjectID = subjectID;
		this.atlasSubjectID = atlasSubjectID;
		this.subjectName = subjectName;
		this.subjectType = subjectType;
		this.subjectPosition = subjectPosition;
		this.country = country;
		this.otherDetails = otherDetails;
		this.isPrimary = isPrimary;
		this.slSubreportCode = slSubreportCode;
		this.slCurrency = slCurrency;
		this.slBudget = slBudget;
		this.updateType = updateType;
		this.subjectRE = subjectRE;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public String getSubjectID() {
		return this.subjectID;
	}

	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}

	public int getAtlasSubjectID() {
		return this.atlasSubjectID;
	}

	public void setAtlasSubjectID(int atlasSubjectID) {
		this.atlasSubjectID = atlasSubjectID;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectPosition() {
		return this.subjectPosition;
	}

	public void setSubjectPosition(String subjectPosition) {
		this.subjectPosition = subjectPosition;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOtherDetails() {
		return this.otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public boolean isIsPrimary() {
		return this.isPrimary;
	}

	public void setIsPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getSlSubreportCode() {
		return this.slSubreportCode;
	}

	public void setSlSubreportCode(String slSubreportCode) {
		this.slSubreportCode = slSubreportCode;
	}

	public String getSlCurrency() {
		return this.slCurrency;
	}

	public void setSlCurrency(String slCurrency) {
		this.slCurrency = slCurrency;
	}

	public double getSlBudget() {
		return this.slBudget;
	}

	public void setSlBudget(double slBudget) {
		this.slBudget = slBudget;
	}

	public String getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public UPSubjectREVO[] getSubjectRE() {
		return this.subjectRE;
	}

	public void setSubjectRE(UPSubjectREVO[] subjectRE) {
		this.subjectRE = subjectRE;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof UPSubjectVO)) {
			return false;
		} else {
			UPSubjectVO other = (UPSubjectVO) obj;
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
						&& (this.subjectID == null && other.getSubjectID() == null
								|| this.subjectID != null && this.subjectID.equals(other.getSubjectID()))
						&& this.atlasSubjectID == other.getAtlasSubjectID()
						&& (this.subjectName == null && other.getSubjectName() == null
								|| this.subjectName != null && this.subjectName.equals(other.getSubjectName()))
						&& (this.subjectType == null && other.getSubjectType() == null
								|| this.subjectType != null && this.subjectType.equals(other.getSubjectType()))
						&& (this.subjectPosition == null && other.getSubjectPosition() == null
								|| this.subjectPosition != null
										&& this.subjectPosition.equals(other.getSubjectPosition()))
						&& (this.country == null && other.getCountry() == null
								|| this.country != null && this.country.equals(other.getCountry()))
						&& (this.otherDetails == null && other.getOtherDetails() == null
								|| this.otherDetails != null && this.otherDetails.equals(other.getOtherDetails()))
						&& this.isPrimary == other.isIsPrimary()
						&& (this.slSubreportCode == null && other.getSlSubreportCode() == null
								|| this.slSubreportCode != null
										&& this.slSubreportCode.equals(other.getSlSubreportCode()))
						&& (this.slCurrency == null && other.getSlCurrency() == null
								|| this.slCurrency != null && this.slCurrency.equals(other.getSlCurrency()))
						&& this.slBudget == other.getSlBudget()
						&& (this.updateType == null && other.getUpdateType() == null
								|| this.updateType != null && this.updateType.equals(other.getUpdateType()))
						&& (this.subjectRE == null && other.getSubjectRE() == null
								|| this.subjectRE != null && Arrays.equals(this.subjectRE, other.getSubjectRE()));
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

			if (this.getSubjectID() != null) {
				_hashCode += this.getSubjectID().hashCode();
			}

			_hashCode += this.getAtlasSubjectID();
			if (this.getSubjectName() != null) {
				_hashCode += this.getSubjectName().hashCode();
			}

			if (this.getSubjectType() != null) {
				_hashCode += this.getSubjectType().hashCode();
			}

			if (this.getSubjectPosition() != null) {
				_hashCode += this.getSubjectPosition().hashCode();
			}

			if (this.getCountry() != null) {
				_hashCode += this.getCountry().hashCode();
			}

			if (this.getOtherDetails() != null) {
				_hashCode += this.getOtherDetails().hashCode();
			}

			_hashCode += (this.isIsPrimary() ? Boolean.TRUE : Boolean.FALSE).hashCode();
			if (this.getSlSubreportCode() != null) {
				_hashCode += this.getSlSubreportCode().hashCode();
			}

			if (this.getSlCurrency() != null) {
				_hashCode += this.getSlCurrency().hashCode();
			}

			_hashCode += (new Double(this.getSlBudget())).hashCode();
			if (this.getUpdateType() != null) {
				_hashCode += this.getUpdateType().hashCode();
			}

			if (this.getSubjectRE() != null) {
				for (int i = 0; i < Array.getLength(this.getSubjectRE()); ++i) {
					Object obj = Array.get(this.getSubjectRE(), i);
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