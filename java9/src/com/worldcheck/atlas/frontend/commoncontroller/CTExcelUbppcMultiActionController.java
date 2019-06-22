package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.report.CTExcelController;
import com.worldcheck.atlas.frontend.report.ProductivityPointsAndCasesController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CTExcelUbppcMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.CTExcelUbppcMultiActionController");
	CTExcelController ctExcelController = null;
	ProductivityPointsAndCasesController productivityPointsAndCasesController = null;

	public CTExcelController getCtExcelController() {
		return this.ctExcelController;
	}

	public void setCtExcelController(CTExcelController ctExcelController) {
		this.ctExcelController = ctExcelController;
	}

	public ProductivityPointsAndCasesController getProductivityPointsAndCasesController() {
		return this.productivityPointsAndCasesController;
	}

	public void setProductivityPointsAndCasesController(
			ProductivityPointsAndCasesController productivityPointsAndCasesController) {
		this.productivityPointsAndCasesController = productivityPointsAndCasesController;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("inside handle request method of CTExcelUbppcMultiActionController. The request param :: "
				+ request.getParameterMap());
		if (null != request && null != request.getSession()) {
			try {
				this.logger.debug("Out :: CTExcelUbppcMultiActionController :: handleRequest" + new Date());
				return this.redirectRequest(request, response);
			} catch (Exception var4) {
				new CMSException(this.logger, var4);
				return null;
			}
		} else {
			this.logger.debug("request.getSession() is " + request.getSession());
			NullPointerException nullPoiinter = new NullPointerException();
			return AtlasUtils.getSessionExceptionView(this.logger, nullPoiinter, response);
		}
	}

	public synchronized ModelAndView redirectRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN CTExcelUbppcMultiActionController:: redirectRequest :: ");
		if (request.getRequestURI().contains("generateExcelOfIncoiceData")) {
			return this.ctExcelController.generateExcelOfIncoiceData(request, response);
		} else if (request.getRequestURI().contains("writeToMultipleCTExcels")) {
			return this.ctExcelController.writeToMultipleCTExcels(request, response);
		} else {
			return request.getRequestURI().contains("generateExcelZipForUBPPC")
					? this.productivityPointsAndCasesController.generateExcelZipForUBPPC(request, response)
					: this.productivityPointsAndCasesController.generateExcelForUBPPC(request, response);
		}
	}
}