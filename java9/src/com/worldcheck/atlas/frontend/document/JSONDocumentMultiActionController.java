package com.worldcheck.atlas.frontend.document;

import com.savvion.sbm.dms.svo.Document;
import com.worldcheck.atlas.document.util.DocUtilCommon;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.JsonBeanUtil;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.document.DisplayDocVO;
import com.worldcheck.atlas.vo.document.DocMapVO;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONDocumentMultiActionController extends JSONMultiActionController {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.document.JSONDocumentMultiActionController");
	private final String JSON_VIEW = "jsonView";
	private static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	private static final String DISPLAY_MODE_PARAMS = "displayMode";
	private static final String USER_ID_PARAMS = "userId";
	private static final String LIST_RESPONSE_PARAMS = "listInReference";
	private static final String DOC_IDS_PARAMS = "docIds";
	private static final String URL_PARAMS = "/sbm/bpmportal/atlas/showDocument.do?docId=";
	private static final String KEY_PARAMS = "key";
	private static final String KEY_PARAMS_VALUE = "Not Permitted";
	private static final String LIST_FOLDER_RESPONSE_PARAMS = "listOfFolderNames";
	private static PropertyReaderUtil propertyReader;
	private static final String FOLDER_NAMES_PARAMS = "folderNames";
	private static final String FILE_NAME = "File Name:";
	private static final String FILE_VERSION = ", File Version:";
	private static final String FILE_LENGTH = ", File Size:";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public ModelAndView displayAllDocuments(HttpServletRequest request, HttpServletResponse response) {
		int start = Integer.parseInt(StringUtils.checkPaginationParams("start", request.getParameter("start")));
		int limit = Integer.parseInt(StringUtils.checkPaginationParams("limit", request.getParameter("limit")));
		this.logger.debug("start is " + start + " limit is " + limit);
		String activityName = request.getParameter("activity");
		ResourceLocator locator = ResourceLocator.self();
		String pid = activityName.split("::")[0].split("#")[1];
		String userId = request.getParameter("userId");
		this.logger.debug("PID is " + pid);
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		List list;
		try {
			list = this.getListOfDocs(userId, pid,
					locator.getDocService().getDocuments(pid, start + 1, start + limit, SBMUtils.getSession(request)));
		} catch (CMSException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		}

		viewForJSON.addObject("listInReference", list);

		try {
			viewForJSON.addObject("total", locator.getDocService().getDocumentCount(pid, SBMUtils.getSession(request)));
			return viewForJSON;
		} catch (CMSException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}
	}

	private List<DisplayDocVO> getListOfDocs(String userId, String pid, Map<String, String> map) throws CMSException {
		int i = 1;
		Set<String> set = map.keySet();
		Map<String, DocMapVO> docMap = new HashMap();
		List<DocMapVO> listOfCustomVO = ResourceLocator.self().getTaskService().getDocs(set);
		Iterator iterator = listOfCustomVO.iterator();

		while (iterator.hasNext()) {
			DocMapVO docMapVO = (DocMapVO) iterator.next();
			docMap.put(docMapVO.getDocId(), docMapVO);
		}

		List<DisplayDocVO> list = new ArrayList();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			this.logger.debug("doc id is " + string + "doc name is " + (String) map.get(string));
			Document doc = DocUtilCommon.getDocService().getDocumentById(DocUtilCommon.getDSContext(), string);
			String[] temp = doc.getPath().split(pid)[0].split("\\/");
			boolean isSensitiveFolderPermitted = true;
			if ("Sensitive".equalsIgnoreCase(temp[3])) {
				isSensitiveFolderPermitted = ResourceLocator.self().getTaskService()
						.hasPermissionForSensitiveFile(userId);
			}

			if (isSensitiveFolderPermitted) {
				DocMapVO customDocumentVO = (DocMapVO) docMap.get(string);
				DisplayDocVO vo = new DisplayDocVO();
				vo.setId("" + i);
				vo.setFolderName(temp[3]);
				Date dt = new Date(doc.getLastUpdatedDate());
				SimpleDateFormat dateFOrmat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				vo.setDateUploded(dateFOrmat.format(dt));
				this.logger.debug("DateFormat::" + dateFOrmat.format(dt));
				this.logger.debug("Date::" + dt);
				this.logger.debug("Date into timestamp::" + dt.getTime());
				vo.setDateUploadedTime(dt.getTime());
				String tempURL = "<a href='/sbm/bpmportal/atlas/showDocument.do?docId=" + string + "'>"
						+ ((String) map.get(string)).split("\\?\\?")[0] + "</a>";
				vo.setFileNameURL(tempURL);
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(5);
				vo.setFileSize(nf.format((double) doc.getSize() / 1024.0D));
				vo.setOwner(doc.getCreator());
				String markedStatus = customDocumentVO.getMarked() == 1L ? "marked" : "";
				this.logger.debug("##FileName::" + tempURL + "MarkedStatus::" + markedStatus);
				vo.setStatus(markedStatus);
				vo.setTeamOfAnalyst(customDocumentVO.getTeamName());
				vo.setVersion("" + customDocumentVO.getVersion());
				this.logger.debug("customDocumentVO.getUploadedByJuno() is " + customDocumentVO.getUploadedByJuno());
				boolean isUploadedByJuno = customDocumentVO.getUploadedByJuno() == 1L;
				vo.setUploadedByJuno(isUploadedByJuno);
				int officeID = ResourceLocator.self().getTaskService().getOfficeId(doc.getCreator());
				this.logger.debug("office id is " + officeID);
				vo.setOfficeID(officeID);
				list.add(vo);
				++i;
			}
		}

		return list;
	}

	public ModelAndView markStatusAsFinal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String activityName = request.getParameter("activity");
		String pid = activityName.split("::")[0].split("#")[1];
		String crn = request.getParameter("crn");
		String version = request.getParameter("version");
		String fileSize = request.getParameter("fileSize");
		String markedFileStatus = request.getParameter("markedFileStatus");
		this.logger.debug("markedFileStatus::" + markedFileStatus + "## length::" + markedFileStatus.length());
		String previousMarkedFileInfo = "";
		String newMarkedFileInfo = "";
		String docId = request.getParameter("docId");

		try {
			if (!markedFileStatus.equals("") || markedFileStatus.trim().length() > 0) {
				previousMarkedFileInfo = "File Name:" + markedFileStatus.split(">")[1].split("<")[0];
				previousMarkedFileInfo = previousMarkedFileInfo + ", File Version:" + markedFileStatus.split("::")[1];
				previousMarkedFileInfo = previousMarkedFileInfo + ", File Size:" + markedFileStatus.split("::")[2];
			}

			if (!docId.equals("") || docId.trim().length() > 0) {
				newMarkedFileInfo = "File Name:" + docId.split(">")[1].split("<")[0];
				newMarkedFileInfo = newMarkedFileInfo + ", File Version:" + version;
				newMarkedFileInfo = newMarkedFileInfo + ", File Size:" + fileSize;
			}

			String folderName = request.getParameter("folderName");
			if (!"Final Report".equalsIgnoreCase(folderName)) {
				viewForJSON.addObject("key", "Not Permitted");
				return viewForJSON;
			}

			docId = docId.split("=")[2].split("'")[0];
			this.logger.debug("docId is " + docId);
			ResourceLocator.self().getDocService().updateStatus(pid, docId, SBMUtils.getSession(request));
			CaseHistory caseHistory = new CaseHistory();
			caseHistory.setCRN(crn);
			caseHistory.setProcessCycle("Final");
			if (crn != null && !crn.isEmpty()) {
				caseHistory.setPid(pid);
				caseHistory.setTaskName("Client Submission Task");
				caseHistory.setTaskStatus("In Progress");
				caseHistory.setPerformer(SBMUtils.getSession(request).getUser());
				caseHistory.setAction("Marked Final Report");
				caseHistory.setOldInfo(previousMarkedFileInfo);
				caseHistory.setNewInfo(newMarkedFileInfo);
				this.logger.debug("CRN:" + caseHistory.getCRN());
				this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
				this.logger.debug("PID:" + caseHistory.getPid());
				this.logger.debug("Performer:" + caseHistory.getPerformer());
				this.logger.debug("Action:" + caseHistory.getAction());
				this.logger.debug("Old Info:" + caseHistory.getOldInfo());
				this.logger.debug("New Info:" + caseHistory.getNewInfo());
				ResourceLocator.self().getCaseHistoryService().setCaseHistoryForUploadFinalDocument(caseHistory);
			}
		} catch (NumberFormatException var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		} catch (CMSException var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}

		viewForJSON.addObject("key", "");
		return viewForJSON;
	}

	public ModelAndView markRevisedDocumentAsFinal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String activityName = request.getParameter("activity");
		String pid = activityName.split("::")[0].split("#")[1];
		ResourceLocator resourceLocator = ResourceLocator.self();
		String crn = request.getParameter("crn");
		String caseStatus = request.getParameter("caseStatus");
		String markedFileStatus = request.getParameter("markedFileStatus");
		String version = request.getParameter("version");
		String fileSize = request.getParameter("fileSize");
		String docId = request.getParameter("docId");
		String newMarkedFileInfo = "";
		String previousMarkedFileInfo = "";
		boolean taskCompleted = false;

		try {
			if (caseStatus.equals("Completed")) {
				taskCompleted = ResourceLocator.self().getSBMService().isTaskCompleted(Long.valueOf(pid),
						SBMUtils.getSession(request));
			}

			this.logger.debug("taskCompleted status::" + taskCompleted);
			if (!taskCompleted) {
				String folderName = request.getParameter("folderName");
				if (!"Final Report".equalsIgnoreCase(folderName)) {
					viewForJSON.addObject("key", "Not Permitted");
					return viewForJSON;
				} else {
					newMarkedFileInfo = "File Name:" + docId.split(">")[1].split("<")[0];
					newMarkedFileInfo = newMarkedFileInfo + ", File Version:" + version;
					newMarkedFileInfo = newMarkedFileInfo + ", File Size:" + fileSize;
					previousMarkedFileInfo = "File Name:" + markedFileStatus.split(">")[1].split("<")[0];
					previousMarkedFileInfo = previousMarkedFileInfo + ", File Version:"
							+ markedFileStatus.split("::")[1];
					previousMarkedFileInfo = previousMarkedFileInfo + ", File Size:" + markedFileStatus.split("::")[2];
					this.logger.debug("###markRevisedDocumentAsFinal PID::" + pid + "##CRN::" + crn + "##CaseStatus::"
							+ caseStatus);
					this.logger.debug("Previous marked file info::" + previousMarkedFileInfo + "New Marked File Info"
							+ newMarkedFileInfo);
					docId = docId.split("=")[2].split("'")[0];
					this.logger.debug("docId is " + docId);
					resourceLocator.getDocService().updateStatus(pid, docId, SBMUtils.getSession(request));
					CaseHistory caseHistory = new CaseHistory();
					caseHistory.setCRN(crn);
					caseHistory.setProcessCycle("Final");
					if (crn != null && !crn.isEmpty()) {
						caseHistory.setPid(pid);
						caseHistory.setTaskName("Client Submission Task");
						caseHistory.setTaskStatus("Approved");
						caseHistory.setPerformer(SBMUtils.getSession(request).getUser());
						caseHistory.setAction("Marked Final Report");
						caseHistory.setOldInfo(previousMarkedFileInfo);
						caseHistory.setNewInfo(newMarkedFileInfo);
						this.logger.debug("CRN:" + caseHistory.getCRN());
						this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
						this.logger.debug("PID:" + caseHistory.getPid());
						this.logger.debug("Performer:" + caseHistory.getPerformer());
						this.logger.debug("Action:" + caseHistory.getAction());
						this.logger.debug("Old Info:" + caseHistory.getOldInfo());
						this.logger.debug("New Info:" + caseHistory.getNewInfo());
						ResourceLocator.self().getCaseHistoryService()
								.setCaseHistoryForUploadFinalDocument(caseHistory);
					}

					if (resourceLocator.getTaskService().getISISStatus(crn) == 1) {
						ClientCaseStatusVO clientCaseStatusVO = resourceLocator.getTaskService()
								.getFileForISIS(Long.parseLong(pid));
						this.logger.debug("file name and version::" + clientCaseStatusVO.getFileName() + "::"
								+ clientCaseStatusVO.getVersion());
						clientCaseStatusVO.setCRN(crn);
						clientCaseStatusVO.setExpressCase(
								(Boolean) resourceLocator.getSBMService().getDataslotValue(Long.parseLong(pid),
										"ExpressCase", SBMUtils.getSession(request)) ? "True" : "False");
						clientCaseStatusVO.setStatus("CMP");
						clientCaseStatusVO.setUpdateType("Case");
						ClientCaseStatusIndustryVO[] industryArray = ResourceLocator.self().getSubjectService()
								.getSubjectsIndustryForISIS(crn);
						ClientCaseStatusRiskVO[] riskArray = ResourceLocator.self().getSubjectService()
								.getSubjectsRisksForISIS(crn);
						clientCaseStatusVO.setSubjectIndustry(industryArray);
						clientCaseStatusVO.setSubjectRisk(riskArray);
						boolean flag = resourceLocator.getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
						this.logger.debug("##Flag after mark new document!!!" + flag);
						viewForJSON.addObject("key", "ISISCase");
					} else {
						viewForJSON.addObject("key", "AtlasCase");
					}

					return viewForJSON;
				}
			} else {
				viewForJSON.addObject("key", "TATOverdue");
				return viewForJSON;
			}
		} catch (NumberFormatException var22) {
			return AtlasUtils.getExceptionView(this.logger, var22);
		} catch (CMSException var23) {
			return AtlasUtils.getExceptionView(this.logger, var23);
		}
	}

	public ModelAndView checkTATOverdue(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String activityName = request.getParameter("activity");
		String pid = activityName.split("::")[0].split("#")[1];
		String caseStatus = request.getParameter("caseStatus");
		boolean taskCompleted = false;

		try {
			if (caseStatus.equals("Completed")) {
				taskCompleted = ResourceLocator.self().getSBMService().isTaskCompleted(Long.valueOf(pid),
						SBMUtils.getSession(request));
			}

			this.logger.debug("taskCompleted status::" + taskCompleted);
			if (taskCompleted) {
				viewForJSON.addObject("key", "TATOverdue");
				return viewForJSON;
			} else {
				viewForJSON.addObject("key", "");
				return viewForJSON;
			}
		} catch (NumberFormatException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getTimeStampOfCCS(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String activityName = request.getParameter("activity");
		String pid = activityName.split("::")[0].split("#")[1];

		try {
			long timeStamp = 0L;
			timeStamp = ResourceLocator.self().getCaseHistoryService().getTimeStampOfCCS(pid);
			viewForJSON.addObject("key", timeStamp);
			return viewForJSON;
		} catch (NumberFormatException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView deleteDocuments(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			String message = "delete Documents Method of Document Action";
			this.logger.debug(message);
			ResourceLocator locator = ResourceLocator.self();
			String activityName = request.getParameter("activity");
			String pid = activityName.split("::")[0].split("#")[1];
			String crn = request.getParameter("crn");
			String caseStatus = request.getParameter("caseStatus");
			this.logger.debug("###deleteDocuments PID::" + pid + "##CRN::" + crn + "##Case Status::" + caseStatus
					+ "##Folder Names::" + request.getParameter("folderNames").toString());
			String[] tokens = request.getParameter("docIds").split(",");
			String[] folderNameTokens = request.getParameter("folderNames").split(",");
			String docIds = "";

			int i;
			for (i = 0; i < tokens.length; ++i) {
				if ("".equalsIgnoreCase(docIds)) {
					docIds = "'" + tokens[i] + "'";
				} else {
					docIds = docIds + ",'" + tokens[i] + "'";
				}
			}

			locator.getDocService().delDocument(docIds);

			for (i = 0; i < folderNameTokens.length; ++i) {
				if (folderNameTokens[i].split("::")[0].equals("Final Report")) {
					CaseHistory caseHistory = new CaseHistory();
					caseHistory.setCRN(crn);
					caseHistory.setProcessCycle("Final");
					if (crn != null && !crn.isEmpty()) {
						caseHistory.setPid(pid);
						caseHistory.setTaskName("Client Submission Task");
						if (caseStatus.equals("Completed Client Submission")) {
							caseHistory.setTaskStatus("Approved");
						} else {
							caseHistory.setTaskStatus("In Progress");
						}

						caseHistory.setPerformer(SBMUtils.getSession(request).getUser());
						caseHistory.setAction("Delete File");
						caseHistory.setOldInfo("");
						caseHistory.setNewInfo("File Name:" + folderNameTokens[i].split("::")[1] + ", File Version:"
								+ folderNameTokens[i].split("::")[2] + ", File Size:"
								+ folderNameTokens[i].split("::")[3]);
						this.logger.debug("CRN:" + caseHistory.getCRN());
						this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
						this.logger.debug("PID:" + caseHistory.getPid());
						this.logger.debug("Performer:" + caseHistory.getPerformer());
						this.logger.debug("Action:" + caseHistory.getAction());
						this.logger.debug("Old Info:" + caseHistory.getOldInfo());
						this.logger.debug("New Info:" + caseHistory.getNewInfo());
						ResourceLocator.self().getCaseHistoryService()
								.setCaseHistoryForUploadFinalDocument(caseHistory);
					}
				}
			}

			return viewForJSON;
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}
	}

	public ModelAndView getFolderNames(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");

		try {
			this.logger.debug("inside get folder names method");
			ResourceLocator locator = ResourceLocator.self();
			String displayMode = request.getParameter("displayMode");
			String userId = request.getParameter("userId");
			this.logger.debug("mode is " + displayMode);
			viewForJSON.addObject("listOfFolderNames",
					JsonBeanUtil.toJsonBean(locator.getDocService().getDocumentFolderNames(displayMode, userId)));
			return viewForJSON;
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView isDocumentMarked(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String activityName = request.getParameter("activity");
		String pid = activityName.split("::")[0].split("#")[1];

		try {
			this.logger.debug("inside isDocumentMarked method");
			ResourceLocator locator = ResourceLocator.self();
			viewForJSON.addObject("isDocumentMarked",
					locator.getDocService().isClientSubmissionPermitted(pid, SBMUtils.getSession(request)));
		} catch (NullPointerException var7) {
			this.logger.debug("No doument found returning failure");
			viewForJSON.addObject("success", false);
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		return viewForJSON;
	}

	public ModelAndView downloadDocuments(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in downloadDocuments");
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		ResourceLocator locator = ResourceLocator.self();

		try {
			String docIds = request.getParameter("docIds");
			String crn = request.getParameter("crn");
			this.logger.debug("crn is : " + crn);
			List<Map<String, Object>> docList = locator.getDocService().downloadDocument(docIds);
			List<File> listOfFiles = new ArrayList();
			SubjectDetails subjectDetails = locator.getSubjectService().getPrimarySubjectDetailsForCase(crn);
			String zipFileName = subjectDetails.getSubjectName().replaceAll("[\\/:*?\\\\\"<>|]", "");
			String pid = "";
			String attachmentsRootDir = propertyReader.getAttachmentsRootDirPath();
			String attachmentDownloadCopyLoc = propertyReader.getAttachmentDownloadCopyPath();
			String tempZipFileLoc = propertyReader.getTempZipFilePath();
			Iterator var16 = docList.iterator();

			while (var16.hasNext()) {
				Map<String, Object> doc = (Map) var16.next();
				String fileLoc = doc.get("physicalLocation").toString().replaceAll("\\\\", "/");
				String docName = (String) doc.get("docName");
				String fileName = attachmentsRootDir + fileLoc;
				this.logger.debug("The fileName is :: " + fileName);
				String[] temp = doc.get("path").toString().split("\\/");
				String folderName = temp[3];
				pid = doc.get("pid").toString();
				this.logger.debug("pid is " + pid);
				String version = doc.get("version").toString();
				this.logger.debug("version is " + version);
				File file = new File(fileName);
				String nameWithoutType = docName.substring(0, docName.lastIndexOf(46));
				String fileType = docName.substring(docName.lastIndexOf(46), docName.length());
				File fileCopy = new File(attachmentDownloadCopyLoc + pid + "/" + folderName + "/" + nameWithoutType
						+ "_v" + version + fileType);
				this.logger.debug("Name of document is " + fileCopy.getName());
				this.logger.debug("Location of document is " + fileCopy.getAbsolutePath());
				locator.getDocService().copyWithStreams(file, fileCopy, false);
				listOfFiles.add(fileCopy);
			}

			String zipFilePath = tempZipFileLoc + zipFileName + ".zip";
			String zipFile = locator.getDocService().generateZipFile(listOfFiles, zipFilePath);
			this.logger.debug("Name of generated Zip file is :: " + zipFile);

			for (int i = 0; i < listOfFiles.size(); ++i) {
				((File) listOfFiles.get(i)).delete();
			}

			File rootDir = new File(attachmentDownloadCopyLoc + pid);
			File[] dirList = rootDir.listFiles();
			File[] var29 = dirList;
			int var28 = dirList.length;

			for (int var36 = 0; var36 < var28; ++var36) {
				File dir = var29[var36];
				dir.delete();
			}

			rootDir.delete();
			viewForJSON.addObject("fileName", zipFile);
			viewForJSON.addObject("exactFileName", zipFileName);
			return viewForJSON;
		} catch (Exception var27) {
			return AtlasUtils.getExceptionView(this.logger, var27);
		}
	}

	public ModelAndView deleteDownloadedZipFile(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in deleteDownloadedZipFile");
		String zipFileName = "";
		ModelAndView jsonView = new ModelAndView("jsonView");
		zipFileName = request.getParameter("zipFileName");
		File file = new File(zipFileName);
		if (file.exists()) {
			file.delete();
		}

		return jsonView;
	}
}