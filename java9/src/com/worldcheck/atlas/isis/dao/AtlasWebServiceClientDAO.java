package com.worldcheck.atlas.isis.dao;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class AtlasWebServiceClientDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.dao.AtlasWebServiceClientDAO");

	public int insertJMSQueueRecord(Map map) throws CMSException {
		this.logger.debug("Inside insertJMSQueueRecord method of AtlasWebServiceClientDAO");
		boolean var2 = false;

		try {
			int sequenceId = (Integer) this.insert("AtlasWebServiceClient.insertJMSQueueRecord", map);
			return sequenceId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}