package com.worldcheck.atlas.scheduler.service;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.bl.SchedulerManager;

public class OverdueCaseScheduleService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.service.OverdueCaseScheduleService");
	private SchedulerManager schedulerManager;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public void sendNotificationForOverdueCases() throws CMSException {
		this.logger.debug("In OverdueCaseScheduleService::sendNotificationForOverdueCases");
		this.schedulerManager.sendNotificationForOverdueCases();
		this.logger.debug("Exiting OverdueCaseScheduleService::sendNotificationForOverdueCases");
	}
}