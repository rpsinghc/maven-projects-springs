package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.report.OfficePerformanceVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONOfficePerformanceController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONOfficePerformanceController");
	private static final String CHART_WIDTH_VALUE = "1160";
	private static final String CHART_HEIGHT_VALUE = "600";
	private static final String CHART_WIDTH = "chartWidth";
	private static final String CHART_HEIGHT = "chartHeight";
	private static final String PRODUCTIVITYPOINTBYOFFICEFUSIONXML = "productivityPointByOfficeFusionXML";
	private static final String PRODUCTIVITYPOINTANALYSTBYOFFICEFUSIONXML = "productivityPointAnalystByOfficeFusionXML";
	private static final String COMPLETEDCASEBYOFFICEFUSIONXML = "completedCaseByOfficeFusionXML";
	private static final String COMPLETEDCASEANALYSTBYOFFICEFUSIONXML = "completedCaseAnalystByOfficeFusionXML";
	private static final String REVENUEBYOFFICEFUSIONXML = "revenueByOfficefusionXML";
	private static final String REVENUEANDANALYSTBYOFFICEFUSIONXML = "revenueAndAnalystByOfficefusionXML";
	private String caption = null;
	private String xAxisName = null;
	private String yAxisName = null;
	private String toolTipReport = "";
	private HashMap<String, String> officeColorHMap = new HashMap();
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView productivityPointByOffice(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("OfficePerformanceController : productivityPointByOffice");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		request.setAttribute("reportName", "productivityPointByOffice");
		String[] category = new String[12];
		ArrayList dataSetList = new ArrayList();

		try {
			List<OfficePerformanceVO> fullList = this.atlasReportService.getReport(request, response);
			this.logger.debug("list :" + fullList.size());
			String[] dataSetSeriesName = new String[fullList.size()];
			int indexDataSetSeriesName = 0;
			Set<String> monthSet = ReportConstants.fiscalYearFullMonthMap.keySet();
			Iterator<String> itr1 = monthSet.iterator();

			for (int var11 = 0; itr1.hasNext(); category[var11++] = (String) itr1.next()) {
				;
			}

			Iterator itr2 = fullList.iterator();

			while (itr2.hasNext()) {
				OfficePerformanceVO opvoOut = (OfficePerformanceVO) itr2.next();
				dataSetSeriesName[indexDataSetSeriesName++] = opvoOut.getOffice();
				List<OfficePerformanceVO> opvoInList = opvoOut.getOfficePerformanceVOList();
				this.logger.debug("number of revenues for month:" + opvoOut.getOffice() + ": is :" + opvoInList.size());
				float[] officeData = new float[12];

				OfficePerformanceVO opvoIn;
				for (Iterator itr3 = opvoInList.iterator(); itr3
						.hasNext(); officeData[Integer.parseInt(opvoIn.getMonth()) - 1] = opvoIn
								.getProductivityPoints()) {
					opvoIn = (OfficePerformanceVO) itr3.next();
					this.logger.debug("Month [" + opvoIn.getMonth() + "] \tPoints:" + opvoIn.getProductivityPoints());
				}

				dataSetList.add(officeData);
			}

			String dataSetMV = "dataSetPointByOffice";
			String categoryMV = "categoryPointByOffice";
			this.caption = "Productivity Points by Office";
			this.xAxisName = "Month";
			this.yAxisName = "Total Points";
			this.toolTipReport = "Productivity Points";
			String productivityPointByOfficeFusionXML = this.setGraphParameters(modelAndView, "chartWidth",
					"chartHeight", category, dataSetSeriesName, dataSetList, this.caption, this.xAxisName,
					this.yAxisName);
			modelAndView.addObject("productivityPointByOfficeFusionXML", productivityPointByOfficeFusionXML);
			modelAndView.addObject("chartWidth", "1160");
			modelAndView.addObject("chartHeight", "600");
			this.productivityPointAnalystByOffice(modelAndView, request, response);
			return modelAndView;
		} catch (CMSException var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		} catch (Exception var19) {
			return AtlasUtils.getJsonExceptionView(this.logger, var19, response);
		}
	}

	public void productivityPointAnalystByOffice(ModelAndView modelAndView, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.logger.debug("OfficePerformanceController : productivityPointAnalystByOffice");
		request.setAttribute("reportName", "productivityPointAnalystByOffice");
		String[] category = new String[12];
		ArrayList dataSetList = new ArrayList();

		try {
			List<OfficePerformanceVO> fullList = this.atlasReportService.getReport(request, response);
			this.logger.debug("list :" + fullList.size());
			String[] dataSetSeriesName = new String[fullList.size()];
			int indexDataSetSeriesName = 0;
			Set<String> monthSet = ReportConstants.fiscalYearFullMonthMap.keySet();
			Iterator<String> itr1 = monthSet.iterator();

			for (int var11 = 0; itr1.hasNext(); category[var11++] = (String) itr1.next()) {
				;
			}

			Iterator itr2 = fullList.iterator();

			while (itr2.hasNext()) {
				OfficePerformanceVO opvoOut = (OfficePerformanceVO) itr2.next();
				dataSetSeriesName[indexDataSetSeriesName++] = opvoOut.getOffice();
				List<OfficePerformanceVO> opvoInList = opvoOut.getOfficePerformanceVOList();
				this.logger.debug("number of revenues for month:" + opvoOut.getOffice() + ": is :" + opvoInList.size());
				float[] officeData = new float[12];

				OfficePerformanceVO opvoIn;
				for (Iterator itr3 = opvoInList.iterator(); itr3
						.hasNext(); officeData[Integer.parseInt(opvoIn.getMonth()) - 1] = opvoIn
								.getProductivitypointsByAnalyst()) {
					opvoIn = (OfficePerformanceVO) itr3.next();
					this.logger.debug(
							"Month [" + opvoIn.getMonth() + "] \tPoints:" + opvoIn.getProductivitypointsByAnalyst());
				}

				dataSetList.add(officeData);
			}

			String dataSetMV = "dataSetPointAnalystByOffice";
			String categoryMV = "categoryPointAnalystByOffice";
			this.caption = "Productivity Points/Analyst by Office";
			this.xAxisName = "Month";
			this.yAxisName = "Points/Analyst";
			this.toolTipReport = "Productivity Points/Analyst";
			String productivityPointAnalystByOfficeFusionXML = this.setGraphParameters(modelAndView, "chartWidth",
					"chartHeight", category, dataSetSeriesName, dataSetList, this.caption, this.xAxisName,
					this.yAxisName);
			modelAndView.addObject("productivityPointAnalystByOfficeFusionXML",
					productivityPointAnalystByOfficeFusionXML);
			modelAndView.addObject("chartWidth", "1160");
			modelAndView.addObject("chartHeight", "600");
			this.revenueAndAnalystByOfficePerformance(modelAndView, request, response);
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	public ModelAndView completedCaseByOffice(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("OfficePerformanceController : completedCaseByOffice");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		request.setAttribute("reportName", "completedCaseByOffice");
		String[] category = new String[12];
		ArrayList dataSetList = new ArrayList();

		try {
			List<OfficePerformanceVO> fullList = this.atlasReportService.getReport(request, response);
			this.logger.debug("list :" + fullList.size());
			String[] dataSetSeriesName = new String[fullList.size()];
			int indexDataSetSeriesName = 0;
			Set<String> monthSet = ReportConstants.fiscalYearFullMonthMap.keySet();
			Iterator<String> itr1 = monthSet.iterator();

			for (int var11 = 0; itr1.hasNext(); category[var11++] = (String) itr1.next()) {
				;
			}

			Iterator itr2 = fullList.iterator();

			while (itr2.hasNext()) {
				OfficePerformanceVO opvoOut = (OfficePerformanceVO) itr2.next();
				dataSetSeriesName[indexDataSetSeriesName++] = opvoOut.getOffice();
				List<OfficePerformanceVO> opvoInList = opvoOut.getOfficePerformanceVOList();
				this.logger.debug("number of revenues for month:" + opvoOut.getOffice() + ": is :" + opvoInList.size());
				float[] officeData = new float[12];

				OfficePerformanceVO opvoIn;
				for (Iterator itr3 = opvoInList.iterator(); itr3
						.hasNext(); officeData[Integer.parseInt(opvoIn.getMonth()) - 1] = (float) opvoIn
								.getCompletedCases()) {
					opvoIn = (OfficePerformanceVO) itr3.next();
					this.logger.debug("completedCaseByOffice > Month [" + opvoIn.getMonth() + "] \tPoints:"
							+ opvoIn.getCompletedCases());
				}

				dataSetList.add(officeData);
			}

			String dataSetMV = "dataSetCompleteCaseByOffice";
			String categoryMV = "categoryCompleteCaseByOffice";
			this.caption = "Completed Cases by Office";
			this.xAxisName = "Month";
			this.yAxisName = "Number of Cases";
			this.toolTipReport = "Completed Cases";
			String completedCaseByOfficeFusionXML = this.setGraphParameters(modelAndView, "chartWidth", "chartHeight",
					category, dataSetSeriesName, dataSetList, this.caption, this.xAxisName, this.yAxisName);
			modelAndView.addObject("completedCaseByOfficeFusionXML", completedCaseByOfficeFusionXML);
			modelAndView.addObject("chartWidth", "1160");
			modelAndView.addObject("chartHeight", "600");
			this.completedCaseAnalystByOffice(modelAndView, request, response);
			return modelAndView;
		} catch (CMSException var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		} catch (Exception var19) {
			return AtlasUtils.getJsonExceptionView(this.logger, var19, response);
		}
	}

	public void completedCaseAnalystByOffice(ModelAndView modelAndView, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.logger.debug("OfficePerformanceController : completedCaseAnalystByOffice");
		request.setAttribute("reportName", "completedCaseAnalystByOffice");
		String[] category = new String[12];
		ArrayList dataSetList = new ArrayList();

		try {
			List<OfficePerformanceVO> fullList = this.atlasReportService.getReport(request, response);
			this.logger.debug("list :" + fullList.size());
			String[] dataSetSeriesName = new String[fullList.size()];
			int indexDataSetSeriesName = 0;
			Set<String> monthSet = ReportConstants.fiscalYearFullMonthMap.keySet();
			Iterator<String> itr1 = monthSet.iterator();

			for (int var11 = 0; itr1.hasNext(); category[var11++] = (String) itr1.next()) {
				;
			}

			Iterator itr2 = fullList.iterator();

			while (itr2.hasNext()) {
				OfficePerformanceVO opvoOut = (OfficePerformanceVO) itr2.next();
				dataSetSeriesName[indexDataSetSeriesName++] = opvoOut.getOffice();
				List<OfficePerformanceVO> opvoInList = opvoOut.getOfficePerformanceVOList();
				this.logger.debug(
						"completedCase Analyst ByOffice >month:" + opvoOut.getOffice() + ": is :" + opvoInList.size());
				float[] officeData = new float[12];

				OfficePerformanceVO opvoIn;
				for (Iterator itr3 = opvoInList.iterator(); itr3
						.hasNext(); officeData[Integer.parseInt(opvoIn.getMonth()) - 1] = opvoIn
								.getCompletedCasesByAnalyst()) {
					opvoIn = (OfficePerformanceVO) itr3.next();
					this.logger
							.debug("Month [" + opvoIn.getMonth() + "] \tPoints:" + opvoIn.getCompletedCasesByAnalyst());
				}

				dataSetList.add(officeData);
			}

			String dataSetMV = "dataSetCompleteCaseAnalystByOffice";
			String categoryMV = "categoryCompleteCaseAnalystByOffice";
			this.caption = "Completed Cases/Analyst by Office";
			this.xAxisName = "Month";
			this.yAxisName = "Number of Cases/ Analyst";
			this.toolTipReport = "Completed Cases/Analyst";
			String completedCaseAnalystByOfficeFusionXML = this.setGraphParameters(modelAndView, "chartWidth",
					"chartHeight", category, dataSetSeriesName, dataSetList, this.caption, this.xAxisName,
					this.yAxisName);
			modelAndView.addObject("completedCaseAnalystByOfficeFusionXML", completedCaseAnalystByOfficeFusionXML);
			modelAndView.addObject("chartWidth", "1160");
			modelAndView.addObject("chartHeight", "600");
			this.revenueAndAnalystByOfficePerformance(modelAndView, request, response);
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	public ModelAndView revenueByOfficePerformance(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("OfficePerformanceController : revenueByOfficePerformance");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		request.setAttribute("reportName", "RevenueByOfficePerformance");
		String[] category = new String[12];
		ArrayList dataSetList = new ArrayList();

		try {
			List<OfficePerformanceVO> fullList = this.atlasReportService.getReport(request, response);
			String[] dataSetSeriesName = new String[fullList.size()];
			int indexDataSetSeriesName = 0;
			Set<String> monthSet = ReportConstants.fiscalYearFullMonthMap.keySet();
			Iterator<String> itr1 = monthSet.iterator();

			for (int var11 = 0; itr1.hasNext(); category[var11++] = (String) itr1.next()) {
				;
			}

			Iterator itr2 = fullList.iterator();

			while (itr2.hasNext()) {
				OfficePerformanceVO opvoOut = (OfficePerformanceVO) itr2.next();
				dataSetSeriesName[indexDataSetSeriesName++] = opvoOut.getOffice();
				List<OfficePerformanceVO> opvoInList = opvoOut.getOfficePerformanceVOList();
				this.logger.debug("number of revenues for month:" + opvoOut.getOffice() + ": is :" + opvoInList.size());
				float[] officeData = new float[12];

				OfficePerformanceVO opvoIn;
				for (Iterator itr3 = opvoInList.iterator(); itr3
						.hasNext(); officeData[Integer.parseInt(opvoIn.getMonth()) - 1] = opvoIn.getRevenue()) {
					opvoIn = (OfficePerformanceVO) itr3.next();
				}

				dataSetList.add(officeData);
			}

			this.caption = "Revenue by Office";
			this.xAxisName = "Month";
			this.yAxisName = "Total Revenue(USD)";
			this.toolTipReport = "Revenue";
			String revenueByOfficefusionXML = this.setGraphParameters(modelAndView, "chartWidth", "chartHeight",
					category, dataSetSeriesName, dataSetList, this.caption, this.xAxisName, this.yAxisName);
			modelAndView.addObject("revenueByOfficefusionXML", revenueByOfficefusionXML);
			this.revenueAndAnalystByOfficePerformance(modelAndView, request, response);
			modelAndView.addObject("chartWidth", "1160");
			modelAndView.addObject("chartHeight", "600");
			return modelAndView;
		} catch (CMSException var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		} catch (Exception var19) {
			return AtlasUtils.getJsonExceptionView(this.logger, var19, response);
		}
	}

	public void revenueAndAnalystByOfficePerformance(ModelAndView modelAndView, HttpServletRequest request,
			HttpServletResponse response) throws CMSException {
		this.logger.debug("OfficePerformanceController : revenueAndAnalystByOfficePerformance");
		request.setAttribute("reportName", "revenueAndAnalystByOfficePerformance");
		String[] category = new String[12];
		ArrayList dataSetList = new ArrayList();

		try {
			List<OfficePerformanceVO> fullList = this.atlasReportService.getReport(request, response);
			String[] dataSetSeriesName = new String[fullList.size()];
			int indexDataSetSeriesName = 0;
			Set<String> monthSet = ReportConstants.fiscalYearFullMonthMap.keySet();
			Iterator<String> itr1 = monthSet.iterator();

			for (int var11 = 0; itr1.hasNext(); category[var11++] = (String) itr1.next()) {
				;
			}

			Iterator itr2 = fullList.iterator();

			while (itr2.hasNext()) {
				OfficePerformanceVO opvoOut = (OfficePerformanceVO) itr2.next();
				dataSetSeriesName[indexDataSetSeriesName++] = opvoOut.getOffice();
				List<OfficePerformanceVO> opvoInList = opvoOut.getOfficePerformanceVOList();
				this.logger.debug("number of revenues for month:" + opvoOut.getOffice() + ": is :" + opvoInList.size());
				float[] officeData = new float[12];

				OfficePerformanceVO opvoIn;
				for (Iterator itr3 = opvoInList.iterator(); itr3
						.hasNext(); officeData[Integer.parseInt(opvoIn.getMonth()) - 1] = opvoIn
								.getRevenueByAnalyst()) {
					opvoIn = (OfficePerformanceVO) itr3.next();
				}

				dataSetList.add(officeData);
			}

			this.caption = "Revenue/Analyst by Office";
			this.xAxisName = "Month";
			this.yAxisName = "Total Revenue(USD)/Analyst";
			this.toolTipReport = "Revenue/Analyst";
			String revenueAndAnalystByOfficefusionXML = this.setGraphParameters(modelAndView, "chartWidth",
					"chartHeight", category, dataSetSeriesName, dataSetList, this.caption, this.xAxisName,
					this.yAxisName);
			modelAndView.addObject("revenueAndAnalystByOfficefusionXML", revenueAndAnalystByOfficefusionXML);
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	private String setGraphParameters(ModelAndView modelAndView, String chartWidth, String chartHeight,
			String[] category, String[] dataSetSeriesName, List<float[]> dataSetList, String caption, String xAxisName,
			String yAxisName) throws CMSException {
		this.logger.debug("In JSONOfficePerformanceController :: setGraphParameters");
		String fusionXMLString = "";
		String categories = "";
		String dataset = "";
		String[] colors = StringUtils.hexCodeGeneratorForColors();

		try {
			int dataSetSeriesNameIndex = 0;

			while (true) {
				if (dataSetSeriesNameIndex >= category.length) {
					for (dataSetSeriesNameIndex = 0; dataSetSeriesNameIndex < dataSetSeriesName.length; ++dataSetSeriesNameIndex) {
						String dataSeriesName = dataSetSeriesName[dataSetSeriesNameIndex];
						if (this.officeColorHMap.isEmpty() || !this.officeColorHMap.containsKey(dataSeriesName)) {
							this.officeColorHMap.put(dataSeriesName, colors[this.officeColorHMap.size()]);
							this.logger.debug("color for office :" + dataSeriesName + " is :"
									+ colors[this.officeColorHMap.size()]);
						}

						String datasetprefix = "<dataset seriesName='" + dataSeriesName + "' color='"
								+ (String) this.officeColorHMap.get(dataSeriesName) + "'  linethickness='4' >";
						String datasetsuffix = "</dataset>";
						float[] values = (float[]) dataSetList.get(dataSetSeriesNameIndex);
						String datasetMain = "";
						float[] arr$ = values;
						int len$ = values.length;

						for (int i$ = 0; i$ < len$; ++i$) {
							float value = arr$[i$];
							datasetMain = datasetMain + " <set value='" + StringUtils.Round(value, 2) + "' color='"
									+ (String) this.officeColorHMap.get(dataSeriesName) + "' toolText='"
									+ dataSetSeriesName[dataSetSeriesNameIndex] + "&nbsp;" + this.toolTipReport
									+ " :&nbsp;" + StringUtils.Round(value, 2) + "' link='' " + "label='"
									+ dataSetSeriesName[dataSetSeriesNameIndex] + "," + StringUtils.Round(value, 2)
									+ "'" + "/>";
						}

						dataset = dataset + datasetprefix + datasetMain + datasetsuffix;
					}

					fusionXMLString = "<chart showValues='0'  caption='" + caption + "' xAxisName='" + xAxisName
							+ "' yAxisName='" + yAxisName
							+ "' bgColor='FFFFFF' showValues='1'  legendPosition='RIGHT' labelDisplay='Stagger'>"
							+ " <categories>    " + categories + " </categories>    " + " " + dataset + "  "
							+ "<styles>" + "<definition>"
							+ "  <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />"
							+ " </definition>" + "<application>" + "   <apply toObject='Canvas' styles='CanvasAnim' />"
							+ "  </application> " + " </styles>" + "</chart>";
					break;
				}

				categories = categories + " <category label='" + category[dataSetSeriesNameIndex] + "'/>";
				++dataSetSeriesNameIndex;
			}
		} catch (Exception var24) {
			throw new CMSException(this.logger, var24);
		}

		this.logger.debug("Exiting JSONOfficePerformanceController :: setGraphParameters");
		return fusionXMLString;
	}
}