package com.worldcheck.atlas.services.task;

import com.integrascreen.orders.Subject;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.audit.RiskHistoryDAO;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO;
import com.worldcheck.atlas.dao.task.TaskManagementDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import com.worldcheck.atlas.vo.document.DocMapVO;
import com.worldcheck.atlas.vo.masters.CategoryDetailsVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
import com.worldcheck.atlas.vo.masters.CountryHBDVO;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskAttributeVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import com.worldcheck.atlas.vo.task.CountryDBNamesVO;
import com.worldcheck.atlas.vo.task.CountryDatabaseVO;
import com.worldcheck.atlas.vo.task.CountrySubjectVO;
import com.worldcheck.atlas.vo.task.CustomTaskVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import com.worldcheck.atlas.vo.task.RecurrentCaseVO;
import com.worldcheck.atlas.vo.task.SubjectListForUserVO;
import com.worldcheck.atlas.vo.task.VendorForCrnDetailsVO;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskManagementService {
	private static final String TEAM_TYPE_ID = "teamTypeId";
	private static final String ACTIONUPDATE = "Update";
	private static final String BREAKLINE = "<br />";
	private static final String BOLDSTART = "<b>";
	private static final String BOLDEND = "</b>";
	private static final String TASK_NAME = "taskName";
	private static final String TEAM_NAME = "teamName";
	private static final String TEAM_ID = "teamId";
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.task.TaskManagementService");
	private static final String PERFORMER = "performer";
	private TaskManagementDAO taskDAO;
	private RiskHistoryDAO riskHistoryDAO = null;
	RiskProfileDAO riskProfileDAO = null;

	public void setTaskDAO(TaskManagementDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public void setRiskProfileDAO(RiskProfileDAO riskProfileDAO) {
		this.riskProfileDAO = riskProfileDAO;
	}

	public void setRiskHistoryDAO(RiskHistoryDAO riskHistoryDAO) {
		this.riskHistoryDAO = riskHistoryDAO;
	}

	public List<CountrySubjectVO> getAllCountries(String crn) throws CMSException {
		return this.taskDAO.getCntryList(crn);
	}

	public List<CountryDBNamesVO> getAllDBNamesForCountry(String countryId) throws CMSException {
		return this.taskDAO.getcntryDBNames(countryId);
	}

	public List<RiskAttributeVO> getRiskAttributeDefaultValues(List<Long> attributeIdsList) throws CMSException {
		return this.taskDAO.getRiskAttributeDefaultValues(attributeIdsList);
	}

	public List<MyTaskPageVO> getAllTeamAssignmentTasks(Map<String, String> mapOfParams) throws CMSException {
		return this.taskDAO.getAllTeamAssignmentTasks(mapOfParams);
	}

	public List<SubjectListForUserVO> getSubjectListForUser(String crn, String performer, String teamTypeId, int start,
			int limit) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("performer", performer);
		mapForParams.put("teamTypeId", teamTypeId);
		mapForParams.put("start", String.valueOf(start + 1));
		mapForParams.put("limit", String.valueOf(start + limit));
		List<SubjectListForUserVO> list = this.taskDAO.getSubjectList(mapForParams);
		this.logger.debug("list size is " + list.size());
		return list;
	}

	public List<SubjectListForUserVO> getREList(String crn, String performer, String subjectName, String subjectID,
			String teamId) throws CMSException {
		Map<String, Object> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("performer", performer);
		mapForParams.put("subjectName", subjectName);
		mapForParams.put("subjectID", Long.parseLong(subjectID));
		mapForParams.put("team_ID", Long.parseLong(teamId));
		List<SubjectListForUserVO> list = this.taskDAO.getREList(mapForParams);
		return list;
	}

	public List<UserMasterVO> getCaseManagerList() throws CMSException {
		return this.taskDAO.getCaseManagerList();
	}

	public String getISISOrderId(String crn) throws CMSException {
		return this.taskDAO.getISISOrderId(crn);
	}

	public String getCategoryLabel(long categoryId) throws CMSException {
		return this.taskDAO.getCategoryLabel(categoryId);
	}

	public List<ClientRequirmentMasterVO> getClientReqData(String clientName) throws CMSException {
		return this.taskDAO.getClientReqData(clientName);
	}

	public boolean isSubjectAdded(String crn) throws CMSException {
		return this.taskDAO.isSubAdded(crn);
	}

	public long getSeqForCRN(String reqRecDate) throws CMSException {
		int selected_year = Integer.parseInt(reqRecDate.split(" ")[0].split("-")[2]);
		this.logger.debug("selected_year is " + selected_year);
		return this.taskDAO.getSeqForCRN(selected_year);
	}

	public long getSeqForRecurrence() throws CMSException {
		return this.taskDAO.getSeqForRecurrence();
	}

	public String getInitialsForReport(String reportType) throws CMSException {
		return this.taskDAO.getInitialsForReport(reportType);
	}

	public String getInitialsForSubReport(String subReportType, String reportType) throws CMSException {
		this.logger.debug("Service subReportType :: " + subReportType);
		this.logger.debug("Service reportType :: " + reportType);
		return this.taskDAO.getInitialsForSubReport(subReportType, reportType);
	}

	public int insertRecurrentDetails(RecurrentCaseVO recurrenceVO) throws CMSException {
		return this.taskDAO.insertRecordForRecurrentCase(recurrenceVO);
	}

	public List<SubReportTypeVO> getSubReportTypes(String reportType) throws CMSException {
		return this.taskDAO.getSubReport(reportType);
	}

	public int updateRecordForClient(CaseDetails caseDetails) throws CMSException {
		return this.taskDAO.updateRecordForCompleteCase(caseDetails);
	}

	public List<VendorForCrnDetailsVO> getVendorDetails(String crn) throws CMSException {
		return this.taskDAO.getVendorDetails(crn);
	}

	public List<String> getRiskAssociatedWithCase(String crn) throws CMSException {
		return this.taskDAO.getRiskAssociatedWithCase(crn);
	}

	public int updateRiskFlag(String crn) throws CMSException {
		return this.taskDAO.updateRiskFlag(crn);
	}

	public List<String> updateRiskAssociatedWithCase(String crn) throws CMSException {
		return this.taskDAO.getRiskAssociatedWithCase(crn);
	}

	public int insertPID(long pid, String crn) throws CMSException {
		HashMap<String, Object> insertMap = new HashMap();
		insertMap.put("CRN", crn);
		insertMap.put("PID", pid);
		return this.taskDAO.insertPID(insertMap);
	}

	public long getPID(String crn) throws CMSException {
		return this.taskDAO.getPID(crn);
	}

	public List<ClientMasterVO> getClientByName() throws CMSException {
		return this.taskDAO.getClientMasterByName();
	}

	public List<ClientMasterVO> getCLientByCode() throws CMSException {
		return this.taskDAO.getClientMasterByCode();
	}

	public String getRDDForST(String teamType, String office, String dueDate, int daysBefore, String receivedDate)
			throws CMSException {
		String stRDD = "";
		SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		Calendar recDate = Calendar.getInstance();
		if (receivedDate != null) {
			try {
				recDate.setTime(targetFormat.parse(receivedDate));
			} catch (ParseException var18) {
				throw new CMSException(this.logger, var18);
			}
		}

		try {
			calendar.setTime(sourceFormat.parse(dueDate));
			Date ptRDD = calendar.getTime();
			this.logger.debug("PTRDD is : " + calendar.getTime());
			boolean lclHolidayCheck = true;
			String holidayName = "";

			do {
				ptRDD = calendar.getTime();
				if (teamType.equals("Supporting - Internal")) {
					this.logger.debug("teamType is Supporting - Internal");
					holidayName = this.taskDAO.checkForHoliday(targetFormat.format(ptRDD), office);
				} else if (teamType.equals("Supporting - BI")) {
					this.logger.debug("inside else");
					holidayName = this.taskDAO.checkHolidayForAnalyst(targetFormat.format(ptRDD), office);
				}

				if ((holidayName == null || holidayName.length() <= 0)
						&& !(new SimpleDateFormat("EEEE")).format(ptRDD).equalsIgnoreCase("Sunday")
						&& !(new SimpleDateFormat("EEEE")).format(ptRDD).equalsIgnoreCase("Saturday")) {
					if (calendar.get(6) <= recDate.get(6) && calendar.get(1) <= recDate.get(1)) {
						stRDD = stRDD + sourceFormat.format(recDate.getTime());
					} else {
						for (int i = 0; i < daysBefore; ++i) {
							calendar.add(5, -1);
							boolean lclHolidayCheck1 = true;
							Date tempStRDD = calendar.getTime();
							this.logger.debug("reduced date is : " + tempStRDD);

							do {
								tempStRDD = calendar.getTime();
								this.logger.debug("tempStRDD in do while " + tempStRDD);
								if (teamType.equals("Supporting - Internal")) {
									this.logger.debug("teamType is Supporting - Internal");
									holidayName = this.taskDAO.checkForHoliday(targetFormat.format(tempStRDD), office);
								} else if (teamType.equals("Supporting - BI")) {
									this.logger.debug("inside else");
									holidayName = this.taskDAO.checkHolidayForAnalyst(targetFormat.format(tempStRDD),
											office);
								}

								if ((holidayName == null || holidayName.length() <= 0)
										&& !(new SimpleDateFormat("EEEE")).format(tempStRDD).equalsIgnoreCase("Sunday")
										&& !(new SimpleDateFormat("EEEE")).format(tempStRDD)
												.equalsIgnoreCase("Saturday")) {
									if (i == daysBefore - 1) {
										if (calendar.get(6) <= recDate.get(6) && calendar.get(1) <= recDate.get(1)) {
											stRDD = stRDD + sourceFormat.format(recDate.getTime());
										} else {
											stRDD = stRDD + sourceFormat.format(tempStRDD);
										}

										this.logger.debug("STRDD is : " + stRDD);
									}

									lclHolidayCheck1 = false;
								} else {
									this.logger.debug(tempStRDD + " is a holiday/weekend");
									calendar.add(5, -1);
									lclHolidayCheck1 = true;
								}
							} while (lclHolidayCheck1);
						}
					}

					lclHolidayCheck = false;
				} else {
					calendar.add(5, -1);
					lclHolidayCheck = true;
				}
			} while (lclHolidayCheck);

			return stRDD;
		} catch (ParseException var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	public String getAllDatesFormat(String reportType, String date, String officeName, String receivedDate)
			throws CMSException {
		String holidayName = "";
		String dt = "";
		int lcl = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		this.logger.debug("old date is " + date);
		Calendar recDate = Calendar.getInstance();
		if (receivedDate != null) {
			try {
				recDate.setTime(simpleDateFormat.parse(receivedDate));
			} catch (ParseException var16) {
				throw new CMSException(this.logger, var16);
			}
		}

		if (date != null) {
			try {
				calendar.setTime(simpleDateFormat.parse(date));
			} catch (ParseException var15) {
				throw new CMSException(this.logger, var15);
			}
		} else {
			try {
				calendar.setTime(simpleDateFormat.parse(receivedDate));
			} catch (ParseException var14) {
				throw new CMSException(this.logger, var14);
			}
		}

		if (date == null) {
			lcl = this.taskDAO.getTurnAround(reportType);
		}

		if (lcl != 0 || date != null) {
			this.logger.debug("turn arround time is " + lcl);
			calendar.add(5, lcl);
			Date lclDate = calendar.getTime();
			this.logger.debug("lclDate is " + lclDate);
			boolean lclHolidayCheck = true;

			do {
				lclDate = calendar.getTime();
				holidayName = this.taskDAO.checkForHoliday(simpleDateFormat.format(lclDate), officeName);
				if ((holidayName == null || holidayName.length() <= 0)
						&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")
						&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
					this.logger.debug("calendar.get(Calendar.DAY_OF_YEAR) " + calendar.get(6));
					this.logger.debug("Calendar.getInstance().get(Calendar.DAY_OF_YEAR) " + recDate.get(6));
					if (calendar.get(6) <= recDate.get(6) && calendar.get(1) <= recDate.get(1)) {
						dt = simpleDateFormat.format(recDate.getTime()) + "::"
								+ simpleDateFormat.format(recDate.getTime());
						this.logger.debug("dt in if Calendar.DAY_OF_YEAR " + dt);
					} else {
						dt = simpleDateFormat.format(lclDate) + "::";
						this.logger.debug("dt in else " + dt);
						calendar.add(5, -1);
						boolean lclHolidayCheck1 = true;
						lclDate = calendar.getTime();

						do {
							lclDate = calendar.getTime();
							this.logger.debug("dt in do while " + lclDate);
							holidayName = this.taskDAO.checkForHoliday(simpleDateFormat.format(lclDate), officeName);
							if ((holidayName == null || holidayName.length() <= 0)
									&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")
									&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
								if (calendar.get(6) <= recDate.get(6) && calendar.get(1) <= recDate.get(1)) {
									dt = dt + simpleDateFormat.format(recDate.getTime());
									this.logger.debug("dt in second if " + dt);
								} else {
									dt = dt + simpleDateFormat.format(lclDate);
									this.logger.debug("dt in second else " + dt);
								}

								lclHolidayCheck1 = false;
							} else {
								calendar.add(5, -1);
								lclHolidayCheck1 = true;
								this.logger.debug("dt in holiday check " + calendar.get(5) + " :: dt is " + dt);
							}
						} while (lclHolidayCheck1);
					}

					lclHolidayCheck = false;
				} else {
					calendar.add(5, -1);
					lclHolidayCheck = true;
					this.logger.debug("dt in last else check " + calendar.get(5));
				}
			} while (lclHolidayCheck);
		}

		this.logger.debug("Date is " + dt);
		return dt;
	}

	public String getOfficeForClient(String clientName) throws CMSException {
		String officeName = "";
		String[] tempClientInfo = clientName.split("-");
		officeName = this.taskDAO.getOfficeForClient(tempClientInfo[0]);
		return officeName;
	}

	public List<UserMasterVO> getAnalystForManager(String managerId) throws CMSException {
		return this.taskDAO.getAnalystForManager(managerId);
	}

	public List<CountryDatabaseVO> getCountryDBForCountry(Map countryMap) throws CMSException {
		return this.taskDAO.getCountryDBForCountry(countryMap);
	}

	public int getCountryDBForCountryCount(String countryId) throws CMSException {
		return this.taskDAO.getCountryDBForCountyCount(countryId);
	}

	public List<DocMapVO> getDocs(Set<String> documentIds) throws CMSException {
		List<DocMapVO> listOFDocs = new ArrayList();
		String docIds = "";
		Iterator iterator = documentIds.iterator();

		while (iterator.hasNext()) {
			if ("".equalsIgnoreCase(docIds)) {
				docIds = "'" + (String) iterator.next() + "'";
			} else {
				docIds = docIds + "," + "'" + (String) iterator.next() + "'";
			}
		}

		if (!"".equalsIgnoreCase(docIds)) {
			listOFDocs = this.taskDAO.getDocuments(docIds);
		}

		this.logger.debug("docIds are " + docIds);
		return (List) listOFDocs;
	}

	public int updateDocuments(List<String> listOfDocNames, List<String> docIds, String teamName, long pid)
			throws CMSException {
		String docId = "";
		Iterator iterator = docIds.iterator();

		while (iterator.hasNext()) {
			if ("".equalsIgnoreCase(docId)) {
				docId = "'" + (String) iterator.next() + "'";
			} else {
				docId = docId + "," + "'" + (String) iterator.next() + "'";
			}
		}

		this.logger.debug("docis " + docId + " for team " + teamName + " for pid " + pid);
		return this.taskDAO.updateDocuments(listOfDocNames, docId, teamName, pid);
	}

	public int resetStatus(List<String> listOfIds, String docId) throws CMSException {
		String docIds = "";
		this.logger.debug("list size is " + listOfIds);
		Iterator iterator = listOfIds.iterator();

		while (iterator.hasNext()) {
			this.logger.debug("docIds value is " + docIds);
			if ("".equalsIgnoreCase(docIds)) {
				docIds = "'" + (String) iterator.next() + "'";
			} else {
				docIds = docIds + "," + "'" + (String) iterator.next() + "'";
			}
		}

		this.logger.debug("ids are " + docIds);
		return this.taskDAO.resetStatus(docIds, docId);
	}

	public int markJuno(String docId) throws CMSException {
		return this.taskDAO.markJuno(docId);
	}

	public int resetStatusForJuno(Set<String> listOfIds, String docId) throws CMSException {
		String docIds = "";
		this.logger.debug("list size is " + listOfIds);
		Iterator iterator = listOfIds.iterator();

		while (iterator.hasNext()) {
			this.logger.debug("docIds value is " + docIds);
			if ("".equalsIgnoreCase(docIds)) {
				docIds = "'" + (String) iterator.next() + "'";
			} else {
				docIds = docIds + "," + "'" + (String) iterator.next() + "'";
			}
		}

		this.logger.debug("ids are " + docIds);
		return this.taskDAO.resetStatusForJuno(docIds, docId);
	}

	public int delDocument(String docIds) throws CMSException {
		return this.taskDAO.deleteDocumentFromCustom(docIds);
	}

	public boolean hasPermissionForSensitiveFile(String userId) throws CMSException {
		return this.taskDAO.hasPermissionForSensitiveFile(userId);
	}

	public boolean hasDeletePermission(String userId) throws CMSException {
		return this.taskDAO.hasDeletePermission(userId);
	}

	public boolean hasUploadPermission(String userId) throws CMSException {
		return this.taskDAO.hasUploadPermission(userId);
	}

	public int getOfficeId(String userId) throws CMSException {
		return this.taskDAO.getOfficeId(userId);
	}

	public String getOfficeCode(String userId) throws CMSException {
		return this.taskDAO.getOfficeCode(userId);
	}

	public String generateCRN(CaseDetails caseDetailsVO, boolean isRecurrentCase) throws CMSException {
		ResourceLocator locator = ResourceLocator.self();
		NumberFormat numberFormatter = NumberFormat.getInstance();
		int minimumDigitsNumber = 5;
		numberFormatter.setMinimumIntegerDigits(minimumDigitsNumber);
		long getSeqForCRN = 0L;
		numberFormatter.setGroupingUsed(false);
		String reqRecDate = "" + caseDetailsVO.getReqRecdDate();
		String pattern = "yyyy-MM-dd hh:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		pattern = "dd-MMM-yyyy HH:mm:ss";

		Date temp;
		try {
			temp = format.parse(reqRecDate);
		} catch (ParseException var20) {
			throw new CMSException(this.logger, var20);
		}

		format = new SimpleDateFormat(pattern);
		reqRecDate = format.format(temp);
		this.logger.debug("req date =is " + reqRecDate);
		getSeqForCRN = locator.getTaskService().getSeqForCRN(reqRecDate);
		String strpoSeqNumber = numberFormatter.format(getSeqForCRN);
		String subReportTypeLocal = "";
		String recurrentNumberLocal = "";
		int subReportTypeID = caseDetailsVO.getclSubreportID();
		this.logger.debug("getcLSubreportID::::::::" + subReportTypeID);
		String clientCode;
		if (subReportTypeID != 0) {
			clientCode = locator.getTaskService().getInitialsForSubReportByID("" + subReportTypeID);
			if (clientCode != null && !"".equalsIgnoreCase(clientCode.trim())) {
				subReportTypeLocal = "\\" + clientCode;
			}
		}

		if (isRecurrentCase) {
			minimumDigitsNumber = 3;
			numberFormatter.setMinimumIntegerDigits(minimumDigitsNumber);
			numberFormatter.setGroupingUsed(false);
			long reccurrentNum = locator.getTaskService().getSeqForRecurrence();
			recurrentNumberLocal = "\\" + numberFormatter.format(reccurrentNum);
		}

		clientCode = caseDetailsVO.getClientCode();
		String reportType = caseDetailsVO.getReportType();
		int selected_year = Integer.parseInt(reqRecDate.split(" ")[0].split("-")[2]);
		String crn = strpoSeqNumber + "\\" + clientCode + "\\"
				+ locator.getTaskService().getInitialsForReport(reportType) + "\\" + selected_year + subReportTypeLocal;
		if (97 == subReportTypeID || 1 == subReportTypeID) {
			crn = crn + "\\L";
		}

		crn = crn + recurrentNumberLocal;
		this.logger.debug("crn is " + crn);
		return crn;
	}

	public void insertDataToAccountsAndNotification(CaseDetails caseDetailsVO) throws CMSException {
		AccountsVO accountsVO = new AccountsVO();
		accountsVO.setCrn(caseDetailsVO.getCrn());
		accountsVO.setClientCode(caseDetailsVO.getClientCode());
		accountsVO.setUpdateBy(caseDetailsVO.getUpdatedBy());
		accountsVO.setBudget(caseDetailsVO.getBudget());
		accountsVO.setCaseFee(Float.toString(caseDetailsVO.getBudget()));
		accountsVO.setIsBudgetDueDateConfirmed(caseDetailsVO.isBudgetDueDateConfirmed());
		accountsVO.setCurrencyCode(caseDetailsVO.getCurrency_Code());
		accountsVO.setTaxCode(caseDetailsVO.getTaxCode());
		ResourceLocator.self().getInvoiceService().insertInvoiceDetails(accountsVO);
		if (!caseDetailsVO.getUpdatedBy().equalsIgnoreCase(caseDetailsVO.getCaseMgrId())) {
			String notificationMessage = "You are assigned the Case Manager for this case(" + caseDetailsVO.getCrn()
					+ ")";
			List<String> listOfUsers = new ArrayList();
			listOfUsers.add(caseDetailsVO.getCaseMgrId());
			ResourceLocator.self().getNotificationService().createSystemNotification(notificationMessage,
					notificationMessage, listOfUsers, caseDetailsVO.getCrn());
		}

	}

	public boolean isMarked(Set<String> docIds) throws CMSException {
		boolean isMarked = false;
		String docs = "";
		Iterator iterator = docIds.iterator();

		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			if (docs.equalsIgnoreCase("")) {
				docs = "'" + string + "'";
			} else {
				docs = docs + ",'" + string + "'";
			}
		}

		if (this.taskDAO.getMarkedCount(docs) > 0) {
			isMarked = true;
		}

		this.logger.debug("doc ids are " + docs);
		return isMarked;
	}

	public void updateClientSubmissionDate(String sendDate, String cycle, String crn, String updatedBy,
			String clientFeedBack) throws CMSException {
		if (this.taskDAO.updateClientDate(sendDate, cycle, crn, updatedBy, clientFeedBack) > 0) {
			this.logger.debug("update successfull");
		} else {
			this.logger.debug("not updated");
		}

	}

	public List<Map<String, Object>> getTaskForManager(String userId) throws CMSException {
		return this.taskDAO.getTaskForManager(userId);
	}

	public int updateRESstauts(String performer, String crn) throws CMSException {
		return this.taskDAO.updateRESstauts(performer, crn);
	}

	public boolean isFinalAndJunoUploadedFile(long pid) throws CMSException {
		return this.taskDAO.isFinalAndJunoUploadedFile(pid);
	}

	public int updateDateForMIS(long teamId, String processCycle, String updatedBy) throws CMSException {
		return this.taskDAO.updateDateForMIS(teamId, processCycle, updatedBy);
	}

	public ClientCaseStatusVO getFileForISIS(long pid) throws CMSException {
		ClientCaseStatusVO clientCaseStatusVO = new ClientCaseStatusVO();
		String fileName = this.taskDAO.getFileForISIS(pid);
		String TRIPLE_EQUALS = "===";
		String[] temp = fileName.split(TRIPLE_EQUALS);
		clientCaseStatusVO.setFileName(temp[0]);
		clientCaseStatusVO.setVersion(Double.parseDouble(temp[1]));
		return clientCaseStatusVO;
	}

	public int getISISStatus(String crn) throws CMSException {
		return this.taskDAO.getISISStatus(crn);
	}

	public Subject getSubjectDetailsForISIS(long subjectId) throws CMSException {
		return this.taskDAO.getSubjectDetailsForISIS(subjectId);
	}

	public String getCountryCode(long countryId) throws CMSException {
		return this.taskDAO.getCountryCode(countryId);
	}

	public String getRiskLabel(String riskCode) throws CMSException {
		return this.taskDAO.getRiskLabel(riskCode);
	}

	public int getSubjectListCountForUser(String crn, String performer2) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("performer", performer2);
		int count = this.taskDAO.getSubjectListCount(mapForParams);
		this.logger.debug("list size is " + count);
		return count;
	}

	public int getDocumentVersion(String docId) throws CMSException {
		return this.taskDAO.getDocumentVersion(docId);
	}

	public int getCBDValue(String crn) throws CMSException {
		return this.taskDAO.getCBDValue(crn);
	}

	public List<CustomTaskVO> getSavvionTasks(String userID, String condition, String statusCondition, int limit,
			int start, String commaSeparatedUserList) throws CMSException {
		condition = " ' AND BLIDS.CASESTATUS <> ''Cancelled''  " + condition + " '";
		Map<String, String> mapOfParams = new HashMap();
		mapOfParams.put("customFilterCondition", condition);
		mapOfParams.put("userID", userID);
		mapOfParams.put("statusCondition", statusCondition);
		String userList = commaSeparatedUserList;
		if (commaSeparatedUserList.length() > 0 && commaSeparatedUserList.equalsIgnoreCase("subordinateAllTask")) {
			userList = "'''subordinateAllTask'''";
		} else if (commaSeparatedUserList.split(",").length > 0) {
			if (commaSeparatedUserList.split(",").length == 1 && commaSeparatedUserList.contains("FinanceQueue")) {
				userList = "'''FinanceQueue'''";
			} else if (commaSeparatedUserList.split(",").length > 1
					&& commaSeparatedUserList.contains("FinanceQueue")) {
				userList = "'''TeamTask'',''FinanceQueue'''";
			} else if (commaSeparatedUserList.split(",").length >= 1 && !commaSeparatedUserList.contains("FinanceQueue")
					&& !commaSeparatedUserList.equalsIgnoreCase("''")) {
				userList = "'''TeamTask'''";
			}
		}

		mapOfParams.put("userList", userList);
		mapOfParams.put("start", String.valueOf(start + 1));
		mapOfParams.put("limit", String.valueOf(start + limit));
		return this.taskDAO.getSavvionTasks(mapOfParams);
	}

	public long getSavvionTasksCount(String userID, String condition, String statusCondition,
			String commaSeparatedUserList) throws CMSException {
		condition = " ' AND BLIDS.CASESTATUS <> ''Cancelled''  " + condition + " '";
		Map<String, String> mapOfParams = new HashMap();
		mapOfParams.put("customFilterCondition", condition);
		mapOfParams.put("userID", userID);
		mapOfParams.put("statusCondition", statusCondition);
		String userList = commaSeparatedUserList;
		if (commaSeparatedUserList.length() > 0 && commaSeparatedUserList.equalsIgnoreCase("subordinateAllTask")) {
			userList = "'''subordinateAllTask'''";
		} else if (commaSeparatedUserList.split(",").length > 0) {
			if (commaSeparatedUserList.split(",").length == 1 && commaSeparatedUserList.contains("FinanceQueue")) {
				userList = "'''FinanceQueue'''";
			} else if (commaSeparatedUserList.split(",").length > 1
					&& commaSeparatedUserList.contains("FinanceQueue")) {
				userList = "'''TeamTask'',''FinanceQueue'''";
			} else if (commaSeparatedUserList.split(",").length >= 1 && !commaSeparatedUserList.contains("FinanceQueue")
					&& !commaSeparatedUserList.equalsIgnoreCase("''")) {
				userList = "'''TeamTask'''";
			}
		}

		mapOfParams.put("userList", userList);
		return this.taskDAO.getSavvionTasksCount(mapOfParams);
	}

	public List<TeamDetails> getMyIncomingTasks(String userID, int limit, int start) throws CMSException {
		Map<String, String> mapOfParams = new HashMap();
		mapOfParams.put("userID", userID);
		mapOfParams.put("start", String.valueOf(start + 1));
		mapOfParams.put("limit", String.valueOf(start + limit));
		mapOfParams.put("statusCondition", "");
		mapOfParams.put("users", "''");
		return this.taskDAO.getMyIncomingTasks(mapOfParams);
	}

	public long getMyIncomingTasksCount(String userID) throws CMSException {
		Map<String, String> mapOfParams = new HashMap();
		mapOfParams.put("userID", userID);
		mapOfParams.put("users", "''");
		mapOfParams.put("statusCondition", "");
		return this.taskDAO.getMyIncomingTasksCount(mapOfParams);
	}

	public String checkHolidayWeakend(String date, String officeName) throws ParseException {
		this.logger.debug("TaskManagementService checkHolidayWeakend " + date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		String holidayName = "";

		try {
			calendar.setTime(simpleDateFormat.parse(date));
			Date lclDate = calendar.getTime();
			holidayName = this.taskDAO.checkForHoliday(simpleDateFormat.format(lclDate), officeName);
			this.logger.debug("holidayName from DB" + holidayName);
			if (holidayName == null || holidayName.length() <= 0) {
				if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
					holidayName = "Weekend";
				} else if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")) {
					holidayName = "Weekend";
				}
			}

			this.logger.debug("check if holiday--" + holidayName);
		} catch (CMSException var7) {
			this.logger.debug("TaskManagementService checkHolidayWeakend in catch " + date);
			var7.printStackTrace();
		}

		return holidayName;
	}

	public String checkHolidayWeakendCDD(String date) throws ParseException {
		this.logger.debug("TaskManagementService checkHolidayWeakendCDD " + date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		String holidayOfficeList = "";

		try {
			calendar.setTime(simpleDateFormat.parse(date));
			Date lclDate = calendar.getTime();
			holidayOfficeList = this.taskDAO.checkHolidayWeakendCDD(simpleDateFormat.format(lclDate));
			this.logger.debug("holidayName from DB" + holidayOfficeList);
			if (holidayOfficeList == null || holidayOfficeList.length() <= 0) {
				if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
					holidayOfficeList = "Weekend";
				} else if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")) {
					holidayOfficeList = "Weekend";
				}
			}

			this.logger.debug("check if holiday--" + holidayOfficeList);
		} catch (CMSException var6) {
			this.logger.debug("TaskManagementService checkHolidayWeakendCDD in catch " + date);
			var6.printStackTrace();
		}

		return holidayOfficeList;
	}

	public boolean checkHolidayWeakendForCaseOffice(String date, String officeId) throws ParseException {
		this.logger.debug("TaskManagementService checkHolidayWeakend " + date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		boolean flag = false;

		try {
			calendar.setTime(simpleDateFormat.parse(date));
			Date lclDate = calendar.getTime();
			if (this.taskDAO.checkHolidayWeakendForCaseOffice(simpleDateFormat.format(lclDate), officeId)) {
				flag = true;
			} else if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
				flag = true;
			} else if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (CMSException var7) {
			this.logger.debug("TaskManagementService checkHolidayWeakend in catch " + date);
			var7.printStackTrace();
		}

		return flag;
	}

	public String checkHolidayWeakendAnalyst(String date, String analyst) throws ParseException {
		this.logger.debug("TaskManagementService checkHolidayWeakendAnalyst " + date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		String holidayName = "";

		try {
			calendar.setTime(simpleDateFormat.parse(date));
			Date lclDate = calendar.getTime();
			holidayName = this.taskDAO.checkHolidayForAnalyst(simpleDateFormat.format(lclDate), analyst);
			this.logger.debug("holidayName from DB" + holidayName);
			if (holidayName == null || holidayName.length() <= 0) {
				if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
					holidayName = "Weekend";
				} else if ((new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")) {
					holidayName = "Weekend";
				}
			}
		} catch (CMSException var7) {
			this.logger.debug("TaskManagementService checkHolidayWeakendAnalyst in catch " + date);
			var7.printStackTrace();
		}

		return holidayName;
	}

	public int isHeaderRecorded(String userId) throws CMSException {
		int count = this.taskDAO.isHeaderRecorded(userId);
		this.logger.debug("list size for isHistoryRecorded is " + count);
		return count;
	}

	public void updateHeaderInfo(String userId, String headerInfo) throws CMSException {
		if (this.taskDAO.updateHeaderInfo(userId, headerInfo) > 0) {
			this.logger.debug("HeaderInfo updated successfully");
		} else {
			this.logger.debug("HeaderInfo not updated");
		}

	}

	public void insertHeaderInfo(String userId, String headerInfo) throws CMSException {
		if (this.taskDAO.insertHeaderInfo(userId, headerInfo) > 0) {
			this.logger.debug("HeaderInfo inserted successfully");
		} else {
			this.logger.debug("HeaderInfo not inserted");
		}

	}

	public List<SubReportTypeVO> getSubReportTypesList() throws CMSException {
		return this.taskDAO.getSubReportTypesList();
	}

	public String getAnalysts(String crn, String cycle, String teamtypeList) throws CMSException {
		return this.taskDAO.getAnalysts(crn, cycle, teamtypeList);
	}

	public List<SubjectListForUserVO> getSubjectLevelRiskForUser(String crn, String performer, String taskName,
			String teamTypeId, String teamName, String teamId) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("performer", performer);
		mapForParams.put("taskName", taskName);
		mapForParams.put("teamTypeId", teamTypeId);
		mapForParams.put("teamName", teamName);
		mapForParams.put("teamId", teamId);
		List<SubjectListForUserVO> list = this.taskDAO.getSubjectLevelRisk(mapForParams);
		this.logger.debug("list size is " + list.size());
		return list;
	}

	public List<RiskCategoryMasterVO> getCaseLevelRiskForUser(String crn, String performer) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("performer", performer);
		List<RiskCategoryMasterVO> list = this.taskDAO.getCaseLevelRisk(mapForParams);
		this.logger.debug("list size is " + list.size());
		return list;
	}

	public int updateOptionalRiskData(RiskProfileVO rVO) throws CMSException {
		return this.taskDAO.updateOptionalRiskData(rVO);
	}

	public int updateOptionalRiskDataHistory(RiskHistory rh) throws CMSException {
		return this.taskDAO.updateOptionalRiskDataHistory(rh);
	}

	public int insertOptionalRiskData(List<RiskProfileVO> rVOList, List<CountryHBDVO> countryHBDResponseList)
			throws CMSException {
		return this.taskDAO.insertOptionalRiskData(rVOList, countryHBDResponseList);
	}

	public int insertHBDCountryRiskData(List<CountryHBDVO> cntryHBDVOList) throws CMSException {
		return this.taskDAO.insertHBDCountryRiskData(cntryHBDVOList);
	}

	public long getNextProfileId() throws CMSException {
		return this.taskDAO.getNextProfileId();
	}

	public List<CountryHBDVO> getCountries(String crn, String subID) throws CMSException {
		return this.taskDAO.getCountries(crn, subID);
	}

	public List<CategoryDetailsVO> getCategoryDetails() throws CMSException {
		return this.taskDAO.getCategoryDetails();
	}

	public long saveAfterCaseCompletionCategoryRisk(String crn, List<RiskProfileVO> riskProfileList,
			List<RiskProfileVO> riskProfileListWithHBD, String taskName, Session session,
			List<RiskAggregationVO> riskAggregationList) throws CMSException {
		int count = false;
		long riskHistoryCount = 0L;
		long riskHistoryCountHBD = 0L;
		long riskCount;
		ArrayList riskHistoryWithHBDList;
		int i;
		RiskHistory rh;
		RiskProfileVO rVO;
		if (riskProfileList.size() > 0) {
			riskHistoryWithHBDList = new ArrayList();

			for (i = 0; i < riskProfileList.size(); ++i) {
				rh = new RiskHistory();
				rVO = (RiskProfileVO) riskProfileList.get(i);
				rh.setCRN(rVO.getCRN());
				rh.setRiskCategoryId(rVO.getRiskCategoryId());
				rh.setRiskCode(rVO.getRiskCode());
				rh.setCountryMasterId(0L);
				rh.setOldInfo(rVO.getOldAttrValue());
				rh.setNewInfo(rVO.getNewattrValue());
				rh.setAction("Update");
				rh.setTask(taskName);
				rh.setUpdatedBy(session.getUser());
				rh.setAttributeId(Long.parseLong(rVO.getAttributeId()));
				if (!rh.getOldInfo().equals(rh.getNewInfo())) {
					riskHistoryWithHBDList.add(rh);
				}
			}

			this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
			riskCount = this.riskProfileDAO.updateRiskProfileData(riskProfileList);
		} else {
			riskCount = 0L;
		}

		this.logger.debug("Risk Profile Updated Records Count::" + riskCount);
		if (riskProfileListWithHBD.size() > 0) {
			riskHistoryWithHBDList = new ArrayList();

			for (i = 0; i < riskProfileListWithHBD.size(); ++i) {
				rh = new RiskHistory();
				rVO = (RiskProfileVO) riskProfileListWithHBD.get(i);
				rh.setCRN(rVO.getCRN());
				rh.setRiskCategoryId(rVO.getRiskCategoryId());
				rh.setRiskCode(rVO.getRiskCode());
				rh.setCountryMasterId(rVO.getCountryId());
				rh.setOldInfo(rVO.getOldAttrValue());
				rh.setNewInfo(rVO.getNewattrValue());
				rh.setAction("Update");
				rh.setTask(taskName);
				rh.setUpdatedBy(session.getUser());
				rh.setAttributeId(Long.parseLong(rVO.getAttributeId()));
				if (!rh.getOldInfo().equals(rh.getNewInfo())) {
					riskHistoryWithHBDList.add(rh);
				}
			}

			this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
			this.riskProfileDAO.updateRiskProfileDataWithHBD(riskProfileListWithHBD);
		} else {
			long var14 = 0L;
		}

		if (riskAggregationList.size() > 0) {
			this.riskProfileDAO.updateRiskAggregationData(riskAggregationList);
		}

		return 0L;
	}

	public long saveCategoryRisk(List<RiskProfileVO> riskProfileList, List<RiskProfileVO> riskProfileListWithHBD,
			String taskName, Session session, List<RiskAggregationVO> riskAggregationList,
			List<TotalRiskAggregationVO> totalRiskAggrList, List<RiskProfileVO> subIndusList) throws CMSException {
		int count = false;
		long riskHistoryCount = 0L;
		long riskHistoryCountHBD = 0L;
		RiskHistory rh = null;
		RiskHistory rhbd = null;
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
		if (riskProfileList.size() > 0) {
			riskHistoryWithHBDList = new ArrayList();
			riskCodeFlag = "";
			historyIndex = 0;
			flag = false;

			for (i = 0; i < riskProfileList.size(); ++i) {
				this.logger.debug("risk profile size::" + riskProfileList.size() + ".." + i);
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
						oldValue = oldValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getOldAttrValue() + "<br />");
					}

					if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
						newValue = newValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getNewattrValue() + "<br />");
					}

					this.logger.debug("old Value without HBD.." + oldValue);
					this.logger.debug("new Value without HBD.." + newValue);
					this.logger.debug("conmparison.." + oldValue.toString().equals(newValue.toString()));
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

						this.logger.debug("old Value without HBD.." + rh.getOldInfo());
						this.logger.debug("new Value without HBD.." + rh.getNewInfo());
						this.logger.debug("conmparison.." + rh.getOldInfo().equals(rh.getNewInfo()));
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
						oldValue = oldValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getOldAttrValue() + "<br />");
					}

					if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
						newValue = newValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getNewattrValue() + "<br />");
					}

					flag = false;
					if (!oldValue.toString().equals(newValue.toString())) {
						rh.setOldInfo(oldValue.toString());
						rh.setNewInfo(newValue.toString());
						riskHistoryWithHBDList.add(rh);
						flag = true;
						++historyIndex;
					}

					this.logger.debug("old Value without HBD.." + oldValue);
					this.logger.debug("new Value without HBD.." + newValue);
					this.logger.debug("conmparison.." + oldValue.toString().equals(newValue.toString()));
					riskCodeFlag = rVO.getRiskCode() + "#" + rVO.getRiskCategoryId() + "#" + rVO.getSubjectId();
				}
			}

			this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
			riskCount = this.riskProfileDAO.updateRiskProfileData(riskProfileList);
		} else {
			riskCount = 0L;
		}

		this.logger.debug("Risk Profile Updated Records Count::" + riskCount);
		if (riskProfileListWithHBD.size() > 0) {
			riskHistoryWithHBDList = new ArrayList();
			riskCodeFlag = "";
			historyIndex = 0;
			flag = false;

			for (i = 0; i < riskProfileListWithHBD.size(); ++i) {
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
						oldValue = oldValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getOldAttrValue() + "<br />");
						this.logger.debug("oldValue if.." + oldValue);
					}

					if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
						newValue = newValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getNewattrValue() + "<br />");
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
						oldValue = oldValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getOldAttrValue() + "<br />");
						this.logger.debug("oldValue if.." + oldValue);
					}

					if (rVO.getAttributeName() != null && rVO.getNewattrValue() != null) {
						newValue = newValue.append(
								"<b>" + rVO.getAttributeName() + "</b>" + ":" + rVO.getNewattrValue() + "<br />");
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

					riskCodeFlag = rVO.getRiskCode() + "#" + rVO.getRiskCategoryId() + "#" + rVO.getSubjectId() + "#"
							+ rVO.getCountryId();
				}
			}

			this.riskHistoryDAO.saveRiskHistoryData(riskHistoryWithHBDList);
			this.riskProfileDAO.updateRiskProfileDataWithHBD(riskProfileListWithHBD);
		} else {
			long var15 = 0L;
		}

		if (subIndusList.size() > 0) {
			this.riskProfileDAO.updateSubjectWithIndustryCode(subIndusList);
		}

		if (riskAggregationList.size() > 0) {
			this.riskProfileDAO.updateRiskAggregationData(riskAggregationList);
		}

		if (totalRiskAggrList.size() > 0) {
			this.riskProfileDAO.updateTotalRiskAggregationData(totalRiskAggrList);
		}

		return 0L;
	}

	public String getInitialsForSubReportByID(String subReportTypeID) throws CMSException {
		return this.taskDAO.getInitialsForSubReportByID(subReportTypeID);
	}
}