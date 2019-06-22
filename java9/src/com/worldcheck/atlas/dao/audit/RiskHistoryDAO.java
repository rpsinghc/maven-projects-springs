package com.worldcheck.atlas.dao.audit;

import com.worldcheck.atlas.dao.audit.RiskHistoryDAO.1;
import com.worldcheck.atlas.dao.audit.RiskHistoryDAO.2;
import com.worldcheck.atlas.dao.audit.RiskHistoryDAO.3;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class RiskHistoryDAO extends SqlMapClientDaoSupport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.audit.RiskHistoryDAO");
	private static final String FETCH_RISK_HISTORY_SQL = "Risk_Master.getRiskHistoryByRiskCode";
	private static final String FETCH_RISK_COUNT_SQL = "Risk_Master.getRiskCountByRiskCode";
	private static final String INSERT_RISK_HISTORY_SQL = "Risk_Master.insertRiskHistoryByRiskCode";

	public List<RiskHistory> getRiskHistoryDetails(HashMap paramList) throws CMSException {
      this.logger.debug("In RiskHistoryDAO::getRiskHistoryDetails");
      new ArrayList();

      List result;
      try {
         result = (List)this.getSqlMapClientTemplate().execute(new 1(this, paramList));
         this.logger.debug("Risk history list size:- " + result.size());
      } catch (DataAccessException var4) {
         throw new CMSException(this.logger, var4);
      } catch (Exception var5) {
         throw new CMSException(this.logger, var5);
      }

      this.logger.debug("Exiting RiskHistoryDAO::getRiskHistoryDetails");
      return result;
   }

	public Integer getRiskHistoryCountForRiskCode(HashMap paramList) throws CMSException {
      this.logger.debug("In RiskHistoryDAO::getRiskHistoryCountForRiskCode");
      boolean var2 = false;

      int count;
      try {
         count = (Integer)this.getSqlMapClientTemplate().execute(new 2(this, paramList));
         this.logger.debug("COunt of Rosk History::" + count);
      } catch (DataAccessException var4) {
         throw new CMSException(this.logger, var4);
      } catch (Exception var5) {
         throw new CMSException(this.logger, var5);
      }

      this.logger.debug("Exiting RiskHistoryDAO::getRiskHistoryCountForRiskCode");
      return count;
   }

	public long saveRiskHistoryData(List<RiskHistory> riskHistoryList) throws CMSException {
      this.logger.debug("In RiskHistoryDAO::saveRiskHistoryData");
      boolean var2 = false;

      int count;
      try {
         count = (Integer)this.getSqlMapClientTemplate().execute(new 3(this, riskHistoryList));
      } catch (DataAccessException var4) {
         throw new CMSException(this.logger, var4);
      } catch (Exception var5) {
         throw new CMSException(this.logger, var5);
      }

      this.logger.debug("Exiting RiskHistoryDAO::saveRiskHistoryData::" + count);
      return (long)count;
   }
}