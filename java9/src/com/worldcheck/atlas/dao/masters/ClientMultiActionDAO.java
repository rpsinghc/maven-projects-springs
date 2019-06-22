package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientContactVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ClientMultiActionDAO extends SqlMapClientTemplate {
	private static final String CLIENT_MASTER_UPDATE_DEACTIVE_CLIENT = "ClientMaster.updateDeactiveClient";
	private static final String CLIENT_MASTER_GET_TOP_TEN = "ClientMaster.getTopTen";
	private static final String CLIENT_MASTER_GET_TOP_TEN_HBD = "ClientMaster.getTopTenForHBD";
	private static final String CLIENT_MASTER_CHECK_CLIENT_NAME = "ClientMaster.checkClientName";
	private static final String CLIENT_MASTER_DELETE_CONTACT = "ClientMaster.deleteContact";
	private static final String CLIENT_MASTER_GET_CONTACT_DETAIL = "ClientMaster.getContactDetail";
	private static final String CLIENT_MASTER_ADD_CLIENT_CONTACT = "ClientMaster.addClientContact";
	private static final String CLIENT_MASTER_GET_CLIENT_CODE = "ClientMaster.getClientCode";
	private static final String CLIENT_MASTER_UPDATE_CLIENT = "ClientMaster.updateClient";
	private static final String CLIENT_MASTER_GET_CLIENT_INFO = "ClientMaster.getClientInfo";
	private static final String CLIENT_MASTER_ADD_NEW_CLIENT = "ClientMaster.addNewClient";
	private static final String CLIENT_MASTER_SEARCH_CLIENT = "ClientMaster.searchClient";
	private static final String CLIENT_MASTER_SELECT_CLIENT_INFO = "ClientMaster.selectClientInfo";
	private static final String CLIENT_MASTER_SEARCH_CLIENT_COUNT = "ClientMaster.searchClientCount";
	private static final String CLIENT_MASTER_SEARCH_CLIENT_EXPORT = "ClientMaster.searchClientExport";
	private static final String CLIENT_MASTER_UPDATE_STATUS_CLIENT = "ClientMaster.updateClientStatus";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ClientMultiActionDAO");

	public List<ClientMasterVO> selectClientInfo(ClientMasterVO clientMasterVO) throws CMSException {
		new ArrayList();
		this.logger.debug("Inside the Client Select All Dao Now" + clientMasterVO.getIsSubreportRequired());

		try {
			List<ClientMasterVO> cmv = this.queryForList("ClientMaster.selectClientInfo", clientMasterVO);
			return cmv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ClientMasterVO> searchClient(ClientMasterVO clientMasterVO) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList("ClientMaster.searchClient", clientMasterVO);
			this.logger.debug("mv " + mv.size());
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int searchClientCount(ClientMasterVO clientMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("ClientMaster.searchClientCount", clientMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String addNewClient(ClientMasterVO clientMasterVO) throws CMSException {
		String clientMasterId = "";

		try {
			clientMasterId = (String) this.insert("ClientMaster.addNewClient", clientMasterVO);
			return clientMasterId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ClientMasterVO getClientInfo(int clientMasterId) throws CMSException {
		ClientMasterVO vo = null;

		try {
			vo = (ClientMasterVO) this.queryForObject("ClientMaster.getClientInfo", clientMasterId);
			return vo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateNewClient(ClientMasterVO clientMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("ClientMaster.updateClient", clientMasterVO);
			this.logger.debug(" number of records updated " + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getClientCode(String compName) throws CMSException {
		String compCode = "";

		try {
			char ch = compName.charAt(0);
			String compAlphabet = String.valueOf(ch);
			compCode = (String) this.queryForObject("ClientMaster.getClientCode", compAlphabet);
			this.logger.debug("comp code " + compCode);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("ClientMultiActionDAO.getClientCode()" + compCode);
		return compCode;
	}

	public int addClientContact(ClientContactVO clientContactVO) throws CMSException {
		boolean var2 = false;

		try {
			int clientContactId = (Integer) this.insert("ClientMaster.addClientContact", clientContactVO);
			this.logger.debug("client contact id " + clientContactId);
			return clientContactId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientContactVO> getClientContact(String clientCode) throws CMSException {
		new ArrayList();

		try {
			List<ClientContactVO> listvo = this.queryForList("ClientMaster.getContactDetail", clientCode);
			return listvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteContact(String clientCode) throws CMSException {
		boolean var2 = false;

		try {
			int countRowsDeleted = this.delete("ClientMaster.deleteContact", clientCode);
			return countRowsDeleted;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String checkClientName(String clientName) throws CMSException {
		String clientCode = "";

		try {
			clientCode = (String) this.queryForObject("ClientMaster.checkClientName", clientName);
			return clientCode;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<CountryMasterVO> getTopTen() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ClientMaster.getTopTen");
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<CountryMasterVO> getTopTenForRiskHBD() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ClientMaster.getTopTenForHBD");
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int updateDeactiveClient(String clientCode) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("ClientMaster.updateDeactiveClient", clientCode);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ClientMasterVO> searchClientForExport(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList("ClientMaster.searchClientExport", excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateClientStatus(ClientMasterVO clientMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = this.update("ClientMaster.updateClientStatus", clientMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}