package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;

public class AtlasReportFactory {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.AtlasReportFactory");
	private TimeTrackerReport timeTrackerReport = null;
	private ProductivityAndRevenueSummaryReport productivityAndRevenueSummaryReport = null;
	private CasesReceivedSummaryReport casesReceivedSummaryReport = null;
	private UtilizationByRevenueReport utilizationByRevenueReport = null;
	private VendorDataSummaryReport vendorDataSummaryReport = null;
	private UtilizationByProductivityReport utilizationByProductivityReport = null;
	private CurrentAnalystLoadingReport currentAnalystLoadingReport = null;
	private InvoiceCTExcelReport invoiceCTExcelReport;
	private FinanceRawDataReport financeRawDataReport;
	private ReviewerRawDataReport reviewerRawDataReport;
	private OfficeSummaryReport officeSummaryReport;
	private VendorRawDataReport vendorRawDataReport;
	private ProductivityPointByOfficePerReport productivityPointByOfficePerReport;
	private ProductivityPointAnalystByOfficeReport productivityPointAnalystByOfficeReport;
	private CompletedCaseByOfficeReport completedCaseByOfficeReport;
	private CompletedCaseAnalystByOfficeReport completedCaseAnalystByOfficeReport;
	private RevenueByOfficePerformanceReport revenueByOfficePerformanceReport;
	private RevenueAndAnalystByOfficePerformanceReport revenueAndAnalystByOfficePerformanceReport;
	private ReportTypeAndOfficeSummaryManager reportTypeAndOfficeSummaryManager;
	private InvoiceTATReportManager invoiceTATReportManager;
	private OverdueReportMultiactionManager overdueReportMultiactionManager;
	private CaseDueTodayMultiactionManager caseDueTodayMultiactionManager;
	private AllOnHoldMultiactionManager allOnHoldMultiactionManager;
	private MyPerformanceMultiactionManager myPerformanceMultiactionManager;
	private ScoreSheetMISReportManager scoreSheetMISReportManager;
	private RevenueSummaryManager revenueSummaryManager;
	private TeamJLPSummaryManager teamJLPSummaryManager;
	private TeamPerformanceReport teamPerformanceReport;

	public void setTeamJLPSummaryManager(TeamJLPSummaryManager teamJLPSummaryManager) {
		this.teamJLPSummaryManager = teamJLPSummaryManager;
	}

	public void setRevenueSummaryManager(RevenueSummaryManager revenueSummaryManager) {
		this.revenueSummaryManager = revenueSummaryManager;
	}

	public void setMyPerformanceMultiactionManager(MyPerformanceMultiactionManager myPerformanceMultiactionManager) {
		this.myPerformanceMultiactionManager = myPerformanceMultiactionManager;
	}

	public void setAllOnHoldMultiactionManager(AllOnHoldMultiactionManager allOnHoldMultiactionManager) {
		this.allOnHoldMultiactionManager = allOnHoldMultiactionManager;
	}

	public void setCaseDueTodayMultiactionManager(CaseDueTodayMultiactionManager caseDueTodayMultiactionManager) {
		this.caseDueTodayMultiactionManager = caseDueTodayMultiactionManager;
	}

	public void setOverdueReportMultiactionManager(OverdueReportMultiactionManager overdueReportMultiactionManager) {
		this.overdueReportMultiactionManager = overdueReportMultiactionManager;
	}

	public void setInvoiceTATReportManager(InvoiceTATReportManager invoiceTATReportManager) {
		this.invoiceTATReportManager = invoiceTATReportManager;
	}

	public void setReportTypeAndOfficeSummaryManager(
			ReportTypeAndOfficeSummaryManager reportTypeAndOfficeSummaryManager) {
		this.reportTypeAndOfficeSummaryManager = reportTypeAndOfficeSummaryManager;
	}

	public void setRevenueAndAnalystByOfficePerformanceReport(
			RevenueAndAnalystByOfficePerformanceReport revenueAndAnalystByOfficePerformanceReport) {
		this.revenueAndAnalystByOfficePerformanceReport = revenueAndAnalystByOfficePerformanceReport;
	}

	public void setRevenueByOfficePerformanceReport(RevenueByOfficePerformanceReport revenueByOfficePerformanceReport) {
		this.revenueByOfficePerformanceReport = revenueByOfficePerformanceReport;
	}

	public void setCurrentAnalystLoadingReport(CurrentAnalystLoadingReport currentAnalystLoadingReport) {
		this.currentAnalystLoadingReport = currentAnalystLoadingReport;
	}

	public void setUtilizationByProductivityReport(UtilizationByProductivityReport utilizationByProductivityReport) {
		this.utilizationByProductivityReport = utilizationByProductivityReport;
	}

	public void setTimeTrackerReport(TimeTrackerReport timeTrackerReport) {
		this.timeTrackerReport = timeTrackerReport;
	}

	public void setProductivityAndRevenueSummaryReport(
			ProductivityAndRevenueSummaryReport productivityAndRevenueSummaryReport) {
		this.productivityAndRevenueSummaryReport = productivityAndRevenueSummaryReport;
	}

	public void setCasesReceivedSummaryReport(CasesReceivedSummaryReport casesReceivedSummaryReport) {
		this.casesReceivedSummaryReport = casesReceivedSummaryReport;
	}

	public void setUtilizationByRevenueReport(UtilizationByRevenueReport utilizationByRevenueReport) {
		this.utilizationByRevenueReport = utilizationByRevenueReport;
	}

	public void setVendorDataSummaryReport(VendorDataSummaryReport vendorDataSummaryReport) {
		this.vendorDataSummaryReport = vendorDataSummaryReport;
	}

	public void setInvoiceCTExcelReport(InvoiceCTExcelReport invoiceCTExcelReport) {
		this.invoiceCTExcelReport = invoiceCTExcelReport;
	}

	public void setFinanceRawDataReport(FinanceRawDataReport financeRawDataReport) {
		this.financeRawDataReport = financeRawDataReport;
	}

	public void setReviewerRawDataReport(ReviewerRawDataReport reviewerRawDataReport) {
		this.reviewerRawDataReport = reviewerRawDataReport;
	}

	public void setOfficeSummaryReport(OfficeSummaryReport officeSummaryReport) {
		this.officeSummaryReport = officeSummaryReport;
	}

	public void setVendorRawDataReport(VendorRawDataReport vendorRawDataReport) {
		this.vendorRawDataReport = vendorRawDataReport;
	}

	public void setProductivityPointByOfficePerReport(
			ProductivityPointByOfficePerReport productivityPointByOfficePerReport) {
		this.productivityPointByOfficePerReport = productivityPointByOfficePerReport;
	}

	public void setProductivityPointAnalystByOfficeReport(
			ProductivityPointAnalystByOfficeReport productivityPointAnalystByOfficeReport) {
		this.productivityPointAnalystByOfficeReport = productivityPointAnalystByOfficeReport;
	}

	public void setCompletedCaseByOfficeReport(CompletedCaseByOfficeReport completedCaseByOfficeReport) {
		this.completedCaseByOfficeReport = completedCaseByOfficeReport;
	}

	public void setCompletedCaseAnalystByOfficeReport(
			CompletedCaseAnalystByOfficeReport completedCaseAnalystByOfficeReport) {
		this.completedCaseAnalystByOfficeReport = completedCaseAnalystByOfficeReport;
	}

	public void setScoreSheetMISReportManager(ScoreSheetMISReportManager scoreSheetMISReportManager) {
		this.scoreSheetMISReportManager = scoreSheetMISReportManager;
	}

	public void setTeamPerformanceReport(TeamPerformanceReport teamPerformanceReport) {
		this.teamPerformanceReport = teamPerformanceReport;
	}

	public IAtlasReport getReportObject(String reportName) {
		IAtlasReport repObj = null;
		this.logger.debug("reportName in factory :: " + reportName);
		if (null != reportName && !reportName.equals("")) {
			if (!reportName.equalsIgnoreCase("timetrackerdatasummary")
					&& !reportName.equalsIgnoreCase("timetrackerrawdata")) {
				if (reportName.equalsIgnoreCase("productivityrevenuesummary")) {
					repObj = this.productivityAndRevenueSummaryReport;
				} else if (reportName.equalsIgnoreCase("casesreceivedsummary")) {
					repObj = this.casesReceivedSummaryReport;
				} else if (reportName.equalsIgnoreCase("utilizationbyrevenue")) {
					repObj = this.utilizationByRevenueReport;
				} else if (reportName.equalsIgnoreCase("vendordatasummary")) {
					repObj = this.vendorDataSummaryReport;
				} else if (reportName.equalsIgnoreCase("utilizationproductivitypointscases")) {
					repObj = this.utilizationByProductivityReport;
				} else if (reportName.equalsIgnoreCase("currentanalystloading")) {
					repObj = this.currentAnalystLoadingReport;
				} else if (reportName.equalsIgnoreCase("invoiceCTExcelReport")) {
					repObj = this.invoiceCTExcelReport;
				} else if (reportName.equalsIgnoreCase("financeRawDataReport")) {
					repObj = this.financeRawDataReport;
				} else if (reportName.equalsIgnoreCase("reviewerRawDataReport")) {
					repObj = this.reviewerRawDataReport;
				} else if (reportName.equalsIgnoreCase("OfficeSummaryReport")) {
					repObj = this.officeSummaryReport;
				} else if (reportName.equalsIgnoreCase("vendorrawdata")) {
					repObj = this.vendorRawDataReport;
				} else if (reportName.equalsIgnoreCase("RevenueByOfficePerformance")) {
					repObj = this.revenueByOfficePerformanceReport;
				} else if (reportName.equalsIgnoreCase("revenueAndAnalystByOfficePerformance")) {
					repObj = this.revenueAndAnalystByOfficePerformanceReport;
				} else if (reportName.equalsIgnoreCase("reportTypeAndOfficeSummary")) {
					repObj = this.reportTypeAndOfficeSummaryManager;
				} else if (reportName.equalsIgnoreCase("invoiceTATReport")) {
					repObj = this.invoiceTATReportManager;
				} else if (reportName.equalsIgnoreCase("overdueReport")) {
					repObj = this.overdueReportMultiactionManager;
				} else if (reportName.equalsIgnoreCase("caseDueTodayReport")) {
					repObj = this.caseDueTodayMultiactionManager;
				} else if (reportName.equalsIgnoreCase("allOnHoldReport")) {
					repObj = this.allOnHoldMultiactionManager;
				} else if (reportName.equalsIgnoreCase("productivityPointByOffice")) {
					repObj = this.productivityPointByOfficePerReport;
				} else if (reportName.equalsIgnoreCase("productivityPointAnalystByOffice")) {
					repObj = this.productivityPointAnalystByOfficeReport;
				} else if (reportName.equalsIgnoreCase("completedCaseByOffice")) {
					repObj = this.completedCaseByOfficeReport;
				} else if (reportName.equalsIgnoreCase("completedCaseAnalystByOffice")) {
					repObj = this.completedCaseAnalystByOfficeReport;
				} else if (reportName.equalsIgnoreCase("analystScoringPerformanceScoreSheetMIS")) {
					repObj = this.scoreSheetMISReportManager;
				} else if (reportName.equalsIgnoreCase("myPerformanceReport")) {
					repObj = this.myPerformanceMultiactionManager;
				} else if (reportName.equalsIgnoreCase("FinanceRevenueSummary")) {
					repObj = this.revenueSummaryManager;
				} else if (reportName.equalsIgnoreCase("teamJLPSummary")) {
					repObj = this.teamJLPSummaryManager;
				} else if (reportName.equalsIgnoreCase("TeamPerformanceReport")) {
					repObj = this.teamPerformanceReport;
				}
			} else {
				repObj = this.timeTrackerReport;
			}
		}

		return (IAtlasReport) repObj;
	}
}