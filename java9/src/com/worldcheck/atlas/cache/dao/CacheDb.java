package com.worldcheck.atlas.cache.dao;

import com.worldcheck.atlas.cache.vo.CacheConstants;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CacheDb extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.cache.dao.CacheDb");

	public HashMap<String, List> getResultSet() throws CMSException {
		this.logger.debug("In getResultSet");
		HashMap resultSetMap = new HashMap();

		try {
			for (int i = 0; i < CacheConstants.cacheKey.length; ++i) {
				new ArrayList();
				List resultSetVOList = this.queryForList("AtlasCache." + CacheConstants.cacheKey[i], (Object) null);
				resultSetMap.put(CacheConstants.cacheKey[i], resultSetVOList);
			}

			return resultSetMap;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List getMasterResultSet(String master) throws CMSException {
		this.logger.debug("In getMasterResultSet");
		new ArrayList();

		try {
			List list = this.queryForList("AtlasCache." + master);
			this.logger.info("fetched for :" + master + ":::::::::" + list.size());
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getUnconfirmedBudgetCount() throws CMSException {
		this.logger.debug("in getUnconfirmedBudgetCount");

		try {
			return (Integer) this.queryForObject("AtlasCache.getUnconfirmedBudgetDetailsCount");
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}