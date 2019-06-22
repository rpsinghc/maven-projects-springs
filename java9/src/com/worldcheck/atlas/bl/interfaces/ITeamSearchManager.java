package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.TeamSearchVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.util.List;

public interface ITeamSearchManager {
	List<UserMasterVO> getAllAnalyst() throws CMSException;

	List<UserMasterVO> getAllReviewer() throws CMSException;

	List<UserMasterVO> getAllBiVendorMgr() throws CMSException;

	List<TeamSearchVO> getTeamSearchResult(TeamSearchVO var1) throws CMSException;

	int resultCount(TeamSearchVO var1) throws CMSException;

	List<MyTaskPageVO> getTeamSearchProcessData(String var1, String var2) throws CMSException;

	List<UserMasterVO> vendorTeamSearchInfo() throws CMSException;
}