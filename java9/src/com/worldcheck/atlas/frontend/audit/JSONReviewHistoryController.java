package com.worldcheck.atlas.frontend.audit;

import com.worldcheck.atlas.bl.audit.ReviewHistoryManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONReviewHistoryController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.audit.JSONReviewHistoryController");
	private ReviewHistoryManager reviewHistoryManager;

	public void setReviewHistoryManager(ReviewHistoryManager reviewHistoryManager) {
		this.reviewHistoryManager = reviewHistoryManager;
	}

	public ModelAndView getReviewHistoryList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN JSONReviewHistoryController::getReviewHistoryList");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String CRN = "";
		if (request.getParameter("crn") != null && !request.getParameter("crn").isEmpty()) {
			CRN = request.getParameter("crn");
		}

		try {
			List<ReviewHistory> reviewHistoryList = this.reviewHistoryManager.getReviewHistory(CRN,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			modelAndView.addObject("total", this.reviewHistoryManager.getReviewHistoryCountForCRN(CRN));
			modelAndView.addObject("reviewHistoryList", reviewHistoryList);
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}

		this.logger.debug("Exiting JSONReviewHistoryController::getReviewHistoryList");
		return modelAndView;
	}
}