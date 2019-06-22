package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.IClientMaster;
import com.worldcheck.atlas.dao.masters.ClientMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientContactVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientMultiActionManager implements IClientMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.ClientMultiActionManager");
	ClientMultiActionDAO clientMultiActionDAO = null;

	public void setClientMultiActionDAO(ClientMultiActionDAO clientMultiActionDAO) {
		this.clientMultiActionDAO = clientMultiActionDAO;
	}

	public List<ClientMasterVO> selectClientInfo(ClientMasterVO clientMasterVO) throws CMSException {
		return this.clientMultiActionDAO.selectClientInfo(clientMasterVO);
	}

	public List<ClientMasterVO> searchClient(ClientMasterVO clientMasterVO) throws CMSException {
		return this.clientMultiActionDAO.searchClient(clientMasterVO);
	}

	public int searchClientCount(ClientMasterVO clientMasterVO) throws CMSException {
		return this.clientMultiActionDAO.searchClientCount(clientMasterVO);
	}

	public void addClient(ClientMasterVO clientMasterVO) throws CMSException {
		this.clientMultiActionDAO.addNewClient(clientMasterVO);
	}

	public void deactivateClient(String clientCodeValue) throws CMSException {
		this.logger.debug("In Deactivate manager");
		this.logger.debug(clientCodeValue);
		clientCodeValue = clientCodeValue.substring(0, clientCodeValue.length() - 1);
		this.logger.debug(clientCodeValue);
	}

	public ClientMasterVO getClientInfo(int idVal) throws CMSException {
		ClientMasterVO vo = this.clientMultiActionDAO.getClientInfo(idVal);
		return vo;
	}

	public void updateClient(ClientMasterVO clientMasterVO) throws CMSException {
		this.clientMultiActionDAO.updateNewClient(clientMasterVO);
	}

	public String getClientCode(String clientName) throws CMSException {
		clientName = clientName.trim().toUpperCase();
		String val = this.clientMultiActionDAO.getClientCode(clientName);
		int code = Integer.parseInt(val);
		String pcode = "";

		try {
			++code;
			this.logger.debug("ClientMultiActionController.main() code >>>  " + code);
			pcode = String.valueOf(code);
			int len = pcode.length();
			if (len == 1) {
				pcode = clientName.charAt(0) + "00" + pcode;
			} else if (len == 2) {
				pcode = clientName.charAt(0) + "0" + pcode;
			} else {
				pcode = clientName.charAt(0) + pcode;
			}

			this.logger.debug("ClientMultiActionController.main()" + pcode);
			return pcode;
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void addClientContact(ClientContactVO clientContactVO) throws CMSException {
		this.clientMultiActionDAO.addClientContact(clientContactVO);
	}

	public List<ClientContactVO> getContactDetail(String clientCode) throws CMSException {
		new ArrayList();
		List<ClientContactVO> listvo = this.clientMultiActionDAO.getClientContact(clientCode);
		return listvo;
	}

	public void delete(String clientCode) throws CMSException {
		this.clientMultiActionDAO.deleteContact(clientCode);
	}

	public String checkClientName(String clientName) throws CMSException {
		String clientCode = "";
		clientCode = this.clientMultiActionDAO.checkClientName(clientName);
		return clientCode;
	}

	public List<CountryMasterVO> getTopCountry() throws CMSException {
		List<CountryMasterVO> list = this.clientMultiActionDAO.getTopTen();
		return list;
	}

	public List<CountryMasterVO> getTopCountryForRiskHBD() throws CMSException {
		List<CountryMasterVO> list = this.clientMultiActionDAO.getTopTenForRiskHBD();
		return list;
	}

	public void updateDeactiveClient(String clientCode) throws CMSException {
		try {
			this.clientMultiActionDAO.updateDeactiveClient(clientCode);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<ClientMasterVO> searchClientForExport(Map<String, Object> excelParamMap) throws CMSException {
		return this.clientMultiActionDAO.searchClientForExport(excelParamMap);
	}

	public void updateClientStatus(ClientMasterVO clientMasterVO) throws CMSException {
		try {
			clientMasterVO.setClientMasterId(
					clientMasterVO.getClientMasterId().substring(0, clientMasterVO.getClientMasterId().length() - 1));
			this.clientMultiActionDAO.updateClientStatus(clientMasterVO);
			this.logger.info("client status for client " + clientMasterVO.getClientMasterId() + " updated to "
					+ clientMasterVO.getClientStatus());
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}