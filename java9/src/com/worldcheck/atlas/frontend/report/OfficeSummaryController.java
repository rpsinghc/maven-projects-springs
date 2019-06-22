package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class OfficeSummaryController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.OfficeSummaryController");
	private static PropertyReaderUtil propertyReader;
	private static final String JSP_NAME = "misOMOQS";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public ModelAndView viewOfficeSummary(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  OfficeSummaryController ");
		ModelAndView modelandview = null;

		try {
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(propertyReader.getOfficeUtilizationReportTypes());
			List<String> quarterList = StringUtils.commaSeparatedStringToList(propertyReader.getQuarterList());
			List<String> yearList = StringUtils.commaSeparatedStringToList(propertyReader.getYearList());
			this.logger.debug("reportTypelist size :: " + reportTypelist.size());
			this.logger.debug("quarterList size :: " + quarterList.size());
			this.logger.debug("yearList size :: " + yearList.size());
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			int currentMonth = cal.get(2) + 1;
			String currentQuarterName = this.getCurrentQuarter(currentMonth);
			modelandview = new ModelAndView("misOMOQS");
			modelandview.addObject("reportTypes", reportTypelist);
			modelandview.addObject("quarters", quarterList);
			modelandview.addObject("years", yearList);
			modelandview.addObject("currentYear", currentYear);
			modelandview.addObject("currentQuarterName", currentQuarterName);
			this.logger.debug("exiting  OfficeSummaryController ");
		} catch (CMSException var11) {
			AtlasUtils.getExceptionView(this.logger, var11);
		} catch (Exception var12) {
			AtlasUtils.getExceptionView(this.logger, var12);
		}

		return modelandview;
	}

	private String getCurrentQuarter(int currentMonth) throws CMSException {
		try {
			int quarterInt = false;
			float quarterFloat = (float) ((double) currentMonth / 3.0D);
			byte quarterInt;
			if (quarterFloat > 0.0F && quarterFloat <= 1.0F) {
				quarterInt = 1;
			} else if (quarterFloat > 1.0F && quarterFloat <= 2.0F) {
				quarterInt = 2;
			} else if (quarterFloat > 2.0F && quarterFloat <= 3.0F) {
				quarterInt = 3;
			} else {
				quarterInt = 4;
			}

			this.logger.debug("quarterInt::" + quarterInt);
			String quarterPrefix = "Quarter ";
			String currentQuarterName = quarterPrefix + quarterInt;
			return currentQuarterName;
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}