package com.worldcheck.atlas.services.officeassignment;

import com.worldcheck.atlas.bl.interfaces.IOfficeAssignment;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class OfficeAssignmentService {
	private IOfficeAssignment officeAssignmentManager;

	public void setOfficeAssignmentManager(IOfficeAssignment officeAssignmentManager) {
		this.officeAssignmentManager = officeAssignmentManager;
	}

	public HashMap<String, CaseDetails> addOfficeDetailsForNewSubject(CaseDetails caseDetails) throws CMSException {
		return this.officeAssignmentManager.addOfficeDetailsForNewSubject(caseDetails);
	}

	public HashMap<String, CaseDetails> updateOfficeDetailsForOldSubject(CaseDetails caseDetails) throws CMSException {
		return this.officeAssignmentManager.updateOfficeDetailsForOldSubject(caseDetails);
	}

	public void addOnlyOfficeDetailsForNewSubject(CaseDetails caseDetails) throws CMSException {
		this.officeAssignmentManager.addOnlyOfficeDetailsForNewSubject(caseDetails);
	}

	public void updateOnlyOfficeDetailsForOldSubject(CaseDetails caseDetails) throws CMSException {
		this.officeAssignmentManager.updateOnlyOfficeDetailsForOldSubject(caseDetails);
	}

	public HashMap<String, CaseDetails> deleteOfficeDetailsForSubject(SubjectDetails subjectDetailsVO)
			throws CMSException {
		return this.officeAssignmentManager.deleteOfficeDetailsForSubject(subjectDetailsVO);
	}

	public CaseDetails deleteBITeamForCase(String crn) throws CMSException {
		return this.officeAssignmentManager.deleteBITeamForCase(crn);
	}

	public boolean isBITeamExistsForCase(String crn) throws CMSException {
		return this.officeAssignmentManager.isBITeamExistsForCase(crn);
	}

	public List<UserMasterVO> getBIManagerList() throws CMSException {
		return this.officeAssignmentManager.getBIManagerList();
	}

	public long getPIDForCRN(String crn) throws CMSException {
		return this.officeAssignmentManager.getPIDForCRN(crn);
	}

	public void saveAOADetailsForCase(HttpServletRequest request) throws CMSException {
		this.officeAssignmentManager.saveAOADetailsForCase(request);
	}

	public boolean checkOfficeAssignmentStatus(String crn) throws CMSException {
		return this.officeAssignmentManager.isOfficeAssignmentDone(crn);
	}

	public List<SubTeamReMapVO> getSubjectAssignmentDetails(SubjectDetails subjectVo) throws CMSException {
		return this.officeAssignmentManager.getSubjectAssignmentDetails(subjectVo);
	}

	public void addInterimCycleToPrimaryTeam(CaseDetails caseDetails, String param) throws CMSException {
		this.officeAssignmentManager.addInterimCycleToPrimaryTeam(caseDetails, param);
	}

	public boolean completeOAForISISCase(String crn, String pid, String userId, String ptResearchHead)
			throws CMSException {
		return this.officeAssignmentManager.completeOAForISISCase(crn, pid, userId, ptResearchHead);
	}

	public boolean resetTeamResearchCMPDates(String crn, HashMap<String, String> teamProcessMapDetails)
			throws CMSException {
		return this.officeAssignmentManager.resetTeamResearchCMPDates(crn, teamProcessMapDetails);
	}

	public boolean resetTeamOfficeDetails(TeamDetails teamDetails) throws CMSException {
		return this.officeAssignmentManager.resetTeamOfficeDetails(teamDetails);
	}

	public int updatePTDueDates(String crn, String rInterim1, String rInterim2, String finalDueDate, String userId)
			throws CMSException {
		return this.officeAssignmentManager.updatePTDueDates(crn, rInterim1, rInterim2, finalDueDate, userId);
	}

	public List<TeamDetails> getPTMainAnalystForCases(List<String> crnDetails) throws CMSException {
		return this.officeAssignmentManager.getPTMainAnalystForCases(crnDetails);
	}

	public TeamDetails getBITeamDetails(String crn) throws CMSException {
		return this.officeAssignmentManager.getBITeamDetails(crn);
	}

	public CaseDetails getDetailForCase(String crn) throws CMSException {
		return this.officeAssignmentManager.getDetailForCase(crn);
	}

	public int updateCaseManager(String crn, String caseMgrId, String userId) throws CMSException {
		return this.officeAssignmentManager.updateCaseManager(crn, caseMgrId, userId);
	}

	public int updateClientDueDates(String crn, String clientFinalDate, String clientInterim1Date,
			String clientInterim2Date) throws CMSException {
		return this.officeAssignmentManager.updateClientDueDates(crn, clientFinalDate, clientInterim1Date,
				clientInterim2Date);
	}

	public int getDaysBeforePT(String clientCode, String reportType, String teamType, String office)
			throws CMSException {
		return this.officeAssignmentManager.getDaysBeforePT(clientCode, reportType, teamType, office);
	}
}