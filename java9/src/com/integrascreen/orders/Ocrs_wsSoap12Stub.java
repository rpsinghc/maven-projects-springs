package com.integrascreen.orders;

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
import org.apache.axis.encoding.XMLType;
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

public class Ocrs_wsSoap12Stub extends Stub implements Ocrs_wsSoap {
	private Vector cachedSerClasses;
	private Vector cachedSerQNames;
	private Vector cachedSerFactories;
	private Vector cachedDeserFactories;
	static OperationDesc[] _operations = new OperationDesc[13];

	static {
		_initOperationDesc1();
		_initOperationDesc2();
	}

	private static void _initOperationDesc1() {
		OperationDesc oper = new OperationDesc();
		oper.setName("UFP");
		ParameterDesc param = new ParameterDesc(new QName("http://orders.integrascreen.com", "Param"), (byte) 1,
				new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;
		oper = new OperationDesc();
		oper.setName("CBD");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "CBDVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "CBDVO"), CBDVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://orders.integrascreen.com", "ResponseVO"));
		oper.setReturnClass(ResponseVO.class);
		oper.setReturnQName(new QName("http://orders.integrascreen.com", "CBDResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[1] = oper;
		oper = new OperationDesc();
		oper.setName("US");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "USVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "USVO"), USVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://orders.integrascreen.com", "ResponseVO"));
		oper.setReturnClass(ResponseVO.class);
		oper.setReturnQName(new QName("http://orders.integrascreen.com", "USResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[2] = oper;
		oper = new OperationDesc();
		oper.setName("pingISIS");
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://orders.integrascreen.com", "pingISISResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[3] = oper;
		oper = new OperationDesc();
		oper.setName("UPCRN");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "UPCRNVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "UPCRNVO"), UPCRNVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://orders.integrascreen.com", "ResponseVO"));
		oper.setReturnClass(ResponseVO.class);
		oper.setReturnQName(new QName("http://orders.integrascreen.com", "UPCRNResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[4] = oper;
		oper = new OperationDesc();
		oper.setName("CurrencyMaster");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "CurrencyMasterVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "CurrencyMasterVO"), CurrencyMasterVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[5] = oper;
		oper = new OperationDesc();
		oper.setName("CountryMaster");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "CountryMasterVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "CountryMasterVO"), CountryMasterVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[6] = oper;
		oper = new OperationDesc();
		oper.setName("IndustryMaster");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "IndustryMasterVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "IndustryMasterVO"), IndustryMasterVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[7] = oper;
		oper = new OperationDesc();
		oper.setName("RiskMaster");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "RiskMasterVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "RiskMasterVO"), RiskMasterVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[8] = oper;
		oper = new OperationDesc();
		oper.setName("REMaster");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "REMasterVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "REMasterVO"), REMasterVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[9] = oper;
	}

	private static void _initOperationDesc2() {
		OperationDesc oper = new OperationDesc();
		oper.setName("ReportTypeMaster");
		ParameterDesc param = new ParameterDesc(new QName("http://orders.integrascreen.com", "ReportTypeMasterVO"),
				(byte) 1, new QName("http://orders.integrascreen.com", "ReportTypeMasterVO"), ReportTypeMasterVO.class,
				false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(XMLType.AXIS_VOID);
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[10] = oper;
		oper = new OperationDesc();
		oper.setName("UPMaster");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "UPMasterVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "UPMasterVO"), UPMasterVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://orders.integrascreen.com", "ResponseVO"));
		oper.setReturnClass(ResponseVO.class);
		oper.setReturnQName(new QName("http://orders.integrascreen.com", "UPMasterResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[11] = oper;
		oper = new OperationDesc();
		oper.setName("UPSubject");
		param = new ParameterDesc(new QName("http://orders.integrascreen.com", "UPSubjectVO"), (byte) 1,
				new QName("http://orders.integrascreen.com", "UPSubjectVO"), UPSubjectVO.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://orders.integrascreen.com", "ResponseVO"));
		oper.setReturnClass(ResponseVO.class);
		oper.setReturnQName(new QName("http://orders.integrascreen.com", "UPSubjectResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[12] = oper;
	}

	public Ocrs_wsSoap12Stub() throws AxisFault {
		this((Service) null);
	}

	public Ocrs_wsSoap12Stub(URL endpointURL, Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public Ocrs_wsSoap12Stub(Service service) throws AxisFault {
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
		QName qName = new QName("http://orders.integrascreen.com", "ArrayOfAttribute");
		this.cachedSerQNames.add(qName);
		Class cls = Attribute[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "Attribute");
		QName qName2 = new QName("http://orders.integrascreen.com", "Attribute");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfClientsVO");
		this.cachedSerQNames.add(qName);
		cls = ClientsVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "ClientsVO");
		qName2 = new QName("http://orders.integrascreen.com", "ClientsVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfCountry");
		this.cachedSerQNames.add(qName);
		cls = Country[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "Country");
		qName2 = new QName("http://orders.integrascreen.com", "Country");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfErrorObjects");
		this.cachedSerQNames.add(qName);
		cls = ErrorObjects[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "ErrorObjects");
		qName2 = new QName("http://orders.integrascreen.com", "ErrorObjects");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfReportTypesVo");
		this.cachedSerQNames.add(qName);
		cls = ReportTypesVo[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "ReportTypesVo");
		qName2 = new QName("http://orders.integrascreen.com", "ReportTypesVo");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfResearchElementsVO");
		this.cachedSerQNames.add(qName);
		cls = ResearchElementsVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "ResearchElementsVO");
		qName2 = new QName("http://orders.integrascreen.com", "ResearchElementsVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfRisk");
		this.cachedSerQNames.add(qName);
		cls = Risk[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "Risk");
		qName2 = new QName("http://orders.integrascreen.com", "Risk");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfRiskCategory");
		this.cachedSerQNames.add(qName);
		cls = RiskCategory[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "RiskCategory");
		qName2 = new QName("http://orders.integrascreen.com", "RiskCategory");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfRiskErrorVO");
		this.cachedSerQNames.add(qName);
		cls = RiskErrorVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "RiskErrorVO");
		qName2 = new QName("http://orders.integrascreen.com", "RiskErrorVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfRiskMapping");
		this.cachedSerQNames.add(qName);
		cls = RiskMapping[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "RiskMapping");
		qName2 = new QName("http://orders.integrascreen.com", "RiskMapping");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfRiskMappingsVO");
		this.cachedSerQNames.add(qName);
		cls = RiskMappingsVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "RiskMappingsVO");
		qName2 = new QName("http://orders.integrascreen.com", "RiskMappingsVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfRiskMasterVO");
		this.cachedSerQNames.add(qName);
		cls = RiskMasterVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "RiskMasterVO");
		qName2 = new QName("http://orders.integrascreen.com", "RiskMasterVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfString");
		this.cachedSerQNames.add(qName);
		cls = String[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://www.w3.org/2001/XMLSchema", "string");
		qName2 = new QName("http://orders.integrascreen.com", "string");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfSubject");
		this.cachedSerQNames.add(qName);
		cls = Subject[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "Subject");
		qName2 = new QName("http://orders.integrascreen.com", "Subject");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfSubjectCountriesVO");
		this.cachedSerQNames.add(qName);
		cls = SubjectCountriesVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "SubjectCountriesVO");
		qName2 = new QName("http://orders.integrascreen.com", "SubjectCountriesVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfSubReportTypeMasterVO");
		this.cachedSerQNames.add(qName);
		cls = SubReportTypeMasterVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "SubReportTypeMasterVO");
		qName2 = new QName("http://orders.integrascreen.com", "SubReportTypeMasterVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfSubReportTypesVO");
		this.cachedSerQNames.add(qName);
		cls = SubReportTypesVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "SubReportTypesVO");
		qName2 = new QName("http://orders.integrascreen.com", "SubReportTypesVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfUPSubjectREVO");
		this.cachedSerQNames.add(qName);
		cls = UPSubjectREVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "UPSubjectREVO");
		qName2 = new QName("http://orders.integrascreen.com", "UPSubjectREVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfUSRiskVO");
		this.cachedSerQNames.add(qName);
		cls = USRiskVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "USRiskVO");
		qName2 = new QName("http://orders.integrascreen.com", "USRiskVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfUSSubjectIndustryVO");
		this.cachedSerQNames.add(qName);
		cls = USSubjectIndustryVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "USSubjectIndustryVO");
		qName2 = new QName("http://orders.integrascreen.com", "USSubjectIndustryVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "ArrayOfUSSubjectRiskVO");
		this.cachedSerQNames.add(qName);
		cls = USSubjectRiskVO[].class;
		this.cachedSerClasses.add(cls);
		qName = new QName("http://orders.integrascreen.com", "USSubjectRiskVO");
		qName2 = new QName("http://orders.integrascreen.com", "USSubjectRiskVO");
		this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		this.cachedDeserFactories.add(new ArrayDeserializerFactory());
		qName = new QName("http://orders.integrascreen.com", "Attribute");
		this.cachedSerQNames.add(qName);
		cls = Attribute.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CaseLevelRisks");
		this.cachedSerQNames.add(qName);
		cls = CaseLevelRisks.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CBDVO");
		this.cachedSerQNames.add(qName);
		cls = CBDVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ClientsVO");
		this.cachedSerQNames.add(qName);
		cls = ClientsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CodeDetailsVO");
		this.cachedSerQNames.add(qName);
		cls = CodeDetailsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "Country");
		this.cachedSerQNames.add(qName);
		cls = Country.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CountryBreakDown");
		this.cachedSerQNames.add(qName);
		cls = CountryBreakDown.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CountryMasterVO");
		this.cachedSerQNames.add(qName);
		cls = CountryMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CRNRiskData");
		this.cachedSerQNames.add(qName);
		cls = CRNRiskData.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "CurrencyMasterVO");
		this.cachedSerQNames.add(qName);
		cls = CurrencyMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ErrorDetailObjects");
		this.cachedSerQNames.add(qName);
		cls = ErrorDetailObjects.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ErrorObjects");
		this.cachedSerQNames.add(qName);
		cls = ErrorObjects.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "IndustryMasterVO");
		this.cachedSerQNames.add(qName);
		cls = IndustryMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "REMasterVO");
		this.cachedSerQNames.add(qName);
		cls = REMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ReportTypeMasterVO");
		this.cachedSerQNames.add(qName);
		cls = ReportTypeMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ReportTypesVo");
		this.cachedSerQNames.add(qName);
		cls = ReportTypesVo.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ResearchElementsVO");
		this.cachedSerQNames.add(qName);
		cls = ResearchElementsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ResponseDetailsVO");
		this.cachedSerQNames.add(qName);
		cls = ResponseDetailsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ResponseSubjectVO");
		this.cachedSerQNames.add(qName);
		cls = ResponseSubjectVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "ResponseVO");
		this.cachedSerQNames.add(qName);
		cls = ResponseVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "Risk");
		this.cachedSerQNames.add(qName);
		cls = Risk.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskAggregation");
		this.cachedSerQNames.add(qName);
		cls = RiskAggregation.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskCategory");
		this.cachedSerQNames.add(qName);
		cls = RiskCategory.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskErrorVO");
		this.cachedSerQNames.add(qName);
		cls = RiskErrorVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskMapping");
		this.cachedSerQNames.add(qName);
		cls = RiskMapping.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskMappingsVO");
		this.cachedSerQNames.add(qName);
		cls = RiskMappingsVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskMasterVO");
		this.cachedSerQNames.add(qName);
		cls = RiskMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskProfile");
		this.cachedSerQNames.add(qName);
		cls = RiskProfile.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "RiskSuccessVO");
		this.cachedSerQNames.add(qName);
		cls = RiskSuccessVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "Subject");
		this.cachedSerQNames.add(qName);
		cls = Subject.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "SubjectCountriesVO");
		this.cachedSerQNames.add(qName);
		cls = SubjectCountriesVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "SubjectLevelRisks");
		this.cachedSerQNames.add(qName);
		cls = SubjectLevelRisks.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "Subjects");
		this.cachedSerQNames.add(qName);
		cls = Subjects.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "SubReportTypeMasterVO");
		this.cachedSerQNames.add(qName);
		cls = SubReportTypeMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "SubReportTypesVO");
		this.cachedSerQNames.add(qName);
		cls = SubReportTypesVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "SuccessMapping");
		this.cachedSerQNames.add(qName);
		cls = SuccessMapping.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "SuccessRisk");
		this.cachedSerQNames.add(qName);
		cls = SuccessRisk.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "UPCRNVO");
		this.cachedSerQNames.add(qName);
		cls = UPCRNVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "UPMasterVO");
		this.cachedSerQNames.add(qName);
		cls = UPMasterVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "UPSubjectREVO");
		this.cachedSerQNames.add(qName);
		cls = UPSubjectREVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "UPSubjectVO");
		this.cachedSerQNames.add(qName);
		cls = UPSubjectVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "USRiskVO");
		this.cachedSerQNames.add(qName);
		cls = USRiskVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "USSubjectIndustryVO");
		this.cachedSerQNames.add(qName);
		cls = USSubjectIndustryVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "USSubjectRiskVO");
		this.cachedSerQNames.add(qName);
		cls = USSubjectRiskVO.class;
		this.cachedSerClasses.add(cls);
		this.cachedSerFactories.add(beansf);
		this.cachedDeserFactories.add(beandf);
		qName = new QName("http://orders.integrascreen.com", "USVO");
		this.cachedSerQNames.add(qName);
		cls = USVO.class;
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
		} catch (Throwable var11) {
			throw new AxisFault("Failure trying to get the Call object", var11);
		}
	}

	public void UFP(String param) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[0]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/UFP");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "UFP"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{param});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public ResponseVO CBD(CBDVO CBDVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[1]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/CBD");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "CBD"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{CBDVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (ResponseVO) _resp;
					} catch (Exception var5) {
						return (ResponseVO) JavaUtils.convert(_resp, ResponseVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public ResponseVO US(USVO USVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[2]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/US");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "US"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{USVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (ResponseVO) _resp;
					} catch (Exception var5) {
						return (ResponseVO) JavaUtils.convert(_resp, ResponseVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public String pingISIS() throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[3]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/pingISIS");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "pingISIS"));
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

	public ResponseVO UPCRN(UPCRNVO UPCRNVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[4]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/UPCRN");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "UPCRN"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{UPCRNVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (ResponseVO) _resp;
					} catch (Exception var5) {
						return (ResponseVO) JavaUtils.convert(_resp, ResponseVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public void currencyMaster(CurrencyMasterVO currencyMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[5]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/CurrencyMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "CurrencyMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{currencyMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public void countryMaster(CountryMasterVO countryMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[6]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/CountryMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "CountryMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{countryMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public void industryMaster(IndustryMasterVO industryMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[7]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/IndustryMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "IndustryMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{industryMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public void riskMaster(RiskMasterVO riskMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[8]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/RiskMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "RiskMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{riskMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public void REMaster(REMasterVO REMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[9]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/REMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "REMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{REMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public void reportTypeMaster(ReportTypeMasterVO reportTypeMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[10]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/ReportTypeMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "ReportTypeMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{reportTypeMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);
				}
			} catch (AxisFault var4) {
				throw var4;
			}
		}
	}

	public ResponseVO UPMaster(UPMasterVO UPMasterVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[11]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/UPMaster");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "UPMaster"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{UPMasterVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (ResponseVO) _resp;
					} catch (Exception var5) {
						return (ResponseVO) JavaUtils.convert(_resp, ResponseVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}

	public ResponseVO UPSubject(UPSubjectVO UPSubjectVO) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		} else {
			Call _call = this.createCall();
			_call.setOperation(_operations[12]);
			_call.setUseSOAPAction(true);
			_call.setSOAPActionURI("http://orders.integrascreen.com/UPSubject");
			_call.setEncodingStyle((String) null);
			_call.setProperty("sendXsiTypes", Boolean.FALSE);
			_call.setProperty("sendMultiRefs", Boolean.FALSE);
			_call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
			_call.setOperationName(new QName("http://orders.integrascreen.com", "UPSubject"));
			this.setRequestHeaders(_call);
			this.setAttachments(_call);

			try {
				Object _resp = _call.invoke(new Object[]{UPSubjectVO});
				if (_resp instanceof RemoteException) {
					throw (RemoteException) _resp;
				} else {
					this.extractAttachments(_call);

					try {
						return (ResponseVO) _resp;
					} catch (Exception var5) {
						return (ResponseVO) JavaUtils.convert(_resp, ResponseVO.class);
					}
				}
			} catch (AxisFault var6) {
				throw var6;
			}
		}
	}
}