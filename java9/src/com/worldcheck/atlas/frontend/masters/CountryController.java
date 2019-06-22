package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.ICountryMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CountryController extends MultiActionController {
	private static final String SAVE = "save";
	private static final String SUBMIT_TYPE = "submitType";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.masters.CountryController");
	private String JSP_COUNTRY_MASTER = "country_master";
	private String JSP_COUNTRY_UPDATE = "country_new";
	private String STATUS = "Active";
	private String ONE = "1";
	private String ZERO = "0";
	private String ACTION = "action";
	private String BLANK_STRING = "";
	private String COUNTRY_PAGE = "countryPage";
	private String UPDATE = "update";
	private String COUNTRYINFO = "countryInfo";
	private String RESULT = "result";
	private String resultMsg;
	private String countryCode = "Country Code";
	private String country = "Country";
	private String englishCountry = "English-Country";
	private String riskLevel = "Risk Level";
	private String region = "Region";
	private String COUNTRY_MASTER = "Country Master";
	private String status = "Country Status";
	private ModelAndView mv = null;
	private ICountryMaster countryManager;

	public void setCountryManager(ICountryMaster countryManager) {
		this.countryManager = countryManager;
	}

	public ModelAndView addCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		try {
			this.mv = new ModelAndView("redirect:countrySearchJspRedirect.do");
			String message = "Add NEW Country";
			HttpSession session = request.getSession();
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			countryMasterVO.setUpdatedBy(userBean.getUserName());
			if (countryMasterVO.getEnglishCountry() != null) {
				countryMasterVO.setEnglishCountry(this.ONE);
			} else {
				countryMasterVO.setEnglishCountry(this.ZERO);
			}

			this.logger.debug("CountryVo :\n" + countryMasterVO.getCountryCode() + "\ncountry:"
					+ countryMasterVO.getCountry() + "\nriskLevel:" + countryMasterVO.getRiskLevel() + "\nRegion:"
					+ countryMasterVO.getRegion() + "\nEnglish:" + countryMasterVO.getEnglishCountry()
					+ "\nCountryStatus:" + countryMasterVO.getCountryStatus());
			if (this.STATUS.equalsIgnoreCase(countryMasterVO.getCountryStatus())) {
				countryMasterVO.setCountryStatus(this.ONE);
			} else {
				countryMasterVO.setCountryStatus(this.ZERO);
			}

			String resultMessage;
			if (null != request.getParameter(this.ACTION)
					&& !this.BLANK_STRING.equalsIgnoreCase(request.getParameter(this.ACTION).trim())) {
				resultMessage = this.countryManager.updateCountry(countryMasterVO);
				session.setAttribute(this.UPDATE, true);
				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
				}
			} else {
				resultMessage = this.countryManager.addCountry(countryMasterVO);
				this.logger.debug(resultMessage);
				session.setAttribute("save", true);
				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
				}
			}

			ResourceLocator.self().getCacheService().addToCacheRunTime("COUNTRY_MASTER");
			if (null != request.getParameter(this.ACTION)
					&& !this.BLANK_STRING.equalsIgnoreCase(request.getParameter(this.ACTION).trim())) {
				this.logger.info("countryCode:" + countryMasterVO.getCountryCode() + " country:"
						+ countryMasterVO.getCountry() + " successfully updated.");
			} else {
				this.logger.info("countryCode:" + countryMasterVO.getCountryCode() + " country:"
						+ countryMasterVO.getCountry() + " successfully added.");
			}
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}

		return this.mv;
	}

	public ModelAndView selectUpdateCountry(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		ModelAndView mv = null;

		try {
			this.logger.debug("update information countryCode :" + countryMasterVO.getCountryMasterId());
			if (countryMasterVO.getCountryMasterId() == 0) {
				return new ModelAndView("redirect:countrySearchJspRedirect.do");
			} else {
				mv = new ModelAndView(this.JSP_COUNTRY_UPDATE);
				countryMasterVO = this.countryManager.getCountryInfo(countryMasterVO.getCountryMasterId());
				mv.addObject(this.COUNTRY_PAGE, this.UPDATE);
				mv.addObject(this.COUNTRYINFO, countryMasterVO);
				return mv;
			}
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView addUpdateJspRedirect(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		try {
			this.mv = new ModelAndView(this.JSP_COUNTRY_UPDATE);
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}

	public ModelAndView countrySearchJspRedirect(HttpServletRequest request, HttpServletResponse response,
			CountryMasterVO countryMasterVO) {
		try {
			this.mv = new ModelAndView(this.JSP_COUNTRY_MASTER);
			if (request.getSession().getAttribute("save") != null) {
				this.mv.addObject("submitType", "save");
				request.getSession().removeAttribute("save");
			}

			if (request.getSession().getAttribute(this.UPDATE) != null) {
				this.mv.addObject("submitType", this.UPDATE);
				request.getSession().removeAttribute(this.UPDATE);
			}
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}

	public ModelAndView countryReportExport(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in countryReportExport");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			this.logger.debug("excelParams::" + excelParams);
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			List<CountryMasterVO> countryDataList = this.countryManager.getReport(excelParamMap);
			this.logger.debug("fetched reDataList>>Size is " + countryDataList.size());
			Map<String, Object> resultMap = this.writeToExcel(countryDataList, response);
			modelandview = new ModelAndView("excelDownloadPopup");
			modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
			modelandview.addObject("fileName", resultMap.get("fileName"));
			this.logger.debug("exiting  Export to Excel CountryController");
			return modelandview;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	private Map<String, Object> writeToExcel(List<CountryMasterVO> countryDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.countryCode);
			lstHeader.add(this.country);
			lstHeader.add(this.status);
			lstHeader.add(this.englishCountry);
			lstHeader.add(this.region);
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = countryDataList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				CountryMasterVO countryMasterVO = (CountryMasterVO) iterator.next();
				datamap.put(this.countryCode, String.valueOf(countryMasterVO.getCountryCode()));
				datamap.put(this.country, String.valueOf(countryMasterVO.getCountry()));
				datamap.put(this.status, String.valueOf(countryMasterVO.getStatus()));
				datamap.put(this.englishCountry, String.valueOf(countryMasterVO.getEnglishCountry()));
				datamap.put(this.region, String.valueOf(countryMasterVO.getRegion()));
				dataList.add(datamap);
			}

			return ExcelDownloader.writeToExcel(lstHeader, dataList, this.COUNTRY_MASTER, (short) 0, (short) 1,
					response, this.COUNTRY_MASTER);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}
}