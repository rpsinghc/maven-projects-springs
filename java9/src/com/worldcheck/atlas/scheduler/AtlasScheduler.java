package com.worldcheck.atlas.scheduler;

import com.ibatis.common.resources.Resources;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.job.EmailPrepaymentCaseJob;
import com.worldcheck.atlas.scheduler.job.OverdueCaseJob;
import com.worldcheck.atlas.scheduler.job.RecurrenceCaseScheduleJob;
import com.worldcheck.atlas.scheduler.job.TempFeedbackAttachRemoveJob;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServlet;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class AtlasScheduler extends HttpServlet {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.scheduler.AtlasScheduler");
	private static final long serialVersionUID = 1L;

	public AtlasScheduler() throws Exception {
		this.logger.debug("in AtlasScheduler::AtlasScheduler()");
		SchedulerFactory schduleFactory = new StdSchedulerFactory();
		Scheduler sched = schduleFactory.getScheduler();

		try {
			sched.start();
			this.ParseXmlAndCreateJob(sched);
		} catch (Exception var4) {
			sched.shutdown();
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting AtlasScheduler::AtlasScheduler()");
	}

	private void ParseXmlAndCreateJob(Scheduler sched) throws CMSException {
		this.logger.debug("In AtlasScheduler::ParseXmlAndCreateJob");

		try {
			SAXReader reader = new SAXReader();
			Reader reader1 = Resources.getResourceAsReader("AtlasScheduleServices.xml");
			Document doc = reader.read(reader1);
			List<Node> myNodeList = doc.selectNodes("//ScheduledJobs/JobClass");
			this.logger.debug("myNodeList size is ----" + myNodeList.size());
			String jobId = "";
			String jobName = "";
			String className = "";
			String triggerName = "";
			String jobGroup = "";
			String runTime = "";
			String serviceName = "";
			Iterator iter = myNodeList.iterator();

			while (iter.hasNext()) {
				Node tstNode = (Node) iter.next();
				jobName = tstNode.valueOf("@jobName");
				className = tstNode.valueOf("@className");
				triggerName = tstNode.valueOf("@triggerName");
				jobGroup = tstNode.valueOf("@jobGroup");
				runTime = tstNode.valueOf("@runTime");
				serviceName = tstNode.valueOf("@serviceName");
				JobDetail jobdetail = this.CreateJobDetails(jobName, jobGroup, className, serviceName);
				sched.addJob(jobdetail, true);
				Calendar cal = Calendar.getInstance();
				int minutesToAdd = 2;
				cal.setTime(new Date());
				cal.add(12, minutesToAdd);
				CronTrigger crontrigger = this.CreateCronTriger(triggerName, jobGroup, jobName, jobGroup, className,
						runTime);
				sched.scheduleJob(crontrigger);
			}
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}

		this.logger.debug("Exiting AtlasScheduler::ParseXmlAndCreateJob");
	}

	private JobDetail CreateJobDetails(String jobName, String jobGroup, String jobClass, String serviceName)
			throws CMSException {
		JobDetail jobdetail = null;
		this.logger.debug("In AtlasScheduler::CreateJobDetails");

		try {
			if (serviceName.equalsIgnoreCase("AtlasRecurenceCaseSchedulerService")) {
				jobdetail = new JobDetail(jobName, jobGroup, RecurrenceCaseScheduleJob.class);
				this.logger.debug("creating job for AtlasRecurenceCaseSchedulerService");
			}

			if (serviceName.equalsIgnoreCase("AtlasOverdueCasesService")) {
				jobdetail = new JobDetail(jobName, jobGroup, OverdueCaseJob.class);
				this.logger.debug("creating job for AtlasOverdueCasesService");
			}

			if (serviceName.equalsIgnoreCase("AtlasEmailPrepaymentCaseService")) {
				jobdetail = new JobDetail(jobName, jobGroup, EmailPrepaymentCaseJob.class);
				this.logger.debug("creating job for AtlasEmailPrepaymentCaseService");
			}

			if (serviceName.equalsIgnoreCase("AtlasTempFeedbackAttachRemoveService")) {
				jobdetail = new JobDetail(jobName, jobGroup, TempFeedbackAttachRemoveJob.class);
				this.logger.debug("creating job for AtlasTempFeedbackAttachRemoveService");
			}
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting AtlasScheduler::CreateJobDetails");
		return jobdetail;
	}

	private CronTrigger CreateCronTriger(String name, String group, String jobName, String jobGroup, String jobClass,
			String cronExperession) throws CMSException {
		this.logger.debug("In AtlasScheduler::CreateCronTriger");
		new CronTrigger();

		CronTrigger crontrigger1;
		try {
			crontrigger1 = new CronTrigger(name, group, jobName, jobGroup, cronExperession);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("Exiting AtlasScheduler::CreateCronTriger");
		return crontrigger1;
	}
}