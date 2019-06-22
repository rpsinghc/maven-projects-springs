package com.worldcheck.atlas.dao.subject;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.util.WebServicePropertyReaderUtil;
import com.worldcheck.atlas.isis.vo.SubjectDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseCreationRiskAssociationVO;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.DeleteSubjectLevelRiskVO;
import com.worldcheck.atlas.vo.RiskAggregationVO;
import com.worldcheck.atlas.vo.RiskProfileHistoryVO;
import com.worldcheck.atlas.vo.SubjectColorVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.SubjectLevelRiskProfileDetailsVO;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class SubjectDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.subject.SubjectDAO");
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;
	private String addSubject = "Subject.insertSubject";
	private String deleteSubject = "Subject.deleteSubject";
	private String caseSubjectDetails = "Subject.crnDetail";
	private String subjectDetailsForCRN = "Subject.subjectDetailsForCRN";
	private String matchingSubject = "Subject.matchingSubjectList";
	private String associateCases = "Subject.assCaseList";
	private String insertAssociateCase = "Subject.insertAssCaseList";
	private String insertSubjectColor = "Subject.insertSubjectColor";
	private String getSubjectColor = "Subject.getSubjectColor";
	private String addColorDetails = "Subject.addColorDetails";
	private String getSubjectIdCountForCase = "Subject.subjectIdCountForCase";
	private String getCRNCount = "Subject.getCRNCount";
	private String getDisAssociatedSubjectCount = "Subject.getDisAsscciateCaseCount";
	private String getAssociatedSubjectCount = "Subject.getAssociatedSubjectCount";
	private String industryCodesForSubjects = "Subject.subjectIndCode";
	private String subjectIDList = "Subject.subjectIdList";
	private String subjectIndustryUpdate = "Subject.updateSubInd";
	private String deleteSubjectRisk = "Subject.deleteSubRisk";
	private String insertSubjectRisk = "Subject.insertSubRisk";
	private String reList = "Subject.getReList";
	private String deleteResForSubject = "Subject.deleteResForSub";
	private String insertResForSubject = "Subject.insertResForSub";
	private String resForReportType = "Subject.reForReportType";
	private String resForSubReportType = "Subject.getReForSubReport";
	private String subjectDetail = "Subject.subjectDetail";
	private String subjectReMap = "Subject.reMap";
	private String subjectTeamValidation = "Subject.subjectValidation";
	private String primarySubjectOfCase = "Subject.primarySubj";
	private String deleteAssociateCase = "Subject.deleteAssCRN";
	private String riskForSubject = "Subject.selectRiskForSubject";
	private String updatePrimarySubject = "Subject.UpdatePrimarySubject";
	private String updatePrimarySubjectJuno = "Subject.updatePrimarySubjectJuno";
	private String updateSubject = "Subject.UpdateSubject";
	private String updateSubjectJuno = "Subject.updateSubjectJuno";
	private String updateISISSubjectJuno = "Subject.updateISISSubjectJuno";
	private String getCaseSubjectIndustry = "Subject.getCaseSubjectIndustry";
	private String primasrySubjectDetails = "Subject.primarySubjectDetail";
	private String insertSubject = "Subject.insertSubject";
	private String insertSubjectJuno = "Subject.insertSubjectJuno";
	private String insertISISSubjectJuno = "Subject.insertISISSubjectJuno";
	private String getCaseManager = "Subject.getCaseManager";
	private String getAnalyst = "Subject.getAnalyst";
	private String getReviewer1 = "Subject.getReviewer1";
	private String getReviewer2 = "Subject.getReviewer2";
	private String getReviewer3 = "Subject.getReviewer3";
	private String getManager = "Subject.getManager";
	private String reForEntityType = "Subject.reForEntityType";
	private String getResForsubject = "Subject.getResForsubject";
	private String getTopTenCountries = "Subject.selectTopTenCounties";
	private String getSubjectCountForCase = "Subject.getSubjectCountForCase";
	private String getSubjectIndustryCount = "Subject.getSubjectIndustryCount";
	private String getMatchingAssociateCasesCount = "Subject.getMatchingAssociateCasesCount";
	private String getAsscciateCaseCount = "Subject.getAsscciateCaseCount";
	private String addEditSubjectValidation = "Subject.addEditSubjectValidation";
	private String crnDetailWithoutPagination = "Subject.crnDetailWithoutPagination";
	private String matchingSubjectListForAddSubject = "Subject.matchingSubjectListForAddSubject";
	private String getCaseReport = "Subject.getCaseReport";
	private String getReportType = "Subject.getReportType";
	private String getClientCodes = "Subject.getClientCodes";
	private String getRiskLevelForClientCountry = "Subject.getRiskLevelForClientCountry";
	private String getResForSpecificReportType = "Subject.getResForSpecificReportType";
	private String getResForSpecificSubReportType = "Subject.getResForSpecificSubReportType";
	private String getPIIDForCRN = "Subject.getPIIDForCRN";
	private String getWatchListedSubject = "Subject.isWatchListedSubject";
	private String getCaseManagerId = "Subject.getCaseManagerId";
	private String watchList = "Subject.watchList";
	private String generateIdForNewSubject = "Subject.generateIdForNewSubject";
	private String getCountryListForCode = "Subject.getCountryListForCode";
	private String getREListForClientCode = "Subject.getREListForClientCode";
	private String getListOfSubjectForReplication = "Subject.getListOfSubjectForReplication";
	private String reportTypeForCase = "Subject.ReportTypeForCase";
	private String updateSubjectBudget = "Subject.updateSubjectBudget";
	private String slSubreportFlagForCRN = "Subject.slSubreportFlag";
	private String ASSOCIATE_COUNTRY_BREAKDOWN_CASE_WITH_RISK = "Subject.associateCountryBreakDownCaseWithRisk";
	private String GET_SUBJECT_LEVEL_RISK_MAPPING = "Subject.getSubjectLevelRiskMapping";
	private String ASSOCIATE_SUBJECT_LEVEL_RISK = "Subject.associateSubjectLevelRisk";
	private String isCaseLevelRiskCountryAlreadyExist = "Subject.isCaseLevelRiskCountryAlreadyExist";
	private String getCountryBrkDownCaseLevelRiskDetails = "Subject.getCountryBrkDownCaseLevelRiskDetails";
	private String getSubjectLevelRiskOldDetails = "Subject.getSubjectLevelRiskOldDetails";
	private String getCaseLevelRiskOldDetails = "Subject.getCaseLevelRiskOldDetails";
	private String deleteRisksAssociatedToSubject = "Subject.deleteRisksAssociatedToSubject";
	private String deleteIsIsRisksAssociatedToSubject = "Subject.deleteIsIsRisksAssociatedToSubject";
	private String ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY = "Subject.associateSubjectLevelRiskHistory";
	private String ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY = "Subject.associateSubjectLevelDeleteRiskHistory";
	private String ASSOCIATE_DELETE_SUBJECT_RISK_HISTORY = "Subject.associateDeleteSubjectRiskHistory";
	private String ASSOCIATE_SUBJECT_LEVEL_RISK_COUNTRY_BREAKDOWN = "Subject.associateSubjectLevelRiskCntryBrkdown";
	private String CRN = "crn";
	private String SUBJECT_ID = "subjectId";
	private String RISK_CODE_LIST = "riskCodeList";
	private String DELETE_OLD_RISKS_ON_SUBJECT_EDIT = "Subject.deleteOldRisksOnSubjectEdit";
	private String getSubjectLevelRiskOldDetailsCntryBrkDown = "Subject.getSubjectLevelRiskOldDetailsCntryBrkDown";
	private String getSubjectLevelRiskMappingWithCntryBrkDown = "Subject.getSubjectLevelRiskMappingWithCntryBrkDown";
	private String getCaseLevelRiskMappingWithCntryBrkDown = "Subject.getCaseLevelRiskMappingWithCntryBrkDown";
	private String getSubjectLevelRiskMappingWithoutCntryBrkDown = "Subject.getSubjectLevelRiskMappingWithoutCntryBrkDown";
	private String getCaseLevelRiskMappingWithoutCntryBrkDown = "Subject.getCaseLevelRiskMappingWithoutCntryBrkDown";
	private String getCaseLevelRiskMappingWithoutCntryBrkDownForDelete = "Subject.getCaseLevelRiskMappingWithoutCntryBrkDownForDelete";
	private String getSubjectLevelCountryForRiskCodeCntryBrkDown = "Subject.getSubjectLevelCountryForRiskCodeCntryBrkDown";
	private String getRiskDetailsNeedToBeDelete = "Subject.getRiskDetailsNeedToBeDelete";
	private String getISISRiskDetailsNeedToBeDelete = "Subject.getISISRiskDetailsNeedToBeDelete";
	private String getCntryBrkDownRiskDetailsNeedToBeDelete = "Subject.getCntryBrkDownRiskDetailsNeedToBeDelete";
	private String getCntryBrkDownISISRiskDetailsNeedToBeDelete = "Subject.getCntryBrkDownISISRiskDetailsNeedToBeDelete";
	private String DELETE_SUBJECT_LEVEL_CNTRY_BRK_DOWN_RISK = "Subject.deleteSubjectLevelCntryBrkDownRisk";
	private String DELETE_SUBJECT_LEVEL_CNTRY_BRK_DOWN_RISK_COUNTRY = "Subject.deleteSubjectLevelCntryBrkDownRiskCountry";
	private String caseDetailsForRiskProfile = "Subject.caseDetailsForRiskProfile";
	private String getSubjectLevelRiskCategory = "Subject.getSubjectLevelRiskCategory";
	private String getSubjectLevelRiskCategoryForResearch = "Subject.getSubjectLevelRiskCategoryForResearch";
	private String INSERT_SUBJECT_ADDITION_RISK_AGGR = "Subject.insertSubjectAdditionRiskAggr";
	private String INSERT_SUBJECT_ADDITION_TOTAL_RISK_AGGR = "Subject.insertSubjectAdditionTotalRiskAggr";
	private String UPDATE_SUBJECT_TOTAL_RISK_AGGR = "Subject.updateSubjectEachSubjectRiskAggr";
	private static final String GET_RISK_CODE_FOR_CLIENT = "Subject.getRiskCode";
	private static final String GET_RISK_CODE_FOR_CLIENT_CNTRY_BRKDOWN = "Subject.getRiskCodeHasCntryBrkDown";
	private static final String INSERT_CASE_ADDITION_TOTAL_RISK_AGGR = "Subject.insertCaseAdditionTotalRiskAggr";
	private static final String UPDATE_SUBJECT_OVERALL_RISK_AGGR = "Subject.updateSubjectOverallRiskAggr";
	private static final String ASSOCIATE_CASE_WITH_RISK = "Subject.associateRiskAtCaseCreation";
	private static final String UPDATE_CASE_LEVEL_RISK = "Subject.updateCaseLevelRisk";
	private static final String UPDATE_SUBJECT_LEVEL_RISK = "Subject.updateSubjectLevelRisk";
	private static final String UPDATE_CASE_LEVEL_RISK_CNTRY_BRK_DOWN = "Subject.updateRiskCntryBrkDownAtSubjectEdit";
	private static final String getCaseLevelCategoryFromProfileForSubject = "Subject.getCaseLevelCategoryFromProfileForSubject";
	private static final String getISISCaseLevelCategoryFromProfileForSubject = "Subject.getISISCaseLevelCategoryFromProfileForSubject";
	private static final String INSERT_CASE_LEVEL_RISK_HISTORY = "Subject.insertCaseLevelRiskHistory";
	private static final String getAlreadyAssociatedRisks = "Subject.getAlreadyAssociatedRisks";
	private static final String getAlreadyAggregatedCategory = "Subject.getAlreadyAggregatedCategory";
	private static final String getSubjectLevelRiskCategoryDetails = "Subject.getSubjectLevelRiskCategoryDetails";
	private static final String getCaseLevelRiskCategoryDetails = "Subject.getCaseLevelRiskCategoryDetails";
	private static final String getSubjectLevelAggragationDetails = "Subject.getSubjectLevelAggragationDetails";
	private static final String getCaseLevelAggragationDetails = "Subject.getCaseLevelAggragationDetails";
	private static final String insertSubjectRiskAggregationOnUpdate = "Subject.insertSubjectRiskAggregationOnUpdate";
	private static final String updateSubjectRiskAggregationOnUpdate = "Subject.updateSubjectRiskAggregationOnUpdate";
	private static final String getAtrributeValuesCaseAggr = "Subject.getAtrributeValuesCaseAggr";
	private static final String getAtrributeValuesSubAggr = "Subject.getAtrributeValuesSubAggr";
	private static final String DELETE_OLD_CASE_RISKS_ON_SUBJECT_EDIT = "Subject.deleteOldCaseRisksOnSubjectEdit";
	private static final String DELETE_OLD_CASE_RISKS_ON_SUBJECT_ADD = "Subject.deleteOldCaseRisksOnSubjectAdd";
	private static final String DELETE_CASE_HAS_COUNTRY_BRK_DOWN = "Subject.deleteCaseHasCntryBrkDown";
	private static final String getSubREs = "Subject.getSubREs";
	private static final String DELETE_CASE_LEVEL_AGGR = "Subject.deleteCaseLevelAggr";
	private static final String DELETE_SUBJECT_LEVEL_AGGR = "Subject.deleteSubLevelCategoryAggr";
	private static final String deleteCategoryWiseRiskAggregationForSubject = "Subject.deleteCategoryWiseRiskAggregationForSubject";
	private static final String deleteEachSubjectRiskAggregation = "Subject.deleteEachSubjectRiskAggregation";
	private static final String deleteISISCategoryWiseRiskAggregationForSubject = "Subject.deleteISISCategoryWiseRiskAggregationForSubject";
	private static final String deleteISISEachSubjectRiskAggregation = "Subject.deleteISISEachSubjectRiskAggregation";
	private static final String getCaseLevelRiskAggregation = "Subject.getCaseLevelRiskAggregation";
	private static final String getSubjectLevelRiskMappingForAggregation = "Subject.getSubjectLevelRiskMappingForAggregation";
	private static final String getTotalAggregationId = "Subject.getTotalAggregationId";
	private static final String getEachSubjectAggregationId = "Subject.getEachSubjectAggregationId";
	private static final String getCountryBrkDownCaseLevelRiskOldDetails = "Subject.getCountryBrkDownCaseLevelRiskOldDetails";
	private static final String getSubREsForSubjectList = "Subject.getSubREsForSubjectList";
	private static final String getSubREstoDelete = "Subject.getSubREstoDelete";
	private static final String getProfileIdList = "Subject.getProfileIdList";
	private static final String getSubREstoDeleteWithoutBrkDown = "Subject.getSubREstoDeleteWithoutBrkDown";
	private static final String getAllCaseLevelRiskMapping = "Subject.getAllCaseLevelRiskMapping";
	private static final String getAllSubREs = "Subject.getAllSubREs";
	private static final String getISISSubREsForSubjectList = "Subject.getISISSubREsForSubjectList";
	private static final String getISISSubREstoDelete = "Subject.getISISSubREstoDelete";
	private static final String getISISSubREstoDeleteWithoutBrkDown = "Subject.getISISSubREstoDeleteWithoutBrkDown";
	private static final String getISISAllSubREs = "Subject.getISISAllSubREs";
	private static final String getCaseLevelRiskMappingWithCntryBrkDownForDelete = "Subject.getCaseLevelRiskMappingWithCntryBrkDownForDelete";
	private static final String getCaseLevelRiskDetailsForDeletedSubjects = "Subject.getCaseLevelRiskDetailsForDeletedSubjects";
	private static final String getCaseLevelRiskDetailsWithoutCntryForDeletedSubjects = "Subject.getCaseLevelRiskDetailsWithoutCntryForDeletedSubjects";
	private static final String getISISCaseLevelRiskDetailsForDeletedSubjects = "Subject.getISISCaseLevelRiskDetailsForDeletedSubjects";
	private static final String getISISCaseLevelRiskDetailsWithoutCntryForDeletedSubjects = "Subject.getISISCaseLevelRiskDetailsWithoutCntryForDeletedSubjects";
	private String caseDtlsForRiskProfile = "Subject.caseDtlsForRiskProfile";
	private String GET_ATTRIBUTE_NAME = "Subject.getAttributeName";
	private static final String BREAKLINE = "<br />";
	private static final String BOLDSTART = "<b>";
	private static final String BOLDEND = "</b>";
	private static final String PERFORMER = "performer";
	private static final String TEAM_TYPE_ID = "teamTypeId";
	private static final String TASK_NAME = "taskName";
	private static final String TEAM_NAME = "teamName";
	private static final String TEAM_ID = "teamId";

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public int deleteSubjectDetail(List subjectIDString) throws CMSException {
		this.logger.debug("Inside deleteSubjectDetail Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectIDString);
		boolean var2 = false;

		try {
			int deleteResultCount = this.delete(this.deleteSubject, subjectIDString);
			return deleteResultCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteRiskDetailsAssociatedToSubjects(List subjectIDString) throws CMSException {
		this.logger.debug("Inside deleteRiskDetailsAssociatedToSubjects Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectIDString);
		int deleteResultCount = false;
		int deleteCategoryAggrCount = false;
		boolean var4 = false;

		try {
			int deleteResultCount = this.delete(this.deleteRisksAssociatedToSubject, subjectIDString);
			if (deleteResultCount > 0) {
				this.delete("Subject.deleteCategoryWiseRiskAggregationForSubject", subjectIDString);
			}

			if (deleteResultCount > 0) {
				this.delete("Subject.deleteEachSubjectRiskAggregation", subjectIDString);
			}

			return deleteResultCount;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public int deleteISISRiskDetailsAssociatedToSubjects(int subjectId) throws CMSException {
		this.logger.debug("Inside deleteRiskDetailsAssociatedToSubjects Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectId);
		int deleteResultCount = false;
		int deleteCategoryAggrCount = false;
		boolean var4 = false;

		try {
			int deleteResultCount = this.delete(this.deleteIsIsRisksAssociatedToSubject, subjectId);
			if (deleteResultCount > 0) {
				this.delete("Subject.deleteISISCategoryWiseRiskAggregationForSubject", subjectId);
			}

			if (deleteResultCount > 0) {
				this.delete("Subject.deleteISISEachSubjectRiskAggregation", subjectId);
			}

			return deleteResultCount;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<DeleteSubjectLevelRiskVO> getRiskDetailsNeedToBeDelete(List subjectIDString) throws CMSException {
		this.logger.debug("Inside getRiskDetailsNeedToBeDelete Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectIDString);
		new ArrayList();

		try {
			List<DeleteSubjectLevelRiskVO> deleteSubjectLevelRiskVOList = this
					.queryForList(this.getRiskDetailsNeedToBeDelete, subjectIDString);
			return deleteSubjectLevelRiskVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<DeleteSubjectLevelRiskVO> getISISRiskDetailsNeedToBeDelete(int subjectId) throws CMSException {
		this.logger.debug("Inside getISISRiskDetailsNeedToBeDelete Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectId);
		new ArrayList();

		try {
			List<DeleteSubjectLevelRiskVO> deleteSubjectLevelRiskVOList = this
					.queryForList(this.getISISRiskDetailsNeedToBeDelete, subjectId);
			return deleteSubjectLevelRiskVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<DeleteSubjectLevelRiskVO> getCntryBrkDownRiskDetailsNeedToBeDelete(List subjectIDString)
			throws CMSException {
		this.logger.debug("Inside getCntryBrkDownRiskDetailsNeedToBeDelete Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectIDString);
		new ArrayList();

		try {
			List<DeleteSubjectLevelRiskVO> deleteSubjectLevelRiskVOList = this
					.queryForList(this.getCntryBrkDownRiskDetailsNeedToBeDelete, subjectIDString);
			return deleteSubjectLevelRiskVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<DeleteSubjectLevelRiskVO> getCntryBrkDownISISRiskDetailsNeedToBeDelete(int subjectId)
			throws CMSException {
		this.logger.debug("Inside getCntryBrkDownISISRiskDetailsNeedToBeDelete Method of SubjectDAO class");
		this.logger.debug("subjectIDString is::::" + subjectId);
		new ArrayList();

		try {
			List<DeleteSubjectLevelRiskVO> deleteSubjectLevelRiskVOList = this
					.queryForList(this.getCntryBrkDownISISRiskDetailsNeedToBeDelete, subjectId);
			return deleteSubjectLevelRiskVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public CaseDetails getSubjectListForCRN(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getSubjectListForCRN Method of SubjectDAO class");
		new CaseDetails();
		this.logger.debug("crn is " + crn);
		Map caseMap = new HashMap();
		caseMap.put("crn", crn);
		caseMap.put("sort", sortColumnName);
		caseMap.put("start", new Integer(start + 1));
		caseMap.put("limit", new Integer(start + limit));
		caseMap.put("dir", sortType);
		this.logger.debug("caseMap is::::::::" + caseMap);

		try {
			CaseDetails caseDetails = (CaseDetails) this.queryForObject(this.caseSubjectDetails, caseMap);
			if (caseDetails == null) {
				List<SubjectDetails> subjectList = new ArrayList();
				this.logger.debug("inside if block");
				caseDetails = new CaseDetails();
				caseDetails.setSubjectList(subjectList);
			}

			return caseDetails;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public CaseDetails getSubjectListForCRN(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectListForCRN Method of SubjectDAO class");
		new CaseDetails();
		this.logger.debug("crn is " + crn);
		Map caseMap = new HashMap();
		caseMap.put("crn", crn);
		this.logger.debug("caseMap is::::::::" + caseMap);

		try {
			CaseDetails caseDetails = (CaseDetails) this.queryForObject(this.crnDetailWithoutPagination, caseMap);
			if (caseDetails == null) {
				List<SubjectDetails> subjectList = new ArrayList();
				this.logger.debug("inside if block");
				caseDetails = new CaseDetails();
				caseDetails.setSubjectList(subjectList);
			}

			return caseDetails;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getMatchingAssociateCasesForSub(String SubName, String subjectID, String crn, String sortColumnName,
			int start, int limit, String sortType) throws CMSException {
		this.logger.debug("Inside getMatchingAssociateCasesForSub Method of SubjectDAO class");
		List subList = null;
		HashMap map = new HashMap();

		try {
			map.put("subjectID", new Integer(subjectID));
			map.put("subjectName", SubName);
			map.put("crn", crn);
			map.put("sort", sortColumnName);
			map.put("start", start + 1);
			map.put("limit", start + limit);
			map.put("dir", sortType);
			this.logger.debug("map is--:::::" + map);
			subList = this.queryForList(this.matchingSubject, map);
			this.logger.debug("subList size is:::::" + subList.size());
			return subList;
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public List getMatchingAssociateCasesForAddSub(String SubName, String subjectID, String crn) throws CMSException {
		this.logger.debug("Inside getMatchingAssociateCasesForSub Method of SubjectDAO class");
		Map map = new HashMap();
		map.put("subjectID", new Integer(subjectID));
		map.put("subjectName", SubName);
		map.put("crn", crn);
		this.logger.debug("map is:::::" + map);
		List subList = null;

		try {
			subList = this.queryForList(this.matchingSubjectListForAddSubject, map);
			return subList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List getAssociatedCasesForSubject(String subId, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getAssociatedCasesForSubject Method of SubjectDAO class");
		this.logger.debug("subId is::::" + subId);
		new ArrayList();
		Map associateCaseMap = new HashMap();
		associateCaseMap.put("subjectID", subId);
		associateCaseMap.put("sort", sortColumnName);
		associateCaseMap.put("start", new Integer(start + 1));
		associateCaseMap.put("limit", new Integer(start + limit));
		associateCaseMap.put("dir", sortType);
		this.logger.debug("associateCaseMap is::::" + associateCaseMap);

		try {
			List<CaseDetails> caseList = this.queryForList(this.associateCases, associateCaseMap);
			this.logger.debug("caseList size is:::" + caseList.size());
			return caseList;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public int addAssociateCaseForSubject(String subID, List crn, String currentUser, List isLegacyList)
			throws CMSException {
		this.logger.debug("Inside addAssociateCaseForSubject Method of SubjectDAO class");
		this.logger.debug("crn is:::::" + crn);
		this.logger.debug("subID is:::::" + subID);
		this.logger.debug("isLegacyList is:::::" + isLegacyList);
		int totalrecordInserted = 0;

		try {
			Iterator it1 = crn.iterator();
			Iterator it2 = isLegacyList.iterator();

			while (it1.hasNext() && it2.hasNext()) {
				++totalrecordInserted;
				Map map = new HashMap();
				map.put("crn", (String) it1.next());
				map.put("isLegacy", new Integer((String) it2.next()));
				map.put("subjectID", new Integer(subID));
				map.put("currentUser", currentUser);
				this.insert(this.insertAssociateCase, map);
			}
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		this.logger.debug("totalrecordInserted is" + totalrecordInserted);
		return totalrecordInserted;
	}

	public int addColorForSubject(SubjectColorVO colorVO) throws CMSException {
		try {
			return (Integer) this.insert(this.insertSubjectColor, colorVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public Map getSubjectIndForSubjects(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectIndForSubjects Method of SubjectDAO class");
		this.logger.debug("crn is:::::" + crn);
		HashMap subIndMap = new HashMap();

		try {
			List<String> subIdList = this.getSubjectListForCase(crn);
			Iterator itr = subIdList.iterator();

			while (itr.hasNext()) {
				String subId = (String) itr.next();
				String subInd = (String) this.queryForObject(this.industryCodesForSubjects, subId);
				subIndMap.put(subId, subInd);
			}

			return subIndMap;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List getSubjectListForCase(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectListForCase Method of SubjectDAO class");
		this.logger.debug("crn is:::::" + crn);
		new ArrayList();

		try {
			List subIdList = this.queryForList(this.subjectIDList, crn);
			return subIdList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateSubjectIndForSubj(Map subIndMap) throws CMSException {
		this.logger.debug("Inside updateSubjectIndForSubj Method of SubjectDAO class");
		this.logger.debug("subIndMap is::::" + subIndMap);
		boolean var2 = false;

		try {
			int updateSubjectIndustryCount = this.update(this.subjectIndustryUpdate, subIndMap);
			return updateSubjectIndustryCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateSubjectRiskListForSub(List SubjectRiskMapList, String subjectID) throws CMSException {
		this.logger.debug("Inside updateSubjectRiskListForSub Method of SubjectDAO class");
		this.logger.debug("subjectID is::::" + subjectID);
		int totalUpdatedSubjectRiskCount = 0;

		try {
			boolean totalUpdatedSubjectRiskCount;
			try {
				this.getSqlMapClient().startTransaction();
				this.delete(this.deleteSubjectRisk, subjectID);

				for (Iterator iterator = SubjectRiskMapList.iterator(); iterator
						.hasNext(); ++totalUpdatedSubjectRiskCount) {
					Map subRiskMap = (Map) iterator.next();
					this.logger.debug("subRiskMap is::::" + subRiskMap);
					this.insert(this.insertSubjectRisk, subRiskMap);
					this.getSqlMapClient().commitTransaction();
				}
			} catch (SQLException var14) {
				totalUpdatedSubjectRiskCount = false;
				throw new CMSException(this.logger, var14);
			} catch (Exception var15) {
				totalUpdatedSubjectRiskCount = false;
				throw new CMSException(this.logger, var15);
			}
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var13) {
				throw new CMSException(this.logger, var13);
			}
		}

		this.logger.debug("total updtaed risks for subjectID::" + subjectID + ":::is:" + totalUpdatedSubjectRiskCount);
		return totalUpdatedSubjectRiskCount;
	}

	public String getReListForSub(String subjectId) throws CMSException {
		this.logger.debug("Inside getReListForSub Method of SubjectDAO class");
		this.logger.debug("subjectId is::::" + subjectId);
		String researchElement = "";

		try {
			researchElement = (String) this.queryForObject(this.reList, subjectId);
			return researchElement;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getReForReportType(String crn) throws CMSException {
		this.logger.debug("Inside getReForReportType Method of SubjectDAO class");
		this.logger.debug("crn is::::::" + crn);
		new ArrayList();

		List reList;
		try {
			reList = this.queryForList(this.resForReportType, crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Re list size for report type is::::" + reList.size());
		return reList;
	}

	public CaseDetails getCaseDetail(String crn) throws CMSException {
		this.logger.debug("Inside getSubReportTypeIdForCase Method of SubjectDAO class");
		CaseDetails caseDetails = (CaseDetails) this.queryForObject(this.getCaseReport, crn);
		this.logger.debug("sub report type is is:::" + caseDetails.getSubReportTypeId());
		return caseDetails;
	}

	public List getReForSubReportType(int subReportTypeId) throws CMSException {
		this.logger.debug("Inside getReForSubReportType Method of SubjectDAO class");
		this.logger.debug("subReportTypeId is::::::" + subReportTypeId);
		new ArrayList();

		List reList;
		try {
			reList = this.queryForList(this.resForSubReportType, new Integer(subReportTypeId));
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Re list size for sub report type is::::" + reList.size());
		return reList;
	}

	public SubjectDetails getSubjectDetailsForSubID(int subId) throws CMSException {
		this.logger.debug("Inside getSubjectDetailsForSubID Method of SubjectDAO class");
		this.logger.debug("subId is:::::" + subId);
		new SubjectDetails();

		try {
			SubjectDetails subDet = (SubjectDetails) this.queryForObject(this.subjectDetail, new Integer(subId));
			return subDet;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Map getReMap() throws CMSException {
		this.logger.debug("Inside getReMap Method of SubjectDAO class");
		new ArrayList();
		HashMap reMap = new HashMap();

		try {
			List reList = this.queryForList(this.subjectReMap);
			Iterator iterator = reList.iterator();

			while (iterator.hasNext()) {
				ResearchElementMasterVO object = (ResearchElementMasterVO) iterator.next();
				reMap.put(object.getResearchElementcode(), object.getResearchElementName());
			}

			return reMap;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public Map getReBiTeamMap() throws CMSException {
		this.logger.debug("Inside getReBiTeamMap Method of SubjectDAO class");
		new ArrayList();
		HashMap reMap = new HashMap();

		try {
			List reList = this.queryForList("Subject.reBITeamMap");
			Iterator iterator = reList.iterator();

			while (iterator.hasNext()) {
				ResearchElementMasterVO object = (ResearchElementMasterVO) iterator.next();
				reMap.put(object.getResearchElementcode(), object.getBiTeam());
			}

			return reMap;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getSubjectValidationForTeam(String crn, String subjectID, String subjectName) throws CMSException {
		this.logger.debug("Inside getSubjectValidationForTeam Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList(this.subjectTeamValidation, map);
			return teamList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List getSubjectValidationForBITeam(String crn, String subjectID, String subjectName) throws CMSException {
		this.logger.debug("Inside getSubjectValidationForBITeam Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("Subject.subjectValidationForBI", map);
			return teamList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public Integer getSubjectValidationForPrimarySub(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectValidationForPrimarySub Method of SubjectDAO class");
		this.logger.debug("crn is::::::" + crn);
		new Integer(0);

		try {
			Integer subjectId = (Integer) this.queryForObject(this.primarySubjectOfCase, crn);
			return subjectId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteAssociateCRNForSub(String crn, String subjectID) throws CMSException {
		this.logger.debug("Inside deleteAssociateCRNForSub Method of SubjectDAO class");
		boolean var3 = false;

		try {
			Map map = new HashMap();
			map.put("subjectID", new Integer(subjectID));
			map.put("crn", crn);
			this.logger.debug("map is::::::" + map);
			int deleteAssociateCRNCount = this.delete(this.deleteAssociateCase, map);
			return deleteAssociateCRNCount;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getREs(String entityType) throws CMSException {
		this.logger.debug("Inside getREs Method of SubjectDAO class");
		this.logger.debug("Entity type::::::" + entityType);
		new ArrayList();

		try {
			List entityREList = this.queryForList(this.reForEntityType, entityType);
			return entityREList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getSubjectRisk(int subject_ID) throws CMSException {
		this.logger.debug("Inside getRiskMaster Method of SubjectDAO class");
		new ArrayList();

		try {
			List riskList = this.queryForList(this.riskForSubject, new Integer(subject_ID));
			this.logger.debug(subject_ID + "riskList size is " + riskList.size());
			return riskList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int saveUpdatedSubject(SubjectDetails subjectDetail) throws CMSException {
		this.logger.debug("Inside saveUpdatedSubject Method of SubjectDAO class");
		this.logger.debug("crn is::::::" + subjectDetail.getCrn());
		subjectDetail.setReportSubjectName(subjectDetail.getSubjectName());
		int updateSubjectCount = false;
		int updateSubjectCount;
		if (subjectDetail.isPrimarySub()) {
			this.logger.debug("new subject is added as primary subject..");

			try {
				this.getSqlMapClient().startTransaction();
				this.update(this.updatePrimarySubject, subjectDetail);
				this.update(this.updatePrimarySubjectJuno, subjectDetail.getCrn());
				updateSubjectCount = this.update(this.updateSubject, subjectDetail);
				this.update(this.updateSubjectJuno, subjectDetail);
				this.getSqlMapClient().commitTransaction();
			} catch (SQLException var16) {
				throw new CMSException(this.logger, var16);
			} catch (Exception var17) {
				throw new CMSException(this.logger, var17);
			} finally {
				try {
					this.getSqlMapClient().endTransaction();
				} catch (SQLException var13) {
					throw new CMSException(this.logger, var13);
				}
			}
		} else {
			try {
				updateSubjectCount = this.update(this.updateSubject, subjectDetail);
				this.update(this.updateSubjectJuno, subjectDetail);
			} catch (DataAccessException var14) {
				throw new CMSException(this.logger, var14);
			} catch (Exception var15) {
				throw new CMSException(this.logger, var15);
			}
		}

		return updateSubjectCount;
	}

	public List getCaseSubjetcIndustry(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getCaseSubjetcIndustry Method of SubjectDAO class");
		this.logger.debug("getCaseSubjetcIndustry..." + crn);
		this.logger.debug("crn is:::" + crn);
		new ArrayList();
		Map industryMap = new HashMap();
		industryMap.put("crn", crn);
		industryMap.put("entity", Integer.parseInt("2"));
		industryMap.put("sort", sortColumnName);
		industryMap.put("start", new Integer(start + 1));
		industryMap.put("limit", new Integer(start + limit));
		industryMap.put("dir", sortType);
		this.logger.debug("industryMap is:::::" + industryMap);

		try {
			List industryList = this.queryForList(this.getCaseSubjectIndustry, industryMap);
			return industryList;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public SubjectDetails getPrimarySubjectDetail(String crn) throws CMSException {
		this.logger.debug("Inside getPrimasrySubjectDetail Method of SubjectDAO class");
		this.logger.debug("crn is:::" + crn);
		new SubjectDetails();

		try {
			SubjectDetails subjectDetails = (SubjectDetails) this.queryForObject(this.primasrySubjectDetails, crn);
			return subjectDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int saveNewSubject(SubjectDetails subjectDetail, List associateCasesList, List isLegacyList)
			throws CMSException {
		this.logger.debug("Inside saveNewSubject Method of SubjectDAO class");

		try {
			this.getSqlMapClient().startTransaction();
			if (subjectDetail.isPrimarySub()) {
				this.update(this.updatePrimarySubject, subjectDetail);
				this.update(this.updatePrimarySubjectJuno, subjectDetail.getCrn());
			}

			this.logger.debug("crn for add new subject" + subjectDetail.getCrn());
			this.insert(this.insertSubject, subjectDetail);
			this.insert(this.insertSubjectJuno, subjectDetail);
			if (associateCasesList.size() > 0) {
				Iterator it1 = associateCasesList.iterator();
				Iterator it2 = isLegacyList.iterator();

				while (it1.hasNext() && it2.hasNext()) {
					Map map = new HashMap();
					map.put("crn", (String) it1.next());
					map.put("isLegacy", new Integer((String) it2.next()));
					map.put("subjectID", subjectDetail.getSubjectId());
					map.put("currentUser", subjectDetail.getUpdatedBy());
					this.logger.debug("map is:::::" + map);
					this.insert(this.insertAssociateCase, map);
				}
			}

			this.getSqlMapClient().commitTransaction();
		} catch (SQLException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var14) {
				throw new CMSException(this.logger, var14);
			}
		}

		this.logger.debug("newly added subject id is::::" + subjectDetail.getSubjectId());
		return subjectDetail.getSubjectId();
	}

	public String getCaseManager(String crn) throws CMSException {
		this.logger.debug("Inside getCaseManager Method of SubjectDAO class");
		this.logger.debug("crn is:::" + crn);
		String caseManager = "";

		try {
			caseManager = (String) this.queryForObject(this.getCaseManager, crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("caseManager is:::" + caseManager);
		return caseManager;
	}

	public List getAnalystForSubjectNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getAnalystForSubjectNotification Method of SubjectDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List analystList;
		try {
			analystList = this.queryForList(this.getAnalyst, crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("analystList size is:::" + analystList.size());
		return analystList;
	}

	public List getReviewersForSubjectNotification(String crn) throws CMSException {
		this.logger.debug("Inside getReviewersForSubjectNotification Method of SubjectDAO class");
		new ArrayList();

		List reviewersList;
		try {
			reviewersList = this.queryForList("Subject.getReviewers", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("reviewer1List size is:::" + reviewersList.size());
		return reviewersList;
	}

	public List getReviewer1ForSubjectNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getReviewer1ForSubjectNotification Method of SubjectDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List reviewer1List;
		try {
			reviewer1List = this.queryForList(this.getReviewer1, crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("reviewer1List size is:::" + reviewer1List.size());
		return reviewer1List;
	}

	public List getReviewer2ForSubjectNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getReviewer2ForSubjectNotification Method of SubjectDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List reviewer2List;
		try {
			reviewer2List = this.queryForList(this.getReviewer2, crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("reviewer2List size is:::" + reviewer2List.size());
		return reviewer2List;
	}

	public List getReviewer3ForSubjectNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getReviewer3ForSubjectNotification Method of SubjectDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List reviewer3List;
		try {
			reviewer3List = this.queryForList(this.getReviewer3, crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("reviewer3List size is:::" + reviewer3List.size());
		return reviewer3List;
	}

	public List getManagerForSubjectNotification(String crn, String teamString) throws CMSException {
		this.logger.debug("Inside getManagerForSubjectNotification Method of SubjectDAO class");
		new ArrayList();
		Map crnMap = new HashMap();
		crnMap.put("crn", crn);
		crnMap.put("team_ID", StringUtils.commaSeparatedStringToList(teamString));
		this.logger.debug("crnMap is:::" + crnMap);

		List managerList;
		try {
			managerList = this.queryForList(this.getManager, crnMap);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("managerList size is:::" + managerList.size());
		return managerList;
	}

	public String getReIdsForSubject(int subejctID) throws CMSException {
		this.logger.debug("Inside getReIdsForSubject Method of SubjectDAO class");
		String reIds = "";

		try {
			reIds = (String) this.queryForObject(this.getResForsubject, new Integer(subejctID));
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("reIds is:::" + reIds);
		return reIds;
	}

	public List getTopTenCountry() throws CMSException {
		this.logger.debug("Inside getTopTenCountry Method of SubjectDAO class");
		new ArrayList();

		List topTenCountries;
		try {
			topTenCountries = this.queryForList(this.getTopTenCountries);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("topTenCountries size is:::" + topTenCountries.size());
		return topTenCountries;
	}

	public int getSubjectCount(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectCount Method of SubjectDAO class");
		boolean var2 = false;

		int subjectCount;
		try {
			subjectCount = (Integer) this.queryForObject(this.getSubjectCountForCase, crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("subjectCount is:::" + subjectCount);
		return subjectCount;
	}

	public int getSubjectIdCount(String subjectId) throws CMSException {
		this.logger.debug("Inside getSubjectIdCount Method of SubjectDAO class");
		boolean var2 = false;

		try {
			int subjectIdCount = (Integer) this.queryForObject(this.getSubjectIdCountForCase, subjectId);
			this.logger.debug("subjectIdCount is:::" + subjectIdCount);
			return subjectIdCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCRNCount(String crn) throws CMSException {
		this.logger.debug("Inside getCRNCount Method of SubjectDAO class");
		boolean var2 = false;

		try {
			int CRNCount = (Integer) this.queryForObject(this.getCRNCount, crn);
			this.logger.debug("getCRNCount is:::" + CRNCount);
			return CRNCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getDisAssociatedSubjectCount(int subjectId) throws CMSException {
		this.logger.debug("Inside getDisAssociatedSubjectCount Method of SubjectDAO class");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.getDisAssociatedSubjectCount, subjectId);
			this.logger.debug("DisAssociatedSubjectCount is:::" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getAssociatedSubjectCount(String subjectName, int subjectID, String crn) throws CMSException {
		this.logger.debug("Inside getAssociatedSubjectCount Method of SubjectDAO class");
		int count = false;
		Map map = new HashMap();
		map.put("subjectID", subjectID);
		map.put("subjectName", subjectName);
		map.put("crn", crn);
		this.logger.debug("map is:::" + map);

		try {
			int count = (Integer) this.queryForObject(this.getAssociatedSubjectCount, map);
			this.logger.debug("AssociatedSubjectCount is:::" + count);
			return count;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int getSubjectIndustryCount(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectIndustryCount Method of SubjectDAO class");
		int subjectIndustryCount = false;
		Map industryMap = new HashMap();
		industryMap.put("crn", crn);
		industryMap.put("entity", Integer.parseInt("2"));
		this.logger.debug("industryMap is:::" + industryMap);

		int subjectIndustryCount;
		try {
			subjectIndustryCount = (Integer) this.queryForObject(this.getSubjectIndustryCount, industryMap);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("subjectIndustryCount is:::" + subjectIndustryCount);
		return subjectIndustryCount;
	}

	public int getMatchingAssociateCasesCount(String subjectName, String subjectID, String crn) throws CMSException {
		this.logger.debug("Inside getMatchingAssociateCasesCount Method of SubjectDAO class");
		int matchingAssociateCasesCount = false;
		Map map = new HashMap();
		map.put("subjectID", new Integer(subjectID));
		map.put("subjectName", subjectName);
		map.put("crn", crn);
		this.logger.debug("map is:::" + map);

		int matchingAssociateCasesCount;
		try {
			matchingAssociateCasesCount = (Integer) this.queryForObject(this.getMatchingAssociateCasesCount, map);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("matchingAssociateCasesCount is:::" + matchingAssociateCasesCount);
		return matchingAssociateCasesCount;
	}

	public int getAsscciateCaseCount(String subjectID) throws CMSException {
		this.logger.debug("Inside getAsscciateCaseCount Method of SubjectDAO class");
		boolean var2 = false;

		int asscciateCaseCount;
		try {
			asscciateCaseCount = (Integer) this.queryForObject(this.getAsscciateCaseCount, new Integer(subjectID));
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("asscciateCaseCount is:::" + asscciateCaseCount);
		return asscciateCaseCount;
	}

	public int getAddEditSubjectValidation(String crn, String subjectName, String countryName, String subjectID,
			String entityType) throws CMSException {
		this.logger.debug("Inside getAddEditSubjectValidation Method of SubjectDAO class");
		Map validationMap = new HashMap();
		validationMap.put("crn", crn);
		validationMap.put("subjectName", subjectName.trim());
		validationMap.put("countryName", countryName.trim());
		validationMap.put("subjectID", new Integer(subjectID));
		validationMap.put("entityType", new Integer(entityType));
		this.logger.debug("validationMap is::::" + validationMap);

		int validationCount;
		try {
			validationCount = (Integer) this.queryForObject(this.addEditSubjectValidation, validationMap);
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		this.logger.debug("validationCount is::::" + validationCount);
		return validationCount;
	}

	public List getReportTypeLIst() throws CMSException {
		this.logger.debug("Inside getReportTypeLIst Method of SubjectDAO class");
		new ArrayList();

		try {
			List repClinetList = this.queryForList(this.getReportType);
			return repClinetList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getClientCodesForReportType(int reportTypeId) throws CMSException {
		this.logger.debug("Inside getClientCodesForReportType Method of SubjectDAO class");
		String clientCodes = "";

		try {
			clientCodes = (String) this.queryForObject(this.getClientCodes, reportTypeId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("clientCodes is:::" + clientCodes);
		return clientCodes;
	}

	public int getCountryRiskLevelForClientCountry(String clientCode) throws CMSException {
		this.logger.debug("Inside getCountryRiskLevelForClientCountry Method of SubjectDAO class");
		boolean var2 = false;

		int riskLevel;
		try {
			riskLevel = (Integer) this.queryForObject(this.getRiskLevelForClientCountry, clientCode);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("riskLevel is:::" + riskLevel);
		return riskLevel;
	}

	public List getResForSpecficReportType(int reportTypeId, int primaryCode, int riskLevel, String subjectEntity)
			throws CMSException {
		this.logger.debug("Inside getResForSpecficReportType Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("reportTypeId", reportTypeId);
			map.put("primaryCode", primaryCode);
			map.put("riskLevel", riskLevel);
			map.put("entityType", Integer.parseInt(subjectEntity));
			map.put("isSpecific", 1);
			this.logger.debug("map is::" + map);
			List reList = this.queryForList(this.getResForSpecificReportType, map);
			return reList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List getResForSpecficSubReportType(int subReportTypeId, int primaryCode, int riskLevel, String subjectEntity)
			throws CMSException {
		this.logger.debug("Inside getResForSpecficSubReportType Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("subReportTypeId", subReportTypeId);
			map.put("primaryCode", primaryCode);
			map.put("riskLevel", riskLevel);
			map.put("entityType", Integer.parseInt(subjectEntity));
			map.put("isSpecific", 1);
			this.logger.debug("map is::" + map);
			List reList = this.queryForList(this.getResForSpecificSubReportType, map);
			return reList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public long getPIIDForCRN(String crn) throws CMSException {
		long piid = 0L;

		try {
			piid = (Long) this.queryForObject(this.getPIIDForCRN, crn);
			return piid;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getFCPAReportTypeList(String subjectEntity) throws CMSException {
		this.logger.debug("Inside getFCPAReportTypeList Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("entityType", Integer.parseInt(subjectEntity));
			map.put("isSpecific", 0);
			this.logger.debug("map is::" + map);
			List reportType = this.queryForList("Subject.getReportTypeForFCPA", map);
			return reportType;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getResForFCPAReportType(int reportTypeId, int primaryCode, String subjectEntity) throws CMSException {
		this.logger.debug("Inside getResForFCPAReportType Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("reportTypeId", reportTypeId);
			map.put("primaryCode", primaryCode);
			map.put("entityType", Integer.parseInt(subjectEntity));
			map.put("isSpecific", 0);
			this.logger.debug("map is::" + map);
			List reList = this.queryForList("Subject.getReIdsForFCPAReportType", map);
			return reList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List getResForFCPASubReportType(int subReportTypeId, int primaryCode, String subjectEntity)
			throws CMSException {
		this.logger.debug("Inside getResForFCPASubReportType Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("subReportTypeId", subReportTypeId);
			map.put("primaryCode", primaryCode);
			map.put("entityType", Integer.parseInt(subjectEntity));
			map.put("isSpecific", 0);
			this.logger.debug("map is::" + map);
			List reList = this.queryForList("Subject.getReIdsForFCPASubReportType", map);
			return reList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List getUpdateSubjectREValidationForTeam(String crn, String subjectID, String reRemovedListString)
			throws CMSException {
		this.logger.debug("Inside getUpdateSubjectREValidationForTeam Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			map.put("re_Ids", reRemovedListString);
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("Subject.updateSubjectREValidation", map);
			return teamList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List getUpdateSubjectBIREValidationForTeam(String crn, String subjectID, String reRemovedListString)
			throws CMSException {
		this.logger.debug("Inside getUpdateSubjectBIREValidationForTeam Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectID", new Integer(subjectID));
			map.put("re_Ids", reRemovedListString);
			this.logger.debug("map is::::::" + map);
			List teamList = this.queryForList("Subject.updateSubjectBIREValidation", map);
			return teamList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public int addISISSubjectToCase(SubjectDetailsVO subjectDetailsVO) throws CMSException {
		this.logger.debug("Inside addISISSubjectToCase Method of SubjectDAO class");
		Integer subject_ID = null;

		try {
			subjectDetailsVO.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
			if (subjectDetailsVO.isPrimarySubject()) {
				this.update("Subject.updateISISPrimarySubject", subjectDetailsVO);
				this.update(this.updatePrimarySubjectJuno, subjectDetailsVO.getCrn());
			}

			this.logger.debug("crn for add new subject" + subjectDetailsVO.getCrn());
			subject_ID = (Integer) this.insert("Subject.insertISISSubject", subjectDetailsVO);
			this.insert(this.insertISISSubjectJuno, subjectDetailsVO);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return subject_ID;
	}

	public int updateISISSubjectToCase(SubjectDetailsVO subjectDetailVO) throws CMSException {
		this.logger.debug("Inside updateISISSubjectToCase Method of SubjectDAO class");
		this.logger.debug("crn is::::::" + subjectDetailVO.getCrn());
		subjectDetailVO.setUpdatedBy(this.webServicePropertyReaderUtil.getWebServiceCaseCreatorId());
		int updateSubjectCount = false;
		int updateSubjectCount;
		if (subjectDetailVO.isPrimarySubject()) {
			this.logger.debug("new subject is added as primary subject..");

			try {
				this.getSqlMapClient().startTransaction();
				this.update("Subject.updateISISPrimarySubject", subjectDetailVO);
				this.update(this.updatePrimarySubjectJuno, subjectDetailVO.getCrn());
				updateSubjectCount = this.update("Subject.updateISISSubject", subjectDetailVO);
				this.update(this.updateISISSubjectJuno, subjectDetailVO);
				this.getSqlMapClient().commitTransaction();
			} catch (SQLException var18) {
				throw new CMSException(this.logger, var18);
			} catch (Exception var19) {
				throw new CMSException(this.logger, var19);
			} finally {
				try {
					this.getSqlMapClient().endTransaction();
				} catch (SQLException var14) {
					throw new CMSException(this.logger, var14);
				}
			}
		} else {
			try {
				updateSubjectCount = this.update("Subject.updateISISSubject", subjectDetailVO);

				try {
					this.update(this.updateISISSubjectJuno, subjectDetailVO);
				} catch (Exception var15) {
					this.logger.error(var15);
				}
			} catch (DataAccessException var16) {
				throw new CMSException(this.logger, var16);
			} catch (Exception var17) {
				throw new CMSException(this.logger, var17);
			}
		}

		return updateSubjectCount;
	}

	public int deleteISISSubjectToCase(String isisSubjectId) throws CMSException {
		this.logger.debug("Inside deleteISISSubjectToCase Method of SubjectDAO class");
		this.logger.debug("isisSubjectId is::::" + isisSubjectId);
		boolean var2 = false;

		try {
			int deleteResultCount = this.delete("Subject.deleteISISSubject", isisSubjectId);
			return deleteResultCount;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getCountryFromCode(String countryCode) {
		this.logger.debug("Inside getCountryFromCode Method of SubjectDAO class");
		String countryName = (String) this.queryForObject("Subject.getCountryFromCode", countryCode);
		this.logger.debug("county Name is:::::::::" + countryName);
		return countryName;
	}

	public List getLegacyCaseSubjectListData(String crn, String sortColumnName, int start, int limit, String sortType)
			throws CMSException {
		this.logger.debug("Inside getLegacyCaseSubjectListData Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("sort", sortColumnName);
			map.put("start", new Integer(start + 1));
			map.put("limit", new Integer(start + limit));
			map.put("dir", sortType);
			this.logger.debug("map is::::::::::::" + map);
			List<SubjectDetails> subjectDetailsList = this.queryForList("Subject.getLegacyCaseSubjectList", map);
			return subjectDetailsList;
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List getLegacySubjectResData(String crn) throws CMSException {
		this.logger.debug("Inside getLegacySubjectResData Method of SubjectDAO class");
		new ArrayList();

		try {
			List<SubjectDetails> subjectDetailsList = this.queryForList("Subject.getLegacySubjectRE", crn);
			return subjectDetailsList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getLegacyCaseSubjectCount(String crn) throws CMSException {
		this.logger.debug("Inside getLegacyCaseSubjectCount Method of SubjectDAO class");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("Subject.getLegacyCaseSubjectListCount", crn);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getLegacyPastRecordsCount(String crn, String subjectId) throws CMSException {
		this.logger.debug("Inside getLegacyPastRecordsCount Method of SubjectDAO class");
		boolean var3 = false;

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectId", subjectId);
			int count = (Integer) this.queryForObject("Subject.getLegacyPastRecordsCount", map);
			return count;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getLegacyPastRecords(String crn, String subjectId, String sortColumnName, int start, int limit,
			String sortType) throws CMSException {
		this.logger.debug("Inside getLegacyPastRecords Method of SubjectDAO class");
		new ArrayList();

		try {
			Map map = new HashMap();
			map.put("crn", crn);
			map.put("subjectId", subjectId);
			map.put("sort", sortColumnName);
			map.put("start", new Integer(start + 1));
			map.put("limit", new Integer(start + limit));
			map.put("dir", sortType);
			this.logger.debug("map is::::::::::::" + map);
			List<SubjectDetails> subjectDetailsList = this.queryForList("Subject.getLegacyPastRecords", map);
			return subjectDetailsList;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List getCaseLevelRiskCategory(String crn) throws CMSException {
		this.logger.debug("Inside getCaseLevelRiskCategory Method of SubjectDAO class");
		new ArrayList();

		try {
			new HashMap();
			this.logger.debug("CRN is::::::::::::" + crn);
			List caseDetailsList = this.queryForList("Subject.getCaseLevelRiskCategory", crn);
			return caseDetailsList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getSubjectLevelRiskCategory(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectLevelRiskCategory Method of SubjectDAO class");
		new ArrayList();

		try {
			new HashMap();
			this.logger.debug("CRN is::::::::::::" + crn);
			List subjectDetailsList = this.queryForList(this.getSubjectLevelRiskCategory, crn);
			return subjectDetailsList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getSubjectLevelRiskCategory(String crn, String performer, String teamTypeId, String taskName,
			String teamName, String teamId) throws CMSException {
		this.logger.debug("Inside getSubjectLevelRiskCategory For Research Method of SubjectDAO class");
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("performer", performer);
		mapForParams.put("teamTypeId", teamTypeId);
		mapForParams.put("taskName", taskName);
		mapForParams.put("teamName", teamName);
		mapForParams.put("teamId", teamId);
		new ArrayList();

		try {
			new HashMap();
			this.logger.debug("CRN is::::::::::::" + crn);
			List subjectDetailsList = this.queryForList(this.getSubjectLevelRiskCategoryForResearch, mapForParams);
			return subjectDetailsList;
		} catch (DataAccessException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public Date getUpdateOnForSubject(int subjectId) throws CMSException {
		this.logger.debug("Inside getUpdateOnForSubject Method of SubjectDAO class");
		Date date = null;

		try {
			date = (Date) this.queryForObject("Subject.getUpdateOnForSubject", subjectId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("date is:::::::::" + date);
		return date;
	}

	public int getISISCaseFlag(String crn) throws CMSException {
		this.logger.debug("Inside getUpdateOnForSubject Method of SubjectDAO class");
		boolean var2 = false;

		try {
			int isisCaseFlag = (Integer) this.queryForObject("Subject.getISISCase", crn);
			return isisCaseFlag;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public SubjectDetails getISISSubjectDetails(String isisSubjectId) throws CMSException {
		this.logger.debug("Inside getISISSubjectDetails Method of SubjectDAO class");
		this.logger.debug("isisSubjectId is:::::" + isisSubjectId);
		new SubjectDetails();

		try {
			SubjectDetails subjectDetails = (SubjectDetails) this.queryForObject("Subject.isisSubjectDetail",
					isisSubjectId);
			return subjectDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getSubjectsIndustryForISIS(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectsIndustryForISIS Method of SubjectDAO class");
		new ArrayList();

		try {
			List<SubjectDetails> industryList = this.queryForList("Subject.getSubjectsIndustryForISIS", crn);
			return industryList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getSubjectsRisksForISIS(String crn) throws CMSException {
		this.logger.debug("Inside getSubjectsRisksForISIS Method of SubjectDAO class");
		new ArrayList();

		try {
			List<SubjectDetails> riskList = this.queryForList("Subject.getSubjectsRisksForISIS", crn);
			return riskList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public SubjectDetails getNonPrimaryDifferemtEntitySubject(String crn, int primarySubjectEntity)
			throws CMSException {
		this.logger.debug("Inside getNonPrimaryDifferemtEntitySubject subject of SubjectDAO class");
		new SubjectDetails();

		try {
			Map dataMap = new HashMap();
			dataMap.put("crn", crn);
			dataMap.put("primarySubEntity", primarySubjectEntity);
			this.logger.debug("dataMap is:::::::::" + dataMap);
			SubjectDetails subjectDetail = (SubjectDetails) this.queryForObject("Subject.getNonPridifferentEntitySub",
					dataMap);
			this.logger.debug("subjectDetails is::::::" + subjectDetail);
			return subjectDetail;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public SubjectDetails getFirstSubjectOfSameEntity(String crn, int entityTypeOfNewSub) throws CMSException {
		this.logger.debug("Inside getFirstSubjectOfSameEntity method of SubjectDAO class");
		new SubjectDetails();

		try {
			Map dataMap = new HashMap();
			dataMap.put("crn", crn);
			dataMap.put("subEntity", entityTypeOfNewSub);
			this.logger.debug("dataMap is:::::::::" + dataMap);
			SubjectDetails subjectDetail = (SubjectDetails) this.queryForObject("Subject.getFirstSubjectOfSameEntity",
					dataMap);
			this.logger.debug("subjectDetails is::::::" + subjectDetail);
			return subjectDetail;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List getReDetails() throws CMSException {
		this.logger.debug("Inside getReDetails subject of SubjectDAO class");
		new ArrayList();

		try {
			List reList = this.queryForList("Subject.reDetailMap", "");
			return reList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List getRisksForSubject(int subjectId) throws CMSException {
		this.logger.debug("Inside getRisksForSubject subject of SubjectDAO class.." + subjectId);
		new ArrayList();

		try {
			List subjectRisks = this.queryForList("Subject.selectSubjectRiskCodes", subjectId);
			return subjectRisks;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void inserToSubjectRisk(List<SubjectDetails> subjectDetailsList) {
		this.logger.debug("Inside inserToSubjectRisk method os SubjectDAO class..");
		HashMap subjectRiskMap = new HashMap();
		subjectRiskMap.put("subjectList", subjectDetailsList);
		this.logger.debug("subjectRiskMap is::::" + subjectRiskMap);
		this.insert("Subject.insertSubjectRisks", subjectRiskMap);
	}

	public String getCaseStatus(String crn) throws CMSException {
		this.logger.debug("Inside getCaseStatus method of SubjectDAO class");
		String caseStatus = "";

		try {
			caseStatus = (String) this.queryForObject("Subject.getCaseStatus", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("caseStatus is::::::" + caseStatus);
		return caseStatus;
	}

	public List getActiveREIds() throws CMSException {
		this.logger.debug("Inside getActiveREIds method of SubjectDAO class");
		new ArrayList();

		try {
			List activeREIds = this.queryForList("Subject.getActiveREIds");
			return activeREIds;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List getAllRisks() throws CMSException {
		this.logger.debug("Inside getAllRisks method of SubjectDAO class");
		new ArrayList();

		try {
			List allRisks = this.queryForList("Subject.getAllRisks");
			return allRisks;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public SubjectDetails getSubjectColorDetails(String crn, int subjectId) throws CMSException {
		new SubjectDetails();

		try {
			this.logger.debug("subjectId:::" + subjectId);
			SubjectDetails subjectDetail = (SubjectDetails) this.queryForObject("Subject.getSubjectColor", subjectId);
			return subjectDetail;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public SubjectLevelRiskProfileDetailsVO getCaseDetailsForRiskProfile(String crn) throws CMSException {
		new SubjectLevelRiskProfileDetailsVO();

		try {
			this.logger.debug("Inside getCaseDetailsForRiskProfile:::" + crn);
			SubjectLevelRiskProfileDetailsVO caseDetailsForRisk = (SubjectLevelRiskProfileDetailsVO) this
					.queryForObject(this.caseDetailsForRiskProfile, crn);
			this.logger.debug("Client Code is---" + caseDetailsForRisk.getClientCode());
			return caseDetailsForRisk;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void insertCaseLevelRiskCountryBreakDown(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao insertCaseLevelRiskCountryBreakDown");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> caseLevelRiskCntryBrkDownDet = this
					.queryForList(this.getCountryBrkDownCaseLevelRiskDetails, subDetails.getCrn());
			Iterator<CaseCreationRiskAssociationVO> caseLevelRiskCntryBrkDownItr = caseLevelRiskCntryBrkDownDet
					.iterator();
			Iterator<CaseCreationRiskAssociationVO> itr = caseLevelRiskCntryBrkDownDet.iterator();
			boolean var7 = false;

			while (true) {
				CaseCreationRiskAssociationVO caseCreateRiskAss;
				List hasCountryBreakDownList;
				do {
					if (!itr.hasNext()) {
						return;
					}

					caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
					hasCountryBreakDownList = this.queryForList(this.isCaseLevelRiskCountryAlreadyExist,
							caseCreateRiskAss.getProfileId());
				} while (hasCountryBreakDownList.contains(subDetails.getCountryCode()));

				caseCreateRiskAss.setCrn(subDetails.getCrn());
				caseCreateRiskAss.setCountryCode(subDetails.getCountryCode());
				this.logger.debug("Subject Details Country Id---" + subDetails.getCountryId());
				StringBuffer newValue = new StringBuffer("");
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
				riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
				riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
				riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
				riskProfileHistoryVO.setAction("Add");
				riskProfileHistoryVO.setTask("Subject Addition");
				riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
				long riskCategoryId = caseCreateRiskAss.getRiskCategoryId();
				List<CaseCreationRiskAssociationVO> attibuteValueMapList = this.queryForList(this.GET_ATTRIBUTE_NAME,
						riskCategoryId);
				Iterator attibuteValueMapItr = attibuteValueMapList.iterator();

				while (true) {
					while (attibuteValueMapItr.hasNext()) {
						CaseCreationRiskAssociationVO attibuteValueMap = (CaseCreationRiskAssociationVO) attibuteValueMapItr
								.next();
						caseCreateRiskAss.setAttributeId(attibuteValueMap.getAttributeId());
						caseCreateRiskAss.setAttributeValue(attibuteValueMap.getAttributeValue());
						this.insert(this.ASSOCIATE_COUNTRY_BREAKDOWN_CASE_WITH_RISK, caseCreateRiskAss);
						if (attibuteValueMap.getAttributeName() != null
								&& attibuteValueMap.getAttributeValue() != null) {
							newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":"
									+ attibuteValueMap.getAttributeValue() + "<br />");
						} else if (attibuteValueMap.getAttributeValue() == null
								|| attibuteValueMap.getAttributeValue().equalsIgnoreCase("")
										&& attibuteValueMap.getAttributeName() != null) {
							newValue = newValue.append(
									"<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":" + "NA" + "<br />");
						}
					}

					riskProfileHistoryVO.setNewValue(newValue.toString());
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
					break;
				}
			}
		} catch (DataAccessException var16) {
			throw new CMSException(this.logger, var16);
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	public void insertIsIsCaseLevelRiskCountryBreakDown(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO,
			SubjectDetailsVO subDetailsvo) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao insertCaseLevelRiskCountryBreakDown");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> caseLevelRiskCntryBrkDownDet = this
					.queryForList(this.getCountryBrkDownCaseLevelRiskDetails, subDetailsvo.getCrn());
			Iterator<CaseCreationRiskAssociationVO> caseLevelRiskCntryBrkDownItr = caseLevelRiskCntryBrkDownDet
					.iterator();
			List<Long> profileIdList = new ArrayList();
			List<String> riskCodeList = new ArrayList();
			HashMap riskCodeWithProfileIdMap = new HashMap();

			while (caseLevelRiskCntryBrkDownItr.hasNext()) {
				CaseCreationRiskAssociationVO det = (CaseCreationRiskAssociationVO) caseLevelRiskCntryBrkDownItr.next();
				profileIdList.add(det.getProfileId());
				riskCodeList.add(det.getRiskCode());
				riskCodeWithProfileIdMap.put(det.getRiskCode(), det.getProfileId());
			}

			if (profileIdList.size() == 0) {
				profileIdList.add(0L);
			}

			List<String> hasCountryBreakDownList = this.queryForList(this.isCaseLevelRiskCountryAlreadyExist,
					profileIdList);
			this.logger.debug("hasCountryBreakDownList is--" + hasCountryBreakDownList);
			if (!hasCountryBreakDownList.contains(subDetailsvo.getCountryCode())) {
				this.logger.debug("hasCountryBreakDownList condition true");
				Iterator<CaseCreationRiskAssociationVO> itr = caseLevelRiskCntryBrkDownDet.iterator();
				boolean var11 = false;

				while (itr.hasNext()) {
					CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
					caseCreateRiskAss.setCrn(subDetailsvo.getCrn());
					caseCreateRiskAss.setCountryCode(subDetailsvo.getCountryCode());
					caseCreateRiskAss
							.setProfileId((Long) riskCodeWithProfileIdMap.get(caseCreateRiskAss.getRiskCode()));
					StringBuffer newValue = new StringBuffer("");
					RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
					riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
					riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
					riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
					riskProfileHistoryVO.setAction("Add");
					riskProfileHistoryVO.setTask("Subject Addition");
					riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
					long riskCategoryId = caseCreateRiskAss.getRiskCategoryId();
					List<CaseCreationRiskAssociationVO> attibuteValueMapList = this
							.queryForList(this.GET_ATTRIBUTE_NAME, riskCategoryId);
					Iterator attibuteValueMapItr = attibuteValueMapList.iterator();

					while (true) {
						while (attibuteValueMapItr.hasNext()) {
							CaseCreationRiskAssociationVO attibuteValueMap = (CaseCreationRiskAssociationVO) attibuteValueMapItr
									.next();
							caseCreateRiskAss.setAttributeId(attibuteValueMap.getAttributeId());
							caseCreateRiskAss.setAttributeValue(attibuteValueMap.getAttributeValue());
							this.insert(this.ASSOCIATE_COUNTRY_BREAKDOWN_CASE_WITH_RISK, caseCreateRiskAss);
							if (attibuteValueMap.getAttributeName() != null
									&& attibuteValueMap.getAttributeValue() != null) {
								newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":"
										+ attibuteValueMap.getAttributeValue() + "<br />");
							} else if (attibuteValueMap.getAttributeValue() == null
									|| attibuteValueMap.getAttributeValue().equalsIgnoreCase("")
											&& attibuteValueMap.getAttributeName() != null) {
								newValue = newValue.append(
										"<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":" + "NA" + "<br />");
							}
						}

						riskProfileHistoryVO.setNewValue(newValue.toString());
						this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
						break;
					}
				}
			}

		} catch (DataAccessException var19) {
			throw new CMSException(this.logger, var19);
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public void insertDeletedSubjectLevelRisksHistory(List<DeleteSubjectLevelRiskVO> entriesNeedtoBeDeletedList)
			throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao insertDeletedSubjectLevelRisksHistory");
			Iterator itr = entriesNeedtoBeDeletedList.iterator();

			while (itr.hasNext()) {
				DeleteSubjectLevelRiskVO deletedRiskHistory = (DeleteSubjectLevelRiskVO) itr.next();
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
				riskProfileHistoryVO.setCrn(deletedRiskHistory.getCrn());
				riskProfileHistoryVO.setSubjectId(deletedRiskHistory.getSubjectId());
				riskProfileHistoryVO.setRiskCode(deletedRiskHistory.getRiskCode());
				riskProfileHistoryVO.setAction("Remove");
				riskProfileHistoryVO.setTask("Subject Remove");
				this.insert(this.ASSOCIATE_DELETE_SUBJECT_RISK_HISTORY, riskProfileHistoryVO);
			}

		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void insertDeletedSubjectLevelCntryBrkDownRisksHistory(
			List<DeleteSubjectLevelRiskVO> entriesNeedtoBeDeletedList) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao insertDeletedSubjectLevelRisksHistory");
			Iterator itr = entriesNeedtoBeDeletedList.iterator();

			while (itr.hasNext()) {
				DeleteSubjectLevelRiskVO deletedRiskHistory = (DeleteSubjectLevelRiskVO) itr.next();
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
				riskProfileHistoryVO.setCrn(deletedRiskHistory.getCrn());
				riskProfileHistoryVO.setSubjectId(deletedRiskHistory.getSubjectId());
				riskProfileHistoryVO.setRiskCode(deletedRiskHistory.getRiskCode());
				riskProfileHistoryVO.setCountryMasterId(deletedRiskHistory.getCountryMasterId());
				riskProfileHistoryVO.setAction("Remove");
				riskProfileHistoryVO.setTask("Subject Remove");
				this.insert(this.ASSOCIATE_DELETE_SUBJECT_RISK_HISTORY, riskProfileHistoryVO);
			}

		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int deleteSubjectLevelRisksCntryBrkDown(List<DeleteSubjectLevelRiskVO> entriesNeedtoBeDeletedList)
			throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao deleteSubjectLevelRisksCntryBrkDown");
			int deletedRisksCountry = 0;
			Iterator itr = entriesNeedtoBeDeletedList.iterator();

			while (itr.hasNext()) {
				DeleteSubjectLevelRiskVO deletedRiskHistory = (DeleteSubjectLevelRiskVO) itr.next();
				deletedRisksCountry = this.delete(this.DELETE_SUBJECT_LEVEL_CNTRY_BRK_DOWN_RISK_COUNTRY,
						deletedRiskHistory);
				this.delete(this.DELETE_SUBJECT_LEVEL_CNTRY_BRK_DOWN_RISK, deletedRiskHistory);
			}

			return deletedRisksCountry;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void updateCaseLevelRiskCountryBreakDown(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao updateCaseLevelRiskCountryBreakDown");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> subjectLevelRiskOldDetailsList = this
					.queryForList("Subject.getCountryBrkDownCaseLevelRiskOldDetails", subDetails.getCrn());
			Iterator<CaseCreationRiskAssociationVO> subLevelRiskOldDetailsItr = subjectLevelRiskOldDetailsList
					.iterator();
			this.logger.debug(
					"subLevelRiskOldDetailsCntryBrkDownItr list size---" + subjectLevelRiskOldDetailsList.size());
			Set<String> oldRiskCodeSet = new HashSet();
			Set<String> oldCountrySet = new HashSet();
			Set<String> commonRiskCodeSet = new HashSet();
			Map<String, Long> riskCodeWithProfileIdMap = new HashMap();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			HashSet commonRiskCodeWithOptToMand = new HashSet();

			while (subLevelRiskOldDetailsItr.hasNext()) {
				CaseCreationRiskAssociationVO subLevelRisOldDet = (CaseCreationRiskAssociationVO) subLevelRiskOldDetailsItr
						.next();
				oldRiskCodeSet.add(subLevelRisOldDet.getRiskCode());
				oldCountrySet.add(subLevelRisOldDet.getCountryCode());
				riskCodeWithProfileIdMap.put(subLevelRisOldDet.getRiskCode(), subLevelRisOldDet.getProfileId());
				riskCodeWithRiskGroupMap.put(subLevelRisOldDet.getRiskCode(), subLevelRisOldDet.getIsRiskMandatory());
			}

			this.logger.debug("Country Set is---" + oldCountrySet.size());
			this.logger.debug("old Country Set is---" + oldCountrySet);
			this.logger.debug("subject entity type--" + subDetails.getEntityTypeId() + "---research elements Type--"
					+ subDetails.getReIds() + "---Country Code---" + subDetails.getCountryCode());
			String[] reStringArr = subDetails.getReIds().split(",");
			List<Integer> reList = new ArrayList();

			for (int i = 0; i < reStringArr.length; ++i) {
				reList.add(Integer.parseInt(reStringArr[i]));
			}

			caseLevelRiskProfileVO.setReIds(reList);
			this.logger.debug("Subject Details Research Elements Id---" + subDetails.getReIds());
			List<CaseCreationRiskAssociationVO> list = this.queryForList(this.getCaseLevelRiskMappingWithCntryBrkDown,
					caseLevelRiskProfileVO);
			Iterator<CaseCreationRiskAssociationVO> itr = list.iterator();
			boolean var16 = false;

			Iterator subRiskOldDetailsItr;
			while (itr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
				caseCreateRiskAss.setCrn(subDetails.getCrn());
				caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
				caseCreateRiskAss.setCountryCode(subDetails.getCountryCode());
				if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) != caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeWithOptToMand.add(caseCreateRiskAss.getRiskCode());
					caseCreateRiskAss
							.setProfileId((Long) riskCodeWithProfileIdMap.get(caseCreateRiskAss.getRiskCode()));
					this.update("Subject.updateCaseLevelRisk", caseCreateRiskAss);
				}

				StringBuffer newValue;
				List attibuteValueMapList;
				Iterator attibuteValueMapItr;
				CaseCreationRiskAssociationVO attibuteValueMap;
				if (!oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
					long profileId = (Long) this.insert("Subject.associateRiskAtCaseCreation", caseCreateRiskAss);
					caseCreateRiskAss.setProfileId(profileId);
					newValue = new StringBuffer("");
					RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
					riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
					riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
					riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
					riskProfileHistoryVO.setAction("Add");
					riskProfileHistoryVO.setTask("Subject Edit");
					riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
					long riskCategoryId = caseCreateRiskAss.getRiskCategoryId();
					attibuteValueMapList = this.queryForList(this.GET_ATTRIBUTE_NAME, riskCategoryId);
					attibuteValueMapItr = attibuteValueMapList.iterator();

					while (true) {
						while (attibuteValueMapItr.hasNext()) {
							attibuteValueMap = (CaseCreationRiskAssociationVO) attibuteValueMapItr.next();
							caseCreateRiskAss.setAttributeId(attibuteValueMap.getAttributeId());
							caseCreateRiskAss.setAttributeValue(attibuteValueMap.getAttributeValue());
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_COUNTRY_BREAKDOWN, caseCreateRiskAss);
							if (attibuteValueMap.getAttributeName() != null
									&& attibuteValueMap.getAttributeValue() != null) {
								newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":"
										+ attibuteValueMap.getAttributeValue() + "<br />");
							} else if (attibuteValueMap.getAttributeValue() == null
									|| attibuteValueMap.getAttributeValue().equalsIgnoreCase("")
											&& attibuteValueMap.getAttributeName() != null) {
								newValue = newValue.append(
										"<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":" + "NA" + "<br />");
							}
						}

						riskProfileHistoryVO.setNewValue(newValue.toString());
						this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
						break;
					}
				} else if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) == caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
					List cntryListForRiskCode = this.queryForList("Subject.getCaseLevelCountryForRiskCodeCntryBrkDown",
							caseCreateRiskAss);
					if (!cntryListForRiskCode.contains(subDetails.getCountryCode())) {
						long riskCategoryId = caseCreateRiskAss.getRiskCategoryId();
						subRiskOldDetailsItr = subjectLevelRiskOldDetailsList.iterator();

						while (subRiskOldDetailsItr.hasNext()) {
							CaseCreationRiskAssociationVO subRiskOldDetails = (CaseCreationRiskAssociationVO) subRiskOldDetailsItr
									.next();
							if (subRiskOldDetails.getRiskCode().equals(caseCreateRiskAss.getRiskCode())) {
								caseCreateRiskAss.setProfileId(subRiskOldDetails.getProfileId());
							}
						}

						newValue = new StringBuffer("");
						RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
						riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
						riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
						riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
						riskProfileHistoryVO.setAction("Add");
						riskProfileHistoryVO.setTask("Subject Edit");
						riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
						attibuteValueMapList = this.queryForList(this.GET_ATTRIBUTE_NAME, riskCategoryId);
						attibuteValueMapItr = attibuteValueMapList.iterator();

						while (true) {
							while (attibuteValueMapItr.hasNext()) {
								attibuteValueMap = (CaseCreationRiskAssociationVO) attibuteValueMapItr.next();
								caseCreateRiskAss.setAttributeId(attibuteValueMap.getAttributeId());
								caseCreateRiskAss.setAttributeValue(attibuteValueMap.getAttributeValue());
								this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_COUNTRY_BREAKDOWN, caseCreateRiskAss);
								if (attibuteValueMap.getAttributeName() != null
										&& attibuteValueMap.getAttributeValue() != null) {
									newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>"
											+ ":" + attibuteValueMap.getAttributeValue() + "<br />");
								} else if (attibuteValueMap.getAttributeValue() == null
										|| attibuteValueMap.getAttributeValue().equalsIgnoreCase("")
												&& attibuteValueMap.getAttributeName() != null) {
									newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>"
											+ ":" + "NA" + "<br />");
								}
							}

							riskProfileHistoryVO.setNewValue(newValue.toString());
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
							break;
						}
					}
				}
			}

			oldRiskCodeSet.removeAll(commonRiskCodeSet);
			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			Set<String> commonRiskCodeWithOptToMandOtherSubjects = new HashSet();
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			Set<Integer> REIdsList = new HashSet();
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this.queryForList("Subject.getSubREs", subDetails);
			subRiskOldDetailsItr = subLevelREList.iterator();

			label130 : while (subRiskOldDetailsItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subRiskOldDetailsItr
						.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}

				this.logger.debug("--------REIdsList----" + REIdsList);
				if (REIdsList.size() == 0) {
					REIdsList.add(0);
				}

				caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
				List<CaseCreationRiskAssociationVO> allSubRiskDetList = this
						.queryForList(this.getCaseLevelRiskMappingWithCntryBrkDown, caseLevelRiskProfileVO);
				Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

				while (true) {
					while (true) {
						if (!allSubRiskDetItr.hasNext()) {
							continue label130;
						}

						CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr
								.next();
						if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& !commonRiskCodeSet.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) == subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(subDetails.getCrn());
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						} else if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) != subAllRiskDet
										.getIsRiskMandatory()
								&& !commonRiskCodeWithOptToMand.contains(subAllRiskDet.getRiskCode())) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							commonRiskCodeWithOptToMandOtherSubjects.add(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(subDetails.getCrn());
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						}
					}
				}
			}

			Set<String> optionalAppliedRiskCodeSet = new HashSet();
			Iterator riskCodeListItr = riskCodeList.iterator();

			while (riskCodeListItr.hasNext()) {
				String riskCode = (String) riskCodeListItr.next();
				if ((Integer) riskCodeWithRiskGroupMap.get(riskCode) == 0
						&& !commonRiskCodeWithOptToMand.contains(riskCode)
						&& !commonRiskCodeWithOptToMandOtherSubjects.contains(riskCode)) {
					optionalAppliedRiskCodeSet.add(riskCode);
				}
			}

			riskCodeList.removeAll(optionalAppliedRiskCodeSet);
			riskCodeList.removeAll(commonRiskCodeWithOptToMand);
			Set<Long> profileSet = new HashSet();

			for (int k = 0; k < riskCodeList.size(); ++k) {
				profileSet.add((Long) riskCodeWithProfileIdMap.get(riskCodeList.get(k)));
			}

			List<Long> profileList = new ArrayList(profileSet);
			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			Map deleteRiskDetails = new HashMap();
			deleteRiskDetails.put(this.CRN, subDetails.getCrn());
			deleteRiskDetails.put(this.RISK_CODE_LIST, riskCodeList);
			if (riskCodeList.size() > 0) {
				this.delete("Subject.deleteCaseHasCntryBrkDown", profileList);
				this.delete("Subject.deleteOldCaseRisksOnSubjectEdit", deleteRiskDetails);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var29) {
			throw new CMSException(this.logger, var29);
		} catch (Exception var30) {
			throw new CMSException(this.logger, var30);
		}
	}

	public void deleteISISCaseLevelRiskCountryBreakDown(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO,
			String crn, int subjectId) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao deleteCaseLevelRiskCountryBreakDown");
			SubjectDetails subDetails = new SubjectDetails();
			subDetails.setCrn(crn);
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			Set<String> oldRiskCodeSet = new HashSet();
			new HashSet();
			new HashSet();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			new HashMap();
			new HashSet();
			List<CaseCreationRiskAssociationVO> getRiskDetailsForDeletedSubjectsList = this
					.queryForList("Subject.getISISCaseLevelRiskDetailsForDeletedSubjects", subjectId);
			Iterator getRiskDetailsForDeletedSubjectsItr = getRiskDetailsForDeletedSubjectsList.iterator();

			while (getRiskDetailsForDeletedSubjectsItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) getRiskDetailsForDeletedSubjectsItr
						.next();
				oldRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
				riskCodeWithRiskGroupMap.put(caseCreateRiskAss.getRiskCode(), caseCreateRiskAss.getIsRiskMandatory());
			}

			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			this.logger.debug(" riskCodeList--" + riskCodeList);
			new HashSet();
			Set<Integer> REIdsList = new HashSet();
			Map deleteCaseRiskForSubjectMap = new HashMap();
			deleteCaseRiskForSubjectMap.put(this.CRN, crn);
			deleteCaseRiskForSubjectMap.put("subjectId", subjectId);
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this.queryForList("Subject.getISISSubREstoDelete",
					deleteCaseRiskForSubjectMap);
			Iterator subLevelREItr = subLevelREList.iterator();

			label67 : while (subLevelREItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subLevelREItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}

				this.logger.debug("--------REIdsList----" + REIdsList);
				if (REIdsList.size() == 0) {
					REIdsList.add(0);
				}

				caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
				List<CaseCreationRiskAssociationVO> allSubRiskDetList = this.queryForList(
						"Subject.getCaseLevelRiskMappingWithCntryBrkDownForDelete", caseLevelRiskProfileVO);
				Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

				while (true) {
					while (true) {
						if (!allSubRiskDetItr.hasNext()) {
							continue label67;
						}

						CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr
								.next();
						if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) == subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						} else if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) != subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						}
					}
				}
			}

			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			Map getProfileIdListMap = new HashMap();
			getProfileIdListMap.put(this.CRN, crn);
			getProfileIdListMap.put(this.RISK_CODE_LIST, riskCodeList);
			List<Integer> profileList = new ArrayList();
			if (riskCodeList.size() > 0) {
				profileList = this.queryForList("Subject.getProfileIdList", getProfileIdListMap);
			}

			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			if (riskCodeList.size() > 0) {
				this.delete("Subject.deleteCaseHasCntryBrkDown", profileList);
				this.delete("Subject.deleteOldCaseRisksOnSubjectEdit", getProfileIdListMap);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var24) {
			throw new CMSException(this.logger, var24);
		} catch (Exception var25) {
			throw new CMSException(this.logger, var25);
		}
	}

	public void deleteCaseLevelRiskCountryBreakDown(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO, String crn,
			List<String> subjectIDStringList) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao deleteCaseLevelRiskCountryBreakDown");
			SubjectDetails subDetails = new SubjectDetails();
			subDetails.setCrn(crn);
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			Set<String> oldRiskCodeSet = new HashSet();
			new HashSet();
			new HashSet();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			new HashMap();
			new HashSet();
			this.logger.debug("Subject Id List--" + subjectIDStringList);
			List<CaseCreationRiskAssociationVO> getRiskDetailsForDeletedSubjectsList = this
					.queryForList("Subject.getCaseLevelRiskDetailsForDeletedSubjects", subjectIDStringList);
			Iterator getRiskDetailsForDeletedSubjectsItr = getRiskDetailsForDeletedSubjectsList.iterator();

			while (getRiskDetailsForDeletedSubjectsItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) getRiskDetailsForDeletedSubjectsItr
						.next();
				oldRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
				riskCodeWithRiskGroupMap.put(caseCreateRiskAss.getRiskCode(), caseCreateRiskAss.getIsRiskMandatory());
			}

			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			this.logger.debug(" riskCodeList--" + riskCodeList);
			Set<Integer> REIdsList = new HashSet();
			Map deleteCaseRiskForSubjectMap = new HashMap();
			deleteCaseRiskForSubjectMap.put(this.CRN, crn);
			deleteCaseRiskForSubjectMap.put("subjectIDStringList", subjectIDStringList);
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this.queryForList("Subject.getSubREstoDelete",
					deleteCaseRiskForSubjectMap);
			Iterator subLevelREItr = subLevelREList.iterator();

			label67 : while (subLevelREItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subLevelREItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}

				this.logger.debug("--------REIdsList----" + REIdsList);
				if (REIdsList.size() == 0) {
					REIdsList.add(0);
				}

				caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
				List<CaseCreationRiskAssociationVO> allSubRiskDetList = this.queryForList(
						"Subject.getCaseLevelRiskMappingWithCntryBrkDownForDelete", caseLevelRiskProfileVO);
				Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

				while (true) {
					while (true) {
						if (!allSubRiskDetItr.hasNext()) {
							continue label67;
						}

						CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr
								.next();
						if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) == subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						} else if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) != subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						}
					}
				}
			}

			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			Map getProfileIdListMap = new HashMap();
			getProfileIdListMap.put(this.CRN, crn);
			getProfileIdListMap.put(this.RISK_CODE_LIST, riskCodeList);
			List<Integer> profileList = new ArrayList();
			if (riskCodeList.size() > 0) {
				profileList = this.queryForList("Subject.getProfileIdList", getProfileIdListMap);
			}

			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			if (riskCodeList.size() > 0) {
				this.delete("Subject.deleteCaseHasCntryBrkDown", profileList);
				this.delete("Subject.deleteOldCaseRisksOnSubjectEdit", getProfileIdListMap);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var23) {
			throw new CMSException(this.logger, var23);
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}
	}

	public void updateCaseLevelRisk(SubjectLevelRiskProfileDetailsVO subjectLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		int totalAggregationId = 0;

		try {
			this.logger.debug("Inside the Subject Dao updateCaseLevelRisk");
			this.logger.debug("sub report type--" + subjectLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ subjectLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ subjectLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> caseLevelRiskOldDetailsList = this
					.queryForList(this.getCaseLevelRiskOldDetails, subDetails);
			Iterator<CaseCreationRiskAssociationVO> caseLevelRiskOldDetailsItr = caseLevelRiskOldDetailsList.iterator();
			this.logger.debug("caseLevelRiskOldDetailsItr list size---" + caseLevelRiskOldDetailsList.size());
			Set<String> oldRiskCodeSet = new HashSet();
			Set<String> commonRiskCodeSet = new HashSet();
			Set<String> riskCodeSet = new HashSet();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			HashSet commonRiskCodeWithOptToMand = new HashSet();

			while (caseLevelRiskOldDetailsItr.hasNext()) {
				CaseCreationRiskAssociationVO subLevelRisOldDet = (CaseCreationRiskAssociationVO) caseLevelRiskOldDetailsItr
						.next();
				oldRiskCodeSet.add(subLevelRisOldDet.getRiskCode());
				riskCodeWithRiskGroupMap.put(subLevelRisOldDet.getRiskCode(), subLevelRisOldDet.getIsRiskMandatory());
			}

			String[] reStringArr = subDetails.getReIds().split(",");
			List<Integer> reList = new ArrayList();

			for (int i = 0; i < reStringArr.length; ++i) {
				reList.add(Integer.parseInt(reStringArr[i]));
			}

			subjectLevelRiskProfileVO.setReIds(reList);
			this.logger.debug("Subject Details Research Elements Id---" + subDetails.getReIds());
			List<CaseCreationRiskAssociationVO> list = this
					.queryForList(this.getCaseLevelRiskMappingWithoutCntryBrkDown, subjectLevelRiskProfileVO);
			Iterator<CaseCreationRiskAssociationVO> itr = list.iterator();
			boolean var16 = false;

			List attributes;
			Iterator attrNameRiskItr;
			while (itr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
				caseCreateRiskAss.setCrn(subDetails.getCrn());
				caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
				if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) != caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeWithOptToMand.add(caseCreateRiskAss.getRiskCode());
					this.update("Subject.updateCaseLevelRisk", caseCreateRiskAss);
				}

				if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) == caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
				} else if (!oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
					this.insert("Subject.associateRiskAtCaseCreation", caseCreateRiskAss);
					if (!riskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
						RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
						riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
						riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
						riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
						riskProfileHistoryVO.setAction("Add");
						riskProfileHistoryVO.setTask("Subject Edit");
						StringBuffer newValue = new StringBuffer("");
						attributes = this.queryForList(this.GET_ATTRIBUTE_NAME, caseCreateRiskAss.getRiskCategoryId());
						attrNameRiskItr = attributes.iterator();

						while (true) {
							while (attrNameRiskItr.hasNext()) {
								CaseCreationRiskAssociationVO attributesValues = (CaseCreationRiskAssociationVO) attrNameRiskItr
										.next();
								if (attributesValues.getAttributeName() != null
										&& attributesValues.getAttributeValue() != null) {
									newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>"
											+ ":" + attributesValues.getAttributeValue() + "<br />");
								} else if (attributesValues.getAttributeValue() == null
										|| attributesValues.getAttributeValue().equalsIgnoreCase("")
												&& attributesValues.getAttributeName() != null) {
									newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>"
											+ ":" + "NA" + "<br />");
								}
							}

							riskProfileHistoryVO.setNewValue(newValue.toString());
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
							riskCodeSet.add(caseCreateRiskAss.getRiskCode());
							break;
						}
					}
				}
			}

			oldRiskCodeSet.removeAll(commonRiskCodeSet);
			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			Set<String> commonRiskCodeWithOptToMandOtherSubjects = new HashSet();
			attributes = this.queryForList("Subject.getSubREs", subDetails);
			attrNameRiskItr = attributes.iterator();

			label118 : while (attrNameRiskItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) attrNameRiskItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");
				Set<Integer> REIdsList = new HashSet();

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}

				this.logger.debug("--------REIdsList----" + REIdsList);
				if (REIdsList.size() == 0) {
					REIdsList.add(0);
				}

				subjectLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
				List<CaseCreationRiskAssociationVO> allSubRiskDetList = this
						.queryForList(this.getCaseLevelRiskMappingWithoutCntryBrkDown, subjectLevelRiskProfileVO);
				Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

				while (true) {
					while (true) {
						if (!allSubRiskDetItr.hasNext()) {
							continue label118;
						}

						CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr
								.next();
						if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& !commonRiskCodeSet.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) == subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(subDetails.getCrn());
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						} else if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) != subAllRiskDet
										.getIsRiskMandatory()
								&& !commonRiskCodeWithOptToMand.contains(subAllRiskDet.getRiskCode())) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							commonRiskCodeWithOptToMandOtherSubjects.add(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(subDetails.getCrn());
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						}
					}
				}
			}

			Set<String> optionalAppliedRiskCodeSet = new HashSet();
			Iterator riskCodeListItr = riskCodeList.iterator();

			while (riskCodeListItr.hasNext()) {
				String riskCode = (String) riskCodeListItr.next();
				if ((Integer) riskCodeWithRiskGroupMap.get(riskCode) == 0
						&& !commonRiskCodeWithOptToMand.contains(riskCode)
						&& !commonRiskCodeWithOptToMandOtherSubjects.contains(riskCode)) {
					optionalAppliedRiskCodeSet.add(riskCode);
				}
			}

			riskCodeList.removeAll(optionalAppliedRiskCodeSet);
			riskCodeList.removeAll(commonRiskCodeWithOptToMand);
			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			Map deleteRiskDetails = new HashMap();
			deleteRiskDetails.put(this.CRN, subDetails.getCrn());
			deleteRiskDetails.put(this.RISK_CODE_LIST, riskCodeList);
			if (riskCodeList.size() > 0) {
				this.delete("Subject.deleteOldCaseRisksOnSubjectEdit", deleteRiskDetails);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

			Object totalAggregationObj = this.queryForObject("Subject.getTotalAggregationId", subDetails.getCrn());
			if (totalAggregationObj != null) {
				totalAggregationId = (Integer) totalAggregationObj;
			}

			if (totalAggregationId == 0 && list.size() > 0) {
				TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
				totalRiskAggr.setCrn(subDetails.getCrn());
				totalRiskAggr.setOverallCaseLvlAggr(0);
				totalRiskAggr.setOverallSubLvlAggr(0);
				totalRiskAggr.setOverallCrnLvlAggr(0);
				this.insert("Subject.insertCaseAdditionTotalRiskAggr", totalRiskAggr);
			}

		} catch (DataAccessException var27) {
			throw new CMSException(this.logger, var27);
		} catch (Exception var28) {
			throw new CMSException(this.logger, var28);
		}
	}

	public void insertRiskProfileData(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		int totalAggregationId = 0;

		try {
			this.logger.debug("Inside the insertRiskProfileData Dao");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			String[] reStringArr = subDetails.getReIds().split(",");
			List<Integer> reList = new ArrayList();

			for (int i = 0; i < reStringArr.length; ++i) {
				reList.add(Integer.parseInt(reStringArr[i]));
			}

			caseLevelRiskProfileVO.setReIds(reList);
			String crn = subDetails.getCrn();
			List<CaseCreationRiskAssociationVO> risksList = this.queryForList("Subject.getAlreadyAssociatedRisks", crn);
			List<String> categoryLists = this.queryForList("Subject.getAlreadyAggregatedCategory", crn);
			Set<String> riskCodeSet = new HashSet();
			Iterator<CaseCreationRiskAssociationVO> risksItr = risksList.iterator();
			Set<String> risksSet = new HashSet();
			HashMap riskMap = new HashMap();

			while (risksItr.hasNext()) {
				CaseCreationRiskAssociationVO alreadyCaseCreationRiskAss = (CaseCreationRiskAssociationVO) risksItr
						.next();
				risksSet.add(alreadyCaseCreationRiskAss.getRiskCode());
				riskMap.put(alreadyCaseCreationRiskAss.getRiskCode(), alreadyCaseCreationRiskAss.getIsRiskMandatory());
			}

			Set<Integer> categorySet = new HashSet();
			new ArrayList();
			Iterator itr = categoryLists.iterator();

			while (itr.hasNext()) {
				String str = (String) itr.next();
				categorySet.add(Integer.parseInt(str));
			}

			List<CaseCreationRiskAssociationVO> list = this.queryForList("Subject.getRiskCode", caseLevelRiskProfileVO);
			itr = list.iterator();

			while (true) {
				CaseCreationRiskAssociationVO caseCreateRiskAss;
				label84 : do {
					while (itr.hasNext()) {
						caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
						if (!risksSet.contains(caseCreateRiskAss.getRiskCode())) {
							caseCreateRiskAss.setCrn(subDetails.getCrn());
							caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
							this.insert("Subject.associateRiskAtCaseCreation", caseCreateRiskAss);
							continue label84;
						}

						if (risksSet.contains(caseCreateRiskAss.getRiskCode())
								&& (Integer) riskMap.get(caseCreateRiskAss.getRiskCode()) != caseCreateRiskAss
										.getIsRiskMandatory()
								&& !riskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
							caseCreateRiskAss.setCrn(subDetails.getCrn());
							caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
							this.update("Subject.updateCaseLevelRisk", caseCreateRiskAss);
							riskCodeSet.add(caseCreateRiskAss.getRiskCode());
						}
					}

					List<CaseCreationRiskAssociationVO> cntryBrkDownList = this
							.queryForList("Subject.getRiskCodeHasCntryBrkDown", caseLevelRiskProfileVO);
					this.logger.debug("Country break down Case level risk List---" + cntryBrkDownList.size());
					new HashSet();
					Iterator cntryBrkDownItr = cntryBrkDownList.iterator();

					while (cntryBrkDownItr.hasNext()) {
						CaseCreationRiskAssociationVO caseCreationRiskAssociationVO = (CaseCreationRiskAssociationVO) cntryBrkDownItr
								.next();
						if (!risksSet.contains(caseCreationRiskAssociationVO.getRiskCode())) {
							caseCreationRiskAssociationVO.setCrn(subDetails.getCrn());
							caseCreationRiskAssociationVO.setSubjectId((long) subDetails.getSubjectId());
							this.insert("Subject.associateRiskAtCaseCreation", caseCreationRiskAssociationVO);
						} else if (risksSet.contains(caseCreationRiskAssociationVO.getRiskCode()) && (Integer) riskMap
								.get(caseCreationRiskAssociationVO.getRiskCode()) != caseCreationRiskAssociationVO
										.getIsRiskMandatory()) {
							caseCreationRiskAssociationVO.setCrn(subDetails.getCrn());
							caseCreationRiskAssociationVO.setSubjectId((long) subDetails.getSubjectId());
							this.update("Subject.updateCaseLevelRisk", caseCreationRiskAssociationVO);
						}
					}

					List<RiskAggregationVO> riskAggrVOList = this.queryForList("Subject.getCaseLevelRiskAggregation",
							caseLevelRiskProfileVO);
					Iterator riskAggrVOItr = riskAggrVOList.iterator();

					while (riskAggrVOItr.hasNext()) {
						RiskAggregationVO riskAggr = (RiskAggregationVO) riskAggrVOItr.next();
						if (!categorySet.contains(riskAggr.getRiskCategoryId())) {
							riskAggr.setSubjectId(0L);
							riskAggr.setAggregateValue(0);
							riskAggr.setCrn(subDetails.getCrn());
							this.insert(this.INSERT_SUBJECT_ADDITION_RISK_AGGR, riskAggr);
						}
					}

					Object totalAggregationObj = this.queryForObject("Subject.getTotalAggregationId",
							subDetails.getCrn());
					if (totalAggregationObj != null) {
						totalAggregationId = (Integer) totalAggregationObj;
					}

					this.logger.debug("CRN Inside insert risk profile data is--" + totalAggregationId);
					if (totalAggregationId == 0 && riskAggrVOList.size() > 0) {
						TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
						totalRiskAggr.setCrn(subDetails.getCrn());
						totalRiskAggr.setOverallCaseLvlAggr(0);
						totalRiskAggr.setOverallSubLvlAggr(0);
						totalRiskAggr.setOverallCrnLvlAggr(0);
						this.insert("Subject.insertCaseAdditionTotalRiskAggr", totalRiskAggr);
					}

					return;
				} while (riskCodeSet.contains(caseCreateRiskAss.getRiskCode()));

				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
				StringBuffer newValue = new StringBuffer("");
				riskProfileHistoryVO.setCrn(subDetails.getCrn());
				riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
				riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
				riskProfileHistoryVO.setAction("Add");
				riskProfileHistoryVO.setTask("Subject Addition");
				List<CaseCreationRiskAssociationVO> attributes = this.queryForList(this.GET_ATTRIBUTE_NAME,
						caseCreateRiskAss.getRiskCategoryId());
				Iterator attrNameRiskItr = attributes.iterator();

				while (true) {
					while (attrNameRiskItr.hasNext()) {
						CaseCreationRiskAssociationVO attributesValues = (CaseCreationRiskAssociationVO) attrNameRiskItr
								.next();
						if (attributesValues.getAttributeName() != null
								&& attributesValues.getAttributeValue() != null) {
							newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>" + ":"
									+ attributesValues.getAttributeValue() + "<br />");
						} else if (attributesValues.getAttributeValue() == null
								|| attributesValues.getAttributeValue().equalsIgnoreCase("")
										&& attributesValues.getAttributeName() != null) {
							newValue = newValue.append(
									"<b>" + attributesValues.getAttributeName() + "</b>" + ":" + "NA" + "<br />");
						}
					}

					riskProfileHistoryVO.setNewValue(newValue.toString());
					this.insert("Subject.insertCaseLevelRiskHistory", riskProfileHistoryVO);
					riskCodeSet.add(caseCreateRiskAss.getRiskCode());
					break;
				}
			}
		} catch (DataAccessException var25) {
			throw new CMSException(this.logger, var25);
		} catch (Exception var26) {
			throw new CMSException(this.logger, var26);
		}
	}

	public void insertSubjectLevelRisk(SubjectLevelRiskProfileDetailsVO subjectLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		int totalAggregationId = 0;

		try {
			this.logger.debug("Inside the Subject Dao insertSubjectLevelRisk");
			this.logger.debug("sub report type--" + subjectLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ subjectLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ subjectLevelRiskProfileVO.getClientCode());
			subjectLevelRiskProfileVO.setSubjectType(subDetails.getEntityTypeId());
			String[] reStringArr = subDetails.getReIds().split(",");
			List<Integer> reList = new ArrayList();

			for (int i = 0; i < reStringArr.length; ++i) {
				reList.add(Integer.parseInt(reStringArr[i]));
			}

			subjectLevelRiskProfileVO.setReIds(reList);
			subjectLevelRiskProfileVO.setSubjectCountry(subDetails.getCountryCode());
			List<CaseCreationRiskAssociationVO> list = this.queryForList(this.GET_SUBJECT_LEVEL_RISK_MAPPING,
					subjectLevelRiskProfileVO);
			this.logger.debug("List Size is--" + list.size());
			Set<String> riskCodeSet = new HashSet();
			Iterator<CaseCreationRiskAssociationVO> itr = list.iterator();
			boolean var10 = false;

			while (true) {
				StringBuffer newValue;
				CaseCreationRiskAssociationVO caseCreateRiskAss;
				RiskProfileHistoryVO riskProfileHistoryVO;
				List riskAggrVOList;
				Iterator attrNameRiskItr;
				CaseCreationRiskAssociationVO attributesValues;
				label108 : do {
					while (itr.hasNext()) {
						caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
						caseCreateRiskAss.setCrn(subDetails.getCrn());
						caseCreateRiskAss.setCountryCode(subDetails.getCountryCode());
						caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
						newValue = new StringBuffer("");
						if (caseCreateRiskAss.getHasCntryBreakdown() == 0) {
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK, caseCreateRiskAss);
							continue label108;
						}

						if (caseCreateRiskAss.getHasCntryBreakdown() == 1
								&& !riskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
							riskProfileHistoryVO = new RiskProfileHistoryVO();
							riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
							riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
							riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
							riskProfileHistoryVO.setAction("Add");
							riskProfileHistoryVO.setTask("Subject Addition");
							riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
							riskProfileHistoryVO.setSubjectId(caseCreateRiskAss.getSubjectId());
							riskAggrVOList = this.queryForList(this.GET_ATTRIBUTE_NAME,
									caseCreateRiskAss.getRiskCategoryId());
							attrNameRiskItr = riskAggrVOList.iterator();

							while (true) {
								while (attrNameRiskItr.hasNext()) {
									attributesValues = (CaseCreationRiskAssociationVO) attrNameRiskItr.next();
									if (attributesValues.getAttributeName() != null
											&& attributesValues.getAttributeValue() != null) {
										newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>"
												+ ":" + attributesValues.getAttributeValue() + "<br />");
									} else if (attributesValues.getAttributeValue() == null
											|| attributesValues.getAttributeValue().equalsIgnoreCase("")
													&& attributesValues.getAttributeName() != null) {
										newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>"
												+ ":" + "NA" + "<br />");
									}
								}

								riskProfileHistoryVO.setNewValue(newValue.toString());
								this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
								riskCodeSet.add(caseCreateRiskAss.getRiskCode());
								break;
							}
						}
					}

					List<CaseCreationRiskAssociationVO> cntryBrkDownList = this
							.queryForList(this.getSubjectLevelRiskMappingWithCntryBrkDown, subjectLevelRiskProfileVO);
					this.logger.debug("cntryBrkDownList--" + cntryBrkDownList.size());
					Iterator cntryBrkDownItr = cntryBrkDownList.iterator();

					while (cntryBrkDownItr.hasNext()) {
						CaseCreationRiskAssociationVO caseCreateRiskAssCntryBrkDown = (CaseCreationRiskAssociationVO) cntryBrkDownItr
								.next();
						caseCreateRiskAssCntryBrkDown.setCrn(subDetails.getCrn());
						caseCreateRiskAssCntryBrkDown.setSubjectId((long) subDetails.getSubjectId());
						long profileId = (Long) this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK,
								caseCreateRiskAssCntryBrkDown);
						Iterator itr1 = list.iterator();

						while (itr1.hasNext()) {
							CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr1
									.next();
							caseCreateRiskAss.setCrn(subDetails.getCrn());
							caseCreateRiskAss.setCountryCode(subDetails.getCountryCode());
							caseCreateRiskAss.setProfileId(profileId);
							if (caseCreateRiskAss.getRiskCode().equals(caseCreateRiskAssCntryBrkDown.getRiskCode())) {
								this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_COUNTRY_BREAKDOWN, caseCreateRiskAss);
							}
						}
					}

					riskAggrVOList = this.queryForList("Subject.getSubjectLevelRiskMappingForAggregation",
							subjectLevelRiskProfileVO);
					attrNameRiskItr = riskAggrVOList.iterator();

					while (attrNameRiskItr.hasNext()) {
						RiskAggregationVO riskAggr = (RiskAggregationVO) attrNameRiskItr.next();
						riskAggr.setSubjectId((long) subDetails.getSubjectId());
						riskAggr.setAggregateValue(0);
						riskAggr.setCrn(subDetails.getCrn());
						this.insert(this.INSERT_SUBJECT_ADDITION_RISK_AGGR, riskAggr);
					}

					Object totalAggregationObj = this.queryForObject("Subject.getTotalAggregationId",
							subDetails.getCrn());
					if (totalAggregationObj != null) {
						totalAggregationId = (Integer) totalAggregationObj;
					}

					if (totalAggregationId == 0 && riskAggrVOList.size() > 0) {
						TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
						totalRiskAggr.setCrn(subDetails.getCrn());
						totalRiskAggr.setOverallCaseLvlAggr(0);
						totalRiskAggr.setOverallSubLvlAggr(0);
						totalRiskAggr.setOverallCrnLvlAggr(0);
						totalAggregationId = (Integer) this.insert("Subject.insertCaseAdditionTotalRiskAggr",
								totalRiskAggr);
					}

					if (riskAggrVOList.size() > 0) {
						SubjectLevelAggregation subRiskAggr = new SubjectLevelAggregation();
						subRiskAggr.setCrn(subDetails.getCrn());
						subRiskAggr.setTotalAggregationId(totalAggregationId);
						subRiskAggr.setSubjectId((long) subDetails.getSubjectId());
						subRiskAggr.setEachSubLvlAggr(0);
						this.insert(this.INSERT_SUBJECT_ADDITION_TOTAL_RISK_AGGR, subRiskAggr);
					}

					return;
				} while (riskCodeSet.contains(caseCreateRiskAss.getRiskCode()));

				riskProfileHistoryVO = new RiskProfileHistoryVO();
				riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
				riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
				riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
				riskProfileHistoryVO.setAction("Add");
				riskProfileHistoryVO.setTask("Subject Addition");
				riskProfileHistoryVO.setSubjectId(caseCreateRiskAss.getSubjectId());
				riskAggrVOList = this.queryForList(this.GET_ATTRIBUTE_NAME, caseCreateRiskAss.getRiskCategoryId());
				attrNameRiskItr = riskAggrVOList.iterator();

				while (true) {
					while (attrNameRiskItr.hasNext()) {
						attributesValues = (CaseCreationRiskAssociationVO) attrNameRiskItr.next();
						if (attributesValues.getAttributeName() != null
								&& attributesValues.getAttributeValue() != null) {
							newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>" + ":"
									+ attributesValues.getAttributeValue() + "<br />");
						} else if (attributesValues.getAttributeValue() == null
								|| attributesValues.getAttributeValue().equalsIgnoreCase("")
										&& attributesValues.getAttributeName() != null) {
							newValue = newValue.append(
									"<b>" + attributesValues.getAttributeName() + "</b>" + ":" + "NA" + "<br />");
						}
					}

					riskProfileHistoryVO.setNewValue(newValue.toString());
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
					riskCodeSet.add(caseCreateRiskAss.getRiskCode());
					break;
				}
			}
		} catch (DataAccessException var18) {
			throw new CMSException(this.logger, var18);
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	public void updateSubjectLevelRiskAggregation(SubjectLevelRiskProfileDetailsVO subjectLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		int totalAggregationId = 0;
		int eachSubjectAggregationId = 0;

		try {
			this.logger.debug("Inside the Subject Dao updateSubjectLevelRiskAggregation");
			this.logger.debug("sub report type--" + subjectLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ subjectLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ subjectLevelRiskProfileVO.getClientCode());
			subjectLevelRiskProfileVO.setSubjectId(subDetails.getSubjectId());
			subjectLevelRiskProfileVO.setCrn(subDetails.getCrn());
			List<RiskAggregationVO> aggrList = this.queryForList("Subject.getSubjectLevelRiskCategoryDetails",
					subjectLevelRiskProfileVO);
			Iterator<RiskAggregationVO> aggrItr = aggrList.iterator();
			List<RiskAggregationVO> aggrList1 = this.queryForList("Subject.getSubjectLevelAggragationDetails",
					subjectLevelRiskProfileVO);
			Iterator<RiskAggregationVO> aggrItr1 = aggrList1.iterator();
			Set<Integer> categorySet = new HashSet();
			Map<Integer, Integer> aggTableMap = new HashMap();
			Set<Integer> categoryNewSet = new HashSet();
			HashSet commonCategorySet = new HashSet();

			while (aggrItr1.hasNext()) {
				RiskAggregationVO riskAgg = (RiskAggregationVO) aggrItr1.next();
				categorySet.add(riskAgg.getRiskCategoryId());
				aggTableMap.put(riskAgg.getRiskCategoryId(), riskAgg.getRiskAggregationId());
			}

			this.logger.debug("Subject Level Category Set--" + categorySet);
			byte aggregateValue = 0;

			while (aggrItr.hasNext()) {
				RiskAggregationVO riskAgg = (RiskAggregationVO) aggrItr.next();
				categoryNewSet.add(riskAgg.getRiskCategoryId());
				if (riskAgg.getAggregateValue() == 1 && aggregateValue == 0) {
					aggregateValue = 1;
				}

				if (!categorySet.contains(riskAgg.getRiskCategoryId())) {
					riskAgg.setRiskType(2);
					this.insert("Subject.insertSubjectRiskAggregationOnUpdate", riskAgg);
				} else if (categorySet.contains(riskAgg.getRiskCategoryId())) {
					commonCategorySet.add(riskAgg.getRiskCategoryId());
					riskAgg.setRiskAggregationId((Integer) aggTableMap.get(riskAgg.getRiskCategoryId()));
					this.update("Subject.updateSubjectRiskAggregationOnUpdate", riskAgg);
				}
			}

			Object totalAggregationObj = this.queryForObject("Subject.getTotalAggregationId", subDetails.getCrn());
			if (totalAggregationObj != null) {
				totalAggregationId = (Integer) totalAggregationObj;
			}

			this.logger.debug("CRN Inside insert risk profile data is--" + totalAggregationId);
			if (totalAggregationId == 0 && aggrList.size() > 0) {
				TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
				totalRiskAggr.setCrn(subDetails.getCrn());
				totalRiskAggr.setOverallCaseLvlAggr(0);
				totalRiskAggr.setOverallSubLvlAggr(0);
				totalRiskAggr.setOverallCrnLvlAggr(0);
				totalAggregationId = (Integer) this.insert("Subject.insertCaseAdditionTotalRiskAggr", totalRiskAggr);
			}

			this.logger.debug("Subject Level new Category Set--" + categoryNewSet);
			this.logger.debug("Subject Level common Category Set--" + commonCategorySet);
			Object eachSubjectAggregationObj = this.queryForObject("Subject.getEachSubjectAggregationId",
					subDetails.getSubjectId());
			if (eachSubjectAggregationObj != null) {
				eachSubjectAggregationId = (Integer) eachSubjectAggregationObj;
			}

			SubjectLevelAggregation subRiskAggr;
			if (aggrList.size() > 0 && eachSubjectAggregationId == 0) {
				subRiskAggr = new SubjectLevelAggregation();
				subRiskAggr.setCrn(subDetails.getCrn());
				subRiskAggr.setTotalAggregationId(totalAggregationId);
				subRiskAggr.setSubjectId((long) subDetails.getSubjectId());
				subRiskAggr.setEachSubLvlAggr(0);
				this.insert(this.INSERT_SUBJECT_ADDITION_TOTAL_RISK_AGGR, subRiskAggr);
			} else {
				subRiskAggr = new SubjectLevelAggregation();
				subRiskAggr.setCrn(subDetails.getCrn());
				subRiskAggr.setSubjectId((long) subDetails.getSubjectId());
				subRiskAggr.setEachSubLvlAggr(aggregateValue);
				this.update(this.UPDATE_SUBJECT_TOTAL_RISK_AGGR, subRiskAggr);
			}

			categorySet.removeAll(commonCategorySet);
			this.logger.debug("subject level Category set after remove---" + categorySet);
			List<Integer> categoryList = new ArrayList(categorySet);
			if (categoryList.size() > 0) {
				Map aggrDeleteMap = new HashMap();
				aggrDeleteMap.put(this.CRN, subDetails.getCrn());
				aggrDeleteMap.put(this.SUBJECT_ID, subDetails.getSubjectId());
				aggrDeleteMap.put("categoryList", categoryList);
				this.delete("Subject.deleteSubLevelCategoryAggr", aggrDeleteMap);
			}

			int overallCaseAggr = 0;
			int overallSubAggr = 0;
			int overallCrnAggr = 0;
			List<Integer> caseLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesCaseAggr",
					subDetails.getCrn());
			List<Integer> subjectLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesSubAggr",
					subDetails.getCrn());
			if (caseLevelAttributeValue.contains(1)) {
				overallCaseAggr = 1;
			}

			if (subjectLevelAttributeValue.contains(1)) {
				overallSubAggr = 1;
			}

			if (overallCaseAggr == 1 || overallSubAggr == 1) {
				overallCrnAggr = 1;
			}

			TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
			totalRiskAggr.setCrn(subDetails.getCrn());
			totalRiskAggr.setOverallCaseLvlAggr(overallCaseAggr);
			totalRiskAggr.setOverallSubLvlAggr(overallSubAggr);
			totalRiskAggr.setOverallCrnLvlAggr(overallCrnAggr);
			this.update("Subject.updateSubjectOverallRiskAggr", totalRiskAggr);
		} catch (DataAccessException var23) {
			throw new CMSException(this.logger, var23);
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}
	}

	public void updateCaseLevelRiskAggregation(SubjectLevelRiskProfileDetailsVO subjectLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao updateCaseLevelRiskAggregation");
			this.logger.debug("sub report type--" + subjectLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ subjectLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ subjectLevelRiskProfileVO.getClientCode());
			subjectLevelRiskProfileVO.setSubjectId(subDetails.getSubjectId());
			subjectLevelRiskProfileVO.setCrn(subDetails.getCrn());
			List<RiskAggregationVO> aggrList = this.queryForList("Subject.getCaseLevelRiskCategoryDetails",
					subjectLevelRiskProfileVO);
			Iterator<RiskAggregationVO> aggrItr = aggrList.iterator();
			List<RiskAggregationVO> aggrList1 = this.queryForList("Subject.getCaseLevelAggragationDetails",
					subjectLevelRiskProfileVO);
			Iterator<RiskAggregationVO> aggrItr1 = aggrList1.iterator();
			Set<Integer> categorySet = new HashSet();
			Map<Integer, Integer> aggTableMap = new HashMap();
			Set<Integer> categoryNewSet = new HashSet();
			HashSet commonCategorySet = new HashSet();

			while (aggrItr1.hasNext()) {
				RiskAggregationVO riskAgg = (RiskAggregationVO) aggrItr1.next();
				categorySet.add(riskAgg.getRiskCategoryId());
				aggTableMap.put(riskAgg.getRiskCategoryId(), riskAgg.getRiskAggregationId());
			}

			boolean aggregateValue = false;

			while (aggrItr.hasNext()) {
				RiskAggregationVO riskAgg = (RiskAggregationVO) aggrItr.next();
				categoryNewSet.add(riskAgg.getRiskCategoryId());
				if (riskAgg.getAggregateValue() == 1 && !aggregateValue) {
					aggregateValue = true;
				}

				if (!categorySet.contains(riskAgg.getRiskCategoryId())) {
					riskAgg.setRiskType(1);
					riskAgg.setSubjectId(0L);
					this.insert("Subject.insertSubjectRiskAggregationOnUpdate", riskAgg);
				} else if (categorySet.contains(riskAgg.getRiskCategoryId())) {
					commonCategorySet.add(riskAgg.getRiskCategoryId());
					riskAgg.setRiskAggregationId((Integer) aggTableMap.get(riskAgg.getRiskCategoryId()));
					this.update("Subject.updateSubjectRiskAggregationOnUpdate", riskAgg);
				}
			}

			this.logger.debug("Case Level new Category Set--" + categorySet);
			this.logger.debug("Case Level new Category Set--" + categoryNewSet);
			this.logger.debug("Case Level common Category Set--" + commonCategorySet);
			categorySet.removeAll(commonCategorySet);
			this.logger.debug("case level Category set after remove---" + categorySet);
			List<Integer> categoryList = new ArrayList(categorySet);
			if (categoryList.size() > 0) {
				Map aggrDeleteMap = new HashMap();
				aggrDeleteMap.put(this.CRN, subDetails.getCrn());
				aggrDeleteMap.put("categoryList", categoryList);
				this.delete("Subject.deleteCaseLevelAggr", aggrDeleteMap);
			}

			int overallCaseAggr = 0;
			int overallSubAggr = 0;
			int overallCrnAggr = 0;
			List<Integer> caseLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesCaseAggr",
					subDetails.getCrn());
			List<Integer> subjectLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesSubAggr",
					subDetails.getCrn());
			if (caseLevelAttributeValue.contains(1)) {
				overallCaseAggr = 1;
			}

			if (subjectLevelAttributeValue.contains(1)) {
				overallSubAggr = 1;
			}

			if (overallCaseAggr == 1 || overallSubAggr == 1) {
				overallCrnAggr = 1;
			}

			TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
			totalRiskAggr.setCrn(subDetails.getCrn());
			totalRiskAggr.setOverallCaseLvlAggr(overallCaseAggr);
			totalRiskAggr.setOverallSubLvlAggr(overallSubAggr);
			totalRiskAggr.setOverallCrnLvlAggr(overallCrnAggr);
			this.update("Subject.updateSubjectOverallRiskAggr", totalRiskAggr);
		} catch (DataAccessException var19) {
			throw new CMSException(this.logger, var19);
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public void deleteCaseLevelRiskAggregation(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO, String crn,
			List<String> subjectIDStringList) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao updateCaseLevelRiskAggregation");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			Set<Long> categorySet = new HashSet();
			List<CaseCreationRiskAssociationVO> getRiskDetailsForDeletedSubjectsList = this
					.queryForList("Subject.getCaseLevelCategoryFromProfileForSubject", subjectIDStringList);
			Iterator getRiskDetailsForDeletedSubjectsItr = getRiskDetailsForDeletedSubjectsList.iterator();

			while (getRiskDetailsForDeletedSubjectsItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) getRiskDetailsForDeletedSubjectsItr
						.next();
				categorySet.add(caseCreateRiskAss.getRiskCategoryId());
			}

			List<Long> categoryList = new ArrayList(categorySet);
			this.logger.debug("Size of categoryList--" + categoryList);
			Set<Integer> REIdsList = new HashSet();
			Map deleteCaseRiskForSubjectMap = new HashMap();
			deleteCaseRiskForSubjectMap.put(this.CRN, crn);
			deleteCaseRiskForSubjectMap.put("subjectIDStringList", subjectIDStringList);
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this.queryForList("Subject.getAllSubREs",
					deleteCaseRiskForSubjectMap);
			Iterator subLevelREItr = subLevelREList.iterator();

			while (subLevelREItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subLevelREItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}
			}

			this.logger.debug("--------REIdsList----" + REIdsList);
			if (REIdsList.size() == 0) {
				REIdsList.add(0);
			}

			caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
			List<CaseCreationRiskAssociationVO> allSubRiskDetList = this
					.queryForList("Subject.getAllCaseLevelRiskMapping", caseLevelRiskProfileVO);
			Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

			while (allSubRiskDetItr.hasNext()) {
				CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr.next();
				if (categoryList.contains(subAllRiskDet.getRiskCategoryId())) {
					categoryList.remove(subAllRiskDet.getRiskCategoryId());
				}
			}

			this.logger.debug("case level Category set after remove---" + categoryList);
			if (categoryList.size() > 0) {
				Map aggrDeleteMap = new HashMap();
				aggrDeleteMap.put(this.CRN, crn);
				aggrDeleteMap.put("categoryList", categoryList);
				this.delete("Subject.deleteCaseLevelAggr", aggrDeleteMap);
			}

			int overallCaseAggr = 0;
			int overallSubAggr = 0;
			int overallCrnAggr = 0;
			List<Integer> caseLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesCaseAggr", crn);
			List<Integer> subjectLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesSubAggr", crn);
			if (caseLevelAttributeValue.contains(1)) {
				overallCaseAggr = 1;
			}

			if (subjectLevelAttributeValue.contains(1)) {
				overallSubAggr = 1;
			}

			if (overallCaseAggr == 1 || overallSubAggr == 1) {
				overallCrnAggr = 1;
			}

			TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
			totalRiskAggr.setCrn(crn);
			totalRiskAggr.setOverallCaseLvlAggr(overallCaseAggr);
			totalRiskAggr.setOverallSubLvlAggr(overallSubAggr);
			totalRiskAggr.setOverallCrnLvlAggr(overallCrnAggr);
			this.update("Subject.updateSubjectOverallRiskAggr", totalRiskAggr);
		} catch (DataAccessException var20) {
			throw new CMSException(this.logger, var20);
		} catch (Exception var21) {
			throw new CMSException(this.logger, var21);
		}
	}

	public void deleteISISCaseLevelRiskAggregation(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO, String crn,
			int subjectId) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao updateCaseLevelRiskAggregation");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			Set<Long> categorySet = new HashSet();
			List<CaseCreationRiskAssociationVO> getRiskDetailsForDeletedSubjectsList = this
					.queryForList("Subject.getISISCaseLevelCategoryFromProfileForSubject", subjectId);
			Iterator getRiskDetailsForDeletedSubjectsItr = getRiskDetailsForDeletedSubjectsList.iterator();

			while (getRiskDetailsForDeletedSubjectsItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) getRiskDetailsForDeletedSubjectsItr
						.next();
				categorySet.add(caseCreateRiskAss.getRiskCategoryId());
			}

			List<Long> categoryList = new ArrayList(categorySet);
			this.logger.debug("Size of categoryList--" + categoryList);
			Set<Integer> REIdsList = new HashSet();
			Map deleteCaseRiskForSubjectMap = new HashMap();
			deleteCaseRiskForSubjectMap.put(this.CRN, crn);
			deleteCaseRiskForSubjectMap.put("subjectId", subjectId);
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this.queryForList("Subject.getISISAllSubREs",
					deleteCaseRiskForSubjectMap);
			Iterator subLevelREItr = subLevelREList.iterator();

			while (subLevelREItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subLevelREItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}
			}

			this.logger.debug("--------REIdsList----" + REIdsList);
			if (REIdsList.size() == 0) {
				REIdsList.add(0);
			}

			caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
			List<CaseCreationRiskAssociationVO> allSubRiskDetList = this
					.queryForList("Subject.getAllCaseLevelRiskMapping", caseLevelRiskProfileVO);
			Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

			while (allSubRiskDetItr.hasNext()) {
				CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr.next();
				if (categoryList.contains(subAllRiskDet.getRiskCategoryId())) {
					categoryList.remove(subAllRiskDet.getRiskCategoryId());
				}
			}

			this.logger.debug("case level Category set after remove---" + categoryList);
			if (categoryList.size() > 0) {
				Map aggrDeleteMap = new HashMap();
				aggrDeleteMap.put(this.CRN, crn);
				aggrDeleteMap.put("categoryList", categoryList);
				this.delete("Subject.deleteCaseLevelAggr", aggrDeleteMap);
			}

			int overallCaseAggr = 0;
			int overallSubAggr = 0;
			int overallCrnAggr = 0;
			List<Integer> caseLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesCaseAggr", crn);
			List<Integer> subjectLevelAttributeValue = this.queryForList("Subject.getAtrributeValuesSubAggr", crn);
			if (caseLevelAttributeValue.contains(1)) {
				overallCaseAggr = 1;
			}

			if (subjectLevelAttributeValue.contains(1)) {
				overallSubAggr = 1;
			}

			if (overallCaseAggr == 1 || overallSubAggr == 1) {
				overallCrnAggr = 1;
			}

			TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
			totalRiskAggr.setCrn(crn);
			totalRiskAggr.setOverallCaseLvlAggr(overallCaseAggr);
			totalRiskAggr.setOverallSubLvlAggr(overallSubAggr);
			totalRiskAggr.setOverallCrnLvlAggr(overallCrnAggr);
			this.update("Subject.updateSubjectOverallRiskAggr", totalRiskAggr);
		} catch (DataAccessException var20) {
			throw new CMSException(this.logger, var20);
		} catch (Exception var21) {
			throw new CMSException(this.logger, var21);
		}
	}

	public void updateSubjectLevelRisk(SubjectLevelRiskProfileDetailsVO subjectLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao updateSubjectLevelRisk");
			this.logger.debug("sub report type--" + subjectLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ subjectLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ subjectLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> subjectLevelRiskOldDetailsList = this
					.queryForList(this.getSubjectLevelRiskOldDetails, subDetails);
			Iterator<CaseCreationRiskAssociationVO> subLevelRiskOldDetailsItr = subjectLevelRiskOldDetailsList
					.iterator();
			this.logger.debug("subLevelRiskOldDetailsItr list size---" + subjectLevelRiskOldDetailsList.size());
			Set<String> oldRiskCodeSet = new HashSet();
			Set<String> commonRiskCodeSet = new HashSet();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			HashSet commonRiskCodeWithOptToMand = new HashSet();

			while (subLevelRiskOldDetailsItr.hasNext()) {
				CaseCreationRiskAssociationVO subLevelRisOldDet = (CaseCreationRiskAssociationVO) subLevelRiskOldDetailsItr
						.next();
				oldRiskCodeSet.add(subLevelRisOldDet.getRiskCode());
				riskCodeWithRiskGroupMap.put(subLevelRisOldDet.getRiskCode(), subLevelRisOldDet.getIsRiskMandatory());
			}

			this.logger.debug("subject entity type--" + subDetails.getEntityTypeId() + "---research elements Type--"
					+ subDetails.getReIds() + "---Country Code---" + subDetails.getCountryCode());
			subjectLevelRiskProfileVO.setSubjectType(subDetails.getEntityTypeId());
			String[] reStringArr = subDetails.getReIds().split(",");
			List<Integer> reList = new ArrayList();

			for (int i = 0; i < reStringArr.length; ++i) {
				reList.add(Integer.parseInt(reStringArr[i]));
			}

			subjectLevelRiskProfileVO.setReIds(reList);
			subjectLevelRiskProfileVO.setSubjectCountry(subDetails.getCountryCode());
			this.logger.debug("Subject Details Research Elements Id---" + subDetails.getReIds());
			List<CaseCreationRiskAssociationVO> list = this
					.queryForList(this.getSubjectLevelRiskMappingWithoutCntryBrkDown, subjectLevelRiskProfileVO);
			Set<String> riskCodeSet = new HashSet();
			Iterator<CaseCreationRiskAssociationVO> itr = list.iterator();
			boolean var15 = false;

			while (itr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
				caseCreateRiskAss.setCrn(subDetails.getCrn());
				caseCreateRiskAss.setCountryCode(subDetails.getCountryCode());
				caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
				if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) != caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeWithOptToMand.add(caseCreateRiskAss.getRiskCode());
					this.update("Subject.updateSubjectLevelRisk", caseCreateRiskAss);
				}

				if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) == caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
				} else if (!oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK, caseCreateRiskAss);
					if (!riskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
						RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
						StringBuffer newValue = new StringBuffer("");
						riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
						riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
						riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
						riskProfileHistoryVO.setAction("Add");
						riskProfileHistoryVO.setTask("Subject Edit");
						riskProfileHistoryVO.setSubjectId(caseCreateRiskAss.getSubjectId());
						List<CaseCreationRiskAssociationVO> attributes = this.queryForList(this.GET_ATTRIBUTE_NAME,
								caseCreateRiskAss.getRiskCategoryId());
						Iterator attrNameRiskItr = attributes.iterator();

						while (true) {
							while (attrNameRiskItr.hasNext()) {
								CaseCreationRiskAssociationVO attributesValues = (CaseCreationRiskAssociationVO) attrNameRiskItr
										.next();
								if (attributesValues.getAttributeName() != null
										&& attributesValues.getAttributeValue() != null) {
									newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>"
											+ ":" + attributesValues.getAttributeValue() + "<br />");
								} else if (attributesValues.getAttributeValue() == null
										|| attributesValues.getAttributeValue().equalsIgnoreCase("")
												&& attributesValues.getAttributeName() != null) {
									newValue = newValue.append("<b>" + attributesValues.getAttributeName() + "</b>"
											+ ":" + "NA" + "<br />");
								}
							}

							riskProfileHistoryVO.setNewValue(newValue.toString());
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
							riskCodeSet.add(caseCreateRiskAss.getRiskCode());
							break;
						}
					}
				}
			}

			oldRiskCodeSet.removeAll(commonRiskCodeSet);
			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			Set<String> optionalAppliedRiskCodeSet = new HashSet();
			Iterator riskCodeListItr = riskCodeList.iterator();

			while (riskCodeListItr.hasNext()) {
				String riskCode = (String) riskCodeListItr.next();
				if ((Integer) riskCodeWithRiskGroupMap.get(riskCode) == 0
						&& !commonRiskCodeWithOptToMand.contains(riskCode)) {
					optionalAppliedRiskCodeSet.add(riskCode);
				}
			}

			riskCodeList.removeAll(optionalAppliedRiskCodeSet);
			Map deleteRiskDetails = new HashMap();
			deleteRiskDetails.put(this.CRN, subDetails.getCrn());
			deleteRiskDetails.put(this.SUBJECT_ID, subDetails.getSubjectId());
			deleteRiskDetails.put(this.RISK_CODE_LIST, riskCodeList);
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			if (riskCodeList.size() > 0) {
				this.delete(this.DELETE_OLD_RISKS_ON_SUBJECT_EDIT, deleteRiskDetails);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setSubjectId((long) subDetails.getSubjectId());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var22) {
			throw new CMSException(this.logger, var22);
		} catch (Exception var23) {
			throw new CMSException(this.logger, var23);
		}
	}

	public void deleteCaseLevelRisk(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO, String crn,
			List<String> subjectIDStringList) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao deleteCaseLevelRisk");
			SubjectDetails subDetails = new SubjectDetails();
			subDetails.setCrn(crn);
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			Set<String> oldRiskCodeSet = new HashSet();
			new HashSet();
			new HashSet();
			new HashMap();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			new HashSet();
			List<CaseCreationRiskAssociationVO> getRiskDetailsForDeletedSubjectsList = this
					.queryForList("Subject.getCaseLevelRiskDetailsWithoutCntryForDeletedSubjects", subjectIDStringList);
			Iterator getRiskDetailsForDeletedSubjectsItr = getRiskDetailsForDeletedSubjectsList.iterator();

			while (getRiskDetailsForDeletedSubjectsItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) getRiskDetailsForDeletedSubjectsItr
						.next();
				oldRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
				riskCodeWithRiskGroupMap.put(caseCreateRiskAss.getRiskCode(), caseCreateRiskAss.getIsRiskMandatory());
			}

			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			new HashSet();
			Set<Integer> REIdsList = new HashSet();
			Map deleteCaseRiskForSubjectMap = new HashMap();
			deleteCaseRiskForSubjectMap.put(this.CRN, crn);
			deleteCaseRiskForSubjectMap.put("subjectIDStringList", subjectIDStringList);
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this.queryForList("Subject.getSubREstoDelete",
					deleteCaseRiskForSubjectMap);
			Iterator subLevelREItr = subLevelREList.iterator();

			label63 : while (subLevelREItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subLevelREItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (int j = 0; j < subDetArr.length; ++j) {
					REIdsList.add(Integer.parseInt(subDetArr[j]));
				}

				this.logger.debug("--------REIdsList----" + REIdsList);
				if (REIdsList.size() == 0) {
					REIdsList.add(0);
				}

				caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
				List<CaseCreationRiskAssociationVO> allSubRiskDetList = this
						.queryForList(this.getCaseLevelRiskMappingWithoutCntryBrkDownForDelete, caseLevelRiskProfileVO);
				Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

				while (true) {
					while (true) {
						if (!allSubRiskDetItr.hasNext()) {
							continue label63;
						}

						CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr
								.next();
						if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) == subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						} else if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) != subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						}
					}
				}
			}

			Map getProfileIdListMap = new HashMap();
			getProfileIdListMap.put(this.CRN, crn);
			getProfileIdListMap.put(this.RISK_CODE_LIST, riskCodeList);
			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			new HashMap();
			if (riskCodeList.size() > 0) {
				this.delete("Subject.deleteOldCaseRisksOnSubjectEdit", getProfileIdListMap);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var24) {
			throw new CMSException(this.logger, var24);
		} catch (Exception var25) {
			throw new CMSException(this.logger, var25);
		}
	}

	public void deleteISISCaseLevelRisk(SubjectLevelRiskProfileDetailsVO caseLevelRiskProfileVO, String crn,
			int subjectId) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao deleteCaseLevelRisk");
			SubjectDetails subDetails = new SubjectDetails();
			subDetails.setCrn(crn);
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			Set<String> oldRiskCodeSet = new HashSet();
			new HashSet();
			new HashSet();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			new HashMap();
			new HashSet();
			List<CaseCreationRiskAssociationVO> getRiskDetailsForDeletedSubjectsList = this
					.queryForList("Subject.getISISCaseLevelRiskDetailsWithoutCntryForDeletedSubjects", subjectId);
			Iterator getRiskDetailsForDeletedSubjectsItr = getRiskDetailsForDeletedSubjectsList.iterator();

			while (getRiskDetailsForDeletedSubjectsItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) getRiskDetailsForDeletedSubjectsItr
						.next();
				oldRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
				riskCodeWithRiskGroupMap.put(caseCreateRiskAss.getRiskCode(), caseCreateRiskAss.getIsRiskMandatory());
			}

			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			this.logger.debug("Size of riskCodeList--" + riskCodeList.size());
			Set<Integer> REIdsList = new HashSet();
			Map deleteCaseRiskForSubjectMap = new HashMap();
			deleteCaseRiskForSubjectMap.put(this.CRN, crn);
			deleteCaseRiskForSubjectMap.put("subjectId", subjectId);
			List<SubjectLevelRiskProfileDetailsVO> subLevelREList = this
					.queryForList("Subject.getISISSubREstoDeleteWithoutBrkDown", deleteCaseRiskForSubjectMap);
			Iterator subLevelREItr = subLevelREList.iterator();

			int l;
			label63 : while (subLevelREItr.hasNext()) {
				SubjectLevelRiskProfileDetailsVO subReDet = (SubjectLevelRiskProfileDetailsVO) subLevelREItr.next();
				String[] subDetArr = subReDet.getReIdString().split(",");

				for (l = 0; l < subDetArr.length; ++l) {
					REIdsList.add(Integer.parseInt(subDetArr[l]));
				}

				this.logger.debug("--------REIdsList----" + REIdsList);
				if (REIdsList.size() == 0) {
					REIdsList.add(0);
				}

				caseLevelRiskProfileVO.setReIds(new ArrayList(REIdsList));
				List<CaseCreationRiskAssociationVO> allSubRiskDetList = this
						.queryForList(this.getCaseLevelRiskMappingWithoutCntryBrkDownForDelete, caseLevelRiskProfileVO);
				Iterator allSubRiskDetItr = allSubRiskDetList.iterator();

				while (true) {
					while (true) {
						if (!allSubRiskDetItr.hasNext()) {
							continue label63;
						}

						CaseCreationRiskAssociationVO subAllRiskDet = (CaseCreationRiskAssociationVO) allSubRiskDetItr
								.next();
						if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) == subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						} else if (riskCodeList.contains(subAllRiskDet.getRiskCode())
								&& (Integer) riskCodeWithRiskGroupMap.get(subAllRiskDet.getRiskCode()) != subAllRiskDet
										.getIsRiskMandatory()) {
							riskCodeList.remove(subAllRiskDet.getRiskCode());
							subAllRiskDet.setSubjectId((long) subReDet.getSubjectId());
							subAllRiskDet.setCrn(crn);
							this.update("Subject.updateCaseLevelRisk", subAllRiskDet);
						}
					}
				}
			}

			Map getProfileIdListMap = new HashMap();
			getProfileIdListMap.put(this.CRN, crn);
			getProfileIdListMap.put("riskCodeList", riskCodeList);
			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			if (riskCodeList.size() > 0) {
				this.delete("Subject.deleteOldCaseRisksOnSubjectEdit", getProfileIdListMap);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var23) {
			throw new CMSException(this.logger, var23);
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}
	}

	public void updateSubjectLevelRiskHasCntryBrkDown(SubjectLevelRiskProfileDetailsVO subjectLevelRiskProfileVO,
			SubjectDetails subDetails) throws CMSException {
		try {
			this.logger.debug("Inside the Subject Dao updateSubjectLevelRiskHasCntryBrkDown");
			this.logger.debug("sub report type--" + subjectLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ subjectLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ subjectLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> subjectLevelRiskOldDetailsList = this
					.queryForList(this.getSubjectLevelRiskOldDetailsCntryBrkDown, subDetails);
			Iterator<CaseCreationRiskAssociationVO> subLevelRiskOldDetailsItr = subjectLevelRiskOldDetailsList
					.iterator();
			this.logger.debug(
					"subLevelRiskOldDetailsCntryBrkDownItr list size---" + subjectLevelRiskOldDetailsList.size());
			Set<String> oldRiskCodeSet = new HashSet();
			Set<String> oldCountrySet = new HashSet();
			Set<String> commonRiskCodeSet = new HashSet();
			Map<String, Long> riskCodeWithProfileIdMap = new HashMap();
			Map<String, Integer> riskCodeWithRiskGroupMap = new HashMap();
			HashSet commonRiskCodeWithOptToMand = new HashSet();

			while (subLevelRiskOldDetailsItr.hasNext()) {
				CaseCreationRiskAssociationVO subLevelRisOldDet = (CaseCreationRiskAssociationVO) subLevelRiskOldDetailsItr
						.next();
				oldRiskCodeSet.add(subLevelRisOldDet.getRiskCode());
				oldCountrySet.add(subLevelRisOldDet.getCountryCode());
				riskCodeWithProfileIdMap.put(subLevelRisOldDet.getRiskCode(), subLevelRisOldDet.getProfileId());
				riskCodeWithRiskGroupMap.put(subLevelRisOldDet.getRiskCode(), subLevelRisOldDet.getIsRiskMandatory());
			}

			this.logger.debug("Country Set is---" + oldCountrySet.size());
			this.logger.debug("old Country Set is---" + oldCountrySet);
			this.logger.debug("subject entity type--" + subDetails.getEntityTypeId() + "---research elements Type--"
					+ subDetails.getReIds() + "---Country Code---" + subDetails.getCountryCode());
			subjectLevelRiskProfileVO.setSubjectType(subDetails.getEntityTypeId());
			String[] reStringArr = subDetails.getReIds().split(",");
			List<Integer> reList = new ArrayList();

			for (int i = 0; i < reStringArr.length; ++i) {
				reList.add(Integer.parseInt(reStringArr[i]));
			}

			subjectLevelRiskProfileVO.setReIds(reList);
			subjectLevelRiskProfileVO.setSubjectCountry(subDetails.getCountryCode());
			this.logger.debug("Subject Details Research Elements Id---" + subDetails.getReIds());
			List<CaseCreationRiskAssociationVO> list = this
					.queryForList(this.getSubjectLevelRiskMappingWithCntryBrkDown, subjectLevelRiskProfileVO);
			this.logger.debug("Refresh list size is--" + list.size());
			Iterator<CaseCreationRiskAssociationVO> itr = list.iterator();
			boolean var16 = false;

			RiskProfileHistoryVO riskProfileHistoryVO;
			while (itr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
				this.logger.debug("Inside Refresh list Risk code is--" + caseCreateRiskAss.getRiskCode());
				caseCreateRiskAss.setCrn(subDetails.getCrn());
				caseCreateRiskAss.setCountryCode(subDetails.getCountryCode());
				caseCreateRiskAss.setSubjectId((long) subDetails.getSubjectId());
				if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) != caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeWithOptToMand.add(caseCreateRiskAss.getRiskCode());
					this.update("Subject.updateSubjectLevelRisk", caseCreateRiskAss);
				}

				StringBuffer newValue;
				Iterator subRiskOldDetailsItr;
				CaseCreationRiskAssociationVO attibuteValueMap;
				if (!oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())) {
					long profileId = (Long) this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK, caseCreateRiskAss);
					long riskCategoryId = caseCreateRiskAss.getRiskCategoryId();
					List<CaseCreationRiskAssociationVO> attibuteValueMapList = this
							.queryForList(this.GET_ATTRIBUTE_NAME, riskCategoryId);
					subRiskOldDetailsItr = attibuteValueMapList.iterator();
					caseCreateRiskAss.setProfileId(profileId);
					riskProfileHistoryVO = new RiskProfileHistoryVO();
					riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
					riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
					riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
					riskProfileHistoryVO.setAction("Add");
					riskProfileHistoryVO.setTask("Subject Edit");
					riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
					riskProfileHistoryVO.setSubjectId(caseCreateRiskAss.getSubjectId());
					newValue = new StringBuffer("");

					while (true) {
						while (subRiskOldDetailsItr.hasNext()) {
							attibuteValueMap = (CaseCreationRiskAssociationVO) subRiskOldDetailsItr.next();
							caseCreateRiskAss.setAttributeId(attibuteValueMap.getAttributeId());
							caseCreateRiskAss.setAttributeValue(attibuteValueMap.getAttributeValue());
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_COUNTRY_BREAKDOWN, caseCreateRiskAss);
							if (attibuteValueMap.getAttributeName() != null
									&& attibuteValueMap.getAttributeValue() != null) {
								newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":"
										+ attibuteValueMap.getAttributeValue() + "<br />");
							} else if (attibuteValueMap.getAttributeValue() == null
									|| attibuteValueMap.getAttributeValue().equalsIgnoreCase("")
											&& attibuteValueMap.getAttributeName() != null) {
								newValue = newValue.append(
										"<b>" + attibuteValueMap.getAttributeName() + "</b>" + ":" + "NA" + "<br />");
							}
						}

						riskProfileHistoryVO.setNewValue(newValue.toString());
						this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
						break;
					}
				} else if (oldRiskCodeSet.contains(caseCreateRiskAss.getRiskCode())
						&& (Integer) riskCodeWithRiskGroupMap.get(caseCreateRiskAss.getRiskCode()) == caseCreateRiskAss
								.getIsRiskMandatory()) {
					commonRiskCodeSet.add(caseCreateRiskAss.getRiskCode());
					List cntryListForRiskCode = this.queryForList(this.getSubjectLevelCountryForRiskCodeCntryBrkDown,
							caseCreateRiskAss);
					if (!cntryListForRiskCode.contains(subDetails.getCountryCode())) {
						long riskCategoryId = caseCreateRiskAss.getRiskCategoryId();
						List<CaseCreationRiskAssociationVO> attibuteValueMapList = this
								.queryForList(this.GET_ATTRIBUTE_NAME, riskCategoryId);
						Iterator<CaseCreationRiskAssociationVO> attibuteValueMapItr = attibuteValueMapList.iterator();
						subRiskOldDetailsItr = subjectLevelRiskOldDetailsList.iterator();

						while (subRiskOldDetailsItr.hasNext()) {
							CaseCreationRiskAssociationVO subRiskOldDetails = (CaseCreationRiskAssociationVO) subRiskOldDetailsItr
									.next();
							if (subRiskOldDetails.getRiskCode().equals(caseCreateRiskAss.getRiskCode())) {
								caseCreateRiskAss.setProfileId(subRiskOldDetails.getProfileId());
							}
						}

						riskProfileHistoryVO = new RiskProfileHistoryVO();
						riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
						riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
						riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
						riskProfileHistoryVO.setAction("Add");
						riskProfileHistoryVO.setTask("Subject Edit");
						riskProfileHistoryVO.setCountryCode(caseCreateRiskAss.getCountryCode());
						riskProfileHistoryVO.setSubjectId(caseCreateRiskAss.getSubjectId());
						newValue = new StringBuffer("");

						while (true) {
							while (attibuteValueMapItr.hasNext()) {
								attibuteValueMap = (CaseCreationRiskAssociationVO) attibuteValueMapItr.next();
								caseCreateRiskAss.setAttributeId(attibuteValueMap.getAttributeId());
								caseCreateRiskAss.setAttributeValue(attibuteValueMap.getAttributeValue());
								this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_COUNTRY_BREAKDOWN, caseCreateRiskAss);
								if (attibuteValueMap.getAttributeName() != null
										&& attibuteValueMap.getAttributeValue() != null) {
									newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>"
											+ ":" + attibuteValueMap.getAttributeValue() + "<br />");
								} else if (attibuteValueMap.getAttributeValue() == null
										|| attibuteValueMap.getAttributeValue().equalsIgnoreCase("")
												&& attibuteValueMap.getAttributeName() != null) {
									newValue = newValue.append("<b>" + attibuteValueMap.getAttributeName() + "</b>"
											+ ":" + "NA" + "<br />");
								}
							}

							riskProfileHistoryVO.setNewValue(newValue.toString());
							this.insert(this.ASSOCIATE_SUBJECT_LEVEL_RISK_HISTORY, riskProfileHistoryVO);
							break;
						}
					}
				}
			}

			this.logger.debug("old risk code set---" + oldRiskCodeSet.size());
			oldRiskCodeSet.removeAll(commonRiskCodeSet);
			List<String> riskCodeList = new ArrayList(oldRiskCodeSet);
			this.logger.debug("old risk code List---" + riskCodeList.size());
			this.logger.debug("riskCodeWithRiskGroupMap--" + riskCodeWithRiskGroupMap);
			this.logger.debug("oldRiskCodeSet--" + oldRiskCodeSet);
			Set<String> optionalAppliedRiskCodeSet = new HashSet();
			Iterator riskCodeListItr = riskCodeList.iterator();

			while (riskCodeListItr.hasNext()) {
				String riskCode = (String) riskCodeListItr.next();
				if ((Integer) riskCodeWithRiskGroupMap.get(riskCode) == 0
						&& !commonRiskCodeWithOptToMand.contains(riskCode)) {
					optionalAppliedRiskCodeSet.add(riskCode);
				}
			}

			riskCodeList.removeAll(optionalAppliedRiskCodeSet);
			Set<Long> profileSet = new HashSet();

			for (int k = 0; k < riskCodeList.size(); ++k) {
				profileSet.add((Long) riskCodeWithProfileIdMap.get(riskCodeList.get(k)));
			}

			List<Long> profileList = new ArrayList(profileSet);
			this.logger.debug("Risk code list size after remove--" + riskCodeList);
			Map deleteRiskDetails = new HashMap();
			deleteRiskDetails.put(this.CRN, subDetails.getCrn());
			deleteRiskDetails.put(this.SUBJECT_ID, subDetails.getSubjectId());
			deleteRiskDetails.put(this.RISK_CODE_LIST, riskCodeList);
			int numOfRow = 0;
			if (riskCodeList.size() > 0) {
				numOfRow = this.delete("Subject.deleteCaseHasCntryBrkDown", profileList);
			}

			if (numOfRow > 0) {
				this.delete(this.DELETE_OLD_RISKS_ON_SUBJECT_EDIT, deleteRiskDetails);
				riskProfileHistoryVO = new RiskProfileHistoryVO();

				for (int l = 0; l < riskCodeList.size(); ++l) {
					riskProfileHistoryVO.setCrn(subDetails.getCrn());
					riskProfileHistoryVO.setSubjectId((long) subDetails.getSubjectId());
					riskProfileHistoryVO.setRiskCode((String) riskCodeList.get(l));
					riskProfileHistoryVO.setAction("Remove");
					riskProfileHistoryVO.setTask("Subject Edit");
					this.insert(this.ASSOCIATE_SUBJECT_LEVEL_DELETE_RISK_HISTORY, riskProfileHistoryVO);
				}
			}

		} catch (DataAccessException var26) {
			throw new CMSException(this.logger, var26);
		} catch (Exception var27) {
			throw new CMSException(this.logger, var27);
		}
	}

	public int saveColorDetails(String taskName, String crn, String color, String taskStatus) throws CMSException {
		TaskColorVO colorVo = new TaskColorVO();

		try {
			colorVo.setColor(color);
			colorVo.setCrn(crn);
			colorVo.setTaskName(taskName);
			colorVo.setTaskStatus(taskStatus);
			return (Integer) this.insert(this.addColorDetails, colorVo);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public String isWatchListedSubject(String subjectName) throws CMSException {
		this.logger.debug("inside isWatchListedSubject ");
		boolean var2 = false;

		try {
			int flag = (Integer) this.queryForObject(this.getWatchListedSubject, subjectName);
			return flag > 0 ? "Yes" : "No";
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getWatchListedSubject() throws CMSException {
		new ArrayList();

		List watchListSubject;
		try {
			watchListSubject = this.queryForList(this.watchList);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("watchListSubject size is:::" + watchListSubject.size());
		return watchListSubject;
	}

	public String caseManagerId(String crn) throws CMSException {
		this.logger.debug("inside isWatchListedSubject ");
		String caseManager = "";

		try {
			caseManager = (String) this.queryForObject(this.getCaseManagerId, crn);
			return caseManager;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int generateIdForNewSubject() throws CMSException {
		this.logger.debug("inside generateIdForNewSubject ");
		boolean var1 = false;

		try {
			int newSubjectID = (Integer) this.queryForObject(this.generateIdForNewSubject);
			return newSubjectID;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getCountryListForCode(String clientCode) {
		return (String) this.queryForObject(this.getCountryListForCode, clientCode);
	}

	public String getREListForClientCode(String clientCode) {
		return (String) this.queryForObject(this.getREListForClientCode, clientCode);
	}

	public List<SubjectDetails> getListOfSubjectForReplication(Map dataMap) {
		return this.queryForList(this.getListOfSubjectForReplication, dataMap);
	}

	public SubjectDetails getReportTypeForCase(SubjectDetails subDetails) throws CMSException {
		try {
			String crn = subDetails.getCrn();
			this.logger.debug("getReportTypeForCase::" + crn);
			subDetails = (SubjectDetails) this.queryForObject(this.reportTypeForCase, crn);
			return subDetails;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateSubjectBudget(String strSubject, String strBudget) throws CMSException {
		this.logger.debug("In SubjectDAO :updateSubjectBudget method");
		this.logger.debug("SubjectIds:" + strSubject);
		this.logger.debug("Budget :" + strBudget);
		int result = 0;
		HashMap subjects = new HashMap();

		try {
			if (strSubject != null || !strSubject.equalsIgnoreCase("")) {
				subjects.put("strSubject", strSubject);
				subjects.put("strBudget", strBudget);
				subjects.put("strOut", "");
				this.queryForList(this.updateSubjectBudget, subjects);
				this.logger.debug("Subjetc Budget Updated Status: " + subjects.get("strOut"));
				result = Integer.parseInt(subjects.get("strOut").toString());
			}

			return result;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<SubjectDetails> getSubjectDetailsForCRN(String crn) throws CMSException {
		this.logger.debug("Inside subjectDetailsForCRN Method of SubjectDAO class");
		this.logger.debug("crn is " + crn);
		Map subjectMap = new HashMap();
		subjectMap.put("crn", crn);
		this.logger.debug("subjectMap is::::::::" + subjectMap);
		new ArrayList();

		try {
			this.queryForList(this.subjectDetailsForCRN, subjectMap);
			List<SubjectDetails> subjectDetails = this.queryForList(this.subjectDetailsForCRN, subjectMap);
			return subjectDetails;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public SubjectDetails getSlSubreportFlag(String crn) throws CMSException {
		this.logger.debug("Inside getSlSubreportFlag Method of SubjectDAO class");
		this.logger.debug("crn is:::" + crn);
		SubjectDetails subjectDetails = new SubjectDetails();

		try {
			String slSubreportFlag = (String) this.queryForObject(this.slSubreportFlagForCRN, crn);
			subjectDetails.setIsSubjLevelSubRptReq(slSubreportFlag);
			return subjectDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}