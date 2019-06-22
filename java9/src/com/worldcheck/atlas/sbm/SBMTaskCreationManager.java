package com.worldcheck.atlas.sbm;

import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class SBMTaskCreationManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.SBMTaskCreationManager");

	public HashMap<String, String> createProcessInstance(HashMap<String, Object> instCreationParams, Session session)
			throws CMSException {
		this.logger.debug("session from request is " + session);
		HashMap<String, String> hmAttributes = new HashMap();
		long pID = 0L;

		try {
			ProcessInstance pInstance = null;
			pInstance = ProcessInstance.create(session, "CaseCreation", hmAttributes, instCreationParams, true);
			hmAttributes.clear();
			pID = pInstance.getID();
			hmAttributes.put("ProcessInstanceId", "" + new Long(pID));
			hmAttributes.put("Priority", pInstance.getPriority());
			this.logger.debug("New processInstance Id is ::" + pInstance.getID());
			return hmAttributes;
		} catch (RemoteException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public long createSubProcessInstance(String ptName, TeamAnalystMapping teamMap, Session session, long parentPID,
			String analyst, String cycleName) throws CMSException {
		long pID = 0L;

		try {
			ProcessInstance pInstance = null;
			pInstance = ProcessInstance.get(session, parentPID);
			Map<String, Object> parentDS = pInstance.getDataSlotValue();
			this.logger.debug("session from request is " + session);
			HashMap<String, String> hmAttributes = new HashMap();
			HashMap<String, Object> instCreationParams = new HashMap();
			instCreationParams.put("TeamTypeList", teamMap.getTeamName());
			instCreationParams.put("TeamCycleName", cycleName);
			instCreationParams.put("ProcessCycle", parentDS.get("ProcessCycle"));
			instCreationParams.put("SubReportType", parentDS.get("SubReportType"));
			instCreationParams.put("CaseCreationDate", parentDS.get("CaseCreationDate"));
			instCreationParams.put("RInterim2", parentDS.get("RInterim2"));
			instCreationParams.put("RInterim1", parentDS.get("RInterim1"));
			instCreationParams.put("RFinal", parentDS.get("RFinal"));
			instCreationParams.put("CInterim2", parentDS.get("CInterim2"));
			instCreationParams.put("CInterim1", parentDS.get("CInterim1"));
			instCreationParams.put("CFinal", parentDS.get("CFinal"));
			instCreationParams.put("ReportType", parentDS.get("ReportType"));
			instCreationParams.put("ReceivedDate", parentDS.get("ReceivedDate"));
			instCreationParams.put("PrimarySubject", parentDS.get("PrimarySubject"));
			instCreationParams.put("parentPID", parentPID);
			instCreationParams.put("ExpressCase", parentDS.get("ExpressCase"));
			instCreationParams.put("CRN", parentDS.get("CRN"));
			instCreationParams.put("ClientReference", parentDS.get("ClientReference"));
			instCreationParams.put("ClientName", parentDS.get("ClientName"));
			instCreationParams.put("CaseStatus", parentDS.get("CaseStatus"));
			instCreationParams.put("CaseManager", parentDS.get("CaseManager"));
			instCreationParams.put("CaseInformation", parentDS.get("CaseInformation"));
			instCreationParams.put("CaseCreator", parentDS.get("CaseCreator"));
			instCreationParams.put("CaseCreationDate", parentDS.get("CaseCreationDate"));
			instCreationParams.put("BranchOffice", parentDS.get("BranchOffice"));
			instCreationParams.put("customDSMap", parentDS.get("customDSMap"));
			if (null != analyst || !"".equalsIgnoreCase(analyst)) {
				instCreationParams.put("Analyst", analyst);
			}

			pInstance = ProcessInstance.create(session, ptName, hmAttributes, instCreationParams, true);
			hmAttributes.clear();
			pID = pInstance.getID();
		} catch (RemoteException var14) {
			throw new CMSException(this.logger, var14);
		}

		this.logger.debug("New processInstance Id is ::" + pID);
		return pID;
	}
}