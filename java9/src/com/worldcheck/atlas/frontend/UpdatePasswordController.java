package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bizmanage.pagegenerator.ServletTools;
import com.savvion.sbm.bpmportal.util.CryptUtils;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UpdatePasswordController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.UpdatePasswordController");
	private String REDIRECT_PAGE = "change_password_confirmation";
	private String NEW_PASSWORD = "newPassword";
	private String IS_PASSWORD_ENCRYPTED = "isPasswordEncrypted";
	private String CONF_STATE = "confState";
	private String USER = "user";

	public ModelAndView updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("Entring UpdatePasswordController :updatePassword ");
		ModelAndView mv = new ModelAndView(this.REDIRECT_PAGE);
		String userName = ServletTools.getUTFString(request, "userName");
		String newPassword = ServletTools.getUTFString(request, this.NEW_PASSWORD);
		String IsUpdateFromAdmin = request.getParameter("IsUpdateFromAdmin");
		if (IsUpdateFromAdmin.equalsIgnoreCase("true")) {
			newPassword = ServletTools.getUTFString(request, "confNewPassword");
			newPassword = CryptUtils.decrypt(userName, newPassword, "-");
		}

		this.logger.info(" current user " + request.getParameter("userName"));
		boolean isPasswordUpdated = false;

		try {
			isPasswordUpdated = ResourceLocator.self().getSBMService().isPaswordUpdated(userName, newPassword);
		} catch (CMSException var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		} catch (Exception var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}

		String confState;
		if (isPasswordUpdated) {
			this.logger.info("password updated successfully");
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			ResourceLocator.self().getUserService().updatePasswordExpiryDate(userName);
			confState = "4";
		} else {
			this.logger.info("password updation failed");
			confState = "2";
		}

		mv.addObject(this.CONF_STATE, confState);
		mv.addObject(this.USER, userName);
		mv.addObject("userName", userName);
		return mv;
	}
}