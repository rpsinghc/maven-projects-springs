package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.CurrencyConversionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface ICurrencyConversion {
	void setCurrencyConversionDAO(CurrencyConversionDAO var1);

	void addNewRate(CurrencyMasterVO var1) throws CMSException;

	void updateRate(CurrencyMasterVO var1, HttpServletRequest var2) throws CMSException;

	List<HashMap<String, HashMap<String, String>>> getAllRate(int var1, int var2, String var3, String var4)
			throws CMSException;

	List<HashMap<String, HashMap<String, String>>> viewHistory(CurrencyMasterVO var1, int var2, int var3, String var4,
			String var5) throws CMSException;

	int totalRateListCount() throws CMSException;

	int totalHistoryRateListCount(CurrencyMasterVO var1) throws CMSException;

	List<CurrencyMasterVO> getAllCurrency() throws CMSException;

	List<CurrencyMasterVO> getActiveCurrency() throws CMSException;

	String getLocalCurrencyValue(Date var1, String var2) throws CMSException;
}