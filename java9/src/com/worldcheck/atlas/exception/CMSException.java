package com.worldcheck.atlas.exception;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;
import org.springframework.jdbc.UncategorizedSQLException;

public class CMSException extends Exception {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.exception.CMSException");
	private static final long serialVersionUID = -159131603846050015L;
	private String errorCode = "CMS_001";
	private String message;
	private static HashMap messages;
	private Throwable cause;
	private static Properties props = null;

	public Throwable getCause() {
		return this.cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public CMSException() {
	}

	public CMSException(String errorCode) {
		super((String) messages.get(errorCode));
		this.errorCode = errorCode;
		this.message = (String) messages.get(errorCode);
		this.logger.debug("New CMSException created for code:" + this.errorCode);
	}

	public CMSException(String errorCode, String errorMessage) {
		String msg = null;
		if (errorCode != null) {
			msg = (String) messages.get(errorCode);
		}

		if (msg != null) {
			this.errorCode = errorCode;
			this.message = new String(msg);
			this.logger.debug("New CMSException created for code:" + this.errorCode);
		} else {
			this.message = errorMessage;
		}

	}

	public CMSException(String errorCode, Exception e) {
		super(e.getMessage() + (String) messages.get(errorCode));
		this.errorCode = errorCode;
		this.message = e.getMessage() + (String) messages.get(errorCode);
		this.logger.debug("New CMSException created for code:" + this.errorCode);
	}

	public CMSException(ILogProducer logger, Exception ex) {
		if (ex instanceof CMSException) {
			CMSException cmse = (CMSException) ex;
			this.errorCode = cmse.getErrorCode();
			this.cause = cmse.getCause();
		} else {
			String exceptName = ex.getClass().getSimpleName();
			String methodName = "get" + exceptName;
			Class clsObj = CMSErrorCodes.class;

			try {
				Object clsIns = clsObj.newInstance();
				Method fooMethod = clsObj.getMethod(methodName);
				String errorCode = (String) fooMethod.invoke(clsIns);
				if (null != errorCode && errorCode.trim().length() > 0) {
					this.errorCode = errorCode;
				}

				if (ex instanceof UncategorizedSQLException) {
					UncategorizedSQLException usqle = (UncategorizedSQLException) ex;
					logger.debug("usqle.getSQLException().getErrorCode() :: " + usqle.getSQLException().getErrorCode());
					String errorCodeNumber;
					if (null == props) {
						errorCodeNumber = "20004";
					} else {
						errorCodeNumber = props.getProperty("atlas.interimcycle.errorcode");
					}

					if (usqle.getSQLException().getErrorCode() == Integer.parseInt(errorCodeNumber)) {
						logger.debug("setting cause in this");
						this.cause = usqle.getSQLException();
					}
				}

				logger.debug("error code of the exception :: " + this.errorCode);
			} catch (InstantiationException var19) {
				var19.printStackTrace();
			} catch (IllegalAccessException var20) {
				var20.printStackTrace();
			} catch (SecurityException var21) {
				var21.printStackTrace();
			} catch (NoSuchMethodException var22) {
				logger.debug("Setting default error code in catch of NoSuchMethodException");
				this.errorCode = "CMS_001";
			} catch (IllegalArgumentException var23) {
				var23.printStackTrace();
			} catch (InvocationTargetException var24) {
				var24.printStackTrace();
			} finally {
				logger.error(ex);
			}
		}

	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	static {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("atlas.properties");

		try {
			if (is != null) {
				props.load(is);
				System.out.println("properties loaded sucessfully...");
			}
		} catch (FileNotFoundException var3) {
			System.out.println("Properties Filenot found . Error is " + var3);
		} catch (IOException var4) {
			System.out.println("IO exception occured " + var4);
		}

	}
}