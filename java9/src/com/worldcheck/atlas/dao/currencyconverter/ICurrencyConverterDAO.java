package com.worldcheck.atlas.dao.currencyconverter;

import com.worldcheck.atlas.vo.currencyconverter.CurrencyConverterVO;
import java.util.ArrayList;
import java.util.Date;

public interface ICurrencyConverterDAO {
	ArrayList selectDataForCurrencyConverter(Date var1, Date var2);

	ArrayList selectDataForCurrencyConverter(Date var1);

	void updateDataForCurrencyConvertor(CurrencyConverterVO var1);

	void addDataForCurrencyConvertor(CurrencyConverterVO var1);
}