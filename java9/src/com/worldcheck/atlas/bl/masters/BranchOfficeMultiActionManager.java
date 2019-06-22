package com.worldcheck.atlas.bl.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IBranchOfficeMaster;
import com.worldcheck.atlas.dao.masters.BranchOfficeMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class BranchOfficeMultiActionManager implements IBranchOfficeMaster {
	private static final String comma = ",";
	private final String OFFICE_ID = "officeId";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.masters.BranchOfficeMultiActionManager");
	BranchOfficeMultiActionDAO branchOfficeMultiActionDAO = null;

	public void setBranchOfficeMultiActionDAO(BranchOfficeMultiActionDAO branchOfficeMultiActionDAO)
			throws CMSException {
		this.logger.debug("In setBranchOfficeMultiActionDAO method");
		this.branchOfficeMultiActionDAO = branchOfficeMultiActionDAO;
	}

	public List<BranchOfficeMasterVO> searchBO(BranchOfficeMasterVO branchOfficeMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("In searchBO method");
		branchOfficeMasterVO.setStart(new Integer(start + 1));
		branchOfficeMasterVO.setLimit(start + limit);
		branchOfficeMasterVO.setSortColumnName(sortColumnName);
		branchOfficeMasterVO.setSortType(sortType);
		return this.branchOfficeMultiActionDAO.searchBO(branchOfficeMasterVO);
	}

	public int searchBoCount(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In searchBoCount method");
		return this.branchOfficeMultiActionDAO.searchBoCount(branchOfficeMasterVO);
	}

	public void deActivatebranchOffice(String branchOfficeCode, String ostatus, String updatedBy) throws CMSException {
		this.logger.debug("In deActivatebranchOffice method");
		this.logger.debug(branchOfficeCode);
		String[] BOCodes = branchOfficeCode.split(",");
		String[] arr$ = BOCodes;
		int len$ = BOCodes.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			String boCode = arr$[i$];
			this.branchOfficeMultiActionDAO.deActivatebranchOffice(boCode, ostatus, updatedBy);
		}

	}

	public void addBranchOffice(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In addBranchOffice method");

		try {
			Integer insertedId = (Integer) this.branchOfficeMultiActionDAO.addBranchOffice(branchOfficeMasterVO);
			this.logger.debug("Inserted branch Id is :" + insertedId);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public BranchOfficeMasterVO getBOInfo(int branchId) throws CMSException {
		this.logger.debug("in getBOInfo method");
		BranchOfficeMasterVO vo = this.branchOfficeMultiActionDAO.getBOInfo(branchId);
		return vo;
	}

	public void updateBo(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In updateBo method");

		try {
			this.branchOfficeMultiActionDAO.updateBo(branchOfficeMasterVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isExist(String branchOffice) throws CMSException {
		this.logger.debug("In isExist method");
		return this.branchOfficeMultiActionDAO.isExist(branchOffice);
	}

	public BranchOfficeMasterVO checkAssociatedtoOffice(String officeId) throws CMSException {
		this.logger.debug("In checkAssociatedWIPCRN");
		BranchOfficeMasterVO bomvo = null;

		try {
			HashMap<String, String> hmap = new HashMap();
			this.getClass();
			hmap.put("officeId", officeId);
			bomvo = this.branchOfficeMultiActionDAO.checkAssociatedtoOffice(hmap);
			return bomvo;
		} catch (CMSException var4) {
			throw var4;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<BranchOfficeMasterVO> getUserAndSubordinateOffices(HttpServletRequest request) throws CMSException {
		this.logger.debug("in  getUserAndSubordinateOffices");
		List officeList = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("logged in user :: " + userBean.getUserName());
			officeList = this.branchOfficeMultiActionDAO.getUserAndSubordinateOffices(userBean.getUserName());
			return officeList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<BranchOfficeMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		return this.branchOfficeMultiActionDAO.getReport(excelParamMap);
	}
}