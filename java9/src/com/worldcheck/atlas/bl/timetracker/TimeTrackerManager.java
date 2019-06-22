package com.worldcheck.atlas.bl.timetracker;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ITimeTracker;
import com.worldcheck.atlas.dao.timetracker.TimeTrackerDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TimeTrackerManager implements ITimeTracker {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.timetracker.TimeTrackerManager");
	private String INTERIM2 = "Interim2";
	private String INTERIM1 = "Interim1";
	private String FINAL = "Final";
	private String VENDOR = "Vendor";
	private String BI = "BI";
	private String INTERNAL = "Internal";
	private String PRIMARY = "Primary";
	private String USER_ID = "userId";
	private String TASK_NAME = "taskName";
	TimeTrackerDAO timeTrackerDAO = null;

	public void setTimeTrackerDAO(TimeTrackerDAO timeTrackerDAO) {
		this.timeTrackerDAO = timeTrackerDAO;
	}

	public int startTimeTracker(TimeTrackerVO timeTrackerVO) throws CMSException {
		return this.timeTrackerDAO.insert(timeTrackerVO);
	}

	public void stopTimeTracker(TimeTrackerVO timeTrackerVO) throws CMSException {
		this.timeTrackerDAO.update(timeTrackerVO);
		ResourceLocator.self().getCacheService().removeTTTokenCache(timeTrackerVO.getUserName());
	}

	public TimeTrackerVO checkActiveTaskForUser(String userId, String crn, Session session, String caseHistoryPerformer)
			throws CMSException {
		this.logger.debug("in checkActiveTaskForUser for user :: " + userId + " :: crn :: " + crn
				+ " :: caseHistoryPerformer :: " + caseHistoryPerformer);
		TimeTrackerVO vo = null;

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String condition = "";
			if (null != crn && crn.trim().length() > 0) {
				condition = "BLIDS.CRN = '" + crn + "'";
			}

			List<MyTaskPageVO> userActiveTaskListForCRN = resourceLocator.getSBMService().getMyTasks(session,
					condition);
			this.logger.debug("userActiveTaskListForCRN size :: " + userActiveTaskListForCRN.size());
			if (userActiveTaskListForCRN.size() > 0) {
				MyTaskPageVO taskVo = null;
				if (userActiveTaskListForCRN.size() == 1) {
					taskVo = (MyTaskPageVO) userActiveTaskListForCRN.get(0);
					this.logger.debug("cycle :: " + taskVo.getCurrentCycle() + " :: task :: " + taskVo.getTaskName()
							+ " :: crn :: " + taskVo.getCrn() + " :: " + taskVo.getWorlItemName() + " :: pid :: "
							+ taskVo.getProcessInstanceId() + " :: getTeamTypeList :: " + taskVo.getTeamTypeList()
							+ " :: status :: " + taskVo.getStatus());
					vo = this.populateTrackerBean(taskVo, userId, session, caseHistoryPerformer);
				} else {
					vo = this.getTrackerVo(crn, userId, resourceLocator, userActiveTaskListForCRN, session,
							caseHistoryPerformer);
				}

				if (null != vo) {
					int trackerId = this.startTimeTracker(vo);
					vo.setTrackerId(trackerId);
				}
			} else {
				this.logger.debug("no active task for user " + userId + " for the CRN :: " + crn);
			}

			return vo;
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private TimeTrackerVO getTrackerVo(String crn, String userId, ResourceLocator resourceLocator,
			List<MyTaskPageVO> userActiveTaskListForCRN, Session session, String caseHistoryPerformer)
			throws CMSException {
		MyTaskPageVO taskVo = null;
		MyTaskPageVO prevTaskVo = null;

		try {
			HashMap<Integer, String> teamNameMap = this.prepareTeamNameMap(crn, resourceLocator);

			for (int i = 0; i < userActiveTaskListForCRN.size(); ++i) {
				taskVo = (MyTaskPageVO) userActiveTaskListForCRN.get(i);
				this.logger.debug("cycle :: " + taskVo.getCurrentCycle() + " :: task :: " + taskVo.getTaskName()
						+ " :: crn :: " + taskVo.getCrn() + " :: " + taskVo.getWorlItemName() + " :: pid :: "
						+ taskVo.getPid() + " :: getTeamTypeList :: " + taskVo.getTeamTypeList() + " :: status :: "
						+ taskVo.getStatus());
				if (i != 0) {
					if (taskVo.getTaskName().equalsIgnoreCase("Office Assignment Task")) {
						prevTaskVo = taskVo;
						break;
					}

					if (prevTaskVo.getTaskName().equalsIgnoreCase("Complete Case Creation")
							&& !taskVo.getTaskName().equalsIgnoreCase("Complete Case Creation")) {
						prevTaskVo = taskVo;
					} else if (prevTaskVo.getTaskName().equalsIgnoreCase("Invoicing Task")
							&& !taskVo.getTaskName().equalsIgnoreCase("Invoicing Task")) {
						prevTaskVo = taskVo;
					} else if (null != taskVo.getTeamTypeList() && null != prevTaskVo.getTeamTypeList()) {
						prevTaskVo = this.getTaskForTrackerAction(taskVo, teamNameMap, prevTaskVo);
					}
				} else {
					prevTaskVo = taskVo;
				}
			}
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}

		return this.populateTrackerBean(prevTaskVo, userId, session, caseHistoryPerformer);
	}

	private MyTaskPageVO getTaskForTrackerAction(MyTaskPageVO taskVo, HashMap<Integer, String> teamNameMap,
			MyTaskPageVO prevTaskVo) throws Exception {
		String currentTeamDetails = "";
		String previousTeamDetails = "";
		currentTeamDetails = taskVo.getTeamTypeList();
		previousTeamDetails = prevTaskVo.getTeamTypeList();
		if (currentTeamDetails.contains(this.PRIMARY) && previousTeamDetails.contains(this.PRIMARY)) {
			this.logger.debug("both tasks are of primary.. so comparing now cycles");
			prevTaskVo = this.taskCycleChecking(taskVo, prevTaskVo);
		} else if (currentTeamDetails.contains(this.PRIMARY) && !previousTeamDetails.contains(this.PRIMARY)) {
			prevTaskVo = taskVo;
		} else if (!currentTeamDetails.contains(this.PRIMARY) && previousTeamDetails.contains(this.PRIMARY)) {
			this.logger.debug(" last item was of primary and current is of non primary");
		} else if (!previousTeamDetails.contains(this.PRIMARY) && !previousTeamDetails.contains(this.INTERNAL)
				&& currentTeamDetails.contains(this.INTERNAL)) {
			prevTaskVo = taskVo;
		} else if (!previousTeamDetails.contains(this.PRIMARY) && previousTeamDetails.contains(this.INTERNAL)
				&& !currentTeamDetails.contains(this.INTERNAL)) {
			this.logger.debug(" last item was of internal non primary and current is of non internal");
		} else if (!previousTeamDetails.contains(this.PRIMARY) && previousTeamDetails.contains(this.INTERNAL)
				&& currentTeamDetails.contains(this.INTERNAL)) {
			this.logger.debug("both tasks are of internal and non primary.. so comparing now team numbering");
			prevTaskVo = this.identifyTaskWithSameTeam(taskVo, teamNameMap, prevTaskVo);
		} else if (!previousTeamDetails.contains(this.PRIMARY) && !previousTeamDetails.contains(this.INTERNAL)
				&& currentTeamDetails.contains(this.BI) && !previousTeamDetails.contains(this.BI)) {
			prevTaskVo = taskVo;
		} else if (!previousTeamDetails.contains(this.PRIMARY) && !previousTeamDetails.contains(this.INTERNAL)
				&& previousTeamDetails.contains(this.BI) && !currentTeamDetails.contains(this.BI)) {
			this.logger.debug(" last item was of BI non primary non internal and current is of non BI");
		} else if (!previousTeamDetails.contains(this.PRIMARY) && !previousTeamDetails.contains(this.INTERNAL)
				&& !previousTeamDetails.contains(this.BI) && currentTeamDetails.contains(this.VENDOR)
				&& previousTeamDetails.contains(this.VENDOR)) {
			prevTaskVo = this.identifyTaskWithSameTeam(taskVo, teamNameMap, prevTaskVo);
		}

		return prevTaskVo;
	}

	private MyTaskPageVO identifyTaskWithSameTeam(MyTaskPageVO taskVo, HashMap<Integer, String> teamNameMap,
			MyTaskPageVO prevTaskVo) throws Exception {
		String currentTeamName = "";
		String previousTeamName = "";
		int currentTeamNumber = 0;
		int previousTeamNumber = 0;
		boolean checkCycle = false;
		if (null != taskVo.getTeamTypeList()) {
			currentTeamName = (String) teamNameMap.get(taskVo.getTeamTypeList().split("#")[1]);
			currentTeamNumber = Integer.parseInt(currentTeamName.split(" ")[1]);
		}

		if (null != prevTaskVo.getTeamTypeList()) {
			previousTeamName = (String) teamNameMap.get(prevTaskVo.getTeamTypeList().split("#")[1]);
			previousTeamNumber = Integer.parseInt(previousTeamName.split(" ")[1]);
		}

		if (previousTeamNumber < currentTeamNumber) {
			this.logger.debug("last item team number is less ..so no change");
		} else if (previousTeamNumber > currentTeamNumber) {
			prevTaskVo = taskVo;
		} else if (previousTeamNumber == currentTeamNumber) {
			checkCycle = true;
		}

		if (checkCycle) {
			this.logger.debug("both tasks are of internal and non primary and team number.. so comparing now cycles");
			prevTaskVo = this.taskCycleChecking(taskVo, prevTaskVo);
		}

		return prevTaskVo;
	}

	private MyTaskPageVO taskCycleChecking(MyTaskPageVO taskVo, MyTaskPageVO prevTaskVo) throws Exception {
		if (null != taskVo.getCurrentCycle() && taskVo.getCurrentCycle().trim().length() > 0
				&& null != prevTaskVo.getCurrentCycle() && prevTaskVo.getCurrentCycle().trim().length() > 0) {
			if (taskVo.getCurrentCycle().contains(this.FINAL) && !prevTaskVo.getCurrentCycle().contains(this.FINAL)) {
				this.logger.debug("current task cycle is final and last item is not final.");
			} else if (!taskVo.getCurrentCycle().contains(this.FINAL)
					&& prevTaskVo.getCurrentCycle().contains(this.FINAL)) {
				prevTaskVo = taskVo;
			} else if (!taskVo.getCurrentCycle().contains(this.INTERIM1)
					&& prevTaskVo.getCurrentCycle().contains(this.INTERIM1)) {
				this.logger.debug("current task cycle is not interim1 and last item is interim1.");
			} else if (taskVo.getCurrentCycle().contains(this.INTERIM1)
					&& !prevTaskVo.getCurrentCycle().contains(this.INTERIM1)) {
				prevTaskVo = taskVo;
			} else if (!taskVo.getCurrentCycle().contains(this.INTERIM2)
					&& !taskVo.getCurrentCycle().contains(this.INTERIM1)
					&& prevTaskVo.getCurrentCycle().contains(this.INTERIM2)) {
				this.logger.debug("current task cycle is neither interim1 nor interim2 and last item is interim2.");
			}
		}

		return prevTaskVo;
	}

	private HashMap<Integer, String> prepareTeamNameMap(String crn, ResourceLocator resourceLocator)
			throws CMSException {
		List<TeamDetails> teamNamesList = resourceLocator.getTeamAssignmentService().getTeamNamesForCRN(crn);
		HashMap<Integer, String> teamNameMap = new HashMap();
		if (teamNamesList.size() > 0) {
			TeamDetails teamDetailVo = null;

			for (int i = 0; i < teamNamesList.size(); ++i) {
				teamDetailVo = (TeamDetails) teamNamesList.get(i);
				teamNameMap.put(teamDetailVo.getTeamId(), teamDetailVo.getTeamName());
			}
		}

		return teamNameMap;
	}

	private TimeTrackerVO populateTrackerBean(MyTaskPageVO taskVo, String userId, Session session,
			String caseHistoryPerformer) throws CMSException {
		TimeTrackerVO vo = null;

		try {
			boolean invoiceTaskFlag = false;
			if (taskVo.getTaskName().equals("Invoicing Task")) {
				int count = this.checkTTOnForTask(taskVo.getCrn(), taskVo.getTaskName(), userId);
				if (count > 0) {
					invoiceTaskFlag = true;
				}

				this.logger.debug(
						" the task is invoicing ..count :: " + count + " :: invoiceTaskFlag :: " + invoiceTaskFlag);
			}

			if (!invoiceTaskFlag) {
				String taskName = taskVo.getTaskName();
				if (taskName.equals("Complete Case Creation")) {
					vo = null;
				} else {
					vo = new TimeTrackerVO();
				}

				if (null != vo) {
					vo.setCrn(taskVo.getCrn());
					vo.setProcessCycle(taskVo.getCurrentCycle());
					vo.setTaskPid(String.valueOf(taskVo.getProcessInstanceId()));
					vo.setTaskName(taskVo.getTaskName());
					vo.setWorkItemName(taskVo.getWorlItemName());
					vo.setTaskStatus(taskVo.getStatus());
					vo.setUserName(userId);
					vo.setUpdatedBy(userId);
				}

				HashMap<String, Object> dsValues = new HashMap();
				if (null != taskVo.getWorlItemName() && taskVo.getWorlItemName().contains("CaseCreation")) {
					if (taskName.equals("Office Assignment Task")) {
						dsValues.put("OfficeTaskSTatus", "In Progress");
					} else if (taskName.equals("Complete Case Creation")) {
						dsValues.put("CompleteTaskStatus", "In Progress");
					} else if (taskName.equals("Consolidation Task")) {
						dsValues.put("ConsolidationTaskStatus", "In Progress");
					} else if (taskName.equals("Client Submission Task")) {
						dsValues.put("ClientSubmissionTaskStatus", "In Progress");
					} else if (taskName.equals("Invoicing Task")) {
						dsValues.put("InvoiceTaskStatus", "In Progress");
					}
				} else {
					dsValues.put("TaskStatus", "In Progress");
				}

				CaseHistory caseHistory = ResourceLocator.self().getSBMService()
						.getCaseDetails(taskVo.getWorlItemName());
				if (null != caseHistory.getOldInfo() && caseHistory.getOldInfo().trim().length() > 0
						&& !caseHistory.getOldInfo().equalsIgnoreCase("In Progress")) {
					this.logger.debug("calling case history from time tracker manager from search");
					caseHistory.setPerformer(caseHistoryPerformer);
					caseHistory.setNewInfo("In Progress");
					ResourceLocator.self().getCaseHistoryService().setCaseHistoryForTaskStatusChange(caseHistory);
				}

				ResourceLocator.self().getSBMService().updateDataSlots(session, taskVo.getProcessInstanceId(),
						dsValues);
			}

			return vo;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<TimeTrackerVO> isTimeTrackerOnForUser(String userId) throws CMSException {
		this.logger.debug("in isTimeTrackerOnForUser for user :: " + userId);

		try {
			return this.timeTrackerDAO.getTrackerInfoForUser(userId);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int stopTimeTrackerForUser(String userId) throws CMSException {
		return this.timeTrackerDAO.stopTimeTrackerForUser(userId);
	}

	public void stopAllTrackerForCase(String crn, String userId) throws CMSException {
		HashMap<Object, String> param = new HashMap();
		param.put("crn", crn);
		param.put(this.USER_ID, userId);
		List<TimeTrackerVO> trackerOnList = this.timeTrackerDAO.getTrackerInfoForCase(crn);
		Iterator i$ = trackerOnList.iterator();

		while (i$.hasNext()) {
			TimeTrackerVO timeTrackerVO = (TimeTrackerVO) i$.next();
			ResourceLocator.self().getCacheService().removeTTTokenCache(timeTrackerVO.getUserName());
		}

		this.timeTrackerDAO.stopAllTrackerForCase(param);
	}

	public int checkTTOnForTask(String crn, String taskName, String userName) throws CMSException {
		HashMap<Object, String> param = new HashMap();
		param.put("crn", crn);
		param.put(this.TASK_NAME, taskName);
		param.put(this.USER_ID, userName);
		return this.timeTrackerDAO.checkTTOnForTask(param);
	}

	public boolean checkTimeTrackerForUserTask(TimeTrackerVO timeTrackerVO) throws CMSException {
		int count = this.timeTrackerDAO.checkTimeTrackerForUserTask(timeTrackerVO);
		boolean isTimeTrackerActive = count > 0;
		return isTimeTrackerActive;
	}
}