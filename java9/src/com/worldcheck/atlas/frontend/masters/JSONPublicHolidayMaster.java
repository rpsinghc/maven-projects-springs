package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.masters.PublicHolidayManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.PublicHolidayMasterVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONPublicHolidayMaster extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONPublicHolidayMaster");
	private String PUBLICHOLIDAYGRIDLIST = "publicHolidayGridList";
	private String YEARLIST = "yearList";
	private String HOLIDAY_ID = "holidayId";
	private String BLANK = "";
	private String MESSAGE = "message";
	private String RESULT = "result";
	private String SUCCESS = "success";
	private String FAIL = "fail";
	private String TRUE = "true";
	private String FALSE = "false";
	private String ACTION = "action";
	private String resultData;
	private int count;
	private String COUNT = "count";
	private ModelAndView mv = null;
	private String years;
	private PublicHolidayManager publicHolidayManager;

	public void setYears(String years) {
		this.years = years;
	}

	public void setPublicHolidayManager(PublicHolidayManager publicHolidayManager) {
		this.publicHolidayManager = publicHolidayManager;
	}

	public ModelAndView getYearList(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");
		viewForJson.addObject(this.YEARLIST, this.publicHolidayManager.getYearList(this.years));
		return viewForJson;
	}

	public ModelAndView deletePublicHoliday(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			String holidayId = request.getParameter(this.HOLIDAY_ID);
			if (this.BLANK.equals(holidayId)) {
				mv.addObject(this.MESSAGE, this.FAIL);
			} else {
				this.resultData = this.resultData + this.publicHolidayManager.deletePublicHoliday(holidayId);
				mv.addObject(this.MESSAGE, this.resultData);
				mv.addObject(this.RESULT, this.SUCCESS);
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView searchPublicHoliyday(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		this.mv = new ModelAndView("jsonView");

		try {
			ArrayList publicHolidayGridList = null;
			if (this.BLANK.equals(publicHolidayMasterVO.getOffice())
					&& this.BLANK.equals(publicHolidayMasterVO.getHolidayYear())) {
				publicHolidayGridList = new ArrayList();
			} else {
				publicHolidayGridList = (ArrayList) this.publicHolidayManager.getPublicHolidayGrid(
						publicHolidayMasterVO, Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				this.count = this.publicHolidayManager.getPublicHolidayGridCount(publicHolidayMasterVO);
				this.mv.addObject("total", this.count);
			}

			this.mv.addObject(this.PUBLICHOLIDAYGRIDLIST, publicHolidayGridList);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView isPublicHoliydayExist(HttpServletRequest request, HttpServletResponse response,
			PublicHolidayMasterVO publicHolidayMasterVO) {
		this.mv = new ModelAndView("jsonView");

		try {
			ArrayList publicHolidayGridList = null;
			publicHolidayMasterVO.setHolidayDate(publicHolidayMasterVO.getHolidayDate().substring(0,
					publicHolidayMasterVO.getHolidayDate().indexOf(84)));
			this.logger.debug("OfficeId :" + publicHolidayMasterVO.getOfficeId() + "Holiday Date:"
					+ publicHolidayMasterVO.getHolidayDate());
			publicHolidayGridList = (ArrayList) this.publicHolidayManager.isPublicHolidayExist(publicHolidayMasterVO);
			this.mv.addObject(this.PUBLICHOLIDAYGRIDLIST, publicHolidayGridList);
			this.mv.addObject(this.COUNT, publicHolidayGridList.size());
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}
}