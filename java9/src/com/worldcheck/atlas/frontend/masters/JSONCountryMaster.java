package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ICountryMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCountryMaster extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.masters.JsonCountryMaster");
	private String COUNTRY_STATUS = "countryStatus";
	private String COUNTRYGRIDLIST = "countryGridList";
	private String RESULT = "result";
	private String SUCCESS = "success";
	private String TRUE = "true";
	private String FALSE = "false";
	private String BLANK = "";
	private int count;
	private static final String Zero = "0";
	private ModelAndView mv = null;
	private ICountryMaster countryManager = null;

	public void setCountryManager(ICountryMaster countryManager) {
		this.countryManager = countryManager;
	}

	public ModelAndView searchCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("sort value is:::" + request.getParameter("sort"));
			this.logger.debug("start value is:::" + Integer.parseInt(request.getParameter("start")));
			this.logger.debug("limit value is::" + Integer.parseInt(request.getParameter("limit")));
			this.logger.debug("dir valus is::::" + request.getParameter("dir"));
			ArrayList countryGridList = null;
			if (!this.BLANK.equals(countryMasterVO.getCountry()) && countryMasterVO.getCountry().length() != 0
					|| !this.BLANK.equals(countryMasterVO.getCountryStatus())
							&& countryMasterVO.getCountryStatus().length() != 0
					|| !this.BLANK.equals(countryMasterVO.getRegion()) && countryMasterVO.getRegion().length() != 0
					|| !this.BLANK.equals(countryMasterVO.getRiskLevel())
							&& countryMasterVO.getRiskLevel().length() != 0) {
				countryGridList = (ArrayList) this.countryManager.getCountryGrid(countryMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				this.count = this.countryManager.getCountryGridCount(countryMasterVO);
				this.mv.addObject("total", this.count);
			} else {
				countryGridList = new ArrayList();
			}

			this.mv.addObject(this.COUNTRYGRIDLIST, countryGridList);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView searchExistCountryCodeAndCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		String actionPerform = request.getParameter("actionPerform");
		this.logger.debug("Searching for Exist CountryCode ::" + countryMasterVO.getCountryCode() + "  :: Country"
				+ countryMasterVO.getCountry());

		try {
			this.mv = new ModelAndView("jsonView");
			boolean resultCountry;
			if (actionPerform.equals("save")) {
				boolean resultCountryCode = this.countryManager.isExistCountryCode(countryMasterVO.getCountryCode());
				if (resultCountryCode) {
					this.mv.addObject("countryCode", this.TRUE);
				} else {
					this.mv.addObject("counryCode", this.FALSE);
				}

				resultCountry = this.countryManager.isExistCountry(countryMasterVO.getCountry());
				if (resultCountry) {
					this.mv.addObject("country", this.TRUE);
				} else {
					this.mv.addObject("country", this.FALSE);
				}
			} else if (actionPerform.equals("update")) {
				resultCountry = this.countryManager.isExistCountry(countryMasterVO.getCountry());
				if (resultCountry) {
					this.mv.addObject("country", this.TRUE);
				} else {
					this.mv.addObject("country", this.FALSE);
				}
			}
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		return this.mv;
	}

	public ModelAndView changeStatusCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		String resultMessage = null;

		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("Change the status of Country");
			request.getParameter(this.COUNTRY_STATUS);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("Country Id:" + countryMasterVO.getCountryMasterId() + " status:"
					+ countryMasterVO.getCountryStatus());
			resultMessage = this.countryManager.changeStatus(countryMasterVO.getCountryMasterId(),
					countryMasterVO.getCountryStatus(), userBean.getUserName());
			this.logger.debug("row updated :" + this.count);
			if (resultMessage.split("#")[1].equals("success")) {
				this.mv.addObject(this.RESULT, this.SUCCESS);
			} else {
				this.mv.addObject(this.RESULT, resultMessage.split("#")[0]);
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("COUNTRY_MASTER");
			if (countryMasterVO.getCountryStatus().equals("1")) {
				this.logger.info("Country  :" + countryMasterVO.getCountryCode() + " successfully deactivated");
			} else {
				this.logger.info("Country :" + countryMasterVO.getCountryCode() + " successfully activated");
			}
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		return this.mv;
	}

	public ModelAndView isCountryAssociated(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("Country Master id:" + countryMasterVO.getCountryMasterId());
			countryMasterVO = this.countryManager.checkAssociatedMaster(countryMasterVO.getCountryMasterId());
			if (!countryMasterVO.getTotalCRN().equalsIgnoreCase("0")
					|| !countryMasterVO.getTotalUser().equalsIgnoreCase("0")
					|| !countryMasterVO.getCntryClientCount().equalsIgnoreCase("0")
					|| !countryMasterVO.getVendorPrincipleCntry().equalsIgnoreCase("0")
					|| !countryMasterVO.getVendorCntryMap().equalsIgnoreCase("0")
					|| !countryMasterVO.getCntryDBMap().equalsIgnoreCase("0")
					|| !countryMasterVO.getCntryLbrMap().equalsIgnoreCase("0")
					|| !countryMasterVO.getNonCRNExpenCount().equalsIgnoreCase("0")
					|| !countryMasterVO.getNonCRNVendorCntry().equalsIgnoreCase("0")
					|| !countryMasterVO.getUserProfileCount().equalsIgnoreCase("0")
					|| !countryMasterVO.getSubjectCount().equalsIgnoreCase("0")) {
				this.mv.addObject("isAssociated", true);
				this.mv.addObject("countryMasterVO", countryMasterVO);
			}
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}
}