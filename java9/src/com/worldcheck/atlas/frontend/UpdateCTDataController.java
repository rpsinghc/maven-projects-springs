package com.worldcheck.atlas.frontend;

import com.worldcheck.atlas.bl.updateCTDataEntry.UpdateCTDataManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.validation.bl.CTDataValidator;
import com.worldcheck.atlas.vo.UpdateCTDataExcelVO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UpdateCTDataController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.UpdateCTDataController");
	private static final String EMPTY_EXCEL_FILE = "Cannot Upload An Empty Excel File.";
	private static final String HASH_UI = "\\";
	private static final String UPDATECTDATA_JSP = "updateCTData";
	private static final String INVALID_EXCEL_MESSAGE = "Wrong Excel file uploaded. Please use an Excel file with the right template";
	private static final String DATA_INVALID_EXCEL_MESSAGE = "Data are in invalid form in excel";
	private static final String VALID_EXCEL = "validExcel";
	private static final String UPDATEDCTDATASTATUS = "UpdatedCTDataStatus";
	private static final String UPDATECTDATASTATUSSIZE = "UpdateCTDataStatusSize";
	private static final String UPDATECTDATASTATUS = "UpdateCTDataStatus";
	private PropertyReaderUtil propertyReaderUtil = null;
	private CTDataValidator ctDataValidator;
	private UpdateCTDataManager updateCTDataManager;

	public void setPropertyReaderUtil(PropertyReaderUtil propertyReader) {
		this.propertyReaderUtil = propertyReader;
	}

	public void setCtDataValidator(CTDataValidator ctDataValidator) {
		this.ctDataValidator = ctDataValidator;
	}

	public void setUpdateCTDataManager(UpdateCTDataManager updateCTDataManager) {
		this.updateCTDataManager = updateCTDataManager;
	}

	public ModelAndView updateCTData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("In UpdateCTDataController : updateCTData");
		ModelAndView modelAndView = new ModelAndView("updateCTData");
		if (request.getSession().getAttribute("UpdatedCTDataStatus") != null) {
			request.getSession().removeAttribute("UpdatedCTDataStatus");
		}

		return modelAndView;
	}

	public synchronized ModelAndView updateCTDataExcel(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In updateCTDataExcel method");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ModelAndView mv = new ModelAndView("updateCTData");
		String updateCTData = "";
		File excelFile = null;
		BufferedInputStream excelFileStream = null;

		try {
			if (request.getSession().getAttribute("UpdatedCTDataStatus") != null) {
				request.getSession().removeAttribute("UpdatedCTDataStatus");
			}

			this.logger.debug("request is a multipart request " + isMultipart);
			String[] fileNames = null;
			String temp = "";
			String tempPath = this.propertyReaderUtil.getCtDataExcelTempPath();
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;
				items = upload.parseRequest(request);
				Iterator itr = items.iterator();

				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (null != item && !item.isFormField()) {
						String itemName = item.getName();
						this.logger.debug(" item names " + itemName);
						this.logger.debug(" index of " + itemName.lastIndexOf("\\"));
						int startIndex = itemName.lastIndexOf("\\");
						if (startIndex > -1) {
							this.logger.debug("request coming from IE browser");
							itemName = itemName.substring(startIndex + 1, itemName.length());
						}

						File fileDir = new File(tempPath);
						if (!fileDir.exists()) {
							fileDir.mkdir();
						}

						temp = tempPath + File.separator + itemName;
						excelFile = new File(temp);
						item.write(excelFile);
					}
				}

				excelFileStream = new BufferedInputStream(new FileInputStream(excelFile));
				this.logger.debug("file input stream found  ");
				HSSFWorkbook wb = new HSSFWorkbook(excelFileStream);
				this.logger.debug("work book found  ");
				if (this.ctDataValidator.performExcelFormatValidation(wb)) {
					updateCTData = this.updateCTDataManager.processExcel(wb, request);
					if (updateCTData.equalsIgnoreCase("")) {
						mv.addObject("validExcel", "Cannot Upload An Empty Excel File.");
						excelFile.delete();
						return mv;
					}

					if (updateCTData.equalsIgnoreCase("Data are in invalid form in excel")) {
						mv.addObject("validExcel", "Data are in invalid form in excel");
						excelFile.delete();
						return mv;
					}

					if (updateCTData.equalsIgnoreCase(String.valueOf(true))) {
						excelFile.delete();
						new ArrayList();
						List<UpdateCTDataExcelVO> resultList = (List) request.getSession()
								.getAttribute("UpdatedCTDataStatus");
						mv.addObject("UpdateCTDataStatusSize", resultList.size());
						mv.addObject("UpdateCTDataStatus", true);
						return mv;
					}
				} else {
					mv.addObject("validExcel",
							"Wrong Excel file uploaded. Please use an Excel file with the right template");
					excelFile.delete();
				}
			}
		} catch (CMSException var19) {
			if (null != excelFile) {
				excelFile.delete();
			}

			return AtlasUtils.getExceptionView(this.logger, var19);
		} catch (Exception var20) {
			return AtlasUtils.getExceptionView(this.logger, var20);
		}

		if (null != excelFile) {
			excelFile.delete();
		}

		return mv;
	}
}