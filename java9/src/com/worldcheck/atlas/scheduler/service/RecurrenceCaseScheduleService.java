package com.worldcheck.atlas.scheduler.service;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.bl.SchedulerManager;

public class RecurrenceCaseScheduleService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.service.RecurrenceCaseScheduleService");
	private SchedulerManager schedulerManager;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public void getAvailableRecCasesForSchedulerAndSendNotification() throws CMSException {
		this.logger.debug("In RecurrenceCaseScheduleService::getAvailableRecCasesForSchedulerAndSendNotification");
		this.schedulerManager.getAvailableRecCasesForSchedulerAndSendNotification();
		this.logger.debug("Exiting RecurrenceCaseScheduleService::getAvailableRecCasesForSchedulerAndSendNotification");
	}

	public void createRecurrCase(String recCaseSchedulerId, String recClientCaseId, String performer, Session session)
			throws CMSException {
		this.logger.debug("In RecurrenceCaseScheduleService::createRecurrCase");
		this.schedulerManager.createRecurrCase(recCaseSchedulerId, recClientCaseId, performer, session);
		this.logger.debug("Exiting RecurrenceCaseScheduleService::createRecurrCase");
	}

	public void updateAcknowledgeDate(String recCaseSchedulerId, String updatedBy) throws CMSException {
		this.logger.debug("In RecurrenceCaseScheduleService::updateAcknowledgeDate");
		this.schedulerManager.updateAcknowledgeDate(recCaseSchedulerId, updatedBy);
		this.logger.debug("Exiting RecurrenceCaseScheduleService::updateAcknowledgeDate");
	}
}