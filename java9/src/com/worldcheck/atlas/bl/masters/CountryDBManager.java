package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.ICountryDB;
import com.worldcheck.atlas.dao.masters.CountryDBDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryDBManager implements ICountryDB {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.CountryDBManager");
	private CountryDBDAO countryDBDAO = null;

	public void setCountryDBDAO(CountryDBDAO countryDBDAO) {
		this.countryDBDAO = countryDBDAO;
	}

	public void addNewCountryDB(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		this.setOptionTabDefaultValues(countryDatabaseMasterVO);
		this.countryDBDAO.addNewCountryDB(countryDatabaseMasterVO);
	}

	private void setOptionTabDefaultValues(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		if (null == countryDatabaseMasterVO.getLocalLanguageMedia()
				|| countryDatabaseMasterVO.getLocalLanguageMedia().trim().length() == 0) {
			countryDatabaseMasterVO.setLocalLanguageMedia("No");
		}

		if (null == countryDatabaseMasterVO.getIncorporationDetail()
				|| countryDatabaseMasterVO.getIncorporationDetail().trim().length() == 0) {
			countryDatabaseMasterVO.setIncorporationDetail("No");
		}

		if (null == countryDatabaseMasterVO.getManagementDetails()
				|| countryDatabaseMasterVO.getManagementDetails().trim().length() == 0) {
			countryDatabaseMasterVO.setManagementDetails("No");
		}

		if (null == countryDatabaseMasterVO.getOwnership()
				|| countryDatabaseMasterVO.getOwnership().trim().length() == 0) {
			countryDatabaseMasterVO.setOwnership("No");
		}

		if (null == countryDatabaseMasterVO.getFinancialInformation()
				|| countryDatabaseMasterVO.getFinancialInformation().trim().length() == 0) {
			countryDatabaseMasterVO.setFinancialInformation("No");
		}

		if (null == countryDatabaseMasterVO.getCivilLitigation()
				|| countryDatabaseMasterVO.getCivilLitigation().trim().length() == 0) {
			countryDatabaseMasterVO.setCivilLitigation("No");
		}

		if (null == countryDatabaseMasterVO.getCriminalLitigation()
				|| countryDatabaseMasterVO.getCriminalLitigation().trim().length() == 0) {
			countryDatabaseMasterVO.setCriminalLitigation("No");
		}

		if (null == countryDatabaseMasterVO.getLitigation()
				|| countryDatabaseMasterVO.getLitigation().trim().length() == 0) {
			countryDatabaseMasterVO.setLitigation("No");
		}

		if (null == countryDatabaseMasterVO.getBankRInsolvencyCovered()
				|| countryDatabaseMasterVO.getBankRInsolvencyCovered().trim().length() == 0) {
			countryDatabaseMasterVO.setBankRInsolvencyCovered("No");
		}

		if (null == countryDatabaseMasterVO.getLawEnforcementCovered()
				|| countryDatabaseMasterVO.getLawEnforcementCovered().trim().length() == 0) {
			countryDatabaseMasterVO.setLawEnforcementCovered("No");
		}

		if (null == countryDatabaseMasterVO.getStockExchanges()
				|| countryDatabaseMasterVO.getStockExchanges().trim().length() == 0) {
			countryDatabaseMasterVO.setStockExchanges("No");
		}

		if (null == countryDatabaseMasterVO.getSecurities()
				|| countryDatabaseMasterVO.getSecurities().trim().length() == 0) {
			countryDatabaseMasterVO.setSecurities("No");
		}

		if (null == countryDatabaseMasterVO.getCentralBank()
				|| countryDatabaseMasterVO.getCentralBank().trim().length() == 0) {
			countryDatabaseMasterVO.setCentralBank("No");
		}

		if (null == countryDatabaseMasterVO.getOthersChecks()
				|| countryDatabaseMasterVO.getOthersChecks().trim().length() == 0) {
			countryDatabaseMasterVO.setOthersChecks("No");
		}

		if (null == countryDatabaseMasterVO.getDirectorship()
				|| countryDatabaseMasterVO.getDirectorship().trim().length() == 0) {
			countryDatabaseMasterVO.setDirectorship("No");
		}

	}

	public void updateCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		this.countryDBDAO.updateCountryDatabase(countryDatabaseMasterVO);
	}

	public void deleteCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		this.countryDBDAO.deleteCountryDatabase(countryDatabaseMasterVO);
	}

	public List<CountryDatabaseMasterVO> searchCDBCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		this.logger.debug("in searchCDBCountryDatabase");

		try {
			this.logger.debug("Before ..start :: " + countryDatabaseMasterVO.getStart() + " limit : "
					+ countryDatabaseMasterVO.getLimit());
			if (countryDatabaseMasterVO.getStart() >= 0 && countryDatabaseMasterVO.getLimit() >= 0) {
				countryDatabaseMasterVO
						.setLimit(countryDatabaseMasterVO.getLimit() + countryDatabaseMasterVO.getStart());
				countryDatabaseMasterVO.setStart(countryDatabaseMasterVO.getStart() + 1);
			}

			this.logger.debug("After .. start :: " + countryDatabaseMasterVO.getStart() + " limit : "
					+ countryDatabaseMasterVO.getLimit());
			this.logger.debug("Column Name :: " + countryDatabaseMasterVO.getSortColumnName() + " Sort Type : "
					+ countryDatabaseMasterVO.getSortType());
			if (countryDatabaseMasterVO.getUpdateStartDate() != null
					&& countryDatabaseMasterVO.getUpdateStartDate().trim().length() > 0) {
				countryDatabaseMasterVO
						.setUpdateStartDate(countryDatabaseMasterVO.getUpdateStartDate().replace("T", " "));
			}

			if (countryDatabaseMasterVO.getUpdateEndDate() != null
					&& countryDatabaseMasterVO.getUpdateEndDate().trim().length() > 0) {
				countryDatabaseMasterVO.setUpdateEndDate(countryDatabaseMasterVO.getUpdateEndDate().replace("T", " "));
			}

			this.logger.debug("getUpdateStartDate :: " + countryDatabaseMasterVO.getUpdateStartDate()
					+ " getUpdateEndDate :: " + countryDatabaseMasterVO.getUpdateEndDate());
			return this.countryDBDAO.searchCDBCountryDatabase(countryDatabaseMasterVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int searchCDBCountryDatabaseCount(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		return this.countryDBDAO.searchCDBCountryDatabaseCount(countryDatabaseMasterVO);
	}

	public List<CountryDatabaseMasterVO> searchLBRCountryDatabase(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		try {
			this.logger.debug("Before ..start :: " + countryDatabaseMasterVO.getStart() + " limit : "
					+ countryDatabaseMasterVO.getLimit());
			if (countryDatabaseMasterVO.getStart() >= 0 && countryDatabaseMasterVO.getLimit() >= 0) {
				countryDatabaseMasterVO
						.setLimit(countryDatabaseMasterVO.getLimit() + countryDatabaseMasterVO.getStart());
				countryDatabaseMasterVO.setStart(countryDatabaseMasterVO.getStart() + 1);
			}

			this.logger.debug("After .. start :: " + countryDatabaseMasterVO.getStart() + " limit : "
					+ countryDatabaseMasterVO.getLimit());
			this.logger.debug("Column Name :: " + countryDatabaseMasterVO.getSortColumnName() + " Sort Type : "
					+ countryDatabaseMasterVO.getSortType());
			if (countryDatabaseMasterVO.getUpdateStartDate() != null
					&& countryDatabaseMasterVO.getUpdateStartDate().trim().length() > 0) {
				countryDatabaseMasterVO
						.setUpdateStartDate(countryDatabaseMasterVO.getUpdateStartDate().replace("T", " "));
			}

			if (countryDatabaseMasterVO.getUpdateEndDate() != null
					&& countryDatabaseMasterVO.getUpdateEndDate().trim().length() > 0) {
				countryDatabaseMasterVO.setUpdateEndDate(countryDatabaseMasterVO.getUpdateEndDate().replace("T", " "));
			}

			this.logger.debug("After ..getUpdateStartDate :: " + countryDatabaseMasterVO.getUpdateStartDate()
					+ " getUpdateEndDate :: " + countryDatabaseMasterVO.getUpdateEndDate());
			return this.countryDBDAO.searchLBRCountryDatabase(countryDatabaseMasterVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int searchLBRCountryDatabaseCount(CountryDatabaseMasterVO countryDatabaseMasterVO) throws CMSException {
		return this.countryDBDAO.searchLBRCountryDatabaseCount(countryDatabaseMasterVO);
	}

	public List<CountryDatabaseMasterVO> exportToExcelCDB(Map<String, Object> excelParamMap) throws CMSException {
		return this.countryDBDAO.exportToExcelCDB(excelParamMap);
	}

	public List<CountryDatabaseMasterVO> exportToExcelLBR(Map<String, Object> excelParamMap) throws CMSException {
		return this.countryDBDAO.exportToExcelLBR(excelParamMap);
	}

	public List<CountryDatabaseMasterVO> sourceAvailability() throws CMSException {
		return this.countryDBDAO.sourceAvailability();
	}

	public CountryDatabaseMasterVO getCDBCountryDB(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		return this.countryDBDAO.getCDBCountryDB(countryDatabaseMasterVO);
	}

	public CountryDatabaseMasterVO getLBRCountryDB(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		return this.countryDBDAO.getLBRCountryDB(countryDatabaseMasterVO);
	}

	public boolean isCountryDBUnique(HashMap<String, Object> paramMap) throws CMSException {
		int countryCodeCount = this.countryDBDAO.isCountryDBUnique(paramMap);
		return countryCodeCount > 0;
	}

	public List<CountryDatabaseMasterVO> getLBRDatabases(CountryDatabaseMasterVO countryDatabaseMasterVO)
			throws CMSException {
		return this.countryDBDAO.getLBRDatabases(countryDatabaseMasterVO);
	}

	public boolean isCountryLBRDBUnique(HashMap<String, Object> paramMap) throws CMSException {
		int countryCodeCount = this.countryDBDAO.isCountryLBRDBUnique(paramMap);
		return countryCodeCount > 0;
	}
}