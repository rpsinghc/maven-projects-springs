package com.worldcheck.atlas.frontend.invoice;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import com.worldcheck.atlas.vo.task.invoice.InvoiceVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class InvoiceManagementController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.invoice.InvoiceManagementController");
	private String successView = "";
	private String successCompleteView = "";
	private static final String ZERO = "0";
	private static final String INVOICE_DATE_TEMP = "invoiceDate";
	private static final String CT_DATE_TEMP = "ctDate";
	private static final String INVOICE_NO = "invoiceNO";
	private static final String INVOICE_AMOUNT = "invoiceAmount";
	private static final String CAPWTOWN_ID = "capetownID";
	private static final String INVOICE_CURRENCY_CODE = "invoiceCurrencyCode";
	private static final String PID = "pid";
	private static final String SPLITER_CONS1 = "::";
	private static final String SPLITER_CONS2 = "#";
	private static final String Action_CONS = "save";
	private static final String ACTION = "action";
	private static String REQ_TOCAPETOWN = "toCapetown";
	private static String REQ_CURRENCYCODE = "currencyCode";
	private static String IS_CHECKED = "cancelledCharges";
	private static String REQ_ISISUSER = "isisUsers";
	private static String REQ_UPDATEBY = "updatedBy";
	private static String REQ_CRN = "crn";
	private static String DISCOUNT = "discount";
	private static String CLIENT_CODE = "clientCode";
	private static String CAPETOWN_ID = "capetownID";
	private static String CASE_FEE = "caseFee";
	private static String CREDIT = "credit";
	private static String MULTIPLE_YEAR_BONUS = "multipleYearBonus";
	private static String INVOICE_TO = "invoiceTo";
	private static String BILLING_ADDRESS = "billingAddress";
	private static String INVOICE_INSTRUCTION = "invoiceInst";
	private static String ACCOUNT_ID = "accountID";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("mm/dd/yyyy");

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public void setSuccessCompleteView(String successCompleteView) {
		this.successCompleteView = successCompleteView;
	}

	public ModelAndView saveOrCompleteInvoice(HttpServletRequest request, HttpServletResponse response,
			AccountsVO accountsVO) {
		String[] temp = (String[]) null;
		this.logger.debug("cancelledCharges::" + request.getParameter("cancelledCharges"));
		this.logger.debug("in saveInvoice..");
		this.logger.debug("Action value is ::" + request.getParameter("action"));
		ResourceLocator resourceLocator = ResourceLocator.self();
		boolean submitFlag = false;

		ModelAndView modelAndView;
		try {
			this.logger.debug("request capetownID" + request.getParameter("capetownID") + "request accountID"
					+ request.getParameter("accountID") + "request caseFee" + request.getParameter("caseFee")
					+ " request Credit " + request.getParameter("credit") + " request multipleYearBonus "
					+ request.getParameter("multipleYearBonus") + request.getParameter("isSubjLevelSubRptReq"));
			this.logger.debug("isSubjLevelSubRptReq" + request.getParameter("isSubjLevelSubRptReq"));
			if (request.getParameter("reqFrom") == null) {
				submitFlag = true;
			}

			if (request.getParameter("workItem") != null) {
				temp = request.getParameter("workItem").split("::");
			} else {
				temp = ("CaseCreation#" + request.getParameter("pid") + "::" + "Invoicing").split("::");
			}

			this.logger.debug("temp " + temp[0]);
			accountsVO = this.getCompleteAccountVO(accountsVO, request);
			String isSubjectLevelBudgetUpdated = request.getParameter("isSubjectLevelBudgetUpdated");
			if (isSubjectLevelBudgetUpdated.equalsIgnoreCase("true")) {
				accountsVO.setIsBudgetDueDateConfirmed(false);
			} else if (isSubjectLevelBudgetUpdated.equalsIgnoreCase("false")
					&& request.getParameter("isSubjLevelSubRptReq").equalsIgnoreCase("true")) {
				accountsVO.setBudgetConfirmedFlag("true");
			}

			if (accountsVO.getAccountID() != null && !accountsVO.getAccountID().isEmpty()
					&& !accountsVO.getAccountID().equals("0")) {
				this.logger.debug("Account Updation is called");
				resourceLocator.getInvoiceService().updateInvoiceDetails(accountsVO);
				String[] modifiedRecords = accountsVO.getModifiedRecords();
				String gridData = Arrays.toString(modifiedRecords);
				this.logger.debug("modifiedRecords::" + gridData);
				if (request.getParameter("isSubjLevelSubRptReq").equalsIgnoreCase("true")) {
					ResourceLocator.self().getSubjectService().updateSubjectBudget(gridData, accountsVO.getCrn());
				}
			} else {
				this.logger.debug("Account insertion is called");
				resourceLocator.getInvoiceService().insertInvoiceDetails(accountsVO);
			}

			InvoiceVO invoiceVO = this.getCompleteInvoiceVO(request);
			if (request.getParameter("capetownID") != null && !request.getParameter("capetownID").isEmpty()
					&& !"0".equals(request.getParameter("capetownID"))) {
				this.logger.debug("updateCapeTownDetails  is called");
				invoiceVO.setCapetownID(Integer.parseInt(request.getParameter("capetownID")));
				resourceLocator.getInvoiceService().updateCapeTownDetails(invoiceVO);
				resourceLocator.getInvoiceService().updateClientDetails(invoiceVO);
			} else {
				this.logger.debug("Insert CapeTownDetails  is called");
				resourceLocator.getInvoiceService().insertCapeTownDetails(invoiceVO);
			}

			this.logger.debug("invoicing performer is >> " + request.getParameter(REQ_UPDATEBY));
			this.logger.debug("pid is >> " + request.getParameter("pid"));
			this.logger.debug("action in saveOrCompleteInvoice method::" + request.getParameter("action"));
			if (!submitFlag) {
				modelAndView = new ModelAndView(
						"redirect:/bpmportal/atlas/accountSection.do?crn=" + request.getParameter(REQ_CRN));
			} else {
				String[] activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(
						SBMUtils.getSession(request), (long) Integer.parseInt(request.getParameter("pid")));
				HashMap<String, Object> dsValues = new HashMap();

				for (int i = 0; i < activatedWSNames.length; ++i) {
					this.logger.debug("activated workstep for case creation process is " + activatedWSNames[i]);
					if (activatedWSNames[i].equals("Invoicing Task")) {
						this.logger.debug("inside condition for Invoicing task workstep");
						dsValues.put("taskPerformer", request.getParameter(REQ_UPDATEBY));
						ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
								(long) Integer.parseInt(request.getParameter("pid")), dsValues);
						this.logger.debug("taskPerformer dataslot updated for invoicing...");
						break;
					}
				}

				if ("save".equals(request.getParameter("action"))) {
					modelAndView = new ModelAndView(this.successView);
					resourceLocator.getSBMService().saveTask(Long.parseLong(temp[0].split("#")[1]), temp[1],
							SBMUtils.getSession(request), (HashMap) SBMUtils.setDS(request));
				} else {
					modelAndView = new ModelAndView(this.successCompleteView);
					resourceLocator.getSBMService().completeTask(SBMUtils.getSession(request), (HashMap) null,
							Long.parseLong(temp[0].split("#")[1]), temp[1]);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO.setCrn(request.getParameter(REQ_CRN));
					timeTrackerVO.setUserName(request.getParameter(REQ_UPDATEBY));
					timeTrackerVO.setUpdatedBy(request.getParameter(REQ_UPDATEBY));
					timeTrackerVO.setTaskName("Invoicing Task");
					timeTrackerVO.setTaskPid(temp[0].split("#")[1]);
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					request.getSession().removeAttribute("TrackerOn");
				}
			}

			CaseHistory caseHistory = new CaseHistory();
			caseHistory.setCRN(request.getParameter(REQ_CRN));
			caseHistory.setTaskName("Invoicing Task");
			if (request.getSession().getAttribute("loginLevel") != null) {
				caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseHistory.setPerformer(request.getParameter(REQ_UPDATEBY));
			}

			caseHistory.setTaskStatus("In Progress");
			caseHistory.setPid(request.getParameter("pid"));
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(this.getOldCaseDetails(request),
					this.getNewCaseDetails(request, accountsVO.getCaseFee()), caseHistory, "Invoicing Task");
		} catch (NumberFormatException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (NullPointerException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		} catch (CMSException var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		} catch (Exception var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}

		this.logger.debug("\n ws saved Sucessfully..");
		return modelAndView;
	}

	private AccountsVO getCompleteAccountVO(AccountsVO accountsVO, HttpServletRequest request) {
		if (request.getParameter(REQ_TOCAPETOWN) == null) {
			accountsVO.setToCapetown("0");
		} else {
			accountsVO.setToCapetown(request.getParameter(REQ_TOCAPETOWN));
		}

		accountsVO.setCaseFee(request.getParameter(CASE_FEE));
		accountsVO.setCredit(request.getParameter(CREDIT));
		accountsVO.setMultipleYearBonus(request.getParameter(MULTIPLE_YEAR_BONUS));
		accountsVO.setCurrencyCode(request.getParameter(REQ_CURRENCYCODE));
		accountsVO.setCancelledCharges(request.getParameter(IS_CHECKED));
		accountsVO.setIsisUser(request.getParameter(REQ_ISISUSER));
		accountsVO.setUpdateBy(request.getParameter(REQ_UPDATEBY));
		accountsVO.setCrn(request.getParameter(REQ_CRN));
		accountsVO.setAccountID(request.getParameter(ACCOUNT_ID));
		this.logger.debug("AccountID " + accountsVO.getAccountID() + "BillingMethod " + accountsVO.getBillingMethod()
				+ "CaseFee " + accountsVO.getCaseFee() + "Credit " + accountsVO.getCredit() + "Multiple Year Bonus "
				+ accountsVO.getMultipleYearBonus() + "ClientCode " + accountsVO.getClientCode() + "Crn "
				+ accountsVO.getCrn() + "CurrencyCode " + accountsVO.getCurrencyCode() + "HandledBY "
				+ accountsVO.getHandledBY() + "IsisUser" + accountsVO.getIsisUser() + "Capetown "
				+ request.getParameter("toCapetown") + "Cancelled Charges" + accountsVO.getCancelledCharges());
		return accountsVO;
	}

	private InvoiceVO getCompleteInvoiceVO(HttpServletRequest request) {
		this.logger.debug("invoiceAddress::" + request.getParameter(BILLING_ADDRESS) + "invoiceTo::"
				+ request.getParameter(INVOICE_TO) + "invoiceInstruction::" + request.getParameter(INVOICE_INSTRUCTION)
				+ "Client Code::" + request.getParameter(CLIENT_CODE) + "capetown Id:::"
				+ request.getParameter(CAPETOWN_ID));
		InvoiceVO invoiceVO = new InvoiceVO();
		invoiceVO.setClientCode(request.getParameter(CLIENT_CODE));
		invoiceVO.setInvoiceTo(request.getParameter(INVOICE_TO));
		invoiceVO.setInvoiceAddress(request.getParameter(BILLING_ADDRESS));
		invoiceVO.setInvoiceInstruction(request.getParameter(INVOICE_INSTRUCTION));
		invoiceVO.setCapetownID(Integer.parseInt(request.getParameter(CAPETOWN_ID)));
		invoiceVO.setInvoiceAmount(request.getParameter("invoiceAmount"));
		invoiceVO.setInvoiceNO(request.getParameter("invoiceNO"));
		invoiceVO.setInvoiceCurrencyCode(request.getParameter("invoiceCurrencyCode"));
		invoiceVO.setInvoiceDate(request.getParameter("invoiceDate"));
		invoiceVO.setCtDate(request.getParameter("ctDate"));
		invoiceVO.setDiscount(request.getParameter(DISCOUNT));
		invoiceVO.setCrn(request.getParameter(REQ_CRN));
		invoiceVO.setUpdateBy(request.getParameter(REQ_UPDATEBY));
		return invoiceVO;
	}

	private CaseDetails getOldCaseDetails(HttpServletRequest request) throws ParseException {
		CaseDetails caseDetails = new CaseDetails();
		this.logger.debug("Invoice no " + request.getParameter("ch_invoiceNo") + " CapeTown "
				+ request.getParameter("ch_toCapetown") + " Case Currency " + request.getParameter("ch_caseCurrency"));
		caseDetails.setInvoiceNumber(request.getParameter("ch_invoiceNo"));
		if (request.getParameter("ch_ctDate") != null && !request.getParameter("ch_ctDate").isEmpty()) {
			this.logger.debug("Ct Date" + request.getParameter("ch_ctDate"));
			caseDetails.setCapeTownDate(this.dateFormat.parse(request.getParameter("ch_ctDate")));
		}

		if (request.getParameter("ch_toCapetown") == null) {
			caseDetails.setCapeTown(false);
		} else if ("0".equals(request.getParameter("ch_toCapetown"))) {
			caseDetails.setCapeTown(false);
		} else {
			caseDetails.setCapeTown(true);
		}

		caseDetails.setCaseCurrency(request.getParameter("ch_caseCurrency"));
		caseDetails.setCaseFee(request.getParameter("ch_caseFee"));
		return caseDetails;
	}

	private CaseDetails getNewCaseDetails(HttpServletRequest request, String caseFee) throws ParseException {
		CaseDetails caseDetails = new CaseDetails();
		caseDetails.setInvoiceNumber(request.getParameter("invoiceNO"));
		this.logger.debug("CT_DATE_TEMP" + request.getParameter("ctDate"));
		this.logger.debug("REQ_TOCAPETOWN " + request.getParameter(REQ_TOCAPETOWN));
		if (request.getParameter("ctDate") != null && !request.getParameter("ctDate").isEmpty()) {
			caseDetails.setCapeTownDate(this.dateFormat2.parse(request.getParameter("ctDate")));
		}

		if (request.getParameter(REQ_TOCAPETOWN) == null) {
			caseDetails.setCapeTown(false);
		} else if ("0".equals(request.getParameter(REQ_TOCAPETOWN))) {
			caseDetails.setCapeTown(false);
		} else {
			caseDetails.setCapeTown(true);
		}

		caseDetails.setCaseCurrency(request.getParameter(REQ_CURRENCYCODE));
		caseDetails.setCaseFee(caseFee);
		return caseDetails;
	}

	public ModelAndView accountSection(HttpServletRequest request, HttpServletResponse response,
			AccountsVO accountsVO) {
		ModelAndView modelAndView = new ModelAndView("accountSection");
		modelAndView.addObject(REQ_CRN, request.getParameter(REQ_CRN));
		return modelAndView;
	}
}