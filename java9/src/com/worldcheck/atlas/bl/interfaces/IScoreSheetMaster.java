package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.ScoreSheetDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import java.util.List;

public interface IScoreSheetMaster {
	void setScoreSheetDAO(ScoreSheetDAO var1);

	List<ScoreSheetMasterVO> getScoreSheetGrid(ScoreSheetMasterVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int getScoreSheetCount(ScoreSheetMasterVO var1) throws CMSException;

	boolean isExistScoreSheetName(String var1);

	void addScoreSheet(ScoreSheetMasterVO var1) throws CMSException;

	ScoreSheetMasterVO getScoreSheetInfo(String var1) throws CMSException;

	String updateScoreSheet(ScoreSheetMasterVO var1) throws CMSException;

	String deleteScoreSheet(String var1) throws CMSException;
}