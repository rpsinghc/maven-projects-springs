package com.worldcheck.atlas.vo;

import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.List;

public class TeamDetails {
	private String crn;
	private int teamId;
	private String teamTypeId;
	private String teamType;
	private String teamName;
	private List<SubTeamReMapVO> teamSubjectREDetails;
	private String reIds;
	private String primarySubName;
	private String primarySubCountry;
	private String byPassInterim;
	private String researchHead;
	private String rhFullName;
	private String dueDate1;
	private String dueDate2;
	private String finalDueDate;
	private String otherOffice;
	private List subList;
	private String reviewer1;
	private String reviewer2;
	private String reviewer3;
	private String rev1FullName;
	private String rev2FullName;
	private String rev3FullName;
	private String otherOffice1;
	private String otherOffice2;
	private String otherOffice3;
	private String managerName;
	private String managerFullName;
	private String office;
	private String officeName;
	private String mainAnalyst;
	private List<String> analyst;
	private List<String> analystId;
	private String performer;
	private List<UserMasterVO> analystList;
	private List<UserMasterVO> managerList;
	private List<String> reviewer;
	private String processCycle;
	private String analysts;
	private String status;
	private String updatedBy;
	private String teamJlpPoints;
	private String workitemName;
	private String scope;
	private String roleType;
	private String csvAnalysts;
	private List<String> teamAssignmentDone;
	private String cinterim1Date;
	private String cinterim2Date;
	private String cfinalDate;
	private String clientDueDates;
	private String researchDueDates;
	private String reqReceivedDate;
	private String primaryTeamDates;

	public String getTeamTypeId() {
		return this.teamTypeId;
	}

	public void setTeamTypeId(String teamTypeId) {
		this.teamTypeId = teamTypeId;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public String getDueDate1() {
		return this.dueDate1;
	}

	public void setDueDate1(String dueDate1) {
		this.dueDate1 = dueDate1;
	}

	public String getDueDate2() {
		return this.dueDate2;
	}

	public void setDueDate2(String dueDate2) {
		this.dueDate2 = dueDate2;
	}

	public String getFinalDueDate() {
		return this.finalDueDate;
	}

	public void setFinalDueDate(String finalDueDate) {
		this.finalDueDate = finalDueDate;
	}

	public String getPrimarySubName() {
		return this.primarySubName;
	}

	public void setPrimarySubName(String primarySubName) {
		this.primarySubName = primarySubName;
	}

	public String getPrimarySubCountry() {
		return this.primarySubCountry;
	}

	public void setPrimarySubCountry(String primarySubCountry) {
		this.primarySubCountry = primarySubCountry;
	}

	public String getManagerName() {
		return this.managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getMainAnalyst() {
		return this.mainAnalyst;
	}

	public void setMainAnalyst(String mainAnalyst) {
		this.mainAnalyst = mainAnalyst;
	}

	public List<String> getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(List<String> analyst) {
		this.analyst = analyst;
	}

	public List<String> getAnalystId() {
		return this.analystId;
	}

	public void setAnalystId(List<String> analystId) {
		this.analystId = analystId;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public List<UserMasterVO> getAnalystList() {
		return this.analystList;
	}

	public void setAnalystList(List<UserMasterVO> analystList) {
		this.analystList = analystList;
	}

	public List getSubList() {
		return this.subList;
	}

	public void setSubList(List subList) {
		this.subList = subList;
	}

	public String getReviewer1() {
		return this.reviewer1;
	}

	public void setReviewer1(String reviewer1) {
		this.reviewer1 = reviewer1;
	}

	public String getReviewer2() {
		return this.reviewer2;
	}

	public void setReviewer2(String reviewer2) {
		this.reviewer2 = reviewer2;
	}

	public String getReviewer3() {
		return this.reviewer3;
	}

	public void setReviewer3(String reviewer3) {
		this.reviewer3 = reviewer3;
	}

	public String getRev1FullName() {
		return this.rev1FullName;
	}

	public void setRev1FullName(String rev1FullName) {
		this.rev1FullName = rev1FullName;
	}

	public String getRev2FullName() {
		return this.rev2FullName;
	}

	public void setRev2FullName(String rev2FullName) {
		this.rev2FullName = rev2FullName;
	}

	public String getRev3FullName() {
		return this.rev3FullName;
	}

	public void setRev3FullName(String rev3FullName) {
		this.rev3FullName = rev3FullName;
	}

	public String getOtherOffice1() {
		return this.otherOffice1;
	}

	public void setOtherOffice1(String otherOffice1) {
		this.otherOffice1 = otherOffice1;
	}

	public String getOtherOffice2() {
		return this.otherOffice2;
	}

	public void setOtherOffice2(String otherOffice2) {
		this.otherOffice2 = otherOffice2;
	}

	public String getOtherOffice3() {
		return this.otherOffice3;
	}

	public void setOtherOffice3(String otherOffice3) {
		this.otherOffice3 = otherOffice3;
	}

	public List<String> getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(List<String> reviewer) {
		this.reviewer = reviewer;
	}

	public String getProcessCycle() {
		return this.processCycle;
	}

	public void setProcessCycle(String processCycle) {
		this.processCycle = processCycle;
	}

	public String getAnalysts() {
		return this.analysts;
	}

	public void setAnalysts(String analysts) {
		this.analysts = analysts;
	}

	public List<UserMasterVO> getManagerList() {
		return this.managerList;
	}

	public void setManagerList(List<UserMasterVO> managerList) {
		this.managerList = managerList;
	}

	public String getOtherOffice() {
		return this.otherOffice;
	}

	public void setOtherOffice(String otherOffice) {
		this.otherOffice = otherOffice;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getByPassInterim() {
		return this.byPassInterim;
	}

	public void setByPassInterim(String byPassInterim) {
		this.byPassInterim = byPassInterim;
	}

	public String getResearchHead() {
		return this.researchHead;
	}

	public void setResearchHead(String researchHead) {
		this.researchHead = researchHead;
	}

	public String getReIds() {
		return this.reIds;
	}

	public void setReIds(String reIds) {
		this.reIds = reIds;
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public List<SubTeamReMapVO> getTeamSubjectREDetails() {
		return this.teamSubjectREDetails;
	}

	public void setTeamSubjectREDetails(List<SubTeamReMapVO> teamSubjectREDetails) {
		this.teamSubjectREDetails = teamSubjectREDetails;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getTeamJlpPoints() {
		return this.teamJlpPoints;
	}

	public void setTeamJlpPoints(String teamJlpPoints) {
		this.teamJlpPoints = teamJlpPoints;
	}

	public String getWorkitemName() {
		return this.workitemName;
	}

	public void setWorkitemName(String workitemName) {
		this.workitemName = workitemName;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getManagerFullName() {
		return this.managerFullName;
	}

	public void setManagerFullName(String managerFullName) {
		this.managerFullName = managerFullName;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getCsvAnalysts() {
		return this.csvAnalysts;
	}

	public void setCsvAnalysts(String csvAnalysts) {
		this.csvAnalysts = csvAnalysts;
	}

	public List<String> getTeamAssignmentDone() {
		return this.teamAssignmentDone;
	}

	public void setTeamAssignmentDone(List<String> teamAssignmentDone) {
		this.teamAssignmentDone = teamAssignmentDone;
	}

	public String getRhFullName() {
		return this.rhFullName;
	}

	public void setRhFullName(String rhFullName) {
		this.rhFullName = rhFullName;
	}

	public void setCinterim1Date(String cinterim1Date) {
		this.cinterim1Date = cinterim1Date;
	}

	public String getCinterim1Date() {
		return this.cinterim1Date;
	}

	public void setCinterim2Date(String cinterim2Date) {
		this.cinterim2Date = cinterim2Date;
	}

	public String getCinterim2Date() {
		return this.cinterim2Date;
	}

	public void setCfinalDate(String cfinalDate) {
		this.cfinalDate = cfinalDate;
	}

	public String getCfinalDate() {
		return this.cfinalDate;
	}

	public void setClientDueDates(String clientDueDates) {
		this.clientDueDates = clientDueDates;
	}

	public String getClientDueDates() {
		return this.clientDueDates;
	}

	public void setResearchDueDates(String researchDueDates) {
		this.researchDueDates = researchDueDates;
	}

	public String getResearchDueDates() {
		return this.researchDueDates;
	}

	public void setReqReceivedDate(String reqReceivedDate) {
		this.reqReceivedDate = reqReceivedDate;
	}

	public String getReqReceivedDate() {
		return this.reqReceivedDate;
	}

	public void setPrimaryTeamDates(String primaryTeamDates) {
		this.primaryTeamDates = primaryTeamDates;
	}

	public String getPrimaryTeamDates() {
		return this.primaryTeamDates;
	}
}