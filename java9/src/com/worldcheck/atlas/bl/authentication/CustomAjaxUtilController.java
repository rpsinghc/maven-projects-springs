package com.worldcheck.atlas.bl.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class CustomAjaxUtilController extends AbstractController {
	protected ModelAndView handleRequestInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponce)
			throws Exception {
		return null;
	}

	private boolean validatePassword(String[] passwords, String entredPassword) {
		return false;
	}
}