package com.worldcheck.atlas.bl.flowcontroller.ibatis;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FlowMutexImpl {
	private static Reader reader;
	private static SqlMapClient sqlMap = null;
	private static final String CHECK_FOR_LOCK = "mutex.GET_LOCK_INFO";
	private static final String GET_LOCK = "mutex.INSERT_LOCK_INFO";
	private static final String RELEASE_LOCK = "mutex.RELEASE_LOCK";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.flowcontroller.ibatis.FlowMutexImpl");

	public FlowMutexImpl() throws IOException {
		reader = Resources.getResourceAsReader("SqlMapConfig1.xml");
		sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
	}

	public void getLock(FlowLockInfoVO lockVO) throws SQLException {
		Map<String, String> map = new HashMap();
		map.put("crn", lockVO.getCrn());
		map.put("ipAddress", lockVO.getIpAddress());
		this.logger.debug(lockVO.getCrn() + " :: " + lockVO.getIpAddress());
		sqlMap.insert("mutex.INSERT_LOCK_INFO", map);
	}

	public void releaseLock(FlowLockInfoVO lockVO) throws SQLException {
		sqlMap.delete("mutex.RELEASE_LOCK", lockVO.getCrn());
	}

	public boolean JNDIlookupSuceeded(String ipAddr) throws IOException {
		boolean isLookupSuceeded = true;
		Object obj = null;

		try {
			ClassLoader classLoader = FlowMutexImpl.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream("atlas.properties");
			Properties prop = new Properties();
			prop.load(is);
			Properties props = new Properties();
			props.put("java.naming.factory.initial", prop.get("atlas.pramati.provider.url.sync"));
			props.put("java.naming.provider.url", ipAddr);
			Context initialContext = new InitialContext(props);
			obj = initialContext.lookup(ipAddr);
			if (ipAddr.equals("")) {
				this.logger.debug("Looked up the initial context");
			} else {
				this.logger.debug(ipAddr + " is bound to: " + obj);
			}
		} catch (NamingException var10) {
			this.logger.error("Encountered a naming exception");
		}

		if (null == obj) {
			isLookupSuceeded = false;
		}

		return isLookupSuceeded;
	}

	public FlowLockInfoVO checkForLock(FlowLockInfoVO lockVO) throws SQLException {
		this.logger.debug("crn is " + lockVO.getCrn());
		String lockMap = (String) sqlMap.queryForObject("mutex.GET_LOCK_INFO", lockVO.getCrn());
		this.logger.debug("Record Exist for the IP::::" + lockMap);
		FlowLockInfoVO lockInfoVO = null;
		if (null != lockMap && lockMap.trim().length() > 0) {
			lockInfoVO = new FlowLockInfoVO();
			lockInfoVO.setCrn(lockVO.getCrn());
			lockInfoVO.setIpAddress(lockMap);
		}

		return lockInfoVO;
	}
}