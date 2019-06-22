package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.vo.JSONBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JsonBeanUtil {
	public static List<JSONBean> toJsonBean(List<String> commonList) {
		new JSONBean();
		List<JSONBean> jsonBeanList = new ArrayList();
		Iterator i$ = commonList.iterator();

		while (i$.hasNext()) {
			String name = (String) i$.next();
			JSONBean jsonBean = new JSONBean();
			jsonBean.setName(name);
			jsonBeanList.add(jsonBean);
		}

		return jsonBeanList;
	}

	public static List<JSONBean> toJsonBeanFromHashMap(HashMap<String, String> map, String listType) {
		new JSONBean();
		List<JSONBean> jsonBeanList = new ArrayList();
		Set<String> keys = map.keySet();
		Iterator i$ = keys.iterator();

		while (i$.hasNext()) {
			String name = (String) i$.next();
			JSONBean jsonBean = new JSONBean();
			jsonBean.setKey(name);
			jsonBean.setValue((String) map.get(name));
			jsonBean.setListType(listType);
			jsonBeanList.add(jsonBean);
		}

		return jsonBeanList;
	}
}