package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.teamassignment.TeamAssignmentDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface ITeamAssignment {
	void setTeamAssignmentDAO(TeamAssignmentDAO var1);

	List<UserMasterVO> getTeamOfficeMaster(String var1, String var2) throws CMSException;

	List<UserMasterVO> getAnalystForManager(String var1) throws CMSException;

	List<UserMasterVO> getTeamAnalystMaster(String var1, String var2) throws CMSException;

	List<UserMasterVO> getTeamReviewerMaster(String var1, String var2) throws CMSException;

	List<UserMasterVO> getReviewerForAllOffice() throws CMSException;

	TeamDetails getTeamDetails(String var1) throws CMSException;

	TaskColorVO getColorDetails(String var1, String var2) throws CMSException;

	CaseDetails getCaseDetails(String var1) throws CMSException;

	List<Object> getSubjectDetails(String var1) throws CMSException;

	List<SubTeamReMapVO> getUserAssignedRe(String var1, String var2) throws CMSException;

	ModelAndView saveAssignmentTask(HttpServletRequest var1) throws CMSException;

	List<TeamDetails> getTeamDetailsForCase(String var1, String var2);

	List<ResearchElementMasterVO> getTeamRes(String var1);

	List<TeamDetails> getCaseTeamDetails(String var1) throws CMSException;

	ModelAndView reassignTeamAssignmentTask(HttpServletRequest var1) throws CMSException;

	List<SubTeamReMapVO> getAssignedUsers(String var1, String var2) throws CMSException;

	List<TeamDetails> getAllOfficeMaster() throws CMSException;

	List<TeamDetails> getAllReviewerMaster() throws CMSException;

	List<TeamDetails> getTeamSubjectDetails(String var1) throws CMSException;

	List<TeamDetails> getTeamAnalystDetails(String var1) throws CMSException;

	List<SubTeamReMapVO> getAnalystReDetails(String var1) throws CMSException;

	List<TeamDetails> getAllAnalystMaster() throws CMSException;

	ModelAndView saveAssignmentTaskTab(HttpServletRequest var1) throws CMSException;

	String getAssignedTeamsForUserAndCrn(String var1, String var2) throws CMSException;

	String getTeamType(String var1) throws CMSException;

	List<String> getUserRoleForTeam(String var1, int var2) throws CMSException;

	List<TeamDetails> getTeamNamesForCRN(String var1) throws CMSException;

	Integer getPrimaryTeamId(String var1) throws CMSException;

	HashMap<String, Object> getTeamAssignmentTaskDetails(HttpServletRequest var1, MyTaskPageVO var2)
			throws CMSException;

	List<TeamDetails> getMyIncomingTaskRole(String var1) throws CMSException;

	List<String> getTeamAssignmentDone(String var1, Session var2) throws CMSException;

	List<SubTeamReMapVO> getTeamSubREAnalystCount(String var1) throws CMSException;

	int deleteTeamDetails(List<TeamDetails> var1) throws CMSException;

	String updateMainAnalyst(String var1, int var2, String var3) throws CMSException;

	void deleteAnalyst(String var1, int var2, String var3) throws CMSException;

	String getTeamManager(String var1) throws CMSException;

	List<TeamDetails> getTeamDetailsForAnalyst(String var1) throws CMSException;

	List<TeamDetails> getBulkAssignedAnalyst(String var1) throws CMSException;

	List<TeamDetails> getLegacyCaseTeamDetails(String var1) throws CMSException;

	List<TeamDetails> getLegacyTeamDetails(String var1, String var2) throws CMSException;

	boolean checkTeamDueDates(String var1, String var2, String var3, String var4) throws CMSException;

	String checkTeamDueDatesTab(List<TeamDetails> var1) throws CMSException;

	List<TeamDetails> getTeamAssignmentWINames(String var1) throws CMSException;
}