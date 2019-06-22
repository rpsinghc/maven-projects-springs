package com.worldcheck.atlas.bl.createcase;

import com.worldcheck.atlas.dao.createcase.CreateCaseDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.CaseLevelRiskProfileDetailsVO;

public class CreateCaseManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.createcase.CreateCaseManager");
	private CreateCaseDAO createCaseDAO = null;

	public void setCreateCaseDAO(CreateCaseDAO createCaseDAO) {
		this.createCaseDAO = createCaseDAO;
	}

	public void createCase(CaseDetails caseDetail) throws CMSException {
		this.logger.debug("inside manager method");
		this.createCaseDAO.createCase(caseDetail);
	}

	public void insertRiskProfileData(CaseLevelRiskProfileDetailsVO caseLevelRiskProfileVO) throws CMSException {
		this.logger.debug("inside manager method");
		this.createCaseDAO.insertRiskProfileData(caseLevelRiskProfileVO);
	}

	public boolean checkForCase(String crn) throws CMSException {
		boolean isCaseExists = false;
		this.logger.debug("inside checkForCase");
		if (this.createCaseDAO.checkForCase(crn) > 0) {
			isCaseExists = true;
		}

		return isCaseExists;
	}

	public boolean checkForRiskData(String crn) throws CMSException {
		boolean isRiskDataExist = false;
		this.logger.debug("inside checkForRiskData");
		if (this.createCaseDAO.checkForRiskData(crn) > 0) {
			isRiskDataExist = true;
		}

		return isRiskDataExist;
	}

	public boolean checkForRiskHistoryData(String crn) throws CMSException {
		boolean isRiskHistoryDataExist = false;
		this.logger.debug("inside checkForRiskHistoryData");
		if (this.createCaseDAO.checkForRiskHistoryData(crn) > 0) {
			isRiskHistoryDataExist = true;
		}

		return isRiskHistoryDataExist;
	}

	public boolean checkForRecurrCase(String crn) throws CMSException {
		boolean isCaseExists = false;
		this.logger.debug("inside checkForRecurrCase");
		if (this.createCaseDAO.checkForRecurrCase(crn) > 0) {
			isCaseExists = true;
		}

		return isCaseExists;
	}

	public boolean isCaseDeleted(String crn) throws CMSException {
		boolean isCaseRemoved = false;
		this.logger.debug("inside isCaseDeleted");
		if (this.createCaseDAO.removeCase(crn) > 0) {
			isCaseRemoved = true;
		}

		return isCaseRemoved;
	}

	public boolean isRiskDataDeleted(String crn) throws CMSException {
		boolean isCaseRemoved = false;
		this.logger.debug("inside isRiskDataDeleted");
		if (this.createCaseDAO.removeRiskData(crn) > 0) {
			isCaseRemoved = true;
		}

		return isCaseRemoved;
	}

	public boolean isRiskHistoryDataDeleted(String crn) throws CMSException {
		boolean isCaseRiskHistoryRemoved = false;
		this.logger.debug("inside isRiskHistoryDataDeleted");
		if (this.createCaseDAO.removeRiskHistoryData(crn) > 0) {
			isCaseRiskHistoryRemoved = true;
		}

		return isCaseRiskHistoryRemoved;
	}

	public boolean isRecurrCaseDeleted(String crn) throws CMSException {
		boolean isCaseRemoved = false;
		this.logger.debug("inside isRecurrCaseDeleted");
		if (this.createCaseDAO.removeRecurrCase(crn) > 0) {
			isCaseRemoved = true;
		}

		return isCaseRemoved;
	}

	public String getAssociatedBDMforClient(String clientName) throws CMSException {
		return this.createCaseDAO.getAssociatedBDMforClient(clientName);
	}

	public void createCaseForISIS(CaseDetails caseDetail) throws CMSException {
		this.logger.debug("inside manager method");
		this.createCaseDAO.createCaseForISIS(caseDetail);
	}
}