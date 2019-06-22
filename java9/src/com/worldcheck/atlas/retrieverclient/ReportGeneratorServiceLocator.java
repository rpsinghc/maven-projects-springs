package com.worldcheck.atlas.retrieverclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class ReportGeneratorServiceLocator extends Service implements ReportGeneratorService {
	private static ResourceBundle resources = ResourceBundle.getBundle("atlas");
	private String ReportGeneratorServiceSoap_address;
	private String ReportGeneratorServiceSoapWSDDServiceName;
	private String ReportGeneratorServiceSoap12_address;
	private String ReportGeneratorServiceSoap12WSDDServiceName;
	private HashSet ports;

	public ReportGeneratorServiceLocator() {
		this.ReportGeneratorServiceSoap_address = resources.getString("atlas.report.service.soap.address");
		this.ReportGeneratorServiceSoapWSDDServiceName = "ReportGeneratorServiceSoap";
		this.ReportGeneratorServiceSoap12_address = resources.getString("atlas.report.service.soap.address");
		this.ReportGeneratorServiceSoap12WSDDServiceName = "ReportGeneratorServiceSoap12";
		this.ports = null;
	}

	public ReportGeneratorServiceLocator(EngineConfiguration config) {
		super(config);
		this.ReportGeneratorServiceSoap_address = resources.getString("atlas.report.service.soap.address");
		this.ReportGeneratorServiceSoapWSDDServiceName = "ReportGeneratorServiceSoap";
		this.ReportGeneratorServiceSoap12_address = resources.getString("atlas.report.service.soap.address");
		this.ReportGeneratorServiceSoap12WSDDServiceName = "ReportGeneratorServiceSoap12";
		this.ports = null;
	}

	public ReportGeneratorServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
		super(wsdlLoc, sName);
		this.ReportGeneratorServiceSoap_address = resources.getString("atlas.report.service.soap.address");
		this.ReportGeneratorServiceSoapWSDDServiceName = "ReportGeneratorServiceSoap";
		this.ReportGeneratorServiceSoap12_address = resources.getString("atlas.report.service.soap.address");
		this.ReportGeneratorServiceSoap12WSDDServiceName = "ReportGeneratorServiceSoap12";
		this.ports = null;
	}

	public String getReportGeneratorServiceSoapAddress() {
		return this.ReportGeneratorServiceSoap_address;
	}

	public String getReportGeneratorServiceSoapWSDDServiceName() {
		return this.ReportGeneratorServiceSoapWSDDServiceName;
	}

	public void setReportGeneratorServiceSoapWSDDServiceName(String name) {
		this.ReportGeneratorServiceSoapWSDDServiceName = name;
	}

	public ReportGeneratorServiceSoap getReportGeneratorServiceSoap() throws ServiceException {
		URL endpoint;
		try {
			endpoint = new URL(this.ReportGeneratorServiceSoap_address);
		} catch (MalformedURLException var3) {
			throw new ServiceException(var3);
		}

		return this.getReportGeneratorServiceSoap(endpoint);
	}

	public ReportGeneratorServiceSoap getReportGeneratorServiceSoap(URL portAddress) throws ServiceException {
		try {
			ReportGeneratorServiceSoapStub _stub = new ReportGeneratorServiceSoapStub(portAddress, this);
			_stub.setPortName(this.getReportGeneratorServiceSoapWSDDServiceName());
			return _stub;
		} catch (AxisFault var3) {
			return null;
		}
	}

	public void setReportGeneratorServiceSoapEndpointAddress(String address) {
		this.ReportGeneratorServiceSoap_address = address;
	}

	public String getReportGeneratorServiceSoap12Address() {
		return this.ReportGeneratorServiceSoap12_address;
	}

	public String getReportGeneratorServiceSoap12WSDDServiceName() {
		return this.ReportGeneratorServiceSoap12WSDDServiceName;
	}

	public void setReportGeneratorServiceSoap12WSDDServiceName(String name) {
		this.ReportGeneratorServiceSoap12WSDDServiceName = name;
	}

	public ReportGeneratorServiceSoap getReportGeneratorServiceSoap12() throws ServiceException {
		URL endpoint;
		try {
			endpoint = new URL(this.ReportGeneratorServiceSoap12_address);
		} catch (MalformedURLException var3) {
			throw new ServiceException(var3);
		}

		return this.getReportGeneratorServiceSoap12(endpoint);
	}

	public ReportGeneratorServiceSoap getReportGeneratorServiceSoap12(URL portAddress) throws ServiceException {
		try {
			ReportGeneratorServiceSoap12Stub _stub = new ReportGeneratorServiceSoap12Stub(portAddress, this);
			_stub.setPortName(this.getReportGeneratorServiceSoap12WSDDServiceName());
			return _stub;
		} catch (AxisFault var3) {
			return null;
		}
	}

	public void setReportGeneratorServiceSoap12EndpointAddress(String address) {
		this.ReportGeneratorServiceSoap12_address = address;
	}

	public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
		try {
			if (ReportGeneratorServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				ReportGeneratorServiceSoapStub _stub = new ReportGeneratorServiceSoapStub(
						new URL(this.ReportGeneratorServiceSoap_address), this);
				_stub.setPortName(this.getReportGeneratorServiceSoapWSDDServiceName());
				return _stub;
			}

			if (ReportGeneratorServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				ReportGeneratorServiceSoap12Stub _stub = new ReportGeneratorServiceSoap12Stub(
						new URL(this.ReportGeneratorServiceSoap12_address), this);
				_stub.setPortName(this.getReportGeneratorServiceSoap12WSDDServiceName());
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
			if ("ReportGeneratorServiceSoap".equals(inputPortName)) {
				return this.getReportGeneratorServiceSoap();
			} else if ("ReportGeneratorServiceSoap12".equals(inputPortName)) {
				return this.getReportGeneratorServiceSoap12();
			} else {
				Remote _stub = this.getPort(serviceEndpointInterface);
				((Stub) _stub).setPortName(portName);
				return _stub;
			}
		}
	}

	public QName getServiceName() {
		return new QName("http://tempuri.org/", "ReportGeneratorService");
	}

	public Iterator getPorts() {
		if (this.ports == null) {
			this.ports = new HashSet();
			this.ports.add(new QName("http://tempuri.org/", "ReportGeneratorServiceSoap"));
			this.ports.add(new QName("http://tempuri.org/", "ReportGeneratorServiceSoap12"));
		}

		return this.ports.iterator();
	}

	public void setEndpointAddress(String portName, String address) throws ServiceException {
		if ("ReportGeneratorServiceSoap".equals(portName)) {
			this.setReportGeneratorServiceSoapEndpointAddress(address);
		} else {
			if (!"ReportGeneratorServiceSoap12".equals(portName)) {
				throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
			}

			this.setReportGeneratorServiceSoap12EndpointAddress(address);
		}

	}

	public void setEndpointAddress(QName portName, String address) throws ServiceException {
		this.setEndpointAddress(portName.getLocalPart(), address);
	}
}