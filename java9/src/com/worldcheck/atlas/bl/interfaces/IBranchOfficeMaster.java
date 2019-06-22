package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.BranchOfficeMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface IBranchOfficeMaster {
	void setBranchOfficeMultiActionDAO(BranchOfficeMultiActionDAO var1) throws CMSException;

	List<BranchOfficeMasterVO> searchBO(BranchOfficeMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int searchBoCount(BranchOfficeMasterVO var1) throws CMSException;

	void deActivatebranchOffice(String var1, String var2, String var3) throws CMSException;

	void addBranchOffice(BranchOfficeMasterVO var1) throws CMSException;

	BranchOfficeMasterVO getBOInfo(int var1) throws CMSException;

	void updateBo(BranchOfficeMasterVO var1) throws CMSException;

	int isExist(String var1) throws CMSException;

	BranchOfficeMasterVO checkAssociatedtoOffice(String var1) throws CMSException;

	List<BranchOfficeMasterVO> getUserAndSubordinateOffices(HttpServletRequest var1) throws CMSException;

	List<BranchOfficeMasterVO> getReport(Map<String, Object> var1) throws CMSException;
}