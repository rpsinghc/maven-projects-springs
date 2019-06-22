package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import com.worldcheck.atlas.vo.masters.JPMCReportAppVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class JPMCReportDAO extends SqlMapClientTemplate {
	private static final String DAILY_JPMC_REPORT_EXPORT = "JPMCReportTemplate.getDailyJpmcExportData";

	public List<MaterializedViewVO> getMaterializedViewRefreshTime(String mvName) throws CMSException {
		try {
			List<String> tempList = new ArrayList();
			String[] tempArray = mvName.split(",");
			String[] var7 = tempArray;
			int var6 = tempArray.length;

			for (int var5 = 0; var5 < var6; ++var5) {
				String str = var7[var5];
				tempList.add(str);
			}

			MaterializedViewVO mv = new MaterializedViewVO();
			mv.setMvList(tempList);
			List<MaterializedViewVO> mvRefreshList = this.queryForList("JPMCReportTemplate.refreshTimeJPMCDailyReports",
					mv);
			this.logger.debug("after dao call MV refresh::" + mvRefreshList.size());
			return mvRefreshList;
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public List<JPMCReportAppVO> getJPMCDataForExport(JPMCReportAppVO jpmcReportAppVo) {
		this.logger.debug("after dao call MV refresh::1");
		List<JPMCReportAppVO> tempList = this.queryForList("JPMCReportTemplate.getDailyJpmcExportData",
				jpmcReportAppVo);
		this.logger.debug("after dao call MV refresh::2");
		return tempList;
	}
}