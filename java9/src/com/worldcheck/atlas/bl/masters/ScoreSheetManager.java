package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.IScoreSheetMaster;
import com.worldcheck.atlas.dao.masters.ScoreSheetDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import java.util.List;

public class ScoreSheetManager implements IScoreSheetMaster {
	private ScoreSheetDAO scoreSheetDAO;

	public void setScoreSheetDAO(ScoreSheetDAO scoreSheetDAO) {
		this.scoreSheetDAO = scoreSheetDAO;
	}

	public List<ScoreSheetMasterVO> getScoreSheetGrid(ScoreSheetMasterVO scoreSheetMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		scoreSheetMasterVO.setStart(start + 1);
		scoreSheetMasterVO.setLimit(start + limit);
		scoreSheetMasterVO.setSortColumnName(sortColumnName);
		scoreSheetMasterVO.setSortType(sortType);
		List<ScoreSheetMasterVO> scoreSheetList = this.scoreSheetDAO.getScoreSheetGrid(scoreSheetMasterVO);
		return scoreSheetList;
	}

	public int getScoreSheetCount(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		return this.scoreSheetDAO.getScoreSheetCount(scoreSheetMasterVO);
	}

	public boolean isExistScoreSheetName(String scoreSheetName) {
		int scoreSheetNameCount = this.scoreSheetDAO.getCountExistScoreSheetName(scoreSheetName);
		return scoreSheetNameCount > 0;
	}

	public void addScoreSheet(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		this.scoreSheetDAO.addScoreSheet(scoreSheetMasterVO);
	}

	public ScoreSheetMasterVO getScoreSheetInfo(String scoresheetMasterId) throws CMSException {
		return this.scoreSheetDAO.getScoreSheetInfo(scoresheetMasterId);
	}

	public String updateScoreSheet(ScoreSheetMasterVO scoreSheetMasterVO) throws CMSException {
		return this.scoreSheetDAO.updateScoreSheet(scoreSheetMasterVO);
	}

	public String deleteScoreSheet(String scoreSheetId) throws CMSException {
		return this.scoreSheetDAO.deleteScoreSheet(scoreSheetId);
	}
}