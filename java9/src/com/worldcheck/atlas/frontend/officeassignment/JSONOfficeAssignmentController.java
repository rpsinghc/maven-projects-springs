package com.worldcheck.atlas.frontend.officeassignment;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.IOfficeAssignment;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.TeamOfficeVO;
import com.worldcheck.atlas.vo.TeamTypeVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONOfficeAssignmentController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.officeassignment.JSONOfficeAssignmentController");
	private String TEAMTYPE_LIST = "teamTypeList";
	private String VENDOR_MANAGERS = "vendorManagers";
	private String BI_MANAGERS = "biManagers";
	private String CASE_DETAIL_LIST = "caseDetailList";
	private String SUBJECT_LIST = "subjectList";
	private String TEAM_SIZE = "teamSize";
	private String TEAM_DETAILS = "teamDetails";
	private String SUCCESS = "success";
	private String CASE_DUEDATES = "caseDueDates";
	private String CRN = "crn";
	private String COLOR = "color";
	private String BIRE_PRESENT = "biREPresent";
	private String SUBJECT_RE_LIST = "subjectREList";
	private String IS_REPLICATED_SBJECTS = "isReplicatedSubjects";
	private String AUTO_ADDED_TEAMS = "autoTeamDetails";
	private String OFFICES = "offices";
	private String OFFICE_IDS = "officeIds";
	private String IS_OADONE = "isOADone";
	private String CURRENTCASE_CYCLE = "currentCaseCycle";
	private String MESSAGE = "message";
	private String BLANK = "blank";
	private String TRUE = "true";
	private String FALSE = "false";
	private String DOUBLE_COMMA = ",,";
	private String CRN_DETAIL = "crnDetail";
	private String INTERIM_FLAG = "interimFlag";
	private String OFFICE_DETAILS = "officeDetails";
	private String BYPASS_DETAILS = "byPassDetails";
	private String TEAM_FDD = "teamFDD";
	private String TEAM_IDD2 = "teamIDD2";
	private String TEAM_IDD1 = "teamIDD1";
	private String TEAMTYPE_DETAILS = "teamTypeDetails";
	private String TEAM_COUNT = "teamCount";
	private String TEAM = "team";
	private String FAILURE_MESSAGE = "failureMessage";
	private String CURRENTCASE_STATUS = "currentCaseStatus";
	private String COMPLETED = "Completed";
	private String CANCELLED = "Cancelled";
	private String FINAL = "Final";
	private String INACTIVE = "Inactive";
	private String INACTIVE_DOUBLEHASH = "Inactive##";
	private String CRN_NO = "crnNo";
	private String COUNTRY = "country";
	private String RED = "red";
	private String Task_Name = "taskName";
	private String CASE_MANAGER = "caseManager";
	private String OA_DATE_FORMAT = "dd-MM-yy";
	private SimpleDateFormat targetFormat;
	private IOfficeAssignment officeAssignmentManager;
	private String SUBJECT_IDS_MATCH_GCC;
	private String OFFICE_LIST_NEW;
	private String COUNTRY_RE_LIST;
	private String AUTO_POPULATED_CASES;

	public JSONOfficeAssignmentController() {
		this.targetFormat = new SimpleDateFormat(this.OA_DATE_FORMAT);
		this.officeAssignmentManager = null;
		this.SUBJECT_IDS_MATCH_GCC = "listAssociatedSubjectsGCC";
		this.OFFICE_LIST_NEW = "officeListNew";
		this.COUNTRY_RE_LIST = "countryREList";
		this.AUTO_POPULATED_CASES = "autoPopulatedCases";
	}

	public void setOfficeAssignmentManager(IOfficeAssignment officeAssignmentManager) {
		this.officeAssignmentManager = officeAssignmentManager;
	}

	public ModelAndView getCaseDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getCaseDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			TaskColorVO colorDetails = null;
			this.logger.debug("crn in  getCaseDetails :: " + request.getParameter(this.CRN));
			String crn = request.getParameter(this.CRN);
			if (request.getParameter(this.Task_Name) != null) {
				String[] task = request.getParameter(this.Task_Name).toString().split("%20");
				String task_name = task[0] + " " + task[1] + " " + task[2];
				colorDetails = this.officeAssignmentManager.getColorDetails(crn, task_name);
				if (colorDetails != null && colorDetails.getColor() != null && colorDetails.getColor().length() != 0) {
					colorDetails.getColor();
				} else {
					colorDetails = new TaskColorVO();
					colorDetails.setColor(this.RED);
					this.logger.debug("IN IF colorDetails.getColor():::" + colorDetails.getColor());
				}

				modelAndView.addObject(this.COLOR, colorDetails);
			}

			List<SubjectDetails> subjectList = this.officeAssignmentManager.getCaseSubjectDetails(crn);
			boolean isOADone = this.officeAssignmentManager.isOfficeAssignmentDone(crn);
			this.logger.debug("isOADone :: " + isOADone);
			if (isOADone) {
				List<TeamDetails> teamDetails = this.officeAssignmentManager.getTeamDetails(crn);
				this.logger.debug("teamDetails size:: " + teamDetails.size());
				int teamSize = teamDetails.size();
				modelAndView.addObject(this.TEAM_DETAILS, teamDetails);
				modelAndView.addObject(this.TEAM_SIZE, teamSize);
			}

			boolean biREPresent = false;
			biREPresent = this.officeAssignmentManager.isBiREPresent(subjectList);
			this.logger.debug("subjectList size :: " + subjectList.size() + " :: biREPresent :: " + biREPresent);
			List<SubjectDetails> subjectREList = this.officeAssignmentManager.getSubjectCountryREDetails(crn);
			this.logger.debug("subjectREList size :: " + subjectREList.size());
			boolean isREPresent = false;
			this.logger.debug("subjectREList size :: " + subjectREList.size() + " :: isREPresent :: " + isREPresent);
			List<TeamOfficeVO> autoTeamDetails = new ArrayList();
			List<TeamOfficeVO> countryREList = new ArrayList();
			List<String> replicatedSubjects = this.officeAssignmentManager.getIsReplicatedSubjects(crn);
			if (replicatedSubjects.size() > 0) {
				autoTeamDetails = this.officeAssignmentManager.getAutoTeamType();
				countryREList = this.officeAssignmentManager.getcountryREList(crn);
			}

			this.logger.debug("isReplicatedSubjects length::" + replicatedSubjects.size() + "autoTeamDetails length::"
					+ ((List) autoTeamDetails).size());
			this.logger.debug("Country RE List Size : " + ((List) countryREList).size());
			List<TeamTypeVO> teamTypeList = this.officeAssignmentManager.getTeamTypes();
			this.logger.debug("teamTypeList size :: " + teamTypeList.size());
			CaseDetails caseDueDates = this.officeAssignmentManager.getCaseDetails(crn);
			this.logger.debug("caseDueDates.getOfficeName()::: " + caseDueDates.getOfficeName());
			this.logger.debug("caseDueDates IDD1 :: " + caseDueDates.getrInterim1() + " :: IDD2 :: "
					+ caseDueDates.getrInterim2() + " :: Final :: " + caseDueDates.getFinalRDueDate());
			List vendorManagers;
			if (biREPresent) {
				vendorManagers = this.officeAssignmentManager.getBIManagerList();
				this.logger.debug("biManagers :: " + vendorManagers);
				modelAndView.addObject(this.BI_MANAGERS, vendorManagers);
			}

			vendorManagers = this.officeAssignmentManager.getVendorManagerList();
			this.logger.debug("vendorManagers :: " + vendorManagers);
			String caseStatus = caseDueDates.getCaseStatus();
			String caseManager = caseDueDates.getCaseManagerName();
			String currentCaseCycle = "";
			if (null != caseStatus && !caseStatus.equalsIgnoreCase(this.CANCELLED)
					&& !caseStatus.equalsIgnoreCase(this.COMPLETED)) {
				long pid = Long.parseLong(caseDueDates.getPid());
				this.logger.debug("pid :: " + pid);
				currentCaseCycle = this.officeAssignmentManager.getCurrentCaseCycle(SBMUtils.getSession(request), pid);
			} else {
				currentCaseCycle = this.FINAL;
			}

			ArrayList<String> officeListNew = new ArrayList();
			if (replicatedSubjects.size() > 0) {
				this.logger.debug("in Replicated Condition");
				Iterator it = ((List) autoTeamDetails).iterator();

				while (it.hasNext()) {
					String officeName = ((TeamOfficeVO) it.next()).getOffice();
					if (!officeListNew.contains(officeName)) {
						officeListNew.add(officeName);
					}
				}
			}

			if (subjectREList.size() > 0) {
				this.logger.debug("in subjectREList ");

				for (int i = 0; i < subjectREList.size(); ++i) {
					SubjectDetails subject = (SubjectDetails) subjectREList.get(i);
					String office = subject.getOffice();
					this.logger.debug("Compare- " + officeListNew + ": " + office);
					if (null != office) {
						if (!officeListNew.contains(office) && biREPresent && officeListNew.size() < 4) {
							officeListNew.add(office);
						}

						if (!officeListNew.contains(office) && !biREPresent && officeListNew.size() < 5) {
							officeListNew.add(office);
						}
					}
				}
			}

			this.logger.debug("Final office list : " + officeListNew);
			this.logger.debug("currentCaseCycle :: " + currentCaseCycle + " :: caseStatus :: " + caseStatus);
			modelAndView.addObject(this.BIRE_PRESENT, biREPresent);
			modelAndView.addObject(this.SUBJECT_RE_LIST, subjectREList);
			modelAndView.addObject(this.IS_REPLICATED_SBJECTS, replicatedSubjects);
			modelAndView.addObject(this.AUTO_ADDED_TEAMS, autoTeamDetails);
			modelAndView.addObject(this.IS_OADONE, isOADone);
			modelAndView.addObject(this.VENDOR_MANAGERS, vendorManagers);
			modelAndView.addObject(this.CASE_DUEDATES, caseDueDates);
			modelAndView.addObject(this.TEAMTYPE_LIST, teamTypeList);
			modelAndView.addObject(this.SUBJECT_LIST, subjectList);
			modelAndView.addObject(this.CURRENTCASE_CYCLE, currentCaseCycle);
			modelAndView.addObject(this.CURRENTCASE_STATUS, caseStatus);
			modelAndView.addObject(this.CASE_MANAGER, caseManager);
			modelAndView.addObject(this.OFFICE_LIST_NEW, officeListNew);
			modelAndView.addObject(this.COUNTRY_RE_LIST, countryREList);
			return modelAndView;
		} catch (CMSException var24) {
			return AtlasUtils.getJsonExceptionView(this.logger, var24, response);
		} catch (Exception var25) {
			return AtlasUtils.getJsonExceptionView(this.logger, var25, response);
		}
	}

	public ModelAndView getLegacyCaseDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getLegacyCaseDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("crn in  getLegacyCaseDetails :: " + request.getParameter(this.CRN));
			String crn = request.getParameter(this.CRN);
			List<TeamDetails> teamDetails = this.officeAssignmentManager.getLegacyTeamDetails(crn);
			this.logger.debug("teamDetails size:: " + teamDetails.size());
			int teamSize = teamDetails.size();
			CaseDetails caseDetail = this.officeAssignmentManager.getLegacyCaseDetails(crn);
			this.logger.debug("caseDetail IDD1 :: " + caseDetail.getcInterim1() + " :: IDD2 :: "
					+ caseDetail.getcInterim2() + " :: Final :: " + caseDetail.getFinalDueDate() + " : manager :: "
					+ caseDetail.getCaseManagerName() + " :: priSubj :: " + caseDetail.getPrimarySubjectName()
					+ " :: priSub country :: " + caseDetail.getPrimarySubjectCountryName());
			modelAndView.addObject(this.TEAM_DETAILS, teamDetails);
			modelAndView.addObject(this.TEAM_SIZE, teamSize);
			modelAndView.addObject(this.CASE_DUEDATES, caseDetail);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getRHForOffice(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getRHForOffice");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug(" :: officeIds :: " + request.getParameter(this.OFFICE_IDS));
			String offices = this.officeAssignmentManager.getOfficeWithoutRH(request.getParameter(this.OFFICE_IDS));
			if (offices.trim().length() == 0) {
				modelAndView.addObject(this.SUCCESS, false);
			} else {
				modelAndView.addObject(this.SUCCESS, true);
				if (offices.startsWith(this.INACTIVE_DOUBLEHASH)) {
					modelAndView.addObject(this.INACTIVE, true);
					offices = offices.substring(this.INACTIVE_DOUBLEHASH.length());
				} else {
					modelAndView.addObject(this.INACTIVE, false);
				}

				modelAndView.addObject(this.OFFICES, offices);
			}

			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView checkDueDates(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in checkDueDates");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("teamCount :: " + request.getParameter(this.TEAM_COUNT));
			this.logger.debug("teamTypeDetails :: " + request.getParameter(this.TEAMTYPE_DETAILS));
			this.logger.debug("IDD1 :: " + request.getParameter(this.TEAM_IDD1) + " :: IDD2 :: "
					+ request.getParameter(this.TEAM_IDD2) + " :: FDD :: " + request.getParameter(this.TEAM_FDD));
			this.logger.debug("byPassDetails :: " + request.getParameter(this.BYPASS_DETAILS));
			this.logger.debug("officeDetails :: " + request.getParameter(this.OFFICE_DETAILS) + " :: interimFlag :: "
					+ request.getParameter(this.INTERIM_FLAG));
			String interimFlag = request.getParameter(this.INTERIM_FLAG);
			boolean interim1Flag = Boolean.parseBoolean(interimFlag.split("#")[0]);
			boolean interim2Flag = Boolean.parseBoolean(interimFlag.split("#")[1]);
			int teamSize = 0;
			if (null != request.getParameter(this.TEAM_COUNT)
					&& request.getParameter(this.TEAM_COUNT).trim().length() > 0) {
				teamSize = Integer.parseInt(request.getParameter(this.TEAM_COUNT));
			}

			TeamDetails allTeamDetails = this.populateAllTeamDDDetails(request);
			String message = this.officeAssignmentManager.checkDueDates(allTeamDetails, teamSize, interim1Flag,
					interim2Flag);
			if (message.trim().length() == 0) {
				modelAndView.addObject(this.SUCCESS, false);
			} else {
				modelAndView.addObject(this.SUCCESS, true);
				modelAndView.addObject(this.MESSAGE, message);
			}

			return modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView checkClientDueDates(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in checkClientDueDates");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String cInterim1Date = request.getParameter("clientIDD1");
			String cInterim2Date = request.getParameter("clientIDD2");
			String cFinalDate = request.getParameter("clientFDD");
			String officeId = request.getParameter("officeId");
			this.logger.debug("cInterim1Date::" + cInterim1Date + "cInterim2Date" + cInterim2Date + "cFinalDate::"
					+ cFinalDate + "officeId::" + officeId);
			boolean message = this.officeAssignmentManager.checkClientDueDates(cInterim1Date, cInterim2Date, cFinalDate,
					officeId);
			if (message) {
				modelAndView.addObject("holiday", this.TRUE);
			} else {
				modelAndView.addObject("holiday", this.FALSE);
			}

			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	private TeamDetails populateAllTeamDDDetails(HttpServletRequest request) {
		TeamDetails allTeamDetails = new TeamDetails();
		if (null != request.getParameter(this.BYPASS_DETAILS)
				&& request.getParameter(this.BYPASS_DETAILS).trim().length() > 0) {
			allTeamDetails.setByPassInterim(request.getParameter(this.BYPASS_DETAILS));
		}

		if (null != request.getParameter(this.TEAMTYPE_DETAILS)
				&& request.getParameter(this.TEAMTYPE_DETAILS).trim().length() > 0) {
			allTeamDetails.setTeamTypeId(request.getParameter(this.TEAMTYPE_DETAILS));
		}

		if (null != request.getParameter(this.OFFICE_DETAILS)
				&& request.getParameter(this.OFFICE_DETAILS).trim().length() > 0) {
			allTeamDetails.setOffice(request.getParameter(this.OFFICE_DETAILS));
		}

		if (null != request.getParameter(this.TEAM_IDD1) && request.getParameter(this.TEAM_IDD1).trim().length() > 0) {
			allTeamDetails.setDueDate1(request.getParameter(this.TEAM_IDD1));
		}

		if (null != request.getParameter(this.TEAM_IDD2) && request.getParameter(this.TEAM_IDD2).trim().length() > 0) {
			allTeamDetails.setDueDate2(request.getParameter(this.TEAM_IDD2));
		}

		if (null != request.getParameter(this.TEAM_FDD) && request.getParameter(this.TEAM_FDD).trim().length() > 0) {
			allTeamDetails.setFinalDueDate(request.getParameter(this.TEAM_FDD));
		}

		return allTeamDetails;
	}

	public ModelAndView checkDueDatesTab(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in checkDueDatesTab");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("crnDetail :: " + request.getParameter(this.CRN_DETAIL));
			this.logger.debug("teamTypeDetails :: " + request.getParameter(this.TEAMTYPE_DETAILS));
			this.logger.debug("IDD1 :: " + request.getParameter(this.TEAM_IDD1) + " :: IDD2 :: "
					+ request.getParameter(this.TEAM_IDD2) + " :: FDD :: " + request.getParameter(this.TEAM_FDD));
			this.logger.debug("byPassDetails :: " + request.getParameter(this.BYPASS_DETAILS));
			this.logger.debug("officeDetails :: " + request.getParameter(this.OFFICE_DETAILS) + " :: interimFlag :: "
					+ request.getParameter(this.INTERIM_FLAG));
			List<CaseDetails> caseDetailsList = this.populateCaseDetails(request);
			String message = this.officeAssignmentManager.checkDueDatesTab(caseDetailsList);
			if (message.trim().length() == 0) {
				modelAndView.addObject(this.SUCCESS, false);
			} else {
				modelAndView.addObject(this.SUCCESS, true);
				modelAndView.addObject(this.MESSAGE, message);
			}

			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView checkClientDueDatesTab(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in checkClientDueDatesTab");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String message = "";
			this.logger.debug("clientIDD1:: " + request.getParameter("clientIDD1") + " :: clientIDD2 :: "
					+ request.getParameter("clientIDD2") + " :: clientFDD :: " + request.getParameter("clientFDD"));
			List<CaseDetails> caseDetailsList = this.populateClientDetails(request);
			this.logger.debug("caseDetailsList size" + caseDetailsList.size());
			message = message + this.officeAssignmentManager.checkClientDueDatesTab(caseDetailsList);
			this.logger.debug("message::" + message);
			if (message.trim().length() == 0) {
				modelAndView.addObject(this.SUCCESS, false);
			} else {
				if (message.contains("weekend") && message.contains("holiday")) {
					message = message.replace("weekend", "");
					message = message.replace("holiday", "");
					message = "Client Due Dates falls on Public Holiday and weekend for the CRN " + message;
				} else if (message.contains("weekend")) {
					message = message.replace("weekend", "");
					message = "Client Due Dates falls on weekend for the CRN " + message;
				} else if (message.contains("holiday")) {
					message = message.replace("holiday", "");
					message = "Client Due Dates falls on Public Holiday for the CRN " + message;
				}

				this.logger.debug("final message prepared in controller::" + message);
				modelAndView.addObject(this.SUCCESS, true);
				modelAndView.addObject(this.MESSAGE, message);
			}

			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	private List<CaseDetails> populateClientDetails(HttpServletRequest request) throws CMSException {
		ArrayList caseDetailsList = new ArrayList();

		try {
			List<String> crnDetails = new ArrayList();
			if (null != request.getParameter(this.CRN_DETAIL)
					&& request.getParameter(this.CRN_DETAIL).trim().length() > 0) {
				this.logger.debug("crnDetail " + request.getParameter(this.CRN_DETAIL));
				crnDetails = StringUtils.commaSeparatedStringToList(request.getParameter(this.CRN_DETAIL));
			}

			String[] caseOfficeIds = null;
			if (null != request.getParameter("officeIds") && request.getParameter("officeIds").trim().length() > 0) {
				this.logger.debug("caseOfficeIds :: " + request.getParameter("officeIds"));
				caseOfficeIds = request.getParameter("officeIds").split(this.DOUBLE_COMMA);
			}

			String[] cInterim1Date = null;
			if (null != request.getParameter("clientIDD1") && request.getParameter("clientIDD1").trim().length() > 0) {
				this.logger.debug("clientIDD1 :: " + request.getParameter("clientIDD1"));
				cInterim1Date = request.getParameter("clientIDD1").split(",");
			}

			String[] cInterim2Date = null;
			if (null != request.getParameter("clientIDD2") && request.getParameter("clientIDD2").trim().length() > 0) {
				this.logger.debug("clientIDD2 :: " + request.getParameter("clientIDD2"));
				cInterim2Date = request.getParameter("clientIDD2").split(",");
			}

			String[] cFinalDate = null;
			if (null != request.getParameter("clientFDD") && request.getParameter("clientFDD").trim().length() > 0) {
				this.logger.debug("clientFDD :: " + request.getParameter("clientFDD"));
				cFinalDate = request.getParameter("clientFDD").split(",");
			}

			this.logger.debug("request.getParameter(officeIds)" + request.getParameter("officeIds"));
			this.logger.debug("request.getParameter(clientIDD1)" + request.getParameter("clientIDD1"));
			this.logger.debug("request.getParameter(clientIDD2)" + request.getParameter("clientIDD2"));
			this.logger.debug("request.getParameter(clientFDD)" + request.getParameter("clientFDD"));
			CaseDetails caseDetail = null;

			for (int i = 0; i < ((List) crnDetails).size(); ++i) {
				caseDetail = new CaseDetails();
				String crn = (String) ((List) crnDetails).get(i);
				this.logger.debug("cInterim1Date::" + cInterim1Date[i]);
				this.logger.debug("cInterim2Date::" + cInterim2Date[i]);
				this.logger.debug("cFinalDate::" + cFinalDate[i]);
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
				caseDetail.setCrn(crn);
				caseDetailsList.add(caseDetail);
			}
		} catch (ParseException var12) {
			this.logger.debug("Exception in parsing::" + var12);
		}

		return caseDetailsList;
	}

	private List<CaseDetails> populateCaseDetails(HttpServletRequest request) throws CMSException {
		List<String> crnDetails = new ArrayList();
		if (null != request.getParameter(this.CRN_DETAIL)
				&& request.getParameter(this.CRN_DETAIL).trim().length() > 0) {
			this.logger.debug("crnDetail " + request.getParameter(this.CRN_DETAIL));
			crnDetails = StringUtils.commaSeparatedStringToList(request.getParameter(this.CRN_DETAIL));
		}

		String[] interimFlagDetails = null;
		if (null != request.getParameter(this.INTERIM_FLAG)
				&& request.getParameter(this.INTERIM_FLAG).trim().length() > 0) {
			this.logger.debug("interimFlag :: " + request.getParameter(this.INTERIM_FLAG));
			interimFlagDetails = request.getParameter(this.INTERIM_FLAG).split(",");
		}

		String[] teamTypeDetails = null;
		if (null != request.getParameter(this.TEAMTYPE_DETAILS)
				&& request.getParameter(this.TEAMTYPE_DETAILS).trim().length() > 0) {
			teamTypeDetails = request.getParameter(this.TEAMTYPE_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] officeDetails = null;
		if (null != request.getParameter(this.OFFICE_DETAILS)
				&& request.getParameter(this.OFFICE_DETAILS).trim().length() > 0) {
			officeDetails = request.getParameter(this.OFFICE_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] byPassDetails = null;
		if (null != request.getParameter(this.BYPASS_DETAILS)
				&& request.getParameter(this.BYPASS_DETAILS).trim().length() > 0) {
			this.logger.debug("byPassDetails :: " + request.getParameter(this.BYPASS_DETAILS));
			byPassDetails = request.getParameter(this.BYPASS_DETAILS).split(this.DOUBLE_COMMA);
		}

		String[] int1DateDetails = null;
		if (null != request.getParameter(this.TEAM_IDD1) && request.getParameter(this.TEAM_IDD1).trim().length() > 0) {
			this.logger.debug("teamIDD1 :: " + request.getParameter(this.TEAM_IDD1));
			int1DateDetails = request.getParameter(this.TEAM_IDD1).split(this.DOUBLE_COMMA);
		}

		String[] int2DateDetails = null;
		if (null != request.getParameter(this.TEAM_IDD2) && request.getParameter(this.TEAM_IDD2).trim().length() > 0) {
			this.logger.debug("teamIDD2 :: " + request.getParameter(this.TEAM_IDD2));
			int2DateDetails = request.getParameter(this.TEAM_IDD2).split(this.DOUBLE_COMMA);
		}

		String[] finalDateDetails = null;
		if (null != request.getParameter(this.TEAM_FDD) && request.getParameter(this.TEAM_FDD).trim().length() > 0) {
			this.logger.debug("teamFDD :: " + request.getParameter(this.TEAM_FDD));
			finalDateDetails = request.getParameter(this.TEAM_FDD).split(this.DOUBLE_COMMA);
		}

		List<CaseDetails> caseDetailsList = new ArrayList();
		List<TeamDetails> teamDetails = null;
		TeamDetails teamDetail = null;
		CaseDetails caseDetail = null;
		List<String> teamTypeDetList = null;
		List<String> officeDetailList = null;

		for (int i = 0; i < ((List) crnDetails).size(); ++i) {
			caseDetail = new CaseDetails();
			teamDetails = new ArrayList();
			String crn = (String) ((List) crnDetails).get(i);
			caseDetail.setCrn(crn);
			String interimFlag = interimFlagDetails[i];
			boolean interim1Flag = Boolean.parseBoolean(interimFlag.split("#")[0]);
			boolean interim2Flag = Boolean.parseBoolean(interimFlag.split("#")[1]);
			this.logger.debug("crn :: " + crn + " ::interim1Flag :: " + interim1Flag + " :: interim2Flag :: "
					+ interim2Flag + " :: officeDetails[i] :: " + officeDetails[i]);
			teamTypeDetList = StringUtils.commaSeparatedStringToList(teamTypeDetails[i]);
			officeDetailList = StringUtils.commaSeparatedStringToList(officeDetails[i]);

			for (int j = 0; j < officeDetailList.size(); ++j) {
				teamDetail = new TeamDetails();
				teamDetail.setCrn(crn);
				teamDetail.setTeamTypeId((String) teamTypeDetList.get(j));
				teamDetail.setOffice((String) officeDetailList.get(j));
				teamDetail.setByPassInterim(byPassDetails[i].split(",")[j]);
				if (null != teamDetail.getByPassInterim() && teamDetail.getByPassInterim().trim().length() > 0
						&& teamDetail.getByPassInterim().equals(this.TRUE)) {
					teamDetail.setDueDate1("");
					teamDetail.setDueDate2("");
				} else {
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
				teamDetails.add(teamDetail);
			}

			caseDetail.setTeamList(teamDetails);
			caseDetailsList.add(caseDetail);
		}

		return caseDetailsList;
	}

	public ModelAndView getAutoOfficeDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getAutoOfficeDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("crn in  getCaseDetails :: " + request.getParameter(this.CRN));
			String crn = request.getParameter(this.CRN);
			String subject = "Mail regarding auto office assignment";
			String message = "";
			String mailTo = "aj-lifeline@thepsi.com";
			List<SubjectDetails> subjectList = this.officeAssignmentManager.getCaseSubjectDetails(crn);
			this.logger.debug("subjectList size :: " + subjectList.size());
			HashMap<String, Object> oaDetails = this.officeAssignmentManager.getAOATeamDetails(subjectList, crn);
			List<TeamDetails> teamDetails = (List) oaDetails.get(this.TEAM);
			int teamSize = 0;
			if (null != teamDetails && teamDetails.size() > 0 && teamDetails.size() <= 6) {
				this.logger.debug("team size:: " + teamDetails.size());
				teamSize = teamDetails.size();
			}

			if (null != teamDetails && teamDetails.size() > 6) {
				teamSize = teamDetails.size();
				this.logger.debug("team size:: " + teamSize);
				message = "The number of supporting team for the crn:::" + crn
						+ " is greater than six and having teams " + teamSize
						+ " total no. of supporting teams.Please delete the supporting teams.";
				ResourceLocator.self().getMailService().sendEmail(subject, mailTo, message);
			} else {
				modelAndView.addObject(this.FAILURE_MESSAGE, oaDetails.get(this.MESSAGE));
			}

			modelAndView.addObject(this.TEAM_DETAILS, teamDetails);
			modelAndView.addObject(this.TEAM_SIZE, teamSize);
			modelAndView.addObject(this.CASE_DUEDATES, oaDetails.get(this.CASE_DUEDATES));
			modelAndView.addObject(this.SUBJECT_LIST, subjectList);
			return modelAndView;
		} catch (CMSException var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		} catch (Exception var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		}
	}

	public ModelAndView getTabCaseDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getTabCaseDetails..search crn value :: " + request.getParameter(this.CRN_NO));
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			Session session = SBMUtils.getSession(request);
			MyTaskPageVO taskVo = new MyTaskPageVO();
			String startRequest = request.getParameter("start");
			String limitRequest = request.getParameter("limit");
			this.logger.debug("Start value is " + startRequest + " ::: and limit is " + limitRequest);
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

			this.logger.debug("Start value is " + taskVo.getStart() + " ::: and limit is " + taskVo.getLimit()
					+ " :: searchCrn :: " + taskVo.getCrn() + " primary sub country :: " + taskVo.getCountry());
			HashMap<String, Object> resultMap = this.officeAssignmentManager.getTabCaseDetails(session, taskVo);
			if (resultMap != null && !resultMap.isEmpty()) {
				List<CaseDetails> caseDetailList = (List) resultMap.get("processInstanceList");
				this.logger.debug("caseDetailList size :: " + caseDetailList.size());
				modelAndView.addObject(this.CASE_DETAIL_LIST, caseDetailList);
				modelAndView.addObject("total", resultMap.get("total"));
			}

			return modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView getTabOtherDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getTabCaseDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> biManagers = this.officeAssignmentManager.getBIManagerList();
			this.logger.debug("biManagers :: " + biManagers.size());
			List<UserMasterVO> vendorManagers = this.officeAssignmentManager.getVendorManagerList();
			this.logger.debug("vendorManagers :: " + vendorManagers.size());
			List<TeamTypeVO> teamTypeList = this.officeAssignmentManager.getTeamTypes();
			modelAndView.addObject(this.TEAMTYPE_LIST, teamTypeList);
			modelAndView.addObject(this.BI_MANAGERS, biManagers);
			modelAndView.addObject(this.VENDOR_MANAGERS, vendorManagers);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getAssociatedSubjectsGCC(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In getAssociatedSubjectsGCC method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			new ArrayList();
			String crn = request.getParameter(this.CRN);
			this.logger.debug("crn in  getAssociatedSubjectsGCC :: " + crn);
			List<SubjectDetails> matchedSubjectGCC = this.officeAssignmentManager.getAssociatedSubjectsGCC(crn);
			Iterator iterator = matchedSubjectGCC.iterator();

			while (iterator.hasNext()) {
				SubjectDetails subjectDetails = (SubjectDetails) iterator.next();
				this.logger.debug("subjectDetails.getSubjectId(): " + subjectDetails.getSubjectId());
				this.logger.debug("subjectDetails.getEntityTypeId(): " + subjectDetails.getEntityTypeId());
				this.logger.debug("subjectDetails.getReIds(): " + subjectDetails.getReIds());
				this.logger.debug("subjectDetails.getReJlpPoints(): " + subjectDetails.getReJlpPoints());
			}

			modelAndView.addObject(this.SUBJECT_IDS_MATCH_GCC, matchedSubjectGCC);
			return modelAndView;
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getTabSubjectREDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in getTabSubjectREDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			Session session = SBMUtils.getSession(request);
			MyTaskPageVO taskVo = new MyTaskPageVO();
			String startRequest = request.getParameter("start");
			String limitRequest = request.getParameter("limit");
			this.logger.debug("Start value is " + startRequest + " ::: and limit is " + limitRequest);
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

			this.logger.debug(" searchCrn :: " + taskVo.getCrn() + " primary sub country :: " + taskVo.getCountry());
			HashMap<String, Object> resultMap = this.officeAssignmentManager.getTabSubjectCountryREDetails(session,
					taskVo);
			if (resultMap != null && !resultMap.isEmpty()) {
				List<CaseDetails> autoPopulatedCases = (List) resultMap.get("autoCaseDetails");
				List<CaseDetails> reListForReplication = (List) resultMap.get("reListForReplication");
				modelAndView.addObject(this.AUTO_POPULATED_CASES, autoPopulatedCases);
				modelAndView.addObject("reListForReplication", reListForReplication);
			}

			return modelAndView;
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}

	public ModelAndView getDaysBeforePT(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getDaysBeforePT");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String clientCode = "NONJ001";
			if (null != httpServletRequest.getParameter("clientCode")
					&& httpServletRequest.getParameter("clientCode").equalsIgnoreCase("J001")) {
				clientCode = httpServletRequest.getParameter("clientCode");
			}

			this.logger.debug("clientCode :::: " + clientCode);
			String reportType = httpServletRequest.getParameter("reportType");
			this.logger.debug("reportType :::: " + reportType);
			String teamTypeList = httpServletRequest.getParameter("teamTypeList");
			this.logger.debug("teamTypeList :::: " + teamTypeList);
			String[] teamTypeArray = teamTypeList.split(",");
			String officeList = httpServletRequest.getParameter("officeList");
			this.logger.debug("officeList :::: " + officeList);
			String[] officeArray = officeList.split(",");
			int[] daysBeforeList = new int[teamTypeArray.length];

			for (int i = 0; i < teamTypeArray.length; ++i) {
				if (null != officeArray[i] && !officeArray[i].equalsIgnoreCase("PRC")) {
					officeArray[i] = "NONPRC";
				}

				this.logger.debug("paremeters :: clientCode - " + clientCode + ", reportType" + reportType
						+ ", teamType - " + teamTypeArray[i] + ", office - " + officeArray[i]);
				daysBeforeList[i] = resourceLocator.getOfficeAssignmentService().getDaysBeforePT(clientCode, reportType,
						teamTypeArray[i], officeArray[i]);
				this.logger.debug("Result :: daysBefore - " + daysBeforeList[i]);
			}

			viewForJSON.addObject("daysBeforeList", daysBeforeList);
			return viewForJSON;
		} catch (NullPointerException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (Exception var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		}
	}
}