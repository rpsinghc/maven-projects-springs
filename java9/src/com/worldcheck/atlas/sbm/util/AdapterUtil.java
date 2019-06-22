package com.worldcheck.atlas.sbm.util;

import com.savvion.sbm.bizlogic.server.ejb.BLServer;
import com.savvion.sbm.bizlogic.server.ejb.BLServerHome;
import com.savvion.sbm.bizlogic.server.svo.ProcessInstance;
import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.util.PService;
import com.savvion.sbm.util.SBMHomeFactory;
import com.tdiinc.userManager.JDBCRealm;
import com.tdiinc.userManager.UserManager;
import com.worldcheck.atlas.sbm.customds.AnalystTaskStatus;
import com.worldcheck.atlas.sbm.customds.AtlasAdapterUtil;
import com.worldcheck.atlas.sbm.customds.AtlasAdapterVO;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.sbm.customds.TeamAnalystMapping;
import com.worldcheck.atlas.sbm.ibatis.LockInfoVO;
import com.worldcheck.atlas.sbm.ibatis.MutexImpl;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

public class AdapterUtil {
	private Session userSession = null;
	private Map<String, String> sbmPropertyMap = null;
	private BLServer blServer;
	private ProcessInstance processInstance;

	public void updateResearchStatus(AtlasAdapterVO adapterVO) throws Exception {
		LockInfoVO lockVO = new LockInfoVO();
		lockVO.setCrn(adapterVO.getCrn());

		try {
			this.isWaitCondition(lockVO);
			System.out.println("after wait condition");
			this.userSession = this.getSession();
			System.out.println("user session is " + this.userSession);
			System.out.println("parent pid is :::: " + adapterVO.getParentPID());
			System.out.println("child pid is :::: " + adapterVO.getPid());
			System.out.println("is bi status is " + adapterVO.isBIVendor());
			CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((HashMap) this
					.getDataslotValue(adapterVO.getParentPID(), "customDSMap")).get("CycleTeamMapping");
			String teamCycleName = adapterVO.getTeamCycleName();
			String teamTypeList = adapterVO.getTeamTypeList();
			String manager = adapterVO.getManager();
			Map<String, CycleInfo> tempCycleInfo = cycleTeamMap.getCycleInformation();
			String currentCycle = cycleTeamMap.getCurrentCycle();
			CycleInfo cycleInfo = (CycleInfo) cycleTeamMap.getCycleInformation().get(teamCycleName);
			HashMap tempMap;
			Map tempTeamAnalystMap;
			if (adapterVO.isBIVendor()) {
				tempMap = new HashMap();
				System.out.println("PID for research task is " + adapterVO.getPid());
				tempTeamAnalystMap = cycleInfo.getTeamInfo();
				System.out.println(
						"in bi " + ((AnalystTaskStatus) ((TeamAnalystMapping) tempTeamAnalystMap.get(teamTypeList))
								.getAnalystTaskStatus().get(manager)).getStatus());
				((AnalystTaskStatus) ((TeamAnalystMapping) tempTeamAnalystMap.get(teamTypeList)).getAnalystTaskStatus()
						.get(manager)).setStatus("Done");
				((TeamAnalystMapping) tempTeamAnalystMap.get(teamTypeList)).setResearchTaskStatus("Done");
				cycleInfo.setTeamInfo(tempTeamAnalystMap);
				tempCycleInfo.put(teamCycleName, cycleInfo);
				cycleTeamMap.setCycleInformation(tempCycleInfo);
				Map<String, CycleTeamMapping> tempFinalMap = new HashMap();
				tempFinalMap.put("CycleTeamMapping", cycleTeamMap);
				tempMap.put("customDSMap", tempFinalMap);
				this.updateDataSlots(adapterVO.getParentPID(), tempMap);
			} else {
				tempMap = new HashMap();
				System.out.println("PID for research task is " + adapterVO.getPid());
				System.out.println("PID for case task is " + adapterVO.getParentPID());
				System.out.println("Team name is " + teamCycleName);
				tempTeamAnalystMap = cycleInfo.getTeamInfo();
				TeamAnalystMapping teamAnalystMap = (TeamAnalystMapping) tempTeamAnalystMap.get(teamTypeList);
				String mainAnalyst = teamAnalystMap.getMainAnalyst();
				System.out.println("main analyst id is " + mainAnalyst);
				Vector<String> reviewers = new Vector();
				Set<String> keySet = teamAnalystMap.getReviewers().keySet();
				Iterator iterator = keySet.iterator();

				while (iterator.hasNext()) {
					String string = (String) iterator.next();
					reviewers.add(string);
					System.out.println("Reviewr is " + string);
				}

				if (!teamTypeList.contains("BI") && !teamTypeList.contains("Vendor")) {
					String tempMainAnalyst;
					if (!teamTypeList.contains("Primary")) {
						tempMainAnalyst = (String) this.getDataslotValue(this.getParentPID(adapterVO.getPid()),
								"MainAnalyst");
						if (null == tempMainAnalyst || "null".equals(tempMainAnalyst)
								|| "<NULL>".equalsIgnoreCase(tempMainAnalyst) || "".equalsIgnoreCase(tempMainAnalyst)) {
							tempMap.put("MainAnalyst", mainAnalyst);
							tempMap.put("Reviewers", reviewers);
							this.updateDataSlots(this.getParentPID(adapterVO.getPid()), tempMap);
							tempMap = new HashMap();
						}
					} else {
						tempMainAnalyst = (String) this.getDataslotValue(adapterVO.getParentPID(), "MainAnalyst");
						System.out.println("Main PT main analyst is " + tempMainAnalyst);
						if (null == tempMainAnalyst || "null".equals(tempMainAnalyst)
								|| "<NULL>".equalsIgnoreCase(tempMainAnalyst) || "".equalsIgnoreCase(tempMainAnalyst)) {
							tempMap.put("MainAnalyst", mainAnalyst);
							tempMap.put("Reviewers", reviewers);
						}
					}
				}

				Map<String, AnalystTaskStatus> tempAnalysts = teamAnalystMap.getAnalystTaskStatus();
				System.out.println("TeamAnalyst map  status is " + teamAnalystMap.getAnalystTaskStatus());
				System.out.println("adapterVO.getManager() is " + adapterVO.getManager());
				AnalystTaskStatus analystTaskStatus = (AnalystTaskStatus) tempAnalysts.get(adapterVO.getManager());
				analystTaskStatus.setStatus("Done");
				tempAnalysts.put(adapterVO.getManager(), analystTaskStatus);
				teamAnalystMap.setResearchTaskStatus("Done");
				Iterator iterator = tempAnalysts.keySet().iterator();

				while (iterator.hasNext()) {
					String string = (String) iterator.next();
					System.out.println("in side research task analyst is  " + string);
					System.out.println("status is " + ((AnalystTaskStatus) tempAnalysts.get(string)).getStatus());
					if (!"Done".equalsIgnoreCase(((AnalystTaskStatus) tempAnalysts.get(string)).getStatus())) {
						teamAnalystMap.setResearchTaskStatus("In Progress");
						break;
					}
				}

				teamAnalystMap.setAnalystTaskStatus(tempAnalysts);
				tempTeamAnalystMap.put(teamTypeList, teamAnalystMap);
				System.out.println("Team type list" + teamTypeList);
				System.out.println("Current cyle is " + currentCycle);
				System.out.println("pid is " + adapterVO.getPid());
				System.out.println("parent PID is " + adapterVO.getParentPID());
				cycleInfo.setTeamInfo(tempTeamAnalystMap);
				tempCycleInfo.put(teamCycleName, cycleInfo);
				cycleTeamMap.setCycleInformation(tempCycleInfo);
				Map<String, CycleTeamMapping> tempFinalMap = new HashMap();
				tempFinalMap.put("CycleTeamMapping", cycleTeamMap);
				tempMap.put("customDSMap", tempFinalMap);
				this.updateDataSlots(adapterVO.getParentPID(), tempMap);
			}
		} catch (Exception var27) {
			System.out.println(var27 + "");
			throw new Exception(var27 + "");
		} finally {
			try {
				System.out.println("crn is " + lockVO.getCrn());
				this.releaseLock(lockVO);
				this.closeSession(this.userSession);
			} catch (Exception var26) {
				System.out.println(var26 + "");
				throw new Exception(var26 + "");
			}
		}

	}

	public void updateTeamStatus(AtlasAdapterVO adapterVO) throws Exception {
		LockInfoVO lockVO = new LockInfoVO();
		lockVO.setCrn(adapterVO.getCrn());

		try {
			this.isWaitCondition(lockVO);
			HashMap<String, Object> dsValues = new HashMap();
			boolean consolidationRequired = true;
			String teamCycleName = adapterVO.getTeamCycleName();
			String teamTypeList = adapterVO.getTeamTypeList();
			System.out.println("teamCycleName is " + teamCycleName + " and team name is ::::: " + teamTypeList);
			this.userSession = this.getSession();
			System.out.println("user session is " + this.userSession);
			System.out.println("parent pid is :::: " + adapterVO.getParentPID());
			System.out.println("child pid is :::: " + adapterVO.getPid());
			CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((HashMap) this
					.getDataslotValue(adapterVO.getParentPID(), "customDSMap")).get("CycleTeamMapping");
			Map<String, TeamAnalystMapping> teamAnlystMap = ((CycleInfo) cycleTeamMap.getCycleInformation()
					.get(teamCycleName)).getTeamInfo();
			Iterator iterator = teamAnlystMap.keySet().iterator();

			while (true) {
				if (iterator.hasNext()) {
					String string = (String) iterator.next();
					if (teamTypeList.equalsIgnoreCase(string)) {
						continue;
					}

					if (this.isTaskCompleted(
							((TeamAnalystMapping) teamAnlystMap.get(string)).getResearchProcessPID())) {
						System.out.println("teamAnlystMap.get(string).getResearchProcessPID() "
								+ ((TeamAnalystMapping) teamAnlystMap.get(string)).getResearchProcessPID());
						continue;
					}

					System.out.println("WORK IN PROGRESS FOR TEAM " + string + " FOR CYCLE " + teamCycleName);
					consolidationRequired = false;
				}

				Map<String, CycleTeamMapping> tempCycleTeamMap = new HashMap();
				tempCycleTeamMap.put("CycleTeamMapping", cycleTeamMap);
				dsValues.put("customDSMap", tempCycleTeamMap);
				dsValues.put("startConsolidation", consolidationRequired);
				System.out.println("Comsolidation required is " + consolidationRequired + " this team's status is "
						+ ((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation().get(teamCycleName))
								.getTeamInfo().get(teamTypeList)).getResearchTaskStatus());
				this.updateDataSlots(adapterVO.getParentPID(), dsValues);
				return;
			}
		} catch (Exception var18) {
			System.out.println(var18 + "");
			throw new Exception(var18 + "");
		} finally {
			try {
				this.releaseLock(lockVO);
				this.closeSession(this.userSession);
			} catch (Exception var17) {
				System.out.println(var17 + "");
				throw new Exception(var17 + "");
			}
		}
	}

	public void updatePID(AtlasAdapterVO adapterVO) throws Exception {
		System.out.println("in update PID method");
		LockInfoVO lockVO = new LockInfoVO();
		lockVO.setCrn(adapterVO.getCrn());

		try {
			this.isWaitCondition(lockVO);
			HashMap<String, Object> dsValues = new HashMap();
			String teamCycleName = adapterVO.getTeamCycleName();
			String teamTypeList = adapterVO.getTeamTypeList();
			this.userSession = this.getSession();
			CycleTeamMapping cycleTeamMap = (CycleTeamMapping) ((HashMap) this
					.getDataslotValue(adapterVO.getParentPID(), "customDSMap")).get("CycleTeamMapping");
			if (adapterVO.isBIVendor()) {
				((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation().get(teamCycleName)).getTeamInfo()
						.get(teamTypeList)).setResearchProcessPID(adapterVO.getPid());
				((AnalystTaskStatus) ((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation()
						.get(teamCycleName)).getTeamInfo().get(teamTypeList)).getAnalystTaskStatus()
								.get(adapterVO.getManager())).setResearchPID(adapterVO.getPid());
				((AnalystTaskStatus) ((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation()
						.get(teamCycleName)).getTeamInfo().get(teamTypeList)).getAnalystTaskStatus()
								.get(adapterVO.getManager())).setStatus("In Progress");
				((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation().get(teamCycleName)).getTeamInfo()
						.get(teamTypeList)).setResearchTaskStatus("In Progress");
			} else {
				System.out.println("team is atlasadapter " + teamTypeList);
				long pid = ((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation().get(teamCycleName))
						.getTeamInfo().get(teamTypeList)).getResearchProcessPID();
				System.out.println("r process pid is " + pid);
				System.out.println("inside " + this.getParentPID(adapterVO.getPid()));
				((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation().get(teamCycleName)).getTeamInfo()
						.get(teamTypeList)).setResearchProcessPID(this.getParentPID(adapterVO.getPid()));
				((AnalystTaskStatus) ((TeamAnalystMapping) ((CycleInfo) cycleTeamMap.getCycleInformation()
						.get(teamCycleName)).getTeamInfo().get(teamTypeList)).getAnalystTaskStatus()
								.get(adapterVO.getManager())).setResearchPID(adapterVO.getPid());
			}

			Map<String, CycleTeamMapping> tempCycleTeamMap = new HashMap();
			tempCycleTeamMap.put("CycleTeamMapping", cycleTeamMap);
			dsValues.put("customDSMap", tempCycleTeamMap);
			this.updateDataSlots(adapterVO.getParentPID(), dsValues);
		} catch (Exception var16) {
			System.out.println(var16 + "");
			throw new Exception(var16 + "");
		} finally {
			try {
				this.releaseLock(lockVO);
				this.closeSession(this.userSession);
			} catch (Exception var15) {
				System.out.println(var15 + "");
				throw new Exception(var15 + "");
			}
		}

	}

	public long getParentPID(long pid) throws RemoteException, Exception {
		long parentPID = 0L;
		this.processInstance = ProcessInstance.get(this.userSession, pid);
		parentPID = this.processInstance.getParentID();
		System.out.println("parent PID is " + parentPID);
		return parentPID;
	}

	public boolean isTaskCompleted(long pid) throws Exception {
		boolean pidCompleted = false;
		System.out.println("checking for pid " + pid + " Session is " + this.userSession);
		if (0L == pid) {
			return pidCompleted;
		} else {
			try {
				this.processInstance = ProcessInstance.get(this.userSession, pid);
			} catch (RemoteException var5) {
				pidCompleted = true;
			}

			return pidCompleted;
		}
	}

	private void isWaitCondition(LockInfoVO lockVO) throws InterruptedException, SQLException, IOException {
		boolean isStillWaiting = true;
		ClassLoader classLoader = AtlasAdapterUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("sbmjndi.properties");
		Properties prop = new Properties();
		prop.load(is);
		lockVO.setIpAddress(prop.getProperty("sbm.pramati.provider.url"));
		int i = 0;

		do {
			if (null == this.checkForLock(lockVO) || "".equalsIgnoreCase(this.checkForLock(lockVO).getIpAddress())) {
				System.out.println(isStillWaiting + "");
				isStillWaiting = false;
				i = 0;
			}

			if (i > 0) {
				Thread.sleep(2000L);
			}

			if (i > 30) {
				i = 0;
				if (!this.isJNDILookupSuceeded(lockVO)) {
					i = 0;
					this.releaseLock(lockVO);
					isStillWaiting = false;
				}
			}

			if (!isStillWaiting) {
				try {
					this.getLock(lockVO);
				} catch (SQLException var8) {
					int i = false;
					isStillWaiting = true;
					System.out.println("Two threads tried to access the DB at same time...");
					throw new SQLException();
				}
			}

			++i;
		} while (isStillWaiting);

	}

	private void getLock(LockInfoVO lockVO) throws SQLException, IOException {
		MutexImpl mutex = new MutexImpl();
		mutex.getLock(lockVO);
	}

	private void releaseLock(LockInfoVO lockVO) throws SQLException, IOException {
		MutexImpl mutex = new MutexImpl();
		mutex.releaseLock(lockVO);
	}

	private boolean isJNDILookupSuceeded(LockInfoVO lockVO) throws IOException, SQLException {
		MutexImpl mutex = new MutexImpl();
		return mutex.JNDIlookupSuceeded(this.checkForLock(lockVO).getIpAddress());
	}

	public LockInfoVO checkForLock(LockInfoVO lockVO) throws SQLException, IOException {
		MutexImpl mutex = new MutexImpl();
		return mutex.checkForLock(lockVO);
	}

	private void loadProperties() throws Exception {
		ClassLoader classLoader = AtlasAdapterUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("atlas.properties");
		Properties prop = new Properties();
		prop.load(is);
		this.sbmPropertyMap = new HashMap();
		this.sbmPropertyMap.put("INITIAL_FACTORY", prop.getProperty("atlas.pramati.factory.initial"));
		this.sbmPropertyMap.put("PROVIDER_URL", prop.getProperty("atlas.pramati.provider.url"));
		this.sbmPropertyMap.put("PRINCIPAL", prop.getProperty("atlas.pramati.principal"));
		this.sbmPropertyMap.put("CREDENTIALS", PService.self().decrypt(prop.getProperty("atlas.pramati.credentials")));
		this.sbmPropertyMap.put("JNDI_NAME", prop.getProperty("atlas.pramati.provider.jndiname"));
		System.out.println("Properties Successfully loaded.....");
	}

	public void closeSession(Session userSession) throws Exception {
		this.blServer.disConnect(userSession);
		System.out.println("Session closed");
	}

	public Session getSession() throws Exception {
		this.loadProperties();
		JDBCRealm jdbRealm = new JDBCRealm();
		String adminId = null;
		String adminUserPwdInDB = null;
		String[] allUsers = jdbRealm.getUserNames();

		for (int i = 0; i < allUsers.length; ++i) {
			if (UserManager.getUserIDInDB(allUsers[i]) == 1) {
				adminId = allUsers[i];
				adminUserPwdInDB = UserManager.getUser(allUsers[i]).getAttribute("PASSWORD");
				adminUserPwdInDB = PService.self().decrypt(adminUserPwdInDB);
				break;
			}
		}

		System.out.println("Admin user is " + adminId);
		BLServerHome server_home = (BLServerHome) SBMHomeFactory.lookupHome(this.sbmPropertyMap, BLServerHome.class);
		this.blServer = server_home.create();
		this.userSession = this.blServer.connect(adminId, adminUserPwdInDB);
		System.out.println("Succesfully connected to Server session Id is " + this.userSession.getID());
		return this.userSession;
	}

	public Object getDataslotValue(long piid, String dsName) throws Exception {
		Object obj = null;
		this.processInstance = ProcessInstance.get(this.userSession, piid);
		obj = this.processInstance.getDataSlotValue(dsName);
		return obj;
	}

	public void updateDataSlots(long pid, HashMap<String, Object> dsValues) throws Exception {
		System.out.println("inside update dataslot method pid is " + pid);
		System.out.println(" for user " + this.userSession.getUser() + " pid is " + pid + " has map is " + dsValues);
		ProcessInstance.updateDataSlot(this.userSession, pid, dsValues);
		System.out.println("after updating dataslots..");
	}

	public void updateDS(long pid, HashMap<String, Object> dsValues, AtlasAdapterVO adapterVO) throws Exception {
		LockInfoVO lockVO = new LockInfoVO();
		lockVO.setCrn(adapterVO.getCrn());

		try {
			this.isWaitCondition(lockVO);
			System.out.println("after wait condition");
			this.userSession = this.getSession();
			System.out.println("user session is " + this.userSession);
			this.updateDataSlots(pid, dsValues);
		} catch (Exception var14) {
			System.out.println(var14 + "");
			throw new Exception(var14 + "");
		} finally {
			try {
				this.releaseLock(lockVO);
				this.closeSession(this.userSession);
			} catch (Exception var13) {
				System.out.println(var13 + "");
				throw new Exception(var13 + "");
			}
		}

	}

	public boolean checkWaitCondition(String cycleName, long pid) throws Exception {
		boolean isConditionMatches = false;
		String currentCycle = "";

		try {
			System.out.println("after wait condition");
			this.userSession = this.getSession();
			System.out.println("user session is " + this.userSession);
			currentCycle = (String) this.getDataslotValue(pid, "ProcessCycle");
			if (null != currentCycle && currentCycle.equalsIgnoreCase(cycleName)) {
				isConditionMatches = true;
			}
		} catch (Exception var14) {
			System.out.println(var14 + "");
			throw new Exception(var14 + "");
		} finally {
			try {
				this.closeSession(this.userSession);
			} catch (Exception var13) {
				System.out.println(var13 + "");
				throw new Exception(var13 + "");
			}
		}

		return isConditionMatches;
	}
}