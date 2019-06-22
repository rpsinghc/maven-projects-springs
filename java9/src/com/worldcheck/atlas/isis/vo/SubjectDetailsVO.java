package com.worldcheck.atlas.isis.vo;

import com.worldcheck.atlas.isis.ArrayOf_xsd_anyType;
import java.io.Serializable;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class SubjectDetailsVO implements Serializable {
	private String crn;
	private String isisSubjectID;
	private int subjectId;
	private String subjectName;
	private int entityType;
	private String countryCode;
	private String subjectPosition;
	private String otherDetails;
	private String address;
	private boolean primarySubject;
	private String subjectFlag;
	private String updatedBy;
	private Calendar updatedOn;
	private String reIds;
	private int slSubreportID;
	private float slBudget;
	private String slCurrency;
	private String slSubreportCode;
	private ArrayOf_xsd_anyType reDetails;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(SubjectDetailsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "SubjectDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("crn");
		elemField.setXmlName(new QName("", "crn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isisSubjectID");
		elemField.setXmlName(new QName("", "isisSubjectID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectId");
		elemField.setXmlName(new QName("", "subjectId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectName");
		elemField.setXmlName(new QName("", "subjectName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("entityType");
		elemField.setXmlName(new QName("", "entityType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("countryCode");
		elemField.setXmlName(new QName("", "countryCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectPosition");
		elemField.setXmlName(new QName("", "subjectPosition"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("otherDetails");
		elemField.setXmlName(new QName("", "otherDetails"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("address");
		elemField.setXmlName(new QName("", "address"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("primarySubject");
		elemField.setXmlName(new QName("", "primarySubject"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectFlag");
		elemField.setXmlName(new QName("", "subjectFlag"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("updatedBy");
		elemField.setXmlName(new QName("", "updatedBy"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("updatedOn");
		elemField.setXmlName(new QName("", "updatedOn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reIds");
		elemField.setXmlName(new QName("", "reIds"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reDetails");
		elemField.setXmlName(new QName("", "reDetails"));
		elemField.setXmlType(new QName("http://isis.atlas.worldcheck.com", "ArrayOf_xsd_anyType"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slSubreportID");
		elemField.setXmlName(new QName("", "slSubreportID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slBudget");
		elemField.setXmlName(new QName("", "slBudget"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "float"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slCurrency");
		elemField.setXmlName(new QName("", "slCurrency"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("slSubreportCode");
		elemField.setXmlName(new QName("", "slSubreportCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public SubjectDetailsVO() {
	}

	public SubjectDetailsVO(String crn, String isisSubjectID, int subjectId, String subjectName, int entityType,
			String countryCode, String subjectPosition, String otherDetails, String address, boolean primarySubject,
			String subjectFlag, String updatedBy, Calendar updatedOn, String reIds, ArrayOf_xsd_anyType reDetails,
			int slSubreportID, float slBudget, String slCurrency, String slSubreportCode) {
		this.crn = crn;
		this.isisSubjectID = isisSubjectID;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.entityType = entityType;
		this.countryCode = countryCode;
		this.subjectPosition = subjectPosition;
		this.otherDetails = otherDetails;
		this.address = address;
		this.primarySubject = primarySubject;
		this.subjectFlag = subjectFlag;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.reIds = reIds;
		this.reDetails = reDetails;
		this.slSubreportID = slSubreportID;
		this.slBudget = slBudget;
		this.slCurrency = slCurrency;
		this.slSubreportCode = slSubreportCode;
	}

	public String getSlSubreportCode() {
		return this.slSubreportCode;
	}

	public void setSlSubreportCode(String slSubreportCode) {
		this.slSubreportCode = slSubreportCode;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getIsisSubjectID() {
		return this.isisSubjectID;
	}

	public void setIsisSubjectID(String isisSubjectID) {
		this.isisSubjectID = isisSubjectID;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public int getEntityType() {
		return this.entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getSubjectPosition() {
		return this.subjectPosition;
	}

	public void setSubjectPosition(String subjectPosition) {
		this.subjectPosition = subjectPosition;
	}

	public String getOtherDetails() {
		return this.otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(boolean primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getSubjectFlag() {
		return this.subjectFlag;
	}

	public void setSubjectFlag(String subjectFlag) {
		this.subjectFlag = subjectFlag;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Calendar getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getReIds() {
		return this.reIds;
	}

	public void setReIds(String reIds) {
		this.reIds = reIds;
	}

	public ArrayOf_xsd_anyType getReDetails() {
		return this.reDetails;
	}

	public void setReDetails(ArrayOf_xsd_anyType reDetails) {
		this.reDetails = reDetails;
	}

	public int getSlSubreportID() {
		return this.slSubreportID;
	}

	public void setSlSubreportID(int sLSubreportID) {
		this.slSubreportID = sLSubreportID;
	}

	public float getSlBudget() {
		return this.slBudget;
	}

	public void setSlBudget(float sLBudget) {
		this.slBudget = sLBudget;
	}

	public String getSlCurrency() {
		return this.slCurrency;
	}

	public void setSlCurrency(String sLCurrency) {
		this.slCurrency = sLCurrency;
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
				boolean _equals = (this.crn == null && other.getCrn() == null
						|| this.crn != null && this.crn.equals(other.getCrn()))
						&& (this.isisSubjectID == null && other.getIsisSubjectID() == null
								|| this.isisSubjectID != null && this.isisSubjectID.equals(other.getIsisSubjectID()))
						&& this.subjectId == other.getSubjectId()
						&& (this.subjectName == null && other.getSubjectName() == null
								|| this.subjectName != null && this.subjectName.equals(other.getSubjectName()))
						&& this.entityType == other.getEntityType()
						&& (this.countryCode == null && other.getCountryCode() == null
								|| this.countryCode != null && this.countryCode.equals(other.getCountryCode()))
						&& (this.subjectPosition == null && other.getSubjectPosition() == null
								|| this.subjectPosition != null
										&& this.subjectPosition.equals(other.getSubjectPosition()))
						&& (this.otherDetails == null && other.getOtherDetails() == null
								|| this.otherDetails != null && this.otherDetails.equals(other.getOtherDetails()))
						&& (this.address == null && other.getAddress() == null
								|| this.address != null && this.address.equals(other.getAddress()))
						&& this.primarySubject == other.isPrimarySubject()
						&& (this.subjectFlag == null && other.getSubjectFlag() == null
								|| this.subjectFlag != null && this.subjectFlag.equals(other.getSubjectFlag()))
						&& (this.updatedBy == null && other.getUpdatedBy() == null
								|| this.updatedBy != null && this.updatedBy.equals(other.getUpdatedBy()))
						&& (this.updatedOn == null && other.getUpdatedOn() == null
								|| this.updatedOn != null && this.updatedOn.equals(other.getUpdatedOn()))
						&& (this.reIds == null && other.getReIds() == null
								|| this.reIds != null && this.reIds.equals(other.getReIds()))
						&& (this.reDetails == null && other.getReDetails() == null
								|| this.reDetails != null && this.reDetails.equals(other.getReDetails()))
						&& this.slSubreportID == other.getSlSubreportID() && this.slBudget == other.getSlBudget()
						&& (this.slCurrency == null && other.getSlCurrency() == null
								|| this.slCurrency != null && this.slCurrency.equals(other.getSlCurrency()))
						&& (this.slSubreportCode == null && other.getSlSubreportCode() == null
								|| this.slSubreportCode != null
										&& this.slSubreportCode.equals(other.getSlSubreportCode()));
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
			if (this.getCrn() != null) {
				_hashCode += this.getCrn().hashCode();
			}

			if (this.getIsisSubjectID() != null) {
				_hashCode += this.getIsisSubjectID().hashCode();
			}

			_hashCode += this.getSubjectId();
			if (this.getSubjectName() != null) {
				_hashCode += this.getSubjectName().hashCode();
			}

			_hashCode += this.getEntityType();
			if (this.getCountryCode() != null) {
				_hashCode += this.getCountryCode().hashCode();
			}

			if (this.getSubjectPosition() != null) {
				_hashCode += this.getSubjectPosition().hashCode();
			}

			if (this.getOtherDetails() != null) {
				_hashCode += this.getOtherDetails().hashCode();
			}

			if (this.getAddress() != null) {
				_hashCode += this.getAddress().hashCode();
			}

			_hashCode += (this.isPrimarySubject() ? Boolean.TRUE : Boolean.FALSE).hashCode();
			if (this.getSubjectFlag() != null) {
				_hashCode += this.getSubjectFlag().hashCode();
			}

			if (this.getUpdatedBy() != null) {
				_hashCode += this.getUpdatedBy().hashCode();
			}

			if (this.getUpdatedOn() != null) {
				_hashCode += this.getUpdatedOn().hashCode();
			}

			if (this.getReIds() != null) {
				_hashCode += this.getReIds().hashCode();
			}

			if (this.getReDetails() != null) {
				_hashCode += this.getReDetails().hashCode();
			}

			_hashCode += this.getSlSubreportID();
			_hashCode = (int) ((float) _hashCode + this.getSlBudget());
			if (this.getSlCurrency() != null) {
				_hashCode += this.getSlCurrency().hashCode();
			}

			if (this.getSlSubreportCode() != null) {
				_hashCode += this.getSlSubreportCode().hashCode();
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