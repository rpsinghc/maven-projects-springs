package com.worldcheck.atlas.frontend.search;

import com.worldcheck.atlas.bl.interfaces.ICaseSearchManager;
import com.worldcheck.atlas.bl.search.CaseSearchManager;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public class JSONCaseSearchController extends JSONMultiActionController {
	private static final String CASE_CREATOR_MASTER = "caseCreatorMasterList";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.JSONCaseSearchController");
	SearchFactory searchFactory = null;
	ICaseSearchManager caseSearchManger = null;

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public void setCaseSearchManager(ICaseSearchManager caseSearchManger) {
		this.caseSearchManger = caseSearchManger;
	}

	public ModelAndView getNormalCaseSearchResults(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) throws Exception {
		try {
			HttpSession session = request.getSession();
			new ArrayList();
			CaseSearchManager caseSearch = (CaseSearchManager) this.searchFactory.getSearchImpl("CaseSearch");
			int count = false;
			List caseSearchList;
			boolean start;
			boolean limit;
			int count;
			int start;
			int limit;
			if (session.getAttribute("ecuteLastSearchClick") != null
					&& !session.getAttribute("ecuteLastSearchClick").equals("no")) {
				this.logger.debug("else criteria");
				if (session.getAttribute("lastSearchCriteria") != null
						&& session.getAttribute("ecuteLastSearchClick").equals("yes")) {
					caseSearchList = caseSearch.search((SearchCriteria) session.getAttribute("lastSearchCriteria"));
					count = caseSearch.resultCount((SearchCriteria) session.getAttribute("lastSearchCriteria"));
					session.setAttribute("ecuteLastSearchClick", "no");
					this.logger.debug("if inside else");
				} else {
					this.logger.debug("else inside else");
					start = false;
					limit = false;
					start = Integer.parseInt(request.getParameter("start"));
					limit = Integer.parseInt(request.getParameter("limit"));
					count = false;
					searchCriteria.setMinRecord(start + 1);
					searchCriteria.setMaxRecord(start + limit);
					searchCriteria.setSortColumnName(request.getParameter("sort"));
					searchCriteria.setSortType(request.getParameter("dir"));
					searchCriteria.setCrnNumbers(StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
					searchCriteria.setStartCaseRecvDate(searchCriteria.getStartCaseRecvDate());
					searchCriteria.setStartClientDueDate(searchCriteria.getStartClientDueDate());
					caseSearchList = caseSearch.search(searchCriteria);
					count = caseSearch.resultCount(searchCriteria);
					session.setAttribute("lastSearchCriteria", searchCriteria);
					session.setAttribute("ecuteLastSearchClick", "no");
				}

				this.logger
						.debug(" else===searchCriteria.getStartCaseRecvDate()  " + searchCriteria.getStartCaseRecvDate()
								+ "searchCriteria.getStartClientDueDate()  " + searchCriteria.getStartClientDueDate());
			} else {
				start = false;
				limit = false;
				start = Integer.parseInt(request.getParameter("start"));
				limit = Integer.parseInt(request.getParameter("limit"));
				count = false;
				searchCriteria.setMinRecord(start + 1);
				searchCriteria.setMaxRecord(start + limit);
				searchCriteria.setSortColumnName(request.getParameter("sort"));
				searchCriteria.setSortType(request.getParameter("dir"));
				searchCriteria.setCrnNumbers(StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
				this.logger
						.debug(" if 1 searchCriteria.getStartCaseRecvDate()  " + searchCriteria.getStartCaseRecvDate()
								+ "searchCriteria.getStartClientDueDate()  " + searchCriteria.getStartClientDueDate());
				searchCriteria.setStartCaseRecvDate(searchCriteria.getStartCaseRecvDate());
				searchCriteria.setStartClientDueDate(searchCriteria.getStartClientDueDate());
				caseSearchList = caseSearch.search(searchCriteria);
				count = caseSearch.resultCount(searchCriteria);
				session.setAttribute("lastSearchCriteria", searchCriteria);
				this.logger.debug("if ecuteLastSearchClick  " + session.getAttribute("ecuteLastSearchClick"));
				session.setAttribute("ecuteLastSearchClick", "no");
				this.logger.debug("1111111111111111111111");
			}

			ModelAndView viewForJson = new ModelAndView("jsonView");
			viewForJson.addObject("caseSearchResults", caseSearchList);
			viewForJson.addObject("total", count);
			return viewForJson;
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getRecurrenceCaseSearchResults(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) throws Exception {
		int start = false;
		int limit = false;
		int count = false;
		new ArrayList();

		List caseSearchList;
		int count;
		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			count = false;
			searchCriteria.setMinRecord(start + 1);
			searchCriteria.setMaxRecord(start + limit);
			searchCriteria.setSortColumnName(request.getParameter("sort"));
			searchCriteria.setSortType(request.getParameter("dir"));
			CaseSearchManager caseSearch = (CaseSearchManager) this.searchFactory.getSearchImpl("CaseSearch");
			searchCriteria.setSearchType("recurrence");
			caseSearchList = caseSearch.search(searchCriteria);
			count = caseSearch.resultCount(searchCriteria);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}

		ModelAndView viewForJson = new ModelAndView("jsonView");
		viewForJson.addObject("caseSearchResults", caseSearchList);
		viewForJson.addObject("total", count);
		return viewForJson;
	}

	public ModelAndView getCaseCreator(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		String crn = "";
		new ArrayList();

		List userList;
		try {
			userList = this.caseSearchManger.getCaseCreatorMaster();
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		viewForJson.addObject("caseCreatorMasterList", userList);
		return viewForJson;
	}

	public ModelAndView checkSessionID(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		if (null != request.getSession().getAttribute("sessionID")
				&& !"".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			viewForJson.addObject("sucess", "false");
		} else {
			viewForJson.addObject("sucess", "true");
		}

		return viewForJson;
	}
}