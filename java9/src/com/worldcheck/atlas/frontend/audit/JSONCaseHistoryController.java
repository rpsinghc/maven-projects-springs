package com.worldcheck.atlas.frontend.audit;

import com.worldcheck.atlas.bl.interfaces.ICaseHistoryEvents;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCaseHistoryController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.audit.JSONCaseHistoryController");
	private ICaseHistoryEvents caseHistoryEventManager;

	public void setCaseHistoryEventManager(ICaseHistoryEvents caseHistoryEventManager) {
		this.caseHistoryEventManager = caseHistoryEventManager;
	}

	public ModelAndView getCaseHistoryList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN JSONCaseHistoryController::getCaseHistoryList");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String CRN = "";
		if (request.getParameter("crn") != null && !request.getParameter("crn").isEmpty()) {
			CRN = request.getParameter("crn");
		}

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List<String> roleList = userDetailsBean.getRoleList();
			List<CaseHistory> caseHistoryList = this.caseHistoryEventManager.getCaseHistory(CRN,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"), roleList);
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			this.logger.debug("caseHistoryList size in JSONCaseHistoryController " + caseHistoryList.size());
			modelAndView.addObject("total", this.caseHistoryEventManager.getCaseHistoryCountForCRN(CRN, roleList));
			modelAndView.addObject("caseHistoryList", caseHistoryList);
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		this.logger.debug("Exiting JSONReviewHistoryController::getReviewHistoryList");
		return modelAndView;
	}
}