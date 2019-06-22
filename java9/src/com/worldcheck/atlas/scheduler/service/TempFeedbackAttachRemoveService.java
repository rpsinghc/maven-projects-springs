package com.worldcheck.atlas.scheduler.service;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.bl.SchedulerManager;
import java.io.IOException;
import java.text.ParseException;

public class TempFeedbackAttachRemoveService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.service.TempFeedbackAttachRemoveService");
	private SchedulerManager schedulerManager;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public void removeTempFeedbackAttach() throws CMSException, ParseException, IOException {
		this.logger.debug("In TempFeedbackAttachRemoveService:::: removeTempFeedbackAttach method");
		this.schedulerManager.removeTempFeedbackAttach();
		this.logger.debug("Exit TempFeedbackAttachRemoveService:::: removeTempFeedbackAttach method");
	}
}