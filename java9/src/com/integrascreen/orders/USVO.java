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

public class USVO implements Serializable {
	private String CRN;
	private String status;
	private String fileName;
	private double version;
	private String expressCase;
	private String updateType;
	private USSubjectRiskVO[] subjectRisk;
	private USSubjectIndustryVO[] subjectIndustry;
	private RiskProfile otherinformation;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(USVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "USVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("status");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Status"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("fileName");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "FileName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("version");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Version"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("expressCase");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ExpressCase"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
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
		elemField.setFieldName("subjectRisk");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectRisk"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "USSubjectRiskVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "USSubjectRiskVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectIndustry");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectIndustry"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "USSubjectIndustryVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "USSubjectIndustryVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("otherinformation");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Otherinformation"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskProfile"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public USVO() {
	}

	public USVO(String CRN, String status, String fileName, double version, String expressCase, String updateType,
			USSubjectRiskVO[] subjectRisk, USSubjectIndustryVO[] subjectIndustry, RiskProfile otherinformation) {
		this.CRN = CRN;
		this.status = status;
		this.fileName = fileName;
		this.version = version;
		this.expressCase = expressCase;
		this.updateType = updateType;
		this.subjectRisk = subjectRisk;
		this.subjectIndustry = subjectIndustry;
		this.otherinformation = otherinformation;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getVersion() {
		return this.version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getExpressCase() {
		return this.expressCase;
	}

	public void setExpressCase(String expressCase) {
		this.expressCase = expressCase;
	}

	public String getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public USSubjectRiskVO[] getSubjectRisk() {
		return this.subjectRisk;
	}

	public void setSubjectRisk(USSubjectRiskVO[] subjectRisk) {
		this.subjectRisk = subjectRisk;
	}

	public USSubjectIndustryVO[] getSubjectIndustry() {
		return this.subjectIndustry;
	}

	public void setSubjectIndustry(USSubjectIndustryVO[] subjectIndustry) {
		this.subjectIndustry = subjectIndustry;
	}

	public RiskProfile getOtherinformation() {
		return this.otherinformation;
	}

	public void setOtherinformation(RiskProfile otherinformation) {
		this.otherinformation = otherinformation;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof USVO)) {
			return false;
		} else {
			USVO other = (USVO) obj;
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
						&& (this.status == null && other.getStatus() == null
								|| this.status != null && this.status.equals(other.getStatus()))
						&& (this.fileName == null && other.getFileName() == null
								|| this.fileName != null && this.fileName.equals(other.getFileName()))
						&& this.version == other.getVersion()
						&& (this.expressCase == null && other.getExpressCase() == null
								|| this.expressCase != null && this.expressCase.equals(other.getExpressCase()))
						&& (this.updateType == null && other.getUpdateType() == null
								|| this.updateType != null && this.updateType.equals(other.getUpdateType()))
						&& (this.subjectRisk == null && other.getSubjectRisk() == null
								|| this.subjectRisk != null && Arrays.equals(this.subjectRisk, other.getSubjectRisk()))
						&& (this.subjectIndustry == null && other.getSubjectIndustry() == null
								|| this.subjectIndustry != null
										&& Arrays.equals(this.subjectIndustry, other.getSubjectIndustry()))
						&& (this.otherinformation == null && other.getOtherinformation() == null
								|| this.otherinformation != null
										&& this.otherinformation.equals(other.getOtherinformation()));
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

			if (this.getStatus() != null) {
				_hashCode += this.getStatus().hashCode();
			}

			if (this.getFileName() != null) {
				_hashCode += this.getFileName().hashCode();
			}

			_hashCode += (new Double(this.getVersion())).hashCode();
			if (this.getExpressCase() != null) {
				_hashCode += this.getExpressCase().hashCode();
			}

			if (this.getUpdateType() != null) {
				_hashCode += this.getUpdateType().hashCode();
			}

			int i;
			Object obj;
			if (this.getSubjectRisk() != null) {
				for (i = 0; i < Array.getLength(this.getSubjectRisk()); ++i) {
					obj = Array.get(this.getSubjectRisk(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getSubjectIndustry() != null) {
				for (i = 0; i < Array.getLength(this.getSubjectIndustry()); ++i) {
					obj = Array.get(this.getSubjectIndustry(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getOtherinformation() != null) {
				_hashCode += this.getOtherinformation().hashCode();
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