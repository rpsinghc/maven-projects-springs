package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.report.OnHoldCasesVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class OnHoldCasesController extends MultiActionController {
	private AtlasReportService atlasReportService;
	private String onHoldCases = "misallOnHoldCases";
	private PropertyReaderUtil propertyReader;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView OnHoldCases(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.onHoldCases);
		return modelAndView;
	}

	public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<OnHoldCasesVO> onHoldCasesVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("");
		modelAndView.addObject("onHoldCasesVO", onHoldCasesVO);
		return modelAndView;
	}
}