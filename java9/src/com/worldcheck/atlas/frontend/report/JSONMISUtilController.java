package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONMISUtilController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONMISUtilController");

	public ModelAndView checkExcelGeneration(HttpServletRequest request, HttpServletResponse response) {
		int reportGenerationGap = 300000;
		ModelAndView modelAndView = new ModelAndView("jsonView");
		Date lastGenerationDate = (Date) request.getSession().getAttribute("excelGeneration");
		this.logger.debug("excel generation status:" + lastGenerationDate);
		this.logger.debug("rg gap:" + reportGenerationGap);
		if (lastGenerationDate != null) {
			if ((new Date()).getTime() - lastGenerationDate.getTime() > (long) reportGenerationGap) {
				this.logger.debug("server seems to be hang in creating other excel, time difference : "
						+ ((new Date()).getTime() - lastGenerationDate.getTime()));
				request.getSession().removeAttribute("excelGeneration");
				modelAndView.addObject("anotherInProgress", "false");
			} else {
				modelAndView.addObject("anotherInProgress", "true");
			}
		}

		return modelAndView;
	}
}