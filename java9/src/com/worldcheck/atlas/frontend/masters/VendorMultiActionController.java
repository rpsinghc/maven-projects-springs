package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.IVendorMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.masters.VendorUploadMasterVO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class VendorMultiActionController extends MultiActionController {
	private static final String DD_MMM_YYYY_HH_MM_SS = "dd MMM yyyy HH:mm:ss";
	private static final String NUM_FILES_LIMIT_EXCEEDED = "numFilesLimitExceeded";
	private static final String FILE_SIZE_LIMIT_EXCEEDED = "fileSizeLimitExceeded";
	private static final String FILE_COUNT_FLAG = "fileCount";
	private static final String FILE_SIZE_FLAG = "fileSize";
	private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	private static final String VENDOR_UPLOADS_TO_DELETE_LIST = "vendorUploadsToDeleteList";
	private static final int MB = 1048576;
	private static final String VENDOR_TEMP_UPLOADS_LIST = "vendorTempUploadsList";
	private static final String VENDOR_NAME_PARAM = "vendorNameParam";
	private static final String FILE_TYPE_RADIO = "fileTypeRadio";
	private static final String COUNTRY_LIST = "countryList";
	private static final String VENDOR_MASTER_VO = "vendorMasterVO";
	private static final String VNDR_CODE = "vndrCode";
	private static final String UPDATE_ACTION = "update";
	private static final String ADD_ACTION = "add";
	private static final String ACTION_PARAM = "actionParam";
	private static final String SHEET_NAME = "VendorMaster";
	private static final String EXCEL_FILE_NAME = "VendorMaster";
	private static final String VENDOR_MASTER_ID = "vendorMasterId";
	private static final String VENDOR_CUSTOM_JSP = "masterVendorSearch";
	private static final String VENDOR_ADD_UPDATE_JSP = "masterVendorAddUpdate";
	private static final String REDIRECT_VENDOR_SEARCH = "redirect:vendorSearch.do";
	private static final String REDIRECT_ADD_UPDATE_VENDOR = "redirect:addUpdateVendor.do";
	private static final String VENDOR_CODE = "Vendor Code";
	private static final String VENDOR_NAME = "Vendor Name";
	private static final String VENDOR_TYPE = "Vendor Type";
	private static final String CLASSIFICATION = "Classification";
	private static final String COUNTRY = "Country";
	private static final String EXPERTISE = "Expertise";
	private static final String COSTS = "Costs";
	private static final String COMMENT = "Comment";
	private static final String CONTACT_NAME = "Contact Name";
	private static final String EMAIL1 = "Email 1";
	private static final String EMAIL2 = "Email 2";
	private static final String PHONE1 = "Phone 1";
	private static final String PHONE2 = "Phone 2";
	private static final String FAX = "Fax";
	private static final String ADDRESS = "Address";
	private static final String CONTRACT_EXPIRY_DATE = "Contract Expiry Date";
	private static final String NDA = "NDA";
	private static final String CONTRACT = "Contract";
	private static final String LEGACY_VENDOR_CODE = "Legacy Vendor Code";
	private static final String STATUS = "Status";
	private static final String DEACTIVE_STATUS = "Deactive";
	private static final String ACTIVE_STATUS = "Active";
	private static final String DEACTIVE_ID = "0";
	private static final String ACTIVE_ID = "1";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.VendorMultiActionController");
	IVendorMaster vendorMultiActionManager = null;
	PropertyReaderUtil propertyReader = null;

	public void setVendorMultiActionManager(IVendorMaster vendorMultiActionManager) {
		this.vendorMultiActionManager = vendorMultiActionManager;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView addUpdateVendor(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		VendorMasterVO vendorMasterVO = null;
		this.logger.debug("in VendorMultiActionController::addUpdateVendor");

		try {
			HttpSession session = request.getSession();
			session.removeAttribute("vendorTempUploadsList");
			session.removeAttribute("vendorUploadsToDeleteList");
			UserBean userDetailsBean = (UserBean) session.getAttribute("userBean");
			String currentUser = userDetailsBean.getUserName();
			this.deleteTempFolderForUser(currentUser);
			String action = request.getParameter("actionParam");
			if (action == null) {
				action = (String) session.getAttribute("actionParam");
			}

			modelAndView = new ModelAndView("masterVendorAddUpdate");
			if (action == null) {
				return new ModelAndView("redirect:vendorSearch.do");
			} else {
				if (!action.equals("add")) {
					if (!action.equals("update")) {
						return new ModelAndView("redirect:vendorSearch.do");
					}

					String vendorMasterId = request.getParameter("vendorMasterId");
					if (vendorMasterId == null && session.getAttribute("vendorMasterId") != null) {
						vendorMasterId = (String) session.getAttribute("vendorMasterId");
					} else if (vendorMasterId == null) {
						return new ModelAndView("redirect:vendorSearch.do");
					}

					vendorMasterVO = this.vendorMultiActionManager.getVendorInfo(vendorMasterId);
					if (vendorMasterVO == null) {
						return new ModelAndView("redirect:vendorSearch.do");
					}
				}

				this.logger.debug("in VendorMultiActionController::addUpdateVendor::vendorNameParam "
						+ (String) session.getAttribute("vendorNameParam"));
				modelAndView.addObject("vendorNameParam", (String) session.getAttribute("vendorNameParam"));
				modelAndView.addObject("actionParam", action);
				modelAndView.addObject("vendorMasterVO", vendorMasterVO);
				session.removeAttribute("actionParam");
				session.removeAttribute("vendorMasterId");
				session.removeAttribute("vendorNameParam");
				return modelAndView;
			}
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	private void deleteTempFolderForUser(String currentUser) {
		File dir = this.getTempVendorUploadDirectory(currentUser);
		if (dir.exists()) {
			File[] arr$ = dir.listFiles();
			int len$ = arr$.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				File file = arr$[i$];
				file.delete();
			}
		}

		dir.delete();
	}

	public ModelAndView saveVendor(HttpServletRequest request, HttpServletResponse response,
			VendorMasterVO vendorMasterVO) {
		ModelAndView modelAndView = null;
		this.logger.debug("in VendorMultiActionController::saveVendor");

		try {
			HttpSession session = request.getSession();
			String action = request.getParameter("actionParam");
			List<Integer> countryList = StringUtils.commaSeparatedStringToIntList(request.getParameter("countryList"));
			UserBean userDetailsBean = (UserBean) session.getAttribute("userBean");
			String currentUser = userDetailsBean.getUserName();
			vendorMasterVO.setUpdatedBy(currentUser);
			vendorMasterVO.setVendorCountryList(countryList);
			if (action.equals("add")) {
				modelAndView = new ModelAndView("redirect:addUpdateVendor.do");
				vendorMasterVO = this.vendorMultiActionManager.saveVendor(vendorMasterVO, false, (List) null,
						(List) null);
				session.setAttribute("vendorMasterId", String.valueOf(vendorMasterVO.getVendorMasterId()));
				session.setAttribute("vendorNameParam", vendorMasterVO.getVendorName());
				session.setAttribute("actionParam", "update");
				this.logger.info("Successfully added vendor :: " + vendorMasterVO.getVendorName() + " :: "
						+ vendorMasterVO.getVendorCode());
			} else if (action.equals("update")) {
				modelAndView = new ModelAndView("redirect:vendorSearch.do");
				List<VendorUploadMasterVO> vendorTempUploadsList = (List) session.getAttribute("vendorTempUploadsList");
				List<VendorUploadMasterVO> vendorUploadsToDeleteList = (List) session
						.getAttribute("vendorUploadsToDeleteList");
				vendorMasterVO = this.vendorMultiActionManager.saveVendor(vendorMasterVO, true, vendorTempUploadsList,
						vendorUploadsToDeleteList);
				this.logger.info("Successfully updated vendor :: " + vendorMasterVO.getVendorName() + " :: "
						+ vendorMasterVO.getVendorCode());
				session.setAttribute("vendorNameParam", vendorMasterVO.getVendorName());
				session.setAttribute("actionParam", "update");
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("VENDOR_MASTER");
			return modelAndView;
		} catch (CMSException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		} catch (Exception var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		}
	}

	private File getTempVendorUploadDirectory(String currentUser) {
		String tempPath = this.propertyReader.getVendorTempUploadsPath();
		tempPath = tempPath + File.separator + currentUser;
		File dir = new File(tempPath);
		return dir;
	}

	public ModelAndView vendorSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("masterVendorSearch");
			HttpSession session = request.getSession();
			if (request.getParameter("actionParam") != null && session.getAttribute("vendorNameParam") != null) {
				modelAndView.addObject("actionParam", request.getParameter("actionParam"));
				modelAndView.addObject("vendorNameParam", (String) session.getAttribute("vendorNameParam"));
				session.removeAttribute("vendorNameParam");
			}

			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView vendorExportToExcel(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  VendorMultiActionController vendorExportToExcel");
		ModelAndView modelAndView = null;

		try {
			String excelParams = request.getParameter("excelParams");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			List<VendorMasterVO> vendorList = this.vendorMultiActionManager.getDataForExport(excelParamMap);
			this.logger.debug("vendorList size is :: " + vendorList.size());
			Map<String, Object> resultMap = this.writeToExcel(vendorList, response);
			modelAndView = new ModelAndView("excelDownloadPopup");
			modelAndView.addObject("fileBytes", resultMap.get("fileBytes"));
			modelAndView.addObject("fileName", resultMap.get("fileName"));
			this.logger.debug("exiting  VendorMultiActionController vendorExportToExcel ");
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	private Map<String, Object> writeToExcel(List<VendorMasterVO> vendorList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToExcel ");
		List<String> lstHeader = this.getHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		VendorMasterVO vendorMasterVO = null;

		try {
			Iterator iterator = vendorList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				vendorMasterVO = (VendorMasterVO) iterator.next();
				this.populateDataMap(vendorMasterVO, datamap);
				dataList.add(datamap);
			}
		} catch (UnsupportedOperationException var8) {
			throw new CMSException(this.logger, var8);
		} catch (ClassCastException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IllegalArgumentException var11) {
			throw new CMSException(this.logger, var11);
		}

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "VendorMaster", (short) 0, (short) 1, response,
				"VendorMaster");
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Vendor Code");
			lstHeader.add("Vendor Name");
			lstHeader.add("Vendor Type");
			lstHeader.add("Classification");
			lstHeader.add("Country");
			lstHeader.add("Expertise");
			lstHeader.add("Costs");
			lstHeader.add("Comment");
			lstHeader.add("Contact Name");
			lstHeader.add("Email 1");
			lstHeader.add("Email 2");
			lstHeader.add("Phone 1");
			lstHeader.add("Phone 2");
			lstHeader.add("Fax");
			lstHeader.add("Address");
			lstHeader.add("Contract Expiry Date");
			lstHeader.add("NDA");
			lstHeader.add("Contract");
			lstHeader.add("Legacy Vendor Code");
			lstHeader.add("Status");
			return lstHeader;
		} catch (UnsupportedOperationException var3) {
			throw new CMSException(this.logger, var3);
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private void populateDataMap(VendorMasterVO vendorMasterVO, LinkedHashMap<String, String> datamap) {
		datamap.put("Vendor Code", String.valueOf(vendorMasterVO.getVendorCode()));
		datamap.put("Vendor Name", String.valueOf(vendorMasterVO.getVendorName()));
		datamap.put("Vendor Type", String.valueOf(vendorMasterVO.getVendorType()));
		datamap.put("Classification", String.valueOf(vendorMasterVO.getVendorClassification()));
		datamap.put("Country", String.valueOf(vendorMasterVO.getCountry()));
		datamap.put("Expertise", String.valueOf(vendorMasterVO.getExpertise()));
		datamap.put("Costs", String.valueOf(vendorMasterVO.getCosts()));
		datamap.put("Comment", String.valueOf(vendorMasterVO.getComments()));
		datamap.put("Contact Name", String.valueOf(vendorMasterVO.getContactName()));
		datamap.put("Email 1", String.valueOf(vendorMasterVO.getEmail1()));
		datamap.put("Email 2", String.valueOf(vendorMasterVO.getEmail2()));
		datamap.put("Phone 1", String.valueOf(vendorMasterVO.getPhone1()));
		datamap.put("Phone 2", String.valueOf(vendorMasterVO.getPhone2()));
		datamap.put("Fax", String.valueOf(vendorMasterVO.getFax()));
		datamap.put("Address", String.valueOf(vendorMasterVO.getAddress()));
		datamap.put("Contract Expiry Date", String.valueOf(vendorMasterVO.getContractExpiry()));
		datamap.put("NDA", String.valueOf(vendorMasterVO.getNdaExists()));
		datamap.put("Contract", String.valueOf(vendorMasterVO.getContractExists()));
		datamap.put("Legacy Vendor Code", String.valueOf(vendorMasterVO.getLegacyVendorCode()));
		if (vendorMasterVO.getVendorStatusId().equals("1")) {
			datamap.put("Status", "Active");
		} else if (vendorMasterVO.getVendorStatusId().equals("0")) {
			datamap.put("Status", "Deactive");
		}

	}

	public ModelAndView uploadVendorFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("in  VendorMultiActionController uploadVendorFile");
		String fileType = null;
		String vendorCode = null;
		FileItem fileItem = null;

		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<VendorUploadMasterVO> vendorTempUploadsList = (List) request.getSession()
					.getAttribute("vendorTempUploadsList");
			if (vendorTempUploadsList == null) {
				vendorTempUploadsList = new ArrayList();
			}

			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(request);
				Iterator itr = items.iterator();

				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (null != item) {
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("fileTypeRadio")) {
								fileType = item.getString();
							} else if (item.getFieldName().equalsIgnoreCase("vndrCode")) {
								vendorCode = item.getString();
							}
						} else {
							fileItem = item;
						}
					}
				}

				String uploadLimitFlag = this.vendorUploadLimitExceeded((List) vendorTempUploadsList, fileItem);
				this.logger.debug("VendorMultiActionController::uploadVendorFile::uploadLimitFlag: " + uploadLimitFlag);
				response.setContentType("text/html");
				JSONObject jsonObject = new JSONObject();
				if (uploadLimitFlag != null) {
					jsonObject = this.populateJsonObject(uploadLimitFlag);
				} else {
					UserBean userDetailsBean = (UserBean) request.getSession().getAttribute("userBean");
					String uploadedBy = userDetailsBean.getUserName();
					File file = this.writeVendorFile(fileItem, uploadedBy);
					VendorUploadMasterVO vendorUploadMasterVO = this.populateVendorUploadVO(fileType, vendorCode,
							fileItem.getName(), uploadedBy, file.length());
					((List) vendorTempUploadsList).add(vendorUploadMasterVO);
					request.getSession().setAttribute("vendorTempUploadsList", vendorTempUploadsList);
					jsonObject.put("success", true);
				}

				jsonObject.write(response.getWriter());
			}

			this.logger.debug("exiting VendorMultiActionController uploadVendorFile ");
			return null;
		} catch (CMSException var18) {
			return AtlasUtils.getExceptionView(this.logger, var18);
		} catch (Exception var19) {
			return AtlasUtils.getExceptionView(this.logger, var19);
		}
	}

	private JSONObject populateJsonObject(String uploadLimitFlag) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		if (uploadLimitFlag.equals("fileSize")) {
			jsonObject.put("fileSizeLimitExceeded", true);
		} else if (uploadLimitFlag.equals("fileCount")) {
			jsonObject.put("numFilesLimitExceeded", true);
		}

		return jsonObject;
	}

	private File writeVendorFile(FileItem fileItem, String uploadedBy) throws Exception {
		File dir = this.getTempVendorUploadDirectory(uploadedBy);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = fileItem.getName();
		if (fileName.contains("\\")) {
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}

		File file = new File(dir, fileName);
		fileItem.write(file);
		return file;
	}

	private VendorUploadMasterVO populateVendorUploadVO(String fileType, String vendorCode, String fileName,
			String uploadedBy, Long fileLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		VendorUploadMasterVO vendorUploadMasterVO = new VendorUploadMasterVO();
		vendorUploadMasterVO.setVendorCode(vendorCode);
		vendorUploadMasterVO.setFileName(fileName);
		vendorUploadMasterVO.setUploadedBy(uploadedBy);
		vendorUploadMasterVO.setUploadedOn(sdf.format(Calendar.getInstance().getTime()));
		vendorUploadMasterVO.setIsNda(Integer.parseInt(fileType) != 0);
		if (vendorUploadMasterVO.getIsNda()) {
			vendorUploadMasterVO.setFileType("NDA");
		} else {
			vendorUploadMasterVO.setFileType("Contract");
		}

		vendorUploadMasterVO.setFileSize((float) (fileLength / 1048576L));
		return vendorUploadMasterVO;
	}

	private String vendorUploadLimitExceeded(List<VendorUploadMasterVO> vendorTempUploadsList, FileItem file) {
		if ((float) vendorTempUploadsList.size() >= this.propertyReader.getMaxAllowedTotalFileCount()) {
			return "fileCount";
		} else {
			float totalSize = 0.0F;

			VendorUploadMasterVO vendorUpload;
			for (Iterator i$ = vendorTempUploadsList.iterator(); i$
					.hasNext(); totalSize += vendorUpload.getFileSize()) {
				vendorUpload = (VendorUploadMasterVO) i$.next();
			}

			totalSize += (float) (file.getSize() / 1048576L);
			long fileSize = file.getSize() / 1048576L;
			return totalSize <= this.propertyReader.getMaxAllowedTotalFileSize()
					&& (float) fileSize <= this.propertyReader.getMaxAllowedIndividualFileSize() ? null : "fileSize";
		}
	}
}