package com.worldcheck.atlas.services.teamassignment;

import com.worldcheck.atlas.bl.interfaces.ITeamAssignment;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CycleInfo;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.TeamDetails;
import java.util.ArrayList;
import java.util.List;

public class TeamAssignmentService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.teamassignment.TeamAssignmentService");
	ITeamAssignment teamAssignmentManager = null;

	public void setTeamAssignmentManager(ITeamAssignment teamAssignmentManager) {
		this.teamAssignmentManager = teamAssignmentManager;
	}

	public List<SubTeamReMapVO> getUserAssignedRe(String userId, String teamId) throws CMSException {
		new ArrayList();
		List<SubTeamReMapVO> reList = this.teamAssignmentManager.getUserAssignedRe(userId, teamId);
		return reList;
	}

	public List<TeamDetails> getTeamRes(String teamId) throws CMSException {
		new ArrayList();
		List<TeamDetails> teamDetails = this.teamAssignmentManager.getTeamSubjectDetails(teamId);
		return teamDetails;
	}

	public String getTeamType(String teamId) throws CMSException {
		return this.teamAssignmentManager.getTeamType(teamId);
	}

	public String getAssignedTeamsForUserAndCrn(String crn, String userId) throws CMSException {
		return this.teamAssignmentManager.getAssignedTeamsForUserAndCrn(crn, userId);
	}

	public List<String> getUserRoleForTeam(String userId, int teamId) throws CMSException {
		return this.teamAssignmentManager.getUserRoleForTeam(userId, teamId);
	}

	public List<TeamDetails> getTeamNamesForCRN(String crn) throws CMSException {
		return this.teamAssignmentManager.getTeamNamesForCRN(crn);
	}

	public Integer getPrimaryTeamId(String crn) throws CMSException {
		return this.teamAssignmentManager.getPrimaryTeamId(crn);
	}

	public List<TeamDetails> getMyIncomingTaskRole(String userId) throws CMSException {
		return this.teamAssignmentManager.getMyIncomingTaskRole(userId);
	}

	public List<SubTeamReMapVO> getTeamSubREAnalystCount(String crn) throws CMSException {
		return this.teamAssignmentManager.getTeamSubREAnalystCount(crn);
	}

	public int deleteTeamDetails(List<TeamDetails> teamIdList) throws CMSException {
		return this.teamAssignmentManager.deleteTeamDetails(teamIdList);
	}

	public String updateMainAnalyst(String analystId, int teamId, String updatedBy) throws CMSException {
		return this.teamAssignmentManager.updateMainAnalyst(analystId, teamId, updatedBy);
	}

	public void deleteAnalyst(String analystId, int teamId, String updatedBy) throws CMSException {
		this.teamAssignmentManager.deleteAnalyst(analystId, teamId, updatedBy);
	}

	public List<TeamDetails> getCaseTeamDetails(String crn) throws CMSException {
		return this.teamAssignmentManager.getCaseTeamDetails(crn);
	}

	public String getTeamManager(String teamId) throws CMSException {
		return this.teamAssignmentManager.getTeamManager(teamId);
	}

	public List<CycleInfo> getTeamCycles(String teamId) throws CMSException {
		List<CycleInfo> cycleList = new ArrayList();
		return cycleList;
	}

	public List<TeamDetails> getTeamDetailsForCase(String userId, String crn) throws CMSException {
		new ArrayList();
		List<TeamDetails> teamList = this.teamAssignmentManager.getTeamDetailsForCase(userId, crn);
		return teamList;
	}

	public List<TeamDetails> getTeamDetailsForAnalyst(String userID) throws CMSException {
		new ArrayList();
		List<TeamDetails> teamList = this.teamAssignmentManager.getTeamDetailsForAnalyst(userID);
		return teamList;
	}
}