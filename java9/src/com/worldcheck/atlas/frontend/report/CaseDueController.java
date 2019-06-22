package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.report.CaseDueVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CaseDueController extends MultiActionController {
	private AtlasReportService atlasReportService;
	private String caseeDueToday = "misCaseDueToday";
	private PropertyReaderUtil propertyReader;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView caseDue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.caseeDueToday);
		return modelAndView;
	}

	public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CaseDueVO> caseDueVO = (ArrayList) this.atlasReportService.getReport(request, response);
		ModelAndView modelAndView = new ModelAndView("sucess");
		modelAndView.addObject("caseDueVO", caseDueVO);
		return modelAndView;
	}
}