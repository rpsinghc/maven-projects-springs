package com.worldcheck.atlas.document;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

public class DocReceiver {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.document.DocReceiver");

	public String[] processDoc(List<FileItem> fileItems, String[] folderName, String userName) throws CMSException {
		ClassLoader classLoader = DocReceiver.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("atlas.properties");
		Properties prop = new Properties();

		try {
			prop.load(is);
		} catch (IOException var9) {
			throw new CMSException(this.logger, var9);
		}

		String tempPath = prop.getProperty("tempFilePath");
		String[] filePath = this.getFiles(fileItems, tempPath, folderName, userName);
		return filePath;
	}

	private String[] getFiles(List<FileItem> fileItems, String tempPath, String[] folderName, String userName)
			throws CMSException {
		String[] fileNames = null;
		int len = folderName.length;

		try {
			this.logger.debug("Inside doc receiver getFIles class");
			String temp = "";
			int i = 0;
			List<FileItem> items = null;
			this.logger.debug(fileItems + "");
			Iterator<FileItem> itr = fileItems.iterator();
			fileNames = new String[len];

			while (itr.hasNext()) {
				this.logger.debug("inside while");
				FileItem item = (FileItem) itr.next();
				this.logger.debug(item.getFieldName());
				if (null != item) {
					this.logger.debug(item.getFieldName());
					this.logger.debug(item + "");
					if (!item.isFormField()) {
						String itemName = item.getFieldName();
						if (itemName.startsWith("file") && !item.getName().equalsIgnoreCase("")) {
							String pathForFile = item.getName();
							pathForFile = pathForFile.replaceAll("\\[", "(");
							pathForFile = pathForFile.replaceAll("\\]", ")");
							this.logger.debug("pathFor file is " + pathForFile);
							String[] tempPathArray = pathForFile.split("\\\\");
							int tempLen = tempPathArray.length;
							this.logger.debug("tempLen is " + tempLen);
							if (tempLen > 1) {
								pathForFile = tempPathArray[tempLen - 1];
								this.logger.debug("pathFor file is " + pathForFile);
							}

							temp = tempPath + "/" + userName + "/" + i + "/" + pathForFile;
							File savedFile = new File(temp);
							String firstFolder = tempPath + "/" + userName;
							if ((new File(firstFolder)).mkdir()) {
								this.logger.debug("Directory Created");
							} else {
								this.logger.debug("Directory is not created");
							}

							String secondFolder = firstFolder + "/" + i;
							if ((new File(secondFolder)).mkdir()) {
								this.logger.debug("Directory Created");
							} else {
								this.logger.debug("Directory is not created");
							}

							item.write(savedFile);
							fileNames[i] = temp;
							this.logger.debug("file name is " + temp);
							++i;
						}
					}
				}
			}

			return fileNames;
		} catch (FileUploadException var19) {
			throw new CMSException(this.logger, var19);
		} catch (Exception var20) {
			throw new CMSException(this.logger, var20);
		}
	}
}