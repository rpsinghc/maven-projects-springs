package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CountryMultiActionController extends MultiActionController {
	public ModelAndView country(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) throws Exception {
		String message = "Country on load";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView addCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) throws Exception {
		String message = "Add Country ";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView saveCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) throws Exception {
		String message = "save Country";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView searchCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) throws Exception {
		String message = "Search Country ";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}
}