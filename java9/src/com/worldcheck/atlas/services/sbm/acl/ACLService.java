package com.worldcheck.atlas.services.sbm.acl;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.acl.ManagerAcl;
import java.util.Map;

public class ACLService {
	private final ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.sbm.acl.ACLService");
	private ManagerAcl managerAcl = null;

	public void setManagerAcl(ManagerAcl managerAcl) {
		this.managerAcl = managerAcl;
	}

	public Map getACLPermissions(String userId) throws CMSException {
		try {
			return this.managerAcl.getACLPermissionsByUserId(userId);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}