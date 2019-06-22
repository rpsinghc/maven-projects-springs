package com.worldcheck.atlas.frontend.casedetails;

import com.worldcheck.atlas.bl.interfaces.ICaseDetailManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCaseDetailsController extends JSONMultiActionController {
	private static final String CASE_STATUS_MASTER = "caseStatusMaster";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.casedetails.JSONCaseDetailsController");
	private ICaseDetailManager caseDetailManager = null;
	private static final String CASE_DETAILS = "caseDetails";

	public void setCaseDetailManager(ICaseDetailManager caseDetailManager) {
		this.caseDetailManager = caseDetailManager;
	}

	public ModelAndView getCaseStatus(HttpServletRequest request, HttpServletResponse response, Object command) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		String crn = "";
		new ArrayList();

		List caseStatusMaster;
		try {
			caseStatusMaster = this.caseDetailManager.getCaseStatusMaster();
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		viewForJson.addObject("caseStatusMaster", caseStatusMaster);
		return viewForJson;
	}

	public ModelAndView getAllCountryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryDatabaseMasterVO> allCountryMasterList = this.caseDetailManager.getAllCountryMasterList();
			modelAndView.addObject("countryMasterList", allCountryMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView caseDetailsOnSubjectSearch(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("Start caseDetailsOnSubjectSearch");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn");
			new CaseDetails();
			int isLegacy = false;
			if (null != request.getParameter("isLegacy") && request.getParameter("isLegacy").equalsIgnoreCase("1")) {
				isLegacy = true;
			}

			this.logger.debug("crn :: " + crn + " :: isLegacy :: " + request.getParameter("isLegacy"));
			CaseDetails caseDetails;
			if (!isLegacy) {
				caseDetails = this.caseDetailManager.getCaseDetails(crn);
			} else {
				caseDetails = this.caseDetailManager.getLegacyCaseDetails(crn);
			}

			modelAndView.addObject("crn", crn);
			modelAndView.addObject("caseDetails", caseDetails);
			this.logger.debug("Exit Case Details On Subject Search");
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getRecurranceCaseDetails(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In getRecurranceCaseDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new CaseDetails();

		try {
			if (null != request.getParameter("crn")) {
				String crn = request.getParameter("crn");
				CaseDetails caseDetails = this.caseDetailManager.getRecurranceCaseDetails(crn);
				modelAndView.addObject("recCaseDetails", caseDetails);
			}

			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView updateRecurranceCaseDetails(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In updateRecurranceCaseDetails");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		CaseDetails caseDetails = new CaseDetails();

		try {
			String recurrNumber = request.getParameter("recurrNumber");
			String crn = request.getParameter("crn");
			String caseManagerName = request.getParameter("caseManager");
			String officeName = request.getParameter("officeName");
			String expressCase = request.getParameter("expressCase");
			String cInterim1Days = request.getParameter("cInterim1Days");
			String cInterim2Days = request.getParameter("cInterim2Days");
			String finalDueDays = request.getParameter("finalDueDays");
			String caseFee = request.getParameter("caseFee");
			String currency = request.getParameter("currency");
			caseDetails.setRecurrNumber(recurrNumber);
			caseDetails.setCrn(crn);
			caseDetails.setCaseManagerName(caseManagerName);
			caseDetails.setOfficeName(officeName);
			caseDetails.setExpressCase(expressCase.equalsIgnoreCase("true") ? 1 : 0);
			caseDetails.setcInterim1Days(cInterim1Days);
			caseDetails.setcInterim2Days(cInterim2Days);
			caseDetails.setFinalDueDays(finalDueDays);
			caseDetails.setCaseFee(caseFee);
			caseDetails.setCaseCurrency(currency);
			this.caseDetailManager.updateRecurranceCaseDetails(caseDetails);
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (CMSException var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		} catch (Exception var16) {
			return AtlasUtils.getJsonExceptionView(this.logger, var16, response);
		}
	}
}