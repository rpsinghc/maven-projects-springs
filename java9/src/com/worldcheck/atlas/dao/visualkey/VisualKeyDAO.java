package com.worldcheck.atlas.dao.visualkey;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.visualkey.VisualKey;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class VisualKeyDAO extends SqlMapClientTemplate {
	private String loggerClass = "com.worldcheck.atlas.dao.visualkey.VisualKeyDAO";
	private ILogProducer logger;

	public VisualKeyDAO() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
	}

	public List<VisualKey> getVisualkeyForCRN(String CRN) throws CMSException {
		this.logger.debug("In VisualKeyDAO.getVisualkeyForCRN");
		new ArrayList();

		List visualKeyList;
		try {
			visualKeyList = this.queryForList("VisualKey.getVisualKey", CRN);
			this.logger.debug("visual key List size :- " + visualKeyList.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting VisualKeyDAO.getVisualkeyForCRN");
		return visualKeyList;
	}
}