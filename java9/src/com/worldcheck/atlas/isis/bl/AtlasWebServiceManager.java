package com.worldcheck.atlas.isis.bl;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.ArrayOf_xsd_anyType;
import com.worldcheck.atlas.isis.bl.interfaces.IAtlasWebServiceManager;
import com.worldcheck.atlas.isis.dao.AtlasWebServiceDAO;
import com.worldcheck.atlas.isis.util.AtlasWebServiceUtil;
import com.worldcheck.atlas.isis.util.DataValidator;
import com.worldcheck.atlas.isis.util.DownLoadBatchFiles;
import com.worldcheck.atlas.isis.util.WebServicePropertyReaderUtil;
import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import com.worldcheck.atlas.isis.vo.ReDetailsVO;
import com.worldcheck.atlas.isis.vo.ResultSubjectVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CaseInfo;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.sbm.customds.CycleTeamMapping;
import com.worldcheck.atlas.services.document.LegacyDocAttachmentService;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;

public class AtlasWebServiceManager implements IAtlasWebServiceManager {
	private AtlasWebServiceDAO atlasWebServiceDAO;
	private DataValidator dataValidator;
	private DownLoadBatchFiles downLoadBatchFiles;
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.bl.AtlasWebServiceManager");
	private LegacyDocAttachmentService legacyDocAttachmentService;

	public void setLegacyDocAttachmentService(LegacyDocAttachmentService legacyDocAttachmentService) {
		this.legacyDocAttachmentService = legacyDocAttachmentService;
	}

	public void setAtlasWebServiceDAO(AtlasWebServiceDAO atlasWebServiceDAO) {
		this.atlasWebServiceDAO = atlasWebServiceDAO;
	}

	public void setDataValidator(DataValidator dataValidator) {
		this.dataValidator = dataValidator;
	}

	public void setDownLoadBatchFiles(DownLoadBatchFiles downLoadBatchFiles) {
		this.downLoadBatchFiles = downLoadBatchFiles;
	}

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public CaseResultVO createCaseForISIS(CaseDetailsVO caseDetailsVO) throws CMSException {
		this.logger.debug("Inside createCaseForISIS method of AtlasWebServiceManager class");
		String crn = null;
		long piid = 0L;
		CaseResultVO caseResultVO = new CaseResultVO();

		try {
			this.logger.debug("going to validate soap header credentials");
			boolean validationFlag = this.validateHeaderCredentials();
			if (!validationFlag) {
				caseResultVO.setErrorCode("COC_1");
				caseResultVO.setErrorMessage("SOAP header credential mismatch");
				caseResultVO.setSuccess(false);
			} else {
				crn = this.atlasWebServiceDAO.checkExistenceforOrder(caseDetailsVO.getOrderGUID());
				ArrayOf_xsd_anyType subjectArray;
				if (crn != null && crn != "") {
					subjectArray = this.getSubjectListforAEDDOCase(crn);
					caseResultVO.setCrn(crn);
					caseResultVO.setResultSubjectDetails(subjectArray);
					caseResultVO.setStatus(caseDetailsVO.isIsBudgetConfirmed() ? "WIP" : "PA");
					caseResultVO.setErrorCode("COC_0");
					caseResultVO.setErrorMessage("");
					caseResultVO.setSuccess(true);
					return caseResultVO;
				}

				this.logger.debug("Order GUID for create case request is::::" + caseDetailsVO.getOrderGUID());
				this.logger.debug("going to validating data for case creation");
				this.logger.debug("########################################################");
				this.logger.debug("getAssignmentType:" + caseDetailsVO.getAssignmentType());
				this.logger.debug("getBranchType:" + caseDetailsVO.getBranchType());
				this.logger.debug("getBudget:" + caseDetailsVO.getBudget());
				this.logger.debug("getBulkOrder:" + caseDetailsVO.getBulkOrder());
				this.logger.debug("getBulkOrderId:" + caseDetailsVO.getBulkOrderId());
				this.logger.debug("getAssignmentType:" + caseDetailsVO.getCaseFlag());
				this.logger.debug("getCaseFlag:" + caseDetailsVO.getCaseManager());
				this.logger.debug("getClientCode:" + caseDetailsVO.getClientCode());
				this.logger.debug("getClientReferenceNumber:" + caseDetailsVO.getClientReferenceNumber());
				this.logger.debug("getCrn:" + caseDetailsVO.getCrn());
				this.logger.debug("getCurrencyCode:" + caseDetailsVO.getCurrencyCode());
				this.logger.debug("getExpressCase:" + caseDetailsVO.getExpressCase());
				this.logger.debug("getIsisUserEmailId:" + caseDetailsVO.getIsisUserEmailId());
				this.logger.debug("getIsisUserWhoPlacedOrder:" + caseDetailsVO.getIsisUserWhoPlacedOrder());
				this.logger.debug("getOfficeAssignment:" + caseDetailsVO.getOfficeAssignment());
				this.logger.debug("getOrderGUID:" + caseDetailsVO.getOrderGUID());
				this.logger.debug("getPriority:" + caseDetailsVO.getPriority());
				this.logger.debug("getReportTypeId:" + caseDetailsVO.getReportTypeId());
				this.logger.debug("getSpecialInstruction:" + caseDetailsVO.getSpecialInstruction());
				this.logger.debug("getSubReportType:" + caseDetailsVO.getSubReportType());
				this.logger.debug("getTaxCode:" + caseDetailsVO.getTaxCode());
				this.logger.debug("getClientFinalDueDate:" + caseDetailsVO.getClientFinalDueDate());
				this.logger.debug("getFileDetails:" + caseDetailsVO.getFileDetails());
				this.logger.debug("getOrderReceiptDate:" + caseDetailsVO.getOrderReceiptDate());
				this.logger.debug("getResearchFinalDueDate:" + caseDetailsVO.getResearchFinalDueDate());
				this.logger.debug("getSubjectDetails:" + caseDetailsVO.getSubjectDetails());
				this.logger.debug("isIsBudgetConfirmed():" + caseDetailsVO.isIsBudgetConfirmed());
				this.logger.debug("caseDetailsVO.getSubjectDetails()():" + caseDetailsVO.getSubjectDetails());
				this.logger.debug("getsLSubReportTypeFlag:" + caseDetailsVO.isIsSLSubReportType());
				this.logger.debug("getCaseLevelSubreportTypeCode:" + caseDetailsVO.getClSubreportCode());
				this.logger.debug("########################################################");
				if (caseDetailsVO.isIsSLSubReportType()) {
					caseDetailsVO.setSubReportType("0");
					caseDetailsVO.setClSubreportID(0);
				} else if (caseDetailsVO.getClSubreportCode() != null
						&& caseDetailsVO.getClSubreportCode().trim().length() > 3) {
					try {
						caseDetailsVO.setClSubreportID(
								Integer.parseInt(caseDetailsVO.getClSubreportCode().trim().substring(3)));
					} catch (NumberFormatException var15) {
						caseDetailsVO.setClSubreportID(0);
					}

					caseDetailsVO.setSubReportType("" + caseDetailsVO.getClSubreportID());
				}

				Map validationMap = this.dataValidator.validateMasterDataForCreateCase(caseDetailsVO);
				this.logger.debug("data validation successful for case creation");
				int officeId = this.getOfficeDetailsForCase(caseDetailsVO.getCaseManager());
				this.logger.debug("office id for case manager is:::" + officeId);
				caseDetailsVO.setBranchType(Integer.toString(officeId));
				crn = this.createISISCase(caseDetailsVO);
				this.logger.debug("genrated crn is::::::::" + crn);
				List<Long> pidList = this.atlasWebServiceDAO.getPIDListfromSavvionforCRN(crn);
				this.logger.debug("List of pid from removal::" + pidList);
				if (pidList != null && pidList.size() > 0) {
					Iterator it = pidList.iterator();

					while (it.hasNext()) {
						this.removePIID((Long) it.next());
					}
				}

				this.logger.debug("PID removal done sucessfully.....");
				caseDetailsVO.setCrn(crn);
				boolean AutoOAFlag = this.validationForOA(caseDetailsVO, validationMap);
				this.logger.debug("AutoOAFlag value is::::::" + AutoOAFlag);
				piid = this.createPIDForISIS(caseDetailsVO, AutoOAFlag);
				if (piid <= 0L) {
					throw new CMSException();
				}

				this.logger.debug("genrated piid is::::" + piid);
				this.insertToClinetCase(caseDetailsVO, piid);
				this.logger.debug("inserted to client case successfully..");
				this.insertTOAccountsAndNotification(caseDetailsVO);
				this.logger.debug("inserted to accounts table successfully..");
				this.logger.debug("going to add subjects to case....");
				subjectArray = this.createSubjectForISISCase(caseDetailsVO);
				String assignmentType = caseDetailsVO.getAssignmentType();
				this.logger.debug("Assignment type is:::" + assignmentType);
				if (AutoOAFlag) {
					String ptResearchHead = caseDetailsVO.getOfficeAssignment();
					this.logger.debug("going to complete office assignemnt task...");
					ResourceLocator.self().getOfficeAssignmentService().completeOAForISISCase(crn, Long.toString(piid),
							this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId(), ptResearchHead);
				}

				this.logger.debug("going to complete Completecasecreation task for piid" + piid);
				this.isRiskAssociatedWithCase(crn);
				this.completeComplteCaseCreation(piid);
				if (!caseDetailsVO.isIsBudgetConfirmed()) {
					ResourceLocator.self().getCacheService().updateBudgetCountCache();
				}

				if (caseDetailsVO.getFileDetails() != null && caseDetailsVO.getFileDetails().getItem() != null
						&& caseDetailsVO.getFileDetails().getItem().length > 0) {
					this.logger.debug("going to start thread for download files from FTP..");
					if (this.webServicePropertyReaderUtil.isFtpMonitoring()) {
						List filesList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
								caseDetailsVO.getFileDetails().getItem(), "CaseFileDetailsVO");
						this.atlasWebServiceDAO.insertToCMSFtpEntries(filesList, crn);
					}

					this.downLoadFilesFromFTP(caseDetailsVO, piid);
				} else {
					this.logger.debug("There is no files to download from FTP in create case request");
				}

				this.logger.debug("Going to send notification for create case through ISIS");
				this.sendNotification(crn, caseDetailsVO.getCaseManager());
				this.logger.debug("After sent notification..");
				this.logger.debug("Success... Going to set success vo for create case");
				caseResultVO.setCrn(crn);
				caseResultVO.setResultSubjectDetails(subjectArray);
				caseResultVO.setStatus(caseDetailsVO.isIsBudgetConfirmed() ? "WIP" : "PA");
				caseResultVO.setErrorCode("COC_0");
				caseResultVO.setErrorMessage("");
				caseResultVO.setSuccess(true);
			}

			return caseResultVO;
		} catch (IllegalArgumentException var16) {
			this.logger.error("Some error occured while creating case for ISIS");
			this.logger.error("Error message is::::" + var16.getMessage());
			caseResultVO.setErrorCode("COC_3");
			caseResultVO.setErrorMessage(var16.getMessage());
			caseResultVO.setSuccess(false);
			throw var16;
		} catch (CMSException var17) {
			this.logger.error("Some error occured while creating case for ISIS");
			this.logger.error("Error message is::::" + var17.getMessage());
			caseResultVO.setErrorCode("COC_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for create case");
			caseResultVO.setSuccess(false);
			throw var17;
		} catch (Exception var18) {
			this.logger.error("Some error occured while creating case for ISIS");
			this.logger.error("Error message is::::" + var18.getMessage());
			caseResultVO.setErrorCode("COC_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for create case");
			caseResultVO.setSuccess(false);
			throw new CMSException(this.logger, var18);
		}
	}

	public CaseResultVO updateCaseForISIS(CaseDetailsVO caseDetailsVO) throws ParseException, CMSException {
		this.logger.debug("Inside updateCaseForISIS method of AtlasWebServiceManager class");
		CaseResultVO caseResultVO = new CaseResultVO();
		ArrayOf_xsd_anyType subjectArray = new ArrayOf_xsd_anyType();
		this.logger.debug("Inside updateCaseForISIS method of AtlasWebServiceManager");
		this.logger.debug("crn is:::::::" + caseDetailsVO.getCrn());

		try {
			this.logger.debug("going to validate soap header credentials");
			boolean validationFlag = this.validateHeaderCredentials();
			if (!validationFlag) {
				caseResultVO.setErrorCode("UOC_1");
				caseResultVO.setErrorMessage("SOAP header credential mismatch");
				caseResultVO.setSuccess(false);
			} else {
				this.logger.debug("Going to check if same crn in Atlas queue");
				boolean isCRNInQueueFlag = this.isCRNInQueue(caseDetailsVO.getCrn());
				if (isCRNInQueueFlag) {
					caseResultVO.setErrorCode("UOC_2");
					caseResultVO.setErrorMessage("Same CRN exist in queue");
					caseResultVO.setSuccess(false);
				} else {
					this.logger.debug("going to validate data for update case..");
					this.dataValidator.validateMasterDataForUpdateCase(caseDetailsVO);
					this.logger.debug("after data validation for update case..");
					String caseManagerId = this.atlasWebServiceDAO.getCaseManagerIdForCase(caseDetailsVO.getCrn());
					this.logger.debug("caseManagerId::::::" + caseManagerId);
					caseDetailsVO.setCaseManager(caseManagerId);
					String caseFlag = caseDetailsVO.getCaseFlag();
					this.logger.debug("case flag value is:::" + caseFlag);
					List<String> flagList = this.convertCommaStringToList(caseFlag);
					this.logger.debug("flagList is::" + flagList);
					if (flagList.contains("S") || flagList.contains("s")) {
						subjectArray = this.updateSubjects(caseDetailsVO, caseDetailsVO.getCrn());
					}

					if (flagList.contains("F") || flagList.contains("f")) {
						this.updateFiles(caseDetailsVO);
						this.sendFileUploadNotificationForUpdateCase(caseDetailsVO.getCrn());
					}

					if (flagList.contains("C") || flagList.contains("c")) {
						this.updateISISCase(caseDetailsVO);
					}

					this.logger.debug("Success... Going to set success vo for update case");
					caseResultVO.setCrn(caseDetailsVO.getCrn());
					caseResultVO.setResultSubjectDetails(subjectArray);
					caseResultVO.setStatus("");
					caseResultVO.setErrorCode("UOC_0");
					caseResultVO.setErrorMessage("");
					caseResultVO.setSuccess(true);
				}
			}

			return caseResultVO;
		} catch (IllegalArgumentException var9) {
			this.logger.error("Some error occured while updating case for ISIS");
			this.logger.error("Error message is::::" + var9.getMessage());
			caseResultVO.setCrn(caseDetailsVO.getCrn());
			caseResultVO.setErrorCode("UOC_3");
			caseResultVO.setErrorMessage(var9.getMessage());
			caseResultVO.setSuccess(false);
			throw var9;
		} catch (CMSException var10) {
			this.logger.error("Some error occured while updating case for ISIS");
			this.logger.error("Error message is::::" + var10.getMessage());
			caseResultVO.setCrn(caseDetailsVO.getCrn());
			caseResultVO.setErrorCode("UOC_4");
			caseResultVO.setSuccess(false);
			caseResultVO.setErrorMessage("Logical error occured while processing data for update case");
			throw var10;
		} catch (Exception var11) {
			this.logger.error("Some error occured while updating case for ISIS");
			this.logger.error("Error message is::::" + var11.getMessage());
			caseResultVO.setCrn(caseDetailsVO.getCrn());
			caseResultVO.setErrorCode("UOC_4");
			caseResultVO.setSuccess(false);
			caseResultVO.setErrorMessage("Logical error occured while processing data for update case");
			throw new CMSException(this.logger, var11);
		}
	}

	public DownloadOnlineReportResultVO downloadOnlineReport(String crn, String fileName, String version)
			throws CMSException {
		this.logger.debug("Inside downloadOnlineReport method of AtlasWebServiceManager");
		DownloadOnlineReportResultVO downloadOnlineReportResultVO = new DownloadOnlineReportResultVO();
		byte[] docBytes = (byte[]) null;
		String docId = "";

		try {
			this.logger.debug("going to validate soap header credentials");
			boolean validationFlag = this.validateHeaderCredentials();
			if (!validationFlag) {
				downloadOnlineReportResultVO.setErrorCode("DOR_1");
				downloadOnlineReportResultVO.setErrorMessage("SOAP header credential mismatch");
				downloadOnlineReportResultVO.setSuccess(false);
			} else {
				this.logger.debug("Going to check if same crn in Atlas queue");
				boolean isCRNInQueueFlag = this.isCRNInQueue(crn);
				if (isCRNInQueueFlag) {
					downloadOnlineReportResultVO.setErrorCode("DOR_2");
					downloadOnlineReportResultVO.setErrorMessage("Same CRN exist in queue");
					downloadOnlineReportResultVO.setSuccess(false);
				} else {
					this.logger.debug("Going to validate data...");
					this.dataValidator.validateDownloadReportData(crn, fileName, version);
					this.logger.debug("crn is:::::::" + crn + ":::::::fileName is::::" + fileName
							+ "::::::version is::::" + version);
					long piid = 0L;

					try {
						piid = this.atlasWebServiceDAO.getPIIDForCRN(crn);
					} catch (Exception var18) {
						piid = 0L;
					}

					this.logger.debug("pid for given crn is:::" + piid);
					if (piid > 0L) {
						docId = this.atlasWebServiceDAO.getDocId(piid, fileName, new Long(version));
						this.logger.debug("docId is:::::::" + docId);
						if (docId == null || docId.equals("")) {
							throw new IllegalArgumentException("No Document found for given information");
						}

						Map documentMap = ResourceLocator.self().getDocService().displayDocument(docId);
						docBytes = (byte[]) documentMap.get("bytes");
					} else {
						this.logger.debug(
								"Given CRN doesn't have PID with Atlas so going to legacy DB for getting final document.");
						int count = this.atlasWebServiceDAO.validateLegacyDocRequest(crn, fileName, version);
						this.logger.debug(
								"count Value to validate Legacy doc request, if it is one then request is valid.");
						if (count != 1) {
							throw new IllegalArgumentException(
									"Given CRN doesn't have valid PID with atlas OR Given CRN does not have valid data with Legacy DB");
						}

						int index = true;
						String newFileNameTemp = "";
						String fileNameWithVersion = "";
						int index = fileName.lastIndexOf(".");
						if (index != -1) {
							newFileNameTemp = fileName.substring(0, index);
							newFileNameTemp = newFileNameTemp + "_" + Float.valueOf(version)
									+ fileName.substring(index);
							fileNameWithVersion = newFileNameTemp;
						} else {
							fileNameWithVersion = fileNameWithVersion + "_" + Float.valueOf("1");
						}

						String folderNameWithCRN = "FINAL-REPORT\\" + crn.replace("\\", "~");
						this.logger.debug("fileNameWithVersion is::" + fileNameWithVersion);
						this.logger.debug("folderNameWithCRN is::" + folderNameWithCRN);

						try {
							this.logger.debug(
									"going to call legacyDocAttachmentService" + this.legacyDocAttachmentService);
							docBytes = this.legacyDocAttachmentService.showLegacyDocument(folderNameWithCRN,
									fileNameWithVersion);
							this.logger.debug("After  call legacyDocAttachmentService " + docBytes);
						} catch (IOException var17) {
							throw new IllegalArgumentException("Got Error While Getting final doc from legacy data.");
						}
					}

					this.logger.debug("docBytes object is:::: " + docBytes);
					if (docBytes == null) {
						throw new CMSException();
					}

					this.logger.debug("docBytes is not null......");
					downloadOnlineReportResultVO.setCrn(crn);
					downloadOnlineReportResultVO.setErrorCode("DOR_0");
					downloadOnlineReportResultVO.setErrorMessage("");
					downloadOnlineReportResultVO.setFileContent(docBytes);
					downloadOnlineReportResultVO.setSuccess(true);
				}
			}

			return downloadOnlineReportResultVO;
		} catch (IllegalArgumentException var19) {
			downloadOnlineReportResultVO.setCrn(crn);
			downloadOnlineReportResultVO.setErrorCode("DOR_3");
			downloadOnlineReportResultVO.setErrorMessage(var19.getMessage());
			downloadOnlineReportResultVO.setSuccess(false);
			throw var19;
		} catch (CMSException var20) {
			downloadOnlineReportResultVO.setCrn(crn);
			downloadOnlineReportResultVO.setErrorCode("DOR_4");
			downloadOnlineReportResultVO
					.setErrorMessage("Logical error occured while processing data for download report");
			downloadOnlineReportResultVO.setSuccess(false);
			throw var20;
		}
	}

	public CaseResultVO cancelOnlineOrder(String crn) throws CMSException {
		this.logger.debug("Inside cancelOnlineOrder method of AtlasWebServiceManager class");
		CaseResultVO caseResultVO = new CaseResultVO();

		try {
			this.logger.debug("going to validate soap header credentials");
			boolean validationFlag = this.validateHeaderCredentials();
			if (!validationFlag) {
				caseResultVO.setCrn(crn);
				caseResultVO.setErrorCode("COO_1");
				caseResultVO.setErrorMessage("SOAP header credential mismatch");
				caseResultVO.setSuccess(false);
			} else {
				this.logger.debug("Going to check if same crn in Atlas queue");
				boolean isCRNInQueueFlag = this.isCRNInQueue(crn);
				if (isCRNInQueueFlag) {
					caseResultVO.setCrn(crn);
					caseResultVO.setErrorCode("COO_2");
					caseResultVO.setErrorCode("Same CRN exist in queue");
					caseResultVO.setSuccess(false);
				} else if (!this.atlasWebServiceDAO.checkCRNInDB(crn)) {
					caseResultVO.setCrn(crn);
					caseResultVO.setErrorCode("COO_3");
					caseResultVO.setErrorCode("CRN doesn't exist with Atlas.. ");
					caseResultVO.setSuccess(false);
				} else {
					CaseDetails oldCaseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
					ResourceLocator.self().getCaseDetailService().updateCaseStatus(crn,
							this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId(), "Cancelled");
					Session session = ResourceLocator.self().getSBMService()
							.getSession(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
					HashMap<String, CycleTeamMapping> customDSMap = (HashMap) ResourceLocator.self().getSBMService()
							.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "customDSMap", session);
					this.logger.debug("customDSMap is " + customDSMap);
					Map<String, CycleInfo> cycleInformation = null;
					if (customDSMap != null) {
						CycleTeamMapping cycleTeamMapping = (CycleTeamMapping) customDSMap.get("CycleTeamMapping");
						this.logger.debug("cycleTeamMapping is " + cycleTeamMapping);
						String currentCycle = cycleTeamMapping.getCurrentCycle();
						this.logger.debug("currentCycle is " + currentCycle);
						cycleInformation = cycleTeamMapping.getCycleInformation();
						this.logger.debug("cycleInformation is " + cycleInformation);
						HashMap<String, Object> dsMap = new HashMap();
						dsMap.put("CaseStatus", "Cancelled");
						ResourceLocator.self().getFlowService().updateDataslotsForCase(session, cycleInformation,
								dsMap);
					}

					CaseDetails newCaseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
					this.logger
							.debug("Long.parseLong(caseDetails.getPid()) " + Long.parseLong(oldCaseDetails.getPid()));
					ResourceLocator.self().getCaseDetailService().isisCancelCaseNotification(oldCaseDetails,
							newCaseDetails, this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
					CaseHistory caseHistory = new CaseHistory();
					caseHistory.setPid(oldCaseDetails.getPid());
					caseHistory.setCRN(oldCaseDetails.getCrn());
					caseHistory.setPerformer(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
					caseHistory.setTaskName("");
					caseHistory.setTaskStatus("");
					caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
							.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
					newCaseDetails
							.setCaseHistoryPerformer(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
					ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
							caseHistory, "caseDetails");
					this.logger.debug("Success... Going to set success vo for cancel online case");
					caseResultVO.setCrn(crn);
					caseResultVO.setStatus("");
					caseResultVO.setErrorCode("COO_0");
					caseResultVO.setErrorMessage("");
					caseResultVO.setSuccess(true);
					AccountsVO accountsVO = new AccountsVO();
					accountsVO.setCrn(crn);
					this.atlasWebServiceDAO.updateAccountDetails(accountsVO);
				}
			}

			return caseResultVO;
		} catch (CMSException var12) {
			this.logger.error("Some error occured while canceling case for ISIS");
			this.logger.error("Error message is::::" + var12.getMessage());
			caseResultVO.setCrn(crn);
			caseResultVO.setErrorCode("COO_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for cancel case");
			caseResultVO.setSuccess(false);
			throw var12;
		}
	}

	public String createISISCase(CaseDetailsVO caseDetailsVO) throws CMSException {
		this.logger.debug("Insdie createISISCase method of AtlasWebServiceManager class");
		String crn = "";

		try {
			CaseDetails createCaseCaseDetils = new CaseDetails();
			String reportTypeName = (String) this.dataValidator.getReportTypeMapFromCache()
					.get(caseDetailsVO.getReportTypeId());
			createCaseCaseDetils.setReportType(reportTypeName);
			createCaseCaseDetils.setClientCode(caseDetailsVO.getClientCode());
			createCaseCaseDetils.setSubReportType(caseDetailsVO.getSubReportType());
			createCaseCaseDetils.setclSubreportID(caseDetailsVO.getClSubreportID());
			createCaseCaseDetils.setReqRecdDate(this.convertDateToTimeStamp(caseDetailsVO.getOrderReceiptDate()));
			crn = ResourceLocator.self().getTaskService().generateCRN(createCaseCaseDetils, false);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("generated crn is::::::" + crn);
		return crn;
	}

	public long createPIDForISIS(CaseDetailsVO caseDetailsVO, boolean AutoOAFlag) throws CMSException {
		this.logger.debug("Insdie createPIDForISIS method of AtlasWebServiceManager class");
		long piid = 0L;

		try {
			String reportTypeName = (String) this.dataValidator.getReportTypeMapFromCache()
					.get(caseDetailsVO.getReportTypeId());
			String clientName = (String) this.dataValidator.getClientMapFromCache().get(caseDetailsVO.getClientCode());
			String branchOfficeName = (String) this.dataValidator.getOfficeMapFromCache()
					.get(new Integer(caseDetailsVO.getBranchType()));
			HashMap<String, Object> hmAttributes = new HashMap();
			hmAttributes.put("CaseCreator", this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			hmAttributes.put("CaseManager", caseDetailsVO.getCaseManager());
			hmAttributes.put("BranchOffice", branchOfficeName);
			hmAttributes.put("CRN", caseDetailsVO.getCrn());
			hmAttributes.put("RFinal", this.convertDateForDS(caseDetailsVO.getResearchFinalDueDate()));
			hmAttributes.put("ReceivedDate", this.convertReceivedDateDateForDS(caseDetailsVO.getOrderReceiptDate()));
			hmAttributes.put("ExpressCase", caseDetailsVO.getExpressCase() == 1);
			hmAttributes.put("ClientReference", caseDetailsVO.getClientReferenceNumber());
			hmAttributes.put("ClientName", clientName);
			hmAttributes.put("CFinal", this.convertDateForDS(caseDetailsVO.getClientFinalDueDate()));
			this.logger.debug("caseDetailsVO.getSpecialInstruction():::::::" + caseDetailsVO.getSpecialInstruction());
			CaseInfo caseInfo = new CaseInfo();
			HashMap<String, CaseInfo> caseInfoMap = new HashMap();
			caseInfo.setCaseInfoBlock(caseDetailsVO.getSpecialInstruction());
			caseInfoMap.put("CaseInfoBlock", caseInfo);
			hmAttributes.put("CaseInfoBlock", caseInfoMap);
			hmAttributes.put("SubReportType", caseDetailsVO.getSubReportType());
			hmAttributes.put("ReportType", reportTypeName);
			String assignmentType = caseDetailsVO.getAssignmentType();
			if (AutoOAFlag) {
				this.logger.debug("Going to set auto OA....");
				hmAttributes.put("isAutoOA", true);
			}

			this.logger.debug("hmAttributes::::" + hmAttributes);
			HashMap map = ResourceLocator.self().getSBMService().createProcessInstance(hmAttributes);
			this.logger.debug("map is...." + map);
			piid = Long.parseLong((String) map.get("ProcessInstanceId"));
		} catch (NullPointerException var13) {
			throw new CMSException(this.logger, var13);
		} catch (CMSException var14) {
			throw new CMSException(this.logger, var14);
		}

		this.logger.debug("piid is:::::" + piid);
		return piid;
	}

	private boolean validationForOA(CaseDetailsVO caseDetailsVO, Map validationMap) throws CMSException {
		this.logger.debug("Insdie validationForOA method of AtlasWebServiceManager class");
		boolean validationFlag = false;

		try {
			String assignmentType = caseDetailsVO.getAssignmentType();
			this.logger.debug("Assignment type is:::" + assignmentType);
			if (assignmentType.equalsIgnoreCase("Team Assignment")
					&& !((String) validationMap.get("isBIREExist")).equalsIgnoreCase("true")) {
				this.logger.debug("first validation passs...");
				String ptResearchHead = caseDetailsVO.getOfficeAssignment();
				boolean rhFlag = this.atlasWebServiceDAO.getRHOfficeValidationForOA(ptResearchHead);
				if (rhFlag) {
					this.logger.debug("second and final validation is true....");
					validationFlag = true;
				}
			} else {
				this.logger.debug("Case is not valid for auto OA...");
			}
		} catch (NullPointerException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("validationFlag value is::::::" + validationFlag);
		return validationFlag;
	}

	public void insertToClinetCase(CaseDetailsVO caseDetailsVO, long piid) throws CMSException {
		this.logger.debug("Insdie insertToClinetCase method of AtlasWebServiceManager class");

		try {
			String reportTypeName = (String) this.dataValidator.getReportTypeMapFromCache()
					.get(caseDetailsVO.getReportTypeId());
			this.logger.debug("reportTypeName:::" + reportTypeName);
			String clientName = (String) this.dataValidator.getClientMapFromCache().get(caseDetailsVO.getClientCode());
			this.logger.debug("clientName:::" + clientName);
			String branchOfficeName = (String) this.dataValidator.getOfficeMapFromCache()
					.get(new Integer(caseDetailsVO.getBranchType()));
			this.logger.debug("BranchType:::" + caseDetailsVO.getBranchType());
			this.logger.debug("branchOfficeName:::" + branchOfficeName);
			CaseDetails caseDetails = new CaseDetails();
			caseDetails.setCrn(caseDetailsVO.getCrn());
			caseDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			caseDetails.setCaseCreatorId(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			caseDetails.setReportTypeId("" + this.dataValidator.getReportTypeMapFromCache().get(reportTypeName));
			caseDetails.setIsSubjLevelSubRPTReq(caseDetailsVO.isIsSLSubReportType() ? "1" : "0");
			caseDetails.setSubReportTypeId("" + caseDetailsVO.getClSubreportID());
			caseDetails.setReqRecdDate(this.convertDateToTimeStamp(caseDetailsVO.getOrderReceiptDate()));
			caseDetails.setOfficeId(branchOfficeName);
			caseDetails.setFinalDueDate(this.convertDateToSqlDate(caseDetailsVO.getClientFinalDueDate()));
			caseDetails.setClientCode(clientName);
			caseDetails.setCaseMgrId(caseDetailsVO.getCaseManager());
			caseDetails.setExpressCase(caseDetailsVO.getExpressCase());
			caseDetails.setCaseInfo(caseDetailsVO.getSpecialInstruction());
			caseDetails.setClientRef(caseDetailsVO.getClientReferenceNumber());
			caseDetails.setIsISIS(1);
			caseDetails.setIsisUser(caseDetailsVO.getIsisUserWhoPlacedOrder());
			caseDetails.setOrderGUID(caseDetailsVO.getOrderGUID());
			caseDetails.setIsBulkOrder(caseDetailsVO.getBulkOrder());
			caseDetails.setBulkOrderId(caseDetailsVO.getBulkOrderId());
			this.logger.debug("research final due date is:::::" + caseDetailsVO.getResearchFinalDueDate());
			caseDetails.setFinalRDueDate(this.convertDateToSqlDate(caseDetailsVO.getResearchFinalDueDate()));
			caseDetails.setCaseStatusId(3);
			caseDetails.setPid("" + piid);
			ResourceLocator.self().getCreateCaseManager().createCaseForISIS(caseDetails);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public void insertTOAccountsAndNotification(CaseDetailsVO caseDetailsVO) throws CMSException {
		this.logger.debug("Insdie insertTOAccountsAndNotification method of AtlasWebServiceManager class");

		try {
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setCrn(caseDetailsVO.getCrn());
			accountsVO.setClientCode(caseDetailsVO.getClientCode());
			accountsVO.setUpdateBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			accountsVO.setBudget(caseDetailsVO.getBudget());
			accountsVO.setCaseFee(Float.toString(caseDetailsVO.getBudget()));
			accountsVO.setIsBudgetDueDateConfirmed(caseDetailsVO.isIsBudgetConfirmed());
			accountsVO.setCurrencyCode(caseDetailsVO.getCurrencyCode());
			accountsVO.setTaxCode(caseDetailsVO.getTaxCode());
			accountsVO.setIsisUser(caseDetailsVO.getIsisUserWhoPlacedOrder());
			accountsVO.setIsisUserEmailId(caseDetailsVO.getIsisUserEmailId());
			accountsVO.setCancelledCharges("No");
			this.atlasWebServiceDAO.insertAccountDetails(accountsVO);
		} catch (NullPointerException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	private void sendNotification(String crn, String caseManager) throws CMSException {
		String notificationMessage = "New case created in EDDO(" + crn + ")";
		List<String> listOfUsers = new ArrayList();
		listOfUsers.add(caseManager);
		List<UserMasterVO> financeUserList = ResourceLocator.self().getUserService().getUsersForRole("R3");
		Iterator iterator = financeUserList.iterator();

		while (iterator.hasNext()) {
			UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
			listOfUsers.add(userMasterVO.getUserID());
		}

		ResourceLocator.self().getNotificationService().createSystemNotification(notificationMessage,
				notificationMessage, listOfUsers, crn);
	}

	private void sendFileUploadNotificationForUpdateCase(String crn) {
		try {
			this.logger.debug("inside sendFileUploadNotificationForUpdateCase of AtlasWebServiceManager class");
			String notificationMessage = "File(s) uploaded by client from EDDO. Please check the Attachment section";
			ResourceLocator.self().getNotificationService().createSystemNotification(notificationMessage,
					notificationMessage, this.getNotificationUsersForUploadDocument(crn), crn);
		} catch (Exception var3) {
			;
		}

	}

	private List getNotificationUsersForUploadDocument(String crn) {
		this.logger.debug("inside getNotificationUsersForUploadDocument of AtlasWebServiceManager class");
		this.logger.debug("crn is:::::" + crn);
		ArrayList userList = new ArrayList();

		try {
			new ArrayList();
			new ArrayList();
			new ArrayList();
			String caseManager = "";
			caseManager = this.atlasWebServiceDAO.getCaseManager(crn);
			userList.add(caseManager);
			this.logger.debug("inside getNotificationUsersForSubject of AtlasWebServiceManager class");
			List analystList = this.atlasWebServiceDAO.getAnalystForUploadDocNotification(crn, "1,2,4");
			Iterator iterator = analystList.iterator();

			String managerName;
			while (iterator.hasNext()) {
				managerName = (String) iterator.next();
				if (!userList.contains(managerName) && managerName != null) {
					userList.add(managerName);
					this.logger.debug("analystName ::" + managerName);
				}
			}

			this.logger.debug("inside getNotificationUsersForSubject of sendFileUploadNotificationForUpdateCase class");
			List reviewersList = this.atlasWebServiceDAO.getReviewersForUploadDocNotification(crn);
			iterator = reviewersList.iterator();

			while (iterator.hasNext()) {
				managerName = (String) iterator.next();
				if (!userList.contains(managerName) && managerName != null) {
					userList.add(managerName);
					this.logger.debug("reviewersName ::" + managerName);
				}
			}

			this.logger.debug("inside getNotificationUsersForSubject of sendFileUploadNotificationForUpdateCase class");
			List managerList = this.atlasWebServiceDAO.getManagerForUploadDocNotification(crn, "3");
			iterator = managerList.iterator();

			while (iterator.hasNext()) {
				managerName = (String) iterator.next();
				if (!userList.contains(managerName) && managerName != null) {
					userList.add(managerName);
					this.logger.debug("managerName ::" + managerName);
				}
			}

			this.logger.debug("userList is:::::" + userList);
		} catch (NullPointerException var9) {
			;
		} catch (Exception var10) {
			;
		}

		return userList;
	}

	public ArrayOf_xsd_anyType createSubjectForISISCase(CaseDetailsVO caseDetailsVO) throws CMSException {
		this.logger.debug("Insdie createSubjectForISISCase method of AtlasWebServiceManager class");
		ArrayList resultSubjectVOList = new ArrayList();

		try {
			List<String> watchList = ResourceLocator.self().getSubjectService().getWatchListedSubject();
			String listedSubject = "";
			if (caseDetailsVO.getSubjectDetails() != null && caseDetailsVO.getSubjectDetails().getItem().length > 0) {
				List subjectList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
						caseDetailsVO.getSubjectDetails().getItem(), "SubjectDetailsVO");
				Iterator iterator = subjectList.iterator();

				while (true) {
					String reIds;
					while (iterator.hasNext()) {
						int subjectId = false;
						SubjectDetailsVO subjectDetailsVO = (SubjectDetailsVO) iterator.next();
						if (subjectDetailsVO.getSubjectName() != null
								&& subjectDetailsVO.getSubjectName().trim().length() > 0) {
							subjectDetailsVO.setSubjectName(this.formateSubjectname(subjectDetailsVO.getSubjectName()));
						}

						if (caseDetailsVO.isIsSLSubReportType()) {
							subjectDetailsVO.setSlSubreportID(
									Integer.parseInt(subjectDetailsVO.getSlSubreportCode().trim().substring(3)));
						}

						subjectDetailsVO.setCrn(caseDetailsVO.getCrn());
						subjectDetailsVO.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
						List reList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
								subjectDetailsVO.getReDetails().getItem(), "ReDetailsVO");
						reIds = "";
						Iterator iterator2 = reList.iterator();

						while (iterator2.hasNext()) {
							ReDetailsVO reDetailsVO = (ReDetailsVO) iterator2.next();
							reIds = reIds + reDetailsVO.getReId();
							if (iterator2.hasNext()) {
								reIds = reIds + ",";
							}
						}

						this.logger.debug("reIds is::::" + reIds);
						subjectDetailsVO.setReIds(reIds);
						boolean biFlag = true;
						String biManagerName = this.webServicePropertyReaderUtil.getWebServiceBIManagerId();
						SubjectDetails subDetails = new SubjectDetails();
						subDetails.setCrn(caseDetailsVO.getCrn());
						subDetails.setClientCode(caseDetailsVO.getClientCode());
						Map reportTypeMasterData = this.dataValidator.getReportTypeMapFromCache();
						subDetails.setReportTypeId("" + reportTypeMasterData.get(caseDetailsVO.getReportTypeId()));
						subDetails.setReportTypeId("" + reportTypeMasterData.get(subDetails.getReportTypeId()));
						this.logger.debug("setReportTypeId::::" + caseDetailsVO.getReportTypeId());
						this.logger.debug("caseDetailsVO.getCLSubreportID()::::" + caseDetailsVO.getClSubreportID());
						this.logger
								.debug("subjectDetailsVO.getSLSubreportID()::::" + subjectDetailsVO.getSlSubreportID());
						if (caseDetailsVO.isIsSLSubReportType()) {
							this.logger.debug(
									"caseDetailsVO.getIsSLSubReportType()::::" + caseDetailsVO.isIsSLSubReportType());
							subDetails.setSubReportTypeId("" + subjectDetailsVO.getSlSubreportID());
						} else {
							this.logger.debug(
									"caseDetailsVO.getIsSLSubReportType()::::" + caseDetailsVO.isIsSLSubReportType());
							subDetails.setSubReportTypeId("" + caseDetailsVO.getClSubreportID());
						}

						int subjectId = ResourceLocator.self().getSubjectService()
								.addISISSubjectToCase(subjectDetailsVO, biFlag, biManagerName, subDetails);
						ResultSubjectVO resultSubjectVO = new ResultSubjectVO();
						resultSubjectVO.setCrn(caseDetailsVO.getCrn());
						resultSubjectVO.setIsisSubjectId(subjectDetailsVO.getIsisSubjectID());
						resultSubjectVO.setAtlasSubjectId(subjectId);
						resultSubjectVOList.add(resultSubjectVO);
						this.logger.debug("Subject added for ISIS case with atlas subject id:::" + subjectId);
						Iterator var17 = watchList.iterator();

						while (var17.hasNext()) {
							String subject = (String) var17.next();
							if (subjectDetailsVO.getSubjectName().trim().equalsIgnoreCase(subject)) {
								listedSubject = listedSubject + subjectDetailsVO.getSubjectName() + "\n";
								break;
							}
						}
					}

					if (listedSubject.length() > 0) {
						String caseManagerId = ResourceLocator.self().getSubjectService()
								.caseManagerId(caseDetailsVO.getCrn());
						String mail_To = ResourceLocator.self().getSBMService().getEmailId(caseManagerId);
						String mail_To_Bcc = "TLR.AtlasSupport@thomsonreuters.com";
						String subject = "Watchlisted Entity(ies) in a case : " + caseDetailsVO.getCrn();
						reIds = "Please check this case immediately. It contains Watchlisted entity(ies) as listed below. Do NOT accept an assignment for research on this entity. Refer questions to the company general counsel.";
						reIds = reIds + "\n" + "\n" + listedSubject;
						ResourceLocator.self().getMailService().sendEmail(subject, mail_To, mail_To_Bcc, reIds);
					}
					break;
				}
			}
		} catch (NullPointerException var18) {
			throw new CMSException(this.logger, var18);
		} catch (CMSException var19) {
			throw new CMSException(this.logger, var19);
		}

		ArrayOf_xsd_anyType xsd = new ArrayOf_xsd_anyType();
		xsd.setItem(resultSubjectVOList.toArray());
		return xsd;
	}

	public void completeComplteCaseCreation(long piid) throws CMSException {
		this.logger.debug("Insdie completeComplteCaseCreation method of AtlasWebServiceManager class");

		try {
			ResourceLocator.self().getSBMService().completeTask((Session) null, (HashMap) null, piid,
					"Complete Case Creation");
			this.logger.debug("complete ComplteCaseCreation task from savvion.");
		} catch (NullPointerException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void isRiskAssociatedWithCase(String crn) throws CMSException {
		this.logger.debug("Inside isRiskAssociatedWithCase");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			List<String> riskAssociatedList = resourceLocator.getTaskService().getRiskAssociatedWithCase(crn);
			this.logger.debug("riskAssociatedList--" + riskAssociatedList);
			if (riskAssociatedList.size() > 0) {
				int updatedRecord = resourceLocator.getTaskService().updateRiskFlag(crn);
				this.logger.debug("Updated records are--" + updatedRecord);
			}

			this.logger.debug("After getting values exit from listForRiskAssociatedWithCase method");
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void downLoadFilesFromFTP(CaseDetailsVO caseDetailsVO, long piid) {
		try {
			this.downLoadBatchFiles.setProperties(caseDetailsVO, piid);
			this.logger.debug("downLoadFilesFromFTP..");
		} catch (Exception var5) {
			;
		}

	}

	public void createDocsForISIS(String UserName, String tempPath, String PIID, String FolderName)
			throws CMSException {
		String folder = "Common";
		String[] folderNames = new String[]{folder};
		String[] pathStringArray1 = new String[]{tempPath};
		ResourceLocator.self().getDocService().CreateDocForISIS(UserName, pathStringArray1, PIID, folderNames,
				(Session) null, (Timestamp) null);
	}

	public List convertCommaStringToList(String commString) throws CMSException {
		List<String> StringList = new ArrayList();
		if (commString != null && !commString.equals("")) {
			StringTokenizer commaSeperatedString = new StringTokenizer(commString, ",");

			while (commaSeperatedString.hasMoreElements()) {
				String string = commaSeperatedString.nextElement().toString();
				StringList.add(string);
			}
		}

		return StringList;
	}

	public void updateISISCase(CaseDetailsVO caseDetailsVO) throws CMSException, ParseException {
		try {
			this.logger.debug("Inside updateISISCase of AtlasWebServiceManager");
			this.updateClientCaseForCaseUpdate(caseDetailsVO);
			this.logger.debug("After update CMS_ClientCASE");
			this.logger.debug("After update CMS_ClientCASE");
			this.updateSBMDSForCaseUpdate(caseDetailsVO);
			this.logger.debug("After update SBM DS");
		} catch (NullPointerException var3) {
			this.logger.debug("Inside Error..::");
			throw var3;
		} catch (CMSException var4) {
			this.logger.debug("Inside Error..::");
			throw var4;
		}
	}

	public ArrayOf_xsd_anyType updateSubjects(CaseDetailsVO caseDetailsVO, String crn)
			throws IllegalArgumentException, CMSException {
		this.logger.debug("inside updateSubjects method of AtlasWebServiceManager class");
		ArrayList resultSubjectVOList = new ArrayList();

		try {
			List<String> watchList = ResourceLocator.self().getSubjectService().getWatchListedSubject();
			String listedSubject = "";
			String reportName = (String) this.dataValidator.getReportTypeMapFromCache()
					.get(caseDetailsVO.getReportTypeId());
			ArrayList subjectList = AtlasWebServiceUtil
					.convertFromArrayObjectToArrayList(caseDetailsVO.getSubjectDetails().getItem(), "SubjectDetailsVO");
			Iterator iterator = subjectList.iterator();

			while (true) {
				String reIds;
				String subjectFlag;
				while (iterator.hasNext()) {
					SubjectDetailsVO subjectDetailsVO = (SubjectDetailsVO) iterator.next();
					subjectDetailsVO.setCrn(crn);
					List reList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
							subjectDetailsVO.getReDetails().getItem(), "ReDetailsVO");
					reIds = "";
					Iterator iterator2 = reList.iterator();

					while (iterator2.hasNext()) {
						ReDetailsVO reDetailsVO = (ReDetailsVO) iterator2.next();
						reIds = reIds + reDetailsVO.getReId();
						if (iterator2.hasNext()) {
							reIds = reIds + ",";
						}
					}

					this.logger.debug("reIds is:::::::::::::::>>>>>>>>>>>>>>>>>>>>>>" + reIds);
					subjectDetailsVO.setReIds(reIds);
					subjectFlag = subjectDetailsVO.getSubjectFlag();
					if (caseDetailsVO.isIsSLSubReportType()) {
						subjectDetailsVO.setSlSubreportID(
								Integer.parseInt(subjectDetailsVO.getSlSubreportCode().trim().substring(3)));
					}

					List<String> flagList = this.convertCommaStringToList(subjectFlag);
					boolean primaryTeamDeleteFlag;
					if (flagList.contains("A") || flagList.contains("a")) {
						this.logger.debug("processing Subject add request from isis");
						primaryTeamDeleteFlag = this.checkForBI(crn, subjectDetailsVO.getReIds());
						String biManagerName = this.webServicePropertyReaderUtil.getWebServiceBIManagerId();
						SubjectDetails subDetails = new SubjectDetails();
						if (subjectDetailsVO.getSubjectName() != null
								&& subjectDetailsVO.getSubjectName().trim().length() > 0) {
							subjectDetailsVO.setSubjectName(this.formateSubjectname(subjectDetailsVO.getSubjectName()));
						}

						subDetails.setCrn(caseDetailsVO.getCrn());
						subDetails.setClientCode(caseDetailsVO.getClientCode());
						subDetails.setReportTypeId("" + this.dataValidator.getReportTypeMapFromCache().get(reportName));
						subDetails.setSlBudget(0.0F);
						if (caseDetailsVO.isIsSLSubReportType()) {
							subDetails.setSubReportTypeId("" + subjectDetailsVO.getSlSubreportID());
						} else {
							subDetails.setSubReportTypeId("" + caseDetailsVO.getClSubreportID());
						}

						int atlasSubjectid = ResourceLocator.self().getSubjectService().addISISSubjectToCase(
								subjectDetailsVO, primaryTeamDeleteFlag, biManagerName, subDetails);
						ResultSubjectVO resultSubjectVO = new ResultSubjectVO();
						resultSubjectVO.setCrn(caseDetailsVO.getCrn());
						resultSubjectVO.setIsisSubjectId(subjectDetailsVO.getIsisSubjectID());
						resultSubjectVO.setAtlasSubjectId(atlasSubjectid);
						resultSubjectVOList.add(resultSubjectVO);
					}

					if (flagList.contains("U") || flagList.contains("u")) {
						primaryTeamDeleteFlag = false;
						int atlasSubjectId = this.atlasWebServiceDAO
								.getAtlasSubIdFromISISSubId(subjectDetailsVO.getIsisSubjectID(), crn);
						String removedRes = this.getremovedREList(atlasSubjectId, subjectDetailsVO.getReIds());
						if (removedRes != null && !"".equals(removedRes)) {
							primaryTeamDeleteFlag = this.atlasWebServiceDAO.getUpdateSubjectPrimaryTeamValidation(crn,
									atlasSubjectId, removedRes);
						}

						if (primaryTeamDeleteFlag) {
							throw new IllegalArgumentException(
									"Update subject request causes deletion of Primary Team ");
						}

						this.logger.debug("processing Subject update request from isis::::::::::");
						subjectDetailsVO.setSubjectId(atlasSubjectId);
						String biStatus = this.getUpdateSubjectBITeamInfo(crn, atlasSubjectId,
								subjectDetailsVO.getReIds());
						List teamList = this.getupdateSubjectTeamDeleteInfo(crn, atlasSubjectId,
								subjectDetailsVO.getReIds());
						SubjectDetails subDetails = new SubjectDetails();
						if (subjectDetailsVO.getSubjectName() != null
								&& subjectDetailsVO.getSubjectName().trim().length() > 0) {
							subjectDetailsVO.setSubjectName(this.formateSubjectname(subjectDetailsVO.getSubjectName()));
						}

						subDetails.setCrn(caseDetailsVO.getCrn());
						subDetails.setClientCode(caseDetailsVO.getClientCode());
						subDetails.setReportTypeId("" + this.dataValidator.getReportTypeMapFromCache().get(reportName));
						subDetails.setSlBudget(0.0F);
						if (caseDetailsVO.isIsSLSubReportType()) {
							subDetails.setSubReportTypeId("" + subjectDetailsVO.getSlSubreportID());
						} else {
							subDetails.setSubReportTypeId("" + caseDetailsVO.getClSubreportID());
						}

						subDetails.setCrn(caseDetailsVO.getCrn());
						subDetails.setClientCode(caseDetailsVO.getClientCode());
						subDetails.setReportTypeId("" + this.dataValidator.getReportTypeMapFromCache().get(reportName));
						if (caseDetailsVO.isIsSLSubReportType()) {
							subDetails.setSubReportTypeId("" + subjectDetailsVO.getSlSubreportID());
						} else {
							subDetails.setSubReportTypeId("" + caseDetailsVO.getClSubreportID());
						}

						ResourceLocator.self().getSubjectService().updateISISSubjectToCase(subjectDetailsVO, biStatus,
								teamList, subDetails);
					}

					if (flagList.contains("D") || flagList.contains("d")) {
						int atlasSubjectId = this.atlasWebServiceDAO
								.getAtlasSubIdFromISISSubId(subjectDetailsVO.getIsisSubjectID(), crn);
						boolean deleteSubjectTeamValidation = this.atlasWebServiceDAO
								.getDeleteSubjectPrimaryTeamValidation(crn, atlasSubjectId);
						if (deleteSubjectTeamValidation) {
							throw new IllegalArgumentException(
									"Delete subject request causes deletion of Primary Team ");
						}

						this.logger.debug("processing Subject delete request from isis");
						subjectDetailsVO.setSubjectId(atlasSubjectId);
						boolean biTeamDeleteFlag = this.doSubjectBIValidation(crn, atlasSubjectId);
						List teamList = this.doSubjectValidation(crn, atlasSubjectId);
						ResourceLocator.self().getSubjectService().deleteISISSubjectToCase(
								subjectDetailsVO.getIsisSubjectID(), crn, biTeamDeleteFlag, teamList);
					}

					Iterator var33 = watchList.iterator();

					while (var33.hasNext()) {
						String subject = (String) var33.next();
						if (subjectDetailsVO.getSubjectName().trim().equalsIgnoreCase(subject)) {
							listedSubject = listedSubject + subjectDetailsVO.getSubjectName() + "\n";
							break;
						}
					}
				}

				if (listedSubject.length() > 0) {
					String caseManagerId = ResourceLocator.self().getSubjectService()
							.caseManagerId(caseDetailsVO.getCrn());
					String mail_To = ResourceLocator.self().getSBMService().getEmailId(caseManagerId);
					String mail_To_Bcc = "TLR.AtlasSupport@thomsonreuters.com";
					reIds = "Watchlisted Entity(ies) in a case : " + caseDetailsVO.getCrn();
					subjectFlag = "Please check this case immediately. It contains Watchlisted entity(ies) as listed below. Do NOT accept an assignment for research on this entity. Refer questions to the company general counsel.";
					subjectFlag = subjectFlag + "\n" + "\n" + listedSubject;
					ResourceLocator.self().getMailService().sendEmail(reIds, mail_To, mail_To_Bcc, subjectFlag);
				}
				break;
			}
		} catch (IllegalArgumentException var20) {
			throw var20;
		} catch (NullPointerException var21) {
			throw new CMSException(this.logger, var21);
		} catch (CMSException var22) {
			throw new CMSException(this.logger, var22);
		}

		this.logger.debug("After processing  subject request for case updation from ISIS");
		ArrayOf_xsd_anyType xsd = new ArrayOf_xsd_anyType();
		if (resultSubjectVOList != null) {
			xsd.setItem(resultSubjectVOList.toArray());
		}

		return xsd;
	}

	public void updateFiles(CaseDetailsVO caseDetailsVO) {
		try {
			if (caseDetailsVO.getFileDetails() != null && caseDetailsVO.getFileDetails().getItem() != null
					&& caseDetailsVO.getFileDetails().getItem().length > 0) {
				long piid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(caseDetailsVO.getCrn());
				this.logger.debug("going to start thread for download files from FTP for update operation..");
				if (this.webServicePropertyReaderUtil.isFtpMonitoring()) {
					this.logger.debug("In here ");
					List filesList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
							caseDetailsVO.getFileDetails().getItem(), "CaseFileDetailsVO");
					String crn1 = caseDetailsVO.getCrn();
					this.atlasWebServiceDAO.insertToCMSFtpEntries(filesList, crn1);
				}

				this.downLoadFilesFromFTP(caseDetailsVO, piid);
			} else {
				this.logger.debug("There is no files to download from FTP in create case request");
			}
		} catch (Exception var6) {
			;
		}

	}

	public String convertDateForDS(Calendar cal) {
		this.logger.debug("Time zone from ISIS request is:::::" + cal.getTimeZone());
		Date d = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String d1 = sdf.format(d);
		return d1;
	}

	private String convertReceivedDateDateForDS(Calendar cal) {
		this.logger.debug("Time zone from ISIS request is:::::" + cal.getTimeZone());
		Date d = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String d1 = sdf.format(d);
		return d1;
	}

	public Timestamp convertDateToTimeStamp(Calendar cal) throws ParseException {
		Timestamp timeStamp = new Timestamp(cal.getTime().getTime());
		return timeStamp;
	}

	public java.sql.Date convertDateToSqlDate(Calendar cal) {
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
		return sqlDate;
	}

	public void updateSBMDSForCaseUpdate(CaseDetailsVO caseDetailsVO) throws CMSException {
		try {
			this.logger.debug("Inside updateSBMDSForCaseUpdate method" + caseDetailsVO.getCrn());
			String reportTypeName = "";
			String branchOfficeName = "";
			String clientName = "";
			long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(caseDetailsVO.getCrn());
			Session session = ResourceLocator.self().getSBMService()
					.getSession(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			HashMap<String, Object> dsValues = new HashMap();
			dsValues.put("ExpressCase", caseDetailsVO.getExpressCase() == 1);
			dsValues.put("ClientReference", caseDetailsVO.getClientReferenceNumber());
			String CaseInformation = ((CaseInfo) ((HashMap) ResourceLocator.self().getSBMService().getDataslotValue(pid,
					"CaseInfoBlock", session)).get("CaseInfoBlock")).getCaseInfoBlock();
			CaseInformation = CaseInformation != null ? CaseInformation : "";
			String isisCaseInformation = caseDetailsVO.getSpecialInstruction() != null
					? caseDetailsVO.getSpecialInstruction()
					: "";
			String completeCaseInformation = CaseInformation + "\n" + isisCaseInformation;
			this.logger.debug("DS caseInfo from Atlas::::" + CaseInformation);
			this.logger.debug("DS isisCaseInformation from request:::" + isisCaseInformation);
			this.logger.debug("DS Complete Case information after append is::::" + completeCaseInformation);
			CaseInfo caseInfo = new CaseInfo();
			HashMap<String, CaseInfo> map = new HashMap();
			caseInfo.setCaseInfoBlock(completeCaseInformation);
			map.put("CaseInfoBlock", caseInfo);
			dsValues.put("CaseInfoBlock", map);
			ResourceLocator.self().getSBMService().updateDataSlots(session, pid, dsValues);
			String[] completeWSArrayString = ResourceLocator.self().getSBMService().getCompletedWSNames(pid, session);
			ArrayList<Long> pidList = null;
			if (completeWSArrayString.length > 0) {
				List<String> wsNames = Arrays.asList(completeWSArrayString);
				if (wsNames.contains("Office Assignment Task")) {
					pidList = (ArrayList) ResourceLocator.self().getSBMService().getAllPIDSOfTheCase(pid);
				}
			}

			if (pidList != null && pidList.size() > 0) {
				Iterator iterator = pidList.iterator();

				while (iterator.hasNext()) {
					Long otherPid = (Long) iterator.next();
					boolean pidFlag = ResourceLocator.self().getSBMService().isTaskCompleted(otherPid, session);
					this.logger.debug(
							"pid is:::::" + Long.parseLong(otherPid.toString()) + "::::::::pidFlag:::::" + pidFlag);
					if (!pidFlag) {
						ResourceLocator.self().getSBMService().updateDataSlots(session, otherPid, dsValues);
					}
				}
			}

		} catch (NullPointerException var19) {
			throw new CMSException(this.logger, var19);
		} catch (CMSException var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public void updateClientCaseForCaseUpdate(CaseDetailsVO caseDetailsVO) throws ParseException, CMSException {
		try {
			this.logger.debug("Inside updateClientCaseForCaseUpdate method" + caseDetailsVO.getCrn());
			CaseDetails caseDetails = new CaseDetails();
			caseDetails.setCrn(caseDetailsVO.getCrn());
			caseDetails.setCaseMgrId(caseDetailsVO.getCaseManager());
			caseDetails.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			caseDetails.setExpressCase(caseDetailsVO.getExpressCase());
			String caseInfo = this.atlasWebServiceDAO.getCaseInformation(caseDetailsVO.getCrn());
			caseInfo = caseInfo != null ? caseInfo : "";
			String isisCaseInformation = caseDetailsVO.getSpecialInstruction() != null
					? caseDetailsVO.getSpecialInstruction()
					: "";
			String completeCaseInfo = caseInfo + "\n" + isisCaseInformation;
			this.logger.debug("caseInfo from Atlas::::" + caseInfo);
			this.logger.debug("isisCaseInformation from request:::" + isisCaseInformation);
			this.logger.debug("Complete Case information after append is::::" + completeCaseInfo);
			caseDetails.setCaseInfo(completeCaseInfo);
			caseDetails.setClientRef(caseDetailsVO.getClientReferenceNumber());
			ResourceLocator.self().getTaskService().updateRecordForClient(caseDetails);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private void removePIID(long piid) {
		try {
			Session session = ResourceLocator.self().getSBMService()
					.getSession(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			ArrayList<Long> pidList = (ArrayList) ResourceLocator.self().getSBMService().getAllPIDSOfTheCase(piid);
			Iterator iterator = pidList.iterator();

			while (iterator.hasNext()) {
				Long otherPid = (Long) iterator.next();
				boolean pidFlag = ResourceLocator.self().getSBMService().isTaskCompleted(otherPid, session);
				this.logger
						.debug("pid is:::::" + Long.parseLong(otherPid.toString()) + "::::::::pidFlag:::::" + pidFlag);
				if (!pidFlag) {
					ResourceLocator.self().getSBMService().removeProcessInstance(otherPid);
				}
			}

			this.logger.debug("Going to remove parent PID--" + piid);
			ResourceLocator.self().getSBMService().removeProcessInstance(piid);
			this.logger.debug("Parent PID removed--" + piid);
		} catch (Exception var8) {
			this.logger.debug("Inside catch block..." + piid);
			this.logger.debug("Exception is ::" + var8);
		}

	}

	private int getOfficeDetailsForCase(String loginId) throws CMSException {
		BranchOfficeMasterVO branchOfficeMaster = this.atlasWebServiceDAO.getofficeIdForCaseManager(loginId);
		return branchOfficeMaster.getBranchOfficeId();
	}

	private boolean validateHeaderCredentials() {
		this.logger.debug("Inside validateHeaderCredentials method of AtlasWebServiceManager class");
		boolean validationFlag = false;

		try {
			this.logger.debug("auth user is:::::::::::::::" + this.webServicePropertyReaderUtil.getAtlasAuthUser());
			this.logger.debug("pass is:::::::::::::::" + this.webServicePropertyReaderUtil.getAtlasAuthPassword());
			Map headerMap = new HashMap();
			MessageContext context = MessageContext.getCurrentContext();
			SOAPHeader req = context.getMessage().getSOAPHeader();
			Iterator headers = req.examineAllHeaderElements();

			while (headers.hasNext()) {
				SOAPHeaderElement header = (SOAPHeaderElement) headers.next();
				Iterator i = header.getChildElements();

				while (i.hasNext()) {
					MessageElement type = (MessageElement) i.next();
					headerMap.put(type.getName(), type.getValue());
				}
			}

			if (headerMap.get("userName").equals(this.webServicePropertyReaderUtil.getAtlasAuthUser())
					&& headerMap.get("password").equals(this.webServicePropertyReaderUtil.getAtlasAuthUser())) {
				validationFlag = true;
			}
		} catch (SOAPException var9) {
			this.logger.debug("SOAPException occured.... in validateHeaderCredentials method");
			this.logger.debug("SOAPException is:::::" + var9.getMessage());
			this.logger.error(var9);
			validationFlag = false;
		} catch (Exception var10) {
			this.logger.debug("Exception occured.... in validateHeaderCredentials method");
			this.logger.debug("Exception is:::::" + var10.getMessage());
			this.logger.error(var10);
			validationFlag = false;
		}

		this.logger.debug("validation Flag Value for soap header is::::::" + validationFlag);
		return validationFlag;
	}

	private boolean isCRNInQueue(String crn) {
		this.logger.debug("Inside isCRNInQueue method of AtlasWebServiceManager class");
		boolean flag = false;

		try {
			int count = this.atlasWebServiceDAO.getCRNInQueue(crn);
			if (count > 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (CMSException var4) {
			this.logger.debug("Exception occured.... in validateHeaderCredentials method");
			this.logger.debug("Exception is:::::" + this.getStackTraceAsString(var4));
			this.logger.error(var4);
			flag = false;
		}

		return flag;
	}

	private void deleteDataForRollBack(String crn) {
		try {
			this.atlasWebServiceDAO.deleteDataForRollBack(crn);
		} catch (Exception var3) {
			this.logger.debug("Some error occured while rollback data.." + this.getStackTraceAsString(var3));
		}

	}

	private boolean checkForBI(String crn, String reIDString) throws CMSException {
		this.logger.debug("inside check for bi........");
		Map reBiTeamMap = this.atlasWebServiceDAO.getReBiTeamMap();
		this.logger.debug("reBiTeamMap is:::::::" + reBiTeamMap);
		boolean biTeamFlag = false;
		boolean isBIExist = false;
		boolean result = true;
		StringTokenizer reTokenizer = new StringTokenizer(reIDString, ",");
		String var8 = "";

		while (reTokenizer.hasMoreElements()) {
			String token = reTokenizer.nextElement().toString();
			String biTeam = (String) reBiTeamMap.get(token);
			if (biTeam.equalsIgnoreCase("Yes")) {
				biTeamFlag = true;
			}
		}

		this.logger.debug("biTeamFlag is::::::::::::>>>>>>>>>>" + biTeamFlag);
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

	private boolean isOfficeAssignmentDone(String crn) throws CMSException {
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

	private boolean isBITeamExist(String crn) throws CMSException {
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

	private Map getBiREStatus(String oldRetring, String newReString) throws CMSException {
		this.logger.debug("inside getBiREStatus of SubjectManager class");
		this.logger.debug("oldReIds is:::::" + oldRetring);
		this.logger.debug("newReIds is:::::" + newReString);
		boolean reupdateFlag = false;
		boolean biREAddedFlag = false;
		boolean biRERemovedFlag = false;
		Map statusMap = new HashMap();
		Map reBiTeamMap = this.atlasWebServiceDAO.getReBiTeamMap();
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

	private List getupdateSubjectTeamDeleteInfo(String crn, int subjectID, String newReString) throws CMSException {
		String oldREString = this.atlasWebServiceDAO.getReIdsForSubject(subjectID);
		this.logger.debug(oldREString + "::::::::::::::::::::::" + newReString);
		Map statusMap = this.getBiREStatus(oldREString, newReString);
		this.logger.debug("statusMap is:::" + statusMap);
		List reRemovedList = (List) statusMap.get("reRemovedList");
		String reRemovedListString = "0";
		if (reRemovedList.size() > 0) {
			reRemovedListString = this.listToCommaSeparatedString(reRemovedList);
		}

		new ArrayList();
		List teamList = this.atlasWebServiceDAO.getUpdateSubjectREValidationForTeam(crn, subjectID,
				reRemovedListString);
		return teamList;
	}

	private String getUpdateSubjectBITeamInfo(String crn, int subjectID, String newReString) throws CMSException {
		this.logger.debug("Going to call getUpdateSubjectBITeamInfo.......:::::::::::::");
		String oldREString = this.atlasWebServiceDAO.getReIdsForSubject(subjectID);
		this.logger.debug("oldREString::::::" + oldREString);
		this.logger.debug("completREIDString" + newReString);
		Map statusMap = this.getBiREStatus(oldREString, newReString);
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
			List teamList = this.atlasWebServiceDAO.getUpdateSubjectBIREValidationForTeam(crn, subjectID,
					reRemovedListString);
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

	private List doSubjectValidation(String crn, int subjectId) throws CMSException {
		this.logger.debug("Inside doSubjectValidation method of AtlasWebServiceManager class");
		this.logger.debug("crn is:::" + crn + ":::subjectId is:::" + subjectId);
		new ArrayList();
		List teamList = this.atlasWebServiceDAO.getSubjectValidationForTeam(crn, subjectId);
		return teamList;
	}

	private boolean doSubjectBIValidation(String crn, int subjectId) throws CMSException {
		boolean biTeamDeleteFlag = false;
		this.logger.debug("going ot call doSubjectBIValidation");
		new ArrayList();
		List teamList = this.atlasWebServiceDAO.getSubjectValidationForBITeam(crn, subjectId);
		if (teamList != null && teamList.size() > 0) {
			biTeamDeleteFlag = true;
		}

		return biTeamDeleteFlag;
	}

	private String listToCommaSeparatedString(List<String> stringList) throws CMSException {
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

	private String getremovedREList(int subjectId, String newReString) throws CMSException {
		String removedRes = "";
		this.logger.debug("Inside getremovedREList method of AtlasWebServiceManager class");

		try {
			String oldRetring = this.atlasWebServiceDAO.getReIdsForSubject(subjectId);
			this.logger.debug("oldRetring is::::::" + oldRetring);
			List oldREList = this.convertCommaStringToList(oldRetring);
			List newREList = this.convertCommaStringToList(newReString);
			new ArrayList();
			new ArrayList();
			Iterator iterator = newREList.iterator();

			String reId;
			while (iterator.hasNext()) {
				reId = (String) iterator.next();
				oldREList.remove(reId);
			}

			if (oldREList != null && oldREList.size() > 0) {
				this.logger.debug("reRemovedList size is::::::" + oldREList.size());
				iterator = oldREList.iterator();

				while (iterator.hasNext()) {
					reId = (String) iterator.next();
					removedRes = removedRes + reId;
					if (iterator.hasNext()) {
						removedRes = removedRes + ",";
					}
				}

				this.logger.debug("removedRes are:::::::::::::" + removedRes);
			}
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("removedRes are:::::::::::::" + removedRes);
		return removedRes;
	}

	private SubjectDetails getPrimarySubjectInfo(String crn) throws CMSException {
		SubjectDetails subjectDetail = ResourceLocator.self().getSubjectService().getPrimarySubjectDetailsForCase(crn);
		return subjectDetail;
	}

	private String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}

	public boolean checkDatabaseConnection() throws CMSException {
		long value = this.atlasWebServiceDAO.checkDatabaseConnection();
		this.logger.debug("is database connected:::" + value);
		return value == 1L;
	}

	private ArrayOf_xsd_anyType getSubjectListforAEDDOCase(String crn) {
		ArrayOf_xsd_anyType xsd = new ArrayOf_xsd_anyType();
		List resultSubjectVOList = this.atlasWebServiceDAO.getSubjectListforAEDDOCase(crn);
		xsd.setItem(resultSubjectVOList.toArray());
		return xsd;
	}

	public String formateSubjectname(String subjectName) throws CMSException {
		for (subjectName = subjectName.trim(); subjectName
				.contains("  "); subjectName = subjectName.replaceAll("  ", " ")) {
			;
		}

		return subjectName;
	}
}