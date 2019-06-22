package com.worldcheck.atlas.dao.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.TeamSearchVO;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TeamSearchDAO extends SqlMapClientTemplate implements ISearchDAO {
	private static final String GET_ALL_ANALYST = "TeamSearch.getAllAnalyst";
	private static final String GET_ALL_REVIEWER = "TeamSearch.getAllReviewer";
	private static final String GET_ALL_BI_VENDOR_MGR = "TeamSearch.getAllBIVendorMgr";
	private static final String GET_TEAM_SEARCH_RESULT = "TeamSearch.getTeamSearchResult";
	private static final String GET_TEAM_SEARCH_COUNT = "TeamSearch.getTeamSearchCount";
	private static final String GET_TEAM_SEARCH_EXPORT_TO_XL = "TeamSearch.getTeamSearchExportToExl";
	private static final String GET_ALL_VENDOR_MGR = "TeamSearch.getAllVendorMgr";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.search.TeamSearchDAO");

	public List search(SearchCriteria searchCriteria) throws CMSException {
		return null;
	}

	public List getAllAnalyst() throws CMSException {
		try {
			List analystList = this.queryForList("TeamSearch.getAllAnalyst");
			return analystList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List getAllReviewer() throws CMSException {
		try {
			List reviewerList = this.queryForList("TeamSearch.getAllReviewer");
			return reviewerList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List getAllbiVendor() throws CMSException {
		try {
			List biVendorList = this.queryForList("TeamSearch.getAllBIVendorMgr");
			return biVendorList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<TeamSearchVO> getTeamSearchResult(TeamSearchVO teamSearchVO) throws CMSException {
		try {
			List<TeamSearchVO> teamSearchList = this.queryForList("TeamSearch.getTeamSearchResult", teamSearchVO);
			return teamSearchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int resultCount(TeamSearchVO teamSearchVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("TeamSearch.getTeamSearchCount", teamSearchVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<TeamSearchVO> searchForExport(Map<String, Object> excelParamMap) throws CMSException {
		List teamSearchList = null;

		try {
			teamSearchList = this.queryForList("TeamSearch.getTeamSearchExportToExl", excelParamMap);
			return teamSearchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> vendorTeamSearchInfo() throws CMSException {
		try {
			List<UserMasterVO> biVendorList = this.queryForList("TeamSearch.getAllVendorMgr");
			return biVendorList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}
}