package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ClientRequirementDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ClientRequirementDAO");
	private String insertClientReq = "ClientRequirement.insertClientReq";
	private String searchClientReq = "ClientRequirement.searchClientReq";
	private String searchClientReqCount = "ClientRequirement.searchClientReqCount";
	private String getInsertedData = "ClientRequirement.getInsertedData";
	private String updateHistoryStatus = "ClientRequirement.updateHistoryStatus";
	private String updateRecord = "ClientRequirement.updateRecord";
	private String getGeneralClient = "ClientRequirement.getGeneralClients";
	private String getFilesForCaseDetail = "ClientRequirement.getFilesForCaseDetail";
	private String getFilesForCaseDetailCount = "ClientRequirement.getFilesForCaseDetailCount";
	private String getHistoryDataOfClient = "ClientRequirement.getHistoryDataOfClient";

	public int insertClientReq(ClientRequirmentMasterVO clientRequirmentMasterVO) throws CMSException {
		this.logger.debug(
				"com.worldcheck.atlas.dao.masters.ClientRequirementDAO.insertClientReq(ClientRequirmentMasterVO)");
		boolean var2 = false;

		try {
			int id = (Integer) this.insert(this.insertClientReq, clientRequirmentMasterVO);
			return id;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientRequirmentMasterVO> searchClientReq(ClientRequirmentMasterVO clientRequirmentMasterVO)
			throws CMSException {
		this.logger.debug(" In searchClientReq method");
		List searchList = null;

		try {
			searchList = this.queryForList(this.searchClientReq, clientRequirmentMasterVO);
			return searchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchClientReqCount(ClientRequirmentMasterVO clientRequirmentMasterVO) throws CMSException {
		this.logger.debug("In searchClientReq method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.searchClientReqCount, clientRequirmentMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ClientRequirmentMasterVO getInsertedData(int id) throws CMSException {
		this.logger.debug("In getInsertedData method");
		ClientRequirmentMasterVO crmvo = null;

		try {
			crmvo = (ClientRequirmentMasterVO) this.queryForObject(this.getInsertedData, id);
			return crmvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientRequirmentMasterVO> getHistoryDataOfClient(String ClientCode) throws CMSException {
		this.logger.debug("In getHistoryDataOfClient method");
		ArrayList crmvo = null;

		try {
			crmvo = (ArrayList) this.queryForList(this.getHistoryDataOfClient, ClientCode);
			this.logger.debug("number of record in history:" + crmvo.size());
			return crmvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateHistoryStatus(HashMap hmap) throws CMSException {
		this.logger.debug("In updateHistoryStatus method");
		boolean var2 = false;

		try {
			int updated = this.update(this.updateHistoryStatus, hmap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRecord(ClientRequirmentMasterVO crmvo) throws CMSException {
		this.logger.debug("In updateRecord method");
		boolean var2 = false;

		try {
			int updated = this.update(this.updateRecord, crmvo);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientMasterVO> getGeneralClientList() throws CMSException {
		this.logger.debug("In getGeneralClientList method");
		List generalClientList = null;

		try {
			generalClientList = this.queryForList(this.getGeneralClient);
			return generalClientList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ClientRequirmentMasterVO> getFilesForCaseDetail(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetail method");
		List searchList = null;

		try {
			searchList = this.queryForList(this.getFilesForCaseDetail, hmap);
			return searchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getFilesForCaseDetailCount(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetailCount");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.getFilesForCaseDetailCount, hmap);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}