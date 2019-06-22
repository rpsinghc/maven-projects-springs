package com.worldcheck.atlas.bl.subject;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.ISubjectManager;
import com.worldcheck.atlas.bl.subject.SubjectManager.1;
import com.worldcheck.atlas.dao.subject.SubjectDAO;
import com.worldcheck.atlas.dao.task.invoice.InvoiceManagementDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.util.WebServicePropertyReaderUtil;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientSubjectVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.IDBUtilis;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.DeleteSubjectLevelRiskVO;
import com.worldcheck.atlas.vo.SubjectColorVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.SubjectLevelRiskProfileDetailsVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import com.worldcheck.atlas.vo.report.TeamDetailsVO;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.collections.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;

public class SubjectManager implements ISubjectManager {
	private SubjectDAO subjectDAO;
	private InvoiceManagementDAO invoiceMgmtDao;
	private String reString = "reString";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.subject.SubjectManager");
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;
	private IDBUtilis idbUtilis;
	String isDeleteSubject = "false";

	public void setInvoiceMgmtDao(InvoiceManagementDAO invoiceMgmtDao) {
		this.invoiceMgmtDao = invoiceMgmtDao;
	}

	public void setIdbUtilis(IDBUtilis idbUtilis) {
		this.idbUtilis = idbUtilis;
	}

	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public CaseDetails getSubjectListForCase(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getSubjectListForCase method of SubjectManager class");
		List subjectListWithReNames = new ArrayList();
		new ArrayList();
		CaseDetails newCaseDetails = new CaseDetails();
		new CaseDetails();

		try {
			List subjectList = new ArrayList();
			this.logger.debug("crn is " + crn);
			CaseDetails caseDetails = this.subjectDAO.getSubjectListForCRN(crn, sortColumnName, start, limit, sortType);
			if (caseDetails != null && caseDetails.getSubjectList() != null) {
				subjectList = caseDetails.getSubjectList();
				this.logger.debug("subject list size for this case is::::" + ((List) subjectList).size());
			}

			if (subjectList != null && ((List) subjectList).size() > 0) {
				Map reMap = this.subjectDAO.getReMap();
				Iterator iterator = ((List) subjectList).iterator();

				while (iterator.hasNext()) {
					SubjectDetails subjectDetail = (SubjectDetails) iterator.next();
					this.logger.debug("subject name::" + subjectDetail.getSubjectName());
					new ArrayList();
					subjectDetail.setReNames(this.reNameIDMap(subjectDetail.getReIds(), reMap));
					subjectListWithReNames.add(subjectDetail);
					this.logger.debug("subjectListWithReNames size" + subjectListWithReNames.size());
				}
			}

			newCaseDetails.setSubjectList(subjectListWithReNames);
			return caseDetails;
		} catch (NullPointerException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public CaseDetails getSubjectListForCase(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectListForCase method of SubjectManager class");
		List subjectListWithReNames = new ArrayList();
		CaseDetails newCaseDetails = new CaseDetails();
		new CaseDetails();

		try {
			List subjectList = new ArrayList();
			this.logger.debug("crn is " + crn);
			CaseDetails caseDetails = this.subjectDAO.getSubjectListForCRN(crn);
			if (caseDetails != null && caseDetails.getSubjectList() != null) {
				subjectList = caseDetails.getSubjectList();
				this.logger.debug("subject list size for this case is::::" + ((List) subjectList).size());
			}

			if (subjectList != null && ((List) subjectList).size() > 0) {
				Map reMap = this.subjectDAO.getReMap();
				Iterator iterator = ((List) subjectList).iterator();

				while (iterator.hasNext()) {
					SubjectDetails subjectDetail = (SubjectDetails) iterator.next();
					this.logger.debug("subject name::" + subjectDetail.getSubjectName());
					new ArrayList();
					subjectDetail.setReNames(this.reNameIDMap(subjectDetail.getReIds(), reMap));
					subjectListWithReNames.add(subjectDetail);
					this.logger.debug("subjectListWithReNames size" + subjectListWithReNames.size());
				}
			}

			newCaseDetails.setSubjectList(subjectListWithReNames);
			return caseDetails;
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public SubjectDetails getSubjectColorDetails(String crn, int subjectId) throws CMSException {
		this.logger.debug("Inside getSubjectListForCase method of SubjectManager class");
		new SubjectDetails();

		try {
			SubjectDetails subjectColor = this.subjectDAO.getSubjectColorDetails(crn, subjectId);
			return subjectColor;
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getCaseSubjectsIndustryList(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getCaseSubjectsIndustryList method of SubjectManager class");
		this.logger.debug("crn is:::" + crn);
		new ArrayList();
		List subjectIndustryList = this.subjectDAO.getCaseSubjetcIndustry(crn, sortColumnName, start, limit, sortType);
		List newSubjectIndustryList = new ArrayList();
		Iterator iterator = subjectIndustryList.iterator();

		while (iterator.hasNext()) {
			SubjectDetails newSubjectDetails = new SubjectDetails();
			SubjectDetails object = (SubjectDetails) iterator.next();
			newSubjectDetails.setSubjectId(object.getSubjectId());
			newSubjectDetails.setSubjectName(object.getSubjectName());
			newSubjectDetails.setIndustryCode(object.getIndustryCode().trim());
			IndustryMasterVO newIndustryMasterVO = new IndustryMasterVO();
			newIndustryMasterVO.setIndustry(object.getIndVO().getIndustry().trim());
			newIndustryMasterVO.setIndustryCode(object.getIndVO().getIndustryCode().trim());
			newSubjectDetails.setIndVO(newIndustryMasterVO);
			newSubjectIndustryList.add(newSubjectDetails);
		}

		return newSubjectIndustryList;
	}

	public SubjectDetails deleteSubjectsForCase(String subjectIDString, String subNameString, String crn,
			String currentUser, String isBIDelete, Session session, String caseHistoryPerformer) throws CMSException {
		this.logger.debug("Inside deleteSubjectsForCase method of SubjectManager class");
		this.logger.debug("subjectIDString is:::::" + subjectIDString);
		List<String> subjectIDStringList = StringUtils.commaSeparatedStringToList(subjectIDString);
		this.logger.debug("isBIDelete value for delete subject is::::" + isBIDelete);
		CaseDetails oldCaseDetails = null;
		SubjectDetails subjectDetails = ResourceLocator.self().getSubjectService().editSubjectForCase(subjectIDString);
		subjectDetails.setCrn(crn);
		SubjectDetails deleteSubjectDetails = new SubjectDetails();
		int deleteSubjectCount = 0;
		int deleteSubjectRisksCount = 0;
		int deleteCntryBrkDownRiskDetails = 0;
		float slBudget = subjectDetails.getSlBudget();
		this.logger.debug("SlBudget for the subject ID" + subjectIDString + "is" + slBudget);
		boolean pingSuccessful = true;
		boolean processFlag = true;
		this.logger.debug("isis flag is::::::" + subjectDetails.getIsisCase());
		this.logger.debug("isis subject id is:::" + subjectDetails.getIsisSubjectId());
		if (subjectDetails.getIsisCase() == 1) {
			pingSuccessful = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
			this.logger.debug("pingSuccessful :: " + pingSuccessful);
			if (!pingSuccessful) {
				deleteSubjectDetails.setUpdateSubjectCount(-1);
				return deleteSubjectDetails;
			}

			ClientSubjectVO clientSubjectVO = this.setISISVOForDeleteSubject(subjectDetails);
			Map resultMap = ResourceLocator.self().getAtlasWebServiceClient().updateSubject(clientSubjectVO);
			this.logger.debug("resultMap value is:::::::" + resultMap);
			int successFlag = (Integer) resultMap.get("successFlag");
			int communicationErrorFlag = (Integer) resultMap.get("communicationErrorFlag");
			String message = (String) resultMap.get("message");
			deleteSubjectDetails.setIsisErrorMessge(message);
			this.logger.debug("successFlag value is::::::" + successFlag);
			this.logger.debug("communicationErrorFlag value is::::" + communicationErrorFlag);
			if (successFlag == 1) {
				processFlag = true;
			} else {
				processFlag = false;
			}
		}

		this.logger.debug("processFlag :: " + processFlag);
		if (pingSuccessful && processFlag) {
			if (isBIDelete.equalsIgnoreCase("true")) {
				oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService().deleteBITeamForCase(crn);
				this.logger.debug("deleteBiTeamResult is.." + oldCaseDetails);
			}

			long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(subjectDetails.getCrn());
			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					session);
			Map reMap = this.subjectDAO.getReMap();
			subjectDetails.setReNames(this.reNameIDMap(subjectDetails.getReIds(), reMap));
			CaseHistory caseHistoryOtherParam = new CaseHistory();
			caseHistoryOtherParam.setCRN(subjectDetails.getCrn());
			caseHistoryOtherParam.setPid(Long.toString(piid));
			caseHistoryOtherParam.setProcessCycle(processCycle);
			if (caseHistoryPerformer.equalsIgnoreCase("")) {
				caseHistoryOtherParam.setPerformer(currentUser);
			} else {
				caseHistoryOtherParam.setPerformer(caseHistoryPerformer);
			}

			CaseDetails caseHistoryOldCaseDetails = new CaseDetails();
			subjectDetails.setCountryName(subjectDetails.getCountryVO().getCountry());
			List caseHistoryOldSubjectList = new ArrayList();
			caseHistoryOldSubjectList.add(subjectDetails);
			caseHistoryOldCaseDetails.setSubjectList(caseHistoryOldSubjectList);
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(caseHistoryOldCaseDetails, new CaseDetails(),
					caseHistoryOtherParam, "Subject");
			HashMap<String, CaseDetails> deleteMap = ResourceLocator.self().getOfficeAssignmentService()
					.deleteOfficeDetailsForSubject(subjectDetails);
			boolean isOfficeAssignmentDone = ResourceLocator.self().getOfficeAssignmentService()
					.checkOfficeAssignmentStatus(crn);
			this.logger.debug("isOfficeAssignmentDone value for delete subject is:::::" + isOfficeAssignmentDone);
			if (isOfficeAssignmentDone) {
				this.logger.debug("going to call flow controller for delete subject...");
				if (oldCaseDetails == null) {
					oldCaseDetails = (CaseDetails) deleteMap.get("oldCaseDetails");
				}

				List subjectList = new ArrayList();
				subjectList.add(subjectDetails);
				oldCaseDetails.setSubjectList(subjectList);
				CaseDetails newCaseDetails = (CaseDetails) deleteMap.get("newCaseDetails");
				newCaseDetails.setUpdatedBy(currentUser);
				ResourceLocator.self().getFlowService().updateDSAndPushFlow(session, oldCaseDetails, newCaseDetails,
						"DeleteSubject");
			}

			List<DeleteSubjectLevelRiskVO> entriesNeedtoBeDeletedList = this.subjectDAO
					.getRiskDetailsNeedToBeDelete(subjectIDStringList);
			List<DeleteSubjectLevelRiskVO> cntryBrkDownEntriesNeedtoBeDeletedList = this.subjectDAO
					.getCntryBrkDownRiskDetailsNeedToBeDelete(subjectIDStringList);
			SubjectLevelRiskProfileDetailsVO caseDetailsForProfile = this.subjectDAO.getCaseDetailsForRiskProfile(crn);
			if (cntryBrkDownEntriesNeedtoBeDeletedList.size() > 0) {
				deleteCntryBrkDownRiskDetails = this.subjectDAO
						.deleteSubjectLevelRisksCntryBrkDown(cntryBrkDownEntriesNeedtoBeDeletedList);
			}

			if (deleteCntryBrkDownRiskDetails > 0) {
				this.subjectDAO
						.insertDeletedSubjectLevelCntryBrkDownRisksHistory(cntryBrkDownEntriesNeedtoBeDeletedList);
			}

			if (entriesNeedtoBeDeletedList.size() > 0) {
				deleteSubjectRisksCount = this.subjectDAO.deleteRiskDetailsAssociatedToSubjects(subjectIDStringList);
			}

			if (deleteSubjectRisksCount > 0) {
				this.subjectDAO.insertDeletedSubjectLevelRisksHistory(entriesNeedtoBeDeletedList);
			}

			this.subjectDAO.deleteCaseLevelRiskCountryBreakDown(caseDetailsForProfile, crn, subjectIDStringList);
			this.subjectDAO.deleteCaseLevelRisk(caseDetailsForProfile, crn, subjectIDStringList);
			this.subjectDAO.deleteCaseLevelRiskAggregation(caseDetailsForProfile, crn, subjectIDStringList);
			deleteSubjectCount = this.subjectDAO.deleteSubjectDetail(subjectIDStringList);
			deleteSubjectDetails.setUpdateSubjectCount(deleteSubjectCount);
			if (slBudget > 0.0F) {
				this.isDeleteSubject = "true";
				this.updateCaseFee(crn, String.valueOf(slBudget), this.isDeleteSubject);
			}

			String subjectNotificationMessage = "Subject(s) deleted from the case(0). Deleted Subject(s) :".replace("0",
					crn) + subNameString + ".";
			this.logger.debug("subjectNotificationMessage is::::::" + subjectNotificationMessage);
			ResourceLocator.self().getNotificationService().createSystemNotification(subjectNotificationMessage,
					subjectNotificationMessage, this.getNotificationUsersForSubject(crn, currentUser), crn);
		}

		this.logger.debug("deleteSubjectCount value is :: " + deleteSubjectCount);
		return deleteSubjectDetails;
	}

	private ClientSubjectVO setISISVOForDeleteSubject(SubjectDetails subjectDetails) {
		ClientSubjectVO clientSubjectVO = new ClientSubjectVO();
		clientSubjectVO.setAtlasSubjectID(subjectDetails.getSubjectId());
		clientSubjectVO.setCRN(subjectDetails.getCrn());
		clientSubjectVO.setPrimary(subjectDetails.isPrimarySub());
		clientSubjectVO.setSubjectID(subjectDetails.getIsisSubjectId());
		clientSubjectVO.setUpdateType("Delete");
		return clientSubjectVO;
	}

	public SubjectDetails getSubjectDetailForSubId(int subjectId) throws CMSException {
		this.logger.debug("Inside getSubjectDetailForSubId method of SubjectManager class");
		this.logger.debug("subjectId is::::" + subjectId);
		SubjectDetails subjectDetail = this.subjectDAO.getSubjectDetailsForSubID(subjectId);
		return subjectDetail;
	}

	public Map getSubIndMapForCase(String crn) throws CMSException {
		this.logger.debug("Inside getSubIndMapForCase method of SubjectManager class");
		this.logger.debug("crn is::::" + crn);
		Map subIndMap = this.subjectDAO.getSubjectIndForSubjects(crn);
		return subIndMap;
	}

	public List getCountryMaster() throws CMSException {
		this.logger.debug("Inside getCountryMaster method of SubjectManager class");
		List countryList = ResourceLocator.self().getCacheService().getCacheItemsList("COUNTRY_MASTER");
		return countryList;
	}

	public List getREElements(String entityType, String subjectEntity, String crn, String subjectID,
			String isSubjLevelSubRptReq, String formAction, String pSubReportTypeId) throws CMSException {
		this.logger.debug("Inside getREElements method of SubjectManager class");
		this.logger
				.debug("entityType is::::" + entityType + ":::::crn is::::" + crn + ":::::subjectID::::" + subjectID);
		List resForEntity = this.subjectDAO.getREs(entityType);
		new ArrayList();
		String resForSubject = null;
		boolean isSubjLevelSubRptReqFlag = false;
		if (!isSubjLevelSubRptReq.trim().equals("1")) {
			resForSubject = this.subjectDAO.getReListForSub(subjectID);
		} else if (!formAction.trim().equalsIgnoreCase("load")) {
			isSubjLevelSubRptReqFlag = true;
		} else {
			resForSubject = this.subjectDAO.getReListForSub(subjectID);
		}

		if (resForSubject == null) {
			resForSubject = "";
		}

		this.logger.debug("resForSubject is:::" + resForSubject);
		Object reIDsList;
		if (entityType.equalsIgnoreCase(subjectEntity) && !isSubjLevelSubRptReqFlag) {
			reIDsList = this.convertCommaStringToList(resForSubject);
		} else {
			new ArrayList();
			int subReportTypeId = Integer.parseInt(this.subjectDAO.getCaseDetail(crn).getSubReportTypeId());
			if (pSubReportTypeId != null && pSubReportTypeId.trim().length() > 0) {
				subReportTypeId = Integer.parseInt(pSubReportTypeId);
			}

			this.logger.debug("subReportTypeId is::" + subReportTypeId);
			List reList;
			if (subReportTypeId != 0 && subReportTypeId > 0) {
				reList = this.subjectDAO.getReForSubReportType(subReportTypeId);
				this.logger.debug("reList for sub report is::" + reList.size());
			} else {
				reList = this.subjectDAO.getReForReportType(crn);
				this.logger.debug("reList for report is::" + reList.size());
			}

			List newreList = new ArrayList();
			Iterator iterator = reList.iterator();

			while (iterator.hasNext()) {
				REMasterVO object = (REMasterVO) iterator.next();
				newreList.add(object.getrEMasterId().toString());
				this.logger.debug("reList for report/sub report is::" + object.getrEMasterId());
			}

			reIDsList = newreList;
			this.logger.debug("reIDsList" + newreList.size());
		}

		ArrayList modifiedREList = new ArrayList();

		try {
			Iterator iterator = resForEntity.iterator();

			while (iterator.hasNext()) {
				REMasterVO reObject1 = (REMasterVO) iterator.next();
				List reGrpList = reObject1.getSubGrpIDList();
				Iterator iterator2 = reGrpList.iterator();

				while (iterator2.hasNext()) {
					REMasterVO reObject2 = (REMasterVO) iterator2.next();
					List reSubGrpList = reObject2.getSubGrpElement();
					Iterator iterator3 = reSubGrpList.iterator();

					while (iterator3.hasNext()) {
						REMasterVO reObject3 = (REMasterVO) iterator3.next();
						String reID = reObject3.getrEMasterId();
						this.logger.debug("---reID---" + reID + " Default check reIDsList" + reIDsList + "Result::"
								+ ((List) reIDsList).contains(reID));
						if (((List) reIDsList).contains(reID)) {
							reObject3.setSelected("1");
							this.logger.debug("reID is" + reID);
						}
					}
				}

				modifiedREList.add(reObject1);
			}
		} catch (NullPointerException var22) {
			throw new CMSException(this.logger, var22);
		} catch (Exception var23) {
			throw new CMSException(this.logger, var23);
		}

		this.logger.debug("modifiedREList size is:::::" + modifiedREList.size());
		return modifiedREList;
	}

	public String reNameIDMap(String res, Map reMap) throws CMSException {
		this.logger.debug("Inside reNameIDMap method of SubjectManager class");
		this.logger.debug("res value is::::" + res);
		String reNames = "";

		try {
			if (!res.equals("")) {
				String reName;
				for (StringTokenizer st = new StringTokenizer(res, ","); st
						.hasMoreElements(); reNames = reNames + reName + ",") {
					String token = st.nextElement().toString();
					reName = (String) reMap.get(token);
				}

				reNames = reNames.substring(0, reNames.length() - 1);
			}

			this.logger.debug("reNames is::::" + reNames);
			return reNames;
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List getAssociateCase(String subID, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getAssociateCase method of SubjectManager class");
		this.logger.debug("subID is::::::" + subID);
		new ArrayList();
		List assCaseList = this.subjectDAO.getAssociatedCasesForSubject(subID, sortColumnName, start, limit, sortType);
		this.logger.debug("assCaseList size is::::" + assCaseList.size());
		return assCaseList;
	}

	public String doSubjectValidation(String crn, String subName, String subID) throws CMSException {
		this.logger.debug("Inside doSubjectValidation method of SubjectManager class");
		this.logger.debug("crn is:::" + crn + "::::subName is:::" + subName + ":::subID is:::" + subID);
		boolean primarySubjectDeleteFlag = false;
		List<String> teamNameList = new ArrayList();
		StringTokenizer st1 = new StringTokenizer(subID, ",");
		StringTokenizer st2 = new StringTokenizer(subName, ",");
		Map subMap = new HashedMap();
		String alertMes = "";

		try {
			String primarySubjectID = "";
			if (this.subjectDAO.getSubjectValidationForPrimarySub(crn) != null) {
				primarySubjectID = this.subjectDAO.getSubjectValidationForPrimarySub(crn).toString();
			}

			while (st1.hasMoreElements() && st2.hasMoreElements()) {
				String subjectID = st1.nextElement().toString();
				String subjectName = st2.nextElement().toString();
				subMap.put(subjectID, subjectName);
				if (primarySubjectID.equalsIgnoreCase(subjectID)) {
					alertMes = alertMes + subjectName + " is a Primary Subject and can't be deleted. <br>";
				} else {
					new ArrayList();
					List teamList = this.subjectDAO.getSubjectValidationForTeam(crn, subjectID, subjectName);
					String teamName = "";
					Iterator iterator = teamList.iterator();

					while (iterator.hasNext()) {
						TeamDetailsVO object = (TeamDetailsVO) iterator.next();
						String teamName1 = object.getTeamType();
						if (teamName1.equalsIgnoreCase("Primary")) {
							primarySubjectDeleteFlag = true;
						}

						teamNameList.add(teamName1);
						teamName = teamName + teamName1;
						if (iterator.hasNext()) {
							teamName = teamName + ",";
						}
					}

					if (teamName.length() > 0) {
						alertMes = alertMes + " " + subjectName + " cannot be deleted as it will remove " + teamName
								+ " teams from this case. <br>";
					}
				}
			}

			this.logger.debug("alert message is:::::" + alertMes);
			return alertMes;
		} catch (NullPointerException var18) {
			throw new CMSException(this.logger, var18);
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	public String doSubjectBIValidation(String crn, String subName, String subID) throws CMSException {
		this.logger.debug("going ot call doSubjectBIValidation");
		new ArrayList();
		List teamList = this.subjectDAO.getSubjectValidationForBITeam(crn, subID, subName);
		String teamName = "";
		this.logger.debug("teamList size is:::" + teamList.size());
		Iterator iterator = teamList.iterator();

		while (iterator.hasNext()) {
			TeamDetailsVO object = (TeamDetailsVO) iterator.next();
			teamName = object.getTeamType();
			this.logger.debug("teamName is::::->" + teamName);
		}

		return teamName;
	}

	public int deleteAssCRNForSub(String crnString, String subjectID) throws CMSException {
		this.logger.debug("Inside deleteAssCRNForSub method of SubjectManager class");
		this.logger.debug("crnString is::::" + crnString + ":::::::subjectID is" + subjectID);
		int deleteAssociateCRNCount = 0;
		Object deleteAssociateCaseList = new ArrayList();

		try {
			if (crnString != null && !crnString.equals("")) {
				deleteAssociateCaseList = this.convertCommaStringToList(crnString);
			}

			String crn;
			for (Iterator iterator = ((List) deleteAssociateCaseList).iterator(); iterator
					.hasNext(); deleteAssociateCRNCount = this.subjectDAO.deleteAssociateCRNForSub(crn, subjectID)) {
				crn = (String) iterator.next();
			}

			return deleteAssociateCRNCount;
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public Map getsubjectIndMap() throws CMSException {
		this.logger.debug("Inside getsubjectIndMap method of SubjectManager class");
		Map industryMap = new HashMap();
		List<IndustryMasterVO> indList = ResourceLocator.self().getCacheService().getCacheItemsList("INDUSTRY_MASTER");
		Iterator iterator = indList.iterator();

		while (iterator.hasNext()) {
			IndustryMasterVO industryMasterVO = (IndustryMasterVO) iterator.next();
			industryMap.put(industryMasterVO.getIndustry(), industryMasterVO.getIndustryCode());
		}

		return industryMap;
	}

	public int updateSubjectIndustry(String[] modifiedRecords, String currentUser) throws CMSException {
		this.logger.debug("Inside updateSubjectIndustry method of SubjectManager class");
		this.logger.debug("modifiedRecords object is::" + modifiedRecords + "::::currentUser is::" + currentUser);
		int updateSubjectIndustryTotalCount = 0;

		try {
			Map subjectIndustryMap = this.getsubjectIndMap();

			for (int i = 0; i < modifiedRecords.length; ++i) {
				Map updateIndustryMap = new HashMap();
				SubjectDetails subjectDetials = new SubjectDetails();
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				subjectDetials.setSubjectId(Integer.parseInt((String) jsonObject.get("jsonSubjectID")));
				subjectDetials
						.setIndustryCode((String) subjectIndustryMap.get((String) jsonObject.get("jsonIndustryID")));
				updateIndustryMap.put("currentUser", currentUser);
				updateIndustryMap.put("subjectID", subjectDetials.getSubjectId());
				updateIndustryMap.put("industryCode", subjectDetials.getIndustryCode());
				this.logger.debug("updateIndustryMap is" + updateIndustryMap);
				this.subjectDAO.updateSubjectIndForSubj(updateIndustryMap);
				++updateSubjectIndustryTotalCount;
			}
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("total records updated are:::" + updateSubjectIndustryTotalCount);
		return updateSubjectIndustryTotalCount;
	}

	public int updateSubjectRisk(String[] modifiedRecords, String currentUser) throws CMSException {
		this.logger.debug("Inside updateSubjectRisk method of SubjectManager class");
		this.logger.debug("modifiedRecords object is::" + modifiedRecords + "::::currentUser is::" + currentUser);
		int totalSubjectsRiskUpdate = 0;

		boolean totalSubjectsRiskUpdate;
		try {
			for (int i = 0; i < modifiedRecords.length; ++i) {
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				String subjectRiskString = (String) jsonObject.get("riskString");
				String subjectID = (String) jsonObject.get("jsonSubjectID");
				this.logger.debug("json object for risks loop" + jsonObject);
				StringTokenizer riskTokenizer1 = new StringTokenizer(subjectRiskString, ",");

				for (ArrayList SubjectRiskMapList = new ArrayList(); riskTokenizer1
						.hasMoreElements(); ++totalSubjectsRiskUpdate) {
					String token = riskTokenizer1.nextElement().toString();
					if (token.endsWith(":true")) {
						Map riskSubjectMap = new HashMap();
						String riskID = token.substring(0, token.indexOf(58));
						riskSubjectMap.put("subjectID", subjectID);
						riskSubjectMap.put("currentUser", currentUser);
						riskSubjectMap.put("riskID", riskID);
						SubjectRiskMapList.add(riskSubjectMap);
						this.logger.debug("riskSubjectMap is" + riskSubjectMap);
					}

					this.subjectDAO.updateSubjectRiskListForSub(SubjectRiskMapList, subjectID);
				}
			}

			return totalSubjectsRiskUpdate;
		} catch (NullPointerException var14) {
			totalSubjectsRiskUpdate = false;
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			totalSubjectsRiskUpdate = false;
			throw new CMSException(this.logger, var15);
		}
	}

	public List convertCommaStringToList(String commString) throws CMSException {
		this.logger.debug("Inside convertCommaStringToList method of SubjectManager class");
		this.logger.debug("commString value is:::::" + commString);
		ArrayList reIDList = new ArrayList();

		try {
			if (commString != null && !commString.equals("")) {
				StringTokenizer commaSeperatedString = new StringTokenizer(commString, ",");

				while (commaSeperatedString.hasMoreElements()) {
					String reID = commaSeperatedString.nextElement().toString();
					reIDList.add(reID);
				}
			}

			return reIDList;
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public SubjectDetails updateSubject(SubjectDetails subjectDetail, Session session, String caseHistoryPerformer)
			throws CMSException {
		this.logger.debug("Inside updateSubject method of SubjectManager class");
		this.logger.debug("crn  value is:::::" + subjectDetail.getCrn());
		this.logger.debug("pullback flag is:::::" + subjectDetail.isPullBackToResearch());
		String countryName = this.subjectDAO.getCountryFromCode(subjectDetail.getCountryCode());
		List activeREIdsList = this.subjectDAO.getActiveREIds();
		SubjectDetails updateSubjectDetails = new SubjectDetails();
		SubjectDetails oldSubjectDetails = ResourceLocator.self().getSubjectService()
				.editSubjectForCase(Integer.toString(subjectDetail.getSubjectId()));
		oldSubjectDetails.setEntityName(oldSubjectDetails.getEntityVO().getEntityType());
		this.logger.debug("Old entity type is::::" + oldSubjectDetails.getEntityName());
		subjectDetail.setCountryName(countryName);
		boolean reUpdateFlag = false;
		int updateSubjectCount = 0;

		try {
			String[] modifiedRecords = subjectDetail.getModifiedRecords();

			String newEntityType;
			for (int i = 0; i < modifiedRecords.length; ++i) {
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				this.logger.debug("jsonObject is:::::" + jsonObject);
				newEntityType = (String) jsonObject.get(this.reString);
				StringTokenizer reTokenizer1 = new StringTokenizer(newEntityType, ",");
				String completREIDString = "";

				String oldREString;
				String subjectNotificationMessage;
				while (reTokenizer1.hasMoreElements()) {
					oldREString = reTokenizer1.nextElement().toString();
					if (oldREString.endsWith(":true")) {
						subjectNotificationMessage = oldREString.substring(0, oldREString.indexOf(58));
						if (activeREIdsList != null && activeREIdsList.size() > 0
								&& activeREIdsList.contains(new Integer(subjectNotificationMessage))
								&& !subjectNotificationMessage.equalsIgnoreCase("None")) {
							completREIDString = completREIDString + subjectNotificationMessage + ",";
						}
					}
				}

				if (!completREIDString.equals("")) {
					completREIDString = completREIDString.substring(0, completREIDString.length() - 1);
				}

				this.logger.debug("completREIDString is:::" + completREIDString);
				subjectDetail.setReIds(completREIDString);
				oldREString = this.subjectDAO.getReIdsForSubject(subjectDetail.getSubjectId());
				reUpdateFlag = this.getReChangeFlag(oldREString, completREIDString);
				if (reUpdateFlag) {
					this.logger.debug("reUpdateFlag is true going to send update notification.");
					subjectNotificationMessage = "Subject Research Element has been updated for the case(0). Please check it out. Updated Subject Name :"
							.replace("0", subjectDetail.getCrn()) + subjectDetail.getSubjectName() + ".";
					this.logger.debug("subjectNotificationMessage is::" + subjectNotificationMessage);
					ResourceLocator.self().getNotificationService().createSystemNotification(subjectNotificationMessage,
							subjectNotificationMessage,
							this.getNotificationUsersForSubject(subjectDetail.getCrn(), subjectDetail.getUpdatedBy()),
							subjectDetail.getCrn());
				} else {
					this.logger.debug("reUpdateFlag is true going to send update notification.");
					subjectNotificationMessage = "";
					CaseDetails deltaCaseDetails = new CaseDetails();
					subjectNotificationMessage = "Please note, Subject information has been updated for the case(0). Following are the details updated:(1)"
							.replace("0", subjectDetail.getCrn());
					subjectNotificationMessage = subjectNotificationMessage + " Updated details are as follows : "
							+ "\n";
					if (!oldSubjectDetails.getSubjectName().equals(subjectDetail.getSubjectName())) {
						this.logger.debug("subjectDetail.getSubjectName::::" + subjectDetail.getSubjectName());
						subjectNotificationMessage = subjectNotificationMessage + "Subject Name:"
								+ subjectDetail.getSubjectName() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					}

					if (!oldSubjectDetails.getCountryVO().getCountry().equals(subjectDetail.getCountryName())) {
						this.logger.debug("subjectDetail.getCountryName::::" + subjectDetail.getCountryName());
						subjectNotificationMessage = subjectNotificationMessage + "Country Name:"
								+ subjectDetail.getCountryName() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					}

					if (oldSubjectDetails.getSubReportType() != null
							&& !oldSubjectDetails.getSubReportType().equals(subjectDetail.getSubReportType())) {
						this.logger.debug("subjectDetail.getOtherDetails::::" + subjectDetail.getOtherDetails());
						subjectNotificationMessage = subjectNotificationMessage + "Sub Report type :"
								+ subjectDetail.getSubReportType() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					}

					if (oldSubjectDetails.getAddress() == null && subjectDetail.getAddress() != null
							&& subjectDetail.getAddress().equalsIgnoreCase("")
							&& subjectDetail.getAddress().length() > 0) {
						this.logger.debug(" In if subjectDetail.getAddress::::" + subjectDetail.getAddress());
						subjectNotificationMessage = subjectNotificationMessage + "Address:"
								+ subjectDetail.getAddress() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					} else if (oldSubjectDetails.getAddress() != null
							&& !oldSubjectDetails.getAddress().equals(subjectDetail.getAddress())) {
						this.logger.debug("in else ifsubjectDetail.getAddress::::" + subjectDetail.getAddress());
						subjectNotificationMessage = subjectNotificationMessage + "Address:"
								+ subjectDetail.getAddress() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					}

					if (oldSubjectDetails.getPosition() == null && subjectDetail.getPosition() != null
							&& !subjectDetail.getPosition().equalsIgnoreCase("")
							&& subjectDetail.getPosition().length() != 0) {
						this.logger.debug("In if subjectDetail.getPosition::::" + subjectDetail.getPosition());
						subjectNotificationMessage = subjectNotificationMessage + "Position:"
								+ subjectDetail.getPosition() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					} else if (oldSubjectDetails.getPosition() != null
							&& !oldSubjectDetails.getPosition().equals(subjectDetail.getPosition())) {
						this.logger.debug("in else if subjectDetail.getPosition::::" + subjectDetail.getPosition());
						subjectNotificationMessage = subjectNotificationMessage + "Position:"
								+ subjectDetail.getPosition() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					}

					if (oldSubjectDetails.getOtherDetails() == null && subjectDetail.getOtherDetails() != null
							&& !subjectDetail.getOtherDetails().equals("")
							&& subjectDetail.getOtherDetails().length() != 0) {
						this.logger.debug("subjectDetail.getOtherDetails::::" + subjectDetail.getOtherDetails());
						subjectNotificationMessage = subjectNotificationMessage + "Other details:"
								+ subjectDetail.getOtherDetails() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					} else if (oldSubjectDetails.getOtherDetails() != null
							&& !oldSubjectDetails.getOtherDetails().equals(subjectDetail.getOtherDetails())) {
						this.logger.debug("subjectDetail.getOtherDetails::::" + subjectDetail.getOtherDetails());
						subjectNotificationMessage = subjectNotificationMessage + "Other details:"
								+ subjectDetail.getOtherDetails() + "\n";
						deltaCaseDetails.setResearchDueDateFlag(true);
					}

					if (deltaCaseDetails.isResearchDueDateFlag()) {
						this.logger.debug("subjectNotificationMessage is::" + subjectNotificationMessage);
						ResourceLocator.self().getNotificationService().createSystemNotification(
								subjectNotificationMessage, subjectNotificationMessage,
								this.getNotificationUsersForSubject(subjectDetail.getCrn(),
										subjectDetail.getUpdatedBy()),
								subjectDetail.getCrn());
					}
				}
			}

			long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(subjectDetail.getCrn());
			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					session);
			newEntityType = subjectDetail.getEntityTypeId() == 1 ? "Individual" : "Company";
			subjectDetail.setEntityName(newEntityType);
			this.logger.debug("New entity type is::::" + subjectDetail.getEntityName());
			Map reMap = this.subjectDAO.getReMap();
			subjectDetail.setReNames(this.reNameIDMap(subjectDetail.getReIds(), reMap));
			boolean pingSuccessful = true;
			boolean processFlag = true;
			if (subjectDetail.getIsisCase() == 1) {
				pingSuccessful = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				this.logger.debug("pingSuccessful :: " + pingSuccessful);
				if (!pingSuccessful) {
					updateSubjectDetails.setUpdateSubjectCount(-1);
					return updateSubjectDetails;
				}

				ClientSubjectVO clientSubjectVO = this.setISISVOForUpdateSubject(subjectDetail);
				Map resultMap = ResourceLocator.self().getAtlasWebServiceClient().updateSubject(clientSubjectVO);
				this.logger.debug("resultMap value is:::::::" + resultMap);
				int successFlag = (Integer) resultMap.get("successFlag");
				int communicationErrorFlag = (Integer) resultMap.get("communicationErrorFlag");
				String message = (String) resultMap.get("message");
				updateSubjectDetails.setIsisErrorMessge(message);
				this.logger.debug("successFlag value is::::::" + successFlag);
				this.logger.debug("communicationErrorFlag value is::::" + communicationErrorFlag);
				if (successFlag == 1) {
					processFlag = true;
				} else {
					processFlag = false;
				}
			}

			this.logger.debug("processFlag :: " + processFlag);
			if (pingSuccessful && processFlag) {
				CaseDetails caseHistoryNewCaseDetails = new CaseDetails();
				List caseHistoryNewSubjectList = new ArrayList();
				caseHistoryNewSubjectList.add(subjectDetail);
				caseHistoryNewCaseDetails.setSubjectList(caseHistoryNewSubjectList);
				CaseDetails caseHistoryOldCaseDetails = new CaseDetails();
				List caseHistoryOldSubjectList = new ArrayList();
				oldSubjectDetails.setReNames(this.reNameIDMap(oldSubjectDetails.getReIds(), reMap));
				oldSubjectDetails.setCountryName(oldSubjectDetails.getCountryVO().getCountry());
				caseHistoryOldSubjectList.add(oldSubjectDetails);
				caseHistoryOldCaseDetails.setSubjectList(caseHistoryOldSubjectList);
				CaseHistory caseHistoryOtherParam = new CaseHistory();
				caseHistoryOtherParam.setCRN(subjectDetail.getCrn());
				caseHistoryOtherParam.setPid(Long.toString(piid));
				caseHistoryOtherParam.setProcessCycle(processCycle);
				if (caseHistoryPerformer.equalsIgnoreCase("")) {
					caseHistoryOtherParam.setPerformer(subjectDetail.getUpdatedBy());
				} else {
					caseHistoryOtherParam.setPerformer(caseHistoryPerformer);
				}

				ResourceLocator.self().getCaseHistoryService().setCaseHistory(caseHistoryOldCaseDetails,
						caseHistoryNewCaseDetails, caseHistoryOtherParam, "Subject");
				this.logger.debug("subjectDetail.getIsBIDelete()-------->>>>>" + subjectDetail.getIsBIDelete());
				CaseDetails oldCaseDetails = null;
				if (subjectDetail.getIsBIDelete().equalsIgnoreCase("true")) {
					oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
							.deleteBITeamForCase(subjectDetail.getCrn());
					this.logger.debug("oldCaseDetails when deleting BI team in update Subject :: " + oldCaseDetails);
				}

				CaseDetails newcaseDetails = new CaseDetails();
				newcaseDetails.setCrn(subjectDetail.getCrn());
				this.logger.debug("crn set for new case details is::::" + subjectDetail.getCrn());
				List<SubjectDetails> subjectList = new ArrayList();
				subjectList.add(subjectDetail);
				newcaseDetails.setSubjectList(subjectList);
				newcaseDetails.setUpdatedBy(subjectDetail.getUpdatedBy());
				this.logger.debug("bi manager value is " + subjectDetail.getSubjectAddedBIMgr());
				if (subjectDetail.getSubjectAddedBIMgr() != null
						&& !subjectDetail.getSubjectAddedBIMgr().trim().equalsIgnoreCase("")) {
					this.logger.debug("inside adding bi mgr:::" + subjectDetail.getSubjectAddedBIMgr());
					TeamDetails teamDetails = new TeamDetails();
					teamDetails.setManagerName(subjectDetail.getSubjectAddedBIMgr());
					List<TeamDetails> teamDetailsList = new ArrayList();
					teamDetailsList.add(teamDetails);
					newcaseDetails.setTeamList(teamDetailsList);
				}

				if (oldCaseDetails == null) {
					oldCaseDetails = new CaseDetails();
				}

				oldCaseDetails.setCrn(subjectDetail.getCrn());
				this.logger.debug("crn set for old case details is::::" + subjectDetail.getCrn());
				List<SubjectDetails> oldSubjectList = new ArrayList();
				oldSubjectList.add(oldSubjectDetails);
				oldCaseDetails.setSubjectList(oldSubjectList);
				boolean isOfficeAssignmentDone = ResourceLocator.self().getOfficeAssignmentService()
						.checkOfficeAssignmentStatus(subjectDetail.getCrn());
				this.logger.debug("isOfficeAssignmentDone is::::" + isOfficeAssignmentDone);
				if (isOfficeAssignmentDone) {
					this.logger.debug("office assignment done... going to call flow controller.......");
					ResourceLocator.self().getFlowService().updateDSAndPushFlow(session, oldCaseDetails, newcaseDetails,
							"UpdateSubject");
				}

				updateSubjectCount = this.subjectDAO.saveUpdatedSubject(subjectDetail);
				updateSubjectDetails.setUpdateSubjectCount(updateSubjectCount);
				SubjectLevelRiskProfileDetailsVO caseDetailsForProfile = this.subjectDAO
						.getCaseDetailsForRiskProfile(subjectDetail.getCrn());
				if (updateSubjectCount > 0) {
					this.subjectDAO.updateCaseLevelRisk(caseDetailsForProfile, subjectDetail);
					this.subjectDAO.updateCaseLevelRiskCountryBreakDown(caseDetailsForProfile, subjectDetail);
					this.subjectDAO.updateSubjectLevelRisk(caseDetailsForProfile, subjectDetail);
					this.subjectDAO.updateSubjectLevelRiskHasCntryBrkDown(caseDetailsForProfile, subjectDetail);
					this.subjectDAO.updateSubjectLevelRiskAggregation(caseDetailsForProfile, subjectDetail);
					this.subjectDAO.updateCaseLevelRiskAggregation(caseDetailsForProfile, subjectDetail);
				}

				if (subjectDetail.isPrimarySub()) {
					HashMap dsValues = new HashMap();
					dsValues.put("PrimarySubjectCountry", countryName);
					dsValues.put("PrimarySubject", subjectDetail.getSubjectName());
					ResourceLocator.self().getSBMService().updateDataSlots(session, piid, dsValues);
				}
			}
		} catch (NullPointerException var30) {
			throw new CMSException(this.logger, var30);
		} catch (Exception var31) {
			throw new CMSException(this.logger, var31);
		}

		this.logger.debug("updateSubjectCount value is :: " + updateSubjectCount);
		return updateSubjectDetails;
	}

	private ClientSubjectVO setISISVOForUpdateSubject(SubjectDetails subjectDetails) {
		ClientSubjectVO clientSubjectVO = new ClientSubjectVO();
		clientSubjectVO.setAtlasSubjectID(subjectDetails.getSubjectId());
		clientSubjectVO.setCountry(subjectDetails.getCountryCode());
		clientSubjectVO.setCRN(subjectDetails.getCrn());
		clientSubjectVO.setOtherDetails(subjectDetails.getOtherDetails());
		clientSubjectVO.setPrimary(subjectDetails.isPrimarySub());
		clientSubjectVO.setSubjectID(subjectDetails.getIsisSubjectId());
		clientSubjectVO.setSubjectName(subjectDetails.getSubjectName());
		clientSubjectVO.setSubjectPosition(subjectDetails.getPosition());
		clientSubjectVO.setSubjectREs(subjectDetails.getReIds());
		clientSubjectVO.setSubjectType(Integer.toString(subjectDetails.getEntityTypeId()));
		clientSubjectVO.setSlBudget(subjectDetails.getSlBudget());
		clientSubjectVO.setSlCurrency(subjectDetails.getSlCurrency());
		if (subjectDetails.getSubReportTypeId() == null
				|| subjectDetails.getSubReportTypeId().trim().equalsIgnoreCase("")) {
			subjectDetails.setSubReportTypeId("0");
		}

		clientSubjectVO.setSlSubreportID(Integer.parseInt(subjectDetails.getSubReportTypeId()));
		clientSubjectVO.setUpdateType("Update");
		return clientSubjectVO;
	}

	public SubjectDetails getDefaultSubjectDetail(String crn) throws CMSException {
		this.logger.debug("Inside updateSubject method of SubjectManager class");
		this.logger.debug("crn  value is:::::" + crn);
		new SubjectDetails();
		SubjectDetails subjectDetails = this.subjectDAO.getPrimarySubjectDetail(crn);
		return subjectDetails;
	}

	public List getREElementsForNewSubject(String subjectEntity, String crn, String pSubReportTypeId,
			String isSubjLevelSubRptReq) throws CMSException {
		this.logger.debug("Inside getREElementsForNewSubject method of SubjectManager class::");
		this.logger.debug("SubjectEntity  value is:::::" + subjectEntity + "::::::crn is:::" + crn);
		CaseDetails caseDetails = this.subjectDAO.getCaseDetail(crn);
		this.logger.debug("subreporttypeid::::" + caseDetails.getSubReportTypeId() + ":::::reporttypeid:::::"
				+ caseDetails.getReportTypeId() + "::::clientcode::" + caseDetails.getClientCode());
		int reportTypeId = Integer.parseInt(caseDetails.getReportTypeId());
		String clientCode = caseDetails.getClientCode();
		int subReportTypeId = Integer.parseInt(caseDetails.getSubReportTypeId());
		if (pSubReportTypeId != null && pSubReportTypeId.trim().length() > 0) {
			subReportTypeId = Integer.parseInt(pSubReportTypeId);
			caseDetails.setSubReportTypeId(pSubReportTypeId);
			this.logger.debug("pSubReportTypeId  value is:::::" + pSubReportTypeId);
		}

		List rptClientlist = this.subjectDAO.getReportTypeLIst();
		List fcpaReportTypeList = this.subjectDAO.getFCPAReportTypeList(subjectEntity);
		SubjectDetails subjectDetails = this.subjectDAO.getPrimarySubjectDetail(crn);
		new ArrayList();
		this.logger.debug("rptClientlist.." + rptClientlist);
		List reIDsList;
		if (rptClientlist != null && rptClientlist.size() > 0) {
			if (rptClientlist.contains(reportTypeId)) {
				this.logger.debug("reportTypeId matches with specific case..");
				String clientCodes = this.subjectDAO.getClientCodesForReportType(reportTypeId);
				List clientCodeList = this.convertCommaStringToList(clientCodes);
				if (clientCodeList.contains(clientCode)) {
					int riskLevel = this.subjectDAO.getCountryRiskLevelForClientCountry(clientCode);
					this.logger.debug("client codematches with specific case..");
					reIDsList = this.getSpecificRESelection(reportTypeId, subReportTypeId, riskLevel, subjectDetails,
							subjectEntity);
				} else if (fcpaReportTypeList != null && fcpaReportTypeList.size() > 0) {
					if (fcpaReportTypeList.contains(reportTypeId)) {
						this.logger.debug("fcpa matches..");
						reIDsList = this.getFCPARESelection(reportTypeId, subReportTypeId, subjectDetails,
								subjectEntity);
					} else {
						this.logger.debug("insdie else of fcpa..");
						reIDsList = this.getDefaultRESelection(caseDetails, subjectDetails, subjectEntity, crn,
								isSubjLevelSubRptReq);
					}
				} else {
					this.logger.debug("insdie else of specific..");
					reIDsList = this.getDefaultRESelection(caseDetails, subjectDetails, subjectEntity, crn,
							isSubjLevelSubRptReq);
				}
			} else if (fcpaReportTypeList != null && fcpaReportTypeList.size() > 0) {
				if (fcpaReportTypeList.contains(reportTypeId)) {
					this.logger.debug("fcpa matches....");
					reIDsList = this.getFCPARESelection(reportTypeId, subReportTypeId, subjectDetails, subjectEntity);
				} else {
					this.logger.debug("insdie else of fcpa....");
					reIDsList = this.getDefaultRESelection(caseDetails, subjectDetails, subjectEntity, crn,
							isSubjLevelSubRptReq);
				}
			} else {
				this.logger.debug("insdie else of specific....");
				reIDsList = this.getDefaultRESelection(caseDetails, subjectDetails, subjectEntity, crn,
						isSubjLevelSubRptReq);
			}
		} else if (fcpaReportTypeList != null && fcpaReportTypeList.size() > 0) {
			if (fcpaReportTypeList.contains(reportTypeId)) {
				this.logger.debug("fcpa matches......");
				reIDsList = this.getFCPARESelection(reportTypeId, subReportTypeId, subjectDetails, subjectEntity);
			} else {
				this.logger.debug("insdie else of fcpa....");
				reIDsList = this.getDefaultRESelection(caseDetails, subjectDetails, subjectEntity, crn,
						isSubjLevelSubRptReq);
			}
		} else {
			this.logger.debug("insdie else of specific....");
			reIDsList = this.getDefaultRESelection(caseDetails, subjectDetails, subjectEntity, crn,
					isSubjLevelSubRptReq);
		}

		List resForEntity = this.subjectDAO.getREs(subjectEntity);
		int primarySubjectEntityID = Integer.parseInt("2");
		this.logger.debug("reIDsList.size is::::::" + reIDsList.size());
		this.logger.debug("reIDsList::::::" + reIDsList);
		ArrayList modifiedREList = new ArrayList();

		try {
			Iterator iterator = resForEntity.iterator();

			while (iterator.hasNext()) {
				REMasterVO reObject1 = (REMasterVO) iterator.next();
				List reGrpList = reObject1.getSubGrpIDList();
				Iterator iterator2 = reGrpList.iterator();

				while (iterator2.hasNext()) {
					REMasterVO reObject2 = (REMasterVO) iterator2.next();
					List reSubGrpList = reObject2.getSubGrpElement();
					Iterator iterator3 = reSubGrpList.iterator();

					while (iterator3.hasNext()) {
						REMasterVO reObject3 = (REMasterVO) iterator3.next();
						String reID = reObject3.getrEMasterId();
						this.logger.debug(reObject3.getName() + "-->>" + reObject3.getRemoveEnglish());
						this.logger.debug("---reID---" + reID);
						if (reIDsList.contains(reID)) {
							reObject3.setSelected("1");
							this.logger.debug("reID is" + reID);
						}
					}
				}

				this.logger.debug(reObject1.getName() + "--" + reObject1.getRemoveEnglish());
				modifiedREList.add(reObject1);
			}
		} catch (NullPointerException var25) {
			throw new CMSException(this.logger, var25);
		} catch (Exception var26) {
			throw new CMSException(this.logger, var26);
		}

		this.logger.debug("modifiedREList size is:::" + modifiedREList.size());
		return modifiedREList;
	}

	private List getSpecificRESelection(int reportTypeId, int subReportTypeId, int riskLevel,
			SubjectDetails subjectDetails, String subjectEntity) throws CMSException {
		new ArrayList();

		try {
			List reIDsList;
			if (subReportTypeId == 0) {
				this.logger.debug("sub report type id is 0 for specfic case");
				if (subjectDetails != null) {
					this.logger.debug("non primary subject..");
					reIDsList = this.subjectDAO.getResForSpecficReportType(reportTypeId, 0, riskLevel, subjectEntity);
					this.logger.debug(reIDsList + "reIDsList size is:::" + reIDsList.size());
				} else {
					this.logger.debug("primary subject..");
					reIDsList = this.subjectDAO.getResForSpecficReportType(reportTypeId, 1, riskLevel, subjectEntity);
					this.logger.debug("reIDsList size is:::" + reIDsList.size());
				}
			} else if (subjectDetails != null) {
				this.logger.debug("non primary subject..");
				reIDsList = this.subjectDAO.getResForSpecficSubReportType(subReportTypeId, 0, riskLevel, subjectEntity);
				this.logger.debug("reIDsList size is:::" + reIDsList.size());
			} else {
				this.logger.debug("primary subject..");
				reIDsList = this.subjectDAO.getResForSpecficSubReportType(subReportTypeId, 1, riskLevel, subjectEntity);
				this.logger.debug("reIDsList size is:::" + reIDsList.size());
			}

			return reIDsList;
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private List getFCPARESelection(int reportTypeId, int subReportTypeId, SubjectDetails subjectDetails,
			String subjectEntity) throws CMSException {
		new ArrayList();

		try {
			List reIDsList;
			if (subReportTypeId == 0) {
				this.logger.debug("sub report type id is 0 for specfic case");
				if (subjectDetails != null) {
					this.logger.debug("non primary subject..");
					reIDsList = this.subjectDAO.getResForFCPAReportType(reportTypeId, 0, subjectEntity);
					this.logger.debug(reIDsList + "reIDsList size is:::" + reIDsList.size());
				} else {
					this.logger.debug("primary subject..");
					reIDsList = this.subjectDAO.getResForFCPAReportType(reportTypeId, 1, subjectEntity);
					this.logger.debug("reIDsList size is:::" + reIDsList.size());
				}
			} else if (subjectDetails != null) {
				this.logger.debug("non primary subject..");
				reIDsList = this.subjectDAO.getResForFCPASubReportType(subReportTypeId, 0, subjectEntity);
				this.logger.debug("reIDsList size is:::" + reIDsList.size());
			} else {
				this.logger.debug("primary subject..");
				reIDsList = this.subjectDAO.getResForFCPASubReportType(subReportTypeId, 1, subjectEntity);
				this.logger.debug("reIDsList size is:::" + reIDsList.size());
			}

			return reIDsList;
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private List getDefaultRESelection(CaseDetails caseDetails, SubjectDetails subjectDetails, String subjectEntity,
			String crn, String isSubjLevelSubRptReq) throws CMSException {
		new ArrayList();
		this.logger.debug("Inside getDefaultRESelection method of SubjectManager class::isSubjLevelSubRptReq::"
				+ isSubjLevelSubRptReq);
		List reIDsList;
		if (subjectDetails == null || isSubjLevelSubRptReq != null
				&& (isSubjLevelSubRptReq == null || isSubjLevelSubRptReq.trim().equals("1"))) {
			reIDsList = this.addPrimarySubject(caseDetails, crn);
		} else {
			reIDsList = this.addNonPrimarySubject(caseDetails, subjectDetails, subjectEntity, crn);
		}

		return reIDsList;
	}

	private List addNonPrimarySubject(CaseDetails caseDetails, SubjectDetails subjectDetails, String subjectEntity,
			String crn) throws CMSException {
		this.logger.debug("Inside addNonPrimarySubject method of SubjectManager class::");
		Object reIDsList = new ArrayList();

		try {
			int primarySubjectEntityID = subjectDetails.getEntityTypeId();
			if (primarySubjectEntityID == Integer.parseInt(subjectEntity)) {
				this.logger.debug("inside if.....");
				int entityTypeOfNewSub = Integer.parseInt(subjectEntity);
				SubjectDetails firstSubjectWithSameEntity = this.subjectDAO.getFirstSubjectOfSameEntity(crn,
						entityTypeOfNewSub);
				String subjectReIDs = firstSubjectWithSameEntity.getReIds();
				if (subjectReIDs != null && !subjectReIDs.equals("")) {
					reIDsList = this.convertCommaStringToList(subjectReIDs);
					this.logger.debug("reIDsList size is::::" + ((List) reIDsList).size());
				}
			} else {
				SubjectDetails nonPrimarySubjectWithDifferentEntity = this.subjectDAO
						.getNonPrimaryDifferemtEntitySubject(crn, primarySubjectEntityID);
				if (nonPrimarySubjectWithDifferentEntity != null) {
					this.logger.debug("nonPrimarySubjectWithDifferentEntity is not null......");
					this.logger.debug(
							"Different entity then primary subject and already have subject in db for this entity and same case..");
					String nonPrimarySubjectReIDs = nonPrimarySubjectWithDifferentEntity.getReIds();
					if (nonPrimarySubjectReIDs != null && !nonPrimarySubjectReIDs.equals("")) {
						reIDsList = this.convertCommaStringToList(nonPrimarySubjectReIDs);
						this.logger.debug("reIDsList size is::::" + ((List) reIDsList).size());
					}
				} else {
					this.logger.debug(
							"different entity then primary subject and dont have any such subject with this entity for this case.");
					this.logger.debug("inside else.....");
					this.logger.debug("going to call.....");
					reIDsList = this.addPrimarySubject(caseDetails, crn);
				}
			}

			return (List) reIDsList;
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private List addFirstNonPrimarySubject(CaseDetails caseDetails, SubjectDetails subjectDetails, String subjectEntity,
			String crn) throws CMSException {
		this.logger.debug("Inside addFirstNonPrimarySubject method of SubjectManager class...");
		ArrayList reIDsList = new ArrayList();

		try {
			Map reMap = new HashMap();
			Map individualREMap = new HashMap();
			Map companyREMap = new HashMap();
			List individualRENameList = new ArrayList();
			List companyRENameList = new ArrayList();
			String primarySubjectReIDs = subjectDetails.getReIds();
			List primarySubjectSelectedRes = new ArrayList();
			if (primarySubjectReIDs != null && !primarySubjectReIDs.equals("")) {
				primarySubjectSelectedRes = this.convertCommaStringToList(primarySubjectReIDs);
				this.logger.debug("reIDsList size is::::" + ((List) primarySubjectSelectedRes).size());
			}

			List primarySubjectSelectedResNames = new ArrayList();
			Map reIdMap = this.subjectDAO.getReMap();
			Iterator iterator = ((List) primarySubjectSelectedRes).iterator();

			while (iterator.hasNext()) {
				String reIdString = (String) iterator.next();
				String reNameForId = (String) reIdMap.get(reIdString);
				this.logger.debug("reNameForId...." + reNameForId);
				primarySubjectSelectedResNames.add(reNameForId);
			}

			new ArrayList();
			int subReportTypeId = Integer.parseInt(caseDetails.getSubReportTypeId());
			this.logger.debug("subReportTypeId is::" + subReportTypeId);
			List resList;
			if (subReportTypeId != 0 && subReportTypeId > 0) {
				resList = this.subjectDAO.getReForSubReportType(subReportTypeId);
				this.logger.debug("resList for sub report is::" + resList.size());
			} else {
				resList = this.subjectDAO.getReForReportType(crn);
				this.logger.debug("resList for report is::" + resList.size());
			}

			List newreList = new ArrayList();
			Iterator iterator1 = resList.iterator();

			while (iterator1.hasNext()) {
				REMasterVO object = (REMasterVO) iterator1.next();
				newreList.add(object.getrEMasterId().toString());
				this.logger.debug("newreList size is::::---" + newreList.size());
			}

			List reList = this.subjectDAO.getReDetails();
			Iterator iterator = reList.iterator();

			while (iterator.hasNext()) {
				REMasterVO object = (REMasterVO) iterator.next();
				if (object.getEntityTypeId().equals("1")) {
					individualREMap.put(object.getName(), object.getrEMasterId());
					individualRENameList.add(object.getName());
				} else if (object.getEntityTypeId().equals("2")) {
					companyREMap.put(object.getName(), object.getrEMasterId());
					companyRENameList.add(object.getName());
				}
			}

			this.logger.debug("Individual RE Map is:::::::::" + individualREMap);
			this.logger.debug("Company RE Map is:::::::::::" + companyREMap);
			reMap.put("1", individualREMap);
			reMap.put("2", companyREMap);
			Map subjectEntityMap = (Map) reMap.get(subjectEntity);
			this.logger.debug("SubjectEntity Map is:::::::::" + subjectEntityMap);
			Iterator iterator = subjectEntityMap.keySet().iterator();

			String reName;
			while (iterator.hasNext()) {
				reName = (String) iterator.next();
				this.logger.debug("reName is::::::::" + reName);
				List primaryEntityReNameList = Integer.toString(subjectDetails.getEntityTypeId()).equals("1")
						? individualRENameList
						: companyRENameList;
				if (primaryEntityReNameList.contains(reName)) {
					this.logger
							.debug("reName matching with primary subject entity is::::::::::::::::::::::::" + reName);
					if (primarySubjectSelectedResNames.contains(reName)) {
						this.logger.debug("**reName that marked as check from primary subject is::::" + reName);
						reIDsList.add(subjectEntityMap.get(reName));
					}
				} else {
					this.logger.debug("Inside else >>>>");
					if (newreList.contains(subjectEntityMap.get(reName))) {
						reIDsList.add(subjectEntityMap.get(reName));
						this.logger.debug("**Inside report/subreport match.....");
					}
				}
			}

			this.logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			this.logger.debug("reIDsList size is:::::::::" + reIDsList.size());
			iterator = reIDsList.iterator();

			while (iterator.hasNext()) {
				reName = (String) iterator.next();
				this.logger.debug("---------------------------" + reName);
			}

			return reIDsList;
		} catch (NullPointerException var23) {
			throw new CMSException(this.logger, var23);
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}
	}

	private List addPrimarySubject(CaseDetails caseDetails, String crn) throws CMSException {
		this.logger.debug("Inside addPrimarySubject method of SubjectManager class::");
		new ArrayList();

		try {
			new ArrayList();
			int subReportTypeId = Integer.parseInt(caseDetails.getSubReportTypeId());
			this.logger.debug("subReportTypeId is::" + subReportTypeId);
			List reList;
			if (subReportTypeId != 0 && subReportTypeId > 0) {
				reList = this.subjectDAO.getReForSubReportType(subReportTypeId);
				this.logger.debug("reList for sub report is::" + reList.size());
			} else {
				reList = this.subjectDAO.getReForReportType(crn);
				this.logger.debug("reList for report is::" + reList.size());
			}

			List newreList = new ArrayList();
			Iterator iterator = reList.iterator();

			while (iterator.hasNext()) {
				REMasterVO object = (REMasterVO) iterator.next();
				newreList.add(object.getrEMasterId().toString());
				this.logger.debug("newreList size is::::" + newreList.size());
			}

			return newreList;
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List getMatchingAssociateCasesForSub(String subjectName, String subjectID, String crn, String sortColumnName,
			int start, int limit, String sortType) throws CMSException {
		this.logger.debug(
				"Inside getMatchingAssociateCasesForSub method of SubjectManager class:::subjectName  value is:::::"
						+ subjectName + "::::::subjectID is:::" + subjectID + "::::::crn is:::" + crn);
		new ArrayList();

		List associateCaseList;
		try {
			associateCaseList = this.subjectDAO.getMatchingAssociateCasesForSub(subjectName, subjectID, crn,
					sortColumnName, start, limit, sortType);
			Iterator iterator = associateCaseList.iterator();

			while (iterator.hasNext()) {
				CaseDetails caseDetailObject = (CaseDetails) iterator.next();
				List<SubjectDetails> subjectDetailList = caseDetailObject.getSubjectList();
				String nonPrimarySubjectCountryString = "";
				Iterator iterator2 = subjectDetailList.iterator();

				while (iterator2.hasNext()) {
					SubjectDetails subjectDetailObject = (SubjectDetails) iterator2.next();
					if (subjectDetailObject.isPrimarySub()) {
						caseDetailObject.setPrimarySubjectName(subjectDetailObject.getSubjectName());
						caseDetailObject.setPrimarySubjectCountryName(subjectDetailObject.getCountryVO().getCountry());
					} else {
						nonPrimarySubjectCountryString = nonPrimarySubjectCountryString
								+ subjectDetailObject.getSubjectName() + "("
								+ subjectDetailObject.getCountryVO().getCountry() + "),";
					}
				}

				nonPrimarySubjectCountryString = !"".equals(nonPrimarySubjectCountryString)
						? nonPrimarySubjectCountryString.substring(0, nonPrimarySubjectCountryString.length() - 1)
						: "";
				caseDetailObject.setOtherSubjectNameCountryMap(nonPrimarySubjectCountryString);
			}
		} catch (NullPointerException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}

		this.logger.debug("associateCaseList size is:::::" + associateCaseList.size());
		return associateCaseList;
	}

	public List getMatchingAssociateCasesForAddSub(String subjectName, String subjectID, String crn)
			throws CMSException {
		this.logger.debug(
				"Inside getMatchingAssociateCasesForSub method of SubjectManager class:::subjectName  value is:::::"
						+ subjectName + "::::::subjectID is:::" + subjectID + "::::::crn is:::" + crn);
		new ArrayList();

		List associateCaseList;
		try {
			associateCaseList = this.subjectDAO.getMatchingAssociateCasesForAddSub(subjectName, subjectID, crn);
			Iterator iterator = associateCaseList.iterator();

			while (iterator.hasNext()) {
				CaseDetails caseDetailObject = (CaseDetails) iterator.next();
				List<SubjectDetails> subjectDetailList = caseDetailObject.getSubjectList();
				String nonPrimarySubjectCountryString = "";
				Iterator iterator2 = subjectDetailList.iterator();

				while (iterator2.hasNext()) {
					SubjectDetails subjectDetailObject = (SubjectDetails) iterator2.next();
					if (subjectDetailObject.isPrimarySub()) {
						caseDetailObject.setPrimarySubjectName(subjectDetailObject.getSubjectName());
						caseDetailObject.setPrimarySubjectCountryName(subjectDetailObject.getCountryVO().getCountry());
					} else {
						nonPrimarySubjectCountryString = nonPrimarySubjectCountryString
								+ subjectDetailObject.getSubjectName() + "("
								+ subjectDetailObject.getCountryVO().getCountry() + "),";
					}
				}

				nonPrimarySubjectCountryString = !"".equals(nonPrimarySubjectCountryString)
						? nonPrimarySubjectCountryString.substring(0, nonPrimarySubjectCountryString.length() - 1)
						: "";
				caseDetailObject.setOtherSubjectNameCountryMap(nonPrimarySubjectCountryString);
			}
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("associateCaseList size is:::::" + associateCaseList.size());
		return associateCaseList;
	}

	public SubjectDetails addNewSubject(SubjectDetails subjectDetail, Session session, String caseHistoryPerformer)
			throws CMSException {
		this.logger.debug("Inside addNewSubject method of SubjectManager class");
		this.logger.debug("crn is " + subjectDetail.getCrn());
		String countryName = this.subjectDAO.getCountryFromCode(subjectDetail.getCountryCode());
		SubjectDetails addSubjectDetails = new SubjectDetails();
		subjectDetail.setCountryName(countryName);
		List activeREIdsList = this.subjectDAO.getActiveREIds();
		int newGeneratedSubjectID = 0;

		try {
			String[] modifiedRecords = subjectDetail.getModifiedRecords();

			for (int i = 0; i < modifiedRecords.length; ++i) {
				List associateCasesList = new ArrayList();
				List isLegacyList = new ArrayList();
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				String reIDString = (String) jsonObject.get(this.reString);
				this.logger.debug("reIDString is::::::" + reIDString);
				String associateCRNString = (String) jsonObject.get("associateCRNString");
				String isLegacyString = (String) jsonObject.get("isLegacy");
				if (associateCRNString != null && !associateCRNString.equals("")) {
					associateCasesList = this.convertCommaStringToList(associateCRNString);
				}

				if (isLegacyString != null && !isLegacyString.equals("")) {
					isLegacyList = this.convertCommaStringToList(isLegacyString);
				}

				StringTokenizer riskTokenizer1 = new StringTokenizer(reIDString, ",");
				String completREIDString = "";

				while (riskTokenizer1.hasMoreElements()) {
					String token = riskTokenizer1.nextElement().toString();
					if (token.endsWith(":true")) {
						String riskID = token.substring(0, token.indexOf(58));
						if (activeREIdsList != null && activeREIdsList.size() > 0
								&& activeREIdsList.contains(new Integer(riskID)) && !riskID.equalsIgnoreCase("None")) {
							completREIDString = completREIDString + riskID + ",";
						}
					}
				}

				if (!completREIDString.equals("")) {
					completREIDString = completREIDString.substring(0, completREIDString.length() - 1);
				}

				this.logger.debug("completREIDString is:::::" + completREIDString);
				subjectDetail.setReIds(completREIDString);
				subjectDetail.setReportSubjectName(subjectDetail.getSubjectName());
				this.logger.debug("is primasy subject in vo is" + subjectDetail.isPrimarySub());
				long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(subjectDetail.getCrn());
				String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid,
						"ProcessCycle", session);
				subjectDetail.setSubjectId(newGeneratedSubjectID);
				Map reMap = this.subjectDAO.getReMap();
				subjectDetail.setReNames(this.reNameIDMap(subjectDetail.getReIds(), reMap));
				newGeneratedSubjectID = this.subjectDAO.generateIdForNewSubject();
				addSubjectDetails.setNewSubjectId(newGeneratedSubjectID);
				this.logger.debug("newGeneratedSubjectID value is :: " + newGeneratedSubjectID);
				subjectDetail.setSubjectId(newGeneratedSubjectID);
				int isISIS = this.subjectDAO.getISISCaseFlag(subjectDetail.getCrn());
				boolean pingSuccessful = true;
				boolean processFlag = true;
				String isisSubjectId = null;
				String subjectNotificationMessage;
				if (isISIS == 1) {
					pingSuccessful = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
					this.logger.debug("pingSuccessful :: " + pingSuccessful);
					if (!pingSuccessful) {
						addSubjectDetails.setNewSubjectId(-1);
						return addSubjectDetails;
					}

					ClientSubjectVO clientSubjectVO = this.setISISVOForAddSubject(subjectDetail);
					Map resultMap = ResourceLocator.self().getAtlasWebServiceClient().updateSubject(clientSubjectVO);
					this.logger.debug("resultMap value is:::::::" + resultMap);
					int successFlag = (Integer) resultMap.get("successFlag");
					int communicationErrorFlag = (Integer) resultMap.get("communicationErrorFlag");
					isisSubjectId = (String) resultMap.get("isisSubjectId");
					subjectNotificationMessage = (String) resultMap.get("message");
					addSubjectDetails.setIsisErrorMessge(subjectNotificationMessage);
					subjectDetail.setIsisSubjectId(isisSubjectId);
					this.logger.debug("successFlag value is::::::" + successFlag);
					this.logger.debug("communicationErrorFlag value is::::" + communicationErrorFlag);
					if (successFlag == 1) {
						processFlag = true;
					} else {
						processFlag = false;
					}
				}

				this.logger.debug("processFlag :: " + processFlag);
				if (!pingSuccessful || !processFlag) {
					addSubjectDetails.setNewSubjectId(0);
					return addSubjectDetails;
				}

				this.subjectDAO.saveNewSubject(subjectDetail, (List) associateCasesList, (List) isLegacyList);
				this.logger.debug("subject added successfully....");
				SubjectLevelRiskProfileDetailsVO caseDetailsForProfile = this.subjectDAO
						.getCaseDetailsForRiskProfile(subjectDetail.getCrn());
				if (subjectDetail.getIsSubjLevelSubRptReq() == null) {
					subjectDetail.setIsSubjLevelSubRptReq("0");
				}

				this.logger.debug("subject added subjectDetail.getIsSubjLevelSubRptReq()...."
						+ subjectDetail.getIsSubjLevelSubRptReq());
				if (subjectDetail.getIsSubjLevelSubRptReq().trim().equalsIgnoreCase("1")) {
					this.logger.debug("subject added subjectDetail.getSubReportTypeId()...."
							+ subjectDetail.getSubReportTypeId());
					caseDetailsForProfile.setSubReportType(Long.parseLong(subjectDetail.getSubReportTypeId()));
				}

				this.subjectDAO.insertRiskProfileData(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.insertCaseLevelRiskCountryBreakDown(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.insertSubjectLevelRisk(caseDetailsForProfile, subjectDetail);
				CaseDetails caseDetails = new CaseDetails();
				caseDetails.setCrn(subjectDetail.getCrn());
				List<SubjectDetails> subjectList = new ArrayList();
				subjectList.add(subjectDetail);
				caseDetails.setSubjectList(subjectList);
				caseDetails.setUpdatedBy(subjectDetail.getUpdatedBy());
				CaseHistory caseHistoryOtherParam = new CaseHistory();
				caseHistoryOtherParam.setCRN(subjectDetail.getCrn());
				caseHistoryOtherParam.setPid(Long.toString(piid));
				caseHistoryOtherParam.setProcessCycle(processCycle);
				if (caseHistoryPerformer.equals("")) {
					caseHistoryOtherParam.setPerformer(subjectDetail.getUpdatedBy());
				} else {
					caseHistoryOtherParam.setPerformer(caseHistoryPerformer);
				}

				ResourceLocator.self().getCaseHistoryService().setCaseHistory(new CaseDetails(), caseDetails,
						caseHistoryOtherParam, "Subject");
				subjectNotificationMessage = "New Subject(s) added to the case(0). New subject(s) :".replace("0",
						subjectDetail.getCrn()) + subjectDetail.getSubjectName() + ".";
				this.logger.debug("subjectNotificationMessage is::" + subjectNotificationMessage);
				ResourceLocator.self().getNotificationService()
						.createSystemNotification(subjectNotificationMessage, subjectNotificationMessage, this
								.getNotificationUsersForSubject(subjectDetail.getCrn(), subjectDetail.getUpdatedBy()),
								subjectDetail.getCrn());
				this.logger.debug("bi manager value is" + subjectDetail.getSubjectAddedBIMgr() + "-----");
				if (subjectDetail.getSubjectAddedBIMgr() != null
						&& !subjectDetail.getSubjectAddedBIMgr().trim().equalsIgnoreCase("")) {
					this.logger.debug("---bi manager exist for added subject......");
					TeamDetails teamDetails = new TeamDetails();
					teamDetails.setManagerName(subjectDetail.getSubjectAddedBIMgr());
					List<TeamDetails> teamDetailsList = new ArrayList();
					teamDetailsList.add(teamDetails);
					caseDetails.setTeamList(teamDetailsList);
				}

				boolean isOfficeAssignmentDone = ResourceLocator.self().getOfficeAssignmentService()
						.checkOfficeAssignmentStatus(subjectDetail.getCrn());
				this.logger.debug("isOfficeAssignmentDone value is:::" + isOfficeAssignmentDone);
				if (isOfficeAssignmentDone) {
					this.logger.debug("office assignment doen going to call flow controller..." + caseDetails.getCrn());
					ResourceLocator.self().getFlowService().updateDS(session, caseDetails, "AddSubject");
				}

				if (subjectDetail.isPrimarySub()) {
					HashMap dsValues = new HashMap();
					dsValues.put("PrimarySubjectCountry", countryName);
					dsValues.put("PrimarySubject", subjectDetail.getSubjectName());
					ResourceLocator.self().getSBMService().updateDataSlots(session, piid, dsValues);
				}
			}
		} catch (NullPointerException var34) {
			throw new CMSException(this.logger, var34);
		} catch (Exception var35) {
			throw new CMSException(this.logger, var35);
		}

		this.logger.debug("newGeneratedSubjectID value is :: " + newGeneratedSubjectID);
		return addSubjectDetails;
	}

	private ClientSubjectVO setISISVOForAddSubject(SubjectDetails subjectDetails) {
		ClientSubjectVO clientSubjectVO = new ClientSubjectVO();
		clientSubjectVO.setAtlasSubjectID(subjectDetails.getSubjectId());
		clientSubjectVO.setCountry(subjectDetails.getCountryCode());
		clientSubjectVO.setCRN(subjectDetails.getCrn());
		clientSubjectVO.setOtherDetails(subjectDetails.getOtherDetails());
		clientSubjectVO.setPrimary(subjectDetails.isPrimarySub());
		clientSubjectVO.setSubjectName(subjectDetails.getSubjectName());
		clientSubjectVO.setSubjectPosition(subjectDetails.getPosition());
		clientSubjectVO.setSubjectREs(subjectDetails.getReIds());
		clientSubjectVO.setSubjectType(Integer.toString(subjectDetails.getEntityTypeId()));
		if (subjectDetails.getSubReportTypeId() == null
				|| subjectDetails.getSubReportTypeId().trim().equalsIgnoreCase("")) {
			subjectDetails.setSubReportTypeId("0");
		}

		clientSubjectVO.setSlSubreportID(Integer.parseInt(subjectDetails.getSubReportTypeId()));
		clientSubjectVO.setSlBudget(subjectDetails.getSlBudget());
		clientSubjectVO.setSlCurrency(subjectDetails.getSlCurrency());
		clientSubjectVO.setUpdateType("Insert");
		return clientSubjectVO;
	}

	public int addAssociateCaseForSubject(String subID, String crn, String currentUser, String isLegacy)
			throws CMSException {
		this.logger.debug("Inside addAssociateCaseForSubject method of SubjectManager class");
		this.logger.debug("subID is::::" + subID + ":::::crn is::::" + crn + "::::currentUser:::" + currentUser
				+ "::::::isLegacy::::::" + isLegacy);
		List crnList = new ArrayList();
		List isLegacyList = new ArrayList();
		if (crn != null) {
			crnList = this.convertCommaStringToList(crn);
		}

		if (isLegacy != null) {
			isLegacyList = this.convertCommaStringToList(isLegacy);
		}

		int totalrecordInserted = this.subjectDAO.addAssociateCaseForSubject(subID, (List) crnList, currentUser,
				(List) isLegacyList);
		return totalrecordInserted;
	}

	public int addColorForSubject(String subID, String crn) throws CMSException {
		SubjectColorVO colorVO = new SubjectColorVO();
		colorVO.setColor("green");
		colorVO.setCrn(crn);
		colorVO.setSubjectId(Integer.parseInt(subID));
		colorVO.setIsClicked(1);
		int flag = this.subjectDAO.addColorForSubject(colorVO);
		return flag;
	}

	public int getDisAssociatedSubjectCount(int subjectId) throws CMSException {
		int count = this.subjectDAO.getDisAssociatedSubjectCount(subjectId);
		return count;
	}

	public int getAssociatedSubjectCount(String subjectName, int subjectID, String crn) throws CMSException {
		int count = this.subjectDAO.getAssociatedSubjectCount(subjectName, subjectID, crn);
		return count;
	}

	public int getSubjectIdCount(String subjectId) throws CMSException {
		int count = this.subjectDAO.getSubjectIdCount(subjectId);
		return count;
	}

	public List getNotificationUsersForSubject(String crn, String currentUser) throws CMSException {
		this.logger.debug("inside getNotificationUsersForSubject of SubjectManager class");
		this.logger.debug("crn is:::::" + crn);
		this.logger.debug("currentUser is:::::" + currentUser);
		ArrayList userList = new ArrayList();

		try {
			new ArrayList();
			new ArrayList();
			new ArrayList();
			new ArrayList();
			new ArrayList();
			new ArrayList();
			String caseManager = "";
			caseManager = this.subjectDAO.getCaseManager(crn);
			if (currentUser != null && !currentUser.equalsIgnoreCase(caseManager)) {
				this.logger.debug(
						"case manager is not inititor... so notification will send to case manager.." + currentUser);
				userList.add(caseManager);
			}

			this.logger.debug("inside getNotificationUsersForSubject of SubjectManager class");
			List analystList = this.subjectDAO.getAnalystForSubjectNotification(crn, "1,2,4");
			Iterator iterator = analystList.iterator();

			while (true) {
				String managerName;
				do {
					do {
						do {
							if (!iterator.hasNext()) {
								this.logger.debug("inside getNotificationUsersForSubject of SubjectManager class");
								List reviewersList = this.subjectDAO.getReviewersForSubjectNotification(crn);
								iterator = reviewersList.iterator();

								while (true) {
									do {
										do {
											do {
												if (!iterator.hasNext()) {
													this.logger.debug(
															"inside getNotificationUsersForSubject of SubjectManager class");
													List managerList = this.subjectDAO
															.getManagerForSubjectNotification(crn, "3");
													iterator = managerList.iterator();

													while (true) {
														do {
															do {
																do {
																	if (!iterator.hasNext()) {
																		this.logger
																				.debug("userList is:::::" + userList);
																		this.logger.debug("userList size is:::::"
																				+ userList.size());
																		return userList;
																	}

																	managerName = (String) iterator.next();
																} while (managerName == null);
															} while (userList.contains(managerName));
														} while (currentUser.equalsIgnoreCase(managerName)
																&& managerName.equalsIgnoreCase(caseManager));

														userList.add(managerName);
														this.logger.debug("managerName ::" + managerName);
													}
												}

												managerName = (String) iterator.next();
											} while (managerName == null);
										} while (userList.contains(managerName));
									} while (currentUser.equalsIgnoreCase(managerName)
											&& managerName.equalsIgnoreCase(caseManager));

									userList.add(managerName);
									this.logger.debug("reviewersName ::" + managerName);
								}
							}

							managerName = (String) iterator.next();
						} while (managerName == null);
					} while (userList.contains(managerName));
				} while (currentUser.equalsIgnoreCase(managerName) && managerName.equalsIgnoreCase(caseManager));

				userList.add(managerName);
				this.logger.debug("analystName ::" + managerName);
			}
		} catch (NullPointerException var13) {
			throw new CMSException(this.logger, var13);
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		}
	}

	public boolean doReValidation(String reIDString, int subjectId) throws CMSException {
		this.logger.debug("inside doReValidation of SubjectManager class");
		boolean reUpdateFlag = false;

		try {
			StringTokenizer reTokenizer1 = new StringTokenizer(reIDString, ",");
			String completREIDString = "";

			String oldREString;
			while (reTokenizer1.hasMoreElements()) {
				oldREString = reTokenizer1.nextElement().toString();
				if (oldREString.endsWith(":true")) {
					String re = oldREString.substring(0, oldREString.indexOf(58));
					if (!re.equalsIgnoreCase("None")) {
						completREIDString = completREIDString + re + ",";
					}
				}
			}

			if (!completREIDString.equals("")) {
				completREIDString = completREIDString.substring(0, completREIDString.length() - 1);
			}

			oldREString = this.subjectDAO.getReIdsForSubject(subjectId);
			reUpdateFlag = this.getReChangeFlag(oldREString, completREIDString);
			return reUpdateFlag;
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private boolean getReChangeFlag(String oldReIds, String newReIds) throws CMSException {
		this.logger.debug("inside getReChangeFlag of SubjectManager class");
		this.logger.debug("oldReIds is:::::" + oldReIds);
		this.logger.debug("newReIds is:::::" + newReIds);
		boolean reupdateFlag = false;

		try {
			List oldREList = this.convertCommaStringToList(oldReIds);
			List newREList = this.convertCommaStringToList(newReIds);
			if (oldREList.size() != newREList.size()) {
				this.logger.debug("inside if...");
				reupdateFlag = true;
			} else {
				this.logger.debug("inside else...");
				if (!oldREList.containsAll(newREList)) {
					this.logger.debug("inside if of  else...");
					reupdateFlag = true;
				}
			}
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("reupdateFlag flag value is::::" + reupdateFlag);
		return reupdateFlag;
	}

	public List getCountryListForSubject() throws CMSException {
      this.logger.debug("inside getCountryListForSubject of SubjectManager class");
      List<CountryMasterVO> finalList = new ArrayList();
      List<CountryMasterVO> countryList = ResourceLocator.self().getCacheService().getCacheItemsList("COUNTRY_MASTER");
      List<CountryMasterVO> topTen = this.subjectDAO.getTopTenCountry();
      List<String> tempList = new ArrayList();
      Comparator<CountryMasterVO> comparator = new 1(this);
      new ArrayList();
      Collections.sort(countryList, comparator);

      try {
         Iterator iterator = topTen.iterator();

         while(iterator.hasNext()) {
            CountryMasterVO countryMasterVO = (CountryMasterVO)iterator.next();
            tempList.add(countryMasterVO.getCountry());
            finalList.add(countryMasterVO);
         }

         CountryMasterVO vo = new CountryMasterVO();
         vo.setCountry("=============");
         vo.setCountryCode("--");
         vo.setEnglishCountry("--");
         finalList.add(vo);
         Iterator iterator = countryList.iterator();

         while(iterator.hasNext()) {
            CountryMasterVO countryMasterVO = (CountryMasterVO)iterator.next();
            if (!tempList.contains(countryMasterVO.getCountry())) {
               finalList.add(countryMasterVO);
            }
         }
      } catch (Exception var10) {
         this.logger.error(var10);
      }

      return finalList;
   }

	public int getSubjectCount(String crn) throws CMSException {
		int count = this.subjectDAO.getSubjectCount(crn);
		return count;
	}

	public CaseDetails getSubjectRiskForCase(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getSubjectRiskForCase method of SubjectManager class");
		List subjectListWithReNames = new ArrayList();
		CaseDetails newCaseDetails = new CaseDetails();
		new CaseDetails();

		try {
			List<SubjectDetails> subjectList = new ArrayList();
			this.logger.debug("crn is " + crn);
			CaseDetails caseDetails = this.subjectDAO.getSubjectListForCRN(crn, sortColumnName, start, limit, sortType);
			new ArrayList();
			String caseStatus = ResourceLocator.self().getSubjectService().getCaseStatus(crn);
			this.logger.debug("Case status is:::::::::" + caseStatus);
			List subRiskMasterList;
			if (!caseStatus.equalsIgnoreCase("Completed") && !caseStatus.equalsIgnoreCase("Cancelled")) {
				subRiskMasterList = ResourceLocator.self().getCacheService().getCacheItemsList("RISK_MASTER");
			} else {
				subRiskMasterList = this.subjectDAO.getAllRisks();
			}

			if (caseDetails != null) {
				subjectList = caseDetails.getSubjectList();
			}

			if (subjectList != null && ((List) subjectList).size() > 0) {
				this.logger.debug("subject list size for this case is::::----" + ((List) subjectList).size());
				Iterator var13 = ((List) subjectList).iterator();

				while (var13.hasNext()) {
					SubjectDetails subjectDetail = (SubjectDetails) var13.next();
					new ArrayList();
					List<RisksMasterVO> subRiskList = this.subjectDAO.getSubjectRisk(subjectDetail.getSubjectId());
					this.logger.debug("RIsk list is:::::::" + subRiskList.size());
					List<RisksMasterVO> newSubRiskMasterList = new ArrayList();
					Iterator var18 = subRiskMasterList.iterator();

					while (var18.hasNext()) {
						RisksMasterVO risksMasterVO = (RisksMasterVO) var18.next();
						RisksMasterVO newRisksMasterVO = new RisksMasterVO();
						newRisksMasterVO.setRiskcode(risksMasterVO.getRiskcode());
						newRisksMasterVO.setRisks(risksMasterVO.getRisks());
						String riskCode = newRisksMasterVO.getRiskcode();
						Iterator var22 = subRiskList.iterator();

						while (var22.hasNext()) {
							RisksMasterVO risksMasterVO1 = (RisksMasterVO) var22.next();
							if (riskCode.equalsIgnoreCase(risksMasterVO1.getRiskcode())) {
								this.logger.debug(
										subjectDetail.getSubjectId() + "risk code for checked is::::" + riskCode);
								newRisksMasterVO.setRisksStatus("1");
							}
						}

						newSubRiskMasterList.add(newRisksMasterVO);
					}

					subjectDetail.setSubjectRisk(newSubRiskMasterList);
					subjectListWithReNames.add(subjectDetail);
					this.logger.debug("subjectListWithReNames size" + subjectListWithReNames.size());
				}
			}

			newCaseDetails.setSubjectList(subjectListWithReNames);
			return newCaseDetails;
		} catch (NullPointerException var23) {
			throw new CMSException(this.logger, var23);
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}
	}

	public int getSubjectIndustryCount(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectIndustryCount method of SubjectManager class");
		int subjectIndustryCount = this.subjectDAO.getSubjectIndustryCount(crn);
		return subjectIndustryCount;
	}

	public int getMatchingAssociateCasesCount(String subjectName, String subjectID, String crn) throws CMSException {
		this.logger.debug("Inside getMatchingAssociateCasesCount method of SubjectManager class");
		int matchingAssociateCasesCount = this.subjectDAO.getMatchingAssociateCasesCount(subjectName, subjectID, crn);
		return matchingAssociateCasesCount;
	}

	public int getAsscciateCaseCount(String subjectID) throws CMSException {
		this.logger.debug("Inside getAsscciateCaseCount method of SubjectManager class");
		int asscciateCaseCount = this.subjectDAO.getAsscciateCaseCount(subjectID);
		return asscciateCaseCount;
	}

	public int getAddEditSubjectValidation(String crn, String subjectName, String countryName, String subjectID,
			String entityType) throws CMSException {
		this.logger.debug("Inside getAddEditSubjectValidation method of SubjectManager class");
		int validationCount = this.subjectDAO.getAddEditSubjectValidation(crn, subjectName, countryName, subjectID,
				entityType);
		return validationCount;
	}

	public long getPIIDForCRN(String crn) throws CMSException {
		this.logger.debug("inside getPIIDForCRN method of SubjectService class");
		long piid = this.subjectDAO.getPIIDForCRN(crn);
		return piid;
	}

	public boolean checkForBI(String crn, String reIDString) throws CMSException {
		this.logger.debug("inside check for bi........");
		Map reBiTeamMap = this.subjectDAO.getReBiTeamMap();
		this.logger.debug("reBiTeamMap is:::::::" + reBiTeamMap);
		boolean biTeamFlag = false;
		boolean isBIExist = false;
		boolean result = true;
		StringTokenizer reTokenizer = new StringTokenizer(reIDString, ",");
		String completREIDString = "";

		while (reTokenizer.hasMoreElements()) {
			String token = reTokenizer.nextElement().toString();
			if (token.endsWith(":true")) {
				String reId = token.substring(0, token.indexOf(58));
				String biTeam = (String) reBiTeamMap.get(reId);
				if (biTeam.equalsIgnoreCase("Yes")) {
					biTeamFlag = true;
				}

				if (!reId.equalsIgnoreCase("None")) {
					completREIDString = completREIDString + reId + ",";
				}
			}
		}

		this.logger.debug("biTeamFlag is::::::::::::" + biTeamFlag);
		if (biTeamFlag) {
			boolean officeAssignmentCompletionCheck = this.isOfficeAssignmentDone(crn);
			this.logger.debug("officeAssignmentCompletionCheck is::" + officeAssignmentCompletionCheck);
			if (officeAssignmentCompletionCheck) {
				isBIExist = this.isBITeamExist(crn);
				this.logger.debug("isBIExist is ::" + isBIExist);
				this.logger.debug("result is:::::::" + isBIExist);
				return isBIExist;
			} else {
				this.logger.debug("result is:::::::>>" + result);
				return result;
			}
		} else {
			this.logger.debug("result is:::::::>>>>>>>" + result);
			return result;
		}
	}

	public boolean isOfficeAssignmentDone(String crn) throws CMSException {
		this.logger.debug("Inside isOfficeAssignmentDone method of JSONSubjectController class" + crn);
		boolean isOfficeAssignmentFlag = false;

		try {
			isOfficeAssignmentFlag = ResourceLocator.self().getOfficeAssignmentService()
					.checkOfficeAssignmentStatus(crn);
			this.logger.debug("isOfficeAssignmentFlag value is::" + isOfficeAssignmentFlag);
			return isOfficeAssignmentFlag;
		} catch (NullPointerException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean isBITeamExist(String crn) throws CMSException {
		this.logger.debug("Inside isBITeamExist method of Subject Manager class");
		boolean isBITeamExistFlag = false;

		try {
			isBITeamExistFlag = ResourceLocator.self().getOfficeAssignmentService().isBITeamExistsForCase(crn);
			this.logger.debug("isBITeamExistFlag value is::" + isBITeamExistFlag);
			return isBITeamExistFlag;
		} catch (NullPointerException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Map getBiREStatus(String oldRetring, String newReString) throws CMSException {
		this.logger.debug("inside getBiREStatus of SubjectManager class");
		this.logger.debug("oldReIds is:::::" + oldRetring);
		this.logger.debug("newReIds is:::::" + newReString);
		boolean reupdateFlag = false;
		boolean biREAddedFlag = false;
		boolean biRERemovedFlag = false;
		Map statusMap = new HashMap();
		Map reBiTeamMap = this.subjectDAO.getReBiTeamMap();
		this.logger.debug("reBiTeamMap is:::" + reBiTeamMap);
		this.logger.debug("oldRetring::" + oldRetring + ":::newReString:::" + newReString);

		try {
			List oldREList = this.convertCommaStringToList(oldRetring);
			List newREList = this.convertCommaStringToList(newReString);
			new ArrayList();
			new ArrayList();
			Iterator iterator = newREList.iterator();

			String object;
			while (iterator.hasNext()) {
				object = (String) iterator.next();
				oldREList.remove(object);
			}

			List reRemovedList = oldREList;
			iterator = oldREList.iterator();

			String biTeam;
			while (iterator.hasNext()) {
				object = (String) iterator.next();
				this.logger.debug("object is:::::::" + object);
				biTeam = (String) reBiTeamMap.get(object);
				if (biTeam.equalsIgnoreCase("Yes")) {
					biRERemovedFlag = true;
				}
			}

			oldREList = this.convertCommaStringToList(oldRetring);
			iterator = oldREList.iterator();

			while (iterator.hasNext()) {
				object = (String) iterator.next();
				newREList.remove(object);
			}

			iterator = newREList.iterator();

			while (iterator.hasNext()) {
				object = (String) iterator.next();
				this.logger.debug("object is::::" + object);
				biTeam = (String) reBiTeamMap.get(object);
				if (biTeam.equalsIgnoreCase("Yes")) {
					biREAddedFlag = true;
				}
			}

			statusMap.put("biRERemovedFlag", biRERemovedFlag);
			statusMap.put("biREAddedFlag", biREAddedFlag);
			statusMap.put("reRemovedList", reRemovedList);
			statusMap.put("reAddedList", newREList);
		} catch (NullPointerException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}

		this.logger.debug("reupdateFlag flag value is::::" + reupdateFlag);
		return statusMap;
	}

	public List getupdateSubjectTeamDeleteInfo(String crn, String subjectID, String newReString) throws CMSException {
		String oldREString = this.subjectDAO.getReIdsForSubject(Integer.parseInt(subjectID));
		boolean primarySubjectDeleteFlag = false;
		List<String> teamNameList = new ArrayList();
		StringTokenizer reTokenizer = new StringTokenizer(newReString, ",");
		String completREIDString = "";

		while (reTokenizer.hasMoreElements()) {
			String token = reTokenizer.nextElement().toString();
			if (token.endsWith(":true")) {
				String reId = token.substring(0, token.indexOf(58));
				completREIDString = completREIDString + reId + ",";
			}
		}

		if (!completREIDString.equals("")) {
			completREIDString = completREIDString.substring(0, completREIDString.length() - 1);
		}

		this.logger.debug(oldREString + "::::::::::::::::::::::" + completREIDString);
		Map statusMap = this.getBiREStatus(oldREString, completREIDString);
		this.logger.debug("statusMap is:::" + statusMap);
		List reRemovedList = (List) statusMap.get("reRemovedList");
		String reRemovedListString = "0";
		if (reRemovedList.size() > 0) {
			reRemovedListString = this.listToCommaSeparatedString(reRemovedList);
		}

		this.logger.debug("---->>>>>>>>>>>>>>>>>>>>>>>>>>>");
		this.logger.debug("crn:::::::" + crn + "::::::subjectID:::::" + subjectID
				+ "::::::::::::::reRemovedListString:::::" + reRemovedListString);
		new ArrayList();
		List teamList = this.subjectDAO.getUpdateSubjectREValidationForTeam(crn, subjectID, reRemovedListString);
		String teamName = "";
		String alertMess = "";
		if (teamList != null && teamList.size() > 0) {
			Iterator iterator = teamList.iterator();

			while (iterator.hasNext()) {
				TeamDetailsVO object = (TeamDetailsVO) iterator.next();
				String teamName1 = object.getTeamType();
				if (teamName1.equalsIgnoreCase("Primary")) {
					primarySubjectDeleteFlag = true;
				}

				teamNameList.add(teamName1);
				teamName = teamName + teamName1;
				if (iterator.hasNext()) {
					teamName = teamName + ",";
				}

				if (!"".equals(teamName)) {
					(new StringBuilder(
							"Your action causes deletion of some teams, so please remove these teams from office section "))
									.append(teamName).toString();
				}
			}
		}

		return teamNameList;
	}

	public String getUpdateSubjectBITeamInfo(String crn, String subjectID, String newReString) throws CMSException {
		this.logger.debug("Going to call getUpdateSubjectBITeamInfo.......");
		String oldREString = this.subjectDAO.getReIdsForSubject(Integer.parseInt(subjectID));
		StringTokenizer reTokenizer = new StringTokenizer(newReString, ",");
		String completREIDString = "";

		while (reTokenizer.hasMoreElements()) {
			String token = reTokenizer.nextElement().toString();
			if (token.endsWith(":true")) {
				String reId = token.substring(0, token.indexOf(58));
				completREIDString = completREIDString + reId + ",";
			}
		}

		if (!completREIDString.equals("")) {
			completREIDString = completREIDString.substring(0, completREIDString.length() - 1);
		}

		this.logger.debug("oldREString::::::" + oldREString);
		this.logger.debug("completREIDString" + completREIDString);
		Map statusMap = this.getBiREStatus(oldREString, completREIDString);
		this.logger.debug("statusMap is:::::::::::::::::::::::::::" + statusMap);
		List reRemovedList = (List) statusMap.get("reRemovedList");
		String reRemovedListString = "0";
		if (reRemovedList.size() > 0) {
			reRemovedListString = this.listToCommaSeparatedString(reRemovedList);
		}

		boolean biRERemovedFlag = (Boolean) statusMap.get("biRERemovedFlag");
		boolean biREAddedFlag = (Boolean) statusMap.get("biREAddedFlag");
		String biResult = "";
		boolean isBIExist = false;
		if (biREAddedFlag && biRERemovedFlag) {
			this.logger.debug(":::::both flags r true::::no change condition");
			biResult = "NoChange";
		}

		if (biREAddedFlag && !biRERemovedFlag) {
			this.logger.debug("biREAddedFlag is:::" + biREAddedFlag + "::::::biRERemovedFlag" + biRERemovedFlag);
			boolean officeAssignmentCompletionCheck = this.isOfficeAssignmentDone(crn);
			if (officeAssignmentCompletionCheck) {
				isBIExist = this.isBITeamExist(crn);
			} else {
				isBIExist = true;
			}

			if (isBIExist) {
				biResult = "NoChange";
			} else {
				biResult = "Add";
			}
		}

		if (!biREAddedFlag && biRERemovedFlag) {
			new ArrayList();
			List teamList = this.subjectDAO.getUpdateSubjectBIREValidationForTeam(crn, subjectID, reRemovedListString);
			this.logger.debug("size of teamList is::::" + teamList.size());
			this.logger.debug("biREAddedFlag is:::" + biREAddedFlag + "::::::biRERemovedFlag" + biRERemovedFlag);
			boolean officeAssignmentCompletionCheck = this.isOfficeAssignmentDone(crn);
			if (officeAssignmentCompletionCheck) {
				isBIExist = this.isBITeamExist(crn);
			} else {
				isBIExist = false;
			}

			if (isBIExist) {
				if (teamList.size() > 0) {
					biResult = "Delete";
				} else {
					biResult = "NoChange";
				}
			} else {
				biResult = "NoChange";
			}
		}

		if (!biREAddedFlag && !biRERemovedFlag) {
			this.logger.debug("both flags r false::::::biREAddedFlag is:::" + biREAddedFlag + "::::::biRERemovedFlag"
					+ biRERemovedFlag);
			biResult = "NoChange";
		}

		this.logger.debug("biResult is::::::::" + biResult);
		return biResult;
	}

	public String listToCommaSeparatedString(List<String> stringList) throws CMSException {
		String resultString = "";

		try {
			Iterator var4 = stringList.iterator();

			while (var4.hasNext()) {
				String string = (String) var4.next();
				if (resultString.length() == 0) {
					resultString = resultString + string;
				} else {
					resultString = resultString + "," + string;
				}
			}

			this.logger.debug("resultString :: " + resultString);
			return resultString;
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int addISISSubjectToCase(SubjectDetailsVO subjectDetailsVO, boolean biFlag, String biManagerName,
			SubjectDetails subDetails) throws CMSException {
		boolean var5 = false;

		try {
			this.logger.debug("addISISSubjectToCase::::getSLSubreportID:" + subjectDetailsVO.getSlSubreportID());
			this.logger.debug("addISISSubjectToCase :::getsLBudget:" + subjectDetailsVO.getSlBudget());
			int subjectID = this.subjectDAO.addISISSubjectToCase(subjectDetailsVO);
			if (subjectID > 0) {
				SubjectLevelRiskProfileDetailsVO caseDetailsForProfile = new SubjectLevelRiskProfileDetailsVO();
				caseDetailsForProfile.setClientCode(subDetails.getClientCode());
				caseDetailsForProfile.setReportType(Long.parseLong(subDetails.getReportTypeId()));
				caseDetailsForProfile.setSubReportType(Long.parseLong(subDetails.getSubReportTypeId()));
				subDetails.setReIds(subjectDetailsVO.getReIds());
				subDetails.setCountryCode(subjectDetailsVO.getCountryCode());
				subDetails.setEntityTypeId(subjectDetailsVO.getEntityType());
				subDetails.setSubjectId(subjectDetailsVO.getSubjectId());
				this.subjectDAO.insertRiskProfileData(caseDetailsForProfile, subDetails);
				this.subjectDAO.insertCaseLevelRiskCountryBreakDown(caseDetailsForProfile, subDetails);
				this.subjectDAO.insertSubjectLevelRisk(caseDetailsForProfile, subDetails);
			}

			String countryName = this.subjectDAO.getCountryFromCode(subjectDetailsVO.getCountryCode());
			subjectDetailsVO.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			SubjectDetails subjectDetails = this.setSubjectDetails(subjectDetailsVO);
			subjectDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			subjectDetails.setCountryName(countryName);
			long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(subjectDetails.getCrn());
			Session session = ResourceLocator.self().getSBMService()
					.getSession(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					session);
			subjectDetails.setSubjectId(subjectID);
			Map reMap = this.subjectDAO.getReMap();
			subjectDetails.setReNames(this.reNameIDMap(subjectDetails.getReIds(), reMap));
			CaseDetails caseDetails = new CaseDetails();
			caseDetails.setCrn(subjectDetails.getCrn());
			caseDetails.setIsISIS(1);
			List<SubjectDetails> subjectList = new ArrayList();
			subjectList.add(subjectDetails);
			caseDetails.setSubjectList(subjectList);
			caseDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			CaseHistory caseHistoryOtherParam = new CaseHistory();
			caseHistoryOtherParam.setCRN(subjectDetailsVO.getCrn());
			caseHistoryOtherParam.setPid(Long.toString(piid));
			caseHistoryOtherParam.setProcessCycle(processCycle);
			caseHistoryOtherParam.setPerformer(subjectDetailsVO.getUpdatedBy());
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(new CaseDetails(), caseDetails,
					caseHistoryOtherParam, "Subject");
			String subjectNotificationMessage = "New Subject(s) added to the case(0). New subject(s) :".replace("0",
					subjectDetailsVO.getCrn()) + subjectDetailsVO.getSubjectName() + ".";
			this.logger.debug("subjectNotificationMessage is::" + subjectNotificationMessage);
			ResourceLocator.self().getNotificationService()
					.createSystemNotification(subjectNotificationMessage, subjectNotificationMessage,
							this.getNotificationUsersForSubject(subjectDetailsVO.getCrn(),
									this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId()),
							subjectDetailsVO.getCrn());
			if (!biFlag) {
				this.logger.debug("---bi manager exist for added subject..........");
				this.logger.debug("BI Manager name is:::.........." + biManagerName);
				TeamDetails teamDetails = new TeamDetails();
				teamDetails.setManagerName(biManagerName);
				List<TeamDetails> teamDetailsList = new ArrayList();
				teamDetailsList.add(teamDetails);
				caseDetails.setTeamList(teamDetailsList);
			}

			boolean isOfficeAssignmentDone = ResourceLocator.self().getOfficeAssignmentService()
					.checkOfficeAssignmentStatus(subjectDetailsVO.getCrn());
			this.logger.debug("isOfficeAssignmentDone value is:::" + isOfficeAssignmentDone);
			if (isOfficeAssignmentDone) {
				this.logger.debug("office assignment doen going to call flow controller..." + caseDetails.getCrn());
				ResourceLocator.self().getFlowService().updateDS(session, caseDetails, "AddSubject");
			}

			if (subjectDetailsVO.isPrimarySubject()) {
				HashMap dsValues = new HashMap();
				dsValues.put("PrimarySubjectCountry", countryName);
				dsValues.put("PrimarySubject", subjectDetailsVO.getSubjectName());
				ResourceLocator.self().getSBMService().updateDataSlots(session, piid, dsValues);
			}

			ResourceLocator.self().getSBMService().getEmailId(this.subjectDAO.caseManagerId(subjectDetailsVO.getCrn()));
			return subjectID;
		} catch (NullPointerException var19) {
			throw new CMSException(this.logger, var19);
		} catch (IllegalArgumentException var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public int updateISISSubjectToCase(SubjectDetailsVO subjectDetailsVO, String BiStatus, List teamList,
			SubjectDetails subDetails) throws CMSException {
		boolean var5 = false;

		try {
			String countryName = this.subjectDAO.getCountryFromCode(subjectDetailsVO.getCountryCode());
			SubjectDetails oldSubjectDetails = this.subjectDAO
					.getISISSubjectDetails(subjectDetailsVO.getIsisSubjectID());
			oldSubjectDetails.setEntityName(oldSubjectDetails.getEntityVO().getEntityType());
			this.logger.debug("Old entity type is::::" + oldSubjectDetails.getEntityName());
			SubjectDetails subjectDetail = this.setSubjectDetails(subjectDetailsVO);
			subjectDetail.setCountryName(countryName);
			subjectDetail.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(subjectDetail.getCrn());
			Session session = ResourceLocator.self().getSBMService()
					.getSession(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					session);
			String newEntityType = subjectDetail.getEntityTypeId() == 1 ? "Individual" : "Company";
			subjectDetail.setEntityName(newEntityType);
			this.logger.debug("New entity type is::::" + subjectDetail.getEntityName());
			Map reMap = this.subjectDAO.getReMap();
			subjectDetail.setReNames(this.reNameIDMap(subjectDetail.getReIds(), reMap));
			CaseDetails caseHistoryNewCaseDetails = new CaseDetails();
			List caseHistoryNewSubjectList = new ArrayList();
			caseHistoryNewSubjectList.add(subjectDetail);
			caseHistoryNewCaseDetails.setSubjectList(caseHistoryNewSubjectList);
			CaseDetails caseHistoryOldCaseDetails = new CaseDetails();
			List caseHistoryOldSubjectList = new ArrayList();
			oldSubjectDetails.setReNames(this.reNameIDMap(oldSubjectDetails.getReIds(), reMap));
			oldSubjectDetails.setCountryName(oldSubjectDetails.getCountryVO().getCountry());
			caseHistoryOldSubjectList.add(oldSubjectDetails);
			caseHistoryOldCaseDetails.setSubjectList(caseHistoryOldSubjectList);
			CaseHistory caseHistoryOtherParam = new CaseHistory();
			caseHistoryOtherParam.setCRN(subjectDetail.getCrn());
			caseHistoryOtherParam.setPid(Long.toString(piid));
			caseHistoryOtherParam.setProcessCycle(processCycle);
			caseHistoryOtherParam.setPerformer(subjectDetail.getUpdatedBy());
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(caseHistoryOldCaseDetails,
					caseHistoryNewCaseDetails, caseHistoryOtherParam, "Subject");
			boolean reUpdateFlag = this.getReChangeFlag(oldSubjectDetails.getReIds(), subjectDetail.getReIds());
			String subjectNotificationMessage;
			CaseDetails deltaCaseDetails;
			if (reUpdateFlag) {
				this.logger.debug("reUpdateFlag is true going to send update notification.");
				subjectNotificationMessage = "Subject Research Element has been updated for the case(0). Please check it out. Updated Subject Name :"
						.replace("0", subjectDetail.getCrn()) + subjectDetail.getSubjectName() + ".";
				this.logger.debug("subjectNotificationMessage is::" + subjectNotificationMessage);
				ResourceLocator.self().getNotificationService().createSystemNotification(subjectNotificationMessage,
						subjectNotificationMessage,
						this.getNotificationUsersForSubject(subjectDetail.getCrn(),
								this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId()),
						subjectDetail.getCrn());
			} else {
				this.logger.debug("reUpdateFlag is true going to send update notification.");
				subjectNotificationMessage = "";
				deltaCaseDetails = new CaseDetails();
				subjectNotificationMessage = "Please note, Subject information has been updated for the case(0). Following are the details updated:(1)"
						.replace("0", subjectDetail.getCrn());
				subjectNotificationMessage = subjectNotificationMessage + " Updated details are as follows : " + "\n";
				if (!oldSubjectDetails.getSubjectName().equals(subjectDetail.getSubjectName())) {
					this.logger.debug("subjectDetail.getSubjectName::::" + subjectDetail.getSubjectName());
					subjectNotificationMessage = subjectNotificationMessage + "Subject Name:"
							+ subjectDetail.getSubjectName() + "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (!oldSubjectDetails.getCountryVO().getCountry().equals(subjectDetail.getCountryName())) {
					this.logger.debug("subjectDetail.getCountryName::::" + subjectDetail.getCountryName());
					subjectNotificationMessage = subjectNotificationMessage + "Country Name:"
							+ subjectDetail.getCountryName() + "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldSubjectDetails.getSubReportType() != null
						&& !oldSubjectDetails.getSubReportType().equals(subjectDetail.getSubReportType())) {
					this.logger.debug("subjectDetail.getOtherDetails::::" + subjectDetail.getOtherDetails());
					subjectNotificationMessage = subjectNotificationMessage + "Sub Report type :"
							+ subjectDetail.getSubReportType() + "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldSubjectDetails.getAddress() == null && subjectDetail.getAddress() != null
						&& !subjectDetail.getAddress().equals("") && subjectDetail.getAddress().length() > 0) {
					this.logger.debug(" In if subjectDetail.getAddress::::" + subjectDetail.getAddress());
					subjectNotificationMessage = subjectNotificationMessage + "Address:" + subjectDetail.getAddress()
							+ "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				} else if (oldSubjectDetails.getAddress() != null
						&& !oldSubjectDetails.getAddress().equals(subjectDetail.getAddress())) {
					this.logger.debug("in else ifsubjectDetail.getAddress::::" + subjectDetail.getAddress());
					subjectNotificationMessage = subjectNotificationMessage + "Address:" + subjectDetail.getAddress()
							+ "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldSubjectDetails.getPosition() == null && subjectDetail.getPosition() != null
						&& !subjectDetail.getPosition().equals("") && subjectDetail.getPosition().length() != 0) {
					this.logger.debug("In if subjectDetail.getPosition::::" + subjectDetail.getPosition());
					subjectNotificationMessage = subjectNotificationMessage + "Position:" + subjectDetail.getPosition()
							+ "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				} else if (oldSubjectDetails.getPosition() != null
						&& !oldSubjectDetails.getPosition().equals(subjectDetail.getPosition())) {
					this.logger.debug("in else if subjectDetail.getPosition::::" + subjectDetail.getPosition());
					subjectNotificationMessage = subjectNotificationMessage + "Position:" + subjectDetail.getPosition()
							+ "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (oldSubjectDetails.getOtherDetails() == null && subjectDetail.getOtherDetails() != null
						&& !subjectDetail.getOtherDetails().equals("")
						&& subjectDetail.getOtherDetails().length() != 0) {
					this.logger.debug("subjectDetail.getOtherDetails::::" + subjectDetail.getOtherDetails());
					subjectNotificationMessage = subjectNotificationMessage + "Other details:"
							+ subjectDetail.getOtherDetails() + "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				} else if (oldSubjectDetails.getOtherDetails() != null
						&& !oldSubjectDetails.getOtherDetails().equals(subjectDetail.getOtherDetails())) {
					this.logger.debug("subjectDetail.getOtherDetails::::" + subjectDetail.getOtherDetails());
					subjectNotificationMessage = subjectNotificationMessage + "Other details:"
							+ subjectDetail.getOtherDetails() + "\n";
					deltaCaseDetails.setResearchDueDateFlag(true);
				}

				if (deltaCaseDetails.isResearchDueDateFlag()) {
					this.logger.debug("subjectNotificationMessage is::" + subjectNotificationMessage);
					ResourceLocator.self().getNotificationService().createSystemNotification(subjectNotificationMessage,
							subjectNotificationMessage,
							this.getNotificationUsersForSubject(subjectDetail.getCrn(),
									this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId()),
							subjectDetail.getCrn());
				}
			}

			if (teamList != null && teamList.size() > 0) {
				ResourceLocator.self().getTeamAssignmentService().deleteTeamDetails(teamList);
			}

			this.logger.debug("BiStatus value is::::::-------->>>>>" + BiStatus);
			CaseDetails oldCaseDetails = null;
			if (BiStatus.equalsIgnoreCase("Delete")) {
				oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
						.deleteBITeamForCase(subjectDetail.getCrn());
				this.logger.debug("oldCaseDetails when deleting BI team in update Subject ::::: " + oldCaseDetails);
			}

			deltaCaseDetails = new CaseDetails();
			deltaCaseDetails.setCrn(subjectDetail.getCrn());
			deltaCaseDetails.setIsISIS(1);
			this.logger.debug("crn set for new case details is::::" + subjectDetail.getCrn());
			List<SubjectDetails> subjectList = new ArrayList();
			subjectDetail.setPullBackToResearch(true);
			subjectList.add(subjectDetail);
			deltaCaseDetails.setSubjectList(subjectList);
			deltaCaseDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			this.logger.debug("bi manager value is " + subjectDetail.getSubjectAddedBIMgr());
			if (BiStatus.equalsIgnoreCase("Add")) {
				this.logger.debug("inside adding bi mgr:::" + subjectDetail.getSubjectAddedBIMgr());
				TeamDetails teamDetails = new TeamDetails();
				teamDetails.setManagerName(subjectDetail.getSubjectAddedBIMgr());
				List<TeamDetails> teamDetailsList = new ArrayList();
				teamDetailsList.add(teamDetails);
				deltaCaseDetails.setTeamList(teamDetailsList);
			}

			if (oldCaseDetails == null) {
				oldCaseDetails = new CaseDetails();
			}

			oldCaseDetails.setCrn(subjectDetail.getCrn());
			oldCaseDetails.setIsISIS(1);
			this.logger.debug("crn set for old case details is::::" + subjectDetail.getCrn());
			List<SubjectDetails> oldSubjectList = new ArrayList();
			oldSubjectList.add(oldSubjectDetails);
			oldCaseDetails.setSubjectList(oldSubjectList);
			boolean isOfficeAssignmentDone = ResourceLocator.self().getOfficeAssignmentService()
					.checkOfficeAssignmentStatus(subjectDetail.getCrn());
			this.logger.debug("isOfficeAssignmentDone is::::" + isOfficeAssignmentDone);
			if (isOfficeAssignmentDone) {
				this.logger.debug("office assignment done... going to call flow controller.......");
				ResourceLocator.self().getFlowService().updateDSAndPushFlow(session, oldCaseDetails, deltaCaseDetails,
						"UpdateSubject");
			}

			this.logger.debug("After caling flow controller.....");
			this.logger.debug("Going to update Db for subject information....");
			int updateSubjectCount = this.subjectDAO.updateISISSubjectToCase(subjectDetailsVO);
			if (updateSubjectCount > 0) {
				SubjectLevelRiskProfileDetailsVO caseDetailsForProfile = new SubjectLevelRiskProfileDetailsVO();
				caseDetailsForProfile.setClientCode(subDetails.getClientCode());
				caseDetailsForProfile.setReportType(Long.parseLong(subDetails.getReportTypeId()));
				caseDetailsForProfile.setSubReportType(Long.parseLong(subDetails.getSubReportTypeId()));
				this.subjectDAO.updateCaseLevelRisk(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.updateCaseLevelRiskCountryBreakDown(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.updateSubjectLevelRisk(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.updateSubjectLevelRiskHasCntryBrkDown(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.updateSubjectLevelRiskAggregation(caseDetailsForProfile, subjectDetail);
				this.subjectDAO.updateCaseLevelRiskAggregation(caseDetailsForProfile, subjectDetail);
			}

			if (subjectDetail.isPrimarySub()) {
				HashMap dsValues = new HashMap();
				dsValues.put("PrimarySubjectCountry", countryName);
				dsValues.put("PrimarySubject", subjectDetail.getSubjectName());
				ResourceLocator.self().getSBMService().updateDataSlots(session, piid, dsValues);
			}

			return updateSubjectCount;
		} catch (NullPointerException var27) {
			throw new CMSException(this.logger, var27);
		} catch (IllegalArgumentException var28) {
			throw new CMSException(this.logger, var28);
		}
	}

	public int deleteISISSubjectToCase(String isisSubjectId, String crn, boolean biTeamDeleteFlag, List teamList)
			throws CMSException {
		int deleteResultCount = false;
		int deleteSubjectRisksCount = 0;
		int deleteCntryBrkDownRisksCount = 0;

		try {
			SubjectDetails subjectDetails = this.subjectDAO.getISISSubjectDetails(isisSubjectId);
			subjectDetails.setCrn(crn);
			subjectDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(subjectDetails.getCrn());
			this.logger.debug("deleteISISSubjectToCase office assignment PID is::::::" + piid);
			Session session = ResourceLocator.self().getSBMService()
					.getSession(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid, "ProcessCycle",
					session);
			Map reMap = this.subjectDAO.getReMap();
			subjectDetails.setReNames(this.reNameIDMap(subjectDetails.getReIds(), reMap));
			CaseHistory caseHistoryOtherParam = new CaseHistory();
			caseHistoryOtherParam.setCRN(subjectDetails.getCrn());
			caseHistoryOtherParam.setPid(Long.toString(piid));
			caseHistoryOtherParam.setProcessCycle(processCycle);
			caseHistoryOtherParam.setPerformer(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			CaseDetails caseHistoryOldCaseDetails = new CaseDetails();
			subjectDetails.setCountryName(subjectDetails.getCountryVO().getCountry());
			List caseHistoryOldSubjectList = new ArrayList();
			caseHistoryOldSubjectList.add(subjectDetails);
			caseHistoryOldCaseDetails.setSubjectList(caseHistoryOldSubjectList);
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(caseHistoryOldCaseDetails, new CaseDetails(),
					caseHistoryOtherParam, "Subject");
			String subjectNotificationMessage = "Subject(s) deleted from the case(0). Deleted Subject(s) :".replace("0",
					crn) + subjectDetails.getSubjectName() + ".";
			this.logger.debug("subjectNotificationMessage is::::::" + subjectNotificationMessage);
			ResourceLocator.self().getNotificationService().createSystemNotification(subjectNotificationMessage,
					subjectNotificationMessage, this.getNotificationUsersForSubject(crn,
							this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId()),
					crn);
			CaseDetails oldCaseDetails = null;
			if (biTeamDeleteFlag) {
				oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService().deleteBITeamForCase(crn);
				this.logger.debug("deleteBiTeamResult is.." + oldCaseDetails);
			}

			HashMap<String, CaseDetails> deleteMap = ResourceLocator.self().getOfficeAssignmentService()
					.deleteOfficeDetailsForSubject(subjectDetails);
			if (teamList != null && teamList.size() > 0) {
				ResourceLocator.self().getTeamAssignmentService().deleteTeamDetails(teamList);
			}

			boolean isOfficeAssignmentDone = ResourceLocator.self().getOfficeAssignmentService()
					.checkOfficeAssignmentStatus(crn);
			this.logger.debug("isOfficeAssignmentDone value for delete subject is:::::" + isOfficeAssignmentDone);
			if (isOfficeAssignmentDone) {
				this.logger.debug("going to call flow controller for delete subject...");
				if (oldCaseDetails == null) {
					oldCaseDetails = (CaseDetails) deleteMap.get("oldCaseDetails");
				}

				List subjectList = new ArrayList();
				subjectList.add(subjectDetails);
				oldCaseDetails.setSubjectList(subjectList);
				oldCaseDetails.setIsISIS(1);
				new CaseDetails();
				CaseDetails newCaseDetails = (CaseDetails) deleteMap.get("newCaseDetails");
				newCaseDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
				newCaseDetails.setIsISIS(1);
				ResourceLocator.self().getFlowService().updateDSAndPushFlow(session, oldCaseDetails, newCaseDetails,
						"DeleteSubject");
			}

			List<DeleteSubjectLevelRiskVO> entriesNeedtoBeDeletedList = this.subjectDAO
					.getISISRiskDetailsNeedToBeDelete(subjectDetails.getSubjectId());
			List<DeleteSubjectLevelRiskVO> cntryBrkDownEntriesNeedtoBeDeletedList = this.subjectDAO
					.getCntryBrkDownISISRiskDetailsNeedToBeDelete(subjectDetails.getSubjectId());
			SubjectLevelRiskProfileDetailsVO caseDetailsForProfile = this.subjectDAO.getCaseDetailsForRiskProfile(crn);
			if (cntryBrkDownEntriesNeedtoBeDeletedList.size() > 0) {
				deleteCntryBrkDownRisksCount = this.subjectDAO
						.deleteSubjectLevelRisksCntryBrkDown(cntryBrkDownEntriesNeedtoBeDeletedList);
			}

			if (deleteCntryBrkDownRisksCount > 0) {
				this.subjectDAO
						.insertDeletedSubjectLevelCntryBrkDownRisksHistory(cntryBrkDownEntriesNeedtoBeDeletedList);
			}

			if (entriesNeedtoBeDeletedList.size() > 0) {
				deleteSubjectRisksCount = this.subjectDAO
						.deleteISISRiskDetailsAssociatedToSubjects(subjectDetails.getSubjectId());
			}

			if (deleteSubjectRisksCount > 0) {
				this.subjectDAO.insertDeletedSubjectLevelRisksHistory(entriesNeedtoBeDeletedList);
			}

			this.subjectDAO.deleteISISCaseLevelRiskCountryBreakDown(caseDetailsForProfile, crn,
					subjectDetails.getSubjectId());
			this.subjectDAO.deleteISISCaseLevelRisk(caseDetailsForProfile, crn, subjectDetails.getSubjectId());
			this.subjectDAO.deleteISISCaseLevelRiskAggregation(caseDetailsForProfile, crn,
					subjectDetails.getSubjectId());
			int deleteResultCount = this.subjectDAO.deleteISISSubjectToCase(isisSubjectId);
			return deleteResultCount;
		} catch (NullPointerException var24) {
			throw new CMSException(this.logger, var24);
		} catch (IllegalArgumentException var25) {
			throw new CMSException(this.logger, var25);
		}
	}

	private SubjectDetails setSubjectDetails(SubjectDetailsVO subjectDetailsVO) throws CMSException {
		SubjectDetails subjectDetails = new SubjectDetails();
		subjectDetails.setSubjectName(subjectDetailsVO.getSubjectName());
		subjectDetails.setSubjectId(subjectDetailsVO.getSubjectId());
		subjectDetails.setCountryCode(subjectDetailsVO.getCountryCode());
		subjectDetails.setCrn(subjectDetailsVO.getCrn());
		subjectDetails.setOtherDetails(subjectDetailsVO.getOtherDetails());
		subjectDetails.setPrimarySub(subjectDetailsVO.isPrimarySubject());
		subjectDetails.setAddress(subjectDetailsVO.getAddress());
		subjectDetails.setPosition(subjectDetailsVO.getSubjectPosition());
		subjectDetails.setReIds(subjectDetailsVO.getReIds());
		subjectDetails.setEntityTypeId(subjectDetailsVO.getEntityType());
		subjectDetails.setAddress(subjectDetailsVO.getAddress());
		this.logger.debug("inside setSubjectDetails method..subjectDetailsVO.getSLSubreportID()::"
				+ subjectDetailsVO.getSlSubreportID());
		if (subjectDetailsVO.getSlSubreportID() > 0) {
			this.logger.debug("inside setSubjectDetails method..subjectDetailsVO.getSLSubreportID() > 0 ::"
					+ (subjectDetailsVO.getSlSubreportID() > 0));
			subjectDetails.setSubReportType(
					this.idbUtilis.getSigleColumnDataFromTable("CMS_SUBREPORT_TYPE_MASTER", "SUBREPORT_TYPE_MASTER_ID",
							"SUB_REPORT", "" + subjectDetailsVO.getSlSubreportID(), (String) null, (String) null));
			subjectDetails.setSubReportTypeId("" + subjectDetailsVO.getSlSubreportID());
		}

		return subjectDetails;
	}

	public List getLegacySubjectListData(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("inside getLegacySubjectListData method..");
		Map subjsctIdREMap = new HashMap();
		new ArrayList();
		new ArrayList();
		ArrayList subjectDetailsList3 = new ArrayList();

		try {
			List<SubjectDetails> subjectDetailsList1 = this.subjectDAO.getLegacyCaseSubjectListData(crn, sortColumnName,
					start, limit, sortType);
			this.logger.debug("subjectDetailsList1 size is" + subjectDetailsList1.size());
			List<SubjectDetails> subjectDetailsList2 = this.subjectDAO.getLegacySubjectResData(crn);
			this.logger.debug("subjectDetailsList2 size is" + subjectDetailsList2.size());
			Iterator iterator;
			SubjectDetails subjectDetails1;
			if (subjectDetailsList2 != null && subjectDetailsList2.size() != 0) {
				iterator = subjectDetailsList2.iterator();

				while (iterator.hasNext()) {
					subjectDetails1 = (SubjectDetails) iterator.next();
					subjsctIdREMap.put(subjectDetails1.getSubjectId(), subjectDetails1.getReIds());
					this.logger.debug("subjsctIdREMap is:::" + subjsctIdREMap);
				}
			}

			if (subjectDetailsList1 != null && subjectDetailsList1.size() != 0) {
				iterator = subjectDetailsList1.iterator();

				while (iterator.hasNext()) {
					subjectDetails1 = (SubjectDetails) iterator.next();
					subjectDetails1.setReIds((String) subjsctIdREMap.get(subjectDetails1.getSubjectId()));
					this.logger.debug("subjectDetails1.subjectId is::::" + subjectDetails1.getSubjectId()
							+ ":::::::::res are::" + subjectDetails1.getReIds());
					subjectDetailsList3.add(subjectDetails1);
				}
			}

			return subjectDetailsList3;
		} catch (NullPointerException var12) {
			throw new CMSException(this.logger, var12);
		} catch (IllegalArgumentException var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	public int getLegacyCaseSubjectCount(String crn) throws CMSException {
		return this.subjectDAO.getLegacyCaseSubjectCount(crn);
	}

	public int getLegacyPastRecordsCount(String crn, String subjectId) throws CMSException {
		return this.subjectDAO.getLegacyPastRecordsCount(crn, subjectId);
	}

	public List getLegacyPastRecords(String crn, String subjectId, String sortColumnName, int start, int limit,
			String sortType) throws CMSException {
		return this.subjectDAO.getLegacyPastRecords(crn, subjectId, sortColumnName, start, limit, sortType);
	}

	public List getCaseLevelRiskCategory(String crn) throws CMSException {
		return this.subjectDAO.getCaseLevelRiskCategory(crn);
	}

	public List getSubjectLevelRiskCategory(String crn) throws CMSException {
		return this.subjectDAO.getSubjectLevelRiskCategory(crn);
	}

	public List getSubjectLevelRiskCategory(String crn, String performer, String teamTypeId, String taskName,
			String teamName, String teamId) throws CMSException {
		return this.subjectDAO.getSubjectLevelRiskCategory(crn, performer, teamTypeId, taskName, teamName, teamId);
	}

	public Date getUpdateOnForSubject(int subjectId) throws CMSException {
		Date date = this.subjectDAO.getUpdateOnForSubject(subjectId);
		return date;
	}

	public boolean checkIfOfficeAssignmnetDone(String crn, Session session) throws CMSException {
		this.logger.debug("Inside checkIfOfficeAssignmnetDone method..........");
		this.logger.debug("Inside checkIfOfficeAssignmnetDone method.........." + crn);
		this.logger.debug("Inside checkIfOfficeAssignmnetDone method.........." + session);
		boolean officeAssignmentDoneFlag = false;

		try {
			long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
			String[] completeWSArrayString = ResourceLocator.self().getSBMService().getCompletedWSNames(pid, session);
			ArrayList<Long> pidList = null;
			if (completeWSArrayString.length > 0) {
				List<String> wsNames = Arrays.asList(completeWSArrayString);
				if (wsNames.contains("Office Assignment Task")) {
					officeAssignmentDoneFlag = true;
				}
			}
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		} catch (IllegalArgumentException var10) {
			throw new CMSException(this.logger, var10);
		}

		this.logger.debug("officeAssignmentDoneFlag is.........." + officeAssignmentDoneFlag);
		return officeAssignmentDoneFlag;
	}

	public ClientCaseStatusIndustryVO[] getSubjectsIndustryForISIS(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectsIndustryForISIS method..........");
		new ArrayList();
		ClientCaseStatusIndustryVO[] clientCaseStatusIndustryVOArray = (ClientCaseStatusIndustryVO[]) null;

		try {
			List<SubjectDetails> industryList = this.subjectDAO.getSubjectsIndustryForISIS(crn);
			if (industryList != null && industryList.size() > 0) {
				this.logger.debug("subject industry size is::::::::::" + industryList.size());
				clientCaseStatusIndustryVOArray = new ClientCaseStatusIndustryVO[industryList.size()];
				int index = 0;

				for (Iterator iterator = industryList.iterator(); iterator.hasNext(); ++index) {
					SubjectDetails subjectDetails = (SubjectDetails) iterator.next();
					ClientCaseStatusIndustryVO clientCaseStatusIndustryVO = new ClientCaseStatusIndustryVO();
					clientCaseStatusIndustryVO.setAtlasSubjectID(Integer.toString(subjectDetails.getSubjectId()));
					clientCaseStatusIndustryVO.setISISSubjectID(subjectDetails.getIsisSubjectId());
					clientCaseStatusIndustryVO.setIndustryID(subjectDetails.getIndustryCode());
					clientCaseStatusIndustryVO.setIndustryName(subjectDetails.getIndustryName());
					clientCaseStatusIndustryVOArray[index] = clientCaseStatusIndustryVO;
					this.logger.debug("AtlasSubjectID::::::::::" + clientCaseStatusIndustryVO.getAtlasSubjectID());
					this.logger.debug("ISISSubjectID::::::::::" + clientCaseStatusIndustryVO.getISISSubjectID());
					this.logger.debug("IndustryID:::::::::::::" + clientCaseStatusIndustryVO.getIndustryID());
					this.logger.debug("IndustryName:::::::::" + clientCaseStatusIndustryVO.getIndustryName());
				}
			}

			return clientCaseStatusIndustryVOArray;
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public ClientCaseStatusRiskVO[] getSubjectsRisksForISIS(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectsRisksForISIS method..........");
		new ArrayList();
		ClientCaseStatusRiskVO[] clientCaseStatusRiskVOArray = (ClientCaseStatusRiskVO[]) null;

		try {
			List<SubjectDetails> riskList = this.subjectDAO.getSubjectsRisksForISIS(crn);
			if (riskList != null && riskList.size() > 0) {
				this.logger.debug("subject size is::::::::::>>>>" + riskList.size());
				clientCaseStatusRiskVOArray = new ClientCaseStatusRiskVO[riskList.size()];
				int index = 0;

				for (Iterator iterator = riskList.iterator(); iterator.hasNext(); ++index) {
					SubjectDetails subjectDetails = (SubjectDetails) iterator.next();
					this.logger.debug("index value is::::::::::" + index);
					ClientCaseStatusRiskVO clientCaseStatusRiskVO = new ClientCaseStatusRiskVO();
					clientCaseStatusRiskVO.setAtlasSubjectID(Integer.toString(subjectDetails.getSubjectId()));
					clientCaseStatusRiskVO.setISISSubjectID(subjectDetails.getIsisSubjectId());
					List subjecRiskList = subjectDetails.getSubjectRisk();
					ClientStatusRiskVO[] clientStatusRiskVOArray = (ClientStatusRiskVO[]) null;
					if (subjecRiskList != null && subjecRiskList.size() > 0) {
						this.logger.debug("risk size is::::::::::" + subjecRiskList.size());
						clientStatusRiskVOArray = new ClientStatusRiskVO[subjecRiskList.size()];
						int index1 = 0;

						for (Iterator iterator2 = subjecRiskList.iterator(); iterator2.hasNext(); ++index1) {
							this.logger.debug("index1 value is::::::::" + index1);
							RisksMasterVO risksMasterVO = (RisksMasterVO) iterator2.next();
							ClientStatusRiskVO clientStatusRiskVO = new ClientStatusRiskVO();
							clientStatusRiskVO.setRiskID(risksMasterVO.getRiskcode());
							clientStatusRiskVO.setRiskName(risksMasterVO.getRisks());
							clientStatusRiskVO.setPossibleRisk("1");
							this.logger.debug("RiskID:::::::::::::" + clientStatusRiskVO.getRiskID());
							this.logger.debug("RiskName:::::::::" + clientStatusRiskVO.getRiskName());
							this.logger.debug("PossibleRisk:::::::::::::" + clientStatusRiskVO.getPossibleRisk());
							clientStatusRiskVOArray[index1] = clientStatusRiskVO;
						}
					}

					clientCaseStatusRiskVO.setRisk(clientStatusRiskVOArray);
					clientCaseStatusRiskVOArray[index] = clientCaseStatusRiskVO;
				}
			}

			return clientCaseStatusRiskVOArray;
		} catch (NullPointerException var14) {
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	public void mergeAndInsertSubjectRisks(List<SubjectDetails> subjectDetailsList) throws CMSException {
		this.logger.debug("Inside mergeAndInsertSubjectRisks method of SubjectManager class");
		ArrayList subjectRiskDetailsList = new ArrayList();

		try {
			if (subjectDetailsList != null && subjectDetailsList.size() > 0) {
				Iterator iterator = subjectDetailsList.iterator();

				while (iterator.hasNext()) {
					SubjectDetails subjectDetails = (SubjectDetails) iterator.next();
					this.logger.debug("Subject Id is ::::::::" + subjectDetails.getSubjectId());
					List subjectRisks = this.subjectDAO.getRisksForSubject(subjectDetails.getSubjectId());
					SubjectDetails riskSubjectDetails = new SubjectDetails();
					List<RisksMasterVO> riskCodesList = new ArrayList();
					List<RisksMasterVO> riskMasterList = subjectDetails.getSubjectRisk();
					if (riskMasterList != null && riskMasterList.size() > 0) {
						Iterator iterator2 = riskMasterList.iterator();

						label52 : while (true) {
							while (true) {
								if (!iterator2.hasNext()) {
									break label52;
								}

								RisksMasterVO risksMasterVO = (RisksMasterVO) iterator2.next();
								if (subjectRisks != null && subjectRisks.size() > 0
										&& subjectRisks.contains(risksMasterVO.getRiskCode())) {
									this.logger.debug(risksMasterVO.getRiskCode() + " already exist with DB");
								} else {
									this.logger.debug("Risk Id is ::::" + risksMasterVO.getRiskCode());
									riskCodesList.add(risksMasterVO);
								}
							}
						}
					}

					if (riskCodesList != null && riskCodesList.size() > 0) {
						riskSubjectDetails.setSubjectId(subjectDetails.getSubjectId());
						riskSubjectDetails.setSubjectRisk(riskCodesList);
						riskSubjectDetails.setUpdatedBy(subjectDetails.getUpdatedBy());
						subjectRiskDetailsList.add(riskSubjectDetails);
					}
				}

				if (subjectRiskDetailsList != null && subjectRiskDetailsList.size() > 0) {
					this.subjectDAO.inserToSubjectRisk(subjectRiskDetailsList);
				}
			}

		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public String getCaseStatus(String crn) throws CMSException {
		this.logger.debug("Inside getCaseStatus method of SubjectManager class");
		return this.subjectDAO.getCaseStatus(crn);
	}

	public int saveColorDetails(String taskName, String crn, String color, String taskStatus) throws CMSException {
		try {
			int i = this.subjectDAO.saveColorDetails(taskName, crn, color, taskStatus);
			return i;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int getCRNCount(String crn) throws CMSException {
		int count = this.subjectDAO.getCRNCount(crn);
		return count;
	}

	public String isWatchListedSubject(String subjectName) throws CMSException {
		return this.subjectDAO.isWatchListedSubject(subjectName);
	}

	public List<String> getWatchListedSubject() throws CMSException {
		return this.subjectDAO.getWatchListedSubject();
	}

	public String caseManagerId(String crn) throws CMSException {
		return this.subjectDAO.caseManagerId(crn);
	}

	public boolean checkForAutoSubjectAddition(SubjectDetails subDetails) throws CMSException {
		boolean flag = true;
		String clientCode = subDetails.getCrn().split("\\\\")[1];
		String countryListForAutoAdd = this.subjectDAO.getCountryListForCode(clientCode);
		this.logger.debug("countryListForAutoAdd : " + countryListForAutoAdd.toString());
		String addedSubCountry = this.subjectDAO.getCountryFromCode(subDetails.getCountryCode());
		this.logger.debug("addedSubCountry : " + addedSubCountry.toString());
		String[] countryListArray = countryListForAutoAdd.split(",");
		String[] var10 = countryListArray;
		int var9 = countryListArray.length;

		for (int var8 = 0; var8 < var9; ++var8) {
			String str = var10[var8];
			this.logger.debug("comparing :" + str + "-" + addedSubCountry.toString());
			if (str.equalsIgnoreCase(addedSubCountry.toString())) {
				flag = false;
				this.logger.debug("Match found for country so no auto replication");
				break;
			}
		}

		this.logger.debug("flag is : " + flag);
		if (flag) {
			String[] reListForAutoAdd = this.subjectDAO.getREListForClientCode(clientCode).split(",");
			this.logger.debug("reListForAutoAdd : " + reListForAutoAdd);
			List activeREIdsList = this.subjectDAO.getActiveREIds();
			String[] modifiedRecords = subDetails.getModifiedRecords();

			label57 : for (int i = 0; i < modifiedRecords.length; ++i) {
				new ArrayList();
				new ArrayList();
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				String reIDString = (String) jsonObject.get(this.reString);
				this.logger.debug("checkForAutoSubjectAddition reIDString is::::::" + reIDString);
				StringTokenizer riskTokenizer1 = new StringTokenizer(reIDString, ",");
				String completREIDString = "";

				while (true) {
					String riskID;
					do {
						do {
							do {
								String token;
								do {
									if (!riskTokenizer1.hasMoreElements()) {
										continue label57;
									}

									token = riskTokenizer1.nextElement().toString();
								} while (!token.endsWith(":true"));

								riskID = token.substring(0, token.indexOf(58));
							} while (activeREIdsList == null);
						} while (activeREIdsList.size() <= 0);
					} while (!activeREIdsList.contains(new Integer(riskID)));

					this.logger.debug("riskID : " + riskID);
					String[] var23 = reListForAutoAdd;
					int var22 = reListForAutoAdd.length;

					for (int var21 = 0; var21 < var22; ++var21) {
						String str = var23[var21];
						if (str.equals(riskID)) {
							completREIDString = completREIDString + riskID + ",";
							this.logger.debug("Match found for RE- " + completREIDString);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public List<SubjectDetails> getListOfSubjectForReplication(SubjectDetails subDetails) throws CMSException {
		String clientCode = subDetails.getCrn().split("\\\\")[1];
		int entityTypeId = subDetails.getEntityTypeId();
		Map dataMap = new HashMap();
		dataMap.put("clientCode", clientCode);
		dataMap.put("entityTypeId", entityTypeId);
		return this.subjectDAO.getListOfSubjectForReplication(dataMap);
	}

	public SubjectDetails getReportTypeForCase(SubjectDetails subDetails) throws CMSException {
		subDetails = this.subjectDAO.getReportTypeForCase(subDetails);
		return subDetails;
	}

	public int updateSubjectBudget(String gridData, String crn) throws CMSException {
		this.logger.debug("In SubjectManager:updateSubjectBudget method");
		String strSubject = "";
		String strBudget = "";
		float newCaseFee = 0.0F;
		int result = 0;
		this.logger.debug("JSON format Grid data" + gridData);

		try {
			JSONArray jsonArray = new JSONArray(gridData);
			JSONObject jsonObject = null;
			new HashMap();

			for (int i = 0; i < jsonArray.length(); ++i) {
				jsonObject = jsonArray.getJSONObject(i);
				String slBud = jsonObject.get("slBudget") != null
						&& jsonObject.get("slBudget").toString().trim().length() != 0
								? jsonObject.get("slBudget").toString()
								: "0";
				newCaseFee += Float.parseFloat(slBud);
				if (jsonObject.get("isUpdated").toString().equalsIgnoreCase("true")) {
					if (strSubject.trim().length() == 0) {
						strSubject = jsonObject.get("subjectId").toString();
						strBudget = slBud;
					} else {
						strSubject = strSubject + "," + jsonObject.get("subjectId").toString();
						strBudget = strBudget + "," + slBud;
					}
				}
			}

			if (strSubject.trim().length() > 0) {
				result = this.subjectDAO.updateSubjectBudget(strSubject, strBudget);
				this.logger.debug("Subject Budget updated status ::" + result);
				this.logger.debug("New Case feee" + newCaseFee);
				long count = this.updateCaseFee(crn, String.valueOf(newCaseFee), this.isDeleteSubject);
				this.logger.debug("Update Case fee count" + count);
			}

			return result;
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public long updateCaseFee(String crn, String casefee, String deleteFlag) throws CMSException {
		long count = 0L;

		try {
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setCrn(crn);
			accountsVO.setCaseFee(casefee);
			if (deleteFlag.equalsIgnoreCase("true")) {
				accountsVO.setSubjectDeleteFlag("true");
			}

			count = this.invoiceMgmtDao.updateCaseFee(accountsVO);
			return count;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public long updateEDDOCaseFee(String crn, String casefee, boolean isBudgetDueDateConfirmed) throws CMSException {
		long count = 0L;

		try {
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setCrn(crn);
			accountsVO.setCaseFee(casefee);
			this.logger.debug("Inside updateEDDOCaseFee isBudgetDueDateConfirmed:" + isBudgetDueDateConfirmed);
			accountsVO.setIsBudgetDueDateConfirmed(isBudgetDueDateConfirmed);
			count = this.invoiceMgmtDao.updateEDDOCaseFee(accountsVO);
			return count;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<SubjectDetails> getSubjectDetailsForCRN(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectDetailsForCRN method of SubjectManager class");
		new ArrayList();

		try {
			this.logger.debug("crn is " + crn);
			List<SubjectDetails> subjectDetails = this.subjectDAO.getSubjectDetailsForCRN(crn);
			return subjectDetails;
		} catch (NullPointerException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public SubjectDetails getSlSubreportFlag(String crn) throws CMSException {
		this.logger.debug("Inside getSlSubreportFlagclass");
		this.logger.debug("crn  value is:::::" + crn);
		SubjectDetails subjectDetails = this.subjectDAO.getSlSubreportFlag(crn);
		this.logger.debug("Subject Level Subreport Flag:::::" + subjectDetails.getIsSubjLevelSubRptReq());
		return subjectDetails;
	}
}