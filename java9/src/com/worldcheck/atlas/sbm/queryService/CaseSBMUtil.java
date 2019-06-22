package com.worldcheck.atlas.sbm.queryService;

import com.savvion.sbm.bizlogic.client.queryservice.QSWorkStepInstance;
import com.savvion.sbm.bizlogic.client.queryservice.QSWorkStepInstanceRS;
import com.savvion.sbm.bizlogic.client.queryservice.QueryService;
import com.savvion.sbm.bizlogic.client.queryservice.WorkStepInstanceFilter;
import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.server.svo.QSWorkStepInstanceData;
import com.savvion.sbm.bizlogic.server.svo.WorkItem;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.AnalystTaskStatus;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.task.CaseStatusVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class CaseSBMUtil {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.queryService.CaseSBMUtil");
	private QueryService qService = null;
	private String[] processNames = new String[]{"CaseCreation", "BIVendorResearch", "ResearchProcess", "ResearchTask",
			"Review", "TeamAssignment"};
	private Session session;
	private List list = null;
	private Map<String, String> taskAdded = new HashMap();
	private boolean isPrimaryTeamTaskAvailable = false;
	private Map<String, String> activeWorkStepMap = new HashMap();
	private Map<String, String> activeCycleMap = new HashMap();
	private static String FUTURE_CLIENT_SUB = "FutureClientSubmission";
	private static String COMPLETED_STATUS = "Completed";
	private static String ACTIVE_STATUS = "Active";
	private static String NOT_STARTED_STATUS = "NotStarted";
	private static String ONHOLD_STATUS = "NotActive";

	public void setSession(Session session) throws CMSException {
		this.session = session;
		this.qService = SBMQueryManager.getQueryService(session);
	}

	public Map<String, Object> getCaseStatus(String crn, Session session) throws Exception {
		List workstepList = new ArrayList();
		Map<String, Object> allTaskMap = new HashMap();
		List<MyTaskPageVO> intrim1List = new ArrayList();
		List<MyTaskPageVO> intrim2List = new ArrayList();
		List<MyTaskPageVO> finalList = new ArrayList();
		String condition = " BLIDS.CRN = '" + crn + "'";
		this.logger.debug("##################################################################");
		List waitForConsList = this.getWaitForConsolidationTask(condition);
		this.logger.debug("waitForConsList size " + waitForConsList.size());
		Iterator iterator = waitForConsList.iterator();

		while (iterator.hasNext()) {
			MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
			List taskList = this.getIntrimCycleInfoForWaitCondition(myTaskPageVO);
			if (myTaskPageVO.getCurrentCycle().equals("Interim1")) {
				intrim1List.addAll(taskList);
			}

			if (myTaskPageVO.getCurrentCycle().equals("Interim2")) {
				intrim2List.addAll(taskList);
			}

			if (myTaskPageVO.getCurrentCycle().equals("Final")) {
				finalList.addAll(taskList);
			}
		}

		this.logger.debug("condition " + condition);

		for (int i = 0; i < this.processNames.length; ++i) {
			workstepList.addAll(this.getWorkStepInstanceListForStatus(this.processNames[i], (String) null, condition));
		}

		this.logger.debug("workstepList" + workstepList);
		List teamDetailList;
		if (!workstepList.isEmpty()) {
			teamDetailList = this.getTaskStaus(workstepList);
			List activeWorkItemCopy = new ArrayList();
			activeWorkItemCopy.addAll(teamDetailList);
			Iterator workItemItr = teamDetailList.iterator();
			int count = false;
			this.logger.debug(
					"-----------------------------------------------------------------------------------------------------");

			while (true) {
				while (workItemItr.hasNext()) {
					MyTaskPageVO taskPageVO = (MyTaskPageVO) workItemItr.next();
					if (taskPageVO.getTaskName().equals("Complete Case Creation")) {
						if (taskPageVO.getCinterim1() != null) {
							intrim1List.add(taskPageVO);
							intrim1List.add(this.getIncomingWorkStepForOA(taskPageVO));
							intrim1List.add(this.getIncomingWorkStepForTA(taskPageVO));
							intrim1List.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
							intrim1List.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
							intrim1List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim1", false));
							intrim1List.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
						} else {
							finalList.add(taskPageVO);
							finalList.add(this.getIncomingWorkStepForOA(taskPageVO));
							finalList.add(this.getIncomingWorkStepForTA(taskPageVO));
							finalList.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
							finalList.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
							finalList.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Final", false));
							finalList.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
							finalList.add(this.getIncomingWorkStepForInvoicing(taskPageVO));
						}
					} else if (taskPageVO.getTaskName().equals("Office Assignment Task")) {
						if (taskPageVO.getCinterim1() != null) {
							intrim1List.add(this.getCompletedWorkStepCCC(taskPageVO));
							intrim1List.add(taskPageVO);
							intrim1List.add(this.getIncomingWorkStepForTA(taskPageVO));
							intrim1List.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
							intrim1List.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
							intrim1List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim1", false));
							intrim1List.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
						} else {
							finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
							finalList.add(taskPageVO);
							finalList.add(this.getIncomingWorkStepForTA(taskPageVO));
							finalList.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
							finalList.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
							finalList.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Final", false));
							finalList.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
						}

						if (taskPageVO.getCinterim2() != null) {
							intrim2List.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
							intrim2List.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
							intrim2List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim2", false));
							intrim2List.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
						}
					} else {
						boolean isShow;
						if (taskPageVO.getTaskName().equals("Team Assignment Task")) {
							isShow = taskPageVO.getTeamName().contains("Primary");
							if (taskPageVO.getCurrentCycle().equals("Interim1")) {
								if (isShow) {
									if (!this.taskAdded.containsKey("Complete Case Creation")) {
										intrim1List.add(this.getCompletedWorkStepCCC(taskPageVO));
									}

									if (!this.taskAdded.containsKey("Office Assignment Task")) {
										intrim1List.add(this.getCompletedWorkStepForOA(taskPageVO));
									}
								}

								intrim1List.add(taskPageVO);
								if (!this.taskAdded.containsKey("BI Research Task#Interim1")) {
									intrim1List.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim1"));
								}

								intrim1List.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
								intrim1List.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
								intrim1List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim1", false));
								if (!this.taskAdded.containsKey("Research Task#Final")) {
									finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
								}

								if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
									finalList.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
								}

								if (isShow) {
									intrim1List.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
								}
							} else if (taskPageVO.getCurrentCycle().equals("Interim2")) {
								intrim2List.add(taskPageVO);
								intrim2List.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
								intrim2List.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
								intrim2List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim2", false));
								if (isShow) {
									intrim2List.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
								}
							} else {
								if (isShow) {
									if (!this.taskAdded.containsKey("Complete Case Creation")) {
										finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
									}

									if (!this.taskAdded.containsKey("Office Assignment Task")) {
										finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
									}
								}

								finalList.add(taskPageVO);
								finalList.add(this.getIncomingWorkStepForResearch(taskPageVO, ""));
								finalList.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
								finalList.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Final", false));
								if (!this.taskAdded.containsKey("Research Task#Final")) {
									finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
								}

								if (!this.taskAdded.containsKey("BI Research Task#Final")) {
									finalList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Final"));
								}

								if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
									finalList.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
								}

								if (isShow) {
									finalList.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
								}
							}
						} else if (taskPageVO.getTaskName().equals("Research Task")) {
							String performer = taskPageVO.getPerformer();
							boolean isPrimary = taskPageVO.getTeamName().contains("Primary");
							if (taskPageVO.getCurrentCycle().equals("Interim1")) {
								if (isPrimary) {
									if (!this.taskAdded.containsKey("Complete Case Creation")) {
										intrim1List.add(this.getCompletedWorkStepCCC(taskPageVO));
									}

									if (!this.taskAdded.containsKey("Office Assignment Task")) {
										intrim1List.add(this.getCompletedWorkStepForOA(taskPageVO));
									}
								}

								if (!this.taskAdded.containsKey("Team Assignment Task")) {
									intrim1List.addAll(this.getCompletedWorkStepForTA(taskPageVO));
								}

								if (!this.taskAdded.containsKey("Research Task#Interim1")) {
									intrim1List.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim1"));
								}

								if (!this.taskAdded.containsKey("BI Research Task#Interim1")) {
									intrim1List.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim1"));
								}

								if (!this.taskAdded.containsKey("Review Task_supporting#Interim1")) {
									intrim1List.addAll(
											this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Interim1"));
								}

								intrim1List.add(taskPageVO);
								if (!this.taskAdded.containsKey("FutureConsolidation#Interim1")) {
									intrim1List
											.addAll(this.getIncomingWorkStepForConsolidation(taskPageVO, "Interim1"));
								}

								if (!this.taskAdded.containsKey("Consolidation Task#Interim1")) {
									intrim1List.addAll(this.getCompletedConsolidationWorkStepForSupporting(taskPageVO,
											"Interim1"));
								}

								if (!this.taskAdded.containsKey("FutureReview#Interim1")) {
									intrim1List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim1", true));
								}

								if (isPrimary && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Interim1")) {
									intrim1List.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Interim1"));
								}
							} else if (taskPageVO.getCurrentCycle().equals("Interim2")) {
								if (this.getAvailableResearchTask(taskPageVO.getCustmDSMap(), "Interim1", performer,
										taskPageVO.getTeamTypeList())) {
									if (!this.activeCycleMap.containsKey("Interim1")) {
										intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
									}

									if (!this.taskAdded.containsKey("Research Task#Interim2")) {
										intrim2List
												.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim2"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Interim2")) {
										intrim2List
												.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim2"));
									}

									if (!this.taskAdded.containsKey("Review Task_supporting#Interim2")) {
										intrim2List.addAll(
												this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Interim2"));
									}

									intrim2List.add(taskPageVO);
								} else {
									intrim2List.add(this.getIncomingWorkStepForResearch(taskPageVO, performer));
								}

								if (!this.taskAdded.containsKey("FutureConsolidation#Interim2")) {
									intrim2List
											.addAll(this.getIncomingWorkStepForConsolidation(taskPageVO, "Interim2"));
								}

								if (!this.taskAdded.containsKey("Consolidation Task#Interim2")) {
									intrim2List.addAll(this.getCompletedConsolidationWorkStepForSupporting(taskPageVO,
											"Interim2"));
								}

								if (!this.taskAdded.containsKey("FutureReview#Interim2")) {
									intrim2List.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Interim2", true));
								}

								if (isPrimary && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Interim2")) {
									intrim2List.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Interim2"));
								}
							} else {
								if (this.getAvailableResearchTask(taskPageVO.getCustmDSMap(), "Interim1", performer,
										taskPageVO.getTeamTypeList())
										&& this.getAvailableResearchTask(taskPageVO.getCustmDSMap(), "Interim2",
												performer, taskPageVO.getTeamTypeList())) {
									if (taskPageVO.getCinterim1() == null) {
										if (isPrimary) {
											if (!this.taskAdded.containsKey("Complete Case Creation")) {
												finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
											}

											if (!this.taskAdded.containsKey("Office Assignment Task")) {
												finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
											}
										}

										if (!this.taskAdded.containsKey("Team Assignment Task")) {
											finalList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
										}
									} else {
										if (!this.activeCycleMap.containsKey("Interim1")) {
											intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
										}

										if (taskPageVO.getCinterim2() != null
												&& !this.activeCycleMap.containsKey("Interim2")) {
											intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
										}
									}

									if (!this.taskAdded.containsKey("Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
										finalList.addAll(
												this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("Consolidation Task#Final")) {
										finalList.addAll(this.getCompletedConsolidationWorkStepForSupporting(taskPageVO,
												"Final"));
									}

									finalList.add(taskPageVO);
								} else {
									finalList.add(this.getIncomingWorkStepForResearch(taskPageVO, performer));
									if (!this.taskAdded.containsKey("Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
										finalList.addAll(
												this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("Consolidation Task#Final")) {
										finalList.addAll(this.getCompletedConsolidationWorkStepForSupporting(taskPageVO,
												"Final"));
									}
								}

								if (!this.taskAdded.containsKey("FutureConsolidation#Final")) {
									finalList.addAll(this.getIncomingWorkStepForConsolidation(taskPageVO, "Final"));
								}

								if (!this.taskAdded.containsKey("FutureReview#Final")) {
									finalList.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Final", true));
								}

								if (isPrimary && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Final")) {
									finalList.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Final"));
								}
							}
						} else if (!taskPageVO.getTaskName().equals("BI Research Task")
								&& !taskPageVO.getTaskName().equals("Vendor Research Task")) {
							if (taskPageVO.getTaskName().contains("Consolidation Task")) {
								isShow = taskPageVO.getTeamName().contains("Primary");
								if (taskPageVO.getCurrentCycle() != null
										&& taskPageVO.getCurrentCycle().equals("Interim1")) {
									if (isShow) {
										if (!this.taskAdded.containsKey("Complete Case Creation")) {
											intrim1List.add(this.getCompletedWorkStepCCC(taskPageVO));
										}

										if (!this.taskAdded.containsKey("Office Assignment Task")) {
											intrim1List.add(this.getCompletedWorkStepForOA(taskPageVO));
										}

										intrim1List.addAll(this.getCompletedConsolidationWorkStepForSupporting(
												taskPageVO, "Interim1"));
										if (!this.taskAdded.containsKey("Review Task_supporting#Interim1")) {
											intrim1List.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO,
													"Interim1"));
										}
									}

									if (!this.taskAdded.containsKey("Team Assignment Task")) {
										intrim1List.addAll(this.getCompletedWorkStepForTA(taskPageVO));
									}

									if (!this.taskAdded.containsKey("Research Task#Interim1")) {
										intrim1List
												.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim1"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Interim1")) {
										intrim1List
												.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim1"));
									}

									intrim1List.add(taskPageVO);
									if (!this.taskAdded.containsKey("FutureReview#Interim1")) {
										intrim1List.addAll(
												this.getIncomingWorkStepForReview(taskPageVO, "Interim1", true));
									}

									if (isShow && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Interim1")) {
										intrim1List.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Interim1"));
									}
								} else if (taskPageVO.getCurrentCycle() != null
										&& taskPageVO.getCurrentCycle().equals("Interim2")) {
									if (!this.activeCycleMap.containsKey("Interim1")) {
										intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
									}

									if (!this.taskAdded.containsKey("Research Task#Interim2")) {
										intrim2List
												.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim2"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Interim2")) {
										intrim2List
												.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim2"));
									}

									intrim2List.add(taskPageVO);
									if (isShow) {
										intrim2List.addAll(this.getCompletedConsolidationWorkStepForSupporting(
												taskPageVO, "Interim2"));
										if (!this.taskAdded.containsKey("Review Task_supporting#Interim2")) {
											intrim2List.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO,
													"Interim2"));
										}
									}

									if (!this.taskAdded.containsKey("FutureReview#Interim2")) {
										intrim2List.addAll(
												this.getIncomingWorkStepForReview(taskPageVO, "Interim2", true));
									}

									if (isShow && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Interim2")) {
										intrim2List.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Interim2"));
									}
								} else {
									if (taskPageVO.getCinterim1() == null) {
										if (isShow) {
											if (!this.taskAdded.containsKey("Complete Case Creation")) {
												finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
											}

											if (!this.taskAdded.containsKey("Office Assignment Task")) {
												finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
											}
										}

										if (!this.taskAdded.containsKey("Team Assignment Task")) {
											finalList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
										}
									} else {
										if (!this.activeCycleMap.containsKey("Interim1")) {
											intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
										}

										if (taskPageVO.getCinterim2() != null
												&& !this.activeCycleMap.containsKey("Interim2")) {
											intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
										}
									}

									if (!this.taskAdded.containsKey("Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Final"));
									}

									if (isShow) {
										finalList.addAll(this.getCompletedConsolidationWorkStepForSupporting(taskPageVO,
												"Final"));
									}

									if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
										finalList.addAll(
												this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
									}

									finalList.add(taskPageVO);
									if (!this.taskAdded.containsKey("FutureReview#Final")) {
										finalList.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Final", true));
									}

									if (isShow && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Final")) {
										finalList.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Final"));
									}
								}
							} else if (taskPageVO.getTaskName().contains("Review")) {
								isShow = taskPageVO.getTeamName().contains("Primary");
								if (taskPageVO.getCurrentCycle() != null
										&& taskPageVO.getCurrentCycle().equals("Interim1")) {
									if (isShow) {
										if (!this.taskAdded.containsKey("Complete Case Creation")) {
											intrim1List.add(this.getCompletedWorkStepCCC(taskPageVO));
										}

										if (!this.taskAdded.containsKey("Office Assignment Task")) {
											intrim1List.add(this.getCompletedWorkStepForOA(taskPageVO));
										}
									}

									if (!this.taskAdded.containsKey("Team Assignment Task")) {
										intrim1List.addAll(this.getCompletedWorkStepForTA(taskPageVO));
									}

									if (!this.taskAdded.containsKey("Research Task#Interim1")) {
										intrim1List
												.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim1"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Interim1")) {
										intrim1List
												.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim1"));
									}

									if (isShow) {
										intrim1List.addAll(
												this.getCompletedConsolidationWorkStep(taskPageVO, "Interim1", true));
										if (!this.taskAdded.containsKey("Review Task#Interim1")) {
											intrim1List.addAll(this.getCompletedReviewWorkStep(taskPageVO, "Interim1"));
										}
									} else {
										intrim1List.addAll(this.getCompletedConsolidationWorkStepForSupporting(
												taskPageVO, "Interim1"));
										if (!this.taskAdded.containsKey("Review Task_supporting#Interim1")) {
											intrim1List.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO,
													"Interim1"));
										}
									}

									intrim1List.add(taskPageVO);
									if (!this.taskAdded.containsKey("FutureReview#Interim1")) {
										intrim1List.addAll(
												this.getIncomingWorkStepForReview(taskPageVO, "Interim1", true));
									}

									if (isShow && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Interim1")) {
										intrim1List.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Interim1"));
									}
								} else if (taskPageVO.getCurrentCycle() != null
										&& taskPageVO.getCurrentCycle().equals("Interim2")) {
									if (!this.activeCycleMap.containsKey("Interim1")) {
										intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
									}

									if (!this.taskAdded.containsKey("Research Task#Interim2")) {
										intrim2List
												.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim2"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Interim2")) {
										intrim2List
												.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim2"));
									}

									if (isShow) {
										intrim2List.addAll(
												this.getCompletedConsolidationWorkStep(taskPageVO, "Interim2", true));
										if (!this.taskAdded.containsKey("Review Task#Interim2")) {
											intrim2List.addAll(this.getCompletedReviewWorkStep(taskPageVO, "Interim2"));
										}
									} else {
										intrim2List.addAll(this.getCompletedConsolidationWorkStepForSupporting(
												taskPageVO, "Interim2"));
										if (!this.taskAdded.containsKey("Review Task_supporting#Interim2")) {
											intrim2List.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO,
													"Interim2"));
										}
									}

									intrim2List.add(taskPageVO);
									if (!this.taskAdded.containsKey("FutureReview#Interim2")) {
										intrim2List.addAll(
												this.getIncomingWorkStepForReview(taskPageVO, "Interim2", true));
									}

									if (isShow && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Interim2")) {
										intrim2List.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Interim2"));
									}
								} else {
									if (taskPageVO.getCinterim1() == null) {
										if (isShow) {
											if (!this.taskAdded.containsKey("Complete Case Creation")) {
												finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
											}

											if (!this.taskAdded.containsKey("Office Assignment Task")) {
												finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
											}
										}

										if (!this.taskAdded.containsKey("Team Assignment Task")) {
											finalList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
										}
									} else {
										if (!this.activeCycleMap.containsKey("Interim1")) {
											intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
										}

										if (taskPageVO.getCinterim2() != null
												&& !this.activeCycleMap.containsKey("Interim2")) {
											intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
										}
									}

									if (!this.taskAdded.containsKey("Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
									}

									if (!this.taskAdded.containsKey("BI Research Task#Final")) {
										finalList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Final"));
									}

									if (isShow) {
										finalList.addAll(
												this.getCompletedConsolidationWorkStep(taskPageVO, "Final", true));
										if (!this.taskAdded.containsKey("Review Task#Final")) {
											finalList.addAll(this.getCompletedReviewWorkStep(taskPageVO, "Final"));
										}
									} else {
										finalList.addAll(this.getCompletedConsolidationWorkStepForSupporting(taskPageVO,
												"Final"));
										if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
											finalList.addAll(
													this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
										}
									}

									finalList.add(taskPageVO);
									if (!this.taskAdded.containsKey("FutureReview#Final")) {
										finalList.addAll(this.getIncomingWorkStepForReview(taskPageVO, "Final", true));
									}

									if (isShow && !this.taskAdded.containsKey(FUTURE_CLIENT_SUB + "#" + "Final")) {
										finalList.add(this.getIncomingWorkStepForClientSub(taskPageVO, "Final"));
									}
								}
							} else if (taskPageVO.getTaskName().contains("Client Submission Task")) {
								isShow = taskPageVO.getTeamName().contains("Primary");
								if (taskPageVO.getCurrentCycle() != null
										&& taskPageVO.getCurrentCycle().equals("Interim1")) {
									if (isShow && taskPageVO.getCinterim1() != null) {
										if (!this.taskAdded.containsKey("Complete Case Creation")) {
											intrim1List.add(this.getCompletedWorkStepCCC(taskPageVO));
										}

										if (!this.taskAdded.containsKey("Office Assignment Task")) {
											intrim1List.add(this.getCompletedWorkStepForOA(taskPageVO));
										}

										if (!this.taskAdded.containsKey("Team Assignment Task")) {
											intrim1List.addAll(this.getCompletedWorkStepForTA(taskPageVO));
										}

										intrim1List
												.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim1"));
										intrim1List
												.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim1"));
										intrim1List.addAll(
												this.getCompletedConsolidationWorkStep(taskPageVO, "Interim1", true));
										intrim1List.addAll(this.getCompletedReviewWorkStep(taskPageVO, "Interim1"));
									}

									intrim1List.add(taskPageVO);
								} else if (taskPageVO.getCurrentCycle() != null
										&& taskPageVO.getCurrentCycle().equals("Interim2")) {
									if (!this.activeCycleMap.containsKey("Interim1")) {
										intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
									}

									intrim2List.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Interim2"));
									intrim2List.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Interim2"));
									intrim2List.addAll(
											this.getCompletedConsolidationWorkStep(taskPageVO, "Interim2", true));
									intrim2List.addAll(this.getCompletedReviewWorkStep(taskPageVO, "Interim2"));
									intrim2List.add(taskPageVO);
								} else {
									if (taskPageVO.getCinterim1() == null) {
										if (isShow) {
											if (!this.taskAdded.containsKey("Complete Case Creation")) {
												finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
											}

											if (!this.taskAdded.containsKey("Office Assignment Task")) {
												finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
											}

											if (!this.taskAdded.containsKey("Team Assignment Task")) {
												finalList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
											}
										}
									} else {
										if (!this.activeCycleMap.containsKey("Interim1")) {
											intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
										}

										if (taskPageVO.getCinterim2() != null
												&& !this.activeCycleMap.containsKey("Interim2")) {
											intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
										}
									}

									finalList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, "Final"));
									finalList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, "Final"));
									finalList.addAll(this.getCompletedConsolidationWorkStep(taskPageVO, "Final", true));
									finalList.addAll(this.getCompletedReviewWorkStep(taskPageVO, "Final"));
									finalList.add(taskPageVO);
								}
							} else if (taskPageVO.getTaskName().equals("Invoicing Task")) {
								if (this.activeWorkStepMap.get("Invoicing Task") != null
										&& this.activeWorkStepMap.size() == 1) {
									if (taskPageVO.getCinterim1() == null) {
										finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
										finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
										finalList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
										finalList.addAll(this.getAllIntrimCycleInfo("Final", taskPageVO));
									} else {
										if (!this.activeCycleMap.containsKey("Interim1")) {
											intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
										}

										if (taskPageVO.getCinterim2() != null
												&& !this.activeCycleMap.containsKey("Interim2")) {
											intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
										}

										finalList.addAll(this.getAllIntrimCycleInfo("Final", taskPageVO));
									}
								}

								if (taskPageVO.getPerformer() != null && !taskPageVO.getPerformer().isEmpty()) {
									if (taskPageVO.getPerformer().equals("FinanceQueue")) {
										taskPageVO.setUserFullName("FinanceQueue");
									}
								} else {
									taskPageVO.setPerformer("FinanceQueue");
									taskPageVO.setUserFullName("FinanceQueue");
								}

								finalList.add(taskPageVO);
							} else if (taskPageVO.getTaskName().equals("ProcessCompletion")) {
								if (taskPageVO.getCinterim1() == null) {
									finalList.add(this.getCompletedWorkStepCCC(taskPageVO));
									finalList.add(this.getCompletedWorkStepForOA(taskPageVO));
									finalList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
									finalList.addAll(this.getAllIntrimCycleInfo("Final", taskPageVO));
								} else {
									if (!this.activeCycleMap.containsKey("Interim1")) {
										intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
									}

									if (taskPageVO.getCinterim2() != null
											&& !this.activeCycleMap.containsKey("Interim2")) {
										intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
									}

									finalList.addAll(this.getAllIntrimCycleInfo("Final", taskPageVO));
								}
							}
						} else if (taskPageVO.getCurrentCycle().equals("Interim1")) {
							if (!this.taskAdded.containsKey("Review Task_supporting#Interim1")) {
								intrim1List
										.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Interim1"));
							}

							intrim1List.add(taskPageVO);
						} else if (taskPageVO.getCurrentCycle().equals("Interim2")) {
							if (this.getAvailableResearchTask(taskPageVO.getCustmDSMap(), "Interim1",
									taskPageVO.getPerformer(), taskPageVO.getTeamTypeList())) {
								if (!this.taskAdded.containsKey("Review Task_supporting#Interim2")) {
									intrim2List.addAll(
											this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Interim2"));
								}

								intrim2List.add(taskPageVO);
							} else {
								intrim2List.add(
										this.getIncomingWorkStepForBIResearch(taskPageVO, taskPageVO.getPerformer()));
							}
						} else if (this.getAvailableResearchTask(taskPageVO.getCustmDSMap(), "Interim1",
								taskPageVO.getPerformer(), taskPageVO.getTeamTypeList())
								&& this.getAvailableResearchTask(taskPageVO.getCustmDSMap(), "Interim2",
										taskPageVO.getPerformer(), taskPageVO.getTeamTypeList())) {
							if (!this.taskAdded.containsKey("Review Task_supporting#Final")) {
								finalList.addAll(this.getCompletedReviewWorkStepForSupporting(taskPageVO, "Final"));
							}

							finalList.add(taskPageVO);
							if (this.isOnlySupportingTaskActive(this.activeWorkStepMap)) {
								if (taskPageVO.getCinterim1() != null && !this.activeCycleMap.containsKey("Interim1")
										&& !this.taskAdded.containsKey("AllTask#Interim1")) {
									intrim1List.addAll(this.getAllIntrimCycleInfo("Interim1", taskPageVO));
								}

								if (taskPageVO.getCinterim2() != null && !this.activeCycleMap.containsKey("Interim2")
										&& !this.taskAdded.containsKey("AllTask#Interim2")) {
									intrim2List.addAll(this.getAllIntrimCycleInfo("Interim2", taskPageVO));
								}
							}
						} else {
							finalList.add(this.getIncomingWorkStepForBIResearch(taskPageVO, taskPageVO.getPerformer()));
						}
					}
				}

				if (!this.activeWorkStepMap.containsKey("Complete Case Creation")) {
					finalList.add(this.getCompletedWorkStepForInvoicing());
				}

				this.logger.debug(
						"--------------------------" + this.taskAdded + "---------------------------------------");
				break;
			}
		} else {
			this.logger.debug("-------------- Process has been completed now getting data from custom db");
			teamDetailList = ResourceLocator.self().getTeamAssignmentService().getCaseTeamDetails(crn);
			CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
			TeamDetails teamDetails = this.getPrimaryTeamObj(teamDetailList);
			this.logger.debug("caseDetails " + caseDetails);
			this.logger.debug("teamDetailList " + teamDetailList);
			if (caseDetails != null && teamDetails != null) {
				this.logger.debug("teamDetailList size " + teamDetailList.size());
				if (caseDetails.getcInterim1() != null) {
					intrim1List.add(this.getCompletedCCCTaskCustom(caseDetails, "Interim1"));
					intrim1List.add(this.getCompletedOATaskCustom(caseDetails, "Interim1"));
					intrim1List.addAll(this.getCompletedTATaskCustom(teamDetailList, "Interim1"));
					intrim1List.addAll(this.getCompletedResearchTaskCustom(teamDetailList, "Interim1"));
					intrim1List.addAll(this.getCompletedBIResearchTaskCustom(teamDetailList, "Interim1"));
					intrim1List.addAll(this.getCompletedConsolidationTaskCustom(teamDetailList, "Interim1"));
					intrim1List.addAll(this.getCompletedReviewTaskCustom(teamDetailList, "Interim1"));
					intrim1List.add(this.getCompletedCSTaskCustom(teamDetails, "Interim1"));
				}

				if (caseDetails.getcInterim2() != null) {
					intrim2List.addAll(this.getCompletedResearchTaskCustom(teamDetailList, "Interim2"));
					intrim2List.addAll(this.getCompletedBIResearchTaskCustom(teamDetailList, "Interim2"));
					intrim2List.addAll(this.getCompletedConsolidationTaskCustom(teamDetailList, "Interim2"));
					intrim2List.addAll(this.getCompletedReviewTaskCustom(teamDetailList, "Interim1"));
					intrim2List.add(this.getCompletedCSTaskCustom(teamDetails, "Interim2"));
				}

				if (caseDetails.getcInterim1() == null) {
					finalList.add(this.getCompletedCCCTaskCustom(caseDetails, "Final"));
					finalList.add(this.getCompletedOATaskCustom(caseDetails, "Final"));
					finalList.addAll(this.getCompletedTATaskCustom(teamDetailList, "Final"));
				}

				finalList.addAll(this.getCompletedResearchTaskCustom(teamDetailList, "Final"));
				finalList.addAll(this.getCompletedBIResearchTaskCustom(teamDetailList, "Final"));
				finalList.addAll(this.getCompletedConsolidationTaskCustom(teamDetailList, "Final"));
				finalList.addAll(this.getCompletedReviewTaskCustom(teamDetailList, "Interim1"));
				finalList.add(this.getCompletedCSTaskCustom(teamDetails, "Final"));
				finalList.add(this.getCompletedInvoicingTaskCustom());
			}
		}

		allTaskMap.put("Intrim1", this.getTeamList(intrim1List));
		allTaskMap.put("Intrim2", this.getTeamList(intrim2List));
		allTaskMap.put("Final", this.getTeamList(finalList));
		this.logger.debug("Intrim1 >>>>>>>>>>>>>>>>>>>>>>>>" + intrim1List);
		this.logger.debug("Intrim2 >>>>>>>>>>>>>>>>>>>>>>>>" + intrim2List);
		this.logger.debug("Final >>>>>>>>>>>>>>>>>>>>>>>>" + finalList);
		this.logger.debug("##################################################################");
		return allTaskMap;
	}

	private TeamDetails getPrimaryTeamObj(List<TeamDetails> teamDetailList) {
		Iterator<TeamDetails> teamItr = teamDetailList.iterator();
		TeamDetails teamDetails = null;

		while (teamItr.hasNext()) {
			teamDetails = (TeamDetails) teamItr.next();
			if ("Primary".contains(teamDetails.getTeamType()) || "1".equals(teamDetails.getTeamTypeId())) {
				break;
			}
		}

		return teamDetails;
	}

	private MyTaskPageVO getCompletedCCCTaskCustom(CaseDetails caseDetails, String currentCycle) {
		this.logger.debug("i am in getCompletedCCCTaskCustom");
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Complete Case Creation");
		if (caseDetails != null) {
			myTaskPageVO.setPerformer(caseDetails.getCaseCreatorId());
		}

		myTaskPageVO.setCurrentCycle(currentCycle);
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
		return myTaskPageVO;
	}

	private MyTaskPageVO getCompletedOATaskCustom(CaseDetails caseDetails, String currentCycle) {
		this.logger.debug("i am in getCompletedOATaskCustom");
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Office Assignment Task");
		if (caseDetails != null) {
			myTaskPageVO.setPerformer(caseDetails.getCaseMgrId());
			myTaskPageVO.setUserFullName(caseDetails.getCaseManagerName());
		}

		myTaskPageVO.setCurrentCycle(currentCycle);
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
		return myTaskPageVO;
	}

	private List<MyTaskPageVO> getCompletedTATaskCustom(List<TeamDetails> teamDetailsList, String currentCycle) {
		this.logger.debug("i am in getCompletedTATaskCustom");
		Iterator<TeamDetails> teamItr = teamDetailsList.iterator();
		List<MyTaskPageVO> teamAssgnmentTaskList = new ArrayList();
		TeamDetails teamDetails = null;

		while (true) {
			do {
				if (!teamItr.hasNext()) {
					return teamAssgnmentTaskList;
				}

				teamDetails = (TeamDetails) teamItr.next();
			} while ((teamDetails == null || !"Primary".contains(teamDetails.getTeamType()))
					&& !"Supporting - Internal".contains(teamDetails.getTeamType())
					&& !teamDetails.getTeamTypeId().equals("1") && !teamDetails.getTeamTypeId().equals("2"));

			MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
			myTaskPageVO.setTaskName("Team Assignment Task");
			if (teamDetails != null) {
				myTaskPageVO.setPerformer(teamDetails.getResearchHead());
				myTaskPageVO.setTeamName(teamDetails.getTeamName());
				myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
			}

			myTaskPageVO.setCurrentCycle(currentCycle);
			myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
			teamAssgnmentTaskList.add(myTaskPageVO);
		}
	}

	private List<MyTaskPageVO> getCompletedResearchTaskCustom(List<TeamDetails> teamDetailsList, String currentCycle) {
		this.logger.debug("i am in getCompletedResearchTaskCustom");
		Iterator<TeamDetails> teamItr = teamDetailsList.iterator();
		List<MyTaskPageVO> researchTaskList = new ArrayList();
		TeamDetails teamDetails = null;

		while (true) {
			do {
				if (!teamItr.hasNext()) {
					return researchTaskList;
				}

				teamDetails = (TeamDetails) teamItr.next();
			} while ((teamDetails == null || !"Primary".contains(teamDetails.getTeamType()))
					&& !"Supporting - Internal".contains(teamDetails.getTeamType())
					&& !teamDetails.getTeamTypeId().equals("1") && !teamDetails.getTeamTypeId().equals("2"));

			String[] analysts = teamDetails.getAnalysts().split(",");
			String[] performers = teamDetails.getPerformer().split(",");

			for (int count = 0; count < analysts.length; ++count) {
				MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
				myTaskPageVO.setTaskName("Research Task");
				if (teamDetails != null) {
					myTaskPageVO.setTeamName(teamDetails.getTeamName());
					myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
				}

				myTaskPageVO.setPerformer(performers[0]);
				myTaskPageVO.setUserFullName(analysts[0]);
				myTaskPageVO.setCurrentCycle(currentCycle);
				myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
				researchTaskList.add(myTaskPageVO);
			}
		}
	}

	private List<MyTaskPageVO> getCompletedBIResearchTaskCustom(List<TeamDetails> teamDetailsList,
			String currentCycle) {
		this.logger.debug("i am in getCompletedBIResearchTaskCustom");
		Iterator<TeamDetails> teamItr = teamDetailsList.iterator();
		List<MyTaskPageVO> biResearchTaskList = new ArrayList();
		TeamDetails teamDetails = null;

		while (true) {
			do {
				if (!teamItr.hasNext()) {
					return biResearchTaskList;
				}

				teamDetails = (TeamDetails) teamItr.next();
			} while ((teamDetails == null || !teamDetails.getTeamType().equals("Supporting - BI"))
					&& !teamDetails.getTeamType().equals("Supporting - Vendor")
					&& !teamDetails.getTeamTypeId().equals("3") && !teamDetails.getTeamTypeId().equals("4"));

			MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
			if (teamDetails.getTeamType().equals("Supporting - BI")) {
				myTaskPageVO.setTaskName("BI Research Task");
			} else {
				myTaskPageVO.setTaskName("Vendor Research Task");
			}

			if (teamDetails != null) {
				myTaskPageVO.setPerformer(teamDetails.getManagerName());
				myTaskPageVO.setUserFullName(teamDetails.getManagerFullName());
				myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
				myTaskPageVO.setTeamName(teamDetails.getTeamName());
			}

			myTaskPageVO.setCurrentCycle(currentCycle);
			myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
			biResearchTaskList.add(myTaskPageVO);
		}
	}

	private List<MyTaskPageVO> getCompletedConsolidationTaskCustom(List<TeamDetails> teamDetailsList,
			String currentCycle) {
		this.logger.debug("i am in getCompletedConsolidationTaskCustom");
		Iterator<TeamDetails> teamItr = teamDetailsList.iterator();
		List<MyTaskPageVO> consolidationTaskList = new ArrayList();
		TeamDetails teamDetails = null;

		while (true) {
			String[] analysts;
			do {
				do {
					if (!teamItr.hasNext()) {
						return consolidationTaskList;
					}

					teamDetails = (TeamDetails) teamItr.next();
				} while (teamDetails == null);

				String analyst = teamDetails.getAnalysts();
				analysts = new String[0];
				if (analyst != null) {
					analysts = analyst.split(",");
				}
			} while (!teamDetails.getTeamType().equals("Primary")
					&& (!teamDetails.getTeamType().equals("Supporting - Internal") || analysts.length <= 1));

			MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
			myTaskPageVO.setTaskName("Consolidation Task");
			myTaskPageVO.setPerformer(teamDetails.getMainAnalyst());
			myTaskPageVO.setUserFullName(teamDetails.getManagerFullName());
			myTaskPageVO.setTeamName(teamDetails.getTeamName());
			myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
			myTaskPageVO.setCurrentCycle(currentCycle);
			myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
			consolidationTaskList.add(myTaskPageVO);
		}
	}

	private List<MyTaskPageVO> getCompletedReviewTaskCustom(List<TeamDetails> teamDetailsList, String currentCycle) {
		this.logger.debug("i am in getCompletedReviewTaskCustom");
		Iterator<TeamDetails> teamItr = teamDetailsList.iterator();
		List<MyTaskPageVO> reviewTaskList = new ArrayList();
		TeamDetails teamDetails = null;

		while (true) {
			do {
				if (!teamItr.hasNext()) {
					return reviewTaskList;
				}

				teamDetails = (TeamDetails) teamItr.next();
			} while ((teamDetails == null || !teamDetails.getTeamType().equals("Primary"))
					&& !teamDetails.getTeamType().equals("Supporting - Internal"));

			MyTaskPageVO myTaskPageVO;
			if (teamDetails.getReviewer1() != null) {
				myTaskPageVO = new MyTaskPageVO();
				myTaskPageVO.setTaskName("Review Task");
				myTaskPageVO.setPerformer(teamDetails.getReviewer1());
				myTaskPageVO.setUserFullName(teamDetails.getRev1FullName());
				myTaskPageVO.setCurrentCycle(currentCycle);
				myTaskPageVO.setTeamName(teamDetails.getTeamName());
				myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
				myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
				reviewTaskList.add(myTaskPageVO);
			}

			if (teamDetails.getReviewer2() != null) {
				myTaskPageVO = new MyTaskPageVO();
				myTaskPageVO.setTaskName("Review Task");
				myTaskPageVO.setPerformer(teamDetails.getReviewer2());
				myTaskPageVO.setUserFullName(teamDetails.getRev2FullName());
				myTaskPageVO.setCurrentCycle(currentCycle);
				myTaskPageVO.setTeamName(teamDetails.getTeamName());
				myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
				myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
				reviewTaskList.add(myTaskPageVO);
			}

			if (teamDetails.getReviewer3() != null) {
				myTaskPageVO = new MyTaskPageVO();
				myTaskPageVO.setTaskName("Review Task");
				myTaskPageVO.setPerformer(teamDetails.getReviewer3());
				myTaskPageVO.setUserFullName(teamDetails.getRev3FullName());
				myTaskPageVO.setCurrentCycle(currentCycle);
				myTaskPageVO.setTeamName(teamDetails.getTeamName());
				myTaskPageVO.setTeamTypeList(teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
				myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
				reviewTaskList.add(myTaskPageVO);
			}
		}
	}

	private boolean isOnlySupportingTaskActive(Map<String, String> activeMap) {
		this.logger.debug(" I am in isOnlySupportingTaskActive");
		boolean isOnlyAvail = false;

		try {
			Set activeTaskSet = activeMap.keySet();

			for (Iterator iterator = activeTaskSet.iterator(); iterator.hasNext(); isOnlyAvail = true) {
				String activeTaskName = (String) iterator.next();
				if (!activeTaskName.equals("Invoicing Task") && !activeTaskName.equals("BI Research Task")
						&& !activeTaskName.equals("Vendor Research Task")) {
					isOnlyAvail = false;
					break;
				}
			}

			this.logger.debug("isOnlyAvail " + isOnlyAvail);
			this.logger.debug("End of isOnlySupportingTaskActive");
		} catch (Exception var6) {
			isOnlyAvail = false;
			this.logger.debug(var6.getMessage());
		}

		return isOnlyAvail;
	}

	private MyTaskPageVO getCompletedCSTaskCustom(TeamDetails teamDetails, String currentCycle) {
		this.logger.debug("i am in getCompletedCSTaskCustom");
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Client Submission Task");
		if (teamDetails != null) {
			myTaskPageVO.setPerformer(teamDetails.getMainAnalyst());
		}

		myTaskPageVO.setCurrentCycle(currentCycle);
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
		return myTaskPageVO;
	}

	private MyTaskPageVO getCompletedInvoicingTaskCustom() {
		this.logger.debug("i am in getCompletedInvoicingTaskCustom");
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Invoicing Task");
		myTaskPageVO.setPerformer("FinanceQueue");
		myTaskPageVO.setUserFullName("FinanceQueue");
		myTaskPageVO.setCurrentCycle("Final");
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setWorkStepStatus(COMPLETED_STATUS);
		return myTaskPageVO;
	}

	private List getAllIntrimCycleInfo(String cycleName, MyTaskPageVO taskPageVO) throws CMSException {
		List taskList = new ArrayList();
		if (cycleName.equals("Interim1")) {
			if (!this.taskAdded.containsKey("Complete Case Creation")) {
				taskList.add(this.getCompletedWorkStepCCC(taskPageVO));
			}

			if (!this.taskAdded.containsKey("Office Assignment Task")) {
				taskList.add(this.getCompletedWorkStepForOA(taskPageVO));
			}

			if (!this.taskAdded.containsKey("Team Assignment Task")) {
				taskList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
			}
		}

		if (!this.taskAdded.containsKey("BI Research Task#" + cycleName)) {
			taskList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, cycleName));
		}

		if (!this.taskAdded.containsKey("Research Task#" + cycleName)) {
			taskList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, cycleName));
		}

		if (!this.taskAdded.containsKey("Consolidation Task#" + cycleName)) {
			taskList.addAll(this.getCompletedConsolidationWorkStep(taskPageVO, cycleName, true));
		}

		if (!this.taskAdded.containsKey("Review Task#" + cycleName)) {
			taskList.addAll(this.getCompletedReviewWorkStep(taskPageVO, cycleName));
		}

		if (!this.taskAdded.containsKey("Client Submission Task#" + cycleName)) {
			taskList.add(this.getCompletedWorkStepForClientSub(taskPageVO, cycleName));
		}

		this.taskAdded.put("AllTask#" + cycleName, "");
		return taskList;
	}

	private List getIntrimCycleInfo(String cycleName, MyTaskPageVO taskPageVO) {
		List taskList = new ArrayList();
		if (cycleName.equals("Interim1")) {
			if (!this.taskAdded.containsKey("Complete Case Creation")) {
				taskList.add(this.getCompletedWorkStepCCC(taskPageVO));
			}

			if (!this.taskAdded.containsKey("Office Assignment Task")) {
				taskList.add(this.getCompletedWorkStepForOA(taskPageVO));
			}

			if (!this.taskAdded.containsKey("Team Assignment Task")) {
				taskList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
			}
		}

		if (!this.taskAdded.containsKey("BI Research Task#" + cycleName)) {
			taskList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, cycleName));
		}

		if (!this.taskAdded.containsKey("Research Task#" + cycleName)) {
			taskList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, cycleName));
		}

		taskList.add(this.getIncomingWorkStepForConsolidationForPrimary(taskPageVO));
		taskList.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
		return taskList;
	}

	private List getIntrimCycleInfoForWaitCondition(MyTaskPageVO taskPageVO) throws CMSException {
		List taskList = new ArrayList();
		String cycleName = taskPageVO.getCurrentCycle();
		this.logger.debug("***********************************************************");
		if (cycleName.equals("Interim1") || cycleName.equals("Final") && taskPageVO.getCinterim1() == null) {
			taskList.add(this.getCompletedWorkStepCCC(taskPageVO));
			taskList.add(this.getCompletedWorkStepForOA(taskPageVO));
			taskList.addAll(this.getCompletedWorkStepForTA(taskPageVO));
		}

		taskList.addAll(this.getCompletedWorkStepForBIResearch(taskPageVO, cycleName));
		taskList.addAll(this.getCompletedWorkStepForResearch(taskPageVO, cycleName));
		if (!this.taskAdded.containsKey("FutureConsolidation#" + cycleName)) {
			taskList.addAll(this.getIncomingWorkStepForConsolidation(taskPageVO, cycleName));
		}

		if (!this.taskAdded.containsKey("FutureReview#" + cycleName)) {
			taskList.addAll(this.getIncomingWorkStepForReview(taskPageVO, cycleName, true));
		}

		taskList.add(this.getIncomingWorkStepForClientSub(taskPageVO, ""));
		this.logger.debug("***********************************************************");
		return taskList;
	}

	private List<CaseStatusVO> getTeamList(List list) {
		Map teamMap = new HashMap();
		Iterator itrimItr = list.iterator();

		while (true) {
			while (itrimItr.hasNext()) {
				MyTaskPageVO taskPageVO = (MyTaskPageVO) itrimItr.next();
				this.logger.debug("TeamType list" + taskPageVO.getTeamTypeList());
				this.logger.debug("getTeamTypeList " + taskPageVO.getTeamTypeList());
				this.logger.debug("getTeamName " + taskPageVO.getTeamName());
				this.logger.debug("getTeamType" + taskPageVO.getTeamType());
				ArrayList taskList;
				List taskList;
				if (taskPageVO.getTeamTypeList() != null && !taskPageVO.getTeamTypeList().isEmpty()
						&& !taskPageVO.getTeamTypeList().contains("Primary")) {
					if (teamMap.get(taskPageVO.getTeamTypeList()) == null) {
						taskList = new ArrayList();
						taskList.add(taskPageVO);
						teamMap.put(taskPageVO.getTeamTypeList(), taskList);
					} else {
						taskList = (List) teamMap.get(taskPageVO.getTeamTypeList());
						taskList.add(taskPageVO);
					}
				} else if (teamMap.get("Primary") == null) {
					taskList = new ArrayList();
					taskList.add(taskPageVO);
					teamMap.put("Primary", taskList);
				} else {
					taskList = (List) teamMap.get("Primary");
					taskList.add(taskPageVO);
				}
			}

			this.logger.debug("teamMap ==================" + teamMap);
			Set keyset;
			if (!teamMap.containsKey((Object) null)) {
				this.logger.debug("if not contains null they goiing to apply sort");
				Map<String, String> sortedMap = new TreeMap(teamMap);
				this.logger.debug("sortedMap " + sortedMap);
				keyset = sortedMap.keySet();
			} else {
				this.logger.debug("Team map contained null so not apply team name sort");
				keyset = teamMap.keySet();
			}

			LinkedHashMap<String, String> teamAvailable = new LinkedHashMap();
			Iterator mapItr = keyset.iterator();
			ArrayList teamViseList = new ArrayList();

			while (mapItr.hasNext()) {
				CaseStatusVO caseStatusVO = new CaseStatusVO();
				String teamTypeList = (String) mapItr.next();
				if (teamTypeList.contains("Primary")) {
					caseStatusVO.setTeamName("Primary");
					teamAvailable.put(teamTypeList, "Primary");
				} else if (teamAvailable.containsKey(teamTypeList)) {
					caseStatusVO.setTeamName((String) teamAvailable.get(teamTypeList));
				} else {
					int count = 1;
					String teamName = teamTypeList.split("#")[0];
					Set teamIdSet = teamAvailable.keySet();
					Iterator teamItr = teamIdSet.iterator();

					while (teamItr.hasNext()) {
						String teamId = (String) teamItr.next();
						if (teamId.contains(teamName)) {
							++count;
						}
					}

					teamAvailable.put(teamTypeList, teamName + " " + count);
					caseStatusVO.setTeamName((String) teamAvailable.get(teamTypeList));
				}

				caseStatusVO.setTaskList(this.sortTaskOnStatus((List) teamMap.get(teamTypeList)));
				teamViseList.add(caseStatusVO);
			}

			this.logger.debug("teamTypeList   " + teamViseList.size());
			this.logger.debug("teamAvailable  " + teamAvailable);
			return teamViseList;
		}
	}

	private List<MyTaskPageVO> sortTaskOnStatus(List<MyTaskPageVO> teamViseList) {
		List finalSortedList = new ArrayList();
		List caseCreationList = new ArrayList();
		List officeAssignmentList = new ArrayList();
		List teamAssignmentList = new ArrayList();
		List researchList = new ArrayList();
		List biResearchList = new ArrayList();
		List vendorResearchList = new ArrayList();
		List invoicingList = new ArrayList();
		List consoliationList = new ArrayList();
		List reviewList = new ArrayList();
		List clientSubmissionList = new ArrayList();
		List otherList = new ArrayList();
		int reviewTaskCount = 1;
		Iterator teamViseItr = teamViseList.iterator();

		while (true) {
			while (teamViseItr.hasNext()) {
				MyTaskPageVO myTaskPageVO = (MyTaskPageVO) teamViseItr.next();
				if ("Complete Case Creation".equals(myTaskPageVO.getTaskName())) {
					caseCreationList.add(myTaskPageVO);
				} else if ("Office Assignment Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Office Assignment Task".replace("Task", ""));
					officeAssignmentList.add(myTaskPageVO);
				} else if ("Team Assignment Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Team Assignment Task".replace("Task", ""));
					teamAssignmentList.add(myTaskPageVO);
				} else if ("Research Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Research Task".replace("Task", ""));
					researchList.add(myTaskPageVO);
				} else if ("BI Research Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("BI Research Task".replace("Task", ""));
					biResearchList.add(myTaskPageVO);
				} else if ("Vendor Research Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Vendor Research Task".replace("Task", ""));
					vendorResearchList.add(myTaskPageVO);
				} else if ("Consolidation Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Consolidation Task".replace("Task", ""));
					consoliationList.add(myTaskPageVO);
				} else if (myTaskPageVO.getTaskName() != null && myTaskPageVO.getTaskName().contains("Review Task")) {
					myTaskPageVO.setTaskName("Reviewer " + reviewTaskCount++);
					reviewList.add(myTaskPageVO);
				} else if ("Client Submission Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Client Submission Task".replace("Task", ""));
					clientSubmissionList.add(myTaskPageVO);
				} else if ("Invoicing Task".equals(myTaskPageVO.getTaskName())) {
					myTaskPageVO.setTaskName("Invoicing Task".replace("Task", ""));
					invoicingList.add(myTaskPageVO);
				} else {
					otherList.add(myTaskPageVO);
				}
			}

			finalSortedList.addAll(caseCreationList);
			finalSortedList.addAll(officeAssignmentList);
			finalSortedList.addAll(teamAssignmentList);
			finalSortedList.addAll(this.reorderResearchTaskForMainAnalyst(consoliationList, researchList));
			finalSortedList.addAll(biResearchList);
			finalSortedList.addAll(vendorResearchList);
			finalSortedList.addAll(this.removeDupliateConsolidation(consoliationList, researchList));
			finalSortedList.addAll(this.reArrangeReviewOrder(reviewList, researchList, consoliationList));
			finalSortedList.addAll(otherList);
			finalSortedList.addAll(clientSubmissionList);
			finalSortedList.addAll(invoicingList);
			return finalSortedList;
		}
	}

	private List reArrangeReviewOrder(List reviewList, List researchList, List consoliationList) {
		this.logger.debug("In CaseSBMUtil::reArrangeReviewOrder");
		this.logger.debug("reviewList.size() " + reviewList.size());
		ArrayList review1List;
		if (reviewList.size() > 1) {
			review1List = new ArrayList();
			List<MyTaskPageVO> listCompleted = new ArrayList();
			List<MyTaskPageVO> listNotStarted = new ArrayList();
			List<MyTaskPageVO> listActive = new ArrayList();
			Iterator iterator = reviewList.iterator();

			while (iterator.hasNext()) {
				MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
				this.logger.debug("myTaskPageVO.getWorkStepStatus() " + myTaskPageVO.getWorkStepStatus());
				if (myTaskPageVO.getWorkStepStatus().equals(ACTIVE_STATUS)) {
					listActive.add(myTaskPageVO);
				} else if (myTaskPageVO.getWorkStepStatus().equals(COMPLETED_STATUS)) {
					listCompleted.add(myTaskPageVO);
				} else if (myTaskPageVO.getWorkStepStatus().equals(NOT_STARTED_STATUS)) {
					listNotStarted.add(myTaskPageVO);
				}
			}

			this.logger.debug("listNotStarted .size( " + listNotStarted.size());
			this.logger.debug("listCompleted .size( " + listCompleted.size());
			this.logger.debug("listActive  .size( " + listActive.size());
			boolean needToChangeStatus = false;
			if (listActive.size() == 0 && listNotStarted.size() == 0
					&& (this.checkActiveTaskForReviewOrConsolidation(researchList)
							|| this.checkActiveTaskForReviewOrConsolidation(consoliationList))) {
				needToChangeStatus = true;
			}

			this.logger.debug(" needToChangeStatus flag + " + needToChangeStatus);
			if (needToChangeStatus) {
				Iterator iterator = listCompleted.iterator();

				while (iterator.hasNext()) {
					MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
					myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
				}
			}

			review1List.addAll(listCompleted);
			review1List.addAll(listActive);
			review1List.addAll(listNotStarted);
			int i = 1;
			Iterator iterator = review1List.iterator();

			while (iterator.hasNext()) {
				MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
				myTaskPageVO.setTaskName("Reviewer " + i++);
			}

			return review1List;
		} else if (reviewList.size() == 1) {
			review1List = new ArrayList();
			this.logger.debug("In reviewList.size()==1");
			boolean needToChangeStats = false;
			MyTaskPageVO myTaskPageVO = (MyTaskPageVO) reviewList.get(0);
			if (!myTaskPageVO.getWorkStepStatus().equalsIgnoreCase(COMPLETED_STATUS)) {
				return reviewList;
			} else {
				if (this.checkActiveTaskForReviewOrConsolidation(researchList)
						|| this.checkActiveTaskForReviewOrConsolidation(consoliationList)) {
					needToChangeStats = true;
				}

				this.logger.debug("needToChangeStats " + needToChangeStats);
				if (needToChangeStats) {
					myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
					review1List.add(0, myTaskPageVO);
					this.logger.debug("out reviewList.size( ::)==1");
					return review1List;
				} else {
					this.logger.debug("out reviewList.size()==1");
					return reviewList;
				}
			}
		} else {
			return reviewList;
		}
	}

	private boolean checkActiveTaskForReviewOrConsolidation(List<MyTaskPageVO> inpuLists) {
		boolean isActive = false;
		Iterator iterator = inpuLists.iterator();

		while (iterator.hasNext()) {
			MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
			if (myTaskPageVO.getWorkStepStatus().equals(ACTIVE_STATUS)) {
				isActive = true;
				break;
			}
		}

		return isActive;
	}

	private List reorderResearchTaskForMainAnalyst(List consolidationList, List researchList) {
		this.logger.debug("In CaseSBMUtil :: reorderResearchTaskForMainAnalyst");
		String mainAnalystName = "";
		if (researchList.size() > 1 && null != consolidationList && consolidationList.size() > 0) {
			MyTaskPageVO myTaskPageVO = (MyTaskPageVO) consolidationList.get(0);
			mainAnalystName = myTaskPageVO.getPerformer();
			this.logger.debug("Main Analyst is " + mainAnalystName);
			if (null != mainAnalystName && !mainAnalystName.isEmpty()) {
				for (int cnt = 0; cnt < researchList.size(); ++cnt) {
					MyTaskPageVO taskVoObject = (MyTaskPageVO) researchList.get(cnt);
					if (taskVoObject.getPerformer().equalsIgnoreCase(mainAnalystName)) {
						researchList.remove(cnt);
						researchList.add(0, taskVoObject);
						break;
					}
				}
			}
		}

		this.logger.debug("Exit CaseSBMUtil :: reorderResearchTaskForMainAnalyst");
		return researchList;
	}

	private List removeDupliateConsolidation(List consoliationList, List researchList) {
		this.logger.debug("In CaseSBMUtil::removeDupliateConsolidation");
		this.logger.debug("consoliationList.size() " + consoliationList.size());
		if (consoliationList.size() <= 1) {
			this.logger.debug("Exiting CaseSBMUtil::removeDupliateConsolidation");
			return consoliationList;
		} else {
			Iterator<MyTaskPageVO> listIterator = consoliationList.iterator();
			List<MyTaskPageVO> list = new ArrayList();
			boolean isConsolidationActive = false;
			boolean isConsolidationNotStarted = false;
			boolean isConsolidationCompleted = false;
			HashMap<String, MyTaskPageVO> resultMap = new HashMap();
			Iterator iterator = consoliationList.iterator();

			while (iterator.hasNext()) {
				MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
				if (myTaskPageVO.getWorkStepStatus().equals(ACTIVE_STATUS)) {
					isConsolidationActive = true;
					resultMap.put(ACTIVE_STATUS, myTaskPageVO);
				} else if (myTaskPageVO.getWorkStepStatus().equals(COMPLETED_STATUS)) {
					isConsolidationCompleted = true;
					resultMap.put(COMPLETED_STATUS, myTaskPageVO);
				} else if (myTaskPageVO.getWorkStepStatus().equals(NOT_STARTED_STATUS)) {
					isConsolidationNotStarted = true;
					resultMap.put(NOT_STARTED_STATUS, myTaskPageVO);
				}
			}

			this.logger.debug("isConsolidationActive " + isConsolidationActive);
			this.logger.debug("isConsolidationNotStarted " + isConsolidationNotStarted);
			this.logger.debug("isConsolidationCompleted " + isConsolidationCompleted);
			if (isConsolidationActive) {
				list.add(resultMap.get(ACTIVE_STATUS));
			} else if (isConsolidationCompleted) {
				if (isConsolidationNotStarted && this.checkActiveTaskForReviewOrConsolidation(researchList)) {
					this.logger.debug("Adding NOT_STARTED_STATUS consolidation");
					list.add(resultMap.get(NOT_STARTED_STATUS));
				} else {
					this.logger.debug("Else Adding COMPLETED_STATUS consolidation");
					list.add(resultMap.get(COMPLETED_STATUS));
				}
			}

			this.logger.debug("list.size() " + list.size());
			this.logger.debug("Exiting CaseSBMUtil::removeDupliateConsolidation");
			return list;
		}
	}

	private MyTaskPageVO getCompletedWorkStepForInvoicing() {
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		if (!this.activeWorkStepMap.containsKey("Invoicing Task")) {
			myTaskPageVO.setTaskName("Invoicing Task");
			myTaskPageVO.setPerformer("FinanceQueue");
			myTaskPageVO.setUserFullName("FinanceQueue");
			myTaskPageVO.setCurrentCycle("Final");
			myTaskPageVO.setWorkStepStatus("Completed");
			myTaskPageVO.setTeamName("Primary");
		}

		return myTaskPageVO;
	}

	private MyTaskPageVO getCompletedWorkStepCCC(MyTaskPageVO taskPageVO) {
		this.logger.debug("In completed case creation " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Complete Case Creation");
		if (customDSMap != null) {
			myTaskPageVO.setPerformer((String) customDSMap.get("CaseCreator"));
		}

		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setWorkStepStatus("Completed");
		this.taskAdded.put("Complete Case Creation", "");
		return myTaskPageVO;
	}

	private MyTaskPageVO getCompletedWorkStepForOA(MyTaskPageVO taskPageVO) {
		this.logger.debug("In getCompletedWorkStepForOA " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Office Assignment Task");
		if (customDSMap != null) {
			myTaskPageVO.setPerformer((String) customDSMap.get("CaseManager"));
		}

		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus("Completed");
		this.taskAdded.put("Office Assignment Task", "");
		return myTaskPageVO;
	}

	private MyTaskPageVO getIncomingWorkStepForOA(MyTaskPageVO taskPageVO) {
		this.logger.debug("getIncomingWorkStepForOA " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Office Assignment Task");
		if (customDSMap != null) {
			myTaskPageVO.setPerformer("");
		}

		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		return myTaskPageVO;
	}

	private MyTaskPageVO getIncomingWorkStepForInvoicing(MyTaskPageVO taskPageVO) {
		this.logger.debug("getIncomingWorkStepForInvoicing ");
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Invoicing Task");
		myTaskPageVO.setPerformer("FinanceQueue");
		myTaskPageVO.setUserFullName("FinanceQueue");
		myTaskPageVO.setCurrentCycle("Final");
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		myTaskPageVO.setTeamName("Primary");
		return myTaskPageVO;
	}

	private MyTaskPageVO getIncomingWorkStepForTA(MyTaskPageVO taskPageVO) {
		this.logger.debug("getIncomingWorkStepForTA " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Team Assignment Task");
		myTaskPageVO.setPerformer("");
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		return myTaskPageVO;
	}

	private MyTaskPageVO getCompletedWorkStepForClientSub(MyTaskPageVO taskPageVO, String cycleName) {
		this.logger.debug("getCompletedWorkStepForClientSub " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Client Submission Task");
		myTaskPageVO.setPerformer((String) customDSMap.get("CaseManager"));
		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList("Primary");
		myTaskPageVO.setCurrentCycle(cycleName);
		myTaskPageVO.setWorkStepStatus("Completed");
		this.taskAdded.put("Client Submission Task#" + cycleName, "");
		return myTaskPageVO;
	}

	private List<MyTaskPageVO> getCompletedConsolidationWorkStep(MyTaskPageVO taskPageVO, String currentCycle,
			Boolean isReview) {
		this.logger.debug("I am in complted consolidation task");
		this.logger.debug("Team name " + taskPageVO.getTeamName());
		Map dsMap = taskPageVO.getCustmDSMap();
		this.logger.debug("dsMap " + dsMap);
		List<MyTaskPageVO> consolidationTaskList = new ArrayList();
		Map customDSMap = null;
		if (dsMap != null) {
			customDSMap = (Map) dsMap.get("customDSMap");
		}

		this.logger.debug("customDSMap " + customDSMap);
		if (customDSMap != null && !customDSMap.isEmpty()) {
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
			if (cycleTeamMapping != null) {
				HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
				CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(currentCycle);
				if (cycleInfo != null) {
					HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
					this.logger.debug("team type list***********" + taskPageVO.getTeamTypeList());
					this.logger.debug("teamInfoMap" + teamInfoMap);
					if (teamInfoMap != null) {
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();
						String teamTypeList = "";
						TeamAnalystMapping analystMapping;
						String teamName;
						Map analistTaskMap;
						MyTaskPageVO myTaskPageVO;
						if (!isReview) {
							teamTypeList = taskPageVO.getTeamTypeList();
							this.logger
									.debug("Completed consolidation task for review for teamtypelist " + teamTypeList);
							analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
							if (analystMapping != null) {
								teamName = analystMapping.getTeamName();
								this.logger.debug("Team Name " + teamName);
								this.logger.debug("Main analyst " + analystMapping.getMainAnalyst());
								analistTaskMap = analystMapping.getAnalystTaskStatus();
								if (teamName.contains("Primary")
										|| teamName.contains("Internal") && analistTaskMap.size() > 1) {
									myTaskPageVO = new MyTaskPageVO();
									myTaskPageVO.setTaskName("Consolidation Task");
									myTaskPageVO.setPerformer(analystMapping.getMainAnalyst());
									myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
									myTaskPageVO.setTeamTypeList(teamTypeList);
									myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
									myTaskPageVO.setWorkStepStatus("Completed");
									consolidationTaskList.add(myTaskPageVO);
								}
							}
						} else {
							label49 : while (true) {
								do {
									do {
										if (!teamItr.hasNext()) {
											break label49;
										}

										teamTypeList = (String) teamItr.next();
										analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
									} while (analystMapping == null);

									teamName = analystMapping.getTeamName();
									analistTaskMap = analystMapping.getAnalystTaskStatus();
								} while (!teamName.contains("Primary")
										&& (!teamName.contains("Internal") || analistTaskMap.size() <= 1));

								myTaskPageVO = new MyTaskPageVO();
								myTaskPageVO.setTaskName("Consolidation Task");
								myTaskPageVO.setPerformer(analystMapping.getMainAnalyst());
								myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
								myTaskPageVO.setTeamTypeList(teamTypeList);
								myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
								myTaskPageVO.setWorkStepStatus("Completed");
								consolidationTaskList.add(myTaskPageVO);
							}
						}
					}
				}
			}
		}

		this.taskAdded.put("Consolidation Task#" + currentCycle, "");
		this.logger.debug("consolidationTaskList size" + consolidationTaskList.size());
		return consolidationTaskList;
	}

	private List<MyTaskPageVO> getCompletedConsolidationWorkStepForSupporting(MyTaskPageVO taskPageVO,
			String currentCycle) throws CMSException {
		this.logger.debug("I am in complted consolidation task for supporting");
		this.logger.debug("Team name " + taskPageVO.getTeamName());
		Map dsMap = taskPageVO.getCustmDSMap();
		this.logger.debug("dsMap " + dsMap);
		List<MyTaskPageVO> consolidationTaskList = new ArrayList();
		Map customDSMap = null;
		if (dsMap != null) {
			customDSMap = (Map) dsMap.get("customDSMap");
		}

		this.logger.debug("customDSMap " + customDSMap);
		if (customDSMap != null && !customDSMap.isEmpty()) {
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
			if (cycleTeamMapping != null) {
				HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
				CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(currentCycle);
				if (cycleInfo != null) {
					HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
					this.logger.debug("team type list***********" + taskPageVO.getTeamTypeList());
					this.logger.debug("teamInfoMap" + teamInfoMap);
					if (teamInfoMap != null) {
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();
						String teamTypeList = "";

						label41 : while (true) {
							TeamAnalystMapping analystMapping;
							String teamName;
							Map analistTaskMap;
							long researchProcessPId;
							List activateList;
							do {
								do {
									if (!teamItr.hasNext()) {
										break label41;
									}

									teamTypeList = (String) teamItr.next();
									analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
								} while (analystMapping == null);

								teamName = analystMapping.getTeamName();
								analistTaskMap = analystMapping.getAnalystTaskStatus();
								researchProcessPId = analystMapping.getResearchProcessPID();
								String[] activateWsName = new String[0];
								if (!ResourceLocator.self().getSBMService().isTaskCompleted(researchProcessPId,
										this.session)) {
									activateWsName = ResourceLocator.self().getSBMService()
											.getActivatedWSNames(this.session, researchProcessPId);
								}

								activateList = Arrays.asList(activateWsName);
							} while (!ResourceLocator.self().getSBMService().isTaskCompleted(researchProcessPId,
									this.session) && !activateList.contains("Review"));

							if (teamName.contains("Internal") && analistTaskMap.size() > 1) {
								MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
								myTaskPageVO.setTaskName("Consolidation Task");
								myTaskPageVO.setPerformer(analystMapping.getMainAnalyst());
								myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
								myTaskPageVO.setTeamTypeList(teamTypeList);
								myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
								myTaskPageVO.setWorkStepStatus("Completed");
								consolidationTaskList.add(myTaskPageVO);
							}
						}
					}
				}
			}
		}

		this.logger.debug("consolidationTaskList size" + consolidationTaskList.size());
		return consolidationTaskList;
	}

	private MyTaskPageVO getIncomingWorkStepForResearch(MyTaskPageVO taskPageVO, String performer) {
		this.logger.debug("getIncomingWorkStepForResearch " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Research Task");
		if (performer != null) {
			myTaskPageVO.setPerformer(performer);
		}

		myTaskPageVO.setTeamName(taskPageVO.getTeamName());
		myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		return myTaskPageVO;
	}

	private MyTaskPageVO getIncomingWorkStepForBIResearch(MyTaskPageVO taskPageVO, String performer) {
		this.logger.debug("getIncomingWorkStepForResearch " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName(taskPageVO.getTaskName());
		if (performer != null) {
			myTaskPageVO.setPerformer(performer);
		}

		myTaskPageVO.setTeamName(taskPageVO.getTeamName());
		myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		return myTaskPageVO;
	}

	private MyTaskPageVO getIncomingWorkStepForConsolidationForPrimary(MyTaskPageVO taskPageVO) {
		this.logger.debug("getIncomingWorkStepForResearch " + taskPageVO.getTeamTypeList());
		this.logger.debug("In incoming consolidation wokstep for primary");
		Map dsMap = taskPageVO.getCustmDSMap();
		Map customDSMap = null;
		String performer = "";
		if (dsMap != null) {
			customDSMap = (Map) dsMap.get("customDSMap");
			this.logger.debug("customDSMap " + customDSMap);
			if (customDSMap != null && !customDSMap.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(taskPageVO.getCurrentCycle());
					if (cycleInfo != null) {
						HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
						this.logger.debug("team type list***********" + taskPageVO.getTeamTypeList());
						this.logger.debug("teamInfoMap" + teamInfoMap);
						if (teamInfoMap != null && taskPageVO.getTeamTypeList() != null
								&& null != teamInfoMap.get(taskPageVO.getTeamTypeList())) {
							TeamAnalystMapping analystMapping = (TeamAnalystMapping) teamInfoMap
									.get(taskPageVO.getTeamTypeList());
							performer = analystMapping.getMainAnalyst();
						}
					}
				}
			}
		}

		this.logger.debug("customDSMap in consolidation" + customDSMap);
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Consolidation Task");
		myTaskPageVO.setPerformer(performer);
		this.logger.debug("TeamName " + taskPageVO.getTeamName() + " Current Cycle " + taskPageVO.getCurrentCycle());
		myTaskPageVO.setTeamName(taskPageVO.getTeamName());
		myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		return myTaskPageVO;
	}

	private List<MyTaskPageVO> getIncomingWorkStepForConsolidation(MyTaskPageVO taskPageVO, String currentCycle)
			throws CMSException {
		this.logger.debug("In incoming consolidation wokstep");
		Map dsMap = taskPageVO.getCustmDSMap();
		this.logger.debug("dsMap " + dsMap);
		List<MyTaskPageVO> consolidationTaskList = new ArrayList();
		Map customDSMap = null;
		boolean isConsolidationAvail = true;
		if (dsMap != null) {
			customDSMap = (Map) dsMap.get("customDSMap");
		}

		this.logger.debug("customDSMap " + customDSMap);
		if (customDSMap != null && !customDSMap.isEmpty()) {
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
			this.logger.debug("cycleTeamMapping " + cycleTeamMapping);
			if (cycleTeamMapping != null) {
				HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
				CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(currentCycle);
				if (cycleInfo != null) {
					HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
					this.logger.debug("team type list***********" + taskPageVO.getTeamTypeList());
					this.logger.debug("teamInfoMap" + teamInfoMap);
					if (teamInfoMap != null) {
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();
						String teamTypeList = "";

						label53 : while (true) {
							TeamAnalystMapping analystMapping;
							String teamName;
							Map analistTaskMap;
							boolean isConsolidationIncoming;
							do {
								do {
									if (!teamItr.hasNext()) {
										break label53;
									}

									teamTypeList = (String) teamItr.next();
									analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
								} while (analystMapping == null);

								teamName = analystMapping.getTeamName();
								analistTaskMap = analystMapping.getAnalystTaskStatus();
								isConsolidationIncoming = false;
								if (teamName.contains("Internal")) {
									long researchProcessPid = analystMapping.getResearchProcessPID();
									String[] activatedWSNames = new String[0];
									if (!ResourceLocator.self().getSBMService().isTaskCompleted(researchProcessPid,
											this.session)) {
										activatedWSNames = ResourceLocator.self().getSBMService()
												.getActivatedWSNames(this.session, researchProcessPid);
										List<String> activatedWSList = Arrays.asList(activatedWSNames);
										if (!activatedWSList.contains("Consolidation Task")
												&& !activatedWSList.contains("Review")) {
											isConsolidationIncoming = true;
										}
									}
								}
							} while (!teamName.contains("Primary") && (!teamName.contains("Internal")
									|| analistTaskMap.size() <= 1 || !isConsolidationIncoming));

							MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
							myTaskPageVO.setTaskName("Consolidation Task");
							myTaskPageVO.setPerformer(analystMapping.getMainAnalyst());
							myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
							myTaskPageVO.setTeamTypeList(teamName);
							myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
							myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
							consolidationTaskList.add(myTaskPageVO);
						}
					}
				}
			}
		}

		this.logger.debug("consolidationTaskList " + consolidationTaskList);
		if (!consolidationTaskList.isEmpty()) {
			this.taskAdded.put("FutureConsolidation#" + currentCycle, "");
		}

		this.logger.debug("Exiting incoming consolidation wokstep");
		return consolidationTaskList;
	}

	private List<MyTaskPageVO> getIncomingWorkStepForReview(MyTaskPageVO taskPageVO, String currentCycle,
			boolean isAvailabe) throws CMSException {
		this.logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< currentCycle :=" + currentCycle);
		Map dsMap = taskPageVO.getCustmDSMap();
		List<MyTaskPageVO> reviewTaskList = new ArrayList();
		if (isAvailabe) {
			Map customDSMap = null;
			if (dsMap != null) {
				customDSMap = (Map) dsMap.get("customDSMap");
			}

			String crn = "";
			crn = (String) dsMap.get("CRN");
			if (customDSMap != null && !customDSMap.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(currentCycle);
					if (cycleInfo != null) {
						HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
						if (teamInfoMap != null && !teamInfoMap.isEmpty()) {
							String teamTypeList = "";
							TeamAnalystMapping analystMapping = null;
							Set teamNameSet = teamInfoMap.keySet();
							Iterator teamItr = teamNameSet.iterator();

							label138 : while (true) {
								String performer;
								String activeReviewer;
								LinkedHashMap reviewers;
								do {
									do {
										long reviewPID;
										do {
											while (true) {
												do {
													if (!teamItr.hasNext()) {
														break label138;
													}

													teamTypeList = (String) teamItr.next();
													analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
												} while (analystMapping == null);

												reviewPID = analystMapping.getReviewProcessPID();
												boolean isReviewIncoming = false;
												long researchProcessPid;
												if (teamTypeList.contains("Primary")) {
													researchProcessPid = ResourceLocator.self()
															.getOfficeAssignmentService().getPIDForCRN(crn);
													String caseCurrentCycle = "";
													caseCurrentCycle = cycleTeamMapping.getCurrentCycle();
													String[] activatedWSNames = new String[0];
													if (!ResourceLocator.self().getSBMService()
															.isTaskCompleted(researchProcessPid, this.session)) {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(this.session, researchProcessPid);
														List<String> activatedWSList = Arrays.asList(activatedWSNames);

														for (Iterator iterator = activatedWSList.iterator(); iterator
																.hasNext(); performer = (String) iterator.next()) {
															;
														}

														if (caseCurrentCycle.equalsIgnoreCase(currentCycle)) {
															if (!activatedWSList.contains("Client Submission Task")
																	&& !activatedWSList.contains("FinalReview")
																	&& !activatedWSList.contains("And 2")) {
																isReviewIncoming = true;
															}
														} else {
															isReviewIncoming = true;
														}
													}
												} else {
													researchProcessPid = analystMapping.getResearchProcessPID();
													String[] activatedWSNames = new String[0];
													if (!ResourceLocator.self().getSBMService()
															.isTaskCompleted(researchProcessPid, this.session)) {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(this.session, researchProcessPid);

														for (int i = 0; i < activatedWSNames.length; ++i) {
															if (activatedWSNames[i]
																	.equalsIgnoreCase("Consolidation Task")
																	|| activatedWSNames[i]
																			.equalsIgnoreCase("Research Task")) {
																isReviewIncoming = true;
																break;
															}
														}
													}
												}

												if (reviewPID != 0L && !isReviewIncoming) {
													break;
												}

												Map reviewers = analystMapping.getReviewers();
												if (reviewers != null && !reviewers.isEmpty()) {
													Set userKeySet = reviewers.keySet();
													Iterator userItr = userKeySet.iterator();
													boolean var35 = true;

													while (userItr.hasNext()) {
														String performer = (String) userItr.next();
														MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
														myTaskPageVO.setTaskName("Review Task");
														myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
														myTaskPageVO.setPerformer(performer);
														myTaskPageVO.setTeamName(taskPageVO.getTeamName());
														myTaskPageVO.setTeamTypeList(teamTypeList);
														reviewTaskList.add(myTaskPageVO);
													}
												}
											}
										} while (ResourceLocator.self().getSBMService().isTaskCompleted(reviewPID,
												this.session));

										activeReviewer = (String) ResourceLocator.self().getSBMService()
												.getDataslotValue(reviewPID, "Reviewer", this.session);
										reviewers = analystMapping.getReviewers();
									} while (reviewers == null);
								} while (reviewers.isEmpty());

								boolean isFirst = false;
								Set userKeySet = reviewers.keySet();
								Iterator userItr = userKeySet.iterator();
								boolean var40 = true;

								while (userItr.hasNext()) {
									performer = (String) userItr.next();
									this.logger.debug(performer);
									if (performer.equals(activeReviewer)) {
										isFirst = true;
									}

									if (!performer.equals(activeReviewer) && isFirst) {
										MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
										myTaskPageVO.setTaskName("Review Task");
										myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
										myTaskPageVO.setPerformer(performer);
										myTaskPageVO.setTeamName(taskPageVO.getTeamName());
										myTaskPageVO.setTeamTypeList(teamTypeList);
										reviewTaskList.add(myTaskPageVO);
									}
								}
							}
						}
					}

					this.taskAdded.put("FutureReview#" + currentCycle, "");
				}
			}
		} else if (reviewTaskList.isEmpty()) {
			MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
			myTaskPageVO.setTaskName("Review Task");
			myTaskPageVO.setPerformer("");
			myTaskPageVO.setTeamName(taskPageVO.getTeamName());
			myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
			myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
			myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
			reviewTaskList.add(myTaskPageVO);
		}

		this.logger.debug("reviewTaskList " + reviewTaskList);
		return reviewTaskList;
	}

	private MyTaskPageVO getIncomingWorkStepForClientSub(MyTaskPageVO taskPageVO, String currentCycle) {
		this.logger.debug("getIncomingWorkStepForClientSub " + taskPageVO.getTeamTypeList());
		Map customDSMap = taskPageVO.getCustmDSMap();
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		myTaskPageVO.setTaskName("Client Submission Task");
		if (customDSMap != null) {
			myTaskPageVO.setPerformer((String) customDSMap.get("CaseManager"));
		} else {
			myTaskPageVO.setPerformer("");
		}

		myTaskPageVO.setTeamName("Primary");
		myTaskPageVO.setTeamTypeList(taskPageVO.getTeamTypeList());
		myTaskPageVO.setCurrentCycle(taskPageVO.getCurrentCycle());
		myTaskPageVO.setWorkStepStatus(NOT_STARTED_STATUS);
		this.taskAdded.put(FUTURE_CLIENT_SUB + "#" + currentCycle, "");
		return myTaskPageVO;
	}

	private List<MyTaskPageVO> getCompletedWorkStepForTA(MyTaskPageVO taskPageVO) {
		this.logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		List completedTeamTask = new ArrayList();
		Map dsMap = taskPageVO.getCustmDSMap();
		String cycleName = "";
		if (taskPageVO.getCinterim1() != null) {
			cycleName = "Interim1";
		} else {
			cycleName = "Final";
		}

		Map customDSMap = null;
		if (dsMap != null) {
			customDSMap = (Map) dsMap.get("customDSMap");
		}

		if (customDSMap != null && !customDSMap.isEmpty()) {
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
			this.logger.debug("cycleTeamMapping is :-" + cycleTeamMapping);
			if (cycleTeamMapping != null) {
				HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
				this.logger.debug("cycleInfoMap is :-" + cycleInfoMap);
				this.logger.debug("cycleName:- " + cycleName);
				this.logger.debug("cycleInfoMap:- " + cycleInfoMap);
				CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
				if (cycleInfo != null) {
					HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
					this.logger.debug("teamName.split(#)[0]***********" + taskPageVO.getTeamTypeList());
					this.logger.debug("teamInfoMap" + teamInfoMap);
					if (teamInfoMap != null) {
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();
						String teamTypeList = "";

						label46 : while (true) {
							TeamAnalystMapping analystMapping;
							String teamName;
							do {
								do {
									do {
										if (!teamItr.hasNext()) {
											break label46;
										}

										teamTypeList = (String) teamItr.next();
										analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
									} while (analystMapping == null);

									teamName = analystMapping.getTeamName();
								} while (!analystMapping.isTeamAssignmentDone());
							} while (!teamName.contains("Primary") && !teamName.contains("Internal"));

							MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
							myTaskPageVO.setTaskName("Team Assignment Task");
							myTaskPageVO.setWorkStepStatus("Completed");
							myTaskPageVO.setCurrentCycle(cycleName);
							myTaskPageVO.setPerformer(analystMapping.getResearchHead());
							myTaskPageVO.setTeamName(analystMapping.getTeamName().split("#")[0]);
							myTaskPageVO.setTeamTypeList(teamTypeList);
							completedTeamTask.add(myTaskPageVO);
						}
					}

					this.logger.debug("completedTeamTask " + completedTeamTask.size());
				}
			}
		}

		this.taskAdded.put("Team Assignment Task", "");
		return completedTeamTask;
	}

	private List<MyTaskPageVO> getCompletedWorkStepForResearch(MyTaskPageVO taskPageVO, String cycleName) {
		this.logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Map dsMap = taskPageVO.getCustmDSMap();
		List researchTaskList = new ArrayList();
		Map customDSMap = null;
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
						String teamTypeList = "";
						TeamAnalystMapping analystMapping = null;
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();

						label41 : while (true) {
							Map analystStatus;
							Iterator userItr;
							do {
								do {
									if (!teamItr.hasNext()) {
										break label41;
									}

									teamTypeList = (String) teamItr.next();
									analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
								} while (analystMapping == null);

								analystStatus = analystMapping.getAnalystTaskStatus();
								Set userKeySet = analystStatus.keySet();
								userItr = userKeySet.iterator();
							} while (!analystMapping.getTeamName().contains("Primary")
									&& !analystMapping.getTeamName().contains("Internal"));

							while (userItr.hasNext()) {
								String performer = (String) userItr.next();
								AnalystTaskStatus analystTaskStatus = (AnalystTaskStatus) analystStatus.get(performer);
								if (analystTaskStatus.getStatus().equals("Done")) {
									MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
									myTaskPageVO.setTaskName("Research Task");
									myTaskPageVO.setWorkStepStatus("Completed");
									myTaskPageVO.setPerformer(performer);
									myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
									myTaskPageVO.setTeamTypeList(teamTypeList);
									researchTaskList.add(myTaskPageVO);
								}
							}
						}
					}
				}
			}
		}

		this.taskAdded.put("Research Task#" + cycleName, "");
		this.logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return researchTaskList;
	}

	private List<MyTaskPageVO> getCompletedWorkStepForBIResearch(MyTaskPageVO taskPageVO, String cycleName) {
		this.logger.debug("I am in bi/vendor research Task for cycleName:-" + cycleName);
		Map dsMap = taskPageVO.getCustmDSMap();
		boolean biExist = false;
		List researchTaskList = new ArrayList();
		Map customDSMap = null;
		if (dsMap != null) {
			customDSMap = (Map) dsMap.get("customDSMap");
		}

		this.logger.debug("customDSMap" + customDSMap);
		if (customDSMap != null && !customDSMap.isEmpty()) {
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
			if (cycleTeamMapping != null) {
				HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
				this.logger.debug("cycleInfoMap" + cycleInfoMap);
				CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
				if (cycleInfo != null) {
					HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
					this.logger.debug("teamInfoMap" + teamInfoMap);
					if (teamInfoMap != null) {
						String teamTypeList = "";
						TeamAnalystMapping analystMapping = null;
						this.logger.debug("TeamTypeList " + taskPageVO.getTeamTypeList());
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();

						label51 : while (true) {
							do {
								do {
									if (!teamItr.hasNext()) {
										break label51;
									}

									teamTypeList = (String) teamItr.next();
								} while (!teamTypeList.contains("BI") && !teamTypeList.contains("Vendor"));

								analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
							} while (analystMapping == null);

							Map analystStatus = analystMapping.getAnalystTaskStatus();
							Set userKeySet = analystStatus.keySet();
							Iterator userItr = userKeySet.iterator();

							while (userItr.hasNext()) {
								String performer = (String) userItr.next();
								AnalystTaskStatus analystTaskStatus = (AnalystTaskStatus) analystStatus.get(performer);
								if (analystTaskStatus.getStatus().equals("Done")) {
									MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
									if (analystMapping.getTaskName() != null) {
										myTaskPageVO.setTaskName(analystMapping.getTaskName());
									} else if (teamTypeList.contains("BI")) {
										myTaskPageVO.setTaskName("BI Research Task");
									} else {
										myTaskPageVO.setTaskName("Vendor Research Task");
									}

									myTaskPageVO.setWorkStepStatus("Completed");
									myTaskPageVO.setPerformer(performer);
									myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
									myTaskPageVO.setTeamTypeList(teamTypeList);
									researchTaskList.add(myTaskPageVO);
								}
							}
						}
					}
				}
			}
		}

		this.taskAdded.put("BI Research Task#" + cycleName, "");
		this.logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return researchTaskList;
	}

	private List<MyTaskPageVO> getCompletedReviewWorkStep(MyTaskPageVO taskPageVO, String cycleName)
			throws CMSException {
		this.logger.debug("<<<<<<<<<<<<<< I am in completed review task<<<<<<<<<<<<<<<<<<<<<<<<<");
		Map dsMap = taskPageVO.getCustmDSMap();
		List reviewTaskList = new ArrayList();
		Map customDSMap = null;
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
						String teamTypeList = "";
						TeamAnalystMapping analystMapping = null;
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();
						this.logger.debug("teamNameSet " + teamNameSet);

						label56 : while (true) {
							Vector reviewers;
							String reviewerName;
							LinkedHashMap allReviewers;
							do {
								do {
									long reviewPID;
									do {
										do {
											if (!teamItr.hasNext()) {
												break label56;
											}

											teamTypeList = (String) teamItr.next();
											analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
										} while (analystMapping == null);

										reviewPID = analystMapping.getReviewProcessPID();
									} while (reviewPID == 0L);

									reviewers = new Vector();
									reviewerName = "";
									if (!ResourceLocator.self().getSBMService().isTaskCompleted(reviewPID,
											this.session)) {
										reviewers = (Vector) ResourceLocator.self().getSBMService()
												.getDataslotValue(reviewPID, "Reviewers", this.session);
										reviewerName = (String) ResourceLocator.self().getSBMService()
												.getDataslotValue(reviewPID, "Reviewer", this.session);
									}

									allReviewers = analystMapping.getReviewers();
								} while (allReviewers == null);
							} while (allReviewers.isEmpty());

							Set userKeySet = allReviewers.keySet();
							Iterator userItr = userKeySet.iterator();
							boolean var21 = true;

							while (userItr.hasNext()) {
								String performer = (String) userItr.next();
								if (!reviewers.contains(performer) && !performer.equalsIgnoreCase(reviewerName)) {
									MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
									myTaskPageVO.setTaskName("Review Task");
									myTaskPageVO.setWorkStepStatus("Completed");
									myTaskPageVO.setPerformer(performer);
									myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
									myTaskPageVO.setTeamTypeList(teamTypeList);
									reviewTaskList.add(myTaskPageVO);
								}
							}
						}
					}
				}
			}
		}

		this.taskAdded.put("Review Task#" + cycleName, "");
		return reviewTaskList;
	}

	private List<MyTaskPageVO> getCompletedReviewWorkStepForSupporting(MyTaskPageVO taskPageVO, String cycleName)
			throws CMSException {
		this.logger.debug("<<<<<<<<<<<<<< I am in completed review task for supporting<<<<<<<<<<<<<<<<<<<<<<<<<");
		Map dsMap = taskPageVO.getCustmDSMap();
		List reviewTaskList = new ArrayList();
		Map customDSMap = null;
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
						String teamTypeList = "";
						TeamAnalystMapping analystMapping = null;
						Set teamNameSet = teamInfoMap.keySet();
						Iterator teamItr = teamNameSet.iterator();
						this.logger.debug("teamNameSet " + teamNameSet);

						label87 : while (true) {
							String reviewerName;
							LinkedHashMap allReviewers;
							Vector reviewers;
							do {
								do {
									long reviewPID;
									boolean isReviewCompleted;
									do {
										do {
											do {
												do {
													if (!teamItr.hasNext()) {
														break label87;
													}

													teamTypeList = (String) teamItr.next();
													analystMapping = (TeamAnalystMapping) teamInfoMap.get(teamTypeList);
												} while (analystMapping == null);
											} while (!teamTypeList.contains("Supporting"));

											reviewPID = analystMapping.getReviewProcessPID();
											isReviewCompleted = true;
											long researchProcessPid = analystMapping.getResearchProcessPID();
											String[] activatedWSNames = new String[0];
											if (!ResourceLocator.self().getSBMService()
													.isTaskCompleted(researchProcessPid, this.session)) {
												activatedWSNames = ResourceLocator.self().getSBMService()
														.getActivatedWSNames(this.session, researchProcessPid);

												for (int i = 0; i < activatedWSNames.length; ++i) {
													if (activatedWSNames[i].equalsIgnoreCase("Consolidation Task")
															|| activatedWSNames[i].equalsIgnoreCase("Research Task")) {
														isReviewCompleted = false;
														break;
													}
												}
											}
										} while (reviewPID == 0L);
									} while (!isReviewCompleted);

									reviewers = new Vector();
									reviewerName = "";
									if (!ResourceLocator.self().getSBMService().isTaskCompleted(reviewPID,
											this.session)) {
										reviewers = (Vector) ResourceLocator.self().getSBMService()
												.getDataslotValue(reviewPID, "Reviewers", this.session);
										reviewerName = (String) ResourceLocator.self().getSBMService()
												.getDataslotValue(reviewPID, "Reviewer", this.session);
									}

									allReviewers = analystMapping.getReviewers();
								} while (allReviewers == null);
							} while (allReviewers.isEmpty());

							Set userKeySet = allReviewers.keySet();
							Iterator userItr = userKeySet.iterator();
							int var25 = 1;

							while (userItr.hasNext()) {
								String performer = (String) userItr.next();
								if (!reviewers.contains(performer) && !performer.equalsIgnoreCase(reviewerName)) {
									MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
									myTaskPageVO.setTaskName("Review Task" + var25++);
									myTaskPageVO.setWorkStepStatus("Completed");
									this.logger.debug("performer " + performer);
									myTaskPageVO.setPerformer(performer);
									myTaskPageVO.setTeamName(teamTypeList.split("#")[0]);
									myTaskPageVO.setTeamTypeList(teamTypeList);
									reviewTaskList.add(myTaskPageVO);
								}
							}
						}
					}
				}
			}
		}

		this.taskAdded.put("Review Task_supporting#" + cycleName, "");
		this.logger.debug("<<<<<<<<<<<<<<End<<<<<<<<<<<<<<<<<<<<<<<<<");
		return reviewTaskList;
	}

	protected List getWorkStepInstanceListForStatus(String templateName, String workstepName, String condition)
			throws Exception {
		ArrayList waitForList = new ArrayList();

		try {
			QSWorkStepInstance qwsi = this.qService.getWorkStepInstance();
			WorkStepInstanceFilter filt = new WorkStepInstanceFilter((String) null, templateName);
			if (condition != null && !condition.isEmpty()) {
				filt.setCondition(condition);
			}

			QSWorkStepInstanceRS qwsirs = qwsi.getList(filt);
			this.logger.debug(" qwsirs getWorlStepInstanceList>>>>>>>>>>>>>>  " + qwsirs.getSql());
			this.list = qwsirs.getList();

			for (int i = 0; i < this.list.size(); ++i) {
				this.logger.debug("List " + this.list);
				QSWorkStepInstanceData qsWorkStepInstance = (QSWorkStepInstanceData) this.list.get(i);
				waitForList.addAll(qsWorkStepInstance.getWorkItemList().getList());
			}
		} catch (Exception var10) {
			this.logger.error(var10);
		}

		return waitForList;
	}

	private List<MyTaskPageVO> getTaskStaus(List workItemList)
			throws RemoteException, UnsupportedEncodingException, CMSException {
		ArrayList activeTaskList = new ArrayList();

		try {
			MyTaskPageVO myTaskPageVO;
			for (Iterator iterator = workItemList.iterator(); iterator.hasNext(); activeTaskList.add(myTaskPageVO)) {
				WorkItem workItem = (WorkItem) iterator.next();
				myTaskPageVO = null;
				this.logger.debug("***************workItem Name :- " + workItem.getName() + "************************");
				ProcessInstance pInstance = ProcessInstance.get(this.session, workItem.getProcessInstanceID());
				Map<String, Object> dsMap = pInstance.getDataSlotValue();
				this.logger.debug("Task starts " + new Date(System.currentTimeMillis()));
				this.logger.debug("dsmap " + dsMap);
				myTaskPageVO = new MyTaskPageVO();
				myTaskPageVO.setTaskName(workItem.getName().split("::")[1]);
				myTaskPageVO.setPerformer(workItem.getPerformer());
				myTaskPageVO.setCinterim1((String) dsMap.get("CInterim1"));
				myTaskPageVO.setCinterim2((String) dsMap.get("CInterim2"));
				myTaskPageVO.setCfinal((String) dsMap.get("CFinal"));
				String caseStatus = (String) dsMap.get("CaseStatus");
				this.logger.debug("caseStstus is " + caseStatus);
				if (!caseStatus.equals("On Hold") && !caseStatus.equals("Cancelled")) {
					myTaskPageVO.setWorkStepStatus(ACTIVE_STATUS);
				} else {
					myTaskPageVO.setWorkStepStatus(ONHOLD_STATUS);
					this.logger.debug("Task is not active");
				}

				this.logger.debug("00000000000000000000000000000000000000000000000000000000000");
				if (!workItem.getName().contains("Complete Case Creation") && !workItem.getName().contains("Invoicing")
						&& !workItem.getName().contains("Office Assignment Task")
						&& !workItem.getName().contains("Client Submission Task")
						&& 0L != (Long) dsMap.get("parentPID")) {
					ProcessInstance pInstance1 = ProcessInstance.get(this.session, (Long) dsMap.get("parentPID"));
					Map<String, Object> dsMap1 = pInstance1.getDataSlotValue();
					myTaskPageVO.setCustmDSMap(dsMap1);
					if (workItem.getName().contains("TeamAssignment")) {
						this.logger.debug("String) dsMap.get(SavvionConstants.TEAM_CYCLE_NAME) "
								+ (String) dsMap.get("TeamCycleName"));
						myTaskPageVO.setCurrentCycle((String) dsMap.get("TeamCycleName"));
						myTaskPageVO.setTeamTypeList((String) dsMap.get("TeamTypeList"));
						myTaskPageVO.setTeamName(myTaskPageVO.getTeamTypeList().split("#")[0]);
					} else if (!workItem.getName().contains("BI Research Task")
							&& !workItem.getName().contains("Vendor Research Task")) {
						if (workItem.getName().contains("Research Task")) {
							myTaskPageVO.setCurrentCycle((String) dsMap.get("TeamCycleName"));
							myTaskPageVO.setTeamTypeList((String) dsMap.get("TeamTypeList"));
							myTaskPageVO.setTeamName(myTaskPageVO.getTeamTypeList().split("#")[0]);
						} else if (workItem.getName().contains("Consolidation Task")) {
							if (dsMap.get("TeamTypeList") == null) {
								myTaskPageVO.setTeamName("Primary");
								myTaskPageVO.setTeamTypeList("Primary");
							} else {
								myTaskPageVO.setTeamTypeList((String) dsMap.get("TeamTypeList"));
								myTaskPageVO.setTeamName(myTaskPageVO.getTeamTypeList().split("#")[0]);
							}

							myTaskPageVO.setCurrentCycle((String) dsMap.get("TeamCycleName"));
						} else if (workItem.getName().contains("Review")) {
							String teamTypeList = (String) dsMap.get("TeamTypeList");
							this.logger.debug("teamTypeList for case status " + teamTypeList);
							if (teamTypeList != null && !teamTypeList.contains("Primary")) {
								myTaskPageVO.setTeamTypeList(teamTypeList);
								myTaskPageVO.setTeamName(myTaskPageVO.getTeamTypeList().split("#")[0]);
								myTaskPageVO.setCurrentCycle((String) dsMap.get("TeamCycleName"));
							} else {
								myTaskPageVO.setTeamTypeList("Primary");
								myTaskPageVO.setTeamName("Primary");
								myTaskPageVO.setCurrentCycle((String) dsMap.get("ProcessCycle"));
							}
						} else {
							this.logger.debug("No match found");
						}
					} else {
						this.logger.debug("BIVendorResearch found");
						myTaskPageVO.setCurrentCycle((String) dsMap.get("BITeamCycleName"));
						myTaskPageVO.setTeamTypeList((String) dsMap.get("BIVendorTaskTypeList"));
						myTaskPageVO.setTeamName(myTaskPageVO.getTeamTypeList().split("#")[0]);
					}
				} else {
					myTaskPageVO.setCurrentCycle((String) dsMap.get("ProcessCycle"));
					myTaskPageVO.setTeamName("Primary");
					myTaskPageVO.setCustmDSMap(dsMap);
					myTaskPageVO.setTeamTypeList(this.getTeamTypeListForPrimary(myTaskPageVO));
				}

				this.activeWorkStepMap.put(workItem.getName().split("::")[1], myTaskPageVO.getCurrentCycle());
				this.activeCycleMap.put(myTaskPageVO.getCurrentCycle(), workItem.getName().split("::")[1]);
				this.logger.debug("+++++Task Name" + myTaskPageVO.getTaskName());
				this.logger.debug("+++++Team name " + myTaskPageVO.getTeamName());
				if (!myTaskPageVO.getTaskName().equalsIgnoreCase("Invoicing Task")
						&& myTaskPageVO.getTeamName().contains("Primary")) {
					this.isPrimaryTeamTaskAvailable = true;
				}
			}

			this.logger.debug("activeWorkStepMap:- " + this.activeWorkStepMap);
			this.logger.debug("activeCycleMap:- " + this.activeCycleMap);
			return activeTaskList;
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private String getTeamTypeListForPrimary(MyTaskPageVO taskPageVO) {
		Map dsMap = taskPageVO.getCustmDSMap();
		String cycleName = "";
		String teamTypeList = "";
		if (taskPageVO.getCinterim1() != null) {
			cycleName = "Interim1";
		} else {
			cycleName = "Final";
		}

		Map customDSMap = null;
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

						while (teamItr.hasNext()) {
							teamTypeList = (String) teamItr.next();
							if (teamTypeList.contains("Primary")) {
								break;
							}
						}
					}
				}
			}
		}

		this.logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>> teamTypeList >>>>>>>>>>>>>>>>>" + teamTypeList);
		return teamTypeList;
	}

	private boolean getAvailableResearchTask(Map<String, Object> dsMap, String cycleName, String performer,
			String team_type_List) throws CMSException {
		Map customDSMap = null;
		boolean isAvailable = true;

		try {
			if (dsMap != null) {
				customDSMap = (Map) dsMap.get("customDSMap");
			}

			this.logger.debug("************************** " + performer + "*********************************");
			this.logger.debug("customDSMap " + customDSMap);
			this.logger.debug("team_type_List" + team_type_List);
			if (customDSMap != null && !customDSMap.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				this.logger.debug("cycleTeamMapping is :-" + cycleTeamMapping);
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					this.logger.debug("cycleInfoMap is :-" + cycleInfoMap);
					this.logger.debug("cycleName:- " + cycleName);
					this.logger.debug("cycleInfoMap:- " + cycleInfoMap);
					CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
					if (cycleInfo != null) {
						HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
						this.logger.debug("teamInfoMap" + teamInfoMap);
						if (teamInfoMap != null) {
							TeamAnalystMapping analystMapping = (TeamAnalystMapping) teamInfoMap.get(team_type_List);
							if (analystMapping != null) {
								AnalystTaskStatus analystTaskStatus = (AnalystTaskStatus) analystMapping
										.getAnalystTaskStatus().get(performer);
								this.logger.debug("analystTaskStatus  " + analystTaskStatus);
								if (analystTaskStatus != null) {
									this.logger.debug("analystTaskStatus.getStatus()" + analystTaskStatus.getStatus());
									if (analystTaskStatus.getStatus().equals("Done")) {
										isAvailable = true;
									} else {
										isAvailable = false;
									}
								}
							}
						}
					}
				}
			}

			return isAvailable;
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private List<MyTaskPageVO> getWaitForConsolidationTask(String condition) throws CMSException {
		ArrayList waitForConsolidationTaskList = new ArrayList();

		try {
			List waitWorkstepList = this.getWorkStepInstanceList("CaseCreation", (String) null,
					"BLWSI.STATUS = 62 AND  BLWSI.WORKSTEP_NAME = 'WaitForConsolidation' AND " + condition,
					SBMQueryManager.getQueryService());
			if (waitWorkstepList != null && !waitWorkstepList.isEmpty()) {
				Iterator iterator = waitWorkstepList.iterator();

				while (iterator.hasNext()) {
					QSWorkStepInstanceData qsWorkStepInstanceData = (QSWorkStepInstanceData) iterator.next();
					this.logger.debug("workItem.getName() " + qsWorkStepInstanceData.getName());
					ProcessInstance pInstance = ProcessInstance.get(this.session,
							qsWorkStepInstanceData.getProcessInstanceID());
					Map<String, Object> dsMap = pInstance.getDataSlotValue();
					this.logger.debug("waitWorkstepMap " + dsMap);
					if (this.checkForWaitForCon(dsMap)) {
						waitForConsolidationTaskList.add(this.getWaitTaskInfoObject(qsWorkStepInstanceData.getName(),
								pInstance.getAppName(), this.session.getUser(), dsMap));
					}
				}
			}
		} catch (Exception var8) {
			new CMSException();
		}

		return waitForConsolidationTaskList;
	}

	protected List getWorkStepInstanceList(String templateName, String workstepName, String condition, QueryService qs)
			throws Exception {
		Object waitForList = new ArrayList();

		try {
			QSWorkStepInstance qwsi = qs.getWorkStepInstance();
			WorkStepInstanceFilter filt = new WorkStepInstanceFilter((String) null, templateName);
			this.logger.debug("condition " + condition);
			if (condition != null && !condition.isEmpty()) {
				filt.setCondition(condition);
			}

			QSWorkStepInstanceRS qwsirs = qwsi.getList(filt);
			this.logger.debug(" qwsirs getWorlStepInstanceList>>>>>>>>>>>>>>  " + qwsirs.getSql());
			waitForList = qwsirs.getList();
			this.logger.debug(" list size getWorlStepInstanceList *******  " + ((List) waitForList).size());
		} catch (Exception var9) {
			this.logger.error(var9);
		}

		return (List) waitForList;
	}

	private boolean checkForWaitForCon(Map dsMap) throws CMSException {
		String cycleName = (String) dsMap.get("ProcessCycle");
		Map customDSMap = null;
		boolean consolidationFlag = false;

		try {
			if (dsMap != null) {
				customDSMap = (Map) dsMap.get("customDSMap");
			}

			this.logger.debug("customDSMap " + customDSMap);
			if (customDSMap != null && !customDSMap.isEmpty()) {
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				this.logger.debug("cycleTeamMapping is :-" + cycleTeamMapping);
				if (cycleTeamMapping != null) {
					HashMap<String, CycleInfo> cycleInfoMap = (HashMap) cycleTeamMapping.getCycleInformation();
					this.logger.debug("cycleInfoMap is :-" + cycleInfoMap);
					this.logger.debug("cycleName:- " + cycleName);
					this.logger.debug("cycleInfoMap:- " + cycleInfoMap);
					CycleInfo cycleInfo = (CycleInfo) cycleInfoMap.get(cycleName);
					if (cycleInfo != null) {
						HashMap teamInfoMap = (HashMap) cycleInfo.getTeamInfo();
						this.logger.debug("teamInfoMap" + teamInfoMap);
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
							this.logger.debug("analystMapping:- " + analystMapping);
							consolidationFlag = analystMapping.getResearchTaskStatus().equals("Done");
							this.logger.debug("consolidationFlag:- " + consolidationFlag);
						}
					}
				}
			}

			return consolidationFlag;
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private MyTaskPageVO getWaitTaskInfoObject(String taskName, String appName, String performer, Map dsMap)
			throws UnsupportedEncodingException {
		MyTaskPageVO myTaskPageVO = new MyTaskPageVO();
		this.logger.debug("wait WorkItem Name:- " + taskName);
		myTaskPageVO.setAppName(appName);
		myTaskPageVO.setEncodedTask("");
		myTaskPageVO.setPerformer(performer);
		myTaskPageVO.setCustmDSMap(dsMap);
		myTaskPageVO.setCfinal((String) dsMap.get("CFinal"));
		myTaskPageVO.setCinterim1((String) dsMap.get("CInterim1"));
		myTaskPageVO.setCinterim2((String) dsMap.get("CInterim2"));
		myTaskPageVO.setCrn((String) dsMap.get("CRN"));
		myTaskPageVO.setCurrentCycle((String) dsMap.get("ProcessCycle"));
		myTaskPageVO.setStatus("In Progress");
		myTaskPageVO.setTeamName("Primary");
		this.logger.debug("Cycle in wait process " + myTaskPageVO.getCurrentCycle());
		myTaskPageVO.setTaskName("Awaiting Consolidation");
		return myTaskPageVO;
	}
}