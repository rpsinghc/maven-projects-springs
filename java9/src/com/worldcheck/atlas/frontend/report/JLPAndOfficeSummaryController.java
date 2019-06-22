package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.vo.report.JLPAndOfficeSummaryVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JLPAndOfficeSummaryController extends MultiActionController {
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView JLPAndOfficeSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<JLPAndOfficeSummaryVO> currentAnalystLoadingVO = (ArrayList) this.atlasReportService
				.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("");
		modelAndView.addObject("jlpAndOfficeSummaryVO", currentAnalystLoadingVO);
		return modelAndView;
	}

	public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<JLPAndOfficeSummaryVO> currentAnalystLoadingVO = (ArrayList) this.atlasReportService
				.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("");
		modelAndView.addObject("jlpAndOfficeSummaryVO", currentAnalystLoadingVO);
		return modelAndView;
	}
}