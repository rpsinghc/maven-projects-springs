package com.worldcheck.atlas.isis.services;

import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.bl.AtlasWebServiceClientManager;
import com.worldcheck.atlas.isis.vo.ClientCBDVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.isis.vo.ClientSubjectVO;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class AtlasWebServiceClient {
	private AtlasWebServiceClientManager atlasWebServiceClientManager;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.services.AtlasWebServiceClient");

	public void setAtlasWebServiceClientManager(AtlasWebServiceClientManager atlasWebServiceClientManager) {
		this.atlasWebServiceClientManager = atlasWebServiceClientManager;
	}

	public boolean updateCBD(ClientCBDVO clientCBDVO) {
		this.logger.debug("Inside service class to call CBD operation");
		boolean flag = false;

		try {
			flag = this.atlasWebServiceClientManager.updateCBD(clientCBDVO);
		} catch (Exception var4) {
			this.logger.debug("Error.... SOme error occured while calling subject operation request for webservice...");
			this.logger.debug("Error.... " + getStackTraceAsString(var4));
		}

		return flag;
	}

	public boolean updateStatus(ClientCaseStatusVO clinetCaseStatusVO) {
		this.logger.debug("Insdie service class method of update case status operation");
		boolean processFlag = false;

		try {
			processFlag = this.atlasWebServiceClientManager.updateStatus(clinetCaseStatusVO);
		} catch (Exception var4) {
			this.logger.debug("Error.... Some error occured while calling update status request for webservice...");
			this.logger.debug("Error.... " + getStackTraceAsString(var4));
		}

		return processFlag;
	}

	public ISISResponseVO updateMaster(UPMasterVO upMasterVO) throws CMSException {
		this.logger.debug("Insdie service class method of update masters operation");
		ISISResponseVO isisResponseVO = this.atlasWebServiceClientManager.updateMaster(upMasterVO);
		return isisResponseVO;
	}

	public Map updateSubject(ClientSubjectVO clientSubjectVO) {
		this.logger.debug("inside updateSubject of client service");
		this.logger.debug("Insdie service class method of updateSubject operation");
		Object resultMap = new HashMap();

		try {
			resultMap = this.atlasWebServiceClientManager.updateSubject(clientSubjectVO);
		} catch (CMSException var4) {
			this.logger.debug("Error.... SOme error occured while calling subject operation request for webservice...");
			this.logger.debug("Error.... " + getStackTraceAsString(var4));
		}

		return (Map) resultMap;
	}

	public boolean pingISIS() {
		this.logger.debug("Inside pingISIS method of service class");
		boolean responseFlag = this.atlasWebServiceClientManager.pingISIS();
		this.logger.debug("responseFlag value is:::::::" + responseFlag);
		return responseFlag;
	}

	public static String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}

	public void updateISISSubjectId(int atlasSubjectId, String isisSubjectId, String crn) throws CMSException {
		this.atlasWebServiceClientManager.updateISISSubjectId(atlasSubjectId, isisSubjectId, crn);
	}
}