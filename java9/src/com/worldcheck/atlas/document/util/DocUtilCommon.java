package com.worldcheck.atlas.document.util;

import com.savvion.sbm.bizlogic.util.BLDocService;
import com.savvion.sbm.dms.DSContext;
import com.savvion.sbm.dms.DocumentService;
import com.savvion.sbm.dms.DocumentServiceFactory;

public class DocUtilCommon {
	private static DSContext ds;

	public static DocumentService getDocService() {
		DocumentService docService = DocumentServiceFactory.getInstance("S0");
		return docService;
	}

	public static DocumentService getDocService(String store) {
		DocumentService docService = DocumentServiceFactory.getInstance(store);
		return docService;
	}

	public static DSContext getDSContext() {
		if (ds == null) {
			ds = BLDocService.startSession();
		}

		return ds;
	}
}