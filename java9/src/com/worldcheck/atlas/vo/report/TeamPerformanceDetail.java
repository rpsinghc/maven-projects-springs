package com.worldcheck.atlas.vo.report;

public class TeamPerformanceDetail {
	private String userLoginId;
	private String team;
	private int teamReportCount;
	private float teamJLP;

	public String getUserLoginId() {
		return this.userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getTeam() {
		return this.team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getTeamReportCount() {
		return this.teamReportCount;
	}

	public void setTeamReportCount(int teamReportCount) {
		this.teamReportCount = teamReportCount;
	}

	public float getTeamJLP() {
		return this.teamJLP;
	}

	public void setTeamJLP(float teamJLP) {
		this.teamJLP = teamJLP;
	}
}