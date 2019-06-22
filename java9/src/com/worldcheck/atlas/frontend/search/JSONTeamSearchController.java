package com.worldcheck.atlas.frontend.search;

import com.worldcheck.atlas.bl.interfaces.ITeamSearchManager;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.TeamSearchVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONTeamSearchController extends JSONMultiActionController {
	private String ANALYST_LIST = "analystInfo";
	private String REVIEWER_LIST = "reviewerInfo";
	private String BI_VENDOR_LIST = "biVendorInfo";
	private String TEAM_SEARCH_RESULT = "teamSearchResult";
	private String Total = "total";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.JSONTeamSearchController");
	SearchFactory searchFactory = null;
	ITeamSearchManager teamSearchManager = null;

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public void setTeamSearchManager(ITeamSearchManager teamSearchManager) {
		this.teamSearchManager = teamSearchManager;
	}

	public ModelAndView analystInfo(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> analystList = this.teamSearchManager.getAllAnalyst();
			viewForJson.addObject(this.ANALYST_LIST, analystList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView reviewerInfo(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> reviewerList = this.teamSearchManager.getAllReviewer();
			viewForJson.addObject(this.REVIEWER_LIST, reviewerList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView biVendorInfo(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> biVendorList = this.teamSearchManager.getAllBiVendorMgr();
			viewForJson.addObject(this.BI_VENDOR_LIST, biVendorList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView vendorTeamSearchInfo(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> biVendorList = this.teamSearchManager.vendorTeamSearchInfo();
			viewForJson.addObject(this.BI_VENDOR_LIST, biVendorList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getTeamSearchResults(HttpServletRequest request, HttpServletResponse response,
			TeamSearchVO teamSearchVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		boolean count = false;

		int count;
		try {
			teamSearchVO.setCrnNumbers(StringUtils.commaSeparatedStringToList(teamSearchVO.getCrnNo()));
			int start = false;
			int limit = false;
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			count = false;
			teamSearchVO.setMinRecord(start + 1);
			teamSearchVO.setMaxRecord(start + limit);
			teamSearchVO.setSortColumnName(request.getParameter("sort"));
			teamSearchVO.setSortType(request.getParameter("dir"));
			List<TeamSearchVO> teamSearchList = this.teamSearchManager.getTeamSearchResult(teamSearchVO);
			count = this.teamSearchManager.resultCount(teamSearchVO);
			viewForJson.addObject(this.TEAM_SEARCH_RESULT, teamSearchList);
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		viewForJson.addObject(this.Total, count);
		return viewForJson;
	}

	public ModelAndView getTeamSearchProcessData(HttpServletRequest request, HttpServletResponse response,
			TeamSearchVO teamSearchVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		new ArrayList();

		List teamSearchList;
		try {
			this.logger.debug("get process data for crn " + teamSearchVO.getCrnNo());
			this.logger.debug("get process data for team name " + teamSearchVO.getTeamName());
			teamSearchList = this.teamSearchManager.getTeamSearchProcessData(teamSearchVO.getCrnNo(),
					teamSearchVO.getTeamName());
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		viewForJson.addObject("teamSearchProcessData", teamSearchList);
		return viewForJson;
	}
}