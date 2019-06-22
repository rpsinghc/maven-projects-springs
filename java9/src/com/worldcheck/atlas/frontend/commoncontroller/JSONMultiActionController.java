package com.worldcheck.atlas.frontend.commoncontroller;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JSONMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController");

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("inside handle request method of JSONMultiActionController . The request param :: "
				+ request.getParameterMap());
		this.logger.debug("The method used by this JSON call is " + request.getMethod());
		if (null != request && null != request.getSession() && null != request.getSession().getAttribute("bizManage")) {
			try {
				return super.handleRequest(request, response);
			} catch (Exception var4) {
				new CMSException(this.logger, var4);
				return null;
			}
		} else {
			this.logger.debug("request is " + request);
			this.logger.debug("request.getSession() is " + request.getSession());
			this.logger.debug("SavvionConstants.strBizManage is " + request.getSession().getAttribute("bizManage"));
			this.logger.error("Session expired While accessing request " + request.getRequestURI());
			NullPointerException nullPoiinter = new NullPointerException();
			return AtlasUtils.getSessionExceptionView(this.logger, nullPoiinter, response);
		}
	}
}