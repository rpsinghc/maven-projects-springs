package com.worldcheck.atlas.frontend.casedetails;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ICaseDetailManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CaseInfo;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CaseDetailsController extends MultiActionController {
	private static final String CASE_DETAILS = "caseDetails";
	private static final String CASEINFO_SECTION_JSP = "caseInformation";
	private static final String ACCOUNT_SECTION_JSP = "accountSection";
	private static final String RECCASEINFO_SECTION_JSP = "recCaseInformation";
	private static final String IS_ISIS_UPDATE = "isISISUpdate";
	private ICaseDetailManager caseDetailManager = null;
	private int isISISUpdate = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.casedetails.CaseDetailsController");

	public void setCaseDetailManager(ICaseDetailManager caseDetailManager) {
		this.caseDetailManager = caseDetailManager;
	}

	public ModelAndView caseDetails(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			HttpSession session = request.getSession();
			if (crn != null) {
				session.removeAttribute("caseInfoCRN");
			}

			modelAndView = new ModelAndView("caseInformation");
			new CaseDetails();
			if (session.getAttribute("caseInfoCRN") != null) {
				crn = (String) session.getAttribute("caseInfoCRN");
			}

			this.logger.debug(" checking isisFlag for crn :: " + crn + " :: isLegacy flag in request :: "
					+ request.getParameter("isLegacy"));
			if (crn != null && !crn.equalsIgnoreCase("null") && request.getParameter("isLegacy") != null
					&& !request.getParameter("isLegacy").equalsIgnoreCase("1")) {
				int isisFlag = ResourceLocator.self().getInvoiceService().getBudgetDetails(crn);
				this.logger.debug(" isisFlag for crn :: " + crn + " :: is :: " + isisFlag);
				session.setAttribute("isisFlag", String.valueOf(isisFlag));
			}

			this.logger
					.debug(" session.getAttribute(Constants.TIMETRACKERCRN) " + session.getAttribute("timeTrackerCrn"));
			if (session.getAttribute("timeTrackerCrn") != null
					&& crn.equalsIgnoreCase((String) session.getAttribute("timeTrackerCrn"))) {
				this.logger.debug("task details for same crn");
			} else {
				this.logger.debug("task details for different crn");
				session.removeAttribute("timeTrackerCrn");
				session.removeAttribute("workItem");
			}

			this.logger.debug("crn :: " + crn + "is Legacy " + request.getParameter("isLegacy") + " :: trackerParam :: "
					+ request.getSession().getAttribute("TrackerOn") + " :: ttParam :: "
					+ request.getParameter("ttParam") + " :: show menu :: " + request.getParameter("showMenu"));
			int isLegacy = 0;
			if (request.getParameter("isLegacy") != null && request.getParameter("isLegacy").equalsIgnoreCase("1")) {
				isLegacy = 1;
			}

			int showMenu = 0;
			if (request.getParameter("showMenu") != null && request.getParameter("showMenu").equalsIgnoreCase("true")) {
				showMenu = 1;
			}

			String excelParams = request.getParameter("searchHistory");
			if (excelParams != null && !"undefined".equalsIgnoreCase(excelParams)) {
				modelAndView.addObject("searchHistory", excelParams);
				this.logger.debug("excel params are " + excelParams);
			}

			boolean ttFlag = false;
			CaseDetails caseDetails;
			String caseHistoryPerformer;
			if (isLegacy == 0) {
				caseDetails = this.caseDetailManager.getCaseDetails(crn);
				String caseStatus = caseDetails.getCaseStatus();
				this.logger.debug("caseStatus :: " + caseStatus);
				if (ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(caseDetails.getPid()),
						SBMUtils.getSession(request))) {
					this.logger.debug(
							" task is already completed so setting case cycle as final and case status as completed");
					modelAndView.addObject("currentCaseCycle", "Final");
					modelAndView.addObject("currentCaseStatus", "Completed");
				} else {
					String currentCaseCycle = this.caseDetailManager.getCurrentCaseCycle(SBMUtils.getSession(request),
							caseDetails.getPid());
					modelAndView.addObject("currentCaseCycle", currentCaseCycle);
					caseHistoryPerformer = this.caseDetailManager.getCurrentCaseStatus(SBMUtils.getSession(request),
							caseDetails.getPid());
					this.logger.debug("caseStatusCustom :: " + caseHistoryPerformer + " :: currentCaseCycle :: "
							+ currentCaseCycle + " for CRN " + caseDetails.getCrn());
					modelAndView.addObject("currentCaseStatus", caseHistoryPerformer);
				}

				if (caseStatus != null && caseStatus.trim().length() > 0
						&& !caseStatus.equalsIgnoreCase("Completed Client Submission")
						&& !caseStatus.equalsIgnoreCase("Cancelled") && !caseStatus.equalsIgnoreCase("Completed")) {
					ttFlag = true;
				}
			} else {
				caseDetails = this.caseDetailManager.getLegacyCaseDetails(crn);
			}

			this.logger.debug("ttFlag :: " + ttFlag);
			UserBean userBean = (UserBean) session.getAttribute("userBean");
			ResourceLocator resourceLocator = ResourceLocator.self();
			if (session.getAttribute("TrackerOn") != null) {
				TimeTrackerVO trackerBean = (TimeTrackerVO) session.getAttribute("trackerBean");
				if (trackerBean != null && crn.equalsIgnoreCase(trackerBean.getCrn())) {
					this.logger.debug("tracker is already on for same CRN so no tt action");
					ttFlag = false;
				} else {
					resourceLocator.getTimeTrackerService().stopTimeTrackerForUser(userBean.getUserName());
					session.removeAttribute("trackerBean");
					session.removeAttribute("TrackerOn");
					resourceLocator.getCacheService().removeTTTokenCache(userBean.getUserName());
				}
			}

			if (request.getParameter("ttParam") != null && request.getParameter("ttParam").trim().length() > 0
					&& request.getParameter("ttParam").equalsIgnoreCase("true") && ttFlag) {
				caseHistoryPerformer = null;
				if (session.getAttribute("loginLevel") != null) {
					caseHistoryPerformer = (String) session.getAttribute("performedBy");
				} else {
					caseHistoryPerformer = userBean.getUserName();
				}

				TimeTrackerVO trackerBean = resourceLocator.getTimeTrackerService().checkActiveTaskForUser(
						userBean.getUserName(), crn, SBMUtils.getSession(request), caseHistoryPerformer);
				if (trackerBean != null) {
					session.setAttribute("trackerBean", trackerBean);
					session.setAttribute("TrackerOn", "TrackerOn");
					resourceLocator.getCacheService().updateTTTokenCache(userBean.getUserName(),
							String.valueOf(trackerBean.getTrackerId()));
				}
			}

			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("isLegacy", Integer.valueOf(isLegacy));
			modelAndView.addObject("showMenu", Integer.valueOf(showMenu));
			modelAndView.addObject("caseDetails", caseDetails);
			modelAndView.addObject("isISISUpdate", request.getSession().getAttribute("isISISUpdate"));
			request.getSession().removeAttribute("isISISUpdate");
			return modelAndView;
		} catch (Exception var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}
	}

	public ModelAndView accountSectionRedirction(HttpServletRequest request, HttpServletResponse response,
			Object command) throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			HttpSession session = request.getSession();
			if (crn != null) {
				session.removeAttribute("caseInfoCRN");
			}

			modelAndView = new ModelAndView("accountSection");
			new CaseDetails();
			this.logger.debug(" checking isisFlag for crn :: " + crn + " :: isLegacy flag in request :: "
					+ request.getParameter("isLegacy"));
			if (crn != null && !crn.equalsIgnoreCase("null")) {
				int isisFlag = ResourceLocator.self().getInvoiceService().getBudgetDetails(crn);
				this.logger.debug(" isisFlag for crn :: " + crn + " :: is :: " + isisFlag);
				session.setAttribute("isisFlag", String.valueOf(isisFlag));
			}

			this.logger
					.debug(" session.getAttribute(Constants.TIMETRACKERCRN) " + session.getAttribute("timeTrackerCrn"));
			if (session.getAttribute("timeTrackerCrn") != null
					&& crn.equalsIgnoreCase((String) session.getAttribute("timeTrackerCrn"))) {
				this.logger.debug("task details for same crn");
			} else {
				this.logger.debug("task details for different crn");
				session.removeAttribute("timeTrackerCrn");
				session.removeAttribute("workItem");
			}

			this.logger.debug("crn :: " + crn + "is Legacy " + request.getParameter("isLegacy") + " :: trackerParam :: "
					+ request.getSession().getAttribute("TrackerOn") + " :: ttParam :: "
					+ request.getParameter("ttParam") + " :: show menu :: " + request.getParameter("showMenu"));
			int isLegacy = 0;
			if (request.getParameter("isLegacy") != null && request.getParameter("isLegacy").equalsIgnoreCase("1")) {
				isLegacy = 1;
			}

			int showMenu = 0;
			if (request.getParameter("showMenu") != null && request.getParameter("showMenu").equalsIgnoreCase("true")) {
				showMenu = 1;
			}

			boolean ttFlag = false;
			CaseDetails caseDetails;
			String caseHistoryPerformer;
			if (isLegacy == 0) {
				caseDetails = this.caseDetailManager.getCaseDetails(crn);
				String caseStatus = caseDetails.getCaseStatus();
				this.logger.debug("caseStatus :: " + caseStatus);
				if (ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(caseDetails.getPid()),
						SBMUtils.getSession(request))) {
					this.logger.debug(
							" task is already completed so setting case cycle as final and case status as completed");
					modelAndView.addObject("currentCaseCycle", "Final");
					modelAndView.addObject("currentCaseStatus", "Completed");
				} else {
					String currentCaseCycle = this.caseDetailManager.getCurrentCaseCycle(SBMUtils.getSession(request),
							caseDetails.getPid());
					modelAndView.addObject("currentCaseCycle", currentCaseCycle);
					caseHistoryPerformer = this.caseDetailManager.getCurrentCaseStatus(SBMUtils.getSession(request),
							caseDetails.getPid());
					this.logger.debug("caseStatusCustom :: " + caseHistoryPerformer + " :: currentCaseCycle :: "
							+ currentCaseCycle + " for CRN " + caseDetails.getCrn());
					modelAndView.addObject("currentCaseStatus", caseHistoryPerformer);
				}

				if (caseStatus != null && caseStatus.trim().length() > 0
						&& !caseStatus.equalsIgnoreCase("Completed Client Submission")
						&& !caseStatus.equalsIgnoreCase("Cancelled") && !caseStatus.equalsIgnoreCase("Completed")) {
					ttFlag = true;
				}
			} else {
				caseDetails = this.caseDetailManager.getLegacyCaseDetails(crn);
			}

			this.logger.debug("ttFlag :: " + ttFlag);
			UserBean userBean = (UserBean) session.getAttribute("userBean");
			ResourceLocator resourceLocator = ResourceLocator.self();
			if (session.getAttribute("TrackerOn") != null) {
				TimeTrackerVO trackerBean = (TimeTrackerVO) session.getAttribute("trackerBean");
				if (trackerBean != null && crn.equalsIgnoreCase(trackerBean.getCrn())) {
					this.logger.debug("tracker is already on for same CRN so no tt action");
					ttFlag = false;
				} else {
					resourceLocator.getTimeTrackerService().stopTimeTrackerForUser(userBean.getUserName());
					session.removeAttribute("trackerBean");
					session.removeAttribute("TrackerOn");
					resourceLocator.getCacheService().removeTTTokenCache(userBean.getUserName());
				}
			}

			if (request.getParameter("ttParam") != null && request.getParameter("ttParam").trim().length() > 0
					&& request.getParameter("ttParam").equalsIgnoreCase("true") && ttFlag) {
				caseHistoryPerformer = null;
				if (session.getAttribute("loginLevel") != null) {
					caseHistoryPerformer = (String) session.getAttribute("performedBy");
				} else {
					caseHistoryPerformer = userBean.getUserName();
				}

				TimeTrackerVO trackerBean = resourceLocator.getTimeTrackerService().checkActiveTaskForUser(
						userBean.getUserName(), crn, SBMUtils.getSession(request), caseHistoryPerformer);
				if (trackerBean != null) {
					session.setAttribute("trackerBean", trackerBean);
					session.setAttribute("TrackerOn", "TrackerOn");
					resourceLocator.getCacheService().updateTTTokenCache(userBean.getUserName(),
							String.valueOf(trackerBean.getTrackerId()));
				}
			}

			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("isLegacy", Integer.valueOf(isLegacy));
			modelAndView.addObject("showMenu", Integer.valueOf(showMenu));
			modelAndView.addObject("caseDetails", caseDetails);
			modelAndView.addObject("isISISUpdate", request.getSession().getAttribute("isISISUpdate"));
			request.getSession().removeAttribute("isISISUpdate");
			return modelAndView;
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}
	}

	public ModelAndView caseDetailsForRec(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			modelAndView = new ModelAndView("recCaseInformation");
			new CaseDetails();
			this.logger.debug("crn :: " + crn + "is Legacy " + request.getParameter("isLegacy") + " :: trackerParam :: "
					+ request.getSession().getAttribute("TrackerOn") + " :: ttParam :: "
					+ request.getParameter("ttParam") + " :: show menu :: " + request.getParameter("showMenu"));
			boolean ttFlag = false;
			CaseDetails caseDetails = this.caseDetailManager.getCaseDetails(crn);
			String caseStatus = caseDetails.getCaseStatus();
			this.logger.debug("caseStatus :: " + caseStatus);
			if (caseStatus != null && caseStatus.trim().length() > 0
					&& !caseStatus.equalsIgnoreCase("Completed Client Submission")
					&& !caseStatus.equalsIgnoreCase("Cancelled") && !caseStatus.equalsIgnoreCase("Completed")) {
				ttFlag = true;
			}

			this.logger.debug("ttFlag :: " + ttFlag);
			if (request.getParameter("ttParam") != null && request.getParameter("ttParam").trim().length() > 0
					&& request.getParameter("ttParam").equalsIgnoreCase("true") && ttFlag) {
				ResourceLocator resourceLocator = ResourceLocator.self();
				HttpSession session = request.getSession();
				UserBean userBean = (UserBean) session.getAttribute("userBean");
				if (session.getAttribute("TrackerOn") != null) {
					resourceLocator.getTimeTrackerService().stopTimeTrackerForUser(userBean.getUserName());
					session.removeAttribute("trackerBean");
					session.removeAttribute("TrackerOn");
				}

				String caseHistoryPerformer = null;
				if (session.getAttribute("loginLevel") != null) {
					caseHistoryPerformer = (String) session.getAttribute("performedBy");
				} else {
					caseHistoryPerformer = userBean.getUserName();
				}

				TimeTrackerVO trackerBean = resourceLocator.getTimeTrackerService().checkActiveTaskForUser(
						userBean.getUserName(), crn, SBMUtils.getSession(request), caseHistoryPerformer);
				if (trackerBean != null) {
					session.setAttribute("trackerBean", trackerBean);
					session.setAttribute("TrackerOn", "TrackerOn");
				}
			}

			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("isLegacy", 0);
			modelAndView.addObject("showMenu", 0);
			modelAndView.addObject("caseDetails", caseDetails);
			return modelAndView;
		} catch (Exception var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		}
	}

	public ModelAndView saveCaseInformation(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("redirect:caseDetails.do");
			HashMap<String, Object> dsMap = new HashMap();
			CaseDetails caseDetails = this.populateCaseDetails(request, dsMap);
			request.getSession().setAttribute("caseInfoCRN", caseDetails.getCrn());
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			caseDetails.setUpdatedBy(userBean.getUserName());
			if (request.getSession().getAttribute("loginLevel") != null) {
				caseDetails.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseDetails.setCaseHistoryPerformer(userBean.getUserName());
			}

			this.isISISUpdate = this.caseDetailManager.saveCaseInformation(caseDetails, SBMUtils.getSession(request),
					dsMap);
			this.logger.info("case information updated successfully for crn " + caseDetails.getCrn());
			request.getSession().setAttribute("isISISUpdate", this.isISISUpdate);
			return modelAndView;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView updateClientFeedback(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("redirect:caseDetails.do");
			CaseDetails caseDetails = new CaseDetails();
			if (request.getParameter("crn") != null) {
				caseDetails.setCrn(request.getParameter("crn"));
			}

			String salesRepresentative;
			if (request.getParameter("clientFeedback") != null && !"".equals(request.getParameter("clientFeedback"))) {
				salesRepresentative = request.getParameter("clientFeedback");
				caseDetails.setClientFeedBack(salesRepresentative);
			}

			if (request.getParameter("SFDCOTNNumbers") != null && !"".equals(request.getParameter("SFDCOTNNumbers"))) {
				salesRepresentative = request.getParameter("SFDCOTNNumbers");
				caseDetails.setSfdcOtnNumbers(salesRepresentative);
			}

			if (request.getParameter("RegionalTerritory") != null
					&& !"".equals(request.getParameter("RegionalTerritory"))) {
				salesRepresentative = request.getParameter("RegionalTerritory");
				caseDetails.setRegionalTerritory(salesRepresentative);
			}

			if (request.getParameter("salesRepresentative") != null
					&& !"".equals(request.getParameter("salesRepresentative"))) {
				salesRepresentative = request.getParameter("salesRepresentative");
				caseDetails.setSalesRepresentative(salesRepresentative);
			}

			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			caseDetails.setUpdatedBy(userBean.getUserName());
			if (request.getSession().getAttribute("loginLevel") != null) {
				caseDetails.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseDetails.setCaseHistoryPerformer(userBean.getUserName());
			}

			this.caseDetailManager.updateClientFeedback(caseDetails);
			this.logger.info("client feedback updated successfully for crn " + caseDetails.getCrn());
			request.getSession().setAttribute("caseInfoCRN", caseDetails.getCrn());
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView updateCaseStatus(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("redirect:caseDetails.do?crn=" + request.getParameter("crn"));
			CaseDetails caseDetails = new CaseDetails();
			HashMap<String, Object> dsMap = new HashMap();
			if (request.getParameter("crn") != null) {
				caseDetails.setCrn(request.getParameter("crn"));
			}

			if (request.getParameter("caseStatusName") != null) {
				dsMap.put("CaseStatus", request.getParameter("caseStatusName"));
			}

			if (request.getParameter("caseStatusId") != null) {
				caseDetails.setCaseStatusId(Integer.parseInt(request.getParameter("caseStatusId")));
			}

			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			caseDetails.setUpdatedBy(userBean.getUserName());
			if (request.getSession().getAttribute("loginLevel") != null) {
				caseDetails.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseDetails.setCaseHistoryPerformer(userBean.getUserName());
			}

			this.isISISUpdate = this.caseDetailManager.updateCaseStatusInfo(caseDetails, SBMUtils.getSession(request),
					dsMap);
			this.logger.info("case status updated successfully for crn " + caseDetails.getCrn());
			request.getSession().setAttribute("caseInfoCRN", caseDetails.getCrn());
			request.getSession().setAttribute("isISISUpdate", this.isISISUpdate);
			return modelAndView;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	private CaseDetails populateCaseDetails(HttpServletRequest request, HashMap<String, Object> dsMap)
			throws CMSException {
		CaseDetails caseDetails = new CaseDetails();
		this.logger.debug("request parameters for case details " + request.getParameterMap());

		try {
			if (request.getParameter("crn") != null) {
				caseDetails.setCrn(request.getParameter("crn"));
			}

			if (request.getParameter("isRecurCase") != null) {
				caseDetails.setIsRecurCase(Integer.parseInt(request.getParameter("isRecurCase")));
			}

			if (request.getParameter("caseMgrId") != null) {
				caseDetails.setCaseMgrId(request.getParameter("caseMgrId"));
				dsMap.put("CaseManager", request.getParameter("caseMgrId"));
			}

			if (request.getParameter("office") != null) {
				caseDetails.setOfficeId(request.getParameter("office"));
			}

			if (request.getParameter("caseStatusName") != null) {
				dsMap.put("CaseStatus", request.getParameter("caseStatusName"));
			}

			if (request.getParameter("officeName") != null) {
				dsMap.put("BranchOffice", request.getParameter("officeName"));
			}

			if (request.getParameter("clientRef") != null) {
				dsMap.put("ClientReference", request.getParameter("clientRef"));
				caseDetails.setClientRef(request.getParameter("clientRef"));
			}

			if (request.getParameter("SFDCOTNNumbers") != null) {
				caseDetails.setSfdcOtnNumbers(request.getParameter("SFDCOTNNumbers"));
			}

			if (request.getParameter("RegionalTerritory") != null) {
				caseDetails.setRegionalTerritory(request.getParameter("RegionalTerritory"));
			}

			String CASE_INFO = "caseInfo";
			if (request.getParameter(CASE_INFO) != null) {
				CaseInfo caseInfo = new CaseInfo();
				caseInfo.setCaseInfoBlock(request.getParameter(CASE_INFO));
				HashMap<String, CaseInfo> map = new HashMap();
				map.put("CaseInfoBlock", caseInfo);
				dsMap.put("CaseInfoBlock", map);
				caseDetails.setCaseInfo(request.getParameter(CASE_INFO));
			}

			if (request.getParameter("caseStatusId") != null) {
				caseDetails.setCaseStatusId(Integer.parseInt(request.getParameter("caseStatusId")));
			}

			String salesRepresentative;
			if (request.getParameter("cInterim2") != null && !"".equals(request.getParameter("cInterim2"))) {
				salesRepresentative = request.getParameter("cInterim2");
				dsMap.put("CInterim2", request.getParameter("cInterim2"));
				caseDetails.setcInterim2(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("cInterim1") != null && !"".equals(request.getParameter("cInterim1"))) {
				salesRepresentative = request.getParameter("cInterim1");
				dsMap.put("CInterim1", request.getParameter("cInterim1"));
				caseDetails.setcInterim1(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("finalDueDate") != null && !"".equals(request.getParameter("finalDueDate"))) {
				salesRepresentative = request.getParameter("finalDueDate");
				dsMap.put("CFinal", request.getParameter("finalDueDate"));
				caseDetails.setFinalDueDate(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("rInterim1") != null && !"".equals(request.getParameter("rInterim1"))) {
				salesRepresentative = request.getParameter("rInterim1");
				dsMap.put("RInterim1", request.getParameter("rInterim1"));
				caseDetails.setrInterim1(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("rInterim2") != null && !"".equals(request.getParameter("rInterim2"))) {
				salesRepresentative = request.getParameter("rInterim2");
				dsMap.put("RInterim2", request.getParameter("rInterim2"));
				caseDetails.setrInterim2(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("csentInterim1") != null && !"".equals(request.getParameter("csentInterim1"))) {
				salesRepresentative = request.getParameter("csentInterim1");
				caseDetails.setClientSentDate1(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("csentInterim2") != null && !"".equals(request.getParameter("csentInterim2"))) {
				salesRepresentative = request.getParameter("csentInterim2");
				caseDetails.setClientSentDate2(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("finalsentDate") != null && !"".equals(request.getParameter("finalsentDate"))) {
				salesRepresentative = request.getParameter("finalsentDate");
				caseDetails.setClientFinalDate(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("clientFeedback") != null && !"".equals(request.getParameter("clientFeedback"))) {
				salesRepresentative = request.getParameter("clientFeedback");
				dsMap.put("ClientFeedback", salesRepresentative);
				caseDetails.setClientFeedBack(salesRepresentative);
			}

			if (request.getParameter("finalRDueDate") != null && !"".equals(request.getParameter("finalRDueDate"))) {
				salesRepresentative = request.getParameter("finalRDueDate");
				dsMap.put("RFinal", request.getParameter("finalRDueDate"));
				caseDetails.setFinalRDueDate(new Date(this.sdf.parse(salesRepresentative).getTime()));
			}

			if (request.getParameter("expressCase") != null) {
				salesRepresentative = request.getParameter("expressCase");
				this.logger.debug(" expressCase " + salesRepresentative);
				if (!salesRepresentative.equalsIgnoreCase("on") && !salesRepresentative.equalsIgnoreCase("true")) {
					caseDetails.setExpressCase(0);
					dsMap.put("ExpressCase", false);
				} else {
					caseDetails.setExpressCase(1);
					dsMap.put("ExpressCase", true);
				}
			} else {
				caseDetails.setExpressCase(0);
				dsMap.put("ExpressCase", false);
			}

			if (request.getParameter("salesRepresentative") != null
					&& !"".equals(request.getParameter("salesRepresentative"))) {
				salesRepresentative = request.getParameter("salesRepresentative");
				caseDetails.setSalesRepresentative(salesRepresentative);
			}

			return caseDetails;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public ModelAndView backToSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;
		String excelParams = request.getParameter("searchHistory");
		if (excelParams != null) {
			Map jsonObject = (Map) JSONValue.parse(excelParams);
			if (jsonObject.get("searchType") != null) {
				if (((String) jsonObject.get("searchType")).equals("caseSearch")) {
					modelAndView = new ModelAndView("redirect:caseSearchHistory.do");
				} else if (((String) jsonObject.get("searchType")).equals("recSearch")) {
					modelAndView = new ModelAndView("redirect:caseSearchHistory.do");
				} else if (((String) jsonObject.get("searchType")).equals("teamSearch")) {
					modelAndView = new ModelAndView("redirect:teamSearchHistory.do");
				} else if (((String) jsonObject.get("searchType")).equals("mesgSearch")) {
					modelAndView = new ModelAndView("redirect:mesgSearchHistory.do");
				} else if (((String) jsonObject.get("searchType")).equals("subjectSearch")) {
					modelAndView = new ModelAndView("redirect:subjectSearchHistory.do");
				} else if (((String) jsonObject.get("searchType")).equals("clientFeedback")) {
					modelAndView = new ModelAndView("redirect:feedbackSearchHistory.do");
				}
			}

			request.getSession().setAttribute("searchHistory", excelParams);
			this.logger.debug("excel params are " + excelParams);
			this.logger.debug("searchType === " + (String) jsonObject.get("searchType"));
		}

		return modelAndView;
	}
}