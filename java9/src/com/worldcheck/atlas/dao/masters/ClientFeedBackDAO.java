package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientFeedBackVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ClientFeedBackDAO extends SqlMapClientTemplate {
	private static final String FEEDBACK_GETFEEDBACKCATEGORYLIST = "ClientFeedBack.getFeedBackCategoryList";
	private static final String FEEDBACK_GETCASEOWNERLIST = "ClientFeedBack.getcaseOwnerList";
	private static final String FEEDBACK_UNLINKED_CRN = "ClientFeedBack.getUnlinkedCRN";
	private static final String FEEDBACK_UNLINKED_CRN_COUNT = "ClientFeedBack.getUnlinkedCRNCount";
	private static final String GET_FEEDBACK_ID = "ClientFeedBack.getnewfeedbackID";
	private static final String ADD_NEW_FEEDBACK = "ClientFeedBack.addFeedback";
	private static final String UPLOAD_NEW_TEMP_FEEDBACK = "ClientFeedBack.addTempAttachmentDetails";
	private static final String UPDATE_NEW_FEEDBACK = "ClientFeedBack.updateFeedback";
	private static final String ADD_CRN_FEEDBACK_MAP = "ClientFeedBack.addCRNFBMap";
	private static final String FEEDBACK_SEARCH = "ClientFeedBack.searchFeedBack";
	private static final String FEEDBACK_SEARCH_COUNT = "ClientFeedBack.searchFeedBackCount";
	private static final String GET_FEEDBACK_INFO = "ClientFeedBack.getFeedBackInfo";
	private static final String GET_LINKED_CRN_LIST = "ClientFeedBack.getLinkedCRNList";
	private static final String Link_CRN_ONUPDATE = "ClientFeedBack.linkCRNOnUpdate";
	private static final String UNlink_CRN_ONUPDATE = "ClientFeedBack.UnlinkCRNOnUpdate";
	private static final String ADD_ATTACHMENT_DETAILS = "ClientFeedBack.addAttachmentDetails";
	private static final String GET_ATTACHMENT_DETAILS = "ClientFeedBack.getAttachmentDetails";
	private static final String GET_TEMP_ATTACHMENT_DETAILS = "ClientFeedBack.getTempAttachmentDetails";
	private static final String CLIENT_FEEDBACK_SEARCH_EXPORT = "ClientFeedBack.searchFeedbackForExport";
	private static final String REMOVE_ATTACHMENTS = "ClientFeedBack.removeAttachments";
	private static final String REMOVE_TEMP_ATTACHMENTS = "ClientFeedBack.removeTempAttachments";
	private static final String ADD_ACTION_HISTORY = "ClientFeedBack.actionHistory";
	private static final String RECLIST = "recList";
	private static final String FEEDBACK_GETFEEDBACKTYPELIST = "ClientFeedBack.getFeedBackTypeList";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ClientFeedBackDAO");

	public List<UserMasterVO> getcaseOwnerList() throws CMSException {
		this.logger.debug("Inside getcaseOwnerList DAO");
		List<UserMasterVO> ownerList = null;
		ownerList = this.queryForList("ClientFeedBack.getcaseOwnerList");
		return ownerList;
	}

	public List<ClientFeedBackVO> getFeedBackCategoryList() throws CMSException {
		return this.queryForList("ClientFeedBack.getFeedBackCategoryList");
	}

	public List<ClientFeedBackVO> getCRNList(ClientFeedBackVO clientFeedBackVO) {
		return this.queryForList("ClientFeedBack.getUnlinkedCRN", clientFeedBackVO);
	}

	public long getCRNListCount(ClientFeedBackVO clientFeedBackVO) {
		return (Long) this.queryForObject("ClientFeedBack.getUnlinkedCRNCount", clientFeedBackVO);
	}

	public long getNewFbId() {
		return (Long) this.queryForObject("ClientFeedBack.getnewfeedbackID");
	}

	public void addCRNFBMap(HashMap<String, Object> linkedCrnMap) {
		this.insert("ClientFeedBack.addCRNFBMap", linkedCrnMap);
	}

	public List<ClientFeedBackVO> searchFeedback(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return this.queryForList("ClientFeedBack.searchFeedBack", clientFeedBackVO);
	}

	public long searchFeedbackCount(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return (Long) this.queryForObject("ClientFeedBack.searchFeedBackCount", clientFeedBackVO);
	}

	public ClientFeedBackVO getFeedBackInfo(long feedBackId) throws CMSException {
		return (ClientFeedBackVO) this.queryForObject("ClientFeedBack.getFeedBackInfo", feedBackId);
	}

	public List<ClientFeedBackVO> getLinkedCRNList(long fbId) throws CMSException {
		return this.queryForList("ClientFeedBack.getLinkedCRNList", fbId);
	}

	public void linkCrnOnUpdate(ClientFeedBackVO cfbVO) throws CMSException {
		this.insert("ClientFeedBack.linkCRNOnUpdate", cfbVO);
	}

	public void unlinkCrnOnUpdate(ClientFeedBackVO cfbVO) throws CMSException {
		this.delete("ClientFeedBack.UnlinkCRNOnUpdate", cfbVO);
	}

	public void updateClientFeedBack(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		this.update("ClientFeedBack.updateFeedback", clientFeedBackVO);
	}

	public void saveAttachmentDetails(ClientFeedBackVO cfVO) throws CMSException {
		this.insert("ClientFeedBack.addAttachmentDetails", cfVO);
	}

	public List<ClientFeedBackVO> displayAttachDocuments(long fbSeqId) throws CMSException {
		return this.queryForList("ClientFeedBack.getAttachmentDetails", fbSeqId);
	}

	public List<ClientFeedBackVO> displayTempAttachDocuments(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return this.queryForList("ClientFeedBack.getTempAttachmentDetails", clientFeedBackVO);
	}

	public List<ClientFeedBackVO> getFeedbackForExport(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return this.queryForList("ClientFeedBack.searchFeedbackForExport", clientFeedBackVO);
	}

	public void removeAttachments(List<Integer> docIDList) throws CMSException {
		this.delete("ClientFeedBack.removeAttachments", docIDList);
	}

	public void removeTempAttachments(List<Integer> docIDList) throws CMSException {
		this.delete("ClientFeedBack.removeTempAttachments", docIDList);
	}

	public void addActionToHistory(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		this.insert("ClientFeedBack.actionHistory", clientFeedBackVO);
	}

	public void saveClientFeedBack(ClientFeedBackVO clientFeedBackVO, ArrayList<ClientFeedBackVO> linkedCrnMapList)
			throws CMSException, SQLException {
		HashMap<String, Object> linkedCrnMap = new HashMap();
		this.insert("ClientFeedBack.addFeedback", clientFeedBackVO);
		this.insert("ClientFeedBack.actionHistory", clientFeedBackVO);
		this.logger.debug("Size of Map" + linkedCrnMap.size());
		if (linkedCrnMapList.size() > 0) {
			linkedCrnMap.put("recList", linkedCrnMapList);
			this.insert("ClientFeedBack.addCRNFBMap", linkedCrnMap);
		}

	}

	public void uploadTempFeedBackAttachment(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		this.insert("ClientFeedBack.addTempAttachmentDetails", clientFeedBackVO);
	}

	public List<ClientFeedBackVO> getFeedbackTypeList(Map<String, String> map) throws CMSException {
		return this.queryForList("ClientFeedBack.getFeedBackTypeList", map);
	}
}