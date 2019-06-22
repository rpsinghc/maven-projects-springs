package com.worldcheck.atlas.services.visualkey;

import com.worldcheck.atlas.bl.visualkey.VisualKeyManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.visualkey.VisualKey;

public class VisualKeyService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.visualkey.VisualKeyService");
	private VisualKeyManager visualKeyManager;

	public void setVisualKeyManager(VisualKeyManager visualKeyManager) {
		this.visualKeyManager = visualKeyManager;
	}

	public VisualKey getVisualkeyForCRN(String CRN) throws CMSException {
		this.logger.debug("In VisualKeyService.getVisualkeyForCRN");
		new VisualKey();

		try {
			VisualKey visualKey = this.visualKeyManager.getVisualkeyForCRN(CRN);
			this.logger.debug("Exiting VisualKeyService.getVisualkeyForCRN");
			return visualKey;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}