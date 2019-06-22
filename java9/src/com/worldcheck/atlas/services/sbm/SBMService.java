package com.worldcheck.atlas.services.sbm;

import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.server.svo.WorkItem;
import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.util.ResourceUtil;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.SBMNotificationManager;
import com.worldcheck.atlas.sbm.SBMSessionManager;
import com.worldcheck.atlas.sbm.SBMTaskCreationManager;
import com.worldcheck.atlas.sbm.SBMTaskManager;
import com.worldcheck.atlas.sbm.SBMUserManager;
import com.worldcheck.atlas.sbm.customds.AnalystTaskStatus;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.sbm.queryService.CaseSBMUtil;
import com.worldcheck.atlas.sbm.queryService.TaskSBMUtil;
import com.worldcheck.atlas.services.task.TaskManagementService;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.sbm.UserCreationVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

public class SBMService {
	private SBMTaskCreationManager sbmTaskCreationManager = null;
	private SBMTaskManager sbmTaskManager = null;
	private SBMSessionManager sessionManager = null;
	private SBMUserManager userManager = null;
	private SBMNotificationManager sbmNotificationManager = null;
	private TaskSBMUtil taskSBMUtil = null;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.sbm.SBMService");

	public void setUserManager(SBMUserManager userManager) {
		this.userManager = userManager;
	}

	public void setTaskSBMUtil(TaskSBMUtil taskSBMUtil) {
		this.taskSBMUtil = taskSBMUtil;
	}

	public void setSbmNotificationManager(SBMNotificationManager sbmNotificationManager) {
		this.sbmNotificationManager = sbmNotificationManager;
	}

	public void setSessionManager(SBMSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setSbmTaskCreationManager(SBMTaskCreationManager sbmTaskCreationManager) {
		this.sbmTaskCreationManager = sbmTaskCreationManager;
	}

	public void setSbmTaskManager(SBMTaskManager sbmTaskManager) {
		this.sbmTaskManager = sbmTaskManager;
	}

	public HashMap<String, String> createProcessInstance(HashMap<String, Object> instCreationParams)
			throws CMSException {
		Session session = null;
		new HashMap();
		session = this.sessionManager.getSession();
		HashMap<String, String> piMap = this.sbmTaskCreationManager.createProcessInstance(instCreationParams, session);
		this.sessionManager.closeSession(session);
		return piMap;
	}

	public long createSubProcessInstance(String ptName, TeamAnalystMapping teamMap, long parentPID, String analyst,
			String cycleName) throws CMSException {
		Session session = null;
		long pid = 0L;
		session = this.sessionManager.getSession();
		pid = this.sbmTaskCreationManager.createSubProcessInstance(ptName, teamMap, session, parentPID, analyst,
				cycleName);
		this.sessionManager.closeSession(session);
		return pid;
	}

	public void completeTask(Session session, HashMap<String, Object> updatedDSValuesMap, long piid, String wsName)
			throws CMSException {
		if (session != null && updatedDSValuesMap != null) {
			this.sbmTaskManager.completeTask(updatedDSValuesMap, piid, wsName, session);
		} else {
			session = this.sessionManager.getSession();
			updatedDSValuesMap = new HashMap();
			this.sbmTaskManager.completeTask(updatedDSValuesMap, piid, wsName, session);
			this.sessionManager.closeSession(session);
		}

	}

	public void completeProcess(Session session, HashMap<String, Object> updatedDSValuesMap, long piid)
			throws CMSException {
		this.sbmTaskManager.completeProcess(updatedDSValuesMap, piid, session);
	}

	public void updateDataSlots(Session session, long pid, HashMap<String, Object> dsValues) throws CMSException {
		this.logger.debug("inside update method of SBM service pid is " + pid);
		this.sbmTaskManager.updateDataSlots(pid, dsValues, session);
	}

	public String[] getActivatedWSNames(Session session, long piid) throws CMSException {
		String[] wsNames = (String[]) null;
		wsNames = this.sbmTaskManager.getActivatedWSNames(piid, session);
		return wsNames;
	}

	public String[] getActivatedSubProcessNames(Session session, long piid) throws CMSException {
		String[] wsNames = (String[]) null;
		wsNames = this.sbmTaskManager.getActivatedProcessNames(piid, session);
		return wsNames;
	}

	public List<WorkItem> getAssignedTasks(Session session) throws CMSException {
		new ArrayList();
		List<WorkItem> list = this.sbmTaskManager.getAssignedTasks(session);
		return list;
	}

	public List<WorkItem> getAvailableTasks(Session session) throws CMSException {
		new ArrayList();
		List<WorkItem> list = this.sbmTaskManager.getAvailableTasks(session);
		return list;
	}

	public List<ProcessInstance> getProcessInstances(Session session) throws CMSException {
		new ArrayList();
		List<ProcessInstance> list = this.sbmTaskManager.getProcessInstances(session);
		return list;
	}

	public Object getDataslotValue(long piid, String dsName, Session session) throws CMSException {
		new Object();
		Object obj = this.sbmTaskManager.getDataslotValue(piid, dsName, session);
		return obj;
	}

	public Map<String, Object> getAllDataslotValues(long piid, Session session) throws CMSException {
		return this.sbmTaskManager.getAllDataslotValues(piid, session);
	}

	public void saveTask(long piid, String wsName, Session session, HashMap<String, Object> dsMap) throws CMSException {
		this.updateDataSlots(session, piid, dsMap);
		this.sbmTaskManager.saveTask(piid, wsName, session);
	}

	public List<WorkItem> delegatedTasks(String taskOwner) throws CMSException {
		new ArrayList();
		List<WorkItem> list = this.sbmTaskManager.delegatedTasks(this.sessionManager.getSession(taskOwner), taskOwner);
		return list;
	}

	public List<MyTaskPageVO> myIncomingTasks(String userId, Session session, int start, int limit)
			throws CMSException {
		List<MyTaskPageVO> teampList = new ArrayList();
		List<TeamDetails> teamDetailsList = ResourceLocator.self().getTaskService().getMyIncomingTasks(userId, limit,
				start);
		Iterator iterator = teamDetailsList.iterator();

		while (true) {
			while (true) {
				TeamDetails teamDetails;
				long pid;
				String analystName;
				String teamAssignmentTask;
				CycleTeamMapping cycleTeamMap;
				String[] tempCycleNames;
				String cycleName;
				do {
					while (true) {
						do {
							if (!iterator.hasNext()) {
								return teampList;
							}

							teamDetails = (TeamDetails) iterator.next();
							pid = ResourceLocator.self().getOfficeAssignmentService()
									.getPIDForCRN(teamDetails.getCrn());
						} while (this.isTaskCompleted(pid, session));

						this.logger.debug("pid is " + pid);
						this.logger.debug("teamDetails.getRoleType() is " + teamDetails.getRoleType());
						if ("REVIEWER".equalsIgnoreCase(teamDetails.getRoleType())) {
							cycleTeamMap = (CycleTeamMapping) ((HashMap) this.getDataslotValue(pid, "customDSMap",
									session)).get("CycleTeamMapping");
							tempCycleNames = new String[3];
							cycleName = "";
							int tempVar = 0;

							for (Iterator iterator2 = cycleTeamMap.getCycleInformation().keySet().iterator(); iterator2
									.hasNext(); ++tempVar) {
								String tempCycleName = (String) iterator2.next();
								if (((CycleInfo) cycleTeamMap.getCycleInformation().get(tempCycleName)).getTeamInfo()
										.get(teamDetails.getTeamName()) != null) {
									tempCycleNames[tempVar] = tempCycleName;
								} else {
									tempCycleNames[tempVar] = "";
								}
							}

							if (!"".equalsIgnoreCase(tempCycleNames[1])) {
								cycleName = (String) this.getDataslotValue(pid, "ProcessCycle", session);
							} else if (tempCycleNames.length != 0) {
								cycleName = tempCycleNames[0];
							}

							this.logger.debug("cycle name is " + cycleName);
							break;
						}

						if (!"MANAGER".equalsIgnoreCase(teamDetails.getRoleType())) {
							if ("AWAITING".equalsIgnoreCase(teamDetails.getRoleType())) {
								Map<String, Object> mapForReview = this.getAllDataslotValues(pid, session);
								MyTaskPageVO reviewerVO = new MyTaskPageVO();
								reviewerVO.setCrn((String) mapForReview.get("CRN"));
								reviewerVO.setClientfinalDueDate((String) mapForReview.get("RFinal"));
								reviewerVO.setCinterim1((String) mapForReview.get("RInterim1"));
								reviewerVO.setCinterim2((String) mapForReview.get("RInterim2"));
								reviewerVO.setTaskName("Awaiting Consolidation");
								reviewerVO.setCurrentTask("Research");
								if ((Boolean) mapForReview.get("ExpressCase")) {
									reviewerVO.setExpressCase("Y");
								} else {
									reviewerVO.setExpressCase("N");
								}

								reviewerVO.setCurrentCycle((String) mapForReview.get("ProcessCycle"));
								reviewerVO.setCountry((String) mapForReview.get("PrimarySubjectCountry"));
								reviewerVO.setPrimarySubject((String) mapForReview.get("PrimarySubject"));
								this.setDueDateForCurrentProcessCycle(reviewerVO);
								teampList.add(reviewerVO);
								this.logger.debug("teampList size::" + teampList.size());
								this.logger.debug("Inside awaiting");
							}
						} else {
							String[] temp = this.getActivatedWSNames(session, pid);

							for (int i = 0; i < temp.length; ++i) {
								MyTaskPageVO reviewerVO;
								Map mapForReview;
								if (!temp[i].equalsIgnoreCase("ProcessCompletion")
										&& !temp[i].equalsIgnoreCase("Client Submission Task")
										&& !temp[i].equalsIgnoreCase("Invoicing Task")
										&& !temp[i].equalsIgnoreCase("And 2")
										|| !temp[i].equalsIgnoreCase("ProcessCompletion")
												&& !temp[i].equalsIgnoreCase("Client Submission Task")
												&& !temp[i].equalsIgnoreCase("Invoicing Task")
												&& temp[i].equalsIgnoreCase("And 2")) {
									reviewerVO = new MyTaskPageVO();
									mapForReview = this.getAllDataslotValues(pid, session);
									reviewerVO.setCrn((String) mapForReview.get("CRN"));
									reviewerVO.setCfinal((String) mapForReview.get("CFinal"));
									reviewerVO.setClientfinalDueDate((String) mapForReview.get("RFinal"));
									reviewerVO.setCinterim1((String) mapForReview.get("CInterim1"));
									reviewerVO.setCinterim2((String) mapForReview.get("CInterim2"));
									reviewerVO.setTaskName("Client Submission Task");
									if (temp[i].equalsIgnoreCase("FinalReview")) {
										reviewerVO.setCurrentTask("Review Task");
									} else {
										reviewerVO.setCurrentTask(temp[i]);
									}

									if ((Boolean) mapForReview.get("ExpressCase")) {
										reviewerVO.setExpressCase("Y");
									} else {
										reviewerVO.setExpressCase("N");
									}

									reviewerVO.setCurrentCycle((String) mapForReview.get("ProcessCycle"));
									reviewerVO.setCountry((String) mapForReview.get("PrimarySubjectCountry"));
									reviewerVO.setPrimarySubject((String) mapForReview.get("PrimarySubject"));
									this.setDueDateForCurrentProcessCycle(reviewerVO);
									teampList.add(reviewerVO);
									this.logger.debug("teampList size::" + teampList.size());
									this.logger.debug("Inside if");
									break;
								}

								if (temp[i].equalsIgnoreCase("Office Assignment Task")) {
									reviewerVO = new MyTaskPageVO();
									mapForReview = this.getAllDataslotValues(pid, session);
									reviewerVO.setCrn((String) mapForReview.get("CRN"));
									reviewerVO.setCfinal((String) mapForReview.get("CFinal"));
									reviewerVO.setCinterim1((String) mapForReview.get("CInterim1"));
									reviewerVO.setCinterim2((String) mapForReview.get("CInterim2"));
									reviewerVO.setTaskName("Client Submission Task");
									reviewerVO.setCurrentTask("Office Assignment Task");
									if ((Boolean) mapForReview.get("ExpressCase")) {
										reviewerVO.setExpressCase("Y");
									} else {
										reviewerVO.setExpressCase("N");
									}

									reviewerVO.setCurrentCycle((String) mapForReview.get("ProcessCycle"));
									reviewerVO.setCountry((String) mapForReview.get("PrimarySubjectCountry"));
									reviewerVO.setPrimarySubject((String) mapForReview.get("PrimarySubject"));
									this.setDueDateForCurrentProcessCycle(reviewerVO);
									teampList.add(reviewerVO);
									this.logger.debug("teampList size::" + teampList.size());
									this.logger.debug("Inside elseif office assignment worskstep");
									break;
								}

								List<String> tempStringList = Arrays.asList(temp);
								boolean checkFlag = false;
								if (!this.isTaskCompleted(pid, session) && !tempStringList.contains("FinalReview")) {
									this.logger.debug("pid is " + pid);
									HashMap<String, Object> customDSMap = (HashMap) this.getDataslotValue(pid,
											"customDSMap", session);
									if (customDSMap != null) {
										CycleTeamMapping cycleTeamMap = (CycleTeamMapping) customDSMap
												.get("CycleTeamMapping");
										Map<String, Object> mapForReview = this.getAllDataslotValues(pid, session);
										String cycleName = (String) mapForReview.get("ProcessCycle");
										this.logger.debug("cycle name is " + cycleName);
										CycleInfo cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation()
												.get(cycleName);
										if (cycleInfo != null) {
											Iterator iterator3 = cycleInfo.getTeamInfo().keySet().iterator();

											label217 : while (true) {
												while (true) {
													String teamName;
													do {
														if (!iterator3.hasNext()) {
															break label217;
														}

														teamName = (String) iterator3.next();
														this.logger.debug("team name is " + teamName);
														TeamAnalystMapping teamAnalystMap = (TeamAnalystMapping) cycleInfo
																.getTeamInfo().get(teamName);
														if (teamAnalystMap == null) {
															this.logger.debug("inside do nothing");
														} else {
															boolean researchAvailable = false;
															boolean isTeamAssignmentActivated = false;
															if (teamAnalystMap.getAnalystTaskStatus() == null
																	|| teamAnalystMap.getAnalystTaskStatus()
																			.size() == 0) {
																isTeamAssignmentActivated = true;
															}

															MyTaskPageVO reviewerVO;
															if (isTeamAssignmentActivated) {
																reviewerVO = new MyTaskPageVO();
																this.logger.debug("start isTeamAssignmentActivated");
																reviewerVO.setCrn((String) mapForReview.get("CRN"));
																reviewerVO
																		.setCfinal((String) mapForReview.get("CFinal"));
																reviewerVO.setClientfinalDueDate(
																		(String) mapForReview.get("RFinal"));
																reviewerVO.setCinterim1(
																		(String) mapForReview.get("CInterim1"));
																reviewerVO.setCinterim2(
																		(String) mapForReview.get("CInterim2"));
																reviewerVO.setTaskName("Client Submission Task");
																if (mapForReview.get("TeamTypeList")
																		.toString() != null) {
																	analystName = mapForReview.get("TeamTypeList")
																			.toString();
																	String[] team = analystName.split("#");
																	teamAssignmentTask = "Team Assignment Task"
																			.concat(team[0]).concat("]");
																	reviewerVO.setCurrentTask(teamAssignmentTask);
																} else {
																	reviewerVO.setCurrentTask("Team Assignment Task");
																}

																if ((Boolean) mapForReview.get("ExpressCase")) {
																	reviewerVO.setExpressCase("Y");
																} else {
																	reviewerVO.setExpressCase("N");
																}

																reviewerVO.setCurrentCycle(
																		(String) mapForReview.get("ProcessCycle"));
																reviewerVO.setCountry((String) mapForReview
																		.get("PrimarySubjectCountry"));
																reviewerVO.setPrimarySubject(
																		(String) mapForReview.get("PrimarySubject"));
																this.setDueDateForCurrentProcessCycle(reviewerVO);
																teampList.add(reviewerVO);
																checkFlag = true;
																this.logger.debug("end isTeamAssignmentActivated");
																this.logger
																		.debug("teampList size::" + teampList.size());
																break label217;
															}

															Iterator iterator4 = teamAnalystMap.getAnalystTaskStatus()
																	.keySet().iterator();

															while (iterator4.hasNext()) {
																analystName = (String) iterator4.next();
																this.logger.debug("Analyst is " + analystName);
																if (!"Done".equalsIgnoreCase(
																		((AnalystTaskStatus) teamAnalystMap
																				.getAnalystTaskStatus()
																				.get(analystName)).getStatus())) {
																	researchAvailable = true;
																	break;
																}
															}

															if (researchAvailable) {
																reviewerVO = new MyTaskPageVO();
																this.logger.debug("start researchAvailable");
																reviewerVO.setCrn((String) mapForReview.get("CRN"));
																reviewerVO
																		.setCfinal((String) mapForReview.get("CFinal"));
																reviewerVO.setClientfinalDueDate(
																		(String) mapForReview.get("RFinal"));
																reviewerVO.setCinterim1(
																		(String) mapForReview.get("CInterim1"));
																reviewerVO.setCinterim2(
																		(String) mapForReview.get("CInterim2"));
																reviewerVO.setTaskName("Client Submission Task");
																reviewerVO.setCurrentTask("Research Task");
																if ((Boolean) mapForReview.get("ExpressCase")) {
																	reviewerVO.setExpressCase("Y");
																} else {
																	reviewerVO.setExpressCase("N");
																}

																reviewerVO.setCurrentCycle(
																		(String) mapForReview.get("ProcessCycle"));
																reviewerVO.setCountry((String) mapForReview
																		.get("PrimarySubjectCountry"));
																reviewerVO.setPrimarySubject(
																		(String) mapForReview.get("PrimarySubject"));
																this.setDueDateForCurrentProcessCycle(reviewerVO);
																teampList.add(reviewerVO);
																checkFlag = true;
																this.logger
																		.debug("teampList size::" + teampList.size());
																this.logger.debug("Inside researchAvailable");
																break label217;
															}
														}
													} while (!teamName.contains("Primary"));

													TeamAnalystMapping teamMap = (TeamAnalystMapping) cycleInfo
															.getTeamInfo().get(teamName);
													if (teamMap.getReviewProcessPID() != 0L && !this
															.isTaskCompleted(teamMap.getReviewProcessPID(), session)) {
														MyTaskPageVO reviewerVO = new MyTaskPageVO();
														this.logger.debug("start primary team initials");
														reviewerVO.setCrn((String) mapForReview.get("CRN"));
														reviewerVO.setCfinal((String) mapForReview.get("CFinal"));
														reviewerVO.setClientfinalDueDate(
																(String) mapForReview.get("RFinal"));
														reviewerVO.setCinterim1((String) mapForReview.get("CInterim1"));
														reviewerVO.setCinterim2((String) mapForReview.get("CInterim2"));
														reviewerVO.setTaskName("Client Submission Task");
														reviewerVO.setCurrentTask("Review Task");
														if ((Boolean) mapForReview.get("ExpressCase")) {
															reviewerVO.setExpressCase("Y");
														} else {
															reviewerVO.setExpressCase("N");
														}

														reviewerVO.setCurrentCycle(
																(String) mapForReview.get("ProcessCycle"));
														reviewerVO.setCountry(
																(String) mapForReview.get("PrimarySubjectCountry"));
														reviewerVO.setPrimarySubject(
																(String) mapForReview.get("PrimarySubject"));
														this.setDueDateForCurrentProcessCycle(reviewerVO);
														teampList.add(reviewerVO);
														checkFlag = true;
														this.logger.debug("teampList size::" + teampList.size());
														this.logger.debug("Inside primary team initials");
													} else {
														this.logger.debug("inside second do nothing");
													}
												}
											}
										}
									}

									if (checkFlag) {
										break;
									}
								}
							}
						}
					}
				} while (cycleTeamMap.getCycleInformation().get(cycleName) == null);

				String teamName = teamDetails.getTeamName();
				this.logger.debug("team name is " + teamName);
				String[] temp = this.getActivatedWSNames(session, pid);
				long reviewPID = ((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation().get(cycleName))
						.getTeamInfo().get(teamName)).getReviewProcessPID();
				boolean isMyIncomingReview = true;
				if (0L != reviewPID && !this.isTaskCompleted(reviewPID, session)) {
					Vector<String> reviewersVector = (Vector) this.getDataslotValue(reviewPID, "Reviewers", session);
					if (!reviewersVector.contains(userId)) {
						isMyIncomingReview = false;
					}
				}

				this.logger.debug("isMyIncomingReview::" + isMyIncomingReview);
				if (!isMyIncomingReview && cycleName.equals("Final")) {
					this.logger.debug(" else isMyIncomingReview");
				} else {
					List<String> tempStringList = Arrays.asList(temp);
					this.logger.debug("tempStringList::" + tempStringList);
					if (!tempStringList.contains("ProcessCompletion")
							&& !tempStringList.contains("Client Submission Task") && !tempStringList.contains("And 2")
							|| !tempStringList.contains("ProcessCompletion")
									&& !tempStringList.contains("Client Submission Task")
									&& !tempStringList.contains("Invoicing Task") && tempStringList.contains("And 2")
							|| !cycleName.equals("Final")) {
						this.logger.debug(" start isMyIncomingReview");
						MyTaskPageVO reviewerVO = new MyTaskPageVO();
						Map<String, Object> mapForReview = this.getAllDataslotValues(pid, session);
						reviewerVO.setCrn((String) mapForReview.get("CRN"));
						reviewerVO.setCfinal((String) mapForReview.get("CFinal"));
						reviewerVO.setClientfinalDueDate((String) mapForReview.get("RFinal"));
						if (!"".equalsIgnoreCase(tempCycleNames[2])) {
							reviewerVO.setCinterim1((String) mapForReview.get("CInterim1"));
						}

						if (!"".equalsIgnoreCase(tempCycleNames[1])) {
							reviewerVO.setCinterim2((String) mapForReview.get("CInterim2"));
						}

						if (mapForReview.get("TeamTypeList").toString() != null) {
							String teamType = mapForReview.get("TeamTypeList").toString();
							String[] team = teamType.split("#");
							String teamAssignmentTask = "Review Task".concat(team[0]).concat("]");
							reviewerVO.setTaskName(teamAssignmentTask);
							analystName = mapForReview.get("TeamCycleName").toString();
							String teamtypeList = mapForReview.get("TeamTypeList").toString();
							teamAssignmentTask = ResourceLocator.self().getTaskService().getAnalysts(
									mapForReview.get("CRN").toString(),
									analystName.substring(1, analystName.length() - 1),
									teamtypeList.substring(1, teamtypeList.length() - 1));
							reviewerVO.setAnalysts(teamAssignmentTask);
						} else {
							reviewerVO.setAnalysts("");
							reviewerVO.setTaskName("Review Task");
						}

						if (tempStringList.contains("Consolidation Task")) {
							reviewerVO.setCurrentTask("Consolidation Task");
						} else if (tempStringList.contains("Review Task")) {
							reviewerVO.setCurrentTask("Review Task");
						} else if (tempStringList.contains("FinalReview")) {
							reviewerVO.setCurrentTask("Review Task");
							reviewerVO.setTaskName("Review Task");
						} else {
							reviewerVO.setCurrentTask("Research");
						}

						if ((Boolean) mapForReview.get("ExpressCase")) {
							reviewerVO.setExpressCase("Y");
						} else {
							reviewerVO.setExpressCase("N");
						}

						reviewerVO.setCurrentCycle(cycleName);
						reviewerVO.setCountry((String) mapForReview.get("PrimarySubjectCountry"));
						reviewerVO.setPrimarySubject((String) mapForReview.get("PrimarySubject"));
						this.setDueDateForCurrentProcessCycle(reviewerVO);
						teampList.add(reviewerVO);
						this.logger.debug("teampList size::" + teampList.size());
						this.logger.debug(" end isMyIncomingReview");
					}
				}
			}
		}
	}

	private void setDueDateForCurrentProcessCycle(MyTaskPageVO reviewerVO) throws CMSException {
		reviewerVO.setDueDateForCurrentCycle(reviewerVO.getClientfinalDueDate());
		this.logger.debug("cycle = " + reviewerVO.getCurrentCycle() + ", cinterim1 = " + reviewerVO.getCinterim1()
				+ ", cinterim2 = " + reviewerVO.getCinterim2() + ", cfinal = " + reviewerVO.getClientfinalDueDate());
		if (reviewerVO.getCurrentCycle() != null && reviewerVO.getCurrentCycle().equalsIgnoreCase("Interim1")) {
			reviewerVO.setDueDateForCurrentCycle(reviewerVO.getCinterim1());
		} else if (reviewerVO.getCurrentCycle() != null && reviewerVO.getCurrentCycle().equalsIgnoreCase("Interim2")) {
			reviewerVO.setDueDateForCurrentCycle(reviewerVO.getCinterim2());
		}

		this.logger.debug("DDFORCC :: " + reviewerVO.getDueDateForCurrentCycle());
		String dueDateforCC = "";
		SimpleDateFormat sourceFormat = null;

		try {
			String[] dateTokens = reviewerVO.getDueDateForCurrentCycle().split("-");
			this.logger.debug("date month - " + dateTokens[1] + " and month length - " + dateTokens[1].length());
			if (dateTokens[1].length() == 3) {
				sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
				this.logger.debug("sourcedate format is dd-MMM-yyyy");
			} else {
				sourceFormat = new SimpleDateFormat("dd-MM-yy");
				this.logger.debug("sourcedate format is dd-MM-yy");
			}

			SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
			dueDateforCC = targetFormat.format(sourceFormat.parse(reviewerVO.getDueDateForCurrentCycle()));
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		reviewerVO.setDueDateForCurrentCycle(dueDateforCC);
	}

	public List<WorkItem> myTeamTask(String users, String status, String ptName) throws CMSException {
		new ArrayList();
		SBMTaskManager sbmTaskManager = new SBMTaskManager();
		String[] user = users.split(",");
		Session[] session = new Session[user.length];

		int i;
		for (i = 0; i < user.length; ++i) {
			session[i] = this.sessionManager.getSession(user[i]);
		}

		List<WorkItem> list = sbmTaskManager.myTeamTask(session, user, status, ptName);

		for (i = 0; i < user.length; ++i) {
			this.sessionManager.closeSession(session[i]);
		}

		return list;
	}

	public void reassignTask(long piid, String newUser, String wsName, Session session) throws CMSException {
		this.sbmTaskManager.reassignTask(piid, newUser, wsName, session);
	}

	public Session getSession(Session session) throws CMSException {
		return session;
	}

	public Session getSession(String UserID) throws CMSException {
		return this.sessionManager.getSession(UserID);
	}

	public long getUserID(String userId) throws CMSException {
		return this.userManager.getUserId(userId);
	}

	public String getPassword(String userId) throws CMSException {
		return this.userManager.getPassword(userId);
	}

	public boolean isUserAdded(String userName, UserCreationVO userCreationVO) throws CMSException {
		return this.userManager.isUserAdded(userName, userCreationVO);
	}

	public String encryptText(String text) throws CMSException {
		return this.userManager.encrypt(text);
	}

	public String decryptText(String text) throws CMSException {
		return this.userManager.decrypt(text);
	}

	public boolean isUserRemoved(String userName) throws CMSException {
		return this.userManager.isUserRemoved(userName);
	}

	public boolean isPaswordUpdated(String userName, String password) throws CMSException {
		return this.userManager.isPassWordUpdated(userName, password);
	}

	public String getEmailId(String userName) throws CMSException {
		return this.userManager.getEmailId(userName);
	}

	public boolean isEmailUpdated(String userName, String emailId) throws CMSException {
		return this.userManager.isEmailUpdated(userName, emailId);
	}

	public boolean isFullNameUpdated(String userName, String fName, String lName) throws CMSException {
		return this.userManager.isFullNameUpdated(userName, fName, lName);
	}

	public boolean isEmailIdExists(String emailId) throws CMSException {
		return this.userManager.isEmailIdExists(emailId);
	}

	public void removeProcessInstance(long pid) throws CMSException {
		Session session = this.sessionManager.getSession();
		this.sbmTaskManager.removePI(session, pid);
		this.sessionManager.closeSession(session);
	}

	public HashMap<String, Object> getMyTasks(Session session, String condition, String statusCondition, int limit,
			int start, boolean hasOnlyFinanceRole, String commaSeparatedUserList, boolean isTATask, boolean hasBIRole)
			throws CMSException {
		HashMap hashMap = null;

		try {
			this.logger.info("GetMyTask called for user " + session.getUser() + " Starts ");
			this.logger.debug("=========================GetMyTask called for user " + session.getUser()
					+ " Starts ==================== ");
			TaskManagementService serviceObject = ResourceLocator.self().getTaskService();
			if (statusCondition != null && !"".equalsIgnoreCase(statusCondition.trim())) {
				statusCondition = " AND " + statusCondition;
			}

			if (hasOnlyFinanceRole) {
				statusCondition = statusCondition + " ORDER BY CASESTATUS ";
			} else if (hasBIRole) {
				statusCondition = statusCondition
						+ " ORDER BY substr(sav.CRN,INSTR(sav.CRN,'\\', 15, 1)+1,4) DESC, sav.CRN DESC ";
			} else {
				statusCondition = statusCondition
						+ " ORDER BY substr(sav.CRN,INSTR(sav.CRN,'\\', 15, 1)+1,4) ASC, sav.CRN ASC ";
			}

			this.logger.debug("=========================GetMyTask status Condition " + statusCondition
					+ " Starts ==================== ");
			hashMap = this.taskSBMUtil.getUserTask(
					serviceObject.getSavvionTasks(session.getUser(), condition, statusCondition, limit, start,
							commaSeparatedUserList),
					serviceObject.getSavvionTasksCount(session.getUser(), condition, statusCondition,
							commaSeparatedUserList),
					session, isTATask);
			this.logger.debug("==========================GetMyTask called for user " + session.getUser()
					+ " Ends.======================");
			this.logger.info("GetMyTask called for user " + session.getUser() + " Ends. ");
			return hashMap;
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public List<MyTaskPageVO> getUnCompletedCases(Session session, String userId) throws CMSException {
		List caseList = null;

		try {
			caseList = this.taskSBMUtil.getUnCompletedCases(userId, session);
			return caseList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<MyTaskPageVO> getMyTasks(Session session, String condition) throws CMSException {
		new ArrayList();

		try {
			List<MyTaskPageVO> resultList = this.taskSBMUtil.getUserTask(condition, session);
			return resultList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public HashMap<String, Object> getBulkDataForUser(Session session, String taskType, MyTaskPageVO taskVo)
			throws CMSException {
		HashMap<String, Object> resultMap = null;
		String condition = "";
		boolean isTATask = false;

		try {
			if ("Team".equalsIgnoreCase(taskType)) {
				condition = condition + " AND WI.WORKSTEP_NAME = ''" + "Team Assignment Task" + "'' ";
				isTATask = true;
			} else if ("Office".equalsIgnoreCase(taskType)) {
				condition = condition + " AND WI.WORKSTEP_NAME = ''" + "Office Assignment Task" + "'' ";
			}

			condition = condition + " AND (BLIDS." + "CaseStatus" + " <>''" + "On Hold" + "'')";
			if (taskVo.getCrn() != null && !taskVo.getCrn().trim().equals("")) {
				condition = condition + " AND BLIDS.CRN LIKE ''%" + taskVo.getCrn() + "%'' ";
			}

			String caseManager;
			if (taskVo.getCountry() != null && !taskVo.getCountry().trim().equals("")) {
				caseManager = taskVo.getCountry();
				if (caseManager.contains("'")) {
					caseManager = caseManager.replaceAll("'", "''''");
				}

				condition = condition + " AND BLIDS." + "PrimarySubjectCountry" + " = ''" + caseManager + "'' ";
			}

			if ("Team".equalsIgnoreCase(taskType)) {
				if (taskVo.getCaseManager() != null && !taskVo.getCaseManager().trim().equals("")) {
					caseManager = taskVo.getCaseManager();
					if (caseManager.contains("'")) {
						caseManager = caseManager.replaceAll("'", "''''");
					}

					condition = condition + " AND BLIDS." + "CaseManager" + " = ''" + caseManager + "'' ";
				}

				if (taskVo.getStartClientDueDate() != null && !taskVo.getStartClientDueDate().trim().equals("")) {
					condition = condition + " AND to_date(BLIDS." + "CFinal" + ",''dd-Mon-yyyy'') >= to_date(''"
							+ taskVo.getStartClientDueDate() + "'',''dd-Mon-yyyy'') ";
				}

				if (taskVo.getEndClientDueDate() != null && !taskVo.getEndClientDueDate().trim().equals("")) {
					condition = condition + " AND to_date(BLIDS." + "CFinal" + ",''dd-Mon-yyyy'') <= to_date(''"
							+ taskVo.getEndClientDueDate() + "'',''dd-Mon-yyyy'') ";
				}
			}

			this.logger.debug("in bulk order query. condition is " + condition);
			resultMap = this.getMyTasks(session, condition, "", taskVo.getLimit(), taskVo.getStart(), false, "''",
					isTATask, false);
			return resultMap;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<MyTaskPageVO> getBulkTADataForUser(Session session, String taskType) throws CMSException {
		new ArrayList();
		String condition = "";
		if ("Team".equalsIgnoreCase(taskType)) {
			condition = "BLWSI.WORKSTEP_NAME = 'Team Assignment Task'";
		} else if ("Office".equalsIgnoreCase(taskType)) {
			condition = "BLWSI.WORKSTEP_NAME = 'Office Assignment Task'";
		}

		try {
			condition = condition + " AND (BLIDS." + "CaseStatus" + "<>'" + "On Hold" + "' AND BLIDS." + "CaseStatus"
					+ "<>'" + "Cancelled" + "')";
			this.logger.debug("in bulk order query. condition is " + condition);
			List<MyTaskPageVO> resultList = this.taskSBMUtil.getUserTask(condition, session);
			return resultList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<MyTaskPageVO> getTeamAssignmentUser(String userId) throws CMSException {
		Session session = this.getSession(userId);
		new ArrayList();
		String condition = "";
		condition = "BLWSI.WORKSTEP_NAME = 'Team Assignment Task'";

		List resultList;
		try {
			condition = condition + " AND (BLIDS." + "CaseStatus" + "<>'" + "Cancelled" + "')";
			this.logger.debug("in bulk order query. condition is " + condition);
			resultList = this.taskSBMUtil.getUserTask(condition, session);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.closeSession(session);
		return resultList;
	}

	public void closeSession(Session userSession) throws CMSException {
		this.sessionManager.closeSession(userSession);
	}

	public void updateDSForOfficeTask(long pid, CycleTeamMapping cycleTeamMap, Session session) throws CMSException {
		this.sbmTaskManager.updateDSForOfficeTask(pid, cycleTeamMap, session);
	}

	public void updateDSForTeamTask(long pid, CycleTeamMapping cycleTeamMap, Session session) throws CMSException {
		this.sbmTaskManager.updateDSForTeamTask(pid, cycleTeamMap, session);
	}

	public String[] getTeamNames(long pid, String processCycle, Session session) throws CMSException {
		return this.sbmTaskManager.teamName(pid, processCycle, session);
	}

	public long getParentPID(long pid, Session session) throws CMSException {
		long parentPID = 0L;
		parentPID = this.sbmTaskManager.getParentPID(pid, session);
		return parentPID;
	}

	public String[] getWSNames(long pid, Session session) throws CMSException {
		return this.sbmTaskManager.getWSNames(pid, session);
	}

	public boolean isWSCompleted(long pid, String wsName, Session session) throws CMSException {
		boolean isWSCompleted = false;
		String[] wsNames = this.getCompletedWSNames(pid, session);
		if (wsNames != null) {
			for (int i = 0; i < wsNames.length; ++i) {
				String string = wsNames[i];
				if (wsName.equalsIgnoreCase(string)) {
					isWSCompleted = true;
					break;
				}
			}
		}

		return isWSCompleted;
	}

	public String[] getCompletedWSNames(long pid, Session session) throws CMSException {
		return this.sbmTaskManager.getCompleteWSNames(pid, session);
	}

	public void bulkTaskModification(Map<String, HashMap<String, Object>> mapOfData, String taskType,
			boolean isRequestForCompletion) throws CMSException {
		Session session = this.sessionManager.getSession();
		this.sbmTaskManager.bulkTaskModification(session, mapOfData, taskType, isRequestForCompletion);
		this.sessionManager.closeSession(session);
	}

	public boolean isUserAddedToQueue(String userName) throws CMSException {
		Properties atlasProp = null;
		boolean isUserAdded = false;

		try {
			atlasProp = ResourceUtil.getPropertyObject("atlas.properties");
			isUserAdded = this.userManager.isUserAddedToQueue(userName, atlasProp.getProperty("atlas.Finance.Queue"));
			return isUserAdded;
		} catch (IOException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public boolean isUserRemovedFromQueue(String userName) throws CMSException {
		Properties atlasProp = null;
		boolean isUserRemoved = false;

		try {
			atlasProp = ResourceUtil.getPropertyObject("atlas.properties");
			isUserRemoved = this.userManager.removeQueueMember(userName, atlasProp.getProperty("atlas.Finance.Queue"));
			return isUserRemoved;
		} catch (IOException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public boolean isTaskCompleted(long pid, Session session) {
		return this.sbmTaskManager.isTaskCompleted(pid, session);
	}

	public List<String> getAllProcessCycles(long pid, Session session) throws CMSException {
		return this.sbmTaskManager.getAllCycleNames(pid, session);
	}

	public List<String> getAllProcessCycles(String workItemName, String teamName, Session session) throws CMSException {
		return this.sbmTaskManager.getAllCycleNames(workItemName, teamName, session);
	}

	public Map<String, Object> getCaseStatus(Session superUserSession, String crn) throws Exception {
		CaseSBMUtil caseSBMUtil = new CaseSBMUtil();
		caseSBMUtil.setSession(superUserSession);
		return caseSBMUtil.getCaseStatus(crn, superUserSession);
	}

	public Session getSession() throws CMSException {
		return this.sessionManager.getSession();
	}

	public boolean isReviewerAvalableForDeletion(long pid, String teamName, String reviewerId, Session session)
			throws CMSException {
		return this.sbmTaskManager.isReviewerAvalableForDeletion(pid, teamName, reviewerId, session);
	}

	public List<MyTaskPageVO> getAllTaskForCrn(String crn) throws CMSException {
		Session session = this.sessionManager.getSession();
		List<MyTaskPageVO> list = this.taskSBMUtil.getAllTaskForCrn(crn, session);
		this.sessionManager.closeSession(session);
		return list;
	}

	public List<MyTaskPageVO> getAllTaskForCrnList(List<String> crnList) throws CMSException {
		Session session = this.sessionManager.getSession();
		List<MyTaskPageVO> list = this.taskSBMUtil.getAllTaskForCrnList(crnList, session);
		this.sessionManager.closeSession(session);
		return list;
	}

	public CaseHistory getCaseDetails(String workItemName) throws CMSException {
		CaseHistory caseHistoryVO = new CaseHistory();
		long pid = Long.parseLong(workItemName.split("::")[0].split("#")[1]);
		String processName = workItemName.split("::")[0].split("#")[0];
		String taskName = workItemName.split("::")[1];
		caseHistoryVO.setPid(String.valueOf(pid));
		caseHistoryVO.setTaskName(taskName);
		Session session = this.getSession();
		caseHistoryVO.setCRN((String) this.getDataslotValue(pid, "CRN", session));
		String processCycle = "";
		if (taskName.equals("Research Task")) {
			processCycle = (String) this.getDataslotValue(pid, "TeamCycleName", session);
		} else if (taskName.equals("BI Research Task")) {
			processCycle = (String) this.getDataslotValue(pid, "BITeamCycleName", session);
		} else if (taskName.equals("Consolidation Task")) {
			if (!"CaseCreation".equalsIgnoreCase(processName)) {
				processCycle = (String) this.getDataslotValue(pid, "TeamCycleName", session);
			} else {
				processCycle = null;
			}

			if (processCycle == null || "null".equalsIgnoreCase(processCycle)
					|| "<NULL>".equalsIgnoreCase(processCycle)) {
				processCycle = (String) this.getDataslotValue(pid, "ProcessCycle", session);
			}
		} else if (taskName.equals("Review Task")) {
			processCycle = (String) this.getDataslotValue(pid, "TeamCycleName", session);
			if (processCycle == null || "null".equalsIgnoreCase(processCycle)
					|| "<NULL>".equalsIgnoreCase(processCycle)) {
				processCycle = (String) this.getDataslotValue(pid, "ProcessCycle", session);
			}
		} else {
			processCycle = (String) this.getDataslotValue(pid, "ProcessCycle", session);
		}

		this.logger.debug("Process cycle is " + processCycle);
		caseHistoryVO.setProcessCycle(processCycle);
		if ("CaseCreation".equalsIgnoreCase(processName)) {
			if ("Client Submission Task".equalsIgnoreCase(taskName)) {
				caseHistoryVO.setOldInfo((String) this.getDataslotValue(pid, "ClientSubmissionTaskStatus", session));
			} else if ("Office Assignment Task".equalsIgnoreCase(taskName)) {
				caseHistoryVO.setOldInfo((String) this.getDataslotValue(pid, "OfficeTaskSTatus", session));
			} else if ("Consolidation Task".equalsIgnoreCase(taskName)) {
				caseHistoryVO.setOldInfo((String) this.getDataslotValue(pid, "ConsolidationTaskStatus", session));
			}
		} else {
			caseHistoryVO.setOldInfo((String) this.getDataslotValue(pid, "TaskStatus", session));
		}

		this.closeSession(session);
		return caseHistoryVO;
	}

	public List<Long> getAllPIDSOfTheCase(long pid) throws CMSException {
		List<Long> getAllPIDS = new ArrayList();
		Session session = this.getSession();
		HashMap<String, CycleTeamMapping> objectMap = (HashMap) this.getDataslotValue(pid, "customDSMap", session);
		if (objectMap != null) {
			CycleTeamMapping cycleTeamMap = (CycleTeamMapping) objectMap.get("CycleTeamMapping");
			this.closeSession(session);
			Map<String, TeamAnalystMapping> teamMap = null;
			Map<String, AnalystTaskStatus> analystMap = null;
			long tempPid = 0L;
			Iterator iterator;
			String teamName;
			Iterator iterator2;
			String analystName;
			if (cycleTeamMap != null && cycleTeamMap.getCycleInformation().containsKey("Interim1")) {
				teamMap = ((CycleInfo) cycleTeamMap.getCycleInformation().get("Interim1")).getTeamInfo();
				iterator = teamMap.keySet().iterator();

				while (iterator.hasNext()) {
					teamName = (String) iterator.next();
					analystMap = ((TeamAnalystMapping) teamMap.get(teamName)).getAnalystTaskStatus();
					tempPid = ((TeamAnalystMapping) teamMap.get(teamName)).getResearchProcessPID();
					if (0L != tempPid) {
						getAllPIDS.add(tempPid);
					}

					tempPid = ((TeamAnalystMapping) teamMap.get(teamName)).getReviewProcessPID();
					if (0L != tempPid) {
						getAllPIDS.add(tempPid);
					}

					iterator2 = analystMap.keySet().iterator();

					while (iterator2.hasNext()) {
						analystName = (String) iterator2.next();
						tempPid = ((AnalystTaskStatus) analystMap.get(analystName)).getResearchPID();
						if (0L != tempPid) {
							getAllPIDS.add(tempPid);
						}
					}
				}
			}

			if (cycleTeamMap != null && cycleTeamMap.getCycleInformation().containsKey("Interim2")) {
				teamMap = ((CycleInfo) cycleTeamMap.getCycleInformation().get("Interim2")).getTeamInfo();
				iterator = teamMap.keySet().iterator();

				while (iterator.hasNext()) {
					teamName = (String) iterator.next();
					analystMap = ((TeamAnalystMapping) teamMap.get(teamName)).getAnalystTaskStatus();
					tempPid = ((TeamAnalystMapping) teamMap.get(teamName)).getResearchProcessPID();
					if (0L != tempPid) {
						getAllPIDS.add(tempPid);
					}

					tempPid = ((TeamAnalystMapping) teamMap.get(teamName)).getReviewProcessPID();
					if (0L != tempPid) {
						getAllPIDS.add(tempPid);
					}

					iterator2 = analystMap.keySet().iterator();

					while (iterator2.hasNext()) {
						analystName = (String) iterator2.next();
						tempPid = ((AnalystTaskStatus) analystMap.get(analystName)).getResearchPID();
						if (0L != tempPid) {
							getAllPIDS.add(tempPid);
						}
					}
				}
			}

			if (cycleTeamMap != null && cycleTeamMap.getCycleInformation().containsKey("Final")) {
				teamMap = ((CycleInfo) cycleTeamMap.getCycleInformation().get("Final")).getTeamInfo();
				iterator = teamMap.keySet().iterator();

				while (iterator.hasNext()) {
					teamName = (String) iterator.next();
					analystMap = ((TeamAnalystMapping) teamMap.get(teamName)).getAnalystTaskStatus();
					tempPid = ((TeamAnalystMapping) teamMap.get(teamName)).getResearchProcessPID();
					if (0L != tempPid) {
						getAllPIDS.add(tempPid);
					}

					tempPid = ((TeamAnalystMapping) teamMap.get(teamName)).getReviewProcessPID();
					if (0L != tempPid) {
						getAllPIDS.add(tempPid);
					}

					iterator2 = analystMap.keySet().iterator();

					while (iterator2.hasNext()) {
						analystName = (String) iterator2.next();
						tempPid = ((AnalystTaskStatus) analystMap.get(analystName)).getResearchPID();
						if (0L != tempPid) {
							getAllPIDS.add(tempPid);
						}
					}
				}
			}
		}

		this.logger.debug("size of list is " + getAllPIDS);
		return getAllPIDS;
	}

	public List<String> getTeamAssignmentDone(long pid, Session session) throws CMSException {
		List<String> listOfTeamNames = new ArrayList();
		CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((HashMap) this.getDataslotValue(pid, "customDSMap",
				session)).get("CycleTeamMapping");
		Map<String, TeamAnalystMapping> teamMap = null;
		Iterator iterator;
		String teamName;
		if (cycleTeamMap.getCycleInformation().containsKey("Interim1")) {
			teamMap = ((CycleInfo) cycleTeamMap.getCycleInformation().get("Interim1")).getTeamInfo();
			iterator = teamMap.keySet().iterator();

			while (iterator.hasNext()) {
				teamName = (String) iterator.next();
				if (((TeamAnalystMapping) teamMap.get(teamName)).isTeamAssignmentDone()
						&& !listOfTeamNames.contains(teamName)) {
					listOfTeamNames.add(teamName);
				}
			}
		}

		if (cycleTeamMap.getCycleInformation().containsKey("Interim2")) {
			teamMap = ((CycleInfo) cycleTeamMap.getCycleInformation().get("Interim2")).getTeamInfo();
			iterator = teamMap.keySet().iterator();

			while (iterator.hasNext()) {
				teamName = (String) iterator.next();
				if (((TeamAnalystMapping) teamMap.get(teamName)).isTeamAssignmentDone()
						&& !listOfTeamNames.contains(teamName)) {
					listOfTeamNames.add(teamName);
				}
			}
		}

		if (cycleTeamMap.getCycleInformation().containsKey("Final")) {
			teamMap = ((CycleInfo) cycleTeamMap.getCycleInformation().get("Final")).getTeamInfo();
			iterator = teamMap.keySet().iterator();

			while (iterator.hasNext()) {
				teamName = (String) iterator.next();
				if (((TeamAnalystMapping) teamMap.get(teamName)).isTeamAssignmentDone()
						&& !listOfTeamNames.contains(teamName)) {
					listOfTeamNames.add(teamName);
				}
			}
		}

		this.logger.debug("size of the list is " + listOfTeamNames.size());
		return listOfTeamNames;
	}

	public boolean isWsCompleted(String userId, String wsName, long pid, Session session) throws CMSException {
		return this.sbmTaskManager.isWsCompleted(userId, wsName, pid, session);
	}

	public void checkAndSendNotification(String crn, String notificationType, Session session, String processCycle)
			throws CMSException {
		this.sbmNotificationManager.checkAndSendNotification(crn, notificationType, session, processCycle);
	}

	public String getworkItemName(long pid, String workStepName) throws CMSException {
		String wiName = "";
		Session session = this.getSession();
		wiName = this.sbmTaskManager.getworkItemName(pid, workStepName, session);
		this.closeSession(session);
		this.logger.debug("wiName is " + wiName);
		return wiName;
	}

	public boolean isOACompleted(String crn, String userID) throws CMSException {
		return this.taskSBMUtil.isOACompleted(crn, userID);
	}

	public boolean wiExistsForUser(String workItemName, Session session, String userName) {
		return this.sbmTaskManager.wiExistsForUser(workItemName, session, userName);
	}

	public boolean isReviewCompleted(long pid, Session session, String teamName) throws CMSException {
		boolean isReviewCompleted = false;
		if (!this.isTaskCompleted(pid, session)) {
			String caseStatus = (String) this.getDataslotValue(pid, "CaseStatus", session);
			if ("Completed Client Submission".equalsIgnoreCase(caseStatus)
					&& "Completed".equalsIgnoreCase(caseStatus)) {
				isReviewCompleted = true;
			} else {
				String processCycle = (String) this.getDataslotValue(pid, "ProcessCycle", session);
				this.logger.debug("process cycle is " + processCycle);
				if ("Final".equalsIgnoreCase(processCycle)) {
					if (teamName.contains("Primary")) {
						List<String> listOfActivatedWS = Arrays.asList(this.getActivatedWSNames(session, pid));
						this.logger.debug("size of the list is " + listOfActivatedWS.size());
						if (listOfActivatedWS.contains("Client Submission Task") || listOfActivatedWS.contains("And 2")
								&& listOfActivatedWS.contains("Invoicing Task")) {
							isReviewCompleted = true;
						}
					} else {
						CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((HashMap) this.getDataslotValue(pid,
								"customDSMap", session)).get("CycleTeamMapping");
						if (cycleTeamMap != null) {
							CycleInfo cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation().get("Final");
							if (cycleInfo != null) {
								TeamAnalystMapping teamMap = (TeamAnalystMapping) cycleInfo.getTeamInfo().get(teamName);
								if (teamMap != null) {
									long rPID = teamMap.getResearchProcessPID();
									this.logger.debug("Research process id is " + rPID);
									if (0L != rPID && this.isTaskCompleted(rPID, session)) {
										isReviewCompleted = true;
									}
								}
							}
						}
					}
				}
			}
		} else {
			isReviewCompleted = true;
		}

		this.logger.debug("isReviewCompleted value is " + isReviewCompleted);
		return isReviewCompleted;
	}

	public List<Long> getPIDListfromSavvionforAtlasCase(String crn) {
		this.logger.debug("inside getPIDListfromSavvionforAtlasCase for CRN :" + crn);
		return this.sbmTaskManager.getPIDListfromSavvionforAtlasCase(crn);
	}
}