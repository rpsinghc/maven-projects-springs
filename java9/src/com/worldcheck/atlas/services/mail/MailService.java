package com.worldcheck.atlas.services.mail;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.mail.MailService.SMTPAuthenticator;
import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.mail.MailService");
	private String email;
	private String password;
	private String host;
	private String port;
	private String authentication;
	private String starttlsProp;
	private static final String SMTP_USER = "mail.smtp.user";
	private static final String SMTP_HOST = "mail.smtp.host";
	private static final String SMTP_PORT = "mail.smtp.port";
	private static final String SMTP_AUTH = "mail.smtp.auth";
	private static final String SMTP_STATTLLS = "mail.smtp.starttls.enable";
	private static final String PWD_STR = "TTKjH27v8s3DwwhvBiQZZA==";

	public void sendEmail(String m_Subject, String m_To, String m_Text) throws CMSException {
		try {
			Authenticator auth = new SMTPAuthenticator(this, (SMTPAuthenticator) null);
			Session session = Session.getInstance(this.getMailProperties(), auth);
			MimeMessage msg = new MimeMessage(session);
			this.logger.debug("m_Subject " + m_Subject);
			this.logger.debug("email " + this.email);
			this.logger.debug("m_To " + m_To);
			msg.setText(m_Text);
			msg.setSubject(m_Subject);
			msg.setFrom(new InternetAddress(this.email));
			msg.addRecipient(RecipientType.TO, new InternetAddress(m_To));
			Transport.send(msg);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void sendEmail(String m_Subject, String m_To, String m_BCC, String m_Text) throws CMSException {
		try {
			Authenticator auth = new SMTPAuthenticator(this, (SMTPAuthenticator) null);
			Session session = Session.getInstance(this.getMailProperties(), auth);
			MimeMessage msg = new MimeMessage(session);
			this.logger.debug("m_Subject " + m_Subject);
			this.logger.debug("email " + this.email);
			this.logger.debug("m_To " + m_To);
			msg.setText(m_Text);
			msg.setSubject(m_Subject);
			msg.setFrom(new InternetAddress(this.email));
			msg.addRecipient(RecipientType.TO, new InternetAddress(m_To));
			msg.addRecipient(RecipientType.BCC, new InternetAddress(m_BCC));
			Transport.send(msg);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public void sendMassEmail(String m_Subject, String m_To, String m_Text) throws CMSException {
		try {
			Authenticator auth = new SMTPAuthenticator(this, (SMTPAuthenticator) null);
			Session session = Session.getInstance(this.getMailProperties(), auth);
			MimeMessage msg = new MimeMessage(session);
			msg.setText(m_Text);
			msg.setSubject(m_Subject);
			msg.setFrom(new InternetAddress(this.email));
			msg.addRecipients(RecipientType.TO, m_To);
			Transport.send(msg);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private Properties getMailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.user", this.email);
		props.put("mail.smtp.host", this.host);
		props.put("mail.smtp.port", this.port);
		props.put("mail.smtp.starttls.enable", this.starttlsProp);
		props.put("mail.smtp.auth", this.authentication);
		return props;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public void setStarttlsProp(String starttlsProp) {
		this.starttlsProp = starttlsProp;
	}

	public boolean sendEmailOnCaseCreation(String emailBody, String emailSubject, String host,
			InternetAddress[] toEmail, InternetAddress[] ccEmail, InternetAddress[] bccEmail, String from)
			throws Exception {
		boolean isMailSent = false;
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		InetAddress inet = InetAddress.getByName(host);
		Session session = Session.getDefaultInstance(properties);
		if (inet.isReachable(2000)) {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipients(RecipientType.TO, toEmail);
			if (ccEmail.length > 0) {
				message.addRecipients(RecipientType.CC, ccEmail);
			}

			if (bccEmail.length > 0) {
				message.addRecipients(RecipientType.BCC, bccEmail);
			}

			message.setSentDate(new Date());
			message.setSubject(emailSubject);
			message.setContent(emailBody, "text/html; charset=\"utf-8\"");
			Transport.send(message);
			isMailSent = true;
			this.logger.debug("Email TEXT******" + emailBody);
			this.logger.debug("Messages successfully sent on case creation");
		} else {
			this.logger.debug("Host Unreachable.");
		}

		return isMailSent;
	}
}