package com.worldcheck.atlas.isis;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class AtlasWebServiceServiceLocator extends Service implements AtlasWebServiceService {
	private String AtlasWebService_address = "http://pslp65.thomsonreuters.com:18793/sbm/services/AtlasWebService";
	private String AtlasWebServiceWSDDServiceName = "AtlasWebService";
	private HashSet ports = null;

	public AtlasWebServiceServiceLocator() {
	}

	public AtlasWebServiceServiceLocator(EngineConfiguration config) {
		super(config);
	}

	public AtlasWebServiceServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
		super(wsdlLoc, sName);
	}

	public String getAtlasWebServiceAddress() {
		return this.AtlasWebService_address;
	}

	public String getAtlasWebServiceWSDDServiceName() {
		return this.AtlasWebServiceWSDDServiceName;
	}

	public void setAtlasWebServiceWSDDServiceName(String name) {
		this.AtlasWebServiceWSDDServiceName = name;
	}

	public AtlasWebService getAtlasWebService() throws ServiceException {
		URL endpoint;
		try {
			endpoint = new URL(this.AtlasWebService_address);
		} catch (MalformedURLException var3) {
			throw new ServiceException(var3);
		}

		return this.getAtlasWebService(endpoint);
	}

	public AtlasWebService getAtlasWebService(URL portAddress) throws ServiceException {
		try {
			AtlasWebServiceSoapBindingStub _stub = new AtlasWebServiceSoapBindingStub(portAddress, this);
			_stub.setPortName(this.getAtlasWebServiceWSDDServiceName());
			return _stub;
		} catch (AxisFault var3) {
			return null;
		}
	}

	public void setAtlasWebServiceEndpointAddress(String address) {
		this.AtlasWebService_address = address;
	}

	public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
		try {
			if (AtlasWebService.class.isAssignableFrom(serviceEndpointInterface)) {
				AtlasWebServiceSoapBindingStub _stub = new AtlasWebServiceSoapBindingStub(
						new URL(this.AtlasWebService_address), this);
				_stub.setPortName(this.getAtlasWebServiceWSDDServiceName());
				return _stub;
			}
		} catch (Throwable var3) {
			throw new ServiceException(var3);
		}

		throw new ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
		if (portName == null) {
			return this.getPort(serviceEndpointInterface);
		} else {
			String inputPortName = portName.getLocalPart();
			if ("AtlasWebService".equals(inputPortName)) {
				return this.getAtlasWebService();
			} else {
				Remote _stub = this.getPort(serviceEndpointInterface);
				((Stub) _stub).setPortName(portName);
				return _stub;
			}
		}
	}

	public QName getServiceName() {
		return new QName("http://isis.atlas.worldcheck.com", "AtlasWebServiceService");
	}

	public Iterator getPorts() {
		if (this.ports == null) {
			this.ports = new HashSet();
			this.ports.add(new QName("http://isis.atlas.worldcheck.com", "AtlasWebService"));
		}

		return this.ports.iterator();
	}

	public void setEndpointAddress(String portName, String address) throws ServiceException {
		if ("AtlasWebService".equals(portName)) {
			this.setAtlasWebServiceEndpointAddress(address);
		} else {
			throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	public void setEndpointAddress(QName portName, String address) throws ServiceException {
		this.setEndpointAddress(portName.getLocalPart(), address);
	}
}