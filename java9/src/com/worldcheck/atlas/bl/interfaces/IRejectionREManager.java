package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.rejection.RejectionREDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface IRejectionREManager {
	void setRejectionREDAO(RejectionREDAO var1);

	List<SubTeamReMapVO> getAllSubjectInfoByTeam(String var1, String var2, String var3, String var4)
			throws CMSException;

	int saveRejectedREInfo(HttpServletRequest var1, Session var2, String var3, String var4, String var5,
			List<SubTeamReMapVO> var6, String var7, String var8, String var9, String var10) throws CMSException;

	void sendNotificationMessage(String var1, int var2, String var3, String var4) throws CMSException;
}