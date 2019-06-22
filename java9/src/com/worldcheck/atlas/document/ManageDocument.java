package com.worldcheck.atlas.document;

import com.savvion.sbm.dms.svo.Document;
import com.worldcheck.atlas.document.util.DocUtilCommon;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageDocument {
	private final ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.document.ManageDocument");

	public Map<String, Object> getDocumentById(String docId) {
		Map<String, Object> map = new HashMap();
		Document doc = DocUtilCommon.getDocService().getDocumentById(DocUtilCommon.getDSContext(), docId);
		this.logger.debug(doc.getContentBytes(DocUtilCommon.getDSContext()).length + "");
		map.put("bytes", doc.getContentBytes(DocUtilCommon.getDSContext()));
		map.put("docName", doc.getName());
		map.put("creator", doc.getCreator());
		map.put("path", doc.getPath());
		map.put("physicalLocation", doc.getPhysicalLocation());
		return map;
	}

	public List<String> getDocumentFolderNames(String mode) {
		List<String> listOfFolders = new ArrayList();
		if ("Full".equalsIgnoreCase(mode)) {
			listOfFolders.add("BI Final");
			listOfFolders.add("Common");
			listOfFolders.add("Draft");
			listOfFolders.add("Filings");
			listOfFolders.add("Vendor");
			listOfFolders.add("Source Comment");
			listOfFolders.add("Interim1 Report");
			listOfFolders.add("Interim2 Report");
			listOfFolders.add("ST Final");
			listOfFolders.add("Interim BI");
			listOfFolders.add("Sensitive");
			listOfFolders.add("Final Report");
		} else {
			listOfFolders.add("BI Final");
			listOfFolders.add("Common");
			listOfFolders.add("Draft");
			listOfFolders.add("Filings");
			listOfFolders.add("Vendor");
			listOfFolders.add("Source Comment");
			listOfFolders.add("Interim1 Report");
			listOfFolders.add("Interim2 Report");
			listOfFolders.add("ST Final");
			listOfFolders.add("Interim BI");
			listOfFolders.add("Sensitive");
		}

		return listOfFolders;
	}
}