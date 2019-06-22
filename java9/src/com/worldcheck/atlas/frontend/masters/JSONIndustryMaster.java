package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IIndustryMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONIndustryMaster extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.masters.JSONIndustryMaster");
	private String JSONVIEW = "jsonView";
	private String INDUSTRYGRIDLIST = "industryGridList";
	private String INDUSTRY_CODE = "industryCode";
	private String INDUSTRY_STATUS = "industryStatus";
	private String SUCCESS = "success";
	private String TRUE = "true";
	private String FALSE = "false";
	private String ACTION = "action";
	private String resultMsg = "";
	private String RESULT_MSG = "resultMsg";
	private String RESULT = "result";
	private String BLANK = "";
	private ModelAndView mv = null;
	private ArrayList industryGridList = null;
	private IIndustryMaster industryManager;

	public void setIndustryManager(IIndustryMaster industryManager) {
		this.industryManager = industryManager;
	}

	public ModelAndView searchIndustry(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		try {
			this.mv = new ModelAndView(this.JSONVIEW);
			this.logger.debug("\nIndustry:" + industryMasterVO.getIndustry() + "\nIndustryCode:"
					+ industryMasterVO.getIndustryCode() + "\nStatus:" + industryMasterVO.getStatus());
			if (this.BLANK.equals(industryMasterVO.getIndustry()) && this.BLANK.equals(industryMasterVO.getStatus())) {
				this.industryGridList = new ArrayList();
			} else {
				this.industryGridList = (ArrayList) this.industryManager.getIndustryGrid(industryMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				int count = this.industryManager.getIndustryGridCount(industryMasterVO);
				this.mv.addObject("total", count);
			}

			this.mv.addObject(this.INDUSTRYGRIDLIST, this.industryGridList);
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return this.mv;
	}

	public ModelAndView searchExistIndustry(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		this.logger.debug("Searching for Exist Currency");

		try {
			this.mv = new ModelAndView("jsonView");
			boolean resultCountry = this.industryManager.isExistIndustry(industryMasterVO.getIndustry());
			if (resultCountry) {
				this.mv.addObject(this.SUCCESS, this.TRUE);
			} else {
				this.mv.addObject(this.SUCCESS, this.FALSE);
			}
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView changeIndustryStatus(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		try {
			this.mv = new ModelAndView(this.JSONVIEW);
			String industryCode = request.getParameter(this.INDUSTRY_CODE);
			String industryStatus = request.getParameter(this.INDUSTRY_STATUS);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("Industry Codes:>>>>>>>" + industryCode);
			List<String> industryCodeList = StringUtils.commaSeparatedStringToList(industryCode);
			String resultMessage = this.industryManager.changeIndustryStatus(industryCodeList, industryStatus,
					userBean.getUserName());
			if (resultMessage.split("#")[1].equals("success")) {
				this.mv.addObject(this.RESULT, this.SUCCESS);
			} else {
				this.mv.addObject(this.RESULT, resultMessage.split("#")[0]);
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("INDUSTRY_MASTER");
			if (industryStatus.equals("1")) {
				this.logger.info(industryMasterVO.getIndustry() + " successfully deactivated.");
			} else {
				this.logger.info(industryMasterVO.getIndustry() + " successfully activated.");
			}
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		return this.mv;
	}

	public ModelAndView isIndustryAssociated(HttpServletRequest request, HttpServletResponse response,
			IndustryMasterVO industryMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			this.logger.debug("Industry Master Code:" + industryMasterVO.getIndustryCode());
			industryMasterVO = this.industryManager.checkAssociatedMaster(industryMasterVO.getIndustryCode());
			if (!industryMasterVO.getTotalCRN().equalsIgnoreCase("0")
					|| !industryMasterVO.getSubjectCount().equalsIgnoreCase("0")) {
				mv.addObject("isAssociated", true);
				mv.addObject("industryMasterVO", industryMasterVO);
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}
}