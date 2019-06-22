package com.worldcheck.atlas.frontend.report;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.report.MyPerformanceMultiactionManager;
import com.worldcheck.atlas.dao.report.ChartReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.vo.report.MyPerformanceReportVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONMyPerformanceMultiactionController extends JSONMultiActionController {
	private String JSONVIEW = "jsonView";
	private AtlasReportService atlasReportService;
	private ChartReportDAO chartReportDAO;
	private String YEAR = "year";
	private String RESULTLIST = "resultList";
	private static final String CHART_WIDTH_VALUE = "630";
	private static final String CHART_HEIGHT_VALUE = "340";
	private static final String CHART_WIDTH = "chartWidth";
	private static final String CHART_HEIGHT = "chartHeight";
	private static final String FUSION_XML_CRN = "fusionXMLCRN";
	private static final String FUSION_XML_POINT = "fusionXMLPOINT";
	private static final String[] colors = new String[]{"0066B3", "FFFF00", "00DE48", "EA0000", "0DF4EA", "970099",
			"FFA258", "7A7A7A", "35638B", "FFF491", "458C70", "F98072"};
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONMyPerformanceMultiactionController");

	public void setChartReportDAO(ChartReportDAO chartReportDAO) {
		this.chartReportDAO = chartReportDAO;
	}

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView searchMyPerformance(HttpServletRequest request, HttpServletResponse response,
			MyPerformanceReportVO myPerformanceReportVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.JSONVIEW);
		this.logger.debug("Inside the Json controller of My Performance");

		try {
			request.setAttribute("reportName", "myPerformanceReport");
			List<HashMap<String, HashMap<String, String>>> ListreportData = this.atlasReportService.getReport(request,
					response);
			modelAndView.addObject(this.RESULTLIST, ListreportData);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (NullPointerException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView numOfCasesByUser(HttpServletRequest request, HttpServletResponse response,
			MyPerformanceReportVO myPerformanceReportVO) throws Exception {
		this.logger.debug("Inside the Json controller of Graph My Performance");
		ModelAndView modelAndView = new ModelAndView(this.JSONVIEW);
		this.logger.debug(request.getParameter(this.YEAR));
		String[] category = new String[12];
		String[] dataSetSeriesName = new String[]{"Completed", "In Progress"};
		List<float[]> crnDataSetList = new ArrayList();
		ArrayList pointDataSetList = new ArrayList();

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userID = userBean.getUserName();
			this.logger.debug("yearrrrrrrrrrrrrrrrrr" + myPerformanceReportVO.getYear());
			myPerformanceReportVO.setAnalyst(userID);
			List<MyPerformanceReportVO> noOfCasesDetail = this.chartReportDAO
					.getNumberOfCasesMyPerformance(myPerformanceReportVO);
			Set<String> monthSet = ReportConstants.fullmonthMap.keySet();
			Iterator<String> monthItr = monthSet.iterator();

			for (int var14 = 0; monthItr.hasNext(); category[var14++] = (String) monthItr.next()) {
				;
			}

			Iterator<MyPerformanceReportVO> listItr = noOfCasesDetail.iterator();
			float[] completedArray = new float[12];
			float[] wipArray = new float[12];
			float[] cmpJLPArray = new float[12];

			float[] wipJLPArray;
			MyPerformanceReportVO mprvo;
			for (wipJLPArray = new float[12]; listItr
					.hasNext(); wipJLPArray[Integer.parseInt(mprvo.getMonth()) - 1] = Float
							.parseFloat(mprvo.getWipJLP())) {
				mprvo = (MyPerformanceReportVO) listItr.next();
				completedArray[Integer.parseInt(mprvo.getMonth()) - 1] = Float.parseFloat(mprvo.getNumOfCasesCMP());
				wipArray[Integer.parseInt(mprvo.getMonth()) - 1] = Float.parseFloat(mprvo.getNumOfCasesWIP());
				cmpJLPArray[Integer.parseInt(mprvo.getMonth()) - 1] = Float.parseFloat(mprvo.getCmpJLP());
			}

			crnDataSetList.add(completedArray);
			crnDataSetList.add(wipArray);
			String chartName = "crnChart";
			this.setGraphParameters(chartName, modelAndView, "630", "340", "month", "Count of Cases", category,
					dataSetSeriesName, crnDataSetList);
			chartName = "pointChart";
			pointDataSetList.add(cmpJLPArray);
			pointDataSetList.add(wipJLPArray);
			this.setGraphParameters(chartName, modelAndView, "630", "340", "month", "Total Points", category,
					dataSetSeriesName, pointDataSetList);
			return modelAndView;
		} catch (CMSException var21) {
			return AtlasUtils.getJsonExceptionView(this.logger, var21, response);
		} catch (NullPointerException var22) {
			return AtlasUtils.getJsonExceptionView(this.logger, var22, response);
		} catch (Exception var23) {
			return AtlasUtils.getJsonExceptionView(this.logger, var23, response);
		}
	}

	private void setGraphParameters(String chart, ModelAndView modelAndView, String chartWidth, String chartHeight,
			String xAxisName, String yAxisName, String[] category, String[] dataSetSeries, List<float[]> dataSetList)
			throws CMSException {
		this.logger.debug("In JSONMyPerformanceMultiactionController :: setGraphParameters");

		try {
			String fusionXMLString = "";
			String categories = "";
			String dataset = "";
			int iterateColors = false;

			int j;
			for (j = 0; j < category.length; ++j) {
				categories = categories + " <category label='" + category[j] + "'/>";
			}

			for (j = 0; j < dataSetSeries.length; ++j) {
				String datasetprefix = "<dataset seriesName='" + dataSetSeries[j] + "'>";
				String datasetSuffix = "</dataset>";
				String datasetMain = "";
				float[] values = (float[]) dataSetList.get(j);
				float[] arr$ = values;
				int len$ = values.length;

				for (int i$ = 0; i$ < len$; ++i$) {
					float value = arr$[i$];
					datasetMain = datasetMain + "<set value='" + value + "'/>";
				}

				boolean zeroFlag = true;
				Iterator i$ = dataSetList.iterator();

				while (i$.hasNext()) {
					float[] values1 = (float[]) i$.next();
					float[] arr$ = values1;
					int len$ = values1.length;

					for (int i$ = 0; i$ < len$; ++i$) {
						float value = arr$[i$];
						if (value == 0.0F && zeroFlag) {
							zeroFlag = true;
						} else {
							zeroFlag = false;
						}
					}
				}

				if (zeroFlag) {
					categories = "";
					datasetMain = "";
				}

				dataset = dataset + datasetprefix + datasetMain + datasetSuffix;
			}

			fusionXMLString = "<chart xAxisName='" + xAxisName + "' yAxisName='" + yAxisName
					+ "' bgColor='FFFFFF' legendPosition='BOTTOM' labelDisplay='Stagger'>" + " <categories>    "
					+ categories + " </categories>    " + dataset + " <styles>  " + " <definition> "
					+ " <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />"
					+ " <style type='font' name='CaptionFont' size='10' color='666666' />"
					+ " <style type='font' name='SubCaptionFont' bold='0'/>"
					+ " <style name='myHTMLFont' type='font' isHTML='1' />" + " </definition>  <application>  "
					+ " <apply toObject='Canvas' styles='CanvasAnim' />  "
					+ " <apply toObject='caption' styles='CaptionFont'/>   <apply toObject='SubCaption' styles='SubCaptionFont'/>"
					+ " <apply toObject='DATALABELS' styles='CaptionFont'/> <apply toObject='YAXISVALUES' styles='CaptionFont'/>"
					+ " <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles></chart>";
			modelAndView.addObject("chartWidth", chartWidth);
			modelAndView.addObject("chartHeight", chartHeight);
			if (chart.equalsIgnoreCase("crnChart")) {
				modelAndView.addObject("fusionXMLCRN", fusionXMLString);
			} else {
				modelAndView.addObject("fusionXMLPOINT", fusionXMLString);
			}
		} catch (Exception var26) {
			throw new CMSException(this.logger, var26);
		}

		this.logger.debug("Exiting JSONMyPerformanceMultiactionController :: setGraphParameters");
	}

	public ModelAndView getCRNS(HttpServletRequest request, HttpServletResponse response,
			MyPerformanceReportVO myPerformanceReportVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.JSONVIEW);
		this.logger.debug("Inside gettin CRN Values.");
		this.logger.debug(request.getParameter("monthVal"));
		this.logger.debug(request.getParameter("reportType"));
		this.logger.debug(request.getParameter("yearVal"));

		try {
			request.setAttribute("reportName", "myPerformanceReport");
			HashMap<String, String> hm = new HashMap();
			hm.put("month", request.getParameter("monthVal"));
			hm.put("reportType", request.getParameter("reportType"));
			hm.put("year", request.getParameter("yearVal"));
			hm.put("userId", ((UserBean) request.getSession().getAttribute("userBean")).getUserName());
			this.logger.debug((String) hm.get("userId"));
			MyPerformanceMultiactionManager crnListManager = (MyPerformanceMultiactionManager) this.atlasReportService
					.getReportObject(request);
			List<MyPerformanceReportVO> crnNum = crnListManager.getCRNS(hm);
			modelAndView.addObject(this.RESULTLIST, crnNum);
			return modelAndView;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}
}