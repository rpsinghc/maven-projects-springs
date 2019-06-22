package com.worldcheck.atlas.vo.masters;

public class ClientContactVO {
	private String name;
	private String phone;
	private String fax;
	private String email;
	private String remark;
	private String clientCode;
	private String userName;
	private int clientContactId;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getClientContactId() {
		return this.clientContactId;
	}

	public void setClientContactId(int clientContactId) {
		this.clientContactId = clientContactId;
	}
}