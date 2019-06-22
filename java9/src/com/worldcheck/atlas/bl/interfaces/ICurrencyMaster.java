package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.CurrencyDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface ICurrencyMaster {
	void setCurrencyDAO(CurrencyDAO var1);

	Object addCurrency(CurrencyMasterVO var1) throws CMSException;

	List<CurrencyMasterVO> searchCurrencyByName(CurrencyMasterVO var1, int var2, int var3) throws CMSException;

	int searchCurrencyByNameCount(CurrencyMasterVO var1) throws CMSException;

	List<CurrencyMasterVO> selectAll() throws CMSException;

	Object updateCurrency(CurrencyMasterVO var1) throws CMSException;

	Object deactivateCurrency(String var1, HttpServletRequest var2) throws CMSException;

	Object activateCurrency(String var1, HttpServletRequest var2) throws CMSException;

	boolean isExistCurrency(String var1) throws CMSException;

	boolean isExistCurrencyCode(String var1) throws CMSException;

	CurrencyMasterVO searchCurrencyByCode(CurrencyMasterVO var1) throws CMSException;

	CurrencyMasterVO associatedCurrency(String var1, HttpServletRequest var2) throws CMSException;

	List<LinkedHashMap<String, String>> getAllRates(String var1) throws CMSException;
}