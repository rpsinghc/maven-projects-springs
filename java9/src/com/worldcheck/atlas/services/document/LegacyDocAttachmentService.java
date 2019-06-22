package com.worldcheck.atlas.services.document;

import com.worldcheck.atlas.dao.document.LegacyDocAttachmentDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.document.LegacyDocAttachmentVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LegacyDocAttachmentService {
	LegacyDocAttachmentDAO legacyDocAttachmentDAO;
	public static PropertyReaderUtil propertyReader;
	private static final String byteParams = "bytes";
	private static final String docNameParam = "docName";
	private static final String showDocJSP = "showDocument";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.document.LegacyDocAttachmentService");

	public void setLegacyDocAttachmentDAO(LegacyDocAttachmentDAO legacyDocAttachmentDAO) {
		this.legacyDocAttachmentDAO = legacyDocAttachmentDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public List<LegacyDocAttachmentVO> getAttachmentDetails(LegacyDocAttachmentVO legacyDocAttachmentVO, int start,
			int limit, String sortColumnName, String sortType) throws CMSException {
		new ArrayList();
		legacyDocAttachmentVO.setStart(new Integer(start + 1));
		legacyDocAttachmentVO.setLimit(new Integer(start + limit));
		legacyDocAttachmentVO.setSortColumnName(sortColumnName);
		legacyDocAttachmentVO.setSortType(sortType);
		legacyDocAttachmentVO.setSortColumnName(sortColumnName);
		List<LegacyDocAttachmentVO> legacyDocAttachmentList = this.legacyDocAttachmentDAO
				.getAttachmentDetails(legacyDocAttachmentVO);
		return legacyDocAttachmentList;
	}

	public int getAttachmentDetailsCount(LegacyDocAttachmentVO legacyDocAttachmentVO) throws CMSException {
		return this.legacyDocAttachmentDAO.getAttachmentDetailsCount(legacyDocAttachmentVO);
	}

	public byte[] showLegacyDocument(String path, String docName) throws IOException {
		byte[] data = null;
		File newFile = new File(
				propertyReader.getLegacyDocAttachmentUrl() + File.separator + path + File.separator + docName);
		this.logger.debug("Downloading attachment path is :" + newFile.getAbsolutePath());
		InputStream is = new FileInputStream(newFile);
		long length = newFile.length();
		if (length > 2147483647L) {
			;
		}

		byte[] data = new byte[(int) length];
		int offset = 0;

		int numRead;
		for (boolean var9 = false; offset < data.length
				&& (numRead = is.read(data, offset, data.length - offset)) >= 0; offset += numRead) {
			;
		}

		if (offset < data.length) {
			throw new IOException("Could not completely read file " + newFile.getName());
		} else {
			is.close();
			return data;
		}
	}
}