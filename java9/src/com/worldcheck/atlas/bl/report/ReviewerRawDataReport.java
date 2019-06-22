package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.ReviewerRawDataVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;

public class ReviewerRawDataReport implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.FinanceRawDataReport");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private static final String CASE_REC_START_DATE = "caseRecStartDate";
	private static final String CASE_REC_END_DATE = "caseRecEndDate";
	private static final String SELECTED_OFFICES = "officeList";
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		new ArrayList();
		this.logger.debug("in ReviewerRawDataReport");
		HashMap<String, Object> paramList = this.setParamaters(request);
		List<ReviewerRawDataVO> reportResult = this.tabularReportDAO.fetchReviewerRawDataReport(request, response,
				paramList);
		this.logger.debug("Exiting ReviewerRawDataReport");
		return reportResult;
	}

	private HashMap<String, Object> setParamaters(HttpServletRequest request) throws CMSException {
		HashMap<String, Object> paramList = new HashMap();
		String caseRecStartDate = request.getParameter("caseRecStartDate");
		String caseRecEndDate = request.getParameter("caseRecEndDate");
		String selectedOffice = request.getParameter("officeList");
		new ArrayList();
		List officeList = StringUtils.commaSeparatedStringToList(selectedOffice);

		try {
			if (request.getParameter("caseRecStartDate") == null && request.getParameter("caseRecEndDate") == null) {
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

				if (selectedOffice != null) {
					paramList.put("officeList", officeList);
				} else {
					paramList.put("officeList", "");
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
		HashMap<String, Object> paramList = this.setParamaters(request);
		int result = this.fetchReviewerRawDataRowCount(paramList);
		return result;
	}

	private int fetchReviewerRawDataRowCount(HashMap<String, Object> paramList) throws CMSException {
		this.logger.debug("in ReviewerRawDataRowCount:: fetchReviewerRawDataRowCount");

		int result;
		try {
			result = this.tabularReportDAO.fetchReviewerRawDataRowCount(paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting  ReviewerRawDataRowCount:: fetchReviewerRawDataRowCount");
		return result;
	}
}