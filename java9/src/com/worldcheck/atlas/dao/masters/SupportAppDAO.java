package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.SupportAppVO;
import java.util.List;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class SupportAppDAO extends SqlMapClientTemplate {
	private static final String GET_CRN_INFO = "SupportApp.getCRNInfo";
	private static final String UPDATE_DATES_WC = "SupportApp.updateDatesWC";
	private static final String UPDATE_DATES_SAV = "SupportApp.updateDatesSAV";
	private static final String FETCH_USER_AUTHTWO = "SupportApp.fetchUserPWD";
	private static final String MONTHLY_RE_DATA_EXPORT = "SupportApp.getMonthlyREDataExport";
	private static final String MONTHLY_CASE_HISTORY_DATA_EXPORT = "SupportApp.getMonthlyCaseHistoryDataExport";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.SupportAppDAO");

	public SupportAppVO searchCRNDetails(String crn) throws CMSException {
		return (SupportAppVO) this.queryForObject("SupportApp.getCRNInfo", crn);
	}

	public void updateCRNDates(SupportAppVO suppVO) throws CMSException {
		this.logger.debug("in updateCRNDates DAO ");
		this.update("SupportApp.updateDatesWC", suppVO);
		this.update("SupportApp.updateDatesSAV", suppVO);
	}

	public String getUserPWD(String userId) throws CMSException {
		return (String) this.queryForObject("SupportApp.fetchUserPWD", userId);
	}

	public List<SupportAppVO> getREDataForExport(SupportAppVO supportAppVO) {
		return this.queryForList("SupportApp.getMonthlyREDataExport", supportAppVO);
	}

	public List<SupportAppVO> getHistoryDataForExport(SupportAppVO supportAppVO) {
		return this.queryForList("SupportApp.getMonthlyCaseHistoryDataExport", supportAppVO);
	}
}