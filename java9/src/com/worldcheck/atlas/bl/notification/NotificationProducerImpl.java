package com.worldcheck.atlas.bl.notification;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class NotificationProducerImpl {
	private static Properties props = null;
	private static Context jndiContext = null;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.notification.NotificationProducerImpl");

	public NotificationProducerImpl() {
		props = getproperties();
		jndiContext = getcontext(props);
	}

	private void pushmessage(NotificationVO notificationVO) {
		try {
			if (jndiContext != null) {
				QueueConnection queueConnection = this.getconnection(jndiContext, props);
				QueueSession queueSession = null;

				try {
					if (queueConnection != null) {
						queueConnection.start();
						queueSession = queueConnection.createQueueSession(false, 1);
						Queue queue = this.getqueue(jndiContext, props);
						if (queue != null) {
							QueueSender queueSender = queueSession.createSender(queue);
							ObjectMessage objMessage = queueSession.createObjectMessage(notificationVO);
							queueSender.send(objMessage, 2, 4, 1000L);
						}
					}
				} catch (JMSException var15) {
					throw new CMSException(this.logger, var15);
				} finally {
					try {
						if (queueSession != null) {
							queueSession.close();
						}

						if (queueConnection != null) {
							queueConnection.close();
						}
					} catch (JMSException var14) {
						var14.printStackTrace();
					}

				}
			}
		} catch (Exception var17) {
			var17.printStackTrace();
		}

	}

	public static Properties getproperties() {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("atlas.properties");

		try {
			if (is != null) {
				props.load(is);
			}
		} catch (FileNotFoundException var3) {
			var3.printStackTrace();
		} catch (IOException var4) {
			var4.printStackTrace();
		}

		return props;
	}

	private static Context getcontext(Properties props) {
		Context jndiContext = null;
		Hashtable<String, String> env = new Hashtable();
		env.put("java.naming.factory.initial", props.getProperty("atlas.pramati.factory.initial"));
		env.put("java.naming.provider.url", props.getProperty("atlas.notification.provider.url"));

		try {
			jndiContext = new InitialContext(env);
		} catch (NamingException var4) {
			var4.printStackTrace();
		}

		return jndiContext;
	}

	private Queue getqueue(Context jndiContext, Properties props) throws CMSException {
		Queue queue = null;

		try {
			queue = (Queue) jndiContext.lookup(props.getProperty("jms.notification.queue"));
			return queue;
		} catch (NamingException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private QueueConnection getconnection(Context jndiContext, Properties props) throws CMSException {
		QueueConnection queueConnection = null;

		try {
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext
					.lookup(props.getProperty("QueueConnectionFactory"));
			queueConnection = queueConnectionFactory.createQueueConnection();
			return queueConnection;
		} catch (NamingException var6) {
			throw new CMSException(this.logger, var6);
		} catch (JMSException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void createSystemNotification(String message, String description, List<String> sendToUserList, String crn)
			throws CMSException {
		try {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setMessage(message);
			notificationVO.setSender(props.getProperty("atlas.system.user"));
			notificationVO.setDsName(props.getProperty("atlas.app.alert.jndiname"));
			notificationVO.setProcessName(props.getProperty("atlas.notification.process.name"));
			notificationVO.setDescription(description);
			notificationVO.setSendToUserList(sendToUserList);
			notificationVO.setCrn(crn);
			if (null != sendToUserList && sendToUserList.size() > 0) {
				this.pushmessage(notificationVO);
			}

		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}