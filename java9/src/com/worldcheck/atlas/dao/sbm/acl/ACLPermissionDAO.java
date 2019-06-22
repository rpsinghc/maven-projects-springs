package com.worldcheck.atlas.dao.sbm.acl;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ACLPermissionDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.sbm.TaskManagementController");

	public List getACLPermissionFromDB(String userId) throws CMSException {
		List permissionList = null;

		try {
			permissionList = this.queryForList("AclPermission.all", new Integer(userId));
			return permissionList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}