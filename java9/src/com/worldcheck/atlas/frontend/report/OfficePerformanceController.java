package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class OfficePerformanceController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.OfficePerformanceController");
	private static final String YEAR_LIST = "yearList";
	private static final String misOfficePerformance = "misOfficePerformance";
	private PropertyReaderUtil propertyReader;
	private AtlasReportService atlasReportService;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView officePerformance(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("In OfficePerformanceController : officePerformance");

		try {
			ModelAndView modelAndView = new ModelAndView("misOfficePerformance");
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			modelAndView.addObject("yearList", yearList);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}
}