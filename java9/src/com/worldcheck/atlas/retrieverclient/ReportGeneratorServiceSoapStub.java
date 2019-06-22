package com.worldcheck.atlas.retrieverclient;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.SerializerFactory;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

public class ReportGeneratorServiceSoapStub extends Stub implements ReportGeneratorServiceSoap {
	private Vector cachedSerClasses;
	private Vector cachedSerQNames;
	private Vector cachedSerFactories;
	private Vector cachedDeserFactories;
	static OperationDesc[] _operations = new OperationDesc[3];

	private static void _initOperationDesc1() {
		OperationDesc oper = new OperationDesc();
		oper.setName("GenerateReport");
		ParameterDesc param = new ParameterDesc(new QName("http://tempuri.org/", "crn"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "ProcessCycle"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "long"), Long.TYPE, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "ReportType"), (byte) 1,
				new QName("http://tempuri.org/", "ReportType"), ReportType.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "OutputFormat"), (byte) 1,
				new QName("http://tempuri.org/", "OutputFormat"), OutputFormat.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "DocumentProperties"), (byte) 1,
				new QName("http://tempuri.org/", "DocumentProperties"), DocumentProperties.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "GenerateReportResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;
		oper = new OperationDesc();
		oper.setName("GenerateAndUpload");
		param = new ParameterDesc(new QName("http://tempuri.org/", "crn"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "ProcessCycle"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "long"), Long.TYPE, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "ReportType"), (byte) 1,
				new QName("http://tempuri.org/", "ReportType"), ReportType.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "OutputFormat"), (byte) 1,
				new QName("http://tempuri.org/", "OutputFormat"), OutputFormat.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "DocumentProperties"), (byte) 1,
				new QName("http://tempuri.org/", "DocumentProperties"), DocumentProperties.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "emailId"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "UserName"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "taskPid"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(Boolean.TYPE);
		oper.setReturnQName(new QName("http://tempuri.org/", "GenerateAndUploadResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[1] = oper;
		oper = new OperationDesc();
		oper.setName("GenerateReportForAtlas");
		param = new ParameterDesc(new QName("http://tempuri.org/", "countryId"), (byte) 1,
				new QName("http://tempuri.org/", "ArrayOfInt"), int[].class, false, false);
		param.setItemQName(new QName("http://tempuri.org/", "int"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "subjectType"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "fileName"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "fileType"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "GenerateReportForAtlasResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[2] = oper;
	}

	public ReportGeneratorServiceSoapStub() throws AxisFault {
		this((Service) null);
	}

	public ReportGeneratorServiceSoapStub(URL endpointURL, Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public ReportGeneratorServiceSoapStub(Service service) throws AxisFault {
		this.cachedSerClasses = new Vector();
		this.cachedSerQNames = new Vector();
		this.cachedSerFactories = new Vector();
		this.cachedDeserFactories = new Vector();
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}

		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
		Class beansf = BeanSerializerFactory.class;
		Class beandf = BeanDeserializerFactory.class;
		Class enumsf = EnumSerializerFactory.class;
		Class enumdf = EnumDeserializerFactory.class;
		Class arraysf = ArraySerializerFactory.class;
		Class arraydf = ArrayDeserializerFactory.class;
		Class simplesf = SimpleSerializerFactory.class;
		Class simpledf = SimpleDeserializerFactory.class;
		Class simplelistsf = SimpleListSerializerFactory.class;
		Class simplelistdf = SimpleListDeserializerFactory.class;
		QName qName = new QName("http://tempuri.org/", "ArrayOfInt");
		this.cachedSerQNames.add(qName);
		Class cls = int[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://www.w3.org/2001/XMLSchema", "int");
		QName qName2 = new QName("http://tempuri.org/", "int");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://tempuri.org/", "DocumentProperties");
		this.cachedSerQNames.add(qName);
		cls = DocumentProperties.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://tempuri.org/", "OutputFormat");
		this.cachedSerQNames.add(qName);
		cls = OutputFormat.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(enumsf);
		this.cachedDeserFactories.add(enumdf);
		qName = new QName("http://tempuri.org/", "ReportType");
		this.cachedSerQNames.add(qName);
		cls = ReportType.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(enumsf);
		this.cachedDeserFactories.add(enumdf);
	}

	protected Call createCall() throws RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}

			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}

			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}

			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}

			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}

			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}

			Enumeration keys = super.cachedProperties.keys();

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}

			synchronized (this) {
				if (this.firstCall()) {
					_call.setEncodingStyle((String) null);

					for (int i = 0; i < this.cachedSerFactories.size(); ++i) {
						Class cls = (Class) this.cachedSerClasses.get(i);
						QName qName = (QName) this.cachedSerQNames.get(i);
						Object x = this.cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class) this.cachedSerFactories.get(i);
							Class df = (Class) this.cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) this.cachedSerFactories
									.get(i);
							DeserializerFactory df = (DeserializerFactory) this.cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}

			return _call;
		} catch (Throwable var12) {
			throw new AxisFault("Failure trying to get the Call object", var12);
		}
	}

	public String generateReport(String crn, long processCycle, ReportType reportType, OutputFormat outputFormat,
			DocumentProperties documentProperties) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[0]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://tempuri.org/GenerateReport");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://tempuri.org/", "GenerateReport"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(
						new Object[]{crn, new Long(processCycle), reportType, outputFormat, documentProperties});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (String) _resp;
					} catch (Exception var10) {
						return (String) JavaUtils.convert(_resp, String.class);
					}
				}
			} catch (AxisFault var11) {
				throw var11;
			}
		}
	}

	public boolean generateAndUpload(String crn, long processCycle, ReportType reportType, OutputFormat outputFormat,
			DocumentProperties documentProperties, String emailId, String userName, String taskPid)
			throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[1]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://tempuri.org/GenerateAndUpload");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://tempuri.org/", "GenerateAndUpload"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{crn, new Long(processCycle), reportType, outputFormat,
						documentProperties, emailId, userName, taskPid});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (Boolean) _resp;
					} catch (Exception var13) {
						return (Boolean) JavaUtils.convert(_resp, Boolean.TYPE);
					}
				}
			} catch (AxisFault var14) {
				throw var14;
			}
		}
	}

	public String generateReportForAtlas(int[] countryId, String subjectType, String fileName, String fileType)
			throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[2]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://tempuri.org/GenerateReportForAtlas");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://tempuri.org/", "GenerateReportForAtlas"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{countryId, subjectType, fileName, fileType});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (String) _resp;
					} catch (Exception var8) {
						return (String) JavaUtils.convert(_resp, String.class);
					}
				}
			} catch (AxisFault var9) {
				throw var9;
			}
		}
	}

	static {
		_initOperationDesc1();
	}
}