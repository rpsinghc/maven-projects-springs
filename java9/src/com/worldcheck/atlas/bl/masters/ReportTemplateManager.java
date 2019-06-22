package com.worldcheck.atlas.bl.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IReportTemplateMaster;
import com.worldcheck.atlas.dao.masters.ReportTemplateDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTemplateMasterVO;
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

public class ReportTemplateManager implements IReportTemplateMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.ReportTemplateManager");
	private static final String GENERAL = "general";
	private static final String SELECTED_CODE_NAME_TO_UPLOAD = "selectedCodeNametoupload";
	private static final String REPORT_TYPE_TO_UPLOAD = "reportTypetoupload";
	private static final String FILE_NAME_TO_UPLOAD = "fileNametoupload";
	private static final String COMMENT_TO_UPLOAD = "commenttoupload";
	private static final String REPORT_TYPE_TO_DELETE = "reportTypetoDelete";
	private static final String FILE_NAME_TO_DELETE = "fileNametoDelete";
	private static final String COMMENT_TO_DELETE = "commenttoDelete";
	private static final String UPLOADABLEFILESTEMPPATH = "uploadableFilesTempPath";
	private String PATH;
	private static final String UPLOAD_TEMP = "reportTmplUploadTemp";
	private static final String UPLOADED_FOLDER = "reportTmplUploadedFiles";
	private static final String HISTORY_FOLDER = "reportTmplHistoryFiles";
	private static final String SESSION_UPLOAD = "sessionUploadFiles";
	private static final String SESSION_UPLOADED = "sessionUploadedFiles";
	private static final String SESSION_HISTORY = "sessionHistoryFiles";
	private static final String HYPEN = "-";
	private static final String COMMA = ",";
	private static final String rptDownIdtoHistory = "rptDownIdtoHistory";
	private static final String clientCodetoHistory = "clientCodetoHistory";
	private static final String reportTypetoHistory = "reportTypetoHistory";
	private static final String fileNametoHistory = "fileNametoHistory";
	private static final String BLANK = "";
	private static final String SEPERATOR = "@SEPERATOR@";
	private static final String ZERO = "0";
	private static final String ONE = "1";
	private static final String COMMENT = "comment";
	private static final String RPTCOMMENT = "rptComment";
	private static final String ID = "id";
	private static final String UPLOLADEDBY = "uploladedBy";
	private static final String GENERAL_1 = "General";
	private ReportTemplateDAO reportTemplateDAO = null;

	public ReportTemplateManager() throws CMSException {
		this.logger.debug("In ReportTemplateManager constructor");
		this.PATH = this.getproperties().getProperty("uploadableFilesTempPath");
	}

	public ReportTemplateDAO getReportTemplateDAO() {
		return this.reportTemplateDAO;
	}

	public void setReportTemplateDAO(ReportTemplateDAO reportTemplateDAO) {
		this.reportTemplateDAO = reportTemplateDAO;
	}

	public HashMap<String, ReportTemplateMasterVO> addReportTemplate(ReportTemplateMasterVO reportTemplateMasterVO,
			File inputFile, String userName) throws CMSException {
		this.logger.debug("In addReportTemplate method");

		try {
			String strRootFilePath = this.PATH + File.separator + "reportTmplUploadTemp";
			this.addFilesToTemp(reportTemplateMasterVO, strRootFilePath, userName, inputFile);
			HashMap<String, ReportTemplateMasterVO> uploadableFileHashMap = this
					.getAllFilesFromTemp(reportTemplateMasterVO, strRootFilePath, userName, inputFile);
			return uploadableFileHashMap;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void upLoadReportTemplate(ReportTemplateMasterVO reportTemplateMasterVO, HttpServletRequest request)
			throws CMSException {
		this.logger.debug("In upLoadReportTemplate method");
		HttpSession session = request.getSession();
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String userName = userBean.getUserName();
		String isGeneral = "0";
		if (request.getParameter("general") != null && !request.getParameter("general").equals("")) {
			isGeneral = request.getParameter("general");
		}

		this.logger.info("isGeneral" + isGeneral);
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
		this.logger.info(clientCodes.length + ":::::::::::::::" + reportTypes.length);

		for (int indexClient = 0; indexClient < clientCodes.length; ++indexClient) {
			for (int indexReport = 0; indexReport < reportTypes.length; ++indexReport) {
				ReportTemplateMasterVO rtdmvo = new ReportTemplateMasterVO();
				rtdmvo.setClientCode(clientCodes[indexClient]);
				rtdmvo.setClientName(codeNames[indexClient]);
				rtdmvo.setReportType(reportTypes[indexReport]);
				rtdmvo.setFileName(fileNames[indexReport]);
				rtdmvo.setComment(comments[indexReport]);
				rtdmvo.setUploladedBy(userName);
				rtdmvo.setIsGeneral(isGeneral);
				String key = clientCodes[indexClient] + reportTypes[indexReport] + fileNames[indexReport];
				new ReportTemplateMasterVO();
				HashMap<String, ReportTemplateMasterVO> sessionHashMap = null;
				ReportTemplateMasterVO rtdmvoInserted;
				int insertedId;
				if (session.getAttribute("sessionUploadedFiles") == null) {
					sessionHashMap = new HashMap();
					insertedId = this.reportTemplateDAO.insertReportTemplate(rtdmvo);
					rtdmvoInserted = this.reportTemplateDAO.getInsertedData(insertedId);
				} else {
					sessionHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
					if (sessionHashMap.containsKey(key)) {
						rtdmvo.setUploadDateAndTime(
								((ReportTemplateMasterVO) sessionHashMap.get(key)).getUploadDateAndTime());
						rtdmvo.setRptDownId(((ReportTemplateMasterVO) sessionHashMap.get(key)).getRptDownId());
						this.reportTemplateDAO.updateRecord(rtdmvo);
						rtdmvoInserted = this.reportTemplateDAO
								.getInsertedData(((ReportTemplateMasterVO) sessionHashMap.get(key)).getRptDownId());
					} else {
						insertedId = this.reportTemplateDAO.insertReportTemplate(rtdmvo);
						rtdmvoInserted = this.reportTemplateDAO.getInsertedData(insertedId);
					}
				}

				sessionHashMap.put(key, rtdmvoInserted);
				session.setAttribute("sessionUploadedFiles", sessionHashMap);
			}
		}

		this.uploadFiles(userName, clientCodes, reportTypes, fileNames, session, codeNames, isGeneral);
	}

	public void deleteReportTemplate(HttpServletRequest request, ReportTemplateMasterVO reportTemplateMasterVO)
			throws CMSException {
		this.logger.debug("IN deleteReportTemplate method");
		String[] reportTypes = request.getParameter("reportTypetoDelete").split(",");
		String[] fileNames = request.getParameter("fileNametoDelete").split(",");
		String[] comments = request.getParameter("commenttoDelete").split(",");

		for (int i = 0; i < reportTypes.length; ++i) {
			reportTemplateMasterVO.setReportType(reportTypes[i]);
			reportTemplateMasterVO.setFileName(fileNames[i]);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			this.deleteFromUploadableTemp(reportTemplateMasterVO, userName);
			HttpSession session = request.getSession();
			if (session.getAttribute("sessionUploadFiles") != null) {
				HashMap<String, ReportTemplateMasterVO> sessionHashMap = (HashMap) session
						.getAttribute("sessionUploadFiles");
				String key = reportTypes[i] + fileNames[i];
				sessionHashMap.remove(key);
				session.setAttribute("sessionUploadFiles", sessionHashMap);
			}
		}

	}

	public List<ReportTemplateMasterVO> searchReportTemplDown(ReportTemplateMasterVO reportTemplateMasterVOInput,
			int start, int limit, String sortColumnName, String sortType) throws CMSException {
		this.logger.debug("In searchReportTemplDown method");
		new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		new SimpleDateFormat("dd-MMM-yyyy");
		if (reportTemplateMasterVOInput.getUpdateStartDate() != null
				&& !reportTemplateMasterVOInput.getUpdateStartDate().equalsIgnoreCase("")) {
			;
		}

		if (reportTemplateMasterVOInput.getUpdateEndDate() != null
				&& !reportTemplateMasterVOInput.getUpdateEndDate().equalsIgnoreCase("")) {
			;
		}

		reportTemplateMasterVOInput.setStart(new Integer(start + 1));
		reportTemplateMasterVOInput.setLimit(new Integer(start + limit));
		if (sortColumnName.equalsIgnoreCase("comment")) {
			reportTemplateMasterVOInput.setSortColumnName("rptComment");
		} else {
			reportTemplateMasterVOInput.setSortColumnName(sortColumnName);
		}

		reportTemplateMasterVOInput.setSortType(sortType);
		reportTemplateMasterVOInput.setRptComment(reportTemplateMasterVOInput.getComment());
		return this.reportTemplateDAO.searchReportTemplDown(reportTemplateMasterVOInput);
	}

	public int searchReportTemplDownCount(ReportTemplateMasterVO reportTemplateMasterVOInput) throws CMSException {
		this.logger.debug("In searchReportTemplDownCount method");
		new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		new SimpleDateFormat("dd-MMM-yyyy");
		if (reportTemplateMasterVOInput.getUpdateStartDate() != null
				&& !reportTemplateMasterVOInput.getUpdateStartDate().equalsIgnoreCase("")) {
			;
		}

		if (reportTemplateMasterVOInput.getUpdateEndDate() != null
				&& !reportTemplateMasterVOInput.getUpdateEndDate().equalsIgnoreCase("")) {
			;
		}

		return this.reportTemplateDAO.searchReportTemplDownCount(reportTemplateMasterVOInput);
	}

	public void getHistoryReportTemplate(ReportTemplateMasterVO reportTemplateMasterVO) {
	}

	public void removeHistoryReportTemplate(ReportTemplateMasterVO reportTemplateMasterVO, HttpServletRequest request)
			throws CMSException {
		this.logger.debug("In removeHistoryReportTemplate method");
		HttpSession session = request.getSession();
		HashMap<String, ReportTemplateMasterVO> sessionuploadedHashMap = null;
		HashMap<String, ReportTemplateMasterVO> sessionHistoryHashMap = null;
		String[] reportDownIds = request.getParameter("rptDownIdtoHistory").split(",");
		String[] clientCodes = request.getParameter("clientCodetoHistory").split(",");
		String[] reportTypes = request.getParameter("reportTypetoHistory").split(",");
		String[] fileNames = request.getParameter("fileNametoHistory").split(",");

		for (int reptDownIdIndex = 0; reptDownIdIndex < reportDownIds.length; ++reptDownIdIndex) {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String uploladedBy = userBean.getUserName();
			HashMap<String, String> hmap = new HashMap();
			hmap.put("id", reportDownIds[reptDownIdIndex]);
			hmap.put("uploladedBy", uploladedBy);
			this.reportTemplateDAO.updateHistoryStatus(hmap);
			String key;
			if (session.getAttribute("sessionUploadedFiles") != null) {
				sessionuploadedHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
				key = clientCodes[reptDownIdIndex] + reportTypes[reptDownIdIndex] + fileNames[reptDownIdIndex];
				sessionuploadedHashMap.remove(key);
				session.setAttribute("sessionUploadedFiles", sessionuploadedHashMap);
			}

			if (session.getAttribute("sessionHistoryFiles") != null) {
				sessionHistoryHashMap = (HashMap) session.getAttribute("sessionHistoryFiles");
			} else {
				sessionHistoryHashMap = new HashMap();
			}

			key = clientCodes[reptDownIdIndex] + reportTypes[reptDownIdIndex] + fileNames[reptDownIdIndex];
			new ReportTemplateMasterVO();
			ReportTemplateMasterVO rtdmvoUpdated = this.reportTemplateDAO
					.getInsertedData(Integer.parseInt(reportDownIds[reptDownIdIndex]));
			sessionHistoryHashMap.put(key, rtdmvoUpdated);
			session.setAttribute("sessionHistoryFiles", sessionHistoryHashMap);
		}

	}

	private void addFilesToTemp(ReportTemplateMasterVO reportTemplateMasterVO, String strRootFilePath, String userName,
			File inputFile) throws Exception {
		this.logger.debug("In addFilesToTemp method");
		this.logger.info(inputFile.getAbsolutePath());

		try {
			String reportType = reportTemplateMasterVO.getReportType();
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
			FileOutputStream fos = null;
			fos = new FileOutputStream(targetFile);
			fos.write(this.getBytesFromFile(inputFile));
			fos.flush();
			fos.close();
		} catch (Exception var12) {
			throw var12;
		}
	}

	private HashMap<String, ReportTemplateMasterVO> getAllFilesFromTemp(ReportTemplateMasterVO reportTemplateMasterVO,
			String strRootFilePath, String userName, File inputFile) throws Exception {
		this.logger.debug("In getAllFilesFromTemp method");
		HashMap uploadableFileHashMap = new HashMap();

		try {
			String reportType = reportTemplateMasterVO.getReportType();
			String comment = reportTemplateMasterVO.getComment();
			File[] fileList = (new File(strRootFilePath + File.separator + userName + File.separator + reportType))
					.listFiles();
			File[] arr$ = fileList;
			int len$ = fileList.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				File file = arr$[i$];
				String fileName = file.getName();
				double filelength = (double) file.length() / 1024.0D;
				String fileSize = String.valueOf(filelength);
				reportTemplateMasterVO = new ReportTemplateMasterVO();
				if (fileName.equals(inputFile.getName())) {
					reportTemplateMasterVO.setReportType(reportType);
					reportTemplateMasterVO.setFileName(fileName);
					reportTemplateMasterVO.setFileSize(fileSize);
					reportTemplateMasterVO.setComment(comment);
					String key = reportType + fileName;
					uploadableFileHashMap.put(key, reportTemplateMasterVO);
				}
			}

			return uploadableFileHashMap;
		} catch (Exception var18) {
			throw var18;
		}
	}

	private void uploadFiles(String userName, String[] clientCodes, String[] reportTypes, String[] fileNames,
			HttpSession session, String[] codeNames, String isGeneral) throws CMSException {
		this.logger.debug("In uploadFiles method");
		String targetFilePath = this.PATH + File.separator + "reportTmplUploadedFiles";
		String sourceFilePath = this.PATH + File.separator + "reportTmplUploadTemp";
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
						FileOutputStream fos = null;

						try {
							fos = new FileOutputStream(targetFile);
						} catch (FileNotFoundException var28) {
							throw new CMSException(this.logger, var28);
						}

						try {
							fos.write(this.getBytesFromFile(sourceFile));
						} catch (IOException var27) {
							throw new CMSException(this.logger, var27);
						}

						try {
							fos.flush();
						} catch (IOException var26) {
							throw new CMSException(this.logger, var26);
						}

						try {
							fos.close();
						} catch (IOException var25) {
							throw new CMSException(this.logger, var25);
						}
					}

					sourceFile.delete();
					if (session.getAttribute("sessionUploadFiles") != null) {
						HashMap<String, ReportTemplateMasterVO> sessionUploadableFilesHashMap = (HashMap) session
								.getAttribute("sessionUploadFiles");
						String key = sourceReportFile.getName() + sourceFile.getName();
						sessionUploadableFilesHashMap.remove(key);
						session.setAttribute("sessionUploadFiles", sessionUploadableFilesHashMap);
					}
				}
			}
		}

	}

	private void deleteFromUploadableTemp(ReportTemplateMasterVO reportTemplateMasterVO, String userName)
			throws CMSException {
		this.logger.debug("In deleteFromUploadableTemp method");
		String reportType = reportTemplateMasterVO.getReportType();
		String fileName = reportTemplateMasterVO.getFileName();
		String path = this.PATH + File.separator + "reportTmplUploadTemp" + File.separator + userName + File.separator
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

	private void removeToHistory(ReportTemplateMasterVO reportTemplateMasterVO) throws CMSException {
		this.logger.debug(" In removeToHistory method");
		String targetFilePath = this.PATH + File.separator + "reportTmplHistoryFiles";
		String[] clientCode = reportTemplateMasterVO.getClientCode().split(",");
		String[] reportType = reportTemplateMasterVO.getReportType().split(",");
		String[] fileName = reportTemplateMasterVO.getFileName().split(",");

		for (int i = 0; i < clientCode.length; ++i) {
			String sourcepath = this.PATH + File.separator + "reportTmplUploadedFiles" + File.separator + clientCode[i]
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

					FileOutputStream fos;
					try {
						fos = new FileOutputStream(targetFile);
					} catch (FileNotFoundException var22) {
						throw new CMSException(this.logger, var22);
					}

					try {
						fos.write(this.getBytesFromFile(sourceFile));
					} catch (IOException var21) {
						throw new CMSException(this.logger, var21);
					}

					try {
						fos.flush();
					} catch (IOException var20) {
						throw new CMSException(this.logger, var20);
					}

					try {
						fos.close();
					} catch (IOException var19) {
						throw new CMSException(this.logger, var19);
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
		} catch (IOException var11) {
			throw new CMSException(this.logger, var11);
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
		this.logger.debug("In  getproperties method");
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
		this.logger.debug("In  deleteUserFiles method");
		String strRootFilePath = this.PATH + File.separator + "reportTmplUploadTemp";
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
		this.logger.debug(" In getGeneralClients method");
		return this.reportTemplateDAO.getGeneralClientList();
	}

	public List<ReportTemplateMasterVO> getFilesForCaseDetail(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetail");
		return this.reportTemplateDAO.getFilesForCaseDetail(hmap);
	}

	public int getFilesForCaseDetailCount(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetailCount");
		return this.reportTemplateDAO.getFilesForCaseDetailCount(hmap);
	}
}