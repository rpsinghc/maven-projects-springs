package com.worldcheck.atlas.frontend.report;

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
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class InvoiceTATReportController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.InvoiceTATReportController");
	private static final String misITATR = "misITATR";
	private static final String YEAR_LIST = "yearList";
	private static final String MONTH_MAP = "monthList";
	private static final String CURRENT_MONTH = "currentMonth";
	private static final String CURRENT_YEAR = "currentYear";
	private PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView invoiceTATReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("In InvoiceTATReportController : invoiceTATReport");

		try {
			ModelAndView modelAndView = new ModelAndView("misITATR");
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			int currentMonth = cal.get(2) + 1;
			modelAndView.addObject("yearList", yearList);
			modelAndView.addObject("monthList", ReportConstants.fullmonthMap);
			modelAndView.addObject("currentMonth", currentMonth);
			modelAndView.addObject("currentYear", currentYear);
			return modelAndView;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}
}