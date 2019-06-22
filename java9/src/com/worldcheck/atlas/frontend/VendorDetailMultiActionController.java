package com.worldcheck.atlas.frontend;

import com.worldcheck.atlas.bl.vendordetail.VendorDetailManager;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class VendorDetailMultiActionController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.vendorDetailMultiActionController");
	private VendorDetailManager vendorDetailManager;

	public void setVendorDetailManager(VendorDetailManager vendorDetailManager) {
		this.vendorDetailManager = vendorDetailManager;
	}

	public ModelAndView vendorDetailSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("com.worldcheck.atlas.frontend.vendorDetailMultiActionController");
			this.logger.debug("CRN  :" + crn);
			modelAndView = new ModelAndView("vendor_summary");
			modelAndView.addObject("crn", crn);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}
}