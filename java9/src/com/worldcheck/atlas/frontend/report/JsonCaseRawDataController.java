package com.worldcheck.atlas.frontend.report;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.report.ICaseRawDataReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.JSONBean;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.CaseRawTableVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JsonCaseRawDataController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JsonCaseRawDataController");
	private ICaseRawDataReport caseRawDataReport;
	private static final String CASE_RAW_DATA_VO_LIST = "caseRawDataVOList";
	private static final String TEMPLATE_NAME = "templateName";
	private static final String LOAD_VALUE = "loadValue";
	private static final String TEMPLATE_CREATOR = "uploadedBy";
	private static final String RESULT = "result";
	private ModelAndView mv = null;
	private String templateCreator;
	private String templateName;
	private String loadCondition;

	public ICaseRawDataReport getCaseRawDataReport() {
		return this.caseRawDataReport;
	}

	public void setCaseRawDataReport(ICaseRawDataReport caseRawDataReport) {
		this.caseRawDataReport = caseRawDataReport;
	}

	public ModelAndView searchCaseRawDataTemplate(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		List caseRawDataVOList = null;

		try {
			this.logger.debug("inside JsonCaseRawDataTemplate:: searchCaseRawDataTemplate");
			this.mv = new ModelAndView("jsonView");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			caseRawTableVO.setUserId(userBean.getUserName());
			this.logger.debug("searchType::" + caseRawTableVO.getSearchType() + "templateType:"
					+ caseRawTableVO.getTemplateName());
			this.logger.debug("createdBy:" + caseRawTableVO.getCreated_by() + "userid:" + caseRawTableVO.getUserId());
			caseRawDataVOList = this.caseRawDataReport.getCaseRawDataGrid(caseRawTableVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			int count = this.caseRawDataReport.getCaseRawDataGridCount(caseRawTableVO);
			this.mv.addObject("total", count);
			this.mv.addObject("caseRawDataVOList", caseRawDataVOList);
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		return this.mv;
	}

	public ModelAndView getAllTemplateCreator(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("In JSONTeamJLPSummaryController : getAllTemplateCreator");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();

		try {
			List<UserMasterVO> templateCreatorList = this.caseRawDataReport.getAllTemplateCreator();
			modelAndView.addObject("TemplateCreatorList", templateCreatorList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView searchExistCaseRawDataTemplate(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("Searching for Exist CaseRawData Template");
		String templateName = request.getParameter("templateName");
		this.logger.debug("templateName:" + templateName);

		try {
			this.mv = new ModelAndView("jsonView");
			boolean result = this.caseRawDataReport.isExistCaseRawDataTemplateName(templateName);
			this.mv.addObject("result", result);
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return this.mv;
	}

	public ModelAndView getInfoListCaseRawDataTemplate(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("Get getInfoListCaseRawDataTemplate");

		try {
			this.mv = new ModelAndView("jsonView");
			List<CaseRawTableVO> infoList = this.caseRawDataReport.getInfoListCaseRawDataTemplate();
			this.mv.addObject("infoList", infoList);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView getAvailableOption(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		List<JSONBean> optionList = null;
		this.logger.debug("JsonCaseRawDataController getAvailableOption");
		List<JSONBean> optionMap = null;
		Object var9 = null;

		try {
			this.mv = new ModelAndView("jsonView");
			String listType = request.getParameter("ListType");
			String action = request.getParameter("status");
			this.logger.debug("ListType:" + listType);
			if (listType != null || "".equalsIgnoreCase(listType)) {
				optionMap = this.caseRawDataReport.getAvailableOption(listType);
			}

			this.mv.addObject("optionMap", optionMap);
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}

		return this.mv;
	}

	public ModelAndView getSelectedOption(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		List<JSONBean> optionList = null;
		this.logger.debug("JsonCaseRawDataController getSelectedOption");
		List<JSONBean> optionMap = null;
		List selOptionMap = null;

		try {
			this.mv = new ModelAndView("jsonView");
			String listType = request.getParameter("ListType");
			String action = request.getParameter("status");
			this.logger.debug("ListType:" + listType);
			if (action != null && action.equals("update")) {
				if (!request.getParameter("selectedInfoId").equals("")) {
					selOptionMap = this.caseRawDataReport.getSelectedOption(listType,
							Long.parseLong(request.getParameter("selectedInfoId")));
					this.mv.addObject("selectedOptionMap", selOptionMap);
				} else {
					this.mv.addObject("selectedOptionMap", new ArrayList());
				}
			}
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}

		return this.mv;
	}

	public ModelAndView saveCaseRawDataTemplate(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		List<JSONBean> optionList = null;
		this.logger.debug("JsonCaseRawDataController saveCaseRawDataTemplate");
		int templateId = 0;

		try {
			this.mv = new ModelAndView("jsonView");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String action = request.getParameter("action");
			this.logger.debug(request.getParameter("action"));
			this.logger.debug("templateName:" + caseRawTableVO.getTemplateName());
			this.logger.debug("shareWith:" + caseRawTableVO.getShareWith());
			this.logger.debug("select info:" + caseRawTableVO.getSelectedInfo());
			this.logger.debug("select Option's:" + caseRawTableVO.getSelectedOption());
			caseRawTableVO.setUpdated_by(userBean.getUserName());
			caseRawTableVO.setCreated_by(userBean.getUserName());
			if (caseRawTableVO.getShareWith().equalsIgnoreCase("mySelf Only")) {
				caseRawTableVO.setShareWith("0");
			} else {
				caseRawTableVO.setShareWith("1");
			}

			if (action.equals("save")) {
				templateId = this.caseRawDataReport.saveCaseRawDataTemplate(caseRawTableVO);
			} else if (action.equals("update")) {
				this.logger.debug("caseRawDataId:" + caseRawTableVO.getTemplateId());
				long deleteRow = this.caseRawDataReport
						.deleteCaseRawDataTemplate(Long.parseLong(caseRawTableVO.getTemplateId()));
				templateId = this.caseRawDataReport.saveCaseRawDataTemplate(caseRawTableVO);
			}

			this.mv.addObject("templateId", templateId);
			this.mv.addObject("result", "success");
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}

		return this.mv;
	}

	public ModelAndView deleteCaseRawDataTemplate(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("JsonCaseRawDataController deleteCaseRawDataTemplate");

		try {
			this.mv = new ModelAndView("jsonView");
			String templateId = request.getParameter("templateId");
			this.logger.debug(request.getParameter("templateId"));
			this.logger.debug("templateName:" + caseRawTableVO.getTemplateName());
			this.logger.debug("shareWith:" + caseRawTableVO.getShareWith());
			this.logger.debug("select info:" + caseRawTableVO.getSelectedInfo());
			long deleteRow = this.caseRawDataReport.deleteCaseRawDataTemplate(Long.parseLong(templateId));
			this.mv.addObject("deleteRow", deleteRow);
			this.mv.addObject("result", "success");
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		return this.mv;
	}

	public ModelAndView getCaseRawDataTemplateInfo(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("JsonCaseRawDataController getCaseRawDataTemplateInfo");

		try {
			this.mv = new ModelAndView("jsonView");
			String templateId = request.getParameter("templateId");
			this.logger.debug("templateId::" + request.getParameter("templateId"));
			caseRawTableVO = this.caseRawDataReport.getCaseRawDataTemplateInfo(Long.parseLong(templateId));
			this.mv.addObject("caseRawTableVO", caseRawTableVO);
			this.mv.addObject("result", "success");
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return this.mv;
	}

	public ModelAndView setDefaultCaseRawDataTemplate(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("JsonCaseRawDataController setDefaultCaseRawDataTemplate");

		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("TemplateId:" + caseRawTableVO.getTemplateId());
			this.logger.debug("TemplateStatus:" + caseRawTableVO.getDefaultTemplate());
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			long updatedRow = this.caseRawDataReport.setDefaultCaseRawDataTemplate(
					Long.parseLong(caseRawTableVO.getTemplateId()), Long.parseLong(caseRawTableVO.getDefaultTemplate()),
					userBean.getUserName());
			this.mv.addObject("updatedRows", updatedRow);
			this.mv.addObject("result", "success");
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		return this.mv;
	}

	public ModelAndView getSelectedInfoFieldsListCaseRawDataTemplate(HttpServletRequest request,
			HttpServletResponse response, CaseRawTableVO caseRawTableVO) {
		this.logger.debug("JsonCaseRawDataController getSelectedInfoFieldsListCaseRawDataTemplate");

		try {
			this.mv = new ModelAndView("jsonView");
			String templateId = request.getParameter("templateId");
			this.logger.debug("templateId::" + request.getParameter("templateId"));
			List<CaseRawTableVO> selectedInfoList = this.caseRawDataReport
					.getSelectedInfoFieldsListCaseRawDataTemplate(Long.parseLong(templateId));
			CaseRawTableVO crdObj = (CaseRawTableVO) selectedInfoList.get(selectedInfoList.size() - 1);
			selectedInfoList.remove(selectedInfoList.size() - 1);
			this.mv.addObject("selectedCRDInfo", crdObj.getSelectedInfo());
			this.mv.addObject("selectedCRDOption", crdObj.getSelectedOption());
			this.mv.addObject("fieldFullName", crdObj.getFieldFullName());
			this.mv.addObject("selectedInfoList", selectedInfoList);
			this.mv.addObject("result", "success");
			this.mv.addObject("resultAction", "update");
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		return this.mv;
	}

	public ModelAndView getSubReportList(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.mv = new ModelAndView("jsonView");
			List<ReportTypeMasterVO> subReportTypeList = this.caseRawDataReport.getSubReportList();
			Iterator var5 = subReportTypeList.iterator();

			while (var5.hasNext()) {
				ReportTypeMasterVO subReport = (ReportTypeMasterVO) var5.next();
				this.logger.debug("report type for each::" + subReport.getReportType() + "   sub_report_name:::"
						+ subReport.getSubReportType());
			}

			if (subReportTypeList.size() > 0) {
				this.mv.addObject("subReportTypeList", subReportTypeList);
				this.mv.addObject("result", "true");
			} else {
				this.mv.addObject("result", "false");
			}

			this.logger.debug("After getting values exit from method");
			return this.mv;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getMaterializedViewRefreshTime(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("MV name::::" + request.getParameter("mvName"));
			List<MaterializedViewVO> mvRefreshList = this.caseRawDataReport
					.getMaterializedViewRefreshTime(request.getParameter("mvName"));
			this.logger.debug("MV LIST ON controller:" + mvRefreshList.size());
			this.mv.addObject("mvRefreshList", mvRefreshList);
		} catch (NullPointerException var4) {
			return AtlasUtils.getExceptionView(this.logger, var4);
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}
}