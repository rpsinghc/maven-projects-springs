package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class BranchOfficeMultiActionDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.dao.masters.BranchOfficeMultiActionDAO");
	private static final String Zero = "0";
	private static final String One = "1";
	private static final String searchBO = "BOMaster.searchBO";
	private static final String SEARCH_BO_COUNT = "BOMaster.searchBO_count";
	private static final String deActivate = "BOMaster.deActivate";
	private static final String activate = "BOMaster.activate";
	private static final String addNewBO = "BOMaster.addNewBO";
	private static final String getBOInfo = "BOMaster.getBOInfo";
	private static final String updateBo = "BOMaster.updateBo";
	private static final String isExist = "BOMaster.isExist";
	private static final String checkAssociatedtoOffice = "BOMaster.checkAssociatedtoOffice";
	private static final String BRANCHOFFICECODE = "branchOfficeCode";
	private static final String UPDATEDBY = "updatedBy";
	private static final String OFFICECAPACITY_GETOFFICES = "OfficeCapacity.getOffices";
	private static final String BRANCH_MASTER_EXPORT_TO_EXCEL = "BOMaster.exportTo_XL";

	public List<BranchOfficeMasterVO> searchBO(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In searchBO method");
		new ArrayList();

		try {
			List<BranchOfficeMasterVO> mv = this.queryForList("BOMaster.searchBO", branchOfficeMasterVO);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchBoCount(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In searchBoCount method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("BOMaster.searchBO_count", branchOfficeMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deActivatebranchOffice(String branchOfficeCode, String ostatus, String updatedBy) throws CMSException {
		this.logger.debug("In deActivatebranchOfficemethod");
		int updated = 0;
		HashMap<String, String> hmap = new HashMap();
		hmap.put("branchOfficeCode", branchOfficeCode);
		hmap.put("updatedBy", updatedBy);

		try {
			this.logger.debug(branchOfficeCode);
			if (ostatus.equalsIgnoreCase("0")) {
				this.logger.debug("inside status 0");
				updated = this.update("BOMaster.deActivate", hmap);
			}

			if (ostatus.equalsIgnoreCase("1")) {
				this.logger.debug("inside status 1");
				updated = this.update("BOMaster.activate", hmap);
			}

			return updated;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public Object addBranchOffice(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In addBranchOffice method");

		try {
			Object insertedObject = this.insert("BOMaster.addNewBO", branchOfficeMasterVO);
			return insertedObject;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public BranchOfficeMasterVO getBOInfo(int branchId) throws CMSException {
		this.logger.debug("In getBOInfo method");
		BranchOfficeMasterVO vo = null;

		try {
			vo = (BranchOfficeMasterVO) this.queryForObject("BOMaster.getBOInfo", branchId);
			return vo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateBo(BranchOfficeMasterVO branchOfficeMasterVO) throws CMSException {
		this.logger.debug("In updateBo method");
		boolean var2 = false;

		try {
			int updated = this.update("BOMaster.updateBo", branchOfficeMasterVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int isExist(String branchOffice) throws CMSException {
		this.logger.debug("In isExist method");
		boolean var2 = false;

		try {
			int count = (Integer) ((Integer) this.queryForObject("BOMaster.isExist", branchOffice));
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public BranchOfficeMasterVO checkAssociatedtoOffice(HashMap hmap) throws CMSException {
		this.logger.debug("In checkAssociatedWIPCRN");
		BranchOfficeMasterVO bomvo = null;

		try {
			bomvo = (BranchOfficeMasterVO) this.queryForObject("BOMaster.checkAssociatedtoOffice", hmap);
			return bomvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<BranchOfficeMasterVO> getUserAndSubordinateOffices(String user) throws CMSException {
		this.logger.debug("In getUserAndSubordinateOffices method with user:: " + user);
		new ArrayList();

		try {
			List<BranchOfficeMasterVO> officeList = this.queryForList("OfficeCapacity.getOffices", user);
			this.logger.debug("officeList size : " + officeList.size());
			return officeList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<BranchOfficeMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList("BOMaster.exportTo_XL", excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}