package com.worldcheck.atlas.frontend.teamassignment;

import com.worldcheck.atlas.bl.interfaces.ITeamAssignment;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONTeamAssignmentController extends JSONMultiActionController {
	private static final String TEAM_IDS = "teamIds";
	private static final String CASE_ASSIGNED_ANALYST_DETAILS = "caseAssignedAnalystDetails";
	private static final String CASE_STATUS = "caseStatus";
	private static final String TEAM_ASSIGNMENT_DONE = "teamAssignmentDone";
	private static final String ANALYST_LOOP = "AnalystLoop";
	private static final String ANALYST_FOR_MANAGER = "analystForManager";
	private static final String CASE_DETAILS = "caseDetails";
	private static final String MANAGER_ID = "managerId";
	private static final String OFFICE_MASTER_LIST = "OfficeMasterList";
	private static final String TEAMTYPE = "teamType";
	private static final String CRN = "crn";
	private static final String TEAM_ANALYST_MASTER_LIST = "analystMasterList";
	private static final String TEAM_REVIEWER_MASTER_LIST = "reviewerMasterList";
	private static final String TEAM_DETAILS = "teamDetails";
	private static final String COLOR_DETAILS = "colorDetails";
	private static final String TEAM_TYPE = "teamType";
	private static final String SUB_DETAILS = "subDetails";
	private static final String ANALYST_DETAILS = "analystDetails";
	private static final String ASSIGNED_USER = "assignedUser";
	private static final String SUB_RE_DETAILS = "subReDetails";
	private static final String OFFICE_DETAILS = "officeDetails";
	private static final String TEAM_SUBJECT_DETAILS = "teamSubDetails";
	private static final String TEAM_ANALYST_DETAILS = "teamAnalystDetails";
	private static final String TEAM_ANALYST_RE_DETAILS = "teamAnalystReDetails";
	private static final String REVIEW_FLAG = "reviewFlag";
	private static final String ALL_REVIEWER_MASTER_LIST = "allReviewerMasterList";
	private static final String TEAM_ASSIGNMENT_WORKITEM_NAMES = "teamAssignmentWINames";
	private String CRN_NO = "crnNo";
	private String COUNTRY = "country";
	private String CASE_MANAGER = "casemanager";
	private String Task_Name = "taskName";
	private String TRUE = "true";
	private String FALSE = "false";
	private String TEAM_IDD1 = "teamIDD1";
	private String TEAM_IDD2 = "teamIDD2";
	private String TEAM_FDD = "teamFDD";
	private String HOLIDAY = "holiday";
	private String OFFICE_ID = "officeId";
	private String DOUBLE_COMMA = ",,";
	private String SUCCESS = "success";
	private String MESSAGE = "message";
	private String BLANK = "blank";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONTeamAssignmentController");
	private ITeamAssignment teamAssignmentManager;

	public void setTeamAssignmentManager(ITeamAssignment teamAssignmentManager) {
		this.teamAssignmentManager = teamAssignmentManager;
	}

	public ModelAndView getTeamOfficeMaster(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List officeMasterList;
		try {
			this.logger.debug("team type " + request.getParameter("teamType"));
			this.logger.debug("crn " + request.getParameter("crn"));
			officeMasterList = this.teamAssignmentManager.getTeamOfficeMaster(request.getParameter("teamType"),
					request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("OfficeMasterList", officeMasterList);
		return viewForJson;
	}

	public ModelAndView getTeamAnalystMaster(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List analystMasterList;
		try {
			this.logger.debug("team type " + request.getParameter("teamType"));
			this.logger.debug("crn " + request.getParameter("crn"));
			analystMasterList = this.teamAssignmentManager.getTeamAnalystMaster(request.getParameter("teamType"),
					request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("analystMasterList", analystMasterList);
		return viewForJson;
	}

	public ModelAndView getTeamReviewerMaster(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List reviewerMasterList;
		try {
			this.logger.debug("team type " + request.getParameter("teamType"));
			this.logger.debug("crn " + request.getParameter("crn"));
			reviewerMasterList = this.teamAssignmentManager.getTeamReviewerMaster(request.getParameter("teamType"),
					request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("reviewerMasterList", reviewerMasterList);
		return viewForJson;
	}

	public ModelAndView getReviewerForAllOffice(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List reviewerMasterList;
		try {
			reviewerMasterList = this.teamAssignmentManager.getReviewerForAllOffice();
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("reviewerMasterList", reviewerMasterList);
		return viewForJson;
	}

	public ModelAndView getTeamOfficeDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		TeamDetails teamDetails = null;
		CaseDetails caseDetails = null;
		TaskColorVO colorDetails = null;

		try {
			String[] resultList;
			String subTeamReMapVOs;
			if (request.getParameter(this.Task_Name) != null) {
				resultList = request.getParameter(this.Task_Name).toString().split("%20");
				subTeamReMapVOs = resultList[0] + " " + resultList[1] + " " + resultList[2];
				colorDetails = this.teamAssignmentManager.getColorDetails(request.getParameter("crn"), subTeamReMapVOs);
				if (colorDetails != null && colorDetails.getColor() != null && colorDetails.getColor().length() != 0) {
					colorDetails.getColor();
				} else {
					colorDetails = new TaskColorVO();
					colorDetails.setColor("red");
					this.logger.debug("IN IF colorDetails.getColor():::" + colorDetails.getColor());
				}

				viewForJson.addObject("colorDetails", colorDetails);
			}

			resultList = null;
			subTeamReMapVOs = null;
			teamDetails = this.teamAssignmentManager.getTeamDetails(request.getParameter("teamType"));
			caseDetails = this.teamAssignmentManager.getCaseDetails(request.getParameter("crn"));
			List<Object> resultList = this.teamAssignmentManager.getSubjectDetails(request.getParameter("teamType"));
			boolean isReviewCompleted = ResourceLocator.self().getSBMService().isReviewCompleted(
					ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(request.getParameter("crn")),
					SBMUtils.getSession(request), request.getParameter("teamType"));
			List<SubTeamReMapVO> subTeamReMapVOs = this.teamAssignmentManager
					.getAssignedUsers(request.getParameter("crn"), request.getParameter("teamType"));
			viewForJson.addObject("teamDetails", teamDetails);
			viewForJson.addObject("caseDetails", caseDetails);
			viewForJson.addObject("subDetails", resultList.get(0));
			viewForJson.addObject("analystDetails", resultList.get(1));
			viewForJson.addObject("subReDetails", resultList.get(2));
			viewForJson.addObject("assignedUser", subTeamReMapVOs);
			viewForJson.addObject("reviewFlag", isReviewCompleted);
			return viewForJson;
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getTeamOtherDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		List<TeamDetails> officeDetails = null;
		List<TeamDetails> reviewerDetails = null;
		List analystDetails = null;

		try {
			officeDetails = this.teamAssignmentManager.getAllOfficeMaster();
			reviewerDetails = this.teamAssignmentManager.getAllReviewerMaster();
			analystDetails = this.teamAssignmentManager.getAllAnalystMaster();
			viewForJson.addObject("officeDetails", officeDetails);
			viewForJson.addObject("reviewerMasterList", reviewerDetails);
			viewForJson.addObject("analystDetails", analystDetails);
			return viewForJson;
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getAnalystForManager(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		Object analystForManager = new ArrayList();

		try {
			analystForManager = this.teamAssignmentManager.getAnalystForManager(request.getParameter("managerId"));
		} catch (CMSException var6) {
			viewForJson.addObject("AnalystLoop", true);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		viewForJson.addObject("analystForManager", analystForManager);
		return viewForJson;
	}

	public ModelAndView getCaseTeamDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		List caseDetails = null;

		try {
			caseDetails = this.teamAssignmentManager.getCaseTeamDetails(request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("caseDetails", caseDetails);
		return viewForJson;
	}

	public ModelAndView getLegacyCaseTeamDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		List caseDetails = null;

		try {
			caseDetails = this.teamAssignmentManager.getLegacyCaseTeamDetails(request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("caseDetails", caseDetails);
		return viewForJson;
	}

	public ModelAndView getLegacyTeamDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List caseDetails;
		try {
			this.logger.debug("crn in legacy team details " + request.getParameter("crn") + " for the team "
					+ request.getParameter("teamType"));
			caseDetails = this.teamAssignmentManager.getLegacyTeamDetails(request.getParameter("crn"),
					request.getParameter("teamType"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("caseDetails", caseDetails);
		return viewForJson;
	}

	public ModelAndView getTeamAssignmentDone(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		List<String> teamAssignmentDone = new ArrayList();
		String caseStatus = "";

		try {
			String pid = ResourceLocator.self().getCaseDetailService().getCaseInfo(request.getParameter("crn"))
					.getPid();
			if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid),
					SBMUtils.getSession(request))) {
				teamAssignmentDone = this.teamAssignmentManager.getTeamAssignmentDone(request.getParameter("crn"),
						SBMUtils.getSession(request));
			} else {
				caseStatus = "Completed";
			}
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		viewForJson.addObject("teamAssignmentDone", teamAssignmentDone);
		viewForJson.addObject("caseStatus", caseStatus);
		return viewForJson;
	}

	public ModelAndView getCaseDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		CaseDetails caseDetails = null;

		try {
			caseDetails = this.teamAssignmentManager.getCaseDetails(request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("caseDetails", caseDetails);
		return viewForJson;
	}

	public ModelAndView getTeamAssignmentTaskDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			MyTaskPageVO taskVo = new MyTaskPageVO();
			String startRequest = request.getParameter("start");
			String limitRequest = request.getParameter("limit");
			this.logger.debug("Start value is " + startRequest + " ::: and limit is " + limitRequest
					+ " :: start date :: " + request.getParameter("startClientDueDate") + " ::end date :: "
					+ request.getParameter("endClientDueDate"));
			if (null == startRequest || "null".equalsIgnoreCase(startRequest)
					|| "".equalsIgnoreCase(startRequest.trim())) {
				startRequest = "0";
			}

			if (null == limitRequest || "null".equalsIgnoreCase(limitRequest)
					|| "".equalsIgnoreCase(limitRequest.trim())) {
				limitRequest = "10";
			}

			taskVo.setStart(Integer.parseInt(startRequest));
			taskVo.setLimit(Integer.parseInt(limitRequest));
			if (null == request.getParameter(this.CRN_NO)) {
				taskVo.setCrn("");
			} else {
				taskVo.setCrn(request.getParameter(this.CRN_NO));
			}

			if (null == request.getParameter(this.COUNTRY)) {
				taskVo.setCountry("");
			} else {
				taskVo.setCountry(request.getParameter(this.COUNTRY));
			}

			if (null == request.getParameter(this.CASE_MANAGER)) {
				taskVo.setCaseManager("");
			} else {
				taskVo.setCaseManager(request.getParameter(this.CASE_MANAGER));
			}

			if (null == request.getParameter("startClientDueDate")) {
				taskVo.setStartClientDueDate("");
			} else {
				taskVo.setStartClientDueDate(request.getParameter("startClientDueDate"));
			}

			if (null == request.getParameter("endClientDueDate")) {
				taskVo.setEndClientDueDate("");
			} else {
				taskVo.setEndClientDueDate(request.getParameter("endClientDueDate"));
			}

			this.logger.debug("Start value is " + taskVo.getStart() + " ::: and limit is " + taskVo.getLimit()
					+ " :: searchCrn :: " + taskVo.getCrn() + " primary sub country :: " + taskVo.getCountry()
					+ " :: CM ::" + taskVo.getCaseManager() + " :: startDueDate :: " + taskVo.getStartClientDueDate()
					+ " :: endDueDate :: " + taskVo.getEndClientDueDate());
			HashMap<String, Object> resultMap = this.teamAssignmentManager.getTeamAssignmentTaskDetails(request,
					taskVo);
			if (resultMap != null && !resultMap.isEmpty()) {
				List<TeamDetails> caseDetails = (List) resultMap.get("processInstanceList");
				this.logger.debug("caseDetails size :: " + caseDetails.size());
				viewForJson.addObject("caseDetails", caseDetails);
				viewForJson.addObject("total", resultMap.get("total"));
			}

			return viewForJson;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getTeamSubjectDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		List<TeamDetails> teamSubjectDetails = null;
		List<TeamDetails> teamAnalystDetails = null;
		List<TeamDetails> caseAssignedAnalystDetails = null;
		List analystReDetails = null;

		try {
			teamSubjectDetails = this.teamAssignmentManager.getTeamSubjectDetails(request.getParameter("teamIds"));
			teamAnalystDetails = this.teamAssignmentManager.getTeamAnalystDetails(request.getParameter("teamIds"));
			analystReDetails = this.teamAssignmentManager.getAnalystReDetails(request.getParameter("teamIds"));
			caseAssignedAnalystDetails = this.teamAssignmentManager
					.getBulkAssignedAnalyst(request.getParameter("teamIds"));
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		viewForJson.addObject("teamSubDetails", teamSubjectDetails);
		viewForJson.addObject("teamAnalystDetails", teamAnalystDetails);
		viewForJson.addObject("teamAnalystReDetails", analystReDetails);
		viewForJson.addObject("caseAssignedAnalystDetails", caseAssignedAnalystDetails);
		return viewForJson;
	}

	public ModelAndView getOtherTeamDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug(" in getOtherTeamDetails ");
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();
		new ArrayList();
		new ArrayList();
		new ArrayList();

		List officeMasterList;
		List analystList;
		List reviewerMasterList;
		List allReviewerMasterList;
		try {
			this.logger.debug("team type :: " + request.getParameter("teamType") + " :: crn :: "
					+ request.getParameter("crn") + " :: managerID :: " + request.getParameter("managerId"));
			officeMasterList = this.teamAssignmentManager.getTeamOfficeMaster(request.getParameter("teamType"),
					request.getParameter("crn"));
			if (request.getParameter("managerId") != null && !request.getParameter("managerId").equals("")) {
				analystList = this.teamAssignmentManager.getAnalystForManager(request.getParameter("managerId"));
			} else {
				analystList = this.teamAssignmentManager.getTeamAnalystMaster(request.getParameter("teamType"),
						request.getParameter("crn"));
			}

			reviewerMasterList = this.teamAssignmentManager.getTeamReviewerMaster(request.getParameter("teamType"),
					request.getParameter("crn"));
			allReviewerMasterList = this.teamAssignmentManager.getReviewerForAllOffice();
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		viewForJson.addObject("OfficeMasterList", officeMasterList);
		viewForJson.addObject("analystMasterList", analystList);
		viewForJson.addObject("reviewerMasterList", reviewerMasterList);
		viewForJson.addObject("allReviewerMasterList", allReviewerMasterList);
		return viewForJson;
	}

	public ModelAndView checkTeamDueDates(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in checkTeamDueDates");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String rInterim1Date = request.getParameter(this.TEAM_IDD1);
			String rInterim2Date = request.getParameter(this.TEAM_IDD2);
			String rFinalDate = request.getParameter(this.TEAM_FDD);
			String officeId = request.getParameter(this.OFFICE_ID);
			this.logger.debug("rInterim1Date::" + rInterim1Date + "rInterim2Date::" + rInterim2Date + "rFinalDate::"
					+ rFinalDate + "officeId::" + officeId);
			boolean message = this.teamAssignmentManager.checkTeamDueDates(rInterim1Date, rInterim2Date, rFinalDate,
					officeId);
			if (message) {
				modelAndView.addObject(this.HOLIDAY, this.TRUE);
			} else {
				modelAndView.addObject(this.HOLIDAY, this.FALSE);
			}

			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView checkTeamDueDatesTab(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in checkTeamDueDatesTab");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String rInterim1Date = request.getParameter(this.TEAM_IDD1);
			String rInterim2Date = request.getParameter(this.TEAM_IDD2);
			String rFinalDate = request.getParameter(this.TEAM_FDD);
			String officeId = request.getParameter(this.OFFICE_ID);
			String crn = request.getParameter("crn");
			String teamType = request.getParameter("teamType");
			this.logger.debug("crn::" + crn + "teamType::" + teamType);
			this.logger.debug("rInterim1Date::" + rInterim1Date + "rInterim2Date" + rInterim2Date + "rFinalDate::"
					+ rFinalDate + "officeId::" + officeId);
			List<TeamDetails> teamDetailsList = this.populateResearchDatesDetails(request);
			this.logger.debug("caseDetailsList size" + teamDetailsList.size());
			String message = this.teamAssignmentManager.checkTeamDueDatesTab(teamDetailsList);
			if (message.trim().length() == 0) {
				modelAndView.addObject(this.SUCCESS, false);
			} else {
				if (message.contains("weekend") && message.contains("holiday")) {
					message = message.replace("weekend", "");
					message = message.replace("holiday", "");
					message = " Team Due Dates falls on public holiday and weekend for the CRN  " + message;
				} else if (message.contains("weekend")) {
					message = message.replace("weekend", "");
					message = " Team Due Dates falls on weekend for the CRN  " + message;
				} else if (message.contains("holiday")) {
					message = message.replace("holiday", "");
					message = "Team Due Dates falls on public holiday for the CRN  " + message;
				}

				modelAndView.addObject(this.SUCCESS, true);
				modelAndView.addObject(this.MESSAGE, message);
			}

			return modelAndView;
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}

	public List<TeamDetails> populateResearchDatesDetails(HttpServletRequest request) throws CMSException {
		this.logger.debug("in checkTeamDueDatesTab");
		new ModelAndView("jsonView");
		int dotIndex = false;
		List<TeamDetails> teamDetailsList = new ArrayList();
		List<String> crn = new ArrayList();
		if (null != request.getParameter("crn") && request.getParameter("crn").trim().length() > 0) {
			this.logger.debug("all crn's:: " + request.getParameter("crn"));
			crn = StringUtils.commaSeparatedStringToList(request.getParameter("crn"));
		}

		List<String> teamType = new ArrayList();
		if (null != request.getParameter("teamType") && request.getParameter("teamType").trim().length() > 0) {
			this.logger.debug("all teamType's:: " + request.getParameter("teamType"));
			teamType = StringUtils.commaSeparatedStringToList(request.getParameter("teamType"));
		}

		List<String> allTeamIds = new ArrayList();
		if (null != request.getParameter("allTeamId") && request.getParameter("allTeamId").trim().length() > 0) {
			this.logger.debug("allTeamId " + request.getParameter("allTeamId"));
			allTeamIds = StringUtils.commaSeparatedStringToList(request.getParameter("allTeamId"));
		}

		String[] OfficeIds = null;
		if (null != request.getParameter("officeId") && request.getParameter("officeId").trim().length() > 0) {
			this.logger.debug("officeIds :: " + request.getParameter("officeId"));
			OfficeIds = request.getParameter("officeId").split(this.DOUBLE_COMMA);
		}

		String[] int1DateDetails = null;
		if (null != request.getParameter(this.TEAM_IDD1) && request.getParameter(this.TEAM_IDD1).trim().length() > 0) {
			this.logger.debug("teamIDD1 :: " + request.getParameter(this.TEAM_IDD1));
			int1DateDetails = request.getParameter(this.TEAM_IDD1).split(",");
		}

		String[] int2DateDetails = null;
		if (null != request.getParameter(this.TEAM_IDD2) && request.getParameter(this.TEAM_IDD2).trim().length() > 0) {
			this.logger.debug("teamIDD2 :: " + request.getParameter(this.TEAM_IDD2));
			int2DateDetails = request.getParameter(this.TEAM_IDD2).split(",");
		}

		String[] finalDateDetails = null;
		if (null != request.getParameter(this.TEAM_FDD) && request.getParameter(this.TEAM_FDD).trim().length() > 0) {
			this.logger.debug("teamFDD :: " + request.getParameter(this.TEAM_FDD));
			finalDateDetails = request.getParameter(this.TEAM_FDD).split(",");
		}

		TeamDetails teamDetail = null;

		for (int i = 0; i < ((List) allTeamIds).size(); ++i) {
			teamDetail = new TeamDetails();
			String teamId = (String) ((List) allTeamIds).get(i);
			teamDetail.setTeamId(Integer.parseInt(teamId));
			teamDetail.setCrn((String) ((List) crn).get(i));
			this.logger.debug("crn.get(i)::" + (String) ((List) crn).get(i));
			teamDetail.setTeamType((String) ((List) teamType).get(i));
			this.logger.debug("teamType" + (String) ((List) teamType).get(i));
			if (!int1DateDetails[i].equalsIgnoreCase(this.BLANK)) {
				teamDetail.setDueDate1(int1DateDetails[i]);
			}

			if (!int2DateDetails[i].equalsIgnoreCase(this.BLANK)) {
				teamDetail.setDueDate2(int2DateDetails[i]);
			}

			teamDetail.setFinalDueDate(finalDateDetails[i]);
			int dotIndex = OfficeIds[i].indexOf(46);
			this.logger.debug("OfficeIds:::" + OfficeIds[i].substring(0, dotIndex));
			teamDetail.setOffice(OfficeIds[i].substring(0, dotIndex));
			teamDetailsList.add(teamDetail);
		}

		return teamDetailsList;
	}

	public ModelAndView getTeamAssignmentWINames(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List teamAssignmentWINames;
		try {
			teamAssignmentWINames = this.teamAssignmentManager.getTeamAssignmentWINames(request.getParameter("crn"));
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		viewForJson.addObject("teamAssignmentWINames", teamAssignmentWINames);
		return viewForJson;
	}
}