package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.dao.history.AtlasHistoryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import java.util.List;

public class AtlasHistoryUtil {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.utils.AtlasHistoryUtil");
	AtlasHistoryVO atlasHistoryVO;
	AtlasHistoryDAO atlasHistoryDAO;
	private static final String REPORT_MASTER = "REPORT_TYPE_MASTER_HISTORY";

	public void setAtlasHistoryDAO(AtlasHistoryDAO atlasHistoryDAO) {
		this.atlasHistoryDAO = atlasHistoryDAO;
	}

	public String insertData(String moduleName, AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("Inside AtlasHistoryUtil:insertData");
		long id = 0L;
		if (moduleName.equalsIgnoreCase("REPORT_TYPE_MASTER")) {
			atlasHistoryVO.setTableName("CMS_REPORT_TYPE_MASTER_HISTORY");
		}

		try {
			id = this.atlasHistoryDAO.addHistory(atlasHistoryVO);
			atlasHistoryVO.setHistoryId(id);
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		}

		return "" + id;
	}

	public List<AtlasHistoryVO> getHistoryData(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN ATLASHISTORTYUTIL::getHistoryData");
		List<AtlasHistoryVO> historyDataList = this.atlasHistoryDAO.getHistoryData(atlasHistoryVO);
		this.logger.debug("Existing ATLASHISTORTYUTIL::getHistoryData");
		return historyDataList;
	}

	public int getHistoryDataCount(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN ATLASHISTORTYUTIL::getHistoryData");
		return this.atlasHistoryDAO.getHistoryDataCount(atlasHistoryVO);
	}

	public String userHistoryInsertData(String moduleName, AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("Inside AtlasHistoryUtil for userMaster:insertData");
		long id = 0L;
		if (moduleName.equalsIgnoreCase("USER_MASTER_HISTORY")) {
			atlasHistoryVO.setTableName("CMS_USER_MASTER_HISTORY");
		}

		try {
			id = this.atlasHistoryDAO.addUserHistory(atlasHistoryVO);
			atlasHistoryVO.setHistoryId(id);
		} catch (CMSException var6) {
			throw new CMSException(this.logger, var6);
		}

		return "" + id;
	}

	public List<AtlasHistoryVO> getUserHistoryData(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN ATLASHISTORTYUTIL::getUserHistoryData");
		List<AtlasHistoryVO> historyDataList = this.atlasHistoryDAO.getUserHistoryData(atlasHistoryVO);
		this.logger.debug("Exiting ATLASHISTORTYUTIL::getUserHistoryData");
		return historyDataList;
	}

	public int getUserHistoryDataCount(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN ATLASHISTORTYUTIL::getUserHistoryDataCount");
		return this.atlasHistoryDAO.getUserHistoryDataCount(atlasHistoryVO);
	}
}