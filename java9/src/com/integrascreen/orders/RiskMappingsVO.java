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

public class RiskMappingsVO implements Serializable {
	private int mappingId;
	private String mappingName;
	private String actionType;
	private int riskWeight;
	private int mappingIsActive;
	private int riskGroup;
	private int visibleToClients;
	private ClientsVO[] clients;
	private ReportTypesVo[] reportTypes;
	private ResearchElementsVO[] researchElement;
	private int subjectType;
	private SubjectCountriesVO[] subjectCountries;
	private int hasAllResearchElements;
	private int hasAllClients;
	private int hasAllReportTypes;
	private int hasSubjectCountries;
	private String createdOn;
	private String lastUpdatedBy;
	private String lastUpdatedOn;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskMappingsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskMappingsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("mappingId");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "MappingId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("mappingName");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "MappingName"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("actionType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ActionType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskWeight");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskWeight"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("mappingIsActive");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "MappingIsActive"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("riskGroup");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "RiskGroup"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("visibleToClients");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "VisibleToClients"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clients");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "Clients"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ClientsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "ClientsVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reportTypes");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ReportTypes"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ReportTypesVo"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "ReportTypesVo"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("researchElement");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "ResearchElement"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "ResearchElementsVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "ResearchElementsVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectType");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectCountries");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectCountries"));
		elemField.setXmlType(new QName("http://orders.integrascreen.com", "SubjectCountriesVO"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new QName("http://orders.integrascreen.com", "SubjectCountriesVO"));
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("hasAllResearchElements");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "hasAllResearchElements"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("hasAllClients");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "hasAllClients"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("hasAllReportTypes");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "hasAllReportTypes"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("hasSubjectCountries");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "hasSubjectCountries"));
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
		elemField.setFieldName("lastUpdatedBy");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "lastUpdatedBy"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("lastUpdatedOn");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "lastUpdatedOn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public RiskMappingsVO() {
	}

	public RiskMappingsVO(int mappingId, String mappingName, String actionType, int riskWeight, int mappingIsActive,
			int riskGroup, int visibleToClients, ClientsVO[] clients, ReportTypesVo[] reportTypes,
			ResearchElementsVO[] researchElement, int subjectType, SubjectCountriesVO[] subjectCountries,
			int hasAllResearchElements, int hasAllClients, int hasAllReportTypes, int hasSubjectCountries,
			String createdOn, String lastUpdatedBy, String lastUpdatedOn) {
		this.mappingId = mappingId;
		this.mappingName = mappingName;
		this.actionType = actionType;
		this.riskWeight = riskWeight;
		this.mappingIsActive = mappingIsActive;
		this.riskGroup = riskGroup;
		this.visibleToClients = visibleToClients;
		this.clients = clients;
		this.reportTypes = reportTypes;
		this.researchElement = researchElement;
		this.subjectType = subjectType;
		this.subjectCountries = subjectCountries;
		this.hasAllResearchElements = hasAllResearchElements;
		this.hasAllClients = hasAllClients;
		this.hasAllReportTypes = hasAllReportTypes;
		this.hasSubjectCountries = hasSubjectCountries;
		this.createdOn = createdOn;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public int getMappingId() {
		return this.mappingId;
	}

	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}

	public String getMappingName() {
		return this.mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public int getRiskWeight() {
		return this.riskWeight;
	}

	public void setRiskWeight(int riskWeight) {
		this.riskWeight = riskWeight;
	}

	public int getMappingIsActive() {
		return this.mappingIsActive;
	}

	public void setMappingIsActive(int mappingIsActive) {
		this.mappingIsActive = mappingIsActive;
	}

	public int getRiskGroup() {
		return this.riskGroup;
	}

	public void setRiskGroup(int riskGroup) {
		this.riskGroup = riskGroup;
	}

	public int getVisibleToClients() {
		return this.visibleToClients;
	}

	public void setVisibleToClients(int visibleToClients) {
		this.visibleToClients = visibleToClients;
	}

	public ClientsVO[] getClients() {
		return this.clients;
	}

	public void setClients(ClientsVO[] clients) {
		this.clients = clients;
	}

	public ReportTypesVo[] getReportTypes() {
		return this.reportTypes;
	}

	public void setReportTypes(ReportTypesVo[] reportTypes) {
		this.reportTypes = reportTypes;
	}

	public ResearchElementsVO[] getResearchElement() {
		return this.researchElement;
	}

	public void setResearchElement(ResearchElementsVO[] researchElement) {
		this.researchElement = researchElement;
	}

	public int getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

	public SubjectCountriesVO[] getSubjectCountries() {
		return this.subjectCountries;
	}

	public void setSubjectCountries(SubjectCountriesVO[] subjectCountries) {
		this.subjectCountries = subjectCountries;
	}

	public int getHasAllResearchElements() {
		return this.hasAllResearchElements;
	}

	public void setHasAllResearchElements(int hasAllResearchElements) {
		this.hasAllResearchElements = hasAllResearchElements;
	}

	public int getHasAllClients() {
		return this.hasAllClients;
	}

	public void setHasAllClients(int hasAllClients) {
		this.hasAllClients = hasAllClients;
	}

	public int getHasAllReportTypes() {
		return this.hasAllReportTypes;
	}

	public void setHasAllReportTypes(int hasAllReportTypes) {
		this.hasAllReportTypes = hasAllReportTypes;
	}

	public int getHasSubjectCountries() {
		return this.hasSubjectCountries;
	}

	public void setHasSubjectCountries(int hasSubjectCountries) {
		this.hasSubjectCountries = hasSubjectCountries;
	}

	public String getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedOn() {
		return this.lastUpdatedOn;
	}

	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskMappingsVO)) {
			return false;
		} else {
			RiskMappingsVO other = (RiskMappingsVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.mappingId == other.getMappingId()
						&& (this.mappingName == null && other.getMappingName() == null
								|| this.mappingName != null && this.mappingName.equals(other.getMappingName()))
						&& (this.actionType == null && other.getActionType() == null
								|| this.actionType != null && this.actionType.equals(other.getActionType()))
						&& this.riskWeight == other.getRiskWeight()
						&& this.mappingIsActive == other.getMappingIsActive() && this.riskGroup == other.getRiskGroup()
						&& this.visibleToClients == other.getVisibleToClients()
						&& (this.clients == null && other.getClients() == null
								|| this.clients != null && Arrays.equals(this.clients, other.getClients()))
						&& (this.reportTypes == null && other.getReportTypes() == null
								|| this.reportTypes != null && Arrays.equals(this.reportTypes, other.getReportTypes()))
						&& (this.researchElement == null && other.getResearchElement() == null
								|| this.researchElement != null
										&& Arrays.equals(this.researchElement, other.getResearchElement()))
						&& this.subjectType == other.getSubjectType()
						&& (this.subjectCountries == null && other.getSubjectCountries() == null
								|| this.subjectCountries != null
										&& Arrays.equals(this.subjectCountries, other.getSubjectCountries()))
						&& this.hasAllResearchElements == other.getHasAllResearchElements()
						&& this.hasAllClients == other.getHasAllClients()
						&& this.hasAllReportTypes == other.getHasAllReportTypes()
						&& this.hasSubjectCountries == other.getHasSubjectCountries()
						&& (this.createdOn == null && other.getCreatedOn() == null
								|| this.createdOn != null && this.createdOn.equals(other.getCreatedOn()))
						&& (this.lastUpdatedBy == null && other.getLastUpdatedBy() == null
								|| this.lastUpdatedBy != null && this.lastUpdatedBy.equals(other.getLastUpdatedBy()))
						&& (this.lastUpdatedOn == null && other.getLastUpdatedOn() == null
								|| this.lastUpdatedOn != null && this.lastUpdatedOn.equals(other.getLastUpdatedOn()));
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
			int _hashCode = _hashCode + this.getMappingId();
			if (this.getMappingName() != null) {
				_hashCode += this.getMappingName().hashCode();
			}

			if (this.getActionType() != null) {
				_hashCode += this.getActionType().hashCode();
			}

			_hashCode += this.getRiskWeight();
			_hashCode += this.getMappingIsActive();
			_hashCode += this.getRiskGroup();
			_hashCode += this.getVisibleToClients();
			int i;
			Object obj;
			if (this.getClients() != null) {
				for (i = 0; i < Array.getLength(this.getClients()); ++i) {
					obj = Array.get(this.getClients(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getReportTypes() != null) {
				for (i = 0; i < Array.getLength(this.getReportTypes()); ++i) {
					obj = Array.get(this.getReportTypes(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			if (this.getResearchElement() != null) {
				for (i = 0; i < Array.getLength(this.getResearchElement()); ++i) {
					obj = Array.get(this.getResearchElement(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			_hashCode += this.getSubjectType();
			if (this.getSubjectCountries() != null) {
				for (i = 0; i < Array.getLength(this.getSubjectCountries()); ++i) {
					obj = Array.get(this.getSubjectCountries(), i);
					if (obj != null && !obj.getClass().isArray()) {
						_hashCode += obj.hashCode();
					}
				}
			}

			_hashCode += this.getHasAllResearchElements();
			_hashCode += this.getHasAllClients();
			_hashCode += this.getHasAllReportTypes();
			_hashCode += this.getHasSubjectCountries();
			if (this.getCreatedOn() != null) {
				_hashCode += this.getCreatedOn().hashCode();
			}

			if (this.getLastUpdatedBy() != null) {
				_hashCode += this.getLastUpdatedBy().hashCode();
			}

			if (this.getLastUpdatedOn() != null) {
				_hashCode += this.getLastUpdatedOn().hashCode();
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