package com.worldcheck.atlas.vo.task;

import java.util.List;

public class CaseStatusVO {
	private String teamName;
	private List<MyTaskPageVO> taskList;

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List getTaskList() {
		return this.taskList;
	}

	public void setTaskList(List taskList) {
		this.taskList = taskList;
	}
}