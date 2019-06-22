package com.worldcheck.atlas.vo.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;

public class TeamSearchVO extends SearchCriteria {
	private String primarySubject;
	private String processCycle;
	private String activeTask;
	private String reviewer1;
	private String reviewer2;
	private String reviewer3;
	private String intrimDueDate1;
	private String intrimDueDate2;
	private String finalDueDate;
	private String teamName;
	private String exactMatch;

	public String getExactMatch() {
		return this.exactMatch;
	}

	public void setExactMatch(String exactMatch) {
		this.exactMatch = exactMatch;
	}

	public String getPrimarySubject() {
		return this.primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}

	public String getActiveTask() {
		return this.activeTask;
	}

	public void setActiveTask(String activeTask) {
		this.activeTask = activeTask;
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

	public String getIntrimDueDate1() {
		return this.intrimDueDate1;
	}

	public void setIntrimDueDate1(String intrimDueDate1) {
		this.intrimDueDate1 = intrimDueDate1;
	}

	public String getIntrimDueDate2() {
		return this.intrimDueDate2;
	}

	public void setIntrimDueDate2(String intrimDueDate2) {
		this.intrimDueDate2 = intrimDueDate2;
	}

	public String getFinalDueDate() {
		return this.finalDueDate;
	}

	public void setFinalDueDate(String intrimDueDate3) {
		this.finalDueDate = intrimDueDate3;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}