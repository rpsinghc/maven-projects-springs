package com.worldcheck.atlas.isis.util;

public class WebServicePropertyReaderUtil {
	private String atlasWebServiceURL;
	private String isisWebServiceURL;
	private String ftpServerAddress;
	private String ftpServerPortNo;
	private String ftpServerUserId;
	private String ftpServerPassword;
	private String atlasTempFilesFolder;
	private int retryCount;
	private int retryGapTime;
	private int transcationQWaitTime;
	private String atlasAuthUser;
	private String atlasAuthPassword;
	private String mailRequired;
	private String smtpHost;
	private String smtpPort;
	private String smtpAuthRequied;
	private String smtpMailAuthUser;
	private String smtpMailAuthPwd;
	private String mailFrom;
	private String mailTo;
	private String mailCC;
	private int ftpFailedFilesRetryCount;
	private long ftpFailedFilesRetryGapTime;
	private String isisAuthUser;
	private String isisAuthPassword;
	private String webServiceBIManagerId;
	private int threadPoolSize;
	private String webServiceCaseCreatorId;
	private String webServiceMailSmtpStarttlsEnable;
	private boolean ftpMonitoring;

	public String getAtlasWebServiceURL() {
		return this.atlasWebServiceURL;
	}

	public void setAtlasWebServiceURL(String atlasWebServiceURL) {
		this.atlasWebServiceURL = atlasWebServiceURL;
	}

	public String getIsisWebServiceURL() {
		return this.isisWebServiceURL;
	}

	public void setIsisWebServiceURL(String isisWebServiceURL) {
		this.isisWebServiceURL = isisWebServiceURL;
	}

	public String getFtpServerAddress() {
		return this.ftpServerAddress;
	}

	public void setFtpServerAddress(String ftpServerAddress) {
		this.ftpServerAddress = ftpServerAddress;
	}

	public String getFtpServerPortNo() {
		return this.ftpServerPortNo;
	}

	public void setFtpServerPortNo(String ftpServerPortNo) {
		this.ftpServerPortNo = ftpServerPortNo;
	}

	public String getFtpServerUserId() {
		return this.ftpServerUserId;
	}

	public void setFtpServerUserId(String ftpServerUserId) {
		this.ftpServerUserId = ftpServerUserId;
	}

	public String getFtpServerPassword() {
		return this.ftpServerPassword;
	}

	public void setFtpServerPassword(String ftpServerPassword) {
		this.ftpServerPassword = ftpServerPassword;
	}

	public String getAtlasTempFilesFolder() {
		return this.atlasTempFilesFolder;
	}

	public void setAtlasTempFilesFolder(String atlasTempFilesFolder) {
		this.atlasTempFilesFolder = atlasTempFilesFolder;
	}

	public int getRetryCount() {
		return this.retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getRetryGapTime() {
		return this.retryGapTime;
	}

	public void setRetryGapTime(int retryGapTime) {
		this.retryGapTime = retryGapTime;
	}

	public int getTranscationQWaitTime() {
		return this.transcationQWaitTime;
	}

	public void setTranscationQWaitTime(int transcationQWaitTime) {
		this.transcationQWaitTime = transcationQWaitTime;
	}

	public String getAtlasAuthUser() {
		return this.atlasAuthUser;
	}

	public void setAtlasAuthUser(String authUser) {
		this.atlasAuthUser = authUser;
	}

	public String getAtlasAuthPassword() {
		return this.atlasAuthPassword;
	}

	public void setAtlasAuthPassword(String authPassword) {
		this.atlasAuthPassword = authPassword;
	}

	public String getMailRequired() {
		return this.mailRequired;
	}

	public void setMailRequired(String mailRequired) {
		this.mailRequired = mailRequired;
	}

	public String getSmtpHost() {
		return this.smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return this.smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpAuthRequied() {
		return this.smtpAuthRequied;
	}

	public void setSmtpAuthRequied(String smtpAuthRequied) {
		this.smtpAuthRequied = smtpAuthRequied;
	}

	public String getSmtpMailAuthUser() {
		return this.smtpMailAuthUser;
	}

	public void setSmtpMailAuthUser(String smtpMailAuthUser) {
		this.smtpMailAuthUser = smtpMailAuthUser;
	}

	public String getSmtpMailAuthPwd() {
		return this.smtpMailAuthPwd;
	}

	public void setSmtpMailAuthPwd(String smtpMailAuthPwd) {
		this.smtpMailAuthPwd = smtpMailAuthPwd;
	}

	public String getMailFrom() {
		return this.mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return this.mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailCC() {
		return this.mailCC;
	}

	public void setMailCC(String mailCC) {
		this.mailCC = mailCC;
	}

	public int getFtpFailedFilesRetryCount() {
		return this.ftpFailedFilesRetryCount;
	}

	public void setFtpFailedFilesRetryCount(int ftpFailedFilesRetryCount) {
		this.ftpFailedFilesRetryCount = ftpFailedFilesRetryCount;
	}

	public long getFtpFailedFilesRetryGapTime() {
		return this.ftpFailedFilesRetryGapTime;
	}

	public void setFtpFailedFilesRetryGapTime(long ftpFailedFilesRetryGapTime) {
		this.ftpFailedFilesRetryGapTime = ftpFailedFilesRetryGapTime;
	}

	public String getIsisAuthUser() {
		return this.isisAuthUser;
	}

	public void setIsisAuthUser(String isisAuthUser) {
		this.isisAuthUser = isisAuthUser;
	}

	public String getIsisAuthPassword() {
		return this.isisAuthPassword;
	}

	public void setIsisAuthPassword(String isisAuthPassword) {
		this.isisAuthPassword = isisAuthPassword;
	}

	public String getWebServiceBIManagerId() {
		return this.webServiceBIManagerId;
	}

	public void setWebServiceBIManagerId(String webServiceBIManagerId) {
		this.webServiceBIManagerId = webServiceBIManagerId;
	}

	public int getThreadPoolSize() {
		return this.threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public String getWebServiceCaseCreatorId() {
		return this.webServiceCaseCreatorId;
	}

	public void setWebServiceCaseCreatorId(String webServiceCaseCreatorId) {
		this.webServiceCaseCreatorId = webServiceCaseCreatorId;
	}

	public String getWebServiceMailSmtpStarttlsEnable() {
		return this.webServiceMailSmtpStarttlsEnable;
	}

	public void setWebServiceMailSmtpStarttlsEnable(String webServiceMailSmtpStarttlsEnable) {
		this.webServiceMailSmtpStarttlsEnable = webServiceMailSmtpStarttlsEnable;
	}

	public void setFtpMonitoring(boolean ftpMonitoring) {
		this.ftpMonitoring = ftpMonitoring;
	}

	public boolean isFtpMonitoring() {
		return this.ftpMonitoring;
	}
}