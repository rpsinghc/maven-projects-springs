package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IMassVendorUpload;
import com.worldcheck.atlas.bl.interfaces.IMasterDataValidator;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.validation.bl.AtlasPOIFSReaderListener;
import com.worldcheck.atlas.vo.massvendordataentry.MassVendorUploadVO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MassVendorUploadController extends MultiActionController {
	private static final String EMPTY_EXCEL_FILE = "Cannot Upload An Empty Excel File.";
	private static final String RECORDS = "records";
	private static final String UPLOADED = "Uploaded";
	private static final String ISMULTIPLESUB = "IsMultipleSub";
	private static final String MASS_VENDOR_UPLOAD_LIST = "massVendorUploadList";
	private static final String MASS_VENDOR_UPLOAD = "massVendorUpload";
	private static final String INVALID_EXCEL_MESSAGE = "Wrong Excel file uploaded. Please use an Excel file with the right template.";
	private static final String VALID_EXCEL = "validExcel";
	private static final String HASH_UI = "\\";
	private static final String MASSVENDORUPLOAD_CUSTOM_JSP = "massVendorUpload";
	private static final Object INVALID_ORIGINAL_TEMPLATE_MESSAGE = "Invalid File Template, Please use the template downloaded from application to upload the data.";
	private static final Object INVALID_MACRO_MESSAGE = "You have not enabled Macro. Please reopen file, enable Macro, save and upload again.";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.MassVendorUploadController");
	private IMasterDataValidator masterDataValidator;
	private IMassVendorUpload massVendorUploadManager;
	private PropertyReaderUtil propertyReaderUtil = null;

	public void setPropertyReaderUtil(PropertyReaderUtil propertyReader) {
		this.propertyReaderUtil = propertyReader;
	}

	public void setMasterDataValidator(IMasterDataValidator masterDataValidator) {
		this.masterDataValidator = masterDataValidator;
	}

	public void setMassVendorUploadManager(IMassVendorUpload massVendorUploadManager) {
		this.massVendorUploadManager = massVendorUploadManager;
	}

	public synchronized ModelAndView uploadExcel(HttpServletRequest request, HttpServletResponse response) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ModelAndView mv = new ModelAndView("redirect:setupMassVendorUpload.do");
		String massVendorRowData = "";
		List<String> multiSubCrnList = new ArrayList();
		File excelFile = null;

		try {
			this.logger.debug("request is a multipart request " + isMultipart);
			String[] fileNames = null;
			String temp = "";
			String tempPath = this.propertyReaderUtil.getMassVendorExcelTemplatePath();
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;
				items = upload.parseRequest(request);
				Iterator itr = items.iterator();

				label81 : while (true) {
					while (true) {
						FileItem item;
						do {
							do {
								if (!itr.hasNext()) {
									break label81;
								}

								item = (FileItem) itr.next();
							} while (null == item);
						} while (item.isFormField());

						String itemName = item.getName();
						this.logger.debug(" item names " + itemName);
						this.logger.debug(" index of " + itemName.lastIndexOf("\\"));
						int startIndex = itemName.lastIndexOf("\\");
						if (startIndex > -1) {
							this.logger.debug("request coming from IE browser");
							itemName = itemName.substring(startIndex + 1, itemName.length());
						}

						temp = tempPath + File.separator + itemName;
						excelFile = new File(temp);
						item.write(excelFile);
						boolean isValidExcel = false;
						boolean isOriginalTemplateFile = false;
						boolean isMacroEnabled = false;
						POIFSReader r = new POIFSReader();
						r.registerListener(new AtlasPOIFSReaderListener(), "DocumentSummaryInformation");
						r.read(new FileInputStream(excelFile));
						if (AtlasPOIFSReaderListener.excelFileCategory != null) {
							isOriginalTemplateFile = AtlasPOIFSReaderListener.excelFileCategory.equalsIgnoreCase(
									this.propertyReaderUtil.getMassVendorTemplateFileSummaryCategory());
						}

						isMacroEnabled = this.masterDataValidator.isMacroEnabledInMassVendorUploadFile(excelFile);
						isValidExcel = this.masterDataValidator.performExcelValidation(excelFile);
						if (isOriginalTemplateFile && isMacroEnabled && isValidExcel) {
							multiSubCrnList = this.massVendorUploadManager.processExcel(excelFile);
							massVendorRowData = (String) ((List) multiSubCrnList).get(0);
							if (massVendorRowData.equalsIgnoreCase("")) {
								request.getSession().setAttribute("validExcel", "Cannot Upload An Empty Excel File.");
								excelFile.delete();
								return mv;
							}
						} else {
							if (!isValidExcel) {
								request.getSession().setAttribute("validExcel",
										"Wrong Excel file uploaded. Please use an Excel file with the right template.");
								excelFile.delete();
								return mv;
							}

							if (!isOriginalTemplateFile) {
								request.getSession().setAttribute("validExcel", INVALID_ORIGINAL_TEMPLATE_MESSAGE);
								excelFile.delete();
								return mv;
							}

							if (!isMacroEnabled) {
								request.getSession().setAttribute("validExcel", INVALID_MACRO_MESSAGE);
								excelFile.delete();
								return mv;
							}
						}
					}
				}
			}
		} catch (Exception var22) {
			return AtlasUtils.getExceptionView(this.logger, var22);
		}

		this.logger.info("File read success fully ");
		this.logger.debug(" row data " + massVendorRowData);
		if (null != excelFile) {
			this.logger.info("going to delete temp file " + excelFile.length());
			excelFile.delete();
			this.logger.info("Deleted the file");
		}

		if (((List) multiSubCrnList).size() > 2 && null != ((List) multiSubCrnList).get(2)
				&& !((String) ((List) multiSubCrnList).get(2)).isEmpty()) {
			request.getSession().setAttribute("IsMultipleSub", ((List) multiSubCrnList).get(2));
		}

		request.getSession().setAttribute("massVendorUploadList", massVendorRowData);
		return mv;
	}

	public ModelAndView downloadExcel(HttpServletRequest httpservletrequest, HttpServletResponse response) {
		this.logger.debug("method to download template");
		ModelAndView mv = new ModelAndView("massVendorUpload");

		try {
			FileInputStream fis = null;
			InputStream is = null;
			OutputStream os = null;
			this.logger.debug("propertyReaderUtil.getMassVendorExcelTemplatePath() "
					+ this.propertyReaderUtil.getMassVendorExcelTemplateDownloadPath());
			File destFile = new File(this.propertyReaderUtil.getMassVendorExcelTemplateDownloadPath());
			this.logger.debug("destFile.exists() " + destFile.exists());
			if (destFile.exists()) {
				ModelAndView var9;
				try {
					response.setContentType("application/xls");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + destFile.getName() + "\"");
					this.logger.debug("file exist");
					fis = new FileInputStream(destFile.getAbsolutePath());
					is = new BufferedInputStream(fis);
					os = response.getOutputStream();
					byte[] buffer = new byte[1024];

					for (int read = is.read(buffer); read >= 0; read = is.read(buffer)) {
						if (read > 0) {
							os.write(buffer, 0, read);
						}
					}

					this.logger.debug("file loc ::" + destFile.getAbsolutePath());
					return mv;
				} catch (Exception var14) {
					var9 = AtlasUtils.getExceptionView(this.logger, var14);
				} finally {
					if (fis != null) {
						fis.close();
					}

					if (is != null) {
						is.close();
					}

					if (os != null) {
						os.flush();
						os.close();
					}

				}

				return var9;
			} else {
				return mv;
			}
		} catch (Exception var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}
	}

	public ModelAndView saveData(HttpServletRequest request, HttpServletResponse response,
			MassVendorUploadVO massVendorUploadVO) {
		ModelAndView mv = new ModelAndView("massVendorUpload");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		ArrayList modifiedRecords = new ArrayList();

		try {
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;
				items = upload.parseRequest(request);
				Iterator<FileItem> itr = items.iterator();

				FileItem item;
				for (int var12 = 0; itr.hasNext(); ++var12) {
					item = (FileItem) itr.next();
					if (null != item && item.isFormField() && item.getFieldName().equalsIgnoreCase("records")) {
						Map jsonObject = (Map) JSONValue.parse(item.getString());
						modifiedRecords.add(jsonObject);
					}
				}

				this.logger.debug("modified data " + modifiedRecords.size());
				item = null;
				String caseHistoryPerformer;
				if (null != request.getSession().getAttribute("loginLevel")) {
					caseHistoryPerformer = (String) request.getSession().getAttribute("performedBy");
				} else {
					caseHistoryPerformer = userBean.getUserName();
				}

				List<String> massVendorRowData = this.massVendorUploadManager.getModifiedDataList(modifiedRecords,
						userBean.getUserName(), caseHistoryPerformer);
				this.logger.debug("size of list " + massVendorRowData.size());
				this.logger.info("the CRN is ====" + (String) massVendorRowData.get(1) + "========"
						+ (String) massVendorRowData.get(0) + "=====" + (String) massVendorRowData.get(2));
				mv.addObject("massVendorUploadList", massVendorRowData.get(0));
				if (massVendorRowData.size() == 3) {
					this.logger.debug((String) massVendorRowData.get(0));
					this.logger.debug((String) massVendorRowData.get(1));
					this.logger.debug((String) massVendorRowData.get(2));
					mv.addObject("Uploaded", massVendorRowData.get(2));
				}

				if (massVendorRowData.size() == 4) {
					this.logger.debug((String) massVendorRowData.get(0));
					this.logger.debug((String) massVendorRowData.get(1));
					this.logger.debug((String) massVendorRowData.get(2));
					this.logger.debug((String) massVendorRowData.get(3));
					mv.addObject("IsMultipleSub", massVendorRowData.get(2));
					mv.addObject("Uploaded", massVendorRowData.get(3));
				}
			}

			return mv;
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}
	}

	public ModelAndView setupMassVendorUpload(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("massVendorUpload");
		this.logger.debug(" parameters " + request.getParameterMap());
		this.logger.debug(request.getAttributeNames().toString());
		if (null != request) {
			Map parameterMap = request.getParameterMap();
			Set keySet = parameterMap.keySet();
			Iterator iterator = keySet.iterator();

			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				modelAndView.addObject(key, request.getParameter(key));
			}
		}

		if (request.getSession() != null && request.getSession().getAttribute("validExcel") != null) {
			modelAndView.addObject("validExcel", request.getSession().getAttribute("validExcel"));
			request.getSession().removeAttribute("validExcel");
		}

		if (request.getSession() != null && request.getSession().getAttribute("IsMultipleSub") != null) {
			modelAndView.addObject("IsMultipleSub", request.getSession().getAttribute("IsMultipleSub"));
			request.getSession().removeAttribute("IsMultipleSub");
		}

		if (request.getSession() != null && request.getSession().getAttribute("massVendorUploadList") != null) {
			modelAndView.addObject("massVendorUploadList", request.getSession().getAttribute("massVendorUploadList"));
			request.getSession().removeAttribute("massVendorUploadList");
		}

		return modelAndView;
	}
}