package com.worldcheck.atlas.retrieverclient;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class DocumentProperties implements Serializable {
	private String title;
	private String author;
	private String company;
	private String password;
	private String fileName;
	private String primarySubjectName;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(DocumentProperties.class, true);

	public DocumentProperties() {
	}

	public DocumentProperties(String title, String author, String company, String password, String fileName,
			String primarySubjectName) {
		this.title = title;
		this.author = author;
		this.company = company;
		this.password = password;
		this.fileName = fileName;
		this.primarySubjectName = primarySubjectName;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPrimarySubjectName() {
		return this.primarySubjectName;
	}

	public void setPrimarySubjectName(String primarySubjectName) {
		this.primarySubjectName = primarySubjectName;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof DocumentProperties)) {
			return false;
		} else {
			DocumentProperties other = (DocumentProperties) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.title == null && other.getTitle() == null
						|| this.title != null && this.title.equals(other.getTitle()))
						&& (this.author == null && other.getAuthor() == null
								|| this.author != null && this.author.equals(other.getAuthor()))
						&& (this.company == null && other.getCompany() == null
								|| this.company != null && this.company.equals(other.getCompany()))
						&& (this.password == null && other.getPassword() == null
								|| this.password != null && this.password.equals(other.getPassword()))
						&& (this.fileName == null && other.getFileName() == null
								|| this.fileName != null && this.fileName.equals(other.getFileName()))
						&& (this.primarySubjectName == null && other.getPrimarySubjectName() == null
								|| this.primarySubjectName != null
										&& this.primarySubjectName.equals(other.getPrimarySubjectName()));
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
			if (this.getTitle() != null) {
				_hashCode += this.getTitle().hashCode();
			}

			if (this.getAuthor() != null) {
				_hashCode += this.getAuthor().hashCode();
			}

			if (this.getCompany() != null) {
				_hashCode += this.getCompany().hashCode();
			}

			if (this.getPassword() != null) {
				_hashCode += this.getPassword().hashCode();
			}

			if (this.getFileName() != null) {
				_hashCode += this.getFileName().hashCode();
			}

			if (this.getPrimarySubjectName() != null) {
				_hashCode += this.getPrimarySubjectName().hashCode();
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

	static {
		typeDesc.setXmlType(new QName("http://tempuri.org/", "DocumentProperties"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("title");
		elemField.setXmlName(new QName("http://tempuri.org/", "Title"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("author");
		elemField.setXmlName(new QName("http://tempuri.org/", "Author"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("company");
		elemField.setXmlName(new QName("http://tempuri.org/", "Company"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("password");
		elemField.setXmlName(new QName("http://tempuri.org/", "Password"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("fileName");
		elemField.setXmlName(new QName("http://tempuri.org/", "FileName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("primarySubjectName");
		elemField.setXmlName(new QName("http://tempuri.org/", "PrimarySubjectName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}
}