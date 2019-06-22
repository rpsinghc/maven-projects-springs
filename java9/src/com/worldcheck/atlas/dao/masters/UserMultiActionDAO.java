package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.BDMRegionVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.UserCountryProfileVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.masters.UserRoleMapVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class UserMultiActionDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.UserMultiActionDAO");
	private static final String USER_MASTER_GET_USERS_FOR_ROLE = "UserMaster.getUsersForRole";
	private static final String USER_MASTER_GET_USERS_PROFILE = "UserMaster.getProfileForService";
	private static final String USER_MASTER_SELLECT_ALL_USER = "UserMaster.selectAllUser_USER";
	private static final String USER_MASTER_SelectRoleInfo = "UserMaster.selectRoleInfo";
	private static final String USER_MASTER_SearchUser_USER = "UserMaster.searchUser_USER";
	private static final String USER_MASTER_Deactive = "UserMaster.deActivate";
	private static final String USER_MASTER_Active = "UserMaster.activate";
	private static final String zero = "0";
	private static final String one = "1";
	private static final String comma = ",";
	private String country = "country";
	private String englishmedia = "englishmedia";
	private String localsupportVal = "localsupport";
	private String True = "true";
	private String False = "false";
	private String success = "success";
	private static final String USER_MASTER_ADD_NEW_USER = "UserMaster.addNewUser";
	private static final String USER_MASTER_GET_SUBORDINATE_LIST = "UserMaster.getSubOrdinateList";
	private static final String USER_MASTER_GET_BACKUP_LIST = "UserMaster.getBackUpList";
	private static final String USER_MASTER_GET_ACTIVE_USER_LIST = "UserMaster.getActiveUserList";
	private static final String USER_MASTER_SELECT_ADDUSER_DETAILS = "UserMaster.selectAddUserDetails";
	private static final String USER_MASTER_SELECT_BACKUP_DETAILS = "UserMaster.selectBackUpDetails";
	private static final String USER_MASTER_SELECT_DETAIL_FOR_CLIENT = "UserMaster.selectDetailForClient";
	private static final String USER_MASTER_CHECK_OFFICE_HEAD = "UserMaster.checkOfficeHead";
	private static final String USER_MASTER_GET_ALL_USERID = "UserMaster.getAllUserId";
	private static final String USER_MASTER_GET_USER_INFO = "UserMaster.getUserInfo";
	private static final String USER_MASTER_UPDATE_USERINFO = "UserMaster.updateUserInfo";
	private static final String USER_MASTER_CHECK_USERID = "UserMaster.checkUserId";
	private static final String USER_MASTER_SELECT_SUPERVISOR = "UserMaster.selectSupervisor";
	private static final String USER_MASTER_UPDATE_DEACTIVE_USER = "UserMaster.updateDeactiveUser";
	private static final String USER_MASTER_ADD_PROFILE_DETAIL = "UserMaster.addProfileDetail";
	private static final String USER_MASTER_ADD_NEW_USER_ROLE = "UserMaster.addNewUserRole";
	private static final String USER_MASTER_UPDATE_NEW_USER_ROLE = "UserMaster.updateNewUserRole";
	private static final String USER_MASTER_GET_USER_ROLEINFO = "UserMaster.getUserRoleInfo";
	private static final String USER_MASTER_GET_LOCAL_LANGINFO = "UserMaster.getLocalLangInfo";
	private static final String USER_MASTER_CHECK_BACKUP = "UserMaster.checkBackUp";
	private static final String USER_MASTER_SELECT_NOUSER_BACKUP = "UserMaster.selectNoUsrBak";
	private static final String USER_MASTER_DELETE_NEW_USER_ROLE = "UserMaster.deleteNewUserRole";
	private static final String USER_MASTER_SEARCH_USER_USERCOUNT = "UserMaster.searchUser_USER_count";
	private static final String USER_MASTER_COUNTRY_MASTER_DETAIL = "UserMaster.COUNTRY_MASTER_DETAIL";
	private static final String USER_MASTER_COUNTRY_MASTER_DETAILCOUNT = "UserMaster.COUNTRY_MASTER_DETAIL_COUNT";
	private static final String USER_MASTER_DELETE_COUNTRY_PROFILE = "UserMaster.deleteCntryProfile";
	private static final String USER_MASTER_EXPORTTOXL_USER = "UserMaster.exportToXl_USER";
	private static final String USER_MASTER_LOGIN_USER_OFFICE = "UserMaster.logInUsrOffice";
	private static final String USER_MASTER_CHK_SUPERVISOR_BEFOR_DEACTIVE = "UserMaster.chkSupervisorBeforeDeactivateUser";
	private static final String USER_MASTER_CHK_BKUP_BEFOR_DEACTIVE = "UserMaster.chkBackup1BeforeDeactivateUser";
	private static final String USER_MASTER_CHK_ACTIVECASE_BEFOR_DEACTIVE = "UserMaster.chkActiveCaseBeforeDeactivateUser";
	private static final String USER_MASTER_CHK_RECURRENCE_ACTIVECASE_BEFOR_DEACTIVE = "UserMaster.chkRecurrenceActiveCaseBeforeDeactivateUser";
	private static final String USER_MASTER_CHK_ASSOCIATEDWITHCLIENT_BEFOR_DEACTIVE = "UserMaster.chkaAssociatedWithClientBeforeDeactivateUser";
	private static final String USER_MASTER_USER_FULLNAME_EXIST = "UserMaster.userFullNameExist";
	private static final String USER_MASTER_DELETE_NEWUSER_ROLE = "UserMaster.deleteNewUserRole";
	private static final String USER_MASTER_SEARCHUSER_USERCOUNT = "UserMaster.searchUser_USER_count";
	private static final String USER_MASTER_COUNTRY_MASTER_DETAILS = "UserMaster.COUNTRY_MASTER_DETAIL";
	private static final String USER_MASTER_COUNTRY_MASTER_DETAIL_COUNT = "UserMaster.COUNTRY_MASTER_DETAIL_COUNT";
	private static final String USER_MASTER_DELETE_COUNTRYPROFILE = "UserMaster.deleteCntryProfile";
	private static final String USER_MASTER_EXPORT_TO_EXCEL = "UserMaster.exportToXl_USER";
	private static final String USER_MASTER_ORGANIZATIONCHART_EXCEL = "UserMaster.organizationChart";
	private static final String USER_MASTER_USER_WITH_ROLE33_EXIST = "UserMaster.userWithRoleR3";
	private static final String USER_MASTER_USER_FULLNAME = "UserMaster.getUsersFullName";
	private static final String USER_MASTER_USER_FULLNAME_ID = "UserMaster.getUsersFullNameFromUserId";
	private static final String USER_MASTER_USER_SUBORDINATES = "UserMaster.getSubordinateForUser";
	private String SUCCESS = "success";
	private static final String GET_BDM_REGION_LIST = "UserMaster.getBDMRegionList";
	private static final String MAP_REGION_TO_BDM = "UserMaster.mapRegionToBDM";
	private static final String UPDATE_BDM_REGION = "UserMaster.updateBdmRegion";
	private static final String GET_BDM_REGION = "UserMaster.getBdmRegion";
	private static final String DELETE_BDM_REGION_MAPPING = "UserMaster.deleteBdmRegionMapping";
	private static final String CHECK_FOR_WIP_CASES_FOR_BDM = "UserMaster.checkForWIPcasesForBDM";
	private static final String CHECK_LOGIN_ATTEMPT = "UserMaster.checkLoginAttempt";
	private static final String UPDATE_LOGIN_ATTEMPT = "UserMaster.updateLoginAttempt";
	private static final String UPDATE_User_PASS_EXP_DATE = "UserMaster.updateUserPasswordExpiryDate";
	private static final String RESET_LOGIN_ATTEMPT = "UserMaster.resetLoginAttempt";
	private static final String INSERT_TEMP_PASS = "UserMaster.addTempPassword";
	private static final String UPDATE_TEMP_PASS = "UserMaster.updateTempPassword";
	private static final String CHECK_TOKEN = "UserMaster.checktoken";
	private static final String REMOVE_TEMP_PASS = "UserMaster.removeTempPassword";
	private static final String CHECK_USER_ID_TEMP = "UserMaster.checkUserIdforTemp";
	private static final String GET_TOKEN = "UserMaster.getToken";
	private static final String GET_USER_PASS_EXPIRY_DETAILS = "UserMaster.getpasswordExpiry";
	private static final String GET_TEMP_PASSS = "UserMaster.getTempPassword";
	private static final String UPDATE_FORCE_PASSWORD = "UserMaster.forgotPasswordUpdate";
	private static final String two = "2";
	private static final String TEMPPASSWORDCOUNT = "UserMaster.getTempCountPassword";
	private static final String UPDATE_USER_PROFILE_PASS_EXP_DATE = "UserMaster.updateUserProfileExpiryDate";
	private static final String NOOFDAYSBEFOREEXPIRE = "UserMaster.getNoDaysBeforeExpiry";
	private static final String USER_MASTER_CHK_ACTIVECASELIST_BEFOR_DEACTIVE = "UserMaster.chkActiveCaseListBeforeDeactivateUser";
	private static final String isEmailExit = "UserMaster.checkEmailExist";

	public List<UserMasterVO> selectAllUser() throws CMSException {
		new UserMasterVO();
		new ArrayList();
		this.logger.debug("Inside the Select All Dao Now");

		try {
			List<UserMasterVO> mv = this.queryForList("UserMaster.selectAllUser_USER");
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserRoleMapVO> roleInfo() throws CMSException {
		new UserRoleMapVO();
		new ArrayList();
		this.logger.debug("Inside the Select All Role Dao Now");

		try {
			List<UserRoleMapVO> mv = this.queryForList("UserMaster.selectRoleInfo");
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> searchUser(UserMasterVO userMasterVO) throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for Offices");

		try {
			mv = this.queryForList("UserMaster.searchUser_USER", userMasterVO);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deactivateUser(Map<String, String> map) throws CMSException {
		int reternval = 0;

		try {
			if (((String) map.get("ustatus")).equalsIgnoreCase("0")) {
				this.logger.debug("inside status 0");
				reternval = this.update("UserMaster.deActivate", map);
			}

			if (((String) map.get("ustatus")).equalsIgnoreCase("1")) {
				this.logger.debug("inside status 1");
				reternval = this.update("UserMaster.activate", map);
			}

			return reternval;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void addNewUser(UserMasterVO userMasterVO) throws CMSException {
		try {
			this.logger.debug("supervisor ::::   " + userMasterVO.getReportsTo());
			this.logger.debug("getBackup1 ::::   " + userMasterVO.getBackup1());
			this.logger.debug("getBackup2 ::::   " + userMasterVO.getBackup2());
			this.logger.debug("getIsOfficeHead ::::   " + userMasterVO.getIsOfficeHead());
			this.logger.debug("getStatus ::::   " + userMasterVO.getStatus());
			this.logger.debug("getStatus ::::   " + userMasterVO.getOffice());
			this.logger.debug("Probition val ::::" + userMasterVO.getProbation());
			this.getSqlMapClient().startTransaction();
			this.logger.debug("Inside the Add new User Dao");
			this.insert("UserMaster.addNewUser", userMasterVO);
			this.getSqlMapClient().commitTransaction();
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> getSubOrdinateList(String userName) throws CMSException {
		List subOrdinateList = null;

		try {
			new ArrayList();
			subOrdinateList = this.queryForList("UserMaster.getSubOrdinateList", userName);
			return subOrdinateList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getBackUpList(String userName) throws CMSException {
		List subOrdinateList = null;

		try {
			new ArrayList();
			subOrdinateList = this.queryForList("UserMaster.getBackUpList", userName);
			return subOrdinateList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getActiveUserList() throws CMSException {
		List activeUser = null;

		try {
			new ArrayList();
			activeUser = this.queryForList("UserMaster.getActiveUserList");
			return activeUser;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> displayDetails(UserMasterVO userMasterVO) throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for DD on Add_User");

		try {
			mv = this.queryForList("UserMaster.selectAddUserDetails", userMasterVO);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> displayBackup(String officeCode, String userToUpdate) throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for DD on Add_User==" + officeCode);

		try {
			this.logger.debug("Value of Office code and userId" + officeCode + userToUpdate);
			HashMap<String, String> hmapBackup = new HashMap();
			hmapBackup.put("officeCode", officeCode);
			hmapBackup.put("userToUpdate", userToUpdate);
			mv = this.queryForList("UserMaster.selectBackUpDetails", hmapBackup);
			this.logger.debug("" + mv.size());
			return mv;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<UserMasterVO> getAssociateDetail() throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for DD on Add_User");

		try {
			mv = this.queryForList("UserMaster.selectDetailForClient");
			this.logger.debug("" + mv.size());
			return mv;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int checkOfficeHead(String officeCode) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("UserMaster.checkOfficeHead", officeCode);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getAllUserID() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("UserMaster.getAllUserId");
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public UserMasterVO getUSerInfo(String userId) throws CMSException {
		UserMasterVO vo = null;

		try {
			vo = (UserMasterVO) this.queryForObject("UserMaster.getUserInfo", userId);
			return vo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateUser(UserMasterVO userMasterVO) throws CMSException {
		this.logger.debug("innside dao for user updation >>>>>>>>>>>>>>>>>   ");
		boolean var2 = false;

		try {
			int count = this.update("UserMaster.updateUserInfo", userMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkUserId(String userID) throws CMSException {
		this.logger.debug("innside dao for check userId  >>>>>>>>>>>>>>>>>   " + userID);
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("UserMaster.checkUserId", userID);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> selectSupervisor() throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for Supervisor");

		try {
			mv = this.queryForList("UserMaster.selectSupervisor");
			return mv;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateDeactiveUser(UserMasterVO userMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("UserMaster.updateDeactiveUser", userMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Object addProfileDetail(UserMasterVO userMasterVO) throws CMSException {
		try {
			Object insertedObject = this.insert("UserMaster.addProfileDetail", userMasterVO);
			return insertedObject;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Object addNewUserRole(UserMasterVO userMasterVO) throws CMSException {
		try {
			Object insertedObject = this.insert("UserMaster.addNewUserRole", userMasterVO);
			return insertedObject;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public Object updateNewUserRole(UserMasterVO userMasterVO) throws CMSException {
		try {
			Object insertedObject = this.insert("UserMaster.updateNewUserRole", userMasterVO);
			return insertedObject;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List getUserRoleInfo(String userId) throws CMSException {
		try {
			this.logger.debug("===========inside Role Dao========");
			this.logger.debug("userId:" + userId);
			List<String> rvo = this.queryForList("UserMaster.getUserRoleInfo", userId);
			this.logger.debug("" + rvo.size());
			return rvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserCountryProfileVO> getLocalLangInfo(String userId) throws CMSException {
		List lvo = null;

		try {
			this.logger.debug("===========inside Role Dao========");
			this.logger.debug("userId:" + userId);
			lvo = this.queryForList("UserMaster.getLocalLangInfo", userId);
			this.logger.debug("" + lvo.size());
			return lvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkBackUp(String officeCode, String userToUpdate) throws CMSException {
		boolean var3 = false;

		try {
			HashMap<String, String> hmapBackup = new HashMap();
			hmapBackup.put("officeCode", officeCode);
			hmapBackup.put("userToUpdate", userToUpdate);
			int val = (Integer) this.queryForObject("UserMaster.checkBackUp", hmapBackup);
			return val;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<UserMasterVO> backupWithotUsr() throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for DD on Add_BAckup no user");

		try {
			mv = this.queryForList("UserMaster.selectNoUsrBak");
			this.logger.debug("" + mv.size());
			return mv;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteNewUserRole(UserMasterVO userMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.delete("UserMaster.deleteNewUserRole", userMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchUserCount(UserMasterVO userMasterVO) throws CMSException {
		this.logger.debug("inside searchUserCount");

		try {
			int count = (Integer) this.queryForObject("UserMaster.searchUser_USER_count", userMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> searchCountry(CountryMasterVO countryMasterVO) throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for Offices");

		try {
			mv = this.queryForList("UserMaster.COUNTRY_MASTER_DETAIL", countryMasterVO);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchCountryCount(CountryMasterVO countryMasterVO) {
		return (Integer) this.queryForObject("UserMaster.COUNTRY_MASTER_DETAIL_COUNT", countryMasterVO);
	}

	public int deleteCntryProfile(UserMasterVO userMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.delete("UserMaster.deleteCntryProfile", userMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList("UserMaster.exportToXl_USER", excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getOrganizationChart(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList("UserMaster.organizationChart", excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getUsersForRole(String roleId) throws CMSException {
		List<UserMasterVO> mv = null;
		this.logger.debug("Inside the Dao for getting users for given role");

		try {
			mv = this.queryForList("UserMaster.getUsersForRole", roleId);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String insertNewUser(UserMasterVO userMasterVO) throws CMSException {
		try {
			this.logger.debug("supervisor ::::   " + userMasterVO.getReportsTo());
			this.logger.debug("getBackup1 ::::   " + userMasterVO.getBackup1());
			this.logger.debug("getBackup2 ::::   " + userMasterVO.getBackup2());
			this.logger.debug("getIsOfficeHead ::::   " + userMasterVO.getIsOfficeHead());
			this.logger.debug("getStatus ::::   " + userMasterVO.getStatus());
			this.logger.debug("getStatus ::::   " + userMasterVO.getOffice());
			this.logger.debug("Probition val ::::" + userMasterVO.getProbation());
			this.logger.debug("Inside the Add new User Dao");
			this.insert("UserMaster.addNewUser", userMasterVO);
		} catch (DataAccessException var3) {
			this.logger.debug("DATA Access Exception");
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			this.logger.debug("Exception");
			throw new CMSException(this.logger, var4);
		}

		return this.SUCCESS;
	}

	public String logInUsrOffice(String userName) throws CMSException {
		try {
			return (String) this.queryForObject("UserMaster.logInUsrOffice", userName);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer chkSupervisorbeforeDeactivateUser(String userID) throws CMSException {
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("UserMaster.chkSupervisorBeforeDeactivateUser", userID);
			this.logger.debug("cont value of sup========" + count);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return count;
	}

	public Integer chkBackup1BeforeDeactivateUser(String userID) throws CMSException {
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("UserMaster.chkBackup1BeforeDeactivateUser", userID);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return count;
	}

	public int userFullNameExist(String userFullName) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("User full name exist " + userFullName);
			int count = (Integer) this.queryForObject("UserMaster.userFullNameExist", userFullName);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String checkForR3(String userID) throws CMSException {
		try {
			this.logger.debug("User Role R3 Exist " + userID);
			String loginId = (String) this.queryForObject("UserMaster.userWithRoleR3", userID);
			return loginId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getUserFullName(List<String> users) throws CMSException {
		new ArrayList();

		try {
			HashMap params = new HashMap();
			params.put("users", users);
			this.logger.debug("User Role R3 Exist " + users);
			List<UserMasterVO> usersInfo = this.queryForList("UserMaster.getUsersFullName", params);
			return usersInfo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getUserFullNameFromId(String userid) throws CMSException {
		String userFullName = null;

		try {
			userFullName = (String) this.queryForObject("UserMaster.getUsersFullNameFromUserId", userid);
			return userFullName;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int chkActiveCaseBeforeDeactivateUser(String userID) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("inside Active Case Dao" + userID);
			int count = (Integer) this.queryForObject("UserMaster.chkActiveCaseBeforeDeactivateUser", userID);
			this.logger.debug("count is ==>" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String chkActiveCaseListBeforeDeactivateUser(String userID) throws CMSException {
		String caseList = "";

		try {
			this.logger.debug("inside Active Case List Dao" + userID);
			caseList = this.queryForList("UserMaster.chkActiveCaseListBeforeDeactivateUser", userID).toString();
			this.logger.debug("CaseList is ==>" + caseList);
			return caseList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkAssociatedWithClientBforDeact(String userID) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("inside Associated with Clietn Dao" + userID);
			int count = (Integer) this.queryForObject("UserMaster.chkaAssociatedWithClientBeforeDeactivateUser",
					userID);
			this.logger.debug("count is-" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateOfficeHead(String userID, int status, String updated_By) throws CMSException {
		this.logger.debug("start : updateOfficeHead");
		this.logger.debug("userID:" + userID + "  status: " + status);
		int updated = false;
		UserMasterVO umvo = new UserMasterVO();
		umvo.setUserID(userID);
		umvo.setStatus(String.valueOf(status));
		umvo.setUpdatedBy(updated_By);

		int updated;
		try {
			updated = this.update("BOMaster.updateOfficeHead", umvo);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}

		this.logger.debug("exit : updateOfficeHead");
		return updated;
	}

	public List<UserMasterVO> getSubordinateForUser(String user) throws CMSException {
		new ArrayList();

		try {
			this.logger.debug("User is " + user);
			List<UserMasterVO> usersInfo = this.queryForList("UserMaster.getSubordinateForUser", user);
			return usersInfo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int chkActiveRecurrenceCaseBeforeDeactivateUser(String userID) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("inside Recurrence Case Dao" + userID);
			int count = (Integer) this.queryForObject("UserMaster.chkRecurrenceActiveCaseBeforeDeactivateUser", userID);
			this.logger.debug("Recurrence Case count is ==>" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<BDMRegionVO> getBDMRegionList() {
		List<BDMRegionVO> bdmRegionList = this.queryForList("UserMaster.getBDMRegionList");
		return bdmRegionList;
	}

	public void mapRegionToBDM(String username, String bdmRegion) {
		HashMap<String, String> params = new HashMap();
		params.put("username", username);
		params.put("bdmRegion", bdmRegion);
		this.insert("UserMaster.mapRegionToBDM", params);
	}

	public void updateBdmRegion(String username, String bdmRegion) {
		HashMap<String, String> params = new HashMap();
		params.put("username", username);
		params.put("bdmRegion", bdmRegion);
		this.update("UserMaster.updateBdmRegion", params);
	}

	public String getBdmRegion(String username) {
		this.logger.debug("username is >> " + username);
		return (String) this.queryForObject("UserMaster.getBdmRegion", username);
	}

	public void deleteBdmRegionMapping(String username) {
		this.delete("UserMaster.deleteBdmRegionMapping", username);
	}

	public int checkForWIPCases(String userID) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("UserMaster.checkForWIPcasesForBDM", userID);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getUserLoginAttempt(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : getUserLoginAttempt");
		boolean var2 = false;

		try {
			int loginAttempt = (Integer) this.queryForObject("UserMaster.checkLoginAttempt", userId);
			return loginAttempt;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateUserLoginAttempt(Map<String, String> map) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : updateUserLoginAttempt");

		try {
			this.logger.debug((String) map.get("isReset"));
			if (!((String) map.get("isReset")).equals("true")) {
				this.logger.debug(
						"in IF :::::::::::: " + (String) map.get("userID") + " " + (String) map.get("unlocked_by"));
				this.update("UserMaster.updateLoginAttempt", map);
			} else {
				this.logger.debug(
						"in ELSE :::::::::::: " + (String) map.get("userID") + " " + (String) map.get("unlocked_by"));
				this.update("UserMaster.resetLoginAttempt", map);
			}

		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateUserPasswordExpDate(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : updateUserPasswordExpDate");

		try {
			this.update("UserMaster.updateUserPasswordExpiryDate", userId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String insertTempPassword(String token, String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : insertTempPassword");

		try {
			HashMap<String, String> map = new HashMap();
			map.put("userId", userId);
			map.put("token", token);
			this.insert("UserMaster.addTempPassword", map);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return this.SUCCESS;
	}

	public String updateTempPassword(String token, String userId, String tempPass) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : updateTempPassword");

		try {
			HashMap<String, String> map = new HashMap();
			map.put("userId", userId);
			map.put("token", token);
			map.put("password", tempPass);
			this.insert("UserMaster.updateTempPassword", map);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		return this.SUCCESS;
	}

	public boolean isTokenValid(String token, String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : isTokenValid");

		try {
			HashMap<String, String> map = new HashMap();
			map.put("userId", userId);
			map.put("token", token);
			int count = (Integer) this.queryForObject("UserMaster.checktoken", map);
			return count > 0;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int removeTempPassword(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO: removeTempPassword");

		try {
			int deletedRow = Integer.valueOf(this.delete("UserMaster.removeTempPassword", userId));
			return deletedRow;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean checkUserIdforTemp(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : isTokenValid");

		try {
			int count = (Integer) this.queryForObject("UserMaster.checkUserIdforTemp", userId);
			return count > 0;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getToken(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : getToken");

		try {
			String token = (String) this.queryForObject("UserMaster.getToken", userId);
			return token;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public UserMasterVO getUserPassExpiryDetails(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : getUserPassExpiryDetails");
		UserMasterVO vo = null;

		try {
			vo = (UserMasterVO) this.queryForObject("UserMaster.getpasswordExpiry", userId);
			return vo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateUserprofilePasswordExpDate(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : updateUserprofilePasswordExpDate");

		try {
			this.update("UserMaster.updateUserProfileExpiryDate", userId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getTempPassword(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : getTempPassword");
		String tempPass = null;

		try {
			tempPass = (String) this.queryForObject("UserMaster.getTempPassword", userId);
			return tempPass;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String forgotPasswordUpdate(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : forgotPasswordUpdate");

		try {
			HashMap<String, String> map = new HashMap();
			map.put("userId", userId);
			map.put("isnewuser", "2");
			this.logger.debug("userId:::" + userId + "TWO:::" + "2");
			this.update("UserMaster.forgotPasswordUpdate", map);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		return this.SUCCESS;
	}

	public int getTempPassCount(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : tempPassCount");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("UserMaster.getTempCountPassword", userId);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getNoDaysBeforeExpiry(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionDAO : getNoDaysBeforeExpiry");
		boolean var2 = false;

		try {
			int noOfDaysBeforeExpire = (Integer) this.queryForObject("UserMaster.getNoDaysBeforeExpiry", userId);
			return noOfDaysBeforeExpire;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int isEmailExit(String pEmailId) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("Entring isEmailExit");
			String emaiId = "";
			String domain_name = pEmailId.substring(pEmailId.indexOf(64));
			this.logger.debug("Domain Name" + domain_name);
			if (domain_name.equalsIgnoreCase("@tr.com")) {
				this.logger.debug("@Replacing tr.com");
				emaiId = pEmailId.replace("tr.com", "thomsonreuters.com");
			} else if (domain_name.equalsIgnoreCase("@thomsonreuters.com")) {
				this.logger.debug("@Replacing thomsonreuters.com");
				emaiId = pEmailId.replace("thomsonreuters.com", "tr.com");
			} else {
				emaiId = null;
			}

			HashMap<String, String> hmapBackup = new HashMap();
			hmapBackup.put("pEmailId", pEmailId);
			hmapBackup.put("emaiId", emaiId);
			int count = (Integer) this.queryForObject("UserMaster.checkEmailExist", hmapBackup);
			this.logger.debug(" Email Id count " + count);
			return count;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}
}