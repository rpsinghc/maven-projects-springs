package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.TeamJLPSummaryVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TeamJLPSummaryDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.report.TeamJLPSummaryDAO");
	private static final String getAllActiveUsers = "TeamJLP.getAllActiveUsers";
	private static final String getAllTemplateCreator = "TeamJLP.getAllTemplateCreator";
	private static final String isExistTemplateName = "TeamJLP.isExistTemplateName";
	private static final String addTeamJLPTemplate = "TeamJLP.addTeamJLPTemplate";
	private static final String searchTeamJLPTemplate = "TeamJLP.searchTeamJLPTemplate";
	private static final String updateTeamJLPTemplate = "TeamJLP.updateTeamJLPTemplate";
	private static final String deleteTeamJLPTemplate = "TeamJLP.deleteTeamJLPTemplate";
	private static final String getSelectedUsers = "TeamJLP.getSelectedUsers";
	private static final String addToDefaultTeamJLPTemplate = "TeamJLP.addToDefaultTeamJLPTemplate";
	private static final String removeFromDefaultTeamJLPTemplate = "TeamJLP.removeFromDefaultTeamJLPTemplate";
	private static final String searchTeamJLPTemplateCount = "TeamJLP.searchTeamJLPTemplateCount";

	public Object addTeamJLPTemplate(TeamJLPSummaryVO teamJLPsummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : addTeamJLPTemplate");

		try {
			Object inserted = this.insert("TeamJLP.addTeamJLPTemplate", teamJLPsummaryVO);
			return inserted;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateTeamJLPTemplate(TeamJLPSummaryVO teamJLPsummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : updateTeamJLPTemplate");
		boolean var2 = false;

		try {
			int updated = this.update("TeamJLP.updateTeamJLPTemplate", teamJLPsummaryVO);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteTeamJLPTemplate(TeamJLPSummaryVO teamJLPsummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : deleteTeamJLPTemplate");
		boolean var2 = false;

		try {
			int deleted = this.delete("TeamJLP.deleteTeamJLPTemplate", teamJLPsummaryVO);
			return deleted;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<TeamJLPSummaryVO> searchTeamJLPTemplate(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : searchTeamJLPTemplate");
		new ArrayList();

		try {
			List<TeamJLPSummaryVO> teamJLPSummaryVOList = this.queryForList("TeamJLP.searchTeamJLPTemplate",
					teamJLPSummaryVO);
			return teamJLPSummaryVOList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchTeamJLPTemplateCount(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : searchTeamJLPTemplateCount");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("TeamJLP.searchTeamJLPTemplateCount", teamJLPSummaryVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int isExistTemplateName(String templateName) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : isExistTemplateName");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("TeamJLP.isExistTemplateName", templateName);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getAllActiveUsers(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : getAllActiveUsers");
		new ArrayList();

		try {
			List<UserMasterVO> userMasterList = this.queryForList("TeamJLP.getAllActiveUsers", teamJLPSummaryVO);
			return userMasterList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getSelectedUsers(TeamJLPSummaryVO teamJLPVO) throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : getSelectedUsers");
		String result = null;

		try {
			result = (String) this.queryForObject("TeamJLP.getSelectedUsers", teamJLPVO);
			return result;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<UserMasterVO> getAllTemplateCreator() throws CMSException {
		this.logger.debug("In TeamJLPSummaryDAO : getAllTemplateCreator");
		new ArrayList();

		try {
			List<UserMasterVO> templateCreatorList = this.queryForList("TeamJLP.getAllTemplateCreator");
			return templateCreatorList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void addToDefaultTeamJLPTemplate(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		try {
			this.update("TeamJLP.addToDefaultTeamJLPTemplate", teamJLPSummaryVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void removeFromDefaultTeamJLPTemplate(TeamJLPSummaryVO teamJLPSummaryVO) throws CMSException {
		try {
			this.update("TeamJLP.removeFromDefaultTeamJLPTemplate", teamJLPSummaryVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}