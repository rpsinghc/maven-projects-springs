package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.ClientMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ClientContactVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.List;
import java.util.Map;

public interface IClientMaster {
	void setClientMultiActionDAO(ClientMultiActionDAO var1);

	List<ClientMasterVO> selectClientInfo(ClientMasterVO var1) throws CMSException;

	List<ClientMasterVO> searchClient(ClientMasterVO var1) throws CMSException;

	int searchClientCount(ClientMasterVO var1) throws CMSException;

	void addClient(ClientMasterVO var1) throws CMSException;

	void deactivateClient(String var1) throws CMSException;

	ClientMasterVO getClientInfo(int var1) throws CMSException;

	void updateClient(ClientMasterVO var1) throws CMSException;

	String getClientCode(String var1) throws CMSException;

	void addClientContact(ClientContactVO var1) throws CMSException;

	List<ClientContactVO> getContactDetail(String var1) throws CMSException;

	void delete(String var1) throws CMSException;

	String checkClientName(String var1) throws CMSException;

	List<CountryMasterVO> getTopCountry() throws CMSException;

	List<CountryMasterVO> getTopCountryForRiskHBD() throws CMSException;

	void updateDeactiveClient(String var1) throws CMSException;

	List<ClientMasterVO> searchClientForExport(Map<String, Object> var1) throws CMSException;

	void updateClientStatus(ClientMasterVO var1) throws CMSException;
}