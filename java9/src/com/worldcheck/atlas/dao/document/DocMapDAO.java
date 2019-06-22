package com.worldcheck.atlas.dao.document;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.document.DocMapVO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class DocMapDAO extends SqlMapClientTemplate {
	private final ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.document.DocMapDAO");
	private static final String GET_ALL_DOC_IDS = "DocConfig.getAllDocIds";
	private static final String GET_DOC_COUNT = "DocConfig.getDocCount";
	private static final String GET_ALL_DOCS = "DocConfig.getAllDocs";
	private static final String GET_DOCS = "DocConfig.getDocs";
	private static final String DELETE_DOCUMENTS = "DocConfig.deleteDocuments";
	private String UPDATE_CREATOR_NAME = "DocConfig.updateCreatorName";
	private String GET_DOC_BY_DOCID = "DocConfig.getDocByDocId";
	private String UPDATE_CREATOR_DATE = "DocConfig.updateCreatorDate";

	public Map<String, String> getMapWithPID(Map<String, Object> map) throws CMSException {
		SortedMap<String, String> mapVO = new TreeMap();
		List list = null;

		try {
			list = this.queryForList("DocConfig.getDocs", map);
			this.logger.debug(" list size is " + list.size());
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		if (list != null) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				DocMapVO docMapVO = (DocMapVO) iterator.next();
				mapVO.put(docMapVO.getDocId(), docMapVO.getNames());
			}
		}

		return mapVO;
	}

	public Map<String, String> getAllData(Map<String, Object> map) throws CMSException {
		SortedMap<String, String> mapVO = new TreeMap();
		List list = null;

		try {
			list = this.queryForList("DocConfig.getAllDocs", map);
			this.logger.debug(" list size is " + list.size());
			if (list.size() < 1) {
				return mapVO;
			}
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		if (list != null) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				DocMapVO docMapVO = (DocMapVO) iterator.next();
				mapVO.put(docMapVO.getDocId(), docMapVO.getNames());
			}
		}

		return mapVO;
	}

	public int getMapWithPIDCount(String pid) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("DocConfig.getDocCount", pid);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<String> getAllDocumentIds(String pid) throws CMSException {
		new ArrayList();

		List docIds;
		try {
			this.logger.debug("pid is " + pid);
			docIds = this.queryForList("DocConfig.getAllDocIds", pid);
			this.logger.debug("size is " + docIds.size());
			this.logger.debug("list is " + docIds);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		List<String> listOfIds = new ArrayList();
		Iterator iterator = docIds.iterator();

		while (iterator.hasNext()) {
			DocMapVO docMapVO = (DocMapVO) iterator.next();
			listOfIds.add(docMapVO.getDocId());
		}

		return listOfIds;
	}

	public int deleteDocumentFromSavvion(String docIds) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.delete("DocConfig.deleteDocuments", docIds);
			this.logger.debug("list is " + docIds + " value is i is " + i);
			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateDocCreatorName(String docIds, String creatorName) throws CMSException {
		Map<String, String> map = new HashMap();
		map.put("docIds", docIds);
		map.put("creatorName", creatorName);
		boolean var4 = false;

		int i;
		try {
			i = this.update(this.UPDATE_CREATOR_NAME, map);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug(i + "rows updated with creator name " + creatorName);
	}

	public DocMapVO getDocumentByDocId(String docId) throws CMSException {
		new DocMapVO();

		try {
			DocMapVO docVo = (DocMapVO) this.queryForObject(this.GET_DOC_BY_DOCID, docId);
			return docVo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateDocCreatorDate(String docIds, Timestamp fileCreationDate) throws CMSException {
		Map<String, Object> map = new HashMap();
		map.put("docIds", docIds);
		map.put("creatorDate", fileCreationDate);
		boolean var4 = false;

		int i;
		try {
			i = this.update(this.UPDATE_CREATOR_DATE, map);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug(i + "rows updated with creator Date " + fileCreationDate);
	}
}