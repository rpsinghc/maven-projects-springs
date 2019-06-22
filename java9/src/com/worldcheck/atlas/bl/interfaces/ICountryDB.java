package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.CountryDBDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICountryDB {
	void setCountryDBDAO(CountryDBDAO var1);

	void addNewCountryDB(CountryDatabaseMasterVO var1) throws CMSException;

	void updateCountryDatabase(CountryDatabaseMasterVO var1) throws CMSException;

	void deleteCountryDatabase(CountryDatabaseMasterVO var1) throws CMSException;

	List<CountryDatabaseMasterVO> searchCDBCountryDatabase(CountryDatabaseMasterVO var1) throws CMSException;

	int searchCDBCountryDatabaseCount(CountryDatabaseMasterVO var1) throws CMSException;

	List<CountryDatabaseMasterVO> searchLBRCountryDatabase(CountryDatabaseMasterVO var1) throws CMSException;

	int searchLBRCountryDatabaseCount(CountryDatabaseMasterVO var1) throws CMSException;

	List<CountryDatabaseMasterVO> exportToExcelCDB(Map<String, Object> var1) throws CMSException;

	List<CountryDatabaseMasterVO> exportToExcelLBR(Map<String, Object> var1) throws CMSException;

	List<CountryDatabaseMasterVO> sourceAvailability() throws CMSException;

	CountryDatabaseMasterVO getCDBCountryDB(CountryDatabaseMasterVO var1) throws CMSException;

	CountryDatabaseMasterVO getLBRCountryDB(CountryDatabaseMasterVO var1) throws CMSException;

	boolean isCountryDBUnique(HashMap<String, Object> var1) throws CMSException;

	List<CountryDatabaseMasterVO> getLBRDatabases(CountryDatabaseMasterVO var1) throws CMSException;

	boolean isCountryLBRDBUnique(HashMap<String, Object> var1) throws CMSException;
}