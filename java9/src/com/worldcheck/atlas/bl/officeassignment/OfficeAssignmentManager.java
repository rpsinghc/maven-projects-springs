package com.worldcheck.atlas.bl.officeassignment;

import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IOfficeAssignment;
import com.worldcheck.atlas.dao.officeassignment.OfficeAssignmentDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.TeamOfficeVO;
import com.worldcheck.atlas.vo.TeamTypeVO;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.masters.UserCountryProfileVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

public class OfficeAssignmentManager implements IOfficeAssignment {
	private String CYCLE_NAME = "cycleName";
	private String PERFORMER = "performer";
	private String MESSAGE = "message";
	private String TEAM = "team";
	private String OA_SECTION_JSP = "officeAssignmentSection";
	private String VENDOR_ROLE = "R12";
	private String BI_ROLE = "R6";
	private String WIP_COUNT = "wipCount";
	private String THRESH_HOLD = "threshHold";
	private String SUBJECT_IDS = "subjectIds";
	private String BR_BREAK = "<br>";
	private String BLANK = "blank";
	private String COMMA_HASH_SEPARATOR = ",#";
	private String REDIRECT_TO_SAVE_TASK_URL = "redirect:/bpmportal/atlas/saveTask.do";
	private String REDIRECT_TO_COMPLETE_TASK_URL = "redirect:/bpmportal/atlas/completeTask.do";
	private String COMPLETE = "complete";
	private String SAVE = "save";
	private String UPDATE_TYPE = "updateType";
	private String OFFICE_IDS = "officeIds";
	private String OA_DATE_FORMAT = "dd-MM-yy";
	private String CRN_LIST = "crnList";
	private String TASK_LIST = "taskList";
	private String TEAM_LIST = "teamList";
	private String PRIMARY = "Primary";
	private String SUBJECT_ID = "subjectId";
	private String TEAM_ID = "teamId";
	private String RE_IDS = "reIds";
	private String VENDOR = "Vendor";
	private String INTERNAL = "Internal";
	private String BI = "BI";
	private String TEAM_IDS = "teamIds";
	private String TEAM_SIZE = "teamSize";
	private String IS_OA_DONE = "isOADone";
	private String INTERIM_FLAG = "interimFlag";
	private String BYPASS_INTERIM = "byPassDetail";
	private String SUBJECT_RE_DETAIL = "subjectReDetail";
	private String FDD = "FDD";
	private String IDD1 = "IDD1";
	private String IDD2 = "IDD2";
	private String TEAM_OFFICE_DETAIL = "teamOfficeDetail";
	private String TEAMTYPE_DETAIL = "teamTypeDetail";
	private String YES = "Yes";
	private String SECTION_FLAG = "sectionFlag";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.officeassignment.OfficeAssignmentManager");
	private OfficeAssignmentDAO officeAssignmentDAO = null;
	private PropertyReaderUtil propertyReader = null;
	private SimpleDateFormat targetFormat;
	Calendar cal;
	private String CASE_DUEDATES;
	private static final String DATE_FORMAT_ddMMMyyyy = "dd-MMM-yyyy";
	private SimpleDateFormat targetSdf;

	public OfficeAssignmentManager() {
		this.targetFormat = new SimpleDateFormat(this.OA_DATE_FORMAT);
		this.cal = Calendar.getInstance();
		this.CASE_DUEDATES = "caseDueDates";
		this.targetSdf = new SimpleDateFormat("dd-MMM-yyyy");
	}

	public void setOfficeAssignmentDAO(OfficeAssignmentDAO officeAssignmentDAO) {
		this.officeAssignmentDAO = officeAssignmentDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public List<TeamTypeVO> getTeamTypes() throws CMSException {
		return this.officeAssignmentDAO.getTeamTypes();
	}

	public CaseDetails getCaseDueDates(String crn) throws CMSException {
		return this.officeAssignmentDAO.getCaseDueDates(crn);
	}

	public CaseDetails getCaseDetails(String crn) throws CMSException {
		return this.officeAssignmentDAO.getCaseDetails(crn);
	}

	public CaseDetails getDetailForCase(String crn) throws CMSException {
		return this.officeAssignmentDAO.getDetailForCase(crn);
	}

	public List<SubjectDetails> getREDetails(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("in getREDetails");
		ArrayList updatedSubjectList = new ArrayList();

		try {
			List<SubjectDetails> subjectList = caseDetails.getSubjectList();
			this.logger.debug("subjectList size :: " + subjectList.size());
			if (subjectList.size() > 0) {
				HashMap<Object, Object> paramMap = null;

				for (int i = 0; i < subjectList.size(); ++i) {
					paramMap = new HashMap();
					SubjectDetails subject = (SubjectDetails) subjectList.get(i);
					paramMap.put(this.RE_IDS, subject.getReIds());
					subject.setReList(this.officeAssignmentDAO.getREDetails(paramMap));
					this.logger.debug(" subject.getReList().size() :: " + subject.getReList().size());
					updatedSubjectList.add(subject);
				}
			}

			return updatedSubjectList;
		} catch (UnsupportedOperationException var7) {
			throw new CMSException(this.logger, var7);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (IllegalArgumentException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IndexOutOfBoundsException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public SubjectDetails getSubjectREDetails(SubjectDetails subjectDetailsVO) throws CMSException {
		this.logger.debug("in getSubjectREDetails");

		try {
			HashMap<Object, Object> paramMap = new HashMap();
			paramMap.put(this.RE_IDS, subjectDetailsVO.getReIds());
			subjectDetailsVO.setReList(this.officeAssignmentDAO.getREDetails(paramMap));
			this.logger.debug(" subjectDetailsVO().size() :: " + subjectDetailsVO.getReList().size());
			return subjectDetailsVO;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<SubjectDetails> getCaseSubjectDetails(String crn) throws CMSException {
		this.logger.debug("in getOfficeAssignmentDetails");
		List<SubjectDetails> subjectList = this.officeAssignmentDAO.getCaseSubjectDetails(crn);
		this.logger.debug("subjectList size :: " + subjectList.size());
		return subjectList;
	}

	public List<SubjectDetails> getSubjectCountryREDetails(String crn) throws CMSException {
		new ArrayList();

		try {
			List<SubjectDetails> subjectDetails = this.officeAssignmentDAO.getSubjectCountryREDetails(crn);
			return subjectDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<String> getIsReplicatedSubjects(String crn) throws CMSException {
		new ArrayList();

		try {
			List<String> subjectDetails = this.officeAssignmentDAO.getIsReplicatedSubjects(crn);
			return subjectDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamOfficeVO> getAutoTeamType() throws CMSException {
		new ArrayList();

		try {
			List<TeamOfficeVO> teamDetails = this.officeAssignmentDAO.getAutoTeamType();
			return teamDetails;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getTeamDetails(String crn) throws CMSException {
		this.logger.debug("in getTeamDetails");
		List<TeamDetails> teamDetailList = this.officeAssignmentDAO.getTeamDetails(crn);
		this.logger.debug("teamDetailList size :: " + teamDetailList.size());
		return teamDetailList;
	}

	public boolean isBiREPresent(List<SubjectDetails> subjectList) throws CMSException {
		this.logger.debug("in isBiREPresent");
		this.logger.debug("subjectList size :: " + subjectList.size());
		boolean biREPresent = false;

		try {
			if (subjectList.size() > 0) {
				for (int i = 0; i < subjectList.size(); ++i) {
					SubjectDetails subject = (SubjectDetails) subjectList.get(i);
					List<ResearchElementMasterVO> reList = subject.getReList();

					for (int j = 0; j < reList.size(); ++j) {
						ResearchElementMasterVO vo = (ResearchElementMasterVO) reList.get(j);
						if (null != vo.getBiTeam() && vo.getBiTeam().equalsIgnoreCase(this.YES)) {
							biREPresent = true;
							this.logger.debug("breaking from inner :: " + biREPresent);
							break;
						}
					}

					if (biREPresent) {
						this.logger.debug("breaking from outer :: " + biREPresent);
						break;
					}
				}
			}

			this.logger.debug("biREPresent :: " + biREPresent);
			return biREPresent;
		} catch (IndexOutOfBoundsException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public boolean isOfficeAssignmentDone(String crn) throws CMSException {
		int count = this.officeAssignmentDAO.isOfficeAssignmentDone(crn);
		return count > 0;
	}

	public ModelAndView saveTeamDetails(HttpServletRequest request) throws CMSException {
		this.logger.debug("in saveTeamDetails");
		ModelAndView modelAndView = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("logged in user :: " + userBean.getUserName() + " coming from :: "
					+ request.getParameter(this.SECTION_FLAG));
			this.logger.debug("teamTypeDetail :: " + request.getParameter(this.TEAMTYPE_DETAIL));
			this.logger.debug("teamOfficeDetail :: " + request.getParameter(this.TEAM_OFFICE_DETAIL));
			this.logger.debug("IDD1 :: " + request.getParameter(this.IDD1) + " :: IDD2 :: "
					+ request.getParameter(this.IDD2) + " :: FDD :: " + request.getParameter(this.FDD));
			this.logger.debug("byPassDetail :: " + request.getParameter(this.BYPASS_INTERIM) + " :: interimFlag : "
					+ request.getParameter(this.INTERIM_FLAG));
			this.logger.debug("subjectReDetail :: " + request.getParameter(this.SUBJECT_RE_DETAIL));
			this.logger.debug(
					"crn :: " + request.getParameter("crn") + " :: teamSize :: " + request.getParameter(this.TEAM_SIZE)
							+ " :: updateType :: " + request.getParameter(this.UPDATE_TYPE));
			this.logger.debug("isOADone :: " + request.getParameter(this.IS_OA_DONE) + " :: teamIds :: "
					+ request.getParameter(this.TEAM_IDS) + " :: officeIds :: "
					+ request.getParameter(this.OFFICE_IDS));
			this.logger.debug("clientDueDates:::" + request.getParameter("cinterim1") + "CIDD2:::"
					+ request.getParameter("cinterim2") + "CFDD::" + request.getParameter("cfinalDate"));
			boolean isOADone = Boolean.parseBoolean(request.getParameter(this.IS_OA_DONE));
			int teamSize = 0;
			CaseDetails oldCaseInfo = ResourceLocator.self().getOfficeAssignmentService()
					.getDetailForCase(request.getParameter("crn"));
			String oldCaseManagerID = oldCaseInfo.getCaseMgrId();
			String newCaseManagerID = request.getParameter("caseManagerId");
			String caseStatus = "";
			if (null != request.getParameter("crn") && request.getParameter("crn").trim().length() > 0) {
				caseStatus = this.getCurrentCaseStatus(request.getParameter("crn"));
			}

			if (null == caseStatus
					|| !caseStatus.equalsIgnoreCase("Cancelled") && !caseStatus.equalsIgnoreCase("On Hold")) {
				if (null == request.getParameter(this.SECTION_FLAG)) {
					boolean wiExists = ResourceLocator.self().getSBMService().wiExistsForUser(
							request.getParameter("workItem"), SBMUtils.getSession(request), userBean.getUserName());
					this.logger.debug("wiExists for :: " + request.getParameter("workItem") + " :: is :: " + wiExists);
					if (!wiExists) {
						this.logger
								.debug("redirecting to taskStatusErrorPage.jsp as task might have been pulled back.");
						return new ModelAndView("redirect:/bpmportal/atlas/taskStatusErrorPage.jsp");
					}
				}

				String interimFlag = request.getParameter(this.INTERIM_FLAG);
				boolean interim1Flag = Boolean.parseBoolean(interimFlag.split("#")[0]);
				boolean interim2Flag = Boolean.parseBoolean(interimFlag.split("#")[1]);
				this.logger.debug("interim1Flag :: " + interim1Flag + " :: interim2Flag :: " + interim2Flag
						+ " :: isOADone :: " + isOADone);
				CaseDetails newCaseDetail = new CaseDetails();
				CaseDetails oldCaseDetail = new CaseDetails();
				String oldClientInterim1Date = "";
				String oldClientInterim2Date = "";
				Date oldClientInterim2date;
				if (null != oldCaseInfo.getcInterim1()) {
					oldClientInterim1Date = oldCaseInfo.getcInterim1().toString();
					oldClientInterim2date = this.targetFormat.parse(oldClientInterim1Date);
					oldCaseDetail.setcInterim1(new java.sql.Date(oldClientInterim2date.getTime()));
				}

				if (null != oldCaseInfo.getcInterim2()) {
					oldClientInterim2Date = oldCaseInfo.getcInterim2().toString();
					oldClientInterim2date = this.targetFormat.parse(oldClientInterim2Date);
					oldCaseDetail.setcInterim2(new java.sql.Date(oldClientInterim2date.getTime()));
				}

				String oldClientFinalDate = oldCaseInfo.getFinalDueDate().toString();
				Date oldClientFinaldate = this.targetFormat.parse(oldClientFinalDate);
				oldCaseDetail.setClientFinalDate(new java.sql.Date(oldClientFinaldate.getTime()));
				String newClientInterim1Date = "";
				String newClientInterim2Date = "";
				Date newClientInterim2date;
				if (null != request.getParameter("cinterim1")
						&& request.getParameter("cinterim1").trim().length() > 0) {
					newClientInterim1Date = request.getParameter("cinterim1").toString();
					newClientInterim2date = this.targetFormat.parse(newClientInterim1Date);
					newCaseDetail.setcInterim1(new java.sql.Date(newClientInterim2date.getTime()));
				}

				if (null != request.getParameter("cinterim2")
						&& request.getParameter("cinterim2").trim().length() > 0) {
					newClientInterim2Date = request.getParameter("cinterim2").toString();
					newClientInterim2date = this.targetFormat.parse(newClientInterim2Date);
					newCaseDetail.setcInterim2(new java.sql.Date(newClientInterim2date.getTime()));
				}

				String newClientFinalDate = request.getParameter("cfinalDate").toString();
				Date newClientFinaldate = this.targetFormat.parse(newClientFinalDate);
				newCaseDetail.setClientFinalDate(new java.sql.Date(newClientFinaldate.getTime()));
				if (!newClientFinalDate.equals(oldClientFinalDate)
						|| !newClientInterim1Date.equals(oldClientInterim1Date)
						|| !newClientInterim2Date.equals(oldClientInterim2Date)) {
					ResourceLocator.self().getOfficeAssignmentService().updateClientDueDates(
							request.getParameter("crn"), newClientFinalDate, newClientInterim1Date,
							newClientInterim2Date);
				}

				newCaseDetail.setCaseMgrId(newCaseManagerID);
				oldCaseDetail.setCaseMgrId(oldCaseManagerID);
				if (!newCaseManagerID.equals(oldCaseManagerID)) {
					ResourceLocator.self().getOfficeAssignmentService().updateCaseManager(request.getParameter("crn"),
							newCaseManagerID, userBean.getUserName());
				}

				CaseDetails newCaseInfo = ResourceLocator.self().getOfficeAssignmentService()
						.getDetailForCase(request.getParameter("crn"));
				newCaseDetail.setCaseManagerName(newCaseInfo.getCaseManagerName());
				oldCaseDetail.setCaseManagerName(oldCaseInfo.getCaseManagerName());
				List<TeamDetails> teamDetails = new ArrayList();
				TeamDetails allTeamDetails = this.populateAllTeamDetails(request);
				HashMap<String, String> teamTypeMap = this.getTeamTypeMap();
				if (null != request.getParameter(this.TEAM_SIZE)
						&& request.getParameter(this.TEAM_SIZE).trim().length() > 0) {
					teamSize = Integer.parseInt(request.getParameter(this.TEAM_SIZE));
				}

				newCaseDetail.setCrn(allTeamDetails.getCrn());
				oldCaseDetail.setCrn(allTeamDetails.getCrn());
				List<TeamDetails> rhAndOfficeDetails = this
						.getResearchHeadsForOffices(request.getParameter(this.OFFICE_IDS));
				HashMap<String, String> rhMap = new HashMap();
				HashMap<String, String> officeMap = new HashMap();
				HashMap<String, String> rhDetailMap = new HashMap();

				for (int k = 0; k < rhAndOfficeDetails.size(); ++k) {
					TeamDetails temp = (TeamDetails) rhAndOfficeDetails.get(k);
					rhMap.put(temp.getOffice(), temp.getResearchHead());
					officeMap.put(temp.getOffice(), temp.getOfficeName());
					rhDetailMap.put(temp.getResearchHead(), temp.getRhFullName());
				}

				List<TeamDetails> managerDetails = this
						.getManagerDetails(request.getParameter(this.TEAM_OFFICE_DETAIL));
				HashMap<String, String> managerMap = new HashMap();

				for (int k = 0; k < managerDetails.size(); ++k) {
					TeamDetails temp = (TeamDetails) managerDetails.get(k);
					managerMap.put(temp.getManagerName(), temp.getManagerFullName());
				}

				HashMap<String, String[]> tempTeamDetail = this.prepareTeamDetailMap(interim1Flag, interim2Flag,
						allTeamDetails);
				String[] teamIds = new String[teamSize];
				List<Integer> hashCodeList = new ArrayList();
				List<Integer> hashCodeListWithJlp = new ArrayList();
				HashMap<String, Integer> mapForAnaCount = null;
				List<TeamDetails> oldTeamDetails = null;
				String primaryRInterim2;
				if (isOADone) {
					if (null != request.getParameter(this.TEAM_IDS)
							&& request.getParameter(this.TEAM_IDS).trim().length() > 0) {
						teamIds = request.getParameter(this.TEAM_IDS).split(",");
					}

					this.logger.debug("teamIds :: " + teamIds);
					oldTeamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(oldCaseDetail.getCrn());
					this.logger.debug("oldTeamDetails size:: " + oldTeamDetails.size());
					oldCaseDetail.setTeamList(oldTeamDetails);
					this.generateHashCodeList(hashCodeList, hashCodeListWithJlp, oldTeamDetails);
					mapForAnaCount = new HashMap();
					List<SubTeamReMapVO> reAnalystCountInfoList = ResourceLocator.self().getTeamAssignmentService()
							.getTeamSubREAnalystCount(oldCaseDetail.getCrn());
					if (reAnalystCountInfoList.size() > 0) {
						SubTeamReMapVO vo = null;
						primaryRInterim2 = null;

						for (int i = 0; i < reAnalystCountInfoList.size(); ++i) {
							vo = (SubTeamReMapVO) reAnalystCountInfoList.get(i);
							primaryRInterim2 = vo.getTeamId() + "#" + vo.getSubjectId() + "#" + vo.getReId();
							mapForAnaCount.put(primaryRInterim2, vo.getAnalystCount());
						}
					}
				}

				this.logger.debug("hashCodeList size :: " + hashCodeList.size());
				String updatedTeamIds = "";
				String primaryRInterim1 = null;
				primaryRInterim2 = null;
				String primaryRFinal = null;
				int teamId = 0;

				int k;
				for (int i = 0; i < teamSize; ++i) {
					TeamDetails teamDetail = new TeamDetails();
					this.populateTeamDetails(interim1Flag, interim2Flag, tempTeamDetail, teamDetail, i);
					teamDetail.setCrn(allTeamDetails.getCrn());
					teamDetail.setUpdatedBy(userBean.getUserName());
					teamDetail.setTeamType((String) teamTypeMap.get(teamDetail.getTeamTypeId()));
					if (isOADone) {
						this.logger.debug("teamIds[i] :: " + teamIds[i]);
						if (null != teamIds[i] && Integer.parseInt(teamIds[i]) != 0) {
							teamId = Integer.parseInt(teamIds[i]);
							this.logger.debug("calling update for teamId :: " + teamId);
							teamDetail.setTeamId(teamId);
							if (null == teamDetail.getTeamType() || teamDetail.getTeamType().trim().length() <= 0
									|| !teamDetail.getTeamType().contains(this.BI)
											&& !teamDetail.getTeamType().contains(this.VENDOR)) {
								this.logger.debug(
										"office id :: " + ((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setOffice(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setResearchHead((String) rhMap
										.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
								teamDetail.setOfficeName((String) officeMap
										.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
								teamDetail.setRhFullName((String) rhDetailMap.get(teamDetail.getResearchHead()));
								this.officeAssignmentDAO.updatePSTOfficeDetails(teamDetail);
							} else {
								this.logger.debug("manager name :: "
										+ ((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setManagerName(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setManagerFullName((String) managerMap
										.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
								this.officeAssignmentDAO.updateBIVTOfficeDetails(teamDetail);
							}

							for (k = 0; k < oldTeamDetails.size(); ++k) {
								TeamDetails teamVo = (TeamDetails) oldTeamDetails.get(k);
								this.logger.debug("Old Team id :: " + teamVo.getTeamId() + " Old Office name ::"
										+ teamVo.getOffice() + " New team ID: " + teamDetail.getTeamId()
										+ " New Office ID :" + teamDetail.getOffice());
								if (!teamVo.getTeamType().contains(this.BI)
										&& !teamVo.getTeamType().contains(this.VENDOR)
										&& !teamDetail.getTeamType().contains(this.BI)
										&& !teamDetail.getTeamType().contains(this.VENDOR)
										&& teamVo.getTeamId() == teamDetail.getTeamId()
										&& teamVo.getOffice().equals(teamDetail.getOffice())) {
									teamDetail.setMainAnalyst(teamVo.getMainAnalyst());
									break;
								}
							}

							teamDetail = this.updateReDetailForOffice(teamDetail, hashCodeList, hashCodeListWithJlp,
									mapForAnaCount);
						} else if (null != teamIds[i] && Integer.parseInt(teamIds[i]) == 0) {
							if (null == teamDetail.getTeamType() || teamDetail.getTeamType().trim().length() <= 0
									|| !teamDetail.getTeamType().contains(this.BI)
											&& !teamDetail.getTeamType().contains(this.VENDOR)) {
								this.logger.debug(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setOffice(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setResearchHead((String) rhMap
										.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
								teamDetail.setOfficeName((String) officeMap
										.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
								teamDetail.setRhFullName((String) rhDetailMap.get(teamDetail.getResearchHead()));
							} else {
								this.logger.debug(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setManagerName(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
								teamDetail.setManagerFullName((String) managerMap
										.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
							}

							teamId = this.officeAssignmentDAO.addTeamDetails(teamDetail);
							this.logger.debug("teamId :: " + teamId);
							teamDetail.setTeamId(teamId);
							teamDetail = this.addReDetailForOffice(teamDetail);
						}
					} else {
						if (null == teamDetail.getTeamType() || teamDetail.getTeamType().trim().length() <= 0
								|| !teamDetail.getTeamType().contains(this.BI)
										&& !teamDetail.getTeamType().contains(this.VENDOR)) {
							this.logger.debug(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
							teamDetail.setOffice(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
							teamDetail.setResearchHead(
									(String) rhMap.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
							teamDetail.setOfficeName((String) officeMap
									.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
							teamDetail.setRhFullName((String) rhDetailMap.get(teamDetail.getResearchHead()));
						} else {
							this.logger.debug(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
							teamDetail.setManagerName(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]);
							teamDetail.setManagerFullName((String) managerMap
									.get(((String[]) tempTeamDetail.get(this.TEAM_OFFICE_DETAIL))[i]));
						}

						teamId = this.officeAssignmentDAO.addTeamDetails(teamDetail);
						this.logger.debug("teamId :: " + teamId);
						teamDetail.setTeamId(teamId);
						teamDetail = this.addReDetailForOffice(teamDetail);
					}

					if (teamDetail.getDueDate1() != null && teamDetail.getDueDate1().length() != 0
							&& teamDetail.getTeamType().contains("Primary")) {
						primaryRInterim1 = teamDetail.getDueDate1();
					}

					if (teamDetail.getDueDate2() != null && teamDetail.getDueDate2().length() != 0
							&& teamDetail.getTeamType().contains("Primary")) {
						primaryRInterim2 = teamDetail.getDueDate2();
					}

					if (teamDetail.getFinalDueDate() != null && teamDetail.getFinalDueDate().length() != 0
							&& teamDetail.getTeamType().contains("Primary")) {
						primaryRFinal = teamDetail.getFinalDueDate();
					}

					if (i == 0) {
						updatedTeamIds = updatedTeamIds + teamId;
					} else {
						updatedTeamIds = updatedTeamIds + "," + teamId;
					}

					this.logger.debug("updatedTeamIds : " + updatedTeamIds);
					teamDetails.add(teamDetail);
				}

				HashMap<Object, Object> param = new HashMap();
				param.put("crn", newCaseDetail.getCrn());
				param.put(this.TEAM_IDS, updatedTeamIds);
				k = this.officeAssignmentDAO.deleteTeams(param);
				this.logger.debug("deletedTeamCount :: " + k);
				newCaseDetail.setTeamList(teamDetails);
				this.logger.debug("workitem in OA :: " + request.getParameter("workItem"));
				newCaseDetail.setWorkitemName(request.getParameter("workItem"));
				oldCaseDetail.setWorkitemName(request.getParameter("workItem"));
				newCaseDetail.setUpdatedBy(userBean.getUserName());
				oldCaseDetail.setUpdatedBy(userBean.getUserName());
				long pid = this.getPIDForCRN(request.getParameter("crn"));
				this.logger.debug("pid :: " + pid);
				boolean isOAComplete = this.isOACompletedForCase(request, pid);
				this.logger.debug("isOAComplete :: " + isOAComplete);
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setCRN(newCaseDetail.getCrn());
				caseHistory.setPid(String.valueOf(pid));
				caseHistory.setProcessCycle("");
				if (null != request.getSession().getAttribute("loginLevel")) {
					caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
					newCaseDetail.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
				} else {
					caseHistory.setPerformer(userBean.getUserName());
					newCaseDetail.setCaseHistoryPerformer(userBean.getUserName());
				}

				if (!isOAComplete) {
					caseHistory.setTaskName("Office Assignment Task");
					caseHistory.setTaskStatus("In Progress");
				} else {
					caseHistory.setTaskName("");
					caseHistory.setTaskStatus("");
				}

				ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetail, newCaseDetail, caseHistory,
						"Office");
				this.logger.debug("update_type::" + request.getParameter(this.UPDATE_TYPE));
				if (null != request.getParameter(this.UPDATE_TYPE)
						&& request.getParameter(this.UPDATE_TYPE).trim().length() > 0) {
					HashMap dsMap;
					String rInterim1;
					if (isOAComplete && request.getParameter(this.UPDATE_TYPE).equalsIgnoreCase(this.SAVE)) {
						dsMap = (HashMap) ResourceLocator.self().getSBMService().getDataslotValue(pid, "customDSMap",
								SBMUtils.getSession(request));
						this.logger.debug("customDSMap is::" + dsMap);
						rInterim1 = null;
						HashMap<String, Object> dsMap = new HashMap();
						if (null != newClientInterim1Date && !newClientInterim1Date.equalsIgnoreCase("")) {
							dsMap.put("CInterim1",
									this.targetSdf.format(this.targetFormat.parse(newClientInterim1Date)));
						}

						if (null != newClientInterim2Date && !newClientInterim2Date.equalsIgnoreCase("")) {
							dsMap.put("CInterim2",
									this.targetSdf.format(this.targetFormat.parse(newClientInterim2Date)));
						}

						dsMap.put("CFinal", this.targetSdf.format(this.targetFormat.parse(newClientFinalDate)));
						if (dsMap != null) {
							CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) dsMap.get("CycleTeamMapping");
							this.logger.debug("cycleTeamMapping is::" + cycleTeamMapping);
							String currentCycle = cycleTeamMapping.getCurrentCycle();
							this.logger.debug("currentCycle is " + currentCycle);
							Map<String, CycleInfo> cycleInformation = cycleTeamMapping.getCycleInformation();
							this.logger.debug("cycleInformation is::" + cycleInformation);
							ResourceLocator.self().getFlowService().updateDataslotsForCase(SBMUtils.getSession(request),
									cycleInformation, dsMap);
						}

						this.logger.debug("In updateDataSlots::" + dsMap);
						ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid,
								dsMap);
						ResourceLocator.self().getFlowService().updateDSAndPushFlow(SBMUtils.getSession(request),
								oldCaseDetail, newCaseDetail, "Office");
						modelAndView = new ModelAndView(this.OA_SECTION_JSP);
						modelAndView.addObject("crn", oldCaseDetail.getCrn());
					} else {
						String rInterim2;
						String rFinal;
						boolean datesUpdated;
						if (!isOAComplete && request.getParameter(this.UPDATE_TYPE).equalsIgnoreCase(this.SAVE)
								&& null != request.getParameter(this.SECTION_FLAG)
								&& request.getParameter(this.SECTION_FLAG).trim().length() > 0) {
							dsMap = new HashMap();
							if (null != newClientInterim1Date && !newClientInterim1Date.equalsIgnoreCase("")) {
								dsMap.put("CInterim1",
										this.targetSdf.format(this.targetFormat.parse(newClientInterim1Date)));
							}

							if (null != newClientInterim2Date && !newClientInterim2Date.equalsIgnoreCase("")) {
								dsMap.put("CInterim2",
										this.targetSdf.format(this.targetFormat.parse(newClientInterim2Date)));
							}

							dsMap.put("CFinal", this.targetSdf.format(this.targetFormat.parse(newClientFinalDate)));
							rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"RInterim1", SBMUtils.getSession(request));
							rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"RInterim2", SBMUtils.getSession(request));
							rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal",
									SBMUtils.getSession(request));
							if (primaryRInterim1 != null) {
								dsMap.put("RInterim1",
										this.targetSdf.format(this.targetFormat.parse(primaryRInterim1)));
								primaryRInterim1 = this.targetSdf.format(this.targetFormat.parse(primaryRInterim1));
							}

							if (primaryRInterim2 != null) {
								dsMap.put("RInterim2",
										this.targetSdf.format(this.targetFormat.parse(primaryRInterim2)));
								primaryRInterim2 = this.targetSdf.format(this.targetFormat.parse(primaryRInterim2));
							}

							if (primaryRFinal != null) {
								dsMap.put("RFinal", this.targetSdf.format(this.targetFormat.parse(primaryRFinal)));
								primaryRFinal = this.targetSdf.format(this.targetFormat.parse(primaryRFinal));
							}

							datesUpdated = false;
							if (rInterim1 != null && !rInterim1.equals(primaryRInterim1)
									|| rInterim2 != null && !rInterim2.equals(primaryRInterim2)
									|| rFinal != null && !rFinal.equals(primaryRInterim2)) {
								ResourceLocator.self().getCaseDetailService().updateCaseResearchDueDates(
										newCaseDetail.getCrn(), primaryRInterim1, primaryRInterim2, primaryRFinal,
										newCaseDetail.getUpdatedBy());
								datesUpdated = true;
							}

							this.logger.debug("case research datesUpdated flag::" + datesUpdated);
							ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid,
									dsMap);
							modelAndView = new ModelAndView(this.OA_SECTION_JSP);
							modelAndView.addObject("crn", newCaseDetail.getCrn());
						} else if (!isOAComplete && request.getParameter(this.UPDATE_TYPE).equalsIgnoreCase(this.SAVE)
								&& null == request.getParameter(this.SECTION_FLAG)) {
							dsMap = new HashMap();
							if (null != newClientInterim1Date && !newClientInterim1Date.equalsIgnoreCase("")) {
								dsMap.put("CInterim1",
										this.targetSdf.format(this.targetFormat.parse(newClientInterim1Date)));
							}

							if (null != newClientInterim2Date && !newClientInterim2Date.equalsIgnoreCase("")) {
								dsMap.put("CInterim2",
										this.targetSdf.format(this.targetFormat.parse(newClientInterim2Date)));
							}

							dsMap.put("CFinal", this.targetSdf.format(this.targetFormat.parse(newClientFinalDate)));
							rInterim1 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"RInterim1", SBMUtils.getSession(request));
							rInterim2 = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
									"RInterim2", SBMUtils.getSession(request));
							rFinal = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "RFinal",
									SBMUtils.getSession(request));
							if (primaryRInterim1 != null) {
								dsMap.put("RInterim1",
										this.targetSdf.format(this.targetFormat.parse(primaryRInterim1)));
								primaryRInterim1 = this.targetSdf.format(this.targetFormat.parse(primaryRInterim1));
							}

							if (primaryRInterim2 != null) {
								dsMap.put("RInterim2",
										this.targetSdf.format(this.targetFormat.parse(primaryRInterim2)));
								primaryRInterim2 = this.targetSdf.format(this.targetFormat.parse(primaryRInterim2));
							}

							if (primaryRFinal != null) {
								dsMap.put("RFinal", this.targetSdf.format(this.targetFormat.parse(primaryRFinal)));
								primaryRFinal = this.targetSdf.format(this.targetFormat.parse(primaryRFinal));
							}

							datesUpdated = false;
							if (rInterim1 != null && !rInterim1.equals(primaryRInterim1)
									|| rInterim2 != null && !rInterim2.equals(primaryRInterim2)
									|| rFinal != null && !rFinal.equals(primaryRInterim2)) {
								ResourceLocator.self().getCaseDetailService().updateCaseResearchDueDates(
										newCaseDetail.getCrn(), primaryRInterim1, primaryRInterim2, primaryRFinal,
										newCaseDetail.getUpdatedBy());
								datesUpdated = true;
							}

							this.logger.debug("case research datesUpdated flag::" + datesUpdated);
							ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid,
									dsMap);
							dsMap.put("taskPerformer", request.getParameter("taskPerformer"));
							dsMap.put("CaseManager", newCaseManagerID);
							ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid,
									dsMap);
							modelAndView = new ModelAndView(this.REDIRECT_TO_SAVE_TASK_URL);
							modelAndView.addObject("workItem", request.getParameter("workItem"));
							TimeTrackerVO timeTracker = new TimeTrackerVO();
							timeTracker.setCrn(oldCaseDetail.getCrn());
							timeTracker.setUserName(oldCaseManagerID);
							timeTracker.setTaskName("Office Assignment Task");
							timeTracker.setTaskPid(String.valueOf(pid));
							timeTracker.setUpdatedBy(userBean.getUserName());
							this.logger.debug("Stopping time tracker and logging case history for crn :: "
									+ oldCaseDetail.getCrn() + " performer :: " + oldCaseManagerID + " taskName :: "
									+ "Office Assignment Task" + " :: taskPid " + pid);
							ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTracker);
							ResourceLocator.self().getSBMService().reassignTask(pid, newCaseManagerID,
									"Office Assignment Task", SBMUtils.getSession(request));
						} else if (request.getParameter(this.UPDATE_TYPE).equalsIgnoreCase(this.COMPLETE)) {
							ResourceLocator.self().getFlowService().updateDS(SBMUtils.getSession(request),
									newCaseDetail, "Office");
							modelAndView = new ModelAndView(this.REDIRECT_TO_COMPLETE_TASK_URL);
							dsMap = new HashMap();
							if (null != newClientInterim1Date && !newClientInterim1Date.equalsIgnoreCase("")) {
								dsMap.put("CInterim1",
										this.targetSdf.format(this.targetFormat.parse(newClientInterim1Date)));
							}

							if (null != newClientInterim2Date && !newClientInterim2Date.equalsIgnoreCase("")) {
								dsMap.put("CInterim2",
										this.targetSdf.format(this.targetFormat.parse(newClientInterim2Date)));
							}

							dsMap.put("CFinal", this.targetSdf.format(this.targetFormat.parse(newClientFinalDate)));
							dsMap.put("taskPerformer", request.getParameter("taskPerformer"));
							dsMap.put("CaseManager", newCaseManagerID);
							ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid,
									dsMap);
							modelAndView.addObject("workItem", request.getParameter("workItem"));
						}
					}
				}

				return modelAndView;
			} else {
				this.logger.debug("redirecting to caseStatusErrorPage.jsp as per change in case status.");
				return null == request.getParameter(this.SECTION_FLAG)
						? new ModelAndView("redirect:/bpmportal/atlas/caseStatusErrorPage.jsp")
						: new ModelAndView("redirect:/bpmportal/myhome/sectionCaseStatusError.jsp");
			}
		} catch (PatternSyntaxException var57) {
			throw new CMSException(this.logger, var57);
		} catch (Exception var58) {
			throw new CMSException(this.logger, var58);
		}
	}

	public TaskColorVO getColorDetails(String crn, String task) throws CMSException {
		HashMap<String, String> param = new HashMap();
		new TaskColorVO();

		try {
			param.put("crn", crn);
			param.put("taskName", task);
			TaskColorVO colorVO = this.officeAssignmentDAO.getColorDetails(param);
			return colorVO;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private HashMap<String, String[]> prepareTeamDetailMap(boolean interim1Flag, boolean interim2Flag,
			TeamDetails allTeamDetails) {
		HashMap<String, String[]> tempTeamDetail = new HashMap();
		tempTeamDetail.put(this.BYPASS_INTERIM, allTeamDetails.getByPassInterim().split(","));
		tempTeamDetail.put(this.TEAMTYPE_DETAIL, allTeamDetails.getTeamTypeId().split(","));
		tempTeamDetail.put(this.TEAM_OFFICE_DETAIL, allTeamDetails.getOffice().split(","));
		if (interim1Flag) {
			tempTeamDetail.put(this.IDD1, allTeamDetails.getDueDate1().split(","));
		}

		if (interim2Flag) {
			tempTeamDetail.put(this.IDD2, allTeamDetails.getDueDate2().split(","));
		}

		tempTeamDetail.put(this.FDD, allTeamDetails.getFinalDueDate().split(","));
		tempTeamDetail.put(this.SUBJECT_RE_DETAIL, allTeamDetails.getReIds().split(this.COMMA_HASH_SEPARATOR));
		return tempTeamDetail;
	}

	private void populateTeamDetails(boolean interim1Flag, boolean interim2Flag,
			HashMap<String, String[]> tempTeamDetail, TeamDetails teamDetail, int index) {
		teamDetail.setByPassInterim(((String[]) tempTeamDetail.get(this.BYPASS_INTERIM))[index]);
		if (null != teamDetail.getByPassInterim() && teamDetail.getByPassInterim().trim().length() > 0
				&& teamDetail.getByPassInterim().equals("true")) {
			teamDetail.setByPassInterim("1");
			teamDetail.setDueDate1("");
			teamDetail.setDueDate2("");
		} else {
			teamDetail.setByPassInterim("0");
			if (interim1Flag) {
				teamDetail.setDueDate1(((String[]) tempTeamDetail.get(this.IDD1))[index]);
			} else {
				teamDetail.setDueDate1("");
			}

			if (interim2Flag) {
				if (((String[]) tempTeamDetail.get(this.IDD2))[index].equalsIgnoreCase(this.BLANK)) {
					teamDetail.setDueDate2("");
				} else {
					teamDetail.setDueDate2(((String[]) tempTeamDetail.get(this.IDD2))[index]);
				}
			} else {
				teamDetail.setDueDate2("");
			}
		}

		teamDetail.setFinalDueDate(((String[]) tempTeamDetail.get(this.FDD))[index]);
		teamDetail.setTeamTypeId(((String[]) tempTeamDetail.get(this.TEAMTYPE_DETAIL))[index]);
		teamDetail.setReIds(((String[]) tempTeamDetail.get(this.SUBJECT_RE_DETAIL))[index]);
	}

	private HashMap<String, String> getTeamTypeMap() throws CMSException {
		List<TeamTypeVO> teamTypeList = this.getTeamTypes();
		HashMap<String, String> teamTypeMap = new HashMap();

		for (int j = 0; j < teamTypeList.size(); ++j) {
			TeamTypeVO vo = (TeamTypeVO) teamTypeList.get(j);
			teamTypeMap.put(vo.getTeamTypeId(), vo.getTeamType());
		}

		return teamTypeMap;
	}

	public List<TeamDetails> getResearchHeadsForOffices(String officeIds) throws CMSException {
		this.logger.debug("officeIds :: " + officeIds);
		List<TeamDetails> rhList = null;
		if (null != officeIds && officeIds.trim().length() > 0) {
			rhList = this.officeAssignmentDAO.getResearchHeadsForOffices(officeIds);
		} else {
			rhList = this.officeAssignmentDAO.getAllResearchHeads();
		}

		this.logger.debug("rhList size :: " + rhList.size());
		return rhList;
	}

	public List<TeamDetails> getManagerDetails(String managerIds) throws CMSException {
		HashMap<String, Object> param = new HashMap();
		param.put("managerIds", StringUtils.commaSeparatedStringToList(managerIds));
		return this.officeAssignmentDAO.getManagerDetails(param);
	}

	public String getOfficeWithoutRH(String officeIds) throws CMSException {
		String offices = "";
		List<TeamDetails> inactiveOffices = this.officeAssignmentDAO.getInactiveOffices(officeIds);

		for (int k = 0; k < inactiveOffices.size(); ++k) {
			offices = offices + ((TeamDetails) inactiveOffices.get(k)).getOfficeName() + this.BR_BREAK;
		}

		this.logger.debug("inactive offices are :: " + offices);
		if (offices.trim().length() > 0) {
			offices = "Inactive##" + offices;
		}

		if (null == inactiveOffices || inactiveOffices.size() <= 0) {
			List<TeamDetails> rhList = this.getResearchHeadsForOffices(officeIds);

			for (int k = 0; k < rhList.size(); ++k) {
				TeamDetails temp = (TeamDetails) rhList.get(k);
				if (null == temp.getResearchHead() || temp.getResearchHead().trim().length() == 0) {
					offices = offices + temp.getOfficeName() + this.BR_BREAK;
				}
			}
		}

		if (offices.trim().length() > 0) {
			offices = offices.substring(0, offices.length() - this.BR_BREAK.length());
		}

		this.logger.debug("inactive offices or offices without RH are :: " + offices);
		return offices;
	}

	private HashMap<String, String> getRHAndOfficeDetails(List<TeamDetails> rhList) throws CMSException {
		HashMap<String, String> rhMap = new HashMap();

		for (int k = 0; k < rhList.size(); ++k) {
			TeamDetails temp = (TeamDetails) rhList.get(k);
			rhMap.put(temp.getOffice(), temp.getResearchHead());
		}

		return rhMap;
	}

	private void generateHashCodeList(List<Integer> hashCodeList, List<Integer> hashCodeListWithJlp,
			List<TeamDetails> oldTeamDetails) {
		List<SubTeamReMapVO> teamSubjectREDetails = null;
		SubTeamReMapVO vo = null;

		for (int k = 0; k < oldTeamDetails.size(); ++k) {
			TeamDetails td = (TeamDetails) oldTeamDetails.get(k);
			teamSubjectREDetails = td.getTeamSubjectREDetails();

			for (int j = 0; j < teamSubjectREDetails.size(); ++j) {
				vo = (SubTeamReMapVO) teamSubjectREDetails.get(j);
				if (null == td.getTeamType() || td.getTeamType().trim().length() <= 0
						|| !td.getTeamType().contains(this.BI) && !td.getTeamType().contains(this.VENDOR)) {
					hashCodeList.add(vo.hashCodeForPST());
				} else {
					hashCodeList.add(vo.hashCode());
				}

				hashCodeListWithJlp.add(vo.hashCodeWithJLP());
			}
		}

	}

	private boolean isOACompletedForCase(HttpServletRequest request, long pid) throws CMSException {
		boolean isOAComplete = false;
		if (null != request.getParameter(this.UPDATE_TYPE)
				&& request.getParameter(this.UPDATE_TYPE).equalsIgnoreCase(this.SAVE)) {
			boolean isAutoOA = (Boolean) ResourceLocator.self().getSBMService().getDataslotValue(pid, "isAutoOA",
					SBMUtils.getSession(request));
			if (!isAutoOA) {
				String[] completedWSNames = ResourceLocator.self().getSBMService().getCompletedWSNames(pid,
						SBMUtils.getSession(request));
				if (null != completedWSNames && completedWSNames.length > 0) {
					for (int k = 0; k < completedWSNames.length; ++k) {
						this.logger.debug("completedWSNames[k] :: " + completedWSNames[k]);
						if (completedWSNames[k].equals("Office Assignment Task")) {
							isOAComplete = true;
							break;
						}
					}
				}
			} else {
				isOAComplete = true;
			}
		}

		return isOAComplete;
	}

	public long getPIDForCRN(String crn) throws CMSException {
		return this.officeAssignmentDAO.getPIDForCRN(crn);
	}

	private TeamDetails addReDetailForOffice(TeamDetails teamDetail) throws CMSException {
		this.logger.debug("in addReDetailForOffice Re Ids :: " + teamDetail.getReIds() + " for team :: "
				+ teamDetail.getTeamId());

		try {
			this.logger.debug("Re Ids length before :: " + teamDetail.getReIds().length());
			if (teamDetail.getReIds().startsWith("#")) {
				teamDetail.setReIds(teamDetail.getReIds().replace("#", ""));
			}

			this.logger.debug("Re Ids length after :: " + teamDetail.getReIds().length());
			List<SubTeamReMapVO> teamSubjectREDetails = new ArrayList();
			String[] subReDetails = teamDetail.getReIds().split(",");

			for (int i = 0; i < subReDetails.length; ++i) {
				SubTeamReMapVO vo = new SubTeamReMapVO();
				vo.setCrn(teamDetail.getCrn());
				vo.setTeamId(teamDetail.getTeamId());
				vo.setSubjectId(subReDetails[i].split("::")[0]);
				vo.setReId(subReDetails[i].split("::")[1]);
				vo.setJlpPoints(subReDetails[i].split("::")[2]);
				vo.setUpdatedBy(teamDetail.getUpdatedBy());
				if (teamDetail.getTeamType().contains(this.BI) || teamDetail.getTeamType().contains(this.VENDOR)) {
					vo.setPerformer(teamDetail.getManagerName());
				}

				int subTeamReMapId = this.officeAssignmentDAO.addTeamREDetails(vo);
				vo.setSubTeamReMapId(subTeamReMapId);
				teamSubjectREDetails.add(vo);
			}

			teamDetail.setTeamSubjectREDetails(teamSubjectREDetails);
			return teamDetail;
		} catch (PatternSyntaxException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private void addReDetailForTeam(TeamDetails teamDetail) throws CMSException {
		this.logger.debug("in addReDetailForTeam for team :: " + teamDetail.getTeamId() + " :: RE List size :: "
				+ teamDetail.getTeamSubjectREDetails().size());

		try {
			List<SubTeamReMapVO> teamSubjectREDetails = teamDetail.getTeamSubjectREDetails();
			SubTeamReMapVO vo = null;

			for (int i = 0; i < teamSubjectREDetails.size(); ++i) {
				vo = (SubTeamReMapVO) teamSubjectREDetails.get(i);
				vo.setTeamId(teamDetail.getTeamId());
				this.officeAssignmentDAO.addTeamREDetails(vo);
			}

		} catch (PatternSyntaxException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private TeamDetails updateReDetailForOffice(TeamDetails teamDetail, List<Integer> hashCodeList,
			List<Integer> hashCodeListWithJlp, HashMap<String, Integer> mapForAnaCount) throws CMSException {
		this.logger.debug("in updateReDetailForOffice Re Ids :: " + teamDetail.getReIds() + " for team :: "
				+ teamDetail.getTeamId());

		try {
			this.logger.debug("Re Ids length before :: " + teamDetail.getReIds().length());
			if (teamDetail.getReIds().startsWith("#")) {
				teamDetail.setReIds(teamDetail.getReIds().replace("#", ""));
			}

			this.logger.debug("Re Ids length after :: " + teamDetail.getReIds().length());
			List<SubTeamReMapVO> teamSubjectREDetails = new ArrayList();
			String[] subReDetails = teamDetail.getReIds().split(",");
			String subjectIds = "";
			String previousSubjectId = "";
			String reIds = "";
			HashMap<Object, Object> param = null;
			int updateCount = false;
			int analystCount = false;
			String analystKey = null;
			boolean isBIVT = false;
			if (teamDetail.getTeamType().contains(this.BI) || teamDetail.getTeamType().contains(this.VENDOR)) {
				isBIVT = true;
			}

			int i;
			for (i = 0; i < subReDetails.length; ++i) {
				SubTeamReMapVO vo = new SubTeamReMapVO();
				vo.setCrn(teamDetail.getCrn());
				vo.setTeamId(teamDetail.getTeamId());
				int subTeamReMapId;
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
					param.put(this.TEAM_ID, teamDetail.getTeamId());
					param.put(this.SUBJECT_ID, previousSubjectId);
					param.put("crn", teamDetail.getCrn());
					param.put(this.RE_IDS, reIds);
					if (!teamDetail.getTeamType().contains(this.BI)
							&& !teamDetail.getTeamType().contains(this.VENDOR)) {
						subTeamReMapId = this.officeAssignmentDAO.deleteSubjectREMappingForTeam(param);
						this.logger.debug("reMapDeletedCount records :: " + subTeamReMapId);
					} else {
						param.put(this.PERFORMER, teamDetail.getManagerName());
						subTeamReMapId = this.officeAssignmentDAO.deleteSubjectREMappingForBIVTTeam(param);
						this.logger.debug("reMapDeletedCount records :: " + subTeamReMapId);
					}

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
				if (isBIVT) {
					vo.setPerformer(teamDetail.getManagerName());
				}

				if (!isBIVT && hashCodeList.contains(vo.hashCodeForPST())
						|| isBIVT && hashCodeList.contains(vo.hashCode())) {
					this.logger.debug("RE already assigned to the team ... checking if change in JLP ");
					if (hashCodeListWithJlp.contains(vo.hashCodeWithJLP())) {
						this.logger.debug("nothing to update");
					} else {
						this.logger.debug("updating JLP ");
						int updateCount;
						if (null == mapForAnaCount) {
							updateCount = this.officeAssignmentDAO.updateTeamREJLP(vo);
						} else {
							if (!isBIVT) {
								analystKey = vo.getTeamId() + "#" + vo.getSubjectId() + "#" + vo.getReId();
								int analystCount = (Integer) mapForAnaCount.get(analystKey);
								String updatedJlp = Float.parseFloat(vo.getJlpPoints()) / (float) analystCount + "";
								this.logger.debug("analystCount :: " + analystCount + " :: updating jlp from :: "
										+ vo.getJlpPoints() + " :: to :: " + updatedJlp);
								vo.setJlpPoints(updatedJlp);
							}

							updateCount = this.officeAssignmentDAO.updateTeamREJLP(vo);
						}

						this.logger.debug("updateCount :: " + updateCount);
					}
				} else {
					this.logger.debug(" adding RE assignment details");
					if (!isBIVT) {
						vo.setPerformer(teamDetail.getMainAnalyst());
					}

					subTeamReMapId = this.officeAssignmentDAO.addTeamREDetails(vo);
					vo.setSubTeamReMapId(subTeamReMapId);
				}

				teamSubjectREDetails.add(vo);
			}

			if (reIds.trim().length() > 0) {
				param = new HashMap();
				param.put(this.TEAM_ID, teamDetail.getTeamId());
				param.put(this.SUBJECT_ID, previousSubjectId);
				param.put("crn", teamDetail.getCrn());
				param.put(this.RE_IDS, reIds);
				if (!teamDetail.getTeamType().contains(this.BI) && !teamDetail.getTeamType().contains(this.VENDOR)) {
					i = this.officeAssignmentDAO.deleteSubjectREMappingForTeam(param);
					this.logger.debug("reMapDeletedCount records :: " + i);
				} else {
					param.put(this.PERFORMER, teamDetail.getManagerName());
					i = this.officeAssignmentDAO.deleteSubjectREMappingForBIVTTeam(param);
					this.logger.debug("reMapDeletedCount records :: " + i);
				}
			}

			this.logger.debug("subjectIds :: " + subjectIds);
			param = new HashMap();
			param.put(this.TEAM_ID, teamDetail.getTeamId());
			param.put(this.SUBJECT_IDS, subjectIds);
			param.put("crn", teamDetail.getCrn());
			i = this.officeAssignmentDAO.deleteSubjectMappingForTeam(param);
			this.logger.debug("deletedCount records :: " + i);
			teamDetail.setTeamSubjectREDetails(teamSubjectREDetails);
			return teamDetail;
		} catch (PatternSyntaxException var18) {
			throw new CMSException(this.logger, var18);
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	private TeamDetails populateAllTeamDetails(HttpServletRequest request) {
		TeamDetails allTeamDetails = new TeamDetails();
		if (null != request.getParameter("crn") && request.getParameter("crn").trim().length() > 0) {
			allTeamDetails.setCrn(request.getParameter("crn"));
		}

		if (null != request.getParameter(this.BYPASS_INTERIM)
				&& request.getParameter(this.BYPASS_INTERIM).trim().length() > 0) {
			allTeamDetails.setByPassInterim(request.getParameter(this.BYPASS_INTERIM));
		}

		if (null != request.getParameter(this.TEAMTYPE_DETAIL)
				&& request.getParameter(this.TEAMTYPE_DETAIL).trim().length() > 0) {
			allTeamDetails.setTeamTypeId(request.getParameter(this.TEAMTYPE_DETAIL));
		}

		if (null != request.getParameter(this.TEAM_OFFICE_DETAIL)
				&& request.getParameter(this.TEAM_OFFICE_DETAIL).trim().length() > 0) {
			allTeamDetails.setOffice(request.getParameter(this.TEAM_OFFICE_DETAIL));
		}

		if (null != request.getParameter(this.IDD1) && request.getParameter(this.IDD1).trim().length() > 0) {
			allTeamDetails.setDueDate1(request.getParameter(this.IDD1));
		}

		if (null != request.getParameter(this.IDD2) && request.getParameter(this.IDD2).trim().length() > 0) {
			allTeamDetails.setDueDate2(request.getParameter(this.IDD2));
		}

		if (null != request.getParameter(this.FDD) && request.getParameter(this.FDD).trim().length() > 0) {
			allTeamDetails.setFinalDueDate(request.getParameter(this.FDD));
		}

		if (null != request.getParameter(this.SUBJECT_RE_DETAIL)
				&& request.getParameter(this.SUBJECT_RE_DETAIL).trim().length() > 0) {
			allTeamDetails.setReIds(request.getParameter(this.SUBJECT_RE_DETAIL));
		}

		return allTeamDetails;
	}

	public HashMap<String, Object> getAOATeamDetails(List<SubjectDetails> subjectList, String crn) throws CMSException {
		this.logger.debug("in getAOATeamDetails ..subjectList size :: " + subjectList.size() + " :: crn :: " + crn);
		List<TeamDetails> teamDetailList = null;
		HashMap oaDetails = new HashMap();

		try {
			String failureMessage = "";
			boolean biREPresent = false;
			biREPresent = this.isBiREPresent(subjectList);
			this.logger.debug(" biREPresent :: " + biREPresent);
			this.logger.debug("jlpThreshhold :: " + this.propertyReader.getJlpThreshHold());
			HashMap<Object, Object> param = null;
			param = new HashMap();
			param.put("crn", crn);
			param.put(this.THRESH_HOLD, this.propertyReader.getJlpThreshHold());
			param.put(this.WIP_COUNT, this.propertyReader.getWipCaseCount());
			List<UserMasterVO> analystList = this.officeAssignmentDAO.getUserDetailsForAnalyst(param);
			this.logger.debug("analystList size:: " + analystList.size());
			List<UserMasterVO> biManagerList = null;
			CaseDetails caseDueDates = null;
			if (biREPresent) {
				biManagerList = this.officeAssignmentDAO.getUserDetailsForBI(crn);
				this.logger.debug("biManagerList size:: " + biManagerList.size());
			}

			if ((null == biManagerList || biManagerList.size() != 0) && analystList.size() != 0) {
				HashMap<Integer, SubjectDetails> localLangREDetailMap = this.getLLREDetails(subjectList);
				this.logger.debug("localLangREDetailMap size :: " + localLangREDetailMap.size());
				caseDueDates = this.getCaseDueDates(crn);
				teamDetailList = this.assignTeamToCase(crn, subjectList, localLangREDetailMap, analystList, biREPresent,
						biManagerList, caseDueDates);
				if (((List) teamDetailList).size() == 1
						&& ((TeamDetails) ((List) teamDetailList).get(0)).getTeamType().contains("BI")) {
					teamDetailList = new ArrayList();
					failureMessage = "as there is no RE other than BI to be assigned to Primary Team.";
				} else {
					failureMessage = "as no language expertise found.";
				}
			} else {
				this.logger.debug("no analyst/BI Manager found.");
				failureMessage = "as analyst have JLP above threshold/too many cases to be closed on research due date.";
			}

			oaDetails.put(this.TEAM, teamDetailList);
			oaDetails.put(this.MESSAGE, failureMessage);
			oaDetails.put(this.CASE_DUEDATES, caseDueDates);
			this.logger.debug("exiting getAOATeamDetails");
			return oaDetails;
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private List<TeamDetails> assignTeamToCase(String crn, List<SubjectDetails> subjectList,
			HashMap<Integer, SubjectDetails> localLangREDetailMap, List<UserMasterVO> analystList, boolean biREPresent,
			List<UserMasterVO> biManagerList, CaseDetails caseDueDates) throws CMSException {
		List<TeamDetails> teamDetailList = new ArrayList();
		this.logger.debug("in assignTeamToCase ..subjectList size :: " + subjectList.size() + " :: crn :: " + crn
				+ " :: localLangREDetailMap size :: " + localLangREDetailMap.size() + " :: biREPresent :: "
				+ biREPresent + " :: biManagerList :: " + biManagerList + " :: analystList size :: "
				+ analystList.size());

		try {
			List<TeamTypeVO> teamTypeList = this.officeAssignmentDAO.getTeamTypes();
			boolean aoaFailure = false;
			List<Integer> userProfileHashCodeListWithBoth = new ArrayList();
			List<Integer> userProfileHashCodeListWithLL = new ArrayList();
			List<Integer> userProfileHashCodeListWithEnglish = new ArrayList();
			List<Integer> userProfileHashCodeListWithCountry = new ArrayList();
			UserMasterVO userVo = null;
			UserCountryProfileVO userProfileVo = null;
			List<UserMasterVO> potentialUsersForSubject = null;
			List<UserMasterVO> assignedUserList = new ArrayList();
			boolean llAnalystAssigned = false;
			boolean engAnalystAssigned = false;
			this.prepareAllHashCodeList(analystList, userProfileHashCodeListWithBoth, userProfileHashCodeListWithLL,
					userProfileHashCodeListWithEnglish, userProfileHashCodeListWithCountry);
			this.logger.debug("userProfileHashCodeListWithBoth size :: " + userProfileHashCodeListWithBoth.size());

			for (int h = 0; h < userProfileHashCodeListWithBoth.size(); ++h) {
				this.logger.debug(" HashCodes for " + h + " :: userProfileHashCodeListWithBoth :: "
						+ userProfileHashCodeListWithBoth.get(h) + " :: userProfileHashCodeListWithLL :: "
						+ userProfileHashCodeListWithLL.get(h) + " :: userProfileHashCodeListWithEnglish :: "
						+ userProfileHashCodeListWithEnglish.get(h) + " :: userProfileHashCodeListWithCountry :: "
						+ userProfileHashCodeListWithCountry.get(h));
			}

			TeamDetails biTeam = null;
			List<SubTeamReMapVO> biTeamSubjectREDetails = null;
			if (biREPresent) {
				biTeam = new TeamDetails();
				biTeamSubjectREDetails = new ArrayList();
				this.populateBITeamDetails(crn, biManagerList, teamTypeList, biTeam);
				this.setSTDueDates(caseDueDates, biTeam);
			}

			int j;
			for (int i = 0; i < subjectList.size(); ++i) {
				llAnalystAssigned = false;
				engAnalystAssigned = false;
				this.logger.debug("team list size at start of subject loop :: " + teamDetailList.size());
				potentialUsersForSubject = new ArrayList();
				SubjectDetails subject = (SubjectDetails) subjectList.get(i);
				this.logger.debug(
						"starting for subject :: " + subject.getSubjectName() + " ::id :: " + subject.getSubjectId()
								+ " :: country :: " + subject.getCountryId() + " :: re_ids :: " + subject.getReIds());
				List oldTeam1SubjectREDetails;
				int team1Index;
				TeamDetails newTeam;
				ArrayList teamSubjectREDetails;
				TeamDetails newTeam;
				TeamDetails newST;
				List oldTeamSubjectREDetails;
				SubTeamReMapVO vo;
				int teamIndex;
				int l;
				ResearchElementMasterVO reVo;
				int p;
				TeamDetails newTeam;
				List reList;
				TeamTypeVO teamType;
				List reList;
				SubTeamReMapVO vo;
				ResearchElementMasterVO reVo;
				UserMasterVO user;
				if (localLangREDetailMap.containsKey(subject.getSubjectId())) {
					this.logger.debug(" subject have local language REs .. assignedUserList.size() :: "
							+ assignedUserList.size());
					if (assignedUserList.size() > 0) {
						for (j = 0; j < assignedUserList.size(); ++j) {
							userVo = (UserMasterVO) assignedUserList.get(j);
							userProfileVo = new UserCountryProfileVO();
							userProfileVo.setProfileCountryId(subject.getCountryId());
							userProfileVo.setEngMediaList(0);
							userProfileVo.setLocLangList(1);
							userProfileVo.setUserMasterId(userVo.getUserMasterId());
							this.logger.debug(" hashcode ::: " + userProfileVo.totalHashCode());
							if (userProfileHashCodeListWithBoth.contains(userProfileVo.totalHashCode())) {
								this.logger.debug(
										"there is assigned analyst who can do LL and English RE for this subject country :: "
												+ userVo.getLoginId());
								potentialUsersForSubject.add(userVo);
								llAnalystAssigned = true;
								break;
							}
						}
					}

					if (potentialUsersForSubject.size() == 0) {
						for (j = 0; j < analystList.size(); ++j) {
							userVo = (UserMasterVO) analystList.get(j);
							userProfileVo = new UserCountryProfileVO();
							userProfileVo.setProfileCountryId(subject.getCountryId());
							userProfileVo.setEngMediaList(0);
							userProfileVo.setLocLangList(1);
							userProfileVo.setUserMasterId(userVo.getUserMasterId());
							this.logger.debug(" hashcode ::: " + userProfileVo.totalHashCode());
							if (userProfileHashCodeListWithBoth.contains(userProfileVo.totalHashCode())) {
								this.logger.debug(
										"there is analyst who can do LL and English RE for this subject country :: "
												+ userVo.getLoginId());
								potentialUsersForSubject.add(userVo);
								break;
							}
						}
					}

					if (potentialUsersForSubject.size() == 0) {
						this.logger.debug("there is no analyst who can do LL and English RE both for the subject :: "
								+ subject.getSubjectId());

						for (j = 0; j < assignedUserList.size(); ++j) {
							userVo = (UserMasterVO) assignedUserList.get(j);
							userProfileVo = new UserCountryProfileVO();
							userProfileVo.setProfileCountryId(subject.getCountryId());
							userProfileVo.setLocLangList(1);
							userProfileVo.setUserMasterId(userVo.getUserMasterId());
							this.logger.debug(" hashcode ::: " + userProfileVo.localLangHashCode());
							if (userProfileHashCodeListWithLL.contains(userProfileVo.localLangHashCode())) {
								this.logger.debug(
										"there is already assigned analyst who can do only LL RE for this subject country :: "
												+ userVo.getLoginId());
								potentialUsersForSubject.add(userVo);
								break;
							}
						}

						if (potentialUsersForSubject.size() == 0) {
							for (j = 0; j < analystList.size(); ++j) {
								userVo = (UserMasterVO) analystList.get(j);
								userProfileVo = new UserCountryProfileVO();
								userProfileVo.setProfileCountryId(subject.getCountryId());
								userProfileVo.setLocLangList(1);
								userProfileVo.setUserMasterId(userVo.getUserMasterId());
								this.logger.debug(" hashcode ::: " + userProfileVo.localLangHashCode());
								if (userProfileHashCodeListWithLL.contains(userProfileVo.localLangHashCode())) {
									this.logger
											.debug("there is analyst who can do only LL RE for this subject country :: "
													+ userVo.getLoginId());
									potentialUsersForSubject.add(userVo);
									break;
								}
							}
						}

						if (potentialUsersForSubject.size() == 0) {
							this.logger.debug("breaking the loop from LL check");
							aoaFailure = true;
							break;
						}

						this.logger.debug(
								"found analyst who can do LL RE..now checking for anlayst for Englist RE of the subject");
						UserMasterVO englishCheckUser = null;
						int j;
						if (assignedUserList.size() > 0) {
							for (j = 0; j < assignedUserList.size(); ++j) {
								userVo = (UserMasterVO) assignedUserList.get(j);
								userProfileVo = new UserCountryProfileVO();
								userProfileVo.setProfileCountryId(subject.getCountryId());
								userProfileVo.setEngMediaList(0);
								userProfileVo.setUserMasterId(userVo.getUserMasterId());
								this.logger.debug(" hashcode ::: " + userProfileVo.englishHashCode() + " :: "
										+ userProfileVo.countryHashCode());
								if (userProfileHashCodeListWithEnglish.contains(userProfileVo.englishHashCode())
										|| !userProfileHashCodeListWithCountry
												.contains(userProfileVo.countryHashCode())) {
									this.logger.debug(
											"there is already assigned analyst who can do only English for this subject country :: "
													+ userVo.getLoginId());
									englishCheckUser = userVo;
									engAnalystAssigned = true;
									break;
								}
							}
						}

						if (null == englishCheckUser) {
							for (j = 0; j < analystList.size(); ++j) {
								userVo = (UserMasterVO) analystList.get(j);
								userProfileVo = new UserCountryProfileVO();
								userProfileVo.setProfileCountryId(subject.getCountryId());
								userProfileVo.setEngMediaList(0);
								userProfileVo.setUserMasterId(userVo.getUserMasterId());
								this.logger.debug(" hashcode ::: " + userProfileVo.englishHashCode() + " :: "
										+ userProfileVo.countryHashCode());
								if (userProfileHashCodeListWithEnglish.contains(userProfileVo.englishHashCode())
										|| !userProfileHashCodeListWithCountry
												.contains(userProfileVo.countryHashCode())) {
									this.logger.debug(
											"there is analyst who can do only English for this subject country :: "
													+ userVo.getLoginId());
									englishCheckUser = userVo;
									break;
								}
							}
						}

						if (null == englishCheckUser) {
							this.logger.debug("breaking the loop from LL english check");
							aoaFailure = true;
							break;
						}

						this.logger.debug("llAnalystAssigned :: " + llAnalystAssigned + " :: engAnalystAssigned :: "
								+ engAnalystAssigned);
						TeamDetails oldTeam1;
						int p;
						ArrayList teamSubjectREDetails;
						SubTeamReMapVO vo;
						int l;
						if (!llAnalystAssigned && !engAnalystAssigned) {
							this.logger.debug("All REs of the subject will be added to the new teams only.");
							teamSubjectREDetails = new ArrayList();
							List<SubTeamReMapVO> stSubjectREDetails = new ArrayList();
							newST = new TeamDetails();
							newTeam = new TeamDetails();
							if (teamDetailList.size() == 0) {
								this.logger.debug("add PT team");
								newTeam.setTeamId(1);
								this.setPTDueDates(caseDueDates, newTeam);
							} else {
								newTeam.setTeamId(teamDetailList.size() + 1);
								this.logger.debug("add ST team");
								this.setSTDueDates(caseDueDates, newTeam);
							}

							this.populateNewTeamDetails(crn, potentialUsersForSubject, newST);
							this.populateSecondTeamDetails(crn, englishCheckUser, newTeam);
							if (teamDetailList.size() == 0) {
								newST.setTeamId(2);
							} else {
								newST.setTeamId(teamDetailList.size() + 2);
							}

							this.logger.debug("second will always be ST team");
							this.setSTDueDates(caseDueDates, newST);
							newTeam = null;
							oldTeam1 = null;
							List<ResearchElementMasterVO> reList = subject.getReList();

							for (p = 0; p < reList.size(); ++p) {
								SubTeamReMapVO vo = new SubTeamReMapVO();
								ResearchElementMasterVO reVo = (ResearchElementMasterVO) reList.get(p);
								vo.setCrn(crn);
								vo.setSubjectId(Integer.toString(subject.getSubjectId()));
								vo.setReId(Integer.toString(reVo.getrEMasterId()));
								vo.setJlpPoints(reVo.getPoints());
								if (biREPresent && null != reVo.getBiTeam()
										&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
									vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
									biTeamSubjectREDetails.add(vo);
								} else if (null != reVo.getLocalLanguage()
										&& reVo.getLocalLanguage().equalsIgnoreCase(this.YES)) {
									vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
									stSubjectREDetails.add(vo);
								} else {
									vo.setPerformer(englishCheckUser.getLoginId());
									teamSubjectREDetails.add(vo);
								}
							}

							newST.setTeamSubjectREDetails(stSubjectREDetails);
							newTeam.setTeamSubjectREDetails(teamSubjectREDetails);
							vo = null;

							for (l = 0; l < teamTypeList.size(); ++l) {
								TeamTypeVO teamType = (TeamTypeVO) teamTypeList.get(l);
								if (teamDetailList.size() == 0) {
									if (teamType.getTeamType().contains(this.PRIMARY)) {
										newTeam.setTeamTypeId(teamType.getTeamTypeId());
										newTeam.setTeamType(teamType.getTeamType());
									}

									if (teamType.getTeamType().contains(this.INTERNAL)) {
										newST.setTeamTypeId(teamType.getTeamTypeId());
										newST.setTeamType(teamType.getTeamType());
									}
								} else if (teamType.getTeamType().contains(this.INTERNAL)) {
									newTeam.setTeamTypeId(teamType.getTeamTypeId());
									newTeam.setTeamType(teamType.getTeamType());
									newST.setTeamTypeId(teamType.getTeamTypeId());
									newST.setTeamType(teamType.getTeamType());
									break;
								}
							}

							UserMasterVO llUser = (UserMasterVO) potentialUsersForSubject.get(0);
							englishCheckUser.setTeamId(newTeam.getTeamId());
							llUser.setTeamId(newST.getTeamId());
							teamDetailList.add(newTeam);
							teamDetailList.add(newST);
							analystList.remove(englishCheckUser);
							assignedUserList.add(englishCheckUser);
							analystList.remove(potentialUsersForSubject.get(0));
							assignedUserList.add(llUser);
						} else {
							int team2Index;
							TeamDetails oldTeam2;
							ResearchElementMasterVO reVo;
							SubTeamReMapVO vo;
							ResearchElementMasterVO reVo;
							List reList;
							int x;
							TeamTypeVO teamType;
							if (llAnalystAssigned && !engAnalystAssigned) {
								this.logger.debug(
										"LL REs of subject to be assigned to already added team and other REs to new team with englishCheckUser as analyst.");
								teamSubjectREDetails = new ArrayList();
								oldTeam1SubjectREDetails = null;
								newST = null;
								team2Index = -1;
								newTeam = new TeamDetails();

								for (p = 0; p < teamDetailList.size(); ++p) {
									newST = (TeamDetails) teamDetailList.get(p);
									if (newST.getTeamId() == ((UserMasterVO) potentialUsersForSubject.get(0))
											.getTeamId()) {
										team2Index = p;
										oldTeam1SubjectREDetails = newST.getTeamSubjectREDetails();
										break;
									}
								}

								this.logger.debug("old team index in list :: " + team2Index
										+ " :: oldTeamSubjectREDetails :: " + oldTeam1SubjectREDetails);
								if (null != oldTeam1SubjectREDetails) {
									this.logger.debug(
											"oldTeamSubjectREDetails size :: " + oldTeam1SubjectREDetails.size());
								}

								this.logger.debug("add new ST team");
								newTeam.setTeamId(teamDetailList.size() + 1);
								this.setSTDueDates(caseDueDates, newTeam);
								this.populateSecondTeamDetails(crn, englishCheckUser, newTeam);
								oldTeam1 = null;
								oldTeam2 = null;
								reList = subject.getReList();

								for (l = 0; l < reList.size(); ++l) {
									vo = new SubTeamReMapVO();
									reVo = (ResearchElementMasterVO) reList.get(l);
									vo.setCrn(crn);
									vo.setSubjectId(Integer.toString(subject.getSubjectId()));
									vo.setReId(Integer.toString(reVo.getrEMasterId()));
									vo.setJlpPoints(reVo.getPoints());
									if (biREPresent && null != reVo.getBiTeam()
											&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
										vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
										biTeamSubjectREDetails.add(vo);
									} else if (null != reVo.getLocalLanguage()
											&& reVo.getLocalLanguage().equalsIgnoreCase(this.YES)) {
										vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
										oldTeam1SubjectREDetails.add(vo);
									} else {
										vo.setPerformer(englishCheckUser.getLoginId());
										teamSubjectREDetails.add(vo);
									}
								}

								this.logger.debug("oldTeamSubjectREDetails size after addition of new subject REs :: "
										+ oldTeam1SubjectREDetails.size());
								newST.setTeamSubjectREDetails(oldTeam1SubjectREDetails);
								newTeam.setTeamSubjectREDetails(teamSubjectREDetails);
								reVo = null;

								for (x = 0; x < teamTypeList.size(); ++x) {
									teamType = (TeamTypeVO) teamTypeList.get(x);
									if (teamType.getTeamType().contains(this.INTERNAL)) {
										newTeam.setTeamTypeId(teamType.getTeamTypeId());
										newTeam.setTeamType(teamType.getTeamType());
										break;
									}
								}

								englishCheckUser.setTeamId(newTeam.getTeamId());
								teamDetailList.add(newTeam);
								if (team2Index >= 0) {
									teamDetailList.remove(team2Index);
									teamDetailList.add(team2Index, newST);
								}

								analystList.remove(englishCheckUser);
								assignedUserList.add(englishCheckUser);
							} else if (!llAnalystAssigned && engAnalystAssigned) {
								this.logger.debug(
										"LL REs of subject to be assigned to new team and other REs to old team which have englishCheckUser as analyst.");
								teamSubjectREDetails = new ArrayList();
								oldTeam1SubjectREDetails = null;
								newST = null;
								team2Index = -1;
								newTeam = new TeamDetails();

								for (p = 0; p < teamDetailList.size(); ++p) {
									newST = (TeamDetails) teamDetailList.get(p);
									if (newST.getTeamId() == englishCheckUser.getTeamId()) {
										team2Index = p;
										oldTeam1SubjectREDetails = newST.getTeamSubjectREDetails();
										break;
									}
								}

								this.logger.debug("old team index in list :: " + team2Index
										+ " :: oldTeamSubjectREDetails :: " + oldTeam1SubjectREDetails);
								if (null != oldTeam1SubjectREDetails) {
									this.logger.debug(
											"oldTeamSubjectREDetails size :: " + oldTeam1SubjectREDetails.size());
								}

								this.logger.debug("add new ST team");
								newTeam.setTeamId(teamDetailList.size() + 1);
								this.setSTDueDates(caseDueDates, newTeam);
								this.populateNewTeamDetails(crn, potentialUsersForSubject, newTeam);
								oldTeam1 = null;
								oldTeam2 = null;
								reList = subject.getReList();

								for (l = 0; l < reList.size(); ++l) {
									vo = new SubTeamReMapVO();
									reVo = (ResearchElementMasterVO) reList.get(l);
									vo.setCrn(crn);
									vo.setSubjectId(Integer.toString(subject.getSubjectId()));
									vo.setReId(Integer.toString(reVo.getrEMasterId()));
									vo.setJlpPoints(reVo.getPoints());
									if (biREPresent && null != reVo.getBiTeam()
											&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
										vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
										biTeamSubjectREDetails.add(vo);
									} else if (null != reVo.getLocalLanguage()
											&& reVo.getLocalLanguage().equalsIgnoreCase(this.YES)) {
										vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
										teamSubjectREDetails.add(vo);
									} else {
										vo.setPerformer(englishCheckUser.getLoginId());
										oldTeam1SubjectREDetails.add(vo);
									}
								}

								this.logger.debug("oldTeamSubjectREDetails size after addition of new subject REs :: "
										+ oldTeam1SubjectREDetails.size());
								newST.setTeamSubjectREDetails(oldTeam1SubjectREDetails);
								newTeam.setTeamSubjectREDetails(teamSubjectREDetails);
								reVo = null;

								for (x = 0; x < teamTypeList.size(); ++x) {
									teamType = (TeamTypeVO) teamTypeList.get(x);
									if (teamType.getTeamType().contains(this.INTERNAL)) {
										newTeam.setTeamTypeId(teamType.getTeamTypeId());
										newTeam.setTeamType(teamType.getTeamType());
										break;
									}
								}

								UserMasterVO user = (UserMasterVO) potentialUsersForSubject.get(0);
								user.setTeamId(newTeam.getTeamId());
								teamDetailList.add(newTeam);
								if (team2Index >= 0) {
									teamDetailList.remove(team2Index);
									teamDetailList.add(team2Index, newST);
								}

								analystList.remove(potentialUsersForSubject.get(0));
								assignedUserList.add(user);
							} else {
								this.logger.debug("All REs of subject to be assigned to already added teams.");
								List<SubTeamReMapVO> oldTeam2SubjectREDetails = new ArrayList();
								oldTeam1SubjectREDetails = null;
								team1Index = -1;
								team2Index = -1;
								newTeam = null;
								oldTeam1 = null;
								oldTeam2 = null;

								for (p = 0; p < teamDetailList.size(); ++p) {
									newTeam = (TeamDetails) teamDetailList.get(p);
									if (team1Index < 0
											&& newTeam.getTeamId() == ((UserMasterVO) potentialUsersForSubject.get(0))
													.getTeamId()) {
										team1Index = p;
										oldTeam1 = (TeamDetails) teamDetailList.get(p);
										oldTeam1SubjectREDetails = oldTeam1.getTeamSubjectREDetails();
									}

									if (team2Index < 0 && newTeam.getTeamId() == englishCheckUser.getTeamId()) {
										team2Index = p;
										oldTeam2 = (TeamDetails) teamDetailList.get(p);
										oldTeam2SubjectREDetails = oldTeam2.getTeamSubjectREDetails();
									}

									if (team2Index >= 0 && team1Index >= 0) {
										break;
									}
								}

								this.logger.debug("old team 1 index in list :: " + team1Index
										+ " :: oldTeam1SubjectREDetails :: " + oldTeam1SubjectREDetails);
								this.logger.debug("old team 2 index in list :: " + team2Index
										+ " :: oldTeam2SubjectREDetails :: " + oldTeam2SubjectREDetails);
								if (null != oldTeam1SubjectREDetails) {
									this.logger.debug(
											"oldTeam1SubjectREDetails size :: " + oldTeam1SubjectREDetails.size());
								}

								if (null != oldTeam2SubjectREDetails) {
									this.logger.debug("oldTeam2SubjectREDetails size :: "
											+ ((List) oldTeam2SubjectREDetails).size());
								}

								vo = null;
								reVo = null;
								List<ResearchElementMasterVO> reList = subject.getReList();

								for (int l = 0; l < reList.size(); ++l) {
									vo = new SubTeamReMapVO();
									reVo = (ResearchElementMasterVO) reList.get(l);
									vo.setCrn(crn);
									vo.setSubjectId(Integer.toString(subject.getSubjectId()));
									vo.setReId(Integer.toString(reVo.getrEMasterId()));
									vo.setJlpPoints(reVo.getPoints());
									if (biREPresent && null != reVo.getBiTeam()
											&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
										vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
										biTeamSubjectREDetails.add(vo);
									} else if (null != reVo.getLocalLanguage()
											&& reVo.getLocalLanguage().equalsIgnoreCase(this.YES)) {
										vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
										oldTeam1SubjectREDetails.add(vo);
									} else {
										vo.setPerformer(englishCheckUser.getLoginId());
										((List) oldTeam2SubjectREDetails).add(vo);
									}
								}

								this.logger.debug("after addition of new subject REs oldTeam1SubjectREDetails size  :: "
										+ oldTeam1SubjectREDetails.size() + " :: oldTeam2SubjectREDetails size  :: "
										+ ((List) oldTeam2SubjectREDetails).size());
								oldTeam1.setTeamSubjectREDetails(oldTeam1SubjectREDetails);
								oldTeam2.setTeamSubjectREDetails((List) oldTeam2SubjectREDetails);
								if (team1Index >= 0) {
									teamDetailList.remove(team1Index);
									teamDetailList.add(team1Index, oldTeam1);
								}

								if (team2Index >= 0) {
									teamDetailList.remove(team2Index);
									teamDetailList.add(team2Index, oldTeam2);
								}
							}
						}
					} else {
						this.logger.debug("there is analyst who can do LL and English RE for the subject :: "
								+ subject.getSubjectId());
						if (!llAnalystAssigned) {
							this.logger.debug("adding new team as no existing team analyst found.");
							teamSubjectREDetails = new ArrayList();
							newTeam = new TeamDetails();
							this.populateNewTeamDetails(crn, potentialUsersForSubject, newTeam);
							if (teamDetailList.size() == 0) {
								this.logger.debug("add PT team");
								newTeam.setTeamId(1);
								this.setPTDueDates(caseDueDates, newTeam);
							} else {
								this.logger.debug("add ST team");
								newTeam.setTeamId(teamDetailList.size() + 1);
								this.setSTDueDates(caseDueDates, newTeam);
							}

							oldTeam1SubjectREDetails = null;
							newST = null;
							reList = subject.getReList();

							for (l = 0; l < reList.size(); ++l) {
								vo = new SubTeamReMapVO();
								reVo = (ResearchElementMasterVO) reList.get(l);
								vo.setCrn(crn);
								vo.setSubjectId(Integer.toString(subject.getSubjectId()));
								vo.setReId(Integer.toString(reVo.getrEMasterId()));
								vo.setJlpPoints(reVo.getPoints());
								if (biREPresent && null != reVo.getBiTeam()
										&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
									vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
									biTeamSubjectREDetails.add(vo);
								} else {
									vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
									teamSubjectREDetails.add(vo);
								}
							}

							newTeam.setTeamSubjectREDetails(teamSubjectREDetails);
							newTeam = null;

							for (p = 0; p < teamTypeList.size(); ++p) {
								teamType = (TeamTypeVO) teamTypeList.get(p);
								if (teamDetailList.size() == 0) {
									if (teamType.getTeamType().contains(this.PRIMARY)) {
										newTeam.setTeamTypeId(teamType.getTeamTypeId());
										newTeam.setTeamType(teamType.getTeamType());
										break;
									}
								} else if (teamType.getTeamType().contains(this.INTERNAL)) {
									newTeam.setTeamTypeId(teamType.getTeamTypeId());
									newTeam.setTeamType(teamType.getTeamType());
									break;
								}
							}

							teamDetailList.add(newTeam);
							user = (UserMasterVO) potentialUsersForSubject.get(0);
							user.setTeamId(newTeam.getTeamId());
							analystList.remove(potentialUsersForSubject.get(0));
							assignedUserList.add(user);
						} else {
							this.logger.debug("adding RE to existing team analyst found.");
							oldTeamSubjectREDetails = null;
							newTeam = null;
							teamIndex = -1;

							for (team1Index = 0; team1Index < teamDetailList.size(); ++team1Index) {
								newTeam = (TeamDetails) teamDetailList.get(team1Index);
								if (newTeam.getTeamId() == ((UserMasterVO) potentialUsersForSubject.get(0))
										.getTeamId()) {
									teamIndex = team1Index;
									oldTeamSubjectREDetails = newTeam.getTeamSubjectREDetails();
									break;
								}
							}

							this.logger.debug("old team index in list :: " + teamIndex
									+ " :: oldTeamSubjectREDetails :: " + oldTeamSubjectREDetails);
							if (null != oldTeamSubjectREDetails) {
								this.logger.debug("oldTeamSubjectREDetails size :: " + oldTeamSubjectREDetails.size());
							}

							newST = null;
							newTeam = null;
							reList = subject.getReList();

							for (p = 0; p < reList.size(); ++p) {
								vo = new SubTeamReMapVO();
								reVo = (ResearchElementMasterVO) reList.get(p);
								vo.setCrn(crn);
								vo.setSubjectId(Integer.toString(subject.getSubjectId()));
								vo.setReId(Integer.toString(reVo.getrEMasterId()));
								vo.setJlpPoints(reVo.getPoints());
								if (biREPresent && null != reVo.getBiTeam()
										&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
									vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
									biTeamSubjectREDetails.add(vo);
								} else {
									vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
									oldTeamSubjectREDetails.add(vo);
								}
							}

							this.logger.debug("oldTeamSubjectREDetails size after adding new REs :: "
									+ oldTeamSubjectREDetails.size());
							newTeam.setTeamSubjectREDetails(oldTeamSubjectREDetails);
							if (teamIndex >= 0) {
								teamDetailList.remove(teamIndex);
								teamDetailList.add(teamIndex, newTeam);
							}
						}
					}
				} else {
					this.logger.debug(" subject does not have local language REs .. assignedUserList.size() :: "
							+ assignedUserList.size());
					if (assignedUserList.size() > 0) {
						for (j = 0; j < assignedUserList.size(); ++j) {
							userVo = (UserMasterVO) assignedUserList.get(j);
							userProfileVo = new UserCountryProfileVO();
							userProfileVo.setProfileCountryId(subject.getCountryId());
							userProfileVo.setEngMediaList(0);
							userProfileVo.setUserMasterId(userVo.getUserMasterId());
							this.logger.debug(" hashcode ::: " + userProfileVo.englishHashCode());
							if (userProfileHashCodeListWithEnglish.contains(userProfileVo.englishHashCode())
									|| !userProfileHashCodeListWithCountry.contains(userProfileVo.countryHashCode())) {
								this.logger.debug(
										"there is assigned analyst who can do English RE for this subject country :: "
												+ userVo.getLoginId());
								potentialUsersForSubject.add(userVo);
								engAnalystAssigned = true;
								break;
							}
						}
					}

					if (potentialUsersForSubject.size() == 0) {
						for (j = 0; j < analystList.size(); ++j) {
							userVo = (UserMasterVO) analystList.get(j);
							userProfileVo = new UserCountryProfileVO();
							userProfileVo.setProfileCountryId(subject.getCountryId());
							userProfileVo.setEngMediaList(0);
							userProfileVo.setUserMasterId(userVo.getUserMasterId());
							this.logger.debug(" hashcode ::: " + userProfileVo.englishHashCode());
							if (userProfileHashCodeListWithEnglish.contains(userProfileVo.englishHashCode())
									|| !userProfileHashCodeListWithCountry.contains(userProfileVo.countryHashCode())) {
								this.logger
										.debug("there is analyst who can do only English for this subject country :: "
												+ userVo.getLoginId());
								potentialUsersForSubject.add(userVo);
								break;
							}
						}
					}

					if (potentialUsersForSubject.size() == 0) {
						this.logger.debug("breaking the loop from only English check");
						aoaFailure = true;
						break;
					}

					this.logger.debug("Analyst found who can do all REs of the subject :: engAnalystAssigned :: "
							+ engAnalystAssigned);
					if (!engAnalystAssigned) {
						this.logger.debug(
								"There is no analyst of existing team who can do RE of the subject. Adding new team");
						teamSubjectREDetails = new ArrayList();
						newTeam = new TeamDetails();
						this.populateNewTeamDetails(crn, potentialUsersForSubject, newTeam);
						if (teamDetailList.size() == 0) {
							this.logger.debug("add PT team");
							newTeam.setTeamId(1);
							this.setPTDueDates(caseDueDates, newTeam);
						} else {
							this.logger.debug("add ST team");
							newTeam.setTeamId(teamDetailList.size() + 1);
							this.setSTDueDates(caseDueDates, newTeam);
						}

						oldTeam1SubjectREDetails = null;
						newST = null;
						reList = subject.getReList();

						for (l = 0; l < reList.size(); ++l) {
							vo = new SubTeamReMapVO();
							reVo = (ResearchElementMasterVO) reList.get(l);
							vo.setCrn(crn);
							vo.setSubjectId(Integer.toString(subject.getSubjectId()));
							vo.setReId(Integer.toString(reVo.getrEMasterId()));
							vo.setJlpPoints(reVo.getPoints());
							if (biREPresent && null != reVo.getBiTeam()
									&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
								vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
								biTeamSubjectREDetails.add(vo);
							} else {
								vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
								teamSubjectREDetails.add(vo);
							}
						}

						newTeam.setTeamSubjectREDetails(teamSubjectREDetails);
						newTeam = null;

						for (p = 0; p < teamTypeList.size(); ++p) {
							teamType = (TeamTypeVO) teamTypeList.get(p);
							if (teamDetailList.size() == 0) {
								if (teamType.getTeamType().contains(this.PRIMARY)) {
									newTeam.setTeamTypeId(teamType.getTeamTypeId());
									newTeam.setTeamType(teamType.getTeamType());
									break;
								}
							} else if (teamType.getTeamType().contains(this.INTERNAL)) {
								newTeam.setTeamTypeId(teamType.getTeamTypeId());
								newTeam.setTeamType(teamType.getTeamType());
								break;
							}
						}

						teamDetailList.add(newTeam);
						user = (UserMasterVO) potentialUsersForSubject.get(0);
						user.setTeamId(newTeam.getTeamId());
						analystList.remove(potentialUsersForSubject.get(0));
						assignedUserList.add(user);
					} else {
						this.logger.debug("There is analyst of existing team who can do RE of the subject");
						oldTeamSubjectREDetails = null;
						newTeam = null;
						teamIndex = -1;

						for (team1Index = 0; team1Index < teamDetailList.size(); ++team1Index) {
							newTeam = (TeamDetails) teamDetailList.get(team1Index);
							if (newTeam.getTeamId() == ((UserMasterVO) potentialUsersForSubject.get(0)).getTeamId()) {
								teamIndex = team1Index;
								oldTeamSubjectREDetails = newTeam.getTeamSubjectREDetails();
								break;
							}
						}

						this.logger.debug("old team index in list :: " + teamIndex + " :: oldTeamSubjectREDetails :: "
								+ oldTeamSubjectREDetails);
						if (null != oldTeamSubjectREDetails) {
							this.logger.debug("oldTeamSubjectREDetails size :: " + oldTeamSubjectREDetails.size());
						}

						newST = null;
						newTeam = null;
						reList = subject.getReList();

						for (p = 0; p < reList.size(); ++p) {
							vo = new SubTeamReMapVO();
							reVo = (ResearchElementMasterVO) reList.get(p);
							vo.setCrn(crn);
							vo.setSubjectId(Integer.toString(subject.getSubjectId()));
							vo.setReId(Integer.toString(reVo.getrEMasterId()));
							vo.setJlpPoints(reVo.getPoints());
							if (biREPresent && null != reVo.getBiTeam()
									&& reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
								vo.setPerformer(((UserMasterVO) biManagerList.get(0)).getLoginId());
								biTeamSubjectREDetails.add(vo);
							} else {
								vo.setPerformer(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
								oldTeamSubjectREDetails.add(vo);
							}
						}

						this.logger.debug("oldTeamSubjectREDetails size after adding new REs :: "
								+ oldTeamSubjectREDetails.size());
						newTeam.setTeamSubjectREDetails(oldTeamSubjectREDetails);
						if (teamIndex >= 0) {
							teamDetailList.remove(teamIndex);
							teamDetailList.add(teamIndex, newTeam);
						}
					}
				}

				if (aoaFailure) {
					break;
				}

				this.logger.debug("team list size at end of subject loop :: " + teamDetailList.size());
			}

			if (biREPresent && !aoaFailure) {
				biTeam.setTeamSubjectREDetails(biTeamSubjectREDetails);
				teamDetailList.add(biTeam);
			}

			if (aoaFailure) {
				this.logger.debug(
						"resetting teamDetailList outside subject loop as breaking from loop due to failure...current team size ::: "
								+ teamDetailList.size());
				teamDetailList = new ArrayList();
			} else {
				this.logger.debug("team assignment done with team : " + teamDetailList.size());
				TeamDetails currentTeam = null;
				TeamDetails prevTeam = null;

				for (j = 0; j < teamDetailList.size(); ++j) {
					currentTeam = (TeamDetails) teamDetailList.get(j);
					if (j == 0) {
						prevTeam = currentTeam;
					} else {
						this.logger.debug("team details :: " + prevTeam.getTeamSubjectREDetails() + " :: "
								+ prevTeam.getTeamSubjectREDetails().size() + " ::: "
								+ currentTeam.getTeamSubjectREDetails() + " :: "
								+ currentTeam.getTeamSubjectREDetails().size());
						if (null != prevTeam.getTeamSubjectREDetails() && prevTeam.getTeamSubjectREDetails().size() == 0
								&& null != currentTeam.getTeamSubjectREDetails()
								&& currentTeam.getTeamSubjectREDetails().size() > 0) {
							this.logger.debug("prevTeam.getTeamType() :: " + prevTeam.getTeamType()
									+ " :: currentTeam.getTeamType() : " + currentTeam.getTeamType());
							if (prevTeam.getTeamType().contains("Primary")
									&& !currentTeam.getTeamType().contains("BI")) {
								this.logger.debug("team to be removed is PT");
								currentTeam.setTeamType(prevTeam.getTeamType());
								currentTeam.setTeamTypeId(prevTeam.getTeamTypeId());
								if (null != caseDueDates.getrInterim1()) {
									currentTeam.setDueDate1(prevTeam.getDueDate1());
								}

								if (null != caseDueDates.getrInterim2()) {
									currentTeam.setDueDate2(prevTeam.getDueDate2());
								}

								currentTeam.setFinalDueDate(prevTeam.getFinalDueDate());
							} else {
								this.logger.debug("either previous team is non primary or current is BI");
							}

							this.logger.debug(
									"removing previous team with :: " + prevTeam.getTeamSubjectREDetails().size());
							teamDetailList.remove(j - 1);
							prevTeam = currentTeam;
							--j;
						} else if (null != currentTeam.getTeamSubjectREDetails()
								&& currentTeam.getTeamSubjectREDetails().size() == 0) {
							this.logger
									.debug("removing current index :: " + currentTeam.getTeamSubjectREDetails().size());
							teamDetailList.remove(j);
							--j;
						}
					}
				}
			}

			this.logger.debug("returning from assignTeamToCase with team : " + teamDetailList.size());
			return teamDetailList;
		} catch (ClassCastException var37) {
			throw new CMSException(this.logger, var37);
		} catch (NullPointerException var38) {
			throw new CMSException(this.logger, var38);
		} catch (UnsupportedOperationException var39) {
			throw new CMSException(this.logger, var39);
		} catch (IllegalArgumentException var40) {
			throw new CMSException(this.logger, var40);
		} catch (IndexOutOfBoundsException var41) {
			throw new CMSException(this.logger, var41);
		} catch (Exception var42) {
			throw new CMSException(this.logger, var42);
		}
	}

	private void populateBITeamDetails(String crn, List<UserMasterVO> biManagerList, List<TeamTypeVO> teamTypeList,
			TeamDetails biTeam) {
		biTeam.setCrn(crn);
		biTeam.setManagerName(((UserMasterVO) biManagerList.get(0)).getLoginId());
		biTeam.setManagerFullName(((UserMasterVO) biManagerList.get(0)).getUserFullName());
		TeamTypeVO teamType = null;

		for (int x = 0; x < teamTypeList.size(); ++x) {
			teamType = (TeamTypeVO) teamTypeList.get(x);
			if (teamType.getTeamType().contains(this.BI)) {
				biTeam.setTeamTypeId(teamType.getTeamTypeId());
				biTeam.setTeamType(teamType.getTeamType());
				break;
			}
		}

	}

	private void populateSecondTeamDetails(String crn, UserMasterVO englishCheckUser, TeamDetails newTeam) {
		newTeam.setOffice(String.valueOf(englishCheckUser.getOfficeId()));
		newTeam.setOfficeName(englishCheckUser.getOffice());
		newTeam.setMainAnalyst(englishCheckUser.getLoginId());
		newTeam.setManagerName(englishCheckUser.getReportsTo());
		newTeam.setReviewer1(englishCheckUser.getReportsTo());
		newTeam.setCrn(crn);
	}

	private void prepareAllHashCodeList(List<UserMasterVO> analystList, List<Integer> userProfileHashCodeListWithBoth,
			List<Integer> userProfileHashCodeListWithLL, List<Integer> userProfileHashCodeListWithEnglish,
			List<Integer> userProfileHashCodeListWithCountry) throws CMSException {
		try {
			UserMasterVO userVo = null;
			List<UserCountryProfileVO> userProfileList = null;
			UserCountryProfileVO userProfileVo = null;

			for (int j = 0; j < analystList.size(); ++j) {
				userVo = (UserMasterVO) analystList.get(j);
				userProfileList = userVo.getUserCountryProfileList();

				for (int k = 0; k < userProfileList.size(); ++k) {
					userProfileVo = (UserCountryProfileVO) userProfileList.get(k);
					userProfileHashCodeListWithBoth.add(userProfileVo.totalHashCode());
					userProfileHashCodeListWithLL.add(userProfileVo.localLangHashCode());
					userProfileHashCodeListWithEnglish.add(userProfileVo.englishHashCode());
					userProfileHashCodeListWithCountry.add(userProfileVo.countryHashCode());
				}
			}

		} catch (ClassCastException var11) {
			throw new CMSException(this.logger, var11);
		} catch (NullPointerException var12) {
			throw new CMSException(this.logger, var12);
		} catch (UnsupportedOperationException var13) {
			throw new CMSException(this.logger, var13);
		} catch (IllegalArgumentException var14) {
			throw new CMSException(this.logger, var14);
		} catch (IndexOutOfBoundsException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	private void populateNewTeamDetails(String crn, List<UserMasterVO> potentialUsersForSubject, TeamDetails newTeam)
			throws CMSException {
		try {
			newTeam.setOffice(String.valueOf(((UserMasterVO) potentialUsersForSubject.get(0)).getOfficeId()));
			newTeam.setOfficeName(((UserMasterVO) potentialUsersForSubject.get(0)).getOffice());
			newTeam.setMainAnalyst(((UserMasterVO) potentialUsersForSubject.get(0)).getLoginId());
			newTeam.setManagerName(((UserMasterVO) potentialUsersForSubject.get(0)).getReportsTo());
			newTeam.setReviewer1(((UserMasterVO) potentialUsersForSubject.get(0)).getReportsTo());
			newTeam.setCrn(crn);
		} catch (IndexOutOfBoundsException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private void setPTDueDates(CaseDetails caseDueDates, TeamDetails newTeam) throws CMSException {
		try {
			if (null != caseDueDates.getrInterim1()) {
				newTeam.setByPassInterim("0");
				newTeam.setDueDate1(this.targetFormat.format(caseDueDates.getrInterim1()));
			} else {
				newTeam.setByPassInterim("1");
				newTeam.setDueDate1("");
			}

			if (null != caseDueDates.getrInterim2()) {
				newTeam.setDueDate2(this.targetFormat.format(caseDueDates.getrInterim2()));
			} else {
				newTeam.setDueDate2("");
			}

			newTeam.setFinalDueDate(this.targetFormat.format(caseDueDates.getFinalRDueDate()));
		} catch (NullPointerException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private void setSTDueDates(CaseDetails caseDueDates, TeamDetails newTeam) throws CMSException {
		Date temp = null;

		try {
			if (null != caseDueDates.getrInterim1()) {
				newTeam.setByPassInterim("0");
				this.cal.setTime(caseDueDates.getrInterim1());
				this.cal.add(5, -1);
				temp = this.cal.getTime();
				if (this.cal.get(7) == 7) {
					this.cal.add(5, -1);
					temp = this.cal.getTime();
				} else if (this.cal.get(7) == 1) {
					this.cal.add(5, -2);
					temp = this.cal.getTime();
				}

				if (temp.compareTo(new Date()) > 0) {
					newTeam.setDueDate1(this.targetFormat.format(temp));
				} else {
					newTeam.setDueDate1(this.targetFormat.format(new Date()));
				}
			} else {
				newTeam.setDueDate1("");
				newTeam.setByPassInterim("1");
			}

			if (null != caseDueDates.getrInterim2()) {
				this.cal.setTime(caseDueDates.getrInterim2());
				this.cal.add(5, -1);
				temp = this.cal.getTime();
				if (this.cal.get(7) == 7) {
					this.cal.add(5, -1);
					temp = this.cal.getTime();
				} else if (this.cal.get(7) == 1) {
					this.cal.add(5, -2);
					temp = this.cal.getTime();
				}

				if (temp.compareTo(new Date()) > 0) {
					newTeam.setDueDate2(this.targetFormat.format(temp));
				} else {
					newTeam.setDueDate2(this.targetFormat.format(new Date()));
				}
			} else {
				newTeam.setDueDate2("");
			}

			this.cal.setTime(caseDueDates.getFinalRDueDate());
			this.cal.add(5, -1);
			temp = this.cal.getTime();
			if (this.cal.get(7) == 7) {
				this.cal.add(5, -1);
				temp = this.cal.getTime();
			} else if (this.cal.get(7) == 1) {
				this.cal.add(5, -2);
				temp = this.cal.getTime();
			}

			if (temp.compareTo(new Date()) > 0) {
				newTeam.setFinalDueDate(this.targetFormat.format(temp));
			} else {
				newTeam.setFinalDueDate(this.targetFormat.format(new Date()));
			}

		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private HashMap<Integer, SubjectDetails> getLLREDetails(List<SubjectDetails> subjectList) throws CMSException {
		this.logger.debug("in getLLREDetails");
		this.logger.debug("subjectList size :: " + subjectList.size());
		HashMap llREDetailMap = null;

		try {
			if (subjectList.size() > 0) {
				llREDetailMap = new HashMap();

				for (int i = 0; i < subjectList.size(); ++i) {
					SubjectDetails subject = (SubjectDetails) subjectList.get(i);
					List<ResearchElementMasterVO> reList = subject.getReList();
					String reIds = "";

					for (int j = 0; j < reList.size(); ++j) {
						ResearchElementMasterVO vo = (ResearchElementMasterVO) reList.get(j);
						if (null != vo.getLocalLanguage() && vo.getLocalLanguage().equalsIgnoreCase(this.YES)) {
							if (reIds.trim().length() == 0) {
								reIds = reIds + vo.getrEMasterId();
							} else {
								reIds = reIds + "," + vo.getrEMasterId();
							}
						}
					}

					this.logger.debug("reIds " + reIds + " for subject :: " + subject.getSubjectId());
					if (reIds.trim().length() > 0) {
						this.logger.debug("LL re present for :: " + subject.getSubjectId());
						SubjectDetails llSubject = new SubjectDetails();
						llSubject.setSubjectId(subject.getSubjectId());
						llSubject.setCountryId(subject.getCountryId());
						llSubject.setReIds(reIds);
						llREDetailMap.put(subject.getSubjectId(), llSubject);
					}
				}
			}

			this.logger.debug("llREDetailMap :: " + llREDetailMap);
			return llREDetailMap;
		} catch (IndexOutOfBoundsException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public void saveAOADetailsForCase(HttpServletRequest request) throws CMSException {
		this.logger.debug("in saveAOADetailsForCase");

		try {
			this.logger.debug("teamDetails in saveAOADetailsForCase :: " + request.getParameter(this.TEAM_LIST));
			if (request.getParameter(this.TEAM_LIST) != null && !request.getParameter(this.TEAM_LIST).equals("")) {
				UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
				this.logger.debug("logged in user :: " + userBean.getUserName());
				JSONArray jsonArray = JSONArray.fromObject(request.getParameter(this.TEAM_LIST));
				this.logger.debug("jsonArray size :: " + jsonArray.size());
				List<TeamDetails> teamDetails = JSONArray.toList(jsonArray, TeamDetails.class);
				this.logger.debug("teamDetails size :: " + teamDetails.size());
				this.logger.debug("saving details for CRN :: " + ((TeamDetails) teamDetails.get(0)).getCrn());
				CaseDetails caseDetail = new CaseDetails();
				List<TeamDetails> modifiedTeamDetails = new ArrayList();
				caseDetail.setCrn(((TeamDetails) teamDetails.get(0)).getCrn());
				int teamId = false;
				List<TeamDetails> rhAndOfficeDetails = this.getResearchHeadsForOffices((String) null);
				HashMap<String, String> rhMap = this.getRHAndOfficeDetails(rhAndOfficeDetails);

				for (int i = 0; i < teamDetails.size(); ++i) {
					TeamDetails teamDetail = (TeamDetails) teamDetails.get(i);
					teamDetail.setUpdatedBy(userBean.getUserName());
					if (null != teamDetail.getTeamType() && teamDetail.getTeamType().contains(this.BI)) {
						this.logger.debug("adding BI team");
					} else {
						this.logger.debug("adding PT/ST team");
						teamDetail.setResearchHead((String) rhMap.get(teamDetail.getOffice()));
					}

					int teamId = this.officeAssignmentDAO.addTeamDetails(teamDetail);
					this.logger.debug("teamId :: " + teamId);
					teamDetail.setTeamId(teamId);
					teamDetail = this.addTeamReDetail(teamDetail);
					modifiedTeamDetails.add(teamDetail);
				}

				caseDetail.setTeamList(modifiedTeamDetails);
				caseDetail.setWorkitemName(request.getParameter("workItem"));
				caseDetail.setUpdatedBy(userBean.getUserName());
				caseDetail.setIsAutoOfcAssign(1);
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setCRN(caseDetail.getCrn());
				caseHistory.setPid(String.valueOf(this.getPIDForCRN(caseDetail.getCrn())));
				caseHistory.setProcessCycle("");
				if (null != request.getSession().getAttribute("loginLevel")) {
					caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
				} else {
					caseHistory.setPerformer(userBean.getUserName());
				}

				caseHistory.setTaskName("Office Assignment Task");
				caseHistory.setTaskStatus("");
				ResourceLocator.self().getCaseHistoryService().setCaseHistory((CaseDetails) null, caseDetail,
						caseHistory, "Auto Office Assignment");
				ResourceLocator.self().getFlowService().updateDS(SBMUtils.getSession(request), caseDetail, "Office");
			} else {
				this.logger.debug("team list is null or blank");
			}

		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private TeamDetails addTeamReDetail(TeamDetails teamDetail) throws CMSException {
		this.logger.debug("in addTeamReDetail for team :: " + teamDetail.getTeamId());

		try {
			List<SubTeamReMapVO> modifiedTeamSubjectREDetails = new ArrayList();
			List<SubTeamReMapVO> teamSubjectREDetails = teamDetail.getTeamSubjectREDetails();
			this.logger.debug("teamSubjectREDetails.size() :: " + teamSubjectREDetails.size());

			for (int i = 0; i < teamSubjectREDetails.size(); ++i) {
				this.logger.debug("teamSubjectREDetails.get(i) :: " + teamSubjectREDetails.get(i));
				JSONObject obj = JSONObject.fromObject(teamSubjectREDetails.get(i));
				SubTeamReMapVO vo = (SubTeamReMapVO) JSONObject.toBean(obj, SubTeamReMapVO.class);
				vo.setTeamId(teamDetail.getTeamId());
				vo.setUpdatedBy(teamDetail.getUpdatedBy());
				int subTeamReMapId = this.officeAssignmentDAO.addTeamREDetails(vo);
				vo.setSubTeamReMapId(subTeamReMapId);
				modifiedTeamSubjectREDetails.add(vo);
			}

			teamDetail.setTeamSubjectREDetails(modifiedTeamSubjectREDetails);
			return teamDetail;
		} catch (PatternSyntaxException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public HashMap<String, Object> getTabCaseDetails(Session session, MyTaskPageVO taskVo) throws CMSException {
		List<CaseDetails> mergedCaseDetailsList = new ArrayList();
		HashMap resultMap = null;

		try {
			resultMap = ResourceLocator.self().getSBMService().getBulkDataForUser(session, "Office", taskVo);
			List<MyTaskPageVO> taskList = null;
			long totalCount = 0L;
			if (resultMap != null && !resultMap.isEmpty()) {
				totalCount = (Long) resultMap.get("total");
				taskList = (List) resultMap.get("processInstanceList");
			}

			this.logger.debug("taskList size :: " + taskList.size() + " totalCount :: " + totalCount);
			if (taskList.size() > 0) {
				HashMap<Object, Object> param = new HashMap();
				param.put(this.TASK_LIST, taskList);
				List<CaseDetails> caseDetailsList = this.officeAssignmentDAO.getTabCaseSubjectDetails(param);
				this.logger.debug("caseDetailsList size :: " + caseDetailsList.size());
				List<String> crnListWithSavedOfficeDetails = this.officeAssignmentDAO
						.getCRNWithSavedOfficeDetails(param);
				this.logger.debug("crnListWithSavedOfficeDetails size :: " + crnListWithSavedOfficeDetails.size());
				List<String> crnListWithBIRE = this.officeAssignmentDAO.getCRNWithBIRE(param);
				this.logger.debug("crnListWithBIRE size :: " + crnListWithBIRE.size());
				List<CaseDetails> caseTeamDetailsList = null;
				if (crnListWithSavedOfficeDetails.size() > 0) {
					param = new HashMap();
					param.put(this.CRN_LIST, crnListWithSavedOfficeDetails);
					caseTeamDetailsList = this.officeAssignmentDAO.getTabCaseTeamDetails(param);
					this.logger.debug("caseTeamDetailsList size :: " + caseTeamDetailsList.size());
				}

				CaseDetails caseDetails = null;
				boolean flagSet = false;

				for (int i = 0; i < caseDetailsList.size(); ++i) {
					flagSet = false;
					caseDetails = (CaseDetails) caseDetailsList.get(i);

					int j;
					for (j = 0; j < taskList.size(); ++j) {
						if (caseDetails.getCrn().equalsIgnoreCase(((MyTaskPageVO) taskList.get(j)).getCrn())) {
							caseDetails.setWorkitemName(((MyTaskPageVO) taskList.get(j)).getWorlItemName());
							taskList.remove(j);
							break;
						}
					}

					for (j = 0; j < crnListWithBIRE.size(); ++j) {
						if (caseDetails.getCrn().equalsIgnoreCase((String) crnListWithBIRE.get(j))) {
							caseDetails.setIsBIREPresent(true);
							flagSet = true;
							crnListWithBIRE.remove(j);
							break;
						}
					}

					if (!flagSet) {
						caseDetails.setIsBIREPresent(false);
					}

					flagSet = false;
					if (crnListWithSavedOfficeDetails.size() > 0) {
						for (j = 0; j < caseTeamDetailsList.size(); ++j) {
							CaseDetails vo = (CaseDetails) caseTeamDetailsList.get(j);
							if (caseDetails.getCrn().equalsIgnoreCase(vo.getCrn())) {
								caseDetails.setIsOASaved(true);
								flagSet = true;
								caseDetails.setTeamList(vo.getTeamList());
								caseTeamDetailsList.remove(j);
								break;
							}
						}
					}

					if (!flagSet) {
						caseDetails.setIsOASaved(false);
						caseDetails.setTeamList(new ArrayList());
					}

					mergedCaseDetailsList.add(caseDetails);
				}

				this.logger.debug(
						"mergedCaseDetailsList :: " + mergedCaseDetailsList.size() + " :: crnListWithBIRE size :: "
								+ crnListWithBIRE.size() + " :: caseTeamDetailsList size:: " + caseTeamDetailsList);
			} else {
				this.logger.debug("no OA task for user");
			}

			if (resultMap != null && !resultMap.isEmpty()) {
				resultMap.put("processInstanceList", mergedCaseDetailsList);
			} else {
				resultMap = new HashMap();
				resultMap.put("processInstanceList", mergedCaseDetailsList);
				resultMap.put("total", 0);
			}

			return resultMap;
		} catch (UnsupportedOperationException var18) {
			throw new CMSException(this.logger, var18);
		} catch (IndexOutOfBoundsException var19) {
			throw new CMSException(this.logger, var19);
		} catch (ClassCastException var20) {
			throw new CMSException(this.logger, var20);
		} catch (IllegalArgumentException var21) {
			throw new CMSException(this.logger, var21);
		} catch (NullPointerException var22) {
			throw new CMSException(this.logger, var22);
		} catch (Exception var23) {
			throw new CMSException(this.logger, var23);
		}
	}

	public HashMap<String, Object> getTabSubjectCountryREDetails(Session session, MyTaskPageVO taskVo)
			throws CMSException {
		HashMap<String, Object> resultMap = null;
		new ArrayList();
		new ArrayList();
		List<CaseDetails> caseDetailsListVO = new ArrayList();
		new ArrayList();
		Object tabCountryREList = new ArrayList();

		try {
			resultMap = ResourceLocator.self().getSBMService().getBulkDataForUser(session, "Office", taskVo);
			List<MyTaskPageVO> taskList = null;
			long totalCount = 0L;
			if (resultMap != null && !resultMap.isEmpty()) {
				totalCount = (Long) resultMap.get("total");
				taskList = (List) resultMap.get("processInstanceList");
			}

			this.logger.debug("taskList size in getTabSubjectCountryREDetails:: " + taskList.size() + " totalCount :: "
					+ totalCount);
			if (taskList.size() > 0) {
				HashMap<Object, Object> param = new HashMap();
				param.put(this.TASK_LIST, taskList);
				new ArrayList();
				new ArrayList();
				new ArrayList();
				List<CaseDetails> listAutoPopulation = this.officeAssignmentDAO.getAutoPopulatedCases(param);
				List<String> crnListWithBIRE = this.officeAssignmentDAO.getCRNWithBIRE(param);
				List<String> crnListWithSavedOfficeDetails = this.officeAssignmentDAO
						.getCRNWithSavedOfficeDetails(param);
				boolean biREPresent = false;
				boolean flagSet = false;
				this.logger.debug("Autopopulated list size is::" + listAutoPopulation.size());
				CaseDetails caseDetails = null;
				ArrayList<String> officeCaseList = null;

				for (int i = 0; i < taskList.size(); ++i) {
					caseDetails = new CaseDetails();
					biREPresent = false;
					this.logger.debug("CRN::" + ((MyTaskPageVO) taskList.get(i)).getCrn());
					List<String> replicatedSubjectList = this.officeAssignmentDAO
							.getAutoAddedSubjectList(((MyTaskPageVO) taskList.get(i)).getCrn());
					officeCaseList = new ArrayList();
					List<BranchOfficeMasterVO> officeBasedSubjectList = new ArrayList();
					Object replicationOffice;
					if (null != replicatedSubjectList && ((List) replicatedSubjectList).size() > 0) {
						replicationOffice = this.officeAssignmentDAO.getOfficesForAutoAddedSubject();
						Iterator it = ((List) replicationOffice).iterator();

						while (it.hasNext()) {
							officeCaseList.add((String) it.next());
						}

						this.logger.debug("Replicated Subject list size  :" + ((List) replicatedSubjectList).size());
						this.logger.debug("Replicated office list size:" + ((List) replicatedSubjectList).size());
					} else {
						replicatedSubjectList = new ArrayList();
						replicationOffice = new ArrayList();
					}

					int j;
					for (j = 0; j < crnListWithBIRE.size(); ++j) {
						if (((MyTaskPageVO) taskList.get(i)).getCrn()
								.equalsIgnoreCase((String) crnListWithBIRE.get(j))) {
							biREPresent = true;
							crnListWithBIRE.remove(j);
							break;
						}
					}

					caseDetails.setIsBIREPresent(biREPresent);
					flagSet = false;
					if (crnListWithSavedOfficeDetails.size() > 0) {
						for (j = 0; j < crnListWithSavedOfficeDetails.size(); ++j) {
							if (((MyTaskPageVO) taskList.get(i)).getCrn()
									.equalsIgnoreCase((String) crnListWithSavedOfficeDetails.get(j))) {
								flagSet = true;
								break;
							}
						}
					}

					caseDetails.setIsOASaved(flagSet);

					for (j = 0; j < listAutoPopulation.size(); ++j) {
						if (((MyTaskPageVO) taskList.get(i)).getCrn()
								.equalsIgnoreCase(((CaseDetails) listAutoPopulation.get(j)).getCrn())) {
							officeBasedSubjectList = ((CaseDetails) listAutoPopulation.get(j))
									.getOfficeBasedSubjectList();

							for (int t = 0; t < ((List) officeBasedSubjectList).size(); ++t) {
								if (biREPresent && !officeCaseList
										.contains(((BranchOfficeMasterVO) ((List) officeBasedSubjectList).get(t))
												.getBranchOffice())
										&& officeCaseList.size() < 4) {
									officeCaseList.add(((BranchOfficeMasterVO) ((List) officeBasedSubjectList).get(t))
											.getBranchOffice());
								}

								if (!biREPresent && !officeCaseList
										.contains(((BranchOfficeMasterVO) ((List) officeBasedSubjectList).get(t))
												.getBranchOffice())
										&& officeCaseList.size() < 5) {
									officeCaseList.add(((BranchOfficeMasterVO) ((List) officeBasedSubjectList).get(t))
											.getBranchOffice());
								}
							}
						}
					}

					caseDetails.setOfficeBasedSubjectList((List) officeBasedSubjectList);
					caseDetails.setReplicatedSubjectList((List) replicatedSubjectList);
					caseDetails.setReplicationOffice((List) replicationOffice);
					caseDetails.setCrn(((MyTaskPageVO) taskList.get(i)).getCrn());
					caseDetails.setOfficeList(officeCaseList);
					this.logger.debug("IsOASaved value is == " + caseDetails.getIsOASaved());
					if (!caseDetails.getIsOASaved()) {
						List<TeamDetails> teamList = new ArrayList();
						CaseDetails caseInfo = ResourceLocator.self().getCaseDetailService()
								.getCaseInfo(((MyTaskPageVO) taskList.get(i)).getCrn());
						String receivedDate = caseInfo.getReqRecdDate1();
						String clientCode = caseInfo.getClientCode();
						this.logger.debug("initial clientCode is --> " + clientCode);
						if (!clientCode.equalsIgnoreCase("J001")) {
							clientCode = "NONJ001";
						}

						String reportType = ((MyTaskPageVO) taskList.get(i)).getCrn().split("\\\\")[2];
						String teamType = "Supporting - Internal";
						Iterator i$ = officeCaseList.iterator();

						while (i$.hasNext()) {
							String office = (String) i$.next();
							TeamDetails teamDetails = new TeamDetails();
							this.logger.debug("initial office is --> " + office);
							if (null != office && !office.equalsIgnoreCase("PRC")) {
								office = "NONPRC";
							}

							this.logger.debug("====Final Args=====");
							this.logger.debug("receivedDate is ::: " + receivedDate);
							this.logger.debug("clientCode is ::: " + clientCode);
							this.logger.debug("reportType is ::: " + reportType);
							this.logger.debug("teamType is ::: " + teamType);
							this.logger.debug("office is ::: " + office);
							int daysBefore = this.getDaysBeforePT(clientCode, reportType, teamType, office);
							this.logger.debug("daysBefore is ::: " + daysBefore);
							String ptRDD;
							String stFinalRDD;
							if (null != caseInfo.getrInterim1()) {
								ptRDD = (new SimpleDateFormat(this.OA_DATE_FORMAT)).format(caseInfo.getrInterim1());
								this.logger.debug("int1RDD :::: " + ptRDD);
								stFinalRDD = ResourceLocator.self().getTaskService().getRDDForST(teamType, office,
										ptRDD, daysBefore, receivedDate);
								this.logger.debug("stInt1RDD >>>> " + stFinalRDD);
								teamDetails.setDueDate1(stFinalRDD);
							}

							if (null != caseInfo.getrInterim2()) {
								ptRDD = (new SimpleDateFormat(this.OA_DATE_FORMAT)).format(caseInfo.getrInterim2());
								this.logger.debug("int2RDD :::: " + ptRDD);
								stFinalRDD = ResourceLocator.self().getTaskService().getRDDForST(teamType, office,
										ptRDD, daysBefore, receivedDate);
								this.logger.debug("stInt2RDD >>>> " + stFinalRDD);
								teamDetails.setDueDate2(stFinalRDD);
							}

							ptRDD = (new SimpleDateFormat(this.OA_DATE_FORMAT)).format(caseInfo.getFinalRDueDate());
							this.logger.debug("ptRDD :::: " + ptRDD);
							stFinalRDD = ResourceLocator.self().getTaskService().getRDDForST(teamType, office, ptRDD,
									daysBefore, receivedDate);
							this.logger.debug("stFinalRDD >>>> " + stFinalRDD);
							teamDetails.setFinalDueDate(stFinalRDD);
							teamList.add(teamDetails);
						}

						caseDetails.setTeamList(teamList);
					}

					caseDetailsListVO.add(caseDetails);
				}

				tabCountryREList = this.officeAssignmentDAO.getTabCountryREList(param);
				if (null == tabCountryREList || ((List) tabCountryREList).size() == 0) {
					tabCountryREList = new ArrayList();
				}

				this.logger.debug("tabCountryREList - " + ((List) tabCountryREList).size());
			} else {
				this.logger.debug("no OA task for user");
			}

			if (resultMap != null && !resultMap.isEmpty()) {
				resultMap.put("autoCaseDetails", caseDetailsListVO);
				resultMap.put("reListForReplication", tabCountryREList);
			} else {
				resultMap = new HashMap();
				resultMap.put("autoCaseDetails", caseDetailsListVO);
				resultMap.put("reListForReplication", tabCountryREList);
			}

			return resultMap;
		} catch (UnsupportedOperationException var33) {
			throw new CMSException(this.logger, var33);
		} catch (IndexOutOfBoundsException var34) {
			throw new CMSException(this.logger, var34);
		} catch (ClassCastException var35) {
			throw new CMSException(this.logger, var35);
		} catch (IllegalArgumentException var36) {
			throw new CMSException(this.logger, var36);
		} catch (NullPointerException var37) {
			throw new CMSException(this.logger, var37);
		} catch (Exception var38) {
			throw new CMSException(this.logger, var38);
		}
	}

	public List<UserMasterVO> getBIManagerList() throws CMSException {
		return ResourceLocator.self().getUserService().getUsersForRole(this.BI_ROLE);
	}

	public List<UserMasterVO> getVendorManagerList() throws CMSException {
		return ResourceLocator.self().getUserService().getUsersForRole(this.VENDOR_ROLE);
	}

	public String getCurrentCaseCycle(Session session, long pid) throws CMSException {
		return (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "ProcessCycle", session);
	}

	public String getCurrentCaseStatus(String crn) throws CMSException {
		CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService().getCaseStatus(crn);
		return caseDetails.getCaseStatus();
	}

	public List<CaseDetails> getStatusForCases(List<String> crnDetails) throws CMSException {
		HashMap<Object, Object> param = new HashMap();
		param.put(this.CRN_LIST, crnDetails);
		return this.officeAssignmentDAO.getStatusForCases(param);
	}

	public void saveTeamDetailsTab(List<CaseDetails> caseDetailsList, Session session, String crnDetails)
			throws CMSException {
		this.logger.debug("in saveTeamDetailsTab :: caseDetailsList size :: " + caseDetailsList.size());

		try {
			CaseDetails newCaseDetail = null;
			CaseDetails oldCaseDetail = null;
			List<TeamDetails> oldTeamDetails = null;
			List<TeamDetails> newTeamDetails = null;
			List<Integer> hashCodeList = null;
			List<Integer> hashCodeListWithJlp = null;
			List<TeamDetails> teamDetails = null;
			List<CaseDetails> newCaseDetailList = new ArrayList();
			List<CaseDetails> oldCaseDetailList = new ArrayList();
			HashMap<String, String> caseStatusMap = new HashMap();
			List updatedCaseDetails;
			if (null != crnDetails && crnDetails.trim().length() > 0) {
				updatedCaseDetails = this.getStatusForCases(StringUtils.commaSeparatedStringToList(crnDetails));
				Iterator i$ = updatedCaseDetails.iterator();

				while (i$.hasNext()) {
					CaseDetails caseDetails = (CaseDetails) i$.next();
					caseStatusMap.put(caseDetails.getCrn(), caseDetails.getCaseStatus());
				}
			}

			TeamDetails teamDetail;
			int teamId;
			for (int j = 0; j < caseDetailsList.size(); ++j) {
				newCaseDetail = (CaseDetails) caseDetailsList.get(j);
				boolean wiExists = ResourceLocator.self().getSBMService()
						.wiExistsForUser(newCaseDetail.getWorkitemName(), session, newCaseDetail.getUpdatedBy());
				this.logger.debug("wiExists for :: " + newCaseDetail.getWorkitemName() + " :: is :: " + wiExists);
				if (!((String) caseStatusMap.get(newCaseDetail.getCrn())).equalsIgnoreCase("Cancelled")
						&& !((String) caseStatusMap.get(newCaseDetail.getCrn())).equalsIgnoreCase("On Hold")
						&& wiExists) {
					teamDetails = new ArrayList();
					oldCaseDetail = null;
					hashCodeList = new ArrayList();
					hashCodeListWithJlp = new ArrayList();
					if (newCaseDetail.getIsOASaved()) {
						oldCaseDetail = new CaseDetails();
						oldCaseDetail.setCrn(newCaseDetail.getCrn());
						oldCaseDetail.setWorkitemName(newCaseDetail.getWorkitemName());
						oldCaseDetail.setCaseMgrId(newCaseDetail.getCaseMgrId());
						oldTeamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(newCaseDetail.getCrn());
						this.logger.debug("oldTeamDetails size:: " + oldTeamDetails.size());
						oldCaseDetail.setTeamList(oldTeamDetails);
						this.generateHashCodeList(hashCodeList, hashCodeListWithJlp, oldTeamDetails);
					}

					this.logger.debug(
							"hashCodeList size :: " + hashCodeList.size() + " :: for crn :: " + newCaseDetail.getCrn());
					String updatedTeamIds = "";
					teamDetail = null;
					newTeamDetails = newCaseDetail.getTeamList();
					int teamId = false;

					for (int i = 0; i < newTeamDetails.size(); ++i) {
						teamDetail = (TeamDetails) newTeamDetails.get(i);
						if (!newCaseDetail.getIsOASaved()) {
							this.logger.debug("calling add as no info saved for case ");
							teamId = this.officeAssignmentDAO.addTeamDetails(teamDetail);
							this.logger.debug("teamId :: " + teamId);
							teamDetail.setTeamId(teamId);
							teamDetail = this.addReDetailForOffice(teamDetail);
						} else {
							this.logger.debug("checking for teamId  :: " + teamDetail.getTeamId());
							if (teamDetail.getTeamId() != 0) {
								this.logger.debug("calling update for teamId :: " + teamDetail.getTeamId());
								if (null == teamDetail.getTeamType() || teamDetail.getTeamType().trim().length() <= 0
										|| !teamDetail.getTeamType().contains(this.BI)
												&& !teamDetail.getTeamType().contains(this.VENDOR)) {
									this.officeAssignmentDAO.updatePSTOfficeDetails(teamDetail);
								} else {
									this.officeAssignmentDAO.updateBIVTOfficeDetails(teamDetail);
								}

								teamDetail = this.updateReDetailForOffice(teamDetail, hashCodeList, hashCodeListWithJlp,
										(HashMap) null);
							} else if (teamDetail.getTeamId() == 0) {
								this.logger.debug("calling add as team id is 0 ");
								teamId = this.officeAssignmentDAO.addTeamDetails(teamDetail);
								this.logger.debug("teamId :: " + teamId);
								teamDetail.setTeamId(teamId);
								teamDetail = this.addReDetailForOffice(teamDetail);
							}

							if (i == 0) {
								updatedTeamIds = updatedTeamIds + teamDetail.getTeamId();
							} else {
								updatedTeamIds = updatedTeamIds + "," + teamDetail.getTeamId();
							}

							this.logger.debug("updatedTeamIds : " + updatedTeamIds);
						}

						teamDetails.add(teamDetail);
					}

					if (newCaseDetail.getIsOASaved()) {
						HashMap<Object, Object> param = new HashMap();
						param.put("crn", newCaseDetail.getCrn());
						param.put(this.TEAM_IDS, updatedTeamIds);
						int deletedTeamCount = this.officeAssignmentDAO.deleteTeams(param);
						this.logger.debug("deletedTeamCount :: " + deletedTeamCount);
					}

					newCaseDetail.setTeamList(teamDetails);
					newCaseDetailList.add(newCaseDetail);
					oldCaseDetailList.add(oldCaseDetail);
				} else {
					this.logger.debug("not to process this case as change in case status.");
				}
			}

			ResourceLocator.self().getFlowService().bulkUpdateDSAndCompleteTask(session, newCaseDetailList, "Office");
			updatedCaseDetails = null;

			for (int k = 0; k < newCaseDetailList.size(); ++k) {
				CaseDetails updatedCaseDetails = (CaseDetails) newCaseDetailList.get(k);
				if (!updatedCaseDetails.isTaskCompleted()) {
					this.logger.debug("deleting team details for crn :: " + updatedCaseDetails.getCrn());
					this.deleteTeamDetailsForCrn(updatedCaseDetails.getCrn());
					if (updatedCaseDetails.getIsOASaved()) {
						this.logger.debug("calling add team details for case from oldCaseDetailList for crn :: "
								+ updatedCaseDetails.getCrn());
						oldTeamDetails = ((CaseDetails) oldCaseDetailList.get(k)).getTeamList();
						int teamId = false;
						teamDetail = null;

						for (teamId = 0; teamId < oldTeamDetails.size(); ++teamId) {
							teamDetail = (TeamDetails) oldTeamDetails.get(teamId);
							teamDetail.setUpdatedBy(((CaseDetails) caseDetailsList.get(0)).getUpdatedBy());
							int teamId = this.officeAssignmentDAO.addTeamDetails(teamDetail);
							this.logger.debug("teamId :: " + teamId);
							teamDetail.setTeamId(teamId);
							this.addReDetailForTeam(teamDetail);
						}
					}
				}
			}

			ResourceLocator.self().getCaseHistoryService().setCaseHistoryForBulkOfficeAssignment(oldCaseDetailList,
					newCaseDetailList);
		} catch (PatternSyntaxException var21) {
			throw new CMSException(this.logger, var21);
		} catch (Exception var22) {
			throw new CMSException(this.logger, var22);
		}
	}

	public void deleteTeamDetailsForCrn(String crn) throws CMSException {
		this.logger.debug("in deleteTeamDetailsForCrn for crn :: " + crn);
		int count = this.officeAssignmentDAO.deleteTeamDetailsForCrn(crn);
		this.logger.debug("team delete count :: " + count);
	}

	public String checkDueDates(TeamDetails allTeamDetails, int teamSize, boolean interim1Flag, boolean interim2Flag)
			throws CMSException {
		this.logger.debug("in the checkDueDates");
		String message = "";

		try {
			HashMap<String, String[]> tempTeamDetail = this.prepareTeamDDDetailMap(interim1Flag, interim2Flag,
					allTeamDetails);
			TeamDetails teamDetail = null;
			boolean holiDayCheck = false;
			boolean weekendCheck = false;
			boolean consecutiveBreakCheck = false;
			HashMap<String, String> dateMap = null;
			List<String> phDateList = null;
			HashMap<Object, Object> param = null;
			Date tempDate = null;

			for (int i = 0; i < teamSize; ++i) {
				weekendCheck = false;
				holiDayCheck = false;
				consecutiveBreakCheck = false;
				teamDetail = new TeamDetails();
				this.populateTeamDDDetails(interim1Flag, interim2Flag, tempTeamDetail, teamDetail, i);
				this.cal.setTime(this.targetFormat.parse(teamDetail.getFinalDueDate()));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					weekendCheck = true;
				}

				if (!weekendCheck && teamDetail.getDueDate1().trim().length() > 0) {
					this.cal.setTime(this.targetFormat.parse(teamDetail.getDueDate1()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
					}
				}

				if (!weekendCheck && teamDetail.getDueDate2().trim().length() > 0) {
					this.cal.setTime(this.targetFormat.parse(teamDetail.getDueDate2()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
					}
				}

				param = new HashMap();
				param.put("FDD", teamDetail.getFinalDueDate());
				dateMap = new HashMap();
				dateMap.put("FDD", teamDetail.getFinalDueDate());
				tempDate = this.targetFormat.parse(teamDetail.getFinalDueDate());
				this.cal.setTime(tempDate);
				this.cal.add(5, -1);
				dateMap.put("FDD1", this.targetFormat.format(this.cal.getTime()));
				param.put("FDD1", this.targetFormat.format(this.cal.getTime()));
				this.cal.setTime(tempDate);
				this.cal.add(5, -2);
				dateMap.put("FDD2", this.targetFormat.format(this.cal.getTime()));
				param.put("FDD2", this.targetFormat.format(this.cal.getTime()));
				this.cal.setTime(tempDate);
				this.cal.add(5, -3);
				dateMap.put("FDD3", this.targetFormat.format(this.cal.getTime()));
				param.put("FDD3", this.targetFormat.format(this.cal.getTime()));
				param.put("IDD1", teamDetail.getDueDate1());
				dateMap.put("IDD1", teamDetail.getDueDate1());
				if (null != teamDetail.getDueDate1() && teamDetail.getDueDate1().trim().length() > 0) {
					tempDate = this.targetFormat.parse(teamDetail.getDueDate1());
					this.cal.setTime(tempDate);
					this.cal.add(5, -1);
					dateMap.put("IDD11", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD11", this.targetFormat.format(this.cal.getTime()));
					this.cal.setTime(tempDate);
					this.cal.add(5, -2);
					dateMap.put("IDD12", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD12", this.targetFormat.format(this.cal.getTime()));
					this.cal.setTime(tempDate);
					this.cal.add(5, -3);
					dateMap.put("IDD13", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD13", this.targetFormat.format(this.cal.getTime()));
				} else {
					param.put("IDD11", "");
					param.put("IDD12", "");
					param.put("IDD13", "");
				}

				param.put("IDD2", teamDetail.getDueDate2());
				dateMap.put("IDD2", teamDetail.getDueDate2());
				if (null != teamDetail.getDueDate2() && teamDetail.getDueDate2().trim().length() > 0) {
					tempDate = this.targetFormat.parse(teamDetail.getDueDate2());
					this.cal.setTime(tempDate);
					this.cal.add(5, -1);
					dateMap.put("IDD21", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD21", this.targetFormat.format(this.cal.getTime()));
					this.cal.setTime(tempDate);
					this.cal.add(5, -2);
					dateMap.put("IDD22", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD22", this.targetFormat.format(this.cal.getTime()));
					this.cal.setTime(tempDate);
					this.cal.add(5, -3);
					dateMap.put("IDD23", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD23", this.targetFormat.format(this.cal.getTime()));
				} else {
					param.put("IDD21", "");
					param.put("IDD22", "");
					param.put("IDD23", "");
				}

				this.logger.debug("dateList size :: " + dateMap.size() + " :: parma map size :: " + param.size());
				if (Integer.parseInt(teamDetail.getTeamTypeId()) != 3
						&& Integer.parseInt(teamDetail.getTeamTypeId()) != 4) {
					param.put("officeId", teamDetail.getOffice());
					phDateList = this.officeAssignmentDAO.checkForPSTHoliday(param);
				} else {
					param.put("manager", teamDetail.getOffice());
					phDateList = this.officeAssignmentDAO.checkForBIVTHoliday(param);
				}

				if (null != phDateList && phDateList.size() > 0) {
					this.logger.debug("phDateList size :: " + phDateList.size());

					for (int k = 0; k < phDateList.size(); ++k) {
						String tempPhDate = (String) phDateList.get(k);
						if (this.targetFormat.parse(tempPhDate)
								.equals(this.targetFormat.parse((String) dateMap.get("FDD")))) {
							holiDayCheck = true;
							break;
						}

						if (((String) dateMap.get("IDD1")).trim().length() > 0
								&& this.targetFormat.parse(tempPhDate)
										.equals(this.targetFormat.parse((String) dateMap.get("IDD1")))
								|| ((String) dateMap.get("IDD2")).trim().length() > 0
										&& this.targetFormat.parse(tempPhDate)
												.equals(this.targetFormat.parse((String) dateMap.get("IDD2")))) {
							holiDayCheck = true;
							break;
						}

						if (k + 2 <= phDateList.size() - 1) {
							if (this.targetFormat.parse(tempPhDate)
									.equals(this.targetFormat.parse((String) dateMap.get("FDD3")))
									&& this.targetFormat.parse((String) phDateList.get(k + 1))
											.equals(this.targetFormat.parse((String) dateMap.get("FDD2")))
									&& this.targetFormat.parse((String) phDateList.get(k + 2))
											.equals(this.targetFormat.parse((String) dateMap.get("FDD1")))) {
								consecutiveBreakCheck = true;
								break;
							}

							if (null != teamDetail.getDueDate1() && teamDetail.getDueDate1().trim().length() > 0
									&& this.targetFormat.parse(tempPhDate)
											.equals(this.targetFormat.parse((String) dateMap.get("IDD13")))
									&& this.targetFormat.parse((String) phDateList.get(k + 1))
											.equals(this.targetFormat.parse((String) dateMap.get("IDD12")))
									&& this.targetFormat.parse((String) phDateList.get(k + 2))
											.equals(this.targetFormat.parse((String) dateMap.get("IDD11")))) {
								consecutiveBreakCheck = true;
								break;
							}

							if (null != teamDetail.getDueDate2() && teamDetail.getDueDate2().trim().length() > 0
									&& this.targetFormat.parse(tempPhDate)
											.equals(this.targetFormat.parse((String) dateMap.get("IDD23")))
									&& this.targetFormat.parse((String) phDateList.get(k + 1))
											.equals(this.targetFormat.parse((String) dateMap.get("IDD22")))
									&& this.targetFormat.parse((String) phDateList.get(k + 2))
											.equals(this.targetFormat.parse((String) dateMap.get("IDD21")))) {
								consecutiveBreakCheck = true;
								break;
							}
						}
					}
				}

				if (weekendCheck && holiDayCheck) {
					if (i == 0) {
						message = message + "Primary Team's Due Date falls on Public Holiday and weekend.<br>";
					} else {
						message = message + "Supporting #" + i
								+ " Team's Due Date falls on Public Holiday and weekend.<br>";
					}
				} else if (holiDayCheck) {
					if (i == 0) {
						message = message + "Primary Team's Due Date falls on Public Holiday.<br>";
					} else {
						message = message + "Supporting #" + i + " Team's Due Date falls on Public Holiday.<br>";
					}
				} else if (weekendCheck) {
					if (i == 0) {
						message = message + "Primary Team's Due Date falls on weekend.<br>";
					} else {
						message = message + "Supporting #" + i + " Team's Due Date falls on weekend.<br>";
					}
				} else if (!holiDayCheck && !weekendCheck && consecutiveBreakCheck) {
					if (i == 0) {
						message = message
								+ "Primary Team's Due Date falls right after at least 3 days of consecutive break.<br>";
					} else {
						message = message + "Supporting #" + i
								+ " Team's Due Date falls right after at least 3 days of consecutive break.<br>";
					}
				}
			}

			return message;
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	public boolean checkClientDueDates(String cInterim1Date, String cInterim2Date, String cFinalDate, String officeId)
			throws CMSException {
		this.logger.debug("in the checkClientDueDates");
		HashMap<Object, Object> param = null;
		boolean holiDayCheck = false;

		try {
			param = new HashMap();
			if (cFinalDate.trim().length() > 0) {
				this.cal.setTime(this.targetFormat.parse(cFinalDate));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					holiDayCheck = true;
				}

				param.put("officeId", officeId);
				param.put("holidayDate", this.targetFormat.parse(cFinalDate));
				if (this.officeAssignmentDAO.checkClientDueDates(param)) {
					holiDayCheck = true;
				}
			}

			if (cInterim1Date.trim().length() > 0) {
				this.cal.setTime(this.targetFormat.parse(cInterim1Date));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					holiDayCheck = true;
				}

				param.put("officeId", officeId);
				param.put("holidayDate", this.targetFormat.parse(cInterim1Date));
				if (this.officeAssignmentDAO.checkClientDueDates(param)) {
					holiDayCheck = true;
				}
			}

			if (cInterim2Date.trim().length() > 0) {
				this.cal.setTime(this.targetFormat.parse(cInterim2Date));
				if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
					holiDayCheck = true;
				}

				param.put("officeId", officeId);
				param.put("holidayDate", this.targetFormat.parse(cInterim2Date));
				if (this.officeAssignmentDAO.checkClientDueDates(param)) {
					holiDayCheck = true;
				}
			}

			this.logger.debug("holiDayCheck:::" + holiDayCheck);
			return holiDayCheck;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public String checkClientDueDatesTab(List<CaseDetails> caseDetailsList) throws CMSException {
		this.logger.debug("in the checkClientDueDatesTab");

		try {
			boolean holidayCheck = false;
			String message = "";
			boolean weekendCheck = false;
			Iterator i$ = caseDetailsList.iterator();

			while (i$.hasNext()) {
				CaseDetails caseDetail = (CaseDetails) i$.next();
				this.logger.debug("caseDetail.getCrn()::" + caseDetail.getCrn());
				this.logger.debug("caseDetail.getOfficeId()::" + caseDetail.getOfficeId());
				this.logger.debug("caseDetail.getcInterim1()::" + caseDetail.getcInterim1());
				this.logger.debug("caseDetail.getcInterim2()::" + caseDetail.getcInterim2());
				this.logger.debug("caseDetail.getFinalDueDate()::" + caseDetail.getFinalDueDate());
				if (caseDetail.getFinalDueDate().toString().length() > 0) {
					this.cal.setTime(new Date(caseDetail.getFinalDueDate().getTime()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
						this.logger.debug("weekendCheck in final date::" + weekendCheck);
					}
				}

				if (null != caseDetail.getcInterim1() && caseDetail.getcInterim1().toString().length() > 0) {
					this.cal.setTime(new Date(caseDetail.getcInterim1().getTime()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
						this.logger.debug("weekendCheck in interim 1 date::" + weekendCheck);
					}
				}

				if (null != caseDetail.getcInterim2() && caseDetail.getcInterim2().toString().length() > 0) {
					this.cal.setTime(new Date(caseDetail.getcInterim2().getTime()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
						this.logger.debug("weekendCheck in interim 2 date::" + weekendCheck);
					}
				}

				this.logger.debug("weekendCheck::" + weekendCheck);
				if (weekendCheck) {
					message = message + caseDetail.getCrn() + "weekend" + "<br>";
					this.logger.debug("message for weekend::" + message);
					weekendCheck = false;
				} else {
					int count = this.officeAssignmentDAO
							.checkClientDueDatesTab(caseDetail.getCrn(), caseDetail.getOfficeId(),
									caseDetail.getcInterim1(), caseDetail.getcInterim2(), caseDetail.getFinalDueDate())
							.length();
					if (count > 0) {
						holidayCheck = true;
						message = message + this.officeAssignmentDAO.checkClientDueDatesTab(caseDetail.getCrn(),
								caseDetail.getOfficeId(), caseDetail.getcInterim1(), caseDetail.getcInterim2(),
								caseDetail.getFinalDueDate()) + "holiday";
					} else {
						holidayCheck = false;
					}

					this.logger.debug("holidayCheck::" + holidayCheck);
					this.logger.debug("message for holiday::" + message);
				}
			}

			return message;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private void populateTeamDDDetails(boolean interim1Flag, boolean interim2Flag,
			HashMap<String, String[]> tempTeamDetail, TeamDetails teamDetail, int index) {
		teamDetail.setByPassInterim(((String[]) tempTeamDetail.get("byPassDetails"))[index]);
		if (null != teamDetail.getByPassInterim() && teamDetail.getByPassInterim().trim().length() > 0
				&& teamDetail.getByPassInterim().equals("true")) {
			teamDetail.setDueDate1("");
			teamDetail.setDueDate2("");
		} else {
			if (interim1Flag) {
				teamDetail.setDueDate1(((String[]) tempTeamDetail.get("teamIDD1"))[index]);
			} else {
				teamDetail.setDueDate1("");
			}

			if (interim2Flag) {
				if (((String[]) tempTeamDetail.get("teamIDD2"))[index].equalsIgnoreCase(this.BLANK)) {
					teamDetail.setDueDate2("");
				} else {
					teamDetail.setDueDate2(((String[]) tempTeamDetail.get("teamIDD2"))[index]);
				}
			} else {
				teamDetail.setDueDate2("");
			}
		}

		teamDetail.setFinalDueDate(((String[]) tempTeamDetail.get("teamFDD"))[index]);
		teamDetail.setTeamTypeId(((String[]) tempTeamDetail.get("teamTypeDetails"))[index]);
		teamDetail.setOffice(((String[]) tempTeamDetail.get("officeDetails"))[index]);
	}

	private HashMap<String, String[]> prepareTeamDDDetailMap(boolean interim1Flag, boolean interim2Flag,
			TeamDetails allTeamDetails) {
		HashMap<String, String[]> tempTeamDetail = new HashMap();
		tempTeamDetail.put("byPassDetails", allTeamDetails.getByPassInterim().split(","));
		tempTeamDetail.put("teamTypeDetails", allTeamDetails.getTeamTypeId().split(","));
		tempTeamDetail.put("officeDetails", allTeamDetails.getOffice().split(","));
		if (interim1Flag) {
			tempTeamDetail.put("teamIDD1", allTeamDetails.getDueDate1().split(","));
		}

		if (interim2Flag) {
			tempTeamDetail.put("teamIDD2", allTeamDetails.getDueDate2().split(","));
		}

		tempTeamDetail.put("teamFDD", allTeamDetails.getFinalDueDate().split(","));
		return tempTeamDetail;
	}

	public String checkDueDatesTab(List<CaseDetails> caseDetailsList) throws CMSException {
		String message = "";

		try {
			TeamDetails teamDetail = null;
			CaseDetails caseDetails = null;
			List<TeamDetails> teamDetails = null;
			boolean holiDayCheck = false;
			boolean weekendCheck = false;
			boolean consecutiveBreakCheck = false;
			int caseCount = caseDetailsList.size();
			int teamSize = false;
			String crnMessage = "";
			HashMap<String, String> dateMap = null;
			List<String> phDateList = null;
			HashMap<Object, Object> param = null;
			Date tempDate = null;

			for (int a = 0; a < caseCount; ++a) {
				crnMessage = "";
				caseDetails = (CaseDetails) caseDetailsList.get(a);
				teamDetails = caseDetails.getTeamList();
				int teamSize = teamDetails.size();

				for (int i = 0; i < teamSize; ++i) {
					teamDetail = (TeamDetails) teamDetails.get(i);
					weekendCheck = false;
					holiDayCheck = false;
					consecutiveBreakCheck = false;
					this.cal.setTime(this.targetFormat.parse(teamDetail.getFinalDueDate()));
					if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
						weekendCheck = true;
					}

					if (!weekendCheck && teamDetail.getDueDate1().trim().length() > 0) {
						this.cal.setTime(this.targetFormat.parse(teamDetail.getDueDate1()));
						if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
							weekendCheck = true;
						}
					}

					if (!weekendCheck && teamDetail.getDueDate2().trim().length() > 0) {
						this.cal.setTime(this.targetFormat.parse(teamDetail.getDueDate2()));
						if (this.cal.get(7) == 7 || this.cal.get(7) == 1) {
							weekendCheck = true;
						}
					}

					param = new HashMap();
					param.put("FDD", teamDetail.getFinalDueDate());
					dateMap = new HashMap();
					dateMap.put("FDD", teamDetail.getFinalDueDate());
					tempDate = this.targetFormat.parse(teamDetail.getFinalDueDate());
					this.cal.setTime(tempDate);
					this.cal.add(5, -1);
					dateMap.put("FDD1", this.targetFormat.format(this.cal.getTime()));
					param.put("FDD1", this.targetFormat.format(this.cal.getTime()));
					this.cal.setTime(tempDate);
					this.cal.add(5, -2);
					dateMap.put("FDD2", this.targetFormat.format(this.cal.getTime()));
					param.put("FDD2", this.targetFormat.format(this.cal.getTime()));
					this.cal.setTime(tempDate);
					this.cal.add(5, -3);
					dateMap.put("FDD3", this.targetFormat.format(this.cal.getTime()));
					param.put("FDD3", this.targetFormat.format(this.cal.getTime()));
					param.put("IDD1", teamDetail.getDueDate1());
					dateMap.put("IDD1", teamDetail.getDueDate1());
					if (null != teamDetail.getDueDate1() && teamDetail.getDueDate1().trim().length() > 0) {
						tempDate = this.targetFormat.parse(teamDetail.getDueDate1());
						this.cal.setTime(tempDate);
						this.cal.add(5, -1);
						dateMap.put("IDD11", this.targetFormat.format(this.cal.getTime()));
						param.put("IDD11", this.targetFormat.format(this.cal.getTime()));
						this.cal.setTime(tempDate);
						this.cal.add(5, -2);
						dateMap.put("IDD12", this.targetFormat.format(this.cal.getTime()));
						param.put("IDD12", this.targetFormat.format(this.cal.getTime()));
						this.cal.setTime(tempDate);
						this.cal.add(5, -3);
						dateMap.put("IDD13", this.targetFormat.format(this.cal.getTime()));
						param.put("IDD13", this.targetFormat.format(this.cal.getTime()));
					} else {
						param.put("IDD11", "");
						param.put("IDD12", "");
						param.put("IDD13", "");
					}

					param.put("IDD2", teamDetail.getDueDate2());
					dateMap.put("IDD2", teamDetail.getDueDate2());
					if (null != teamDetail.getDueDate2() && teamDetail.getDueDate2().trim().length() > 0) {
						tempDate = this.targetFormat.parse(teamDetail.getDueDate2());
						this.cal.setTime(tempDate);
						this.cal.add(5, -1);
						dateMap.put("IDD21", this.targetFormat.format(this.cal.getTime()));
						param.put("IDD21", this.targetFormat.format(this.cal.getTime()));
						this.cal.setTime(tempDate);
						this.cal.add(5, -2);
						dateMap.put("IDD22", this.targetFormat.format(this.cal.getTime()));
						param.put("IDD22", this.targetFormat.format(this.cal.getTime()));
						this.cal.setTime(tempDate);
						this.cal.add(5, -3);
						dateMap.put("IDD23", this.targetFormat.format(this.cal.getTime()));
						param.put("IDD23", this.targetFormat.format(this.cal.getTime()));
					} else {
						param.put("IDD21", "");
						param.put("IDD22", "");
						param.put("IDD23", "");
					}

					this.logger.debug("dateList size :: " + dateMap.size() + " :: parma map size :: " + param.size());
					if (Integer.parseInt(teamDetail.getTeamTypeId()) != 3
							&& Integer.parseInt(teamDetail.getTeamTypeId()) != 4) {
						param.put("officeId", teamDetail.getOffice());
						phDateList = this.officeAssignmentDAO.checkForPSTHoliday(param);
					} else {
						param.put("manager", teamDetail.getOffice());
						phDateList = this.officeAssignmentDAO.checkForBIVTHoliday(param);
					}

					if (null != phDateList && phDateList.size() > 0) {
						this.logger.debug("phDateList size :: " + phDateList.size());

						for (int k = 0; k < phDateList.size(); ++k) {
							String tempPhDate = (String) phDateList.get(k);
							if (this.targetFormat.parse(tempPhDate)
									.equals(this.targetFormat.parse((String) dateMap.get("FDD")))) {
								holiDayCheck = true;
								break;
							}

							if (((String) dateMap.get("IDD1")).trim().length() > 0
									&& this.targetFormat.parse(tempPhDate)
											.equals(this.targetFormat.parse((String) dateMap.get("IDD1")))
									|| ((String) dateMap.get("IDD2")).trim().length() > 0
											&& this.targetFormat.parse(tempPhDate)
													.equals(this.targetFormat.parse((String) dateMap.get("IDD2")))) {
								holiDayCheck = true;
								break;
							}

							if (k + 2 <= phDateList.size() - 1) {
								if (this.targetFormat.parse(tempPhDate)
										.equals(this.targetFormat.parse((String) dateMap.get("FDD3")))
										&& this.targetFormat.parse((String) phDateList.get(k + 1))
												.equals(this.targetFormat.parse((String) dateMap.get("FDD2")))
										&& this.targetFormat.parse((String) phDateList.get(k + 2))
												.equals(this.targetFormat.parse((String) dateMap.get("FDD1")))) {
									consecutiveBreakCheck = true;
									break;
								}

								if (null != teamDetail.getDueDate1() && teamDetail.getDueDate1().trim().length() > 0
										&& this.targetFormat.parse(tempPhDate)
												.equals(this.targetFormat.parse((String) dateMap.get("IDD13")))
										&& this.targetFormat.parse((String) phDateList.get(k + 1))
												.equals(this.targetFormat.parse((String) dateMap.get("IDD12")))
										&& this.targetFormat.parse((String) phDateList.get(k + 2))
												.equals(this.targetFormat.parse((String) dateMap.get("IDD11")))) {
									consecutiveBreakCheck = true;
									break;
								}

								if (null != teamDetail.getDueDate2() && teamDetail.getDueDate2().trim().length() > 0
										&& this.targetFormat.parse(tempPhDate)
												.equals(this.targetFormat.parse((String) dateMap.get("IDD23")))
										&& this.targetFormat.parse((String) phDateList.get(k + 1))
												.equals(this.targetFormat.parse((String) dateMap.get("IDD22")))
										&& this.targetFormat.parse((String) phDateList.get(k + 2))
												.equals(this.targetFormat.parse((String) dateMap.get("IDD21")))) {
									consecutiveBreakCheck = true;
									break;
								}
							}
						}
					}

					if (weekendCheck && holiDayCheck) {
						if (i == 0) {
							crnMessage = crnMessage
									+ "Primary Team's Due Date falls on Public Holiday and weekend.<br>";
						} else {
							crnMessage = crnMessage + "Supporting #" + i
									+ " Team's Due Date falls on Public Holiday and weekend.<br>";
						}
					} else if (holiDayCheck) {
						if (i == 0) {
							crnMessage = crnMessage + "Primary Team's Due Date falls on Public Holiday.<br>";
						} else {
							crnMessage = crnMessage + "Supporting #" + i
									+ " Team's Due Date falls on Public Holiday.<br>";
						}
					} else if (weekendCheck) {
						if (i == 0) {
							crnMessage = crnMessage + "Primary Team's Due Date falls on weekend.<br>";
						} else {
							crnMessage = crnMessage + "Supporting #" + i + " Team's Due Date falls on weekend.<br>";
						}
					} else if (!holiDayCheck && !weekendCheck && consecutiveBreakCheck) {
						if (i == 0) {
							crnMessage = crnMessage
									+ "Primary Team's Due Date falls right after at least 3 days of consecutive break.<br>";
						} else {
							crnMessage = crnMessage + "Supporting #" + i
									+ " Team's Due Date falls right after at least 3 days of consecutive break.<br>";
						}
					}
				}

				if (crnMessage.trim().length() > 0) {
					message = message + "<br> CRN :" + caseDetails.getCrn() + "<br>" + crnMessage;
				}
			}

			return message;
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public boolean resetTeamOfficeDetails(TeamDetails teamDetails) throws CMSException {
		this.logger.debug("in resetTeamOfficeDetails");
		boolean update = false;

		try {
			HashMap<String, Object> param = null;
			param = new HashMap();
			param.put("crn", teamDetails.getCrn());
			param.put("team_ID", teamDetails.getTeamId());
			this.officeAssignmentDAO.deleteReMappingForTeam(param);
			this.officeAssignmentDAO.resetTeamOfficeDetails(param);
			List<SubTeamReMapVO> teamReDetailsList = teamDetails.getTeamSubjectREDetails();
			SubTeamReMapVO vo = null;

			for (int i = 0; i < teamReDetailsList.size(); ++i) {
				vo = (SubTeamReMapVO) teamReDetailsList.get(i);
				this.officeAssignmentDAO.addTeamREDetails(vo);
			}

			update = true;
			return update;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public boolean resetTeamResearchCMPDates(String crn, HashMap<String, String> teamProcessMapDetails)
			throws CMSException {
		this.logger.debug("in resetTeamResearchCMPDates for crn :: " + crn);
		boolean update = false;

		try {
			HashMap<String, String> param = null;
			Set<String> hashSet = teamProcessMapDetails.keySet();
			Iterator i$ = hashSet.iterator();

			while (i$.hasNext()) {
				String key = (String) i$.next();
				update = false;
				param = new HashMap();
				String cycleName = (String) teamProcessMapDetails.get(key);
				String teamId = key.split("#")[1];
				param.put("crn", crn);
				param.put(this.CYCLE_NAME, cycleName);
				param.put("team_ID", teamId);
				int updateCount = this.officeAssignmentDAO.resetTeamResearchCMPDates(param);
				if (updateCount > 0) {
					update = true;
				}
			}

			return update;
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public int updatePTDueDates(String crn, String rInterim1, String rInterim2, String finalDueDate, String userId)
			throws CMSException {
		int updateCount = false;
		this.logger.debug(" in updatePTDueDates for :: " + crn + " rInterim1 " + rInterim1 + " rInterim2 " + rInterim2
				+ " finalDueDate " + finalDueDate);

		try {
			SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
			TeamDetails teamDetails = new TeamDetails();
			teamDetails.setCrn(crn);
			Date temp = null;
			if (rInterim1 != null) {
				temp = sourceFormat.parse(rInterim1);
				teamDetails.setDueDate1(this.targetFormat.format(temp));
			}

			if (rInterim2 != null) {
				temp = sourceFormat.parse(rInterim2);
				teamDetails.setDueDate2(this.targetFormat.format(temp));
			}

			temp = sourceFormat.parse(finalDueDate);
			teamDetails.setFinalDueDate(this.targetFormat.format(temp));
			this.logger.debug("team dates final :: " + teamDetails.getFinalDueDate() + " :: I1 :: "
					+ teamDetails.getDueDate1() + " :: I2 :: " + teamDetails.getDueDate2());
			teamDetails.setUpdatedBy(userId);
			int updateCount = this.officeAssignmentDAO.updatePTDueDates(teamDetails);
			this.logger.debug("updateCount ::   " + updateCount);
			return updateCount;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<TeamDetails> getPTMainAnalystForCases(List<String> crnDetails) throws CMSException {
		HashMap<String, Object> param = new HashMap();
		param.put(this.CRN_LIST, crnDetails);
		return this.officeAssignmentDAO.getPTMainAnalystForCases(param);
	}

	public HashMap<String, CaseDetails> addOfficeDetailsForNewSubject(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("in addOfficeDetailsForNewSubject");
		HashMap caseInfo = null;

		try {
			List<TeamDetails> teamDetails = null;
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(caseDetails.getCrn());
			this.logger.debug("oldTeamDetails size:: " + teamDetails.size());
			CaseDetails newCaseDetail = new CaseDetails();
			newCaseDetail.setCrn(caseDetails.getCrn());
			TeamDetails biTeam = null;
			TeamDetails firstSupportingTeam = null;
			if (null != caseDetails.getTeamList() && caseDetails.getTeamList().size() > 0) {
				List<TeamTypeVO> teamTypeList = this.officeAssignmentDAO.getTeamTypes();
				biTeam = new TeamDetails();
				this.createBITeam(caseDetails, biTeam, teamTypeList);
				this.addBIDueDates(teamDetails, biTeam);
				int biTeamId = this.officeAssignmentDAO.addTeamDetails(biTeam);
				this.logger.debug("biTeamId :: " + biTeamId);
				biTeam.setTeamId(biTeamId);
			}

			caseDetails.setTeamList(teamDetails);
			SubjectDetails subject = (SubjectDetails) caseDetails.getSubjectList().get(0);
			this.logger.debug("subject name :: " + subject.getSubjectName() + " :: id :: " + subject.getSubjectId()
					+ " :: is primary :: " + subject.isPrimarySub());
			List<SubTeamReMapVO> priSubjectDetails = null;
			if (!subject.isPrimarySub()) {
				priSubjectDetails = this.officeAssignmentDAO.getPrimarySubjectAssignmentDetails(caseDetails.getCrn());
				this.logger.debug(" priSubjectDetails size :: " + priSubjectDetails.size());
			} else {
				this.logger.debug("primary subject updation");
			}

			SubTeamReMapVO vo = null;
			SubjectDetails tempSubject = this.getSubjectREDetails(subject);
			List<ResearchElementMasterVO> reList = tempSubject.getReList();
			boolean assign = false;

			for (int i = 0; i < reList.size(); ++i) {
				assign = false;
				ResearchElementMasterVO reVo = (ResearchElementMasterVO) reList.get(i);
				vo = new SubTeamReMapVO();
				vo.setSubjectId(String.valueOf(subject.getSubjectId()));
				vo.setCrn(subject.getCrn());
				vo.setReId(String.valueOf(reVo.getrEMasterId()));
				vo.setJlpPoints(reVo.getPoints());
				int k;
				if (!subject.isPrimarySub()) {
					for (k = 0; k < priSubjectDetails.size(); ++k) {
						if (Integer.parseInt(((SubTeamReMapVO) priSubjectDetails.get(k)).getReId()) == reVo
								.getrEMasterId()
								|| ((SubTeamReMapVO) priSubjectDetails.get(k)).getReName()
										.equalsIgnoreCase(reVo.getResearchElementName())) {
							vo.setPerformer(((SubTeamReMapVO) priSubjectDetails.get(k)).getPerformer());
							vo.setTeamId(((SubTeamReMapVO) priSubjectDetails.get(k)).getTeamId());
							assign = true;
							break;
						}
					}
				}

				if (assign) {
					this.logger.debug("RE present in primary subject : " + reVo.getrEMasterId());
				} else if (null != reVo.getBiTeam() && reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
					if (null == biTeam) {
						for (k = 0; k < teamDetails.size(); ++k) {
							if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.BI)) {
								biTeam = (TeamDetails) teamDetails.get(k);
								break;
							}
						}
					}

					vo.setPerformer(biTeam.getManagerName());
					vo.setTeamId(biTeam.getTeamId());
				} else if (null != reVo.getSupportingVendorTeam()
						&& reVo.getSupportingVendorTeam().equalsIgnoreCase(this.YES)) {
					if (null == firstSupportingTeam) {
						for (k = 0; k < teamDetails.size(); ++k) {
							if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.INTERNAL)
									|| ((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.VENDOR)) {
								firstSupportingTeam = (TeamDetails) teamDetails.get(k);
								break;
							}
						}
					}

					if (null == firstSupportingTeam) {
						firstSupportingTeam = (TeamDetails) teamDetails.get(0);
					}

					if (firstSupportingTeam.getTeamType().contains(this.VENDOR)) {
						vo.setPerformer(firstSupportingTeam.getManagerName());
					} else {
						vo.setPerformer(firstSupportingTeam.getMainAnalyst());
					}

					vo.setTeamId(firstSupportingTeam.getTeamId());
				} else {
					vo.setPerformer(((TeamDetails) teamDetails.get(0)).getMainAnalyst());
					vo.setTeamId(((TeamDetails) teamDetails.get(0)).getTeamId());
				}

				vo.setUpdatedBy(caseDetails.getUpdatedBy());
				k = this.officeAssignmentDAO.addTeamREDetails(vo);
				vo.setSubTeamReMapId(k);
			}

			this.logger.debug("subject RE assignment done successfully.");
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(newCaseDetail.getCrn());
			this.logger.debug("newTeamDetails size:: " + teamDetails.size());
			newCaseDetail.setTeamList(teamDetails);
			caseInfo = new HashMap();
			caseInfo.put("oldCaseDetails", caseDetails);
			caseInfo.put("newCaseDetails", newCaseDetail);
			return caseInfo;
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	private void addBIDueDates(List<TeamDetails> teamDetails, TeamDetails biTeam) throws ParseException {
		String ptFinalDueDate = ((TeamDetails) teamDetails.get(0)).getFinalDueDate();
		this.cal.setTime(this.targetFormat.parse(ptFinalDueDate));
		this.cal.add(5, -1);
		Date temp = this.cal.getTime();
		if (this.cal.get(7) == 7) {
			this.cal.add(5, -1);
			temp = this.cal.getTime();
		} else if (this.cal.get(7) == 1) {
			this.cal.add(5, -2);
			temp = this.cal.getTime();
		}

		if (temp.compareTo(new Date()) > 0) {
			biTeam.setFinalDueDate(this.targetFormat.format(temp));
		} else {
			biTeam.setFinalDueDate(this.targetFormat.format(new Date()));
		}

	}

	private void createBITeam(CaseDetails caseDetails, TeamDetails biTeam, List<TeamTypeVO> teamTypeList) {
		biTeam.setCrn(caseDetails.getCrn());
		biTeam.setUpdatedBy(caseDetails.getUpdatedBy());
		biTeam.setManagerName(((TeamDetails) caseDetails.getTeamList().get(0)).getManagerName());
		biTeam.setByPassInterim("1");
		TeamTypeVO teamType = null;

		for (int x = 0; x < teamTypeList.size(); ++x) {
			teamType = (TeamTypeVO) teamTypeList.get(x);
			if (teamType.getTeamType().contains(this.BI)) {
				biTeam.setTeamTypeId(teamType.getTeamTypeId());
				biTeam.setTeamType(teamType.getTeamType());
				break;
			}
		}

	}

	public List<SubTeamReMapVO> getSubjectAssignmentDetails(SubjectDetails subjectVo) throws CMSException {
		return this.officeAssignmentDAO.getSubjectAsisgnmentDetails(subjectVo);
	}

	public HashMap<String, CaseDetails> updateOfficeDetailsForOldSubject(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("in updateOfficeDetailsForOldSubject");
		HashMap caseInfo = null;

		try {
			List<TeamDetails> teamDetails = null;
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(caseDetails.getCrn());
			this.logger.debug("oldTeamDetails size:: " + teamDetails.size());
			CaseDetails newCaseDetail = new CaseDetails();
			newCaseDetail.setCrn(caseDetails.getCrn());
			TeamDetails biTeam = null;
			TeamDetails firstSupportingTeam = null;
			if (null != caseDetails.getTeamList() && caseDetails.getTeamList().size() > 0) {
				List<TeamTypeVO> teamTypeList = this.officeAssignmentDAO.getTeamTypes();
				biTeam = new TeamDetails();
				this.createBITeam(caseDetails, biTeam, teamTypeList);
				this.addBIDueDates(teamDetails, biTeam);
				int biTeamId = this.officeAssignmentDAO.addTeamDetails(biTeam);
				this.logger.debug("biTeamId :: " + biTeamId);
				biTeam.setTeamId(biTeamId);
			}

			caseDetails.setTeamList(teamDetails);
			SubjectDetails subject = (SubjectDetails) caseDetails.getSubjectList().get(0);
			this.logger.debug("subject name :: " + subject.getSubjectName() + " :: id :: " + subject.getSubjectId()
					+ " :: is primary :: " + subject.isPrimarySub());
			List<SubTeamReMapVO> priSubjectDetails = null;
			if (!subject.isPrimarySub()) {
				priSubjectDetails = this.officeAssignmentDAO.getPrimarySubjectAssignmentDetails(caseDetails.getCrn());
				this.logger.debug(" priSubjectDetails size :: " + priSubjectDetails.size());
			} else {
				this.logger.debug("primary subject updation");
			}

			List<SubTeamReMapVO> subjectAssignmentDetails = this.officeAssignmentDAO
					.getSubjectAsisgnmentDetails(subject);
			SubTeamReMapVO vo = null;
			SubjectDetails tempSubject = this.getSubjectREDetails(subject);
			List<ResearchElementMasterVO> reList = tempSubject.getReList();
			this.logger.debug("subjectAssignmentDetails size :: " + subjectAssignmentDetails.size()
					+ " :: re list size :: " + reList.size());
			List<Integer> subjectAssignHashCodeList = new ArrayList();

			for (int j = 0; j < subjectAssignmentDetails.size(); ++j) {
				vo = (SubTeamReMapVO) subjectAssignmentDetails.get(j);
				subjectAssignHashCodeList.add(vo.hashCodeForRE());
			}

			this.logger.debug(" subjectAssignHashCodeList size :: " + subjectAssignHashCodeList.size());
			boolean assign = false;
			ResearchElementMasterVO reVo = null;

			int k;
			for (int i = 0; i < reList.size(); ++i) {
				assign = false;
				reVo = (ResearchElementMasterVO) reList.get(i);
				vo = new SubTeamReMapVO();
				vo.setReId(String.valueOf(reVo.getrEMasterId()));
				if (subjectAssignHashCodeList.contains(vo.hashCodeForRE())) {
					this.logger.debug("RE already assigned :: " + reVo.getrEMasterId());
					assign = true;
				} else {
					this.logger.debug(" assigning team to RE :: " + reVo.getrEMasterId());
					vo.setSubjectId(String.valueOf(subject.getSubjectId()));
					vo.setCrn(subject.getCrn());
					vo.setJlpPoints(reVo.getPoints());
					if (!subject.isPrimarySub()) {
						for (k = 0; k < priSubjectDetails.size(); ++k) {
							if (Integer.parseInt(((SubTeamReMapVO) priSubjectDetails.get(k)).getReId()) == reVo
									.getrEMasterId()
									|| ((SubTeamReMapVO) priSubjectDetails.get(k)).getReName()
											.equalsIgnoreCase(reVo.getResearchElementName())) {
								vo.setPerformer(((SubTeamReMapVO) priSubjectDetails.get(k)).getPerformer());
								vo.setTeamId(((SubTeamReMapVO) priSubjectDetails.get(k)).getTeamId());
								assign = true;
								break;
							}
						}
					}

					if (assign) {
						this.logger.debug("RE already assigned to team :: " + reVo.getrEMasterId());
					} else if (null != reVo.getBiTeam() && reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
						if (null == biTeam) {
							for (k = 0; k < teamDetails.size(); ++k) {
								if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.BI)) {
									biTeam = (TeamDetails) teamDetails.get(k);
									break;
								}
							}
						}

						vo.setPerformer(biTeam.getManagerName());
						vo.setTeamId(biTeam.getTeamId());
					} else if (null != reVo.getSupportingVendorTeam()
							&& reVo.getSupportingVendorTeam().equalsIgnoreCase(this.YES)) {
						if (null == firstSupportingTeam) {
							for (k = 0; k < teamDetails.size(); ++k) {
								if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.INTERNAL)
										|| ((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.VENDOR)) {
									firstSupportingTeam = (TeamDetails) teamDetails.get(k);
									break;
								}
							}
						}

						if (null == firstSupportingTeam) {
							firstSupportingTeam = (TeamDetails) teamDetails.get(0);
						}

						if (firstSupportingTeam.getTeamType().contains(this.VENDOR)) {
							vo.setPerformer(firstSupportingTeam.getManagerName());
						} else {
							vo.setPerformer(firstSupportingTeam.getMainAnalyst());
						}

						vo.setTeamId(firstSupportingTeam.getTeamId());
					} else {
						vo.setPerformer(((TeamDetails) teamDetails.get(0)).getMainAnalyst());
						vo.setTeamId(((TeamDetails) teamDetails.get(0)).getTeamId());
					}

					vo.setUpdatedBy(caseDetails.getUpdatedBy());
					k = this.officeAssignmentDAO.addTeamREDetails(vo);
					vo.setSubTeamReMapId(k);
				}
			}

			HashMap<Object, Object> param = new HashMap();
			param.put(this.SUBJECT_ID, subject.getSubjectId());
			param.put(this.RE_IDS, subject.getReIds());
			param.put("crn", subject.getCrn());
			param.put(this.TEAM_ID, (Object) null);
			k = this.officeAssignmentDAO.deleteSubjectREMappingForTeam(param);
			this.logger.debug("reMapDeletedCount records i.e; no. of res removed from the subject :: " + k);
			this.logger.debug("subject RE assignment done successfully.");
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(newCaseDetail.getCrn());
			this.logger.debug("newTeamDetails size:: " + teamDetails.size());
			newCaseDetail.setTeamList(teamDetails);
			caseInfo = new HashMap();
			caseInfo.put("oldCaseDetails", caseDetails);
			caseInfo.put("newCaseDetails", newCaseDetail);
			return caseInfo;
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	public HashMap<String, CaseDetails> deleteOfficeDetailsForSubject(SubjectDetails subjectDetailsVO)
			throws CMSException {
		this.logger.debug("in deleteOfficeDetailsForSubject");
		HashMap caseInfo = null;

		try {
			List<TeamDetails> teamDetails = null;
			CaseDetails oldCaseDetail = new CaseDetails();
			CaseDetails newCaseDetail = new CaseDetails();
			oldCaseDetail.setCrn(subjectDetailsVO.getCrn());
			newCaseDetail.setCrn(subjectDetailsVO.getCrn());
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(oldCaseDetail.getCrn());
			this.logger.debug("oldTeamDetails size:: " + teamDetails.size());
			oldCaseDetail.setTeamList(teamDetails);
			int count = this.officeAssignmentDAO.deleteSubjectOfficeDetails(subjectDetailsVO);
			this.logger.debug(" delete count :: " + count);
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(newCaseDetail.getCrn());
			this.logger.debug("newTeamDetails size:: " + teamDetails.size());
			newCaseDetail.setTeamList(teamDetails);
			caseInfo = new HashMap();
			caseInfo.put("oldCaseDetails", oldCaseDetail);
			caseInfo.put("newCaseDetails", newCaseDetail);
			return caseInfo;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public CaseDetails deleteBITeamForCase(String crn) throws CMSException {
		this.logger.debug("in deleteBITeamForCase");
		CaseDetails caseDetails = new CaseDetails();
		caseDetails.setCrn(crn);
		List<TeamDetails> teamDetails = null;
		teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(crn);
		this.logger.debug("oldTeamDetails size:: " + teamDetails.size());
		int count = this.officeAssignmentDAO.deleteBITeamForCase(crn);
		this.logger.debug("BI team delete count :: " + count);
		caseDetails.setTeamList(teamDetails);
		return caseDetails;
	}

	public boolean isBITeamExistsForCase(String crn) throws CMSException {
		this.logger.debug("in isBITeamExistsForCase");
		int count = this.officeAssignmentDAO.isBITeamExistsForCase(crn);
		this.logger.debug("BI team count :: " + count);
		return count > 0;
	}

	public void addInterimCycleToPrimaryTeam(CaseDetails caseDetails, String param) throws CMSException {
		if (null != param && param.trim().length() > 0) {
			if (param.equalsIgnoreCase("interim1")) {
				this.officeAssignmentDAO.addInterim1CycleToPrimary(caseDetails);
			} else if (param.equalsIgnoreCase("interim2")) {
				this.officeAssignmentDAO.addInterim2CycleToPrimary(caseDetails);
			} else {
				this.officeAssignmentDAO.addInterimCyclesToPrimary(caseDetails);
			}
		}

	}

	public void addOnlyOfficeDetailsForNewSubject(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("in addOnlyOfficeDetailsForNewSubject");

		try {
			List<TeamDetails> teamDetails = null;
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(caseDetails.getCrn());
			this.logger.debug("oldTeamDetails size:: " + teamDetails.size());
			TeamDetails biTeam = null;
			TeamDetails firstSupportingTeam = null;
			if (null != caseDetails.getTeamList() && caseDetails.getTeamList().size() > 0) {
				List<TeamTypeVO> teamTypeList = this.officeAssignmentDAO.getTeamTypes();
				biTeam = new TeamDetails();
				this.createBITeam(caseDetails, biTeam, teamTypeList);
				this.addBIDueDates(teamDetails, biTeam);
				int biTeamId = this.officeAssignmentDAO.addTeamDetails(biTeam);
				this.logger.debug("biTeamId :: " + biTeamId);
				biTeam.setTeamId(biTeamId);
			}

			caseDetails.setTeamList(teamDetails);
			SubjectDetails subject = (SubjectDetails) caseDetails.getSubjectList().get(0);
			this.logger.debug("subject name :: " + subject.getSubjectName() + " :: id :: " + subject.getSubjectId()
					+ " :: is primary :: " + subject.isPrimarySub());
			List<SubTeamReMapVO> priSubjectDetails = null;
			if (!subject.isPrimarySub()) {
				priSubjectDetails = this.officeAssignmentDAO.getPrimarySubjectAssignmentDetails(caseDetails.getCrn());
				this.logger.debug(" priSubjectDetails size :: " + priSubjectDetails.size());
			} else {
				this.logger.debug("primary subject updation");
			}

			SubTeamReMapVO vo = null;
			SubjectDetails tempSubject = this.getSubjectREDetails(subject);
			List<ResearchElementMasterVO> reList = tempSubject.getReList();
			boolean assign = false;

			for (int i = 0; i < reList.size(); ++i) {
				assign = false;
				ResearchElementMasterVO reVo = (ResearchElementMasterVO) reList.get(i);
				vo = new SubTeamReMapVO();
				vo.setSubjectId(String.valueOf(subject.getSubjectId()));
				vo.setCrn(subject.getCrn());
				vo.setReId(String.valueOf(reVo.getrEMasterId()));
				vo.setJlpPoints(reVo.getPoints());
				int k;
				if (!subject.isPrimarySub()) {
					for (k = 0; k < priSubjectDetails.size(); ++k) {
						if (Integer.parseInt(((SubTeamReMapVO) priSubjectDetails.get(k)).getReId()) == reVo
								.getrEMasterId()
								|| ((SubTeamReMapVO) priSubjectDetails.get(k)).getReName()
										.equalsIgnoreCase(reVo.getResearchElementName())) {
							vo.setPerformer(((SubTeamReMapVO) priSubjectDetails.get(k)).getPerformer());
							vo.setTeamId(((SubTeamReMapVO) priSubjectDetails.get(k)).getTeamId());
							assign = true;
							break;
						}
					}
				}

				if (assign) {
					this.logger.debug("RE present in primary subject : " + reVo.getrEMasterId());
				} else if (null != reVo.getBiTeam() && reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
					if (null == biTeam) {
						for (k = 0; k < teamDetails.size(); ++k) {
							if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.BI)) {
								biTeam = (TeamDetails) teamDetails.get(k);
								break;
							}
						}
					}

					vo.setPerformer(biTeam.getManagerName());
					vo.setTeamId(biTeam.getTeamId());
				} else if (null != reVo.getSupportingVendorTeam()
						&& reVo.getSupportingVendorTeam().equalsIgnoreCase(this.YES)) {
					if (null == firstSupportingTeam) {
						for (k = 0; k < teamDetails.size(); ++k) {
							if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.INTERNAL)
									|| ((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.VENDOR)) {
								firstSupportingTeam = (TeamDetails) teamDetails.get(k);
								break;
							}
						}
					}

					if (null == firstSupportingTeam) {
						firstSupportingTeam = (TeamDetails) teamDetails.get(0);
					}

					if (firstSupportingTeam.getTeamType().contains(this.VENDOR)) {
						vo.setPerformer(firstSupportingTeam.getManagerName());
					} else {
						vo.setPerformer(firstSupportingTeam.getMainAnalyst());
					}

					vo.setTeamId(firstSupportingTeam.getTeamId());
				} else {
					vo.setPerformer(((TeamDetails) teamDetails.get(0)).getMainAnalyst());
					vo.setTeamId(((TeamDetails) teamDetails.get(0)).getTeamId());
				}

				vo.setUpdatedBy(caseDetails.getUpdatedBy());
				k = this.officeAssignmentDAO.addTeamREDetails(vo);
				vo.setSubTeamReMapId(k);
			}

			this.logger.debug("subject RE assignment done successfully.");
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		}
	}

	public void updateOnlyOfficeDetailsForOldSubject(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("in updateOnlyOfficeDetailsForOldSubject");

		try {
			List<TeamDetails> teamDetails = null;
			teamDetails = this.officeAssignmentDAO.getCompleteTeamDetails(caseDetails.getCrn());
			this.logger.debug("oldTeamDetails size:: " + teamDetails.size());
			TeamDetails biTeam = null;
			TeamDetails firstSupportingTeam = null;
			if (null != caseDetails.getTeamList() && caseDetails.getTeamList().size() > 0) {
				List<TeamTypeVO> teamTypeList = this.officeAssignmentDAO.getTeamTypes();
				biTeam = new TeamDetails();
				this.createBITeam(caseDetails, biTeam, teamTypeList);
				this.addBIDueDates(teamDetails, biTeam);
				int biTeamId = this.officeAssignmentDAO.addTeamDetails(biTeam);
				this.logger.debug("biTeamId :: " + biTeamId);
				biTeam.setTeamId(biTeamId);
			}

			caseDetails.setTeamList(teamDetails);
			SubjectDetails subject = (SubjectDetails) caseDetails.getSubjectList().get(0);
			this.logger.debug("subject name :: " + subject.getSubjectName() + " :: id :: " + subject.getSubjectId()
					+ " :: is primary :: " + subject.isPrimarySub());
			List<SubTeamReMapVO> priSubjectDetails = null;
			if (!subject.isPrimarySub()) {
				priSubjectDetails = this.officeAssignmentDAO.getPrimarySubjectAssignmentDetails(caseDetails.getCrn());
				this.logger.debug(" priSubjectDetails size :: " + priSubjectDetails.size());
			} else {
				this.logger.debug("primary subject updation");
			}

			List<SubTeamReMapVO> subjectAssignmentDetails = this.officeAssignmentDAO
					.getSubjectAsisgnmentDetails(subject);
			SubTeamReMapVO vo = null;
			SubjectDetails tempSubject = this.getSubjectREDetails(subject);
			List<ResearchElementMasterVO> reList = tempSubject.getReList();
			this.logger.debug("subjectAssignmentDetails size :: " + subjectAssignmentDetails.size()
					+ " :: re list size :: " + reList.size());
			List<Integer> subjectAssignHashCodeList = new ArrayList();

			for (int j = 0; j < subjectAssignmentDetails.size(); ++j) {
				vo = (SubTeamReMapVO) subjectAssignmentDetails.get(j);
				subjectAssignHashCodeList.add(vo.hashCodeForRE());
			}

			this.logger.debug(" subjectAssignHashCodeList size :: " + subjectAssignHashCodeList.size());
			boolean assign = false;
			ResearchElementMasterVO reVo = null;

			int k;
			for (int i = 0; i < reList.size(); ++i) {
				assign = false;
				reVo = (ResearchElementMasterVO) reList.get(i);
				vo = new SubTeamReMapVO();
				vo.setReId(String.valueOf(reVo.getrEMasterId()));
				if (subjectAssignHashCodeList.contains(vo.hashCodeForRE())) {
					this.logger.debug("RE already assigned :: " + reVo.getrEMasterId());
					assign = true;
				} else {
					this.logger.debug(" assigning team to RE :: " + reVo.getrEMasterId());
					vo.setSubjectId(String.valueOf(subject.getSubjectId()));
					vo.setCrn(subject.getCrn());
					vo.setJlpPoints(reVo.getPoints());
					if (!subject.isPrimarySub()) {
						for (k = 0; k < priSubjectDetails.size(); ++k) {
							if (Integer.parseInt(((SubTeamReMapVO) priSubjectDetails.get(k)).getReId()) == reVo
									.getrEMasterId()
									|| ((SubTeamReMapVO) priSubjectDetails.get(k)).getReName()
											.equalsIgnoreCase(reVo.getResearchElementName())) {
								vo.setPerformer(((SubTeamReMapVO) priSubjectDetails.get(k)).getPerformer());
								vo.setTeamId(((SubTeamReMapVO) priSubjectDetails.get(k)).getTeamId());
								assign = true;
								break;
							}
						}
					}

					if (assign) {
						this.logger.debug("RE already assigned to team :: " + reVo.getrEMasterId());
					} else if (null != reVo.getBiTeam() && reVo.getBiTeam().equalsIgnoreCase(this.YES)) {
						if (null == biTeam) {
							for (k = 0; k < teamDetails.size(); ++k) {
								if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.BI)) {
									biTeam = (TeamDetails) teamDetails.get(k);
									break;
								}
							}
						}

						vo.setPerformer(biTeam.getManagerName());
						vo.setTeamId(biTeam.getTeamId());
					} else if (null != reVo.getSupportingVendorTeam()
							&& reVo.getSupportingVendorTeam().equalsIgnoreCase(this.YES)) {
						if (null == firstSupportingTeam) {
							for (k = 0; k < teamDetails.size(); ++k) {
								if (((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.INTERNAL)
										|| ((TeamDetails) teamDetails.get(k)).getTeamType().contains(this.VENDOR)) {
									firstSupportingTeam = (TeamDetails) teamDetails.get(k);
									break;
								}
							}
						}

						if (null == firstSupportingTeam) {
							firstSupportingTeam = (TeamDetails) teamDetails.get(0);
						}

						if (firstSupportingTeam.getTeamType().contains(this.VENDOR)) {
							vo.setPerformer(firstSupportingTeam.getManagerName());
						} else {
							vo.setPerformer(firstSupportingTeam.getMainAnalyst());
						}

						vo.setTeamId(firstSupportingTeam.getTeamId());
					} else {
						vo.setPerformer(((TeamDetails) teamDetails.get(0)).getMainAnalyst());
						vo.setTeamId(((TeamDetails) teamDetails.get(0)).getTeamId());
					}

					vo.setUpdatedBy(caseDetails.getUpdatedBy());
					k = this.officeAssignmentDAO.addTeamREDetails(vo);
					vo.setSubTeamReMapId(k);
				}
			}

			HashMap<Object, Object> param = new HashMap();
			param.put(this.SUBJECT_ID, subject.getSubjectId());
			param.put(this.RE_IDS, subject.getReIds());
			param.put("crn", subject.getCrn());
			param.put(this.TEAM_ID, (Object) null);
			k = this.officeAssignmentDAO.deleteSubjectREMappingForTeam(param);
			this.logger.debug("reMapDeletedCount records i.e; no. of res removed from the subject :: " + k);
			this.logger.debug("subject RE assignment done successfully.");
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public boolean completeOAForISISCase(String crn, String pid, String userId, String ptResearchHead)
			throws CMSException {
		this.logger.debug("in completeOAForISISCase :: crn :: " + crn + " :: pid :: " + pid + " :: userId :: "
				+ " ::pt RH :: " + ptResearchHead);
		boolean oaCompleted = false;

		try {
			TeamDetails officeDetails = this.officeAssignmentDAO.getRHOffice(ptResearchHead);
			this.logger.debug("officeDetails :: " + officeDetails);
			if (null != officeDetails.getOffice() && officeDetails.getOffice().trim().length() > 0) {
				this.logger.debug("officeId :: " + officeDetails.getOffice() + " :: office Name :: "
						+ officeDetails.getOfficeName());
				List<SubjectDetails> subjectList = this.getCaseSubjectDetails(crn);
				List<TeamTypeVO> teamTypes = this.getTeamTypes();
				CaseDetails caseDetail = this.getCaseDueDates(crn);
				List<SubTeamReMapVO> teamSubjectREDetails = new ArrayList();
				TeamTypeVO teamType = null;
				SubTeamReMapVO vo = null;
				SubjectDetails subject = null;
				ResearchElementMasterVO reVo = null;
				TeamDetails primaryTeam = new TeamDetails();
				primaryTeam.setCrn(crn);
				primaryTeam.setResearchHead(ptResearchHead);
				primaryTeam.setOffice(officeDetails.getOffice());
				primaryTeam.setOfficeName(officeDetails.getOfficeName());
				primaryTeam.setByPassInterim("0");
				this.setPTDueDates(caseDetail, primaryTeam);

				int teamId;
				for (teamId = 0; teamId < teamTypes.size(); ++teamId) {
					teamType = (TeamTypeVO) teamTypes.get(teamId);
					if (teamType.getTeamType().contains(this.PRIMARY)) {
						primaryTeam.setTeamTypeId(teamType.getTeamTypeId());
						primaryTeam.setTeamType(teamType.getTeamType());
						break;
					}
				}

				primaryTeam.setUpdatedBy(userId);
				teamId = this.officeAssignmentDAO.addPSTOfficeDetails(primaryTeam);
				this.logger.debug("primary Team Id :: " + teamId);
				primaryTeam.setTeamId(teamId);

				for (int i = 0; i < subjectList.size(); ++i) {
					subject = (SubjectDetails) subjectList.get(i);
					List<ResearchElementMasterVO> subjectReList = subject.getReList();

					for (int j = 0; j < subjectReList.size(); ++j) {
						reVo = (ResearchElementMasterVO) subjectReList.get(j);
						vo = new SubTeamReMapVO();
						vo.setSubjectId(String.valueOf(subject.getSubjectId()));
						vo.setCrn(subject.getCrn());
						vo.setReId(String.valueOf(reVo.getrEMasterId()));
						vo.setJlpPoints(reVo.getPoints());
						vo.setTeamId(primaryTeam.getTeamId());
						vo.setUpdatedBy(userId);
						int subTeamReMapId = this.officeAssignmentDAO.addTeamREDetails(vo);
						vo.setSubTeamReMapId(subTeamReMapId);
						teamSubjectREDetails.add(vo);
					}
				}

				primaryTeam.setTeamSubjectREDetails(teamSubjectREDetails);
				caseDetail.setPid(pid);
				List<TeamDetails> teamList = new ArrayList();
				teamList.add(primaryTeam);
				caseDetail.setTeamList(teamList);
				caseDetail.setUpdatedBy(userId);
				ResourceLocator locator = ResourceLocator.self();
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setCRN(caseDetail.getCrn());
				caseHistory.setPid(pid);
				caseHistory.setProcessCycle("");
				caseHistory.setPerformer(userId);
				caseHistory.setTaskName("Office Assignment Task");
				caseHistory.setTaskStatus("");
				ResourceLocator.self().getCaseHistoryService().setCaseHistory((CaseDetails) null, caseDetail,
						caseHistory, "Auto Office Assignment");
				Session session = locator.getSBMService().getSession(userId);
				locator.getFlowService().updateDS(session, caseDetail, "Office");
				locator.getSBMService().closeSession(session);
				this.logger.debug("office assignment completed successfully for ISIS case.");
				oaCompleted = true;
			} else {
				this.logger.debug("office for the research head provided does not exists.");
				oaCompleted = false;
			}

			return oaCompleted;
		} catch (Exception var21) {
			oaCompleted = false;
			throw new CMSException(this.logger, var21);
		}
	}

	public CaseDetails getLegacyCaseDetails(String crn) throws CMSException {
		return this.officeAssignmentDAO.getLegacyCaseDetails(crn);
	}

	public List<TeamDetails> getLegacyTeamDetails(String crn) throws CMSException {
		this.logger.debug("in getTeamDetails");
		List<TeamDetails> teamDetailList = this.officeAssignmentDAO.getLegacyTeamDetails(crn);
		this.logger.debug("teamDetailList size :: " + teamDetailList.size());
		return teamDetailList;
	}

	public TeamDetails getBITeamDetails(String crn) throws CMSException {
		return this.officeAssignmentDAO.getBITeamDetails(crn);
	}

	public int updateCaseManager(String crn, String caseMgrId, String userId) throws CMSException {
		CaseDetails caseDetail = new CaseDetails();
		caseDetail.setCrn(crn);
		caseDetail.setCaseMgrId(caseMgrId);
		caseDetail.setUpdatedBy(userId);
		return this.officeAssignmentDAO.updateCaseManager(caseDetail);
	}

	public int updateClientDueDates(String crn, String clientFinalDate, String clientInterim1Date,
			String clientInterim2Date) throws CMSException {
		boolean var5 = false;

		try {
			Date temp = null;
			CaseDetails caseDetail = new CaseDetails();
			caseDetail.setCrn(crn);
			this.logger.debug("clientInterim1Date::" + clientInterim1Date);
			this.logger.debug("clientInterim2Date::" + clientInterim2Date);
			this.logger.debug("clientFinalDate::" + clientFinalDate);
			if (clientInterim1Date != null && !clientInterim1Date.equals("")) {
				temp = this.targetFormat.parse(clientInterim1Date);
				this.logger.debug("clientInterim1Date::" + temp);
				caseDetail.setcInterim1(new java.sql.Date(temp.getTime()));
			}

			if (clientInterim2Date != null && !clientInterim2Date.equals("")) {
				this.logger.debug("In If condition::" + clientInterim2Date);
				temp = this.targetFormat.parse(clientInterim2Date);
				this.logger.debug("clientInterim2Date::" + temp);
				caseDetail.setcInterim2(new java.sql.Date(temp.getTime()));
			}

			temp = this.targetFormat.parse(clientFinalDate);
			this.logger.debug("clientFinalDate::" + temp);
			caseDetail.setClientFinalDate(new java.sql.Date(temp.getTime()));
			int updateCount = this.officeAssignmentDAO.updateClientDueDates(caseDetail);
			this.logger.debug("updateCount ::   " + updateCount);
			return updateCount;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<SubjectDetails> getAssociatedSubjectsGCC(String crn) throws CMSException {
		try {
			return this.officeAssignmentDAO.getAssociatedSubjectsGCC(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getDaysBeforePT(String clientCode, String reportType, String teamType, String office)
			throws CMSException {
		return this.officeAssignmentDAO.getDaysBeforePT(clientCode, reportType, teamType, office);
	}

	public List<TeamOfficeVO> getcountryREList(String crn) throws CMSException {
		return this.officeAssignmentDAO.getcountryREList(crn);
	}
}