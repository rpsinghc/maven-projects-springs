package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.REMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ResearchElementGroupMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IREMaster {
	void setReMultiActionDAO(REMultiActionDAO var1);

	List<ResearchElementMasterVO> searchRE(ResearchElementMasterVO var1) throws CMSException;

	int searchRECount(ResearchElementMasterVO var1) throws CMSException;

	String updateREStatus(String var1, int var2, String var3) throws CMSException;

	String addResearchElement(ResearchElementMasterVO var1) throws CMSException;

	ResearchElementMasterVO getREInfo(ResearchElementMasterVO var1) throws CMSException;

	String addResearchElementGroup(ResearchElementMasterVO var1) throws CMSException;

	int getGroupId(String var1) throws CMSException;

	String updateRE(ResearchElementMasterVO var1, int var2) throws CMSException;

	List<ResearchElementGroupMasterVO> getGroupInfo(ResearchElementMasterVO var1) throws CMSException;

	boolean isGroupNameUnique(HashMap<String, Object> var1) throws CMSException;

	int canREsDeactivated(String var1) throws CMSException;

	String isSubjectTypeREUnique(String var1) throws CMSException;

	String isWIPCaseImpacted(String var1) throws CMSException;

	List<ResearchElementMasterVO> getReport(Map<String, Object> var1) throws CMSException;
}