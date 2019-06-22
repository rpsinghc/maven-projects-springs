package com.worldcheck.atlas.isis.util;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.isis.dao.AtlasWebServiceDAO;
import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseFileDetailsVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

public class DownLoadBatchFiles implements Runnable {
	private WebServicePropertyReaderUtil webServicePropertyReaderUtil;
	private AtlasWebServiceDAO atlasWebServiceDAO;
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.util.DownLoadBatchFiles");
	private static ResourceBundle resources = ResourceBundle.getBundle("atlas");
	private static final ThreadPool pool;
	private String crn;
	private long piid;
	private CaseDetailsVO caseDetailsVO;
	private String ftpAddress;
	private String ftpUserName;
	private String ftpUserPwd;
	private String portNum;
	private String lcleDirPath;
	private LinkedList filesNotDwnld;
	private int maxDownLoadFileRetryCount;
	private long retryCountGapTime;
	int downLoadFileRetryCount = 0;

	static {
		pool = new ThreadPool(Integer.parseInt(resources.getString("atlas.webService.thread.pool.size")));
	}

	public void setWebServicePropertyReader(WebServicePropertyReaderUtil webServicePropertyReaderUtil) {
		this.webServicePropertyReaderUtil = webServicePropertyReaderUtil;
	}

	public void setAtlasWebServiceDAO(AtlasWebServiceDAO atlasWebServiceDAO) {
		this.atlasWebServiceDAO = atlasWebServiceDAO;
	}

	public void setProperties(CaseDetailsVO caseDetailsVO, long piid) {
		this.logger.debug("Inside setProperties of DownLoadBatchFiles class..");
		this.crn = caseDetailsVO.getCrn();
		this.caseDetailsVO = caseDetailsVO;
		this.piid = piid;
		this.ftpAddress = this.webServicePropertyReaderUtil.getFtpServerAddress();
		this.ftpUserName = this.webServicePropertyReaderUtil.getFtpServerUserId();
		this.ftpUserPwd = this.webServicePropertyReaderUtil.getFtpServerPassword();
		this.portNum = this.webServicePropertyReaderUtil.getFtpServerPortNo();
		this.lcleDirPath = this.webServicePropertyReaderUtil.getAtlasTempFilesFolder() + "\\"
				+ this.crn.replace('\\', '-') + "\\";
		this.maxDownLoadFileRetryCount = this.webServicePropertyReaderUtil.getFtpFailedFilesRetryCount();
		this.retryCountGapTime = this.webServicePropertyReaderUtil.getFtpFailedFilesRetryGapTime();
		this.logger.debug("ftpAddress:::::" + this.ftpAddress);
		this.logger.debug("ftpUserName::::" + this.ftpUserName);
		this.logger.debug("ftpUserPwd::" + this.ftpUserPwd);
		this.logger.debug("portNum::::" + this.portNum);
		this.logger.debug("lcleDirPath::::" + this.lcleDirPath);
		this.logger.debug("maxDownLoadFileRetryCount::::" + this.maxDownLoadFileRetryCount);
		this.logger.debug("retryCountGapTime::::" + this.retryCountGapTime);
		this.logger.debug("going to assign thread instance to pool");
		pool.assign(this);
	}

	public void run() {
		this.logger.debug("Inside run....");

		try {
			List filesList = AtlasWebServiceUtil.convertFromArrayObjectToArrayList(
					this.caseDetailsVO.getFileDetails().getItem(), "CaseFileDetailsVO");
			this.logger.debug("file size for case is:::::::::::" + filesList.size());
			this.downloadActualFiles(filesList, this.caseDetailsVO.getCaseManager());
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}

	public synchronized void downloadActualFiles(List fileList, String caseMgrId) {
		this.logger.debug("Inside downloadActualFiles method of DownLoadBatchFiles class");
		this.filesNotDwnld = new LinkedList();
		String rmteDirPath = null;
		String fileName = null;
		Timestamp fileCreationDate = null;

		try {
			FtpDownloader ftp = new FtpDownloader();
			String serverName = this.ftpAddress;
			boolean succdwm = false;
			CaseFileDetailsVO file;
			int i;
			if (!ftp.connectAndLogin(serverName, this.ftpUserName, this.ftpUserPwd, Integer.parseInt(this.portNum))) {
				for (i = 0; i < fileList.size(); ++i) {
					file = (CaseFileDetailsVO) fileList.get(i);
					fileName = file.getFileName();
					rmteDirPath = file.getPath();
					this.filesNotDwnld.add(file);
				}
			} else {
				this.logger.debug("got connected with ftp........");
				this.logger.debug("Going to process files.........");

				for (i = 0; i < fileList.size(); ++i) {
					file = (CaseFileDetailsVO) fileList.get(i);
					fileName = file.getFileName();
					rmteDirPath = file.getPath();
					fileCreationDate = this.convertDateToTimeStamp(file.getCreationDateTime());
					this.logger.debug("fileName:::::" + fileName + ":::::rmteDirPath" + rmteDirPath + ":::::::" + i);

					try {
						ftp.ascii();
						boolean success = (new File(this.lcleDirPath)).mkdirs();
						String origFileName = fileName;
						fileName = fileName.replaceAll("\\[", "(");
						fileName = fileName.replaceAll("\\]", ")");
						if (!success) {
							succdwm = ftp.downloadFile(rmteDirPath + origFileName, this.lcleDirPath + fileName);
						} else {
							succdwm = ftp.downloadFile(rmteDirPath + origFileName, this.lcleDirPath + fileName);
						}

						this.logger.debug("file download  flag value is:::::::::::::::::::::::::::::::" + succdwm);
						if (succdwm) {
							String folder = "Common";
							String[] folderNames = new String[]{folder};
							String[] pathStringArray1 = new String[1];
							String path = this.lcleDirPath + fileName;
							pathStringArray1[0] = path;
							this.logger.debug("path is::::::::::::::::::::" + path);
							this.logger.debug("Going to copy file to savvion..");
							ResourceLocator.self().getDocService().CreateDocForISIS(caseMgrId, pathStringArray1,
									Long.toString(this.piid), folderNames, (Session) null, fileCreationDate);
							this.logger
									.debug("Successful Downloading file :::::::::::::::::::::::::::::::::" + fileName);
							int status = 1;
							this.atlasWebServiceDAO.updateCMSFtpEntries(fileName, this.crn, status);
						} else {
							this.logger.debug("Error in downloading file from FTP " + fileName);
							File del = new File(this.lcleDirPath + fileName);
							del.delete();
							this.filesNotDwnld.add(file);
						}
					} catch (Exception var22) {
						this.logger.debug("Error in downloading file from FTP " + fileName);
						this.logger.debug("Error is::::::" + this.getStackTraceAsString(var22));
						File del = new File(this.lcleDirPath + fileName);
						del.delete();
						this.filesNotDwnld.add(file);
						this.retryDownloadBatchFiles(this.filesNotDwnld);
						this.filesNotDwnld.remove(file);
						var22.printStackTrace();
					}
				}

				try {
					ftp.logout();
					ftp.disconnect();
					this.deleteTempFolder(this.lcleDirPath);
				} catch (Exception var21) {
					this.logger.debug("Error is::::::" + this.getStackTraceAsString(var21));
				}
			}

			this.logger.debug("Retry logic :::::::filesNotDwnld.size():::::::::" + this.filesNotDwnld.size());
			if (!this.filesNotDwnld.isEmpty()) {
				this.retryDownloadBatchFiles(this.filesNotDwnld);
			}
		} catch (Exception var23) {
			this.logger.debug("Error in Downloading files through WebService " + var23.getMessage());
			this.logger.debug("Error is::::::" + this.getStackTraceAsString(var23));

			for (int i = 0; i < fileList.size(); ++i) {
				CaseFileDetailsVO file = (CaseFileDetailsVO) fileList.get(i);
				fileName = file.getFileName();
				rmteDirPath = file.getPath();
				this.filesNotDwnld.add(file);
			}

			try {
				this.retryDownloadBatchFiles(this.filesNotDwnld);
			} catch (FTPConnectionClosedException var19) {
				this.logger.debug("Error is :::" + this.getStackTraceAsString(var19));
			} catch (IOException var20) {
				this.logger.debug("Error is :::" + this.getStackTraceAsString(var20));
			}
		}

	}

	public void retryDownloadBatchFiles(LinkedList filesNotDwnld) throws IOException, FTPConnectionClosedException {
		this.logger.debug("Entring DownloadBatchFiles :retryDownloadBatchFiles ");

		try {
			if (!filesNotDwnld.isEmpty()) {
				this.logger.debug("File count for Filed file to download is more then zero:::" + filesNotDwnld.size());
				Iterator iterator = filesNotDwnld.iterator();

				while (iterator.hasNext()) {
					CaseFileDetailsVO caseFileDetailsVO = (CaseFileDetailsVO) iterator.next();
					int status = 2;
					this.atlasWebServiceDAO.updateCMSFtpEntries(caseFileDetailsVO.getFileName(), this.crn, status);
				}

				this.logger.debug("downLoadFileRetryCount=[" + this.downLoadFileRetryCount + "]");
				if (this.downLoadFileRetryCount < this.maxDownLoadFileRetryCount) {
					++this.downLoadFileRetryCount;
					Thread.sleep(this.retryCountGapTime);
					this.logger.debug("Going for Retry....");
					this.logger.debug("Going for Retry.....Retry count is.." + this.downLoadFileRetryCount);
					this.tryFailedFiles(filesNotDwnld);
				} else {
					this.downLoadFileRetryCount = 0;
					this.atlasWebServiceDAO.insertToCMSFTPFailFiles(filesNotDwnld, this.crn);
				}
			}

		} catch (Exception var6) {
			this.logger.debug("Error in downloading files using retry option");
			this.logger.debug("Error is :::" + this.getStackTraceAsString(var6));
			throw new FTPConnectionClosedException();
		}
	}

	public Timestamp convertDateToTimeStamp(Calendar cal) throws ParseException {
		Timestamp timeStamp = new Timestamp(cal.getTime().getTime());
		return timeStamp;
	}

	private void tryFailedFiles(List fileList) {
		this.downloadActualFiles(fileList, this.caseDetailsVO.getCaseManager());
	}

	public String getStackTraceAsString(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}

	private void deleteTempFolder(String folderPath) {
		this.logger.debug("Going to delete temp folder of location:::" + folderPath);
		if (folderPath != null) {
			File directory = new File(folderPath);
			if (directory.exists() && directory.list().length == 0) {
				boolean folderDeleteResult = directory.delete();
				this.logger.debug("Folder " + folderPath + " Delete result:::" + folderDeleteResult);
			}
		}

	}
}