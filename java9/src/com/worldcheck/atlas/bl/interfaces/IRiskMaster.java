package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.RiskDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskAttributesMasterVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RisksHistoryVO;
import com.worldcheck.atlas.vo.masters.RisksMapVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import java.util.ArrayList;
import java.util.List;

public interface IRiskMaster {
	void setRiskDAO(RiskDAO var1);

	List<RisksMasterVO> getRiskGrid(RisksMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int getRiskGridCount(RisksMasterVO var1) throws CMSException;

	String changeRiskStatus(String[] var1, String var2, String var3, ArrayList<RisksMasterVO> var4) throws CMSException;

	RisksMasterVO getRiskInfo(String var1) throws CMSException;

	String updateRisk(RisksMasterVO var1, String var2) throws CMSException;

	String addRisks(RisksMasterVO var1) throws CMSException;

	boolean isExistRisk(RisksMasterVO var1) throws CMSException;

	RisksMasterVO checkAssociatedMaster(String var1) throws CMSException;

	List<RisksMasterVO> getRisks(RisksMasterVO var1, int var2, int var3, String var4, String var5) throws CMSException;

	List<RiskCategoryMasterVO> getRiskCategory() throws CMSException;

	String addMapping(RisksMapVO var1) throws CMSException;

	int getRisksCount(RisksMasterVO var1) throws CMSException;

	List<RisksMasterVO> getRiskCaseHistory(RisksMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int getRiskCaseHistoryCount(RisksMasterVO var1) throws CMSException;

	String addMappings(ArrayList<RisksMapVO> var1) throws CMSException;

	List<RiskAttributesMasterVO> getRiskAttributes(RisksMasterVO var1) throws CMSException;

	List<RisksMapVO> getAllRisks(RisksMasterVO var1) throws CMSException;

	String updateMapping(RisksMapVO var1) throws CMSException;

	String changeMappingStatus(RisksMapVO var1) throws CMSException;

	List<RisksHistoryVO> getMappingHistory(RisksHistoryVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int getMappingHistoryGridCount(Integer var1) throws CMSException;

	List<ClientMasterVO> getLHSClientList(String var1) throws CMSException;

	List<ClientMasterVO> getRHSClientList(String var1) throws CMSException;

	List<CountryMasterVO> getLHSCountryList(String var1);

	List<CountryMasterVO> getRHSCountryList(String var1);
}