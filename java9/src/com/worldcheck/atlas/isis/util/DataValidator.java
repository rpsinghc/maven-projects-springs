package com.worldcheck.atlas.isis.util;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.dao.AtlasWebServiceDAO;
import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseFileDetailsVO;
import com.worldcheck.atlas.isis.vo.ReDetailsVO;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.DBUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DataValidator {
	private AtlasWebServiceDAO atlasWebServiceDAO;
	private DBUtils dbUtils;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.util.DataValidator");

	public void setAtlasWebServiceDAO(AtlasWebServiceDAO atlasWebServiceDAO) {
		this.atlasWebServiceDAO = atlasWebServiceDAO;
	}

	public void setDbUtils(DBUtils dbUtils) {
		this.dbUtils = dbUtils;
	}

	public Map validateMasterDataForCreateCase(CaseDetailsVO caseDetailsVO)
			throws IllegalArgumentException, CMSException {
		this.logger.debug("Inside validateMasterDataForCreateCase method of DataValidator class");
		Map createCaseValidatorMap = new HashMap();
		Map clientMap = this.getClientMapFromCache();
		if (caseDetailsVO.getOrderGUID() != null && !caseDetailsVO.getOrderGUID().equals("")) {
			if (caseDetailsVO.getBulkOrder() != 0 && caseDetailsVO.getBulkOrder() != 1) {
				throw new IllegalArgumentException("Invalid bulk Order flag");
			} else if (caseDetailsVO.getBulkOrder() != 1
					|| caseDetailsVO.getBulkOrderId() != null && !caseDetailsVO.getBulkOrderId().equalsIgnoreCase("")) {
				if (caseDetailsVO.getClientCode() != null && !caseDetailsVO.getClientCode().equalsIgnoreCase("")
						&& clientMap.keySet().contains(caseDetailsVO.getClientCode())) {
					if (caseDetailsVO.getIsisUserWhoPlacedOrder() != null
							&& !caseDetailsVO.getIsisUserWhoPlacedOrder().equals("")) {
						if (caseDetailsVO.getIsisUserEmailId() != null
								&& !caseDetailsVO.getIsisUserEmailId().equals("")) {
							if (caseDetailsVO.getReportTypeId() != null
									&& !caseDetailsVO.getReportTypeId().equalsIgnoreCase("")
									&& this.getReportTypeMapFromCache().keySet()
											.contains(caseDetailsVO.getReportTypeId())) {
								if (caseDetailsVO.getAssignmentType() == null
										|| caseDetailsVO.getAssignmentType().equals("")
										|| !caseDetailsVO.getAssignmentType().equalsIgnoreCase("Office Assignment")
												&& !caseDetailsVO.getAssignmentType()
														.equalsIgnoreCase("Team Assignment")) {
									throw new IllegalArgumentException("Invalid Assignment Type");
								} else if (caseDetailsVO.getClientFinalDueDate() != null
										&& this.validateDateType(caseDetailsVO.getClientFinalDueDate())) {
									if (caseDetailsVO.getOrderReceiptDate() != null
											&& this.validateDateType(caseDetailsVO.getOrderReceiptDate())) {
										if (caseDetailsVO.getResearchFinalDueDate() != null
												&& this.validateDateType(caseDetailsVO.getResearchFinalDueDate())) {
											if (caseDetailsVO.getAssignmentType().equalsIgnoreCase("Office Assignment")
													&& (caseDetailsVO.getOfficeAssignment() == null
															|| caseDetailsVO.getOfficeAssignment().equalsIgnoreCase("")
															|| !this.validateCM(caseDetailsVO.getOfficeAssignment()))) {
												throw new IllegalArgumentException("Invalid Office Assignment");
											} else if (caseDetailsVO.getAssignmentType()
													.equalsIgnoreCase("Office Assignment")
													&& !caseDetailsVO.getOfficeAssignment()
															.equalsIgnoreCase(caseDetailsVO.getCaseManager())) {
												throw new IllegalArgumentException(
														"Given Case Manager and office assignment Case Manger are not same");
											} else if (!caseDetailsVO.getAssignmentType()
													.equalsIgnoreCase("Team Assignment")
													|| caseDetailsVO.getOfficeAssignment() != null
															&& !caseDetailsVO.getOfficeAssignment().equalsIgnoreCase("")
															&& this.validateRH(caseDetailsVO.getOfficeAssignment())) {
												if (caseDetailsVO.getCaseManager() != null
														&& !caseDetailsVO.getCaseManager().equals("")
														&& this.validateCM(caseDetailsVO.getCaseManager())) {
													if (caseDetailsVO.getCurrencyCode() != null
															&& !caseDetailsVO.getCurrencyCode().equalsIgnoreCase("")
															&& this.getCurrencyMapFromCache().keySet()
																	.contains(caseDetailsVO.getCurrencyCode())) {
														if (caseDetailsVO.getExpressCase() != 0
																&& caseDetailsVO.getExpressCase() != 1) {
															throw new IllegalArgumentException(
																	"Invalid Express Case Value");
														} else if (clientMap
																.get(caseDetailsVO.getClientCode()
																		+ "IS_SL_SRPT_FLAG") != null
																&& clientMap
																		.get(caseDetailsVO.getClientCode()
																				+ "IS_SL_SRPT_FLAG")
																		.toString().trim().equals("1") == caseDetailsVO
																				.isIsSLSubReportType()) {
															if (!caseDetailsVO.isIsSLSubReportType()) {
																if (!this.validateCaseLevelSubReportType(
																		caseDetailsVO)) {
																	throw new IllegalArgumentException(
																			"Invalid CL Sub Report Type Code");
																}
															} else if (!caseDetailsVO.isIsBudgetConfirmed()
																	&& caseDetailsVO.isIsBudgetConfirmed()) {
																throw new IllegalArgumentException(
																		"Invalid Value of isBudgetConfirmed");
															}

															List subjectDetailsVOList = AtlasWebServiceUtil
																	.convertFromArrayObjectToArrayList(
																			caseDetailsVO.getSubjectDetails().getItem(),
																			"SubjectDetailsVO");
															if (subjectDetailsVOList != null
																	&& subjectDetailsVOList.size() != 0) {
																Iterator iterator = subjectDetailsVOList.iterator();

																label216 : while (iterator.hasNext()) {
																	SubjectDetailsVO subjectDetailsVO = (SubjectDetailsVO) iterator
																			.next();
																	new ArrayList();
																	if (subjectDetailsVO.getIsisSubjectID() != null
																			&& !subjectDetailsVO.getIsisSubjectID()
																					.equalsIgnoreCase("")) {
																		if (subjectDetailsVO.getSubjectName() != null
																				&& !subjectDetailsVO.getSubjectName()
																						.equalsIgnoreCase("")) {
																			if (subjectDetailsVO.getEntityType() != 1
																					&& subjectDetailsVO
																							.getEntityType() != 2) {
																				throw new IllegalArgumentException(
																						"Invalid Entity Type");
																			}

																			ArrayList reList = (ArrayList) this
																					.getREListCache(subjectDetailsVO
																							.getEntityType());
																			if (subjectDetailsVO
																					.getCountryCode() != null
																					&& !subjectDetailsVO
																							.getCountryCode()
																							.equalsIgnoreCase("")
																					&& this.getCountryMapFromCache()
																							.keySet()
																							.contains(subjectDetailsVO
																									.getCountryCode())) {
																				if (!subjectDetailsVO.isPrimarySubject()
																						&& subjectDetailsVO
																								.isPrimarySubject()) {
																					throw new IllegalArgumentException(
																							"Invalid Primary Subject Flag");
																				}

																				if (caseDetailsVO
																						.isIsSLSubReportType()) {
																					if (!this
																							.validateSubLevelSubReportType(
																									caseDetailsVO,
																									subjectDetailsVO)) {
																						throw new IllegalArgumentException(
																								"Invalid SL Sub Report Type Code");
																					}

																					if (subjectDetailsVO
																							.getSlCurrency() == null
																							|| caseDetailsVO
																									.getCurrencyCode()
																									.equalsIgnoreCase(
																											"")
																							|| !this.getCurrencyMapFromCache()
																									.keySet().contains(
																											subjectDetailsVO
																													.getSlCurrency())) {
																						throw new IllegalArgumentException(
																								"Invalid SL currency code");
																					}
																				}

																				if (subjectDetailsVO
																						.getReDetails() != null
																						&& subjectDetailsVO
																								.getReDetails()
																								.getItem() != null
																						&& subjectDetailsVO
																								.getReDetails()
																								.getItem().length != 0) {
																					List reDetailsVoList = AtlasWebServiceUtil
																							.convertFromArrayObjectToArrayList(
																									subjectDetailsVO
																											.getReDetails()
																											.getItem(),
																									"ReDetailsVO");
																					Iterator iterator2 = reDetailsVoList
																							.iterator();

																					while (true) {
																						while (true) {
																							if (!iterator2.hasNext()) {
																								continue label216;
																							}

																							ReDetailsVO reDetailsVO = (ReDetailsVO) iterator2
																									.next();
																							this.logger.debug(
																									"re id is::::"
																											+ reDetailsVO
																													.getReId());
																							if (!reList.contains(
																									reDetailsVO
																											.getReId())) {
																								throw new IllegalArgumentException(
																										"Invalid RE Id for ISIS Subject Id "
																												+ subjectDetailsVO
																														.getIsisSubjectID());
																							}

																							List biReList = this.atlasWebServiceDAO
																									.getBIRes(
																											subjectDetailsVO
																													.getEntityType());
																							if (biReList != null
																									&& biReList
																											.size() > 0
																									&& biReList
																											.contains(
																													reDetailsVO
																															.getReId())) {
																								createCaseValidatorMap
																										.put("isBIREExist",
																												"true");
																								this.logger.debug(
																										"Case Creation request has BI RE with subject, so task will always created for office assignment irrespective of assignemnt type..");
																							} else {
																								createCaseValidatorMap
																										.put("isBIREExist",
																												"false");
																							}
																						}
																					}
																				}

																				throw new IllegalArgumentException(
																						"No REs found for the ISIS subject Id "
																								+ subjectDetailsVO
																										.getIsisSubjectID());
																			}

																			throw new IllegalArgumentException(
																					"Invalid Country Code");
																		}

																		throw new IllegalArgumentException(
																				"Invalid Subject Name");
																	}

																	throw new IllegalArgumentException(
																			"Invalid ISIS Subject Id");
																}

																if (caseDetailsVO.getFileDetails() != null
																		&& caseDetailsVO.getFileDetails()
																				.getItem() != null
																		&& caseDetailsVO.getFileDetails()
																				.getItem().length > 0) {
																	List fileList = AtlasWebServiceUtil
																			.convertFromArrayObjectToArrayList(
																					caseDetailsVO.getFileDetails()
																							.getItem(),
																					"CaseFileDetailsVO");
																	if (fileList != null && fileList.size() > 0) {
																		Iterator iterator = fileList.iterator();

																		while (iterator.hasNext()) {
																			CaseFileDetailsVO caseFileDetailsVO = (CaseFileDetailsVO) iterator
																					.next();
																			if (caseFileDetailsVO.getFileName() == null
																					|| caseFileDetailsVO.getFileName()
																							.equals("")) {
																				throw new IllegalArgumentException(
																						"Invalid File Name");
																			}

																			if (caseFileDetailsVO.getPath() == null
																					|| caseFileDetailsVO.getPath()
																							.equals("")) {
																				throw new IllegalArgumentException(
																						"Invalid File path");
																			}
																		}
																	}
																}

																return createCaseValidatorMap;
															} else {
																throw new IllegalArgumentException(
																		"No Subjects found with case.");
															}
														} else {
															throw new IllegalArgumentException(
																	"Invalid Is Subject Level Sub Report Type Flag");
														}
													} else {
														throw new IllegalArgumentException("Invalid currency code");
													}
												} else {
													throw new IllegalArgumentException("Invalid Case Manager");
												}
											} else {
												throw new IllegalArgumentException("Invalid Office Assignment");
											}
										} else {
											throw new IllegalArgumentException("Invalid Final Due Date");
										}
									} else {
										throw new IllegalArgumentException("Invalid Order Receipt Date");
									}
								} else {
									throw new IllegalArgumentException("Invalid Client Final Due Date");
								}
							} else {
								this.logger.debug("Report Type Code:::" + caseDetailsVO.getReportTypeId());
								this.logger.debug(
										"getReportTypeMapFromCache() Containes ::" + this.getReportTypeMapFromCache()
												.keySet().contains(caseDetailsVO.getReportTypeId()));
								throw new IllegalArgumentException("Invalid report Type code");
							}
						} else {
							throw new IllegalArgumentException("Invalid ISIS user e-mail id who placed order");
						}
					} else {
						throw new IllegalArgumentException("Invalid ISIS user who placed order");
					}
				} else {
					this.logger.debug("Inside Client code validation clientMap.keySet()" + clientMap.keySet());
					throw new IllegalArgumentException("Atlas: Invalid client code");
				}
			} else {
				throw new IllegalArgumentException("Invalid bulk Order id");
			}
		} else {
			throw new IllegalArgumentException("Invalid order GUID");
		}
	}

	public String validateMasterDataForUpdateCase(CaseDetailsVO caseDetailsVO)
			throws IllegalArgumentException, CMSException {
		this.logger.debug("Inside validateMasterDataForUpdateCase method of DataValidator class");
		String caseFlag = caseDetailsVO.getCaseFlag();
		Map clientMap = this.getClientMapFromCache();
		List<String> flagList = null;
		if (caseFlag != null && !caseFlag.equalsIgnoreCase("")) {
			flagList = this.convertCommaStringToList(caseFlag);
			if (!flagList.contains("c") && !flagList.contains("C") && !flagList.contains("s") && !flagList.contains("S")
					&& !flagList.contains("f") && !flagList.contains("F")) {
				throw new IllegalArgumentException("Invalid caseFlag");
			} else if (caseDetailsVO.getCrn() != null && !caseDetailsVO.getCrn().equals("")) {
				if (ResourceLocator.self().getSubjectService().getCaseStatus(caseDetailsVO.getCrn())
						.equalsIgnoreCase("Cancelled")) {
					throw new IllegalArgumentException("Case status is Cancelled");
				} else {
					if (flagList.contains("c") || flagList.contains("C")) {
						this.logger.debug(caseDetailsVO.getCrn());
						if (caseDetailsVO.getCrn() == null || caseDetailsVO.getCrn().equals("")) {
							throw new IllegalArgumentException("Invalid CRN..");
						}

						if (caseDetailsVO.getExpressCase() != 0 && caseDetailsVO.getExpressCase() != 1) {
							throw new IllegalArgumentException("Invalid Express Case Value");
						}
					}

					ArrayList subjectDetailsVOList;
					Iterator iterator;
					if (flagList.contains("s") || flagList.contains("S")) {
						label287 : {
							subjectDetailsVOList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
									caseDetailsVO.getSubjectDetails().getItem(), "SubjectDetailsVO");
							if (subjectDetailsVOList != null && subjectDetailsVOList.size() != 0) {
								iterator = subjectDetailsVOList.iterator();

								label241 : while (true) {
									SubjectDetailsVO subjectDetailsVO;
									String commaSepratedREIds;
									do {
										if (!iterator.hasNext()) {
											break label287;
										}

										subjectDetailsVO = (SubjectDetailsVO) iterator.next();
										int atlasSubjectId = false;
										commaSepratedREIds = "";
										String subjectFlag = subjectDetailsVO.getSubjectFlag();
										this.logger.debug(subjectFlag);
										if (subjectFlag == null || subjectFlag.equalsIgnoreCase("")) {
											throw new IllegalArgumentException("Invalid Subject Flag");
										}

										flagList = this.convertCommaStringToList(subjectFlag);
										if (!flagList.contains("a") && !flagList.contains("A")
												&& !flagList.contains("u") && !flagList.contains("U")
												&& !flagList.contains("d") && !flagList.contains("D")) {
											throw new IllegalArgumentException("Invalid Subject Flag");
										}

										new ArrayList();
										if (flagList.contains("a") || flagList.contains("A") || flagList.contains("u")
												|| flagList.contains("U") || flagList.contains("d")
												|| flagList.contains("D")) {
											if (subjectDetailsVO.getIsisSubjectID() == null
													|| subjectDetailsVO.getIsisSubjectID().equalsIgnoreCase("")) {
												throw new IllegalArgumentException("Invalid ISIS Subject Id");
											}

											if ((flagList.contains("d") || flagList.contains("D"))
													&& this.atlasWebServiceDAO
															.getPrimarySubjectISISId(caseDetailsVO.getCrn()) != null
													&& !this.atlasWebServiceDAO
															.getPrimarySubjectISISId(caseDetailsVO.getCrn()).equals("")
													&& this.atlasWebServiceDAO
															.getPrimarySubjectISISId(caseDetailsVO.getCrn())
															.equalsIgnoreCase(subjectDetailsVO.getIsisSubjectID())) {
												boolean primarySubFlag = this.checkRequestSubjectForPrimarySubject(
														subjectDetailsVOList, subjectDetailsVO.getIsisSubjectID());
												if (!primarySubFlag) {
													throw new IllegalArgumentException(
															"Primary Subject Can't be deleted");
												}
											}

											if (flagList.contains("u") || flagList.contains("U")
													|| flagList.contains("d") || flagList.contains("D")) {
												if (!this.atlasWebServiceDAO.checkISISSubjectExist(
														subjectDetailsVO.getIsisSubjectID(), caseDetailsVO.getCrn())) {
													throw new IllegalArgumentException(
															"Given ISIS Subject Id doesn't exist with Atlas "
																	+ subjectDetailsVO.getIsisSubjectID());
												}

												int atlasSubjectId = this.atlasWebServiceDAO.getAtlasSubIdFromISISSubId(
														subjectDetailsVO.getIsisSubjectID(), caseDetailsVO.getCrn());
												this.logger.debug("atlas Subject id for ISIS subject Id "
														+ subjectDetailsVO.getIsisSubjectID() + " Is::::: "
														+ atlasSubjectId);
											}
										}
									} while (!flagList.contains("a") && !flagList.contains("A")
											&& !flagList.contains("u") && !flagList.contains("U"));

									if (subjectDetailsVO.getSubjectName() != null
											&& !subjectDetailsVO.getSubjectName().equalsIgnoreCase("")) {
										if (subjectDetailsVO.getEntityType() != 1
												&& subjectDetailsVO.getEntityType() != 2) {
											throw new IllegalArgumentException("Invalid Entity Type");
										}

										ArrayList reList = (ArrayList) this
												.getREListCache(subjectDetailsVO.getEntityType());
										if (subjectDetailsVO.getCountryCode() != null
												&& !subjectDetailsVO.getCountryCode().equalsIgnoreCase("")
												&& this.getCountryMapFromCache().keySet()
														.contains(subjectDetailsVO.getCountryCode())) {
											if (!subjectDetailsVO.isPrimarySubject()
													&& subjectDetailsVO.isPrimarySubject()) {
												throw new IllegalArgumentException("Invalid Primary Subject Flag");
											}

											String IS_SUBJECT_LEVEL_SUBRPT = this.dbUtils.getSigleColumnDataFromTable(
													"CMS_CLIENTCASE", "NVL(IS_SUBJ_LEVEL_SUBRPT_REQ,'0')", "crn",
													caseDetailsVO.getCrn(), (String) null, (String) null);
											this.logger.debug("validate master data :::IS_SUBJECT_LEVEL_SUBRPT:"
													+ IS_SUBJECT_LEVEL_SUBRPT);
											if (IS_SUBJECT_LEVEL_SUBRPT.trim().equals("1") != caseDetailsVO
													.isIsSLSubReportType()) {
												throw new IllegalArgumentException(
														"Invalid Is Subject Level Sub Report Type Flag");
											}

											if (caseDetailsVO.isIsSLSubReportType()) {
												if (!this.validateSubLevelSubReportType(caseDetailsVO,
														subjectDetailsVO)) {
													throw new IllegalArgumentException(
															"Invalid SL Sub Report Type Code");
												}

												if (subjectDetailsVO.getSlCurrency() == null
														|| caseDetailsVO.getCurrencyCode().equalsIgnoreCase("")
														|| !this.getCurrencyMapFromCache().keySet()
																.contains(subjectDetailsVO.getSlCurrency())) {
													throw new IllegalArgumentException("Invalid SL currency code");
												}
											}

											List reDetailsVoList = AtlasWebServiceUtil
													.convertFromArrayObjectToArrayList(
															subjectDetailsVO.getReDetails().getItem(), "ReDetailsVO");
											if (reDetailsVoList != null && reDetailsVoList.size() != 0) {
												Iterator iterator2 = reDetailsVoList.iterator();

												while (true) {
													if (!iterator2.hasNext()) {
														continue label241;
													}

													ReDetailsVO reDetailsVO = (ReDetailsVO) iterator2.next();
													this.logger.debug("re id is::::" + reDetailsVO.getReId());
													if (!reList.contains(reDetailsVO.getReId())) {
														throw new IllegalArgumentException("Invalid RE Id");
													}

													commaSepratedREIds = commaSepratedREIds + reDetailsVO.getReId();
													if (iterator2.hasNext()) {
														commaSepratedREIds = commaSepratedREIds + ",";
													}
												}
											}

											throw new IllegalArgumentException("No REs found for the ISIS subject Id "
													+ subjectDetailsVO.getIsisSubjectID());
										}

										throw new IllegalArgumentException("Invalid Country Code");
									}

									throw new IllegalArgumentException("Invalid Subject Name");
								}
							}

							throw new IllegalArgumentException("No Subjects found with case.");
						}
					}

					if (flagList.contains("f") || flagList.contains("f")) {
						subjectDetailsVOList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
								caseDetailsVO.getFileDetails().getItem(), "CaseFileDetailsVO");
						if (subjectDetailsVOList != null && subjectDetailsVOList.size() != 0) {
							iterator = subjectDetailsVOList.iterator();

							while (iterator.hasNext()) {
								CaseFileDetailsVO caseFileDetailsVO = (CaseFileDetailsVO) iterator.next();
								if (caseFileDetailsVO.getFileName() != null
										&& !caseFileDetailsVO.getFileName().equals("")) {
									if (caseFileDetailsVO.getPath() != null
											&& !caseFileDetailsVO.getPath().equals("")) {
										continue;
									}

									throw new IllegalArgumentException("Invalid File path");
								}

								throw new IllegalArgumentException("Invalid File Name");
							}

							return "";
						} else {
							throw new IllegalArgumentException("No Files found with case.");
						}
					} else {
						return "";
					}
				}
			} else {
				throw new IllegalArgumentException("Invalid CRN..");
			}
		} else {
			throw new IllegalArgumentException("Invalid caseFlag");
		}
	}

	public void validateDownloadReportData(String crn, String fileName, String version)
			throws IllegalArgumentException, CMSException {
		if (crn != null && !crn.equals("")) {
			if (fileName != null && !fileName.equals("")) {
				if (version == null || version.equals("")) {
					throw new IllegalArgumentException("invalid version");
				}
			} else {
				throw new IllegalArgumentException("Invalid fileName");
			}
		} else {
			throw new IllegalArgumentException("Invalid crn");
		}
	}

	public Map getClientMapFromCache() throws CMSException {
		this.logger.debug("Inside getClientMapFromCache method of DataValidator class");
		Map clientMap = new HashMap();
		List<ClientMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("CLIENT_MASTER");
		Iterator iterator = clientMasterList.iterator();

		while (iterator.hasNext()) {
			ClientMasterVO clientMasterVO = (ClientMasterVO) iterator.next();
			clientMap.put(clientMasterVO.getClientCode(), clientMasterVO.getClientName());
			clientMap.put(clientMasterVO.getClientCode() + "IS_SL_SRPT_FLAG", clientMasterVO.getIsSubreportRequired());
		}

		this.logger.debug("clientMap:::" + clientMap);
		return clientMap;
	}

	public Map getOfficeMapFromCache() throws CMSException {
		this.logger.debug("Inside getOfficeMapFromCache method of DataValidator class");
		Map officeMap = new HashMap();
		List<BranchOfficeMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("OFFICE_MASTER");
		Iterator iterator = clientMasterList.iterator();

		while (iterator.hasNext()) {
			BranchOfficeMasterVO branchOfficeMasterVO = (BranchOfficeMasterVO) iterator.next();
			officeMap.put(branchOfficeMasterVO.getBranchOfficeId(), branchOfficeMasterVO.getBranchOffice());
		}

		this.logger.debug("officeMap:::::" + officeMap);
		return officeMap;
	}

	public Map getReportTypeMapFromCache() throws CMSException {
		this.logger.debug("Inside getReportTypeMapFromCache method of DataValidator class");
		Map reportTypeMap = new HashMap();
		List<ReportTypeMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("REPORT_TYPE_MASTER");
		Iterator iterator = clientMasterList.iterator();

		while (iterator.hasNext()) {
			ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) iterator.next();
			reportTypeMap.put(reportTypeMasterVO.getReportTypeCode(), reportTypeMasterVO.getReportType());
			reportTypeMap.put(reportTypeMasterVO.getReportType().trim(), reportTypeMasterVO.getReportTypeId());
		}

		this.logger.debug("reportTypeMap:::::" + reportTypeMap);
		return reportTypeMap;
	}

	public Map getCountryMapFromCache() throws CMSException {
		this.logger.debug("Inside getCountryMapFromCache method of DataValidator class");
		Map countryMap = new HashMap();
		List<CountryMasterVO> countryMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("COUNTRY_MASTER");
		Iterator iterator = countryMasterList.iterator();

		while (iterator.hasNext()) {
			CountryMasterVO countryMasterVO = (CountryMasterVO) iterator.next();
			countryMap.put(countryMasterVO.getCountryCode(), countryMasterVO.getCountry());
		}

		return countryMap;
	}

	public Map getCurrencyMapFromCache() throws CMSException {
		this.logger.debug("Inside getCurrencyMapFromCache method of DataValidator class");
		Map currencyMap = new HashMap();
		List<CurrencyMasterVO> currencyMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList("CURRENCY_MASTER");
		Iterator iterator = currencyMasterList.iterator();

		while (iterator.hasNext()) {
			CurrencyMasterVO currencyMasterVO = (CurrencyMasterVO) iterator.next();
			currencyMap.put(currencyMasterVO.getCurrencyCode(), currencyMasterVO.getCurrency());
		}

		this.logger.debug("currencyMap:::::" + currencyMap);
		return currencyMap;
	}

	public List getREListCache(int entityType) throws CMSException {
		this.logger.debug("Inside getREListCache method of DataValidator class");
		this.logger.debug("entityType is" + entityType);
		List<Integer> reList = new ArrayList();
		String reString = "";
		if (entityType == 1) {
			reString = "RE_MASTER_INDIVIDUAL";
		} else {
			reString = "RE_MASTER_COMPANY";
		}

		List<REMasterVO> reMasterList = ResourceLocator.self().getCacheService().getCacheItemsList(reString);
		Iterator iterator = reMasterList.iterator();

		while (iterator.hasNext()) {
			REMasterVO reMasterVO = (REMasterVO) iterator.next();
			this.logger.debug("re id is:::" + reMasterVO.getrEMasterId());
			reList.add(new Integer(reMasterVO.getrEMasterId()));
		}

		this.logger.debug("reList is..." + reList);
		return reList;
	}

	public boolean validateCM(String caseManager) throws CMSException {
		this.logger.debug("Inside validateCM method of DataValidator class");
		boolean validationFlag = false;
		int count = this.atlasWebServiceDAO.validateCM(caseManager);
		if (count > 0) {
			validationFlag = true;
		}

		return validationFlag;
	}

	public boolean validateRH(String researchHead) throws CMSException {
		this.logger.debug("Inside validateRH method of DataValidator class");
		boolean validationFlag = false;
		int count = this.atlasWebServiceDAO.validateRH(researchHead);
		if (count > 0) {
			validationFlag = true;
		}

		return validationFlag;
	}

	public boolean validateCRNForGUId(String guId) throws CMSException {
		this.logger.debug("Inside validateCRNForGUId method of DataValidator class");
		boolean validationFlag = true;
		validationFlag = this.atlasWebServiceDAO.validateCRN(guId);
		this.logger.debug("validationFlag is:::" + validationFlag);
		return validationFlag;
	}

	public boolean validateDateType(Calendar cal) {
		this.logger.debug("Inside validateDateType method of DataValidator class");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String stringDate = sdf.format(cal.getTime());

		try {
			sdf.parse(stringDate);
			return true;
		} catch (Exception var5) {
			return false;
		}
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

	public List<String> getSubReportsForReport(String reportTypeCode) throws CMSException {
		new ArrayList();
		List<String> subReportList = this.atlasWebServiceDAO.getSubReportsForReport(reportTypeCode);
		return subReportList;
	}

	public boolean validateCaseLevelSubReportType(CaseDetailsVO caseDetailsVO) throws CMSException {
		boolean flag = false;
		if (!caseDetailsVO.isIsSLSubReportType()) {
			label54 : {
				this.logger.debug(
						"Inside method of subjectDetailsVO.getSLSubreportCode :" + caseDetailsVO.getClSubreportCode());
				List<String> subReportIDList = this.getSubReportTypeIDForReport(caseDetailsVO.getReportTypeId());
				if (subReportIDList != null && subReportIDList.size() != 0 || caseDetailsVO.getClSubreportCode() != null
						&& caseDetailsVO.getClSubreportCode().trim().length() != 0) {
					if (caseDetailsVO.getClSubreportCode() != null
							&& caseDetailsVO.getClSubreportCode().trim().length() > 3) {
						try {
							caseDetailsVO.setClSubreportID(
									Integer.parseInt(caseDetailsVO.getClSubreportCode().trim().substring(3)));
						} catch (NumberFormatException var5) {
							return false;
						}

						this.logger.debug(
								"Inside method of caseDetailsVO.getClSubreportID()" + caseDetailsVO.getClSubreportID());
						if (subReportIDList != null && subReportIDList.size() > 0
								&& subReportIDList.contains("" + caseDetailsVO.getClSubreportID())) {
							this.logger
									.debug("Inside method of subReportIDList.contains(caseDetailsVO.getClSubreportID())"
											+ subReportIDList.contains(caseDetailsVO.getClSubreportID()));
							flag = true;
						}
						break label54;
					}

					flag = false;
					return false;
				}

				flag = true;
				return true;
			}
		}

		this.logger.debug("Inside method of flag" + flag);
		return flag;
	}

	public boolean validateSubLevelSubReportType(CaseDetailsVO caseDetailsVO, SubjectDetailsVO subjectDetailsVO)
			throws CMSException {
		boolean flag = false;
		this.logger.debug("Inside validateSubLevelSubReportType method of Datavalidator");
		if (caseDetailsVO.isIsSLSubReportType()) {
			this.logger.debug(
					"Inside method of subjectDetailsVO.getSLSubreportCode :" + subjectDetailsVO.getSlSubreportCode());
			if (subjectDetailsVO.getSlSubreportCode() == null
					|| subjectDetailsVO.getSlSubreportCode().trim().length() <= 3) {
				flag = false;
				return false;
			}

			try {
				subjectDetailsVO
						.setSlSubreportID(Integer.parseInt(subjectDetailsVO.getSlSubreportCode().trim().substring(3)));
			} catch (NumberFormatException var5) {
				return false;
			}

			List<String> subReportIDList = this.getSubReportTypeIDForReport(caseDetailsVO.getReportTypeId());
			this.logger.debug(
					"Inside method of subjectDetailsVO.getSLSubreportID :" + subjectDetailsVO.getSlSubreportID());
			if (subReportIDList != null && subReportIDList.size() > 0
					&& subReportIDList.contains("" + subjectDetailsVO.getSlSubreportID())) {
				this.logger.debug("Inside method of subReportIDList.contains(caseDetailsVO.getClSubreportID())"
						+ subReportIDList.contains(caseDetailsVO.getClSubreportID()));
				flag = true;
			}

			this.logger.debug("Inside method of flag" + flag);
		}

		return flag;
	}

	public List<String> getSubReportTypeIDForReport(String reportTypeCode) throws CMSException {
		new ArrayList();
		List<String> subReportIDList = this.atlasWebServiceDAO.getSubReportTypeIDForReport(reportTypeCode);
		return subReportIDList;
	}

	public String getremovedREList(int subjectId, String newReString) throws CMSException {
		String removedRes = "";

		try {
			String oldRetring = this.atlasWebServiceDAO.getReIdsForSubject(subjectId);
			List oldREList = this.convertCommaStringToList(oldRetring);
			List newREList = this.convertCommaStringToList(newReString);
			new ArrayList();
			List reAddedList = new ArrayList();
			Iterator iterator = newREList.iterator();

			String reId;
			while (iterator.hasNext()) {
				reId = (String) iterator.next();
				oldREList.remove(reId);
			}

			if (oldREList != null && oldREList.size() > 0) {
				iterator = reAddedList.iterator();

				while (iterator.hasNext()) {
					reId = (String) iterator.next();
					removedRes = removedRes + reId;
					if (iterator.hasNext()) {
						removedRes = removedRes + ",";
					}
				}
			}

			return removedRes;
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private boolean checkRequestSubjectForPrimarySubject(List subjectDetailsVOList, String isisId) throws CMSException {
		this.logger.debug("Inside checkRequestSubjectForPrimarySubject of DataValidator class " + isisId);
		boolean primarySubjectFlag = false;

		try {
			Iterator iterator = subjectDetailsVOList.iterator();

			while (iterator.hasNext()) {
				SubjectDetailsVO subjectDetailsVO = (SubjectDetailsVO) iterator.next();
				if (subjectDetailsVO.isPrimarySubject()
						&& !subjectDetailsVO.getIsisSubjectID().equalsIgnoreCase(isisId)) {
					primarySubjectFlag = true;
					this.logger.debug("Subject Name is:::" + subjectDetailsVO.getSubjectName()
							+ " :::primary subject flag is:::" + subjectDetailsVO.isPrimarySubject());
				}
			}

			return primarySubjectFlag;
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}
}