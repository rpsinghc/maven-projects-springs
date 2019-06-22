package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IRejectionREManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.web.servlet.ModelAndView;

public class JSONRejectionREMultiActionController extends JSONMultiActionController {
	private static final String UNDEFINED = "undefined";
	private static final String NULL = "null";
	private static final String PRIMARY_HASH = "Primary#";
	private static final String REJETCION_TEAM = "rejetcionTeam";
	private static final String SUCCESS = "success";
	private static final String RESULT = "result";
	private static final String SUBJECT_INFO = "subjectInfo";
	private static final String HASH = "#";
	private static final String REJECTED_FROM = "rejectedFrom";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONRejectionREMultiActionController");
	private IRejectionREManager rejectionREManager;

	public void setRejectionREManager(IRejectionREManager rejectionREManager) {
		this.rejectionREManager = rejectionREManager;
	}

	public ModelAndView getAllSubjectInfoByTeam(HttpServletRequest request, HttpServletResponse response,
			SubTeamReMapVO subTeamReMapVO) {
		ModelAndView mv = null;

		try {
			this.logger.debug("inside getAllSubjectInfoByTeam");
			mv = new ModelAndView("jsonView");
			this.logger.debug("CRN :" + subTeamReMapVO.getCrn());
			this.logger.debug("ProcessCycle:" + subTeamReMapVO.getProcessCycle());
			String rejectionteam = request.getParameter("rejetcionTeam");
			this.logger.debug("rejectionTeam::" + rejectionteam);
			String teamNameString = this.getTeamName(subTeamReMapVO.getCrn(), rejectionteam);
			String teamId = teamNameString.split("#")[0];
			String teamName = teamNameString.split("#")[1];
			this.logger.debug("TeamName:" + teamName);
			this.logger.debug("TeamId" + teamId);
			String workItemName = request.getParameter("workItem");
			this.logger.debug("workItemName:" + workItemName);
			String processCycle = this.getProcessCycle(teamName, workItemName, request).split("#")[0];
			this.logger.debug("#####Rejection###: ProcessCyle:" + processCycle);
			subTeamReMapVO.setProcessCycle(processCycle);
			this.logger.debug("processCycle:" + processCycle);
			List<SubTeamReMapVO> subjectInfo = this.rejectionREManager.getAllSubjectInfoByTeam(subTeamReMapVO.getCrn(),
					teamId, teamName, subTeamReMapVO.getProcessCycle());
			this.logger.debug("vendorDetailVO:" + subTeamReMapVO);
			mv.addObject("result", "success");
			mv.addObject("processCycle", processCycle);
			mv.addObject("subjectInfo", subjectInfo);
			return mv;
		} catch (CMSException var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		} catch (Exception var19) {
			return AtlasUtils.getJsonExceptionView(this.logger, var19, response);
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

	public ModelAndView rejectedREInfo(HttpServletRequest request, HttpServletResponse response,
			SubTeamReMapVO subTeamReMapVO) {
		ModelAndView mv = null;

		try {
			this.logger.debug("Inside the Rejection::");
			this.logger.debug("Task Status::" + subTeamReMapVO.getRejectedTaskStatus());
			String rejectedTaskStatus = subTeamReMapVO.getRejectedTaskStatus();
			mv = new ModelAndView("jsonView");
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
			new ArrayList();

			List teamDetailsVOList;
			try {
				teamDetailsVOList = this.getSubTeamReMapVO(teamDetails);
			} catch (NullPointerException var23) {
				return AtlasUtils.getJsonExceptionView(this.logger, var23, response);
			} catch (ArrayIndexOutOfBoundsException var24) {
				return AtlasUtils.getJsonExceptionView(this.logger, var24, response);
			}

			long pid = Long.parseLong(workItemName.split("#")[1].split("::")[0]);
			this.logger.debug("WorkItem  PID:" + pid);
			long piid = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID",
					SBMUtils.getSession(request));
			this.logger.debug("Process Parent PID:" + piid);
			if (0L == piid) {
				piid = pid;
			}

			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					SBMUtils.getSession(request));
			this.logger.debug("ProcessCyle:" + processCycle);
			String updateBy = ((UserBean) request.getSession().getAttribute("userBean")).getUserName();
			this.logger.debug("teamDetailsVOList size:" + teamDetailsVOList.size());
			this.logger.debug("Rejected from::" + rejectedFrom);
			this.logger.debug("RejetcionTeam::" + rejetcionTeam);
			this.rejectionREManager.saveRejectedREInfo(request, session, workItemName, processCycle, rejetcionTeam,
					teamDetailsVOList, RejectionReason, updateBy, rejectedTaskStatus, rejectedFrom);
			this.logger.debug("vendorDetailVO:" + subTeamReMapVO);
			mv.addObject("result", "success");
			return mv;
		} catch (CMSException var25) {
			return AtlasUtils.getJsonExceptionView(this.logger, var25, response);
		} catch (Exception var26) {
			return AtlasUtils.getJsonExceptionView(this.logger, var26, response);
		}
	}

	public List<SubTeamReMapVO> getSubTeamReMapVO(List<String> teamDetailList) {
		SubTeamReMapVO subTeamReMapVO = null;
		List<SubTeamReMapVO> teamDetailsVOList = new ArrayList();
		Iterator i$ = teamDetailList.iterator();

		while (i$.hasNext()) {
			String string = (String) i$.next();
			this.logger.debug("Rejection String ::" + string);
			String[] data = string.split("#");
			subTeamReMapVO = new SubTeamReMapVO();
			subTeamReMapVO.setCrn(data[0]);
			subTeamReMapVO.setCountry(data[1]);
			subTeamReMapVO.setOfficeId(data[2]);
			subTeamReMapVO.setOffice(data[3]);
			subTeamReMapVO.setTeamId(data[4] != null && !"".equals(data[4]) && !"undefined".equals(data[4])
					? Integer.parseInt(data[4])
					: 0);
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
		}

		i$ = teamDetailsVOList.iterator();

		while (i$.hasNext()) {
			SubTeamReMapVO subTeamReMapVO2 = (SubTeamReMapVO) i$.next();
			this.logger.debug(subTeamReMapVO2.getPerformer() + "#" + subTeamReMapVO2.getPerformerFullName());
			this.logger.debug(subTeamReMapVO2.getMainAnalyst() + "#" + subTeamReMapVO2.getAnalystFullName());
			this.logger.debug(subTeamReMapVO2.getReportsTo() + "#" + subTeamReMapVO2.getReportsToFullName());
		}

		return teamDetailsVOList;
	}
}