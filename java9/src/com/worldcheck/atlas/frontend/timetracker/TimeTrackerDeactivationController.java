package com.worldcheck.atlas.frontend.timetracker;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.savvion.sbm.bpmportal.bizsite.util.HexDecoder;
import com.savvion.sbm.bpmportal.mvc2.Controller;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TimeTrackerDeactivationController extends Controller {
	private static final long serialVersionUID = 1L;
	private String WORK_ITEM = "workItem";
	private String TASK_NAME = "taskName";
	private String PID = "pid";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.timetracker.TimeTrackerDeactivationController");

	public void init(ServletConfig servletconfig) throws ServletException {
		this.logger.debug("TimeTrackerDeactivationController: initialized");
		super.init(servletconfig);
	}

	public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws ServletException, IOException {
		this.logger.debug("TimeTrackerDeactivationController: doGet");

		try {
			HttpSession httpSession = httpservletrequest.getSession();
			String trackerParam = (String) httpSession.getAttribute("TrackerOn");
			if (null != trackerParam && !trackerParam.equals("")) {
				ResourceLocator resourceLocator = ResourceLocator.self();
				UserBean userBean = (UserBean) httpSession.getAttribute("userBean");
				String decodedTaskName;
				if (null != httpservletrequest.getParameter(this.WORK_ITEM)
						&& null != httpservletrequest.getParameter("crn")) {
					this.logger.debug("stopping time tracker from bean.");
					decodedTaskName = httpservletrequest.getParameter(this.WORK_ITEM);
					String crn = httpservletrequest.getParameter("crn");
					this.logger.debug("workItemName :: " + decodedTaskName + " :: crn :: " + crn);
					String decodedWorkName = HexDecoder.decode(decodedTaskName);
					this.logger.debug("decodedWorkName :: " + decodedWorkName + " :: crn :: " + crn);
					String[] pinfo = decodedWorkName.split("#");
					String pid = "0";
					String pt = "";
					if (pinfo.length == 2) {
						pt = pinfo[0];
						pid = pinfo[1].split("::")[0];
					}

					this.logger.debug("decodedWorkName :: " + decodedWorkName + " :: pid :: " + pid);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO.setCrn(crn);
					timeTrackerVO.setUserName(userBean.getUserName());
					timeTrackerVO.setTaskPid(pid);
					timeTrackerVO.setTaskName(null == decodedWorkName ? "" : decodedWorkName.split("::")[1]);
					timeTrackerVO.setUpdatedBy(userBean.getUserName());
					resourceLocator.getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					httpSession.removeAttribute("trackerBean");
					httpSession.removeAttribute("TrackerOn");
				} else if (null != httpservletrequest.getParameter(this.TASK_NAME)
						&& null != httpservletrequest.getParameter("crn")
						&& null != httpservletrequest.getParameter(this.PID)) {
					this.logger.debug("stopping time tracker from tracker bean.");
					this.logger.debug("taskName :: " + httpservletrequest.getParameter(this.TASK_NAME) + " :: pid :: "
							+ httpservletrequest.getParameter(this.PID) + " :: crn :: "
							+ httpservletrequest.getParameter("crn"));
					decodedTaskName = HexDecoder.decode(httpservletrequest.getParameter(this.TASK_NAME));
					this.logger.debug("decodedTaskName :: " + decodedTaskName);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO.setCrn(httpservletrequest.getParameter("crn"));
					timeTrackerVO.setUserName(userBean.getUserName());
					timeTrackerVO.setTaskPid(httpservletrequest.getParameter(this.PID));
					timeTrackerVO.setTaskName(decodedTaskName);
					timeTrackerVO.setUpdatedBy(userBean.getUserName());
					resourceLocator.getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					httpSession.removeAttribute("trackerBean");
					httpSession.removeAttribute("TrackerOn");
				}
			} else {
				this.logger.debug("time tracker stopping activity not performed.");
			}

			super.doGet(httpservletrequest, httpservletresponse);
		} catch (Exception var14) {
			AtlasUtils.getExceptionView(this.logger, var14);
		}

	}

	public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws ServletException, IOException {
		this.doGet(httpservletrequest, httpservletresponse);
	}
}