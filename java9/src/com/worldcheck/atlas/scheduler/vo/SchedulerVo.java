package com.worldcheck.atlas.scheduler.vo;

import java.util.Date;
import java.util.List;

public class SchedulerVo {
	private long recCaseSchedulerId;
	private long recClientCaseId;
	private String description;
	private Date acknowledgeDate;
	private Date expiresOn;
	private String CRN;
	private String clientCode;
	private String reportType;
	private Long lastRecurrenceNumber;
	private String updatedBy;
	private Date updatedOn;
	private String finalDueDays;
	private String interimClientDueDays1;
	private String interimClientDueDays2;
	private String officeName;
	private String requestRecvdDate;
	private String clientInterm1Date;
	private String clientInterm2Date;
	private String clientFinalDate;
	private String rschInterm1Date;
	private String rschInterm2Date;
	private String rschFinalDate;
	private List<String> listOfEnteries;

	public List<String> getListOfEnteries() {
		return this.listOfEnteries;
	}

	public void setListOfEnteries(List<String> listOfEnteries) {
		this.listOfEnteries = listOfEnteries;
	}

	public long getRecCaseSchedulerId() {
		return this.recCaseSchedulerId;
	}

	public void setRecCaseSchedulerId(long recCaseSchedulerId) {
		this.recCaseSchedulerId = recCaseSchedulerId;
	}

	public long getRecClientCaseId() {
		return this.recClientCaseId;
	}

	public void setRecClientCaseId(long recClientCaseId) {
		this.recClientCaseId = recClientCaseId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Date getAcknowledgeDate() {
		return this.acknowledgeDate;
	}

	public void setAcknowledgeDate(Date acknowledgeDate) {
		this.acknowledgeDate = acknowledgeDate;
	}

	public Date getExpiresOn() {
		return this.expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Long getLastRecurrenceNumber() {
		return this.lastRecurrenceNumber;
	}

	public void setLastRecurrenceNumber(Long lastRecurrenceNumber) {
		this.lastRecurrenceNumber = lastRecurrenceNumber;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getFinalDueDays() {
		return this.finalDueDays;
	}

	public void setFinalDueDays(String finalDueDays) {
		this.finalDueDays = finalDueDays;
	}

	public String getInterimClientDueDays1() {
		return this.interimClientDueDays1;
	}

	public void setInterimClientDueDays1(String interimClientDueDays1) {
		this.interimClientDueDays1 = interimClientDueDays1;
	}

	public String getInterimClientDueDays2() {
		return this.interimClientDueDays2;
	}

	public void setInterimClientDueDays2(String interimClientDueDays2) {
		this.interimClientDueDays2 = interimClientDueDays2;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getRequestRecvdDate() {
		return this.requestRecvdDate;
	}

	public void setRequestRecvdDate(String requestRecvdDate) {
		this.requestRecvdDate = requestRecvdDate;
	}

	public String getClientInterm1Date() {
		return this.clientInterm1Date;
	}

	public void setClientInterm1Date(String clientInterm1Date) {
		this.clientInterm1Date = clientInterm1Date;
	}

	public String getClientInterm2Date() {
		return this.clientInterm2Date;
	}

	public void setClientInterm2Date(String clientInterm2Date) {
		this.clientInterm2Date = clientInterm2Date;
	}

	public String getClientFinalDate() {
		return this.clientFinalDate;
	}

	public void setClientFinalDate(String clientFinalDate) {
		this.clientFinalDate = clientFinalDate;
	}

	public String getRschInterm1Date() {
		return this.rschInterm1Date;
	}

	public void setRschInterm1Date(String rschInterm1Date) {
		this.rschInterm1Date = rschInterm1Date;
	}

	public String getRschInterm2Date() {
		return this.rschInterm2Date;
	}

	public void setRschInterm2Date(String rschInterm2Date) {
		this.rschInterm2Date = rschInterm2Date;
	}

	public String getRschFinalDate() {
		return this.rschFinalDate;
	}

	public void setRschFinalDate(String rschFinalDate) {
		this.rschFinalDate = rschFinalDate;
	}
}