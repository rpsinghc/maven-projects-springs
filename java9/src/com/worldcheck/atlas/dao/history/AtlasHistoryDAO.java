package com.worldcheck.atlas.dao.history;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class AtlasHistoryDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.history.AtlasHistoryDAO");
	private static final String ALTAS_HISTORY_ADD = "AtlasHistory.addHistory";
	private static final String ALTAS_HISTORY_GET_OLD_INFO = "AtlasHistory.getOldInfo";
	private static final String ALTAS_HISTORY_DATA = "AtlasHistory.getHistoryData";
	private static final String ALTAS_HISTORY_DATA_COUNT = "AtlasHistory.getHistoryDataCount";
	private static final String ALTAS_USER_HISTORY_ADD = "AtlasHistory.addUserHistory";
	private static final String ALTAS_USER_HISTORY_DATA = "AtlasHistory.getUserHistoryData";
	private static final String ALTAS_USER_HISTORY_DATA_COUNT = "AtlasHistory.getUserHistoryDataCount";

	public long addHistory(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::addHistory");
		long id = 0L;

		try {
			id = (Long) this.insert("AtlasHistory.addHistory", atlasHistoryVO);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("generated id is ::::  " + id);
		return id;
	}

	public AtlasHistoryVO getOldInfo(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::getOldInfo");
		long id = 0L;
		this.logger.debug("generated id is ::::  " + id);
		return atlasHistoryVO;
	}

	public List<AtlasHistoryVO> getHistoryData(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::getHistoryData");
		new ArrayList();

		try {
			List<AtlasHistoryVO> historyList = this.queryForList("AtlasHistory.getHistoryData", atlasHistoryVO);
			this.logger.debug("Size of the data return" + historyList.size());
			return historyList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getHistoryDataCount(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::getHistoryDataCount");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("AtlasHistory.getHistoryDataCount", atlasHistoryVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long addUserHistory(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::addUserHistory");
		long id = 0L;

		try {
			id = (Long) this.insert("AtlasHistory.addUserHistory", atlasHistoryVO);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("generated id is ::::  " + id);
		return id;
	}

	public List<AtlasHistoryVO> getUserHistoryData(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::getUserHistoryData");
		new ArrayList();

		try {
			List<AtlasHistoryVO> historyList = this.queryForList("AtlasHistory.getUserHistoryData", atlasHistoryVO);
			this.logger.debug("Size of the data return" + historyList.size());
			return historyList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getUserHistoryDataCount(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN AtlasHistoryDAO::getUserHistoryDataCount");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("AtlasHistory.getUserHistoryDataCount", atlasHistoryVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}