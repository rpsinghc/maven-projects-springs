package com.worldcheck.atlas.scheduler.service;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.bl.SchedulerManager;
import java.text.ParseException;

public class EmailPrepaymentCaseService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.service.EmailPrepaymentCaseService");
	private SchedulerManager schedulerManager;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public void sendEmailPrepayementCase() throws CMSException, ParseException {
		this.logger.debug("In EmailPrepaymentCaseService:::: sendEmailPrepayementCase method");
		this.schedulerManager.sendEmailPrepayementCase();
		this.logger.debug("Exit EmailPrepaymentCaseService:::: sendEmailPrepayementCase method");
	}
}