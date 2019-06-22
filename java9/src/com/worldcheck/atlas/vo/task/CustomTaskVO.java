package com.worldcheck.atlas.vo.task;

public class CustomTaskVO {
	private String crn;
	private int processCycle;
	private String cycle;
	private String completeTaskStatus;
	private String officeTaskStatus;
	private String invoiceTaskStatus;
	private String consolidationTaskStatus;
	private String clientSubmissionTaskStatus;
	private String awaitingTaskStatus;
	private String taskStatus;
	private long workItemID;
	private long processTemplateID;
	private long processInstanceID;
	private String workStepName;
	private String workItemName;
	private String startTime;
	private String endTime;
	private String dueDate;
	private String performer;
	private String previousPerformer;
	private String priority;
	private String status;
	private String primarySubjectCountry;
	private String primarySubject;
	private String caseStatus;
	private String analysts;

	public String getAnalysts() {
		return this.analysts;
	}

	public void setAnalysts(String analysts) {
		this.analysts = analysts;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(int processCycle) {
		this.processCycle = processCycle;
	}

	public String getCycle() {
		return this.cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getCompleteTaskStatus() {
		return this.completeTaskStatus;
	}

	public void setCompleteTaskStatus(String completeTaskStatus) {
		this.completeTaskStatus = completeTaskStatus;
	}

	public String getOfficeTaskStatus() {
		return this.officeTaskStatus;
	}

	public void setOfficeTaskStatus(String officeTaskStatus) {
		this.officeTaskStatus = officeTaskStatus;
	}

	public String getInvoiceTaskStatus() {
		return this.invoiceTaskStatus;
	}

	public void setInvoiceTaskStatus(String invoiceTaskStatus) {
		this.invoiceTaskStatus = invoiceTaskStatus;
	}

	public String getConsolidationTaskStatus() {
		return this.consolidationTaskStatus;
	}

	public void setConsolidationTaskStatus(String consolidationTaskStatus) {
		this.consolidationTaskStatus = consolidationTaskStatus;
	}

	public String getClientSubmissionTaskStatus() {
		return this.clientSubmissionTaskStatus;
	}

	public void setClientSubmissionTaskStatus(String clientSubmissionTaskStatus) {
		this.clientSubmissionTaskStatus = clientSubmissionTaskStatus;
	}

	public String getAwaitingTaskStatus() {
		return this.awaitingTaskStatus;
	}

	public void setAwaitingTaskStatus(String awaitingTaskStatus) {
		this.awaitingTaskStatus = awaitingTaskStatus;
	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public long getWorkItemID() {
		return this.workItemID;
	}

	public void setWorkItemID(long workItemID) {
		this.workItemID = workItemID;
	}

	public long getProcessTemplateID() {
		return this.processTemplateID;
	}

	public void setProcessTemplateID(long processTemplateID) {
		this.processTemplateID = processTemplateID;
	}

	public long getProcessInstanceID() {
		return this.processInstanceID;
	}

	public void setProcessInstanceID(long processInstanceID) {
		this.processInstanceID = processInstanceID;
	}

	public String getWorkStepName() {
		return this.workStepName;
	}

	public void setWorkStepName(String workStepName) {
		this.workStepName = workStepName;
	}

	public String getWorkItemName() {
		return this.workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public String getPreviousPerformer() {
		return this.previousPerformer;
	}

	public void setPreviousPerformer(String previousPerformer) {
		this.previousPerformer = previousPerformer;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPrimarySubjectCountry() {
		return this.primarySubjectCountry;
	}

	public void setPrimarySubjectCountry(String primarySubjectCountry) {
		this.primarySubjectCountry = primarySubjectCountry;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
}