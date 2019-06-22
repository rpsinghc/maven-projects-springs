package com.worldcheck.atlas.bl.junointerface;

import com.worldcheck.atlas.dao.junointerface.CRNInfoDAO;
import com.worldcheck.atlas.vo.junointerface.CRNInfoVO;

public class CRNInfoControllerImpl implements ICRNInfoController {
	private static ICRNInfoController iCRNInfo;

	public static ICRNInfoController getInstance() {
		if (iCRNInfo == null) {
			iCRNInfo = new CRNInfoControllerImpl();
		}

		return iCRNInfo;
	}

	public CRNInfoVO getInfoForCase(String crn) {
		CRNInfoVO crnInfoVO = null;
		CRNInfoDAO crnInfoDAO = null;
		((CRNInfoDAO) crnInfoDAO).getCRNInfo(crn);
		return (CRNInfoVO) crnInfoVO;
	}
}