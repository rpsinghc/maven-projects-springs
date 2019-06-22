package com.worldcheck.atlas.frontend;

import com.worldcheck.atlas.vo.SubjectDetails;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class SubjectController extends MultiActionController {
	public ModelAndView SubjectList(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subjectDetails) throws Exception {
		String message = "Default Method of Subject-Details Action";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView addSubjectDetailAndNew(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subjectDetails) throws Exception {
		String message = "Add SubjectDetail and more SubjectDetail Added...";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView addSubjectDetailAndBack(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subjectDetails) throws Exception {
		String message = "Add SubjectDetail and Back to Subject List";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView deleteSubjectDetail(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subjectDetails) throws Exception {
		String message = "Add SubjectDetail and Back to Subject List";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView revert(HttpServletRequest request, HttpServletResponse response, SubjectDetails subjectDetails)
			throws Exception {
		String message = "Revert SubjectDetail ";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}

	public ModelAndView subjectConfiguration(HttpServletRequest request, HttpServletResponse response,
			SubjectDetails subjectDetails) throws Exception {
		String message = "update SubjectCongfiguration ";
		System.out.println(message);
		return new ModelAndView("showmessage", "message", message);
	}
}