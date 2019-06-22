package com.worldcheck.atlas.frontend.dashboard;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.JsonBeanUtil;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class JSONGetDashboardReportParams implements Controller {
	private PropertyReaderUtil propertyReader;
	private static final String YEAR_LIST = "yearList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.dashboard.JSONgetDashboardReportParams");

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONgetDashboardReportParams");
		ModelAndView modelandview = null;

		try {
			modelandview = new ModelAndView("jsonView");
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			modelandview.addObject("yearList", JsonBeanUtil.toJsonBean(yearList));
		} catch (CMSException var5) {
			AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			AtlasUtils.getExceptionView(this.logger, var6);
		}

		this.logger.debug("Exiting JSONgetDashboardReportParams");
		return modelandview;
	}
}