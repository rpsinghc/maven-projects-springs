package com.worldcheck.atlas.isis;

import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseFileDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportVO;
import com.worldcheck.atlas.isis.vo.ReDetailsVO;
import com.worldcheck.atlas.isis.vo.ResultSubjectVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.isis.vo.UserDetails;
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

public class AtlasWebServiceSoapBindingStub extends Stub implements AtlasWebService {
	private Vector cachedSerClasses;
	private Vector cachedSerQNames;
	private Vector cachedSerFactories;
	private Vector cachedDeserFactories;
	static OperationDesc[] _operations = new OperationDesc[6];

	private static void _initOperationDesc1() {
		OperationDesc oper = new OperationDesc();
		oper.setName("createOnlineCase");
		ParameterDesc param = new ParameterDesc(new QName("", "in0"), (byte) 1,
				new QName("http://vo.isis.atlas.worldcheck.com", "CaseDetailsVO"), CaseDetailsVO.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseResultVO"));
		oper.setReturnClass(CaseResultVO.class);
		oper.setReturnQName(new QName("", "createOnlineCaseReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;
		oper = new OperationDesc();
		oper.setName("updateOnlineCase");
		param = new ParameterDesc(new QName("", "in0"), (byte) 1,
				new QName("http://vo.isis.atlas.worldcheck.com", "CaseDetailsVO"), CaseDetailsVO.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseResultVO"));
		oper.setReturnClass(CaseResultVO.class);
		oper.setReturnQName(new QName("", "updateOnlineCaseReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.LITERAL);
		_operations[1] = oper;
		oper = new OperationDesc();
		oper.setName("downloadOnlineReport");
		param = new ParameterDesc(new QName("", "in0"), (byte) 1,
				new QName("http://vo.isis.atlas.worldcheck.com", "DownloadOnlineReportVO"),
				DownloadOnlineReportVO.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://vo.isis.atlas.worldcheck.com", "DownloadOnlineReportResultVO"));
		oper.setReturnClass(DownloadOnlineReportResultVO.class);
		oper.setReturnQName(new QName("", "downloadOnlineReportReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.LITERAL);
		_operations[2] = oper;
		oper = new OperationDesc();
		oper.setName("cancelOnlineOrder");
		param = new ParameterDesc(new QName("", "in0"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseResultVO"));
		oper.setReturnClass(CaseResultVO.class);
		oper.setReturnQName(new QName("", "cancelOnlineOrderReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.LITERAL);
		_operations[3] = oper;
		oper = new OperationDesc();
		oper.setName("updateClientReferanceNumber");
		param = new ParameterDesc(new QName("", "in0"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("", "in1"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, true, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://vo.isis.atlas.worldcheck.com", "CaseResultVO"));
		oper.setReturnClass(CaseResultVO.class);
		oper.setReturnQName(new QName("", "updateClientReferanceNumberReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.LITERAL);
		_operations[4] = oper;
		oper = new OperationDesc();
		oper.setName("pingAtlas");
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("", "pingAtlasReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.LITERAL);
		_operations[5] = oper;
	}

	public AtlasWebServiceSoapBindingStub() throws AxisFault {
		this((Service) null);
	}

	public AtlasWebServiceSoapBindingStub(URL endpointURL, Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public AtlasWebServiceSoapBindingStub(Service service) throws AxisFault {
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
		QName qName = new QName("http://isis.atlas.worldcheck.com", "ArrayOf_xsd_anyType");
		this.cachedSerQNames.add(qName);
		Class cls = ArrayOf_xsd_anyType.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "CaseDetailsVO");
		this.cachedSerQNames.add(qName);
		cls = CaseDetailsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "CaseFileDetailsVO");
		this.cachedSerQNames.add(qName);
		cls = CaseFileDetailsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "CaseResultVO");
		this.cachedSerQNames.add(qName);
		cls = CaseResultVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "DownloadOnlineReportResultVO");
		this.cachedSerQNames.add(qName);
		cls = DownloadOnlineReportResultVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "DownloadOnlineReportVO");
		this.cachedSerQNames.add(qName);
		cls = DownloadOnlineReportVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "ReDetailsVO");
		this.cachedSerQNames.add(qName);
		cls = ReDetailsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "ResultSubjectVO");
		this.cachedSerQNames.add(qName);
		cls = ResultSubjectVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "SubjectDetailsVO");
		this.cachedSerQNames.add(qName);
		cls = SubjectDetailsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://vo.isis.atlas.worldcheck.com", "UserDetails");
		this.cachedSerQNames.add(qName);
		cls = UserDetails.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
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

	public CaseResultVO createOnlineCase(CaseDetailsVO in0) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[0]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://isis.atlas.worldcheck.com", "createOnlineCase"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{in0});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (CaseResultVO) _resp;
					} catch (Exception var5) {
						return (CaseResultVO) JavaUtils.convert(_resp, CaseResultVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public CaseResultVO updateOnlineCase(CaseDetailsVO in0) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[1]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://isis.atlas.worldcheck.com", "updateOnlineCase"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{in0});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (CaseResultVO) _resp;
					} catch (Exception var5) {
						return (CaseResultVO) JavaUtils.convert(_resp, CaseResultVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public DownloadOnlineReportResultVO downloadOnlineReport(DownloadOnlineReportVO in0) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[2]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://isis.atlas.worldcheck.com", "downloadOnlineReport"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{in0});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (DownloadOnlineReportResultVO) _resp;
					} catch (Exception var5) {
						return (DownloadOnlineReportResultVO) JavaUtils.convert(_resp,
								DownloadOnlineReportResultVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public CaseResultVO cancelOnlineOrder(String in0) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[3]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://isis.atlas.worldcheck.com", "cancelOnlineOrder"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{in0});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (CaseResultVO) _resp;
					} catch (Exception var5) {
						return (CaseResultVO) JavaUtils.convert(_resp, CaseResultVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public CaseResultVO updateClientReferanceNumber(String in0, String in1) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[4]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://isis.atlas.worldcheck.com", "updateClientReferanceNumber"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{in0, in1});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (CaseResultVO) _resp;
					} catch (Exception var6) {
						return (CaseResultVO) JavaUtils.convert(_resp, CaseResultVO.class);
					}
				}
			} catch (AxisFault var7) {
				throw var7;
			}
		}
	}

	public String pingAtlas() throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[5]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			_call.setOperationName(new QName("http://isis.atlas.worldcheck.com", "pingAtlas"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[0]);
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (String) _resp;
					} catch (Exception var4) {
						return (String) JavaUtils.convert(_resp, String.class);
					}
				}
			} catch (AxisFault var5) {
				throw var5;
			}
		}
	}

	static {
		_initOperationDesc1();
	}
}