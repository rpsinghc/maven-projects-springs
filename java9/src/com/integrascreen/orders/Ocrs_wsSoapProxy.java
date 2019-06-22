package com.integrascreen.orders;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class Ocrs_wsSoapProxy implements Ocrs_wsSoap {
	private String _endpoint = null;
	private Ocrs_wsSoap ocrs_wsSoap = null;

	public Ocrs_wsSoapProxy() {
		this._initOcrs_wsSoapProxy();
	}

	public Ocrs_wsSoapProxy(String endpoint) {
		this._endpoint = endpoint;
		this._initOcrs_wsSoapProxy();
	}

	private void _initOcrs_wsSoapProxy() {
		try {
			this.ocrs_wsSoap = (new Ocrs_wsLocator()).getocrs_wsSoap();
			if (this.ocrs_wsSoap != null) {
				if (this._endpoint != null) {
					((Stub) this.ocrs_wsSoap)._setProperty("javax.xml.rpc.service.endpoint.address", this._endpoint);
				} else {
					this._endpoint = (String) ((Stub) this.ocrs_wsSoap)
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
		if (this.ocrs_wsSoap != null) {
			((Stub) this.ocrs_wsSoap)._setProperty("javax.xml.rpc.service.endpoint.address", this._endpoint);
		}

	}

	public Ocrs_wsSoap getOcrs_wsSoap() {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap;
	}

	public void UFP(String param) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.UFP(param);
	}

	public ResponseVO CBD(CBDVO CBDVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap.CBD(CBDVO);
	}

	public ResponseVO US(USVO USVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap.US(USVO);
	}

	public String pingISIS() throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap.pingISIS();
	}

	public ResponseVO UPCRN(UPCRNVO UPCRNVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap.UPCRN(UPCRNVO);
	}

	public void currencyMaster(CurrencyMasterVO currencyMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.currencyMaster(currencyMasterVO);
	}

	public void countryMaster(CountryMasterVO countryMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.countryMaster(countryMasterVO);
	}

	public void industryMaster(IndustryMasterVO industryMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.industryMaster(industryMasterVO);
	}

	public void riskMaster(RiskMasterVO riskMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.riskMaster(riskMasterVO);
	}

	public void REMaster(REMasterVO REMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.REMaster(REMasterVO);
	}

	public void reportTypeMaster(ReportTypeMasterVO reportTypeMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		this.ocrs_wsSoap.reportTypeMaster(reportTypeMasterVO);
	}

	public ResponseVO UPMaster(UPMasterVO UPMasterVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap.UPMaster(UPMasterVO);
	}

	public ResponseVO UPSubject(UPSubjectVO UPSubjectVO) throws RemoteException {
		if (this.ocrs_wsSoap == null) {
			this._initOcrs_wsSoapProxy();
		}

		return this.ocrs_wsSoap.UPSubject(UPSubjectVO);
	}
}