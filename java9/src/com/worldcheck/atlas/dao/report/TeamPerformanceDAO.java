package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.TeamPerformance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TeamPerformanceDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.report.TeamPerformanceDAO");
	private static final String GET_TEAM_PERFORMANCE_USERS_SQL = "TeamPerformance.getUsersForTeamPerformance";
	private static final String INSERT_TEAM_PERFORMANCE_TEMPLATE_SQL = "TeamPerformance.saveTemplate";
	private static final String GET_USER_TEMPLATE_SQL = "TeamPerformance.getTemplateForUsers";
	private static final String DELETE_USER_TEMPLATE_SQL = "TeamPerformance.deleteTemplate";
	private static final String GET_TEAM_PERFORMANCE_REPORT_USER_SQL = "TeamPerformance.getTeamPerformanceResultForUserTemplate";
	private static final String GET_TEAM_PERFORMANCE_REPORT_OFFICE_SQL = "TeamPerformance.getTeamPerformanceResultForOfficeTemplate";
	private static final String GET_TEAM_PERFORMANCE_DETAIL_SQL = "TeamPerformance.getTeamPerformanceDetailResult";
	private static final String CHECK_TEMPLATE_EXISTS_SQL = "TeamPerformance.checkUserTemplateExists";
	private static final String TEAM_PERORMANCE_USER_NAME_LIST = "teamPerformanceUserNameList";
	private static final String GET_ALL_REPORT_SQL = "TeamPerformance.getAllReportTypes";
	private static final String GET_RESULT_COUNT_USER_TEMPLATE_SQL = "TeamPerformance.getResultCountForUserTemplate";
	private static final String GET_RESULT_COUNT_Office_TEMPLATE_SQL = "TeamPerformance.getResultCountForOfficeTemplate";
	private static final String GET_USER_REPORT_USER_Excel_SQL = "TeamPerformance.getReportForUserTemplateForExcelDownload";
	private static final String GET_OFFICE_REPORT_OFFICE_Excel_SQL = "TeamPerformance.getReportForOfficeTemplateForExcelDownload";
	private static final String YEAR = "year";
	private static final String MONTH = "month";

	public List<TeamPerformance> fetchTeamPerformanceUsers(String userId) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO : fetchTeamPerformanceUsers");
		new ArrayList();

		List resultList;
		try {
			resultList = this.queryForList("TeamPerformance.getUsersForTeamPerformance", userId);
			this.logger.debug("Team performance users list size:- " + resultList.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO : fetchTeamPerformanceUsers");
		return resultList;
	}

	public int saveTemplate(TeamPerformance teamPerformance) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::saveTemplate");

		int templateId;
		try {
			templateId = (Integer) this.insert("TeamPerformance.saveTemplate", teamPerformance);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::saveTemplate");
		return templateId;
	}

	public List<TeamPerformance> getTemplatesOfUser(String userId) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getTemplatesOfUser");
		new ArrayList();

		List teamPerformanceList;
		try {
			teamPerformanceList = this.queryForList("TeamPerformance.getTemplateForUsers", userId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getTemplatesOfUser");
		return teamPerformanceList;
	}

	public int checkUserTemplateExists(HashMap<String, String> hashMap) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::checkUserTemplateExists");
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("TeamPerformance.checkUserTemplateExists", hashMap);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::checkUserTemplateExists");
		return count;
	}

	public int deleteUserTemplate(String templateId) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::deleteUserTemplate");
		boolean var2 = false;

		int deleteResultCount;
		try {
			deleteResultCount = this.delete("TeamPerformance.deleteTemplate", templateId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::deleteUserTemplate");
		return deleteResultCount;
	}

	public List<LinkedHashMap> getTeamPerformanceReportForUserTemplate(Map<String, Object> hashMap)
			throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getTeamPerformanceReportForUserTemplate");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("TeamPerformance.getTeamPerformanceResultForUserTemplate", hashMap);
			this.logger.debug("resultSetObj " + resultSetObj.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getTeamPerformanceReportForUserTemplate");
		return resultSetObj;
	}

	public List<LinkedHashMap> getReportForUserTemplateForExcelDownload(Map<String, Object> hashMap)
			throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getReportForUserTemplateForExcelDownload");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("TeamPerformance.getReportForUserTemplateForExcelDownload", hashMap);
			this.logger.debug("resultSetObj " + resultSetObj.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getReportForUserTemplateForExcelDownload");
		return resultSetObj;
	}

	public List<LinkedHashMap> getTeamPerformanceReportForOfficeTemplate(Map<String, Object> hashMap)
			throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getTeamPerformanceReportForOfficeTemplate");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("TeamPerformance.getTeamPerformanceResultForOfficeTemplate", hashMap);
			this.logger.debug("resultSetObj " + resultSetObj.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getTeamPerformanceReportForOfficeTemplate");
		return resultSetObj;
	}

	public List<LinkedHashMap> getReportForOfficeTemplateForExcelDownload(Map<String, Object> hashMap)
			throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getReportForOfficeTemplateForExcelDownload");
		new ArrayList();

		List resultSetObj;
		try {
			resultSetObj = this.queryForList("TeamPerformance.getReportForOfficeTemplateForExcelDownload", hashMap);
			this.logger.debug("resultSetObj " + resultSetObj.size());
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getReportForOfficeTemplateForExcelDownload");
		return resultSetObj;
	}

	public List<TeamPerformance> getTeamPerformanceDetail(List<String> teamPerformanceUserNameList,
			Map<String, Object> excelParamMap) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getTeamPerformanceDetail");
		new ArrayList();

		List teamPerformanceDetailList;
		try {
			Map paramMap = new HashMap();
			paramMap.put("teamPerformanceUserNameList", teamPerformanceUserNameList);
			paramMap.put("year", excelParamMap.get("year"));
			paramMap.put("month", excelParamMap.get("month"));
			teamPerformanceDetailList = this.queryForList("TeamPerformance.getTeamPerformanceDetailResult", paramMap);
			this.logger.debug("teamPerformanceDetailList " + teamPerformanceDetailList.size());
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getTeamPerformanceDetail");
		return teamPerformanceDetailList;
	}

	public List<ReportTypeMasterVO> getAllReportTypes() throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getAllReportTypes");
		new ArrayList();

		List reportCrnInitialList;
		try {
			reportCrnInitialList = this.queryForList("TeamPerformance.getAllReportTypes");
			this.logger.debug("reportCrnInitialList " + reportCrnInitialList.size());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getAllReportTypes");
		return reportCrnInitialList;
	}

	public Integer getUserTemplateResultCount(Map<String, String> hashMap) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getUserTemplateResultCount");
		Integer count = 0;

		try {
			count = (Integer) this.queryForObject("TeamPerformance.getResultCountForUserTemplate", hashMap);
			this.logger.debug("getUserTemplateResultCount " + count);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getUserTemplateResultCount");
		return count;
	}

	public Integer getOfficeTemplateResultCount(Map<String, String> hashMap) throws CMSException {
		this.logger.debug("In TeamPerformanceDAO::getOfficeTemplateResultCount");
		Integer count = 0;

		try {
			count = (Integer) this.queryForObject("TeamPerformance.getResultCountForOfficeTemplate", hashMap);
			this.logger.debug("getOfficeTemplateResultCount " + count);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("Exiting TeamPerformanceDAO::getOfficeTemplateResultCount");
		return count;
	}
}