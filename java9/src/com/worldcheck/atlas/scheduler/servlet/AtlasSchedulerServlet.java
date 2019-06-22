package com.worldcheck.atlas.scheduler.servlet;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.AtlasScheduler;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class AtlasSchedulerServlet extends HttpServlet {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.servlet.AtlasSchedulerServlet");
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig servletconfig) throws ServletException {
		this.logger.debug(" AtlasSchedulerServlet.init() here loaded ");

		try {
			new AtlasScheduler();
		} catch (Exception var3) {
			this.logger.error(var3);
		}

	}

	public void destroy() {
	}
}