package com.worldcheck.atlas.vo.notification;

import java.io.Serializable;
import java.util.List;

public class NotificationVO implements Serializable {
	private static final long serialVersionUID = -8904852345703681392L;
	private int id;
	private int falg;
	private List<String> alertName;
	private int parentAlertId;
	private String name;
	private String message;
	private String description;
	private String recipient;
	private String recipientFullName;
	private String sender;
	private String senderFullName;
	private String recvTime;
	private String ackDate;
	private String primarySub;
	private String crn;
	private String mgrId;
	private String mainAnalyst;
	private String reviewer1;
	private String reviewer2;
	private String reviewer3;
	private String mainAnalystFullName;
	private String reviewer1FullName;
	private String reviewer2FullName;
	private String reviewer3FullName;
	private String userFullName;
	private String userName;
	private String sysUserName;
	private int minRecord;
	private int maxRecord;
	private String sortOrder;
	private String sortColumnName;
	private String sortType;
	private List<String> sendToUserList;
	private String dsName;
	private String processName;
	private String sentFromTime;
	private String sentToTime;
	private String caseStatus;
	private String filter;
	private String ackFromDate;
	private String ackToDate;
	private String notificationFromDate;
	private String notificationToDate;
	private String clientCode;
	private String recurrenceAcknowledgeDateFrom;
	private String recurrenceAcknowledgeDateTo;
	private String reportType;

	public String getAckFromDate() {
		return this.ackFromDate;
	}

	public void setAckFromDate(String ackFromDate) {
		this.ackFromDate = ackFromDate;
	}

	public String getAckToDate() {
		return this.ackToDate;
	}

	public void setAckToDate(String ackToDate) {
		this.ackToDate = ackToDate;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getNotificationFromDate() {
		return this.notificationFromDate;
	}

	public void setNotificationFromDate(String notificationFromDate) {
		this.notificationFromDate = notificationFromDate;
	}

	public String getNotificationToDate() {
		return this.notificationToDate;
	}

	public void setNotificationToDate(String notificationToDate) {
		this.notificationToDate = notificationToDate;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSentFromTime() {
		return this.sentFromTime;
	}

	public void setSentFromTime(String sentFromTime) {
		this.sentFromTime = sentFromTime;
	}

	public String getSentToTime() {
		return this.sentToTime;
	}

	public void setSentToTime(String sentToTime) {
		this.sentToTime = sentToTime;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public List<String> getAlertName() {
		return this.alertName;
	}

	public void setAlertName(List<String> alertName) {
		this.alertName = alertName;
	}

	public int getFalg() {
		return this.falg;
	}

	public void setFalg(int falg) {
		this.falg = falg;
	}

	public String getDsName() {
		return this.dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRecipientFullName() {
		return this.recipientFullName;
	}

	public void setRecipientFullName(String recipientFullName) {
		this.recipientFullName = recipientFullName;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderFullName() {
		return this.senderFullName;
	}

	public void setSenderFullName(String senderFullName) {
		this.senderFullName = senderFullName;
	}

	public String getRecvTime() {
		return this.recvTime;
	}

	public void setRecvTime(String recvTime) {
		this.recvTime = recvTime;
	}

	public String getAckDate() {
		return this.ackDate;
	}

	public void setAckDate(String ackDate) {
		this.ackDate = ackDate;
	}

	public String getPrimarySub() {
		return this.primarySub;
	}

	public void setPrimarySub(String primarySub) {
		this.primarySub = primarySub;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getMgrId() {
		return this.mgrId;
	}

	public void setMgrId(String mgrId) {
		this.mgrId = mgrId;
	}

	public String getMainAnalyst() {
		return this.mainAnalyst;
	}

	public void setMainAnalyst(String mainAnalyst) {
		this.mainAnalyst = mainAnalyst;
	}

	public String getReviewer1() {
		return this.reviewer1;
	}

	public void setReviewer1(String reviewer1) {
		this.reviewer1 = reviewer1;
	}

	public String getReviewer2() {
		return this.reviewer2;
	}

	public void setReviewer2(String reviewer2) {
		this.reviewer2 = reviewer2;
	}

	public String getReviewer3() {
		return this.reviewer3;
	}

	public void setReviewer3(String reviewer3) {
		this.reviewer3 = reviewer3;
	}

	public String getMainAnalystFullName() {
		return this.mainAnalystFullName;
	}

	public void setMainAnalystFullName(String mainAnalystFullName) {
		this.mainAnalystFullName = mainAnalystFullName;
	}

	public String getReviewer1FullName() {
		return this.reviewer1FullName;
	}

	public void setReviewer1FullName(String reviewer1FullName) {
		this.reviewer1FullName = reviewer1FullName;
	}

	public String getReviewer2FullName() {
		return this.reviewer2FullName;
	}

	public void setReviewer2FullName(String reviewer2FullName) {
		this.reviewer2FullName = reviewer2FullName;
	}

	public String getReviewer3FullName() {
		return this.reviewer3FullName;
	}

	public void setReviewer3FullName(String reviewer3FullName) {
		this.reviewer3FullName = reviewer3FullName;
	}

	public String getUserFullName() {
		return this.userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setParentAlertId(int parentAlertId) {
		this.parentAlertId = parentAlertId;
	}

	public int getParentAlertId() {
		return this.parentAlertId;
	}

	public String getSysUserName() {
		return this.sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public int getMinRecord() {
		return this.minRecord;
	}

	public void setMinRecord(int minRecord) {
		this.minRecord = minRecord;
	}

	public int getMaxRecord() {
		return this.maxRecord;
	}

	public void setMaxRecord(int maxRecord) {
		this.maxRecord = maxRecord;
	}

	public String getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public List<String> getSendToUserList() {
		return this.sendToUserList;
	}

	public void setSendToUserList(List<String> sendToUserList) {
		this.sendToUserList = sendToUserList;
	}

	public String getProcessName() {
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public void setRecurrenceAcknowledgeDateFrom(String recurrenceAcknowledgeDateFrom) {
		this.recurrenceAcknowledgeDateFrom = recurrenceAcknowledgeDateFrom;
	}

	public String getRecurrenceAcknowledgeDateFrom() {
		return this.recurrenceAcknowledgeDateFrom;
	}

	public void setrecurrenceAcknowledgeDateTo(String recurrenceAcknowledgeDateTo) {
		this.recurrenceAcknowledgeDateTo = recurrenceAcknowledgeDateTo;
	}

	public String getrecurrenceAcknowledgeDateTo() {
		return this.recurrenceAcknowledgeDateTo;
	}
}