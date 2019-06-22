package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.report.ReviewerRawDataController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ReviewerMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.ReviewerMultiActionController");
	ReviewerRawDataController reviewerRawDataController = null;

	public void setReviewerRawDataController(ReviewerRawDataController reviewerRawDataController) {
		this.reviewerRawDataController = reviewerRawDataController;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In :: ReviewerMultiActionController :: handleRequest" + new Date());
		if (null != request && null != request.getSession()) {
			try {
				return this.redirectRequest(request, response);
			} catch (Exception var4) {
				new CMSException(this.logger, var4);
				return null;
			}
		} else {
			this.logger.debug("request is " + request);
			NullPointerException nullPoiinter = new NullPointerException();
			return AtlasUtils.getSessionExceptionView(this.logger, nullPoiinter, response);
		}
	}

	public synchronized ModelAndView redirectRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return request.getRequestURI().contains("exportToExcelReviewerRawData")
				? this.reviewerRawDataController.exportToExcelReviewerRawData(request, response)
				: this.reviewerRawDataController.writeToMultipleExcels(request, response);
	}
}