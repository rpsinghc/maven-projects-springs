package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bizmanage.pagegenerator.ServletTools;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.savvion.sbm.bpmportal.util.CryptUtils;
import com.savvion.sbm.util.PService;
import com.worldcheck.atlas.bl.interfaces.IMasterDataValidator;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONUpdatePasswordController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONUpdatePasswordController");
	private static final String AUTHTWO = "password";
	private static final String SUCCESS = "Success";
	private static final String FAIL = "Fail";
	private static final String UPDATE_AUTHTWO = "UpdatePassword";
	private static final String TRUE = "true";
	private static final String USER_MASTER = "userMaster";
	private IMasterDataValidator masterDataValidator;
	private String JSONVIEW = "jsonView";
	private String MESSAGE = "message";
	private String CURRENT_CRED = "currentPassword";
	private static final String FALSE = "false";
	private String USER = "user";
	String REDIRECT_LOGIN_PAGE = "redirect:/bpmportal/login.jsp";

	public void setMasterDataValidator(IMasterDataValidator masterDataValidator) {
		this.masterDataValidator = masterDataValidator;
	}

	public ModelAndView savePassword(HttpServletRequest request, HttpServletResponse response) {
		String message = "";
		this.logger.debug("Inside Save Password");
		return new ModelAndView("updatePassword_custom", "message", message);
	}

	public ModelAndView updatepassword(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
			throws Exception {
		this.logger.debug("Going to validate new password");

		try {
			String credEntered = ServletTools.getUTFString(httpservletrequest, "password");
			String userName = ServletTools.getUTFString(httpservletrequest, "userName");
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			credEntered = CryptUtils.decrypt(userName, credEntered, "-");
			String credEncr = PService.self().encrypt(credEntered);
			this.logger.debug("encrypted password " + credEncr);
			httpservletresponse.setContentType("text/json;charset=UTF-8");
			boolean isValidCred = this.masterDataValidator.performPasswordValidation(String.valueOf(userId), credEncr);
			this.logger.debug("is valid password " + isValidCred);
			if (isValidCred) {
				httpservletrequest.getSession().setAttribute("UpdatePassword", "true");
				httpservletresponse.getWriter().write("Success");
			} else {
				httpservletresponse.setStatus(400);
				httpservletresponse.getWriter().write("Fail");
			}

			httpservletresponse.getWriter().flush();
			httpservletresponse.getWriter().close();
			return null;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpservletresponse);
		}
	}

	public ModelAndView getUpdatePasswordUserList(HttpServletRequest request, HttpServletResponse httpservletresponse)
			throws Exception {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			List<UserMasterVO> userMasterVOList = new ArrayList();
			List roleList = userDetailsBean.getRoleList();
			if (roleList.contains("R1")) {
				userMasterVOList = ResourceLocator.self().getUserService().getActiveUserList();
				this.logger.debug(" user master vo list " + userMasterVOList);
			} else {
				UserMasterVO userMasterVO = new UserMasterVO();
				userMasterVO.setUserID(userBean.getUserName());
				userMasterVO.setUsername(userBean.getUserName());
				((List) userMasterVOList).add(userMasterVO);
			}

			viewForJson.addObject("userMaster", userMasterVOList);
			return viewForJson;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpservletresponse);
		}
	}

	public ModelAndView validDateResetPassword(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) {
		this.logger.debug("Entring JSONUPDATEPASSOWRD:validDateResetPassword");
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);

		try {
			String userName = ServletTools.getUTFString(httpservletrequest, "userName");
			String credEntered = ServletTools.getUTFString(httpservletrequest, "password");
			String cuurentCred = ServletTools.getUTFString(httpservletrequest, this.CURRENT_CRED);
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			viewForJson.addObject(this.USER, userName);
			if (userId == 0L) {
				viewForJson.addObject("Success", "false");
				viewForJson.addObject(this.MESSAGE, "Invalid UserName/Password");
				return viewForJson;
			}

			cuurentCred = CryptUtils.decrypt(userName, cuurentCred, "-");
			String cuurentCredEncr = PService.self().encrypt(cuurentCred);
			boolean isTempCredValid = this.masterDataValidator.performResetPasswordValidation(String.valueOf(userId),
					cuurentCredEncr);
			if (!isTempCredValid) {
				viewForJson.addObject("Success", "false");
				viewForJson.addObject(this.MESSAGE, "Invalid UserName/Password");
				return viewForJson;
			}

			credEntered = CryptUtils.decrypt(userName, credEntered, "-");
			String credEncr = PService.self().encrypt(credEntered);
			boolean isValidCred = this.masterDataValidator.performPasswordValidation(String.valueOf(userId), credEncr);
			if (!isValidCred) {
				viewForJson.addObject("Success", "false");
				viewForJson.addObject(this.MESSAGE, "Password should not be the same as the last six passwords used");
				return viewForJson;
			}

			if (userId > 0L && isTempCredValid && isValidCred) {
				viewForJson.addObject("Success", "true");
				viewForJson.addObject(this.MESSAGE, "Success");
				return viewForJson;
			}
		} catch (CMSException var13) {
			new CMSException(this.logger, var13);
		}

		return viewForJson;
	}

	public ModelAndView saveNewPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("Entring JSONUPDATEPASSWORDCONROLLER :saveNewPassword ");
		ModelAndView mv = new ModelAndView("jsonView");
		String userName = ServletTools.getUTFString(request, "userName");
		String newCred = ServletTools.getUTFString(request, "password");
		newCred = CryptUtils.decrypt(userName, newCred, "-");
		boolean isCredUpdated = false;

		try {
			isCredUpdated = ResourceLocator.self().getSBMService().isPaswordUpdated(userName, newCred);
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}

		if (isCredUpdated) {
			this.logger.info("password updated successfully");
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			ResourceLocator.self().getUserService().updatePasswordExpiryDate(userName);
			ResourceLocator.self().getUserService().removeTempPassword("" + userId);
			mv.addObject(this.MESSAGE, "Password Updation Successfully");
			mv.addObject("Success", "true");
			return mv;
		} else {
			this.logger.info("password updation failed");
			mv.addObject(this.MESSAGE, "Password Updation Failed");
			mv.addObject("Success", "Fail");
			return mv;
		}
	}

	public ModelAndView customValidationforUpdatePassword(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws Exception {
		this.logger.debug("Entring JSONUPDATEPASSWORDCONTROLLER:customValidationforUpdatePassword");

		try {
			String credEntered = ServletTools.getUTFString(httpservletrequest, this.CURRENT_CRED);
			String userName = ServletTools.getUTFString(httpservletrequest, "userName");
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			credEntered = CryptUtils.decrypt(userName, credEntered, "-");
			String encrEncr = PService.self().encrypt(credEntered);
			httpservletresponse.setContentType("text/json;charset=UTF-8");
			boolean isValidCred = this.masterDataValidator.performPasswordValidation(String.valueOf(userId), encrEncr);
			this.logger.debug("is valid password " + isValidCred);
			if (isValidCred) {
				httpservletrequest.getSession().setAttribute("password", "true");
				httpservletresponse.getWriter().write("Success");
			} else {
				httpservletresponse.setStatus(400);
				httpservletresponse.getWriter().write("Fail");
			}

			httpservletresponse.getWriter().flush();
			httpservletresponse.getWriter().close();
			return null;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpservletresponse);
		}
	}

	public ModelAndView updatepasswordProfile(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws Exception {
		this.logger.debug("Entring JSONUpdatePasswordController : updatepasswordProfile ");
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		boolean isValidCred = true;

		try {
			String credEntered = ServletTools.getUTFString(httpservletrequest, "password");
			String userName = ServletTools.getUTFString(httpservletrequest, "userName");
			String currentCred = ServletTools.getUTFString(httpservletrequest, "currentPassword");
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			credEntered = CryptUtils.decrypt(userName, credEntered, "-");
			String CredEncr = PService.self().encrypt(credEntered);
			currentCred = CryptUtils.decrypt(userName, currentCred, "-");
			String currentCredEncrypted = PService.self().encrypt(currentCred);
			String UserCurrentCred = ResourceLocator.self().getSBMService().getPassword(userName);
			boolean isCurrentValidCred = this.masterDataValidator.performCurrentPasswordValidation(UserCurrentCred,
					currentCredEncrypted);
			this.logger.debug("is Current valid password " + isCurrentValidCred);
			if (!isCurrentValidCred) {
				viewForJson.addObject("Success", "false");
				viewForJson.addObject(this.MESSAGE,
						"Invalid Password! Either the Old password does not match or New password does not meet the minimum Password complexity requirement.");
				return viewForJson;
			} else {
				UserMasterVO userMasterVO = ResourceLocator.self().getUserService().getUserPassExpiryDetails(userName);
				if (userMasterVO.getIsNewUser() != 1) {
					isValidCred = this.masterDataValidator.performPasswordValidation(String.valueOf(userId), CredEncr);
				}

				this.logger.debug("is valid password " + isValidCred);
				if (!isValidCred) {
					viewForJson.addObject("Success", "false");
					viewForJson.addObject(this.MESSAGE,
							"Password should not be the same as the last six passwords used");
					return viewForJson;
				} else if (isCurrentValidCred && isValidCred) {
					viewForJson.addObject("Success", "true");
					viewForJson.addObject(this.MESSAGE, "Success");
					return viewForJson;
				} else {
					return viewForJson;
				}
			}
		} catch (Exception var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, httpservletresponse);
		}
	}

	public ModelAndView saveNewPasswordProfile(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("Entring JSONUPDATEPASSWORDCONROLLER :saveNewPasswordProfile ");
		ModelAndView mv = new ModelAndView("jsonView");
		String userName = ServletTools.getUTFString(request, "userName");
		String newCred = ServletTools.getUTFString(request, "password");
		newCred = CryptUtils.decrypt(userName, newCred, "-");
		boolean isCredUpdated = false;

		try {
			isCredUpdated = ResourceLocator.self().getSBMService().isPaswordUpdated(userName, newCred);
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}

		if (isCredUpdated) {
			this.logger.info("password updated successfully");
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			ResourceLocator.self().getUserService().updatePasswordExpiryDate(userName);
			int count = ResourceLocator.self().getUserService().getTempPassCount("" + userId);
			this.logger.debug("Temparory Password count:" + count);
			if (count > 0) {
				ResourceLocator.self().getUserService().removeTempPassword("" + userId);
			}

			mv.addObject(this.MESSAGE, "You Password Updated Successfully Please login ");
			mv.addObject("Success", "true");
			return mv;
		} else {
			this.logger.info("password updation failed");
			mv.addObject(this.MESSAGE, "Password Updation Failed");
			mv.addObject("Success", "Fail");
			return mv;
		}
	}
}