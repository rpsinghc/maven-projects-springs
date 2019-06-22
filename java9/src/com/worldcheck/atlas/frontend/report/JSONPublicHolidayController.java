package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.masters.PublicHolidayManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.PublicHolidayMasterVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONPublicHolidayController extends JSONController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONPublicHolidayController");
	private PublicHolidayManager publicHolidayManager = null;
	private String HOLIDAY_LIST = "holidayList";
	private String START = "start";
	private String LIMIT = "limit";
	private String TOTAL = "total";

	public void setPublicHolidayManager(PublicHolidayManager publicHolidayManager) {
		this.publicHolidayManager = publicHolidayManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONPublicHolidayController");
		ModelAndView modelandview = super.handleRequest(request, response);
		if (null != modelandview) {
			return modelandview;
		} else {
			try {
				this.logger.debug(request.getParameter(this.START) + ":: " + request.getParameter(this.LIMIT));
				List<PublicHolidayMasterVO> holidayList = this.publicHolidayManager
						.getCurrentAndNextMonthHolidays(request);
				int totalCount = this.publicHolidayManager.getCurrentAndNextMonthHolidaysCount();
				this.logger.debug("holidayList size :: " + holidayList.size() + " :: totalCount ::" + totalCount);
				modelandview = new ModelAndView("jsonView");
				modelandview.addObject(this.HOLIDAY_LIST, holidayList);
				modelandview.addObject(this.TOTAL, totalCount);
				this.logger.debug("out of JSONPublicHolidayController");
				return modelandview;
			} catch (CMSException var6) {
				return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
			} catch (Exception var7) {
				return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
			}
		}
	}
}