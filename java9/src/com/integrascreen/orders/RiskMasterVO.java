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

public class RiskMasterVO implements Serializable {
	private String action;
	private String code;
	private String description;
	private String status;
	private String label;
	private int riskCategoryID;
	private String riskCategoryLabel;
	private int riskIsActive;
	private int riskHasCountryBreakDown;
	private int type;
	private String remarks;
	private int displayOnProfile;
	private String createdOn;
	private RiskMappingsVO[] riskMappings;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskMasterVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskMasterVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("action");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Action"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("code");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Code"));
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
		elemField.setFieldName("status");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Status"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("label");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Label"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskCategoryID");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskCategoryID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskCategoryLabel");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskCategoryLabel"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskIsActive");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskIsActive"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskHasCountryBreakDown");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskHasCountryBreakDown"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Type"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("remarks");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Remarks"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("displayOnProfile");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "DisplayOnProfile"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("createdOn");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CreatedOn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskMappings");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskMappings"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "RiskMappingsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "RiskMappingsVO"));
		typeDesc.addFieldDesc(elemField);
	}

	public RiskMasterVO() {
	}

	public RiskMasterVO(String action, String code, String description, String status, String label, int riskCategoryID,
			String riskCategoryLabel, int riskIsActive, int riskHasCountryBreakDown, int type, String remarks,
			int displayOnProfile, String createdOn, RiskMappingsVO[] riskMappings) {
		this.action = action;
		this.code = code;
		this.description = description;
		this.status = status;
		this.label = label;
		this.riskCategoryID = riskCategoryID;
		this.riskCategoryLabel = riskCategoryLabel;
		this.riskIsActive = riskIsActive;
		this.riskHasCountryBreakDown = riskHasCountryBreakDown;
		this.type = type;
		this.remarks = remarks;
		this.displayOnProfile = displayOnProfile;
		this.createdOn = createdOn;
		this.riskMappings = riskMappings;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getRiskCategoryID() {
		return this.riskCategoryID;
	}

	public void setRiskCategoryID(int riskCategoryID) {
		this.riskCategoryID = riskCategoryID;
	}

	public String getRiskCategoryLabel() {
		return this.riskCategoryLabel;
	}

	public void setRiskCategoryLabel(String riskCategoryLabel) {
		this.riskCategoryLabel = riskCategoryLabel;
	}

	public int getRiskIsActive() {
		return this.riskIsActive;
	}

	public void setRiskIsActive(int riskIsActive) {
		this.riskIsActive = riskIsActive;
	}

	public int getRiskHasCountryBreakDown() {
		return this.riskHasCountryBreakDown;
	}

	public void setRiskHasCountryBreakDown(int riskHasCountryBreakDown) {
		this.riskHasCountryBreakDown = riskHasCountryBreakDown;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getDisplayOnProfile() {
		return this.displayOnProfile;
	}

	public void setDisplayOnProfile(int displayOnProfile) {
		this.displayOnProfile = displayOnProfile;
	}

	public String getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public RiskMappingsVO[] getRiskMappings() {
		return this.riskMappings;
	}

	public void setRiskMappings(RiskMappingsVO[] riskMappings) {
		this.riskMappings = riskMappings;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskMasterVO)) {
			return false;
		} else {
			RiskMasterVO other = (RiskMasterVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.action == null && other.getAction() == null
						|| this.action != null && this.action.equals(other.getAction()))
						&& (this.code == null && other.getCode() == null
								|| this.code != null && this.code.equals(other.getCode()))
						&& (this.description == null && other.getDescription() == null
								|| this.description != null && this.description.equals(other.getDescription()))
						&& (this.status == null && other.getStatus() == null
								|| this.status != null && this.status.equals(other.getStatus()))
						&& (this.label == null && other.getLabel() == null
								|| this.label != null && this.label.equals(other.getLabel()))
						&& this.riskCategoryID == other.getRiskCategoryID()
						&& (this.riskCategoryLabel == null && other.getRiskCategoryLabel() == null
								|| this.riskCategoryLabel != null
										&& this.riskCategoryLabel.equals(other.getRiskCategoryLabel()))
						&& this.riskIsActive == other.getRiskIsActive()
						&& this.riskHasCountryBreakDown == other.getRiskHasCountryBreakDown()
						&& this.type == other.getType()
						&& (this.remarks == null && other.getRemarks() == null
								|| this.remarks != null && this.remarks.equals(other.getRemarks()))
						&& this.displayOnProfile == other.getDisplayOnProfile()
						&& (this.createdOn == null && other.getCreatedOn() == null
								|| this.createdOn != null && this.createdOn.equals(other.getCreatedOn()))
						&& (this.riskMappings == null && other.getRiskMappings() == null || this.riskMappings != null
								&& Arrays.equals(this.riskMappings, other.getRiskMappings()));
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
			if (this.getAction() != null) {
				_hashCode += this.getAction().hashCode();
			}

			if (this.getCode() != null) {
				_hashCode += this.getCode().hashCode();
			}

			if (this.getDescription() != null) {
				_hashCode += this.getDescription().hashCode();
			}

			if (this.getStatus() != null) {
				_hashCode += this.getStatus().hashCode();
			}

			if (this.getLabel() != null) {
				_hashCode += this.getLabel().hashCode();
			}

			_hashCode += this.getRiskCategoryID();
			if (this.getRiskCategoryLabel() != null) {
				_hashCode += this.getRiskCategoryLabel().hashCode();
			}

			_hashCode += this.getRiskIsActive();
			_hashCode += this.getRiskHasCountryBreakDown();
			_hashCode += this.getType();
			if (this.getRemarks() != null) {
				_hashCode += this.getRemarks().hashCode();
			}

			_hashCode += this.getDisplayOnProfile();
			if (this.getCreatedOn() != null) {
				_hashCode += this.getCreatedOn().hashCode();
			}

			if (this.getRiskMappings() != null) {
				for (int i = 0; i < Array.getLength(this.getRiskMappings()); ++i) {
					Object obj = Array.get(this.getRiskMappings(), i);
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