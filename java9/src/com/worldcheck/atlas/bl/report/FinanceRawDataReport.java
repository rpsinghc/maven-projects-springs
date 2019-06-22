package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.FinanceRawDataVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;

public class FinanceRawDataReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.FinanceRawDataReport");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private static final String CASE_REC_START_DATE = "caseRecStartDate";
	private static final String CASE_REC_END_DATE = "caseRecEndDate";
	private static final String CLIENT_FINAL_START_DATE = "clientFinalSentStartDate";
	private static final String CLIENT_FINAL_END_DATE = "clientFinalSentEndDate";
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		new ArrayList();
		this.logger.debug("in FinanceRawDataReport");
		HashMap<String, String> paramList = this.setParamaters(request);
		List<FinanceRawDataVO> reportResult = this.tabularReportDAO.fetchFinanceRawDataReport(request, response,
				paramList);
		this.logger.debug("Exiting FinanceRawDataReport");
		return reportResult;
	}

	private HashMap<String, String> setParamaters(HttpServletRequest request) throws CMSException {
		HashMap<String, String> paramList = new HashMap();
		String caseRecStartDate = request.getParameter("caseRecStartDate");
		String caseRecEndDate = request.getParameter("caseRecEndDate");
		String clientFinalStartDate = request.getParameter("clientFinalSentStartDate");
		String clientFinalEndDate = request.getParameter("clientFinalSentEndDate");

		try {
			if (request.getParameter("caseRecStartDate") == null && request.getParameter("caseRecEndDate") == null
					&& request.getParameter("clientFinalSentStartDate") == null
					&& request.getParameter("clientFinalSentEndDate") == null) {
				throw new NullPointerException();
			} else {
				if (caseRecStartDate != null && !caseRecStartDate.isEmpty()) {
					paramList.put("caseRecStartDate", sdf1.format(sdf.parse(caseRecStartDate)));
				} else {
					paramList.put("caseRecStartDate", "");
				}

				if (caseRecEndDate != null && !caseRecEndDate.isEmpty()) {
					paramList.put("caseRecEndDate", sdf1.format(sdf.parse(caseRecEndDate)));
				} else {
					paramList.put("caseRecEndDate", "");
				}

				if (clientFinalStartDate != null && !clientFinalStartDate.isEmpty()) {
					paramList.put("clientFinalSentStartDate", sdf1.format(sdf.parse(clientFinalStartDate)));
				} else {
					paramList.put("clientFinalSentStartDate", "");
				}

				if (clientFinalEndDate != null && !clientFinalEndDate.isEmpty()) {
					paramList.put("clientFinalSentEndDate", sdf1.format(sdf.parse(clientFinalEndDate)));
				} else {
					paramList.put("clientFinalSentEndDate", "");
				}

				return paramList;
			}
		} catch (ParseException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		HashMap<String, String> paramList = this.setParamaters(request);
		int result = this.fetchFinanceRawDataRowCount(paramList);
		return result;
	}

	private int fetchFinanceRawDataRowCount(HashMap<String, String> paramList) throws CMSException {
		this.logger.debug("in FinanceRawDataReport:: fetchFinanceRawDataRowCount");

		int result;
		try {
			result = this.tabularReportDAO.fetchFinanceRawDataRowCount(paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting FinanceRawDataReport::fetchFinanceRawDataRowCount");
		return result;
	}
}