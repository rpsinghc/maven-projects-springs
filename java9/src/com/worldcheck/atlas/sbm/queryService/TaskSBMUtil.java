package com.worldcheck.atlas.sbm.queryService;

import com.savvion.sbm.bizlogic.client.queryservice.QSWorkItem;
import com.savvion.sbm.bizlogic.client.queryservice.QSWorkItemFilter;
import com.savvion.sbm.bizlogic.client.queryservice.QSWorkStepInstance;
import com.savvion.sbm.bizlogic.client.queryservice.QSWorkStepInstanceRS;
import com.savvion.sbm.bizlogic.client.queryservice.QueryService;
import com.savvion.sbm.bizlogic.client.queryservice.WorkStepInstanceFilter;
import com.savvion.sbm.bizlogic.server.svo.DateTime;
import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.server.svo.QSWorkStepInstanceData;
import com.savvion.sbm.bizlogic.server.svo.WorkItem;
import com.savvion.sbm.bizlogic.server.svo.WorkItemList;
import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.bizsite.util.HexEncoder;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.AnalystTaskStatus;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.task.CustomTaskVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.Vector;

public class TaskSBMUtil {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.queryService.TaskSBMUtil");
	private static String PROCESS_INSTANCE_LIST = "processInstanceList";
	private String[] processNames = new String[]{"CaseCreation", "BIVendorResearch", "ResearchProcess", "ResearchTask",
			"Review", "TeamAssignment"};
	private List list = null;

	public HashMap<String, Object> getUserTask(List<CustomTaskVO> listOfTasks, long taskCount, Session session,
			boolean isTATask) throws CMSException {
		HashMap<String, Object> resultMap = new HashMap();
		ArrayList presentationList = new ArrayList();

		try {
			this.logger.debug(" TaskSBMUtil getUserTask Start" + listOfTasks);
			Iterator var9 = listOfTasks.iterator();

			while (true) {
				if (!var9.hasNext()) {
					resultMap.put("total", taskCount);
					resultMap.put(PROCESS_INSTANCE_LIST, presentationList);
					break;
				}

				CustomTaskVO taskObject = (CustomTaskVO) var9.next();
				ProcessInstance pInstance = ProcessInstance.get(session, taskObject.getProcessInstanceID());
				Map<String, Object> dsMap = pInstance.getDataSlotValue();
				if ("WaitForConsolidation".equalsIgnoreCase(taskObject.getWorkStepName())) {
					presentationList.add(this.getAwaitingCustomTaskObject(taskObject, dsMap, pInstance.getAppName()));
					this.logger.debug(" TaskSBMUtil getUserTask Start 2 " + listOfTasks);
				} else {
					presentationList.add(this.getCustomTaskObject(taskObject, dsMap, pInstance.getAppName(), isTATask));
					this.logger.debug(" TaskSBMUtil getUserTask Start 3 " + dsMap);
				}
			}
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("Task Ends " + new Date(System.currentTimeMillis()));
		return resultMap;
	}

	private List<MyTaskPageVO> getWaitForConsolidationTask(String condition, Session session) throws CMSException {
		ArrayList waitForConsolidationTaskList = new ArrayList();

		try {
			if (condition.contains("TaskStatus")) {
				condition = this.getConditionForCaseCreationPT(condition);
			}

			if (condition.contains("Awaiting Consolidation")) {
				condition = condition.replaceAll("AND BLWSI.WORKSTEP_NAME = 'Awaiting Consolidation'", " ");
			}

			condition = "BLWSI.STATUS = 62 AND BLIDS.MainAnalyst='" + session.getUser()
					+ "' AND BLWSI.WORKSTEP_NAME = 'WaitForConsolidation' AND " + condition;
			this.logger.debug("Awaiting consolidation condition is " + condition);
			List waitWorkstepList = this.getWorkStepInstanceList("CaseCreation", (String) null, condition,
					SBMQueryManager.getQueryService());
			if (waitWorkstepList != null && !waitWorkstepList.isEmpty()) {
				Iterator iterator = waitWorkstepList.iterator();

				while (iterator.hasNext()) {
					QSWorkStepInstanceData qsWorkStepInstanceData = (QSWorkStepInstanceData) iterator.next();
					ProcessInstance pInstance = ProcessInstance.get(session,
							qsWorkStepInstanceData.getProcessInstanceID());
					Map<String, Object> dsMap = pInstance.getDataSlotValue();
					if (this.checkForWaitForCon(dsMap)) {
						waitForConsolidationTaskList.add(this.getWaitTaskInfoObject(qsWorkStepInstanceData.getName(),
								pInstance.getAppName(), session.getUser(), dsMap));
					}
				}
			}
		} catch (Exception var9) {
			new CMSException();
		}

		return waitForConsolidationTaskList;
	}

	private boolean checkForWaitForCon(Map dsMap) throws CMSException {
		String cycleName = (String) dsMap.get("ProcessCycle");
		Map customDSMap = null;
		boolean consolidationFlag = false;

		try {
			if (dsMap != null) {
				customDSMap = (Map) dsMap.get("customDSMap");
			}

			if (customDSMap != null && !customDSMap.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
					if (cycleInfo != null) {
						HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
						if (teamInfoMap != null) {
							TeamAnalystMapping analystMapping = null;
							Set teamNameSet = teamInfoMap.keySet();
							Iterator teamItr = teamNameSet.iterator();
							String teamTypeList = "";

							while (teamItr.hasNext()) {
								teamTypeList = (String) teamItr.next();
								if (teamTypeList.contains("Primary")) {
									break;
								}
							}

							analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
							if (analystMapping != null) {
								consolidationFlag = analystMapping.getResearchTaskStatus().equals("Done");
							}
						}
					}
				}
			}

			return consolidationFlag;
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	public List<MyTaskPageVO> getUserTask(String condition, Session session) throws CMSException {
		List<MyTaskPageVO> presentationList = new ArrayList();
		ArrayList workItemList = new ArrayList();

		try {
			this.logger.debug("condition " + condition);
			if (condition != null && !condition.isEmpty()) {
				for (int i = 0; i < this.processNames.length; ++i) {
					if (this.processNames[i].equals("CaseCreation") && condition.contains("TaskStatus")) {
						if (condition.contains("TaskStatus in (")) {
							workItemList.addAll(this.getWorkItemsList(this.getConditionForTeamTask(condition),
									this.processNames[i], session));
						} else {
							workItemList.addAll(this.getWorkItemsList(this.getConditionForCaseCreationPT(condition),
									this.processNames[i], session));
						}
					} else {
						workItemList.addAll(this.getWorkItemsList(condition, this.processNames[i], session));
					}
				}
			} else {
				workItemList.addAll(this.getWorkItemsList((String) null, (String) null, session));
			}

			if (workItemList != null && !workItemList.isEmpty()) {
				presentationList = this.getPresentionObject(workItemList, session);
			}

			return (List) presentationList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<MyTaskPageVO> getUnCompletedCases(String userID, Session session) throws CMSException {
		ArrayList unCompletedTasks = new ArrayList();

		try {
			String condition = " BLWSI.WORKSTEP_NAME like '%Client Submission Task%' AND BLIDS.ProcessCycle='Final'";
			unCompletedTasks.addAll(this.getUnCompletedObjectForUser(
					this.getWorkItemsList(condition, this.processNames[0], session), userID, session));
			condition = " BLWSI.WORKSTEP_NAME like '%Review Task%' AND BLIDS.ProcessCycle='Final'";
			unCompletedTasks.addAll(this.getUnCompletedObjectForUser(
					this.getWorkItemsList(condition, this.processNames[4], session), userID, session));
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Task Ends " + new Date(System.currentTimeMillis()));
		return unCompletedTasks;
	}

	private List getUnCompletedObjectForUser(List workItemList, String userId, Session session) throws CMSException {
		ArrayList presentionObj = new ArrayList();

		try {
			Iterator iterator = workItemList.iterator();

			while (iterator.hasNext()) {
				WorkItem workItem = (WorkItem) iterator.next();
				ProcessInstance pInstance = ProcessInstance.get(session, workItem.getProcessInstanceID());
				String taskName = "";
				Map<String, Object> dsMap = pInstance.getDataSlotValue();
				if (this.isUserValidForCase(dsMap, userId)) {
					MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
					if (workItem.getName().contains("::")) {
						taskName = workItem.getName().split("::")[1];
						if (taskName.equals("Team Assignment Task") && dsMap.get("TeamTypeList").toString() != null) {
							String teamType = dsMap.get("TeamTypeList").toString();
							String[] team = teamType.split("#");
							taskName = taskName + team[0] + "]";
						}
					}

					if (taskName.trim().contains("CaseCreation")) {
						myTaskPageVO.setTaskName("Client Submission Task");
					} else {
						myTaskPageVO.setTaskName(taskName);
					}

					myTaskPageVO.setPerformer(workItem.getPerformer());
					myTaskPageVO.setCfinal((String) dsMap.get("CFinal"));
					myTaskPageVO.setCinterim1((String) dsMap.get("CInterim1"));
					myTaskPageVO.setCinterim2((String) dsMap.get("CInterim2"));
					myTaskPageVO.setCountry((String) dsMap.get("BranchOffice"));
					myTaskPageVO.setCrn((String) dsMap.get("CRN"));
					myTaskPageVO.setCurrentCycle((String) dsMap.get("ProcessCycle"));
					Boolean eCase = (Boolean) dsMap.get("ExpressCase");
					myTaskPageVO.setExpressCase(eCase ? "Y" : "N");
					myTaskPageVO.setPrimarySubject((String) dsMap.get("PrimarySubject"));
					presentionObj.add(myTaskPageVO);
				}
			}

			return presentionObj;
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private boolean isUserValidForCase(Map dsMap, String userId) {
		HashMap customDSMAp = (HashMap) dsMap.get("customDSMap");
		boolean validFlag = false;
		if (customDSMAp != null && !customDSMAp.isEmpty()) {
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMAp.get("CycleTeamMapping");
			if (cycleTeamMapping != null) {
				HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
				CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get("Final");
				if (cycleInfo != null) {
					HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
					if (teamInfoMap != null) {
						Set teamKeySet = teamInfoMap.keySet();
						Iterator iterator2 = teamKeySet.iterator();

						while (true) {
							String teamName;
							do {
								if (!iterator2.hasNext()) {
									return validFlag;
								}

								teamName = (String) iterator2.next();
							} while (!teamName.contains("Primary") && !teamName.contains("Internal"));

							TeamAnalystMapping analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamName);
							if (analystMapping.getAnalystTaskStatus().containsKey(userId)) {
								validFlag = true;
							}
						}
					}
				}
			}
		}

		return validFlag;
	}

	protected List getWorkItemsList(String condition, String processName, Session session, boolean hasOnlyFinanceRole)
			throws Exception {
		QueryService qService = SBMQueryManager.getQueryService(session);
		QSWorkItem bswi = qService.getWorkItem();
		QSWorkItemFilter qsWorkItemFilter = new QSWorkItemFilter((String) null, processName);
		qsWorkItemFilter.setFetchSize(0);
		if (condition.contains("TaskStatus") && "CaseCreation".equalsIgnoreCase(processName)) {
			condition = this.getConditionForCaseCreationPT(condition);
		}

		qsWorkItemFilter.setCondition(condition);
		if (hasOnlyFinanceRole) {
			qsWorkItemFilter.setOrderBy("BLIDS.CaseStatus");
		} else {
			qsWorkItemFilter.setOrderBy("BLIDS.CRN DESC");
		}

		WorkItemList workItemList = bswi.getList(qsWorkItemFilter);
		return workItemList.getList();
	}

	protected List getWorkItemsList(String condition, String processName, Session session) throws Exception {
		QueryService qService = SBMQueryManager.getQueryService(session);
		QSWorkItem bswi = qService.getWorkItem();
		QSWorkItemFilter qsWorkItemFilter = new QSWorkItemFilter((String) null, processName);
		qsWorkItemFilter.setFetchSize(0);
		if (condition != null && !condition.isEmpty()) {
			qsWorkItemFilter.setCondition(condition);
			qsWorkItemFilter.setOrderBy("BLIDS.CRN");
		}

		WorkItemList workItemList = bswi.getList(qsWorkItemFilter);
		return workItemList.getList();
	}

	protected List getWorkStepInstanceList(String templateName, String workstepName, String condition, QueryService qs)
			throws Exception {
		Object waitForList = new ArrayList();

		try {
			QSWorkStepInstance qwsi = qs.getWorkStepInstance();
			WorkStepInstanceFilter filt = new WorkStepInstanceFilter((String) null, templateName);
			if (condition != null && !condition.isEmpty()) {
				filt.setCondition(condition);
			}

			QSWorkStepInstanceRS qwsirs = qwsi.getList(filt);
			waitForList = qwsirs.getList();
		} catch (Exception var9) {
			this.logger.error(var9);
		}

		return (List) waitForList;
	}

	private List<MyTaskPageVO> getPresentionObject(List workItemList, Session session)
			throws RemoteException, UnsupportedEncodingException, CMSException {
		this.logger.debug("In the getPresentationObject method....");
		ArrayList presentionObj = new ArrayList();

		try {
			Map<String, Object> tempDSMap = new HashMap();
			boolean availableFlag = false;
			this.logger.debug("Iterating over a list containing total task(s)" + workItemList.size());
			Iterator iterator = workItemList.iterator();

			while (iterator.hasNext()) {
				WorkItem workItem = (WorkItem) iterator.next();
				MyTaskPageVO myTaskPageVO = null;
				long loopStartTime = System.currentTimeMillis();
				ProcessInstance pInstance = ProcessInstance.get(session, workItem.getProcessInstanceID());
				Map<String, Object> dsMap = pInstance.getDataSlotValue();
				if (dsMap.get("CaseStatus") != null && !"Cancelled".equalsIgnoreCase((String) dsMap.get("CaseStatus"))
						&& !workItem.getName().contains("AtlasQueueProcess")) {
					if (workItem.getName().contains("Review")) {
						String teamCycleName = (String) dsMap.get("TeamCycleName");
						String team_type_List = (String) dsMap.get("TeamTypeList");
						myTaskPageVO = this.getTaskInfoObject(workItem, dsMap, (String) dsMap.get("TaskStatus"),
								team_type_List);
						myTaskPageVO.setCurrentCycle(teamCycleName);
					} else if (!workItem.getName().contains("ResearchProcess")
							&& !workItem.getName().contains("ResearchTask")
							&& !workItem.getName().contains("BIVendorResearch")) {
						myTaskPageVO = this.getTaskInfoObject(workItem, dsMap, (String) null, (String) null);
					} else {
						ProcessInstance pInstance1 = ProcessInstance.get(session, (Long) dsMap.get("parentPID"));
						Map<String, Object> dsMap1 = pInstance1.getDataSlotValue();
						String crn = (String) dsMap.get("CRN");
						String teamCycleName = null;
						String team_type_List = null;
						if (!workItem.getName().contains("BI") && !workItem.getName().contains("Vendor")) {
							if (workItem.getName().contains("ResearchTask")) {
								teamCycleName = (String) dsMap.get("TeamCycleName");
								team_type_List = (String) dsMap.get("TeamTypeList");
							} else if (workItem.getName().contains("ResearchProcess")) {
								teamCycleName = (String) dsMap.get("TeamCycleName");
								team_type_List = (String) dsMap.get("TeamTypeList");
							}
						} else {
							teamCycleName = (String) dsMap.get("BITeamCycleName");
							team_type_List = (String) dsMap.get("BIVendorTaskTypeList");
						}

						if ("Interim1".equals(teamCycleName)) {
							availableFlag = true;
						} else {
							boolean avail1;
							if ("Interim2".equals(teamCycleName)) {
								avail1 = this.getObjectResearchTask(dsMap1, "Interim1", team_type_List, session);
								if (avail1) {
									availableFlag = false;
								} else {
									availableFlag = true;
								}
							} else if ("Final".equals(teamCycleName)) {
								avail1 = this.getObjectResearchTask(dsMap1, "Interim1", team_type_List, session);
								boolean avail2 = this.getObjectResearchTask(dsMap1, "Interim2", team_type_List,
										session);
								if (!avail1 && !avail2) {
									availableFlag = true;
								} else {
									availableFlag = false;
								}
							}
						}

						if (availableFlag) {
							myTaskPageVO = this.getTaskInfoObject(workItem, dsMap, (String) dsMap.get("TaskStatus"),
									team_type_List);
							myTaskPageVO.setCurrentCycle(teamCycleName);
						}
					}

					if (myTaskPageVO != null) {
						myTaskPageVO.setCustmDSMap(tempDSMap);
						myTaskPageVO.setEncodedProcessCycle(HexEncoder.encode(myTaskPageVO.getCurrentCycle()));
						presentionObj.add(myTaskPageVO);
						this.logger.debug("The loop for task[" + myTaskPageVO.getTaskName() + "] and CRN ["
								+ myTaskPageVO.getCrn() + "] ended here...");
					}
				}

				long loopEndTime = System.currentTimeMillis();
				this.logger.debug("This loop took [" + (loopEndTime - loopStartTime) + "] milliseconds..");
			}

			return presentionObj;
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	private boolean getObjectResearchTask(Map<String, Object> dsMap, String processCycle, String team_type_List,
			Session session) throws CMSException {
		boolean isAvailable = false;

		try {
			HashMap customDSMAp = (HashMap) dsMap.get("customDSMap");
			if (customDSMAp != null && !customDSMAp.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMAp.get("CycleTeamMapping");
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					if (cycleInfoMap != null) {
						isAvailable = this.getCycleInfo(cycleInfoMap, processCycle, team_type_List,
								(String) dsMap.get("Analyst"), session);
					}
				}
			}

			return isAvailable;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private boolean getCycleInfo(HashMap cycleInfoMap, String cycleName, String team_type_List, String analyst,
			Session session) {
		boolean isAvailable = false;
		CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
		if (cycleInfo != null) {
			HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
			if (teamInfoMap != null) {
				TeamAnalystMapping analystMapping = (TeamAnalystMapping) teamInfoMap.get(team_type_List);
				if (analystMapping != null) {
					AnalystTaskStatus analystTaskStatus = (AnalystTaskStatus) analystMapping.getAnalystTaskStatus()
							.get(session.getUser());
					if (analystTaskStatus != null) {
						if (analystTaskStatus.getStatus().equals("Done")) {
							isAvailable = false;
						} else {
							isAvailable = true;
						}
					}
				}
			}
		}

		return isAvailable;
	}

	private MyTaskPageVO getCustomTaskObject(CustomTaskVO taskObject, Map<String, Object> dsMap, String appName,
			boolean isTATask) throws UnsupportedEncodingException, CMSException {
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		String taskName = taskObject.getWorkStepName();
		String getcorrectTeamName = null;
		if (dsMap.get("TeamTypeList") instanceof Vector) {
			Vector teamTypeVal = (Vector) dsMap.get("TeamTypeList");
			this.logger.debug("Value of Vector==>");
			Enumeration vEnum = teamTypeVal.elements();

			while (vEnum.hasMoreElements()) {
				getcorrectTeamName = (String) vEnum.nextElement();
				this.logger.debug("Vector teamTypeVal==>" + getcorrectTeamName);
			}
		} else {
			String teamTypeVal = (String) dsMap.get("TeamTypeList");
			getcorrectTeamName = teamTypeVal;
			this.logger.debug("teamTypeVal==>" + teamTypeVal);
		}

		myTaskPageVO.setPid(taskObject.getWorkItemID());
		myTaskPageVO.setProcessInstanceId(taskObject.getProcessInstanceID());
		myTaskPageVO.setWorlItemName(taskObject.getWorkItemName());
		myTaskPageVO.setAppName(appName);
		myTaskPageVO.setEncodedTask(HexEncoder.encode(taskObject.getWorkItemName()));
		myTaskPageVO.setPerformer(taskObject.getPerformer());
		myTaskPageVO.setCountry(taskObject.getPrimarySubjectCountry());
		myTaskPageVO.setCrn(taskObject.getCrn());
		Boolean eCase = (Boolean) dsMap.get("ExpressCase");
		myTaskPageVO.setExpressCase(eCase ? "Y" : "N");
		myTaskPageVO.setPrimarySubject(taskObject.getPrimarySubject());
		myTaskPageVO.setStatus(taskObject.getCaseStatus());
		myTaskPageVO.setCaseStatus(taskObject.getCaseStatus());
		myTaskPageVO.setClientfinalDueDate((String) dsMap.get("CFinal"));
		myTaskPageVO.setTaskName(taskName);
		if (taskObject.getAnalysts() != null && !taskObject.getAnalysts().equals("null")) {
			myTaskPageVO.setAnalysts(taskObject.getAnalysts());
		} else {
			myTaskPageVO.setAnalysts("");
		}

		if (taskName.equals("Invoicing Task")) {
			myTaskPageVO.setCfinal((String) dsMap.get("CFinal"));
			myTaskPageVO.setCinterim1((String) dsMap.get("CInterim1"));
			myTaskPageVO.setCinterim2((String) dsMap.get("CInterim2"));
			myTaskPageVO.setCurrentCycle("");
			myTaskPageVO.setStatus(taskObject.getInvoiceTaskStatus());
			myTaskPageVO.setAnalysts(taskObject.getAnalysts());
		} else {
			this.logger.debug("Checking RDD>>>> App Name" + appName + ">>" + "Task Name" + taskName);
			if (appName != null && appName.contains("CaseCreation")) {
				myTaskPageVO.setCfinal((String) dsMap.get("RFinal"));
				myTaskPageVO.setCinterim1((String) dsMap.get("RInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("RInterim2"));
			} else if (getcorrectTeamName != null && getcorrectTeamName.contains("Primary")
					&& taskName.equals("Research Task")) {
				this.logger.debug("Checking RDD>>>>>>>>" + dsMap.get("RFinal") + ">>" + dsMap.get("RInterim1") + ">>"
						+ dsMap.get("RInterim2"));
				myTaskPageVO.setCfinal((String) dsMap.get("RFinal"));
				myTaskPageVO.setCinterim1((String) dsMap.get("RInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("RInterim2"));
			} else {
				myTaskPageVO.setCfinal((String) dsMap.get("tFinal"));
				myTaskPageVO.setCinterim1((String) dsMap.get("tInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("tInterim2"));
			}

			if (!taskName.equals("Office Assignment Task") && !taskName.equals("Complete Case Creation")
					&& !taskName.equals("Team Assignment Task")) {
				myTaskPageVO.setCurrentCycle(taskObject.getCycle());
			} else {
				myTaskPageVO.setCurrentCycle("");
			}

			if (taskName.equalsIgnoreCase("Office Assignment Task")) {
				myTaskPageVO.setStatus(taskObject.getOfficeTaskStatus());
			} else if (taskName.equalsIgnoreCase("Complete Case Creation")) {
				myTaskPageVO.setStatus(taskObject.getCompleteTaskStatus());
			} else if (taskName.equalsIgnoreCase("Consolidation Task")) {
				if (getcorrectTeamName != null && getcorrectTeamName.contains("Supporting - Internal")) {
					this.logger.debug("SP::::" + taskObject.getTaskStatus());
					myTaskPageVO.setStatus(taskObject.getTaskStatus() != null
							? taskObject.getTaskStatus()
							: taskObject.getConsolidationTaskStatus());
				} else {
					myTaskPageVO.setStatus(taskObject.getConsolidationTaskStatus());
				}

				this.logger.debug("PT:::::::::::" + taskObject.getConsolidationTaskStatus());
			} else if (taskName.equalsIgnoreCase("Client Submission Task")) {
				myTaskPageVO.setStatus(taskObject.getClientSubmissionTaskStatus());
				myTaskPageVO.setCfinal((String) dsMap.get("CFinal"));
				myTaskPageVO.setCinterim1((String) dsMap.get("CInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("CInterim2"));
			} else {
				myTaskPageVO.setStatus(taskObject.getTaskStatus());
			}
		}

		if (myTaskPageVO.getCrn() != null) {
			myTaskPageVO.setEncodedCRN(HexEncoder.encode(myTaskPageVO.getCrn()));
		}

		if (myTaskPageVO.getStatus() != null) {
			myTaskPageVO.setEncodedTaskStatus(HexEncoder.encode(myTaskPageVO.getStatus()));
		}

		Vector teamTypeVector;
		String dueDateforCC;
		if (isTATask) {
			dueDateforCC = null;
			if (dueDateforCC == null) {
				if (appName.contains("TeamAssignment")) {
					dueDateforCC = (String) dsMap.get("TeamTypeList");
					this.logger.debug("teamTypeList :: " + dueDateforCC + " : from ds :: " + dsMap.get("TeamTypeList"));
				} else {
					teamTypeVector = (Vector) dsMap.get("TeamTypeList");
					if (teamTypeVector != null && teamTypeVector instanceof Vector) {
						Iterator teamItr = teamTypeVector.iterator();

						while (teamItr.hasNext()) {
							dueDateforCC = (String) teamItr.next();
							if (dueDateforCC.contains("Primary")) {
								break;
							}
						}
					}
				}
			}

			myTaskPageVO.setTeamTypeList(dueDateforCC);
		}

		if (myTaskPageVO.getCfinal() != null && !myTaskPageVO.getCfinal().isEmpty()) {
			myTaskPageVO.setDueDateForCurrentCycle(myTaskPageVO.getCfinal());
		} else {
			myTaskPageVO.setDueDateForCurrentCycle((String) dsMap.get("RFinal"));
		}

		this.logger.debug("crn = " + myTaskPageVO.getCrn() + ", taskName = " + myTaskPageVO.getTaskName()
				+ ", teamName = " + myTaskPageVO.getTeamName() + ", cycle = " + taskObject.getCycle()
				+ ", currentCycle = " + myTaskPageVO.getCurrentCycle() + ", cinterim1 = " + dsMap.get("CInterim1")
				+ ", cinterim2 = " + dsMap.get("CInterim2") + ", cfinal = " + myTaskPageVO.getClientfinalDueDate());
		if (myTaskPageVO.getCurrentCycle() != null && myTaskPageVO.getCurrentCycle().equalsIgnoreCase("Interim1")) {
			myTaskPageVO.setDueDateForCurrentCycle((String) dsMap.get("CInterim1"));
		} else if (myTaskPageVO.getCurrentCycle() != null
				&& myTaskPageVO.getCurrentCycle().equalsIgnoreCase("Interim2")) {
			myTaskPageVO.setDueDateForCurrentCycle((String) dsMap.get("CInterim2"));
		}

		this.logger.debug("DDFORCC :: " + myTaskPageVO.getDueDateForCurrentCycle());
		dueDateforCC = "";
		teamTypeVector = null;

		try {
			String[] dateTokens = myTaskPageVO.getDueDateForCurrentCycle().split("-");
			this.logger.debug("date month - " + dateTokens[1] + " and month length - " + dateTokens[1].length());
			SimpleDateFormat sourceFormat;
			if (dateTokens[1].length() == 3) {
				sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
				this.logger.debug("sourcedate format is dd-MMM-yyyy");
			} else {
				sourceFormat = new SimpleDateFormat("dd-MM-yy");
				this.logger.debug("sourcedate format is dd-MM-yy");
			}

			SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
			dueDateforCC = targetFormat.format(sourceFormat.parse(myTaskPageVO.getDueDateForCurrentCycle()));
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}

		myTaskPageVO.setDueDateForCurrentCycle(dueDateforCC);
		DateTime caseCreationDate = (DateTime) dsMap.get("CaseCreationDate");
		this.logger.debug("CCD_StringValue = " + caseCreationDate.getStringValue() + ",    CCD_TimeStamp = "
				+ caseCreationDate.getValue());
		myTaskPageVO.setCaseCreationDate("" + caseCreationDate.getValue());
		return myTaskPageVO;
	}

	private MyTaskPageVO getAwaitingCustomTaskObject(CustomTaskVO taskObject, Map<String, Object> dsMap, String appName)
			throws UnsupportedEncodingException, CMSException {
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setAppName(appName);
		myTaskPageVO.setEncodedTask("");
		myTaskPageVO.setPerformer(taskObject.getPerformer());
		myTaskPageVO.setCfinal((String) dsMap.get("RFinal"));
		myTaskPageVO.setCinterim1((String) dsMap.get("RInterim1"));
		myTaskPageVO.setCinterim2((String) dsMap.get("RInterim2"));
		myTaskPageVO.setCountry(taskObject.getPrimarySubjectCountry());
		myTaskPageVO.setCrn(taskObject.getCrn());
		myTaskPageVO.setCurrentCycle(taskObject.getCycle());
		Boolean eCase = (Boolean) dsMap.get("ExpressCase");
		myTaskPageVO.setExpressCase(eCase ? "Y" : "N");
		myTaskPageVO.setPrimarySubject(taskObject.getPrimarySubject());
		myTaskPageVO.setStatus(taskObject.getAwaitingTaskStatus());
		myTaskPageVO.setCaseStatus(taskObject.getCaseStatus());
		myTaskPageVO.setTaskName("Awaiting Consolidation");
		myTaskPageVO.setDueDateForCurrentCycle(myTaskPageVO.getCfinal());
		if (myTaskPageVO.getCurrentCycle() != null && myTaskPageVO.getCurrentCycle().equalsIgnoreCase("Interim1")) {
			myTaskPageVO.setDueDateForCurrentCycle(myTaskPageVO.getCinterim1());
		} else if (myTaskPageVO.getCurrentCycle() != null
				&& myTaskPageVO.getCurrentCycle().equalsIgnoreCase("Interim2")) {
			myTaskPageVO.setDueDateForCurrentCycle(myTaskPageVO.getCinterim2());
		}

		String dueDateforCC = "";
		SimpleDateFormat sourceFormat = null;

		try {
			String[] dateTokens = myTaskPageVO.getDueDateForCurrentCycle().split("-");
			this.logger.debug("date month - " + dateTokens[1] + " and month length - " + dateTokens[1].length());
			if (dateTokens[1].length() == 3) {
				sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
				this.logger.debug("sourcedate format is dd-MMM-yyyy");
			} else {
				sourceFormat = new SimpleDateFormat("dd-MM-yy");
				this.logger.debug("sourcedate format is dd-MM-yy");
			}

			SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
			dueDateforCC = targetFormat.format(sourceFormat.parse(myTaskPageVO.getDueDateForCurrentCycle()));
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		myTaskPageVO.setDueDateForCurrentCycle(dueDateforCC);
		DateTime caseCreationDate = (DateTime) dsMap.get("CaseCreationDate");
		this.logger.debug("CCD_StringValue = " + caseCreationDate.getStringValue() + ",    CCD_TimeStamp = "
				+ caseCreationDate.getValue());
		myTaskPageVO.setCaseCreationDate("" + caseCreationDate.getValue());
		return myTaskPageVO;
	}

	private MyTaskPageVO getTaskInfoObject(WorkItem workItem, Map dsMap, String taskStatus, String teamTypeList)
			throws UnsupportedEncodingException {
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		String taskName = workItem.getName().split("::")[1];
		myTaskPageVO.setPid(workItem.getID());
		myTaskPageVO.setProcessInstanceId(workItem.getProcessInstanceID());
		myTaskPageVO.setWorlItemName(workItem.getName());
		myTaskPageVO.setAppName(workItem.getAppName());
		myTaskPageVO.setEncodedTask(HexEncoder.encode(workItem.getName()));
		myTaskPageVO.setPerformer(workItem.getPerformer());
		myTaskPageVO.setIsOverdue(this.getOverDueStatus(new DateTime(workItem.getDueDate())));
		myTaskPageVO.setCasePriority((String) dsMap.get("CasePriority"));
		myTaskPageVO.setCountry((String) dsMap.get("PrimarySubjectCountry"));
		myTaskPageVO.setCrn((String) dsMap.get("CRN"));
		Boolean eCase = (Boolean) dsMap.get("ExpressCase");
		myTaskPageVO.setExpressCase(eCase ? "Y" : "N");
		myTaskPageVO.setPrimarySubject((String) dsMap.get("PrimarySubject"));
		myTaskPageVO.setStatus((String) dsMap.get("CaseStatus"));
		myTaskPageVO.setTaskName(taskName);
		if (taskName.equals("Invoicing Task")) {
			myTaskPageVO.setCfinal((String) dsMap.get("CFinal"));
			myTaskPageVO.setCinterim1((String) dsMap.get("CInterim1"));
			myTaskPageVO.setCinterim2((String) dsMap.get("CInterim2"));
			myTaskPageVO.setCurrentCycle("");
			myTaskPageVO.setStatus((String) dsMap.get("InvoiceTaskStatus"));
			teamTypeList = null;
		} else {
			long parentPid = (Long) dsMap.get("parentPID");
			if (parentPid != 0L && parentPid != (Long) dsMap.get("PID")) {
				myTaskPageVO.setCfinal((String) dsMap.get("tFinal"));
				myTaskPageVO.setCinterim1((String) dsMap.get("tInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("tInterim2"));
			} else {
				myTaskPageVO.setCfinal((String) dsMap.get("RFinal"));
				myTaskPageVO.setCinterim1((String) dsMap.get("RInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("RInterim2"));
			}

			if (!taskName.equals("Office Assignment Task") && !taskName.equals("Complete Case Creation")
					&& !taskName.equals("Team Assignment Task")) {
				myTaskPageVO.setCurrentCycle((String) dsMap.get("ProcessCycle"));
			} else {
				myTaskPageVO.setCurrentCycle("");
			}

			if (taskStatus == null) {
				if (workItem.getName().contains("Office Assignment Task")) {
					myTaskPageVO.setStatus((String) dsMap.get("OfficeTaskSTatus"));
				} else if (workItem.getName().contains("Complete Case Creation")) {
					myTaskPageVO.setStatus((String) dsMap.get("CompleteTaskStatus"));
				} else if (workItem.getName().contains("Consolidation Task")) {
					myTaskPageVO.setStatus((String) dsMap.get("ConsolidationTaskStatus"));
				} else if (workItem.getName().contains("Client Submission Task")) {
					myTaskPageVO.setStatus((String) dsMap.get("ClientSubmissionTaskStatus"));
				} else {
					myTaskPageVO.setStatus((String) dsMap.get("TaskStatus"));
				}
			} else {
				myTaskPageVO.setStatus(taskStatus);
			}

			if (teamTypeList == null) {
				if (workItem.getName().contains("TeamAssignment")) {
					teamTypeList = (String) dsMap.get("TeamTypeList");
				} else {
					Vector teamTypeVector = (Vector) dsMap.get("TeamTypeList");
					if (teamTypeVector != null) {
						Iterator teamItr = teamTypeVector.iterator();

						while (teamItr.hasNext()) {
							teamTypeList = (String) teamItr.next();
							if (teamTypeList.contains("Primary")) {
								break;
							}
						}
					}
				}
			}

			if (taskName.equals("Consolidation Task") || taskName.equals("Office Assignment Task")
					|| taskName.equals("Complete Case Creation") || taskName.equals("Client Submission Task")) {
				teamTypeList = null;
			}
		}

		myTaskPageVO.setTeamTypeList(teamTypeList);
		if (myTaskPageVO.getCrn() != null) {
			myTaskPageVO.setEncodedCRN(HexEncoder.encode(myTaskPageVO.getCrn()));
		}

		if (myTaskPageVO.getStatus() != null) {
			myTaskPageVO.setEncodedTaskStatus(HexEncoder.encode(myTaskPageVO.getStatus()));
		}

		return myTaskPageVO;
	}

	private MyTaskPageVO getWaitTaskInfoObject(String taskName, String appName, String performer, Map dsMap)
			throws UnsupportedEncodingException {
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setAppName(appName);
		myTaskPageVO.setEncodedTask("");
		myTaskPageVO.setPerformer(performer);
		myTaskPageVO.setCasePriority((String) dsMap.get("CasePriority"));
		myTaskPageVO.setCfinal((String) dsMap.get("RFinal"));
		myTaskPageVO.setCinterim1((String) dsMap.get("RInterim1"));
		myTaskPageVO.setCinterim2((String) dsMap.get("RInterim2"));
		myTaskPageVO.setCountry((String) dsMap.get("PrimarySubjectCountry"));
		myTaskPageVO.setCrn((String) dsMap.get("CRN"));
		myTaskPageVO.setCurrentCycle((String) dsMap.get("ProcessCycle"));
		Boolean eCase = (Boolean) dsMap.get("ExpressCase");
		myTaskPageVO.setExpressCase(eCase ? "Y" : "N");
		myTaskPageVO.setPrimarySubject((String) dsMap.get("PrimarySubject"));
		myTaskPageVO.setStatus((String) dsMap.get("AwaitingTaskSTatus"));
		myTaskPageVO.setTaskName("Awaiting Consolidation");
		return myTaskPageVO;
	}

	private Boolean getOverDueStatus(DateTime date) {
		Calendar calendar = date.getCalendar();
		calendar.setTimeZone(new SimpleTimeZone(0, "GMT"));
		Calendar todayDate = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
		return todayDate.after(calendar);
	}

	public List<MyTaskPageVO> getMyIncompletedTasks(String userName, Session session) throws CMSException {
		List<MyTaskPageVO> listOfPresentationObj = new ArrayList();
		listOfPresentationObj.addAll(this.getWaitForConsolidationTask("1=1", session));
		return listOfPresentationObj;
	}

	private String getConditionForCaseCreationPT(String condition) {
		String condition_CC = "";
		int startIndex = condition.indexOf("TaskStatus");
		String taskStatus = "";
		if (startIndex != -1) {
			taskStatus = " " + condition.substring(startIndex + "TaskStatus".length()) + " ";
			condition_CC = condition.replaceAll("BLIDS.TaskStatus", "((BLIDS.CompleteTaskStatus");
			condition_CC = condition_CC + " AND BLWSI.WORKSTEP_NAME ='" + "Complete Case Creation" + "') OR (BLIDS."
					+ "OfficeTaskSTatus" + taskStatus + " AND BLWSI.WORKSTEP_NAME ='" + "Office Assignment Task"
					+ "') OR (BLIDS." + "InvoiceTaskStatus" + taskStatus + "AND BLWSI.WORKSTEP_NAME ='"
					+ "Invoicing Task" + "') OR (BLIDS." + "ConsolidationTaskStatus" + taskStatus
					+ " AND BLWSI.WORKSTEP_NAME ='" + "Consolidation Task" + "') OR (BLIDS."
					+ "ClientSubmissionTaskStatus" + taskStatus + " AND BLWSI.WORKSTEP_NAME ='"
					+ "Client Submission Task" + "') OR (BLIDS.AwaitingTaskSTatus " + taskStatus
					+ " AND BLWSI.WORKSTEP_NAME = 'WaitForConsolidation')" + ")";
		} else {
			condition_CC = "BLIDS.CaseStatus <> 'Cancelled'";
			this.logger.warn(
					"This code need to be review now , this condition has broken the code. Need urgent attention");
		}

		this.logger.debug("Updated condition " + condition_CC);
		return condition_CC;
	}

	private String getConditionForTeamTask(String condition) {
		String condition_CC = "";
		condition_CC = condition_CC + " BLIDS." + "OfficeTaskSTatus" + " in ('" + "New" + "','" + "In Progress"
				+ "') OR BLIDS." + "InvoiceTaskStatus" + " in ('" + "New" + "','" + "In Progress" + "')" + " OR BLIDS."
				+ "ConsolidationTaskStatus" + " in ('" + "New" + "','" + "In Progress" + "')" + " OR BLIDS."
				+ "ClientSubmissionTaskStatus" + " in ('" + "New" + "','" + "In Progress" + "')" + " OR BLIDS."
				+ "CompleteTaskStatus" + " in ('" + "New" + "','" + "In Progress" + "')";
		return condition_CC;
	}

	public List<MyTaskPageVO> getAllTaskForCrn(String crn, Session session) throws CMSException {
		String condition = "BLIDS.CRN='" + crn + "'";
		List<MyTaskPageVO> presentationList = new ArrayList();
		ArrayList workItemList = new ArrayList();

		try {
			QueryService qService = SBMQueryManager.getQueryService(session);
			this.logger.debug("condition " + condition);

			for (int i = 0; i < this.processNames.length; ++i) {
				this.list = this.getWorkStepInstanceList(this.processNames[i], (String) null, condition, qService);

				for (int j = 0; j < this.list.size(); ++j) {
					QSWorkStepInstanceData qsWorkStepInstance = (QSWorkStepInstanceData) this.list.get(j);
					workItemList.addAll(qsWorkStepInstance.getWorkItemList().getList());
				}
			}

			this.logger.debug("workItemList is" + workItemList.size());
			List waitTaskList = this.getWaitForConsolidationTaskForCrn(this.gerPerformers(workItemList), session);
			this.logger.debug("Wait for task size " + waitTaskList.size());
			if (!waitTaskList.isEmpty()) {
				presentationList.addAll(waitTaskList);
			}

			if (workItemList != null && !workItemList.isEmpty()) {
				presentationList.addAll(this.getPresentionObjectForCrn(workItemList, session));
			}

			this.logger.debug("presentationList size " + presentationList.size());
			return presentationList;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<MyTaskPageVO> getAllTaskForCrnList(List<String> crnList, Session session) throws CMSException {
		StringBuffer resultString = new StringBuffer();
		Iterator var5 = crnList.iterator();

		String condition;
		while (var5.hasNext()) {
			condition = (String) var5.next();
			if (resultString.length() == 0) {
				resultString.append("'" + condition + "'");
			} else {
				resultString.append(",'" + condition + "'");
			}
		}

		condition = "BLIDS.CRN IN (" + resultString + ")";
		List<MyTaskPageVO> presentationList = new ArrayList();
		ArrayList workItemList = new ArrayList();

		try {
			QueryService qService = SBMQueryManager.getQueryService(session);
			this.logger.debug("condition " + condition);

			for (int i = 0; i < this.processNames.length; ++i) {
				this.list = this.getWorkStepInstanceList(this.processNames[i], (String) null, condition, qService);

				for (int j = 0; j < this.list.size(); ++j) {
					QSWorkStepInstanceData qsWorkStepInstance = (QSWorkStepInstanceData) this.list.get(j);
					workItemList.addAll(qsWorkStepInstance.getWorkItemList().getList());
				}
			}

			this.logger.debug("workItemList is" + workItemList.size());
			List waitTaskList = this.getWaitForConsolidationTaskForCrn(this.gerPerformers(workItemList), session);
			this.logger.debug("Wait for task size " + waitTaskList.size());
			if (!waitTaskList.isEmpty()) {
				presentationList.addAll(waitTaskList);
			}

			if (workItemList != null && !workItemList.isEmpty()) {
				presentationList.addAll(this.getPresentionObjectForCrn(workItemList, session));
			}

			this.logger.debug("presentationList size " + presentationList.size());
			return presentationList;
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private List<MyTaskPageVO> getPresentionObjectForCrn(List workItemList, Session session)
			throws RemoteException, UnsupportedEncodingException, CMSException {
		ArrayList presentionObj = new ArrayList();

		try {
			String crnTemp = "";
			boolean availableFlag = false;
			Iterator iterator = workItemList.iterator();

			while (iterator.hasNext()) {
				WorkItem workItem = (WorkItem) iterator.next();
				MyTaskPageVO myTaskPageVO = null;
				String performer = workItem.getPerformer();
				ProcessInstance pInstance = ProcessInstance.get(session, workItem.getProcessInstanceID());
				Map<String, Object> dsMap = pInstance.getDataSlotValue();
				if (workItem.getName().contains("Review")) {
					String teamCycleName = (String) dsMap.get("TeamCycleName");
					String team_type_List = (String) dsMap.get("TeamTypeList");
					myTaskPageVO = this.getTaskInfoObject(workItem, dsMap, teamCycleName, team_type_List);
					myTaskPageVO.setCurrentCycle(teamCycleName);
				} else if (!workItem.getName().contains("ResearchProcess")
						&& !workItem.getName().contains("ResearchTask")
						&& !workItem.getName().contains("BIVendorResearch")) {
					myTaskPageVO = this.getTaskInfoObject(workItem, dsMap, (String) null, (String) null);
				} else {
					ProcessInstance pInstance1 = ProcessInstance.get(session, (Long) dsMap.get("parentPID"));
					Map<String, Object> dsMap1 = pInstance1.getDataSlotValue();
					String crn = (String) dsMap.get("CRN");
					String teamCycleName = null;
					String team_type_List = null;
					if (!workItem.getName().contains("BI") && !workItem.getName().contains("Vendor")) {
						if (workItem.getName().contains("ResearchTask")) {
							teamCycleName = (String) dsMap.get("TeamCycleName");
							team_type_List = (String) dsMap.get("TeamTypeList");
						} else if (workItem.getName().contains("ResearchProcess")) {
							teamCycleName = (String) dsMap.get("TeamCycleName");
							team_type_List = (String) dsMap.get("TeamTypeList");
						}
					} else {
						teamCycleName = (String) dsMap.get("BITeamCycleName");
						team_type_List = (String) dsMap.get("BIVendorTaskTypeList");
					}

					if ("Interim1".equals(teamCycleName)) {
						availableFlag = true;
					} else {
						boolean avail1;
						if ("Interim2".equals(teamCycleName)) {
							avail1 = this.getObjectResearchTaskForCrn(dsMap1, "Interim1", team_type_List, performer);
							if (avail1) {
								availableFlag = false;
							} else {
								availableFlag = true;
							}
						} else if ("Final".equals(teamCycleName)) {
							avail1 = this.getObjectResearchTaskForCrn(dsMap1, "Interim1", team_type_List, performer);
							boolean avail2 = this.getObjectResearchTaskForCrn(dsMap1, "Interim2", team_type_List,
									performer);
							if (!avail1 && !avail2) {
								availableFlag = true;
							} else {
								availableFlag = false;
							}
						}
					}

					if (availableFlag) {
						myTaskPageVO = this.getTaskInfoObject(workItem, dsMap, (String) dsMap1.get("TaskStatus"),
								team_type_List);
						myTaskPageVO.setCurrentCycle(teamCycleName);
					}
				}

				if (myTaskPageVO != null) {
					myTaskPageVO.setCustmDSMap(dsMap);
					presentionObj.add(myTaskPageVO);
				}
			}

			return presentionObj;
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	private boolean getObjectResearchTaskForCrn(Map<String, Object> dsMap, String processCycle, String team_type_List,
			String performer) throws CMSException {
		boolean isAvailable = false;

		try {
			HashMap customDSMAp = (HashMap) dsMap.get("customDSMap");
			if (customDSMAp != null && !customDSMAp.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMAp.get("CycleTeamMapping");
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					if (cycleInfoMap != null) {
						isAvailable = this.getCycleInfoForCrn(cycleInfoMap, processCycle, team_type_List,
								(String) dsMap.get("Analyst"), performer);
					}
				}
			}

			return isAvailable;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private boolean getCycleInfoForCrn(HashMap cycleInfoMap, String cycleName, String team_type_List, String analyst,
			String performer) {
		boolean isAvailable = false;
		CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
		if (cycleInfo != null) {
			HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
			if (teamInfoMap != null) {
				TeamAnalystMapping analystMapping = (TeamAnalystMapping) teamInfoMap.get(team_type_List);
				if (analystMapping != null) {
					AnalystTaskStatus analystTaskStatus = (AnalystTaskStatus) analystMapping.getAnalystTaskStatus()
							.get(performer);
					if (analystTaskStatus != null) {
						if (analystTaskStatus.getStatus().equals("Done")) {
							isAvailable = false;
						} else {
							isAvailable = true;
						}
					}
				}
			}
		}

		return isAvailable;
	}

	private List<MyTaskPageVO> getWaitForConsolidationTaskForCrn(String performers, Session session)
			throws CMSException {
		ArrayList waitForConsolidationTaskList = new ArrayList();

		try {
			List waitWorkstepList = this
					.getWorkStepInstanceList("CaseCreation", (String) null,
							"BLWSI.STATUS = 62 AND BLIDS.MainAnalyst in (" + performers
									+ ") AND BLWSI.WORKSTEP_NAME = 'WaitForConsolidation'",
							SBMQueryManager.getQueryService());
			if (waitWorkstepList != null && !waitWorkstepList.isEmpty()) {
				Iterator iterator = waitWorkstepList.iterator();

				while (iterator.hasNext()) {
					QSWorkStepInstanceData qsWorkStepInstanceData = (QSWorkStepInstanceData) iterator.next();
					ProcessInstance pInstance = ProcessInstance.get(session,
							qsWorkStepInstanceData.getProcessInstanceID());
					Map<String, Object> dsMap = pInstance.getDataSlotValue();
					if (this.checkForWaitForCon(dsMap)) {
						waitForConsolidationTaskList.add(this.getWaitTaskInfoObject(qsWorkStepInstanceData.getName(),
								pInstance.getAppName(), performers, dsMap));
					}
				}
			}
		} catch (Exception var9) {
			new CMSException();
		}

		return waitForConsolidationTaskList;
	}

	private String gerPerformers(List workItemList) {
		String performers = "'1'";

		WorkItem workItem;
		for (Iterator iterator = workItemList.iterator(); iterator
				.hasNext(); performers = "'" + workItem.getPerformer() + "'," + performers) {
			workItem = (WorkItem) iterator.next();
			MyTaskPageVO myTaskPageVO = null;
		}

		return performers;
	}

	public boolean isOACompleted(String crn, String userID) throws CMSException {
		boolean isOACompleted = false;

		try {
			this.logger.debug("CRN is " + crn + " and UserID is " + userID);
			CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService()
					.getCaseInfo(crn.replace("\\\\", "\\"));
			this.logger.debug("PID is " + caseDetails.getPid());
			this.logger.debug("In getCaseDetails caseDetails.getClientRef()" + caseDetails.getClientRef());
			Long pid = null;
			if (caseDetails.getPid() != null && !caseDetails.getPid().isEmpty()) {
				pid = Long.parseLong(caseDetails.getPid());
			}

			Session session = ResourceLocator.self().getSBMService().getSession(userID);
			if (!ResourceLocator.self().getSBMService().isTaskCompleted(pid, session)) {
				boolean isAutoOA = (Boolean) ResourceLocator.self().getSBMService().getDataslotValue(pid, "isAutoOA",
						session);
				if (!isAutoOA) {
					String[] completedWSNames = ResourceLocator.self().getSBMService().getCompletedWSNames(pid,
							session);
					if (completedWSNames != null && completedWSNames.length > 0) {
						for (int k = 0; k < completedWSNames.length; ++k) {
							if (completedWSNames[k].equals("Office Assignment Task")) {
								isOACompleted = true;
								break;
							}
						}
					}
				} else {
					isOACompleted = true;
				}
			} else {
				isOACompleted = true;
			}
		} catch (Exception var10) {
			this.logger.debug(var10.getLocalizedMessage());
		}

		return isOACompleted;
	}
}