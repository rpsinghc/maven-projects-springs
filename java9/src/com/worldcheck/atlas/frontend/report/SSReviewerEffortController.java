package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.vo.report.SSReviewerEffortVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class SSReviewerEffortController extends MultiActionController {
	private AtlasReportService atlasReportService;

	public ModelAndView revert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<SSReviewerEffortVO> sSReviewerEffortVO = (ArrayList) this.atlasReportService.getReport(request,
				response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", sSReviewerEffortVO);
		return modelAndView;
	}

	public ModelAndView run(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<SSReviewerEffortVO> sSReviewerEffortVO = (ArrayList) this.atlasReportService.getReport(request,
				response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", sSReviewerEffortVO);
		return modelAndView;
	}
}