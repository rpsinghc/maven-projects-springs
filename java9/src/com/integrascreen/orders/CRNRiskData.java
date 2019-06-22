package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CRNRiskData implements Serializable {
	private String CRN;
	private String orderID;
	private RiskAggregation riskAggregation;
	private int hasCaseLevelRisks;
	private CaseLevelRisks caseLevelRisks;
	private int hasSubjectLevelRisks;
	private SubjectLevelRisks subjectLevelRisks;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CRNRiskData.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "CRNRiskData"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("orderID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "OrderID"));
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
		elemField.setFieldName("hasCaseLevelRisks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "HasCaseLevelRisks"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("caseLevelRisks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CaseLevelRisks"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CaseLevelRisks"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("hasSubjectLevelRisks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "HasSubjectLevelRisks"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectLevelRisks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectLevelRisks"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SubjectLevelRisks"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public CRNRiskData() {
	}

	public CRNRiskData(String CRN, String orderID, RiskAggregation riskAggregation, int hasCaseLevelRisks,
			CaseLevelRisks caseLevelRisks, int hasSubjectLevelRisks, SubjectLevelRisks subjectLevelRisks) {
		this.CRN = CRN;
		this.orderID = orderID;
		this.riskAggregation = riskAggregation;
		this.hasCaseLevelRisks = hasCaseLevelRisks;
		this.caseLevelRisks = caseLevelRisks;
		this.hasSubjectLevelRisks = hasSubjectLevelRisks;
		this.subjectLevelRisks = subjectLevelRisks;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public String getOrderID() {
		return this.orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public RiskAggregation getRiskAggregation() {
		return this.riskAggregation;
	}

	public void setRiskAggregation(RiskAggregation riskAggregation) {
		this.riskAggregation = riskAggregation;
	}

	public int getHasCaseLevelRisks() {
		return this.hasCaseLevelRisks;
	}

	public void setHasCaseLevelRisks(int hasCaseLevelRisks) {
		this.hasCaseLevelRisks = hasCaseLevelRisks;
	}

	public CaseLevelRisks getCaseLevelRisks() {
		return this.caseLevelRisks;
	}

	public void setCaseLevelRisks(CaseLevelRisks caseLevelRisks) {
		this.caseLevelRisks = caseLevelRisks;
	}

	public int getHasSubjectLevelRisks() {
		return this.hasSubjectLevelRisks;
	}

	public void setHasSubjectLevelRisks(int hasSubjectLevelRisks) {
		this.hasSubjectLevelRisks = hasSubjectLevelRisks;
	}

	public SubjectLevelRisks getSubjectLevelRisks() {
		return this.subjectLevelRisks;
	}

	public void setSubjectLevelRisks(SubjectLevelRisks subjectLevelRisks) {
		this.subjectLevelRisks = subjectLevelRisks;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CRNRiskData)) {
			return false;
		} else {
			CRNRiskData other = (CRNRiskData) obj;
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
						&& (this.orderID == null && other.getOrderID() == null
								|| this.orderID != null && this.orderID.equals(other.getOrderID()))
						&& (this.riskAggregation == null && other.getRiskAggregation() == null
								|| this.riskAggregation != null
										&& this.riskAggregation.equals(other.getRiskAggregation()))
						&& this.hasCaseLevelRisks == other.getHasCaseLevelRisks()
						&& (this.caseLevelRisks == null && other.getCaseLevelRisks() == null
								|| this.caseLevelRisks != null && this.caseLevelRisks.equals(other.getCaseLevelRisks()))
						&& this.hasSubjectLevelRisks == other.getHasSubjectLevelRisks()
						&& (this.subjectLevelRisks == null && other.getSubjectLevelRisks() == null
								|| this.subjectLevelRisks != null
										&& this.subjectLevelRisks.equals(other.getSubjectLevelRisks()));
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

			if (this.getOrderID() != null) {
				_hashCode += this.getOrderID().hashCode();
			}

			if (this.getRiskAggregation() != null) {
				_hashCode += this.getRiskAggregation().hashCode();
			}

			_hashCode += this.getHasCaseLevelRisks();
			if (this.getCaseLevelRisks() != null) {
				_hashCode += this.getCaseLevelRisks().hashCode();
			}

			_hashCode += this.getHasSubjectLevelRisks();
			if (this.getSubjectLevelRisks() != null) {
				_hashCode += this.getSubjectLevelRisks().hashCode();
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