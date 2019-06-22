package com.worldcheck.atlas.bl.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IFeedBack;
import com.worldcheck.atlas.dao.masters.ClientFeedBackDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.masters.ClientFeedBackVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;

public class ClientFeedBackManager implements IFeedBack {
	private static final String RECLIST = "recList";
	private static final long KB = 1024L;
	private static final String SHOWKB = " KB";
	private static PropertyReaderUtil propertyReader;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.ClientFeedBackManager");
	ClientFeedBackDAO clientFeedBackDAO = new ClientFeedBackDAO();

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public void setClientFeedBackDAO(ClientFeedBackDAO clientFeedBackDAO) {
		this.clientFeedBackDAO = clientFeedBackDAO;
	}

	public List<UserMasterVO> getcaseOwnerList() throws CMSException {
		return this.clientFeedBackDAO.getcaseOwnerList();
	}

	public List<ClientFeedBackVO> getFeedBackCategoryList() throws CMSException {
		return this.clientFeedBackDAO.getFeedBackCategoryList();
	}

	public List<ClientFeedBackVO> getCRNList(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return this.clientFeedBackDAO.getCRNList(clientFeedBackVO);
	}

	public long getCRNListCount(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return this.clientFeedBackDAO.getCRNListCount(clientFeedBackVO);
	}

	public long saveClientFeedBack(ClientFeedBackVO clientFeedBackVO, HttpServletRequest request)
			throws CMSException, SQLException {
		String crnList = request.getParameter("linkedCRNList");
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String userName = userBean.getUserName();
		this.logger.debug("User Name is updatedby " + userName);
		if (Integer.parseInt(clientFeedBackVO.getFbStatusField()) == 5) {
			Date date = new Date();
			String completeDate = (new SimpleDateFormat("dd-MM-yy")).format(date);
			this.logger.debug("sysdate========>>>" + completeDate);
			clientFeedBackVO.setDateClosed(completeDate);
		} else {
			clientFeedBackVO.setDateClosed((String) null);
		}

		long fbId = this.clientFeedBackDAO.getNewFbId();
		clientFeedBackVO.setNewFbSeqId(fbId);
		clientFeedBackVO.setUpdatedBy(userName);
		String[] linkedCrnlist = crnList.split("\\s*,\\s*");
		ArrayList<ClientFeedBackVO> linkedCrnMapList = new ArrayList();

		for (int i = 0; i < linkedCrnlist.length; ++i) {
			ClientFeedBackVO cfbVO = new ClientFeedBackVO();
			this.logger.debug("what is in linked CRN list>>" + linkedCrnlist[i].length());
			if (linkedCrnlist[i].length() > 0) {
				cfbVO.setCrnMapID(fbId);
				cfbVO.setCrnName(linkedCrnlist[i]);
				cfbVO.setCrnLinkedBy(userName);
				linkedCrnMapList.add(cfbVO);
			}
		}

		this.logger.debug("Before Save Cleint Feedback In DAO :" + fbId);
		this.clientFeedBackDAO.saveClientFeedBack(clientFeedBackVO, linkedCrnMapList);
		this.logger.debug("After Save Cleint Feedback In DAO:" + fbId);
		String tempPath = propertyReader.getTempFBattachment();
		File file = new File(tempPath + "\\" + fbId);
		if (!file.exists()) {
			file.mkdir();
		}

		this.logger.debug("File path :" + file + " fbId :" + fbId);
		String sourceTempAttachments = propertyReader.getFeedback_temp_attachment_path() + request.getSession().getId();
		File source_Temp = new File(sourceTempAttachments);
		this.logger
				.debug("File source_Temp :" + sourceTempAttachments + " source_Temp.exists():" + source_Temp.exists());
		if (source_Temp.exists()) {
			File[] sFiles = (new File(sourceTempAttachments)).listFiles();
			ClientFeedBackVO cfVO = null;
			int copyStat = false;
			this.logger.debug("Inside IF:" + sFiles);
			if (sFiles != null) {
				File[] var20 = sFiles;
				int var19 = sFiles.length;

				for (int var18 = 0; var18 < var19; ++var18) {
					File f = var20[var18];
					byte copyStat = 0;

					try {
						this.logger.debug("FileUtils.copyFileToDirectory:" + f + ":" + file);
						FileUtils.copyFileToDirectory(f, file);
					} catch (IOException var24) {
						copyStat = -1;
						this.logger.debug("status of copy from tep to original path is=[" + copyStat + "]");
						this.logger.debug("File not copied = [" + f.getName() + "]");
					}

					if (copyStat != -1) {
						this.logger.debug("Data save on save button:" + tempPath + "\\" + fbId + "\\" + f.getName());
						cfVO = new ClientFeedBackVO();
						cfVO.setFileName(f.getName());
						cfVO.setFilePath(tempPath + "\\" + fbId + "\\" + f.getName());
						double size = (double) f.length() / 1024.0D;
						cfVO.setFileSize(size + " KB");
						cfVO.setNewFbSeqId(fbId);
						String user_Name = SBMUtils.getSession(request).getUser();
						cfVO.setUpdatedBy(user_Name);
						this.clientFeedBackDAO.saveAttachmentDetails(cfVO);
					}
				}
			}
		}

		return fbId;
	}

	public List<ClientFeedBackVO> searchFeedback(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		this.logger.debug("Length of Status >>>>>" + clientFeedBackVO.getSearchCaseStatus().length());
		clientFeedBackVO.setLimit(new Integer(clientFeedBackVO.getStart() + clientFeedBackVO.getLimit()));
		clientFeedBackVO.setStart(new Integer(clientFeedBackVO.getStart() + 1));
		String[] searchStatusList;
		ArrayList newList;
		String myInt;
		int var5;
		int var6;
		String[] var7;
		if (clientFeedBackVO.getSearchClientCode().length() > 0) {
			searchStatusList = clientFeedBackVO.getSearchClientCode().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var7 = searchStatusList;
			var6 = searchStatusList.length;

			for (var5 = 0; var5 < var6; ++var5) {
				myInt = var7[var5];
				newList.add(myInt);
				this.logger.debug("Status list>>>>" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setClientCodeList(newList);
			}
		}

		if (clientFeedBackVO.getSearchCaseOwner().length() > 0) {
			searchStatusList = clientFeedBackVO.getSearchCaseOwner().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var7 = searchStatusList;
			var6 = searchStatusList.length;

			for (var5 = 0; var5 < var6; ++var5) {
				myInt = var7[var5];
				newList.add(myInt);
				this.logger.debug("Status list>>>>" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setCaseOwnerList(newList);
			}
		}

		if (clientFeedBackVO.getSearchCaseStatus().length() > 0) {
			searchStatusList = clientFeedBackVO.getSearchCaseStatus().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var7 = searchStatusList;
			var6 = searchStatusList.length;

			for (var5 = 0; var5 < var6; ++var5) {
				myInt = var7[var5];
				newList.add(Integer.valueOf(myInt));
				this.logger.debug("Status list>>>>" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setStatusList(newList);
			}
		}

		return this.clientFeedBackDAO.searchFeedback(clientFeedBackVO);
	}

	public long searchFeedbackCount(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		this.logger.debug("Length of Status >>>>>" + clientFeedBackVO.getSearchCaseStatus().length());
		this.logger.debug("Length of client code >>>>>" + clientFeedBackVO.getSearchClientCode().length());
		this.logger.debug("Length of case owner >>>>>" + clientFeedBackVO.getSearchCaseOwner().length());
		String[] searchStatusList;
		ArrayList newList;
		String myInt;
		int var5;
		int var6;
		String[] var7;
		if (clientFeedBackVO.getSearchClientCode().length() > 0) {
			searchStatusList = clientFeedBackVO.getSearchClientCode().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var7 = searchStatusList;
			var6 = searchStatusList.length;

			for (var5 = 0; var5 < var6; ++var5) {
				myInt = var7[var5];
				newList.add(myInt);
				this.logger.debug("Status list>>>>" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setClientCodeList(newList);
			}
		}

		if (clientFeedBackVO.getSearchCaseOwner().length() > 0) {
			searchStatusList = clientFeedBackVO.getSearchCaseOwner().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var7 = searchStatusList;
			var6 = searchStatusList.length;

			for (var5 = 0; var5 < var6; ++var5) {
				myInt = var7[var5];
				newList.add(myInt);
				this.logger.debug("Status list>>>>" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setCaseOwnerList(newList);
			}
		}

		if (clientFeedBackVO.getSearchCaseStatus().length() > 0) {
			searchStatusList = clientFeedBackVO.getSearchCaseStatus().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var7 = searchStatusList;
			var6 = searchStatusList.length;

			for (var5 = 0; var5 < var6; ++var5) {
				myInt = var7[var5];
				newList.add(Integer.valueOf(myInt));
				this.logger.debug("Status list>>>>" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setStatusList(newList);
			}
		}

		return this.clientFeedBackDAO.searchFeedbackCount(clientFeedBackVO);
	}

	public ClientFeedBackVO getFeedBackInfo(long feedBackId) throws CMSException {
		return this.clientFeedBackDAO.getFeedBackInfo(feedBackId);
	}

	public List<ClientFeedBackVO> getLinkedCRNList(long l) throws CMSException {
		return this.clientFeedBackDAO.getLinkedCRNList(l);
	}

	public void linkUnlinkCrnOnUpdate(String crnList, int actionType, long fbId, String userName) throws CMSException {
		String[] updateCrnArr;
		ArrayList linkedCrnMapList;
		if (actionType == 1) {
			updateCrnArr = crnList.split("\\s*,\\s*");
			linkedCrnMapList = new ArrayList();
			HashMap<String, Object> linkedCrnMap = new HashMap();
			this.logger.debug("updateCrnArr.length>>" + updateCrnArr.length);

			for (int i = 0; i < updateCrnArr.length; ++i) {
				ClientFeedBackVO cfbVO = new ClientFeedBackVO();
				this.logger
						.debug("what is in linked CRN list>>" + updateCrnArr[i].length() + "Value>>" + updateCrnArr[i]);
				if (updateCrnArr[i].length() > 0) {
					cfbVO.setCrnMapID(fbId);
					cfbVO.setCrnName(updateCrnArr[i]);
					cfbVO.setCrnLinkedBy(userName);
					linkedCrnMapList.add(cfbVO);
				}
			}

			this.logger.debug("linkedCrnMapList.size()>>" + linkedCrnMapList.size());
			if (linkedCrnMapList.size() > 0) {
				linkedCrnMap.put("recList", linkedCrnMapList);
				this.clientFeedBackDAO.addCRNFBMap(linkedCrnMap);
			}
		} else {
			updateCrnArr = crnList.split("\\s*,\\s*");
			linkedCrnMapList = new ArrayList(updateCrnArr.length);
			ClientFeedBackVO cfbVO = new ClientFeedBackVO();
			String[] var12 = updateCrnArr;
			int var11 = updateCrnArr.length;

			for (int var13 = 0; var13 < var11; ++var13) {
				String myStr = var12[var13];
				linkedCrnMapList.add(myStr);
				this.logger.debug("Crn list>>>>" + myStr);
			}

			cfbVO.setLinkUnlinkCrnList(linkedCrnMapList);
			cfbVO.setNewFbSeqId(fbId);
			this.clientFeedBackDAO.unlinkCrnOnUpdate(cfbVO);
		}

	}

	public void updateClientFeedBack(ClientFeedBackVO clientFeedBackVO, HttpServletRequest request)
			throws CMSException {
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String userName = userBean.getUserName();
		this.logger.debug("User Name is updatedby " + userName);
		if (Integer.parseInt(clientFeedBackVO.getFbStatusField()) == 5) {
			Date date = new Date();
			String completeDate = (new SimpleDateFormat("dd-MM-yy")).format(date);
			this.logger.debug("sysdate========>>>" + completeDate);
			clientFeedBackVO.setDateClosed(completeDate);
		} else {
			clientFeedBackVO.setDateClosed((String) null);
		}

		clientFeedBackVO.setUpdatedBy(userName);
		this.clientFeedBackDAO.updateClientFeedBack(clientFeedBackVO);
		this.clientFeedBackDAO.addActionToHistory(clientFeedBackVO);
	}

	public void uploadFeedBackAttachment(List<FileItem> items, long fbID, String userName) throws CMSException {
		try {
			this.logger.debug("Inside saveFeedbackAttachements method ");
			ClientFeedBackVO cfVO = new ClientFeedBackVO();
			String tempPath = propertyReader.getTempFBattachment();
			String fileName = "";
			List<FileItem> fileItems = null;
			fileItems = items;
			this.logger.debug("" + items);
			Iterator itr = items.iterator();

			while (itr.hasNext()) {
				this.logger.debug("inside while");
				FileItem fileItems1 = (FileItem) itr.next();
				this.logger.debug(fileItems1.getFieldName());
				if (fileItems != null) {
					this.logger.debug(fileItems1.getFieldName());
					this.logger.debug("" + fileItems);
					if (!fileItems1.isFormField()) {
						String pathForFile = fileItems1.getName();
						pathForFile = pathForFile.replaceAll("\\[", "(");
						pathForFile = pathForFile.replaceAll("\\]", ")");
						this.logger.debug("Complete path of file is " + pathForFile);
						String[] tempPathArray = pathForFile.split("\\\\");
						int tempLen = tempPathArray.length;
						this.logger.debug("tempLen is " + tempLen);
						if (tempLen > 1) {
							fileName = tempPathArray[tempLen - 1];
							this.logger.debug("file name is " + fileName);
						} else {
							fileName = pathForFile;
							this.logger.debug("file name is " + pathForFile);
						}

						String firstFolder = tempPath + "/" + fbID;
						if ((new File(firstFolder)).mkdir()) {
							this.logger.debug("Directory Created");
						} else {
							this.logger.debug("Directory already exists");
						}

						tempPath = tempPath + "/" + fbID + "/" + fileName;
						File savedFile = new File(tempPath);
						fileItems1.write(savedFile);
						this.logger.debug("Successfully Uploaded");
						cfVO.setFileName(fileName);
						cfVO.setFilePath(tempPath);
						double fsize = (double) (fileItems1.getSize() / 1024L);
						String filesize = fsize + " KB";
						cfVO.setFileSize(filesize);
						cfVO.setNewFbSeqId(fbID);
						cfVO.setUpdatedBy(userName);
						this.clientFeedBackDAO.saveAttachmentDetails(cfVO);
						this.logger.debug("file path is " + tempPath);
					}
				}
			}

		} catch (FileUploadException var19) {
			throw new CMSException(this.logger, var19);
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}

	public void uploadTempFeedBackAttachment(List<FileItem> items, String userName, ClientFeedBackVO clientFeedBackVO)
			throws CMSException {
		try {
			this.clientFeedBackDAO.uploadTempFeedBackAttachment(clientFeedBackVO);
			String tempPath = propertyReader.getFeedback_temp_attachment_path();
			String fileName = "";
			List<FileItem> fileItems = null;
			fileItems = items;
			this.logger.debug("" + items);
			Iterator itr = items.iterator();

			while (itr.hasNext()) {
				this.logger.debug("inside while");
				FileItem fileItems1 = (FileItem) itr.next();
				this.logger.debug(fileItems1.getFieldName());
				if (fileItems != null) {
					this.logger.debug(fileItems1.getFieldName());
					this.logger.debug("" + fileItems);
					if (!fileItems1.isFormField()) {
						String pathForFile = fileItems1.getName();
						pathForFile = pathForFile.replaceAll("\\[", "(");
						pathForFile = pathForFile.replaceAll("\\]", ")");
						this.logger.debug("Complete path of file is " + pathForFile);
						String[] tempPathArray = pathForFile.split("\\\\");
						int tempLen = tempPathArray.length;
						this.logger.debug("tempLen is " + tempLen);
						if (tempLen > 1) {
							fileName = tempPathArray[tempLen - 1];
							this.logger.debug("file name is " + fileName);
						} else {
							fileName = pathForFile;
							this.logger.debug("file name is " + pathForFile);
						}

						String firstFolder = tempPath + "/" + clientFeedBackVO.getSessionID();
						if ((new File(firstFolder)).mkdir()) {
							this.logger.debug("Directory Created");
						} else {
							this.logger.debug("Directory already exists");
						}

						tempPath = tempPath + "/" + clientFeedBackVO.getSessionID() + "/" + fileName;
						File savedFile = new File(tempPath);
						fileItems1.write(savedFile);
						this.logger.debug("Successfully Uploaded");
						this.logger.debug("file path is " + tempPath);
					}
				}
			}

		} catch (FileUploadException var14) {
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	public List<ClientFeedBackVO> displayAttachDocuments(long newFbSeqId) throws CMSException {
		return this.clientFeedBackDAO.displayAttachDocuments(newFbSeqId);
	}

	public List<ClientFeedBackVO> displayTempAttachDocuments(ClientFeedBackVO clientFeedBackVO) throws CMSException {
		return this.clientFeedBackDAO.displayTempAttachDocuments(clientFeedBackVO);
	}

	public List<ClientFeedBackVO> getFeedbackForExport(Map<String, Object> excelParamMap) throws CMSException {
		this.logger.debug("Values " + excelParamMap.get("dateLoggedStart"));
		this.logger.debug("Values " + excelParamMap.get("dateLoggedEnd"));
		this.logger.debug("Values " + excelParamMap.get("searchClientCode"));
		this.logger.debug("Values " + excelParamMap.get("searchCaseOwner"));
		this.logger.debug("Values " + excelParamMap.get("searchCaseStatus"));
		ClientFeedBackVO clientFeedBackVO = new ClientFeedBackVO();
		if (excelParamMap.get("dateLoggedStart") != null && !excelParamMap.get("dateLoggedStart").equals("")) {
			clientFeedBackVO.setDateLoggedStart(excelParamMap.get("dateLoggedStart").toString());
		}

		if (excelParamMap.get("dateLoggedEnd") != null && !excelParamMap.get("dateLoggedEnd").equals("")) {
			clientFeedBackVO.setDateLoggedEnd(excelParamMap.get("dateLoggedEnd").toString());
		}

		String[] searchStatusList;
		ArrayList newList;
		String myInt;
		int var6;
		int var7;
		String[] var8;
		if (excelParamMap.get("searchClientCode") != null && !excelParamMap.get("searchClientCode").equals("")) {
			searchStatusList = excelParamMap.get("searchClientCode").toString().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var8 = searchStatusList;
			var7 = searchStatusList.length;

			for (var6 = 0; var6 < var7; ++var6) {
				myInt = var8[var6];
				newList.add(myInt);
				this.logger.debug("Client Code --" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setClientCodeList(newList);
			}
		}

		if (excelParamMap.get("searchCaseOwner") != null && !excelParamMap.get("searchCaseOwner").equals("")) {
			searchStatusList = excelParamMap.get("searchCaseOwner").toString().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var8 = searchStatusList;
			var7 = searchStatusList.length;

			for (var6 = 0; var6 < var7; ++var6) {
				myInt = var8[var6];
				newList.add(myInt);
				this.logger.debug("Case Owner --" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setCaseOwnerList(newList);
			}
		}

		if (excelParamMap.get("searchCaseStatus") != null && !excelParamMap.get("searchCaseStatus").equals("")) {
			searchStatusList = excelParamMap.get("searchCaseStatus").toString().split("\\s*,\\s*");
			newList = new ArrayList(searchStatusList.length);
			var8 = searchStatusList;
			var7 = searchStatusList.length;

			for (var6 = 0; var6 < var7; ++var6) {
				myInt = var8[var6];
				newList.add(Integer.valueOf(myInt));
				this.logger.debug("Status list --" + myInt);
			}

			if (newList != null) {
				clientFeedBackVO.setStatusList(newList);
			}
		}

		return this.clientFeedBackDAO.getFeedbackForExport(clientFeedBackVO);
	}

	public void removeAttachments(String documentList, String attachIds) throws CMSException {
		String[] docArr = documentList.split("\\s*::\\s*");
		String[] docIdArr = attachIds.split("\\s*,\\s*");
		List<Integer> docIDList = new ArrayList(docIdArr.length);
		this.logger.debug("array list<<<<" + docIdArr + ">>>>>");
		this.logger.debug("array list length" + docIdArr.length);
		String[] var9 = docIdArr;
		int var8 = docIdArr.length;

		String myStr2;
		int var7;
		for (var7 = 0; var7 < var8; ++var7) {
			myStr2 = var9[var7];
			docIDList.add(Integer.parseInt(myStr2));
			this.logger.debug("docIdArr val>>>>" + myStr2);
		}

		this.clientFeedBackDAO.removeAttachments(docIDList);
		var9 = docArr;
		var8 = docArr.length;

		for (var7 = 0; var7 < var8; ++var7) {
			myStr2 = var9[var7];
			this.logger.debug("Doc to remove>>>>" + myStr2);
			File file = new File(myStr2);
			boolean isDeleted = file.delete();
			this.logger.debug("Is Deleted>>>>" + isDeleted);
		}

	}

	public void removeAttachments(String documentList, String attachIds, String storeId) throws CMSException {
		String[] docArr = documentList.split("\\s*::\\s*");
		String[] docIdArr = attachIds.split("\\s*,\\s*");
		List<Integer> docIDList = new ArrayList(docIdArr.length);
		this.logger.debug("temporary attachment removal");
		this.logger.debug("array list<<<<" + docIdArr + ">>>>>");
		this.logger.debug("array list length" + docIdArr.length);
		String[] var10 = docIdArr;
		int var9 = docIdArr.length;

		String file;
		for (int var8 = 0; var8 < var9; ++var8) {
			file = var10[var8];
			docIDList.add(Integer.parseInt(file));
			this.logger.debug("docIdArr val>>>>" + file);
		}

		this.clientFeedBackDAO.removeTempAttachments(docIDList);
		file = null;
		String[] var11 = docArr;
		int var13 = docArr.length;

		for (var9 = 0; var9 < var13; ++var9) {
			String myStr2 = var11[var9];
			this.logger.debug("Doc to remove>>>>" + myStr2);
			File file = new File(myStr2);
			boolean isDeleted = file.delete();
			this.logger.debug("Is Deleted>>>>" + isDeleted);
		}

	}

	public List<ClientFeedBackVO> getFeedbackTypeList(String fieldId, String moduleId) throws CMSException {
		HashMap<String, String> map = new HashMap();
		map.put("fieldId", fieldId);
		map.put("moduleId", moduleId);
		return this.clientFeedBackDAO.getFeedbackTypeList(map);
	}
}