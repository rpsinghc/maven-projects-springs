package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.report.CaseRawDataController;
import com.worldcheck.atlas.frontend.report.UtilizationByRevenueController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.CaseRawTableVO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CRDUBRMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.CRDUBRMultiActionController");
	CaseRawDataController caseRawDataController = null;
	UtilizationByRevenueController utilizationByRevenueController = null;

	public CaseRawDataController getCaseRawDataController() {
		return this.caseRawDataController;
	}

	public void setCaseRawDataController(CaseRawDataController caseRawDataController) {
		this.caseRawDataController = caseRawDataController;
	}

	public UtilizationByRevenueController getUtilizationByRevenueController() {
		return this.utilizationByRevenueController;
	}

	public void setUtilizationByRevenueController(UtilizationByRevenueController utilizationByRevenueController) {
		this.utilizationByRevenueController = utilizationByRevenueController;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN CRDUBRMultiActionController:: handleRequest :: " + new Date());
		this.logger.debug("inside handle request method of CRDUBRMultiActionController. The request param :: "
				+ request.getParameterMap());
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

	public synchronized ModelAndView redirectRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("IN CRDUBRMultiActionController:: redirectRequest :: ");
		CaseRawTableVO caseRawTableVO = new CaseRawTableVO();
		if (request.getRequestURI().contains("caseRawDataExportToExcel")) {
			return this.caseRawDataController.caseRawDataExportToExcel(request, response, caseRawTableVO);
		} else if (request.getRequestURI().contains("writeCRDToCSV")) {
			return this.caseRawDataController.writeCRDToCSV(request, response, caseRawTableVO);
		} else {
			return request.getRequestURI().contains("generateExcelZipForUBR")
					? this.utilizationByRevenueController.generateExcelZipForUBR(request, response)
					: this.utilizationByRevenueController.generateUBRExcel(request, response);
		}
	}
}