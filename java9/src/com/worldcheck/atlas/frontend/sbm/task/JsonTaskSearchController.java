package com.worldcheck.atlas.frontend.sbm.task;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.services.casedetail.CaseDetailService;
import com.worldcheck.atlas.services.sbm.SBMService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.task.CaseStatusVO;
import com.worldcheck.atlas.vo.task.MyOnHoldCaseVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public class JsonTaskSearchController extends JSONMultiActionController {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.sbm.task.TaskSearchController");
	private SBMService sbmService = ResourceLocator.self().getSBMService();

	public ModelAndView getUserAllTask(HttpServletRequest request, HttpServletResponse response,
			MyTaskPageVO myTaskPageVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		long startTime1 = 0L;
		long endTime1 = 0L;

		try {
			String startRequest = request.getParameter("start");
			String limitRequest = request.getParameter("limit");
			this.logger.debug("Start value is " + startRequest + " ::: and limit is " + limitRequest);
			if (startRequest == null || "null".equalsIgnoreCase(startRequest)
					|| "".equalsIgnoreCase(startRequest.trim())) {
				startRequest = "0";
			}

			if (limitRequest == null || "null".equalsIgnoreCase(limitRequest)
					|| "".equalsIgnoreCase(limitRequest.trim())) {
				limitRequest = "10";
			}

			int start = Integer.parseInt(startRequest);
			int limit = Integer.parseInt(limitRequest);
			long startTime = System.currentTimeMillis();
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List<String> roleList = userDetailsBean.getRoleList();
			boolean hasOnlyFinanceRole = false;
			boolean hasBIRole = false;
			if (roleList.size() == 1 && roleList.contains("R3")) {
				hasOnlyFinanceRole = true;
			} else if (roleList.contains("R6")) {
				hasBIRole = true;
			}

			this.logger.debug("hasOnlyFinanceRole value is " + hasOnlyFinanceRole);
			String condition = "";
			if (!myTaskPageVO.getStatus().isEmpty()) {
				condition = "TaskStatus ='" + myTaskPageVO.getStatus() + "'";
			}

			HashMap<String, Object> resultMap = this.sbmService.getMyTasks(SBMUtils.getSession(request),
					this.getConditionString(myTaskPageVO), this.getTaskStatusQuery(condition), limit, start,
					hasOnlyFinanceRole, "''", false, hasBIRole);
			long endTime = System.currentTimeMillis();
			this.logger.debug("Time Taken by controller " + (endTime - startTime) + "ms");
			startTime1 = System.currentTimeMillis();
			endTime = 0L;
			if (resultMap != null && !resultMap.isEmpty()) {
				modelAndView.addObject("total", (Long) resultMap.get("total"));
				modelAndView.addObject("processInstanceList", resultMap.get("processInstanceList"));
			}

			endTime1 = System.currentTimeMillis();
			this.logger.debug("Time Taken by controller " + (endTime1 - startTime1) + "ms");
			return modelAndView;
		} catch (Exception var23) {
			return AtlasUtils.getJsonExceptionView(this.logger, var23, response);
		}
	}

	public ModelAndView getUserTeamTask(HttpServletRequest request, HttpServletResponse response,
			MyTaskPageVO taskPageVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String startRequest = request.getParameter("start");
			String limitRequest = request.getParameter("limit");
			this.logger.debug(
					"In the getUserTeamTask Start value is " + startRequest + " ::: and limit is " + limitRequest);
			if (startRequest == null || "null".equalsIgnoreCase(startRequest)
					|| "".equalsIgnoreCase(startRequest.trim())) {
				startRequest = "0";
			}

			if (limitRequest == null || "null".equalsIgnoreCase(limitRequest)
					|| "".equalsIgnoreCase(limitRequest.trim())) {
				limitRequest = "10";
			}

			int start = Integer.parseInt(startRequest);
			int limit = Integer.parseInt(limitRequest);
			String users = "''";
			boolean isSessionCreated = false;
			Session userSession = null;
			if (taskPageVO != null && taskPageVO.getSubordinateUserId() != null
					&& taskPageVO.getSubordinateUserId().trim().length() > 0) {
				if (!"All".equalsIgnoreCase(taskPageVO.getSubordinateUserId())) {
					userSession = this.sbmService.getSession(taskPageVO.getSubordinateUserId());
					isSessionCreated = true;
					users = "subordinateAllTask";
				} else {
					userSession = SBMUtils.getSession(request);
					List<UserMasterVO> subordinateIds = ResourceLocator.self().getUserService()
							.getSubOrdinateList(userSession.getUser());
					boolean isFinanceIncluded = false;
					Iterator iterator = subordinateIds.iterator();

					while (iterator.hasNext()) {
						UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
						List<String> userRoleList = ResourceLocator.self().getUserService()
								.getUserRoleInfo(userMasterVO.getUsername());
						if (!isFinanceIncluded && userRoleList.contains("R3")) {
							isFinanceIncluded = true;
							users = "'''FinanceQueue''";
						}

						if (!userMasterVO.getUsername().equalsIgnoreCase(userSession.getUser())) {
							if ("''".equalsIgnoreCase(users)) {
								users = "'''" + userMasterVO.getUsername() + "''";
							} else {
								users = users + ",''" + userMasterVO.getUsername() + "''";
							}
						}
					}

					if (!"''".equalsIgnoreCase(users)) {
						users = users + "'";
					}
				}
			}

			String condition = "";
			if (taskPageVO != null && !taskPageVO.getTaskName().isEmpty()) {
				condition = condition + " AND WI.WORKSTEP_NAME =''" + taskPageVO.getTaskName() + "'' ";
			}

			HashMap<String, Object> resultMap = this.sbmService.getMyTasks(userSession, condition,
					this.getConditionForTeamTask(taskPageVO), limit, start, false, users, false, false);
			if (resultMap != null && !resultMap.isEmpty()) {
				modelAndView.addObject("total", (Long) resultMap.get("total"));
				modelAndView.addObject("processInstanceList", resultMap.get("processInstanceList"));
			}

			if (isSessionCreated) {
				this.sbmService.closeSession(userSession);
			}
		} catch (Exception var17) {
			AtlasUtils.getJsonExceptionView(this.logger, var17, response);
		}

		return modelAndView;
	}

	private List<MyTaskPageVO> getUserFullName(List<MyTaskPageVO> resultList, String users) throws CMSException {
		List userFullNameList = ResourceLocator.self().getUserService()
				.getUserFullName(StringUtils.commaSeparatedStringToList(users));
		this.logger.debug("userFullNameList size is" + userFullNameList.size());
		Iterator rsltItr = resultList.iterator();

		while (true) {
			while (true) {
				while (rsltItr.hasNext()) {
					MyTaskPageVO myTaskPageVO = (MyTaskPageVO) rsltItr.next();
					Iterator usrItr = userFullNameList.iterator();

					while (usrItr.hasNext()) {
						UserMasterVO masterVO = (UserMasterVO) usrItr.next();
						if (masterVO.getLoginId().equals(myTaskPageVO.getPerformer())) {
							if (masterVO.getUserFullName() != null && !masterVO.getUserFullName().isEmpty()) {
								myTaskPageVO.setUserFullName(masterVO.getUserFullName());
								break;
							}

							myTaskPageVO.setUserFullName(myTaskPageVO.getPerformer());
							break;
						}
					}
				}

				return resultList;
			}
		}
	}

	public ModelAndView getTaskStatus(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String condition = "";
			String crn = request.getParameter("crn");
			this.logger.debug("CRN " + crn);
			Map allTaskStatus = this.sbmService.getCaseStatus(this.sbmService.getSession(), crn);
			modelAndView.addObject("Inrim1Data",
					this.getUserFullNameForCaseStatus((List) allTaskStatus.get("Intrim1")));
			modelAndView.addObject("Inrim2Data",
					this.getUserFullNameForCaseStatus((List) allTaskStatus.get("Intrim2")));
			modelAndView.addObject("FinalData", this.getUserFullNameForCaseStatus((List) allTaskStatus.get("Final")));
		} catch (Exception var7) {
			AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return modelAndView;
	}

	private List<MyTaskPageVO> getUserFullNameForCaseStatus(List taskList) throws CMSException {
		this.logger.debug("task list size" + taskList.size());
		Iterator<CaseStatusVO> rsltItr = taskList.iterator();
		String users = "z";

		Iterator taskItr;
		while (rsltItr.hasNext()) {
			CaseStatusVO caseStatusVO = (CaseStatusVO) rsltItr.next();

			String performer;
			for (taskItr = caseStatusVO.getTaskList().iterator(); taskItr.hasNext(); users = users + "," + performer) {
				MyTaskPageVO taskPageVO = (MyTaskPageVO) taskItr.next();
				performer = taskPageVO.getPerformer();
			}
		}

		this.logger.debug("Users are :_" + users);
		List userFullNameList = ResourceLocator.self().getUserService()
				.getUserFullName(StringUtils.commaSeparatedStringToList(users));
		this.logger.debug("userFullNameList " + userFullNameList.size());
		taskItr = taskList.iterator();

		label33 : while (taskItr.hasNext()) {
			CaseStatusVO caseStatusVO = (CaseStatusVO) taskItr.next();
			Iterator taskItr = caseStatusVO.getTaskList().iterator();

			while (true) {
				while (true) {
					if (!taskItr.hasNext()) {
						continue label33;
					}

					MyTaskPageVO taskPageVO = (MyTaskPageVO) taskItr.next();
					String performer = taskPageVO.getPerformer();
					Iterator usrItr = userFullNameList.iterator();

					while (usrItr.hasNext()) {
						UserMasterVO masterVO = (UserMasterVO) usrItr.next();
						if (masterVO.getLoginId().equals(performer)) {
							taskPageVO.setUserFullName(masterVO.getUserFullName());
							break;
						}
					}
				}
			}
		}

		return taskList;
	}

	public ModelAndView getOnHoldCases(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			CaseDetailService caseDetailService = ResourceLocator.self().getCaseDetailService();
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			limit += start;
			HttpSession session = request.getSession();
			UserDetailsBean detailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("start", start);
			paramMap.put("limit", limit);
			paramMap.put("userId", detailsBean.getLoginUserId());
			List<MyOnHoldCaseVO> onHoldCaseList = caseDetailService.getOnHoldCase(paramMap);
			modelAndView.addObject("processInstanceList", onHoldCaseList);
			modelAndView.addObject("total", caseDetailService.getCountOfOnHoldCases(paramMap));
			return modelAndView;
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView getUnCompletedCases(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			limit += start;
			List<MyTaskPageVO> onHoldCaseList = new ArrayList();
			HttpSession session = request.getSession();
			UserDetailsBean detailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			List<TeamDetails> teamDetailList = ResourceLocator.self().getTeamAssignmentService()
					.getTeamDetailsForAnalyst(detailsBean.getLoginUserId());
			Map<String, String> userMap = new HashMap();
			Iterator teamItr = teamDetailList.iterator();

			while (teamItr.hasNext()) {
				TeamDetails teamDetails = (TeamDetails) teamItr.next();
				if (teamDetails.getManagerName() != null && !teamDetails.getManagerName().isEmpty()) {
					userMap.put(teamDetails.getManagerName(), "");
				}

				if (teamDetails.getReviewer1() != null && !teamDetails.getReviewer1().isEmpty()) {
					userMap.put(teamDetails.getReviewer1(), "");
				}

				if (teamDetails.getReviewer2() != null && !teamDetails.getReviewer2().isEmpty()) {
					userMap.put(teamDetails.getReviewer2(), "");
				}

				if (teamDetails.getReviewer3() != null && !teamDetails.getReviewer3().isEmpty()) {
					userMap.put(teamDetails.getReviewer3(), "");
				}
			}

			this.logger.debug("Final Team members are " + userMap);
			Iterator userItr = userMap.keySet().iterator();

			while (userItr.hasNext()) {
				Session sbmSession = null;
				String userId = (String) userItr.next();

				try {
					sbmSession = this.sbmService.getSession(userId);
				} catch (Exception var16) {
					this.logger.debug(userId + "User is not valid" + var16.getLocalizedMessage());
				}

				this.logger.debug("sbmSession is " + sbmSession);
				if (sbmSession != null) {
					onHoldCaseList.addAll(ResourceLocator.self().getSBMService().getUnCompletedCases(sbmSession,
							detailsBean.getLoginUserId()));
				}
			}

			int count = onHoldCaseList.size();
			this.logger.debug("Total case size " + count);
			if (limit - start < count - start) {
				modelAndView.addObject("processInstanceList", onHoldCaseList.subList(start, limit));
			} else {
				modelAndView.addObject("processInstanceList", onHoldCaseList.subList(start, count));
			}

			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (Exception var17) {
			return AtlasUtils.getJsonExceptionView(this.logger, var17, response);
		}
	}

	private String getConditionString(MyTaskPageVO myTaskPageVO) {
		String condition = "";
		if (myTaskPageVO != null) {
			String primarySubjectCountry;
			if (!myTaskPageVO.getPrimarySubject().isEmpty()) {
				primarySubjectCountry = myTaskPageVO.getPrimarySubject().toLowerCase();
				if (primarySubjectCountry.contains("'")) {
					primarySubjectCountry = primarySubjectCountry.replaceAll("'", "''''");
				}

				condition = condition + " AND LOWER(BLIDS." + "PrimarySubject" + ") = ''" + primarySubjectCountry
						+ "'' ";
			}

			if (!myTaskPageVO.getCountry().isEmpty()) {
				primarySubjectCountry = myTaskPageVO.getCountry();
				if (primarySubjectCountry.contains("'")) {
					primarySubjectCountry = primarySubjectCountry.replaceAll("'", "''''");
				}

				condition = condition + " AND BLIDS." + "PrimarySubjectCountry" + " = ''" + primarySubjectCountry
						+ "'' ";
			}

			if (!myTaskPageVO.getTaskName().isEmpty()) {
				condition = condition + " AND WI.WORKSTEP_NAME = ''" + myTaskPageVO.getTaskName() + "'' ";
			}

			if (!myTaskPageVO.getCaseStatus().isEmpty()) {
				condition = condition + " AND BLIDS.CASESTATUS = ''" + myTaskPageVO.getCaseStatus() + "'' ";
			}

			if (!myTaskPageVO.getCrn().isEmpty()) {
				condition = condition + " AND BLIDS.CRN LIKE ''%" + myTaskPageVO.getCrn() + "%'' ";
			}
		}

		this.logger.debug("condition " + condition);
		return condition;
	}

	private String getTaskStatusQuery(String condition) {
		String condition_CC = condition;
		int startIndex = condition.indexOf("TaskStatus");
		String taskStatus = "";
		if (startIndex != -1) {
			taskStatus = " " + condition.substring(startIndex + "TaskStatus".length()) + " ";
			condition_CC = condition.replaceAll("TaskStatus", "((CompleteTaskStatus");
			condition_CC = condition_CC + " AND WORKSTEP_NAME ='" + "Complete Case Creation" + "') OR ("
					+ "OfficeTaskSTatus" + taskStatus + " AND WORKSTEP_NAME ='" + "Office Assignment Task" + "') OR ("
					+ "InvoiceTaskStatus" + taskStatus + "AND WORKSTEP_NAME ='" + "Invoicing Task" + "') OR ("
					+ "ConsolidationTaskStatus" + taskStatus + " AND WORKSTEP_NAME ='" + "Consolidation Task"
					+ "') OR (" + "ClientSubmissionTaskStatus" + taskStatus + " AND WORKSTEP_NAME ='"
					+ "Client Submission Task" + "') OR (AwaitingTaskSTatus " + taskStatus
					+ " AND WORKSTEP_NAME = 'WaitForConsolidation')" + " OR ( " + "TaskStatus" + taskStatus + " ))";
		}

		this.logger.debug("Updated condition " + condition_CC);
		return condition_CC;
	}

	private String getConditionForTeamTask(MyTaskPageVO myTaskPageVO) {
		String condition = "";
		if (myTaskPageVO != null && !myTaskPageVO.getStatus().isEmpty()) {
			if (myTaskPageVO.getStatus() != null && !myTaskPageVO.getStatus().isEmpty()
					&& !myTaskPageVO.getStatus().equals("0")) {
				if (myTaskPageVO.getStatus().equals("1")) {
					condition = "TaskStatus='New'";
				} else {
					condition = "TaskStatus in ('In Progress','Revise','Rework')";
				}
			} else {
				condition = "TaskStatus in ('New','In Progress','Revise','Rework')";
			}
		}

		condition = this.getTaskStatusQuery(condition);
		this.logger.debug("JsonTaskSearchController.getConditionForTeamTask():::::: condition is " + condition);
		return condition;
	}

	public ModelAndView getIncomingTasks(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			HttpSession session = request.getSession();
			UserDetailsBean detailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			List<MyTaskPageVO> myINcomingTaskList = ResourceLocator.self().getSBMService()
					.myIncomingTasks(detailsBean.getLoginUserId(), SBMUtils.getSession(request), start, limit);
			modelAndView.addObject("processInstanceList", myINcomingTaskList);
			modelAndView.addObject("total",
					ResourceLocator.self().getTaskService().getMyIncomingTasksCount(detailsBean.getLoginUserId()));
			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getSubordinateList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			UserDetailsBean detailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			List<UserMasterVO> analistList = new ArrayList();
			UserMasterVO userMasterVOForLoggeninUser = new UserMasterVO();
			userMasterVOForLoggeninUser.setUserFullName(
					ResourceLocator.self().getUserService().getUserFullNameFromId(detailsBean.getLoginUserId()));
			userMasterVOForLoggeninUser.setUsername(detailsBean.getLoginUserId());
			analistList.add(userMasterVOForLoggeninUser);
			analistList
					.addAll(ResourceLocator.self().getUserService().getSubOrdinateList(detailsBean.getLoginUserId()));
			modelAndView.addObject("subordinateList", analistList);
			return modelAndView;
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}
}