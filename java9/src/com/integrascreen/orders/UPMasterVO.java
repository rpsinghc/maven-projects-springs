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

public class UPMasterVO implements Serializable {
	private String master;
	private String updateType;
	private String updatedBy;
	private CurrencyMasterVO curencyMaster;
	private CountryMasterVO countryMaster;
	private REMasterVO REMaster;
	private RiskMasterVO[] riskMaster;
	private IndustryMasterVO industryMaster;
	private ReportTypeMasterVO reportTypeMaster;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(UPMasterVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "UPMasterVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("master");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Master"));
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
		elemField.setFieldName("updatedBy");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "UpdatedBy"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("curencyMaster");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CurencyMaster"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CurrencyMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("countryMaster");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CountryMaster"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "CountryMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("REMaster");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "REMaster"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "REMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskMaster");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskMaster"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "RiskMasterVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("industryMaster");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "IndustryMaster"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "IndustryMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reportTypeMaster");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ReportTypeMaster"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ReportTypeMasterVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public UPMasterVO() {
	}

	public UPMasterVO(String master, String updateType, String updatedBy, CurrencyMasterVO curencyMaster,
			CountryMasterVO countryMaster, REMasterVO REMaster, RiskMasterVO[] riskMaster,
			IndustryMasterVO industryMaster, ReportTypeMasterVO reportTypeMaster) {
		this.master = master;
		this.updateType = updateType;
		this.updatedBy = updatedBy;
		this.curencyMaster = curencyMaster;
		this.countryMaster = countryMaster;
		this.REMaster = REMaster;
		this.riskMaster = riskMaster;
		this.industryMaster = industryMaster;
		this.reportTypeMaster = reportTypeMaster;
	}

	public String getMaster() {
		return this.master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public CurrencyMasterVO getCurencyMaster() {
		return this.curencyMaster;
	}

	public void setCurencyMaster(CurrencyMasterVO curencyMaster) {
		this.curencyMaster = curencyMaster;
	}

	public CountryMasterVO getCountryMaster() {
		return this.countryMaster;
	}

	public void setCountryMaster(CountryMasterVO countryMaster) {
		this.countryMaster = countryMaster;
	}

	public REMasterVO getREMaster() {
		return this.REMaster;
	}

	public void setREMaster(REMasterVO REMaster) {
		this.REMaster = REMaster;
	}

	public RiskMasterVO[] getRiskMaster() {
		return this.riskMaster;
	}

	public void setRiskMaster(RiskMasterVO[] riskMaster) {
		this.riskMaster = riskMaster;
	}

	public IndustryMasterVO getIndustryMaster() {
		return this.industryMaster;
	}

	public void setIndustryMaster(IndustryMasterVO industryMaster) {
		this.industryMaster = industryMaster;
	}

	public ReportTypeMasterVO getReportTypeMaster() {
		return this.reportTypeMaster;
	}

	public void setReportTypeMaster(ReportTypeMasterVO reportTypeMaster) {
		this.reportTypeMaster = reportTypeMaster;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof UPMasterVO)) {
			return false;
		} else {
			UPMasterVO other = (UPMasterVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.master == null && other.getMaster() == null
						|| this.master != null && this.master.equals(other.getMaster()))
						&& (this.updateType == null && other.getUpdateType() == null
								|| this.updateType != null && this.updateType.equals(other.getUpdateType()))
						&& (this.updatedBy == null && other.getUpdatedBy() == null
								|| this.updatedBy != null && this.updatedBy.equals(other.getUpdatedBy()))
						&& (this.curencyMaster == null && other.getCurencyMaster() == null
								|| this.curencyMaster != null && this.curencyMaster.equals(other.getCurencyMaster()))
						&& (this.countryMaster == null && other.getCountryMaster() == null
								|| this.countryMaster != null && this.countryMaster.equals(other.getCountryMaster()))
						&& (this.REMaster == null && other.getREMaster() == null
								|| this.REMaster != null && this.REMaster.equals(other.getREMaster()))
						&& (this.riskMaster == null && other.getRiskMaster() == null
								|| this.riskMaster != null && Arrays.equals(this.riskMaster, other.getRiskMaster()))
						&& (this.industryMaster == null && other.getIndustryMaster() == null
								|| this.industryMaster != null && this.industryMaster.equals(other.getIndustryMaster()))
						&& (this.reportTypeMaster == null && other.getReportTypeMaster() == null
								|| this.reportTypeMaster != null
										&& this.reportTypeMaster.equals(other.getReportTypeMaster()));
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
			if (this.getMaster() != null) {
				_hashCode += this.getMaster().hashCode();
			}

			if (this.getUpdateType() != null) {
				_hashCode += this.getUpdateType().hashCode();
			}

			if (this.getUpdatedBy() != null) {
				_hashCode += this.getUpdatedBy().hashCode();
			}

			if (this.getCurencyMaster() != null) {
				_hashCode += this.getCurencyMaster().hashCode();
			}

			if (this.getCountryMaster() != null) {
				_hashCode += this.getCountryMaster().hashCode();
			}

			if (this.getREMaster() != null) {
				_hashCode += this.getREMaster().hashCode();
			}

			if (this.getRiskMaster() != null) {
				for (int i = 0; i < Array.getLength(this.getRiskMaster()); ++i) {
					Object obj = Array.get(this.getRiskMaster(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getIndustryMaster() != null) {
				_hashCode += this.getIndustryMaster().hashCode();
			}

			if (this.getReportTypeMaster() != null) {
				_hashCode += this.getReportTypeMaster().hashCode();
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