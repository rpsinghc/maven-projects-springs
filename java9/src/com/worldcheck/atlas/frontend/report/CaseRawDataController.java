package com.worldcheck.atlas.frontend.report;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.report.ICaseRawDataReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.report.CaseRawTableVO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CaseRawDataController extends MultiActionController {
	private static final String EXCEL_DATA = "excelData";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.CaseRawDataController");
	private ICaseRawDataReport caseRawDataReport;
	private static final String LOGIN_USER = "loginUser";
	private static final String CASE_RAW_DATA_CUSTOM = "caseRawData_custom";
	private static final String LOAD_VALUE = "loadValue";
	private static final String FIRST_TIME = "firstTime";
	private static final String IS_CSV = "isCsv";
	private static final String VAR_TEMPLATE_ID = "varTemplateId";
	private static final String VAR_START_DATE = "varStartDate";
	private static final String VAR_DATE_TYPE = "varDateType";
	private static final String VAR_END_DATE = "varEndDate";
	private static final String VAR_TEMP_CREATOR = "varSearchTempCreator";
	private static final String VAR_TEMP_NAME = "varSearchTempName";
	private static final String VAR_SEARCH_TYPE = "hStrSearchType";
	private static final String VAR_START = "varStart";
	private static final String VAR_SORT = "varSort";
	private static final String VAR_LIMIT = "varLimit";
	private static final String VAR_DIR = "varDir";
	private static final String JLP_SUM_EXPRESSION = "NVL(JOB_LOADING_POINTS_PT,0)+ NVL(JOB_LOADING_POINTS_ST1,0)+ NVL(JOB_LOADING_POINTS_ST2,0)+ NVL(JOB_LOADING_POINTS_ST3,0)+ NVL(JOB_LOADING_POINTS_ST4,0)+ NVL(JOB_LOADING_POINTS_ST5,0)+ NVL(JOB_LOADING_POINTS_ST6,0) TOTAL_JOB_LOADING_POINTS";
	private ModelAndView mv = null;
	private static final String CRD_REPORT_NAME = "CaseRawData";

	public void setCaseRawDataReport(ICaseRawDataReport caseRawDataReport) {
		this.caseRawDataReport = caseRawDataReport;
	}

	public ModelAndView caseRawDataSearchJspRedirect(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");

		try {
			this.mv = new ModelAndView("caseRawData_custom");
			this.mv.addObject("loginUser", userBean.getUserName());
			this.mv.addObject("loadValue", "firstTime");
			if (request.getSession().getAttribute("excelData") != null
					&& request.getSession().getAttribute("excelData").equals("false")) {
				this.mv.addObject("excelData", "NODATA");
				this.setParamsInMv(this.mv, request);
				if (request.getSession().getAttribute("isCsv") != null) {
					request.getSession().removeAttribute("isCsv");
				}
			} else {
				this.mv.addObject("excelData", "DATA");
			}

			request.getSession().removeAttribute("excelData");
			if (request.getSession().getAttribute("isCsv") != null) {
				this.mv.addObject("isCsv", "true");
				request.getSession().removeAttribute("isCsv");
				if (request.getSession().getAttribute("excelData") != null) {
					request.getSession().removeAttribute("excelData");
				}

				this.setParamsInMv(this.mv, request);
			}
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}

		return this.mv;
	}

	private void setParamsInMv(ModelAndView modelAndView, HttpServletRequest request) throws CMSException {
		this.logger.debug("In CaseRawDataController::setParamsInMv");
		this.mv.addObject("varTemplateId", request.getSession().getAttribute("varTemplateId"));
		this.mv.addObject("varStartDate", request.getSession().getAttribute("varStartDate"));
		this.mv.addObject("varDateType", request.getSession().getAttribute("varDateType"));
		this.mv.addObject("varEndDate", request.getSession().getAttribute("varEndDate"));
		this.mv.addObject("varSearchTempCreator", request.getSession().getAttribute("varSearchTempCreator"));
		this.mv.addObject("varSearchTempName", request.getSession().getAttribute("varSearchTempName"));
		this.mv.addObject("varStart", request.getSession().getAttribute("varStart"));
		this.mv.addObject("varSort", request.getSession().getAttribute("varSort"));
		this.mv.addObject("varLimit", request.getSession().getAttribute("varLimit"));
		this.mv.addObject("varDir", request.getSession().getAttribute("varDir"));
		this.mv.addObject("hStrSearchType", request.getSession().getAttribute("hStrSearchType"));
		request.getSession().removeAttribute("varTemplateId");
		request.getSession().removeAttribute("varStartDate");
		request.getSession().removeAttribute("varDateType");
		request.getSession().removeAttribute("varEndDate");
		request.getSession().removeAttribute("varSearchTempCreator");
		request.getSession().removeAttribute("varSearchTempName");
		request.getSession().removeAttribute("varStart");
		request.getSession().removeAttribute("varSort");
		request.getSession().removeAttribute("varLimit");
		request.getSession().removeAttribute("varDir");
		request.getSession().removeAttribute("hStrSearchType");
		this.logger.debug("Exit CaseRawDataController::setParamsInMv");
	}

	private void setupCaseRawDataExcel(ModelAndView modelAndView, HttpServletRequest request) throws CMSException {
		this.logger.debug("Inside CaseRawDataController::setupCaseRawDataExcel");
		request.getSession().setAttribute("varTemplateId", request.getParameter("hTemplateId"));
		request.getSession().setAttribute("varStartDate", request.getParameter("hstartDate"));
		request.getSession().setAttribute("varDateType", request.getParameter("hdateType"));
		request.getSession().setAttribute("varEndDate", request.getParameter("hendDate"));
		request.getSession().setAttribute("varSearchTempCreator", request.getParameter("htempCreator"));
		request.getSession().setAttribute("varSearchTempName", request.getParameter("templatename"));
		request.getSession().setAttribute("varStart", request.getParameter("hstart"));
		request.getSession().setAttribute("varSort", request.getParameter("hsort"));
		request.getSession().setAttribute("varLimit", request.getParameter("hlimit"));
		request.getSession().setAttribute("varDir", request.getParameter("hdir"));
		request.getSession().setAttribute("hStrSearchType", request.getParameter("hStrSearchType"));
		this.logger.debug("Exit CaseRawDataController::setupCaseRawDataExcel");
	}

	public ModelAndView caseRawDataExportToExcel(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("In CaseRawDataController::caseRawDataExportToExcel");
		String buttonValue = "";
		String buttonName = "";
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");

		label94 : {
			ModelAndView var15;
			try {
				request.getSession().setAttribute("excelGeneration", new Date());
				String templateId = request.getParameter("hTemplateId");
				String dateType = request.getParameter("hdateType");
				String startDate = request.getParameter("hstartDate");
				String endDate = request.getParameter("hendDate");
				int cellsInResultset = false;
				int rowsInResultset = false;
				buttonValue = request.getParameter("crdExcelButton").toString();
				this.logger.debug("Button Value:::" + buttonValue);
				if (buttonValue.equals("0")) {
					buttonName = "crdExcelButton";
					this.logger.debug("button name::" + buttonName);
				}

				if (buttonValue.equals("1")) {
					buttonName = "crdSubjectButton";
					this.logger.debug("button name::" + buttonName);
				}

				if (buttonValue.equals("2")) {
					buttonName = "crdJlpButton";
					this.logger.debug("Button Value:::" + buttonValue);
				}

				this.logger.debug("\nTemplateId:" + templateId + "\ndateType:" + dateType + "\nstartDate:" + startDate
						+ "\nEndDate:" + endDate);
				HashMap<String, Integer> resultMap = this.caseRawDataReport.crdExcelCount(templateId, dateType,
						startDate, endDate, buttonName);
				int cellsInResultset = (Integer) resultMap.get("cellsInExportedExcel");
				int rowsInResultset = (Integer) resultMap.get("rowCount");
				if (rowsInResultset > 0) {
					this.logger.debug("export as CSV");
					request.getSession().setAttribute("isCsv", "true");
					this.mv = new ModelAndView("redirect:caseRawDataSearchJspRedirect.do");
					this.mv.addObject("loginUser", userBean.getUserName());
					this.setupCaseRawDataExcel(this.mv, request);
				} else {
					this.mv = new ModelAndView("redirect:caseRawDataSearchJspRedirect.do");
					this.mv.addObject("loginUser", userBean.getUserName());
					request.getSession().setAttribute("excelData", "false");
					this.setupCaseRawDataExcel(this.mv, request);
				}
				break label94;
			} catch (Exception var18) {
				var15 = AtlasUtils.getExceptionView(this.logger, var18);
			} finally {
				if (request.getSession().getAttribute("excelGeneration") != null) {
					request.getSession().removeAttribute("excelGeneration");
				}

			}

			return var15;
		}

		this.logger.debug("Out CaseRawDataController::caseRawDataExportToExcel");
		return this.mv;
	}

	private String writeToExcel(List<HashMap> caseRawDataMapList, HttpServletResponse response,
			List<String> selectedColumnsList) throws CMSException, IOException, SQLException {
		try {
			this.logger.debug("CRD controller ::: writeToExcel");
			if (selectedColumnsList.contains(
					"NVL(JOB_LOADING_POINTS_PT,0)+ NVL(JOB_LOADING_POINTS_ST1,0)+ NVL(JOB_LOADING_POINTS_ST2,0)+ NVL(JOB_LOADING_POINTS_ST3,0)+ NVL(JOB_LOADING_POINTS_ST4,0)+ NVL(JOB_LOADING_POINTS_ST5,0)+ NVL(JOB_LOADING_POINTS_ST6,0) TOTAL_JOB_LOADING_POINTS")) {
				selectedColumnsList.remove(
						"NVL(JOB_LOADING_POINTS_PT,0)+ NVL(JOB_LOADING_POINTS_ST1,0)+ NVL(JOB_LOADING_POINTS_ST2,0)+ NVL(JOB_LOADING_POINTS_ST3,0)+ NVL(JOB_LOADING_POINTS_ST4,0)+ NVL(JOB_LOADING_POINTS_ST5,0)+ NVL(JOB_LOADING_POINTS_ST6,0) TOTAL_JOB_LOADING_POINTS");
			}

			this.logger.debug("No of selectedColumnsList Column:::::::" + selectedColumnsList.size());
			List<List<String>> dataList = new ArrayList();

			ArrayList subDataList;
			label73 : for (Iterator var6 = caseRawDataMapList.iterator(); var6.hasNext(); dataList.add(subDataList)) {
				HashMap hashMap = (HashMap) var6.next();
				subDataList = new ArrayList();
				Iterator iterator = selectedColumnsList.iterator();

				while (true) {
					String templateCol;
					do {
						do {
							if (!iterator.hasNext()) {
								continue label73;
							}

							templateCol = (String) iterator.next();
						} while (templateCol == null);
					} while (templateCol.isEmpty());

					if ((templateCol.equalsIgnoreCase("OTHER_SUBJECTS")
							|| templateCol.equalsIgnoreCase("OTHER_SUBJECT_COUNTRIES")
							|| templateCol.equalsIgnoreCase("CASE_DETAILS")) && hashMap.get(templateCol) != null
							&& hashMap.get(templateCol) instanceof Clob) {
						String clobValue = this
								.convertInputStreamToString(((Clob) hashMap.get(templateCol)).getCharacterStream());
						if (clobValue != null && !clobValue.isEmpty() && clobValue.length() > 0
								&& clobValue.length() > 5000) {
							clobValue = clobValue.substring(0, 5000);
							clobValue = clobValue + "...";
						}

						hashMap.put(templateCol, clobValue);
					}

					boolean flag = hashMap.get(templateCol) != null
							? this.checkDataType(hashMap.get(templateCol))
							: false;
					if (flag) {
						Date date = new Date(((Timestamp) hashMap.get(templateCol)).getTime());
						SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MM-yyyy");
						subDataList.add(targetSdf.format(date));
					} else {
						subDataList.add(String.valueOf(hashMap.get(templateCol)));
					}
				}
			}

			return ExcelDownloader.writeToExcel1(selectedColumnsList, dataList, "CaseRawData", (short) 0, (short) 1,
					response, "CaseRawData");
		} catch (ClassCastException var13) {
			throw new CMSException(this.logger, var13);
		} catch (NullPointerException var14) {
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	public String convertInputStreamToString(Reader reader) throws IOException {
		if (reader != null) {
			StringBuilder sb = new StringBuilder();

			try {
				BufferedReader r1 = new BufferedReader(reader);

				String line;
				while ((line = r1.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				reader.close();
			}

			return sb.toString();
		} else {
			return "";
		}
	}

	private boolean checkDataType(Object dataObj) {
		boolean isTimeStampDataType = false;
		isTimeStampDataType = dataObj.getClass() == Timestamp.class;
		return isTimeStampDataType;
	}

	public ModelAndView writeCRDToCSV(HttpServletRequest request, HttpServletResponse response,
			CaseRawTableVO caseRawTableVO) {
		this.logger.debug("In CaseRawDataController :: writeCRDToCSV");
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String fileName = "";
		String buttonValue = "";
		String buttonName = "";
		int rowsInResultset = false;
		ModelAndView modelandview = null;

		label94 : {
			ModelAndView var16;
			try {
				modelandview = new ModelAndView("excelDownloadPopupZip");
				request.getSession().setAttribute("excelGeneration", new Date());
				String templateId = request.getParameter("hTemplateId");
				String dateType = request.getParameter("hdateType");
				String startDate = request.getParameter("hstartDate");
				String endDate = request.getParameter("hendDate");
				buttonValue = request.getParameter("crdExcelButton").toString();
				this.logger.debug("buttonValue:::" + buttonValue);
				this.logger.debug("\nTemplateId:" + templateId + "\ndateType:" + dateType + "\nstartDate:" + startDate
						+ "\nEndDate:" + endDate);
				if (buttonValue.equals("0")) {
					buttonName = "crdExcelButton";
					this.logger.debug("button name::" + buttonName);
				}

				if (buttonValue.equals("1")) {
					buttonName = "crdSubjectButton";
					this.logger.debug("button name::" + buttonName);
				}

				if (buttonValue.equals("2")) {
					buttonName = "crdJlpButton";
					this.logger.debug("Button Value:::" + buttonValue);
				}

				this.logger.debug("Button Value:::" + buttonValue);
				this.logger.debug("\nTemplateId:" + templateId + "\ndateType:" + dateType + "\nstartDate:" + startDate
						+ "\nEndDate:" + endDate);
				caseRawTableVO = this.caseRawDataReport.caseRawDataExportToExcel(templateId, dateType, startDate,
						endDate, buttonName);
				this.logger.debug("rows in resultset" + caseRawTableVO.getExcelDataList().size());
				if (caseRawTableVO.getExcelDataList().size() > 0) {
					fileName = this.writeToCsv(caseRawTableVO.getExcelDataList(), response, request,
							caseRawTableVO.getFieldName());
					modelandview.addObject("fileName", fileName);
					request.getSession().setAttribute("excelData", "true");
				} else {
					modelandview = new ModelAndView("redirect:caseRawDataSearchJspRedirect.do");
					request.getSession().setAttribute("excelData", "false");
				}
				break label94;
			} catch (Exception var19) {
				var16 = AtlasUtils.getExceptionView(this.logger, var19);
			} finally {
				if (request.getSession().getAttribute("excelGeneration") != null) {
					request.getSession().removeAttribute("excelGeneration");
				}

			}

			return var16;
		}

		this.logger.debug("Out CaseRawDataController :: writeCRDToCSV");
		return modelandview;
	}

	private String writeToCsv(List<HashMap> caseRawDataMapList, HttpServletResponse response,
			HttpServletRequest request, List<String> selectedColumnsList) throws CMSException {
		try {
			this.logger.debug("In CaseRawDataController :: writeToCsv @" + new Date());
			if (selectedColumnsList.contains(
					"NVL(JOB_LOADING_POINTS_PT,0)+ NVL(JOB_LOADING_POINTS_ST1,0)+ NVL(JOB_LOADING_POINTS_ST2,0)+ NVL(JOB_LOADING_POINTS_ST3,0)+ NVL(JOB_LOADING_POINTS_ST4,0)+ NVL(JOB_LOADING_POINTS_ST5,0)+ NVL(JOB_LOADING_POINTS_ST6,0) TOTAL_JOB_LOADING_POINTS")) {
				selectedColumnsList.remove(
						"NVL(JOB_LOADING_POINTS_PT,0)+ NVL(JOB_LOADING_POINTS_ST1,0)+ NVL(JOB_LOADING_POINTS_ST2,0)+ NVL(JOB_LOADING_POINTS_ST3,0)+ NVL(JOB_LOADING_POINTS_ST4,0)+ NVL(JOB_LOADING_POINTS_ST5,0)+ NVL(JOB_LOADING_POINTS_ST6,0) TOTAL_JOB_LOADING_POINTS");
			}

			List<List<String>> dataList = new ArrayList();

			ArrayList subDataList;
			label69 : for (Iterator var7 = caseRawDataMapList.iterator(); var7.hasNext(); dataList.add(subDataList)) {
				HashMap hashMap = (HashMap) var7.next();
				subDataList = new ArrayList();
				Iterator iterator = selectedColumnsList.iterator();

				while (true) {
					String templateCol;
					do {
						do {
							if (!iterator.hasNext()) {
								continue label69;
							}

							templateCol = (String) iterator.next();
						} while (templateCol == null);
					} while (templateCol.isEmpty());

					if ((templateCol.equalsIgnoreCase("OTHER_SUBJECTS")
							|| templateCol.equalsIgnoreCase("OTHER_SUBJECT_COUNTRIES")
							|| templateCol.equalsIgnoreCase("CASE_DETAILS")) && hashMap.get(templateCol) != null
							&& hashMap.get(templateCol) instanceof Clob) {
						String clobValue = this
								.convertInputStreamToString(((Clob) hashMap.get(templateCol)).getCharacterStream());
						if (clobValue != null && !clobValue.isEmpty() && clobValue.length() > 32000) {
							clobValue = clobValue.substring(0, 32000);
							clobValue = clobValue + "...";
						}

						hashMap.put(templateCol, clobValue);
					}

					boolean flag = hashMap.get(templateCol) != null
							? this.checkDataType(hashMap.get(templateCol))
							: false;
					if (flag) {
						Date date = new Date(((Timestamp) hashMap.get(templateCol)).getTime());
						SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MM-yyyy");
						subDataList.add(targetSdf.format(date));
					} else {
						subDataList.add(String.valueOf(hashMap.get(templateCol)));
					}
				}
			}

			String fileName = "";
			fileName = CSVDownloader.exportCSV(selectedColumnsList, dataList, "CaseRawData", response);
			return fileName;
		} catch (CMSException var14) {
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}
}