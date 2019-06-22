package com.worldcheck.atlas.vo.masters;

public class UserRoleMapVO {
	private String roleId;
	private String permissionName;
	private long permissionId;
	private String action;
	private String rDescription;

	public String getrDescription() {
		return this.rDescription;
	}

	public void setrDescription(String rDescription) {
		this.rDescription = rDescription;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public long getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}