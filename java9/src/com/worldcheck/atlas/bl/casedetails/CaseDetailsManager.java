package com.worldcheck.atlas.bl.casedetails;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ICaseDetailManager;
import com.worldcheck.atlas.dao.audit.RiskHistoryDAO;
import com.worldcheck.atlas.dao.casedetails.CaseDetailsDAO;
import com.worldcheck.atlas.dao.masters.RiskDAO;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.CaseStatus;
import com.worldcheck.atlas.vo.EmailTemplateVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.MyOnHoldCaseVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.io.StringReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.internet.InternetAddress;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CaseDetailsManager implements ICaseDetailManager {
	private static final String ACTION_REQ_ON_HOLD_STATUS_FOR_THIS_CASE_IS_LIFTED_PLEASE_PROCEED_ON_WITH_THE_CASE = "ACTION REQ: On-Hold status for this case is lifted. Please proceed on with the case.";
	private static final String ON_HOLD_STATUS_IS_LIFTED_FOR_THIS_CASE = "On-Hold status is lifted for this case.";
	private static final String CASE_INFORMATION_HAS_BEEN_UPDATED_HERE_IS_THE_LATEST_ENTRY = "Case Information has been updated. Here is the latest entry :";
	private static final String YOU_ARE_ASSIGNED_THE_CASE_MANAGER_FOR_THIS_CASE = "You are assigned the Case Manager for this case.";
	private static final String YOU_ARE_NO_LONGER_THE_CASE_MANAGER_FOR_THIS_CASE = "You are no longer the Case Manager for this case.";
	private static final String NEW_CASE_MANAGER_IS_ASSIGNED_TO_THIS_CASE = "New Case Manager is assigned to this case.";
	private static final String THE_CASE_IS_NOW_CANCELLED = "The case is now CANCELLED.";
	private static final String THE_CASE_IS_NOW_PUT_ON_HOLD = "The case is now put ON-HOLD.";
	private static final String CASE = "Case";
	private static final String FALSE = "False";
	private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	private static final String ACTIONUPDATE = "Update";
	private static final String BREAKLINE = "<br />";
	private static final String BOLDSTART = "<b>";
	private static final String BOLDEND = "</b>";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.casedetails.CaseDetailsManager");
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	CaseDetailsDAO caseDetailsDAO = null;
	RiskDAO riskDAO = null;
	RiskProfileDAO riskProfileDAO = null;
	private RiskHistoryDAO riskHistoryDAO = null;
	private static PropertyReaderUtil propertyReader;
	private static String emailBodyPrepared = "";

	public void setRiskDAO(RiskDAO riskDAO) {
		this.riskDAO = riskDAO;
	}

	public void setRiskProfileDAO(RiskProfileDAO riskProfileDAO) {
		this.riskProfileDAO = riskProfileDAO;
	}

	public void setRiskHistoryDAO(RiskHistoryDAO riskHistoryDAO) {
		this.riskHistoryDAO = riskHistoryDAO;
	}

	public void setCaseDetailsDAO(CaseDetailsDAO caseDetailsDAO) {
		this.caseDetailsDAO = caseDetailsDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public CaseDetails getCaseDetails(String crn) throws CMSException {
		this.logger.debug("In getCaseDetails");
		new CaseDetails();

		try {
			CaseDetails caseDetails = this.caseDetailsDAO.getCaseDetails(crn);
			return caseDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public CaseDetails getCaseStatus(String crn) throws CMSException {
		this.logger.debug("In getCaseStatus");

		try {
			return this.caseDetailsDAO.getCaseStatus(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int saveCaseInformation(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap)
			throws CMSException {
		int count = 0;
		boolean updateStatus = true;
		boolean processFlag = true;

		try {
			this.logger.debug("going to update the following dataslots from case information " + dsMap);
			CaseDetails oldCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
			if (caseDetails.getCaseStatusId() == 4 && oldCaseDetails.getIsISIS() == 1) {
				updateStatus = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				if (!updateStatus) {
					return -1;
				}

				this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
				ClientCaseStatusVO clientCaseStatusVO = new ClientCaseStatusVO();
				clientCaseStatusVO.setCRN(oldCaseDetails.getCrn());
				clientCaseStatusVO.setStatus("CNL");
				clientCaseStatusVO.setFileName("");
				clientCaseStatusVO.setVersion(1.0D);
				clientCaseStatusVO.setExpressCase("False");
				clientCaseStatusVO.setUpdateType("Case");
				processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
			}

			if (updateStatus && processFlag) {
				count = this.caseDetailsDAO.saveCaseInformation(caseDetails);
				if (caseDetails.getCaseStatusId() == 4 || caseDetails.getCaseStatusId() == 1) {
					ResourceLocator.self().getTimeTrackerService().stopAllTrackerForCase(caseDetails.getCrn(),
							caseDetails.getUpdatedBy());
				}

				ResourceLocator.self().getFlowService().updateCaseInformationDS(session, oldCaseDetails, caseDetails,
						dsMap);
				CaseDetails newCaseDetails = this.getCaseDetails(caseDetails.getCrn());
				this.logger.debug("Long.parseLong(caseDetails.getPid()) " + Long.parseLong(oldCaseDetails.getPid()));
				this.sendNotification(oldCaseDetails, newCaseDetails, caseDetails.getUpdatedBy());
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setPid(oldCaseDetails.getPid());
				caseHistory.setCRN(oldCaseDetails.getCrn());
				caseHistory.setPerformer(caseDetails.getCaseHistoryPerformer());
				caseHistory.setTaskName("");
				caseHistory.setTaskStatus("");
				caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
						.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
				newCaseDetails.setCaseHistoryPerformer(caseDetails.getCaseHistoryPerformer());
				ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
						caseHistory, "caseDetails");
			}

			return count;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	private void sendNotification(CaseDetails oldCaseDetails, CaseDetails newCaseDetails, String userId)
			throws CMSException {
		try {
			List<UserMasterVO> sendToUserList = ResourceLocator.self().getNotificationService()
					.getAssignedUsers(newCaseDetails.getCrn(), userId);
			List<String> userList = new ArrayList();
			Iterator iterator = sendToUserList.iterator();

			while (iterator.hasNext()) {
				UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
				userList.add(userMasterVO.getUsername());
			}

			List managersUser;
			Iterator iterator;
			UserMasterVO userMasterVO;
			ArrayList onHoldCaseUserList;
			List researchUser;
			if (oldCaseDetails.getCaseStatusId() != 1 && newCaseDetails.getCaseStatusId() == 1) {
				onHoldCaseUserList = new ArrayList();
				onHoldCaseUserList.addAll(userList);
				if (oldCaseDetails.getIsISIS() != 1) {
					onHoldCaseUserList.add(oldCaseDetails.getCaseCreatorId());
				}

				researchUser = this.caseDetailsDAO.getResearchHeadsForCase(newCaseDetails.getCrn());
				onHoldCaseUserList.addAll(researchUser);
				managersUser = ResourceLocator.self().getUserService().getUsersForRole("R3");
				iterator = managersUser.iterator();

				while (iterator.hasNext()) {
					userMasterVO = (UserMasterVO) iterator.next();
					onHoldCaseUserList.add(userMasterVO.getUserID());
				}

				ResourceLocator.self().getNotificationService().createSystemNotification("The case is now put ON-HOLD.",
						"The case is now put ON-HOLD.", onHoldCaseUserList, newCaseDetails.getCrn());
			}

			this.logger.debug("Lift On-Hold oldCaseDetails.getCaseStatusId() && newCaseDetails.getCaseStatusId() "
					+ oldCaseDetails.getCaseStatusId() + "---" + newCaseDetails.getCaseStatusId());
			if (oldCaseDetails.getCaseStatusId() == 1 && newCaseDetails.getCaseStatusId() != 1) {
				onHoldCaseUserList = new ArrayList();
				onHoldCaseUserList.addAll(userList);
				this.logger.debug("Lift On-Hold-oldCaseDetails.getIsISIS()=" + oldCaseDetails.getIsISIS());
				if (oldCaseDetails.getIsISIS() != 1) {
					onHoldCaseUserList.add(oldCaseDetails.getCaseCreatorId());
				}

				researchUser = this.caseDetailsDAO.getResearchHeadsForCase(newCaseDetails.getCrn());
				managersUser = this.caseDetailsDAO.getTeamManagerForCase(newCaseDetails.getCrn());
				onHoldCaseUserList.addAll(researchUser);
				onHoldCaseUserList.addAll(managersUser);
				List<UserMasterVO> financeUserList = ResourceLocator.self().getUserService().getUsersForRole("R3");
				Iterator iterator = financeUserList.iterator();

				while (iterator.hasNext()) {
					UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
					onHoldCaseUserList.add(userMasterVO.getUserID());
				}

				List<MyTaskPageVO> myTaskPageVoList = ResourceLocator.self().getSBMService()
						.getAllTaskForCrn(newCaseDetails.getCrn());
				HashMap<String, String> myTask = new HashMap();
				Iterator iterator = myTaskPageVoList.iterator();

				while (iterator.hasNext()) {
					MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
					if (myTaskPageVO.getPerformer() != null) {
						myTask.put(myTaskPageVO.getPerformer(), myTaskPageVO.getPerformer());
					}
				}

				Set myTaskSet = myTask.keySet();
				List<String> onHoldActiveCaseUserList = new ArrayList();
				List<String> onHoldFinalActiveCaseUserList = new ArrayList();
				Iterator iterator = myTaskSet.iterator();

				String memberUser;
				while (iterator.hasNext()) {
					memberUser = (String) iterator.next();
					onHoldActiveCaseUserList.add(memberUser);
				}

				iterator = userList.iterator();

				while (iterator.hasNext()) {
					memberUser = (String) iterator.next();
					if (onHoldActiveCaseUserList.contains(memberUser)) {
						onHoldFinalActiveCaseUserList.add(memberUser);
					} else {
						onHoldCaseUserList.add(memberUser);
					}
				}

				this.logger.debug("number of user which has active tasks " + onHoldFinalActiveCaseUserList.size());
				this.logger.debug("number of user which does not have active tasks " + onHoldCaseUserList.size());
				ResourceLocator.self().getNotificationService().createSystemNotification(
						"On-Hold status is lifted for this case.", "On-Hold status is lifted for this case.",
						onHoldCaseUserList, newCaseDetails.getCrn());
				ResourceLocator.self().getNotificationService().createSystemNotification(
						"ACTION REQ: On-Hold status for this case is lifted. Please proceed on with the case.",
						"ACTION REQ: On-Hold status for this case is lifted. Please proceed on with the case.",
						onHoldFinalActiveCaseUserList, newCaseDetails.getCrn());
			}

			if (oldCaseDetails.getCaseStatusId() != 4 && newCaseDetails.getCaseStatusId() == 4) {
				onHoldCaseUserList = new ArrayList();
				onHoldCaseUserList.addAll(userList);
				if (oldCaseDetails.getIsISIS() != 1) {
					onHoldCaseUserList.add(oldCaseDetails.getCaseCreatorId());
				}

				researchUser = this.caseDetailsDAO.getResearchHeadsForCase(newCaseDetails.getCrn());
				onHoldCaseUserList.addAll(researchUser);
				managersUser = ResourceLocator.self().getUserService().getUsersForRole("R3");
				iterator = managersUser.iterator();

				while (iterator.hasNext()) {
					userMasterVO = (UserMasterVO) iterator.next();
					onHoldCaseUserList.add(userMasterVO.getUserID());
				}

				ResourceLocator.self().getNotificationService().createSystemNotification("The case is now CANCELLED.",
						"The case is now CANCELLED.", onHoldCaseUserList, newCaseDetails.getCrn());
			}

			if (!oldCaseDetails.getCaseMgrId().equalsIgnoreCase(newCaseDetails.getCaseMgrId())) {
				onHoldCaseUserList = new ArrayList();
				Iterator iterator = userList.iterator();

				while (iterator.hasNext()) {
					String receiver = (String) iterator.next();
					if (!receiver.equalsIgnoreCase(oldCaseDetails.getCaseMgrId())
							&& !receiver.equalsIgnoreCase(newCaseDetails.getCaseMgrId())) {
						onHoldCaseUserList.add(receiver);
					}
				}

				researchUser = this.caseDetailsDAO.getResearchHeadsForCase(newCaseDetails.getCrn());
				managersUser = this.caseDetailsDAO.getTeamManagerForCase(newCaseDetails.getCrn());
				onHoldCaseUserList.addAll(researchUser);
				onHoldCaseUserList.addAll(managersUser);
				ResourceLocator.self().getNotificationService().createSystemNotification(
						"New Case Manager is assigned to this case.", "New Case Manager is assigned to this case.",
						onHoldCaseUserList, newCaseDetails.getCrn());
				ResourceLocator.self().getNotificationService().createSystemNotification(
						"You are no longer the Case Manager for this case.",
						"You are no longer the Case Manager for this case.", oldCaseDetails.getCaseMgrId(),
						newCaseDetails.getCrn());
				ResourceLocator.self().getNotificationService().createSystemNotification(
						"You are assigned the Case Manager for this case.",
						"You are assigned the Case Manager for this case.", newCaseDetails.getCaseMgrId(),
						newCaseDetails.getCrn());
			}

			if ((oldCaseDetails.getCaseInfo() != null || newCaseDetails.getCaseInfo() != null)
					&& (oldCaseDetails.getCaseInfo() == null && newCaseDetails.getCaseInfo() != null
							|| oldCaseDetails.getCaseInfo() != null && newCaseDetails.getCaseInfo() == null
							|| !oldCaseDetails.getCaseInfo().equalsIgnoreCase(newCaseDetails.getCaseInfo()))) {
				ResourceLocator.self().getNotificationService().createSystemNotification(
						"Case Information has been updated. Here is the latest entry :" + newCaseDetails.getCaseInfo(),
						"Case Information has been updated. Here is the latest entry :" + newCaseDetails.getCaseInfo(),
						userList, newCaseDetails.getCrn());
			}

			if (!userId.equalsIgnoreCase(newCaseDetails.getCaseMgrId())) {
				Date oldClientInterim1DueDate = oldCaseDetails.getcInterim1();
				Date oldClientInterim2DueDate = oldCaseDetails.getcInterim2();
				Date oldClientFinalDueDate = oldCaseDetails.getFinalDueDate();
				Date newClientInterim1DueDate = newCaseDetails.getcInterim1();
				Date newClientInterim2DueDate = newCaseDetails.getcInterim2();
				Date newClientFinalDueDate = newCaseDetails.getFinalDueDate();
				CaseDetails deltaCaseDetails = new CaseDetails();
				String CLIENT_DUE_DATES = "";
				if (oldClientInterim1DueDate != null
						&& oldClientInterim1DueDate.compareTo(newClientInterim1DueDate) != 0) {
					CLIENT_DUE_DATES = "Client Interim 1 DueDate ( " + newClientInterim1DueDate
							+ " ) has been updated for this case";
					this.logger.debug("Client Interim 1 DueDate ( " + newClientInterim1DueDate
							+ " ) has been updated for this case");
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldClientInterim2DueDate != null
						&& oldClientInterim2DueDate.compareTo(newClientInterim2DueDate) != 0) {
					CLIENT_DUE_DATES = "Client Interim 2 DueDate ( " + newClientInterim2DueDate
							+ " ) has been updated for this case";
					this.logger.debug("Client Interim 2 DueDate ( " + newClientInterim2DueDate
							+ " ) has been updated for this case");
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldClientFinalDueDate != null && oldClientFinalDueDate.compareTo(newClientFinalDueDate) != 0) {
					CLIENT_DUE_DATES = "Client Final DueDate( " + newClientFinalDueDate
							+ " ) has been updated for this case";
					this.logger.debug(
							"Client Final DueDate( " + newClientFinalDueDate + " ) has been updated for this case");
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldClientInterim1DueDate != null
						&& oldClientInterim1DueDate.compareTo(newClientInterim1DueDate) != 0
						&& oldClientInterim2DueDate != null
						&& oldClientInterim2DueDate.compareTo(newClientInterim2DueDate) != 0
						&& oldClientFinalDueDate != null
						&& oldClientFinalDueDate.compareTo(newClientFinalDueDate) != 0) {
					CLIENT_DUE_DATES = "Client Interim 1 DueDate ( " + newClientInterim1DueDate
							+ " ) Interim 2 DueDate ( " + newClientInterim2DueDate + " ) Final DueDate( "
							+ newClientFinalDueDate + " ) has been updated for this case";
					this.logger.debug("Client Interim 1 DueDate ( " + newClientInterim1DueDate
							+ " ) Interim 2 DueDate ( " + newClientInterim2DueDate + " ) Final DueDate( "
							+ newClientFinalDueDate + " ) has been updated for this case");
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (deltaCaseDetails.isResearchDueDateFlag()) {
					this.logger.debug("Notification Messages:::::");
					ResourceLocator.self().getNotificationService().createSystemNotification(CLIENT_DUE_DATES,
							CLIENT_DUE_DATES, newCaseDetails.getCaseMgrId(), newCaseDetails.getCrn());
				}
			}

		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	public List<CaseStatus> getCaseStatusMaster() throws CMSException {
		new ArrayList();

		try {
			List<CaseStatus> caseStatus = this.caseDetailsDAO.getCaseStatusMaster();
			return caseStatus;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryDatabaseMasterVO> getAllCountryMasterList() throws CMSException {
		new ArrayList();

		try {
			List<CountryDatabaseMasterVO> allCountryMasterList = this.caseDetailsDAO.getAllCountryMasterList();
			return allCountryMasterList;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public boolean updateCaseStatus(String crn, String userId, String status) throws CMSException {
		boolean isUpdated = false;

		try {
			CaseDetails oldCaseDetails = this.getCaseDetails(crn);
			if (oldCaseDetails != null && oldCaseDetails.getPid() != null) {
				CaseDetails caseDetails = new CaseDetails();
				caseDetails.setCrn(crn);
				caseDetails.setUpdatedBy(userId);
				caseDetails.setCaseStatus(status);
				int count = this.caseDetailsDAO.updateCaseStatus(caseDetails);
				ResourceLocator.self().getTimeTrackerService().stopAllTrackerForCase(crn, userId);
				if (count > 0) {
					HashMap<String, Object> dsValues = new HashMap();
					dsValues.put("CaseStatus", status);
					Session session = ResourceLocator.self().getSBMService().getSession(userId);
					ResourceLocator.self().getSBMService().updateDataSlots(session,
							Long.parseLong(oldCaseDetails.getPid()), dsValues);
					ResourceLocator.self().getSBMService().closeSession(session);
					isUpdated = true;
				}
			}

			return isUpdated;
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<MyOnHoldCaseVO> getOnHoldCase(Map<String, Object> parameterMap) throws CMSException {
		new ArrayList();

		try {
			List<MyOnHoldCaseVO> holdCases = this.caseDetailsDAO.getOnHoldCases(parameterMap);
			return holdCases;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getCountOfOnHoldCases(Map parameterMap) throws CMSException {
		return this.caseDetailsDAO.getCountOfOnHoldCases(parameterMap);
	}

	public CaseDetails getLegacyCaseDetails(String crn) throws CMSException {
		this.logger.debug("In getCaseDetails for legacy ");
		new CaseDetails();

		try {
			CaseDetails caseDetails = this.caseDetailsDAO.getLegacyCaseDetails(crn);
			return caseDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getCrnForPid(long pid) throws CMSException {
		return this.caseDetailsDAO.getCrnForPid(pid);
	}

	public CaseDetails getRecurranceCaseDetails(String crn) throws CMSException {
		this.logger.debug("IN getRecurranceCaseDetails");
		return this.caseDetailsDAO.getRecurranceCaseDetails(crn);
	}

	public void updateRecurranceCaseDetails(CaseDetails caseDetails) throws CMSException {
		this.logger.debug("IN updateRecurranceCaseDetails");
		this.caseDetailsDAO.updateRecurranceCaseDetails(caseDetails);
	}

	public String getCurrentCaseCycle(Session session, String pid) throws CMSException {
		this.logger.debug("pid for getting current case cycle :: " + pid);
		String currentCaseCycle = "";

		try {
			currentCaseCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid),
					"ProcessCycle", session);
			return currentCaseCycle;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getCurrentCaseStatus(Session session, String pid) throws CMSException {
		String currentCaseStatus = "";

		try {
			currentCaseStatus = (String) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid),
					"CaseStatus", session);
			return currentCaseStatus;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateCaseResearchDueDates(String crn, String rInterim1, String rInterim2, String finalDueDate,
			String userId) throws CMSException {
		int updateCount = false;
		this.logger.debug(" Going to update research due dates of case as per the primary team due dates");
		this.logger.debug(" rInterim1 " + rInterim1 + " rInterim2 " + rInterim2 + " finalDueDate " + finalDueDate
				+ " for the crn " + crn);

		try {
			CaseDetails caseDetails = new CaseDetails();
			caseDetails.setCrn(crn);
			if (rInterim1 != null) {
				caseDetails.setrInterim1(new Date(this.sdf.parse(rInterim1).getTime()));
			}

			if (rInterim2 != null) {
				caseDetails.setrInterim2(new Date(this.sdf.parse(rInterim2).getTime()));
			}

			caseDetails.setFinalRDueDate(new Date(this.sdf.parse(finalDueDate).getTime()));
			caseDetails.setUpdatedBy(userId);
			int updateCount = this.caseDetailsDAO.updateCaseResearchDueDates(caseDetails);
			this.logger.debug("number of cases updated  " + updateCount);
			return updateCount;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public void saveSavvionCaseInformation(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap)
			throws CMSException {
		int count = false;
		boolean updateStatus = true;
		boolean processFlag = true;

		try {
			this.logger.debug("going to update the following dataslots " + dsMap);
			CaseDetails oldCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
			if (caseDetails.getCaseStatusId() == 4 && oldCaseDetails.getIsISIS() == 1) {
				updateStatus = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				if (updateStatus) {
					this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
					ClientCaseStatusVO clientCaseStatusVO = new ClientCaseStatusVO();
					clientCaseStatusVO.setCRN(oldCaseDetails.getCrn());
					clientCaseStatusVO.setStatus("CNL");
					clientCaseStatusVO.setFileName("");
					clientCaseStatusVO.setVersion(1.0D);
					clientCaseStatusVO.setExpressCase("False");
					clientCaseStatusVO.setUpdateType("Case");
					processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
				}
			}

			if (updateStatus && processFlag) {
				this.caseDetailsDAO.saveSavvionCaseInformation(caseDetails);
				if (caseDetails.getCaseStatusId() == 4 || caseDetails.getCaseStatusId() == 1) {
					ResourceLocator.self().getTimeTrackerService().stopAllTrackerForCase(caseDetails.getCrn(),
							caseDetails.getUpdatedBy());
				}

				ResourceLocator.self().getFlowService().updateCaseInformationDS(session, oldCaseDetails, caseDetails,
						dsMap);
				CaseDetails newCaseDetails = this.getCaseDetails(caseDetails.getCrn());
				this.logger.debug("Long.parseLong(caseDetails.getPid()) " + Long.parseLong(oldCaseDetails.getPid()));
				this.sendNotification(oldCaseDetails, newCaseDetails, caseDetails.getUpdatedBy());
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setPid(oldCaseDetails.getPid());
				caseHistory.setCRN(oldCaseDetails.getCrn());
				if (caseDetails.getCaseHistoryPerformer() != null && !caseDetails.getCaseHistoryPerformer().isEmpty()) {
					caseHistory.setPerformer(caseDetails.getCaseHistoryPerformer());
				} else {
					caseHistory.setPerformer(caseDetails.getUpdatedBy());
				}

				caseHistory.setTaskName("");
				caseHistory.setTaskStatus("");
				caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
						.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
				ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
						caseHistory, "caseDetails");
			}

		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public void saveSavvionCaseInformation(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap,
			List<RiskProfileVO> riskProfileList, List<RiskProfileVO> riskProfileListWithHBD,
			List<RiskProfileVO> subIndusList, List<RiskAggregationVO> riskAggregationList, String taskName,
			List<TotalRiskAggregationVO> totalRiskAggregationList) throws CMSException {
		int count = false;
		long riskHistoryCount = 0L;
		long riskHistoryCountHBD = 0L;
		boolean updateStatus = true;
		RiskHistory rh = null;
		RiskHistory rhbd = null;
		boolean processFlag = true;

		try {
			this.logger.debug("going to update the following dataslots " + dsMap);
			CaseDetails oldCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
			if (caseDetails.getCaseStatusId() == 4 && oldCaseDetails.getIsISIS() == 1) {
				updateStatus = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				if (updateStatus) {
					this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
					ClientCaseStatusVO clientCaseStatusVO = new ClientCaseStatusVO();
					clientCaseStatusVO.setCRN(oldCaseDetails.getCrn());
					clientCaseStatusVO.setStatus("CNL");
					clientCaseStatusVO.setFileName("");
					clientCaseStatusVO.setVersion(1.0D);
					clientCaseStatusVO.setExpressCase("False");
					clientCaseStatusVO.setUpdateType("Case");
					processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
				}
			}

			if (updateStatus && processFlag) {
				this.caseDetailsDAO.saveSavvionCaseInformation(caseDetails);
				if (caseDetails.getCaseStatusId() == 4 || caseDetails.getCaseStatusId() == 1) {
					ResourceLocator.self().getTimeTrackerService().stopAllTrackerForCase(caseDetails.getCrn(),
							caseDetails.getUpdatedBy());
				}

				ResourceLocator.self().getFlowService().updateCaseInformationDS(session, oldCaseDetails, caseDetails,
						dsMap);
				CaseDetails newCaseDetails = this.getCaseDetails(caseDetails.getCrn());
				this.logger.debug("Long.parseLong(caseDetails.getPid()) " + Long.parseLong(oldCaseDetails.getPid()));
				this.sendNotification(oldCaseDetails, newCaseDetails, caseDetails.getUpdatedBy());
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setPid(oldCaseDetails.getPid());
				caseHistory.setCRN(oldCaseDetails.getCrn());
				if (caseDetails.getCaseHistoryPerformer() != null && !caseDetails.getCaseHistoryPerformer().isEmpty()) {
					caseHistory.setPerformer(caseDetails.getCaseHistoryPerformer());
				} else {
					caseHistory.setPerformer(caseDetails.getUpdatedBy());
				}

				caseHistory.setTaskName("");
				caseHistory.setTaskStatus("");
				caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
						.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
				ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
						caseHistory, "caseDetails");
				long riskCount;
				String riskCodeFlag;
				StringBuffer oldValue;
				StringBuffer newValue;
				ArrayList riskHistoryWithHBDList;
				int historyIndex;
				boolean flag;
				int i;
				RiskProfileVO rVO;
				String riskCode;
				long catId;
				long subId;
				if (riskProfileList.size() <= 0) {
					riskCount = 0L;
				} else {
					riskHistoryWithHBDList = new ArrayList();
					riskCodeFlag = "";
					historyIndex = 0;
					flag = false;
					i = 0;

					while (true) {
						if (i >= riskProfileList.size()) {
							this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
							riskCount = this.riskProfileDAO.updateRiskProfileData(riskProfileList);
							break;
						}

						rVO = (RiskProfileVO) riskProfileList.get(i);
						riskCode = rVO.getRiskCode();
						catId = rVO.getRiskCategoryId();
						subId = 0L;
						subId = rVO.getSubjectId();
						String finalString = riskCode + "#" + catId + "#" + subId;
						if (i != 0 && riskCodeFlag.equals(finalString)) {
							this.logger.debug("else Risk code flag is == riskcode.." + flag);
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
							}

							if (!oldValue.toString().equals(newValue.toString())) {
								if (flag) {
									rh = (RiskHistory) riskHistoryWithHBDList.get(riskHistoryWithHBDList.size() - 1);
									StringBuffer tempValueOld = new StringBuffer(rh.getOldInfo());
									tempValueOld = tempValueOld.append(oldValue);
									StringBuffer tempValueNew = new StringBuffer(rh.getNewInfo());
									tempValueNew = tempValueNew.append(newValue);
									rh.setOldInfo(tempValueOld.toString());
									rh.setNewInfo(tempValueNew.toString());
								} else {
									rh.setOldInfo(oldValue.toString());
									rh.setNewInfo(newValue.toString());
									riskHistoryWithHBDList.add(rh);
								}
							}
						} else {
							this.logger.debug("i==0 or Risk code flag is != riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							rh = new RiskHistory();
							rh.setCRN(rVO.getCRN());
							rh.setRiskCategoryId(rVO.getRiskCategoryId());
							rh.setRiskCode(rVO.getRiskCode());
							rh.setCountryMasterId(0L);
							rh.setSubjectId(rVO.getSubjectId());
							rh.setAction("Update");
							rh.setTask(taskName);
							rh.setUpdatedBy(session.getUser());
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
							}

							this.logger.debug("oldValue.." + oldValue);
							this.logger.debug("newValue.." + newValue);
							flag = false;
							if (!oldValue.toString().equals(newValue.toString())) {
								rh.setOldInfo(oldValue.toString());
								rh.setNewInfo(newValue.toString());
								this.logger.debug("old info.." + rh.getOldInfo());
								this.logger.debug("new info.." + rh.getNewInfo());
								riskHistoryWithHBDList.add(rh);
								flag = true;
								++historyIndex;
							}

							riskCodeFlag = rVO.getRiskCode() + "#" + rVO.getRiskCategoryId() + "#" + rVO.getSubjectId();
						}

						++i;
					}
				}

				this.logger.debug("Risk Profile Updated Records Count::" + riskCount);
				long riskCountWithHBD;
				if (riskProfileListWithHBD.size() <= 0) {
					riskCountWithHBD = 0L;
				} else {
					riskHistoryWithHBDList = new ArrayList();
					riskCodeFlag = "";
					historyIndex = 0;
					flag = false;
					i = 0;

					while (true) {
						if (i >= riskProfileListWithHBD.size()) {
							this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
							riskCountWithHBD = this.riskProfileDAO.updateRiskProfileDataWithHBD(riskProfileListWithHBD);
							break;
						}

						rVO = (RiskProfileVO) riskProfileListWithHBD.get(i);
						riskCode = rVO.getRiskCode();
						catId = rVO.getRiskCategoryId();
						subId = 0L;
						subId = rVO.getSubjectId();
						long countryId = rVO.getCountryId();
						String finalString = riskCode + "#" + catId + "#" + subId + "#" + countryId;
						if (i != 0 && riskCodeFlag.equals(finalString)) {
							this.logger.debug("else Risk code flag is == riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
								this.logger.debug("oldValue if.." + oldValue);
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
								this.logger.debug("newValue if.." + newValue);
							}

							if (!oldValue.toString().equals(newValue.toString())) {
								if (flag) {
									rh = (RiskHistory) riskHistoryWithHBDList.get(riskHistoryWithHBDList.size() - 1);
									StringBuffer tempValueOld = new StringBuffer(rh.getOldInfo());
									tempValueOld = tempValueOld.append(oldValue);
									StringBuffer tempValueNew = new StringBuffer(rh.getNewInfo());
									tempValueNew = tempValueNew.append(newValue);
									rh.setOldInfo(tempValueOld.toString());
									this.logger.debug("rh.getOldInfo().." + rh.getOldInfo());
									rh.setNewInfo(tempValueNew.toString());
									this.logger.debug("rh.getNewInfo().." + rh.getNewInfo());
								} else {
									rh.setOldInfo(oldValue.toString());
									rh.setNewInfo(newValue.toString());
									riskHistoryWithHBDList.add(rh);
								}
							}
						} else {
							this.logger.debug("i==0 or Risk code flag is != riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							rh = new RiskHistory();
							rh.setCRN(rVO.getCRN());
							rh.setRiskCategoryId(rVO.getRiskCategoryId());
							rh.setRiskCode(rVO.getRiskCode());
							rh.setCountryMasterId(rVO.getCountryId());
							rh.setSubjectId(rVO.getSubjectId());
							rh.setAction("Update");
							rh.setTask(taskName);
							rh.setUpdatedBy(session.getUser());
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
								this.logger.debug("oldValue if.." + oldValue);
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
								this.logger.debug("newValue if.." + newValue);
							}

							flag = false;
							if (!oldValue.toString().equals(newValue.toString())) {
								rh.setOldInfo(oldValue.toString());
								rh.setNewInfo(newValue.toString());
								riskHistoryWithHBDList.add(rh);
								flag = true;
								++historyIndex;
							}

							riskCodeFlag = rVO.getRiskCode() + "#" + rVO.getRiskCategoryId() + "#" + rVO.getSubjectId()
									+ "#" + rVO.getCountryId();
						}

						++i;
					}
				}

				if (subIndusList.size() > 0) {
					this.riskProfileDAO.updateSubjectWithIndustryCode(subIndusList);
				}

				this.logger.debug("Risk Profile With HBD Updated Records Count::" + riskCountWithHBD);
				if (riskAggregationList.size() > 0) {
					this.riskProfileDAO.updateRiskAggregationData(riskAggregationList);
				}

				if (totalRiskAggregationList.size() > 0) {
					this.riskProfileDAO.updateTotalRiskAggregationData(totalRiskAggregationList);
				}
			}

		} catch (Exception var44) {
			throw new CMSException(this.logger, var44);
		}
	}

	public long fetchProfileId(List<RiskProfileVO> riskForProfileIDList) throws CMSException {
		try {
			return this.riskProfileDAO.fetchProfileId(riskForProfileIDList);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int fetchTotalAggrId(String crn) throws CMSException {
		try {
			return this.riskProfileDAO.fetchTotalAggrId(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void saveSubjectAggregation(List<SubjectLevelAggregation> subLevelAggregation) throws CMSException {
		try {
			this.riskProfileDAO.saveSubjectAggregation(subLevelAggregation);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public CaseDetails getCaseInfoForPID(String pid) throws CMSException {
		return this.caseDetailsDAO.getCaseInfoForPID(pid);
	}

	public int updateClientFeedback(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			CaseDetails oldCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			int count = this.caseDetailsDAO.updateClientFeedback(caseDetails);
			CaseDetails newCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			CaseHistory caseHistory = new CaseHistory();
			caseHistory.setPid(oldCaseDetails.getPid());
			caseHistory.setCRN(oldCaseDetails.getCrn());
			caseHistory.setPerformer(caseDetails.getCaseHistoryPerformer());
			caseHistory.setTaskName("");
			caseHistory.setTaskStatus("");
			caseHistory.setProcessCycle("Final");
			newCaseDetails.setCaseHistoryPerformer(caseDetails.getCaseHistoryPerformer());
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails, caseHistory,
					"caseDetails");
			return count;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public HashMap<String, String> getPIDsForBulkCRN(List<String> cRNList) throws CMSException {
		return this.caseDetailsDAO.getPIDsForBulkCRN(cRNList);
	}

	public int updateCaseStatusInfo(CaseDetails caseDetails, Session session, HashMap<String, Object> dsMap)
			throws CMSException {
		int count = 0;
		boolean updateStatus = true;
		boolean processFlag = true;

		try {
			this.logger.debug("going to update the following dataslots from case information " + dsMap);
			CaseDetails oldCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			int tempCaseStatusId = oldCaseDetails.getCaseStatusId();
			String tempCaseStatus = oldCaseDetails.getCaseStatus();
			oldCaseDetails.setCaseStatus(caseDetails.getCaseStatus());
			oldCaseDetails.setCaseStatusId(caseDetails.getCaseStatusId());
			oldCaseDetails.setUpdatedBy(caseDetails.getUpdatedBy());
			oldCaseDetails.setCaseHistoryPerformer(caseDetails.getCaseHistoryPerformer());
			oldCaseDetails.setOfficeId(oldCaseDetails.getOfficeName());
			this.logger.debug(" is recurrence case  " + oldCaseDetails.getIsRecurCase());
			if (oldCaseDetails.getCaseStatusId() == 4 && oldCaseDetails.getIsISIS() == 1) {
				updateStatus = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				if (updateStatus) {
					ClientCaseStatusVO clientCaseStatusVO = new ClientCaseStatusVO();
					clientCaseStatusVO.setCRN(oldCaseDetails.getCrn());
					clientCaseStatusVO.setStatus("CNL");
					clientCaseStatusVO.setFileName("");
					clientCaseStatusVO.setVersion(1.0D);
					clientCaseStatusVO.setExpressCase("False");
					clientCaseStatusVO.setUpdateType("Case");
					processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
				}
			}

			if (updateStatus && processFlag) {
				count = this.caseDetailsDAO.saveCaseInformation(oldCaseDetails);
				CaseDetails newCaseDetailsCaseHistory = this.getCaseDetails(caseDetails.getCrn());
				newCaseDetailsCaseHistory.setUpdatedBy(caseDetails.getUpdatedBy());
				newCaseDetailsCaseHistory.setCaseHistoryPerformer(caseDetails.getCaseHistoryPerformer());
				if (oldCaseDetails.getCaseStatusId() == 4 || oldCaseDetails.getCaseStatusId() == 1) {
					ResourceLocator.self().getTimeTrackerService().stopAllTrackerForCase(oldCaseDetails.getCrn(),
							oldCaseDetails.getUpdatedBy());
				}

				ResourceLocator.self().getFlowService().updateCaseInformationDS(session, oldCaseDetails, oldCaseDetails,
						dsMap);
				this.logger.debug("Long.parseLong(caseDetails.getPid()) " + Long.parseLong(oldCaseDetails.getPid()));
				oldCaseDetails.setCaseStatusId(tempCaseStatusId);
				oldCaseDetails.setCaseStatus(tempCaseStatus);
				this.sendNotification(oldCaseDetails, newCaseDetailsCaseHistory, oldCaseDetails.getUpdatedBy());
				CaseHistory caseHistory = new CaseHistory();
				caseHistory.setPid(oldCaseDetails.getPid());
				caseHistory.setCRN(oldCaseDetails.getCrn());
				caseHistory.setPerformer(oldCaseDetails.getCaseHistoryPerformer());
				caseHistory.setTaskName("");
				caseHistory.setTaskStatus("");
				caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
						.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
				oldCaseDetails.setCaseHistoryPerformer(oldCaseDetails.getCaseHistoryPerformer());
				ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetailsCaseHistory,
						caseHistory, "caseDetails");
			}

			return count;
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	public void isisCancelCaseNotification(CaseDetails oldCaseDetails, CaseDetails newCaseDetails, String userId)
			throws CMSException {
		try {
			List<UserMasterVO> sendToUserList = ResourceLocator.self().getNotificationService()
					.getAssignedUsers(newCaseDetails.getCrn(), userId);
			List<String> userList = new ArrayList();
			Iterator iterator = sendToUserList.iterator();

			while (iterator.hasNext()) {
				UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
				userList.add(userMasterVO.getUsername());
			}

			if (oldCaseDetails.getCaseStatusId() != 4 && newCaseDetails.getCaseStatusId() == 4) {
				List<String> cancelledCaseUserList = new ArrayList();
				cancelledCaseUserList.addAll(userList);
				if (oldCaseDetails.getIsISIS() != 1) {
					cancelledCaseUserList.add(oldCaseDetails.getCaseCreatorId());
				}

				List<String> researchUser = this.caseDetailsDAO.getResearchHeadsForCase(newCaseDetails.getCrn());
				cancelledCaseUserList.addAll(researchUser);
				List<UserMasterVO> financeUserList = ResourceLocator.self().getUserService().getUsersForRole("R3");
				Iterator iterator = financeUserList.iterator();

				while (iterator.hasNext()) {
					UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
					cancelledCaseUserList.add(userMasterVO.getUserID());
				}

				ResourceLocator.self().getNotificationService().createSystemNotification("The case is now CANCELLED.",
						"The case is now CANCELLED.", cancelledCaseUserList, newCaseDetails.getCrn());
			}

		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public void getMailBody(String crn, String isISIS, String isAtlas, String isBulk) throws CMSException {
		boolean isMailSent = false;
		String emailBody = "";
		String clientCode = crn.split("\\\\")[1];
		Map<String, String> dataMap = new HashMap();
		this.logger.debug("client Code" + clientCode);
		dataMap.put("crn", crn);
		dataMap.put("clientCode", clientCode);
		dataMap.put("isIsis", isISIS);
		dataMap.put("isAtlas", isAtlas);
		dataMap.put("isBulk", isBulk);
		EmailTemplateVO emailTemplateVO = this.caseDetailsDAO.getMailBody(dataMap);

		try {
			if (emailTemplateVO != null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				try {
					factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				} catch (ParserConfigurationException var22) {
					;
				}

				try {
					factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				} catch (ParserConfigurationException var21) {
					;
				}

				try {
					factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				} catch (ParserConfigurationException var20) {
					;
				}

				factory.setXIncludeAware(false);
				factory.setExpandEntityReferences(false);
				factory.setNamespaceAware(true);
				DocumentBuilder db = factory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(emailTemplateVO.getEmailBody()));
				Document doc = db.parse(is);
				NodeList nodes = doc.getElementsByTagName("mailBody");
				Element element = (Element) nodes.item(0);
				NodeList subject = element.getElementsByTagName("subject");
				Element line = (Element) subject.item(0);
				String emailSubject = getCharacterDataFromElement(line);
				this.logger.debug("Subject:::" + emailSubject);
				NodeList body = element.getElementsByTagName("body");
				line = (Element) body.item(0);
				emailBody = getCharacterDataFromElement(line);
				this.logger.debug("Body::::" + emailBody);
				emailTemplateVO.setClientCode(clientCode);
				isMailSent = this.setEmailContent(emailTemplateVO, emailSubject, emailBody);
				emailTemplateVO.setFinalEmailBody(emailBodyPrepared);
				if (isMailSent) {
					emailTemplateVO.setIsEmailSent("1");
				} else {
					emailTemplateVO.setIsEmailSent("0");
				}

				this.caseDetailsDAO.insertMailLogs(emailTemplateVO);
			}

		} catch (Exception var23) {
			emailTemplateVO.setFinalEmailBody(emailBodyPrepared);
			emailTemplateVO.setIsEmailSent("0");
			emailTemplateVO.setClientCode(clientCode);
			this.caseDetailsDAO.insertMailLogs(emailTemplateVO);
			throw new CMSException(this.logger, var23);
		}
	}

	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		} else {
			return "";
		}
	}

	private boolean setEmailContent(EmailTemplateVO emailTemplateVO, String emailSubject, String emailBody)
			throws Exception {
		String bccMailList = "";
		emailBodyPrepared = "";
		SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat sm1 = new SimpleDateFormat("yyyy-MM-dd");
		emailSubject = emailSubject.replace("@CRN", emailTemplateVO.getCrn());
		emailBody = emailBody.replace("@NEWLINE", "<br />");
		emailBody = emailBody.replace("@CRN", emailTemplateVO.getCrn());
		emailBody = emailBody.replace("@CaseManager", emailTemplateVO.getCaseManager());
		if (emailTemplateVO.getInterim1CDD() != null && emailTemplateVO.getInterim1CDD().length() > 0) {
			emailBody = emailBody.replace("@Interim1CDD", sm.format(sm1.parse(emailTemplateVO.getInterim1CDD())));
		} else {
			emailBody = emailBody.replace("@Interim1CDD", "");
		}

		if (emailTemplateVO.getInterim2CDD() != null && emailTemplateVO.getInterim2CDD().length() > 0) {
			emailBody = emailBody.replace("@Interim2CDD", sm.format(sm1.parse(emailTemplateVO.getInterim2CDD())));
		} else {
			emailBody = emailBody.replace("@Interim2CDD", "");
		}

		if (emailTemplateVO.getClientReferenceNumber() != null) {
			emailBody = emailBody.replace("@ClientReferenceNumber", emailTemplateVO.getClientReferenceNumber());
		} else {
			emailBody = emailBody.replace("@ClientReferenceNumber", "");
		}

		emailBody = emailBody.replace("@FinalCDD", sm.format(sm1.parse(emailTemplateVO.getFinalCDD())));
		this.logger.debug("finaDD::::" + sm.format(sm1.parse(emailTemplateVO.getFinalCDD())));
		new SubjectDetails();
		StringBuffer subjectTable = new StringBuffer();
		subjectTable.append(
				"<table border=1 cellspacing='0' cellpadding='0'><tr><th>Subject Name</th><th>Entity Type</th><th>Subject Country</th></tr>");
		List<SubjectDetails> subjectDeatils = emailTemplateVO.getSubjectDetailsList();
		Iterator itr = subjectDeatils.iterator();

		while (itr.hasNext()) {
			SubjectDetails subjectVO = (SubjectDetails) itr.next();
			subjectTable.append("<tr><td>" + subjectVO.getSubjectName() + "</td><td>" + subjectVO.getEntityName()
					+ "</td><td>" + subjectVO.getCountryName() + "</td></tr>");
		}

		subjectTable = subjectTable.append("</table>");
		emailBody = emailBody.replace("@SubjectTable", subjectTable);
		emailBodyPrepared = emailBody;
		String host = propertyReader.getHostEmailPrepayment();
		String from = propertyReader.getEmailFrom();
		if (emailTemplateVO.getEmailBCC() != null && emailTemplateVO.getDefaultMailingList() != null) {
			bccMailList = emailTemplateVO.getEmailBCC() + "," + emailTemplateVO.getDefaultMailingList();
		} else if (emailTemplateVO.getEmailBCC() != null) {
			bccMailList = emailTemplateVO.getEmailBCC();
		} else if (emailTemplateVO.getDefaultMailingList() != null) {
			bccMailList = emailTemplateVO.getDefaultMailingList();
		}

		String[] bccArray;
		if (!bccMailList.isEmpty() && bccMailList.length() > 0) {
			bccArray = bccMailList.split(",");
		} else {
			bccArray = new String[0];
		}

		String[] ccArray;
		if (emailTemplateVO.getEmailCC() != null && emailTemplateVO.getEmailCC().length() > 0) {
			ccArray = emailTemplateVO.getEmailCC().split(",");
		} else {
			ccArray = new String[0];
		}

		String[] toArray;
		if (emailTemplateVO.getEmailTO() != null && emailTemplateVO.getEmailTO().length() > 0) {
			toArray = emailTemplateVO.getEmailTO().split(",");
		} else {
			toArray = new String[0];
		}

		InternetAddress[] bccEmail = this.itearteArray(bccArray);
		InternetAddress[] ccEmail = this.itearteArray(ccArray);
		InternetAddress[] toEmail = this.itearteArray(toArray);
		this.logger.debug("to:::" + toArray.length);
		this.logger.debug("cc:::" + ccArray.length);
		this.logger.debug("bcc:::" + bccArray.length);
		return ResourceLocator.self().getMailService().sendEmailOnCaseCreation(emailBody, emailSubject, host, toEmail,
				ccEmail, bccEmail, from);
	}

	private InternetAddress[] itearteArray(String[] array) throws Exception {
		InternetAddress[] address = new InternetAddress[array.length];

		for (int i = 0; i < array.length; ++i) {
			address[i] = new InternetAddress(array[i]);
		}

		return address;
	}

	public void saveAfterCompletionSavvionCaseInformation(CaseDetails caseDetails, Session session,
			HashMap<String, Object> dsMap, List<RiskProfileVO> riskProfileList,
			List<RiskProfileVO> riskProfileListWithHBD, List<RiskProfileVO> subIndusList,
			List<RiskAggregationVO> riskAggregationList, String taskName,
			List<TotalRiskAggregationVO> totalRiskAggregationList, List<SubjectLevelAggregation> subjectAggregationList)
			throws CMSException {
		int count = false;
		long riskHistoryCount = 0L;
		long riskHistoryCountHBD = 0L;
		boolean updateStatus = true;
		RiskHistory rh = null;
		RiskHistory rhbd = null;
		boolean processFlag = true;

		try {
			this.logger.debug("going to update the following dataslots " + dsMap);
			CaseDetails oldCaseDetails = this.getCaseDetails(caseDetails.getCrn());
			this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
			if (caseDetails.getCaseStatusId() == 4 && oldCaseDetails.getIsISIS() == 1) {
				updateStatus = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				if (updateStatus) {
					this.logger.debug(" is recurrence case  " + caseDetails.getIsRecurCase());
					ClientCaseStatusVO clientCaseStatusVO = new ClientCaseStatusVO();
					clientCaseStatusVO.setCRN(oldCaseDetails.getCrn());
					clientCaseStatusVO.setStatus("CNL");
					clientCaseStatusVO.setFileName("");
					clientCaseStatusVO.setVersion(1.0D);
					clientCaseStatusVO.setExpressCase("False");
					clientCaseStatusVO.setUpdateType("Case");
					processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
				}
			}

			if (updateStatus && processFlag) {
				long riskCount;
				String riskCodeFlag;
				StringBuffer oldValue;
				StringBuffer newValue;
				int historyIndex;
				boolean flag;
				int i;
				RiskProfileVO rVO;
				String riskCode;
				long catId;
				long subId;
				ArrayList riskHistoryWithHBDList;
				if (riskProfileList.size() <= 0) {
					riskCount = 0L;
				} else {
					riskHistoryWithHBDList = new ArrayList();
					riskCodeFlag = "";
					historyIndex = 0;
					flag = false;
					i = 0;

					while (true) {
						if (i >= riskProfileList.size()) {
							this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
							riskCount = this.riskProfileDAO.updateRiskProfileData(riskProfileList);
							break;
						}

						rVO = (RiskProfileVO) riskProfileList.get(i);
						riskCode = rVO.getRiskCode();
						catId = rVO.getRiskCategoryId();
						subId = 0L;
						subId = rVO.getSubjectId();
						String finalString = riskCode + "#" + catId + "#" + subId;
						if (i != 0 && riskCodeFlag.equals(finalString)) {
							this.logger.debug("else Risk code flag is == riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
							}

							if (!oldValue.toString().equals(newValue.toString())) {
								if (flag) {
									rh = (RiskHistory) riskHistoryWithHBDList.get(riskHistoryWithHBDList.size() - 1);
									StringBuffer tempValueOld = new StringBuffer(rh.getOldInfo());
									tempValueOld = tempValueOld.append(oldValue);
									StringBuffer tempValueNew = new StringBuffer(rh.getNewInfo());
									tempValueNew = tempValueNew.append(newValue);
									rh.setOldInfo(tempValueOld.toString());
									rh.setNewInfo(tempValueNew.toString());
								} else {
									rh.setOldInfo(oldValue.toString());
									rh.setNewInfo(newValue.toString());
									riskHistoryWithHBDList.add(rh);
								}
							}
						} else {
							this.logger.debug("i==0 or Risk code flag is != riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							rh = new RiskHistory();
							rh.setCRN(rVO.getCRN());
							rh.setRiskCategoryId(rVO.getRiskCategoryId());
							rh.setRiskCode(rVO.getRiskCode());
							rh.setCountryMasterId(0L);
							rh.setSubjectId(rVO.getSubjectId());
							rh.setAction("Update");
							rh.setTask(taskName);
							rh.setUpdatedBy(session.getUser());
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
							}

							flag = false;
							if (!oldValue.toString().equals(newValue.toString())) {
								rh.setOldInfo(oldValue.toString());
								rh.setNewInfo(newValue.toString());
								riskHistoryWithHBDList.add(rh);
								flag = true;
								++historyIndex;
							}

							riskCodeFlag = rVO.getRiskCode() + "#" + rVO.getRiskCategoryId() + "#" + rVO.getSubjectId();
						}

						++i;
					}
				}

				this.logger.debug("Risk Profile Updated Records Count::" + riskCount);
				long riskCountWithHBD;
				if (riskProfileListWithHBD.size() <= 0) {
					riskCountWithHBD = 0L;
				} else {
					riskHistoryWithHBDList = new ArrayList();
					riskCodeFlag = "";
					historyIndex = 0;
					flag = false;
					i = 0;

					while (true) {
						if (i >= riskProfileListWithHBD.size()) {
							this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
							riskCountWithHBD = this.riskProfileDAO.updateRiskProfileDataWithHBD(riskProfileListWithHBD);
							break;
						}

						rVO = (RiskProfileVO) riskProfileListWithHBD.get(i);
						riskCode = rVO.getRiskCode();
						catId = rVO.getRiskCategoryId();
						subId = 0L;
						subId = rVO.getSubjectId();
						long countryId = rVO.getCountryId();
						String finalString = riskCode + "#" + catId + "#" + subId + "#" + countryId;
						if (i != 0 && riskCodeFlag.equals(finalString)) {
							this.logger.debug("else Risk code flag is == riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
								this.logger.debug("oldValue if.." + oldValue);
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
								this.logger.debug("newValue if.." + newValue);
							}

							if (!oldValue.toString().equals(newValue.toString())) {
								if (flag) {
									rh = (RiskHistory) riskHistoryWithHBDList.get(riskHistoryWithHBDList.size() - 1);
									StringBuffer tempValueOld = new StringBuffer(rh.getOldInfo());
									tempValueOld = tempValueOld.append(oldValue);
									StringBuffer tempValueNew = new StringBuffer(rh.getNewInfo());
									tempValueNew = tempValueNew.append(newValue);
									rh.setOldInfo(tempValueOld.toString());
									this.logger.debug("rh.getOldInfo().." + rh.getOldInfo());
									rh.setNewInfo(tempValueNew.toString());
									this.logger.debug("rh.getNewInfo().." + rh.getNewInfo());
								} else {
									rh.setOldInfo(oldValue.toString());
									rh.setNewInfo(newValue.toString());
									riskHistoryWithHBDList.add(rh);
								}
							}
						} else {
							this.logger.debug("i==0 or Risk code flag is != riskcode");
							oldValue = new StringBuffer("");
							newValue = new StringBuffer("");
							rh = new RiskHistory();
							rh.setCRN(rVO.getCRN());
							rh.setRiskCategoryId(rVO.getRiskCategoryId());
							rh.setRiskCode(rVO.getRiskCode());
							rh.setCountryMasterId(rVO.getCountryId());
							rh.setSubjectId(rVO.getSubjectId());
							rh.setAction("Update");
							rh.setTask(taskName);
							rh.setUpdatedBy(session.getUser());
							if (rVO.getAttributeName() != null && rVO.getOldAttrValue() != null) {
								oldValue = oldValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getOldAttrValue() + "<br />");
								this.logger.debug("oldValue if.." + oldValue);
							}

							if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
								newValue = newValue.append("<b>" + rVO.getAttributeName() + "</b>" + ":"
										+ rVO.getNewattrValue() + "<br />");
								this.logger.debug("newValue if.." + newValue);
							}

							flag = false;
							if (!oldValue.toString().equals(newValue.toString())) {
								rh.setOldInfo(oldValue.toString());
								rh.setNewInfo(newValue.toString());
								riskHistoryWithHBDList.add(rh);
								flag = true;
								++historyIndex;
							}

							riskCodeFlag = rVO.getRiskCode() + "#" + rVO.getRiskCategoryId() + "#" + rVO.getSubjectId()
									+ "#" + rVO.getCountryId();
						}

						++i;
					}
				}

				if (subIndusList.size() > 0) {
					this.riskProfileDAO.updateSubjectWithIndustryCode(subIndusList);
				}

				this.logger.debug("Risk Profile With HBD Updated Records Count::" + riskCountWithHBD);
				if (riskAggregationList.size() > 0) {
					this.riskProfileDAO.updateRiskAggregationData(riskAggregationList);
				}

				if (totalRiskAggregationList.size() > 0) {
					this.riskProfileDAO.updateTotalRiskAggregationData(totalRiskAggregationList);
				}

				if (subjectAggregationList.size() > 0) {
					this.riskProfileDAO.saveSubjectAggregation(subjectAggregationList);
				}
			}

		} catch (Exception var43) {
			throw new CMSException(this.logger, var43);
		}
	}
}