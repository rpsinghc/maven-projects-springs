package com.worldcheck.atlas.sbm;

import com.savvion.sbm.bizlogic.server.ejb.BLServer;
import com.savvion.sbm.bizlogic.server.ejb.BLServerHome;
import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.util.PService;
import com.savvion.sbm.util.SBMHomeFactory;
import com.tdiinc.userManager.JDBCRealm;
import com.tdiinc.userManager.User;
import com.tdiinc.userManager.UserManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.ejb.CreateException;
import javax.naming.NamingException;

public class SBMSessionManager {
	private Session userSession = null;
	private Map<String, String> sbmPropertyMap = null;
	private BLServer blServer;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.SBMSessionManager");

	private void loadProperties() throws CMSException {
		try {
			ClassLoader classLoader = SBMSessionManager.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream("atlas.properties");
			Properties prop = new Properties();
			prop.load(is);
			this.sbmPropertyMap = new HashMap();
			this.sbmPropertyMap.put("INITIAL_FACTORY", prop.getProperty("atlas.pramati.factory.initial"));
			this.sbmPropertyMap.put("PROVIDER_URL", prop.getProperty("atlas.pramati.provider.url"));
			this.sbmPropertyMap.put("PRINCIPAL", prop.getProperty("atlas.pramati.principal"));
			this.sbmPropertyMap.put("CREDENTIALS",
					PService.self().decrypt(prop.getProperty("atlas.pramati.credentials")));
			this.sbmPropertyMap.put("JNDI_NAME", prop.getProperty("atlas.pramati.provider.jndiname"));
			this.logger.debug("Properties Successfully loaded.....");
		} catch (FileNotFoundException var4) {
			throw new CMSException(this.logger, var4);
		} catch (IOException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public Session getSession(String userId) throws CMSException {
		try {
			this.loadProperties();
			User user = UserManager.getUser(userId);
			String pwd = user.getAttribute("password");
			pwd = PService.self().decrypt(pwd);
			BLServerHome server_home = (BLServerHome) SBMHomeFactory.lookupHome(this.sbmPropertyMap,
					BLServerHome.class);
			this.blServer = server_home.create();
			this.userSession = this.blServer.connect(userId, pwd);
			this.logger.debug("Session sucessfully created for user " + userId);
		} catch (NamingException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IOException var6) {
			throw new CMSException(this.logger, var6);
		} catch (CreateException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		return this.userSession;
	}

	public void closeSession(Session userSession) throws CMSException {
		try {
			if (this.blServer.isSessionValid(userSession)) {
				this.blServer.disConnect(userSession);
				this.logger.debug("Session closed");
			} else {
				this.logger.debug("blserver session is invalid");
			}

		} catch (RemoteException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Session getSession() throws CMSException {
		try {
			this.loadProperties();
			JDBCRealm jdbRealm = new JDBCRealm();
			String adminId = null;
			String adminUserPwdInDB = null;
			String[] allUsers = jdbRealm.getUserNames();
			int i = 0;

			while (true) {
				if (i < allUsers.length) {
					if (UserManager.getUserIDInDB(allUsers[i]) != 1) {
						++i;
						continue;
					}

					adminId = allUsers[i];
					adminUserPwdInDB = UserManager.getUser(allUsers[i]).getAttribute("PASSWORD");
					adminUserPwdInDB = PService.self().decrypt(adminUserPwdInDB);
				}

				this.logger.debug("Admin user is " + adminId);
				BLServerHome server_home = (BLServerHome) SBMHomeFactory.lookupHome(this.sbmPropertyMap,
						BLServerHome.class);
				this.blServer = server_home.create();
				this.userSession = this.blServer.connect(adminId, adminUserPwdInDB);
				this.logger.debug("Succesfully connected to Server session Id is " + this.userSession.getID());
				return this.userSession;
			}
		} catch (NamingException var8) {
			throw new CMSException(this.logger, var8);
		} catch (RemoteException var9) {
			throw new CMSException(this.logger, var9);
		} catch (CreateException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}
}