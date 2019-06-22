package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.IndustryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import java.util.List;

public interface IIndustryMaster {
	void setIndustryDAO(IndustryDAO var1);

	List<IndustryMasterVO> getIndustryGrid(IndustryMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	String changeIndustryStatus(List var1, String var2, String var3) throws CMSException;

	IndustryMasterVO getIndustryInfo(String var1) throws CMSException;

	String updateIndustry(IndustryMasterVO var1) throws CMSException;

	String addIndustry(IndustryMasterVO var1) throws CMSException;

	int getIndustryGridCount(IndustryMasterVO var1) throws CMSException;

	boolean isExistIndustry(String var1) throws CMSException;

	IndustryMasterVO checkAssociatedMaster(String var1) throws CMSException;
}