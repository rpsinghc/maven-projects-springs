package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.ClientRequirementDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

public interface IClientRequirement {
	ClientRequirementDAO getClientRequirementDAO();

	void setClientRequirementDAO(ClientRequirementDAO var1);

	List<ClientRequirmentMasterVO> searchClientReq(ClientRequirmentMasterVO var1, int var2, int var3, String var4,
			String var5) throws CMSException;

	int searchClientReqCount(ClientRequirmentMasterVO var1) throws CMSException;

	HashMap<String, ClientRequirmentMasterVO> addClientReq(ClientRequirmentMasterVO var1, File var2, String var3)
			throws CMSException;

	void upLoadClientReq(ClientRequirmentMasterVO var1, HttpServletRequest var2) throws CMSException;

	void deleteClientReq(HttpServletRequest var1, ClientRequirmentMasterVO var2) throws CMSException;

	void getHistoryClientReq(ClientRequirmentMasterVO var1) throws CMSException;

	void removeHistoryClientReq(ClientRequirmentMasterVO var1, HttpServletRequest var2) throws CMSException;

	Properties getproperties() throws CMSException;

	void deleteUserFiles(String var1) throws CMSException;

	void deleteDirectory(File var1) throws CMSException;

	List<ClientMasterVO> getGeneralClients() throws CMSException;

	List<ClientRequirmentMasterVO> getFilesForCaseDetail(HashMap var1) throws CMSException;

	int getFilesForCaseDetailCount(HashMap var1) throws CMSException;
}