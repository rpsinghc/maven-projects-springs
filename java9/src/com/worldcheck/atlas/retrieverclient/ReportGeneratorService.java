package com.worldcheck.atlas.retrieverclient;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ReportGeneratorService extends Service {
	String getReportGeneratorServiceSoapAddress();

	ReportGeneratorServiceSoap getReportGeneratorServiceSoap() throws ServiceException;

	ReportGeneratorServiceSoap getReportGeneratorServiceSoap(URL var1) throws ServiceException;

	String getReportGeneratorServiceSoap12Address();

	ReportGeneratorServiceSoap getReportGeneratorServiceSoap12() throws ServiceException;

	ReportGeneratorServiceSoap getReportGeneratorServiceSoap12(URL var1) throws ServiceException;
}