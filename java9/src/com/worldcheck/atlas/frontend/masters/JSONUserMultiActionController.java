package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IUserMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.BDMRegionVO;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.masters.UserRoleMapVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONUserMultiActionController extends JSONMultiActionController {
	private static final String activeRecuCaseCount = "activeRecuCaseCount";
	private static final String JSONVIEW = "jsonView";
	private static final String OFFICEHEADFAIL = "officeHeadFail";
	private static final String ALL_USERID = "allUserId";
	private static final String EMAIL_ID_EXIST = "emailIdExist";
	private static final String USER_ID_EXIST = "userIdExist";
	private static final String userName = "userList";
	private static final String supervisorName = "supervisorList";
	private static final String roleName = "roleList";
	private static final String adduserName = "add_userList";
	private static final String add_backUpName = "add_backUpList";
	private static final String searchName = "searchResult";
	private static final String trueval = "true";
	private static final String falseval = "false";
	private static final String updval = "update";
	private static final String deactive = "0";
	private static final String success = "success";
	private static final String count = "count";
	private static final String associateDetail = "associateDetail";
	private static final String error = "error";
	private static final String superCount = "superCount";
	private static final String backup1Count = "backup1Count";
	private static final String activeCaseCount = "activeCase";
	private static final String recurrenceActCaseCount = "recurrenceActiveCase";
	private static final String associatedClientsCount = "associatedClientsCount";
	private static final String isFullNameExist = "isFullNameExist";
	private static final String office = "office";
	private static final String emailIdVal = "emailId";
	private static final String userIDVal = "userID";
	private static final String actionTypeVal = "actionType";
	private static final String name = "name";
	private static final String mailIdVal = "mailId";
	private static final String Action = "action";
	private static final String isOHPre = "isOHPre";
	private static final String usrLoginId = "loginIdVal";
	private static final String ustatusVal = "ustatus";
	private static final String space = "";
	private static final String ROLE_ID = "roleID";
	private static final String USER_List = "userList";
	private static final String validateStatus = "validateStatus";
	private static final String beforDeactive = "beforDeactive";
	private static final String one = "1";
	private static final String zero = "0";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONUserMultiActionController");
	private static final String activeCaseList = "activeCaseList";
	IUserMaster userMultiActionManager = null;

	public void setUserMultiActionManager(IUserMaster userMultiActionManager) {
		this.userMultiActionManager = userMultiActionManager;
	}

	public ModelAndView vaildateUser(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String officeCode = request.getParameter("office");
		String emailId = request.getParameter("emailId");
		String userID = request.getParameter("userID");
		String actionType = request.getParameter("actionType");
		this.logger.debug("Action type in Validate User mehtod==>>>>>>>>" + actionType);
		int count = false;
		int userIdCount = false;
		boolean var11 = false;

		try {
			int count = this.userMultiActionManager.checkOfficeHead(officeCode);
			if (count > 0) {
				modelAndView.addObject("officeHeadFail", "true");
			}

			if (actionType != null && !actionType.equals("update")) {
				int userIdCount = this.userMultiActionManager.checkUserId(userID);
				if (userIdCount > 0) {
					modelAndView.addObject("userIdExist", true);
				}
			}

			this.logger.debug("inside vaildateUser method");
			if (actionType != null && actionType.equals("update")) {
				this.logger.debug("inside if condition");
				int wipCasesCount = this.userMultiActionManager.checkForWIPCases(userID);
				this.logger.debug("wipCasesCount = " + wipCasesCount);
				if (wipCasesCount > 0) {
					modelAndView.addObject("updateFail", true);
				}
			}

			return modelAndView;
		} catch (CMSException var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		} catch (Exception var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		}
	}

	public ModelAndView userFullNameExist(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String userFullName = request.getParameter("name");
		boolean var6 = false;

		try {
			int count = this.userMultiActionManager.userFullNameExist(userFullName);
			if (count > 0) {
				modelAndView.addObject("isFullNameExist", true);
			} else {
				modelAndView.addObject("isFullNameExist", false);
			}

			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView emailIdExist(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String mailId = request.getParameter("mailId");

		try {
			boolean isEmailExist = this.userMultiActionManager.isEmailExist(mailId);
			if (isEmailExist) {
				modelAndView.addObject("emailIdExist", true);
			} else {
				modelAndView.addObject("emailIdExist", false);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getUserIDs(HttpServletRequest request, HttpServletResponse response, UserMasterVO userMasterVO)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> userList = this.userMultiActionManager.getAllUserID();
			modelAndView.addObject("allUserId", userList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView user(HttpServletRequest request, HttpServletResponse response, UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			new ArrayList();
			new ArrayList();
			ArrayList<UserMasterVO> userList = (ArrayList) this.userMultiActionManager.selectAllUser();
			modelAndView.addObject("userList", userList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView supervisorinfo(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			new ArrayList();
			ArrayList<UserMasterVO> supervisorList = (ArrayList) this.userMultiActionManager.selectSupervisor();
			modelAndView.addObject("supervisorList", supervisorList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView roleInfo(HttpServletRequest request, HttpServletResponse response,
			UserRoleMapVO userRoleMapVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			new ArrayList();
			ArrayList<UserRoleMapVO> roleList = (ArrayList) this.userMultiActionManager.roleInfo();
			modelAndView.addObject("success", true);
			modelAndView.addObject("roleList", roleList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView displayDetails(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String uid = userMasterVO.getUserID();
			this.logger.debug("INside controler of DD of ADD_User===" + uid);
			new ArrayList();
			ArrayList<UserMasterVO> add_userList = (ArrayList) this.userMultiActionManager.displayDetails(userMasterVO);
			modelAndView.addObject("add_userList", add_userList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView displayBackup(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("INside controler of backup DD of ADD_User");
			ArrayList<UserMasterVO> add_backUpList = null;
			new ArrayList();
			String officeCode = request.getParameter("action");
			String userToUpdate = request.getParameter("userToUpdate");
			this.logger.debug("=========>>" + officeCode + userToUpdate);
			int count = this.userMultiActionManager.checkBackUp(officeCode, userToUpdate);
			if (count == 0) {
				this.logger.debug("inside if of getting backup::");
				add_backUpList = (ArrayList) this.userMultiActionManager.backupWithotUsr();
			} else {
				this.logger.debug("inside else of getting backup::");
				add_backUpList = (ArrayList) this.userMultiActionManager.displayBackup(officeCode, userToUpdate);
			}

			modelAndView.addObject("add_backUpList", add_backUpList);
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView deActivateUser(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		int countSuper = 0;
		int countBackup1 = 0;
		int activeCase = 0;
		int recurrenceActiveCase = 0;
		int isAssociatedWithClient = 0;
		String usrWithRoleR3 = "";
		boolean resultValue = true;
		String activeCases = "";

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String updated_by = userBean.getUserName();
			String message = "deActivate User Method";
			this.logger.debug(message);
			String userID = request.getParameter("action");
			String ustatus = request.getParameter("ustatus");
			String validate = request.getParameter("validateStatus");
			String deactiveFlag = request.getParameter("beforDeactive");
			String isOffHeadPre = request.getParameter("isOHPre");
			String loginId = request.getParameter("loginIdVal");
			if (ustatus.equalsIgnoreCase("0") && validate.equalsIgnoreCase("true")) {
				this.logger.debug("============>>>Inside count before deactivate");
				activeCase = this.userMultiActionManager.chkActiveCaseBeforeDeactivateUser(userID);
				activeCases = this.userMultiActionManager.chkActiveCaseListBeforeDeactivateUser(userID);
				recurrenceActiveCase = this.userMultiActionManager.chkActiveRecurrenceCaseBeforeDeactivateUser(userID);
				isAssociatedWithClient = this.userMultiActionManager.checkAssociatedWithClientBforDeact(userID);
				this.logger.debug("============>>>Inside Sup-count before deactivate" + countSuper);
				this.logger.debug("============>>>Inside Back-count before deactivate" + countBackup1);
				this.logger.debug("============>>>Inside ActiveCase-count before deactivate" + activeCase);
				this.logger.debug("============>>>Inside Associated-count before deactivate" + isAssociatedWithClient);
				this.logger.debug("============>>>Inside Recurrence Case before deactivate" + recurrenceActiveCase);
			}

			if (activeCase <= 0 && recurrenceActiveCase <= 0 && isAssociatedWithClient <= 0) {
				if (deactiveFlag.equals("false")) {
					usrWithRoleR3 = this.userMultiActionManager.checkForR3(userID);
					this.logger.debug(".................role R3 Count" + usrWithRoleR3);
					this.userMultiActionManager.deactivateUser(userID, ustatus, isOffHeadPre, loginId, updated_by);
					if (usrWithRoleR3 != null && !usrWithRoleR3.equals("") && ustatus.equalsIgnoreCase("0")) {
						resultValue = ResourceLocator.self().getSBMService().isUserRemovedFromQueue(userID);
					}

					if (usrWithRoleR3 != null && !usrWithRoleR3.equals("") && ustatus.equalsIgnoreCase("1")) {
						resultValue = ResourceLocator.self().getSBMService().isUserAddedToQueue(userID);
					}

					if (!resultValue) {
						this.logger.debug("Warning: Not Able to modify User in Savvion Queue.");
					}
				}
			} else {
				this.logger.debug("============>>>Inside count > 0");
				modelAndView.addObject("activeCase", activeCase);
				modelAndView.addObject("recurrenceActiveCase", recurrenceActiveCase);
				modelAndView.addObject("associatedClientsCount", isAssociatedWithClient);
				modelAndView.addObject("activeCaseList", activeCases);
			}

			modelAndView.addObject("success", "true");
			if (ustatus != null && ustatus.equalsIgnoreCase("1")) {
				this.logger.info("userMasterId " + userID + "successfully Activated.");
			} else {
				this.logger.info("userMasterId " + userID + "successfully Deactivated.");
			}

			return modelAndView;
		} catch (CMSException var22) {
			return AtlasUtils.getJsonExceptionView(this.logger, var22, response);
		} catch (Exception var23) {
			return AtlasUtils.getJsonExceptionView(this.logger, var23, response);
		}
	}

	public ModelAndView deactivateOnUpdate(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		int countSuper = 0;
		int countBackup1 = 0;
		int activeCase = false;
		int recurrenceActiveCase = false;
		int isAssociatedWithClient = false;
		String activeCases = "";

		try {
			String message = "deActivate On Update";
			this.logger.debug(message);
			String userID = request.getParameter("action");
			String ustatus = request.getParameter("ustatus");
			this.logger.debug("============>>>Inside count before deactivate");
			int activeCase = this.userMultiActionManager.chkActiveCaseBeforeDeactivateUser(userID);
			activeCases = this.userMultiActionManager.chkActiveCaseListBeforeDeactivateUser(userID);
			int recurrenceActiveCase = this.userMultiActionManager.chkActiveRecurrenceCaseBeforeDeactivateUser(userID);
			int isAssociatedWithClient = this.userMultiActionManager.checkAssociatedWithClientBforDeact(userID);
			this.logger.debug("============>>>Inside count before deactivate" + countSuper);
			this.logger.debug("============>>>Inside count before deactivate" + countBackup1);
			this.logger.debug("============>>>Inside ActiveCase-count before deactivate" + activeCase);
			this.logger.debug("============>>>Inside ActiveRecuCase-count before deactivate" + recurrenceActiveCase);
			this.logger.debug("============>>>Inside Associated-count before deactivate" + isAssociatedWithClient);
			this.logger.debug("============>>>Inside Associated-case list before deactivateactiveCaseList");
			if (activeCase > 0) {
				this.logger.debug("============>>>Inside count > 0");
				modelAndView.addObject("success", "false");
				modelAndView.addObject("superCount", Integer.valueOf(countSuper));
				modelAndView.addObject("backup1Count", Integer.valueOf(countBackup1));
				modelAndView.addObject("activeCase", activeCase);
				modelAndView.addObject("activeRecuCaseCount", recurrenceActiveCase);
				modelAndView.addObject("associatedClientsCount", isAssociatedWithClient);
				modelAndView.addObject("activeCaseList", activeCases);
			}

			modelAndView.addObject("success", "true");
			return modelAndView;
		} catch (CMSException var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		} catch (Exception var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		}
	}

	public ModelAndView searchUser(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("inside searh user >>>>  ");
			this.logger.debug("status :::::: " + userMasterVO.getStatus());
			this.logger.debug("status :::::: " + userMasterVO.getUsername());
			this.logger.debug("status :::::: " + userMasterVO.getUserID());
			new ArrayList();
			new ArrayList();
			ArrayList searchResult;
			if (userMasterVO.getUsername().equalsIgnoreCase("") && userMasterVO.getUserID().equalsIgnoreCase("")
					&& userMasterVO.getSupervisor().equalsIgnoreCase("")
					&& userMasterVO.getStatus().equalsIgnoreCase("")
					&& userMasterVO.getLocalLanguage().equalsIgnoreCase("")
					&& userMasterVO.getOffice().equalsIgnoreCase("")) {
				searchResult = new ArrayList();
			} else {
				searchResult = (ArrayList) this.userMultiActionManager.searchUser(userMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				int count = this.userMultiActionManager.searchUserCount(userMasterVO);
				modelAndView.addObject("total", count);
			}

			modelAndView.addObject("searchResult", searchResult);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getUserDetailForClient(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			new ArrayList();
			List<UserMasterVO> associateDetailList = this.userMultiActionManager.getAssociateDetail();
			modelAndView.addObject("associateDetail", associateDetailList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getCountryMasterDetailValue(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new ArrayList();

		try {
			ArrayList<UserMasterVO> searchCountry = (ArrayList) this.userMultiActionManager
					.searchCountry(countryMasterVO);
			int count = this.userMultiActionManager.searchCountryCount(countryMasterVO);
			modelAndView.addObject("searchResult", searchCountry);
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getUserForRoles(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String roleId = request.getParameter("roleID");
			this.logger.debug("roleId is " + roleId);
			List<UserMasterVO> userList = this.userMultiActionManager.getUsersForRole(roleId);
			this.logger.debug("userList size " + userList.size());
			modelAndView.addObject("userList", userList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView checkTimeTrackerForR3(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			TimeTrackerVO vo = new TimeTrackerVO();
			String userId = request.getParameter("userId");
			vo.setUserName(userId);
			vo.setTaskName("Invoicing Task");
			boolean ttFlag = ResourceLocator.self().getTimeTrackerService().checkTimeTrackerForUserTask(vo);
			if (ttFlag) {
				this.logger.debug("R3 role cannot be removed for the user");
			} else {
				this.logger.debug("R3 role can be removed.");
			}

			modelAndView.addObject("canChange", ttFlag);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getUserFullNameFromID(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String userId = request.getParameter("userId");
		this.logger.debug("User Id is " + userId);
		if (userId != null && !"".equalsIgnoreCase(userId)) {
			try {
				String userName = this.userMultiActionManager.getUserFullNameFromId(userId);
				this.logger.debug("user full name is " + userName);
				modelAndView.addObject("userName", userName);
			} catch (CMSException var6) {
				return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
			} catch (Exception var7) {
				return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
			}
		}

		return modelAndView;
	}

	public ModelAndView getBDMRegionList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<BDMRegionVO> bdmRegionList = this.userMultiActionManager.getBDMRegionList();
			modelAndView.addObject("bdmRegionList", bdmRegionList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView checkForbdmWIPCases(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String userID = request.getParameter("userID");
		boolean updateFail = false;
		this.logger.debug("userID is " + userID);

		try {
			int wipCasesCount = this.userMultiActionManager.checkForWIPCases(userID);
			this.logger.debug("wipCasesCount = " + wipCasesCount);
			if (wipCasesCount > 0) {
				updateFail = true;
			}

			modelAndView.addObject("updateFail", updateFail);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getUserMasterHistory(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug(
					"Inside getUserMasterHistory#### " + userMasterVO.getUserMasterId() + " User Master ID >>>   ");
			AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			int count = false;
			atlasHistoryVO.setHistoryKey("" + userMasterVO.getUserMasterId());
			atlasHistoryVO.setStart(start + 1);
			atlasHistoryVO.setLimit(start + limit);
			atlasHistoryVO.setSortColumnName(request.getParameter("sort"));
			atlasHistoryVO.setSortType(request.getParameter("dir"));
			List<AtlasHistoryVO> userHistoryList = this.userMultiActionManager.getUserHistory(atlasHistoryVO);
			int count = this.userMultiActionManager.getUserHistoryCount(atlasHistoryVO);
			this.logger.debug("List Size:::::: " + userHistoryList.size());
			modelAndView.addObject("userMasterIdList", userHistoryList);
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}