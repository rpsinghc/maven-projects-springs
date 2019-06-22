package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class VendorDataSummaryController extends AbstractController {
	private static final String JSP_NAME = "misVEODSView";
	private static final String CURRENT_MONTH = "currentMonth";
	private static final String CURRENT_YEAR = "currentYear";
	private static final String MONTH_MAP = "monthMap";
	private static final String YEAR_LIST = "yearList";
	private static final String REPORT_TYPES = "reportTypes";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.VendorDataSummaryController");
	private PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in VendorDataSummaryController");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader);
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getVendorExpenditureReportTypes());
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			int currentYear = Calendar.getInstance().get(1);
			int currentMonth = Calendar.getInstance().get(2) + 1;
			this.logger.debug("reportTypelist size :: " + reportTypelist.size());
			this.logger.debug("yearList size :: " + yearList.size());
			modelAndView = new ModelAndView("misVEODSView");
			modelAndView.addObject("reportTypes", reportTypelist);
			modelAndView.addObject("yearList", yearList);
			modelAndView.addObject("monthMap", ReportConstants.monthMap);
			modelAndView.addObject("currentYear", currentYear);
			modelAndView.addObject("currentMonth", currentMonth);
			this.logger.debug("exiting VendorDataSummaryController");
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}
}