package com.worldcheck.atlas.isis;

import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import javax.xml.soap.SOAPException;
import org.apache.axis.MessageContext;

public class AtlasWebServiceSoapBindingImpl implements AtlasWebService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.AtlasWebServiceSoapBindingImpl");

	public CaseResultVO createOnlineCase(CaseDetailsVO in0) throws RemoteException {
		this.logger.debug("Inside createOnlineCase...");
		MessageContext context = MessageContext.getCurrentContext();
		String soapBodyString = "";

		try {
			soapBodyString = context.getMessage().getSOAPPart().getEnvelope().toString();
			this.logger.debug("soapBody::::::::" + soapBodyString);
		} catch (SOAPException var5) {
			this.logger.debug("Some Error..." + getStackTraceAsString(var5));
		}

		return ResourceLocator.self().getAtlasWebService().createCaseForISIS(in0, soapBodyString);
	}

	public CaseResultVO updateOnlineCase(CaseDetailsVO in0) throws RemoteException {
		this.logger.debug("Inside updateOnlineCase...");
		MessageContext context = MessageContext.getCurrentContext();
		String soapBodyString = "";

		try {
			soapBodyString = context.getMessage().getSOAPPart().getEnvelope().toString();
			this.logger.debug("soapBody::::::::" + soapBodyString);
		} catch (SOAPException var5) {
			this.logger.debug("Some Error..." + getStackTraceAsString(var5));
		}

		return ResourceLocator.self().getAtlasWebService().updateCaseForISIS(in0, soapBodyString);
	}

	public DownloadOnlineReportResultVO downloadOnlineReport(DownloadOnlineReportVO in0) throws RemoteException {
		this.logger.debug("Inside DownloadOnlineReportResultVO...");
		MessageContext context = MessageContext.getCurrentContext();
		String soapBodyString = "";

		try {
			soapBodyString = context.getMessage().getSOAPPart().getEnvelope().toString();
			this.logger.debug("soapBody::::::::" + soapBodyString);
		} catch (SOAPException var5) {
			this.logger.debug("Some Error..." + getStackTraceAsString(var5));
		}

		return ResourceLocator.self().getAtlasWebService().downloadOnlineReport(in0.getCrn(), in0.getFileName(),
				in0.getVersion(), soapBodyString);
	}

	public CaseResultVO cancelOnlineOrder(String in0) throws RemoteException {
		this.logger.debug("Inside cancelOnlineOrder...");
		MessageContext context = MessageContext.getCurrentContext();
		String soapBodyString = "";

		try {
			soapBodyString = context.getMessage().getSOAPPart().getEnvelope().toString();
			this.logger.debug("soapBody::::::::" + soapBodyString);
		} catch (SOAPException var5) {
			this.logger.debug("Some Error..." + getStackTraceAsString(var5));
		}

		return ResourceLocator.self().getAtlasWebService().cancelOnlineOrder(in0, soapBodyString);
	}

	public CaseResultVO updateClientReferanceNumber(String in0, String in1) throws RemoteException {
		return null;
	}

	public String pingAtlas() throws RemoteException {
		this.logger.debug("Inside pingAtlas...");
		return ResourceLocator.self().getAtlasWebService().pingAtlas();
	}

	public static String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}
}