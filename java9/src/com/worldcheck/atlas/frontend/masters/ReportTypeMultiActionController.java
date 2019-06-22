package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IReportTypeMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ReportTypeMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.ReportTypeMultiActionController");
	private static final String ACTION = "action";
	private static final String GET_RPT_TYPE = "getRptInfo";
	private static final String IS_SUBREPORT = "isSubreport";
	private static final String RPT_ID = "rptId";
	private static final String REPORT_TYPE_ADD = "report_type_add";
	private static final String REPORT_TYPE_LIST = "report_type_list";
	private static final String SUBMIT_TYPE = "submitType";
	private static final String SAVE = "save";
	private static final String UPDATE = "update";
	private IReportTypeMaster reportTypeManager = null;

	public void setReportTypeManager(IReportTypeMaster reportTypeManager) {
		this.reportTypeManager = reportTypeManager;
	}

	public ModelAndView addReportTypeAndNew(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String userName = null;

		try {
			userName = userBean.getUserName();
			HttpSession session = request.getSession();
			String turnTime = request.getParameter("turnTime");
			String researchElement = request.getParameter("researchElement");
			this.logger.debug("Research Elements names::::::::::::" + researchElement);
			reportTypeMasterVO.setResearchElementNames(researchElement);
			reportTypeMasterVO.setTurnaroundTime(turnTime);
			String needSubRptType = request.getParameter("rp-subType");
			this.logger.debug("needSubRptType  ::::  " + needSubRptType);
			reportTypeMasterVO.setNeedSubReportType(needSubRptType);
			String message = "Add ReportType and New ReportType";
			this.logger.debug(
					"ReportTypeMultiActionController.addReportTypeAndNew()" + reportTypeMasterVO.getReportTypeCode());
			this.logger.debug("report type status is   " + reportTypeMasterVO.getReportTypeStatus());
			this.logger.debug("report master turn around time   " + reportTypeMasterVO.getTurnaroundTime());
			String reName = request.getParameter("associatedResearchElements");
			this.logger.debug("parameter map :::::   " + request.getParameterMap());
			if (reportTypeMasterVO.getNeedSubReportType() == null) {
				reportTypeMasterVO.setNeedSubReportType("2");
			}

			if (reName != null && !reName.equals("")) {
				reName = reName.substring(0, reName.lastIndexOf(44));
				this.logger.debug("selected re's listr >>>   " + reName);
			}

			this.logger.debug("RETest ######" + reName);
			reportTypeMasterVO.setResearchElement(reName);
			reportTypeMasterVO.setUserName(userName);
			String resultMessage = this.reportTypeManager.addReportISIS(reportTypeMasterVO);
			ResourceLocator.self().getCacheService().addToCacheRunTime("REPORT_TYPE_MASTER");
			ResourceLocator.self().getCacheService().addToCacheRunTime("REPORT_TYPE_MASTER_ONLY_WITH_SRPT");
			session.setAttribute("save", true);
			if (resultMessage.split("#")[1].equals("success")) {
				session.setAttribute("Message", "success");
			} else {
				session.setAttribute("Message", resultMessage.split("#")[0]);
			}

			this.logger.info(reportTypeMasterVO.getReportType() + " successfully added.");
			ModelAndView mv = new ModelAndView("redirect:searchReportType.do");
			return mv;
		} catch (CMSException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}
	}

	public ModelAndView getReportTypeInfo(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		ModelAndView mv = new ModelAndView("report_type_add");
		this.logger.debug("ReportTypeMultiActionController.getReportTypeInfo()");

		try {
			this.logger.debug("ReportType Id Updated:" + reportTypeMasterVO.getReportTypeId());
			if (reportTypeMasterVO.getReportTypeId() == 0L) {
				return new ModelAndView("redirect:searchReportType.do");
			} else {
				ReportTypeMasterVO rptVO = this.reportTypeManager.getRptInfo(reportTypeMasterVO.getReportTypeId());
				mv.addObject("getRptInfo", rptVO);
				mv.addObject("action", "update");
				int count = this.reportTypeManager.checkSubRpt(reportTypeMasterVO.getReportTypeId());
				if (count > 0) {
					mv.addObject("isSubreport", "1");
				}

				return mv;
			}
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView updateReportType(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");

		try {
			String userName = userBean.getUserName();
			String turnTime = request.getParameter("turnTime");
			reportTypeMasterVO.setTurnaroundTime(turnTime);
			HttpSession session = request.getSession();
			String needSubRptType = request.getParameter("rp-subType");
			String reName = request.getParameter("associatedResearchElements");
			String message = "Add ReportType and New ReportType";
			String isRPTStatusChange = request.getParameter("isStatusChange");
			String researchElement = request.getParameter("researchElement");
			this.logger.debug("Research Elements names::::::::::::" + researchElement);
			reportTypeMasterVO.setResearchElementNames(researchElement);
			reportTypeMasterVO.setHdnInitialsUseCRN(request.getParameter("hdnInitCRN"));
			reportTypeMasterVO.setHdnTurnaroundTime(request.getParameter("hdnTurntime"));
			reportTypeMasterVO.setHdnNeedSubReportType(request.getParameter("hdnNeedSubreport"));
			reportTypeMasterVO.setHdnAssociatedResearchElements(request.getParameter("hdnAssociatedResearchElements"));
			reportTypeMasterVO.setHdnResearchElementNames(request.getParameter("hdnResearchElement"));
			this.logger.debug("Old data ###" + reportTypeMasterVO.getHdnInitialsUseCRN() + ","
					+ reportTypeMasterVO.getHdnTurnaroundTime() + "," + reportTypeMasterVO.getHdnResearchElementNames()
					+ "," + reportTypeMasterVO.getHdnNeedSubReportType());
			this.logger.debug("reportType master ID:::>" + reportTypeMasterVO.getReportTypeId());
			this.logger.debug("needSubRptType  ::::  " + needSubRptType);
			this.logger.debug(
					"ReportTypeMultiActionController.addReportTypeAndNew()" + reportTypeMasterVO.getReportTypeCode());
			this.logger.debug("report type status is" + reportTypeMasterVO.getReportTypeStatus());
			this.logger.debug("report master turn around time " + reportTypeMasterVO.getTurnaroundTime());
			this.logger.debug("parameter map :::::   " + request.getParameterMap());
			if (reName != null && !reName.equals("")) {
				reName = reName.substring(0, reName.lastIndexOf(44));
			}

			this.logger.debug("selected re's listr >>>   " + reName);
			reportTypeMasterVO.setNeedSubReportType(needSubRptType);
			reportTypeMasterVO.setResearchElement(reName);
			reportTypeMasterVO.setUserName(userName);
			reportTypeMasterVO.setIsRPTStatusChange(isRPTStatusChange);
			String resultMessage = this.reportTypeManager.updateReportISIS(reportTypeMasterVO);
			ResourceLocator.self().getCacheService().addToCacheRunTime("REPORT_TYPE_MASTER");
			ResourceLocator.self().getCacheService().addToCacheRunTime("REPORT_TYPE_MASTER_ONLY_WITH_SRPT");
			session.setAttribute("update", true);
			if (resultMessage.split("#")[1].equals("success")) {
				session.setAttribute("Message", "success");
			} else {
				session.setAttribute("Message", resultMessage.split("#")[0]);
			}

			this.logger.info(reportTypeMasterVO.getReportType() + " successfully updated.");
			ModelAndView mv = new ModelAndView("redirect:searchReportType.do");
			return mv;
		} catch (CMSException var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		} catch (Exception var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}
	}

	public ModelAndView searchReportType(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		try {
			ModelAndView mv = new ModelAndView("report_type_list");
			if (request.getSession().getAttribute("save") != null) {
				mv.addObject("submitType", "save");
				request.getSession().removeAttribute("save");
			}

			if (request.getSession().getAttribute("update") != null) {
				mv.addObject("submitType", "update");
				request.getSession().removeAttribute("update");
			}

			return mv;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView reportMasteraddJSP(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		try {
			ModelAndView mv = new ModelAndView("report_type_add");
			return mv;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}
}