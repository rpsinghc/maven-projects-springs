package com.worldcheck.atlas.isis.vo;

import com.worldcheck.atlas.isis.ArrayOf_xsd_anyType;
import java.io.Serializable;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CaseDetailsVO implements Serializable {
	private String crn;
	private String assignmentType;
	private String branchType;
	private float budget;
	private int bulkOrder;
	private String bulkOrderId;
	private String caseFlag;
	private String caseManager;
	private String clientCode;
	private Calendar clientFinalDueDate;
	private String clientReferenceNumber;
	private String currencyCode;
	private int expressCase;
	private ArrayOf_xsd_anyType fileDetails;
	private String officeAssignment;
	private String orderGUID;
	private Calendar orderReceiptDate;
	private String priority;
	private String reportTypeId;
	private Calendar researchFinalDueDate;
	private String specialInstruction;
	private String subReportType;
	private boolean isBudgetConfirmed;
	private ArrayOf_xsd_anyType subjectDetails;
	private String taxCode;
	private String isisUserWhoPlacedOrder;
	private String isisUserEmailId;
	private boolean isSLSubReportType;
	private int clSubreportID;
	private String clSubreportCode;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(CaseDetailsVO.class, true);

	static {
		typeDesc.setXmlType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseDetailsVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("crn");
		elemField.setXmlName(new QName("", "crn"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("assignmentType");
		elemField.setXmlName(new QName("", "assignmentType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("branchType");
		elemField.setXmlName(new QName("", "branchType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("budget");
		elemField.setXmlName(new QName("", "budget"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "float"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("bulkOrder");
		elemField.setXmlName(new QName("", "bulkOrder"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("bulkOrderId");
		elemField.setXmlName(new QName("", "bulkOrderId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("caseFlag");
		elemField.setXmlName(new QName("", "caseFlag"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("caseManager");
		elemField.setXmlName(new QName("", "caseManager"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clientCode");
		elemField.setXmlName(new QName("", "clientCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clientFinalDueDate");
		elemField.setXmlName(new QName("", "clientFinalDueDate"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clientReferenceNumber");
		elemField.setXmlName(new QName("", "clientReferenceNumber"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("currencyCode");
		elemField.setXmlName(new QName("", "currencyCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("expressCase");
		elemField.setXmlName(new QName("", "expressCase"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("fileDetails");
		elemField.setXmlName(new QName("", "fileDetails"));
		elemField.setXmlType(new QName("http://isis.atlas.worldcheck.com", "ArrayOf_xsd_anyType"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("officeAssignment");
		elemField.setXmlName(new QName("", "officeAssignment"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("orderGUID");
		elemField.setXmlName(new QName("", "orderGUID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("orderReceiptDate");
		elemField.setXmlName(new QName("", "orderReceiptDate"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("priority");
		elemField.setXmlName(new QName("", "priority"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reportTypeId");
		elemField.setXmlName(new QName("", "reportTypeId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("researchFinalDueDate");
		elemField.setXmlName(new QName("", "researchFinalDueDate"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("specialInstruction");
		elemField.setXmlName(new QName("", "specialInstruction"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subReportType");
		elemField.setXmlName(new QName("", "subReportType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isBudgetConfirmed");
		elemField.setXmlName(new QName("", "isBudgetConfirmed"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectDetails");
		elemField.setXmlName(new QName("", "subjectDetails"));
		elemField.setXmlType(new QName("http://isis.atlas.worldcheck.com", "ArrayOf_xsd_anyType"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("taxCode");
		elemField.setXmlName(new QName("", "taxCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isisUserWhoPlacedOrder");
		elemField.setXmlName(new QName("", "isisUserWhoPlacedOrder"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isisUserEmailId");
		elemField.setXmlName(new QName("", "isisUserEmailId"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isSLSubReportType");
		elemField.setXmlName(new QName("", "isSLSubReportType"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clSubreportID");
		elemField.setXmlName(new QName("", "clSubreportID"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("clSubreportCode");
		elemField.setXmlName(new QName("", "clSubreportCode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public CaseDetailsVO() {
	}

	public CaseDetailsVO(String crn, String assignmentType, String branchType, float budget, int bulkOrder,
			String bulkOrderId, String caseFlag, String caseManager, String clientCode, Calendar clientFinalDueDate,
			String clientReferenceNumber, String currencyCode, int expressCase, ArrayOf_xsd_anyType fileDetails,
			String officeAssignment, String orderGUID, Calendar orderReceiptDate, String priority, String reportTypeId,
			Calendar researchFinalDueDate, String specialInstruction, String subReportType, boolean isBudgetConfirmed,
			ArrayOf_xsd_anyType subjectDetails, String taxCode, String isisUserWhoPlacedOrder, String isisUserEmailId,
			boolean isSLSubReportType, int clSubreportID, String clSubreportCode) {
		this.crn = crn;
		this.assignmentType = assignmentType;
		this.branchType = branchType;
		this.budget = budget;
		this.bulkOrder = bulkOrder;
		this.bulkOrderId = bulkOrderId;
		this.caseFlag = caseFlag;
		this.caseManager = caseManager;
		this.clientCode = clientCode;
		this.clientFinalDueDate = clientFinalDueDate;
		this.clientReferenceNumber = clientReferenceNumber;
		this.currencyCode = currencyCode;
		this.expressCase = expressCase;
		this.fileDetails = fileDetails;
		this.officeAssignment = officeAssignment;
		this.orderGUID = orderGUID;
		this.orderReceiptDate = orderReceiptDate;
		this.priority = priority;
		this.reportTypeId = reportTypeId;
		this.researchFinalDueDate = researchFinalDueDate;
		this.specialInstruction = specialInstruction;
		this.subReportType = subReportType;
		this.isBudgetConfirmed = isBudgetConfirmed;
		this.subjectDetails = subjectDetails;
		this.taxCode = taxCode;
		this.isisUserWhoPlacedOrder = isisUserWhoPlacedOrder;
		this.isisUserEmailId = isisUserEmailId;
		this.isSLSubReportType = isSLSubReportType;
		this.clSubreportID = clSubreportID;
		this.clSubreportCode = clSubreportCode;
	}

	public String getClSubreportCode() {
		return this.clSubreportCode;
	}

	public void setClSubreportCode(String clSubreportCode) {
		this.clSubreportCode = clSubreportCode;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getAssignmentType() {
		return this.assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public String getBranchType() {
		return this.branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

	public float getBudget() {
		return this.budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public int getBulkOrder() {
		return this.bulkOrder;
	}

	public void setBulkOrder(int bulkOrder) {
		this.bulkOrder = bulkOrder;
	}

	public String getBulkOrderId() {
		return this.bulkOrderId;
	}

	public void setBulkOrderId(String bulkOrderId) {
		this.bulkOrderId = bulkOrderId;
	}

	public String getCaseFlag() {
		return this.caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Calendar getClientFinalDueDate() {
		return this.clientFinalDueDate;
	}

	public void setClientFinalDueDate(Calendar clientFinalDueDate) {
		this.clientFinalDueDate = clientFinalDueDate;
	}

	public String getClientReferenceNumber() {
		return this.clientReferenceNumber;
	}

	public void setClientReferenceNumber(String clientReferenceNumber) {
		this.clientReferenceNumber = clientReferenceNumber;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public int getExpressCase() {
		return this.expressCase;
	}

	public void setExpressCase(int expressCase) {
		this.expressCase = expressCase;
	}

	public ArrayOf_xsd_anyType getFileDetails() {
		return this.fileDetails;
	}

	public void setFileDetails(ArrayOf_xsd_anyType fileDetails) {
		this.fileDetails = fileDetails;
	}

	public String getOfficeAssignment() {
		return this.officeAssignment;
	}

	public void setOfficeAssignment(String officeAssignment) {
		this.officeAssignment = officeAssignment;
	}

	public String getOrderGUID() {
		return this.orderGUID;
	}

	public void setOrderGUID(String orderGUID) {
		this.orderGUID = orderGUID;
	}

	public Calendar getOrderReceiptDate() {
		return this.orderReceiptDate;
	}

	public void setOrderReceiptDate(Calendar orderReceiptDate) {
		this.orderReceiptDate = orderReceiptDate;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getReportTypeId() {
		return this.reportTypeId;
	}

	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public Calendar getResearchFinalDueDate() {
		return this.researchFinalDueDate;
	}

	public void setResearchFinalDueDate(Calendar researchFinalDueDate) {
		this.researchFinalDueDate = researchFinalDueDate;
	}

	public String getSpecialInstruction() {
		return this.specialInstruction;
	}

	public void setSpecialInstruction(String specialInstruction) {
		this.specialInstruction = specialInstruction;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public boolean isIsBudgetConfirmed() {
		return this.isBudgetConfirmed;
	}

	public void setIsBudgetConfirmed(boolean isBudgetConfirmed) {
		this.isBudgetConfirmed = isBudgetConfirmed;
	}

	public ArrayOf_xsd_anyType getSubjectDetails() {
		return this.subjectDetails;
	}

	public void setSubjectDetails(ArrayOf_xsd_anyType subjectDetails) {
		this.subjectDetails = subjectDetails;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getIsisUserWhoPlacedOrder() {
		return this.isisUserWhoPlacedOrder;
	}

	public void setIsisUserWhoPlacedOrder(String isisUserWhoPlacedOrder) {
		this.isisUserWhoPlacedOrder = isisUserWhoPlacedOrder;
	}

	public String getIsisUserEmailId() {
		return this.isisUserEmailId;
	}

	public void setIsisUserEmailId(String isisUserEmailId) {
		this.isisUserEmailId = isisUserEmailId;
	}

	public boolean isIsSLSubReportType() {
		return this.isSLSubReportType;
	}

	public void setIsSLSubReportType(boolean isSLSubReportType) {
		this.isSLSubReportType = isSLSubReportType;
	}

	public int getClSubreportID() {
		return this.clSubreportID;
	}

	public void setClSubreportID(int clSubreportID) {
		this.clSubreportID = clSubreportID;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CaseDetailsVO)) {
			return false;
		} else {
			CaseDetailsVO other = (CaseDetailsVO) obj;
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
						&& (this.assignmentType == null && other.getAssignmentType() == null
								|| this.assignmentType != null && this.assignmentType.equals(other.getAssignmentType()))
						&& (this.branchType == null && other.getBranchType() == null
								|| this.branchType != null && this.branchType.equals(other.getBranchType()))
						&& this.budget == other.getBudget() && this.bulkOrder == other.getBulkOrder()
						&& (this.bulkOrderId == null && other.getBulkOrderId() == null
								|| this.bulkOrderId != null && this.bulkOrderId.equals(other.getBulkOrderId()))
						&& (this.caseFlag == null && other.getCaseFlag() == null
								|| this.caseFlag != null && this.caseFlag.equals(other.getCaseFlag()))
						&& (this.caseManager == null && other.getCaseManager() == null
								|| this.caseManager != null && this.caseManager.equals(other.getCaseManager()))
						&& (this.clientCode == null && other.getClientCode() == null
								|| this.clientCode != null && this.clientCode.equals(other.getClientCode()))
						&& (this.clientFinalDueDate == null && other.getClientFinalDueDate() == null
								|| this.clientFinalDueDate != null
										&& this.clientFinalDueDate.equals(other.getClientFinalDueDate()))
						&& (this.clientReferenceNumber == null && other.getClientReferenceNumber() == null
								|| this.clientReferenceNumber != null
										&& this.clientReferenceNumber.equals(other.getClientReferenceNumber()))
						&& (this.currencyCode == null && other.getCurrencyCode() == null
								|| this.currencyCode != null && this.currencyCode.equals(other.getCurrencyCode()))
						&& this.expressCase == other.getExpressCase()
						&& (this.fileDetails == null && other.getFileDetails() == null
								|| this.fileDetails != null && this.fileDetails.equals(other.getFileDetails()))
						&& (this.officeAssignment == null && other.getOfficeAssignment() == null
								|| this.officeAssignment != null
										&& this.officeAssignment.equals(other.getOfficeAssignment()))
						&& (this.orderGUID == null && other.getOrderGUID() == null
								|| this.orderGUID != null && this.orderGUID.equals(other.getOrderGUID()))
						&& (this.orderReceiptDate == null && other.getOrderReceiptDate() == null
								|| this.orderReceiptDate != null
										&& this.orderReceiptDate.equals(other.getOrderReceiptDate()))
						&& (this.priority == null && other.getPriority() == null
								|| this.priority != null && this.priority.equals(other.getPriority()))
						&& (this.reportTypeId == null && other.getReportTypeId() == null
								|| this.reportTypeId != null && this.reportTypeId.equals(other.getReportTypeId()))
						&& (this.researchFinalDueDate == null && other.getResearchFinalDueDate() == null
								|| this.researchFinalDueDate != null
										&& this.researchFinalDueDate.equals(other.getResearchFinalDueDate()))
						&& (this.specialInstruction == null && other.getSpecialInstruction() == null
								|| this.specialInstruction != null
										&& this.specialInstruction.equals(other.getSpecialInstruction()))
						&& (this.subReportType == null && other.getSubReportType() == null
								|| this.subReportType != null && this.subReportType.equals(other.getSubReportType()))
						&& this.isBudgetConfirmed == other.isIsBudgetConfirmed()
						&& (this.subjectDetails == null && other.getSubjectDetails() == null
								|| this.subjectDetails != null && this.subjectDetails.equals(other.getSubjectDetails()))
						&& (this.taxCode == null && other.getTaxCode() == null
								|| this.taxCode != null && this.taxCode.equals(other.getTaxCode()))
						&& (this.isisUserWhoPlacedOrder == null && other.getIsisUserWhoPlacedOrder() == null
								|| this.isisUserWhoPlacedOrder != null
										&& this.isisUserWhoPlacedOrder.equals(other.getIsisUserWhoPlacedOrder()))
						&& (this.isisUserEmailId == null && other.getIsisUserEmailId() == null
								|| this.isisUserEmailId != null
										&& this.isisUserEmailId.equals(other.getIsisUserEmailId()))
						&& this.isSLSubReportType == other.isIsSLSubReportType()
						&& this.clSubreportID == other.getClSubreportID()
						&& (this.clSubreportCode == null && other.getClSubreportCode() == null
								|| this.clSubreportCode != null
										&& this.clSubreportCode.equals(other.getClSubreportCode()));
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

			if (this.getAssignmentType() != null) {
				_hashCode += this.getAssignmentType().hashCode();
			}

			if (this.getBranchType() != null) {
				_hashCode += this.getBranchType().hashCode();
			}

			_hashCode += (new Float(this.getBudget())).hashCode();
			_hashCode += this.getBulkOrder();
			if (this.getBulkOrderId() != null) {
				_hashCode += this.getBulkOrderId().hashCode();
			}

			if (this.getCaseFlag() != null) {
				_hashCode += this.getCaseFlag().hashCode();
			}

			if (this.getCaseManager() != null) {
				_hashCode += this.getCaseManager().hashCode();
			}

			if (this.getClientCode() != null) {
				_hashCode += this.getClientCode().hashCode();
			}

			if (this.getClientFinalDueDate() != null) {
				_hashCode += this.getClientFinalDueDate().hashCode();
			}

			if (this.getClientReferenceNumber() != null) {
				_hashCode += this.getClientReferenceNumber().hashCode();
			}

			if (this.getCurrencyCode() != null) {
				_hashCode += this.getCurrencyCode().hashCode();
			}

			_hashCode += this.getExpressCase();
			if (this.getFileDetails() != null) {
				_hashCode += this.getFileDetails().hashCode();
			}

			if (this.getOfficeAssignment() != null) {
				_hashCode += this.getOfficeAssignment().hashCode();
			}

			if (this.getOrderGUID() != null) {
				_hashCode += this.getOrderGUID().hashCode();
			}

			if (this.getOrderReceiptDate() != null) {
				_hashCode += this.getOrderReceiptDate().hashCode();
			}

			if (this.getPriority() != null) {
				_hashCode += this.getPriority().hashCode();
			}

			if (this.getReportTypeId() != null) {
				_hashCode += this.getReportTypeId().hashCode();
			}

			if (this.getResearchFinalDueDate() != null) {
				_hashCode += this.getResearchFinalDueDate().hashCode();
			}

			if (this.getSpecialInstruction() != null) {
				_hashCode += this.getSpecialInstruction().hashCode();
			}

			if (this.getSubReportType() != null) {
				_hashCode += this.getSubReportType().hashCode();
			}

			_hashCode += (this.isIsBudgetConfirmed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
			if (this.getSubjectDetails() != null) {
				_hashCode += this.getSubjectDetails().hashCode();
			}

			if (this.getTaxCode() != null) {
				_hashCode += this.getTaxCode().hashCode();
			}

			if (this.getIsisUserWhoPlacedOrder() != null) {
				_hashCode += this.getIsisUserWhoPlacedOrder().hashCode();
			}

			if (this.getIsisUserEmailId() != null) {
				_hashCode += this.getIsisUserEmailId().hashCode();
			}

			_hashCode += (this.isIsSLSubReportType() ? Boolean.TRUE : Boolean.FALSE).hashCode();
			_hashCode += this.getClSubreportID();
			if (this.getClSubreportCode() != null) {
				_hashCode += this.getClSubreportCode().hashCode();
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