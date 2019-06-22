package com.worldcheck.atlas.frontend.audit;

import com.worldcheck.atlas.bl.interfaces.IRiskHistoryEvents;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONRiskHistoryController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.audit.JSONRiskHistoryController");
	private IRiskHistoryEvents riskHistoryEventManager;

	public void setRiskHistoryEventManager(IRiskHistoryEvents riskHistoryEventManager) {
		this.riskHistoryEventManager = riskHistoryEventManager;
	}

	public ModelAndView getRiskHistoryList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN JSONRiskHistoryController::getRiskHistoryList");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String CRN = "";
		String riskCode = "";
		long subID = 0L;
		if (request.getParameter("crn") != null && !request.getParameter("crn").trim().isEmpty()) {
			CRN = request.getParameter("crn");
		}

		if (request.getParameter("riskCode") != null && !request.getParameter("riskCode").trim().isEmpty()) {
			riskCode = request.getParameter("riskCode");
		}

		if (request.getParameter("subID") != null && !request.getParameter("subID").trim().isEmpty()) {
			subID = Long.parseLong(request.getParameter("subID"));
		}

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List<String> roleList = userDetailsBean.getRoleList();
			this.logger.debug("Role for logged in user::" + roleList);
			List<RiskHistory> riskHistoryList = this.riskHistoryEventManager.getRiskHistory(CRN, riskCode, subID,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"), roleList);
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			this.logger.debug("riskHistoryList size in JSONRiskHistoryController " + riskHistoryList.size());
			this.logger.debug("riskHistoryList data in JSONRiskHistoryController " + riskHistoryList);
			modelAndView.addObject("total",
					this.riskHistoryEventManager.getRiskHistoryCountForRiskCode(CRN, riskCode, subID));
			modelAndView.addObject("riskHistoryList", riskHistoryList);
		} catch (CMSException var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		} catch (Exception var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}

		this.logger.debug("Exiting JSONRiskHistoryController::getReviewHistoryList");
		return modelAndView;
	}
}