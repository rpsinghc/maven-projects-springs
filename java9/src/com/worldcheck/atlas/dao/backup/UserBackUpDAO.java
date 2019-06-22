package com.worldcheck.atlas.dao.backup;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.UserBackUpVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class UserBackUpDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.rejection.RejectionREDAO");

	public List<UserBackUpVO> getUserBackUpList(String userId, String sortColumnName, String sortType, int start,
			int limit) throws CMSException {
		try {
			this.logger.debug("UserId : " + userId);
			new ArrayList();
			HashMap<String, String> map = new HashMap();
			this.logger.debug("userId:" + userId + "sortColumnName:" + sortColumnName + "sortType:" + sortType
					+ "start:" + start + "limit:" + limit);
			map.put("userId", userId);
			map.put("sortColumnName", sortColumnName);
			map.put("sortType", sortType);
			map.put("start", start + "");
			map.put("limit", limit + "");
			List<UserBackUpVO> backUpVOList = this.queryForList("UserBackUp.getBackUp", map);
			this.logger.debug("BackUpVoList Size:" + backUpVOList.size());
			return backUpVOList;
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public int getUserBackUpCount(String userid) throws CMSException {
		this.logger.debug("insideUserBackUpDAO::getUserBackUpCount:" + userid);

		try {
			return (Integer) this.queryForObject("UserBackUp.totalBackUpList", userid);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}