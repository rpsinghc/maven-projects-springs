package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ClientFeedBackVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;

public interface IFeedBack {
	List<UserMasterVO> getcaseOwnerList() throws CMSException;

	List<ClientFeedBackVO> getFeedBackCategoryList() throws CMSException;

	List<ClientFeedBackVO> getCRNList(ClientFeedBackVO var1) throws CMSException;

	long getCRNListCount(ClientFeedBackVO var1) throws CMSException;

	long saveClientFeedBack(ClientFeedBackVO var1, HttpServletRequest var2) throws CMSException, SQLException;

	long searchFeedbackCount(ClientFeedBackVO var1) throws CMSException;

	List<ClientFeedBackVO> searchFeedback(ClientFeedBackVO var1) throws CMSException;

	ClientFeedBackVO getFeedBackInfo(long var1) throws CMSException;

	List<ClientFeedBackVO> getLinkedCRNList(long var1) throws CMSException;

	void linkUnlinkCrnOnUpdate(String var1, int var2, long var3, String var5) throws CMSException;

	void updateClientFeedBack(ClientFeedBackVO var1, HttpServletRequest var2) throws CMSException;

	void uploadFeedBackAttachment(List<FileItem> var1, long var2, String var4) throws CMSException;

	void uploadTempFeedBackAttachment(List<FileItem> var1, String var2, ClientFeedBackVO var3) throws CMSException;

	List<ClientFeedBackVO> displayAttachDocuments(long var1) throws CMSException;

	List<ClientFeedBackVO> displayTempAttachDocuments(ClientFeedBackVO var1) throws CMSException;

	List<ClientFeedBackVO> getFeedbackForExport(Map<String, Object> var1) throws CMSException;

	void removeAttachments(String var1, String var2) throws CMSException;

	void removeAttachments(String var1, String var2, String var3) throws CMSException;

	List<ClientFeedBackVO> getFeedbackTypeList(String var1, String var2) throws CMSException;
}