package com.worldcheck.atlas.frontend.subject;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ISubjectManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class SubjectController extends MultiActionController {
	private String redirectStringForSubjectList = "redirect:getCaseSubjectList.do";
	private String redirectStringForAddSubject = "redirect:addSubjectForCase.do";
	private String subjectEditScreen = "case_Subject_Edit";
	private String subjectAddScreen = "case_Subject_Add";
	private String subjectListScreen = "case_SubList";
	private String subjectRiskScreen = "subject_Risks";
	private String listString = "List";
	private String addString = "Add";
	private String loggerClass = "com.worldcheck.atlas.frontend.subject.SubjectController";
	private String subjectListScreenForCC = "complete_Case_SubList";
	private ILogProducer logger;
	private ISubjectManager subjectManager;
	private String checkClientCode;
	public static final String TOKEN_KEY = "_syncToken";

	public SubjectController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.subjectManager = null;
		this.checkClientCode = "S147";
	}

	public void setSubjectManager(ISubjectManager subjectManager) {
		this.subjectManager = subjectManager;
	}

	public ModelAndView saveSubjectIndustry(HttpServletRequest request, HttpServletResponse response,
			CaseDetails caseDetails) {
		this.logger.debug("Inside saveSubjectIndustry method of SubjectController class");
		this.logger.debug("crn is:::" + caseDetails.getCrn());
		ModelAndView modelAndView = new ModelAndView(this.subjectRiskScreen);
		modelAndView.addObject("activeTab", "0");
		modelAndView.addObject("crn", caseDetails.getCrn());

		try {
			String[] modifiedRecords = caseDetails.getModifiedRecords();
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			ResourceLocator.self().getSubjectService().saveSubjectIndustry(modifiedRecords, currentUser);
			this.logger.info("Subject industries saved successfully for crn : " + caseDetails.getCrn());
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView saveSubjectRisk(HttpServletRequest request, HttpServletResponse response,
			CaseDetails caseDetails) {
		this.logger.debug("Inside saveSubjectRisk method of SubjectController class");
		this.logger.debug("crn is::::" + caseDetails.getCrn());
		ModelAndView modelAndView = new ModelAndView("subject_Risks");
		modelAndView.addObject("activeTab", "1");
		modelAndView.addObject("crn", caseDetails.getCrn());

		try {
			String[] modifiedRecords = caseDetails.getModifiedRecords();
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			ResourceLocator.self().getSubjectService().saveSubjectRisk(modifiedRecords, currentUser);
			this.logger.info("Subject Risks saved successfully for crn : " + caseDetails.getCrn());
			return modelAndView;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getSubjectRiskAndIndustry(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getSubjectRiskAndIndustry method of SubjectController class");
		ModelAndView modelAndView = new ModelAndView(this.subjectRiskScreen);

		try {
			String crn = request.getParameter("crn");
			modelAndView.addObject("crn", crn);
			String riskTypeId;
			if (request.getParameter("sourceReq") != null && request.getParameter("sourceReq").equals("leftNav")) {
				riskTypeId = ResourceLocator.self().getSubjectService().getCaseStatus(crn);
				modelAndView.addObject("crnStatus", riskTypeId);
			}

			riskTypeId = request.getParameter("riskTypeId");
			if (riskTypeId.equals("caseLevelNav")) {
				modelAndView.addObject("activeTab", 0);
			} else if (riskTypeId.equals("subjectLevelNav")) {
				modelAndView.addObject("activeTab", 1);
			}

			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getCaseSubjectList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getCaseSubjectList method of SubjectController class");
		boolean isBizsolo = false;
		ModelAndView modelAndView = new ModelAndView(this.subjectListScreen);

		try {
			String task_name = request.getParameter("taskName");
			String task_Status = request.getParameter("currentCaseStatus");
			String crn = request.getParameter("crn");
			String color = request.getParameter("colour");
			color = "green";
			boolean isExist = false;
			if (task_name != null && task_Status != null) {
				String[] task = task_name.toString().split("%20");
				String[] task_status = task_Status.toString().split("%20");
				String taskName = task[0] + " " + task[1] + " " + task[2];
				String taskStatus = task_status[0] + " " + task_status[1];
				int count = ResourceLocator.self().getSubjectService().getCRNCount(crn);
				if (count == 0 && taskName != null) {
					ResourceLocator.self().getSubjectService().saveColorDetails(taskName, crn, color, taskStatus);
				}
			}

			long pid = ResourceLocator.self().getTaskService().getPID(crn);
			if (pid == 0L) {
				isBizsolo = true;
			}

			modelAndView.addObject("color", color);
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("isBizsolo", isBizsolo);
			return modelAndView;
		} catch (NullPointerException var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		} catch (Exception var17) {
			return AtlasUtils.getExceptionView(this.logger, var17);
		}
	}

	public ModelAndView editSubjectForCase(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subDetails) {
		this.logger.debug("Inside editSubjectForCase method of SubjectController class");
		ModelAndView modelAndView = new ModelAndView(this.subjectEditScreen);

		try {
			String crn = request.getParameter("crn");
			String subID = request.getParameter("subjectID");
			SubjectDetails subDetVO = ResourceLocator.self().getSubjectService().editSubjectForCase(subID);
			subDetVO.setCrn(crn);
			subDetVO.setSubjectId(Integer.parseInt(subID));
			subDetVO.setSubjectAddedBIMgr("");
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("subDetVO", subDetVO);
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView addSubjectForCase(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subDetails) {
		this.logger.debug("Inside addSubjectForCase method of SubjectController class");
		ModelAndView modelAndView = new ModelAndView(this.subjectAddScreen);

		try {
			String crn = request.getParameter("crn");
			SubjectDetails primarysubDetailVO = ResourceLocator.self().getSubjectService().addSubjectForCase(crn);
			SubjectDetails newSubjectDetail = new SubjectDetails();
			newSubjectDetail.setCrn(crn);
			newSubjectDetail = ResourceLocator.self().getSubjectService().getReportTypeForCase(newSubjectDetail);
			if (primarysubDetailVO != null) {
				this.logger.debug("primary subject already exist...");
				this.logger.debug("primary subject entity type id = " + primarysubDetailVO.getEntityTypeId());
				newSubjectDetail.setEntityTypeId(primarysubDetailVO.getEntityTypeId());
				newSubjectDetail.setPrimarySub(false);
				this.logger.debug("primary subject country id is::::::" + primarysubDetailVO.getCountryId());
				newSubjectDetail.setCountryId(primarysubDetailVO.getCountryId());
				newSubjectDetail.setCountryVO(primarysubDetailVO.getCountryVO());
				newSubjectDetail.setCrn(crn);
			} else {
				this.logger.debug("primary subject not exist... so going to add as primary");
				newSubjectDetail.setEntityTypeId(Integer.parseInt("2"));
				newSubjectDetail.setPrimarySub(true);
				newSubjectDetail.setCrn(crn);
				newSubjectDetail.setSubjectAddedBIMgr("");
			}

			modelAndView.addObject("crn", crn);
			modelAndView.addObject("subDetVO", newSubjectDetail);
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	private synchronized boolean isTokenValid(HttpServletRequest request, String token) throws Exception {
		HttpSession session = request.getSession();
		String sessionToken = (String) session.getAttribute("_syncToken");
		if (token == null) {
			throw new Exception("Missing synchronizer token in request");
		} else if (sessionToken == null) {
			throw new Exception("Missing synchronizer token in session");
		} else if (sessionToken.equals(token)) {
			session.setAttribute("_syncToken", nextToken());
			return true;
		} else {
			return false;
		}
	}

	public static String nextToken() {
		long seed = System.currentTimeMillis();
		Random r = new Random();
		r.setSeed(seed);
		return Long.toString(seed) + Long.toString(Math.abs(r.nextLong()));
	}

	public ModelAndView saveSubjectForCase(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subDetails) throws Exception {
		this.logger.debug("Inside saveSubjectForCase method of SubjectController class");
		String token = request.getParameter("_syncToken");
		String view;
		ModelAndView modelAndView;
		String redirectString;
		if (this.isTokenValid(request, token)) {
			view = "";
			modelAndView = null;

			try {
				redirectString = request.getParameter("redirectString");
				if (redirectString.equalsIgnoreCase(this.listString)) {
					view = this.redirectStringForSubjectList;
					modelAndView = new ModelAndView(view);
				} else {
					view = this.redirectStringForAddSubject;
					modelAndView = new ModelAndView(view);
				}

				this.logger.info("Sub report Type for the Subject is hSubReportTypeDesc : "
						+ request.getParameter("hSubReportTypeDesc"));
				if (request.getParameter("hSubReportTypeDesc") != null) {
					subDetails.setSubReportType(request.getParameter("hSubReportTypeDesc"));
				}

				subDetails.setCrn(request.getParameter("crn"));
				subDetails.setSubjectId(Integer.parseInt(request.getParameter("subjectID")));
				UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession()
						.getAttribute("userDetailsBean");
				String currentUser = userDetailsBean.getLoginUserId();
				subDetails.setUpdatedBy(currentUser);
				Session session = SBMUtils.getSession(request);
				String caseHistoryPerformer = "";
				if (request.getSession().getAttribute("loginLevel") != null) {
					caseHistoryPerformer = (String) request.getSession().getAttribute("performedBy");
				}

				SubjectDetails updateSubjectDetials = ResourceLocator.self().getSubjectService()
						.saveSubjectForCase(subDetails, session, caseHistoryPerformer);
				int updateSubjectCount = updateSubjectDetials.getUpdateSubjectCount();
				String message = updateSubjectDetials.getIsisErrorMessge();
				if (updateSubjectCount > 0) {
					this.logger.info("Subject updated successfully for subject Id : " + subDetails.getSubjectId()
							+ " and CRN : " + subDetails.getCrn());
				} else if (updateSubjectCount == 0) {
					this.logger.info("Subject updated failed  for subject Id : " + subDetails.getSubjectId()
							+ " and CRN : " + subDetails.getCrn() + "ISIS ERROR Message" + message);
				} else {
					this.logger.info("Subject updated failed  for subject Id : " + subDetails.getSubjectId()
							+ " and CRN : " + subDetails.getCrn() + "Not able to communicate with EDDO");
				}

				Map model = new HashMap();
				model.put("crn", request.getParameter("crn"));
				model.put("subjectID", request.getParameter("subjectID"));
				modelAndView.addAllObjects(model);
				modelAndView.addObject("updateSubjectCount", updateSubjectCount);
				modelAndView.addObject("ISISErrorMessage", message);
				this.logger.debug("Subject Controller end by Himanshu");
				return modelAndView;
			} catch (NullPointerException var16) {
				return AtlasUtils.getExceptionView(this.logger, var16);
			} catch (Exception var17) {
				return AtlasUtils.getExceptionView(this.logger, var17);
			}
		} else {
			view = "";
			modelAndView = null;
			redirectString = request.getParameter("redirectString");
			if (redirectString.equalsIgnoreCase(this.listString)) {
				view = this.redirectStringForSubjectList;
				modelAndView = new ModelAndView(view);
			} else {
				view = this.redirectStringForAddSubject;
				modelAndView = new ModelAndView(view);
			}

			modelAndView.addObject("tokenInvalid", "You used double click so your second request is declined.");
			return modelAndView;
		}
	}

	public ModelAndView saveNewSubjectForCase(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subDetails) throws Exception {
		String token = request.getParameter("_syncToken");
		this.logger.debug("Inside saveNewSubjectForCase method of SubjectController class");
		String view;
		String redirectString;
		if (this.isTokenValid(request, token)) {
			view = "";
			String redirectString = request.getParameter("redirectString");
			redirectString = null;
			ModelAndView modelAndView;
			if (redirectString.equalsIgnoreCase(this.listString)) {
				view = this.redirectStringForSubjectList;
				modelAndView = new ModelAndView(view);
			} else {
				view = this.redirectStringForAddSubject;
				modelAndView = new ModelAndView(view);
			}

			try {
				subDetails.setCrn(request.getParameter("crn"));
				UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession()
						.getAttribute("userDetailsBean");
				String currentUser = userDetailsBean.getLoginUserId();
				subDetails.setUpdatedBy(currentUser);
				Session session = SBMUtils.getSession(request);
				String caseHistoryPerformer = "";
				if (request.getSession().getAttribute("loginLevel") != null) {
					caseHistoryPerformer = (String) request.getSession().getAttribute("performedBy");
				}

				if (request.getParameter("hSubReportTypeDesc") != null) {
					subDetails.setSubReportType(request.getParameter("hSubReportTypeDesc"));
				}

				SubjectDetails updateSubjectDetials = ResourceLocator.self().getSubjectService()
						.saveNewSubjectForCase(subDetails, session, caseHistoryPerformer);
				int newGeneratedSubjectID = updateSubjectDetials.getNewSubjectId();
				String message = updateSubjectDetials.getIsisErrorMessge();
				if (newGeneratedSubjectID > 0) {
					this.logger.info("Subject added successfully for crn : " + subDetails.getCrn()
							+ " and generated subject id is: " + newGeneratedSubjectID);
					this.logger.debug("newly added subject id is:::::" + newGeneratedSubjectID);
				} else if (newGeneratedSubjectID < 0) {
					this.logger.debug("Communication error with ISIS. Subject not added.");
				} else if (newGeneratedSubjectID == 0) {
					this.logger.debug("Add Subject failed because receive error message from EDDO." + message);
				}

				Map model = new HashMap();
				model.put("crn", request.getParameter("crn"));
				modelAndView.addAllObjects(model);
				modelAndView.addObject("newSubjectId", newGeneratedSubjectID);
				modelAndView.addObject("ISISErrorMessage", message);
				if (this.checkForAutoSubjectAddition(subDetails) && newGeneratedSubjectID > 0) {
					int checkFlag = false;
					this.logger.debug("match found");
					List<SubjectDetails> subjectListForReplication = ResourceLocator.self().getSubjectService()
							.getListOfSubjectForReplication(subDetails);
					this.logger.debug("size for auto replication: " + subjectListForReplication.size());
					Iterator it = subjectListForReplication.iterator();

					while (it.hasNext()) {
						SubjectDetails autoSubject = (SubjectDetails) it.next();
						this.logger.debug("Country:: " + autoSubject.getCountryCode());
						this.logger.debug("RE:: " + autoSubject.getReIds());
						String[] modifiedRecords = subDetails.getModifiedRecords();
						this.logger.debug("modifiedRecords size :  " + modifiedRecords.length);

						for (int i = 0; i < modifiedRecords.length; ++i) {
							String JSONstring = modifiedRecords[i];
							Map jsonObject = (Map) JSONValue.parse(JSONstring);
							String reIDString = autoSubject.getReIds().concat(":true");
							reIDString = reIDString.replaceAll(",", ":true,");
							this.logger.debug("reIDString is::::::" + reIDString);
							String isLegacyString = (String) jsonObject.get("isLegacy");
							JSONObject obj = new JSONObject();
							obj.put("reString", reIDString);
							obj.put("associateCRNString", "");
							obj.put("isLegacy", isLegacyString);
							modifiedRecords[i] = obj.toString();
							this.logger.debug("modifiedRecords[i] \n " + modifiedRecords[i]);
						}

						subDetails.setModifiedRecords(modifiedRecords);
						subDetails.setCountryCode(autoSubject.getCountryCode());
						subDetails.setPrimarySub(false);
						subDetails.setIsReplicated(1);
						SubjectDetails addAutoSubjectDetails = ResourceLocator.self().getSubjectService()
								.saveNewSubjectForCase(subDetails, session, caseHistoryPerformer);
						int autoAddedSubjectID = addAutoSubjectDetails.getNewSubjectId();
						if (autoAddedSubjectID > 0) {
							this.logger.info("#### Duplicate Subject added successfully for crn : "
									+ subDetails.getCrn() + " and generated subject id is: " + autoAddedSubjectID);
							this.logger.debug("####newly added Duplicate subject id is:::::" + autoAddedSubjectID);
						} else if (autoAddedSubjectID < 0) {
							this.logger.debug("####Communication error with ISIS. Duplicate Subject not added.");
						} else if (autoAddedSubjectID == 0) {
							this.logger.debug(
									"####Add Duplicate Subject failed because because receive error message from EDDO."
											+ addAutoSubjectDetails.getIsisErrorMessge());
						}
					}
				} else {
					this.logger.debug("NO match found");
				}

				return modelAndView;
			} catch (NullPointerException var27) {
				return AtlasUtils.getExceptionView(this.logger, var27);
			} catch (Exception var28) {
				return AtlasUtils.getExceptionView(this.logger, var28);
			}
		} else {
			view = "";
			ModelAndView modelAndView = null;
			redirectString = request.getParameter("redirectString");
			if (redirectString.equalsIgnoreCase(this.listString)) {
				view = this.redirectStringForSubjectList;
				modelAndView = new ModelAndView(view);
			} else {
				view = this.redirectStringForAddSubject;
				modelAndView = new ModelAndView(view);
			}

			modelAndView.addObject("tokenInvalid", "You used double click so your second request is declined.");
			return modelAndView;
		}
	}

	public ModelAndView getCaseSubjectListCC(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getCaseSubjectListCC method of SubjectController class");
		boolean isBizsolo = false;
		ModelAndView modelAndView = new ModelAndView(this.subjectListScreenForCC);

		try {
			String crn = request.getParameter("crn");
			long pid = ResourceLocator.self().getTaskService().getPID(crn);
			if (pid == 0L) {
				isBizsolo = true;
			}

			modelAndView.addObject(crn);
			modelAndView.addObject("isBizsolo", isBizsolo);
			return modelAndView;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView getLegacyCaseSubjectList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside getLegacyCaseSubjectList method of SubjectController class");
		ModelAndView modelAndView = new ModelAndView("legacyCaseSubjectList");

		try {
			String crn = request.getParameter("crn");
			modelAndView.addObject("crn", crn);
			return modelAndView;
		} catch (NullPointerException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public boolean checkForAutoSubjectAddition(SubjectDetails subDetails) {
		String clientCode = subDetails.getCrn().split("\\\\")[1];
		boolean flag = false;
		if (clientCode.equals(this.checkClientCode)) {
			try {
				flag = ResourceLocator.self().getSubjectService().checkForAutoSubjectAddition(subDetails);
			} catch (CMSException var5) {
				var5.printStackTrace();
			}
		}

		return flag;
	}
}