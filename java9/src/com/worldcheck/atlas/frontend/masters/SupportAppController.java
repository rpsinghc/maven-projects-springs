package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.interfaces.ISupportApp;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.SupportAppVO;
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

public class SupportAppController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.SupportAppController");
	private String SUPPORTAPPJSP = "supportApp";
	private static final String CASE_REFERENCE_NO = "CASE REFERENCE NO";
	private static final String CLIENT_CODE = "CLIENT CODE";
	private static final String CLIENT_NAME = "CLIENT NAME";
	private static final String CLIENT_FINAL_SENT_DATE = "CLIENT FINAL SENT DATE";
	private static final String SUBJECT_NAME = "SUBJECT NAME";
	private static final String SUBJECT_TYPE = "SUBJECT TYPE";
	private static final String SUBJECT_COUNTRY = "SUBJECT COUNTRY";
	private static final String RESEARCH_ELEMENT = "RESEARCH ELEMENT";
	private static final String CASE_STATUS = "CASE STATUS";
	private static final String ANALYST = "ANALYST";
	private static final String TEAM_TYPE = "TEAM TYPE";
	private static final String OFFICE = "OFFICE";
	private static final String JLPOINTS = "JLPOINTS";
	private static final String CASE_RECEIVED_DATE = "CASE_RECEIVED_DATE";
	private static final String TASK_NAME = "TASK_NAME";
	private static final String ACTION = "ACTION";
	private static final String OLD_INFO = "OLD_INFO";
	private static final String NEW_INFO = "NEW_INFO";
	private static final String PERFORMER = "PERFORMER";
	private static final String SERVER_TIME = "SERVER_TIME";
	private static final String RE_DATA = "MonthlyREData";
	private static final String CASE_HISTORY_DATA = "MonthlyCaseHistoryData";
	PropertyReaderUtil propertyReader = null;
	ISupportApp supportAppManager = null;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setSupportAppManager(ISupportApp supportAppManager) {
		this.supportAppManager = supportAppManager;
	}

	public ModelAndView supportPage(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView(this.SUPPORTAPPJSP);

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

	public ModelAndView exportToExcelSupport(HttpServletRequest request, HttpServletResponse response,
			SupportAppVO supportAppVO) {
		this.logger.debug("in export to excel");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			String dataType = request.getParameter("dataType");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("parameters :: " + excelParamMap);
			this.logger.debug("dataType :: " + dataType);
			new ArrayList();
			List<SupportAppVO> dataForExport = this.getDataForExport(dataType, excelParamMap);
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

	private List<SupportAppVO> getDataForExport(String dataType, Map<String, Object> excelParamMap)
			throws CMSException {
		Object dataForExport = new ArrayList();

		try {
			if (dataType.equals("MonthlyREData")) {
				dataForExport = this.supportAppManager.getREDataForExport(excelParamMap);
			} else if (dataType.equals("MonthlyCaseHistoryData")) {
				dataForExport = this.supportAppManager.getHistoryDataForExport(excelParamMap);
			}

			return (List) dataForExport;
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	private String writeToExcel(List<SupportAppVO> resultList, String dataType, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderList(dataType);
		List<List<String>> dataList = new ArrayList();
		SupportAppVO supportAppVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				List<String> datamap = new ArrayList();
				supportAppVO = (SupportAppVO) iterator.next();
				this.logger.debug("##Test support..supportAppVO.getClient_final_sent_date()::"
						+ supportAppVO.getClient_final_sent_date());
				this.populateDataMap(supportAppVO, dataType, datamap);
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
			if (dataType.equals("MonthlyREData")) {
				lstHeader.add("CASE REFERENCE NO");
				lstHeader.add("CLIENT CODE");
				lstHeader.add("CLIENT NAME");
				lstHeader.add("CLIENT FINAL SENT DATE");
				lstHeader.add("SUBJECT NAME");
				lstHeader.add("SUBJECT TYPE");
				lstHeader.add("SUBJECT COUNTRY");
				lstHeader.add("RESEARCH ELEMENT");
				lstHeader.add("CASE STATUS");
				lstHeader.add("ANALYST");
				lstHeader.add("TEAM TYPE");
				lstHeader.add("OFFICE");
				lstHeader.add("JLPOINTS");
			} else if (dataType.equals("MonthlyCaseHistoryData")) {
				lstHeader.add("CASE REFERENCE NO");
				lstHeader.add("CASE_RECEIVED_DATE");
				lstHeader.add("CLIENT FINAL SENT DATE");
				lstHeader.add("CASE STATUS");
				lstHeader.add("TASK_NAME");
				lstHeader.add("ACTION");
				lstHeader.add("OLD_INFO");
				lstHeader.add("NEW_INFO");
				lstHeader.add("PERFORMER");
				lstHeader.add("SERVER_TIME");
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

	private void populateDataMap(SupportAppVO supportAppVO, String dataType, List<String> datamap) {
		if (dataType.equals("MonthlyREData")) {
			datamap.add(String
					.valueOf(supportAppVO.getCase_reference_no() != null ? supportAppVO.getCase_reference_no() : ""));
			datamap.add(String.valueOf(supportAppVO.getClient_code() != null ? supportAppVO.getClient_code() : ""));
			datamap.add(String.valueOf(supportAppVO.getClient_name() != null ? supportAppVO.getClient_name() : ""));
			datamap.add(String.valueOf(supportAppVO.getClient_final_sent_date() != null
					? supportAppVO.getClient_final_sent_date().split(" ")[0]
					: ""));
			datamap.add(String.valueOf(supportAppVO.getSubject_name() != null ? supportAppVO.getSubject_name() : ""));
			datamap.add(String.valueOf(supportAppVO.getSubject_type() != null ? supportAppVO.getSubject_type() : ""));
			datamap.add(
					String.valueOf(supportAppVO.getSubject_country() != null ? supportAppVO.getSubject_country() : ""));
			datamap.add(String
					.valueOf(supportAppVO.getResearch_element() != null ? supportAppVO.getResearch_element() : ""));
			datamap.add(String.valueOf(supportAppVO.getCase_status() != null ? supportAppVO.getCase_status() : ""));
			datamap.add(String.valueOf(supportAppVO.getAnalyst() != null ? supportAppVO.getAnalyst() : ""));
			datamap.add(String.valueOf(supportAppVO.getTeam_type() != null ? supportAppVO.getTeam_type() : ""));
			datamap.add(String.valueOf(supportAppVO.getOffice() != null ? supportAppVO.getOffice() : ""));
			datamap.add(String.valueOf(supportAppVO.getJlppoints() != null ? supportAppVO.getJlppoints() : ""));
		} else if (dataType.equals("MonthlyCaseHistoryData")) {
			datamap.add(String
					.valueOf(supportAppVO.getCase_reference_no() != null ? supportAppVO.getCase_reference_no() : ""));
			datamap.add(String
					.valueOf(supportAppVO.getCaseReceivedDate() != null ? supportAppVO.getCaseReceivedDate() : ""));
			datamap.add(String.valueOf(
					supportAppVO.getClient_final_sent_date() != null ? supportAppVO.getClient_final_sent_date() : ""));
			datamap.add(String.valueOf(supportAppVO.getCase_status() != null ? supportAppVO.getCase_status() : ""));
			datamap.add(String.valueOf(supportAppVO.getTaskName() != null ? supportAppVO.getTaskName() : ""));
			datamap.add(String.valueOf(supportAppVO.getAction() != null ? supportAppVO.getAction() : ""));
			datamap.add(String.valueOf(supportAppVO.getOldInfo() != null ? supportAppVO.getOldInfo() : ""));
			datamap.add(String.valueOf(supportAppVO.getNewInfo() != null ? supportAppVO.getNewInfo() : ""));
			datamap.add(String.valueOf(supportAppVO.getPerformer() != null ? supportAppVO.getPerformer() : ""));
			datamap.add(String.valueOf(supportAppVO.getServerTime() != null ? supportAppVO.getServerTime() : ""));
		}

	}
}