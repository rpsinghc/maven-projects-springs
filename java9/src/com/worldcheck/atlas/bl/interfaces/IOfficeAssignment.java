package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.officeassignment.OfficeAssignmentDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.TaskColorVO;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.TeamOfficeVO;
import com.worldcheck.atlas.vo.TeamTypeVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface IOfficeAssignment {
	void setOfficeAssignmentDAO(OfficeAssignmentDAO var1);

	void setPropertyReader(PropertyReaderUtil var1);

	List<TeamTypeVO> getTeamTypes() throws CMSException;

	CaseDetails getCaseDueDates(String var1) throws CMSException;

	List<SubjectDetails> getREDetails(CaseDetails var1) throws CMSException;

	List<SubjectDetails> getCaseSubjectDetails(String var1) throws CMSException;

	List<TeamDetails> getTeamDetails(String var1) throws CMSException;

	boolean isBiREPresent(List<SubjectDetails> var1) throws CMSException;

	boolean isOfficeAssignmentDone(String var1) throws CMSException;

	ModelAndView saveTeamDetails(HttpServletRequest var1) throws CMSException;

	List<TeamDetails> getManagerDetails(String var1) throws CMSException;

	List<TeamDetails> getResearchHeadsForOffices(String var1) throws CMSException;

	String getOfficeWithoutRH(String var1) throws CMSException;

	long getPIDForCRN(String var1) throws CMSException;

	HashMap<String, Object> getAOATeamDetails(List<SubjectDetails> var1, String var2) throws CMSException;

	void saveAOADetailsForCase(HttpServletRequest var1) throws CMSException;

	HashMap<String, Object> getTabCaseDetails(Session var1, MyTaskPageVO var2) throws CMSException;

	HashMap<String, Object> getTabSubjectCountryREDetails(Session var1, MyTaskPageVO var2) throws CMSException;

	List<UserMasterVO> getBIManagerList() throws CMSException;

	List<UserMasterVO> getVendorManagerList() throws CMSException;

	String getCurrentCaseCycle(Session var1, long var2) throws CMSException;

	String getCurrentCaseStatus(String var1) throws CMSException;

	List<CaseDetails> getStatusForCases(List<String> var1) throws CMSException;

	void saveTeamDetailsTab(List<CaseDetails> var1, Session var2, String var3) throws CMSException;

	String checkDueDates(TeamDetails var1, int var2, boolean var3, boolean var4) throws CMSException;

	String checkDueDatesTab(List<CaseDetails> var1) throws CMSException;

	boolean resetTeamOfficeDetails(TeamDetails var1) throws CMSException;

	boolean resetTeamResearchCMPDates(String var1, HashMap<String, String> var2) throws CMSException;

	HashMap<String, CaseDetails> addOfficeDetailsForNewSubject(CaseDetails var1) throws CMSException;

	void addOnlyOfficeDetailsForNewSubject(CaseDetails var1) throws CMSException;

	List<SubTeamReMapVO> getSubjectAssignmentDetails(SubjectDetails var1) throws CMSException;

	HashMap<String, CaseDetails> updateOfficeDetailsForOldSubject(CaseDetails var1) throws CMSException;

	void updateOnlyOfficeDetailsForOldSubject(CaseDetails var1) throws CMSException;

	HashMap<String, CaseDetails> deleteOfficeDetailsForSubject(SubjectDetails var1) throws CMSException;

	CaseDetails deleteBITeamForCase(String var1) throws CMSException;

	boolean isBITeamExistsForCase(String var1) throws CMSException;

	void addInterimCycleToPrimaryTeam(CaseDetails var1, String var2) throws CMSException;

	boolean completeOAForISISCase(String var1, String var2, String var3, String var4) throws CMSException;

	int updatePTDueDates(String var1, String var2, String var3, String var4, String var5) throws CMSException;

	List<TeamDetails> getPTMainAnalystForCases(List<String> var1) throws CMSException;

	CaseDetails getLegacyCaseDetails(String var1) throws CMSException;

	List<TeamDetails> getLegacyTeamDetails(String var1) throws CMSException;

	TeamDetails getBITeamDetails(String var1) throws CMSException;

	CaseDetails getCaseDetails(String var1) throws CMSException;

	int updateCaseManager(String var1, String var2, String var3) throws CMSException;

	CaseDetails getDetailForCase(String var1) throws CMSException;

	TaskColorVO getColorDetails(String var1, String var2) throws CMSException;

	boolean checkClientDueDates(String var1, String var2, String var3, String var4) throws CMSException;

	int updateClientDueDates(String var1, String var2, String var3, String var4) throws CMSException;

	String checkClientDueDatesTab(List<CaseDetails> var1) throws CMSException;

	List<SubjectDetails> getSubjectCountryREDetails(String var1) throws CMSException;

	List<SubjectDetails> getAssociatedSubjectsGCC(String var1) throws CMSException;

	int getDaysBeforePT(String var1, String var2, String var3, String var4) throws CMSException;

	List<String> getIsReplicatedSubjects(String var1) throws CMSException;

	List<TeamOfficeVO> getAutoTeamType() throws CMSException;

	List<TeamOfficeVO> getcountryREList(String var1) throws CMSException;
}