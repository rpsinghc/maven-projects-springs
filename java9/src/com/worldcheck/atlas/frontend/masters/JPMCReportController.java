package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.masters.JPMCReportManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.JPMCReportAppVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JPMCReportController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JPMCReportController");
	private String JPMCREPORTAPPJSP = "jpmcReportApp";
	private static final String JPMC_Report_Data = "JPMC_Daily_Report_Data";
	private static final String CASE_REFERENCE_NO = "CASE REFERENCE NO";
	private static final String CLIENT_CODE = "CLIENT CODE";
	private static final String CLIENT_NAME = "CLIENT NAME";
	private static final String CASE_STATUS = "CASE STATUS";
	private static final String CASE_FEE = "CASE FEE";
	private static final String REQUEST_RECEIVED_DATE = "REQUEST RECEIVED DATE";
	private static final String FINAL_DUE_DATE = "FINAL DUE DATE";
	private static final String CLIENT_FINAL_SENT_DATE = "CLIENT FINAL SENT DATE";
	private static final String SUBJECT_TYPE = "SUBJECT TYPE";
	private static final String SUBJECT_NAME = "SUBJECT NAME";
	private static final String SUBJECT_COUNTRY = "SUBJECT COUNTRY";
	private static final String OTHER_DETAILS = "OTHER_DETAILS";
	private static final String RESEARCH_ELEMENT_ORIGINAL = "RESEARCH ELEMENT ORIGINAL";
	JPMCReportManager jpmcReportManager = null;
	PropertyReaderUtil propertyReader = null;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setJpmcReportManager(JPMCReportManager jpmcReportManager) {
		this.jpmcReportManager = jpmcReportManager;
	}

	public ModelAndView exportToExcelJPMCReport(HttpServletRequest request, HttpServletResponse response,
			JPMCReportAppVO jpmcReportAppVO) {
		this.logger.debug("in export to excel");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			String dataType = request.getParameter("dataType");
			this.logger.debug("dataType :: " + dataType);
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("parameters :: " + excelParamMap);
			this.logger.debug("dataType :: " + dataType);
			new ArrayList();
			List<JPMCReportAppVO> dataForExport = this.getDataForExport(dataType, excelParamMap);
			this.logger.debug("number of records " + dataForExport.size());
			String fileName = this.writeToExcel(dataForExport, dataType, response);
			modelandview = new ModelAndView("misExcelDownloadPopup");
			modelandview.addObject("fileName", fileName);
			return modelandview;
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	private List<JPMCReportAppVO> getDataForExport(String dataType, Map<String, Object> excelParamMap)
			throws CMSException {
		Object dataForExport = new ArrayList();

		try {
			if (dataType.equals("JPMC_Daily_Report_Data")) {
				dataForExport = this.jpmcReportManager.getJPMCDataForExport(excelParamMap);
			}

			return (List) dataForExport;
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private String writeToExcel(List<JPMCReportAppVO> resultList, String dataType, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderList(dataType);
		List<List<String>> dataList = new ArrayList();
		JPMCReportAppVO jpmcReportAppVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				List<String> datamap = new ArrayList();
				jpmcReportAppVO = (JPMCReportAppVO) iterator.next();
				this.populateDataMap(jpmcReportAppVO, dataType, datamap);
				dataList.add(datamap);
			}

			return CSVDownloader.exportCSV(lstHeader, dataList, dataType, response);
		} catch (UnsupportedOperationException var9) {
			throw new CMSException(this.logger, var9);
		} catch (ClassCastException var10) {
			throw new CMSException(this.logger, var10);
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (IllegalArgumentException var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private List<String> getHeaderList(String dataType) throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			if (dataType.equals("JPMC_Daily_Report_Data")) {
				lstHeader.add("CASE REFERENCE NO");
				lstHeader.add("CLIENT CODE");
				lstHeader.add("CLIENT NAME");
				lstHeader.add("CASE STATUS");
				lstHeader.add("CASE FEE");
				lstHeader.add("REQUEST RECEIVED DATE");
				lstHeader.add("FINAL DUE DATE");
				lstHeader.add("CLIENT FINAL SENT DATE");
				lstHeader.add("SUBJECT TYPE");
				lstHeader.add("SUBJECT NAME");
				lstHeader.add("SUBJECT COUNTRY");
				lstHeader.add("OTHER_DETAILS");
				lstHeader.add("RESEARCH ELEMENT ORIGINAL");
			}

			return lstHeader;
		} catch (UnsupportedOperationException var4) {
			throw new CMSException(this.logger, var4);
		} catch (ClassCastException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (IllegalArgumentException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private void populateDataMap(JPMCReportAppVO jpmcReportAppVO, String dataType, List<String> datamap) {
		if (dataType.equals("JPMC_Daily_Report_Data")) {
			datamap.add(String.valueOf(
					jpmcReportAppVO.getCase_reference_no() != null ? jpmcReportAppVO.getCase_reference_no() : ""));
			datamap.add(
					String.valueOf(jpmcReportAppVO.getClient_code() != null ? jpmcReportAppVO.getClient_code() : ""));
			datamap.add(
					String.valueOf(jpmcReportAppVO.getClient_name() != null ? jpmcReportAppVO.getClient_name() : ""));
			datamap.add(
					String.valueOf(jpmcReportAppVO.getCase_status() != null ? jpmcReportAppVO.getCase_status() : ""));
			datamap.add(String.valueOf(jpmcReportAppVO.getCase_fee() != null ? jpmcReportAppVO.getCase_fee() : ""));
			datamap.add(String.valueOf(jpmcReportAppVO.getRequest_received_date() != null
					? jpmcReportAppVO.getRequest_received_date().split(" ")[0]
					: ""));
			datamap.add(String.valueOf(jpmcReportAppVO.getFinal_due_date() != null
					? jpmcReportAppVO.getFinal_due_date().split(" ")[0]
					: ""));
			datamap.add(String.valueOf(jpmcReportAppVO.getClient_final_sentdate() != null
					? jpmcReportAppVO.getClient_final_sentdate().split(" ")[0]
					: ""));
			datamap.add(
					String.valueOf(jpmcReportAppVO.getSubject_type() != null ? jpmcReportAppVO.getSubject_type() : ""));
			datamap.add(
					String.valueOf(jpmcReportAppVO.getSubject_name() != null ? jpmcReportAppVO.getSubject_name() : ""));
			datamap.add(String
					.valueOf(jpmcReportAppVO.getSubject_country() != null ? jpmcReportAppVO.getSubject_country() : ""));
			datamap.add(String
					.valueOf(jpmcReportAppVO.getOther_details() != null ? jpmcReportAppVO.getOther_details() : ""));
			datamap.add(String.valueOf(jpmcReportAppVO.getResearch_element_original() != null
					? jpmcReportAppVO.getResearch_element_original()
					: ""));
		}

	}

	public ModelAndView jpmcReportPage(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView(this.JPMCREPORTAPPJSP);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			long userID = ResourceLocator.self().getSBMService().getUserID(userMasterVO.getUserID());
			this.logger.debug("goTo Support App Page User Name is (R-1.3.1)>>>>>  " + userName);
			userMasterVO.setUserMasterId(userID);
			this.logger.debug("VO have User ID is (R-1.3.1)>>>>>" + userMasterVO.getUserID());
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}
}