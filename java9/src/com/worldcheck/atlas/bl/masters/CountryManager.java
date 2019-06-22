package com.worldcheck.atlas.bl.masters;

import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.bl.interfaces.ICountryMaster;
import com.worldcheck.atlas.dao.masters.CountryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.List;
import java.util.Map;

public class CountryManager implements ICountryMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.CountryManager");
	private CountryDAO countryDAO = null;

	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	public List<CountryMasterVO> getCountryGrid(CountryMasterVO countryMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		countryMasterVO.setStart(new Integer(start + 1));
		countryMasterVO.setLimit(new Integer(start + limit));
		countryMasterVO.setSortColumnName(sortColumnName);
		countryMasterVO.setSortType(sortType);
		List<CountryMasterVO> countryList = this.countryDAO.getCountryGrid(countryMasterVO);
		return countryList;
	}

	public String changeStatus(int countryId, String countryStatus, String updatedBy) throws CMSException {
		String var4 = "";

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Country");
			upMasterVO.setUpdateType("Update");
			com.integrascreen.orders.CountryMasterVO masterObject = new com.integrascreen.orders.CountryMasterVO();
			CountryMasterVO countryMasterVO = this.countryDAO.getCountryInfo(countryId);
			this.logger.debug("CountryCode::" + countryMasterVO.getCountryCode() + "Country::"
					+ countryMasterVO.getCountryCode() + "Risk::" + countryMasterVO.getRiskLevel() + "Region::"
					+ countryMasterVO.getRegion() + "EnglishCountry::" + countryMasterVO.getEnglishCountry()
					+ "CountryStatus::" + countryMasterVO.getCountryStatus());
			masterObject.setCode(countryMasterVO.getCountryCode());
			masterObject.setIsEnglishSpeaking(!countryMasterVO.getEnglishCountry().equals("0"));
			masterObject.setDescription(countryMasterVO.getCountry());
			masterObject.setIsEnglishSpeaking(!countryMasterVO.getEnglishCountry().equals("0"));
			masterObject.setStatus(countryStatus.equals("1") ? "D" : "A");
			upMasterVO.setCountryMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.countryDAO.changeStatus(countryId, countryStatus, updatedBy) + "#success"
					: isisResponseVO.getResponseVO().getMessage() + "#failure";
		} catch (CMSException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public CountryMasterVO getCountryInfo(int countryId) throws CMSException {
		CountryMasterVO countryMasterVO = this.countryDAO.getCountryInfo(countryId);
		return countryMasterVO;
	}

	public String updateCountry(CountryMasterVO countryMasterVO) throws CMSException {
		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Country");
			upMasterVO.setUpdateType("Update");
			com.integrascreen.orders.CountryMasterVO masterObject = new com.integrascreen.orders.CountryMasterVO();
			this.logger.debug("CountryCode::" + countryMasterVO.getCountryCode() + "Country::"
					+ countryMasterVO.getCountryCode() + "Risk::" + countryMasterVO.getRiskLevel() + "Region::"
					+ countryMasterVO.getRegion() + "EnglishCountry::" + countryMasterVO.getEnglishCountry()
					+ "CountryStatus::" + countryMasterVO.getCountryStatus());
			masterObject.setCode(countryMasterVO.getCountryCode());
			masterObject.setDescription(countryMasterVO.getCountry());
			masterObject.setIsEnglishSpeaking(!countryMasterVO.getEnglishCountry().equals("0"));
			masterObject.setStatus(countryMasterVO.getCountryStatus().equals("0") ? "D" : "A");
			upMasterVO.setCountryMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.countryDAO.updateCountry(countryMasterVO) + "#success"
					: isisResponseVO.getResponseVO().getMessage() + "#failure";
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String addCountry(CountryMasterVO countryMasterVO) throws CMSException {
		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Country");
			upMasterVO.setUpdateType("Insert");
			com.integrascreen.orders.CountryMasterVO masterObject = new com.integrascreen.orders.CountryMasterVO();
			this.logger.debug("CountryCode::" + countryMasterVO.getCountryCode() + "Country::"
					+ countryMasterVO.getCountryCode() + "Risk::" + countryMasterVO.getRiskLevel() + "Region::"
					+ countryMasterVO.getRegion() + "EnglishCountry::" + countryMasterVO.getEnglishCountry()
					+ "CountryStatus::" + countryMasterVO.getCountryStatus());
			masterObject.setCode(countryMasterVO.getCountryCode());
			masterObject.setDescription(countryMasterVO.getCountry());
			masterObject.setIsEnglishSpeaking(!countryMasterVO.getEnglishCountry().equals("0"));
			masterObject.setStatus("A");
			upMasterVO.setCountryMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.countryDAO.addCountry(countryMasterVO) + "#success"
					: isisResponseVO.getResponseVO().getMessage() + "#failure";
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean isExistCountryCode(String countryCode) throws CMSException {
		int countryCodeCount = this.countryDAO.getCountExistCountryCode(countryCode);
		return countryCodeCount > 0;
	}

	public boolean isExistCountry(String country) throws CMSException {
		int countryNameCount = this.countryDAO.getCountExistCountry(country);
		return countryNameCount > 0;
	}

	public int getCountryGridCount(CountryMasterVO countryMasterVO) throws CMSException {
		return this.countryDAO.getCountryGridCount(countryMasterVO);
	}

	public CountryMasterVO checkAssociatedMaster(int countryMasterId) throws CMSException {
		return this.countryDAO.checkAssociatedMaster(countryMasterId);
	}

	public List<CountryMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		return this.countryDAO.getReport(excelParamMap);
	}
}