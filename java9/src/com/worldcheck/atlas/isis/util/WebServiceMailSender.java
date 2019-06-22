package com.worldcheck.atlas.isis.util;

import com.worldcheck.atlas.isis.util.WebServiceMailSender.1;
import com.worldcheck.atlas.isis.util.WebServiceMailSender.SMTPAuthenticator;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class WebServiceMailSender {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.util.WebServiceMailSender");
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public void sendMail(String subject, String message) {
		this.logger.debug("Inside sendMail method..");

		try {
			if (this.webServicePropertyReaderUtil.getMailRequired().equalsIgnoreCase("true")) {
				this.postMail(this.convertCommaStringToArray(this.webServicePropertyReaderUtil.getMailTo()), subject,
						message, this.webServicePropertyReaderUtil.getMailFrom());
				this.logger.debug("Sucessfully Sent mail to All Users..");
			} else {
				this.logger.debug("Mail required flag is not true... So mail functionlity is disabled.");
			}
		} catch (Exception var4) {
			this.logger.error("Inside Error::::::::::::" + this.getStackTraceAsString(var4));
		}

	}

	public void postMail(String[] recipients, String subject, String message, String from) throws MessagingException {
      this.logger.debug("Inside postMail method..");

      try {
         boolean debug = false;
         this.logger.debug("webServicePropertyReaderUtil:::::" + this.webServicePropertyReaderUtil);
         this.logger.debug("webServicePropertyReaderUtil.getSmtpHost()::::::" + this.webServicePropertyReaderUtil.getSmtpHost());
         this.logger.debug("webServicePropertyReaderUtil.getSmtpAuthRequied()" + this.webServicePropertyReaderUtil.getSmtpAuthRequied());
         this.logger.debug("webServicePropertyReaderUtil.getSmtpPort()::::::" + this.webServicePropertyReaderUtil.getSmtpPort());
         this.logger.debug("webServicePropertyReaderUtil.getWebServiceMailSmtpStarttlsEnable()::::::" + this.webServicePropertyReaderUtil.getWebServiceMailSmtpStarttlsEnable());
         Properties props = new Properties();
         props.put("mail.smtp.host", this.webServicePropertyReaderUtil.getSmtpHost());
         props.put("mail.smtp.auth", this.webServicePropertyReaderUtil.getSmtpAuthRequied());
         props.put("mail.smtp.port", this.webServicePropertyReaderUtil.getSmtpPort());
         props.put("mail.smtp.starttls.enable", this.webServicePropertyReaderUtil.getWebServiceMailSmtpStarttlsEnable());
         Authenticator auth = new SMTPAuthenticator(this, (1)null);
         this.logger.debug("auth is" + auth);
         Session session = Session.getInstance(props, auth);
         this.logger.debug("session is:::::" + session);
         session.setDebug(debug);
         Message msg = new MimeMessage(session);
         InternetAddress addressFrom = new InternetAddress(from);
         msg.setFrom(addressFrom);
         InternetAddress[] addressTo = new InternetAddress[recipients.length];

         for(int i = 0; i < recipients.length; ++i) {
            addressTo[i] = new InternetAddress(recipients[i]);
         }

         msg.setRecipients(RecipientType.TO, addressTo);
         msg.setSubject(subject);
         msg.setContent(message, "text/plain");
         Transport.send(msg);
      } catch (Exception var13) {
         this.logger.error("Inside Error::::::::::::" + this.getStackTraceAsString(var13));
      }

   }

	private String[] convertCommaStringToArray(String commaSeparatedString) {
		this.logger.debug("Inside convertCommaStringToArray method of mail sender");
		String[] stringObj = null;
		if (!commaSeparatedString.equals("") && commaSeparatedString != null) {
			StringTokenizer reTokenizer = new StringTokenizer(commaSeparatedString, ",");
			stringObj = new String[reTokenizer.countTokens()];

			for (int index = 0; reTokenizer.hasMoreElements(); ++index) {
				stringObj[index] = (String) reTokenizer.nextElement();
			}
		}

		return stringObj;
	}

	public String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}
}