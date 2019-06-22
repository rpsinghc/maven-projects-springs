package com.worldcheck.atlas.bl.masters;

import com.integrascreen.orders.ClientsVO;
import com.integrascreen.orders.ErrorDetailObjects;
import com.integrascreen.orders.ErrorObjects;
import com.integrascreen.orders.ReportTypesVo;
import com.integrascreen.orders.ResearchElementsVO;
import com.integrascreen.orders.RiskMapping;
import com.integrascreen.orders.RiskMappingsVO;
import com.integrascreen.orders.RiskMasterVO;
import com.integrascreen.orders.RiskSuccessVO;
import com.integrascreen.orders.SubReportTypesVO;
import com.integrascreen.orders.SubjectCountriesVO;
import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.bl.interfaces.IRiskMaster;
import com.worldcheck.atlas.dao.masters.RiskDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskAttributesMasterVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RisksHistoryVO;
import com.worldcheck.atlas.vo.masters.RisksMapVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RiskManager implements IRiskMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.RiskManager");
	private RiskDAO riskDAO;
	private String Add = "Add";
	private String UPDATE = "Update";
	private String BLANK = "";

	public void setRiskDAO(RiskDAO riskDAO) {
		this.riskDAO = riskDAO;
	}

	public List<RisksMasterVO> getRiskGrid(RisksMasterVO risksMasterVO, int start, int limit, String sortColumnName,
			String sortType) throws CMSException {
		risksMasterVO.setStart(start + 1);
		risksMasterVO.setLimit(start + limit);
		risksMasterVO.setSortColumnName(sortColumnName);
		risksMasterVO.setSortType(sortType);
		List<RisksMasterVO> riskList = this.riskDAO.getRiskGrid(risksMasterVO);
		return riskList;
	}

	public List<RisksMasterVO> getRisks(RisksMasterVO risksMasterVO, int start, int limit, String sortColumnName,
			String sortType) throws CMSException {
		String associatedSubReportTypes = null;
		String updatedSubReports = null;
		risksMasterVO.setStart(start + 1);
		risksMasterVO.setLimit(start + limit);
		risksMasterVO.setSortColumnName(sortColumnName);
		risksMasterVO.setSortType(sortType);
		if (!risksMasterVO.getSelectedReportTypes().trim().isEmpty()
				&& !this.BLANK.equals(risksMasterVO.getSelectedReportTypes())) {
			associatedSubReportTypes = this.riskDAO.getAssociatedSubreportTypes(risksMasterVO);
			this.logger.debug("associatedSubReportTypes:::" + associatedSubReportTypes);
			if (associatedSubReportTypes != null) {
				this.logger.debug("In associatedSubReportTypes not null condition::");
				risksMasterVO.setAssociatedSubReportTypeList(this.getFiltersList(associatedSubReportTypes));
				this.logger.debug("associated subreporttype list:::" + risksMasterVO.getAssociatedSubReportTypeList());
				String[] subReportArr = associatedSubReportTypes.split(",");
				if (risksMasterVO.getSubReportTypeList() != null) {
					List<String> tempArr = risksMasterVO.getSubReportTypeList();
					this.logger.debug("subreportType List::" + risksMasterVO.getSubReportTypeList());
					Object[] obj = tempArr.toArray();
					Object[] var14 = obj;
					int var13 = obj.length;

					for (int var12 = 0; var12 < var13; ++var12) {
						Object selectSubReport = var14[var12];
						this.logger.debug("**" + selectSubReport + "**");
						String[] var18 = subReportArr;
						int var17 = subReportArr.length;

						for (int var16 = 0; var16 < var17; ++var16) {
							String associateSubReport = var18[var16];
							if (((String) selectSubReport).equalsIgnoreCase(associateSubReport)) {
								this.logger.debug("selectSubReport in true condition:::" + selectSubReport);
								tempArr.remove(selectSubReport);
							}
						}
					}

					this.logger.debug("****Selected Sub Report Types:::" + tempArr.toString());
					Iterator var21 = tempArr.iterator();

					while (var21.hasNext()) {
						String str = (String) var21.next();
						if (updatedSubReports == null) {
							updatedSubReports = str;
						} else {
							updatedSubReports = updatedSubReports + "," + str;
						}
					}

					this.logger.debug("****updatedSubReports:::" + updatedSubReports);
					risksMasterVO.setSelectedSubReportTypes(updatedSubReports != null ? updatedSubReports : "");
					this.logger.debug("selected subreport types:::" + risksMasterVO.getSelectedSubReportTypes());
					if (!risksMasterVO.getSelectedSubReportTypes().trim().isEmpty()
							&& !this.BLANK.equals(risksMasterVO.getSelectedSubReportTypes())) {
						risksMasterVO
								.setSubReportTypeList(this.getFiltersList(risksMasterVO.getSelectedSubReportTypes()));
					}
				}
			}
		}

		List<RisksMasterVO> riskList = this.riskDAO.getRisks(risksMasterVO);
		return riskList;
	}

	public int getRiskGridCount(RisksMasterVO risksMasterVO) throws CMSException {
		return this.riskDAO.getRiskGridCount(risksMasterVO);
	}

	public int getRisksCount(RisksMasterVO risksMasterVO) throws CMSException {
		return this.riskDAO.getRisksCount(risksMasterVO);
	}

	public List<RisksMasterVO> getRiskCaseHistory(RisksMasterVO risksMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		risksMasterVO.setStart(start + 1);
		risksMasterVO.setLimit(start + limit);
		risksMasterVO.setSortColumnName(sortColumnName);
		risksMasterVO.setSortType(sortType);
		List<RisksMasterVO> riskList = this.riskDAO.getRiskCaseHistory(risksMasterVO);
		return riskList;
	}

	public int getRiskCaseHistoryCount(RisksMasterVO risksMasterVO) throws CMSException {
		return this.riskDAO.getRiskCaseHistoryCount(risksMasterVO);
	}

	public String changeRiskStatus(String[] riskCodeList, String riskStatus, String updatedBy,
			ArrayList<RisksMasterVO> risksList) throws CMSException {
		String successMessage = "";
		String failureMessage = "";
		String resultMessage = null;
		String risksOnSuccess = "";
		String risksOnFailure = "";

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Risk");
			this.logger.debug("riskStatus in riskManager::::" + riskStatus);
			if (riskStatus.equals("1")) {
				upMasterVO.setUpdateType("Update");
			} else {
				upMasterVO.setUpdateType("Activate");
			}

			this.logger.debug("update type in upMasterVO:::" + upMasterVO.getUpdateType());
			RiskMasterVO[] masterObject = new RiskMasterVO[riskCodeList.length];
			this.logger.debug("<=========== XML Starts from here ===============>");
			this.logger.debug("<Risk>");
			this.logger.debug("<UpdateType>" + upMasterVO.getUpdateType() + "</UpdateType>");
			RiskMasterVO riskDetails = null;
			RisksMasterVO risksMasterVO = null;
			int riskListSize = risksList.size();

			for (int i = 0; i < riskListSize; ++i) {
				riskDetails = new RiskMasterVO();
				risksMasterVO = (RisksMasterVO) risksList.get(i);
				riskDetails.setCode(risksMasterVO.getRiskCode());
				riskDetails.setLabel(risksMasterVO.getRiskLabel());
				riskDetails.setRiskIsActive(risksMasterVO.getRisksStatus().equals("1") ? 0 : 1);
				riskDetails.setRemarks(risksMasterVO.getRemarks());
				riskDetails.setRiskCategoryID(Integer.parseInt(risksMasterVO.getRiskCategory()));
				riskDetails.setDisplayOnProfile(risksMasterVO.getDisplayOnProfileForm());
				if (risksMasterVO.getRisksStatus().equals("1")) {
					riskDetails.setAction("Update");
				} else {
					riskDetails.setAction("Activate");
				}

				this.logger.debug("<Action>" + riskDetails.getAction() + "</Action>");
				this.logger.debug("<Code>" + riskDetails.getCode() + "</Code>");
				this.logger.debug("<Label>" + riskDetails.getLabel() + "</Label>");
				this.logger.debug("<RiskCategoryId>" + riskDetails.getRiskCategoryID() + "</RiskCategoryId>");
				this.logger.debug("<RiskIsActive>" + riskDetails.getRiskIsActive() + "</RiskIsActive>");
				this.logger.debug("<DisplayOnProfile>" + riskDetails.getDisplayOnProfile() + "</DisplayOnProfile>");
				this.logger.debug("<Remarks>" + riskDetails.getRemarks() + "</Remarks>");
				masterObject[i] = riskDetails;
			}

			this.logger.debug("</Risk>");
			this.logger.debug("<============ XML Ends Here ===============>");
			upMasterVO.setRiskMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			String riskCode;
			String riskLabel;
			int i;
			if (isisResponseVO.getResponseVO().getResponseDetailsVo().getSuccessObject() != null) {
				this.logger.debug("When successObject is not null:::");
				String[] riskCodes = isisResponseVO.getResponseVO().getResponseDetailsVo().getSuccessObject().getRisks()
						.getRiskCode().getCodes();
				this.logger.debug("riskcodes on success after response from ISIS:::" + riskCodes.toString());

				for (i = 0; i < riskCodes.length; ++i) {
					for (int j = 0; j < riskCodeList.length; ++j) {
						riskCode = riskCodeList[j].split("#")[0];
						riskLabel = riskCodeList[j].split("#")[1];
						if (riskCode.equalsIgnoreCase(riskCodes[i])) {
							if (risksOnSuccess.equals(this.BLANK)) {
								risksOnSuccess = riskLabel;
							} else {
								risksOnSuccess = risksOnSuccess + "," + riskLabel;
							}
						}
					}
				}

				successMessage = this.riskDAO.changeRiskStatus(riskCodes, riskStatus, updatedBy) + "#" + risksOnSuccess;
			}

			if (isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects() != null) {
				this.logger.debug("When errorDetails Object is not null:::");
				this.logger.debug("Size of error object:::" + isisResponseVO.getResponseVO().getResponseDetailsVo()
						.getErrorDetailObjects().getRisks().length);
				ErrorObjects[] errors = isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects()
						.getRisks();

				for (i = 0; i < errors.length; ++i) {
					risksOnFailure = "";
					this.logger.debug("errors[i]:::" + errors[i]);
					ErrorObjects error = errors[i];
					if (errors[i] != null) {
						String message = error.getErrorMessage();
						String errorCode = error.getErrorCode();
						this.logger.debug("error message:: " + message + "    errorCode:::" + errorCode);
						String[] codes = error.getRiskCode().getCodes();

						for (int j = 0; j < codes.length; ++j) {
							for (int k = 0; k < riskCodeList.length; ++k) {
								riskCode = riskCodeList[k].split("#")[0];
								riskLabel = riskCodeList[k].split("#")[1];
								if (riskCode.equalsIgnoreCase(codes[j])) {
									if (risksOnFailure.equals(this.BLANK)) {
										risksOnFailure = riskLabel;
									} else {
										risksOnFailure = risksOnFailure + "," + riskLabel;
									}
								}
							}
						}

						this.logger.debug("riskCodesOnFailure:::" + risksOnFailure);
						if (failureMessage.equals(this.BLANK)) {
							this.logger.debug("message in blank condition:::" + message);
							failureMessage = message + "-" + risksOnFailure;
							this.logger.debug("failureMessage in blank condition:::" + failureMessage);
						} else {
							this.logger.debug("message in not blank condition:::" + message);
							failureMessage = failureMessage + "," + message + "-" + risksOnFailure;
							this.logger.debug("failureMessage in not blank condition:::" + failureMessage);
						}

						this.logger.debug("failureMessage:::" + failureMessage);
					}
				}

				this.logger.debug("failureMessage after loop:::" + failureMessage);
			}

			resultMessage = successMessage + "$" + failureMessage;
			this.logger.debug("resultMessage in risk Manager:::" + resultMessage);
			return resultMessage;
		} catch (CMSException var26) {
			throw new CMSException(this.logger, var26);
		}
	}

	public RisksMasterVO getRiskInfo(String riskCode) throws CMSException {
		RisksMasterVO riskMasterVO = this.riskDAO.getRiskInfo(riskCode);
		return riskMasterVO;
	}

	public String updateRisk(RisksMasterVO risksMasterVO, String HiddenValues) throws CMSException {
		try {
			String[] hiddenValues = risksMasterVO.getHiddenText().split(",");
			String[] oldValues = HiddenValues.split("----------");
			risksMasterVO.setOldRemarks(oldValues[0]);
			risksMasterVO.setOldRiskIsActive(oldValues[1]);
			risksMasterVO.setOldDisplayOnProfileForm(oldValues[2]);
			risksMasterVO.setOldCountryBreakdown(Integer.parseInt(oldValues[3]));
			risksMasterVO.setOldRiskLabel(oldValues[4]);
			risksMasterVO.setRiskIsActive(hiddenValues[0].equals("true") ? 1 : 0);
			risksMasterVO.setDisplayOnProfileForm(hiddenValues[2].equals("true") ? 1 : 0);
			risksMasterVO.setRiskCategory(hiddenValues[6]);
			this.logger.debug("category Id inside update is ===" + risksMasterVO.getRiskCategory());
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Risk");
			upMasterVO.setUpdateType(risksMasterVO.getRiskIsActive() == 1 ? "Activate" : this.UPDATE);
			this.logger.debug("upMasterVO update Type is -->>" + upMasterVO.getUpdateType());
			RiskMasterVO masterObject = new RiskMasterVO();
			masterObject.setAction(this.UPDATE);
			masterObject.setCode(risksMasterVO.getRiskCode());
			masterObject.setLabel(risksMasterVO.getRiskLabel());
			masterObject.setRiskIsActive(risksMasterVO.getRiskIsActive() == 0 ? 0 : 1);
			masterObject.setRemarks(risksMasterVO.getRemarks());
			masterObject.setRiskCategoryID(Integer.parseInt(risksMasterVO.getRiskCategory()));
			masterObject.setDisplayOnProfile(risksMasterVO.getDisplayOnProfileForm());
			this.logger.debug("---------------UPDATE RISK XML-----------------------");
			this.logger.debug("<Risk>");
			this.logger.debug("<UpdateType>" + upMasterVO.getUpdateType() + "</UpdateType>");
			this.logger.debug("<Action>" + this.UPDATE + "</Action>");
			this.logger.debug("<Code>" + risksMasterVO.getRiskCode() + "</Code>");
			this.logger.debug("<Label>" + risksMasterVO.getRiskLabel() + "</Label>");
			this.logger.debug(
					"<RiskCategoryId>" + Integer.parseInt(risksMasterVO.getRiskCategory()) + "</RiskCategoryId>");
			this.logger.debug("<RiskIsActive>" + masterObject.getRiskIsActive() + "</RiskIsActive>");
			this.logger.debug("<DisplayOnProfile>" + risksMasterVO.getDisplayOnProfileForm() + "</DisplayOnProfile>");
			this.logger.debug("<Remarks>" + risksMasterVO.getRemarks() + "</Remarks>");
			this.logger.debug("</Risk>");
			this.logger.debug("---------------END OF UPDATE RISK XML-----------------------");
			RiskMasterVO[] riskMasterArray = new RiskMasterVO[]{masterObject};
			upMasterVO.setRiskMaster(riskMasterArray);
			if (Integer.parseInt(risksMasterVO.getOldDisplayOnProfileForm()) == risksMasterVO.getDisplayOnProfileForm()
					&& Integer.parseInt(risksMasterVO.getOldRiskIsActive()) == risksMasterVO.getRiskIsActive()
					&& risksMasterVO.getOldRemarks().equals(risksMasterVO.getRemarks())
					&& risksMasterVO.getOldRiskLabel().equalsIgnoreCase(risksMasterVO.getRiskLabel())) {
				this.logger.debug("No change in risk");
				return "#NoChange";
			} else {
				ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient()
						.updateMaster(upMasterVO);
				if (isisResponseVO.isSuccess()) {
					int count = this.riskDAO.updateRisk(risksMasterVO);
					if (count > 0) {
						this.logger.debug("1 Message" + count + "#success");
						return count + "#success";
					} else {
						this.logger.debug("count <0" + count + "#failure");
						return "Risk Updation Failed#failure";
					}
				} else {
					String errorMessage = "";
					if (isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects() == null) {
						this.logger.debug("error details object is null");
						return "NO Errors Found #failure";
					} else {
						this.logger.debug("inside get Risks of update Risk.");
						ErrorObjects[] errorObject = isisResponseVO.getResponseVO().getResponseDetailsVo()
								.getErrorDetailObjects().getRisks();
						if (errorObject != null) {
							for (int i = 0; i < errorObject.length; ++i) {
								String[] ErrorCodes = errorObject[i].getRiskCode().getCodes();
								String errorCode = "";

								for (int j = 0; j < ErrorCodes.length; ++j) {
									if (j == ErrorCodes.length - 1) {
										errorCode = errorCode + ErrorCodes[j] + "-";
									} else {
										errorCode = errorCode + ErrorCodes[j] + ",";
									}
								}

								errorMessage = errorMessage + errorCode + errorObject[i].getErrorMessage() + "--";
							}
						}

						this.logger.debug("2 error Message" + errorMessage + "#failure");
						return errorMessage + "#failure";
					}
				}
			}
		} catch (CMSException var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public String addRisks(RisksMasterVO risksMasterVO) throws CMSException {
		String PREPEND_RISKCODE = "R00";

		try {
			String[] hiddenValues = risksMasterVO.getHiddenText().split(",");
			risksMasterVO.setRiskCategory(hiddenValues[1]);
			risksMasterVO.setDisplayOnProfile(hiddenValues[2]);
			risksMasterVO.setDisplayOnProfileForm(hiddenValues[2].equals("true") ? 1 : 0);
			risksMasterVO.setRiskType(hiddenValues[3]);
			risksMasterVO.setCountryBreakdown(hiddenValues[4]);
			risksMasterVO.setHasCountryBreakdown(hiddenValues[4].equals("true") ? 1 : 0);
			risksMasterVO.setRiskCategoryLabel(hiddenValues[5]);
			this.logger.debug("risk category label is ----->>>" + hiddenValues[5]);
			int riskId = this.riskDAO.getRiskId();
			String risksCode = PREPEND_RISKCODE + riskId;
			this.logger.debug("risksCode-----" + risksCode);
			risksMasterVO.setRiskCode(risksCode);
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Risk");
			upMasterVO.setUpdateType("Insert");
			RiskMasterVO masterObject = new RiskMasterVO();
			RiskMappingsVO[] riskMapVO = new RiskMappingsVO[risksMasterVO.getRiskMapVO().size()];
			masterObject.setCode(risksCode);
			masterObject.setAction(this.Add);
			masterObject.setDisplayOnProfile(risksMasterVO.getDisplayOnProfileForm());
			masterObject.setLabel(risksMasterVO.getRiskLabel());
			masterObject.setRemarks(risksMasterVO.getRemarks());
			masterObject.setRiskCategoryID(Integer.parseInt(risksMasterVO.getRiskCategory()));
			masterObject.setRiskCategoryLabel(risksMasterVO.getRiskCategoryLabel());
			masterObject.setRiskHasCountryBreakDown(risksMasterVO.getHasCountryBreakdown());
			masterObject.setRiskIsActive(risksMasterVO.getRiskIsActive());
			masterObject.setType(Integer.parseInt(risksMasterVO.getRiskType()));
			masterObject.setCreatedOn(risksMasterVO.getCreationDate());
			this.logger.debug("---------------ADD RISK XML-----------------------");
			this.logger.debug("<Risk>");
			this.logger.debug("<UpdateType>Insert</UpdateType>");
			this.logger.debug("<Action>" + this.Add + "</Action>");
			this.logger.debug("<Code>" + risksCode + "</Code>");
			this.logger.debug("<Label>" + risksMasterVO.getRiskLabel() + "</Label>");
			this.logger.debug(
					"<RiskCategoryId>" + Integer.parseInt(risksMasterVO.getRiskCategory()) + "</RiskCategoryId>");
			this.logger.debug("<RiskCategoryLabel>" + risksMasterVO.getRiskCategoryLabel() + "</RiskCategoryLabel>");
			this.logger.debug("<RiskIsActive>" + risksMasterVO.getRiskIsActive() + "</RiskIsActive>");
			this.logger.debug("<Type>" + Integer.parseInt(risksMasterVO.getRiskType()) + "</Type>");
			this.logger.debug("<RiskHasCountryBreakdown>" + risksMasterVO.getHasCountryBreakdown()
					+ "</RiskHasCountryBreakdown>");
			this.logger.debug("<DisplayOnProfile>" + risksMasterVO.getDisplayOnProfileForm() + "</DisplayOnProfile>");
			this.logger.debug("<Remarks>" + risksMasterVO.getRemarks() + "</Remarks>");
			this.logger.debug("<RiskMappings>");

			int j;
			String[] mappingId;
			for (int i = 0; i < risksMasterVO.getRiskMapVO().size(); ++i) {
				((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).setMappingId(this.riskDAO.getMappingId());
				RiskMappingsVO riskMap = new RiskMappingsVO();
				riskMap.setMappingId(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getMappingId());
				riskMap.setActionType(this.Add);
				this.logger.debug("<RiskMap>");
				this.logger.debug("<MappingId>" + ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getMappingId()
						+ "</MappingId>");
				this.logger.debug("<Action>" + this.Add + "</Action>");
				this.logger.debug("<MappingName>"
						+ ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getRiskMappingName() + "</MappingName>");
				this.logger.debug("<MappingIsActive>"
						+ ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getMappingStatus() + "</MappingIsActive>");
				this.logger.debug("<RiskGroup>" + ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getRiskGroup()
						+ "</RiskGroup>");
				this.logger.debug(
						"<VisibleToClient>" + ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getVisibleToClient()
								+ "</VisibleToClient>");
				this.logger.debug("<LastUpdatedBy>" + ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getUpdatedBy()
						+ "</LastUpdatedBy>");
				if (risksMasterVO.getRiskType().equals("1")) {
					riskMap.setHasSubjectCountries(0);
					this.logger.debug("<HasSubjectCountries>0</HasSubjectCountries>");
				} else {
					riskMap.setSubjectType(
							Integer.parseInt(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getSubjectType()));
					riskMap.setHasSubjectCountries(
							((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getSubjectCountry().equals("-1")
									? 1
									: 0);
					this.logger.debug("<SubjectType>"
							+ Integer.parseInt(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getSubjectType())
							+ "</SubjectType>");
					this.logger.debug(
							"<HasSubjectCountries>" + riskMap.getHasSubjectCountries() + "</HasSubjectCountries>");
				}

				this.logger.debug("<SubjectCountries>");
				String[] countries = ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getCountryCodes().split(",");
				SubjectCountriesVO[] subjectCountries = new SubjectCountriesVO[countries.length];

				for (int j = 0; j < countries.length; ++j) {
					SubjectCountriesVO subjectCountriesVO = new SubjectCountriesVO();
					subjectCountriesVO.setCountry(countries[j]);
					this.logger.debug("<CountryMasterId>" + countries[j] + "</CountryMasterId>");
					subjectCountries[j] = subjectCountriesVO;
				}

				riskMap.setSubjectCountries(subjectCountries);
				this.logger.debug("</SubjectCountries>");
				riskMap.setHasAllClients(
						((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getClientCodes().equals("-1") ? 1 : 0);
				this.logger.debug("<HasAllClients>" + riskMap.getHasAllClients() + "</HasAllClients>");
				this.logger.debug("<Clients>");
				String[] clients = ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getClientCodes().split(",");
				ClientsVO[] clientCodes = new ClientsVO[clients.length];

				for (int j = 0; j < clients.length; ++j) {
					ClientsVO clientCodesVO = new ClientsVO();
					clientCodesVO.setClientCode(clients[j]);
					this.logger.debug("<ClientCode>" + clients[j] + "</ClientCode>");
					clientCodes[j] = clientCodesVO;
				}

				riskMap.setClients(clientCodes);
				this.logger.debug("</Clients>");
				riskMap.setHasAllResearchElements(
						((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getResearchElements().equals("-1") ? 1 : 0);
				this.logger.debug(
						"<HasAllResearchElements>" + riskMap.getHasAllResearchElements() + "</HasAllResearchElements>");
				this.logger.debug("<ResearchElements>");
				mappingId = ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getResearchElements().split(",");
				ResearchElementsVO[] researchElements = new ResearchElementsVO[mappingId.length];

				for (j = 0; j < mappingId.length; ++j) {
					ResearchElementsVO researchElementsVO = new ResearchElementsVO();
					researchElementsVO.setREID(Integer.parseInt(mappingId[j]));
					this.logger.debug("<REid>" + Integer.parseInt(mappingId[j]) + "</REid>");
					researchElements[j] = researchElementsVO;
				}

				riskMap.setResearchElement(researchElements);
				this.logger.debug("</ResearchElements>");
				riskMap.setHasAllReportTypes(
						((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getReportTypes().equals("-1") ? 1 : 0);
				this.logger.debug("<HasAllReportTypes>" + riskMap.getHasAllReportTypes() + "</HasAllReportTypes>");
				this.logger.debug("<ReportTypes>");
				String[] reportType = ((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getReportTypeNames()
						.split(",");
				String repType = "";
				ReportTypesVo[] reportTypes = new ReportTypesVo[reportType.length];

				for (int j = 0; j < reportType.length; ++j) {
					ReportTypesVo reportTypesVO = new ReportTypesVo();
					String[] reptArray = reportType[j].split("-");
					SubReportTypesVO[] subReportTypes = new SubReportTypesVO[reptArray.length - 1];
					this.logger.debug("<ReportType>");

					for (int k = 0; k < reptArray.length; ++k) {
						repType = reptArray[0];
						reportTypesVO.setReportTypeCode(repType);
						if (k == 0) {
							this.logger.debug("<ReportTypeCode>" + repType + "</ReportTypeCode>");
							this.logger.debug("<SubReportTypes>");
						}

						if (k != reptArray.length - 1) {
							SubReportTypesVO subReportType = new SubReportTypesVO();
							subReportType.setSubjReportTypeCode(reptArray[k + 1]);
							this.logger.debug("<SubReportTypeCode>" + reptArray[k + 1] + "</SubReportTypeCode>");
							subReportTypes[k] = subReportType;
						}
					}

					this.logger.debug("</SubReportTypes>");
					this.logger.debug("</ReportType>");
					reportTypesVO.setSubjReportType(subReportTypes);
					reportTypes[j] = reportTypesVO;
				}

				this.logger.debug("</ReportTypes>");
				riskMap.setReportTypes(reportTypes);
				riskMap.setLastUpdatedBy(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getUpdatedBy());
				riskMap.setLastUpdatedOn(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getUpdatedOn());
				riskMap.setMappingIsActive(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getMappingStatus());
				riskMap.setMappingName(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getRiskMappingName());
				riskMap.setRiskGroup(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getRiskGroup());
				riskMap.setCreatedOn(risksMasterVO.getCreationDate());
				riskMap.setVisibleToClients(((RisksMapVO) risksMasterVO.getRiskMapVO().get(i)).getVisibleToClient());
				riskMapVO[i] = riskMap;
				this.logger.debug("</RiskMap>");
			}

			this.logger.debug("</RiskMappings>");
			this.logger.debug("</Risk>");
			this.logger.debug("---------------END OF ADD RISK XML-----------------------");
			masterObject.setRiskMappings(riskMapVO);
			RiskMasterVO[] riskMasterArray = new RiskMasterVO[]{masterObject};
			upMasterVO.setRiskMaster(riskMasterArray);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				risksMasterVO.setRiskCode(risksCode);
				this.riskDAO.addRisks(risksMasterVO);
				return risksMasterVO.getRiskCode() + "#success";
			} else {
				String Message = "";
				if (isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects() == null) {
					this.logger.debug("error details object is null");
					return "No Errors Found #failure";
				} else {
					this.logger.debug("inside addRisks , Error details objects");
					ErrorObjects[] risks = isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects()
							.getRisks();
					if (risks != null) {
						String msg = "";

						for (int i = 0; i < risks.length; ++i) {
							mappingId = risks[i].getRiskCode().getCodes();
							String code = "";

							for (j = 0; j < mappingId.length; ++j) {
								if (j == mappingId.length - 1) {
									code = code + mappingId[j] + "-";
								} else {
									code = code + mappingId[j] + ",";
								}
							}

							msg = code + risks[i].getErrorMessage();
							Message = Message + msg + "--";
						}

						this.logger
								.debug("error occured while saving risk level info at ISIS and message is " + Message);
						return Message + "#failure";
					} else {
						ArrayList<RisksMapVO> totalMapping = risksMasterVO.getRiskMapVO();
						RiskSuccessVO successObject = isisResponseVO.getResponseVO().getResponseDetailsVo()
								.getSuccessObject();
						this.logger.debug("successObject.." + successObject);
						int k;
						if (successObject != null) {
							this.logger.debug("successObject.." + successObject);
							if (successObject.getRiskMapping() != null) {
								mappingId = successObject.getRiskMapping().getMapping().getCodes();
								this.logger.debug("successfull mappings count is " + mappingId.length);
								ArrayList<RisksMapVO> totalMappings = risksMasterVO.getRiskMapVO();
								ArrayList<RisksMapVO> newRiskMapVO = new ArrayList();

								for (k = 0; k < mappingId.length; ++k) {
									this.logger.debug("got some successfull mappings from ISIS end, mapping id is "
											+ mappingId[k]);
									this.logger.debug("totalMappings added were " + totalMappings.size());

									for (int k = 0; k < totalMappings.size(); ++k) {
										this.logger.debug(
												"check condition " + ((RisksMapVO) totalMappings.get(k)).getMappingId()
														+ "==" + Integer.parseInt(mappingId[k]));
										if (((RisksMapVO) totalMappings.get(k)).getMappingId() == Integer
												.parseInt(mappingId[k])) {
											this.logger.debug(
													"inserting successfull mappings...and mapping Ids saved at ISIS end are "
															+ ((RisksMapVO) totalMappings.get(k)).getMappingId());
											newRiskMapVO.add((RisksMapVO) totalMappings.get(k));
										}
									}
								}

								risksMasterVO.setRiskCode(risksCode);
								risksMasterVO.setRiskMapVO(newRiskMapVO);
								this.riskDAO.addRisks(risksMasterVO);
							}
						}

						risksMasterVO.setRiskMapVO(totalMapping);
						ErrorDetailObjects errorDetailsObject = isisResponseVO.getResponseVO().getResponseDetailsVo()
								.getErrorDetailObjects();
						if (errorDetailsObject == null) {
							return "All Mappings Inserted Successfully#success";
						} else {
							this.logger.debug("got some failed mappings...");
							RiskMapping[] riskMapping = errorDetailsObject.getRiskMappings();
							if (riskMapping == null) {
								this.logger.debug("found No risk mappings.");
								return "No successfull or failed Mappings found #failure";
							} else {
								this.logger.debug(
										"INSIDE ADD RISKS IN RISK MANAGER -- no of mappings not added to isis --->>"
												+ riskMapping.length);
								String MessageCodes = "";
								this.logger.debug("riskMapping.length==" + riskMapping.length);

								for (k = 0; k < riskMapping.length; ++k) {
									this.logger.debug("inside risk mapping Loop-- and k is " + k);
									ArrayList<RisksMapVO> totalMaps = risksMasterVO.getRiskMapVO();
									Integer FailedMappingId = riskMapping[k].getMappingID();
									String MappingName = "";
									this.logger.debug("total Mappings added to Risk were --->>" + totalMaps.size());

									for (int c = 0; c < totalMaps.size(); ++c) {
										this.logger.debug(((RisksMapVO) totalMaps.get(c)).getMappingId()
												+ "is equal to == " + FailedMappingId);
										if (((RisksMapVO) totalMaps.get(c)).getMappingId() == FailedMappingId) {
											MappingName = ((RisksMapVO) totalMaps.get(c)).getRiskMappingName();
											this.logger.debug("Mapping Name for failed Mapping is --->>" + MappingName);
										}
									}

									this.logger.debug("Mapping Name inside errorDetails Object is" + MappingName);
									ErrorObjects[] Codes = riskMapping[k].getMappingError();
									MessageCodes = MessageCodes + MappingName + "***";
									this.logger.debug("Codes.length--" + Codes.length);

									for (int j = 0; j < Codes.length; ++j) {
										this.logger.debug("Codes at j = " + j + "are --" + Codes[j]);
										String msg = "";
										if (Codes[j] == null) {
											this.logger.debug("Codes are null");
										} else {
											String mapping;
											int s;
											String[] mappingID;
											if (Codes[j].getReportType() != null) {
												mappingID = Codes[j].getReportType().getCodes();
												mapping = "";
												this.logger.debug("reports----" + mappingID.length);

												for (s = 0; s < mappingID.length; ++s) {
													if (s == mappingID.length - 1) {
														mapping = mapping + mappingID[s];
													} else {
														mapping = mapping + mappingID[s] + ",";
													}
												}

												msg = mapping + "-" + Codes[j].getErrorMessage();
											} else if (Codes[j].getREID() != null) {
												mappingID = Codes[j].getREID().getCodes();
												mapping = "";
												this.logger.debug("re----" + mappingID.length);

												for (s = 0; s < mappingID.length; ++s) {
													if (s == mappingID.length - 1) {
														mapping = mapping + mappingID[s];
													} else {
														mapping = mapping + mappingID[s] + ",";
													}
												}

												msg = mapping + "-" + Codes[j].getErrorMessage();
											} else if (Codes[j].getClientCode() != null) {
												mappingID = Codes[j].getClientCode().getCodes();
												mapping = "";
												this.logger.debug("ClientCodes----" + mappingID.length);

												for (s = 0; s < mappingID.length; ++s) {
													if (s == mappingID.length - 1) {
														mapping = mapping + mappingID[s];
													} else {
														mapping = mapping + mappingID[s] + ",";
													}
												}

												msg = mapping + "-" + Codes[j].getErrorMessage();
											} else if (Codes[j].getSubCountry() != null) {
												mappingID = Codes[j].getSubCountry().getCodes();
												mapping = "";
												this.logger.debug("subjectCountry----" + mappingID.length);

												for (s = 0; s < mappingID.length; ++s) {
													if (s == mappingID.length - 1) {
														mapping = mapping + mappingID[s];
													} else {
														mapping = mapping + mappingID[s] + ",";
													}
												}

												msg = mapping + "-" + Codes[j].getErrorMessage();
											} else if (Codes[j].getSubReportType() != null) {
												mappingID = Codes[j].getSubReportType().getCodes();
												mapping = "";
												this.logger.debug("subReportTypes----" + mappingID.length);

												for (s = 0; s < mappingID.length; ++s) {
													if (s == mappingID.length - 1) {
														mapping = mapping + mappingID[s];
													} else {
														mapping = mapping + mappingID[s] + ",";
													}
												}

												msg = mapping + "-" + Codes[j].getErrorMessage();
											} else if (Codes[j].getMappingID() != null) {
												mappingID = Codes[j].getMappingID().getCodes();
												mapping = "";
												this.logger.debug("mappingID----" + mappingID.length);

												for (s = 0; s < mappingID.length; ++s) {
													if (s == mappingID.length - 1) {
														mapping = mapping + mappingID[s];
													} else {
														mapping = mapping + mappingID[s] + ",";
													}
												}

												msg = mapping + "-" + Codes[j].getErrorMessage();
											}

											if (j == Codes.length - 1) {
												MessageCodes = MessageCodes + msg;
											} else {
												MessageCodes = MessageCodes + msg + "$$";
											}
										}
									}

									MessageCodes = MessageCodes + "@@";
								}

								this.logger.debug("Error codes with their mapping Ids are --->> " + MessageCodes);
								return MessageCodes + "#failure";
							}
						}
					}
				}
			}
		} catch (CMSException var30) {
			throw new CMSException(this.logger, var30);
		}
	}

	public boolean isExistRisk(RisksMasterVO risksMasterVO) throws CMSException {
		int riskNameCount = this.riskDAO.getCountExistRisk(risksMasterVO);
		return riskNameCount > 0;
	}

	public RisksMasterVO checkAssociatedMaster(String riskCode) throws CMSException {
		return this.riskDAO.checkAssociatedMaster(riskCode);
	}

	public List<RiskCategoryMasterVO> getRiskCategory() throws CMSException {
		List<RiskCategoryMasterVO> categoryList = this.riskDAO.getRiskCategory();
		return categoryList;
	}

	public String addMapping(RisksMapVO risksMapVO) throws CMSException {
		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Risk");
			upMasterVO.setUpdateType("Update");
			RiskMasterVO masterObject = new RiskMasterVO();
			masterObject.setCode(risksMapVO.getRiskCode());
			masterObject.setAction(this.UPDATE);
			risksMapVO.setMappingId(this.riskDAO.getMappingId());
			RiskMappingsVO riskMap = new RiskMappingsVO();
			this.logger.debug("---------------ADD MAPPING XML-----------------------");
			this.logger.debug("<Risk>");
			this.logger.debug("<UpdateType>Update</UpdtaeType>");
			this.logger.debug("<Code>" + risksMapVO.getRiskCode() + "</Code>");
			this.logger.debug("<Action>" + this.UPDATE + "</Action>");
			this.logger.debug("<RiskMappings>");
			this.logger.debug("<RiskMap>");
			this.logger.debug("<MappingId>" + risksMapVO.getMappingId() + "</MappingId>");
			this.logger.debug("<ActionType>" + this.Add + "</ActionType>");
			this.logger.debug("<MappingName>" + risksMapVO.getRiskMappingName() + "</MappingName>");
			this.logger.debug("<MappingIsActive>" + risksMapVO.getMappingStatus() + "</MappingIsActive>");
			this.logger.debug("<RiskGroup>" + risksMapVO.getRiskGroup() + "</RiskGroup>");
			this.logger.debug("<VisibleToClient>" + risksMapVO.getVisibleToClient() + "</VisibleToClient>");
			this.logger.debug("<LastUpdatedBy>" + risksMapVO.getUpdatedBy() + "</LastUpdatedBy>");
			riskMap.setMappingId(risksMapVO.getMappingId());
			riskMap.setActionType(this.Add);
			riskMap.setHasAllClients(risksMapVO.getClientCodes().equals("-1") ? 1 : 0);
			riskMap.setHasAllReportTypes(risksMapVO.getReportTypes().equals("-1") ? 1 : 0);
			riskMap.setHasAllResearchElements(risksMapVO.getResearchElements().equals("-1") ? 1 : 0);
			if (!risksMapVO.getSubjectType().equals("1") && !risksMapVO.getSubjectType().equals("2")) {
				riskMap.setHasSubjectCountries(0);
				this.logger.debug("<HasSubjectCountries>0</HasSubjectCountries>");
			} else {
				riskMap.setSubjectType(Integer.parseInt(risksMapVO.getSubjectType()));
				riskMap.setHasSubjectCountries(risksMapVO.getSubjectCountry().equals("-1") ? 1 : 0);
				this.logger.debug("<SubjectType>" + risksMapVO.getSubjectType() + "</SubjectType>");
				this.logger
						.debug("<HasSubjectCountries>" + riskMap.getHasSubjectCountries() + "</HasSubjectCountries>");
			}

			String[] clients;
			int j;
			if (risksMapVO.getCountryCodes() != null) {
				clients = risksMapVO.getCountryCodes().split(",");
				SubjectCountriesVO[] subjectCountries = new SubjectCountriesVO[clients.length];
				this.logger.debug("<SubjectCountries>");

				for (j = 0; j < clients.length; ++j) {
					SubjectCountriesVO subjectCountriesVO = new SubjectCountriesVO();
					subjectCountriesVO.setCountry(clients[j]);
					this.logger.debug("<CountryMasterId>" + clients[j] + "</CountryMasterId>");
					subjectCountries[j] = subjectCountriesVO;
				}

				this.logger.debug("</SubjectCountries>");
				riskMap.setSubjectCountries(subjectCountries);
			}

			this.logger.debug("<HasAllClients>" + riskMap.getHasAllClients() + "</HasAllClients>");
			this.logger.debug("Clients");
			clients = risksMapVO.getClientCodes().split(",");
			ClientsVO[] clientCodes = new ClientsVO[clients.length];

			for (j = 0; j < clients.length; ++j) {
				ClientsVO clientCodesVO = new ClientsVO();
				clientCodesVO.setClientCode(clients[j]);
				this.logger.debug("<ClientCode>" + clients[j] + "</ClientCode>");
				clientCodes[j] = clientCodesVO;
			}

			this.logger.debug("</Clients>");
			riskMap.setClients(clientCodes);
			this.logger.debug(
					"<HasAllResearchElements>" + riskMap.getHasAllResearchElements() + "</HasAllResearchElements>");
			this.logger.debug("<ResearchElements>");
			String[] re = risksMapVO.getResearchElements().split(",");
			ResearchElementsVO[] researchElements = new ResearchElementsVO[re.length];

			for (int j = 0; j < re.length; ++j) {
				ResearchElementsVO researchElementsVO = new ResearchElementsVO();
				researchElementsVO.setREID(Integer.parseInt(re[j]));
				this.logger.debug("<REid>" + Integer.parseInt(re[j]) + "</REid>");
				researchElements[j] = researchElementsVO;
			}

			this.logger.debug("</ResearchElements>");
			riskMap.setResearchElement(researchElements);
			this.logger.debug("<HasAllReportTypes>" + riskMap.getHasAllReportTypes() + "</HasAllReportTypes>");
			this.logger.debug("<ReportTypes>");
			String[] reportType = risksMapVO.getReportTypeNames().split(",");
			String repType = "";
			ReportTypesVo[] reportTypes = new ReportTypesVo[reportType.length];

			for (int j = 0; j < reportType.length; ++j) {
				ReportTypesVo reportTypesVO = new ReportTypesVo();
				String[] report = reportType[j].split("-");
				SubReportTypesVO[] subReportTypes = new SubReportTypesVO[report.length - 1];
				this.logger.debug("<ReportType>");

				for (int k = 0; k < report.length; ++k) {
					repType = report[0];
					reportTypesVO.setReportTypeCode(repType);
					if (k == 0) {
						this.logger.debug("<ReportTypeCode>" + repType + "</ReportTypeCode>");
						this.logger.debug("<SubReportTypes>");
					}

					if (k != report.length - 1) {
						SubReportTypesVO subReportType = new SubReportTypesVO();
						subReportType.setSubjReportTypeCode(report[k + 1]);
						this.logger.debug("<SubReportTypeCode>" + report[k + 1] + "</SubReportTypeCode>");
						subReportTypes[k] = subReportType;
					}
				}

				this.logger.debug("</SubReportTypes>");
				this.logger.debug("</ReportType>");
				reportTypesVO.setSubjReportType(subReportTypes);
				reportTypes[j] = reportTypesVO;
			}

			this.logger.debug("</ReportTypes>");
			riskMap.setReportTypes(reportTypes);
			riskMap.setLastUpdatedBy(risksMapVO.getUpdatedBy());
			riskMap.setLastUpdatedOn(risksMapVO.getUpdatedOn());
			riskMap.setMappingIsActive(risksMapVO.getMappingStatus());
			riskMap.setMappingName(risksMapVO.getRiskMappingName());
			riskMap.setRiskGroup(risksMapVO.getRiskGroup());
			riskMap.setVisibleToClients(risksMapVO.getVisibleToClient());
			this.logger.debug("</RiskMap>");
			this.logger.debug("</RiskMappings>");
			this.logger.debug("</Risk>");
			this.logger.debug("---------------END OF ADD MAPPING XML-----------------------");
			RiskMappingsVO[] riskMapVO = new RiskMappingsVO[]{riskMap};
			masterObject.setRiskMappings(riskMapVO);
			RiskMasterVO[] riskMasterArray = new RiskMasterVO[]{masterObject};
			upMasterVO.setRiskMaster(riskMasterArray);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				return this.riskDAO.addMapping(risksMapVO) + "#success";
			} else {
				String Message = "";
				if (isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects() != null) {
					ErrorObjects[] risks = isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects()
							.getRisks();
					if (risks != null) {
						String msg = "";

						for (int i = 0; i < risks.length; ++i) {
							String[] Codes = risks[i].getRiskCode().getCodes();
							String code = "";

							for (int j = 0; j < Codes.length; ++j) {
								if (j == Codes.length - 1) {
									code = code + Codes[j] + "-";
								} else {
									code = code + Codes[j] + ",";
								}
							}

							msg = code + risks[i].getErrorMessage();
							Message = Message + msg + "--";
						}

						return Message + "#failure";
					}
				} else {
					this.logger.debug("inside addMapping error details object is null");
				}

				ErrorDetailObjects errorDetailsObject = isisResponseVO.getResponseVO().getResponseDetailsVo()
						.getErrorDetailObjects();
				if (errorDetailsObject == null) {
					this.logger.debug("Error Details object null ");
					return "Error Details Object is null#failure";
				} else {
					this.logger.debug("inside errordetails object in addMapping");
					RiskMapping[] riskMapping = errorDetailsObject.getRiskMappings();
					if (riskMapping == null) {
						this.logger.debug("found No risk mappings.");
						return "No successfull or failed Mappings found #failure";
					} else {
						this.logger.debug("INSIDE ADD MAPPING IN RISK MANAGER -- no of mappings not added to isis --->>"
								+ riskMapping.length);
						String MessageCodes = "";

						for (int k = 0; k < riskMapping.length; ++k) {
							Integer FailedMappingId = riskMapping[k].getMappingID();
							String MappingName = "";
							if (risksMapVO.getMappingId() == FailedMappingId) {
								MappingName = risksMapVO.getRiskMappingName();
							}

							this.logger.debug("mapping name inside addmapping is ---->>>" + MappingName);
							ErrorObjects[] Codes = riskMapping[k].getMappingError();
							MessageCodes = MessageCodes + MappingName + "***";

							for (int j = 0; j < Codes.length; ++j) {
								this.logger.debug("Codes at j = " + j + "are " + Codes[j]);
								String msg = "";
								if (Codes[j] == null) {
									this.logger.debug("Codes are null inside add mapping");
								} else {
									String[] mappingID;
									String mapping;
									int s;
									if (Codes[j].getReportType() != null) {
										mappingID = Codes[j].getReportType().getCodes();
										mapping = "";

										for (s = 0; s < mappingID.length; ++s) {
											if (s == mappingID.length - 1) {
												mapping = mapping + mappingID[s];
											} else {
												mapping = mapping + mappingID[s] + ",";
											}
										}

										msg = mapping + "-" + Codes[j].getErrorMessage();
									} else if (Codes[j].getREID() != null) {
										mappingID = Codes[j].getREID().getCodes();
										mapping = "";

										for (s = 0; s < mappingID.length; ++s) {
											if (s == mappingID.length - 1) {
												mapping = mapping + mappingID[s];
											} else {
												mapping = mapping + mappingID[s] + ",";
											}
										}

										msg = mapping + "-" + Codes[j].getErrorMessage();
									} else if (Codes[j].getClientCode() != null) {
										mappingID = Codes[j].getClientCode().getCodes();
										mapping = "";

										for (s = 0; s < mappingID.length; ++s) {
											if (s == mappingID.length - 1) {
												mapping = mapping + mappingID[s];
											} else {
												mapping = mapping + mappingID[s] + ",";
											}
										}

										msg = mapping + "-" + Codes[j].getErrorMessage();
									} else if (Codes[j].getSubCountry() != null) {
										mappingID = Codes[j].getSubCountry().getCodes();
										mapping = "";

										for (s = 0; s < mappingID.length; ++s) {
											if (s == mappingID.length - 1) {
												mapping = mapping + mappingID[s];
											} else {
												mapping = mapping + mappingID[s] + ",";
											}
										}

										msg = mapping + "-" + Codes[j].getErrorMessage();
									} else if (Codes[j].getSubReportType() != null) {
										mappingID = Codes[j].getSubReportType().getCodes();
										mapping = "";

										for (s = 0; s < mappingID.length; ++s) {
											if (s == mappingID.length - 1) {
												mapping = mapping + mappingID[s];
											} else {
												mapping = mapping + mappingID[s] + ",";
											}
										}

										msg = mapping + "-" + Codes[j].getErrorMessage();
									} else if (Codes[j].getMappingID() != null) {
										mappingID = Codes[j].getMappingID().getCodes();
										mapping = "";

										for (s = 0; s < mappingID.length; ++s) {
											if (s == mappingID.length - 1) {
												mapping = mapping + mappingID[s];
											} else {
												mapping = mapping + mappingID[s] + ",";
											}
										}

										msg = mapping + "-" + Codes[j].getErrorMessage();
									}

									if (j == Codes.length - 1) {
										MessageCodes = MessageCodes + msg;
									} else {
										MessageCodes = MessageCodes + msg + "$$";
									}
								}
							}

							MessageCodes = MessageCodes + "@@";
						}

						this.logger.debug("Error codes while adding mapping  --->> " + MessageCodes);
						return MessageCodes + "#failure";
					}
				}
			}
		} catch (CMSException var28) {
			throw new CMSException(this.logger, var28);
		}
	}

	public String addMappings(ArrayList<RisksMapVO> mappingList) throws CMSException {
		int mappingId = 0;

		try {
			for (int i = 0; i < mappingList.size(); ++i) {
				mappingId = this.riskDAO.getMappingId();
				((RisksMapVO) mappingList.get(i)).setMappingId(mappingId);
				this.riskDAO.addMappings((RisksMapVO) mappingList.get(i));
			}

			return mappingId + "#success";
		} catch (CMSException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<RiskAttributesMasterVO> getRiskAttributes(RisksMasterVO risksMasterVO) throws CMSException {
		List<RiskAttributesMasterVO> attributesList = this.riskDAO.getRiskAttributes(risksMasterVO);
		return attributesList;
	}

	public List<RisksMapVO> getAllRisks(RisksMasterVO risksMasterVO) throws CMSException {
		List<RisksMapVO> riskList = this.riskDAO.getAllRisks(risksMasterVO);
		return riskList;
	}

	public static boolean isArraysEquals(Set<?> s1, Set<?> s2) {
		if (s1.size() != s2.size()) {
			return false;
		} else {
			return s1.containsAll(s2);
		}
	}

	public String updateMapping(RisksMapVO risksMapVO) throws CMSException {
		RisksMapVO oldRiskMapVo = this.riskDAO.getOldRiskMapping(risksMapVO.getMappingId());
		String[] oldClientCodeArr = oldRiskMapVo.getClientCodes().split(",");
		String[] newClientCodeArr = risksMapVO.getClientCodes().split(",");
		Set<String> oldClientCodeSet = new HashSet(Arrays.asList(oldClientCodeArr));
		Set<String> newClientCodeSet = new HashSet(Arrays.asList(newClientCodeArr));
		String[] oldReportTypeArr = oldRiskMapVo.getReportTypes().split(",");
		String[] newReportTypeArr = risksMapVO.getReportTypes().split(",");
		Set<String> oldReportTypeSet = new HashSet(Arrays.asList(oldReportTypeArr));
		Set<String> newReportTypeSet = new HashSet(Arrays.asList(newReportTypeArr));
		String[] oldSubReportTypeArr = oldRiskMapVo.getSubReportTypes().split(",");
		String[] newSubReportTypeArr = risksMapVO.getSubReportTypes().split(",");
		Set<String> oldSubReportTypeSet = new HashSet(Arrays.asList(oldSubReportTypeArr));
		Set<String> newSubReportTypeSet = new HashSet(Arrays.asList(newSubReportTypeArr));
		String[] oldResearchElementArr = oldRiskMapVo.getResearchElements().split(",");
		String[] newResearchElementArr = risksMapVO.getResearchElements().split(",");
		Set<String> oldResearchElementSet = new HashSet(Arrays.asList(oldResearchElementArr));
		Set<String> newResearchElementSet = new HashSet(Arrays.asList(newResearchElementArr));
		String[] oldSubCntryArr = new String[0];
		String[] newSubCntryArr = new String[0];
		String subjectType = "";
		String oldSubjectType = "";
		this.logger.debug("----risksMapVO.getSubjectType()---" + risksMapVO.getSubjectType());
		this.logger.debug("----oldRiskMapVo.getSubjectType()---" + oldRiskMapVo.getSubjectType());
		this.logger.debug("----risksMapVO.getSubjectCountry()---" + risksMapVO.getSubjectCountry());
		this.logger.debug("----risksMapVO.getSubjectCountry()---" + risksMapVO.getSubjectCountry());
		if (risksMapVO.getSubjectCountry() != null && !"null".equals(risksMapVO.getSubjectCountry())
				&& !"".equals(risksMapVO.getSubjectCountry())) {
			this.logger.debug("Enter into not null subject country");
			oldSubCntryArr = oldRiskMapVo.getSubjectCountry().split(",");
			newSubCntryArr = risksMapVO.getSubjectCountry().split(",");
		}

		if (risksMapVO.getSubjectType() != null && !"null".equals(risksMapVO.getSubjectType())
				&& !"".equals(risksMapVO.getSubjectCountry())) {
			this.logger.debug("Enter into not null subject type");
			subjectType = risksMapVO.getSubjectType();
			oldSubjectType = oldRiskMapVo.getSubjectType();
		}

		this.logger.debug("----risksMapVO.getSubjectType()---" + risksMapVO.getSubjectType());
		this.logger.debug("----oldRiskMapVo.getSubjectType()---" + oldRiskMapVo.getSubjectType());
		Set<String> oldSubCntrySet = new HashSet(Arrays.asList(oldSubCntryArr));
		HashSet newSubCntrySet = new HashSet(Arrays.asList(newSubCntryArr));

		try {
			if (risksMapVO.getRiskMappingName().equals(oldRiskMapVo.getRiskMappingName())
					&& subjectType.equals(oldSubjectType)
					&& risksMapVO.getVisibleToClient() == oldRiskMapVo.getVisibleToClient()
					&& risksMapVO.getRiskGroup() == oldRiskMapVo.getRiskGroup()
					&& isArraysEquals(oldClientCodeSet, newClientCodeSet)
					&& isArraysEquals(oldReportTypeSet, newReportTypeSet)
					&& isArraysEquals(oldSubReportTypeSet, newSubReportTypeSet)
					&& isArraysEquals(oldResearchElementSet, newResearchElementSet)
					&& isArraysEquals(oldSubCntrySet, newSubCntrySet)) {
				return "#NoChange";
			} else {
				UPMasterVO upMasterVO = new UPMasterVO();
				upMasterVO.setMaster("Risk");
				upMasterVO.setUpdateType(this.UPDATE);
				RiskMasterVO masterObject = new RiskMasterVO();
				masterObject.setCode(risksMapVO.getRiskCode());
				masterObject.setAction(this.UPDATE);
				RiskMappingsVO riskMap = new RiskMappingsVO();
				riskMap.setMappingId(risksMapVO.getMappingId());
				riskMap.setActionType(this.UPDATE);
				riskMap.setHasAllClients(risksMapVO.getClientCodes().equals("-1") ? 1 : 0);
				riskMap.setHasAllReportTypes(risksMapVO.getReportTypes().equals("-1") ? 1 : 0);
				riskMap.setHasAllResearchElements(risksMapVO.getResearchElements().equals("-1") ? 1 : 0);
				this.logger.debug("---------------UPDATE MAPPING XML-----------------------");
				this.logger.debug("<Risk>");
				this.logger.debug("<UpdateType>" + this.UPDATE + "</UpdtaeType>");
				this.logger.debug("<Code>" + risksMapVO.getRiskCode() + "</Code>");
				this.logger.debug("<Action>" + this.UPDATE + "</Action>");
				this.logger.debug("<RiskMappings>");
				this.logger.debug("<RiskMap>");
				this.logger.debug("<MappingId>" + risksMapVO.getMappingId() + "</MappingId>");
				this.logger.debug("<ActionType>" + this.UPDATE + "</ActionType>");
				this.logger.debug("<MappingName>" + risksMapVO.getRiskMappingName() + "</MappingName>");
				this.logger.debug("<MappingIsActive>" + risksMapVO.getRiskMappingName() + "</MappingIsActive>");
				this.logger.debug("<RiskGroup>" + risksMapVO.getRiskGroup() + "</RiskGroup>");
				this.logger.debug("<VisibleToClient>" + risksMapVO.getVisibleToClient() + "</VisibleToClient>");
				this.logger.debug("<LastUpdatedBy>" + risksMapVO.getUpdatedBy() + "</LastUpdatedBy>");
				if (!risksMapVO.getSubjectType().equals("1") && !risksMapVO.getSubjectType().equals("2")) {
					riskMap.setHasSubjectCountries(0);
					this.logger.debug("<HasSubjectCountries>0</HasSubjectCountries>");
				} else {
					riskMap.setSubjectType(Integer.parseInt(risksMapVO.getSubjectType()));
					riskMap.setHasSubjectCountries(risksMapVO.getSubjectCountry().equals("-1") ? 1 : 0);
					this.logger.debug("<SubjectType>" + risksMapVO.getSubjectType() + "</SubjectType>");
					this.logger.debug(
							"<HasSubjectCountries>" + riskMap.getHasSubjectCountries() + "</HasSubjectCountries>");
				}

				String[] clients;
				int j;
				if (risksMapVO.getCountryCodes() != null) {
					clients = risksMapVO.getCountryCodes().split(",");
					SubjectCountriesVO[] subjectCountries = new SubjectCountriesVO[clients.length];
					this.logger.debug("<SubjectCountries>");

					for (j = 0; j < clients.length; ++j) {
						SubjectCountriesVO subjectCountriesVO = new SubjectCountriesVO();
						subjectCountriesVO.setCountry(clients[j]);
						this.logger.debug("<CountryMasterId>" + clients[j] + "</CountryMasterId>");
						subjectCountries[j] = subjectCountriesVO;
					}

					this.logger.debug("</SubjectCountries>");
					riskMap.setSubjectCountries(subjectCountries);
				}

				this.logger.debug("<HasAllClients>" + riskMap.getHasAllClients() + "</HasAllClients>");
				this.logger.debug("<Clients>");
				clients = risksMapVO.getClientCodes().split(",");
				ClientsVO[] clientCodes = new ClientsVO[clients.length];

				for (j = 0; j < clients.length; ++j) {
					ClientsVO clientCodesVO = new ClientsVO();
					clientCodesVO.setClientCode(clients[j]);
					this.logger.debug("<ClientCode>" + clients[j] + "</ClientCode>");
					clientCodes[j] = clientCodesVO;
				}

				this.logger.debug("</Clients>");
				riskMap.setClients(clientCodes);
				this.logger.debug(
						"<HasAllResearchElements>" + riskMap.getHasAllResearchElements() + "</HasAllResearchElements>");
				this.logger.debug("<ResearchElements>");
				String[] re = risksMapVO.getResearchElements().split(",");
				ResearchElementsVO[] researchElements = new ResearchElementsVO[re.length];

				for (int j = 0; j < re.length; ++j) {
					ResearchElementsVO researchElementsVO = new ResearchElementsVO();
					researchElementsVO.setREID(Integer.parseInt(re[j]));
					this.logger.debug("<REid>" + Integer.parseInt(re[j]) + "</REid>");
					researchElements[j] = researchElementsVO;
				}

				riskMap.setResearchElement(researchElements);
				this.logger.debug("</ResearchElements>");
				this.logger.debug("<HasAllReportTypes>" + riskMap.getHasAllReportTypes() + "</HasAllReportTypes>");
				this.logger.debug("<ReportTypes>");
				String[] reportType = risksMapVO.getReportTypeNames().split(",");
				String repType = "";
				ReportTypesVo[] reportTypes = new ReportTypesVo[reportType.length];

				for (int j = 0; j < reportType.length; ++j) {
					ReportTypesVo reportTypesVO = new ReportTypesVo();
					String[] report = reportType[j].split("-");
					SubReportTypesVO[] subReportTypes = new SubReportTypesVO[report.length - 1];
					this.logger.debug("<ReportType>");

					for (int k = 0; k < report.length; ++k) {
						repType = report[0];
						reportTypesVO.setReportTypeCode(repType);
						if (k == 0) {
							this.logger.debug("<ReportTypeCode>" + repType + "</ReportTypeCode>");
							this.logger.debug("<SubReportTypes>");
						}

						if (k != report.length - 1) {
							SubReportTypesVO subReportType = new SubReportTypesVO();
							subReportType.setSubjReportTypeCode(report[k + 1]);
							this.logger.debug("<SubReportTypeCode>" + report[k + 1] + "</SubReportTypeCode>");
							subReportTypes[k] = subReportType;
						}
					}

					this.logger.debug("</SubReportTypes>");
					this.logger.debug("</ReportType>");
					reportTypesVO.setSubjReportType(subReportTypes);
					reportTypes[j] = reportTypesVO;
				}

				this.logger.debug("</ReportTypes>");
				riskMap.setReportTypes(reportTypes);
				riskMap.setLastUpdatedBy(risksMapVO.getUpdatedBy());
				riskMap.setLastUpdatedOn(risksMapVO.getUpdatedOn());
				riskMap.setMappingIsActive(risksMapVO.getMappingStatus());
				riskMap.setMappingName(risksMapVO.getRiskMappingName());
				riskMap.setRiskGroup(risksMapVO.getRiskGroup());
				riskMap.setVisibleToClients(risksMapVO.getVisibleToClient());
				RiskMappingsVO[] riskMapVO = new RiskMappingsVO[]{riskMap};
				masterObject.setRiskMappings(riskMapVO);
				RiskMasterVO[] riskMasterArray = new RiskMasterVO[]{masterObject};
				upMasterVO.setRiskMaster(riskMasterArray);
				this.logger.debug("</RiskMap>");
				this.logger.debug("</RiskMappings>");
				this.logger.debug("</Risk>");
				this.logger.debug("---------------END OF UPDATE MAPPING XML-----------------------");
				ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient()
						.updateMaster(upMasterVO);
				if (isisResponseVO.isSuccess()) {
					return this.riskDAO.updateMapping(risksMapVO) + "#success";
				} else {
					String Message = "";
					if (isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects() == null) {
						this.logger.debug("inside update mapping error details object is null");
						return "No Errors Found #failure";
					} else {
						ErrorObjects[] risks = isisResponseVO.getResponseVO().getResponseDetailsVo()
								.getErrorDetailObjects().getRisks();
						if (risks != null) {
							String msg = "";

							for (int i = 0; i < risks.length; ++i) {
								String[] Codes = risks[i].getRiskCode().getCodes();
								String code = "";

								for (int j = 0; j < Codes.length; ++j) {
									if (j == Codes.length - 1) {
										code = code + Codes[j] + "-";
									} else {
										code = code + Codes[j] + ",";
									}
								}

								msg = code + risks[i].getErrorMessage();
								Message = Message + msg + "--";
							}

							return Message + "#failure";
						} else {
							ErrorDetailObjects errorDetailsObject = isisResponseVO.getResponseVO()
									.getResponseDetailsVo().getErrorDetailObjects();
							if (errorDetailsObject == null) {
								this.logger.debug("Mapping Inserted Successfully........... ");
								return " Mapping Inserted Successfully#success";
							} else {
								this.logger.debug("inside error details object for updateMapping");
								RiskMapping[] riskMapping = errorDetailsObject.getRiskMappings();
								if (riskMapping == null) {
									this.logger.debug(" risk mapping is null");
									return " No Mappings found ...........#failure";
								} else {
									this.logger.debug(
											"INSIDE ADD MAPPING IN RISK MANAGER -- no of mappings not added to isis --->>"
													+ riskMapping.length);
									String MessageCodes = "";

									for (int k = 0; k < riskMapping.length; ++k) {
										Integer FailedMappingId = riskMapping[k].getMappingID();
										String MappingName = "";
										if (risksMapVO.getMappingId() == FailedMappingId) {
											MappingName = risksMapVO.getRiskMappingName();
										}

										ErrorObjects[] Codes = riskMapping[k].getMappingError();
										MessageCodes = MessageCodes + MappingName + "***";

										for (int j = 0; j < Codes.length; ++j) {
											this.logger.debug("Codes" + Codes[j]);
											String msg = "";
											if (Codes[j] == null) {
												this.logger.debug("Codes are null");
											} else {
												String[] mappingID;
												String mapping;
												int s;
												if (Codes[j].getReportType() != null) {
													mappingID = Codes[j].getReportType().getCodes();
													mapping = "";

													for (s = 0; s < mappingID.length; ++s) {
														if (s == mappingID.length - 1) {
															mapping = mapping + mappingID[s];
														} else {
															mapping = mapping + mappingID[s] + ",";
														}
													}

													msg = mapping + "-" + Codes[j].getErrorMessage();
												} else if (Codes[j].getREID() != null) {
													mappingID = Codes[j].getREID().getCodes();
													mapping = "";

													for (s = 0; s < mappingID.length; ++s) {
														if (s == mappingID.length - 1) {
															mapping = mapping + mappingID[s];
														} else {
															mapping = mapping + mappingID[s] + ",";
														}
													}

													msg = mapping + "-" + Codes[j].getErrorMessage();
												} else if (Codes[j].getClientCode() != null) {
													mappingID = Codes[j].getClientCode().getCodes();
													mapping = "";

													for (s = 0; s < mappingID.length; ++s) {
														if (s == mappingID.length - 1) {
															mapping = mapping + mappingID[s];
														} else {
															mapping = mapping + mappingID[s] + ",";
														}
													}

													msg = mapping + "-" + Codes[j].getErrorMessage();
												} else if (Codes[j].getSubCountry() != null) {
													mappingID = Codes[j].getSubCountry().getCodes();
													mapping = "";

													for (s = 0; s < mappingID.length; ++s) {
														if (s == mappingID.length - 1) {
															mapping = mapping + mappingID[s];
														} else {
															mapping = mapping + mappingID[s] + ",";
														}
													}

													msg = mapping + "-" + Codes[j].getErrorMessage();
												} else if (Codes[j].getSubReportType() != null) {
													mappingID = Codes[j].getSubReportType().getCodes();
													mapping = "";

													for (s = 0; s < mappingID.length; ++s) {
														if (s == mappingID.length - 1) {
															mapping = mapping + mappingID[s];
														} else {
															mapping = mapping + mappingID[s] + ",";
														}
													}

													msg = mapping + "-" + Codes[j].getErrorMessage();
												} else if (Codes[j].getMappingID() != null) {
													mappingID = Codes[j].getMappingID().getCodes();
													mapping = "";

													for (s = 0; s < mappingID.length; ++s) {
														if (s == mappingID.length - 1) {
															mapping = mapping + mappingID[s];
														} else {
															mapping = mapping + mappingID[s] + ",";
														}
													}

													msg = mapping + "-" + Codes[j].getErrorMessage();
												}

												if (j == Codes.length - 1) {
													MessageCodes = MessageCodes + msg;
												} else {
													MessageCodes = MessageCodes + msg + "$$";
												}
											}
										}

										MessageCodes = MessageCodes + "@@";
									}

									this.logger.debug("Error codes while adding mapping  --->> " + MessageCodes);
									return MessageCodes + "#failure";
								}
							}
						}
					}
				}
			}
		} catch (CMSException var53) {
			throw new CMSException(this.logger, var53);
		}
	}

	public String changeMappingStatus(RisksMapVO risksMapVO) throws CMSException {
		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Risk");
			upMasterVO.setUpdateType(risksMapVO.getMappingStatus() == 0 ? "Activate" : this.UPDATE);
			this.logger.debug("upMasterVO update Type is -->>" + upMasterVO.getUpdateType());
			RiskMasterVO masterObject = new RiskMasterVO();
			this.logger.debug("riskCode inside changeMappingStatus is " + risksMapVO.getRiskCode());
			masterObject.setCode(risksMapVO.getRiskCode());
			masterObject.setAction(this.UPDATE);
			RiskMappingsVO riskMap = new RiskMappingsVO();
			riskMap.setMappingId(risksMapVO.getMappingId());
			riskMap.setActionType(this.UPDATE);
			this.logger.debug("mapping name inside getRiskMappingName" + risksMapVO.getRiskMappingName());
			riskMap.setMappingName(risksMapVO.getRiskMappingName());
			riskMap.setLastUpdatedBy(risksMapVO.getUpdatedBy());
			riskMap.setLastUpdatedOn(risksMapVO.getUpdatedOn());
			riskMap.setMappingIsActive(risksMapVO.getMappingStatus() == 1 ? 0 : 1);
			this.logger.debug("------------CHANGE MAPPING STATUS XML---------------");
			this.logger.debug("<Risk>");
			this.logger.debug("<UpdateType>" + upMasterVO.getUpdateType() + "</UpdateType>");
			this.logger.debug("<Action>" + upMasterVO.getUpdateType() + "</Action>");
			this.logger.debug("<Code>" + risksMapVO.getRiskCode() + "</Code>");
			this.logger.debug("<RiskMappings>");
			this.logger.debug("<RiskMap>");
			this.logger.debug("<MappingId>" + risksMapVO.getMappingId() + "</MappingId>");
			this.logger.debug("<ActionType>" + this.UPDATE + "</ActionType>");
			this.logger.debug("<MappingName>" + risksMapVO.getRiskMappingName() + "</MappingName>");
			this.logger.debug("<MappingIsActive>" + riskMap.getMappingIsActive() + "</MappingIsActive>");
			this.logger.debug("<LastUpdatedBy>" + risksMapVO.getUpdatedBy() + "</LastUpdatedBy>");
			this.logger.debug("<RiskMap>");
			this.logger.debug("<RiskMappings>");
			this.logger.debug("</Risk>");
			this.logger.debug("------------END OF CHANGE MAPPING STATUS XML---------------");
			RiskMappingsVO[] riskMapVO = new RiskMappingsVO[]{riskMap};
			masterObject.setRiskMappings(riskMapVO);
			RiskMasterVO[] riskMasterArray = new RiskMasterVO[]{masterObject};
			upMasterVO.setRiskMaster(riskMasterArray);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				return this.riskDAO.changeMappingStatus(risksMapVO) + "#success";
			} else if (isisResponseVO.getResponseVO().getResponseDetailsVo().getErrorDetailObjects() == null) {
				return "No Error Found #failure";
			} else {
				RiskMapping[] riskMappings = isisResponseVO.getResponseVO().getResponseDetailsVo()
						.getErrorDetailObjects().getRiskMappings();
				ErrorObjects[] mappingError = riskMappings[0].getMappingError();
				String Message = "";
				String msg = "";

				for (int i = 0; i < mappingError.length; ++i) {
					String Mappingname = "";
					Mappingname = risksMapVO.getRiskMappingName();
					msg = Mappingname + "-" + mappingError[i].getErrorMessage();
					Message = Message + msg + "--";
				}

				return Message + "#failure";
			}
		} catch (CMSException var14) {
			throw new CMSException(this.logger, var14);
		}
	}

	public int getMappingHistoryGridCount(Integer mappingId) throws CMSException {
		return this.riskDAO.getMappingHistoryGridCount(mappingId);
	}

	public List<RisksHistoryVO> getMappingHistory(RisksHistoryVO risksHistoryVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		risksHistoryVO.setStart(start + 1);
		risksHistoryVO.setLimit(start + limit);
		risksHistoryVO.setSortColumnName(sortColumnName);
		risksHistoryVO.setSortType(sortType);
		List<RisksHistoryVO> mappingList = this.riskDAO.getMappingHistory(risksHistoryVO);
		return mappingList;
	}

	private List<String> getFiltersList(String tempString) {
		List<String> tempList = new ArrayList();
		String[] tempArray = tempString.split(",");
		String[] var7 = tempArray;
		int var6 = tempArray.length;

		for (int var5 = 0; var5 < var6; ++var5) {
			String str = var7[var5];
			tempList.add(str);
		}

		return tempList;
	}

	public List<ClientMasterVO> getLHSClientList(String clientCodes) throws CMSException {
		List clientCodeListUI = null;
		List listofClientCodes;
		if (clientCodes.contains("-1")) {
			this.logger.debug("Inside if, contains All");
			listofClientCodes = this.riskDAO.getLHSClientList(clientCodeListUI);
		} else {
			this.logger.debug("Inside else, doesn't contains All");
			this.logger.debug("clientCodeList==" + clientCodeListUI);
			if (!clientCodes.isEmpty() && !clientCodes.equals("")) {
				this.logger.debug("getLHSClientList Inside not empty condition");
				clientCodeListUI = Arrays.asList(clientCodes.split(","));
			}

			listofClientCodes = this.riskDAO.getLHSClientList(clientCodeListUI);
			ClientMasterVO clientVO = new ClientMasterVO();
			clientVO.setClientCode("-1");
			clientVO.setClientName("All");
			clientVO.setCodeName("All");
			listofClientCodes.add(0, clientVO);
		}

		this.logger.debug("Size of data to be returned on UI for LHS===" + listofClientCodes.size());
		return listofClientCodes;
	}

	public List<ClientMasterVO> getRHSClientList(String clientCodes) throws CMSException {
		List clientCodeListUI = null;
		List<ClientMasterVO> listofClientCodes = new ArrayList();
		if (clientCodes.contains("-1")) {
			this.logger.debug("RHS Inside if, contains All");
			ClientMasterVO clientVO = new ClientMasterVO();
			clientVO.setClientCode("-1");
			clientVO.setClientName("All");
			clientVO.setCodeName("All");
			((List) listofClientCodes).add(0, clientVO);
		} else {
			this.logger.debug("Inside else, doesn't contains All");
			if (!clientCodes.isEmpty() && !clientCodes.equals("")) {
				this.logger.debug("getRHSClientList Inside not empty condition");
				clientCodeListUI = Arrays.asList(clientCodes.split(","));
			}

			this.logger.debug("clientCodeList==" + clientCodeListUI);
			listofClientCodes = this.riskDAO.getRHSClientList(clientCodeListUI);
		}

		this.logger.debug("Size of data to be returned on UI for RHS===" + ((List) listofClientCodes).size());
		return (List) listofClientCodes;
	}

	public List<CountryMasterVO> getLHSCountryList(String countrylist) {
		List countryListUI = null;
		List listofCountryCodes;
		if (countrylist.equals("-1")) {
			this.logger.debug("getLHSCountryList LHS Inside if, contains All");
			listofCountryCodes = this.riskDAO.getLHSCountryList(countryListUI);
		} else {
			this.logger.debug("getLHSCountryList LHS Inside else, doesn't contains All");
			if (!countrylist.isEmpty() && !countrylist.equals("")) {
				this.logger.debug("getLHSCountryList Inside not empty condition");
				countryListUI = Arrays.asList(countrylist.split(","));
			}

			this.logger.debug("countryListUI==" + countryListUI);
			listofCountryCodes = this.riskDAO.getLHSCountryList(countryListUI);
			CountryMasterVO countryMasterVO = new CountryMasterVO();
			countryMasterVO.setCountryMasterId(-1);
			countryMasterVO.setCountryCode("All");
			countryMasterVO.setCountry("All");
			listofCountryCodes.add(0, countryMasterVO);
		}

		this.logger.debug("Size of data to be returned on UI for LHS===" + listofCountryCodes.size());
		return listofCountryCodes;
	}

	public List<CountryMasterVO> getRHSCountryList(String countrylist) {
		List countryListUI = null;
		List<CountryMasterVO> listofCountryCodes = new ArrayList();
		if (countrylist.equals("-1")) {
			this.logger.debug(" getRHSCountryList RHS Inside if, contains All");
			CountryMasterVO countryMasterVO = new CountryMasterVO();
			countryMasterVO.setCountryMasterId(-1);
			countryMasterVO.setCountryCode("All");
			countryMasterVO.setCountry("All");
			((List) listofCountryCodes).add(0, countryMasterVO);
		} else {
			this.logger.debug("getRHSCountryList RHS Inside else, doesn't contains All");
			if (!countrylist.isEmpty() && !countrylist.equals("")) {
				this.logger.debug("getRHSCountryList Inside not empty condition");
				countryListUI = Arrays.asList(countrylist.split(","));
			}

			this.logger.debug("getRHSCountryList countryListUI==" + countryListUI);
			listofCountryCodes = this.riskDAO.getRHSCountryList(countryListUI);
		}

		this.logger.debug(
				"getRHSCountryList Size of data to be returned on UI for RHS===" + ((List) listofCountryCodes).size());
		return (List) listofCountryCodes;
	}
}