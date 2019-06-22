package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.ICurrencyConversion;
import com.worldcheck.atlas.dao.masters.CurrencyConversionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class CurrencyConverisonManager implements ICurrencyConversion {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.masters.CurrencyConverisonManager");
	private static final String ONE = "1.0000";
	private static final String NA = "NA";
	private static final String USD = "USD";
	private static final String INR = "INR";
	private static final String SGD = "SGD";
	private static final String AED = "AED";
	private static final String HKD = "HKD";
	private static final String GBP = "GBP";
	private static final String RMB = "RMB";
	private static final String EUR = "EUR";
	private static final String updateBy = "updateBy";
	private static final String startDate = "startDate";
	private static final String updatedOn = "updatedOn";
	private static final String conversionRateKey = "conversionRateKey";
	private static final String STARTDATE = "startDate";
	private static final String OPERATION = "operation";
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	private static final String FULL_FORMAT_DATE = "dd-MM-yy hh:mm:ss";
	private String currencyCode = "currencyCode";
	private String effectiveDate = "effectiveDate";
	CurrencyConversionDAO currencyConversionDAO = null;

	public void setCurrencyConversionDAO(CurrencyConversionDAO currencyConversionDAO) {
		this.currencyConversionDAO = currencyConversionDAO;
	}

	public void addNewRate(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In addNewRate method");
		String exchangeRate = "currencyConversionDAO.get"
				+ currencyMasterVO.getCurrencyCode().substring(0, 1).toUpperCase()
				+ currencyMasterVO.getCurrencyCode().substring(1).toLowerCase() + "()";
		currencyMasterVO.setExchangeRate(exchangeRate);
		this.currencyConversionDAO.addNewRate(currencyMasterVO);
	}

	public void updateRate(CurrencyMasterVO currencyMasterVo, HttpServletRequest request) throws CMSException {
		this.logger.debug("In updateRate method");
		Enumeration requestParams = request.getParameterNames();

		while (true) {
			while (true) {
				String requestParmItem;
				do {
					do {
						if (!requestParams.hasMoreElements()) {
							return;
						}

						requestParmItem = requestParams.nextElement().toString();
					} while (requestParmItem.equalsIgnoreCase("startDate"));
				} while (requestParmItem.equalsIgnoreCase("operation"));

				currencyMasterVo.setCurrencyCode(requestParmItem);
				currencyMasterVo.setExchangeRate(request.getParameter(requestParmItem));
				if (request.getParameter("operation").equalsIgnoreCase("insert")) {
					this.currencyConversionDAO.addNewRate(currencyMasterVo);
				} else if (request.getParameter("operation").equalsIgnoreCase("update")) {
					if (this.currencyConversionDAO.currencyExistForEffectiveDate(currencyMasterVo) == 0
							&& !currencyMasterVo.getExchangeRate().equalsIgnoreCase("NA")) {
						this.currencyConversionDAO.addNewRate(currencyMasterVo);
					} else if (!currencyMasterVo.getExchangeRate().equalsIgnoreCase("NA")) {
						this.currencyConversionDAO.updateRate(currencyMasterVo);
					}
				}
			}
		}
	}

	public List<HashMap<String, HashMap<String, String>>> getAllRate(int start, int limit, String sortColumnName,
			String sortType) throws CMSException {
		this.logger.debug("In getAllRatemethod");
		SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		HashMap<String, String> hmapValue = null;
		HashMap<String, HashMap<String, String>> hmapList = null;
		List<HashMap<String, HashMap<String, String>>> finalList = new ArrayList();
		int activeCurrencycount = this.currencyConversionDAO.getAllActiveCurrencyCount();
		HashMap<String, String> hmap = new HashMap();
		int newStart = start * activeCurrencycount + 1;
		int newLimit = limit * activeCurrencycount + start * activeCurrencycount;
		hmap.put("start", String.valueOf(newStart));
		hmap.put("limit", String.valueOf(newLimit));
		hmap.put("sort", sortColumnName);
		hmap.put("dir", sortType);
		this.logger.debug("SortColumn: " + sortColumnName + " sortType:" + sortType);
		List<CurrencyMasterVO> inputVOList = this.currencyConversionDAO.getAllRates(hmap);
		Iterator itr = inputVOList.iterator();

		while (itr.hasNext()) {
			CurrencyMasterVO itrVO = (CurrencyMasterVO) itr.next();
			List<CurrencyMasterVO> innerLst = itrVO.getCurrencyConversionList();
			hmapValue = new HashMap();
			Iterator inItr = innerLst.iterator();

			while (inItr.hasNext()) {
				hmapList = new HashMap();
				CurrencyMasterVO inItrVO = (CurrencyMasterVO) inItr.next();
				if (inItrVO.getCurrencyCode().equalsIgnoreCase("USD")) {
					hmapValue.put(inItrVO.getCurrencyCode(), "1.0000");
				} else {
					hmapValue.put(inItrVO.getCurrencyCode(), inItrVO.getExchangeRate());
				}

				hmapValue.put("updateBy", inItrVO.getUpdateBy());
				hmapValue.put("startDate", inItrVO.getStartDate());
				if (!inItrVO.getUpdatedOn().equalsIgnoreCase("NA")) {
					try {
						hmapValue.put("updatedOn", sdfOut.format(sdfIn.parse(inItrVO.getUpdatedOn())));
					} catch (ParseException var21) {
						throw new CMSException(this.logger, var21);
					}
				} else {
					hmapValue.put("updatedOn", inItrVO.getUpdatedOn());
				}
			}

			hmapList.put("conversionRateKey", hmapValue);
			finalList.add(hmapList);
		}

		return finalList;
	}

	public List<HashMap<String, HashMap<String, String>>> viewHistory(CurrencyMasterVO currencyMasterVO, int start,
			int limit, String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("In viewHistory method");
		new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		HashMap<String, String> hmapValue = null;
		HashMap<String, HashMap<String, String>> hmapList = null;
		List<HashMap<String, HashMap<String, String>>> finalList = new ArrayList();
		int allCurrencycount = this.currencyConversionDAO.getAllCurrencyCount();
		int newStart = start * allCurrencycount + 1;
		int newLimit = limit * allCurrencycount + start * allCurrencycount;
		currencyMasterVO.setStart(new Integer(newStart));
		currencyMasterVO.setLimit(new Integer(newLimit));
		currencyMasterVO.setSortColumnName(sortColumnName);
		currencyMasterVO.setSortType(sortType);
		List<CurrencyMasterVO> inputVOList = this.currencyConversionDAO.viewHistory(currencyMasterVO);
		Iterator itr = inputVOList.iterator();

		while (itr.hasNext()) {
			CurrencyMasterVO itrVO = (CurrencyMasterVO) itr.next();
			List<CurrencyMasterVO> innerLst = itrVO.getCurrencyConversionList();
			hmapValue = new HashMap();
			Iterator inItr = innerLst.iterator();

			while (inItr.hasNext()) {
				hmapList = new HashMap();
				CurrencyMasterVO inItrVO = (CurrencyMasterVO) inItr.next();
				if (inItrVO.getCurrencyCode().equalsIgnoreCase("USD")) {
					hmapValue.put(inItrVO.getCurrencyCode(), "1.0000");
				} else {
					hmapValue.put(inItrVO.getCurrencyCode(), inItrVO.getExchangeRate());
				}

				if (hmapValue.get("updateBy") == null) {
					hmapValue.put("updateBy", inItrVO.getUpdateBy());
				}

				if (hmapValue.get("updateBy") != null && ((String) hmapValue.get("updateBy")).equalsIgnoreCase("NA")) {
					hmapValue.put("updateBy", inItrVO.getUpdateBy());
				}

				hmapValue.put("startDate", inItrVO.getStartDate());
				hmapValue.put("updatedOn", inItrVO.getUpdatedOn());
			}

			hmapList.put("conversionRateKey", hmapValue);
			finalList.add(hmapList);
		}

		return finalList;
	}

	public int totalRateListCount() throws CMSException {
		this.logger.debug("In totalRateListCount method");
		boolean var1 = false;

		int count;
		try {
			count = this.currencyConversionDAO.getAllRatesCount()
					/ this.currencyConversionDAO.getAllActiveCurrencyCount();
		} catch (ArithmeticException var3) {
			this.logger.debug("exception in totalRateListCount method:" + var3);
			count = 0;
		}

		return count;
	}

	public int totalHistoryRateListCount(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In totalHistoryRateListCount method");
		boolean var2 = false;

		int count;
		try {
			this.logger.debug(
					"totalHistoryRateListCount>>" + this.currencyConversionDAO.getHistoryRatesCount(currencyMasterVO)
							+ "::" + this.currencyConversionDAO.getAllCurrencyCount());
			count = this.currencyConversionDAO.getHistoryRatesCount(currencyMasterVO)
					/ this.currencyConversionDAO.getAllCurrencyCount();
		} catch (ArithmeticException var4) {
			this.logger.debug("exception in totalHistoryRateListCount method:" + var4);
			count = 0;
		}

		return count;
	}

	public List<CurrencyMasterVO> getAllCurrency() throws CMSException {
		this.logger.debug("In getAllCurrency method");
		return this.currencyConversionDAO.getAllCurrency();
	}

	public List<CurrencyMasterVO> getActiveCurrency() throws CMSException {
		this.logger.debug("In getActiveCurrency method");
		return this.currencyConversionDAO.getActiveCurrency();
	}

	public String getLocalCurrencyValue(Date effectiveDate, String currencyCode) throws CMSException {
		this.logger.debug("In getLocalCurrencyValue method");
		String exchangeRate = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			String startDate = sdf.format(effectiveDate);
			HashMap<String, String> hmap = new HashMap();
			hmap.put(this.currencyCode, currencyCode);
			hmap.put(this.effectiveDate, startDate);
			exchangeRate = this.currencyConversionDAO.getLocalCurrencyValue(hmap);
			return exchangeRate;
		} catch (CMSException var7) {
			throw var7;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}
}