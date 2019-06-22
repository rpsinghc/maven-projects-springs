package com.worldcheck.atlas.sbm.acl;

import com.worldcheck.atlas.dao.sbm.acl.ACLPermissionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.sbm.acl.RolePermissionVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ManagerAcl {
	private final ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.acl.ManagerAcl");
	private ACLPermissionDAO aclPermissionDAO = null;
	private static final String[] aclActions = new String[]{"A", "O", "R", "NA"};

	public void setAclPermissionDAO(ACLPermissionDAO aclPermissionDAO) {
		this.aclPermissionDAO = aclPermissionDAO;
	}

	public Map getACLPermissionsByUserId(String userId) throws CMSException {
		HashMap resuleMap = new HashMap();

		try {
			List permissionList = this.aclPermissionDAO.getACLPermissionFromDB(userId);
			if (permissionList != null && !permissionList.isEmpty()) {
				HashMap<String, String> idHsMap = new HashMap();
				HashMap<String, String> nameHsMap = new HashMap();
				List roleList = new ArrayList();
				Iterator iterator = permissionList.iterator();

				while (iterator.hasNext()) {
					RolePermissionVo rolePer = (RolePermissionVo) iterator.next();
					if (!roleList.contains(rolePer.getRoleId())) {
						roleList.add(rolePer.getRoleId());
					}

					if (checkPriority(idHsMap, String.valueOf(rolePer.getPermissionId()), rolePer.getAction())) {
						idHsMap.put(String.valueOf(rolePer.getPermissionId()), rolePer.getAction());
						nameHsMap.put(rolePer.getPermissionName(), rolePer.getAction());
					}
				}

				resuleMap.put("permissionMap", nameHsMap);
				resuleMap.put("roleList", roleList);
			}

			return resuleMap;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private static boolean checkPriority(HashMap sqlMap, String permissionId, String action) {
		boolean isHighPriorityAction = false;
		if (sqlMap.isEmpty()) {
			return true;
		} else {
			Iterator it = sqlMap.entrySet().iterator();

			while (it.hasNext()) {
				Entry pairs = (Entry) it.next();
				if (permissionId.equals(pairs.getKey().toString())) {
					String pairValue = (String) pairs.getValue();
					if (pairValue.equals(aclActions[0])) {
						return false;
					}

					int newActionIndex = 0;
					int oldActionIndex = 0;

					for (int i = 0; i < aclActions.length; ++i) {
						if (action.indexOf(aclActions[i]) == 0) {
							newActionIndex = i;
						}

						if (pairs.getValue().toString().indexOf(aclActions[i]) == 0) {
							oldActionIndex = i;
						}
					}

					if (newActionIndex < oldActionIndex) {
						isHighPriorityAction = true;
						return isHighPriorityAction;
					}

					isHighPriorityAction = false;
					return isHighPriorityAction;
				}
			}

			return true;
		}
	}
}