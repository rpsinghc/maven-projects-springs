package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ErrorObjects implements Serializable {
	private String errorCode;
	private String errorMessage;
	private CodeDetailsVO order;
	private CodeDetailsVO attribute;
	private CodeDetailsVO categoryID;
	private CodeDetailsVO riskCode;
	private CodeDetailsVO mappingID;
	private CodeDetailsVO clientCode;
	private CodeDetailsVO reportType;
	private CodeDetailsVO subReportType;
	private CodeDetailsVO REID;
	private CodeDetailsVO subCountry;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ErrorObjects.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ErrorObjects"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("errorCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("errorMessage");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ErrorMessage"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("order");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Order"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("attribute");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Attribute"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("categoryID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CategoryID"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskCode"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("mappingID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "MappingID"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clientCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ClientCode"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reportType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ReportType"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subReportType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubReportType"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("REID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "REID"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subCountry");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubCountry"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CodeDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public ErrorObjects() {
	}

	public ErrorObjects(String errorCode, String errorMessage, CodeDetailsVO order, CodeDetailsVO attribute,
			CodeDetailsVO categoryID, CodeDetailsVO riskCode, CodeDetailsVO mappingID, CodeDetailsVO clientCode,
			CodeDetailsVO reportType, CodeDetailsVO subReportType, CodeDetailsVO REID, CodeDetailsVO subCountry) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.order = order;
		this.attribute = attribute;
		this.categoryID = categoryID;
		this.riskCode = riskCode;
		this.mappingID = mappingID;
		this.clientCode = clientCode;
		this.reportType = reportType;
		this.subReportType = subReportType;
		this.REID = REID;
		this.subCountry = subCountry;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public CodeDetailsVO getOrder() {
		return this.order;
	}

	public void setOrder(CodeDetailsVO order) {
		this.order = order;
	}

	public CodeDetailsVO getAttribute() {
		return this.attribute;
	}

	public void setAttribute(CodeDetailsVO attribute) {
		this.attribute = attribute;
	}

	public CodeDetailsVO getCategoryID() {
		return this.categoryID;
	}

	public void setCategoryID(CodeDetailsVO categoryID) {
		this.categoryID = categoryID;
	}

	public CodeDetailsVO getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(CodeDetailsVO riskCode) {
		this.riskCode = riskCode;
	}

	public CodeDetailsVO getMappingID() {
		return this.mappingID;
	}

	public void setMappingID(CodeDetailsVO mappingID) {
		this.mappingID = mappingID;
	}

	public CodeDetailsVO getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(CodeDetailsVO clientCode) {
		this.clientCode = clientCode;
	}

	public CodeDetailsVO getReportType() {
		return this.reportType;
	}

	public void setReportType(CodeDetailsVO reportType) {
		this.reportType = reportType;
	}

	public CodeDetailsVO getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(CodeDetailsVO subReportType) {
		this.subReportType = subReportType;
	}

	public CodeDetailsVO getREID() {
		return this.REID;
	}

	public void setREID(CodeDetailsVO REID) {
		this.REID = REID;
	}

	public CodeDetailsVO getSubCountry() {
		return this.subCountry;
	}

	public void setSubCountry(CodeDetailsVO subCountry) {
		this.subCountry = subCountry;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ErrorObjects)) {
			return false;
		} else {
			ErrorObjects other = (ErrorObjects) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.errorCode == null && other.getErrorCode() == null
						|| this.errorCode != null && this.errorCode.equals(other.getErrorCode()))
						&& (this.errorMessage == null && other.getErrorMessage() == null
								|| this.errorMessage != null && this.errorMessage.equals(other.getErrorMessage()))
						&& (this.order == null && other.getOrder() == null
								|| this.order != null && this.order.equals(other.getOrder()))
						&& (this.attribute == null && other.getAttribute() == null
								|| this.attribute != null && this.attribute.equals(other.getAttribute()))
						&& (this.categoryID == null && other.getCategoryID() == null
								|| this.categoryID != null && this.categoryID.equals(other.getCategoryID()))
						&& (this.riskCode == null && other.getRiskCode() == null
								|| this.riskCode != null && this.riskCode.equals(other.getRiskCode()))
						&& (this.mappingID == null && other.getMappingID() == null
								|| this.mappingID != null && this.mappingID.equals(other.getMappingID()))
						&& (this.clientCode == null && other.getClientCode() == null
								|| this.clientCode != null && this.clientCode.equals(other.getClientCode()))
						&& (this.reportType == null && other.getReportType() == null
								|| this.reportType != null && this.reportType.equals(other.getReportType()))
						&& (this.subReportType == null && other.getSubReportType() == null
								|| this.subReportType != null && this.subReportType.equals(other.getSubReportType()))
						&& (this.REID == null && other.getREID() == null
								|| this.REID != null && this.REID.equals(other.getREID()))
						&& (this.subCountry == null && other.getSubCountry() == null
								|| this.subCountry != null && this.subCountry.equals(other.getSubCountry()));
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
			if (this.getErrorCode() != null) {
				_hashCode += this.getErrorCode().hashCode();
			}

			if (this.getErrorMessage() != null) {
				_hashCode += this.getErrorMessage().hashCode();
			}

			if (this.getOrder() != null) {
				_hashCode += this.getOrder().hashCode();
			}

			if (this.getAttribute() != null) {
				_hashCode += this.getAttribute().hashCode();
			}

			if (this.getCategoryID() != null) {
				_hashCode += this.getCategoryID().hashCode();
			}

			if (this.getRiskCode() != null) {
				_hashCode += this.getRiskCode().hashCode();
			}

			if (this.getMappingID() != null) {
				_hashCode += this.getMappingID().hashCode();
			}

			if (this.getClientCode() != null) {
				_hashCode += this.getClientCode().hashCode();
			}

			if (this.getReportType() != null) {
				_hashCode += this.getReportType().hashCode();
			}

			if (this.getSubReportType() != null) {
				_hashCode += this.getSubReportType().hashCode();
			}

			if (this.getREID() != null) {
				_hashCode += this.getREID().hashCode();
			}

			if (this.getSubCountry() != null) {
				_hashCode += this.getSubCountry().hashCode();
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