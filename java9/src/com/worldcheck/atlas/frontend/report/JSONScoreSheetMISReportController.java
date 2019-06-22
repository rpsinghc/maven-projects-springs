package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONScoreSheetMISReportController extends JSONMultiActionController {
	private static final String SCORE_SHEET_NAME_LIST = "scoreSheetNameList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONScoreSheetMISReportController");
	private AtlasReportService atlasReportService;
	private TabularReportDAO tabularReportDAO;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public ModelAndView analystScoringPerformanceScoreSheetMIS(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			request.setAttribute("reportName", "analystScoringPerformanceScoreSheetMIS");
			this.logger.debug("Inside the JSONScoreSheetMISReportController analystScoringPerformanceScoreSheetMIS");
			this.logger.debug("office :" + scoreSheetMasterVO.getOffice() + "scoreSheetName:"
					+ scoreSheetMasterVO.getScoreSheetName() + "month:" + scoreSheetMasterVO.getMonth() + "year:"
					+ scoreSheetMasterVO.getYear());
			List<ScoreSheetMasterVO> scoreSheetNameList = this.atlasReportService.getReport(request, response);
			mv.addObject("scoreSheetPerformance", scoreSheetNameList);
			int count = this.tabularReportDAO.getAnalystScoringMISGridCount(scoreSheetMasterVO);
			mv.addObject("total", count);
			return mv;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView reviewEffortScoreSheetMIS(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			this.logger.debug("Inside the JSONScoreSheetMISReportController reviewEffortScoreSheetMIS");
			this.logger.debug("office ::" + scoreSheetMasterVO.getOffice() + "scoreSheetName::"
					+ scoreSheetMasterVO.getScoreSheetName() + "month::" + scoreSheetMasterVO.getMonth() + "year::"
					+ scoreSheetMasterVO.getYear() + "SortColumnName:" + scoreSheetMasterVO.getSortColumnName()
					+ "SortType:" + scoreSheetMasterVO.getSortType() + "Start:" + scoreSheetMasterVO.getStart()
					+ "Limit:" + scoreSheetMasterVO.getLimit());
			List scoreSheetNameList = this.tabularReportDAO.reviewEffortScoreSheetMIS(scoreSheetMasterVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			mv.addObject("reviewEfforts", scoreSheetNameList);
			int count = this.tabularReportDAO.getReviewEffortScoringMISGridCount(scoreSheetMasterVO);
			mv.addObject("total", count);
			return mv;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getScoreSheetNameList(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		ModelAndView mv = new ModelAndView("jsonView");

		try {
			this.logger.debug("OfficeId:" + scoreSheetMasterVO.getOffice());
			List scoreSheetNameList = this.tabularReportDAO.getScoreSheetNameList(scoreSheetMasterVO.getOffice());
			mv.addObject("scoreSheetNameList", scoreSheetNameList);
			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}
}