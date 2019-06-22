package com.worldcheck.atlas.retrieverclient;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class ReportGeneratorServiceSoapProxy implements ReportGeneratorServiceSoap {
	private String _endpoint = null;
	private ReportGeneratorServiceSoap reportGeneratorServiceSoap = null;

	public ReportGeneratorServiceSoapProxy() {
		this._initReportGeneratorServiceSoapProxy();
	}

	public ReportGeneratorServiceSoapProxy(String endpoint) {
		this._endpoint = endpoint;
		this._initReportGeneratorServiceSoapProxy();
	}

	private void _initReportGeneratorServiceSoapProxy() {
		try {
			this.reportGeneratorServiceSoap = (new ReportGeneratorServiceLocator()).getReportGeneratorServiceSoap();
			if (this.reportGeneratorServiceSoap != null) {
				if (this._endpoint != null) {
					((Stub) this.reportGeneratorServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address",
							this._endpoint);
				} else {
					this._endpoint = (String) ((Stub) this.reportGeneratorServiceSoap)
							._getProperty("javax.xml.rpc.service.endpoint.address");
				}
			}
		} catch (ServiceException var2) {
			;
		}

	}

	public String getEndpoint() {
		return this._endpoint;
	}

	public void setEndpoint(String endpoint) {
		this._endpoint = endpoint;
		if (this.reportGeneratorServiceSoap != null) {
			((Stub) this.reportGeneratorServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address",
					this._endpoint);
		}

	}

	public ReportGeneratorServiceSoap getReportGeneratorServiceSoap() {
		if (this.reportGeneratorServiceSoap == null) {
			this._initReportGeneratorServiceSoapProxy();
		}

		return this.reportGeneratorServiceSoap;
	}

	public String generateReport(String crn, long processCycle, ReportType reportType, OutputFormat outputFormat,
			DocumentProperties documentProperties) throws RemoteException {
		if (this.reportGeneratorServiceSoap == null) {
			this._initReportGeneratorServiceSoapProxy();
		}

		return this.reportGeneratorServiceSoap.generateReport(crn, processCycle, reportType, outputFormat,
				documentProperties);
	}

	public boolean generateAndUpload(String crn, long processCycle, ReportType reportType, OutputFormat outputFormat,
			DocumentProperties documentProperties, String emailId, String userName, String taskPid)
			throws RemoteException {
		if (this.reportGeneratorServiceSoap == null) {
			this._initReportGeneratorServiceSoapProxy();
		}

		return this.reportGeneratorServiceSoap.generateAndUpload(crn, processCycle, reportType, outputFormat,
				documentProperties, emailId, userName, taskPid);
	}

	public String generateReportForAtlas(int[] countryId, String subjectType, String fileName, String fileType)
			throws RemoteException {
		if (this.reportGeneratorServiceSoap == null) {
			this._initReportGeneratorServiceSoapProxy();
		}

		return this.reportGeneratorServiceSoap.generateReportForAtlas(countryId, subjectType, fileName, fileType);
	}
}