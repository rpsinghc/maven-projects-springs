package com.worldcheck.atlas.vo.masters;

import java.util.List;

public class UserMasterVO {
	private String userID;
	private String office;
	private int isOfficeHead;
	private String username;
	private String reportsTo;
	private String emailID;
	private String designation;
	private String password;
	private String confirmPassword;
	private String backup1;
	private String backup2;
	private String status;
	private String roles;
	private String probation;
	private String country;
	private String toExcel;
	private String supervisor;
	private String localLanguage;
	private String engMedia;
	private String updatedBy;
	private String updatedOn;
	private String userIdVal;
	private String userFullName;
	private String loginId;
	private int countryID;
	private int engMediaCheck;
	private int locLangCheck;
	private int officeId;
	private int userCntryPId;
	private long userMasterId;
	private int roleId;
	private String[] modifiedRecords;
	private List<String> userRoleList;
	private Integer limit;
	private Integer start;
	private String sortColumnName;
	private String sortType;
	private List<UserCountryProfileVO> userCountryProfileList;
	private float jlpPoints;
	private int wipCases;
	private String backupStatus;
	private String supervisorStatus;
	private String officeHead;
	private String localLangCountriesNames;
	private String expiryDate;
	private int isNewUser;
	private String isPasswordExpire;
	private String isAccountLocked;
	private int loginAttempt;
	private String userCreator;
	private String userCreationDate;
	private String statusModifiedBy;
	private String statusModifiedOn;
	private String previousRoles;
	private String roleModifiedBy;
	private String roleModifiedOn;
	private String unlockedBy;
	private String unlockedOn;
	private String bdmRegion;
	private String hdnuId;
	private String hdnuName;
	private String hdnuOffice;
	private String hdnuback1;
	private String hdnuback2;
	private String hdnureportsTo;
	private String hdnuemail;
	private String hdnudesignation;
	private String hdnustatus;
	private String hdnactionUpdate;
	private String hdnupdateDeactive;
	private String hdnuroles;
	private String hdnuprob;
	private String hdnuccode;
	private String hdnuserIdVal;
	private String hdnbdmRegion;
	private String hdnuAccountLocked;
	private int hdnisOfficeHead;
	private String previousEmailId;
	private String emailIDModifiedBy;
	private String emailIDModifiedOn;
	private String previousOffice;
	private String officeModifiedBy;
	private String officeModifiedOn;
	private String previousSupervisor;
	private String supervisorModifiedBy;
	private String supervisorModifiedOn;
	private int teamId;

	public String getPreviousEmailId() {
		return this.previousEmailId;
	}

	public void setPreviousEmailId(String previousEmailId) {
		this.previousEmailId = previousEmailId;
	}

	public String getEmailIDModifiedBy() {
		return this.emailIDModifiedBy;
	}

	public void setEmailIDModifiedBy(String emailIDModifiedBy) {
		this.emailIDModifiedBy = emailIDModifiedBy;
	}

	public String getEmailIDModifiedOn() {
		return this.emailIDModifiedOn;
	}

	public void setEmailIDModifiedOn(String emailIDModifiedOn) {
		this.emailIDModifiedOn = emailIDModifiedOn;
	}

	public String getPreviousOffice() {
		return this.previousOffice;
	}

	public void setPreviousOffice(String previousOffice) {
		this.previousOffice = previousOffice;
	}

	public String getOfficeModifiedBy() {
		return this.officeModifiedBy;
	}

	public void setOfficeModifiedBy(String officeModifiedBy) {
		this.officeModifiedBy = officeModifiedBy;
	}

	public String getOfficeModifiedOn() {
		return this.officeModifiedOn;
	}

	public void setOfficeModifiedOn(String officeModifiedOn) {
		this.officeModifiedOn = officeModifiedOn;
	}

	public String getPreviousSupervisor() {
		return this.previousSupervisor;
	}

	public void setPreviousSupervisor(String previousSupervisor) {
		this.previousSupervisor = previousSupervisor;
	}

	public String getSupervisorModifiedBy() {
		return this.supervisorModifiedBy;
	}

	public void setSupervisorModifiedBy(String supervisorModifiedBy) {
		this.supervisorModifiedBy = supervisorModifiedBy;
	}

	public String getSupervisorModifiedOn() {
		return this.supervisorModifiedOn;
	}

	public void setSupervisorModifiedOn(String supervisorModifiedOn) {
		this.supervisorModifiedOn = supervisorModifiedOn;
	}

	public int getLoginAttempt() {
		return this.loginAttempt;
	}

	public void setLoginAttempt(int loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	public String getIsAccountLocked() {
		return this.isAccountLocked;
	}

	public void setIsAccountLocked(String isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}

	public String getIsPasswordExpire() {
		return this.isPasswordExpire;
	}

	public void setIsPasswordExpire(String isPasswordExpire) {
		this.isPasswordExpire = isPasswordExpire;
	}

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryData(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getIsNewUser() {
		return this.isNewUser;
	}

	public void setIsNewUser(int isNewUser) {
		this.isNewUser = isNewUser;
	}

	public String getOfficeHead() {
		return this.officeHead;
	}

	public void setOfficeHead(String officeHead) {
		this.officeHead = officeHead;
	}

	public String getBackupStatus() {
		return this.backupStatus;
	}

	public void setBackupStatus(String backupStatus) {
		this.backupStatus = backupStatus;
	}

	public String getSupervisorStatus() {
		return this.supervisorStatus;
	}

	public void setSupervisorStatus(String supervisorStatus) {
		this.supervisorStatus = supervisorStatus;
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserFullName() {
		return this.userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserIdVal() {
		return this.userIdVal;
	}

	public void setUserIdVal(String userIdVal) {
		this.userIdVal = userIdVal;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getEngMedia() {
		return this.engMedia;
	}

	public void setEngMedia(String engMedia) {
		this.engMedia = engMedia;
	}

	public List<String> getUserRoleList() {
		return this.userRoleList;
	}

	public void setUserRoleList(List<String> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String[] getModifiedRecords() {
		return this.modifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.modifiedRecords = modifiedRecords;
	}

	public long getUserMasterId() {
		return this.userMasterId;
	}

	public void setUserMasterId(long userMasterId) {
		this.userMasterId = userMasterId;
	}

	public int getUserCntryPId() {
		return this.userCntryPId;
	}

	public void setUserCntryPId(int userCntryPId) {
		this.userCntryPId = userCntryPId;
	}

	public int getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(int officeId) {
		this.officeId = officeId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public int getCountryID() {
		return this.countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}

	public int getEngMediaCheck() {
		return this.engMediaCheck;
	}

	public void setEngMediaCheck(int engMediaCheck) {
		this.engMediaCheck = engMediaCheck;
	}

	public int getLocLangCheck() {
		return this.locLangCheck;
	}

	public void setLocLangCheck(int locLangCheck) {
		this.locLangCheck = locLangCheck;
	}

	public String getUserID() {
		return this.userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReportsTo() {
		return this.reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getEmailID() {
		return this.emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getBackup1() {
		return this.backup1;
	}

	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}

	public String getBackup2() {
		return this.backup2;
	}

	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoles() {
		return this.roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getProbation() {
		return this.probation;
	}

	public void setProbation(String probation) {
		this.probation = probation;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getToExcel() {
		return this.toExcel;
	}

	public void setToExcel(String toExcel) {
		this.toExcel = toExcel;
	}

	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getLocalLanguage() {
		return this.localLanguage;
	}

	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}

	public int getIsOfficeHead() {
		return this.isOfficeHead;
	}

	public void setIsOfficeHead(int isOfficeHead) {
		this.isOfficeHead = isOfficeHead;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public List<UserCountryProfileVO> getUserCountryProfileList() {
		return this.userCountryProfileList;
	}

	public void setUserCountryProfileList(List<UserCountryProfileVO> userCountryProfileList) {
		this.userCountryProfileList = userCountryProfileList;
	}

	public float getJlpPoints() {
		return this.jlpPoints;
	}

	public void setJlpPoints(float jlpPoints) {
		this.jlpPoints = jlpPoints;
	}

	public int getWipCases() {
		return this.wipCases;
	}

	public void setWipCases(int wipCases) {
		this.wipCases = wipCases;
	}

	public void setLocalLangCountriesNames(String localLangCountriesNames) {
		this.localLangCountriesNames = localLangCountriesNames;
	}

	public String getLocalLangCountriesNames() {
		return this.localLangCountriesNames;
	}

	public void setStatusModifiedBy(String statusModifiedBy) {
		this.statusModifiedBy = statusModifiedBy;
	}

	public String getStatusModifiedBy() {
		return this.statusModifiedBy;
	}

	public void setStatusModifiedOn(String statusModifiedOn) {
		this.statusModifiedOn = statusModifiedOn;
	}

	public String getStatusModifiedOn() {
		return this.statusModifiedOn;
	}

	public void setPreviousRoles(String previousRoles) {
		this.previousRoles = previousRoles;
	}

	public String getPreviousRoles() {
		return this.previousRoles;
	}

	public void setRoleModifiedBy(String roleModifiedBy) {
		this.roleModifiedBy = roleModifiedBy;
	}

	public String getRoleModifiedBy() {
		return this.roleModifiedBy;
	}

	public void setRoleModifiedOn(String roleModifiedOn) {
		this.roleModifiedOn = roleModifiedOn;
	}

	public String getRoleModifiedOn() {
		return this.roleModifiedOn;
	}

	public void setHdnuName(String hdnuName) {
		this.hdnuName = hdnuName;
	}

	public String getHdnuName() {
		return this.hdnuName;
	}

	public void setHdnuId(String hdnuId) {
		this.hdnuId = hdnuId;
	}

	public String getHdnuId() {
		return this.hdnuId;
	}

	public void setBdmRegion(String bdmRegion) {
		this.bdmRegion = bdmRegion;
	}

	public String getBdmRegion() {
		return this.bdmRegion;
	}

	public void setHdnisOfficeHead(int hdnisOfficeHead) {
		this.hdnisOfficeHead = hdnisOfficeHead;
	}

	public int getHdnisOfficeHead() {
		return this.hdnisOfficeHead;
	}

	public void setUnlockedBy(String unlockedBy) {
		this.unlockedBy = unlockedBy;
	}

	public String getUnlockedBy() {
		return this.unlockedBy;
	}

	public void setUnlockedOn(String unlockedOn) {
		this.unlockedOn = unlockedOn;
	}

	public String getUnlockedOn() {
		return this.unlockedOn;
	}

	public String getHdnuOffice() {
		return this.hdnuOffice;
	}

	public void setHdnuOffice(String hdnuOffice) {
		this.hdnuOffice = hdnuOffice;
	}

	public String getHdnuback1() {
		return this.hdnuback1;
	}

	public void setHdnuback1(String hdnuback1) {
		this.hdnuback1 = hdnuback1;
	}

	public String getHdnuback2() {
		return this.hdnuback2;
	}

	public void setHdnuback2(String hdnuback2) {
		this.hdnuback2 = hdnuback2;
	}

	public String getHdnureportsTo() {
		return this.hdnureportsTo;
	}

	public void setHdnureportsTo(String hdnureportsTo) {
		this.hdnureportsTo = hdnureportsTo;
	}

	public String getHdnuemail() {
		return this.hdnuemail;
	}

	public void setHdnuemail(String hdnuemail) {
		this.hdnuemail = hdnuemail;
	}

	public String getHdnudesignation() {
		return this.hdnudesignation;
	}

	public void setHdnudesignation(String hdnudesignation) {
		this.hdnudesignation = hdnudesignation;
	}

	public String getHdnustatus() {
		return this.hdnustatus;
	}

	public void setHdnustatus(String hdnustatus) {
		this.hdnustatus = hdnustatus;
	}

	public String getHdnactionUpdate() {
		return this.hdnactionUpdate;
	}

	public void setHdnactionUpdate(String hdnactionUpdate) {
		this.hdnactionUpdate = hdnactionUpdate;
	}

	public String getHdnupdateDeactive() {
		return this.hdnupdateDeactive;
	}

	public void setHdnupdateDeactive(String hdnupdateDeactive) {
		this.hdnupdateDeactive = hdnupdateDeactive;
	}

	public String getHdnuroles() {
		return this.hdnuroles;
	}

	public void setHdnuroles(String hdnuroles) {
		this.hdnuroles = hdnuroles;
	}

	public String getHdnuprob() {
		return this.hdnuprob;
	}

	public void setHdnuprob(String hdnuprob) {
		this.hdnuprob = hdnuprob;
	}

	public String getHdnuccode() {
		return this.hdnuccode;
	}

	public void setHdnuccode(String hdnuccode) {
		this.hdnuccode = hdnuccode;
	}

	public String getHdnuserIdVal() {
		return this.hdnuserIdVal;
	}

	public void setHdnuserIdVal(String hdnuserIdVal) {
		this.hdnuserIdVal = hdnuserIdVal;
	}

	public String getHdnbdmRegion() {
		return this.hdnbdmRegion;
	}

	public void setHdnbdmRegion(String hdnbdmRegion) {
		this.hdnbdmRegion = hdnbdmRegion;
	}

	public String getHdnuAccountLocked() {
		return this.hdnuAccountLocked;
	}

	public void setHdnuAccountLocked(String hdnuAccountLocked) {
		this.hdnuAccountLocked = hdnuAccountLocked;
	}

	public void setUserCreator(String userCreator) {
		this.userCreator = userCreator;
	}

	public String getUserCreator() {
		return this.userCreator;
	}

	public void setUserCreationDate(String userCreationDate) {
		this.userCreationDate = userCreationDate;
	}

	public String getUserCreationDate() {
		return this.userCreationDate;
	}
}