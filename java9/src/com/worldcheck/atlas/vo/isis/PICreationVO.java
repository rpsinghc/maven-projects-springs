package com.worldcheck.atlas.vo.isis;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class PICreationVO implements Serializable {
	private Vector BIAndVendorProcessDs;
	private Vector BIManager;
	private Vector BIVendorTaskTypeVector;
	private Vector PTOfficeHead;
	private Vector STOfficeHead;
	private Vector STReviewerVector;
	private Vector analyst;
	private String caseCreator;
	private String caseManager;
	private String caseOffice;
	private String caseStatus;
	private String financeHead;
	private Vector impactedTeams;
	private Boolean isAutoOA;
	private Boolean isAutoTeamAssign;
	private Boolean isCSApproved;
	private Boolean isCancelled;
	private Boolean isConsolidationApproved;
	private Boolean isFinalCycle;
	private Boolean isFinalReviewApproved;
	private Boolean isPullback;
	private Boolean isVendorProcess;
	private Boolean isVendorTaskGenerated;
	private String mainAnalyst;
	private Vector mainReviewerVector;
	private String officeHead;
	private Vector primaryAndSupportingProcessDs;
	private String primaryTeam;
	private String processCycle;
	private String researchStatus;
	private String reviewers;
	private String supportingTeam;
	private String taskStatus;
	private String teamAssignmentGroup;
	private Vector teamAssignmentTaskVector;
	private Long teamCount;
	private Vector teamResearchHeadVector;
	private Vector teamTypeVector;
	private Vector vendorManager;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(PICreationVO.class, true);

	public PICreationVO() {
	}

	public PICreationVO(Vector BIAndVendorProcessDs, Vector BIManager, Vector BIVendorTaskTypeVector,
			Vector PTOfficeHead, Vector STOfficeHead, Vector STReviewerVector, Vector analyst, String caseCreator,
			String caseManager, String caseOffice, String caseStatus, String financeHead, Vector impactedTeams,
			Boolean isAutoOA, Boolean isAutoTeamAssign, Boolean isCSApproved, Boolean isCancelled,
			Boolean isConsolidationApproved, Boolean isFinalCycle, Boolean isFinalReviewApproved, Boolean isPullback,
			Boolean isVendorProcess, Boolean isVendorTaskGenerated, String mainAnalyst, Vector mainReviewerVector,
			String officeHead, Vector primaryAndSupportingProcessDs, String primaryTeam, String processCycle,
			String researchStatus, String reviewers, String supportingTeam, String taskStatus,
			String teamAssignmentGroup, Vector teamAssignmentTaskVector, Long teamCount, Vector teamResearchHeadVector,
			Vector teamTypeVector, Vector vendorManager) {
		this.BIAndVendorProcessDs = BIAndVendorProcessDs;
		this.BIManager = BIManager;
		this.BIVendorTaskTypeVector = BIVendorTaskTypeVector;
		this.PTOfficeHead = PTOfficeHead;
		this.STOfficeHead = STOfficeHead;
		this.STReviewerVector = STReviewerVector;
		this.analyst = analyst;
		this.caseCreator = caseCreator;
		this.caseManager = caseManager;
		this.caseOffice = caseOffice;
		this.caseStatus = caseStatus;
		this.financeHead = financeHead;
		this.impactedTeams = impactedTeams;
		this.isAutoOA = isAutoOA;
		this.isAutoTeamAssign = isAutoTeamAssign;
		this.isCSApproved = isCSApproved;
		this.isCancelled = isCancelled;
		this.isConsolidationApproved = isConsolidationApproved;
		this.isFinalCycle = isFinalCycle;
		this.isFinalReviewApproved = isFinalReviewApproved;
		this.isPullback = isPullback;
		this.isVendorProcess = isVendorProcess;
		this.isVendorTaskGenerated = isVendorTaskGenerated;
		this.mainAnalyst = mainAnalyst;
		this.mainReviewerVector = mainReviewerVector;
		this.officeHead = officeHead;
		this.primaryAndSupportingProcessDs = primaryAndSupportingProcessDs;
		this.primaryTeam = primaryTeam;
		this.processCycle = processCycle;
		this.researchStatus = researchStatus;
		this.reviewers = reviewers;
		this.supportingTeam = supportingTeam;
		this.taskStatus = taskStatus;
		this.teamAssignmentGroup = teamAssignmentGroup;
		this.teamAssignmentTaskVector = teamAssignmentTaskVector;
		this.teamCount = teamCount;
		this.teamResearchHeadVector = teamResearchHeadVector;
		this.teamTypeVector = teamTypeVector;
		this.vendorManager = vendorManager;
	}

	public Vector getBIAndVendorProcessDs() {
		return this.BIAndVendorProcessDs;
	}

	public void setBIAndVendorProcessDs(Vector BIAndVendorProcessDs) {
		this.BIAndVendorProcessDs = BIAndVendorProcessDs;
	}

	public Vector getBIManager() {
		return this.BIManager;
	}

	public void setBIManager(Vector BIManager) {
		this.BIManager = BIManager;
	}

	public Vector getBIVendorTaskTypeVector() {
		return this.BIVendorTaskTypeVector;
	}

	public void setBIVendorTaskTypeVector(Vector BIVendorTaskTypeVector) {
		this.BIVendorTaskTypeVector = BIVendorTaskTypeVector;
	}

	public Vector getPTOfficeHead() {
		return this.PTOfficeHead;
	}

	public void setPTOfficeHead(Vector PTOfficeHead) {
		this.PTOfficeHead = PTOfficeHead;
	}

	public Vector getSTOfficeHead() {
		return this.STOfficeHead;
	}

	public void setSTOfficeHead(Vector STOfficeHead) {
		this.STOfficeHead = STOfficeHead;
	}

	public Vector getSTReviewerVector() {
		return this.STReviewerVector;
	}

	public void setSTReviewerVector(Vector STReviewerVector) {
		this.STReviewerVector = STReviewerVector;
	}

	public Vector getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(Vector analyst) {
		this.analyst = analyst;
	}

	public String getCaseCreator() {
		return this.caseCreator;
	}

	public void setCaseCreator(String caseCreator) {
		this.caseCreator = caseCreator;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public String getCaseOffice() {
		return this.caseOffice;
	}

	public void setCaseOffice(String caseOffice) {
		this.caseOffice = caseOffice;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getFinanceHead() {
		return this.financeHead;
	}

	public void setFinanceHead(String financeHead) {
		this.financeHead = financeHead;
	}

	public Vector getImpactedTeams() {
		return this.impactedTeams;
	}

	public void setImpactedTeams(Vector impactedTeams) {
		this.impactedTeams = impactedTeams;
	}

	public Boolean getIsAutoOA() {
		return this.isAutoOA;
	}

	public void setIsAutoOA(Boolean isAutoOA) {
		this.isAutoOA = isAutoOA;
	}

	public Boolean getIsAutoTeamAssign() {
		return this.isAutoTeamAssign;
	}

	public void setIsAutoTeamAssign(Boolean isAutoTeamAssign) {
		this.isAutoTeamAssign = isAutoTeamAssign;
	}

	public Boolean getIsCSApproved() {
		return this.isCSApproved;
	}

	public void setIsCSApproved(Boolean isCSApproved) {
		this.isCSApproved = isCSApproved;
	}

	public Boolean getIsCancelled() {
		return this.isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Boolean getIsConsolidationApproved() {
		return this.isConsolidationApproved;
	}

	public void setIsConsolidationApproved(Boolean isConsolidationApproved) {
		this.isConsolidationApproved = isConsolidationApproved;
	}

	public Boolean getIsFinalCycle() {
		return this.isFinalCycle;
	}

	public void setIsFinalCycle(Boolean isFinalCycle) {
		this.isFinalCycle = isFinalCycle;
	}

	public Boolean getIsFinalReviewApproved() {
		return this.isFinalReviewApproved;
	}

	public void setIsFinalReviewApproved(Boolean isFinalReviewApproved) {
		this.isFinalReviewApproved = isFinalReviewApproved;
	}

	public Boolean getIsPullback() {
		return this.isPullback;
	}

	public void setIsPullback(Boolean isPullback) {
		this.isPullback = isPullback;
	}

	public Boolean getIsVendorProcess() {
		return this.isVendorProcess;
	}

	public void setIsVendorProcess(Boolean isVendorProcess) {
		this.isVendorProcess = isVendorProcess;
	}

	public Boolean getIsVendorTaskGenerated() {
		return this.isVendorTaskGenerated;
	}

	public void setIsVendorTaskGenerated(Boolean isVendorTaskGenerated) {
		this.isVendorTaskGenerated = isVendorTaskGenerated;
	}

	public String getMainAnalyst() {
		return this.mainAnalyst;
	}

	public void setMainAnalyst(String mainAnalyst) {
		this.mainAnalyst = mainAnalyst;
	}

	public Vector getMainReviewerVector() {
		return this.mainReviewerVector;
	}

	public void setMainReviewerVector(Vector mainReviewerVector) {
		this.mainReviewerVector = mainReviewerVector;
	}

	public String getOfficeHead() {
		return this.officeHead;
	}

	public void setOfficeHead(String officeHead) {
		this.officeHead = officeHead;
	}

	public Vector getPrimaryAndSupportingProcessDs() {
		return this.primaryAndSupportingProcessDs;
	}

	public void setPrimaryAndSupportingProcessDs(Vector primaryAndSupportingProcessDs) {
		this.primaryAndSupportingProcessDs = primaryAndSupportingProcessDs;
	}

	public String getPrimaryTeam() {
		return this.primaryTeam;
	}

	public void setPrimaryTeam(String primaryTeam) {
		this.primaryTeam = primaryTeam;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}

	public String getResearchStatus() {
		return this.researchStatus;
	}

	public void setResearchStatus(String researchStatus) {
		this.researchStatus = researchStatus;
	}

	public String getReviewers() {
		return this.reviewers;
	}

	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}

	public String getSupportingTeam() {
		return this.supportingTeam;
	}

	public void setSupportingTeam(String supportingTeam) {
		this.supportingTeam = supportingTeam;
	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTeamAssignmentGroup() {
		return this.teamAssignmentGroup;
	}

	public void setTeamAssignmentGroup(String teamAssignmentGroup) {
		this.teamAssignmentGroup = teamAssignmentGroup;
	}

	public Vector getTeamAssignmentTaskVector() {
		return this.teamAssignmentTaskVector;
	}

	public void setTeamAssignmentTaskVector(Vector teamAssignmentTaskVector) {
		this.teamAssignmentTaskVector = teamAssignmentTaskVector;
	}

	public Long getTeamCount() {
		return this.teamCount;
	}

	public void setTeamCount(Long teamCount) {
		this.teamCount = teamCount;
	}

	public Vector getTeamResearchHeadVector() {
		return this.teamResearchHeadVector;
	}

	public void setTeamResearchHeadVector(Vector teamResearchHeadVector) {
		this.teamResearchHeadVector = teamResearchHeadVector;
	}

	public Vector getTeamTypeVector() {
		return this.teamTypeVector;
	}

	public void setTeamTypeVector(Vector teamTypeVector) {
		this.teamTypeVector = teamTypeVector;
	}

	public Vector getVendorManager() {
		return this.vendorManager;
	}

	public void setVendorManager(Vector vendorManager) {
		this.vendorManager = vendorManager;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof PICreationVO)) {
			return false;
		} else {
			PICreationVO other = (PICreationVO) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = (this.BIAndVendorProcessDs == null && other.getBIAndVendorProcessDs() == null
						|| this.BIAndVendorProcessDs != null
								&& this.BIAndVendorProcessDs.equals(other.getBIAndVendorProcessDs()))
						&& (this.BIManager == null && other.getBIManager() == null
								|| this.BIManager != null && this.BIManager.equals(other.getBIManager()))
						&& (this.BIVendorTaskTypeVector == null && other.getBIVendorTaskTypeVector() == null
								|| this.BIVendorTaskTypeVector != null
										&& this.BIVendorTaskTypeVector.equals(other.getBIVendorTaskTypeVector()))
						&& (this.PTOfficeHead == null && other.getPTOfficeHead() == null
								|| this.PTOfficeHead != null && this.PTOfficeHead.equals(other.getPTOfficeHead()))
						&& (this.STOfficeHead == null && other.getSTOfficeHead() == null
								|| this.STOfficeHead != null && this.STOfficeHead.equals(other.getSTOfficeHead()))
						&& (this.STReviewerVector == null && other.getSTReviewerVector() == null
								|| this.STReviewerVector != null
										&& this.STReviewerVector.equals(other.getSTReviewerVector()))
						&& (this.analyst == null && other.getAnalyst() == null
								|| this.analyst != null && this.analyst.equals(other.getAnalyst()))
						&& (this.caseCreator == null && other.getCaseCreator() == null
								|| this.caseCreator != null && this.caseCreator.equals(other.getCaseCreator()))
						&& (this.caseManager == null && other.getCaseManager() == null
								|| this.caseManager != null && this.caseManager.equals(other.getCaseManager()))
						&& (this.caseOffice == null && other.getCaseOffice() == null
								|| this.caseOffice != null && this.caseOffice.equals(other.getCaseOffice()))
						&& (this.caseStatus == null && other.getCaseStatus() == null
								|| this.caseStatus != null && this.caseStatus.equals(other.getCaseStatus()))
						&& (this.financeHead == null && other.getFinanceHead() == null
								|| this.financeHead != null && this.financeHead.equals(other.getFinanceHead()))
						&& (this.impactedTeams == null && other.getImpactedTeams() == null
								|| this.impactedTeams != null && this.impactedTeams.equals(other.getImpactedTeams()))
						&& (this.isAutoOA == null && other.getIsAutoOA() == null
								|| this.isAutoOA != null && this.isAutoOA.equals(other.getIsAutoOA()))
						&& (this.isAutoTeamAssign == null && other.getIsAutoTeamAssign() == null
								|| this.isAutoTeamAssign != null
										&& this.isAutoTeamAssign.equals(other.getIsAutoTeamAssign()))
						&& (this.isCSApproved == null && other.getIsCSApproved() == null
								|| this.isCSApproved != null && this.isCSApproved.equals(other.getIsCSApproved()))
						&& (this.isCancelled == null && other.getIsCancelled() == null
								|| this.isCancelled != null && this.isCancelled.equals(other.getIsCancelled()))
						&& (this.isConsolidationApproved == null && other.getIsConsolidationApproved() == null
								|| this.isConsolidationApproved != null
										&& this.isConsolidationApproved.equals(other.getIsConsolidationApproved()))
						&& (this.isFinalCycle == null && other.getIsFinalCycle() == null
								|| this.isFinalCycle != null && this.isFinalCycle.equals(other.getIsFinalCycle()))
						&& (this.isFinalReviewApproved == null && other.getIsFinalReviewApproved() == null
								|| this.isFinalReviewApproved != null
										&& this.isFinalReviewApproved.equals(other.getIsFinalReviewApproved()))
						&& (this.isPullback == null && other.getIsPullback() == null
								|| this.isPullback != null && this.isPullback.equals(other.getIsPullback()))
						&& (this.isVendorProcess == null && other.getIsVendorProcess() == null
								|| this.isVendorProcess != null
										&& this.isVendorProcess.equals(other.getIsVendorProcess()))
						&& (this.isVendorTaskGenerated == null && other.getIsVendorTaskGenerated() == null
								|| this.isVendorTaskGenerated != null
										&& this.isVendorTaskGenerated.equals(other.getIsVendorTaskGenerated()))
						&& (this.mainAnalyst == null && other.getMainAnalyst() == null
								|| this.mainAnalyst != null && this.mainAnalyst.equals(other.getMainAnalyst()))
						&& (this.mainReviewerVector == null && other.getMainReviewerVector() == null
								|| this.mainReviewerVector != null
										&& this.mainReviewerVector.equals(other.getMainReviewerVector()))
						&& (this.officeHead == null && other.getOfficeHead() == null
								|| this.officeHead != null && this.officeHead.equals(other.getOfficeHead()))
						&& (this.primaryAndSupportingProcessDs == null
								&& other.getPrimaryAndSupportingProcessDs() == null
								|| this.primaryAndSupportingProcessDs != null && this.primaryAndSupportingProcessDs
										.equals(other.getPrimaryAndSupportingProcessDs()))
						&& (this.primaryTeam == null && other.getPrimaryTeam() == null
								|| this.primaryTeam != null && this.primaryTeam.equals(other.getPrimaryTeam()))
						&& (this.processCycle == null && other.getProcessCycle() == null
								|| this.processCycle != null && this.processCycle.equals(other.getProcessCycle()))
						&& (this.researchStatus == null && other.getResearchStatus() == null
								|| this.researchStatus != null && this.researchStatus.equals(other.getResearchStatus()))
						&& (this.reviewers == null && other.getReviewers() == null
								|| this.reviewers != null && this.reviewers.equals(other.getReviewers()))
						&& (this.supportingTeam == null && other.getSupportingTeam() == null
								|| this.supportingTeam != null && this.supportingTeam.equals(other.getSupportingTeam()))
						&& (this.taskStatus == null && other.getTaskStatus() == null
								|| this.taskStatus != null && this.taskStatus.equals(other.getTaskStatus()))
						&& (this.teamAssignmentGroup == null && other.getTeamAssignmentGroup() == null
								|| this.teamAssignmentGroup != null
										&& this.teamAssignmentGroup.equals(other.getTeamAssignmentGroup()))
						&& (this.teamAssignmentTaskVector == null && other.getTeamAssignmentTaskVector() == null
								|| this.teamAssignmentTaskVector != null
										&& this.teamAssignmentTaskVector.equals(other.getTeamAssignmentTaskVector()))
						&& (this.teamCount == null && other.getTeamCount() == null
								|| this.teamCount != null && this.teamCount.equals(other.getTeamCount()))
						&& (this.teamResearchHeadVector == null && other.getTeamResearchHeadVector() == null
								|| this.teamResearchHeadVector != null
										&& this.teamResearchHeadVector.equals(other.getTeamResearchHeadVector()))
						&& (this.teamTypeVector == null && other.getTeamTypeVector() == null
								|| this.teamTypeVector != null && this.teamTypeVector.equals(other.getTeamTypeVector()))
						&& (this.vendorManager == null && other.getVendorManager() == null
								|| this.vendorManager != null && this.vendorManager.equals(other.getVendorManager()));
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
			if (this.getBIAndVendorProcessDs() != null) {
				_hashCode += this.getBIAndVendorProcessDs().hashCode();
			}

			if (this.getBIManager() != null) {
				_hashCode += this.getBIManager().hashCode();
			}

			if (this.getBIVendorTaskTypeVector() != null) {
				_hashCode += this.getBIVendorTaskTypeVector().hashCode();
			}

			if (this.getPTOfficeHead() != null) {
				_hashCode += this.getPTOfficeHead().hashCode();
			}

			if (this.getSTOfficeHead() != null) {
				_hashCode += this.getSTOfficeHead().hashCode();
			}

			if (this.getSTReviewerVector() != null) {
				_hashCode += this.getSTReviewerVector().hashCode();
			}

			if (this.getAnalyst() != null) {
				_hashCode += this.getAnalyst().hashCode();
			}

			if (this.getCaseCreator() != null) {
				_hashCode += this.getCaseCreator().hashCode();
			}

			if (this.getCaseManager() != null) {
				_hashCode += this.getCaseManager().hashCode();
			}

			if (this.getCaseOffice() != null) {
				_hashCode += this.getCaseOffice().hashCode();
			}

			if (this.getCaseStatus() != null) {
				_hashCode += this.getCaseStatus().hashCode();
			}

			if (this.getFinanceHead() != null) {
				_hashCode += this.getFinanceHead().hashCode();
			}

			if (this.getImpactedTeams() != null) {
				_hashCode += this.getImpactedTeams().hashCode();
			}

			if (this.getIsAutoOA() != null) {
				_hashCode += this.getIsAutoOA().hashCode();
			}

			if (this.getIsAutoTeamAssign() != null) {
				_hashCode += this.getIsAutoTeamAssign().hashCode();
			}

			if (this.getIsCSApproved() != null) {
				_hashCode += this.getIsCSApproved().hashCode();
			}

			if (this.getIsCancelled() != null) {
				_hashCode += this.getIsCancelled().hashCode();
			}

			if (this.getIsConsolidationApproved() != null) {
				_hashCode += this.getIsConsolidationApproved().hashCode();
			}

			if (this.getIsFinalCycle() != null) {
				_hashCode += this.getIsFinalCycle().hashCode();
			}

			if (this.getIsFinalReviewApproved() != null) {
				_hashCode += this.getIsFinalReviewApproved().hashCode();
			}

			if (this.getIsPullback() != null) {
				_hashCode += this.getIsPullback().hashCode();
			}

			if (this.getIsVendorProcess() != null) {
				_hashCode += this.getIsVendorProcess().hashCode();
			}

			if (this.getIsVendorTaskGenerated() != null) {
				_hashCode += this.getIsVendorTaskGenerated().hashCode();
			}

			if (this.getMainAnalyst() != null) {
				_hashCode += this.getMainAnalyst().hashCode();
			}

			if (this.getMainReviewerVector() != null) {
				_hashCode += this.getMainReviewerVector().hashCode();
			}

			if (this.getOfficeHead() != null) {
				_hashCode += this.getOfficeHead().hashCode();
			}

			if (this.getPrimaryAndSupportingProcessDs() != null) {
				_hashCode += this.getPrimaryAndSupportingProcessDs().hashCode();
			}

			if (this.getPrimaryTeam() != null) {
				_hashCode += this.getPrimaryTeam().hashCode();
			}

			if (this.getProcessCycle() != null) {
				_hashCode += this.getProcessCycle().hashCode();
			}

			if (this.getResearchStatus() != null) {
				_hashCode += this.getResearchStatus().hashCode();
			}

			if (this.getReviewers() != null) {
				_hashCode += this.getReviewers().hashCode();
			}

			if (this.getSupportingTeam() != null) {
				_hashCode += this.getSupportingTeam().hashCode();
			}

			if (this.getTaskStatus() != null) {
				_hashCode += this.getTaskStatus().hashCode();
			}

			if (this.getTeamAssignmentGroup() != null) {
				_hashCode += this.getTeamAssignmentGroup().hashCode();
			}

			if (this.getTeamAssignmentTaskVector() != null) {
				_hashCode += this.getTeamAssignmentTaskVector().hashCode();
			}

			if (this.getTeamCount() != null) {
				_hashCode += this.getTeamCount().hashCode();
			}

			if (this.getTeamResearchHeadVector() != null) {
				_hashCode += this.getTeamResearchHeadVector().hashCode();
			}

			if (this.getTeamTypeVector() != null) {
				_hashCode += this.getTeamTypeVector().hashCode();
			}

			if (this.getVendorManager() != null) {
				_hashCode += this.getVendorManager().hashCode();
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
		typeDesc.setXmlType(new QName("http://isis.vo.atlas.worldcheck.com", "PICreationVO"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("BIAndVendorProcessDs");
		elemField.setXmlName(new QName("", "BIAndVendorProcessDs"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("BIManager");
		elemField.setXmlName(new QName("", "BIManager"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("BIVendorTaskTypeVector");
		elemField.setXmlName(new QName("", "BIVendorTaskTypeVector"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("PTOfficeHead");
		elemField.setXmlName(new QName("", "PTOfficeHead"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("STOfficeHead");
		elemField.setXmlName(new QName("", "STOfficeHead"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("STReviewerVector");
		elemField.setXmlName(new QName("", "STReviewerVector"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("analyst");
		elemField.setXmlName(new QName("", "analyst"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("caseCreator");
		elemField.setXmlName(new QName("", "caseCreator"));
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
		elemField.setFieldName("caseOffice");
		elemField.setXmlName(new QName("", "caseOffice"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("caseStatus");
		elemField.setXmlName(new QName("", "caseStatus"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("financeHead");
		elemField.setXmlName(new QName("", "financeHead"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("impactedTeams");
		elemField.setXmlName(new QName("", "impactedTeams"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isAutoOA");
		elemField.setXmlName(new QName("", "isAutoOA"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isAutoTeamAssign");
		elemField.setXmlName(new QName("", "isAutoTeamAssign"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isCSApproved");
		elemField.setXmlName(new QName("", "isCSApproved"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isCancelled");
		elemField.setXmlName(new QName("", "isCancelled"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isConsolidationApproved");
		elemField.setXmlName(new QName("", "isConsolidationApproved"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isFinalCycle");
		elemField.setXmlName(new QName("", "isFinalCycle"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isFinalReviewApproved");
		elemField.setXmlName(new QName("", "isFinalReviewApproved"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isPullback");
		elemField.setXmlName(new QName("", "isPullback"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isVendorProcess");
		elemField.setXmlName(new QName("", "isVendorProcess"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("isVendorTaskGenerated");
		elemField.setXmlName(new QName("", "isVendorTaskGenerated"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("mainAnalyst");
		elemField.setXmlName(new QName("", "mainAnalyst"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("mainReviewerVector");
		elemField.setXmlName(new QName("", "mainReviewerVector"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("officeHead");
		elemField.setXmlName(new QName("", "officeHead"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("primaryAndSupportingProcessDs");
		elemField.setXmlName(new QName("", "primaryAndSupportingProcessDs"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("primaryTeam");
		elemField.setXmlName(new QName("", "primaryTeam"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("processCycle");
		elemField.setXmlName(new QName("", "processCycle"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("researchStatus");
		elemField.setXmlName(new QName("", "researchStatus"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("reviewers");
		elemField.setXmlName(new QName("", "reviewers"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("supportingTeam");
		elemField.setXmlName(new QName("", "supportingTeam"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("taskStatus");
		elemField.setXmlName(new QName("", "taskStatus"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("teamAssignmentGroup");
		elemField.setXmlName(new QName("", "teamAssignmentGroup"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("teamAssignmentTaskVector");
		elemField.setXmlName(new QName("", "teamAssignmentTaskVector"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("teamCount");
		elemField.setXmlName(new QName("", "teamCount"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("teamResearchHeadVector");
		elemField.setXmlName(new QName("", "teamResearchHeadVector"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("teamTypeVector");
		elemField.setXmlName(new QName("", "teamTypeVector"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("vendorManager");
		elemField.setXmlName(new QName("", "vendorManager"));
		elemField.setXmlType(new QName("http://xml.apache.org/xml-soap", "Vector"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}
}