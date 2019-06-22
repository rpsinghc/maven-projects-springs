package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.masters.VendorUploadMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class VendorMultiActionDAO extends SqlMapClientTemplate {
	private static final String COUNTRY_ID = "countryId";
	private static final String UPDATED_BY = "updatedBy";
	private static final String VENDOR_FILE_NAMES = "vendorFileNames";
	private static final String SELECTED_VENDOR_ID = "selectedVendorId";
	private static final String VENDOR_STATUS = "vendorStatus";
	private static final String VENDOR_NAME = "vendorName";
	private static final String VENDOR_CODE = "vendorCode";
	private static final String COUNTRY_LIST = "countryList";
	private static final String VENDOR_MASTER_ID = "vendorMasterId";
	private static final String VENDOR_MASTER_VENDOR_EXISTS_SQL = "VendorMaster.vendorExists";
	private static final String VENDOR_MASTER_DELETE_VENDOR_UPLOADS_SQL = "VendorMaster.deleteVendorUploads";
	private static final String VENDOR_MASTER_GET_VENDOR_UPLOADS_SQL = "VendorMaster.getVendorUploads";
	private static final String VENDOR_MASTER_ADD_VENDOR_UPLOAD_SQL = "VendorMaster.addVendorUpload";
	private static final String GET_ASSOC_WIP_CASES_LIST_SQL = "VendorMaster.getAssociatedWIPCases";
	private static final String GET_DATA_FOR_EXPORT_SQL = "VendorMaster.getDataForExport";
	private static final String GET_SELECTED_COUNTRIES_SQL = "VendorMaster.getSelectedCountries";
	private static final String GET_ALL_COUNTRIES_SQL = "VendorMaster.getAllCountries";
	private static final String CHANGE_VENDOR_STATUS_SQL = "VendorMaster.changeVendorStatus";
	private static final String SEARCH_VENDOR_SQL = "VendorMaster.searchVendor";
	private static final String GET_VENDOR_COUNTRY_LIST_SQL = "VendorMaster.getVendorCountryList";
	private static final String GET_VENDOR_INFO_SQL = "VendorMaster.getVendorInfo";
	private static final String SELECT_VENDOR_TYPE_SQL = "VendorMaster.selectVendorType";
	private static final String INSERT_VENDOR_COUNTRY_MAPPING_SQL = "VendorMaster.insertVendorCountryMapping";
	private static final String DELETE_VENDOR_COUNTRY_MAPPING_SQL = "VendorMaster.deleteVendorCountryMapping";
	private static final String INSERT_VENDOR_SQL = "VendorMaster.insertVendor";
	private static final String UPDATE_VENDOR_SQL = "VendorMaster.updateVendor";
	private static final String GET_TOTAL_COUNT_SQL = "VendorMaster.getTotalCount";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.VendorMultiActionDAO");

	public Integer insertVendor(VendorMasterVO vendorMasterVO) throws CMSException {
		Integer vendorMasterId = null;
		this.logger.debug("in insertVendor");

		try {
			vendorMasterId = (Integer) this.insert("VendorMaster.insertVendor", vendorMasterVO);
			return vendorMasterId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer updateVendor(VendorMasterVO vendorMasterVO) throws CMSException {
		Integer result = null;
		this.logger.debug("in updateVendor");

		try {
			result = this.update("VendorMaster.updateVendor", vendorMasterVO);
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer insertVendorCountryMappings(VendorMasterVO vendorMasterVO) throws CMSException {
		HashMap<String, Object> paramMap = new HashMap();
		Integer numOfCountries = null;
		this.logger.debug("in insertVendorCountryMapping");

		try {
			this.delete("VendorMaster.deleteVendorCountryMapping", vendorMasterVO.getVendorMasterId());
			paramMap.put("vendorMasterId", vendorMasterVO.getVendorMasterId());
			paramMap.put("updatedBy", vendorMasterVO.getUpdatedBy());
			this.logger.debug("VendorMultiActionDAO::insertVendorCountryMapping::vendorMasterId: "
					+ vendorMasterVO.getVendorMasterId());
			Iterator i$ = vendorMasterVO.getVendorCountryList().iterator();

			while (i$.hasNext()) {
				Integer countryId = (Integer) i$.next();
				paramMap.put("countryId", countryId);
				this.insert("VendorMaster.insertVendorCountryMapping", paramMap);
			}

			numOfCountries = vendorMasterVO.getVendorCountryList().size();
			return numOfCountries;
		} catch (UnsupportedOperationException var6) {
			throw new CMSException(this.logger, var6);
		} catch (ClassCastException var7) {
			throw new CMSException(this.logger, var7);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (IllegalArgumentException var9) {
			throw new CMSException(this.logger, var9);
		} catch (DataAccessException var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<VendorMasterVO> getVendorTypes() throws CMSException {
		new ArrayList();
		this.logger.debug("in getVendorTypes");

		try {
			List<VendorMasterVO> vendorTypesList = this.queryForList("VendorMaster.selectVendorType");
			return vendorTypesList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public VendorMasterVO getVendorInfo(String vendorMasterId) throws CMSException {
		VendorMasterVO vendorInfo = null;
		List<Integer> vendorCountryList = null;
		this.logger.debug("in getVendorInfo");

		try {
			this.logger.debug("getVendorInfo::vendorMasterId: " + vendorMasterId);
			vendorInfo = (VendorMasterVO) this.queryForObject("VendorMaster.getVendorInfo", vendorMasterId);
			vendorCountryList = this.queryForList("VendorMaster.getVendorCountryList", vendorMasterId);
			if (vendorInfo != null) {
				vendorInfo.setVendorCountryList(vendorCountryList);
			}

			return vendorInfo;
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<VendorMasterVO> searchVendor(HashMap<String, Object> paramMap) throws CMSException {
		List<VendorMasterVO> vendorList = null;
		this.logger.debug("in searchVendor");

		try {
			vendorList = this.queryForList("VendorMaster.searchVendor", paramMap);
			return vendorList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer getTotalCount(HashMap<String, Object> paramMap) throws CMSException {
		this.logger.debug("in getTotalCount");
		Integer totalCount = null;

		try {
			totalCount = (Integer) this.queryForObject("VendorMaster.getTotalCount", paramMap);
			return totalCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<VendorMasterVO> getDataForExport(Map<String, Object> excelParamMap) throws CMSException {
		List<VendorMasterVO> vendorList = null;
		this.logger.debug("in searchVendor");
		new ArrayList();

		try {
			List<String> countryList = StringUtils
					.commaSeparatedStringToList((String) excelParamMap.get("countryList"));
			excelParamMap.put("countryList", countryList);
			vendorList = this.queryForList("VendorMaster.getDataForExport", excelParamMap);
			return vendorList;
		} catch (UnsupportedOperationException var5) {
			throw new CMSException(this.logger, var5);
		} catch (ClassCastException var6) {
			throw new CMSException(this.logger, var6);
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (IllegalArgumentException var8) {
			throw new CMSException(this.logger, var8);
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public Integer changeVendorStatus(String selectedVendorId, String vendorStatus, String updatedBy)
			throws CMSException {
		HashMap<String, Object> paramMap = new HashMap();
		this.logger.debug("in changeVendorStatus");
		Integer numVendorsUpdated = null;

		try {
			paramMap.put("selectedVendorId", selectedVendorId);
			paramMap.put("vendorStatus", vendorStatus);
			paramMap.put("updatedBy", updatedBy);
			numVendorsUpdated = this.update("VendorMaster.changeVendorStatus", paramMap);
			return numVendorsUpdated;
		} catch (UnsupportedOperationException var7) {
			throw new CMSException(this.logger, var7);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (IllegalArgumentException var10) {
			throw new CMSException(this.logger, var10);
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public List<String> getAssociatedWIPCasesList(String selectedVendorId) throws CMSException {
		List<String> vendorCrnList = null;
		this.logger.debug("in getAssociatedWIPCasesList");

		try {
			vendorCrnList = this.queryForList("VendorMaster.getAssociatedWIPCases", selectedVendorId);
			return vendorCrnList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String addVendorUpload(VendorUploadMasterVO vendorUploadMasterVO) throws CMSException {
		this.logger.debug("in addVendorUpload");
		String fileName = null;

		try {
			this.insert("VendorMaster.addVendorUpload", vendorUploadMasterVO);
			fileName = vendorUploadMasterVO.getFileName();
			return fileName;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<VendorUploadMasterVO> getVendorUploads(String vendorCode) throws CMSException {
		this.logger.debug("in getVendorUploads");
		List<VendorUploadMasterVO> vendorUploadsList = null;
		HashMap paramMap = new HashMap();

		try {
			paramMap.put("vendorCode", vendorCode);
			vendorUploadsList = this.queryForList("VendorMaster.getVendorUploads", paramMap);
			return vendorUploadsList;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Integer deleteVendorUploads(String vendorCode, List<String> vendorFileNames) throws CMSException {
		this.logger.debug("in deleteVendorUploads");
		HashMap<String, Object> paramMap = new HashMap();
		Integer numFilesDeleted = null;

		try {
			paramMap.put("vendorCode", vendorCode);
			paramMap.put("vendorFileNames", vendorFileNames);
			numFilesDeleted = this.delete("VendorMaster.deleteVendorUploads", paramMap);
			return numFilesDeleted;
		} catch (UnsupportedOperationException var6) {
			throw new CMSException(this.logger, var6);
		} catch (ClassCastException var7) {
			throw new CMSException(this.logger, var7);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (IllegalArgumentException var9) {
			throw new CMSException(this.logger, var9);
		} catch (DataAccessException var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public String vendorExists(String vendorName) throws CMSException {
		this.logger.debug("in vendorExists");
		HashMap<String, Object> paramMap = new HashMap();
		String vendorCode = null;

		try {
			paramMap.put("vendorName", vendorName);
			vendorCode = (String) this.queryForObject("VendorMaster.vendorExists", paramMap);
			return vendorCode;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CountryMasterVO> getAllCountries(String vendorMasterId) throws CMSException {
		List<CountryMasterVO> allCountriesList = null;
		HashMap paramMap = new HashMap();

		try {
			paramMap.put("vendorMasterId", vendorMasterId);
			allCountriesList = this.queryForList("VendorMaster.getAllCountries", paramMap);
			return allCountriesList;
		} catch (UnsupportedOperationException var5) {
			throw new CMSException(this.logger, var5);
		} catch (ClassCastException var6) {
			throw new CMSException(this.logger, var6);
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (IllegalArgumentException var8) {
			throw new CMSException(this.logger, var8);
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List<CountryMasterVO> getSelectedCountries(String vendorMasterId) throws CMSException {
		List selectedCountriesList = null;

		try {
			selectedCountriesList = this.queryForList("VendorMaster.getSelectedCountries", vendorMasterId);
			return selectedCountriesList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}