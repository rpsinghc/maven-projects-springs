package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.VendorDataSummaryVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONVendorDataSummaryController extends JSONController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONVendorDataSummaryController");
	private static final String VENDOR_DETAILS_LIST = "vendorDetailsList";
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorDataSummaryController");
		ModelAndView modelAndView = super.handleRequest(request, response);
		if (null != modelAndView) {
			return modelAndView;
		} else {
			try {
				request.setAttribute("reportName", "vendordatasummary");
				List<VendorDataSummaryVO> vendorDetailsList = this.atlasReportService.getReport(request, response);
				int totalCount = this.atlasReportService.getTotalCount(request, response);
				this.logger.debug("vendorDetailsList size :: " + vendorDetailsList.size());
				modelAndView = new ModelAndView("jsonView");
				modelAndView.addObject("vendorDetailsList", vendorDetailsList);
				modelAndView.addObject("total", totalCount);
				return modelAndView;
			} catch (CMSException var6) {
				return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
			} catch (Exception var7) {
				return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
			}
		}
	}
}