package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IIndustryMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class IndustryMultiActionController extends MultiActionController {
	private static final String SAVE = "save";
	private static final String SUBMIT_TYPE = "submitType";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.IndustryMultiActionController");
	private String JSP_INDUSTRY_MASTER = "industry";
	private String JSP_ADD_INDUSTRY_MASTER = "industry_new";
	private String INDUSTRY_MASTER = "industryMaster";
	private String PREPEND_CODE = "MBT00";
	private String ACTION = "action";
	private String UPDATE = "update";
	private String BLANK = "";
	private String ONE = "1";
	private String resultMsg = "";
	private String RESULT_MSG = "resultMsg";
	private ModelAndView mv = null;
	private IIndustryMaster industryManager;

	public void setIndustryManager(IIndustryMaster industryManager) {
		this.industryManager = industryManager;
	}

	public ModelAndView addIndustry(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) throws CMSException {
		String resultMessage = null;

		try {
			this.mv = new ModelAndView("redirect:industrySearchJspRedirect.do");
			HttpSession session = request.getSession();
			this.logger.debug("Inside the IndustryMultiActionController");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			industryMasterVO.setUpdatedBy(userBean.getUserName());
			String action = request.getParameter(this.ACTION);
			if (null != action && !this.BLANK.equalsIgnoreCase(action.trim())) {
				resultMessage = this.industryManager.updateIndustry(industryMasterVO);
				session.setAttribute(this.UPDATE, true);
				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
				}
			} else {
				industryMasterVO.setPrePendIndustryCode(this.PREPEND_CODE);
				industryMasterVO.setStatus(this.ONE);
				this.logger.debug(industryMasterVO.getPrePendIndustryCode() + industryMasterVO.getIndustry()
						+ industryMasterVO.getStatus());
				resultMessage = this.industryManager.addIndustry(industryMasterVO);
				session.setAttribute("save", true);
				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
				}
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("INDUSTRY_MASTER");
			if (null != action && !this.BLANK.equalsIgnoreCase(action.trim())) {
				this.logger.info(industryMasterVO.getIndustry() + " successfully updated.");
			} else {
				this.logger.info(industryMasterVO.getIndustry() + " successfully added.");
			}
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}

		return this.mv;
	}

	public ModelAndView getIndustryInfo(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		try {
			this.logger.debug("\n<<<<<<Industry Code>>>>>>>>:" + industryMasterVO.getIndustryCode());
			if (null == industryMasterVO.getIndustryCode() || industryMasterVO.getIndustryCode().equalsIgnoreCase("")) {
				return new ModelAndView("redirect:industrySearchJspRedirect.do");
			}

			this.mv = new ModelAndView(this.JSP_ADD_INDUSTRY_MASTER);
			this.logger.debug("\nIndustry :" + industryMasterVO.getIndustry() + "\nStatus :"
					+ industryMasterVO.getStatus() + "\nIndustry Code:" + industryMasterVO.getIndustryCode());
			industryMasterVO = this.industryManager.getIndustryInfo(industryMasterVO.getIndustryCode());
			this.mv.addObject(this.INDUSTRY_MASTER, industryMasterVO);
			this.mv.addObject(this.ACTION, this.UPDATE);
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}

		return this.mv;
	}

	public ModelAndView industryAddUpdateJspRedirect(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		try {
			this.mv = new ModelAndView(this.JSP_ADD_INDUSTRY_MASTER);
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}

	public ModelAndView industrySearchJspRedirect(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		try {
			this.mv = new ModelAndView(this.JSP_INDUSTRY_MASTER);
			if (request.getSession().getAttribute("save") != null) {
				this.mv.addObject("submitType", "save");
				request.getSession().removeAttribute("save");
			}

			if (request.getSession().getAttribute(this.UPDATE) != null) {
				this.mv.addObject("submitType", this.UPDATE);
				request.getSession().removeAttribute(this.UPDATE);
			}
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}
}