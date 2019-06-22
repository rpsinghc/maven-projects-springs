package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.bl.interfaces.ITeamJLPSummary;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.dao.report.TeamJLPSummaryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.TeamJLPSummaryVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TeamJLPSummaryManager implements IAtlasReport, ITeamJLPSummary {
	private static final Object TYPE = "misReportType";
	private static final String ONE = "1";
	private static final Object TWO = "2";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.TeamJLPSummaryManager");
	TeamJLPSummaryDAO teamJLPSummaryDAO;
	private TabularReportDAO tabularReportDAO = null;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public void setTeamJLPSummaryDAO(TeamJLPSummaryDAO teamJLPSummaryDAO) {
		this.teamJLPSummaryDAO = teamJLPSummaryDAO;
	}

	public void addTeamJLPTemplate(TeamJLPSummaryVO teamJLPsummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:addTeamJLPTemplate");
		this.teamJLPSummaryDAO.addTeamJLPTemplate(teamJLPsummaryVO);
	}

	public int updateTeamJLPTemplate(TeamJLPSummaryVO teamJLPsummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:updateTeamJLPTemplate");
		return this.teamJLPSummaryDAO.updateTeamJLPTemplate(teamJLPsummaryVO);
	}

	public int deleteTeamJLPTemplate(TeamJLPSummaryVO teamJLPsummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:deleteTeamJLPTemplate");
		int deleted = this.teamJLPSummaryDAO.deleteTeamJLPTemplate(teamJLPsummaryVO);
		return deleted;
	}

	public List<TeamJLPSummaryVO> searchTeamJLPTemplate(TeamJLPSummaryVO teamJLPSummaryVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:searchTeamJLPTemplate");
		teamJLPSummaryVO.setStart(new Integer(start + 1));
		teamJLPSummaryVO.setLimit(new Integer(start + limit));
		teamJLPSummaryVO.setSortColumnName(sortColumnName);
		teamJLPSummaryVO.setSortType(sortType);
		return this.teamJLPSummaryDAO.searchTeamJLPTemplate(teamJLPSummaryVO);
	}

	public int searchTeamJLPTemplateCount(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:searchTeamJLPTemplateCount");
		int count = this.teamJLPSummaryDAO.searchTeamJLPTemplateCount(teamJLPSummaryVO);
		this.logger.debug("fetched size:" + count);
		return count;
	}

	public boolean isExistTemplateName(String templateName) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:isExistTemplateName");

		try {
			int count = this.teamJLPSummaryDAO.isExistTemplateName(templateName);
			return count > 0;
		} catch (CMSException var3) {
			throw var3;
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> getAllActiveUsers(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager: getAllActiveUsers");
		return this.teamJLPSummaryDAO.getAllActiveUsers(teamJLPSummaryVO);
	}

	public List<UserMasterVO> getSelectedUsers(TeamJLPSummaryVO teamJLPVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager:  getSelectedUsers");
		ArrayList resultList = new ArrayList();

		try {
			String result = this.teamJLPSummaryDAO.getSelectedUsers(teamJLPVO);
			String[] resultArray = result.split(",");

			for (int i = 0; i < resultArray.length; ++i) {
				UserMasterVO umVO = new UserMasterVO();
				umVO.setUserFullName(resultArray[i]);
				resultList.add(umVO);
			}

			return resultList;
		} catch (CMSException var7) {
			throw var7;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<UserMasterVO> getAllTemplateCreator() throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager: getAllTemplateCreator");
		return this.teamJLPSummaryDAO.getAllTemplateCreator();
	}

	public void addToDefaultTeamJLPTemplate(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager: addToDefaultTeamJLPTemplate");
		this.teamJLPSummaryDAO.addToDefaultTeamJLPTemplate(teamJLPSummaryVO);
	}

	public void removeFromDefaultTeamJLPTemplate(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager: removeFromDefaultTeamJLPTemplate");
		this.teamJLPSummaryDAO.removeFromDefaultTeamJLPTemplate(teamJLPSummaryVO);
	}

	public List<TeamJLPSummaryVO> fetchJLPReport(HashMap<String, String> hmap) throws CMSException {
		this.logger.debug("In TeamJLPSummaryManager: fetchJLPReport");
		List<TeamJLPSummaryVO> resultList = new ArrayList();
		if (((String) hmap.get(TYPE)).equals("1")) {
			resultList = this.tabularReportDAO.fetchTeamLevelJLPReport(hmap);
		} else if (((String) hmap.get(TYPE)).equals(TWO)) {
			resultList = this.tabularReportDAO.fetchCaseLevelJLPReport(hmap);
		}

		return (List) resultList;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		List reportResult = null;
		return (List) reportResult;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}
}