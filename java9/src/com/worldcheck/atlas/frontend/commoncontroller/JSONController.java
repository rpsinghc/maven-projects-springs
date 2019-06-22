package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class JSONController implements Controller {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.JSONController");

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Inside handleRequest");
		if (null != request && null != request.getSession() && null != request.getSession().getAttribute("bizManage")) {
			return null;
		} else {
			this.logger.error("Session expired . While accessing request " + request.getRequestURI());
			NullPointerException nullPoiinter = new NullPointerException();
			return AtlasUtils.getSessionExceptionView(this.logger, nullPoiinter, response);
		}
	}
}