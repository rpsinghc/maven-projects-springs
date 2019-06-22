package com.worldcheck.atlas.frontend.search;

import com.worldcheck.atlas.bl.interfaces.ISubjectSearchManager;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.bl.search.SubjectSearchManager;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.SubjectDetails;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JSONSubjectSearchController extends MultiActionController {
	private static final String SUBJECT_TYPE_MASTER = "subjectTypeMaster";
	SearchFactory searchFactory = null;
	ISubjectSearchManager subjectSearchManger = null;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.JSONSubjectSearchController");

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public void setSubjectSearchManager(ISubjectSearchManager subjectSearchManger) {
		this.subjectSearchManger = subjectSearchManger;
	}

	public ModelAndView getSearchSubjectType(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		String crn = "";
		new ArrayList();

		List subjectTypeList;
		try {
			subjectTypeList = this.subjectSearchManger.getSubjectTypeList();
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		viewForJson.addObject("subjectTypeMaster", subjectTypeList);
		return viewForJson;
	}

	public ModelAndView getSubjectSearchResults(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) throws Exception {
		SubjectSearchManager subjectSearch = (SubjectSearchManager) this.searchFactory.getSearchImpl("SubjectSearch");
		int start = false;
		int limit = false;
		int count = false;
		new ArrayList();

		List subjectSearchList;
		int count;
		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			count = false;
			searchCriteria.setMinRecord(start + 1);
			searchCriteria.setMaxRecord(start + limit);
			searchCriteria.setSortColumnName(request.getParameter("sort"));
			searchCriteria.setSortType(request.getParameter("dir"));
			searchCriteria.setCountry(StringUtils.commaSeparatedStringToList(searchCriteria.getCountryId()));
			subjectSearchList = subjectSearch.search(searchCriteria);
			count = subjectSearch.resultCount(searchCriteria);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		ModelAndView viewForJson = new ModelAndView("jsonView");
		viewForJson.addObject("subjectSearchResults", subjectSearchList);
		viewForJson.addObject("total", count);
		return viewForJson;
	}

	public ModelAndView getAssociateCasesForSubSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		this.logger.debug("Inside getAssociateCasesForSub method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		int start = false;
		int limit = false;
		boolean count = false;

		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			count = false;
			searchCriteria.setMinRecord(start + 1);
			searchCriteria.setMaxRecord(start + limit);
			searchCriteria.setSortColumnName(request.getParameter("sort"));
			searchCriteria.setSortType(request.getParameter("dir"));
			List<SubjectDetails> assCaseList = this.subjectSearchManger.getAssociateCasesForSub(searchCriteria);
			modelAndView.addObject("total", this.subjectSearchManger.getAsscciateCaseCount(searchCriteria));
			modelAndView.addObject("assCaseList", assCaseList);
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (NullPointerException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}