package com.worldcheck.atlas.dao.createcase;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseCreationRiskAssociationVO;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.CaseLevelRiskProfileDetailsVO;
import com.worldcheck.atlas.vo.RiskProfileHistoryVO;
import com.worldcheck.atlas.vo.TotalRiskAggregationVO;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CreateCaseDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.createcase.CreateCaseDAO");
	private static final String CHECK_FOR_CASE = "CreateCase.checkForCase";
	private static final String CHECK_FOR_RISK_DATA = "CreateCase.checkForRiskData";
	private static final String CHECK_FOR_RISK_HISTORY_DATA = "CreateCase.checkForRiskHistoryData";
	private static final String CHECK_PID_BY_ORDER_ID = "CreateCase.checkPIDByOrderId";
	private static final String CHECK_CRN_BY_ORDER_ID = "CreateCase.checkCRNByOrderId";
	private static final String CHECK_FOR_RECURR_CASE = "CreateCase.checkForRecurrCase";
	private static final String CREATE_WITHOUT_SUBJECT = "CreateCase.createCase_Client_Without_Subject";
	private static final String CREATE_WITH_SUBJECT = "CreateCase.createCase_Client";
	private static final String REMOVE_CASE = "CreateCase.removeCase";
	private static final String REMOVE_RISK_DATA = "CreateCase.removeRiskData";
	private static final String REMOVE_RISK_HISTORY_DATA = "CreateCase.removeRiskHistoryData";
	private static final String REMOVE_RECURR_CASE = "CreateCase.removeRecurrCase";
	private static final String GET_ASSOCIATED_BDM_FOR_CLIENT = "CreateCase.getAssociatedBDMforClient";
	private static final String GET_RISK_CODE_FOR_CLIENT = "CreateCase.getRiskCode";
	private static final String GET_RISK_CODE_FOR_CLIENT_CNTRY_BRKDOWN = "CreateCase.getRiskCodeHasCntryBrkDown";
	private static final String INSERT_CASE_ADDITION_TOTAL_RISK_AGGR = "CreateCase.insertCaseAdditionTotalRiskAggr";
	private static final String ASSOCIATE_CASE_WITH_RISK = "CreateCase.associateRiskAtCaseCreation";
	private static final String INSERT_CASE_LEVEL_RISK_HISTORY = "CreateCase.insertCaseLevelRiskHistory";
	private static final String CREATE_CASE_WITHOUT_SUBJECT = "CreateCase.createCase_Without_Subject";
	private static final String CREATE_CASE_WITH_SUBJECT = "CreateCase.createCase_With_Subject";

	public void createCase(CaseDetails caseDetail) throws CMSException {
		try {
			this.logger.debug("Inside the caseCreate Dao");
			this.logger.debug("sub report type value is " + caseDetail.getSubReportTypeId());
			if (caseDetail.getSubReportTypeId() != null
					&& !"".equalsIgnoreCase(caseDetail.getSubReportTypeId().trim())) {
				this.insert("CreateCase.createCase_Client", caseDetail);
			} else {
				this.insert("CreateCase.createCase_Client_Without_Subject", caseDetail);
			}

		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void insertRiskProfileData(CaseLevelRiskProfileDetailsVO caseLevelRiskProfileVO) throws CMSException {
		try {
			this.logger.debug("Inside the caseCreate Dao");
			this.logger.debug("sub report type--" + caseLevelRiskProfileVO.getSubReportType() + "---Report Type--"
					+ caseLevelRiskProfileVO.getReportType() + "---Client Code---"
					+ caseLevelRiskProfileVO.getClientCode());
			List<CaseCreationRiskAssociationVO> list = this.queryForList("CreateCase.getRiskCode",
					caseLevelRiskProfileVO);
			this.logger.debug("List Size is--" + list.size());
			Iterator itr = list.iterator();

			while (itr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreateRiskAss = (CaseCreationRiskAssociationVO) itr.next();
				caseCreateRiskAss.setCrn(caseLevelRiskProfileVO.getCrn());
				this.insert("CreateCase.associateRiskAtCaseCreation", caseCreateRiskAss);
				RiskProfileHistoryVO riskProfileHistoryVO = new RiskProfileHistoryVO();
				riskProfileHistoryVO.setCrn(caseCreateRiskAss.getCrn());
				riskProfileHistoryVO.setRiskCategoryId(caseCreateRiskAss.getRiskCategoryId());
				riskProfileHistoryVO.setRiskCode(caseCreateRiskAss.getRiskCode());
				riskProfileHistoryVO.setAction("Add");
				riskProfileHistoryVO.setTask("Case Creation");
				riskProfileHistoryVO.setAttributeId(caseCreateRiskAss.getAttributeId());
				riskProfileHistoryVO.setNewValue(caseCreateRiskAss.getAttributeValue());
				this.insert("CreateCase.insertCaseLevelRiskHistory", riskProfileHistoryVO);
			}

			List<CaseCreationRiskAssociationVO> cntryBrkDownList = this
					.queryForList("CreateCase.getRiskCodeHasCntryBrkDown", caseLevelRiskProfileVO);
			Iterator cntryBrkDownItr = cntryBrkDownList.iterator();

			while (cntryBrkDownItr.hasNext()) {
				CaseCreationRiskAssociationVO caseCreationRiskAssociationVO = (CaseCreationRiskAssociationVO) cntryBrkDownItr
						.next();
				caseCreationRiskAssociationVO.setCrn(caseLevelRiskProfileVO.getCrn());
				this.insert("CreateCase.associateRiskAtCaseCreation", caseCreationRiskAssociationVO);
			}

			TotalRiskAggregationVO totalRiskAggr = new TotalRiskAggregationVO();
			totalRiskAggr.setCrn(caseLevelRiskProfileVO.getCrn());
			totalRiskAggr.setOverallCaseLvlAggr(0);
			totalRiskAggr.setOverallSubLvlAggr(0);
			totalRiskAggr.setOverallCrnLvlAggr(0);
			this.insert("CreateCase.insertCaseAdditionTotalRiskAggr", totalRiskAggr);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int checkForCase(String crn) throws CMSException {
		int i = 0;

		try {
			Object obj = this.queryForObject("CreateCase.checkForCase", crn);
			if (obj != null) {
				i = (Integer) obj;
			}

			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkForRiskData(String crn) throws CMSException {
		int i = 0;

		try {
			Object obj = this.queryForObject("CreateCase.checkForRiskData", crn);
			if (obj != null) {
				i = (Integer) obj;
			}

			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkForRiskHistoryData(String crn) throws CMSException {
		int i = 0;

		try {
			Object obj = this.queryForObject("CreateCase.checkForRiskHistoryData", crn);
			if (obj != null) {
				i = (Integer) obj;
			}

			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkForRecurrCase(String crn) throws CMSException {
		int i = 0;

		try {
			Object obj = this.queryForObject("CreateCase.checkForRecurrCase", crn);
			if (obj != null) {
				i = (Integer) obj;
			}

			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int removeCase(String crn) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.delete("CreateCase.removeCase", crn);
			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int removeRiskData(String crn) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.delete("CreateCase.removeRiskData", crn);
			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int removeRiskHistoryData(String crn) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.delete("CreateCase.removeRiskHistoryData", crn);
			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int removeRecurrCase(String crn) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.delete("CreateCase.removeRecurrCase", crn);
			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getAssociatedBDMforClient(String clientName) throws CMSException {
		String clientname = "";

		try {
			Object obj = this.queryForObject("CreateCase.getAssociatedBDMforClient", clientName);
			if (obj != null) {
				clientname = (String) obj;
			}

			return clientname;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void createCaseForISIS(CaseDetails caseDetail) throws CMSException {
		try {
			this.logger.debug("Inside the caseCreate Dao");
			this.logger.debug("sub report type value is " + caseDetail.getSubReportTypeId());
			if (caseDetail.getSubReportTypeId() != null && !"".equalsIgnoreCase(caseDetail.getSubReportTypeId().trim())
					&& !"0".equalsIgnoreCase(caseDetail.getSubReportTypeId().trim())) {
				this.insert("CreateCase.createCase_With_Subject", caseDetail);
			} else {
				this.insert("CreateCase.createCase_Without_Subject", caseDetail);
			}

		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}