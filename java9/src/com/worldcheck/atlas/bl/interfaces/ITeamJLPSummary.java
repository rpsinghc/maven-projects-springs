package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.TeamJLPSummaryVO;
import java.util.HashMap;
import java.util.List;

public interface ITeamJLPSummary {
	void addTeamJLPTemplate(TeamJLPSummaryVO var1) throws CMSException;

	int updateTeamJLPTemplate(TeamJLPSummaryVO var1) throws CMSException;

	int deleteTeamJLPTemplate(TeamJLPSummaryVO var1) throws CMSException;

	List<TeamJLPSummaryVO> searchTeamJLPTemplate(TeamJLPSummaryVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	boolean isExistTemplateName(String var1) throws CMSException;

	List<UserMasterVO> getAllActiveUsers(TeamJLPSummaryVO var1) throws CMSException;

	List<UserMasterVO> getSelectedUsers(TeamJLPSummaryVO var1) throws CMSException;

	List<UserMasterVO> getAllTemplateCreator() throws CMSException;

	void addToDefaultTeamJLPTemplate(TeamJLPSummaryVO var1) throws CMSException;

	void removeFromDefaultTeamJLPTemplate(TeamJLPSummaryVO var1) throws CMSException;

	List<TeamJLPSummaryVO> fetchJLPReport(HashMap<String, String> var1) throws CMSException;

	int searchTeamJLPTemplateCount(TeamJLPSummaryVO var1) throws CMSException;
}