package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientGroupMasterVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ClientGroupDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ClientGroupDAO");
	private static final String searchBO = "CGMaster.searchBO";
	private static final String SEARCH_BO_COUNT = "CGMaster.searchBO_count";
	private static final String addNewBO = "CGMaster.addNewBO";
	private static final String getBOInfo = "CGMaster.getBOInfo";
	private static final String updateBo = "CGMaster.updateBo";
	private static final String isExist = "CGMaster.isExist";
	private static final String searchGroupByName = "CGMaster.searchGroupByName";
	private static final String deActivateGroup = "CGMaster.deActivateGroup";
	private static final String deAssociateClient = "CGMaster.deAssociateClient";

	public Object addClientGroup(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In addClientGroup method DAO");

		try {
			Object insertedObject = this.insert("CGMaster.addNewBO", clientGroupMasterVO);
			return insertedObject;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ClientGroupMasterVO getCGInfo(int branchId) throws CMSException {
		this.logger.debug("In getCGInfo method DAO");
		ClientGroupMasterVO vo = null;

		try {
			vo = (ClientGroupMasterVO) this.queryForObject("CGMaster.getBOInfo", branchId);
			return vo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateCG(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In updateCG method DAO");
		boolean var2 = false;

		try {
			int updated = this.update("CGMaster.updateBo", clientGroupMasterVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int isExist(String branchOffice) throws CMSException {
		this.logger.debug("In isExist method DAO");
		boolean var2 = false;

		try {
			int count = (Integer) ((Integer) this.queryForObject("CGMaster.isExist", branchOffice));
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientGroupMasterVO> searchClientGroup(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In searchClientGroup method DAO");
		new ArrayList();

		try {
			List<ClientGroupMasterVO> mv = this.queryForList("CGMaster.searchBO", clientGroupMasterVO);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchCGCount(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In searchCGCount method DAO");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("CGMaster.searchBO_count", clientGroupMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ClientGroupMasterVO searchGroupByName(String currencyCode) throws CMSException {
		this.logger.debug("searchGroupByName DAO");
		ClientGroupMasterVO cmvo = null;

		try {
			cmvo = (ClientGroupMasterVO) this.queryForObject("CGMaster.searchGroupByName", currencyCode);
			return cmvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deactivateGroup(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In deactivateGroup method DAO");
		boolean var2 = false;

		try {
			int updated = this.update("CGMaster.deActivateGroup", clientGroupMasterVO);
			if (updated != 0) {
				this.deactivateAssociateClient(clientGroupMasterVO);
				this.logger.debug("client DeAssociated ");
			} else {
				this.logger.debug("client not DeAssociated ");
			}

			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deactivateAssociateClient(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In deactivateAssociateClient method DAO");
		boolean var2 = false;

		try {
			int updated = this.update("CGMaster.deAssociateClient", clientGroupMasterVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}