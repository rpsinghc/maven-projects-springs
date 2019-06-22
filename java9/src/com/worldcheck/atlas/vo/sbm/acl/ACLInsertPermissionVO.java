package com.worldcheck.atlas.vo.sbm.acl;

import java.util.HashMap;

public class ACLInsertPermissionVO {
	private HashMap<String, String> permissions;

	public HashMap<String, String> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(HashMap<String, String> permissions) {
		this.permissions = permissions;
	}
}