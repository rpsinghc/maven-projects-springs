package com.worldcheck.atlas.dao.updateCTDataEntry;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.UpdateCTDataExcelVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class UpdateCTDataDAO extends SqlMapClientTemplate {
	private static final String UPDATECRNACCOUNTS = "UpdateCTData.updateCRNAccounts";
	private static final String ADDCRNACCOUNTS = "UpdateCTData.insertCRNAccounts";
	private static final String ISAVAILCRNINTOCAPETOWN = "UpdateCTData.isAvailCRNinToCapeTown";
	private static final String ISAVAILACTIVECURRENCYCODE = "UpdateCTData.isAvailActiveCurrencyCode";
	private static final String GET_CRN_DATA = "UpdateCTData.getCRNData";
	private static final String GET_CLIENTCASE_CRN = "UpdateCTData.getCRNListfromClientCase";
	private static final String GET_ALL_ACTIVE_CURRENCY_CODE = "UpdateCTData.getAllActiveCurrencyCode";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.updateCTDataEntry.UpdateCTDataDAO");

	public int updateCRNAccounts(UpdateCTDataExcelVO updateCTDataVO, Boolean update) throws CMSException {
		this.logger.debug("In UpdateCTDataEntryDAO : updateCRNAccount");
		int updated = 0;

		try {
			if (update) {
				updated = this.update("UpdateCTData.updateCRNAccounts", updateCTDataVO);
				this.logger.debug("updated crn in CTData :" + updateCTDataVO.getCrn());
			} else {
				this.insert("UpdateCTData.insertCRNAccounts", updateCTDataVO);
				this.logger.debug("Inserted new crn in CTData :" + updateCTDataVO.getCrn());
			}
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exit: UpdateCTDataEntryDAO : updateCRNAccount");
		return updated;
	}

	public int isAvailActiveCurrencyCode(String currencyCode) throws CMSException {
		this.logger.debug("In UpdateCTDataEntryDAO : isAvailActiveCurrencyCode");
		boolean var2 = false;

		try {
			HashMap<String, String> hmap = new HashMap();
			hmap.put("currencyCode", currencyCode);
			int count = (Integer) this.queryForObject("UpdateCTData.isAvailActiveCurrencyCode", hmap);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getCrnDataList(List<String> crnNumbers) throws CMSException {
		this.logger.debug("In getCrnDataList");
		new ArrayList();

		try {
			List<String> crnDataList = this.queryForList("UpdateCTData.getCRNData", crnNumbers);
			return crnDataList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getCRNListfromClientCase(List<String> crnNumbers) throws CMSException {
		this.logger.debug("In getCRNListfromClientCase");
		new ArrayList();

		try {
			List<String> crnDataList = this.queryForList("UpdateCTData.getCRNListfromClientCase", crnNumbers);
			return crnDataList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getAllActiveCurrencyCode() throws CMSException {
		this.logger.debug("In getAllActiveCurrencyCode");
		new ArrayList();

		try {
			List<String> currencyCodeList = this.queryForList("UpdateCTData.getAllActiveCurrencyCode");
			return currencyCodeList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}