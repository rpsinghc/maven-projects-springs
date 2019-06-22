package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IBranchOfficeMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONBranchOfficeMultiactionController extends JSONMultiActionController {
	IBranchOfficeMaster branchOfficeMultiActionManager = null;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.BranchOfficeMultiActionController");
	private String ACTION = "action";
	private static final String Ostatus = "ostatus";
	private static final String success = "success";
	private static final String BLANK = "";
	private static final String Zero = "0";
	private static final String OFFICE_MASTER = "OFFICE_MASTER";
	private String searchName = "searchResult";

	public void setBranchOfficeMultiActionManager(IBranchOfficeMaster branchOfficeMultiActionManager) {
		this.branchOfficeMultiActionManager = branchOfficeMultiActionManager;
	}

	public ModelAndView deActivatebranchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) throws Exception {
		this.logger.debug("In deActivatebranchOffice method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String branchOfficeCode = request.getParameter(this.ACTION);
			String ostatus = request.getParameter("ostatus");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String updatedBy = userBean.getUserName();
			this.logger.debug(branchOfficeCode);
			this.logger.debug(ostatus);
			this.branchOfficeMultiActionManager.deActivatebranchOffice(branchOfficeCode, ostatus, updatedBy);
			this.logger.info("Branch office code \" " + branchOfficeCode + " \" status is successfully changed");
			modelAndView.addObject("success", true);
			ResourceLocator.self().getCacheService().addToCacheRunTime("OFFICE_MASTER");
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView checkAssociatedtoDeactivateOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) throws Exception {
		this.logger.debug("In checkAssociatedWIPCRNtoDeactivateOffice");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String officeId = request.getParameter(this.ACTION);
			String ostatus = request.getParameter("ostatus");
			if (ostatus.equalsIgnoreCase("0")) {
				BranchOfficeMasterVO bomvo = this.branchOfficeMultiActionManager.checkAssociatedtoOffice(officeId);
				if (!bomvo.getCcCount().equalsIgnoreCase("0") || !bomvo.getSsmCount().equalsIgnoreCase("0")
						|| !bomvo.getCmCount().equalsIgnoreCase("0") || !bomvo.getRccCount().equalsIgnoreCase("0")
						|| !bomvo.getTdCount().equalsIgnoreCase("0") || !bomvo.getHmCount().equalsIgnoreCase("0")
						|| !bomvo.getUmCount().equalsIgnoreCase("0")) {
					modelAndView.addObject("isAssociated", true);
					modelAndView.addObject("vo", bomvo);
				}
			}

			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView searchBranchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) throws Exception {
		this.logger.debug("In searchBranchOffice method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		ArrayList searchResult = null;

		try {
			if (branchOfficeMasterVO.getBranchOffice().equalsIgnoreCase("")
					&& branchOfficeMasterVO.getBranchOfficeStatus().equalsIgnoreCase("")) {
				searchResult = new ArrayList();
			} else {
				new ArrayList();
				searchResult = (ArrayList) this.branchOfficeMultiActionManager.searchBO(branchOfficeMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				int count = this.branchOfficeMultiActionManager.searchBoCount(branchOfficeMasterVO);
				modelAndView.addObject("total", count);
			}

			modelAndView.addObject(this.searchName, searchResult);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView searchExistBranchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) throws Exception {
		this.logger.debug("In searchExistBranchOffice method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		boolean var5 = false;

		try {
			int count = this.branchOfficeMultiActionManager.isExist(request.getParameter(this.ACTION));
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
}