package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.CurrentAnalystLoadingVO;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CurrentAnalystLoadingReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.report.CurrentAnalystLoadingReport");
	private TabularReportDAO tabularReportDAO = null;
	private String OFFICE = "office";
	private String START_DATE = "startDate";
	private String END_DATE = "endDate";
	private String START_MONTH = "startMonth";
	private String END_MONTH = "endMonth";
	private String START_YEAR = "startYear";
	private String END_YEAR = "endYear";
	private String REPORT_TYPE = "reportType";

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List<CurrentAnalystLoadingVO> reportResult = null;
		this.logger.debug("in CurrentAnalystLoadingReport");

		try {
			this.logger.debug("in request params - Manager:: " + request.getParameter(this.OFFICE) + " :: "
					+ request.getParameter(this.START_DATE) + " :: " + request.getParameter(this.END_DATE) + "::"
					+ request.getParameter("reportType"));
			HashMap<String, Object> paramMap = new HashMap();
			String startDate = request.getParameter(this.START_DATE);
			String endDate = request.getParameter(this.END_DATE);
			paramMap.put(this.OFFICE, request.getParameter(this.OFFICE));
			paramMap.put(this.START_DATE, startDate);
			paramMap.put(this.END_DATE, endDate);
			paramMap.put(this.REPORT_TYPE, request.getParameter("reportType"));
			reportResult = this.tabularReportDAO.fetchCurrentAnalystLoadingReport(paramMap);
			this.logger.debug("reportResult size :: " + reportResult.size());
			return reportResult;
		} catch (PatternSyntaxException var7) {
			throw new CMSException(this.logger, var7);
		} catch (NullPointerException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}

	public String fetchPublicHolidays(HashMap<String, Object> hmap) {
		String phd = this.tabularReportDAO.fetchPublicHolidays(hmap);
		return phd;
	}
}