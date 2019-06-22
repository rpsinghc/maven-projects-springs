package com.worldcheck.atlas.services.task.invoice;

import com.worldcheck.atlas.dao.task.invoice.InvoiceManagementDAO;
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
import java.util.List;

public class InvoiceManagementService {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.task.invoice.InvoiceManagementService");
	private InvoiceManagementDAO invoiceManagementDAO;

	public void setInvoiceManagementDAO(InvoiceManagementDAO invoiceManagementDAO) {
		this.invoiceManagementDAO = invoiceManagementDAO;
	}

	public List<InvoiceVO> getInvoice(String crn) throws CMSException {
		return this.invoiceManagementDAO.getInvoice(crn);
	}

	public int getReportTypeCount(String crn) throws CMSException {
		return this.invoiceManagementDAO.getReportTypeCount(crn);
	}

	public List<CapetownInfoVO> getCapetownInfo(String crn) throws CMSException {
		return this.invoiceManagementDAO.getCapetownInfo(crn);
	}

	public List<PrepaymentSummaryVO> getPrepaymentSummaryCRN(String crn) throws CMSException {
		return this.invoiceManagementDAO.getPrepaymentSummaryCRN(crn);
	}

	public List<AccountsLinkcasesVO> getAccountLinkcase(HashMap paramMap) throws CMSException {
		return this.invoiceManagementDAO.getAccountLinkcase(paramMap);
	}

	public void insertInvoiceDetails(AccountsVO accountsVO) throws CMSException {
		this.invoiceManagementDAO.insertInvoiceDetails(accountsVO);
	}

	public void updateInvoiceDetails(AccountsVO accountsVO) throws CMSException {
		this.invoiceManagementDAO.updateInvoiceDetails(accountsVO);
	}

	public AccountsVO getAccountDetailForCrn(String crn) throws CMSException {
		return this.invoiceManagementDAO.getAccountDetailForCrn(crn);
	}

	public void updateCapeTownDetails(InvoiceVO invoiceVO) throws CMSException {
		this.invoiceManagementDAO.updateCapeTownDetails(invoiceVO);
	}

	public void updateClientDetails(InvoiceVO invoiceVO) throws CMSException {
		this.invoiceManagementDAO.updateClientDetails(invoiceVO);
	}

	public String getParentInvoiceNo(String crn) throws CMSException {
		return this.invoiceManagementDAO.getInvoiceRegisterNo(crn);
	}

	public List<AccountsVO> getAccountDetails(String crn) throws CMSException {
		return this.invoiceManagementDAO.getAccountDetail(crn);
	}

	public List<AccountsVO> getRelatedCRN(String crn) throws CMSException {
		return this.invoiceManagementDAO.getRelatedCRN(crn);
	}

	public Integer getMaxRegisterNO() throws CMSException {
		return this.invoiceManagementDAO.getMaxRegisterNO();
	}

	public void unlinkCRN(HashMap params) throws CMSException {
		try {
			this.invoiceManagementDAO.unlinkCRN(params);
			this.invoiceManagementDAO.clearChildCRNRegisterNoUponUnlinking(params);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void unlinkChildCRN(HashMap params) throws CMSException {
		try {
			this.invoiceManagementDAO.unlinkChildCRN(params);
			this.invoiceManagementDAO.clearChildCRNRegisterNoUponUnlinking(params);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void linkCRN(List<AccountsLinkcasesVO> params) throws CMSException {
		try {
			this.invoiceManagementDAO.linkCRN(params);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void linkCRNParent(List<AccountsLinkcasesVO> params) throws CMSException {
		try {
			this.invoiceManagementDAO.linkCRNParent(params);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CRNInfoVO> getUnlinkedCRN(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.getUnlinkedCRN(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CRNInfoVO> getUnlinkedCRNParent(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.getUnlinkedCRNParent(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<PrepaymentSummaryVO> getPrepaymentSummary(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.getPrepaymentSummary(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updateRegNo(HashMap<String, String> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.updateRegNo(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updateRegNoParent(HashMap<String, String> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.updateRegNoParent(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updateParentCrnRegNo(HashMap<String, String> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.updateParentCrnRegNo(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getTotalCountUnlinkedCRN(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.getTotalCountUnlinkedCRN(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getTotalCountUnlinkedCRNParent(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return this.invoiceManagementDAO.getTotalCountUnlinkedCRNParent(paramMap);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isChildCRN(String crn) throws CMSException {
		try {
			return this.invoiceManagementDAO.isChildCRN(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public Integer insertCapeTownDetails(InvoiceVO invoiceVO) throws CMSException {
		try {
			return this.invoiceManagementDAO.insertCapeTownDetails(invoiceVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public void updateAccountChildCRN(AccountsVO accountsVO) throws CMSException {
		try {
			this.invoiceManagementDAO.updateAccountChildCRN(accountsVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getTotalCountlinkedCRN(String crn) throws CMSException {
		try {
			return this.invoiceManagementDAO.getTotalCountlinkedCRN(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CapetownInfoVO> getLegacyInvoiceDetail(String crn) throws CMSException {
		try {
			return this.invoiceManagementDAO.getLegacyInvoiceDetail(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CRNInfoVO> getParentCRN(String crn) throws CMSException {
		try {
			return this.invoiceManagementDAO.getParentCRN(crn);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getBudgetDetails(String crn) throws CMSException {
		return this.invoiceManagementDAO.isISISCase(crn);
	}
}