package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.masters.PublicHolidayManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.PublicHolidayMasterVO;
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

public class PublicHolidayMultiActionController extends MultiActionController {
	private static final String SAVE = "save";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.PublicHolidayMultiActionController");
	private static final String SUBMIT_TYPE = "submitType";
	private PublicHolidayManager publicHolidayManager;
	private String PUBLIC_HOLIDAY_LIST = "public_holiday_list";
	private String PUBLIC_HOLIDAY_NEW = "public_holiday_new";
	private String BLANK = "";
	private String ACTION = "action";
	private String UPDATE = "update";
	private String PUBLIC_HOLIDAY = "publicHoliday";
	private String resultMsg = "";
	private String office = "Office";
	private String holiday = "Holiday";
	private String date = "Date";
	private String PUBLIC_HOLIDAY_MASTER = "Public Holiday Master";
	private ModelAndView mv = null;

	public void setPublicHolidayManager(PublicHolidayManager publicHolidayManager) {
		this.publicHolidayManager = publicHolidayManager;
	}

	public ModelAndView publicHolidayReportExport(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in publicHolidayReportExport");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			this.logger.debug("excelParams::" + excelParams);
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			List<PublicHolidayMasterVO> holidayDataList = this.publicHolidayManager.getReport(excelParamMap);
			this.logger.debug("fetched reDataList>>Size is " + holidayDataList.size());
			Map<String, Object> resultMap = this.writeToExcel(holidayDataList, response);
			modelandview = new ModelAndView("excelDownloadPopup");
			modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
			modelandview.addObject("fileName", resultMap.get("fileName"));
			this.logger.debug("exiting  Export to Excel BranchOfficeController");
			return modelandview;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	private Map<String, Object> writeToExcel(List<PublicHolidayMasterVO> holidayDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.office);
			lstHeader.add(this.holiday);
			lstHeader.add(this.date);
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = holidayDataList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				PublicHolidayMasterVO publicHolidayMasterVO = (PublicHolidayMasterVO) iterator.next();
				datamap.put(this.office, String.valueOf(publicHolidayMasterVO.getOffice()));
				datamap.put(this.holiday, String.valueOf(publicHolidayMasterVO.getHoliday()));
				datamap.put(this.date, String.valueOf(publicHolidayMasterVO.getHolidayDate()));
				dataList.add(datamap);
			}

			return ExcelDownloader.writeToExcel(lstHeader, dataList, this.PUBLIC_HOLIDAY_MASTER, (short) 0, (short) 1,
					response, this.PUBLIC_HOLIDAY_MASTER);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public ModelAndView addPublicHoliyday(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		try {
			this.mv = new ModelAndView("redirect:publicHolidaySearchJspRedirect.do");
			HttpSession session = request.getSession();
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			publicHolidayMasterVO.setUpdatedBy(userBean.getUserName());
			this.logger.debug("Dated on:" + publicHolidayMasterVO.getHolidayDate());
			if (null != request.getParameter(this.ACTION)) {
				if (this.BLANK.equalsIgnoreCase(request.getParameter(this.ACTION).trim())) {
					this.publicHolidayManager.addPublicHoliday(publicHolidayMasterVO);
					session.setAttribute("save", true);
				} else {
					this.publicHolidayManager.updatePublicHoliday(publicHolidayMasterVO);
					session.setAttribute(this.UPDATE, true);
				}

				if (this.BLANK.equalsIgnoreCase(request.getParameter(this.ACTION).trim())) {
					this.logger.info(publicHolidayMasterVO.getHoliday() + " successfully added.");
				} else {
					this.logger.info(publicHolidayMasterVO.getHoliday() + " successfully updated.");
				}
			}

			return this.mv;
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView getPublicHoliydayInfo(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		try {
			if (null != publicHolidayMasterVO.getHolidayId()
					&& !publicHolidayMasterVO.getHolidayId().equalsIgnoreCase("")) {
				this.mv = new ModelAndView(this.PUBLIC_HOLIDAY_NEW);
				publicHolidayMasterVO = this.publicHolidayManager
						.getPublicHolidayInfo(publicHolidayMasterVO.getHolidayId());
				this.mv.addObject(this.PUBLIC_HOLIDAY, publicHolidayMasterVO);
				this.mv.addObject(this.ACTION, this.UPDATE);
				return this.mv;
			} else {
				return new ModelAndView("redirect:publicHolidaySearchJspRedirect.do");
			}
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView publicHolidayAddUpdateJspRedirect(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		try {
			this.mv = new ModelAndView(this.PUBLIC_HOLIDAY_NEW);
			return this.mv;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}

	public ModelAndView publicHolidaySearchJspRedirect(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		try {
			this.mv = new ModelAndView(this.PUBLIC_HOLIDAY_LIST);
			if (request.getSession().getAttribute("save") != null) {
				this.mv.addObject("submitType", "save");
				request.getSession().removeAttribute("save");
			}

			if (request.getSession().getAttribute(this.UPDATE) != null) {
				this.mv.addObject("submitType", this.UPDATE);
				request.getSession().removeAttribute(this.UPDATE);
			}

			return this.mv;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}
}