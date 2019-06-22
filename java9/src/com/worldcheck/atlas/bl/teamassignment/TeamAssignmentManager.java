package com.worldcheck.atlas.bl.teamassignment;

import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ITeamAssignment;
import com.worldcheck.atlas.dao.teamassignment.TeamAssignmentDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.services.sbm.SBMService;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class TeamAssignmentManager implements ITeamAssignment {
	private static final String REASSIGN_WORKITEM_NAME_DETAILS = "reassignWorkitemNameDetails";
	private static final String REASSIGN_MANAGER_DETAILS = "reassignManagerDetails";
	private static final String REASSIGN_SELECTED_TEAM_IDS = "reassignSelectedTeamIds";
	private static final String BLANK = "blank";
	private static final String WORKITEM_NAME_DETAILS = "workitemNameDetails";
	private static final String MANAGER_DETAILS = "managerDetails";
	private static final String TEAM_TYPE_DETAILS = "teamTypeDetails";
	private static final String CRN_DETAILS = "crnDetails";
	private static final String OTHER_OFFICE_DETAILS = "otherOfficeDetails";
	private static final String REVIEWER_DETAILS = "reviewerDetails";
	private static final String DOUBLECOMMA = ",,";
	private static final String ANALYST_DETAILS = "analystDetails";
	private static final String SELECTED_TEAM_IDS = "selectedTeamIds";
	private static final String DOUBLEHASHKEYSEPERATOR = ",##";
	private static final String TEAM_RE_DETAILS = "teamReDetails";
	private static final String _0 = "0";
	private static final String _1 = "1";
	private static final String MANAGER_NAME = "managerName";
	private static final String FINAL_DUE_DATE_ID = "rfinalDate";
	private static final String INTRIMDATE2 = "rinterim2";
	private static final String INTRIMDATE1 = "rinterim1";
	private static final String OTHER_OFFICE_DET = "otherOfficeDet";
	private static final String REVIEWER_DET = "reviewerDet";
	private static final String ANALYST_DET = "analystDet";
	private static final String TEAM_ID = "teamId";
	private static final String HOLIDAY_DATE = "holidayDate";
	private static final String REDIRECT_BPMPORTAL_ATLAS_COMPLETE_TASK_DO = "redirect:/bpmportal/atlas/completeTask.do";
	private static final String COMPLETE = "complete";
	private static final String TEAM_TYPE_NAME = "teamTypeName";
	private static final String TEAM_ASSIGNMENT_SECTION = "teamAssignmentSection";
	private static final String REDIRECT_BPMPORTAL_ATLAS_SAVE_TASK_DO = "redirect:/bpmportal/atlas/saveTask.do";
	private static final String SAVE = "save";
	private static final String UPDATE = "update";
	private static final String UPDATE_TYPE = "updateType";
	private static final String COMMAHASHKEY_SEPERATOR = ",#";
	private static final String TEAM_RE_DET = "teamReDet";
	private static final String ANALYST_COUNT = "analystCount";
	private String OA_DATE_FORMAT = "dd-MM-yy";
	Calendar cal = Calendar.getInstance();
	private SimpleDateFormat targetFormat;
	private static final String DATE_FORMAT_ddMMMyy = "dd-MMM-yy";
	private static final String DATE_FORMAT_ddMMMyyyy = "dd-MMM-yyyy";
	private SimpleDateFormat targetSdf;
	private SimpleDateFormat targetSdformat;
	private String OFFICE_ID;
	private static final String R_INTERIM1 = "teamInt1DueDateDetails";
	private static final String R_INTERIM2 = "teamInt2DueDateDetails";
	private static final String R_FINAL_DATE = "teamFinalDueDateDetails";
	private ILogProducer logger;
	private TeamAssignmentDAO teamAssignmentDAO;
	private static final String TEAMTYPE = "teamType";
	private static final String TEAM_IDS = "teamIds";

	public TeamAssignmentManager() {
		this.targetFormat = new SimpleDateFormat(this.OA_DATE_FORMAT);
		this.targetSdf = new SimpleDateFormat("dd-MMM-yy");
		this.targetSdformat = new SimpleDateFormat("dd-MMM-yyyy");
		this.OFFICE_ID = "officeId";
		this.logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.teamassignment.TeamAssignmentManager");
	}

	public void setTeamAssignmentDAO(TeamAssignmentDAO teamAssignmentDAO) {
		this.teamAssignmentDAO = teamAssignmentDAO;
	}

	public List<UserMasterVO> getTeamOfficeMaster(String teamType, String crn) throws CMSException {
		TeamDetails teamDetailsVO = new TeamDetails();
		new ArrayList();

		try {
			String[] teamTypeId = teamType.split("#");
			teamDetailsVO.setTeamType(teamTypeId[0]);
			teamDetailsVO.setTeamId(Integer.parseInt(teamTypeId[1]));
			teamDetailsVO.setCrn(crn);
			List<UserMasterVO> officeMasterList = this.teamAssignmentDAO.getTeamOfficeMaster(teamDetailsVO);
			this.logger.debug("office list " + officeMasterList.size());
			return officeMasterList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<UserMasterVO> getAnalystForManager(String managerId) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> analystForManager = this.teamAssignmentDAO.getAnalystForManager(managerId);
			this.logger.debug("analyst For Manager list " + analystForManager.size());
			return analystForManager;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> getTeamAnalystMaster(String teamType, String crn) throws CMSException {
		TeamDetails teamDetailsVO = new TeamDetails();
		new ArrayList();

		try {
			String[] teamTypeId = teamType.split("#");
			teamDetailsVO.setTeamType(teamTypeId[0]);
			teamDetailsVO.setTeamId(Integer.parseInt(teamTypeId[1]));
			teamDetailsVO.setCrn(crn);
			List<UserMasterVO> analystMasterList = this.teamAssignmentDAO.getTeamAnalystMaster(teamDetailsVO);
			this.logger.debug("analyst Master list " + analystMasterList.size());
			return analystMasterList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<UserMasterVO> getTeamReviewerMaster(String teamType, String crn) throws CMSException {
		TeamDetails teamDetailsVO = new TeamDetails();
		new ArrayList();

		try {
			String[] teamTypeId = teamType.split("#");
			teamDetailsVO.setTeamType(teamTypeId[0]);
			teamDetailsVO.setTeamId(Integer.parseInt(teamTypeId[1]));
			teamDetailsVO.setCrn(crn);
			List<UserMasterVO> reviewerMasterList = this.teamAssignmentDAO.getTeamReviewerMaster(teamDetailsVO);
			this.logger.debug("analyst Master list " + reviewerMasterList.size());
			return reviewerMasterList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<UserMasterVO> getReviewerForAllOffice() throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> reviewerList = this.teamAssignmentDAO.getReviewerForAllOffice();
			this.logger.debug("analyst For Manager list " + reviewerList.size());
			return reviewerList;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public TeamDetails getTeamDetails(String teamType) throws CMSException {
		new TeamDetails();

		try {
			String[] teamTypeId = teamType.split("#");
			TeamDetails teamDetails = this.teamAssignmentDAO.getTeamDetails(teamTypeId[1]);
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public TaskColorVO getColorDetails(String crn, String task) throws CMSException {
		HashMap<String, String> param = new HashMap();
		new TaskColorVO();

		try {
			param.put("crn", crn);
			param.put("taskName", task);
			TaskColorVO colorVO = this.teamAssignmentDAO.getColorDetails(param);
			return colorVO;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public CaseDetails getCaseDetails(String crn) throws CMSException {
		new CaseDetails();
		CaseDetails caseDetails = this.teamAssignmentDAO.getCaseDetails(crn);
		return caseDetails;
	}

	public List<Object> getSubjectDetails(String teamType) throws CMSException {
		new ArrayList();
		List<SubjectDetails> subjectDetailsList = new ArrayList();
		List<Object> resultList = new ArrayList();
		new HashMap();

		try {
			String[] teamTypeId = teamType.split("#");
			List<SubTeamReMapVO> subReDetails = this.teamAssignmentDAO.getSubjectDetails(teamTypeId[1]);
			String prevSubName = "";
			new ArrayList();
			this.initSubjectReList(subReDetails, subjectDetailsList);
			List<SubTeamReMapVO> subTeam = this.initAnalystList(teamTypeId[1]);
			resultList.add(subjectDetailsList);
			resultList.add(subTeam);
			resultList.add(subReDetails);
			return resultList;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private List<SubTeamReMapVO> initAnalystList(String teamId) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> subReDetails = this.teamAssignmentDAO.getAnalystForTeam(teamId);
			return subReDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	private List<SubTeamReMapVO> initAnalystReList(List<SubTeamReMapVO> subReDetails, List<SubTeamReMapVO> subTeam)
			throws CMSException {
		LinkedHashMap<String, SubTeamReMapVO> analystReMap = new LinkedHashMap();
		Iterator iterator = subReDetails.iterator();

		String reId;
		SubTeamReMapVO subTeamReMapVO;
		while (iterator.hasNext()) {
			SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) iterator.next();
			String analystName = subTeamReMap.getPerformer();
			reId = subTeamReMap.getReId();
			this.logger.debug("analyst name  " + analystName);
			subTeamReMapVO = new SubTeamReMapVO();
			if (analystName != null) {
				SubTeamReMapVO subMapVO = (SubTeamReMapVO) analystReMap.get(analystName);
				if (subMapVO != null) {
					String reIds = subMapVO.getReId();
					this.logger.debug(" re Ids for analyst " + reIds);
					if (reIds != null) {
						reId = reIds + "," + reId;
						this.logger.debug(" re Id for analyst " + reId);
					}
				}

				subTeamReMapVO.setReId(reId);
				subTeamReMapVO.setPerformerFullName(subTeamReMap.getPerformerFullName());
				analystReMap.put(analystName, subTeamReMapVO);
			}
		}

		Set<String> set = analystReMap.keySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			SubTeamReMapVO subTeamReMap = new SubTeamReMapVO();
			reId = (String) iterator.next();
			this.logger.debug("analyst " + reId);
			subTeamReMap.setPerformer(reId);
			subTeamReMapVO = (SubTeamReMapVO) analystReMap.get(reId);
			String reIds = subTeamReMapVO.getReId();
			subTeamReMap.setReIds(StringUtils.commaSeparatedStringToList(reIds));
			subTeamReMap.setPerformerFullName(subTeamReMapVO.getPerformerFullName());
			subTeam.add(subTeamReMap);
		}

		this.logger.debug("analyst re map " + subTeam.size());
		return subTeam;
	}

	private void initSubjectReList(List<SubTeamReMapVO> subReDetails, List<SubjectDetails> subjectDetailsList)
			throws NumberFormatException {
		Iterator iterator = subReDetails.iterator();

		while (true) {
			while (iterator.hasNext()) {
				new SubjectDetails();
				SubTeamReMapVO subTeamReMap = (SubTeamReMapVO) iterator.next();
				String subName = subTeamReMap.getSubject();
				String subId = subTeamReMap.getSubjectId();
				new ArrayList();
				ResearchElementMasterVO reMasterVo = new ResearchElementMasterVO();
				reMasterVo.setResearchElementName(subTeamReMap.getReName());
				reMasterVo.setrEMasterId(Integer.parseInt(subTeamReMap.getReId()));
				reMasterVo.setPoints(subTeamReMap.getJlpPoints());
				int i = 0;
				boolean match = false;

				for (Iterator iterator1 = subjectDetailsList.iterator(); iterator1.hasNext(); ++i) {
					SubjectDetails subjectDetails1 = (SubjectDetails) iterator1.next();
					if (subjectDetails1.getSubjectName().equals(subName)
							&& subjectDetails1.getSubjectId() == Integer.parseInt(subId)) {
						match = true;
						break;
					}
				}

				SubjectDetails subjectDetailsTemp;
				if (match) {
					subjectDetailsTemp = (SubjectDetails) subjectDetailsList.get(i);
					List<ResearchElementMasterVO> reList = subjectDetailsTemp.getReList();
					boolean matchRe = false;
					Iterator iterator2 = reList.iterator();

					while (iterator2.hasNext()) {
						ResearchElementMasterVO researchElementMasterVO = (ResearchElementMasterVO) iterator2.next();
						if (researchElementMasterVO.getrEMasterId() == reMasterVo.getrEMasterId()) {
							matchRe = true;
							break;
						}
					}

					if (!matchRe) {
						reList.add(reMasterVo);
						subjectDetailsTemp.setReList(reList);
					}
				} else {
					subjectDetailsTemp = new SubjectDetails();
					subjectDetailsTemp.setSubjectName(subTeamReMap.getSubject());
					subjectDetailsTemp.setSubjectId(Integer.parseInt(subTeamReMap.getSubjectId()));
					subjectDetailsTemp.setEntityName(subTeamReMap.getEntityName());
					subjectDetailsTemp.setCountryName(subTeamReMap.getCountryName());
					List<ResearchElementMasterVO> reList = new ArrayList();
					reList.add(reMasterVo);
					subjectDetailsTemp.setReList(reList);
					subjectDetailsList.add(subjectDetailsTemp);
				}
			}

			return;
		}
	}

	public List<SubTeamReMapVO> getUserAssignedRe(String userId, String teamId) throws CMSException {
		Object details = new ArrayList();

		try {
			List<Integer> teamIdList = StringUtils.commaSeparatedStringToIntList(teamId);
			if (teamIdList.size() > 0) {
				HashMap<String, Object> paramMap = new HashMap();
				paramMap.put("teamIds", teamIdList);
				paramMap.put("userId", userId);
				details = this.teamAssignmentDAO.getUserReForTeam(paramMap);
				this.logger.debug("size of teams " + ((List) details).size());
			}

			return (List) details;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public ModelAndView saveAssignmentTask(HttpServletRequest request) throws CMSException {
		ModelAndView modelAndView = null;
		CaseDetails newCaseDetail = new CaseDetails();
		TeamDetails teamDetails = new TeamDetails();
		CaseDetails oldCaseDetail = new CaseDetails();

		try {
			int analystCount = 0;
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("request.getParameter(Constants.WORKITEM) " + request.getParameter("workItem"));
			if (request.getParameter("workItem") != null && request.getParameter("updateType") != null
					&& request.getParameter("updateType").trim().length() > 0
					&& !request.getParameter("updateType").equalsIgnoreCase("update")
					&& !ResourceLocator.self().getSBMService().wiExistsForUser(request.getParameter("workItem"),
							SBMUtils.getSession(request), userBean.getUserName())) {
				this.logger.debug("redirecting to task status error page for workitem no longer exists.");
				return new ModelAndView("redirect:/bpmportal/atlas/taskStatusErrorPage.jsp");
			} else {
				this.populateTeamDetail(request, teamDetails);
				teamDetails.setUpdatedBy(userBean.getUserName());
				newCaseDetail.setUpdatedBy(userBean.getUserName());
				if (request.getParameter("analystCount") != null
						&& request.getParameter("analystCount").trim().length() > 0) {
					analystCount = Integer.parseInt(request.getParameter("analystCount"));
				}

				String[] teamReDetList = (String[]) null;
				if (request.getParameter("teamReDet") != null
						&& request.getParameter("teamReDet").trim().length() > 0) {
					teamReDetList = request.getParameter("teamReDet").split(",#");
				}

				String caseStatus = "";
				if (request.getParameter("crn") != null && request.getParameter("crn").trim().length() > 0) {
					CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService()
							.getCaseStatus(teamDetails.getCrn());
					caseStatus = caseDetails.getCaseStatus();
				}

				if (caseStatus != null
						&& (caseStatus.equalsIgnoreCase("Cancelled") || caseStatus.equalsIgnoreCase("On Hold"))) {
					if (request.getParameter("updateType") != null
							&& request.getParameter("updateType").trim().length() > 0
							&& request.getParameter("updateType").equalsIgnoreCase("update")) {
						this.logger.debug("redirecting to caseStatusErrorPage.jsp as per change in case status.");
						return new ModelAndView("redirect:/bpmportal/myhome/sectionCaseStatusError.jsp");
					} else {
						this.logger.debug("redirecting to caseStatusErrorPage.jsp as per change in case status.");
						return new ModelAndView("redirect:/bpmportal/atlas/caseStatusErrorPage.jsp");
					}
				} else {
					newCaseDetail.setCrn(teamDetails.getCrn());
					oldCaseDetail.setCrn(teamDetails.getCrn());
					oldCaseDetail.setUpdatedBy(userBean.getUserName());
					String primaryRinterim1 = null;
					String primaryRinterim2 = null;
					String primaryRfinal = null;
					String teamTInterim1 = null;
					String teamTInterim2 = null;
					String teamTFinal = null;
					if (teamDetails.getDueDate1() != null && teamDetails.getDueDate1().length() != 0) {
						oldCaseDetail
								.setrInterim1(new Date(this.targetFormat.parse(teamDetails.getDueDate1()).getTime()));
						teamTInterim1 = teamDetails.getDueDate1();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryRinterim1 = teamDetails.getDueDate1();
						}
					}

					if (teamDetails.getDueDate2() != null && teamDetails.getDueDate2().length() != 0) {
						oldCaseDetail
								.setrInterim2(new Date(this.targetFormat.parse(teamDetails.getDueDate2()).getTime()));
						teamTInterim2 = teamDetails.getDueDate2();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryRinterim2 = teamDetails.getDueDate2();
						}
					}

					oldCaseDetail.setFinalDueDate(
							new Date(this.targetFormat.parse(teamDetails.getFinalDueDate()).getTime()));
					teamTFinal = teamDetails.getFinalDueDate();
					this.logger.debug("teamTFinal supporting::" + teamTFinal);
					if (teamDetails.getTeamType().contains("Primary")) {
						primaryRfinal = teamDetails.getFinalDueDate();
						this.logger.debug("primaryRfinal::" + primaryRfinal);
					}

					newCaseDetail
							.setResearchDueDates(primaryRinterim1 + "::" + primaryRinterim2 + "::" + primaryRfinal);
					String newrInterim1 = "";
					String newrInterim2 = "";
					if (request.getParameter("rinterim1") != null
							&& request.getParameter("rinterim1").trim().length() > 0) {
						newrInterim1 = request.getParameter("rinterim1").toString();
						newCaseDetail.setrInterim1(new Date(this.targetFormat.parse(newrInterim1).getTime()));
					}

					if (request.getParameter("rinterim2") != null
							&& request.getParameter("rinterim2").trim().length() > 0) {
						newrInterim2 = request.getParameter("rinterim2").toString();
						newCaseDetail.setDueDate2(new Date(this.targetFormat.parse(newrInterim2).getTime()));
					}

					String newrFinal = request.getParameter("rfinalDate").toString();
					this.logger.debug("newrFinal::" + newrFinal);
					newCaseDetail.setFinalDueDate(new Date(this.targetFormat.parse(newrFinal).getTime()));
					this.populateOldCaseDetails(teamDetails, oldCaseDetail);
					this.teamAssignmentDAO.updateTeamDetails(teamDetails);
					this.logger.debug("request.getParameter(Constants.WORKITEM) " + request.getParameter("workItem"));
					newCaseDetail.setWorkitemName(request.getParameter("workItem"));
					oldCaseDetail.setWorkitemName(request.getParameter("workItem"));
					if (request.getParameter("updateType") != null
							&& request.getParameter("updateType").trim().length() > 0) {
						if (request.getParameter("updateType").equalsIgnoreCase("update")) {
							new ArrayList();
							List<SubTeamReMapVO> teamSubjectREDetails = new ArrayList();
							List<SubTeamReMapVO> subReDetails = this.teamAssignmentDAO
									.getSubjectDetails(String.valueOf(teamDetails.getTeamId()));
							int updateAnalystCount = this.teamAssignmentDAO.deleteAnalyst(teamDetails);
							int deletedAnalystCount = this.teamAssignmentDAO.deleteAnalystSubTeamReMap(teamDetails);
							this.logger.debug("deletedAnalystCount :: " + deletedAnalystCount);
							this.logger.debug("updateAnalystCount :: " + updateAnalystCount);
							List<Integer> hashCodeList = new ArrayList();
							List<Integer> hashCodeListWithJlp = new ArrayList();
							SubTeamReMapVO vo = null;

							int i;
							for (i = 0; i < subReDetails.size(); ++i) {
								vo = (SubTeamReMapVO) subReDetails.get(i);
								this.logger.debug(" hash code " + vo.hashCode());
								hashCodeList.add(vo.hashCode());
								hashCodeListWithJlp.add(vo.hashCodeWithJLP());
							}

							for (i = 0; i < analystCount; ++i) {
								teamDetails.setReIds(teamReDetList[i]);
								this.logger.debug(" analyst is " + (String) teamDetails.getAnalyst().get(i));
								this.updateReDetailForTeam(teamDetails, hashCodeList, hashCodeListWithJlp,
										(String) teamDetails.getAnalyst().get(i), teamSubjectREDetails);
							}
						} else {
							this.teamAssignmentDAO.deleteSubTeamDetails(teamDetails.getTeamId());

							for (int i = 0; i < analystCount; ++i) {
								teamDetails.setReIds(teamReDetList[i]);
								this.updatePerformerForSubTeam(teamDetails, (String) teamDetails.getAnalyst().get(i));
							}
						}
					}

					List<TeamDetails> teamList = new ArrayList();
					teamList.add(teamDetails);
					newCaseDetail.setTeamList(teamList);
					this.insertCaseHistory(teamDetails, oldCaseDetail, userBean, request.getParameter("updateType"),
							request);
					if (request.getParameter("updateType") != null
							&& request.getParameter("updateType").trim().length() > 0) {
						String[] temp;
						HashMap dsValues;
						if (request.getParameter("updateType").equalsIgnoreCase("save")) {
							modelAndView = new ModelAndView("redirect:/bpmportal/atlas/saveTask.do");
							temp = request.getParameter("workItem").split("::");
							this.logger.debug(" WI temp:::" + temp);
							dsValues = new HashMap();
							dsValues.put("taskPerformer", request.getParameter("taskPerformer"));
							long parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(
									Long.parseLong(temp[0].split("#")[1]), "parentPID", SBMUtils.getSession(request));
							this.logger.debug("parentPID is::" + parentPID);
							HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self()
									.getSBMService()
									.getDataslotValue(parentPID, "customDSMap", SBMUtils.getSession(request));
							this.logger.debug("customDSMap is::" + customDSMap);
							CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
							if (teamTInterim1 != null) {
								dsValues.put("tInterim1",
										this.targetSdf.format(this.targetFormat.parse(teamTInterim1)));
								teamTInterim1 = this.targetSdf.format(this.targetFormat.parse(teamTInterim1));
								this.logger.debug("teamTInterim1" + teamTInterim1);
							}

							if (teamTInterim2 != null) {
								dsValues.put("tInterim2",
										this.targetSdf.format(this.targetFormat.parse(teamTInterim2)));
								teamTInterim2 = this.targetSdf.format(this.targetFormat.parse(teamTInterim2));
								this.logger.debug("teamTInterim2" + teamTInterim2);
							}

							if (teamTFinal != null) {
								dsValues.put("tFinal", this.targetSdf.format(this.targetFormat.parse(teamTFinal)));
								teamTFinal = this.targetSdf.format(this.targetFormat.parse(teamTFinal));
								this.logger.debug("teamTFinal" + teamTFinal);
							}

							if (primaryRinterim1 != null) {
								dsValues.put("RInterim1",
										this.targetSdf.format(this.targetFormat.parse(primaryRinterim1)));
								primaryRinterim1 = this.targetSdf.format(this.targetFormat.parse(primaryRinterim1));
								this.logger.debug("primaryRinterim1" + primaryRinterim1);
							}

							if (primaryRinterim2 != null) {
								dsValues.put("RInterim2",
										this.targetSdf.format(this.targetFormat.parse(primaryRinterim2)));
								primaryRinterim2 = this.targetSdf.format(this.targetFormat.parse(primaryRinterim2));
								this.logger.debug("primaryRinterim2" + primaryRinterim2);
							}

							if (primaryRfinal != null) {
								dsValues.put("RFinal", this.targetSdf.format(this.targetFormat.parse(primaryRfinal)));
								primaryRfinal = this.targetSdf.format(this.targetFormat.parse(primaryRfinal));
								this.logger.debug("primaryRfinal" + primaryRfinal);
							}

							String rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(
									Long.parseLong(temp[0].split("#")[1]), "RInterim1", SBMUtils.getSession(request));
							String rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(
									Long.parseLong(temp[0].split("#")[1]), "RInterim2", SBMUtils.getSession(request));
							String rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(
									Long.parseLong(temp[0].split("#")[1]), "RFinal", SBMUtils.getSession(request));
							this.logger.debug("rInterim1::" + rInterim1);
							this.logger.debug("rInterim2::" + rInterim2);
							this.logger.debug("rFinal::" + rFinal);
							boolean ResearchDatesUpdated = false;
							this.logger.debug("before updateResearchDueDateDataslots::");
							if (teamDetails.getTeamType().contains("Primary")
									&& (rInterim1 != null && !rInterim1.equals(primaryRinterim1)
											|| rInterim2 != null && !rInterim2.equals(primaryRinterim2)
											|| rFinal != null && !rFinal.equals(primaryRfinal))) {
								this.logger.debug("In updateResearchDueDateDataslots::");
								ResourceLocator.self().getFlowService().updateResearchDueDateDataslots(parentPID,
										SBMUtils.getSession(request), newCaseDetail,
										cycleTeamMapping.getCycleInformation());
								ResearchDatesUpdated = true;
							}

							this.logger.debug("ResearchDatesUpdated flag for Team Assignment::" + ResearchDatesUpdated);
							ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
									Long.parseLong(temp[0].split("#")[1]), dsValues);
							modelAndView.addObject("workItem", request.getParameter("workItem"));
						} else if (request.getParameter("updateType").equalsIgnoreCase("update")) {
							ResourceLocator.self().getFlowService().updateDSAndPushFlow(SBMUtils.getSession(request),
									oldCaseDetail, newCaseDetail, "Team");
							modelAndView = new ModelAndView("teamAssignmentSection");
							modelAndView.addObject("crn", teamDetails.getCrn());
							modelAndView.addObject("teamType", request.getParameter("teamTypeName"));
						} else if (request.getParameter("updateType").equalsIgnoreCase("complete")) {
							ResourceLocator.self().getFlowService().updateDS(SBMUtils.getSession(request),
									newCaseDetail, "Team");
							modelAndView = new ModelAndView("redirect:/bpmportal/atlas/completeTask.do");
							temp = request.getParameter("workItem").split("::");
							dsValues = new HashMap();
							dsValues.put("taskPerformer", request.getParameter("taskPerformer"));
							ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
									Long.parseLong(temp[0].split("#")[1]), dsValues);
							modelAndView.addObject("workItem", request.getParameter("workItem"));
							modelAndView.addObject("workItem", request.getParameter("workItem"));
						}
					}

					return modelAndView;
				}
			}
		} catch (PatternSyntaxException var30) {
			throw new CMSException(this.logger, var30);
		} catch (Exception var31) {
			throw new CMSException(this.logger, var31);
		}
	}

	private void insertCaseHistory(TeamDetails teamDetails, CaseDetails oldCaseDetail, UserBean userBean, String action,
			HttpServletRequest request) throws CMSException {
		CaseDetails newCaseHistoryDetail = new CaseDetails();
		CaseHistory caseHistory = new CaseHistory();
		if (oldCaseDetail.getWorkitemName() != null && !"null".equalsIgnoreCase(oldCaseDetail.getWorkitemName())
				&& "".equalsIgnoreCase(oldCaseDetail.getWorkitemName())) {
			String wiName = oldCaseDetail.getWorkitemName();
			String[] temp = oldCaseDetail.getWorkitemName().split("::");
			this.logger.debug("wi name " + oldCaseDetail.getWorkitemName());
			this.logger.debug("pid is " + Long.parseLong(temp[0].split("#")[1]));
			caseHistory.setPid(String.valueOf(Long.parseLong(temp[0].split("#")[1])));
		} else {
			CaseDetails caseDetailsPID = ResourceLocator.self().getCaseDetailService()
					.getCaseInfo(oldCaseDetail.getCrn());
			caseHistory.setPid(caseDetailsPID.getPid());
		}

		newCaseHistoryDetail.setCrn(oldCaseDetail.getCrn());
		this.populateOldCaseDetails(teamDetails, newCaseHistoryDetail);
		caseHistory.setCRN(oldCaseDetail.getCrn());
		if (action != null && action.equalsIgnoreCase("update")) {
			caseHistory.setTaskName("");
			caseHistory.setTaskStatus("");
		} else {
			caseHistory.setTaskName("Team Assignment Task");
			caseHistory.setTaskStatus("In Progress");
		}

		if (request.getSession().getAttribute("loginLevel") != null) {
			caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
		} else {
			caseHistory.setPerformer(userBean.getUserName());
		}

		ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetail, newCaseHistoryDetail, caseHistory,
				"Team");
	}

	private void insertBulkCaseHistory(TeamDetails teamDetails, CaseDetails oldCaseDetail, UserBean userBean,
			String action) throws CMSException {
		CaseDetails newCaseHistoryDetail = new CaseDetails();
		CaseHistory caseHistory = new CaseHistory();
		if (oldCaseDetail.getWorkitemName() != null && !"null".equalsIgnoreCase(oldCaseDetail.getWorkitemName())
				&& "".equalsIgnoreCase(oldCaseDetail.getWorkitemName())) {
			String wiName = oldCaseDetail.getWorkitemName();
			String[] temp = oldCaseDetail.getWorkitemName().split("::");
			this.logger.debug("wi name " + oldCaseDetail.getWorkitemName());
			this.logger.debug("pid is " + Long.parseLong(temp[0].split("#")[1]));
			caseHistory.setPid(String.valueOf(Long.parseLong(temp[0].split("#")[1])));
		} else {
			CaseDetails caseDetailsPID = ResourceLocator.self().getCaseDetailService()
					.getCaseInfo(oldCaseDetail.getCrn());
			caseHistory.setPid(caseDetailsPID.getPid());
		}

		newCaseHistoryDetail.setCrn(oldCaseDetail.getCrn());
		this.populateOldCaseDetails(teamDetails, newCaseHistoryDetail);
		caseHistory.setCRN(oldCaseDetail.getCrn());
		caseHistory.setPerformer(userBean.getUserName());
		if (action != null && action.equalsIgnoreCase("update")) {
			caseHistory.setTaskName("");
			caseHistory.setTaskStatus("");
		} else {
			caseHistory.setTaskName("Team Assignment Task");
			caseHistory.setTaskStatus("In Progress");
		}

		ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetail, newCaseHistoryDetail, caseHistory,
				"Team");
	}

	private void populateOldCaseDetails(TeamDetails teamDetails, CaseDetails oldCaseDetail) throws CMSException {
		List<TeamDetails> oldTeamDetails = new ArrayList();
		new TeamDetails();

		try {
			HashMap param = new HashMap();
			param.put("teamId", teamDetails.getTeamId());
			param.put("crn", oldCaseDetail.getCrn());
			TeamDetails oldTeamDetail = this.teamAssignmentDAO.getCompleteTeamDetails(param);
			this.populateOldTeamDetails(oldTeamDetail);
			oldTeamDetails.add(oldTeamDetail);
			this.logger.debug("oldTeamDetails size:: " + oldTeamDetails.size());
			oldCaseDetail.setTeamList(oldTeamDetails);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private void populateOldTeamDetails(TeamDetails oldTeamDetail) throws CMSException {
		this.logger.debug("old team details " + oldTeamDetail);
		oldTeamDetail.setAnalyst(StringUtils.commaSeparatedStringToList(oldTeamDetail.getAnalysts()));
		this.logger.debug("number of performer " + oldTeamDetail.getPerformer());
		oldTeamDetail.setAnalystId(StringUtils.commaSeparatedStringToList(oldTeamDetail.getPerformer()));
		List<String> reviewerList = new ArrayList();
		if (oldTeamDetail.getReviewer1() != null) {
			reviewerList.add(oldTeamDetail.getReviewer1());
		}

		if (oldTeamDetail.getReviewer2() != null) {
			reviewerList.add(oldTeamDetail.getReviewer2());
		}

		if (oldTeamDetail.getReviewer3() != null) {
			reviewerList.add(oldTeamDetail.getReviewer3());
		}

		oldTeamDetail.setReviewer(reviewerList);
	}

	private TeamDetails updateReDetailForTeam(TeamDetails teamDetail, List<Integer> hashCodeList,
			List<Integer> hashCodeListWithJLP, String performer, List<SubTeamReMapVO> teamSubjectREDetails)
			throws CMSException {
		this.logger.debug("in updateReDetailForTeam Re Ids :: " + teamDetail.getReIds() + " for team :: "
				+ teamDetail.getTeamId());

		try {
			this.logger.debug(" analyst  updateReDetailForTeam is " + performer);
			this.logger.debug("Re Ids length before :: " + teamDetail.getReIds().length());
			if (teamDetail.getReIds().startsWith("#")) {
				teamDetail.setReIds(teamDetail.getReIds().replace("#", ""));
			}

			this.logger.debug("Re Ids length after :: " + teamDetail.getReIds().length());
			String[] subReDetails = teamDetail.getReIds().split(",");
			String subjectIds = "";
			String previousSubjectId = "";
			String reIds = "";
			HashMap param = null;
			List<SubTeamReMapVO> teamSubjectREDetailsAdd = new ArrayList();
			List<SubTeamReMapVO> teamSubjectREDetailsUpdate = new ArrayList();

			int i;
			for (i = 0; i < subReDetails.length; ++i) {
				SubTeamReMapVO vo = new SubTeamReMapVO();
				this.logger.debug("team crn " + teamDetail.getCrn());
				vo.setCrn(teamDetail.getCrn());
				vo.setTeamId(teamDetail.getTeamId());
				if (i == 0) {
					previousSubjectId = subReDetails[i].split("::")[0];
					subjectIds = subjectIds + subReDetails[i].split("::")[0];
					this.logger.debug(" i == 0 :: " + previousSubjectId);
				} else if (previousSubjectId.equalsIgnoreCase(subReDetails[i].split("::")[0])) {
					this.logger
							.debug("same subjectId :: " + previousSubjectId + " :: " + subReDetails[i].split("::")[0]);
					previousSubjectId = subReDetails[i].split("::")[0];
				} else {
					this.logger.debug("subjectId not same :: " + previousSubjectId + " :: "
							+ subReDetails[i].split("::")[0] + " :: reIds " + reIds);
					param = new HashMap();
					param.put("teamId", teamDetail.getTeamId());
					param.put("subjectId", previousSubjectId);
					param.put("reIds", StringUtils.commaSeparatedStringToIntList(reIds));
					param.put("crn", teamDetail.getCrn());
					param.put("performer", performer);
					int reMapDeletedCount = this.teamAssignmentDAO.deleteSubjectREMappingForAnalyst(param);
					this.logger.debug("reMapDeletedCount records :: " + reMapDeletedCount);
					reIds = "";
					previousSubjectId = subReDetails[i].split("::")[0];
					subjectIds = subjectIds + "," + subReDetails[i].split("::")[0];
				}

				vo.setSubjectId(subReDetails[i].split("::")[0]);
				vo.setReId(subReDetails[i].split("::")[1]);
				if (i != 0 && reIds.trim().length() != 0) {
					reIds = reIds + "," + subReDetails[i].split("::")[1];
				} else {
					reIds = reIds + subReDetails[i].split("::")[1];
				}

				vo.setJlpPoints(subReDetails[i].split("::")[2]);
				vo.setUpdatedBy(teamDetail.getUpdatedBy());
				vo.setPerformer(performer);
				this.logger.debug(" analyst  updateReDetailForTeam is " + performer + " reid " + vo.getReId()
						+ " subject id " + vo.getSubjectId() + " team id " + vo.getTeamId());
				this.logger.debug(" hash code  " + vo.hashCode());
				if (hashCodeList.contains(vo.hashCode())) {
					if (hashCodeListWithJLP.contains(vo.hashCodeWithJLP())) {
						this.logger.debug("no change in jlp as well");
					} else {
						this.logger.debug("update jlp points");
						teamSubjectREDetailsUpdate.add(vo);
					}
				} else {
					this.logger.debug("going to insert");
					teamSubjectREDetailsAdd.add(vo);
				}

				teamSubjectREDetails.add(vo);
			}

			if (teamSubjectREDetailsAdd.size() > 0) {
				this.teamAssignmentDAO.addTeamREDetailsList(teamSubjectREDetailsAdd);
			} else if (teamSubjectREDetailsUpdate.size() > 0) {
				this.teamAssignmentDAO.updatePerformerReJlpList(teamSubjectREDetailsUpdate);
			}

			if (reIds.trim().length() > 0) {
				param = new HashMap();
				param.put("teamId", teamDetail.getTeamId());
				param.put("subjectId", previousSubjectId);
				param.put("reIds", StringUtils.commaSeparatedStringToIntList(reIds));
				param.put("performer", performer);
				param.put("crn", teamDetail.getCrn());
				i = this.teamAssignmentDAO.deleteSubjectREMappingForAnalyst(param);
				this.logger.debug("reMapDeletedCount records :: " + i);
			}

			this.logger.debug("subjectIds :: " + subjectIds);
			param = new HashMap();
			param.put("teamId", teamDetail.getTeamId());
			param.put("subjectIds", StringUtils.commaSeparatedStringToIntList(subjectIds));
			param.put("crn", teamDetail.getCrn());
			param.put("performer", performer);
			i = this.teamAssignmentDAO.deleteSubjectMappingForAnalyst(param);
			this.logger.debug("deletedCount records :: " + i);
			teamDetail.setTeamSubjectREDetails(teamSubjectREDetails);
			return teamDetail;
		} catch (PatternSyntaxException var16) {
			throw new CMSException(this.logger, var16);
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	private void updatePerformerForSubTeam(TeamDetails teamDetail, String performer) throws CMSException {
		this.logger.debug("Re Ids :: " + teamDetail.getReIds());

		try {
			this.logger.debug("Re Ids length before :: " + teamDetail.getReIds().length());
			if (teamDetail.getReIds().startsWith("#")) {
				teamDetail.setReIds(teamDetail.getReIds().replace("#", ""));
			}

			this.logger.debug("Re Ids length after :: " + teamDetail.getReIds().length());
			List<SubTeamReMapVO> teamSubjectREDetails = new ArrayList();
			String[] subReDetails = teamDetail.getReIds().split(",");

			for (int i = 0; i < subReDetails.length; ++i) {
				this.logger.debug("subReDetails[i] :: " + subReDetails[i]);
				SubTeamReMapVO vo = new SubTeamReMapVO();
				vo.setCrn(teamDetail.getCrn());
				vo.setTeamId(teamDetail.getTeamId());
				vo.setSubjectId(subReDetails[i].split("::")[0]);
				vo.setReId(subReDetails[i].split("::")[1]);
				vo.setJlpPoints(subReDetails[i].split("::")[2]);
				vo.setPerformer(performer);
				vo.setUpdatedBy(teamDetail.getUpdatedBy());
				teamSubjectREDetails.add(vo);
			}

			this.teamAssignmentDAO.addTeamREDetailsList(teamSubjectREDetails);
			teamDetail.setTeamSubjectREDetails(teamSubjectREDetails);
		} catch (PatternSyntaxException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private void populateTeamDetail(HttpServletRequest request, TeamDetails teamDetails) throws CMSException {
		try {
			if (request.getParameter("analystDet") != null && request.getParameter("analystDet").trim().length() > 0) {
				List<String> analystList = StringUtils.commaSeparatedStringToList(request.getParameter("analystDet"));
				this.logger.debug(" analyst details  " + request.getParameter("analystDet"));
				if (request.getParameter("analystDet").endsWith(",")) {
					teamDetails.setCsvAnalysts(request.getParameter("analystDet").substring(0,
							request.getParameter("analystDet").length() - 1));
				} else {
					teamDetails.setCsvAnalysts(request.getParameter("analystDet"));
				}

				teamDetails.setAnalyst(analystList);
				teamDetails.setMainAnalyst((String) analystList.get(0));
			}

			if (request.getParameter("reviewerDet") != null
					&& request.getParameter("reviewerDet").trim().length() > 0) {
				this.populateReviewers(request.getParameter("reviewerDet"), teamDetails);
			}

			if (request.getParameter("otherOfficeDet") != null
					&& request.getParameter("otherOfficeDet").trim().length() > 0) {
				teamDetails.setOtherOffice(request.getParameter("otherOfficeDet"));
				this.populateOtherOffice(request.getParameter("otherOfficeDet"), teamDetails);
			}

			if (request.getParameter("crn") != null && request.getParameter("crn").trim().length() > 0) {
				teamDetails.setCrn(request.getParameter("crn"));
			}

			if (request.getParameter("teamTypeName") != null
					&& request.getParameter("teamTypeName").trim().length() > 0) {
				String teamType = request.getParameter("teamTypeName");
				String[] teamTypeList = teamType.split("#");
				teamDetails.setTeamType(teamTypeList[0]);
				teamDetails.setTeamId(Integer.parseInt(teamTypeList[1]));
			}

			if (request.getParameter("rinterim1") != null && request.getParameter("rinterim1").trim().length() > 0) {
				teamDetails.setDueDate1(request.getParameter("rinterim1"));
			}

			if (request.getParameter("rinterim2") != null && request.getParameter("rinterim2").trim().length() > 0) {
				teamDetails.setDueDate2(request.getParameter("rinterim2"));
			}

			if (request.getParameter("rfinalDate") != null && request.getParameter("rfinalDate").trim().length() > 0) {
				teamDetails.setFinalDueDate(request.getParameter("rfinalDate"));
			}

			if (request.getParameter("managerName") != null
					&& request.getParameter("managerName").trim().length() > 0) {
				teamDetails.setManagerName(request.getParameter("managerName"));
			}

		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private void populateOtherOffice(String otherOfficeDet, TeamDetails teamDetails) throws CMSException {
		try {
			List<String> otherOfficeList = StringUtils.commaSeparatedStringToList(otherOfficeDet);
			int otherOfficeCount = 0;
			Iterator var6 = otherOfficeList.iterator();

			while (var6.hasNext()) {
				String otherOffice = (String) var6.next();
				++otherOfficeCount;
				if (otherOffice.equalsIgnoreCase("true")) {
					otherOffice = "1";
				} else {
					otherOffice = "0";
				}

				if (otherOfficeCount == 1) {
					teamDetails.setOtherOffice1(otherOffice);
				} else if (otherOfficeCount == 2) {
					teamDetails.setOtherOffice2(otherOffice);
				} else if (otherOfficeCount == 3) {
					teamDetails.setOtherOffice3(otherOffice);
				}
			}

			if (teamDetails.getOtherOffice2() == null) {
				teamDetails.setOtherOffice2("0");
				teamDetails.setOtherOffice3("0");
			} else if (teamDetails.getOtherOffice3() == null) {
				teamDetails.setOtherOffice3("0");
			}

		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private void populateReviewers(String reviewerDet, TeamDetails teamDetails) throws CMSException {
		try {
			List<String> reviewerList = StringUtils.commaSeparatedStringToList(reviewerDet);
			teamDetails.setReviewer(reviewerList);
			int reviewerCount = 0;
			Iterator var6 = reviewerList.iterator();

			while (var6.hasNext()) {
				String reviewer = (String) var6.next();
				++reviewerCount;
				if (reviewerCount == 1) {
					teamDetails.setReviewer1(reviewer);
				} else if (reviewerCount == 2) {
					teamDetails.setReviewer2(reviewer);
				} else if (reviewerCount == 3) {
					teamDetails.setReviewer3(reviewer);
				}
			}

		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<TeamDetails> getTeamDetailsForCase(String userId, String crn) {
		return null;
	}

	public List<ResearchElementMasterVO> getTeamRes(String teamId) {
		return null;
	}

	public List<TeamDetails> getCaseTeamDetails(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetailsList = this.teamAssignmentDAO.getCaseTeamDetails(crn);
			return teamDetailsList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public ModelAndView reassignTeamAssignmentTask(HttpServletRequest request) throws CMSException {
		String managerName = null;
		String[] temp = (String[]) null;
		TeamDetails newTeamDetails = new TeamDetails();
		TeamDetails oldTeamDetails = new TeamDetails();
		CaseDetails oldCaseDEtails = new CaseDetails();
		CaseDetails newCaseDEtails = new CaseDetails();
		CaseHistory caseHistory = new CaseHistory();

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (request.getParameter("workItem") != null
					&& !ResourceLocator.self().getSBMService().wiExistsForUser(request.getParameter("workItem"),
							SBMUtils.getSession(request), userBean.getUserName())) {
				this.logger.debug("redirecting to task status error page for workitem no longer exists.");
				return new ModelAndView("redirect:/bpmportal/atlas/taskStatusErrorPage.jsp");
			} else {
				this.populateTeamDetail(request, newTeamDetails);
				this.logger.debug("request parameters in map for reassign " + request.getParameterMap());
				if (request.getParameter("managerName") != null) {
					managerName = request.getParameter("managerName");
				}

				if (request.getParameter("crn") != null) {
					newTeamDetails.setCrn(request.getParameter("crn"));
					oldTeamDetails.setCrn(request.getParameter("crn"));
					oldCaseDEtails.setCrn(request.getParameter("crn"));
					newCaseDEtails.setCrn(request.getParameter("crn"));
					caseHistory.setCRN(request.getParameter("crn"));
				}

				String wiName;
				if (request.getParameter("teamTypeName") != null
						&& request.getParameter("teamTypeName").trim().length() > 0) {
					wiName = request.getParameter("teamTypeName");
					this.logger.debug("team type in reassign " + wiName);
					String[] teamTypeList = wiName.split("#");
					newTeamDetails.setTeamType(teamTypeList[0]);
					oldTeamDetails.setTeamType(teamTypeList[0]);
					newTeamDetails.setTeamId(Integer.parseInt(teamTypeList[1]));
					oldTeamDetails.setTeamId(Integer.parseInt(teamTypeList[1]));
				}

				caseHistory.setCRN(request.getParameter("crn"));
				caseHistory.setTaskName("Team Assignment Task");
				caseHistory.setTaskStatus("In Progress");
				if (request.getSession().getAttribute("loginLevel") != null) {
					caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
				} else {
					caseHistory.setPerformer(userBean.getUserName());
				}

				this.populateOldCaseDetails(oldTeamDetails, oldCaseDEtails);
				newTeamDetails.setUpdatedBy(userBean.getUserName());
				newTeamDetails.setManagerName(managerName);
				newTeamDetails.setOtherOffice1("0");
				newTeamDetails.setOtherOffice2("0");
				newTeamDetails.setOtherOffice3("0");
				this.teamAssignmentDAO.updateTeamDetails(newTeamDetails);
				this.populateOldCaseDetails(newTeamDetails, newCaseDEtails);
				wiName = null;
				if (request.getParameter("workItem") != null) {
					wiName = request.getParameter("workItem");
					temp = request.getParameter("workItem").split("::");
					this.logger.debug("wi name " + request.getParameter("workItem"));
					this.logger.debug("going to reassign task " + managerName);
					this.logger.debug("pid is " + Long.parseLong(temp[0].split("#")[1]));
					this.logger.debug(" ws name " + temp[1]);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO.setCrn(newTeamDetails.getCrn());
					timeTrackerVO.setUserName(userBean.getUserName());
					timeTrackerVO.setUpdatedBy(userBean.getUserName());
					timeTrackerVO.setTaskName("Team Assignment Task");
					timeTrackerVO.setTaskPid(temp[0].split("#")[1]);
					caseHistory.setPid(temp[0].split("#")[1]);
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					request.getSession().removeAttribute("TrackerOn");
					HashMap<String, Object> dsValues = new HashMap();
					dsValues.put("TeamResearchHeadList", managerName);
					dsValues.put("TaskStatus", "New");
					ResourceLocator.self().getCaseHistoryService().setCaseHistoryForTeamManagerReassign(
							(TeamDetails) oldCaseDEtails.getTeamList().get(0),
							(TeamDetails) newCaseDEtails.getTeamList().get(0), caseHistory);
					HashMap<String, Object> caseCreationDSValues = new HashMap();
					long caseCreationPID = Long.parseLong(ResourceLocator.self().getCaseDetailService()
							.getCaseInfo(newTeamDetails.getCrn()).getPid());
					HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
							.getDataslotValue(caseCreationPID, "customDSMap", SBMUtils.getSession(request));
					CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
					Map<String, CycleInfo> cycleInformation = cycleTeamMapping.getCycleInformation();
					Iterator var20 = cycleInformation.keySet().iterator();

					while (var20.hasNext()) {
						String cycle = (String) var20.next();
						CycleInfo cycleInfo = (CycleInfo) cycleInformation.get(cycle);
						TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) cycleInfo.getTeamInfo()
								.get(request.getParameter("teamTypeName"));
						if (teamAnalystMapping != null) {
							teamAnalystMapping.setResearchHead(managerName);
						}
					}

					caseCreationDSValues.put("customDSMap", customDSMap);
					ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
							caseCreationPID, caseCreationDSValues);
					ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
							Long.parseLong(temp[0].split("#")[1]), dsValues);
					ResourceLocator.self().getSBMService().reassignTask(Long.parseLong(temp[0].split("#")[1]),
							managerName, wiName, SBMUtils.getSession(request));
				}

				return null;
			}
		} catch (Exception var23) {
			throw new CMSException(this.logger, var23);
		}
	}

	public List<SubTeamReMapVO> getAssignedUsers(String crn, String teamType) throws CMSException {
		new ArrayList();
		SubTeamReMapVO subTeamReMapVO = new SubTeamReMapVO();

		try {
			String[] teamTypeId = teamType.split("#");
			subTeamReMapVO.setCrn(crn);
			subTeamReMapVO.setTeamId(Integer.parseInt(teamTypeId[1]));
			List<SubTeamReMapVO> subReDetails = this.teamAssignmentDAO.getAssignedUsers(subTeamReMapVO);
			return subReDetails;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public HashMap<String, Object> getTeamAssignmentTaskDetails(HttpServletRequest request, MyTaskPageVO taskVo)
			throws CMSException {
		HashMap resultMap = null;

		try {
			List<TeamDetails> details = new ArrayList();
			SBMService sbmService = ResourceLocator.self().getSBMService();
			resultMap = sbmService.getBulkDataForUser(SBMUtils.getSession(request), "Team", taskVo);
			long totalCount = 0L;
			List<MyTaskPageVO> myTaskPageVOList = null;
			if (resultMap != null && !resultMap.isEmpty()) {
				totalCount = (Long) resultMap.get("total");
				myTaskPageVOList = (List) resultMap.get("processInstanceList");
			}

			this.logger.debug("number of cases assigned " + myTaskPageVOList.size() + "::totalCount :; " + totalCount);
			List<TeamDetails> details = this.initCaseDetail(details, myTaskPageVOList);
			this.mergeTeamData(details, myTaskPageVOList);
			this.logger.debug("size of teams " + details.size());
			if (resultMap != null && !resultMap.isEmpty()) {
				resultMap.put("processInstanceList", details);
			} else {
				resultMap = new HashMap();
				resultMap.put("processInstanceList", details);
				resultMap.put("total", 0);
			}

			return resultMap;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private void mergeTeamData(List<TeamDetails> details, List<MyTaskPageVO> myTaskPageVOList) {
		new ArrayList();
		Iterator var5 = myTaskPageVOList.iterator();

		while (var5.hasNext()) {
			MyTaskPageVO myTaskPageVO = (MyTaskPageVO) var5.next();
			Iterator var7 = details.iterator();

			while (var7.hasNext()) {
				TeamDetails teamDetails = (TeamDetails) var7.next();
				String teamName = myTaskPageVO.getTeamTypeList();
				String[] teamTypeId = teamName.split("#");
				if (teamDetails.getTeamId() == Integer.parseInt(teamTypeId[1])) {
					this.logger.debug("work item name for team " + teamDetails.getTeamId() + " is "
							+ myTaskPageVO.getWorlItemName());
					teamDetails.setWorkitemName(myTaskPageVO.getWorlItemName());
				}
			}
		}

	}

	private List<TeamDetails> initCaseDetail(List<TeamDetails> details, List<MyTaskPageVO> myTaskPageVOList)
			throws CMSException {
		try {
			List<Integer> teamIds = new ArrayList();
			Iterator var5 = myTaskPageVOList.iterator();

			while (var5.hasNext()) {
				MyTaskPageVO myTaskPageVO = (MyTaskPageVO) var5.next();
				String teamName = myTaskPageVO.getTeamTypeList();
				String[] teamTypeId = teamName.split("#");
				this.logger.debug("team type " + teamTypeId[0]);
				this.logger.debug("team type id " + teamTypeId[1]);
				teamIds.add(Integer.parseInt(teamTypeId[1]));
			}

			this.logger.debug("size of team ids " + teamIds.size());
			if (teamIds.size() > 0) {
				HashMap<String, List> paramMap = new HashMap();
				paramMap.put("teamIds", teamIds);
				details = this.teamAssignmentDAO.getCompleteCaseDetailsList(paramMap);
				this.logger.debug("size of teams " + details.size());
			}

			return details;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<TeamDetails> getAllOfficeMaster() throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> officeDetails = this.teamAssignmentDAO.getAllOfficeMaster();
			return officeDetails;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getAllReviewerMaster() throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> reviewerDetails = this.teamAssignmentDAO.getAllReviewerMaster();
			return reviewerDetails;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getTeamSubjectDetails(String teamIds) throws CMSException {
		Object details = new ArrayList();

		try {
			List<Integer> teamIdList = StringUtils.commaSeparatedStringToIntList(teamIds);
			if (teamIdList.size() > 0) {
				HashMap<String, List> paramMap = new HashMap();
				paramMap.put("teamIds", teamIdList);
				details = this.teamAssignmentDAO.getTeamSubjectDetails(paramMap);
				this.logger.debug("size of teams " + ((List) details).size());
			}

			return (List) details;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getTeamAnalystDetails(String teamIds) throws CMSException {
		Object details = new ArrayList();

		try {
			List<Integer> teamIdList = StringUtils.commaSeparatedStringToIntList(teamIds);
			if (teamIdList.size() > 0) {
				HashMap<String, List> paramMap = new HashMap();
				paramMap.put("teamIds", teamIdList);
				details = this.teamAssignmentDAO.getTeamAnalystDetails(paramMap);
				this.logger.debug("size of teams " + ((List) details).size());
			}

			return (List) details;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getAnalystReDetails(String teamIds) throws CMSException {
		Object details = new ArrayList();

		try {
			List<Integer> teamIdList = StringUtils.commaSeparatedStringToIntList(teamIds);
			if (teamIdList.size() > 0) {
				HashMap<String, List> paramMap = new HashMap();
				paramMap.put("teamIds", teamIdList);
				details = this.teamAssignmentDAO.getAnalystReDetails(paramMap);
				this.logger.debug("size of teams " + ((List) details).size());
			}

			return (List) details;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getAllAnalystMaster() throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> reviewerDetails = this.teamAssignmentDAO.getAllAnalystMaster();
			return reviewerDetails;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public ModelAndView saveAssignmentTaskTab(HttpServletRequest request) throws CMSException {
		List<TeamDetails> teamDetails = new ArrayList();
		new CaseDetails();
		new CaseDetails();

		try {
			int analystCount = false;
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			List<Integer> reassignTeamIds = new ArrayList();
			List<Integer> selectedIds = new ArrayList();
			if (request.getParameter("reassignSelectedTeamIds") != null
					&& request.getParameter("reassignSelectedTeamIds").trim().length() > 0) {
				reassignTeamIds = StringUtils
						.commaSeparatedStringToIntList(request.getParameter("reassignSelectedTeamIds"));
			}

			if (request.getParameter("selectedTeamIds") != null
					&& request.getParameter("selectedTeamIds").trim().length() > 0) {
				this.logger.debug("selectedTeamIds " + request.getParameter("selectedTeamIds"));
				selectedIds = StringUtils.commaSeparatedStringToIntList(request.getParameter("selectedTeamIds"));
			}

			((List) selectedIds).addAll((Collection) reassignTeamIds);
			HashMap<String, String> caseStatusMap = new HashMap();
			if (selectedIds != null && ((List) selectedIds).size() > 0) {
				caseStatusMap = this.teamAssignmentDAO.getStatusForCases((List) selectedIds);
			}

			this.logger.debug("case status map in team assignment task " + caseStatusMap);
			TimeTrackerVO timeTrackerVO = null;
			boolean stopTT = false;
			if (request.getSession().getAttribute("TrackerOn") != null
					&& !request.getSession().getAttribute("TrackerOn").equals("")) {
				stopTT = true;
				timeTrackerVO = (TimeTrackerVO) request.getSession().getAttribute("trackerBean");
			}

			if (request.getParameter("reassignWorkitemNameDetails") != null
					&& request.getParameter("reassignWorkitemNameDetails").trim().length() > 0) {
				this.logger.debug("workitemNameDetails :: " + request.getParameter("reassignWorkitemNameDetails"));
				if (timeTrackerVO != null && stopTT) {
					if (request.getParameter("reassignWorkitemNameDetails").contains(timeTrackerVO.getWorkItemName())) {
						this.logger.debug("stopping team assignment task tracker for crn :: " + timeTrackerVO.getCrn()
								+ " from bulk assignment");
					} else {
						stopTT = false;
					}
				}
			}

			if (request.getParameter("workitemNameDetails") != null
					&& request.getParameter("workitemNameDetails").trim().length() > 0) {
				this.logger.debug("workitemNameDetails :: " + request.getParameter("workitemNameDetails"));
				if (timeTrackerVO != null && stopTT) {
					if (request.getParameter("workitemNameDetails").contains(timeTrackerVO.getWorkItemName())) {
						this.logger.debug("stopping team assignment task tracker for crn :: " + timeTrackerVO.getCrn()
								+ " from bulk assignment");
					} else {
						stopTT = false;
					}
				}
			}

			if (stopTT) {
				if (timeTrackerVO != null) {
					timeTrackerVO.setUpdatedBy(userBean.getUserName());
					timeTrackerVO.setUserName(userBean.getUserName());
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					request.getSession().removeAttribute("TrackerOn");
				} else {
					this.logger.debug("timeTrackerVO is null in session so not stopping tracker.");
				}
			}

			this.reassignTabTask(request, caseStatusMap);
			this.populateTeamDetailTab(request, teamDetails, caseStatusMap);
			this.logger.debug("number of teams to be updated " + teamDetails.size());
			String[] teamReDetList = (String[]) null;
			if (request.getParameter("teamReDetails") != null
					&& request.getParameter("teamReDetails").trim().length() > 0) {
				this.logger.debug("team re details " + request.getParameter("teamReDetails"));
				teamReDetList = request.getParameter("teamReDetails").split(",##");
			}

			int teamCount = 0;
			List<CaseDetails> caseDetailsList = new ArrayList();
			List<CaseDetails> oldCaseDetailsList = new ArrayList();
			List<CaseDetails> newCaseDetailsList = new ArrayList();
			new CaseDetails();

			for (Iterator iterator = teamDetails.iterator(); iterator.hasNext(); ++teamCount) {
				CaseDetails newCaseDetail = new CaseDetails();
				CaseDetails oldCaseDetail = new CaseDetails();
				CaseDetails caseHistoryCaseDetails = new CaseDetails();
				TeamDetails teamDetailsVO = (TeamDetails) iterator.next();
				newCaseDetail.setCrn(teamDetailsVO.getCrn());
				oldCaseDetail.setCrn(teamDetailsVO.getCrn());
				caseHistoryCaseDetails.setCrn(teamDetailsVO.getCrn());
				this.populateOldCaseDetails(teamDetailsVO, oldCaseDetail);
				teamDetailsVO.setUpdatedBy(userBean.getUserName());
				this.teamAssignmentDAO.updateTeamDetails(teamDetailsVO);
				this.teamAssignmentDAO.deleteSubTeamDetails(teamDetailsVO.getTeamId());
				int analystCount = teamDetailsVO.getAnalyst().size();

				for (int i = 0; i < analystCount; ++i) {
					teamDetailsVO.setReIds(teamReDetList[teamCount].split(",#")[i]);
					this.updatePerformerForSubTeam(teamDetailsVO, (String) teamDetailsVO.getAnalyst().get(i));
				}

				newCaseDetail.setWorkitemName(teamDetailsVO.getWorkitemName());
				oldCaseDetail.setWorkitemName(teamDetailsVO.getWorkitemName());
				List<TeamDetails> teamList = new ArrayList();
				teamList.add(teamDetailsVO);
				newCaseDetail.setTeamList(teamList);
				if (request.getSession().getAttribute("loginLevel") != null) {
					newCaseDetail.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
					caseHistoryCaseDetails
							.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
				} else {
					newCaseDetail.setCaseHistoryPerformer(userBean.getUserName());
					caseHistoryCaseDetails.setCaseHistoryPerformer(userBean.getUserName());
				}

				caseHistoryCaseDetails.setUpdatedBy(userBean.getUserName());
				newCaseDetail.setUpdatedBy(userBean.getUserName());
				if (teamDetailsVO.getDueDate1() != null && teamDetailsVO.getDueDate1().length() > 0) {
					newCaseDetail
							.setrInterim1(new Date(this.targetFormat.parse(teamDetailsVO.getDueDate1()).getTime()));
				}

				if (teamDetailsVO.getDueDate2() != null && teamDetailsVO.getDueDate2().length() > 0) {
					newCaseDetail
							.setrInterim2(new Date(this.targetFormat.parse(teamDetailsVO.getDueDate2()).getTime()));
				}

				if (teamDetailsVO.getFinalDueDate() != null && teamDetailsVO.getFinalDueDate().length() > 0) {
					newCaseDetail.setFinalDueDate(
							new Date(this.targetFormat.parse(teamDetailsVO.getFinalDueDate()).getTime()));
				}

				caseDetailsList.add(newCaseDetail);
				this.populateOldCaseDetails(teamDetailsVO, caseHistoryCaseDetails);
				oldCaseDetailsList.add(oldCaseDetail);
				newCaseDetailsList.add(caseHistoryCaseDetails);
			}

			ResourceLocator.self().getFlowService().bulkUpdateDSAndCompleteTask(SBMUtils.getSession(request),
					caseDetailsList, "Team");

			for (int i = 0; i < caseDetailsList.size(); ++i) {
				CaseDetails caseDetailsTemp = (CaseDetails) caseDetailsList.get(i);
				CaseDetails oldCaseDetailsTemp = (CaseDetails) oldCaseDetailsList.get(i);
				if (!caseDetailsTemp.isTaskCompleted()) {
					((CaseDetails) newCaseDetailsList.get(i)).setTaskCompleted(caseDetailsTemp.isTaskCompleted());
					List<TeamDetails> oldTeamDetailsTempList = oldCaseDetailsTemp.getTeamList();
					TeamDetails oldTeamDetailsTemp = (TeamDetails) oldTeamDetailsTempList.get(0);
					oldTeamDetailsTemp.setCsvAnalysts(oldTeamDetailsTemp.getPerformer());
					this.teamAssignmentDAO.updateTeamDetails(oldTeamDetailsTemp);
					this.teamAssignmentDAO.deleteSubTeamDetails(oldTeamDetailsTemp.getTeamId());
					List<SubTeamReMapVO> oldSubTeamReDetails = oldTeamDetailsTemp.getTeamSubjectREDetails();

					for (int j = 0; j < oldSubTeamReDetails.size(); ++j) {
						SubTeamReMapVO tempVO = (SubTeamReMapVO) oldSubTeamReDetails.get(j);
						tempVO.setCrn(oldTeamDetailsTemp.getCrn());
						this.teamAssignmentDAO.addTeamREDetails(tempVO);
					}
				}
			}

			ResourceLocator.self().getCaseHistoryService().setCaseHistoryForBulkTeamAssignment(oldCaseDetailsList,
					newCaseDetailsList);
			return null;
		} catch (Exception var26) {
			throw new CMSException(this.logger, var26);
		}
	}

	private void reassignTabTask(HttpServletRequest request, HashMap<String, String> caseStatusMap)
			throws CMSException {
		try {
			this.logger.debug("in reassignTabTask");
			String primaryRinterim1 = null;
			String primaryRinterim2 = null;
			String primaryRfinal = null;
			String teamTInterim1 = null;
			String teamTInterim2 = null;
			String teamTFinal = null;
			List<Integer> reassignTeamIds = new ArrayList();
			List<String> reassignManagerDetails = new ArrayList();
			List<String> reassignWorkitemNameDetails = new ArrayList();
			List<String> reassignCRNDetails = new ArrayList();
			List<String> reassignTeamTypeDetails = new ArrayList();
			List<CaseDetails> oldCaseDetailsList = new ArrayList();
			List<CaseDetails> newCaseDetailsList = new ArrayList();
			HashMap<String, String> CRNPIDMapping = new HashMap();
			if (request.getParameter("reassignSelectedTeamIds") != null
					&& request.getParameter("reassignSelectedTeamIds").trim().length() > 0) {
				reassignTeamIds = StringUtils
						.commaSeparatedStringToIntList(request.getParameter("reassignSelectedTeamIds"));
			}

			if (request.getParameter("reassignTeamTypeDetails") != null
					&& request.getParameter("reassignTeamTypeDetails").trim().length() > 0) {
				reassignTeamTypeDetails = StringUtils
						.commaSeparatedStringToList(request.getParameter("reassignTeamTypeDetails"));
			}

			if (request.getParameter("reassignCRNDetails") != null
					&& request.getParameter("reassignCRNDetails").trim().length() > 0) {
				reassignCRNDetails = StringUtils.commaSeparatedStringToList(request.getParameter("reassignCRNDetails"));
			}

			if (request.getParameter("reassignManagerDetails") != null
					&& request.getParameter("reassignManagerDetails").trim().length() > 0) {
				reassignManagerDetails = StringUtils
						.commaSeparatedStringToList(request.getParameter("reassignManagerDetails"));
			}

			if (request.getParameter("reassignWorkitemNameDetails") != null
					&& request.getParameter("reassignWorkitemNameDetails").trim().length() > 0) {
				reassignWorkitemNameDetails = StringUtils
						.commaSeparatedStringToList(request.getParameter("reassignWorkitemNameDetails"));
			}

			if (reassignCRNDetails != null && ((List) reassignCRNDetails).size() > 0) {
				CRNPIDMapping = ResourceLocator.self().getCaseDetailService()
						.getPIDsForBulkCRN((List) reassignCRNDetails);
			}

			String[] int1DateDetails = (String[]) null;
			if (request.getParameter("teamInt1DueDateDetails") != null
					&& request.getParameter("teamInt1DueDateDetails").trim().length() > 0) {
				this.logger.debug("R_INTERIM1 reassignTabTask:: " + request.getParameter("teamInt1DueDateDetails"));
				int1DateDetails = request.getParameter("teamInt1DueDateDetails").split(",");
			}

			String[] int2DateDetails = (String[]) null;
			if (request.getParameter("teamInt2DueDateDetails") != null
					&& request.getParameter("teamInt2DueDateDetails").trim().length() > 0) {
				this.logger.debug("R_INTERIM2 reassignTabTask:: " + request.getParameter("teamInt2DueDateDetails"));
				int2DateDetails = request.getParameter("teamInt2DueDateDetails").split(",");
			}

			String[] finalDateDetails = (String[]) null;
			if (request.getParameter("teamFinalDueDateDetails") != null
					&& request.getParameter("teamFinalDueDateDetails").trim().length() > 0) {
				this.logger.debug("R_FINAL_DATE reassignTabTask :: " + request.getParameter("teamFinalDueDateDetails"));
				finalDateDetails = request.getParameter("teamFinalDueDateDetails").split(",");
			}

			String[] teamTypeList = (String[]) null;
			String[] teamType = (String[]) null;
			if (request.getParameter("teamTypeDetails") != null
					&& request.getParameter("teamTypeDetails").trim().length() > 0) {
				this.logger.debug("request.getParameter(TEAM_TYPE_DETAILS)reassignTabTask::"
						+ request.getParameter("teamTypeDetails"));
				teamTypeList = request.getParameter("teamTypeDetails").split(",");
			}

			String wiName = null;
			TeamDetails teamDetails = null;
			TeamDetails oldTeamDetails = null;
			TeamDetails newTeamDetails = null;
			CaseDetails oldCaseDetails = null;
			CaseDetails newCaseDetails = null;
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");

			for (int i = 0; i < ((List) reassignTeamIds).size(); ++i) {
				Boolean workItemExistForUser = ((List) reassignWorkitemNameDetails).get(i) != null && !ResourceLocator
						.self().getSBMService().wiExistsForUser((String) ((List) reassignWorkitemNameDetails).get(i),
								SBMUtils.getSession(request), userBean.getUserName());
				this.logger.debug("workItemExistForUser before >> " + workItemExistForUser);
				if (request.getParameter("teamAssignmentLeftNav") != null
						&& request.getParameter("teamAssignmentLeftNav").trim().length() > 0
						&& request.getParameter("teamAssignmentLeftNav").equals("true")) {
					workItemExistForUser = false;
				}

				this.logger.debug("workItemExistForUser after >> " + workItemExistForUser);
				if (!((String) caseStatusMap.get(String.valueOf(((List) reassignTeamIds).get(i))))
						.equalsIgnoreCase("Cancelled")
						&& !((String) caseStatusMap.get(String.valueOf(((List) reassignTeamIds).get(i))))
								.equalsIgnoreCase("On Hold")
						&& !workItemExistForUser) {
					teamDetails = new TeamDetails();
					oldTeamDetails = new TeamDetails();
					newTeamDetails = new TeamDetails();
					oldCaseDetails = new CaseDetails();
					newCaseDetails = new CaseDetails();
					this.logger.debug("team id is " + ((List) reassignTeamIds).get(i));
					teamDetails.setTeamId((Integer) ((List) reassignTeamIds).get(i));
					oldTeamDetails.setTeamId((Integer) ((List) reassignTeamIds).get(i));
					newTeamDetails.setTeamId((Integer) ((List) reassignTeamIds).get(i));
					oldCaseDetails.setCrn((String) ((List) reassignCRNDetails).get(i));
					newCaseDetails.setCrn((String) ((List) reassignCRNDetails).get(i));
					newCaseDetails.setUpdatedBy(userBean.getUserName());
					teamDetails.setUpdatedBy(userBean.getUserName());
					teamDetails.setManagerName((String) ((List) reassignManagerDetails).get(i));
					teamDetails.setOtherOffice1("0");
					teamDetails.setOtherOffice2("0");
					teamDetails.setOtherOffice3("0");
					if (teamTypeList[i] != null && !"".equalsIgnoreCase(teamTypeList[i])
							&& !"blank".equalsIgnoreCase(teamTypeList[i])) {
						this.logger.debug("teamTypeList[i] in reassignment task tab:: " + teamTypeList[i]);
						teamType = teamTypeList[i].split("#");
						teamDetails.setTeamType(teamType[0]);
					}

					if (int1DateDetails[i] != null && !"".equalsIgnoreCase(int1DateDetails[i])
							&& !"blank".equalsIgnoreCase(int1DateDetails[i])) {
						this.logger.debug("int1DateDetails[i]in reassignment task tab:: " + int1DateDetails[i]);
						teamDetails.setDueDate1(int1DateDetails[i]);
					}

					if (int2DateDetails[i] != null && !"".equalsIgnoreCase(int2DateDetails[i])
							&& !"blank".equalsIgnoreCase(int2DateDetails[i])) {
						this.logger.debug("int2DateDetails[i]in reassignment task tab:: " + int2DateDetails[i]);
						teamDetails.setDueDate2(int2DateDetails[i]);
					}

					this.logger.debug("finalDateDetails[i] in reassignment task tab:: " + finalDateDetails[i]);
					teamDetails.setFinalDueDate(finalDateDetails[i]);
					if (teamDetails.getDueDate1() != null && teamDetails.getDueDate1().length() != 0) {
						oldCaseDetails
								.setrInterim1(new Date(this.targetFormat.parse(teamDetails.getDueDate1()).getTime()));
						teamTInterim1 = teamDetails.getDueDate1();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryRinterim1 = teamDetails.getDueDate1();
						}
					}

					if (teamDetails.getDueDate2() != null && teamDetails.getDueDate2().length() != 0) {
						oldCaseDetails
								.setrInterim2(new Date(this.targetFormat.parse(teamDetails.getDueDate2()).getTime()));
						teamTInterim2 = teamDetails.getDueDate2();
						if (teamDetails.getTeamType().contains("Primary")) {
							primaryRinterim2 = teamDetails.getDueDate2();
						}
					}

					oldCaseDetails.setFinalDueDate(
							new Date(this.targetFormat.parse(teamDetails.getFinalDueDate()).getTime()));
					this.logger.debug("old case detail::final due date" + oldCaseDetails.getFinalRDueDate());
					teamTFinal = teamDetails.getFinalDueDate();
					this.logger.debug("teamTFinal supporting::" + teamTFinal);
					if (teamDetails.getTeamType().contains("Primary")) {
						primaryRfinal = teamDetails.getFinalDueDate();
						this.logger.debug("primaryRfinal::" + primaryRfinal);
					}

					newCaseDetails
							.setResearchDueDates(primaryRinterim1 + "::" + primaryRinterim2 + "::" + primaryRfinal);
					String newrInterim1 = "";
					String newrInterim2 = "";
					if (int1DateDetails[i] != null && !"".equalsIgnoreCase(int1DateDetails[i])
							&& !"blank".equalsIgnoreCase(int1DateDetails[i])) {
						newrInterim1 = request.getParameter("teamInt1DueDateDetails").toString();
						newCaseDetails.setrInterim1(new Date(this.targetFormat.parse(newrInterim1).getTime()));
					}

					if (int2DateDetails[i] != null && !"".equalsIgnoreCase(int2DateDetails[i])
							&& !"blank".equalsIgnoreCase(int2DateDetails[i])) {
						newrInterim2 = request.getParameter("teamInt2DueDateDetails").toString();
						newCaseDetails.setrInterim2(new Date(this.targetFormat.parse(newrInterim2).getTime()));
					}

					String newrFinal = request.getParameter("teamFinalDueDateDetails").toString();
					this.logger.debug("newrFinal::" + newrFinal);
					newCaseDetails.setFinalDueDate(new Date(this.targetFormat.parse(newrFinal).getTime()));
					this.logger.debug("getFinalRDueDate" + newCaseDetails.getFinalDueDate());
					this.populateOldCaseDetails(oldTeamDetails, oldCaseDetails);
					this.teamAssignmentDAO.updateTeamDetails(teamDetails);
					this.populateOldCaseDetails(newTeamDetails, newCaseDetails);
					wiName = (String) ((List) reassignWorkitemNameDetails).get(i);
					oldCaseDetails.setWorkitemName(wiName);
					newCaseDetails.setWorkitemName(wiName);
					if (request.getSession().getAttribute("loginLevel") != null) {
						oldCaseDetails
								.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
						newCaseDetails
								.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
					} else {
						newCaseDetails
								.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
						newCaseDetails
								.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
					}

					String[] temp = wiName.split("::");
					this.logger.debug("wi name " + wiName);
					this.logger.debug("going to reassign task " + (String) ((List) reassignManagerDetails).get(i));
					this.logger.debug("pid is " + Long.parseLong(temp[0].split("#")[1]));
					this.logger.debug(" ws name " + temp[1]);
					HashMap<String, Object> caseCreationDSValues = new HashMap();
					this.logger.debug("crn :: " + (String) ((List) reassignCRNDetails).get(i));
					long caseCreationPID = Long
							.parseLong((String) CRNPIDMapping.get(String.valueOf(((List) reassignCRNDetails).get(i))));
					this.logger.debug("caseCreationPID :: " + caseCreationPID);
					HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
							.getDataslotValue(caseCreationPID, "customDSMap", SBMUtils.getSession(request));
					CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
					Map<String, CycleInfo> cycleInformation = cycleTeamMapping.getCycleInformation();
					Iterator var42 = cycleInformation.keySet().iterator();

					while (var42.hasNext()) {
						String cycle = (String) var42.next();
						CycleInfo cycleInfo = (CycleInfo) cycleInformation.get(cycle);
						this.logger.debug("team Name :: " + (String) ((List) reassignTeamTypeDetails).get(i)
								+ " :: new Manager :: " + (String) ((List) reassignManagerDetails).get(i));
						TeamAnalystMapping teamAnalystMapping = (TeamAnalystMapping) cycleInfo.getTeamInfo()
								.get((String) ((List) reassignTeamTypeDetails).get(i) + "#"
										+ ((List) reassignTeamIds).get(i));
						if (teamAnalystMapping != null) {
							teamAnalystMapping.setResearchHead((String) ((List) reassignManagerDetails).get(i));
						}
					}

					caseCreationDSValues.put("customDSMap", customDSMap);
					ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
							caseCreationPID, caseCreationDSValues);
					HashMap<String, Object> dsValues = new HashMap();
					dsValues.put("TeamResearchHeadList", ((List) reassignManagerDetails).get(i));
					dsValues.put("TaskStatus", "New");
					if (teamTInterim1 != null) {
						dsValues.put("tInterim1", this.targetSdf.format(this.targetFormat.parse(teamTInterim1)));
						teamTInterim1 = this.targetSdf.format(this.targetFormat.parse(teamTInterim1));
						this.logger.debug("teamTInterim1" + teamTInterim1);
					}

					if (teamTInterim2 != null) {
						dsValues.put("tInterim2", this.targetSdf.format(this.targetFormat.parse(teamTInterim2)));
						teamTInterim2 = this.targetSdf.format(this.targetFormat.parse(teamTInterim2));
						this.logger.debug("teamTInterim2" + teamTInterim2);
					}

					if (teamTFinal != null) {
						dsValues.put("tFinal", this.targetSdf.format(this.targetFormat.parse(teamTFinal)));
						teamTFinal = this.targetSdf.format(this.targetFormat.parse(teamTFinal));
						this.logger.debug("teamTFinal" + teamTFinal);
					}

					if (primaryRinterim1 != null) {
						dsValues.put("RInterim1", this.targetSdf.format(this.targetFormat.parse(primaryRinterim1)));
						primaryRinterim1 = this.targetSdf.format(this.targetFormat.parse(primaryRinterim1));
						this.logger.debug("primaryRinterim1" + primaryRinterim1);
					}

					if (primaryRinterim2 != null) {
						dsValues.put("RInterim2", this.targetSdf.format(this.targetFormat.parse(primaryRinterim2)));
						primaryRinterim2 = this.targetSdf.format(this.targetFormat.parse(primaryRinterim2));
						this.logger.debug("primaryRinterim2" + primaryRinterim2);
					}

					if (primaryRfinal != null) {
						dsValues.put("RFinal", this.targetSdf.format(this.targetFormat.parse(primaryRfinal)));
						primaryRfinal = this.targetSdf.format(this.targetFormat.parse(primaryRfinal));
						this.logger.debug("primaryRfinal" + primaryRfinal);
					}

					String rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(
							Long.parseLong(temp[0].split("#")[1]), "RInterim1", SBMUtils.getSession(request));
					String rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(
							Long.parseLong(temp[0].split("#")[1]), "RInterim2", SBMUtils.getSession(request));
					String rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(
							Long.parseLong(temp[0].split("#")[1]), "RFinal", SBMUtils.getSession(request));
					this.logger.debug("rInterim1::" + rInterim1);
					this.logger.debug("rInterim2::" + rInterim2);
					this.logger.debug("rFinal::" + rFinal);
					boolean ResearchDatesUpdated = false;
					this.logger.debug("before updateResearchDueDateDataslots::");
					if (teamDetails.getTeamType().contains("Primary")
							&& (rInterim1 != null && !rInterim1.equals(primaryRinterim1)
									|| rInterim2 != null && !rInterim2.equals(primaryRinterim2)
									|| rFinal != null && !rFinal.equals(primaryRfinal))) {
						this.logger.debug("In updateResearchDueDateDataslots::");
						ResourceLocator.self().getFlowService().updateResearchDueDateDataslots(caseCreationPID,
								SBMUtils.getSession(request), newCaseDetails, cycleTeamMapping.getCycleInformation());
						ResearchDatesUpdated = true;
					}

					this.logger.debug("ResearchDatesUpdated flag for Team Assignment::" + ResearchDatesUpdated);
					oldCaseDetailsList.add(oldCaseDetails);
					newCaseDetailsList.add(newCaseDetails);
					ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
							Long.parseLong(temp[0].split("#")[1]), dsValues);
					ResourceLocator.self().getSBMService().reassignTask(Long.parseLong(temp[0].split("#")[1]),
							(String) ((List) reassignManagerDetails).get(i), wiName, SBMUtils.getSession(request));
				} else {
					this.logger.debug("not to process for team " + String.valueOf(((List) reassignTeamIds).get(i))
							+ " as change in case status.");
				}
			}

			ResourceLocator.self().getCaseHistoryService().setCaseHistoryForBulkTeamManagerReassign(oldCaseDetailsList,
					newCaseDetailsList);
		} catch (Exception var46) {
			throw new CMSException(this.logger, var46);
		}
	}

	private void populateTeamDetailTab(HttpServletRequest request, List<TeamDetails> teamDetails,
			HashMap<String, String> caseStatusMap) throws CMSException {
		try {
			new TeamDetails();
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			List<Integer> selectedIds = new ArrayList();
			if (request.getParameter("selectedTeamIds") != null
					&& request.getParameter("selectedTeamIds").trim().length() > 0) {
				this.logger.debug("selectedTeamIds " + request.getParameter("selectedTeamIds"));
				selectedIds = StringUtils.commaSeparatedStringToIntList(request.getParameter("selectedTeamIds"));
			}

			String[] analystDetails = (String[]) null;
			if (request.getParameter("analystDetails") != null
					&& request.getParameter("analystDetails").trim().length() > 0) {
				this.logger.debug("analysts " + request.getParameter("analystDetails"));
				analystDetails = request.getParameter("analystDetails").split(",,");
			}

			String[] reviewerDetails = (String[]) null;
			if (request.getParameter("reviewerDetails") != null
					&& request.getParameter("reviewerDetails").trim().length() > 0) {
				reviewerDetails = request.getParameter("reviewerDetails").split(",,");
			}

			String[] otherOfficeDetails = (String[]) null;
			if (request.getParameter("otherOfficeDetails") != null
					&& request.getParameter("otherOfficeDetails").trim().length() > 0) {
				otherOfficeDetails = request.getParameter("otherOfficeDetails").split(",,");
			}

			String[] crnDetails = (String[]) null;
			if (request.getParameter("crnDetails") != null && request.getParameter("crnDetails").trim().length() > 0) {
				crnDetails = request.getParameter("crnDetails").split(",");
			}

			String[] teamTypeDetails = (String[]) null;
			if (request.getParameter("teamTypeDetails") != null
					&& request.getParameter("teamTypeDetails").trim().length() > 0) {
				teamTypeDetails = request.getParameter("teamTypeDetails").split(",");
			}

			String[] managerDetails = (String[]) null;
			if (request.getParameter("managerDetails") != null
					&& request.getParameter("managerDetails").trim().length() > 0) {
				this.logger.debug(" request.getParameter() " + request.getParameter("managerDetails"));
				managerDetails = request.getParameter("managerDetails").split(",#");
			}

			String[] workitemNameDetails = (String[]) null;
			if (request.getParameter("workitemNameDetails") != null
					&& request.getParameter("workitemNameDetails").trim().length() > 0) {
				this.logger.debug(" request.getParameter() " + request.getParameter("workitemNameDetails"));
				workitemNameDetails = request.getParameter("workitemNameDetails").split(",");
			}

			String[] int1DateDetails = (String[]) null;
			if (request.getParameter("teamInt1DueDateDetails") != null
					&& request.getParameter("teamInt1DueDateDetails").trim().length() > 0) {
				this.logger.debug("R_INTERIM1 :: " + request.getParameter("teamInt1DueDateDetails"));
				int1DateDetails = request.getParameter("teamInt1DueDateDetails").split(",");
			}

			String[] int2DateDetails = (String[]) null;
			if (request.getParameter("teamInt2DueDateDetails") != null
					&& request.getParameter("teamInt2DueDateDetails").trim().length() > 0) {
				this.logger.debug("R_INTERIM2 :: " + request.getParameter("teamInt2DueDateDetails"));
				int2DateDetails = request.getParameter("teamInt2DueDateDetails").split(",");
			}

			String[] finalDateDetails = (String[]) null;
			if (request.getParameter("teamFinalDueDateDetails") != null
					&& request.getParameter("teamFinalDueDateDetails").trim().length() > 0) {
				this.logger.debug("R_FINAL_DATE :: " + request.getParameter("teamFinalDueDateDetails"));
				finalDateDetails = request.getParameter("teamFinalDueDateDetails").split(",");
			}

			this.populateUpdatedTeamDetailsList(teamDetails, (List) selectedIds, analystDetails, reviewerDetails,
					otherOfficeDetails, crnDetails, teamTypeDetails, managerDetails, workitemNameDetails,
					int1DateDetails, int2DateDetails, finalDateDetails, caseStatusMap, SBMUtils.getSession(request),
					userBean.getUserName());
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	private void populateUpdatedTeamDetailsList(List<TeamDetails> teamDetails, List<Integer> selectedIds,
			String[] analystDetails, String[] reviewerDetails, String[] otherOfficeDetails, String[] crnDetails,
			String[] teamTypeDetails, String[] managerDetails, String[] workitemNameDetails, String[] int1DateDetails,
			String[] int2DateDetails, String[] finalDateDetails, HashMap<String, String> caseStatusMap, Session session,
			String userName) throws CMSException {
		try {
			for (int i = 0; i < selectedIds.size(); ++i) {
				this.logger.debug("team id " + selectedIds.get(i));
				if (!((String) caseStatusMap.get(String.valueOf(selectedIds.get(i)))).equalsIgnoreCase("Cancelled")
						&& !((String) caseStatusMap.get(String.valueOf(selectedIds.get(i)))).equalsIgnoreCase("On Hold")
						&& (workitemNameDetails[i] == null || ResourceLocator.self().getSBMService()
								.wiExistsForUser(workitemNameDetails[i], session, userName))) {
					Integer teamId = (Integer) selectedIds.get(i);
					TeamDetails teamDetail = new TeamDetails();
					teamDetail.setTeamId(teamId);
					if (analystDetails[i].endsWith(",")) {
						teamDetail.setCsvAnalysts(analystDetails[i].substring(0, analystDetails[i].length() - 1));
					} else {
						teamDetail.setCsvAnalysts(analystDetails[i]);
					}

					teamDetail.setAnalysts(analystDetails[i]);
					teamDetail.setWorkitemName(workitemNameDetails[i]);
					teamDetail.setAnalyst(StringUtils.commaSeparatedStringToList(analystDetails[i]));
					teamDetail
							.setMainAnalyst((String) StringUtils.commaSeparatedStringToList(analystDetails[i]).get(0));
					if (managerDetails[i] != null && !"".equalsIgnoreCase(managerDetails[i])
							&& !"blank".equalsIgnoreCase(managerDetails[i])) {
						this.logger.debug("managerDetails[i] " + managerDetails[i]);
						teamDetail.setManagerName(managerDetails[i]);
					}

					if (int1DateDetails[i] != null && !"".equalsIgnoreCase(int1DateDetails[i])
							&& !"blank".equalsIgnoreCase(int1DateDetails[i])) {
						this.logger.debug("int1DateDetails[i] " + int1DateDetails[i]);
						teamDetail.setDueDate1(int1DateDetails[i]);
					}

					if (int2DateDetails[i] != null && !"".equalsIgnoreCase(int2DateDetails[i])
							&& !"blank".equalsIgnoreCase(int2DateDetails[i])) {
						this.logger.debug("int2DateDetails[i] " + int2DateDetails[i]);
						teamDetail.setDueDate2(int2DateDetails[i]);
					}

					this.logger.debug("finalDateDetails[i] " + finalDateDetails[i]);
					teamDetail.setFinalDueDate(finalDateDetails[i]);
					teamDetail.setTeamType(teamTypeDetails[i]);
					teamDetail.setCrn(crnDetails[i]);
					this.populateReviewers(reviewerDetails[i], teamDetail);
					this.populateOtherOffice(otherOfficeDetails[i], teamDetail);
					teamDetails.add(teamDetail);
				} else {
					this.logger.debug("not to process for team " + selectedIds.get(i) + " as change in case status.");
				}
			}

		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	public String getAssignedTeamsForUserAndCrn(String crn, String userId) throws CMSException {
		HashMap<String, String> paramMap = new HashMap();
		String teamNames = "";

		try {
			paramMap.put("crn", crn);
			paramMap.put("userId", userId);
			teamNames = this.teamAssignmentDAO.getAssignedTeamsForUserAndCrn(paramMap);
			return teamNames;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public String getTeamType(String teamId) throws CMSException {
		String teamType = "";

		try {
			teamType = this.teamAssignmentDAO.getTeamType(teamId);
			return teamType;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<String> getUserRoleForTeam(String userId, int teamId) throws CMSException {
		HashMap<String, Object> paramMap = new HashMap();
		new ArrayList();

		try {
			paramMap.put("teamId", teamId);
			paramMap.put("userId", userId);
			List<String> userRoles = this.teamAssignmentDAO.getUserRoleForTeam(paramMap);
			return userRoles;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<TeamDetails> getTeamNamesForCRN(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.teamAssignmentDAO.getTeamNamesForCRN(crn);
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer getPrimaryTeamId(String crn) throws CMSException {
		Integer teamId = 0;

		try {
			teamId = this.teamAssignmentDAO.getPrimaryTeamId(crn);
			return teamId;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamDetails> getMyIncomingTaskRole(String userId) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.teamAssignmentDAO.getMyIncomingTaskRole(userId);
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	private boolean isTACompletedForCase(HttpServletRequest request, long pid) throws CMSException {
		boolean isTAComplete = false;
		if (request.getParameter("updateType") != null
				&& request.getParameter("updateType").equalsIgnoreCase("update")) {
			String[] completedWSNames = ResourceLocator.self().getSBMService().getCompletedWSNames(pid,
					SBMUtils.getSession(request));
			if (completedWSNames != null && completedWSNames.length > 0) {
				for (int k = 0; k < completedWSNames.length; ++k) {
					this.logger.debug("completedWSNames[k] :: " + completedWSNames[k]);
					if (completedWSNames[k].equals("Team Assignment Task")) {
						isTAComplete = true;
						break;
					}
				}
			}
		}

		return isTAComplete;
	}

	public List<String> getTeamAssignmentDone(String crn, Session session) throws CMSException {
		new ArrayList();

		try {
			CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
			List<String> teamAssignmentDone = ResourceLocator.self().getSBMService()
					.getTeamAssignmentDone(Long.parseLong(caseDetails.getPid()), session);
			return teamAssignmentDone;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getTeamSubREAnalystCount(String crn) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> subTeamReMapVOs = this.teamAssignmentDAO.getTeamSubREAnalystCount(crn);
			return subTeamReMapVOs;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteTeamDetails(List<TeamDetails> teamIdList) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.teamAssignmentDAO.deleteTeamDetails(teamIdList);
			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void deleteAnalyst(String analystId, int teamId, String updatedBy) throws CMSException {
		String analysts = null;
		String updatedAnalysts = "";

		try {
			analysts = this.teamAssignmentDAO.selectAnalystsForTeam(teamId);
			List<String> analystsArr = StringUtils.commaSeparatedStringToList(analysts);
			Iterator var8 = analystsArr.iterator();

			while (var8.hasNext()) {
				String analyst = (String) var8.next();
				if (!analyst.equals(analystId)) {
					updatedAnalysts = updatedAnalysts + "," + analyst;
				}
			}

			TeamDetails teamDetails = new TeamDetails();
			this.logger.debug("update analysts " + updatedAnalysts.substring(1, updatedAnalysts.length()));
			List<String> updatedAnalystsList = StringUtils
					.commaSeparatedStringToList(updatedAnalysts.substring(1, updatedAnalysts.length()));
			teamDetails.setTeamId(teamId);
			teamDetails.setCsvAnalysts(updatedAnalysts.substring(1, updatedAnalysts.length()));
			teamDetails.setUpdatedBy(updatedBy);
			this.teamAssignmentDAO.deleteAnalyst(teamDetails);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public String updateMainAnalyst(String analystId, int teamId, String updatedBy) throws CMSException {
		String mainAnalyst = null;
		String analysts = null;
		String updatedAnalysts = "";

		try {
			analysts = this.teamAssignmentDAO.selectAnalystsForTeam(teamId);
			List<String> analystsArr = StringUtils.commaSeparatedStringToList(analysts);
			Iterator var9 = analystsArr.iterator();

			while (var9.hasNext()) {
				String analyst = (String) var9.next();
				if (!analyst.equals(analystId)) {
					updatedAnalysts = updatedAnalysts + "," + analyst;
				}
			}

			this.logger.debug("update analysts " + updatedAnalysts.substring(1, updatedAnalysts.length()));
			List<String> updatedAnalystsList = StringUtils
					.commaSeparatedStringToList(updatedAnalysts.substring(1, updatedAnalysts.length()));
			TeamDetails teamDetails = new TeamDetails();
			teamDetails.setTeamId(teamId);
			teamDetails.setCsvAnalysts(updatedAnalysts.substring(1, updatedAnalysts.length()));
			teamDetails.setUpdatedBy(updatedBy);
			mainAnalyst = (String) updatedAnalystsList.get(0);
			teamDetails.setMainAnalyst(mainAnalyst);
			this.teamAssignmentDAO.deleteAnalyst(teamDetails);
			return mainAnalyst;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public String getTeamManager(String teamId) throws CMSException {
		return this.teamAssignmentDAO.getTeamManager(teamId);
	}

	public List<TeamDetails> getTeamDetailsForAnalyst(String userID) throws CMSException {
		return this.teamAssignmentDAO.getTeamDetailsForAnalyst(userID);
	}

	public List<TeamDetails> getBulkAssignedAnalyst(String teamIds) throws CMSException {
		List<Integer> teamIdList = StringUtils.commaSeparatedStringToIntList(teamIds);
		List<TeamDetails> teamDetails = new ArrayList();
		if (teamIdList.size() > 0) {
			teamDetails = this.teamAssignmentDAO.getBulkAssignedAnalyst(teamIdList);
		}

		return (List) teamDetails;
	}

	public List<TeamDetails> getLegacyCaseTeamDetails(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetailsList = this.teamAssignmentDAO.getLegacyCaseTeamDetails(crn);
			return teamDetailsList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamDetails> getLegacyTeamDetails(String crn, String teamType) throws CMSException {
		new ArrayList();
		TeamDetails teamDetailsVO = new TeamDetails();

		try {
			String[] teamTypeId = teamType.split("#");
			this.logger.debug("team type is  " + teamTypeId[0] + "team id is " + Integer.parseInt(teamTypeId[1]));
			teamDetailsVO.setCrn(crn);
			teamDetailsVO.setTeamType(teamTypeId[0]);
			teamDetailsVO.setTeamId(Integer.parseInt(teamTypeId[1]));
			List<TeamDetails> teamDetailsList = this.teamAssignmentDAO.getLegacyTeamDetails(teamDetailsVO);
			return teamDetailsList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public boolean checkTeamDueDates(String rInterim1Date, String rInterim2Date, String rFinalDate, String officeId)
			throws CMSException {
		this.logger.debug("in the checkClientDueDates");
		HashMap<Object, Object> param = null;
		boolean holiDayCheck = false;

		try {
			param = new HashMap();
			param.put(this.OFFICE_ID, officeId);
			if (rFinalDate.trim().length() > 0) {
				this.cal.setTime(this.targetFormat.parse(rFinalDate));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					holiDayCheck = true;
				}

				param.put("holidayDate", this.targetSdf.format(this.targetFormat.parse(rFinalDate)));
				if (this.teamAssignmentDAO.checkTeamDueDates(param)) {
					holiDayCheck = true;
				}
			}

			if (rInterim1Date.trim().length() > 0) {
				this.cal.setTime(this.targetFormat.parse(rInterim1Date));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					holiDayCheck = true;
				}

				param.put("holidayDate", this.targetSdf.format(this.targetFormat.parse(rInterim1Date)));
				if (this.teamAssignmentDAO.checkTeamDueDates(param)) {
					holiDayCheck = true;
				}
			}

			if (rInterim2Date.trim().length() > 0) {
				this.cal.setTime(this.targetFormat.parse(rInterim2Date));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					holiDayCheck = true;
				}

				param.put("holidayDate", this.targetSdf.format(this.targetFormat.parse(rInterim2Date)));
				if (this.teamAssignmentDAO.checkTeamDueDates(param)) {
					holiDayCheck = true;
				}
			}

			this.logger.debug("holiDayCheck:::" + holiDayCheck);
			return holiDayCheck;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public String checkTeamDueDatesTab(List<TeamDetails> teamDetailsList) throws CMSException {
		this.logger.debug("in the checkTeamDueDatesTab");
		String message = "";
		boolean holidayCheck = false;
		boolean weekendCheck = false;

		try {
			Iterator var6 = teamDetailsList.iterator();

			while (var6.hasNext()) {
				TeamDetails teamDetail = (TeamDetails) var6.next();
				this.logger.debug("teamDetail.getOfficeId()::" + teamDetail.getTeamId());
				this.logger.debug("teamDetail.getcInterim1()::" + teamDetail.getDueDate1());
				this.logger.debug("teamDetail.getcInterim2()::" + teamDetail.getDueDate2());
				this.logger.debug("teamDetail.getFinalDueDate()::" + teamDetail.getFinalDueDate());
				this.logger.debug("teamDetail.getTeamType()::" + teamDetail.getTeamType());
				this.logger.debug("teamDetail.getCrn()::" + teamDetail.getCrn());
				if (teamDetail.getFinalDueDate().trim().length() > 0) {
					this.cal.setTime(this.targetFormat.parse(teamDetail.getFinalDueDate()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
						this.logger.debug("weekendCheck for final due date::" + weekendCheck);
					}
				}

				if (teamDetail.getDueDate2() != null && teamDetail.getDueDate2().trim().length() > 0) {
					this.cal.setTime(this.targetFormat.parse(teamDetail.getDueDate2()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
						this.logger.debug("weekendCheck for due date 2::" + weekendCheck);
					}
				}

				if (teamDetail.getDueDate1() != null && teamDetail.getDueDate1().trim().length() > 0) {
					this.cal.setTime(this.targetFormat.parse(teamDetail.getDueDate1()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
						this.logger.debug("weekendCheck for due date 1::" + weekendCheck);
					}
				}

				this.logger.debug("weekendCheck::" + weekendCheck);
				if (weekendCheck) {
					message = message + teamDetail.getCrn() + " (" + teamDetail.getTeamType() + ") weekend <br>";
					this.logger.debug("message for weekend::" + message);
					weekendCheck = false;
				} else {
					int count = this.teamAssignmentDAO
							.checkTeamDueDatesTab(teamDetail.getTeamType(), teamDetail.getCrn(), teamDetail.getOffice(),
									teamDetail.getDueDate1(), teamDetail.getDueDate2(), teamDetail.getFinalDueDate())
							.length();
					if (count > 0) {
						holidayCheck = true;
						message = message + this.teamAssignmentDAO.checkTeamDueDatesTab(teamDetail.getTeamType(),
								teamDetail.getCrn(), teamDetail.getOffice(), teamDetail.getDueDate1(),
								teamDetail.getDueDate2(), teamDetail.getFinalDueDate()) + "holiday";
						this.logger.debug("message for holiday::" + message);
					} else {
						holidayCheck = false;
					}
				}
			}

			return message;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<TeamDetails> getTeamAssignmentWINames(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamAssignmentWINames = this.teamAssignmentDAO.getTeamAssignmentWINames(crn);
			return teamAssignmentWINames;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}