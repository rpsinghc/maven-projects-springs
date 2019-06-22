package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ICurrencyConversion;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCurrencyConversionMultiActionController extends JSONMultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.JSONCurrencyConversionMultiActionController";
	private ILogProducer logger;
	private static final String JSON_VIEW = "jsonView";
	private static final String SELECT_LIST = "selectList";
	private static final String HISTORY_LIST = "historyList";
	private static final String ALL_CURRENCY_HISTORY = "allCurrencyForHistory";
	private static final String BLANK = "";
	private static final String error = "error";
	private static final String START_DATE = "startDate";
	private static final String SUCCESS = "success";
	private static final String ALL_ACTIVE_CURRENCY_LIST = "currencyMasterList";
	ICurrencyConversion currencyConversionService;

	public JSONCurrencyConversionMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.currencyConversionService = null;
	}

	public void setCurrencyConversionService(ICurrencyConversion currencyConversionService) {
		this.currencyConversionService = currencyConversionService;
	}

	public ModelAndView getAllRates(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In getAllRates method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<HashMap<String, HashMap<String, String>>> selectList = this.currencyConversionService.getAllRate(
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			modelAndView.addObject("selectList", selectList);
			int count = this.currencyConversionService.totalRateListCount();
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView viewHistory(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In viewHistory method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		List<HashMap<String, HashMap<String, String>>> historyList = null;
		int count = 0;

		try {
			if ("".equalsIgnoreCase(currencyMasterVO.getStartDate())
					&& "".equalsIgnoreCase(currencyMasterVO.getEndDate())) {
				historyList = new ArrayList();
			} else {
				historyList = this.currencyConversionService.viewHistory(currencyMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				count = this.currencyConversionService.totalHistoryRateListCount(currencyMasterVO);
				modelAndView.addObject("total", count);
			}

			this.logger.debug("Total Record for view History:" + count);
			modelAndView.addObject("historyList", historyList);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getAllCurrencyForHistory(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In getAllCurrencyForHistory method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		List allCurrencyForHistoryList = null;

		try {
			allCurrencyForHistoryList = this.currencyConversionService.getAllCurrency();
			modelAndView.addObject("allCurrencyForHistory", allCurrencyForHistoryList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getActiveCurrencyMaster(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In getActiveCurrencyMaster method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		List allActiveCurrencyList = null;

		try {
			allActiveCurrencyList = this.currencyConversionService.getActiveCurrency();
			modelAndView.addObject("currencyMasterList", allActiveCurrencyList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView updateRate(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In updateRate method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			new SimpleDateFormat("dd-MM-yy hh:mm:ss");
			new SimpleDateFormat("dd-MMM-yyyy");
			currencyMasterVO.setStartDate(request.getParameter("startDate"));
			currencyMasterVO.setUpdateBy(userBean.getUserName());
			this.logger.info(currencyMasterVO.getStartDate());
			Enumeration e = request.getParameterNames();

			while (true) {
				if (!e.hasMoreElements()) {
					this.currencyConversionService.updateRate(currencyMasterVO, request);
					ResourceLocator.self().getCacheService().addToCacheRunTime("CURRENCY_MASTER");
					modelAndView.addObject("success", true);
					break;
				}

				this.logger.info(e.nextElement().toString());
			}
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		this.logger.info("currency rate is Added/Updated for date " + currencyMasterVO.getStartDate());
		return modelAndView;
	}
}