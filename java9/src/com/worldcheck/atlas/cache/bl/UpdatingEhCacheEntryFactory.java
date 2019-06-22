package com.worldcheck.atlas.cache.bl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.ehcache.constructs.blocking.UpdatingCacheEntryFactory;

public class UpdatingEhCacheEntryFactory extends EhCacheEntryFactory implements UpdatingCacheEntryFactory {
	public void updateEntryValue(Object key, Object value) throws Exception {
		try {
			Long t1 = System.currentTimeMillis();
			HashMap<String, List> resultSetHashMap = EhCacheUtil.getInstance().getResultSetHashMap();
			int find = false;
			Set keySet = resultSetHashMap.keySet();

			for (Iterator keyItr = keySet.iterator(); keyItr.hasNext(); find = true) {
				Object keyObj = keyItr.next();
				String keyName = keyObj.toString();
				if (keyName.equals(key) && ((List) value).size() > 0
						&& ((List) value).size() != ((List) resultSetHashMap.get(keyObj)).size()) {
					((List) value).clear();
					List valueList = (List) resultSetHashMap.get(keyObj);
					Iterator valueListItr = valueList.iterator();

					while (valueListItr.hasNext()) {
						Object valueListItrObj = valueListItr.next();
						((List) value).add(valueListItrObj);
					}

					find = true;
					break;
				}
			}

			if (!find) {
				((List) value).removeAll((Collection) resultSetHashMap.get(key));
			}
		} catch (Exception var13) {
			var13.printStackTrace();
		}

	}
}