package com.worldcheck.atlas.bl.masters;

import com.integrascreen.orders.REMasterVO;
import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.bl.interfaces.IREMaster;
import com.worldcheck.atlas.dao.masters.REMultiActionDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class REMultiActionManager implements IREMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.REMultiActionManager");
	REMultiActionDAO reMultiActionDAO = null;

	public void setReMultiActionDAO(REMultiActionDAO reMultiActionDAO) {
		this.reMultiActionDAO = reMultiActionDAO;
	}

	public List<ResearchElementMasterVO> searchRE(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		try {
			this.logger.debug("Before ..start :: " + researchElementMasterVO.getStart() + " limit : "
					+ researchElementMasterVO.getLimit());
			if (researchElementMasterVO.getStart() >= 0 && researchElementMasterVO.getLimit() >= 0) {
				researchElementMasterVO
						.setLimit(researchElementMasterVO.getLimit() + researchElementMasterVO.getStart());
				researchElementMasterVO.setStart(researchElementMasterVO.getStart() + 1);
			}

			this.logger.debug("After .. start :: " + researchElementMasterVO.getStart() + " limit : "
					+ researchElementMasterVO.getLimit());
			return this.reMultiActionDAO.searchRE(researchElementMasterVO);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int searchRECount(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		return this.reMultiActionDAO.searchRECount(researchElementMasterVO);
	}

	public String updateREStatus(String rEMasterId, int statusVal, String userName) throws CMSException {
		return this.reMultiActionDAO.updateREStatus(rEMasterId, statusVal, userName);
	}

	public String addResearchElement(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		int reId = this.reMultiActionDAO.addResearchElement(researchElementMasterVO);
		researchElementMasterVO.setrEMasterId(reId);
		String message = "";

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("RE");
			upMasterVO.setUpdateType("Insert");
			REMasterVO masterObject = new REMasterVO();
			this.logger.debug("Re Code/id :: " + researchElementMasterVO.getrEMasterId() + " :: RE name :: "
					+ researchElementMasterVO.getResearchElementName() + " Subject Type :: "
					+ researchElementMasterVO.getReEntityTypeId() + " Status :: "
					+ researchElementMasterVO.getResearchElementStatus());
			masterObject.setCode(researchElementMasterVO.getrEMasterId() + "");
			masterObject.setDescription(researchElementMasterVO.getResearchElementName());
			masterObject.setSubjectType(researchElementMasterVO.getReEntityTypeId());
			masterObject.setStatus("A");
			upMasterVO.setREMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				message = "success";
			} else {
				this.reMultiActionDAO.deleteREDetails(researchElementMasterVO.getrEMasterId() + "");
				message = isisResponseVO.getResponseVO().getMessage() + " for "
						+ researchElementMasterVO.getResearchElementName();
			}

			return message;
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public ResearchElementMasterVO getREInfo(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		return this.reMultiActionDAO.getREInfo(researchElementMasterVO);
	}

	public String addResearchElementGroup(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		int reId = this.reMultiActionDAO.addResearchElementGroup(researchElementMasterVO);
		researchElementMasterVO.setrEMasterId(reId);
		String message = "";

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("RE");
			upMasterVO.setUpdateType("Insert");
			REMasterVO masterObject = new REMasterVO();
			this.logger.debug("Re Code/id :: " + researchElementMasterVO.getrEMasterId() + " :: RE name :: "
					+ researchElementMasterVO.getResearchElementName() + " Subject Type :: "
					+ researchElementMasterVO.getReEntityTypeId() + " Status :: "
					+ researchElementMasterVO.getResearchElementStatus());
			masterObject.setCode(researchElementMasterVO.getrEMasterId() + "");
			masterObject.setDescription(researchElementMasterVO.getResearchElementName());
			masterObject.setSubjectType(researchElementMasterVO.getReEntityTypeId());
			masterObject.setStatus("A");
			upMasterVO.setREMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				message = "success";
			} else {
				this.reMultiActionDAO.deleteREDetails(researchElementMasterVO.getrEMasterId() + "");
				message = isisResponseVO.getResponseVO().getMessage() + " for "
						+ researchElementMasterVO.getResearchElementName();
			}

			return message;
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public int getGroupId(String groupName) throws CMSException {
		return this.reMultiActionDAO.getGroupId(groupName);
	}

	public String updateRE(ResearchElementMasterVO researchElementMasterVO, int groupId) throws CMSException {
		return this.reMultiActionDAO.updateRE(researchElementMasterVO, groupId);
	}

	public List getGroupInfo(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		return this.reMultiActionDAO.getGroupInfo(researchElementMasterVO);
	}

	public boolean isGroupNameUnique(HashMap<String, Object> paramMap) throws CMSException {
		int countryCodeCount = this.reMultiActionDAO.isGroupNameUnique(paramMap);
		return countryCodeCount > 0;
	}

	public int canREsDeactivated(String reIds) throws CMSException {
		return this.reMultiActionDAO.canREsDeactivated(reIds);
	}

	public String isSubjectTypeREUnique(String reInfo) throws CMSException {
		return this.reMultiActionDAO.isSubjectTypeREUnique(reInfo);
	}

	public String isWIPCaseImpacted(String reInfo) throws CMSException {
		return this.reMultiActionDAO.isWIPCaseImpacted(reInfo);
	}

	public List<ResearchElementMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		return this.reMultiActionDAO.getReport(excelParamMap);
	}
}