package com.worldcheck.atlas.isis.vo;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ResultSubjectVO implements Serializable {
	private String crn;
	private int atlasSubjectId;
	private String isisSubjectId;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ResultSubjectVO.class, true);

	public ResultSubjectVO() {
	}

	public ResultSubjectVO(String crn, int atlasSubjectId, String isisSubjectId) {
		this.crn = crn;
		this.atlasSubjectId = atlasSubjectId;
		this.isisSubjectId = isisSubjectId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getAtlasSubjectId() {
		return this.atlasSubjectId;
	}

	public void setAtlasSubjectId(int atlasSubjectId) {
		this.atlasSubjectId = atlasSubjectId;
	}

	public String getIsisSubjectId() {
		return this.isisSubjectId;
	}

	public void setIsisSubjectId(String isisSubjectId) {
		this.isisSubjectId = isisSubjectId;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ResultSubjectVO)) {
			return false;
		} else {
			ResultSubjectVO other = (ResultSubjectVO) obj;
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
						&& this.atlasSubjectId == other.getAtlasSubjectId()
						&& (this.isisSubjectId == null && other.getIsisSubjectId() == null
								|| this.isisSubjectId != null && this.isisSubjectId.equals(other.getIsisSubjectId()));
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

			_hashCode += this.getAtlasSubjectId();
			if (this.getIsisSubjectId() != null) {
				_hashCode += this.getIsisSubjectId().hashCode();
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
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "ResultSubjectVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("crn");
		elemField.setXmlName(new QName("", "crn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("atlasSubjectId");
		elemField.setXmlName(new QName("", "atlasSubjectId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isisSubjectId");
		elemField.setXmlName(new QName("", "isisSubjectId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}
}