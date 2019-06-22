package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.downloadexcel.SourceAvailabilityExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.ICountryDB;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CountryDatabaseMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.CountryDatabaseMultiActionController");
	private String EXPORT_TYPE = "lbrParam";
	private String JSP = "masterMainCountryDB";
	private String DB_NAME = "dbName";
	private String CDB = "cdb";
	private String ADD_JSP = "masterAddCountryDB";
	private String SOURCE_AVAILABILITY = "SourceAvailability";
	private String SEARCH_CDB = "SearchCDBResult";
	private String SEARCH_LBR = "SearchLBRResult";
	private String DB_COUNTRY = "country";
	private String DB_CATEGORY = "category";
	private String REDIRECT_COUNTRYDB_MASTER = "redirect:countryDatabase.do";
	private String DELETE_SUCCESS = "deleteSuccess";
	private String UPDATE_SUCCESS = "updateSuccess";
	private String ADD_SUCCESS = "addSuccess";
	private String COUNTRY = "Country";
	private String CATEGORY = "Category";
	private String DATABASE_NAME = "Name Of Database";
	private String LINK = "Link";
	private String TYPE = "Type";
	private String SEARCH_METHOD = "Search Methodology";
	private String DESCRIPTION = "Description";
	private String LANGUAGE = "Language";
	private String REMARKS = "Remarks";
	private String LLM_COVERED = "Local Language Media";
	private String ROC_INCORPORATION = "Incorporation Details";
	private String ROC_MANAGEMENT = "Management Details";
	private String ROC_FINANCIAL = "Financial Information";
	private String ROC_OWNERSHIP = "Ownership Structure";
	private String CIVIL_LITIGATION = "Civil Litigation";
	private String CRIMINAL_LITIGATION = "Criminal Litigation";
	private String LITIGATION = "Litigation";
	private String SOURCEAVA_LITIGATION = "Litigation (when it is not discernable whether it is civil/criminal) ";
	private String BANKRUPTCY_COVERED = "Bankruptcy/ Insolvency/ credit checks/";
	private String LAW_ENFO_COVERED = "Law Enforcement Checks";
	private String STOCK_EXCHANGE = "Stock Exchange";
	private String SECURITIES = "Securities Regulator";
	private String CENTRAL_BANK = "Central Bank";
	private String OTHER_CHECKS = "Other Regulatory Checks";
	private String DIR_COVERED = "Directorship Checks";
	private String LBR_DB_NAME = "Name Of Database (LBR)";
	private String LBR_DESCRIPTION = "Description Of Content";
	private String LBR_TABLE = "LBR Table";
	private String POSITION = "Numbering";
	private String LBR_GROUP = "LBR Group";
	private ICountryDB countryDBManager = null;

	public void setCountryDBManager(ICountryDB countryDBManager) {
		this.countryDBManager = countryDBManager;
	}

	public ModelAndView countryDatabase(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in CountryDatabase on load");
		ModelAndView modelAndView = new ModelAndView(this.JSP);
		if (null != request.getSession().getAttribute(this.DELETE_SUCCESS)) {
			modelAndView.addObject(this.DELETE_SUCCESS, "true");
			request.getSession().removeAttribute(this.DELETE_SUCCESS);
		} else if (null != request.getSession().getAttribute(this.UPDATE_SUCCESS)) {
			modelAndView.addObject(this.UPDATE_SUCCESS, "true");
			request.getSession().removeAttribute(this.UPDATE_SUCCESS);
		} else if (null != request.getSession().getAttribute(this.ADD_SUCCESS)) {
			modelAndView.addObject(this.ADD_SUCCESS, "true");
			request.getSession().removeAttribute(this.ADD_SUCCESS);
		}

		return modelAndView;
	}

	public ModelAndView addCountryDatabase(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in addCountryDatabase for loading add page");
		ModelAndView modelAndView = new ModelAndView(this.ADD_JSP);
		return modelAndView;
	}

	public ModelAndView getCountryDatabase(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in getCountryDatabase");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug(" other request values :: " + request.getParameter(this.DB_NAME) + " :: "
					+ request.getParameter(this.DB_CATEGORY) + " :: " + request.getParameter(this.DB_COUNTRY) + " :: "
					+ request.getParameter(this.EXPORT_TYPE));
			countryDatabaseMasterVO.setCountry(request.getParameter(this.DB_COUNTRY));
			String lbrParam = request.getParameter(this.EXPORT_TYPE);
			CountryDatabaseMasterVO countryDBMasterVO = null;
			if (null != lbrParam && lbrParam.trim().length() > 0 && null != request.getParameter(this.DB_NAME)
					&& request.getParameter(this.DB_NAME).trim().length() > 0
					&& null != request.getParameter(this.DB_CATEGORY)
					&& request.getParameter(this.DB_CATEGORY).trim().length() > 0
					&& null != request.getParameter(this.DB_COUNTRY)
					&& request.getParameter(this.DB_COUNTRY).trim().length() > 0) {
				modelAndView = new ModelAndView(this.ADD_JSP);
				if (lbrParam.equalsIgnoreCase(this.CDB)) {
					countryDatabaseMasterVO.setNameOfDatabase(request.getParameter(this.DB_NAME));
					countryDatabaseMasterVO.setCategory(request.getParameter(this.DB_CATEGORY));
					countryDBMasterVO = this.countryDBManager.getCDBCountryDB(countryDatabaseMasterVO);
					countryDBMasterVO.setLbrTable(lbrParam);
				} else {
					countryDatabaseMasterVO.setLbrDBName(request.getParameter(this.DB_NAME));
					countryDatabaseMasterVO.setLbrTable(request.getParameter(this.DB_CATEGORY));
					countryDBMasterVO = this.countryDBManager.getLBRCountryDB(countryDatabaseMasterVO);
					countryDBMasterVO.setLbrTable(lbrParam);
				}
			} else {
				this.logger.debug("lbrParam or other required params in request are null");
				modelAndView = new ModelAndView(this.REDIRECT_COUNTRYDB_MASTER);
			}

			if (null != countryDBMasterVO) {
				this.logger.debug("returning countryDBMasterVO :: " + countryDBMasterVO);
				modelAndView.addObject("countryDatabaseMasterVO", countryDBMasterVO);
			} else {
				modelAndView = new ModelAndView(this.REDIRECT_COUNTRYDB_MASTER);
			}

			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView addCountryDatabaseAndBack(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in addCountryDatabaseAndBack");
		ModelAndView modelAndView = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("getCompanyLbr() : " + countryDatabaseMasterVO.getCompanyLbr()
					+ " : getIndividualLbr() : " + countryDatabaseMasterVO.getIndividualLbr() + " : getLbrTable() : "
					+ countryDatabaseMasterVO.getLbrTable());
			countryDatabaseMasterVO.setUpdatedBy(userBean.getUserName());
			this.countryDBManager.addNewCountryDB(countryDatabaseMasterVO);
			modelAndView = new ModelAndView(this.REDIRECT_COUNTRYDB_MASTER);
			request.getSession().setAttribute(this.ADD_SUCCESS, "true");
			this.logger.info(" Successfully added country database :: " + countryDatabaseMasterVO.getNameOfDatabase());
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView updateCountryDatabase(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in UpdateCountryDatabase :: " + countryDatabaseMasterVO.getCountryDBMasterId());
		ModelAndView modelAndView = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			countryDatabaseMasterVO.setUpdatedBy(userBean.getUserName());
			this.countryDBManager.updateCountryDatabase(countryDatabaseMasterVO);
			this.logger
					.info(" Successfully updated country database :: " + countryDatabaseMasterVO.getNameOfDatabase());
			modelAndView = new ModelAndView(this.REDIRECT_COUNTRYDB_MASTER);
			request.getSession().setAttribute(this.UPDATE_SUCCESS, "true");
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView deleteCountryDatabase(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) throws Exception {
		this.logger.debug("in deleteCountryDatabase :: " + countryDatabaseMasterVO.getCountryDBMasterId());
		ModelAndView modelAndView = null;

		try {
			this.countryDBManager.deleteCountryDatabase(countryDatabaseMasterVO);
			this.logger.info("Deleted successfully country database :: " + countryDatabaseMasterVO.getNameOfDatabase());
			modelAndView = new ModelAndView(this.REDIRECT_COUNTRYDB_MASTER);
			request.getSession().setAttribute(this.DELETE_SUCCESS, "true");
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView sourceAvailabilityCountryDatabase(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in sourceAvailabilityCountryDatabase");

		try {
			List<CountryDatabaseMasterVO> resultList = this.countryDBManager.sourceAvailability();
			this.writeToExcel(resultList, response);
		} catch (CMSException var4) {
			return AtlasUtils.getExceptionView(this.logger, var4);
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return new ModelAndView(this.JSP);
	}

	private void writeToExcel(List<CountryDatabaseMasterVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToExcel ");
		List<String> lstHeader = this.getHeaderList();
		List<String> lstFirstHeader = this.getFirstHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		CountryDatabaseMasterVO countryDatabaseMasterVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				countryDatabaseMasterVO = (CountryDatabaseMasterVO) iterator.next();
				this.populateDataMap(countryDatabaseMasterVO, datamap);
				dataList.add(datamap);
			}
		} catch (UnsupportedOperationException var9) {
			throw new CMSException(this.logger, var9);
		} catch (ClassCastException var10) {
			throw new CMSException(this.logger, var10);
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (IllegalArgumentException var12) {
			throw new CMSException(this.logger, var12);
		}

		SourceAvailabilityExcelDownloader.writeToExcel(lstFirstHeader, lstHeader, dataList, this.SOURCE_AVAILABILITY,
				(short) 0, (short) 1, response, this.SOURCE_AVAILABILITY);
	}

	private void populateDataMap(CountryDatabaseMasterVO countryDatabaseMasterVO,
			LinkedHashMap<String, String> datamap) {
		datamap.put(this.COUNTRY, String.valueOf(countryDatabaseMasterVO.getCountry()));
		datamap.put(this.DATABASE_NAME, String.valueOf(countryDatabaseMasterVO.getNameOfDatabase()));
		datamap.put(this.LLM_COVERED, String.valueOf(countryDatabaseMasterVO.getLocalLanguageMedia()));
		datamap.put(this.ROC_INCORPORATION, String.valueOf(countryDatabaseMasterVO.getIncorporationDetail()));
		datamap.put(this.ROC_MANAGEMENT, String.valueOf(countryDatabaseMasterVO.getManagementDetails()));
		datamap.put(this.ROC_OWNERSHIP, String.valueOf(countryDatabaseMasterVO.getOwnership()));
		datamap.put(this.ROC_FINANCIAL, String.valueOf(countryDatabaseMasterVO.getFinancialInformation()));
		datamap.put(this.CIVIL_LITIGATION, String.valueOf(countryDatabaseMasterVO.getCivilLitigation()));
		datamap.put(this.CRIMINAL_LITIGATION, String.valueOf(countryDatabaseMasterVO.getCriminalLitigation()));
		datamap.put(this.SOURCEAVA_LITIGATION, String.valueOf(countryDatabaseMasterVO.getLitigation()));
		datamap.put(this.BANKRUPTCY_COVERED, String.valueOf(countryDatabaseMasterVO.getBankRInsolvencyCovered()));
		datamap.put(this.LAW_ENFO_COVERED, String.valueOf(countryDatabaseMasterVO.getLawEnforcementCovered()));
		datamap.put(this.STOCK_EXCHANGE, String.valueOf(countryDatabaseMasterVO.getStockExchanges()));
		datamap.put(this.SECURITIES, String.valueOf(countryDatabaseMasterVO.getSecurities()));
		datamap.put(this.CENTRAL_BANK, String.valueOf(countryDatabaseMasterVO.getCentralBank()));
		datamap.put(this.OTHER_CHECKS, String.valueOf(countryDatabaseMasterVO.getOthersChecks()));
		datamap.put(this.DIR_COVERED, String.valueOf(countryDatabaseMasterVO.getDirectorship()));
	}

	private List<String> getFirstHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("");
			lstHeader.add("");
			lstHeader.add("Local Language Media Checks");
			lstHeader.add("Registrar of Companies checks");
			lstHeader.add("");
			lstHeader.add("");
			lstHeader.add("");
			lstHeader.add("Litigation checks");
			lstHeader.add("");
			lstHeader.add("");
			lstHeader.add("Bankruptcy/ Insolvency/ credit checks");
			lstHeader.add("Law Enforcement Checks");
			lstHeader.add("Regulatory checks");
			lstHeader.add("");
			lstHeader.add("");
			lstHeader.add("");
			lstHeader.add(this.DIR_COVERED);
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

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add(this.COUNTRY);
			lstHeader.add(this.DATABASE_NAME);
			lstHeader.add(this.LLM_COVERED);
			lstHeader.add(this.ROC_INCORPORATION);
			lstHeader.add(this.ROC_MANAGEMENT);
			lstHeader.add(this.ROC_OWNERSHIP);
			lstHeader.add(this.ROC_FINANCIAL);
			lstHeader.add(this.CIVIL_LITIGATION);
			lstHeader.add(this.CRIMINAL_LITIGATION);
			lstHeader.add(this.SOURCEAVA_LITIGATION);
			lstHeader.add(this.BANKRUPTCY_COVERED);
			lstHeader.add(this.LAW_ENFO_COVERED);
			lstHeader.add(this.STOCK_EXCHANGE);
			lstHeader.add(this.SECURITIES);
			lstHeader.add(this.CENTRAL_BANK);
			lstHeader.add(this.OTHER_CHECKS);
			lstHeader.add(this.DIR_COVERED);
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

	public ModelAndView exportToExcelCountryDatabase(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("in exportToExcelCountryDatabase :: " + request.getParameter(this.EXPORT_TYPE));
			String excelParams = request.getParameter("excelParams");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			String lbrParam = request.getParameter(this.EXPORT_TYPE);
			List<CountryDatabaseMasterVO> resultList = null;
			if (null != lbrParam && lbrParam.trim().length() > 0) {
				Map resultMap;
				if (lbrParam.equalsIgnoreCase(this.CDB)) {
					resultList = this.countryDBManager.exportToExcelCDB(excelParamMap);
					resultMap = this.writeToExcelCDB(resultList, response);
					modelAndView = new ModelAndView("excelDownloadPopup");
					modelAndView.addObject("fileBytes", resultMap.get("fileBytes"));
					modelAndView.addObject("fileName", resultMap.get("fileName"));
					return modelAndView;
				}

				resultList = this.countryDBManager.exportToExcelLBR(excelParamMap);
				resultMap = this.writeToExcelLBR(resultList, response);
				modelAndView = new ModelAndView("excelDownloadPopup");
				modelAndView.addObject("fileBytes", resultMap.get("fileBytes"));
				modelAndView.addObject("fileName", resultMap.get("fileName"));
				return modelAndView;
			}

			new ArrayList();
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}

		return new ModelAndView(this.JSP);
	}

	private Map<String, Object> writeToExcelCDB(List<CountryDatabaseMasterVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToExcel ");
		List<String> lstHeader = this.getCDBHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		CountryDatabaseMasterVO countryDatabaseMasterVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				countryDatabaseMasterVO = (CountryDatabaseMasterVO) iterator.next();
				this.populateCDBDataMap(countryDatabaseMasterVO, datamap);
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

		return ExcelDownloader.writeToExcel(lstHeader, dataList, this.SEARCH_CDB, (short) 0, (short) 1, response,
				this.SEARCH_CDB);
	}

	private Map<String, Object> writeToExcelLBR(List<CountryDatabaseMasterVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToExcel ");
		List<String> lstHeader = this.getLBRHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		CountryDatabaseMasterVO countryDatabaseMasterVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				countryDatabaseMasterVO = (CountryDatabaseMasterVO) iterator.next();
				this.populateLBRDataMap(countryDatabaseMasterVO, datamap);
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

		return ExcelDownloader.writeToExcel(lstHeader, dataList, this.SEARCH_LBR, (short) 0, (short) 1, response,
				this.SEARCH_LBR);
	}

	private void populateCDBDataMap(CountryDatabaseMasterVO countryDatabaseMasterVO,
			LinkedHashMap<String, String> datamap) {
		datamap.put(this.COUNTRY, String.valueOf(countryDatabaseMasterVO.getCountry()));
		datamap.put(this.CATEGORY, String.valueOf(countryDatabaseMasterVO.getCategory()));
		datamap.put(this.DATABASE_NAME, String.valueOf(countryDatabaseMasterVO.getNameOfDatabase()));
		datamap.put(this.LINK, String.valueOf(countryDatabaseMasterVO.getLink()));
		datamap.put(this.TYPE, String.valueOf(countryDatabaseMasterVO.getType()));
		datamap.put(this.SEARCH_METHOD, String.valueOf(countryDatabaseMasterVO.getSearchMethodology()));
		datamap.put(this.DESCRIPTION, String.valueOf(countryDatabaseMasterVO.getDescription()));
		datamap.put(this.LANGUAGE, String.valueOf(countryDatabaseMasterVO.getLanguage()));
		datamap.put(this.REMARKS, String.valueOf(countryDatabaseMasterVO.getRemarks()));
		datamap.put(this.LLM_COVERED, String.valueOf(countryDatabaseMasterVO.getLocalLanguageMedia()));
		datamap.put(this.ROC_INCORPORATION, String.valueOf(countryDatabaseMasterVO.getIncorporationDetail()));
		datamap.put(this.ROC_MANAGEMENT, String.valueOf(countryDatabaseMasterVO.getManagementDetails()));
		datamap.put(this.ROC_FINANCIAL, String.valueOf(countryDatabaseMasterVO.getFinancialInformation()));
		datamap.put(this.ROC_OWNERSHIP, String.valueOf(countryDatabaseMasterVO.getOwnership()));
		datamap.put(this.CIVIL_LITIGATION, String.valueOf(countryDatabaseMasterVO.getCivilLitigation()));
		datamap.put(this.CRIMINAL_LITIGATION, String.valueOf(countryDatabaseMasterVO.getCriminalLitigation()));
		datamap.put(this.LITIGATION, String.valueOf(countryDatabaseMasterVO.getLitigation()));
		datamap.put(this.BANKRUPTCY_COVERED, String.valueOf(countryDatabaseMasterVO.getBankRInsolvencyCovered()));
		datamap.put(this.LAW_ENFO_COVERED, String.valueOf(countryDatabaseMasterVO.getLawEnforcementCovered()));
		datamap.put(this.STOCK_EXCHANGE, String.valueOf(countryDatabaseMasterVO.getStockExchanges()));
		datamap.put(this.SECURITIES, String.valueOf(countryDatabaseMasterVO.getSecurities()));
		datamap.put(this.CENTRAL_BANK, String.valueOf(countryDatabaseMasterVO.getCentralBank()));
		datamap.put(this.OTHER_CHECKS, String.valueOf(countryDatabaseMasterVO.getOthersChecks()));
		datamap.put(this.DIR_COVERED, String.valueOf(countryDatabaseMasterVO.getDirectorship()));
	}

	private List<String> getCDBHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add(this.COUNTRY);
			lstHeader.add(this.CATEGORY);
			lstHeader.add(this.DATABASE_NAME);
			lstHeader.add(this.LINK);
			lstHeader.add(this.TYPE);
			lstHeader.add(this.SEARCH_METHOD);
			lstHeader.add(this.DESCRIPTION);
			lstHeader.add(this.LANGUAGE);
			lstHeader.add(this.REMARKS);
			lstHeader.add(this.LLM_COVERED);
			lstHeader.add(this.ROC_INCORPORATION);
			lstHeader.add(this.ROC_MANAGEMENT);
			lstHeader.add(this.ROC_FINANCIAL);
			lstHeader.add(this.ROC_OWNERSHIP);
			lstHeader.add(this.CIVIL_LITIGATION);
			lstHeader.add(this.CRIMINAL_LITIGATION);
			lstHeader.add(this.LITIGATION);
			lstHeader.add(this.BANKRUPTCY_COVERED);
			lstHeader.add(this.LAW_ENFO_COVERED);
			lstHeader.add(this.STOCK_EXCHANGE);
			lstHeader.add(this.SECURITIES);
			lstHeader.add(this.CENTRAL_BANK);
			lstHeader.add(this.OTHER_CHECKS);
			lstHeader.add(this.DIR_COVERED);
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

	private void populateLBRDataMap(CountryDatabaseMasterVO countryDatabaseMasterVO,
			LinkedHashMap<String, String> datamap) {
		datamap.put(this.COUNTRY, String.valueOf(countryDatabaseMasterVO.getCountry()));
		datamap.put(this.LBR_GROUP, String.valueOf(countryDatabaseMasterVO.getCategory()));
		datamap.put(this.LBR_DB_NAME, String.valueOf(countryDatabaseMasterVO.getLbrDBName()));
		datamap.put(this.LBR_TABLE, String.valueOf(countryDatabaseMasterVO.getLbrTable()));
		datamap.put(this.LBR_DESCRIPTION, String.valueOf(countryDatabaseMasterVO.getLbrDescription()));
		datamap.put(this.POSITION, String.valueOf(countryDatabaseMasterVO.getPosition()));
	}

	private List<String> getLBRHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add(this.COUNTRY);
			lstHeader.add(this.LBR_GROUP);
			lstHeader.add(this.LBR_DB_NAME);
			lstHeader.add(this.LBR_TABLE);
			lstHeader.add(this.LBR_DESCRIPTION);
			lstHeader.add(this.POSITION);
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
}