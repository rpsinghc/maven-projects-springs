package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.interfaces.ICurrencyConversion;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CurrencyConversionMultiActionController extends MultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.CurrencyConversionMultiActionController";
	private ILogProducer logger;
	private static final String START_DATE = "startDate";
	private static final String UPDATE = "currency_lst";
	private static final String SUCCESS = "success";
	private static final String error = "error";
	ICurrencyConversion currencyConversionService;

	public CurrencyConversionMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.currencyConversionService = null;
	}

	public void setCurrencyConversionService(ICurrencyConversion currencyConversionService) {
		this.currencyConversionService = currencyConversionService;
	}
}