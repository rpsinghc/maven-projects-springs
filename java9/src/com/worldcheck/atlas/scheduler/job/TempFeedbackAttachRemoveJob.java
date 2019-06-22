package com.worldcheck.atlas.scheduler.job;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class TempFeedbackAttachRemoveJob implements StatefulJob {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.scheduler.job.TempFeedbackAttachRemoveJob");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		this.logger.debug("In TempFeedbackAttachRemoveJob :: execute @" + new Date());

		try {
			ResourceLocator.self().getAtlasTempFeedbackAttachRemoveService().removeTempFeedbackAttach();
		} catch (Exception var3) {
			this.logger.error(var3);
		}

		this.logger.debug("Exit TempFeedbackAttachRemoveJob :: execute @" + new Date());
	}
}