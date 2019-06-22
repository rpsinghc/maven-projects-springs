package com.worldcheck.atlas.bl.flowcontroller;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.flowcontroller.ibatis.FlowLockInfoVO;
import com.worldcheck.atlas.bl.flowcontroller.ibatis.FlowMutexImpl;
import com.worldcheck.atlas.bl.integration.AtlasJunoIntegration;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.AnalystTaskStatus;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.ReviewStatusMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.utils.DeltaCalculator;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

public class FlowController {
	private static final String DATE_FORMAT_ddMMyy = "dd-MM-yy";
	private static final String DATE_FORMAT_ddMMMyyyy = "dd-MMM-yyyy";
	private static final String NULL = "null";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.flowcontroller.FlowController");

	public void updateDS(Session session, CaseDetails caseDetails, String module) throws CMSException {
		long pid = this.getProcessInstanceId(caseDetails.getWorkitemName());
		String currentCycle;
		String teamTInterim1;
		HashMap dsValues;
		String rInterim1;
		String teamTInterim2;
		if (module.equals("Office")) {
			if (pid == 0L) {
				pid = Long.parseLong(caseDetails.getPid());
			}

			List<TeamDetails> teamList = caseDetails.getTeamList();
			List<String> applicableCyclesList = new ArrayList();
			Map<String, TeamAnalystMapping> interim1TeamInfo = null;
			Map<String, TeamAnalystMapping> interim2TeamInfo = null;
			Map<String, TeamAnalystMapping> finalTeamInfo = new HashMap();
			currentCycle = null;
			String primaryRInterim2 = null;
			teamTInterim1 = null;
			Iterator var15 = teamList.iterator();

			while (var15.hasNext()) {
				TeamDetails team = (TeamDetails) var15.next();
				TeamAnalystMapping teamAnalystMapping = this.initializeMappingForOfficeTask(team);
				if (caseDetails.getIsAutoOfcAssign() == 1) {
					teamAnalystMapping.setResearchHead(team.getManagerName());
				}

				boolean isBypassInterim = team.getByPassInterim().equals("1");
				if (!isBypassInterim) {
					this.logger.debug("due date for int 1 is :" + team.getDueDate1() + " for team " + team.getTeamType()
							+ "#" + team.getTeamId());
					if (team.getDueDate1() != null && team.getDueDate1().length() != 0) {
						if (team.getTeamType().contains("Primary") && team.getDueDate1() != null
								&& !team.getDueDate1().equals("")) {
							currentCycle = team.getDueDate1();
						}

						interim1TeamInfo = this.addTeamForInterimCycle(teamAnalystMapping, "Interim1",
								team.getDueDate1(), interim1TeamInfo, applicableCyclesList);
					}

					this.logger.debug("due date for int 2 is :" + team.getDueDate2() + " for team " + team.getTeamType()
							+ "#" + team.getTeamId());
					if (team.getDueDate2() != null && team.getDueDate2().length() != 0) {
						if (team.getTeamType().contains("Primary") && team.getDueDate2() != null
								&& !team.getDueDate2().equals("")) {
							primaryRInterim2 = team.getDueDate2();
						}

						interim2TeamInfo = this.addTeamForInterimCycle(teamAnalystMapping, "Interim2",
								team.getDueDate2(), interim2TeamInfo, applicableCyclesList);
					}
				}

				teamAnalystMapping.setClientDueDate(team.getFinalDueDate());
				if (!applicableCyclesList.contains("Final")) {
					applicableCyclesList.add("Final");
				}

				finalTeamInfo.put(teamAnalystMapping.getTeamName(), teamAnalystMapping);
				if (team.getTeamType().contains("Primary") && team.getFinalDueDate() != null
						&& !team.getFinalDueDate().equals("")) {
					teamTInterim1 = team.getFinalDueDate();
				}
			}

			this.logger.debug("interim1TeamInfo is " + interim1TeamInfo);
			this.logger.debug("interim2TeamInfo is " + interim2TeamInfo);
			this.logger.debug("finalTeamInfo is " + finalTeamInfo);
			teamTInterim2 = null;
			Map<String, CycleInfo> cycleInfoMap = new HashMap();
			if (interim1TeamInfo != null) {
				teamTInterim2 = this.addTeamInfoForCycle(interim1TeamInfo, cycleInfoMap, "Interim1", teamTInterim2);
			}

			if (interim2TeamInfo != null) {
				teamTInterim2 = this.addTeamInfoForCycle(interim2TeamInfo, cycleInfoMap, "Interim2", teamTInterim2);
			}

			if (finalTeamInfo != null) {
				teamTInterim2 = this.addTeamInfoForCycle(finalTeamInfo, cycleInfoMap, "Final", teamTInterim2);
			}

			CycleTeamMapping cycleTeamMapping = this.populateCycleTeamMap(teamTInterim2, applicableCyclesList,
					cycleInfoMap);
			SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MM-yy");
			dsValues = new HashMap();

			try {
				if (currentCycle != null && !currentCycle.equals("")) {
					dsValues.put("RInterim1", targetSdf.format(sourceSdf.parse(currentCycle)));
					currentCycle = targetSdf.format(sourceSdf.parse(currentCycle));
				}

				if (primaryRInterim2 != null && !primaryRInterim2.equals("")) {
					dsValues.put("RInterim2", targetSdf.format(sourceSdf.parse(primaryRInterim2)));
					primaryRInterim2 = targetSdf.format(sourceSdf.parse(primaryRInterim2));
				}

				if (teamTInterim1 != null && !teamTInterim1.equals("")) {
					dsValues.put("RFinal", targetSdf.format(sourceSdf.parse(teamTInterim1)));
					teamTInterim1 = targetSdf.format(sourceSdf.parse(teamTInterim1));
				}
			} catch (ParseException var31) {
				throw new CMSException(this.logger, var31);
			}

			String rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RInterim1",
					session);
			String rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RInterim2",
					session);
			rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal", session);
			if (rInterim1 != null && !rInterim1.equals(currentCycle)
					|| rInterim2 != null && !rInterim2.equals(primaryRInterim2)
					|| rInterim1 != null && !rInterim1.equals(teamTInterim1)) {
				ResourceLocator.self().getCaseDetailService().updateCaseResearchDueDates(caseDetails.getCrn(),
						currentCycle, primaryRInterim2, teamTInterim1, caseDetails.getUpdatedBy());
				ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
			}

			ResourceLocator.self().getSBMService().updateDSForOfficeTask(pid, cycleTeamMapping, session);
		} else if (module.equals("Team")) {
			String teamName = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "TeamTypeList",
					session);
			long parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID", session);
			HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
					.getDataslotValue(parentPID, "customDSMap", session);
			CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
			currentCycle = cycleTeamMapping.getCurrentCycle();
			TeamDetails teamDetails = (TeamDetails) caseDetails.getTeamList().get(0);
			if (teamDetails.getTeamType().contains("Primary")) {
				cycleTeamMapping.setPrimaryTeamMainAnalyst(teamDetails.getMainAnalyst());
				cycleTeamMapping.setPrimaryTeamMembers(teamDetails.getAnalyst());
			}

			teamTInterim1 = null;
			teamTInterim2 = null;
			String teamTFinal = null;
			String primaryTeamRInterim1 = null;
			String primaryTeamRInterim2 = null;
			String primaryTeamRFinal = null;
			dsValues = new HashMap();
			SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MM-yy");
			if (teamDetails.getDueDate1() != null && teamDetails.getDueDate1().length() != 0) {
				teamTInterim1 = teamDetails.getDueDate1();
				if (teamDetails.getTeamType().contains("Primary")) {
					primaryTeamRInterim1 = teamDetails.getDueDate1();
				}
			}

			if (teamDetails.getDueDate2() != null && teamDetails.getDueDate2().length() != 0) {
				teamTInterim2 = teamDetails.getDueDate2();
				if (teamDetails.getTeamType().contains("Primary")) {
					primaryTeamRInterim2 = teamDetails.getDueDate2();
				}
			}

			if (teamDetails.getFinalDueDate() != null && teamDetails.getFinalDueDate().length() != 0) {
				teamTFinal = teamDetails.getFinalDueDate();
				if (teamDetails.getTeamType().contains("Primary")) {
					primaryTeamRFinal = teamDetails.getFinalDueDate();
				}
			}

			try {
				if (teamTInterim1 != null) {
					dsValues.put("tInterim1", targetSdf.format(sourceSdf.parse(teamTInterim1)));
					teamTInterim1 = targetSdf.format(sourceSdf.parse(teamTInterim1));
				}

				if (teamTInterim2 != null) {
					dsValues.put("tInterim2", targetSdf.format(sourceSdf.parse(teamTInterim2)));
					teamTInterim2 = targetSdf.format(sourceSdf.parse(teamTInterim2));
				}

				if (teamTFinal != null) {
					dsValues.put("tFinal", targetSdf.format(sourceSdf.parse(teamTFinal)));
					teamTFinal = targetSdf.format(sourceSdf.parse(teamTFinal));
				}

				if (primaryTeamRInterim1 != null) {
					dsValues.put("RInterim1", targetSdf.format(sourceSdf.parse(primaryTeamRInterim1)));
					primaryTeamRInterim1 = targetSdf.format(sourceSdf.parse(primaryTeamRInterim1));
				}

				if (primaryTeamRInterim2 != null) {
					dsValues.put("RInterim2", targetSdf.format(sourceSdf.parse(primaryTeamRInterim2)));
					primaryTeamRInterim2 = targetSdf.format(sourceSdf.parse(primaryTeamRInterim2));
				}

				if (primaryTeamRFinal != null) {
					dsValues.put("RFinal", targetSdf.format(sourceSdf.parse(primaryTeamRFinal)));
					primaryTeamRFinal = targetSdf.format(sourceSdf.parse(primaryTeamRFinal));
				}
			} catch (ParseException var30) {
				throw new CMSException(this.logger, var30);
			}

			rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RInterim1", session);
			String rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RInterim2",
					session);
			String rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal", session);
			String tInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "tInterim1",
					session);
			String tInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "tInterim2",
					session);
			String tFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "tFinal", session);
			boolean ResearchDatesUpdated = false;
			this.logger.debug("before updateResearchDueDateDataslots::");
			if (teamDetails.getTeamType().contains("Primary")
					&& (rInterim1 != null && !rInterim1.equals(primaryTeamRInterim1)
							|| rInterim2 != null && !rInterim2.equals(primaryTeamRInterim2)
							|| rFinal != null && !rFinal.equals(primaryTeamRFinal))) {
				this.updateResearchDueDateDataslots(parentPID, session, caseDetails,
						cycleTeamMapping.getCycleInformation());
				ResearchDatesUpdated = true;
			}

			this.logger.debug("ResearchDatesUpdated flag for Team Assignment::" + ResearchDatesUpdated);
			boolean teamDueDatesUpdated = false;
			if (tInterim1 != null && !tInterim1.equals(teamTInterim1)
					|| tInterim2 != null && !tInterim2.equals(teamTInterim2)
					|| tFinal != null && !tFinal.equals(teamTFinal)) {
				ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
				teamDueDatesUpdated = true;
			}

			this.logger.debug("teamDueDatesUpdated flag for Team Assignment::" + teamDueDatesUpdated);
			if (!currentCycle.equals("Interim2") && !currentCycle.equals("Final")) {
				this.populateTeamMappingForCycle(teamName, cycleTeamMapping, teamDetails, "Interim1");
			}

			if (!currentCycle.equals("Final")) {
				this.populateTeamMappingForCycle(teamName, cycleTeamMapping, teamDetails, "Interim2");
			}

			this.populateTeamMappingForCycle(teamName, cycleTeamMapping, teamDetails, "Final");
			ResourceLocator.self().getSBMService().updateDSForTeamTask(pid, cycleTeamMapping, session);
		} else if (module.equals("AddSubject")) {
			caseDetails.setUpdatedBy(((SubjectDetails) caseDetails.getSubjectList().get(0)).getUpdatedBy());
			HashMap<String, CaseDetails> caseDetailsMap = ResourceLocator.self().getOfficeAssignmentService()
					.addOfficeDetailsForNewSubject(caseDetails);
			CaseDetails oldCaseDetails = (CaseDetails) caseDetailsMap.get("oldCaseDetails");
			CaseDetails newCaseDetails = (CaseDetails) caseDetailsMap.get("newCaseDetails");
			newCaseDetails.setSubjectList(caseDetails.getSubjectList());
			newCaseDetails.setUpdatedBy(caseDetails.getUpdatedBy());
			if (((SubjectDetails) caseDetails.getSubjectList().get(0)).isPrimarySub()) {
				newCaseDetails.setPrimarySubjectFlag(true);
				newCaseDetails
						.setPrimarySubjectName(((SubjectDetails) caseDetails.getSubjectList().get(0)).getSubjectName());
				newCaseDetails.setPrimarySubjectCountryName(
						((SubjectDetails) caseDetails.getSubjectList().get(0)).getCountryName());
			}

			this.updateDSAndPushFlow(session, oldCaseDetails, newCaseDetails, module);
		}

	}

	public List<CaseDetails> bulkUpdateDSAndCompleteTask(Session session, List<CaseDetails> caseDetailsList,
			String module) throws CMSException {
		String workstepName = null;
		Iterator var6 = caseDetailsList.iterator();

		while (var6.hasNext()) {
			CaseDetails caseDetails = (CaseDetails) var6.next();

			try {
				long pid = this.getProcessInstanceId(caseDetails.getWorkitemName());
				String newClientFinalDate;
				String primaryRInterim1;
				String primaryRInterim2;
				String primaryRFinal;
				String rFinal;
				HashMap dsValues;
				if (module.equals("Office")) {
					if (pid == 0L) {
						pid = Long.parseLong(caseDetails.getPid());
					}

					workstepName = "Office Assignment Task";
					List<TeamDetails> teamList = caseDetails.getTeamList();
					String caseManagerId = caseDetails.getCaseMgrId();
					CaseDetails oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
							.getDetailForCase(caseDetails.getCrn());
					String oldCaseManagerId = oldCaseDetails.getCaseMgrId();
					String oldClientInterim1Date = "";
					String oldClientInterim2Date = "";
					String oldClientFinalDate = oldCaseDetails.getFinalDueDate().toString();
					this.logger.debug("oldClientFinalDate::" + oldClientFinalDate);
					if (oldCaseDetails.getcInterim1() != null && !oldCaseDetails.getcInterim1().equals("")) {
						oldClientInterim1Date = oldCaseDetails.getcInterim1().toString();
						this.logger.debug("oldClientInterim1Date::" + oldClientInterim1Date);
					}

					if (oldCaseDetails.getcInterim2() != null && !oldCaseDetails.getcInterim2().equals("")) {
						oldClientInterim2Date = oldCaseDetails.getcInterim2().toString();
						this.logger.debug("oldClientInterim2Date::" + oldClientInterim2Date);
					}

					String newClientInterim1Date = "";
					String newClientInterim2Date = "";
					newClientFinalDate = caseDetails.getFinalDueDate().toString();
					this.logger.debug("newClientFinalDate::" + newClientFinalDate);
					if (caseDetails.getcInterim1() != null && !caseDetails.getcInterim1().equals("")) {
						newClientInterim1Date = caseDetails.getcInterim1().toString();
						this.logger.debug("newClientInterim1Date::" + newClientInterim1Date);
					}

					if (caseDetails.getcInterim2() != null && !caseDetails.getcInterim2().equals("")) {
						newClientInterim2Date = caseDetails.getcInterim2().toString();
						this.logger.debug("newClientInterim2Date::" + newClientInterim2Date);
					}

					List<String> applicableCyclesList = new ArrayList();
					Map<String, TeamAnalystMapping> interim1TeamInfo = null;
					Map<String, TeamAnalystMapping> interim2TeamInfo = null;
					Map<String, TeamAnalystMapping> finalTeamInfo = new HashMap();
					primaryRInterim1 = null;
					primaryRInterim2 = null;
					primaryRFinal = null;
					Iterator var57 = teamList.iterator();

					while (var57.hasNext()) {
						TeamDetails team = (TeamDetails) var57.next();
						TeamAnalystMapping teamAnalystMapping = this.initializeMappingForOfficeTask(team);
						boolean isBypassInterim = team.getByPassInterim().equals("1");
						if (!isBypassInterim) {
							this.logger.debug("due date for int 1 is :" + team.getDueDate1() + " for team "
									+ team.getTeamType() + "#" + team.getTeamId());
							if (team.getDueDate1() != null && team.getDueDate1().length() != 0) {
								if (team.getTeamType().contains("Primary")) {
									primaryRInterim1 = team.getDueDate1();
								}

								interim1TeamInfo = this.addTeamForInterimCycle(teamAnalystMapping, "Interim1",
										team.getDueDate1(), interim1TeamInfo, applicableCyclesList);
							}

							this.logger.debug("due date for int 2 is :" + team.getDueDate2() + " for team "
									+ team.getTeamType() + "#" + team.getTeamId());
							if (team.getDueDate2() != null && team.getDueDate2().length() != 0) {
								if (team.getTeamType().contains("Primary")) {
									primaryRInterim2 = team.getDueDate2();
								}

								interim2TeamInfo = this.addTeamForInterimCycle(teamAnalystMapping, "Interim2",
										team.getDueDate2(), interim2TeamInfo, applicableCyclesList);
							}
						}

						teamAnalystMapping.setClientDueDate(team.getFinalDueDate());
						if (!applicableCyclesList.contains("Final")) {
							applicableCyclesList.add("Final");
						}

						finalTeamInfo.put(teamAnalystMapping.getTeamName(), teamAnalystMapping);
						if (team.getTeamType().contains("Primary")) {
							primaryRFinal = team.getFinalDueDate();
						}
					}

					this.logger.debug("interim1TeamInfo is " + interim1TeamInfo);
					this.logger.debug("interim2TeamInfo is " + interim2TeamInfo);
					this.logger.debug("finalTeamInfo is " + finalTeamInfo);
					rFinal = null;
					Map<String, CycleInfo> cycleInfoMap = new HashMap();
					if (interim1TeamInfo != null) {
						rFinal = this.addTeamInfoForCycle(interim1TeamInfo, cycleInfoMap, "Interim1", rFinal);
					}

					if (interim2TeamInfo != null) {
						rFinal = this.addTeamInfoForCycle(interim2TeamInfo, cycleInfoMap, "Interim2", rFinal);
					}

					if (finalTeamInfo != null) {
						rFinal = this.addTeamInfoForCycle(finalTeamInfo, cycleInfoMap, "Final", rFinal);
					}

					CycleTeamMapping cycleTeamMapping = this.populateCycleTeamMap(rFinal, applicableCyclesList,
							cycleInfoMap);
					SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
					SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MM-yy");
					SimpleDateFormat sourceSdformat = new SimpleDateFormat("yyyy-MM-dd");
					dsValues = new HashMap();

					try {
						if (!oldCaseManagerId.equals(caseManagerId)) {
							dsValues.put("CaseManager", caseManagerId);
						}

						if (primaryRInterim1 != null) {
							dsValues.put("RInterim1", targetSdf.format(sourceSdf.parse(primaryRInterim1)));
							primaryRInterim1 = targetSdf.format(sourceSdf.parse(primaryRInterim1));
						}

						if (primaryRInterim2 != null) {
							dsValues.put("RInterim2", targetSdf.format(sourceSdf.parse(primaryRInterim2)));
							primaryRInterim2 = targetSdf.format(sourceSdf.parse(primaryRInterim2));
						}

						if (primaryRFinal != null) {
							dsValues.put("RFinal", targetSdf.format(sourceSdf.parse(primaryRFinal)));
							primaryRFinal = targetSdf.format(sourceSdf.parse(primaryRFinal));
						}

						if (newClientInterim1Date != null && !newClientInterim1Date.equals("")) {
							dsValues.put("CInterim1", targetSdf.format(sourceSdformat.parse(newClientInterim1Date)));
							newClientInterim1Date = sourceSdf.format(sourceSdformat.parse(newClientInterim1Date));
						}

						this.logger.debug("newClientInterim2Date in condition::" + newClientInterim2Date);
						if (newClientInterim2Date != null && !newClientInterim2Date.equals("")) {
							dsValues.put("CInterim2", targetSdf.format(sourceSdformat.parse(newClientInterim2Date)));
							newClientInterim2Date = sourceSdf.format(sourceSdformat.parse(newClientInterim2Date));
						}

						this.logger.debug("newClientFinalDate in condition::" + newClientFinalDate);
						if (newClientFinalDate != null && !newClientFinalDate.equals("")) {
							dsValues.put("CFinal", targetSdf.format(sourceSdformat.parse(newClientFinalDate)));
							newClientFinalDate = sourceSdf.format(sourceSdformat.parse(newClientFinalDate));
						}
					} catch (ParseException var42) {
						throw new CMSException(this.logger, var42);
					}

					String rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"RInterim1", session);
					String rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"RInterim2", session);
					String rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal",
							session);
					boolean datesUpdated = false;
					boolean caseManagerUpdated = false;
					boolean clientDueDateUpdated = false;
					if (!oldCaseManagerId.equals(caseManagerId)) {
						ResourceLocator.self().getOfficeAssignmentService().updateCaseManager(caseDetails.getCrn(),
								caseManagerId, caseDetails.getUpdatedBy());
						caseManagerUpdated = true;
					}

					if (rInterim1 != null && !rInterim1.equals(primaryRInterim1)
							|| rInterim2 != null && !rInterim2.equals(primaryRInterim2)
							|| rFinal != null && !rFinal.equals(primaryRInterim2)) {
						ResourceLocator.self().getCaseDetailService().updateCaseResearchDueDates(caseDetails.getCrn(),
								primaryRInterim1, primaryRInterim2, primaryRFinal, caseDetails.getUpdatedBy());
						datesUpdated = true;
					}

					this.logger.debug("before custome update");
					if (!oldClientInterim1Date.equals(newClientInterim1Date)
							|| !oldClientInterim2Date.equals(newClientInterim2Date)
							|| !oldClientFinalDate.equals(newClientFinalDate)) {
						this.logger.debug("in custom update");
						this.logger.debug("newClientFinalDate before updation::" + newClientFinalDate);
						this.logger.debug("newClientInterim1Date before updation::" + newClientInterim1Date);
						this.logger.debug("newClientInterim2Date before updation::" + newClientInterim2Date);
						ResourceLocator.self().getOfficeAssignmentService().updateClientDueDates(caseDetails.getCrn(),
								newClientFinalDate, newClientInterim1Date, newClientInterim2Date);
						clientDueDateUpdated = true;
					}

					this.logger.debug("clientDueDateUpdatedflag::" + clientDueDateUpdated);
					if (datesUpdated || caseManagerUpdated || clientDueDateUpdated) {
						ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
					}

					CaseDetails newCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
							.getDetailForCase(caseDetails.getCrn());
					CaseHistory caseHistory = new CaseHistory();
					caseHistory.setPid(oldCaseDetails.getPid());
					caseHistory.setCRN(oldCaseDetails.getCrn());
					caseHistory.setPerformer(caseDetails.getCaseHistoryPerformer());
					caseHistory.setTaskName("Office Assignment Task");
					caseHistory.setTaskStatus("In Progress");
					caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
							.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
					newCaseDetails.setCaseHistoryPerformer(caseDetails.getCaseHistoryPerformer());
					ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
							caseHistory, "caseDetails");
					ResourceLocator.self().getSBMService().updateDSForOfficeTask(pid, cycleTeamMapping, session);
					HashMap<String, Object> taskPerformerDS = new HashMap();
					taskPerformerDS.put("taskPerformer", caseDetails.getCaseHistoryPerformer());
					ResourceLocator.self().getSBMService().completeTask(session, taskPerformerDS, pid, workstepName);
				} else if (module.equals("Team")) {
					workstepName = "Team Assignment Task";
					String teamName = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"TeamTypeList", session);
					long parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID",
							session);
					HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
							.getDataslotValue(parentPID, "customDSMap", session);
					CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
					TeamDetails teamDetails = (TeamDetails) caseDetails.getTeamList().get(0);
					HashMap<String, Object> dsValues = new HashMap();
					SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
					SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MM-yy");
					if (teamDetails.getTeamType().contains("Primary")) {
						cycleTeamMapping.setPrimaryTeamMainAnalyst(teamDetails.getMainAnalyst());
						cycleTeamMapping.setPrimaryTeamMembers(teamDetails.getAnalyst());
					}

					newClientFinalDate = null;
					String teamTInterim2 = null;
					String teamTFinal = null;
					String primaryTeamRInterim1 = null;
					String primaryTeamRInterim2 = null;
					primaryRInterim1 = null;
					if (teamDetails.getDueDate1() != null && teamDetails.getDueDate1().length() != 0) {
						newClientFinalDate = teamDetails.getDueDate1();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryTeamRInterim1 = teamDetails.getDueDate1();
						}
					}

					if (teamDetails.getDueDate2() != null && teamDetails.getDueDate2().length() != 0) {
						teamTInterim2 = teamDetails.getDueDate2();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryTeamRInterim2 = teamDetails.getDueDate2();
						}
					}

					if (teamDetails.getFinalDueDate() != null && teamDetails.getFinalDueDate().length() != 0) {
						teamTFinal = teamDetails.getFinalDueDate();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryRInterim1 = teamDetails.getFinalDueDate();
						}
					}

					if (teamDetails.getTeamType().contains("Primary")) {
						this.logger.debug("Setting case research due dates :: " + primaryTeamRInterim1 + "::"
								+ primaryTeamRInterim2 + "::" + primaryRInterim1);
						caseDetails.setResearchDueDates(
								primaryTeamRInterim1 + "::" + primaryTeamRInterim2 + "::" + primaryRInterim1);
					}

					if (newClientFinalDate != null) {
						dsValues.put("tInterim1", targetSdf.format(sourceSdf.parse(newClientFinalDate)));
						newClientFinalDate = targetSdf.format(sourceSdf.parse(newClientFinalDate));
					}

					if (teamTInterim2 != null) {
						dsValues.put("tInterim2", targetSdf.format(sourceSdf.parse(teamTInterim2)));
						teamTInterim2 = targetSdf.format(sourceSdf.parse(teamTInterim2));
					}

					if (teamTFinal != null) {
						dsValues.put("tFinal", targetSdf.format(sourceSdf.parse(teamTFinal)));
						teamTFinal = targetSdf.format(sourceSdf.parse(teamTFinal));
					}

					if (primaryTeamRInterim1 != null) {
						dsValues.put("RInterim1", targetSdf.format(sourceSdf.parse(primaryTeamRInterim1)));
						primaryTeamRInterim1 = targetSdf.format(sourceSdf.parse(primaryTeamRInterim1));
					}

					if (primaryTeamRInterim2 != null) {
						dsValues.put("RInterim2", targetSdf.format(sourceSdf.parse(primaryTeamRInterim2)));
						primaryTeamRInterim2 = targetSdf.format(sourceSdf.parse(primaryTeamRInterim2));
					}

					if (primaryRInterim1 != null) {
						dsValues.put("RFinal", targetSdf.format(sourceSdf.parse(primaryRInterim1)));
						primaryRInterim1 = targetSdf.format(sourceSdf.parse(primaryRInterim1));
					}

					primaryRInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"RInterim1", session);
					primaryRFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RInterim2",
							session);
					rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal", session);
					String tInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"tInterim1", session);
					String tInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"tInterim2", session);
					String tFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "tFinal",
							session);
					boolean ResearchDatesUpdated = false;
					this.logger.debug("before updateResearchDueDateDataslots::");
					if (teamDetails.getTeamType().contains("Primary")
							&& (primaryRInterim2 != null && !primaryRInterim2.equals(primaryTeamRInterim1)
									|| primaryRFinal != null && !primaryRFinal.equals(primaryTeamRInterim2)
									|| rFinal != null && !rFinal.equals(primaryRInterim1))) {
						this.logger.debug("teamDetails.getTeamType() :: " + teamDetails.getTeamType());
						this.updateResearchDueDateDataslots(parentPID, session, caseDetails,
								cycleTeamMapping.getCycleInformation());
						ResearchDatesUpdated = true;
					}

					this.logger.debug("ResearchDatesUpdated flag for Team Assignment::" + ResearchDatesUpdated);
					boolean teamDueDatesUpdated = false;
					if (tInterim1 != null && !tInterim1.equals(newClientFinalDate)
							|| tInterim2 != null && !tInterim2.equals(teamTInterim2)
							|| tFinal != null && !tFinal.equals(teamTFinal)) {
						ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
						teamDueDatesUpdated = true;
					}

					this.logger.debug("teamDueDatesUpdated flag for Team Assignment::" + teamDueDatesUpdated);
					this.populateTeamMappingForCycle(teamName, cycleTeamMapping, teamDetails, "Interim1");
					this.populateTeamMappingForCycle(teamName, cycleTeamMapping, teamDetails, "Interim2");
					this.populateTeamMappingForCycle(teamName, cycleTeamMapping, teamDetails, "Final");
					ResourceLocator.self().getSBMService().updateDSForTeamTask(pid, cycleTeamMapping, session);
					dsValues = new HashMap();
					dsValues.put("taskPerformer", caseDetails.getCaseHistoryPerformer());
					ResourceLocator.self().getSBMService().completeTask(session, dsValues, pid, workstepName);
				}
			} catch (Exception var43) {
				caseDetails.setTaskCompleted(false);
			}
		}

		return caseDetailsList;
	}

	private void populateTeamMappingForCycle(String teamName, CycleTeamMapping cycleTeamMapping,
			TeamDetails teamDetails, String cycleName) {
		this.logger.debug("cycle is " + cycleName);
		CycleInfo cycleInfo = (CycleInfo) cycleTeamMapping.getCycleInformation().get(cycleName);
		if (cycleInfo != null) {
			TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) cycleInfo.getTeamInfo().get(teamName);
			this.logger.debug("team name is " + teamName);
			this.logger.debug("teamAnalystMapping is " + teamAnalystMapping);
			if (teamAnalystMapping != null) {
				teamAnalystMapping.setTeamAssignmentDone(true);
				teamAnalystMapping.setMainAnalyst(teamDetails.getMainAnalyst());
				teamAnalystMapping.setTaskName("ResearchTask");
				if (cycleInfo.getCycleName().equals("Interim1")) {
					if (teamDetails.getDueDate1() != null && teamDetails.getDueDate1().length() != 0) {
						teamAnalystMapping.setClientDueDate(teamDetails.getDueDate1());
					}
				} else if (cycleInfo.getCycleName().equals("Interim2")) {
					if (teamDetails.getDueDate2() != null && teamDetails.getDueDate2().length() != 0) {
						teamAnalystMapping.setClientDueDate(teamDetails.getDueDate2());
					}
				} else if (cycleInfo.getCycleName().equals("Final") && teamDetails.getFinalDueDate() != null
						&& teamDetails.getFinalDueDate().length() != 0) {
					teamAnalystMapping.setClientDueDate(teamDetails.getFinalDueDate());
				}

				if (!teamDetails.getTeamType().contains("BI") && !teamDetails.getTeamType().contains("Vendor")) {
					HashMap<String, AnalystTaskStatus> analystTaskStatusMap = new HashMap();
					this.logger.debug("analysts list size is ::: " + teamDetails.getAnalyst());
					Iterator var9 = teamDetails.getAnalyst().iterator();

					while (var9.hasNext()) {
						String analyst = (String) var9.next();
						AnalystTaskStatus analystTaskStatus = new AnalystTaskStatus();
						analystTaskStatus.setStatus("New");
						analystTaskStatus.setResearchPID(0L);
						this.logger.debug("adding analyst : " + analyst + " to map");
						analystTaskStatusMap.put(analyst, analystTaskStatus);
					}

					this.logger.debug("analysts map size is ::: " + analystTaskStatusMap.size());
					teamAnalystMapping.setAnalystTaskStatus(analystTaskStatusMap);
					LinkedHashMap<String, ReviewStatusMapping> reviewers = new LinkedHashMap();
					this.logger.debug("reviewers list size is ::: " + teamDetails.getReviewer());
					Iterator var14 = teamDetails.getReviewer().iterator();

					while (var14.hasNext()) {
						String reviewer = (String) var14.next();
						ReviewStatusMapping reviewStatusMapping = new ReviewStatusMapping();
						reviewStatusMapping.setStatus("");
						reviewStatusMapping.setResearchPID(0L);
						this.logger.debug("adding reviewer : " + reviewer + " to map");
						reviewers.put(reviewer, reviewStatusMapping);
					}

					this.logger.debug("reviewers map size is ::: " + reviewers.size());
					teamAnalystMapping.setReviewers(reviewers);
				}
			}
		}

	}

	private String addTeamInfoForCycle(Map<String, TeamAnalystMapping> teamInfo, Map<String, CycleInfo> cycleInfoMap,
			String cycleName, String currentCycle) {
		CycleInfo cycleInfo = this.populateCycleInfo(teamInfo, cycleName);
		if (currentCycle == null) {
			currentCycle = cycleName;
		}

		cycleInfoMap.put(cycleName, cycleInfo);
		return currentCycle;
	}

	private Map<String, TeamAnalystMapping> addTeamForInterimCycle(TeamAnalystMapping finalTeamAnalystMapping,
			String interimCycle, String interimDueDate, Map<String, TeamAnalystMapping> interimTeamInfo,
			List<String> applicableCyclesList) {
		TeamAnalystMapping teamAnalystMapping = new TeamAnalystMapping(finalTeamAnalystMapping);
		HashMap<String, AnalystTaskStatus> analystTaskStatusMap = new HashMap();
		if (teamAnalystMapping.getTeamName().contains("BI") || teamAnalystMapping.getTeamName().contains("Vendor")) {
			AnalystTaskStatus analystTaskStatus = new AnalystTaskStatus();
			analystTaskStatus.setStatus("");
			analystTaskStatus.setResearchPID(0L);
			analystTaskStatusMap.put(teamAnalystMapping.getResearchHead(), analystTaskStatus);
		}

		teamAnalystMapping.setClientDueDate(interimDueDate);
		teamAnalystMapping.setAnalystTaskStatus(analystTaskStatusMap);
		if (interimTeamInfo == null) {
			interimTeamInfo = new HashMap();
			applicableCyclesList.add(interimCycle);
		}

		this.logger.debug("adding teamAnalystMapping " + teamAnalystMapping.getTeamName() + " to map for interim "
				+ interimCycle);
		((Map) interimTeamInfo).put(teamAnalystMapping.getTeamName(), teamAnalystMapping);
		this.logger.debug("team info size for " + interimCycle + " is " + ((Map) interimTeamInfo).size());
		return (Map) interimTeamInfo;
	}

	private CycleTeamMapping populateCycleTeamMap(String currentCycle, List<String> applicableCyclesList,
			Map<String, CycleInfo> cycleInfoMap) {
		CycleTeamMapping cycleTeamMap = new CycleTeamMapping();
		cycleTeamMap.setCurrentCycle(currentCycle);
		cycleTeamMap.setCycleApplicable((String[]) applicableCyclesList.toArray(new String[0]));
		cycleTeamMap.setCycleInformation(cycleInfoMap);
		return cycleTeamMap;
	}

	private CycleInfo populateCycleInfo(Map<String, TeamAnalystMapping> teamInfo, String cycleName) {
		CycleInfo cycleInfo = new CycleInfo();
		cycleInfo.setCycleName(cycleName);
		cycleInfo.setCycleStatus("In Progress");
		cycleInfo.setTeamInfo(teamInfo);
		return cycleInfo;
	}

	private TeamAnalystMapping initializeMappingForOfficeTask(TeamDetails team) {
		this.logger.debug("initializing TeamAnalystMapping for team " + team.getTeamType() + "#" + team.getTeamId());
		TeamAnalystMapping teamAnalystMapping = new TeamAnalystMapping();
		teamAnalystMapping.setBranchOffice(team.getOffice());
		teamAnalystMapping.setTeamName(team.getTeamType() + "#" + team.getTeamId());
		HashMap<String, AnalystTaskStatus> analystTaskStatusMap = new HashMap();
		if (!team.getTeamType().contains("BI") && !team.getTeamType().contains("Vendor")) {
			this.logger.debug("setting research head " + team.getResearchHead() + " for team " + team.getTeamType()
					+ "#" + team.getTeamId());
			teamAnalystMapping.setResearchHead(team.getResearchHead());
			teamAnalystMapping.setTeamAssignmentDone(false);
		} else {
			this.logger.debug("setting research head " + team.getManagerName() + " for team " + team.getTeamType() + "#"
					+ team.getTeamId());
			teamAnalystMapping.setResearchHead(team.getManagerName());
			AnalystTaskStatus analystTaskStatus = new AnalystTaskStatus();
			analystTaskStatus.setStatus("New");
			analystTaskStatus.setResearchPID(0L);
			analystTaskStatusMap.put(team.getManagerName(), analystTaskStatus);
			teamAnalystMapping.setTeamAssignmentDone(true);
		}

		teamAnalystMapping.setAnalystTaskStatus(analystTaskStatusMap);
		teamAnalystMapping.setResearchTaskStatus("");
		this.logger.debug("adding analystTaskStatus to map for team " + team.getTeamType() + "#" + team.getTeamId());
		return teamAnalystMapping;
	}

	private long getProcessInstanceId(String workitemName) {
		String[] temp = (String[]) null;
		long piid = 0L;
		if (workitemName != null && !"null".equalsIgnoreCase(workitemName) && !"".equalsIgnoreCase(workitemName)) {
			temp = workitemName.split("::");
			piid = Long.parseLong(temp[0].split("#")[1]);
		}

		return piid;
	}

	private String getWorkstepName(String workitemName) {
		String workstepName = null;
		workstepName = workitemName.split("::")[1];
		return workstepName;
	}

	public void updateDSAndPushFlow(Session session, CaseDetails oldCaseDetails, CaseDetails newCaseDetails,
			String module) throws CMSException {
		DeltaCalculator deltaCalculator = new DeltaCalculator();
		CaseDetails deltaCaseDetails = deltaCalculator.getDelta(oldCaseDetails, newCaseDetails, module);
		long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(newCaseDetails.getCrn());
		this.logger.debug("updateDSAndPushFlow called by module: " + module);
		this.logger.debug("CaseCreation PID is " + pid);
		this.logger.debug("deltaCaseDetails is " + deltaCaseDetails);
		boolean isOfficeAssignmentDone = this.isOfficeAssignmentDone(pid, session);
		this.logger.debug("isOfficeAssignmentDone is " + isOfficeAssignmentDone);
		if (deltaCaseDetails != null) {
			this.logger.debug("deltaCaseDetails.getCrn() " + deltaCaseDetails.getCrn());
			if (module.equals("UpdateSubject")) {
				if (deltaCaseDetails.isReFlag()) {
					newCaseDetails
							.setUpdatedBy(((SubjectDetails) newCaseDetails.getSubjectList().get(0)).getUpdatedBy());
					CaseDetails oldCaseDetailsOA = null;
					this.logger.debug("oldCaseDetails.getTeamList() " + oldCaseDetails.getTeamList());
					if (oldCaseDetails.getTeamList() != null && oldCaseDetails.getTeamList().size() > 0) {
						this.logger.debug("oldCaseDetails.getTeamList().size() " + oldCaseDetails.getTeamList().size());
						oldCaseDetailsOA = oldCaseDetails;
					}

					HashMap<String, CaseDetails> caseDetailsMap = ResourceLocator.self().getOfficeAssignmentService()
							.updateOfficeDetailsForOldSubject(newCaseDetails);
					if (oldCaseDetailsOA == null) {
						this.logger.debug("retrieving oldCaseDetails bean from office assignment service");
						oldCaseDetailsOA = (CaseDetails) caseDetailsMap.get("oldCaseDetails");
					}

					CaseDetails newCaseDetailsOA = (CaseDetails) caseDetailsMap.get("newCaseDetails");
					newCaseDetailsOA.setUpdatedBy(deltaCaseDetails.getUpdatedBy());
					if (deltaCaseDetails.isPrimarySubjectFlag()) {
						newCaseDetailsOA.setPrimarySubjectFlag(deltaCaseDetails.isPrimarySubjectFlag());
						newCaseDetailsOA.setPrimarySubjectName(deltaCaseDetails.getPrimarySubjectName());
						newCaseDetailsOA.setPrimarySubjectCountryName(deltaCaseDetails.getPrimarySubjectCountryName());
					}

					this.logger.debug("deltaCaseDetails.isSubjectFlag() :: " + deltaCaseDetails.isSubjectFlag());
					if (deltaCaseDetails.isSubjectFlag()) {
						List<SubTeamReMapVO> subjectAssignmentDetails = ResourceLocator.self()
								.getOfficeAssignmentService()
								.getSubjectAssignmentDetails((SubjectDetails) newCaseDetails.getSubjectList().get(0));
						newCaseDetailsOA.setSubTeamReMapForPullback(subjectAssignmentDetails);
						newCaseDetailsOA.setSubjectFlag(deltaCaseDetails.isSubjectFlag());
					}

					CaseDetails deltaCaseDetailsOA = deltaCalculator.getDelta(oldCaseDetailsOA, newCaseDetailsOA,
							"UpdateOfficeForSubject");
					this.logger.debug("deltaCaseDetailsOA is " + deltaCaseDetailsOA);
					if (deltaCaseDetailsOA != null) {
						this.logger.debug("deltaCaseDetailsOA.getCrn() " + deltaCaseDetailsOA.getCrn());
						this.doPullBack(pid, session, deltaCaseDetailsOA, (HashMap) null, module);
						deltaCaseDetails.setRemovedPids(deltaCaseDetailsOA.getRemovedPids());
					}
				} else if (deltaCaseDetails.isSubjectFlag()) {
					List<SubTeamReMapVO> subjectAssignmentDetails = ResourceLocator.self().getOfficeAssignmentService()
							.getSubjectAssignmentDetails((SubjectDetails) newCaseDetails.getSubjectList().get(0));
					deltaCaseDetails.setSubTeamReMapForPullback(subjectAssignmentDetails);
					this.doPullBack(pid, session, deltaCaseDetails, (HashMap) null, module);
				} else if (deltaCaseDetails.isPrimarySubjectFlag()) {
					this.doPullBack(pid, session, deltaCaseDetails, (HashMap) null, module);
				}
			} else if (module.equals("CaseInformation")) {
				if (deltaCaseDetails.getrInterim1() != null) {
					if (deltaCaseDetails.getrInterim2() != null) {
						ResourceLocator.self().getOfficeAssignmentService()
								.addInterimCycleToPrimaryTeam(deltaCaseDetails, "both");
					} else {
						ResourceLocator.self().getOfficeAssignmentService()
								.addInterimCycleToPrimaryTeam(deltaCaseDetails, "interim1");
					}
				} else if (deltaCaseDetails.getrInterim2() != null) {
					ResourceLocator.self().getOfficeAssignmentService().addInterimCycleToPrimaryTeam(deltaCaseDetails,
							"interim2");
				}

				this.doPullBack(pid, session, deltaCaseDetails, (HashMap) null, module);
			} else {
				this.doPullBack(pid, session, deltaCaseDetails, (HashMap) null, module);
			}

			this.sendJunoTriggers(module, deltaCaseDetails);
		}

	}

	public void updateCaseInformationDS(Session session, CaseDetails oldCaseDetails, CaseDetails newCaseDetails,
			HashMap<String, Object> dsValues) throws CMSException {
		DeltaCalculator deltaCalculator = new DeltaCalculator();
		CaseDetails deltaCaseDetails = deltaCalculator.getDelta(oldCaseDetails, newCaseDetails, "CaseInformation");
		long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(newCaseDetails.getCrn());
		this.logger.debug("inside updateCaseInformationDS...");
		this.logger.debug("CaseCreation PID is " + pid);
		this.logger.debug("deltaCaseDetails is " + deltaCaseDetails);
		boolean isOfficeAssignmentDone = this.isOfficeAssignmentDone(pid, session);
		this.logger.debug("isOfficeAssignmentDone is " + isOfficeAssignmentDone);
		if (deltaCaseDetails != null) {
			this.logger.debug("deltaCaseDetails.getCrn() " + deltaCaseDetails.getCrn());
			if (deltaCaseDetails.getrInterim1() != null) {
				if (deltaCaseDetails.getrInterim2() != null) {
					ResourceLocator.self().getOfficeAssignmentService().addInterimCycleToPrimaryTeam(deltaCaseDetails,
							"both");
				} else {
					ResourceLocator.self().getOfficeAssignmentService().addInterimCycleToPrimaryTeam(deltaCaseDetails,
							"interim1");
				}
			} else if (deltaCaseDetails.getrInterim2() != null) {
				ResourceLocator.self().getOfficeAssignmentService().addInterimCycleToPrimaryTeam(deltaCaseDetails,
						"interim2");
			}

			if (isOfficeAssignmentDone) {
				this.doPullBack(pid, session, deltaCaseDetails, dsValues, "CaseInformation");
			} else {
				this.updateCaseInformationDataslots(pid, session, deltaCaseDetails, dsValues);
			}

			this.sendJunoTriggers("CaseInformation", deltaCaseDetails);
		} else {
			this.updateCaseInformationDataslots(pid, session, newCaseDetails, dsValues);
		}

	}

	private void sendJunoTriggers(String module, CaseDetails deltaCaseDetails) {
		try {
			List addedInterimDueDates;
			if (module.equals("AddSubject")) {
				this.logger.debug("sendSubjectAddTrigger has been triggered... "
						+ ((SubjectDetails) deltaCaseDetails.getSubjectList().get(0)).getSubjectName());
				AtlasJunoIntegration.sendSubjectAddTrigger(deltaCaseDetails.getCrn(),
						(SubjectDetails) deltaCaseDetails.getSubjectList().get(0));
				if (deltaCaseDetails.isPrimarySubjectFlag()) {
					this.logger.debug("primarySubjectChangedTrigger has been triggered... "
							+ ((SubjectDetails) deltaCaseDetails.getSubjectList().get(0)).getSubjectName());
					AtlasJunoIntegration.primarySubjectChangedTrigger(deltaCaseDetails.getCrn(),
							(SubjectDetails) deltaCaseDetails.getSubjectList().get(0));
				}
			} else if (module.equals("UpdateSubject")) {
				if (deltaCaseDetails.isReFlag()) {
					addedInterimDueDates = deltaCaseDetails.getDeletedSubTeamReMappings();
					if (addedInterimDueDates.size() != 0) {
						this.logger.debug("sendREDeleteFromSubjectTrigger has been triggered... "
								+ ((SubjectDetails) deltaCaseDetails.getSubjectList().get(0)).getSubjectName());
						AtlasJunoIntegration.sendREDeleteFromSubjectTrigger(deltaCaseDetails.getCrn(),
								(SubjectDetails) deltaCaseDetails.getSubjectList().get(0),
								deltaCaseDetails.getDeletedSubTeamReMappings());
					}

					List<String> addedSubTeamReMappings = deltaCaseDetails.getAddedSubTeamReMappings();
					if (addedSubTeamReMappings.size() != 0) {
						this.logger.debug("sendREAddedToSubjectTrigger has been triggered... "
								+ ((SubjectDetails) deltaCaseDetails.getSubjectList().get(0)).getSubjectName());
						AtlasJunoIntegration.sendREAddedToSubjectTrigger(deltaCaseDetails.getCrn(),
								(SubjectDetails) deltaCaseDetails.getSubjectList().get(0),
								deltaCaseDetails.getAddedSubTeamReMappings());
					}
				}

				if (deltaCaseDetails.isPrimarySubjectFlag()) {
					this.logger.debug("primarySubjectChangedTrigger has been triggered... "
							+ ((SubjectDetails) deltaCaseDetails.getSubjectList().get(0)).getSubjectName());
					AtlasJunoIntegration.primarySubjectChangedTrigger(deltaCaseDetails.getCrn(),
							(SubjectDetails) deltaCaseDetails.getSubjectList().get(0));
				}
			} else if (module.equals("DeleteSubject")) {
				this.logger.debug("sendSubjectDeleteTrigger has been triggered... "
						+ ((SubjectDetails) deltaCaseDetails.getSubjectList().get(0)).getSubjectName());
				AtlasJunoIntegration.sendSubjectDeleteTrigger(deltaCaseDetails.getCrn(),
						(SubjectDetails) deltaCaseDetails.getSubjectList().get(0));
			}

			Iterator var5;
			String addedInterim;
			String addedInterimDueDate;
			if (deltaCaseDetails.isDueDateFlag()) {
				HashMap<String, TeamDetails> addedInterimsForTeamMap = deltaCaseDetails.getAddedInterimsForTeamMap();
				var5 = addedInterimsForTeamMap.keySet().iterator();

				while (var5.hasNext()) {
					addedInterimDueDate = (String) var5.next();
					addedInterim = addedInterimDueDate.split("::")[0];
					String interimCycle = addedInterimDueDate.split("::")[1];
					if (!deltaCaseDetails.getAddedTeamsMap().containsKey(addedInterim)) {
						String teamName = addedInterim.split("@")[0];
						this.logger.debug("sendProcessCycleAddedToTeamTrigger has been triggered... " + teamName + "::"
								+ interimCycle);
						AtlasJunoIntegration.sendProcessCycleAddedToTeamTrigger(deltaCaseDetails.getCrn(), teamName,
								interimCycle);
					}
				}
			} else if (deltaCaseDetails.isInterimCycleFlag()) {
				addedInterimDueDates = deltaCaseDetails.getAddedInterimDueDates();
				var5 = addedInterimDueDates.iterator();

				while (var5.hasNext()) {
					addedInterimDueDate = (String) var5.next();
					addedInterim = addedInterimDueDate.split("::")[0];
					this.logger.debug("sendProcessCycleAddedToTeamTrigger has been triggered... "
							+ deltaCaseDetails.getPrimaryTeamName() + "::" + addedInterim);
					AtlasJunoIntegration.sendProcessCycleAddedToTeamTrigger(deltaCaseDetails.getCrn(),
							deltaCaseDetails.getPrimaryTeamName(), addedInterim);
				}
			}

			if (deltaCaseDetails.getRemovedPids().size() > 0) {
				this.logger.debug("sendCasePullbackTrigger has been triggered... isPullback :: "
						+ deltaCaseDetails.isPullbackFlag());
				AtlasJunoIntegration.sendCasePullbackTrigger(deltaCaseDetails.getCrn(),
						deltaCaseDetails.getRemovedPids(), deltaCaseDetails.isPullbackFlag());
			}
		} catch (Exception var9) {
			this.logger.debug(var9.getMessage());
			this.logger.debug(var9.getStackTrace().toString());
		}

	}

	private void sendRejectionTriggerToJuno(String crn, Long pid) {
		try {
			ArrayList<Long> pidList = new ArrayList();
			pidList.add(pid);
			this.logger.debug("sendCasePullbackTrigger has been triggered... isPullback :: true");
			AtlasJunoIntegration.sendCasePullbackTrigger(crn, pidList, true);
		} catch (Exception var4) {
			this.logger.debug(var4.getMessage());
			this.logger.debug(var4.getStackTrace().toString());
		}

	}

	private void updatePrimarySubjectDataslots(long pid, Session session, CaseDetails deltaCaseDetails,
			Map<String, CycleInfo> cycleInformation) throws CMSException {
		this.logger.debug("cycleInformation is " + cycleInformation);
		HashMap<String, Object> dsValues = new HashMap();
		dsValues.put("PrimarySubject", deltaCaseDetails.getPrimarySubjectName());
		dsValues.put("PrimarySubjectCountry", deltaCaseDetails.getPrimarySubjectCountryName());
		ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
		this.updateDataslotsForCase(session, cycleInformation, dsValues);
	}

	public void updateResearchDueDateDataslots(long pid, Session session, CaseDetails deltaCaseDetails,
			Map<String, CycleInfo> cycleInformation) throws CMSException {
		this.logger.debug("cycleInformation is " + cycleInformation);
		SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MM-yy");
		HashMap<String, Object> dsValues = new HashMap();
		String rInterim1 = deltaCaseDetails.getResearchDueDates().split("::")[0];
		String rInterim2 = deltaCaseDetails.getResearchDueDates().split("::")[1];
		String rFinal = deltaCaseDetails.getResearchDueDates().split("::")[2];

		try {
			if (rInterim1.length() != 0 && !rInterim1.equalsIgnoreCase("null")) {
				dsValues.put("RInterim1", targetSdf.format(sourceSdf.parse(rInterim1)));
				rInterim1 = targetSdf.format(sourceSdf.parse(rInterim1));
				this.logger.debug("rInterim1::" + rInterim1);
			} else {
				rInterim1 = null;
			}

			if (rInterim2.length() != 0 && !rInterim2.equalsIgnoreCase("null")) {
				dsValues.put("RInterim2", targetSdf.format(sourceSdf.parse(rInterim2)));
				rInterim2 = targetSdf.format(sourceSdf.parse(rInterim2));
				this.logger.debug("rInterim2::" + rInterim2);
			} else {
				rInterim2 = null;
			}

			if (rFinal.length() != 0 && !rFinal.equalsIgnoreCase("null")) {
				dsValues.put("RFinal", targetSdf.format(sourceSdf.parse(rFinal)));
				rFinal = targetSdf.format(sourceSdf.parse(rFinal));
				this.logger.debug("rFinal::" + rFinal);
			}
		} catch (ParseException var13) {
			throw new CMSException(this.logger, var13);
		}

		ResourceLocator.self().getCaseDetailService().updateCaseResearchDueDates(deltaCaseDetails.getCrn(), rInterim1,
				rInterim2, rFinal, deltaCaseDetails.getUpdatedBy());
		this.logger.debug("after updateCaseResearchDueDates::");
		ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
		this.logger.debug("after updateDataSlots::");
		this.updateDataslotsForCase(session, cycleInformation, dsValues);
	}

	private void updateTeamDueDateDataslots(long pid, Session session, CaseDetails deltaCaseDetails,
			Map<String, CycleInfo> cycleInformation, HashMap<String, String> updatedDueDatesMap) throws CMSException {
		this.logger.debug("in updateTeamDueDateDataslots... ");
		SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
		HashMap<String, Object> dueDateDSValues = new HashMap();

		String teamName;
		for (Iterator var11 = updatedDueDatesMap.keySet().iterator(); var11.hasNext(); this
				.updateDueDateDataslotsForTeam(session, cycleInformation, dueDateDSValues, teamName)) {
			teamName = (String) var11.next();
			this.logger.debug("teamName :: " + teamName + " dueDates :: " + (String) updatedDueDatesMap.get(teamName));
			String updatedRInterim1 = ((String) updatedDueDatesMap.get(teamName)).split("#")[0];
			String updatedRInterim2 = ((String) updatedDueDatesMap.get(teamName)).split("#")[1];
			String updatedRFinal = ((String) updatedDueDatesMap.get(teamName)).split("#")[2];

			try {
				if (updatedRInterim1.length() != 0 && !updatedRInterim1.equalsIgnoreCase("null")) {
					dueDateDSValues.put("tInterim1", targetSdf.format(sourceSdf.parse(updatedRInterim1)));
				}

				if (updatedRInterim2.length() != 0 && !updatedRInterim2.equalsIgnoreCase("null")) {
					dueDateDSValues.put("tInterim2", targetSdf.format(sourceSdf.parse(updatedRInterim2)));
				}

				if (updatedRFinal.length() != 0 && !updatedRFinal.equalsIgnoreCase("null")) {
					dueDateDSValues.put("tFinal", targetSdf.format(sourceSdf.parse(updatedRFinal)));
				}
			} catch (ParseException var16) {
				throw new CMSException(this.logger, var16);
			}
		}

	}

	public void updateDataslotsForCase(Session session, Map<String, CycleInfo> cycleInformation,
			HashMap<String, Object> dataslotValues) throws CMSException {
		this.logger.debug("in updateDataslotsForCase... ");
		Iterator var5 = cycleInformation.keySet().iterator();

		label50 : while (var5.hasNext()) {
			String interimCycle = (String) var5.next();
			Iterator var7 = ((CycleInfo) cycleInformation.get(interimCycle)).getTeamInfo().keySet().iterator();

			while (true) {
				String teamName;
				TeamAnalystMapping teamAnalystMapping;
				do {
					do {
						if (!var7.hasNext()) {
							continue label50;
						}

						teamName = (String) var7.next();
						this.logger.debug("checking for interim cycle : " + interimCycle);
						teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation.get(interimCycle))
								.getTeamInfo().get(teamName);
					} while (teamAnalystMapping == null);
				} while (teamAnalystMapping.getResearchProcessPID() == 0L);

				this.logger.debug("teamAnalystMapping.getResearchProcessPID() for " + teamName + " is "
						+ teamAnalystMapping.getResearchProcessPID());
				boolean researchProcessCompleted = ResourceLocator.self().getSBMService()
						.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
				this.logger.debug("researchProcessCompleted for " + teamName + " is " + researchProcessCompleted);
				if (!researchProcessCompleted) {
					ResourceLocator.self().getSBMService().updateDataSlots(session,
							teamAnalystMapping.getResearchProcessPID(), dataslotValues);
					if (teamAnalystMapping.getAnalystTaskStatus() != null) {
						Iterator var11 = teamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

						while (var11.hasNext()) {
							String analyst = (String) var11.next();
							this.logger.debug(
									"teamAnalystMapping.getAnalystTaskStatus().get(analyst).getResearchPID() for "
											+ analyst + " is " + ((AnalystTaskStatus) teamAnalystMapping
													.getAnalystTaskStatus().get(analyst)).getResearchPID());
							if (((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
									.getResearchPID() != 0L) {
								boolean researchTaskCompleted = ResourceLocator.self().getSBMService().isTaskCompleted(
										((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
												.getResearchPID(),
										session);
								this.logger
										.debug("researchTaskCompleted for " + analyst + " is " + researchTaskCompleted);
								if (!researchTaskCompleted) {
									ResourceLocator.self().getSBMService().updateDataSlots(session,
											((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
													.getResearchPID(),
											dataslotValues);
								}
							}
						}
					}
				}

				this.logger.debug("teamAnalystMapping.getReviewProcessPID() for " + teamName + " is "
						+ teamAnalystMapping.getReviewProcessPID());
				if (teamAnalystMapping.getReviewProcessPID() != 0L) {
					boolean reviewTaskCompleted = ResourceLocator.self().getSBMService()
							.isTaskCompleted(teamAnalystMapping.getReviewProcessPID(), session);
					this.logger.debug("reviewTaskCompleted for " + teamName + " is " + reviewTaskCompleted);
					if (!reviewTaskCompleted) {
						ResourceLocator.self().getSBMService().updateDataSlots(session,
								teamAnalystMapping.getReviewProcessPID(), dataslotValues);
					}
				}
			}
		}

	}

	public void updateDueDateDataslotsForTeam(Session session, Map<String, CycleInfo> cycleInformation,
			HashMap<String, Object> dataslotValues, String teamName) throws CMSException {
		this.logger.debug("in updateDueDateDataslotsForTeam... ");
		Iterator var6 = cycleInformation.keySet().iterator();

		while (true) {
			TeamAnalystMapping teamAnalystMapping;
			do {
				do {
					if (!var6.hasNext()) {
						return;
					}

					String interimCycle = (String) var6.next();
					this.logger.debug("checking for interim cycle : " + interimCycle);
					teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation.get(interimCycle))
							.getTeamInfo().get(teamName);
				} while (teamAnalystMapping == null);
			} while (teamAnalystMapping.getResearchProcessPID() == 0L);

			this.logger.debug("teamAnalystMapping.getResearchProcessPID() for " + teamName + " is "
					+ teamAnalystMapping.getResearchProcessPID());
			boolean researchProcessCompleted = ResourceLocator.self().getSBMService()
					.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
			this.logger.debug("researchProcessCompleted for " + teamName + " is " + researchProcessCompleted);
			if (!researchProcessCompleted) {
				ResourceLocator.self().getSBMService().updateDataSlots(session,
						teamAnalystMapping.getResearchProcessPID(), dataslotValues);
				if (teamAnalystMapping.getAnalystTaskStatus() != null) {
					Iterator var10 = teamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

					while (var10.hasNext()) {
						String analyst = (String) var10.next();
						this.logger.debug("teamAnalystMapping.getAnalystTaskStatus().get(analyst).getResearchPID() for "
								+ analyst + " is "
								+ ((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
										.getResearchPID());
						if (((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
								.getResearchPID() != 0L) {
							boolean researchTaskCompleted = ResourceLocator.self().getSBMService().isTaskCompleted(
									((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
											.getResearchPID(),
									session);
							this.logger.debug("researchTaskCompleted for " + analyst + " is " + researchTaskCompleted);
							if (!researchTaskCompleted) {
								ResourceLocator.self().getSBMService().updateDataSlots(session,
										((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus().get(analyst))
												.getResearchPID(),
										dataslotValues);
							}
						}
					}
				}
			}

			this.logger.debug("teamAnalystMapping.getReviewProcessPID() for " + teamName + " is "
					+ teamAnalystMapping.getReviewProcessPID());
			if (teamAnalystMapping.getReviewProcessPID() != 0L) {
				boolean reviewTaskCompleted = ResourceLocator.self().getSBMService()
						.isTaskCompleted(teamAnalystMapping.getReviewProcessPID(), session);
				this.logger.debug("reviewTaskCompleted for " + teamName + " is " + reviewTaskCompleted);
				if (!reviewTaskCompleted) {
					ResourceLocator.self().getSBMService().updateDataSlots(session,
							teamAnalystMapping.getReviewProcessPID(), dataslotValues);
				}
			}
		}
	}

	private boolean isOfficeAssignmentDone(long pid, Session session) throws CMSException {
		boolean isOfficeAssignmentDone = false;
		boolean isAutoOA = (Boolean) ResourceLocator.self().getSBMService().getDataslotValue(pid, "isAutoOA", session);
		if (!isAutoOA) {
			String[] completedWSNames = ResourceLocator.self().getSBMService().getCompletedWSNames(pid, session);

			for (int i = 0; i < completedWSNames.length; ++i) {
				if (completedWSNames[i].contains("Office Assignment Task")) {
					isOfficeAssignmentDone = true;
				}
			}
		} else {
			isOfficeAssignmentDone = true;
		}

		return isOfficeAssignmentDone;
	}

	private void updateCaseInformationDataslots(long pid, Session session, CaseDetails caseDetails,
			HashMap<String, Object> dataslotValues) throws CMSException {
		synchronized (caseDetails.getCrn().intern()) {
			this.logger.debug("Testing by Himanshu for update dataslots");
			FlowLockInfoVO lockVO = new FlowLockInfoVO();
			lockVO.setCrn(caseDetails.getCrn());

			try {
				this.isWaitCondition(lockVO);
				HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
						.getDataslotValue(pid, "customDSMap", session);
				this.logger.debug("customDSMap is " + customDSMap);
				Map<String, CycleInfo> cycleInformation = null;
				String rInterim1;
				if (customDSMap != null) {
					CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
					this.logger.debug("cycleTeamMapping is " + cycleTeamMapping);
					rInterim1 = cycleTeamMapping.getCurrentCycle();
					this.logger.debug("currentCycle is " + rInterim1);
					cycleInformation = cycleTeamMapping.getCycleInformation();
					this.logger.debug("cycleInformation is " + cycleInformation);
				}

				FlowNotificationManager flowNotificationManager = new FlowNotificationManager(caseDetails.getCrn());
				String rInterim2;
				String rFinal;
				if (caseDetails.isCaseManagerFlag()) {
					String[] activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(session,
							pid);
					rInterim2 = null;

					for (int i = 0; i < activatedWSNames.length; ++i) {
						this.logger.debug("activated workstep for case creation process is " + activatedWSNames[i]);
						if (activatedWSNames[i].equals("Office Assignment Task")
								|| activatedWSNames[i].equals("Client Submission Task")) {
							rInterim2 = activatedWSNames[i];
							break;
						}
					}

					if (rInterim2 != null) {
						rFinal = caseDetails.getCaseMgrId().split("#")[0];
						String newCM = caseDetails.getCaseMgrId().split("#")[1];
						this.logger.debug("reassigning " + rInterim2 + " for pid " + pid + " to " + newCM);
						this.stopTimeTracker(rInterim2, String.valueOf(pid), caseDetails.getCrn(), rFinal,
								caseDetails.getUpdatedBy());
						ResourceLocator.self().getSBMService().reassignTask(pid, newCM, rInterim2, session);
					}
				}

				if (caseDetails.isResearchDueDateFlag()) {
					this.logger.debug("::::::::::::::::::::::: Change in research due dates :::::::::::::::::::::");
					rInterim1 = caseDetails.getResearchDueDates().split("::")[0];
					rInterim2 = caseDetails.getResearchDueDates().split("::")[1];
					rFinal = caseDetails.getResearchDueDates().split("::")[2];
					if (rInterim1.length() == 0 || rInterim1.equalsIgnoreCase("null")) {
						rInterim1 = null;
					}

					if (rInterim2.length() == 0 || rInterim2.equalsIgnoreCase("null")) {
						rInterim2 = null;
					}

					this.logger.debug("dueDates :: " + caseDetails.getResearchDueDates());
					ResourceLocator.self().getOfficeAssignmentService().updatePTDueDates(caseDetails.getCrn(),
							rInterim1, rInterim2, rFinal, caseDetails.getUpdatedBy());
					if (cycleInformation != null) {
						TeamAnalystMapping primaryTeam = this.getPrimaryTeamForCycle(cycleInformation, "Final");
						String oldRInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
								"RInterim1", session);
						String oldRInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
								"RInterim2", session);
						String oldRFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
								"RFinal", session);
						if (primaryTeam.isTeamAssignmentDone()) {
							if (oldRInterim1 != null && !oldRInterim1.equals(rInterim1)) {
								flowNotificationManager.prepareResearchDueDateUpdatedNotification(primaryTeam,
										"Interim1", rInterim1);
							}

							if (oldRInterim2 != null && !oldRInterim2.equals(rInterim2)) {
								flowNotificationManager.prepareResearchDueDateUpdatedNotification(primaryTeam,
										"Interim2", rInterim2);
							}

							if (oldRFinal != null && !oldRFinal.equals(rFinal)) {
								flowNotificationManager.prepareResearchDueDateUpdatedNotification(primaryTeam, "Final",
										rFinal);
							}
						}
					}
				}

				ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dataslotValues);
				dataslotValues.remove("ClientFeedback");
				dataslotValues.remove("ClientSentDate");
				dataslotValues.remove("isFinalCycle");
				if (cycleInformation != null) {
					this.updateDataslotsForCase(session, cycleInformation, dataslotValues);
				}

				flowNotificationManager.sendNotifications();
			} catch (Exception var26) {
				throw new CMSException(this.logger, var26);
			} finally {
				try {
					this.releaseLock(lockVO);
				} catch (Exception var25) {
					throw new CMSException(this.logger, var25);
				}
			}

		}
	}

	private void doPullBack(long pid, Session session, CaseDetails deltaCaseDetails,
			HashMap<String, Object> dataslotValues, String module) throws CMSException {
		synchronized (deltaCaseDetails.getCrn().intern()) {
			if (this.isOfficeAssignmentDone(pid, session)) {
				FlowLockInfoVO lockVO = new FlowLockInfoVO();
				lockVO.setCrn(deltaCaseDetails.getCrn());

				try {
					this.isWaitCondition(lockVO);
					HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
							.getDataslotValue(pid, "customDSMap", session);
					this.logger.debug("customDSMap is " + customDSMap);
					CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
					this.logger.debug("cycleTeamMapping is " + cycleTeamMapping);
					String currentCycle = cycleTeamMapping.getCurrentCycle();
					this.logger.debug("currentCycle is " + currentCycle);
					Map<String, CycleInfo> cycleInformation = cycleTeamMapping.getCycleInformation();
					this.logger.debug("cycleInformation is " + cycleInformation);
					List<String> applicableCyclesList = new ArrayList();
					this.logger.debug("cycleTeamMapping.getCycleApplicable() " + cycleTeamMapping.getCycleApplicable());

					for (int index = 0; index < cycleTeamMapping.getCycleApplicable().length; ++index) {
						applicableCyclesList.add(cycleTeamMapping.getCycleApplicable()[index]);
					}

					CycleInfo interim1CycleInfo = (CycleInfo) cycleInformation.get("Interim1");
					this.logger.debug("interim1CycleInfo is " + interim1CycleInfo);
					Map<String, TeamAnalystMapping> interim1TeamInfo = null;
					if (interim1CycleInfo != null) {
						interim1TeamInfo = interim1CycleInfo.getTeamInfo();
					}

					this.logger.debug("interim1TeamInfo is " + interim1TeamInfo);
					CycleInfo interim2CycleInfo = (CycleInfo) cycleInformation.get("Interim2");
					this.logger.debug("interim2CycleInfo is " + interim2CycleInfo);
					Map<String, TeamAnalystMapping> interim2TeamInfo = null;
					if (interim2CycleInfo != null) {
						interim2TeamInfo = interim2CycleInfo.getTeamInfo();
					}

					this.logger.debug("interim2TeamInfo is " + interim2TeamInfo);
					CycleInfo finalCycleInfo = (CycleInfo) cycleInformation.get("Final");
					this.logger.debug("finalCycleInfo is " + finalCycleInfo);
					Map<String, TeamAnalystMapping> finalTeamInfo = null;
					if (finalCycleInfo != null) {
						finalTeamInfo = finalCycleInfo.getTeamInfo();
					}

					this.logger.debug("finalTeamInfo is " + finalTeamInfo);
					HashMap<String, String> workstepInfoMap = new HashMap();
					FlowNotificationManager flowNotificationManager = new FlowNotificationManager(
							deltaCaseDetails.getCrn());
					HashMap dsValues;
					HashMap teamNamesMap;
					String caseManager;
					String interimCycle;
					String oldMainAnalyst;
					TeamAnalystMapping teamAnalystMapping;
					boolean isResearchProcessCompleted;
					HashMap mainAnalystMap;
					String rInterim1;
					Iterator var72;
					String teamName;
					Iterator var79;
					HashMap childReviewersMap;
					String teamName;
					String teamName;
					String reviewer;
					Iterator var105;
					if (module.equals("CaseInformation")) {
						dsValues = new HashMap();
						HashMap<String, String> teamNamesMap = new HashMap();
						teamNamesMap = new HashMap();
						TeamAnalystMapping primaryTeamAnalystMapping = this.getPrimaryTeamForCycle(cycleInformation,
								"Final");
						if (deltaCaseDetails.isInterimCycleFlag()) {
							this.logger.debug(
									"::::::::::::::::::::::: Pullback because of interim cycle :::::::::::::::::::::");
							List<String> addedInterimDueDates = deltaCaseDetails.getAddedInterimDueDates();
							TeamAnalystMapping primaryTeamForCurrentCycle = this
									.getPrimaryTeamForCycle(cycleInformation, currentCycle);
							caseManager = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"CaseManager", session);
							deltaCaseDetails.setPrimaryTeamName(primaryTeamForCurrentCycle.getTeamName());
							var79 = addedInterimDueDates.iterator();

							while (true) {
								while (var79.hasNext()) {
									teamName = (String) var79.next();
									this.logger.debug("addedInterimDueDate " + teamName);
									oldMainAnalyst = teamName.split("::")[0];
									teamName = teamName.split("::")[1];
									boolean isResearchProcessCompleted;
									String[] researchActivatedWSNames;
									if (oldMainAnalyst.equals("Interim1")) {
										this.logger.debug("adding interim 1 cycle to case with due date " + teamName);
										applicableCyclesList.add(0, "Interim1");
										interim1TeamInfo = new HashMap();
										this.addTeamToInterimCycle(primaryTeamAnalystMapping, oldMainAnalyst, teamName,
												(Map) interim1TeamInfo);
										this.addTeamInfoForCycle((Map) interim1TeamInfo, cycleInformation, "Interim1",
												currentCycle);
										this.logger.debug("team assignment is done for team "
												+ primaryTeamAnalystMapping.getTeamName() + " : "
												+ primaryTeamAnalystMapping.isTeamAssignmentDone());
										isResearchProcessCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(primaryTeamAnalystMapping.getResearchProcessPID(),
														session);
										this.logger.debug("team " + primaryTeamAnalystMapping.getTeamName()
												+ "research process status is "
												+ (isResearchProcessCompleted ? "done" : "in progress"));
										if (!isResearchProcessCompleted) {
											researchActivatedWSNames = ResourceLocator.self().getSBMService()
													.getActivatedWSNames(session,
															primaryTeamAnalystMapping.getResearchProcessPID());
											if (researchActivatedWSNames.length != 0
													&& researchActivatedWSNames[0].equals("Team Assignment Task")) {
												this.stopTimeTrackerAndLogCaseHistory("Team Assignment Task",
														String.valueOf(
																primaryTeamAnalystMapping.getResearchProcessPID()),
														deltaCaseDetails.getCrn(), currentCycle, "Complete",
														primaryTeamAnalystMapping.getResearchHead(), "Pullback",
														deltaCaseDetails.getUpdatedBy());
												((TeamAnalystMapping) ((Map) interim1TeamInfo)
														.get(primaryTeamAnalystMapping.getTeamName()))
																.setResearchTaskStatus("New");
											}

											ResourceLocator.self().getSBMService().removeProcessInstance(
													primaryTeamAnalystMapping.getResearchProcessPID());
											deltaCaseDetails.getRemovedPids()
													.add(primaryTeamAnalystMapping.getResearchProcessPID());
										} else if (this.isPrimaryReviewComplete(session, pid)) {
											teamNamesMap.put(primaryTeamAnalystMapping.getTeamName(), "Final");
										}

										this.logger.debug("team research task status is "
												+ primaryTeamAnalystMapping.getResearchTaskStatus());
										var105 = primaryTeamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

										while (var105.hasNext()) {
											teamName = (String) var105.next();
											this.logger.debug("analyst research task pid is "
													+ ((AnalystTaskStatus) primaryTeamAnalystMapping
															.getAnalystTaskStatus().get(teamName)).getResearchPID());
											isResearchProcessCompleted = ResourceLocator.self().getSBMService()
													.isTaskCompleted(((AnalystTaskStatus) primaryTeamAnalystMapping
															.getAnalystTaskStatus().get(teamName)).getResearchPID(),
															session);
											this.logger.debug("analyst " + teamName + "research task status is "
													+ (isResearchProcessCompleted ? "done" : "in progress"));
											if (!isResearchProcessCompleted) {
												this.stopTimeTrackerAndLogCaseHistory("Research Task",
														String.valueOf(((AnalystTaskStatus) primaryTeamAnalystMapping
																.getAnalystTaskStatus().get(teamName))
																		.getResearchPID()),
														deltaCaseDetails.getCrn(), "Final", "Complete", teamName,
														"Pullback", deltaCaseDetails.getUpdatedBy());
												ResourceLocator.self().getSBMService().removeProcessInstance(
														((AnalystTaskStatus) primaryTeamAnalystMapping
																.getAnalystTaskStatus().get(teamName))
																		.getResearchPID());
												deltaCaseDetails.getRemovedPids()
														.add(((AnalystTaskStatus) primaryTeamAnalystMapping
																.getAnalystTaskStatus().get(teamName))
																		.getResearchPID());
											}
										}

										this.resetTasksForTeam(primaryTeamAnalystMapping);
										dsValues.put("isPullback", true);
										workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
												(TeamAnalystMapping) null, primaryTeamForCurrentCycle));
										cycleTeamMapping.setCurrentCycle("Interim1");
										flowNotificationManager.prepareInterimCycleAddedNotification(
												primaryTeamAnalystMapping, finalCycleInfo, caseManager,
												deltaCaseDetails.getUpdatedBy(), cycleTeamMapping.getCurrentCycle());
									} else if (oldMainAnalyst.equals("Interim2")) {
										this.logger.debug("adding interim 2 cycle to case with due date " + teamName);
										if (interim1TeamInfo == null) {
											applicableCyclesList.add(0, "Interim2");
										} else {
											applicableCyclesList.add(1, "Interim2");
										}

										Map<String, TeamAnalystMapping> interim2TeamInfo = new HashMap();
										this.addTeamToInterimCycle(primaryTeamAnalystMapping, oldMainAnalyst, teamName,
												interim2TeamInfo);
										this.addTeamInfoForCycle(interim2TeamInfo, cycleInformation, "Interim2",
												currentCycle);
										if (cycleTeamMapping.getCurrentCycle().equals("Interim1")) {
											flowNotificationManager.prepareInterimCycleAddedNotification(
													primaryTeamAnalystMapping, finalCycleInfo, caseManager,
													deltaCaseDetails.getUpdatedBy(), (String) null);
										} else {
											this.logger.debug("team assignment is done for team "
													+ primaryTeamAnalystMapping.getTeamName() + " : "
													+ primaryTeamAnalystMapping.isTeamAssignmentDone());
											isResearchProcessCompleted = ResourceLocator.self().getSBMService()
													.isTaskCompleted(primaryTeamAnalystMapping.getResearchProcessPID(),
															session);
											this.logger.debug("team " + primaryTeamAnalystMapping.getTeamName()
													+ "research process status is "
													+ (isResearchProcessCompleted ? "done" : "in progress"));
											if (!isResearchProcessCompleted) {
												researchActivatedWSNames = ResourceLocator.self().getSBMService()
														.getActivatedWSNames(session,
																primaryTeamAnalystMapping.getResearchProcessPID());
												if (researchActivatedWSNames.length != 0
														&& researchActivatedWSNames[0].equals("Team Assignment Task")) {
													this.stopTimeTrackerAndLogCaseHistory("Team Assignment Task",
															String.valueOf(
																	primaryTeamAnalystMapping.getResearchProcessPID()),
															deltaCaseDetails.getCrn(), currentCycle, "Complete",
															primaryTeamAnalystMapping.getResearchHead(), "Pullback",
															deltaCaseDetails.getUpdatedBy());
													((TeamAnalystMapping) ((Map) interim1TeamInfo)
															.get(primaryTeamAnalystMapping.getTeamName()))
																	.setResearchTaskStatus("New");
												}

												ResourceLocator.self().getSBMService().removeProcessInstance(
														primaryTeamAnalystMapping.getResearchProcessPID());
												deltaCaseDetails.getRemovedPids()
														.add(primaryTeamAnalystMapping.getResearchProcessPID());
											} else if (this.isPrimaryReviewComplete(session, pid)) {
												teamNamesMap.put(primaryTeamAnalystMapping.getTeamName(), "Final");
											}

											this.logger.debug("team research task status is "
													+ primaryTeamAnalystMapping.getResearchTaskStatus());
											var105 = primaryTeamAnalystMapping.getAnalystTaskStatus().keySet()
													.iterator();

											while (var105.hasNext()) {
												teamName = (String) var105.next();
												this.logger.debug("analyst research task pid is "
														+ ((AnalystTaskStatus) primaryTeamAnalystMapping
																.getAnalystTaskStatus().get(teamName))
																		.getResearchPID());
												isResearchProcessCompleted = ResourceLocator.self().getSBMService()
														.isTaskCompleted(((AnalystTaskStatus) primaryTeamAnalystMapping
																.getAnalystTaskStatus().get(teamName)).getResearchPID(),
																session);
												this.logger.debug("analyst " + teamName + "research task status is "
														+ (isResearchProcessCompleted ? "done" : "in progress"));
												if (!isResearchProcessCompleted) {
													this.stopTimeTrackerAndLogCaseHistory("Research Task",
															String.valueOf(
																	((AnalystTaskStatus) primaryTeamAnalystMapping
																			.getAnalystTaskStatus().get(teamName))
																					.getResearchPID()),
															deltaCaseDetails.getCrn(), "Final", "Complete", teamName,
															"Pullback", deltaCaseDetails.getUpdatedBy());
													ResourceLocator.self().getSBMService().removeProcessInstance(
															((AnalystTaskStatus) primaryTeamAnalystMapping
																	.getAnalystTaskStatus().get(teamName))
																			.getResearchPID());
													deltaCaseDetails.getRemovedPids()
															.add(((AnalystTaskStatus) primaryTeamAnalystMapping
																	.getAnalystTaskStatus().get(teamName))
																			.getResearchPID());
												}
											}

											this.resetTasksForTeam(primaryTeamAnalystMapping);
											dsValues.put("isPullback", true);
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													(TeamAnalystMapping) null, primaryTeamForCurrentCycle));
											cycleTeamMapping.setCurrentCycle("Interim2");
											flowNotificationManager.prepareInterimCycleAddedNotification(
													primaryTeamAnalystMapping, finalCycleInfo, caseManager,
													deltaCaseDetails.getUpdatedBy(),
													cycleTeamMapping.getCurrentCycle());
										}
									}
								}

								childReviewersMap = new HashMap();
								boolean startConsolidation = this.isStartConsolidation(pid, session, currentCycle,
										cycleInformation, childReviewersMap);
								if (startConsolidation) {
									dsValues.put("startConsolidation", startConsolidation);
									dsValues.putAll(childReviewersMap);
								}

								ResourceLocator.self().getOfficeAssignmentService()
										.resetTeamResearchCMPDates(deltaCaseDetails.getCrn(), teamNamesMap);
								this.logger.debug(
										"setting cycleApplicable - " + applicableCyclesList.toArray(new String[0]));
								cycleTeamMapping.setCycleInformation(cycleInformation);
								cycleTeamMapping
										.setCycleApplicable((String[]) applicableCyclesList.toArray(new String[0]));
								customDSMap.put("CycleTeamMapping", cycleTeamMapping);
								teamNamesMap.put("customDSMap", customDSMap);
								teamNamesMap.put("ProcessCycle", cycleTeamMapping.getCurrentCycle());
								break;
							}
						}

						if (deltaCaseDetails.isCaseManagerFlag()) {
							this.logger.debug("::::::::::::::::::::::: Change in case manager :::::::::::::::::::::");
							String[] activatedWSNames = ResourceLocator.self().getSBMService()
									.getActivatedWSNames(session, pid);
							rInterim1 = null;

							for (int i = 0; i < activatedWSNames.length; ++i) {
								this.logger.debug(
										"activated workstep for case creation process is " + activatedWSNames[i]);
								if (activatedWSNames[i].equals("Office Assignment Task")
										|| activatedWSNames[i].equals("Client Submission Task")) {
									rInterim1 = activatedWSNames[i];
									break;
								}
							}

							if (rInterim1 != null) {
								caseManager = deltaCaseDetails.getCaseMgrId().split("#")[0];
								teamName = deltaCaseDetails.getCaseMgrId().split("#")[1];
								this.logger.debug("reassigning " + rInterim1 + " for pid " + pid + " to " + teamName);
								this.stopTimeTracker(rInterim1, String.valueOf(pid), deltaCaseDetails.getCrn(),
										caseManager, deltaCaseDetails.getUpdatedBy());
								ResourceLocator.self().getSBMService().reassignTask(pid, teamName, rInterim1, session);
							}
						}

						mainAnalystMap = new HashMap();
						if (deltaCaseDetails.isResearchDueDateFlag()) {
							this.logger.debug(
									"::::::::::::::::::::::: Change in research due dates :::::::::::::::::::::");
							rInterim1 = deltaCaseDetails.getResearchDueDates().split("::")[0];
							caseManager = deltaCaseDetails.getResearchDueDates().split("::")[1];
							teamName = deltaCaseDetails.getResearchDueDates().split("::")[2];
							if (rInterim1.length() == 0 || rInterim1.equalsIgnoreCase("null")) {
								rInterim1 = null;
							}

							if (caseManager.length() == 0 || caseManager.equalsIgnoreCase("null")) {
								caseManager = null;
							}

							ResourceLocator.self().getOfficeAssignmentService().updatePTDueDates(
									deltaCaseDetails.getCrn(), rInterim1, caseManager, teamName,
									deltaCaseDetails.getUpdatedBy());
							interimCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"RInterim1", session);
							oldMainAnalyst = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"RInterim2", session);
							teamName = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal",
									session);
							SimpleDateFormat sourceSdf = new SimpleDateFormat("dd-MMM-yyyy");
							SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MM-yy");
							teamAnalystMapping = null;
							isResearchProcessCompleted = primaryTeamAnalystMapping.isTeamAssignmentDone();
							if (interimCycle != null && !interimCycle.equals(rInterim1)) {
								reviewer = targetSdf.format(sourceSdf.parse(rInterim1));
								if (isResearchProcessCompleted) {
									flowNotificationManager.prepareResearchDueDateUpdatedNotification(
											primaryTeamAnalystMapping, "Interim1", rInterim1);
								}
							} else {
								reviewer = "null";
							}

							reviewer = reviewer + "#";
							if (oldMainAnalyst != null && !oldMainAnalyst.equals(caseManager)) {
								reviewer = reviewer + targetSdf.format(sourceSdf.parse(caseManager));
								if (isResearchProcessCompleted) {
									flowNotificationManager.prepareResearchDueDateUpdatedNotification(
											primaryTeamAnalystMapping, "Interim2", caseManager);
								}
							} else {
								reviewer = reviewer + "null";
							}

							reviewer = reviewer + "#";
							if (teamName != null && !teamName.equals(teamName)) {
								reviewer = reviewer + targetSdf.format(sourceSdf.parse(teamName));
								if (isResearchProcessCompleted) {
									flowNotificationManager.prepareResearchDueDateUpdatedNotification(
											primaryTeamAnalystMapping, "Final", teamName);
								}
							} else {
								reviewer = reviewer + "null";
							}

							this.logger.debug("dueDates :: " + reviewer);
							mainAnalystMap.put(primaryTeamAnalystMapping.getTeamName(), reviewer);
						}

						ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dataslotValues);
						dataslotValues.remove("ClientFeedback");
						dataslotValues.remove("ClientSentDate");
						dataslotValues.remove("isFinalCycle");
						this.updateDataslotsForCase(session, cycleInformation, dataslotValues);
						this.updateTeamDueDateDataslots(pid, session, deltaCaseDetails, cycleInformation,
								mainAnalystMap);
						if (teamNamesMap.size() > 0) {
							ResourceLocator.self().getSBMService().updateDataSlots(session, pid, teamNamesMap);
						}

						Thread.sleep(2000L);
						this.completeWorksteps(workstepInfoMap, session, dsValues, deltaCaseDetails.getCrn(),
								deltaCaseDetails.getUpdatedBy());
						var72 = workstepInfoMap.keySet().iterator();

						while (var72.hasNext()) {
							rInterim1 = (String) var72.next();
							long pidToBeCompleted = Long.valueOf(rInterim1.split("#")[1]);
							if (!deltaCaseDetails.getRemovedPids().contains(pidToBeCompleted)) {
								deltaCaseDetails.getRemovedPids().add(pidToBeCompleted);
							}
						}

						if (dsValues.containsKey("isPullback")) {
							deltaCaseDetails.setPullbackFlag(true);
						}
					} else {
						dsValues = new HashMap();
						TeamAnalystMapping primaryTeamAnalystMapping = this.getPrimaryTeamForCycle(cycleInformation,
								currentCycle);
						teamNamesMap = new HashMap();
						HashMap affectedTeamsMap;
						Set addedInterims;
						String teamName;
						String interimName;
						HashMap parentReviewersMap;
						Iterator var80;
						TeamAnalystMapping teamAnalystMapping;
						String oldCM;
						if (deltaCaseDetails.isTeamFlag()) {
							this.logger.debug("::::::::::::::::::::::: Pullback because of team :::::::::::::::::::::");
							affectedTeamsMap = deltaCaseDetails.getDeletedTeamsMap();
							addedInterims = affectedTeamsMap.keySet();
							ArrayList<String> deletedTeamNames = new ArrayList();
							Iterator var29 = addedInterims.iterator();

							label3640 : while (var29.hasNext()) {
								caseManager = (String) var29.next();
								this.logger.debug("deleting team " + caseManager);
								interimCycle = caseManager.split("@")[0];
								oldMainAnalyst = caseManager.split("@")[1];
								Set<String> interimsList = cycleInformation.keySet();
								Iterator var34 = interimsList.iterator();

								while (true) {
									do {
										if (!var34.hasNext()) {
											continue label3640;
										}

										teamName = (String) var34.next();
									} while (!teamName.equals("Final")
											&& (!teamName.equals("Interim2") || currentCycle.equals("Final"))
											&& (!teamName.equals("Interim1") || currentCycle.equals("Interim2")
													|| currentCycle.equals("Final")));

									teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation
											.get(teamName)).getTeamInfo().get(interimCycle);
									if (teamAnalystMapping != null) {
										this.logger.debug("deleting team from interim cycle : " + teamName);
										this.logger.debug("team assignment is done for team " + interimCycle + " : "
												+ teamAnalystMapping.isTeamAssignmentDone());
										if (!teamAnalystMapping.isTeamAssignmentDone()) {
											isResearchProcessCompleted = ResourceLocator.self().getSBMService()
													.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
															session);
											this.logger.debug("team assignment completed for team with pid "
													+ teamAnalystMapping.getResearchProcessPID() + " : "
													+ isResearchProcessCompleted);
											if (!isResearchProcessCompleted) {
												this.stopTimeTrackerAndLogCaseHistory("Team Assignment Task",
														String.valueOf(teamAnalystMapping.getResearchProcessPID()),
														deltaCaseDetails.getCrn(), teamName, "Complete",
														teamAnalystMapping.getResearchHead(), "Pullback",
														deltaCaseDetails.getUpdatedBy());
												ResourceLocator.self().getSBMService().removeProcessInstance(
														teamAnalystMapping.getResearchProcessPID());
												deltaCaseDetails.getRemovedPids()
														.add(teamAnalystMapping.getResearchProcessPID());
											}
										} else {
											this.logger.debug("team research process pid is "
													+ teamAnalystMapping.getResearchProcessPID());
											isResearchProcessCompleted = ResourceLocator.self().getSBMService()
													.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
															session);
											this.logger.debug("team " + teamAnalystMapping.getTeamName()
													+ "research process status is "
													+ (isResearchProcessCompleted ? "done" : "in progress"));
											if (!isResearchProcessCompleted) {
												String[] researchActivatedWSNames = ResourceLocator.self()
														.getSBMService().getActivatedWSNames(session,
																teamAnalystMapping.getResearchProcessPID());
												if (researchActivatedWSNames.length != 0) {
													if (researchActivatedWSNames[0].equals("Review")) {
														interimName = (String) ResourceLocator.self().getSBMService()
																.getDataslotValue(
																		teamAnalystMapping.getReviewProcessPID(),
																		"Reviewer", session);
														this.stopTimeTrackerAndLogCaseHistory("Review Task",
																String.valueOf(
																		teamAnalystMapping.getReviewProcessPID()),
																deltaCaseDetails.getCrn(), teamName, "Complete",
																interimName, "Pullback",
																deltaCaseDetails.getUpdatedBy());
													} else if (researchActivatedWSNames[0]
															.equals("Consolidation Task")) {
														this.stopTimeTrackerAndLogCaseHistory("Consolidation Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), teamName, "Complete",
																teamAnalystMapping.getMainAnalyst(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
													} else if (researchActivatedWSNames[0].equals("BI Research Task")) {
														this.stopTimeTrackerAndLogCaseHistory("BI Research Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), teamName, "Complete",
																teamAnalystMapping.getResearchHead(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
													} else if (researchActivatedWSNames[0]
															.equals("Vendor Research Task")) {
														this.stopTimeTrackerAndLogCaseHistory("Vendor Research Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), teamName, "Complete",
																teamAnalystMapping.getResearchHead(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
													}
												}

												ResourceLocator.self().getSBMService().removeProcessInstance(
														teamAnalystMapping.getResearchProcessPID());
												deltaCaseDetails.getRemovedPids()
														.add(teamAnalystMapping.getResearchProcessPID());
											}

											if (!teamAnalystMapping.getTeamName().contains("BI")
													&& !teamAnalystMapping.getTeamName().contains("Vendor")) {
												this.logger.debug("team research task status is "
														+ teamAnalystMapping.getResearchTaskStatus());
												Iterator var115 = teamAnalystMapping.getAnalystTaskStatus().keySet()
														.iterator();

												while (var115.hasNext()) {
													oldCM = (String) var115.next();
													this.logger.debug("analyst research task pid is "
															+ ((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(oldCM))
																			.getResearchPID());
													boolean isResearchTaskCompleted = ResourceLocator.self()
															.getSBMService()
															.isTaskCompleted(((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(oldCM))
																			.getResearchPID(),
																	session);
													this.logger.debug("analyst " + oldCM + "research task status is "
															+ (isResearchTaskCompleted ? "done" : "in progress"));
													if (!isResearchTaskCompleted) {
														this.stopTimeTrackerAndLogCaseHistory("Research Task",
																String.valueOf(((AnalystTaskStatus) teamAnalystMapping
																		.getAnalystTaskStatus().get(oldCM))
																				.getResearchPID()),
																deltaCaseDetails.getCrn(), teamName, "Complete", oldCM,
																"Pullback", deltaCaseDetails.getUpdatedBy());
														ResourceLocator.self().getSBMService().removeProcessInstance(
																((AnalystTaskStatus) teamAnalystMapping
																		.getAnalystTaskStatus().get(oldCM))
																				.getResearchPID());
														deltaCaseDetails.getRemovedPids()
																.add(((AnalystTaskStatus) teamAnalystMapping
																		.getAnalystTaskStatus().get(oldCM))
																				.getResearchPID());
													}
												}
											}
										}
									}

									if (interimCycle.contains("Primary")) {
										flowNotificationManager.preparePrimaryTeamDeletedNotification(finalCycleInfo,
												oldMainAnalyst);
									} else if (interimCycle.contains("Vendor")) {
										flowNotificationManager.prepareVendorTeamDeletedNotification(finalCycleInfo,
												oldMainAnalyst);
									} else if (interimCycle.contains("Supporting - Internal")) {
										flowNotificationManager.prepareSupportingTeamDeletedNotification(finalCycleInfo,
												oldMainAnalyst);
									}

									((CycleInfo) cycleInformation.get(teamName)).getTeamInfo().remove(interimCycle);
									deletedTeamNames.add(interimCycle);
								}
							}

							parentReviewersMap = deltaCaseDetails.getAddedTeamsMap();
							Set<String> addedTeams = parentReviewersMap.keySet();
							var80 = addedTeams.iterator();

							while (var80.hasNext()) {
								interimCycle = (String) var80.next();
								this.logger.debug("adding team " + interimCycle);
								TeamDetails teamDetails = (TeamDetails) parentReviewersMap.get(interimCycle);
								teamName = interimCycle.split("@")[0];
								if (deletedTeamNames.contains(teamName) && !teamName.contains("BI")
										&& !teamName.contains("Vendor")) {
									this.logger.debug("resetting team office details for team " + teamName);
									ResourceLocator.self().getOfficeAssignmentService()
											.resetTeamOfficeDetails(teamDetails);
								}

								teamAnalystMapping = this.initializeMappingForOfficeTask(teamDetails);
								teamAnalystMapping.setResearchTaskStatus("New");
								boolean isBypassInterim = teamDetails.getByPassInterim().equals("1");
								if (!isBypassInterim) {
									this.logger.debug("due date for int 1 is :" + teamDetails.getDueDate1()
											+ " for team " + teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
									if (teamDetails.getDueDate1() != null && teamDetails.getDueDate1().length() != 0
											&& !currentCycle.equals("Interim2") && !currentCycle.equals("Final")) {
										this.addTeamToInterimCycle(teamAnalystMapping, "Interim1",
												teamDetails.getDueDate1(), (Map) interim1TeamInfo);
										teamAnalystMapping.setResearchTaskStatus("New");
									}

									this.logger.debug("due date for int 2 is :" + teamDetails.getDueDate2()
											+ " for team " + teamDetails.getTeamType() + "#" + teamDetails.getTeamId());
									if (teamDetails.getDueDate2() != null && teamDetails.getDueDate2().length() != 0
											&& !currentCycle.equals("Final")) {
										this.addTeamToInterimCycle(teamAnalystMapping, "Interim2",
												teamDetails.getDueDate2(), interim2TeamInfo);
										teamAnalystMapping.setResearchTaskStatus("New");
									}
								}

								teamAnalystMapping.setClientDueDate(teamDetails.getFinalDueDate());
								finalTeamInfo.put(teamAnalystMapping.getTeamName(), teamAnalystMapping);
								this.logger.debug("current cycle is " + currentCycle);
								dsValues.put("isPullback", true);
								workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
										(TeamAnalystMapping) null, primaryTeamAnalystMapping));
							}
						}

						TeamAnalystMapping teamAnalystMapping;
						boolean startConsolidation;
						String[] activatedWSNames;
						String reviewer;
						Iterator var106;
						boolean isResearchProcessCompleted;
						if (deltaCaseDetails.isDueDateFlag()) {
							this.logger.debug(
									"::::::::::::::::::::::: Pullback because of due dates :::::::::::::::::::::");
							affectedTeamsMap = deltaCaseDetails.getAddedInterimsForTeamMap();
							addedInterims = affectedTeamsMap.keySet();
							var72 = addedInterims.iterator();

							label3595 : while (true) {
								while (true) {
									do {
										if (!var72.hasNext()) {
											break label3595;
										}

										rInterim1 = (String) var72.next();
										teamName = rInterim1.split("::")[0];
										interimCycle = rInterim1.split("::")[1];
									} while (deltaCaseDetails.getAddedTeamsMap().containsKey(teamName));

									oldMainAnalyst = teamName.split("@")[0];
									this.logger.debug("adding team " + oldMainAnalyst + " to " + interimCycle);
									boolean removeCycles = true;
									teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo.get(oldMainAnalyst);
									if (interimCycle.equals("Interim1")) {
										removeCycles = true;
										this.addTeamToInterimCycle(teamAnalystMapping, interimCycle,
												((TeamDetails) affectedTeamsMap.get(rInterim1)).getDueDate1(),
												(Map) interim1TeamInfo);
										startConsolidation = ResourceLocator.self().getSBMService()
												.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
										this.logger.debug("team " + teamAnalystMapping.getTeamName()
												+ "research process status is "
												+ (startConsolidation ? "done" : "in progress"));
										if (!startConsolidation) {
											activatedWSNames = ResourceLocator.self().getSBMService()
													.getActivatedWSNames(session,
															teamAnalystMapping.getResearchProcessPID());
											if (activatedWSNames.length != 0) {
												if (activatedWSNames[0].equals("Review")) {
													reviewer = (String) ResourceLocator.self().getSBMService()
															.getDataslotValue(teamAnalystMapping.getReviewProcessPID(),
																	"Reviewer", session);
													this.stopTimeTrackerAndLogCaseHistory("Review Task",
															String.valueOf(teamAnalystMapping.getReviewProcessPID()),
															deltaCaseDetails.getCrn(), interimCycle, "Complete",
															reviewer, "Pullback", deltaCaseDetails.getUpdatedBy());
												} else if (activatedWSNames[0].equals("Consolidation Task")) {
													this.stopTimeTrackerAndLogCaseHistory("Consolidation Task",
															String.valueOf(teamAnalystMapping.getResearchProcessPID()),
															deltaCaseDetails.getCrn(), interimCycle, "Complete",
															teamAnalystMapping.getMainAnalyst(), "Pullback",
															deltaCaseDetails.getUpdatedBy());
												} else if (activatedWSNames[0].equals("BI Research Task")) {
													this.stopTimeTrackerAndLogCaseHistory("BI Research Task",
															String.valueOf(teamAnalystMapping.getResearchProcessPID()),
															deltaCaseDetails.getCrn(), interimCycle, "Complete",
															teamAnalystMapping.getResearchHead(), "Pullback",
															deltaCaseDetails.getUpdatedBy());
												} else if (activatedWSNames[0].equals("Vendor Research Task")) {
													this.stopTimeTrackerAndLogCaseHistory("Vendor Research Task",
															String.valueOf(teamAnalystMapping.getResearchProcessPID()),
															deltaCaseDetails.getCrn(), interimCycle, "Complete",
															teamAnalystMapping.getResearchHead(), "Pullback",
															deltaCaseDetails.getUpdatedBy());
												} else if (activatedWSNames[0].equals("Team Assignment Task")) {
													this.stopTimeTrackerAndLogCaseHistory("Team Assignment Task",
															String.valueOf(teamAnalystMapping.getResearchProcessPID()),
															deltaCaseDetails.getCrn(), interimCycle, "Complete",
															teamAnalystMapping.getResearchHead(), "Pullback",
															deltaCaseDetails.getUpdatedBy());
													((TeamAnalystMapping) ((Map) interim1TeamInfo)
															.get(teamAnalystMapping.getTeamName()))
																	.setResearchTaskStatus("New");
												}
											}

											ResourceLocator.self().getSBMService()
													.removeProcessInstance(teamAnalystMapping.getResearchProcessPID());
											deltaCaseDetails.getRemovedPids()
													.add(teamAnalystMapping.getResearchProcessPID());
										}

										if (!teamAnalystMapping.getTeamName().contains("BI")
												&& !teamAnalystMapping.getTeamName().contains("Vendor")) {
											this.logger.debug("team research task status is "
													+ teamAnalystMapping.getResearchTaskStatus());
											var106 = teamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

											while (var106.hasNext()) {
												reviewer = (String) var106.next();
												this.logger.debug("analyst research task pid is "
														+ ((AnalystTaskStatus) teamAnalystMapping.getAnalystTaskStatus()
																.get(reviewer)).getResearchPID());
												isResearchProcessCompleted = ResourceLocator.self().getSBMService()
														.isTaskCompleted(((AnalystTaskStatus) teamAnalystMapping
																.getAnalystTaskStatus().get(reviewer)).getResearchPID(),
																session);
												this.logger.debug("analyst " + reviewer + "research task status is "
														+ (isResearchProcessCompleted ? "done" : "in progress"));
												if (!isResearchProcessCompleted) {
													this.stopTimeTrackerAndLogCaseHistory("Research Task",
															String.valueOf(((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(reviewer))
																			.getResearchPID()),
															deltaCaseDetails.getCrn(), interimCycle, "Complete",
															reviewer, "Pullback", deltaCaseDetails.getUpdatedBy());
													ResourceLocator.self().getSBMService().removeProcessInstance(
															((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(reviewer))
																			.getResearchPID());
													deltaCaseDetails.getRemovedPids()
															.add(((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(reviewer))
																			.getResearchPID());
												}
											}
										}

										this.resetTasksForTeam(teamAnalystMapping);
										dsValues.put("isPullback", true);
										workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
												(TeamAnalystMapping) null, primaryTeamAnalystMapping));
										flowNotificationManager.prepareDueDateAddedNotification(teamAnalystMapping,
												"Interim1");
									} else if (interimCycle.equals("Interim2")) {
										this.addTeamToInterimCycle(teamAnalystMapping, interimCycle,
												((TeamDetails) affectedTeamsMap.get(rInterim1)).getDueDate2(),
												interim2TeamInfo);
										if (currentCycle.equals("Interim1")
												&& ((Map) interim1TeamInfo).get(oldMainAnalyst) != null) {
											removeCycles = false;
											flowNotificationManager.prepareDueDateAddedNotification(teamAnalystMapping,
													(String) null);
										} else {
											removeCycles = true;
											startConsolidation = ResourceLocator.self().getSBMService().isTaskCompleted(
													teamAnalystMapping.getResearchProcessPID(), session);
											this.logger.debug("team " + teamAnalystMapping.getTeamName()
													+ "research process status is "
													+ (startConsolidation ? "done" : "in progress"));
											if (!startConsolidation) {
												activatedWSNames = ResourceLocator.self().getSBMService()
														.getActivatedWSNames(session,
																teamAnalystMapping.getResearchProcessPID());
												if (activatedWSNames.length != 0) {
													if (activatedWSNames[0].equals("Review")) {
														reviewer = (String) ResourceLocator.self().getSBMService()
																.getDataslotValue(
																		teamAnalystMapping.getReviewProcessPID(),
																		"Reviewer", session);
														this.stopTimeTrackerAndLogCaseHistory("Review Task",
																String.valueOf(
																		teamAnalystMapping.getReviewProcessPID()),
																deltaCaseDetails.getCrn(), interimCycle, "Complete",
																reviewer, "Pullback", deltaCaseDetails.getUpdatedBy());
													} else if (activatedWSNames[0].equals("Consolidation Task")) {
														this.stopTimeTrackerAndLogCaseHistory("Consolidation Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), interimCycle, "Complete",
																teamAnalystMapping.getMainAnalyst(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
													} else if (activatedWSNames[0].equals("BI Research Task")) {
														this.stopTimeTrackerAndLogCaseHistory("BI Research Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), interimCycle, "Complete",
																teamAnalystMapping.getResearchHead(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
													} else if (activatedWSNames[0].equals("Vendor Research Task")) {
														this.stopTimeTrackerAndLogCaseHistory("Vendor Research Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), interimCycle, "Complete",
																teamAnalystMapping.getResearchHead(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
													} else if (activatedWSNames[0].equals("Team Assignment Task")) {
														this.stopTimeTrackerAndLogCaseHistory("Team Assignment Task",
																String.valueOf(
																		teamAnalystMapping.getResearchProcessPID()),
																deltaCaseDetails.getCrn(), interimCycle, "Complete",
																teamAnalystMapping.getResearchHead(), "Pullback",
																deltaCaseDetails.getUpdatedBy());
														((TeamAnalystMapping) interim2TeamInfo
																.get(teamAnalystMapping.getTeamName()))
																		.setResearchTaskStatus("New");
													}
												}

												ResourceLocator.self().getSBMService().removeProcessInstance(
														teamAnalystMapping.getResearchProcessPID());
												deltaCaseDetails.getRemovedPids()
														.add(teamAnalystMapping.getResearchProcessPID());
											}

											if (!teamAnalystMapping.getTeamName().contains("BI")
													&& !teamAnalystMapping.getTeamName().contains("Vendor")) {
												this.logger.debug("team research task status is "
														+ teamAnalystMapping.getResearchTaskStatus());
												var106 = teamAnalystMapping.getAnalystTaskStatus().keySet().iterator();

												while (var106.hasNext()) {
													reviewer = (String) var106.next();
													this.logger.debug("analyst research task pid is "
															+ ((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(reviewer))
																			.getResearchPID());
													isResearchProcessCompleted = ResourceLocator.self().getSBMService()
															.isTaskCompleted(((AnalystTaskStatus) teamAnalystMapping
																	.getAnalystTaskStatus().get(reviewer))
																			.getResearchPID(),
																	session);
													this.logger.debug("analyst " + reviewer + "research task status is "
															+ (isResearchProcessCompleted ? "done" : "in progress"));
													if (!isResearchProcessCompleted) {
														this.stopTimeTrackerAndLogCaseHistory("Research Task",
																String.valueOf(((AnalystTaskStatus) teamAnalystMapping
																		.getAnalystTaskStatus().get(reviewer))
																				.getResearchPID()),
																deltaCaseDetails.getCrn(), interimCycle, "Complete",
																reviewer, "Pullback", deltaCaseDetails.getUpdatedBy());
														ResourceLocator.self().getSBMService().removeProcessInstance(
																((AnalystTaskStatus) teamAnalystMapping
																		.getAnalystTaskStatus().get(reviewer))
																				.getResearchPID());
														deltaCaseDetails.getRemovedPids()
																.add(((AnalystTaskStatus) teamAnalystMapping
																		.getAnalystTaskStatus().get(reviewer))
																				.getResearchPID());
													}
												}
											}

											flowNotificationManager.prepareDueDateAddedNotification(teamAnalystMapping,
													"Interim2");
										}

										this.resetTasksForTeam(teamAnalystMapping);
										dsValues.put("isPullback", true);
										workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
												(TeamAnalystMapping) null, primaryTeamAnalystMapping));
									}
								}
							}
						}

						affectedTeamsMap = new HashMap();
						mainAnalystMap = new HashMap();
						HashMap<Long, Boolean> reassignConsolidationMap = new HashMap();
						String teamName;
						boolean isResearchProcessCompleted;
						List subTeamReMapForPullback;
						Map analystTaskStatus;
						TeamAnalystMapping teamAnalystMapping;
						AnalystTaskStatus analystTaskStatus;
						AnalystTaskStatus analystTaskStatus;
						boolean isResearchTaskCompleted;
						String teamName;
						if (deltaCaseDetails.isAnalystFlag()) {
							this.logger
									.debug("::::::::::::::::::::::: Pullback because of analyst :::::::::::::::::::::");
							subTeamReMapForPullback = deltaCaseDetails.getDeletedAnalysts();
							var79 = subTeamReMapForPullback.iterator();

							label3549 : while (var79.hasNext()) {
								teamName = (String) var79.next();
								Set<String> interimsList = cycleInformation.keySet();
								teamName = teamName.split("::")[0].split("@")[0];
								teamName = teamName.split("::")[1];
								this.logger.debug("deleting analyst " + teamName + " from team " + teamName);
								var105 = interimsList.iterator();

								while (true) {
									do {
										do {
											if (!var105.hasNext()) {
												continue label3549;
											}

											teamName = (String) var105.next();
										} while (!teamName.equals("Final")
												&& (!teamName.equals("Interim2") || currentCycle.equals("Final"))
												&& (!teamName.equals("Interim1") || currentCycle.equals("Interim2")
														|| currentCycle.equals("Final")));

										teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation
												.get(teamName)).getTeamInfo().get(teamName);
									} while (teamAnalystMapping == null);

									if (teamAnalystMapping.isTeamAssignmentDone()) {
										analystTaskStatus = (AnalystTaskStatus) teamAnalystMapping
												.getAnalystTaskStatus().get(teamName);
										this.logger
												.debug("checking analystTaskStatus.getStatus() before deleting analyst "
														+ analystTaskStatus.getStatus());
										isResearchTaskCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(analystTaskStatus.getResearchPID(), session);
										this.logger.debug("analyst " + teamName + " research task pid "
												+ analystTaskStatus.getResearchPID() + " status is "
												+ (isResearchTaskCompleted ? "done" : "in progress"));
										if (!isResearchTaskCompleted) {
											this.stopTimeTrackerAndLogCaseHistory("Research Task",
													String.valueOf(((AnalystTaskStatus) teamAnalystMapping
															.getAnalystTaskStatus().get(teamName)).getResearchPID()),
													deltaCaseDetails.getCrn(), teamName, "Complete", teamName,
													"Pullback", deltaCaseDetails.getUpdatedBy());
											ResourceLocator.self().getSBMService()
													.removeProcessInstance(analystTaskStatus.getResearchPID());
											deltaCaseDetails.getRemovedPids().add(analystTaskStatus.getResearchPID());
										}

										if (deltaCaseDetails.getMainAnalystMap().get(teamName) == null) {
											this.logger.debug("Only deleting analyst for pull back");
											ResourceLocator.self().getTeamAssignmentService().deleteAnalyst(teamName,
													Integer.valueOf(teamName.split("#")[1]),
													deltaCaseDetails.getUpdatedBy());
										} else {
											teamName = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName))
													.split("#")[0];
											teamName = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName))
													.split("#")[1];
											if (teamName.equals(teamName)) {
												if (teamName.equalsIgnoreCase("null")) {
													teamName = ResourceLocator.self().getTeamAssignmentService()
															.updateMainAnalyst(teamName,
																	Integer.valueOf(teamName.split("#")[1]),
																	deltaCaseDetails.getUpdatedBy());
													this.logger.debug("new main analyst for team " + teamName + " is "
															+ teamName);
													deltaCaseDetails.getMainAnalystMap().put(teamName,
															teamName + "#" + teamName);
												}

												flowNotificationManager.prepareMainAnalystReplacedNotification(
														teamAnalystMapping, teamName, teamName);
												teamAnalystMapping.setMainAnalyst(teamName);
												if (teamAnalystMapping.getTeamName().contains("Primary")) {
													cycleTeamMapping.setPrimaryTeamMainAnalyst(teamName);
												}

												isResearchProcessCompleted = ResourceLocator.self().getSBMService()
														.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																session);
												this.logger.debug("team " + teamName + "research process status is "
														+ (isResearchProcessCompleted ? "done" : "in progress"));
												if (!isResearchProcessCompleted || teamName.contains("Primary")) {
													long pidToBeUpdated = 0L;
													boolean isConsolidationActivated = false;
													String[] activatedWSNames = new String[0];
													if (teamName.contains("Primary")) {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(session, pid);
														pidToBeUpdated = pid;
													} else {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(session,
																		teamAnalystMapping.getResearchProcessPID());
														pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
													}

													mainAnalystMap.put(pidToBeUpdated, teamName + "#" + teamName);

													for (int i = 0; i < activatedWSNames.length; ++i) {
														this.logger.debug("activated workstep for team " + teamName
																+ " is " + activatedWSNames[i]);
														if (activatedWSNames[i].equals("Consolidation Task")) {
															isConsolidationActivated = true;
															break;
														}
													}

													this.logger.debug("isConsolidationActivated for team " + teamName
															+ ": " + isConsolidationActivated);
													reassignConsolidationMap.put(pidToBeUpdated,
															isConsolidationActivated);
												}
											}
										}
									}

									this.logger.debug("adding " + teamName + " to affected teams map for " + teamName);
									affectedTeamsMap.put(teamName, teamName);
									flowNotificationManager.prepareAnalystDeletedNotification(teamAnalystMapping,
											teamName);
									teamAnalystMapping.getAnalystTaskStatus().remove(teamName);
								}
							}

							List<String> addedAnalysts = deltaCaseDetails.getAddedAnalysts();
							var80 = addedAnalysts.iterator();

							while (var80.hasNext()) {
								interimCycle = (String) var80.next();
								teamName = interimCycle.split("::")[0].split("@")[0];
								teamName = interimCycle.split("::")[1];
								this.logger.debug("adding analyst " + teamName + " to team " + teamName);
								if (interim1TeamInfo != null && ((Map) interim1TeamInfo).containsKey(teamName)
										&& !currentCycle.equals("Interim2") && !currentCycle.equals("Final")) {
									teamAnalystMapping = (TeamAnalystMapping) ((Map) interim1TeamInfo).get(teamName);
									if (teamAnalystMapping.isTeamAssignmentDone()) {
										this.logger.debug("adding analyst " + teamName + " to team " + teamName
												+ " for interim 1 cycle");
										analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
										analystTaskStatus = new AnalystTaskStatus();
										analystTaskStatus.setStatus("New");
										analystTaskStatus.setResearchPID(0L);
										this.logger.debug("team " + teamName + " research task status is "
												+ teamAnalystMapping.getResearchTaskStatus());
										isResearchProcessCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
										if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
											this.logger.debug("team " + teamName + "research process status is "
													+ (isResearchProcessCompleted ? "done" : "in progress"));
											if (isResearchProcessCompleted) {
												dsValues.put("isPullback", true);
												workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
														(TeamAnalystMapping) null, primaryTeamAnalystMapping));
											} else {
												dsValues.put("isPullback", true);
												workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
														teamAnalystMapping, primaryTeamAnalystMapping));
											}

											if (!teamNamesMap.containsKey(teamName)) {
												teamNamesMap.put(teamName, "Interim1");
											}
										} else {
											dsValues.put("isPullback", true);
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													teamAnalystMapping, primaryTeamAnalystMapping));
										}

										analystTaskStatus.put(teamName, analystTaskStatus);
										if (deltaCaseDetails.getMainAnalystMap().get(teamName) != null) {
											interimName = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName))
													.split("#")[1];
											if (teamName.equalsIgnoreCase(interimName)) {
												if (!teamAnalystMapping.getMainAnalyst().equals(interimName)) {
													flowNotificationManager.prepareMainAnalystReplacedNotification(
															teamAnalystMapping, teamAnalystMapping.getMainAnalyst(),
															interimName);
												}

												teamAnalystMapping.setMainAnalyst(interimName);
												if (teamAnalystMapping.getTeamName().contains("Primary")) {
													cycleTeamMapping.setPrimaryTeamMainAnalyst(teamName);
												}
											}
										}

										this.logger.debug(
												"removing " + teamName + " from affected teams map for " + "Interim1");
										affectedTeamsMap.remove(teamName);
									}
								}

								if (interim2TeamInfo != null && interim2TeamInfo.containsKey(teamName)
										&& !currentCycle.equals("Final")) {
									this.logger.debug("adding analyst " + teamName + " to team " + teamName
											+ " for interim 2 cycle");
									teamAnalystMapping = (TeamAnalystMapping) interim2TeamInfo.get(teamName);
									if (teamAnalystMapping.isTeamAssignmentDone()) {
										analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
										analystTaskStatus = new AnalystTaskStatus();
										analystTaskStatus.setStatus("New");
										analystTaskStatus.setResearchPID(0L);
										this.logger.debug("team " + teamName + " research task status is "
												+ teamAnalystMapping.getResearchTaskStatus());
										if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
											isResearchProcessCompleted = ResourceLocator.self().getSBMService()
													.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
															session);
											this.logger.debug("team " + teamName + "research process status is "
													+ (isResearchProcessCompleted ? "done" : "in progress"));
											if (isResearchProcessCompleted) {
												dsValues.put("isPullback", true);
												workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
														(TeamAnalystMapping) null, primaryTeamAnalystMapping));
											} else {
												dsValues.put("isPullback", true);
												workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
														teamAnalystMapping, primaryTeamAnalystMapping));
											}

											if (!teamNamesMap.containsKey(teamName)) {
												teamNamesMap.put(teamName, "Interim2");
											}
										} else {
											dsValues.put("isPullback", true);
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													teamAnalystMapping, primaryTeamAnalystMapping));
										}

										analystTaskStatus.put(teamName, analystTaskStatus);
										if (deltaCaseDetails.getMainAnalystMap().get(teamName) != null) {
											oldCM = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName))
													.split("#")[1];
											if (teamName.equalsIgnoreCase(oldCM)) {
												if (!teamAnalystMapping.getMainAnalyst().equals(oldCM)) {
													flowNotificationManager.prepareMainAnalystReplacedNotification(
															teamAnalystMapping, teamAnalystMapping.getMainAnalyst(),
															oldCM);
												}

												teamAnalystMapping.setMainAnalyst(oldCM);
												if (teamAnalystMapping.getTeamName().contains("Primary")) {
													cycleTeamMapping.setPrimaryTeamMainAnalyst(teamName);
												}
											}
										}

										this.logger.debug(
												"removing " + teamName + " from affected teams map for " + "Interim2");
										affectedTeamsMap.remove(teamName);
									}
								}

								if (finalTeamInfo != null) {
									this.logger.debug(
											"adding analyst " + teamName + " to team " + teamName + " for final cycle");
									teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo.get(teamName);
									if (teamAnalystMapping.isTeamAssignmentDone()) {
										analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
										analystTaskStatus = new AnalystTaskStatus();
										analystTaskStatus.setStatus("New");
										analystTaskStatus.setResearchPID(0L);
										this.logger.debug("team " + teamName + " research task status is "
												+ teamAnalystMapping.getResearchTaskStatus());
										if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
											isResearchProcessCompleted = ResourceLocator.self().getSBMService()
													.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
															session);
											this.logger.debug("team " + teamName + "research process status is "
													+ (isResearchProcessCompleted ? "done" : "in progress"));
											if (isResearchProcessCompleted) {
												dsValues.put("isPullback", true);
												workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
														(TeamAnalystMapping) null, primaryTeamAnalystMapping));
											} else {
												dsValues.put("isPullback", true);
												workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
														teamAnalystMapping, primaryTeamAnalystMapping));
											}

											if (!teamNamesMap.containsKey(teamName)) {
												teamNamesMap.put(teamName, "Final");
											}
										} else {
											dsValues.put("isPullback", true);
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													teamAnalystMapping, primaryTeamAnalystMapping));
										}

										analystTaskStatus.put(teamName, analystTaskStatus);
										flowNotificationManager.prepareAnalystAddedNotification(teamAnalystMapping,
												teamName);
										if (deltaCaseDetails.getMainAnalystMap().get(teamName) != null) {
											oldCM = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName))
													.split("#")[1];
											if (teamName.equalsIgnoreCase(oldCM)) {
												if (!teamAnalystMapping.getMainAnalyst().equals(oldCM)) {
													flowNotificationManager.prepareMainAnalystReplacedNotification(
															teamAnalystMapping, teamAnalystMapping.getMainAnalyst(),
															oldCM);
												}

												teamAnalystMapping.setMainAnalyst(oldCM);
												if (teamAnalystMapping.getTeamName().contains("Primary")) {
													cycleTeamMapping.setPrimaryTeamMainAnalyst(teamName);
												}
											}
										}

										this.logger.debug(
												"removing " + teamName + " from affected teams map for " + "Final");
										affectedTeamsMap.remove(teamName);
									}
								}
							}
						} else if (deltaCaseDetails.isMainAnalystFlag()) {
							this.logger.debug(
									"::::::::::::::::::::::: Pullback because of main analyst :::::::::::::::::::::");
							Set<String> teamList = deltaCaseDetails.getMainAnalystMap().keySet();
							var79 = teamList.iterator();

							label3492 : while (var79.hasNext()) {
								teamName = (String) var79.next();
								oldMainAnalyst = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName))
										.split("#")[0];
								teamName = ((String) deltaCaseDetails.getMainAnalystMap().get(teamName)).split("#")[1];
								this.logger.debug(teamName + " :: old main analyst : " + oldMainAnalyst
										+ " :: new main analyst : " + teamName);
								Set<String> interimsList = cycleInformation.keySet();
								var105 = interimsList.iterator();

								while (true) {
									do {
										do {
											do {
												do {
													if (!var105.hasNext()) {
														continue label3492;
													}

													teamName = (String) var105.next();
												} while (!teamName.equals("Final")
														&& (!teamName.equals("Interim2")
																|| currentCycle.equals("Final"))
														&& (!teamName.equals("Interim1")
																|| currentCycle.equals("Interim2")
																|| currentCycle.equals("Final")));

												teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation
														.get(teamName)).getTeamInfo().get(teamName);
											} while (teamAnalystMapping == null);
										} while (!teamAnalystMapping.isTeamAssignmentDone());

										if (!teamAnalystMapping.getMainAnalyst().equals(teamName)) {
											flowNotificationManager.prepareMainAnalystReplacedNotification(
													teamAnalystMapping, teamAnalystMapping.getMainAnalyst(), teamName);
										}

										teamAnalystMapping.setMainAnalyst(teamName);
										if (teamAnalystMapping.getTeamName().contains("Primary")) {
											cycleTeamMapping.setPrimaryTeamMainAnalyst(teamName);
										}

										isResearchProcessCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
										this.logger.debug("team " + teamName + "research process status is "
												+ (isResearchProcessCompleted ? "done" : "in progress"));
									} while (isResearchProcessCompleted && !teamName.contains("Primary"));

									long pidToBeUpdated = 0L;
									boolean isConsolidationActivated = false;
									String[] activatedWSNames = new String[0];
									if (teamName.contains("Primary")) {
										activatedWSNames = ResourceLocator.self().getSBMService()
												.getActivatedWSNames(session, pid);
										pidToBeUpdated = pid;
									} else {
										activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(
												session, teamAnalystMapping.getResearchProcessPID());
										pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
									}

									mainAnalystMap.put(pidToBeUpdated, oldMainAnalyst + "#" + teamName);

									for (int i = 0; i < activatedWSNames.length; ++i) {
										this.logger.debug("activated workstep for team " + teamName + " is "
												+ activatedWSNames[i]);
										if (activatedWSNames[i].equals("Consolidation Task")) {
											isConsolidationActivated = true;
											break;
										}
									}

									this.logger.debug("isConsolidationActivated for team " + teamName + ": "
											+ isConsolidationActivated);
									reassignConsolidationMap.put(pidToBeUpdated, isConsolidationActivated);
								}
							}
						}

						List deletedReviewers;
						Iterator var99;
						if (deltaCaseDetails.isReFlag()) {
							this.logger.debug("::::::::::::::::::::::: Pullback because of RE :::::::::::::::::::::");
							subTeamReMapForPullback = deltaCaseDetails.getAddedSubTeamReMappings();
							teamName = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"CaseManager", session);
							var80 = subTeamReMapForPullback.iterator();

							label3438 : while (true) {
								Map analystTaskStatusMap;
								do {
									do {
										do {
											if (!var80.hasNext()) {
												this.logger.debug(
														"::::::::::::::::::::::: Deleted REs :::::::::::::::::::::");
												deletedReviewers = deltaCaseDetails.getDeletedSubTeamReMappings();
												var99 = deletedReviewers.iterator();

												while (var99.hasNext()) {
													oldMainAnalyst = (String) var99.next();
													teamName = oldMainAnalyst.split("&&")[0];
													teamName = teamName.split("::")[0];
													reviewer = teamName.split("::")[0].split("@")[0];
													reviewer = teamName.split("::")[1];
													this.logger.debug("deletedSubTeamReMapping :: " + oldMainAnalyst);
													if (!deltaCaseDetails.getDeletedAnalysts().contains(teamName)
															&& !deltaCaseDetails.getDeletedTeamsMap()
																	.containsKey(teamName)) {
														this.logger.debug("impacted analyst " + reviewer + " from team "
																+ reviewer);
														TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo
																.get(reviewer);
														flowNotificationManager.prepareREUpdatedNotification(
																teamAnalystMapping, teamName,
																deltaCaseDetails.getUpdatedBy());
													}
												}
												break label3438;
											}

											interimCycle = (String) var80.next();
											teamName = interimCycle.split("&&")[0];
											teamName = teamName.split("::")[0].split("@")[0];
											teamName = teamName.split("::")[1];
										} while (deltaCaseDetails.getAddedAnalysts().contains(teamName));

										this.logger.debug("impacted analyst " + teamName + " from team " + teamName);
										if (interim1TeamInfo != null && ((Map) interim1TeamInfo).containsKey(teamName)
												&& currentCycle.equals("Interim1")) {
											teamAnalystMapping = (TeamAnalystMapping) ((Map) interim1TeamInfo)
													.get(teamName);
											if (teamAnalystMapping.isTeamAssignmentDone()) {
												analystTaskStatusMap = teamAnalystMapping.getAnalystTaskStatus();
												this.logger.debug("team " + teamName + " research task status is "
														+ teamAnalystMapping.getResearchTaskStatus());
												if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
													this.logger.debug("pulling back team " + teamName + " analyst "
															+ teamName + " for interim 1 cycle");
													analystTaskStatus = (AnalystTaskStatus) analystTaskStatusMap
															.get(teamName);
													analystTaskStatus.setStatus("");
													analystTaskStatus.setResearchPID(0L);
													dsValues.put("isPullback", true);
													isResearchTaskCompleted = ResourceLocator.self().getSBMService()
															.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																	session);
													this.logger.debug("team " + teamName + "research process status is "
															+ (isResearchTaskCompleted ? "done" : "in progress"));
													if (isResearchTaskCompleted) {
														workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid,
																session, (TeamAnalystMapping) null,
																primaryTeamAnalystMapping));
													} else {
														workstepInfoMap
																.putAll(this.getWorkstepToBeCompleted(pid, session,
																		teamAnalystMapping, primaryTeamAnalystMapping));
													}

													if (!teamNamesMap.containsKey(teamName)) {
														teamNamesMap.put(teamName, "Interim1");
													}
												}
											}

											flowNotificationManager.prepareREUpdatedNotification(teamAnalystMapping,
													teamName, deltaCaseDetails.getUpdatedBy());
										}

										if (interim2TeamInfo != null && interim2TeamInfo.containsKey(teamName)
												&& (currentCycle.equals("Interim2") || interim1TeamInfo != null
														&& !((Map) interim1TeamInfo).containsKey(teamName))) {
											teamAnalystMapping = (TeamAnalystMapping) interim2TeamInfo.get(teamName);
											if (teamAnalystMapping.isTeamAssignmentDone()) {
												analystTaskStatusMap = teamAnalystMapping.getAnalystTaskStatus();
												this.logger.debug("team " + teamName + " research task status is "
														+ teamAnalystMapping.getResearchTaskStatus());
												if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
													this.logger.debug("pulling back team " + teamName + " analyst "
															+ teamName + " for interim 2 cycle");
													analystTaskStatus = (AnalystTaskStatus) analystTaskStatusMap
															.get(teamName);
													analystTaskStatus.setStatus("");
													analystTaskStatus.setResearchPID(0L);
													dsValues.put("isPullback", true);
													isResearchTaskCompleted = ResourceLocator.self().getSBMService()
															.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																	session);
													this.logger.debug("team " + teamName + "research process status is "
															+ (isResearchTaskCompleted ? "done" : "in progress"));
													dsValues.put("isPullback", true);
													if (isResearchTaskCompleted) {
														workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid,
																session, (TeamAnalystMapping) null,
																primaryTeamAnalystMapping));
													} else {
														workstepInfoMap
																.putAll(this.getWorkstepToBeCompleted(pid, session,
																		teamAnalystMapping, primaryTeamAnalystMapping));
													}

													if (!teamNamesMap.containsKey(teamName)) {
														teamNamesMap.put(teamName, "Interim2");
													}
												}
											}

											flowNotificationManager.prepareREUpdatedNotification(teamAnalystMapping,
													teamName, deltaCaseDetails.getUpdatedBy());
										}
									} while (finalTeamInfo == null);
								} while (!currentCycle.equals("Final")
										&& (interim1TeamInfo == null || ((Map) interim1TeamInfo).containsKey(teamName)
												|| interim2TeamInfo != null)
										&& (interim1TeamInfo == null || ((Map) interim1TeamInfo).containsKey(teamName)
												|| interim2TeamInfo == null || interim2TeamInfo.containsKey(teamName)));

								teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo.get(teamName);
								if (teamAnalystMapping.isTeamAssignmentDone()) {
									analystTaskStatusMap = teamAnalystMapping.getAnalystTaskStatus();
									this.logger.debug("team " + teamName + " research task status is "
											+ teamAnalystMapping.getResearchTaskStatus());
									if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
										this.logger.debug("pulling back team " + teamName + " analyst " + teamName
												+ " for final cycle");
										analystTaskStatus = (AnalystTaskStatus) analystTaskStatusMap.get(teamName);
										analystTaskStatus.setStatus("");
										analystTaskStatus.setResearchPID(0L);
										dsValues.put("isPullback", true);
										isResearchTaskCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
										this.logger.debug("team " + teamName + "research process status is "
												+ (isResearchTaskCompleted ? "done" : "in progress"));
										if (isResearchTaskCompleted) {
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													(TeamAnalystMapping) null, primaryTeamAnalystMapping));
										} else {
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													teamAnalystMapping, primaryTeamAnalystMapping));
										}

										if (!teamNamesMap.containsKey(teamName)) {
											teamNamesMap.put(teamName, "Final");
										}
									}
								}

								flowNotificationManager.prepareREUpdatedNotification(teamAnalystMapping, teamName,
										deltaCaseDetails.getUpdatedBy());
							}
						}

						if (deltaCaseDetails.isSubjectFlag()) {
							this.logger
									.debug("::::::::::::::::::::::: Pullback because of subject :::::::::::::::::::::");
							subTeamReMapForPullback = deltaCaseDetails.getSubTeamReMapForPullback();
							var79 = subTeamReMapForPullback.iterator();

							label3390 : while (true) {
								do {
									do {
										if (!var79.hasNext()) {
											break label3390;
										}

										SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) var79.next();
										oldMainAnalyst = String.valueOf(subTeamReMap.getTeamId());
										teamName = ResourceLocator.self().getTeamAssignmentService()
												.getTeamType(oldMainAnalyst) + "#" + oldMainAnalyst;
										teamName = subTeamReMap.getPerformer();
										this.logger.debug("impacted analyst " + teamName + " from team " + teamName);
										if (interim1TeamInfo != null && ((Map) interim1TeamInfo).containsKey(teamName)
												&& currentCycle.equals("Interim1")) {
											this.logger.debug("pulling back team " + teamName + " analyst " + teamName
													+ " for interim 1 cycle");
											teamAnalystMapping = (TeamAnalystMapping) ((Map) interim1TeamInfo)
													.get(teamName);
											if (teamAnalystMapping.isTeamAssignmentDone()) {
												analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
												this.logger.debug("team " + teamName + " research task status is "
														+ teamAnalystMapping.getResearchTaskStatus());
												if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
													analystTaskStatus = (AnalystTaskStatus) analystTaskStatus
															.get(teamName);
													analystTaskStatus.setStatus("");
													analystTaskStatus.setResearchPID(0L);
													isResearchProcessCompleted = ResourceLocator.self().getSBMService()
															.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																	session);
													this.logger.debug("team " + teamName + "research process status is "
															+ (isResearchProcessCompleted ? "done" : "in progress"));
													if (isResearchProcessCompleted) {
														dsValues.put("isPullback", true);
														workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid,
																session, (TeamAnalystMapping) null,
																primaryTeamAnalystMapping));
													} else {
														dsValues.put("isPullback", true);
														workstepInfoMap
																.putAll(this.getWorkstepToBeCompleted(pid, session,
																		teamAnalystMapping, primaryTeamAnalystMapping));
													}

													if (!teamNamesMap.containsKey(teamName)) {
														teamNamesMap.put(teamName, "Interim1");
													}
												}
											}
										}

										if (interim2TeamInfo != null && interim2TeamInfo.containsKey(teamName)
												&& (currentCycle.equals("Interim2") || interim1TeamInfo != null
														&& !((Map) interim1TeamInfo).containsKey(teamName))) {
											this.logger.debug("pulling back team " + teamName + " analyst " + teamName
													+ " for interim 2 cycle");
											teamAnalystMapping = (TeamAnalystMapping) interim2TeamInfo.get(teamName);
											if (teamAnalystMapping.isTeamAssignmentDone()) {
												analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
												this.logger.debug("team " + teamName + " research task status is "
														+ teamAnalystMapping.getResearchTaskStatus());
												if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
													analystTaskStatus = (AnalystTaskStatus) analystTaskStatus
															.get(teamName);
													analystTaskStatus.setStatus("");
													analystTaskStatus.setResearchPID(0L);
													isResearchProcessCompleted = ResourceLocator.self().getSBMService()
															.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																	session);
													this.logger.debug("team " + teamName + "research process status is "
															+ (isResearchProcessCompleted ? "done" : "in progress"));
													if (isResearchProcessCompleted) {
														dsValues.put("isPullback", true);
														workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid,
																session, (TeamAnalystMapping) null,
																primaryTeamAnalystMapping));
													} else {
														dsValues.put("isPullback", true);
														workstepInfoMap
																.putAll(this.getWorkstepToBeCompleted(pid, session,
																		teamAnalystMapping, primaryTeamAnalystMapping));
													}

													if (!teamNamesMap.containsKey(teamName)) {
														teamNamesMap.put(teamName, "Interim2");
													}
												}
											}
										}
									} while (finalTeamInfo == null);
								} while (!currentCycle.equals("Final")
										&& (interim1TeamInfo == null || ((Map) interim1TeamInfo).containsKey(teamName)
												|| interim2TeamInfo != null)
										&& (interim1TeamInfo == null || ((Map) interim1TeamInfo).containsKey(teamName)
												|| interim2TeamInfo == null || interim2TeamInfo.containsKey(teamName)));

								this.logger.debug(
										"pulling back team " + teamName + " analyst " + teamName + " for final cycle");
								teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo.get(teamName);
								if (teamAnalystMapping.isTeamAssignmentDone()) {
									analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
									this.logger.debug("team " + teamName + " research task status is "
											+ teamAnalystMapping.getResearchTaskStatus());
									if (teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
										analystTaskStatus = (AnalystTaskStatus) analystTaskStatus.get(teamName);
										analystTaskStatus.setStatus("");
										analystTaskStatus.setResearchPID(0L);
										isResearchProcessCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
										this.logger.debug("team " + teamName + "research process status is "
												+ (isResearchProcessCompleted ? "done" : "in progress"));
										if (isResearchProcessCompleted) {
											dsValues.put("isPullback", true);
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													(TeamAnalystMapping) null, primaryTeamAnalystMapping));
										} else {
											dsValues.put("isPullback", true);
											workstepInfoMap.putAll(this.getWorkstepToBeCompleted(pid, session,
													teamAnalystMapping, primaryTeamAnalystMapping));
										}

										if (!teamNamesMap.containsKey(teamName)) {
											teamNamesMap.put(teamName, "Final");
										}
									}
								}
							}
						}

						parentReviewersMap = new HashMap();
						childReviewersMap = new HashMap();
						Iterator var103;
						int reviewerIndex;
						if (deltaCaseDetails.isReviewerFlag()) {
							this.logger.debug(
									"::::::::::::::::::::::: Pullback because of reviewer :::::::::::::::::::::");
							deletedReviewers = deltaCaseDetails.getDeletedReviewers();

							for (int i = 0; i < deletedReviewers.size(); ++i) {
								this.logger.debug("deletedReviewers::" + (String) deletedReviewers.get(i));
							}

							var99 = deletedReviewers.iterator();

							HashMap reviewDSValues;
							label3344 : while (var99.hasNext()) {
								oldMainAnalyst = (String) var99.next();
								teamName = oldMainAnalyst.split("::")[0].split("@")[0];
								teamName = oldMainAnalyst.split("::")[1];
								this.logger.debug("deleting reviewer " + teamName + " from team " + teamName);
								reviewer = teamName.split("#")[0];
								int reviewerIndex = Integer.valueOf(teamName.split("#")[1]);
								Set<String> interimsList = cycleInformation.keySet();
								Iterator var122 = interimsList.iterator();

								while (true) {
									TeamAnalystMapping teamAnalystMapping;
									do {
										if (!var122.hasNext()) {
											continue label3344;
										}

										interimName = (String) var122.next();
										teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation
												.get(interimName)).getTeamInfo().get(teamName);
									} while (teamAnalystMapping == null);

									if (teamAnalystMapping.isTeamAssignmentDone()) {
										isResearchProcessCompleted = ResourceLocator.self().getSBMService()
												.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
										this.logger.debug("team " + teamName + "research process status is "
												+ (isResearchProcessCompleted ? "done" : "in progress"));
										if (!isResearchProcessCompleted || teamName.contains("Primary")) {
											new Vector();
											long pidToBeUpdated = 0L;
											if (teamName.contains("Primary")) {
												pidToBeUpdated = pid;
												this.logger.debug("pid updated::" + pid);
											} else {
												pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
												this.logger.debug("pid updated::" + pidToBeUpdated);
											}

											Iterator var151 = parentReviewersMap.entrySet().iterator();

											while (var151.hasNext()) {
												Entry<String, Vector<String>> map = (Entry) var151.next();
												this.logger.debug("Map info with key-value::" + (String) map.getKey()
														+ ".." + map.getValue());
											}

											Vector reviewers;
											if (parentReviewersMap.containsKey(teamName + "::" + pidToBeUpdated)) {
												reviewers = (Vector) parentReviewersMap
														.get(teamName + "::" + pidToBeUpdated);
												this.logger.debug("Reviewers in if:::" + reviewers);
											} else {
												reviewers = (Vector) ResourceLocator.self().getSBMService()
														.getDataslotValue(pidToBeUpdated, "Reviewers", session);
												this.logger.debug("Reviewers in else:::" + reviewers);
											}

											if (reviewers != null) {
												HashMap<String, Object> reviewDSValues = new HashMap();
												this.logger.debug(
														"before remove:::" + reviewers + "for reviewer ..." + reviewer);
												this.removeReviewer(reviewer, reviewers);
												this.logger.debug("after remove:::" + reviewers);
												reviewDSValues.put("Reviewers", reviewers);
												this.logger.debug("reviewDSValues.." + reviewDSValues);
												this.logger
														.debug("adding reviewers list to parentReviewersMap with key : "
																+ teamName + "::" + pidToBeUpdated);
												parentReviewersMap.put(teamName + "::" + pidToBeUpdated, reviewers);
											}

											this.logger.debug("parent Reviewer map::" + parentReviewersMap);
											boolean isReviewActivated = false;
											String[] activatedWSNames = new String[0];
											if (teamName.contains("Primary")) {
												activatedWSNames = ResourceLocator.self().getSBMService()
														.getActivatedWSNames(session, pid);
											} else {
												activatedWSNames = ResourceLocator.self().getSBMService()
														.getActivatedWSNames(session,
																teamAnalystMapping.getResearchProcessPID());
											}

											for (int i = 0; i < activatedWSNames.length; ++i) {
												this.logger.debug("activated workstep for team " + teamName + " is "
														+ activatedWSNames[i]);
												if (activatedWSNames[i].equals("Review")
														|| activatedWSNames[i].equals("FinalReview")) {
													isReviewActivated = true;
													break;
												}
											}

											this.logger.debug("isReviewActivated for team " + teamName + ": "
													+ isReviewActivated);
											if (isReviewActivated) {
												this.logger.debug(
														"review process pid for " + teamName + " for " + interimName
																+ " is " + teamAnalystMapping.getReviewProcessPID());
												if (teamAnalystMapping.getReviewProcessPID() != 0L) {
													reviewDSValues = new HashMap();
													if (childReviewersMap
															.containsKey(teamAnalystMapping.getReviewProcessPID())) {
														reviewDSValues = (HashMap) childReviewersMap
																.get(teamAnalystMapping.getReviewProcessPID());
													}

													new Vector();
													Vector reviewersForReviewProcess;
													if (reviewDSValues.containsKey("Reviewers")) {
														reviewersForReviewProcess = (Vector) reviewDSValues
																.get("Reviewers");
													} else {
														reviewersForReviewProcess = (Vector) ResourceLocator.self()
																.getSBMService().getDataslotValue(
																		teamAnalystMapping.getReviewProcessPID(),
																		"Reviewers", session);
													}

													String currentReviewer = (String) ResourceLocator.self()
															.getSBMService()
															.getDataslotValue(teamAnalystMapping.getReviewProcessPID(),
																	"Reviewer", session);
													if (reviewersForReviewProcess.contains(reviewer)) {
														this.removeReviewer(reviewer, reviewersForReviewProcess);
													} else if (currentReviewer.equals(reviewer)) {
														reviewDSValues.put("isDeleted", true);
														workstepInfoMap.put(
																"Review Task#"
																		+ teamAnalystMapping.getReviewProcessPID(),
																String.valueOf(teamAnalystMapping.getReviewProcessPID()
																		+ "#" + currentReviewer));
													}

													reviewDSValues.put("Reviewers", reviewersForReviewProcess);
													childReviewersMap.put(teamAnalystMapping.getReviewProcessPID(),
															reviewDSValues);
												}
											}
										}
									}

									if (!this.isReviewerIndexChanged(reviewer, teamName,
											deltaCaseDetails.getAddedReviewers())) {
										flowNotificationManager.prepareReviewerRemovedNotification(teamAnalystMapping,
												reviewer);
									}

									teamAnalystMapping.getReviewers().remove(reviewer);
								}
							}

							List<String> addedReviewers = deltaCaseDetails.getAddedReviewers();
							var103 = addedReviewers.iterator();

							while (var103.hasNext()) {
								teamName = (String) var103.next();
								this.logger.debug("add Reviewer::" + teamName);
							}

							var103 = addedReviewers.iterator();

							label3301 : while (true) {
								LinkedHashMap reviewersMap;
								TeamAnalystMapping teamAnalystMapping;
								ReviewStatusMapping reviewStatusMapping;
								String[] activatedWSNames;
								boolean isReviewActivated;
								int i;
								HashMap reviewDSValues;
								Vector reviewers;
								int currentReviewerIndex;
								long pidToBeUpdated;
								Vector reviewersForReviewProcess;
								String reviewerName;
								Iterator var165;
								do {
									do {
										if (!var103.hasNext()) {
											break label3301;
										}

										teamName = (String) var103.next();
										teamName = teamName.split("::")[0].split("@")[0];
										reviewer = teamName.split("::")[1];
										reviewer = reviewer.split("#")[0];
										reviewerIndex = Integer.valueOf(reviewer.split("#")[1]);
										if (interim1TeamInfo != null && ((Map) interim1TeamInfo).containsKey(teamName)
												&& !currentCycle.equals("Interim2") && !currentCycle.equals("Final")) {
											teamAnalystMapping = (TeamAnalystMapping) ((Map) interim1TeamInfo)
													.get(teamName);
											if (teamAnalystMapping.isTeamAssignmentDone()) {
												this.logger.debug("adding reviewer " + reviewer + " to team " + teamName
														+ " for interim 1 cycle");
												reviewersMap = teamAnalystMapping.getReviewers();
												reviewStatusMapping = new ReviewStatusMapping();
												reviewStatusMapping.setStatus("");
												isResearchProcessCompleted = ResourceLocator.self().getSBMService()
														.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																session);
												this.logger.debug("team " + teamName + "research process status is "
														+ (isResearchProcessCompleted ? "done" : "in progress"));
												if (!isResearchProcessCompleted || teamName.contains("Primary")) {
													isReviewActivated = false;
													activatedWSNames = new String[0];
													if (teamName.contains("Primary")) {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(session, pid);
													} else {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(session,
																		teamAnalystMapping.getResearchProcessPID());
													}

													for (i = 0; i < activatedWSNames.length; ++i) {
														this.logger.debug("activated workstep for team " + teamName
																+ " is " + activatedWSNames[i]);
														if (activatedWSNames[i].equals("Review")
																|| activatedWSNames[i].equals("FinalReview")) {
															isReviewActivated = true;
															break;
														}
													}

													this.logger.debug("isReviewActivated for team " + teamName + ": "
															+ isReviewActivated);
													if (isReviewActivated && currentCycle.equals("Interim1")) {
														this.logger.debug("review process pid for " + teamName
																+ " for interim1 cycle is "
																+ teamAnalystMapping.getReviewProcessPID());
														if (teamAnalystMapping.getReviewProcessPID() != 0L) {
															reviewStatusMapping.setResearchPID(
																	teamAnalystMapping.getReviewProcessPID());
															reviewDSValues = new HashMap();
															if (childReviewersMap.containsKey(
																	teamAnalystMapping.getReviewProcessPID())) {
																reviewDSValues = (HashMap) childReviewersMap
																		.get(teamAnalystMapping.getReviewProcessPID());
															}

															currentReviewerIndex = this.getCurrentReviewerIndex(session,
																	teamAnalystMapping, pid);
															if (reviewerIndex >= currentReviewerIndex) {
																new Vector();
																if (reviewDSValues.containsKey("Reviewers")) {
																	reviewersForReviewProcess = (Vector) reviewDSValues
																			.get("Reviewers");
																} else {
																	reviewersForReviewProcess = (Vector) ResourceLocator
																			.self().getSBMService().getDataslotValue(
																					teamAnalystMapping
																							.getReviewProcessPID(),
																					"Reviewers", session);
																}

																if (!reviewersForReviewProcess.contains(reviewer)) {
																	if (reviewersForReviewProcess
																			.size() > reviewerIndex) {
																		reviewersForReviewProcess.add(reviewerIndex,
																				reviewer);
																	} else {
																		reviewersForReviewProcess.add(reviewer);
																	}

																	reviewDSValues.put("Reviewers",
																			reviewersForReviewProcess);
																	childReviewersMap.put(
																			teamAnalystMapping.getReviewProcessPID(),
																			reviewDSValues);
																}
															}
														}
													}

													new Vector();
													pidToBeUpdated = 0L;
													if (teamName.contains("Primary")) {
														pidToBeUpdated = pid;
													} else {
														pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
													}

													if (parentReviewersMap
															.containsKey(teamName + "::" + pidToBeUpdated)) {
														reviewers = (Vector) parentReviewersMap
																.get(teamName + "::" + pidToBeUpdated);
														this.logger.debug("Anurag Reviewers in if::" + reviewers);
													} else {
														reviewers = (Vector) ResourceLocator.self().getSBMService()
																.getDataslotValue(pidToBeUpdated, "Reviewers", session);
														this.logger.debug("Anurag Reviewers in else::" + reviewers);
														if (reviewers == null) {
															reviewers = new Vector();
															var165 = reviewersMap.keySet().iterator();

															while (var165.hasNext()) {
																reviewerName = (String) var165.next();
																reviewers.add(reviewerName);
															}
														}
													}

													if (reviewers != null && !reviewers.contains(reviewer)) {
														reviewDSValues = new HashMap();
														if (reviewers.size() > reviewerIndex) {
															reviewers.add(reviewerIndex, reviewer);
															this.logger.debug("Anurag Add reviewer if in reviewrers:::"
																	+ reviewers + "..." + reviewer);
														} else {
															reviewers.add(reviewer);
															this.logger.debug("Anurag Add reviewer if in reviewrers:::"
																	+ reviewers + "..." + reviewer);
														}

														this.logger.debug("Anurag final reviewrers:::" + reviewers);
														reviewDSValues.put("Reviewers", reviewers);
														this.logger.debug(
																"adding reviewers list to parentReviewersMap with key : "
																		+ teamName + "::" + pidToBeUpdated);
														parentReviewersMap.put(teamName + "::" + pidToBeUpdated,
																reviewers);
													}
												}

												this.logger.debug("parentReviewersMap:::" + parentReviewersMap);
												reviewersMap.put(reviewer, reviewStatusMapping);
												this.logger.debug("reviewersMap:::" + reviewersMap);
											}
										}

										if (interim2TeamInfo != null && interim2TeamInfo.containsKey(teamName)
												&& !currentCycle.equals("Final")) {
											teamAnalystMapping = (TeamAnalystMapping) interim2TeamInfo.get(teamName);
											if (teamAnalystMapping.isTeamAssignmentDone()) {
												this.logger.debug("adding reviewer " + reviewer + " to team " + teamName
														+ " for interim 2 cycle");
												reviewersMap = teamAnalystMapping.getReviewers();
												reviewStatusMapping = new ReviewStatusMapping();
												reviewStatusMapping.setStatus("");
												isResearchProcessCompleted = ResourceLocator.self().getSBMService()
														.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(),
																session);
												this.logger.debug("team " + teamName + "research process status is "
														+ (isResearchProcessCompleted ? "done" : "in progress"));
												if (!isResearchProcessCompleted || teamName.contains("Primary")) {
													isReviewActivated = false;
													activatedWSNames = new String[0];
													if (teamName.contains("Primary")) {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(session, pid);
													} else {
														activatedWSNames = ResourceLocator.self().getSBMService()
																.getActivatedWSNames(session,
																		teamAnalystMapping.getResearchProcessPID());
													}

													for (i = 0; i < activatedWSNames.length; ++i) {
														this.logger.debug("activated workstep for team " + teamName
																+ " is " + activatedWSNames[i]);
														if (activatedWSNames[i].equals("Review")
																|| activatedWSNames[i].equals("FinalReview")) {
															isReviewActivated = true;
															break;
														}
													}

													this.logger.debug("isReviewActivated for team " + teamName + ": "
															+ isReviewActivated);
													if (isReviewActivated && currentCycle.equals("Interim2")) {
														this.logger.debug("review process pid for " + teamName
																+ " for interim2 cycle is "
																+ teamAnalystMapping.getReviewProcessPID());
														if (teamAnalystMapping.getReviewProcessPID() != 0L) {
															reviewStatusMapping.setResearchPID(
																	teamAnalystMapping.getReviewProcessPID());
															reviewDSValues = new HashMap();
															if (childReviewersMap.containsKey(
																	teamAnalystMapping.getReviewProcessPID())) {
																reviewDSValues = (HashMap) childReviewersMap
																		.get(teamAnalystMapping.getReviewProcessPID());
															}

															currentReviewerIndex = this.getCurrentReviewerIndex(session,
																	teamAnalystMapping, pid);
															if (reviewerIndex >= currentReviewerIndex) {
																new Vector();
																if (reviewDSValues.containsKey("Reviewers")) {
																	reviewersForReviewProcess = (Vector) reviewDSValues
																			.get("Reviewers");
																} else {
																	reviewersForReviewProcess = (Vector) ResourceLocator
																			.self().getSBMService().getDataslotValue(
																					teamAnalystMapping
																							.getReviewProcessPID(),
																					"Reviewers", session);
																}

																if (!reviewersForReviewProcess.contains(reviewer)) {
																	if (reviewersForReviewProcess
																			.size() > reviewerIndex) {
																		reviewersForReviewProcess.add(reviewerIndex,
																				reviewer);
																	} else {
																		reviewersForReviewProcess.add(reviewer);
																	}

																	reviewDSValues.put("Reviewers",
																			reviewersForReviewProcess);
																	childReviewersMap.put(
																			teamAnalystMapping.getReviewProcessPID(),
																			reviewDSValues);
																}
															}
														}
													}

													new Vector();
													pidToBeUpdated = 0L;
													if (teamName.contains("Primary")) {
														pidToBeUpdated = pid;
													} else {
														pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
													}

													if (parentReviewersMap
															.containsKey(teamName + "::" + pidToBeUpdated)) {
														reviewers = (Vector) parentReviewersMap
																.get(teamName + "::" + pidToBeUpdated);
													} else {
														reviewers = (Vector) ResourceLocator.self().getSBMService()
																.getDataslotValue(pidToBeUpdated, "Reviewers", session);
														if (reviewers == null) {
															reviewers = new Vector();
															var165 = reviewersMap.keySet().iterator();

															while (var165.hasNext()) {
																reviewerName = (String) var165.next();
																reviewers.add(reviewerName);
															}
														}
													}

													if (reviewers != null && !reviewers.contains(reviewer)) {
														reviewDSValues = new HashMap();
														if (reviewers.size() > reviewerIndex) {
															reviewers.add(reviewerIndex, reviewer);
														} else {
															reviewers.add(reviewer);
														}

														reviewDSValues.put("Reviewers", reviewers);
														this.logger.debug(
																"adding reviewers list to parentReviewersMap with key : "
																		+ teamName + "::" + pidToBeUpdated);
														parentReviewersMap.put(teamName + "::" + pidToBeUpdated,
																reviewers);
													}
												}

												reviewersMap.put(reviewer, reviewStatusMapping);
											}
										}
									} while (finalTeamInfo == null);

									teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo.get(teamName);
								} while (!teamAnalystMapping.isTeamAssignmentDone());

								this.logger.debug(
										"adding reviewer " + reviewer + " to team " + teamName + " for final cycle");
								reviewersMap = teamAnalystMapping.getReviewers();
								reviewStatusMapping = new ReviewStatusMapping();
								reviewStatusMapping.setStatus("");
								isResearchProcessCompleted = ResourceLocator.self().getSBMService()
										.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
								this.logger.debug("team " + teamName + "research process status is "
										+ (isResearchProcessCompleted ? "done" : "in progress"));
								if (!isResearchProcessCompleted || teamName.contains("Primary")) {
									isReviewActivated = false;
									activatedWSNames = new String[0];
									if (teamName.contains("Primary")) {
										activatedWSNames = ResourceLocator.self().getSBMService()
												.getActivatedWSNames(session, pid);
									} else {
										activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(
												session, teamAnalystMapping.getResearchProcessPID());
									}

									for (i = 0; i < activatedWSNames.length; ++i) {
										this.logger.debug("activated workstep for team " + teamName + " is "
												+ activatedWSNames[i]);
										if (activatedWSNames[i].equals("Review")
												|| activatedWSNames[i].equals("FinalReview")) {
											isReviewActivated = true;
											break;
										}
									}

									this.logger
											.debug("isReviewActivated for team " + teamName + ": " + isReviewActivated);
									if (isReviewActivated && currentCycle.equals("Final")) {
										this.logger.debug("review process pid for " + teamName + " for final cycle is "
												+ teamAnalystMapping.getReviewProcessPID());
										if (teamAnalystMapping.getReviewProcessPID() != 0L) {
											reviewStatusMapping
													.setResearchPID(teamAnalystMapping.getReviewProcessPID());
											reviewDSValues = new HashMap();
											if (childReviewersMap
													.containsKey(teamAnalystMapping.getReviewProcessPID())) {
												reviewDSValues = (HashMap) childReviewersMap
														.get(teamAnalystMapping.getReviewProcessPID());
											}

											currentReviewerIndex = this.getCurrentReviewerIndex(session,
													teamAnalystMapping, pid);
											if (reviewerIndex >= currentReviewerIndex) {
												new Vector();
												if (reviewDSValues.containsKey("Reviewers")) {
													reviewersForReviewProcess = (Vector) reviewDSValues
															.get("Reviewers");
												} else {
													reviewersForReviewProcess = (Vector) ResourceLocator.self()
															.getSBMService()
															.getDataslotValue(teamAnalystMapping.getReviewProcessPID(),
																	"Reviewers", session);
												}

												if (!reviewersForReviewProcess.contains(reviewer)) {
													if (reviewersForReviewProcess.size() > reviewerIndex) {
														reviewersForReviewProcess.add(reviewerIndex, reviewer);
													} else {
														reviewersForReviewProcess.add(reviewer);
													}

													reviewDSValues.put("Reviewers", reviewersForReviewProcess);
													childReviewersMap.put(teamAnalystMapping.getReviewProcessPID(),
															reviewDSValues);
												}
											}
										}
									}

									new Vector();
									pidToBeUpdated = 0L;
									if (teamName.contains("Primary")) {
										pidToBeUpdated = pid;
									} else {
										pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
									}

									if (parentReviewersMap.containsKey(teamName + "::" + pidToBeUpdated)) {
										reviewers = (Vector) parentReviewersMap.get(teamName + "::" + pidToBeUpdated);
										this.logger.debug("key:::" + teamName + "::" + pidToBeUpdated
												+ "  in parentReviewersMap if condition:::" + reviewers);
									} else {
										reviewers = (Vector) ResourceLocator.self().getSBMService()
												.getDataslotValue(pidToBeUpdated, "Reviewers", session);
										this.logger.debug(
												"in else condition when parentReviewersMap does not contain key::: "
														+ reviewers);
										if (reviewers == null) {
											this.logger.debug("in chetan code::");
											reviewers = new Vector();
											var165 = reviewersMap.keySet().iterator();

											while (var165.hasNext()) {
												reviewerName = (String) var165.next();
												this.logger.debug("in chetan code reviewerName::" + reviewerName);
												reviewers.add(reviewerName);
											}
										}
									}

									this.logger
											.debug("reviewers map ::" + reviewers + " reviewer to add:::" + reviewer);
									if (reviewers != null && !reviewers.contains(reviewer)) {
										reviewDSValues = new HashMap();
										if (reviewers.size() > reviewerIndex) {
											this.logger
													.debug("in if condition when reviewers size is greater then zero::"
															+ reviewer);
											reviewers.add(reviewerIndex, reviewer);
											this.logger.debug("reviewers in if when not null::" + reviewers);
										} else {
											this.logger.debug("in else condition reviwer to add::" + reviewer);
											reviewers.add(reviewer);
											this.logger.debug("in else condition when not null::" + reviewer);
										}

										reviewDSValues.put("Reviewers", reviewers);
										this.logger.debug("adding reviewers list to parentReviewersMap with key : "
												+ teamName + "::" + pidToBeUpdated);
										this.logger
												.debug("reviewers before adding in parentReviewersMap::" + reviewers);
										parentReviewersMap.put(teamName + "::" + pidToBeUpdated, reviewers);
									}
								}

								this.logger.debug("in reasearch task completed for final cycle condition::: ");
								this.logger.debug("reviewer::" + reviewer);
								reviewersMap.put(reviewer, reviewStatusMapping);
							}
						}

						Set<String> teamNamesList = teamNamesMap.keySet();
						var99 = teamNamesList.iterator();

						while (var99.hasNext()) {
							oldMainAnalyst = (String) var99.next();
							teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleTeamMapping
									.getCycleInformation().get(teamNamesMap.get(oldMainAnalyst))).getTeamInfo()
											.get(oldMainAnalyst);
							teamAnalystMapping.setResearchTaskStatus("");
						}

						boolean signalToGoAhead = true;
						var103 = affectedTeamsMap.keySet().iterator();

						while (true) {
							Iterator var138;
							if (!var103.hasNext()) {
								HashMap<String, Boolean> goToDsValues = new HashMap();
								HashMap<String, Object> caseCreationDSValues = new HashMap();
								startConsolidation = false;
								if (dsValues.containsKey("isPullback") || deltaCaseDetails.isTeamFlag()
										&& deltaCaseDetails.getDeletedTeamsMap().size() != 0) {
									startConsolidation = this.isStartConsolidation(pid, session, currentCycle,
											cycleInformation, goToDsValues);
									if (startConsolidation) {
										caseCreationDSValues.putAll(goToDsValues);
									} else if (this.isPrimaryReviewComplete(session, pid)
											&& dsValues.containsKey("isPullback")) {
										teamNamesMap.put(primaryTeamAnalystMapping.getTeamName(), currentCycle);
									}

									ResourceLocator.self().getOfficeAssignmentService()
											.resetTeamResearchCMPDates(deltaCaseDetails.getCrn(), teamNamesMap);
								}

								this.logger.debug("deltaCaseDetails.isPrimarySubjectFlag() : "
										+ deltaCaseDetails.isPrimarySubjectFlag());
								if (deltaCaseDetails.isPrimarySubjectFlag()) {
									this.updatePrimarySubjectDataslots(pid, session, deltaCaseDetails,
											cycleInformation);
								}

								this.logger.debug("deltaCaseDetails.isResearchDueDateFlag() : "
										+ deltaCaseDetails.isResearchDueDateFlag());
								if (deltaCaseDetails.isResearchDueDateFlag()) {
									this.updateResearchDueDateDataslots(pid, session, deltaCaseDetails,
											cycleInformation);
								}

								this.logger.debug("deltaCaseDetails.isDueDateUpdatedFlag() : "
										+ deltaCaseDetails.isDueDateUpdatedFlag());
								HashMap startConsolidationDSValues;
								String cycleName;
								if (deltaCaseDetails.isDueDateUpdatedFlag()) {
									startConsolidationDSValues = new HashMap();
									var138 = deltaCaseDetails.getUpdatedDueDatesList().iterator();

									label3139 : while (true) {
										while (true) {
											String updatedRFinal;
											TeamAnalystMapping team;
											do {
												do {
													if (!var138.hasNext()) {
														this.updateTeamDueDateDataslots(pid, session, deltaCaseDetails,
																cycleInformation, startConsolidationDSValues);
														break label3139;
													}

													reviewer = (String) var138.next();
													interimName = reviewer.split("::")[0];
													teamName = reviewer.split("::")[1];
													teamName = interimName.split("@")[0];
													this.logger.debug("teamNameKey - " + interimName);
													String updatedRInterim1 = teamName.split("#")[0];
													String updatedRInterim2 = teamName.split("#")[1];
													updatedRFinal = teamName.split("#")[2];
													if (!updatedRInterim1.equalsIgnoreCase("null")
															&& !currentCycle.equals("Interim2")
															&& !currentCycle.equals("Final")) {
														cycleName = "Interim1";
														team = (TeamAnalystMapping) ((CycleInfo) cycleInformation
																.get(cycleName)).getTeamInfo().get(teamName);
														this.logger.debug("Interim1 team.getClientDueDate() :: "
																+ team.getClientDueDate());
														if (!team.getClientDueDate().equals(updatedRInterim1)) {
															team.setClientDueDate(updatedRInterim1);
															startConsolidationDSValues.put(teamName, teamName);
															if (!team.getTeamName().contains("Primary") && !team
																	.getTeamName().contains("Supporting - Internal")) {
																if (team.getTeamName().contains("BI")) {
																	flowNotificationManager
																			.prepareBIDueDateUpdatedNotification(team,
																					this.getPrimaryTeamForCycle(
																							cycleInformation,
																							cycleName));
																} else if (team.getTeamName().contains("Vendor")) {
																	flowNotificationManager
																			.prepareVendorDueDateUpdatedNotification(
																					team,
																					this.getPrimaryTeamForCycle(
																							cycleInformation,
																							cycleName));
																}
															} else {
																flowNotificationManager
																		.prepareTeamDueDateUpdatedNotification(team,
																				cycleName);
															}
														}
													}

													if (!updatedRInterim2.equalsIgnoreCase("null")
															&& !currentCycle.equals("Final")) {
														cycleName = "Interim2";
														team = (TeamAnalystMapping) ((CycleInfo) cycleInformation
																.get(cycleName)).getTeamInfo().get(teamName);
														this.logger.debug("Interim2 team.getClientDueDate() :: "
																+ team.getClientDueDate());
														if (!team.getClientDueDate().equals(updatedRInterim2)) {
															team.setClientDueDate(updatedRInterim2);
															startConsolidationDSValues.put(teamName, teamName);
															if (!team.getTeamName().contains("Primary") && !team
																	.getTeamName().contains("Supporting - Internal")) {
																if (team.getTeamName().contains("BI")) {
																	flowNotificationManager
																			.prepareBIDueDateUpdatedNotification(team,
																					this.getPrimaryTeamForCycle(
																							cycleInformation,
																							cycleName));
																} else if (team.getTeamName().contains("Vendor")) {
																	flowNotificationManager
																			.prepareVendorDueDateUpdatedNotification(
																					team,
																					this.getPrimaryTeamForCycle(
																							cycleInformation,
																							cycleName));
																}
															} else {
																flowNotificationManager
																		.prepareTeamDueDateUpdatedNotification(team,
																				cycleName);
															}
														}
													}
												} while (updatedRFinal.equalsIgnoreCase("null"));

												cycleName = "Final";
												team = (TeamAnalystMapping) ((CycleInfo) cycleInformation
														.get(cycleName)).getTeamInfo().get(teamName);
												this.logger.debug(
														"Final team.getClientDueDate() :: " + team.getClientDueDate());
											} while (team.getClientDueDate().equals(updatedRFinal));

											team.setClientDueDate(updatedRFinal);
											startConsolidationDSValues.put(teamName, teamName);
											if (!team.getTeamName().contains("Primary")
													&& !team.getTeamName().contains("Supporting - Internal")) {
												if (team.getTeamName().contains("BI")) {
													flowNotificationManager.prepareBIDueDateUpdatedNotification(team,
															this.getPrimaryTeamForCycle(cycleInformation, cycleName));
												} else if (team.getTeamName().contains("Vendor")) {
													flowNotificationManager.prepareVendorDueDateUpdatedNotification(
															team,
															this.getPrimaryTeamForCycle(cycleInformation, cycleName));
												}
											} else {
												flowNotificationManager.prepareTeamDueDateUpdatedNotification(team,
														cycleName);
											}
										}
									}
								}

								if (deltaCaseDetails.isCaseManagerFlag()) {
									this.logger.debug(
											"::::::::::::::::::::::: Change in case manager :::::::::::::::::::::");
									activatedWSNames = ResourceLocator.self().getSBMService()
											.getActivatedWSNames(session, pid);
									reviewer = null;

									for (reviewerIndex = 0; reviewerIndex < activatedWSNames.length; ++reviewerIndex) {
										this.logger.debug("activated workstep for case creation process is "
												+ activatedWSNames[reviewerIndex]);
										if (activatedWSNames[reviewerIndex].equals("Office Assignment Task")
												|| activatedWSNames[reviewerIndex].equals("Client Submission Task")) {
											reviewer = activatedWSNames[reviewerIndex];
											break;
										}
									}

									oldCM = deltaCaseDetails.getCaseMgrId().split("#")[0];
									interimName = deltaCaseDetails.getCaseMgrId().split("#")[1];
									if (reviewer != null) {
										this.logger.debug(
												"reassigning " + reviewer + " for pid " + pid + " to " + interimName);
										this.stopTimeTracker(reviewer, String.valueOf(pid), deltaCaseDetails.getCrn(),
												oldCM, deltaCaseDetails.getUpdatedBy());
										ResourceLocator.self().getSBMService().reassignTask(pid, interimName, reviewer,
												session);
									}

									caseCreationDSValues.put("CaseManager", interimName);
								}

								var138 = mainAnalystMap.keySet().iterator();

								long pidToBeUpdated;
								while (var138.hasNext()) {
									pidToBeUpdated = (Long) var138.next();
									HashMap<String, Object> mainAnalystDSValues = new HashMap();
									teamName = ((String) mainAnalystMap.get(pidToBeUpdated)).split("#")[0];
									teamName = ((String) mainAnalystMap.get(pidToBeUpdated)).split("#")[1];
									mainAnalystDSValues.put("MainAnalyst", teamName);
									this.logger
											.debug("updating main analyst " + teamName + " for pid " + pidToBeUpdated);
									ResourceLocator.self().getSBMService().updateDataSlots(session, pidToBeUpdated,
											mainAnalystDSValues);
									if ((Boolean) reassignConsolidationMap.get(pidToBeUpdated)) {
										this.stopTimeTracker("Consolidation Task", String.valueOf(pidToBeUpdated),
												deltaCaseDetails.getCrn(), teamName, deltaCaseDetails.getUpdatedBy());
										ResourceLocator.self().getSBMService().reassignTask(pidToBeUpdated, teamName,
												"Consolidation Task", session);
									}
								}

								var106 = parentReviewersMap.keySet().iterator();

								long pidToBeCompleted;
								while (var106.hasNext()) {
									reviewer = (String) var106.next();
									pidToBeCompleted = Long.valueOf(reviewer.split("::")[1]);
									teamName = reviewer.split("::")[0];
									HashMap<String, Object> reviewDSValues = new HashMap();
									Vector<String> reviewers = (Vector) parentReviewersMap.get(reviewer);
									LinkedHashMap<String, ReviewStatusMapping> newReviewersMap = new LinkedHashMap();
									LinkedHashMap<String, ReviewStatusMapping> oldReviewersMap = ((TeamAnalystMapping) finalTeamInfo
											.get(teamName)).getReviewers();
									Iterator var168 = reviewers.iterator();

									while (var168.hasNext()) {
										cycleName = (String) var168.next();
										this.logger.debug("updated reviewer :: " + cycleName);
										newReviewersMap.put(cycleName,
												(ReviewStatusMapping) oldReviewersMap.get(cycleName));
									}

									var168 = cycleInformation.keySet().iterator();

									while (var168.hasNext()) {
										cycleName = (String) var168.next();
										((TeamAnalystMapping) ((CycleInfo) cycleInformation.get(cycleName))
												.getTeamInfo().get(teamName)).setReviewers(newReviewersMap);
									}

									reviewDSValues.put("Reviewers", parentReviewersMap.get(reviewer));
									ResourceLocator.self().getSBMService().updateDataSlots(session, pidToBeCompleted,
											reviewDSValues);
								}

								var138 = childReviewersMap.keySet().iterator();

								while (var138.hasNext()) {
									pidToBeUpdated = (Long) var138.next();
									ResourceLocator.self().getSBMService().updateDataSlots(session, pidToBeUpdated,
											(HashMap) childReviewersMap.get(pidToBeUpdated));
								}

								customDSMap.put("CycleTeamMapping", cycleTeamMapping);
								caseCreationDSValues.put("customDSMap", customDSMap);
								this.logger.debug("before updating customDSMap for pid " + pid);
								ResourceLocator.self().getSBMService().updateDataSlots(session, pid,
										caseCreationDSValues);
								Thread.sleep(2000L);
								this.completeWorksteps(workstepInfoMap, session, dsValues, deltaCaseDetails.getCrn(),
										deltaCaseDetails.getUpdatedBy());
								var106 = workstepInfoMap.keySet().iterator();

								while (var106.hasNext()) {
									reviewer = (String) var106.next();
									pidToBeCompleted = Long.valueOf(reviewer.split("#")[1]);
									if (!deltaCaseDetails.getRemovedPids().contains(pidToBeCompleted)) {
										deltaCaseDetails.getRemovedPids().add(pidToBeCompleted);
									}
								}

								if (dsValues.containsKey("isPullback")) {
									deltaCaseDetails.setPullbackFlag(true);
								}

								if (startConsolidation) {
									this.logger.debug("putting startConsolidation " + startConsolidation
											+ " in case creation ds values");
									startConsolidationDSValues = new HashMap();
									startConsolidationDSValues.put("startConsolidation", startConsolidation);
									ResourceLocator.self().getSBMService().updateDataSlots(session, pid,
											startConsolidationDSValues);
								}
								break;
							}

							teamName = (String) var103.next();
							teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleInformation
									.get(affectedTeamsMap.get(teamName))).getTeamInfo().get(teamName);
							analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
							if (analystTaskStatus.keySet().size() == 0) {
								signalToGoAhead = false;
							} else {
								var138 = analystTaskStatus.keySet().iterator();

								while (var138.hasNext()) {
									reviewer = (String) var138.next();
									this.logger.debug("analyst research task status is "
											+ ((AnalystTaskStatus) analystTaskStatus.get(reviewer)).getStatus());
									this.logger.debug("analyst research task pid is "
											+ ((AnalystTaskStatus) analystTaskStatus.get(reviewer)).getResearchPID());
									if (((AnalystTaskStatus) analystTaskStatus.get(reviewer)).getResearchPID() == 0L
											|| "".equals(((AnalystTaskStatus) analystTaskStatus.get(reviewer))
													.getStatus())) {
										signalToGoAhead = false;
										break;
									}

									isResearchTaskCompleted = ResourceLocator.self().getSBMService().isTaskCompleted(
											((AnalystTaskStatus) analystTaskStatus.get(reviewer)).getResearchPID(),
											session);
									this.logger.debug("analyst " + reviewer + " research task pid "
											+ ((AnalystTaskStatus) analystTaskStatus.get(reviewer)).getResearchPID()
											+ " status is " + (isResearchTaskCompleted ? "done" : "in progress"));
									if (!isResearchTaskCompleted) {
										signalToGoAhead = false;
										break;
									}
								}
							}

							this.logger.debug("signalToGoAhead is " + signalToGoAhead + " for team " + teamName
									+ " for cycle " + (String) affectedTeamsMap.get(teamName));
							isResearchProcessCompleted = ResourceLocator.self().getSBMService()
									.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
							if (signalToGoAhead && !isResearchProcessCompleted) {
								HashMap<String, Object> researchProcessDSValues = new HashMap();
								researchProcessDSValues.put("signalToGoAhead", signalToGoAhead);
								ResourceLocator.self().getSBMService().updateDataSlots(session,
										teamAnalystMapping.getResearchProcessPID(), researchProcessDSValues);
								teamAnalystMapping.setResearchTaskStatus("Done");
							}
						}
					}

					flowNotificationManager.sendNotifications();
				} catch (Exception var58) {
					throw new CMSException(this.logger, var58);
				} finally {
					try {
						this.releaseLock(lockVO);
					} catch (Exception var57) {
						throw new CMSException(this.logger, var57);
					}
				}
			}

		}
	}

	private void removeReviewer(String reviewerToRemove, Vector<String> reviewers) {
		int indexToRemove = -1;

		for (int index = 0; index < reviewers.size(); ++index) {
			if (((String) reviewers.get(index)).equals(reviewerToRemove)) {
				indexToRemove = index;
				break;
			}
		}

		if (indexToRemove != -1) {
			reviewers.remove(indexToRemove);
		}

	}

	private int getCurrentReviewerIndex(Session session, TeamAnalystMapping teamAnalystMapping, long parentPID)
			throws CMSException {
		int currentReviewerIndex = -1;
		String currentReviewer = (String) ResourceLocator.self().getSBMService()
				.getDataslotValue(teamAnalystMapping.getReviewProcessPID(), "Reviewer", session);
		long pidToBeUpdated = 0L;
		if (teamAnalystMapping.getTeamName().contains("Primary")) {
			pidToBeUpdated = parentPID;
		} else {
			pidToBeUpdated = teamAnalystMapping.getResearchProcessPID();
		}

		Vector<String> existingReviewers = (Vector) ResourceLocator.self().getSBMService()
				.getDataslotValue(pidToBeUpdated, "Reviewers", session);
		int index = 0;

		for (Iterator var12 = existingReviewers.iterator(); var12.hasNext(); ++index) {
			String existingReviewer = (String) var12.next();
			if (existingReviewer.equals(currentReviewer)) {
				currentReviewerIndex = index;
				break;
			}
		}

		this.logger.debug(
				"currentReviewerIndex for team " + teamAnalystMapping.getTeamName() + " is " + currentReviewerIndex);
		return currentReviewerIndex;
	}

	private void stopTimeTrackerAndLogCaseHistory(String taskName, String taskPid, String crn, String processCycle,
			String taskStatus, String performer, String status, String updatedBy) throws CMSException {
		TimeTrackerVO timeTracker = new TimeTrackerVO();
		timeTracker.setCrn(crn);
		timeTracker.setUserName(performer);
		timeTracker.setTaskName(taskName);
		timeTracker.setTaskPid(taskPid);
		timeTracker.setUpdatedBy(updatedBy);
		this.logger
				.debug("Stopping time tracker and logging case history for crn :: " + crn + " performer :: " + performer
						+ " taskName :: " + taskName + " :: taskPid " + taskPid + " processCycle :: " + processCycle);
		ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTracker);
		ResourceLocator.self().getCaseHistoryService().setCaseHistoryForTaskCompleted(taskName, taskPid, crn,
				processCycle, taskStatus, performer, status);
	}

	private void stopTimeTracker(String taskName, String taskPid, String crn, String performer, String updatedBy)
			throws CMSException {
		TimeTrackerVO timeTracker = new TimeTrackerVO();
		timeTracker.setCrn(crn);
		timeTracker.setUserName(performer);
		timeTracker.setTaskName(taskName);
		timeTracker.setTaskPid(taskPid);
		timeTracker.setUpdatedBy(updatedBy);
		this.logger.debug("Stopping time tracker and logging case history for crn :: " + crn + " performer :: "
				+ performer + " taskName :: " + taskName + " :: taskPid " + taskPid);
		ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTracker);
	}

	private boolean isPrimaryReviewComplete(Session session, long caseCreationPid) throws CMSException {
		String[] activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(session,
				caseCreationPid);
		String caseCreationWS = null;
		boolean isPrimaryReviewComplete = false;

		for (int i = 0; i < activatedWSNames.length; ++i) {
			this.logger.debug("activated workstep for pid " + caseCreationPid + " is " + activatedWSNames[i]);
			if (!activatedWSNames[i].equals("Invoicing Task") && !activatedWSNames[i].equals("And 2")) {
				caseCreationWS = activatedWSNames[i];
				break;
			}
		}

		if (caseCreationWS != null && caseCreationWS.equals("Client Submission Task")) {
			isPrimaryReviewComplete = true;
		} else {
			isPrimaryReviewComplete = false;
		}

		return isPrimaryReviewComplete;
	}

	private TeamAnalystMapping getPrimaryTeamForCycle(Map<String, CycleInfo> cycleInformation, String cycleName) {
		CycleInfo cycleInfo = (CycleInfo) cycleInformation.get(cycleName);
		Map<String, TeamAnalystMapping> teamInfo = cycleInfo.getTeamInfo();
		Set<String> teamNames = teamInfo.keySet();
		TeamAnalystMapping primaryTeamAnalystMapping = null;
		Iterator var8 = teamNames.iterator();

		while (var8.hasNext()) {
			String teamName = (String) var8.next();
			if (teamName.contains("Primary")) {
				primaryTeamAnalystMapping = (TeamAnalystMapping) teamInfo.get(teamName);
				break;
			}
		}

		return primaryTeamAnalystMapping;
	}

	private boolean isReviewerIndexChanged(String reviewer, String teamName, List<String> addedReviewersList) {
		boolean reviewerIndexChanged = false;

		for (int index = 0; index < 3; ++index) {
			String reviewerKey = teamName + "::" + reviewer + "#";
			reviewerKey = reviewerKey + index;
			if (addedReviewersList.contains(reviewerKey)) {
				reviewerIndexChanged = true;
			}
		}

		return reviewerIndexChanged;
	}

	private boolean isStartConsolidation(long caseCreationPid, Session session, String currentCycle,
			Map<String, CycleInfo> cycleInformation, Map<String, Boolean> goToDsValues) throws CMSException {
		boolean startConsolidation = false;
		TeamAnalystMapping primaryTeamAnalystMapping = this.getPrimaryTeamForCycle(cycleInformation, currentCycle);
		if (primaryTeamAnalystMapping.isTeamAssignmentDone()) {
			this.logger.debug("primaryTeamAnalystMapping.getResearchTaskStatus() : "
					+ primaryTeamAnalystMapping.getResearchTaskStatus());
			if (primaryTeamAnalystMapping.getResearchTaskStatus().equals("Done")) {
				String[] activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(session,
						caseCreationPid);

				for (int i = 0; i < activatedWSNames.length; ++i) {
					this.logger.debug("activated workstep for case creation is " + activatedWSNames[i]);
					if (activatedWSNames[i].equals("Consolidation Task")) {
						break;
					}

					if (activatedWSNames[i].equals("FinalReview")) {
						goToDsValues.put("goToReview", true);
						break;
					}

					if (activatedWSNames[i].equals("Client Submission Task")) {
						goToDsValues.put("goToClientSubmission", true);
						break;
					}
				}

				startConsolidation = true;
				Map<String, TeamAnalystMapping> currentCycleTeamInfo = ((CycleInfo) cycleInformation.get(currentCycle))
						.getTeamInfo();
				Set<String> teamNames = currentCycleTeamInfo.keySet();
				Iterator var13 = teamNames.iterator();

				while (var13.hasNext()) {
					String teamName = (String) var13.next();
					if (!teamName.contains("Primary")) {
						TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) currentCycleTeamInfo.get(teamName);
						if (!teamAnalystMapping.isTeamAssignmentDone()) {
							startConsolidation = false;
							break;
						}

						if (!teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
							startConsolidation = false;
							break;
						}

						if (teamAnalystMapping.getResearchProcessPID() == 0L) {
							startConsolidation = false;
							break;
						}

						boolean isResearchProcessCompleted = ResourceLocator.self().getSBMService()
								.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
						this.logger.debug("team " + teamAnalystMapping.getTeamName() + "research process status is "
								+ (isResearchProcessCompleted ? "done" : "in progress"));
						if (!isResearchProcessCompleted) {
							startConsolidation = false;
							break;
						}
					}
				}
			}
		}

		return startConsolidation;
	}

	private void resetTasksForTeam(TeamAnalystMapping teamAnalystMapping) {
		Map<String, AnalystTaskStatus> analystTaskStatus = teamAnalystMapping.getAnalystTaskStatus();
		if (analystTaskStatus != null) {
			Set<String> analystList = analystTaskStatus.keySet();
			Iterator var5 = analystList.iterator();

			while (var5.hasNext()) {
				String analyst = (String) var5.next();
				((AnalystTaskStatus) analystTaskStatus.get(analyst)).setResearchPID(0L);
				((AnalystTaskStatus) analystTaskStatus.get(analyst)).setStatus("");
			}

			teamAnalystMapping.setResearchTaskStatus("");
		}

		LinkedHashMap<String, ReviewStatusMapping> reviewTaskStatus = teamAnalystMapping.getReviewers();
		if (reviewTaskStatus != null) {
			Set<String> reviewerList = reviewTaskStatus.keySet();
			Iterator var6 = reviewerList.iterator();

			while (var6.hasNext()) {
				String reviewer = (String) var6.next();
				((ReviewStatusMapping) reviewTaskStatus.get(reviewer)).setResearchPID(0L);
				((ReviewStatusMapping) reviewTaskStatus.get(reviewer)).setStatus("");
			}
		}

	}

	private HashMap<String, String> getWorkstepToBeCompleted(long caseCreationPid, Session session,
			TeamAnalystMapping teamAnalystMapping, TeamAnalystMapping primaryTeamAnalystMapping) throws CMSException {
		String workstepToBeCompleted = null;
		long pidForWorkstep = 0L;
		String performer = null;
		HashMap<String, String> workstepInfoMap = new HashMap();
		String[] activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(session,
				caseCreationPid);
		String caseCreationWS = null;

		for (int i = 0; i < activatedWSNames.length; ++i) {
			this.logger.debug("activated workstep for pid " + caseCreationPid + " is " + activatedWSNames[i]);
			if (!activatedWSNames[i].equals("Invoicing Task") && !activatedWSNames[i].equals("And 2")) {
				caseCreationWS = activatedWSNames[i];
				break;
			}
		}

		this.logger.debug("caseCreationWS is " + caseCreationWS);
		if (caseCreationWS != null && teamAnalystMapping == null) {
			if (caseCreationWS.equals("FinalReview")) {
				workstepToBeCompleted = "Review Task";
				pidForWorkstep = primaryTeamAnalystMapping.getReviewProcessPID();
				performer = (String) ResourceLocator.self().getSBMService()
						.getDataslotValue(primaryTeamAnalystMapping.getReviewProcessPID(), "Reviewer", session);
			} else {
				workstepToBeCompleted = caseCreationWS;
				pidForWorkstep = caseCreationPid;
				if (caseCreationWS.equals("Consolidation Task")) {
					performer = (String) ResourceLocator.self().getSBMService().getDataslotValue(caseCreationPid,
							"MainAnalyst", session);
				} else if (caseCreationWS.equals("Client Submission Task")) {
					performer = (String) ResourceLocator.self().getSBMService().getDataslotValue(caseCreationPid,
							"CaseManager", session);
				}
			}
		} else if (teamAnalystMapping != null) {
			if (teamAnalystMapping.isTeamAssignmentDone()) {
				boolean isResearchProcessCompleted = ResourceLocator.self().getSBMService()
						.isTaskCompleted(teamAnalystMapping.getResearchProcessPID(), session);
				this.logger.debug("isResearchProcessCompleted for " + teamAnalystMapping.getTeamName() + " pid "
						+ teamAnalystMapping.getResearchProcessPID() + " : " + isResearchProcessCompleted);
				if (!isResearchProcessCompleted) {
					String[] researchActivatedWSNames = ResourceLocator.self().getSBMService()
							.getActivatedWSNames(session, teamAnalystMapping.getResearchProcessPID());
					if (researchActivatedWSNames.length != 0) {
						if (researchActivatedWSNames[0].equals("Review")) {
							workstepToBeCompleted = "Review Task";
							pidForWorkstep = teamAnalystMapping.getReviewProcessPID();
							performer = (String) ResourceLocator.self().getSBMService()
									.getDataslotValue(teamAnalystMapping.getReviewProcessPID(), "Reviewer", session);
						} else if (researchActivatedWSNames[0].equals("Consolidation Task")) {
							workstepToBeCompleted = "Consolidation Task";
							pidForWorkstep = teamAnalystMapping.getResearchProcessPID();
							performer = teamAnalystMapping.getMainAnalyst();
						}
					} else {
						workstepToBeCompleted = "WaitAdapter";
						pidForWorkstep = teamAnalystMapping.getResearchProcessPID();
						performer = "null";
					}
				} else {
					workstepToBeCompleted = "WaitForConsolidation";
					pidForWorkstep = caseCreationPid;
					performer = "null";
				}
			}
		} else {
			workstepToBeCompleted = "WaitForConsolidation";
			pidForWorkstep = caseCreationPid;
			performer = "null";
		}

		if (workstepToBeCompleted != null) {
			this.logger.debug("workstep to be completed for pid " + pidForWorkstep + " is " + workstepToBeCompleted);
			workstepInfoMap.put(workstepToBeCompleted + "#" + pidForWorkstep, pidForWorkstep + "#" + performer);
		}

		return workstepInfoMap;
	}

	private void completeWorksteps(Map<String, String> workstepInfoMap, Session session,
			HashMap<String, Object> updatedDSValuesMap, String crn, String updatedBy) throws CMSException {
		Set<String> worksteps = workstepInfoMap.keySet();
		Iterator var8 = worksteps.iterator();

		while (true) {
			while (var8.hasNext()) {
				String workstep = (String) var8.next();
				String workstepName = workstep.split("#")[0];
				String pidForWorkstep = ((String) workstepInfoMap.get(workstep)).split("#")[0];
				String performer = ((String) workstepInfoMap.get(workstep)).split("#")[1];
				if (!workstepName.equals("WaitAdapter") && !workstepName.equals("WaitForConsolidation")) {
					this.logger.debug("before completing workstep " + workstepName + " for pid "
							+ (String) workstepInfoMap.get(workstep));
					this.stopTimeTracker(workstepName, pidForWorkstep, crn, performer, updatedBy);
					boolean taskCompleted = ResourceLocator.self().getSBMService()
							.isTaskCompleted(Long.valueOf(pidForWorkstep), session);
					if (!taskCompleted) {
						updatedDSValuesMap.put("taskPerformer", performer);
						ResourceLocator.self().getSBMService().completeTask(session, updatedDSValuesMap,
								Long.valueOf(pidForWorkstep), workstepName);
					}
				} else {
					HashMap<String, Object> dsValues = new HashMap();
					dsValues.put("isPullback", updatedDSValuesMap.get("isPullback"));
					boolean taskCompleted = ResourceLocator.self().getSBMService()
							.isTaskCompleted(Long.valueOf(pidForWorkstep), session);
					if (!taskCompleted) {
						ResourceLocator.self().getSBMService().updateDataSlots(session, Long.valueOf(pidForWorkstep),
								dsValues);
					}
				}
			}

			return;
		}
	}

	private void updateDataslotsForWorksteps(Map<String, Long> workstepInfoMap, Session session,
			HashMap<String, Object> updatedDSValuesMap) throws CMSException {
		if (updatedDSValuesMap.containsKey("isRejected")) {
			Set<String> worksteps = workstepInfoMap.keySet();
			Iterator var6 = worksteps.iterator();

			while (var6.hasNext()) {
				String workstep = (String) var6.next();
				this.logger.debug("before updating dataslot for workstep " + workstep + " with pid "
						+ workstepInfoMap.get(workstep));
				ResourceLocator.self().getSBMService().updateDataSlots(session, (Long) workstepInfoMap.get(workstep),
						updatedDSValuesMap);
			}
		}

	}

	private void addTeamToInterimCycle(TeamAnalystMapping finalTeamAnalystMapping, String interimCycle,
			String interimDueDate, Map<String, TeamAnalystMapping> interimTeamInfo) {
		TeamAnalystMapping teamAnalystMapping = new TeamAnalystMapping(finalTeamAnalystMapping);
		Map<String, AnalystTaskStatus> finalAnalystTaskStatus = finalTeamAnalystMapping.getAnalystTaskStatus();
		if (finalAnalystTaskStatus != null) {
			Set<String> analystList = finalTeamAnalystMapping.getAnalystTaskStatus().keySet();
			HashMap<String, AnalystTaskStatus> analystTaskStatusMap = new HashMap();
			Iterator var10 = analystList.iterator();

			while (var10.hasNext()) {
				String analyst = (String) var10.next();
				AnalystTaskStatus analystTaskStatus = new AnalystTaskStatus();
				analystTaskStatus.setStatus("New");
				analystTaskStatus.setResearchPID(0L);
				analystTaskStatusMap.put(analyst, analystTaskStatus);
			}

			teamAnalystMapping.setAnalystTaskStatus(analystTaskStatusMap);
		}

		teamAnalystMapping.setResearchTaskStatus("");
		LinkedHashMap<String, ReviewStatusMapping> reviewers = finalTeamAnalystMapping.getReviewers();
		if (reviewers != null) {
			Set<String> reviewersList = finalTeamAnalystMapping.getReviewers().keySet();
			LinkedHashMap<String, ReviewStatusMapping> reviewStatusMap = new LinkedHashMap();
			Iterator var14 = reviewersList.iterator();

			while (var14.hasNext()) {
				String reviewer = (String) var14.next();
				ReviewStatusMapping reviewStatus = new ReviewStatusMapping();
				reviewStatus.setStatus("");
				reviewStatus.setResearchPID(0L);
				reviewStatusMap.put(reviewer, reviewStatus);
			}

			teamAnalystMapping.setReviewers(reviewStatusMap);
		}

		teamAnalystMapping.setClientDueDate(interimDueDate);
		this.logger.debug("adding teamAnalystMapping " + teamAnalystMapping.getTeamName() + " to map for interim "
				+ interimCycle);
		interimTeamInfo.put(teamAnalystMapping.getTeamName(), teamAnalystMapping);
		this.logger.debug("team info size for " + interimCycle + " is " + interimTeamInfo.size());
	}

	public void updateDSForRejection(Session session, String workitemName, List<SubTeamReMapVO> subTeamReMapList)
			throws CMSException {
		String crn = ((SubTeamReMapVO) subTeamReMapList.get(0)).getCrn();
		synchronized (crn.intern()) {
			FlowLockInfoVO lockVO = new FlowLockInfoVO();
			lockVO.setCrn(crn);

			try {
				this.isWaitCondition(lockVO);
				this.logger.debug("workitemName is : " + workitemName);
				long pid = this.getProcessInstanceId(workitemName);
				String workstepName = this.getWorkstepName(workitemName);
				long parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID",
						session);
				if (parentPID == 0L) {
					parentPID = pid;
				}

				this.logger.debug("parentPID is : " + parentPID);
				HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
						.getDataslotValue(parentPID, "customDSMap", session);
				this.logger.debug("customDSMap is " + customDSMap);
				CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
				this.logger.debug("cycleTeamMapping is " + cycleTeamMapping);
				String currentCycle = cycleTeamMapping.getCurrentCycle();
				this.logger.debug("currentCycle is " + currentCycle);
				Map<String, CycleInfo> cycleInformation = cycleTeamMapping.getCycleInformation();
				this.logger.debug("cycleInformation is " + cycleInformation);
				List<String> applicableCyclesList = new ArrayList();
				this.logger.debug("cycleTeamMapping.getCycleApplicable() " + cycleTeamMapping.getCycleApplicable());

				for (int index = 0; index < cycleTeamMapping.getCycleApplicable().length; ++index) {
					applicableCyclesList.add(cycleTeamMapping.getCycleApplicable()[index]);
				}

				CycleInfo interim1CycleInfo = (CycleInfo) cycleInformation.get("Interim1");
				this.logger.debug("interim1CycleInfo is " + interim1CycleInfo);
				Map<String, TeamAnalystMapping> interim1TeamInfo = null;
				if (interim1CycleInfo != null) {
					interim1TeamInfo = interim1CycleInfo.getTeamInfo();
				}

				this.logger.debug("interim1TeamInfo is " + interim1TeamInfo);
				CycleInfo interim2CycleInfo = (CycleInfo) cycleInformation.get("Interim2");
				this.logger.debug("interim2CycleInfo is " + interim2CycleInfo);
				Map<String, TeamAnalystMapping> interim2TeamInfo = null;
				if (interim2CycleInfo != null) {
					interim2TeamInfo = interim2CycleInfo.getTeamInfo();
				}

				this.logger.debug("interim2TeamInfo is " + interim2TeamInfo);
				CycleInfo finalCycleInfo = (CycleInfo) cycleInformation.get("Final");
				this.logger.debug("finalCycleInfo is " + finalCycleInfo);
				Map<String, TeamAnalystMapping> finalTeamInfo = null;
				if (finalCycleInfo != null) {
					finalTeamInfo = finalCycleInfo.getTeamInfo();
				}

				this.logger.debug("finalTeamInfo is " + finalTeamInfo);
				HashMap<String, Long> workstepInfoMap = new HashMap();
				this.logger.debug("::::::::::::::::::::::: Pullback because of rejection :::::::::::::::::::::");
				HashMap<String, Object> dsValues = new HashMap();
				HashMap<String, String> teamNamesMap = new HashMap();
				Iterator var28 = subTeamReMapList.iterator();

				while (true) {
					while (var28.hasNext()) {
						SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) var28.next();
						String teamId = String.valueOf(subTeamReMap.getTeamId());
						String teamName = ResourceLocator.self().getTeamAssignmentService().getTeamType(teamId) + "#"
								+ teamId;
						String analyst = subTeamReMap.getPerformer();
						this.logger.debug("impacted analyst " + analyst + " from team " + teamName);
						TeamAnalystMapping teamAnalystMapping;
						Map analystTaskStatusMap;
						AnalystTaskStatus analystTaskStatus;
						if (interim1TeamInfo != null && interim1TeamInfo.containsKey(teamName)
								&& currentCycle.equals("Interim1")) {
							this.logger.debug(
									"pulling back team " + teamName + " analyst " + analyst + " for interim 1 cycle");
							teamAnalystMapping = (TeamAnalystMapping) interim1TeamInfo.get(teamName);
							if (!teamAnalystMapping.isTeamAssignmentDone()) {
								throw new CMSException();
							}

							analystTaskStatusMap = teamAnalystMapping.getAnalystTaskStatus();
							analystTaskStatus = (AnalystTaskStatus) analystTaskStatusMap.get(analyst);
							analystTaskStatus.setStatus("");
							analystTaskStatus.setResearchPID(0L);
							this.logger.debug("team " + teamName + " research task status is "
									+ teamAnalystMapping.getResearchTaskStatus());
							if (!teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
								throw new CMSException();
							}

							dsValues.put("isRejected", true);
							workstepInfoMap.put(workstepName + "#" + pid, pid);
							if (!teamNamesMap.containsKey(teamName)) {
								teamNamesMap.put(teamName, "Interim1");
							}

							teamAnalystMapping.getAnalystTaskStatus().put(analyst, analystTaskStatus);
							interim1TeamInfo.put(teamName, teamAnalystMapping);
						} else if (interim2TeamInfo != null && interim2TeamInfo.containsKey(teamName)
								&& !currentCycle.equals("Final")) {
							this.logger.debug(
									"pulling back team " + teamName + " analyst " + analyst + " for interim 2 cycle");
							teamAnalystMapping = (TeamAnalystMapping) interim2TeamInfo.get(teamName);
							if (!teamAnalystMapping.isTeamAssignmentDone()) {
								throw new CMSException();
							}

							analystTaskStatusMap = teamAnalystMapping.getAnalystTaskStatus();
							analystTaskStatus = (AnalystTaskStatus) analystTaskStatusMap.get(analyst);
							analystTaskStatus.setStatus("");
							analystTaskStatus.setResearchPID(0L);
							this.logger.debug("team " + teamName + " research task status is "
									+ teamAnalystMapping.getResearchTaskStatus());
							if (!teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
								throw new CMSException();
							}

							dsValues.put("isRejected", true);
							workstepInfoMap.put(workstepName + "#" + pid, pid);
							if (!teamNamesMap.containsKey(teamName)) {
								teamNamesMap.put(teamName, "Interim2");
							}

							teamAnalystMapping.getAnalystTaskStatus().put(analyst, analystTaskStatus);
							interim2TeamInfo.put(teamName, teamAnalystMapping);
						} else if (finalTeamInfo != null && (currentCycle.equals("Final")
								|| (interim1TeamInfo == null || !interim1TeamInfo.containsKey(teamName))
										&& (interim2TeamInfo == null || !interim2TeamInfo.containsKey(teamName)))) {
							this.logger.debug(
									"pulling back team " + teamName + " analyst " + analyst + " for final cycle");
							teamAnalystMapping = (TeamAnalystMapping) finalTeamInfo.get(teamName);
							if (!teamAnalystMapping.isTeamAssignmentDone()) {
								throw new CMSException();
							}

							analystTaskStatusMap = teamAnalystMapping.getAnalystTaskStatus();
							analystTaskStatus = (AnalystTaskStatus) analystTaskStatusMap.get(analyst);
							analystTaskStatus.setStatus("");
							analystTaskStatus.setResearchPID(0L);
							this.logger.debug("team " + teamName + " research task status is "
									+ teamAnalystMapping.getResearchTaskStatus());
							if (!teamAnalystMapping.getResearchTaskStatus().equals("Done")) {
								throw new CMSException();
							}

							dsValues.put("isRejected", true);
							workstepInfoMap.put(workstepName + "#" + pid, pid);
							if (!teamNamesMap.containsKey(teamName)) {
								teamNamesMap.put(teamName, "Final");
							}

							teamAnalystMapping.getAnalystTaskStatus().put(analyst, analystTaskStatus);
							finalTeamInfo.put(teamName, teamAnalystMapping);
						}
					}

					Set<String> teamNamesList = teamNamesMap.keySet();
					Iterator var50 = teamNamesList.iterator();

					while (var50.hasNext()) {
						String teamName = (String) var50.next();
						TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) ((CycleInfo) cycleTeamMapping
								.getCycleInformation().get(teamNamesMap.get(teamName))).getTeamInfo().get(teamName);
						teamAnalystMapping.setResearchTaskStatus("");
					}

					customDSMap.put("CycleTeamMapping", cycleTeamMapping);
					HashMap<String, Object> caseCreationDSValues = new HashMap();
					caseCreationDSValues.put("customDSMap", customDSMap);
					this.updateDataslotsForWorksteps(workstepInfoMap, session, dsValues);
					this.logger.debug("before updating customDSMap for pid " + parentPID);
					ResourceLocator.self().getSBMService().updateDataSlots(session, parentPID, caseCreationDSValues);
					this.sendRejectionTriggerToJuno(crn, pid);
					return;
				}
			} catch (Exception var43) {
				throw new CMSException(this.logger, var43);
			} finally {
				try {
					this.releaseLock(lockVO);
				} catch (Exception var42) {
					throw new CMSException(this.logger, var42);
				}
			}
		}
	}

	private void isWaitCondition(FlowLockInfoVO lockVO) throws InterruptedException, SQLException, IOException {
		boolean isStillWaiting = true;
		ClassLoader classLoader = FlowController.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("atlas.properties");
		Properties prop = new Properties();
		prop.load(is);
		lockVO.setIpAddress(prop.getProperty("atlas.pramati.provider.url.sync"));
		int i = 0;

		do {
			if (this.checkForLock(lockVO) == null || "".equalsIgnoreCase(this.checkForLock(lockVO).getIpAddress())) {
				this.logger.debug(String.valueOf(isStillWaiting));
				isStillWaiting = false;
				i = 0;
			}

			if (i > 0) {
				Thread.sleep(2000L);
			}

			if (i > 30) {
				i = 0;
				if (!this.isJNDILookupSuceeded(lockVO)) {
					i = 0;
					this.releaseLock(lockVO);
					isStillWaiting = false;
				}
			}

			if (!isStillWaiting) {
				try {
					this.getLock(lockVO);
				} catch (SQLException var8) {
					if (!(var8.getCause() instanceof SQLIntegrityConstraintViolationException)) {
						int i = false;
						isStillWaiting = true;
						this.logger.error("Some unexpected SQL exception occured, This code will break from here..."
								+ var8.getStackTrace().toString() + " :: " + var8);
						throw new SQLException();
					}

					i = 1;
					isStillWaiting = true;
					this.logger.debug(
							"Two threads tried to access the DB at same time...So putting this thread in loop again.;");
				}
			}

			++i;
		} while (isStillWaiting);

	}

	private FlowLockInfoVO checkForLock(FlowLockInfoVO lockVO) throws SQLException, IOException {
		FlowMutexImpl mutex = new FlowMutexImpl();
		return mutex.checkForLock(lockVO);
	}

	private void releaseLock(FlowLockInfoVO lockVO) throws SQLException, IOException {
		FlowMutexImpl mutex = new FlowMutexImpl();
		mutex.releaseLock(lockVO);
	}

	private boolean isJNDILookupSuceeded(FlowLockInfoVO lockVO) throws IOException, SQLException {
		FlowMutexImpl mutex = new FlowMutexImpl();
		return mutex.JNDIlookupSuceeded(this.checkForLock(lockVO).getIpAddress());
	}

	private void getLock(FlowLockInfoVO lockVO) throws SQLException, IOException {
		FlowMutexImpl mutex = new FlowMutexImpl();
		mutex.getLock(lockVO);
	}
}