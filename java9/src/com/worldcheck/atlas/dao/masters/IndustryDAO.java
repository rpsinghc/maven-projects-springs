package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class IndustryDAO extends SqlMapClientTemplate {
	private static final String INDUSTRY_MASTER_SEARCH_EXISTING_INDUSTRY = "Industry_Master.searchExistingIndustry";
	private String SUCCESS = "success";
	private String ADD_INDUSTRY = "Industry_Master.addIndustry";
	private String DELETE_INDUSTRY = "Industry_Master.deleteIndustry";
	private String DEACTIVATE_INDUSTRY = "Industry_Master.deActivate";
	private String UPDATE_INDUSTRY = "Industry_Master.updateIndustry";
	private String LIST_FOR_INDUSTRIES = "Industry_Master.getIndustryGridList";
	private String SELECT_INDUSTRY = "Industry_Master.getIndustryInfo";
	private String COUNT_INDUSTRY = "Industry_Master.getIndustryGridListCount";
	private String INDUSTRY_CODE_LIST = "industryCodeList";
	private String INDUSTRY_STAUS = "industryStatus";
	private String UPDATED_BY = "updatedBy";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.IndustryDAO");

	public List<IndustryMasterVO> getIndustryGrid(IndustryMasterVO industryMasterVO) throws CMSException {
		try {
			new ArrayList();
			List<IndustryMasterVO> industryGridList = this.queryForList(this.LIST_FOR_INDUSTRIES, industryMasterVO);
			return industryGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getIndustryGridCount(IndustryMasterVO industryMasterVO) throws CMSException {
		this.logger.debug("Inside Industry Dao to return Total no of Industry.");

		try {
			return (Integer) this.queryForObject(this.COUNT_INDUSTRY, industryMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int changeIndustryStatus(List industryCodeList, String industryStatus, String updatedBy)
			throws CMSException {
		HashMap paramMap = new HashMap();

		try {
			this.logger.debug("Inside the Industry Dao");
			paramMap.put(this.INDUSTRY_CODE_LIST, industryCodeList);
			paramMap.put(this.INDUSTRY_STAUS, industryStatus);
			paramMap.put(this.UPDATED_BY, updatedBy);
			int count = this.update(this.DEACTIVATE_INDUSTRY, paramMap);
			return count;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public IndustryMasterVO getIndustryInfo(String industryId) throws CMSException {
		try {
			this.logger.debug("Update Industry.... from IndustryDAO");
			IndustryMasterVO industryMasterVO = (IndustryMasterVO) this.queryForObject(this.SELECT_INDUSTRY,
					industryId);
			return industryMasterVO;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateIndustry(IndustryMasterVO industryMasterVO) throws CMSException {
		try {
			this.logger.debug("Update Industry.... from IndustryDAO");
			int count = Integer.valueOf(this.update(this.UPDATE_INDUSTRY, industryMasterVO));
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int addIndustry(IndustryMasterVO industryMasterVO) throws CMSException {
		try {
			this.logger.debug("Insert new Industry.... from IndustryDAO");
			int count = (Integer) this.insert(this.ADD_INDUSTRY, industryMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountExistIndustry(String industry) throws CMSException {
		try {
			int count = Integer
					.parseInt(this.queryForObject("Industry_Master.searchExistingIndustry", industry).toString());
			this.logger.debug("Industry Name Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public IndustryMasterVO checkAssociatedMaster(String industryCode) throws CMSException {
		this.logger.debug("inside IndustryDAO :: checkAssociatedMaster IndustryCode::" + industryCode);

		try {
			return (IndustryMasterVO) this.queryForObject("Industry_Master.checkAssociatedMaster", industryCode);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteIndustry(String industryCode) throws CMSException {
		try {
			this.logger.debug("Delete new Industry.... from IndustryDAO");
			int count = Integer.valueOf(this.delete(this.DELETE_INDUSTRY, industryCode));
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}