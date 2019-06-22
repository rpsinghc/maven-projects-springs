package com.worldcheck.atlas.frontend.subject;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONSubjectController extends JSONMultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.subject.JSONSubjectController";
	private ILogProducer logger;
	private String caseSubjectIndustryList;
	private String matchingSubjectList;
	private String defaultNewSubjectID;
	private String reFlag;
	public static final String GREY = "grey";
	public static final String RED = "red";
	private static final String PERFORMER = "performer";
	private static final String TEAM_TYPE_ID = "teamTypeId";
	private static final String TASK_NAME = "taskName";
	private static final String ID_OF_TEAM = "idOfTeam";
	private static final String HASH = "#";

	public JSONSubjectController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.caseSubjectIndustryList = "caseSubjectIndustryList";
		this.matchingSubjectList = "matchingSubjectList";
		this.defaultNewSubjectID = "0";
		this.reFlag = "reFlag";
	}

	public ModelAndView getSubjectListForCase(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getSubjectListForCase method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			int disassciateCount = false;
			int associatedCount = false;
			int subjectId = false;
			String subjectName = null;
			this.logger.debug("crn number is:::" + crn);
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("start value is:::" + Integer.parseInt(request.getParameter("start")));
			this.logger.debug("limit value is::" + Integer.parseInt(request.getParameter("limit")));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			int subjectCount = ResourceLocator.self().getSubjectService().getSubjectCount(crn);
			CaseDetails caseDetails = ResourceLocator.self().getSubjectService().getSubjectListForCase(crn,
					request.getParameter("sort"), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			List subjectIdList = caseDetails.getSubjectList();
			SubjectDetails subjectDetails;
			SubjectDetails subjectColor;
			if (subjectIdList != null && subjectIdList.size() > 0) {
				for (Iterator iterator = subjectIdList.iterator(); iterator.hasNext(); subjectDetails
						.setColor(subjectColor.getColor())) {
					subjectDetails = (SubjectDetails) iterator.next();
					this.logger.debug("subject ID::" + subjectDetails.getSubjectId());
					int subjectId = subjectDetails.getSubjectId();
					subjectName = subjectDetails.getSubjectName();
					subjectColor = ResourceLocator.self().getSubjectService().getSubjectColorDetails(crn, subjectId);
					if (subjectColor == null || subjectColor.getColor() == null
							|| subjectColor.getColor().length() == 0) {
						subjectColor = new SubjectDetails();
						subjectColor.setColor("red");
						this.logger.debug("IN IF subjectColor.getColor():::" + subjectColor.getColor());
					}

					if (!"green".equals(subjectColor.getColor())) {
						int disassciateCount = ResourceLocator.self().getSubjectService()
								.getDisAssociatedSubjectCount(subjectId);
						int associatedCount = ResourceLocator.self().getSubjectService()
								.getAssociatedSubjectCount(subjectName, subjectId, crn);
						this.logger.debug("DisassciateCount:::" + disassciateCount);
						this.logger.debug("associatedCount:::" + associatedCount);
						if (disassciateCount == 0 && associatedCount == 0) {
							subjectColor.setColor("grey");
						}
					}
				}
			}

			modelAndView.addObject("caseDetails", caseDetails);
			modelAndView.addObject("total", subjectCount);
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		} catch (Exception var16) {
			return AtlasUtils.getJsonExceptionView(this.logger, var16, response);
		}
	}

	public ModelAndView getAssociateCasesForSub(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getAssociateCasesForSub method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			String subID = request.getParameter("subjectID");
			boolean isExist = false;
			List<String> assCaseList = ResourceLocator.self().getSubjectService().getAssociateCasesForSub(subID,
					request.getParameter("sort"), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			modelAndView.addObject("assCaseList", assCaseList);
			modelAndView.addObject("total", ResourceLocator.self().getSubjectService().getAsscciateCaseCount(subID));
			modelAndView.addObject("success", true);
			int count = ResourceLocator.self().getSubjectService().getSubjectIdCount(subID);
			if (count == 0) {
				ResourceLocator.self().getSubjectService().addColorForSubject(subID, crn);
			}

			return modelAndView;
		} catch (NullPointerException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView deleteAssCRN(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside deleteAssCRN method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crnString = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crnString);
			String subID = request.getParameter("associateSubjectID");
			ResourceLocator.self().getSubjectService().deleteAssCRN(crnString, subID);
			return modelAndView;
		} catch (NullPointerException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView deleteSubject(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside deleteSubject method of JSONSubjectController class");
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			String subNameString = request.getParameter("subNameString");
			String subIDString = request.getParameter("subIDString");
			String alertMes = "";
			String biAlertMes = "";
			Date updatedOn = null;
			if (crn != null && subNameString != null && subIDString != null) {
				alertMes = ResourceLocator.self().getSubjectService().doSubjectDeletionValidation(crn, subNameString,
						subIDString);
				biAlertMes = ResourceLocator.self().getSubjectService().doSubjectBIValidation(crn, subNameString,
						subIDString);
				updatedOn = ResourceLocator.self().getSubjectService().getUpdateOnForSubject(new Integer(subIDString));
			}

			if (alertMes.length() == 0) {
				alertMes = "success";
			}

			mv.addObject("alert", alertMes);
			mv.addObject("biAlertMessage", biAlertMes);
			mv.addObject("updateOnTime", updatedOn);
			return mv;
		} catch (NullPointerException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView confirmDeleteSubject(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside confirmDeleteSubject method of JSONSubjectController class");
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			String subNameString = request.getParameter("subNameString");
			String subIDString = request.getParameter("subIDString");
			String isBIDelete = request.getParameter("isBIDelete");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			Session session = SBMUtils.getSession(request);
			String alertMes = "";
			String deleteAlert = "failure";
			String isisErrorMessage = "";
			if (crn != null && subNameString != null && subIDString != null) {
				String caseHistoryPerformer = "";
				if (request.getSession().getAttribute("loginLevel") != null) {
					caseHistoryPerformer = (String) request.getSession().getAttribute("performedBy");
				}

				SubjectDetails deleteSubjectDetials = ResourceLocator.self().getSubjectService().confirmDeleteSubject(
						subIDString, subNameString, crn, currentUser, isBIDelete, session, caseHistoryPerformer);
				int deleteSubjectCount = deleteSubjectDetials.getUpdateSubjectCount();
				isisErrorMessage = deleteSubjectDetials.getIsisErrorMessge();
				if (deleteSubjectCount > 0) {
					this.logger
							.info("Subject deleted successfully for subject Id : " + subIDString + " and CRN : " + crn);
					deleteAlert = "success";
				} else if (deleteSubjectCount == 0) {
					deleteAlert = "queueError";
					isisErrorMessage = deleteSubjectDetials.getIsisErrorMessge();
				}
			}

			mv.addObject("alert", deleteAlert);
			mv.addObject("ISISErrorMessage", isisErrorMessage);
			return mv;
		} catch (NullPointerException var17) {
			return AtlasUtils.getJsonExceptionView(this.logger, var17, response);
		} catch (Exception var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		}
	}

	public ModelAndView getResearchElements(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getResearchElements method of JSONSubjectController class");
		List list = null;
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String entity = request.getParameter("entity");
			String subjectEntity = request.getParameter("subjectEntity");
			String crn = request.getParameter("crn");
			String subjectID = request.getParameter("subjectID");
			String isSubjLevelSubRptReq = request.getParameter("isSubjLevelSubRptReq");
			String formAction = request.getParameter("formAction");
			String subReportTypeId = request.getParameter("subReportTypeId");
			if (formAction == null) {
				formAction = "load";
			}

			if (isSubjLevelSubRptReq == null) {
				isSubjLevelSubRptReq = "0";
			}

			list = ResourceLocator.self().getSubjectService().getResearchElements(entity, subjectEntity, crn, subjectID,
					isSubjLevelSubRptReq, formAction, subReportTypeId);
			modelAndView.addObject("list", list);
			return modelAndView;
		} catch (NullPointerException var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		} catch (Exception var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		}
	}

	public ModelAndView getResearchElementsForNewSubject(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getResearchElementsForNewSubject method of JSONSubjectController class");
		List list = null;
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String subjectEntity = request.getParameter("subjectEntity");
			String crn = request.getParameter("crn");
			String subReportTypeId = request.getParameter("subReportTypeId");
			String isSubjLevelSubRptReq = request.getParameter("isSubjLevelSubRptReq");
			if (isSubjLevelSubRptReq == null) {
				isSubjLevelSubRptReq = "0";
			}

			list = ResourceLocator.self().getSubjectService().getResearchElementsForNewSubject(subjectEntity, crn,
					subReportTypeId, isSubjLevelSubRptReq);
			modelAndView.addObject("list", list);
			return modelAndView;
		} catch (NullPointerException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getCaseSubjectsIndustryList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getCaseSubjectsIndustryList method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			List caseSubIndustryList = ResourceLocator.self().getSubjectService().getCaseSubjectsIndustryList(crn,
					request.getParameter("sort"), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			modelAndView.addObject(this.caseSubjectIndustryList, caseSubIndustryList);
			modelAndView.addObject("total", ResourceLocator.self().getSubjectService().getSubjectIndustryCount(crn));
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getMatchingAssociateCasesForSub(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getMatchingAssociateCasesForSub method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String subjectName = request.getParameter("subjectName");
			String subjectID = this.defaultNewSubjectID;
			if (request.getParameter("subjectID") != null && !request.getParameter("subjectID").equals("")) {
				subjectID = request.getParameter("subjectID");
			}

			String crn = request.getParameter("crn");
			List<CaseDetails> caseMatchingSubjectList = ResourceLocator.self().getSubjectService()
					.getMatchingAssociateCasesForSub(subjectName, subjectID, crn, request.getParameter("sort"),
							Integer.parseInt(request.getParameter("start")),
							Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			modelAndView.addObject(this.matchingSubjectList, caseMatchingSubjectList);
			modelAndView.addObject("total", ResourceLocator.self().getSubjectService()
					.getMatchingAssociateCasesCount(subjectName, subjectID, crn));
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getMatchingAssociateCasesForAddSub(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getMatchingAssociateCasesForSub method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String subjectName = request.getParameter("subjectName");
			String subjectID = this.defaultNewSubjectID;
			if (request.getParameter("subjectID") != null && !request.getParameter("subjectID").equals("")) {
				subjectID = request.getParameter("subjectID");
			}

			String crn = request.getParameter("crn");
			List<CaseDetails> caseMatchingSubjectList = ResourceLocator.self().getSubjectService()
					.getMatchingAssociateCasesForAddSub(subjectName, subjectID, crn);
			modelAndView.addObject(this.matchingSubjectList, caseMatchingSubjectList);
			modelAndView.addObject("total", ResourceLocator.self().getSubjectService()
					.getMatchingAssociateCasesCount(subjectName, subjectID, crn));
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView addAssociateCaseForSubject(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside addAssociateCaseForSubject method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String crn = request.getParameter("crn");
		this.logger.debug("crn number is:::" + crn);
		String isLegacy = request.getParameter("isLegacy");
		this.logger.debug("isLegacy value is::::" + isLegacy);
		String subID = request.getParameter("associateSubjectID");
		UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
		String currentUser = userDetailsBean.getLoginUserId();

		try {
			ResourceLocator.self().getSubjectService().addAssociateCaseForSubject(subID, crn, currentUser, isLegacy);
			return modelAndView;
		} catch (NullPointerException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView popualteCountryList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryMasterVO> finalList = ResourceLocator.self().getSubjectService().getCountryListForSubject();
			this.logger.debug("size of country list is:::" + finalList.size());
			modelAndView.addObject("countryMasterList", finalList);
			return modelAndView;
		} catch (NullPointerException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getSubjectRiskForCase(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getSubjectRiskForCase method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			CaseDetails caseDetails = ResourceLocator.self().getSubjectService().getSubjectRiskForCase(crn,
					request.getParameter("sort"), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			CaseDetails caseDetails2 = new CaseDetails();
			Iterator iterator = caseDetails.getSubjectList().iterator();

			SubjectDetails subjectDetails;
			Iterator iterator2;
			while (iterator.hasNext()) {
				subjectDetails = (SubjectDetails) iterator.next();

				RisksMasterVO var10;
				for (iterator2 = subjectDetails.getSubjectRisk().iterator(); iterator2
						.hasNext(); var10 = (RisksMasterVO) iterator2.next()) {
					;
				}
			}

			List<SubjectDetails> subjectDetailList2 = new ArrayList();
			iterator2 = caseDetails.getSubjectList().iterator();

			while (iterator2.hasNext()) {
				subjectDetails = (SubjectDetails) iterator2.next();
				SubjectDetails subjectDetails2 = new SubjectDetails();
				subjectDetails2.setSubjectId(subjectDetails.getSubjectId());
				subjectDetails2.setSubjectName(subjectDetails.getSubjectName());
				subjectDetails2.setCaseStatus(subjectDetails.getCaseStatus());
				subjectDetails2.setCountryVO(subjectDetails.getCountryVO());
				List<RisksMasterVO> risksMasterVOList = new ArrayList();
				RisksMasterVO otherRiskVO = null;
				Iterator var14 = subjectDetails.getSubjectRisk().iterator();

				while (var14.hasNext()) {
					RisksMasterVO riskMasterVO = (RisksMasterVO) var14.next();
					RisksMasterVO riskMasterVO2 = new RisksMasterVO();
					riskMasterVO2.setRiskcode(riskMasterVO.getRiskcode());
					riskMasterVO2.setRisks(riskMasterVO.getRisks());
					riskMasterVO2.setRisksStatus(riskMasterVO.getRisksStatus());
					if (riskMasterVO2.getRisks().equalsIgnoreCase("Other Risk Factors")) {
						otherRiskVO = riskMasterVO2;
					} else {
						risksMasterVOList.add(riskMasterVO2);
					}
				}

				if (otherRiskVO != null) {
					risksMasterVOList.add(otherRiskVO);
				}

				subjectDetails2.setSubjectRisk(risksMasterVOList);
				subjectDetailList2.add(subjectDetails2);
			}

			caseDetails2.setSubjectList(subjectDetailList2);
			modelAndView.addObject("caseDetails", caseDetails2);
			modelAndView.addObject("total", ResourceLocator.self().getSubjectService().getSubjectCount(crn));
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var16) {
			return AtlasUtils.getJsonExceptionView(this.logger, var16, response);
		} catch (Exception var17) {
			return AtlasUtils.getJsonExceptionView(this.logger, var17, response);
		}
	}

	public ModelAndView subjectNameAndCountryValidation(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside subjectNameAndCountryValidation method of JSONSubjectController class");
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			String subjectID = "0";
			Date updatedOn = null;
			int associateCasesCount = 0;
			boolean reUpdateFlag = false;
			String updateBiTeamResult = "";
			List biDeleteString = new ArrayList();
			new ArrayList();
			boolean biTeamResult = false;
			String crn = request.getParameter("crn");
			Session session = SBMUtils.getSession(request);
			this.logger.debug("crn number is:::" + crn);
			String subjectName = request.getParameter("subjectName");
			String countryName = request.getParameter("countryName");
			String entityType = request.getParameter("entityType");
			String reString = request.getParameter("reString");
			this.logger.debug("subjectID is::::::::::>>" + subjectID);
			this.logger.debug("subjectID is::::::::::>>>>" + request.getParameter("subjectID"));
			this.logger.debug("reString is::::::-" + request.getParameter("reString"));
			if (request.getParameter("subjectID") != null && !request.getParameter("subjectID").equalsIgnoreCase("")) {
				this.logger.debug("inside if");
				subjectID = request.getParameter("subjectID");
			} else {
				this.logger.debug("else..");
				subjectID = "0";
			}

			this.logger.debug("subjectID is::::::::::" + subjectID);
			int validationCount = false;
			String alertMes = "";
			if (crn != null && subjectName != null) {
				;
			}

			if (!validationCount) {
				alertMes = "success";
				associateCasesCount = ResourceLocator.self().getSubjectService()
						.getMatchingAssociateCasesCount(subjectName, subjectID, crn);
				if (!"0".equals(subjectID)) {
					reUpdateFlag = ResourceLocator.self().getSubjectService().doReValidation(reString,
							Integer.parseInt(subjectID));
					biDeleteString = ResourceLocator.self().getSubjectService().getupdateSubjectTeamDeleteInfo(crn,
							subjectID, reString);
					updateBiTeamResult = ResourceLocator.self().getSubjectService().getUpdateSubjectBITeamInfo(crn,
							subjectID, reString);
				}

				this.logger.debug("reUpdateFlag value is:::" + reUpdateFlag);
			} else {
				alertMes = "Invalid Subject Name,Country Name And Entity Type Combination";
			}

			updatedOn = ResourceLocator.self().getSubjectService().getUpdateOnForSubject(new Integer(subjectID));
			this.logger.debug("crn is::::::" + crn + "::::::::session is::::" + session);
			boolean officeAssignmentDoneFlag = ResourceLocator.self().getSubjectService()
					.checkIfOfficeAssignmnetDone(crn, session);
			this.logger.debug("officeAssignmentDoneFlag is" + officeAssignmentDoneFlag);
			this.logger.debug("alertMes for subjectNameAndCountryValidation is" + alertMes);
			mv.addObject(this.reFlag, reUpdateFlag);
			mv.addObject("alert", alertMes);
			mv.addObject("total", new Integer(associateCasesCount));
			mv.addObject("updateOnTime", updatedOn);
			mv.addObject("officeAssignmentFlag", officeAssignmentDoneFlag);
			biTeamResult = ResourceLocator.self().getSubjectService().checkForBI(crn, reString);
			this.logger.debug("biTeamResult::" + biTeamResult + "::biDeleteString::" + biDeleteString
					+ "::::updateBiTeamResult::" + updateBiTeamResult);
			mv.addObject("biTeamResultFlag", biTeamResult);
			mv.addObject("biDeleteMesgString", biDeleteString);
			mv.addObject("updateBiTeamResultString", updateBiTeamResult);
			return mv;
		} catch (NullPointerException var21) {
			return AtlasUtils.getJsonExceptionView(this.logger, var21, response);
		} catch (Exception var22) {
			return AtlasUtils.getJsonExceptionView(this.logger, var22, response);
		}
	}

	public ModelAndView getBIManagerList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside isBITeamExist method of JSONSubjectController class");
		ModelAndView mv = new ModelAndView("jsonView");
		new ArrayList();

		try {
			String crn = request.getParameter("crn");
			List biManagerNames = ResourceLocator.self().getOfficeAssignmentService().getBIManagerList();
			this.logger.debug("biManagerNames value is::" + biManagerNames);
			mv.addObject("biManagerNamesList", biManagerNames);
			return mv;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView biValidation(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside biValidation method of JSONSubjectController class");
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			String subjectID = "0";
			boolean biTeamResult = false;
			String crn = request.getParameter("crn");
			String reString = request.getParameter("reString");
			this.logger.debug("reString is::::::-" + request.getParameter("reString"));
			if (request.getParameter("subjectID") != null && !request.getParameter("subjectID").equalsIgnoreCase("")) {
				this.logger.debug("inside if");
				subjectID = request.getParameter("subjectID");
			} else {
				this.logger.debug("else..");
				subjectID = "0";
			}

			biTeamResult = ResourceLocator.self().getSubjectService().checkForBI(crn, reString);
			List biDeleteString = ResourceLocator.self().getSubjectService().getupdateSubjectTeamDeleteInfo(crn,
					subjectID, reString);
			String updateBiTeamResult = ResourceLocator.self().getSubjectService().getUpdateSubjectBITeamInfo(crn,
					subjectID, reString);
			this.logger.debug("biTeamResult::" + biTeamResult + "::biDeleteString::" + biDeleteString
					+ "::::updateBiTeamResult::" + updateBiTeamResult);
			mv.addObject("biTeamResultFlag", biTeamResult);
			mv.addObject("biDeleteMesgString", biDeleteString);
			mv.addObject("updateBiTeamResultString", updateBiTeamResult);
			return mv;
		} catch (NullPointerException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView saveSubjectRisk(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside saveSubjectRisk method of SubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String[] modifiedRecords = request.getParameterValues("ModifiedRisk");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			ResourceLocator.self().getSubjectService().saveSubjectRisk(modifiedRecords, currentUser);
			this.logger.debug("Subject risks updated successfully");
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView getLegacyCaseSubjectDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getLegacyCaseSubjectDetails method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("start value is:::" + Integer.parseInt(request.getParameter("start")));
			this.logger.debug("limit value is::" + Integer.parseInt(request.getParameter("limit")));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			int subjectCount = ResourceLocator.self().getSubjectService().getLegacyCaseSubjectCount(crn);
			this.logger.debug("total count is:::::" + subjectCount);
			List legacySubjectDetails = ResourceLocator.self().getSubjectService().getLegacyCaseSubjectDetails(crn,
					request.getParameter("sort"), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			modelAndView.addObject("legacySubjectDetails", legacySubjectDetails);
			modelAndView.addObject("total", subjectCount);
			return modelAndView;
		} catch (NullPointerException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getLegacyPastRecords(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getLegacyCaseSubjectDetails method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			String subjectId = request.getParameter("subjectID");
			this.logger.debug("crn number is:::" + crn);
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("start value is:::" + Integer.parseInt(request.getParameter("start")));
			this.logger.debug("limit value is::" + Integer.parseInt(request.getParameter("limit")));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			int subjectCount = ResourceLocator.self().getSubjectService().getLegacyPastRecordsCount(crn, subjectId);
			this.logger.debug("total count is:::::" + subjectCount);
			List pastRecordsList = ResourceLocator.self().getSubjectService().getLegacyPastRecords(crn, subjectId,
					request.getParameter("sort"), Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("dir"));
			modelAndView.addObject("pastRecordsList", pastRecordsList);
			modelAndView.addObject("total", subjectCount);
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getCaseLevelRiskCategory(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getCaseLevelRiskCategory method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			List caseLevelCategoryList = ResourceLocator.self().getSubjectService().getCaseLevelRiskCategory(crn);
			modelAndView.addObject("caseLevelCategoryList", caseLevelCategoryList);
			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getSubjectLevelRiskCategory(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getSubjectLevelRiskCategory method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			List subjectLevelCategoryList = ResourceLocator.self().getSubjectService().getSubjectLevelRiskCategory(crn);
			modelAndView.addObject("subjectLevelCategoryList", subjectLevelCategoryList);
			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getSubjectLevelRiskCategoryForResearch(HttpServletRequest request,
			HttpServletResponse response) {
		this.logger.debug("Inside getSubjectLevelRiskCategory method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = "";
			String performer = "";
			String teamTypeId = "";
			String taskName = "";
			String idOfTeam = "";
			String teamName = "";
			String teamId = "";
			if (request.getParameter("crn") != null) {
				crn = request.getParameter("crn");
			}

			this.logger.debug("crn number is:::" + crn);
			if (request.getParameter("performer") != null) {
				performer = request.getParameter("performer");
			}

			this.logger.debug("PERFORMER is:::" + performer);
			if (request.getParameter("teamTypeId") != null) {
				teamTypeId = request.getParameter("teamTypeId");
			}

			if (request.getParameter("taskName") != null) {
				taskName = request.getParameter("taskName");
			}

			if (request.getParameter("idOfTeam") != null) {
				idOfTeam = request.getParameter("idOfTeam");
			}

			this.logger.debug("Team Type Id is:::" + teamTypeId);
			this.logger.debug("Team Id is:::" + idOfTeam);
			if (idOfTeam.trim().length() > 0) {
				String[] temp = idOfTeam.split("#");
				teamName = temp[0];
				teamId = temp[1];
			}

			List subjectLevelCategoryList = ResourceLocator.self().getSubjectService().getSubjectLevelRiskCategory(crn,
					performer, teamTypeId, taskName, teamName, teamId);
			modelAndView.addObject("subjectLevelCategoryList", subjectLevelCategoryList);
			return modelAndView;
		} catch (NullPointerException var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		} catch (Exception var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		}
	}

	public ModelAndView getCRNStatus(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getCaseStatus method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String caseStatus = "";

		try {
			String crn = request.getParameter("crn");
			caseStatus = ResourceLocator.self().getSubjectService().getCaseStatus(crn);
			modelAndView.addObject("crnStatus", caseStatus);
			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView watchListedsubjectValidation(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside watchListedsubjectValidation method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String subjectName = request.getParameter("subjectName");

		try {
			String isWatchListed = ResourceLocator.self().getSubjectService().isWatchListedSubject(subjectName);
			modelAndView.addObject("iswatchListed", isWatchListed);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView updateSubjectBudget(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In JSONSubjectController :updateSubjectBudget method");
		int result = false;
		long count = 0L;
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String gridData = request.getParameter("jsonData");
			String caseFee = request.getParameter("newCaseFee");
			String crn = request.getParameter("crn");
			int result = ResourceLocator.self().getSubjectService().updateSubjectBudget(gridData, crn);
			this.logger.debug("New Case Fee " + caseFee);
			this.logger.debug("CRN:" + crn);
			modelAndView.addObject("updateCount", result);
			this.logger.debug("JSONSubjectController: updateSubjectBudget: result: " + result);
			return modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView getSubjectDetailsForCRN(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getsubjectDetailsForCRN method of JSONSubjectController class");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn number is:::" + crn);
			List<SubjectDetails> subjectDetails = ResourceLocator.self().getSubjectService()
					.getSubjectDetailsForCRN(crn);
			modelAndView.addObject("subjectDetails", subjectDetails);
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}
}