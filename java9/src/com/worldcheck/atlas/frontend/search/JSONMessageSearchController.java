package com.worldcheck.atlas.frontend.search;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.search.MessageSearchManager;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONMessageSearchController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.JSONMessageSearchController");
	SearchFactory searchFactory = null;

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public ModelAndView getMessageSearchResults(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) throws Exception {
		MessageSearchManager messageSearch = (MessageSearchManager) this.searchFactory.getSearchImpl("MessageSearch");
		int start = false;
		int limit = false;
		int count = false;
		new ArrayList();

		List messageSearchList;
		int count;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			count = false;
			searchCriteria.setMinRecord(start + 1);
			searchCriteria.setMaxRecord(start + limit);
			searchCriteria.setSortColumnName(request.getParameter("sort"));
			searchCriteria.setSortType(request.getParameter("dir"));
			searchCriteria.setCrnNumbers(StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
			searchCriteria.setLoginUser(userBean.getUserName());
			messageSearchList = messageSearch.search(searchCriteria);
			count = messageSearch.resultCount(searchCriteria);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		ModelAndView viewForJson = new ModelAndView("jsonView");
		viewForJson.addObject("messageSearchResults", messageSearchList);
		viewForJson.addObject("total", count);
		return viewForJson;
	}

	public ModelAndView getRecurrenceMessageSearchResults(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) throws Exception {
		MessageSearchManager messageSearch = (MessageSearchManager) this.searchFactory.getSearchImpl("MessageSearch");
		int start = false;
		int limit = false;
		int count = false;
		new ArrayList();

		List messageSearchList;
		int count;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			count = false;
			searchCriteria.setMinRecord(start + 1);
			searchCriteria.setMaxRecord(start + limit);
			searchCriteria.setSortColumnName(request.getParameter("sort"));
			searchCriteria.setSortType(request.getParameter("dir"));
			searchCriteria.setCrnNumbers(StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
			searchCriteria.setLoginUser(userBean.getUserName());
			messageSearchList = messageSearch.recSearch(searchCriteria);
			count = messageSearch.recResultCount(searchCriteria);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}

		ModelAndView viewForJson = new ModelAndView("jsonView");
		viewForJson.addObject("messageSearchResults", messageSearchList);
		viewForJson.addObject("total", count);
		return viewForJson;
	}

	public ModelAndView getUsersForMessageSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) throws Exception {
		new ArrayList();

		List userList;
		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			MessageSearchManager messageSearch = (MessageSearchManager) this.searchFactory
					.getSearchImpl("MessageSearch");
			userList = messageSearch.getUsersList(request.getParameter("sentMsg"), userBean.getUserName());
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		ModelAndView viewForJson = new ModelAndView("jsonView");
		viewForJson.addObject("messageSearchUserMaster", userList);
		return viewForJson;
	}

	public ModelAndView createSearchRecCase(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("jsonView");
		String message = "Case Created Successfully.";
		String recCaseSchedulerId = "";
		if (null != request.getParameter("recCaseSchedulerId")) {
			recCaseSchedulerId = request.getParameter("recCaseSchedulerId");
		}

		String recClientCaseId = "";
		if (null != request.getParameter("recClientCaseId")) {
			recClientCaseId = request.getParameter("recClientCaseId");
		}

		this.logger.debug("recClientCaseId " + recClientCaseId + " recCaseSchedulerId " + recCaseSchedulerId);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (null != recClientCaseId && !recClientCaseId.equalsIgnoreCase("")) {
				this.logger.debug("Going to create case");
				ResourceLocator.self().getNotificationService().createRecCase(recClientCaseId, recCaseSchedulerId,
						userBean.getUserName(), SBMUtils.getSession(request));
			}
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		request.getSession().setAttribute("message", message);
		return mv;
	}
}