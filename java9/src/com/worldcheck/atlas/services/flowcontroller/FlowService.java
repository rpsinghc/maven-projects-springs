package com.worldcheck.atlas.services.flowcontroller;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.flowcontroller.FlowController;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowService {
	private FlowController flowController;

	public void setFlowController(FlowController flowController) {
		this.flowController = flowController;
	}

	public void updateDS(Session session, CaseDetails caseDetails, String module) throws CMSException {
		this.flowController.updateDS(session, caseDetails, module);
	}

	public List<CaseDetails> bulkUpdateDSAndCompleteTask(Session session, List<CaseDetails> caseDetailsList,
			String module) throws CMSException {
		return this.flowController.bulkUpdateDSAndCompleteTask(session, caseDetailsList, module);
	}

	public void updateDSAndPushFlow(Session session, CaseDetails oldCaseDetails, CaseDetails newCaseDetails,
			String module) throws CMSException {
		this.flowController.updateDSAndPushFlow(session, oldCaseDetails, newCaseDetails, module);
	}

	public void updateCaseInformationDS(Session session, CaseDetails oldCaseDetails, CaseDetails newCaseDetails,
			HashMap<String, Object> dsValues) throws CMSException {
		this.flowController.updateCaseInformationDS(session, oldCaseDetails, newCaseDetails, dsValues);
	}

	public void updateDSForRejection(Session session, String workitemName, List<SubTeamReMapVO> subTeamReMapList)
			throws CMSException {
		this.flowController.updateDSForRejection(session, workitemName, subTeamReMapList);
	}

	public void updateDataslotsForCase(Session session, Map<String, CycleInfo> cycleInformation,
			HashMap<String, Object> dataslotValues) throws CMSException {
		this.flowController.updateDataslotsForCase(session, cycleInformation, dataslotValues);
	}

	public void updateResearchDueDateDataslots(long pid, Session session, CaseDetails caseDetails,
			Map<String, CycleInfo> cycleInformation) throws CMSException {
		this.flowController.updateResearchDueDateDataslots(pid, session, caseDetails, cycleInformation);
	}
}