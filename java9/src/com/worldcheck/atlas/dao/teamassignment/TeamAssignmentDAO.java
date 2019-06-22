package com.worldcheck.atlas.dao.teamassignment;

import com.worldcheck.atlas.dao.teamassignment.TeamAssignmentDAO.1;
import com.worldcheck.atlas.dao.teamassignment.TeamAssignmentDAO.2;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TeamAssignmentDAO extends SqlMapClientTemplate {
	private static final String TEAM_ASSIGNMENT_GET_BULK_ASSIGNED_ANALYST = "TeamAssignment.getBulkAssignedAnalyst";
	private static final String TEAM_ASSIGNMENT_GET_CASE_STATUS_RESULT_MAP = "TeamAssignment.getCaseStatusResultMap";
	private static final String TEAM_ASSIGNMENT_GET_CSV_ANALYSTS_FOR_TEAM = "TeamAssignment.getCSVAnalystsForTeam";
	private static final String TEAM_ASSIGNMENT_GET_TEAM_MANAGER = "TeamAssignment.getTeamManager";
	private static final String TEAM_ASSIGNMENT_UPDATE_PERFORMER_RE_JLP = "TeamAssignment.updatePerformerReJlp";
	private static final String TEAM_ASSIGNMENT_GET_TEAM_DETAILS_FOR_ANALYIST = "TeamAssignment.getTeamDetailsForAnalyist";
	private static final String TEAM_ASSIGNMENT_GET_USER_RE_FOR_TEAM = "TeamAssignment.getUserReForTeam";
	private static final String TEAM_ASSIGNMENT_GET_ASSIGNED_TEAMS_FOR_USER_AND_CRN = "TeamAssignment.getAssignedTeamsForUserAndCrn";
	private static final String TEAM_ASSIGNMENT_GET_ALL_OFFICE_MASTER = "TeamAssignment.getAllOfficeMaster";
	private static final String TEAM_ASSIGNMENT_GET_CASE_TEAM_SUB_RE_DETAILS = "TeamAssignment.getCaseTeamSubReDetails";
	private static final String TEAM_ASSIGNMENT_GET_COMPLET_CASE_DETAILS = "TeamAssignment.getCompletCaseDetails";
	private static final String TEAM_ASSIGNMENT_DELETE_SUBJECT_RE_MAPPING_FOR_ANALYST = "TeamAssignment.deleteSubjectREMappingForAnalyst";
	private static final String TEAM_ASSIGNMENT_DELETE_SUBJECT_MAPPING_FOR_ANALYST = "TeamAssignment.deleteSubjectMappingForAnalyst";
	private static final String TEAM_ASSIGNMENT_ADD_SUB_TEAM_DETAILS = "TeamAssignment.addSubTeamDetails";
	private static final String TEAM_ASSIGNMENT_UPDATE_TEAM_DETAILS = "TeamAssignment.updateTeamDetails";
	private static final String TEAM_ASSIGNMENT_GET_SUB_TEAM_DETAILS = "TeamAssignment.getSubTeamDetails";
	private static final String OFFICE_MASTER_LIST = "TeamAssignment.getOfficeMaster";
	private static final String ANALYST_MASTER_LIST = "TeamAssignment.getAnalystMaster";
	private static final String ANALYST_MANAGER_LIST = "TeamAssignment.getAnalystForManager";
	private static final String REVIEWER_MASTER_LIST = "TeamAssignment.getReviewerMaster";
	private static final String REVIEWER_LIST = "TeamAssignment.getReviewerList";
	private static final String ANALYST_LIST = "TeamAssignment.getAnalystListTeam";
	private static final String MANAGER_ID = "managerId";
	private static final String GET_TEAM_DETAILS = "TeamAssignment.getTeamDetails";
	private static final String GET_COLOR_DETAILS = "TeamAssignment.getColorDetails";
	private static final String GET_CASE_TEAM_DETAILS = "TeamAssignment.getCaseTeamDetails";
	private static final String GET_CASE_DETAILS = "TeamAssignment.getCaseDetails";
	private static final String TEAM_ASSIGNMENT_UPDATE_SUB_TEAM_DETAILS = "TeamAssignment.updateSubTeamDetails";
	private static final String TEAM_ASSIGNMENT_DELETE_SUB_TEAM_DETAILS = "TeamAssignment.deleteSubTeamDetails";
	private static final String TEAM_ASSIGNMENT_GET_ASSIGNED_DETAILS = "TeamAssignment.getAssignedUserDetails";
	private static final String DELETE_ANALYST_TEAM_DETAILS = "TeamAssignment.deleteAnalystTeamDetails";
	private static final String TEAM_ASSIGNMENT_GET_ALL_REVIEWER_MASTER = "TeamAssignment.getAllOfficeReviewer";
	private static final String GET_TEAM_SUBJECT_DETAILS = "TeamAssignment.getTeamSubjectDetails";
	private static final String GET_ANALYST_RE_DETAILS = "TeamAssignment.getAnalystReDetails";
	private static final String GET_TEAM_ANALYST_DETAILS = "TeamAssignment.getAnalystTeamDetails";
	private static final String TEAM_ASSIGNMENT_GET_ALL_ANALYST_MASTER = "TeamAssignment.getAllOfficeAnalyst";
	private static final String TEAM_ASSIGNMENT_GET_TEAM_TYPE = "TeamAssignment.getTeamType";
	private static final String TEAM_ASSIGNMENT_GET_USER_ROLE_TEAM = "TeamAssignment.getUserRoleForTeam";
	private static final String TEAM_ASSIGNMENT_GET_TEAMNAMES_FOR_CRN = "TeamAssignment.getTeamNamesForCRN";
	private static final String TEAM_ASSIGNMENT_GET_PRIMARY_TEAM_ID = "TeamAssignment.getPrimaryTeamId";
	private static final String TEAM_ASSIGNMENT_GET_MY_INCOMING_TASK_ROLE = "TeamAssignment.getMyIncomingTaskRole";
	private static final String TEAM_ASSIGNMENT_GET_TEAM_SUB_RE_ANALYST_COUNT = "TeamAssignment.getTeamSubREAnalystCount";
	private static final String TEAM_ASSIGNMENT_DELETE_TEAM_DETAILS = "TeamAssignment.deleteTeamDetails";
	private static final String TEAM_ASSIGNMENT_ANALYSTS_TEAM_DETAILS = "TeamAssignment.getAnalystForTeam";
	private static final String GET_LEGACY_CASE_TEAM_DETAILS = "TeamAssignment.getLegacyCaseTeamDetails";
	private static final String TEAM_ASSIGNMENT_GET_LEGACY_CASE_TEAM_SUB_RE_DETAILS = "TeamAssignment.getLegacyCaseTeamSubReDetails";
	private static final String CHECK_HOLIDAY_FOR_TEAM_SQL = "TeamAssignment.isHoliday";
	private static final String CHECK_HOLIDAY_FOR_TEAMS_TAB_SQL = "TeamAssignment.isHolidayCheckForTeams";
	private static final String GET_WORKITEM_NAMES = "TeamAssignment.getTeamAssignmentWINames";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.teamassignment.TeamAssignmentDAO");

	public boolean saveTeamAssignmentSummary(CaseDetails caseDetails) {
		return true;
	}

	public boolean saveREAssignmentSummary(CaseDetails caseDetails) {
		return true;
	}

	public List<UserMasterVO> getTeamOfficeMaster(TeamDetails teamDetailsVO) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> officeMasterList = this.queryForList("TeamAssignment.getOfficeMaster", teamDetailsVO);
			return officeMasterList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getAnalystForManager(String managerId) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> analystForManager = this.queryForList("TeamAssignment.getAnalystForManager", managerId);
			this.logger.debug(" list size in dao " + analystForManager.size());
			return analystForManager;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getTeamAnalystMaster(TeamDetails teamDetailsVO) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> analystMasterList = this.queryForList("TeamAssignment.getAnalystMaster", teamDetailsVO);
			return analystMasterList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getTeamReviewerMaster(TeamDetails teamDetailsVO) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> reviewerMasterList = this.queryForList("TeamAssignment.getReviewerMaster",
					teamDetailsVO);
			return reviewerMasterList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getReviewerForAllOffice() throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> reviewerMasterList = this.queryForList("TeamAssignment.getReviewerList");
			return reviewerMasterList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public TeamDetails getTeamDetails(String teamId) throws CMSException {
		new TeamDetails();

		try {
			TeamDetails teamDetails = (TeamDetails) this.queryForObject("TeamAssignment.getTeamDetails", teamId);
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public CaseDetails getCaseDetails(String crn) throws CMSException {
		new CaseDetails();

		try {
			CaseDetails caseDetails = (CaseDetails) this.queryForObject("TeamAssignment.getCaseDetails", crn);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public TaskColorVO getColorDetails(HashMap param) throws CMSException {
		new TaskColorVO();

		try {
			TaskColorVO colorVO = (TaskColorVO) this.queryForObject("TeamAssignment.getColorDetails", param);
			return colorVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getSubjectDetails(String teamId) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> subTeamReMapVO = this.queryForList("TeamAssignment.getSubTeamDetails", teamId);
			return subTeamReMapVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateTeamDetails(TeamDetails teamDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("TeamAssignment.updateTeamDetails", teamDetails);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateTeamREDetails(SubTeamReMapVO subTeamReMapVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("TeamAssignment.updateSubTeamDetails", subTeamReMapVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int addTeamREDetails(SubTeamReMapVO subTeamReMapVO) throws CMSException {
		this.logger.debug("In side addTeamREDetails");
		boolean var2 = false;

		try {
			int count = (Integer) this.insert("TeamAssignment.addSubTeamDetails", subTeamReMapVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int addTeamREDetailsList(List<SubTeamReMapVO> teamSubjectREDetails) throws CMSException {
      this.logger.debug("Entring TeamAssignmentDAO: addTeamREDetailsList");
      boolean var2 = false;

      try {
         int count = (Integer)this.execute(new 1(this, teamSubjectREDetails));
         this.logger.debug("No of recordes inserted" + count);
         return count;
      } catch (DataAccessException var4) {
         throw new CMSException(this.logger, var4);
      } catch (Exception var5) {
         throw new CMSException(this.logger, var5);
      }
   }

	public int deleteSubTeamDetails(int teamId) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("TeamAssignment.deleteSubTeamDetails", teamId);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getAssignedUsers(SubTeamReMapVO subTeamReMapVO) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> subTeamReMapVOList = this.queryForList("TeamAssignment.getAssignedUserDetails",
					subTeamReMapVO);
			return subTeamReMapVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getCaseTeamDetails(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getCaseTeamDetails", crn);
			this.logger.debug("number of teams " + teamDetails.size());
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteAnalyst(TeamDetails teamDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.delete("TeamAssignment.updateCSVAnalysts", teamDetails);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteSubjectREMappingForAnalyst(HashMap param) throws CMSException {
		try {
			return this.delete("TeamAssignment.deleteSubjectREMappingForAnalyst", param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int deleteSubjectMappingForAnalyst(HashMap param) throws CMSException {
		try {
			return this.delete("TeamAssignment.deleteSubjectMappingForAnalyst", param);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public TeamDetails getCompleteTeamDetails(HashMap param) throws CMSException {
		new TeamDetails();

		try {
			TeamDetails teamDetails = (TeamDetails) this.queryForObject("TeamAssignment.getCaseTeamSubReDetails",
					param);
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getCompleteCaseDetailsList(HashMap<String, List> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> caseDetails = this.queryForList("TeamAssignment.getCompletCaseDetails", paramMap);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getAllOfficeMaster() throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> officeDetails = this.queryForList("TeamAssignment.getAllOfficeMaster");
			return officeDetails;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamDetails> getAllReviewerMaster() throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> reviewerDetails = this.queryForList("TeamAssignment.getAllOfficeReviewer");
			return reviewerDetails;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamDetails> getTeamSubjectDetails(HashMap<String, List> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> caseDetails = this.queryForList("TeamAssignment.getTeamSubjectDetails", paramMap);
			return caseDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getAnalystReDetails(HashMap<String, List> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> analystReDetails = this.queryForList("TeamAssignment.getAnalystReDetails", paramMap);
			return analystReDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getTeamAnalystDetails(HashMap<String, List> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> analystReDetails = this.queryForList("TeamAssignment.getAnalystTeamDetails", paramMap);
			return analystReDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getAllAnalystMaster() throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> analystDetails = this.queryForList("TeamAssignment.getAllOfficeAnalyst");
			return analystDetails;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getAssignedTeamsForUserAndCrn(HashMap<String, String> paramMap) throws CMSException {
		String teamNames = "";

		try {
			teamNames = (String) this.queryForObject("TeamAssignment.getAssignedTeamsForUserAndCrn", paramMap);
			return teamNames;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getTeamType(String teamId) throws CMSException {
		String teamType = "";

		try {
			teamType = (String) this.queryForObject("TeamAssignment.getTeamType", teamId);
			return teamType;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getUserRoleForTeam(HashMap<String, Object> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<String> userRoles = this.queryForList("TeamAssignment.getUserRoleForTeam");
			return userRoles;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getUserReForTeam(HashMap<String, Object> paramMap) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> analystReDetails = this.queryForList("TeamAssignment.getUserReForTeam", paramMap);
			return analystReDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getTeamNamesForCRN(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getTeamNamesForCRN", crn);
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Integer getPrimaryTeamId(String crn) throws CMSException {
		Integer teamId = 0;

		try {
			teamId = (Integer) this.queryForObject("TeamAssignment.getPrimaryTeamId", crn);
			return teamId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getMyIncomingTaskRole(String userId) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getMyIncomingTaskRole", userId);
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubTeamReMapVO> getTeamSubREAnalystCount(String crn) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> subTeamReMapVOs = this.queryForList("TeamAssignment.getTeamSubREAnalystCount", crn);
			return subTeamReMapVOs;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteTeamDetails(List<TeamDetails> teamIdList) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.delete("TeamAssignment.deleteTeamDetails", teamIdList);
			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteAnalystFromTeam(SubTeamReMapVO subTeamReMapVO) {
		return 0;
	}

	public List<SubTeamReMapVO> getAnalystForTeam(String teamId) throws CMSException {
		new ArrayList();

		try {
			List<SubTeamReMapVO> subReDetails = this.queryForList("TeamAssignment.getAnalystForTeam", teamId);
			return subReDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteAnalyst(String analystId, int teamId) {
		return 0;
	}

	public String getNewMainAnalyst(int teamId) {
		return null;
	}

	public String selectAnalystsForTeam(int teamId) throws CMSException {
		String analysts = null;

		try {
			analysts = (String) this.queryForObject("TeamAssignment.getCSVAnalystsForTeam", teamId);
			return analysts;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getTeamManager(String teamId) throws CMSException {
		String teamManager = null;

		try {
			teamManager = (String) this.queryForObject("TeamAssignment.getTeamManager", teamId);
			return teamManager;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteAnalystSubTeamReMap(TeamDetails teamDetails) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.delete("TeamAssignment.deleteAnalystTeamDetails", teamDetails);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getTeamDetailsForAnalyst(String userID) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getTeamDetailsForAnalyist", userID);
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updatePerformerReJlp(SubTeamReMapVO vo) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("TeamAssignment.updatePerformerReJlp", vo);
			return count;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updatePerformerReJlpList(List<SubTeamReMapVO> teamSubjectREDetails) throws CMSException {
      this.logger.debug("Entring TeamAssignmentDAO: updatePerformerReJlpList");
      boolean var2 = false;

      try {
         int count = (Integer)this.execute(new 2(this, teamSubjectREDetails));
         this.logger.debug("No of recordes inserted" + count);
         return count;
      } catch (DataAccessException var4) {
         throw new CMSException(this.logger, var4);
      } catch (Exception var5) {
         throw new CMSException(this.logger, var5);
      }
   }

	public HashMap<String, String> getStatusForCases(List<Integer> selectedIds) throws CMSException {
		new HashMap();

		try {
			HashMap<String, String> teamDetails = (HashMap) this.queryForMap("TeamAssignment.getCaseStatusResultMap",
					selectedIds, "key", "value");
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamDetails> getBulkAssignedAnalyst(List<Integer> teamIds) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getBulkAssignedAnalyst", teamIds);
			return teamDetails;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamDetails> getLegacyCaseTeamDetails(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getLegacyCaseTeamDetails", crn);
			this.logger.debug("number of teams " + teamDetails.size());
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamDetails> getLegacyTeamDetails(TeamDetails teamDetailsVO) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamDetails = this.queryForList("TeamAssignment.getLegacyCaseTeamSubReDetails",
					teamDetailsVO);
			return teamDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean checkTeamDueDates(HashMap<Object, Object> param) throws CMSException {
		boolean isHoliday = false;

		try {
			Integer obj = (Integer) this.queryForObject("TeamAssignment.isHoliday", param);
			if (obj != null && obj > 0) {
				isHoliday = true;
			}

			this.logger.debug("isHoliday::" + isHoliday);
			return isHoliday;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String checkTeamDueDatesTab(String teamType, String crn, String officeId, String rInterim1, String rInterim2,
			String rFinal) throws CMSException {
		boolean isHoliday = false;
		String message = "";
		HashMap param = null;

		try {
			param = new HashMap();
			param.put("officeId", officeId);
			param.put("rInterim1", rInterim1);
			param.put("rInterim2", rInterim2);
			param.put("rFinal", rFinal);
			Integer obj = (Integer) this.queryForObject("TeamAssignment.isHolidayCheckForTeams", param);
			if (obj != null && obj > 0) {
				isHoliday = true;
			}

			if (isHoliday) {
				message = crn + "(" + teamType + ")" + "<br>";
			}

			return message;
		} catch (ClassCastException var11) {
			throw new CMSException(this.logger, var11);
		} catch (DataAccessException var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public List<TeamDetails> getTeamAssignmentWINames(String crn) throws CMSException {
		new ArrayList();

		try {
			List<TeamDetails> teamAssignmentWINames = this.queryForList("TeamAssignment.getTeamAssignmentWINames", crn);
			this.logger.debug("size of teamAssignmentWINames ::" + teamAssignmentWINames.size());
			return teamAssignmentWINames;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}