package com.worldcheck.atlas.scheduler.job;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class RecurrenceCaseScheduleJob implements StatefulJob {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.job.RecurrenceCaseScheduleJob");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		this.logger.debug("in RecurrenceCaseScheduleJob ::execute @ " + new Date());

		try {
			ResourceLocator.self().getAtlasRecurrCaseSchedulerService()
					.getAvailableRecCasesForSchedulerAndSendNotification();
		} catch (Exception var3) {
			this.logger.error(var3);
		}

		this.logger.debug("Exit RecurrenceCaseScheduleJob ::execute @ " + new Date());
	}
}