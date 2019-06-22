package com.worldcheck.atlas.isis.util;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Vector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpDownloader extends FTPClient {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.isis.util.FtpDownloader");

	public boolean connectAndLogin(String host, String userName, String password, int portNumber)
			throws IOException, UnknownHostException, FTPConnectionClosedException {
		this.logger.debug("inside connectAndLogin method of FtpDownloader class");
		boolean success = false;
		this.connect(host);
		int reply = this.getReplyCode();
		if (FTPReply.isPositiveCompletion(reply)) {
			success = this.login(userName, password);
		}

		if (!success) {
			this.disconnect();
		}

		this.logger.debug("success flag value is::::" + success);
		return success;
	}

	public void setPassiveMode(boolean setPassive) {
		if (setPassive) {
			this.enterLocalPassiveMode();
		} else {
			this.enterLocalActiveMode();
		}

	}

	public boolean ascii() throws IOException {
		return this.setFileType(0);
	}

	public boolean binary() throws IOException {
		return this.setFileType(2);
	}

	public boolean downloadFile(String serverFile, String localFile) throws IOException, FTPConnectionClosedException {
		FileOutputStream outputStream = new FileOutputStream(localFile);
		InputStream inputStream = this.retrieveFileStream(serverFile);
		IOUtils.copy(inputStream, outputStream);
		outputStream.flush();
		IOUtils.closeQuietly(outputStream);
		IOUtils.closeQuietly(inputStream);
		boolean status = this.completePendingCommand();
		return status;
	}

	public boolean uploadFile(String localFile, String serverFile) throws IOException, FTPConnectionClosedException {
		FileInputStream in = new FileInputStream(localFile);
		boolean result = this.storeFile(serverFile, in);
		in.close();
		return result;
	}

	public Vector listFileNames() throws IOException, FTPConnectionClosedException {
		FTPFile[] files = this.listFiles();
		Vector v = new Vector();

		for (int i = 0; i < files.length; ++i) {
			if (!files[i].isDirectory()) {
				v.addElement(files[i].getName());
			}
		}

		return v;
	}

	public String listFileNamesString() throws IOException, FTPConnectionClosedException {
		return this.vectorToString(this.listFileNames(), "\n");
	}

	public Vector listSubdirNames() throws IOException, FTPConnectionClosedException {
		FTPFile[] files = this.listFiles();
		Vector v = new Vector();

		for (int i = 0; i < files.length; ++i) {
			if (files[i].isDirectory()) {
				v.addElement(files[i].getName());
			}
		}

		return v;
	}

	public String listSubdirNamesString() throws IOException, FTPConnectionClosedException {
		return this.vectorToString(this.listSubdirNames(), "\n");
	}

	private String vectorToString(Vector v, String delim) {
		StringBuffer sb = new StringBuffer();
		String s = "";

		for (int i = 0; i < v.size(); ++i) {
			sb.append(s).append((String) v.elementAt(i));
			s = delim;
		}

		return sb.toString();
	}
}