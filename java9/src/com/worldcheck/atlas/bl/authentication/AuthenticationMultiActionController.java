package com.worldcheck.atlas.bl.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class AuthenticationMultiActionController extends MultiActionController {
	public ModelAndView addUserSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	public ModelAndView removeUserSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
}