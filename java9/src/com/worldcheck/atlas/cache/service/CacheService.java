package com.worldcheck.atlas.cache.service;

import com.worldcheck.atlas.cache.bl.EhCacheProvider;
import com.worldcheck.atlas.cache.utils.producer.InvokeCacheServiceProducer;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class CacheService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.cache.service.CacheService");
	private static final String ACL_TOKEN = "ACLToken";
	private static final String TT_TOKEN = "TimeTrackerToken";
	private EhCacheProvider ehCacheProvider;

	public void setEhCacheProvider(EhCacheProvider ehCacheProvider) {
		this.ehCacheProvider = ehCacheProvider;
	}

	public List getCacheItemsList(String master) throws CMSException {
		Ehcache cache = null;
		List cacheItemList = null;

		try {
			cache = this.ehCacheProvider.getCache();
			List ls = cache.getKeys();
			Iterator itr = ls.iterator();

			while (itr.hasNext()) {
				Object keyObject = itr.next();
				String key = keyObject.toString();
				if (master.equalsIgnoreCase(key)) {
					cacheItemList = (List) cache.get(keyObject).getObjectValue();
					break;
				}
			}

			return cacheItemList;
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public HashMap<String, String> getACLTokenCache() throws CMSException {
		new HashMap();

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			HashMap<String, String> acltokenHMap = (HashMap) cache.get("ACLToken").getObjectValue();
			return acltokenHMap;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void updateACLTokenCache(HashMap<String, String> hmap) throws CMSException {
		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("map", hmap);
		hashMap.put("action", "updateToken");
		InvokeCacheServiceProducer invoke = new InvokeCacheServiceProducer();

		try {
			Iterator i$ = hmap.keySet().iterator();

			while (i$.hasNext()) {
				String key = (String) i$.next();
				this.updateTokenForACL(key, (String) hmap.get(key));
			}

			invoke.invokeService(hashMap, this.getIpAddress());
		} catch (IOException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private String getIpAddress() throws CMSException {
		String ipaddress = "";

		try {
			InetAddress ownIP = InetAddress.getLocalHost();
			ipaddress = ownIP.getHostAddress();
		} catch (UnknownHostException var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("ip address of this request is " + ipaddress);
		return ipaddress;
	}

	public void removeACLTokenCache(String userId) throws CMSException {
		HashMap<String, String> hmap = new HashMap();
		hmap.put(userId, "remove");
		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("map", hmap);
		hashMap.put("action", "updateToken");
		InvokeCacheServiceProducer invoke = new InvokeCacheServiceProducer();

		try {
			this.removeTokenForACL(userId);
			invoke.invokeService(hashMap, this.getIpAddress());
		} catch (IOException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void addToCacheRunTime(String master) throws CMSException {
		this.logger.debug("In cacheService: addToCacheRunTime");
		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("token", master);
		hashMap.put("map", (Object) null);
		hashMap.put("action", "updateMaster");
		this.logger.debug("inside addToCache method");
		InvokeCacheServiceProducer invoke = new InvokeCacheServiceProducer();

		try {
			this.refreshCache(master);
			invoke.invokeService(hashMap, this.getIpAddress());
		} catch (IOException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void refreshCache(String master) throws CMSException {
		this.logger.debug("In CacheService: refreshCache " + master);
		new ArrayList();

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			List masterUpdatedList = this.ehCacheProvider.addToCacheRunTime(master);
			this.logger.debug(
					masterUpdatedList + ">>>>>>>>before put" + ((List) cache.get(master).getObjectValue()).size());
			cache.remove(master);
			cache.put(new Element(master, masterUpdatedList));
			this.logger.debug(masterUpdatedList.size() + ">>>>>>>>>>>>>>after put"
					+ ((List) cache.get(master).getObjectValue()).size());
			this.logger
					.debug("size is " + ((List) this.ehCacheProvider.getCache().get(master).getObjectValue()).size());
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateTokenForACL(String userId, String tokenId) throws CMSException {
		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			HashMap<String, String> tempMap = new HashMap();
			if (null != cache.get("ACLToken")) {
				tempMap = (HashMap) cache.get("ACLToken").getObjectValue();
				if (null == tempMap) {
					tempMap = new HashMap();
				}
			}

			tempMap.put(userId, tokenId);
			Element el = new Element("ACLToken", tempMap);
			cache.put(el);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void removeTokenForACL(String userId) throws CMSException {
		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			HashMap<String, String> tempMap = null;
			if (null != cache.get("ACLToken")) {
				tempMap = (HashMap) cache.get("ACLToken").getObjectValue();
				if (null != tempMap) {
					tempMap.remove(userId);
					Element el = new Element("ACLToken", tempMap);
					cache.put(el);
				}
			}

		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getTimeTrackerTokenCache(String userId) throws CMSException {
		this.logger.debug("In get TimeTracker Token for userId :" + userId);
		HashMap<String, String> timeTrackerTokenHMap = null;
		String timeTrackerToken = null;

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			timeTrackerTokenHMap = (HashMap) cache.get("TimeTrackerToken").getObjectValue();
			this.logger.debug("timeTrackerTokenHMap :: " + timeTrackerTokenHMap);
			timeTrackerToken = (String) timeTrackerTokenHMap.get(userId);
			return timeTrackerToken;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateTimeTrackerTokenCache(String userId, String timeTrackerId) throws CMSException {
		this.logger.debug("In add TimeTracker Token for userId :" + userId);
		HashMap timeTrackerTokenHMap = null;

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			timeTrackerTokenHMap = (HashMap) cache.get("TimeTrackerToken").getObjectValue();
			Element el;
			if (null != timeTrackerTokenHMap) {
				timeTrackerTokenHMap.remove(userId);
				timeTrackerTokenHMap.put(userId, timeTrackerId);
				el = new Element("TimeTrackerToken", timeTrackerTokenHMap);
				cache.put(el);
			} else {
				timeTrackerTokenHMap.put(userId, timeTrackerId);
				el = new Element("TimeTrackerToken", timeTrackerTokenHMap);
				cache.put(el);
			}

		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void removeTimeTrackerTokenCache(String userId) throws CMSException {
		this.logger.debug("In remove TimeTracker Token for userId :" + userId);

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			HashMap<String, String> timeTrackerTokenHMap = null;
			if (null != cache.get("TimeTrackerToken")) {
				timeTrackerTokenHMap = (HashMap) cache.get("TimeTrackerToken").getObjectValue();
				if (null == timeTrackerTokenHMap) {
					this.logger.debug("timetracker token not initailzed in cache.");
				} else {
					timeTrackerTokenHMap.remove(userId);
					Element el = new Element("TimeTrackerToken", timeTrackerTokenHMap);
					cache.put(el);
				}
			}

		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateTTTokenCache(String userId, String timeTrackerId) throws CMSException {
		this.logger.debug("in updateTTTokenCache");
		HashMap<String, Object> hashMap = new HashMap();
		HashMap<String, String> hmap = new HashMap();
		hmap.put("userId", userId);
		hmap.put("trackerId", timeTrackerId);
		hashMap.put("map", hmap);
		hashMap.put("action", "updateTTToken");
		InvokeCacheServiceProducer invoke = new InvokeCacheServiceProducer();

		try {
			this.updateTimeTrackerTokenCache(userId, timeTrackerId);
			invoke.invokeService(hashMap, this.getIpAddress());
		} catch (IOException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void removeTTTokenCache(String userId) throws CMSException {
		this.logger.debug("in removeTTTokenCache");
		HashMap<String, String> hmap = new HashMap();
		hmap.put("userId", userId);
		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("map", hmap);
		hashMap.put("action", "deleteTTToken");
		InvokeCacheServiceProducer invoke = new InvokeCacheServiceProducer();

		try {
			this.removeTimeTrackerTokenCache(userId);
			invoke.invokeService(hashMap, this.getIpAddress());
		} catch (IOException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int getBudgetCount() throws CMSException {
		this.logger.debug("in get budget count:");

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			return (Integer) cache.get("budgetCount").getObjectValue();
		} catch (Exception var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public void updateBudgetCount() throws CMSException {
		this.logger.debug("in update budget count:");
		boolean var1 = false;

		try {
			Ehcache cache = this.ehCacheProvider.getCache();
			int count = this.ehCacheProvider.getUnconfirmedBudgetCount();
			cache.remove("budgetCount");
			cache.put(new Element("budgetCount", count));
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void updateBudgetCountCache() throws CMSException {
		HashMap<String, Object> hashMap = new HashMap();
		hashMap.put("token", "BudgetCount");
		hashMap.put("map", (Object) null);
		hashMap.put("action", "updateBudgetCount");
		InvokeCacheServiceProducer invoke = new InvokeCacheServiceProducer();

		try {
			this.updateBudgetCount();
			invoke.invokeService(hashMap, this.getIpAddress());
		} catch (IOException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}