package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.VendorMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.masters.VendorUploadMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IVendorMaster {
	void setVendorMultiActionDAO(VendorMultiActionDAO var1);

	void setPropertyReader(PropertyReaderUtil var1);

	VendorMasterVO saveVendor(VendorMasterVO var1, boolean var2, List<VendorUploadMasterVO> var3,
			List<VendorUploadMasterVO> var4) throws CMSException;

	VendorMasterVO getVendorInfo(String var1) throws CMSException;

	List<VendorMasterVO> getVendorTypes() throws CMSException;

	List<VendorMasterVO> searchVendor(HashMap<String, Object> var1) throws CMSException;

	int getTotalCount(HashMap<String, Object> var1) throws CMSException;

	List<VendorMasterVO> getDataForExport(Map<String, Object> var1) throws CMSException;

	void changeVendorStatus(String var1, String var2, String var3) throws CMSException;

	List<String> getAssociatedWIPCasesList(String var1) throws CMSException;

	void addVendorUpload(VendorUploadMasterVO var1) throws CMSException;

	List<VendorUploadMasterVO> getVendorUploads(String var1, List<VendorUploadMasterVO> var2,
			List<VendorUploadMasterVO> var3) throws CMSException;

	String vendorExists(String var1) throws CMSException;

	List<CountryMasterVO> getAllCountries(String var1) throws CMSException;

	List<CountryMasterVO> getSelectedCountries(String var1) throws CMSException;

	List<VendorUploadMasterVO> tempDeleteVendorUploads(List<String> var1, List<VendorUploadMasterVO> var2,
			List<VendorUploadMasterVO> var3, String var4, String var5);

	boolean vendorFileExists(String var1, String var2, String var3);
}