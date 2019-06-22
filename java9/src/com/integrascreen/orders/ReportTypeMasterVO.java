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

public class ReportTypeMasterVO implements Serializable {
	private String reportypeCode;
	private String description;
	private int TAT;
	private int hasSubReport;
	private String RLDefaultREs;
	private SubReportTypeMasterVO[] subReportType;
	private int RLStatus;
	private String RLActionType;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(ReportTypeMasterVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "ReportTypeMasterVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("reportypeCode");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ReportypeCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Description"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("TAT");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TAT"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("hasSubReport");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "HasSubReport"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("RLDefaultREs");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RLDefaultREs"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subReportType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubReportType"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SubReportTypeMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "SubReportTypeMasterVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("RLStatus");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RLStatus"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("RLActionType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RLActionType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public ReportTypeMasterVO() {
	}

	public ReportTypeMasterVO(String reportypeCode, String description, int TAT, int hasSubReport, String RLDefaultREs,
			SubReportTypeMasterVO[] subReportType, int RLStatus, String RLActionType) {
		this.reportypeCode = reportypeCode;
		this.description = description;
		this.TAT = TAT;
		this.hasSubReport = hasSubReport;
		this.RLDefaultREs = RLDefaultREs;
		this.subReportType = subReportType;
		this.RLStatus = RLStatus;
		this.RLActionType = RLActionType;
	}

	public String getReportypeCode() {
		return this.reportypeCode;
	}

	public void setReportypeCode(String reportypeCode) {
		this.reportypeCode = reportypeCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTAT() {
		return this.TAT;
	}

	public void setTAT(int TAT) {
		this.TAT = TAT;
	}

	public int getHasSubReport() {
		return this.hasSubReport;
	}

	public void setHasSubReport(int hasSubReport) {
		this.hasSubReport = hasSubReport;
	}

	public String getRLDefaultREs() {
		return this.RLDefaultREs;
	}

	public void setRLDefaultREs(String RLDefaultREs) {
		this.RLDefaultREs = RLDefaultREs;
	}

	public SubReportTypeMasterVO[] getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(SubReportTypeMasterVO[] subReportType) {
		this.subReportType = subReportType;
	}

	public int getRLStatus() {
		return this.RLStatus;
	}

	public void setRLStatus(int RLStatus) {
		this.RLStatus = RLStatus;
	}

	public String getRLActionType() {
		return this.RLActionType;
	}

	public void setRLActionType(String RLActionType) {
		this.RLActionType = RLActionType;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ReportTypeMasterVO)) {
			return false;
		} else {
			ReportTypeMasterVO other = (ReportTypeMasterVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.reportypeCode == null && other.getReportypeCode() == null
						|| this.reportypeCode != null && this.reportypeCode.equals(other.getReportypeCode()))
						&& (this.description == null && other.getDescription() == null
								|| this.description != null && this.description.equals(other.getDescription()))
						&& this.TAT == other.getTAT() && this.hasSubReport == other.getHasSubReport()
						&& (this.RLDefaultREs == null && other.getRLDefaultREs() == null
								|| this.RLDefaultREs != null && this.RLDefaultREs.equals(other.getRLDefaultREs()))
						&& (this.subReportType == null && other.getSubReportType() == null || this.subReportType != null
								&& Arrays.equals(this.subReportType, other.getSubReportType()))
						&& this.RLStatus == other.getRLStatus()
						&& (this.RLActionType == null && other.getRLActionType() == null
								|| this.RLActionType != null && this.RLActionType.equals(other.getRLActionType()));
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
			if (this.getReportypeCode() != null) {
				_hashCode += this.getReportypeCode().hashCode();
			}

			if (this.getDescription() != null) {
				_hashCode += this.getDescription().hashCode();
			}

			_hashCode += this.getTAT();
			_hashCode += this.getHasSubReport();
			if (this.getRLDefaultREs() != null) {
				_hashCode += this.getRLDefaultREs().hashCode();
			}

			if (this.getSubReportType() != null) {
				for (int i = 0; i < Array.getLength(this.getSubReportType()); ++i) {
					Object obj = Array.get(this.getSubReportType(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			_hashCode += this.getRLStatus();
			if (this.getRLActionType() != null) {
				_hashCode += this.getRLActionType().hashCode();
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