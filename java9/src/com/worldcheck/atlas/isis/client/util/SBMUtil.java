package com.worldcheck.atlas.isis.client.util;

import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.rmi.RemoteException;
import java.util.HashMap;

public class SBMUtil {
	private static ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.client.util.SBMUtil");

	public static void createPIDForISISWebServiceCleint(HashMap instCreationParams) throws CMSException {
		logger.debug("Inside createPIDForISISWebServiceCleint method of SBMUtil");
		Session session = ResourceLocator.self().getSBMService().getSession();
		createProcessInstance(instCreationParams, session);
		ResourceLocator.self().getSBMService().closeSession(session);
	}

	public static HashMap<String, String> createProcessInstance(HashMap<String, Object> instCreationParams,
			Session session) throws CMSException {
		logger.debug("Inside createProcessInstance method of SBMUtil");
		ProcessInstance pInstance = null;
		HashMap<String, String> hmAttributes = new HashMap();
		long pID = 0L;

		try {
			pInstance = ProcessInstance.create(session, "AtlasQueueProcess", hmAttributes, instCreationParams, true);
			hmAttributes.clear();
			pID = pInstance.getID();
			logger.debug("pID is:::" + pID);
			hmAttributes.put("ProcessInstanceId", "" + new Long(pID));
			hmAttributes.put("Priority", pInstance.getPriority());
		} catch (RemoteException var7) {
			logger.debug("Inside Error..." + var7);
			var7.printStackTrace();
		}

		return hmAttributes;
	}
}