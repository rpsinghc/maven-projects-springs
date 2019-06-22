package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.IUserMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ExposePropertyPlaceholderConfigurer;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserCountryProfileVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UserMultiActionController extends MultiActionController {
	private AtlasReportService atlasReportService;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.UserMultiActionController");
	private ModelAndView mv = null;
	private String Message = "Default Method of User Action";
	private int Mesg_no = 1;
	private String JsonView = "jsonView";
	private static final String GET_USER = "getUser";
	private static final String ACTION = "action";
	private static final String UPDATE_DEACTIVE = "updateDeactive";
	private static final String USER_MASTER = "User Master";
	private static final String Organization_Chart = "Organization Chart";
	private static final String GET_CPL = "cpl";
	private static final String activeRecuCaseCount = "activeRecuCaseCount";
	private String HOME = "redirect:reset_user.do";
	private String Add_User = "add_user";
	private String addSuccess = "addSuccess";
	private String trueval = "true";
	private String falseval = "false";
	private String updval = "update";
	private String User_custom = "user_custom";
	private String role = "role";
	private String userID = "User ID";
	private String usrName = "User Name";
	private String usrOffice = "Office";
	private String officeHead = "Office Head";
	private String supervisor = "Supervisor Name";
	private String email = "Email ID";
	private String doLocalLanguage = "Can Do Local Language";
	private String designation = "Designation";
	private String backUp1 = "Backup1";
	private String backUp2 = "Backup2";
	private String roles = "Roles";
	private String usrStatus = "Status";
	private String space = "";
	private String luoffice = "luoffice";
	private String emailID = "Email";
	private String isCase = "isCase";
	private String testFlag;
	private String loginId = "loginId";
	private String userIdVal = "userId";
	private String uIdVal = "uId";
	private String zero = "0";
	private String one = "1";
	private String comma = ",";
	private String roleString = "roleString";
	private String ccode = "ccode";
	private String llang = "llang";
	private String engmed = "engmed";
	private String Active = "Active";
	private String Deactive = "Deactive";
	private String ISORGANIZATION = "isOrgainzation";
	private String activeCaseCount = "activeCase";
	private String activeTaskCount = "activetask";
	private String redirect = "redirect:getUserInfo.do?userId=";
	private String pastOH = "uofficehead";
	private String activeTaskOh = "activeTaskOh";
	private boolean isuseradded = false;
	private String m_subject = "Atlas New Login Created";
	private String m_message = "";
	private String emailFailure = "emailFailure";
	ExposePropertyPlaceholderConfigurer propertyConfigurer = null;
	IUserMaster userMultiActionManager = null;
	private String usrcreatedby = "User Creator";
	private String usrcreationdt = "Creation Date";
	private String statusmodifiedby = "Status Modified By";
	private String statusmodifiedon = "Status Modified On";
	private String previousroles = "Previous Roles";
	private String rolemodifiedby = "Role Modified By";
	private String rolemodifiedon = "Role Modified On";
	private String unlockedBy = "Unlocked By";
	private String unlockedOn = "Unlocked On";
	private String previousEmailId = "Previous Email ID";
	private String emailIdModifiedBy = "Email ID modified by";
	private String emailIdModifiedOn = "Email ID modified on";
	private String previousoffice = "Previous Office";
	private String officeModifiedBy = "Office modified by";
	private String officeModifiedOn = "Office modified on ";
	private String previousSupervisor = "Previous Supervisor";
	private String supervisorModifiedBy = "Supervisor modified by";
	private String supervisorModifiedOn = "Supervisor modified on";
	UserCountryProfileVO userCountryProfileVO = null;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyConfigurer(ExposePropertyPlaceholderConfigurer propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

	public void setUserMultiActionManager(IUserMaster userMultiActionManager) {
		this.userMultiActionManager = userMultiActionManager;
	}

	public void setUserCountryProfileVO(UserCountryProfileVO userCountryProfileVO) {
		this.userCountryProfileVO = userCountryProfileVO;
	}

	public ModelAndView add_user(HttpServletRequest request, HttpServletResponse response, UserMasterVO userMasterVO)
			throws CMSException {
		ModelAndView modelAndView = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			String lUOfficeCode = this.userMultiActionManager.logInUsrOffice(userName);
			modelAndView = new ModelAndView(this.Add_User);
			modelAndView.addObject(this.luoffice, lUOfficeCode);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView reset_user(HttpServletRequest request, HttpServletResponse response, UserMasterVO userMasterVO)
			throws CMSException {
		ModelAndView modelAndView = new ModelAndView(this.User_custom);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			String lUOfficeCode = this.userMultiActionManager.logInUsrOffice(userName);
			if (request.getSession().getAttribute(this.isCase) != null
					&& request.getSession().getAttribute(this.isCase).toString().equalsIgnoreCase(this.trueval)) {
				modelAndView.addObject(this.addSuccess, request.getSession().getAttribute(this.isCase));
				request.getSession().removeAttribute(this.isCase);
			} else if (request.getSession().getAttribute(this.isCase) != null
					&& request.getSession().getAttribute(this.isCase).toString().equalsIgnoreCase(this.emailFailure)) {
				modelAndView.addObject(this.addSuccess, this.emailFailure);
				request.getSession().removeAttribute(this.isCase);
			} else {
				modelAndView.addObject(this.addSuccess, this.falseval);
			}

			modelAndView.addObject(this.luoffice, lUOfficeCode);
			modelAndView.addObject(this.loginId, userName);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView addUserAndNew(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws CMSException {
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			String userRole = request.getParameter(this.role);
			String bdmRegion = request.getParameter("bdmRegion");
			if (bdmRegion != null && !bdmRegion.isEmpty()) {
				this.userMultiActionManager.mapRegionToBDM(userMasterVO.getUserID(), bdmRegion);
			}

			this.logger.debug("generated user id in savvion is >>>>>  " + userMasterVO.getUserID());
			String tempPassword = "";

			try {
				tempPassword = AtlasUtils.generatePassword();
			} catch (Exception var15) {
				return AtlasUtils.getExceptionView(this.logger, var15);
			}

			userMasterVO.setPassword(tempPassword);
			userMasterVO.setUpdatedBy(userName);
			userMasterVO.setUserCreator(userName);
			userMasterVO.setRoleModifiedBy(userName);
			this.isuseradded = this.userMultiActionManager.insertNewUser(userMasterVO, userRole);
			this.mv = new ModelAndView(this.HOME);
			if (this.isuseradded) {
				request.getSession().setAttribute(this.isCase, this.trueval);
				this.logger.info("User " + userMasterVO.getUsername() + " Successfully Added.");

				try {
					String userId = userMasterVO.getUserID().toLowerCase();
					String to_EmailId = ResourceLocator.self().getSBMService().getEmailId(userId);
					this.logger.debug("Sending email to the user");
					String resetPasswordURL = (String) this.propertyConfigurer.getResolvedProps()
							.get("atlas.loginpage.url");
					this.logger.debug("UserName" + userMasterVO.getUsername());
					this.logger.debug("User ID " + userId);
					this.logger.debug("Email ID" + to_EmailId);
					this.logger.debug("User Master Id: " + userMasterVO.getUserMasterId());
					this.logger.debug("UserId value:" + userMasterVO.getUserIdVal());
					this.m_message = "Your Atlas account has been created. \n\rYour new login information as below:\n\rLogin Id : "
							+ userId + "\n\r" + "Temporary Password is : "
							+ ResourceLocator.self().getSBMService()
									.decryptText(ResourceLocator.self().getSBMService().getPassword(userId))
							+ "\n\r" + "Please use below link to login into ATLAS. \n\r" + resetPasswordURL
							+ "\n\r Note: You will need to choose a new password when you login in system for the first time. \n\r"
							+ "\n\r Thank you.\n\rThomson Reuters \n\r\n\rrisk.thomsonreuters.com \n\r*********************************************************************************************** \n\rThis email was sent to you by Thomson Reuters,the global news and Information Company.\r Any views expressed in this message are those of the individual sender,\rexcept where the sender specifically states them to be the views of Thomson Reuters. \r************************************************************************************************ ";
					this.logger.debug("Atlas New Login Created Message:::::" + this.m_message);

					try {
						ResourceLocator.self().getMailService().sendEmail(this.m_subject, to_EmailId, this.m_message);
					} catch (Exception var13) {
						this.logger.debug("unable to send email::::");
						request.getSession().setAttribute(this.isCase, this.emailFailure);
					}

					this.m_message = "";
				} catch (Exception var14) {
					AtlasUtils.getExceptionView(this.logger, var14);
				}
			} else {
				this.logger.debug(
						"UMInfo: User Is not created as there is some issue in DB. Either the userID is already available in Savvion or it is corrupted");
			}
		} catch (CMSException var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		} catch (Exception var17) {
			return AtlasUtils.getExceptionView(this.logger, var17);
		}

		return this.mv;
	}

	public ModelAndView getUserInfo(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		try {
			if (request.getParameter(this.userIdVal) != null
					&& !request.getParameter(this.userIdVal).equalsIgnoreCase(this.space)) {
				int activeCases = false;
				int activeRecuCase = false;
				String al = this.space;
				String cc = this.space;
				String ll = this.space;
				String em = this.space;
				String bdmRegion = this.space;
				this.mv = new ModelAndView(this.Add_User);
				UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
				String userName = userBean.getUserName();
				String userId = request.getParameter(this.userIdVal);
				UserMasterVO vo = this.userMultiActionManager.getUserInfo(userId);
				int allowInvalidAttempt = Integer
						.parseInt((String) this.propertyConfigurer.getResolvedProps().get("atlas.login.attempt"));
				int currentLoginCount = vo.getLoginAttempt();
				if (currentLoginCount > allowInvalidAttempt) {
					vo.setIsAccountLocked("1");
				} else {
					vo.setIsAccountLocked("0");
				}

				if (ResourceLocator.self().getSBMService().getEmailId(userId) == null
						|| ResourceLocator.self().getSBMService().getEmailId(userId).equals(this.space)) {
					this.mv = new ModelAndView(this.User_custom);
					return this.mv;
				}

				String email = ResourceLocator.self().getSBMService().getEmailId(userId);
				vo.setEmailID(email);
				if (vo.getStatus() != null && vo.getStatus().equalsIgnoreCase(this.zero)) {
					this.mv.addObject("updateDeactive", "updateDeactive");
				}

				userMasterVO.setUserRoleList(this.userMultiActionManager.getUserRoleInfo(userId));
				List<String> list = userMasterVO.getUserRoleList();
				this.logger.debug("size is " + list.size());
				this.logger.debug("list of roles is :: " + list);
				if (list.contains("R10")) {
					bdmRegion = this.userMultiActionManager.getBdmRegion(vo.getUserID());
				}

				this.logger.debug("Region for bdm is = " + bdmRegion);
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {
					al = al + (String) iterator.next() + this.comma;
					this.logger.debug(al);
				}

				new ArrayList();
				List<UserCountryProfileVO> cpl = this.userMultiActionManager.getLocalLangInfo(userId);
				Iterator iterator = cpl.iterator();

				while (iterator.hasNext()) {
					cc = cc + ((UserCountryProfileVO) iterator.next()).getCountryId() + this.comma;
					this.logger.debug(cc);
				}

				for (iterator = cpl.iterator(); iterator
						.hasNext(); ll = ll + ((UserCountryProfileVO) iterator.next()).getLocLangList() + this.comma) {
					;
				}

				for (iterator = cpl.iterator(); iterator
						.hasNext(); em = em + ((UserCountryProfileVO) iterator.next()).getEngMediaList() + this.comma) {
					;
				}

				int activeCases = this.userMultiActionManager.chkActiveCaseBeforeDeactivateUser(vo.getUserIdVal());
				this.logger.debug("active Cases for user in Get User Info is" + activeCases);
				int activeRecuCase = this.userMultiActionManager
						.chkActiveRecurrenceCaseBeforeDeactivateUser(vo.getUserIdVal());
				this.logger.debug("active Recurrence Cases for user in Get User Info is" + activeRecuCase);
				this.mv.addObject("getUser", vo);
				this.mv.addObject("action", this.updval);
				this.mv.addObject(this.roleString, al);
				this.mv.addObject(this.ccode, cc);
				this.mv.addObject(this.llang, ll);
				this.mv.addObject(this.engmed, em);
				this.mv.addObject(this.addSuccess, this.falseval);
				this.mv.addObject(this.loginId, userName);
				this.mv.addObject(this.activeCaseCount, activeCases);
				this.mv.addObject("activeRecuCaseCount", activeRecuCase);
				this.mv.addObject("bdmRegion", bdmRegion);
			} else {
				this.mv = new ModelAndView(this.User_custom);
			}
		} catch (CMSException var21) {
			return AtlasUtils.getExceptionView(this.logger, var21);
		} catch (Exception var22) {
			return AtlasUtils.getExceptionView(this.logger, var22);
		}

		return this.mv;
	}

	public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			int pastOHValue = Integer.valueOf(request.getParameter(this.pastOH));
			String userName = userBean.getUserName();
			String uId = request.getParameter(this.uIdVal);
			String isAccountLocked = request.getParameter("AccountLocked");
			this.logger.debug("Account lock value" + isAccountLocked);
			if (isAccountLocked.equalsIgnoreCase(this.zero)) {
				userMasterVO.setIsAccountLocked("0");
			}

			this.logger.debug("User id at the time of updation > >>    >  " + uId);
			userMasterVO.setUserID(uId);
			userMasterVO.setUnlockedBy(userName);
			this.logger.debug("userName::::::::: " + userName);
			userMasterVO.setRoleModifiedBy(userName);
			userMasterVO.setUpdatedBy(userName);
			String userRole = request.getParameter(this.role);
			String updateDeactive = request.getParameter("updateDeactive");
			this.logger.debug("Inside ========> Update Deactivate" + updateDeactive);
			this.logger.debug("Role chaged or not ???===>" + request.getParameter("userRolechangeFlag"));
			if (updateDeactive != null && !updateDeactive.equals(this.space)) {
				this.userMultiActionManager.updateDeactiveUser(userMasterVO);
				this.mv = new ModelAndView(this.redirect + userMasterVO.getUserID());
			} else {
				this.userMultiActionManager.updateUser(userMasterVO, userRole, pastOHValue);
				request.getSession().setAttribute(this.isCase, this.updval);
				this.mv = new ModelAndView(this.HOME);
			}

			this.logger.info("User" + uId + "successfully updated");
			this.logger.debug("uId is ::: " + uId);
			String bdmRegion = request.getParameter("bdmRegion");
			this.logger.debug("update bdm to :: " + bdmRegion);
			this.logger.debug("userMasterVO.getUserID() :: " + userMasterVO.getUserID());
			this.logger.debug("bdmRegion is not Empty()" + !bdmRegion.isEmpty() + " :::: " + bdmRegion.isEmpty());
			if (bdmRegion != null && !bdmRegion.isEmpty()) {
				this.logger.debug("inside bdm if condition");
				String ifBdmExists = this.userMultiActionManager.getBdmRegion(uId);
				if (ifBdmExists != null && !ifBdmExists.isEmpty()) {
					this.userMultiActionManager.updateBdmRegion(uId, bdmRegion);
				} else {
					this.userMultiActionManager.mapRegionToBDM(uId, bdmRegion);
				}
			} else {
				this.logger.debug("inside bdm else condition");
				this.userMultiActionManager.deleteBdmRegionMapping(uId);
			}

			if ("true".equalsIgnoreCase(request.getParameter("userRolechangeFlag"))) {
				this.logger.debug("is userRolechangeFlag ?" + request.getParameter("userRolechangeFlag"));
				HashMap hsMap = ResourceLocator.self().getCacheService().getACLTokenCache();
				this.logger.debug("Exixting hs map " + hsMap);
				if (hsMap.containsKey(uId)) {
					this.logger.debug("In role update flag");
					hsMap.put(uId, "RoleUpdated");
					ResourceLocator.self().getCacheService().updateACLTokenCache(hsMap);
				}

				this.logger.debug("End of role updation flag");
				this.logger.debug("getUserIdVal() " + userMasterVO.getUserIdVal());
				Map updatedRoleMap = ResourceLocator.self().getACLService()
						.getACLPermissions(String.valueOf(userMasterVO.getUserIdVal()));
				this.logger.debug("updatedRoleMap " + updatedRoleMap);
				ResourceLocator.self().getAtlasDashboardService().updateUserDashboard(uId, updatedRoleMap,
						userMasterVO.getUserIdVal());
				this.logger.debug("-----------after cache");
			}

			this.logger.debug("Exiting user----------------------");
		} catch (CMSException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}

		return this.mv;
	}

	public ModelAndView userReportExport(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		this.logger.debug("in  UserMultiaction Controller.exportToExcel ");
		ModelAndView modelandview = null;

		try {
			request.setAttribute("reportName", "User Information");
			String excelParams = request.getParameter("excelParams");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("THE VALUE OF ORGANIZATION CHART:" + excelParamMap.get(this.ISORGANIZATION).toString());
			List userDataList;
			Map resultMap;
			if (excelParamMap.get(this.ISORGANIZATION).equals(this.trueval)) {
				userDataList = this.userMultiActionManager.getOrganizationChart(excelParamMap);
				this.logger.debug("fetched UserDataList for Organization Chart>>Size is " + userDataList.size());
				resultMap = this.writeToExcelOrg(userDataList, response);
				modelandview = new ModelAndView("excelDownloadPopup");
				modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
				modelandview.addObject("fileName", resultMap.get("fileName"));
				this.logger.debug("exiting Organization chart Excel  UserDataController ");
			} else {
				userDataList = this.userMultiActionManager.getReport(excelParamMap);
				this.logger.debug("fetched UserDataList>>Size is " + userDataList.size());
				resultMap = this.writeToExcel(userDataList, response);
				modelandview = new ModelAndView("excelDownloadPopup");
				modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
				modelandview.addObject("fileName", resultMap.get("fileName"));
				this.logger.debug("exiting  Export to Excel UserDataController ");
			}

			return modelandview;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	private Map<String, Object> writeToExcel(List<UserMasterVO> userDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.userID);
			lstHeader.add(this.usrName);
			lstHeader.add(this.designation);
			lstHeader.add(this.usrOffice);
			lstHeader.add(this.officeHead);
			lstHeader.add(this.supervisor);
			lstHeader.add(this.backUp1);
			lstHeader.add(this.backUp2);
			lstHeader.add(this.roles);
			lstHeader.add(this.usrStatus);
			lstHeader.add(this.emailID);
			lstHeader.add(this.doLocalLanguage);
			lstHeader.add(this.usrcreatedby);
			lstHeader.add(this.usrcreationdt);
			lstHeader.add(this.statusmodifiedby);
			lstHeader.add(this.statusmodifiedon);
			lstHeader.add(this.previousroles);
			lstHeader.add(this.rolemodifiedby);
			lstHeader.add(this.rolemodifiedon);
			lstHeader.add(this.unlockedBy);
			lstHeader.add(this.unlockedOn);
			lstHeader.add(this.previousEmailId);
			lstHeader.add(this.emailIdModifiedBy);
			lstHeader.add(this.emailIdModifiedOn);
			lstHeader.add(this.previousoffice);
			lstHeader.add(this.officeModifiedBy);
			lstHeader.add(this.officeModifiedOn);
			lstHeader.add(this.previousSupervisor);
			lstHeader.add(this.supervisorModifiedBy);
			lstHeader.add(this.supervisorModifiedOn);
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = userDataList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
				datamap.put(this.userID, String.valueOf(userMasterVO.getUserID()));
				datamap.put(this.usrName, String.valueOf(userMasterVO.getUsername()));
				datamap.put(this.designation, String.valueOf(userMasterVO.getDesignation()));
				datamap.put(this.usrOffice, String.valueOf(userMasterVO.getOffice()));
				datamap.put(this.officeHead, String.valueOf(userMasterVO.getOfficeHead()));
				datamap.put(this.supervisor, String.valueOf(userMasterVO.getSupervisor()));
				datamap.put(this.backUp1, String.valueOf(userMasterVO.getBackup1()));
				datamap.put(this.backUp2, String.valueOf(userMasterVO.getBackup2()));
				datamap.put(this.roles, String.valueOf(userMasterVO.getRoles()));
				if (userMasterVO.getStatus().equalsIgnoreCase(this.one)) {
					datamap.put(this.usrStatus, this.Active);
				} else {
					datamap.put(this.usrStatus, this.Deactive);
				}

				datamap.put(this.emailID, String.valueOf(userMasterVO.getEmailID()));
				datamap.put(this.doLocalLanguage, String.valueOf(userMasterVO.getLocalLangCountriesNames()));
				datamap.put(this.usrcreatedby, String.valueOf(userMasterVO.getUserCreator()));
				datamap.put(this.usrcreationdt, String.valueOf(userMasterVO.getUserCreationDate()));
				datamap.put(this.statusmodifiedby, String.valueOf(userMasterVO.getStatusModifiedBy()));
				datamap.put(this.statusmodifiedon, String.valueOf(userMasterVO.getStatusModifiedOn()));
				datamap.put(this.previousroles, String.valueOf(userMasterVO.getPreviousRoles()));
				datamap.put(this.rolemodifiedby, String.valueOf(userMasterVO.getRoleModifiedBy()));
				datamap.put(this.rolemodifiedon, String.valueOf(userMasterVO.getRoleModifiedOn()));
				datamap.put(this.unlockedBy, String.valueOf(userMasterVO.getUnlockedBy()));
				datamap.put(this.unlockedOn, String.valueOf(userMasterVO.getUnlockedOn()));
				datamap.put(this.previousEmailId, String.valueOf(userMasterVO.getPreviousEmailId()));
				datamap.put(this.emailIdModifiedBy, String.valueOf(userMasterVO.getEmailIDModifiedBy()));
				datamap.put(this.emailIdModifiedOn, String.valueOf(userMasterVO.getEmailIDModifiedOn()));
				datamap.put(this.previousoffice, String.valueOf(userMasterVO.getPreviousOffice()));
				datamap.put(this.officeModifiedBy, String.valueOf(userMasterVO.getOfficeModifiedBy()));
				datamap.put(this.officeModifiedOn, String.valueOf(userMasterVO.getOfficeModifiedOn()));
				datamap.put(this.previousSupervisor, String.valueOf(userMasterVO.getPreviousSupervisor()));
				datamap.put(this.supervisorModifiedBy, String.valueOf(userMasterVO.getSupervisorModifiedBy()));
				datamap.put(this.supervisorModifiedOn, String.valueOf(userMasterVO.getSupervisorModifiedOn()));
				dataList.add(datamap);
			}

			return ExcelDownloader.writeToExcel(lstHeader, dataList, "User Master", (short) 0, (short) 1, response,
					"User Master");
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	private Map<String, Object> writeToExcelOrg(List<UserMasterVO> userDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.usrOffice);
			lstHeader.add(this.supervisor);
			lstHeader.add(this.usrName);
			lstHeader.add(this.designation);
			lstHeader.add(this.backUp1);
			lstHeader.add(this.backUp2);
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = userDataList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				UserMasterVO userMasterVO = (UserMasterVO) iterator.next();
				datamap.put(this.usrOffice, String.valueOf(userMasterVO.getOffice()));
				datamap.put(this.supervisor, String.valueOf(userMasterVO.getSupervisor()));
				datamap.put(this.usrName, String.valueOf(userMasterVO.getUsername()));
				datamap.put(this.designation, String.valueOf(userMasterVO.getDesignation()));
				datamap.put(this.backUp1, String.valueOf(userMasterVO.getBackup1()));
				datamap.put(this.backUp2, String.valueOf(userMasterVO.getBackup2()));
				dataList.add(datamap);
			}

			return ExcelDownloader.writeToExcel(lstHeader, dataList, "Organization Chart", (short) 0, (short) 1,
					response, "Organization Chart");
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}
}