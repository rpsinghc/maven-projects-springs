package com.worldcheck.atlas.bl.audit;

import com.worldcheck.atlas.bl.interfaces.ICaseHistoryEvents;
import com.worldcheck.atlas.dao.audit.CaseHistoryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.VendorDetailVO;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CaseHistoryEventManager implements ICaseHistoryEvents {
	public static final String REJECT = "Reject";
	public static final String SYSTEM_PULLBACK_MSG = "[Due to System Pullback]";
	public static final String RE_REJECTION_MSG = "[Due to RE Rejection]";
	public static final String COMPLETED_STRING = " Completed ";
	private static final String COMMA_WITH_SPACE = " , ";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.audit.CaseHistoryEventManager");
	private CaseHistoryDAO caseHistoryDAO;

	public void setCaseHistoryDAO(CaseHistoryDAO caseHistoryDAO) {
		this.caseHistoryDAO = caseHistoryDAO;
	}

	public void setCaseHistory(CaseDetails oldCaseDetails, CaseDetails newCaseDetails,
			CaseHistory caseHistoryForOtherParams, String module) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistory");

		try {
			List<CaseHistory> caseHistoryList = new ArrayList();
			List oldSubjectDetail = new ArrayList();
			List newSubjectDetail = new ArrayList();
			if (oldCaseDetails != null && oldCaseDetails.getSubjectList() != null) {
				oldSubjectDetail = oldCaseDetails.getSubjectList();
			}

			if (newCaseDetails != null && newCaseDetails.getSubjectList() != null) {
				newSubjectDetail = newCaseDetails.getSubjectList();
			}

			List oldTeamDetail = new ArrayList();
			List newTeamDetail = new ArrayList();
			if (oldCaseDetails != null && oldCaseDetails.getTeamList() != null) {
				oldTeamDetail = oldCaseDetails.getTeamList();
			}

			if (newCaseDetails != null && newCaseDetails.getTeamList() != null) {
				newTeamDetail = newCaseDetails.getTeamList();
			}

			if (module.equalsIgnoreCase("Subject")) {
				this.logger.debug("oldSubjectDetail size " + ((List) oldSubjectDetail).size());
				this.logger.debug("newSubjectDetail size " + ((List) newSubjectDetail).size());
				caseHistoryList = this.setCaseHistoryForSubject((List) oldSubjectDetail, (List) newSubjectDetail);
			} else if (module.equalsIgnoreCase("Office")) {
				caseHistoryList = this.setCaseHistoryForOffice((List) oldTeamDetail, (List) newTeamDetail,
						caseHistoryForOtherParams.getCRN(), oldCaseDetails.getCaseManagerName(),
						newCaseDetails.getCaseManagerName());
			} else if (module.equalsIgnoreCase("Team")) {
				this.logger.debug("going to call CaseHistoryEventManager::setCaseHistoryForTeam ");
				if (oldTeamDetail != null && newTeamDetail != null && ((List) oldTeamDetail).size() > 0
						&& ((List) newTeamDetail).size() > 0) {
					caseHistoryList = this.setCaseHistoryForTeam((TeamDetails) ((List) oldTeamDetail).get(0),
							(TeamDetails) ((List) newTeamDetail).get(0));
				}
			} else if (module.equalsIgnoreCase("Invoicing Task")) {
				caseHistoryList = this.setCaseHIstoryForInvoicing(oldCaseDetails, newCaseDetails);
			} else if (module.equalsIgnoreCase("caseDetails")) {
				caseHistoryList = this.setCaseHistoryForCaseDetails(oldCaseDetails, newCaseDetails);
			} else if (module.equalsIgnoreCase("VendorDetails")) {
				List oldVendorList = new ArrayList();
				List newVendorList = new ArrayList();
				if (oldCaseDetails != null && oldCaseDetails.getVendorList() != null) {
					oldVendorList = oldCaseDetails.getVendorList();
				}

				if (newCaseDetails != null && newCaseDetails.getVendorList() != null) {
					newVendorList = newCaseDetails.getVendorList();
				}

				caseHistoryList = this.setCaseHistoryForVendor((List) oldVendorList, (List) newVendorList);
			} else if (module.equalsIgnoreCase("Auto Office Assignment")) {
				caseHistoryList = this.setCaseHistoryForAutoOfficeAssignment((List) newTeamDetail);
			}

			this.logger.debug("Prepared case history list size is :- " + ((List) caseHistoryList).size());
			int cnt = 0;
			Iterator iterator = ((List) caseHistoryList).iterator();

			while (true) {
				if (!iterator.hasNext()) {
					this.caseHistoryDAO.insertCaseHistoryEvent((List) caseHistoryList);
					break;
				}

				++cnt;
				CaseHistory caseHistory = (CaseHistory) iterator.next();
				this.setCaseHistoryOtherParams(caseHistoryForOtherParams, caseHistory);
			}
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}

		this.logger.debug("in CaseHistoryEventManager::setCaseHistory");
	}

	private void setCaseHistoryOtherParams(CaseHistory caseHistoryForOtherParams, CaseHistory caseHistory)
			throws CMSException {
		this.logger.debug("in CaseHistoryEventManager:: setCaseHistoryOtherParams");

		try {
			this.logger.debug("caseHistoryForOtherParams.getPid() " + caseHistoryForOtherParams.getPid());
			caseHistory.setPid(caseHistoryForOtherParams.getPid());
			caseHistory.setCRN(caseHistoryForOtherParams.getCRN());
			caseHistory.setProcessCycle(caseHistoryForOtherParams.getProcessCycle());
			caseHistory.setTaskName(caseHistoryForOtherParams.getTaskName());
			caseHistory.setTaskStatus(caseHistoryForOtherParams.getTaskStatus());
			caseHistory.setPerformer(caseHistoryForOtherParams.getPerformer());
			this.logger.debug("exiting CaseHistoryEventManager:: setCaseHistoryOtherParams");
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	private List<CaseHistory> setCaseHistoryForSubject(List oldSubjectDetail, List newSubjectDetail)
			throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForSubject");
		CaseHistory caseHistory = new CaseHistory();
		Object caseHistoryList = new ArrayList();

		try {
			SubjectDetails subDetailObj;
			if (oldSubjectDetail.size() < newSubjectDetail.size()) {
				this.logger.debug("setCaseHistoryForSubject:: Subject Add");
				subDetailObj = (SubjectDetails) newSubjectDetail.get(newSubjectDetail.size() - 1);
				caseHistory.setAction("Subject Added");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo(subDetailObj.getSubjectName());
				((List) caseHistoryList).add(caseHistory);
			} else if (oldSubjectDetail.size() > newSubjectDetail.size()) {
				this.logger.debug("setCaseHistoryForSubject:: Subject Delete");
				subDetailObj = (SubjectDetails) oldSubjectDetail.get(oldSubjectDetail.size() - 1);
				caseHistory.setAction("Subject Deleted");
				caseHistory.setNewInfo("");
				caseHistory.setOldInfo(subDetailObj.getSubjectName());
				((List) caseHistoryList).add(caseHistory);
			} else {
				String actionStr = "Research Element";
				String oldInfoStr = "";
				String newInfoStr = "";

				for (int i = 0; i < oldSubjectDetail.size(); ++i) {
					SubjectDetails oldSubDetailObj = (SubjectDetails) oldSubjectDetail.get(i);
					SubjectDetails newSubDetailObj = (SubjectDetails) newSubjectDetail.get(i);
					this.updateNULLForSubject(oldSubDetailObj);
					this.updateNULLForSubject(newSubDetailObj);
					String oldSubREObj = oldSubDetailObj.getReNames();
					String newSubREObj = newSubDetailObj.getReNames();
					List oldSubREList = StringUtils.commaSeparatedStringToList(oldSubREObj);
					List newSubREList = StringUtils.commaSeparatedStringToList(newSubREObj);
					caseHistoryList = this.getDeltaInRELists(oldSubREList, newSubREList,
							newSubDetailObj.getSubjectName());
					if (!oldSubDetailObj.getSubjectName().equalsIgnoreCase(newSubDetailObj.getSubjectName())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Name:- " + oldSubDetailObj.getSubjectName();
						newInfoStr = newInfoStr + "," + "New Subject Name:- " + newSubDetailObj.getSubjectName();
					}

					if (oldSubDetailObj.isPrimarySub() != newSubDetailObj.isPrimarySub()) {
						oldInfoStr = oldInfoStr + "," + "Old:- " + oldSubDetailObj.getSubjectName() + " Is Primary:- "
								+ oldSubDetailObj.isPrimarySub();
						newInfoStr = newInfoStr + "," + "New:- " + newSubDetailObj.getSubjectName() + " Is Primary:- "
								+ newSubDetailObj.isPrimarySub();
					}

					if (!oldSubDetailObj.getEntityName().equalsIgnoreCase(newSubDetailObj.getEntityName())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Entity:- " + oldSubDetailObj.getEntityName();
						newInfoStr = newInfoStr + "," + "New Eubject Entity:- " + newSubDetailObj.getEntityName();
					}

					if (!oldSubDetailObj.getPosition().equalsIgnoreCase(newSubDetailObj.getPosition())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Position:- " + oldSubDetailObj.getPosition();
						newInfoStr = newInfoStr + "," + "New Subject Position:- " + newSubDetailObj.getPosition();
					}

					if (!oldSubDetailObj.getCountryName().equalsIgnoreCase(newSubDetailObj.getCountryName())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Country Name:- "
								+ oldSubDetailObj.getCountryName();
						newInfoStr = newInfoStr + "," + "New Subject Country Name:- "
								+ newSubDetailObj.getCountryName();
					}

					if (!oldSubDetailObj.getAddress().equalsIgnoreCase(newSubDetailObj.getAddress())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Address:- " + oldSubDetailObj.getAddress();
						newInfoStr = newInfoStr + "," + "New Subject Address:- " + newSubDetailObj.getAddress();
					}

					if (!oldSubDetailObj.getOtherDetails().equalsIgnoreCase(newSubDetailObj.getOtherDetails())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Other Details:- "
								+ oldSubDetailObj.getOtherDetails();
						newInfoStr = newInfoStr + "," + "New Subject Other Details:- "
								+ newSubDetailObj.getOtherDetails();
					}

					if (!oldSubDetailObj.getSubReportType().equalsIgnoreCase(newSubDetailObj.getSubReportType())) {
						oldInfoStr = oldInfoStr + "," + "Old Subject Sub Report Type:- "
								+ oldSubDetailObj.getSubReportType();
						newInfoStr = newInfoStr + "," + "New Subject Sub Report Type:- "
								+ newSubDetailObj.getSubReportType();
					}
				}

				if (!oldInfoStr.isEmpty() || !newInfoStr.isEmpty()) {
					caseHistory.setAction("Subject Info Updated");
					if (oldInfoStr.charAt(0) == ',') {
						oldInfoStr = oldInfoStr.substring(1, oldInfoStr.length());
					}

					if (newInfoStr.charAt(0) == ',') {
						newInfoStr = newInfoStr.substring(1, newInfoStr.length());
					}

					caseHistory.setOldInfo(oldInfoStr);
					caseHistory.setNewInfo(newInfoStr);
					((List) caseHistoryList).add(caseHistory);
				}
			}
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForSubject");
		return (List) caseHistoryList;
	}

	private List<CaseHistory> getDeltaInLists(List oldNames, List newNames, String actionStr) throws CMSException {
		this.logger.debug("In CaseHistoryEventManager::getDeltaInLists");
		List<CaseHistory> caseHistoryList = new ArrayList();
		CaseHistory caseHistoryForAdded = new CaseHistory();
		String oldInfoStr = "";
		String newInfoStr = "";

		try {
			Iterator iterator = oldNames.iterator();

			while (iterator.hasNext()) {
				Object object = iterator.next();
				if (!newNames.contains(object)) {
					oldInfoStr = oldInfoStr + "," + object.toString();
				}
			}

			CaseHistory caseHistoryForDeleted = new CaseHistory();
			Iterator iterator = newNames.iterator();

			while (iterator.hasNext()) {
				Object object = iterator.next();
				if (!oldNames.contains(object)) {
					newInfoStr = newInfoStr + "," + object.toString();
				}
			}

			if (!oldInfoStr.isEmpty()) {
				caseHistoryForAdded.setAction(actionStr + " Deleted");
				if (oldInfoStr.charAt(0) == ',') {
					oldInfoStr = oldInfoStr.substring(1, oldInfoStr.length());
				}

				caseHistoryForAdded.setOldInfo(oldInfoStr);
				caseHistoryForAdded.setNewInfo("");
				caseHistoryList.add(caseHistoryForAdded);
			}

			if (!newInfoStr.isEmpty()) {
				caseHistoryForDeleted.setOldInfo("");
				if (newInfoStr.charAt(0) == ',') {
					newInfoStr = newInfoStr.substring(1, newInfoStr.length());
				}

				caseHistoryForDeleted.setNewInfo(newInfoStr);
				caseHistoryForDeleted.setAction(actionStr + " Added");
				caseHistoryList.add(caseHistoryForDeleted);
			}
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::getDeltaInLists");
		return caseHistoryList;
	}

	private List<CaseHistory> getDeltaInRELists(List oldNames, List newNames, String SubjectName) throws CMSException {
		this.logger.debug("In CaseHistoryEventManager::getDeltaInRELists");
		List<CaseHistory> caseHistoryList = new ArrayList();
		CaseHistory caseHistoryForAdded = new CaseHistory();
		String oldInfoStr = "";
		String newInfoStr = "";

		try {
			Iterator iterator = oldNames.iterator();

			while (iterator.hasNext()) {
				Object object = iterator.next();
				if (!newNames.contains(object)) {
					oldInfoStr = oldInfoStr + "," + object.toString();
				}
			}

			CaseHistory caseHistoryForDeleted = new CaseHistory();
			Iterator iterator = newNames.iterator();

			while (iterator.hasNext()) {
				Object object = iterator.next();
				if (!oldNames.contains(object)) {
					newInfoStr = newInfoStr + "," + object.toString();
				}
			}

			if (!oldInfoStr.isEmpty()) {
				caseHistoryForAdded.setAction("Research Element Deleted");
				if (oldInfoStr.charAt(0) == ',') {
					oldInfoStr = oldInfoStr.substring(1, oldInfoStr.length());
				}

				oldInfoStr = "Subject:-  " + SubjectName + ", Research Element:- " + oldInfoStr;
				caseHistoryForAdded.setOldInfo(oldInfoStr);
				caseHistoryForAdded.setNewInfo("");
				caseHistoryList.add(caseHistoryForAdded);
			}

			if (!newInfoStr.isEmpty()) {
				caseHistoryForDeleted.setOldInfo("");
				if (newInfoStr.charAt(0) == ',') {
					newInfoStr = newInfoStr.substring(1, newInfoStr.length());
				}

				newInfoStr = "Subject:-  " + SubjectName + ", Research Element:- " + newInfoStr;
				caseHistoryForDeleted.setNewInfo(newInfoStr);
				caseHistoryForDeleted.setAction("Research Element Added");
				caseHistoryList.add(caseHistoryForDeleted);
			}
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::getDeltaInRELists");
		return caseHistoryList;
	}

	private List<CaseHistory> setCaseHistoryForOffice(List<TeamDetails> oldTeamDetail, List<TeamDetails> newTeamDetails,
			String crn, String oldCaseManagerName, String newCaseManagerName) throws CMSException {
		List<CaseHistory> caseHistoryList = new ArrayList();
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForOffice " + crn);

		try {
			List<Integer> oldTeamIds = new ArrayList();
			List<Integer> newTeamIds = new ArrayList();
			HashMap<Integer, TeamDetails> newTeamIDAndTypeMap = new HashMap();
			HashMap<Integer, TeamDetails> oldTeamIDAndTypeMap = new HashMap();
			Iterator mapIterator = oldTeamDetail.iterator();

			TeamDetails teamDetail;
			while (mapIterator.hasNext()) {
				teamDetail = (TeamDetails) mapIterator.next();
				oldTeamIds.add(teamDetail.getTeamId());
				oldTeamIDAndTypeMap.put(new Integer(teamDetail.getTeamId()), teamDetail);
			}

			mapIterator = newTeamDetails.iterator();

			while (mapIterator.hasNext()) {
				teamDetail = (TeamDetails) mapIterator.next();
				newTeamIds.add(teamDetail.getTeamId());
				newTeamIDAndTypeMap.put(new Integer(teamDetail.getTeamId()), teamDetail);
			}

			if (!oldCaseManagerName.equalsIgnoreCase(newCaseManagerName)) {
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setAction("Case Manager Reassigned");
				caseHistory.setOldInfo(oldCaseManagerName);
				caseHistory.setNewInfo(newCaseManagerName);
				caseHistoryList.add(caseHistory);
			}

			CaseHistory caseHistory;
			TeamDetails teamDetailObj;
			String teamTypeId;
			Integer key;
			if (oldTeamIDAndTypeMap != null) {
				mapIterator = oldTeamIDAndTypeMap.keySet().iterator();

				label78 : while (true) {
					while (true) {
						if (!mapIterator.hasNext()) {
							break label78;
						}

						key = (Integer) mapIterator.next();
						if (newTeamIds.contains(key)) {
							this.logger.debug("team ID " + key
									+ " already exists in this case .Now checking other changes in team");
							this.updateNULLForOfficeAssignment((TeamDetails) oldTeamIDAndTypeMap.get(key));
							this.updateNULLForOfficeAssignment((TeamDetails) newTeamIDAndTypeMap.get(key));
							this.checkOtherChangesForTeam((TeamDetails) oldTeamIDAndTypeMap.get(key),
									(TeamDetails) newTeamIDAndTypeMap.get(key), caseHistoryList);
						} else {
							caseHistory = new CaseHistory();
							teamDetailObj = (TeamDetails) oldTeamIDAndTypeMap.get(key);
							this.updateNULLForOfficeAssignment(teamDetailObj);
							teamTypeId = teamDetailObj.getTeamTypeId();
							if (!"1".equals(teamTypeId) && !teamDetailObj.getTeamType().equalsIgnoreCase("Primary")) {
								if (teamDetailObj.getTeamType().equalsIgnoreCase("Supporting - Internal")) {
									caseHistory.setAction("Supporting Team Deleted");
									caseHistory.setOldInfo(teamDetailObj.getOfficeName());
									caseHistory.setNewInfo("");
									caseHistoryList.add(caseHistory);
								} else if (teamDetailObj.getTeamType().equalsIgnoreCase("Supporting - Vendor")) {
									caseHistory.setAction("Vendor Team Deleted");
									caseHistory.setOldInfo(teamDetailObj.getManagerFullName());
									caseHistory.setNewInfo("");
									caseHistoryList.add(caseHistory);
								}
							} else {
								caseHistory.setAction("Primary Team is Deleted");
								caseHistory.setOldInfo(teamDetailObj.getOfficeName());
								caseHistory.setNewInfo("");
								caseHistoryList.add(caseHistory);
							}
						}
					}
				}
			}

			if (newTeamIDAndTypeMap != null) {
				mapIterator = newTeamIDAndTypeMap.keySet().iterator();

				while (mapIterator.hasNext()) {
					key = (Integer) mapIterator.next();
					if (!oldTeamIds.contains(key)) {
						caseHistory = new CaseHistory();
						teamDetailObj = (TeamDetails) newTeamIDAndTypeMap.get(key);
						this.updateNULLForOfficeAssignment(teamDetailObj);
						teamTypeId = teamDetailObj.getTeamTypeId();
						if (teamDetailObj.getTeamType().equalsIgnoreCase("Primary")) {
							caseHistory.setAction("Primary Team Added");
							caseHistory.setOldInfo("");
							caseHistory.setNewInfo(teamDetailObj.getOfficeName());
							caseHistoryList.add(caseHistory);
						} else if (teamDetailObj.getTeamType().equalsIgnoreCase("Supporting - Internal")) {
							caseHistory.setAction("Supporting Team Added");
							caseHistory.setOldInfo("");
							caseHistory.setNewInfo(teamDetailObj.getOfficeName());
							caseHistoryList.add(caseHistory);
						} else if (teamDetailObj.getTeamType().equalsIgnoreCase("Supporting - Vendor")) {
							caseHistory.setAction("Vendor Team Added");
							caseHistory.setOldInfo("");
							caseHistory.setNewInfo(teamDetailObj.getManagerFullName());
							caseHistoryList.add(caseHistory);
						}
					}
				}
			}

			new ArrayList();
			List<CaseHistory> caseHistForReShuffling = this.setCaseHistoryForREShuffling(oldTeamDetail, newTeamDetails,
					crn);
			if (caseHistForReShuffling.size() > 0) {
				caseHistoryList.addAll(caseHistoryList.size(), caseHistForReShuffling);
			}
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}

		this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForOffice");
		return caseHistoryList;
	}

	private List<CaseHistory> checkOtherChangesForTeam(TeamDetails oldTeamDetailObj, TeamDetails newTeamDetailObj,
			List<CaseHistory> caseHistoryList) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::checkOtherChangesForTeam");

		try {
			if (!oldTeamDetailObj.getTeamType().equalsIgnoreCase("Primary")
					&& !oldTeamDetailObj.getTeamType().equalsIgnoreCase("Supporting - Internal")) {
				if (oldTeamDetailObj.getTeamTypeId().equals("3")) {
					this.checkOtherChangesForBiTeam(oldTeamDetailObj, newTeamDetailObj, caseHistoryList);
				} else if (oldTeamDetailObj.getTeamTypeId().equals("4")) {
					this.checkOtherChangesForVendorTeam(oldTeamDetailObj, newTeamDetailObj, caseHistoryList);
				}
			} else {
				CaseHistory caseHistory;
				if (oldTeamDetailObj.getDueDate1() == null && newTeamDetailObj.getDueDate1() != null
						&& !newTeamDetailObj.getDueDate1().isEmpty()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Research Interim 1 Due Date Updated");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName() + " , Date:- "
							+ newTeamDetailObj.getDueDate1().toString());
					caseHistoryList.add(caseHistory);
				} else if (oldTeamDetailObj.getDueDate1() != null && newTeamDetailObj.getDueDate1() != null) {
					if (oldTeamDetailObj.getDueDate1().compareTo(newTeamDetailObj.getDueDate1()) != 0) {
						caseHistory = new CaseHistory();
						caseHistory.setAction("Research Interim 1 Due Date Updated");
						caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName() + " , Date:- "
								+ oldTeamDetailObj.getDueDate1().toString());
						caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName() + " , Date:- "
								+ newTeamDetailObj.getDueDate1().toString());
						caseHistoryList.add(caseHistory);
					}
				} else if (oldTeamDetailObj.getDueDate1() != null && !oldTeamDetailObj.getDueDate1().isEmpty()
						&& (newTeamDetailObj.getDueDate1() == null || newTeamDetailObj.getDueDate1().isEmpty())) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Research Interim 1 Due Date Updated");
					caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName() + " , Date:- "
							+ oldTeamDetailObj.getDueDate1().toString());
					caseHistory.setNewInfo("");
					caseHistoryList.add(caseHistory);
				}

				if (oldTeamDetailObj.getDueDate2() == null && newTeamDetailObj.getDueDate2() != null
						&& !newTeamDetailObj.getDueDate2().isEmpty()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Research Interim 2 Due Date Updated");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName() + " ,  Date:- "
							+ newTeamDetailObj.getDueDate2().toString());
					caseHistoryList.add(caseHistory);
				} else if (oldTeamDetailObj.getDueDate2() != null && newTeamDetailObj.getDueDate2() != null) {
					if (oldTeamDetailObj.getDueDate2().compareTo(newTeamDetailObj.getDueDate2()) != 0) {
						caseHistory = new CaseHistory();
						caseHistory.setAction("Research Interim 2 Due Date Updated");
						caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName() + " ,  Date:- "
								+ oldTeamDetailObj.getDueDate2().toString());
						caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName() + " ,  Date:- "
								+ newTeamDetailObj.getDueDate2().toString());
						caseHistoryList.add(caseHistory);
					}
				} else if (oldTeamDetailObj.getDueDate2() != null && !oldTeamDetailObj.getDueDate2().isEmpty()
						&& (newTeamDetailObj.getDueDate2() == null || newTeamDetailObj.getDueDate2().isEmpty())) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Research Interim 2 Due Date Updated");
					caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName() + " ,  Date:- "
							+ oldTeamDetailObj.getDueDate2().toString());
					caseHistory.setNewInfo("");
					caseHistoryList.add(caseHistory);
				}

				if (oldTeamDetailObj.getFinalDueDate() != null && newTeamDetailObj.getFinalDueDate() != null
						&& oldTeamDetailObj.getFinalDueDate().compareTo(newTeamDetailObj.getFinalDueDate()) != 0) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Research Final Due Date Updated");
					caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName() + " ,  Date:- "
							+ oldTeamDetailObj.getFinalDueDate());
					caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName() + " ,  Date:- "
							+ newTeamDetailObj.getFinalDueDate());
					caseHistoryList.add(caseHistory);
				}
			}
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryEventManager ::checkOtherChangesForTeam");
		return caseHistoryList;
	}

	private void checkOtherChangesForVendorTeam(TeamDetails oldTeamDetailObj, TeamDetails newTeamDetailObj,
			List<CaseHistory> caseHistoryList) {
		this.logger.debug("in CaseHistoryEventManager ::checkOtherChangesForVendorTeam");
		CaseHistory caseHistory;
		if (oldTeamDetailObj.getFinalDueDate() == null && newTeamDetailObj.getFinalDueDate() != null
				&& !newTeamDetailObj.getFinalDueDate().isEmpty()) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("Vendor Final Due Date Updated");
			caseHistory.setOldInfo("");
			caseHistory.setNewInfo(
					newTeamDetailObj.getManagerName() + " , " + newTeamDetailObj.getFinalDueDate().toString());
			caseHistoryList.add(caseHistory);
		} else if (oldTeamDetailObj.getFinalDueDate() != null && newTeamDetailObj.getFinalDueDate() != null
				&& oldTeamDetailObj.getFinalDueDate().compareTo(newTeamDetailObj.getFinalDueDate()) != 0) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("Vendor Final Due Date Updated");
			caseHistory.setOldInfo("Vendor Manager:-" + oldTeamDetailObj.getManagerFullName() + " , Due Date:- "
					+ oldTeamDetailObj.getFinalDueDate().toString());
			caseHistory.setNewInfo("Vendor Manager:-" + newTeamDetailObj.getManagerFullName() + " , Due Date:- "
					+ newTeamDetailObj.getFinalDueDate().toString());
			caseHistoryList.add(caseHistory);
		}

		this.logger.debug("Exiting CaseHistoryEventManager ::checkOtherChangesForVendorTeam");
	}

	private void checkOtherChangesForBiTeam(TeamDetails oldTeamDetailObj, TeamDetails newTeamDetailObj,
			List<CaseHistory> caseHistoryList) {
		this.logger.debug("in CaseHistoryEventManager ::checkOtherChangesForBiTeam");
		this.logger.debug("comparision for BI team");
		CaseHistory caseHistory;
		if (!oldTeamDetailObj.getManagerFullName().equalsIgnoreCase(newTeamDetailObj.getManagerFullName())) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("BI Analyst Reassigned");
			caseHistory.setOldInfo(oldTeamDetailObj.getManagerFullName());
			caseHistory.setNewInfo(newTeamDetailObj.getManagerFullName());
			caseHistoryList.add(caseHistory);
		}

		if (oldTeamDetailObj.getDueDate1() == null && newTeamDetailObj.getDueDate1() != null
				&& !newTeamDetailObj.getDueDate1().isEmpty()) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("BI Interim 1 Due Date Updated");
			caseHistory.setOldInfo("");
			caseHistory.setNewInfo(newTeamDetailObj.getDueDate1());
			caseHistoryList.add(caseHistory);
		} else if (oldTeamDetailObj.getDueDate1() != null && newTeamDetailObj.getDueDate1() != null) {
			if (oldTeamDetailObj.getDueDate1().compareTo(newTeamDetailObj.getDueDate1()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("BI Interim 1 Due Date Updated");
				caseHistory.setOldInfo(oldTeamDetailObj.getDueDate1().toString());
				caseHistory.setNewInfo(newTeamDetailObj.getDueDate1().toString());
				caseHistoryList.add(caseHistory);
			}
		} else if (oldTeamDetailObj.getDueDate1() != null && !oldTeamDetailObj.getDueDate1().isEmpty()
				&& (newTeamDetailObj.getDueDate1() == null || newTeamDetailObj.getDueDate1().isEmpty())) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("BI Interim 1 Due Date Updated");
			caseHistory.setOldInfo(oldTeamDetailObj.getDueDate1().toString());
			caseHistory.setNewInfo("");
			caseHistoryList.add(caseHistory);
		}

		if (oldTeamDetailObj.getDueDate2() == null && newTeamDetailObj.getDueDate2() != null
				&& !newTeamDetailObj.getDueDate2().isEmpty()) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("BI Interim 2 Due Date Updated");
			caseHistory.setOldInfo("");
			caseHistory.setNewInfo(newTeamDetailObj.getDueDate2().toString());
			caseHistoryList.add(caseHistory);
		} else if (oldTeamDetailObj.getDueDate2() != null && newTeamDetailObj.getDueDate2() != null) {
			if (oldTeamDetailObj.getDueDate2().compareTo(newTeamDetailObj.getDueDate2()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("BI Interim 2 Due Date Updated");
				caseHistory.setOldInfo(oldTeamDetailObj.getDueDate2());
				caseHistory.setNewInfo(newTeamDetailObj.getDueDate2());
				caseHistoryList.add(caseHistory);
			}
		} else if (oldTeamDetailObj.getDueDate2() != null && !oldTeamDetailObj.getDueDate2().isEmpty()
				&& (newTeamDetailObj.getDueDate2() == null || newTeamDetailObj.getDueDate2().isEmpty())) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("BI Interim 2 Due Date Updated");
			caseHistory.setOldInfo(oldTeamDetailObj.getDueDate2());
			caseHistory.setNewInfo("");
			caseHistoryList.add(caseHistory);
		}

		if (oldTeamDetailObj.getFinalDueDate() != null && newTeamDetailObj.getFinalDueDate() != null
				&& oldTeamDetailObj.getFinalDueDate().compareTo(newTeamDetailObj.getFinalDueDate()) != 0) {
			caseHistory = new CaseHistory();
			caseHistory.setAction("BI Final Due Date Updated");
			caseHistory.setOldInfo(oldTeamDetailObj.getFinalDueDate());
			caseHistory.setNewInfo(newTeamDetailObj.getFinalDueDate());
			caseHistoryList.add(caseHistory);
		}

		this.logger.debug("Exiting CaseHistoryEventManager ::checkOtherChangesForBiTeam");
	}

	private List<CaseHistory> setCaseHistoryForAutoOfficeAssignment(List<TeamDetails> newTeamDetails)
			throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForAutoOfficeAssignment");
		String teamsAdded = "";
		List<CaseHistory> caseHistoryList = new ArrayList();
		if (newTeamDetails != null && newTeamDetails.size() > 0) {
			TeamDetails teamDetailsObj;
			for (Iterator iterator = newTeamDetails.iterator(); iterator.hasNext(); teamsAdded = teamsAdded
					+ teamDetailsObj.getTeamType() + "(" + teamDetailsObj.getOfficeName() + "),") {
				teamDetailsObj = (TeamDetails) iterator.next();
				this.updateNULLForOfficeAssignment(teamDetailsObj);
			}

			if (!teamsAdded.isEmpty()) {
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setAction("Auto office assignment Completed");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo("teams added:- " + teamsAdded);
				caseHistoryList.add(caseHistory);
			}
		}

		this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForAutoOfficeAssignment");
		return caseHistoryList;
	}

	private List<CaseHistory> setCaseHistoryForTeam(TeamDetails oldTeamDetailObj, TeamDetails newTeamDetailObj)
			throws CMSException {
		this.logger.debug("calling CaseHistoryEventManager:: setCaseHistoryForTeam");
		ArrayList caseHistoryList = new ArrayList();

		try {
			if (oldTeamDetailObj != null && newTeamDetailObj != null) {
				this.updateNullForTeamAssignmentTeamDetails(oldTeamDetailObj);
				this.updateNullForTeamAssignmentTeamDetails(newTeamDetailObj);
				if (oldTeamDetailObj.getTeamType().contains("Primary")
						|| oldTeamDetailObj.getTeamType().equalsIgnoreCase("Supporting - Internal")) {
					String oldManagerName = "";
					String newManagerName = "";
					if (oldTeamDetailObj.getManagerFullName() != null) {
						oldManagerName = oldTeamDetailObj.getManagerFullName();
					}

					if (newTeamDetailObj.getManagerFullName() != null) {
						newManagerName = newTeamDetailObj.getManagerFullName();
					}

					if (!oldManagerName.equalsIgnoreCase(newManagerName)) {
						CaseHistory caseHistory = new CaseHistory();
						if (!oldManagerName.isEmpty() && oldManagerName != null) {
							if (!newManagerName.isEmpty() && newManagerName != null) {
								caseHistory.setAction("Manager Reassigned");
								caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName()
										+ " and Manager Name:- " + oldManagerName);
								caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName()
										+ " and Manager Name:- " + newManagerName);
								caseHistoryList.add(caseHistory);
							}
						} else {
							caseHistory.setAction("Manager (Level 2) Assigned");
							caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName()
									+ " and Manager Name:- " + newManagerName);
							caseHistory.setOldInfo("");
							caseHistoryList.add(caseHistory);
						}
					}

					new ArrayList();
					List<CaseHistory> caseHistoryForAnalyst = this.getDeltaForAnalysts(oldTeamDetailObj,
							newTeamDetailObj);
					Iterator iterator = caseHistoryForAnalyst.iterator();

					while (iterator.hasNext()) {
						CaseHistory caseHistory = (CaseHistory) iterator.next();
						caseHistoryList.add(caseHistory);
					}

					new ArrayList();
					List<CaseHistory> caseHistoryForReviewer = this.getDeltaForReviewer(oldTeamDetailObj,
							newTeamDetailObj);
					Iterator iterator = caseHistoryForReviewer.iterator();

					while (iterator.hasNext()) {
						CaseHistory caseHistory = (CaseHistory) iterator.next();
						caseHistoryList.add(caseHistory);
					}
				}
			}
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		this.logger.debug("Exiting CaseHistoryEventManager:: setCaseHistoryForTeam");
		return caseHistoryList;
	}

	private List<CaseHistory> getDeltaForAnalysts(TeamDetails oldTeamDetail, TeamDetails newTeamDetail)
			throws CMSException {
		this.logger.debug("In CaseHistoryEventManager:: getDeltaForAnalysts");
		List<CaseHistory> caseHistoryList = new ArrayList();
		List<String> oldList = new ArrayList();
		Object newList = new ArrayList();

		try {
			if (oldTeamDetail.getAnalyst() != null) {
				oldList = oldTeamDetail.getAnalyst();
			}

			if (newTeamDetail.getAnalyst() != null) {
				newList = newTeamDetail.getAnalyst();
			}

			Iterator iterator;
			String loginID;
			for (iterator = ((List) oldList).iterator(); iterator.hasNext(); loginID = (String) iterator.next()) {
				;
			}

			iterator = ((List) newList).iterator();

			label65 : while (true) {
				if (!iterator.hasNext()) {
					if (((List) oldList).size() == ((List) newList).size()) {
						int i = 0;

						while (true) {
							if (i >= ((List) oldList).size()) {
								break label65;
							}

							if (!((String) ((List) oldList).get(i))
									.equalsIgnoreCase((String) ((List) newList).get(i))) {
								CaseHistory caseHistory = new CaseHistory();
								caseHistory.setAction("Analyst Reassigned");
								caseHistory.setOldInfo("Old Analyst Name:- " + (String) ((List) oldList).get(i)
										+ " Office:- " + oldTeamDetail.getOfficeName());
								caseHistory.setNewInfo("New Analyst Name:- " + (String) ((List) newList).get(i)
										+ "  Office:- " + newTeamDetail.getOfficeName());
								caseHistoryList.add(caseHistory);
							}

							++i;
						}
					} else {
						CaseHistory caseHistory;
						if (((List) oldList).size() > ((List) newList).size()) {
							iterator = ((List) oldList).iterator();

							while (true) {
								if (!iterator.hasNext()) {
									break label65;
								}

								loginID = (String) iterator.next();
								if (!((List) newList).contains(loginID)) {
									caseHistory = new CaseHistory();
									caseHistory.setAction("Analyst Deleted");
									caseHistory.setOldInfo("Old Analyst name:- " + loginID + " Office:- "
											+ oldTeamDetail.getOfficeName());
									caseHistory.setNewInfo("");
									caseHistoryList.add(caseHistory);
								}
							}
						} else {
							iterator = ((List) newList).iterator();

							while (true) {
								if (!iterator.hasNext()) {
									break label65;
								}

								loginID = (String) iterator.next();
								if (!((List) oldList).contains(loginID)) {
									caseHistory = new CaseHistory();
									caseHistory.setAction("Analyst Assigned");
									caseHistory.setOldInfo("");
									caseHistory.setNewInfo("New Analyst Office:- " + newTeamDetail.getOfficeName()
											+ " , Analyst Name:- " + loginID);
									caseHistoryList.add(caseHistory);
								}
							}
						}
					}
				}

				loginID = (String) iterator.next();
			}
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting CaseHistoryEventManager:: getDeltaForAnalysts");
		return caseHistoryList;
	}

	private List<CaseHistory> getDeltaForReviewer(TeamDetails oldTeamDetail, TeamDetails newTeamDetail)
			throws CMSException {
		List<CaseHistory> caseHistoryList = new ArrayList();
		this.logger.debug("In CaseHistoryEventManager:: getDeltaForReviewer");

		try {
			CaseHistory caseHistory;
			if (oldTeamDetail.getRev1FullName() != null && !oldTeamDetail.getRev1FullName().isEmpty()) {
				caseHistory = new CaseHistory();
				if (newTeamDetail.getRev1FullName() != null && !newTeamDetail.getRev1FullName().isEmpty()) {
					if (!oldTeamDetail.getRev1FullName().equalsIgnoreCase(newTeamDetail.getRev1FullName())) {
						caseHistory.setAction("Reviewer Reassigned");
						caseHistory.setOldInfo("Office:- " + oldTeamDetail.getOfficeName() + " , Reviewer:- "
								+ oldTeamDetail.getRev1FullName());
						caseHistory.setNewInfo("Office:- " + newTeamDetail.getOfficeName() + " , Reviewer:- "
								+ newTeamDetail.getRev1FullName());
						caseHistoryList.add(caseHistory);
					}
				} else {
					caseHistory.setAction("Reviewer Deleted");
					caseHistory.setOldInfo("Office:- " + oldTeamDetail.getOfficeName() + " , Reviewer:- "
							+ oldTeamDetail.getRev1FullName());
					caseHistory.setNewInfo("");
					caseHistoryList.add(caseHistory);
				}
			}

			if (oldTeamDetail.getRev2FullName() != null && !oldTeamDetail.getRev2FullName().isEmpty()) {
				caseHistory = new CaseHistory();
				if (newTeamDetail.getRev2FullName() != null && !newTeamDetail.getRev2FullName().isEmpty()) {
					if (!oldTeamDetail.getRev2FullName().equalsIgnoreCase(newTeamDetail.getRev2FullName())) {
						caseHistory.setAction("Reviewer Reassigned");
						caseHistory.setOldInfo("Office:- " + oldTeamDetail.getOfficeName() + " , Reviewer:- "
								+ oldTeamDetail.getRev2FullName());
						caseHistory.setNewInfo("Office:- " + newTeamDetail.getOfficeName() + " , Reviewer:- "
								+ newTeamDetail.getRev2FullName());
						caseHistoryList.add(caseHistory);
					}
				} else {
					caseHistory.setAction("Reviewer Deleted");
					caseHistory.setOldInfo("Office:- " + oldTeamDetail.getOfficeName() + " , Reviewer:- "
							+ oldTeamDetail.getRev2FullName());
					caseHistory.setNewInfo("");
					caseHistoryList.add(caseHistory);
				}
			}

			if (oldTeamDetail.getRev3FullName() != null && !oldTeamDetail.getRev3FullName().isEmpty()) {
				caseHistory = new CaseHistory();
				if (newTeamDetail.getRev3FullName() != null && !newTeamDetail.getRev3FullName().isEmpty()) {
					if (!oldTeamDetail.getRev3FullName().equalsIgnoreCase(newTeamDetail.getRev3FullName())) {
						caseHistory.setAction("Reviewer Reassigned");
						caseHistory.setOldInfo("Office:- " + oldTeamDetail.getOfficeName() + " , Reviewer:- "
								+ oldTeamDetail.getRev3FullName());
						caseHistory.setNewInfo("Office:- " + newTeamDetail.getOfficeName() + " , Reviewer:- "
								+ newTeamDetail.getRev3FullName());
						caseHistoryList.add(caseHistory);
					}
				} else {
					caseHistory.setAction("Reviewer Deleted");
					caseHistory.setOldInfo("Office:- " + oldTeamDetail.getOfficeName() + " , Reviewer:- "
							+ oldTeamDetail.getRev3FullName());
					caseHistory.setNewInfo("");
					caseHistoryList.add(caseHistory);
				}
			}
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryEventManager:: getDeltaForReviewer");
		return caseHistoryList;
	}

	private CaseHistory setCaseHistoryInfo(String action, String oldInfo, String newInfo, CaseHistory caseHistory)
			throws CMSException {
		this.logger.debug("In CaseHistoryEventManager:: setCaseHistoryInfo");

		try {
			caseHistory.setAction(action);
			caseHistory.setOldInfo(oldInfo);
			caseHistory.setNewInfo(newInfo);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting CaseHistoryEventManager:: setCaseHistoryInfo");
		return caseHistory;
	}

	public void setCaseHistoryForTaskCompleted(String taskName, String PId, String CRN, String processCycle,
			String taskStatus, String performer, String status) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForTaskCompleted");
		CaseHistory caseHistory = new CaseHistory();
		ArrayList caseHistoryList = new ArrayList();

		try {
			String actionStr = "";
			String actionReason = "";
			caseHistory.setPid(PId);
			caseHistory.setCRN(CRN);
			caseHistory.setProcessCycle(processCycle);
			caseHistory.setTaskName(taskName);
			caseHistory.setTaskStatus(taskStatus);
			caseHistory.setPerformer(performer);
			if (status.equalsIgnoreCase("Pullback")) {
				actionReason = "[Due to System Pullback]";
			} else if (status.equalsIgnoreCase("Reject")) {
				actionReason = "[Due to RE Rejection]";
			} else if (!"".equals(status)) {
				actionReason = "[Due to " + status + "]";
			}

			actionStr = taskName + " Completed " + actionReason;
			this.setCaseHistoryInfo(actionStr, "", "", caseHistory);
			caseHistoryList.add(caseHistory);
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
			this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForTaskCompleted");
		} catch (Exception var12) {
			this.logger.error(var12);
		}

	}

	public void setCaseHistoryForTaskStatusChange(CaseHistory caseHistoryForOtherParams) throws CMSException {
		this.logger.debug("In CaseHistoryEventManager:: setCaseHistoryForTaskStatusChange");

		try {
			String oldStatus = "";
			String newStatus = "";
			if (caseHistoryForOtherParams.getOldInfo() != null) {
				oldStatus = caseHistoryForOtherParams.getOldInfo();
			}

			if (caseHistoryForOtherParams.getNewInfo() != null) {
				newStatus = caseHistoryForOtherParams.getNewInfo();
			}

			this.logger.debug("newStatus " + newStatus);
			this.logger.debug("oldStatus " + oldStatus);
			if (!oldStatus.equalsIgnoreCase(newStatus)) {
				List<CaseHistory> caseHistoryList = new ArrayList();
				if (!caseHistoryForOtherParams.getTaskName().equalsIgnoreCase("Team Assignment Task")
						&& !caseHistoryForOtherParams.getTaskName().equalsIgnoreCase("Office Assignment Task")) {
					caseHistoryForOtherParams
							.setAction("Change of Task Status for " + caseHistoryForOtherParams.getTaskName());
					caseHistoryList.add(caseHistoryForOtherParams);
					this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
				} else {
					this.logger.debug(caseHistoryForOtherParams.getTaskName());
				}
			}

			this.logger.debug("Exitng CaseHistoryEventManager:: setCaseHistoryForTaskStatusChange");
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void setCaseHistoryForCaseStatusChange(String oldStatus, String newStatus,
			CaseHistory caseHistoryForOtherParams) throws CMSException {
		this.logger.debug("In CaseHistoryEventManager:: setCaseHistoryForCaseStatusChange");

		try {
			if (!oldStatus.equalsIgnoreCase(newStatus)) {
				String actionStr = "";
				if (newStatus.equalsIgnoreCase("Cancelled")) {
					actionStr = "Case Cancelled";
				} else if (newStatus.equalsIgnoreCase("On Hold")) {
					actionStr = "Case put On-Hold";
				} else if (newStatus.equalsIgnoreCase("In Progress")) {
					actionStr = "Case reverted back to In Progress";
				} else if (newStatus.equalsIgnoreCase("Completed")) {
					actionStr = "Case Completed";
				}

				if (!actionStr.isEmpty()) {
					CaseHistory caseHistory = new CaseHistory();
					List<CaseHistory> caseHistoryList = new ArrayList();
					this.setCaseHistoryOtherParams(caseHistoryForOtherParams, caseHistory);
					caseHistoryList.add(this.setCaseHistoryInfo(actionStr, "", "", caseHistory));
					this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
				}
			}
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting CaseHistoryEventManager:: setCaseHistoryForCaseStatusChange");
	}

	private List<CaseHistory> setCaseHIstoryForInvoicing(CaseDetails oldCaseDetail, CaseDetails newCaseDetail)
			throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHIstoryForInvoicing");
		ArrayList caseHistoryList = new ArrayList();

		try {
			if (oldCaseDetail != null && newCaseDetail != null) {
				this.updateNULLForInvoicing(oldCaseDetail);
				this.updateNULLForInvoicing(newCaseDetail);
				CaseHistory caseHistory;
				if (oldCaseDetail.getCapeTownDate() == null && newCaseDetail.getCapeTownDate() != null) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Update to \"CT Date\"");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo(newCaseDetail.getCapeTownDate().toString());
					caseHistoryList.add(caseHistory);
				} else if (oldCaseDetail.getCapeTownDate() != null && newCaseDetail.getCapeTownDate() != null
						&& oldCaseDetail.getCapeTownDate().compareTo(newCaseDetail.getCapeTownDate()) != 0) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Update to \"CT Date\"");
					caseHistory.setOldInfo(oldCaseDetail.getCapeTownDate().toString());
					caseHistory.setNewInfo(newCaseDetail.getCapeTownDate().toString());
					caseHistoryList.add(caseHistory);
				}

				if (oldCaseDetail.isCapeTown() != newCaseDetail.isCapeTown()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Update to \"To Cape Town checkbox\"");
					caseHistory.setOldInfo((new Boolean(oldCaseDetail.isCapeTown())).toString());
					caseHistory.setNewInfo((new Boolean(newCaseDetail.isCapeTown())).toString());
					caseHistoryList.add(caseHistory);
				}

				if (oldCaseDetail.getCaseFee() == null && newCaseDetail.getCaseFee() != null
						&& !newCaseDetail.getCaseFee().isEmpty()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Case Fee Changed");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo(newCaseDetail.getCaseFee());
					caseHistory.setIsVisibileForAll(0);
					caseHistoryList.add(caseHistory);
				} else if (oldCaseDetail.getCaseFee() != null && !oldCaseDetail.getCaseFee().isEmpty()
						&& newCaseDetail.getCaseFee() != null && !newCaseDetail.getCaseFee().isEmpty()) {
					if (Float.parseFloat(oldCaseDetail.getCaseFee()) != Float.parseFloat(newCaseDetail.getCaseFee())) {
						caseHistory = new CaseHistory();
						caseHistory.setAction("Case Fee Changed");
						caseHistory.setOldInfo(oldCaseDetail.getCaseFee());
						caseHistory.setNewInfo(newCaseDetail.getCaseFee());
						caseHistoryList.add(caseHistory);
					}
				} else if (oldCaseDetail.getCaseFee().isEmpty() && !newCaseDetail.getCaseFee().isEmpty()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Case Fee Changed");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo(newCaseDetail.getCaseFee());
					caseHistoryList.add(caseHistory);
				}

				if ((oldCaseDetail.getInvoiceNumber() == null || oldCaseDetail.getInvoiceNumber().isEmpty())
						&& newCaseDetail.getInvoiceNumber() != null && !newCaseDetail.getInvoiceNumber().isEmpty()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Invoice Number Added");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo(newCaseDetail.getInvoiceNumber());
					caseHistoryList.add(caseHistory);
				} else if (oldCaseDetail.getInvoiceNumber() != null && newCaseDetail.getInvoiceNumber() != null
						&& !oldCaseDetail.getInvoiceNumber().equals(newCaseDetail.getInvoiceNumber())) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Invoice Number Changed");
					caseHistory.setOldInfo(oldCaseDetail.getInvoiceNumber());
					caseHistory.setNewInfo(newCaseDetail.getInvoiceNumber());
					caseHistoryList.add(caseHistory);
				}

				if ((oldCaseDetail.getCaseCurrency() == null || oldCaseDetail.getCaseCurrency().isEmpty())
						&& newCaseDetail.getCaseCurrency() != null && !newCaseDetail.getCaseCurrency().isEmpty()) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Case Currency Changed");
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo(newCaseDetail.getCaseCurrency());
					caseHistory.setIsVisibileForAll(0);
					caseHistoryList.add(caseHistory);
				} else if (oldCaseDetail.getCaseCurrency() != null && newCaseDetail.getCaseCurrency() != null
						&& !oldCaseDetail.getCaseCurrency().equalsIgnoreCase(newCaseDetail.getCaseCurrency())) {
					caseHistory = new CaseHistory();
					caseHistory.setAction("Case Currency Changed");
					caseHistory.setOldInfo(oldCaseDetail.getCaseCurrency());
					caseHistory.setNewInfo(newCaseDetail.getCaseCurrency());
					caseHistory.setIsVisibileForAll(0);
					caseHistoryList.add(caseHistory);
				}
			}

			this.logger.debug("Exiting CaseHistoryEventManager::setCaseHIstoryForInvoicing");
			return caseHistoryList;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private List<CaseHistory> setCaseHistoryForVendor(List<VendorDetailVO> oldVendorDetail,
			List<VendorDetailVO> newVendorDetail) throws CMSException {
		this.logger.debug("In CaseHistoryEventManager::setCaseHistoryForVendor");
		this.logger.debug("oldVendorDetail size " + oldVendorDetail.size());
		this.logger.debug("newVendorDetail size " + newVendorDetail.size());
		CaseHistory caseHistory = new CaseHistory();
		ArrayList caseHistoryList = new ArrayList();

		try {
			VendorDetailVO vendorDetailObj;
			if (oldVendorDetail.size() < newVendorDetail.size()) {
				vendorDetailObj = (VendorDetailVO) newVendorDetail.get(0);
				caseHistory.setAction("Vendor Added");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo(this.replaceNullWithEmpty(vendorDetailObj.getVendorName()));
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			} else if (oldVendorDetail.size() > newVendorDetail.size()) {
				vendorDetailObj = (VendorDetailVO) oldVendorDetail.get(0);
				caseHistory.setAction("Vendor Deleted");
				caseHistory.setOldInfo(this.replaceNullWithEmpty(vendorDetailObj.getVendorName()));
				caseHistory.setNewInfo("");
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			} else {
				String oldInfoStr = "";
				String newInfoStr = "";
				String actionStr = "Vendor Details Updated";

				for (int i = 0; i < oldVendorDetail.size(); ++i) {
					VendorDetailVO oldVenDetailObj = (VendorDetailVO) oldVendorDetail.get(i);
					VendorDetailVO newVenDetailObj = (VendorDetailVO) newVendorDetail.get(i);
					this.updateNULLForVendorDetails(oldVenDetailObj);
					this.updateNULLForVendorDetails(newVenDetailObj);
					if (!oldVenDetailObj.getVendorStatus().equalsIgnoreCase(newVenDetailObj.getVendorStatus())) {
						oldInfoStr = oldInfoStr + "," + "Old Vendor Status:- " + oldVenDetailObj.getVendorStatus();
						newInfoStr = newInfoStr + "," + "New Vendor Status:- " + newVenDetailObj.getVendorStatus();
					}

					if (oldVenDetailObj.getJobReqSentDate().compareTo(newVenDetailObj.getJobReqSentDate()) != 0) {
						oldInfoStr = oldInfoStr + "," + "Old Job Request Sent Date:- "
								+ oldVenDetailObj.getJobReqSentDate();
						newInfoStr = newInfoStr + "," + "New Job Request Sent Date:- "
								+ newVenDetailObj.getJobReqSentDate();
					}

					if (oldVenDetailObj.getCommissionDate().compareTo(newVenDetailObj.getCommissionDate()) != 0) {
						oldInfoStr = oldInfoStr + "," + "Old Commission Date:- " + oldVenDetailObj.getCommissionDate();
						newInfoStr = newInfoStr + "," + "New Commission Date:- " + newVenDetailObj.getCommissionDate();
					}

					if (oldVenDetailObj.getVendorDueDate().compareTo(newVenDetailObj.getVendorDueDate()) != 0) {
						oldInfoStr = oldInfoStr + "," + "Old Vendor Due Date " + oldVenDetailObj.getVendorDueDate();
						newInfoStr = newInfoStr + "," + "New Vendor Due Date " + newVenDetailObj.getVendorDueDate();
					}

					if (oldVenDetailObj.getVendorSubDate().compareTo(newVenDetailObj.getVendorSubDate()) != 0) {
						oldInfoStr = oldInfoStr + "," + "Old Vendor Submission Date "
								+ oldVenDetailObj.getVendorSubDate();
						newInfoStr = newInfoStr + "," + "New Vendor Submission Date "
								+ newVenDetailObj.getVendorSubDate();
					}

					if (!oldVenDetailObj.getImmediateAttention()
							.equalsIgnoreCase(newVenDetailObj.getImmediateAttention())) {
						oldInfoStr = oldInfoStr + "," + "Old Immediate Attention"
								+ oldVenDetailObj.getImmediateAttention();
						newInfoStr = newInfoStr + "," + "New Immediate Attention"
								+ newVenDetailObj.getImmediateAttention();
					}

					if (!oldVenDetailObj.getCurrency().equalsIgnoreCase(newVenDetailObj.getCurrency())) {
						oldInfoStr = oldInfoStr + "," + "Old Vendor Currency " + oldVenDetailObj.getCurrency();
						newInfoStr = newInfoStr + "," + "New Vendor Currency " + newVenDetailObj.getCurrency();
					}

					if (!oldVenDetailObj.getCost().equalsIgnoreCase(newVenDetailObj.getCost())) {
						oldInfoStr = oldInfoStr + "," + "Old Cost " + oldVenDetailObj.getCost();
						newInfoStr = newInfoStr + "," + "New Cost " + newVenDetailObj.getCost();
					}

					if (!oldVenDetailObj.getInvoiceNumber().equalsIgnoreCase(newVenDetailObj.getInvoiceNumber())) {
						oldInfoStr = oldInfoStr + "," + "Old Invoice Number " + oldVenDetailObj.getInvoiceNumber();
						newInfoStr = newInfoStr + "," + "New Invoice Number " + newVenDetailObj.getInvoiceNumber();
					}

					if (!oldVenDetailObj.getVendorMgrMesToVendor()
							.equalsIgnoreCase(newVenDetailObj.getVendorMgrMesToVendor())) {
						oldInfoStr = oldInfoStr + "," + "Old Vendor Manager Message "
								+ oldVenDetailObj.getVendorMgrMesToVendor();
						newInfoStr = newInfoStr + "," + "New Vendor Manager Message "
								+ newVenDetailObj.getVendorMgrMesToVendor();
					}

					if (!oldVenDetailObj.getVendorComments().equalsIgnoreCase(newVenDetailObj.getVendorComments())) {
						oldInfoStr = oldInfoStr + "," + "Old Vendor Comments " + oldVenDetailObj.getVendorComments();
						newInfoStr = newInfoStr + "," + "New Vendor Comments " + newVenDetailObj.getVendorComments();
					}

					if (!oldVenDetailObj.getNameSearch().equalsIgnoreCase(newVenDetailObj.getNameSearch())) {
						oldInfoStr = oldInfoStr + "," + "Old Name Search " + oldVenDetailObj.getNameSearch();
						newInfoStr = newInfoStr + "," + "New Name Search " + newVenDetailObj.getNameSearch();
					}
				}

				if (!oldInfoStr.isEmpty() || !newInfoStr.isEmpty()) {
					caseHistory.setAction(actionStr);
					if (oldInfoStr.charAt(0) == ',') {
						oldInfoStr = oldInfoStr.substring(1, oldInfoStr.length());
					}

					if (newInfoStr.charAt(0) == ',') {
						newInfoStr = newInfoStr.substring(1, newInfoStr.length());
					}

					caseHistory.setOldInfo(oldInfoStr);
					caseHistory.setNewInfo(newInfoStr);
					caseHistory.setIsVisibileForAll(0);
					caseHistoryList.add(caseHistory);
				}
			}

			this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForVendor");
			return caseHistoryList;
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private List<CaseHistory> setCaseHistoryForCaseDetails(CaseDetails oldCaseDetail, CaseDetails newCaseDetail)
			throws CMSException {
		ArrayList caseHistoryList = new ArrayList();

		try {
			this.logger.debug("In CaseHistoryEventManager::setCaseHistoryForCaseDetails");
			this.updateNULLForCaseDetails(oldCaseDetail);
			this.updateNULLForCaseDetails(newCaseDetail);
			CaseHistory caseHistory;
			if (oldCaseDetail.getcInterim1() == null && newCaseDetail.getcInterim1() != null) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Client Interim 1 Due Date Updated");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo(newCaseDetail.getcInterim1().toString());
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			} else if (oldCaseDetail.getcInterim1() != null && newCaseDetail.getcInterim1() != null
					&& oldCaseDetail.getcInterim1().compareTo(newCaseDetail.getcInterim1()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Client Interim 1 Due Date Updated");
				caseHistory.setOldInfo(oldCaseDetail.getcInterim1().toString());
				caseHistory.setNewInfo(newCaseDetail.getcInterim1().toString());
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			}

			if (oldCaseDetail.getcInterim2() == null && newCaseDetail.getcInterim2() != null) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Client Interim 2 Due Date Updated");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo(newCaseDetail.getcInterim2().toString());
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			} else if (oldCaseDetail.getcInterim2() != null && newCaseDetail.getcInterim2() != null
					&& oldCaseDetail.getcInterim2().compareTo(newCaseDetail.getcInterim2()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Client Interim 2 Due Date Updated");
				caseHistory.setOldInfo(oldCaseDetail.getcInterim2().toString());
				caseHistory.setNewInfo(newCaseDetail.getcInterim2().toString());
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			}

			if (oldCaseDetail.getFinalDueDate().compareTo(newCaseDetail.getFinalDueDate()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Client Final Due Date Updated");
				caseHistory.setOldInfo(oldCaseDetail.getFinalDueDate().toString());
				caseHistory.setNewInfo(newCaseDetail.getFinalDueDate().toString());
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			}

			if (!oldCaseDetail.getCaseManagerName().equalsIgnoreCase(newCaseDetail.getCaseManagerName())) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Case Manager Reassigned");
				caseHistory.setOldInfo(oldCaseDetail.getCaseManagerName());
				caseHistory.setNewInfo(newCaseDetail.getCaseManagerName());
				caseHistoryList.add(caseHistory);
			}

			if (!oldCaseDetail.getCaseInfo().equalsIgnoreCase(newCaseDetail.getCaseInfo())) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Case information Updated");
				caseHistory.setOldInfo(oldCaseDetail.getCaseInfo());
				caseHistory.setNewInfo(newCaseDetail.getCaseInfo());
				caseHistoryList.add(caseHistory);
			}

			if (oldCaseDetail.getExpressCase() != newCaseDetail.getExpressCase()) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Express Case Status Changed");
				String oldIsExpress = oldCaseDetail.getExpressCase() == 0 ? "False" : "True";
				String newIsExpress = newCaseDetail.getExpressCase() == 0 ? "False" : "True";
				caseHistory.setOldInfo(oldIsExpress);
				caseHistory.setNewInfo(newIsExpress);
				caseHistoryList.add(caseHistory);
			}

			if (oldCaseDetail.getrInterim1() == null && newCaseDetail.getrInterim1() != null) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Interim 1 process cycle added");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo(newCaseDetail.getrInterim1().toString());
				caseHistoryList.add(caseHistory);
			} else if (oldCaseDetail.getrInterim1() != null && newCaseDetail.getrInterim1() != null
					&& oldCaseDetail.getrInterim1().compareTo(newCaseDetail.getrInterim1()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Research Interim 1 Due Date Updated");
				caseHistory.setOldInfo(oldCaseDetail.getrInterim1().toString());
				caseHistory.setNewInfo(newCaseDetail.getrInterim1().toString());
				caseHistoryList.add(caseHistory);
			}

			if (oldCaseDetail.getrInterim2() == null && newCaseDetail.getrInterim2() != null) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Interim 2 process cycle added");
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo(newCaseDetail.getrInterim2().toString());
				caseHistoryList.add(caseHistory);
			} else if (oldCaseDetail.getrInterim2() != null && newCaseDetail.getrInterim2() != null
					&& oldCaseDetail.getrInterim2().compareTo(newCaseDetail.getrInterim2()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Research Interim 2 Due Date Updated");
				caseHistory.setOldInfo(oldCaseDetail.getrInterim2().toString());
				caseHistory.setNewInfo(newCaseDetail.getrInterim2().toString());
				caseHistoryList.add(caseHistory);
			}

			if (oldCaseDetail.getFinalRDueDate().compareTo(newCaseDetail.getFinalRDueDate()) != 0) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Research Final Due Date Updated");
				caseHistory.setOldInfo(oldCaseDetail.getFinalRDueDate().toString());
				caseHistory.setNewInfo(newCaseDetail.getFinalRDueDate().toString());
				caseHistoryList.add(caseHistory);
			}

			if (!oldCaseDetail.getCaseStatus().equalsIgnoreCase(newCaseDetail.getCaseStatus())) {
				String actionStr = "";
				if (newCaseDetail.getCaseStatus().equalsIgnoreCase("Cancelled")) {
					actionStr = "Case Cancelled";
				} else if (newCaseDetail.getCaseStatus().equalsIgnoreCase("On hold")) {
					actionStr = "Case put On-Hold";
				} else if (newCaseDetail.getCaseStatus().equalsIgnoreCase("In Progress")) {
					actionStr = "Case reverted back to In Progress";
				}

				if (!actionStr.isEmpty()) {
					CaseHistory caseHistory = new CaseHistory();
					caseHistory.setAction(actionStr);
					caseHistory.setOldInfo("");
					caseHistory.setNewInfo("");
					caseHistoryList.add(caseHistory);
				}
			}

			if (oldCaseDetail.getSalesRepresentative() != null && !oldCaseDetail.getSalesRepresentative()
					.equalsIgnoreCase(newCaseDetail.getSalesRepresentative())) {
				caseHistory = new CaseHistory();
				caseHistory.setAction("Sales Representative Updated");
				caseHistory.setOldInfo(oldCaseDetail.getSalesRepresentative());
				caseHistory.setNewInfo(newCaseDetail.getSalesRepresentative());
				caseHistoryList.add(caseHistory);
			}
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForCaseDetails");
		return caseHistoryList;
	}

	public void setCaseHistoryForConfirmBudgetAndDueDate(List<UnconfimredBudgetVO> unconfimredBudgetVOList)
			throws CMSException {
		List<CaseHistory> caseHistoryList = new ArrayList();
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForConfirmBudgetAndDueDate");

		try {
			Iterator iterator = unconfimredBudgetVOList.iterator();

			while (iterator.hasNext()) {
				UnconfimredBudgetVO unconfimredBudgetVOObj = (UnconfimredBudgetVO) iterator.next();
				CaseHistory caseHistory = new CaseHistory();
				String newInfo = "Final Due date of Case is:- "
						+ this.replaceNullWithEmpty(unconfimredBudgetVOObj.getFinalDate()) + " and Case Fee is :- "
						+ unconfimredBudgetVOObj.getCaseFee();
				if (unconfimredBudgetVOObj.getIsSubreportRequire().equalsIgnoreCase("true")) {
					(new StringBuilder(String.valueOf(newInfo))).append("\r\n")
							.append(unconfimredBudgetVOObj.getNewInfo()).toString();
				}

				caseHistory.setCRN(this.replaceNullWithEmpty(unconfimredBudgetVOObj.getCRN()));
				caseHistory.setPid(this.replaceNullWithEmpty(String.valueOf(unconfimredBudgetVOObj.getPid())));
				caseHistory.setProcessCycle("");
				caseHistory.setAction("Confirmed Budget and Due Date");
				caseHistory.setTaskName("");
				caseHistory.setTaskStatus("");
				caseHistory.setPerformer(this.replaceNullWithEmpty(unconfimredBudgetVOObj.getCaseHistoryPerformer()));
				caseHistory.setOldInfo("");
				caseHistory.setNewInfo("Final Due date of Case is:- "
						+ this.replaceNullWithEmpty(unconfimredBudgetVOObj.getFinalDate()) + " and Case Fee is :- "
						+ unconfimredBudgetVOObj.getCaseFee());
				caseHistory.setIsVisibileForAll(0);
				caseHistoryList.add(caseHistory);
			}

			this.logger.debug("caseHistoryList.size " + caseHistoryList.size());
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<CaseHistory> setCaseHistoryForReviewScoresheet(String action, CaseHistory caseHistoryOtherParam)
			throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForReviewScoresheet");
		List<CaseHistory> caseHistoryList = new ArrayList();
		new CaseHistory();

		try {
			this.updateNULLForCaseHistoryParams(caseHistoryOtherParam);
			caseHistoryList.add(this.setCaseHistoryInfo("Review Scoresheet " + action, "", "", caseHistoryOtherParam));
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForReviewScoresheet");
		return caseHistoryList;
	}

	private List<CaseHistory> setCaseHistoryForREShuffling(List<TeamDetails> oldTeamDetailsList,
			List<TeamDetails> newTeamDetailsList, String crn) throws CMSException {
		this.logger.debug("In CaseHistoryEventManager::setCaseHistoryForREShuffling :- " + crn);
		ArrayList caseHistoryList = new ArrayList();

		try {
			List<REMasterVO> reIndividualList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("RE_MASTER_INDIVIDUAL");
			List<REMasterVO> reCompanyList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("RE_MASTER_COMPANY");
			Map<String, String> reIdNameMap = new HashMap();
			List<String> crnList = new ArrayList();
			crnList.add(crn);
			Map<BigDecimal, String> subjectMap = this.caseHistoryDAO.getSubjectMap(crnList);
			Iterator iterator = reIndividualList.iterator();

			REMasterVO reMasterVO;
			while (iterator.hasNext()) {
				reMasterVO = (REMasterVO) iterator.next();
				reIdNameMap.put(reMasterVO.getrEMasterId(), reMasterVO.getName());
			}

			iterator = reCompanyList.iterator();

			while (iterator.hasNext()) {
				reMasterVO = (REMasterVO) iterator.next();
				reIdNameMap.put(reMasterVO.getrEMasterId(), reMasterVO.getName());
			}

			List<String> oldSubTeamReStringList = new ArrayList();
			List<String> newSubTeamReStringList = new ArrayList();
			Map<String, List<String>> oldMap = new HashMap();
			Map<String, List<String>> newMap = new HashMap();
			Map<String, String> oldSubRETeamNamesMap = new HashMap();
			Map<String, String> newSubRETeamNamesMap = new HashMap();
			this.logger.debug("oldTeamDetailsList " + oldTeamDetailsList.size());
			this.logger.debug("newTeamDetailsList " + newTeamDetailsList.size());
			Iterator mapIterator = oldTeamDetailsList.iterator();

			label122 : while (true) {
				TeamDetails newTeamDetailObj;
				List oldTeamIDs;
				Iterator iterator2;
				String str;
				SubTeamReMapVO subTeamReMapVO;
				do {
					if (!mapIterator.hasNext()) {
						this.logger.debug("Old team details iterated");
						mapIterator = newTeamDetailsList.iterator();

						while (true) {
							do {
								if (!mapIterator.hasNext()) {
									this.logger.debug("New team details iterated");
									mapIterator = oldSubTeamReStringList.iterator();

									while (true) {
										String key;
										String subReId;
										while (mapIterator.hasNext()) {
											key = (String) mapIterator.next();
											subReId = key.substring(0, key.indexOf("$"));
											int strIndexOfAnd = key.indexOf("&&&");
											str = key.substring(key.indexOf("$") + 1, key.length());
											List<String> teamListForKey = new ArrayList();
											if (oldMap != null && oldMap.containsKey(subReId)) {
												List<String> teamListForKey = (List) oldMap.get(subReId);
												teamListForKey.add(str);
												oldMap.remove(subReId);
												oldMap.put(subReId, teamListForKey);
												String csvTeamName = (String) oldSubRETeamNamesMap.get(subReId);
												csvTeamName = csvTeamName + " , "
														+ str.substring(str.indexOf("&&&") + 3, str.length());
												oldSubRETeamNamesMap.remove(subReId);
												oldSubRETeamNamesMap.put(subReId, csvTeamName);
											} else {
												teamListForKey.add(str);
												oldMap.put(subReId, teamListForKey);
												oldSubRETeamNamesMap.put(subReId,
														str.substring(str.indexOf("&&&") + 3, str.length()));
											}
										}

										this.logger.debug("Old Map Created");
										mapIterator = newSubTeamReStringList.iterator();

										while (true) {
											String newTeamName;
											while (mapIterator.hasNext()) {
												key = (String) mapIterator.next();
												subReId = key.substring(0, key.indexOf("$"));
												String teamId = key.substring(key.indexOf("$") + 1, key.length());
												List<String> teamListForKey = new ArrayList();
												if (newMap != null && newMap.containsKey(subReId)) {
													List<String> teamListForKey = (List) newMap.get(subReId);
													teamListForKey.add(teamId);
													newMap.remove(subReId);
													newMap.put(subReId, teamListForKey);
													newTeamName = (String) newSubRETeamNamesMap.get(subReId);
													newTeamName = newTeamName + " , " + teamId
															.substring(teamId.indexOf("&&&") + 3, teamId.length());
													newSubRETeamNamesMap.remove(subReId);
													newSubRETeamNamesMap.put(subReId, newTeamName);
												} else {
													teamListForKey.add(teamId);
													newMap.put(subReId, teamListForKey);
													newSubRETeamNamesMap.put(subReId, teamId
															.substring(teamId.indexOf("&&&") + 3, teamId.length()));
												}
											}

											this.logger.debug("New Map Created");
											String oldTeamNames;
											String newTeamNames;
											String rEName;
											String rEId;
											String subjectId;
											String subjectName;
											List newTeamIDs;
											CaseHistory caseHistory;
											int i;
											if (oldMap != null) {
												mapIterator = oldMap.keySet().iterator();

												label192 : while (true) {
													while (true) {
														do {
															do {
																if (!mapIterator.hasNext()) {
																	break label192;
																}

																key = (String) mapIterator.next();
																oldTeamIDs = (List) oldMap.get(key);
																newTeamIDs = (List) newMap.get(key);
															} while (oldTeamIDs == null);
														} while (oldTeamIDs.size() <= 0);

														for (i = 0; i < oldTeamIDs.size(); ++i) {
															newTeamName = (String) oldTeamIDs.get(i);
															if (newTeamIDs != null
																	&& !newTeamIDs.contains(newTeamName)) {
																caseHistory = new CaseHistory();
																caseHistory.setAction("Research Elements Reassigned");
																oldTeamNames = (String) oldSubRETeamNamesMap.get(key);
																newTeamNames = (String) newSubRETeamNamesMap.get(key);
																rEName = "";
																rEId = "";
																subjectId = "";
																subjectName = "";
																subjectId = key.substring(0, key.indexOf("##"));
																rEId = key.substring(key.indexOf("##") + 2,
																		key.length());
																rEName = (String) reIdNameMap.get(rEId);
																subjectName = (String) subjectMap.get(BigDecimal
																		.valueOf((long) Integer.parseInt(subjectId)));
																caseHistory.setOldInfo("Subject:- " + subjectName
																		+ " ,Research Element:- " + rEName
																		+ " ,Teams:- " + oldTeamNames);
																caseHistory.setNewInfo("Subject:- " + subjectName
																		+ " ,Research Element:- " + rEName
																		+ " ,Teams:- " + newTeamNames);
																caseHistoryList.add(caseHistory);
																newMap.remove(key);
																break;
															}
														}
													}
												}
											}

											this.logger.debug("Start iterating for new map");
											if (newMap != null) {
												mapIterator = newMap.keySet().iterator();

												while (true) {
													while (true) {
														do {
															do {
																if (!mapIterator.hasNext()) {
																	break label122;
																}

																key = (String) mapIterator.next();
																oldTeamIDs = (List) oldMap.get(key);
																newTeamIDs = (List) newMap.get(key);
															} while (newTeamIDs == null);
														} while (newTeamIDs.size() <= 0);

														for (i = 0; i < newTeamIDs.size(); ++i) {
															newTeamName = (String) newTeamIDs.get(i);
															if (oldTeamIDs != null
																	&& !oldTeamIDs.contains(newTeamName)) {
																caseHistory = new CaseHistory();
																caseHistory.setAction("Research Elements Reassigned");
																oldTeamNames = (String) oldSubRETeamNamesMap.get(key);
																newTeamNames = (String) newSubRETeamNamesMap.get(key);
																rEName = "";
																rEId = "";
																subjectId = "";
																subjectName = "";
																subjectId = key.substring(0, key.indexOf("##"));
																rEId = key.substring(key.indexOf("##") + 2,
																		key.length());
																rEName = (String) reIdNameMap.get(rEId);
																subjectName = (String) subjectMap.get(BigDecimal
																		.valueOf((long) Integer.parseInt(subjectId)));
																caseHistory.setOldInfo("Subject:- " + subjectName
																		+ " ,Research Element:- " + rEName
																		+ " ,Teams:- " + oldTeamNames);
																caseHistory.setNewInfo("Subject:- " + subjectName
																		+ " ,Research Element:- " + rEName
																		+ " ,Teams:- " + newTeamNames);
																caseHistoryList.add(caseHistory);
																break;
															}
														}
													}
												}
											}
											break label122;
										}
									}
								}

								newTeamDetailObj = (TeamDetails) mapIterator.next();
								oldTeamIDs = newTeamDetailObj.getTeamSubjectREDetails();
							} while (oldTeamIDs == null);

							for (iterator2 = oldTeamIDs.iterator(); iterator2.hasNext(); newSubTeamReStringList
									.add(str)) {
								str = "";
								subTeamReMapVO = (SubTeamReMapVO) iterator2.next();
								if (!newTeamDetailObj.getTeamType().contains("BI")
										&& !newTeamDetailObj.getTeamType().contains("Vendor")) {
									str = subTeamReMapVO.getSubjectId() + "##" + subTeamReMapVO.getReId() + "$"
											+ subTeamReMapVO.getTeamId() + "&&&" + newTeamDetailObj.getTeamType() + "["
											+ newTeamDetailObj.getOfficeName() + "]";
								} else {
									str = subTeamReMapVO.getSubjectId() + "##" + subTeamReMapVO.getReId() + "$"
											+ subTeamReMapVO.getTeamId() + "&&&" + newTeamDetailObj.getTeamType() + "["
											+ newTeamDetailObj.getManagerFullName() + "]";
								}
							}
						}
					}

					newTeamDetailObj = (TeamDetails) mapIterator.next();
					oldTeamIDs = newTeamDetailObj.getTeamSubjectREDetails();
				} while (oldTeamIDs == null);

				for (iterator2 = oldTeamIDs.iterator(); iterator2.hasNext(); oldSubTeamReStringList.add(str)) {
					str = "";
					subTeamReMapVO = (SubTeamReMapVO) iterator2.next();
					if (!newTeamDetailObj.getTeamType().contains("BI")
							&& !newTeamDetailObj.getTeamType().contains("Vendor")) {
						str = subTeamReMapVO.getSubjectId() + "##" + subTeamReMapVO.getReId() + "$"
								+ subTeamReMapVO.getTeamId() + "&&&" + newTeamDetailObj.getTeamType() + "["
								+ newTeamDetailObj.getOfficeName() + "]";
					} else {
						str = subTeamReMapVO.getSubjectId() + "##" + subTeamReMapVO.getReId() + "$"
								+ subTeamReMapVO.getTeamId() + "&&&" + newTeamDetailObj.getTeamType() + "["
								+ newTeamDetailObj.getManagerFullName() + "]";
					}
				}
			}
		} catch (Exception var29) {
			throw new CMSException(this.logger, var29);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForREShuffling");
		return caseHistoryList;
	}

	public List<CaseHistory> getCaseHistory(String CRN, int start, int limit, String columnName, String sortType,
			List<String> roleList) throws CMSException {
		this.logger.debug("IN CaseHistoryEventManager::getCaseHistory");
		new ArrayList();
		HashMap paramList = new HashMap();

		List caseHistoryList;
		try {
			paramList.put("CRN", CRN);
			paramList.put("start", new Integer(start + 1));
			paramList.put("limit", new Integer(start + limit));
			paramList.put("sort", columnName);
			paramList.put("dir", sortType);
			int isEligibleForCompleteCaseHistory = 0;
			Iterator iterator = roleList.iterator();

			while (true) {
				if (iterator.hasNext()) {
					String role = (String) iterator.next();
					if (!role.equals("R1") && !role.equals("R2") && !role.equals("R3") && !role.equals("R6")
							&& !role.equals("R7") && !role.equals("R12")) {
						continue;
					}

					isEligibleForCompleteCaseHistory = 1;
				}

				paramList.put("isEligibleForCompleteCaseHistory", Integer.valueOf(isEligibleForCompleteCaseHistory));
				caseHistoryList = this.caseHistoryDAO.selectCaseHistoryDetails(paramList);
				break;
			}
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("IN CaseHistoryEventManager::getCaseHistory");
		return caseHistoryList;
	}

	public int getCaseHistoryCountForCRN(String CRN, List<String> roleList) throws CMSException {
		this.logger.debug("IN CaseHistoryEventManager::getCaseHistoryCountForCRN");
		int count = false;
		HashMap paramList = new HashMap();

		int count;
		try {
			paramList.put("CRN", CRN);
			int isEligibleForCompleteCaseHistory = 0;
			Iterator iterator = roleList.iterator();

			while (true) {
				if (iterator.hasNext()) {
					String role = (String) iterator.next();
					if (!role.equals("R1") && !role.equals("R2") && !role.equals("R3") && !role.equals("R6")
							&& !role.equals("R7")) {
						continue;
					}

					isEligibleForCompleteCaseHistory = 1;
				}

				paramList.put("isEligibleForCompleteCaseHistory", Integer.valueOf(isEligibleForCompleteCaseHistory));
				count = Integer.valueOf(this.caseHistoryDAO.getCaseHistoryCountForCrn(paramList));
				break;
			}
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::getCaseHistoryCountForCRN");
		return count;
	}

	public void setCaseHistoryForMassVendorUpload(List<CaseHistory> caseHistoryList) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForMassVendorUpload");
		this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
		this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForMassVendorUpload");
	}

	public void setCaseHistoryForRejection(CaseHistory caseHistoryOtherParam) throws CMSException {
		ArrayList caseHistoryList = new ArrayList();

		try {
			caseHistoryOtherParam.setAction("Rejected at " + caseHistoryOtherParam.getTaskName());
			caseHistoryOtherParam.setOldInfo("");
			caseHistoryList.add(caseHistoryOtherParam);
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void setCaseHistoryForComments(String deletedComments, CaseHistory caseHistoryOtherParam)
			throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForComments");
		ArrayList caseHistoryList = new ArrayList();

		try {
			caseHistoryOtherParam.setAction("Comments Deleted from Comment Section");
			caseHistoryOtherParam.setOldInfo(deletedComments);
			caseHistoryOtherParam.setNewInfo("");
			caseHistoryList.add(caseHistoryOtherParam);
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForComments");
	}

	private void updateNULLForSubject(SubjectDetails subjectDetailsObj) {
		this.logger.debug("in CaseHistoryEventManager::updateNULLForSubject");
		subjectDetailsObj.setSubjectName(this.replaceNullWithEmpty(subjectDetailsObj.getSubjectName()));
		subjectDetailsObj.setEntityName(this.replaceNullWithEmpty(subjectDetailsObj.getEntityName()));
		subjectDetailsObj.setPosition(this.replaceNullWithEmpty(subjectDetailsObj.getPosition()));
		subjectDetailsObj.setCountryName(this.replaceNullWithEmpty(subjectDetailsObj.getCountryName()));
		subjectDetailsObj.setAddress(this.replaceNullWithEmpty(subjectDetailsObj.getAddress()));
		subjectDetailsObj.setOtherDetails(this.replaceNullWithEmpty(subjectDetailsObj.getOtherDetails()));
		subjectDetailsObj.setReNames(this.replaceNullWithEmpty(subjectDetailsObj.getReNames()));
		subjectDetailsObj.setSubReportType(this.replaceNullWithEmpty(subjectDetailsObj.getSubReportType()));
	}

	private void updateNULLForCaseDetails(CaseDetails caseDetails) {
		this.logger.debug("in CaseHistoryEventManager::updateNULLForCaseDetails");
		caseDetails.setCaseInfo(this.replaceNullWithEmpty(caseDetails.getCaseInfo()));
		caseDetails.setCaseManagerName(this.replaceNullWithEmpty(caseDetails.getCaseManagerName()));
		caseDetails.setClientRef(this.replaceNullWithEmpty(caseDetails.getClientRef()));
		caseDetails.setOfficeName(this.replaceNullWithEmpty(caseDetails.getOfficeName()));
		caseDetails.setReqRecdDate1(this.replaceNullWithEmpty(caseDetails.getReqRecdDate1()));
		caseDetails.setClientName(this.replaceNullWithEmpty(caseDetails.getClientName()));
		caseDetails.setCaseStatus(this.replaceNullWithEmpty(caseDetails.getCaseStatus()));
	}

	private void updateNULLForOfficeAssignment(TeamDetails teamDetails) {
		this.logger.debug("in CaseHistoryEventManager::updateNULLForOfficeAssignment");
		teamDetails.setTeamType(this.replaceNullWithEmpty(teamDetails.getTeamType()));
		teamDetails.setOfficeName(this.replaceNullWithEmpty(teamDetails.getOfficeName()));
		teamDetails.setFinalDueDate(this.replaceNullWithEmpty(teamDetails.getFinalDueDate()));
		teamDetails.setManagerName(this.replaceNullWithEmpty(teamDetails.getManagerName()));
		teamDetails.setManagerFullName(this.replaceNullWithEmpty(teamDetails.getManagerFullName()));
		teamDetails.setTeamTypeId(this.replaceNullWithEmpty(teamDetails.getTeamTypeId()));
	}

	private void updateNULLForVendorDetails(VendorDetailVO vendorDetailVO) {
		this.logger.debug("in CaseHistoryEventManager::updateNULLForVendorDetails");
		vendorDetailVO.setVendorName(this.replaceNullWithEmpty(vendorDetailVO.getVendorName()));
		vendorDetailVO.setVendorStatus(this.replaceNullWithEmpty(vendorDetailVO.getVendorStatus()));
		vendorDetailVO.setImmediateAttention(this.replaceNullWithEmpty(vendorDetailVO.getImmediateAttention()));
		vendorDetailVO.setCurrency(this.replaceNullWithEmpty(vendorDetailVO.getCurrency()));
		vendorDetailVO.setCost(this.replaceNullWithEmpty(vendorDetailVO.getCost()));
		vendorDetailVO.setInvoiceNumber(this.replaceNullWithEmpty(vendorDetailVO.getInvoiceNumber()));
		vendorDetailVO.setVendorMgrMesToVendor(this.replaceNullWithEmpty(vendorDetailVO.getVendorMgrMesToVendor()));
		vendorDetailVO.setVendorComments(this.replaceNullWithEmpty(vendorDetailVO.getVendorComments()));
		vendorDetailVO.setNameSearch(this.replaceNullWithEmpty(vendorDetailVO.getNameSearch()));
		vendorDetailVO.setJobReqSentDate(this.replaceNullWithEmpty(vendorDetailVO.getJobReqSentDate()));
		vendorDetailVO.setCommissionDate(this.replaceNullWithEmpty(vendorDetailVO.getCommissionDate()));
		vendorDetailVO.setVendorDueDate(this.replaceNullWithEmpty(vendorDetailVO.getVendorDueDate()));
		vendorDetailVO.setVendorSubDate(this.replaceNullWithEmpty(vendorDetailVO.getVendorSubDate()));
	}

	private void updateNULLForCaseHistoryParams(CaseHistory caseHistory) {
		this.logger.debug("in CaseHistoryEventManager::updateNULLForCaseHistoryParams");
		caseHistory.setTaskName(this.replaceNullWithEmpty(caseHistory.getTaskName()));
		caseHistory.setTaskStatus(this.replaceNullWithEmpty(caseHistory.getTaskStatus()));
		caseHistory.setPerformer(this.replaceNullWithEmpty(caseHistory.getPerformer()));
		caseHistory.setPid(this.replaceNullWithEmpty(caseHistory.getPid()));
		caseHistory.setProcessCycle(this.replaceNullWithEmpty(caseHistory.getProcessCycle()));
	}

	private void updateNULLForInvoicing(CaseDetails caseDetails) {
		this.logger.debug("in CaseHistoryEventManager::updateNULLForInvoicing");
		caseDetails.setCaseFee(this.replaceNullWithEmpty(caseDetails.getCaseFee()));
		caseDetails.setInvoiceNumber(this.replaceNullWithEmpty(caseDetails.getInvoiceNumber()));
		caseDetails.setCaseCurrency(this.replaceNullWithEmpty(caseDetails.getCaseCurrency()));
	}

	private void updateNullForTeamAssignmentTeamDetails(TeamDetails teamDetailsObj) {
		this.logger.debug("in CaseHistoryEventManager::updateNullForTeamAssignmentTeamDetails");
		teamDetailsObj.setTeamType(this.replaceNullWithEmpty(teamDetailsObj.getTeamType()));
		teamDetailsObj.setManagerFullName(this.replaceNullWithEmpty(teamDetailsObj.getManagerFullName()));
		teamDetailsObj.setOfficeName(this.replaceNullWithEmpty(teamDetailsObj.getOfficeName()));
		teamDetailsObj.setRev1FullName(this.replaceNullWithEmpty(teamDetailsObj.getRev1FullName()));
		teamDetailsObj.setRev2FullName(this.replaceNullWithEmpty(teamDetailsObj.getRev2FullName()));
		teamDetailsObj.setRev3FullName(this.replaceNullWithEmpty(teamDetailsObj.getRev3FullName()));
	}

	private String replaceNullWithEmpty(String attributeName) {
		return attributeName != null && !attributeName.isEmpty() ? attributeName : "";
	}

	public void setCaseHistoryForBulkOfficeAssignment(List<CaseDetails> oldCaseDetailsList,
			List<CaseDetails> newCaseDetailsList) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForBulkOfficeAssignment");

		try {
			List<CaseHistory> completeCaseHistoryList = new ArrayList();
			this.logger.debug("oldCaseDetailsList size " + oldCaseDetailsList.size());
			this.logger.debug("newCaseDetailsList size " + newCaseDetailsList.size());

			for (int i = 0; i < newCaseDetailsList.size(); ++i) {
				CaseDetails oldCaseDetails = new CaseDetails();
				CaseDetails newCaseDetails = new CaseDetails();
				new ArrayList();
				if (oldCaseDetailsList.get(i) != null) {
					oldCaseDetails = (CaseDetails) oldCaseDetailsList.get(i);
				}

				if (newCaseDetailsList.get(i) != null) {
					newCaseDetails = (CaseDetails) newCaseDetailsList.get(i);
				}

				if (newCaseDetails.isTaskCompleted()) {
					List<TeamDetails> oldTeamDetail = new ArrayList();
					List<TeamDetails> newTeamDetail = new ArrayList();
					if (oldCaseDetails != null && oldCaseDetails.getTeamList() != null) {
						oldTeamDetail = oldCaseDetails.getTeamList();
					}

					if (newCaseDetails != null && newCaseDetails.getTeamList() != null) {
						newTeamDetail = newCaseDetails.getTeamList();
					}

					List<CaseHistory> caseHistoryList = this.setCaseHistoryForOffice((List) oldTeamDetail,
							(List) newTeamDetail, newCaseDetails.getCrn(), "", "");
					int cnt = 0;
					Iterator iterator = caseHistoryList.iterator();

					while (iterator.hasNext()) {
						++cnt;
						CaseHistory caseHistory = (CaseHistory) iterator.next();
						String[] pinfo = newCaseDetails.getWorkitemName().split("#");
						String pid = "0";
						String pt = "";
						if (pinfo.length == 2) {
							pt = pinfo[0];
							pid = pinfo[1].split("::")[0];
						}

						caseHistory.setCRN(newCaseDetails.getCrn());
						caseHistory.setPerformer(newCaseDetails.getCaseHistoryPerformer());
						caseHistory.setPid(pid);
						caseHistory.setProcessCycle("");
						caseHistory.setTaskName("Office Assignment Task");
						caseHistory.setTaskStatus("In Progress");
					}

					completeCaseHistoryList.addAll(completeCaseHistoryList.size(), caseHistoryList);
				}
			}

			this.logger.debug("completeCaseHistoryList size " + completeCaseHistoryList.size());
			this.caseHistoryDAO.insertCaseHistoryEvent(completeCaseHistoryList);
			this.logger.debug("Exiting setCaseHistoryForBulkOfficeAssignment");
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public void setCaseHistoryForTaskApproved(CaseHistory caseHistoryOtherParam) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForTaskApproved");

		try {
			List<CaseHistory> caseHistoryList = new ArrayList();
			caseHistoryOtherParam.setAction("Approved at " + caseHistoryOtherParam.getTaskName());
			caseHistoryOtherParam.setOldInfo("");
			caseHistoryOtherParam.setNewInfo("");
			caseHistoryList.add(caseHistoryOtherParam);
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
			this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForTaskApproved");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void setCaseHistoryForUploadFinalDocument(CaseHistory caseHistoryOtherParam) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForUploadFinalDocument");

		try {
			List<CaseHistory> caseHistoryList = new ArrayList();
			caseHistoryList.add(caseHistoryOtherParam);
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
			this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForUploadFinalDocument");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public long getTimeStampOfCCS(String pid) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::getTimeStampOfCCS");
		long timeStamp = 0L;

		try {
			timeStamp = this.caseHistoryDAO.getTimeStampOfCCS(pid);
			this.logger.debug("Exiting CaseHistoryEventManager ::getTimeStampOfCCS");
			return timeStamp;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void setCaseHistoryForBulkTeamAssignment(List<CaseDetails> oldCaseDetailsList,
			List<CaseDetails> newCaseDetailsList) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForBulkTeamAssignment");

		try {
			List<CaseHistory> completeCaseHistoryList = new ArrayList();
			this.logger.debug("oldCaseDetailsList size " + oldCaseDetailsList.size());
			this.logger.debug("newCaseDetailsList size " + newCaseDetailsList.size());

			for (int i = 0; i < oldCaseDetailsList.size(); ++i) {
				CaseDetails oldCaseDetails = new CaseDetails();
				CaseDetails newCaseDetails = new CaseDetails();
				new ArrayList();
				if (oldCaseDetailsList.get(i) != null) {
					oldCaseDetails = (CaseDetails) oldCaseDetailsList.get(i);
				}

				if (newCaseDetailsList.get(i) != null) {
					newCaseDetails = (CaseDetails) newCaseDetailsList.get(i);
				}

				if (newCaseDetails.isTaskCompleted()) {
					List<TeamDetails> oldTeamDetail = new ArrayList();
					List<TeamDetails> newTeamDetail = new ArrayList();
					if (oldCaseDetails != null && oldCaseDetails.getTeamList() != null) {
						oldTeamDetail = oldCaseDetails.getTeamList();
					}

					if (newCaseDetails != null && newCaseDetails.getTeamList() != null) {
						newTeamDetail = newCaseDetails.getTeamList();
					}

					for (int j = 0; j < ((List) oldTeamDetail).size(); ++j) {
						TeamDetails oldTeamObj = new TeamDetails();
						TeamDetails newTeamObj = new TeamDetails();
						if (((List) oldTeamDetail).get(j) != null) {
							oldTeamObj = (TeamDetails) ((List) oldTeamDetail).get(j);
						}

						if (((List) newTeamDetail).get(j) != null) {
							newTeamObj = (TeamDetails) ((List) newTeamDetail).get(j);
						}

						List<CaseHistory> caseHistoryList = this.setCaseHistoryForTeam(oldTeamObj, newTeamObj);
						int cnt = 0;
						Iterator iterator = caseHistoryList.iterator();

						while (iterator.hasNext()) {
							++cnt;
							CaseHistory caseHistory = (CaseHistory) iterator.next();
							caseHistory.setCRN(oldCaseDetails.getCrn());
							String[] pinfo = oldCaseDetails.getWorkitemName().split("#");
							String pid = "0";
							String pt = "";
							if (pinfo.length == 2) {
								pt = pinfo[0];
								pid = pinfo[1].split("::")[0];
							}

							caseHistory.setPerformer(newCaseDetails.getCaseHistoryPerformer());
							caseHistory.setPid(pid);
							caseHistory.setProcessCycle(oldTeamObj.getProcessCycle());
							caseHistory.setTaskName("Team Assignment");
							caseHistory.setTaskStatus("In Progress");
						}

						completeCaseHistoryList.addAll(completeCaseHistoryList.size(), caseHistoryList);
					}
				}
			}

			this.logger.debug("completeCaseHistoryList size " + completeCaseHistoryList.size());
			this.caseHistoryDAO.insertCaseHistoryEvent(completeCaseHistoryList);
			this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForBulkTeamAssignment");
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	public void setCaseHistoryForCaseCreated(CaseHistory caseHistoryOtherParam) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForCaseCreated");

		try {
			List<CaseHistory> caseHistoryList = new ArrayList();
			caseHistoryOtherParam.setAction("Case Created");
			caseHistoryOtherParam.setOldInfo("");
			caseHistoryOtherParam.setNewInfo("");
			caseHistoryList.add(caseHistoryOtherParam);
			this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
			this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForCaseCreated");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void setCaseHistoryForTeamManagerReassign(TeamDetails oldTeamDetailObj, TeamDetails newTeamDetailObj,
			CaseHistory caseHistoryOtherParm) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForTeamManagerReassign");

		try {
			List<CaseHistory> caseHistoryList = new ArrayList();
			String oldManagerName = "";
			String newManagerName = "";
			if (oldTeamDetailObj.getManagerFullName() != null) {
				oldManagerName = oldTeamDetailObj.getManagerFullName();
			}

			if (newTeamDetailObj.getManagerFullName() != null) {
				newManagerName = newTeamDetailObj.getManagerFullName();
			}

			this.logger.debug("oldManagerName " + oldManagerName + " newManagerName " + newManagerName);
			if (!oldManagerName.equalsIgnoreCase(newManagerName)) {
				if (!oldManagerName.isEmpty() && oldManagerName != null) {
					if (!newManagerName.isEmpty() && newManagerName != null) {
						caseHistoryOtherParm.setAction("Manager Reassigned");
						caseHistoryOtherParm.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName()
								+ " and Manager Name:- " + oldManagerName);
						caseHistoryOtherParm.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName()
								+ " and Manager Name:- " + newManagerName);
						caseHistoryList.add(caseHistoryOtherParm);
						this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
					}
				} else {
					caseHistoryOtherParm.setAction("Manager (Level 2) Assigned");
					caseHistoryOtherParm.setNewInfo(
							"Office:- " + newTeamDetailObj.getOfficeName() + " and Manager Name:- " + newManagerName);
					caseHistoryOtherParm.setOldInfo("");
					caseHistoryList.add(caseHistoryOtherParm);
					this.caseHistoryDAO.insertCaseHistoryEvent(caseHistoryList);
				}
			}

			this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForTeamManagerReassign");
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private List<CaseHistory> setCaseHistoryForTeamManagerReassign(TeamDetails oldTeamDetailObj,
			TeamDetails newTeamDetailObj) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager ::setCaseHistoryForTeamManagerReassignV2");
		ArrayList caseHistoryList = new ArrayList();

		try {
			String oldManagerName = "";
			String newManagerName = "";
			if (oldTeamDetailObj.getManagerFullName() != null) {
				oldManagerName = oldTeamDetailObj.getManagerFullName();
			}

			if (newTeamDetailObj.getManagerFullName() != null) {
				newManagerName = newTeamDetailObj.getManagerFullName();
			}

			if (!oldManagerName.equalsIgnoreCase(newManagerName)) {
				CaseHistory caseHistory = new CaseHistory();
				if (!oldManagerName.isEmpty() && oldManagerName != null) {
					if (!newManagerName.isEmpty() && newManagerName != null) {
						caseHistory.setAction("Manager Reassigned");
						caseHistory.setOldInfo("Office:- " + oldTeamDetailObj.getOfficeName() + " and Manager Name:- "
								+ oldManagerName);
						caseHistory.setNewInfo("Office:- " + newTeamDetailObj.getOfficeName() + " and Manager Name:- "
								+ newManagerName);
						caseHistoryList.add(caseHistory);
					}
				} else {
					caseHistory.setAction("Manager (Level 2) Assigned");
					caseHistory.setNewInfo(
							"Office:- " + newTeamDetailObj.getOfficeName() + " and Manager Name:- " + newManagerName);
					caseHistory.setOldInfo("");
					caseHistoryList.add(caseHistory);
				}
			}

			this.logger.debug("Exiting CaseHistoryEventManager ::setCaseHistoryForTeamManagerReassignV2");
			return caseHistoryList;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void setCaseHistoryForBulkTeamManagerReassign(List<CaseDetails> oldCaseDetailsList,
			List<CaseDetails> newCaseDetailsList) throws CMSException {
		this.logger.debug("in CaseHistoryEventManager::setCaseHistoryForBulkTeamManagerReassign");

		try {
			List<CaseHistory> completeCaseHistoryList = new ArrayList();
			this.logger.debug("oldCaseDetailsList size " + oldCaseDetailsList.size());
			this.logger.debug("newCaseDetailsList size " + newCaseDetailsList.size());

			for (int i = 0; i < oldCaseDetailsList.size(); ++i) {
				CaseDetails oldCaseDetails = new CaseDetails();
				CaseDetails newCaseDetails = new CaseDetails();
				new ArrayList();
				if (oldCaseDetailsList.get(i) != null) {
					oldCaseDetails = (CaseDetails) oldCaseDetailsList.get(i);
				}

				if (newCaseDetailsList.get(i) != null) {
					newCaseDetails = (CaseDetails) newCaseDetailsList.get(i);
				}

				List<TeamDetails> oldTeamDetail = new ArrayList();
				List<TeamDetails> newTeamDetail = new ArrayList();
				if (oldCaseDetails != null && oldCaseDetails.getTeamList() != null) {
					oldTeamDetail = oldCaseDetails.getTeamList();
				}

				if (newCaseDetails != null && newCaseDetails.getTeamList() != null) {
					newTeamDetail = newCaseDetails.getTeamList();
				}

				for (int j = 0; j < ((List) oldTeamDetail).size(); ++j) {
					TeamDetails oldTeamObj = new TeamDetails();
					TeamDetails newTeamObj = new TeamDetails();
					if (((List) oldTeamDetail).get(j) != null) {
						oldTeamObj = (TeamDetails) ((List) oldTeamDetail).get(j);
					}

					if (((List) newTeamDetail).get(j) != null) {
						newTeamObj = (TeamDetails) ((List) newTeamDetail).get(j);
					}

					List<CaseHistory> caseHistoryList = this.setCaseHistoryForTeamManagerReassign(oldTeamObj,
							newTeamObj);
					int cnt = 0;
					Iterator iterator = caseHistoryList.iterator();

					while (iterator.hasNext()) {
						++cnt;
						CaseHistory caseHistory = (CaseHistory) iterator.next();
						caseHistory.setCRN(oldCaseDetails.getCrn());
						String[] pinfo = oldCaseDetails.getWorkitemName().split("#");
						String pid = "0";
						String pt = "";
						if (pinfo.length == 2) {
							pt = pinfo[0];
							pid = pinfo[1].split("::")[0];
						}

						caseHistory.setPerformer(newCaseDetails.getCaseHistoryPerformer());
						caseHistory.setPid(pid);
						caseHistory.setProcessCycle(oldTeamObj.getProcessCycle());
						caseHistory.setTaskName("Team Assignment");
						caseHistory.setTaskStatus("In Progress");
					}

					completeCaseHistoryList.addAll(completeCaseHistoryList.size(), caseHistoryList);
				}
			}

			this.logger.debug("completeCaseHistoryList size " + completeCaseHistoryList.size());
			this.caseHistoryDAO.insertCaseHistoryEvent(completeCaseHistoryList);
			this.logger.debug("Exiting CaseHistoryEventManager::setCaseHistoryForBulkTeamManagerReassign");
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}
}