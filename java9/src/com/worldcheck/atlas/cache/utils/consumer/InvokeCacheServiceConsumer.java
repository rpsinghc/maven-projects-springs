package com.worldcheck.atlas.cache.utils.consumer;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class InvokeCacheServiceConsumer implements MessageListener, MessageDrivenBean {
	private static final long serialVersionUID = 1L;
	MessageDrivenContext mdbContext;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.cache.utils.consumer.InvokeCacheServiceConsumer");

	public void onMessage(Message message) {
		try {
			ObjectMessage objMessage = (ObjectMessage) message;
			this.logger.debug("inside OnMessage method");
			HashMap<String, Object> nodeMap = (HashMap) objMessage.getObject();
			this.logger.debug("After getting HashMap Object from message");
			this.executeServlet(nodeMap);
			this.logger.debug("Servlet executed successfully...");
		} catch (JMSException var4) {
			this.logger.error("JMSException occured. Error is " + var4);
		} catch (RuntimeException var5) {
			this.logger.error("RuntimeException occured. Error is " + var5);
		} catch (Exception var6) {
			this.logger.error("An unexpected error occured. Error is " + var6);
		}

	}

	public void ejbCreate() throws CreateException {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void init() throws Exception {
		this.logger.debug("In side AtlasQueueListener init().... !");
	}

	public void ejbRemove() throws EJBException {
		this.mdbContext = null;
	}

	public void setMessageDrivenContext(MessageDrivenContext mdbContext) throws EJBException {
		this.mdbContext = mdbContext;
	}

	private void executeServlet(HashMap<String, Object> hashMap) {
		InputStream is = null;
		String token = "";
		String userId = "";
		Properties props = new Properties();
		InputStream ipStream = ClassLoader.getSystemResourceAsStream("sbmjndi.properties");
		String action = (String) hashMap.get("action");
		this.logger.debug("action :: " + action + " :: hashMap is " + hashMap.get("map"));
		if (null != hashMap.get("map")) {
			HashMap mapForTTAndACLToken;
			if (!action.equals("updateTTToken") && !action.equals("deleteTTToken")) {
				mapForTTAndACLToken = (HashMap) hashMap.get("map");
				Iterator<String> iterator = mapForTTAndACLToken.keySet().iterator();
				if (iterator.hasNext()) {
					userId = (String) iterator.next();
				}

				token = (String) mapForTTAndACLToken.get(userId);
			} else {
				mapForTTAndACLToken = (HashMap) hashMap.get("map");
				userId = (String) mapForTTAndACLToken.get("userId");
				if (action.equals("updateTTToken")) {
					token = (String) mapForTTAndACLToken.get("trackerId");
				} else {
					token = "deleteTTToken";
				}
			}
		} else {
			token = (String) hashMap.get("token");
		}

		this.logger.debug("tokens are " + token + " :: " + userId + " :: " + " :: Action is :: " + action);

		try {
			props.load(ipStream);
			String portNumber = props.getProperty("sbm.pramati.web.http.port");
			this.logger.debug("port number is " + portNumber);
			URL u = new URL("http://127.0.0.1:" + portNumber + "/sbm/bpmportal/atlas/UpdateCache.updateCache?" + "token"
					+ "=" + token + "&" + "userId" + "=" + userId + "&" + "action" + "=" + action);
			is = u.openStream();
			DataInputStream dis = new DataInputStream(new BufferedInputStream(is));

			String temp;
			while ((temp = dis.readLine()) != null) {
				this.logger.debug(temp);
			}
		} catch (MalformedURLException var23) {
			this.logger.error("Error occured in servlet calling .. Exception is " + var23);
		} catch (IOException var24) {
			this.logger.error("Error occured in calling servlet " + var24);
		} finally {
			try {
				is.close();
			} catch (IOException var22) {
				this.logger.warn("Error while closing input stream " + var22);
			}

		}

	}
}