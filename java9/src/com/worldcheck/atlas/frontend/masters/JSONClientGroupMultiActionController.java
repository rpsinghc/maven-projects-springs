package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IClientGroup;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientGroupMasterVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONClientGroupMultiActionController extends JSONMultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.JSONClientGroupMultiActionController";
	private ILogProducer logger;
	private static final String JSON_VIEW = "jsonView";
	private static final String HISTORY_LIST = "historyList";
	private static final String SUCCESS = "success";
	private String ACTION;
	private static final String success = "success";
	private String CURRENCY;
	private String CURRENCY_CODE;
	IClientGroup clientGroupService;

	public JSONClientGroupMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.ACTION = "action";
		this.CURRENCY = "currency";
		this.CURRENCY_CODE = "currencyCode";
		this.clientGroupService = null;
	}

	public void setClientGroupService(IClientGroup clientGroupService) {
		this.clientGroupService = clientGroupService;
	}

	public ModelAndView viewGroup(HttpServletRequest request, HttpServletResponse response,
			ClientGroupMasterVO clientGroupMasterVO) throws Exception {
		this.logger.debug("In viewGroup method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		ArrayList historyList = null;

		try {
			new ArrayList();
			historyList = (ArrayList) this.clientGroupService.searchClientGroup(clientGroupMasterVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			int count = this.clientGroupService.searchCGCount(clientGroupMasterVO);
			modelAndView.addObject("total", count);
			modelAndView.addObject("historyList", historyList);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView addGroup(HttpServletRequest request, HttpServletResponse response,
			ClientGroupMasterVO clientGroupMasterVO) throws Exception {
		this.logger.debug("In addGroup method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			clientGroupMasterVO.setClientGroupName(request.getParameter(this.CURRENCY));
			clientGroupMasterVO.setUpdateBy(userBean.getUserName());
			Object message = this.clientGroupService.addGroup(clientGroupMasterVO);

			try {
				Integer.parseInt(message.toString());
				modelAndView.addObject("success", true);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception" + var8);
				modelAndView.addObject("success", message.toString());
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("CLIENT_GROUP_MASTER");
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		this.logger.info(
				"clientGroupMaster Name \"" + clientGroupMasterVO.getClientGroupName() + "\" is successfully added");
		return modelAndView;
	}

	public ModelAndView searchExistGroup(HttpServletRequest request, HttpServletResponse response,
			ClientGroupMasterVO clientGroupMasterVO) throws Exception {
		this.logger.debug("In searchExistGroup method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		boolean var5 = false;

		try {
			int count = this.clientGroupService.isExist(request.getParameter(this.ACTION));
			if (count > 0) {
				modelAndView.addObject("success", true);
			} else {
				modelAndView.addObject("success", false);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView searchGroupByName(HttpServletRequest request, HttpServletResponse response,
			ClientGroupMasterVO clientGroupMasterVO) throws Exception {
		this.logger.debug("In searchGroupByName method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			clientGroupMasterVO.setClientGroupName(request.getParameter(this.ACTION));
			ClientGroupMasterVO searchbyCodeList = this.clientGroupService.searchGroupByName(clientGroupMasterVO);
			modelAndView.addObject("success", true);
			modelAndView.addObject(this.CURRENCY_CODE, searchbyCodeList.getClientGroupId());
			modelAndView.addObject(this.CURRENCY, searchbyCodeList.getClientGroupName());
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView updateGroup(HttpServletRequest request, HttpServletResponse response,
			ClientGroupMasterVO clientGroupMasterVO) throws Exception {
		this.logger.debug("In updateGroup method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			clientGroupMasterVO.setStatusval("1");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			clientGroupMasterVO.setClientGroupName(request.getParameter(this.CURRENCY));
			clientGroupMasterVO.setClientGroupId(request.getParameter(this.CURRENCY_CODE));
			clientGroupMasterVO.setUpdateBy(userBean.getUserName());
			Object message = this.clientGroupService.updateGroup(clientGroupMasterVO);

			try {
				Integer.parseInt(message.toString());
				modelAndView.addObject("success", true);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception " + var8);
				modelAndView.addObject("success", message.toString());
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("CLIENT_GROUP_MASTER");
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		this.logger.info(
				"clientGroupMaster Name \"" + clientGroupMasterVO.getClientGroupName() + "\" is successfully updated");
		return modelAndView;
	}

	public ModelAndView deactivateGroup(HttpServletRequest request, HttpServletResponse response,
			ClientGroupMasterVO clientGroupMasterVO) throws Exception {
		this.logger.debug("In deactivateGroup method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String currencyCodes = request.getParameter(this.ACTION);
			Object message = this.clientGroupService.deactivateGroup(currencyCodes, request, clientGroupMasterVO);

			try {
				Integer.parseInt(message.toString());
				modelAndView.addObject("success", true);
			} catch (NumberFormatException var8) {
				this.logger.debug("number format exception" + var8);
				modelAndView.addObject("success", message.toString());
			}

			this.logger.info("client group deactivated and client deassociated with code" + currencyCodes
					+ "is successfully done");
			ResourceLocator.self().getCacheService().addToCacheRunTime("CLIENT_GROUP_MASTER");
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}