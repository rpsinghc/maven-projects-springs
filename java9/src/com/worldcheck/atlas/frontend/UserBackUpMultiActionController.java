package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.savvion.sbm.bpmportal.bizpass.BizPassUtilities;
import com.worldcheck.atlas.bl.backup.UserBackUpManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.UserBackUpVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public class UserBackUpMultiActionController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.UserBackupMultiActionController");
	private UserBackUpManager userBackUpManager;

	public void setUserBackUpManager(UserBackUpManager userBackUpManager) {
		this.userBackUpManager = userBackUpManager;
	}

	public ModelAndView switchUserSession(HttpServletRequest request, HttpServletResponse response,
			UserBackUpVO userBackUpVO) throws Exception {
		Object modelAndView = null;

		try {
			this.logger.debug(
					"inside UserBackUpMultiActionController::switchUserSession ->  userid:" + userBackUpVO.getUserId());
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("<**********From UserBean      LoginId: " + userBean.getUserName() + "************>");
			String password = this.userBackUpManager.switchUserSession(userBackUpVO.getUserId());
			HttpSession session = request.getSession();
			this.logger.debug("LoginLevel ::" + session.getAttribute("loginLevel"));
			if (request.getSession().getAttribute("loginLevel") != null
					&& !request.getSession().getAttribute("loginLevel").equals("")) {
				int loginLevel = (Integer) request.getSession().getAttribute("loginLevel");
				if (loginLevel < 2) {
					++loginLevel;
					String userName1 = (String) request.getSession().getAttribute("userName1");
					session.invalidate();
					request.getSession().setAttribute("loginLevel", loginLevel);
					request.getSession().setAttribute("userName1", userName1);
					request.getSession().setAttribute("userFullName1",
							ResourceLocator.self().getUserService().getUserFullNameFromId(userName1));
					request.getSession().setAttribute("userName2", userBean.getUserName());
					request.getSession().setAttribute("userFullName2",
							ResourceLocator.self().getUserService().getUserFullNameFromId(userBean.getUserName()));
				}
			} else {
				int loginLevel = 1;
				session.invalidate();
				request.getSession().setAttribute("loginLevel", Integer.valueOf(loginLevel));
				request.getSession().setAttribute("userName1", userBean.getUserName());
				request.getSession().setAttribute("userFullName1",
						ResourceLocator.self().getUserService().getUserFullNameFromId(userBean.getUserName()));
			}

			BizPassUtilities.sendPostRedirect(response, "/sbm/bpmportal/myhome/redirect", userBackUpVO.getUserId(),
					password);
			return (ModelAndView) modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView logOutUserSession(HttpServletRequest request, HttpServletResponse response,
			UserBackUpVO userBackUpVO) throws Exception {
		Object modelAndView = null;

		try {
			String loginId = null;
			String password = null;
			HttpSession session = request.getSession();
			this.logger.debug("Login Level:" + request.getSession().getAttribute("loginLevel"));
			if ((Integer) session.getAttribute("loginLevel") == 1
					|| (Integer) session.getAttribute("loginLevel") == 2) {
				loginId = (String) session.getAttribute("userName1");
				session.invalidate();
				password = this.userBackUpManager.switchUserSession(loginId);
			}

			BizPassUtilities.sendPostRedirect(response, "/sbm/bpmportal/myhome/redirect", loginId, password);
			return (ModelAndView) modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView userBackUp(HttpServletRequest request, HttpServletResponse response, UserBackUpVO userBackUpVO)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("apps_custom");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}
}