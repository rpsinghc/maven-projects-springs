package com.worldcheck.atlas.vo;

public class TeamOfficeVO {
	private String office;
	private String teamTypeId;
	private long reId;
	private String countryCode;
	private String jlPoints;

	public long getReId() {
		return this.reId;
	}

	public void setReId(long reId) {
		this.reId = reId;
	}

	public void setTeamTypeId(String teamTypeId) {
		this.teamTypeId = teamTypeId;
	}

	public String getTeamTypeId() {
		return this.teamTypeId;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getOffice() {
		return this.office;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setJlPoints(String jlPoints) {
		this.jlPoints = jlPoints;
	}

	public String getJlPoints() {
		return this.jlPoints;
	}
}