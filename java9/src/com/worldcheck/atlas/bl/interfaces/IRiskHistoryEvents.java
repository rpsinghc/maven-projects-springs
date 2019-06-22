package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import java.util.List;

public interface IRiskHistoryEvents {
	List<RiskHistory> getRiskHistory(String var1, String var2, long var3, int var5, int var6, String var7, String var8,
			List<String> var9) throws CMSException;

	Object getRiskHistoryCountForRiskCode(String var1, String var2, long var3) throws CMSException;
}