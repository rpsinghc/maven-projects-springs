package com.worldcheck.atlas.vo.task;

import java.util.Map;

public class MyTaskPageVO {
	private String crn;
	private String expressCase;
	private String cinterim2;
	private String cinterim1;
	private String cfinal;
	private String clientfinalDueDate;
	private String casePriority;
	private String country;
	private String taskName;
	private String status;
	private String currentCycle;
	private String primarySubject;
	private String worlItemName;
	private String appName;
	private Long pid;
	private Long processInstanceId;
	private String encodedTask;
	private String performer;
	private String loggedInUser;
	private Boolean isOverdue;
	private String teamTypeList;
	private Map custmDSMap;
	private String teamType;
	private String workStepStatus;
	private String teamName;
	private String currentTask;
	private String userFullName;
	private String encodedCRN;
	private String encodedProcessCycle;
	private String encodedTaskStatus;
	private String caseStatus;
	private String caseCreationDate;
	private String analysts;
	private String subordinateUserId;
	private String caseManager;
	private String startClientDueDate;
	private String endClientDueDate;
	private String workItemId;
	private String dueDateForCurrentCycle;
	private int start;
	private int limit;

	public String getWorkItemId() {
		return this.workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getAnalysts() {
		return this.analysts;
	}

	public void setAnalysts(String analysts) {
		this.analysts = analysts;
	}

	public String getStartClientDueDate() {
		return this.startClientDueDate;
	}

	public void setStartClientDueDate(String startClientDueDate) {
		this.startClientDueDate = startClientDueDate;
	}

	public String getEndClientDueDate() {
		return this.endClientDueDate;
	}

	public void setEndClientDueDate(String endClientDueDate) {
		this.endClientDueDate = endClientDueDate;
	}

	public String getCaseManager() {
		return this.caseManager;
	}

	public void setCaseManager(String caseManager) {
		this.caseManager = caseManager;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getEncodedCRN() {
		return this.encodedCRN;
	}

	public void setEncodedCRN(String encodedCRN) {
		this.encodedCRN = encodedCRN;
	}

	public String getEncodedProcessCycle() {
		return this.encodedProcessCycle;
	}

	public void setEncodedProcessCycle(String encodedProcessCycle) {
		this.encodedProcessCycle = encodedProcessCycle;
	}

	public String getEncodedTaskStatus() {
		return this.encodedTaskStatus;
	}

	public void setEncodedTaskStatus(String encodedTaskStatus) {
		this.encodedTaskStatus = encodedTaskStatus;
	}

	public Long getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getWorkStepStatus() {
		return this.workStepStatus;
	}

	public void setWorkStepStatus(String workStepStatus) {
		this.workStepStatus = workStepStatus;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public Map getCustmDSMap() {
		return this.custmDSMap;
	}

	public void setCustmDSMap(Map custmDSMap) {
		this.custmDSMap = custmDSMap;
	}

	public String getTeamTypeList() {
		return this.teamTypeList;
	}

	public void setTeamTypeList(String teamTypeList) {
		this.teamTypeList = teamTypeList;
	}

	public Boolean getIsOverdue() {
		return this.isOverdue;
	}

	public void setIsOverdue(Boolean isOverdue) {
		this.isOverdue = isOverdue;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public String getLoggedInUser() {
		return this.loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public String getEncodedTask() {
		return this.encodedTask;
	}

	public void setEncodedTask(String encodedTask) {
		this.encodedTask = encodedTask;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getWorlItemName() {
		return this.worlItemName;
	}

	public void setWorlItemName(String worlItemName) {
		this.worlItemName = worlItemName;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getExpressCase() {
		return this.expressCase;
	}

	public void setExpressCase(String expressCase) {
		this.expressCase = expressCase;
	}

	public String getCasePriority() {
		return this.casePriority;
	}

	public String getCinterim2() {
		return this.cinterim2;
	}

	public void setCinterim2(String cinterim2) {
		this.cinterim2 = cinterim2;
	}

	public String getCinterim1() {
		return this.cinterim1;
	}

	public void setCinterim1(String cinterim1) {
		this.cinterim1 = cinterim1;
	}

	public String getCfinal() {
		return this.cfinal;
	}

	public void setCfinal(String cfinal) {
		this.cfinal = cfinal;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentCycle() {
		return this.currentCycle;
	}

	public void setCurrentCycle(String currentCycle) {
		this.currentCycle = currentCycle;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getCurrentTask() {
		return this.currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	public String getUserFullName() {
		return this.userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getSubordinateUserId() {
		return this.subordinateUserId;
	}

	public void setSubordinateUserId(String subordinateUserId) {
		this.subordinateUserId = subordinateUserId;
	}

	public void setClientfinalDueDate(String clientfinalDueDate) {
		this.clientfinalDueDate = clientfinalDueDate;
	}

	public String getClientfinalDueDate() {
		return this.clientfinalDueDate;
	}

	public void setDueDateForCurrentCycle(String dueDateForCurrentCycle) {
		this.dueDateForCurrentCycle = dueDateForCurrentCycle;
	}

	public String getDueDateForCurrentCycle() {
		return this.dueDateForCurrentCycle;
	}

	public void setCaseCreationDate(String caseCreationDate) {
		this.caseCreationDate = caseCreationDate;
	}

	public String getCaseCreationDate() {
		return this.caseCreationDate;
	}
}