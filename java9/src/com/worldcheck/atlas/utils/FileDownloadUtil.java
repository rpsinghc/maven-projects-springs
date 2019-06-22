package com.worldcheck.atlas.utils;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.masters.ClientRequirementManager;
import com.worldcheck.atlas.bl.masters.ReportTemplateManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownloadUtil extends HttpServlet {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.utils.fileDownloadUtil");
	ReportTemplateManager reportTemplateService = new ReportTemplateManager();
	ClientRequirementManager clientRequirementService = new ClientRequirementManager();
	private String reportPATH;
	private String REPORT_UPLOADED_FOLDER;
	private String clientPATH;
	private String CLIENT_UPLOADED_FOLDER;

	public FileDownloadUtil() throws CMSException {
		this.reportPATH = this.reportTemplateService.getproperties().getProperty("uploadableFilesTempPath");
		this.REPORT_UPLOADED_FOLDER = "reportTmplUploadedFiles";
		this.clientPATH = this.clientRequirementService.getproperties().getProperty("uploadableFilesTempPath");
		this.CLIENT_UPLOADED_FOLDER = "clientReqUploadedFiles";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.logger.debug("com.worldcheck.atlas.utils.fileDownloadUtil.doGet(HttpServletRequest, HttpServletResponse)");
		InputStream is = null;
		OutputStream os = null;
		FileInputStream fis = null;
		File file = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			String fileRootPath = "";
			String fullpath = "";
			if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("clientReq")) {
				fileRootPath = this.clientPATH + File.separator + this.CLIENT_UPLOADED_FOLDER + File.separator
						+ userName;
				fullpath = fileRootPath + File.separator + request.getParameter("action");
			}

			if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("reportDown")) {
				fileRootPath = this.reportPATH + File.separator + this.REPORT_UPLOADED_FOLDER + File.separator
						+ userName;
				fullpath = fileRootPath + File.separator + request.getParameter("action");
			}

			file = new File(fullpath);
			response.setContentType("application/xls");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			if (file.exists()) {
				fis = new FileInputStream(fullpath);
				is = new BufferedInputStream(fis);
				os = response.getOutputStream();
				byte[] buffer = new byte[1024];

				for (int read = is.read(buffer); read >= 0; read = is.read(buffer)) {
					if (read > 0) {
						os.write(buffer, 0, read);
					}
				}

				os.flush();
			}
		} catch (IOException var34) {
			AtlasUtils.getExceptionView(this.logger, var34);
		} catch (Exception var35) {
			AtlasUtils.getExceptionView(this.logger, var35);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var33) {
					AtlasUtils.getExceptionView(this.logger, var33);
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException var32) {
					AtlasUtils.getExceptionView(this.logger, var32);
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException var31) {
					AtlasUtils.getExceptionView(this.logger, var31);
				}
			}

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.logger
				.debug("com.worldcheck.atlas.utils.fileDownloadUtil.doPost(HttpServletRequest, HttpServletResponse)");
		InputStream is = null;
		OutputStream os = null;
		FileInputStream fis = null;
		File file = null;

		try {
			String userName = "";
			if (request.getParameter("uploadedBy") != null) {
				userName = request.getParameter("uploadedBy");
			} else {
				UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
				userName = userBean.getUserName();
			}

			this.logger.debug("user:::::::action::::type" + userName + ":::::::::::::::"
					+ request.getParameter("action") + "::::::::::::::" + request.getParameter("type"));
			String fileRootPath = "";
			String fullpath = "";
			if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("clientReq")) {
				fileRootPath = this.clientPATH + File.separator + this.CLIENT_UPLOADED_FOLDER + File.separator
						+ userName;
				fullpath = fileRootPath + File.separator + request.getParameter("action");
			}

			if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("reportDown")) {
				fileRootPath = this.reportPATH + File.separator + this.REPORT_UPLOADED_FOLDER + File.separator
						+ userName;
				fullpath = fileRootPath + File.separator + request.getParameter("action");
			}

			file = new File(fullpath);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			if (file.exists()) {
				fis = new FileInputStream(fullpath);
				is = new BufferedInputStream(fis);
				os = response.getOutputStream();
				byte[] buffer = new byte[1024];

				for (int read = is.read(buffer); read >= 0; read = is.read(buffer)) {
					if (read > 0) {
						os.write(buffer, 0, read);
					}
				}

				os.flush();
			}
		} catch (IOException var33) {
			AtlasUtils.getExceptionView(this.logger, var33);
		} catch (Exception var34) {
			AtlasUtils.getExceptionView(this.logger, var34);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var32) {
					AtlasUtils.getExceptionView(this.logger, var32);
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException var31) {
					AtlasUtils.getExceptionView(this.logger, var31);
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException var30) {
					AtlasUtils.getExceptionView(this.logger, var30);
				}
			}

		}

	}
}