package com.worldcheck.atlas.services.audit;

import com.worldcheck.atlas.bl.interfaces.ICaseHistoryEvents;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.util.ArrayList;
import java.util.List;

public class CaseHistoryService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.audit.CaseHistoryService");
	private ICaseHistoryEvents caseHistoryEventManager;

	public void setCaseHistoryEventManager(ICaseHistoryEvents caseHistoryEventManager) {
		this.caseHistoryEventManager = caseHistoryEventManager;
	}

	public void setCaseHistory(CaseDetails oldCaseDetails, CaseDetails newCaseDetails, CaseHistory caseHistory,
			String module) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistory");

		try {
			this.caseHistoryEventManager.setCaseHistory(oldCaseDetails, newCaseDetails, caseHistory, module);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistory");
	}

	public List<CaseHistory> getCaseHistory(String CRN, int start, int limit, String columnName, String sortDirection,
			List<String> userRoleList) throws CMSException {
		this.logger.debug("In CaseHistoryService::getCaseHistory");
		new ArrayList();

		List caseHistoryList;
		try {
			caseHistoryList = this.caseHistoryEventManager.getCaseHistory(CRN, start, limit, columnName, sortDirection,
					userRoleList);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting CaseHistoryService::getCaseHistory");
		return caseHistoryList;
	}

	public void setCaseHistoryForTaskCompleted(String taskName, String PId, String CRN, String processCycle,
			String taskStatus, String performer, String status) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForTaskCompleted");

		try {
			this.caseHistoryEventManager.setCaseHistoryForTaskCompleted(taskName, PId, CRN, processCycle, taskStatus,
					performer, status);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForTaskCompleted");
	}

	public void setCaseHistoryForTaskStatusChange(CaseHistory caseHistoryForOtherParams) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForTaskStatusChange");

		try {
			this.caseHistoryEventManager.setCaseHistoryForTaskStatusChange(caseHistoryForOtherParams);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForTaskStatusChange");
	}

	public void setCaseHistoryForCaseStatusChange(String oldStatus, String newStatus,
			CaseHistory caseHistoryForOtherParams) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForCaseStatusChange");

		try {
			this.caseHistoryEventManager.setCaseHistoryForCaseStatusChange(oldStatus, newStatus,
					caseHistoryForOtherParams);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForCaseStatusChange");
	}

	public void setCaseHistoryForReviewScoresheet(String action, CaseHistory caseHistoryForOtherParams)
			throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForReviewScoresheet");

		try {
			this.caseHistoryEventManager.setCaseHistoryForReviewScoresheet(action, caseHistoryForOtherParams);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForReviewScoresheet");
	}

	public void setCaseHistoryForRejection(CaseHistory caseHistoryOtherParam) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForRejection");

		try {
			this.caseHistoryEventManager.setCaseHistoryForRejection(caseHistoryOtherParam);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForRejection");
	}

	public void setCaseHistoryForConfirmBudgetAndDueDate(List<UnconfimredBudgetVO> unconfimredBudgetVOList)
			throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForConfirmBudgetAndDueDate");

		try {
			this.caseHistoryEventManager.setCaseHistoryForConfirmBudgetAndDueDate(unconfimredBudgetVOList);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForConfirmBudgetAndDueDate");
	}

	public void setCaseHistoryForComments(String deletedComments, CaseHistory caseHistoryOtherParam)
			throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForComments");

		try {
			this.caseHistoryEventManager.setCaseHistoryForComments(deletedComments, caseHistoryOtherParam);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForComments");
	}

	public void setCaseHistoryForTaskApproved(CaseHistory caseHistory) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForTaskApproved");

		try {
			this.caseHistoryEventManager.setCaseHistoryForTaskApproved(caseHistory);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForTaskApproved");
	}

	public long getTimeStampOfCCS(String pid) throws CMSException {
		this.logger.debug("In CaseHistoryService::getTimeStampOfCCS");
		long timeStamp = 0L;

		try {
			timeStamp = this.caseHistoryEventManager.getTimeStampOfCCS(pid);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryService::getTimeStampOfCCS");
		return timeStamp;
	}

	public void setCaseHistoryForUploadFinalDocument(CaseHistory caseHistory) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForUploadFinalDocument");

		try {
			this.caseHistoryEventManager.setCaseHistoryForUploadFinalDocument(caseHistory);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForUploadFinalDocument");
	}

	public void setCaseHistoryForBulkOfficeAssignment(List<CaseDetails> oldCaseDetailsList,
			List<CaseDetails> newCaseDetailsList) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForBulkOfficeAssignment");

		try {
			this.caseHistoryEventManager.setCaseHistoryForBulkOfficeAssignment(oldCaseDetailsList, newCaseDetailsList);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForBulkOfficeAssignment");
	}

	public void setCaseHistoryForBulkTeamAssignment(List<CaseDetails> oldCaseDetailsList,
			List<CaseDetails> newCaseDetailsList) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForBulkTeamAssignment");

		try {
			this.caseHistoryEventManager.setCaseHistoryForBulkTeamAssignment(oldCaseDetailsList, newCaseDetailsList);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForBulkTeamAssignment");
	}

	public void setCaseHistoryForMassVendorUpload(List<CaseHistory> caseHistoryList) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForMassVendorUpload");

		try {
			this.caseHistoryEventManager.setCaseHistoryForMassVendorUpload(caseHistoryList);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForMassVendorUpload");
	}

	public void setCaseHistoryForCaseCreated(CaseHistory caseHistory) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForCaseCreated");

		try {
			this.caseHistoryEventManager.setCaseHistoryForCaseCreated(caseHistory);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForCaseCreated");
	}

	public void setCaseHistoryForTeamManagerReassign(TeamDetails oldTeamDetailObj, TeamDetails newTeamDetailObj,
			CaseHistory caseHistoryOtherParm) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForTeamManagerReassign");

		try {
			this.caseHistoryEventManager.setCaseHistoryForTeamManagerReassign(oldTeamDetailObj, newTeamDetailObj,
					caseHistoryOtherParm);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForTeamManagerReassign");
	}

	public void setCaseHistoryForBulkTeamManagerReassign(List<CaseDetails> oldCaseDetailsList,
			List<CaseDetails> newCaseDetailsList) throws CMSException {
		this.logger.debug("In CaseHistoryService::setCaseHistoryForTeamManagerReassign");

		try {
			this.caseHistoryEventManager.setCaseHistoryForBulkTeamManagerReassign(oldCaseDetailsList,
					newCaseDetailsList);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting CaseHistoryService::setCaseHistoryForTeamManagerReassign");
	}
}