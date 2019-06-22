package com.worldcheck.atlas.bl.visualkey;

import com.worldcheck.atlas.dao.visualkey.VisualKeyDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.visualkey.VisualKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VisualKeyManager {
	private String loggerClass = "com.worldcheck.atlas.bl.visualkey.VisualKeyManager";
	private ILogProducer logger;
	private static final String CRN = "CRN";
	private VisualKeyDAO visualKeyDAO;

	public VisualKeyManager() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
	}

	public void setVisualKeyDAO(VisualKeyDAO visualKeyDAO) {
		this.visualKeyDAO = visualKeyDAO;
	}

	public VisualKey getVisualkeyForCRN(String CRN) throws CMSException {
		this.logger.debug("In VisualKeyManager.getVisualkeyForCRN");
		VisualKey visualKey = new VisualKey();

		try {
			new ArrayList();
			List<VisualKey> visualKeyList = this.visualKeyDAO.getVisualkeyForCRN(CRN);
			if (visualKeyList.size() > 0) {
				visualKey.setCrn(((VisualKey) visualKeyList.get(0)).getCrn());
				visualKey.setCaseManager(((VisualKey) visualKeyList.get(0)).getCaseManager());
				int count = 0;
				Iterator iterator = visualKeyList.iterator();

				while (iterator.hasNext()) {
					VisualKey visualKeyObj = (VisualKey) iterator.next();
					if (visualKeyObj.getTeamType() != null) {
						if (visualKeyObj.getTeamType().contains("Primary")) {
							visualKey.setPrimaryAnalyst(
									visualKeyObj.getPrimaryAnalyst() != null ? visualKeyObj.getPrimaryAnalyst() : "");
							visualKey.setManager(visualKeyObj.getManager() != null ? visualKeyObj.getManager() : "");
							visualKey.setSeniorAnalyst(visualKeyObj.getMainAnalystManager() != null
									? visualKeyObj.getMainAnalystManager()
									: "");
						} else if (visualKeyObj.getTeamType().contains("Supporting - BI")) {
							visualKey.setBusinessIntelligence(
									visualKeyObj.getManager() != null ? visualKeyObj.getManager() : "");
						} else if (visualKeyObj.getTeamType().contains("Supporting - Internal")) {
							if (count == 0) {
								visualKey.setSupportingAnalyst(visualKeyObj.getPrimaryAnalyst() != null
										? visualKeyObj.getPrimaryAnalyst()
										: "");
							}

							++count;
						}
					}
				}
			}

			this.logger.debug("Exiting VisualKeyManager.getVisualkeyForCRN");
			return visualKey;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}
}