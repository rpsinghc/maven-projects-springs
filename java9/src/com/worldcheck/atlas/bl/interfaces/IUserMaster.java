package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.UserMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.BDMRegionVO;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.UserCountryProfileVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.masters.UserRoleMapVO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;

public interface IUserMaster {
	void setUserMultiActionDAO(UserMultiActionDAO var1);

	List<UserMasterVO> selectAllUser() throws CMSException;

	List<UserRoleMapVO> roleInfo() throws CMSException;

	List<UserMasterVO> searchUser(UserMasterVO var1, int var2, int var3, String var4, String var5) throws CMSException;

	void deactivateUser(String var1, String var2, String var3, String var4, String var5) throws CMSException;

	void addUser(UserMasterVO var1, String var2) throws CMSException;

	List<UserMasterVO> getSubOrdinateList(String var1) throws CMSException;

	List<UserMasterVO> getBackUpList(String var1) throws CMSException;

	List<UserMasterVO> getActiveUserList() throws CMSException;

	List<UserMasterVO> displayDetails(UserMasterVO var1) throws CMSException;

	List<UserMasterVO> displayBackup(String var1, String var2) throws CMSException;

	List<UserMasterVO> getAssociateDetail() throws CMSException;

	int checkOfficeHead(String var1) throws CMSException;

	List<UserMasterVO> getAllUserID() throws CMSException;

	UserMasterVO getUserInfo(String var1) throws CMSException;

	void updateUser(UserMasterVO var1, String var2, int var3) throws CMSException;

	int checkUserId(String var1) throws CMSException;

	List<UserMasterVO> selectSupervisor() throws CMSException;

	void updateDeactiveUser(UserMasterVO var1) throws CMSException;

	List getUserRoleInfo(String var1) throws CMSException;

	List<UserCountryProfileVO> getLocalLangInfo(String var1) throws CMSException;

	int checkBackUp(String var1, String var2) throws CMSException;

	List<UserMasterVO> backupWithotUsr() throws CMSException;

	int searchUserCount(UserMasterVO var1) throws CMSException;

	List<UserMasterVO> searchCountry(CountryMasterVO var1) throws CMSException;

	int searchCountryCount(CountryMasterVO var1) throws CMSException;

	List<UserMasterVO> getReport(Map<String, Object> var1) throws CMSException;

	List<UserMasterVO> getOrganizationChart(Map<String, Object> var1) throws CMSException;

	List<UserMasterVO> getUsersForRole(String var1) throws CMSException;

	boolean insertNewUser(UserMasterVO var1, String var2) throws CMSException;

	String logInUsrOffice(String var1) throws CMSException;

	int chkSupervisorbeforeDeactivateUser(String var1) throws CMSException;

	int chkBackup1BeforeDeactivateUser(String var1) throws CMSException;

	int userFullNameExist(String var1) throws CMSException;

	String checkForR3(String var1) throws CMSException;

	List<UserMasterVO> getUserFullName(List<String> var1) throws CMSException;

	String getUserFullNameFromId(String var1) throws CMSException;

	int chkActiveCaseBeforeDeactivateUser(String var1) throws CMSException;

	int checkAssociatedWithClientBforDeact(String var1) throws CMSException;

	List<UserMasterVO> getSubordinateForUser(String var1) throws CMSException;

	int chkActiveRecurrenceCaseBeforeDeactivateUser(String var1) throws CMSException;

	List<BDMRegionVO> getBDMRegionList() throws CMSException;

	void mapRegionToBDM(String var1, String var2) throws CMSException;

	void updateBdmRegion(String var1, String var2) throws CMSException;

	String getBdmRegion(String var1) throws CMSException;

	void deleteBdmRegionMapping(String var1) throws CMSException;

	int checkForWIPCases(String var1) throws CMSException;

	int getUserLoginAttempt(String var1, int var2) throws CMSException;

	void updatePasswordExpiryDate(String var1) throws CMSException;

	void updateUserLoginAttempt(Map<String, String> var1) throws CMSException;

	boolean isTokenValid(String var1, String var2) throws CMSException;

	String insertTempPassword(String var1, String var2) throws CMSException;

	String updateTempPassword(String var1, String var2, String var3) throws CMSException;

	void removeTempPassword(String var1) throws CMSException;

	boolean checkUserIdForTemp(String var1) throws CMSException;

	String getToken(String var1) throws CMSException;

	UserMasterVO getUserPassExpiryDetails(String var1) throws CMSException;

	String getTempPassword(String var1) throws CMSException;

	String forgotPasswordUpdate(String var1) throws CMSException;

	int getTempPassCount(String var1) throws CMSException;

	void updateUserPasswordExpiryDate(String var1) throws CMSException;

	int getNoDaysBeforeExpiry(String var1) throws CMSException;

	String chkActiveCaseListBeforeDeactivateUser(String var1) throws CMSException;

	boolean isEmailExist(String var1) throws CMSException;

	int getUserHistoryCount(AtlasHistoryVO var1) throws CMSException;

	List<AtlasHistoryVO> getUserHistory(AtlasHistoryVO var1) throws CMSException, SAXException, IOException;
}