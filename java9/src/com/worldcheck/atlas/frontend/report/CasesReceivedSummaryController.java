package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class CasesReceivedSummaryController extends AbstractController {
	private static final String REPORT_TYPES = "reportTypes";
	private static final String JSP_NAME = "misNCRSView";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.CasesReceivedSummaryController");
	private PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in CasesReceivedSummaryController");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader);
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getOfficeUtilizationReportTypes());
			this.logger.debug("reportTypelist size :: " + reportTypelist.size());
			modelAndView = new ModelAndView("misNCRSView");
			modelAndView.addObject("reportTypes", reportTypelist);
			this.logger.debug("exiting CasesReceivedSummaryController");
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}
}