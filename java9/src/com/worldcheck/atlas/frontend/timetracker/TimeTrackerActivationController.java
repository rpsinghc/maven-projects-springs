package com.worldcheck.atlas.frontend.timetracker;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.savvion.sbm.bpmportal.bizsite.util.HexDecoder;
import com.savvion.sbm.bpmportal.mvc2.Controller;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TimeTrackerActivationController extends Controller {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.timetracker.TimeTrackerActivationController");
	private static final long serialVersionUID = 1L;
	private String PROCESS_CYCLE = "processCycle";
	private String BIZSITE_TASKNAME = "bizsite_taskNameE";
	public String TASK_STATUS = "taskStatus";

	public void init(ServletConfig servletconfig) throws ServletException {
		this.logger.debug("TimeTrackerActivationController: initialized");
		super.init(servletconfig);
	}

	public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws ServletException, IOException {
		this.logger.debug("TimeTrackerActivationController: doGet");
		boolean invoiceTaskFlag = false;
		ResourceLocator resourceLocator = ResourceLocator.self();
		HttpSession httpSession = httpservletrequest.getSession();
		UserBean userBean = (UserBean) httpSession.getAttribute("userBean");

		try {
			String taskName;
			String crn;
			if (null != httpservletrequest.getParameter(this.BIZSITE_TASKNAME)
					&& httpservletrequest.getParameter(this.BIZSITE_TASKNAME).trim().length() > 0
					&& null != httpservletrequest.getParameter("crn")
					&& httpservletrequest.getParameter("crn").trim().length() > 0) {
				this.logger.debug("starting time tracker activity  for CRN :: "
						+ HexDecoder.decode(httpservletrequest.getParameter("crn")));
				this.logger.debug("userBean ::" + userBean.getUserName() + " :: Process cycle :: "
						+ httpservletrequest.getParameter(this.PROCESS_CYCLE));
				this.logger.debug("task Name ::" + httpservletrequest.getParameter(this.BIZSITE_TASKNAME)
						+ " :: task status :: " + httpservletrequest.getParameter(this.TASK_STATUS));
				taskName = httpservletrequest.getParameter(this.BIZSITE_TASKNAME);
				crn = HexDecoder.decode(httpservletrequest.getParameter("crn"));
				String processCycle = "";
				if (null != httpservletrequest.getParameter(this.PROCESS_CYCLE)
						&& httpservletrequest.getParameter(this.PROCESS_CYCLE).trim().length() > 0) {
					processCycle = HexDecoder.decode(httpservletrequest.getParameter(this.PROCESS_CYCLE));
				}

				String taskStatus = "";
				if (null != httpservletrequest.getParameter(this.TASK_STATUS)
						&& httpservletrequest.getParameter(this.TASK_STATUS).trim().length() > 0) {
					taskStatus = HexDecoder.decode(httpservletrequest.getParameter(this.TASK_STATUS));
				}

				String decodedTaskName = HexDecoder.decode(taskName);
				this.logger.debug("decoded items TaskName ::" + decodedTaskName + " :: crn ::" + crn
						+ " :: process cycle :: " + processCycle + " :: task status :: " + taskStatus);
				String[] pinfo = decodedTaskName.split("#");
				String pid = "0";
				if (pinfo.length == 2) {
					pid = pinfo[1].split("::")[0];
				}

				this.logger.debug("PID :: " + pid);
				boolean wiExists = ResourceLocator.self().getSBMService().wiExistsForUser(decodedTaskName,
						SBMUtils.getSession(httpservletrequest), userBean.getUserName());
				this.logger.debug("wiExists for :: " + decodedTaskName + " :: is :: " + wiExists);
				if (wiExists) {
					CaseDetails caseDetails = resourceLocator.getCaseDetailService().getCaseStatus(crn);
					this.logger.debug("case status :: " + caseDetails.getCaseStatus());
					if (null != caseDetails.getCaseStatus()
							&& !caseDetails.getCaseStatus().equalsIgnoreCase("Cancelled")) {
						String trackerParam = (String) httpSession.getAttribute("TrackerOn");
						if (null != trackerParam && !trackerParam.equals("")) {
							resourceLocator.getTimeTrackerService().stopTimeTrackerForUser(userBean.getUserName());
							httpSession.removeAttribute("TrackerOn");
						}

						if (decodedTaskName.split("::")[1].equals("Invoicing Task")) {
							int count = resourceLocator.getTimeTrackerService().checkTTOnForTask(crn,
									decodedTaskName.split("::")[1], userBean.getUserName());
							if (count > 0) {
								invoiceTaskFlag = true;
							}

							this.logger.debug(" the task is invoicing ..count :: " + count + " :: invoiceTaskFlag :: "
									+ invoiceTaskFlag);
						}

						if (!invoiceTaskFlag) {
							if (!decodedTaskName.split("::")[1].equals("Complete Case Creation")) {
								TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
								timeTrackerVO.setCrn(crn);
								timeTrackerVO.setUserName(userBean.getUserName());
								timeTrackerVO.setTaskPid(pid);
								timeTrackerVO.setProcessCycle(processCycle);
								timeTrackerVO.setTaskStatus(taskStatus);
								timeTrackerVO
										.setTaskName(null == decodedTaskName ? "" : decodedTaskName.split("::")[1]);
								timeTrackerVO.setWorkItemName(null == decodedTaskName ? "" : decodedTaskName);
								timeTrackerVO.setUpdatedBy(userBean.getUserName());
								int timeTrackerId = resourceLocator.getTimeTrackerService()
										.startTimeTracker(timeTrackerVO);
								timeTrackerVO.setTrackerId(timeTrackerId);
								httpSession.setAttribute("trackerBean", timeTrackerVO);
								httpSession.setAttribute("TrackerOn", "TrackerOn");
								ResourceLocator.self().getCacheService().updateTTTokenCache(userBean.getUserName(),
										timeTrackerId + "");
								httpSession.removeAttribute("workItem");
								this.logger.debug(" checking isisFlag for crn :: " + crn);
								if (null != crn && !crn.equalsIgnoreCase("null")) {
									int isisFlag = ResourceLocator.self().getInvoiceService().getBudgetDetails(crn);
									this.logger.debug(" isisFlag for crn :: " + crn + " :: is :: " + isisFlag);
									httpSession.setAttribute("isisFlag", isisFlag + "");
								}
							} else {
								httpSession.setAttribute("workItem", null == decodedTaskName ? "" : decodedTaskName);
								httpSession.setAttribute("timeTrackerCrn", crn);
							}

							HashMap<String, Object> dsValues = new HashMap();
							if (null != decodedTaskName && decodedTaskName.contains("CaseCreation")) {
								if (decodedTaskName.split("::")[1].equals("Office Assignment Task")) {
									dsValues.put("OfficeTaskSTatus", "In Progress");
								} else if (decodedTaskName.split("::")[1].equals("Complete Case Creation")) {
									dsValues.put("CompleteTaskStatus", "In Progress");
								} else if (decodedTaskName.split("::")[1].equals("Consolidation Task")) {
									dsValues.put("ConsolidationTaskStatus", "In Progress");
								} else if (decodedTaskName.split("::")[1].equals("Client Submission Task")) {
									dsValues.put("ClientSubmissionTaskStatus", "In Progress");
								} else if (decodedTaskName.split("::")[1].equals("Invoicing Task")) {
									dsValues.put("InvoiceTaskStatus", "In Progress");
								}
							} else {
								dsValues.put("TaskStatus", "In Progress");
							}

							CaseHistory caseHistory = resourceLocator.getSBMService().getCaseDetails(decodedTaskName);
							if (null != caseHistory.getOldInfo() && caseHistory.getOldInfo().trim().length() > 0
									&& !caseHistory.getOldInfo().equalsIgnoreCase("In Progress")) {
								this.logger.debug("calling case history from time tracker controller :: "
										+ httpSession.getAttribute("loginLevel"));
								if (null != httpSession.getAttribute("loginLevel")) {
									caseHistory.setPerformer((String) httpSession.getAttribute("performedBy"));
								} else {
									caseHistory.setPerformer(userBean.getUserName());
								}

								this.logger.debug("case history performer :: " + caseHistory.getPerformer());
								caseHistory.setNewInfo("In Progress");
								resourceLocator.getCaseHistoryService().setCaseHistoryForTaskStatusChange(caseHistory);
							}

							resourceLocator.getSBMService().updateDataSlots(SBMUtils.getSession(httpservletrequest),
									Long.valueOf(pid), dsValues);
						} else {
							resourceLocator.getCacheService().removeTTTokenCache(userBean.getUserName());
						}
					} else {
						this.logger.debug("case status is cancelled ..not performing time tracker activity.");
					}
				} else {
					this.logger.debug("Work item does not exists...so TT activity not performed.");
				}
			} else if (null != httpservletrequest.getParameter(this.BIZSITE_TASKNAME)
					&& httpservletrequest.getParameter(this.BIZSITE_TASKNAME).trim().length() > 0) {
				taskName = HexDecoder.decode(httpservletrequest.getParameter(this.BIZSITE_TASKNAME));
				if (taskName.split("::")[1].equals("Complete Case Creation")) {
					this.logger.debug("coming from create case and add subject..so just stopping tracker.");
					crn = (String) httpSession.getAttribute("TrackerOn");
					if (null != crn && !crn.equals("")) {
						resourceLocator.getTimeTrackerService().stopTimeTrackerForUser(userBean.getUserName());
						httpSession.removeAttribute("TrackerOn");
						resourceLocator.getCacheService().removeTTTokenCache(userBean.getUserName());
					}
				} else {
					this.logger.debug("TT called for same task..so not stopping");
				}
			} else {
				this.logger.debug("time tracker not started as either workitem or crn is null/blank.");
			}
		} catch (Exception var20) {
			AtlasUtils.getExceptionView(this.logger, var20);
		}

		if (invoiceTaskFlag) {
			this.logger.debug("task is invoicing task which is already being worked upon by some other user.");
			httpservletresponse.sendRedirect(
					httpservletresponse.encodeRedirectURL("/sbm/bpmportal/myhome/bpmerrorwomenucustom.jsp"));
		} else {
			super.doGet(httpservletrequest, httpservletresponse);
		}

	}

	public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws ServletException, IOException {
		this.logger.debug("TimeTrackerActivationController: doPost");
	}
}