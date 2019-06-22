package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.SupportAppVO;
import java.util.List;
import java.util.Map;

public interface ISupportApp {
	SupportAppVO searchCRNDetails(String var1) throws CMSException;

	void updateCRNDates(SupportAppVO var1) throws CMSException;

	String getUserPWD(String var1) throws CMSException;

	List<SupportAppVO> getREDataForExport(Map<String, Object> var1) throws CMSException;

	List<SupportAppVO> getHistoryDataForExport(Map<String, Object> var1) throws CMSException;
}