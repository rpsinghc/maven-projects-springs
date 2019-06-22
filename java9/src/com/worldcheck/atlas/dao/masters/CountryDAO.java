package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CountryDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.CountryDAO");
	private String UPDATED_BY = "updatedBy";
	private String SUCCESS = "Successful";
	private String COUNTRY_CODE_LIST = "countryCodeList";
	private String COUNTRY_STATUS = "countryStatus";
	private String TOTAL_COUNTRIES = "Country_Master.getGridListCount";
	private String ADD_COUNTRY = "Country_Master.addCountry";
	private String SEARCH_COUNTRYCODE = "Country_Master.searchExistCountryCode";
	private String SEARCH_COUNTRY_NAME = "Country_Master.searchExistCountry";
	private String UPDATE_COUNTRY = "Country_Master.updateCountry";
	private String GET_COUNTRY_INFO = "Country_Master.getCountryInfo";
	private String CHANGE_STATUS = "Country_Master.deActivate";
	private String SEARCH_COUNTRY = "Country_Master.getGridList";
	private String COUNTRY_MASTER_EXPORT_TO_EXCEL = "Country_Master.export_XLS";

	public List<CountryMasterVO> getCountryGrid(CountryMasterVO countryMasterVO) throws CMSException {
		try {
			this.logger.debug("country:" + countryMasterVO.getCountry() + "RiskLevel:" + countryMasterVO.getRiskLevel()
					+ "Region:" + countryMasterVO.getRegion() + "CountryStatus :" + countryMasterVO.getCountryStatus()
					+ "StartLimit:" + countryMasterVO.getStart() + "EndLimit:" + countryMasterVO.getLimit()
					+ "sortColumn Name :" + countryMasterVO.getSortColumnName() + "SortType :"
					+ countryMasterVO.getSortType());
			new ArrayList();
			this.logger.debug(">>>>>>>>>" + countryMasterVO.getCountry() + countryMasterVO.getCountryStatus());
			List<CountryMasterVO> countryGridList = this.queryForList(this.SEARCH_COUNTRY, countryMasterVO);
			this.logger.debug("countryGridList size :" + countryGridList.size());
			return countryGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int changeStatus(int countryId, String countryStatus, String updatedBy) throws CMSException {
		HashMap paramMap = new HashMap();

		try {
			this.logger.debug("Inside the Dao change country status. CountryId:" + countryId + " Country Status:"
					+ countryStatus);
			paramMap.put("countryId", countryId);
			paramMap.put(this.COUNTRY_STATUS, countryStatus);
			paramMap.put(this.UPDATED_BY, updatedBy);
			int count = this.update(this.CHANGE_STATUS, paramMap);
			this.logger.debug("sucessfully change country status. Updated row:" + count);
			return count;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public CountryMasterVO getCountryInfo(int countryId) throws CMSException {
		try {
			this.logger.debug("get Country Info  from CountryDAO");
			CountryMasterVO country = (CountryMasterVO) this.queryForObject(this.GET_COUNTRY_INFO, countryId);
			return country;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateCountry(CountryMasterVO countryMasterVO) throws CMSException {
		try {
			this.logger.debug("Update Country.... from CountryDAO");
			int count = this.update(this.UPDATE_COUNTRY, countryMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int addCountry(CountryMasterVO countryMasterVO) throws CMSException {
		try {
			this.logger.debug("Insert new Country.... from CountryDAO");
			int rowId = (Integer) this.insert(this.ADD_COUNTRY, countryMasterVO);
			return rowId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountExistCountryCode(String countryCode) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject(this.SEARCH_COUNTRYCODE, countryCode).toString());
			this.logger.debug("Country Code Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountExistCountry(String country) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject(this.SEARCH_COUNTRY_NAME, country).toString());
			this.logger.debug("Country Name Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountryGridCount(CountryMasterVO countryMasterVO) throws CMSException {
		this.logger.debug("count>>>>>>>>>>>>>>>>>>>>>>inside dao:" + countryMasterVO.getStart());

		try {
			return (Integer) this.queryForObject(this.TOTAL_COUNTRIES, countryMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public CountryMasterVO checkAssociatedMaster(int countryMasterId) throws CMSException {
		this.logger.debug("count>>>>>>>>>>>>>>>>>>>>>>inside CountryDAO countryID:" + countryMasterId);

		try {
			return (CountryMasterVO) this.queryForObject("Country_Master.checkAssociatedMaster", countryMasterId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CountryMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList(this.COUNTRY_MASTER_EXPORT_TO_EXCEL, excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}