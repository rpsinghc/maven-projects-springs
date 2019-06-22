package com.worldcheck.atlas.dao.junointerface;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.worldcheck.atlas.vo.currencyconverter.CurrencyConverterVO;
import com.worldcheck.atlas.vo.junointerface.CRNInfoVO;
import java.io.Reader;

public class CRNInfoDAO {
	private static SqlMapClient sqlMap = null;
	CurrencyConverterVO currencyConverterVO = null;

	public CRNInfoDAO() throws Exception {
		Reader reader = null;

		try {
			reader = Resources.getResourceAsReader("ibatis/config.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		} catch (Exception var3) {
			;
		}

	}

	public CRNInfoVO getCRNInfo(String crn) {
		CRNInfoVO crnInfoVO = null;
		return (CRNInfoVO) crnInfoVO;
	}
}