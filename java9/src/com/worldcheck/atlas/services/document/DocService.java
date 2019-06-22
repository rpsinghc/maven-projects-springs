package com.worldcheck.atlas.services.document;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.dao.document.DocMapDAO;
import com.worldcheck.atlas.document.DocCreator;
import com.worldcheck.atlas.document.DocSender;
import com.worldcheck.atlas.document.ManageDocument;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.document.DocMapVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.fileupload.FileItem;

public class DocService {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.services.document.DocService");
	private DocCreator docCreator = null;
	private DocSender docSender = null;
	private ManageDocument docDisplay = null;
	private DocMapDAO docMapDAO = null;

	public void setDocCreator(DocCreator docCreator) {
		this.docCreator = docCreator;
	}

	public void setDocSender(DocSender docSender) {
		this.docSender = docSender;
	}

	public void CreateDoc(String userName, List<FileItem> fileItems, String pid, String[] folderNames, Session session)
			throws CMSException {
		boolean isSessionRemovable = false;
		ResourceLocator locator = ResourceLocator.self();
		if (session == null) {
			session = locator.getSBMService().getSession(userName);
			isSessionRemovable = true;
		}

		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		String teamName = "";
		CaseDetails caseDetailsVO = locator.getCaseDetailService().getCaseInfoForPID(String.valueOf(parentPID));
		String crn = caseDetailsVO.getCrn();
		this.logger.debug("crn is " + crn + " .For pid " + parentPID);
		String caseManager = caseDetailsVO.getCaseMgrId();
		this.logger.debug("CaseManager is " + caseManager);
		if (caseManager.equalsIgnoreCase(userName)) {
			teamName = "CaseManager";
		}

		String teams = locator.getTeamAssignmentService().getAssignedTeamsForUserAndCrn(crn, userName);
		if (teams == null) {
			teams = "";
		}

		if (teamName.equalsIgnoreCase("")) {
			teamName = teams;
		} else if (!"".equalsIgnoreCase(teams) && teams != null && !"null".equals(teams)) {
			teamName = teamName + "," + teams;
		}

		if (teamName.equalsIgnoreCase("")) {
			teamName = this.prepareTeamNamesForSameOfficeUsers(session.getUser(), locator, teamName, crn);
			if (teamName.equalsIgnoreCase("")) {
				teamName = "Others";
			}
		}

		this.logger.debug("inside createDoc  method. Team name(s) are" + teamName);

		try {
			List<String> listOfDocs = this.docCreator.CreateDoc("admin", fileItems, String.valueOf(parentPID),
					folderNames);
			List<String> listOfDocNames = new ArrayList();
			String docIds = "";
			int i = 0;

			while (true) {
				if (i >= listOfDocs.size()) {
					this.logger.debug("docIds are " + docIds);
					this.docMapDAO.updateDocCreatorName(docIds, userName);
					i = locator.getTaskService().updateDocuments(listOfDocNames, listOfDocs, teamName, parentPID);
					this.logger.debug("rows updated " + i);
					break;
				}

				listOfDocNames.add((String) this.docDisplay.getDocumentById((String) listOfDocs.get(i)).get("docName"));
				if (docIds.equals("")) {
					docIds = "'" + (String) listOfDocs.get(i) + "'";
				} else {
					docIds = docIds + ",'" + (String) listOfDocs.get(i) + "'";
				}

				++i;
			}
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}

		if (isSessionRemovable) {
			locator.getSBMService().closeSession(session);
		}

	}

	private String prepareTeamNamesForSameOfficeUsers(String userName, ResourceLocator locator, String teamName,
			String crn) throws CMSException {
		this.logger.debug("prepareTeamNamesForSameOfficeUsers :: userName is " + userName);
		List<TeamDetails> teamsList = locator.getTeamAssignmentService().getCaseTeamDetails(crn);
		UserMasterVO userInfo = locator.getUserService().getUserInfo(userName);
		this.logger.debug("user office name " + userInfo.getOfficeId());
		Iterator var8 = teamsList.iterator();

		while (var8.hasNext()) {
			TeamDetails teamDetails = (TeamDetails) var8.next();
			this.logger.debug("office name is " + teamDetails.getOffice());
			this.logger.debug("team type " + teamDetails.getTeamType());
			String managerOffice = "";
			if (teamDetails.getOffice() == null) {
				managerOffice = "" + locator.getUserService().getUserInfo(teamDetails.getManagerName()).getOfficeId();
			}

			this.logger.debug("manager's office is " + managerOffice);
			if (Long.parseLong(
					teamDetails.getOffice() != null ? teamDetails.getOffice() : managerOffice) == (long) userInfo
							.getOfficeId()) {
				if (teamDetails.getTeamType().contains("Primary")) {
					teamName = "Primary";
				}

				if (teamDetails.getTeamType().contains("Supporting - Internal")) {
					if (teamName.equalsIgnoreCase("")) {
						teamName = "Supporting - Internal";
					} else {
						teamName = teamName + "," + "Supporting - Internal";
					}
				}

				if (teamDetails.getTeamType().contains("Supporting - BI")) {
					if (teamName.equalsIgnoreCase("")) {
						teamName = "Supporting - BI";
					} else {
						teamName = teamName + "," + "Supporting - BI";
					}
				}

				if (teamDetails.getTeamType().contains("Supporting - Vendor")) {
					if (teamName.equalsIgnoreCase("")) {
						teamName = "Supporting - Vendor";
					} else {
						teamName = teamName + "," + "Supporting - Vendor";
					}
				}
			}
		}

		return teamName;
	}

	public List<String> CreateDocForISIS(String userName, String[] tempPath, String pid, String[] folderNames,
			Session session, Timestamp fileCreationDate) throws CMSException {
		List<String> listOfDocs = null;
		boolean isSessionRemovable = false;
		ResourceLocator locator = ResourceLocator.self();
		if (session == null) {
			session = locator.getSBMService().getSession(userName);
			isSessionRemovable = true;
		}

		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		String teamName = "Others";
		this.logger.debug("inside createDoc  method. Team name(s) are" + teamName);

		try {
			userName = "AEDDO";
			listOfDocs = this.docCreator.CreateDocForISIS("admin", tempPath, String.valueOf(parentPID), folderNames);
			List<String> listOfDocNames = new ArrayList();
			String docIds = "";
			int i = 0;

			while (true) {
				if (i >= listOfDocs.size()) {
					this.logger.debug("docIds are " + docIds);
					this.docMapDAO.updateDocCreatorName(docIds, userName);
					this.docMapDAO.updateDocCreatorDate(docIds, fileCreationDate);
					i = locator.getTaskService().updateDocuments(listOfDocNames, listOfDocs, teamName, parentPID);
					this.logger.debug("rows updated " + i);
					break;
				}

				listOfDocNames.add((String) this.docDisplay.getDocumentById((String) listOfDocs.get(i)).get("docName"));
				if (docIds.equals("")) {
					docIds = "'" + (String) listOfDocs.get(i) + "'";
				} else {
					docIds = docIds + ",'" + (String) listOfDocs.get(i) + "'";
				}

				++i;
			}
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}

		if (isSessionRemovable) {
			locator.getSBMService().closeSession(session);
		}

		return listOfDocs;
	}

	public List<String> CreateDocForJUNO(String userName, String[] tempPath, String pid, String[] folderNames,
			Session session) throws CMSException {
		List<String> listOfDocs = null;
		boolean isSessionRemovable = false;
		ResourceLocator locator = ResourceLocator.self();
		if (session == null) {
			session = locator.getSBMService().getSession(userName);
			isSessionRemovable = true;
		}

		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		String teamName = "";
		CaseDetails caseDetailsVO = locator.getCaseDetailService().getCaseInfoForPID(String.valueOf(parentPID));
		String crn = caseDetailsVO.getCrn();
		this.logger.debug("crn is " + crn + " .For pid " + parentPID);
		String caseManager = caseDetailsVO.getCaseMgrId();
		this.logger.debug("CaseManager is " + caseManager);
		if (caseManager.equalsIgnoreCase(userName)) {
			teamName = "CaseManager";
		}

		String teams = locator.getTeamAssignmentService().getAssignedTeamsForUserAndCrn(crn, userName);
		if (teams == null) {
			teams = "";
		}

		if (teamName.equalsIgnoreCase("")) {
			teamName = teams;
		} else if (!"".equalsIgnoreCase(teams) && teams != null && !"null".equals(teams)) {
			teamName = teamName + "," + teams;
		}

		if (teamName.equalsIgnoreCase("")) {
			teamName = this.prepareTeamNamesForSameOfficeUsers(session.getUser(), locator, teamName, crn);
			if (teamName.equalsIgnoreCase("")) {
				teamName = "Others";
			}
		}

		this.logger.debug("inside createDoc  method. Team name(s) are" + teamName);

		try {
			listOfDocs = this.docCreator.CreateDocForISIS("admin", tempPath, String.valueOf(parentPID), folderNames);
			List<String> listOfDocNames = new ArrayList();
			String docIds = "";
			int i = 0;

			while (true) {
				if (i >= listOfDocs.size()) {
					this.logger.debug("docIds are " + docIds);
					this.docMapDAO.updateDocCreatorName(docIds, userName);
					i = locator.getTaskService().updateDocuments(listOfDocNames, listOfDocs, teamName, parentPID);
					this.logger.debug("rows updated " + i);
					break;
				}

				listOfDocNames.add((String) this.docDisplay.getDocumentById((String) listOfDocs.get(i)).get("docName"));
				if (docIds.equals("")) {
					docIds = "'" + (String) listOfDocs.get(i) + "'";
				} else {
					docIds = docIds + ",'" + (String) listOfDocs.get(i) + "'";
				}

				++i;
			}
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}

		if (isSessionRemovable) {
			locator.getSBMService().closeSession(session);
		}

		return listOfDocs;
	}

	public List<String> CreateDocForRecurrentCase(String userName, String teamName, String[] tempPath, String pid,
			String[] folderNames, Session session) throws CMSException {
		List<String> listOfDocs = null;
		boolean isSessionRemovable = false;
		ResourceLocator locator = ResourceLocator.self();
		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		this.logger.debug("inside createDoc  method. Team name(s) are" + teamName);

		try {
			listOfDocs = this.docCreator.CreateDocForISIS("admin", tempPath, String.valueOf(parentPID), folderNames);
			List<String> listOfDocNames = new ArrayList();
			String docIds = "";
			int i = 0;

			while (true) {
				if (i >= listOfDocs.size()) {
					this.logger.debug("docIds are " + docIds);
					this.docMapDAO.updateDocCreatorName(docIds, userName);
					i = locator.getTaskService().updateDocuments(listOfDocNames, listOfDocs, teamName, parentPID);
					this.logger.debug("rows updated " + i);
					break;
				}

				listOfDocNames.add((String) this.docDisplay.getDocumentById((String) listOfDocs.get(i)).get("docName"));
				if (docIds.equals("")) {
					docIds = "'" + (String) listOfDocs.get(i) + "'";
				} else {
					docIds = docIds + ",'" + (String) listOfDocs.get(i) + "'";
				}

				++i;
			}
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}

		if (isSessionRemovable) {
			locator.getSBMService().closeSession(session);
		}

		return listOfDocs;
	}

	public Map<String, String> getDocuments(String pid, int start, int limit, Session session) throws CMSException {
		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		return this.docSender.getDocuments(String.valueOf(parentPID), start, limit);
	}

	public Map<String, String> getAllDocuments(String pid, Session session) throws CMSException {
		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		return this.docSender.getAllData(String.valueOf(parentPID));
	}

	public boolean isClientSubmissionPermitted(String pid, Session session) throws CMSException, NullPointerException {
		boolean hasPermission = false;
		Map<String, String> mapOfDocs = this.getAllDocuments(pid, session);
		if (mapOfDocs.size() < 1) {
			throw new NullPointerException("No Document Attached");
		} else {
			Set<String> docIds = mapOfDocs.keySet();
			this.logger.debug("isMarked status " + docIds.size());
			hasPermission = ResourceLocator.self().getTaskService().isMarked(docIds);
			this.logger.debug("isMarked status " + hasPermission);
			return hasPermission;
		}
	}

	public int getDocumentCount(String pid, Session session) throws CMSException {
		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		return this.docSender.getDocumentCount(String.valueOf(parentPID));
	}

	public Map<String, Object> displayDocument(String docId) {
		return this.docDisplay.getDocumentById(docId);
	}

	public void setDocDisplay(ManageDocument docDisplay) {
		this.docDisplay = docDisplay;
	}

	public void delDocument(String docIds) throws CMSException {
		int i = this.docMapDAO.deleteDocumentFromSavvion(docIds);
		if (i > 0) {
			i = ResourceLocator.self().getTaskService().delDocument(docIds);
		}

		this.logger.debug("total rows deleted " + i);
	}

	public List<String> getDocumentFolderNames(String mode, String userId) throws CMSException {
		new ArrayList();
		List<String> listOfNames = this.docDisplay.getDocumentFolderNames(mode);
		this.logger.debug("mode is " + mode);
		this.logger.debug("size od the list is " + listOfNames.size());
		if (listOfNames.size() > 0 && !ResourceLocator.self().getTaskService().hasPermissionForSensitiveFile(userId)) {
			listOfNames.remove("Sensitive");
		}

		return listOfNames;
	}

	public void updateStatus(String pid, String docId, Session session) throws CMSException {
		long parentPID = 0L;
		if (!ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(pid), session)) {
			parentPID = (Long) ResourceLocator.self().getSBMService().getDataslotValue(Long.parseLong(pid), "parentPID",
					session);
		}

		if (parentPID == 0L) {
			parentPID = Long.parseLong(pid);
		}

		ResourceLocator.self().getTaskService().resetStatus(this.docMapDAO.getAllDocumentIds(String.valueOf(parentPID)),
				docId);
	}

	public void setDocMapDAO(DocMapDAO docMapDAO) {
		this.docMapDAO = docMapDAO;
	}

	public List<Map<String, Object>> downloadDocument(String docIds) throws CMSException {
		String[] tokens = docIds.split(",");
		List<Map<String, Object>> mapList = new ArrayList();

		for (int i = 0; i < tokens.length; ++i) {
			Map<String, Object> mp = this.displayDocument(tokens[i]);
			DocMapVO vo = this.docMapDAO.getDocumentByDocId(tokens[i]);
			mp.put("pid", vo.getPid());
			mp.put("version", vo.getVersion());
			mapList.add(mp);
		}

		return mapList;
	}

	public void copyWithStreams(File aSourceFile, File aTargetFile, boolean aAppend) throws IOException {
		this.logger.debug("in copyWithStreams method");
		File aTargetDir = aTargetFile.getParentFile();
		if (!aTargetDir.exists()) {
			aTargetDir.mkdirs();
		}

		InputStream inStream = null;
		OutputStream outStream = null;
		byte[] bucket = new byte['耀'];
		inStream = new BufferedInputStream(new FileInputStream(aSourceFile));
		outStream = new BufferedOutputStream(new FileOutputStream(aTargetFile, aAppend));
		int bytesRead = 0;

		while (bytesRead != -1) {
			bytesRead = inStream.read(bucket);
			if (bytesRead > 0) {
				outStream.write(bucket, 0, bytesRead);
			}
		}

		if (inStream != null) {
			inStream.close();
		}

		if (outStream != null) {
			outStream.close();
		}

	}

	public String generateZipFile(List<File> sourceFiles, String zipFile) throws IOException {
		this.logger.debug("in generateZipFile method");
		byte[] buffer = new byte[1024];
		FileOutputStream fout = new FileOutputStream(zipFile);
		ZipOutputStream zout = new ZipOutputStream(fout);
		String fileName = "";
		this.logger.debug("Size of list of file :: " + sourceFiles.size());

		for (int i = 0; i < sourceFiles.size(); ++i) {
			fileName = ((File) sourceFiles.get(i)).getName();
			String absoluteFilePath = ((File) sourceFiles.get(i)).getAbsolutePath();
			FileInputStream fin = new FileInputStream(absoluteFilePath);
			this.logger.debug("File name is :: " + fileName);
			this.logger.debug("File path is :: " + ((File) sourceFiles.get(i)).getAbsolutePath());
			String[] temp = ((File) sourceFiles.get(i)).getAbsolutePath().split("\\\\");
			String folderName = temp[temp.length - 2];
			this.logger.debug("- - - - - - - - - - - - - - - -");
			zout.putNextEntry(new ZipEntry(folderName + "/" + fileName));

			int length;
			long totalSize;
			for (totalSize = 0L; (length = fin.read(buffer)) > 0; totalSize += (long) length) {
				zout.write(buffer, 0, length);
			}

			this.logger.debug("written " + totalSize + " bytes to zip file");
			zout.closeEntry();
			fin.close();
		}

		zout.close();
		return zipFile;
	}
}