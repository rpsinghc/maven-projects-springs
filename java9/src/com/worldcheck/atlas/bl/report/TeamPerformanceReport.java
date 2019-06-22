package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.TeamPerformanceDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.TeamPerformance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TeamPerformanceReport implements IAtlasReport {
	private TeamPerformanceDAO teamPerformanceDAO;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.TeamPerformanceReport");
	private static final String USER_ID = "userId";
	private static final String TEMPLATE_NAME = "templateName";
	private static final String TEMPLATE_ID = "templateId";
	private static final String TEMPLATE_TYPE = "templateType";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String TEMPLATE_TYPE_USER = "User";
	private static final String TEAM_PERFORMANCE_LIST = "teamPerformanceList";
	private static final String TEAM_PERFORMANCE_DETAIL = "teamPerformanceDetailList";

	public void setTeamPerformanceDAO(TeamPerformanceDAO teamPerformanceDAO) {
		this.teamPerformanceDAO = teamPerformanceDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return null;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}

	public List<TeamPerformance> fetchTeamPerformanceUsers(String userId) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::fetchTeamPerformanceUsers");
		new ArrayList();
		List<TeamPerformance> userList = this.teamPerformanceDAO.fetchTeamPerformanceUsers(userId);
		this.logger.debug("Exiting TeamPerformanceReport::fetchTeamPerformanceUsers");
		return userList;
	}

	public void insertTemplate(String templateName, String templateType, String templateValue, String userId)
			throws CMSException {
		this.logger.debug("In TeamPerformanceReport::insertTemplate");

		try {
			TeamPerformance teamPerformance = new TeamPerformance();
			teamPerformance.setTemplateName(templateName);
			teamPerformance.setTemplateType(templateType);
			teamPerformance.setTemplateValue(templateValue);
			teamPerformance.setUserLoginId(userId);
			this.teamPerformanceDAO.saveTemplate(teamPerformance);
			this.logger.debug("In TeamPerformanceReport::insertTemplate");
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<TeamPerformance> getUserTemplates(String userId) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::getUserTemplates");
		new ArrayList();

		try {
			List<TeamPerformance> teamPerformanceList = this.teamPerformanceDAO.getTemplatesOfUser(userId);
			this.logger.debug("In TeamPerformanceReport::getUserTemplates");
			return teamPerformanceList;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int checkUserTemplateExists(String userId, String templateName) throws CMSException {
		int count = false;
		this.logger.debug("In TeamPerformanceReport::checkUserTemplateExists");

		int count;
		try {
			HashMap<String, String> hashMap = new HashMap();
			hashMap.put("userId", userId);
			hashMap.put("templateName", templateName);
			count = this.teamPerformanceDAO.checkUserTemplateExists(hashMap);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("In TeamPerformanceReport::checkUserTemplateExists");
		return count;
	}

	public int deleteUserTemplates(String templateId) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::deleteUserTemplates");
		int deleteResultCount = this.teamPerformanceDAO.deleteUserTemplate(templateId);
		this.logger.debug("In TeamPerformanceReport::deleteUserTemplates");
		return deleteResultCount;
	}

	public List<LinkedHashMap> fetchReport(String templateId, String templateType, String year, String month,
			List<ReportTypeMasterVO> reportTypeList, int start, int limit) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::fetchReport");
		new ArrayList();

		List teamPerformanceList;
		try {
			HashMap<String, Object> hashMap = new HashMap();
			hashMap.put("templateId", templateId);
			hashMap.put("templateType", templateType);
			hashMap.put("year", year);
			hashMap.put("month", month);
			hashMap.put("start", new Integer(start + 1));
			hashMap.put("limit", new Integer(start + limit));
			ArrayList queryStringList;
			Iterator iterator;
			ReportTypeMasterVO reportTypeMasterVO;
			String queryStr;
			if (templateType.equalsIgnoreCase("User")) {
				queryStringList = new ArrayList();
				iterator = reportTypeList.iterator();

				while (iterator.hasNext()) {
					reportTypeMasterVO = (ReportTypeMasterVO) iterator.next();
					queryStr = "";
					queryStr = "sum(case when (REPORT_TYPE = '" + reportTypeMasterVO.getReportType() + "') then "
							+ "(TOTAL_REPORTS) else 0 end) as " + reportTypeMasterVO.getInitialsUseCRN() + "";
					queryStringList.add(queryStr);
				}

				hashMap.put("queryStringList", queryStringList);
				teamPerformanceList = this.teamPerformanceDAO.getTeamPerformanceReportForUserTemplate(hashMap);
			} else {
				queryStringList = new ArrayList();
				iterator = reportTypeList.iterator();

				while (iterator.hasNext()) {
					reportTypeMasterVO = (ReportTypeMasterVO) iterator.next();
					queryStr = "";
					queryStr = "sum(case when (REPORT_TYPE = '" + reportTypeMasterVO.getReportType() + "') then "
							+ "(TOTAL_REPORTS) else 0 end) as " + reportTypeMasterVO.getInitialsUseCRN() + "";
					queryStringList.add(queryStr);
				}

				this.logger.debug("queryStringList " + queryStringList);
				hashMap.put("queryStringList", queryStringList);
				teamPerformanceList = this.teamPerformanceDAO.getTeamPerformanceReportForOfficeTemplate(hashMap);
			}
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		}

		this.logger.debug("In TeamPerformanceReport::fetchReport");
		return teamPerformanceList;
	}

	public HashMap<String, Object> fetchDataForExportToExcel(Map<String, Object> excelParamMap,
			List<ReportTypeMasterVO> reportTypeList) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::fetchDataForExportToExcel");
		String templateType = "";
		List<String> queryStringList = new ArrayList();
		Iterator iterator = reportTypeList.iterator();

		while (iterator.hasNext()) {
			ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) iterator.next();
			String queryStr = "";
			queryStr = "sum(case when (REPORT_TYPE = '" + reportTypeMasterVO.getReportType() + "') then "
					+ "(TOTAL_REPORTS) else 0 end) as " + reportTypeMasterVO.getInitialsUseCRN() + "";
			queryStringList.add(queryStr);
		}

		excelParamMap.put("queryStringList", queryStringList);
		new HashMap();

		HashMap resultMap;
		try {
			templateType = (String) excelParamMap.get("templateType");
			List<String> teamPerformanceUserNameList = new ArrayList();
			new ArrayList();
			List teamPerformanceMapList;
			if (templateType.equalsIgnoreCase("User")) {
				teamPerformanceMapList = this.teamPerformanceDAO
						.getReportForUserTemplateForExcelDownload(excelParamMap);
			} else {
				teamPerformanceMapList = this.teamPerformanceDAO
						.getReportForOfficeTemplateForExcelDownload(excelParamMap);
			}

			this.logger.debug("teamPerformanceMapList " + teamPerformanceMapList.size());
			Iterator iterator = teamPerformanceMapList.iterator();

			while (true) {
				if (!iterator.hasNext()) {
					List<TeamPerformance> teamPerformanceDetailList = this.teamPerformanceDAO
							.getTeamPerformanceDetail(teamPerformanceUserNameList, excelParamMap);
					resultMap = new HashMap();
					resultMap.put("teamPerformanceList", teamPerformanceMapList);
					resultMap.put("teamPerformanceDetailList", teamPerformanceDetailList);
					break;
				}

				LinkedHashMap teamPerformanceMap = (LinkedHashMap) iterator.next();
				String analystLoginID = (String) teamPerformanceMap.get("ANALYST");
				teamPerformanceUserNameList.add(analystLoginID.toString());
			}
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}

		this.logger.debug("In TeamPerformanceReport::fetchDataForExportToExcel");
		return resultMap;
	}

	public List<ReportTypeMasterVO> getAllReportTypes() throws CMSException {
		this.logger.debug("In TeamPerformanceReport::getAllReportTypes");
		new ArrayList();

		List reportCrnInitialList;
		try {
			reportCrnInitialList = this.teamPerformanceDAO.getAllReportTypes();
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("Exiting TeamPerformanceReport::getAllReportTypes");
		return reportCrnInitialList;
	}

	public Integer getUserTemplateResultCount(String templateId, String year, String month) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::getUserTemplateResultCount");
		Integer count = 0;
		HashMap map = new HashMap();

		try {
			map.put("templateId", templateId);
			map.put("year", year);
			map.put("month", month);
			count = this.teamPerformanceDAO.getUserTemplateResultCount(map);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting TeamPerformanceReport::getUserTemplateResultCount");
		return count;
	}

	public Integer getOfficeTemplateResultCount(String templateId, String year, String month) throws CMSException {
		this.logger.debug("In TeamPerformanceReport::getOfficeTemplateResultCount");
		Integer count = 0;
		HashMap map = new HashMap();

		try {
			map.put("templateId", templateId);
			map.put("year", year);
			map.put("month", month);
			count = this.teamPerformanceDAO.getOfficeTemplateResultCount(map);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Exiting TeamPerformanceReport::getOfficeTemplateResultCount");
		return count;
	}
}