package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class DBUtils extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.utils.DBUtils");
	private static final String GET_DYNAMIC_DATA = "DBUtils.getDynamicColumnData";

	public String getSigleColumnDataFromTable(String tablename, String selectColumn, String whrClauseKey,
			String whrClauseValue, String whrClauseKey_opt, String whrClauseValue_opt) throws CMSException {
		this.logger.debug("Inside getDescFromTable Method of DBUtils class Start");
		String desc = "";

		try {
			Map<String, String> map = new HashMap();
			map.put("tablename", tablename);
			map.put("selectColumn", selectColumn);
			map.put("whrClauseKey", whrClauseKey);
			map.put("whrClauseValue", whrClauseValue);
			map.put("whrClauseKey_opt", whrClauseKey_opt);
			map.put("whrClauseValue_opt", whrClauseValue_opt);
			desc = (String) this.queryForObject("DBUtils.getDynamicColumnData", map);
		} catch (ClassCastException var18) {
			throw new CMSException(this.logger, var18);
		} catch (DataAccessException var19) {
			throw new CMSException(this.logger, var19);
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var17) {
				throw new CMSException(this.logger, var17);
			}
		}

		this.logger.debug("Inside getDescFromTable Method of DBUtils class End");
		return desc;
	}
}