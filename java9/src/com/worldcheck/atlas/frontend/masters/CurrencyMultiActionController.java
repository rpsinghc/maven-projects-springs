package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.ICurrencyMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CurrencyMultiActionController extends MultiActionController {
	private static final String error = "error";
	private static final String hcurrencyStatus = "hcurrencyStatus";
	private static final String CURRENCY_LIST_redirect = "redirect:currency.do";
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.CurrencyMultiActionController";
	private String SUCCESS = "success";
	private String CURRENCY_LIST = "currencyList";
	private String CURRENCY_LIST_jsp = "currency_lst";
	private String CURRENCY_NEW_jsp = "currency_new";
	private String UPDATE_H_CURRENCY_STATUS = "updateHCurrencyStatus";
	private ILogProducer logger;
	private ICurrencyMaster currencyService;
	private final String fromSave;
	private String Currency_Master;
	private String date;

	public CurrencyMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.currencyService = null;
		this.fromSave = "fromSave";
		this.Currency_Master = "Currency_Conversion_Master";
		this.date = "Date";
	}

	public void setCurrencyService(ICurrencyMaster currencyService) {
		this.currencyService = currencyService;
	}

	public ModelAndView currency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In currency method");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.CURRENCY_LIST_jsp);
			HttpSession var10000 = request.getSession();
			this.getClass();
			if (var10000.getAttribute("fromSave") != null) {
				var10000 = request.getSession();
				this.getClass();
				String fromSave = var10000.getAttribute("fromSave").toString();
				this.getClass();
				modelAndView.addObject("fromSave", fromSave);
				var10000 = request.getSession();
				this.getClass();
				var10000.removeAttribute("fromSave");
			}

			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView currencyMasterReportExport(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In currencyMasterReportExport");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			this.logger.debug("excelParams::" + excelParams);
			List<LinkedHashMap<String, String>> currencyDataList = this.currencyService.getAllRates(excelParams);
			this.logger.debug("fetched currencyDataList>>Size is " + currencyDataList.size());
			Map<String, Object> resultMap = this.writeToExcel(currencyDataList, excelParams, response);
			modelandview = new ModelAndView("excelDownloadPopup");
			modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
			modelandview.addObject("fileName", resultMap.get("fileName"));
			this.logger.debug("exiting  Export to Excel currencyMasterController");
			return modelandview;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	private Map<String, Object> writeToExcel(List<LinkedHashMap<String, String>> currencyDataList, String excelParams,
			HttpServletResponse response) throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.date);
			String[] currencyCodes = excelParams.split(",");

			for (int i = 0; i < currencyCodes.length; ++i) {
				lstHeader.add(currencyCodes[i].replaceAll("'", " "));
			}

			return ExcelDownloader.writeToExcel(lstHeader, currencyDataList, this.Currency_Master, (short) 0, (short) 1,
					response, this.Currency_Master);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public ModelAndView newCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		ModelAndView modelAndView = null;
		this.logger.debug("InnewCurrency method");

		try {
			modelAndView = new ModelAndView(this.CURRENCY_NEW_jsp);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView addCurrency(HttpServletRequest request, HttpServletResponse response,
			CurrencyMasterVO currencyMasterVO) throws Exception {
		this.logger.debug("In addCurrency method");
		ModelAndView modelAndView = new ModelAndView("redirect:currency.do");

		try {
			currencyMasterVO.setCurrencyStatus(request.getParameter("hcurrencyStatus"));
			this.logger.info(currencyMasterVO.getCurrencyStatus());
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			currencyMasterVO.setUpdateBy(userBean.getUserName());
			Object message = this.currencyService.addCurrency(currencyMasterVO);

			HttpSession var10000;
			try {
				Integer.parseInt(message.toString());
				var10000 = request.getSession();
				this.getClass();
				var10000.setAttribute("fromSave", this.SUCCESS);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception because ISIS failure" + var8);
				var10000 = request.getSession();
				this.getClass();
				var10000.setAttribute("fromSave", message.toString());
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("CURRENCY_MASTER");
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}

		this.logger.info("Currency \"" + currencyMasterVO.getCurrency() + "\" is added successfully");
		return modelAndView;
	}
}