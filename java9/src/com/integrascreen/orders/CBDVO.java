package com.integrascreen.orders;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CBDVO implements Serializable {
	private String CRN;
	private double budget;
	private String currencyCode;
	private Calendar dueDate;
	private String status;
	private SubjectDetailsVO[] subjectList;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CBDVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "CBDVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("CRN");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CRN"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("budget");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Budget"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("currencyCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CurrencyCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("dueDate");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "DueDate"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
		elemField.setFieldName("subjectList");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectList"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SubjectDetailsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "SubjectDetailsVO"));
		typeDesc.addFieldDesc(elemField);
	}

	public CBDVO() {
	}

	public CBDVO(String CRN, double budget, String currencyCode, Calendar dueDate, String status,
			SubjectDetailsVO[] subjectList) {
		this.CRN = CRN;
		this.budget = budget;
		this.currencyCode = currencyCode;
		this.dueDate = dueDate;
		this.status = status;
		this.subjectList = subjectList;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String CRN) {
		this.CRN = CRN;
	}

	public double getBudget() {
		return this.budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Calendar getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SubjectDetailsVO[] getSubjectList() {
		return this.subjectList;
	}

	public void setSubjectList(SubjectDetailsVO[] subjectList) {
		this.subjectList = subjectList;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CBDVO)) {
			return false;
		} else {
			CBDVO other = (CBDVO) obj;
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
						&& this.budget == other.getBudget()
						&& (this.currencyCode == null && other.getCurrencyCode() == null
								|| this.currencyCode != null && this.currencyCode.equals(other.getCurrencyCode()))
						&& (this.dueDate == null && other.getDueDate() == null
								|| this.dueDate != null && this.dueDate.equals(other.getDueDate()))
						&& (this.status == null && other.getStatus() == null
								|| this.status != null && this.status.equals(other.getStatus()))
						&& (this.subjectList == null && other.getSubjectList() == null
								|| this.subjectList != null && Arrays.equals(this.subjectList, other.getSubjectList()));
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

			_hashCode += (new Double(this.getBudget())).hashCode();
			if (this.getCurrencyCode() != null) {
				_hashCode += this.getCurrencyCode().hashCode();
			}

			if (this.getDueDate() != null) {
				_hashCode += this.getDueDate().hashCode();
			}

			if (this.getStatus() != null) {
				_hashCode += this.getStatus().hashCode();
			}

			if (this.getSubjectList() != null) {
				for (int i = 0; i < Array.getLength(this.getSubjectList()); ++i) {
					Object obj = Array.get(this.getSubjectList(), i);
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