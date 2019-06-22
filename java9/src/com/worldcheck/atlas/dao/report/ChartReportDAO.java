package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.dashboard.DashBoardReportVO;
import com.worldcheck.atlas.vo.report.InvoiceTATReportVO;
import com.worldcheck.atlas.vo.report.MyPerformanceReportVO;
import com.worldcheck.atlas.vo.report.OfficePerformanceVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ChartReportDAO extends SqlMapClientTemplate {
	private static final String GET_PROFIT_REPORT_DATA_SQL = "Reports.getProfitReport";
	private static final String GET_EXPENDITURE_REPORT_DATA_SQL = "Reports.getExpenditureReport";
	private static final String GET_TOP_CLIENT_DATA_SQL = "Reports.getRevenueSummaryTopClient";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.report.ChartReportDAO");

	public List<DashBoardReportVO> getProfitReportData(HashMap<String, Integer> paramList) throws CMSException {
		this.logger.debug("in ChartReportDAO :: getProfitReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Reports.getProfitReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting ChartReportDAO :: getProfitReportData");
		return resultSetObj;
	}

	public List<DashBoardReportVO> getExpenditureReportData(HashMap<String, Integer> paramList) throws CMSException {
		this.logger.debug("in ChartReportDAO :: getExpenditureReportData");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("Reports.getExpenditureReport", paramList);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting ChartReportDAO :: getExpenditureReportData");
		return resultSetObj;
	}

	public List<InvoiceTATReportVO> fetchInvoiceTATReport(HashMap<String, String> hmap) throws CMSException {
		this.logger.debug("In fetchInvoiceTATReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("FinanceOverview.invoiceTATReport", hmap);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ChartReportDAO :: fetchInvoiceTATReport");
		return resultSetObj;
	}

	public List<OfficePerformanceVO> fetchRevenueByOfficePerformanceReport(String year) throws CMSException {
		this.logger.debug("In fetchRevenueByOfficePerformanceReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("OfficePerformance.Revenue", year);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ChartReportDAO :: fetchRevenueByOfficePerformanceReport" + resultSetObj.size());
		return resultSetObj;
	}

	public List<OfficePerformanceVO> fetchRevenueAndAnalystByOfficePerformanceReport(String year) throws CMSException {
		this.logger.debug("In fetchRevenueAndAnalystByOfficePerformanceReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("OfficePerformance.Revenue", year);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug(
				"Exiting ChartReportDAO :: fetchRevenueAndAnalystByOfficePerformanceReport" + resultSetObj.size());
		return resultSetObj;
	}

	public List productivityPointByOfficePerReport(String year) throws CMSException {
		this.logger.debug("In productivityPointByOfficePerReportt");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("OfficePerformance.Point", year);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ChartReportDAO :: productivityPointByOfficePerReport" + resultSetObj.size());
		return resultSetObj;
	}

	public List productivityPointAnalystByOfficePerReport(String year) throws CMSException {
		this.logger.debug("In productivityPointAnalystByOfficePerReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("OfficePerformance.PointAnalyst", year);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ChartReportDAO :: productivityPointByOfficePerReport" + resultSetObj.size());
		return resultSetObj;
	}

	public List completedCaseByOfficePerReport(String year) throws CMSException {
		this.logger.debug("In productivityPointAnalystByOfficePerReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("OfficePerformance.case", year);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ChartReportDAO :: productivityPointByOfficePerReport" + resultSetObj.size());
		return resultSetObj;
	}

	public List completedCaseAnalystByOfficePerReport(String year) throws CMSException {
		this.logger.debug("In productivityPointAnalystByOfficePerReport");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("OfficePerformance.caseAnalyst", year);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting ChartReportDAO :: productivityPointByOfficePerReport" + resultSetObj.size());
		return resultSetObj;
	}

	public List<MyPerformanceReportVO> getNumberOfCasesMyPerformance(MyPerformanceReportVO myPerformanceReportVO)
			throws CMSException {
		this.logger.debug("in ChartReportDAO :: getNumberOfCasesMyPerformance");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("MyPerformance.noOfCases", myPerformanceReportVO);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting ChartReportDAO :: getNumberOfCasesMyPerformance");
		return resultSetObj;
	}
}