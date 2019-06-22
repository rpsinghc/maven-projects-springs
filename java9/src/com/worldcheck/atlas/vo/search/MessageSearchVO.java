package com.worldcheck.atlas.vo.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import java.util.Date;

public class MessageSearchVO extends SearchCriteria {
	private int id;
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
	private long recCaseSchedulerId;
	private long recClientCaseId;
	private Date acknowledgeDate;
	private Long lastRecurrenceNumber;
	private String clientCode;
	private String reportType;
	private String requestRecvdDate;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentAlertId() {
		return this.parentAlertId;
	}

	public void setParentAlertId(int parentAlertId) {
		this.parentAlertId = parentAlertId;
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

	public Date getAcknowledgeDate() {
		return this.acknowledgeDate;
	}

	public void setAcknowledgeDate(Date acknowledgeDate) {
		this.acknowledgeDate = acknowledgeDate;
	}

	public Long getLastRecurrenceNumber() {
		return this.lastRecurrenceNumber;
	}

	public void setLastRecurrenceNumber(Long lastRecurrenceNumber) {
		this.lastRecurrenceNumber = lastRecurrenceNumber;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getRequestRecvdDate() {
		return this.requestRecvdDate;
	}

	public void setRequestRecvdDate(String requestRecvdDate) {
		this.requestRecvdDate = requestRecvdDate;
	}
}