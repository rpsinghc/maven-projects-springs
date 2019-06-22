package com.worldcheck.atlas.bl.noncrnexpenditure;

import com.worldcheck.atlas.bl.interfaces.INonCrnExpenditure;
import com.worldcheck.atlas.dao.noncrnexpenditure.NonCrnExpenditureDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.noncrnexpenditure.NonCRNExpenditureVO;
import java.util.ArrayList;
import java.util.List;

public class NonCrnExpenditureService implements INonCrnExpenditure {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.noncrnexpenditure.NonCrnExpenditureService");
	private NonCrnExpenditureDAO nonCrnExpenditureDAO;

	public void setNonCrnExpenditureDAO(NonCrnExpenditureDAO nonCrnExpenditureDAO) {
		this.nonCrnExpenditureDAO = nonCrnExpenditureDAO;
	}

	public void saveCRNData(NonCRNExpenditureVO nonCRNExpenditureVO) throws CMSException {
		this.nonCrnExpenditureDAO.save(nonCRNExpenditureVO);
	}

	public List<VendorMasterVO> getCountryVendorMaster(String countryId) throws CMSException {
		new ArrayList();
		List<VendorMasterVO> vendorList = this.nonCrnExpenditureDAO.getCountryVendorMaster(countryId);
		return vendorList;
	}
}