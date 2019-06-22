package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IRejectionREManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class RejectionMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.RejectionMultiActionController");
	private IRejectionREManager rejectionREManager;
	private static final String UNDEFINED = "undefined";
	private static final String NULL = "null";
	private static final String PRIMARY_HASH = "Primary#";
	private static final String REJETCION_TEAM = "rejetcionTeam";
	private String successForwardView = "";
	private static final String HASH = "#";

	public void setSuccessForwardView(String successForwardView) {
		this.successForwardView = successForwardView;
	}

	public void setRejectionREManager(IRejectionREManager rejectionREManager) {
		this.rejectionREManager = rejectionREManager;
	}

	public ModelAndView saveRejectedRE(HttpServletRequest request, HttpServletResponse response,
			SubTeamReMapVO subTeamReMapVO) throws Exception {
		try {
			String crn = request.getParameter("crnVal");
			this.logger.debug("Inside the Rejection::");
			this.logger.debug("########################## REJECTION MULTIACTION CONTROLLER ##########################");
			this.logger.debug("crn:" + crn);
			this.logger.debug("Rejection Comments:" + request.getParameter("rejectionReason"));
			this.logger.debug("workItem:" + request.getParameter("workItem"));
			this.logger.debug("rejetcionTeam:" + request.getParameter("rejetcionTeam"));
			this.logger.debug("RejectionInfo:" + request.getParameter("rejectREInfo"));
			this.logger.debug("rejectedTaskStatus:" + request.getParameter("rejectedTaskStatus"));
			this.logger.debug("Task Status::" + subTeamReMapVO.getRejectedTaskStatus());
			subTeamReMapVO.setCrn(crn);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (null != request.getParameter("workItem")
					&& !ResourceLocator.self().getSBMService().wiExistsForUser(request.getParameter("workItem"),
							SBMUtils.getSession(request), userBean.getUserName())) {
				this.logger.debug("redirecting to task status error page for workitem no longer exists.");
				return new ModelAndView("redirect:/bpmportal/atlas/taskStatusErrorPage.jsp");
			} else {
				String rejectedTaskStatus = subTeamReMapVO.getRejectedTaskStatus();
				String RejectionReason = subTeamReMapVO.getRejectionReason();
				Session session = SBMUtils.getSession(request);
				String workItemName = request.getParameter("workItem");
				String rejetcionTeam = request.getParameter("rejetcionTeam");
				this.logger.debug("Session:" + session + " workItemName:" + workItemName + " JsonString:"
						+ subTeamReMapVO.getRejectREInfo() + " teamTypeList:" + rejetcionTeam);
				String teamNameString = this.getTeamName(subTeamReMapVO.getCrn(), rejetcionTeam);
				String teamName = teamNameString.split("#")[1];
				String rejectedFrom = this.getProcessCycle(teamName, workItemName, request);
				this.logger.debug("Rejected String::" + rejectedFrom);
				JSONArray jsonArray = JSONArray.fromObject(subTeamReMapVO.getRejectREInfo());
				List<String> teamDetails = JSONArray.toList(jsonArray, String.class);
				List<SubTeamReMapVO> teamDetailsVOList = new ArrayList();
				Iterator i$ = teamDetails.iterator();

				while (i$.hasNext()) {
					String string = (String) i$.next();
					this.logger.debug("SubTeamReMapVO String" + string);
					String[] data = string.split("#");
					this.logger.debug("CRN:" + data[0] + " & Country:" + data[1] + " & Office Id:" + data[2]
							+ " &  Office :" + data[3] + " & Team Id:" + data[4] + " & TeamName :" + data[5]
							+ " & Subject Id:" + data[6] + " & Subject :" + data[7] + " & RE Id:" + data[8]
							+ " & RENAME :" + data[9] + " & Performer:" + data[10] + " & PerformerName :" + data[11]
							+ " & Reports To:" + data[12] + " &  ReportsToFullName:" + data[13] + " & Manager :"
							+ data[14] + " & ManagerFullName :" + data[15] + " & MainAnalyst :" + data[16]
							+ " & MainAnalystFullname :" + data[17]);
					subTeamReMapVO = new SubTeamReMapVO();
					subTeamReMapVO.setCrn(data[0]);
					subTeamReMapVO.setCountry(data[1]);
					subTeamReMapVO.setOfficeId(data[2]);
					subTeamReMapVO.setOffice(data[3]);
					this.logger.debug("array >>  TeamId::" + data[4]);
					String teamId = data[4];
					if (teamId != null && !teamId.equals("") && !teamId.equals("undefined")) {
						subTeamReMapVO.setTeamId(Integer.parseInt(data[4]));
					} else {
						subTeamReMapVO.setTeamId(0);
					}

					this.logger.debug("VO >>  Team Id::" + subTeamReMapVO.getTeamId());
					subTeamReMapVO.setTeamName(data[5]);
					subTeamReMapVO.setSubjectId(data[6]);
					subTeamReMapVO.setSubject(data[7]);
					subTeamReMapVO.setReId(data[8]);
					subTeamReMapVO.setReName(data[9]);
					subTeamReMapVO.setPerformer(data[10]);
					subTeamReMapVO.setPerformerFullName(data[11]);
					subTeamReMapVO.setReportsTo(data[12]);
					subTeamReMapVO.setReportsToFullName(data[13]);
					subTeamReMapVO.setManager(data[14]);
					subTeamReMapVO.setManagerFullName(data[15]);
					subTeamReMapVO.setMainAnalyst(data[16]);
					subTeamReMapVO.setAnalystFullName(data[17]);
					teamDetailsVOList.add(subTeamReMapVO);
					teamDetailsVOList.add(subTeamReMapVO);
				}

				long pid = Long.parseLong(workItemName.split("#")[1].split("::")[0]);
				this.logger.debug("WorkItem  PID:" + pid);
				long piid = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID",
						SBMUtils.getSession(request));
				this.logger.debug("Process Parent PID:" + piid);
				if (0L == piid) {
					piid = pid;
				}

				String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid,
						"ProcessCycle", SBMUtils.getSession(request));
				this.logger.debug("ProcessCyle:" + processCycle);
				String updateBy = ((UserBean) request.getSession().getAttribute("userBean")).getUserName();
				this.logger.debug("teamDetailsVOList size:" + teamDetailsVOList.size());
				this.rejectionREManager.saveRejectedREInfo(request, session, workItemName, processCycle, rejetcionTeam,
						teamDetailsVOList, RejectionReason, updateBy, rejectedTaskStatus, rejectedFrom);
				this.logger.debug("vendorDetailVO:" + subTeamReMapVO);
				HashMap<String, Object> dsValues = new HashMap();
				this.logger.debug("#### WORK_ITEM: " + request.getParameter("workItem"));
				dsValues.put("taskPerformer", request.getParameter("taskPerformer"));
				ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid, dsValues);
				request.getRequestDispatcher(this.successForwardView).forward(request, response);
				return null;
			}
		} catch (CMSException var26) {
			return AtlasUtils.getExceptionView(this.logger, var26);
		} catch (Exception var27) {
			return AtlasUtils.getExceptionView(this.logger, var27);
		}
	}

	public String getTeamName(String crn, String rejectionteam) throws CMSException {
		String teamId = null;
		String teamName = null;

		try {
			if (rejectionteam != null && rejectionteam.length() != 0 && !rejectionteam.equalsIgnoreCase("null")) {
				this.logger.debug("rejectionTeam:" + rejectionteam);
				teamName = rejectionteam.split("#")[0];
				teamId = rejectionteam.split("#")[1];
			} else {
				rejectionteam = "Primary#" + ResourceLocator.self().getTeamAssignmentService().getPrimaryTeamId(crn);
				this.logger.debug("rejectionTeam:" + rejectionteam);
				teamName = rejectionteam.split("#")[0];
				teamId = rejectionteam.split("#")[1];
			}
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		}

		return teamId + "#" + teamName;
	}

	public String getProcessCycle(String teamName, String workItemName, HttpServletRequest request)
			throws CMSException {
		long pid = 0L;
		long piid = 0L;

		try {
			pid = Long.parseLong(workItemName.split("#")[1].split("::")[0]);
			this.logger.debug("WorkItem  PID:" + pid);
			piid = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID",
					SBMUtils.getSession(request));
			this.logger.debug("Process Parent PID:" + piid);
			String taskName = workItemName.split("#")[1].split("::")[1];
			String processCycle;
			String rejectedFrom;
			if (workItemName.contains("Review Task")) {
				long getParentPID = Long.valueOf(
						ResourceLocator.self().getSBMService().getParentPID(pid, SBMUtils.getSession(request)));
				this.logger.debug("getParentPID:" + getParentPID);
				if (piid == getParentPID) {
					this.logger.debug("Piid:" + piid + "getParentPID:" + getParentPID);
					processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(getParentPID,
							"ProcessCycle", SBMUtils.getSession(request));
					rejectedFrom = processCycle + "#" + teamName + "#" + taskName;
				} else {
					processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(getParentPID,
							"TeamCycleName", SBMUtils.getSession(request));
					rejectedFrom = processCycle + "#" + teamName + "#" + taskName;
				}
			} else if (workItemName.contains("Consolidation Task")) {
				if (piid == 0L) {
					processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "ProcessCycle",
							SBMUtils.getSession(request));
					rejectedFrom = processCycle + "#" + teamName + "#" + taskName;
				} else {
					processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid,
							"TeamCycleName", SBMUtils.getSession(request));
					rejectedFrom = processCycle + "#" + teamName + "#" + taskName;
				}
			} else {
				this.logger.debug("inside Client Subbmision  the 2nd if");
				processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "ProcessCycle",
						SBMUtils.getSession(request));
				rejectedFrom = processCycle + "#" + teamName + "#" + taskName;
			}

			this.logger.debug("RejectedFrom::" + rejectedFrom);
			return rejectedFrom;
		} catch (CMSException var13) {
			throw new CMSException(this.logger, var13);
		}
	}
}