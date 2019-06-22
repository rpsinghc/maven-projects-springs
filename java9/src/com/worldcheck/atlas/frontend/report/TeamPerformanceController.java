package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.report.TeamPerformanceReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.TeamPerformance;
import com.worldcheck.atlas.vo.report.TeamPerformanceDetail;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TeamPerformanceController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.TeamPerformanceController");
	private static PropertyReaderUtil propertyReader;
	private AtlasReportService atlasReportService;
	private static final String JSP_NAME = "statsTeamPerformance_addTemp";
	private static final String TEMPLATE_NAME = "templateName";
	private static final String TEMPLATE_TYPE_HIDDEN = "hdnTemplateType";
	private static final String SELECTED_USER_NAME = "selectedUsersName";
	private static final String OFFICE = "office";
	private static final String STATS_TEAM_PERFORMANCE = "statsTeamPerformance";
	private static final String YEARS = "years";
	private static final String CURRENT_YEAR = "currentYear";
	private static final String CURRENT_MONTH = "currentMonth";
	private static final String MONTH_MAP = "monthMap";
	private static final String TEAM_PERFORMANCE_LIST = "teamPerformanceList";
	private static final String TEAM_PERFORMANCE_DETAIL_LIST = "teamPerformanceDetailList";
	private static final String SUPERVISOR = "Supervisor";
	private static final String USER_NAME = "User Name";
	private static final String MONTH_HEADER = "Month";
	private static final String TOTAL_POINTS_HEADER = "Total Points";
	private static final String JLP = "JLP";
	private static final String POINTS_HEADER_SUFFIX = " Points";
	private static final String TOTAL_REPORTS_HEADER = "Total Reports";
	private static final String REDIRECT_JSP = "redirect:setupViewTeamPerformance.do";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public ModelAndView setupAddTemplate(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  TeamPerformanceController::setupAddTemplate ");
		ModelAndView modelandview = null;

		try {
			modelandview = new ModelAndView("statsTeamPerformance_addTemp");
			this.logger.debug("exiting  TeamPerformanceController::setupAddTemplate ");
		} catch (Exception var5) {
			AtlasUtils.getExceptionView(this.logger, var5);
		}

		return modelandview;
	}

	public ModelAndView saveTemplate(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  TeamPerformanceController::saveTemplate ");
		ModelAndView modelandview = null;

		try {
			String templateName = "";
			String templateType = "";
			String templateOffice = "";
			String templateUserNames = "";
			String templateValue = "";
			modelandview = new ModelAndView("redirect:setupViewTeamPerformance.do");
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String userId = userDetailsBean.getLoginUserId();
			if (request.getParameter("templateName") != null) {
				templateName = request.getParameter("templateName");
			}

			if (request.getParameter("hdnTemplateType") != null) {
				templateType = request.getParameter("hdnTemplateType");
			}

			if (request.getParameter("selectedUsersName") != null) {
				templateUserNames = request.getParameter("selectedUsersName");
			}

			if (request.getParameter("office") != null) {
				templateOffice = request.getParameter("office");
			}

			if (templateType.equalsIgnoreCase("office")) {
				templateValue = templateOffice;
			} else {
				templateValue = templateUserNames;
			}

			request.setAttribute("reportName", "TeamPerformanceReport");
			this.logger.debug("templateName " + templateName);
			this.logger.debug("templateType " + templateType);
			this.logger.debug("templateUserNames " + templateUserNames);
			this.logger.debug("templateOffice " + templateOffice);
			this.logger.debug("templateValue " + templateValue);
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			teamPerformanceRpt.insertTemplate(templateName, templateType, templateValue, userId);
			this.logger.debug("exiting  TeamPerformanceController::saveTemplate ");
		} catch (CMSException var12) {
			AtlasUtils.getExceptionView(this.logger, var12);
		} catch (Exception var13) {
			AtlasUtils.getExceptionView(this.logger, var13);
		}

		return modelandview;
	}

	public ModelAndView setupViewTeamPerformance(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("In  TeamPerformanceController::setupViewTeamPerformance ");
		ModelAndView modelandview = null;

		try {
			List<String> yearList = StringUtils.commaSeparatedStringToList(propertyReader.getYearList());
			List<String> modifiedYearList = new ArrayList();
			this.logger.debug("yearList size :: " + yearList.size());
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			modelandview = new ModelAndView("statsTeamPerformance");
			int indOf2009 = yearList.indexOf("2009");
			int indOfCurrentYear = yearList.indexOf(String.valueOf(currentYear));
			if (indOf2009 >= 0 && indOfCurrentYear >= 0) {
				modifiedYearList = yearList.subList(indOf2009, indOfCurrentYear + 1);
				modelandview.addObject("years", modifiedYearList);
			} else {
				modelandview.addObject("years", yearList);
			}

			int currentMonth = cal.get(2) + 1;
			this.logger.debug("modifiedYearList size :: " + ((List) modifiedYearList).size());
			modelandview.addObject("currentYear", currentYear);
			modelandview.addObject("currentMonth", currentMonth);
			modelandview.addObject("monthMap", ReportConstants.monthMap);
			this.logger.debug("Exiting  TeamPerformanceController ::setupViewTeamPerformance ");
		} catch (CMSException var11) {
			AtlasUtils.getExceptionView(this.logger, var11);
		} catch (Exception var12) {
			AtlasUtils.getExceptionView(this.logger, var12);
		}

		return modelandview;
	}

	public ModelAndView exportToExcel(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONTeamPerformanceController::exportToExcel");
		ModelAndView modelAndView = new ModelAndView("statsTeamPerformance");

		try {
			request.getSession().setAttribute("excelGeneration", new Date());
			String excelParams = request.getParameter("excelParams");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("parameter " + excelParamMap);
			request.setAttribute("reportName", "TeamPerformanceReport");
			TeamPerformanceReport teamPerformanceRpt = (TeamPerformanceReport) this.atlasReportService
					.getReportObject(request);
			List<ReportTypeMasterVO> reportTypeList = teamPerformanceRpt.getAllReportTypes();
			HashMap<String, Object> resultMap = teamPerformanceRpt.fetchDataForExportToExcel(excelParamMap,
					reportTypeList);
			List<LinkedHashMap> teamPerformanceList = (List) resultMap.get("teamPerformanceList");
			List<TeamPerformance> teamPerformanceDetailList = (List) resultMap.get("teamPerformanceDetailList");
			Map<String, Object> resultsMap = this.writeToExcel(teamPerformanceList, teamPerformanceDetailList, response,
					reportTypeList);
			modelAndView = new ModelAndView("excelDownloadPopup");
			modelAndView.addObject("fileBytes", resultsMap.get("fileBytes"));
			modelAndView.addObject("fileName", resultsMap.get("fileName"));
			List<String> yearList = StringUtils.commaSeparatedStringToList(propertyReader.getYearList());
			List<String> modifiedYearList = new ArrayList();
			this.logger.debug("yearList size :: " + yearList.size());
			Iterator iterator = yearList.iterator();

			while (iterator.hasNext()) {
				String yearStr = (String) iterator.next();
				if (Integer.parseInt(yearStr) >= 2009) {
					modifiedYearList.add(yearStr);
				}
			}

			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			int currentMonth = cal.get(2) + 1;
			modelAndView.addObject("years", modifiedYearList);
			modelAndView.addObject("currentYear", currentYear);
			modelAndView.addObject("currentMonth", currentMonth);
			modelAndView.addObject("monthMap", ReportConstants.monthMap);
		} catch (CMSException var21) {
			AtlasUtils.getExceptionView(this.logger, var21);
		} catch (Exception var22) {
			AtlasUtils.getExceptionView(this.logger, var22);
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		this.logger.debug("Exiting JSONTeamPerformanceController::exportToExcel");
		return modelAndView;
	}

	private Map<String, Object> writeToExcel(List<LinkedHashMap> teamPerformanceList,
			List<TeamPerformance> teamPerformanceDetailList, HttpServletResponse response,
			List<ReportTypeMasterVO> reportTypeList) throws CMSException {
		this.logger.debug("in JSONTeamPerformanceController::writeToExcel");

		try {
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			new ArrayList();
			new ArrayList();
			List<String> lstDynamicHeader = this.createDynamicHeader(teamPerformanceDetailList);
			List<String> lstHeader = this.createCompleteHeader(lstDynamicHeader, reportTypeList);
			LinkedHashMap<String, LinkedHashMap> userTeamJlpRptMap = new LinkedHashMap();
			Iterator iterator = teamPerformanceDetailList.iterator();

			LinkedHashMap teamPerformanceMap;
			String key;
			while (iterator.hasNext()) {
				TeamPerformanceDetail teamPerformanceObj = (TeamPerformanceDetail) iterator.next();
				if (!userTeamJlpRptMap.containsKey(teamPerformanceObj.getUserLoginId())) {
					teamPerformanceMap = new LinkedHashMap();
					Iterator iterator2 = lstDynamicHeader.iterator();

					while (iterator2.hasNext()) {
						String teamName = (String) iterator2.next();
						teamPerformanceMap.put(teamName, "0.0###0");
					}

					userTeamJlpRptMap.put(teamPerformanceObj.getUserLoginId(), teamPerformanceMap);
				}

				new LinkedHashMap();
				teamPerformanceMap = (LinkedHashMap) userTeamJlpRptMap.get(teamPerformanceObj.getUserLoginId());
				float jlpPointsTillnow = Float.parseFloat("0.0");
				int rptCountTillnow = false;
				key = "";
				key = (String) teamPerformanceMap.get(teamPerformanceObj.getTeam());
				jlpPointsTillnow = Float.parseFloat(key.substring(0, key.indexOf("###")));
				int rptCountTillnow = Integer.parseInt(key.substring(key.indexOf("###") + 3, key.length()));
				jlpPointsTillnow += teamPerformanceObj.getTeamJLP();
				rptCountTillnow += teamPerformanceObj.getTeamReportCount();
				key = jlpPointsTillnow + "###" + rptCountTillnow;
				teamPerformanceMap.put(teamPerformanceObj.getTeam(), key);
				this.logger.debug(
						"user == " + teamPerformanceObj.getUserLoginId() + " selUserTeamMap " + teamPerformanceMap);
			}

			iterator = teamPerformanceList.iterator();

			while (iterator.hasNext()) {
				new LinkedHashMap();
				teamPerformanceMap = (LinkedHashMap) iterator.next();
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				datamap.put("Supervisor", String.valueOf(teamPerformanceMap.get("SUPERVISOR")));
				datamap.put("User Name", String.valueOf(teamPerformanceMap.get("ANALYST")));
				datamap.put("Month", String.valueOf(teamPerformanceMap.get("MONTH")));
				datamap.put("Total Points", String.valueOf(teamPerformanceMap.get("TOTAL_POINTS")));
				LinkedHashMap<String, String> userTeamMapObj = (LinkedHashMap) userTeamJlpRptMap
						.get(teamPerformanceMap.get("ANALYST"));
				String reportTypeName;
				Iterator iterator2;
				if (userTeamMapObj != null) {
					iterator2 = userTeamMapObj.keySet().iterator();

					while (iterator2.hasNext()) {
						key = (String) iterator2.next();
						reportTypeName = "";
						reportTypeName = (String) userTeamMapObj.get(key);
						float jlpPointsVal = Float
								.parseFloat(reportTypeName.substring(0, reportTypeName.indexOf("###")));
						datamap.put(key, String.valueOf(jlpPointsVal));
					}
				}

				datamap.put("Total Reports", String.valueOf(teamPerformanceMap.get("TOTAL_REPORTS")));
				if (userTeamMapObj != null) {
					iterator2 = userTeamMapObj.keySet().iterator();

					while (iterator2.hasNext()) {
						key = (String) iterator2.next();
						reportTypeName = "";
						reportTypeName = (String) userTeamMapObj.get(key);
						int jlpRptCountVal = Integer.parseInt(
								reportTypeName.substring(reportTypeName.indexOf("###") + 3, reportTypeName.length()));
						datamap.put(key + "Report", String.valueOf(jlpRptCountVal));
					}
				}

				iterator2 = reportTypeList.iterator();

				while (iterator2.hasNext()) {
					ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) iterator2.next();
					reportTypeName = reportTypeMasterVO.getReportType();
					String reportCRNInitital = reportTypeMasterVO.getInitialsUseCRN();
					datamap.put(reportTypeName, String.valueOf(teamPerformanceMap.get(reportCRNInitital)));
				}

				dataList.add(datamap);
			}

			this.logger.debug("EXITING JSONTeamPerformanceController::writeToExcel");
			return ExcelDownloader.writeToExcel(lstHeader, dataList, "Team Performance", (short) 0, (short) 1, response,
					"Team Performance");
		} catch (ClassCastException var17) {
			throw new CMSException(this.logger, var17);
		} catch (NullPointerException var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	private List<String> createDynamicHeader(List<TeamPerformance> teamPerformanceDetailList) {
		List<String> lstDynamicHeader = new ArrayList();
		lstDynamicHeader.add("PT");
		Iterator iterator = teamPerformanceDetailList.iterator();

		while (iterator.hasNext()) {
			TeamPerformanceDetail teamPerformanceDetail = (TeamPerformanceDetail) iterator.next();
			if (!lstDynamicHeader.contains(teamPerformanceDetail.getTeam())) {
				this.logger.debug("lst dynamic header :- " + teamPerformanceDetail.getTeam());
				lstDynamicHeader.add(teamPerformanceDetail.getTeam());
			}
		}

		Collections.sort(lstDynamicHeader, String.CASE_INSENSITIVE_ORDER);
		return lstDynamicHeader;
	}

	private List<String> createCompleteHeader(List<String> lstDynamicHeader, List<ReportTypeMasterVO> reportTypeList) {
		List<String> lstHeader = new ArrayList();
		lstHeader.add("Supervisor");
		lstHeader.add("User Name");
		lstHeader.add("Month");
		lstHeader.add("Total Points");
		Iterator iterator2 = lstDynamicHeader.iterator();

		String dynamicTeam;
		while (iterator2.hasNext()) {
			dynamicTeam = (String) iterator2.next();
			lstHeader.add(dynamicTeam + " Points");
		}

		lstHeader.add("Total Reports");
		iterator2 = lstDynamicHeader.iterator();

		while (iterator2.hasNext()) {
			dynamicTeam = (String) iterator2.next();
			lstHeader.add(dynamicTeam + " Reports");
		}

		iterator2 = reportTypeList.iterator();

		while (iterator2.hasNext()) {
			ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) iterator2.next();
			lstHeader.add(reportTypeMasterVO.getReportType());
		}

		return lstHeader;
	}
}