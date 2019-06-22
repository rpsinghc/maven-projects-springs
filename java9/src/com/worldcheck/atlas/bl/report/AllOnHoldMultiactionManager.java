package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.AllOnHoldCaseVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllOnHoldMultiactionManager implements IAtlasReport {
	private String start = "start";
	private String limit = "limit";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.OverdueReportMultiactionManager");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		AllOnHoldCaseVO allOnHoldCaseVO = new AllOnHoldCaseVO();
		int startVal = Integer.parseInt(request.getParameter(this.start)) + 1;
		int limitVal = Integer.parseInt(request.getParameter(this.start))
				+ Integer.parseInt(request.getParameter(this.limit));
		allOnHoldCaseVO.setStart(startVal);
		allOnHoldCaseVO.setLimit(limitVal);
		allOnHoldCaseVO.setSortColumnName(request.getParameter("sort"));
		allOnHoldCaseVO.setSortType(request.getParameter("dir"));
		return this.tabularReportDAO.allOnHoldCase(allOnHoldCaseVO);
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return this.tabularReportDAO.allOnHoldCaseCount();
	}
}