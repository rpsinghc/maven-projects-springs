package com.integrascreen.orders;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Ocrs_wsSoap extends Remote {
	void UFP(String var1) throws RemoteException;

	ResponseVO CBD(CBDVO var1) throws RemoteException;

	ResponseVO US(USVO var1) throws RemoteException;

	String pingISIS() throws RemoteException;

	ResponseVO UPCRN(UPCRNVO var1) throws RemoteException;

	void currencyMaster(CurrencyMasterVO var1) throws RemoteException;

	void countryMaster(CountryMasterVO var1) throws RemoteException;

	void industryMaster(IndustryMasterVO var1) throws RemoteException;

	void riskMaster(RiskMasterVO var1) throws RemoteException;

	void REMaster(REMasterVO var1) throws RemoteException;

	void reportTypeMaster(ReportTypeMasterVO var1) throws RemoteException;

	ResponseVO UPMaster(UPMasterVO var1) throws RemoteException;

	ResponseVO UPSubject(UPSubjectVO var1) throws RemoteException;
}