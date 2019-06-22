package com.worldcheck.atlas.dao.currencyconverter;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.worldcheck.atlas.vo.currencyconverter.CurrencyConverterVO;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;

public class CurrencyConverterDAOImpl implements ICurrencyConverterDAO {
	private static SqlMapClient sqlMap = null;
	CurrencyConverterVO currencyConverterVO = null;

	public CurrencyConverterDAOImpl() throws Exception {
		Reader reader = null;

		try {
			reader = Resources.getResourceAsReader("ibatis/config.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (Exception var3) {
			;
		}

	}

	public void addDataForCurrencyConvertor(CurrencyConverterVO currencyConverterVO) {
	}

	public void updateDataForCurrencyConvertor(CurrencyConverterVO currencyConverterVO) {
	}

	public ArrayList selectDataForCurrencyConverter(Date FromDate, Date toDate) {
		return null;
	}

	public ArrayList selectDataForCurrencyConverter(Date date) {
		return null;
	}
}