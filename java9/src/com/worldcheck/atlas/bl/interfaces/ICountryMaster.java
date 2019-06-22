package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.CountryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.List;
import java.util.Map;

public interface ICountryMaster {
	void setCountryDAO(CountryDAO var1);

	List<CountryMasterVO> getCountryGrid(CountryMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	String changeStatus(int var1, String var2, String var3) throws CMSException;

	CountryMasterVO getCountryInfo(int var1) throws CMSException;

	String updateCountry(CountryMasterVO var1) throws CMSException;

	String addCountry(CountryMasterVO var1) throws CMSException;

	boolean isExistCountryCode(String var1) throws CMSException;

	boolean isExistCountry(String var1) throws CMSException;

	int getCountryGridCount(CountryMasterVO var1) throws CMSException;

	CountryMasterVO checkAssociatedMaster(int var1) throws CMSException;

	List<CountryMasterVO> getReport(Map<String, Object> var1) throws CMSException;
}