package com.worldcheck.atlas.dao.task.invoice;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.task.invoice.AccountsLinkcasesVO;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import com.worldcheck.atlas.vo.task.invoice.CRNInfoVO;
import com.worldcheck.atlas.vo.task.invoice.CapetownInfoVO;
import com.worldcheck.atlas.vo.task.invoice.InvoiceVO;
import com.worldcheck.atlas.vo.task.invoice.PrepaymentSummaryVO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class InvoiceManagementDAO extends SqlMapClientTemplate {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.invoice.InvoiceManagementDAO");
	private static final String ACCOUNT_LINK_CASE = "Invoice.getAccountLinkCase";
	private static final String INVOICE_DETAIL = "Invoice.getInvoice";
	private static final String REPORT_DETAIL = "Invoice.getReportTypeCount";
	private static final String ACCOUNT_DETAILS = "Invoice.getAccountDetail";
	private static final String INVOICE_REGISTER_DETAILS = "Invoice.getParentInvoiceNo";
	private static final String INSERT_INVOICE_DETAIL = "Invoice.insertInvoiceDetails";
	private static final String UPDATE_INVOICE_DETAIL = "Invoice.updateInvoiceDetails";
	private static final String UPDATE_CAPTOWN_DETAIL = "Invoice.updateCapetownDetails";
	private static final String RELATED_CRN = "Invoice.getRelatedCRN";
	private static final String UNLINKED_CRN_DETAIL = "Invoice.getUnlinkedCRN";
	private static final String UNLINKED_CRN_DETAIL_PARENT = "Invoice.getUnlinkedCRNParent";
	private static final String PREPAYMENT_SUMMARY_DETAILS = "Invoice.getPrepaymentSummary";
	private static final String UPDATE_REGISTER_INFO = "Invoice.updateRegNo";
	private static final String UPDATE_REGISTER_INFO_PARENT = "Invoice.updateRegNoParent";
	private static final String UPDATE_PARENT_REGISTER_INFO = "Invoice.updateParentCRNRegNo";
	private static final String CAPETOWN_INFO = "Invoice.getCapetownInfo";
	private static final String PREPAYMENT_SUMMARY_CRN = "Invoice.getPrepaymentSummaryCRN";
	private static final String INSERT_CAPETOWN_INFO = "Invoice.insertCapeTownDetails";
	private static final String REGISTER_NO = "Invoice.getMaxRegisterNO";
	private static final String UNLINKED_CRN = "Invoice.unlinkCRN";
	private static final String UNLINKED_CHILD_CRN = "Invoice.unlinkChildCRN";
	private static final String LINK_CRN = "Invoice.insertLinkedCRN";
	private static final String LINK_CRN_PARENT = "Invoice.insertLinkedCRNParent";
	private static final String IS_CHILD_CRN = "Invoice.isChildCRN";
	private static final String TOTAL_COUNT_UNLINKRD_CRN = "Invoice.getTotalCountUnlinkedCRN";
	private static final String TOTAL_COUNT_UNLINKRD_CRN_PARENT = "Invoice.getTotalCountUnlinkedCRNParent";
	private static final String UPDATE_ACCOUNTS_CHILD_CRN = "Invoice.updateAccountsForChildCrn";
	private static final String TOTAL_COUNT_LINKRD_CRN = "Invoice.getTotalCountLinkedCRN";
	private static final String LEGACY_INFO = "Invoice.getAccountInfoForLegacy";
	private static final String PARENT_CRN = "Invoice.getParentCRN";
	private static final String CLEAR_REG_NUM_UPON_UNLINKING = "Invoice.updateRegDateNumberOnUnLinking";
	private static final String IS_ISIS_CASE = "Invoice.isISISCase";
	private static final String UPDATE_CLIENT_DETAIL = "Invoice.updateClientDetail";
	private static final String ACCOUNT_DETAILS_CRN = "Invoice.getAccountDetailForCase";
	private static final String UPDATE_CASE_FEE = "Invoice.updateCaseFee";
	private static final String UPDATE_EDDO_CASE_FEE = "Invoice.updateEDDOCaseFee";

	public int getReportTypeCount(String crn) throws CMSException {
		try {
			return (Integer) this.queryForObject("Invoice.getReportTypeCount", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<InvoiceVO> getInvoice(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getInvoice", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<AccountsLinkcasesVO> getAccountLinkcase(HashMap paramMap) throws CMSException {
		try {
			return this.queryForList("Invoice.getAccountLinkCase", paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<AccountsVO> getAccountDetail(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getAccountDetail", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public AccountsVO getAccountDetailForCrn(String crn) throws CMSException {
		AccountsVO accountsVo = null;

		try {
			accountsVo = (AccountsVO) this.queryForObject("Invoice.getAccountDetail", crn);
			return accountsVo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getInvoiceRegisterNo(String crn) throws CMSException {
		try {
			return (String) this.queryForObject("Invoice.getParentInvoiceNo", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CapetownInfoVO> getCapetownInfo(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getCapetownInfo", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<PrepaymentSummaryVO> getPrepaymentSummaryCRN(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getPrepaymentSummaryCRN", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer getMaxRegisterNO() throws CMSException {
		try {
			return (Integer) this.queryForObject("Invoice.getMaxRegisterNO");
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int insertInvoiceDetails(AccountsVO accountsVO) throws CMSException {
		boolean var2 = false;

		try {
			int added = (Integer) this.insert("Invoice.insertInvoiceDetails", accountsVO);
			return added;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateInvoiceDetails(AccountsVO accountsVO) throws CMSException {
		boolean var2 = false;

		try {
			int updated = this.update("Invoice.updateInvoiceDetails", accountsVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateCapeTownDetails(InvoiceVO invoiceVO) throws CMSException {
		boolean var2 = false;

		try {
			int updated = this.update("Invoice.updateCapetownDetails", invoiceVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateClientDetails(InvoiceVO invoiceVO) throws CMSException {
		boolean var2 = false;

		try {
			int updated = this.update("Invoice.updateClientDetail", invoiceVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRegNo(HashMap<String, String> paramMap) throws CMSException {
		boolean var2 = false;

		try {
			int updated = this.update("Invoice.updateRegNo", paramMap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRegNoParent(HashMap<String, String> paramMap) throws CMSException {
		boolean var2 = false;

		try {
			int updated = this.update("Invoice.updateRegNoParent", paramMap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateParentCrnRegNo(HashMap<String, String> paramMap) throws CMSException {
		boolean var2 = false;

		try {
			int updated = this.update("Invoice.updateParentCRNRegNo", paramMap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<AccountsVO> getRelatedCRN(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getRelatedCRN", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CRNInfoVO> getUnlinkedCRN(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.queryForList("Invoice.getUnlinkedCRN", paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CRNInfoVO> getUnlinkedCRNParent(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.queryForList("Invoice.getUnlinkedCRNParent", paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<PrepaymentSummaryVO> getPrepaymentSummary(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.queryForList("Invoice.getPrepaymentSummary", paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void unlinkChildCRN(HashMap params) throws CMSException {
		try {
			this.delete("Invoice.unlinkChildCRN", params);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void unlinkCRN(HashMap params) throws CMSException {
		try {
			this.delete("Invoice.unlinkCRN", params);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void linkCRN(List<AccountsLinkcasesVO> crnList) throws CMSException {
		try {
			Iterator crnItr = crnList.iterator();

			while (crnItr.hasNext()) {
				AccountsLinkcasesVO accountsLlinkcasesVO = (AccountsLinkcasesVO) crnItr.next();
				this.insert("Invoice.insertLinkedCRN", accountsLlinkcasesVO);
			}

		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void linkCRNParent(List<AccountsLinkcasesVO> crnList) throws CMSException {
		try {
			Iterator crnItr = crnList.iterator();

			while (crnItr.hasNext()) {
				AccountsLinkcasesVO accountsLlinkcasesVO = (AccountsLinkcasesVO) crnItr.next();
				this.insert("Invoice.insertLinkedCRNParent", accountsLlinkcasesVO);
			}

		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getTotalCountUnlinkedCRN(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return (Integer) this.queryForObject("Invoice.getTotalCountUnlinkedCRN", paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getTotalCountUnlinkedCRNParent(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return (Integer) this.queryForObject("Invoice.getTotalCountUnlinkedCRNParent", paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int isChildCRN(String crn) throws CMSException {
		try {
			return (Integer) this.queryForObject("Invoice.isChildCRN", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public Integer insertCapeTownDetails(InvoiceVO invoiceVO) throws CMSException {
		try {
			return (Integer) this.insert("Invoice.insertCapeTownDetails", invoiceVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateAccountChildCRN(AccountsVO params) throws CMSException {
		try {
			this.update("Invoice.updateAccountsForChildCrn", params);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getTotalCountlinkedCRN(String crn) throws CMSException {
		try {
			return (Integer) this.queryForObject("Invoice.getTotalCountLinkedCRN", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CapetownInfoVO> getLegacyInvoiceDetail(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getAccountInfoForLegacy", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CRNInfoVO> getParentCRN(String crn) throws CMSException {
		try {
			return this.queryForList("Invoice.getParentCRN", crn);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void clearChildCRNRegisterNoUponUnlinking(HashMap params) throws CMSException {
		try {
			this.update("Invoice.updateRegDateNumberOnUnLinking", params);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int isISISCase(String crn) throws CMSException {
		try {
			return Integer.parseInt(this.queryForObject("Invoice.isISISCase", crn).toString());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public long updateCaseFee(AccountsVO accountsVO) throws CMSException {
		long updated = 0L;

		try {
			updated = (long) this.update("Invoice.updateCaseFee", accountsVO);
			return updated;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public long updateEDDOCaseFee(AccountsVO accountsVO) throws CMSException {
		long updated = 0L;

		try {
			updated = (long) this.update("Invoice.updateEDDOCaseFee", accountsVO);
			return updated;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}