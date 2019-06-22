package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IRiskMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.RisksMapVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class RisksMultiActionController extends MultiActionController {
	private static final String SAVE = "save";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.RisksMultiActionController");
	private String JSP_RISK_MASTER = "risks_master";
	private static final String SUBMIT_TYPE = "submitType";
	private String JSP_RISK_ADD = "risks_new";
	private String RISKMASTER = "risksMaster";
	private String RISKCODE = "riskCode";
	private String PREPEND_RISKCODE = "R00";
	private String ZERO = "0";
	private String ONE = "1";
	private IRiskMaster riskManager;
	private ModelAndView mv = null;
	private String ACTION = "action";
	private String UPDATE = "update";
	private String resultMsg = "";
	private String RESULT = "result";
	private String HiddenValue = "hiddenValues";
	private String SAVE_AND_ADD_NEW = "SaveAndAddNew";

	public void setRiskManager(IRiskMaster riskManager) {
		this.riskManager = riskManager;
	}

	public ModelAndView getRiskInfo(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		try {
			this.logger.debug("RisksMultiActionController.getRiskInfo()" + risksMasterVO.getRiskCode());
			if (risksMasterVO.getRiskCode() == null || risksMasterVO.getRiskCode().equalsIgnoreCase("")) {
				return new ModelAndView("redirect:riskSearchJspRedirect.do");
			}

			this.mv = new ModelAndView(this.JSP_RISK_ADD);
			risksMasterVO = this.riskManager.getRiskInfo(risksMasterVO.getRiskCode());
			this.mv.addObject(this.RISKMASTER, risksMasterVO);
			this.mv.addObject(this.ACTION, this.UPDATE);
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}

		return this.mv;
	}

	public ModelAndView addRisks(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ArrayList<RisksMapVO> mappingList = new ArrayList();
		String resultMessage = null;
		Object var8 = null;

		try {
			this.mv = new ModelAndView("redirect:riskSearchJspRedirect.do");
			HttpSession session = request.getSession();
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			risksMasterVO.setUpdatedBy(userBean.getUserName());
			String action = request.getParameter(this.ACTION);
			this.logger.debug("Inside the addRisk of RiskMultiAction:" + action);
			String mappings;
			if (action.equalsIgnoreCase(this.UPDATE)) {
				mappings = request.getParameter(this.HiddenValue);
				this.logger.debug("HiddenValues=========" + mappings);
				this.logger.debug("riskCode:" + risksMasterVO.getRiskCode() + "::: risk:" + risksMasterVO.getRiskLabel()
						+ "::: status:" + risksMasterVO.getRiskActive());
				resultMessage = this.riskManager.updateRisk(risksMasterVO, mappings);
				session.setAttribute(this.UPDATE, true);
				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
					this.mv.addObject("Message", "success");
				} else if (resultMessage.split("#")[1].equals("NoChange")) {
					session.setAttribute("Message", "NoChange");
					this.mv.addObject("Message", "NoChange");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
					this.mv.addObject("Message", resultMessage.split("#")[0]);
				}
			} else {
				risksMasterVO.setStatus(this.ONE);
				risksMasterVO.setRiskIsActive(1);
				risksMasterVO.setPrePendRiskCode(this.PREPEND_RISKCODE);
				mappings = risksMasterVO.getJsonData();
				this.logger.debug("data------>>" + mappings);
				JSONArray jsonArray = new JSONArray(mappings);
				this.logger.debug("jsonArray" + jsonArray);
				this.logger.debug("jsonArray.length()--->>" + jsonArray.length());

				for (int i = 0; i < jsonArray.length(); ++i) {
					RisksMapVO risksMapVO = new RisksMapVO();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					this.logger.debug("json object returned is ----" + jsonObject);
					risksMapVO.setRiskMappingName(jsonObject.get("riskMappingName").toString());
					risksMapVO.setRiskGroup(Integer.parseInt(jsonObject.get("riskGroup").toString()));
					risksMapVO.setVisibleToClient(Integer.parseInt(jsonObject.get("visibleToClient").toString()));
					risksMapVO.setReportTypes(jsonObject.get("reportTypes").toString());
					risksMapVO.setSubjectType(jsonObject.get("subjectType").toString());
					risksMapVO.setSubReportTypes(jsonObject.get("subReportTypes").toString());
					risksMapVO.setSubjectCountry(jsonObject.get("subjectCountry").toString());
					risksMapVO.setMappingStatus(Integer.parseInt(jsonObject.get("mappingStatus").toString()));
					risksMapVO.setResearchElements(jsonObject.get("researchElements").toString());
					risksMapVO.setClientCodes(jsonObject.get("clientCodes").toString());
					risksMapVO.setReportTypeNames(jsonObject.get("reportTypeNames").toString());
					risksMapVO.setCountryCodes(jsonObject.get("countryCodes").toString());
					risksMapVO.setUpdatedBy(userBean.getUserName());
					mappingList.add(i, risksMapVO);
				}

				risksMasterVO.setRiskMapVO(mappingList);
				resultMessage = this.riskManager.addRisks(risksMasterVO);
				session.setAttribute("save", true);
				String saveAndAddNew = request.getParameter(this.SAVE_AND_ADD_NEW);
				this.logger.debug("saveAndAddNew====>>" + saveAndAddNew);
				if (saveAndAddNew.equals("1")) {
					this.logger.debug("redirecting to the same js");
					session.setAttribute(this.SAVE_AND_ADD_NEW, "2");
					this.mv = new ModelAndView("redirect:riskaddUpdateJspRedirect.do");
					this.logger.debug("inside save and add new mv view name -->>" + this.mv.getViewName());
				} else {
					session.setAttribute("save", true);
				}

				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("RISK_CODE", resultMessage.split("#")[0]);
					session.setAttribute("Message", "success");
					this.mv.addObject("Message", "success");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
					this.mv.addObject("Message", resultMessage.split("#")[0]);
				}
			}

			this.mv.addObject(this.RESULT, this.resultMsg);
			ResourceLocator.self().getCacheService().addToCacheRunTime("RISK_MASTER");
			if (action.equalsIgnoreCase(this.UPDATE)) {
				this.logger.info(risksMasterVO.getRisks() + " successfully updated.");
			} else {
				this.logger.info(risksMasterVO.getRisks() + " successfully added.");
				this.logger.info(risksMasterVO.getRiskLabel() + " successfully added.");
			}
		} catch (CMSException var16) {
			AtlasUtils.getExceptionView(this.logger, var16);
		} catch (Exception var17) {
			AtlasUtils.getExceptionView(this.logger, var17);
		}

		this.logger.debug("returning  mv view name -->>" + this.mv.getViewName());
		return this.mv;
	}

	public ModelAndView riskaddUpdateJspRedirect(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView(this.JSP_RISK_ADD);
			return mv;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView riskSearchJspRedirect(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView(this.JSP_RISK_MASTER);
			request.getSession().removeAttribute(this.SAVE_AND_ADD_NEW);
			if (request.getSession().getAttribute("save") != null) {
				mv.addObject("submitType", "save");
				request.getSession().removeAttribute("save");
				request.getSession().removeAttribute(this.SAVE_AND_ADD_NEW);
			}

			if (request.getSession().getAttribute(this.UPDATE) != null) {
				mv.addObject("submitType", this.UPDATE);
				request.getSession().removeAttribute(this.UPDATE);
			}

			return mv;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}
}