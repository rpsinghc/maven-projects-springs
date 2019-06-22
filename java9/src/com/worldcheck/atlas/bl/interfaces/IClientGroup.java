package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.ClientGroupDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ClientGroupMasterVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface IClientGroup {
	void setClientGroupDAO(ClientGroupDAO var1);

	int searchCGCount(ClientGroupMasterVO var1) throws CMSException;

	List<ClientGroupMasterVO> searchClientGroup(ClientGroupMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int isExist(String var1) throws CMSException;

	ClientGroupMasterVO searchGroupByName(ClientGroupMasterVO var1) throws CMSException;

	Object updateGroup(ClientGroupMasterVO var1) throws CMSException;

	Object addGroup(ClientGroupMasterVO var1) throws CMSException;

	Object deactivateGroup(String var1, HttpServletRequest var2, ClientGroupMasterVO var3) throws CMSException;
}