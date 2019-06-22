package com.worldcheck.atlas.bl.backup;

import com.savvion.sbm.util.PService;
import com.worldcheck.atlas.dao.backup.UserBackUpDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.UserBackUpVO;
import java.util.List;

public class UserBackUpManager {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.backup.BackupManager");
	private UserBackUpDAO userBackUpDAO;

	public void setUserBackUpDAO(UserBackUpDAO userBackUpDAO) {
		this.userBackUpDAO = userBackUpDAO;
	}

	public List<UserBackUpVO> getUserBackUpList(String userId, String sortColumnName, String sortType, int start,
			int limit) throws CMSException {
		++start;
		limit += start;
		return this.userBackUpDAO.getUserBackUpList(userId, sortColumnName, sortType, start, limit);
	}

	public int getUserBackUpCount(String userId) throws CMSException {
		return this.userBackUpDAO.getUserBackUpCount(userId);
	}

	public String switchUserSession(String loginId) throws CMSException {
		return PService.self().decrypt(ResourceLocator.self().getSBMService().getPassword(loginId));
	}
}