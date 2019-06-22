package com.worldcheck.atlas.bl.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IClientGroup;
import com.worldcheck.atlas.dao.masters.ClientGroupDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientGroupMasterVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class ClientGroupManager implements IClientGroup {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.ClientGroupManager");
	ClientGroupDAO clientGroupDAO = null;

	public void setClientGroupDAO(ClientGroupDAO clientGroupDAO) {
		this.clientGroupDAO = clientGroupDAO;
	}

	public Object addGroup(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		try {
			return this.clientGroupDAO.addClientGroup(clientGroupMasterVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<ClientGroupMasterVO> searchClientGroup(ClientGroupMasterVO clientGroupMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("In searchClientGroup");
		clientGroupMasterVO.setStart(new Integer(start + 1));
		clientGroupMasterVO.setLimit(start + limit);
		clientGroupMasterVO.setSortColumnName(sortColumnName);
		clientGroupMasterVO.setSortType(sortType);
		return this.clientGroupDAO.searchClientGroup(clientGroupMasterVO);
	}

	public int searchCGCount(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In searchCGCount method");
		return this.clientGroupDAO.searchCGCount(clientGroupMasterVO);
	}

	public int isExist(String branchOffice) throws CMSException {
		this.logger.debug("In isExist method");
		return this.clientGroupDAO.isExist(branchOffice);
	}

	public ClientGroupMasterVO searchGroupByName(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("IN searchGroupByName");
		return this.clientGroupDAO.searchGroupByName(clientGroupMasterVO.getClientGroupName());
	}

	public Object updateGroup(ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In updateGroup method");

		try {
			return this.clientGroupDAO.updateCG(clientGroupMasterVO);
		} catch (CMSException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public Object deactivateGroup(String currencyCodes, HttpServletRequest request,
			ClientGroupMasterVO clientGroupMasterVO) throws CMSException {
		this.logger.debug("In deactivateGroup method");
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		clientGroupMasterVO.setUpdateBy(userBean.getUserName());
		clientGroupMasterVO.setClientGroupId(currencyCodes);

		try {
			return this.clientGroupDAO.deactivateGroup(clientGroupMasterVO);
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}