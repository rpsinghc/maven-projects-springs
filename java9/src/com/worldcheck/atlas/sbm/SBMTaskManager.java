package com.worldcheck.atlas.sbm;

import com.savvion.sbm.bizlogic.client.queryservice.QSWorkItemFilter;
import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.server.svo.ProcessInstanceList;
import com.savvion.sbm.bizlogic.server.svo.WorkItem;
import com.savvion.sbm.bizlogic.server.svo.WorkItemList;
import com.savvion.sbm.bizlogic.server.svo.WorkStepInstance;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.dao.AtlasWebServiceDAO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class SBMTaskManager {
	private AtlasWebServiceDAO atlasWebServiceDAO;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.SBMTaskManager");

	public void setAtlasWebServiceDAO(AtlasWebServiceDAO atlasWebServiceDAO) {
		this.atlasWebServiceDAO = atlasWebServiceDAO;
	}

	public void completeTask(HashMap<String, Object> updatedDSValuesMap, long piid, String wsName, Session session)
			throws CMSException {
		try {
			this.logger.debug("inside SBMTaskManager.completeTask() : before getting ProcessInstance");
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			this.logger.debug(
					"inside SBMTaskManager.completeTask() : after getting ProcessInstance, before getting WorkStepInstance");
			WorkStepInstance ws = processInstance.getWorkStepInstance(wsName);
			this.logger.debug(
					"inside SBMTaskManager.completeTask() : after getting WorkStepInstance, before completing workstep");
			ws.complete(updatedDSValuesMap);
			this.logger.debug("WorkStep " + wsName + " has been completed for process instance " + piid);
		} catch (RemoteException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public void completeProcess(HashMap<String, Object> updatedDSValuesMap, long piid, Session session)
			throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			processInstance.complete(updatedDSValuesMap);
			this.logger.debug("process has been completed for pid " + piid);
		} catch (RemoteException var6) {
			throw new CMSException(this.logger, var6);
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public void updateDataSlots(long pid, HashMap<String, Object> dsValues, Session session) throws CMSException {
		try {
			this.logger.debug("inside update dataslot method pid is " + pid);
			this.logger.debug(" for user " + session.getUser() + " pid is " + pid + " has map is " + dsValues);
			ProcessInstance.updateDataSlot(session, pid, dsValues);
			this.logger.debug("after updating dataslots..");
		} catch (RemoteException var6) {
			throw new CMSException(this.logger, var6);
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public String[] getActivatedWSNames(long piid, Session session) throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			return processInstance.getActivatedWorkStepNames();
		} catch (RemoteException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public String[] getActivatedProcessNames(long piid, Session session) throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			String[] activatedSubProcess = (String[]) null;
			int i = 0;
			List<ProcessInstance> processInstanceList = processInstance.getActiveSubProcessList().getList();
			activatedSubProcess = new String[processInstanceList.size()];

			for (Iterator iterator = processInstanceList.iterator(); iterator.hasNext(); ++i) {
				ProcessInstance processInstanceTemp = (ProcessInstance) iterator.next();
				activatedSubProcess[i] = processInstanceTemp.getName();
				this.logger.debug("name of ws is " + activatedSubProcess[i]);
			}

			return activatedSubProcess;
		} catch (RemoteException var10) {
			throw new CMSException(this.logger, var10);
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public List<WorkItem> getAssignedTasks(Session session) throws CMSException {
		try {
			return WorkItemList.getAssignedList(session).getList();
		} catch (NullPointerException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<WorkItem> getAvailableTasks(Session session) throws CMSException {
		try {
			return WorkItemList.getAvailableList(session).getList();
		} catch (NullPointerException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ProcessInstance> getProcessInstances(Session session) throws CMSException {
		try {
			return ProcessInstanceList.getList(session).getList();
		} catch (NullPointerException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Object getDataslotValue(long piid, String dsName, Session session) throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			return processInstance.getDataSlotValue(dsName);
		} catch (RemoteException var6) {
			throw new CMSException(this.logger, var6);
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public Map<String, Object> getAllDataslotValues(long piid, Session session) throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			return processInstance.getDataSlotValue();
		} catch (RemoteException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void saveTask(long piid, String wsName, Session session) throws CMSException {
		try {
			WorkStepInstance workStep = WorkStepInstance.get(session, piid, wsName);
			workStep.save();
		} catch (RemoteException var7) {
			throw new CMSException(this.logger, var7);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List<WorkItem> delegatedTasks(Session session, String taskOwner) throws CMSException {
		ArrayList list = new ArrayList();

		try {
			list.addAll(WorkItemList.getAssignedList(session).getList());
			list.addAll(WorkItemList.getAvailableList(session).getList());
			return list;
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<WorkItem> myTeamTask(Session[] sessions, String[] user, String status, String ptName)
			throws CMSException {
		ArrayList list = new ArrayList();

		try {
			QSWorkItemFilter wiFilter = new QSWorkItemFilter(ptName, ptName);
			if (status.equalsIgnoreCase("x")) {
				wiFilter.setCondition("BLIDS.TaskStatus='New'");
			} else if (status.equalsIgnoreCase("y")) {
				wiFilter.setCondition("BLIDS.TaskStatus='WIP'");
			}

			for (int i = 0; i < user.length; ++i) {
				Session session = sessions[i];
				list.addAll(WorkItemList.getAssignedList(session, wiFilter).getList());
				list.addAll(WorkItemList.getAvailableList(session, wiFilter).getList());
			}

			return list;
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public void reassignTask(long piid, String newUser, String wsName, Session session) throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, piid);
			List<WorkItem> list = processInstance.getWorkItemList().getList();
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				WorkItem workItem = (WorkItem) iterator.next();
				if (workItem.getName().contains(wsName)) {
					workItem.reAssign(newUser);
				}
			}

		} catch (RemoteException var10) {
			throw new CMSException(this.logger, var10);
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public void removePI(Session session, long pid) throws CMSException {
		try {
			ProcessInstance processInstance = ProcessInstance.get(session, pid);
			processInstance.remove();
		} catch (RemoteException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void updateDSForOfficeTask(long pid, CycleTeamMapping cycleTeamMap, Session session) throws CMSException {
		this.logger.debug("cycle team map is " + cycleTeamMap);
		HashMap<String, Object> dsValues = new HashMap();
		Map<String, CycleTeamMapping> tempMap = new HashMap();
		tempMap.put("CycleTeamMapping", cycleTeamMap);
		this.logger.debug("update office temp Map is " + tempMap);
		dsValues.put("customDSMap", tempMap);
		this.updateDataSlots(pid, dsValues, session);
	}

	public synchronized void updateDSForTeamTask(long pid, CycleTeamMapping cycleTeamMap, Session session)
			throws CMSException {
		HashMap<String, Object> dsValues = new HashMap();
		HashMap<String, CycleTeamMapping> tempMap = new HashMap();
		tempMap.put("CycleTeamMapping", cycleTeamMap);
		dsValues.put("customDSMap", tempMap);
		ProcessInstance processInstance = null;

		try {
			processInstance = ProcessInstance.get(session, pid);
			processInstance.updateSlotValue(dsValues);
		} catch (RemoteException var12) {
			throw new CMSException(this.logger, var12);
		}

		long parentPID = (Long) this.getDataslotValue(pid, "parentPID", session);
		if (parentPID > 0L) {
			try {
				processInstance = ProcessInstance.get(session, parentPID);
				processInstance.updateSlotValue(dsValues);
			} catch (RemoteException var11) {
				throw new CMSException(this.logger, var11);
			}
		} else {
			this.logger.error("parent pid not found");
		}

	}

	public long getParentPID(long pid, Session session) throws CMSException {
		long parentPID = 0L;

		try {
			ProcessInstance processInstance = ProcessInstance.get(session, pid);
			parentPID = processInstance.getParentID();
		} catch (RemoteException var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("parent PID is " + parentPID);
		return parentPID;
	}

	public void casePulledBack(long pid) {
	}

	public String[] teamName(long pid, String processCycle, Session session) throws CMSException {
		String[] teamName = (String[]) null;
		CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((HashMap) this.getDataslotValue(pid, "customDSMap",
				session)).get("CycleTeamMapping");
		CycleInfo cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation().get(processCycle);
		Map<String, TeamAnalystMapping> teams = cycleInfo.getTeamInfo();
		Set<String> teamObjects = teams.keySet();
		teamName = new String[teamObjects.size()];
		int i = 0;

		for (Iterator iterator = teamObjects.iterator(); iterator.hasNext(); ++i) {
			String string = (String) iterator.next();
			teamName[i] = ((TeamAnalystMapping) teams.get(string)).getTaskName();
		}

		return teamName;
	}

	public String[] getWSNames(long pid, Session session) throws CMSException {
		String[] wsNames = (String[]) null;

		try {
			ProcessInstance processInstance = ProcessInstance.get(session, pid);
			wsNames = processInstance.getActivatedWorkStepNames();
			this.logger.debug("Ws names for pid " + pid + " is " + wsNames);
			return wsNames;
		} catch (RemoteException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public String[] getCompleteWSNames(long pid, Session session) throws CMSException {
		String[] wsNames = (String[]) null;

		try {
			ProcessInstance processInstance = ProcessInstance.get(session, pid);
			wsNames = processInstance.getCompletedWorkStepNames();
			this.logger.debug("completed Ws names for pid " + pid + " is " + wsNames);
			return wsNames;
		} catch (RemoteException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public boolean isTaskCompleted(long pid, Session session) {
		boolean pidCompleted = false;

		try {
			ProcessInstance.get(session, pid);
			this.logger.debug("Process instance exists for pid " + pid);
		} catch (RemoteException var6) {
			pidCompleted = true;
		}

		return pidCompleted;
	}

	public void bulkTaskModification(Session session, Map<String, HashMap<String, Object>> mapOfData, String taskType,
			boolean isRequestForCompletion) throws CMSException {
		Iterator iterator;
		String pid;
		if (isRequestForCompletion) {
			iterator = mapOfData.keySet().iterator();

			while (iterator.hasNext()) {
				pid = (String) iterator.next();
				this.completeTask((HashMap) mapOfData.get(pid), Long.parseLong(pid), taskType, session);
			}
		} else {
			iterator = mapOfData.keySet().iterator();

			while (iterator.hasNext()) {
				pid = (String) iterator.next();
				this.updateDataSlots(Long.parseLong(pid), (HashMap) mapOfData.get(pid), session);
				this.saveTask(Long.parseLong(pid), taskType, session);
			}
		}

	}

	public List<String> getAllCycleNames(long pid, Session session) throws CMSException {
		List<String> list = new ArrayList();
		String temp = (String) this.getDataslotValue(pid, "RInterim1", session);
		if (temp != null && !"<NULL>".equalsIgnoreCase(temp) && !"".equalsIgnoreCase(temp)) {
			list.add("Interim1");
		}

		temp = (String) this.getDataslotValue(pid, "RInterim2", session);
		if (temp != null && !"<NULL>".equalsIgnoreCase(temp) && !"".equalsIgnoreCase(temp)) {
			list.add("Interim2");
		}

		temp = (String) this.getDataslotValue(pid, "RFinal", session);
		if (temp != null && !"<NULL>".equalsIgnoreCase(temp) && !"".equalsIgnoreCase(temp)) {
			list.add("Final");
		}

		return list;
	}

	public List<String> getAllCycleNames(String workItemName, String teamName, Session session)
			throws NumberFormatException, CMSException {
		List<String> list = new ArrayList();
		String[] workItemInfo = workItemName.split("::");
		String[] ptInfo = workItemInfo[0].split("#");
		long pid = 0L;
		this.logger.debug("workitem is " + workItemName);
		if (ptInfo[0].equalsIgnoreCase("CaseCreation")) {
			pid = Long.parseLong(ptInfo[1]);
		} else {
			pid = (Long) this.getDataslotValue(Long.parseLong(ptInfo[1]), "parentPID", session);
		}

		this.logger.debug("pid is " + pid);
		CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((Map) this.getDataslotValue(pid, "customDSMap", session))
				.get("CycleTeamMapping");
		this.logger.debug("cycleTeamMap is " + cycleTeamMap);
		if (cycleTeamMap.getCycleInformation().get("Interim1") != null
				&& ((CycleInfo) cycleTeamMap.getCycleInformation().get("Interim1")).getTeamInfo()
						.containsKey(teamName)) {
			list.add("Interim1");
		}

		if (cycleTeamMap.getCycleInformation().get("Interim2") != null
				&& ((CycleInfo) cycleTeamMap.getCycleInformation().get("Interim2")).getTeamInfo()
						.containsKey(teamName)) {
			list.add("Interim2");
		}

		if (cycleTeamMap.getCycleInformation().get("Final") != null
				&& ((CycleInfo) cycleTeamMap.getCycleInformation().get("Final")).getTeamInfo().containsKey(teamName)) {
			list.add("Final");
		}

		this.logger.debug("size is " + list.size());
		return list;
	}

	public boolean isReviewerAvalableForDeletion(long pid, String teamName, String reviewerId, Session session)
			throws CMSException {
		HashMap<String, CycleTeamMapping> customDs = (HashMap) this.getDataslotValue(pid, "customDSMap", session);
		CycleTeamMapping cycleTeamMap = (CycleTeamMapping) customDs.get("CycleTeamMapping");
		CycleInfo cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation().get(cycleTeamMap.getCurrentCycle());
		long reviewPID = 0L;
		if (cycleInfo.getTeamInfo().containsKey(teamName)) {
			this.logger.debug("cycle is " + cycleTeamMap.getCurrentCycle());
		} else {
			cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation().get("Final");
			this.logger.debug("cycle is Final");
		}

		reviewPID = ((TeamAnalystMapping) cycleInfo.getTeamInfo().get(teamName)).getReviewProcessPID();
		if (reviewPID == 0L) {
			this.logger.debug("Review is not yet started ");
			return true;
		} else if (this.isTaskCompleted(reviewPID, session)) {
			this.logger.debug("Task has already beean completed for this cycle ");
			return false;
		} else {
			Vector<String> reviewers = (Vector) this.getDataslotValue(reviewPID, "Reviewers", session);
			if (!reviewers.contains(reviewerId) && !reviewerId.equalsIgnoreCase(
					(String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "Reviewer", session))) {
				this.logger.debug("Review has already started");
				return false;
			} else {
				this.logger.debug("Task can be deleted ");
				return true;
			}
		}
	}

	public boolean isWsCompleted(String userId, String wsName, long pid, Session session) throws CMSException {
		boolean isCompleted = false;

		try {
			if (!this.isTaskCompleted(pid, session)) {
				ProcessInstance processInstance = ProcessInstance.get(session, pid);
				WorkStepInstance ws;
				if ("Consolidation Task".equalsIgnoreCase(wsName)) {
					ws = processInstance.getWorkStepInstance(wsName);
					isCompleted = !ws.isActivated();
					this.logger.debug("isCompleted in CONSOLIDATION_WS_NAME is " + isCompleted);
				} else if ("Review Task".equalsIgnoreCase(wsName)) {
					if (!userId.equalsIgnoreCase((String) processInstance.getDataSlotValue("Reviewer"))) {
						isCompleted = true;
						this.logger.debug("isCompleted in REVIEWER is " + isCompleted);
					}
				} else if ("Client Submission Task".equalsIgnoreCase(wsName)) {
					ws = processInstance.getWorkStepInstance(wsName);
					isCompleted = !ws.isActivated();
					this.logger.debug("isCompleted in CLIENT_SUBMISISON_WS_NAME is " + isCompleted);
				}
			} else {
				isCompleted = true;
				this.logger.debug("isCompleted task doen't exists " + isCompleted);
			}
		} catch (RemoteException var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("isCompleted is " + isCompleted);
		return isCompleted;
	}

	public String getworkItemName(long pid, String workStepName, Session session) throws CMSException {
		String workItemName = "";

		try {
			ProcessInstance processInstance = ProcessInstance.get(session, pid);
			List<WorkItem> wiList = processInstance.getWorkItemList().getList();
			Iterator iterator = wiList.iterator();

			while (iterator.hasNext()) {
				WorkItem workItem = (WorkItem) iterator.next();
				String wiName = workItem.getName();
				this.logger.debug("wiName is " + wiName);
				if (wiName.contains(workStepName)) {
					workItemName = wiName;
				}
			}
		} catch (RemoteException var11) {
			throw new CMSException(this.logger, var11);
		}

		this.logger.debug("getWorkItemName() " + workItemName);
		return workItemName;
	}

	public boolean wiExistsForUser(String workItemName, Session session, String userName) {
		boolean isWIExists = false;

		try {
			this.logger.debug("workstep name is " + workItemName);
			this.logger.debug("user is " + userName);
			List<WorkItem> list = null;
			long pid = Long.parseLong(workItemName.split("::")[0].split("#")[1]);
			this.logger.debug("pid is " + pid);
			ProcessInstance processInstance = ProcessInstance.get(session, pid);
			list = processInstance.getAssignedWorkItemList().getList();
			Iterator iterator = list.iterator();

			WorkItem workItem;
			while (iterator.hasNext()) {
				workItem = (WorkItem) iterator.next();
				if (workItem.getName().equalsIgnoreCase(workItemName)
						&& workItem.getPerformer().equalsIgnoreCase(userName)) {
					isWIExists = true;
				}
			}

			list = processInstance.getAvailableWorkItemList().getList();
			iterator = list.iterator();

			while (iterator.hasNext()) {
				workItem = (WorkItem) iterator.next();
				if (workItem.getName().equalsIgnoreCase(workItemName)) {
					isWIExists = true;
				}
			}
		} catch (RemoteException var11) {
			;
		} catch (NullPointerException var12) {
			this.logger.error(var12);
		} catch (Exception var13) {
			this.logger.error(var13);
		}

		return isWIExists;
	}

	public List<Long> getPIDListfromSavvionforAtlasCase(String crn) {
		this.logger.debug("Inside getPIDListfromSavvionforAtlasCase");
		List<Long> pidList = this.atlasWebServiceDAO.getPIDListfromSavvionforCRN(crn);
		return pidList;
	}
}