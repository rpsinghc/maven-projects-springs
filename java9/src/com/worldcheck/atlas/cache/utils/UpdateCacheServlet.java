package com.worldcheck.atlas.cache.utils;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateCacheServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.cache.utils.UpdateCacheServlet");

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			this.doPost(request, response);
		} catch (IOException var4) {
			this.logger.error("Error in goGet " + var4);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");
		String action = request.getParameter("action");
		this.logger.debug("Servlet invoked with action " + action + " :: " + token);
		if (null != token && !"".equalsIgnoreCase(token)) {
			try {
				if (null != action) {
					if (!"updateToken".equalsIgnoreCase(action) && !"addToken".equalsIgnoreCase(action)) {
						if (!action.equals("updateTTToken") && !action.equals("deleteTTToken")) {
							if ("updateBudgetCount".equalsIgnoreCase(action)) {
								ResourceLocator.self().getCacheService().updateBudgetCount();
							} else {
								this.logger.debug("refreshCache called from UpdateCacheServlet");
								ResourceLocator.self().getCacheService().refreshCache(token);
							}
						} else if (action.equals("updateTTToken")) {
							ResourceLocator.self().getCacheService().updateTimeTrackerTokenCache(
									request.getParameter("userId"), request.getParameter("token"));
						} else {
							ResourceLocator.self().getCacheService()
									.removeTimeTrackerTokenCache(request.getParameter("userId"));
						}
					} else if (!"remove".equalsIgnoreCase(request.getParameter("token"))) {
						ResourceLocator.self().getCacheService().updateTokenForACL(request.getParameter("userId"),
								request.getParameter("token"));
					} else {
						ResourceLocator.self().getCacheService().removeTokenForACL(request.getParameter("userId"));
					}
				}
			} catch (CMSException var6) {
				this.logger.error("Error occured in servlet " + var6);
			}
		}

	}
}