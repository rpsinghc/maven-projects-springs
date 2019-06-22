package com.worldcheck.atlas.isis.bl;

import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.dao.AtlasWebServiceClientDAO;
import com.worldcheck.atlas.isis.util.AtlasWebServiceUtil;
import com.worldcheck.atlas.isis.util.WebServicePropertyReaderUtil;
import com.worldcheck.atlas.isis.vo.ClientCBDVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.isis.vo.ClientSubjectVO;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.HashMap;
import java.util.Map;

public class AtlasWebServiceClientManager {
	private AtlasWebServiceClientDAO atlasWebServiceClientDAO;
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.isis.bl.AtlasWebServiceClientManager");
	private String crn;
	private String errorType;
	private boolean isCommunicationError;
	private long maxRetryCount;
	private boolean maxRetryFlag;
	private Map operationObject;
	private boolean processFlag;
	private long retryAttemptCount;
	private long retryGapTime;
	private String operationType;
	private Object reqObject;
	private long queueRecordId;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getErrorType() {
		return this.errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public boolean isCommunicationError() {
		return this.isCommunicationError;
	}

	public void setCommunicationError(boolean isCommunicationError) {
		this.isCommunicationError = isCommunicationError;
	}

	public long getMaxRetryCount() {
		return this.maxRetryCount;
	}

	public void setMaxRetryCount(long maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	public boolean isMaxRetryFlag() {
		return this.maxRetryFlag;
	}

	public void setMaxRetryFlag(boolean maxRetryFlag) {
		this.maxRetryFlag = maxRetryFlag;
	}

	public Map getOperationObject() {
		return this.operationObject;
	}

	public void setOperationObject(Map operationObject) {
		this.operationObject = operationObject;
	}

	public boolean isProcessFlag() {
		return this.processFlag;
	}

	public void setProcessFlag(boolean processFlag) {
		this.processFlag = processFlag;
	}

	public long getRetryAttemptCount() {
		return this.retryAttemptCount;
	}

	public void setRetryAttemptCount(long retryAttemptCount) {
		this.retryAttemptCount = retryAttemptCount;
	}

	public long getRetryGapTime() {
		return this.retryGapTime;
	}

	public void setRetryGapTime(long retryGapTime) {
		this.retryGapTime = retryGapTime;
	}

	public String getOperationType() {
		return this.operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Object getReqObject() {
		return this.reqObject;
	}

	public void setReqObject(Object reqObject) {
		this.reqObject = reqObject;
	}

	public long getQueueRecordId() {
		return this.queueRecordId;
	}

	public void setQueueRecordId(long queueRecordId) {
		this.queueRecordId = queueRecordId;
	}

	public void setAtlasWebServiceClientDAO(AtlasWebServiceClientDAO atlasWebServiceClientDAO) {
		this.atlasWebServiceClientDAO = atlasWebServiceClientDAO;
	}

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public boolean updateCBD(ClientCBDVO clientCBDVO) throws CMSException {
		this.logger.debug("inside updateCBD method of AtlasWebServiceClientManager class");
		String operationType = "ConfirmBudgetAndDueDateOperation";
		return this.updateISISMethod(operationType, clientCBDVO);
	}

	public Boolean updateStatus(ClientCaseStatusVO clientCaseStatusVO) throws CMSException {
		this.logger.debug("inside updateStatus method of AtlasWebServiceClientManager class");
		String operationType = "UpdateStatusOperation";
		return this.updateISISMethod(operationType, clientCaseStatusVO);
	}

	public ISISResponseVO updateMaster(UPMasterVO upMasterVO) throws CMSException {
		this.logger.debug("inside updateMaster method of AtlasWebServiceClientManager class");
		ISISResponseVO isisResponseVO = AtlasWebServiceUtil.updateISISMaster(upMasterVO);
		return isisResponseVO;
	}

	public Map updateSubject(ClientSubjectVO subjectDetailsVO) throws CMSException {
		this.logger.debug("inside ClientSubjectVO method of AtlasWebServiceClientManager class");
		Map dataMap = new HashMap();
		dataMap.put("OperationType", "SubjectOperation");
		dataMap.put("ReqObject", subjectDetailsVO);
		dataMap.put("QueueRecordId", new Long(-1L));
		this.logger.debug("dataMap value is:::::" + dataMap);
		Object resultMap = new HashMap();

		try {
			resultMap = AtlasWebServiceUtil.callISISWebService(dataMap);
		} catch (Exception var5) {
			this.logger
					.debug("Error:::Some Error occured in updateSubject method of AtlasWebServiceClientManager class..."
							+ var5.getMessage());
			this.logger.debug("Error message is::::" + AtlasWebServiceUtil.getStackTraceAsString(var5));
		}

		return (Map) resultMap;
	}

	public boolean pingISIS() {
		this.logger.debug("inside pingISIS method of AtlasWebServiceClientManager class");
		boolean responseFlag = AtlasWebServiceUtil.pingISIS();
		return responseFlag;
	}

	private boolean updateISISMethod(String operationType, Object reqObject) {
		Map dataMap = new HashMap();
		dataMap.put("OperationType", operationType);
		if (operationType.equals("UpdateStatusOperation")) {
			dataMap.put("ReqObject", (ClientCaseStatusVO) reqObject);
		} else if (operationType.equals("ConfirmBudgetAndDueDateOperation")) {
			dataMap.put("ReqObject", (ClientCBDVO) reqObject);
		} else if (operationType.equals("SubjectOperation")) {
			dataMap.put("ReqObject", (ClientSubjectVO) reqObject);
		}

		dataMap.put("QueueRecordId", new Long(-1L));
		this.logger.debug("dataMap value is:::::" + dataMap);

		try {
			Map resultMap = AtlasWebServiceUtil.callISISWebService(dataMap);
			this.logger.debug("resultMap value is:::::::" + resultMap);
			int successFlag = (Integer) resultMap.get("successFlag");
			int communicationErrorFlag = (Integer) resultMap.get("communicationErrorFlag");
			this.logger.debug("successFlag value is::::::" + successFlag);
			this.logger.debug("communicationErrorFlag value is::::" + communicationErrorFlag);
			String errorCode = "";
			if (communicationErrorFlag == 1) {
				errorCode = (String) resultMap.get("errorCode");
			}

			if (successFlag == 1) {
				this.processFlag = true;
			} else {
				this.processFlag = false;
				if (communicationErrorFlag == 0) {
					this.isCommunicationError = true;
				}
			}

			++this.retryAttemptCount;
			if (this.retryAttemptCount > this.getMaxRetryCount()) {
				this.maxRetryFlag = true;
			} else {
				this.maxRetryFlag = false;
			}

			this.logger.debug("retryAttemptCount is::::::::" + this.retryAttemptCount);
		} catch (Exception var8) {
			this.logger.debug(
					"Error:::Some Error occured in updateISISMethod method of AtlasWebServiceClientManager class..."
							+ var8.getMessage());
			this.logger.debug("Error message is::::" + AtlasWebServiceUtil.getStackTraceAsString(var8));
		}

		return this.processFlag;
	}

	public void updateISISSubjectId(int atlasSubjectId, String isisSubjectId, String crn) throws CMSException {
		AtlasWebServiceUtil.updateISISSubjectId(atlasSubjectId, isisSubjectId, crn);
	}
}