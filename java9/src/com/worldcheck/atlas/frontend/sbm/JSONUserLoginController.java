package com.worldcheck.atlas.frontend.sbm;

import com.savvion.sbm.util.PService;
import com.worldcheck.atlas.bl.interfaces.IMasterDataValidator;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.sbm.SBMService;
import com.worldcheck.atlas.utils.ExposePropertyPlaceholderConfigurer;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JSONUserLoginController extends MultiActionController {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.sbm.JSONUserLoginController");
	private ResourceLocator locator = null;
	private final String JSON_VIEW = "jsonView";
	private static final String USER_NAME = "userName";
	private static final String USER_STATUS = "userStatus";
	private static final String USER_LOGIN_STATUS = "userLoginStatus";
	private static final String IS_AUTHTWO_OK = "isPasswordOk";
	private static final String STATUS = "status";
	private static final String ONE = "1";
	private static final String USER_SESSION_STATUS = "userSessionStatus";
	private String m_subject = "Atlas Reset Password Link";
	private String m_message_Forgot = null;
	private static final String CREDENTIAL = "password";
	ExposePropertyPlaceholderConfigurer propertyConfigurer = null;
	private IMasterDataValidator masterDataValidator;
	private static SecureRandom random = new SecureRandom();

	public void setMasterDataValidator(IMasterDataValidator masterDataValidator) {
		this.masterDataValidator = masterDataValidator;
	}

	public void setPropertyConfigurer(ExposePropertyPlaceholderConfigurer propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

	public ModelAndView getLoginUserStatus(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("inside getLoginUserStatus name for retrieve userStatus");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			String userName = "";
			if (httpServletRequest.getParameter("userName") != null) {
				userName = httpServletRequest.getParameter("userName");
			}

			this.logger.debug("userName is" + userName + "Testing " + httpServletRequest.getHeader("User-Agent"));
			long userId = ResourceLocator.self().getSBMService().getUserID(userName);
			if (userId == 0L) {
				viewForJSON.addObject("userStatus", -1);
				return viewForJSON;
			}

			if (userId > 0L) {
				Map<String, String> map = new HashMap();
				map.put("unlocked_by", "");
				map.put("userID", userName);
				map.put("isReset", "false");
				this.locator.getUserService().updateUserLoginAttempt(map);
			}

			String credentialUI = httpServletRequest.getParameter("password");
			String tempEncrycredential = this.locator.getUserService().getTempPassword("" + userId);
			String tempcredential = PService.self().decrypt(tempEncrycredential);
			boolean iscredentialUpdated = false;
			if (credentialUI.equals(tempcredential)) {
				iscredentialUpdated = ResourceLocator.self().getSBMService().isPaswordUpdated(userName, tempcredential);
				this.logger.debug("Password Updated in savvion: " + iscredentialUpdated);
			}

			String currentcredentialEncrypted = PService.self().encrypt(credentialUI);
			String UserCurrentcredential = ResourceLocator.self().getSBMService().getPassword(userName);
			this.logger.debug("UserCurrentcredential::::::::::::::" + UserCurrentcredential);
			this.logger.debug("currentcredentialEncrypted::::::::::::::" + currentcredentialEncrypted);
			boolean isCurrentValidcredential = this.masterDataValidator
					.performCurrentPasswordValidation(UserCurrentcredential, currentcredentialEncrypted);
			this.logger.debug("REsult :::" + this.masterDataValidator
					.performCurrentPasswordValidation(UserCurrentcredential, currentcredentialEncrypted));
			this.logger.debug("isCurrentValidcredential::::::::::::::" + isCurrentValidcredential);
			if (!isCurrentValidcredential) {
				viewForJSON.addObject("userStatus", -1);
				return viewForJSON;
			}

			int allowInvalidAttempt = Integer
					.parseInt((String) this.propertyConfigurer.getResolvedProps().get("atlas.login.attempt"));
			this.logger.debug("allowInvalidAttempt::::::::::::::" + allowInvalidAttempt);
			int loginAttempt = this.locator.getUserService().getUserLoginAttempt(userName, allowInvalidAttempt);
			this.logger.debug("loginAttempt::::::::::::::" + loginAttempt);
			if (loginAttempt > allowInvalidAttempt) {
				viewForJSON.addObject("userStatus", 3);
				return viewForJSON;
			}

			if (this.checkLoginStatus(userName)) {
				viewForJSON.addObject("userLoginStatus", 1);
				this.logger.info(userName + " trying to login again from " + httpServletRequest.getLocalAddr());
			} else {
				viewForJSON.addObject("userLoginStatus", 0);
			}

			viewForJSON.addObject("userStatus", this.getUserMasterInfo(userName, httpServletRequest).getStatus());
			this.logger.debug("After checking user status");
		} catch (NullPointerException var16) {
			new CMSException(this.logger, var16);
		} catch (Exception var17) {
			new CMSException(this.logger, var17);
		}

		return viewForJSON;
	}

	public ModelAndView getUserStatus(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ClassNotFoundException {
		this.logger.debug("inside getUserStatus for forgot password");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			String userName = "";
			if (httpServletRequest.getParameter("userName") != null) {
				userName = httpServletRequest.getParameter("userName");
			}

			this.logger.debug("userName is" + userName);
			String userNameEncr = URLEncoder.encode(PService.self().encrypt(userName), "UTF-8");
			UserMasterVO masterVO = this.getUserMasterInfo(userName, httpServletRequest);
			viewForJSON.addObject("status", masterVO.getStatus());
			int allowInvalidAttempt = Integer
					.parseInt((String) this.propertyConfigurer.getResolvedProps().get("atlas.login.attempt"));
			int loginAttempt = this.locator.getUserService().getUserLoginAttempt(userName, allowInvalidAttempt);
			if ("1".equals(masterVO.getStatus()) && loginAttempt <= allowInvalidAttempt) {
				String userId = masterVO.getUserIdVal();
				this.logger.debug("userId" + userId);
				String isCreated = "false";
				String token = "";
				if (this.locator.getUserService().checkUserIdForTemp(userId)) {
					token = this.locator.getUserService().getToken(userId);
					isCreated = "success";
				} else {
					token = (new BigInteger(130, random)).toString(32).toString();
					isCreated = this.locator.getUserService().insertTempPassword(token, userId);
				}

				this.logger.debug("Token is =" + token + "userName Encr:::" + userNameEncr);
				String resetPasswordURL = (String) this.propertyConfigurer.getResolvedProps()
						.get("atlas.forgotcred.url");
				if (isCreated.equalsIgnoreCase("success")) {
					this.logger.debug("Configure" + resetPasswordURL);
					resetPasswordURL = resetPasswordURL + "?" + "Param1" + "=" + token + "&" + "Param2" + "="
							+ userNameEncr;
					URL url = new URL(resetPasswordURL);
					this.logger.debug("URL is" + url);
					SBMService sbmService = this.locator.getSBMService();
					this.m_message_Forgot = "You have requested forgot password for the your Login ID : "
							+ userName.toLowerCase() + "\n\r"
							+ "Click on the link below to initiate the password reset process. \n\r" + url + "\n\r"
							+ "(If the link does not work, please copy the full address above and paste it to your Internet browser)\n\r"
							+ "If you did not request for your password to be reset, please ignore this email.\n\r"
							+ "Be assured that your login details are very secure.\n\r"
							+ "\n\r Thank you.\n\rThomson Reuters \n\r\n\rrisk.thomsonreuters.com \n\r*********************************************************************************************** \n\rThis email was sent to you by Thomson Reuters,the global news and Information Company.\r Any views expressed in this message are those of the individual sender,\rexcept where the sender specifically states them to be the views of Thomson Reuters. \r************************************************************************************************ ";
					this.locator.getMailService().sendEmail(this.m_subject,
							sbmService.getEmailId(userName.toLowerCase()), this.m_message_Forgot);
					this.m_message_Forgot = "";
				}
			} else {
				viewForJSON.addObject("status", "0");
			}

			this.logger.debug("After checking user status");
		} catch (NullPointerException var15) {
			new CMSException(this.logger, var15);
		} catch (Exception var16) {
			new CMSException(this.logger, var16);
		}

		return viewForJSON;
	}

	public ModelAndView setSessionLogout(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("inside setSessionLogout");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			HttpSession session = httpServletRequest.getSession();
			this.logger.debug("User details are " + session.getAttribute("userDetailsBean"));
			this.logger.debug("Max inactive interval is " + session.getMaxInactiveInterval());
			if (session.getAttribute("userDetailsBean") == null) {
				viewForJSON.addObject("userSessionStatus", 0);
			} else {
				viewForJSON.addObject("userSessionStatus", 1);
			}

			session.setMaxInactiveInterval(session.getMaxInactiveInterval());
		} catch (Exception var5) {
			new CMSException(this.logger, var5);
		}

		return viewForJSON;
	}

	private UserMasterVO getUserMasterInfo(String userName, HttpServletRequest httpServletRequest)
			throws CMSException, IOException {
		this.locator = ResourceLocator.self();
		UserMasterVO masterVO = this.locator.getUserService().getUserInfo(userName);
		if (masterVO != null) {
			BufferedWriter writer = null;

			try {
				SimpleDateFormat dt1 = new SimpleDateFormat("MM-dd-yyyy");
				String userAgent = httpServletRequest.getHeader("User-Agent");
				httpServletRequest.getSession().setAttribute("userAgent", userAgent);
				String loginLogspath = ((String) this.propertyConfigurer.getResolvedProps().get("atlas.loginlogs.path"))
						.toString();
				File file = new File(loginLogspath + "\\AtlasLogin-" + dt1.format(new Date()) + ".csv");
				if (file.exists()) {
					writer = new BufferedWriter(new FileWriter(file, true));
				} else {
					writer = new BufferedWriter(new FileWriter(file));
					writer.write("UserID,UserName,LoginTime,LogoutTime,MachineInfo,Comment");
					writer.newLine();
				}

				writer.write(userName + "," + masterVO.getUsername() + "," + new Date() + ",,\"" + userAgent + "\","
						+ "Proper Login from the IP :" + httpServletRequest.getRemoteAddr());
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (Exception var17) {
				;
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception var16) {
					;
				}

			}
		}

		return masterVO;
	}

	private boolean checkLoginStatus(String userName) throws CMSException {
		boolean status = false;
		HashMap<String, String> statusMap = this.locator.getCacheService().getACLTokenCache();
		if (statusMap != null) {
			status = statusMap.containsKey(userName);
		}

		return status;
	}

	public ModelAndView logUserLoginStatus(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("Entring JsonUserLoginController :logUserLoginStatus ");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			String userName = "";
			if (httpServletRequest.getParameter("userName") != null) {
				userName = httpServletRequest.getParameter("userName");
			}

			String ipAdd = httpServletRequest.getRemoteAddr();
			this.logger.info(userName + " trying to login again from " + ipAdd);
			this.logMulpleLoginlog(userName, ipAdd, httpServletRequest);
			this.logger.debug("Logged details");
		} catch (NullPointerException var6) {
			new CMSException(this.logger, var6);
		} catch (Exception var7) {
			new CMSException(this.logger, var7);
		}

		return viewForJSON;
	}

	private void logMulpleLoginlog(String userName, String ipAdd, HttpServletRequest httpServletRequest)
			throws CMSException, IOException {
		this.logger.debug("Entring JsonUserLoginController :logMulpleLoginlog");
		this.locator = ResourceLocator.self();
		UserMasterVO masterVO = this.locator.getUserService().getUserInfo(userName);
		if (masterVO != null) {
			BufferedWriter writer = null;

			try {
				SimpleDateFormat dt1 = new SimpleDateFormat("MM-dd-yyyy");
				String userAgent = httpServletRequest.getHeader("User-Agent");
				httpServletRequest.getSession().setAttribute("userAgent", userAgent);
				String loginLogspath = ((String) this.propertyConfigurer.getResolvedProps().get("atlas.loginlogs.path"))
						.toString();
				File file = new File(loginLogspath + "\\AtlasConcurrentLogin-" + dt1.format(new Date()) + ".csv");
				if (file.exists()) {
					writer = new BufferedWriter(new FileWriter(file, true));
				} else {
					writer = new BufferedWriter(new FileWriter(file));
					writer.write("UserID,UserName,LoginTime,IPAddress,MachineInfo,Comment");
					writer.newLine();
				}

				writer.write(userName + "," + masterVO.getUsername() + "," + new Date() + "," + ipAdd + "," + userAgent
						+ "," + "Concurrent login attempt");
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (Exception var18) {
				;
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception var17) {
					;
				}

			}
		}

	}
}