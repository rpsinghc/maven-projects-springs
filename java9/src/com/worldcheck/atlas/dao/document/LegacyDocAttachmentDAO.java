package com.worldcheck.atlas.dao.document;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.document.LegacyDocAttachmentVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class LegacyDocAttachmentDAO extends SqlMapClientTemplate {
	private String GET_ATTACHMENT_DETAILS = "LegacyDocAttachment.getAttachmentDetails";
	private String GET_ATTACHMENT_DETAILS_COUNT = "LegacyDocAttachment.getAttachmentDetailsCount";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.LegacyDocAttachmentDAO");

	public List<LegacyDocAttachmentVO> getAttachmentDetails(LegacyDocAttachmentVO legacyDocAttachmentVO)
			throws CMSException {
		new ArrayList();

		try {
			List<LegacyDocAttachmentVO> legacyDocAttachmentList = this.queryForList(this.GET_ATTACHMENT_DETAILS,
					legacyDocAttachmentVO);
			return legacyDocAttachmentList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getAttachmentDetailsCount(LegacyDocAttachmentVO legacyDocAttachmentVO) throws CMSException {
		try {
			int count = (Integer) this.queryForObject(this.GET_ATTACHMENT_DETAILS_COUNT, legacyDocAttachmentVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}