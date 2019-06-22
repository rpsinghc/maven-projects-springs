package com.worldcheck.atlas.services.sbm.acl;

import com.worldcheck.atlas.dao.sbm.acl.ACLInsertPermissionDAO;
import java.util.HashMap;

public class ACLInsertPermissionService {
	private ACLInsertPermissionDAO aclDAO = null;

	public void setAclDAO(ACLInsertPermissionDAO aclDAO) {
		this.aclDAO = aclDAO;
	}

	public void insertPermission(String roleName, HashMap<String, String> roleAttributes) {
		this.setAclDAO(this.aclDAO);
	}
}