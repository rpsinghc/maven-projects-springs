package com.worldcheck.atlas.isis.impl;

import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import com.worldcheck.atlas.utils.ResourceLocator;

public class AtlasWebServiceImpl {
	public CaseResultVO createOnlineCase(CaseDetailsVO caseDetailsVO) throws Exception {
		ResourceLocator.self().getAtlasWebService().createCaseForISIS(new CaseDetailsVO(), "");
		return null;
	}

	public CaseResultVO updateOnlineCase(CaseDetailsVO caseDetailsVO) {
		return null;
	}

	public DownloadOnlineReportResultVO downloadOnlineReport(String crn, String filename, String version) {
		return null;
	}

	public CaseResultVO cancelOnlineOrder(String crn) {
		return null;
	}

	public CaseResultVO updateClientReferanceNumber(String crn, String clientRefNumber) {
		return null;
	}
}