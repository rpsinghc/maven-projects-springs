package com.worldcheck.atlas.frontend.invoice;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.task.invoice.AccountsLinkcasesVO;
import com.worldcheck.atlas.vo.task.invoice.CRNInfoVO;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JSONInvoiceManagementController extends MultiActionController {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.invoice.JSONInvoiceManagementController");
	private ResourceLocator locator = null;
	private final String JSON_VIEW = "jsonView";
	private static final String REQ_CRN = "crn";
	private static final String UNLINK_CRN_IDS = "unlink_crn_ids";
	private static final String INVOICE_DETAILS = "invoiceDetails";
	private static final String CAPETOWN_INFO = "capetownInfo";
	private static final String PREPAYMENT_SUMMARY_CRN = "prepaymentSummaryCRN";
	private static final String ACCOUNT_DETAILS = "accountDetails";
	private static final String RELATED_CRN = "relatedCrn";
	private static final String ACCOUNT_LINK_CASE = "listAccountLinkcase";
	private String crn;
	private static final String REGISTER_NO = "registerNo";
	private static final String UNLINKED_CRN = "unLinkedCRN";
	private static final String UNLINKED_CRN_PARENT = "unLinkedCRNParent";
	private static final String PREPAYMENT_SUMMARY_ROOT = "prepaymentSummaryRoot";
	private static final String LINK_CRN = "linkCRN";
	private static final String UPDATE_BY = "updateBy";
	private static final String IS_ORR_REPORT_TYPE = "isORRType";
	private static final String IS_CHILD_CRN = "isChildCrn";
	private static final String UNLINKED_CRNS = "unlink_crns";

	public ModelAndView getInvoiceDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("inside getInvoiceDetails name for retriever invoice dtails");
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			int reportCount = this.locator.getInvoiceService().getReportTypeCount(this.crn);
			viewForJson.addObject("isORRType", reportCount > 0 ? "Y" : "N");
			if (null != httpServletRequest.getParameter("crn")) {
				this.crn = httpServletRequest.getParameter("crn");
			}

			this.logger.debug("value of crn is :: " + this.crn);
			viewForJson.addObject("invoiceDetails", this.locator.getInvoiceService().getInvoice(this.crn));
			int count = this.locator.getInvoiceService().isChildCRN(this.crn);
			viewForJson.addObject("isChildCrn", count > 0 ? "Y" : "N");
			viewForJson.addObject("prepaymentSummaryCRN",
					this.locator.getInvoiceService().getPrepaymentSummaryCRN(this.crn));
			viewForJson.addObject("capetownInfo", this.locator.getInvoiceService().getCapetownInfo(this.crn));
			viewForJson.addObject("accountDetails", this.locator.getInvoiceService().getAccountDetails(this.crn));
			viewForJson.addObject("relatedCrn", this.locator.getInvoiceService().getRelatedCRN(this.crn));
			viewForJson.addObject("reportTypeCount", this.locator.getInvoiceService().getTotalCountlinkedCRN(this.crn));
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, httpServletResponse);
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, httpServletResponse);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, httpServletResponse);
		}

		this.logger.debug("After getting values exit from method");
		return viewForJson;
	}

	public ModelAndView getAccountLinkcase(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.logger.debug("inside getAccountLinkcase name for retriever invoice dtails");
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			if (null != httpServletRequest.getParameter("crn")) {
				this.crn = httpServletRequest.getParameter("crn");
			}

			this.logger.debug("value of crn is :: " + this.crn);
			HashMap<String, Object> paramMap = new HashMap();
			int start = Integer.parseInt(httpServletRequest.getParameter("start"));
			int limit = Integer.parseInt(httpServletRequest.getParameter("limit"));
			limit += start;
			paramMap.put("start", start + 1);
			paramMap.put("limit", limit);
			paramMap.put("crn", this.crn);
			viewForJSON.addObject("listAccountLinkcase", this.locator.getInvoiceService().getAccountLinkcase(paramMap));
			viewForJSON.addObject("total", this.locator.getInvoiceService().getTotalCountlinkedCRN(this.crn));
		} catch (NullPointerException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, httpServletResponse);
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, httpServletResponse);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpServletResponse);
		}

		this.logger.debug("After getting values exit from method");
		return viewForJSON;
	}

	public ModelAndView getUnlinkedCRN(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			CRNInfoVO crnInfoVO) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			if (null != httpServletRequest.getParameter("crn")) {
				this.crn = httpServletRequest.getParameter("crn");
			}

			int start = Integer.parseInt(httpServletRequest.getParameter("start"));
			int limit = Integer.parseInt(httpServletRequest.getParameter("limit"));
			limit += start;
			this.logger.debug("Start " + (start + 1));
			this.logger.debug("limit " + limit);
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put("start", start + 1);
			paramMap.put("limit", limit);
			paramMap.put("crn", this.crn);
			paramMap.put("crnList", StringUtils.commaSeparatedStringToList(crnInfoVO.getSearchedCRN()));
			String reqRecDate = crnInfoVO.getReq_recd_dt();
			this.logger.debug("reqRecDate " + reqRecDate);
			int tIndex = crnInfoVO.getReq_recd_dt().indexOf("T");
			String searchDate = "";
			if (tIndex != -1) {
				searchDate = reqRecDate.substring(0, tIndex);
			}

			this.logger.debug("searchDate" + searchDate);
			paramMap.put("searchDate", searchDate);
			this.logger.debug("value of crn is :: " + this.crn);
			List unlinkedCRN = this.locator.getInvoiceService().getUnlinkedCRN(paramMap);
			this.logger.debug("size of unlinked crn " + unlinkedCRN.size());
			viewForJSON.addObject("unLinkedCRN", unlinkedCRN);
			viewForJSON.addObject("total", this.locator.getInvoiceService().getTotalCountUnlinkedCRN(paramMap));
			return viewForJSON;
		} catch (NullPointerException var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, httpServletResponse);
		} catch (CMSException var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, httpServletResponse);
		} catch (Exception var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, httpServletResponse);
		}
	}

	public ModelAndView getPrepaymentSummary(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CRNInfoVO crnInfoVO) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			if (null != httpServletRequest.getParameter("crn")) {
				this.crn = httpServletRequest.getParameter("crn");
			}

			int start = Integer.parseInt(httpServletRequest.getParameter("start"));
			int limit = Integer.parseInt(httpServletRequest.getParameter("limit"));
			limit += start;
			this.logger.debug("Start " + (start + 1));
			this.logger.debug("limit " + limit);
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put("start", start + 1);
			paramMap.put("limit", limit);
			paramMap.put("crn", this.crn);
			paramMap.put("crnList", StringUtils.commaSeparatedStringToList(crnInfoVO.getSearchedCRN()));
			this.logger.debug("value of crn is :: " + this.crn);
			List prepaymentSummary = this.locator.getInvoiceService().getPrepaymentSummary(paramMap);
			this.logger.debug("size of unlinked crn " + prepaymentSummary.size());
			viewForJSON.addObject("prepaymentSummaryRoot", prepaymentSummary);
			viewForJSON.addObject("total", this.locator.getInvoiceService().getTotalCountUnlinkedCRNParent(paramMap));
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpServletResponse);
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, httpServletResponse);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, httpServletResponse);
		}
	}

	public ModelAndView getUnlinkedCRNParent(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CRNInfoVO crnInfoVO) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			if (null != httpServletRequest.getParameter("crn")) {
				this.crn = httpServletRequest.getParameter("crn");
			}

			int start = Integer.parseInt(httpServletRequest.getParameter("start"));
			int limit = Integer.parseInt(httpServletRequest.getParameter("limit"));
			limit += start;
			this.logger.debug("Start " + (start + 1));
			this.logger.debug("limit " + limit);
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put("start", start + 1);
			paramMap.put("limit", limit);
			paramMap.put("crn", this.crn);
			paramMap.put("crnList", StringUtils.commaSeparatedStringToList(crnInfoVO.getSearchedCRN()));
			this.logger.debug("value of crn is :: " + this.crn);
			List unlinkedCRN = this.locator.getInvoiceService().getUnlinkedCRNParent(paramMap);
			this.logger.debug("size of unlinked crn " + unlinkedCRN.size());
			viewForJSON.addObject("unLinkedCRNParent", unlinkedCRN);
			viewForJSON.addObject("total", this.locator.getInvoiceService().getTotalCountUnlinkedCRNParent(paramMap));
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpServletResponse);
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, httpServletResponse);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, httpServletResponse);
		}
	}

	public ModelAndView unlinkCRN(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			String caseIds = null;
			String unlinkedCRNs = "";
			if (null != httpServletRequest.getParameter("unlink_crn_ids")) {
				caseIds = httpServletRequest.getParameter("unlink_crn_ids");
			}

			this.logger.debug("value of UNLINK_CRN_IDS is :: " + caseIds);
			if (null != httpServletRequest.getParameter("unlink_crns")) {
				unlinkedCRNs = httpServletRequest.getParameter("unlink_crns");
			}

			this.logger.debug("value of unlinkedCRNs is :: " + unlinkedCRNs);
			HashMap params = new HashMap();
			params.put("caseList", StringUtils.commaSeparatedStringToIntList(caseIds));
			params.put("unlinkedCrnList", StringUtils.commaSeparatedStringToList(unlinkedCRNs));
			this.locator.getInvoiceService().unlinkCRN(params);
			return viewForJSON;
		} catch (NullPointerException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, httpServletResponse);
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, httpServletResponse);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, httpServletResponse);
		}
	}

	public ModelAndView unlinkChildCRN(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			String unlinkedCRNs = "";
			if (null != httpServletRequest.getParameter("unlink_crns")) {
				unlinkedCRNs = httpServletRequest.getParameter("unlink_crns");
			}

			this.logger.debug("value of unlinkedCRNs is :: " + unlinkedCRNs);
			HashMap params = new HashMap();
			params.put("unlinkedCrnList", StringUtils.commaSeparatedStringToList(unlinkedCRNs));
			this.locator.getInvoiceService().unlinkChildCRN(params);
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, httpServletResponse);
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, httpServletResponse);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, httpServletResponse);
		}
	}

	public ModelAndView inserLinkCRN(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			List linkCRNList = new ArrayList();
			Calendar calendar = Calendar.getInstance();
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMinimumIntegerDigits(2);
			String linkCRN = httpServletRequest.getParameter("linkCRN");
			String updateBY = httpServletRequest.getParameter("updateBy");
			String crn = httpServletRequest.getParameter("crn");
			HashMap<String, String> paramMap = new HashMap();
			String[] stringArray = linkCRN.split(",");
			String registerNo = "C" + httpServletRequest.getParameter("registerNo") + "";
			paramMap.put("regNo", registerNo);
			paramMap.put("crn", crn);

			for (int i = 0; i < stringArray.length; ++i) {
				AccountsLinkcasesVO accountsLlinkcasesVO = new AccountsLinkcasesVO();
				accountsLlinkcasesVO.setCrn(crn);
				accountsLlinkcasesVO.setLinkedCRN(stringArray[i]);
				accountsLlinkcasesVO.setUpdateBy(updateBY);
				linkCRNList.add(accountsLlinkcasesVO);
			}

			this.locator.getInvoiceService().linkCRN(linkCRNList);
			this.logger.debug("Linked the crns");
			this.logger.debug("paramMap " + paramMap);
			this.locator.getInvoiceService().updateRegNo(paramMap);
			paramMap.put("regNo", httpServletRequest.getParameter("registerNo"));
			this.locator.getInvoiceService().updateParentCrnRegNo(paramMap);
			this.logger.debug("Updated register nos");
			return viewForJSON;
		} catch (NullPointerException var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, httpServletResponse);
		} catch (CMSException var16) {
			return AtlasUtils.getJsonExceptionView(this.logger, var16, httpServletResponse);
		} catch (Exception var17) {
			return AtlasUtils.getJsonExceptionView(this.logger, var17, httpServletResponse);
		}
	}

	public ModelAndView inserLinkCRNParent(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			List linkCRNList = new ArrayList();
			Calendar calendar = Calendar.getInstance();
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMinimumIntegerDigits(2);
			String linkCRN = httpServletRequest.getParameter("linkCRN");
			String updateBY = httpServletRequest.getParameter("updateBy");
			String crn = httpServletRequest.getParameter("crn");
			HashMap<String, String> paramMap = new HashMap();
			String[] stringArray = linkCRN.split(",");
			paramMap.put("crn", crn);
			paramMap.put("linkCRN", linkCRN);

			for (int i = 0; i < stringArray.length; ++i) {
				AccountsLinkcasesVO accountsLlinkcasesVO = new AccountsLinkcasesVO();
				accountsLlinkcasesVO.setCrn(crn);
				accountsLlinkcasesVO.setLinkedCRN(stringArray[i]);
				accountsLlinkcasesVO.setUpdateBy(updateBY);
				linkCRNList.add(accountsLlinkcasesVO);
			}

			this.locator.getInvoiceService().linkCRNParent(linkCRNList);
			this.logger.debug("Linked the crns");
			this.logger.debug("paramMap " + paramMap);
			String registerNo = this.locator.getInvoiceService().getParentInvoiceNo(linkCRN);
			if (registerNo != null) {
				registerNo = "C" + registerNo;
				paramMap.put("regNo", registerNo);
				this.locator.getInvoiceService().updateRegNoParent(paramMap);
			}

			this.logger.debug("Updated register nos");
			return viewForJSON;
		} catch (NullPointerException var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, httpServletResponse);
		} catch (CMSException var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, httpServletResponse);
		} catch (Exception var16) {
			return AtlasUtils.getJsonExceptionView(this.logger, var16, httpServletResponse);
		}
	}

	public ModelAndView generateRegisterNo(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			Integer regNo = this.locator.getInvoiceService().getMaxRegisterNO();
			Calendar calendar = Calendar.getInstance();
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMinimumIntegerDigits(5);
			String formatedRegNo = formatter.format((long) (regNo + 1));
			formatter.setMinimumIntegerDigits(2);
			formatter.setGroupingUsed(false);
			String registerNo = "C" + formatedRegNo + "";
			HashMap<String, String> paramMap = new HashMap();
			String crn = httpServletRequest.getParameter("crn");
			this.logger.debug("registerNo " + registerNo + "formatedRegNo " + formatedRegNo);
			paramMap.put("regNo", registerNo.replace(",", ""));
			paramMap.put("crn", crn);
			this.locator.getInvoiceService().updateRegNo(paramMap);
			paramMap.put("regNo", formatedRegNo.replace(",", ""));
			this.locator.getInvoiceService().updateParentCrnRegNo(paramMap);
			viewForJSON.addObject("registerNo", formatedRegNo.replace(",", ""));
			return viewForJSON;
		} catch (NullPointerException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, httpServletResponse);
		} catch (CMSException var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, httpServletResponse);
		} catch (Exception var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, httpServletResponse);
		}
	}

	public ModelAndView getLegacyInvoiceDetail(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			String crn = null;
			if (null != httpServletRequest.getParameter("crn")) {
				crn = httpServletRequest.getParameter("crn");
			}

			this.logger.debug("value of CRN is :: " + crn);
			viewForJSON.addObject("LegacyInvoiceDetails", this.locator.getInvoiceService().getLegacyInvoiceDetail(crn));
			return viewForJSON;
		} catch (NullPointerException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, httpServletResponse);
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, httpServletResponse);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, httpServletResponse);
		}
	}

	public ModelAndView getParentCRN(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.locator = ResourceLocator.self();
			String crn = null;
			if (null != httpServletRequest.getParameter("crn")) {
				crn = httpServletRequest.getParameter("crn");
			}

			this.logger.debug("value of CRN is :: " + crn);
			viewForJSON.addObject("parentLinkedCRN", this.locator.getInvoiceService().getParentCRN(crn));
			return viewForJSON;
		} catch (NullPointerException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, httpServletResponse);
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, httpServletResponse);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, httpServletResponse);
		}
	}
}