package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.bl.backup.UserBackUpManager;
import com.worldcheck.atlas.bl.createcase.CreateCaseManager;
import com.worldcheck.atlas.bl.interfaces.ICurrencyConversion;
import com.worldcheck.atlas.bl.interfaces.ILeaveMaster;
import com.worldcheck.atlas.bl.interfaces.IReviewTaskManagementService;
import com.worldcheck.atlas.bl.interfaces.IUnconfirmBudget;
import com.worldcheck.atlas.bl.interfaces.IUserMaster;
import com.worldcheck.atlas.cache.service.CacheService;
import com.worldcheck.atlas.isis.services.AtlasWebService;
import com.worldcheck.atlas.isis.services.AtlasWebServiceClient;
import com.worldcheck.atlas.scheduler.service.EmailPrepaymentCaseService;
import com.worldcheck.atlas.scheduler.service.OverdueCaseScheduleService;
import com.worldcheck.atlas.scheduler.service.RecurrenceCaseScheduleService;
import com.worldcheck.atlas.scheduler.service.TempFeedbackAttachRemoveService;
import com.worldcheck.atlas.services.audit.CaseHistoryService;
import com.worldcheck.atlas.services.audit.ReviewHistoryService;
import com.worldcheck.atlas.services.casedetail.CaseDetailService;
import com.worldcheck.atlas.services.comments.CommentService;
import com.worldcheck.atlas.services.dashboard.AtlasDashboardService;
import com.worldcheck.atlas.services.document.DocService;
import com.worldcheck.atlas.services.flowcontroller.FlowService;
import com.worldcheck.atlas.services.mail.MailService;
import com.worldcheck.atlas.services.notification.NotificationService;
import com.worldcheck.atlas.services.officeassignment.OfficeAssignmentService;
import com.worldcheck.atlas.services.sbm.SBMService;
import com.worldcheck.atlas.services.sbm.acl.ACLService;
import com.worldcheck.atlas.services.subject.SubjectService;
import com.worldcheck.atlas.services.task.ReviewTaskManagementService;
import com.worldcheck.atlas.services.task.TaskManagementService;
import com.worldcheck.atlas.services.task.invoice.InvoiceManagementService;
import com.worldcheck.atlas.services.teamassignment.TeamAssignmentService;
import com.worldcheck.atlas.services.timetracker.TimeTrackerService;
import com.worldcheck.atlas.services.updatepassword.UpdatePasswordService;
import com.worldcheck.atlas.services.visualkey.VisualKeyService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ResourceLocator {
	private static final String[] RESOURCES = new String[]{"classpath:/atlasDispatcher-servlet.xml",
			"classpath:/ehcache-servlet.xml"};
	private static ResourceLocator self = null;
	private ClassPathXmlApplicationContext appcontext = null;
	private static final String SBMServices = "sbmService";
	private static final String ACLServices = "aclService";
	private static final String DOCServices = "docService";
	private static final String CacheService = "cacheService";
	private static final String TaskManagementService = "taskService";
	private static final String SubjectService = "subjectService";
	private static final String InvoiceManagementService = "invoiceService";
	private static final String UserMultiActionManager = "userMultiActionManager";
	private static final String IUserMaster = "private static final String";
	private static final String NotificationService = "notificationService";
	private static final String CreateCaseManagerBean = "createCaseManager";
	private static final String MailService = "mailService";
	private static final String LeaveManager = "leaveService";
	private static final String FlowService = "flowService";
	private static final String OfficeAssignmentService = "officeAssignmentService";
	private static final String CommentService = "commentService";
	private static final String VisualKeyService = "visualKeyService";
	private static final String ReviewTaskManagementService = "reviewTaskManagementService";
	private static final String ReviewHistoryService = "reviewHistoryService";
	private static final String CaseHistoryService = "caseHistoryService";
	private static final String TeamAssignmentService = "teamAssignmentService";
	private static final String TimeTrackerService = "timeTrackerService";
	private static final String currencyConversionService = "currencyConversionService";
	private static final String caseDetailService = "caseDetailService";
	private static final String AtlasWebService = "atlasWebService";
	private static final String AtlasWebServiceClient = "atlasWebServiceClient";
	private static final String AtlasRecurrCaseSchedulerService = "atlasRecurrCaseSchedulerService";
	private static final String Update_authtwo_Service = "updatePasswordService";
	private static final String AtlasOverdueCasesService = "atlasOverDueCasesService";
	private static final String UserBackUpManagerService = "userBackUpManager";
	private static final String AtlasDashboardService = "atlasDashboardService";
	private static final String AtlasEmailPrepaymentCaseService = "atlasEmailPrepaymentCaseService";
	private static final String AtlasTempFeedbackAttachRemoveService = "atlasTempFeedbackAttachRemoveService";
	private static final String UnconfirmBudgetManagerService = "unconfirmBudgetManager";

	public static ResourceLocator self() {
		if (self == null) {
			self = new ResourceLocator();
			self.initialize();
		}

		return self;
	}

	public ClassPathXmlApplicationContext getContext() {
		if (this.appcontext == null) {
			self.initialize();
		}

		return this.appcontext;
	}

	private void initialize() {
		this.appcontext = new ClassPathXmlApplicationContext(RESOURCES);
	}

	public SBMService getSBMService() {
		return (SBMService) this.getContext().getBean("sbmService", SBMService.class);
	}

	public ACLService getACLService() {
		return (ACLService) this.getContext().getBean("aclService", ACLService.class);
	}

	public DocService getDocService() {
		return (DocService) this.getContext().getBean("docService", DocService.class);
	}

	public CacheService getCacheService() {
		return (CacheService) this.getContext().getBean("cacheService", CacheService.class);
	}

	public SubjectService getSubjectService() {
		return (SubjectService) this.getContext().getBean("subjectService", SubjectService.class);
	}

	public TaskManagementService getTaskService() {
		return (TaskManagementService) this.getContext().getBean("taskService", TaskManagementService.class);
	}

	public InvoiceManagementService getInvoiceService() {
		return (InvoiceManagementService) this.getContext().getBean("invoiceService", InvoiceManagementService.class);
	}

	public IUserMaster getUserService() {
		return (IUserMaster) this.getContext().getBean("userMultiActionManager", IUserMaster.class);
	}

	public NotificationService getNotificationService() {
		return (NotificationService) this.getContext().getBean("notificationService", NotificationService.class);
	}

	public CreateCaseManager getCreateCaseManager() {
		return (CreateCaseManager) this.getContext().getBean("createCaseManager", CreateCaseManager.class);
	}

	public MailService getMailService() {
		return (MailService) this.getContext().getBean("mailService", MailService.class);
	}

	public ILeaveMaster getLeaveService() {
		return (ILeaveMaster) this.getContext().getBean("leaveService", ILeaveMaster.class);
	}

	public FlowService getFlowService() {
		return (FlowService) this.getContext().getBean("flowService", FlowService.class);
	}

	public OfficeAssignmentService getOfficeAssignmentService() {
		return (OfficeAssignmentService) this.getContext().getBean("officeAssignmentService",
				OfficeAssignmentService.class);
	}

	public CommentService getCommentService() {
		return (CommentService) this.getContext().getBean("commentService", CommentService.class);
	}

	public VisualKeyService getVisualKeyService() {
		return (VisualKeyService) this.getContext().getBean("visualKeyService", VisualKeyService.class);
	}

	public IReviewTaskManagementService getReviewTaskManagementService() {
		return (ReviewTaskManagementService) this.getContext().getBean("reviewTaskManagementService",
				IReviewTaskManagementService.class);
	}

	public ReviewHistoryService getReviewHistoryService() {
		return (ReviewHistoryService) this.getContext().getBean("reviewHistoryService", ReviewHistoryService.class);
	}

	public CaseHistoryService getCaseHistoryService() {
		return (CaseHistoryService) this.getContext().getBean("caseHistoryService", CaseHistoryService.class);
	}

	public TeamAssignmentService getTeamAssignmentService() {
		return (TeamAssignmentService) this.getContext().getBean("teamAssignmentService", TeamAssignmentService.class);
	}

	public TimeTrackerService getTimeTrackerService() {
		return (TimeTrackerService) this.getContext().getBean("timeTrackerService", TimeTrackerService.class);
	}

	public ICurrencyConversion getCurrencyConversionService() {
		return (ICurrencyConversion) this.getContext().getBean("currencyConversionService", ICurrencyConversion.class);
	}

	public CaseDetailService getCaseDetailService() {
		return (CaseDetailService) this.getContext().getBean("caseDetailService", CaseDetailService.class);
	}

	public AtlasWebService getAtlasWebService() {
		return (AtlasWebService) this.getContext().getBean("atlasWebService", AtlasWebService.class);
	}

	public AtlasWebServiceClient getAtlasWebServiceClient() {
		return (AtlasWebServiceClient) this.getContext().getBean("atlasWebServiceClient", AtlasWebServiceClient.class);
	}

	public RecurrenceCaseScheduleService getAtlasRecurrCaseSchedulerService() {
		return (RecurrenceCaseScheduleService) this.getContext().getBean("atlasRecurrCaseSchedulerService",
				RecurrenceCaseScheduleService.class);
	}

	public UpdatePasswordService getUpdatePasswordService() {
		return (UpdatePasswordService) this.getContext().getBean("updatePasswordService", UpdatePasswordService.class);
	}

	public OverdueCaseScheduleService getAtlasOverdueCasesService() {
		return (OverdueCaseScheduleService) this.getContext().getBean("atlasOverDueCasesService",
				OverdueCaseScheduleService.class);
	}

	public UserBackUpManager getUserBackUpManager() {
		return (UserBackUpManager) this.getContext().getBean("userBackUpManager", UserBackUpManager.class);
	}

	public AtlasDashboardService getAtlasDashboardService() {
		return (AtlasDashboardService) this.getContext().getBean("atlasDashboardService", AtlasDashboardService.class);
	}

	public EmailPrepaymentCaseService getAtlasEmailPrepaymentCaseService() {
		return (EmailPrepaymentCaseService) this.getContext().getBean("atlasEmailPrepaymentCaseService",
				EmailPrepaymentCaseService.class);
	}

	public TempFeedbackAttachRemoveService getAtlasTempFeedbackAttachRemoveService() {
		return (TempFeedbackAttachRemoveService) this.getContext().getBean("atlasTempFeedbackAttachRemoveService",
				TempFeedbackAttachRemoveService.class);
	}

	public IUnconfirmBudget getUnconfirmBudgetManagerService() {
		return (IUnconfirmBudget) this.getContext().getBean("unconfirmBudgetManager", IUnconfirmBudget.class);
	}
}