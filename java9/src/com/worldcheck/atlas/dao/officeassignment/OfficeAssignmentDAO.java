package com.worldcheck.atlas.dao.officeassignment;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.TeamOfficeVO;
import com.worldcheck.atlas.vo.TeamTypeVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class OfficeAssignmentDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.officeassignment.OfficeAssignmentDAO");
	private String GET_TEAM_TYPES_SQL = "OfficeAssignment.getTeamTypes";
	private String GET_CASE_DUEDATES_SQL = "OfficeAssignment.getCaseDueDates";
	private String GET_CASE_DETAILS_SQL = "OfficeAssignment.getCaseDetails";
	private String GET_DETAIL_FOR_CASE__SQL = "OfficeAssignment.getDetailForCase";
	private String GET_RE_DETAILS_SQL = "OfficeAssignment.getREDetails";
	private String ADD_TEAM_DETAILS_SQL = "OfficeAssignment.addTeamDetails";
	private String GET_COLOR_DETAILS_SQL = "OfficeAssignment.getColorDetails";
	private String CHECK_HOLIDAY_FOR_CLIENT_SQL = "OfficeAssignment.isHoliday";
	private String UPDATE_PST_DETAILS_SQL = "OfficeAssignment.updatePSTOffice";
	private String UPDATE_BIVT_DETAILS_SQL = "OfficeAssignment.updateBIVTOffice";
	private String ADD_INTERIMS_TO_PT_SQL = "OfficeAssignment.addInterimCyclesToPrimary";
	private String ADD_INTERIM2_TO_PT_SQL = "OfficeAssignment.addInterim2CycleToPrimary";
	private String ADD_INTERIM1_TO_PT_SQL = "OfficeAssignment.addInterim1CycleToPrimary";
	private String GET_TAB_CASE_TEAM_DETAILS_SQL = "OfficeAssignment.getTabCaseTeamDetails";
	private String GET_CRN_WITH_BIRE_SQL = "OfficeAssignment.getCRNWithBIRE";
	private String GET_CRN_WITHSAVED_OFFICE_DETAILS_SQL = "OfficeAssignment.getCRNWithSavedOfficeDetails";
	private String GET_TAB_CASE_SUBJECT_DETAILS_SQL = "OfficeAssignment.getTabCaseSubjectDetails";
	private String GET_PRI_SUB_ASSIGNMENT_DETAILS_SQL = "OfficeAssignment.getPrimarySubjectAssignmentDetails";
	private String DELETE_SUBJECT_OFFICE_DETAILS_SQL = "OfficeAssignment.deleteSubjectOfficeDetails";
	private String DELETE_TEAMS_SQL = "OfficeAssignment.deleteTeams";
	private String GET_SUBJECT_ASSIGNMENT_DETAILS_SQL = "OfficeAssignment.getSubjectAsisgnmentDetails";
	private String DELETE_BITEAM_FORCASE_SQL = "OfficeAssignment.deleteBITeamForCase";
	private String IS_BI_TEAM_EXISTS_FORCASE_SQL = "OfficeAssignment.isBITeamExistsForCase";
	private String GET_BI_USER_DETAILS_SQL = "OfficeAssignment.getUserDetailsForBI";
	private String GET_ANALYST_USER_DETAILS_SQL = "OfficeAssignment.getUserDetailsForAnalyst";
	private String DELETE_SUBJECT_MAPPING_FORTEAM_SQL = "OfficeAssignment.deleteSubjectMappingForTeam";
	private String DELETE_SUBJECT_RE_MAPPING_FORTEAM_SQL = "OfficeAssignment.deleteSubjectREMappingForTeam";
	private String GET_CASE_COMPLETE_DETAILS_SQL = "OfficeAssignment.getCompleteTeamDetails";
	private String GET_TEAM_DETAILS_SQL = "OfficeAssignment.getTeamDetails";
	private String GET_CASESUBJECT_DETAILS_SQL = "OfficeAssignment.getCaseSubjectDetails";
	private String GET_SUBJECT_COUNTRY_RE_DETAILS_SQL = "OfficeAssignment.getSubjectCountryREDetails";
	private String GET_REPLICATED_SUBJECTS_SQL = "OfficeAssignment.getReplicatedSubjects";
	private String GET_AUTO_ADDED_TEAMS_SQL = "OfficeAssignment.getAutoAddedTeams";
	private String GET_PID_FOR_CRN_SQL = "OfficeAssignment.getPIDForCRN";
	private String IS_OADONE_SQL = "OfficeAssignment.isOfficeAssignmentDone";
	private String GET_RH_FOR_ALLOFFICES_SQL = "OfficeAssignment.getAllResearchHeads";
	private String GET_RH_FOR_OFFICE_SQL = "OfficeAssignment.getResearchHeads";
	private String ADD_TEAM_RE_DETAILS_SQL = "OfficeAssignment.addTeamREDetails";
	private String GET_RH_OFFICE_SQL = "OfficeAssignment.getRHOffice";
	private String ADD_BIVT_OFFICE_SQL = "OfficeAssignment.addBIVTOffice";
	private String ADD_PST_OFFICE_SQL = "OfficeAssignment.addPSTOffice";
	private String CHECK_HOLIDAY_FOR_PST_OFFICE_SQL = "OfficeAssignment.checkForPSTHoliday";
	private String CHECK_HOLIDAY_FOR_BIVT_SQL = "OfficeAssignment.checkForBIVTHoliday";
	private String GET_MANAGER_DETAILS_SQL = "OfficeAssignment.getManagerDetails";
	private String DELETE_SUBJECT_RE_MAPPING_FORBIVTTEAM_SQL = "OfficeAssignment.deleteSubjectREMappingForBIVTTeam";
	private String UPDATE_TEAM_RE_JLP_SQL = "OfficeAssignment.updateTeamReJlp";
	private String RESET_TEAM_CMP_DATES_SQL = "OfficeAssignment.resetTeamResearchCMPDates";
	private String RESET_TEAM_DETAILS_SQL = "OfficeAssignment.resetTeamOfficeDetails";
	private String DELETE_TEAM_RE_MAPPING_DETAILS_SQL = "OfficeAssignment.deleteReMappingForTeam";
	private String UPDATE_PT_DUEDATES_SQL = "OfficeAssignment.updatePTDueDates";
	private String GET_STATUS_DETAILS_FOR_CASES_SQL = "OfficeAssignment.getStatusForCases";
	private String GET_PT_MAINANALYST_DETAILS_SQL = "OfficeAssignment.getPTMainAnalystForCases";
	private String GET_INACTIVE_OFFICES_SQL = "OfficeAssignment.getInactiveOffices";
	private String GET_LEGACY_CASE_DETAILS_SQL = "OfficeAssignment.getLegacyCaseDetails";
	private String GET_LEGACY_TEAM_DETAILS_SQL = "OfficeAssignment.getLegacyTeamDetails";
	private String DELETE_TEAM_DETAILS_FORCASE_SQL = "OfficeAssignment.deleteTeamDetailsForCrn";
	private String GET_BI_TEAM_DETAILS_SQL = "OfficeAssignment.getBITeamDetails";
	private String UPDATE_CASE_MANAGER = "OfficeAssignment.updateCaseManager";
	private String UPDATE_CLIENT_DUEDATES_SQL = "OfficeAssignment.updateClientDueDates";
	private String CHECK_HOLIDAY_FOR_CLIENT_TAB_SQL = "OfficeAssignment.checkClientDueDatesTab";
	private String GET_ASSOCIATED_SUBJECT_GCC = "OfficeAssignment.getAssociatedSubjectsGCC";
	private String GET_COUNTRY_RE_LIST = "OfficeAssignment.getCountryREList";
	private String GET_DAYS_BEFORE_PT_RDD = "OfficeAssignment.getDaysBeforePtRdd";
	private String GET_AUTO_ADDED_SUBJECT_LIST = "OfficeAssignment.getAutoAddedSubjectList";
	private String GET_OFFICE_FOR_AUTO_ADDED_SUBJECT = "OfficeAssignment.getOfficesForAutoAddedSubject";
	private String GET_AUTO_POPULATED_CASES = "OfficeAssignment.getAutoPopulatedCases";
	private String GET_TAB_COUNTRY_RE_LIST = "OfficeAssignment.getTabCountryREList";

	public List<TeamTypeVO> getTeamTypes() throws CMSException {
		try {
			return this.queryForList(this.GET_TEAM_TYPES_SQL);
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public CaseDetails getCaseDueDates(String crn) throws CMSException {
		try {
			return (CaseDetails) this.queryForObject(this.GET_CASE_DUEDATES_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public CaseDetails getCaseDetails(String crn) throws CMSException {
		try {
			return (CaseDetails) this.queryForObject(this.GET_CASE_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public CaseDetails getDetailForCase(String crn) throws CMSException {
		try {
			return (CaseDetails) this.queryForObject(this.GET_DETAIL_FOR_CASE__SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<ResearchElementMasterVO> getREDetails(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_RE_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addPSTOfficeDetails(TeamDetails teamDetails) throws CMSException {
		try {
			return (Integer) this.insert(this.ADD_PST_OFFICE_SQL, teamDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addBIVTOfficeDetails(TeamDetails teamDetails) throws CMSException {
		try {
			return (Integer) this.insert(this.ADD_BIVT_OFFICE_SQL, teamDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addTeamDetails(TeamDetails teamDetails) throws CMSException {
		try {
			return (Integer) this.insert(this.ADD_TEAM_DETAILS_SQL, teamDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void updatePSTOfficeDetails(TeamDetails teamDetails) throws CMSException {
		try {
			this.update(this.UPDATE_PST_DETAILS_SQL, teamDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void updateBIVTOfficeDetails(TeamDetails teamDetails) throws CMSException {
		try {
			this.update(this.UPDATE_BIVT_DETAILS_SQL, teamDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addTeamREDetails(SubTeamReMapVO subTeamReMapVO) throws CMSException {
		try {
			return (Integer) this.insert(this.ADD_TEAM_RE_DETAILS_SQL, subTeamReMapVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updateTeamREJLP(SubTeamReMapVO subTeamReMapVO) throws CMSException {
		try {
			return this.update(this.UPDATE_TEAM_RE_JLP_SQL, subTeamReMapVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getResearchHeadsForOffices(String officeIds) throws CMSException {
		try {
			return this.queryForList(this.GET_RH_FOR_OFFICE_SQL, officeIds);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getInactiveOffices(String officeIds) throws CMSException {
		try {
			return this.queryForList(this.GET_INACTIVE_OFFICES_SQL, officeIds);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getAllResearchHeads() throws CMSException {
		try {
			return this.queryForList(this.GET_RH_FOR_ALLOFFICES_SQL);
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public List<TeamDetails> getManagerDetails(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_MANAGER_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isOfficeAssignmentDone(String crn) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject(this.IS_OADONE_SQL, crn).toString());
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (NumberFormatException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public long getPIDForCRN(String crn) throws CMSException {
		try {
			long pid = Long.parseLong(this.queryForObject(this.GET_PID_FOR_CRN_SQL, crn).toString());
			return pid;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NumberFormatException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubjectDetails> getCaseSubjectDetails(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_CASESUBJECT_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<SubjectDetails> getSubjectCountryREDetails(String crn) throws CMSException {
		try {
			this.logger.debug("in getSubjectCountryREDetails::");
			return this.queryForList(this.GET_SUBJECT_COUNTRY_RE_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<String> getIsReplicatedSubjects(String crn) throws CMSException {
		try {
			this.logger.debug("in getIsReplicatedSubjects::");
			return this.queryForList(this.GET_REPLICATED_SUBJECTS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamOfficeVO> getAutoTeamType() throws CMSException {
		try {
			this.logger.debug("in getAutoTeamType::");
			return this.queryForList(this.GET_AUTO_ADDED_TEAMS_SQL);
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public List<TeamDetails> getTeamDetails(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_TEAM_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getCompleteTeamDetails(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_CASE_COMPLETE_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteTeams(HashMap param) throws CMSException {
		try {
			return this.delete(this.DELETE_TEAMS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteSubjectREMappingForTeam(HashMap param) throws CMSException {
		try {
			return this.delete(this.DELETE_SUBJECT_RE_MAPPING_FORTEAM_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteSubjectREMappingForBIVTTeam(HashMap param) throws CMSException {
		try {
			return this.delete(this.DELETE_SUBJECT_RE_MAPPING_FORBIVTTEAM_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteSubjectMappingForTeam(HashMap param) throws CMSException {
		try {
			return this.delete(this.DELETE_SUBJECT_MAPPING_FORTEAM_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<UserMasterVO> getUserDetailsForAnalyst(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_ANALYST_USER_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<UserMasterVO> getUserDetailsForBI(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_BI_USER_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isBITeamExistsForCase(String crn) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject(this.IS_BI_TEAM_EXISTS_FORCASE_SQL, crn).toString());
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (NumberFormatException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteBITeamForCase(String crn) throws CMSException {
		try {
			return this.delete(this.DELETE_BITEAM_FORCASE_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<SubTeamReMapVO> getSubjectAsisgnmentDetails(SubjectDetails subjectDetailsVO) throws CMSException {
		try {
			return this.queryForList(this.GET_SUBJECT_ASSIGNMENT_DETAILS_SQL, subjectDetailsVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteSubjectOfficeDetails(SubjectDetails subjectDetailsVO) throws CMSException {
		try {
			return this.delete(this.DELETE_SUBJECT_OFFICE_DETAILS_SQL, subjectDetailsVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<SubTeamReMapVO> getPrimarySubjectAssignmentDetails(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_PRI_SUB_ASSIGNMENT_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CaseDetails> getTabCaseSubjectDetails(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_TAB_CASE_SUBJECT_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<String> getCRNWithSavedOfficeDetails(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_CRN_WITHSAVED_OFFICE_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<String> getCRNWithBIRE(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_CRN_WITH_BIRE_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CaseDetails> getTabCaseTeamDetails(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_TAB_CASE_TEAM_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addInterim1CycleToPrimary(CaseDetails caseDetails) throws CMSException {
		try {
			return this.update(this.ADD_INTERIM1_TO_PT_SQL, caseDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addInterim2CycleToPrimary(CaseDetails caseDetails) throws CMSException {
		try {
			return this.update(this.ADD_INTERIM2_TO_PT_SQL, caseDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addInterimCyclesToPrimary(CaseDetails caseDetails) throws CMSException {
		try {
			return this.update(this.ADD_INTERIMS_TO_PT_SQL, caseDetails);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public TeamDetails getRHOffice(String researchHead) throws CMSException {
		try {
			return (TeamDetails) this.queryForObject(this.GET_RH_OFFICE_SQL, researchHead);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public boolean checkClientDueDates(HashMap<Object, Object> param) throws CMSException {
		boolean isHoliday = false;

		try {
			Integer obj = (Integer) this.queryForObject(this.CHECK_HOLIDAY_FOR_CLIENT_SQL, param);
			if (null != obj && obj > 0) {
				isHoliday = true;
			}

			return isHoliday;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> checkForPSTHoliday(HashMap<Object, Object> param) throws CMSException {
		try {
			return this.queryForList(this.CHECK_HOLIDAY_FOR_PST_OFFICE_SQL, param);
		} catch (ClassCastException var3) {
			throw new CMSException(this.logger, var3);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<String> checkForBIVTHoliday(HashMap<Object, Object> param) throws CMSException {
		try {
			return this.queryForList(this.CHECK_HOLIDAY_FOR_BIVT_SQL, param);
		} catch (ClassCastException var3) {
			throw new CMSException(this.logger, var3);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resetTeamResearchCMPDates(HashMap<String, String> param) throws CMSException {
		try {
			return this.update(this.RESET_TEAM_CMP_DATES_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int resetTeamOfficeDetails(HashMap<String, Object> param) throws CMSException {
		try {
			return this.update(this.RESET_TEAM_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteReMappingForTeam(HashMap<String, Object> param) throws CMSException {
		try {
			return this.delete(this.DELETE_TEAM_RE_MAPPING_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updatePTDueDates(TeamDetails teamDetails) throws CMSException {
		Integer count = 0;

		try {
			count = this.update(this.UPDATE_PT_DUEDATES_SQL, teamDetails);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		return count;
	}

	public List<CaseDetails> getStatusForCases(HashMap<Object, Object> param) throws CMSException {
		try {
			return this.queryForList(this.GET_STATUS_DETAILS_FOR_CASES_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getPTMainAnalystForCases(HashMap param) throws CMSException {
		try {
			return this.queryForList(this.GET_PT_MAINANALYST_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public CaseDetails getLegacyCaseDetails(String crn) throws CMSException {
		try {
			return (CaseDetails) this.queryForObject(this.GET_LEGACY_CASE_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamDetails> getLegacyTeamDetails(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_LEGACY_TEAM_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteTeamDetailsForCrn(String crn) throws CMSException {
		try {
			return this.delete(this.DELETE_TEAM_DETAILS_FORCASE_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public TeamDetails getBITeamDetails(String crn) throws CMSException {
		try {
			return (TeamDetails) this.queryForObject(this.GET_BI_TEAM_DETAILS_SQL, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public TaskColorVO getColorDetails(HashMap param) throws CMSException {
		try {
			return (TaskColorVO) this.queryForObject(this.GET_COLOR_DETAILS_SQL, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updateCaseManager(CaseDetails caseDetail) throws CMSException {
		Integer count = 0;

		try {
			count = this.update(this.UPDATE_CASE_MANAGER, caseDetail);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		return count;
	}

	public int updateClientDueDates(CaseDetails caseDetail) throws CMSException {
		Integer count = 0;

		try {
			count = this.update(this.UPDATE_CLIENT_DUEDATES_SQL, caseDetail);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		return count;
	}

	public String checkClientDueDatesTab(String crn, String officeId, Date cInterim1, Date cInterim2, Date cFinal)
			throws CMSException {
		boolean isHoliday = false;
		String message = "";
		HashMap param = null;

		try {
			param = new HashMap();
			param.put("officeId", officeId);
			param.put("cInterim1", cInterim1);
			param.put("cInterim2", cInterim2);
			param.put("cFinal", cFinal);
			Integer obj = (Integer) this.queryForObject(this.CHECK_HOLIDAY_FOR_CLIENT_TAB_SQL, param);
			if (null != obj && obj > 0) {
				isHoliday = true;
			}

			if (isHoliday) {
				this.logger.debug("isHoliday ::" + isHoliday);
				message = "" + crn + "<br>";
				this.logger.debug("message::" + message);
			}

			return message;
		} catch (ClassCastException var10) {
			throw new CMSException(this.logger, var10);
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public List<SubjectDetails> getAssociatedSubjectsGCC(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_ASSOCIATED_SUBJECT_GCC, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getDaysBeforePT(String clientCode, String reportType, String teamType, String office)
			throws CMSException {
		this.logger.debug("inside TaskManagementDAO getDaysBeforePT");
		int daysBefore = 0;
		Map<String, String> map = new HashMap();
		map.put("clientCode", clientCode);
		map.put("reportType", reportType);
		map.put("teamType", teamType);
		map.put("office", office);

		try {
			if (null != this.queryForObject(this.GET_DAYS_BEFORE_PT_RDD, map)) {
				daysBefore = (Integer) this.queryForObject(this.GET_DAYS_BEFORE_PT_RDD, map);
			}

			return daysBefore;
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List<String> getAutoAddedSubjectList(String crn) throws CMSException {
		try {
			return this.queryForList(this.GET_AUTO_ADDED_SUBJECT_LIST, crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<String> getOfficesForAutoAddedSubject() throws CMSException {
		try {
			return this.queryForList(this.GET_OFFICE_FOR_AUTO_ADDED_SUBJECT);
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public List<TeamOfficeVO> getcountryREList(String crn) {
		return this.queryForList(this.GET_COUNTRY_RE_LIST, crn);
	}

	public List<CaseDetails> getAutoPopulatedCases(HashMap<Object, Object> param) throws CMSException {
		try {
			return this.queryForList(this.GET_AUTO_POPULATED_CASES, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CaseDetails> getTabCountryREList(HashMap<Object, Object> param) throws CMSException {
		try {
			return this.queryForList(this.GET_TAB_COUNTRY_RE_LIST, param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}