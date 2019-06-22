package com.worldcheck.atlas.bl.unconfirmbudget;

import com.integrascreen.orders.SubjectDetailsVO;
import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.bl.interfaces.IUnconfirmBudget;
import com.worldcheck.atlas.dao.subject.SubjectDAO;
import com.worldcheck.atlas.dao.unconfirmbudget.UnconfirmBudgetDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ClientCBDVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONValue;

public class UnconfirmBudgetManager implements IUnconfirmBudget {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.unconfirmbudget.UnconfirmBudgetManager");
	private UnconfirmBudgetDAO unconfirmBudgetDAO = null;
	private SubjectDAO subjectDAO;
	private SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sourceFormat = new SimpleDateFormat("dd MMM, yyyy");
	private SimpleDateFormat sourceFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
	private HashMap<Integer, CountryMasterVO> countryMap = new HashMap();

	public void setUnconfirmBudgetDAO(UnconfirmBudgetDAO unconfirmBudgetDAO) {
		this.unconfirmBudgetDAO = unconfirmBudgetDAO;
	}

	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	public UnconfimredBudgetVO getBudgetDetails(String crn) throws CMSException {
		return this.unconfirmBudgetDAO.getBudgetDetails(crn);
	}

	public int saveISISDetails(UnconfimredBudgetVO vo, Session session) throws CMSException {
		try {
			this.logger.debug("in saveISISDetails for crn:: " + vo.getCRN());
			if (vo.getCRN() == null) {
				return -2;
			} else {
				boolean pingSuccessful = false;
				boolean processFlag = false;
				int count = 0;
				String isSubjLevelSubRptReq = vo.getIsSubreportRequire();
				this.logger.debug("Subreport Type require" + isSubjLevelSubRptReq);
				if (isSubjLevelSubRptReq.equalsIgnoreCase("true")) {
					this.saveSubjectLevelBudget(vo, session);
				}

				pingSuccessful = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
				this.logger.debug("pingSuccessful :: " + pingSuccessful);
				if (!pingSuccessful) {
					return -1;
				} else {
					ClientCBDVO cbdVo = new ClientCBDVO();
					cbdVo.setCRN(vo.getCRN());
					cbdVo.setBudget(Double.parseDouble(vo.getCaseFee()));
					Calendar cal = Calendar.getInstance();
					Date temp = this.sourceFormat1.parse(vo.getFinalDate());
					this.logger.debug("temp :: " + temp);
					String tempDate = this.targetFormat.format(temp);
					temp = this.targetFormat.parse(tempDate);
					this.logger.debug("tempDate :: " + tempDate + " :: temp :: " + temp);
					cal.setTime(temp);
					this.logger.debug("cal time :: " + cal.getTimeInMillis() + " :: date :: " + cal.getTime()
							+ " :: temp :: " + temp);
					cal.set(16, 0);
					cal.set(15, 0);
					this.logger.debug("cal time :: " + cal.getTimeInMillis() + " :: date :: " + cal.getTime()
							+ " :: temp :: " + temp);
					cbdVo.setDueDate(cal);
					cbdVo.setCurrencyCode(vo.getCurrency());
					cbdVo.setStatus(vo.getCaseStatus());
					if (isSubjLevelSubRptReq.equalsIgnoreCase("true")) {
						String newValue = "";
						SubjectDetailsVO[] subjectLevelBudgetVoArray = (SubjectDetailsVO[]) null;
						String[] modifiedRecords1 = (String[]) null;
						int gridDataLen = false;
						int gridDataLen;
						if (vo.getTaskName() != null && vo.getTaskName().equalsIgnoreCase("clientSubmission")) {
							try {
								JSONArray jsonArray = new JSONArray(vo.getGridData());
								gridDataLen = jsonArray.length();
								modifiedRecords1 = new String[gridDataLen];
								new ArrayList();

								for (int i = 0; i < modifiedRecords1.length; ++i) {
									modifiedRecords1[i] = jsonArray.optString(i);
								}
							} catch (JSONException var20) {
								throw new CMSException(this.logger, var20);
							}
						} else {
							modifiedRecords1 = vo.getModifiedRecords();
							gridDataLen = modifiedRecords1.length;
						}

						float newCaseFee = 0.0F;
						subjectLevelBudgetVoArray = new SubjectDetailsVO[modifiedRecords1.length];
						this.getCountryCode();

						for (int i = 0; i < modifiedRecords1.length; ++i) {
							String JSONstring = modifiedRecords1[i];
							Map jsonObject = (Map) JSONValue.parse(JSONstring);
							newCaseFee += Float.parseFloat(jsonObject.get("slBudget").toString());
							SubjectDetailsVO subjectLevelBudgetVO = new SubjectDetailsVO();
							this.logger.debug("isisi subhect ID" + jsonObject.get("isisSubjectId").toString());
							if (jsonObject.get("isisSubjectId").toString() != null) {
								subjectLevelBudgetVO.setISISSubjectID(jsonObject.get("isisSubjectId").toString());
							}

							if (jsonObject.get("subjectName").toString() != null) {
								subjectLevelBudgetVO.setSubjectName(jsonObject.get("subjectName").toString());
							}

							if (jsonObject.get("countryId").toString() != null) {
								subjectLevelBudgetVO.setCountry(((CountryMasterVO) this.countryMap
										.get(Integer.parseInt(jsonObject.get("countryId").toString())))
												.getCountryCode());
							}

							if (jsonObject.get("entityTypeId").toString() != null) {
								subjectLevelBudgetVO
										.setEntityType(Integer.parseInt(jsonObject.get("entityTypeId").toString()));
							}

							if (jsonObject.get("subReportTypeId").toString() != null
									&& jsonObject.get("subReportTypeId").toString().trim().length() > 0) {
								subjectLevelBudgetVO.setSubReportCode(jsonObject.get("subReportTypeId").toString());
							}

							if (jsonObject.get("slBudget").toString() != null) {
								subjectLevelBudgetVO
										.setSLBudget((double) Float.parseFloat(jsonObject.get("slBudget").toString()));
							}

							if (jsonObject.get("currencycode").toString() != null) {
								subjectLevelBudgetVO.setSLCurrency(jsonObject.get("currencycode").toString());
							}

							subjectLevelBudgetVoArray[i] = subjectLevelBudgetVO;
							newValue = newValue + "\r\n" + "Subject name  " + jsonObject.get("subjectName").toString()
									+ " Subject Budget " + jsonObject.get("slBudget").toString();
						}

						cbdVo.setSubjectList(subjectLevelBudgetVoArray);
						vo.setNewInfo(newValue);
						vo.setCaseFee(String.valueOf(newCaseFee));
						cbdVo.setBudget(Double.parseDouble(vo.getCaseFee()));
					}

					processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateCBD(cbdVo);
					if (processFlag) {
						CaseDetails oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
								.getDetailForCase(vo.getCRN());
						count = this.unconfirmBudgetDAO.saveISISDetails(vo);
						HashMap<String, Object> dsValues = new HashMap();
						dsValues.put("CRN", vo.getCRN());
						dsValues.put("CaseManager", vo.getCaseMgrId());
						this.logger.debug("finalRDate >>> " + vo.getFinalRDate());
						this.logger.debug("finalCDate >>> " + vo.getFinalDate());
						dsValues.put("RFinal", vo.getFinalRDate());
						dsValues.put("CFinal", vo.getFinalDate());
						CaseDetails newCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
								.getDetailForCase(vo.getCRN());
						newCaseDetails.setCrn(vo.getCRN());
						newCaseDetails.setFinalRDueDate(
								new java.sql.Date(this.sourceFormat1.parse(vo.getFinalRDate()).getTime()));
						newCaseDetails.setFinalDueDate(
								new java.sql.Date(this.sourceFormat1.parse(vo.getFinalDate()).getTime()));
						newCaseDetails.setCaseMgrId(vo.getCaseMgrId());
						newCaseDetails.setUpdatedBy(vo.getUpdatedBy());
						ResourceLocator.self().getFlowService().updateCaseInformationDS(session, oldCaseDetails,
								newCaseDetails, dsValues);
						CaseHistory caseHistory = new CaseHistory();
						caseHistory.setPid(oldCaseDetails.getPid());
						caseHistory.setCRN(oldCaseDetails.getCrn());
						caseHistory.setPerformer(vo.getCaseHistoryPerformer());
						caseHistory.setTaskName("");
						caseHistory.setTaskStatus("");
						caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
								.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
						newCaseDetails.setCaseHistoryPerformer(vo.getCaseHistoryPerformer());
						ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
								caseHistory, "caseDetails");
						long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(vo.getCRN());
						vo.setPid(pid);
						List<UnconfimredBudgetVO> unconfirmList = new ArrayList();
						unconfirmList.add(vo);
						ResourceLocator.self().getCaseHistoryService()
								.setCaseHistoryForConfirmBudgetAndDueDate(unconfirmList);
					}

					return count;
				}
			}
		} catch (NumberFormatException var21) {
			throw new CMSException(this.logger, var21);
		} catch (Exception var22) {
			throw new CMSException(this.logger, var22);
		}
	}

	public List<UnconfimredBudgetVO> getBudgetRecords(UnconfimredBudgetVO vo) throws CMSException {
		return this.unconfirmBudgetDAO.getBudgetRecords(vo);
	}

	public int getBudgetRecordsCount(String loginId) throws CMSException {
		return this.unconfirmBudgetDAO.getBudgetRecordsCount(loginId);
	}

	public String saveUnconfirmDetails(UnconfimredBudgetVO unconfimredBudgetVO, String userName,
			String caseHistoryPerformer, Session session) throws CMSException {
		try {
			String failedCrnList = "";
			this.logger.debug("From Assignment Tab");
			String[] modifiedRecords = unconfimredBudgetVO.getModifiedRecords();
			if (modifiedRecords != null) {
				this.logger.debug("modifiedRecords.length :: " + modifiedRecords.length);
				List<UnconfimredBudgetVO> unconfirmList = new ArrayList();
				UnconfimredBudgetVO vo = null;

				for (int i = 0; i < modifiedRecords.length; ++i) {
					vo = new UnconfimredBudgetVO();
					String jsonString = modifiedRecords[i];
					Map jsonObject = (Map) JSONValue.parse(jsonString);
					this.logger.debug("json object " + jsonObject);
					String CRN = (String) jsonObject.get("CRN");
					String jsEscapedString = "%u017C";
					ScriptEngineManager factory = new ScriptEngineManager();
					ScriptEngine engine = factory.getEngineByName("JavaScript");
					String unEscpaedCRN = (String) engine.eval("unescape('" + CRN + "')");
					this.logger.debug("UnEscapedCRN" + unEscpaedCRN);
					vo.setCRN(unEscpaedCRN);
					vo.setCaseFee((String) jsonObject.get("caseFee"));
					vo.setTeamId((String) jsonObject.get("teamId"));
					vo.setSupportingTeam1DueDate((String) jsonObject.get("supportingTeam1DueDate"));
					vo.setFinalRDate((String) jsonObject.get("finalRDate"));
					vo.setFinalDate((String) jsonObject.get("finalDate"));
					vo.setCaseMgrId((String) jsonObject.get("caseManager"));
					String isSubjLevelSubRptReq = (String) jsonObject.get("isSubreportrequire");
					vo.setIsSubreportRequire(isSubjLevelSubRptReq);
					if (isSubjLevelSubRptReq.equalsIgnoreCase("true")) {
						org.json.simple.JSONArray jsonArray1 = (org.json.simple.JSONArray) jsonObject
								.get("subjectGridData");
						new JSONArray(jsonArray1.toString());
						vo.setGridData(jsonArray1.toString());
						vo.setTaskName("clientSubmission");
						this.saveSubjectLevelBudget(vo, session);
					}

					vo.setUpdatedBy(userName);
					boolean pingSuccessful = false;
					boolean processFlag = false;
					pingSuccessful = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
					this.logger.debug("pingSuccessful :: " + pingSuccessful);
					if (!pingSuccessful) {
						return "failure";
					}

					ClientCBDVO cbdVo = new ClientCBDVO();
					cbdVo.setCRN(vo.getCRN());
					cbdVo.setBudget(Double.parseDouble(vo.getCaseFee()));
					Calendar cal = Calendar.getInstance();
					Date temp = this.sourceFormat1.parse(vo.getFinalDate());
					this.logger.debug("temp :: " + temp);
					String tempDate = this.targetFormat.format(temp);
					temp = this.targetFormat.parse(tempDate);
					this.logger.debug("tempDate :: " + tempDate + " :: temp :: " + temp);
					cal.setTime(temp);
					this.logger.debug("cal time :: " + cal.getTimeInMillis() + " :: date :: " + cal.getTime()
							+ " :: temp :: " + temp);
					cal.set(16, 0);
					cal.set(15, 0);
					this.logger.debug("cal time :: " + cal.getTimeInMillis() + " :: date :: " + cal.getTime()
							+ " :: temp :: " + temp);
					cbdVo.setDueDate(cal);
					UnconfimredBudgetVO isisVo = this.unconfirmBudgetDAO.getDetailForISIS(vo.getCRN());
					cbdVo.setCurrencyCode(isisVo.getCurrency());
					cbdVo.setStatus(isisVo.getCaseStatus());
					if (isSubjLevelSubRptReq.equalsIgnoreCase("true")) {
						String newValue = "";
						SubjectDetailsVO[] subjectLevelBudgetVoArray = (SubjectDetailsVO[]) null;
						String[] modifiedRecords1 = (String[]) null;
						int gridDataLen = false;
						org.json.simple.JSONArray jsonArray1 = (org.json.simple.JSONArray) jsonObject
								.get("subjectGridData");
						JSONArray jsonArray2 = new JSONArray(jsonArray1.toString());
						vo.setGridData(jsonArray1.toString());
						vo.setTaskName("clientSubmission");
						int gridDataLen = jsonArray2.length();
						modifiedRecords1 = new String[gridDataLen];
						new ArrayList();

						for (int j = 0; j < modifiedRecords1.length; ++j) {
							modifiedRecords1[j] = jsonArray2.optString(j);
						}

						subjectLevelBudgetVoArray = new SubjectDetailsVO[modifiedRecords1.length];
						float newCaseFee = 0.0F;
						this.getCountryCode();

						for (int k = 0; k < modifiedRecords1.length; ++k) {
							this.logger.debug("Inside modifiedRecords1 records");
							String JSONstring = modifiedRecords1[k];
							Map jsonObject1 = (Map) JSONValue.parse(JSONstring);
							SubjectDetailsVO subjectLevelBudgetVO = new SubjectDetailsVO();
							newCaseFee += Float.parseFloat(jsonObject1.get("slBudget").toString());
							if (jsonObject1.get("isisSubjectId").toString() != null) {
								subjectLevelBudgetVO.setISISSubjectID(jsonObject1.get("isisSubjectId").toString());
							}

							if (jsonObject1.get("subjectName").toString() != null) {
								subjectLevelBudgetVO.setSubjectName(jsonObject1.get("subjectName").toString());
							}

							if (jsonObject1.get("countryId").toString() != null) {
								subjectLevelBudgetVO.setCountry(((CountryMasterVO) this.countryMap
										.get(Integer.parseInt(jsonObject1.get("countryId").toString())))
												.getCountryCode());
							}

							if (jsonObject1.get("entityTypeId").toString() != null) {
								subjectLevelBudgetVO
										.setEntityType(Integer.parseInt(jsonObject1.get("entityTypeId").toString()));
							}

							this.logger.debug("####" + jsonObject1.get("subReportTypeId").toString());
							if (jsonObject1.get("subReportTypeId").toString() != null
									&& jsonObject1.get("subReportTypeId").toString().trim().length() > 0) {
								subjectLevelBudgetVO.setSubReportCode(jsonObject1.get("subReportTypeId").toString());
							}

							if (jsonObject1.get("slBudget").toString() != null) {
								subjectLevelBudgetVO
										.setSLBudget((double) Float.parseFloat(jsonObject1.get("slBudget").toString()));
							}

							if (jsonObject1.get("currencycode").toString() != null) {
								subjectLevelBudgetVO.setSLCurrency(jsonObject1.get("currencycode").toString());
							}

							subjectLevelBudgetVoArray[k] = subjectLevelBudgetVO;
							newValue = newValue + "\r\n" + "Subject name  " + jsonObject1.get("subjectName").toString()
									+ " Subject Budget " + jsonObject1.get("slBudget").toString();
						}

						cbdVo.setSubjectList(subjectLevelBudgetVoArray);
						vo.setCaseFee(String.valueOf(newCaseFee));
						cbdVo.setBudget(Double.parseDouble(vo.getCaseFee()));
						vo.setNewInfo(newValue);
					}

					processFlag = ResourceLocator.self().getAtlasWebServiceClient().updateCBD(cbdVo);
					this.logger.debug("processFlag :: " + processFlag);
					if (processFlag) {
						CaseDetails oldCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
								.getDetailForCase(unEscpaedCRN);
						this.unconfirmBudgetDAO.saveUnconfirmDetails(vo);
						long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(unEscpaedCRN);
						HashMap<String, Object> dsValues = new HashMap();
						dsValues.put("CRN", unEscpaedCRN);
						dsValues.put("CaseManager", jsonObject.get("caseManager"));
						this.logger.debug("finalResearchDate >>> " + vo.getFinalRDate());
						this.logger.debug("finalClientDate >>> " + vo.getFinalDate());
						dsValues.put("RFinal", vo.getFinalRDate());
						dsValues.put("CFinal", vo.getFinalDate());
						CaseDetails newCaseDetails = ResourceLocator.self().getOfficeAssignmentService()
								.getDetailForCase(unEscpaedCRN);
						newCaseDetails.setCrn(unEscpaedCRN);
						newCaseDetails.setFinalRDueDate(new java.sql.Date(
								this.sourceFormat1.parse((String) jsonObject.get("finalRDate")).getTime()));
						newCaseDetails.setFinalDueDate(new java.sql.Date(
								this.sourceFormat1.parse((String) jsonObject.get("finalDate")).getTime()));
						newCaseDetails.setCaseMgrId((String) jsonObject.get("caseManager"));
						newCaseDetails.setUpdatedBy(userName);
						ResourceLocator.self().getFlowService().updateCaseInformationDS(session, oldCaseDetails,
								newCaseDetails, dsValues);
						CaseHistory caseHistory = new CaseHistory();
						caseHistory.setPid(oldCaseDetails.getPid());
						caseHistory.setCRN(oldCaseDetails.getCrn());
						caseHistory.setPerformer(caseHistoryPerformer);
						caseHistory.setTaskName("");
						caseHistory.setTaskStatus("");
						caseHistory.setProcessCycle((String) ResourceLocator.self().getSBMService()
								.getDataslotValue(Long.parseLong(oldCaseDetails.getPid()), "ProcessCycle", session));
						newCaseDetails.setCaseHistoryPerformer(caseHistoryPerformer);
						ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails,
								caseHistory, "caseDetails");
						vo.setPid(pid);
						vo.setCaseHistoryPerformer(caseHistoryPerformer);
						unconfirmList.add(vo);
					} else if (failedCrnList.isEmpty()) {
						failedCrnList = failedCrnList + vo.getCRN();
					} else {
						failedCrnList = failedCrnList + "," + vo.getCRN();
					}

					this.logger.debug("failedCrnList >>>> " + failedCrnList);
				}

				ResourceLocator.self().getCaseHistoryService().setCaseHistoryForConfirmBudgetAndDueDate(unconfirmList);
			} else {
				this.logger.debug("doing nothing as modifiedRecords is null");
			}

			return failedCrnList;
		} catch (NumberFormatException var37) {
			throw new CMSException(this.logger, var37);
		} catch (Exception var38) {
			throw new CMSException(this.logger, var38);
		}
	}

	public String getCrnOffice(String crn) throws CMSException {
		try {
			String officeId = this.unconfirmBudgetDAO.getCrnOffice(crn);
			return officeId;
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void saveSubjectLevelBudget(UnconfimredBudgetVO vo, Session session) throws CMSException {
		try {
			this.logger.debug("In UnconfirmBudgetManager:saveSubjectLevelBudget");
			if (vo.getTaskName() != null && vo.getTaskName().equalsIgnoreCase("clientSubmission")) {
				String gridData = vo.getGridData();
				int result = ResourceLocator.self().getSubjectService().updateSubjectBudget(gridData, vo.getCRN());
				this.logger.debug("No. of subject Updated" + result);
			} else {
				String[] modifiedRecords = vo.getModifiedRecords();
				String gridData = Arrays.toString(modifiedRecords);
				int result = ResourceLocator.self().getSubjectService().updateSubjectBudget(gridData, vo.getCRN());
				this.logger.debug("No. of subject Updated" + result);
			}

		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void getCountryCode() {
		this.logger.debug("In UnconfirmBudgetManager:saveSubjectLevelBudgetGetCountryCode :::::");

		try {
			List countryList = ResourceLocator.self().getCacheService().getCacheItemsList("COUNTRY_MASTER");
			Iterator iterator = countryList.iterator();

			while (iterator.hasNext()) {
				CountryMasterVO cvo = (CountryMasterVO) iterator.next();
				this.countryMap.put(cvo.getCountryMasterId(), cvo);
			}
		} catch (CMSException var4) {
			var4.printStackTrace();
		}

	}
}