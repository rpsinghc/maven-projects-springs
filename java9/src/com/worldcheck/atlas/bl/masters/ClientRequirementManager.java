package com.worldcheck.atlas.bl.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IClientRequirement;
import com.worldcheck.atlas.dao.masters.ClientRequirementDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ClientRequirementManager implements IClientRequirement {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.ClientRequirementManager");
	private String PATH;
	private static final String UPLOAD_TEMP = "clientReqUploadTemp";
	private static final String UPLOADED_FOLDER = "clientReqUploadedFiles";
	private static final String HISTORY_FOLDER = "clientReqHistoryFiles";
	private static final String SESSION_UPLOAD = "sessionUploadFiles";
	private static final String SESSION_UPLOADED = "sessionUploadedFiles";
	private static final String SESSION_HISTORY = "sessionHistoryFiles";
	private static final String HYPEN = "-";
	private static final String COMMA = ",";
	private static final String general = "general";
	private static final String selectedCodeNametoupload = "selectedCodeNametoupload";
	private static final String reportTypetoupload = "reportTypetoupload";
	private static final String fileNametoupload = "fileNametoupload";
	private static final String commenttoupload = "commenttoupload";
	private static final String instructiontoupload = "instructiontoupload";
	private static final String reportTypetoDelete = "reportTypetoDelete";
	private static final String fileNametoDelete = "fileNametoDelete";
	private static final String commenttoDelete = "commenttoDelete";
	private static final String instructiontoDelete = "instructiontoDelete";
	private static final String clientReqIdtoHistory = "clientReqIdtoHistory";
	private static final String clientCodetoHistory = "clientCodetoHistory";
	private static final String reportTypetoHistory = "reportTypetoHistory";
	private static final String fileNametoHistory = "fileNametoHistory";
	private static final String BLANK = "";
	private static final String SEPERATOR = "@SEPERATOR@";
	private static final String COMMENT = "comment";
	private static final String CLIENTREQCOMMENT = "clientReqComment";
	private static final String ZERO = "0";
	private static final String ID = "id";
	private static final String UPLOLADEDBY = "uploladedBy";
	private static final String GENERAL = "General";
	ClientRequirementDAO clientRequirementDAO = null;

	public ClientRequirementManager() throws CMSException {
		this.logger.debug("In ClientRequirementManager()");
		this.PATH = this.getproperties().getProperty("uploadableFilesTempPath");
	}

	public ClientRequirementDAO getClientRequirementDAO() {
		return this.clientRequirementDAO;
	}

	public void setClientRequirementDAO(ClientRequirementDAO clientRequirementDAO) {
		this.clientRequirementDAO = clientRequirementDAO;
	}

	public List<ClientRequirmentMasterVO> searchClientReq(ClientRequirmentMasterVO clientRequirmentMasterVOInput,
			int start, int limit, String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("In searchClientReq method");
		new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		new SimpleDateFormat("dd-MMM-yyyy");
		if (clientRequirmentMasterVOInput.getUpdateStartDate() != null
				&& !clientRequirmentMasterVOInput.getUpdateStartDate().equalsIgnoreCase("")) {
			;
		}

		if (clientRequirmentMasterVOInput.getUpdateEndDate() != null
				&& !clientRequirmentMasterVOInput.getUpdateEndDate().equalsIgnoreCase("")) {
			;
		}

		clientRequirmentMasterVOInput.setStart(new Integer(start + 1));
		clientRequirmentMasterVOInput.setLimit(new Integer(start + limit));
		if (sortColumnName.equalsIgnoreCase("comment")) {
			clientRequirmentMasterVOInput.setSortColumnName("clientReqComment");
		} else {
			clientRequirmentMasterVOInput.setSortColumnName(sortColumnName);
		}

		clientRequirmentMasterVOInput.setSortType(sortType);
		clientRequirmentMasterVOInput.setClientReqComment(clientRequirmentMasterVOInput.getComment());
		return this.clientRequirementDAO.searchClientReq(clientRequirmentMasterVOInput);
	}

	public int searchClientReqCount(ClientRequirmentMasterVO clientRequirmentMasterVOInput) throws CMSException {
		this.logger.debug("In searchClientReqCount method");
		new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		new SimpleDateFormat("dd-MMM-yyyy");
		if (clientRequirmentMasterVOInput.getUpdateStartDate() != null
				&& !clientRequirmentMasterVOInput.getUpdateStartDate().equalsIgnoreCase("")) {
			;
		}

		if (clientRequirmentMasterVOInput.getUpdateEndDate() != null
				&& !clientRequirmentMasterVOInput.getUpdateEndDate().equalsIgnoreCase("")) {
			;
		}

		return this.clientRequirementDAO.searchClientReqCount(clientRequirmentMasterVOInput);
	}

	public HashMap<String, ClientRequirmentMasterVO> addClientReq(ClientRequirmentMasterVO clientRequirmentMasterVO,
			File inputFile, String userName) throws CMSException {
		this.logger.debug("In addClientReq method");
		String strRootFilePath = this.PATH + File.separator + "clientReqUploadTemp";

		try {
			this.addFilesToTemp(clientRequirmentMasterVO, strRootFilePath, userName, inputFile);
			HashMap<String, ClientRequirmentMasterVO> uploadableFileHashMap = this
					.getAllFilesFromTemp(clientRequirmentMasterVO, strRootFilePath, userName, inputFile);
			return uploadableFileHashMap;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public void upLoadClientReq(ClientRequirmentMasterVO clientRequirmentMasterVO, HttpServletRequest request)
			throws CMSException {
		this.logger.debug("In upLoadClientReq method");
		HttpSession session = request.getSession();
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String userName = userBean.getUserName();
		String isGeneral = "0";
		if (request.getParameter("general") != null && !request.getParameter("general").equals("")) {
			isGeneral = request.getParameter("general");
		}

		List<ClientMasterVO> generalClient = null;
		String[] tempCodeNames;
		if (isGeneral.equalsIgnoreCase("1")) {
			generalClient = this.getGeneralClients();
			tempCodeNames = new String[generalClient.size()];
			Iterator<ClientMasterVO> itr = generalClient.iterator();

			for (int var10 = 0; itr.hasNext(); tempCodeNames[var10++] = ((ClientMasterVO) itr.next()).getCodeName()) {
				;
			}
		} else {
			tempCodeNames = request.getParameter("selectedCodeNametoupload").split("@SEPERATOR@");
		}

		String[] clientCodes = new String[tempCodeNames.length];
		String[] codeNames = new String[tempCodeNames.length];

		for (int i = 0; i < tempCodeNames.length; ++i) {
			clientCodes[i] = tempCodeNames[i].split("-")[0];
			codeNames[i] = tempCodeNames[i].split("-")[1];
		}

		String[] reportTypes = request.getParameter("reportTypetoupload").split(",");
		String[] fileNames = request.getParameter("fileNametoupload").split(",");
		String[] comments = request.getParameter("commenttoupload").split("@SEPERATOR@");
		String[] instructions = request.getParameter("instructiontoupload").split("@SEPERATOR@");
		this.logger.info(clientCodes.length + ":::::::::::::::" + reportTypes.length);

		for (int indexClient = 0; indexClient < clientCodes.length; ++indexClient) {
			for (int indexReport = 0; indexReport < reportTypes.length; ++indexReport) {
				ClientRequirmentMasterVO crmvo = new ClientRequirmentMasterVO();
				crmvo.setClientCode(clientCodes[indexClient]);
				crmvo.setClientName(codeNames[indexClient]);
				crmvo.setReportType(reportTypes[indexReport]);
				crmvo.setUploadedFile(fileNames[indexReport]);
				crmvo.setComment(comments[indexReport]);
				crmvo.setInstruction(instructions[indexReport]);
				crmvo.setUploladedBy(userName);
				crmvo.setIsGeneral(isGeneral);
				String key = clientCodes[indexClient] + reportTypes[indexReport] + fileNames[indexReport];
				new ClientRequirmentMasterVO();
				HashMap<String, ClientRequirmentMasterVO> sessionHashMap = null;
				ClientRequirmentMasterVO crmvoInserted;
				int insertedId;
				if (session.getAttribute("sessionUploadedFiles") == null) {
					sessionHashMap = new HashMap();
					insertedId = this.clientRequirementDAO.insertClientReq(crmvo);
					crmvoInserted = this.clientRequirementDAO.getInsertedData(insertedId);
				} else {
					sessionHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
					if (sessionHashMap.containsKey(key)) {
						crmvo.setUploadedDateAndTime(
								((ClientRequirmentMasterVO) sessionHashMap.get(key)).getUploadedDateAndTime());
						crmvo.setClientReqId(((ClientRequirmentMasterVO) sessionHashMap.get(key)).getClientReqId());
						this.clientRequirementDAO.updateRecord(crmvo);
						crmvoInserted = this.clientRequirementDAO
								.getInsertedData(((ClientRequirmentMasterVO) sessionHashMap.get(key)).getClientReqId());
					} else {
						insertedId = this.clientRequirementDAO.insertClientReq(crmvo);
						crmvoInserted = this.clientRequirementDAO.getInsertedData(insertedId);
					}
				}

				sessionHashMap.put(key, crmvoInserted);
				session.setAttribute("sessionUploadedFiles", sessionHashMap);
			}
		}

		this.uploadFiles(userName, clientCodes, reportTypes, fileNames, session, codeNames, isGeneral);
	}

	public void deleteClientReq(HttpServletRequest request, ClientRequirmentMasterVO clientRequirmentMasterVO)
			throws CMSException {
		this.logger.debug("In deleteClientReq METHOD");
		String[] reportTypes = request.getParameter("reportTypetoDelete").split(",");
		String[] fileNames = request.getParameter("fileNametoDelete").split(",");
		String[] comments = request.getParameter("commenttoDelete").split(",");
		String[] instructions = request.getParameter("instructiontoDelete").split(",");

		for (int i = 0; i < reportTypes.length; ++i) {
			clientRequirmentMasterVO.setReportType(reportTypes[i]);
			clientRequirmentMasterVO.setUploadedFile(fileNames[i]);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			this.deleteFromUploadableTemp(clientRequirmentMasterVO, userName);
			HttpSession session = request.getSession();
			if (session.getAttribute("sessionUploadFiles") != null) {
				HashMap<String, ClientRequirmentMasterVO> sessionHashMap = (HashMap) session
						.getAttribute("sessionUploadFiles");
				String key = reportTypes[i] + fileNames[i];
				sessionHashMap.remove(key);
				session.setAttribute("sessionUploadFiles", sessionHashMap);
			}
		}

	}

	public void getHistoryClientReq(ClientRequirmentMasterVO ClientRequirmentMasterVO) throws CMSException {
		this.logger.debug("In getHistoryClientReq method");
	}

	public void removeHistoryClientReq(ClientRequirmentMasterVO clientRequirmentMasterVO, HttpServletRequest request)
			throws CMSException {
		this.logger.debug("In removeHistoryClientReq method)");
		HttpSession session = request.getSession();
		HashMap<String, ClientRequirmentMasterVO> sessionuploadedHashMap = null;
		HashMap<String, ClientRequirmentMasterVO> sessionHistoryHashMap = null;
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String uploladedBy = userBean.getUserName();
		String[] clientReqIds = request.getParameter("clientReqIdtoHistory").split(",");
		String[] clientCodes = request.getParameter("clientCodetoHistory").split(",");
		String[] reportTypes = request.getParameter("reportTypetoHistory").split(",");
		String[] fileNames = request.getParameter("fileNametoHistory").split(",");

		for (int clientReqIdIndex = 0; clientReqIdIndex < clientReqIds.length; ++clientReqIdIndex) {
			HashMap<String, String> hmap = new HashMap();
			hmap.put("id", clientReqIds[clientReqIdIndex]);
			hmap.put("uploladedBy", uploladedBy);
			this.clientRequirementDAO.updateHistoryStatus(hmap);
			String key;
			if (session.getAttribute("sessionUploadedFiles") != null) {
				sessionuploadedHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
				key = clientCodes[clientReqIdIndex] + reportTypes[clientReqIdIndex] + fileNames[clientReqIdIndex];
				sessionuploadedHashMap.remove(key);
				session.setAttribute("sessionUploadedFiles", sessionuploadedHashMap);
			}

			if (session.getAttribute("sessionHistoryFiles") != null) {
				sessionHistoryHashMap = (HashMap) session.getAttribute("sessionHistoryFiles");
			} else {
				sessionHistoryHashMap = new HashMap();
			}

			key = clientCodes[clientReqIdIndex] + reportTypes[clientReqIdIndex] + fileNames[clientReqIdIndex];
			new ClientRequirmentMasterVO();
			ClientRequirmentMasterVO crmvoUpdated = this.clientRequirementDAO
					.getInsertedData(Integer.parseInt(clientReqIds[clientReqIdIndex]));
			sessionHistoryHashMap.put(key, crmvoUpdated);
			session.setAttribute("sessionHistoryFiles", sessionHistoryHashMap);
		}

	}

	private void addFilesToTemp(ClientRequirmentMasterVO clientRequirmentMasterVO, String strRootFilePath,
			String userName, File inputFile) throws Exception {
		this.logger.debug("In addFilesToTemp method");

		try {
			String reportType = clientRequirmentMasterVO.getReportType();
			String clientReportUploadingFileName = inputFile.getName();
			File dirfile = new File(strRootFilePath);
			if (!dirfile.exists()) {
				dirfile.mkdir();
			}

			File userFolder = new File(dirfile.getAbsolutePath() + File.separator + userName);
			if (!userFolder.exists()) {
				userFolder.mkdir();
			}

			File ReportTypeFolder = new File(userFolder.getAbsolutePath() + File.separator + reportType);
			if (!ReportTypeFolder.exists()) {
				ReportTypeFolder.mkdir();
			}

			File targetFile = new File(
					ReportTypeFolder.getAbsolutePath() + File.separator + clientReportUploadingFileName);
			FileOutputStream fos = new FileOutputStream(targetFile);
			fos.write(this.getBytesFromFile(inputFile));
			fos.flush();
			fos.close();
		} catch (Exception var12) {
			throw var12;
		}
	}

	private HashMap<String, ClientRequirmentMasterVO> getAllFilesFromTemp(
			ClientRequirmentMasterVO clientRequirmentMasterVO, String strRootFilePath, String userName, File inputFile)
			throws Exception {
		this.logger.debug("In getAllFilesFromTemp method");
		HashMap uploadableFileHashMap = new HashMap();

		try {
			String reportType = clientRequirmentMasterVO.getReportType();
			String comment = clientRequirmentMasterVO.getComment();
			String instruction = clientRequirmentMasterVO.getInstruction();
			File[] fileList = (new File(strRootFilePath + File.separator + userName + File.separator + reportType))
					.listFiles();
			File[] arr$ = fileList;
			int len$ = fileList.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				File file = arr$[i$];
				String fileName = file.getName();
				double filelength = (double) file.length() / 1024.0D;
				String fileSize = String.valueOf(filelength);
				clientRequirmentMasterVO = new ClientRequirmentMasterVO();
				if (fileName.equals(inputFile.getName())) {
					clientRequirmentMasterVO.setReportType(reportType);
					clientRequirmentMasterVO.setUploadedFile(fileName);
					clientRequirmentMasterVO.setFileSize(fileSize);
					clientRequirmentMasterVO.setComment(comment);
					clientRequirmentMasterVO.setInstruction(instruction);
					String key = reportType + fileName;
					uploadableFileHashMap.put(key, clientRequirmentMasterVO);
				}
			}

			return uploadableFileHashMap;
		} catch (Exception var19) {
			throw var19;
		}
	}

	private void uploadFiles(String userName, String[] clientCodes, String[] reportTypes, String[] fileNames,
			HttpSession session, String[] codeNames, String isGeneral) throws CMSException {
		this.logger.debug("In uploadFiles method");
		String targetFilePath = this.PATH + File.separator + "clientReqUploadedFiles";
		String sourceFilePath = this.PATH + File.separator + "clientReqUploadTemp";
		File targetFolder = new File(targetFilePath);
		if (!targetFolder.exists()) {
			targetFolder.mkdir();
		}

		File targetUserFolder = new File(targetFolder.getAbsolutePath() + File.separator + userName);
		if (!targetUserFolder.exists()) {
			targetUserFolder.mkdir();
		}

		File[] sourceReportFiles = (new File(sourceFilePath + File.separator + userName)).listFiles();
		File[] arr$ = sourceReportFiles;
		int len$ = sourceReportFiles.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			File sourceReportFile = arr$[i$];

			for (int rt = 0; rt < reportTypes.length; ++rt) {
				if (sourceReportFile.getName().equals(reportTypes[rt])) {
					File sourceFile = new File(sourceReportFile.getAbsolutePath() + File.separator + fileNames[rt]);
					if (isGeneral.equalsIgnoreCase("1")) {
						clientCodes = new String[]{"General"};
					}

					for (int cc = 0; cc < clientCodes.length; ++cc) {
						File targetClientFolder = new File(
								targetUserFolder.getAbsolutePath() + File.separator + clientCodes[cc].trim());
						if (!targetClientFolder.exists()) {
							targetClientFolder.mkdir();
						}

						File targetReportFolder = new File(
								targetClientFolder.getAbsolutePath() + File.separator + reportTypes[rt]);
						if (!targetReportFolder.exists()) {
							targetReportFolder.mkdir();
						}

						File targetFile = new File(
								targetReportFolder.getAbsolutePath() + File.separator + fileNames[rt]);

						try {
							FileOutputStream fos = new FileOutputStream(targetFile);
							fos.write(this.getBytesFromFile(sourceFile));
							fos.flush();
							fos.close();
						} catch (IOException var24) {
							throw new CMSException(this.logger, var24);
						}
					}

					sourceFile.delete();
					if (session.getAttribute("sessionUploadFiles") != null) {
						HashMap<String, ClientRequirmentMasterVO> sessionUploadableFilesHashMap = (HashMap) session
								.getAttribute("sessionUploadFiles");
						String key = sourceReportFile.getName() + sourceFile.getName();
						sessionUploadableFilesHashMap.remove(key);
						session.setAttribute("sessionUploadFiles", sessionUploadableFilesHashMap);
					}
				}
			}
		}

	}

	private void deleteFromUploadableTemp(ClientRequirmentMasterVO clientRequirmentMasterVO, String userName)
			throws CMSException {
		this.logger.debug("In deleteFromUploadableTemp method");
		String reportType = clientRequirmentMasterVO.getReportType();
		String fileName = clientRequirmentMasterVO.getUploadedFile();
		String path = this.PATH + File.separator + "clientReqUploadTemp" + File.separator + userName + File.separator
				+ reportType;
		File[] files = (new File(path)).listFiles();
		File[] arr$ = files;
		int len$ = files.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			File file = arr$[i$];
			if (file.getName().equalsIgnoreCase(fileName)) {
				file.delete();
			}
		}

	}

	private void removeToHistory(ClientRequirmentMasterVO clientRequirmentMasterVO) throws CMSException {
		this.logger.debug("In removeToHistory method");
		String targetFilePath = this.PATH + File.separator + "clientReqHistoryFiles";
		String[] clientCode = clientRequirmentMasterVO.getClientCode().split(",");
		String[] reportType = clientRequirmentMasterVO.getReportType().split(",");
		String[] fileName = clientRequirmentMasterVO.getUploadedFile().split(",");

		for (int i = 0; i < clientCode.length; ++i) {
			String sourcepath = this.PATH + File.separator + "clientReqUploadedFiles" + File.separator + clientCode[i]
					+ File.separator + reportType[i];
			File[] sourceFiles = (new File(sourcepath)).listFiles();
			File targetdirfile = new File(targetFilePath);
			if (!targetdirfile.exists()) {
				targetdirfile.mkdir();
			}

			File targetClientFolder = new File(targetdirfile.getAbsolutePath() + File.separator + clientCode[i]);
			if (!targetClientFolder.exists()) {
				targetClientFolder.mkdir();
			}

			File targetReportTypeFolder = new File(
					targetClientFolder.getAbsolutePath() + File.separator + reportType[i]);
			if (!targetReportTypeFolder.exists()) {
				targetReportTypeFolder.mkdir();
			}

			File[] arr$ = sourceFiles;
			int len$ = sourceFiles.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				File sourceFile = arr$[i$];
				if (sourceFile.getName().equalsIgnoreCase(fileName[i])) {
					File targetFile = new File(
							targetReportTypeFolder.getAbsolutePath() + File.separator + sourceFile.getName());

					try {
						FileOutputStream fos = new FileOutputStream(targetFile);
						fos.write(this.getBytesFromFile(sourceFile));
						fos.flush();
						fos.close();
					} catch (IOException var18) {
						throw new CMSException(this.logger, var18);
					}
				}
			}
		}

	}

	private byte[] getBytesFromFile(File file) throws CMSException {
		this.logger.debug("In getBytesFromFile method");
		FileInputStream is = null;

		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IOException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}

		long length = file.length();
		if (length > 2147483647L) {
			;
		}

		byte[] bytes = new byte[(int) length];
		int offset = 0;
		boolean var7 = false;

		int numRead;
		try {
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
		} catch (IOException var13) {
			throw new CMSException(this.logger, var13);
		}

		if (offset < bytes.length) {
			;
		}

		try {
			is.close();
			return bytes;
		} catch (IOException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public Properties getproperties() throws CMSException {
		this.logger.debug("In getproperties method");
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("atlas.properties");

		try {
			if (is != null) {
				props.load(is);
			}
		} catch (FileNotFoundException var4) {
			var4.printStackTrace();
		} catch (IOException var5) {
			var5.printStackTrace();
		}

		return props;
	}

	public void deleteUserFiles(String userName) throws CMSException {
		this.logger.debug("In deleteUserFiles method");
		String strRootFilePath = this.PATH + File.separator + "clientReqUploadTemp";
		File file = new File(strRootFilePath + File.separator + userName);
		this.deleteDirectory(file);
	}

	public void deleteDirectory(File path) throws CMSException {
		this.logger.debug("In deleteDirectory method");
		if (path.exists()) {
			File[] files = path.listFiles();

			for (int i = 0; i < files.length; ++i) {
				if (files[i].isDirectory()) {
					this.deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}

	}

	public List<ClientMasterVO> getGeneralClients() throws CMSException {
		this.logger.debug("In getGeneralClients method");
		return this.clientRequirementDAO.getGeneralClientList();
	}

	public List<ClientRequirmentMasterVO> getFilesForCaseDetail(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetail method");
		return this.clientRequirementDAO.getFilesForCaseDetail(hmap);
	}

	public int getFilesForCaseDetailCount(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetailCount method");
		return this.clientRequirementDAO.getFilesForCaseDetailCount(hmap);
	}
}