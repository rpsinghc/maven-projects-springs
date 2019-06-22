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

public class TimeTrackerDataSummaryController extends AbstractController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.TimeTrackerDataSummaryController");
	private PropertyReaderUtil propertyReader;
	private String REPORT_TYPE_LIST = "reportTypes";
	private String JSP = "misTTDataSummary";
	private String YEAR_LIST = "yearList";
	private String CURRENT_YEAR = "currentYear";
	private String CURRENT_MONTH = "currentMonth";
	private String MONTH_MAP = "monthMap";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) {
		ModelAndView modelAndView = null;
		this.logger.debug("in TimeTrackerDataSummaryController");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader.getTimeTrackerReportTypes() + " year list :: "
					+ this.propertyReader.getYearList());
			List<String> reportTypeList = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getTimeTrackerReportTypes());
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			modelAndView = new ModelAndView(this.JSP);
			modelAndView.addObject(this.REPORT_TYPE_LIST, reportTypeList);
			modelAndView.addObject(this.MONTH_MAP, ReportConstants.monthMap);
			modelAndView.addObject(this.CURRENT_MONTH, Calendar.getInstance().get(2) + 1);
			modelAndView.addObject(this.CURRENT_YEAR, Calendar.getInstance().get(1));
			modelAndView.addObject(this.YEAR_LIST, yearList);
			this.logger.debug("exiting TimeTrackerDataSummaryController");
			return modelAndView;
		} catch (ArrayIndexOutOfBoundsException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}
}