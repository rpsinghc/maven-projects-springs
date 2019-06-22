package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ICurrencyMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCurrencyMultiActionController extends JSONMultiActionController {
	private static final String SUCCESS = "success";
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.JSONCurrencyMultiActionController";
	private ILogProducer logger;
	private String SEARCH_LIST;
	private String ACTION;
	private String CURRENCY_CODE;
	private String CURRENCY;
	private String CURRENCY_STATUS;
	private static final String BLANK = "";
	private static final String error = "error";
	private String UPDATE_H_CURRENCY_STATUS;
	private static final String Zero = "0";
	private ICurrencyMaster currencyService;

	public JSONCurrencyMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.SEARCH_LIST = "searchList";
		this.ACTION = "action";
		this.CURRENCY_CODE = "currencyCode";
		this.CURRENCY = "currency";
		this.CURRENCY_STATUS = "currencyStatus";
		this.UPDATE_H_CURRENCY_STATUS = "updateHCurrencyStatus";
		this.currencyService = null;
	}

	public void setCurrencyService(ICurrencyMaster currencyService) {
		this.currencyService = currencyService;
	}

	public ModelAndView deactivateCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In deactivateCurrency method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String currencyCodes = request.getParameter(this.ACTION);
			Object message = this.currencyService.deactivateCurrency(currencyCodes, request);

			try {
				Integer.parseInt(message.toString());
				modelAndView.addObject("success", true);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception because ISIS failure" + var8);
				modelAndView.addObject("success", message.toString());
			}

			this.logger.info("currency with currency code :" + currencyCodes + "is successfully deactivated");
			ResourceLocator.self().getCacheService().addToCacheRunTime("CURRENCY_MASTER");
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView activateCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In activateCurrency method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String currencyCodes = request.getParameter(this.ACTION);
			Object message = this.currencyService.activateCurrency(currencyCodes, request);

			try {
				Integer.parseInt(message.toString());
				modelAndView.addObject("success", true);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception because ISIS failure" + var8);
				modelAndView.addObject("success", message.toString());
			}

			this.logger.info("currency with currency code :" + currencyCodes + "is successfully activated");
			ResourceLocator.self().getCacheService().addToCacheRunTime("CURRENCY_MASTER");
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView searchCurrencyByName(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In searchCurrencyByName method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		Object searchList = null;

		try {
			if ("".equalsIgnoreCase(currencyMasterVO.getCurrency())
					&& "".equalsIgnoreCase(currencyMasterVO.getCurrencyStatus())) {
				searchList = new ArrayList();
			} else {
				new ArrayList();
				this.logger.debug("request.getParameter(Constants.PAGINATION_MIN)" + request.getParameter("start"));
				currencyMasterVO.setSortColumnName(request.getParameter("sort"));
				currencyMasterVO.setSortType(request.getParameter("dir"));
				searchList = this.currencyService.searchCurrencyByName(currencyMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")));
				modelAndView.addObject("total", this.currencyService.searchCurrencyByNameCount(currencyMasterVO));
			}

			modelAndView.addObject(this.SEARCH_LIST, searchList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView searchExistCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In searchExistCurrency method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String currency = request.getParameter(this.ACTION);
			this.logger.info("check for currency:" + currency);
			boolean result = this.currencyService.isExistCurrency(currency);
			this.logger.info(String.valueOf(result));
			if (result) {
				modelAndView.addObject("success", true);
			} else {
				modelAndView.addObject("success", false);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView searchExistCurrencyCode(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In searchExistCurrencyCode method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String currencyCode = request.getParameter(this.ACTION);
			this.logger.info("exist for currency code" + currencyCode);
			boolean result = this.currencyService.isExistCurrencyCode(currencyCode);
			this.logger.info(String.valueOf(result));
			if (result) {
				modelAndView.addObject("success", true);
			} else {
				modelAndView.addObject("success", false);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView searchCurrencyByCode(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In searchCurrencyByCode method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			currencyMasterVO.setCurrencyCode(request.getParameter(this.ACTION));
			CurrencyMasterVO searchbyCodeList = this.currencyService.searchCurrencyByCode(currencyMasterVO);
			modelAndView.addObject("success", true);
			modelAndView.addObject(this.CURRENCY_CODE, searchbyCodeList.getCurrencyCode());
			modelAndView.addObject(this.CURRENCY, searchbyCodeList.getCurrency());
			modelAndView.addObject(this.CURRENCY_STATUS, searchbyCodeList.getCurrencyStatus());
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView updateCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In updateCurrency method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			currencyMasterVO.setCurrencyStatus(request.getParameter(this.UPDATE_H_CURRENCY_STATUS));
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			currencyMasterVO.setUpdateBy(userBean.getUserName());
			Object message = this.currencyService.updateCurrency(currencyMasterVO);

			try {
				Integer.parseInt(message.toString());
				modelAndView.addObject("success", true);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception because ISIS failure" + var8);
				modelAndView.addObject("success", message.toString());
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("CURRENCY_MASTER");
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		this.logger.info("currency \"" + currencyMasterVO.getCurrency() + "\" is successfully updated");
		return modelAndView;
	}

	public ModelAndView checkAssociatedCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In associatedCurrency");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String currencyCodes = request.getParameter(this.ACTION);
			CurrencyMasterVO cmvo = this.currencyService.associatedCurrency(currencyCodes, request);
			if (!cmvo.getAcCount().equalsIgnoreCase("0") || !cmvo.getTocptownCount().equalsIgnoreCase("0")
					|| !cmvo.getVendorCount().equalsIgnoreCase("0") || !cmvo.getRecCaseCount().equalsIgnoreCase("0")) {
				modelAndView.addObject("isAssociated", true);
				modelAndView.addObject("vo", cmvo);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}
}