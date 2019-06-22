package com.worldcheck.atlas.dao.noncrnexpenditure;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.noncrnexpenditure.NonCRNExpenditureVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class NonCrnExpenditureDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.noncrnexpenditure.NonCrnExpenditureDAO");
	private String INSERT_NONCRN_DATA_QUERY = "AdminConsole.insertCRNExpenditure";
	private String GET_COUNTRYVENDORlIST_QUERY = "AdminConsole.getCountryVendorMaster";

	public int save(NonCRNExpenditureVO nonCRNExpenditureVO) throws CMSException {
		boolean var2 = false;

		try {
			int masterId = (Integer) this.insert(this.INSERT_NONCRN_DATA_QUERY, nonCRNExpenditureVO);
			this.logger.debug("master id for non crn " + masterId);
			return masterId;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public List<VendorMasterVO> getCountryVendorMaster(String countryId) throws CMSException {
		new ArrayList();

		try {
			List<VendorMasterVO> vendorList = this.queryForList(this.GET_COUNTRYVENDORlIST_QUERY, countryId);
			return vendorList;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}
}