package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.massvendordataentry.MassVendorUploadDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IMassVendorUpload {
	void setMassVendorUploadDAO(MassVendorUploadDAO var1);

	void setPropertyReader(PropertyReaderUtil var1);

	void setMasterDataValidator(IMasterDataValidator var1);

	List<String> processExcel(File var1) throws CMSException;

	void addData(List<HashMap> var1, String var2) throws CMSException;

	List<String> getModifiedDataList(List<Map> var1, String var2, String var3) throws CMSException;
}