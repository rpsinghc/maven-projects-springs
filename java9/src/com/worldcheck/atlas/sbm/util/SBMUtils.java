package com.worldcheck.atlas.sbm.util;

import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bizmanage.api.BizManageBean;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CaseInfo;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SBMUtils {
	private static ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.sbm.util.SBMUtils");

	public static Map<String, Object> setDS(HttpServletRequest httpServletRequest) throws CMSException {
		HashMap dsMap = new HashMap();

		try {
			if (null != httpServletRequest.getParameter("Analyst")) {
				dsMap.put("Analyst", httpServletRequest.getParameter("Analyst"));
			}

			if (null != httpServletRequest.getParameter("CaseCreator")) {
				dsMap.put("CaseCreator", httpServletRequest.getParameter("CaseCreator"));
			}

			if (null != httpServletRequest.getParameter("CaseManager")) {
				dsMap.put("CaseManager", httpServletRequest.getParameter("CaseManager"));
			}

			if (null != httpServletRequest.getParameter("CaseStatus")) {
				dsMap.put("CaseStatus", httpServletRequest.getParameter("CaseStatus"));
			}

			if (null != httpServletRequest.getParameter("BIManager")) {
				dsMap.put("BIManager", httpServletRequest.getParameter("BIManager"));
			}

			if (null != httpServletRequest.getParameter("BIAndVendorProcessDs")) {
				dsMap.put("BIAndVendorProcessDs", httpServletRequest.getParameter("BIAndVendorProcessDs"));
			}

			if (null != httpServletRequest.getParameter("BIVendorTaskTypeList")) {
				dsMap.put("BIVendorTaskTypeList", httpServletRequest.getParameter("BIVendorTaskTypeList"));
			}

			if (null != httpServletRequest.getParameter("BranchOffice")) {
				dsMap.put("BranchOffice", httpServletRequest.getParameter("BranchOffice"));
			}

			if (null != httpServletRequest.getParameter("CaseOffice")) {
				dsMap.put("CaseOffice", httpServletRequest.getParameter("CaseOffice"));
			}

			if (null != httpServletRequest.getParameter("CRN")) {
				dsMap.put("CRN", httpServletRequest.getParameter("CRN"));
			}

			if (null != httpServletRequest.getParameter("FinanceHead")) {
				dsMap.put("FinanceHead", httpServletRequest.getParameter("FinanceHead"));
			}

			if (null != httpServletRequest.getParameter("ImpactedTeams")) {
				dsMap.put("ImpactedTeams", httpServletRequest.getParameter("ImpactedTeams"));
			}

			if (null != httpServletRequest.getParameter("isAutoOA")) {
				dsMap.put("isAutoOA", httpServletRequest.getParameter("isAutoOA"));
			}

			if (null != httpServletRequest.getParameter("isAutoTeamAssign")) {
				dsMap.put("isAutoTeamAssign", httpServletRequest.getParameter("isAutoTeamAssign"));
			}

			if (null != httpServletRequest.getParameter("isBIProcess")) {
				dsMap.put("isBIProcess", httpServletRequest.getParameter("isBIProcess"));
			}

			if (null != httpServletRequest.getParameter("isCancelled")) {
				dsMap.put("isCancelled", httpServletRequest.getParameter("isCancelled"));
			}

			if (null != httpServletRequest.getParameter("isCSApproved")) {
				dsMap.put("isCSApproved", httpServletRequest.getParameter("isCSApproved"));
			}

			if (null != httpServletRequest.getParameter("isFinalReviewApproved")) {
				dsMap.put("isFinalReviewApproved", httpServletRequest.getParameter("isFinalReviewApproved"));
			}

			if (null != httpServletRequest.getParameter("isPullback")) {
				dsMap.put("isPullback", httpServletRequest.getParameter("isPullback"));
			}

			if (null != httpServletRequest.getParameter("isVendorProcess")) {
				dsMap.put("isVendorProcess", httpServletRequest.getParameter("isVendorProcess"));
			}

			if (null != httpServletRequest.getParameter("isVendorTaskGenerated")) {
				dsMap.put("isVendorTaskGenerated", httpServletRequest.getParameter("isVendorTaskGenerated"));
			}

			if (null != httpServletRequest.getParameter("isFinalCycle")) {
				dsMap.put("isFinalCycle", httpServletRequest.getParameter("isFinalCycle"));
			}

			if (null != httpServletRequest.getParameter("MainAnalyst")) {
				dsMap.put("MainAnalyst", httpServletRequest.getParameter("MainAnalyst"));
			}

			if (null != httpServletRequest.getParameter("MainReviewerList")) {
				dsMap.put("MainReviewerList", httpServletRequest.getParameter("MainReviewerList"));
			}

			if (null != httpServletRequest.getParameter("OfficeHead")) {
				dsMap.put("OfficeHead", httpServletRequest.getParameter("OfficeHead"));
			}

			if (null != httpServletRequest.getParameter("PrimaryAndSupportingProcessDs")) {
				dsMap.put("PrimaryAndSupportingProcessDs",
						httpServletRequest.getParameter("PrimaryAndSupportingProcessDs"));
			}

			if (null != httpServletRequest.getParameter("PrimaryTeam")) {
				dsMap.put("PrimaryTeam", httpServletRequest.getParameter("PrimaryTeam"));
			}

			if (null != httpServletRequest.getParameter("ProcessCycle")) {
				dsMap.put("ProcessCycle", httpServletRequest.getParameter("ProcessCycle"));
			}

			if (null != httpServletRequest.getParameter("PTOfficeHead")) {
				dsMap.put("PTOfficeHead", httpServletRequest.getParameter("PTOfficeHead"));
			}

			if (null != httpServletRequest.getParameter("STReviewerList")) {
				dsMap.put("STReviewerList", httpServletRequest.getParameter("BIVendorResearch"));
			}

			if (null != httpServletRequest.getParameter("SupportingTeam")) {
				dsMap.put("SupportingTeam", httpServletRequest.getParameter("SupportingTeam"));
			}

			if (null != httpServletRequest.getParameter("TaskStatus")) {
				dsMap.put("TaskStatus", httpServletRequest.getParameter("TaskStatus"));
			}

			if (null != httpServletRequest.getParameter("TeamAssignmentGroup")) {
				dsMap.put("TeamAssignmentGroup", httpServletRequest.getParameter("TeamAssignmentGroup"));
			}

			if (null != httpServletRequest.getParameter("TeamAssignmentTaskList")) {
				dsMap.put("TeamAssignmentTaskList", httpServletRequest.getParameter("TeamAssignmentTaskList"));
			}

			if (null != httpServletRequest.getParameter("TeamCount")) {
				dsMap.put("TeamCount", httpServletRequest.getParameter("TeamCount"));
			}

			if (null != httpServletRequest.getParameter("TeamResearchHeadList")) {
				dsMap.put("TeamResearchHeadList", httpServletRequest.getParameter("TeamResearchHeadList"));
			}

			if (null != httpServletRequest.getParameter("TeamStatus")) {
				dsMap.put("TeamStatus", httpServletRequest.getParameter("TeamStatus"));
			}

			if (null != httpServletRequest.getParameter("TeamTypeList")) {
				dsMap.put("TeamTypeList", httpServletRequest.getParameter("TeamTypeList"));
			}

			if (null != httpServletRequest.getParameter("VendorManager")) {
				dsMap.put("VendorManager", httpServletRequest.getParameter("VendorManager"));
			}

			if (null != httpServletRequest.getParameter("TeamType")) {
				dsMap.put("TeamType", httpServletRequest.getParameter("TeamType"));
			}

			if (null != httpServletRequest.getParameter("TaskType")) {
				dsMap.put("TaskType", httpServletRequest.getParameter("TaskType"));
			}

			if (null != httpServletRequest.getParameter("RInterim2")) {
				dsMap.put("RInterim2", httpServletRequest.getParameter("RInterim2"));
			}

			if (null != httpServletRequest.getParameter("RInterim1")) {
				dsMap.put("RInterim1", httpServletRequest.getParameter("RInterim1"));
			}

			if (null != httpServletRequest.getParameter("RFinal")) {
				dsMap.put("RFinal", httpServletRequest.getParameter("RFinal"));
			}

			if (null != httpServletRequest.getParameter("ResearchProcessType")) {
				dsMap.put("ResearchProcessType", httpServletRequest.getParameter("ResearchProcessType"));
			}

			if (null != httpServletRequest.getParameter("ReceivedDate")) {
				dsMap.put("ReceivedDate", httpServletRequest.getParameter("ReceivedDate"));
			}

			if (null != httpServletRequest.getParameter("isRejected")) {
				dsMap.put("isRejected", httpServletRequest.getParameter("isRejected"));
			}

			if (null != httpServletRequest.getParameter("ExpressCase")) {
				dsMap.put("ExpressCase", httpServletRequest.getParameter("ExpressCase"));
			}

			if (null != httpServletRequest.getParameter("ClientReference")) {
				dsMap.put("ClientReference", httpServletRequest.getParameter("ClientReference"));
			}

			if (null != httpServletRequest.getParameter("ClientName")) {
				dsMap.put("ClientName", httpServletRequest.getParameter("ClientName"));
			}

			if (null != httpServletRequest.getParameter("CInterim2")) {
				dsMap.put("CInterim2", httpServletRequest.getParameter("CInterim2"));
			}

			if (null != httpServletRequest.getParameter("CInterim1")) {
				dsMap.put("CInterim1", httpServletRequest.getParameter("CInterim1"));
			}

			if (null != httpServletRequest.getParameter("CFinal")) {
				dsMap.put("CFinal", httpServletRequest.getParameter("CFinal"));
			}

			if (null != httpServletRequest.getParameter("CasePriority")) {
				dsMap.put("CasePriority", httpServletRequest.getParameter("CasePriority"));
			}

			if (null != httpServletRequest.getParameter("CaseInformation")) {
				CaseInfo caseInfo = new CaseInfo();
				caseInfo.setCaseInfoBlock(httpServletRequest.getParameter("CaseInformation"));
				HashMap<String, CaseInfo> map = new HashMap();
				map.put("CaseInfoBlock", caseInfo);
				dsMap.put("CaseInfoBlock", map);
			}

			if (null != httpServletRequest.getParameter("SubReportType")) {
				dsMap.put("SubReportType", httpServletRequest.getParameter("SubReportType"));
			}

			if (null != httpServletRequest.getParameter("ReportType")) {
				dsMap.put("ReportType", httpServletRequest.getParameter("ReportType"));
			}

			if (null != httpServletRequest.getParameter("ClientFeedback")) {
				dsMap.put("ClientFeedback", httpServletRequest.getParameter("ClientFeedback"));
			}

			if (null != httpServletRequest.getParameter("ClientSentDate")) {
				dsMap.put("ClientSentDate", httpServletRequest.getParameter("ClientSentDate"));
			}

			return dsMap;
		} catch (NullPointerException var4) {
			throw new CMSException(logger, var4);
		}
	}

	public static Session getSession(HttpServletRequest httpRequest) throws CMSException {
		Session session = null;
		logger.debug("inside session method reuest is " + httpRequest);

		try {
			HttpSession sess = httpRequest.getSession();
			session = ((BizManageBean) sess.getAttribute("bizManage")).getBizLogicSession();
			logger.debug("user from request session is " + session.getUser());
			return session;
		} catch (NullPointerException var3) {
			throw new CMSException(logger, var3);
		}
	}

	public static boolean checkForPermission(String cycleName, String teamName, String workItemName, Session session)
			throws NumberFormatException, CMSException {
		boolean hasPermission = false;
		long pid = Long.parseLong(workItemName.split("::")[0].split("#")[1]);
		long parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID", session);
		CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) ((CycleTeamMapping) ((Map) ResourceLocator.self()
				.getSBMService().getDataslotValue(parentPID, "customDSMap", session)).get("CycleTeamMapping"));
		if (null != cycleTeamMapping.getCycleInformation().get("Interim1")
				&& null == ((CycleInfo) cycleTeamMapping.getCycleInformation().get("Interim1")).getTeamInfo()
						.get(teamName)
				&& !cycleTeamMapping.getCurrentCycle().equalsIgnoreCase(cycleName)) {
			hasPermission = true;
		}

		if (hasPermission || cycleTeamMapping.getCurrentCycle().equalsIgnoreCase(cycleName)) {
			hasPermission = true;
		}

		return hasPermission;
	}

	public static boolean isFinanceUser(HttpSession session, String roleNames) {
		boolean isFinanceUser = false;
		UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
		List<String> roleList = userDetailsBean.getRoleList();
		String[] roleName = roleNames.split(",");

		for (int i = 0; i < roleName.length; ++i) {
			if (roleList.contains(roleName[i])) {
				isFinanceUser = true;
				break;
			}
		}

		return isFinanceUser;
	}
}