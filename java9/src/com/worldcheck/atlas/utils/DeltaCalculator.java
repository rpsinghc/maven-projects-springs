package com.worldcheck.atlas.utils;

import com.savvion.sbm.util.PService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeltaCalculator {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.utils.DeltaCalculator");
	private static final String TEAM_RE = "teamRE";
	private static final String REVIEWER = "reviewer";
	private static final String ANALYST = "analyst";
	private static final String DUE_DATE = "dueDate";
	private static final String TEAM = "team";
	private static final String RE = "RE";
	private static final String DUE_DATE_UPDATED = "dueDateUpdated";

	public CaseDetails getDelta(CaseDetails oldCaseDetails, CaseDetails newCaseDetails, String module)
			throws CMSException {
		CaseDetails deltaCaseDetails = new CaseDetails();
		this.logger.debug("module : " + module);
		this.logger.debug("newCaseDetails.getCrn() " + newCaseDetails.getCrn());
		deltaCaseDetails.setCrn(newCaseDetails.getCrn());
		this.logger.debug("newCaseDetails.getUpdatedBy() " + newCaseDetails.getUpdatedBy());
		deltaCaseDetails.setUpdatedBy(newCaseDetails.getUpdatedBy());
		this.logger.debug("newCaseDetails.getPrimarySubjectName() " + newCaseDetails.getPrimarySubjectName());
		deltaCaseDetails.setPrimarySubjectName(newCaseDetails.getPrimarySubjectName());
		this.logger.debug(
				"newCaseDetails.getPrimarySubjectCountryName() " + newCaseDetails.getPrimarySubjectCountryName());
		deltaCaseDetails.setPrimarySubjectCountryName(newCaseDetails.getPrimarySubjectCountryName());
		this.logger.debug("newCaseDetails.isPrimarySubjectFlag() " + newCaseDetails.isPrimarySubjectFlag());
		if (newCaseDetails.isPrimarySubjectFlag()) {
			deltaCaseDetails.setPrimarySubjectFlag(newCaseDetails.isPrimarySubjectFlag());
		}

		if (!module.equals("AddSubject") && !module.equals("UpdateSubject")) {
			if (module.equals("DeleteSubject")) {
				deltaCaseDetails.setSubjectList(oldCaseDetails.getSubjectList());
			}
		} else {
			deltaCaseDetails.setSubjectList(newCaseDetails.getSubjectList());
		}

		HashMap oldREMap;
		Iterator i$;
		String newMainAnalyst;
		HashMap oldMainAnalystMap;
		if (module.equals("UpdateSubject")) {
			SubjectDetails oldSubjectDetails = (SubjectDetails) oldCaseDetails.getSubjectList().get(0);
			SubjectDetails newSubjectDetails = (SubjectDetails) newCaseDetails.getSubjectList().get(0);
			this.logger.debug("oldSubjectDetails.getCountryName() :: " + oldSubjectDetails.getCountryName());
			this.logger.debug("newSubjectDetails.getCountryName() :: " + newSubjectDetails.getCountryName());
			if (!oldSubjectDetails.getCountryName().equals(newSubjectDetails.getCountryName())) {
				deltaCaseDetails.setSubjectFlag(true);
				if (newSubjectDetails.isPrimarySub()) {
					deltaCaseDetails.setPrimarySubjectFlag(true);
					deltaCaseDetails.setPrimarySubjectName(newSubjectDetails.getSubjectName());
					deltaCaseDetails.setPrimarySubjectCountryName(newSubjectDetails.getCountryName());
				}
			}

			this.logger.debug("oldSubjectDetails.getSubjectName() :: " + oldSubjectDetails.getSubjectName());
			this.logger.debug("newSubjectDetails.getSubjectName() :: " + newSubjectDetails.getSubjectName());
			if (!oldSubjectDetails.getSubjectName().equals(newSubjectDetails.getSubjectName())) {
				if (newSubjectDetails.isPrimarySub()) {
					deltaCaseDetails.setPrimarySubjectFlag(true);
					deltaCaseDetails.setPrimarySubjectName(newSubjectDetails.getSubjectName());
					deltaCaseDetails.setPrimarySubjectCountryName(newSubjectDetails.getCountryName());
				}

				this.logger.debug(
						"newSubjectDetails.isPullBackToResearch() :: " + newSubjectDetails.isPullBackToResearch());
				if (newSubjectDetails.isPullBackToResearch()) {
					deltaCaseDetails.setSubjectFlag(true);
				}
			}

			this.logger.debug("oldSubjectDetails.isPrimarySub() :: " + oldSubjectDetails.isPrimarySub());
			this.logger.debug("newSubjectDetails.isPrimarySub() :: " + newSubjectDetails.isPrimarySub());
			if (!oldSubjectDetails.isPrimarySub() && newSubjectDetails.isPrimarySub()) {
				deltaCaseDetails.setPrimarySubjectFlag(true);
				deltaCaseDetails.setPrimarySubjectName(newSubjectDetails.getSubjectName());
				deltaCaseDetails.setPrimarySubjectCountryName(newSubjectDetails.getCountryName());
			}

			oldREMap = new HashMap();
			List<String> oldREList = StringUtils.commaSeparatedStringToList(oldSubjectDetails.getReIds());
			i$ = oldREList.iterator();

			while (i$.hasNext()) {
				newMainAnalyst = (String) i$.next();
				oldREMap.put(oldSubjectDetails.getSubjectId() + "::" + newMainAnalyst,
						oldSubjectDetails.getSubjectId() + "::" + newMainAnalyst);
			}

			oldMainAnalystMap = new HashMap();
			List<String> newREList = StringUtils.commaSeparatedStringToList(newSubjectDetails.getReIds());
			Iterator i$ = newREList.iterator();

			while (i$.hasNext()) {
				String reId = (String) i$.next();
				oldMainAnalystMap.put(newSubjectDetails.getSubjectId() + "::" + reId,
						newSubjectDetails.getSubjectId() + "::" + reId);
			}

			this.setDelta(oldREMap, oldMainAnalystMap, "RE", deltaCaseDetails);
		} else {
			String oldResearchDueDates;
			String newResearchDueDates;
			String reviewer;
			String oldInt1DueDate;
			String oldInt2DueDate;
			HashMap analystReMap;
			Iterator i$;
			HashMap oldInterim2DueDates;
			Iterator i$;
			Iterator i$;
			String oldResearchDueDates;
			HashMap oldResearchDatesMap;
			String newInt1DueDate;
			String teamReKey;
			String teamName;
			String subjectReKey;
			if (!module.equals("Office") && !module.equals("DeleteSubject") && !module.equals("AddSubject")
					&& !module.equals("UpdateOfficeForSubject")) {
				if (module.equals("Team")) {
					TeamDetails oldTeamDetails = (TeamDetails) oldCaseDetails.getTeamList().get(0);
					TeamDetails newTeamDetails = (TeamDetails) newCaseDetails.getTeamList().get(0);
					oldREMap = new HashMap();
					Iterator i$ = oldTeamDetails.getAnalystId().iterator();

					String oldMainAnalyst;
					while (i$.hasNext()) {
						oldMainAnalyst = (String) i$.next();
						this.logger.debug("old analyst : " + oldMainAnalyst);
						oldREMap.put(this.getTeamName(oldTeamDetails, (String) null) + "::" + oldMainAnalyst,
								oldMainAnalyst);
					}

					Map<String, String> newAnalystMap = new HashMap();
					i$ = newTeamDetails.getAnalyst().iterator();

					while (i$.hasNext()) {
						newMainAnalyst = (String) i$.next();
						this.logger.debug("new analyst : " + newMainAnalyst);
						newAnalystMap.put(this.getTeamName(newTeamDetails, (String) null) + "::" + newMainAnalyst,
								newMainAnalyst);
					}

					this.setDelta(oldREMap, newAnalystMap, "analyst", deltaCaseDetails);
					oldMainAnalyst = oldTeamDetails.getMainAnalyst();
					newMainAnalyst = newTeamDetails.getMainAnalyst();
					this.logger.debug("old main analyst : " + oldMainAnalyst);
					this.logger.debug("new main analyst : " + newMainAnalyst);
					if (!oldMainAnalyst.equals(newMainAnalyst)) {
						deltaCaseDetails.getMainAnalystMap().put(this.getTeamName(newTeamDetails, (String) null),
								oldTeamDetails.getMainAnalyst() + "#" + newTeamDetails.getMainAnalyst());
						deltaCaseDetails.setMainAnalystFlag(true);
					}

					Map<String, String> oldReviewerMap = new LinkedHashMap();
					int oldIndex = 0;

					for (Iterator i$ = oldTeamDetails.getReviewer().iterator(); i$.hasNext(); ++oldIndex) {
						oldResearchDueDates = (String) i$.next();
						this.logger.debug("old reviewer : " + oldResearchDueDates);
						oldReviewerMap.put(this.getTeamName(oldTeamDetails, (String) null) + "::" + oldResearchDueDates
								+ "#" + oldIndex, oldResearchDueDates);
					}

					Map<String, String> newReviewerMap = new LinkedHashMap();
					int newIndex = 0;

					for (i$ = newTeamDetails.getReviewer().iterator(); i$.hasNext(); ++newIndex) {
						reviewer = (String) i$.next();
						this.logger.debug("new reviewer : " + reviewer);
						newReviewerMap.put(
								this.getTeamName(newTeamDetails, (String) null) + "::" + reviewer + "#" + newIndex,
								reviewer);
					}

					this.setDelta(oldReviewerMap, newReviewerMap, "reviewer", deltaCaseDetails);
					analystReMap = new HashMap();
					i$ = oldTeamDetails.getTeamSubjectREDetails().iterator();

					while (i$.hasNext()) {
						SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
						analystReMap.put(
								this.getTeamName(oldTeamDetails, (String) null) + "::" + subTeamReMap.getPerformer()
										+ "&&" + subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId(),
								subTeamReMap.getPerformer());
					}

					oldInterim2DueDates = new HashMap();
					i$ = newTeamDetails.getTeamSubjectREDetails().iterator();

					while (i$.hasNext()) {
						SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
						oldInterim2DueDates.put(
								this.getTeamName(newTeamDetails, (String) null) + "::" + subTeamReMap.getPerformer()
										+ "&&" + subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId(),
								subTeamReMap.getPerformer());
					}

					this.setDelta(analystReMap, oldInterim2DueDates, "RE", deltaCaseDetails);
					oldInt1DueDate = "null";
					oldInt2DueDate = "null";
					oldResearchDatesMap = new HashMap();
					if (oldTeamDetails.getDueDate1() != null && !oldTeamDetails.getDueDate1().equals("")) {
						oldInt1DueDate = oldTeamDetails.getDueDate1();
					}

					if (oldTeamDetails.getDueDate2() != null && !oldTeamDetails.getDueDate2().equals("")) {
						oldInt2DueDate = oldTeamDetails.getDueDate2();
					}

					this.logger.debug("oldTeamDetails.getFinalDueDate()::" + oldTeamDetails.getFinalDueDate());
					oldResearchDueDates = oldInt1DueDate + "#" + oldInt2DueDate + "#"
							+ oldTeamDetails.getFinalDueDate();
					this.logger.debug("oldResearchDueDates in Team assignment module::" + oldResearchDueDates);
					oldResearchDatesMap.put(this.getTeamName(oldTeamDetails, module) + "::" + oldResearchDueDates,
							oldResearchDueDates);
					HashMap<String, String> newResearchDatesMap = new HashMap();
					newInt1DueDate = "null";
					String newInt2DueDate = "null";
					this.logger.debug("newTeamDetails.getDueDate1()::" + newTeamDetails.getDueDate1());
					this.logger.debug("newTeamDetails.getDueDate2()::" + newTeamDetails.getDueDate2());
					this.logger.debug("newTeamDetails.getFinalDueDate()::" + newTeamDetails.getFinalDueDate());
					if (newTeamDetails.getDueDate1() != null && !newTeamDetails.getDueDate1().equals("")) {
						newInt1DueDate = newTeamDetails.getDueDate1();
					}

					if (newTeamDetails.getDueDate2() != null && !newTeamDetails.getDueDate2().equals("")) {
						newInt2DueDate = newTeamDetails.getDueDate2();
					}

					teamReKey = newInt1DueDate + "#" + newInt2DueDate + "#" + newTeamDetails.getFinalDueDate();
					this.logger.debug("newResearchDueDates in Team assignment module::" + teamReKey);
					newResearchDatesMap.put(this.getTeamName(newTeamDetails, module) + "::" + teamReKey, teamReKey);
					this.setDelta(oldResearchDatesMap, newResearchDatesMap, "dueDateUpdated", deltaCaseDetails);
					this.logger.debug("DUE_DATE_UPDATED::" + deltaCaseDetails.isDueDateUpdatedFlag());
					if (this.getTeamName(oldTeamDetails, module).contains("Primary")) {
						teamName = oldInt1DueDate + "::" + oldInt2DueDate + "::" + oldTeamDetails.getFinalDueDate();
						this.logger.debug("oldPrimaryDueDates in IF::" + teamName);
						subjectReKey = newInt1DueDate + "::" + newInt2DueDate + "::" + newTeamDetails.getFinalDueDate();
						this.logger.debug("newPrimaryDueDates in IF::" + subjectReKey);
						if (!teamName.equals(subjectReKey)) {
							deltaCaseDetails.setResearchDueDateFlag(true);
							deltaCaseDetails.setResearchDueDates(subjectReKey);
						}
					}
				} else if (module.equals("CaseInformation")) {
					Date oldRInterim1DueDate = oldCaseDetails.getrInterim1();
					Date oldRInterim2DueDate = oldCaseDetails.getrInterim2();
					Date oldRFinalDueDate = oldCaseDetails.getFinalRDueDate();
					Date newRInterim1DueDate = newCaseDetails.getrInterim1();
					Date newRInterim2DueDate = newCaseDetails.getrInterim2();
					Date newRFinalDueDate = newCaseDetails.getFinalRDueDate();
					Date newCInterim1DueDate = newCaseDetails.getcInterim1();
					Date newCInterim2DueDate = newCaseDetails.getcInterim2();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					if (oldRInterim1DueDate == null && newRInterim1DueDate != null) {
						deltaCaseDetails.setInterimCycleFlag(true);
						deltaCaseDetails.getAddedInterimDueDates().add("Interim1::" + sdf.format(newRInterim1DueDate)
								+ "::" + sdf.format(newCInterim1DueDate));
						deltaCaseDetails.setrInterim1(newRInterim1DueDate);
						deltaCaseDetails.setcInterim1(newCInterim1DueDate);
					}

					if (oldRInterim2DueDate == null && newRInterim2DueDate != null) {
						deltaCaseDetails.setInterimCycleFlag(true);
						deltaCaseDetails.getAddedInterimDueDates().add("Interim2::" + sdf.format(newRInterim2DueDate)
								+ "::" + sdf.format(newCInterim2DueDate));
						deltaCaseDetails.setrInterim2(newRInterim2DueDate);
						deltaCaseDetails.setcInterim2(newCInterim2DueDate);
					}

					oldResearchDueDates = "";
					newResearchDueDates = "";
					if (oldRInterim1DueDate != null) {
						oldResearchDueDates = sdf.format(oldRInterim1DueDate);
					} else {
						oldResearchDueDates = "null";
					}

					if (oldRInterim2DueDate != null) {
						oldResearchDueDates = oldResearchDueDates + "::" + sdf.format(oldRInterim2DueDate);
					} else {
						oldResearchDueDates = oldResearchDueDates + "::" + "null";
					}

					if (oldRFinalDueDate != null) {
						oldResearchDueDates = oldResearchDueDates + "::" + sdf.format(oldRFinalDueDate);
					} else {
						oldResearchDueDates = oldResearchDueDates + "::" + "null";
					}

					if (newRInterim1DueDate != null) {
						newResearchDueDates = sdf.format(newRInterim1DueDate);
					} else {
						newResearchDueDates = "null";
					}

					if (newRInterim2DueDate != null) {
						newResearchDueDates = newResearchDueDates + "::" + sdf.format(newRInterim2DueDate);
					} else {
						newResearchDueDates = newResearchDueDates + "::" + "null";
					}

					if (newRFinalDueDate != null) {
						newResearchDueDates = newResearchDueDates + "::" + sdf.format(newRFinalDueDate);
					} else {
						newResearchDueDates = newResearchDueDates + "::" + "null";
					}

					this.logger.debug("old research due dates : " + oldResearchDueDates);
					this.logger.debug("new research due dates : " + newResearchDueDates);
					if (!oldResearchDueDates.equals(newResearchDueDates)) {
						deltaCaseDetails.setResearchDueDateFlag(true);
						deltaCaseDetails.setResearchDueDates(newResearchDueDates);
					}

					this.logger.debug("old CM : " + oldCaseDetails.getCaseMgrId());
					this.logger.debug("new CM : " + newCaseDetails.getCaseMgrId());
					if (!oldCaseDetails.getCaseMgrId().equals(newCaseDetails.getCaseMgrId())) {
						deltaCaseDetails
								.setCaseMgrId(oldCaseDetails.getCaseMgrId() + "#" + newCaseDetails.getCaseMgrId());
						deltaCaseDetails.setCaseManagerFlag(true);
					}
				}
			} else {
				if (newCaseDetails.isSubjectFlag()) {
					deltaCaseDetails.setSubjectFlag(newCaseDetails.isSubjectFlag());
					deltaCaseDetails.setSubTeamReMapForPullback(newCaseDetails.getSubTeamReMapForPullback());
				}

				List<TeamDetails> oldTeamList = oldCaseDetails.getTeamList();
				List<TeamDetails> newTeamList = newCaseDetails.getTeamList();
				String oldPrimaryDueDates = "";
				String newPrimaryDueDates = "";
				oldMainAnalystMap = new HashMap();
				HashMap<String, String> oldResearchDatesMap = new HashMap();
				Map<String, Object> oldTeamsMap = new HashMap();
				Iterator i$ = oldTeamList.iterator();

				while (i$.hasNext()) {
					TeamDetails oldTeamDetails = (TeamDetails) i$.next();
					oldResearchDueDates = "null";
					newResearchDueDates = "null";
					oldTeamsMap.put(this.getTeamName(oldTeamDetails, module), oldTeamDetails);
					if (oldTeamDetails.getDueDate1() != null && !oldTeamDetails.getDueDate1().equals("")) {
						oldResearchDueDates = oldTeamDetails.getDueDate1();
					}

					if (oldTeamDetails.getDueDate2() != null && !oldTeamDetails.getDueDate2().equals("")) {
						newResearchDueDates = oldTeamDetails.getDueDate2();
					}

					reviewer = oldResearchDueDates + "#" + newResearchDueDates + "#" + oldTeamDetails.getFinalDueDate();
					if (this.getTeamName(oldTeamDetails, module).contains("Primary")) {
						oldPrimaryDueDates = oldResearchDueDates + "::" + newResearchDueDates + "::"
								+ oldTeamDetails.getFinalDueDate();
					}

					oldResearchDatesMap.put(this.getTeamName(oldTeamDetails, module) + "::" + reviewer, reviewer);
					this.logger.debug("main analyst for " + this.getTeamName(oldTeamDetails, module) + " : "
							+ oldTeamDetails.getMainAnalyst());
					oldMainAnalystMap.put(this.getTeamName(oldTeamDetails, module), oldTeamDetails.getMainAnalyst());
				}

				Map<String, Object> newTeamsMap = new HashMap();
				HashMap<String, String> newResearchDatesMap = new HashMap();

				TeamDetails newTeamDetails;
				for (Iterator i$ = newTeamList.iterator(); i$.hasNext(); newResearchDatesMap
						.put(this.getTeamName(newTeamDetails, module) + "::" + oldInt2DueDate, oldInt2DueDate)) {
					newTeamDetails = (TeamDetails) i$.next();
					reviewer = "null";
					oldInt1DueDate = "null";
					newTeamsMap.put(this.getTeamName(newTeamDetails, module), newTeamDetails);
					if (newTeamDetails.getDueDate1() != null && !newTeamDetails.getDueDate1().equals("")) {
						reviewer = newTeamDetails.getDueDate1();
					}

					if (newTeamDetails.getDueDate2() != null && !newTeamDetails.getDueDate2().equals("")) {
						oldInt1DueDate = newTeamDetails.getDueDate2();
					}

					oldInt2DueDate = reviewer + "#" + oldInt1DueDate + "#" + newTeamDetails.getFinalDueDate();
					if (this.getTeamName(newTeamDetails, module).contains("Primary")) {
						newPrimaryDueDates = reviewer + "::" + oldInt1DueDate + "::" + newTeamDetails.getFinalDueDate();
					}
				}

				this.setDelta(oldTeamsMap, newTeamsMap, "team", deltaCaseDetails);
				Iterator i$;
				HashMap newInterim2DueDates;
				TeamDetails oldTeamDetails;
				Iterator i$;
				TeamDetails newTeamDetails;
				Iterator i$;
				ArrayList analystList;
				if (!module.equals("Office")) {
					ArrayList<String> analystList = new ArrayList();
					analystReMap = new HashMap();
					oldInterim2DueDates = new HashMap();
					i$ = oldTeamList.iterator();

					while (i$.hasNext()) {
						oldTeamDetails = (TeamDetails) i$.next();
						i$ = oldTeamDetails.getTeamSubjectREDetails().iterator();

						while (i$.hasNext()) {
							SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
							if (subTeamReMap.getPerformer() != null && subTeamReMap.getPerformer().length() != 0
									&& !subTeamReMap.getPerformer().equalsIgnoreCase("null")) {
								oldInterim2DueDates.put(
										this.getTeamName(oldTeamDetails, module) + "::" + subTeamReMap.getPerformer()
												+ "&&" + subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId(),
										subTeamReMap.getPerformer());
								if (!analystList.contains(this.getTeamName(oldTeamDetails, module) + "::"
										+ subTeamReMap.getPerformer())) {
									analystList.add(this.getTeamName(oldTeamDetails, module) + "::"
											+ subTeamReMap.getPerformer());
								}

								this.logger.debug("old RE : " + this.getTeamName(oldTeamDetails, module) + "::"
										+ subTeamReMap.getPerformer() + "&&" + subTeamReMap.getSubjectId() + "::"
										+ subTeamReMap.getReId());
							}
						}
					}

					this.logger.debug("oldSubTeamReMap length is " + oldInterim2DueDates.size());
					newInterim2DueDates = new HashMap();
					i$ = newTeamList.iterator();

					while (i$.hasNext()) {
						newTeamDetails = (TeamDetails) i$.next();
						i$ = newTeamDetails.getTeamSubjectREDetails().iterator();

						while (i$.hasNext()) {
							SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
							if (subTeamReMap.getPerformer() != null && subTeamReMap.getPerformer().length() != 0
									&& !subTeamReMap.getPerformer().equalsIgnoreCase("null")) {
								newInterim2DueDates.put(
										this.getTeamName(newTeamDetails, module) + "::" + subTeamReMap.getPerformer()
												+ "&&" + subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId(),
										subTeamReMap.getPerformer());
								ArrayList<String> reList = (ArrayList) analystReMap.get(
										this.getTeamName(newTeamDetails, module) + "::" + subTeamReMap.getPerformer());
								this.logger.debug("reList for analyst " + this.getTeamName(newTeamDetails, module)
										+ "::" + subTeamReMap.getPerformer() + " is " + reList);
								if (reList == null) {
									reList = new ArrayList();
								}

								this.logger.debug("adding " + subTeamReMap.getSubjectId() + "::"
										+ subTeamReMap.getReId() + " to reList for " + subTeamReMap.getPerformer());
								reList.add(subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId());
								this.logger.debug("map key is " + this.getTeamName(newTeamDetails, module) + "::"
										+ subTeamReMap.getPerformer());
								analystReMap.put(
										this.getTeamName(newTeamDetails, module) + "::" + subTeamReMap.getPerformer(),
										reList);
								this.logger.debug("new RE : " + this.getTeamName(newTeamDetails, module) + "::"
										+ subTeamReMap.getPerformer() + "&&" + subTeamReMap.getSubjectId() + "::"
										+ subTeamReMap.getReId());
							}
						}
					}

					this.logger.debug("newSubTeamReMap length is " + newInterim2DueDates.size());
					this.setDelta(oldInterim2DueDates, newInterim2DueDates, "RE", deltaCaseDetails);
					i$ = analystList.iterator();

					label534 : while (true) {
						String analystKey;
						do {
							if (!i$.hasNext()) {
								break label534;
							}

							analystKey = (String) i$.next();
							oldResearchDueDates = analystKey.split("::")[0];
							this.logger.debug("checking re list for " + analystKey);
							analystList = (ArrayList) analystReMap.get(analystKey);
						} while (analystList != null && analystList.size() != 0);

						this.logger.debug(
								"analyst to be deleted is " + oldResearchDueDates.split("@")[0] + "::" + analystKey);
						newInt1DueDate = analystKey.split("::")[1];
						deltaCaseDetails.getDeletedAnalysts()
								.add(oldResearchDueDates.split("@")[0] + "::" + newInt1DueDate);
						if (oldMainAnalystMap.get(oldResearchDueDates) != null
								&& ((String) oldMainAnalystMap.get(oldResearchDueDates)).equals(newInt1DueDate)) {
							this.logger.debug("main analyst deleted for team " + oldResearchDueDates.split("@")[0]);
							deltaCaseDetails.getMainAnalystMap().put(oldResearchDueDates.split("@")[0],
									newInt1DueDate + "#" + "null");
						}

						deltaCaseDetails.setAnalystFlag(true);
					}
				} else {
					this.setDelta(oldResearchDatesMap, newResearchDatesMap, "dueDateUpdated", deltaCaseDetails);
					this.logger.debug("old primary team due dates : " + oldPrimaryDueDates);
					this.logger.debug("new primary team due dates : " + newPrimaryDueDates);
					if (!oldPrimaryDueDates.equals(newPrimaryDueDates)) {
						deltaCaseDetails.setResearchDueDateFlag(true);
						deltaCaseDetails.setResearchDueDates(newPrimaryDueDates);
					}

					Map<String, Object> oldInterim1DueDates = new HashMap();
					i$ = oldTeamList.iterator();

					while (i$.hasNext()) {
						TeamDetails oldTeamDetails = (TeamDetails) i$.next();
						if (oldTeamDetails.getDueDate1() != null && oldTeamDetails.getDueDate1().length() != 0) {
							oldInterim1DueDates.put(this.getTeamName(oldTeamDetails, module) + "::" + "Interim1",
									oldTeamDetails);
						}
					}

					analystReMap = new HashMap();
					i$ = newTeamList.iterator();

					while (i$.hasNext()) {
						TeamDetails newTeamDetails = (TeamDetails) i$.next();
						if (newTeamDetails.getDueDate1() != null && newTeamDetails.getDueDate1().length() != 0) {
							analystReMap.put(this.getTeamName(newTeamDetails, module) + "::" + "Interim1",
									newTeamDetails);
						}
					}

					this.setDelta(oldInterim1DueDates, analystReMap, "dueDate", deltaCaseDetails);
					oldInterim2DueDates = new HashMap();
					i$ = oldTeamList.iterator();

					while (i$.hasNext()) {
						oldTeamDetails = (TeamDetails) i$.next();
						if (oldTeamDetails.getDueDate2() != null && oldTeamDetails.getDueDate2().length() != 0) {
							oldInterim2DueDates.put(this.getTeamName(oldTeamDetails, module) + "::" + "Interim2",
									oldTeamDetails);
						}
					}

					newInterim2DueDates = new HashMap();
					i$ = newTeamList.iterator();

					while (i$.hasNext()) {
						newTeamDetails = (TeamDetails) i$.next();
						if (newTeamDetails.getDueDate2() != null && newTeamDetails.getDueDate2().length() != 0) {
							newInterim2DueDates.put(this.getTeamName(newTeamDetails, module) + "::" + "Interim2",
									newTeamDetails);
						}
					}

					this.setDelta(oldInterim2DueDates, newInterim2DueDates, "dueDate", deltaCaseDetails);
					Map<String, String> oldTeamReMap = new HashMap();
					i$ = oldTeamList.iterator();

					while (i$.hasNext()) {
						TeamDetails oldTeamDetails = (TeamDetails) i$.next();
						Iterator i$ = oldTeamDetails.getTeamSubjectREDetails().iterator();

						while (i$.hasNext()) {
							SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
							oldTeamReMap.put(this.getTeamName(oldTeamDetails, module) + "&&"
									+ subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId(),
									this.getTeamName(oldTeamDetails, module));
						}
					}

					this.logger.debug("oldTeamReMap length is " + oldTeamReMap.size());
					oldResearchDatesMap = new HashMap();
					i$ = newTeamList.iterator();

					Iterator i$;
					while (i$.hasNext()) {
						TeamDetails newTeamDetails = (TeamDetails) i$.next();
						i$ = newTeamDetails.getTeamSubjectREDetails().iterator();

						while (i$.hasNext()) {
							SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
							oldResearchDatesMap.put(this.getTeamName(newTeamDetails, module) + "&&"
									+ subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId(),
									this.getTeamName(newTeamDetails, module));
						}
					}

					this.logger.debug("newTeamReMap length is " + oldResearchDatesMap.size());
					this.setDelta(oldTeamReMap, oldResearchDatesMap, "teamRE", deltaCaseDetails);
					Map<String, ArrayList<String>> analystReMap = new HashMap();
					analystList = new ArrayList();
					i$ = oldTeamList.iterator();

					while (i$.hasNext()) {
						TeamDetails oldTeamDetails = (TeamDetails) i$.next();
						Iterator i$ = oldTeamDetails.getTeamSubjectREDetails().iterator();

						while (i$.hasNext()) {
							SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
							if (subTeamReMap.getPerformer() != null && subTeamReMap.getPerformer().length() != 0
									&& !subTeamReMap.getPerformer().equalsIgnoreCase("null")
									&& !this.getTeamName(oldTeamDetails, module).contains("BI")
									&& !this.getTeamName(oldTeamDetails, module).contains("Vendor")) {
								ArrayList<String> reList = (ArrayList) analystReMap.get(
										this.getTeamName(oldTeamDetails, module) + "::" + subTeamReMap.getPerformer());
								if (reList == null) {
									reList = new ArrayList();
									if (!analystList.contains(subTeamReMap.getPerformer())) {
										analystList.add(subTeamReMap.getPerformer());
									}
								}

								reList.add(subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId());
								analystReMap.put(
										this.getTeamName(oldTeamDetails, module) + "::" + subTeamReMap.getPerformer(),
										reList);
								this.logger.debug("old RE : " + this.getTeamName(oldTeamDetails, module) + "::"
										+ subTeamReMap.getPerformer() + "&&" + subTeamReMap.getSubjectId() + "::"
										+ subTeamReMap.getReId());
							}
						}
					}

					this.logger.debug("oldSubTeamReMap length is " + analystReMap.size());
					Map<String, String> newSubTeamReMap = new HashMap();
					Iterator i$ = newTeamList.iterator();

					while (i$.hasNext()) {
						TeamDetails newTeamDetails = (TeamDetails) i$.next();
						Iterator i$ = newTeamDetails.getTeamSubjectREDetails().iterator();

						while (i$.hasNext()) {
							SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) i$.next();
							if (subTeamReMap.getPerformer() != null && subTeamReMap.getPerformer().length() != 0
									&& !subTeamReMap.getPerformer().equalsIgnoreCase("null")
									&& !this.getTeamName(newTeamDetails, module).contains("BI")
									&& !this.getTeamName(newTeamDetails, module).contains("Vendor")) {
								deltaCaseDetails.getAddedSubTeamReMappings()
										.add(this.getTeamName(newTeamDetails, module) + "::"
												+ subTeamReMap.getPerformer() + "&&" + subTeamReMap.getSubjectId()
												+ "::" + subTeamReMap.getReId());
								ArrayList<String> reList = (ArrayList) analystReMap.get(
										this.getTeamName(newTeamDetails, module) + "::" + subTeamReMap.getPerformer());
								if (reList == null) {
									reList = new ArrayList();
								}

								reList.add(subTeamReMap.getSubjectId() + "::" + subTeamReMap.getReId());
								analystReMap.put(
										this.getTeamName(newTeamDetails, module) + "::" + subTeamReMap.getPerformer(),
										reList);
								this.logger.debug("new RE : " + this.getTeamName(newTeamDetails, module) + "::"
										+ subTeamReMap.getPerformer() + "&&" + subTeamReMap.getSubjectId() + "::"
										+ subTeamReMap.getReId());
							}
						}
					}

					this.logger.debug("newSubTeamReMap length is " + newSubTeamReMap.size());
					if (deltaCaseDetails.isTeamReFlag()) {
						i$ = deltaCaseDetails.getAddedTeamReList().iterator();

						label417 : while (true) {
							do {
								if (!i$.hasNext()) {
									i$ = deltaCaseDetails.getDeletedTeamReList().iterator();

									while (true) {
										do {
											if (!i$.hasNext()) {
												break label417;
											}

											teamReKey = (String) i$.next();
											teamName = teamReKey.split("&&")[0];
										} while (deltaCaseDetails.getDeletedTeamsMap().containsKey(teamName));

										subjectReKey = teamReKey.split("&&")[1];
										Iterator i$ = analystList.iterator();

										while (i$.hasNext()) {
											String analyst = (String) i$.next();
											ArrayList<String> reList = (ArrayList) analystReMap
													.get(teamName + "::" + analyst);
											if (reList != null && reList.size() != 0) {
												reList.remove(subjectReKey);
												if (reList.size() == 0) {
													this.logger.debug("analyst to be deleted is "
															+ teamName.split("@")[0] + "::" + analyst);
													deltaCaseDetails.getDeletedAnalysts()
															.add(teamName.split("@")[0] + "::" + analyst);
													deltaCaseDetails.setAnalystFlag(true);
													if (oldMainAnalystMap.get(teamName) != null
															&& ((String) oldMainAnalystMap.get(teamName))
																	.equals(analyst)) {
														this.logger.debug("main analyst deleted for team "
																+ teamName.split("@")[0]);
														deltaCaseDetails.getMainAnalystMap().put(teamName.split("@")[0],
																analyst + "#" + "null");
													}
												}
											}
										}
									}
								}

								teamReKey = (String) i$.next();
							} while (!teamReKey.contains("BI") && !teamReKey.contains("Vendor"));

							teamName = teamReKey.split("&&")[0];
							subjectReKey = teamReKey.split("&&")[1];
							String performer = teamName.split("@")[1];
							deltaCaseDetails.getAddedSubTeamReMappings()
									.add(teamName + "::" + performer + "&&" + subjectReKey);
						}
					}

					if (deltaCaseDetails.getAddedSubTeamReMappings().size() > 0) {
						deltaCaseDetails.setReFlag(true);
					}

					this.logger.debug("old CM : " + oldCaseDetails.getCaseMgrId());
					this.logger.debug("new CM : " + newCaseDetails.getCaseMgrId());
					if (!oldCaseDetails.getCaseMgrId().equals(newCaseDetails.getCaseMgrId())) {
						deltaCaseDetails
								.setCaseMgrId(oldCaseDetails.getCaseMgrId() + "#" + newCaseDetails.getCaseMgrId());
						deltaCaseDetails.setCaseManagerFlag(true);
					}
				}
			}
		}

		this.logger.debug("deltaCaseDetails.isSubjectFlag(): " + deltaCaseDetails.isSubjectFlag());
		this.logger.debug("deltaCaseDetails.isPrimarySubjectFlag(): " + deltaCaseDetails.isPrimarySubjectFlag());
		this.logger.debug("deltaCaseDetails.isReFlag(): " + deltaCaseDetails.isReFlag());
		this.logger.debug("deltaCaseDetails.isTeamFlag(): " + deltaCaseDetails.isTeamFlag());
		this.logger.debug("deltaCaseDetails.isAnalystFlag(): " + deltaCaseDetails.isAnalystFlag());
		this.logger.debug("deltaCaseDetails.isDueDateFlag(): " + deltaCaseDetails.isDueDateFlag());
		this.logger.debug("deltaCaseDetails.isInterimCycleFlag(): " + deltaCaseDetails.isInterimCycleFlag());
		this.logger.debug("deltaCaseDetails.isReviewerFlag(): " + deltaCaseDetails.isReviewerFlag());
		this.logger.debug("deltaCaseDetails.isCaseManagerFlag(): " + deltaCaseDetails.isCaseManagerFlag());
		this.logger.debug("deltaCaseDetails.isResearchDueDateFlag(): " + deltaCaseDetails.isResearchDueDateFlag());
		this.logger.debug("deltaCaseDetails.isDueDateUpdatedFlag(): " + deltaCaseDetails.isDueDateUpdatedFlag());
		return !deltaCaseDetails.isAnalystFlag() && !deltaCaseDetails.isDueDateFlag() && !deltaCaseDetails.isReFlag()
				&& !deltaCaseDetails.isReviewerFlag() && !deltaCaseDetails.isSubjectFlag()
				&& !deltaCaseDetails.isTeamFlag() && !deltaCaseDetails.isInterimCycleFlag()
				&& !deltaCaseDetails.isPrimarySubjectFlag() && !deltaCaseDetails.isCaseManagerFlag()
				&& !deltaCaseDetails.isResearchDueDateFlag() && !deltaCaseDetails.isMainAnalystFlag()
				&& !deltaCaseDetails.isDueDateUpdatedFlag() ? null : deltaCaseDetails;
	}

	private String getTeamName(TeamDetails teamDetails, String module) {
		String teamName = teamDetails.getTeamType() + "#" + teamDetails.getTeamId();
		if (module != null && (module.equals("Office") || module.equals("DeleteSubject") || module.equals("AddSubject")
				|| module.equals("UpdateOfficeForSubject"))) {
			if (!teamDetails.getTeamType().contains("BI") && !teamDetails.getTeamType().contains("Vendor")) {
				teamName = teamName + "@" + teamDetails.getOfficeName();
			} else {
				teamName = teamName + "@" + teamDetails.getManagerName();
			}
		}

		return teamName;
	}

	private boolean setDelta(Map oldNames, Map newNames, String deltaType, CaseDetails deltaCaseDetails) {
		boolean deltaSet = false;
		Set<String> oldKeySet = oldNames.keySet();
		Iterator i$ = oldKeySet.iterator();

		while (i$.hasNext()) {
			String key = (String) i$.next();
			Object object = oldNames.get(key);
			if (!newNames.containsKey(key)) {
				this.logger.debug("deltaType " + deltaType + ": " + key + " is deleted");
				this.setValue(key, object, deltaCaseDetails, deltaType, false);
				deltaSet = true;
			}
		}

		Set<String> newKeySet = newNames.keySet();
		Iterator i$ = newKeySet.iterator();

		while (i$.hasNext()) {
			String key = (String) i$.next();
			Object object = newNames.get(key);
			if (!oldNames.containsKey(key)) {
				this.logger.debug("deltaType " + deltaType + ": " + key + " is added");
				this.setValue(key, object, deltaCaseDetails, deltaType, true);
				deltaSet = true;
			}
		}

		return deltaSet;
	}

	private void setValue(String deltaValue, Object object, CaseDetails deltaCaseDetails, String deltaType,
			boolean isAdded) {
		if (deltaType.equalsIgnoreCase("team")) {
			deltaCaseDetails.setTeamFlag(true);
			if (isAdded) {
				deltaCaseDetails.getAddedTeamsMap().put(deltaValue, (TeamDetails) object);
			} else {
				deltaCaseDetails.getDeletedTeamsMap().put(deltaValue, (TeamDetails) object);
			}
		}

		if (deltaType.equalsIgnoreCase("dueDate")) {
			deltaCaseDetails.setDueDateFlag(true);
			if (isAdded) {
				deltaCaseDetails.getAddedInterimsForTeamMap().put(deltaValue, (TeamDetails) object);
			}
		}

		if (deltaType.equalsIgnoreCase("dueDateUpdated") && isAdded) {
			deltaCaseDetails.setDueDateUpdatedFlag(true);
			deltaCaseDetails.getUpdatedDueDatesList().add(deltaValue);
		}

		if (deltaType.equalsIgnoreCase("RE")) {
			deltaCaseDetails.setReFlag(true);
			if (isAdded) {
				deltaCaseDetails.getAddedSubTeamReMappings().add(deltaValue);
			} else {
				deltaCaseDetails.getDeletedSubTeamReMappings().add(deltaValue);
			}
		}

		if (deltaType.equalsIgnoreCase("teamRE")) {
			deltaCaseDetails.setTeamReFlag(true);
			if (isAdded) {
				deltaCaseDetails.getAddedTeamReList().add(deltaValue);
			} else {
				deltaCaseDetails.getDeletedTeamReList().add(deltaValue);
			}
		}

		if (deltaType.equalsIgnoreCase("analyst")) {
			deltaCaseDetails.setAnalystFlag(true);
			if (isAdded) {
				deltaCaseDetails.getAddedAnalysts().add(deltaValue);
			} else {
				deltaCaseDetails.getDeletedAnalysts().add(deltaValue);
			}
		}

		if (deltaType.equalsIgnoreCase("reviewer")) {
			deltaCaseDetails.setReviewerFlag(true);
			if (isAdded) {
				deltaCaseDetails.getAddedReviewers().add(deltaValue);
			} else {
				deltaCaseDetails.getDeletedReviewers().add(deltaValue);
			}
		}

	}

	public static void main(String[] args) throws CMSException {
		System.out.println(PService.self().encrypt("ebms"));
	}
}