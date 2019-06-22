package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.noncrnexpenditure.NonCrnExpenditureDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.noncrnexpenditure.NonCRNExpenditureVO;
import java.util.List;

public interface INonCrnExpenditure {
	void setNonCrnExpenditureDAO(NonCrnExpenditureDAO var1);

	void saveCRNData(NonCRNExpenditureVO var1) throws CMSException;

	List<VendorMasterVO> getCountryVendorMaster(String var1) throws CMSException;
}