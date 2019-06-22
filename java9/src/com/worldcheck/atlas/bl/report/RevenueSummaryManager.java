package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.bl.downloadexcel.ChartExcelDownloader;
import com.worldcheck.atlas.dao.report.ChartReportDAO;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.Constants;
import com.worldcheck.atlas.vo.report.CaseRawDataFinanceVO;
import com.worldcheck.atlas.vo.report.RevenueSummaryVO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class RevenueSummaryManager implements IAtlasReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.RevenueSummaryManager");
	private static final String REPORT_TYPE = "reportType";
	private static final String SUB_REPORT_TYPE = "subReportType";
	private ChartReportDAO chartReportDAO = null;
	private TabularReportDAO tabularReportDAO;
	private static final String COMBINED_CASE_REVENUE = "combined";
	private static final String REPORT_TYPE_CASE_REVENUE = "reportType";
	private static final String SUB_REPORT_TYPE_CASE_REVENUE = "subReportType";
	private static final String DELIVERY_LEGENDS_EARLIER = "Earlier";
	private static final String DELIVERY_LEGENDS_ON_TIME = "On-Time";
	private static final String DELIVERY_LEGENDS_NOT_ON_TIME = "Not-On-Time";
	private static final String CASE_DATA_SERIES_NAME = "caseDataSetSeriesName";
	private static final String CASE_DATA_SET_LIST = "casesDatasetList";
	private static final String CASE_TYPE = "caseType";
	private static final String REVENUE_DATA_SERIES_NAME = "revenueDataSetSeriesName";
	private static final String REVENUE_DATA_SET_LIST = "revenueDatasetList";
	private static final String REVENUE_TYPE = "revenueType";
	private static final String PIE_CHART = "pieChart";
	private static final String BAR_CHART = "barChart";
	private static final String CASE_ARRAY = "caseArray";
	private static final String REVENUE_ARRAY = "revenueArray";
	private static final String NUMBER_OF_CASES = "Number of Cases";
	private static final String REVENUE_USD = "Revenue(USD)";
	private static int sizeOfReport = 1;
	private Map<String, String> colorMap = new HashMap();

	public void setChartReportDAO(ChartReportDAO chartReportDAO) {
		this.chartReportDAO = chartReportDAO;
	}

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List<Object> fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return null;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}

	public Map<String, Object> getDataRevenueSummaryFO(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		String isCancelled = "No";
		List<String> caseStatusWithoutCancelled = new ArrayList();
		new HashMap();
		if (!revenueSummaryVO.getSelectedClient().trim().isEmpty()) {
			revenueSummaryVO.setClientList(this.getFiltersList(revenueSummaryVO.getSelectedClient()));
		}

		if (!revenueSummaryVO.getSelectedCaseStatus().trim().isEmpty()) {
			String[] tempArray = revenueSummaryVO.getSelectedCaseStatus().split(",");
			String[] var9 = tempArray;
			int var8 = tempArray.length;

			for (int var7 = 0; var7 < var8; ++var7) {
				String str = var9[var7];
				if (!str.equalsIgnoreCase("Cancelled")) {
					caseStatusWithoutCancelled.add(str);
				} else {
					isCancelled = "Yes";
				}
			}
		}

		revenueSummaryVO.setCaseStatusList(caseStatusWithoutCancelled);
		revenueSummaryVO.setIsCancelled(isCancelled);
		if (!revenueSummaryVO.getSelectedReportType().trim().isEmpty()) {
			revenueSummaryVO.setReportTypeList(this.getFiltersList(revenueSummaryVO.getSelectedReportType()));
			this.logger.debug("****Report with report" + revenueSummaryVO.getReportTypeList());
		}

		if (!revenueSummaryVO.getSelectedSubReportTypeField().isEmpty()) {
			revenueSummaryVO
					.setSubReportTypeList(this.getFiltersList(revenueSummaryVO.getSelectedSubReportTypeField()));
			this.logger.debug("****Report with report" + revenueSummaryVO.getSubReportTypeList());
		}

		if (!revenueSummaryVO.getSelectedReportWithSubReportField().isEmpty()) {
			revenueSummaryVO.setReportWithSubReportList(
					this.getFiltersList(revenueSummaryVO.getSelectedReportWithSubReportField()));
			this.logger.debug("****Report with sub report" + revenueSummaryVO.getReportWithSubReportList());
		}

		if (!revenueSummaryVO.getSalesRepresentativeField().isEmpty()) {
			revenueSummaryVO
					.setSalesRepresentativeList(this.getFiltersList(revenueSummaryVO.getSalesRepresentativeField()));
			this.logger.debug("****Report with sales representative" + revenueSummaryVO.getSalesRepresentativeField());
		}

		if (!revenueSummaryVO.getExcludeChildField().isEmpty()) {
			revenueSummaryVO.setExcludeChildList(this.getFiltersList(revenueSummaryVO.getExcludeChildField()));
			this.logger.debug("****Report with child field" + revenueSummaryVO.getExcludeChildField());
		}

		this.setColorCode();
		Map controllerDataMap;
		if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByMonth")) {
			controllerDataMap = this.getCasesByMonth(revenueSummaryVO);
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")) {
			controllerDataMap = this.getCasesByCaseStatus(revenueSummaryVO);
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
			controllerDataMap = this.getCasesByReportType(revenueSummaryVO);
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByClients")) {
			controllerDataMap = this.getCasesByClientCode(revenueSummaryVO);
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByPrimarySubjectCountry")) {
			controllerDataMap = this.getCasesByPrimarySubjectCountry(revenueSummaryVO);
		} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataOnTimeDeliveryPercentage")) {
			controllerDataMap = this.getOntimeDeliveryPercentage(revenueSummaryVO);
		} else {
			controllerDataMap = this.getTATHistogramData(revenueSummaryVO);
		}

		return controllerDataMap;
	}

	private Map<String, Object> getOntimeDeliveryPercentage(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In manager on time delivery percentage");
		Map<String, Object> controllerDataMap = new HashMap();
		String[] deliveryDataSeriesName = new String[]{"Delivery Percentage"};
		String isDataPresent = "true";
		String fusionXMLStringCases = "";
		String caseTableData = "";
		new HashMap();
		List<RevenueSummaryVO> resultList = this.tabularReportDAO.getOntimeDeliveryPercentage(revenueSummaryVO);
		if (resultList.size() > 0) {
			Object[] obj;
			if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("reportType")) {
				obj = revenueSummaryVO.getReportTypeList().toArray();
				deliveryDataSeriesName = (String[]) Arrays.copyOf(obj, obj.length, String[].class);
				this.logger.debug("Array size::" + deliveryDataSeriesName);
			} else if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("subReportType")) {
				obj = revenueSummaryVO.getReportWithSubReportList().toArray();
				deliveryDataSeriesName = (String[]) Arrays.copyOf(obj, obj.length, String[].class);
				this.logger.debug("Array size::" + deliveryDataSeriesName);
			}

			Map<String, String> caseMap = this.createFusionGraphForDelivery(resultList, deliveryDataSeriesName,
					revenueSummaryVO.getHeaderFirstGraphField(), revenueSummaryVO.getXaxisFirstGraphField(),
					revenueSummaryVO.getYaxisFirstGraphField(), revenueSummaryVO.getLabelFirstGraphField(),
					revenueSummaryVO.getDisplayCriteriaField());
			fusionXMLStringCases = (String) caseMap.get("fusionXMLString");
			caseTableData = (String) caseMap.get("tableData");
		} else {
			isDataPresent = "false";
		}

		controllerDataMap.put("casesDataXMLString", fusionXMLStringCases);
		controllerDataMap.put("revenueDataXMLString", "");
		controllerDataMap.put("caseTable", caseTableData);
		controllerDataMap.put("revenueTable", "");
		controllerDataMap.put("isDataPresent", isDataPresent);
		return controllerDataMap;
	}

	private Map<String, String> createFusionGraphForDelivery(List<RevenueSummaryVO> dataList, String[] dataSeriesName,
			String caption, String xAxisName, String yAxisName, String showLabel, String chartType) {
		int label = 0;
		boolean flag = false;
		String temp = "";
		String deliveryPercentage = "";
		String datasetMain = "";
		String categories = "";
		String dataset = "";
		String datasetprefix = "";
		String row1 = "<tr><td>&nbsp;</td>";
		String row2 = "";
		String tableString = "<div class='overflowTable'><table class='top10DataTable'>";
		Map<String, String> resultMap = new HashMap();
		Map<String, String> dataMap = new HashMap();
		Map<String, Map<String, String>> superDataMap = new HashMap();
		String value = "";
		if (showLabel.equalsIgnoreCase("true")) {
			label = 1;
		}

		List<String> deliveryLegends = this.getDeliveryLegends();
		Iterator<String> delivery = deliveryLegends.iterator();
		RevenueSummaryVO rsVO;
		Iterator it;
		int i;
		if (!chartType.equalsIgnoreCase("combined")) {
			for (i = 0; i < dataSeriesName.length; ++i) {
				dataMap = new HashMap();
				delivery = deliveryLegends.iterator();

				while (delivery.hasNext()) {
					temp = (String) delivery.next();
					it = dataList.iterator();
					flag = false;

					while (it.hasNext()) {
						rsVO = (RevenueSummaryVO) it.next();
						if (temp.equalsIgnoreCase(rsVO.getGraphLabel())
								&& dataSeriesName[i].equalsIgnoreCase(rsVO.getReportType())) {
							deliveryPercentage = this.decimalConvertor(
									Float.parseFloat(rsVO.getDeliveryTypePercentage().trim()) * 100.0F);
							this.logger.debug("Value for ontime==" + deliveryPercentage);
							((Map) dataMap).put(temp, deliveryPercentage);
							flag = true;
							break;
						}
					}

					if (!flag) {
						((Map) dataMap).put(temp, "0.0");
					}
				}

				superDataMap.put(dataSeriesName[i], dataMap);
			}
		} else {
			while (delivery.hasNext()) {
				it = dataList.iterator();
				temp = (String) delivery.next();
				flag = false;

				while (it.hasNext()) {
					rsVO = (RevenueSummaryVO) it.next();
					if (temp.equalsIgnoreCase(rsVO.getGraphLabel())) {
						deliveryPercentage = this
								.decimalConvertor(Float.parseFloat(rsVO.getDeliveryTypePercentage().trim()) * 100.0F);
						this.logger.debug("Value for ontime==" + deliveryPercentage);
						((Map) dataMap).put(temp, deliveryPercentage);
						flag = true;
						break;
					}
				}

				if (!flag) {
					((Map) dataMap).put(temp, "0.0");
				}
			}
		}

		for (delivery = deliveryLegends.iterator(); delivery.hasNext(); row1 = row1 + "<td>" + temp + "</td>") {
			temp = (String) delivery.next();
			categories = categories + " <category label='" + temp + "'/>";
		}

		for (i = 0; i < dataSeriesName.length; ++i) {
			datasetprefix = "<dataset>";
			delivery = deliveryLegends.iterator();
			datasetMain = "";
			if (chartType.equalsIgnoreCase("combined")) {
				for (row2 = row2 + "<tr><td><span style='background-color:#" + (String) this.colorMap.get("combined")
						+ "' " + "class='revsumCaseBlue'></span>&nbsp;&nbsp;" + dataSeriesName[i] + "</td>"; delivery
								.hasNext(); datasetMain = datasetMain + " <set value='" + value + "' color='"
										+ (String) this.colorMap.get("combined") + "' toolText='" + dataSeriesName[i]
										+ ",&nbsp;&nbsp;" + value + "' link='' " + "/>") {
					temp = (String) delivery.next();
					value = (String) ((Map) dataMap).get(temp);
					row2 = row2 + "<td>" + value + "%</td>";
				}
			} else {
				row2 = row2 + "<tr><td><span style='background-color:#" + (String) this.colorMap.get(dataSeriesName[i])
						+ "' " + "class='revsumCaseBlue'></span>&nbsp;&nbsp;" + dataSeriesName[i] + "</td>";
				dataMap = (Map) superDataMap.get(dataSeriesName[i]);

				for (delivery = deliveryLegends.iterator(); delivery.hasNext(); datasetMain = datasetMain
						+ " <set value='" + value + "' color='" + (String) this.colorMap.get(dataSeriesName[i])
						+ "'toolText='" + dataSeriesName[i] + ",&nbsp;&nbsp;" + value + "' link='' " + "/>") {
					temp = (String) delivery.next();
					value = (String) ((Map) dataMap).get(temp);
					row2 = row2 + "<td>" + value + "%</td>";
				}
			}

			dataset = dataset + datasetprefix + datasetMain + "</dataset>";
			row1 = row1 + "</tr>";
			row2 = row2 + "</tr>";
		}

		tableString = tableString + row1 + row2 + "</table></div>";
		String fusionXMLString = "<chart showValues='" + label + "' showLabel='0' caption='" + caption + "' yAxisName='"
				+ yAxisName
				+ "'showLabel='0' bgColor='FFFFFF' labelDisplay='WRAP' showPercentValues='1' showPercentInToolTip = '1'  "
				+ "valuePadding='5' numDivLines='10'" + "numbersuffix ='%25'>" + "<categories>  " + categories
				+ " </categories>" + dataset + " <styles> " + "<definition> "
				+ "<style name='myCaptionFont' type='font' font='Calibri' size='18' bold='1' color='000600'/> "
				+ "<style name='xAxisFont' type='font' font='Calibri' size='11' bold='1' color='000600'/> "
				+ "<style name='yAxisFont' type='font' font='Calibri' size='10' bold='1' color='000600'/> "
				+ "</definition>" + " <application> " + "<apply toObject='Caption' styles='myCaptionFont'/> "
				+ "<apply toObject='XAXISNAME' styles='xAxisFont'/> "
				+ "<apply toObject='YAXISNAME' styles='yAxisFont'/> "
				+ "<apply toObject='DATAVALUES' styles='xAxisFont'/> " + "</application> " + "</styles>" + "</chart>";
		this.logger.debug("Fusion xml: " + fusionXMLString);
		resultMap.put("tableData", tableString);
		resultMap.put("fusionXMLString", fusionXMLString);
		return resultMap;
	}

	private Map<String, Object> getTATHistogramData(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In manager TAT Histogram");
		Map<String, Object> controllerDataMap = new HashMap();
		String isDataPresent = "true";
		String fusionXMLStringCases = "";
		String caseTableData = "";
		String statisticsTable = "";
		new HashMap();
		Map<String, List<RevenueSummaryVO>> resultMap = this.tabularReportDAO.getTATHistogramData(revenueSummaryVO);
		if (((List) resultMap.get("cases")).size() > 0) {
			Map<String, String> caseMap = this.createFusionGraph((List) resultMap.get("cases"),
					Constants.REVENUE_COLORS, Constants.CASEDATASERIESENAME,
					revenueSummaryVO.getHeaderFirstGraphField(), revenueSummaryVO.getXaxisFirstGraphField(),
					revenueSummaryVO.getYaxisFirstGraphField(), revenueSummaryVO.getLabelFirstGraphField(), "combined",
					revenueSummaryVO.getTabName());
			fusionXMLStringCases = (String) caseMap.get("fusionXMLString");
			caseTableData = (String) caseMap.get("tableData");
			RevenueSummaryVO rsVO = (RevenueSummaryVO) ((List) resultMap.get("invoiceTATReport")).get(0);
			statisticsTable = "<table class='top10DataTable'  style='width:200px;'><tr bgcolor='#FFC000'><td colspan='2'>Turn-Around-Time-Statistics</td></tr><tr><td>Minimum</td><td>"
					+ this.decimalConversionTwoPlaces((float) rsVO.getMinimumCaseValue()) + "</td></tr>"
					+ "<tr><td>Maximum</td><td>" + this.decimalConversionTwoPlaces((float) rsVO.getMaximumCaseValue())
					+ "</td></tr>" + "<tr><td>Average</td><td>"
					+ this.decimalConversionTwoPlaces(Float.parseFloat(rsVO.getAverageCaseValue().trim()))
					+ "</td></tr>" + "<tr><td>Range</td><td>"
					+ this.decimalConversionTwoPlaces((float) rsVO.getRangeCaseValue()) + "</td></tr>" + "</table>";
		} else {
			isDataPresent = "false";
		}

		controllerDataMap.put("casesDataXMLString", fusionXMLStringCases);
		controllerDataMap.put("revenueDataXMLString", "");
		controllerDataMap.put("caseTable", caseTableData);
		controllerDataMap.put("revenueTable", "");
		controllerDataMap.put("statisticsTable", statisticsTable);
		controllerDataMap.put("isDataPresent", isDataPresent);
		return controllerDataMap;
	}

	private Map<String, Object> getCasesByPrimarySubjectCountry(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In manager cases by primary subject country");
		Map<String, Object> controllerDataMap = new HashMap();
		String fusionXMLStringCases = "";
		String fusionXMLStringRevenue = "";
		String caseTableData = "";
		String revenueTableData = "";
		String isDataPresent = "true";
		Map<String, List<RevenueSummaryVO>> resultMap = this.tabularReportDAO
				.getCasesByPrimarySubjectCountry(revenueSummaryVO);
		new HashMap();
		if (((List) resultMap.get("cases")).size() > 0) {
			Map<String, String> caseMap = this.createFusionGraph((List) resultMap.get("cases"), Constants.CASE_COLORS,
					Constants.CASEDATASERIESENAME, revenueSummaryVO.getHeaderFirstGraphField(),
					revenueSummaryVO.getXaxisFirstGraphField(), revenueSummaryVO.getYaxisFirstGraphField(),
					revenueSummaryVO.getLabelFirstGraphField(), "combined", revenueSummaryVO.getTabName());
			fusionXMLStringCases = (String) caseMap.get("fusionXMLString");
			caseTableData = (String) caseMap.get("tableData");
			caseMap = this.createFusionGraph((List) resultMap.get("revenue"), Constants.REVENUE_COLORS,
					Constants.REVENUEDATASETSERIES, revenueSummaryVO.getHeaderSecondGraphField(),
					revenueSummaryVO.getXaxisSecondGraphField(), revenueSummaryVO.getYaxisSecondGraphField(),
					revenueSummaryVO.getLabelSecondGraphField(), "revenue", revenueSummaryVO.getTabName());
			fusionXMLStringRevenue = (String) caseMap.get("fusionXMLString");
			revenueTableData = (String) caseMap.get("tableData");
		} else {
			isDataPresent = "false";
		}

		controllerDataMap.put("casesDataXMLString", fusionXMLStringCases);
		controllerDataMap.put("revenueDataXMLString", fusionXMLStringRevenue);
		controllerDataMap.put("caseTable", caseTableData);
		controllerDataMap.put("revenueTable", revenueTableData);
		controllerDataMap.put("isDataPresent", isDataPresent);
		return controllerDataMap;
	}

	private Map<String, String> createFusionGraph(List<RevenueSummaryVO> dataList, String[] colors,
			String[] dataSeriesName, String caption, String xAxisName, String yAxisName, String showLabel,
			String chartType, String tabName) {
		String attributes = "";
		int label = 0;
		String temp = "";
		String color = "";
		String datasetMain = "";
		String plotSpacePercent = "";
		String categories = "";
		String dataset = "";
		String datasetprefix = "";
		String row1 = "<tr><td>&nbsp;</td>";
		String row2 = "";
		String tableString = "<div class='overflowTable'><table class='top10DataTable'>";
		String tempValue = "";
		Map<String, String> resultMap = new HashMap();
		if (chartType.equalsIgnoreCase("revenue")) {
			attributes = "numberPrefix ='$'";
		}

		if (showLabel.equalsIgnoreCase("true")) {
			label = 1;
		}

		if (tabName.equalsIgnoreCase("dataTatHistogram")) {
			plotSpacePercent = "plotSpacePercent='0'";
		}

		Iterator it;
		for (it = dataList.iterator(); it.hasNext(); row1 = row1 + "<td>" + temp + "</td>") {
			temp = ((RevenueSummaryVO) it.next()).getGraphLabel();
			categories = categories + " <category label='" + temp + "'/>";
		}

		if (tabName.equalsIgnoreCase("dataTatHistogram")) {
			color = (String) this.colorMap.get("revenue");
		} else if (chartType.equalsIgnoreCase("revenue")) {
			color = (String) this.colorMap.get("revenue");
		} else {
			color = (String) this.colorMap.get("combined");
		}

		for (int i = 0; i < dataSeriesName.length; ++i) {
			datasetprefix = "<dataset>";
			row2 = row2 + "<tr><td><span style='background-color:#" + color + "' "
					+ "class='revsumCaseBlue'></span>&nbsp;&nbsp;" + dataSeriesName[i] + "</td>";
			it = dataList.iterator();
			RevenueSummaryVO rsVO;
			if (chartType.equalsIgnoreCase("combined")) {
				if (tabName.equalsIgnoreCase("dataTatHistogram")) {
					color = (String) this.colorMap.get("revenue");
				} else {
					color = (String) this.colorMap.get("combined");
				}

				while (it.hasNext()) {
					rsVO = (RevenueSummaryVO) it.next();
					row2 = row2 + "<td>" + this.numberToThousandSeparator((float) rsVO.getNumberOfCases()) + "</td>";
					datasetMain = datasetMain + " <set value='" + rsVO.getNumberOfCases() + "' color='" + color
							+ "' toolText='" + dataSeriesName[i] + ",&nbsp;&nbsp;" + rsVO.getNumberOfCases()
							+ "' link='' " + "/>";
				}
			} else {
				while (it.hasNext()) {
					rsVO = (RevenueSummaryVO) it.next();
					tempValue = this.numberToThousandSeparator(rsVO.getRevenue());
					row2 = row2 + "<td>$" + tempValue + "</td>";
					datasetMain = datasetMain + " <set value='" + tempValue + "' color='"
							+ (String) this.colorMap.get("revenue") + "' toolText='" + dataSeriesName[i]
							+ ",&nbsp;&nbsp;" + tempValue + "' link='' " + "/>";
				}
			}

			dataset = dataset + datasetprefix + datasetMain + "</dataset>";
		}

		row1 = row1 + "</tr>";
		row2 = row2 + "</tr>";
		tableString = tableString + row1 + row2 + "</table></div>";
		this.logger.debug("label::" + label);
		String fusionXMLString = "<chart showValues='" + label + "' showLabel='0' caption='" + caption + "' xAxisName='"
				+ xAxisName + "' yAxisName='" + yAxisName
				+ "'showLabel='0' bgColor='FFFFFF' labelDisplay='WRAP' formatNumberScale='0'  valuePadding='5' numDivLines='10'"
				+ " thousandSeparatorPosition='1' inThousandSeparator=',' " + plotSpacePercent + attributes + ">"
				+ "<categories>  " + categories + " </categories>" + dataset + " <styles> " + "<definition> "
				+ "<style name='myCaptionFont' type='font' font='Calibri' size='18' bold='1' color='000600'/> "
				+ "<style name='xAxisFont' type='font' font='Calibri' size='11' bold='1' color='000600'/> "
				+ "<style name='yAxisFont' type='font' font='Calibri' size='10' bold='1' color='000600'/> "
				+ "</definition>" + " <application> " + "<apply toObject='Caption' styles='myCaptionFont'/> "
				+ "<apply toObject='XAXISNAME' styles='xAxisFont'/> "
				+ "<apply toObject='YAXISNAME' styles='yAxisFont'/> "
				+ "<apply toObject='DATAVALUES' styles='xAxisFont'/> " + "</application> " + "</styles>" + "</chart>";
		resultMap.put("tableData", tableString);
		resultMap.put("fusionXMLString", fusionXMLString);
		return resultMap;
	}

	public String exportChartAndDataToExcel(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) {
		String isCancelled = "No";
		List<String> listofReport = new ArrayList();
		List<String> caseStatusWithoutCancelled = new ArrayList();
		String fileName = null;
		List<String> lstMonth = null;
		List<String> lstDeliveryStatus = null;
		String displayCriteria = null;
		new ArrayList();
		new HashMap();
		new ArrayList();
		this.logger.debug("in exportToExcelRevenueChartData:::");

		try {
			if (!revenueSummaryVO.getSelectedClient().trim().isEmpty()) {
				revenueSummaryVO.setClientList(this.getFiltersList(revenueSummaryVO.getSelectedClient()));
			}

			if (!revenueSummaryVO.getSelectedCaseStatus().trim().isEmpty()) {
				String[] tempArray = revenueSummaryVO.getSelectedCaseStatus().split(",");
				String[] var18 = tempArray;
				int var17 = tempArray.length;

				for (int var16 = 0; var16 < var17; ++var16) {
					String str = var18[var16];
					if (!str.equals("Cancelled")) {
						caseStatusWithoutCancelled.add(str);
					} else {
						isCancelled = "Yes";
					}
				}
			}

			revenueSummaryVO.setCaseStatusList(caseStatusWithoutCancelled);
			revenueSummaryVO.setIsCancelled(isCancelled);
			if (!revenueSummaryVO.getSelectedReportType().trim().isEmpty()) {
				revenueSummaryVO.setReportTypeList(this.getFiltersList(revenueSummaryVO.getSelectedReportType()));
				this.logger.debug("****Report with report" + revenueSummaryVO.getReportTypeList());
			}

			if (!revenueSummaryVO.getSelectedSubReportTypeField().isEmpty()) {
				revenueSummaryVO
						.setSubReportTypeList(this.getFiltersList(revenueSummaryVO.getSelectedSubReportTypeField()));
				this.logger.debug("****Report with report" + revenueSummaryVO.getSubReportTypeList());
			}

			if (!revenueSummaryVO.getSelectedReportWithSubReportField().isEmpty()) {
				revenueSummaryVO.setReportWithSubReportList(
						this.getFiltersList(revenueSummaryVO.getSelectedReportWithSubReportField()));
				this.logger.debug("****Report with sub report" + revenueSummaryVO.getReportWithSubReportList());
			}

			if (!revenueSummaryVO.getSalesRepresentativeField().isEmpty()) {
				revenueSummaryVO.setSalesRepresentativeList(
						this.getFiltersList(revenueSummaryVO.getSalesRepresentativeField()));
				this.logger
						.debug("****Report with sales representative" + revenueSummaryVO.getSalesRepresentativeField());
			}

			if (!revenueSummaryVO.getExcludeChildField().isEmpty()) {
				revenueSummaryVO.setExcludeChildList(this.getFiltersList(revenueSummaryVO.getExcludeChildField()));
				this.logger.debug("****Report with child field" + revenueSummaryVO.getExcludeChildField());
			}

			List crdDataList = this.fetchCrdReport(request, response, revenueSummaryVO);
			List revenueChartDataList;
			if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByMonth")) {
				this.logger.debug("RevenueSummaryManager###First Tab");
				revenueChartDataList = this.fetchReport(request, response, revenueSummaryVO);
				RevenueSummaryVO rv = (RevenueSummaryVO) revenueChartDataList.get(0);
				RevenueSummaryVO rvLast = (RevenueSummaryVO) revenueChartDataList.get(revenueChartDataList.size() - 1);
				String startMonthFromDB = "01-" + rv.getMonth();
				String startMonthFromUI = revenueSummaryVO.getStartDateField();
				String endMonthFromDB = "01-" + rvLast.getMonth();
				String endMonthFromUI = revenueSummaryVO.getEndDateField();
				String resultStartMonth = "";
				String resultEndMonth = "";
				this.logger
						.debug("startMonthFromDB::" + startMonthFromDB + "######startMonthFromUI" + startMonthFromUI);
				this.logger.debug("endMonthFromDB::" + endMonthFromDB + "######endMonthFromUI" + endMonthFromUI);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
				Date d1 = sdf.parse(startMonthFromDB);
				Date d2 = sdf.parse(startMonthFromUI);
				Date d3 = sdf.parse(endMonthFromDB);
				Date d4 = sdf.parse(endMonthFromUI);
				this.logger.debug("d1 time::" + d1.getTime() + "###### d2 Time:::" + d2.getTime());
				this.logger.debug("d3 time::" + d3.getTime() + "###### d4 Time:::" + d4.getTime());
				if (d1.getTime() <= d2.getTime()) {
					resultStartMonth = startMonthFromDB;
				} else {
					resultStartMonth = revenueSummaryVO.getStartDateField();
				}

				if (d3.getTime() >= d4.getTime()) {
					resultEndMonth = endMonthFromDB;
				} else {
					resultEndMonth = revenueSummaryVO.getEndDateField();
				}

				this.logger.debug("resultStartMonth::" + resultStartMonth);
				this.logger.debug("resultEndMonth::" + resultEndMonth);
				lstMonth = getListOfMonth(resultStartMonth, resultEndMonth);
				displayCriteria = revenueSummaryVO.getDisplayCriteriaField();
				if (displayCriteria.equalsIgnoreCase("reportType")) {
					sizeOfReport = revenueSummaryVO.getReportTypeList().size();
					listofReport = revenueSummaryVO.getReportTypeList();
				} else if (displayCriteria.equalsIgnoreCase("subReportType")) {
					sizeOfReport = revenueSummaryVO.getReportWithSubReportList().size();
					listofReport = revenueSummaryVO.getReportWithSubReportList();
					this.logger.debug("Sub report type list size::" + sizeOfReport);
					this.logger.debug("Sub report type list ::" + listofReport);
				} else {
					sizeOfReport = 1;
				}

				this.logger.debug("Combined chart execute");
				this.logger.debug("Size of report for first tab::" + sizeOfReport);
				fileName = this.writeToExcel(revenueChartDataList, crdDataList, response, revenueSummaryVO, lstMonth,
						(List) listofReport, sizeOfReport, displayCriteria);
			} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")) {
				this.logger.debug("RevenueSummaryManager###Second Tab");
				revenueChartDataList = this.fetchReport(request, response, revenueSummaryVO);
				List<String> listofReport = revenueSummaryVO.getCaseStatusList();
				sizeOfReport = 1;
				this.logger.debug("Size of report for second tab::" + sizeOfReport);
				if (revenueSummaryVO.getCancelledWithCharges().equalsIgnoreCase("Yes")) {
					listofReport.add("Cancelled with charges");
				}

				if (revenueSummaryVO.getIsCancelled().equalsIgnoreCase("Yes")) {
					listofReport.add("Cancelled without charges");
				}

				fileName = this.exportChartToExcel(revenueChartDataList, crdDataList, response, revenueSummaryVO,
						listofReport, 2);
			} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
				this.logger.debug("RevenueSummaryManager###Third Tab");
				sizeOfReport = 1;
				revenueChartDataList = this.fetchReport(request, response, revenueSummaryVO);
				displayCriteria = revenueSummaryVO.getDisplayCriteriaField();
				if (displayCriteria.equalsIgnoreCase("reportType")) {
					listofReport = revenueSummaryVO.getReportTypeList();
				} else if (displayCriteria.equalsIgnoreCase("subReportType")) {
					listofReport = revenueSummaryVO.getReportWithSubReportList();
				}

				this.logger.debug("Report type list ::" + listofReport);
				this.logger.debug("Size of report for third tab::" + sizeOfReport);
				fileName = this.exportChartToExcel(revenueChartDataList, crdDataList, response, revenueSummaryVO,
						(List) listofReport, 3);
			} else {
				Map casesChartDataMap;
				if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByClients")) {
					this.logger.debug("RevenueSummaryManager###Fourth Tab");
					casesChartDataMap = this.fetchReportForCases(request, response, revenueSummaryVO);
					displayCriteria = revenueSummaryVO.getDisplayCriteriaField();
					this.logger.debug("RevenueSummaryManager###Fourth Tab##DisplayCriteria" + displayCriteria);
					if (displayCriteria.equalsIgnoreCase("reportType")) {
						listofReport = revenueSummaryVO.getReportTypeList();
						sizeOfReport = ((List) listofReport).size();
					} else if (displayCriteria.equalsIgnoreCase("subReportType")) {
						listofReport = revenueSummaryVO.getReportWithSubReportList();
						sizeOfReport = ((List) listofReport).size();
						this.logger.debug("Sub report type list size::" + sizeOfReport);
						this.logger.debug("Sub report type list ::" + listofReport);
					} else {
						sizeOfReport = 1;
					}

					this.logger.debug("Combined chart execute");
					this.logger.debug("Size of report for first tab::" + sizeOfReport);
					this.logger.debug("Report type list ::" + listofReport);
					this.logger.debug("Size of report for third tab::" + sizeOfReport);
					fileName = this.exportChartToExcelWithMapAndReportType(casesChartDataMap, crdDataList, response,
							revenueSummaryVO, (List) listofReport, sizeOfReport, displayCriteria);
				} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByPrimarySubjectCountry")) {
					this.logger.debug("RevenueSummaryManager###Fifth Tab");
					sizeOfReport = 1;
					this.logger.debug("Primary Subject Country item::" + revenueSummaryVO.getTopItemField());
					casesChartDataMap = this.fetchReportForCases(request, response, revenueSummaryVO);
					this.logger.debug("Report type list ::" + listofReport);
					this.logger.debug("Size of report for third tab::" + sizeOfReport);
					fileName = this.exportChartToExcelWithMap(casesChartDataMap, crdDataList, response,
							revenueSummaryVO, (List) listofReport);
				} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataOnTimeDeliveryPercentage")) {
					this.logger.debug("RevenueSummaryManager###Sixth Tab");
					revenueChartDataList = this.fetchReport(request, response, revenueSummaryVO);
					lstDeliveryStatus = this.getDeliveryLegends();
					displayCriteria = revenueSummaryVO.getDisplayCriteriaField();
					if (displayCriteria.equalsIgnoreCase("reportType")) {
						sizeOfReport = revenueSummaryVO.getReportTypeList().size();
						listofReport = revenueSummaryVO.getReportTypeList();
					} else if (displayCriteria.equalsIgnoreCase("subReportType")) {
						sizeOfReport = revenueSummaryVO.getReportWithSubReportList().size();
						listofReport = revenueSummaryVO.getReportWithSubReportList();
						this.logger.debug("Sub report type list size::" + sizeOfReport);
						this.logger.debug("Sub report type list ::" + listofReport);
					} else {
						sizeOfReport = 1;
					}

					this.logger.debug("Combined chart execute");
					this.logger.debug("Size of report for sixth tab tab::" + sizeOfReport);
					fileName = this.writeToExcelForDelivery(revenueChartDataList, crdDataList, response,
							revenueSummaryVO, lstDeliveryStatus, (List) listofReport, sizeOfReport, displayCriteria);
				} else if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataTatHistogram")) {
					this.logger.debug("RevenueSummaryManager###Seventh Tab");
					revenueChartDataList = this.fetchReport(request, response, revenueSummaryVO);
					sizeOfReport = 1;
					this.logger.debug("Size of report for second tab::" + sizeOfReport);
					fileName = this.exportChartToExcelForTAT(revenueChartDataList, crdDataList, response,
							revenueSummaryVO, (List) listofReport, 1);
				}
			}

			this.logger.debug("exiting  exportToExcelRevenueChartData ");
		} catch (InvalidFormatException var28) {
			this.logger.debug("InvalidFormatException::" + var28);
		} catch (Exception var29) {
			this.logger.debug("Exception::" + var29);
		}

		return fileName;
	}

	private String exportChartToExcelWithMapAndReportType(Map<Integer, List<RevenueSummaryVO>> casesChartDataMap,
			List crdDataList, HttpServletResponse response, RevenueSummaryVO revenueSummaryVO,
			List<String> listofReport, int sizeOfReport, String displayCriteria)
			throws CMSException, IOException, InvalidFormatException {
		this.logger.debug("RevenueSummaryManager###export chart for Fourth Tab");
		Map<Integer, List<String>> headersMapByCaseStatus = null;
		Map<Integer, List<List<String>>> dataMapByCaseStatus = null;
		List<String> casesHeaderByCaseStatusList = null;
		List<String> revenuesHeaderByCaseStatusList = null;
		List<List<String>> casesDataByCaseStatusList = null;
		List<List<String>> revenueDataByCaseStatusList = null;
		List<String> crdHeaderList = null;
		List crddataList = null;

		try {
			headersMapByCaseStatus = this.getCasesHeaderList(revenueSummaryVO, displayCriteria);
			casesHeaderByCaseStatusList = (List) headersMapByCaseStatus.get(1);
			revenuesHeaderByCaseStatusList = (List) headersMapByCaseStatus.get(2);
			dataMapByCaseStatus = this.getDataMapWithReport(casesChartDataMap, listofReport, displayCriteria);
			casesDataByCaseStatusList = (List) dataMapByCaseStatus.get(1);
			revenueDataByCaseStatusList = (List) dataMapByCaseStatus.get(2);
			this.logger.debug("DataCaSEs by Status::" + casesDataByCaseStatusList);
			this.logger.debug("DataRevenues by Status::" + revenueDataByCaseStatusList);
			crdHeaderList = this.getCRDHeaderList();
			crddataList = this.getCRDDataMap(crdDataList);
			this.logger.debug("##Size of Report in exportChartToExcelWithMap()::" + sizeOfReport);
		} catch (ClassCastException var17) {
			throw new CMSException(this.logger, var17);
		} catch (NullPointerException var18) {
			throw new CMSException(this.logger, var18);
		}

		return ChartExcelDownloader.exportChartToExcel(casesHeaderByCaseStatusList, revenuesHeaderByCaseStatusList,
				casesDataByCaseStatusList, revenueDataByCaseStatusList, crdHeaderList, crddataList, response,
				"FINACE REPORT", revenueSummaryVO, sizeOfReport, 4);
	}

	private String exportChartToExcelWithMap(Map<Integer, List<RevenueSummaryVO>> casesChartDataMap, List crdDataList,
			HttpServletResponse response, RevenueSummaryVO revenueSummaryVO, List<String> listofReport)
			throws CMSException, IOException, InvalidFormatException {
		this.logger.debug("RevenueSummaryManager###export chart for Fifth Tab");
		Map<Integer, List<String>> headersMapByCaseStatus = null;
		Map<Integer, List<List<String>>> dataMapByCaseStatus = null;
		List<String> casesHeaderByCaseStatusList = null;
		List<String> revenuesHeaderByCaseStatusList = null;
		List<List<String>> casesDataByCaseStatusList = null;
		List<List<String>> revenueDataByCaseStatusList = null;
		List<String> crdHeaderList = null;
		List crddataList = null;

		try {
			headersMapByCaseStatus = this.getHeaderList(revenueSummaryVO);
			casesHeaderByCaseStatusList = (List) headersMapByCaseStatus.get(1);
			revenuesHeaderByCaseStatusList = (List) headersMapByCaseStatus.get(2);
			dataMapByCaseStatus = this.getDataMap(casesChartDataMap);
			casesDataByCaseStatusList = (List) dataMapByCaseStatus.get(1);
			revenueDataByCaseStatusList = (List) dataMapByCaseStatus.get(2);
			this.logger.debug("DataCaSEs by Status::" + casesDataByCaseStatusList);
			this.logger.debug("DataRevenues by Status::" + revenueDataByCaseStatusList);
			crdHeaderList = this.getCRDHeaderList();
			crddataList = this.getCRDDataMap(crdDataList);
			this.logger.debug("##Size of Report in exportChartToExcelWithMap()::" + sizeOfReport);
		} catch (ClassCastException var15) {
			throw new CMSException(this.logger, var15);
		} catch (NullPointerException var16) {
			throw new CMSException(this.logger, var16);
		}

		return ChartExcelDownloader.exportChartToExcel(casesHeaderByCaseStatusList, revenuesHeaderByCaseStatusList,
				casesDataByCaseStatusList, revenueDataByCaseStatusList, crdHeaderList, crddataList, response,
				"FINACE REPORT", revenueSummaryVO, sizeOfReport, 5);
	}

	private Map<Integer, List<List<String>>> getDataMap(Map<Integer, List<RevenueSummaryVO>> casesChartDataMap) {
		Map<Integer, List<List<String>>> dataMapByCaseStatus = new LinkedHashMap();
		List<List<String>> dataCasesListByCaseStatus = new ArrayList();
		List<List<String>> dataRevenueListByCaseStatus = new ArrayList();
		List<RevenueSummaryVO> casesChartDataList = (List) casesChartDataMap.get(1);
		List<RevenueSummaryVO> revenueChartDataList = (List) casesChartDataMap.get(2);
		RevenueSummaryVO revenueSummaryVO = null;
		List<String> dataListCase = null;
		List<String> dataListRevenue = null;
		Iterator iterator = casesChartDataList.iterator();

		while (iterator.hasNext()) {
			dataListCase = new ArrayList();
			revenueSummaryVO = (RevenueSummaryVO) iterator.next();
			dataListCase.add(String.valueOf(revenueSummaryVO.getGraphLabel()));
			dataListCase.add(String.valueOf(revenueSummaryVO.getNumberOfCases()));
			dataCasesListByCaseStatus.add(dataListCase);
		}

		iterator = revenueChartDataList.iterator();

		while (iterator.hasNext()) {
			dataListRevenue = new ArrayList();
			revenueSummaryVO = (RevenueSummaryVO) iterator.next();
			dataListRevenue.add(String.valueOf(revenueSummaryVO.getGraphLabel()));
			dataListRevenue.add(String.valueOf(revenueSummaryVO.getRevenue()));
			dataRevenueListByCaseStatus.add(dataListRevenue);
		}

		dataMapByCaseStatus.put(1, dataCasesListByCaseStatus);
		dataMapByCaseStatus.put(2, dataRevenueListByCaseStatus);
		this.logger.debug("DataCaSEs by Status::" + dataCasesListByCaseStatus);
		this.logger.debug("DataRevenues by Status::" + dataRevenueListByCaseStatus);
		return dataMapByCaseStatus;
	}

	private Map<Integer, List<List<String>>> getDataMapWithReport(
			Map<Integer, List<RevenueSummaryVO>> casesChartDataMap, List<String> reportList, String displayCriteria) {
		Map<Integer, List<List<String>>> dataMapByCaseStatus = new LinkedHashMap();
		List<List<String>> dataCasesListByCaseStatus = new ArrayList();
		List<List<String>> dataRevenueListByCaseStatus = new ArrayList();
		List<RevenueSummaryVO> casesChartDataList = (List) casesChartDataMap.get(1);
		List<RevenueSummaryVO> revenueChartDataList = (List) casesChartDataMap.get(2);
		List<RevenueSummaryVO> clientCodeListForCases = new ArrayList();
		List<RevenueSummaryVO> clientCodeListForRevenue = new ArrayList();
		clientCodeListForCases.addAll(casesChartDataList);
		clientCodeListForRevenue.addAll(revenueChartDataList);
		RevenueSummaryVO revenueSummaryVO = null;
		List<String> clientListForCases = new ArrayList();
		Set<String> set = new LinkedHashSet();
		List<String> clientListForRevenue = new ArrayList();
		Set<String> setRevenue = new LinkedHashSet();
		List<String> dataListCase = null;
		List<String> dataListRevenue = null;
		Iterator iterator = clientCodeListForCases.iterator();

		while (iterator.hasNext()) {
			revenueSummaryVO = (RevenueSummaryVO) iterator.next();
			set.add(String.valueOf(revenueSummaryVO.getClientCode()));
		}

		clientListForCases.addAll(set);
		iterator = clientCodeListForRevenue.iterator();

		while (iterator.hasNext()) {
			revenueSummaryVO = (RevenueSummaryVO) iterator.next();
			setRevenue.add(String.valueOf(revenueSummaryVO.getClientCode()));
		}

		clientListForRevenue.addAll(setRevenue);
		Iterator var19;
		Iterator iterator;
		String clientCode;
		if (displayCriteria.equalsIgnoreCase("combined")) {
			for (var19 = clientListForCases.iterator(); var19.hasNext(); dataCasesListByCaseStatus.add(dataListCase)) {
				clientCode = (String) var19.next();
				dataListCase = new ArrayList();
				dataListCase.add(clientCode);
				dataListCase.add("0");
				iterator = casesChartDataList.iterator();

				while (iterator.hasNext()) {
					revenueSummaryVO = (RevenueSummaryVO) iterator.next();
					if (clientCode.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getClientCode()))) {
						dataListCase.set(1, String.valueOf(revenueSummaryVO.getNumberOfCases()));
						break;
					}
				}
			}

			for (var19 = clientListForRevenue.iterator(); var19.hasNext(); dataRevenueListByCaseStatus
					.add(dataListRevenue)) {
				clientCode = (String) var19.next();
				dataListRevenue = new ArrayList();
				dataListRevenue.add(clientCode);
				dataListRevenue.add("0");
				iterator = revenueChartDataList.iterator();

				while (iterator.hasNext()) {
					revenueSummaryVO = (RevenueSummaryVO) iterator.next();
					if (clientCode.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getClientCode()))) {
						dataListRevenue.set(1, String.valueOf(revenueSummaryVO.getRevenue()));
						break;
					}
				}
			}
		} else if (displayCriteria.equalsIgnoreCase("reportType")
				|| displayCriteria.equalsIgnoreCase("subReportType")) {
			Iterator var21;
			String reportTypeList;
			Iterator var23;
			String reportTypeList;
			int i;
			label109 : for (var19 = clientListForCases.iterator(); var19.hasNext(); dataCasesListByCaseStatus
					.add(dataListCase)) {
				clientCode = (String) var19.next();
				dataListCase = new ArrayList();
				dataListCase.add(clientCode);
				var21 = reportList.iterator();

				while (var21.hasNext()) {
					reportTypeList = (String) var21.next();
					dataListCase.add("0");
				}

				iterator = casesChartDataList.iterator();

				while (true) {
					while (true) {
						if (!iterator.hasNext()) {
							continue label109;
						}

						revenueSummaryVO = (RevenueSummaryVO) iterator.next();
						i = 1;
						var23 = reportList.iterator();

						while (var23.hasNext()) {
							reportTypeList = (String) var23.next();
							if (clientCode.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getClientCode()))) {
								if (String.valueOf(revenueSummaryVO.getReportType()).equalsIgnoreCase(reportTypeList)) {
									dataListCase.set(i, String.valueOf(revenueSummaryVO.getNumberOfCases()));
									++i;
									break;
								}

								++i;
							}
						}
					}
				}
			}

			label82 : for (var19 = clientListForRevenue.iterator(); var19.hasNext(); dataRevenueListByCaseStatus
					.add(dataListRevenue)) {
				clientCode = (String) var19.next();
				dataListRevenue = new ArrayList();
				dataListRevenue.add(clientCode);
				var21 = reportList.iterator();

				while (var21.hasNext()) {
					reportTypeList = (String) var21.next();
					dataListRevenue.add("0");
				}

				iterator = revenueChartDataList.iterator();

				while (true) {
					while (true) {
						if (!iterator.hasNext()) {
							continue label82;
						}

						revenueSummaryVO = (RevenueSummaryVO) iterator.next();
						i = 1;
						var23 = reportList.iterator();

						while (var23.hasNext()) {
							reportTypeList = (String) var23.next();
							if (clientCode.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getClientCode()))) {
								if (String.valueOf(revenueSummaryVO.getReportType()).equalsIgnoreCase(reportTypeList)) {
									dataListRevenue.set(i, String.valueOf(revenueSummaryVO.getRevenue()));
									++i;
									break;
								}

								++i;
							}
						}
					}
				}
			}
		}

		dataMapByCaseStatus.put(1, dataCasesListByCaseStatus);
		dataMapByCaseStatus.put(2, dataRevenueListByCaseStatus);
		this.logger.debug("DataCaSEs by Status::" + dataCasesListByCaseStatus);
		this.logger.debug("DataRevenues by Status::" + dataRevenueListByCaseStatus);
		return dataMapByCaseStatus;
	}

	private String exportChartToExcel(List revenueChartDataList, List crdDataList, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO, List<String> reportList, int x)
			throws CMSException, IOException, InvalidFormatException {
		this.logger.debug("RevenueSummaryManager###export chart for Second Tab");
		Map<Integer, List<String>> headersMapByCaseStatus = null;
		Map<Integer, List<List<String>>> dataMapByCaseStatus = null;
		List<String> casesHeaderByCaseStatusList = null;
		List<String> revenuesHeaderByCaseStatusList = null;
		List<List<String>> casesDataByCaseStatusList = null;
		List<List<String>> revenueDataByCaseStatusList = null;
		List<String> crdHeaderList = null;
		List crddataList = null;

		try {
			headersMapByCaseStatus = this.getHeaderList(revenueSummaryVO);
			casesHeaderByCaseStatusList = (List) headersMapByCaseStatus.get(1);
			revenuesHeaderByCaseStatusList = (List) headersMapByCaseStatus.get(2);
			dataMapByCaseStatus = this.getCaseStatusDataMap(revenueChartDataList, reportList, x);
			casesDataByCaseStatusList = (List) dataMapByCaseStatus.get(1);
			revenueDataByCaseStatusList = (List) dataMapByCaseStatus.get(2);
			this.logger.debug("DataCaSEs by Status::" + casesDataByCaseStatusList);
			this.logger.debug("DataRevenues by Status::" + revenueDataByCaseStatusList);
			crdHeaderList = this.getCRDHeaderList();
			crddataList = this.getCRDDataMap(crdDataList);
			this.logger.debug("##Size of Report in exportChartToExcel()::" + sizeOfReport);
		} catch (ClassCastException var16) {
			throw new CMSException(this.logger, var16);
		} catch (NullPointerException var17) {
			throw new CMSException(this.logger, var17);
		}

		return ChartExcelDownloader.exportChartToExcel(casesHeaderByCaseStatusList, revenuesHeaderByCaseStatusList,
				casesDataByCaseStatusList, revenueDataByCaseStatusList, crdHeaderList, crddataList, response,
				"FINACE REPORT", revenueSummaryVO, sizeOfReport, x);
	}

	private String exportChartToExcelForTAT(List revenueChartDataList, List crdDataList, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO, List<String> reportList, int x)
			throws CMSException, IOException, InvalidFormatException {
		this.logger.debug("RevenueSummaryManager###export chart for Second Tab");
		List<String> casesHeaderByCaseStatusList = null;
		List<List<String>> casesDataByCaseStatusList = null;
		List<String> crdHeaderList = null;
		List crddataList = null;

		try {
			casesHeaderByCaseStatusList = this.getHeaderListForTAT(revenueSummaryVO);
			casesDataByCaseStatusList = this.getDataForTAT(revenueChartDataList, reportList);
			this.logger.debug("DataCaSEs by TAT::" + casesDataByCaseStatusList);
			crdHeaderList = this.getCRDHeaderList();
			crddataList = this.getCRDDataMap(crdDataList);
			this.logger.debug("##Size of Report in exportChartToExcelForTAT()::" + sizeOfReport);
		} catch (ClassCastException var12) {
			throw new CMSException(this.logger, var12);
		} catch (NullPointerException var13) {
			throw new CMSException(this.logger, var13);
		}

		return ChartExcelDownloader.exportChartToExcelForTAT(casesHeaderByCaseStatusList, casesDataByCaseStatusList,
				crdHeaderList, crddataList, response, "FINACE REPORT", revenueSummaryVO, sizeOfReport);
	}

	private List<String> getFiltersList(String tempString) {
		List<String> tempList = new ArrayList();
		String[] tempArray = tempString.split(",");
		String[] var7 = tempArray;
		int var6 = tempArray.length;

		for (int var5 = 0; var5 < var6; ++var5) {
			String str = var7[var5];
			tempList.add(str);
		}

		return tempList;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response, RevenueSummaryVO revenueSummaryVO)
			throws CMSException {
		this.logger.debug("in fetchReport");
		new ArrayList();
		this.logger.debug("in fetchReport after query");
		List<RevenueSummaryVO> reportResult = this.tabularReportDAO.fetchRevenueChartReport(request, response,
				revenueSummaryVO);
		this.logger.debug("Exiting fetchReport");
		return reportResult;
	}

	public Map fetchReportForCases(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("in fetchReport");
		new HashMap();
		this.logger.debug("in fetchReport after query");
		Map<Integer, List<RevenueSummaryVO>> reportResult = this.tabularReportDAO
				.fetchCasesAndRevenueChartReport(request, response, revenueSummaryVO);
		this.logger.debug("Exiting fetchReport");
		return reportResult;
	}

	public List fetchCrdReport(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("in fetchCrdReport");
		new ArrayList();
		this.logger.debug("in fetchCrdReport after query");
		List<CaseRawDataFinanceVO> reportResult = this.tabularReportDAO.fetchCrdDataReport(request, response,
				revenueSummaryVO);
		this.logger.debug("Exiting fetchCrdReport");
		return reportResult;
	}

	private String writeToExcel(List<RevenueSummaryVO> revenueChartDataList, List<CaseRawDataFinanceVO> crdDataList,
			HttpServletResponse response, RevenueSummaryVO revenueSummaryVO, List<String> lstMonth,
			List<String> reportList, int sizeOfReport, String displayCriteria)
			throws CMSException, IOException, InvalidFormatException {
		try {
			this.logger.debug("in writeToExcel:: revenueChartDataList ");
			Map<Integer, List<String>> headerMap = this.getCasesHeaderList(revenueSummaryVO, displayCriteria);
			List<String> headerCaseList = (List) headerMap.get(1);
			List<String> headerRevenueList = (List) headerMap.get(2);
			Map<Integer, List<List<String>>> dataMap = this.getCasesRevenueDataMap(revenueChartDataList, lstMonth,
					reportList, displayCriteria);
			List<List<String>> dataCaseList = (List) dataMap.get(1);
			List<List<String>> dataRevenueList = (List) dataMap.get(2);
			this.logger.debug("in writeToExcel:: crdDataList ");
			List<String> crdHeaderList = this.getCRDHeaderList();
			List<List<String>> crddataList = this.getCRDDataMap(crdDataList);
			this.logger.debug("in writeToExcel:: before calling writeToExcel1 " + dataRevenueList);
			return ChartExcelDownloader.exportChartToExcel(headerCaseList, headerRevenueList, dataCaseList,
					dataRevenueList, crdHeaderList, crddataList, response, "FINACE REPORT", revenueSummaryVO,
					sizeOfReport, 1);
		} catch (ClassCastException var17) {
			throw new CMSException(this.logger, var17);
		} catch (NullPointerException var18) {
			throw new CMSException(this.logger, var18);
		} catch (InvalidFormatException var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	private String writeToExcelForDelivery(List<RevenueSummaryVO> revenueChartDataList,
			List<CaseRawDataFinanceVO> crdDataList, HttpServletResponse response, RevenueSummaryVO revenueSummaryVO,
			List<String> lstDeliveryStatus, List<String> reportList, int sizeOfReport, String displayCriteria)
			throws CMSException, IOException, InvalidFormatException {
		try {
			this.logger.debug("in writeToExcel:: revenueChartDataList ");
			List<String> headerCaseList = this.getHeaderListCasesOnly(revenueSummaryVO, displayCriteria);
			List<List<String>> dataCaseList = this.getDataListCasesOnly(revenueChartDataList, lstDeliveryStatus,
					reportList, displayCriteria);
			this.logger.debug("in writeToExcel:: crdDataList ");
			List<String> crdHeaderList = this.getCRDHeaderList();
			List<List<String>> crddataList = this.getCRDDataMap(crdDataList);
			return ChartExcelDownloader.writeToExcelForDelivery(headerCaseList, dataCaseList, crdHeaderList,
					crddataList, response, "FINACE REPORT", revenueSummaryVO, sizeOfReport);
		} catch (ClassCastException var13) {
			throw new CMSException(this.logger, var13);
		} catch (NullPointerException var14) {
			throw new CMSException(this.logger, var14);
		} catch (InvalidFormatException var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	private Map<Integer, List<String>> getCasesHeaderList(RevenueSummaryVO revenueSummaryVO, String displayCriteria) {
		Map<Integer, List<String>> headerMap = new LinkedHashMap();
		List<String> lstCasesHeader = new ArrayList();
		List<String> lstRevenuesHeader = new ArrayList();
		lstCasesHeader.add(revenueSummaryVO.getXaxisFirstGraphField());
		lstRevenuesHeader.add(revenueSummaryVO.getXaxisSecondGraphField());
		if (displayCriteria.equalsIgnoreCase("combined")) {
			lstCasesHeader.add(revenueSummaryVO.getYaxisFirstGraphField());
			lstRevenuesHeader.add(revenueSummaryVO.getYaxisSecondGraphField());
		} else {
			List tempReportTypeList;
			String temp;
			Iterator var8;
			if (displayCriteria.equalsIgnoreCase("reportType")) {
				tempReportTypeList = revenueSummaryVO.getReportTypeList();
				var8 = tempReportTypeList.iterator();

				while (var8.hasNext()) {
					temp = (String) var8.next();
					lstCasesHeader.add(temp);
					lstRevenuesHeader.add(temp);
				}
			} else if (displayCriteria.equalsIgnoreCase("subReportType")) {
				tempReportTypeList = revenueSummaryVO.getReportWithSubReportList();
				var8 = tempReportTypeList.iterator();

				while (var8.hasNext()) {
					temp = (String) var8.next();
					lstCasesHeader.add(temp);
					lstRevenuesHeader.add(temp);
				}
			}
		}

		this.logger.debug("Sub report header list::" + lstCasesHeader);
		this.logger.debug("Sub report header list::" + lstRevenuesHeader);
		headerMap.put(1, lstCasesHeader);
		headerMap.put(2, lstRevenuesHeader);
		return headerMap;
	}

	private Map<Integer, List<List<String>>> getCasesRevenueDataMap(List<RevenueSummaryVO> revenueChartDataList,
			List<String> lstMonth, List<String> reportList, String displayCriteria) {
		this.logger.debug("In getRevenueDataMap :: getRevenueDataMap::" + revenueChartDataList.size());
		this.logger.debug("tesst cases and revenue");
		Map<Integer, List<List<String>>> map = new LinkedHashMap();
		List<List<String>> dataCasesList = new ArrayList();
		List<List<String>> dataRevenueList = new ArrayList();
		List<String> dataListCase = null;
		List<String> dataListRevenue = null;
		RevenueSummaryVO revenueSummaryVO = null;
		String monthList;
		Iterator var12;
		Iterator iterator;
		if (displayCriteria.equalsIgnoreCase("combined")) {
			var12 = lstMonth.iterator();

			while (var12.hasNext()) {
				monthList = (String) var12.next();
				dataListCase = new ArrayList();
				dataListRevenue = new ArrayList();
				dataListCase.add(monthList);
				dataListRevenue.add(monthList);
				dataListCase.add("0");
				dataListRevenue.add("0");
				iterator = revenueChartDataList.iterator();

				while (iterator.hasNext()) {
					revenueSummaryVO = (RevenueSummaryVO) iterator.next();
					if (monthList.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getMonth()))) {
						dataListCase.set(1, String.valueOf(revenueSummaryVO.getNumberOfCases()));
						dataListRevenue.set(1, String.valueOf(revenueSummaryVO.getRevenue()));
						break;
					}
				}

				dataCasesList.add(dataListCase);
				dataRevenueList.add(dataListRevenue);
			}
		} else if (displayCriteria.equalsIgnoreCase("reportType")
				|| displayCriteria.equalsIgnoreCase("subReportType")) {
			var12 = lstMonth.iterator();

			while (var12.hasNext()) {
				monthList = (String) var12.next();
				dataListCase = new ArrayList();
				dataListRevenue = new ArrayList();
				dataListCase.add(monthList);
				dataListRevenue.add(monthList);
				Iterator var14 = reportList.iterator();

				while (var14.hasNext()) {
					String reportTypeList = (String) var14.next();
					dataListCase.add("0");
					dataListRevenue.add("0");
				}

				iterator = revenueChartDataList.iterator();

				while (true) {
					while (iterator.hasNext()) {
						revenueSummaryVO = (RevenueSummaryVO) iterator.next();
						int i = 1;
						Iterator var16 = reportList.iterator();

						while (var16.hasNext()) {
							String reportTypeList = (String) var16.next();
							if (monthList.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getMonth()))) {
								if (String.valueOf(revenueSummaryVO.getReportType()).equalsIgnoreCase(reportTypeList)) {
									dataListCase.set(i, String.valueOf(revenueSummaryVO.getNumberOfCases()));
									dataListRevenue.set(i, String.valueOf(revenueSummaryVO.getRevenue()));
									++i;
									break;
								}

								++i;
							}
						}
					}

					dataCasesList.add(dataListCase);
					dataRevenueList.add(dataListRevenue);
					break;
				}
			}
		}

		this.logger.debug("Data cases list::" + dataCasesList);
		this.logger.debug("Data Revenues list::" + dataRevenueList);
		map.put(1, dataCasesList);
		map.put(2, dataRevenueList);
		this.logger.debug("Size of map::" + map.size());
		return map;
	}

	private List<String> getCRDHeaderList() {
		List<String> lstHeader = new ArrayList();
		lstHeader.add("REPORT TYPE");
		lstHeader.add("CLIENT CODE");
		lstHeader.add("CLIENT NAME");
		lstHeader.add("CLIENT GROUP");
		lstHeader.add("PRIMARY SUBJECT");
		lstHeader.add("PRIMARY SUBJECT COUNTRY");
		lstHeader.add("CLIENT REFERENCE NUMBER");
		lstHeader.add("PRIMARY SUBJECT TYPE");
		lstHeader.add("CRN");
		lstHeader.add("CASE FEE USD");
		lstHeader.add("CASE FEE USD PLAN FX");
		lstHeader.add("CASE RECEIVED DATE");
		lstHeader.add("CLIENT FINAL DUE DATE");
		lstHeader.add("CLIENT FINAL SENT DATE");
		lstHeader.add("CASE MANAGER");
		lstHeader.add("CASE STATUS");
		lstHeader.add("CANCELLED WITH CHARGES");
		lstHeader.add("PARENT CRN");
		lstHeader.add("SUB REPORT TYPE PARENT CRN");
		lstHeader.add("SALES REPRESENTATIVE REGION");
		lstHeader.add("SALES MONTH");
		lstHeader.add("ACHEIVED TAT");
		lstHeader.add("TARGET TAT");
		lstHeader.add("DELIVERY");
		return lstHeader;
	}

	private List<List<String>> getCRDDataMap(List<CaseRawDataFinanceVO> crdDataList) {
		this.logger.debug("In getRevenueDataMap :: getRevenueDataMap");
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = crdDataList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			CaseRawDataFinanceVO crdSummaryVO = (CaseRawDataFinanceVO) iterator.next();
			datamap.add(String.valueOf(crdSummaryVO.getReportType() != null ? crdSummaryVO.getReportType() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getClientCode()));
			datamap.add(String.valueOf(crdSummaryVO.getClientName()));
			datamap.add(
					String.valueOf(crdSummaryVO.getMainClientGroup() != null ? crdSummaryVO.getMainClientGroup() : ""));
			datamap.add(
					String.valueOf(crdSummaryVO.getPrimarySubject() != null ? crdSummaryVO.getPrimarySubject() : ""));
			datamap.add(String.valueOf(
					crdSummaryVO.getPrimarySubjectCountry() != null ? crdSummaryVO.getPrimarySubjectCountry() : ""));
			datamap.add(String.valueOf(
					crdSummaryVO.getClientReferenceNumber() != null ? crdSummaryVO.getClientReferenceNumber() : ""));
			datamap.add(String
					.valueOf(crdSummaryVO.getPrimarySubjectType() != null ? crdSummaryVO.getPrimarySubjectType() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getCRN()));
			datamap.add(String.valueOf(crdSummaryVO.getCaseFee_usd() != null ? crdSummaryVO.getCaseFee_usd() : ""));
			datamap.add(String.valueOf(
					crdSummaryVO.getCase_fee_usd_plan_fx() != null ? crdSummaryVO.getCase_fee_usd_plan_fx() : ""));
			datamap.add(String
					.valueOf(crdSummaryVO.getCaseReceivedDate() != null ? crdSummaryVO.getCaseReceivedDate() : ""));
			datamap.add(
					String.valueOf(crdSummaryVO.getFinal_dd_Client() != null ? crdSummaryVO.getFinal_dd_Client() : ""));
			datamap.add(String.valueOf(
					crdSummaryVO.getClientFinalSentDate() != null ? crdSummaryVO.getClientFinalSentDate() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getCaseManager() != null ? crdSummaryVO.getCaseManager() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getCaseStatus() != null ? crdSummaryVO.getCaseStatus() : ""));
			datamap.add(String
					.valueOf(crdSummaryVO.getCancelledCharges() != null ? crdSummaryVO.getCancelledCharges() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getParentCRN() != null ? crdSummaryVO.getParentCRN() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getSubReportType_Parent_crn() != null
					? crdSummaryVO.getSubReportType_Parent_crn()
					: ""));
			datamap.add(String.valueOf(crdSummaryVO.getSalesRepresentativeRegion() != null
					? crdSummaryVO.getSalesRepresentativeRegion()
					: ""));
			datamap.add(String.valueOf(crdSummaryVO.getSalesmonth() != null ? crdSummaryVO.getSalesmonth() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getAcheived_tat() != null ? crdSummaryVO.getAcheived_tat() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getTarget_tat() != null ? crdSummaryVO.getTarget_tat() : ""));
			datamap.add(String.valueOf(crdSummaryVO.getDelivery() != null ? crdSummaryVO.getDelivery() : ""));
			dataList.add(datamap);
		}

		return dataList;
	}

	public static List<String> getListOfMonth(String startLocalDate, String endLocalDate) {
		Date startDate = new Date(startLocalDate);
		Date endDate = new Date(endLocalDate);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy");
		List<String> months = new ArrayList();
		int diffYear = endCalendar.get(1) - startCalendar.get(1);
		int diffMonth = diffYear * 12 + endCalendar.get(2) - startCalendar.get(2) + 1;
		sdf.setCalendar(startCalendar);

		while (diffMonth > 0) {
			months.add(sdf.format(startCalendar.getTime()));
			startCalendar.add(2, 1);
			--diffMonth;
		}

		return months;
	}

	public Map<Integer, List<String>> getCaseStatusHeaderList(RevenueSummaryVO revenueSummaryVO) {
		Map<Integer, List<String>> dataMap = null;
		dataMap = new HashMap();
		List<String> lstCases = null;
		lstCases = new ArrayList();
		List<String> lstRevenue = null;
		lstRevenue = new ArrayList();
		lstCases.add(revenueSummaryVO.getXaxisFirstGraphField());
		lstCases.add(revenueSummaryVO.getYaxisFirstGraphField());
		lstRevenue.add(revenueSummaryVO.getXaxisSecondGraphField());
		lstRevenue.add(revenueSummaryVO.getYaxisSecondGraphField());
		this.logger.debug("Header of first Graph by Case status::" + lstCases);
		this.logger.debug("Header of second graph by Case status::" + lstRevenue);
		dataMap.put(1, lstCases);
		dataMap.put(2, lstRevenue);
		return dataMap;
	}

	public Map<Integer, List<List<String>>> getCaseStatusDataMap(List revenueChartDataList) {
		Map<Integer, List<List<String>>> dataMapByCaseStatus = new LinkedHashMap();
		List<List<String>> dataCasesListByCaseStatus = new ArrayList();
		List<List<String>> dataRevenueListByCaseStatus = new ArrayList();
		Iterator iterator = revenueChartDataList.iterator();

		while (iterator.hasNext()) {
			RevenueSummaryVO revenueSummaryVO = (RevenueSummaryVO) iterator.next();
			List<String> dataListCase = new ArrayList();
			List<String> dataListRevenue = new ArrayList();
			dataListCase.add(revenueSummaryVO.getCaseStatus());
			dataListCase.add(String.valueOf(revenueSummaryVO.getNumberOfCases()));
			dataListRevenue.add(revenueSummaryVO.getCaseStatus());
			dataListRevenue.add(String.valueOf(revenueSummaryVO.getRevenue()));
			dataCasesListByCaseStatus.add(dataListCase);
			dataRevenueListByCaseStatus.add(dataListRevenue);
		}

		dataMapByCaseStatus.put(1, dataCasesListByCaseStatus);
		dataMapByCaseStatus.put(2, dataRevenueListByCaseStatus);
		return dataMapByCaseStatus;
	}

	private List<String> getDeliveryLegends() {
		List<String> months = new ArrayList();
		months.add("Earlier");
		months.add("On-Time");
		months.add("Not-On-Time");
		return months;
	}

	public Map<Integer, List<String>> getHeaderList(RevenueSummaryVO revenueSummaryVO) {
		Map<Integer, List<String>> dataMap = null;
		dataMap = new HashMap();
		List<String> lstCases = null;
		lstCases = new ArrayList();
		List<String> lstRevenue = null;
		lstRevenue = new ArrayList();
		lstCases.add(revenueSummaryVO.getXaxisFirstGraphField());
		lstCases.add(revenueSummaryVO.getYaxisFirstGraphField());
		lstRevenue.add(revenueSummaryVO.getXaxisSecondGraphField());
		lstRevenue.add(revenueSummaryVO.getYaxisSecondGraphField());
		this.logger.debug("Header of first Graph by Case status::" + lstCases);
		this.logger.debug("Header of second graph by Case status::" + lstRevenue);
		dataMap.put(1, lstCases);
		dataMap.put(2, lstRevenue);
		return dataMap;
	}

	public Map<Integer, List<List<String>>> getCaseStatusDataMap(List revenueChartDataList, List<String> reportList,
			int x) {
		Map<Integer, List<List<String>>> dataMapByCaseStatus = new LinkedHashMap();
		List<List<String>> dataCasesListByCaseStatus = new ArrayList();
		List<List<String>> dataRevenueListByCaseStatus = new ArrayList();
		RevenueSummaryVO revenueSummaryVO = null;
		List<String> dataListCase = null;
		List<String> dataListRevenue = null;
		Iterator var11 = reportList.iterator();

		while (var11.hasNext()) {
			String lstReport = (String) var11.next();
			dataListCase = new ArrayList();
			dataListRevenue = new ArrayList();
			dataListCase.add(lstReport);
			dataListRevenue.add(lstReport);
			dataListCase.add("0");
			dataListRevenue.add("0");
			Iterator iterator;
			if (x == 2) {
				iterator = revenueChartDataList.iterator();

				while (iterator.hasNext()) {
					revenueSummaryVO = (RevenueSummaryVO) iterator.next();
					if (lstReport.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getCaseStatus()))) {
						this.logger.debug("##Case status ..." + revenueSummaryVO.getCaseStatus());
						dataListCase.set(1, String.valueOf(revenueSummaryVO.getNumberOfCases()));
						dataListRevenue.set(1, String.valueOf(revenueSummaryVO.getRevenue()));
						break;
					}
				}
			} else if (x == 3) {
				iterator = revenueChartDataList.iterator();

				while (iterator.hasNext()) {
					revenueSummaryVO = (RevenueSummaryVO) iterator.next();
					if (lstReport.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getReportType()))) {
						this.logger.debug("##Case status ..." + revenueSummaryVO.getCaseStatus());
						dataListCase.set(1, String.valueOf(revenueSummaryVO.getNumberOfCases()));
						dataListRevenue.set(1, String.valueOf(revenueSummaryVO.getRevenue()));
						break;
					}
				}
			}

			dataCasesListByCaseStatus.add(dataListCase);
			dataRevenueListByCaseStatus.add(dataListRevenue);
			dataMapByCaseStatus.put(1, dataCasesListByCaseStatus);
			dataMapByCaseStatus.put(2, dataRevenueListByCaseStatus);
		}

		this.logger.debug("DataCaSEs by report type::" + dataCasesListByCaseStatus);
		this.logger.debug("DataRevenues by report type::" + dataRevenueListByCaseStatus);
		return dataMapByCaseStatus;
	}

	private List<String> getHeaderListCasesOnly(RevenueSummaryVO revenueSummaryVO, String displayCriteria) {
		new LinkedHashMap();
		List<String> lstCasesHeader = new ArrayList();
		lstCasesHeader.add(revenueSummaryVO.getXaxisFirstGraphField());
		if (displayCriteria.equalsIgnoreCase("combined")) {
			lstCasesHeader.add(revenueSummaryVO.getYaxisFirstGraphField());
		} else {
			List tempReportTypeList;
			String temp;
			Iterator var7;
			if (displayCriteria.equalsIgnoreCase("reportType")) {
				tempReportTypeList = revenueSummaryVO.getReportTypeList();
				var7 = tempReportTypeList.iterator();

				while (var7.hasNext()) {
					temp = (String) var7.next();
					lstCasesHeader.add(temp);
				}
			} else if (displayCriteria.equalsIgnoreCase("subReportType")) {
				tempReportTypeList = revenueSummaryVO.getReportWithSubReportList();
				var7 = tempReportTypeList.iterator();

				while (var7.hasNext()) {
					temp = (String) var7.next();
					lstCasesHeader.add(temp);
				}
			}
		}

		this.logger.debug("Sub report header list::" + lstCasesHeader);
		return lstCasesHeader;
	}

	private List<List<String>> getDataListCasesOnly(List<RevenueSummaryVO> revenueChartDataList,
			List<String> lstDeliveryStatus, List<String> reportList, String displayCriteria) {
		this.logger.debug("In getRevenueDataMap :: getRevenueDataMap::" + revenueChartDataList.size());
		this.logger.debug("tesst cases and revenue");
		List<List<String>> dataCasesList = new ArrayList();
		List<String> dataListCase = null;
		RevenueSummaryVO revenueSummaryVO = null;
		String deliveryList;
		Iterator var9;
		Iterator iterator;
		if (displayCriteria.equalsIgnoreCase("combined")) {
			for (var9 = lstDeliveryStatus.iterator(); var9.hasNext(); dataCasesList.add(dataListCase)) {
				deliveryList = (String) var9.next();
				dataListCase = new ArrayList();
				dataListCase.add(deliveryList);
				dataListCase.add("0.00");
				iterator = revenueChartDataList.iterator();

				while (iterator.hasNext()) {
					revenueSummaryVO = (RevenueSummaryVO) iterator.next();
					if (deliveryList.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getGraphLabel()))) {
						dataListCase.set(1, String.valueOf(revenueSummaryVO.getDeliveryTypePercentage()));
						break;
					}
				}
			}
		} else if (displayCriteria.equalsIgnoreCase("reportType")
				|| displayCriteria.equalsIgnoreCase("subReportType")) {
			label55 : for (var9 = lstDeliveryStatus.iterator(); var9.hasNext(); dataCasesList.add(dataListCase)) {
				deliveryList = (String) var9.next();
				dataListCase = new ArrayList();
				dataListCase.add(deliveryList);
				Iterator var11 = reportList.iterator();

				while (var11.hasNext()) {
					String reportTypeList = (String) var11.next();
					dataListCase.add("0.00");
				}

				iterator = revenueChartDataList.iterator();

				while (true) {
					while (true) {
						if (!iterator.hasNext()) {
							continue label55;
						}

						revenueSummaryVO = (RevenueSummaryVO) iterator.next();
						int i = 1;
						Iterator var13 = reportList.iterator();

						while (var13.hasNext()) {
							String reportTypeList = (String) var13.next();
							if (deliveryList.equalsIgnoreCase(String.valueOf(revenueSummaryVO.getGraphLabel()))) {
								if (String.valueOf(revenueSummaryVO.getReportType()).equalsIgnoreCase(reportTypeList)) {
									dataListCase.set(i, String.valueOf(revenueSummaryVO.getDeliveryTypePercentage()));
									++i;
									break;
								}

								++i;
							}
						}
					}
				}
			}
		}

		this.logger.debug("Data cases list::" + dataCasesList);
		return dataCasesList;
	}

	public List<String> getHeaderListForTAT(RevenueSummaryVO revenueSummaryVO) {
		List<String> lstCases = null;
		lstCases = new ArrayList();
		lstCases.add(revenueSummaryVO.getXaxisFirstGraphField());
		lstCases.add(revenueSummaryVO.getYaxisFirstGraphField());
		this.logger.debug("Header of first Graph by TAT::" + lstCases);
		return lstCases;
	}

	public List<List<String>> getDataForTAT(List revenueChartDataList, List<String> reportList) {
		List<List<String>> dataCasesListByCaseStatus = new ArrayList();
		RevenueSummaryVO revenueSummaryVO = null;
		List<String> dataListCase = null;
		List<String> dataListRevenue = null;
		Iterator iterator = revenueChartDataList.iterator();

		while (iterator.hasNext()) {
			dataListCase = new ArrayList();
			revenueSummaryVO = (RevenueSummaryVO) iterator.next();
			dataListCase.add(String.valueOf(revenueSummaryVO.getGraphLabel()));
			dataListCase.add(String.valueOf(revenueSummaryVO.getNumberOfCases()));
			dataCasesListByCaseStatus.add(dataListCase);
		}

		this.logger.debug("####DataCAsess by TAT::" + dataCasesListByCaseStatus);
		return dataCasesListByCaseStatus;
	}

	public int getTotalCount(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		String isCancelled = "No";
		List<String> caseStatusWithoutCancelled = new ArrayList();
		this.logger.debug("in exportToExcelRevenueChartData:::");

		try {
			if (!revenueSummaryVO.getSelectedClient().trim().isEmpty()) {
				revenueSummaryVO.setClientList(this.getFiltersList(revenueSummaryVO.getSelectedClient()));
			}

			if (!revenueSummaryVO.getSelectedCaseStatus().trim().isEmpty()) {
				String[] tempArray = revenueSummaryVO.getSelectedCaseStatus().split(",");
				String[] var10 = tempArray;
				int var9 = tempArray.length;

				for (int var8 = 0; var8 < var9; ++var8) {
					String str = var10[var8];
					if (!str.equals("Cancelled")) {
						caseStatusWithoutCancelled.add(str);
					} else {
						isCancelled = "Yes";
					}
				}
			}

			revenueSummaryVO.setCaseStatusList(caseStatusWithoutCancelled);
			revenueSummaryVO.setIsCancelled(isCancelled);
			if (!revenueSummaryVO.getSelectedReportType().trim().isEmpty()) {
				revenueSummaryVO.setReportTypeList(this.getFiltersList(revenueSummaryVO.getSelectedReportType()));
				this.logger.debug("****Report with report" + revenueSummaryVO.getReportTypeList());
			}

			if (!revenueSummaryVO.getSelectedSubReportTypeField().isEmpty()) {
				revenueSummaryVO
						.setSubReportTypeList(this.getFiltersList(revenueSummaryVO.getSelectedSubReportTypeField()));
				this.logger.debug("****Report with report" + revenueSummaryVO.getSubReportTypeList());
			}

			if (!revenueSummaryVO.getSelectedReportWithSubReportField().isEmpty()) {
				revenueSummaryVO.setReportWithSubReportList(
						this.getFiltersList(revenueSummaryVO.getSelectedReportWithSubReportField()));
				this.logger.debug("****Report with sub report" + revenueSummaryVO.getReportWithSubReportList());
			}

			if (!revenueSummaryVO.getSalesRepresentativeField().isEmpty()) {
				revenueSummaryVO.setSalesRepresentativeList(
						this.getFiltersList(revenueSummaryVO.getSalesRepresentativeField()));
				this.logger
						.debug("****Report with sales representative" + revenueSummaryVO.getSalesRepresentativeField());
			}

			if (!revenueSummaryVO.getExcludeChildField().isEmpty()) {
				revenueSummaryVO.setExcludeChildList(this.getFiltersList(revenueSummaryVO.getExcludeChildField()));
				this.logger.debug("****Report with child field" + revenueSummaryVO.getExcludeChildField());
			}
		} catch (Exception var11) {
			this.logger.debug("Exception::" + var11);
		}

		return this.fetchReportSize(request, response, revenueSummaryVO);
	}

	public int fetchReportSize(HttpServletRequest request, HttpServletResponse response,
			RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("in fetchReportSize");
		int reportSize = this.tabularReportDAO.fetchReportSize(request, response, revenueSummaryVO);
		this.logger.debug("Exiting fetchReportSize::" + reportSize);
		return reportSize;
	}

	private void setColorCode() {
		this.colorMap.put("combined", "1F497D");
		this.colorMap.put("revenue", "00B200");
		this.colorMap.put("cases", "1F497D");
		this.colorMap.put("IS Lite", "1F497D");
		this.colorMap.put("IS Lite-STD", "1F497D");
		this.colorMap.put("IS Lite-LLC", "C00000");
		this.colorMap.put("IS Standard", "009900");
		this.colorMap.put("IS Premium", "FFC000");
		this.colorMap.put("IS ORR", "F79646");
		this.colorMap.put("IS ORR-CSP", "00B0F0");
		this.colorMap.put("IS ORR-FMT", "FFFF00");
		this.colorMap.put("IS ORR-ADJ", "FF66CC");
		this.colorMap.put("IS ORR-PRE", "7030A0");
		this.colorMap.put("IS ORR-SSP", "00FF00");
		this.colorMap.put("IS ORR-SPJ", "0000FF");
		this.colorMap.put("IS FCPA", "4BACC6");
		this.colorMap.put("In Progress", "1F497D");
		this.colorMap.put("Completed", "009900");
		this.colorMap.put("On Hold", "FFC000");
		this.colorMap.put("Cancelled without charges", "F79646");
		this.colorMap.put("Completed Client Submission", "C00000");
		this.colorMap.put("Cancelled with charges", "4BACC6");
	}

	private String numberToThousandSeparator(float number) {
		String formattedNumber = "";
		String pattern = "#,###,###,###,###,###";
		NumberFormat decimalFormat = new DecimalFormat(pattern);
		formattedNumber = decimalFormat.format((double) number);
		return formattedNumber;
	}

	private String decimalConvertor(float number) {
		this.logger.debug("float number:" + number);
		String formattedNumber = "";
		formattedNumber = String.format("%.01f", number);
		this.logger.debug("return number:" + formattedNumber);
		return formattedNumber;
	}

	private String decimalConversionTwoPlaces(float number) {
		String formattedNumber = "";
		formattedNumber = String.format("%.02f", number);
		return formattedNumber;
	}

	private Map<String, Object> getCasesByMonth(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In Manager getCasesByMonth");
		String isDataPresent = "true";
		String casesByMonthXMLString = "";
		String revenueByMonthFusionXMLString = "";
		String caseTableData = "";
		String revenueTableData = "";
		new ArrayList();
		Map<String, Object> controllerDataMap = new HashMap();
		new HashMap();
		List<RevenueSummaryVO> revenueSummaryVOList = this.tabularReportDAO.getCasesByMonth(revenueSummaryVO);
		if (revenueSummaryVOList.size() > 0) {
			RevenueSummaryVO rv = (RevenueSummaryVO) revenueSummaryVOList.get(0);
			RevenueSummaryVO rvLast = (RevenueSummaryVO) revenueSummaryVOList.get(revenueSummaryVOList.size() - 1);
			String startMonthFromDB = "01-" + rv.getMonth();
			String startMonthFromUI = revenueSummaryVO.getStartDateField();
			String endMonthFromDB = "01-" + rvLast.getMonth();
			String endMonthFromUI = revenueSummaryVO.getEndDateField();
			String resultStartMonth = "";
			String resultEndMonth = "";
			this.logger.debug("startMonthFromDB::" + startMonthFromDB + "######startMonthFromUI" + startMonthFromUI);
			this.logger.debug("endMonthFromDB::" + endMonthFromDB + "######endMonthFromUI" + endMonthFromUI);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			new Date();
			new Date();
			new Date();
			new Date();

			Date d1;
			Date d2;
			Date d3;
			Date d4;
			try {
				d1 = sdf.parse(startMonthFromDB);
				d2 = sdf.parse(startMonthFromUI);
				d3 = sdf.parse(endMonthFromDB);
				d4 = sdf.parse(endMonthFromUI);
			} catch (ParseException var25) {
				throw new CMSException();
			}

			this.logger.debug("d1 time::" + d1.getTime() + "###### d2 Time:::" + d2.getTime());
			if (d1.getTime() <= d2.getTime()) {
				resultStartMonth = startMonthFromDB;
			} else {
				resultStartMonth = revenueSummaryVO.getStartDateField();
			}

			if (d3.getTime() >= d4.getTime()) {
				resultEndMonth = endMonthFromDB;
			} else {
				resultEndMonth = revenueSummaryVO.getEndDateField();
			}

			this.logger.debug("resultStartMonth::" + resultStartMonth);
			this.logger.debug("resultEndMonth::" + resultEndMonth);
			List<String> month = getListOfMonth(resultStartMonth, resultEndMonth);
			HashMap<String, Object> dataMap = this.casesByMonthTypeSummary(revenueSummaryVO, revenueSummaryVOList,
					month);
			this.logger.debug("dataMap size::" + dataMap.size());
			Map<String, String> caseMap = this.getCasesGraphParameters(revenueSummaryVO, month,
					(String[]) dataMap.get("caseDataSetSeriesName"), (List) dataMap.get("casesDatasetList"),
					revenueSummaryVO.getHeaderFirstGraphField(), revenueSummaryVO.getXaxisFirstGraphField(),
					revenueSummaryVO.getYaxisFirstGraphField(), revenueSummaryVO.getLabelFirstGraphField(),
					(String) dataMap.get("caseType"));
			casesByMonthXMLString = (String) caseMap.get("fusionXMLString");
			caseTableData = (String) caseMap.get("tableData");
			caseMap = this.getCasesGraphParameters(revenueSummaryVO, month,
					(String[]) dataMap.get("revenueDataSetSeriesName"), (List) dataMap.get("revenueDatasetList"),
					revenueSummaryVO.getHeaderSecondGraphField(), revenueSummaryVO.getXaxisSecondGraphField(),
					revenueSummaryVO.getYaxisSecondGraphField(), revenueSummaryVO.getLabelSecondGraphField(),
					(String) dataMap.get("revenueType"));
			revenueByMonthFusionXMLString = (String) caseMap.get("fusionXMLString");
			revenueTableData = (String) caseMap.get("tableData");
		} else {
			isDataPresent = "false";
		}

		this.logger.debug("casesByMonthXMLString::" + casesByMonthXMLString);
		this.logger.debug("revenueByMonthFusionXMLString::" + revenueByMonthFusionXMLString);
		controllerDataMap.put("casesDataXMLString", casesByMonthXMLString);
		controllerDataMap.put("revenueDataXMLString", revenueByMonthFusionXMLString);
		controllerDataMap.put("caseTable", caseTableData);
		controllerDataMap.put("revenueTable", revenueTableData);
		controllerDataMap.put("isDataPresent", isDataPresent);
		return controllerDataMap;
	}

	public HashMap<String, Object> casesByMonthTypeSummary(RevenueSummaryVO revenueSummaryVO,
			List<RevenueSummaryVO> revenueSummaryVOList, List<String> month) throws CMSException {
		this.logger.debug("In RevenueSummaryManager : casesByMonthTypeSummary");
		HashMap datamap = new HashMap();

		try {
			List<float[]> casesDataSetList = new ArrayList();
			List<float[]> revenueDataSetList = new ArrayList();
			float[] casesArray = (float[]) null;
			float[] revenueArray = (float[]) null;
			String[] dataSetSeriesName = (String[]) null;
			List<String> reportList = null;
			int i;
			if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("combined")) {
				this.logger.debug("In combined condition::" + revenueSummaryVO.getDisplayCriteriaField());
				RevenueSummaryVO rsvo = null;
				casesArray = new float[month.size()];
				revenueArray = new float[month.size()];

				for (i = 0; i < revenueSummaryVOList.size(); ++i) {
					rsvo = (RevenueSummaryVO) revenueSummaryVOList.get(i);
					this.logger.debug("month:::" + rsvo.getMonth());
					this.logger.debug("month index:::" + month.indexOf(rsvo.getMonth()));
					casesArray[month.indexOf(rsvo.getMonth())] = (float) rsvo.getNumberOfCases();
					revenueArray[month.indexOf(rsvo.getMonth())] = rsvo.getRevenue();
				}

				this.logger.debug("casesArray::" + casesArray.length);
				revenueDataSetList.add(revenueArray);
				casesDataSetList.add(casesArray);
				datamap.put("casesDatasetList", casesDataSetList);
				datamap.put("caseDataSetSeriesName", Constants.CASEDATASERIESENAME);
				datamap.put("caseType", "cases");
				datamap.put("revenueDatasetList", revenueDataSetList);
				datamap.put("revenueDataSetSeriesName", Constants.REVENUEDATASETSERIES);
				datamap.put("revenueType", "revenue");
			} else {
				Object[] obj;
				if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("reportType")) {
					this.logger.debug("In Report Type condition::" + revenueSummaryVO.getDisplayCriteriaField());
					this.logger.debug("reportType list size in casesByReportTypeSummary::::"
							+ revenueSummaryVO.getReportTypeList().size());
					reportList = revenueSummaryVO.getReportTypeList();
					obj = revenueSummaryVO.getReportTypeList().toArray();
					dataSetSeriesName = (String[]) Arrays.copyOf(obj, obj.length, String[].class);
				} else {
					this.logger.debug("In Sub Report Type condition::" + revenueSummaryVO.getDisplayCriteriaField());
					this.logger.debug("subReportType list size in casesBySubReportTypeSummary::::"
							+ revenueSummaryVO.getReportWithSubReportList().size());
					reportList = revenueSummaryVO.getReportWithSubReportList();
					obj = revenueSummaryVO.getReportWithSubReportList().toArray();
					dataSetSeriesName = (String[]) Arrays.copyOf(obj, obj.length, String[].class);
				}

				for (i = 0; i < reportList.size(); ++i) {
					casesArray = new float[month.size()];
					revenueArray = new float[month.size()];
					Iterator itr = revenueSummaryVOList.iterator();

					while (itr.hasNext()) {
						RevenueSummaryVO rsVO = (RevenueSummaryVO) itr.next();
						this.logger.debug("report types in rsvo:::" + rsVO.getReportType()
								+ "::::reportType from request:::" + (String) reportList.get(i));
						if (((String) reportList.get(i)).equalsIgnoreCase(rsVO.getReportType())) {
							casesArray[month.indexOf(rsVO.getMonth())] = (float) rsVO.getNumberOfCases();
							revenueArray[month.indexOf(rsVO.getMonth())] = rsVO.getRevenue();
							this.logger.debug(month.indexOf(rsVO.getMonth()) + "========" + rsVO.getRevenue());
						}
					}

					casesDataSetList.add(casesArray);
					revenueDataSetList.add(revenueArray);
				}

				datamap.put("casesDatasetList", casesDataSetList);
				datamap.put("caseDataSetSeriesName", dataSetSeriesName);
				datamap.put("caseType", "reportType");
				datamap.put("revenueDatasetList", revenueDataSetList);
				datamap.put("revenueDataSetSeriesName", dataSetSeriesName);
				datamap.put("revenueType", "revenue");
			}
		} catch (Exception var15) {
			AtlasUtils.getExceptionView(this.logger, var15);
		}

		return datamap;
	}

	private Map<String, Object> getCasesByCaseStatus(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In Manager getCasesByCaseStatus");
		new ArrayList();
		Map<String, Object> controllerDataMap = new HashMap();
		new HashMap();
		new HashMap();
		String isDataPresent = "true";

		try {
			List<RevenueSummaryVO> revenueSummaryVOList = this.tabularReportDAO.getCasesByCaseStatus(revenueSummaryVO);
			this.logger.debug("revenueSummaryVOList size in getCasesByCaseStatus:::" + revenueSummaryVOList.size());
			List<String> listofCaseStatus = revenueSummaryVO.getCaseStatusList();
			if (revenueSummaryVO.getCancelledWithCharges().equalsIgnoreCase("Yes")) {
				listofCaseStatus.add("Cancelled with charges");
			}

			if (revenueSummaryVO.getIsCancelled().equalsIgnoreCase("Yes")) {
				listofCaseStatus.add("Cancelled without charges");
			}

			this.logger.debug("case status List size::::" + listofCaseStatus.size());
			if (revenueSummaryVOList.size() > 0) {
				HashMap<String, Object> dataMap = this.casesByCaseStatusTypeSummary(revenueSummaryVO,
						revenueSummaryVOList, listofCaseStatus);
				this.logger.debug("dataMap size::" + dataMap.size());
				Map<String, Object> casesByCaseStatusMap = this.getDataXMLString(revenueSummaryVO, listofCaseStatus,
						(float[]) dataMap.get("casesDatasetList"), revenueSummaryVO.getHeaderFirstGraphField(),
						revenueSummaryVO.getXaxisFirstGraphField(), revenueSummaryVO.getYaxisFirstGraphField(),
						revenueSummaryVO.getLabelFirstGraphField(), (String) dataMap.get("caseType"));
				this.logger.debug("Cases barChart XMLString::" + casesByCaseStatusMap.get("barChart"));
				this.logger.debug("Cases Pie XMLString::" + casesByCaseStatusMap.get("pieChart"));
				controllerDataMap.put("casesDataXMLString", casesByCaseStatusMap.get("barChart"));
				controllerDataMap.put("casesPieXMLString", casesByCaseStatusMap.get("pieChart"));
				controllerDataMap.put("caseTable", casesByCaseStatusMap.get("tableData"));
				Map<String, Object> revenueByCaseStatusMap = this.getDataXMLString(revenueSummaryVO, listofCaseStatus,
						(float[]) dataMap.get("revenueDatasetList"), revenueSummaryVO.getHeaderSecondGraphField(),
						revenueSummaryVO.getXaxisSecondGraphField(), revenueSummaryVO.getYaxisSecondGraphField(),
						revenueSummaryVO.getLabelSecondGraphField(), (String) dataMap.get("revenueType"));
				this.logger.debug("revenue barChartXMLString::" + revenueByCaseStatusMap.get("barChart"));
				this.logger.debug("revenue PieXMLString::" + revenueByCaseStatusMap.get("pieChart"));
				controllerDataMap.put("revenueDataXMLString", revenueByCaseStatusMap.get("barChart"));
				controllerDataMap.put("revenuePieXMLString", revenueByCaseStatusMap.get("pieChart"));
				controllerDataMap.put("revenueTable", revenueByCaseStatusMap.get("tableData"));
			} else {
				isDataPresent = "false";
			}

			controllerDataMap.put("isDataPresent", isDataPresent);
		} catch (Exception var9) {
			AtlasUtils.getExceptionView(this.logger, var9);
		}

		return controllerDataMap;
	}

	private Map<String, Object> getCasesByReportType(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In Manager getCasesByReportType");
		new ArrayList();
		Map<String, Object> controllerDataMap = new HashMap();
		new HashMap();
		new HashMap();
		String isDataPresent = "true";

		try {
			List<RevenueSummaryVO> revenueSummaryVOList = this.tabularReportDAO.getCasesByReportType(revenueSummaryVO);
			this.logger.debug("revenueSummaryVOList size in getCasesByReportType:::" + revenueSummaryVOList.size());
			if (revenueSummaryVOList.size() > 0) {
				HashMap<String, Object> dataMap = this.casesByReportTypeSummary(revenueSummaryVO, revenueSummaryVOList);
				this.logger.debug("dataMap size::" + dataMap.size());
				Map<String, Object> casesByReportTypeMap = this.getDataXMLString(revenueSummaryVO,
						(List) dataMap.get("caseDataSetSeriesName"), (float[]) dataMap.get("casesDatasetList"),
						revenueSummaryVO.getHeaderFirstGraphField(), revenueSummaryVO.getXaxisFirstGraphField(),
						revenueSummaryVO.getYaxisFirstGraphField(), revenueSummaryVO.getLabelFirstGraphField(),
						(String) dataMap.get("caseType"));
				this.logger.debug("casesByCaseStatusMap bar chart string::" + casesByReportTypeMap.get("barChart"));
				this.logger.debug("casesByCaseStatusMap pie chart string::" + casesByReportTypeMap.get("pieChart"));
				controllerDataMap.put("casesDataXMLString", casesByReportTypeMap.get("barChart"));
				controllerDataMap.put("casesPieXMLString", casesByReportTypeMap.get("pieChart"));
				controllerDataMap.put("caseTable", casesByReportTypeMap.get("tableData"));
				Map<String, Object> revenueByReportTypeMap = this.getDataXMLString(revenueSummaryVO,
						(List) dataMap.get("revenueDataSetSeriesName"), (float[]) dataMap.get("revenueDatasetList"),
						revenueSummaryVO.getHeaderSecondGraphField(), revenueSummaryVO.getXaxisSecondGraphField(),
						revenueSummaryVO.getYaxisSecondGraphField(), revenueSummaryVO.getLabelSecondGraphField(),
						(String) dataMap.get("revenueType"));
				this.logger.debug("revenueByCaseStatusMap bar chart string::" + revenueByReportTypeMap.get("barChart"));
				this.logger.debug("revenueByCaseStatusMap pie chart string::" + revenueByReportTypeMap.get("pieChart"));
				controllerDataMap.put("revenueDataXMLString", revenueByReportTypeMap.get("barChart"));
				controllerDataMap.put("revenuePieXMLString", revenueByReportTypeMap.get("pieChart"));
				controllerDataMap.put("revenueTable", revenueByReportTypeMap.get("tableData"));
			} else {
				isDataPresent = "false";
			}

			controllerDataMap.put("isDataPresent", isDataPresent);
		} catch (Exception var8) {
			AtlasUtils.getExceptionView(this.logger, var8);
		}

		return controllerDataMap;
	}

	public HashMap<String, Object> casesByReportTypeSummary(RevenueSummaryVO revenueSummaryVO,
			List<RevenueSummaryVO> revenueSummaryVOList) throws CMSException {
		this.logger.debug("In RevenueSummaryManager : casesByReportTypeSummary");
		HashMap datamap = new HashMap();

		try {
			List<String> reportList = null;
			if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("reportType")) {
				this.logger.debug("In Report Type condition::" + revenueSummaryVO.getDisplayCriteriaField());
				this.logger.debug("reportType list size in casesByReportTypeSummary::::"
						+ revenueSummaryVO.getReportTypeList().size());
				reportList = revenueSummaryVO.getReportTypeList();
			} else if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("subReportType")) {
				this.logger.debug("In Sub Report Type condition::" + revenueSummaryVO.getDisplayCriteriaField());
				this.logger.debug("subReportType list size in casesBySubReportTypeSummary::::"
						+ revenueSummaryVO.getReportWithSubReportList().size());
				reportList = revenueSummaryVO.getReportWithSubReportList();
			}

			float[] casesArray = new float[reportList.size()];
			float[] revenueArray = new float[reportList.size()];
			RevenueSummaryVO rsVO = null;
			Iterator<RevenueSummaryVO> itr = null;
			if (revenueSummaryVOList != null) {
				for (int i = 0; i < reportList.size(); ++i) {
					itr = revenueSummaryVOList.iterator();

					while (itr.hasNext()) {
						rsVO = (RevenueSummaryVO) itr.next();
						this.logger.debug("rsVO.getReportType():::" + rsVO.getReportType()
								+ "::::reportType from request:::" + (String) reportList.get(i));
						if (((String) reportList.get(i)).equalsIgnoreCase(rsVO.getReportType())) {
							casesArray[i] = (float) rsVO.getNumberOfCases();
							revenueArray[i] = rsVO.getRevenue();
							this.logger.debug("ith index::" + i + "========" + casesArray[i]);
						}
					}
				}

				this.logger.debug("casesArray length -- " + casesArray.length);
				datamap.put("casesDatasetList", casesArray);
				datamap.put("caseType", "reportType");
				datamap.put("caseDataSetSeriesName", reportList);
				datamap.put("revenueDatasetList", revenueArray);
				datamap.put("revenueType", "revenue");
				datamap.put("revenueDataSetSeriesName", reportList);
			}
		} catch (Exception var10) {
			AtlasUtils.getExceptionView(this.logger, var10);
		}

		return datamap;
	}

	public HashMap<String, Object> casesByCaseStatusTypeSummary(RevenueSummaryVO revenueSummaryVO,
			List<RevenueSummaryVO> revenueSummaryVOList, List<String> listofCaseStatus) throws CMSException {
		this.logger.debug("RevenueSummaryManager : casesByCaseStatusTypeSummary");
		HashMap datamap = new HashMap();

		try {
			float[] casesArray = new float[listofCaseStatus.size()];
			float[] revenueArray = new float[listofCaseStatus.size()];
			this.logger.debug("caseStatus list size in casesByCaseStatusTypeSummary::::" + listofCaseStatus.size());
			RevenueSummaryVO rsVO = null;
			Iterator<RevenueSummaryVO> itr = null;

			for (int i = 0; i < listofCaseStatus.size(); ++i) {
				this.logger.debug("CaseStatus = " + (String) listofCaseStatus.get(i));
				itr = revenueSummaryVOList.iterator();

				while (itr.hasNext()) {
					rsVO = (RevenueSummaryVO) itr.next();
					this.logger.debug("caseStatus in rsvo:::" + rsVO.getCaseStatus() + "::::caseStatus from request:::"
							+ (String) listofCaseStatus.get(i));
					if (((String) listofCaseStatus.get(i)).equalsIgnoreCase(rsVO.getCaseStatus())) {
						casesArray[i] = (float) rsVO.getNumberOfCases();
						this.logger.debug("i====" + i + " casesArray[i]:::" + casesArray[i]);
						revenueArray[i] = rsVO.getRevenue();
					}
				}
			}

			this.logger.debug("casesArray length -- " + casesArray.length);
			datamap.put("casesDatasetList", casesArray);
			datamap.put("caseType", "subReportType");
			datamap.put("revenueDatasetList", revenueArray);
			datamap.put("revenueType", "revenue");
		} catch (Exception var10) {
			AtlasUtils.getExceptionView(this.logger, var10);
		}

		return datamap;
	}

	private Map<String, Object> getCasesByClientCode(RevenueSummaryVO revenueSummaryVO) throws CMSException {
		this.logger.debug("In Manager getCasesByClientCode");
		new ArrayList();
		new ArrayList();
		Map<String, Object> controllerDataMap = new HashMap();
		new HashMap();
		new HashMap();
		new HashMap();
		new HashMap();
		String isDataPresent = "true";

		try {
			Map<String, Object> dataMapValues = this.tabularReportDAO.getCasesByClientCode(revenueSummaryVO);
			List<RevenueSummaryVO> casesByClientList = (List) dataMapValues.get("clientCaseList");
			this.logger.debug("revenueSummaryVOList size::" + casesByClientList.size());
			List<RevenueSummaryVO> revenueByClientList = (List) dataMapValues.get("clientRevenueList");
			this.logger.debug("revenueSummaryVOList size::" + revenueByClientList.size());
			String casesFusionXMLString = "";
			String revenueFusionXMLString = "";
			String caseTable = "";
			String revenueTable = "";
			if (casesByClientList.size() > 0) {
				HashMap<String, Object> dataMap = this.casesByClientCodeSummary(revenueSummaryVO, casesByClientList,
						revenueByClientList);
				this.logger.debug("dataMap size::" + dataMap.size());
				if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("combined")) {
					this.logger.debug("in cases if condition:::");
					Map<String, Object> casesByClientCodeMap = this.getDataXMLString(revenueSummaryVO,
							(List) dataMap.get("clientCaseList"), (float[]) dataMap.get("casesDatasetList"),
							revenueSummaryVO.getHeaderFirstGraphField(), revenueSummaryVO.getXaxisFirstGraphField(),
							revenueSummaryVO.getYaxisFirstGraphField(), revenueSummaryVO.getLabelFirstGraphField(),
							(String) dataMap.get("caseType"));
					Map<String, Object> revenueByClientCodeMap = this.getDataXMLString(revenueSummaryVO,
							(List) dataMap.get("clientRevenueList"), (float[]) dataMap.get("revenueDatasetList"),
							revenueSummaryVO.getHeaderSecondGraphField(), revenueSummaryVO.getXaxisSecondGraphField(),
							revenueSummaryVO.getYaxisSecondGraphField(), revenueSummaryVO.getLabelSecondGraphField(),
							(String) dataMap.get("revenueType"));
					this.logger.debug("casesXMLString::" + casesByClientCodeMap.get("barChart"));
					this.logger.debug("revenueFusionXMLString::" + revenueByClientCodeMap.get("barChart"));
					controllerDataMap.put("casesDataXMLString", casesByClientCodeMap.get("barChart"));
					controllerDataMap.put("revenueDataXMLString", revenueByClientCodeMap.get("barChart"));
					controllerDataMap.put("caseTable", casesByClientCodeMap.get("tableData"));
					controllerDataMap.put("revenueTable", revenueByClientCodeMap.get("tableData"));
				} else {
					this.logger.debug("in cases else condition:::");
					Map<String, String> caseMap = this.getCasesGraphParameters(revenueSummaryVO,
							(List) dataMap.get("clientCaseList"), (String[]) dataMap.get("caseDataSetSeriesName"),
							(List) dataMap.get("casesDatasetList"), revenueSummaryVO.getHeaderFirstGraphField(),
							revenueSummaryVO.getXaxisFirstGraphField(), revenueSummaryVO.getYaxisFirstGraphField(),
							revenueSummaryVO.getLabelFirstGraphField(), (String) dataMap.get("caseType"));
					casesFusionXMLString = (String) caseMap.get("fusionXMLString");
					caseTable = (String) caseMap.get("tableData");
					caseMap = this.getCasesGraphParameters(revenueSummaryVO, (List) dataMap.get("clientRevenueList"),
							(String[]) dataMap.get("revenueDataSetSeriesName"),
							(List) dataMap.get("revenueDatasetList"), revenueSummaryVO.getHeaderSecondGraphField(),
							revenueSummaryVO.getXaxisSecondGraphField(), revenueSummaryVO.getYaxisSecondGraphField(),
							revenueSummaryVO.getLabelSecondGraphField(), (String) dataMap.get("revenueType"));
					revenueFusionXMLString = (String) caseMap.get("fusionXMLString");
					revenueTable = (String) caseMap.get("tableData");
					this.logger.debug("casesByMonthXMLString::" + casesFusionXMLString);
					this.logger.debug("revenueByMonthFusionXMLString::" + revenueFusionXMLString);
					controllerDataMap.put("casesDataXMLString", casesFusionXMLString);
					controllerDataMap.put("revenueDataXMLString", revenueFusionXMLString);
					controllerDataMap.put("caseTable", caseTable);
					controllerDataMap.put("revenueTable", revenueTable);
				}
			} else {
				isDataPresent = "false";
			}

			controllerDataMap.put("isDataPresent", isDataPresent);
		} catch (Exception var15) {
			AtlasUtils.getExceptionView(this.logger, var15);
		}

		return controllerDataMap;
	}

	public HashMap<String, Object> casesByClientCodeSummary(RevenueSummaryVO revenueSummaryVO,
			List<RevenueSummaryVO> casesByClientList, List<RevenueSummaryVO> revenueByClientList) throws CMSException {
		this.logger.debug("In RevenueSummaryManager : casesByClientCodeSummary");
		HashMap<String, Object> datamap = new HashMap();
		new HashMap();

		try {
			new ArrayList();
			new ArrayList();
			List<String> casesClientList = new ArrayList();
			List<String> revenueClientList = new ArrayList();
			new ArrayList();
			String[] reportTypes = (String[]) null;
			Set<String> casesClientCodeSet = new LinkedHashSet();
			Set<String> revenueClientCodeSet = new LinkedHashSet();
			RevenueSummaryVO rsvo = null;

			int i;
			for (i = 0; i < casesByClientList.size(); ++i) {
				rsvo = (RevenueSummaryVO) casesByClientList.get(i);
				casesClientCodeSet.add(rsvo.getClientCode());
			}

			this.logger.debug("clientCodeSet size::" + casesClientCodeSet.size());
			casesClientList.addAll(casesClientCodeSet);

			for (i = 0; i < revenueByClientList.size(); ++i) {
				rsvo = (RevenueSummaryVO) revenueByClientList.get(i);
				revenueClientCodeSet.add(rsvo.getClientCode());
			}

			this.logger.debug("clientCodeSet size::" + revenueClientCodeSet.size());
			revenueClientList.addAll(revenueClientCodeSet);
			HashMap dataValueMap;
			if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("combined")) {
				this.logger.debug("In combined condition::" + revenueSummaryVO.getDisplayCriteriaField());
				dataValueMap = this.getDataListForCombined(casesByClientList, revenueByClientList);
				float[] casesArray = (float[]) dataValueMap.get("caseArray");
				float[] revenueArray = (float[]) dataValueMap.get("revenueArray");
				datamap.put("casesDatasetList", casesArray);
				datamap.put("clientCaseList", casesClientList);
				datamap.put("caseType", "cases");
				datamap.put("revenueDatasetList", revenueArray);
				datamap.put("clientRevenueList", revenueClientList);
				datamap.put("revenueType", "revenue");
			} else {
				List reportList;
				Object[] obj;
				if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("reportType")) {
					reportList = revenueSummaryVO.getReportTypeList();
					obj = revenueSummaryVO.getReportTypeList().toArray();
					reportTypes = (String[]) Arrays.copyOf(obj, obj.length, String[].class);
				} else {
					reportList = revenueSummaryVO.getReportWithSubReportList();
					obj = revenueSummaryVO.getReportWithSubReportList().toArray();
					reportTypes = (String[]) Arrays.copyOf(obj, obj.length, String[].class);
				}

				dataValueMap = this.getDataListForReport(casesByClientList, revenueByClientList, revenueClientList,
						casesClientList, reportList);
				List<float[]> casesDataSetList = (List) dataValueMap.get("casesDatasetList");
				List<float[]> revenueDataSetList = (List) dataValueMap.get("revenueDatasetList");
				datamap.put("casesDatasetList", casesDataSetList);
				datamap.put("caseDataSetSeriesName", reportTypes);
				datamap.put("clientCaseList", casesClientList);
				datamap.put("caseType", "reportType");
				datamap.put("revenueDatasetList", revenueDataSetList);
				datamap.put("revenueDataSetSeriesName", reportTypes);
				datamap.put("clientRevenueList", revenueClientList);
				datamap.put("revenueType", "revenue");
			}
		} catch (Exception var17) {
			AtlasUtils.getExceptionView(this.logger, var17);
		}

		return datamap;
	}

	private HashMap<String, Object> getDataListForReport(List<RevenueSummaryVO> clientList,
			List<RevenueSummaryVO> revenueList, List<String> revenueClientCodes, List<String> caseClientCodes,
			List<String> reportTypes) throws CMSException {
		HashMap<String, Object> datamap = new HashMap();
		List<float[]> casesDataSetList = new ArrayList();
		ArrayList revenueDataSetList = new ArrayList();

		try {
			float[] casesArray = (float[]) null;
			float[] revenueArray = (float[]) null;
			RevenueSummaryVO rsvo = null;

			for (int i = 0; i < reportTypes.size(); ++i) {
				casesArray = new float[caseClientCodes.size()];
				revenueArray = new float[revenueClientCodes.size()];

				int j;
				for (j = 0; j < clientList.size(); ++j) {
					rsvo = (RevenueSummaryVO) clientList.get(j);
					if (((String) reportTypes.get(i)).equalsIgnoreCase(rsvo.getReportType())) {
						this.logger.debug("reportList.get(i)" + (String) reportTypes.get(i) + " rsvo.getReportType()::"
								+ rsvo.getReportType());
						casesArray[caseClientCodes.indexOf(rsvo.getClientCode())] = (float) rsvo.getNumberOfCases();
						this.logger.debug("rsvo.getClientCode() in caseArray::" + rsvo.getClientCode() + "------"
								+ rsvo.getNumberOfCases() + "clientList.indexOf(rsvo.getClientCode())"
								+ caseClientCodes.indexOf(rsvo.getClientCode()));
					}
				}

				casesDataSetList.add(casesArray);

				for (j = 0; j < revenueList.size(); ++j) {
					rsvo = (RevenueSummaryVO) revenueList.get(j);
					if (((String) reportTypes.get(i)).equalsIgnoreCase(rsvo.getReportType())) {
						this.logger.debug("reportList.get(i)" + (String) reportTypes.get(i) + " rsvo.getReportType()::"
								+ rsvo.getReportType());
						revenueArray[revenueClientCodes.indexOf(rsvo.getClientCode())] = rsvo.getRevenue();
						this.logger.debug("rsvo.getClientCode() in revenueArray::" + rsvo.getClientCode() + "------"
								+ rsvo.getRevenue() + "clientList.indexOf(rsvo.getClientCode())"
								+ revenueClientCodes.indexOf(rsvo.getClientCode()));
					}
				}

				revenueDataSetList.add(revenueArray);
			}

			Iterator var20 = casesDataSetList.iterator();

			while (var20.hasNext()) {
				float[] values = (float[]) var20.next();
				float[] var17 = values;
				int var16 = values.length;

				for (int var15 = 0; var15 < var16; ++var15) {
					float value = var17[var15];
					this.logger.debug("value::" + value);
				}

				this.logger.debug("values::" + values.toString());
			}

			datamap.put("casesDatasetList", casesDataSetList);
			datamap.put("revenueDatasetList", revenueDataSetList);
			return datamap;
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	private HashMap<String, Object> getDataListForCombined(List<RevenueSummaryVO> clientDataList,
			List<RevenueSummaryVO> revenueDataList) throws CMSException {
		HashMap<String, Object> datamap = new HashMap();
		float[] casesArray = new float[clientDataList.size()];
		float[] revenueArray = new float[revenueDataList.size()];

		try {
			RevenueSummaryVO rsvo = null;

			int i;
			for (i = 0; i < clientDataList.size(); ++i) {
				rsvo = (RevenueSummaryVO) clientDataList.get(i);
				this.logger.debug("index:::" + i);
				casesArray[i] = (float) rsvo.getNumberOfCases();
				this.logger.debug("casesArray[i]::" + casesArray[i] + " no. of cases::" + rsvo.getNumberOfCases());
			}

			for (i = 0; i < revenueDataList.size(); ++i) {
				rsvo = (RevenueSummaryVO) revenueDataList.get(i);
				this.logger.debug("index:::" + i);
				revenueArray[i] = rsvo.getRevenue();
				this.logger.debug("revenueArray[i]::" + revenueArray[i] + " no. of cases::" + rsvo.getRevenue());
			}

			this.logger.debug("casesArray::" + casesArray.length);
			this.logger.debug("revenueArray::" + revenueArray.length);
			datamap.put("caseArray", casesArray);
			datamap.put("revenueArray", revenueArray);
			return datamap;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private Map<String, String> getCasesGraphParameters(RevenueSummaryVO revenueSummaryVO, List<String> month,
			String[] dataSetSeriesName, List<float[]> dataSetList, String caption, String xAxisName, String yAxisName,
			String islabel, String chartType) throws CMSException {
		this.logger.debug("In JSONRevenueSummaryController :: getCasesGraphParameters");
		String fusionXMLString = "";
		Map<String, String> resultMap = new HashMap();
		String row1 = "<tr><td>&nbsp;</td>";
		String row2 = "";
		String tableString = "<div class='overflowTable'><table class='top10DataTable'>";
		String attributes = " ";
		String checkRevenue = "";
		String tempValue = "";

		try {
			String categories = "";
			String dataset = "";
			String datasetprefix = "";
			String datasetsuffix = "</dataset>";
			String color = "";
			if (chartType.equalsIgnoreCase("revenue")) {
				attributes = "numberPrefix ='$'";
				checkRevenue = "$";
			}

			int i;
			for (i = 0; i < month.size(); ++i) {
				categories = categories + " <category label='" + (String) month.get(i) + "'/>";
				row1 = row1 + "<td>" + (String) month.get(i) + "</td>";
			}

			this.logger.debug("dataSetSeriesName.length:::" + dataSetSeriesName.length);

			for (i = 0; i < dataSetSeriesName.length; ++i) {
				datasetprefix = "<dataset>";
				this.logger.debug("dataSetSeriesNameIndex:::" + i + "dataSetSeriesName[i]:::" + dataSetSeriesName[i]);
				if (dataSetSeriesName[i].equalsIgnoreCase("Number of Cases")) {
					color = (String) this.colorMap.get("combined");
					this.logger.debug("colorMap.get combined :::" + (String) this.colorMap.get("combined"));
				}

				if (dataSetSeriesName[i].equalsIgnoreCase("Revenue(USD)")) {
					color = (String) this.colorMap.get("revenue");
					this.logger.debug("colorMap.get revenue :::" + (String) this.colorMap.get("revenue"));
				}

				if (revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("reportType")
						|| revenueSummaryVO.getDisplayCriteriaField().equalsIgnoreCase("subReportType")) {
					color = (String) this.colorMap.get(dataSetSeriesName[i]);
					this.logger.debug(
							"colorMap.get for report types:::" + (String) this.colorMap.get(dataSetSeriesName[i]));
				}

				row2 = row2 + "<tr><td><span style='background-color:#" + color + "' "
						+ "class='revsumCaseBlue'></span>&nbsp;&nbsp;" + dataSetSeriesName[i] + "</td>";
				float[] values = (float[]) dataSetList.get(i);
				String datasetMain = "";
				this.logger.debug("Values size::::" + values.length);
				float[] var29 = values;
				int var28 = values.length;

				for (int var27 = 0; var27 < var28; ++var27) {
					float value = var29[var27];
					tempValue = this.numberToThousandSeparator(value);
					row2 = row2 + "<td>" + checkRevenue + tempValue + "</td>";
					datasetMain = datasetMain + " <set value='" + tempValue + "' color='" + color + "' toolText='"
							+ dataSetSeriesName[i] + ",&nbsp;&nbsp;" + tempValue + "' link='' " + "/>";
				}

				dataset = dataset + datasetprefix + datasetMain + datasetsuffix;
				row1 = row1 + "</tr>";
				row2 = row2 + "</tr>";
			}

			tableString = tableString + row1 + row2 + "</table></div>";
			int label = 0;
			if (islabel.equalsIgnoreCase("true")) {
				label = 1;
			}

			this.logger.debug("label::" + label);
			fusionXMLString = "<chart showValues='" + label + "' showLabel='0' caption='" + caption + "' xAxisName='"
					+ xAxisName + "' yAxisName='" + yAxisName
					+ "'showLabel='0' bgColor='FFFFFF' labelDisplay='WRAP' formatNumberScale='0' numDivLines='10' anchorSides='4' lineThickness='2.25'"
					+ " anchorRadius='5' anchorBgColor='F5F50E' thousandSeparatorPosition='1' inThousandSeparator=',' canvasPadding='20' valuePadding='5'"
					+ attributes + ">" + "<categories>  " + categories + " </categories>" + dataset + " <styles> "
					+ "<definition> "
					+ "<style name='myCaptionFont' type='font' font='Calibri' size='18' bold='1' color='000600'/>"
					+ "<style name='xAxisFont' type='font' font='Calibri' size='11' bold='1' color='000600'/> "
					+ "<style name='yAxisFont' type='font' font='Calibri' size='10' bold='1' color='000600'/> "
					+ "</definition>" + " <application> " + "<apply toObject='Caption' styles='myCaptionFont'/> "
					+ "<apply toObject='XAXISNAME' styles='xAxisFont'/> "
					+ "<apply toObject='YAXISNAME' styles='yAxisFont'/> "
					+ "<apply toObject='DATAVALUES' styles='xAxisFont'/> " + "</application> " + "</styles>"
					+ "</chart>";
		} catch (Exception var30) {
			throw new CMSException(this.logger, var30);
		}

		resultMap.put("tableData", tableString);
		resultMap.put("fusionXMLString", fusionXMLString);
		return resultMap;
	}

	private HashMap<String, Object> getDataXMLString(RevenueSummaryVO revenueSummaryVO, List<String> dataSetSeriesName,
			float[] dataSetList, String caption, String xAxisName, String yAxisName, String islabel, String chartType)
			throws CMSException {
		this.logger.debug("In RevenueSummaryManager :: getDataXMLString");
		HashMap<String, Object> datamap = new HashMap();
		String barXMLString = "";
		String pieXMLString = "";
		String row1 = "<tr><td>&nbsp;</td>";
		String row2 = "";
		String tableString = "<div class='overflowTable'><table class='top10DataTable'>";
		String checkRevenue = "";
		String tempValue = "";

		try {
			String categories = "";
			String dataset = "";
			String datasetprefix = "<dataset>";
			String datasetsuffix = "</dataset>";
			String numberPrefix = "";
			String color = (String) this.colorMap.get("combined");
			String seriesName = Constants.CASEDATASERIESENAME[0];
			if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByClients")) {
				if (chartType.equalsIgnoreCase("cases")) {
					color = (String) this.colorMap.get("combined");
					this.logger.debug("colorMap.get(COMBINED):::" + (String) this.colorMap.get("combined"));
				}

				if (chartType.equalsIgnoreCase("revenue")) {
					color = (String) this.colorMap.get("revenue");
					this.logger.debug("colorMap.get(revenue):::" + (String) this.colorMap.get("revenue"));
				}
			}

			if (chartType.equalsIgnoreCase("revenue")) {
				numberPrefix = "numberPrefix ='$'";
				checkRevenue = "$";
				color = (String) this.colorMap.get("revenue");
				seriesName = Constants.REVENUEDATASETSERIES[0];
			}

			this.logger.debug("categoryList length:::" + dataSetSeriesName.size());
			row2 = row2 + "<tr><td><span style='background-color:#" + color + "' "
					+ "class='revsumCaseBlue'></span>&nbsp;&nbsp;" + seriesName + "</td>";

			for (int i = 0; i < dataSetSeriesName.size(); ++i) {
				if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")
						|| revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
					color = (String) this.colorMap.get(dataSetSeriesName.get(i));
					this.logger.debug("color checking" + color + " seriename" + (String) dataSetSeriesName.get(i));
				}

				this.logger.debug("category value:::" + (String) dataSetSeriesName.get(i));
				float value = dataSetList[i];
				tempValue = this.numberToThousandSeparator(value);
				this.logger.debug("numberOfCases:::" + tempValue);
				String datasetMain = " <set value='" + tempValue + "' color='" + color + "' toolText='"
						+ (String) dataSetSeriesName.get(i) + ",&nbsp;&nbsp;" + tempValue + "' link='' " + "/>";
				categories = categories + " <category label='" + (String) dataSetSeriesName.get(i) + "'/>";
				row1 = row1 + "<td>" + (String) dataSetSeriesName.get(i) + "</td>";
				row2 = row2 + "<td>" + checkRevenue + tempValue + "</td>";
				dataset = dataset + datasetMain;
			}

			dataset = datasetprefix + dataset + datasetsuffix;
			row1 = row1 + "</tr>";
			row2 = row2 + "</tr>";
			tableString = tableString + row1 + row2 + "</table></div>";
			int label = 0;
			if (islabel.equalsIgnoreCase("true")) {
				label = 1;
			}

			this.logger.debug("label::" + label);
			barXMLString = "<chart showValues='" + label + "' showLabel='0' caption='" + caption + "' xAxisName='"
					+ xAxisName + "' yAxisName='" + yAxisName
					+ "'showLabel='0' bgColor='FFFFFF' labelDisplay='WRAP'  valuePadding='5' numDivLines='10'"
					+ " formatNumberScale='0' thousandSeparatorPosition='1' inThousandSeparator=',' showPercentageInLabel='0' "
					+ numberPrefix + ">" + "<categories>  " + categories + " </categories>" + dataset + " <styles> "
					+ "<definition> "
					+ "<style name='myCaptionFont' type='font' font='Calibri' size='18' bold='1' color='000600'/> "
					+ "<style name='xAxisFont' type='font' font='Calibri' size='11' bold='1' color='000600'/> "
					+ "<style name='yAxisFont' type='font' font='Calibri' size='10' bold='1' color='000600'/> "
					+ "</definition>" + " <application> " + "<apply toObject='Caption' styles='myCaptionFont'/> "
					+ "<apply toObject='XAXISNAME' styles='xAxisFont'/> "
					+ "<apply toObject='YAXISNAME' styles='yAxisFont'/> "
					+ "<apply toObject='DATAVALUES' styles='xAxisFont'/> " + "</application> " + "</styles>"
					+ "</chart>";
			if (revenueSummaryVO.getTabName().equalsIgnoreCase("dataByCaseStatus")
					|| revenueSummaryVO.getTabName().equalsIgnoreCase("dataByReportType")) {
				pieXMLString = this.getPieDataXMLString(label, caption, dataSetSeriesName, dataSetList, numberPrefix);
				datamap.put("pieChart", pieXMLString);
			}

			datamap.put("barChart", barXMLString);
			datamap.put("tableData", tableString);
			return datamap;
		} catch (Exception var27) {
			throw new CMSException(this.logger, var27);
		}
	}

	private String getPieDataXMLString(int label, String caption, List<String> categoryList, float[] dataSetList,
			String numberPrefix) throws CMSException {
		String pieXMLString = "";
		String tempValue = "";

		try {
			String dataset = "";
			this.logger.debug("categoryList length:::" + categoryList.size());

			for (int i = 0; i < categoryList.size(); ++i) {
				this.logger.debug("case Status:::" + (String) categoryList.get(i));
				float casesByCaseStatus = dataSetList[i];
				tempValue = this.numberToThousandSeparator(casesByCaseStatus);
				String datasetMain = " <set label = '" + (String) categoryList.get(i) + "' value='" + tempValue
						+ "' color='" + (String) this.colorMap.get(categoryList.get(i)) + "' toolText='"
						+ (String) categoryList.get(i) + ",&nbsp;&nbsp;" + tempValue + "'isSliced='1'/>";
				dataset = dataset + datasetMain;
			}

			pieXMLString = "<graph showValues='" + label + "' showLabel='" + label + "' bgColor='FFFFFF' caption='"
					+ caption + "'labelDisplay='WRAP'"
					+ "pieRadius='130' pieBorderColor='FFFFFF' showNames='1' showShadow='1' formatNumberScale='0'"
					+ "thousandSeparatorPosition='1' inThousandSeparator=',' manageLabelOverflow='1' showZeroPies='0'"
					+ "animation='0' slicingDistance ='20' showpercentageinlabel='1' pieBorderColor='FFFFFF' decimals='1'"
					+ numberPrefix + ">" + dataset + " <styles> " + "<definition> "
					+ "<style name='myCaptionFont' type='font' font='Calibri' size='18' bold='1' color='000600'/> "
					+ "<style name='fontAttribute' type='font' font='Calibri' size='11' bold='1' color='000600'/> "
					+ "</definition>" + " <application> " + "<apply toObject='Caption' styles='myCaptionFont'/> "
					+ "<apply toObject='DATALABELS' styles='fontAttribute'/> " + "</application> " + "</styles>"
					+ "</graph>";
			return pieXMLString;
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}
}