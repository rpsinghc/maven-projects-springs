package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.dao.masters.RiskProfileDAO.1;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.2;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.3;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.4;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.5;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.6;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.7;
import com.worldcheck.atlas.dao.masters.RiskProfileDAO.8;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class RiskProfileDAO extends SqlMapClientDaoSupport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.RiskProfileDAO");
	private static final String UPDATE_RISK_PROFILE_DATA = "Risk_Master.updateRiskProfileData";
	private static final String INSERT_RISK_PROFILE_DATA_WITH_SUBJECT = "Risk_Master.insertRiskProfileDataWithSubject";
	private static final String INSERT_RISK_PROFILE_DATA_WITHOUT_SUBJECT = "Risk_Master.insertRiskProfileDataWithoutSubject";
	private static final String UPDATE_PROFILE_DATA_WITH_HBD = "Risk_Master.updateRiskProfileDataWithHBD";
	private static final String UPDATE_SUBJECT_DATA_WITH_INDUSTRY_CODE = "Risk_Master.updateSubjectDataWithIndusCode";
	private static final String UPDATE_EACH_SUBJECT_AGGREGATION_DATA_WITH_RISK = "Risk_Master.updateEachSubjectAggregationData";
	private static final String UPDATE_AGGREGATION_DATA_WITH_RISK = "Risk_Master.updateAggregationDataWithRisk";
	private static final String UPDATE_TOTAL_AGGREGATION_DATA_WITH_RISK = "Risk_Master.updateTotalAggregationDataWithRisk";
	private static final String GET_PROFILE_ID_BY_RISK_CODE = "Risk_Master.getProfileIdByRiskCode";
	private static final String GET_TOTAL_AGGR_ID_BY_CRN = "Risk_Master.getTotalAggrIdByCRN";

	public long updateRiskProfileData(List<RiskProfileVO> riskProfileList) throws CMSException {
      long totalCount = 0L;

      try {
         this.logger.debug("updateRiskProfileData.... from RiskProfileDAO" + riskProfileList.size());
         int count = (Integer)this.getSqlMapClientTemplate().execute(new 1(this, riskProfileList));
         return totalCount + (long)count;
      } catch (DataAccessException var6) {
         throw new CMSException(this.logger, var6);
      } catch (Exception var7) {
         throw new CMSException(this.logger, var7);
      }
   }

	public long updateRiskProfileDataWithHBD(List<RiskProfileVO> riskProfileListWithHBD) throws CMSException {
      long totalCount = 0L;

      try {
         this.logger.debug("updateRiskProfileData.... from RiskProfileDAO" + riskProfileListWithHBD.size());
         int count = (Integer)this.getSqlMapClientTemplate().execute(new 2(this, riskProfileListWithHBD));
         return totalCount + (long)count;
      } catch (DataAccessException var6) {
         throw new CMSException(this.logger, var6);
      } catch (Exception var7) {
         throw new CMSException(this.logger, var7);
      }
   }

	public long updateRiskAggregationData(List<RiskAggregationVO> riskAggregationList) throws CMSException {
      long totalCount = 0L;

      try {
         this.logger.debug("updateRiskAggregationData.... from RiskProfileDAO" + riskAggregationList.size());
         int count = (Integer)this.getSqlMapClientTemplate().execute(new 3(this, riskAggregationList));
         return totalCount + (long)count;
      } catch (DataAccessException var6) {
         throw new CMSException(this.logger, var6);
      } catch (Exception var7) {
         throw new CMSException(this.logger, var7);
      }
   }

	public long updateTotalRiskAggregationData(List<TotalRiskAggregationVO> totalRiskAggregationList) throws CMSException {
      long totalCount = 0L;

      try {
         this.logger.debug("updateTotalRiskAggregationData.... from RiskProfileDAO" + totalRiskAggregationList.size());
         int count = (Integer)this.getSqlMapClientTemplate().execute(new 4(this, totalRiskAggregationList));
         return totalCount + (long)count;
      } catch (DataAccessException var6) {
         throw new CMSException(this.logger, var6);
      } catch (Exception var7) {
         throw new CMSException(this.logger, var7);
      }
   }

	public long updateSubjectWithIndustryCode(List<RiskProfileVO> subIndusList) throws CMSException {
      long totalCount = 0L;

      try {
         this.logger.debug("updateSubjectWithIndustryCode.... from RiskProfileDAO" + subIndusList.size());
         int count = (Integer)this.getSqlMapClientTemplate().execute(new 5(this, subIndusList));
         return totalCount + (long)count;
      } catch (DataAccessException var6) {
         throw new CMSException(this.logger, var6);
      } catch (Exception var7) {
         throw new CMSException(this.logger, var7);
      }
   }

	public long fetchProfileId(List<RiskProfileVO> riskForProfileIDList) throws CMSException {
      long profileId = 0L;

      try {
         profileId = (Long)this.getSqlMapClientTemplate().execute(new 6(this, riskForProfileIDList));
      } catch (DataAccessException var5) {
         throw new CMSException(this.logger, var5);
      }

      this.logger.debug("Profile id is::" + profileId);
      return profileId;
   }

	public int fetchTotalAggrId(String crn) throws CMSException {
      int profileId = 0;

      try {
         this.logger.debug("fetchTotalAggrId getSqlMapClientTemplate()----" + this.getSqlMapClientTemplate());
         this.logger.debug("fetchTotalAggrId CRN----" + crn);
         Object obj = this.getSqlMapClientTemplate().execute(new 7(this, crn));
         if (obj != null) {
            profileId = (Integer)obj;
         }
      } catch (DataAccessException var4) {
         throw new CMSException(this.logger, var4);
      }

      this.logger.debug("Profile id is::" + profileId);
      return profileId;
   }

	public void saveSubjectAggregation(List<SubjectLevelAggregation> subLevelAggregation) throws CMSException {
      long var2 = 0L;

      try {
         this.logger.debug("saveSubjectAggregation.... from RiskProfileDAO" + subLevelAggregation.size());
         int count = (Integer)this.getSqlMapClientTemplate().execute(new 8(this, subLevelAggregation));
      } catch (DataAccessException var6) {
         throw new CMSException(this.logger, var6);
      } catch (Exception var7) {
         throw new CMSException(this.logger, var7);
      }
   }
}