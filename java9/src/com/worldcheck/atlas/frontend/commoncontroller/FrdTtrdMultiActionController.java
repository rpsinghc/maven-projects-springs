package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.report.FinanceRawDataController;
import com.worldcheck.atlas.frontend.report.TimeTrackerRawDataController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FrdTtrdMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.FrdTtrdMultiActionController");
	FinanceRawDataController financeRawDataController = null;
	TimeTrackerRawDataController timeTrackerRawDataController = null;

	public FinanceRawDataController getFinanceRawDataController() {
		return this.financeRawDataController;
	}

	public void setFinanceRawDataController(FinanceRawDataController financeRawDataController) {
		this.financeRawDataController = financeRawDataController;
	}

	public TimeTrackerRawDataController getTimeTrackerRawDataController() {
		return this.timeTrackerRawDataController;
	}

	public void setTimeTrackerRawDataController(TimeTrackerRawDataController timeTrackerRawDataController) {
		this.timeTrackerRawDataController = timeTrackerRawDataController;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In :: FrdTtrdMultiActionController :: handleRequest" + new Date());
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
		if (request.getRequestURI().contains("exportToExcelFinanceRawData")) {
			return this.financeRawDataController.exportToExcelFinanceRawData(request, response);
		} else if (request.getRequestURI().contains("writeToMultipleExcels")) {
			return this.financeRawDataController.writeToMultipleExcels(request, response);
		} else {
			return request.getRequestURI().contains("generateExcelZipForTTRD")
					? this.timeTrackerRawDataController.generateExcelZipForTTRD(request, response)
					: this.timeTrackerRawDataController.generateExcelForTTRD(request, response);
		}
	}
}