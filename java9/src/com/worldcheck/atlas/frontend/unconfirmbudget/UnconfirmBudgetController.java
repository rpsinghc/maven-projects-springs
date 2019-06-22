package com.worldcheck.atlas.frontend.unconfirmbudget;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IUnconfirmBudget;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UnconfirmBudgetController extends MultiActionController {
	private String JSP = "isisConfirmation";
	private String REDIRECT_URL = "redirect:isisConfirmation.do";
	private String REDIRECT_URL_SUBMIT = "redirect:isisConfirmationsubmit.do";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.unconfirmbudget.UnconfirmBudgetController");
	private IUnconfirmBudget unconfirmBudgetManager = null;
	ModelAndView modelAndView = null;

	public void setUnconfirmBudgetManager(IUnconfirmBudget unconfirmBudgetManager) {
		this.unconfirmBudgetManager = unconfirmBudgetManager;
	}

	public ModelAndView isisConfirmation(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in isisConfirmation ");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn :: " + crn);
			String updateCount = request.getParameter("updateCount");
			this.logger.debug("updateCount :: " + updateCount);
			UnconfimredBudgetVO vo1 = this.unconfirmBudgetManager.getBudgetDetails(crn);
			vo1.setIsCBDFail("false");
			String officeIdForCRn = this.unconfirmBudgetManager.getCrnOffice(crn);
			this.modelAndView = new ModelAndView(this.JSP);
			this.modelAndView.addObject("crnOffice", officeIdForCRn);
			this.modelAndView.addObject("crn", crn);
			this.modelAndView.addObject("UnconfirmVO", vo1);
			this.modelAndView.addObject("updateCount", updateCount);
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}

		return this.modelAndView;
	}

	public ModelAndView isisConfirmationsubmit(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In UnconfirmBudgetController:isisConfirmationsubmit");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn :: " + crn);
			String updateCount = request.getParameter("updateCount");
			this.logger.debug("updateCount :: " + updateCount);
			HttpSession session = request.getSession();
			UnconfimredBudgetVO vo1 = (UnconfimredBudgetVO) session.getAttribute("UnconfirmBudgeVO");
			this.logger.debug("Store value" + vo1.getStore());
			vo1.setIsCBDFail("true");
			String officeIdForCRn = this.unconfirmBudgetManager.getCrnOffice(crn);
			this.modelAndView = new ModelAndView(this.JSP);
			this.modelAndView.addObject("crnOffice", officeIdForCRn);
			this.modelAndView.addObject("crn", crn);
			this.modelAndView.addObject("UnconfirmVO", vo1);
			this.modelAndView.addObject("updateCount", updateCount);
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		return this.modelAndView;
	}

	public ModelAndView saveISISBudgetDetails(HttpServletRequest request, HttpServletResponse response,
			UnconfimredBudgetVO unconfimredBudgetVO) {
		this.logger.debug("in saveISISBudgetDetails :: " + unconfimredBudgetVO.getIsConfirmed() + " :: case fee :: "
				+ unconfimredBudgetVO.getCaseFee());
		this.modelAndView = new ModelAndView(this.REDIRECT_URL);

		try {
			String[] modifiedRecord = request.getParameterValues("modifiedRecords");
			String isSubreportRequire = request.getParameter("isSubreportrequire");
			String grid = request.getParameter("SubjectGrid");
			unconfimredBudgetVO.setSubjectGrid(grid);
			unconfimredBudgetVO.setModifiedRecords(modifiedRecord);
			unconfimredBudgetVO.setIsSubreportRequire(isSubreportRequire);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			unconfimredBudgetVO.setUpdatedBy(userBean.getUserName());
			this.logger.debug("UpdatedBy :: " + userBean.getUserName());
			this.logger.debug("CaseHistoryPerformer :: " + unconfimredBudgetVO.getCaseHistoryPerformer());
			unconfimredBudgetVO.setUpdatedBy(userBean.getUserName());
			if (request.getSession().getAttribute("loginLevel") != null) {
				unconfimredBudgetVO.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				unconfimredBudgetVO.setCaseHistoryPerformer(userBean.getUserName());
			}

			int updateCount = this.unconfirmBudgetManager.saveISISDetails(unconfimredBudgetVO,
					SBMUtils.getSession(request));
			this.logger.debug("updateCount value is ::" + updateCount);
			if (Integer.parseInt(unconfimredBudgetVO.getIsConfirmed()) == 0) {
				ResourceLocator.self().getCacheService().updateBudgetCountCache();
			}

			this.modelAndView.addObject("updateCount", updateCount);
			this.modelAndView.addObject("crn", unconfimredBudgetVO.getCRN());
			if (updateCount == -2) {
				this.logger.debug("Information provided was not correct.");
			} else if (updateCount == -1) {
				this.logger.debug("Communication error with ISIS.");
			} else if (updateCount == 0) {
				this.logger.debug(
						"Case Cancellation failed because additional actions for this case is still pending at ISIS end.");
			} else {
				this.logger.debug("isis budget details saved Successfully..");
			}
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}

		return this.modelAndView;
	}

	public ModelAndView goToUnconfirmBudget(HttpServletRequest request, HttpServletResponse response) {
		return this.modelAndView = new ModelAndView("budgetDueDate_custom");
	}

	public ModelAndView saveUnconfirmBudgetDetails(HttpServletRequest request, HttpServletResponse response,
			UnconfimredBudgetVO unconfimredBudgetVO) {
		this.logger.debug("in saveUnconfirmBudgetDetails");
		this.modelAndView = new ModelAndView("redirect:goToUnconfirmBudget.do");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (request.getSession().getAttribute("loginLevel") != null) {
				unconfimredBudgetVO.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				unconfimredBudgetVO.setCaseHistoryPerformer(userBean.getUserName());
			}

			String failedCrnList = this.unconfirmBudgetManager.saveUnconfirmDetails(unconfimredBudgetVO,
					userBean.getUserName(), unconfimredBudgetVO.getCaseHistoryPerformer(),
					SBMUtils.getSession(request));
			ResourceLocator.self().getCacheService().updateBudgetCountCache();
			if (failedCrnList.isEmpty()) {
				this.logger.debug("budget details saved Successfully..");
			} else if (failedCrnList.equalsIgnoreCase("failure")) {
				this.logger.debug("budget details not saved. Communication error with ISIS..");
			} else {
				this.logger.debug(
						"Confirm Budget and Due Date failed because additional actions for this case is still pending at ISIS end.");
			}

			this.modelAndView.addObject("failedCrnList", failedCrnList);
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}

		return this.modelAndView;
	}

	public ModelAndView getBudgetRecords(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("inside getBudgetRecords");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			HttpSession session = httpServletRequest.getSession();
			UnconfimredBudgetVO vo = new UnconfimredBudgetVO();
			vo.setSortColumnName(httpServletRequest.getParameter("sort"));
			vo.setSortType(httpServletRequest.getParameter("dir"));
			vo.setLimit(Integer.parseInt(httpServletRequest.getParameter("limit"))
					+ Integer.parseInt(httpServletRequest.getParameter("start")));
			vo.setStart(Integer.parseInt(httpServletRequest.getParameter("start")) + 1);
			this.logger.debug("max :: " + httpServletRequest.getParameter("limit") + " min :: "
					+ httpServletRequest.getParameter("start"));
			UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			vo.setCaseMgrId(userDetailsBean.getLoginUserId());
			List<UnconfimredBudgetVO> unConfirmedBudgetList = this.unconfirmBudgetManager.getBudgetRecords(vo);
			int totalCount = 0;
			if (unConfirmedBudgetList != null && unConfirmedBudgetList.size() > 0) {
				totalCount = this.unconfirmBudgetManager.getBudgetRecordsCount(userDetailsBean.getLoginUserId());
			}

			this.logger.debug("unConfirmedBudgetList size :: " + unConfirmedBudgetList.size() + " :: totalCount :: "
					+ totalCount);
			viewForJSON.addObject("unConfirmedBudgetList", unConfirmedBudgetList);
			viewForJSON.addObject("total", totalCount);
			this.logger.debug("After getting values exit from method getBudgetRecords");
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getCBDDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("inside getCBDDetails");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn is :: " + crn);
			UnconfimredBudgetVO vo = this.unconfirmBudgetManager.getBudgetDetails(crn);
			viewForJSON.addObject("crn", crn);
			viewForJSON.addObject("UnconfirmVO", vo);
			return viewForJSON;
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView saveISISConfirmationDetails(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("inside saveISISConfirmationDetails");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			String supportingTeam1DueDate = request.getParameter("supportingTeam1DueDate");
			String finalRDate = request.getParameter("finalRDate");
			String finalDate = request.getParameter("finalDate");
			String caseMgrId = request.getParameter("caseManager");
			String caseFee = request.getParameter("caseFee");
			String isConfirmed = request.getParameter("isConfirmed");
			String teamId = request.getParameter("teamId");
			String isSubreportRequire = request.getParameter("isSubreportrequire");
			String taskName = request.getParameter("taskName");
			String caseStatus = request.getParameter("caseStatus");
			String currencyCode = request.getParameter("currencyCode");
			this.logger.debug("SUbreport require" + isSubreportRequire);
			this.logger.debug("crn :: " + crn + ", supportingTeam1DueDate :: " + supportingTeam1DueDate
					+ ", finalRDate :: " + finalRDate + ", caseManager ::" + caseMgrId + ", caseFee :: " + caseFee
					+ ", isConfirmed :: " + isConfirmed + ", teamId ::" + teamId + "Case status" + caseStatus
					+ ",Currency" + currencyCode);
			UnconfimredBudgetVO unconfimredBudgetVO = new UnconfimredBudgetVO();
			if (isSubreportRequire.equalsIgnoreCase("true")) {
				String gridData = request.getParameter("subjecrgrid");
				unconfimredBudgetVO.setGridData(gridData);
			}

			unconfimredBudgetVO.setCRN(crn);
			unconfimredBudgetVO.setSupportingTeam1DueDate(supportingTeam1DueDate);
			unconfimredBudgetVO.setFinalRDate(finalRDate);
			unconfimredBudgetVO.setFinalDate(finalDate);
			unconfimredBudgetVO.setCaseMgrId(caseMgrId);
			unconfimredBudgetVO.setCaseFee(caseFee);
			unconfimredBudgetVO.setIsConfirmed(isConfirmed);
			unconfimredBudgetVO.setTeamId(teamId);
			unconfimredBudgetVO.setIsSubreportRequire(isSubreportRequire);
			unconfimredBudgetVO.setTaskName(taskName);
			unconfimredBudgetVO.setCaseStatus(caseStatus);
			unconfimredBudgetVO.setCurrency(currencyCode);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			unconfimredBudgetVO.setUpdatedBy(userBean.getUserName());
			this.logger.debug("UpdatedBy :: " + userBean.getUserName());
			if (request.getSession().getAttribute("loginLevel") != null) {
				unconfimredBudgetVO.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				unconfimredBudgetVO.setCaseHistoryPerformer(userBean.getUserName());
			}

			this.logger.debug("CaseHistoryPerformer :: " + unconfimredBudgetVO.getCaseHistoryPerformer());
			int updateCount = this.unconfirmBudgetManager.saveISISDetails(unconfimredBudgetVO,
					SBMUtils.getSession(request));
			this.logger.debug("updateCount value is ::" + updateCount);
			if (Integer.parseInt(unconfimredBudgetVO.getIsConfirmed()) == 0) {
				ResourceLocator.self().getCacheService().updateBudgetCountCache();
			}

			viewForJSON.addObject("updateCount", updateCount);
			viewForJSON.addObject("crn", unconfimredBudgetVO.getCRN());
			if (updateCount == -2) {
				this.logger.debug("Information provided was not correct.");
			} else if (updateCount == -1) {
				this.logger.debug("Communication error with ISIS.");
			} else if (updateCount == 0) {
				this.logger.debug(
						"Case Cancellation failed because additional actions for this case is still pending at ISIS end.");
			} else {
				this.logger.debug("isis budget details saved Successfully..");
				viewForJSON.addObject("success", true);
			}

			return viewForJSON;
		} catch (CMSException var19) {
			return AtlasUtils.getExceptionView(this.logger, var19);
		} catch (Exception var20) {
			return AtlasUtils.getExceptionView(this.logger, var20);
		}
	}
}