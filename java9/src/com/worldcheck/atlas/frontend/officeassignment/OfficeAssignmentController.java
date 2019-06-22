package com.worldcheck.atlas.frontend.officeassignment;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IOfficeAssignment;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.TeamTypeVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class OfficeAssignmentController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.officeassignment.OfficeAssignmentController");
	private String ASSIGNMENT_JSP = "officeAssignmentTab";
	private static final String CASE = "Case";
	private static final String FALSE = "False";
	private String SECTION_ASSIGNMENT_JSP = "officeAssignmentSection";
	private String SECTION_ASSIGNMENT_JSP_READ = "officeAssignmentSectionR";
	private String CRN_DETAILS = "crnDetails";
	private String CASE_MANAGER_DETAILS = "casemanagerIdDetails";
	private String WORKITEM_DETAILS = "workitemNameDetails";
	private String TEAMTYPE_DETAILS = "teamTypeDetails";
	private String OFFICE_DETAILS = "officeDetails";
	private String TEAMID_DETAILS = "teamIdDetails";
	private String INTERIM1DD_DETAILS = "int1DateDetails";
	private String INTERIM2DD_DETAILS = "int2DateDetails";
	private String FINALDD_DETAILS = "finalDateDetails";
	private String BYPASS_DETAILS = "byPassDetails";
	private String ISOADONE_DETAILS = "isOADoneDetails";
	private String ISBIRE_DETAILS = "isBIREPresentDetails";
	private String CASERE_DETAILS = "caseReDetails";
	private String ALL_OFFICE_IDS = "allOfficeIds";
	private String CASEINTERIM_DETAILS = "interimFlagDetails";
	private String BLANK = "blank";
	private String TRUE = "true";
	private String BI = "BI";
	private String VENDOR = "Vendor";
	private String DOUBLE_COMMA = ",,";
	private String COMMA_HASH = ",#";
	private String COMMA_DOUBLE_HASH = ",##";
	private String TEAM_SIZE = "teamSize";
	private String LEGACY_ASSIGNMENT_JSP = "officeAssignmentLegacy";
	private String OA_DATE_FORMAT = "dd-MM-yy";
	private SimpleDateFormat targetFormat;
	private IOfficeAssignment officeAssignmentManager;

	public OfficeAssignmentController() {
		this.targetFormat = new SimpleDateFormat(this.OA_DATE_FORMAT);
		this.officeAssignmentManager = null;
	}

	public void setOfficeAssignmentManager(IOfficeAssignment officeAssignmentManager) {
		this.officeAssignmentManager = officeAssignmentManager;
	}

	public ModelAndView saveOfficeAssignment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in saveOfficeAssignment");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("crn :: " + request.getParameter("crn") + " :: teamSize :: "
					+ request.getParameter(this.TEAM_SIZE));
			modelAndView = this.officeAssignmentManager.saveTeamDetails(request);
			this.logger.debug("exiting.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView completeOfficeAssignment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in completeOfficeAssignment");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("crn :: " + request.getParameter("crn") + " :: teamSize :: "
					+ request.getParameter(this.TEAM_SIZE));
			modelAndView = this.officeAssignmentManager.saveTeamDetails(request);
			this.logger.debug("exiting completeOfficeAssignment.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView officeAssignment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in officeAssignment");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.ASSIGNMENT_JSP);
			this.logger.debug("exiting.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView officeAssignmentSection(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in officeAssignmentSection");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.SECTION_ASSIGNMENT_JSP);
			String crn = request.getParameter("crn");
			this.logger.debug("crn :: " + crn);
			modelAndView.addObject("crn", crn);
			this.logger.debug("exiting.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView officeAssignmentSectionR(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in Read Only officeAssignmentSection");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.SECTION_ASSIGNMENT_JSP_READ);
			String crn = request.getParameter("crn");
			this.logger.debug("crn :: " + crn);
			modelAndView.addObject("crn", crn);
			this.logger.debug("exiting.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView officeAssignmentLegacy(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in officeAssignmentLegacy");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.LEGACY_ASSIGNMENT_JSP);
			String crn = request.getParameter("crn");
			this.logger.debug("crn :: " + crn);
			modelAndView.addObject("crn", crn);
			this.logger.debug("exiting.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView completeOfficeAssignmentTab(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in completeOfficeAssignmentTab");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("crnDetails :: " + request.getParameter(this.CRN_DETAILS) + " :: workitemNameDetails :: "
					+ request.getParameter(this.WORKITEM_DETAILS));
			this.logger.debug("teamTypeDetails :: " + request.getParameter(this.TEAMTYPE_DETAILS)
					+ " :: officeDetails :: " + request.getParameter(this.OFFICE_DETAILS) + " :: teamIdDetails :: "
					+ request.getParameter(this.TEAMID_DETAILS));
			this.logger.debug("int1DateDetails :: " + request.getParameter(this.INTERIM1DD_DETAILS)
					+ " :: int2DateDetails :: " + request.getParameter(this.INTERIM2DD_DETAILS)
					+ " :: finalDateDetails :: " + request.getParameter(this.FINALDD_DETAILS));
			this.logger.debug("byPassDetails :: " + request.getParameter(this.BYPASS_DETAILS)
					+ " :: isOADoneDetails :: " + request.getParameter(this.ISOADONE_DETAILS)
					+ " :: isBIREPresentDetails :: " + request.getParameter(this.ISBIRE_DETAILS));
			this.logger.debug("caseReDetails :: " + request.getParameter(this.CASERE_DETAILS) + " :: allOfficeIds :: "
					+ request.getParameter(this.ALL_OFFICE_IDS) + " :: interimFlagDetails :: "
					+ request.getParameter(this.CASEINTERIM_DETAILS));
			this.logger.debug("casemanagerIdDetails::" + request.getParameter(this.CASE_MANAGER_DETAILS));
			this.logger.debug("CaseOfficeIds::" + request.getParameter("officeIds"));
			List<CaseDetails> caseDetailsList = this.populateCaseDetails(request);
			this.officeAssignmentManager.saveTeamDetailsTab(caseDetailsList, SBMUtils.getSession(request),
					request.getParameter(this.CRN_DETAILS));
			modelAndView = new ModelAndView(this.ASSIGNMENT_JSP);
			this.logger.debug("exiting completeOfficeAssignmentTab.");
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	private List<CaseDetails> populateCaseDetails(HttpServletRequest request) throws CMSException, ParseException {
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String allOfficeIds = null;
		if (null != request.getParameter(this.ALL_OFFICE_IDS)
				&& request.getParameter(this.ALL_OFFICE_IDS).trim().length() > 0) {
			this.logger.debug("allOfficeIds " + request.getParameter(this.ALL_OFFICE_IDS));
			allOfficeIds = request.getParameter(this.ALL_OFFICE_IDS);
		}

		String trackerParam = (String) request.getSession().getAttribute("TrackerOn");
		TimeTrackerVO timeTrackerVO = null;
		boolean stopTT = false;
		if (null != trackerParam && !trackerParam.equals("")) {
			stopTT = true;
			timeTrackerVO = (TimeTrackerVO) request.getSession().getAttribute("trackerBean");
		}

		List<String> crnDetails = new ArrayList();
		if (null != request.getParameter(this.CRN_DETAILS)
				&& request.getParameter(this.CRN_DETAILS).trim().length() > 0) {
			this.logger.debug("crnDetails " + request.getParameter(this.CRN_DETAILS));
			crnDetails = StringUtils.commaSeparatedStringToList(request.getParameter(this.CRN_DETAILS));
		}

		String[] workitemNameDetails = null;
		if (null != request.getParameter(this.WORKITEM_DETAILS)
				&& request.getParameter(this.WORKITEM_DETAILS).trim().length() > 0) {
			this.logger.debug("workitemNameDetails :: " + request.getParameter(this.WORKITEM_DETAILS));
			if (null != timeTrackerVO && stopTT) {
				if (request.getParameter(this.WORKITEM_DETAILS).contains(timeTrackerVO.getWorkItemName())) {
					this.logger.debug("stopping office assignment task tracker for crn :: " + timeTrackerVO.getCrn()
							+ " from bulk assignment");
				} else {
					stopTT = false;
				}
			}

			workitemNameDetails = request.getParameter(this.WORKITEM_DETAILS).split(",");
		}

		if (stopTT) {
			if (null != timeTrackerVO) {
				timeTrackerVO.setUpdatedBy(userBean.getUserName());
				timeTrackerVO.setUserName(userBean.getUserName());
				ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
				request.getSession().removeAttribute("TrackerOn");
			} else {
				this.logger.debug("timeTrackerVO is null in session so not stopping tracker.");
			}
		}

		String[] interimFlagDetails = null;
		if (null != request.getParameter(this.CASEINTERIM_DETAILS)
				&& request.getParameter(this.CASEINTERIM_DETAILS).trim().length() > 0) {
			this.logger.debug("interimFlagDetails :: " + request.getParameter(this.CASEINTERIM_DETAILS));
			interimFlagDetails = request.getParameter(this.CASEINTERIM_DETAILS).split(",");
		}

		String[] isOADoneDetails = null;
		if (null != request.getParameter(this.ISOADONE_DETAILS)
				&& request.getParameter(this.ISOADONE_DETAILS).trim().length() > 0) {
			this.logger.debug("isOADoneDetails :: " + request.getParameter(this.ISOADONE_DETAILS));
			isOADoneDetails = request.getParameter(this.ISOADONE_DETAILS).split(",");
		}

		String[] isBIREPresentDetails = null;
		if (null != request.getParameter(this.ISBIRE_DETAILS)
				&& request.getParameter(this.ISBIRE_DETAILS).trim().length() > 0) {
			this.logger.debug("isBIREPresentDetails :: " + request.getParameter(this.ISBIRE_DETAILS));
			isBIREPresentDetails = request.getParameter(this.ISBIRE_DETAILS).split(",");
		}

		String[] teamIdDetails = null;
		if (null != request.getParameter(this.TEAMID_DETAILS)
				&& request.getParameter(this.TEAMID_DETAILS).trim().length() > 0) {
			this.logger.debug("teamIdDetails " + request.getParameter(this.TEAMID_DETAILS));
			teamIdDetails = request.getParameter(this.TEAMID_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] teamTypeDetails = null;
		if (null != request.getParameter(this.TEAMTYPE_DETAILS)
				&& request.getParameter(this.TEAMTYPE_DETAILS).trim().length() > 0) {
			teamTypeDetails = request.getParameter(this.TEAMTYPE_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] officeDetails = null;
		String allManagerIds = "";
		if (null != request.getParameter(this.OFFICE_DETAILS)
				&& request.getParameter(this.OFFICE_DETAILS).trim().length() > 0) {
			officeDetails = request.getParameter(this.OFFICE_DETAILS).split(this.DOUBLE_COMMA);
			allManagerIds = request.getParameter(this.OFFICE_DETAILS);
		}

		String[] casemanagerIdDetails = null;
		if (null != request.getParameter(this.CASE_MANAGER_DETAILS)
				&& request.getParameter(this.CASE_MANAGER_DETAILS).trim().length() > 0) {
			this.logger.debug("casemanagerIdDetails :: " + request.getParameter(this.CASE_MANAGER_DETAILS));
			casemanagerIdDetails = request.getParameter(this.CASE_MANAGER_DETAILS).split(",");
			this.logger.debug("casemanagerIdDetails " + casemanagerIdDetails);
		}

		String[] caseOfficeIds = null;
		if (null != request.getParameter("caseOfficeIds")
				&& request.getParameter("caseOfficeIds").trim().length() > 0) {
			this.logger.debug("caseOfficeIds :: " + request.getParameter("caseOfficeIds"));
			caseOfficeIds = request.getParameter("caseOfficeIds").split(this.DOUBLE_COMMA);
		}

		String[] cInterim1Date = null;
		if (null != request.getParameter("clientint1DateDetails")
				&& request.getParameter("clientint1DateDetails").trim().length() > 0) {
			this.logger.debug("clientint1DateDetails :: " + request.getParameter("clientint1DateDetails"));
			cInterim1Date = request.getParameter("clientint1DateDetails").split(",");
		}

		String[] cInterim2Date = null;
		if (null != request.getParameter("clientint2DateDetails")
				&& request.getParameter("clientint2DateDetails").trim().length() > 0) {
			this.logger.debug("clientint2DateDetails :: " + request.getParameter("clientint2DateDetails"));
			cInterim2Date = request.getParameter("clientint2DateDetails").split(",");
		}

		String[] cFinalDate = null;
		if (null != request.getParameter("clientfinalDateDetails")
				&& request.getParameter("clientfinalDateDetails").trim().length() > 0) {
			this.logger.debug("clientfinalDateDetails :: " + request.getParameter("clientfinalDateDetails"));
			cFinalDate = request.getParameter("clientfinalDateDetails").split(",");
		}

		String[] byPassDetails = null;
		if (null != request.getParameter(this.BYPASS_DETAILS)
				&& request.getParameter(this.BYPASS_DETAILS).trim().length() > 0) {
			this.logger.debug("byPassDetails :: " + request.getParameter(this.BYPASS_DETAILS));
			byPassDetails = request.getParameter(this.BYPASS_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] int1DateDetails = null;
		if (null != request.getParameter(this.INTERIM1DD_DETAILS)
				&& request.getParameter(this.INTERIM1DD_DETAILS).trim().length() > 0) {
			this.logger.debug("int1DateDetails :: " + request.getParameter(this.INTERIM1DD_DETAILS));
			int1DateDetails = request.getParameter(this.INTERIM1DD_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] int2DateDetails = null;
		if (null != request.getParameter(this.INTERIM2DD_DETAILS)
				&& request.getParameter(this.INTERIM2DD_DETAILS).trim().length() > 0) {
			this.logger.debug("int2DateDetails :: " + request.getParameter(this.INTERIM2DD_DETAILS));
			int2DateDetails = request.getParameter(this.INTERIM2DD_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] finalDateDetails = null;
		if (null != request.getParameter(this.FINALDD_DETAILS)
				&& request.getParameter(this.FINALDD_DETAILS).trim().length() > 0) {
			this.logger.debug("finalDateDetails :: " + request.getParameter(this.FINALDD_DETAILS));
			finalDateDetails = request.getParameter(this.FINALDD_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] caseReDetails = null;
		if (null != request.getParameter(this.CASERE_DETAILS)
				&& request.getParameter(this.CASERE_DETAILS).trim().length() > 0) {
			this.logger.debug("caseReDetails  " + request.getParameter(this.CASERE_DETAILS));
			caseReDetails = request.getParameter(this.CASERE_DETAILS).split(this.COMMA_DOUBLE_HASH);
		}

		String caseHistoryPerformer = "";
		if (null != request.getSession().getAttribute("loginLevel")) {
			caseHistoryPerformer = (String) request.getSession().getAttribute("performedBy");
		} else {
			caseHistoryPerformer = userBean.getUserName();
		}

		List<CaseDetails> caseDetailsList = new ArrayList();
		List<TeamDetails> teamDetails = null;
		TeamDetails teamDetail = null;
		CaseDetails caseDetail = null;
		List<Integer> teamIds = null;
		List<String> teamTypeDetList = null;
		List<String> officeDetailList = null;
		String[] teamReDetailList = null;
		HashMap<String, String> teamTypeMap = this.getTeamTypeMap();
		List<TeamDetails> rhAndOfficeDetails = this.officeAssignmentManager.getResearchHeadsForOffices(allOfficeIds);
		HashMap<String, String> rhMap = new HashMap();
		HashMap<String, String> officeMap = new HashMap();
		HashMap<String, String> rhDetailMap = new HashMap();

		for (int k = 0; k < rhAndOfficeDetails.size(); ++k) {
			TeamDetails temp = (TeamDetails) rhAndOfficeDetails.get(k);
			rhMap.put(temp.getOffice(), temp.getResearchHead());
			officeMap.put(temp.getOffice(), temp.getOfficeName());
			rhDetailMap.put(temp.getResearchHead(), temp.getRhFullName());
		}

		if (null != allManagerIds && allManagerIds.trim().length() > 0) {
			allManagerIds = allManagerIds.replaceAll(this.DOUBLE_COMMA, ",");
		}

		this.logger.debug("allManagerIds :: " + allManagerIds);
		List<TeamDetails> managerDetails = this.officeAssignmentManager.getManagerDetails(allManagerIds);
		HashMap<String, String> managerMap = new HashMap();

		int i;
		for (i = 0; i < managerDetails.size(); ++i) {
			TeamDetails temp = (TeamDetails) managerDetails.get(i);
			managerMap.put(temp.getManagerName(), temp.getManagerFullName());
		}

		for (i = 0; i < ((List) crnDetails).size(); ++i) {
			caseDetail = new CaseDetails();
			teamDetails = new ArrayList();
			String crn = (String) ((List) crnDetails).get(i);
			caseDetail.setCrn(crn);
			caseDetail.setUpdatedBy(userBean.getUserName());
			String interimFlag = interimFlagDetails[i];
			boolean interim1Flag = Boolean.parseBoolean(interimFlag.split("#")[0]);
			boolean interim2Flag = Boolean.parseBoolean(interimFlag.split("#")[1]);
			this.logger.debug("crn :: " + crn + " ::interim1Flag :: " + interim1Flag + " :: interim2Flag :: "
					+ interim2Flag + " :: officeDetails[i] :: " + officeDetails[i]);
			caseDetail.setIsOASaved(Boolean.parseBoolean(isOADoneDetails[i]));
			caseDetail.setIsBIREPresent(Boolean.parseBoolean(isBIREPresentDetails[i]));
			caseDetail.setWorkitemName(workitemNameDetails[i]);
			caseDetail.setCaseMgrId(casemanagerIdDetails[i]);
			teamIds = StringUtils.commaSeparatedStringToIntList(teamIdDetails[i]);
			teamTypeDetList = StringUtils.commaSeparatedStringToList(teamTypeDetails[i]);
			officeDetailList = StringUtils.commaSeparatedStringToList(officeDetails[i]);
			teamReDetailList = caseReDetails[i].split(this.COMMA_HASH);
			this.logger.debug(
					"teamIds list size :: " + teamIds.size() + " :: OASaved flag :: " + caseDetail.getIsOASaved());
			this.logger.debug("request.getParameter(officeIds)" + caseOfficeIds);
			this.logger.debug("request.getParameter(clientIDD1)" + cInterim1Date[i]);
			this.logger.debug("request.getParameter(clientIDD2)" + cInterim2Date[i]);
			this.logger.debug("request.getParameter(clientFDD)" + cFinalDate[i]);
			int dotIndex = false;
			if (!cInterim1Date[i].equalsIgnoreCase(this.BLANK)) {
				caseDetail.setcInterim1(new Date(this.targetFormat.parse(cInterim1Date[i]).getTime()));
			}

			if (!cInterim2Date[i].equalsIgnoreCase(this.BLANK)) {
				caseDetail.setcInterim2(new Date(this.targetFormat.parse(cInterim2Date[i]).getTime()));
			}

			if (!cFinalDate[i].equalsIgnoreCase(this.BLANK)) {
				caseDetail.setFinalDueDate(new Date(this.targetFormat.parse(cFinalDate[i]).getTime()));
			}

			int dotIndex = caseOfficeIds[i].indexOf(46);
			this.logger.debug("caseOfficeIds:::" + caseOfficeIds[i].substring(0, dotIndex));
			caseDetail.setOfficeId(caseOfficeIds[i].substring(0, dotIndex));

			for (int j = 0; j < officeDetailList.size(); ++j) {
				teamDetail = new TeamDetails();
				teamDetail.setCrn(crn);
				teamDetail.setTeamId((Integer) teamIds.get(j));
				teamDetail.setTeamTypeId((String) teamTypeDetList.get(j));
				teamDetail.setTeamType((String) teamTypeMap.get(teamTypeDetList.get(j)));
				if (null == teamDetail.getTeamType() || teamDetail.getTeamType().trim().length() <= 0
						|| !teamDetail.getTeamType().contains(this.BI)
								&& !teamDetail.getTeamType().contains(this.VENDOR)) {
					teamDetail.setOffice((String) officeDetailList.get(j));
					teamDetail.setResearchHead((String) rhMap.get(officeDetailList.get(j)));
					teamDetail.setOfficeName((String) officeMap.get(officeDetailList.get(j)));
					teamDetail.setRhFullName((String) rhDetailMap.get(teamDetail.getResearchHead()));
				} else {
					teamDetail.setManagerName((String) officeDetailList.get(j));
					teamDetail.setManagerFullName((String) managerMap.get(officeDetailList.get(j)));
				}

				teamDetail.setByPassInterim(byPassDetails[i].split(",")[j]);
				if (null != teamDetail.getByPassInterim() && teamDetail.getByPassInterim().trim().length() > 0
						&& teamDetail.getByPassInterim().equals(this.TRUE)) {
					teamDetail.setByPassInterim("1");
					teamDetail.setDueDate1("");
					teamDetail.setDueDate2("");
				} else {
					teamDetail.setByPassInterim("0");
					if (interim1Flag) {
						if (int1DateDetails[i].split(",")[j].equalsIgnoreCase(this.BLANK)) {
							teamDetail.setDueDate1("");
						} else {
							teamDetail.setDueDate1(int1DateDetails[i].split(",")[j]);
						}
					}

					if (interim2Flag) {
						if (int2DateDetails[i].split(",")[j].equalsIgnoreCase(this.BLANK)) {
							teamDetail.setDueDate2("");
						} else {
							teamDetail.setDueDate2(int2DateDetails[i].split(",")[j]);
						}
					} else {
						teamDetail.setDueDate2("");
					}
				}

				teamDetail.setFinalDueDate(finalDateDetails[i].split(",")[j]);
				teamDetail.setReIds(teamReDetailList[j]);
				teamDetail.setUpdatedBy(userBean.getUserName());
				teamDetails.add(teamDetail);
			}

			caseDetail.setTeamList(teamDetails);
			caseDetail.setCaseHistoryPerformer(caseHistoryPerformer);
			caseDetailsList.add(caseDetail);
		}

		return caseDetailsList;
	}

	private HashMap<String, String> getTeamTypeMap() throws CMSException {
		List<TeamTypeVO> teamTypeList = this.officeAssignmentManager.getTeamTypes();
		HashMap<String, String> teamTypeMap = new HashMap();

		for (int j = 0; j < teamTypeList.size(); ++j) {
			TeamTypeVO vo = (TeamTypeVO) teamTypeList.get(j);
			teamTypeMap.put(vo.getTeamTypeId(), vo.getTeamType());
		}

		return teamTypeMap;
	}
}