package com.worldcheck.atlas.cache.bl;

import java.util.HashMap;
import java.util.List;

public class EhCacheUtil {
	private static EhCacheUtil ehCacheUtil = new EhCacheUtil();
	private HashMap<String, List> resultSetHashMap = new HashMap();

	public HashMap<String, List> getResultSetHashMap() {
		return this.resultSetHashMap;
	}

	public void setResultSetHashMap(HashMap<String, List> resultSetHashMap) {
		this.resultSetHashMap = resultSetHashMap;
	}

	public static EhCacheUtil getInstance() {
		return ehCacheUtil;
	}
}