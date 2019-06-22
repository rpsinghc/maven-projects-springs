package com.worldcheck.atlas.frontend.report;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ITeamJLPSummary;
import com.worldcheck.atlas.bl.report.TeamJLPSummaryManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.TeamJLPSummaryVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONTeamJLPSummaryController extends JSONMultiActionController {
	private static final String activeUserList = "activeUserList";
	private static final String allTemplateCreatorList = "allTemplateCreatorList";
	private static final String BLANK = "";
	private static final String TEMPLATEIDS = "templateIds";
	private static final String ALL = "ALL";
	private static final String SEARCHLIST = "searchList";
	private static final String TEMPLATEID = "templateId";
	private static final String SELECTEDUSERLIST = "selectedUserList";
	private static final String ISEXIST = "isExist";
	private static final String TEMPLATENAME = "templateName";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String MISREPORTTYPE = "misReportType";
	private static final String SORTINGCOLUMN = "sortingColumn";
	private static final String SORTTYPE = "sortType";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONTeamJLPSummaryController");
	private AtlasReportService atlasReportService;
	private ITeamJLPSummary teamJLPSummaryManager;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setTeamJLPSummaryManager(ITeamJLPSummary teamJLPSummaryManager) {
		this.teamJLPSummaryManager = teamJLPSummaryManager;
	}

	public ModelAndView addTeamJLPTemplate(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : addTeamJLPTemplate");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		teamJLPSummaryVO.setUpdatedBy(userBean.getUserName());
		if (request.getParameter("templateIds") != null && !request.getParameter("templateIds").equalsIgnoreCase("")) {
			this.logger.debug("Update:" + request.getParameter("templateIds"));
			teamJLPSummaryVO.setTemplateId(Integer.parseInt(request.getParameter("templateIds")));
			this.teamJLPSummaryManager.updateTeamJLPTemplate(teamJLPSummaryVO);
		} else {
			this.teamJLPSummaryManager.addTeamJLPTemplate(teamJLPSummaryVO);
		}

		return modelAndView;
	}

	public ModelAndView updateTeamJLPTemplate(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : updateTeamJLPTemplate");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		this.teamJLPSummaryManager.updateTeamJLPTemplate(teamJLPSummaryVO);
		return modelAndView;
	}

	public ModelAndView deleteTeamJLPTemplate(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : deleteTeamJLPTemplate");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		this.teamJLPSummaryManager.deleteTeamJLPTemplate(teamJLPSummaryVO);
		return modelAndView;
	}

	public ModelAndView searchTeamJLPTemplate(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : searchTeamJLPTemplate");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");

		try {
			if (teamJLPSummaryVO.getTemplateCreator().equalsIgnoreCase("")
					&& teamJLPSummaryVO.getTemplateName().equalsIgnoreCase("")) {
				teamJLPSummaryVO.setUserId(userBean.getUserName());
			} else if (teamJLPSummaryVO.getTemplateCreator().equalsIgnoreCase("ALL")
					&& teamJLPSummaryVO.getTemplateName().equalsIgnoreCase("")) {
				teamJLPSummaryVO.setTemplateCreator("");
			}

			if (!userBean.getUserName().equalsIgnoreCase(teamJLPSummaryVO.getTemplateCreator())) {
				teamJLPSummaryVO.setSharedWith("1");
			}

			this.logger.debug("Search: creator" + teamJLPSummaryVO.getTemplateCreator() + ", login user id"
					+ teamJLPSummaryVO.getUserId() + ", Name:" + teamJLPSummaryVO.getTemplateName() + ", Shared :"
					+ teamJLPSummaryVO.getSharedWith());
			List<TeamJLPSummaryVO> searchList = this.teamJLPSummaryManager.searchTeamJLPTemplate(teamJLPSummaryVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			modelAndView.addObject("searchList", searchList);
			modelAndView.addObject("total", this.teamJLPSummaryManager.searchTeamJLPTemplateCount(teamJLPSummaryVO));
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getAllActiveUsers(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("In JSONTeamJLPSummaryController : getAllActiveUsers");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();
		TeamJLPSummaryVO teamJLPSummaryVO = new TeamJLPSummaryVO();

		try {
			if (request.getParameter("templateId") != null) {
				teamJLPSummaryVO.setTemplateId(Integer.parseInt(request.getParameter("templateId")));
			}

			List<UserMasterVO> userMasterList = this.teamJLPSummaryManager.getAllActiveUsers(teamJLPSummaryVO);
			modelAndView.addObject("activeUserList", userMasterList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getSelectedUsers(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In JSONTeamJLPSummaryController : getAllActiveUsers");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();

		try {
			if (request.getParameter("templateId") != null) {
				teamJLPSummaryVO.setTemplateId(Integer.parseInt(request.getParameter("templateId")));
				List<UserMasterVO> resultList = this.teamJLPSummaryManager.getSelectedUsers(teamJLPSummaryVO);
				modelAndView.addObject("selectedUserList", resultList);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getAllTemplateCreator(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("In JSONTeamJLPSummaryController : getAllTemplateCreator");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();

		try {
			List<UserMasterVO> templateCreatorList = this.teamJLPSummaryManager.getAllTemplateCreator();
			modelAndView.addObject("allTemplateCreatorList", templateCreatorList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView isExistTemplateName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : isExistTemplateName");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			boolean isExist = this.teamJLPSummaryManager.isExistTemplateName(request.getParameter("templateName"));
			modelAndView.addObject("isExist", isExist);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView addToDefaultTeamJLPTemplate(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : addToDefaultTeamJLPTemplate");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			teamJLPSummaryVO.setUserName(userBean.getUserName());
			this.teamJLPSummaryManager.addToDefaultTeamJLPTemplate(teamJLPSummaryVO);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView removeFromDefaultTeamJLPTemplate(HttpServletRequest request, HttpServletResponse response,
			TeamJLPSummaryVO teamJLPSummaryVO) throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : removeFromDefaultTeamJLPTemplate");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			teamJLPSummaryVO.setUserName(userBean.getUserName());
			this.teamJLPSummaryManager.removeFromDefaultTeamJLPTemplate(teamJLPSummaryVO);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView searchTeamJLPExcelReport(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In JSONTeamJLPSummaryController : searchTeamJLPExcelReport");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();

		try {
			if (request.getParameter("year") != null && request.getParameter("month") != null
					&& request.getParameter("misReportType") != null && request.getParameter("templateId") != null) {
				String year = request.getParameter("year");
				String month = request.getParameter("month");
				String type = request.getParameter("misReportType");
				String templateId = request.getParameter("templateId");
				String sortingColumn = request.getParameter("sort");
				String sortType = request.getParameter("dir");
				HashMap<String, String> inputHMap = new HashMap();
				inputHMap.put("year", year);
				inputHMap.put("month", month);
				inputHMap.put("misReportType", type);
				inputHMap.put("templateId", templateId);
				inputHMap.put("sortingColumn", sortingColumn);
				inputHMap.put("sortType", sortType);
				request.setAttribute("reportName", "teamJLPSummary");
				TeamJLPSummaryManager teamJLPManager = (TeamJLPSummaryManager) this.atlasReportService
						.getReportObject(request);
				List<TeamJLPSummaryVO> resultList = teamJLPManager.fetchJLPReport(inputHMap);
				modelAndView.addObject("searchList", resultList);
			}

			return modelAndView;
		} catch (CMSException var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		} catch (Exception var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		}
	}
}