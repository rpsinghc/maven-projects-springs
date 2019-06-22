package com.worldcheck.atlas.isis;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface AtlasWebServiceService extends Service {
	String getAtlasWebServiceAddress();

	AtlasWebService getAtlasWebService() throws ServiceException;

	AtlasWebService getAtlasWebService(URL var1) throws ServiceException;
}