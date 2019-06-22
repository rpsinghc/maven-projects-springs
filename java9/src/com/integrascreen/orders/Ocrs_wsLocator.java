package com.integrascreen.orders;

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

public class Ocrs_wsLocator extends Service implements Ocrs_ws {
	private String ocrs_wsSoap_address = "https://10.55.129.103/ws/ocrs_ws.asmx";
	private String ocrs_wsSoapWSDDServiceName = "ocrs_wsSoap";
	private String ocrs_wsSoap12_address = "https://10.55.129.103/ws/ocrs_ws.asmx";
	private String ocrs_wsSoap12WSDDServiceName = "ocrs_wsSoap12";
	private HashSet ports = null;

	public Ocrs_wsLocator() {
	}

	public Ocrs_wsLocator(EngineConfiguration config) {
		super(config);
	}

	public Ocrs_wsLocator(String wsdlLoc, QName sName) throws ServiceException {
		super(wsdlLoc, sName);
	}

	public String getocrs_wsSoapAddress() {
		return this.ocrs_wsSoap_address;
	}

	public String getocrs_wsSoapWSDDServiceName() {
		return this.ocrs_wsSoapWSDDServiceName;
	}

	public void setocrs_wsSoapWSDDServiceName(String name) {
		this.ocrs_wsSoapWSDDServiceName = name;
	}

	public Ocrs_wsSoap getocrs_wsSoap() throws ServiceException {
		URL endpoint;
		try {
			endpoint = new URL(this.ocrs_wsSoap_address);
		} catch (MalformedURLException var3) {
			throw new ServiceException(var3);
		}

		return this.getocrs_wsSoap(endpoint);
	}

	public Ocrs_wsSoap getocrs_wsSoap(URL portAddress) throws ServiceException {
		try {
			Ocrs_wsSoapStub _stub = new Ocrs_wsSoapStub(portAddress, this);
			_stub.setPortName(this.getocrs_wsSoapWSDDServiceName());
			return _stub;
		} catch (AxisFault var3) {
			return null;
		}
	}

	public void setocrs_wsSoapEndpointAddress(String address) {
		this.ocrs_wsSoap_address = address;
	}

	public String getocrs_wsSoap12Address() {
		return this.ocrs_wsSoap12_address;
	}

	public String getocrs_wsSoap12WSDDServiceName() {
		return this.ocrs_wsSoap12WSDDServiceName;
	}

	public void setocrs_wsSoap12WSDDServiceName(String name) {
		this.ocrs_wsSoap12WSDDServiceName = name;
	}

	public Ocrs_wsSoap getocrs_wsSoap12() throws ServiceException {
		URL endpoint;
		try {
			endpoint = new URL(this.ocrs_wsSoap12_address);
		} catch (MalformedURLException var3) {
			throw new ServiceException(var3);
		}

		return this.getocrs_wsSoap12(endpoint);
	}

	public Ocrs_wsSoap getocrs_wsSoap12(URL portAddress) throws ServiceException {
		try {
			Ocrs_wsSoap12Stub _stub = new Ocrs_wsSoap12Stub(portAddress, this);
			_stub.setPortName(this.getocrs_wsSoap12WSDDServiceName());
			return _stub;
		} catch (AxisFault var3) {
			return null;
		}
	}

	public void setocrs_wsSoap12EndpointAddress(String address) {
		this.ocrs_wsSoap12_address = address;
	}

	public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
		try {
			if (Ocrs_wsSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				Ocrs_wsSoapStub _stub = new Ocrs_wsSoapStub(new URL(this.ocrs_wsSoap_address), this);
				_stub.setPortName(this.getocrs_wsSoapWSDDServiceName());
				return _stub;
			}

			if (Ocrs_wsSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				Ocrs_wsSoap12Stub _stub = new Ocrs_wsSoap12Stub(new URL(this.ocrs_wsSoap12_address), this);
				_stub.setPortName(this.getocrs_wsSoap12WSDDServiceName());
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
			if ("ocrs_wsSoap".equals(inputPortName)) {
				return this.getocrs_wsSoap();
			} else if ("ocrs_wsSoap12".equals(inputPortName)) {
				return this.getocrs_wsSoap12();
			} else {
				Remote _stub = this.getPort(serviceEndpointInterface);
				((Stub) _stub).setPortName(portName);
				return _stub;
			}
		}
	}

	public QName getServiceName() {
		return new QName("https://orders.integrascreen.com", "ocrs_ws");
	}

	public Iterator getPorts() {
		if (this.ports == null) {
			this.ports = new HashSet();
			this.ports.add(new QName("https://orders.integrascreen.com", "ocrs_wsSoap"));
			this.ports.add(new QName("https://orders.integrascreen.com", "ocrs_wsSoap12"));
		}

		return this.ports.iterator();
	}

	public void setEndpointAddress(String portName, String address) throws ServiceException {
		if ("ocrs_wsSoap".equals(portName)) {
			this.setocrs_wsSoapEndpointAddress(address);
		} else {
			if (!"ocrs_wsSoap12".equals(portName)) {
				throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
			}

			this.setocrs_wsSoap12EndpointAddress(address);
		}

	}

	public void setEndpointAddress(QName portName, String address) throws ServiceException {
		this.setEndpointAddress(portName.getLocalPart(), address);
	}
}