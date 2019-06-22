package com.worldcheck.atlas.document;

import com.savvion.sbm.dms.svo.Document;
import com.savvion.sbm.dms.svo.DocumentFolder;
import com.savvion.sbm.util.FileUtil;
import com.worldcheck.atlas.document.util.DocUtilCommon;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;

public class DocCreator {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.document.DocCreator");
	private DocReceiver docReceiver;

	public List<String> CreateDoc(String userName, List<FileItem> fileItems, String pid, String[] folderName)
			throws CMSException {
		List<String> listOfDocIds = new ArrayList();
		this.logger.debug("Inside method of create document " + folderName.length);
		String delPath = "";

		try {
			String[] tempPath = this.docReceiver.processDoc(fileItems, folderName, userName);

			for (int i = 0; i < tempPath.length; ++i) {
				this.logger.debug("tempPath is:: " + tempPath[i]);
				if (null != tempPath[i] && !"".equalsIgnoreCase(tempPath[i].trim())) {
					String pathOfDoc = "/sbm/1/" + folderName[i] + "/" + folderName[i] + pid;
					File file = new File(tempPath[i]);
					byte[] getBytes = this.getDocInStream(file);
					Map<String, Object> map = new HashMap();
					map.put("CREATOR", userName);
					if (!this.checkFolder(pathOfDoc)) {
						this.CreateFolder(pathOfDoc);
					}

					Document doc = DocUtilCommon.getDocService().createDocument(DocUtilCommon.getDSContext(), pathOfDoc,
							file.getName(), map, getBytes);
					listOfDocIds.add(doc.getId());
				}

				if (tempPath.length > 0) {
					this.logger.debug("tempPath[0].split(userName) " + tempPath[0].split(userName)[0]);
					delPath = tempPath[0].split(userName)[0] + userName;
					this.logger.debug("del path is " + delPath);
				}
			}

			if (!"".equalsIgnoreCase(delPath)) {
				this.delFolder(delPath);
			}

			return listOfDocIds;
		} catch (NullPointerException var14) {
			throw new CMSException(this.logger, var14);
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	public List<String> CreateDocForISIS(String userName, String[] tempPath, String pid, String[] folderName)
			throws CMSException {
		List<String> listOfDocIds = new ArrayList();
		this.logger.debug("Inside method of create document for ISIS/JUNO " + folderName.length);

		try {
			for (int i = 0; i < tempPath.length; ++i) {
				this.logger.debug("tempPath is:: " + tempPath[i]);
				if (null != tempPath[i] && !"".equalsIgnoreCase(tempPath[i].trim())) {
					String pathOfDoc = "/sbm/1/" + folderName[i] + "/" + folderName[i] + pid;
					File file = new File(tempPath[i]);
					byte[] getBytes = this.getDocInStream(file);
					Map<String, Object> map = new HashMap();
					map.put("CREATOR", userName);
					if (!this.checkFolder(pathOfDoc)) {
						this.CreateFolder(pathOfDoc);
					}

					Document doc = DocUtilCommon.getDocService().createDocument(DocUtilCommon.getDSContext(), pathOfDoc,
							file.getName(), map, getBytes);
					listOfDocIds.add(doc.getId());
					this.delTempFile(tempPath[i]);
				}
			}

			return listOfDocIds;
		} catch (NullPointerException var12) {
			throw new CMSException(this.logger, var12);
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private boolean checkFolder(String pidPath) {
		boolean flag = false;
		flag = DocUtilCommon.getDocService().isFolderExistByPath(DocUtilCommon.getDSContext(), pidPath);
		this.logger.debug("inside checkFolder " + flag);
		return flag;
	}

	private void CreateFolder(String pidPath) {
		this.logger.debug("pidPath is " + pidPath);
		String[] path = pidPath.split("/");
		this.logger.debug("paths are " + path[1] + "/" + path[2] + " " + "::" + " " + path[3] + "/" + path[4]);
		if (!this.checkFolder("/sbm/1")) {
			DocUtilCommon.getDocService().createFolder(DocUtilCommon.getDSContext(), "/sbm", "1",
					"This is a parent folder inside dsdocRoot");
		}

		if (!this.checkFolder("/sbm/1/" + path[3])) {
			DocUtilCommon.getDocService().createFolder(DocUtilCommon.getDSContext(), "/sbm/1", path[3],
					"This is a working folder " + path[3]);
		}

		if (!this.checkFolder("/sbm/1/" + path[3] + "/" + path[4])) {
			this.logger.debug("/sbm/1/" + path[3] + "/" + path[4]);
			DocumentFolder docFolder = DocUtilCommon.getDocService().createFolder(DocUtilCommon.getDSContext(),
					"/sbm/1/" + path[3], path[4], "this folder is containning document of pid " + path[4]);
			this.logger.debug("folder is " + docFolder.getPath());
		}

		this.logger.debug("folder created ");
	}

	private byte[] getDocInStream(File file) {
		byte[] abyte0 = null;
		ByteArrayInputStream bytearrayinputstream = FileUtil.getContent(file);
		byte[] abyte0 = FileUtil.getByteArray(bytearrayinputstream);
		return abyte0;
	}

	public void setDocReceiver(DocReceiver docReceiver) {
		this.docReceiver = docReceiver;
	}

	private void delTempFile(String loc) {
		File f = new File(loc);
		f.delete();
	}

	private void delFolder(String srcFolder) {
		File directory = new File(srcFolder);
		if (!directory.exists()) {
			this.logger.debug("Directory does not exist.");
		} else {
			this.delete(directory);
		}

		this.logger.debug("Done");
	}

	private void delete(File file) {
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
				this.logger.debug("Directory is deleted : " + file.getAbsolutePath());
			} else {
				String[] files = file.list();
				String[] arr$ = files;
				int len$ = files.length;

				for (int i$ = 0; i$ < len$; ++i$) {
					String temp = arr$[i$];
					File fileDelete = new File(file, temp);
					this.delete(fileDelete);
				}

				if (file.list().length == 0) {
					file.delete();
					this.logger.debug("Directory is deleted : " + file.getAbsolutePath());
				}
			}
		} else if (file.exists()) {
			file.delete();
			this.logger.debug("File is deleted : " + file.getAbsolutePath());
		} else {
			this.logger.debug("No File found : " + file.getAbsolutePath());
		}

	}
}