package com.worldcheck.atlas.bl.flowcontroller.ibatis;

public class FlowLockInfoVO {
	private String crn;
	private String ipAddress;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}