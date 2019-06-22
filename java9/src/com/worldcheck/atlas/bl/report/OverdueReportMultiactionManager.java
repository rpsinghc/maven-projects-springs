package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.OverdueReportVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OverdueReportMultiactionManager implements IAtlasReport {
	private String level = "level";
	private String office = "office";
	private String start = "start";
	private String limit = "limit";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.OverdueReportMultiactionManager");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		OverdueReportVO overdueReportVO = new OverdueReportVO();
		overdueReportVO.setOffice(request.getParameter(this.office));
		this.logger.debug("==========IN Manager of REport========>" + request.getParameter(this.office));
		int startVal = Integer.parseInt(request.getParameter(this.start)) + 1;
		int limitVal = Integer.parseInt(request.getParameter(this.start))
				+ Integer.parseInt(request.getParameter(this.limit));
		overdueReportVO.setStart(startVal);
		overdueReportVO.setLimit(limitVal);
		overdueReportVO.setSortColumnName(request.getParameter("sort"));
		overdueReportVO.setSortType(request.getParameter("dir"));
		return this.tabularReportDAO.fetchOverdue(request.getParameter(this.level), overdueReportVO);
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		OverdueReportVO overdueReportVO = new OverdueReportVO();
		overdueReportVO.setOffice(request.getParameter(this.office));
		return this.tabularReportDAO.fetchOverdueCount(request.getParameter(this.level), overdueReportVO);
	}
}