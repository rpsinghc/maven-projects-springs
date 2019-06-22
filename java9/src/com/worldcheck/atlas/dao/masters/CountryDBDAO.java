package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONValue;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CountryDBDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.CountryDBDAO");
	private String COUNTRY_MASTER_ID = "countryDBMasterId";
	private String COUNTRY = "country";
	private String LBR_TYPE = "lbrType";
	private String UPDATED_BY = "updatedBy";
	private String DOUBLE_HASH_AMPERSAND = "##&&";
	private String LBR_DESC = "lbrDescription";
	private String LBR_DBNAME = "lbrDBName";
	private String POSITION = "position";
	private String MASTER_INSERT_SQL_ID = "CountryDB.insertInMaster";
	private String OPTION_INSERT_SQL_ID = "CountryDB.insertInOptions";
	private String LBR_INSERT_SQL_ID = "CountryDB.insertInLBR";
	private String SEARCH_CDB_SQL_ID = "CountryDB.searchCDBCountryDatabase";
	private String SEARCH_LBR_SQL_ID = "CountryDB.searchLBRCountryDatabase";
	private String LBR_EXPORT_SQL_ID = "CountryDB.exportToExcelLBR";
	private String SOURCE_AVAILABILITY_SQL_ID = "CountryDB.sourceAvailability";
	private String GET_CDB_COUNTRYDB_SQL_ID = "CountryDB.getCDBCountryDB";
	private String GET_LBR_COUNTRYDB_SQL_ID = "CountryDB.getLBRCountryDB";
	private String COUNTRYDB_UNIQUE_SQL_ID = "CountryDB.isCountryDBUnique";
	private String GET_LBR_DB_SQL_ID = "CountryDB.getLBRDatabases";
	private String LBR_DB_UNIQUE_SQL_ID = "CountryDB.isCountryLBRDBUnique";
	private String SEARCH_LBR_COUNT_SQL_ID = "CountryDB.searchLBRCountryDatabaseCount";
	private String SEARCH_CDB_COUNT_SQL_ID = "CountryDB.searchCDBCountryDatabaseCount";
	private String DELETE_SQL_ID = "CountryDB.deleteCountryDatabase";

	public int addNewCountryDB(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		this.logger.debug("in addNewCountryDB");

		try {
			int masterId = (Integer) this.insert(this.MASTER_INSERT_SQL_ID, countryDatabaseMasterVO);
			this.logger.debug("added record in country db master : " + masterId);
			countryDatabaseMasterVO.setCountryDBMasterId(masterId);
			this.insert(this.OPTION_INSERT_SQL_ID, countryDatabaseMasterVO);
			this.logger.debug("options added");
			String[] companyRecords = countryDatabaseMasterVO.getCompanyLbr();
			String dbName;
			String description;
			if (null != companyRecords && companyRecords.length > 0) {
				for (int i = 0; i < companyRecords.length; ++i) {
					HashMap<Object, Object> paramMap = new HashMap();
					String dbName = "";
					dbName = "";
					description = companyRecords[i];
					Map jsonObject = (Map) JSONValue.parse(description);
					this.logger.debug("json object " + jsonObject);
					dbName = (String) jsonObject.get(this.LBR_DBNAME);
					dbName = dbName.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					dbName = (String) jsonObject.get(this.LBR_DESC);
					dbName = dbName.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					paramMap.put(this.COUNTRY_MASTER_ID, countryDatabaseMasterVO.getCountryDBMasterId());
					paramMap.put(this.UPDATED_BY, countryDatabaseMasterVO.getUpdatedBy());
					paramMap.put(this.LBR_TYPE, 1);
					paramMap.put(this.COUNTRY, countryDatabaseMasterVO.getCountry());
					paramMap.put(this.LBR_DBNAME, dbName);
					paramMap.put(this.LBR_DESC, dbName);
					paramMap.put(this.POSITION, (String) jsonObject.get(this.POSITION));
					this.insert(this.LBR_INSERT_SQL_ID, paramMap);
				}
			}

			String[] individualRecords = countryDatabaseMasterVO.getIndividualLbr();
			if (null != individualRecords && individualRecords.length > 0) {
				for (int i = 0; i < individualRecords.length; ++i) {
					HashMap<Object, Object> paramMap = new HashMap();
					dbName = "";
					description = "";
					String JSONstring = individualRecords[i];
					Map jsonObject = (Map) JSONValue.parse(JSONstring);
					this.logger.debug("json object " + jsonObject);
					dbName = (String) jsonObject.get(this.LBR_DBNAME);
					dbName = dbName.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					description = (String) jsonObject.get(this.LBR_DESC);
					description = description.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					paramMap.put(this.COUNTRY_MASTER_ID, countryDatabaseMasterVO.getCountryDBMasterId());
					paramMap.put(this.UPDATED_BY, countryDatabaseMasterVO.getUpdatedBy());
					paramMap.put(this.LBR_TYPE, 2);
					paramMap.put(this.COUNTRY, countryDatabaseMasterVO.getCountry());
					paramMap.put(this.LBR_DBNAME, dbName);
					paramMap.put(this.LBR_DESC, description);
					paramMap.put(this.POSITION, (String) jsonObject.get(this.POSITION));
					this.insert(this.LBR_INSERT_SQL_ID, paramMap);
				}
			}

			return masterId;
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public int updateCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		try {
			int count = false;
			this.logger.debug("in updateCountryDatabase");
			this.update("CountryDB.updateInMaster", countryDatabaseMasterVO);
			this.logger.debug("updated record in country db master ");
			int count = this.update("CountryDB.updateInOptions", countryDatabaseMasterVO);
			this.logger.debug("options updated..deleting existing LBR entries.");
			this.delete("CountryDB.deleteFromLBR", countryDatabaseMasterVO);
			this.logger.debug("deleted from LBR..now going checking for new records to be inserted");
			String[] companyRecords = countryDatabaseMasterVO.getCompanyLbr();
			String dbName;
			String description;
			if (null != companyRecords && companyRecords.length > 0) {
				for (int i = 0; i < companyRecords.length; ++i) {
					HashMap<Object, Object> paramMap = new HashMap();
					String dbName = "";
					dbName = "";
					description = companyRecords[i];
					Map jsonObject = (Map) JSONValue.parse(description);
					this.logger.debug("json object " + jsonObject);
					dbName = (String) jsonObject.get(this.LBR_DBNAME);
					dbName = dbName.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					dbName = (String) jsonObject.get(this.LBR_DESC);
					dbName = dbName.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					paramMap.put(this.COUNTRY_MASTER_ID, countryDatabaseMasterVO.getCountryDBMasterId());
					paramMap.put(this.UPDATED_BY, countryDatabaseMasterVO.getUpdatedBy());
					paramMap.put(this.LBR_TYPE, 1);
					paramMap.put(this.COUNTRY, countryDatabaseMasterVO.getCountry());
					paramMap.put(this.LBR_DBNAME, dbName);
					paramMap.put(this.LBR_DESC, dbName);
					paramMap.put(this.POSITION, (String) jsonObject.get(this.POSITION));
					this.insert(this.LBR_INSERT_SQL_ID, paramMap);
				}
			}

			String[] individualRecords = countryDatabaseMasterVO.getIndividualLbr();
			if (null != individualRecords && individualRecords.length > 0) {
				for (int i = 0; i < individualRecords.length; ++i) {
					HashMap<Object, Object> paramMap = new HashMap();
					dbName = "";
					description = "";
					String JSONstring = individualRecords[i];
					Map jsonObject = (Map) JSONValue.parse(JSONstring);
					this.logger.debug("json object " + jsonObject);
					dbName = (String) jsonObject.get(this.LBR_DBNAME);
					dbName = dbName.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					description = (String) jsonObject.get(this.LBR_DESC);
					description = description.replaceAll(this.DOUBLE_HASH_AMPERSAND, "\"");
					paramMap.put(this.COUNTRY_MASTER_ID, countryDatabaseMasterVO.getCountryDBMasterId());
					paramMap.put(this.UPDATED_BY, countryDatabaseMasterVO.getUpdatedBy());
					paramMap.put(this.LBR_TYPE, 2);
					paramMap.put(this.COUNTRY, countryDatabaseMasterVO.getCountry());
					paramMap.put(this.LBR_DBNAME, dbName);
					paramMap.put(this.LBR_DESC, description);
					paramMap.put(this.POSITION, (String) jsonObject.get(this.POSITION));
					this.insert(this.LBR_INSERT_SQL_ID, paramMap);
				}
			}

			return count;
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public int deleteCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		try {
			return this.delete(this.DELETE_SQL_ID, countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryDatabaseMasterVO> searchCDBCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		try {
			return this.queryForList(this.SEARCH_CDB_SQL_ID, countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int searchCDBCountryDatabaseCount(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		try {
			return (Integer) this.queryForObject(this.SEARCH_CDB_COUNT_SQL_ID, countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryDatabaseMasterVO> searchLBRCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		try {
			return this.queryForList(this.SEARCH_LBR_SQL_ID, countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int searchLBRCountryDatabaseCount(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		try {
			return (Integer) this.queryForObject(this.SEARCH_LBR_COUNT_SQL_ID, countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryDatabaseMasterVO> exportToExcelCDB(Map<String, Object> excelParamMap) throws CMSException {
		try {
			return this.queryForList("CountryDB.exportToExcelCDB", excelParamMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryDatabaseMasterVO> exportToExcelLBR(Map<String, Object> excelParamMap) throws CMSException {
		try {
			return this.queryForList(this.LBR_EXPORT_SQL_ID, excelParamMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryDatabaseMasterVO> sourceAvailability() throws CMSException {
		try {
			return this.queryForList(this.SOURCE_AVAILABILITY_SQL_ID);
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public CountryDatabaseMasterVO getCDBCountryDB(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		try {
			return (CountryDatabaseMasterVO) this.queryForObject(this.GET_CDB_COUNTRYDB_SQL_ID,
					countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public CountryDatabaseMasterVO getLBRCountryDB(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		try {
			return (CountryDatabaseMasterVO) this.queryForObject(this.GET_LBR_COUNTRYDB_SQL_ID,
					countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isCountryDBUnique(HashMap<String, Object> paramMap) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject(this.COUNTRYDB_UNIQUE_SQL_ID, paramMap).toString());
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (NumberFormatException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CountryDatabaseMasterVO> getLBRDatabases(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		try {
			return this.queryForList(this.GET_LBR_DB_SQL_ID, countryDatabaseMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isCountryLBRDBUnique(HashMap<String, Object> paramMap) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject(this.LBR_DB_UNIQUE_SQL_ID, paramMap).toString());
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (NumberFormatException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}