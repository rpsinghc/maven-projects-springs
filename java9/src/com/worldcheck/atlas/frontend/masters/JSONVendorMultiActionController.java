package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IVendorMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.VendorUploadMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONVendorMultiActionController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONVendorMultiActionController");
	private static final String TEMP_FILES_EXIST = "tempFilesExist";
	private static final String VENDOR_NAME = "vendorName";
	private static final String VENDOR_UPLOADS_TO_DELETE_LIST = "vendorUploadsToDeleteList";
	private static final String VENDOR_TEMP_UPLOADS_LIST = "vendorTempUploadsList";
	private static final String VENDOR_MASTER_ID = "vendorMasterId";
	private static final String VENDOR_UPLOADS_LIST = "vendorUploadsList";
	private static final String VENDOR_FILE_NAMES = "vendorFileNames";
	private static final String FILE_EXISTS = "fileExists";
	private static final String FILE_NAME = "fileName";
	private static final String VENDOR_CODE = "vendorCode";
	private static final String SELECTED_COUNTRIES_LIST = "selectedCountriesList";
	private static final String ALL_COUNTRIES_LIST = "allCountriesList";
	private static final String VENDOR_MASTER = "VENDOR_MASTER";
	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String SEARCH_RESULT = "searchResult";
	private static final String SUCCESS = "success";
	private static final String VENDOR_CRN_LIST = "vendorCrnList";
	private static final String VENDOR_LIST = "vendorList";
	private static final String SELECTED_VENDOR_ID = "selectedVendorId";
	private static final String VENDOR_STATUS = "vendorStatus";
	private static final int ACTIVE_STATUS = 1;
	private static final String COMMENTS = "comments";
	private static final String EXPERTISE = "expertise";
	private static final String CONTRACT_EXPIRY_END = "contractExpiryEnd";
	private static final String CONTRACT_EXPIRY_START = "contractExpiryStart";
	private static final String COUNTRY_LIST = "countryList";
	private static final String VENDOR_TYPE_ID = "vendorTypeId";
	IVendorMaster vendorMultiActionManager;
	PropertyReaderUtil propertyReader = null;

	public void setVendorMultiActionManager(IVendorMaster vendorMultiActionManager) {
		this.vendorMultiActionManager = vendorMultiActionManager;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView getVendorTypeList(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::getVendorTypeList");
		ModelAndView modelAndView = null;
		ArrayList vendorList = null;

		try {
			vendorList = (ArrayList) this.vendorMultiActionManager.getVendorTypes();
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("vendorList", vendorList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView searchVendor(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::searchVendor");
		ModelAndView modelAndView = null;
		ArrayList searchResult = null;

		try {
			HashMap<String, Object> paramMap = new HashMap();
			new ArrayList();
			String sortColumnName = request.getParameter("sort");
			String sortType = request.getParameter("dir");
			this.logger.debug("SortColumn :" + sortColumnName + " SortType:" + sortType);
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			limit += start;
			++start;
			List<String> countryList = StringUtils.commaSeparatedStringToList(request.getParameter("countryList"));
			String contractExpiryStart = request.getParameter("contractExpiryStart");
			String contractExpiryEnd = request.getParameter("contractExpiryEnd");
			if (contractExpiryStart != null && contractExpiryStart.length() != 0) {
				contractExpiryStart = contractExpiryStart.substring(0, contractExpiryStart.indexOf("T"));
			}

			if (contractExpiryEnd != null && contractExpiryEnd.length() != 0) {
				contractExpiryEnd = contractExpiryEnd.substring(0, contractExpiryEnd.indexOf("T"));
			}

			paramMap.put("vendorTypeId", request.getParameter("vendorTypeId"));
			paramMap.put("countryList", countryList);
			paramMap.put("vendorCode", request.getParameter("vendorCode"));
			paramMap.put("vendorName", request.getParameter("vendorName"));
			paramMap.put("contractExpiryStart", contractExpiryStart);
			paramMap.put("contractExpiryEnd", contractExpiryEnd);
			paramMap.put("expertise", request.getParameter("expertise"));
			paramMap.put("comments", request.getParameter("comments"));
			paramMap.put("vendorStatus", request.getParameter("vendorStatus"));
			paramMap.put("start", start);
			paramMap.put("limit", limit);
			paramMap.put("sort", sortColumnName);
			paramMap.put("dir", sortType);
			this.logger.debug("JSONVendorMultiActionController::start: " + start + " ::limit: " + limit);
			searchResult = (ArrayList) this.vendorMultiActionManager.searchVendor(paramMap);
			int totalCount = this.vendorMultiActionManager.getTotalCount(paramMap);
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("searchResult", searchResult);
			modelAndView.addObject("total", totalCount);
			return modelAndView;
		} catch (CMSException var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		} catch (Exception var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		}
	}

	public ModelAndView changeVendorStatus(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::changeVendorStatus");
		ModelAndView modelAndView = null;
		List<String> crnList = null;
		StringBuffer vendorCrnList = new StringBuffer();

		try {
			String selectedVendorId = request.getParameter("selectedVendorId");
			String vendorStatus = request.getParameter("vendorStatus");
			modelAndView = new ModelAndView("jsonView");
			UserBean userDetailsBean = (UserBean) request.getSession().getAttribute("userBean");
			String currentUser = userDetailsBean.getUserName();
			if (Integer.parseInt(vendorStatus) == 1) {
				crnList = this.vendorMultiActionManager.getAssociatedWIPCasesList(selectedVendorId);
				this.logger.debug("JSONVendorMultiActionController::crnList size: " + crnList.size());
				if (crnList.isEmpty()) {
					this.vendorMultiActionManager.changeVendorStatus(selectedVendorId,
							request.getParameter("vendorStatus"), currentUser);
					this.logger.info("Successfully activated vendor with Id :: " + selectedVendorId);
					modelAndView.addObject("success", "true");
					ResourceLocator.self().getCacheService().addToCacheRunTime("VENDOR_MASTER");
				} else {
					Iterator i$ = crnList.iterator();

					while (i$.hasNext()) {
						String crn = (String) i$.next();
						vendorCrnList.append(crn + "<br/>");
					}

					modelAndView.addObject("vendorCrnList", vendorCrnList.toString());
					modelAndView.addObject("success", "false");
				}
			} else {
				this.vendorMultiActionManager.changeVendorStatus(selectedVendorId, request.getParameter("vendorStatus"),
						currentUser);
				this.logger.info("Successfully deactivated vendor with Id :: " + selectedVendorId);
				modelAndView.addObject("success", "true");
				ResourceLocator.self().getCacheService().addToCacheRunTime("VENDOR_MASTER");
			}

			return modelAndView;
		} catch (CMSException var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		} catch (NumberFormatException var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		} catch (Exception var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		}
	}

	public ModelAndView checkAssociatedWIPCases(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::checkAssociatedWIPCases");
		ModelAndView modelAndView = null;
		List<String> crnList = null;
		StringBuffer vendorCrnList = new StringBuffer();

		try {
			String selectedVendorId = request.getParameter("selectedVendorId");
			modelAndView = new ModelAndView("jsonView");
			crnList = this.vendorMultiActionManager.getAssociatedWIPCasesList(selectedVendorId);
			if (crnList.isEmpty()) {
				modelAndView.addObject("success", "true");
			} else {
				Iterator i$ = crnList.iterator();

				while (i$.hasNext()) {
					String crn = (String) i$.next();
					vendorCrnList.append(crn + "<br/>");
				}

				modelAndView.addObject("vendorCrnList", vendorCrnList.toString());
				modelAndView.addObject("success", "false");
			}

			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getVendorUploads(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::getVendorUploads");
		ModelAndView modelAndView = null;
		List vendorUploadsList = null;

		try {
			String vendorCode = request.getParameter("vendorCode");
			modelAndView = new ModelAndView("jsonView");
			List<VendorUploadMasterVO> vendorTempUploadsList = (List) request.getSession()
					.getAttribute("vendorTempUploadsList");
			List<VendorUploadMasterVO> vendorUploadsToDeleteList = (List) request.getSession()
					.getAttribute("vendorUploadsToDeleteList");
			vendorUploadsList = this.vendorMultiActionManager.getVendorUploads(vendorCode, vendorTempUploadsList,
					vendorUploadsToDeleteList);
			modelAndView.addObject("vendorUploadsList", vendorUploadsList);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView deleteVendorUploads(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::deleteVendorUploads");
		List<String> vendorFileNames = null;
		ModelAndView modelAndView = null;

		try {
			UserBean userDetailsBean = (UserBean) request.getSession().getAttribute("userBean");
			String uploadedBy = userDetailsBean.getUserName();
			String vendorCode = request.getParameter("vendorCode");
			vendorFileNames = StringUtils.commaSeparatedStringToList(request.getParameter("vendorFileNames"));
			List<VendorUploadMasterVO> vendorTempUploadsList = (List) request.getSession()
					.getAttribute("vendorTempUploadsList");
			List<VendorUploadMasterVO> vendorUploadsToDeleteList = (List) request.getSession()
					.getAttribute("vendorUploadsToDeleteList");
			vendorUploadsToDeleteList = this.vendorMultiActionManager.tempDeleteVendorUploads(vendorFileNames,
					vendorTempUploadsList, vendorUploadsToDeleteList, uploadedBy, vendorCode);
			modelAndView = new ModelAndView("jsonView");
			if (vendorTempUploadsList != null) {
				request.getSession().setAttribute("vendorTempUploadsList", vendorTempUploadsList);
			}

			if (vendorUploadsToDeleteList != null) {
				request.getSession().setAttribute("vendorUploadsToDeleteList", vendorUploadsToDeleteList);
			}

			return modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView vendorFileExists(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::vendorFileExists");
		boolean fileExists = false;
		ModelAndView modelAndView = null;

		try {
			UserBean userDetailsBean = (UserBean) request.getSession().getAttribute("userBean");
			String uploadedBy = userDetailsBean.getUserName();
			String vendorCode = request.getParameter("vendorCode");
			String fileName = request.getParameter("fileName");
			fileExists = this.vendorMultiActionManager.vendorFileExists(vendorCode, fileName, uploadedBy);
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("fileExists", fileExists);
			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView vendorExists(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::vendorExists");
		ModelAndView modelAndView = null;

		try {
			String vendorCode = this.vendorMultiActionManager.vendorExists(request.getParameter("vendorName"));
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("vendorCode", vendorCode);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		}
	}

	public ModelAndView tempFilesExist(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::tempFilesExist");
		ModelAndView modelAndView = null;
		boolean tempFilesExist = false;

		try {
			List<VendorUploadMasterVO> vendorTempUploadsList = (List) request.getSession()
					.getAttribute("vendorTempUploadsList");
			if (vendorTempUploadsList != null && vendorTempUploadsList.size() > 0) {
				tempFilesExist = true;
			}

			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("tempFilesExist", tempFilesExist);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getAllCountries(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::getAllCountries");
		ModelAndView modelAndView = null;
		ArrayList allCountriesList = null;

		try {
			allCountriesList = (ArrayList) this.vendorMultiActionManager
					.getAllCountries(request.getParameter("vendorMasterId"));
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("allCountriesList", allCountriesList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getSelectedCountries(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONVendorMultiActionController::getSelectedCountries");
		ModelAndView modelAndView = null;
		ArrayList selectCountriesList = null;

		try {
			selectCountriesList = (ArrayList) this.vendorMultiActionManager
					.getSelectedCountries(request.getParameter("vendorMasterId"));
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject("selectedCountriesList", selectCountriesList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}
}