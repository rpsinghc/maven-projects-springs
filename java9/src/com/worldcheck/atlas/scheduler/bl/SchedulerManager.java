package com.worldcheck.atlas.scheduler.bl;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.scheduler.dao.SchedulerDAO;
import com.worldcheck.atlas.scheduler.vo.EmailPrepaymentCaseVo;
import com.worldcheck.atlas.scheduler.vo.OverdueCasesSchedulerVo;
import com.worldcheck.atlas.scheduler.vo.SchedulerVo;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.document.DocMapVO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;

public class SchedulerManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.scheduler.bl.SchedulerManager");
	private SchedulerDAO schedulerDAO;
	private static PropertyReaderUtil propertyReader;
	private static String receivedDate = "";
	private static String cInterim1 = "";
	private static String cInterim2 = "";
	private static String cFinal = "";
	private static String rInterim1 = "";
	private static String rInterim2 = "";
	private static String rFinal = "";

	public void setSchedulerDAO(SchedulerDAO schedulerDAO) {
		this.schedulerDAO = schedulerDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public void getAvailableRecCasesForSchedulerAndSendNotification() throws CMSException {
		this.logger.debug("In SchedulerManager::getAvailableRecCasesForSchedulerAndSendNotification");

		try {
			List<String> crnList = new ArrayList();
			List<SchedulerVo> schedulerVoList = this.schedulerDAO.getAvailableRecCasesForScheduler();
			Iterator iterator = schedulerVoList.iterator();

			while (iterator.hasNext()) {
				SchedulerVo schedulerVo = (SchedulerVo) iterator.next();
				crnList.add(schedulerVo.getCRN());
				this.logger.debug("Base Setup CRN:- " + schedulerVo.getCRN());
				String alertDescription = "You need to Create a Recurrence Case for Client:-"
						+ schedulerVo.getClientCode() + " ,Report Type is:- " + schedulerVo.getReportType();
				this.logger.debug("case manager notification string is:- " + alertDescription);
				schedulerVo.setDescription(alertDescription);
				this.insertRecCaseNotificationAndDates(schedulerVo);
			}

			if (crnList.size() > 0) {
				this.updateSchedulerLastNextRunDate(crnList);
			}

			this.logger.debug("Exiting SchedulerManager::getAvailableRecCasesForScheduler");
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private void insertRecCaseNotificationAndDates(SchedulerVo schedulerVo) throws CMSException {
		this.logger.debug("In SchedulerManager::insertRecCaseNotificationAndDates");

		try {
			this.calculateDueDates(schedulerVo);
			schedulerVo.setClientInterm1Date(cInterim1);
			schedulerVo.setClientInterm2Date(cInterim2);
			schedulerVo.setClientFinalDate(cFinal);
			schedulerVo.setRschInterm1Date(rInterim1);
			schedulerVo.setRschInterm2Date(rInterim2);
			schedulerVo.setRschFinalDate(rFinal);
			schedulerVo.setRequestRecvdDate(receivedDate);
			this.schedulerDAO.insertRecCaseNotificationAndDates(schedulerVo);
			this.logger.debug("Exiting SchedulerManager::insertRecCaseNotificationAndDates");
		} catch (CMSException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	private void updateSchedulerLastNextRunDate(List<String> crnList) throws CMSException {
		this.logger.debug("In SchedulerManager::updateSchedulerLastNextRunDate");

		try {
			this.schedulerDAO.updateSchedulerLastNextRunDate(crnList);
			this.logger.debug("Exiting SchedulerManager::updateSchedulerLastNextRunDate");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	private String calculateDate(String days) throws CMSException {
		this.logger.debug("In SchedulerManager::calculateDate");
		String newDate = "";

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy H:m:s");
			Calendar calendar = Calendar.getInstance();
			int local = false;
			this.logger.debug("number of days is " + days);
			calendar = Calendar.getInstance();
			if (days != null && !"".equalsIgnoreCase(days.trim())) {
				int local = Integer.parseInt(days);
				calendar.add(5, local);
				newDate = simpleDateFormat.format(calendar.getTime());
			}

			this.logger.debug("new date is " + newDate);
			this.logger.debug("Exiting SchedulerManager::calculateDate");
			return newDate;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private void calculateDueDates(SchedulerVo schedulerVo) throws CMSException {
		this.logger.debug("In SchedulerManager::calculateDueDates");
		String clientInterimDate1 = "";
		String clientInterimDate2 = "";
		String clientFinalDate = "";

		try {
			if (!schedulerVo.getInterimClientDueDays1().equals("0")) {
				clientInterimDate1 = this.calculateDate(String.valueOf(schedulerVo.getInterimClientDueDays1()));
				clientInterimDate1 = this.getAllDatesFormat((String) null, clientInterimDate1,
						schedulerVo.getOfficeName());
			} else {
				clientInterimDate1 = "";
			}

			if (!schedulerVo.getInterimClientDueDays2().equals("0")) {
				clientInterimDate2 = this.calculateDate(String.valueOf(schedulerVo.getInterimClientDueDays2()));
				clientInterimDate2 = this.getAllDatesFormat((String) null, clientInterimDate2,
						schedulerVo.getOfficeName());
			} else {
				clientInterimDate2 = "";
			}

			clientFinalDate = this.calculateDate(String.valueOf(schedulerVo.getFinalDueDays()));
			clientFinalDate = this.getAllDatesFormat((String) null, clientFinalDate, schedulerVo.getOfficeName());
			String[] atrArray;
			if (!clientInterimDate1.isEmpty()) {
				atrArray = clientInterimDate1.split("::");
				cInterim1 = atrArray[0];
				rInterim1 = atrArray[1];
			}

			if (!clientInterimDate2.isEmpty()) {
				atrArray = clientInterimDate2.split("::");
				cInterim2 = atrArray[0];
				rInterim2 = atrArray[1];
			}

			if (!clientFinalDate.isEmpty()) {
				atrArray = clientFinalDate.split("::");
				cFinal = atrArray[0];
				rFinal = atrArray[1];
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			receivedDate = simpleDateFormat.format(calendar.getTime());
			this.logger.debug("cInterim1 " + cInterim1);
			this.logger.debug("rInterim1 " + rInterim1);
			this.logger.debug("cInterim2 " + cInterim2);
			this.logger.debug("rInterim2 " + rInterim2);
			this.logger.debug("cFinal " + cFinal);
			this.logger.debug("rFinal " + rFinal);
			this.logger.debug("receivedDate " + receivedDate);
			this.logger.debug("Exiting SchedulerManager::calculateDueDates");
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public void createRecurrCase(String recCaseSchedulerId, String recClientCaseId, String performer, Session session)
			throws CMSException {
		this.logger.debug("in SchedulerManager::createRecurrCase");
		new SchedulerVo();
		long newCasePid = 0L;
		String newCrn = "";

		try {
			this.logger.debug("Step1 :- getLastRecurNumberAndDates ");
			SchedulerVo schedulerVo = this.schedulerDAO.getLastRecurNumberAndDates(recCaseSchedulerId);
			String lastRecurrNumber = String.valueOf(schedulerVo.getLastRecurrenceNumber());
			this.logger.debug("Last recurrence number from table is :- " + lastRecurrNumber);
			if (lastRecurrNumber.length() == 1) {
				lastRecurrNumber = "00" + lastRecurrNumber;
			} else if (lastRecurrNumber.length() == 2) {
				lastRecurrNumber = "0" + lastRecurrNumber;
			}

			long lastRecurrNumberLong = Long.parseLong(lastRecurrNumber);
			++lastRecurrNumberLong;
			this.logger.debug("new LRN in long>> " + lastRecurrNumberLong);
			this.updateLastRecurrenceNumber(recClientCaseId);
			String recurrCaseCrn = schedulerVo.getCRN();
			String lastRecurrNumberStr = "";
			lastRecurrNumberStr = String.valueOf(lastRecurrNumberLong);
			if (lastRecurrNumberStr.length() == 1) {
				lastRecurrNumberStr = "00" + lastRecurrNumberStr;
			} else if (lastRecurrNumberStr.length() == 2) {
				lastRecurrNumberStr = "0" + lastRecurrNumberStr;
			}

			newCrn = this.generateAndUpdateCrnInScheduler(recurrCaseCrn, lastRecurrNumberStr, recCaseSchedulerId);
			this.logger.debug("newCrn>>> " + newCrn);
			this.logger.debug("lastRecurrNumber>>> " + lastRecurrNumber);
			CaseDetails caseDetails = this.getcaseDetails(recClientCaseId);
			boolean local = false;
			if (caseDetails.getExpressCase() == 1) {
				local = true;
			}

			this.insertCustomData(newCrn, caseDetails.getCaseInfo(), caseDetails.getCaseMgrId(),
					caseDetails.getClientCode(), caseDetails.getClientRef(), caseDetails.getReportTypeId(),
					caseDetails.getSubReportTypeId(), caseDetails.getOfficeId(), local,
					schedulerVo.getRequestRecvdDate(), schedulerVo.getClientFinalDate(),
					schedulerVo.getClientInterm1Date(), schedulerVo.getClientInterm2Date(),
					schedulerVo.getRschFinalDate(), schedulerVo.getRschInterm1Date(), schedulerVo.getRschInterm2Date(),
					performer, recurrCaseCrn, session);
			newCasePid = this.getPidForCrn(newCrn);
			this.getAttachmentsForCase(recurrCaseCrn, newCasePid, session);
			this.logger.info("Recurrent cases is created using system notification with crn-> " + newCrn);
			this.logger.debug("Exiting SchedulerManager::createRecurrCase");
		} catch (Exception var16) {
			this.deleteCrnFromClientCase(newCrn);
			this.removePIIDAndCase(newCasePid);
			throw new CMSException(this.logger, var16);
		}
	}

	private void updateLastRecurrenceNumber(String recClientCaseId) throws CMSException {
		this.logger.debug("In SchedulerManager::updateLastRecurrneceNumber");

		try {
			HashMap<String, String> paramMap = new HashMap();
			paramMap.put("recClientCaseId", recClientCaseId);
			this.schedulerDAO.updateLastRecurrenceNumber(paramMap);
			this.logger.debug("Exiting SchedulerManager::updateLastRecurrneceNumber");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	private String generateAndUpdateCrnInScheduler(String crnOfRecurrenceSetup, String lastRecurNumber,
			String recCaseSchedulerId) throws CMSException {
		this.logger.debug("In SchedulerManager::generateAndUpdateCrnInScheduler");

		try {
			this.logger.debug(
					"crnOfRecurrenceSetup>> " + crnOfRecurrenceSetup + " and lastRecurNumber is:- " + lastRecurNumber);
			crnOfRecurrenceSetup = crnOfRecurrenceSetup.substring(0, crnOfRecurrenceSetup.lastIndexOf("\\"));
			crnOfRecurrenceSetup = crnOfRecurrenceSetup.concat("\\" + lastRecurNumber);
			this.logger.debug("crnOfRecurrenceSetup_new " + crnOfRecurrenceSetup);
			HashMap paramMap = new HashMap();
			paramMap.put("CRN", crnOfRecurrenceSetup);
			paramMap.put("recCaseSchedulerId", recCaseSchedulerId);
			this.schedulerDAO.updateNewCrnInScheduler(paramMap);
			this.logger.debug("Exiting SchedulerManager::generateAndUpdateCrnInScheduler");
			return crnOfRecurrenceSetup;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private void insertCustomData(String crn, String caseInformation, String caseManager, String clientName,
			String clientReference, String reportType, String subReportType, String branchOffice, boolean expressCase,
			String receivedDate, String cFinal, String cInterim1, String cInterim2, String rFinal, String rInterim1,
			String rInterim2, String performer, String recurrCaseCrn, Session session) throws CMSException {
		this.logger.debug("In SchedulerManager::insertCustomData");
		CaseDetails caseDetailsVO = new CaseDetails();
		caseDetailsVO.setCrn(crn);
		caseDetailsVO.setCaseInfo(caseInformation);
		caseDetailsVO.setCaseMgrId(caseManager);
		caseDetailsVO.setClientCode(clientName);
		caseDetailsVO.setClientRef(clientReference);
		caseDetailsVO.setReportTypeId(reportType);
		caseDetailsVO.setSubReportTypeId(subReportType);
		caseDetailsVO.setOfficeId(branchOffice);
		int local = 0;
		if (expressCase) {
			local = 1;
		}

		caseDetailsVO.setExpressCase(local);
		local = 0;
		caseDetailsVO.setIsRecurCase(local);

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			Date localDate = null;
			if (receivedDate != null && !"".equalsIgnoreCase(receivedDate.trim())) {
				this.logger.debug(
						"in if block of receive date.. The pattern followed here is " + simpleDateFormat.toPattern());
				this.logger.debug("receivedDate " + receivedDate);
				this.logger.debug(
						"receivedDate in timestamp " + new Timestamp(simpleDateFormat.parse(receivedDate).getTime()));
				caseDetailsVO.setReqRecdDate(new Timestamp(simpleDateFormat.parse(receivedDate).getTime()));
			}

			this.logger.debug("getReqRecdDate " + caseDetailsVO.getReqRecdDate());
			localDate = null;
			simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			if (cFinal != null && !"".equalsIgnoreCase(cFinal.trim())) {
				localDate = new Date(simpleDateFormat.parse(cFinal).getTime());
				caseDetailsVO.setFinalDueDate(localDate);
			}

			localDate = null;
			if (cInterim1 != null && !"".equalsIgnoreCase(cInterim1.trim())) {
				localDate = new Date(simpleDateFormat.parse(cInterim1).getTime());
				caseDetailsVO.setcInterim1(localDate);
			}

			localDate = null;
			if (cInterim2 != null && !"".equalsIgnoreCase(cInterim2.trim())) {
				localDate = new Date(simpleDateFormat.parse(cInterim2).getTime());
				caseDetailsVO.setcInterim2(localDate);
			}

			localDate = null;
			if (rFinal != null && !"".equalsIgnoreCase(rFinal.trim())) {
				localDate = new Date(simpleDateFormat.parse(rFinal).getTime());
				caseDetailsVO.setFinalRDueDate(localDate);
			}

			localDate = null;
			if (rInterim1 != null && !"".equalsIgnoreCase(rInterim1.trim())) {
				localDate = new Date(simpleDateFormat.parse(rInterim1).getTime());
				caseDetailsVO.setrInterim1(localDate);
			}

			localDate = null;
			if (rInterim2 != null && !"".equalsIgnoreCase(rInterim2.trim())) {
				localDate = new Date(simpleDateFormat.parse(rInterim2).getTime());
				caseDetailsVO.setrInterim2(localDate);
			}

			local = 0;
			caseDetailsVO.setIsAutoOfcAssign(local);
			caseDetailsVO.setCaseStatusId(3);
			caseDetailsVO.setClientReportStatus(0);
			caseDetailsVO.setCaseCreatorId(performer);
			caseDetailsVO.setUpdatedBy(performer);
			ResourceLocator.self().getCreateCaseManager().createCase(caseDetailsVO);
			long pid = this.createPIDForCrn(caseDetailsVO);
			this.updatePidForCase(crn, pid);
			this.fetchAndAddSubjectforCase(recurrCaseCrn, crn, pid, session);
			this.logger.debug("In SchedulerManager::insertCustomData");
		} catch (ParseException var26) {
			throw new CMSException(this.logger, var26);
		} catch (Exception var27) {
			throw new CMSException(this.logger, var27);
		}
	}

	private CaseDetails getcaseDetails(String recClientCaseId) throws CMSException {
		this.logger.debug(" in SchedulerManager::getcaseDetails");
		new CaseDetails();

		CaseDetails caseDetails;
		try {
			caseDetails = this.schedulerDAO.getcaseDetails(recClientCaseId);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug(" exit SchedulerManager::getcaseDetails");
		return caseDetails;
	}

	private void addSubjectToCase(List<SubjectDetails> subjectDetailsList, String crn, long pid, Session session)
			throws CMSException {
		this.logger.debug("In SchedulerManager::addSubjectToCase");

		try {
			Iterator iterator = subjectDetailsList.iterator();

			while (iterator.hasNext()) {
				SubjectDetails subjectDetails = (SubjectDetails) iterator.next();
				int baseSubjectID = subjectDetails.getSubjectId();
				this.logger.debug("Inserting subject name :- " + subjectDetails.getSubjectName() + "for case : " + crn);
				subjectDetails.setCrn(crn);
				int subjectID = this.schedulerDAO.addSubjectToCase(subjectDetails);
				this.logger.debug("addSubjectToCase::new subjectID -----" + subjectID);
				this.logger.debug("addSubjectToCase::base subjectID -----" + baseSubjectID);
				this.associateSubjectForCRN(baseSubjectID, subjectID);
				this.logCaseHistoryForAddSubject(crn, String.valueOf(pid), subjectDetails.getSubjectName(), "SYSTEM");
				if (subjectDetails.isPrimarySub()) {
					HashMap dsValues = new HashMap();
					dsValues.put("PrimarySubjectCountry", subjectDetails.getCountryName());
					dsValues.put("PrimarySubject", subjectDetails.getSubjectName());
					ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
				}
			}

		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private void fetchAndAddSubjectforCase(String recurCaseCrn, String newCaseCrn, long pid, Session session)
			throws CMSException {
		this.logger.debug("In SchedulerManager::getSubjectToCase");

		try {
			new ArrayList();
			HashMap<String, String> paramMap = new HashMap();
			paramMap.put("recurCaseCrn", recurCaseCrn);
			List<SubjectDetails> subjectDetailsList = this.schedulerDAO.getSubjectForCrn(paramMap);
			this.logger.debug("subjectDetailsList for parent case " + subjectDetailsList.size());
			this.addSubjectToCase(subjectDetailsList, newCaseCrn, pid, session);
			this.logger.debug("exiting SchedulerManager::getSubjectToCase");
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private String getAllDatesFormat(String reportType, String date, String officeName)
			throws CMSException, IOException {
		this.logger.debug("In SchedulerManager::getAllDatesFormat");
		String dt = "";

		try {
			int lcl = 0;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Calendar calendar = Calendar.getInstance();
			this.logger.debug("old date is " + date);
			if (date != null) {
				try {
					calendar.setTime(simpleDateFormat.parse(date));
				} catch (ParseException var11) {
					throw new CMSException(this.logger, var11);
				}
			}

			if (date == null) {
				lcl = this.schedulerDAO.getTurnAround(reportType);
			}

			if (lcl != 0 || date != null) {
				this.logger.debug("turn arround time is " + lcl);
				calendar.add(5, lcl);
				java.util.Date lclDate = calendar.getTime();
				this.logger.debug("lclDate is " + lclDate);
				boolean lclHolidayCheck = true;

				do {
					lclDate = calendar.getTime();
					if (!this.schedulerDAO.checkForHoliday(simpleDateFormat.format(lclDate), officeName)
							&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")
							&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Saturday")) {
						this.logger.debug("calendar.get(Calendar.DAY_OF_YEAR) " + calendar.get(6));
						this.logger.debug(
								"Calendar.getInstance().get(Calendar.DAY_OF_YEAR) " + Calendar.getInstance().get(6));
						if (calendar.get(6) <= Calendar.getInstance().get(6)
								&& calendar.get(1) <= Calendar.getInstance().get(1)) {
							dt = simpleDateFormat.format(Calendar.getInstance().getTime()) + "::"
									+ simpleDateFormat.format(Calendar.getInstance().getTime());
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
								if (!this.schedulerDAO.checkForHoliday(simpleDateFormat.format(lclDate), officeName)
										&& !(new SimpleDateFormat("EEEE")).format(lclDate).equalsIgnoreCase("Sunday")
										&& !(new SimpleDateFormat("EEEE")).format(lclDate)
												.equalsIgnoreCase("Saturday")) {
									if (calendar.get(6) <= Calendar.getInstance().get(6)
											&& calendar.get(1) <= Calendar.getInstance().get(1)) {
										dt = dt + simpleDateFormat.format(Calendar.getInstance().getTime());
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
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("Date is " + dt);
		this.logger.debug("exiting SchedulerManager::getAllDatesFormat");
		return dt;
	}

	private void updatePidForCase(String crn, long pid) throws CMSException {
		this.logger.debug("In SchedulerManager::updatePidForCase");

		try {
			Map<String, String> map = new HashMap();
			map.put("crn", crn);
			map.put("pid", String.valueOf(pid));
			this.schedulerDAO.updatePidForCase(map);
			this.logger.debug("Exiting SchedulerManager::updatePidForCase");
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private long createPIDForCrn(CaseDetails caseDetailsVO) throws CMSException {
		this.logger.debug("Insdie createPIDForCrn method of scheuler manager class");
		long piid = 0L;

		try {
			HashMap<String, Object> hmAttributes = new HashMap();
			hmAttributes.put("CaseCreator", caseDetailsVO.getCaseCreatorId());
			hmAttributes.put("CaseManager", caseDetailsVO.getCaseMgrId());
			hmAttributes.put("BranchOffice", caseDetailsVO.getOfficeId());
			hmAttributes.put("CRN", caseDetailsVO.getCrn());
			if (caseDetailsVO.getrInterim1() != null) {
				hmAttributes.put("RInterim1", this.convertDateForDS(caseDetailsVO.getrInterim1()));
			}

			if (caseDetailsVO.getrInterim2() != null) {
				hmAttributes.put("RInterim2", this.convertDateForDS(caseDetailsVO.getrInterim2()));
			}

			if (caseDetailsVO.getcInterim1() != null) {
				hmAttributes.put("CInterim1", this.convertDateForDS(caseDetailsVO.getcInterim1()));
			}

			if (caseDetailsVO.getcInterim2() != null) {
				hmAttributes.put("CInterim2", this.convertDateForDS(caseDetailsVO.getcInterim2()));
			}

			hmAttributes.put("RFinal", this.convertDateForDS(caseDetailsVO.getFinalRDueDate()));
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			String d1 = sdf.format(caseDetailsVO.getReqRecdDate());
			hmAttributes.put("ReceivedDate", d1);
			hmAttributes.put("ExpressCase", caseDetailsVO.getExpressCase() == 1);
			hmAttributes.put("ClientReference", caseDetailsVO.getClientRef());
			hmAttributes.put("ClientName", caseDetailsVO.getClientCode());
			hmAttributes.put("CFinal", this.convertDateForDS(caseDetailsVO.getFinalDueDate()));
			hmAttributes.put("CaseInformation", caseDetailsVO.getCaseInfo());
			hmAttributes.put("SubReportType", caseDetailsVO.getSubReportTypeId());
			hmAttributes.put("ReportType", caseDetailsVO.getReportTypeId());
			this.logger.debug("hmAttributes::::" + hmAttributes);
			HashMap map = ResourceLocator.self().getSBMService().createProcessInstance(hmAttributes);
			this.logger.debug("map is...." + map);
			piid = Long.parseLong((String) map.get("ProcessInstanceId"));
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("piid is:::::" + piid);
		return piid;
	}

	private String convertDateForDS(java.util.Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String d1 = sdf.format(d);
		return d1;
	}

	public int updateAcknowledgeDate(String recCaseSchedulerId, String updatedBy) throws CMSException {
		this.logger.debug("In SchedulerManager::updateAcknowledgeDate");
		boolean var3 = false;

		try {
			this.logger.debug("Going to update acknowledge date for : - recCaseSchedulerId " + recCaseSchedulerId);
			this.logger.debug("updatedBy " + updatedBy);
			Map<String, String> map = new HashMap();
			map.put("recCaseSchedulerId", recCaseSchedulerId);
			map.put("updatedBy", updatedBy);
			int updateCount = this.schedulerDAO.updateAcknowledgeDate(map);
			this.logger.debug("Exiting SchedulerManager::updateAcknowledgeDate");
			return updateCount;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private void associateSubjectForCRN(int baseSubjectID, int newSubjectId) throws CMSException {
		this.logger.debug("In SchedulerManager::associateSubjectForCRN");

		try {
			Map<String, String> map = new HashMap();
			map.put("baseSubjectId", String.valueOf(baseSubjectID));
			map.put("newSubjectId", String.valueOf(newSubjectId));
			this.schedulerDAO.insertAscSubject(map);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting SchedulerManager::associateSubjectForCRN");
	}

	private List<DocMapVO> getAttachmentsForCase(String crn, long newCasePid, Session session) throws CMSException {
		this.logger.debug("In SchedulerManager::getAttachmentsForCase");
		Map<String, String> map = new HashMap();
		new ArrayList();

		List DocMapVOList;
		try {
			map.put("crn", crn);
			DocMapVOList = this.schedulerDAO.getAttachmentsForCase(map);
			String filePath = "";
			this.logger.debug("Attachment added for case with pid(New Case PID):- " + newCasePid);
			int j = 0;
			Iterator iterator = DocMapVOList.iterator();

			while (true) {
				if (!iterator.hasNext()) {
					String tempFilePath = propertyReader.getTempFilePath();
					tempFilePath = tempFilePath + "//RecurrenceCaseAttach//";
					deleteFile(tempFilePath);
					break;
				}

				DocMapVO docMapVO = (DocMapVO) iterator.next();
				new HashMap();
				Map<String, Object> displayDocMap = ResourceLocator.self().getDocService()
						.displayDocument(docMapVO.getDocId());
				this.logger.debug("processing for DocId ::- " + docMapVO.getDocId());
				String docName = "";
				String creator = "";
				String path = "";
				String[] pathArr = (String[]) null;
				byte[] bytes = (byte[]) displayDocMap.get("bytes");
				docName = (String) displayDocMap.get("docName");
				creator = (String) displayDocMap.get("creator");
				path = (String) displayDocMap.get("path");
				this.logger.debug("docName:- " + docName + ",creator:- " + creator + ",path:- " + path);
				pathArr = path.split("/");
				path = pathArr[3];
				if (j == 0) {
					filePath = this.createFolderStructure(creator, String.valueOf(newCasePid), path);
				}

				++j;
				this.logger.debug("filePath:- " + filePath);
				this.createFile(docName, filePath, bytes);
				this.logger.debug("TempFilePath from property reader>> " + propertyReader.getTempFilePath());
				String[] tempPath = new String[1];
				String[] folderNames = new String[1];
				tempPath[0] = filePath + "//" + docName;
				folderNames[0] = path;
				this.logger.debug("Going to  call getDocService::CreateDocForRecurrentCase");
				List<String> newdocIdList = ResourceLocator.self().getDocService().CreateDocForRecurrentCase(creator,
						docMapVO.getTeamName(), tempPath, String.valueOf(newCasePid), folderNames, session);
				if (newdocIdList.size() > 0) {
					this.updateDocVersion((String) newdocIdList.get(0), docMapVO.getVersion());
				}
			}
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}

		this.logger.debug("Exiting SchedulerManager::getAttachmentsForCase");
		return DocMapVOList;
	}

	private void updateDocVersion(String docID, long version) throws CMSException {
		this.logger.debug("Inside SchedulerManager::updateDocVersion");
		DocMapVO docMapVO = new DocMapVO();

		try {
			docMapVO.setDocId(docID);
			docMapVO.setVersion(version);
			this.schedulerDAO.updateDocVersion(docMapVO);
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting SchedulerManager::updateDocVersion");
	}

	private static void deleteFile(String sFilePath) {
		File oFile = new File(sFilePath);
		if (oFile.isDirectory()) {
			File[] aFiles = oFile.listFiles();
			File[] var6 = aFiles;
			int var5 = aFiles.length;

			for (int var4 = 0; var4 < var5; ++var4) {
				File oFileCur = var6[var4];
				deleteFile(oFileCur.getAbsolutePath());
				oFileCur.delete();
			}
		}

	}

	private String createFolderStructure(String userId, String pid, String finalReportDummy) throws CMSException {
		String tempFilePath = propertyReader.getTempFilePath();
		File dir = new File(tempFilePath);
		if (!dir.exists()) {
			(new File(tempFilePath)).mkdirs();
		}

		String newtempFilePath = tempFilePath + "//RecurrenceCaseAttach//" + userId + "//" + pid + "//"
				+ finalReportDummy;
		File dirRecuurenceCaseAttach = new File(newtempFilePath);
		if (!dirRecuurenceCaseAttach.exists()) {
			(new File(newtempFilePath)).mkdirs();
		}

		return newtempFilePath;
	}

	private void createFile(String fileName, String filePath, byte[] bytes) throws CMSException {
		try {
			FileOutputStream fos = new FileOutputStream(filePath + "//" + fileName);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IOException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private long getPidForCrn(String crn) throws CMSException {
		long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
		this.logger.debug("pid " + pid);
		return pid;
	}

	private void logCaseHistoryForAddSubject(String crn, String pid, String subjectName, String performer)
			throws CMSException {
		this.logger.debug("In SchedulerManager::logCaseHistoryForAddSubject");
		SubjectDetails subjectDetail = new SubjectDetails();
		List<SubjectDetails> subjectList = new ArrayList();
		CaseDetails caseDetails = new CaseDetails();

		try {
			subjectDetail.setSubjectName(subjectName);
			subjectDetail.setUpdatedBy(performer);
			caseDetails.setCrn(crn);
			subjectList.add(subjectDetail);
			caseDetails.setSubjectList(subjectList);
			caseDetails.setUpdatedBy(subjectDetail.getUpdatedBy());
			CaseHistory caseHistoryOtherParam = new CaseHistory();
			caseHistoryOtherParam.setCRN(crn);
			caseHistoryOtherParam.setPid(pid);
			caseHistoryOtherParam.setProcessCycle("");
			caseHistoryOtherParam.setPerformer(performer);
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(new CaseDetails(), caseDetails,
					caseHistoryOtherParam, "Subject");
			this.logger.debug("Exiting SchedulerManager::logCaseHistoryForAddSubject");
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private void deleteCrnFromClientCase(String crn) throws CMSException {
		this.logger.debug("In SchedulerManager::deleteCrnFromClientCase");

		try {
			Map<String, String> map = new HashMap();
			map.put("CRN", crn);
			this.schedulerDAO.deleteCaseFromClientCase(map);
			this.logger.debug("Exiting SchedulerManager::deleteCrnFromClientCase");
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	private void removePIIDAndCase(long piid) throws CMSException {
		ResourceLocator.self().getSBMService().removeProcessInstance(piid);
	}

	public void sendNotificationForOverdueCases() throws CMSException {
		this.logger.debug("In SchedulerManager::sendNotificationForOverdueCases");
		List<OverdueCasesSchedulerVo> overDueCasesList = this.schedulerDAO.getOverdueCases();
		Iterator iterator = overDueCasesList.iterator();

		while (iterator.hasNext()) {
			OverdueCasesSchedulerVo overdueCasesSchVo = (OverdueCasesSchedulerVo) iterator.next();
			String userStr = overdueCasesSchVo.getEligibleUsersForNotification();
			List<String> userList = StringUtils.commaSeparatedStringToList(userStr);
			HashMap<String, String> userMap = new HashMap();
			Iterator iterator2 = userList.iterator();

			while (iterator2.hasNext()) {
				String userName = (String) iterator2.next();
				if (userName != null && !userName.isEmpty()) {
					userMap.put(userName, userName);
				}
			}

			List<String> list = new ArrayList(userMap.keySet());
			ResourceLocator.self().getNotificationService().createSystemNotification(overdueCasesSchVo.getOverdueMsg(),
					overdueCasesSchVo.getOverdueMsg(), list, overdueCasesSchVo.getCrn());
		}

	}

	public void sendEmailPrepayementCase() throws CMSException, ParseException {
		this.logger.debug("In SchedulerManager:::: sendEmailPrepayementCase method");
		List<EmailPrepaymentCaseVo> emailPreapaymentCaseList = this.schedulerDAO.sendEmailPrepayementCase();
		String from = "";
		String bcc = "";
		String subject = "";
		String setText = "";
		String totalBalanceUSD = "not available";
		String receivedDateinString = "";
		SimpleDateFormat sm = new SimpleDateFormat("yy-M-dd");
		SimpleDateFormat sm1 = new SimpleDateFormat("dd-MMM-yyyy");
		java.util.Date systemDate = new java.util.Date();
		Calendar cal = Calendar.getInstance();
		String host = propertyReader.getHostEmailPrepayment();
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);

		try {
			Iterator iterator = emailPreapaymentCaseList.iterator();

			while (iterator.hasNext()) {
				String toFinance = "";
				EmailPrepaymentCaseVo emailPrepaymentCaseVo = (EmailPrepaymentCaseVo) iterator.next();
				emailPrepaymentCaseVo.setCaseDueDate(emailPrepaymentCaseVo.getCaseDueDate().substring(0, 10));
				java.util.Date receivedDate = sm.parse(emailPrepaymentCaseVo.getCaseDueDate());
				emailPrepaymentCaseVo.setCaseDueDate(sm1.format(receivedDate));
				cal.setTime(receivedDate);
				cal.add(5, 30);
				receivedDate = cal.getTime();
				receivedDateinString = sm1.format(receivedDate);
				totalBalanceUSD = "not available";
				if (emailPrepaymentCaseVo.getTotalBalanceUSD() != null) {
					totalBalanceUSD = "USD " + emailPrepaymentCaseVo.getTotalBalanceUSD().trim();
				}

				this.logger.debug("emailPrepaymentCaseVo.getEmailCM() -- " + emailPrepaymentCaseVo.getEmailCM());
				this.logger.debug("emailPrepaymentCaseVo.getEmailBDM() -- " + emailPrepaymentCaseVo.getEmailBDM());
				MimeMessage message = new MimeMessage(session);
				toFinance = propertyReader.getEmailFinanceTR();
				bcc = propertyReader.getEmailInternalPSI();
				from = propertyReader.getEmailFrom();
				this.logger.debug("bcc=" + bcc + " to=" + toFinance + " from=" + from);
				subject = "Due Date Notification: Subscription Sales Plan for " + emailPrepaymentCaseVo.getCrn();
				setText = "<b>Subscription Sales Plan details:</b><br /><br />";
				setText = setText + "<b>CRN -</b> " + emailPrepaymentCaseVo.getCrn() + "<br />";
				setText = setText + "<b>Due Date -</b> " + emailPrepaymentCaseVo.getCaseDueDate() + "<br />";
				setText = setText + "<b>Balance -</b> " + totalBalanceUSD + "<br />";
				setText = setText + "<b>Bonus credit expiry date -</b> " + receivedDateinString + "<br /><br />";
				setText = setText + "If the Subscription Sales Plan is not renewed before this date, "
						+ "the bonus credit would be forfeited automatically.<br /><br />";
				setText = setText + "Please engage with client to renew the annual investment plan. "
						+ "If you have already done so, just ignore the email.<br /><br />";
				setText = setText + "This is an auto generated email.<br />Please do not reply.";
				message.setFrom(new InternetAddress(from));
				message.addRecipient(RecipientType.TO, new InternetAddress(toFinance));
				message.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
				message.setSentDate(systemDate);
				message.setSubject(subject);
				message.setContent(setText, "text/html");
				Transport.send(message);
				this.logger.debug("Email TEXT******" + setText);
				this.logger.debug("Messages successfully sent");
			}
		} catch (MessagingException var20) {
			this.logger.error(var20);
		}

	}

	public void removeTempFeedbackAttach() throws CMSException, ParseException, IOException {
		this.logger.debug("In SchedulerManager:::: removeTempFeedbackAttach method");
		List<String> listOfFiles = this.schedulerDAO.getTempFeedbackAttach();
		if (listOfFiles != null && listOfFiles.size() > 0) {
			File directory = null;
			Iterator it = listOfFiles.iterator();

			while (it.hasNext()) {
				directory = new File((String) it.next());
				if (directory.exists() && directory.isDirectory()) {
					FileUtils.deleteDirectory(directory);
				}
			}

			this.logger.debug("Calling removeTempFeedbackAttach method to delete enteries from database ");
			this.schedulerDAO.removeTempFeedbackAttach(listOfFiles);
			this.logger.debug("Files enteries removed from database");
		}

		this.logger.debug("Exit SchedulerManager:::: removeTempFeedbackAttach method");
	}
}