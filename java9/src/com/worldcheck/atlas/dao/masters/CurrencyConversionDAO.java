package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CurrencyConversionDAO extends SqlMapClientTemplate {
	private static final String NA = "NA";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.CurrencyConversionDAO");
	private String newRate = "CurrencyConversionMaster.newRate";
	private String updateRate = "CurrencyConversionMaster.updateRate";
	private String viewHistory = "CurrencyConversionMaster.viewHistory";
	private String selectAll = "CurrencyConversionMaster.selectAll";
	private String selectAllCount = "CurrencyConversionMaster.selectAllCount";
	private String ActiveCurrencyCount = "CurrencyConversionMaster.ActiveCurrencyCount";
	private String HistoryRatesCount = "CurrencyConversionMaster.viewHistoryCount";
	private String existForEffeciveDate = "CurrencyConversionMaster.currencyExistForEffectiveDate";
	private String AllCurrencyCount = "CurrencyConversionMaster.AllCurrencyCount";
	private String AllCurrency = "CurrencyConversionMaster.AllCurrency";
	private String AllActiveCurrency = "CurrencyConversionMaster.AllActiveCurrency";
	private String getLocalCurrencyValue = "CurrencyConversionMaster.getLocalCurrencyValue";

	public int addNewRate(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In addNewRate method");
		boolean var2 = false;

		try {
			int added = (Integer) ((Integer) this.insert(this.newRate, currencyMasterVO));
			return added;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRate(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In updateRate method");
		boolean var2 = false;

		try {
			int updated = this.update(this.updateRate, currencyMasterVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CurrencyMasterVO> viewHistory(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In viewHistory method");
		List historyList = null;

		try {
			historyList = this.queryForList(this.viewHistory, currencyMasterVO);
			return historyList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CurrencyMasterVO> getAllRates(HashMap hmap) throws CMSException {
		this.logger.debug("In getAllRates method");
		List allRates = null;

		try {
			allRates = this.queryForList(this.selectAll, hmap);
			return allRates;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getAllRatesCount() throws CMSException {
		this.logger.debug("In getAllRatesCount method");
		boolean var1 = false;

		try {
			int count = (Integer) this.queryForObject(this.selectAllCount);
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getAllActiveCurrencyCount() throws CMSException {
		this.logger.debug("In getAllActiveCurrencyCount method");
		boolean var1 = false;

		try {
			int count = (Integer) this.queryForObject(this.ActiveCurrencyCount);
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int currencyExistForEffectiveDate(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In currencyExistForEffectiveDate method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.existForEffeciveDate, currencyMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getHistoryRatesCount(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In getHistoryRatesCount method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.HistoryRatesCount, currencyMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getAllCurrencyCount() throws CMSException {
		this.logger.debug("In getAllCurrencyCount method");
		boolean var1 = false;

		try {
			int count = (Integer) this.queryForObject(this.AllCurrencyCount);
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CurrencyMasterVO> getAllCurrency() throws CMSException {
		this.logger.debug("In getAllCurrency method");
		List allCurrency = null;

		try {
			allCurrency = this.queryForList(this.AllCurrency);
			return allCurrency;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CurrencyMasterVO> getActiveCurrency() throws CMSException {
		this.logger.debug("In getActiveCurrency method");
		List allActiveCurrency = null;

		try {
			allActiveCurrency = this.queryForList(this.AllActiveCurrency);
			return allActiveCurrency;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getLocalCurrencyValue(HashMap hmap) throws CMSException {
		this.logger.debug("In getLocalCurrencyValue method");
		String exchangeRate = "NA";

		try {
			exchangeRate = (String) this.queryForObject(this.getLocalCurrencyValue, hmap);
			return exchangeRate;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}