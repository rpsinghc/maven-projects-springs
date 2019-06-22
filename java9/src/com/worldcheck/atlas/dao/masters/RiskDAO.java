package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskAttributesMasterVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RisksHistoryVO;
import com.worldcheck.atlas.vo.masters.RisksMapVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class RiskDAO extends SqlMapClientTemplate {
	private static final String RISK_MASTER_SEARCH_EXIST_RISK = "Risk_Master.searchExistRisk";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.RiskDAO");
	private String SUCCESS = "success";
	private String RISK_LIST = "Risk_Master.getRiskGridList";
	private String ACTIVATED_RISK_LIST = "Risk_Master.getRiskGridList";
	private String ACTIVATED_RISK_COUNT = "Risk_Master.getRiskGridListCount";
	private String RISK_DEACTIVATE = "Risk_Master.deActivate";
	private String RISK_INFO = "Risk_Master.getRiskInfo";
	private String RISK_UPDATE = "Risk_Master.updateRisk";
	private String RISK_ADD = "Risk_Master.addRisk";
	private String DELETE_RISK = "Risk_Master.deleteRisk";
	private String RISK_COUNT = "Risk_Master.getRiskGridListCount";
	private String RISK_CODE_LIST = "riskCodeList";
	private String RISK_STAUS = "riskStatus";
	private String UPDATEDBY = "updatedBy";
	private String UPDATERISKPROFILEDATA = "Risk_Master.updateRiskProfileData";
	private String MAPPING_ADD = "Risk_Master.addMapping";
	private String GET_RISK_ATTRIBUTES = "Risk_Master.getRiskAttributes";
	private String ALL_RISK_LIST = "Risk_Master.getAllRisks";
	private String RISK_MAPPING_UPDATE = "Risk_Master.updateMapping";
	private String CHANGE_MAPPING_STATUS = "Risk_Master.changeMappingStatus";
	private String MAPPING_ADD_HISTORY = "Risk_Master.addMappingHistory";
	private String MAPPING_UPDATE_HISTORY = "Risk_Master.updateMappingHistory";
	private String GET_MAPPING = "Risk_Master.getMapping";
	private String ALL_MAPPING_LIST = "Risk_Master.getAllMappings";
	private String CHANGE_HISTORY_STATUS = "Risk_Master.CHANGE_HISTORY_STATUS";
	private String GET_MAPPING_STATUS = "Risk_Master.getMappingStatus";
	private String RISK_ADD_HISTORY = "Risk_Master.addRiskHistory";
	private String RISK_UPDATE_HISTORY = "Risk_Master.RISK_UPDATE_HISTORY";
	private String MAPPING_HISTORY_COUNT = "Risk_Master.getMappingHistoryCount";
	private String GET_RISKID = "Risk_Master.GET_RISKID";
	private String GET_MAPPING_ID = "Risk_Master.GET_MAPPING_ID";
	private String CLIENT_ADD = "Risk_Master.addClient";
	private String REPORT_ADD = "Risk_Master.addReport";
	private String SUB_REPORT_ADD = "Risk_Master.addSubReport";
	private String RE_ADD = "Risk_Master.addRE";
	private String SUBJECT_COUNTRY_ADD = "Risk_Master.addCountry";
	private String DELETE_CLIENT = "Risk_Master.deleteClient";
	private String DELETE_REPORT = "Risk_Master.deleteReport";
	private String DELETE_SUB_REPORT = "Risk_Master.deleteSubReport";
	private String DELETE_RE = "Risk_Master.deleteRE";
	private String DELETE_COUNTRY = "Risk_Master.deleteCountry";
	private String getRiskCategory = "Risk_Master.getRiskCategory";
	private String RISK_CASE_HISTORY = "Risk_Master.getCaseHistoryOfRisk";
	private String ASSOCIATED_SUB_REPORTS = "Risk_Master.getAssociatedSubReports";
	private String RISK_HISTORY_COUNT = "Risk_Master.getCaseHistoryOfRiskCount";
	private String INSERT_RISK_HISTORY = "Risk_Master.insert_risk_history";

	public List<RisksMasterVO> getRiskGrid(RisksMasterVO risksMasterVO) throws CMSException {
		List riskGridList = null;

		try {
			riskGridList = this.queryForList(this.RISK_LIST, risksMasterVO);
			return riskGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<RisksMasterVO> getRisks(RisksMasterVO risksMasterVO) throws CMSException {
		List riskGridList = null;

		try {
			riskGridList = this.queryForList(this.ACTIVATED_RISK_LIST, risksMasterVO);
			return riskGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getAssociatedSubreportTypes(RisksMasterVO risksMasterVO) throws CMSException {
		String subReports = null;

		try {
			subReports = (String) this.queryForObject(this.ASSOCIATED_SUB_REPORTS, risksMasterVO);
			return subReports;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getRiskGridCount(RisksMasterVO risksMasterVO) throws CMSException {
		this.logger.debug("Inside Risk Dao to return Total no of Risks.");

		try {
			return (Integer) this.queryForObject(this.RISK_COUNT, risksMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getRisksCount(RisksMasterVO risksMasterVO) throws CMSException {
		this.logger.debug("Inside Risk Dao to return Total no of activated Risks.");

		try {
			return (Integer) this.queryForObject(this.ACTIVATED_RISK_COUNT, risksMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int changeRiskStatus(String[] riskCodeList, String riskStatus, String updatedBy) throws CMSException {
		HashMap<String, Object> paramMap = new HashMap();
		RisksMasterVO risksMasterVO = null;

		int var10;
		try {
			this.getSqlMapClient().startTransaction();
			this.logger.debug("Inside the Risk Dao");
			paramMap.put(this.RISK_CODE_LIST, riskCodeList);
			paramMap.put(this.RISK_STAUS, riskStatus);
			paramMap.put(this.UPDATEDBY, updatedBy);
			int count = this.update(this.RISK_DEACTIVATE, paramMap);
			this.logger.debug("count:::" + count);
			if (count > 0) {
				for (int i = 0; i < riskCodeList.length; ++i) {
					risksMasterVO = new RisksMasterVO();
					String riskCode = riskCodeList[i];
					this.logger.debug("riskCode:::" + riskCode + " riskCodeList[i]:::" + riskCodeList[i]);
					risksMasterVO.setRiskCode(riskCode);
					if (riskStatus.equals("1")) {
						risksMasterVO.setOldRiskStatus("1");
						risksMasterVO.setNewRiskStatus("0");
					} else {
						risksMasterVO.setOldRiskStatus("0");
						risksMasterVO.setNewRiskStatus("1");
					}

					risksMasterVO.setUpdatedBy(updatedBy);
					risksMasterVO.setAction("Modified");
					risksMasterVO.setOldRiskLabel("");
					risksMasterVO.setNewRiskLabel("");
					risksMasterVO.setNewDisplayOnProfile("");
					risksMasterVO.setOldDisplayOnProfile("");
					risksMasterVO.setOldRemark("");
					risksMasterVO.setNewRemark("");
					risksMasterVO.setCountryBreakdown("");
					this.insert(this.INSERT_RISK_HISTORY, risksMasterVO);
				}
			}

			this.getSqlMapClient().commitTransaction();
			var10 = count;
		} catch (DataAccessException var18) {
			throw new CMSException(this.logger, var18);
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var17) {
				throw new CMSException(this.logger, var17);
			}
		}

		return var10;
	}

	public List<RisksMasterVO> getRiskCaseHistory(RisksMasterVO risksMasterVO) throws CMSException {
		List riskHistoryList = null;

		try {
			riskHistoryList = this.queryForList(this.RISK_CASE_HISTORY, risksMasterVO);
			return riskHistoryList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getRiskCaseHistoryCount(RisksMasterVO risksMasterVO) throws CMSException {
		this.logger.debug("Inside Risk Dao to getRiskCaseHistoryCount");

		try {
			return (Integer) this.queryForObject(this.RISK_HISTORY_COUNT, risksMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public RisksMasterVO getRiskInfo(String riskCode) throws CMSException {
		RisksMasterVO risksMasterVO = null;

		try {
			risksMasterVO = (RisksMasterVO) this.queryForObject(this.RISK_INFO, riskCode);
			return risksMasterVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRisk(RisksMasterVO risksMasterVO) throws CMSException {
		int count;
		try {
			this.getSqlMapClient().startTransaction();
			this.logger.debug("Update Risk.... from RiskDAO");
			count = this.update(this.RISK_UPDATE, risksMasterVO);
			if (Integer.parseInt(risksMasterVO.getOldDisplayOnProfileForm()) != risksMasterVO.getDisplayOnProfileForm()
					|| Integer.parseInt(risksMasterVO.getOldRiskIsActive()) != risksMasterVO.getRiskIsActive()
					|| !risksMasterVO.getOldRemarks().equals(risksMasterVO.getRemarks())
					|| !risksMasterVO.getOldRiskLabel().equalsIgnoreCase(risksMasterVO.getRiskLabel())) {
				if (Integer.parseInt(risksMasterVO.getOldDisplayOnProfileForm()) == risksMasterVO
						.getDisplayOnProfileForm()) {
					risksMasterVO.setOldDisplayOnProfileForm("");
				}

				if (Integer.parseInt(risksMasterVO.getOldRiskIsActive()) == risksMasterVO.getRiskIsActive()) {
					risksMasterVO.setOldRiskIsActive("");
				}

				if (risksMasterVO.getOldRemarks().equals(risksMasterVO.getRemarks())) {
					risksMasterVO.setOldRemarks("");
					risksMasterVO.setRemarks("");
				}

				if (risksMasterVO.getOldRiskLabel().equalsIgnoreCase(risksMasterVO.getRiskLabel())) {
					risksMasterVO.setOldRiskLabel("");
					risksMasterVO.setRiskLabel("");
				}

				this.insert(this.RISK_UPDATE_HISTORY, risksMasterVO);
			}

			this.getSqlMapClient().commitTransaction();
		} catch (DataAccessException var12) {
			throw new CMSException(this.logger, var12);
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var11) {
				throw new CMSException(this.logger, var11);
			}
		}

		return count;
	}

	public int addRisks(RisksMasterVO risksMasterVO) throws CMSException {
		byte var2 = 0;

		try {
			this.getSqlMapClient().startTransaction();
			this.logger.debug("Insert new Risk.... from RiskDAO");
			this.logger
					.debug("Risk:New" + risksMasterVO.getRiskLabel() + "\tStatus:" + risksMasterVO.getRiskIsActive());
			this.logger.debug("risksCode------------ in dao==" + risksMasterVO.getRiskCode());
			this.insert(this.RISK_ADD, risksMasterVO);
			this.insert(this.RISK_ADD_HISTORY, risksMasterVO);

			for (int i = 0; i < risksMasterVO.getRiskMapVO().size(); ++i) {
				RisksMapVO risksMapVO = (RisksMapVO) risksMasterVO.getRiskMapVO().get(i);
				risksMapVO.setRiskCode(risksMasterVO.getRiskCode());
				this.insert(this.MAPPING_ADD, risksMasterVO.getRiskMapVO().get(i));
				this.insert(this.MAPPING_ADD_HISTORY, risksMasterVO.getRiskMapVO().get(i));
				this.insertInMultipleTables(risksMapVO);
				this.getSqlMapClient().commitTransaction();
			}

			int count = var2 + 1;
			return count;
		} catch (DataAccessException var13) {
			throw new CMSException(this.logger, var13);
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var12) {
				throw new CMSException(this.logger, var12);
			}
		}
	}

	public int getCountExistRisk(RisksMasterVO risksMasterVO) throws CMSException {
		try {
			int count = Integer.parseInt(this.queryForObject("Risk_Master.searchExistRisk", risksMasterVO).toString());
			this.logger.debug("RISK Name Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public RisksMasterVO checkAssociatedMaster(String riskCode) throws CMSException {
		this.logger.debug("inside RiskDAO :: checkAssociatedMaster riskCode::" + riskCode);

		try {
			return (RisksMasterVO) this.queryForObject("Risk_Master.checkAssociatedMaster", riskCode);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deleteRisks(String riskCode) throws CMSException {
		try {
			this.logger.debug("Delete new Risk.... from RiskDAO");
			int count = Integer.valueOf(this.delete(this.DELETE_RISK, riskCode));
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<RiskCategoryMasterVO> getRiskCategory() throws CMSException {
		List list = null;

		try {
			list = this.queryForList(this.getRiskCategory);
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int addMapping(RisksMapVO risksMapVO) throws CMSException {
		byte var2 = 0;

		int count;
		try {
			this.getSqlMapClient().startTransaction();
			this.insert(this.MAPPING_ADD, risksMapVO);
			this.insert(this.MAPPING_ADD_HISTORY, risksMapVO);
			this.insertInMultipleTables(risksMapVO);
			this.getSqlMapClient().commitTransaction();
			count = var2 + 1;
		} catch (DataAccessException var12) {
			throw new CMSException(this.logger, var12);
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var11) {
				throw new CMSException(this.logger, var11);
			}
		}

		return count;
	}

	public int addMappings(RisksMapVO mappingList) throws CMSException {
		byte var2 = 0;

		int count;
		try {
			this.getSqlMapClient().startTransaction();
			this.insert(this.MAPPING_ADD, mappingList);
			this.insert(this.MAPPING_ADD_HISTORY, mappingList);
			this.getSqlMapClient().commitTransaction();
			count = var2 + 1;
		} catch (DataAccessException var12) {
			throw new CMSException(this.logger, var12);
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var11) {
				throw new CMSException(this.logger, var11);
			}
		}

		return count;
	}

	public List<RiskAttributesMasterVO> getRiskAttributes(RisksMasterVO risksMasterVO) throws CMSException {
		List list = null;

		try {
			list = this.queryForList(this.GET_RISK_ATTRIBUTES, risksMasterVO);
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<RisksMapVO> getAllRisks(RisksMasterVO risksMasterVO) throws CMSException {
		List riskGridList = null;

		try {
			riskGridList = this.queryForList(this.ALL_RISK_LIST, risksMasterVO);
			this.logger.debug("in dao-- riskGridList" + riskGridList.size());
			return riskGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public RisksMapVO getOldRiskMapping(int mappingId) {
		RisksMapVO getOldMapping = (RisksMapVO) this.queryForObject(this.GET_MAPPING, mappingId);
		return getOldMapping;
	}

	public int updateMapping(RisksMapVO risksMapVO) throws CMSException {
		int count = false;
		RisksHistoryVO risksHistoryVO = new RisksHistoryVO();
		boolean var4 = false;

		int count;
		try {
			this.getSqlMapClient().startTransaction();
			this.logger.debug("Update Risk MAPPING.... from RiskDAO");
			int mappingId = risksMapVO.getMappingId();
			RisksMapVO getmapping = (RisksMapVO) this.queryForObject(this.GET_MAPPING, mappingId);
			risksHistoryVO.setACTION("Modified");
			risksHistoryVO.setMAPPING_ID(getmapping.getMappingId());
			risksHistoryVO.setOLD_CLIENTCODE(getmapping.getClientCodes());
			risksHistoryVO.setOLD_MAPPING_NAME(getmapping.getRiskMappingName());
			risksHistoryVO.setOLD_RE(getmapping.getResearchElements());
			risksHistoryVO.setOLD_REPORTTYPE(getmapping.getReportTypes());
			risksHistoryVO.setOLD_RISK_GROUP(String.valueOf(getmapping.getRiskGroup()));
			risksHistoryVO.setOLD_SUBJECT_TYPE(getmapping.getSubjectType());
			risksHistoryVO.setOLD_SUBJECTCOUNTRIES(getmapping.getSubjectCountry());
			risksHistoryVO.setOLD_SUBREPORTTYPE(getmapping.getSubReportTypes());
			risksHistoryVO.setOLD_VISIBLE_TO_CLIENT(String.valueOf(getmapping.getVisibleToClient()));
			risksHistoryVO.setNEW_CLIENTCODE(risksMapVO.getClientCodes());
			risksHistoryVO.setNEW_MAPPING_NAME(risksMapVO.getRiskMappingName());
			risksHistoryVO.setNEW_RE(risksMapVO.getResearchElements());
			risksHistoryVO.setNEW_REPORTTYPE(risksMapVO.getReportTypes());
			risksHistoryVO.setNEW_RISK_GROUP(String.valueOf(risksMapVO.getRiskGroup()));
			risksHistoryVO.setNEW_SUBJECT_TYPE(risksMapVO.getSubjectType());
			risksHistoryVO.setNEW_SUBJECTCOUNTRIES(risksMapVO.getSubjectCountry());
			risksHistoryVO.setNEW_SUBREPORTTYPE(risksMapVO.getSubReportTypes());
			risksHistoryVO.setNEW_VISIBLE_TO_CLIENT(String.valueOf(risksMapVO.getVisibleToClient()));
			risksHistoryVO.setUPDATEDBY(risksMapVO.getUpdatedBy());
			this.insert(this.MAPPING_UPDATE_HISTORY, risksHistoryVO);
			count = this.update(this.RISK_MAPPING_UPDATE, risksMapVO);
			int deleteMappingId = risksMapVO.getMappingId();
			this.delete(this.DELETE_CLIENT, deleteMappingId);
			this.delete(this.DELETE_REPORT, deleteMappingId);
			this.delete(this.DELETE_SUB_REPORT, deleteMappingId);
			this.delete(this.DELETE_RE, deleteMappingId);
			this.delete(this.DELETE_COUNTRY, deleteMappingId);
			risksMapVO.setMappingId(deleteMappingId);
			this.insertInMultipleTables(risksMapVO);
			this.getSqlMapClient().commitTransaction();
		} catch (DataAccessException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var14) {
				throw new CMSException(this.logger, var14);
			}
		}

		return count;
	}

	public int changeMappingStatus(RisksMapVO risksMapVO) throws CMSException {
		int count;
		try {
			this.getSqlMapClient().startTransaction();
			this.logger.debug("Inside the Risk Dao  -- changeMappingStatus");
			int mappingId = risksMapVO.getMappingId();
			int status = (Integer) this.queryForObject(this.GET_MAPPING_STATUS, mappingId);
			HashMap<String, Object> paramMap = new HashMap();
			this.logger.debug("Inside changeMappingStatus....");
			paramMap.put("status", status);
			paramMap.put("MAPPING_ID", mappingId);
			paramMap.put(this.UPDATEDBY, risksMapVO.getUpdatedBy());
			this.insert(this.CHANGE_HISTORY_STATUS, paramMap);
			count = this.update(this.CHANGE_MAPPING_STATUS, risksMapVO);
			this.getSqlMapClient().commitTransaction();
		} catch (DataAccessException var14) {
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var13) {
				throw new CMSException(this.logger, var13);
			}
		}

		return count;
	}

	public List<RisksHistoryVO> getMappingHistory(RisksHistoryVO risksHistoryVO) throws CMSException {
		List riskMappingList = null;

		try {
			riskMappingList = this.queryForList(this.ALL_MAPPING_LIST, risksHistoryVO);
			return riskMappingList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getMappingHistoryGridCount(Integer mappingId) throws CMSException {
		this.logger.debug("Inside Risk Dao to return count of history grid.");

		try {
			return (Integer) this.queryForObject(this.MAPPING_HISTORY_COUNT, mappingId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getRiskId() {
		return (Integer) this.queryForObject(this.GET_RISKID);
	}

	public int getMappingId() {
		return (Integer) this.queryForObject(this.GET_MAPPING_ID);
	}

	public int insertInMultipleTables(RisksMapVO risksMapVO) {
		this.logger.debug("inside insert in multiple tables");
		int count = 0;
		String[] clientCodes = risksMapVO.getClientCodes().split(",");

		for (int k = 0; k < clientCodes.length; ++k) {
			risksMapVO.setClientCodes(clientCodes[k]);
			this.insert(this.CLIENT_ADD, risksMapVO);
		}

		this.logger.debug("insertion in clients done");
		String[] reportTypes = risksMapVO.getReportTypes().split(",");

		for (int k = 0; k < reportTypes.length; ++k) {
			risksMapVO.setReportTypes(reportTypes[k]);
			this.insert(this.REPORT_ADD, risksMapVO);
		}

		this.logger.debug("insertion in reports done");
		String[] subReportTypes = risksMapVO.getSubReportTypes().split(",");

		for (int k = 0; k < subReportTypes.length; ++k) {
			risksMapVO.setSubReportTypes(subReportTypes[k]);
			this.insert(this.SUB_REPORT_ADD, risksMapVO);
		}

		this.logger.debug("insertion in subreports done");
		String[] re = risksMapVO.getResearchElements().split(",");

		for (int k = 0; k < re.length; ++k) {
			risksMapVO.setResearchElements(re[k]);
			this.insert(this.RE_ADD, risksMapVO);
		}

		this.logger.debug("insertion in re done");
		String[] country = risksMapVO.getSubjectCountry().split(",");

		for (int k = 0; k < country.length; ++k) {
			risksMapVO.setSubjectCountry(country[k]);
			this.insert(this.SUBJECT_COUNTRY_ADD, risksMapVO);
		}

		this.logger.debug("insertion in country done");
		int count = count + 1;
		return count;
	}

	public List<ClientMasterVO> getLHSClientList(List<String> clientCodeList) throws CMSException {
		int k = 0;
		int start = false;
		int end = false;
		String clientCodeQueryString = null;
		if (clientCodeList != null) {
			clientCodeQueryString = "";

			while (k < clientCodeList.size()) {
				int start = k;
				int end = k + 1000;
				if (end >= clientCodeList.size()) {
					end = clientCodeList.size() + 1;
				}

				k += 999;
				this.logger.debug("after k updated k is :: " + k + " :: start is  " + start + " end is " + end);
				clientCodeQueryString = clientCodeQueryString + "CLIENT_CODE NOT IN ("
						+ this.listToCommaSeparatedString(clientCodeList.subList(start, end - 1)) + ")";
				if (k < clientCodeList.size()) {
					clientCodeQueryString = clientCodeQueryString + " AND ";
				}
			}
		}

		this.logger.debug("clientCodeQueryString LHS CLIENT==" + clientCodeQueryString);
		RisksMapVO mapVO = new RisksMapVO();
		mapVO.setElementsData(clientCodeQueryString);
		return this.queryForList("Risk_Master.getLHSClientList", mapVO);
	}

	public List<ClientMasterVO> getRHSClientList(List<String> clientCodeList) throws CMSException {
		int k = 0;
		int start = false;
		int end = false;
		String clientCodeQueryString = null;
		if (clientCodeList != null) {
			clientCodeQueryString = "";

			while (k < clientCodeList.size()) {
				int start = k;
				int end = k + 1000;
				if (end >= clientCodeList.size()) {
					end = clientCodeList.size() + 1;
				}

				k += 999;
				this.logger.debug("after k updated k is :: " + k + " :: start is  " + start + " end is " + end);
				clientCodeQueryString = clientCodeQueryString + "CLIENT_CODE IN ("
						+ this.listToCommaSeparatedString(clientCodeList.subList(start, end - 1)) + ")";
				if (k < clientCodeList.size()) {
					clientCodeQueryString = clientCodeQueryString + " OR ";
				}
			}
		}

		RisksMapVO mapVO = new RisksMapVO();
		mapVO.setElementsData(clientCodeQueryString);
		this.logger.debug("clientCodeQueryString RHS CLIENT==" + clientCodeQueryString);
		return this.queryForList("Risk_Master.getRHSClientList", mapVO);
	}

	public List<CountryMasterVO> getLHSCountryList(List countryListUI) {
		RisksMapVO mapVO = new RisksMapVO();
		mapVO.setListOfElements(countryListUI);
		return this.queryForList("Risk_Master.getLHSCountryList", mapVO);
	}

	public List<CountryMasterVO> getRHSCountryList(List countryListUI) {
		RisksMapVO mapVO = new RisksMapVO();
		mapVO.setListOfElements(countryListUI);
		return this.queryForList("Risk_Master.getRHSCountryList", mapVO);
	}

	private String listToCommaSeparatedString(List<String> stringList) throws CMSException {
		StringBuffer resultString = new StringBuffer();

		try {
			this.logger.debug("in listToCommaSeparatedStringObject ..stringList :: " + stringList);
			Iterator var4 = stringList.iterator();

			while (var4.hasNext()) {
				String stringObj = (String) var4.next();
				if (resultString.length() == 0) {
					resultString.append("'" + stringObj + "'");
				} else {
					resultString.append(",'" + stringObj + "'");
				}
			}

			this.logger.debug("resultString :: " + resultString.toString());
			return resultString.toString();
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}
}