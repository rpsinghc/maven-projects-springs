package com.worldcheck.atlas.bl.masters;

import com.integrascreen.orders.UPMasterVO;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ICurrencyMaster;
import com.worldcheck.atlas.dao.masters.CurrencyDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class CurrencyManager implements ICurrencyMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.CurrencyManager");
	private static final String COMMA = ",";
	private final String currencyCode = "currencyCode";
	private static final String updateBy = "updateBy";
	private static final String CURRENCY = "Currency";
	private static final String INSERT = "Insert";
	private static final String ADD_ACTIVE = "A";
	private static final String UPDATE = "Update";
	private static final Object ZERO = "0";
	private static final String DEACTIVE = "D";
	private CurrencyDAO currencyDAO = null;

	public void setCurrencyDAO(CurrencyDAO currencyDAO) {
		this.currencyDAO = currencyDAO;
	}

	public Object addCurrency(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In addCurrency method");

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Currency");
			upMasterVO.setUpdateType("Insert");
			com.integrascreen.orders.CurrencyMasterVO masterObject = new com.integrascreen.orders.CurrencyMasterVO();
			masterObject.setCode(currencyMasterVO.getCurrencyCode());
			masterObject.setDescription(currencyMasterVO.getCurrency());
			masterObject.setStatus("A");
			upMasterVO.setCurencyMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.currencyDAO.addNewCurrency(currencyMasterVO)
					: isisResponseVO.getResponseVO().getMessage();
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CurrencyMasterVO> searchCurrencyByName(CurrencyMasterVO currencyMasterVO, int start, int limit)
			throws CMSException {
		this.logger.debug("IN searchCurrencyByNameMETHOD");
		currencyMasterVO.setStart(new Integer(start + 1));
		currencyMasterVO.setLimit(new Integer(start + limit));
		return this.currencyDAO.searchCurrencyByName(currencyMasterVO);
	}

	public int searchCurrencyByNameCount(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("IN searchCurrencyByNameCount method");
		return this.currencyDAO.searchCurrencyByNameCount(currencyMasterVO);
	}

	public List<CurrencyMasterVO> selectAll() throws CMSException {
		this.logger.debug("In selectAll method");
		return this.currencyDAO.selectAll();
	}

	public Object updateCurrency(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In updateCurrency method");

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Currency");
			upMasterVO.setUpdateType("Update");
			com.integrascreen.orders.CurrencyMasterVO masterObject = new com.integrascreen.orders.CurrencyMasterVO();
			masterObject.setCode(currencyMasterVO.getCurrencyCode());
			masterObject.setDescription(currencyMasterVO.getCurrency());
			masterObject.setStatus(currencyMasterVO.getCurrencyStatus().equals(ZERO) ? "D" : "A");
			upMasterVO.setCurencyMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.currencyDAO.updateCurrency(currencyMasterVO)
					: isisResponseVO.getResponseVO().getMessage();
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Object deactivateCurrency(String currencyCodes, HttpServletRequest request) throws CMSException {
		this.logger.debug("In deactivateCurrency method");
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		HashMap<String, String> inputHashMap = new HashMap();
		String[] currencyCodeArray = currencyCodes.split(",");
		Object returnValue = null;
		String[] arr$ = currencyCodeArray;
		int len$ = currencyCodeArray.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			String currencyCode = arr$[i$];
			this.getClass();
			inputHashMap.put("currencyCode", currencyCode);
			inputHashMap.put("updateBy", userBean.getUserName());

			try {
				UPMasterVO upMasterVO = new UPMasterVO();
				upMasterVO.setMaster("Currency");
				upMasterVO.setUpdateType("Update");
				com.integrascreen.orders.CurrencyMasterVO masterObject = new com.integrascreen.orders.CurrencyMasterVO();
				masterObject.setCode(currencyCode);
				masterObject.setDescription(this.currencyDAO.seacrhCurrencyByCode(currencyCode).getCurrency());
				masterObject.setStatus("D");
				upMasterVO.setCurencyMaster(masterObject);
				ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient()
						.updateMaster(upMasterVO);
				if (isisResponseVO.isSuccess()) {
					returnValue = this.currencyDAO.deactivateCurrency(inputHashMap);
				} else {
					returnValue = isisResponseVO.getResponseVO().getMessage();
				}
			} catch (CMSException var14) {
				throw new CMSException(this.logger, var14);
			}
		}

		return returnValue;
	}

	public Object activateCurrency(String currencyCodes, HttpServletRequest request) throws CMSException {
		this.logger.debug("IN activateCurrency METHOD");
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		HashMap<String, String> inputHashMap = new HashMap();
		String[] currencyCodeArray = currencyCodes.split(",");
		Object returnValue = null;
		String[] arr$ = currencyCodeArray;
		int len$ = currencyCodeArray.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			String currencyCode = arr$[i$];
			this.getClass();
			inputHashMap.put("currencyCode", currencyCode);
			inputHashMap.put("updateBy", userBean.getUserName());

			try {
				UPMasterVO upMasterVO = new UPMasterVO();
				upMasterVO.setMaster("Currency");
				upMasterVO.setUpdateType("Update");
				com.integrascreen.orders.CurrencyMasterVO masterObject = new com.integrascreen.orders.CurrencyMasterVO();
				masterObject.setCode(currencyCode);
				masterObject.setDescription(this.currencyDAO.seacrhCurrencyByCode(currencyCode).getCurrency());
				masterObject.setStatus("A");
				upMasterVO.setCurencyMaster(masterObject);
				ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient()
						.updateMaster(upMasterVO);
				if (isisResponseVO.isSuccess()) {
					returnValue = this.currencyDAO.activateCurrency(inputHashMap);
				} else {
					returnValue = isisResponseVO.getResponseVO().getMessage();
				}
			} catch (CMSException var14) {
				throw new CMSException(this.logger, var14);
			}
		}

		return returnValue;
	}

	public boolean isExistCurrency(String currency) throws CMSException {
		this.logger.debug("In isExistCurrency method");
		int currencyCount = this.currencyDAO.getCountExistCurrency(currency);
		return currencyCount > 0;
	}

	public boolean isExistCurrencyCode(String currencyCode) throws CMSException {
		this.logger.debug("In isExistCurrencyCode method");
		int currencyCodeCount = this.currencyDAO.getCountExistCurrencyCode(currencyCode);
		return currencyCodeCount > 0;
	}

	public CurrencyMasterVO searchCurrencyByCode(CurrencyMasterVO currencyMasterVO) throws CMSException {
		this.logger.debug("In searchCurrencyByCode method ");
		return this.currencyDAO.seacrhCurrencyByCode(currencyMasterVO.getCurrencyCode());
	}

	public CurrencyMasterVO associatedCurrency(String currencyCodes, HttpServletRequest request) throws CMSException {
		this.logger.debug("In associatedCurrency");
		CurrencyMasterVO cmvoOut = null;

		try {
			String[] currencyCodeArray = currencyCodes.split(",");
			String[] arr$ = currencyCodeArray;
			int len$ = currencyCodeArray.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				String currencyCode = arr$[i$];
				CurrencyMasterVO cmvoIn = new CurrencyMasterVO();
				cmvoIn.setCurrencyCode(currencyCode);
				cmvoOut = this.currencyDAO.associatedCurrency(cmvoIn);
			}

			return cmvoOut;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<LinkedHashMap<String, String>> getAllRates(String activeCurrencyCodes) throws CMSException {
		this.logger.debug("In getAllRates method");
		return this.currencyDAO.getAllRates(activeCurrencyCodes);
	}
}