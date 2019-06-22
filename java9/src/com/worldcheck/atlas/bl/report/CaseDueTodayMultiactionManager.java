package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.CaseDueTodayVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaseDueTodayMultiactionManager implements IAtlasReport {
	private String start = "start";
	private String limit = "limit";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.OverdueReportMultiactionManager");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		CaseDueTodayVO caseDueTodayVO = new CaseDueTodayVO();
		int startVal = Integer.parseInt(request.getParameter(this.start)) + 1;
		int limitVal = Integer.parseInt(request.getParameter(this.start))
				+ Integer.parseInt(request.getParameter(this.limit));
		caseDueTodayVO.setStart(startVal);
		caseDueTodayVO.setLimit(limitVal);
		caseDueTodayVO.setSortColumnName(request.getParameter("sort"));
		caseDueTodayVO.setSortType(request.getParameter("dir"));
		return this.tabularReportDAO.caseDueToday(caseDueTodayVO);
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return this.tabularReportDAO.caseDueTodayCount();
	}
}