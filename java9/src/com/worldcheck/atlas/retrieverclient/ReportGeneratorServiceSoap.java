package com.worldcheck.atlas.retrieverclient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReportGeneratorServiceSoap extends Remote {
	String generateReport(String var1, long var2, ReportType var4, OutputFormat var5, DocumentProperties var6)
			throws RemoteException;

	boolean generateAndUpload(String var1, long var2, ReportType var4, OutputFormat var5, DocumentProperties var6,
			String var7, String var8, String var9) throws RemoteException;

	String generateReportForAtlas(int[] var1, String var2, String var3, String var4) throws RemoteException;
}