package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.interfaces.ICountryDB;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONCountryDBMultiActionController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONCountryDBMultiActionController");
	private String SUCCESS = "success";
	private String TRUE = "true";
	private String FALSE = "false";
	private String COUNTRY = "country";
	private String LBR_DB_NAME = "lbrDBName";
	private String LBR_GROUP = "lbrGroup";
	private String DB_NAME = "dbName";
	private String LBR_DB_LIST = "lbrDBList";
	private String LBR_SEARCH_LIST = "lbrSearchList";
	private String CDB_SEARCH_LIST = "cdbSearchList";
	private String TOTAL = "total";
	private ICountryDB countryDBManager = null;

	public void setCountryDBManager(ICountryDB countryDBManager) {
		this.countryDBManager = countryDBManager;
	}

	public ModelAndView searchCDBCountryDB(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in searchCDBCountryDB");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("getUpdateStartDate :: " + countryDatabaseMasterVO.getUpdateStartDate()
					+ " getUpdateEndDate :: " + countryDatabaseMasterVO.getUpdateEndDate());
			countryDatabaseMasterVO.setSortColumnName(request.getParameter("sort"));
			countryDatabaseMasterVO.setSortType(request.getParameter("dir"));
			List<CountryDatabaseMasterVO> cdbSearchList = this.countryDBManager
					.searchCDBCountryDatabase(countryDatabaseMasterVO);
			int totalCount = this.countryDBManager.searchCDBCountryDatabaseCount(countryDatabaseMasterVO);
			this.logger.debug("cdbSearchList size :: " + cdbSearchList.size() + " total count :: " + totalCount);
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject(this.CDB_SEARCH_LIST, cdbSearchList);
			modelAndView.addObject(this.TOTAL, totalCount);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView searchLBRCountryDB(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in searchLBRCountryDB");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("Before ..getUpdateStartDate :: " + countryDatabaseMasterVO.getUpdateStartDate()
					+ " getUpdateEndDate :: " + countryDatabaseMasterVO.getUpdateEndDate());
			countryDatabaseMasterVO.setSortColumnName(request.getParameter("sort"));
			countryDatabaseMasterVO.setSortType(request.getParameter("dir"));
			List<CountryDatabaseMasterVO> lbrSearchList = this.countryDBManager
					.searchLBRCountryDatabase(countryDatabaseMasterVO);
			int totalCount = this.countryDBManager.searchLBRCountryDatabaseCount(countryDatabaseMasterVO);
			this.logger.debug("lbrSearchList size :: " + lbrSearchList.size() + " total count :: " + totalCount);
			modelAndView.addObject(this.LBR_SEARCH_LIST, lbrSearchList);
			modelAndView.addObject(this.TOTAL, totalCount);
			return modelAndView;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView isCountryDBUnique(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in isCountryDBUnique");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("input parameters:: " + request.getParameter(this.COUNTRY) + " :: "
					+ request.getParameter(this.DB_NAME));
			String country = request.getParameter(this.COUNTRY);
			String dbName = request.getParameter(this.DB_NAME);
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put(this.COUNTRY, country);
			paramMap.put(this.DB_NAME, dbName);
			boolean result = this.countryDBManager.isCountryDBUnique(paramMap);
			this.logger.debug("flag for  isCountryDBUnique :: " + result);
			modelAndView = new ModelAndView("jsonView");
			if (result) {
				modelAndView.addObject(this.SUCCESS, this.TRUE);
			} else {
				modelAndView.addObject(this.SUCCESS, this.FALSE);
			}

			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getLBRDatabases(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in getLBRDatabases");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("country :: " + countryDatabaseMasterVO.getCountry() + " :: master id :: "
					+ countryDatabaseMasterVO.getCountryDBMasterId() + " LBR type :: "
					+ countryDatabaseMasterVO.getLbrTable());
			List<CountryDatabaseMasterVO> lbrDBList = this.countryDBManager.getLBRDatabases(countryDatabaseMasterVO);
			this.logger.debug("lbrDBList size :: " + lbrDBList);
			modelAndView = new ModelAndView("jsonView");
			modelAndView.addObject(this.LBR_DB_LIST, lbrDBList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView isCountryLBRDBUnique(HttpServletRequest request, HttpServletResponse response,
			CountryDatabaseMasterVO countryDatabaseMasterVO) {
		this.logger.debug("in isCountryLBRDBUnique");
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("input parameters:: " + request.getParameter(this.COUNTRY) + " :: "
					+ request.getParameter(this.LBR_DB_NAME) + " :: " + request.getParameter(this.LBR_GROUP));
			String country = request.getParameter(this.COUNTRY);
			String lbrDBName = request.getParameter(this.LBR_DB_NAME);
			String lbrGroup = request.getParameter(this.LBR_GROUP);
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put(this.COUNTRY, country);
			paramMap.put(this.LBR_DB_NAME, lbrDBName);
			if (lbrGroup != null && Integer.parseInt(lbrGroup) == 3) {
				this.logger.debug("in if when 3");
				paramMap.put(this.LBR_GROUP, "1,2");
			} else {
				this.logger.debug(" in else when not 3");
				paramMap.put(this.LBR_GROUP, lbrGroup);
			}

			boolean result = this.countryDBManager.isCountryLBRDBUnique(paramMap);
			this.logger.debug("flag for  isCountryDBUnique :: " + result);
			modelAndView = new ModelAndView("jsonView");
			if (result) {
				modelAndView.addObject(this.SUCCESS, this.TRUE);
			} else {
				modelAndView.addObject(this.SUCCESS, this.FALSE);
			}

			return modelAndView;
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}
}