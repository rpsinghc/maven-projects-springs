package com.worldcheck.atlas.validation.dao;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ValidationDAO extends SqlMapClientTemplate {
	private static final String ADMIN_CONSOLE_GET_AUTHTWO_HISTORY = "AdminConsole.getPasswordHistory";
	private static final String GET_TEMP_PASSWORD = "UserMaster.getTempPassword";
	private static final String ADMIN_CONSOLE_GET_CRN_COUNT = "AdminConsole.getCRNCount";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.validation.dao.ValidationDAO");

	public String getPasswordHistory(String userName) throws CMSException {
		String password = "";

		try {
			password = (String) this.queryForObject("AdminConsole.getPasswordHistory", userName);
			return password;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public int getClientNameCount(String clientName) {
		return 0;
	}

	public CaseDetails getCaseDetails(String crn) {
		return null;
	}

	public int checkCRN(String crn) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("AdminConsole.getCRNCount", crn);
			this.logger.debug("count " + count);
			return count;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}

	public String getTempPassword(String userName) throws CMSException {
		this.logger.debug("Entring ValidationDao : gettempPassword");
		String password = "";

		try {
			password = (String) this.queryForObject("UserMaster.getTempPassword", userName);
			return password;
		} catch (DataAccessException var5) {
			CMSException cmsException = new CMSException(this.logger, var5);
			throw cmsException;
		}
	}
}