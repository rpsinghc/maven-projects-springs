package com.worldcheck.atlas.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ExposePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private Map<String, String> resolvedProps;

	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		this.resolvedProps = new HashMap();
		Iterator i$ = props.keySet().iterator();

		while (i$.hasNext()) {
			Object key = i$.next();
			String keyStr = key.toString();
			String propVal = props.getProperty(keyStr);
			this.resolvedProps.put(keyStr, propVal);
		}

	}

	public Map<String, String> getResolvedProps() {
		return Collections.unmodifiableMap(this.resolvedProps);
	}
}