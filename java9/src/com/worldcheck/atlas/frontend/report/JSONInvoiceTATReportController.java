package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.InvoiceTATReportVO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONInvoiceTATReportController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONInvoiceTATReportController");
	private static final String CHART_WIDTH_VALUE = "630";
	private static final String CHART_HEIGHT_VALUE = "340";
	private static final String CHART_WIDTH = "chartWidth";
	private static final String CHART_HEIGHT = "chartHeight";
	private static final String FUSION_XML = "fusionXML";
	private AtlasReportService atlasReportService;

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public ModelAndView viewInvoiceTATReport(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In InvoiceTATReportController : viewInvoiceTATReport");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			request.setAttribute("reportName", "invoiceTATReport");
			List<InvoiceTATReportVO> result = this.atlasReportService.getReport(request, response);
			HashMap<String, Integer> resultMap = new HashMap();
			Iterator iterator = result.iterator();

			while (iterator.hasNext()) {
				InvoiceTATReportVO invoiceTATReportVO = (InvoiceTATReportVO) iterator.next();
				String key = "Within 48 Hours";
				int value = Integer.parseInt(invoiceTATReportVO.getWithin48Hours());
				resultMap.put(key, value);
				key = "Over 48 Hours";
				value = Integer.parseInt(invoiceTATReportVO.getOver48Hours());
				resultMap.put(key, value);
			}

			this.setGraphParameters(modelAndView, "chartWidth", "chartHeight", resultMap);
		} catch (CMSException var10) {
			AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}

		return modelAndView;
	}

	private void setGraphParameters(ModelAndView modelAndView, String chartWidth, String chartHeight,
			HashMap<String, Integer> resultMap) throws CMSException {
		this.logger.debug("In JSONGeneratetRevenueSummaryChartController :: setGraphParameters");

		try {
			String fusionXMLString = "";
			String dataset = "";
			String[] colors = new String[]{"d4e2f4", "0066B3"};
			int iterateColors = 0;
			Iterator mapIterator = resultMap.keySet().iterator();

			while (true) {
				if (!mapIterator.hasNext()) {
					fusionXMLString = "<chart bgColor='FFFFFF' showPercentValues='1'  legendPosition='BOTTOM' labelDisplay='Stagger'> "
							+ dataset + "  " + " <styles>  " + " <definition> "
							+ " <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />"
							+ " <style type='font' name='CaptionFont' size='10' color='666666' />"
							+ " <style type='font' name='SubCaptionFont' bold='0'/>"
							+ " <style name='myHTMLFont' type='font' isHTML='1' />" + " </definition>  <application>  "
							+ " <apply toObject='Canvas' styles='CanvasAnim' />  "
							+ " <apply toObject='caption' styles='CaptionFont'/>   <apply toObject='SubCaption' styles='SubCaptionFont'/>"
							+ " <apply toObject='DATALABELS' styles='CaptionFont'/> <apply toObject='YAXISVALUES' styles='CaptionFont'/>"
							+ " <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles></chart>";
					modelAndView.addObject("fusionXML", fusionXMLString);
					modelAndView.addObject("chartWidth", "630");
					modelAndView.addObject("chartHeight", "340");
					break;
				}

				String key = (String) mapIterator.next();
				int value = (Integer) resultMap.get(key);
				dataset = dataset + " <set value='" + value + "' color='" + colors[iterateColors++] + "' toolText='"
						+ key + "%26lt;BR%26gt;cases : " + value + "' link='' label='" + key + "," + value + "'" + "/>";
			}
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		this.logger.debug("Exiting JSONInvoiceTATReportController :: setGraphParameters");
	}
}