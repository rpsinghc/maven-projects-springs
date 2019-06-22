package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.InvoiceMISVO;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class InvoiceMISDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.report.InvoiceMISDAO");
	private static final String INVOICE_MIS_DATA_EXPORT = "InvoiceMIS.getInvoiceMISData";

	public List<InvoiceMISVO> getInvoiceMISDataForExport(InvoiceMISVO invoicemisvo) throws CMSException {
		this.logger.debug("Entring  InvoiceMISDAO:getInvoiceMISDataForExport");

		try {
			List<InvoiceMISVO> misList = this.queryForList("InvoiceMIS.getInvoiceMISData", invoicemisvo);
			this.logger.debug("Existing  InvoiceMISDAO:getInvoiceMISDataForExport");
			return misList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}