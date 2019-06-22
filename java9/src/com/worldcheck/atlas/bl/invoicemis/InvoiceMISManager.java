package com.worldcheck.atlas.bl.invoicemis;

import com.worldcheck.atlas.dao.report.InvoiceMISDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.InvoiceMISVO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

public class InvoiceMISManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.bl.invoicemis.InvoiceMISManager");
	InvoiceMISDAO invoiceMISDao = null;

	public void setInvoiceMISDao(InvoiceMISDAO invoiceMISDao) {
		this.invoiceMISDao = invoiceMISDao;
	}

	public List<InvoiceMISVO> getInvoiceMISDataForExport(Map<String, Object> excelParamMap) throws CMSException {
		this.logger.debug("Entring InvoiceMISManager:getInvoiceMISDataForExport");

		try {
			this.logger.debug("clientCode " + excelParamMap.get("clientCode"));
			this.logger.debug("Case Status " + excelParamMap.get("caseStatus"));
			InvoiceMISVO invoicemisVo = new InvoiceMISVO();
			String clientcode;
			ArrayList list;
			if (excelParamMap.get("clientCode").toString() != null) {
				clientcode = excelParamMap.get("clientCode").toString();
				list = new ArrayList(Arrays.asList(clientcode.split(",")));
				invoicemisVo.setClientCode(list);
			}

			if (excelParamMap.get("caseStatus").toString() != null) {
				clientcode = excelParamMap.get("caseStatus").toString();
				list = new ArrayList(Arrays.asList(clientcode.split(",")));
				invoicemisVo.setCaseStatus(list);
			}

			if (excelParamMap.get("caseReceivedDateFrom").toString() != null) {
				invoicemisVo.setCaseCreationDateFrom(excelParamMap.get("caseReceivedDateFrom").toString());
			}

			if (excelParamMap.get("caseReceivedDateTo").toString() != null) {
				invoicemisVo.setCaseCreationDateTo(excelParamMap.get("caseReceivedDateTo").toString());
			}

			if (excelParamMap.get("finalSentDateFrom").toString() != null) {
				invoicemisVo.setFinalaReportSentdateFrom(excelParamMap.get("finalSentDateFrom").toString());
			}

			if (excelParamMap.get("finalSentDateTo").toString() != null) {
				invoicemisVo.setFinalaReportSentdateTo(excelParamMap.get("finalSentDateTo").toString());
			}

			List<InvoiceMISVO> invoiceMISList = this.invoiceMISDao.getInvoiceMISDataForExport(invoicemisVo);
			this.logger.debug("Existing InvoiceMISManager:getInvoiceMISDataForExport");
			return invoiceMISList;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}