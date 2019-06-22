package com.worldcheck.atlas.sbm.queryService;

import com.savvion.sbm.bizlogic.client.queryservice.QSUtil;
import com.savvion.sbm.bizlogic.client.queryservice.QueryService;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.SBMSessionManager;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SBMQueryManager {
	private static ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.queryService.SBMQueryManager");
	SBMQueryManager manager;
	SBMSessionManager sbmSessionManager;
	private static String java_naming_factory_initial;
	private static String java_naming_provider_url;
	private static String java_naming_security_principal;
	private static String java_naming_security_credentials;
	private static String savvion_datasource;

	public static void setLogger(ILogProducer logger) {
		logger = logger;
	}

	public void setManager(SBMQueryManager manager) {
		this.manager = manager;
	}

	public void setSbmSessionManager(SBMSessionManager sbmSessionManager) {
		this.sbmSessionManager = sbmSessionManager;
	}

	public void setJava_naming_factory_initial(String javaNamingFactoryInitial) {
		java_naming_factory_initial = javaNamingFactoryInitial;
	}

	public void setJava_naming_provider_url(String javaNamingProviderUrl) {
		java_naming_provider_url = javaNamingProviderUrl;
	}

	public void setJava_naming_security_principal(String javaNamingSecurityPrincipal) {
		java_naming_security_principal = javaNamingSecurityPrincipal;
	}

	public void setJava_naming_security_credentials(String javaNamingSecurityCredentials) {
		java_naming_security_credentials = javaNamingSecurityCredentials;
	}

	public void setSavvion_datasource(String savvionDatasource) {
		savvion_datasource = savvionDatasource;
	}

	public static QueryService getQueryService(Session session) throws CMSException {
		QueryService qs = null;

		try {
			logger.debug("QSUtil :: " + getDataSource());
			QSUtil.setDataSource(getDataSource());
			qs = new QueryService(session);
			if (qs == null) {
				throw new Exception();
			}
		} catch (Exception var3) {
			throw new CMSException(logger, var3);
		}

		logger.debug("QueryService getQueryService() OUT");
		return qs;
	}

	public static QueryService getQueryService() throws Exception {
		QueryService qs = null;
		logger.debug("QueryService getQueryService() IN");

		try {
			qs = createQueryService();
			if (qs == null) {
				throw new CMSException("Savvion Server(s) not yet started. Please check if it is started");
			}
		} catch (Exception var2) {
			throw new CMSException(logger, var2);
		}

		logger.debug("QueryService getQueryService() OUT");
		return qs;
	}

	private static QueryService createQueryService() throws CMSException {
		QueryService qs = null;
		logger.debug("createQueryService() - start");

		try {
			SBMSessionManager sbmSessionManager = new SBMSessionManager();
			QSUtil.setDataSource(getDataSource());
			qs = new QueryService(sbmSessionManager.getSession());
		} catch (Exception var2) {
			throw new CMSException();
		}

		logger.debug("createQueryService() - end");
		return qs;
	}

	private static DataSource getDataSource() throws NamingException, CMSException {
		DataSource mds = null;
		Properties properties = new Properties();
		String datasource = savvion_datasource;

		try {
			properties.setProperty("com.pramati.naming.client.PramatiClientContextFactory",
					java_naming_factory_initial);
			properties.setProperty("java.naming.provider.url", java_naming_provider_url);
			properties.setProperty("java.naming.security.principal", java_naming_security_principal);
			properties.setProperty("java.naming.security.credentials", java_naming_security_credentials);
		} catch (Exception var5) {
			throw new CMSException(logger, var5);
		}

		InitialContext initialcontext = new InitialContext(properties);
		mds = (DataSource) initialcontext.lookup(datasource);
		return mds;
	}
}