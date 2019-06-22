package com.worldcheck.atlas.document;

import com.worldcheck.atlas.dao.document.DocMapDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.document.DocMapVO;
import java.util.HashMap;
import java.util.Map;

public class DocSender {
	private DocMapDAO docMapDAO = null;

	public void setDocListlDAO(DocMapVO docListlDAO) {
	}

	public Map<String, String> getDocuments(String pid, int start, int limit) throws CMSException {
		Map<String, Object> map = new HashMap();
		map.put("pid", pid);
		map.put("start", start);
		map.put("limit", limit);
		return this.docMapDAO.getMapWithPID(map);
	}

	public Map<String, String> getAllData(String pid) throws CMSException {
		Map<String, Object> map = new HashMap();
		map.put("pid", pid);
		return this.docMapDAO.getAllData(map);
	}

	public int getDocumentCount(String pid) throws CMSException {
		return this.docMapDAO.getMapWithPIDCount(pid);
	}

	public void setDocMapDAO(DocMapDAO docMapDAO) {
		this.docMapDAO = docMapDAO;
	}
}