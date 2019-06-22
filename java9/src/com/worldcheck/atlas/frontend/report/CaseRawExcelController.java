package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.vo.report.CaseRawExcelVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CaseRawExcelController extends MultiActionController {
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView CaseRawExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<CaseRawExcelVO> caseRawExcelVO = this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}

	public ModelAndView viewCaseRawExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CaseRawExcelVO> caseRawExcelVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}

	public ModelAndView deleteCaseRawExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CaseRawExcelVO> caseRawExcelVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}

	public ModelAndView addCaseRawExcelAndNew(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ArrayList<CaseRawExcelVO> caseRawExcelVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}

	public ModelAndView addCaseRawExcelAndBack(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ArrayList<CaseRawExcelVO> caseRawExcelVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}

	public ModelAndView revert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CaseRawExcelVO> caseRawExcelVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}

	public ModelAndView run(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CaseRawExcelVO> caseRawExcelVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("showmessage");
		modelAndView.addObject("caseRawExcelVO", caseRawExcelVO);
		return modelAndView;
	}
}