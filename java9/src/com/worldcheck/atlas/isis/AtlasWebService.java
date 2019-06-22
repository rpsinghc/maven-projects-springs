package com.worldcheck.atlas.isis;

import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportVO;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AtlasWebService extends Remote {
	CaseResultVO createOnlineCase(CaseDetailsVO var1) throws RemoteException;

	CaseResultVO updateOnlineCase(CaseDetailsVO var1) throws RemoteException;

	DownloadOnlineReportResultVO downloadOnlineReport(DownloadOnlineReportVO var1) throws RemoteException;

	CaseResultVO cancelOnlineOrder(String var1) throws RemoteException;

	CaseResultVO updateClientReferanceNumber(String var1, String var2) throws RemoteException;

	String pingAtlas() throws RemoteException;
}