package com.integrascreen.orders;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface Ocrs_ws extends Service {
	String getocrs_wsSoapAddress();

	Ocrs_wsSoap getocrs_wsSoap() throws ServiceException;

	Ocrs_wsSoap getocrs_wsSoap(URL var1) throws ServiceException;

	String getocrs_wsSoap12Address();

	Ocrs_wsSoap getocrs_wsSoap12() throws ServiceException;

	Ocrs_wsSoap getocrs_wsSoap12(URL var1) throws ServiceException;
}