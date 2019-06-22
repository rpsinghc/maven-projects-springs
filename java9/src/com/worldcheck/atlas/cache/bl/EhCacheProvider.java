package com.worldcheck.atlas.cache.bl;

import com.worldcheck.atlas.cache.dao.CacheDb;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

public class EhCacheProvider {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.cache.bl.EhCacheProvider");
	private CacheEntryFactory updatingFactory;
	private Ehcache cache;
	private HashMap<String, List> resultSetMap;
	private CacheDb cacheDb;

	public void setCacheDb(CacheDb cacheDb) {
		this.logger.debug("In setCacheDb");
		this.cacheDb = cacheDb;
	}

	public HashMap<String, List> getResultSetMap() {
		return this.resultSetMap;
	}

	public void setResultSetMap(HashMap<String, List> resultSetMap) {
		this.resultSetMap = resultSetMap;
	}

	public void setCache(Ehcache cache) {
		cache.removeAll();
		this.cache = cache;

		try {
			this.run();
		} catch (Throwable var3) {
			var3.printStackTrace();
		}

	}

	public Ehcache getCache() {
		return this.cache;
	}

	public void run() {
		this.logger.debug("In run method");

		try {
			Long t1 = System.currentTimeMillis();
			this.resultSetMap = this.cacheDb.getResultSet();
			EhCacheUtil.getInstance().setResultSetHashMap(this.resultSetMap);
			Long t2 = System.currentTimeMillis();
			Set keySet = this.resultSetMap.keySet();
			Iterator itr = keySet.iterator();

			while (itr.hasNext()) {
				Object key = itr.next();
				List value = (List) this.resultSetMap.get(key);
				if (!this.cache.getKeys().contains(key.toString())) {
					this.cache.put(new Element(key.toString(), value));
				}
			}
		} catch (Exception var9) {
			var9.printStackTrace();
		}

		try {
			this.cache.put(new Element("TimeTrackerToken", new HashMap()));
			this.cache.put(new Element("ACLToken", new HashMap()));
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		try {
			this.cache.put(new Element("budgetCount", this.cacheDb.getUnconfirmedBudgetCount()));
		} catch (Exception var7) {
			var7.printStackTrace();
		}

	}

	public List addToCacheRunTime(String master) throws CMSException {
		this.logger.debug("In addToCacheRunTime");
		return this.cacheDb.getMasterResultSet(master);
	}

	public int getUnconfirmedBudgetCount() throws CMSException {
		this.logger.debug("in getUnconfirmedBudgetCount");
		return this.cacheDb.getUnconfirmedBudgetCount();
	}
}