package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.report.TeamPerformanceReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.TeamPerformance;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONTeamPerformanceController extends JSONMultiActionController {
	private AtlasReportService atlasReportService;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONTeamPerformanceController");
	private static final String USER_LIST = "userList";
	private static final String USER_TEMPLATE_LIST = "userTemplateList";
	private static final String TEMPLATE_NAME = "templateName";
	private static final String COUNT = "count";
	private static final String TEMPLATE_ID = "templateId";
	private static final String TEMPLATE_TYPE = "templateType";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String TEAM_PERFORMANCE_DATA_LIST = "teamPerformanceDataList";
	private static final String TEMPLATE_TYPE_USER = "User";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView getTeamPerformanceUsers(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController ::getTeamPerformanceUsers");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			List<TeamPerformance> userList = teamPerformanceRpt.fetchTeamPerformanceUsers(userId);
			this.logger.debug("user List list size :: " + userList.size());
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("userList", userList);
			this.logger.debug("Exiting JSONTeamPerformanceController ::getTeamPerformanceUsers");
		} catch (CMSException var8) {
			AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		}

		return modelAndView;
	}

	public ModelAndView getUserTemplates(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController::getUserTemplates");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			List<TeamPerformance> userTemplateList = teamPerformanceRpt.getUserTemplates(userId);
			this.logger.debug("userTemplateList size :: " + userTemplateList.size());
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("userTemplateList", userTemplateList);
		} catch (CMSException var8) {
			AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		}

		return modelAndView;
	}

	public ModelAndView checkUserTemplateExists(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController::checkUserTemplateExists");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			String templateName = "";
			if (request.getParameter("templateName") != null) {
				templateName = request.getParameter("templateName");
			}

			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			int count = teamPerformanceRpt.checkUserTemplateExists(userId, templateName);
			this.logger.debug("checkUserTemplateExists count :: " + count);
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("count", count);
		} catch (CMSException var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			AtlasUtils.getExceptionView(this.logger, var10);
		}

		this.logger.debug("exiting JSONTeamPerformanceController::checkUserTemplateExists");
		return modelAndView;
	}

	public ModelAndView deleteUserTemplates(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController::deleteUserTemplates");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String templateId = "";
			templateId = request.getParameter("templateId");
			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			int deleteResultCount = teamPerformanceRpt.deleteUserTemplates(templateId);
			this.logger.debug("deleteResultCount :: " + deleteResultCount);
			modelAndView = new ModelAndView("jsonView");
		} catch (CMSException var7) {
			AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			AtlasUtils.getExceptionView(this.logger, var8);
		}

		this.logger.debug("Exiting JSONTeamPerformanceController::deleteUserTemplates");
		return modelAndView;
	}

	public ModelAndView fetchReport(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController::fetchReport");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String templateId = "";
			String templateType = "";
			String year = "";
			String month = "";
			if (request.getParameter("templateId") != null) {
				templateId = request.getParameter("templateId");
			}

			if (request.getParameter("templateType") != null) {
				templateType = request.getParameter("templateType");
			}

			if (request.getParameter("year") != null) {
				year = request.getParameter("year");
			}

			if (request.getParameter("month") != null) {
				month = request.getParameter("month");
			}

			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			List<ReportTypeMasterVO> reportTypeList = teamPerformanceRpt.getAllReportTypes();
			int resultSetSize = false;
			int resultSetSize;
			if (templateType.equalsIgnoreCase("User")) {
				resultSetSize = teamPerformanceRpt.getUserTemplateResultCount(templateId, year, month);
			} else {
				resultSetSize = teamPerformanceRpt.getOfficeTemplateResultCount(templateId, year, month);
			}

			List<LinkedHashMap> teamPerformanceDataList = teamPerformanceRpt.fetchReport(templateId, templateType, year,
					month, reportTypeList, Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")));
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("total", resultSetSize);
			modelAndView.addObject("teamPerformanceDataList", teamPerformanceDataList);
		} catch (CMSException var12) {
			AtlasUtils.getExceptionView(this.logger, var12);
		} catch (Exception var13) {
			AtlasUtils.getExceptionView(this.logger, var13);
		}

		this.logger.debug("Exiting JSONTeamPerformanceController::fetchReport");
		return modelAndView;
	}

	public ModelAndView fetchReportType(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController::fetchReportType");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			List<ReportTypeMasterVO> reportTypeList = teamPerformanceRpt.getAllReportTypes();
			modelAndView.addObject("reportTypeList", reportTypeList);
		} catch (CMSException var6) {
			AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			AtlasUtils.getExceptionView(this.logger, var7);
		}

		this.logger.debug("Exiting JSONTeamPerformanceController::fetchReportType");
		return modelAndView;
	}
}