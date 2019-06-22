package com.worldcheck.atlas.bl.masters;

import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.bl.interfaces.IIndustryMaster;
import com.worldcheck.atlas.dao.masters.IndustryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import java.util.List;

public class IndustryManager implements IIndustryMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.IndustryManager");
	private IndustryDAO industryDAO;

	public void setIndustryDAO(IndustryDAO industryDAO) {
		this.industryDAO = industryDAO;
	}

	public List<IndustryMasterVO> getIndustryGrid(IndustryMasterVO industryMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		industryMasterVO.setStart(start + 1);
		industryMasterVO.setLimit(start + limit);
		industryMasterVO.setSortColumnName(sortColumnName);
		industryMasterVO.setSortType(sortType);
		List<IndustryMasterVO> industryList = this.industryDAO.getIndustryGrid(industryMasterVO);
		return industryList;
	}

	public String changeIndustryStatus(List industryCodeList, String industryStatus, String updatedBy)
			throws CMSException {
		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Industry");
			upMasterVO.setUpdateType("Update");
			com.integrascreen.orders.IndustryMasterVO masterObject = new com.integrascreen.orders.IndustryMasterVO();
			IndustryMasterVO industryMasterVO = this.industryDAO.getIndustryInfo((String) industryCodeList.get(0));
			this.logger.debug("IndustryCode::" + industryMasterVO.getIndustryCode() + "Industry::"
					+ industryMasterVO.getIndustry() + "Industry Status::" + industryMasterVO.getStatus());
			masterObject.setCode(industryMasterVO.getIndustryCode());
			masterObject.setDescription(industryMasterVO.getIndustry());
			masterObject.setStatus(industryMasterVO.getStatus().equals("1") ? "D" : "A");
			this.logger.debug(masterObject.getStatus());
			upMasterVO.setIndustryMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.industryDAO.changeIndustryStatus(industryCodeList, industryStatus, updatedBy) + "#success"
					: isisResponseVO.getResponseVO().getMessage() + "#failure";
		} catch (CMSException var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public IndustryMasterVO getIndustryInfo(String industryId) throws CMSException {
		IndustryMasterVO industryMasterVO = this.industryDAO.getIndustryInfo(industryId);
		return industryMasterVO;
	}

	public String updateIndustry(IndustryMasterVO industryMasterVO) throws CMSException {
		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Industry");
			upMasterVO.setUpdateType("Update");
			com.integrascreen.orders.IndustryMasterVO masterObject = new com.integrascreen.orders.IndustryMasterVO();
			this.logger.debug("IndustryCode::" + industryMasterVO.getIndustryCode() + "Industry::"
					+ industryMasterVO.getIndustry() + "Industry Status::" + industryMasterVO.getStatus());
			masterObject.setCode(industryMasterVO.getIndustryCode());
			masterObject.setDescription(industryMasterVO.getIndustry());
			masterObject.setStatus(industryMasterVO.getStatus().equals("0") ? "D" : "A");
			upMasterVO.setIndustryMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			return isisResponseVO.isSuccess()
					? this.industryDAO.updateIndustry(industryMasterVO) + "#success"
					: isisResponseVO.getResponseVO().getMessage() + "#failure";
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String addIndustry(IndustryMasterVO industryMasterVO) throws CMSException {
		String PREPEND_CODE = "MBT00";

		try {
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Industry");
			upMasterVO.setUpdateType("Insert");
			com.integrascreen.orders.IndustryMasterVO masterObject = new com.integrascreen.orders.IndustryMasterVO();
			int industryCode = this.industryDAO.addIndustry(industryMasterVO);
			this.logger.debug("IndustryCode::" + PREPEND_CODE + industryCode);
			industryMasterVO.setIndustryCode(PREPEND_CODE + industryCode);
			this.logger.debug("IndustryCode::" + industryMasterVO.getIndustryCode() + "Industry::"
					+ industryMasterVO.getIndustry() + "Industry Status::" + industryMasterVO.getStatus());
			masterObject.setCode(industryMasterVO.getIndustryCode());
			masterObject.setDescription(industryMasterVO.getIndustry());
			masterObject.setStatus("A");
			upMasterVO.setIndustryMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				return industryMasterVO.getIndustryCode() + "#success";
			} else {
				this.industryDAO.deleteIndustry(industryMasterVO.getIndustryCode());
				return isisResponseVO.getResponseVO().getMessage() + "#failure";
			}
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public int getIndustryGridCount(IndustryMasterVO industryMasterVO) throws CMSException {
		return this.industryDAO.getIndustryGridCount(industryMasterVO);
	}

	public boolean isExistIndustry(String industry) throws CMSException {
		int industryCount = this.industryDAO.getCountExistIndustry(industry);
		return industryCount > 0;
	}

	public IndustryMasterVO checkAssociatedMaster(String industryCode) throws CMSException {
		return this.industryDAO.checkAssociatedMaster(industryCode);
	}
}