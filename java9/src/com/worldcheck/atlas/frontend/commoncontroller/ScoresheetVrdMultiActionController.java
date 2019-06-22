package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.report.ScoreSheetMISReportController;
import com.worldcheck.atlas.frontend.report.VendorRawDataController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ScoresheetVrdMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.ScoresheetVrdMultiActionController");
	ScoreSheetMISReportController scoreSheetMISReportController = null;
	VendorRawDataController vendorRawDataController = null;

	public ScoreSheetMISReportController getScoreSheetMISReportController() {
		return this.scoreSheetMISReportController;
	}

	public void setScoreSheetMISReportController(ScoreSheetMISReportController scoreSheetMISReportController) {
		this.scoreSheetMISReportController = scoreSheetMISReportController;
	}

	public VendorRawDataController getVendorRawDataController() {
		return this.vendorRawDataController;
	}

	public void setVendorRawDataController(VendorRawDataController vendorRawDataController) {
		this.vendorRawDataController = vendorRawDataController;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("iN ScoresheetVrdMultiActionController:: handleRequest :: " + new Date());
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
		ScoreSheetMasterVO scoreSheetMasterVO = new ScoreSheetMasterVO();
		if (request.getRequestURI().contains("downloadScoreSheetMISReport")) {
			return this.scoreSheetMISReportController.downloadScoreSheetMISReport(request, response,
					scoreSheetMasterVO);
		} else if (request.getRequestURI().contains("generateExcelZipForSSMIS")) {
			return this.scoreSheetMISReportController.generateExcelZipForSSMIS(request, response, scoreSheetMasterVO);
		} else {
			return request.getRequestURI().contains("writeToMultipleVRDExcels")
					? this.vendorRawDataController.writeToMultipleVRDExcels(request, response)
					: this.vendorRawDataController.generateVEORDExcel(request, response);
		}
	}
}