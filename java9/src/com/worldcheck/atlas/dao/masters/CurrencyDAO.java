package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CurrencyDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.CurrencyDAO");
	private String addNewCurrency = "CurrencyMaster.addNewCurrency";
	private String searchCurrency = "CurrencyMaster.searchCurrency";
	private String searchCurrencyCount = "CurrencyMaster.searchCurrencyCount";
	private String selectAll = "CurrencyMaster.selectAll";
	private String selectAllRates = "CurrencyMaster.export_ToXls";
	private String activeCurrCodes = "CurrencyMaster.activatedCurrency";
	private String updateCurrency = "CurrencyMaster.updateCurrency";
	private String deactivateCurrency = "CurrencyMaster.deactivateCurrency";
	private String activateCurrency = "CurrencyMaster.activateCurrency";
	private String searchExistCurrencyShortForm = "CurrencyMaster.searchExistCurrencyShortForm";
	private String searchExistCurrencyCode = "CurrencyMaster.searchExistCurrencyCode";
	private String searchExistCurrency = "CurrencyMaster.searchExistCurrency";
	private String seacrhCurrencyByCode = "CurrencyMaster.seacrhCurrencyByCode";
	private String associatedCurrency = "CurrencyMaster.associatedCurrency";

	public Object addNewCurrency(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("com.worldcheck.atlas.dao.masters.CurrencyDAO.addNewCurrency(CurrencyMasterVO)");

		try {
			this.insert(this.addNewCurrency, currencyMasterVO);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return 1;
	}

	public List<CurrencyMasterVO> searchCurrencyByName(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In searchCurrencyByName method");
		new ArrayList();

		try {
			List<CurrencyMasterVO> searchList = this.queryForList(this.searchCurrency, currencyMasterVO);
			return searchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchCurrencyByNameCount(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In searchCurrencyByNameCount method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.searchCurrencyCount, currencyMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CurrencyMasterVO> selectAll() throws CMSException {
		this.logger.debug("In selectAll method");
		new ArrayList();

		try {
			List<CurrencyMasterVO> selectList = this.queryForList(this.selectAll);
			return selectList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateCurrency(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In updateCurrency method");
		boolean var2 = false;

		try {
			int updated = this.update(this.updateCurrency, currencyMasterVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deactivateCurrency(HashMap inputHashMap) throws CMSException {
		this.logger.debug("In deactivateCurrency method");
		boolean var2 = false;

		try {
			int updated = this.update(this.deactivateCurrency, inputHashMap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int activateCurrency(HashMap inputHashMap) throws CMSException {
		this.logger.debug("In activateCurrency method");
		boolean var2 = false;

		try {
			int updated = this.update(this.activateCurrency, inputHashMap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountExistCurrency(String currency) throws CMSException {
		this.logger.debug("In getCountExistCurrency method");
		boolean var2 = false;

		try {
			int currencyCount = Integer.parseInt(this.queryForObject(this.searchExistCurrency, currency).toString());
			return currencyCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountExistCurrencyCode(String currencyCode) throws CMSException {
		this.logger.debug("In getCountExistCurrencyShortFormmethod");
		boolean var2 = false;

		try {
			int currencyCodeCount = Integer
					.parseInt(this.queryForObject(this.searchExistCurrencyCode, currencyCode).toString());
			return currencyCodeCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public CurrencyMasterVO seacrhCurrencyByCode(String currencyCode) throws CMSException {
		this.logger.debug("In seacrhCurrencyByCode method");
		CurrencyMasterVO cmvo = null;

		try {
			cmvo = (CurrencyMasterVO) this.queryForObject(this.seacrhCurrencyByCode, currencyCode);
			return cmvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public CurrencyMasterVO associatedCurrency(CurrencyMasterVO cmvoIn) throws CMSException {
		this.logger.debug("In associatedCurrency");
		CurrencyMasterVO cmvoOut = null;

		try {
			cmvoOut = (CurrencyMasterVO) this.queryForObject(this.associatedCurrency, cmvoIn);
			return cmvoOut;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<LinkedHashMap<String, String>> getAllRates(String activeCurrencyCodes) throws CMSException {
		this.logger.debug("In getAllRates method");
		new ArrayList();

		try {
			List<LinkedHashMap<String, String>> resultSetObj = this.queryForList(this.selectAllRates,
					activeCurrencyCodes);
			return resultSetObj;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}