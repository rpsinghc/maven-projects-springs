package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.vendordetail.VendorDetailManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.VendorDetailVO;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONVendorDetailMultiActionController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONVendorDetailMultiActionController");
	private VendorDetailManager vendorDetailManager;

	public void setVendorDetailManager(VendorDetailManager vendorDetailManager) {
		this.vendorDetailManager = vendorDetailManager;
	}

	public ModelAndView searchVendorDetailList(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			this.logger.debug("Inside the JSONVendorDetailMultiActionController <<searchVendorDetailList()>>"
					+ vendorDetailVO.getVendorId() + "CRN:" + vendorDetailVO.getCrn());
			vendorDetailVO = this.vendorDetailManager.searchVendorInformation(vendorDetailVO.getVendorId(),
					vendorDetailVO.getCrn());
			if (null == vendorDetailVO) {
				mv.addObject("result", "no result found");
			} else {
				mv.addObject("vendorDetailVO", vendorDetailVO);
				mv.addObject("result", "success");
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView saveVendorDetail(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			String action = request.getParameter("vendorAction");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			vendorDetailVO.setUpdatedBy(userBean.getUserName());
			if (null != vendorDetailVO.getJobReqSentDate() && vendorDetailVO.getJobReqSentDate().trim().length() != 0) {
				vendorDetailVO.setJobReqSentDate(vendorDetailVO.getJobReqSentDate().substring(0,
						vendorDetailVO.getJobReqSentDate().indexOf(84)));
			}

			if (null != vendorDetailVO.getCommissionDate() && vendorDetailVO.getCommissionDate().trim().length() != 0) {
				vendorDetailVO.setCommissionDate(vendorDetailVO.getCommissionDate().substring(0,
						vendorDetailVO.getCommissionDate().indexOf(84)));
			}

			if (null != vendorDetailVO.getVendorDueDate() && vendorDetailVO.getVendorDueDate().trim().length() != 0) {
				vendorDetailVO.setVendorDueDate(
						vendorDetailVO.getVendorDueDate().substring(0, vendorDetailVO.getVendorDueDate().indexOf(84)));
			}

			if (null != vendorDetailVO.getVendorSubDate() && vendorDetailVO.getVendorSubDate().trim().length() != 0) {
				vendorDetailVO.setVendorSubDate(
						vendorDetailVO.getVendorSubDate().substring(0, vendorDetailVO.getVendorSubDate().indexOf(84)));
			}

			this.logger.debug(
					"com.worldcheck.atlas.frontend.JSONVendorDetailMultiActionController    getVendorNameList<br>CRN:"
							+ vendorDetailVO.getCrn() + "<br>VendorId:" + vendorDetailVO.getVendorId()
							+ "<br>Vendor Name:" + vendorDetailVO.getVendorName() + "<br>Vendor Type:"
							+ vendorDetailVO.getVendorType() + "<br>Vendor Type Display:"
							+ vendorDetailVO.getVendorTypeDisplay() + "<br>status:" + vendorDetailVO.getVendorStatus()
							+ "<br>status Display:" + vendorDetailVO.getVendorStatusDisplay() + "<br>jobReqSentDate:"
							+ vendorDetailVO.getJobReqSentDate() + "<br>commissioningDate:"
							+ vendorDetailVO.getCommissionDate() + "<br>vendorDueDate:"
							+ vendorDetailVO.getVendorDueDate() + "<br>vendorSubmissionDate:"
							+ vendorDetailVO.getVendorSubDate() + "<br>immediateAttention:"
							+ vendorDetailVO.getImmediateAttention() + "<br>vendorManager:"
							+ vendorDetailVO.getVendorManager() + "<br>Country:" + vendorDetailVO.getCountryId()
							+ "<br>Country Display:" + vendorDetailVO.getCountryDispaly() + "<br>currencyCmb:"
							+ vendorDetailVO.getCurrency() + "<br>currencyCmb Display:"
							+ vendorDetailVO.getCurrencyDisplay() + "<br>amount:" + vendorDetailVO.getCost()
							+ "<br>name Searched:" + vendorDetailVO.getNameSearch() + "<br>invoiceno:"
							+ vendorDetailVO.getInvoiceNumber() + "<br>UpdatedBy:" + vendorDetailVO.getUpdatedBy()
							+ "<br>CreatedBy:" + vendorDetailVO.getCreatedBy());
			if (null != vendorDetailVO.getWorkItem()) {
				long pid = Long.parseLong(vendorDetailVO.getWorkItem().split("#")[1].split("::")[0]);
				this.logger.debug("WorkItem  PID:" + pid);
				long piid = (Long) ResourceLocator.self().getSBMService().getDataslotValue(pid, "parentPID",
						SBMUtils.getSession(request));
				this.logger.debug("Process Parent PID:" + piid);
				if (0L == piid) {
					piid = pid;
				}

				String processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(piid,
						"ProcessCycle", SBMUtils.getSession(request));
				vendorDetailVO.setProcessCycle(processCycle);
			}

			int vendorInvoiceId;
			if (action.equalsIgnoreCase("save")) {
				vendorDetailVO.setCreatedBy(userBean.getUserName());
				vendorInvoiceId = this.vendorDetailManager.saveVendorDetail(vendorDetailVO);
				this.saveVendorCaseHistory(vendorDetailVO, request, response);
			} else {
				this.logger.debug("Vendor ID:" + vendorDetailVO.getReqForVendorInvoiceId());
				VendorDetailVO oldvendorDetailVO = this.vendorDetailManager
						.getVendorInformation(vendorDetailVO.getReqForVendorInvoiceId());
				this.logger.debug("Old Information ::<br>CRN:" + oldvendorDetailVO.getCrn() + "<br>VendorId:"
						+ oldvendorDetailVO.getVendorId() + "<br>Vendor Name:" + oldvendorDetailVO.getVendorName()
						+ "<br>Vendor Type:" + oldvendorDetailVO.getVendorType() + "<br>Vendor Type Display:"
						+ oldvendorDetailVO.getVendorTypeDisplay() + "<br>status:" + oldvendorDetailVO.getVendorStatus()
						+ "<br>status Display:" + oldvendorDetailVO.getVendorStatusDisplay() + "<br>jobReqSentDate:"
						+ oldvendorDetailVO.getJobReqSentDate() + "<br>commissioningDate:"
						+ oldvendorDetailVO.getCommissionDate() + "<br>vendorDueDate:"
						+ oldvendorDetailVO.getVendorDueDate() + "<br>vendorSubmissionDate:"
						+ oldvendorDetailVO.getVendorSubDate() + "<br>immediateAttention:"
						+ oldvendorDetailVO.getImmediateAttention() + "<br>vendorManager:"
						+ oldvendorDetailVO.getVendorManager() + "<br>Country:" + oldvendorDetailVO.getCountryId()
						+ "<br>Country Display:" + oldvendorDetailVO.getCountryDispaly() + "<br>currencyCmb:"
						+ oldvendorDetailVO.getCurrency() + "<br>currencyCmb Display:"
						+ oldvendorDetailVO.getCurrencyDisplay() + "<br>amount:" + oldvendorDetailVO.getCost()
						+ "<br>vendor Manager to Message:" + oldvendorDetailVO.getVendorMgrMesToVendor()
						+ "<br>name Searched:" + oldvendorDetailVO.getNameSearch() + "<br>Comments:"
						+ oldvendorDetailVO.getVendorComments() + "<br>invoiceno:"
						+ oldvendorDetailVO.getInvoiceNumber() + "<br>UpdatedBy:" + oldvendorDetailVO.getUpdatedBy()
						+ "<br>CreatedBy:" + oldvendorDetailVO.getCreatedBy());
				vendorInvoiceId = this.vendorDetailManager.updateVendorDetail(vendorDetailVO);
				this.logger.debug("New Information ::<br>CRN:" + vendorDetailVO.getCrn() + "<br>VendorId:"
						+ vendorDetailVO.getVendorId() + "<br>Vendor Name:" + vendorDetailVO.getVendorName()
						+ "<br>Vendor Type:" + vendorDetailVO.getVendorType() + "<br>Vendor Type Display:"
						+ vendorDetailVO.getVendorTypeDisplay() + "<br>status:" + vendorDetailVO.getVendorStatus()
						+ "<br>status Display:" + vendorDetailVO.getVendorStatusDisplay() + "<br>jobReqSentDate:"
						+ vendorDetailVO.getJobReqSentDate() + "<br>commissioningDate:"
						+ vendorDetailVO.getCommissionDate() + "<br>vendorDueDate:" + vendorDetailVO.getVendorDueDate()
						+ "<br>vendorSubmissionDate:" + vendorDetailVO.getVendorSubDate() + "<br>immediateAttention:"
						+ vendorDetailVO.getImmediateAttention() + "<br>vendorManager:"
						+ vendorDetailVO.getVendorManager() + "<br>Country:" + vendorDetailVO.getCountryId()
						+ "<br>Country Display:" + vendorDetailVO.getCountryDispaly() + "<br>currencyCmb:"
						+ vendorDetailVO.getCurrency() + "<br>currencyCmb Display:"
						+ vendorDetailVO.getCurrencyDisplay() + "<br>amount:" + vendorDetailVO.getCost()
						+ "<br>vendor Manager to Message:" + vendorDetailVO.getVendorMgrMesToVendor()
						+ "<br>name Searched:" + vendorDetailVO.getNameSearch() + "<br>Comments:"
						+ vendorDetailVO.getVendorComments() + "<br>invoiceno:" + vendorDetailVO.getInvoiceNumber()
						+ "<br>UpdatedBy:" + vendorDetailVO.getUpdatedBy() + "<br>CreatedBy:"
						+ vendorDetailVO.getCreatedBy());
				if (vendorDetailVO.getJobReqSentDate() != null && !vendorDetailVO.getJobReqSentDate().equals("")) {
					vendorDetailVO.setJobReqSentDate(this.convertDate(vendorDetailVO.getJobReqSentDate()));
				}

				if (vendorDetailVO.getCommissionDate() != null && !vendorDetailVO.getCommissionDate().equals("")) {
					vendorDetailVO.setCommissionDate(this.convertDate(vendorDetailVO.getCommissionDate()));
				}

				if (vendorDetailVO.getVendorDueDate() != null && !vendorDetailVO.getVendorDueDate().equals("")) {
					vendorDetailVO.setVendorDueDate(this.convertDate(vendorDetailVO.getVendorDueDate()));
				}

				if (vendorDetailVO.getVendorSubDate() != null && !vendorDetailVO.getVendorSubDate().equals("")) {
					vendorDetailVO.setVendorSubDate(this.convertDate(vendorDetailVO.getVendorSubDate()));
				}

				this.updateVendorCaseHistory(oldvendorDetailVO, vendorDetailVO, request, response);
			}

			mv.addObject("result", "success");
			mv.addObject("vendorInvoiceId", vendorInvoiceId);
			mv.addObject("vendorId", vendorDetailVO.getVendorId());
			mv.addObject("vendorType", vendorDetailVO.getVendorType());
			mv.addObject("vendorManager", vendorDetailVO.getVendorManager());
			mv.addObject("vendorStatus", vendorDetailVO.getVendorStatus());
			return mv;
		} catch (CMSException var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		} catch (Exception var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		}
	}

	public ModelAndView saveVendorCaseHistory(VendorDetailVO vendorDetailVO, HttpServletRequest request,
			HttpServletResponse response) {
		CaseDetails newCaseDetails = null;
		CaseDetails oldCaseDetails = null;
		CaseHistory caseHistory = null;

		try {
			this.logger.debug(" JSONVendorDetailMultiActionController > saveVendorCaseHistory");
			newCaseDetails = new CaseDetails();
			List<VendorDetailVO> vendorList = new ArrayList();
			vendorList.add(vendorDetailVO);
			newCaseDetails.setVendorList(vendorList);
			caseHistory = new CaseHistory();
			this.logger.debug("WorkItem:" + vendorDetailVO.getWorkItem());
			caseHistory.setCRN(vendorDetailVO.getCrn());
			if (null != vendorDetailVO.getWorkItem()) {
				caseHistory.setProcessCycle(vendorDetailVO.getProcessCycle());
				caseHistory.setPid(vendorDetailVO.getWorkItem().split("#")[1].split("::")[0]);
				caseHistory.setTaskName(vendorDetailVO.getWorkItem().split("#")[1].split("::")[1]);
			} else {
				caseHistory.setPid(
						ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(vendorDetailVO.getCrn()) + "");
			}

			this.logger.debug("request.getSession().getAttribute(loginLevel) is "
					+ request.getSession().getAttribute("loginLevel"));
			if (null != request.getSession().getAttribute("loginLevel")) {
				caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseHistory.setPerformer(vendorDetailVO.getUpdatedBy());
			}

			this.logger.debug("oldCaseDetails::>>" + oldCaseDetails + " newCaseDetails::>>" + newCaseDetails
					+ " caseHistory::>>" + caseHistory + "VendorDetails");
			ResourceLocator.self().getCaseHistoryService().setCaseHistory((CaseDetails) oldCaseDetails, newCaseDetails,
					caseHistory, "VendorDetails");
			return null;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView updateVendorCaseHistory(VendorDetailVO oldvendorDetailVO, VendorDetailVO vendorDetailVO,
			HttpServletRequest request, HttpServletResponse response) {
		CaseDetails newCaseDetails = null;
		CaseDetails oldCaseDetails = null;
		CaseHistory caseHistory = null;

		try {
			this.logger.debug(" JSONVendorDetailMultiActionController > updateVendorCaseHistory");
			newCaseDetails = new CaseDetails();
			oldCaseDetails = new CaseDetails();
			List<VendorDetailVO> vendorList = new ArrayList();
			vendorList.add(vendorDetailVO);
			newCaseDetails.setVendorList(vendorList);
			caseHistory = new CaseHistory();
			this.logger.debug("WorkItem:" + vendorDetailVO.getWorkItem());
			caseHistory.setCRN(vendorDetailVO.getCrn());
			if (null != vendorDetailVO.getWorkItem()) {
				caseHistory.setProcessCycle(vendorDetailVO.getProcessCycle());
				caseHistory.setPid(vendorDetailVO.getWorkItem().split("#")[1].split("::")[0]);
				caseHistory.setTaskName(vendorDetailVO.getWorkItem().split("#")[1].split("::")[1]);
			} else {
				caseHistory.setPid(
						ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(vendorDetailVO.getCrn()) + "");
			}

			this.logger.debug("request.getSession().getAttribute(loginLevel) is "
					+ request.getSession().getAttribute("loginLevel"));
			if (null != request.getSession().getAttribute("loginLevel")) {
				caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseHistory.setPerformer(vendorDetailVO.getUpdatedBy());
			}

			vendorList = new ArrayList();
			vendorList.add(oldvendorDetailVO);
			oldCaseDetails.setVendorList(vendorList);
			this.logger.debug("oldCaseDetails::>>" + oldCaseDetails + " newCaseDetails::>>" + newCaseDetails
					+ " caseHistory::>>" + caseHistory + "VendorDetails");
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, newCaseDetails, caseHistory,
					"VendorDetails");
			return null;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getVendorNameList(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;
		List vendorNameList = null;

		try {
			mv = new ModelAndView("jsonView");
			vendorNameList = this.vendorDetailManager.getVendorNameList(vendorDetailVO);
			mv.addObject("vendorNameList", vendorNameList);
			return mv;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getVendorDetailList(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;
		List vendorGridList = null;

		try {
			mv = new ModelAndView("jsonView");
			String sortColumn = request.getParameter("sort");
			String sortType = request.getParameter("dir");
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			this.logger.debug("Inside the JSONVendorDetailMultiActionController <<getVendorDetailList()>>CRN:"
					+ vendorDetailVO.getCrn() + "visiableToAnalyst :" + vendorDetailVO.getVisiableToAnalyst()
					+ "sortColumn Name :" + sortColumn + "SortType  :" + sortType + "start  :" + start + "limit  :"
					+ limit);
			vendorGridList = this.vendorDetailManager.getVendorDetailList(vendorDetailVO.getCrn(),
					vendorDetailVO.getVisiableToAnalyst(), start, limit, sortColumn, sortType);
			this.logger.info("Row Return:" + vendorGridList.size());
			this.logger.debug("Count total no of record:CRN:" + vendorDetailVO.getCrn() + "sortColumn Name :"
					+ sortColumn + "SortType  :" + sortType + "start  :" + start + "limit  :" + limit);
			int count = this.vendorDetailManager.getVendorDetailSize(vendorDetailVO.getCrn(),
					vendorDetailVO.getVisiableToAnalyst());
			mv.addObject("total", count);
			mv.addObject("vendorGridList", vendorGridList);
			return mv;
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}

	public ModelAndView getVendorInformation(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			vendorDetailVO = this.vendorDetailManager.getVendorInformation(vendorDetailVO.getReqForVendorInvoiceId());
			this.logger.debug("vendorDetailVO:" + vendorDetailVO);
			mv.addObject("result", "success");
			mv.addObject("vendorDetailVO", vendorDetailVO);
			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView deAssociatedVendorDetail(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			this.logger
					.debug("CRN::" + vendorDetailVO.getCrn() + " :: inside the JsonVendorDetaiMultiActionController");
			this.logger.debug("VendorId::" + vendorDetailVO.getReqForVendorInvoiceId() + " workItem::"
					+ vendorDetailVO.getWorkItem() + ":: CRN::" + vendorDetailVO.getCrn());
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			VendorDetailVO oldVendorDetailVO = this.vendorDetailManager
					.getVendorInformation(vendorDetailVO.getReqForVendorInvoiceId());
			int count = this.vendorDetailManager.deAssociatedVendorDetail(vendorDetailVO.getReqForVendorInvoiceId());
			if (null != vendorDetailVO.getWorkItem()) {
				oldVendorDetailVO.setWorkItem(vendorDetailVO.getWorkItem());
			}

			oldVendorDetailVO.setUpdatedBy(userBean.getUserName());
			this.deleteVendorCaseHistory(oldVendorDetailVO, request, response);
			this.logger.debug("vendorDetailVO:" + vendorDetailVO);
			mv.addObject("result", "success");
			mv.addObject("vendorDetailVO", vendorDetailVO);
			return mv;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView deleteVendorCaseHistory(VendorDetailVO vendorDetailVO, HttpServletRequest request,
			HttpServletResponse response) {
		CaseDetails newCaseDetails = null;
		CaseDetails oldCaseDetails = null;
		CaseHistory caseHistory = null;
		ArrayList vendorList = null;

		try {
			this.logger.debug("Inside JSONVendorDetailMultiActionController >>> deleteVenDorCaseHistory ...");
			oldCaseDetails = new CaseDetails();
			vendorList = new ArrayList();
			vendorList.add(vendorDetailVO);
			oldCaseDetails.setVendorList(vendorList);
			caseHistory = new CaseHistory();
			this.logger.debug("WorkItem:" + vendorDetailVO.getWorkItem());
			caseHistory.setCRN(vendorDetailVO.getCrn());
			if (null != vendorDetailVO.getWorkItem()) {
				caseHistory.setProcessCycle(vendorDetailVO.getProcessCycle());
				caseHistory.setPid(vendorDetailVO.getWorkItem().split("#")[1].split("::")[0]);
				caseHistory.setTaskName(vendorDetailVO.getWorkItem().split("#")[1].split("::")[1]);
			} else {
				caseHistory.setPid(
						ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(vendorDetailVO.getCrn()) + "");
			}

			this.logger.debug("Updated By::" + vendorDetailVO.getUpdatedBy());
			this.logger.debug("request.getSession().getAttribute(loginLevel) is "
					+ request.getSession().getAttribute("loginLevel"));
			if (null != request.getSession().getAttribute("loginLevel")) {
				caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseHistory.setPerformer(vendorDetailVO.getUpdatedBy());
			}

			this.logger.debug("oldCaseDetails::>>" + oldCaseDetails + " newCaseDetails::>>" + newCaseDetails
					+ " caseHistory::>>" + caseHistory + "VendorDetails");
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(oldCaseDetails, (CaseDetails) newCaseDetails,
					caseHistory, "VendorDetails");
			return null;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView sendEmailTemplate(HttpServletRequest request, HttpServletResponse response,
			VendorDetailVO vendorDetailVO) {
		ModelAndView mv = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("inside sendEmailTemplate");
			mv = new ModelAndView("jsonView");
			this.logger.debug("VendorId:" + vendorDetailVO.getVendorId() + "VendorInvoicceID::"
					+ vendorDetailVO.getReqForVendorInvoiceId());
			this.logger.debug("VendorId:" + vendorDetailVO.getVendorInvoiceId());
			VendorDetailVO vendorEmailInformation = this.vendorDetailManager.sendEmailTemplate(
					vendorDetailVO.getVendorInvoiceId() + "", vendorDetailVO.getCrn(), userBean.getUserName());
			this.logger.debug("vendorDetailVO:" + vendorDetailVO);
			mv.addObject("result", "success");
			mv.addObject("vendorEmailInformation", vendorEmailInformation);
			return mv;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public String convertDate(String dateString) {
		SimpleDateFormat sourcedateformat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date date = sourcedateformat.parse(dateString);
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd-MM-yyyy");
			this.logger.debug("after parse date::" + sdfDestination.format(date));
			return sdfDestination.format(date);
		} catch (ParseException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5) + "";
		}
	}
}