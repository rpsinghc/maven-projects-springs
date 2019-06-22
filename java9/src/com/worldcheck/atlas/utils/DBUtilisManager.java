package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;

public class DBUtilisManager implements IDBUtilis {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.utils.DBUtilisManager");
	DBUtils dbUtils;

	public void setDbUtils(DBUtils dbUtils) {
		this.dbUtils = dbUtils;
	}

	public String getSigleColumnDataFromTable(String tablename, String selectColumn, String whrClauseKey,
			String whrClauseValue, String whrClauseKeyOpt, String whrClauseValueOpt) throws CMSException {
		this.logger.debug("inside getSigleColumnDataFromTable method..tablename::" + tablename);
		this.logger.debug("inside getSigleColumnDataFromTable method..selectColumn::" + selectColumn);
		this.logger.debug("inside getSigleColumnDataFromTable method..whrClauseKey::" + whrClauseKey);
		this.logger.debug("inside getSigleColumnDataFromTable method..whrClauseValue::" + whrClauseValue);
		this.logger.debug("inside getSigleColumnDataFromTable method..dbUtils::" + this.dbUtils);
		return this.dbUtils.getSigleColumnDataFromTable(tablename, selectColumn, whrClauseKey, whrClauseValue,
				whrClauseKeyOpt, whrClauseValueOpt);
	}
}