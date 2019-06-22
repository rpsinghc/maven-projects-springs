package com.worldcheck.atlas.frontend.masters;

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
import org.springframework.web.servlet.mvc.AbstractController;

public class OfficePerformanceController extends AbstractController {
	private static final String JSP_NAME = "officePerformance_custom";
	private static final String CURRENT_YEAR = "currentYear";
	private static final String YEAR_LIST = "yearList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.OfficePerformanceController");
	private PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		ModelAndView modelAndView = null;
		this.logger.debug("in OfficePerformanceController");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader);
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			int currentYear = Calendar.getInstance().get(1);
			this.logger.debug("yearList size :: " + yearList.size());
			modelAndView = new ModelAndView("officePerformance_custom");
			modelAndView.addObject("yearList", yearList);
			modelAndView.addObject("currentYear", currentYear);
			this.logger.debug("exiting OfficePerformanceController");
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}
}