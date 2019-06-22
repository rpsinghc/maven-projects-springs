package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.MyPerformanceReportVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MyPerformanceMultiactionController extends MultiActionController {
	private String MYPERFORMANCE = "misMyperformance";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.MyPerformanceMultiactionController");
	private static final String YEAR_LIST = "yearList";
	private static final String CURRENT_YEAR = "currentYear";
	private PropertyReaderUtil propertyReader;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView myPerformance(HttpServletRequest request, HttpServletResponse response,
			MyPerformanceReportVO myPerformanceReportVO) throws CMSException {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.MYPERFORMANCE);
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			new ArrayList();
			int indOf2009 = yearList.indexOf("2009");
			int indOfCurrentYear = yearList.indexOf(String.valueOf(currentYear));
			if (indOf2009 >= 0 && indOfCurrentYear >= 0) {
				List<String> subYearList = yearList.subList(indOf2009, indOfCurrentYear + 1);
				modelAndView.addObject("yearList", subYearList);
			} else {
				modelAndView.addObject("yearList", yearList);
			}

			modelAndView.addObject("currentYear", currentYear);
			return modelAndView;
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}
}