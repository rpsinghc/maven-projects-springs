package com.worldcheck.atlas.sbm.queryService;

import java.util.HashMap;
import java.util.Map;

public class TaskProcessMapping {
	private static final Map<String, String> taskProcessMap = new HashMap();

	public static String getProcessName(String taskName) {
		return taskProcessMap.containsKey(taskName) ? (String) taskProcessMap.get(taskName) : new String();
	}

	static {
		taskProcessMap.put("Complete Case Creation", "CaseCreation");
		taskProcessMap.put("Primary Consolidation Task", "CaseCreation");
		taskProcessMap.put("Office Assignment Task", "CaseCreation");
		taskProcessMap.put("Client Submission Task", "CaseCreation");
		taskProcessMap.put("Awaiting Consolidation", "CaseCreation");
		taskProcessMap.put("Invoicing Task", "CaseCreation");
		taskProcessMap.put("Supporting Consolidation Task", "ResearchProcess");
		taskProcessMap.put("Research Task", "ResearchTask");
		taskProcessMap.put("Review Task", "Review");
		taskProcessMap.put("Team Assignment Task", "TeamAssignment");
		taskProcessMap.put("BI Research Task", "BIVendorResearch");
		taskProcessMap.put("Vendor Research Task", "BIVendorResearch");
	}
}