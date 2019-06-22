package com.worldcheck.atlas.bl.currencyconverter;

import com.worldcheck.atlas.dao.currencyconverter.CurrencyConverterDAOImpl;
import com.worldcheck.atlas.vo.currencyconverter.CurrencyConverterVO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CurrencyConverterController extends MultiActionController {
	protected CurrencyConverterVO currencyConverterVO;
	protected CurrencyConverterDAOImpl currencyConverterDAOImpl;

	public ModelAndView getHistoricalCurrencyExchangeData(HttpServletRequest request, HttpServletResponse response) {
		Date fromDate = null;
		Date toDate = null;
		this.currencyConverterDAOImpl.selectDataForCurrencyConverter((Date) fromDate, (Date) toDate);
		return new ModelAndView("showmessage", "currencyConverterVO", this.currencyConverterVO);
	}

	public ModelAndView getCurrencyExchangeRateForDate(HttpServletRequest request, HttpServletResponse response) {
		Date date = null;
		this.currencyConverterDAOImpl.selectDataForCurrencyConverter((Date) date);
		return new ModelAndView("showmessage", "currencyConverterVO", this.currencyConverterVO);
	}

	public ModelAndView saveCurrencyExchangeDataForDate(HttpServletRequest request, HttpServletResponse response) {
		this.currencyConverterDAOImpl.addDataForCurrencyConvertor(this.currencyConverterVO);
		return new ModelAndView("showmessage", "currencyConverterVO", this.currencyConverterVO);
	}
}