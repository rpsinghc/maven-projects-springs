package com.worldcheck.atlas.bl.rejection;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.IRejectionREManager;
import com.worldcheck.atlas.dao.rejection.RejectionREDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class RejectionREManager implements IRejectionREManager {
	private static final String ALL = "all";
	private static final String HASH = "#";
	private static final String PRIMARY_HASH = "Primary#";
	private static final String TEAM_ID = "teamId";
	private static final String SUBJECT_NAME = "subjectName";
	private static final String PERFORMED_BY = "performedBy";
	private static final String LOGIN_LEVEL = "loginLevel";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.rejection.RejectionREManager");
	private RejectionREDAO rejectionREDAO;
	private Set<String> PTanalyst = null;
	private Set<String> PTL2Manager_Supervisior = null;
	private Set<String> BIanalyst = null;
	private Set<String> BIL2Manager_Supervisior = null;
	private int STTeamNo = 1;
	private Set<String> STanalyst = null;
	private Set<String> STL2Manager_Supervisior = null;
	private Map<Object, Object> STMap = null;
	private int VTTeamNo = 1;
	private Set<String> VTanalyst = null;
	private Set<String> VTL2Manager_Supervisior = null;
	private Map<Object, Object> VTMap = null;
	private Set<String> officeList = null;

	public void setRejectionREDAO(RejectionREDAO rejectionREDAO) {
		this.rejectionREDAO = rejectionREDAO;
	}

	public List<SubTeamReMapVO> getAllSubjectInfoByTeam(String crn, String teamId, String teamName, String processCycle)
			throws CMSException {
		return this.rejectionREDAO.getAllSubjectInfoByTeam(crn, teamId, teamName, processCycle);
	}

	public int saveRejectedREInfo(HttpServletRequest request, Session session, String workItemName, String processCycle,
			String teamTypeList, List<SubTeamReMapVO> teamDetailsVOList, String rejectReason, String updateBy,
			String rejectedTaskStatus, String rejectedFrom) throws CMSException {
		long teamId = 0L;
		boolean var13 = false;

		try {
			this.logger.debug("request.getSession().getAttribute(loginLevel) is "
					+ request.getSession().getAttribute("loginLevel"));
			String userName;
			if (null != request.getSession().getAttribute("loginLevel")) {
				userName = (String) request.getSession().getAttribute("performedBy");
			} else {
				userName = updateBy;
			}

			int totalRejectedRE = this.rejectionREDAO.saveRejectedREInfo(teamDetailsVOList);
			this.logger.debug("Total Rejected RE::" + totalRejectedRE);
			List<ReviewHistory> reviewHistoryItemsList = new ArrayList();
			HashMap<String, String> subjectREInfoMap = new HashMap();
			Iterator i$ = teamDetailsVOList.iterator();

			while (true) {
				SubTeamReMapVO subTeamReMapVO;
				while (i$.hasNext()) {
					subTeamReMapVO = (SubTeamReMapVO) i$.next();
					this.logger.debug("CRN:" + subTeamReMapVO.getCrn() + "\tSubjectId:" + subTeamReMapVO.getSubjectId()
							+ "\tSubject:" + subTeamReMapVO.getSubject() + "\tREID:" + subTeamReMapVO.getReId()
							+ "\tREName:" + subTeamReMapVO.getReName() + "\tTeamID:" + subTeamReMapVO.getTeamId()
							+ "\tPerformer:" + subTeamReMapVO.getPerformer());
					subjectREInfoMap.put("crn", subTeamReMapVO.getCrn());
					subjectREInfoMap.put("subjectName", subTeamReMapVO.getSubject());
					subjectREInfoMap.put("teamId", subTeamReMapVO.getTeamId() + "");
					if (subjectREInfoMap.get(subTeamReMapVO.getSubject()) != null
							&& !"".equals(subjectREInfoMap.get(subTeamReMapVO.getSubject()))) {
						String reName = (String) subjectREInfoMap.get(subTeamReMapVO.getSubject());
						subjectREInfoMap.put(subTeamReMapVO.getSubject(), reName + "," + subTeamReMapVO.getReName());
					} else {
						subjectREInfoMap.put(subTeamReMapVO.getSubject(), subTeamReMapVO.getReName());
					}
				}

				Set<String> teamSet = this.LoadData(teamDetailsVOList);
				if (!teamSet.contains("Primary")) {
					this.logger.debug("Primary Team information should be load from database.");
					new SubTeamReMapVO();
					subTeamReMapVO = this.rejectionREDAO.getPrimaryTemaInfo((String) subjectREInfoMap.get("crn"));
					if (subTeamReMapVO != null) {
						this.loadPrimaryTeamInfo(subTeamReMapVO);
					}
				}

				String officeNameList = this.rejectedOfficeList(this.officeList, "all");
				this.logger.debug("officeNameList::" + officeNameList);
				String reviewStatus = "";
				Set<String> subjectKey = subjectREInfoMap.keySet();
				Iterator i$ = subjectKey.iterator();

				while (true) {
					while (true) {
						String key;
						do {
							if (!i$.hasNext()) {
								ReviewHistory reviewHistory = new ReviewHistory();
								CaseHistory caseHistoryOtherParam = new CaseHistory();
								reviewHistory.setCRN((String) subjectREInfoMap.get("crn"));
								reviewHistory.setTaskName(workItemName.split("#")[1].split("::")[1]);
								reviewHistory.setReviewComment(rejectReason);
								reviewHistory.setReviewStatus(this.officeList + "; " + reviewStatus);
								if (null != request.getSession().getAttribute("loginLevel")) {
									reviewHistory
											.setCommentFrom((String) request.getSession().getAttribute("performedBy"));
								} else {
									reviewHistory.setCommentFrom(userName);
								}

								reviewHistory.setUpdatedBy(updateBy);
								this.logger
										.debug("RejectionREManager # saveRejectedREInfo crn:" + reviewHistory.getCRN());
								reviewHistoryItemsList.add(reviewHistory);
								this.logger.debug("************Review History**************");
								this.logger.debug("CRN::" + reviewHistory.getCRN());
								this.logger.debug("GetTaskName::" + reviewHistory.getTaskName());
								this.logger.debug("GetReviewComment::" + reviewHistory.getReviewComment());
								this.logger.debug("GetReviewStatus::" + reviewHistory.getReviewStatus());
								this.logger.debug("GetCommentFrom::" + reviewHistory.getCommentFrom());
								ResourceLocator.self().getReviewHistoryService()
										.setReviewHistory(reviewHistoryItemsList);
								this.logger.debug("Rejected Task status:" + rejectedTaskStatus);
								caseHistoryOtherParam.setCRN((String) subjectREInfoMap.get("crn"));
								caseHistoryOtherParam.setProcessCycle(processCycle);
								caseHistoryOtherParam.setPid(workItemName.split("#")[1].split("::")[0]);
								caseHistoryOtherParam.setTaskName(workItemName.split("#")[1].split("::")[1]);
								caseHistoryOtherParam.setTaskStatus(rejectedTaskStatus);
								caseHistoryOtherParam.setOldInfo("");
								caseHistoryOtherParam.setNewInfo(reviewStatus);
								if (null != request.getSession().getAttribute("loginLevel")) {
									caseHistoryOtherParam
											.setPerformer((String) request.getSession().getAttribute("performedBy"));
								} else {
									caseHistoryOtherParam.setPerformer(userName);
								}

								this.logger.debug("CRN:" + caseHistoryOtherParam.getCRN());
								this.logger.debug("processCycle:" + caseHistoryOtherParam.getProcessCycle());
								this.logger.debug("PID:" + caseHistoryOtherParam.getPid());
								this.logger.debug("Performer:" + caseHistoryOtherParam.getPerformer());
								this.logger.debug("Information:(office-subject-RE)" + reviewStatus);
								ResourceLocator.self().getCaseHistoryService()
										.setCaseHistoryForRejection(caseHistoryOtherParam);
								ResourceLocator.self().getFlowService().updateDSForRejection(session, workItemName,
										teamDetailsVOList);
								if (subjectREInfoMap.get("teamId") != null) {
									teamId = Long.parseLong((String) subjectREInfoMap.get("teamId"));
								}

								UserMasterVO userMasterVO = ResourceLocator.self().getUserService()
										.getUserInfo(updateBy);
								this.logger.debug("TeamTypeList::" + rejectedFrom);
								int notificationCase = this.getNotificationCase(teamSet, rejectedFrom);
								this.logger.debug("Task is Rejected Case :: " + notificationCase);
								this.sendNotificationMessage((String) subjectREInfoMap.get("crn"), notificationCase,
										userMasterVO.getUsername(), rejectReason);
								return totalRejectedRE;
							}

							key = (String) i$.next();
						} while (key.equals("crn"));

						if ("".equals(reviewStatus) && !key.equals("subjectName")) {
							reviewStatus = "REs rejected for :" + key + "[" + (String) subjectREInfoMap.get(key) + "]";
						} else if (!key.equals("teamId") && !key.equals("subjectName")) {
							if (reviewStatus.equals("")) {
								reviewStatus = key + "[" + (String) subjectREInfoMap.get(key) + "]";
							} else {
								reviewStatus = reviewStatus + "," + key + "[" + (String) subjectREInfoMap.get(key)
										+ "]";
							}
						}
					}
				}
			}
		} catch (NullPointerException var26) {
			throw new CMSException(this.logger, var26);
		}
	}

	private Set LoadData(List<SubTeamReMapVO> subTeamReMapList) {
		this.PTanalyst = new HashSet();
		this.PTL2Manager_Supervisior = new HashSet();
		this.BIanalyst = new HashSet();
		this.BIL2Manager_Supervisior = new HashSet();
		this.STTeamNo = 1;
		this.STanalyst = new HashSet();
		this.STL2Manager_Supervisior = new HashSet();
		this.STMap = null;
		this.VTTeamNo = 1;
		this.VTanalyst = new HashSet();
		this.VTL2Manager_Supervisior = new HashSet();
		this.VTMap = null;
		this.logger.debug("PTanalyst HasHSet::" + this.PTanalyst);
		this.logger.debug("PTL2Manager_Supervisior HasHSet::" + this.PTL2Manager_Supervisior);
		this.logger.debug("PTanalyst HasHSet::" + this.PTanalyst);
		this.logger.debug("BIL2Manager_Supervisior HasHSet::" + this.BIL2Manager_Supervisior);
		this.logger.debug("STanalyst HasHSet::" + this.STanalyst);
		this.logger.debug("PTL2Manager_Supervisior HasHSet::" + this.STL2Manager_Supervisior);
		this.logger.debug("VTanalyst HasHSet::" + this.VTanalyst);
		this.logger.debug("VTL2Manager_Supervisior HasHSet::" + this.VTL2Manager_Supervisior);
		Set<String> teamSet = new HashSet();
		this.officeList = new HashSet();
		if (null != subTeamReMapList && subTeamReMapList.size() != 0) {
			Iterator i$ = subTeamReMapList.iterator();

			while (true) {
				while (i$.hasNext()) {
					SubTeamReMapVO subTeamReMapVO = (SubTeamReMapVO) i$.next();
					if (subTeamReMapVO.getTeamName().equalsIgnoreCase("Primary")) {
						teamSet.add("Primary");
						this.PTanalyst.add(subTeamReMapVO.getPerformer() + "#" + subTeamReMapVO.getPerformerFullName());
						if (subTeamReMapVO.getMainAnalyst().equalsIgnoreCase(subTeamReMapVO.getReportsTo())) {
							this.PTL2Manager_Supervisior
									.add(subTeamReMapVO.getReportsTo() + "#" + subTeamReMapVO.getReportsToFullName());
						} else {
							this.PTL2Manager_Supervisior
									.add(subTeamReMapVO.getMainAnalyst() + "#" + subTeamReMapVO.getAnalystFullName());
							this.PTL2Manager_Supervisior
									.add(subTeamReMapVO.getReportsTo() + "#" + subTeamReMapVO.getReportsToFullName());
						}

						this.logger.debug("Add into office ::" + subTeamReMapVO.getOffice());
						this.officeList.add("Primary[" + subTeamReMapVO.getOffice() + "]");
					} else if (subTeamReMapVO.getTeamName().equalsIgnoreCase("Supporting - BI")) {
						teamSet.add("Supporting - BI");
						this.BIanalyst.add(subTeamReMapVO.getPerformer() + "#" + subTeamReMapVO.getPerformerFullName());
						this.BIL2Manager_Supervisior
								.add(subTeamReMapVO.getReportsTo() + "#" + subTeamReMapVO.getReportsToFullName());
						this.logger.debug("Add into office ::" + subTeamReMapVO.getOffice());
						this.officeList.add("Supporting - BI");
					} else {
						boolean flag;
						Set key;
						SubTeamReMapVO temp;
						Iterator i$;
						Object object;
						if (subTeamReMapVO.getTeamName().equalsIgnoreCase("Supporting - Internal")) {
							teamSet.add("Supporting - Internal");
							flag = false;
							if (this.STMap == null) {
								this.STMap = new HashMap();
								this.STMap.put("ST-Team-" + this.STTeamNo, subTeamReMapVO);
								this.logger.debug("Add into office ::" + subTeamReMapVO.getOffice());
								this.officeList.add("Supporting - Internal-" + this.STTeamNo + "["
										+ subTeamReMapVO.getOffice() + "]");
								++this.STTeamNo;
							} else {
								key = this.STMap.keySet();
								i$ = key.iterator();

								while (i$.hasNext()) {
									object = i$.next();
									temp = (SubTeamReMapVO) this.STMap.get(object);
									if (subTeamReMapVO.getPerformer().equalsIgnoreCase(temp.getPerformer())) {
										flag = true;
										break;
									}
								}

								if (!flag) {
									this.STMap.put("ST-Team-" + this.STTeamNo, subTeamReMapVO);
									this.logger.debug("Add into office ::" + subTeamReMapVO.getOffice());
									this.officeList.add("Supporting - Internal-" + this.STTeamNo + "["
											+ subTeamReMapVO.getOffice() + "]");
									++this.STTeamNo;
								}
							}
						} else if (subTeamReMapVO.getTeamName().equalsIgnoreCase("Supporting - Vendor")) {
							teamSet.add("Supporting - Vendor");
							flag = false;
							if (this.VTMap == null) {
								this.VTMap = new HashMap();
								this.VTMap.put("VT-Team-" + this.STTeamNo, subTeamReMapVO);
								this.logger.debug("Add into office ::" + subTeamReMapVO.getOffice());
								this.officeList.add("Supporting - Vendor-" + this.VTTeamNo);
								++this.VTTeamNo;
							} else {
								key = this.VTMap.keySet();
								i$ = key.iterator();

								while (i$.hasNext()) {
									object = i$.next();
									temp = (SubTeamReMapVO) this.VTMap.get(object);
									if (subTeamReMapVO.getPerformer().equalsIgnoreCase(temp.getPerformer())) {
										flag = true;
										break;
									}
								}

								if (!flag) {
									this.VTMap.put("VT-Team-" + this.VTTeamNo, subTeamReMapVO);
									this.logger.debug("Add into office ::" + subTeamReMapVO.getOffice());
									this.officeList.add("Supporting - Vendor-" + this.VTTeamNo);
									++this.VTTeamNo;
								}
							}
						}
					}
				}

				return teamSet;
			}
		} else {
			this.logger.debug("subTeamReMapList could not be blank.");
			return teamSet;
		}
	}

	private int getNotificationCase(Set<String> teamSet, String rejectedFrom) {
		this.logger.debug("TeamSet::" + teamSet);
		this.logger.debug("RejectedFrom::" + rejectedFrom);
		int notificationCase = 0;
		this.logger.debug("Inside the GetNotification: TeamSet ::" + teamSet);
		this.logger.debug("Primary::" + teamSet.contains("Primary"));
		this.logger.debug("Supporting Internal::" + teamSet.contains("Supporting - Internal"));
		this.logger.debug("Supporting BI::" + teamSet.contains("Supporting - BI"));
		this.logger.debug("Supporting Vendor::" + teamSet.contains("Supporting - Vendor"));
		if (rejectedFrom.split("#")[1].equalsIgnoreCase("Primary")) {
			if (teamSet.contains("Primary") && teamSet.contains("Supporting - Internal")
					&& teamSet.contains("Supporting - BI") && teamSet.contains("Supporting - Vendor")) {
				this.logger.debug("Case: 8 from Table 2::");
				notificationCase = 8;
			} else if (teamSet.contains("Primary") && teamSet.contains("Supporting - Internal")
					&& teamSet.contains("Supporting - BI")) {
				this.logger.debug("Case: 6 from Table 2::");
				notificationCase = 6;
			} else if (teamSet.contains("Primary") && teamSet.contains("Supporting - Internal")) {
				this.logger.debug("Case: 5 from Table 2::");
				notificationCase = 5;
			} else if (teamSet.contains("Primary")) {
				this.logger.debug("Case: 1 from Table 2::");
				notificationCase = 1;
			} else if (teamSet.contains("Supporting - Internal") && teamSet.contains("Supporting - BI")) {
				this.logger.debug("Case: 9 from Table 2::");
				notificationCase = 9;
			} else if (teamSet.contains("Supporting - Internal") && teamSet.contains("Supporting - Vendor")) {
				this.logger.debug("Case: 10 from Table 2::");
				notificationCase = 10;
			} else if (teamSet.contains("Supporting - BI") && teamSet.contains("Supporting - Vendor")) {
				this.logger.debug("Case: 11 from Table 2::");
				notificationCase = 11;
			} else if (teamSet.contains("Supporting - Internal")) {
				this.logger.debug("Case: 2 from Table 2::");
				notificationCase = 2;
			} else if (teamSet.contains("Supporting - BI")) {
				this.logger.debug("Case: 3 from Table 2::");
				notificationCase = 3;
			} else if (teamSet.contains("Supporting - Vendor")) {
				this.logger.debug("Case: 4 from Table 2::");
				notificationCase = 4;
			}
		} else {
			notificationCase = 7;
		}

		return notificationCase;
	}

	public String getPTAnalystName() {
		if (this.PTanalyst != null) {
			Iterator i$ = this.PTanalyst.iterator();
			if (i$.hasNext()) {
				String s = (String) i$.next();
				return s;
			}
		}

		return null;
	}

	public Set<String> getPTL2Manager_Supervisior() {
		Set<String> manager_supervisior = null;
		if (this.PTL2Manager_Supervisior != null) {
			manager_supervisior = new HashSet();
			Iterator i$ = this.PTL2Manager_Supervisior.iterator();

			while (i$.hasNext()) {
				String str = (String) i$.next();
				manager_supervisior.add(str);
			}
		}

		return manager_supervisior;
	}

	public void loadPrimaryTeamInfo(SubTeamReMapVO subTeamReMapVO) {
		this.PTanalyst.add(subTeamReMapVO.getPerformer() + "#" + subTeamReMapVO.getPerformerFullName());
		if (subTeamReMapVO.getMainAnalyst().equalsIgnoreCase(subTeamReMapVO.getReportsTo())) {
			this.PTL2Manager_Supervisior
					.add(subTeamReMapVO.getReportsTo() + "#" + subTeamReMapVO.getReportsToFullName());
		} else {
			this.PTL2Manager_Supervisior
					.add(subTeamReMapVO.getMainAnalyst() + "#" + subTeamReMapVO.getAnalystFullName());
			this.PTL2Manager_Supervisior
					.add(subTeamReMapVO.getReportsTo() + "#" + subTeamReMapVO.getReportsToFullName());
		}

	}

	public Set<String> getSTAnalystNameList() {
		Set<String> stAnalystSet = new HashSet();
		int i = 1;
		if (this.STMap != null) {
			Set stTeamSet = this.STMap.keySet();

			for (Iterator i$ = stTeamSet.iterator(); i$.hasNext(); ++i) {
				Object object = i$.next();
				SubTeamReMapVO temp = (SubTeamReMapVO) this.STMap.get(object);
				stAnalystSet.add("ST-" + i + "::" + temp.getPerformer() + "#" + temp.getPerformerFullName());
			}
		} else {
			this.logger.debug("Supporting Team Map is Null");
		}

		return stAnalystSet;
	}

	public Set<String> getSTL2Manager_Supervisior() {
		Set<String> stAnalystSet = new HashSet();
		int i = 1;
		Set stTeamSet = this.STMap.keySet();

		for (Iterator i$ = stTeamSet.iterator(); i$.hasNext(); ++i) {
			Object object = i$.next();
			SubTeamReMapVO temp = (SubTeamReMapVO) this.STMap.get(object);
			stAnalystSet.add("ST-" + i + "::" + temp.getMainAnalyst() + "#" + temp.getAnalystFullName());
			stAnalystSet.add("ST-" + i + "::" + temp.getReportsTo() + "#" + temp.getReportsToFullName());
		}

		return stAnalystSet;
	}

	public String getBIAnalystName() {
		if (this.BIanalyst != null) {
			Iterator i$ = this.BIanalyst.iterator();
			if (i$.hasNext()) {
				String s = (String) i$.next();
				return s;
			}
		}

		return null;
	}

	public Set<String> getBIL2Manager_Supervisior() {
		Set<String> manager_supervisior = null;
		if (this.BIL2Manager_Supervisior != null) {
			manager_supervisior = new HashSet();
			Iterator i$ = this.BIL2Manager_Supervisior.iterator();

			while (i$.hasNext()) {
				String str = (String) i$.next();
				manager_supervisior.add(str);
			}
		}

		return manager_supervisior;
	}

	public Set<String> getVendorAnalystNameList() {
		Set<String> vendorAnalystSet = new HashSet();
		int i = 1;
		Set stTeamSet = this.VTMap.keySet();

		for (Iterator i$ = stTeamSet.iterator(); i$.hasNext(); ++i) {
			Object object = i$.next();
			SubTeamReMapVO temp = (SubTeamReMapVO) this.VTMap.get(object);
			vendorAnalystSet.add("VT-" + i + "::" + temp.getPerformer() + "#" + temp.getPerformerFullName());
		}

		return vendorAnalystSet;
	}

	public Set<String> getVendorL2Manager_Supervisior() {
		Set<String> vendorAnalystSet = new HashSet();
		int i = 1;
		Set stTeamSet = this.VTMap.keySet();

		for (Iterator i$ = stTeamSet.iterator(); i$.hasNext(); ++i) {
			Object object = i$.next();
			SubTeamReMapVO temp = (SubTeamReMapVO) this.VTMap.get(object);
			vendorAnalystSet.add("VT-" + i + "::" + temp.getReportsTo() + "#" + temp.getReportsToFullName());
		}

		return vendorAnalystSet;
	}

	public List<String> stringToList(String str, String type) {
		List<String> temp = new ArrayList();
		String[] tempArr = null;
		tempArr = str.split("#");
		if (type.equalsIgnoreCase("ID")) {
			temp.add(tempArr[0]);
		} else {
			temp.add(tempArr[1]);
		}

		return temp;
	}

	public List<String> setTOList(Set<String> set, String type) {
		List<String> temp = new ArrayList();
		Iterator itr = set.iterator();

		while (itr.hasNext()) {
			if (type.equalsIgnoreCase("ID")) {
				temp.add(((String) itr.next()).split("#")[0]);
			} else if (type.equalsIgnoreCase("fullName")) {
				temp.add(((String) itr.next()).split("#")[1]);
			} else {
				temp.add(itr.next());
			}
		}

		return temp;
	}

	public List<String> mergeToList(List<String> src, List<String> dest) {
		Set<String> tempSet = new HashSet();
		Iterator i$ = src.iterator();

		String data;
		while (i$.hasNext()) {
			data = (String) i$.next();
			tempSet.add(data);
		}

		i$ = dest.iterator();

		while (i$.hasNext()) {
			data = (String) i$.next();
			tempSet.add(data);
		}

		return this.setTOList(tempSet, "");
	}

	public void sendNotificationMessage(String crn, int notificationCase, String updateBy, String rejectReason)
			throws CMSException {
		List<String> senderList = null;
		this.logger.debug("crn::" + crn);
		this.logger.debug("notificationCase::" + notificationCase);
		this.logger.debug("updateBy::" + updateBy);
		this.logger.debug("rejectReason::" + rejectReason);
		String message;
		String[] tempStrarr;
		int id;
		List vendor;
		Iterator i$;
		String string;
		Iterator i$;
		String str2;
		ArrayList senderList;
		Object senderList;
		switch (notificationCase) {
			case 1 :
				tempStrarr = null;
				this.logger.debug("######### PRIMARY TEAM ############ \nPT Analyst");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				this.logger.debug("PT Analyst:" + senderList);
				message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				new ArrayList();
				senderList = this.setTOList(this.getPTL2Manager_Supervisior(), "ID");
				this.logger.debug("PT manager&supervisior:" + senderList);
				message = "This report has been rejected by <updateBy>. The analyst for this report is <PTanalystname>. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<PTanalystname>", this.getPTAnalystName().split("#")[1]);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("\n * * *");
				break;
			case 2 :
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				senderList = this.mergeToList(senderList, this.setTOList(this.getPTL2Manager_Supervisior(), "ID"));
				this.logger.debug("PT ANALYST , L2Manager & Supervisior ::" + senderList);
				message = "This report has been rejected by <updateBy>. The Research Elements that were rejected belong to <ST name> Team. You'll need to reconsolidate this report later. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				Iterator i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							return;
						}

						String string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					Iterator i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						String str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 3 :
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				senderList = this.mergeToList(senderList, this.setTOList(this.getPTL2Manager_Supervisior(), "ID"));
				this.logger.debug("PT ANALYST , L2Manager & Supervisior ::" + senderList);
				message = "This report has been rejected by <updateBy>. The Research Elements that were rejected belong to BI Team. You'll need to reconsolidate this report later. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("####################### BI TEAM ###############\n   BI Analyst");
				new ArrayList();
				senderList = this.stringToList(this.getBIAnalystName(), "ID");
				this.logger.debug("Supporting - BI Analyst:" + senderList);
				message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				new ArrayList();
				senderList = this.setTOList(this.getBIL2Manager_Supervisior(), "ID");
				this.logger.debug("Supporting - BI supervisior:" + senderList);
				message = "This report has been rejected by <updateBy>. The analyst for this report is <BIanalystname>. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<BIanalystname>", this.getBIAnalystName().split("#")[1]);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				this.logger.debug("\n* * *");
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				break;
			case 4 :
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				senderList = this.mergeToList(senderList, this.setTOList(this.getPTL2Manager_Supervisior(), "ID"));
				this.logger.debug("PT ANALYST , L2Manager & Supervisior ::" + senderList);
				message = "This report has been rejected by <updateBy>. The Research Elements that were rejected belong to Vendor Team. You'll need to reconsolidate this report later. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  Vendor TEAM ##############" + this.getVendorAnalystNameList());
				vendor = this.setTOList(this.getVendorAnalystNameList(), "ID");
				id = vendor.size();
				i$ = this.setTOList(this.getVendorAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							return;
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("VT-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("VT's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							senderList, crn);
					senderList = new ArrayList();
					i$ = this.setTOList(this.getVendorL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase("VT-" + id)) {
							senderList.add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of Vendor's Team :: " + senderList);
					message = "This report has been rejected by <updateBy>. The Vendor Manager for this report is <VTanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<VTanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * *");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							senderList, crn);
					--id;
				}
			case 5 :
				tempStrarr = null;
				this.logger.debug("######### PRIMARY TEAM ############ \nPT Analyst");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				this.logger.debug("PT Analyst:" + senderList);
				message = "Rejected : Your report has been rejected by <updateBy>. Please rework. The Research Elements for <ST name> Team were also rejected. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				new ArrayList();
				senderList = this.setTOList(this.getPTL2Manager_Supervisior(), "ID");
				this.logger.debug("PT manager&supervisior:" + senderList);
				message = "This report has been rejected by <updateBy>. The analyst for this report is <PTanalystname>. The Research Elements for <ST name> Team were also rejected. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<PTanalystname>", this.getPTAnalystName().split("#")[1]);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							this.logger.debug("\n * * *");
							return;
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 6 :
				tempStrarr = null;
				this.logger.debug("######### PRIMARY TEAM ############ \nPT Analyst");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				this.logger.debug("PT Analyst:" + senderList);
				message = "Rejected : Your report has been rejected by <updateBy>. Please rework. The Research Elements for <ST name> Team and BI Team were also rejected. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				new ArrayList();
				senderList = this.setTOList(this.getPTL2Manager_Supervisior(), "ID");
				this.logger.debug("PT manager&supervisior:" + senderList);
				message = "This report has been rejected by <updateBy>. The analyst for this report is <PTanalystname>. The Research Elements for <ST name> Team and BI Team were also rejected. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<PTanalystname>", this.getPTAnalystName().split("#")[1]);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							this.logger.debug("####################### BI TEAM ###############\n   BI Analyst");
							new ArrayList();
							senderList = this.stringToList(this.getBIAnalystName(), "ID");
							this.logger.debug("Supporting - BI Analyst:" + senderList);
							message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
							message = message.replace("<updateBy>", updateBy);
							message = message.replace("<rejectionReason>", rejectReason);
							this.logger.debug(message);
							ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
									senderList, crn);
							new ArrayList();
							senderList = this.setTOList(this.getBIL2Manager_Supervisior(), "ID");
							this.logger.debug("Supporting - BI supervisior:" + senderList);
							message = "This report has been rejected by <updateBy>. The analyst for this report is <BIanalystname>. Rejection comment : <rejectionReason>.";
							message = message.replace("<updateBy>", updateBy);
							message = message.replace("<BIanalystname>", this.getBIAnalystName().split("#")[1]);
							message = message.replace("<rejectionReason>", rejectReason);
							this.logger.debug(message);
							this.logger.debug("\n* * *");
							ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
									senderList, crn);
							this.logger.debug("\n * * *");
							return;
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 7 :
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							return;
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 8 :
				tempStrarr = null;
				this.logger.debug("######### PRIMARY TEAM ############ \nPT Analyst");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				this.logger.debug("PT Analyst:" + senderList);
				message = "Rejected : Your report has been rejected by <updateBy>. Please rework. The Research Elements for <ST name> Team and BI Team were also rejected. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				new ArrayList();
				senderList = this.setTOList(this.getPTL2Manager_Supervisior(), "ID");
				this.logger.debug("PT manager&supervisior:" + senderList);
				message = "This report has been rejected by <updateBy>. The analyst for this report is <PTanalystname>. The Research Elements for <ST name> Team and BI Team were also rejected. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<PTanalystname>", this.getPTAnalystName().split("#")[1]);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							this.logger.debug("####################### BI TEAM ###############\n   BI Analyst");
							new ArrayList();
							senderList = this.stringToList(this.getBIAnalystName(), "ID");
							this.logger.debug("Supporting - BI Analyst:" + senderList);
							message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
							message = message.replace("<updateBy>", updateBy);
							message = message.replace("<rejectionReason>", rejectReason);
							this.logger.debug(message);
							ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
									senderList, crn);
							new ArrayList();
							senderList = this.setTOList(this.getBIL2Manager_Supervisior(), "ID");
							this.logger.debug("Supporting - BI supervisior:" + senderList);
							message = "This report has been rejected by <updateBy>. The analyst for this report is <BIanalystname>. Rejection comment : <rejectionReason>.";
							message = message.replace("<updateBy>", updateBy);
							message = message.replace("<BIanalystname>", this.getBIAnalystName().split("#")[1]);
							message = message.replace("<rejectionReason>", rejectReason);
							this.logger.debug(message);
							this.logger.debug("\n* * *");
							ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
									senderList, crn);
							this.logger.debug(
									"###############  Vendor TEAM ##############" + this.getVendorAnalystNameList());
							vendor = this.setTOList(this.getVendorAnalystNameList(), "ID");
							id = vendor.size();
							i$ = this.setTOList(this.getVendorAnalystNameList(), "all").iterator();

							while (true) {
								do {
									if (!i$.hasNext()) {
										this.logger.debug("\n * * *");
										return;
									}

									string = (String) i$.next();
									tempStrarr = string.split("::");
								} while (!("VT-" + id).equalsIgnoreCase(tempStrarr[0]));

								new ArrayList();
								senderList = this.stringToList(tempStrarr[1], "ID");
								this.logger.debug("VT's Analyst::" + senderList);
								message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
								message = message.replace("<updateBy>", updateBy);
								message = message.replace("<rejectionReason>", rejectReason);
								this.logger.debug(message);
								ResourceLocator.self().getNotificationService().createSystemNotification(message,
										message, senderList, crn);
								senderList = new ArrayList();
								i$ = this.setTOList(this.getVendorL2Manager_Supervisior(), "all").iterator();

								while (i$.hasNext()) {
									str2 = (String) i$.next();
									if (str2.split("::")[0].equalsIgnoreCase("VT-" + id)) {
										senderList.add(str2.split("::")[1].split("#")[0]);
									}
								}

								this.logger.debug("L2Manager & SuperVisior of Vendor's Team :: " + senderList);
								message = "This report has been rejected by <updateBy>. The Vendor Manager for this report is <VTanalystname>. Rejection comment : <rejectionReason>.";
								message = message.replace("<updateBy>", updateBy);
								message = message.replace("<VTanalystname>", tempStrarr[1].split("#")[1]);
								message = message.replace("<rejectionReason>", rejectReason);
								this.logger.debug(message);
								this.logger.debug("\n* * *");
								ResourceLocator.self().getNotificationService().createSystemNotification(message,
										message, senderList, crn);
								--id;
							}
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 9 :
				this.logger.debug("###############  PT ANALYST,L2MANAGER & SUPERVISIOR ##############");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				senderList = this.mergeToList(senderList, this.setTOList(this.getPTL2Manager_Supervisior(), "ID"));
				this.logger.debug("PT ANALYST , L2Manager & Supervisior ::" + senderList);
				message = "This report has been rejected by <updateBy>. The Research Elements that were rejected belong to <ST name> Team. You'll need to reconsolidate this report later. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							this.logger.debug("####################### BI TEAM ###############\n   BI Analyst");
							new ArrayList();
							senderList = this.stringToList(this.getBIAnalystName(), "ID");
							this.logger.debug("Supporting - BI Analyst:" + senderList);
							message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
							message = message.replace("<updateBy>", updateBy);
							message = message.replace("<rejectionReason>", rejectReason);
							this.logger.debug(message);
							ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
									senderList, crn);
							new ArrayList();
							senderList = this.setTOList(this.getBIL2Manager_Supervisior(), "ID");
							this.logger.debug("Supporting - BI supervisior:" + senderList);
							message = "This report has been rejected by <updateBy>. The analyst for this report is <BIanalystname>. Rejection comment : <rejectionReason>.";
							message = message.replace("<updateBy>", updateBy);
							message = message.replace("<BIanalystname>", this.getBIAnalystName().split("#")[1]);
							message = message.replace("<rejectionReason>", rejectReason);
							this.logger.debug(message);
							this.logger.debug("\n* * *");
							ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
									senderList, crn);
							return;
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 10 :
				this.logger.debug("###############  PT ANALYST,L2MANAGER & SUPERVISIOR ##############");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				senderList = this.mergeToList(senderList, this.setTOList(this.getPTL2Manager_Supervisior(), "ID"));
				this.logger.debug("PT ANALYST , L2Manager & Supervisior ::" + senderList);
				message = "This report has been rejected by <updateBy>. The Research Elements that were rejected belong to <ST name> Team. You'll need to reconsolidate this report later. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  SUPPOURTING INTERNAL TEAM ##############");
				id = 1;
				i$ = this.setTOList(this.getSTAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							this.logger.debug(
									"###############  Vendor TEAM ##############" + this.getVendorAnalystNameList());
							vendor = this.setTOList(this.getVendorAnalystNameList(), "ID");
							id = vendor.size();
							i$ = this.setTOList(this.getVendorAnalystNameList(), "all").iterator();

							while (true) {
								do {
									if (!i$.hasNext()) {
										this.logger.debug("\n * * *");
										return;
									}

									string = (String) i$.next();
									tempStrarr = string.split("::");
								} while (!("VT-" + id).equalsIgnoreCase(tempStrarr[0]));

								new ArrayList();
								senderList = this.stringToList(tempStrarr[1], "ID");
								this.logger.debug("VT's Analyst::" + senderList);
								message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
								message = message.replace("<updateBy>", updateBy);
								message = message.replace("<rejectionReason>", rejectReason);
								this.logger.debug(message);
								ResourceLocator.self().getNotificationService().createSystemNotification(message,
										message, senderList, crn);
								senderList = new ArrayList();
								i$ = this.setTOList(this.getVendorL2Manager_Supervisior(), "all").iterator();

								while (i$.hasNext()) {
									str2 = (String) i$.next();
									if (str2.split("::")[0].equalsIgnoreCase("VT-" + id)) {
										senderList.add(str2.split("::")[1].split("#")[0]);
									}
								}

								this.logger.debug("L2Manager & SuperVisior of Vendor's Team :: " + senderList);
								message = "This report has been rejected by <updateBy>. The Vendor Manager for this report is <VTanalystname>. Rejection comment : <rejectionReason>.";
								message = message.replace("<updateBy>", updateBy);
								message = message.replace("<VTanalystname>", tempStrarr[1].split("#")[1]);
								message = message.replace("<rejectionReason>", rejectReason);
								this.logger.debug(message);
								this.logger.debug("\n* * *");
								ResourceLocator.self().getNotificationService().createSystemNotification(message,
										message, senderList, crn);
								--id;
							}
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("ST-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("ST's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					i$ = this.setTOList(this.getSTL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase(tempStrarr[0])) {
							senderList = new ArrayList();
							((List) senderList).add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of ST's Team" + senderList);
					message = "This report has been rejected by <updateBy>. The analyst for this report is <STanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<STanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * * ");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							(List) senderList, crn);
					++id;
				}
			case 11 :
				this.logger.debug("###############  PT ANALYST,L2MANAGER & SUPERVISIOR ##############");
				new ArrayList();
				senderList = this.stringToList(this.getPTAnalystName(), "ID");
				senderList = this.mergeToList(senderList, this.setTOList(this.getPTL2Manager_Supervisior(), "ID"));
				this.logger.debug("PT ANALYST , L2Manager & Supervisior ::" + senderList);
				message = "This report has been rejected by <updateBy>. The Research Elements that were rejected belong to <ST name> Team. You'll need to reconsolidate this report later. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("####################### BI TEAM ###############\n   BI Analyst");
				new ArrayList();
				senderList = this.stringToList(this.getBIAnalystName(), "ID");
				this.logger.debug("Supporting - BI Analyst:" + senderList);
				message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				new ArrayList();
				senderList = this.setTOList(this.getBIL2Manager_Supervisior(), "ID");
				this.logger.debug("Supporting - BI supervisior:" + senderList);
				message = "This report has been rejected by <updateBy>. The analyst for this report is <BIanalystname>. Rejection comment : <rejectionReason>.";
				message = message.replace("<updateBy>", updateBy);
				message = message.replace("<BIanalystname>", this.getBIAnalystName().split("#")[1]);
				message = message.replace("<rejectionReason>", rejectReason);
				this.logger.debug(message);
				this.logger.debug("\n* * *");
				ResourceLocator.self().getNotificationService().createSystemNotification(message, message, senderList,
						crn);
				this.logger.debug("###############  Vendor TEAM ##############" + this.getVendorAnalystNameList());
				vendor = this.setTOList(this.getVendorAnalystNameList(), "ID");
				id = vendor.size();
				i$ = this.setTOList(this.getVendorAnalystNameList(), "all").iterator();

				while (true) {
					do {
						if (!i$.hasNext()) {
							return;
						}

						string = (String) i$.next();
						tempStrarr = string.split("::");
					} while (!("VT-" + id).equalsIgnoreCase(tempStrarr[0]));

					new ArrayList();
					senderList = this.stringToList(tempStrarr[1], "ID");
					this.logger.debug("VT's Analyst::" + senderList);
					message = "Rejected : Your report has been rejected by <updateBy>. Please rework. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							senderList, crn);
					senderList = new ArrayList();
					i$ = this.setTOList(this.getVendorL2Manager_Supervisior(), "all").iterator();

					while (i$.hasNext()) {
						str2 = (String) i$.next();
						if (str2.split("::")[0].equalsIgnoreCase("VT-" + id)) {
							senderList.add(str2.split("::")[1].split("#")[0]);
						}
					}

					this.logger.debug("L2Manager & SuperVisior of Vendor's Team :: " + senderList);
					message = "This report has been rejected by <updateBy>. The Vendor Manager for this report is <VTanalystname>. Rejection comment : <rejectionReason>.";
					message = message.replace("<updateBy>", updateBy);
					message = message.replace("<VTanalystname>", tempStrarr[1].split("#")[1]);
					message = message.replace("<rejectionReason>", rejectReason);
					this.logger.debug(message);
					this.logger.debug("\n* * *");
					ResourceLocator.self().getNotificationService().createSystemNotification(message, message,
							senderList, crn);
					--id;
				}
		}

	}

	public String rejectedOfficeList(Set<String> officeList, String type) {
		String officeStr = "";
		Iterator i$ = officeList.iterator();

		while (i$.hasNext()) {
			String officeName = (String) i$.next();
			if (type.equals("Primary") && officeName.indexOf("Primary") != -1) {
				if (officeStr.equals("")) {
					officeStr = officeStr + officeName;
				} else {
					officeStr = officeStr + "," + officeName;
				}
			}

			if (type.equals("Supporting - BI") && officeName.indexOf("Supporting - BI") != -1) {
				if (officeStr.equals("")) {
					officeStr = officeStr + officeName;
				} else {
					officeStr = officeStr + "," + officeName;
				}
			}

			if (type.equals("Supporting - Internal") && officeName.indexOf("Supporting - Internal") != -1) {
				if (officeStr.equals("")) {
					officeStr = officeStr + officeName;
				} else {
					officeStr = officeStr + "," + officeName;
				}
			}

			if (type.equals("Supporting - Vendor") && officeName.indexOf("Supporting - Vendor") != -1) {
				if (officeStr.equals("")) {
					officeStr = officeStr + officeName;
				} else {
					officeStr = officeStr + "," + officeName;
				}
			}

			if (type.equals("all")) {
				this.logger.debug(officeName);
				if (officeStr.equals("")) {
					officeStr = officeStr + officeName;
				} else {
					officeStr = officeStr + "," + officeName;
				}
			}
		}

		return officeStr;
	}
}