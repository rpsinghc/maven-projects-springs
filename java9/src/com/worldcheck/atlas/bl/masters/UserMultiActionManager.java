package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.IUserMaster;
import com.worldcheck.atlas.dao.masters.UserMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasHistoryUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.BDMRegionVO;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.UserCountryProfileVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.masters.UserRoleMapVO;
import com.worldcheck.atlas.vo.sbm.UserCreationVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONValue;
import org.xml.sax.SAXException;

public class UserMultiActionManager implements IUserMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.UserMultiActionManager");
	private String deActiveEmpty = "Please Select Some Rows";
	UserMultiActionDAO userMultiActionDAO = null;
	private static final String comma = ",";
	private static final String one = "1";
	private static final String zero = "0";
	private String True = "true";
	private String False = "false";
	private String country = "country";
	private String englishmedia = "englishmedia";
	private String localsupportVal = "localsupport";
	private boolean check;
	private String empty = "";
	private String R3 = "R3";
	private AtlasHistoryUtil atlasHistoryUtil;

	public void setAtlasHistoryUtil(AtlasHistoryUtil atlasHistoryUtil) {
		this.atlasHistoryUtil = atlasHistoryUtil;
	}

	public void setUserMultiActionDAO(UserMultiActionDAO userMultiActionDAO) {
		this.userMultiActionDAO = userMultiActionDAO;
	}

	public List<UserMasterVO> selectAllUser() throws CMSException {
		return this.userMultiActionDAO.selectAllUser();
	}

	public List<UserRoleMapVO> roleInfo() throws CMSException {
		return this.userMultiActionDAO.roleInfo();
	}

	public List<UserMasterVO> searchUser(UserMasterVO userMasterVO, int start, int limit, String sortColumnName,
			String sortType) throws CMSException {
		this.logger.debug("LOcal Lang========>" + userMasterVO.getLocalLanguage());
		userMasterVO.setStart(new Integer(start + 1));
		userMasterVO.setLimit(new Integer(start + limit));
		userMasterVO.setSortColumnName(sortColumnName);
		userMasterVO.setSortType(sortType);
		return this.userMultiActionDAO.searchUser(userMasterVO);
	}

	public void deactivateUser(String userID, String ustatus, String isOffHeadPre, String loginId, String updated_by)
			throws CMSException {
		this.logger.debug("In Deactivate manager");
		this.logger.debug(userID);
		String[] userIdVal = userID.split(",");
		String[] var10 = userIdVal;
		int var9 = userIdVal.length;

		for (int var8 = 0; var8 < var9; ++var8) {
			String boCode = var10[var8];
			Map<String, String> map = new HashMap();
			map.put("userID", boCode);
			map.put("ustatus", ustatus);
			map.put("updated_by", updated_by);
			this.userMultiActionDAO.deactivateUser(map);
			String action = "Update";
			UserMasterVO newUMVO = new UserMasterVO();
			UserMasterVO oldUMVO = new UserMasterVO();
			if (((String) map.get("ustatus")).equalsIgnoreCase("0")) {
				newUMVO.setStatus("0");
				oldUMVO.setStatus("1");
				action = "Deactivate";
			} else {
				newUMVO.setStatus("1");
				oldUMVO.setStatus("0");
				action = "Activate";
			}

			if (ustatus.equals("0") && isOffHeadPre.equals("1")) {
				this.userMultiActionDAO.updateOfficeHead(loginId, 0, updated_by);
				newUMVO.setIsOfficeHead(0);
				oldUMVO.setIsOfficeHead(1);
			} else {
				newUMVO.setIsOfficeHead(3);
				oldUMVO.setIsOfficeHead(3);
			}

			String newXml;
			String oldXml;
			try {
				newXml = this.createHistoryData(newUMVO);
				this.logger.debug("NEW XML File for History" + newXml);
				oldXml = this.createHistoryData(oldUMVO);
				this.logger.debug("OLD XML File for History" + oldXml);
			} catch (ParserConfigurationException var19) {
				throw new CMSException(this.logger, var19);
			} catch (Exception var20) {
				throw new CMSException(this.logger, var20);
			}

			AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
			if (newXml != null && newXml.trim().length() > 0) {
				atlasHistoryVO.setHistoryKey(boCode);
				atlasHistoryVO.setNewInfo(newXml);
				atlasHistoryVO.setOldInfo(oldXml);
				atlasHistoryVO.setAction(action);
				atlasHistoryVO.setUpdatedBy(updated_by);
				String historyID = this.atlasHistoryUtil.userHistoryInsertData("USER_MASTER_HISTORY", atlasHistoryVO);
				this.logger.debug("Inserted record  in History table Id is ##" + historyID);
			}
		}

	}

	public void addUser(UserMasterVO userMasterVO, String userRole) throws CMSException {
		this.userMultiActionDAO.addNewUser(userMasterVO);
		String[] userRoles = userRole.split(",");
		String[] var7 = userRoles;
		int var6 = userRoles.length;

		for (int var5 = 0; var5 < var6; ++var5) {
			String userRol = var7[var5];
			this.logger.debug(userMasterVO.getUserID());
			this.logger.debug("--" + userMasterVO.getUserMasterId());
			this.logger.debug(userRol);
			userMasterVO.setRoles(userRol);
			this.userMultiActionDAO.addNewUserRole(userMasterVO);
		}

	}

	public List<UserMasterVO> getSubOrdinateList(String userName) throws CMSException {
		new ArrayList();
		List<UserMasterVO> subOrdinateList = this.userMultiActionDAO.getSubOrdinateList(userName);
		return subOrdinateList;
	}

	public List<UserMasterVO> getBackUpList(String userName) throws CMSException {
		new ArrayList();
		List<UserMasterVO> subOrdinateList = this.userMultiActionDAO.getBackUpList(userName);
		return subOrdinateList;
	}

	public List<UserMasterVO> getActiveUserList() throws CMSException {
		new ArrayList();
		List<UserMasterVO> subOrdinateList = this.userMultiActionDAO.getActiveUserList();
		return subOrdinateList;
	}

	public List<UserMasterVO> displayDetails(UserMasterVO userMasterVO) throws CMSException {
		return this.userMultiActionDAO.displayDetails(userMasterVO);
	}

	public List<UserMasterVO> displayBackup(String officeCode, String userToUpdate) throws CMSException {
		return this.userMultiActionDAO.displayBackup(officeCode, userToUpdate);
	}

	public List<UserMasterVO> getAssociateDetail() throws CMSException {
		return this.userMultiActionDAO.getAssociateDetail();
	}

	public int checkOfficeHead(String officeCode) throws CMSException {
		int count = false;
		int count = this.userMultiActionDAO.checkOfficeHead(officeCode);
		return count;
	}

	public List<UserMasterVO> getAllUserID() throws CMSException {
		List<UserMasterVO> list = null;
		list = this.userMultiActionDAO.getAllUserID();
		return list;
	}

	public UserMasterVO getUserInfo(String userId) throws CMSException {
		UserMasterVO vo = this.userMultiActionDAO.getUSerInfo(userId);
		return vo;
	}

	public void updateUser(UserMasterVO userMasterVO, String userRole, int pastOHValue) throws CMSException {
		String userIDWithRole3 = this.empty;
		boolean failToUpdate = true;
		String old_MailId = this.empty;
		UserMasterVO nvo = userMasterVO;
		int flag = false;
		String[] modifiedRecords = userMasterVO.getModifiedRecords();
		UserMasterVO newUMVO = new UserMasterVO();
		UserMasterVO oldUMVO = new UserMasterVO();
		String action = "Update";
		this.logger.debug("hdnId" + userMasterVO.getHdnuId());
		this.logger.debug("hdnuName" + userMasterVO.getHdnuName());
		this.logger.debug("hdnuOffice" + userMasterVO.getHdnuOffice());
		this.logger.debug("hdnuback1" + userMasterVO.getHdnuback1());
		this.logger.debug("hdnuback2" + userMasterVO.getHdnuback2());
		this.logger.debug("hdnureportsTo" + userMasterVO.getHdnureportsTo());
		this.logger.debug("hdnuemail" + userMasterVO.getHdnuemail());
		this.logger.debug("hdnudesignation" + userMasterVO.getHdnudesignation());
		this.logger.debug("hdnustatus" + userMasterVO.getHdnustatus());
		this.logger.debug("hdnuofficehead" + userMasterVO.getHdnisOfficeHead());
		this.logger.debug("hdnactionUpdate" + userMasterVO.getHdnactionUpdate());
		this.logger.debug("hdnupdateDeactive" + userMasterVO.getHdnupdateDeactive());
		this.logger.debug("hdnuroles" + userMasterVO.getHdnuroles());
		this.logger.debug("hdnuprob" + userMasterVO.getHdnuprob());
		this.logger.debug("hdnuccode" + userMasterVO.getHdnuccode());
		this.logger.debug("hdnuserIdVal" + userMasterVO.getHdnuserIdVal());
		this.logger.debug("hdnbdmRegion" + userMasterVO.getHdnbdmRegion());
		this.logger.debug("hdnuAccountLocked" + userMasterVO.getHdnuAccountLocked());
		if (!userMasterVO.getHdnuName().equalsIgnoreCase(userMasterVO.getUsername())) {
			newUMVO.setUsername(userMasterVO.getUsername());
			oldUMVO.setUsername(userMasterVO.getHdnuName());
		}

		if (!userMasterVO.getHdnuOffice().equalsIgnoreCase(userMasterVO.getOffice())) {
			newUMVO.setOffice(userMasterVO.getOffice());
			oldUMVO.setOffice(userMasterVO.getHdnuOffice());
			List<BranchOfficeMasterVO> officeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("OFFICE_MASTER");
			Iterator<BranchOfficeMasterVO> iterator = officeMasterList.iterator();
			HashMap map = new HashMap();

			BranchOfficeMasterVO bvo;
			while (iterator.hasNext()) {
				bvo = (BranchOfficeMasterVO) iterator.next();
				map.put(bvo.getBranchOfficeCode(), bvo);
			}

			bvo = (BranchOfficeMasterVO) map.get(userMasterVO.getHdnuOffice());
			userMasterVO.setPreviousOffice(bvo.getBranchOffice());
		}

		if (!userMasterVO.getHdnbdmRegion().equalsIgnoreCase(userMasterVO.getBdmRegion())) {
			newUMVO.setBdmRegion(userMasterVO.getBdmRegion());
			oldUMVO.setBdmRegion(userMasterVO.getHdnbdmRegion());
		}

		if (!userMasterVO.getHdnuback1().equalsIgnoreCase(userMasterVO.getBackup1())) {
			newUMVO.setBackup1(userMasterVO.getBackup1());
			oldUMVO.setBackup1(userMasterVO.getHdnuback1());
		}

		if (!userMasterVO.getHdnuback2().equalsIgnoreCase(userMasterVO.getBackup2())) {
			newUMVO.setBackup2(userMasterVO.getBackup2());
			oldUMVO.setBackup2(userMasterVO.getHdnuback2());
		}

		if (!userMasterVO.getHdnureportsTo().equalsIgnoreCase(userMasterVO.getReportsTo())) {
			newUMVO.setReportsTo(userMasterVO.getReportsTo());
			oldUMVO.setReportsTo(userMasterVO.getHdnureportsTo());
			userMasterVO.setPreviousSupervisor(this.getUserFullNameFromId(userMasterVO.getHdnureportsTo()));
		}

		if (!userMasterVO.getHdnuemail().equalsIgnoreCase(userMasterVO.getEmailID())) {
			newUMVO.setEmailID(userMasterVO.getEmailID());
			oldUMVO.setEmailID(userMasterVO.getHdnuemail());
			userMasterVO.setPreviousEmailId(userMasterVO.getHdnuemail());
		}

		if (!userMasterVO.getHdnudesignation().equalsIgnoreCase(userMasterVO.getDesignation())) {
			newUMVO.setDesignation(userMasterVO.getDesignation());
			oldUMVO.setDesignation(userMasterVO.getHdnudesignation());
		}

		if (userMasterVO.getHdnisOfficeHead() != userMasterVO.getIsOfficeHead()) {
			newUMVO.setIsOfficeHead(userMasterVO.getIsOfficeHead());
			oldUMVO.setIsOfficeHead(userMasterVO.getHdnisOfficeHead());
		} else {
			newUMVO.setIsOfficeHead(3);
			oldUMVO.setIsOfficeHead(3);
		}

		if (userMasterVO.getIsAccountLocked() != null && userMasterVO.getIsAccountLocked().equalsIgnoreCase("0")) {
			newUMVO.setIsAccountLocked(userMasterVO.getIsAccountLocked());
			oldUMVO.setIsAccountLocked(userMasterVO.getHdnuAccountLocked());
		}

		if (!userMasterVO.getHdnustatus().equalsIgnoreCase(userMasterVO.getStatus())) {
			userMasterVO.setStatusModifiedBy(userMasterVO.getUpdatedBy());
			newUMVO.setStatus(userMasterVO.getStatus());
			oldUMVO.setStatus(userMasterVO.getHdnustatus());
			if (userMasterVO.getStatus().equalsIgnoreCase("0")) {
				action = "Deactivate";
			} else {
				action = "Activate";
			}
		}

		try {
			ResourceLocator.self().getSBMService().isEmailUpdated(userMasterVO.getUserID(), userMasterVO.getEmailID());
			ResourceLocator.self().getSBMService().isFullNameUpdated(userMasterVO.getUserID(),
					userMasterVO.getUsername(), "");
			userIDWithRole3 = this.userMultiActionDAO.checkForR3(userMasterVO.getUserIdVal());
			this.logger.debug("Role Three Exist---" + userIDWithRole3);
			if (userMasterVO.getProbation() == null || userMasterVO.getProbation().equalsIgnoreCase(this.empty)) {
				userMasterVO.setProbation("1");
			}

			List<String> userRoleListBefore = new ArrayList(Arrays.asList(userMasterVO.getHdnuroles().split(",")));
			List<String> userRoleListAfter = new ArrayList(Arrays.asList(userRole.split(",")));
			Set<String> setRoleListBefore = new HashSet(userRoleListBefore);
			Set<String> setRoleListAfter = new HashSet(userRoleListAfter);
			boolean roleChangeFlag = setRoleListBefore.containsAll(userRoleListAfter)
					&& setRoleListAfter.containsAll(userRoleListBefore);
			this.logger.debug("Role List Before ::::::::" + userRoleListBefore.toString());
			this.logger.debug("Role List After ::::::::" + userRoleListAfter.toString());
			this.logger.debug("setRoleListBefore ::::::::" + setRoleListBefore.toString());
			this.logger.debug("setRoleListAfter ::::::::" + setRoleListAfter.toString());
			this.logger.debug("roleChangeFlag ::::::::" + roleChangeFlag);
			if (!roleChangeFlag) {
				userMasterVO.setPreviousRoles(this.sortedRoleList(userRoleListBefore));
				newUMVO.setRoles(this.sortedRoleList(userRoleListAfter));
				oldUMVO.setRoles(this.sortedRoleList(userRoleListBefore));
			}

			this.userMultiActionDAO.updateUser(userMasterVO);
			this.logger.debug("Account locking " + userMasterVO.getIsAccountLocked());
			if (userMasterVO.getIsAccountLocked() != null && userMasterVO.getIsAccountLocked().equalsIgnoreCase("0")) {
				Map<String, String> map = new HashMap();
				map.put("unlocked_by", userMasterVO.getUnlockedBy());
				map.put("userID", userMasterVO.getUserID());
				map.put("isReset", "true");
				this.updateUserLoginAttempt(map);
			}

			String[] userRoles = userRole.split(",");
			String newXml;
			if (userRoles.length > 0) {
				this.userMultiActionDAO.deleteNewUserRole(userMasterVO);
				String[] var22 = userRoles;
				int var21 = userRoles.length;

				for (int var20 = 0; var20 < var21; ++var20) {
					newXml = var22[var20];
					this.logger.debug(userMasterVO.getUserID());
					this.logger.debug("--" + userMasterVO.getUserMasterId());
					this.logger.debug(newXml);
					userMasterVO.setRoles(newXml);
					this.userMultiActionDAO.updateNewUserRole(userMasterVO);
					if (newXml.equalsIgnoreCase(this.R3)) {
						flag = true;
					}
				}

				this.updateUserPasswordExpiryDate(userMasterVO.getUserID());
			}

			if (userIDWithRole3 != null && !userIDWithRole3.equalsIgnoreCase(this.empty) && !flag) {
				this.logger.debug("inside Role R3 case1----------");
				failToUpdate = ResourceLocator.self().getSBMService().isUserRemovedFromQueue(userIDWithRole3);
			}

			if ((userIDWithRole3 == null || userIDWithRole3.equalsIgnoreCase(this.empty)) && flag) {
				this.logger.debug("inside Role R3 case2----------");
				failToUpdate = ResourceLocator.self().getSBMService().isUserAddedToQueue(userMasterVO.getUserID());
			}

			if (!failToUpdate) {
				this.logger.debug("Warning: Not Able to modify User in Savvion Queue.");
			}

			String historyID;
			if (modifiedRecords != null && modifiedRecords.length > 0) {
				this.userMultiActionDAO.deleteCntryProfile(userMasterVO);

				for (int i = 0; i < modifiedRecords.length; ++i) {
					int engMedia = 0;
					int localsupport = 0;
					historyID = modifiedRecords[i];
					Map jsonObject = (Map) JSONValue.parse(historyID);
					nvo.setCountry((String) jsonObject.get(this.country));
					if (((String) jsonObject.get(this.englishmedia)).equals(this.False)) {
						engMedia = 1;
					}

					if (((String) jsonObject.get(this.localsupportVal)).equals(this.True)) {
						localsupport = 1;
					}

					nvo.setEngMediaCheck(engMedia);
					nvo.setLocLangCheck(localsupport);
					this.logger.debug("UserID========>" + nvo.getUserIdVal());
					this.logger.debug("UserID========>" + nvo.getUserID());
					this.logger.debug("UserID========>" + nvo.getUpdatedBy());
					this.logger.debug("UserID========>" + nvo.getEngMediaCheck());
					this.logger.debug("UserID========>" + nvo.getLocLangCheck());
					this.logger.debug("UserID========>" + nvo.getCountry());
					this.userMultiActionDAO.addProfileDetail(nvo);
				}
			} else {
				this.userMultiActionDAO.deleteCntryProfile(userMasterVO);
			}

			if (userMasterVO.getIsOfficeHead() != pastOHValue || Integer.valueOf(userMasterVO.getStatus()) == 0) {
				this.logger.debug("calling update office head function values:" + userMasterVO.getIsOfficeHead());
				this.logger.debug("calling update Previous office head values:" + pastOHValue);
				this.logger.debug("calling update Status values:" + Integer.valueOf(userMasterVO.getStatus()));
				this.userMultiActionDAO.updateOfficeHead(userMasterVO.getUserID(),
						Integer.valueOf(userMasterVO.getStatus()), userMasterVO.getUpdatedBy());
			}

			String oldXml;
			try {
				newXml = this.createHistoryData(newUMVO);
				this.logger.debug("NEW XML File for History" + newXml);
				oldXml = this.createHistoryData(oldUMVO);
				this.logger.debug("OLD XML File for History" + oldXml);
			} catch (ParserConfigurationException var24) {
				throw new CMSException(this.logger, var24);
			} catch (Exception var25) {
				throw new CMSException(this.logger, var25);
			}

			AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
			if (newXml != null && newXml.trim().length() > 0) {
				atlasHistoryVO.setHistoryKey(userMasterVO.getUserIdVal());
				atlasHistoryVO.setNewInfo(newXml);
				atlasHistoryVO.setOldInfo(oldXml);
				atlasHistoryVO.setAction(action);
				atlasHistoryVO.setUpdatedBy(userMasterVO.getUpdatedBy());
				historyID = this.atlasHistoryUtil.userHistoryInsertData("USER_MASTER_HISTORY", atlasHistoryVO);
				this.logger.debug("Inserted record  in History table Id is ##" + historyID);
			}

		} catch (CMSException var26) {
			ResourceLocator.self().getSBMService().isEmailUpdated(userMasterVO.getUserID(), old_MailId);
			throw var26;
		} catch (Exception var27) {
			ResourceLocator.self().getSBMService().isEmailUpdated(userMasterVO.getUserID(), old_MailId);
			throw new CMSException(this.logger, var27);
		}
	}

	public int checkUserId(String userID) throws CMSException {
		int count = false;
		int count = this.userMultiActionDAO.checkUserId(userID);
		return count;
	}

	public List<UserMasterVO> selectSupervisor() throws CMSException {
		return this.userMultiActionDAO.selectSupervisor();
	}

	public void updateDeactiveUser(UserMasterVO userMasterVO) throws CMSException {
		String userIDWithRole3 = "";
		boolean failToUpdate = true;
		UserMasterVO newUMVO = new UserMasterVO();
		UserMasterVO oldUMVO = new UserMasterVO();
		if (!userMasterVO.getHdnustatus().equalsIgnoreCase(userMasterVO.getStatus())) {
			newUMVO.setStatus(userMasterVO.getStatus());
			oldUMVO.setStatus(userMasterVO.getHdnustatus());
			newUMVO.setIsOfficeHead(3);
			oldUMVO.setIsOfficeHead(3);
		}

		this.userMultiActionDAO.updateDeactiveUser(userMasterVO);

		String newXml;
		String oldXml;
		try {
			newXml = this.createHistoryData(newUMVO);
			this.logger.debug("NEW XML File for History" + newXml);
			oldXml = this.createHistoryData(oldUMVO);
			this.logger.debug("OLD XML File for History" + oldXml);
		} catch (ParserConfigurationException var10) {
			throw new CMSException(this.logger, var10);
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}

		AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
		if (newXml != null && newXml.trim().length() > 0) {
			atlasHistoryVO.setHistoryKey(userMasterVO.getUserIdVal());
			atlasHistoryVO.setNewInfo(newXml);
			atlasHistoryVO.setOldInfo(oldXml);
			atlasHistoryVO.setAction("Activate");
			atlasHistoryVO.setUpdatedBy(userMasterVO.getUpdatedBy());
			String historyID = this.atlasHistoryUtil.userHistoryInsertData("USER_MASTER_HISTORY", atlasHistoryVO);
			this.logger.debug("Inserted record  in History table Id is ##" + historyID);
		}

		userIDWithRole3 = this.userMultiActionDAO.checkForR3(userMasterVO.getUserIdVal());
		this.logger.debug("successfull==================>>>" + userIDWithRole3);
		if (userIDWithRole3 != null && !userIDWithRole3.equalsIgnoreCase(this.empty)) {
			failToUpdate = ResourceLocator.self().getSBMService().isUserAddedToQueue(userIDWithRole3);
		}

		if (!failToUpdate) {
			this.logger.debug("Warning: Not Able to modify User in Savvion Queue.");
		}

	}

	public List getUserRoleInfo(String userId) throws CMSException {
		List<String> rvo = this.userMultiActionDAO.getUserRoleInfo(userId);
		return rvo;
	}

	public List<UserCountryProfileVO> getLocalLangInfo(String userId) throws CMSException {
		List<UserCountryProfileVO> lvo = this.userMultiActionDAO.getLocalLangInfo(userId);
		return lvo;
	}

	public int checkBackUp(String officeCode, String userToUpdate) throws CMSException {
		return this.userMultiActionDAO.checkBackUp(officeCode, userToUpdate);
	}

	public List<UserMasterVO> backupWithotUsr() throws CMSException {
		return this.userMultiActionDAO.backupWithotUsr();
	}

	public int searchUserCount(UserMasterVO userMasterVO) throws CMSException {
		return this.userMultiActionDAO.searchUserCount(userMasterVO);
	}

	public List<UserMasterVO> searchCountry(CountryMasterVO countryMasterVO) throws CMSException {
		return this.userMultiActionDAO.searchCountry(countryMasterVO);
	}

	public int searchCountryCount(CountryMasterVO countryMasterVO) throws CMSException {
		return this.userMultiActionDAO.searchCountryCount(countryMasterVO);
	}

	public List<UserMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		return this.userMultiActionDAO.getReport(excelParamMap);
	}

	public List<UserMasterVO> getOrganizationChart(Map<String, Object> excelParamMap) throws CMSException {
		return this.userMultiActionDAO.getOrganizationChart(excelParamMap);
	}

	public List<UserMasterVO> getUsersForRole(String roleId) throws CMSException {
		return this.userMultiActionDAO.getUsersForRole(roleId);
	}

	public boolean insertNewUser(UserMasterVO userMasterVO, String userRole) throws CMSException {
		this.logger.debug("Entring UserMultiActionManager: insertNewUser");
		byte flag = 0;

		try {
			UserCreationVO userCreationVO = new UserCreationVO();
			userCreationVO.setFirstName(userMasterVO.getUsername());
			userCreationVO.setPassword(ResourceLocator.self().getSBMService().encryptText(userMasterVO.getPassword()));
			userCreationVO.setEmail(userMasterVO.getEmailID());
			this.logger.debug("getPassword()  >>>>   " + userCreationVO.getPassword());
			this.logger.debug("supervisor ::::   " + userMasterVO.getReportsTo());
			this.logger.debug("getBackup1 ::::   " + userMasterVO.getBackup1());
			this.logger.debug("getBackup2 ::::   " + userMasterVO.getBackup2());
			this.logger.debug("getIsOfficeHead ::::   " + userMasterVO.getIsOfficeHead());
			this.check = ResourceLocator.self().getSBMService().isUserAdded(userMasterVO.getUserID().toLowerCase(),
					userCreationVO);
			long userID = ResourceLocator.self().getSBMService().getUserID(userMasterVO.getUserID().toLowerCase());
			userMasterVO.setUserMasterId(userID);
			userMasterVO.setUserIdVal(String.valueOf(userID));
			userMasterVO.setIsNewUser(1);
			if (this.check) {
				this.userMultiActionDAO.insertNewUser(userMasterVO);
				String[] userRoles = userRole.split(",");
				String[] var12 = userRoles;
				int var11 = userRoles.length;

				int i;
				for (i = 0; i < var11; ++i) {
					String userRol = var12[i];
					this.logger.debug(userMasterVO.getUserID());
					this.logger.debug("--" + userMasterVO.getUserMasterId());
					this.logger.debug(userRol);
					userMasterVO.setRoles(userRol);
					this.userMultiActionDAO.addNewUserRole(userMasterVO);
					if (userRol.equalsIgnoreCase(this.R3)) {
						flag = 1;
					}
				}

				if (flag == 1) {
					boolean resultValue = ResourceLocator.self().getSBMService()
							.isUserAddedToQueue(userMasterVO.getUserID().toLowerCase());
					if (!resultValue) {
						this.logger.debug("Warning: Not Able to modify User in Savvion Queue.");
					}
				}

				this.logger.debug("=======flag value====>>>" + flag);
				String[] modifiedRecords = userMasterVO.getModifiedRecords();
				String message;
				if (modifiedRecords != null && modifiedRecords.length > 0) {
					for (i = 0; i < modifiedRecords.length; ++i) {
						int engMedia = 0;
						int localsupport = 0;
						message = modifiedRecords[i];
						Map jsonObject = (Map) JSONValue.parse(message);
						userMasterVO.setCountry((String) jsonObject.get(this.country));
						if (((String) jsonObject.get(this.englishmedia)).equals(this.False)) {
							engMedia = 1;
						}

						if (((String) jsonObject.get(this.localsupportVal)).equals(this.True)) {
							localsupport = 1;
						}

						userMasterVO.setEngMediaCheck(engMedia);
						userMasterVO.setLocLangCheck(localsupport);
						this.logger.debug("=========>>>check user id in controller" + userMasterVO.getUserIdVal());
						this.userMultiActionDAO.addProfileDetail(userMasterVO);
					}
				}

				List<String> userRoleListAfter = new ArrayList(Arrays.asList(userRole.split(",")));
				userMasterVO.setRoles(this.sortedRoleList(userRoleListAfter));

				String xml;
				try {
					xml = this.createHistoryData(userMasterVO);
					this.logger.debug("History Data" + xml);
				} catch (ParserConfigurationException var21) {
					throw new CMSException(this.logger, var21);
				} catch (Exception var22) {
					throw new CMSException(this.logger, var22);
				}

				AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
				atlasHistoryVO.setHistoryKey("" + userMasterVO.getUserMasterId());
				atlasHistoryVO.setNewInfo(xml);
				atlasHistoryVO.setOldInfo("");
				atlasHistoryVO.setAction("ADD");
				this.logger.debug("In Add user history::: User ID value:::" + userMasterVO.getUserID());
				atlasHistoryVO.setUpdatedBy(userMasterVO.getUpdatedBy());
				message = this.atlasHistoryUtil.userHistoryInsertData("USER_MASTER_HISTORY", atlasHistoryVO);
				this.logger.debug("ADD USER_MASTER_HISTORY::::::::::::::" + message);
			}

			if (userMasterVO.getIsOfficeHead() == 1) {
				this.userMultiActionDAO.updateOfficeHead(userMasterVO.getUserID(), 1, userMasterVO.getUpdatedBy());
			}
		} catch (CMSException var23) {
			ResourceLocator.self().getSBMService().isUserRemoved(userMasterVO.getUserID().toLowerCase());
			throw var23;
		} catch (Exception var24) {
			this.logger.debug("Exception");
			ResourceLocator.self().getSBMService().isUserRemoved(userMasterVO.getUserID().toLowerCase());
			throw new CMSException(this.logger, var24);
		} finally {
			this.logger.debug("Inside Finallyyyyyyyyy block" + userMasterVO.getUserID());
		}

		return this.check;
	}

	public String logInUsrOffice(String userName) throws CMSException {
		return this.userMultiActionDAO.logInUsrOffice(userName);
	}

	public int chkSupervisorbeforeDeactivateUser(String userID) throws CMSException {
		return this.userMultiActionDAO.chkSupervisorbeforeDeactivateUser(userID);
	}

	public int chkBackup1BeforeDeactivateUser(String userID) throws CMSException {
		return this.userMultiActionDAO.chkBackup1BeforeDeactivateUser(userID);
	}

	public int userFullNameExist(String userFullName) throws CMSException {
		return this.userMultiActionDAO.userFullNameExist(userFullName);
	}

	public String checkForR3(String userID) throws CMSException {
		return this.userMultiActionDAO.checkForR3(userID);
	}

	public List<UserMasterVO> getUserFullName(List<String> users) throws CMSException {
		return this.userMultiActionDAO.getUserFullName(users);
	}

	public String getUserFullNameFromId(String userid) throws CMSException {
		return this.userMultiActionDAO.getUserFullNameFromId(userid);
	}

	public int chkActiveCaseBeforeDeactivateUser(String userID) throws CMSException {
		return this.userMultiActionDAO.chkActiveCaseBeforeDeactivateUser(userID);
	}

	public String chkActiveCaseListBeforeDeactivateUser(String userID) throws CMSException {
		return this.userMultiActionDAO.chkActiveCaseListBeforeDeactivateUser(userID);
	}

	public int checkAssociatedWithClientBforDeact(String userID) throws CMSException {
		return this.userMultiActionDAO.checkAssociatedWithClientBforDeact(userID);
	}

	public List<UserMasterVO> getSubordinateForUser(String userID) throws CMSException {
		return this.userMultiActionDAO.getSubordinateForUser(userID);
	}

	public int chkActiveRecurrenceCaseBeforeDeactivateUser(String userID) throws CMSException {
		this.logger.debug("In Recurrence Case service");
		return this.userMultiActionDAO.chkActiveRecurrenceCaseBeforeDeactivateUser(userID);
	}

	public List<BDMRegionVO> getBDMRegionList() throws CMSException {
		this.logger.debug("In UserMultiactionManager getBDMRegionList method");
		return this.userMultiActionDAO.getBDMRegionList();
	}

	public void mapRegionToBDM(String username, String bdmRegion) throws CMSException {
		this.logger.debug("In UserMultiactionManager mapRegionToBDM method");
		this.userMultiActionDAO.mapRegionToBDM(username, bdmRegion);
	}

	public void updateBdmRegion(String username, String bdmRegion) throws CMSException {
		this.logger.debug("In UserMultiactionManager mapRegionToBDM method");
		this.userMultiActionDAO.updateBdmRegion(username, bdmRegion);
	}

	public String getBdmRegion(String username) throws CMSException {
		this.logger.debug("In UserMultiactionManager getBdmRegion method");
		return this.userMultiActionDAO.getBdmRegion(username);
	}

	public void deleteBdmRegionMapping(String username) throws CMSException {
		this.userMultiActionDAO.deleteBdmRegionMapping(username);
	}

	public int checkForWIPCases(String userID) throws CMSException {
		return this.userMultiActionDAO.checkForWIPCases(userID);
	}

	public int getUserLoginAttempt(String userId, int allowedAttempt) throws CMSException {
		this.logger.debug("Entring UserMultiActionManager : getUserLoginAttempt");
		int loginAttempt = this.userMultiActionDAO.getUserLoginAttempt(userId);
		return loginAttempt;
	}

	public void updateUserLoginAttempt(Map<String, String> map) throws CMSException {
		this.logger.debug("Entring UserMultiActionManager : updateUserLoginAttempt");
		this.userMultiActionDAO.updateUserLoginAttempt(map);
	}

	public void updatePasswordExpiryDate(String userID) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : updatePasswordExpiryDate");
		this.userMultiActionDAO.updateUserPasswordExpDate(userID);
	}

	public boolean isTokenValid(String token, String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : isTokenValid");
		return this.userMultiActionDAO.isTokenValid(token, userId);
	}

	public String insertTempPassword(String token, String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : insertTempPassword");
		return this.userMultiActionDAO.insertTempPassword(token, userId);
	}

	public String updateTempPassword(String token, String userId, String tempPassword) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : updateTempPassword");
		return this.userMultiActionDAO.updateTempPassword(token, userId, tempPassword);
	}

	public void removeTempPassword(String userID) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : removeTempPassword");
		int nowofRows = this.userMultiActionDAO.removeTempPassword(userID);
		this.logger.debug(nowofRows + " Deleted from the temp password table");
	}

	public boolean checkUserIdForTemp(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : checkUserId");
		return this.userMultiActionDAO.checkUserIdforTemp(userId);
	}

	public String getToken(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : checkUserId");
		return this.userMultiActionDAO.getToken(userId);
	}

	public UserMasterVO getUserPassExpiryDetails(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : getUserPassExpiryDetails");
		return this.userMultiActionDAO.getUserPassExpiryDetails(userId);
	}

	public void updateUserPasswordExpiryDate(String userID) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : updateUserPasswordExpiryDate");
		this.userMultiActionDAO.updateUserprofilePasswordExpDate(userID);
	}

	public String getTempPassword(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : getTempPassword");
		return this.userMultiActionDAO.getTempPassword(userId);
	}

	public String forgotPasswordUpdate(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : forgotPasswordUpdate");
		return this.userMultiActionDAO.forgotPasswordUpdate(userId);
	}

	public int getTempPassCount(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : getTempPassCount");
		return this.userMultiActionDAO.getTempPassCount(userId);
	}

	public int getNoDaysBeforeExpiry(String userId) throws CMSException {
		this.logger.debug("Entring UserMultiActionManaer : getNoDaysBeforeExpiry");
		return this.userMultiActionDAO.getNoDaysBeforeExpiry(userId);
	}

	public boolean isEmailExist(String emailId) throws CMSException {
		boolean emailIdExist = false;

		try {
			int count = this.userMultiActionDAO.isEmailExit(emailId);
			if (count > 0) {
				emailIdExist = true;
			}

			return emailIdExist;
		} catch (NullPointerException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String createHistoryData(UserMasterVO userMasterVO) throws ParserConfigurationException, Exception {
		this.logger.debug("In User Master::createHistoryData");
		String historyData = "";
		String prefix = "<br/>&emsp;<b>";
		String suffix = ":</b>";
		if (userMasterVO.getUserID() != null && !userMasterVO.getRoles().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "User ID" + suffix + userMasterVO.getUserID();
		}

		if (userMasterVO.getUsername() != null && !userMasterVO.getUsername().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "User Name(Full Name)" + suffix + userMasterVO.getUsername();
		}

		if (userMasterVO.getStatus() != null) {
			String Status = userMasterVO.getStatus();
			if (Status.equalsIgnoreCase("1")) {
				Status = "Active";
			}

			if (Status.equalsIgnoreCase("0")) {
				Status = "Deactive";
			}

			historyData = historyData + prefix + "Status" + suffix + Status;
		}

		if (userMasterVO.getOffice() != null && !userMasterVO.getOffice().equalsIgnoreCase("")) {
			List<BranchOfficeMasterVO> officeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("OFFICE_MASTER");
			Iterator<BranchOfficeMasterVO> iterator = officeMasterList.iterator();
			HashMap map = new HashMap();

			BranchOfficeMasterVO bvo;
			while (iterator.hasNext()) {
				bvo = (BranchOfficeMasterVO) iterator.next();
				map.put(bvo.getBranchOfficeCode(), bvo);
			}

			bvo = (BranchOfficeMasterVO) map.get(userMasterVO.getOffice());
			historyData = historyData + prefix + "Office" + suffix + bvo.getBranchOffice();
		}

		if (userMasterVO.getBdmRegion() != null && !userMasterVO.getBdmRegion().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Sales Representative Region" + suffix + userMasterVO.getBdmRegion();
		}

		this.logger.debug("In create xml set office head value::: " + userMasterVO.getIsOfficeHead());
		if (userMasterVO.getIsOfficeHead() == 0 || userMasterVO.getIsOfficeHead() == 1) {
			if (userMasterVO.getIsOfficeHead() == 1) {
				historyData = historyData + prefix + "Office Head" + suffix + "Yes";
			}

			if (userMasterVO.getIsOfficeHead() == 0) {
				historyData = historyData + prefix + "Office Head" + suffix + "No";
			}
		}

		if (userMasterVO.getDesignation() != null && !userMasterVO.getDesignation().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Designation" + suffix + userMasterVO.getDesignation();
		}

		if (userMasterVO.getEmailID() != null && !userMasterVO.getEmailID().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Email ID" + suffix + userMasterVO.getEmailID();
		}

		if (userMasterVO.getIsAccountLocked() != null && !userMasterVO.getIsAccountLocked().equalsIgnoreCase("")) {
			if (userMasterVO.getIsAccountLocked().equalsIgnoreCase("0")) {
				historyData = historyData + prefix + "Account Locked" + suffix + "No";
			}

			if (userMasterVO.getIsAccountLocked().equalsIgnoreCase("1")) {
				historyData = historyData + prefix + "Account Locked" + suffix + "Yes";
			}
		}

		if (userMasterVO.getReportsTo() != null && !userMasterVO.getReportsTo().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Reports To" + suffix
					+ this.getUserFullNameFromId(userMasterVO.getReportsTo());
		}

		if (userMasterVO.getBackup1() != null && !userMasterVO.getBackup1().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Backup 1" + suffix
					+ this.getUserFullNameFromId(userMasterVO.getBackup1());
		}

		if (userMasterVO.getBackup2() != null && !userMasterVO.getBackup2().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Backup 2" + suffix
					+ this.getUserFullNameFromId(userMasterVO.getBackup2());
		}

		if (userMasterVO.getRoles() != null && !userMasterVO.getRoles().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "User Roles" + suffix + userMasterVO.getRoles();
		}

		return historyData;
	}

	public List<AtlasHistoryVO> getUserHistory(AtlasHistoryVO atlasHistoryVO)
			throws CMSException, SAXException, IOException {
		this.logger.debug("IN User Master::getUserHistory");
		List<AtlasHistoryVO> userHistoryList = this.atlasHistoryUtil.getUserHistoryData(atlasHistoryVO);
		this.logger.debug("Existing ReportType Master::getReportHistory");
		return userHistoryList;
	}

	public int getUserHistoryCount(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN User Master::getUserHistoryCount");
		return this.atlasHistoryUtil.getUserHistoryDataCount(atlasHistoryVO);
	}

	public String sortedRoleList(List<String> userRoleList) throws CMSException {
		try {
			Map<Integer, String> sortedRoleMap = new TreeMap();
			Iterator var4 = userRoleList.iterator();

			while (var4.hasNext()) {
				String number = (String) var4.next();
				sortedRoleMap.put(Integer.parseInt(number.replaceAll("R", "")), number);
			}

			return sortedRoleMap.values().toString();
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}