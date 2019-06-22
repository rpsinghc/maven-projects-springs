package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.IBranchOfficeMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class BranchOfficeMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.BranchOfficeMultiActionController");
	private ModelAndView mv = null;
	private String bOfficeList = "bOfficeList";
	private String searchName = "searchResult";
	private String action = "action";
	private static final String Ostatus = "ostatus";
	private static final String success = "success";
	private static final String branch_office = "branch_office";
	private static final String redirect_branch_office = "redirect:branchOffice.do";
	private static final String avilable = "avilable";
	private static final String add_branch_office = "add_branch_office";
	private static final Object update = "update";
	private static final String getRE = "getRE";
	private static final String deactUpdate = "deactUpdate";
	private static final String Zero = "Zero";
	private static final String bIdvale = "bIdvale";
	private static final String office = "office";
	private static final String Status = "status";
	private static final String boId = "boId";
	private static final String BLANK = "";
	private static final String error = "error";
	private final String fromSave = "fromSave";
	private final String fromUpdate = "fromUpdate";
	private static final String OFFICE_MASTER = "OFFICE_MASTER";
	private String branchOfficeCode = "Branch Office Code";
	private String branchOffice = "Branch Office";
	private String branchOfficeUserId = "Branch Office UserId";
	private String status = "Branch Office Status";
	private String BRANCH_Office_MASTER = "Branch Office Master";
	private static final String REDIRECT_BRANCHOFFICE = "redirect:branchOffice.do";
	IBranchOfficeMaster branchOfficeMultiActionManager = null;

	public void setBranchOfficeMultiActionManager(IBranchOfficeMaster branchOfficeMultiActionManager) {
		this.branchOfficeMultiActionManager = branchOfficeMultiActionManager;
	}

	public ModelAndView branchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) {
		this.logger.debug("In branchOffice method");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("branch_office");
			HttpSession var10000 = request.getSession();
			this.getClass();
			String fromUpdate;
			if (var10000.getAttribute("fromSave") != null) {
				var10000 = request.getSession();
				this.getClass();
				fromUpdate = var10000.getAttribute("fromSave").toString();
				this.getClass();
				modelAndView.addObject("fromSave", fromUpdate);
				var10000 = request.getSession();
				this.getClass();
				var10000.removeAttribute("fromSave");
			}

			var10000 = request.getSession();
			this.getClass();
			if (var10000.getAttribute("fromUpdate") != null) {
				var10000 = request.getSession();
				this.getClass();
				fromUpdate = var10000.getAttribute("fromUpdate").toString();
				this.getClass();
				modelAndView.addObject("fromUpdate", fromUpdate);
				var10000 = request.getSession();
				this.getClass();
				var10000.removeAttribute("fromUpdate");
			}

			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView branchOfficeReportExport(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in branchOfficeReportExport");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			this.logger.debug("excelParams::" + excelParams);
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			List<BranchOfficeMasterVO> branchDataList = this.branchOfficeMultiActionManager.getReport(excelParamMap);
			this.logger.debug("fetched reDataList>>Size is " + branchDataList.size());
			Map<String, Object> resultMap = this.writeToExcel(branchDataList, response);
			modelandview = new ModelAndView("excelDownloadPopup");
			modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
			modelandview.addObject("fileName", resultMap.get("fileName"));
			this.logger.debug("exiting  Export to Excel BranchOfficeController");
			return modelandview;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	private Map<String, Object> writeToExcel(List<BranchOfficeMasterVO> branchDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.branchOfficeCode);
			lstHeader.add(this.branchOffice);
			lstHeader.add(this.branchOfficeUserId);
			lstHeader.add(this.status);
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = branchDataList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				BranchOfficeMasterVO branchOfficeMasterVO = (BranchOfficeMasterVO) iterator.next();
				datamap.put(this.branchOfficeCode, String.valueOf(branchOfficeMasterVO.getBranchOfficeId()));
				datamap.put(this.branchOffice, String.valueOf(branchOfficeMasterVO.getBranchOffice()));
				datamap.put(this.branchOfficeUserId, String.valueOf(branchOfficeMasterVO.getBranchOfficeUsername()));
				datamap.put(this.status, String.valueOf(branchOfficeMasterVO.getBranchOfficeStatus()));
				dataList.add(datamap);
			}

			return ExcelDownloader.writeToExcel(lstHeader, dataList, this.BRANCH_Office_MASTER, (short) 0, (short) 1,
					response, this.BRANCH_Office_MASTER);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public ModelAndView newBranchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) {
		this.logger.debug("In newBranchOffice method");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("add_branch_office");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView addBranchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) {
		this.logger.debug("In addBranchOffice method");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			branchOfficeMasterVO.setUpdatedBy(userName);
			this.branchOfficeMultiActionManager.addBranchOffice(branchOfficeMasterVO);
			this.logger
					.info("Branch office \" " + branchOfficeMasterVO.getBranchOffice() + " \" is successfully added");
			this.mv = new ModelAndView("redirect:branchOffice.do");
			HttpSession var10000 = request.getSession();
			this.getClass();
			var10000.setAttribute("fromSave", true);
			ResourceLocator.self().getCacheService().addToCacheRunTime("OFFICE_MASTER");
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}

		return this.mv;
	}

	public ModelAndView getBOInfo(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) {
		this.logger.debug("In getBOInfo method");

		try {
			this.mv = new ModelAndView("add_branch_office");
			if (request.getParameter("bIdvale") == null) {
				return new ModelAndView("redirect:branchOffice.do");
			}

			int branchId = Integer.parseInt(request.getParameter("bIdvale"));
			BranchOfficeMasterVO vo = this.branchOfficeMultiActionManager.getBOInfo(branchId);
			if (vo == null) {
				return new ModelAndView("redirect:branchOffice.do");
			}

			if (vo.getBranchOfficeStatus().equalsIgnoreCase("Zero")) {
				this.mv.addObject("deactUpdate", true);
			}

			this.mv.addObject("getRE", vo);
			this.mv.addObject(this.action, update);
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}

		return this.mv;
	}

	public ModelAndView updateBranchOffice(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) {
		this.logger.debug("In updateBranchOffice method");
		ModelAndView modelAndView = new ModelAndView("redirect:branchOffice.do");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			branchOfficeMasterVO.setUpdatedBy(userName);
			String office_name = request.getParameter("office");
			int status = Integer.parseInt(request.getParameter("status"));
			int oCode = Integer.parseInt(request.getParameter("boId"));
			branchOfficeMasterVO.setBranchOffice(office_name);
			branchOfficeMasterVO.setStatusval(status);
			branchOfficeMasterVO.setBranchOfficeId(oCode);
			this.branchOfficeMultiActionManager.updateBo(branchOfficeMasterVO);
			this.logger
					.info("Branch office \" " + branchOfficeMasterVO.getBranchOffice() + " \" is successfully updated");
			HttpSession var10000 = request.getSession();
			this.getClass();
			var10000.setAttribute("fromUpdate", true);
			ResourceLocator.self().getCacheService().addToCacheRunTime("OFFICE_MASTER");
			return modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}
}